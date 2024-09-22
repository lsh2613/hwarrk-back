package com.hwarrk.common.apiPayload.code.statusEnums;

import com.hwarrk.common.apiPayload.code.BaseCode;
import com.hwarrk.common.apiPayload.code.ResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseCode {

    // common
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // 토큰
    BLACKLISTED_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN4011", "블랙리스트에 존재하는 Access Token입니다"),
    MISSING_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, "TOKEN4012", "Access Token이 존재하지 않습니다."),
    TOKEN_ID_MISMATCH(HttpStatus.FORBIDDEN, "TOKEN4003", "토큰의 사용자와 로그인된 사용자가 일치하지 않습니다."),

    // 회원 관련
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "사용자를 찾을 수 없습니다."),
    DUPLICATED_MEMBER_ID(HttpStatus.UNAUTHORIZED, "MEMBER4011", "이미 존재하는 사용자 ID입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "MEMBER4012", "사용자 비밀번호가 일치하지 않습니다"),
    SESSION_NOT_FOUND(HttpStatus.UNAUTHORIZED, "MEMBER4013", "존재하지 않는 유효한 세션입니다."),
    MEMBER_FORBIDDEN(HttpStatus.FORBIDDEN, "MEMBER4031", "사용자에게 권한이 없습니다."),

    // 프로젝트
    PROJECT_NOT_FOUND(HttpStatus.BAD_REQUEST, "PROJECT4001", "프로젝트를 찾을 수 없습니다."),
    PROJECT_LEADER_REQUIRED(HttpStatus.UNAUTHORIZED, "PROJECT4011", "프로젝트 리더만 프로젝트를 삭제할 수 있습니다."),

    // 프로젝트-조인
    PROJECT_JOIN_NOT_FOUND(HttpStatus.NOT_FOUND, "PROJECT_JOIN4041", "프로젝트 참가 신청을 찾을 수 없습니다."),
    PROJECT_JOIN_CONFLICT(HttpStatus.CONFLICT, "PROJECT_JOIN4091", "프로젝트 참가 신청이 이미 존재합니다."),

    // 알림
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "NOTIFICATION4041", "알림을 찾을 수 없습니다"),

    // S3 이미지 업로드
    FAIL_IMAGE_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "S5001", "S3에 이미지 업로드를 실패했습니다."),
    FAIL_IMAGE_DELETE(HttpStatus.INTERNAL_SERVER_ERROR, "S5002", "S3에서 이미지 삭제를 실패했습니다"),
    BAD_REQUEST_IMAGE(HttpStatus.BAD_REQUEST, "S400", "잘못된 이미지 데이터입니다."),
    UNAUTHORIZED_S3(HttpStatus.UNAUTHORIZED, "S401", "S3 접근 인증에 실패했습니다."),
    FORBIDDEN_S3(HttpStatus.FORBIDDEN, "S403", "S3 권한을 가지고 있지 않습니다."),
    UNAVAILABLE_S3(HttpStatus.SERVICE_UNAVAILABLE, "S503", "S3 서버가 일시적으로 데이터를 처리할 수 없습니다."),

    // 파일
    FAIL_FILE_CONVERT(HttpStatus.BAD_REQUEST, "CONVERT1000", "파일 변환에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ResponseDTO getDto() {
        return ResponseDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ResponseDTO getHttpStatusDto() {
        return ResponseDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
