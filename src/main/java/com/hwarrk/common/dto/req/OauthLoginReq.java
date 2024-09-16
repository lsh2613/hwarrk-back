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
    @Schema(defaultValue = "KAKAO")
    private OauthProvider oauthProvider;
    @NotBlank
    private String code;
}
