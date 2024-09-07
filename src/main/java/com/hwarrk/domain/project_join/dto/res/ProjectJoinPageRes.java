package com.hwarrk.domain.project_join.dto.res;

import com.hwarrk.domain.project_join.entity.ProjectJoin;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record ProjectJoinPageRes(
        List<ProjectJoinRes> ProjectJoinResList,
        long totalElements,
        int totalPages,
        boolean isLast
) {
    public static ProjectJoinPageRes mapPageToPageRes(Page<ProjectJoin> pages) {
        return ProjectJoinPageRes.builder()
                .ProjectJoinResList(
                        pages.getContent().stream()
                                .map(ProjectJoinRes::mapEntityToRes)
                                .toList()
                )
                .totalElements(pages.getTotalElements())
                .totalPages(pages.getTotalPages())
                .isLast(pages.isLast())
                .build();
    }
}
