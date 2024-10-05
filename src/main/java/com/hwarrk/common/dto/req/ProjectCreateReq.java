package com.hwarrk.common.dto.req;

import com.hwarrk.common.constant.StepType;
import com.hwarrk.common.constant.WayType;
import com.hwarrk.entity.Member;
import com.hwarrk.entity.Project;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ProjectCreateReq(
        @NotNull
        String name,
        @NotNull
        String stepType,
        @NotNull
        String domain,
        @NotNull
        LocalDate startDate,
        LocalDate endDate,
        @NotNull
        String wayType,
        @NotNull
        String area,
        @NotNull
        String subject,
        String image,
        @NotNull
        String description
) {
    public Project mapCreateReqToProject(Member member) {
        return Project.builder()
                .name(name)
                .step(StepType.valueOf(stepType))
                .domain(domain)
                .startDate(startDate)
                .endDate(endDate)
                .way(WayType.valueOf(wayType))
                .area(area)
                .subject(subject)
                .image(image)
                .description(description)
                .leader(member)
                .build();
    }
}
