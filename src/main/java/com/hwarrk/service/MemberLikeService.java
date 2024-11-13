package com.hwarrk.service;

import com.hwarrk.common.constant.LikeType;
import com.hwarrk.common.dto.res.MemberCardRes;
import com.hwarrk.common.dto.res.SliceRes;
import org.springframework.data.domain.Pageable;

public interface MemberLikeService {
    void likeMember(Long loginId, Long memberId, LikeType likeType);
    SliceRes<MemberCardRes> getMyLikedMemberCards(Long memberId, Long lastMemberLikeId, Pageable pageable);
}
