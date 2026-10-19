package com.paintfinder.image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.paintfinder.global.dto.ResponseDto;
import com.paintfinder.global.error.GlobalException;
import com.paintfinder.image.error.ImageErrorCode;
import com.paintfinder.image.util.AwsS3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Service
@PropertySource(value = "application_key.properties")
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService{
    @Value("${S3.BUCKETNAME}")
    private String bucket;
    private final AmazonS3Client amazonS3Client;
    private final AwsS3Uploader awsS3Uploader;

    /**
     * 이미지 삭제 경우(아직 PaintController에 연결 X)
     * 1. paint 삭제시
     * 2. paint 이미지 갱신시
     *
     * @param imgUrl
     * @return HttpStatus.OK : 200 = 통과
     * HttpStatus.BAD_REQUEST : 400 = s3에 존재하지 않는 imgUrl을 전송
     */
    @Override
    public ResponseDto deleteImage(String directory, String imgUrl) throws UnsupportedEncodingException {
        if(imgUrl==null||imgUrl.isEmpty()){
            return ResponseDto.success();
        }
        String key = imgUrl.substring(imgUrl.indexOf(directory));

        // S3 인코딩 문제 적용
        String decodedKey = URLDecoder.decode(key, "UTF-8");

        try{
            // 버킷과 key 기준으로 객체 리스트를 조회해 직접 확인
            ListObjectsV2Request request = new ListObjectsV2Request()
                    .withBucketName(bucket)
                    .withPrefix(decodedKey);
            ListObjectsV2Result result = amazonS3Client.listObjectsV2(request);
            if (result.getObjectSummaries().isEmpty()) {
                log.error("객체가 존재하지 않음: {}", decodedKey);
                return ResponseDto.fail(ImageErrorCode.S3_IMAGE_NOT_EXIST);
            }

            // S3에서 객체 삭제
            amazonS3Client.deleteObject(bucket, decodedKey);
        } catch (AmazonS3Exception e) {
            log.error("S3 파일 삭제에 실패했습니다: {}", e.getMessage());
            return ResponseDto.fail(ImageErrorCode.S3_CONNECT_ERROR);
        }
        return ResponseDto.success();
    }

    @Override
    public ResponseDto<String> uploadImage(String directory, MultipartFile file) {
        if(file==null || file.isEmpty()) {
            throw new GlobalException(ImageErrorCode.MULTIPARTFILE_IMAGE_NOT_EXIST);
        }
        String url = awsS3Uploader.uploadImage(directory, file);

        return ResponseDto.success(url);
    }
}
