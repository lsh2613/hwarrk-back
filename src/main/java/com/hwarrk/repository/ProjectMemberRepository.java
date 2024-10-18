package com.hwarrk.repository;

import com.hwarrk.common.dto.dto.MemberWithLikeDto;
import com.hwarrk.common.dto.dto.ProjectMemberWithLikeDto;
import com.hwarrk.common.dto.dto.ProjectWithLikeDto;
import com.hwarrk.entity.Member;
import com.hwarrk.entity.Project;
import com.hwarrk.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

    @Query("SELECT new com.hwarrk.common.dto.dto.MemberWithLikeDto(pm.member, " +
            "CASE WHEN ml.id IS NOT NULL THEN true ELSE false END) " +
            "FROM ProjectMember pm " +
            "LEFT JOIN MemberLike ml ON ml.fromMember.id = :memberId AND ml.toMember.id = pm.member.id " +
            "WHERE pm.project.id = :projectId"
    )
    List<MemberWithLikeDto> findMembersWithLikeByMemberIdAndProjectId(@Param("memberId") Long memberId, @Param("projectId") Long projectId);

    @Query("SELECT new com.hwarrk.common.dto.dto.ProjectWithLikeDto(pm.project, " +
            "CASE WHEN pl.id IS NOT NULL THEN true ELSE false END) " +
            "FROM ProjectMember pm " +
            "LEFT JOIN ProjectLike pl ON pl.member.id = :memberId AND pl.project.id = pm.project.id " +
            "WHERE pm.member.id = :memberId"
    )
    List<ProjectWithLikeDto> findProjectsWithLikeByMemberId(@Param("memberId") Long memberId);

    Optional<ProjectMember> findByProjectAndMember(Project project, Member member);

    @Query("SELECT new com.hwarrk.common.dto.dto.ProjectMemberWithLikeDto(pm, "
            + "CASE WHEN pl.id IS NOT NULL THEN true ELSE false END) "
            + "FROM ProjectMember pm "
            + "LEFT JOIN FETCH pm.member psm "
            + "LEFT JOIN FETCH psm.projectLikes pl "
            + "WHERE pm.project.id = :projectId")
    List<ProjectMemberWithLikeDto> findProjectMembersByProjectId(@Param("projectId") Long projectId);

    List<ProjectMember> findAllByProject(Project project);
}
