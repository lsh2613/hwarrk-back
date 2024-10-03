package com.hwarrk.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.hwarrk.common.EntityFacade;
import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.common.dto.req.ProfileCond;
import com.hwarrk.common.dto.req.UpdateProfileReq;
import com.hwarrk.common.dto.res.MemberRes;
import com.hwarrk.common.dto.res.MyProfileRes;
import com.hwarrk.common.dto.res.PageRes;
import com.hwarrk.common.dto.res.ProfileRes;
import com.hwarrk.common.exception.GeneralHandler;
import com.hwarrk.entity.Member;
import com.hwarrk.entity.Project;
import com.hwarrk.entity.ProjectDescription;
import com.hwarrk.jwt.TokenProvider;
import com.hwarrk.redis.RedisUtil;
import com.hwarrk.repository.MemberRepository;
import com.hwarrk.repository.MemberRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

import static com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus.TOKEN_ID_MISMATCH;

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
        Member toMember = entityFacade.getMember(toMemberId);

        if (toMember.getIsVisible() == false)
            throw new GeneralHandler(ErrorStatus.PROFILE_NOT_VISIBLE);

        ProfileRes res = memberRepositoryCustom.getMemberProfileRes(fromMemberId, toMemberId);
        memberRepository.increaseViews(toMember.getId());

        return res;
    }

    @Override
    public PageRes getMembers(Long memberId, ProfileCond cond, Pageable pageable) {
        Page<MemberRes> memberPage = memberRepositoryCustom.getFilteredMemberPage(memberId, cond, pageable);
        return PageRes.mapResToPageRes(memberPage);
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

        member.addProjectDescriptions(projectDescriptions);
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
    public void logout(String accessToken, String refreshToken, Long memberId) {
        DecodedJWT decodedAccessToken = tokenProvider.decodedJWT(accessToken);
        Long accessTokenId = decodedAccessToken.getClaim("id").asLong();

        DecodedJWT decodedRefreshToken = tokenProvider.decodedJWT(refreshToken);
        Long refreshTokenId = decodedRefreshToken.getClaim("id").asLong();

        if (!accessTokenId.equals(memberId) || !refreshTokenId.equals(memberId)) {
            throw new GeneralHandler(TOKEN_ID_MISMATCH);
        }

        Date expiresAt = decodedAccessToken.getExpiresAt();
        long diff = expiresAt.getTime() - System.currentTimeMillis();
        redisUtil.setBlackList(accessToken, accessTokenId, diff);

        redisUtil.deleteData(refreshToken);
    }
}
