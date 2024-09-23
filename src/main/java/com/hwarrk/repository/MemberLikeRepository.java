package com.hwarrk.repository;

import com.hwarrk.entity.Member;
import com.hwarrk.entity.MemberLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberLikeRepository extends JpaRepository<MemberLike, Long> {

    Optional<MemberLike> findByFromMemberAndToMember(Member fromMember, Member toMember);
}
