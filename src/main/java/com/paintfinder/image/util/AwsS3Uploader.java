package com.paintfinder.image.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.paintfinder.global.error.GlobalException;
import com.paintfinder.image.error.ImageErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@PropertySource(value = "application_key.properties")
@RequiredArgsConstructor
@Slf4j
@Component
public class AwsS3Uploader {

    @Value("${S3.BUCKETNAME}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;

    /**
     *
     * @param folderName
     * @param file
     * @return String 업로드 한 경로파일명
     */
    public String uploadImage(String folderName, MultipartFile file) {

        // 메타데이터로 파일의 콘텐츠 유형과 크기를 설정해 파일의 무결성을 보장한다.
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());

        // S3에 저장할 폴더, 파일명을 UUID로 중복되지 않게 설정 (8자리로 축소)
        String fileName = folderName + "/" + UUID.randomUUID().toString().substring(0, 8) + getFileExtension(file.getOriginalFilename());

        try(InputStream inputStream = file.getInputStream()){
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            return amazonS3Client.getUrl(bucket, fileName).toString();
        } catch (IOException e){
            log.error("S3 파일 업로드에 실패했습니다. {}", e.getMessage());
            throw new GlobalException(ImageErrorCode.S3_CONNECT_ERROR);
        }
    }

    // 파일 확장자(.jpg, .png, .pdf 등)를 올바르게 가져오는 함수
    private String getFileExtension(String fileName) {
        return fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".")) : "";
    }
}
