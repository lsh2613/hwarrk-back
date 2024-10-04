package com.hwarrk.entity;

import java.time.Period;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CareerInfo {

    private CareerType careerType;
    private Period totalExperience;
    private String lastCareer;
    private ProjectMember projectMember;

    public static CareerInfo createEntryCareerInfo(ProjectMember projectMember) {
        CareerInfo careerInfo = new CareerInfo();
        careerInfo.careerType = CareerType.ENTRY_LEVEL;
        careerInfo.totalExperience = Period.ZERO;
        careerInfo.lastCareer = "없음";
        careerInfo.projectMember = projectMember;
        return careerInfo;
    }

    public static CareerInfo createExperienceCareerInfo(Period totalExperience, String lastCareer,
                                                        ProjectMember projectMember) {
        CareerInfo careerInfo = new CareerInfo();
        careerInfo.careerType = CareerType.EXPERIENCE;
        careerInfo.totalExperience = totalExperience;
        careerInfo.lastCareer = lastCareer;
        careerInfo.projectMember = projectMember;
        return careerInfo;
    }

    public boolean isSamePerson(ProjectMember projectMember) {
        return this.projectMember.equals(projectMember);
    }
}
