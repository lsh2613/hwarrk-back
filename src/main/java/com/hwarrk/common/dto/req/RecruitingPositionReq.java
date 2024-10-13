package com.hwarrk.common.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class RecruitingPositionReq {
    private String positionType;
    private int number;
}
