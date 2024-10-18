package com.hwarrk.controller;

import com.hwarrk.common.apiPayload.CustomApiResponse;
import com.hwarrk.common.dto.req.PostCreateReq;
import com.hwarrk.common.dto.req.PostFilterSearchReq;
import com.hwarrk.common.dto.req.PostUpdateReq;
import com.hwarrk.common.dto.res.MyPostRes;
import com.hwarrk.common.dto.res.PostFilterSearchRes;
import com.hwarrk.common.dto.res.RecommendPostRes;
import com.hwarrk.common.dto.res.SpecificPostDetailRes;
import com.hwarrk.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "프로젝트 공고 생성")
    @PostMapping
    public CustomApiResponse createPost(@AuthenticationPrincipal Long loginId,
                                        @RequestBody PostCreateReq req) {
        Long postId = postService.createPost(req, loginId);
        return CustomApiResponse.onSuccess(postId);
    }

    @Operation(summary = "프로젝트 공고 수정")
    @PostMapping("/{postId}")
    public CustomApiResponse updatePost(@AuthenticationPrincipal Long loginId,
                                        @PathVariable Long postId,
                                        @RequestBody PostUpdateReq req) {
        postService.updatePost(req, loginId, postId);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "특정 프로젝트 공고 상세 조회")
    @GetMapping("/{postId}")
    public CustomApiResponse findSpecificPostInfo(@AuthenticationPrincipal Long loginId,
                                                  @PathVariable Long postId) {
        SpecificPostDetailRes post = postService.findSpecificPostInfo(postId, loginId);
        return CustomApiResponse.onSuccess(post);
    }

    @Operation(summary = "프로젝트 공고 삭제")
    @DeleteMapping("/{postId}")
    public CustomApiResponse deletePost(@AuthenticationPrincipal Long loginId,
                                        @PathVariable Long postId) {
        postService.deletePost(loginId, postId);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "내가 만든 프로젝트 공고 조회")
    @GetMapping("/leader")
    public CustomApiResponse findMyPosts(@AuthenticationPrincipal Long loginId) {
        List<MyPostRes> post = postService.findMyPosts(loginId);
        return CustomApiResponse.onSuccess(post);
    }

    @Operation(summary = "프로젝트 공고 필터링 조회")
    @GetMapping
    public CustomApiResponse findFilteredPost(@AuthenticationPrincipal Long loginId,
                                              @RequestBody PostFilterSearchReq req) {
        List<PostFilterSearchRes> post = postService.findFilteredPost(req, loginId);
        return CustomApiResponse.onSuccess(post);
    }

    @Operation(summary = "추천 프로젝트 공고 조회")
    @GetMapping("/recommend")
    public CustomApiResponse findRecommendPosts(@AuthenticationPrincipal Long loginId) {
        List<RecommendPostRes> post = postService.findRecommendPosts(loginId);
        return CustomApiResponse.onSuccess(post);
    }
}
