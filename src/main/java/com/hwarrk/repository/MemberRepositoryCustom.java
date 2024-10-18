package com.hwarrk.repository;

import com.hwarrk.common.dto.dto.MemberWithLikeDto;
import com.hwarrk.common.dto.req.ProfileCond;
import com.hwarrk.common.dto.res.ProfileRes;
import com.hwarrk.entity.Member;
import com.hwarrk.common.dto.dto.ContentWithTotalDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepositoryCustom {
    Member getMyProfile(Long memberId);
    ContentWithTotalDto getFilteredMemberPage(Long memberId, ProfileCond cond, Pageable pageable);
    MemberWithLikeDto getMemberProfileRes(Long fromMemberId, Long toMemberId);
}
