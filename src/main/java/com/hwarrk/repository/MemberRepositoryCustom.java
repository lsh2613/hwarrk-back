package com.hwarrk.repository;

import com.hwarrk.common.dto.dto.MemberWithLikeDto;
import com.hwarrk.common.constant.SkillType;
import com.hwarrk.common.dto.dto.ContentWithTotalDto;
import com.hwarrk.common.dto.dto.MemberWithLikeDto;
import com.hwarrk.common.dto.req.ProfileCond;
import com.hwarrk.common.dto.res.ProfileRes;
import com.hwarrk.entity.Member;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepositoryCustom {
    Member getMyProfile(Long memberId);
    ContentWithTotalDto getFilteredMemberPage(Long memberId, ProfileCond cond, Pageable pageable);

    ProfileRes getMemberProfileRes(Long fromMemberId, Long toMemberId);

    List<MemberWithLikeDto> findRecommendedMembers(List<SkillType> skills, Long memberId);
    MemberWithLikeDto getMemberProfileRes(Long fromMemberId, Long toMemberId);
}
