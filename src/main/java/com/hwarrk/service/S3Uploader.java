package com.hwarrk.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.hwarrk.common.apiPayload.code.statusEnums.ErrorStatus;
import com.hwarrk.common.exception.GeneralHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Uploader {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public void deleteImg(String fileName) {
        try {
            amazonS3.deleteObject(bucket, fileName);
            log.debug("S3에서 파일이 삭제되었습니다. 파일명: " + fileName);
        } catch (AmazonServiceException e) {
            switch (e.getStatusCode()) {
                case 400:
                    throw new GeneralHandler(ErrorStatus.BAD_REQUEST_IMAGE);
                case 401:
                    throw new GeneralHandler(ErrorStatus.UNAUTHORIZED_S3);
                case 403:
                    throw new GeneralHandler(ErrorStatus.FORBIDDEN_S3);
                case 500:
                    throw new GeneralHandler(ErrorStatus.FAIL_IMAGE_DELETE);
                case 503:
                    throw new GeneralHandler(ErrorStatus.UNAVAILABLE_S3);
            }
        }
    }

    /* MultipartFile을 전달받아 File로 전환 후 S3에 업로드 */
    public Map<String, String> uploadImg(MultipartFile file) {
        Map<String, String> map = new HashMap<>();
        try {
            Optional<File> uploadFile = convert(file);
            String imgUrl = upload(uploadFile.get());
            map.put("imgUrl", imgUrl);
        } catch (AmazonServiceException e){
            switch (e.getStatusCode()) {
                case 400:
                    throw new GeneralHandler(ErrorStatus.BAD_REQUEST_IMAGE);
                case 401:
                    throw new GeneralHandler(ErrorStatus.UNAUTHORIZED_S3);
                case 403:
                    throw new GeneralHandler(ErrorStatus.FORBIDDEN_S3);
                case 500:
                    throw new GeneralHandler(ErrorStatus.FAIL_IMAGE_UPLOAD);
                case 503:
                    throw new GeneralHandler(ErrorStatus.UNAVAILABLE_S3);
            }
        }
        return map;
    }

    private String upload(File uploadFile) {
        String fileName = UUID.randomUUID().toString();
        String uploadImgUrl = putS3(uploadFile, fileName);

        removeNewFile(uploadFile);

        return uploadImgUrl;
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File file) {
        if (file.delete()) {
            log.debug("파일이 삭제되었습니다.");
        } else {
            log.debug("파일 삭제에 실패했습니다.");
        }
    }

    private Optional<File> convert(MultipartFile file) {
        File convertFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            if (convertFile.createNewFile()) {
                FileOutputStream fileOutputStream = new FileOutputStream(convertFile);
                fileOutputStream.write(file.getBytes());
                return Optional.of(convertFile);
            }
        }catch (IOException e){
            throw new GeneralHandler(ErrorStatus.FAIL_FILE_CONVERT);
        }
        return Optional.empty();
    }
}
