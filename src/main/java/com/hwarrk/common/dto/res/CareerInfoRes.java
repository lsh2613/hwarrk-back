package com.hwarrk.common.dto.res;

import com.hwarrk.entity.CareerInfo;
import com.hwarrk.entity.CareerType;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CareerInfoRes {
    private CareerType careerType;
    private int totalExperience;
    private String lastCareer;

    public static CareerInfoRes mapEntityToRes(CareerInfo careerInfo) {
        return CareerInfoRes.builder()
                .careerType(careerInfo.careerType())
                .totalExperience(careerInfo.totalExperienceYears())
                .lastCareer(careerInfo.lastCareer())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CareerInfoRes that = (CareerInfoRes) o;
        return totalExperience == that.totalExperience && Objects.equals(lastCareer, that.lastCareer)
                && careerType == that.careerType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(careerType, totalExperience, lastCareer);
    }

    @Override
    public String toString() {
        return "CareerInfoRes[" +
                "careerType=" + careerType + ", " +
                "totalExperience=" + totalExperience + ", " +
                "lastCareer=" + lastCareer + ']';
    }

}
