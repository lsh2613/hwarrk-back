package com.hwarrk.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.hwarrk.common.EntityFacade;
import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.common.constant.PositionType;
import com.hwarrk.common.constant.Role;
import com.hwarrk.common.constant.SkillType;
import com.hwarrk.common.constant.TokenType;
import com.hwarrk.common.dto.dto.ContentWithTotalDto;
import com.hwarrk.common.dto.dto.MemberWithLikeDto;
import com.hwarrk.common.dto.req.ProfileCond;
import com.hwarrk.common.dto.req.UpdateProfileReq;
import com.hwarrk.common.dto.res.*;
import com.hwarrk.common.exception.GeneralHandler;
import com.hwarrk.entity.*;
import com.hwarrk.jwt.TokenProvider;
import com.hwarrk.redis.RedisUtil;
import com.hwarrk.repository.MemberRepository;
import com.hwarrk.repository.MemberRepositoryCustom;
import jakarta.servlet.http.HttpServletRequest;

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
    public void updateMember(Long loginId, UpdateProfileReq updateProfileReq, MultipartFile image) {
        Member member = entityFacade.getMember(loginId);

        updateMemberImage(image, member);

        List<ProjectDescription> projectDescriptions = getProjectDescriptions(updateProfileReq, member);
        updateProfileReq.updateMember(member, projectDescriptions);
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

        MemberWithLikeDto memberWithLikeDto = memberRepositoryCustom.getMemberProfileRes(fromMemberId, toMemberId);
        Member member = memberWithLikeDto.member();
        boolean isLiked = memberWithLikeDto.isLiked();

        List<String> portfolios = member.getPortfolios().stream().map(Portfolio::getLink).toList();
        List<PositionType> positions = member.getPositions().stream().map(Position::getPositionType).toList();
        List<SkillType> skills = member.getSkills().stream().map(Skill::getSkillType).toList();
        List<DegreeRes> degrees = member.getDegrees().stream().map(DegreeRes::mapEntityToRes).toList();
        List<CareerRes> careers = member.getCareers().stream().map(CareerRes::createRes).toList();
        List<ProjectDescriptionRes> projectDescriptions = member.getProjectDescriptions().stream().map(ProjectDescriptionRes::mapEntityToRes).toList();
        List<MemberReviewRes> memberReviews = member.loadPositiveReviewInfo().stream().map(MemberReviewRes::createRes).toList();
        double embers = member.loadEmbers();

        memberRepository.increaseViews(toMember.getId());

        return ProfileRes.createRes(member, portfolios, positions, skills,
                isLiked, degrees, careers, projectDescriptions, memberReviews, embers);
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

    private List<ProjectDescription> getProjectDescriptions(UpdateProfileReq updateProfileReq, Member member) {
        if (updateProfileReq.projectDescriptions() == null)
            return null;

        return updateProfileReq.projectDescriptions().stream()
                .map(updateProjectDescriptionReq -> {
                    Project project = entityFacade.getProject(updateProjectDescriptionReq.projectId());
                    return updateProjectDescriptionReq.mapReqToEntity(member, project);
                })
                .toList();
    }

    private void updateMemberImage(MultipartFile image, Member member) {
        if (image != null) {
            String memberImage = member.getImage();
            if (memberImage != null) {
                s3Uploader.deleteImg(memberImage);
            }
            String uploadedImg = s3Uploader.uploadImg(image);
            member.setImage(uploadedImg);
        }
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
