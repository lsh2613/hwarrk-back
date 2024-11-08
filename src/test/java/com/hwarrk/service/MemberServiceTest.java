package com.hwarrk.service;

import com.hwarrk.common.EntityFacade;
import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.common.constant.*;
import com.hwarrk.common.dto.req.ProfileCond;
import com.hwarrk.common.dto.req.ProfileUpdateReq;
import com.hwarrk.common.dto.res.MemberRes;
import com.hwarrk.common.dto.res.MyProfileRes;
import com.hwarrk.common.dto.res.PageRes;
import com.hwarrk.common.dto.res.ProfileRes;
import com.hwarrk.common.exception.GeneralHandler;
import com.hwarrk.entity.Member;
import com.hwarrk.entity.MemberLike;
import com.hwarrk.entity.Project;
import com.hwarrk.entity.ProjectStatus;
import com.hwarrk.jwt.TokenProvider;
import com.hwarrk.redis.RedisTokenUtil;
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

import static com.hwarrk.common.dto.req.ProfileUpdateReq.*;
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
    private RedisTokenUtil redisTokenUtil;

    Member member_01;

    Project project_01;
    Project project_02;

    String nickname = "LSH";
    MemberStatus memberStatus = MemberStatus.사프_찾는_중;
    String email = "test@test.com";
    String introduction = "My introduction";
    List<String> portfolios = List.of("Portfolio_01", "Portfolio_02");
    List<PositionType> positions = List.of(PositionType.PM, PositionType.BACKEND);
    List<SkillType> skills = List.of(SkillType.JAVA, SkillType.SPRING);
    boolean isVisible = true;
    List<DegreeUpdateReq> degrees = List.of(
            new DegreeUpdateReq(
                    "University",
                    "University A",
                    "School A",
                    "Computer Science",
                    "졸업",
                    "2010-09-01",
                    "2014-06-01"
            ),
            new DegreeUpdateReq(
                    "University",
                    "University B",
                    "School B",
                    "Software Engineering",
                    "졸업 예정자",
                    "2015-09-01",
                    "2017-06-01"
            )
    );
    List<CareerUpdateReq> careers = List.of(
            new CareerUpdateReq(
                    "Company A",
                    "Engineering",
                    "Software Engineer",
                    LocalDate.of(2018, 01, 01),
                    LocalDate.of(2020, 12, 31),
                    "Developed software"
            ),
            new CareerUpdateReq(
                    "Company B",
                    "Product Design",
                    "UX Designer",
                    LocalDate.of(2021, 01, 01),
                    LocalDate.of(2023, 11, 11),
                    "Designed UX"
            )
    );
    List<ProjectDescriptionUpdateReq> projectDescriptions;
    List<ExternalProjectDescriptionUpdateReq> externalProjectDescriptions = List.of(
            new ExternalProjectDescriptionUpdateReq(
                    "Project Alpha",
                    "www.domain-01.com",
                    LocalDate.of(2023, 1, 10),
                    LocalDate.of(2023, 6, 15),
                    ProjectStatus.ONGOING,
                    PositionType.BACKEND,
                    "subject_01",
                    "description_012"
            ),
            new ExternalProjectDescriptionUpdateReq(
                    "Project Beta",
                    "www.domain-02.com",
                    LocalDate.of(2023, 7, 1),
                    LocalDate.of(2023, 12, 31),
                    ProjectStatus.COMPLETE,
                    PositionType.ANDROID,
                    "subject_02",
                    "description_02"
            )
    );

    @BeforeEach
    void setup() {
        member_01 = memberRepository.save(new Member("test_01", OauthProvider.KAKAO));
        project_01 = projectRepository.save(new Project("Project name", "Project introduction", member_01));
        project_02 = projectRepository.save(new Project("Project name", "Project introduction", member_01));
        projectDescriptions = List.of(
                new ProjectDescriptionUpdateReq(project_01.getId(), "Project description_01"),
                new ProjectDescriptionUpdateReq(project_02.getId(), "Project description_02")
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
        assertThat(redisTokenUtil.getMemberId(refreshToken)).isNull();
        assertThat(redisTokenUtil.isBlacklistedToken(accessToken)).isTrue();
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
        assertThat(redisTokenUtil.getMemberId(refreshToken)).isNull();
        assertThat(redisTokenUtil.isBlacklistedToken(accessToken)).isTrue();
    }

    @Test
    void 블랙리스트_검증_실패() {
        //given

        //when

        //then
        assertThat(redisTokenUtil.isBlacklistedToken("NotToken")).isFalse();
    }

    @Test
    void 프로필_작성() {
        //given
        ProfileUpdateReq req = new ProfileUpdateReq(nickname, memberStatus, email, introduction, portfolios, positions, skills,
                isVisible, degrees, careers, projectDescriptions, externalProjectDescriptions);

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
        assertThat(member_01.getExternalProjectDescriptions().size()).isEqualTo(externalProjectDescriptions.size());
    }

    @Test
    void 프로필_수정() {
        //given
        ProfileUpdateReq req = new ProfileUpdateReq(nickname, memberStatus, email, introduction, null, positions,
                null, isVisible, null, null, projectDescriptions, externalProjectDescriptions);
        memberService.updateMember(member_01.getId(), req, null);

        String updateNickname = "홍길동";
        MemberStatus updateMemberStatus = MemberStatus.이직_구직_중;
        String updateEmail = "홍길동@test.com";
        String updateIntroduction = "홍길동";
        List<PositionType> updatePositions = List.of(PositionType.FRONTEND, PositionType.GRAPHIC_DESIGNER, PositionType.PO);
        boolean updateIsVisible = true;
        List<ProjectDescriptionUpdateReq> updateProjectDescriptions = List.of(
                new ProjectDescriptionUpdateReq(project_01.getId(), "Update Project description_01")
        );
        List<ExternalProjectDescriptionUpdateReq> updateExternalProjectDescriptions = List.of(
                new ExternalProjectDescriptionUpdateReq(
                        "Updated Project",
                        "www.update.com",
                        LocalDate.of(2024, 1, 10),
                        LocalDate.of(2024, 6, 15),
                        ProjectStatus.ONGOING,
                        PositionType.BACKEND,
                        "updated_subject_01",
                        "updated_description_012"
                )
        );
        ProfileUpdateReq updateReq = new ProfileUpdateReq(
                updateNickname, updateMemberStatus, updateEmail, updateIntroduction,
                null, updatePositions, null, updateIsVisible, null,
                null, updateProjectDescriptions, updateExternalProjectDescriptions
        );

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
        assertThat(member_01.getExternalProjectDescriptions().size()).isEqualTo(updateExternalProjectDescriptions.size());
    }

    @Test
    void 나의_프로필_조회() {
        //given
        ProfileUpdateReq req = new ProfileUpdateReq(nickname, memberStatus, email, introduction, portfolios,
                positions, skills, isVisible, degrees, careers, projectDescriptions, externalProjectDescriptions);
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
        assertThat(res.externalProjectDescriptions().size()).isEqualTo(externalProjectDescriptions.size());
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
        ProfileUpdateReq req = new ProfileUpdateReq(nickname, memberStatus, email, introduction, portfolios,
                positions, skills, isVisible, degrees, careers, projectDescriptions, externalProjectDescriptions);
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
        assertThat(res.externalProjectDescriptions().size()).isEqualTo(externalProjectDescriptions.size());

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
        ProfileUpdateReq req = new ProfileUpdateReq(nickname, memberStatus, email, introduction, portfolios,
                positions, skills, isVisible, degrees, careers, projectDescriptions, externalProjectDescriptions);
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
        assertThat(content_01.getMemberId()).isEqualTo(member_03.getId());
        assertThat(content_01.isLiked()).isFalse();

        MemberRes content_02 = content.get(1); // member_02
        assertThat(content_02.getMemberId()).isEqualTo(member_02.getId());
        assertThat(content_02.isLiked()).isTrue();
    }

}
