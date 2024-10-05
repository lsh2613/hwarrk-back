package com.hwarrk.entity;


import com.hwarrk.common.constant.MemberStatus;
import com.hwarrk.common.constant.OauthProvider;
import com.hwarrk.common.constant.Role;
import com.hwarrk.common.exception.GeneralHandler;
import com.hwarrk.oauth2.member.OauthMember;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus.LAST_CAREER_NOT_FOUND;
import static com.hwarrk.common.dto.res.MemberRes.CareerInfo;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "MEMBER")
public class Member extends BaseEntity {

    private static final String NO_LAST_COMPANY_INFO = "없음";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String socialId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OauthProvider oauthProvider;

    private Role role;

    private MemberStatus memberStatus;

    private String image;

    // 중복 여부 체크 필요!
    private String nickname;

    private String birth;

    private String email;

    private String phone;

    private String introduction;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Portfolio> portfolios = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Position> positions = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Skill> skills = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Degree> degrees = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 10)
    private List<Career> careers = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 10)
    private List<ProjectLike> projectLikes = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 10)
    private List<ProjectMember> projectMembers = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectDescription> projectDescriptions = new ArrayList<>();

    private Double embers;

    private Boolean isVisible;

    private Integer views = 0;

    public Member(Long id) {
        this.id = id;
    }

    public void addPortfolios(List<Portfolio> portfolios) {
        this.portfolios.clear();
        this.portfolios.addAll(portfolios);
    }

    public void addPositions(List<Position> positions) {
        this.positions.clear();
        this.positions.addAll(positions);
    }

    public void addSkills(List<Skill> skills) {
        this.skills.clear();
        this.skills.addAll(skills);
    }

    public void addDegrees(List<Degree> degrees) {
        this.degrees.clear();
        this.degrees.addAll(degrees);
    }

    public Member(String nickname, String socialId, OauthProvider oauthProvider) {
        this.nickname = nickname;
        this.socialId = socialId;
        this.oauthProvider = oauthProvider;
    }

    public void addCareers(List<Career> careers) {
        this.careers.clear();
        this.careers.addAll(careers);
    }

    public void addProjectDescriptions(List<ProjectDescription> projectDescriptions) {
        this.projectDescriptions.clear();
        this.projectDescriptions.addAll(projectDescriptions);
    }

    public Member(String socialId, OauthProvider oauthProvider) {
        this.socialId = socialId;
        this.oauthProvider = oauthProvider;
        this.role = Role.GUEST;
    }

    public Member(OauthMember request) {
        this.socialId = request.getSocialId();
        this.oauthProvider = request.getOauthProvider();
        this.email = request.getEmail();
        this.nickname = request.getNickname();
        this.role = Role.GUEST;
    }

    @Builder
    public Member(MemberStatus memberStatus, String image, String nickname, String birth, String email, String phone,
                  List<Portfolio> portfolios, List<Position> positions, List<Skill> skills,
                  List<Degree> degrees,
                  List<Career> careers) {
        this.memberStatus = memberStatus;
        this.image = image;
        this.nickname = nickname;
        this.birth = birth;
        this.email = email;
        this.phone = phone;
        this.portfolios = portfolios;
        this.positions = positions;
        this.skills = skills;
        this.degrees = degrees;
        this.careers = careers;
    }

    public void addProjectLike(ProjectLike projectLike) {
        if (Optional.ofNullable(projectLikes).isEmpty()) {
            projectLikes = new ArrayList<>();
        }
        this.projectLikes.add(projectLike);
    }

    public void addProjectMember(ProjectMember projectMember) {
        if (Optional.ofNullable(projectMembers).isEmpty()) {
            projectMembers = new ArrayList<>();
        }
        this.projectMembers.add(projectMember);

    }

    public CareerInfo loadCareer() {
        if (careers.isEmpty()) {
            return CareerInfo.createEntryCareerInfo();
        }
        return getExperienceCareerInfo();
    }

    private CareerInfo getExperienceCareerInfo() {
        Period totalExperience = Period.ZERO;

        String lastCareer = NO_LAST_COMPANY_INFO;
        LocalDate lastLocalDate = LocalDate.MIN;

        for (Career career : this.careers) {
            totalExperience = totalExperience.plus(career.calculateExperience());
            LocalDate endDate = career.getEndDate();
            if (endDate.isAfter(lastLocalDate)) {
                lastLocalDate = endDate;
                lastCareer = career.getCompany();
            }
        }

        if (lastCareer.equals(NO_LAST_COMPANY_INFO)) {
            throw new GeneralHandler(LAST_CAREER_NOT_FOUND);
        }

        int totalMonths = totalExperience.getMonths() + totalExperience.getDays() / 30;
        int totalYears = totalExperience.getYears() + totalMonths / 12;

        return CareerInfo.createExperienceCareerInfo(totalYears, lastCareer);
    }

    public boolean isSameId(Long loginId) {
        return id.equals(loginId);
    }
}
