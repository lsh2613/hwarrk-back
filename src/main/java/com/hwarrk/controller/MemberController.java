package com.hwarrk.controller;

import com.hwarrk.common.apiPayload.CustomApiResponse;
import com.hwarrk.common.dto.req.ProfileCond;
import com.hwarrk.common.dto.req.ProfileUpdateReq;
import com.hwarrk.common.dto.res.MemberCardRes;
import com.hwarrk.common.dto.res.MyProfileRes;
import com.hwarrk.common.dto.res.PageRes;
import com.hwarrk.common.dto.res.ProfileRes;
import com.hwarrk.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "유저/프로필")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "로그아웃",
            description = "헤더로 AccessToken과 RefreshToken을 담아 요청, 로그아웃 시 Req에 넘어오는 token은 블랙리스트 토큰으로 관리하여 재사용을 막음")
    @PostMapping("/logout")
    public CustomApiResponse logout(HttpServletRequest request,
                                    @AuthenticationPrincipal Long loginId) {
        memberService.logout(request);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping
    public CustomApiResponse deleteMember(@AuthenticationPrincipal Long loginId) {
        memberService.deleteMember(loginId);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "프로필 작성/수정",
            responses = {
                    @ApiResponse(responseCode = "PROJECT4001", description = "프로젝트를 찾을 수 없습니다"),
                    @ApiResponse(responseCode = "S400", description = "잘못된 이미지 데이터입니다"),
                    @ApiResponse(responseCode = "S401", description = "S3 접근 인증에 실패했습니다"),
                    @ApiResponse(responseCode = "S403", description = "S3 권한을 가지고 있지 않습니다"),
                    @ApiResponse(responseCode = "S5001", description = "S3에 이미지 업로드를 실패했습니다"),
                    @ApiResponse(responseCode = "S503", description = "S3 서버가 일시적으로 데이터를 처리할 수 없습니다"),
            })
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CustomApiResponse updateProfile(@AuthenticationPrincipal Long loginId,
                                           @RequestPart ProfileUpdateReq profileUpdateReq,
                                           @RequestPart(value = "image", required = false) MultipartFile image) {
        memberService.updateProfile(loginId, profileUpdateReq, image);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "나의 프로필 조회")
    @GetMapping("/my-profile")
    public CustomApiResponse<MyProfileRes> getMyProfile(@AuthenticationPrincipal Long loginId) {
        MyProfileRes res = memberService.getMyProfile(loginId);
        return CustomApiResponse.onSuccess(res);
    }

    @Operation(summary = "남의 프로필 조회")
    @ApiResponse(responseCode = "MEMBER4041", description = "사용자를 찾을 수 없습니다")
    @GetMapping("{memberId}")
    public CustomApiResponse<ProfileRes> getProfile(@AuthenticationPrincipal Long loginId,
                                                    @PathVariable Long memberId) {
        ProfileRes res = memberService.getProfile(loginId, memberId);
        return CustomApiResponse.onSuccess(res);
    }

    @Operation(summary = "프로필 허브 조회")
    @GetMapping
    public CustomApiResponse<PageRes<MemberCardRes>> getFilteredMemberCard(@AuthenticationPrincipal Long loginId,
                                                                           @RequestBody ProfileCond cond,
                                                                           @PageableDefault Pageable pageable) {
        PageRes<MemberCardRes> res = memberService.getFilteredMemberCard(loginId, cond, pageable);
        return CustomApiResponse.onSuccess(res);
    }

}
