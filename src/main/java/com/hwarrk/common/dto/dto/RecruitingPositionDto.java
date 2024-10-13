package com.hwarrk.common.dto.dto;

import com.hwarrk.entity.RecruitingPosition;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class RecruitingPositionDto {

    private String positionType;
    private int number;

    public static RecruitingPositionDto mapEntityToDto(RecruitingPosition recruitingPosition) {
        RecruitingPositionDto recruitingPositionDto = new RecruitingPositionDto();
        recruitingPositionDto.positionType = recruitingPosition.getPosition().name();
        recruitingPositionDto.number = recruitingPositionDto.getNumber();
        return recruitingPositionDto;
    }
}
