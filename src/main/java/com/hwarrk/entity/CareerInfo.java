package com.hwarrk.entity;

import lombok.Builder;

import java.time.Period;

@Builder
public record CareerInfo(
        CareerType careerType,
        int totalExperienceYears,
        String lastCareer
) {

    public static CareerInfo createEntryCareerInfo() {
        return CareerInfo.builder()
                .careerType(CareerType.ENTRY_LEVEL)
                .totalExperienceYears(Period.ZERO.getYears())
                .lastCareer("없음")
                .build();
    }

    public static CareerInfo createExperienceCareerInfo(int totalYears, String lastCareer) {
        return CareerInfo.builder()
                .careerType(CareerType.EXPERIENCE)
                .totalExperienceYears(totalYears)
                .lastCareer(lastCareer)
                .build();
    }

}
