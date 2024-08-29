package com.hwarrk.domain.project.dto.res;


import com.hwarrk.domain.project.entity.Project;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record ProjectPageRes(
        List<ProjectRes> projectResList,
        long totalElements,
        int totalPages,
        boolean isLast

) {
    public static ProjectPageRes mapPagesToRes(Page<Project> projects) {
        return ProjectPageRes.builder()
                .projectResList(
                        projects.getContent().stream()
                                .map(project -> ProjectRes.mapEntityToRes(project))
                                .toList())
                .totalElements(projects.getTotalElements())
                .totalPages(projects.getTotalPages())
                .isLast(projects.isLast())
                .build();
    }
}
