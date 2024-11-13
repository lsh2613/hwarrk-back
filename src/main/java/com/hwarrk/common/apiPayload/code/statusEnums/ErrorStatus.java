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
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // 토큰
    BLACKLISTED_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN4011", "블랙리스트에 존재하는 Access Token입니다"),
    MISSING_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, "TOKEN4012", "Access Token이 존재하지 않습니다."),

    // 회원 관련
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER4041", "사용자를 찾을 수 없습니다."),
    DUPLICATED_MEMBER_ID(HttpStatus.UNAUTHORIZED, "MEMBER4011", "이미 존재하는 사용자 ID입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "MEMBER4012", "사용자 비밀번호가 일치하지 않습니다"),
    SESSION_NOT_FOUND(HttpStatus.UNAUTHORIZED, "MEMBER4013", "존재하지 않는 유효한 세션입니다."),
    MEMBER_FORBIDDEN(HttpStatus.FORBIDDEN, "MEMBER4031", "사용자에게 권한이 없습니다."),
    GUEST_ROLE_FORBIDDEN(HttpStatus.FORBIDDEN, "MEMBER4032", "게스트 회원은 이용할 수 없는 기능입니다."),

    // 프로필 관련
    PROFILE_NOT_VISIBLE(HttpStatus.FORBIDDEN, "PROFILE4031", "조회하려는 사용자의 프로필이 비공개 상태입니다."),

    // 회원 찜
    MEMBER_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_LIKE4041", "찜을 찾을 수 없습니다."),
    MEMBER_LIKE_CONFLICT(HttpStatus.CONFLICT, "MEMBER_LIKE4091", "찜이 이미 존재합니다."),

    // 사용자 리뷰
    PROJECT_MEMBER_ONLY_CAN_REVIEW(HttpStatus.BAD_REQUEST, "MEMBER_REVIEW4001", "같은 프로젝트의 팀원끼리만 리뷰를 작성할 수 있습니다."),
    MEMBER_REVIEW_CONFLICT(HttpStatus.CONFLICT, "MEMBER_REVIEW4091", "리뷰가 이미 존재합니다."),
    SELF_REVIEW_FORBIDDEN(HttpStatus.FORBIDDEN, "MEMBER_REVIEW4031", "자신에 대한 리뷰는 작성할 수 없습니다."),


    // 프로젝트
    PROJECT_NOT_FOUND(HttpStatus.BAD_REQUEST, "PROJECT4001", "프로젝트를 찾을 수 없습니다."),
    PROJECT_LEADER_REQUIRED(HttpStatus.UNAUTHORIZED, "PROJECT4011", "프로젝트 리더 권한이 필요합니다."),
    PROJECT_INCOMPLETE(HttpStatus.BAD_REQUEST, "PROJECT4002", "완료된 프로젝트가 아닙니다."),

    // 프로젝트 찜
    PROJECT_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "PROJECT_LIKE4041", "프로젝트 찜을 찾을 수 없습니다."),
    PROJECT_LIKE_CONFLICT(HttpStatus.CONFLICT, "PROJECT_LIKE4091", "프로젝트 찜이 이미 존재합니다."),

    // 프로젝트-조인
    PROJECT_JOIN_NOT_FOUND(HttpStatus.NOT_FOUND, "PROJECT_JOIN4041", "프로젝트 참가 신청을 찾을 수 없습니다."),
    PROJECT_JOIN_CONFLICT(HttpStatus.CONFLICT, "PROJECT_JOIN4091", "프로젝트 참가 신청이 이미 존재합니다."),

    // 프로젝트-팀원
    PROJECT_LEADER_CANNOT_BE_REMOVED(HttpStatus.BAD_REQUEST, "PROJECT_MEMBER4001", "프로젝트 리더는 추방될 수 없습니다."),
    PROJECT_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "PROJECT_MEMBER4041", "프로젝트 팀원을 찾을 수 없습니다."),
    PROJECT_MEMBER_CONFLICT(HttpStatus.CONFLICT, "PROJECT_MEMBER4091", "프로젝트 팀원이 이미 존재합니다."),


    // 구인글
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST_4041", "구인글을 찾을 수 없습니다"),

    // 구인글 찜
    POST_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "POST_LIKE4041", "구인글 찜을 찾을 수 없습니다."),
    POST_LIKE_CONFLICT(HttpStatus.CONFLICT, "POST_LIKE4091", "구인글 찜이 이미 존재합니다."),

    // 알림
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "NOTIFICATION4041", "알림을 찾을 수 없습니다"),
    INVALID_BINDING_TYPE(HttpStatus.BAD_REQUEST, "NOTIFICATION4001", "유효하지 않은 바인딩 타입입니다."),

    // S3 이미지 업로드
    FAIL_IMAGE_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "S5001", "S3에 이미지 업로드를 실패했습니다."),
    FAIL_IMAGE_DELETE(HttpStatus.INTERNAL_SERVER_ERROR, "S5002", "S3에서 이미지 삭제를 실패했습니다"),
    BAD_REQUEST_IMAGE(HttpStatus.BAD_REQUEST, "S400", "잘못된 이미지 데이터입니다."),
    UNAUTHORIZED_S3(HttpStatus.UNAUTHORIZED, "S401", "S3 접근 인증에 실패했습니다."),
    FORBIDDEN_S3(HttpStatus.FORBIDDEN, "S403", "S3 권한을 가지고 있지 않습니다."),
    UNAVAILABLE_S3(HttpStatus.SERVICE_UNAVAILABLE, "S503", "S3 서버가 일시적으로 데이터를 처리할 수 없습니다."),

    // 파일
    FAIL_FILE_CONVERT(HttpStatus.BAD_REQUEST, "CONVERT1000", "파일 변환에 실패했습니다."),

    // 커리어 요약
    LAST_CAREER_NOT_FOUND(HttpStatus.NOT_FOUND, "CAREER4041", "최신 회사 정보가 존재하지 않습니다."),

    // 채팅방
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAT_ROOM4041", "채팅방을 찾을 수 없습니다"),

    // 채팅방 참가자
    CHAT_ROOM_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAT_ROOM_MEMBER4041", "채팅방 참가자를 찾을 수 없습니다"),
    NOT_CHAT_ROOM_MEMBER(HttpStatus.FORBIDDEN, "CHAT_ROOM_MEMBER4031", "채팅방에 참여 중인 사용자가 아닙니다."),

    // Stomp header
    MISSING_CHAT_ROOM_ID(HttpStatus.BAD_REQUEST, "CHATROOM4002", "Stomp header에 chat-room-id 존재하지 않습니다"),
    MISSING_MEMBER_ID_IN_SESSION(HttpStatus.BAD_REQUEST, "CHATROOM4002", "Stomp header session에 memberId가 존재하지 않습니다");


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
