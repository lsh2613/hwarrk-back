package com.hwarrk.common.apiPayload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.hwarrk.common.apiPayload.code.statusEnums.SuccessStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonPropertyOrder({"isSuccess", "code", "message", "data"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomApiResponse<T> {

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;

    private final String code;

    private final String message;

    private T data;

    // 성공 응답
    public static <T> CustomApiResponse<T> onSuccess(){
        return new CustomApiResponse<>(true, SuccessStatus._OK.getCode(), SuccessStatus._OK.getMessage(), null);
    }

    public static <T> CustomApiResponse<T> onSuccess(T result){
        return new CustomApiResponse<>(true, SuccessStatus._OK.getCode(), SuccessStatus._OK.getMessage(), result);
    }

    // 실패 응답
    public static <T> CustomApiResponse<T> onFailure(String code, String message, T data){
        return new CustomApiResponse<>(false, code, message, data);
    }
}
