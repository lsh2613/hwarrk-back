package com.hwarrk.service;

import com.hwarrk.common.constant.LikeType;
import com.hwarrk.common.dto.res.SliceRes;
import org.springframework.data.domain.Pageable;

public interface PostLikeService {
    void likePost(Long loginId, Long postId, LikeType likeType);
    SliceRes getMyLikedPostCards(Long loginId, Long lastPostLikeId, Pageable pageable);
}
