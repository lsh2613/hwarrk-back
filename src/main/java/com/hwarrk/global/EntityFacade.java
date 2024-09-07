package com.hwarrk.global;

import com.hwarrk.domain.project.entity.Project;
import com.hwarrk.domain.project.repository.ProjectRepository;
import com.hwarrk.domain.project_join.entity.ProjectJoin;
import com.hwarrk.domain.project_join.repository.ProjectJoinRepository;
import com.hwarrk.global.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.global.common.exception.GeneralHandler;
import com.hwarrk.domain.member.entity.Member;
import com.hwarrk.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


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
