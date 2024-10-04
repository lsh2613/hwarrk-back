//package com.hwarrk.service;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.never;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import com.hwarrk.common.EntityFacade;
//import com.hwarrk.common.constant.ProjectFilterType;
//import com.hwarrk.common.constant.RecruitingType;
//import com.hwarrk.common.dto.dto.ProjectWithLikeDto;
//import com.hwarrk.common.dto.req.ProjectCreateReq;
//import com.hwarrk.common.dto.req.ProjectFilterSearchReq;
//import com.hwarrk.common.dto.req.ProjectUpdateReq;
//import com.hwarrk.common.dto.res.CompleteProjectsRes;
//import com.hwarrk.common.dto.res.MyProjectRes;
//import com.hwarrk.common.dto.res.PageRes;
//import com.hwarrk.common.dto.res.ProjectFilterSearchRes;
//import com.hwarrk.common.dto.res.ProjectRes;
//import com.hwarrk.common.dto.res.RecommendProjectRes;
//import com.hwarrk.common.dto.res.SliceRes;
//import com.hwarrk.common.dto.res.SpecificProjectDetailRes;
//import com.hwarrk.common.dto.res.SpecificProjectInfoRes;
//import com.hwarrk.entity.Member;
//import com.hwarrk.entity.Project;
//import com.hwarrk.entity.ProjectMember;
//import com.hwarrk.repository.CareerInfoRepository;
//import com.hwarrk.repository.ProjectRepository;
//import com.hwarrk.repository.ProjectRepositoryCustom;
//import java.util.Collections;
//import java.util.LinkedHashSet;
//import java.util.List;
//import java.util.Optional;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Slice;
//
//class ProjectServiceImplTest {
//
//    @Mock
//    private EntityFacade entityFacade;
//
//    @Mock
//    private ProjectRepository projectRepository;
//
//    @Mock
//    private CareerInfoRepository careerInfoRepository;
//
//    @Mock
//    private ProjectRepositoryCustom projectRepositoryCustom;
//
//    @InjectMocks
//    private ProjectServiceImpl projectService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void createProject() {
//        // given
//        Long loginId = 1L;
//        ProjectCreateReq req = mock(ProjectCreateReq.class);
//        Member member = mock(Member.class);
//        Project project = mock(Project.class);
//
//        when(entityFacade.getMember(loginId)).thenReturn(member);
//        when(req.mapCreateReqToProject(member)).thenReturn(project);
//        when(projectRepository.save(project)).thenReturn(project);
//        when(project.getId()).thenReturn(1L);
//
//        // when
//        Long result = projectService.createProject(loginId, req);
//
//        // then
//        assertThat(result).isEqualTo(1L);
//        verify(entityFacade, times(1)).getMember(loginId);
//        verify(projectRepository, times(1)).save(project);
//    }
//
//    @Test
//    void getSpecificProjectInfo_Success() {
//        // given
//        Long projectId = 1L;
//        Project project = mock(Project.class);
//        ProjectMember projectMember = mock(ProjectMember.class);
//
//        when(projectRepository.findSpecificProjectInfoById(projectId)).thenReturn(Optional.of(project));
//        when(project.getProjectMembers()).thenReturn(new LinkedHashSet<>(Collections.singleton(projectMember)));
//        when(projectMember.isCareerInfoPresent()).thenReturn(false);
//
//        // when
//        SpecificProjectInfoRes result = projectService.getSpecificProjectInfo(projectId);
//
//        // then
//        assertThat(result).isNotNull();
//        verify(projectRepository, times(1)).findSpecificProjectInfoById(projectId);
//        verify(careerInfoRepository, times(1)).save(any());
//    }
//
//    @Test
//    void getSpecificProjectInfo_Fail() {
//        // given
//        Long projectId = 1L;
//        when(projectRepository.findSpecificProjectInfoById(projectId)).thenReturn(Optional.empty());
//
//        // when, then
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            projectService.getSpecificProjectInfo(projectId);
//        });
//
//        assertThat(exception.getMessage()).isEqualTo("프로젝트가 존재하지 않습니다.");
//        verify(projectRepository, times(1)).findSpecificProjectInfoById(projectId);
//    }
//
//    @Test
//    void getProjects() {
//        // given
//        Pageable pageable = PageRequest.of(0, 10);
//        Project project = mock(Project.class);
//        Page<Project> projectPage = new PageImpl<>(Collections.singletonList(project));
//
//        when(projectRepository.findAllByOrderByCreatedAtDesc(pageable)).thenReturn(projectPage);
//
//        // when
//        PageRes<ProjectRes> result = projectService.getProjects(pageable);
//
//        // then
//        assertThat(result).isNotNull();
//        assertThat(result.content()).hasSize(1);
//        verify(projectRepository, times(1)).findAllByOrderByCreatedAtDesc(pageable);
//    }
//
//    @Test
//    void deleteProject_Success() {
//        // given
//        Long loginId = 1L;
//        Long projectId = 1L;
//        Project project = mock(Project.class);
//
//        when(entityFacade.getProject(projectId)).thenReturn(project);
//        when(project.isProjectLeader(loginId)).thenReturn(true);
//
//        // when
//        projectService.deleteProject(loginId, projectId);
//
//        // then
//        verify(projectRepository, times(1)).delete(project);
//        verify(entityFacade, times(1)).getProject(projectId);
//    }
//
//    @Test
//    void deleteProject_NotLeader() {
//        // given
//        Long loginId = 1L;
//        Long projectId = 1L;
//        Project project = mock(Project.class);
//
//        when(entityFacade.getProject(projectId)).thenReturn(project);
//        when(project.isProjectLeader(loginId)).thenReturn(false);
//
//        // when
//        projectService.deleteProject(loginId, projectId);
//
//        // then
//        verify(projectRepository, never()).delete(any());
//    }
//
//    @Test
//    void updateProject_Success() {
//        // given
//        Long loginId = 1L;
//        Long projectId = 1L;
//        ProjectUpdateReq req = mock(ProjectUpdateReq.class);
//        Project project = mock(Project.class);
//
//        when(entityFacade.getProject(projectId)).thenReturn(project);
//        when(project.isProjectLeader(loginId)).thenReturn(true);
//
//        // when
//        projectService.updateProject(loginId, projectId, req);
//
//        // then
//        verify(project, times(1)).updateProject(any());
//    }
//
//    @Test
//    void updateProject_NotLeader() {
//        // given
//        Long loginId = 1L;
//        Long projectId = 1L;
//        ProjectUpdateReq req = mock(ProjectUpdateReq.class);
//        Project project = mock(Project.class);
//
//        when(entityFacade.getProject(projectId)).thenReturn(project);
//        when(project.isProjectLeader(loginId)).thenReturn(false);
//
//        // when
//        projectService.updateProject(loginId, projectId, req);
//
//        // then
//        verify(project, never()).updateProject(any());
//    }
//
//    @Test
//    void completeProject_Success() {
//        // given
//        Long projectId = 1L;
//        Project project = mock(Project.class);
//
//        when(entityFacade.getProject(projectId)).thenReturn(project);
//
//        // when
//        projectService.completeProject(projectId);
//
//        // then
//        verify(project, times(1)).completeProject();
//        verify(entityFacade, times(1)).getProject(projectId);
//    }
//
//    @Test
//    void getCompleteProjects_Success() {
//        // given
//        Long loginId = 1L;
//        ProjectWithLikeDto projectWithLikeDto = mock(ProjectWithLikeDto.class);
//        List<ProjectWithLikeDto> projectWithLikeDtos = Collections.singletonList(projectWithLikeDto);
//
//        when(projectRepository.findProjectsAndIsLikedByMember(loginId)).thenReturn(projectWithLikeDtos);
//
//        // when
//        List<CompleteProjectsRes> result = projectService.getCompleteProjects(loginId);
//
//        // then
//        assertThat(result).hasSize(1);
//        verify(projectRepository, times(1)).findProjectsAndIsLikedByMember(loginId);
//    }
//
//    @Test
//    void deleteCompleteProject_Success() {
//        // given
//        Long loginId = 1L;
//        Long projectId = 1L;
//        Project project = mock(Project.class);
//
//        when(entityFacade.getProject(projectId)).thenReturn(project);
//        when(project.isProjectLeader(loginId)).thenReturn(true);
//        when(project.isComplete()).thenReturn(true);
//
//        // when
//        projectService.deleteCompleteProject(loginId, projectId);
//
//        // then
//        verify(projectRepository, times(1)).delete(project);
//    }
//
//    @Test
//    void deleteCompleteProject_NotLeader() {
//        // given
//        Long loginId = 1L;
//        Long projectId = 1L;
//        Project project = mock(Project.class);
//
//        when(entityFacade.getProject(projectId)).thenReturn(project);
//        when(project.isProjectLeader(loginId)).thenReturn(false);
//        when(project.isComplete()).thenReturn(true);
//
//        // when
//        projectService.deleteCompleteProject(loginId, projectId);
//
//        // then
//        verify(projectRepository, never()).delete(any());
//    }
//
//    @Test
//    void deleteCompleteProject_NotComplete() {
//        // given
//        Long loginId = 1L;
//        Long projectId = 1L;
//        Project project = mock(Project.class);
//
//        when(entityFacade.getProject(projectId)).thenReturn(project);
//        when(project.isProjectLeader(loginId)).thenReturn(true);
//        when(project.isComplete()).thenReturn(false);
//
//        // when
//        projectService.deleteCompleteProject(loginId, projectId);
//
//        // then
//        verify(projectRepository, never()).delete(any());
//    }
//
//    @Test
//    void getMyProjects_Success() {
//        // given
//        Long loginId = 1L;
//        Project project = mock(Project.class);
//        List<Project> myProjects = Collections.singletonList(project);
//
//        when(projectRepository.findByLeaderOrderByCreatedAtDesc(loginId)).thenReturn(myProjects);
//
//        // when
//        List<MyProjectRes> result = projectService.getMyProjects(loginId);
//
//        // then
//        assertThat(result).hasSize(1);
//        verify(projectRepository, times(1)).findByLeaderOrderByCreatedAtDesc(loginId);
//    }
//
//    @Test
//    void getSpecificProjectDetails_Success() {
//        // given
//        Long projectId = 1L;
//        Project project = mock(Project.class);
//
//        when(projectRepository.findSpecificProjectDetailsById(projectId)).thenReturn(Optional.of(project));
//
//        // when
//        SpecificProjectDetailRes result = projectService.getSpecificProjectDetails(projectId);
//
//        // then
//        assertThat(result).isNotNull();
//        verify(projectRepository, times(1)).findSpecificProjectDetailsById(projectId);
//    }
//
//    @Test
//    void getSpecificProjectDetails_Fail() {
//        // given
//        Long projectId = 1L;
//
//        when(projectRepository.findSpecificProjectDetailsById(projectId)).thenReturn(Optional.empty());
//
//        // when / then
//        assertThatThrownBy(() -> projectService.getSpecificProjectDetails(projectId))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("프로젝트를 찾을 수 없습니다.");
//    }
//
//    @Test
//    void getFilteredSearchProjects_Success() {
//        // given
//        Long loginId = 1L;
//        ProjectFilterSearchReq req = mock(ProjectFilterSearchReq.class);
//        Pageable pageable = mock(Pageable.class);
//        RecruitingType recruitingType = RecruitingType.EVERYTHING;
//        ProjectFilterType projectFilterType = ProjectFilterType.LATEST;
//        String keyWord = "keyword";
//        Slice<Project> projects = mock(Slice.class);
//
//        when(req.getRecruitingType()).thenReturn(recruitingType.name());
//        when(req.getFilterType()).thenReturn(projectFilterType.name());
//        when(req.getKeyWord()).thenReturn(keyWord);
//        when(projectRepositoryCustom.findFilteredProjects(recruitingType, projectFilterType, keyWord, loginId, pageable)).thenReturn(projects);
//
//        // when
//        SliceRes<ProjectFilterSearchRes> result = projectService.getFilteredSearchProjects(loginId, req, pageable);
//
//        // then
//        assertThat(result).isNotNull();
//        verify(projectRepositoryCustom, times(1)).findFilteredProjects(recruitingType, projectFilterType, keyWord, loginId, pageable);
//    }
//
//    @Test
//    void getRecommendedProjects_Success() {
//        // given
//        Long loginId = 1L;
//        Project project = mock(Project.class);
//        List<Project> recommendedProjects = Collections.singletonList(project);
//
//        when(projectRepositoryCustom.findRecommendedProjects(loginId)).thenReturn(recommendedProjects);
//
//        // when
//        List<RecommendProjectRes> result = projectService.getRecommendedProjects(loginId);
//
//        // then
//        assertThat(result).hasSize(1);
//        verify(projectRepositoryCustom, times(1)).findRecommendedProjects(loginId);
//    }
//}
