package com.hwarrk.repository;

import com.hwarrk.common.dto.dto.ProjectMemberWithLikeDto;
import com.hwarrk.entity.ProjectMember;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

    @Query("SELECT new com.hwarrk.common.dto.dto.ProjectMemberWithLikeDto(pm, "
            + "CASE WHEN pl.id IS NOT NULL THEN true ELSE false END) "
            + "FROM ProjectMember pm "
            + "LEFT JOIN FETCH pm.member psm "
            + "LEFT JOIN FETCH psm.projectLikes pl "
            + "WHERE pm.project.id = :projectId")
    List<ProjectMemberWithLikeDto> findProjectMembersByProjectId(@Param("projectId") Long projectId);
}
