package com.hwarrk.common.dto.res;

import com.hwarrk.entity.Career;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CareerRes(
        String company,
        String domain, // 직군
        String job, // 직무
        LocalDate startDate,
        LocalDate endDate,
        String description
) {
    public static CareerRes mapEntityToRes(Career careerEntity) {
        return CareerRes.builder()
                .company(careerEntity.getCompany())
                .domain(careerEntity.getDomain())
                .job(careerEntity.getJob())
                .startDate(careerEntity.getStartDate())
                .endDate(careerEntity.getEndDate())
                .description(careerEntity.getDescription())
                .build();
    }
}
