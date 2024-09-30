package com.hwarrk.repository;

import com.hwarrk.entity.Project;
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

    @Query("SELECT p FROM Project p " +
            "JOIN FETCH p.post ps " +
            "JOIN FETCH p.projectMembers pm " +
            "JOIN FETCH pm.careerInfo c " +
            "JOIN FETCH pm.member m " +
            "JOIN FETCH m.careers " +
            "WHERE p.id = :id")
    Optional<Project> findSpecificProjectInfoById(@Param("id") Long id);
}
