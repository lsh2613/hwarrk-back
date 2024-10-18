package com.hwarrk.repository;

import com.hwarrk.entity.Member;
import com.hwarrk.entity.MemberReview;
import com.hwarrk.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberReviewRepository extends JpaRepository<MemberReview, Long> {
    Optional<MemberReview> findByProjectAndFromMemberAndToMember(Project project, Member fromMember, Member toMember);
}
