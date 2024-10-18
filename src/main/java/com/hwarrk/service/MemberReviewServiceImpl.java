package com.hwarrk.service;

import com.hwarrk.common.EntityFacade;
import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.common.dto.req.ReviewCreateReq;
import com.hwarrk.common.exception.GeneralHandler;
import com.hwarrk.entity.Member;
import com.hwarrk.entity.MemberReview;
import com.hwarrk.entity.Project;
import com.hwarrk.entity.ProjectMember;
import com.hwarrk.repository.MemberReviewRepository;
import com.hwarrk.repository.ProjectMemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MemberReviewServiceImpl implements MemberReviewService {

    private final EntityFacade entityFacade;
    private final MemberReviewRepository memberReviewRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    public void createReview(Long projectId, Long fromMemberId, Long toMemberId, ReviewCreateReq req) {
        Project project = entityFacade.getProject(projectId);
        Member fromMember = entityFacade.getMember(fromMemberId);
        Member toMember = entityFacade.getMember(toMemberId);

        List<ProjectMember> projectMembers = projectMemberRepository.findAllByProject(project);

        if (!projectMembers.stream().map(ProjectMember::getMember).toList().containsAll(List.of(fromMember, toMember)))
            throw new GeneralHandler(ErrorStatus.PROJECT_MEMBER_ONLY_CAN_REVIEW);

        if (fromMember.isSameId(toMember.getId()))
            throw new GeneralHandler(ErrorStatus.SELF_REVIEW_FORBIDDEN);

        if (memberReviewRepository.findByProjectAndFromMemberAndToMember(project, fromMember, toMember).isPresent())
            throw new GeneralHandler(ErrorStatus.MEMBER_REVIEW_CONFLICT);

        MemberReview createdMemberReview = req.mapReqToMember(project, fromMember, toMember);
        memberReviewRepository.save(createdMemberReview);
    }
}
