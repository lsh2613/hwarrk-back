package com.hwarrk.common.dto.req;

import com.hwarrk.common.constant.MemberStatus;
import com.hwarrk.common.constant.PositionType;
import com.hwarrk.common.constant.FilterType;
import com.hwarrk.common.constant.SkillType;

public record ProfileCond(
        PositionType positionType,
        SkillType skillType,
        MemberStatus memberStatus,
        FilterType filterType,
        String keyword
) {
}
