package com.hwarrk.common;

import com.hwarrk.entity.*;
import com.hwarrk.repository.*;
import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.common.exception.GeneralHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@RequiredArgsConstructor
@Service
public class EntityFacade {
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final ProjectJoinRepository projectJoinRepository;
    private final NotificationRepository notificationRepository;
    private final PostRepository postRepository;

    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND)
        );
    }

    public Project getProject(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(
                () -> new GeneralHandler(ErrorStatus.PROJECT_NOT_FOUND)
        );
    }

    public ProjectJoin getProjectJoin(Long projectJoinId) {
        return projectJoinRepository.findById(projectJoinId).orElseThrow(
                () -> new GeneralHandler(ErrorStatus.PROJECT_JOIN_NOT_FOUND)
        );
    }

    public Notification getNotification(Long notificationId) {
        return notificationRepository.findById(notificationId).orElseThrow(
                () -> new GeneralHandler(ErrorStatus.NOTIFICATION_NOT_FOUND)
        );
    }

    public Post getPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new GeneralHandler(ErrorStatus.POST_NOT_FOUND)
        );
    }
}
