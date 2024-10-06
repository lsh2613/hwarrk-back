package com.hwarrk.common.dto.dto;

import java.util.List;

public record ContentWithTotalDto(
        List<MemberWithLikeDto> memberPage,
        long total
) {
}
