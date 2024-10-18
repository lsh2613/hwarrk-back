package com.hwarrk.common.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostFilterSearchReq {

    private String positionType;
    private String wayType;
    private String skillType;
    private String filterType;
    private String keyWord;
}
