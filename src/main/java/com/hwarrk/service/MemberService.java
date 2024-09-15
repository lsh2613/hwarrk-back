package com.hwarrk.service;

public interface MemberService {
    void logout(String AccessToken, String RefreshToken, Long memberId);

    void deleteMember(Long loginMemberId);
}
