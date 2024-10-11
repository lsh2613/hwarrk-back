package com.hwarrk.service;

import com.hwarrk.common.EntityFacade;
import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.common.dto.dto.MemberWithLikeDto;
import com.hwarrk.common.dto.dto.ProjectWithLikeDto;
import com.hwarrk.common.dto.res.CareerInfoRes;
import com.hwarrk.common.dto.res.MemberRes;
import com.hwarrk.common.dto.res.ProjectRes;
import com.hwarrk.common.exception.GeneralHandler;
import com.hwarrk.entity.Member;
import com.hwarrk.entity.Project;
import com.hwarrk.entity.ProjectMember;
import com.hwarrk.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
@Slf4j
public class ProjectMemberServiceImpl implements ProjectMemberService {

    private final EntityFacade entityFacade;
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    public List<MemberRes> getMembersInProject(Long memberId, Long projectId) {
        Project project = entityFacade.getProject(projectId);

        List<MemberWithLikeDto> memberWithLikeDtos = projectMemberRepository.findMembersWithLikeByMemberIdAndProjectId(memberId, project.getId());

        List<MemberRes> memberResList = memberWithLikeDtos.stream()
                .map(dto -> {
                    Member member = dto.member();
                    CareerInfoRes careerInfoRes = CareerInfoRes.mapEntityToRes(member.loadCareer());
                    return MemberRes.mapEntityToRes(member, careerInfoRes, dto.isLiked());
                })
                .toList();

        return memberResList;
    }

    @Override
    public List<ProjectRes> getMyProjects(Long memberId) {
        List<ProjectWithLikeDto> projectWithLikeDtos = projectMemberRepository.findProjectsWithLikeByMemberId(memberId);

        List<ProjectRes> projectResList = projectWithLikeDtos.stream()
                .map(ProjectRes::createRes)
                .toList();

        return projectResList;
    }

    @Override
    public void removeProjectMember(Long loginId, Long projectId, Long memberId) {
        Member leader = entityFacade.getMember(loginId);
        Project project = entityFacade.getProject(projectId);

        if (!leader.isSameId(project.getLeader().getId()))
            throw new GeneralHandler(ErrorStatus.PROJECT_LEADER_REQUIRED);
        if (leader.isSameId(memberId))
            throw new GeneralHandler(ErrorStatus.PROJECT_LEADER_CANNOT_BE_REMOVED);

        Member member = entityFacade.getMember(memberId);
        Optional<ProjectMember> projectMember = projectMemberRepository.findByProjectAndMember(project, member);


        projectMember.ifPresentOrElse(
                pm -> {
                    pm.getMember().removeProjectMember(pm);
                    projectMemberRepository.delete(pm);
                },
                () -> {
                    throw new GeneralHandler(ErrorStatus.PROJECT_MEMBER_NOT_FOUND);
                }
        );
    }
}
