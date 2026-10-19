package com.paintfinder.global.dto;

import com.paintfinder.global.error.GeneralErrorCode;
import lombok.Getter;

@Getter
public class ErrorResponseDto {
    private String errorCode;
    private String errorMessage;

    ErrorResponseDto(GeneralErrorCode generalErrorCode){
        this.errorCode = generalErrorCode.getErrorCode();
        this.errorMessage = generalErrorCode.getErrorMessage();
    }
}
