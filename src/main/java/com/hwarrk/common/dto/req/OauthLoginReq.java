package com.hwarrk.common.dto.req;

import com.hwarrk.common.constant.OauthProvider;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OauthLoginReq {
    @NotNull
    @Schema(description = "소셜 로그인 제공자", examples = {"KAKAO", "GOOGLE", "APPLE"})
    private OauthProvider oauthProvider;

    @NotBlank
    @Schema(description = "OAuth Provider Server로 부터 받은 인증 코드")
    private String code;
}
