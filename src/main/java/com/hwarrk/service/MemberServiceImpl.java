package com.hwarrk.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.hwarrk.common.EntityFacade;
import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.common.constant.Role;
import com.hwarrk.common.constant.TokenType;
import com.hwarrk.common.dto.dto.ContentWithTotalDto;
import com.hwarrk.common.dto.req.ProfileCond;
import com.hwarrk.common.dto.req.ProfileUpdateReq;
import com.hwarrk.common.dto.res.CareerInfoRes;
import com.hwarrk.common.dto.res.MemberRes;
import com.hwarrk.common.dto.res.MyProfileRes;
import com.hwarrk.common.dto.res.PageRes;
import com.hwarrk.common.dto.res.ProfileRes;
import com.hwarrk.common.exception.GeneralHandler;
import com.hwarrk.entity.*;
import com.hwarrk.jwt.TokenProvider;
import com.hwarrk.redis.RedisUtil;
import com.hwarrk.repository.MemberRepository;
import com.hwarrk.repository.MemberRepositoryCustom;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final MemberRepositoryCustom memberRepositoryCustom;
    private final EntityFacade entityFacade;
    private final TokenProvider tokenProvider;
    private final RedisUtil redisUtil;
    private final S3Uploader s3Uploader;

    @Override
    public void deleteMember(Long memberId) {
        Member member = entityFacade.getMember(memberId);
        memberRepository.delete(member);
    }

    @Override
    public void updateMember(Long loginId, ProfileUpdateReq profileUpdateReq, MultipartFile image) {
        Member member = entityFacade.getMember(loginId);

        String imageUrl = updateMemberImage(image, member);

        List<Position> positions = profileUpdateReq.positions() == null ?
                Collections.emptyList() : profileUpdateReq.positions().stream().map(positionType -> new Position(positionType, member)).toList();

        List<Portfolio> portfolios = profileUpdateReq.portfolios() == null ?
                Collections.emptyList() : profileUpdateReq.portfolios().stream().map(portfolioLink -> new Portfolio(portfolioLink, member)).toList();

        List<Skill> skills = profileUpdateReq.skills() == null ?
                Collections.emptyList() : profileUpdateReq.skills().stream().map(skillType -> new Skill(skillType, member)).toList();

        List<Degree> degrees = profileUpdateReq.degrees() == null ?
                Collections.emptyList() : profileUpdateReq.degrees().stream().map(degreeReq -> degreeReq.mapReqToEntity(member)).toList();

        List<Career> careers = profileUpdateReq.careers() == null ?
                Collections.emptyList() : profileUpdateReq.careers().stream().map(careerReq -> careerReq.mapReqToEntity(member)).toList();

        List<ProjectDescription> projectDescriptions = profileUpdateReq.projectDescriptions() == null ?
                Collections.emptyList() : profileUpdateReq.projectDescriptions().stream()
                .map(projectDescriptionUpdateReq -> {
                    Project project = entityFacade.getProject(projectDescriptionUpdateReq.projectId());
                    return projectDescriptionUpdateReq.mapReqToEntity(member, project);
                })
                .toList();

        List<ExternalProjectDescription> externalProjectDescriptions = profileUpdateReq.externalProjectDescriptions() == null ?
                Collections.emptyList() : profileUpdateReq.externalProjectDescriptions().stream()
                .map(externalProjectDescriptionUpdateReq -> externalProjectDescriptionUpdateReq.mapReqToEntity(member))
                .toList();

        Member updatedMember = profileUpdateReq.mapReqToMember(imageUrl, positions, portfolios, skills, degrees, careers, projectDescriptions, externalProjectDescriptions);
        member.updateMember(updatedMember);
    }

    @Override
    public MyProfileRes getMyProfile(Long memberId) {
        Member member = memberRepositoryCustom.getMyProfile(memberId);
        memberRepository.increaseViews(memberId);

        return MyProfileRes.mapEntityToRes(member);
    }

    @Override
    public ProfileRes getProfile(Long fromMemberId, Long toMemberId) {
        Member fromMember = entityFacade.getMember(fromMemberId);
        Member toMember = entityFacade.getMember(toMemberId);

        if (fromMember.getRole() == Role.GUEST)
            throw new GeneralHandler(ErrorStatus.GUEST_ROLE_FORBIDDEN);

        if (toMember.getIsVisible() == false)
            throw new GeneralHandler(ErrorStatus.PROFILE_NOT_VISIBLE);

        ProfileRes res = memberRepositoryCustom.getMemberProfileRes(fromMemberId, toMemberId);
        memberRepository.increaseViews(toMember.getId());

        return res;
    }

    @Override
    public PageRes getMembers(Long memberId, ProfileCond cond, Pageable pageable) {
        ContentWithTotalDto memberPageWithTotalDto = memberRepositoryCustom.getFilteredMemberPage(memberId, cond, pageable);

        List<MemberRes> memberRes = memberPageWithTotalDto.memberPage().stream().map(MemberWithLikeDto -> {
                    Member member = MemberWithLikeDto.member();
                    CareerInfoRes careerInfoRes = CareerInfoRes.mapEntityToRes(member.loadCareer());
                    return MemberRes.mapEntityToRes(member, careerInfoRes, MemberWithLikeDto.isLiked());
                })
                .toList();

        PageImpl<MemberRes> memberResPage = new PageImpl<>(memberRes, pageable, memberPageWithTotalDto.total());

        return PageRes.mapResToPageRes(memberResPage);
    }

    private String updateMemberImage(MultipartFile image, Member member) {
        String memberImage = member.getImage();
        if (image != null) {
            if (memberImage != null) {
                s3Uploader.deleteImg(memberImage);
            }
            String uploadedImg = s3Uploader.uploadImg(image);
            return uploadedImg;
        }
        return memberImage;
    }

    @Override
    public void logout(HttpServletRequest request) {
        String accessToken = tokenProvider.extractToken(request, TokenType.ACCESS_TOKEN);
        addToBlackList(accessToken);

        String refreshToken = tokenProvider.extractToken(request, TokenType.REFRESH_TOKEN);
        redisUtil.deleteData(refreshToken);
    }

    private void addToBlackList(String accessToken) {
        DecodedJWT decodedAccessToken = tokenProvider.decodedJWT(accessToken);

        Long accessTokenId = decodedAccessToken.getClaim("id").asLong();
        Date expiresAt = decodedAccessToken.getExpiresAt();
        long diff = expiresAt.getTime() - System.currentTimeMillis();

        redisUtil.setBlackList(accessToken, accessTokenId, diff);
    }
}
