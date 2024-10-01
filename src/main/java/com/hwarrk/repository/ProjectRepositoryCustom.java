package com.hwarrk.repository;

import com.hwarrk.common.constant.ProjectFilterType;
import com.hwarrk.common.constant.RecruitingType;
import com.hwarrk.entity.Project;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ProjectRepositoryCustom {

    Slice<Project> findFilteredProjects(RecruitingType recruitingType, ProjectFilterType filterType, String keyWord, Long memberId, Pageable pageable);
    List<Project> findRecommendedProjects(Long memberId);
}
