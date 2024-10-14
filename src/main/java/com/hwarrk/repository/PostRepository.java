package com.hwarrk.repository;

import com.hwarrk.entity.Post;
import com.hwarrk.entity.Project;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByProject(Project project);

    @Query("SELECT p FROM Post p "
            + "JOIN FETCH p.project pp "
            + "JOIN FETCH pp.projectLikes pl "
            + "WHERE p.member.id = :memberId")
    List<Post> findPostsByMember(@Param("memberId") Long memberId);

    @Query("SELECT p FROM Post p "
            + "JOIN FETCH p.project pp "
            + "JOIN FETCH p.skills s "
            + "JOIN FETCH p.positions rp "
            + "WHERE p.id = :postId")
    Optional<Post> findPostsWithSkillsAndRecruitingPositions(@Param("postId") Long postId);
}
