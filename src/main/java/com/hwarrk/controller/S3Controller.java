package com.hwarrk.controller;

import com.hwarrk.common.apiPayload.CustomApiResponse;
import com.hwarrk.service.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/s3")
public class S3Controller {
    private final S3Uploader s3Uploader;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomApiResponse uploadImg(@RequestPart(value = "image") MultipartFile multipartFile) {
        String img = s3Uploader.uploadImg(multipartFile);
        return CustomApiResponse.onSuccess(img);
    }
}
