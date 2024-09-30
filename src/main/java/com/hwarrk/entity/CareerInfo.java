package com.hwarrk.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Period;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "CAREER_INFO")
public class CareerInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "career_info_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private CareerType careerType;

    private Period totalExperience;
    private String lastCareer;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "project_member_id")
    private ProjectMember projectMember;

    public void addProjectMember(ProjectMember projectMember) {
        this.projectMember = projectMember;
        projectMember.addCareerInfo(this);
    }

    public static CareerInfo createEntryCareerInfo() {
        CareerInfo careerInfo = new CareerInfo();
        careerInfo.careerType = CareerType.ENTRY_LEVEL;
        careerInfo.totalExperience = Period.ZERO;
        careerInfo.lastCareer = "없음";
        return careerInfo;
    }

    public static CareerInfo createExperienceCareerInfo(Period totalExperience, String lastCareer) {
        CareerInfo careerInfo = new CareerInfo();
        careerInfo.careerType = CareerType.EXPERIENCE;
        careerInfo.totalExperience = totalExperience;
        careerInfo.lastCareer = lastCareer;
        return careerInfo;
    }
}
