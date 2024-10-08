package com.hwarrk.repository;

import com.hwarrk.common.dto.dto.ProjectJoinWithLikeDto;
import com.hwarrk.entity.ProjectJoin;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectJoinRepository extends JpaRepository<ProjectJoin, Long> {
    Optional<ProjectJoin> findByProject_idAndMember_Id(Long projectId, Long memberId);
    Page<ProjectJoin> findAllByMember_IdOrderByCreatedAtDesc(Long memberId, Pageable pageable);
    Page<ProjectJoin> findAllByProject_IdOrderByCreatedAtDesc(Long memberId, Pageable pageable);

    @Query("SELECT new com.hwarrk.common.dto.dto.ProjectJoinWithLikeDto(pj, "
            + "CASE WHEN pl.id IS NOT NULL THEN true ELSE false END) "
            + "FROM ProjectJoin pj "
            + "LEFT JOIN FETCH pj.member pjm "
            + "LEFT JOIN FETCH pjm.projectLikes pl "
            + "WHERE pj.project.id = :projectId")
    List<ProjectJoinWithLikeDto> findProjectJoinsWithProjectId(@Param("projectId") Long projectId);
}
