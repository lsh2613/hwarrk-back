package com.hwarrk.common.dto.res;

import com.hwarrk.entity.Career;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CareerRes {
    private long careerId;
    private String company;
    private String domain;
    private String job;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;

    public static CareerRes createRes(Career career) {
        CareerRes careerRes = new CareerRes();
        careerRes.careerId = career.getId();
        careerRes.company = career.getCompany();
        careerRes.domain = career.getDomain();
        careerRes.job = career.getJob();
        careerRes.startDate = career.getStartDate();
        careerRes.endDate = career.getEndDate();
        careerRes.description = career.getDescription();
        return careerRes;
    }
}
