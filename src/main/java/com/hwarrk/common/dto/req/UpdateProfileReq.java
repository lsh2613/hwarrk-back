package com.hwarrk.common.dto.req;

import com.hwarrk.common.constant.MemberStatus;
import com.hwarrk.common.constant.PositionType;
import com.hwarrk.entity.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public record UpdateProfileReq(
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
        List<String> skills,
        boolean isVisible,
        List<UpdateDegreeReq> degrees,
        List<UpdateCareerReq> careers,
        List<UpdateProjectDescriptionReq> projectDescriptions
) {
    public List<Portfolio> mapReqToPortfolios(Member member) {
        return portfolios == null ?
                Collections.emptyList() : portfolios.stream().map(portfolioLink -> new Portfolio(portfolioLink, member)).toList();
    }

    public List<Position> mapReqToPositions(Member member) {
        return positions == null ?
                Collections.emptyList() : positions.stream().map(position -> new Position(position, member)).toList();
    }

    public List<Skill> mapReqToSkills(Member member) {
        return skills == null ?
                Collections.emptyList() : skills.stream().map(skill -> new Skill(skill, member)).toList();
    }

    public List<Degree> mapReqToDegrees(Member member) {
        return degrees == null ?
                Collections.emptyList() : degrees.stream().map(degreeReq -> degreeReq.mapReqToEntity(member)).toList();
    }

    public List<Career> mapReqToCareers(Member member) {
        return careers == null ?
                Collections.emptyList() : careers.stream().map(careerReq -> careerReq.mapReqToEntity(member)).toList();
    }

    public record UpdateDegreeReq(
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

    public record UpdateCareerReq(
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

    public record UpdateProjectDescriptionReq(
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
