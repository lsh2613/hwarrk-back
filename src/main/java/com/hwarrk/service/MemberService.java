package com.hwarrk.service;

import com.hwarrk.common.dto.req.ProfileCond;
import com.hwarrk.common.dto.req.ProfileUpdateReq;
import com.hwarrk.common.dto.res.MyProfileRes;
import com.hwarrk.common.dto.res.PageRes;
import com.hwarrk.common.dto.res.ProfileRes;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {
    void logout(HttpServletRequest request);
    void deleteMember(Long loginId);
    void updateMember(Long loginId, ProfileUpdateReq req, MultipartFile image);
    MyProfileRes getMyProfile(Long memberId);
    ProfileRes getProfile(Long loginId, Long memberId);
    PageRes getMembers(Long memberId, ProfileCond cond, Pageable pageable);
}
