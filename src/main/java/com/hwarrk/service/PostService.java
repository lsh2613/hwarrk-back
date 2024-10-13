package com.hwarrk.service;

import static com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus.MEMBER_NOT_FOUND;

import com.hwarrk.common.EntityFacade;
import com.hwarrk.common.constant.PositionType;
import com.hwarrk.common.constant.PostFilterType;
import com.hwarrk.common.constant.SkillType;
import com.hwarrk.common.constant.WayType;
import com.hwarrk.common.dto.dto.PostWithLikeDto;
import com.hwarrk.common.dto.dto.RecruitingPositionDto;
import com.hwarrk.common.dto.req.PostCreateReq;
import com.hwarrk.common.dto.req.PostFilterSearchReq;
import com.hwarrk.common.dto.req.PostUpdateReq;
import com.hwarrk.common.dto.res.MyPostRes;
import com.hwarrk.common.dto.res.PostFilterSearchRes;
import com.hwarrk.common.dto.res.RecommendPostRes;
import com.hwarrk.common.exception.GeneralHandler;
import com.hwarrk.entity.Member;
import com.hwarrk.entity.Position;
import com.hwarrk.entity.Post;
import com.hwarrk.entity.Project;
import com.hwarrk.entity.RecruitingPosition;
import com.hwarrk.entity.Skill;
import com.hwarrk.repository.MemberRepository;
import com.hwarrk.repository.PostLikeRepository;
import com.hwarrk.repository.PostRepository;
import com.hwarrk.repository.PostRepositoryCustom;
import java.util.List;
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
    private final PostLikeRepository postLikeRepository;
    private final PostRepositoryCustom postRepositoryCustom;
    private final MemberRepository memberRepository;

    public Long createPost(PostCreateReq req) {
        Project project = entityFacade.getProject(req.getProjectId());
        if (Optional.ofNullable(postRepository.findByProject(project))
                .isPresent()) {
            throw new IllegalStateException("프로젝트에 이미 공고가 존재합니다.");
        }

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

    public void updatePost(PostUpdateReq req) {
        Post post = entityFacade.getPost(req.getPostId());
        post.updatePost(req.getTitle(), req.getBody(), req.getSkills());
        post.addSkills(req.getSkills());

        for (RecruitingPositionDto positionReq : req.getRecruitingPositionDtoList()) {
            RecruitingPosition recruitingPosition = RecruitingPosition.create(positionReq.getPositionType(),
                    positionReq.getNumber());
            recruitingPosition.addPost(post);
        }
    }

    public void deletePost(Long postId) {
        Post post = entityFacade.getPost(postId);
        postRepository.delete(post);
    }

    public List<MyPostRes> findMyPosts(Long memberId) {
        Member member = entityFacade.getMember(memberId);
        List<Post> myPosts = postRepository.findPostsByMember(member.getId());
        return myPosts.stream().map(p -> MyPostRes.mapEntityToRes(p, p.isPostLike(member))).toList();
    }

    public List<PostFilterSearchRes> findFilteredPost(PostFilterSearchReq req, Long memberId) {
        Member member = entityFacade.getMember(memberId);

        PositionType positionType = PositionType.findType(req.getPositionType());
        WayType wayType = WayType.valueOf(req.getWayType());
        SkillType skillType = SkillType.findType(req.getSkillType());
        PostFilterType filterType = PostFilterType.findType(req.getFilterType());

        List<PostWithLikeDto> postWithLikeDtos = postRepositoryCustom.findFilteredPost(positionType, wayType, skillType,
                filterType, req.getKeyWord(), member.getId());

        return postWithLikeDtos.stream()
                .map(PostFilterSearchRes::createRes)
                .toList();
    }

    public List<RecommendPostRes> findRecommendPosts(Long memberId) {
        Member member = memberRepository.findMemberStacksById(memberId)
                .orElseThrow(() -> new GeneralHandler(MEMBER_NOT_FOUND));

        List<Skill> skills = member.getSkills();
        List<SkillType> skillTypes = skills.stream().map(Skill::getSkillType).toList();

        List<Position> positions = member.getPositions();
        List<PositionType> positionTypes = positions.stream().map(Position::getPositionType).toList();

        List<PostWithLikeDto> postWithLikeDtos = postRepositoryCustom.findPostsBySkillsAndPositions(skillTypes,
                positionTypes);

        return postWithLikeDtos.stream()
                .map(RecommendPostRes::createRes)
                .toList();
    }
}
