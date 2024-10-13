package com.hwarrk.common.dto.res;

import com.hwarrk.entity.CareerInfo;
import com.hwarrk.entity.CareerType;
import lombok.Builder;

@Builder
public record CareerInfoRes(
        CareerType careerType,
        int totalExperience,
        String lastCareer
) {
    public static CareerInfoRes mapEntityToRes(CareerInfo careerInfo) {
        return CareerInfoRes.builder()
                .careerType(careerInfo.careerType())
                .totalExperience(careerInfo.totalExperienceYears())
                .lastCareer(careerInfo.lastCareer())
                .build();
    }
}
