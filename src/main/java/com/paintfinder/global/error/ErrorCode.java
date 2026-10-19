package com.paintfinder.global.error;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum ErrorCode implements GeneralErrorCode{

    //************ 일반 에러 **********************
    DATA_INTEGRITY_VIOLATION("1001", "제약 조건을 위배하였거나, 중복된 키를 사용하였습니다"),
    EMPTY_RESULT("1002", "조회 결과가 없습니다"),
    INVALID_JSON("1003", "json 형식이 잘못되었습니다"),
    UNKNOWN_ERROR("1099", "알 수 없는 이유 발생"),
    // 동시성 문제, 네트워크 문제 등

    //************ 토큰 에러 **********************
    INVALID_TOKEN("1101", "Invalid JWT Token"),

    //************* 구조 상 에러 *****************
    PERMISSION_DENIED("1201", "해당 데이터에 권한이 없습니다.");


    private final String errorCode;
    private final String errorMessage;

    ErrorCode(String errorCode, String errorMessage) {
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
