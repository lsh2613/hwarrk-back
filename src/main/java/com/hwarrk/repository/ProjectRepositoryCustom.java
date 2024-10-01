package com.hwarrk.repository;

import com.hwarrk.common.constant.ProjectFilterType;
import com.hwarrk.common.constant.RecruitingType;
import com.hwarrk.entity.Project;
import java.util.List;

public interface ProjectRepositoryCustom {

    List<Project> findFilteredProjects(RecruitingType recruitingType, ProjectFilterType filterType, String keyWord, Long memberId);
    List<Project> findRecommendedProjects(Long memberId);
}
