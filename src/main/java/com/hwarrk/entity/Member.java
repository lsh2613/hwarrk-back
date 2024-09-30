package com.hwarrk.entity;


import com.hwarrk.common.constant.MemberStatus;
import com.hwarrk.common.constant.OauthProvider;
import com.hwarrk.common.constant.Role;
import com.hwarrk.common.dto.req.UpdateProfileReq;
import com.hwarrk.oauth2.member.OauthMember;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "MEMBER")
public class Member extends BaseEntity {
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

    private String email;


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
    private List<Career> careers = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectDescription> projectDescriptions = new ArrayList<>();

    private Double embers;

    private Boolean isVisible;

    private Integer views = 0;

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
    }

    public Member(OauthMember request) {
        this.socialId = request.getSocialId();
        this.oauthProvider = request.getOauthProvider();
        this.email = request.getEmail();
        this.nickname = request.getNickname();
        this.role = Role.GUEST;
    }

    @Builder
    public Member(MemberStatus memberStatus, String image, String nickname, String email, List<Portfolio> portfolios, List<Position> positions, List<Skill> skills, List<Degree> degrees, List<Career> careers) {
        this.memberStatus = memberStatus;
        this.image = image;
        this.nickname = nickname;
        this.email = email;
        this.portfolios = portfolios;
        this.positions = positions;
        this.skills = skills;
        this.degrees = degrees;
        this.careers = careers;
    }

    public void updateProfile(UpdateProfileReq updateProfileReq) {
        this.nickname = updateProfileReq.nickname();
        this.memberStatus = updateProfileReq.memberStatus();
        this.email = updateProfileReq.email();
        this.introduction = updateProfileReq.introduction();
        addPositions(updateProfileReq.mapReqToPositions(this));
        addPortfolios(updateProfileReq.mapReqToPortfolios(this));
        addSkills(updateProfileReq.mapReqToSkills(this));
        this.isVisible = updateProfileReq.isVisible();
        addDegrees(updateProfileReq.mapReqToDegrees(this));
        addCareers(updateProfileReq.mapReqToCareers(this));
    }
}
