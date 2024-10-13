package com.hwarrk.service;

import com.hwarrk.common.EntityFacade;
import com.hwarrk.common.dto.req.PostCreateReq;
import com.hwarrk.common.dto.req.PostCreateReq.RecruitingPositionReq;
import com.hwarrk.entity.Post;
import com.hwarrk.entity.Project;
import com.hwarrk.entity.RecruitingPosition;
import com.hwarrk.repository.PostRepository;
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

    public Long createPost(PostCreateReq req) {
        Project project = entityFacade.getProject(req.getProjectId());
        if (Optional.ofNullable(postRepository.findByProject(project))
                .isPresent()) {
            throw new IllegalStateException("프로젝트에 이미 공고가 존재합니다.");
        }

        Post post = req.createPost();
        post.addProject(project);
        post.addSkills(req.getSkills());

        for (RecruitingPositionReq positionReq : req.getRecruitingPositionReqList()) {
            RecruitingPosition recruitingPosition = RecruitingPosition.create(positionReq.getPositionType(),
                    positionReq.getNumber());
            recruitingPosition.addPost(post);
        }

        return postRepository.save(post).getId();
    }
}
