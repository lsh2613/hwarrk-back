package com.hwarrk.repository;

import com.hwarrk.common.SliceCustomImpl;
import org.springframework.data.domain.Pageable;

public interface PostLikeRepositoryCustom {
    SliceCustomImpl getLikedPostSlice(Long loginId, Long lastPostLikeId, Pageable pageable);
}
