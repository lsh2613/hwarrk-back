package com.hwarrk.repository;

import com.hwarrk.common.dto.dto.ProjectWithLikeDto;
import com.hwarrk.entity.Project;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT DISTINCT p FROM Project p " +
            "LEFT JOIN FETCH p.post ps " +
            "LEFT JOIN FETCH p.projectMembers pm " +
            "LEFT JOIN FETCH pm.careerInfo c " +
            "LEFT JOIN FETCH pm.member m " +
            "WHERE p.id = :projectId")
    Optional<Project> findSpecificProjectInfoById(@Param("projectId") Long projectId);

    @Query("SELECT new com.hwarrk.common.dto.dto.ProjectWithLikeDto(p, " +
            "CASE WHEN pl.id IS NOT NULL THEN true ELSE false END) " +
            "FROM Project p " +
            "LEFT JOIN ProjectLike pl ON pl.project.id = p.id AND pl.member.id = :memberId ")
    List<ProjectWithLikeDto> findProjectsAndIsLikedByMember(@Param("memberId") Long memberId);

    @Query("SELECT p FROM Project p "
            + "LEFT JOIN FETCH p.post ps "
            + "LEFT JOIN FETCH ps.positions pss "
            + "WHERE p.leader.id = :memberId")
    List<Project> findByLeaderOrderByCreatedAtDesc(@Param("memberId") Long memberId);

    @Query("SELECT DISTINCT p FROM Project p "
            + "LEFT JOIN FETCH p.projectMembers ps "
            + "LEFT JOIN FETCH ps.member psm "
            + "LEFT JOIN FETCH p.projectJoins pj "
            + "LEFT JOIN FETCH pj.member pjm "
            + "WHERE p.id = :projectId")
    Optional<Project> findSpecificProjectDetailsById(@Param("projectId") Long projectId);
}
