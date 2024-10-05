package com.hwarrk.repository;

import com.hwarrk.entity.Member;
import com.hwarrk.entity.Project;
import com.hwarrk.entity.ProjectLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectLikeRepository extends JpaRepository<ProjectLike, Long> {
    Optional<ProjectLike> findByMemberAndProject(Member member, Project project);

    @Query("SELECT COUNT(pl) FROM ProjectLike pl WHERE pl.project.id = :projectId")
    int countLikesByProjectId(@Param("projectId") Long projectId);
}
