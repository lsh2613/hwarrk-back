package com.hwarrk.common.dto.res;

import com.hwarrk.entity.CareerInfo;
import com.hwarrk.entity.CareerType;
import java.time.Period;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CareerInfoRes {

    private CareerType careerType;
    private Period totalExperience;
    private String lastCareer;

    public static CareerInfoRes mapEntityToRes(CareerInfo careerInfo) {
        CareerInfoRes careerInfoRes = new CareerInfoRes();
        careerInfoRes.careerType = careerInfo.getCareerType();
        careerInfoRes.totalExperience = careerInfo.getTotalExperience();
        careerInfoRes.lastCareer = careerInfo.getLastCareer();
        return careerInfoRes;
    }
}
