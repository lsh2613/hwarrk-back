package com.hwarrk.common.dto.res;

import com.hwarrk.entity.Career;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record CareerRes(
        long careerId,
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

    public static CareerRes createRes(Career career) {
        return CareerRes.builder()
                .careerId(career.getId())
                .company(career.getCompany())
                .domain(career.getDomain())
                .job(career.getJob())
                .startDate(career.getStartDate())
                .endDate(career.getEndDate())
                .description(career.getDescription())
                .build();
    }
}
