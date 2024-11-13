package com.hwarrk.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hwarrk.common.EntityFacade;
import com.hwarrk.common.dto.dto.ProjectJoinWithLikeDto;
import com.hwarrk.common.dto.dto.ProjectMemberWithLikeDto;
import com.hwarrk.common.dto.dto.ProjectWithLikeDto;
import com.hwarrk.common.dto.req.ProjectCreateReq;
import com.hwarrk.common.dto.req.ProjectFilterSearchReq;
import com.hwarrk.common.dto.req.ProjectUpdateReq;
import com.hwarrk.common.dto.res.CareerInfoRes;
import com.hwarrk.common.dto.res.CompleteProjectsRes;
import com.hwarrk.common.dto.res.MemberCardRes;
import com.hwarrk.common.dto.res.MyProjectRes;
import com.hwarrk.common.dto.res.PageRes;
import com.hwarrk.common.dto.res.ProjectFilterSearchRes;
import com.hwarrk.common.dto.res.ProjectRes;
import com.hwarrk.common.dto.res.RecommendProjectRes;
import com.hwarrk.common.dto.res.SpecificProjectDetailRes;
import com.hwarrk.common.dto.res.SpecificProjectInfoRes;
import com.hwarrk.common.exception.GeneralHandler;
import com.hwarrk.entity.CareerInfo;
import com.hwarrk.entity.Member;
import com.hwarrk.entity.Post;
import com.hwarrk.entity.Project;
import com.hwarrk.entity.ProjectJoin;
import com.hwarrk.entity.ProjectMember;
import com.hwarrk.repository.ProjectJoinRepository;
import com.hwarrk.repository.ProjectMemberRepository;
import com.hwarrk.repository.ProjectRepository;
import com.hwarrk.repository.ProjectRepositoryCustom;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

class ProjectServiceImplTest {

    @Mock
    private EntityFacade entityFacade;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectRepositoryCustom projectRepositoryCustom;

