package com.a503.onjeong.global.util;

import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Util {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public String uploadFile(MultipartFile file) throws IOException {

        String S3FileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        amazonS3.putObject(bucket, S3FileName, file.getInputStream(), null);

        return S3FileName;
    }

    public void deleteFile(String S3FileName) {

        amazonS3.deleteObject(bucket, S3FileName);
    }

    public String getFile(String S3FileName) {

        try {
            URL url = amazonS3.getUrl(bucket, S3FileName);
            return (url != null) ? url.getPath() : null;
        } catch (Exception e) {
            return null;
        }
    }
}
