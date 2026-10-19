package com.paintfinder.global.dto;

import com.paintfinder.global.error.ErrorCode;
import com.paintfinder.global.error.GeneralErrorCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ResponseDto<T> {
    private GeneralErrorCode code = ErrorCode.UNKNOWN_ERROR;
    private T data = null;
    private boolean isSuccess = false;

    @Builder
    public ResponseDto(boolean isSuccess, T data, GeneralErrorCode code){
        this.isSuccess = isSuccess;
        this.data = data;
        this.code = code;
    }

    public static <T> ResponseDto<T> success(T data){
        return new ResponseDto<>(true, data, null);
    }
    public static <T> ResponseDto<T> success(){
        return new ResponseDto<>(true, null, null);
    }
    public static <T> ResponseDto<T> fail(GeneralErrorCode code){
        return new ResponseDto<>(false, null, code);
    }
    public static <T> ResponseDto<T> fail(T data, GeneralErrorCode code){
        return new ResponseDto<>(false, data, code);
    }
}
