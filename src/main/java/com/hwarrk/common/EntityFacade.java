package com.hwarrk.common;

import com.hwarrk.entity.Project;
import com.hwarrk.repository.ProjectRepository;
import com.hwarrk.entity.ProjectJoin;
import com.hwarrk.repository.ProjectJoinRepository;
import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.common.exception.GeneralHandler;
import com.hwarrk.entity.Member;
import com.hwarrk.repository.MemberRepository;
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
}
