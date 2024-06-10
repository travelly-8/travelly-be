package com.demo.travellybe.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.demo.travellybe.exception.CustomException;
import com.demo.travellybe.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxSizeString;


    public List<String> uploadFiles(List<MultipartFile> files, String path) {
        List<String> fileUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            fileUrls.add(uploadFile(file, path));
        }

        return fileUrls;
    }

    public String uploadFile(MultipartFile file, String path) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        String filePath = "images/" + path + "/" + file.getOriginalFilename();

        try {
            amazonS3.putObject(bucket, filePath, file.getInputStream(), metadata);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_UPLOAD_FAILED);
        }

        return amazonS3.getUrl(bucket, filePath).toString();
    }
}