    @Mock
    private S3Uploader s3Uploader;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @Mock
    private ProjectJoinRepository projectJoinRepository;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProject() {
        // given
        Long loginId = 1L;
        ProjectCreateReq req = mock(ProjectCreateReq.class);
        MultipartFile image = mock(MultipartFile.class);
        Member member = mock(Member.class);
        Project project = mock(Project.class);
        String givenImageUrl = "imageUrl";

        when(entityFacade.getMember(loginId)).thenReturn(member);
        when(s3Uploader.uploadImg(any())).thenReturn(givenImageUrl);
        when(req.mapCreateReqToProject(member, givenImageUrl)).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(project);
        when(project.getId()).thenReturn(1L);

        // when
        Long result = projectService.createProject(loginId, req, image);

        // then
        assertThat(result).isEqualTo(1L);
        verify(entityFacade, times(1)).getMember(loginId);
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void getSpecificProjectInfo_Success() {
        // given
        Long memberId = 1L;
        Long projectId = 1L;

        Project project = mock(Project.class);
        Member member_01 = mock(Member.class);
        Member member_02 = mock(Member.class);

        LinkedHashSet<ProjectMember> projectMembers = new LinkedHashSet<>();
        ProjectMember projectMember_01 = mock(ProjectMember.class);
        ProjectMember projectMember_02 = mock(ProjectMember.class);
        projectMembers.add(projectMember_01);
        projectMembers.add(projectMember_02);

        Post post = mock(Post.class);
        List<CareerInfo> careerInfos = List.of(mock(CareerInfo.class), mock(CareerInfo.class));

        when(projectRepository.findSpecificProjectInfoById(projectId)).thenReturn(Optional.of(project));
        when(projectRepository.existsProjectLikeByMemberId(memberId, projectId)).thenReturn(false);
        when(projectRepository.findMemberLikesByMemberId(memberId, projectId)).thenReturn(Collections.emptyList());

        when(project.getProjectMembers()).thenReturn(projectMembers);
        when(project.getPost()).thenReturn(post);

        when(projectMember_01.getMember()).thenReturn(member_01);
        when(projectMember_02.getMember()).thenReturn(member_02);

        when(projectMember_01.getMember().loadCareer()).thenReturn(careerInfos.get(0));
        when(projectMember_02.getMember().loadCareer()).thenReturn(careerInfos.get(1));

        // when
        SpecificProjectInfoRes result = projectService.getSpecificProjectInfo(memberId, projectId);

        // then
        assertThat(result).isNotNull();
        SpecificProjectInfoRes expected = SpecificProjectInfoRes.mapEntityToRes(project, false,
                List.of(new MemberCardRes(new CareerInfoRes()), new MemberCardRes(new CareerInfoRes())));
        assertThat(result.getName()).isEqualTo(expected.getName());
        assertThat(result.getImage()).isEqualTo(expected.getImage());
        assertThat(result.isLiked()).isFalse();
        assertThat(result.getMemberCardResList()).isNotEmpty();
        assertThat(result.getMemberCardResList().get(0)).isEqualTo(expected.getMemberCardResList().get(0));
        assertThat(result.getMemberCardResList().get(1)).isEqualTo(expected.getMemberCardResList().get(1));

        verify(projectRepository, times(1)).findSpecificProjectInfoById(projectId);
        verify(projectRepository, times(1)).existsProjectLikeByMemberId(memberId, projectId);
        verify(projectRepository, times(1)).findMemberLikesByMemberId(memberId, projectId);
        verify(project, times(1)).incrementViews();
    }

    @Test
    void getSpecificProjectInfo_Fail() {
        // given
        Long memberId = 1L;
        Long projectId = 1L;
        when(projectRepository.findSpecificProjectInfoById(projectId)).thenReturn(Optional.empty());

        // when, then
        Exception exception = assertThrows(GeneralHandler.class, () -> {
            projectService.getSpecificProjectInfo(memberId, projectId);
        });

        assertThat(exception.getMessage()).isEqualTo("프로젝트를 찾을 수 없습니다.");
        verify(projectRepository, times(1)).findSpecificProjectInfoById(projectId);
    }

    @Test
    void getProjects() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Project project = mock(Project.class);
        Page<Project> projectPage = new PageImpl<>(Collections.singletonList(project));

        when(projectRepository.findAllByOrderByCreatedAtDesc(pageable)).thenReturn(projectPage);

        // when
        PageRes<ProjectRes> result = projectService.getProjects(pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.content()).hasSize(1);
        verify(projectRepository, times(1)).findAllByOrderByCreatedAtDesc(pageable);
    }

    @Test
    void deleteProject_Success() {
        // given
        Long loginId = 1L;
        Long projectId = 1L;
        Project project = mock(Project.class);
        String givenImageUrl = "imageUrl";

        when(entityFacade.getProject(projectId)).thenReturn(project);
        when(project.isProjectLeader(loginId)).thenReturn(true);
        when(project.getImage()).thenReturn(givenImageUrl);

        // when
        projectService.deleteProject(loginId, projectId);

        // then
        verify(projectRepository, times(1)).delete(project);
        verify(entityFacade, times(1)).getProject(projectId);
        verify(s3Uploader, times(1)).deleteImg(givenImageUrl);
    }

    @Test
    void deleteProject_NotLeader() {
        // given
        Long loginId = 1L;
        Long projectId = 1L;
        Project project = mock(Project.class);

        when(entityFacade.getProject(projectId)).thenReturn(project);
        when(project.isProjectLeader(loginId)).thenReturn(false);

        // when
        projectService.deleteProject(loginId, projectId);

        // then
        verify(projectRepository, never()).delete(any());
    }

    @Test
    void updateProject_Success() {
        // given
        Long loginId = 1L;
        Long projectId = 1L;
        ProjectUpdateReq req = mock(ProjectUpdateReq.class);
        Project project = mock(Project.class);
        MultipartFile image = mock(MultipartFile.class);
        String givenImageUrl = "imageUrl";

        when(entityFacade.getProject(projectId)).thenReturn(project);
        when(s3Uploader.uploadImg(image)).thenReturn(givenImageUrl);
        when(project.isProjectLeader(loginId)).thenReturn(true);

        // when
        projectService.updateProject(loginId, projectId, req, image);

        // then
        verify(entityFacade, times(1)).getProject(projectId);
        verify(project, times(1)).updateProject(any(), anyString());
        verify(s3Uploader, times(1)).uploadImg(image);
    }

