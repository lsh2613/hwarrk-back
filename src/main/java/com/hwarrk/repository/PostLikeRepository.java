package com.hwarrk.repository;

import com.hwarrk.entity.Member;
import com.hwarrk.entity.Post;
import com.hwarrk.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByMemberAndPost(Member member, Post post);
}
