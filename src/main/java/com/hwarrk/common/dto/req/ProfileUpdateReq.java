package com.hwarrk.common.dto.req;

import com.hwarrk.common.constant.MemberStatus;
import com.hwarrk.common.constant.PositionType;
import com.hwarrk.common.constant.Role;
import com.hwarrk.common.constant.SkillType;
import com.hwarrk.entity.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public record ProfileUpdateReq(
        @NotNull
        @Size(min = 2, max = 10)
        String nickname,
        @NotNull
        MemberStatus memberStatus,
        @NotNull
        String email,
        @NotNull
        String introduction,
        List<String> portfolios,
        @NotNull
        List<PositionType> positions,
        @NotNull
        List<SkillType> skills,
        boolean isVisible,
        List<DegreeUpdateReq> degrees,
        List<CareerUpdateReq> careers,
        List<ProjectDescriptionUpdateReq> projectDescriptions
) {

    public Member mapReqToMember(String imageUrl, List<Position> positions, List<Portfolio> portfolios, List<Skill> skills,
                                 List<Degree> degrees, List<Career> careers, List<ProjectDescription> projectDescriptions) {
        return Member.builder()
                .memberStatus(memberStatus)
                .image(imageUrl)
                .nickname(nickname)
                .email(email)
                .introduction(introduction)
                .portfolios(portfolios)
                .positions(positions)
                .skills(skills)
                .isVisible(isVisible)
                .degrees(degrees)
                .careers(careers)
                .projectDescriptions(projectDescriptions)
                .build();
    }

    public record DegreeUpdateReq(
            String degreeType,
            String universityType,
            String school,
            String major,
            String graduationType,
            String entranceDate,
            String graduationDate
    ) {
        public Degree mapReqToEntity(Member member) {
            return Degree.builder()
                    .degreeType(degreeType)
                    .universityType(universityType)
                    .school(school)
                    .major(major)
                    .graduationType(graduationType)
                    .entranceDate(entranceDate)
                    .graduationDate(graduationDate)
                    .member(member)
                    .build();
        }
    }

    public record CareerUpdateReq(
            String company,
            String domain, // 직군
            String job, // 직무
            LocalDate startDate,
            LocalDate endDate,
            String description
    ) {
        public Career mapReqToEntity(Member member) {
            return Career.builder()
                    .company(company)
                    .domain(domain)
                    .job(job)
                    .startDate(startDate)
                    .endDate(endDate)
                    .description(description)
                    .member(member)
                    .build();
        }
    }

    public record ProjectDescriptionUpdateReq(
            Long projectId,
            String description
    ) {
        public ProjectDescription mapReqToEntity(Member member, Project project) {
            return ProjectDescription.builder()
                    .project(project)
                    .member(member)
                    .description(description)
                    .build();
        }
    }
}