    @Test
    void updateProject_NotLeader() {
        // given
        Long loginId = 1L;
        Long projectId = 1L;
        ProjectUpdateReq req = mock(ProjectUpdateReq.class);
        Project project = mock(Project.class);
        MultipartFile image = mock(MultipartFile.class);

        when(entityFacade.getProject(projectId)).thenReturn(project);
        when(project.isProjectLeader(loginId)).thenReturn(false);

        // when
        projectService.updateProject(loginId, projectId, req, image);

        // then
        verify(project, never()).updateProject(any(), anyString());
    }

    @Test
    void completeProject_Success() {
        // given
        Long projectId = 1L;
        Project project = mock(Project.class);

        when(entityFacade.getProject(projectId)).thenReturn(project);

        // when
        projectService.completeProject(projectId);

        // then
        verify(project, times(1)).completeProject();
        verify(entityFacade, times(1)).getProject(projectId);
    }

    @Test
    void getCompleteProjects_Success() {
        // given
        Long loginId = 1L;
        Project project = mock(Project.class);
        ProjectWithLikeDto projectWithLikeDto = mock(ProjectWithLikeDto.class);
        List<ProjectWithLikeDto> projectWithLikeDtos = Collections.singletonList(projectWithLikeDto);

        when(projectRepository.findProjectsAndIsLikedByMember(loginId)).thenReturn(projectWithLikeDtos);
        when(projectWithLikeDto.getProject()).thenReturn(project);

        // when
        List<CompleteProjectsRes> result = projectService.getCompleteProjects(loginId);

        // then
        assertThat(result).hasSize(1);
        verify(projectRepository, times(1)).findProjectsAndIsLikedByMember(loginId);
    }

    @Test
    void deleteCompleteProject_Success() {
        // given
        Long loginId = 1L;
        Long projectId = 1L;
        Project project = mock(Project.class);
        String givenImageUrl = "imageUrl";

        when(entityFacade.getProject(projectId)).thenReturn(project);
        when(project.isProjectLeader(loginId)).thenReturn(true);
        when(project.isComplete()).thenReturn(true);
        when(project.getImage()).thenReturn(givenImageUrl);

        // when
        projectService.deleteCompleteProject(loginId, projectId);

        // then
        verify(projectRepository, times(1)).delete(project);
        verify(s3Uploader, times(1)).deleteImg(givenImageUrl);
    }

    @Test
    void deleteCompleteProject_NotLeader() {
        // given
        Long loginId = 1L;
        Long projectId = 1L;
        Project project = mock(Project.class);

        when(entityFacade.getProject(projectId)).thenReturn(project);
        when(project.isProjectLeader(loginId)).thenReturn(false);
        when(project.isComplete()).thenReturn(true);

        // when
        projectService.deleteCompleteProject(loginId, projectId);

        // then
        verify(projectRepository, never()).delete(any());
    }

    @Test
    void deleteCompleteProject_NotComplete() {
        // given
        Long loginId = 1L;
        Long projectId = 1L;
        Project project = mock(Project.class);

        when(entityFacade.getProject(projectId)).thenReturn(project);
        when(project.isProjectLeader(loginId)).thenReturn(true);
        when(project.isComplete()).thenReturn(false);

        // when
        projectService.deleteCompleteProject(loginId, projectId);

        // then
        verify(projectRepository, never()).delete(any());
    }

    @Test
    void getMyProjects_Success() {
        // given
        Long loginId = 1L;
        Project project = mock(Project.class);
        List<Project> myProjects = Collections.singletonList(project);
        Post post = mock(Post.class);

        when(projectRepository.findByLeaderOrderByCreatedAtDesc(loginId)).thenReturn(myProjects);
        when(project.getPost()).thenReturn(post);

        // when
        List<MyProjectRes> result = projectService.getMyProjects(loginId);

        // then
        assertThat(result).hasSize(1);
        verify(projectRepository, times(1)).findByLeaderOrderByCreatedAtDesc(loginId);
    }

