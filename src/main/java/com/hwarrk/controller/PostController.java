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

    @PostMapping
    public CustomApiResponse createPost(@AuthenticationPrincipal Long loginId,
                                        @RequestBody PostCreateReq req) {
        Long postId = postService.createPost(req);
        return CustomApiResponse.onSuccess(postId);
    }

    @PostMapping("/{postId}")
    public CustomApiResponse updatePost(@AuthenticationPrincipal Long loginId,
                                        @PathVariable Long postId,
                                        @RequestBody PostUpdateReq req) {
        postService.updatePost(req, postId);
        return CustomApiResponse.onSuccess();
    }

    @GetMapping("/{postId}")
    public CustomApiResponse findSpecificPostInfo(@AuthenticationPrincipal Long loginId,
                                                  @PathVariable Long postId) {
        SpecificPostDetailRes post = postService.findSpecificPostInfo(postId, loginId);
        return CustomApiResponse.onSuccess(post);
    }

    @DeleteMapping("/{postId}")
    public CustomApiResponse deletePost(@AuthenticationPrincipal Long loginId,
                                        @PathVariable Long postId) {
        postService.deletePost(postId);
        return CustomApiResponse.onSuccess();
    }

    @GetMapping("/leader")
    public CustomApiResponse findMyPosts(@AuthenticationPrincipal Long loginId) {
        List<MyPostRes> post = postService.findMyPosts(loginId);
        return CustomApiResponse.onSuccess(post);
    }

    @GetMapping
    public CustomApiResponse findFilteredPost(@AuthenticationPrincipal Long loginId,
                                              @RequestBody PostFilterSearchReq req) {
        List<PostFilterSearchRes> post = postService.findFilteredPost(req, loginId);
        return CustomApiResponse.onSuccess(post);
    }

    @GetMapping("/recommend")
    public CustomApiResponse findRecommendPosts(@AuthenticationPrincipal Long loginId) {
        List<RecommendPostRes> post = postService.findRecommendPosts(loginId);
        return CustomApiResponse.onSuccess(post);
    }
}
