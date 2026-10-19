package com.paintfinder.member.error;

import com.fasterxml.jackson.annotation.JsonValue;
import com.paintfinder.global.error.GeneralErrorCode;

import java.util.HashMap;
import java.util.Map;

public enum MemberErrorCode implements GeneralErrorCode {
    DUPLICATE_NICKNAME("2001", "이미 사용중인 닉네임입니다."),
    INVALID_NICKNAME("2002", "닉네임 형식이 올바르지 않습니다.");

    private final String errorCode;
    private final String errorMessage;

    MemberErrorCode(String errorCode, String errorMessage) {
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
