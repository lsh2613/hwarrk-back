package com.hwarrk.service;

import com.hwarrk.common.constant.LikeType;
import com.hwarrk.common.dto.res.SliceRes;
import org.springframework.data.domain.Pageable;

public interface ProjectLikeService {
    void likeProject(Long memberId, Long projectId, LikeType likeType);
    SliceRes getMyLikedProjectCards(Long loginId, Long lastProjectLikeId, Pageable pageable);
}
