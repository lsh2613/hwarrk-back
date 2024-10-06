package com.hwarrk.repository;

import com.hwarrk.entity.PostLike;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostLikeRepositoryCustom {
    List<PostLike> getPostLikeSliceInfo(Long loginId, Long lastPostLikeId, Pageable pageable);
}
