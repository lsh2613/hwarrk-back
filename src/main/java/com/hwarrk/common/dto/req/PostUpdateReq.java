package com.hwarrk.common.dto.req;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class PostUpdateReq {

    private long postId;
    private String title;
    private String body;
    private List<RecruitingPositionReq> recruitingPositionReqList;
    private List<String> skills;
}
