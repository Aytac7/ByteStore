package com.example.startapp.service.auth;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@Service
@Log4j2
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 s3client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    public String uploadFile(String fileName, MultipartFile file) throws IOException {
        File fileToUpload = convertMultipartFileToFile(file);
        s3client.putObject(new PutObjectRequest(bucketName, fileName, fileToUpload));
        return s3client.getUrl(bucketName, fileName).toString();
    }

    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        }
        return convertedFile;
    }
    public S3Object getFile(String keyName) {
        return s3client.getObject(bucketName, keyName);
    }

}