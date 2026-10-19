package com.paintfinder.image.controller;

import com.paintfinder.global.dto.ResponseDto;
import com.paintfinder.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {
    private final ImageService imageService;

    @PostMapping(value = "/test", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadTestImage(
            @RequestPart("file")MultipartFile multipartFile,
            @RequestPart("directory") String directoryName
            ){
        ResponseDto responseDto = imageService.uploadImage(directoryName, multipartFile);

        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity deleteTest(
            @RequestParam String imgUrl,
            @RequestParam String directoryName
            ) throws UnsupportedEncodingException {
        ResponseDto responseDto = imageService.deleteImage(directoryName, imgUrl);
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }
}
