package com.paintfinder.image.service;

import com.paintfinder.global.dto.ResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;


public interface ImageService {

    /**
     * S3 내 이미지 삭제.
     * @param directory : 이미지가 담겨 있는 디렉토리명 필요.
     * @param imgUrl
     * @return
     */
    ResponseDto deleteImage(String directory, String imgUrl) throws UnsupportedEncodingException;

    /**
     * 이미지 S3에 업로드
     * @param directory : 마이팔레트면 palette, 페인트면 paint 폴더명으로 저장하기.
     * @param file
     * @return String url
     */
    ResponseDto<String> uploadImage(String directory, MultipartFile file);
}
