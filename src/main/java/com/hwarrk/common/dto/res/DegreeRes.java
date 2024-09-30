package com.hwarrk.common.dto.res;

import com.hwarrk.entity.Degree;
import lombok.Builder;

@Builder
public record DegreeRes(
        String degreeType,
        String universityType,
        String school,
        String major,
        String graduationType,
        String entranceDate,
        String graduationDate
) {
    public static DegreeRes mapEntityToRes(Degree degreeEntity) {
        return DegreeRes.builder()
                .degreeType(degreeEntity.getDegreeType())
                .universityType(degreeEntity.getUniversityType())
                .school(degreeEntity.getSchool())
                .major(degreeEntity.getMajor())
                .graduationType(degreeEntity.getGraduationType())
                .entranceDate(degreeEntity.getEntranceDate())
                .graduationDate(degreeEntity.getGraduationDate())
                .build();
    }
}
