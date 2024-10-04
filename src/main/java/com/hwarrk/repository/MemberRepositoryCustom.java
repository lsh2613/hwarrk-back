package com.hwarrk.repository;

import com.hwarrk.common.dto.req.ProfileCond;
import com.hwarrk.common.dto.res.ProfileRes;
import com.hwarrk.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepositoryCustom {
    Member getMyProfile(Long memberId);
    Page getFilteredMemberPage(Long memberId, ProfileCond cond, Pageable pageable);
    ProfileRes getMemberProfileRes(Long fromMemberId, Long toMemberId);
}
