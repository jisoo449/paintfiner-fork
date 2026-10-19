package com.paintfinder.global.handler;

import com.paintfinder.global.dto.ResponseDto;
import com.paintfinder.global.error.GlobalException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ResponseDto> handleGlobalException(GlobalException e){
        ResponseDto responseDto = ResponseDto.fail(e.getGeneralErrorCode());

        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }
}
