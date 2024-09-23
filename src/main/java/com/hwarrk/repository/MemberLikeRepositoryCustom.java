package com.hwarrk.repository;

import com.hwarrk.common.SliceCustomImpl;
import org.springframework.data.domain.Pageable;

public interface MemberLikeRepositoryCustom {
    SliceCustomImpl getLikedMemberSlice(Long memberId, Long lastMemberLikeId, Pageable pageable);
}
