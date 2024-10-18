package com.hwarrk.common.dto.res;

import com.hwarrk.common.constant.PositionType;
import com.hwarrk.entity.ExternalProjectDescription;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ExternalProjectDescriptionRes(
        String name,
        String domain,
        LocalDate startDate,
        LocalDate endDate,
        PositionType positionType,
        String subject,
        String description
) {
    public static ExternalProjectDescriptionRes mapEntityToRes(ExternalProjectDescription externalProjectDescription) {
        return ExternalProjectDescriptionRes.builder()
                .name(externalProjectDescription.getName())
                .domain(externalProjectDescription.getDomain())
                .startDate(externalProjectDescription.getStartDate())
                .endDate(externalProjectDescription.getEndDate())
                .positionType(externalProjectDescription.getPositionType())
                .subject(externalProjectDescription.getSubject())
                .description(externalProjectDescription.getDescription())
                .build();
    }
}