    @Test
    void getSpecificProjectDetails_Success() {
        // given
        Long projectId = 1L;
        Project project = mock(Project.class);
        ProjectMemberWithLikeDto projectMemberWithLikeDto = mock(ProjectMemberWithLikeDto.class);
        ProjectJoinWithLikeDto projectJoinWithLikeDto = mock(ProjectJoinWithLikeDto.class);
        ProjectMember projectMember = mock(ProjectMember.class);
        ProjectJoin projectJoin = mock(ProjectJoin.class);
        Member member = mock(Member.class);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(projectMemberRepository.findProjectMembersByProjectId(projectId)).thenReturn(
                List.of(projectMemberWithLikeDto));
        when(projectJoinRepository.findProjectJoinsWithProjectId(projectId)).thenReturn(
                List.of(projectJoinWithLikeDto));
        when(projectMemberWithLikeDto.getProjectMember()).thenReturn(projectMember);
        when(projectJoinWithLikeDto.getProjectJoin()).thenReturn(projectJoin);
        when(projectMember.getMember()).thenReturn(member);
        when(projectJoin.getMember()).thenReturn(member);

        // when
        SpecificProjectDetailRes result = projectService.getSpecificProjectDetails(projectId);

        // then
        assertThat(result).isNotNull();
        verify(projectRepository, times(1)).findById(projectId);
        verify(projectMemberRepository, times(1)).findProjectMembersByProjectId(projectId);
        verify(projectJoinRepository, times(1)).findProjectJoinsWithProjectId(projectId);
    }

    @Test
    void getSpecificProjectDetails_Fail() {
        // given
        Long projectId = 1L;

        when(projectRepository.findSpecificProjectDetailsById(projectId)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> projectService.getSpecificProjectDetails(projectId))
                .isInstanceOf(GeneralHandler.class)
                .hasMessage("프로젝트를 찾을 수 없습니다.");
    }

    @Test
    void getFilteredSearchProjects_Success() {
        // given
        Long loginId = 1L;
        ProjectFilterSearchReq req = mock(ProjectFilterSearchReq.class);
        Pageable pageable = PageRequest.of(0, 10);
        Project project = mock(Project.class);

        when(req.getRecruitingType()).thenReturn("모집 중");
        when(req.getFilterType()).thenReturn("최신");
        when(req.getKeyWord()).thenReturn("keyword");

        List<ProjectWithLikeDto> projectWithLikeDtos = Collections.singletonList(mock(ProjectWithLikeDto.class));
        PageImpl<ProjectWithLikeDto> projectWithLikeDtoPage = new PageImpl<>(projectWithLikeDtos, pageable,
                projectWithLikeDtos.size());

        when(projectRepositoryCustom.findFilteredProjects(any(), any(), anyString(), anyLong(), any(Pageable.class)))
                .thenReturn(projectWithLikeDtoPage);
        when(projectWithLikeDtos.get(0).getProject()).thenReturn(project);

        // when
        PageRes<ProjectFilterSearchRes> result = projectService.getFilteredSearchProjects(loginId, req,
                pageable);

        // then
        assertThat(result)
                .isNotNull()
                .extracting(PageRes::content, PageRes::totalElements, PageRes::totalPages, PageRes::isLast)
                .containsExactly(
                        projectWithLikeDtos.stream().map(ProjectFilterSearchRes::createRes).toList(),
                        projectWithLikeDtoPage.getTotalElements(),
                        projectWithLikeDtoPage.getTotalPages(),
                        projectWithLikeDtoPage.isLast()
                );

        verify(projectRepositoryCustom, times(1))
                .findFilteredProjects(any(), any(), anyString(), anyLong(), any(Pageable.class));
    }

    @Test
    void getRecommendedProjects_Success() {
        // given
        Long loginId = 1L;
        ProjectWithLikeDto projectWithLikeDto = mock(ProjectWithLikeDto.class);
        List<ProjectWithLikeDto> recommendedProjects = Collections.singletonList(projectWithLikeDto);
        Post post = mock(Post.class);
        Project project = mock(Project.class);

        when(projectRepositoryCustom.findRecommendedProjects(loginId)).thenReturn(recommendedProjects);
        when(projectWithLikeDto.getProject()).thenReturn(project);
        when(project.getPost()).thenReturn(post);

        // when
        List<RecommendProjectRes> result = projectService.getRecommendedProjects(loginId);

        // then
        assertThat(result).hasSize(1);
        verify(projectRepositoryCustom, times(1)).findRecommendedProjects(loginId);
    }
}
