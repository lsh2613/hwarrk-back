package com.hwarrk.common.apiPayload.code.statusEnums;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class ErrorMessage {
    public static final String TOKEN_NOT_FOUND = "해당 토큰이 존재 하지 않습니다.";
    public static final String TOKEN_VERIFICATION = "해당 토큰이 유효하지 않습니다.";
}
