package com.hwarrk.service;

import com.hwarrk.common.EntityFacade;
import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.common.constant.*;
import com.hwarrk.common.dto.req.ProfileCond;
import com.hwarrk.common.dto.req.UpdateProfileReq;
import com.hwarrk.common.dto.res.MemberRes;
import com.hwarrk.common.dto.res.MyProfileRes;
import com.hwarrk.common.dto.res.PageRes;
import com.hwarrk.common.dto.res.ProfileRes;
import com.hwarrk.common.exception.GeneralHandler;
import com.hwarrk.entity.Member;
import com.hwarrk.entity.MemberLike;
import com.hwarrk.entity.Project;
import com.hwarrk.jwt.TokenProvider;
import com.hwarrk.redis.RedisUtil;
import com.hwarrk.repository.MemberLikeRepository;
import com.hwarrk.repository.MemberRepository;
import com.hwarrk.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.hwarrk.common.dto.req.UpdateProfileReq.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class MemberServiceTest {

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private MemberLikeRepository memberLikeRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private EntityFacade entityFacade;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private RedisUtil redisUtil;

    Member member_01;

    Project project_01;
    Project project_02;

    List<UpdateProjectDescriptionReq> projectDescriptions;

    String nickname = "LSH";
    MemberStatus memberStatus = MemberStatus.사프_찾는_중;
    String email = "test@test.com";
    String introduction = "My introduction";
    List<String> portfolios = List.of("Portfolio_01", "Portfolio_02");
    List<PositionType> positions = List.of(PositionType.PM, PositionType.BACKEND);
    List<SkillType> skills = List.of(SkillType.JAVA, SkillType.SPRING);
    boolean isVisible = true;
    List<UpdateDegreeReq> degrees = List.of(
            new UpdateDegreeReq(
                    "University",
                    "University A",
                    "School A",
                    "Computer Science",
                    "졸업",
                    "2010-09-01",
                    "2014-06-01"
            ),
            new UpdateDegreeReq(
                    "University",
                    "University B",
                    "School B",
                    "Software Engineering",
                    "졸업 예정자",
                    "2015-09-01",
                    "2017-06-01"
            )
    );
    List<UpdateCareerReq> careers = List.of(
            new UpdateCareerReq(
                    "Company A",
                    "Engineering",
                    "Software Engineer",
                    LocalDate.of(2018, 01, 01),
                    LocalDate.of(2020, 12, 31),
                    "Developed software"
            ),
            new UpdateCareerReq(
                    "Company B",
                    "Product Design",
                    "UX Designer",
                    LocalDate.of(2021, 01, 01),
                    LocalDate.of(2023, 11, 11),
                    "Designed UX"
            )
    );

    @BeforeEach
    void setup() {
        member_01 = memberRepository.save(new Member("test_01", OauthProvider.KAKAO));
        project_01 = projectRepository.save(new Project("Project name", "Project introduction", member_01));
        project_02 = projectRepository.save(new Project("Project name", "Project introduction", member_01));
        projectDescriptions = List.of(
                new UpdateProjectDescriptionReq(project_01.getId(), "Project description_01"),
                new UpdateProjectDescriptionReq(project_02.getId(), "Project description_02")
        );
    }

    @Test
    void 회원_삭제_성공() {
        //given

        //when
        memberService.deleteMember(member_01.getId());

        //then
        assertThrows(GeneralHandler.class, () -> entityFacade.getMember(member_01.getId()));
    }

    @Test
    void 회원_삭제_실패() {
        //then
        assertThrows(GeneralHandler.class, () -> memberService.deleteMember(999L));
    }

    @Test
    void 로그아웃_성공() {
        //given

        String accessToken = tokenProvider.issueAccessToken(member_01.getId());
        String refreshToken = tokenProvider.issueRefreshToken(member_01.getId());

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(accessHeader, "Bearer " + accessToken);
        request.addHeader(refreshHeader, "Bearer " + refreshToken);

        //when
        memberService.logout(request);

        //then
        assertThat(redisUtil.getData(refreshToken)).isNull();
        assertThat(redisUtil.containsInBlackList(accessToken)).isTrue();
    }

    @Test
    void 블랙리스트_검증_성공() {
        //given
        String accessToken = tokenProvider.issueAccessToken(member_01.getId());
        String refreshToken = tokenProvider.issueRefreshToken(member_01.getId());

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(accessHeader, "Bearer " + accessToken);
        request.addHeader(refreshHeader, "Bearer " + refreshToken);

        //when
        memberService.logout(request);

        //then
        assertThat(redisUtil.getData(refreshToken)).isNull();
        assertThat(redisUtil.isBlacklistedToken(accessToken)).isTrue();
    }

    @Test
    void 블랙리스트_검증_실패() {
        //given

        //when

        //then
        assertThat(redisUtil.isBlacklistedToken("NotToken")).isFalse();
    }

    @Test
    void 프로필_작성() {
        //given
        UpdateProfileReq req = new UpdateProfileReq(nickname, memberStatus, email, introduction, portfolios, positions, skills, isVisible, degrees, careers, projectDescriptions);

        //when
        memberService.updateMember(member_01.getId(), req, null);

        //then
        assertThat(member_01.getNickname()).isEqualTo(nickname);
        assertThat(member_01.getMemberStatus()).isEqualTo(memberStatus);
        assertThat(member_01.getEmail()).isEqualTo(email);
        assertThat(member_01.getIntroduction()).isEqualTo(introduction);
        assertThat(member_01.getPortfolios().size()).isEqualTo(portfolios.size());
        assertThat(member_01.getPositions().size()).isEqualTo(positions.size());
        assertThat(member_01.getSkills().size()).isEqualTo(skills.size());
        assertThat(member_01.getIsVisible()).isEqualTo(isVisible);
        assertThat(member_01.getDegrees().size()).isEqualTo(degrees.size());
        assertThat(member_01.getCareers().size()).isEqualTo(careers.size());
        assertThat(member_01.getProjectDescriptions().size()).isEqualTo(projectDescriptions.size());
        assertThat(member_01.getRole()).isEqualTo(Role.USER);
    }

    @Test
    void 프로필_수정() {
        //given
        UpdateProfileReq req = new UpdateProfileReq(nickname, memberStatus, email, introduction, null, positions, null, isVisible, null, null, projectDescriptions);
        memberService.updateMember(member_01.getId(), req, null);

        String updateNickname = "홍길동";
        MemberStatus updateMemberStatus = MemberStatus.이직_구직_중;
        String updateEmail = "홍길동@test.com";
        String updateIntroduction = "홍길동";
        List<PositionType> updatePositions = List.of(PositionType.FRONTEND, PositionType.GRAPHIC_DESIGNER, PositionType.PO);
        boolean updateIsVisible = true;
        List<UpdateProjectDescriptionReq> updateProjectDescriptions = List.of(
                new UpdateProjectDescriptionReq(project_01.getId(), "Update Project description_01")
        );

        UpdateProfileReq updateReq = new UpdateProfileReq(
                updateNickname, updateMemberStatus, updateEmail, updateIntroduction, null, updatePositions, null, updateIsVisible, null, null, updateProjectDescriptions);

        //when
        memberService.updateMember(member_01.getId(), updateReq, null);

        //then
        assertThat(member_01.getNickname()).isEqualTo(updateNickname);
        assertThat(member_01.getMemberStatus()).isEqualTo(updateMemberStatus);
        assertThat(member_01.getEmail()).isEqualTo(updateEmail);
        assertThat(member_01.getIntroduction()).isEqualTo(updateIntroduction);
        assertThat(member_01.getPortfolios().size()).isEqualTo(0);
        assertThat(member_01.getPositions().size()).isEqualTo(updatePositions.size());
        assertThat(member_01.getSkills().size()).isEqualTo(0);
        assertThat(member_01.getIsVisible()).isEqualTo(updateIsVisible);
        assertThat(member_01.getSkills().size()).isEqualTo(0);
        assertThat(member_01.getDegrees().size()).isEqualTo(0);
        assertThat(member_01.getCareers().size()).isEqualTo(0);
        assertThat(member_01.getProjectDescriptions().size()).isEqualTo(updateProjectDescriptions.size());
        assertThat(member_01.getRole()).isEqualTo(Role.USER);
    }

    @Test
    void 나의_프로필_조회() {
        //given
        UpdateProfileReq req = new UpdateProfileReq(nickname, memberStatus, email, introduction, portfolios, positions, skills, isVisible, degrees, careers, projectDescriptions);
        memberService.updateMember(member_01.getId(), req, null);

        //when
        MyProfileRes res = memberService.getMyProfile(member_01.getId());

        //then
        assertThat(res.nickname()).isEqualTo(nickname);
        assertThat(res.memberStatus()).isEqualTo(memberStatus);
        assertThat(res.email()).isEqualTo(email);
        assertThat(res.introduction()).isEqualTo(introduction);
        assertThat(res.portfolios().size()).isEqualTo(portfolios.size());
        assertThat(res.positions().size()).isEqualTo(positions.size());
        assertThat(res.skills().size()).isEqualTo(skills.size());
        assertThat(res.isVisible()).isEqualTo(isVisible);
        assertThat(res.degrees().size()).isEqualTo(degrees.size());
        assertThat(res.careers().size()).isEqualTo(careers.size());
        assertThat(res.projectDescriptions().size()).isEqualTo(projectDescriptions.size());
    }

    /**
     * 스토리
     * 1. member_01 프로필 작성
     * 2. member_02 -> member_01 찜
     * 3. member_02가 member_01 프로필 조회
     */
    @Test
    void 남의_프로필_조회_성공() {
        //given
        Member member_02 = memberRepository.save(new Member("test_02", OauthProvider.KAKAO));
        member_02.setRole(Role.USER);
        UpdateProfileReq req = new UpdateProfileReq(nickname, memberStatus, email, introduction, portfolios, positions, skills, isVisible, degrees, careers, projectDescriptions);
        memberService.updateMember(member_01.getId(), req, null);

        memberLikeRepository.save(new MemberLike(member_02, member_01));

        //when
        ProfileRes res = memberService.getProfile(member_02.getId(), member_01.getId());

        //then
        assertThat(res.nickname()).isEqualTo(nickname);
        assertThat(res.memberStatus()).isEqualTo(memberStatus);
        assertThat(res.email()).isEqualTo(email);
        assertThat(res.introduction()).isEqualTo(introduction);
        assertThat(res.portfolios().size()).isEqualTo(portfolios.size());
        assertThat(res.positions().size()).isEqualTo(positions.size());
        assertThat(res.skills().size()).isEqualTo(skills.size());
        assertThat(res.isLiked()).isTrue();
        assertThat(res.degrees().size()).isEqualTo(degrees.size());
        assertThat(res.careers().size()).isEqualTo(careers.size());
        assertThat(res.projectDescriptions().size()).isEqualTo(projectDescriptions.size());

        Member member = memberRepository.findById(member_01.getId()).get();
        assertThat(member.getViews()).isEqualTo(1);
    }

    @Test
    void 남의_프로필_조회_실패_01() {
        //given
        Member member_02 = memberRepository.save(new Member("test_02", OauthProvider.KAKAO));

        //when

        //then
        GeneralHandler e = assertThrows(GeneralHandler.class, () -> memberService.getProfile(member_02.getId(), member_01.getId()));
        assertThat(e.getErrorStatus()).isEqualTo(ErrorStatus.GUEST_ROLE_FORBIDDEN);
    }

    @Test
    void 남의_프로필_조회_실패_02() {
        //given
        Member member_02 = memberRepository.save(new Member("test_02", OauthProvider.KAKAO));
        member_01.setRole(Role.USER);
        member_02.setIsVisible(false);

        //when

        //then
        GeneralHandler e = assertThrows(GeneralHandler.class, () -> memberService.getProfile(member_01.getId(), member_02.getId()));
        assertThat(e.getErrorStatus()).isEqualTo(ErrorStatus.PROFILE_NOT_VISIBLE);
    }

    /**
     * 스토리
     * 1. member_02, member_03 프로필 등록
     * 2. 검색 조건 (포지션-백엔드, 기술-SPRING, 사용자 상태 - 사프 찾는 중, 필터 - 인기순, 검색 키워드- 'LSH'
     * 3. member_01 -> member_02 찜
     * 4. 최신순이므로 member_03부터 조회
     */
    @Test
    void 프로필_허브_조회_with_필터_검색() {
        //given
        Member member_02 = memberRepository.save(new Member("test_02", OauthProvider.KAKAO));
        Member member_03 = memberRepository.save(new Member("test_03", OauthProvider.KAKAO));
        UpdateProfileReq req = new UpdateProfileReq(nickname, memberStatus, email, introduction, portfolios, positions, skills, isVisible, degrees, careers, projectDescriptions);
        memberService.updateMember(member_02.getId(), req, null);
        memberService.updateMember(member_03.getId(), req, null);

        ProfileCond cond = new ProfileCond(PositionType.BACKEND, SkillType.SPRING, MemberStatus.사프_찾는_중, FilterType.LATEST, "LSH");

        memberLikeRepository.save(new MemberLike(member_01, member_02));

        //when
        PageRes res = memberService.getMembers(member_01.getId(), cond, PageRequest.of(0, 2));

        //then
        List<MemberRes> content = res.content();
        assertThat(content.size()).isEqualTo(2);
        assertThat(res.totalElements()).isEqualTo(2);
        assertThat(res.totalPages()).isEqualTo(1);
        assertThat(res.isLast()).isTrue();

        MemberRes content_01 = content.get(0); // member_03
        assertThat(content_01.memberId()).isEqualTo(member_03.getId());
        assertThat(content_01.isLiked()).isFalse();

        MemberRes content_02 = content.get(1); // member_02
        assertThat(content_02.memberId()).isEqualTo(member_02.getId());
        assertThat(content_02.isLiked()).isTrue();
    }

}
