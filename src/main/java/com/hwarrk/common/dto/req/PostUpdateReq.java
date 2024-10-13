package com.hwarrk.common.dto.req;

import com.hwarrk.common.dto.dto.RecruitingPositionDto;
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
    private List<RecruitingPositionDto> recruitingPositionDtoList;
    private List<String> skills;
}
