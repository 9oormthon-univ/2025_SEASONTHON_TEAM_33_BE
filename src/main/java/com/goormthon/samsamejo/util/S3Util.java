package com.goormthon.samsamejo.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
@RequiredArgsConstructor
public class S3Util {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket.name}")
    private String bucket;

    @Async
    public void uploadFile(byte[] file, String savePath, String fileName, String contentType) {
        RequestBody requestBody = RequestBody.fromBytes(file);
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(savePath + fileName)
                .contentType(contentType)
                .build();

        s3Client.putObject(putObjectRequest, requestBody);
    }
}
