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
            + "LEFT JOIN FETCH pp.projectLikes pl "
            + "WHERE pp.leader.id = :memberId")
    List<Post> findPostsByMember(@Param("memberId") Long memberId);

    @Query("SELECT p FROM Post p "
            + "LEFT JOIN FETCH p.positions pos "
            + "WHERE p.id = :postId")
    Optional<Post> findPostWithPositions(@Param("postId") Long postId);

    @Query("SELECT p FROM Post p "
            + "LEFT JOIN FETCH p.skills ps "
            + "WHERE p.id = :postId")
    Optional<Post> findPostWithSkills(@Param("postId") Long postId);
}
