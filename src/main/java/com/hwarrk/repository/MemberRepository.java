package com.hwarrk.repository;

import com.hwarrk.common.constant.OauthProvider;
import com.hwarrk.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByOauthProviderAndSocialId(OauthProvider oauthProvider, String socialId);

    //    @Modifying(clearAutomatically = true)
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Member m SET m.views = m.views + 1 WHERE m.id = :memberId")
    void increaseViews(@Param("memberId") Long memberId);

    @Query("SELECT m FROM Member m "
            + "LEFT JOIN FETCH m.skills "
            + "LEFT JOIN FETCH m.positions "
            + "WHERE m.id = :memberId")
    Optional<Member> findMemberStacksById(@Param("memberId") Long memberId);
}
