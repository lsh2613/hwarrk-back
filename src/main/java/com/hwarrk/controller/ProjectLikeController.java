package com.hwarrk.controller;

import com.hwarrk.common.apiPayload.CustomApiResponse;
import com.hwarrk.common.constant.LikeType;
import com.hwarrk.common.dto.res.SliceRes;
import com.hwarrk.service.ProjectLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/project-likes")
public class ProjectLikeController {

    private final ProjectLikeService projectLikeService;

    @Operation(summary = "프로젝트 찜하기",
            responses = {
                    @ApiResponse(responseCode = "MEMBER4041", description = "사용자를 찾을 수 없습니다"),
                    @ApiResponse(responseCode = "PROJECT4001", description = "프로젝트를 찾을 수 없습니다"),
                    @ApiResponse(responseCode = "PROJECT_LIKE4091", description = "찜이 이미 존재합니다"),
                    @ApiResponse(responseCode = "PROJECT_LIKE4041", description = "찜을 찾을 수 없습니다")
            }
    )
    @PostMapping("/projects/{projectId}")
    public CustomApiResponse likeProject(@AuthenticationPrincipal Long loginId,
                                         @PathVariable("projectId") Long projectId,
                                         @RequestParam LikeType likeType) {
        projectLikeService.likeProject(loginId, projectId, likeType);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "프로젝트 찜목록 조회")
    @GetMapping
    public CustomApiResponse<SliceRes> getLikedProjects(@AuthenticationPrincipal Long loginId,
                                                        @RequestParam Long lastProjectLikeId,
                                                        @PageableDefault Pageable pageable) {
        SliceRes res = projectLikeService.getLikedProjectSlice(loginId, lastProjectLikeId, pageable);
        return CustomApiResponse.onSuccess(res);
    }
}
