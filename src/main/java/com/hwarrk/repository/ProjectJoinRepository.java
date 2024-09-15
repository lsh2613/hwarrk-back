package com.hwarrk.repository;

import com.hwarrk.entity.ProjectJoin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectJoinRepository extends JpaRepository<ProjectJoin, Long> {
    Optional<ProjectJoin> findByProject_idAndMember_Id(Long projectId, Long memberId);
    Page<ProjectJoin> findAllByMember_IdOrderByCreatedAtDesc(Long memberId, Pageable pageable);
    Page<ProjectJoin> findAllByProject_IdOrderByCreatedAtDesc(Long memberId, Pageable pageable);
}
