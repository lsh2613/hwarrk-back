package com.hwarrk.repository;

import com.hwarrk.entity.MemberLike;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberLikeRepositoryCustom {
    List<MemberLike> getMemberLikeSliceInfo(Long memberId, Long lastMemberLikeId, Pageable pageable);
}
