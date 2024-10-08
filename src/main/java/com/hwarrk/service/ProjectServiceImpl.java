package com.hwarrk.service;

import static com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus.PROJECT_NOT_FOUND;

import com.hwarrk.common.EntityFacade;
import com.hwarrk.common.constant.ProjectFilterType;
import com.hwarrk.common.constant.RecruitingType;
import com.hwarrk.common.dto.dto.MemberWithLikeDto;
import com.hwarrk.common.dto.dto.ProjectWithLikeDto;
import com.hwarrk.common.dto.req.ProjectCreateReq;
import com.hwarrk.common.dto.req.ProjectFilterSearchReq;
import com.hwarrk.common.dto.req.ProjectUpdateReq;
import com.hwarrk.common.dto.res.CareerInfoRes;
import com.hwarrk.common.dto.res.CompleteProjectsRes;
import com.hwarrk.common.dto.res.MemberRes;
import com.hwarrk.common.dto.res.MyProjectRes;
import com.hwarrk.common.dto.res.PageRes;
import com.hwarrk.common.dto.res.ProjectFilterSearchRes;
import com.hwarrk.common.dto.res.ProjectRes;
import com.hwarrk.common.dto.res.RecommendProjectRes;
import com.hwarrk.common.dto.res.SpecificProjectDetailRes;
import com.hwarrk.common.dto.res.SpecificProjectInfoRes;
import com.hwarrk.common.exception.GeneralHandler;
import com.hwarrk.entity.Member;
import com.hwarrk.entity.Project;
import com.hwarrk.repository.ProjectRepository;
import com.hwarrk.repository.ProjectRepositoryCustom;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {

    private final EntityFacade entityFacade;
    private final ProjectRepository projectRepository;
    private final ProjectRepositoryCustom projectRepositoryCustom;
    private final S3Uploader s3Uploader;

    @Override
    public Long createProject(Long loginId, ProjectCreateReq req) {
        Member member = entityFacade.getMember(loginId);
        String imageUrl = s3Uploader.uploadImg(req.image());
        Project project = req.mapCreateReqToProject(member, imageUrl);
        projectRepository.save(project);
        return project.getId();
    }

    @Override
    public SpecificProjectInfoRes getSpecificProjectInfo(Long loginId, Long projectId) {
        Project project = projectRepository.findSpecificProjectInfoById(projectId)
                .orElseThrow(() -> new GeneralHandler(PROJECT_NOT_FOUND));

        boolean isProjectLiked = projectRepository.existsProjectLikeByMemberId(loginId, projectId);

        List<MemberWithLikeDto> memberLikes = projectRepository.findMemberLikesByMemberId(loginId, projectId);

        Map<Long, Boolean> likedMembersMap = memberLikes.stream()
                .collect(Collectors.toMap(dto -> dto.member().getId(), MemberWithLikeDto::isLiked));

        List<MemberRes> memberResList = project.getProjectMembers().stream()
                .map(pm -> MemberRes.mapEntityToRes(
                        pm.getMember(),
                        CareerInfoRes.mapEntityToRes(pm.getMember().loadCareer()),
                        likedMembersMap.getOrDefault(pm.getMember().getId(), false)
                ))
                .toList();

        project.incrementViews();

        return SpecificProjectInfoRes.mapEntityToRes(project, isProjectLiked, memberResList);
    }

    @Override
    public PageRes<ProjectRes> getProjects(Pageable pageable) {
        Page<Project> projects = projectRepository.findAllByOrderByCreatedAtDesc(pageable);
        return PageRes.mapPageToPageRes(projects, ProjectRes::mapEntityToRes);
    }

    @Override
    public void deleteProject(Long loginId, Long projectId) {
        Project project = entityFacade.getProject(projectId);
        if (project.isProjectLeader(loginId)) {
            projectRepository.delete(project);
        }
    }

    @Override
    public void updateProject(Long loginId, Long projectId, ProjectUpdateReq req) {
        Project project = entityFacade.getProject(projectId);
        if (project.isProjectLeader(loginId)) {
            project.updateProject(req.mapUpdateReqToProject());
        }
    }

    @Override
    public void completeProject(Long projectId) {
        Project project = entityFacade.getProject(projectId);
        project.completeProject();
    }

    @Override
    public List<CompleteProjectsRes> getCompleteProjects(Long loginId) {
        List<ProjectWithLikeDto> projectWithLikeDtos = projectRepository.findProjectsAndIsLikedByMember(loginId);
        return projectWithLikeDtos.stream().map(CompleteProjectsRes::createRes).toList();
    }

    @Override
    public void deleteCompleteProject(Long loginId, Long projectId) {
        Project project = entityFacade.getProject(projectId);
        if (project.isProjectLeader(loginId) && project.isComplete()) {
            projectRepository.delete(project);
        }
    }

    @Override
    public List<MyProjectRes> getMyProjects(Long loginId) {
        List<Project> myProjects = projectRepository.findByLeaderOrderByCreatedAtDesc(loginId);
        return myProjects.stream().map(MyProjectRes::createRes).toList();
    }

    @Override
    public SpecificProjectDetailRes getSpecificProjectDetails(Long projectId) {
        Project project = projectRepository.findSpecificProjectDetailsById(projectId)
                .orElseThrow(() -> new GeneralHandler(PROJECT_NOT_FOUND));
        return SpecificProjectDetailRes.createRes(project);
    }

    @Override
    public PageRes<ProjectFilterSearchRes> getFilteredSearchProjects(Long loginId, ProjectFilterSearchReq req,
                                                                     Pageable pageable) {
        RecruitingType recruitingType = RecruitingType.findType(req.getRecruitingType());
        ProjectFilterType projectFilterType = ProjectFilterType.findType(req.getFilterType());
        String keyWord = req.getKeyWord();

        PageImpl<ProjectWithLikeDto> projects = projectRepositoryCustom.findFilteredProjects(recruitingType, projectFilterType,
                keyWord, loginId, pageable);

        return PageRes.mapPageToPageRes(projects, ProjectFilterSearchRes::createRes);
    }

    @Override
    public List<RecommendProjectRes> getRecommendedProjects(Long loginId) {
        List<ProjectWithLikeDto> recommendedProjects = projectRepositoryCustom.findRecommendedProjects(loginId);
        return recommendedProjects.stream().map(RecommendProjectRes::createRes).toList();
    }
}
