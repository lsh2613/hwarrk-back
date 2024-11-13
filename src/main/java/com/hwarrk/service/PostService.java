package com.hwarrk.service;

import static com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus.MEMBER_NOT_FOUND;
import static com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus.POST_NOT_FOUND;
import static com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus.PROJECT_NOT_FOUND;

import com.hwarrk.common.EntityFacade;
import com.hwarrk.common.constant.PositionType;
import com.hwarrk.common.constant.PostFilterType;
import com.hwarrk.common.constant.SkillType;
import com.hwarrk.common.constant.WayType;
import com.hwarrk.common.dto.dto.MemberWithLikeDto;
import com.hwarrk.common.dto.dto.PostWithLikeDto;
import com.hwarrk.common.dto.dto.RecruitingPositionDto;
import com.hwarrk.common.dto.req.PostCreateReq;
import com.hwarrk.common.dto.req.PostFilterSearchReq;
import com.hwarrk.common.dto.req.PostUpdateReq;
import com.hwarrk.common.dto.res.CareerInfoRes;
import com.hwarrk.common.dto.res.MemberCardRes;
import com.hwarrk.common.dto.res.MyPostRes;
import com.hwarrk.common.dto.res.PostFilterSearchRes;
import com.hwarrk.common.dto.res.ProjectMemberRes;
import com.hwarrk.common.dto.res.RecommendPostRes;
import com.hwarrk.common.dto.res.SpecificPostDetailRes;
import com.hwarrk.common.exception.GeneralHandler;
import com.hwarrk.entity.Member;
import com.hwarrk.entity.Position;
import com.hwarrk.entity.Post;
import com.hwarrk.entity.Project;
import com.hwarrk.entity.ProjectMember;
import com.hwarrk.entity.RecruitingPosition;
import com.hwarrk.entity.Skill;
import com.hwarrk.repository.MemberRepository;
import com.hwarrk.repository.MemberRepositoryCustom;
import com.hwarrk.repository.PostRepository;
import com.hwarrk.repository.PostRepositoryCustom;
import com.hwarrk.repository.ProjectRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final EntityFacade entityFacade;
    private final PostRepository postRepository;
    private final PostRepositoryCustom postRepositoryCustom;
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepositoryCustom memberRepositoryCustom;

    public Long createPost(PostCreateReq req, Long loginId) {
        Project project = entityFacade.getProject(req.getProjectId());
        validateDuplicatePost(project);
        validateProjectLeader(loginId, project);

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

    private void validateDuplicatePost(Project project) {
        if (Optional.ofNullable(postRepository.findByProject(project))
                .isPresent()) {
            throw new IllegalStateException("프로젝트에 이미 공고가 존재합니다.");
        }
    }

    private void validateProjectLeader(Long loginId, Project project) {
        if (!project.isProjectLeader(loginId)) {
            throw new IllegalArgumentException("프로젝트를 생성한 리더가 아닙니다.");
        }
    }

    public void updatePost(PostUpdateReq req, Long loginId, Long postId) {
        Post post = entityFacade.getPost(postId);
        validateProjectLeader(loginId, post.getProject());
        post.updatePost(req.getTitle(), req.getBody(), req.getSkills());
        post.addSkills(req.getSkills());

        for (RecruitingPositionDto positionReq : req.getRecruitingPositionDtoList()) {
            RecruitingPosition recruitingPosition = RecruitingPosition.create(positionReq.getPositionType(),
                    positionReq.getNumber());
            recruitingPosition.addPost(post);
        }
    }

    public SpecificPostDetailRes findSpecificPostInfo(Long postId, Long memberId) {
        Post post = postRepository.findPostWithPositions(postId).orElseThrow(() -> new GeneralHandler(POST_NOT_FOUND));
        postRepository.findPostWithSkills(postId).orElseThrow(() -> new GeneralHandler(POST_NOT_FOUND));

        Member member = entityFacade.getMember(memberId);

        Project project = projectRepository.findProjectMembersAndMembersById(post.getProject().getId())
                .orElseThrow(() -> new GeneralHandler(PROJECT_NOT_FOUND));

        Set<ProjectMember> projectMembers = project.getProjectMembers();

        return SpecificPostDetailRes.createRes(post, createProjectMemberResList(projectMembers, member),
                createMemberResList(post, memberId));
    }

    private List<ProjectMemberRes> createProjectMemberResList(Set<ProjectMember> projectMembers,
                                                              Member fromMember) {
        return projectMembers.stream()
                .map(pm -> {
                    Member member = pm.getMember();
                    return ProjectMemberRes.createRes(member,
                            CareerInfoRes.mapEntityToRes(member.loadCareer()),
                            member.isFollower(fromMember));
                })
                .toList();
    }

    private List<MemberCardRes> createMemberResList(Post post, Long memberId) {
        List<MemberWithLikeDto> recommendedMembers = memberRepositoryCustom.findRecommendedMembers(post.getSkills(),
                memberId);
        return recommendedMembers.stream()
                .map(m -> {
                    Member member = m.member();
                    return MemberCardRes.mapEntityToRes(member, CareerInfoRes.mapEntityToRes(member.loadCareer()),
                            m.isLiked());
                })
                .toList();
    }

    public void deletePost(Long loginId, Long postId) {
        Post post = entityFacade.getPost(postId);
        validateProjectLeader(loginId, post.getProject());
        postRepository.delete(post);
    }

    public List<MyPostRes> findMyPosts(Long memberId) {
        Member member = entityFacade.getMember(memberId);
        List<Post> myPosts = postRepository.findPostsByMember(member.getId());
        return myPosts.stream().map(MyPostRes::mapEntityToRes).toList();
    }

    public List<PostFilterSearchRes> findFilteredPost(PostFilterSearchReq req, Long memberId) {
        Member member = entityFacade.getMember(memberId);

        WayType wayType = findProperWayType(req);
        PositionType positionType = PositionType.findType(req.getPositionType());
        SkillType skillType = SkillType.findType(req.getSkillType());
        PostFilterType filterType = PostFilterType.findType(req.getFilterType());

        List<PostWithLikeDto> postWithLikeDtos = postRepositoryCustom.findFilteredPost(positionType, wayType, skillType,
                filterType, req.getKeyWord(), member.getId());

        return postWithLikeDtos.stream()
                .map(PostFilterSearchRes::createRes)
                .toList();
    }

    private WayType findProperWayType(PostFilterSearchReq req) {
        if (Optional.ofNullable(req.getWayType()).isEmpty()) {
            return WayType.NONE;
        }
        return WayType.valueOf(req.getWayType());
    }

    public List<RecommendPostRes> findRecommendPosts(Long memberId) {
        Member member = memberRepository.findMemberStacksById(memberId)
                .orElseThrow(() -> new GeneralHandler(MEMBER_NOT_FOUND));

        List<Skill> skills = member.getSkills();
        List<SkillType> skillTypes = skills.stream().map(Skill::getSkillType).toList();

        List<Position> positions = member.getPositions();
        List<PositionType> positionTypes = positions.stream().map(Position::getPositionType).toList();

        List<PostWithLikeDto> postWithLikeDtos = postRepositoryCustom.findPostsBySkillsAndPositions(skillTypes,
                positionTypes, memberId);

        return postWithLikeDtos.stream()
                .map(RecommendPostRes::createRes)
                .toList();
    }
}
