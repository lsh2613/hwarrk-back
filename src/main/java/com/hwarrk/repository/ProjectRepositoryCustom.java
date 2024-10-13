package com.hwarrk.repository;

import com.hwarrk.common.constant.ProjectFilterType;
import com.hwarrk.common.constant.RecruitingType;
import com.hwarrk.common.dto.dto.ProjectWithLikeDto;
import java.util.List;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public interface ProjectRepositoryCustom {

    PageImpl<ProjectWithLikeDto> findFilteredProjects(RecruitingType recruitingType, ProjectFilterType filterType, String keyWord, Long memberId, Pageable pageable);
    List<ProjectWithLikeDto> findRecommendedProjects(Long memberId);
}
