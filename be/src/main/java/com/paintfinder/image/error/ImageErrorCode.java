package com.paintfinder.image.error;

import com.fasterxml.jackson.annotation.JsonValue;
import com.paintfinder.global.error.GeneralErrorCode;

import java.util.HashMap;
import java.util.Map;

public enum ImageErrorCode implements GeneralErrorCode {
    S3_CONNECT_ERROR("6001", "Connect to S3 failed"),
    S3_IMAGE_NOT_EXIST("6002", "Image is not Exist"),
    MULTIPARTFILE_IMAGE_NOT_EXIST("6003", "multipartFile is empty");
    private final String errorCode;
    private final String errorMessage;

    ImageErrorCode(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    // throw 할 때 code값이 정확히 전달되지 않음. json 값으로 파싱해서 전달하니 해결됨.
    @JsonValue
    public Map<String, String> toJson() {
        Map<String, String> map = new HashMap<>();
        map.put("code", errorCode);
        map.put("message", errorMessage);
        return map;
    }
}
