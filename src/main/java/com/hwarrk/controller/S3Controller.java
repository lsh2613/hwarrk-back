package com.hwarrk.controller;

import com.hwarrk.common.apiPayload.CustomApiResponse;
import com.hwarrk.service.S3Uploader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "s3 업로드")
@RequiredArgsConstructor
@RestController
@RequestMapping("/s3")
public class S3Controller {
    private final S3Uploader s3Uploader;

    @Operation(summary = "이미지 업로드", description = "이미지 업로드는 다른 api에 같이 포함되어 있음. 이 API는 테스트 용으로 사용")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomApiResponse<String> uploadImg(@RequestPart(value = "image") MultipartFile multipartFile) {
        String img = s3Uploader.uploadImg(multipartFile);
        return CustomApiResponse.onSuccess(img);
    }
}
