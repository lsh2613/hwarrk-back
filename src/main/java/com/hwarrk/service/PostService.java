package com.hwarrk.service;

import com.hwarrk.common.EntityFacade;
import com.hwarrk.common.dto.dto.RecruitingPositionDto;
import com.hwarrk.common.dto.req.PostCreateReq;
import com.hwarrk.common.dto.req.PostUpdateReq;
import com.hwarrk.common.dto.res.MyPostRes;
import com.hwarrk.entity.Member;
import com.hwarrk.entity.Post;
import com.hwarrk.entity.Project;
import com.hwarrk.entity.RecruitingPosition;
import com.hwarrk.repository.PostLikeRepository;
import com.hwarrk.repository.PostRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final EntityFacade entityFacade;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    public Long createPost(PostCreateReq req) {
        Project project = entityFacade.getProject(req.getProjectId());
        if (Optional.ofNullable(postRepository.findByProject(project))
                .isPresent()) {
            throw new IllegalStateException("프로젝트에 이미 공고가 존재합니다.");
        }

        Post post = req.createPost();
        post.addProject(project);
        post.addSkills(req.getSkills());

        for (RecruitingPositionDto positionReq : req.getRecruitingPositionDtoList()) {
            RecruitingPosition recruitingPosition = RecruitingPosition.create(positionReq.getPositionType(),
                    positionReq.getNumber());
            recruitingPosition.addPost(post);
        }

        return postRepository.save(post).getId();
    }

    public void updatePost(PostUpdateReq req) {
        Post post = entityFacade.getPost(req.getPostId());
        post.updatePost(req.getTitle(), req.getBody(), req.getSkills());
        post.addSkills(req.getSkills());

        for (RecruitingPositionDto positionReq : req.getRecruitingPositionDtoList()) {
            RecruitingPosition recruitingPosition = RecruitingPosition.create(positionReq.getPositionType(),
                    positionReq.getNumber());
            recruitingPosition.addPost(post);
        }
    }

    public void deletePost(Long postId) {
        Post post = entityFacade.getPost(postId);
        postRepository.delete(post);
    }

    public List<MyPostRes> findMyPosts(Long memberId) {
        Member member = entityFacade.getMember(memberId);
        List<Post> myPosts = postRepository.findPostsByMember(member.getId());
        return myPosts.stream().map(p -> MyPostRes.mapEntityToRes(p, p.isPostLike(member))).toList();
    }
}
