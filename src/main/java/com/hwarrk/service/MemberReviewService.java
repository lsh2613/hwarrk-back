package com.hwarrk.service;

import com.hwarrk.common.dto.req.ReviewCreateReq;

public interface MemberReviewService {
    void createReview(Long projectId, Long fromMemberId, Long toMemberId, ReviewCreateReq req);
}
