package com.hwarrk.common.dto.req;

import com.hwarrk.common.constant.MemberReviewTag;
import com.hwarrk.common.constant.MemberReviewTagType;
import com.hwarrk.entity.Member;
import com.hwarrk.entity.MemberReview;
import com.hwarrk.entity.Project;

public record ReviewCreateReq(
        MemberReviewTag tag,
        MemberReviewTagType type
) {
    public MemberReview mapReqToMember(Project project, Member fromMember, Member toMember) {
        return MemberReview.builder()
                .project(project)
                .fromMember(fromMember)
                .toMember(toMember)
                .tag(tag)
                .tagType(type)
                .build();
    }
}
