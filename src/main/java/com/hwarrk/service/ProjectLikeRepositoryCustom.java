package com.hwarrk.service;

import com.hwarrk.common.SliceCustomImpl;
import org.springframework.data.domain.Pageable;

public interface ProjectLikeRepositoryCustom {
    SliceCustomImpl getLikedProjectSlice(Long memberId, Long lastProjectLikeId, Pageable pageable);
}
