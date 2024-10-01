package com.hwarrk.common.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProjectFilterSearchReq {

    private String recruitingType;
    private String filterType;
    private String keyWord;
}
