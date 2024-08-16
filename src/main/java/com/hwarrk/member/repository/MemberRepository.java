package com.hwarrk.member.repository;

import com.hwarrk.global.common.constant.OauthProvider;
import com.hwarrk.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByOauthProviderAndSocialId(OauthProvider oauthProvider, String socialId);
}
