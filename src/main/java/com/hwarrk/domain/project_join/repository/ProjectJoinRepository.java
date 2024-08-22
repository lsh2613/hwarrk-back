package com.hwarrk.domain.project_join.repository;

import com.hwarrk.domain.project_join.entity.ProjectJoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectJoinRepository extends JpaRepository<ProjectJoin, Long> {
}
