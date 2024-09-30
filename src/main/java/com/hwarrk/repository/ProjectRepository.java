package com.hwarrk.repository;

import com.hwarrk.common.dto.dto.ProjectWithLikeDto;
import com.hwarrk.entity.Member;
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
            "JOIN FETCH p.post ps " +
            "JOIN FETCH p.projectMembers pm " +
            "JOIN FETCH pm.careerInfo c " +
            "JOIN FETCH pm.member m " +
            "JOIN FETCH m.projectLikes pl " +
            "JOIN FETCH m.careers cs " +
            "WHERE p.id = :id")
    Optional<Project> findSpecificProjectInfoById(@Param("id") Long id);

    @Query("SELECT new com.hwarrk.common.dto.dto.ProjectWithLikeDto(p, " +
            "CASE WHEN pl.id IS NOT NULL THEN true ELSE false END) " +
            "FROM Project p " +
            "LEFT JOIN ProjectLike pl ON pl.project.id = p.id AND pl.member.id = :memberId " +
            "WHERE p IN (SELECT pl.project FROM ProjectLike pl WHERE pl.member.id = :memberId)")
    List<ProjectWithLikeDto> findProjectsAndIsLikedByMember(@Param("memberId") Long memberId);

    @Query("SELECT p FROM Project p "
            + "JOIN FETCH p.post ps "
            + "JOIN FETCH ps.positions pss "
            + "WHERE p.leader.id = :member")
    List<Project> findByLeaderOrderByCreatedAtDesc(@Param("memberId") Long memberId);
}
