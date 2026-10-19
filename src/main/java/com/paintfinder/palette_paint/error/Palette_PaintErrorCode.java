package com.paintfinder.palette_paint.error;

import com.fasterxml.jackson.annotation.JsonValue;
import com.paintfinder.global.error.GeneralErrorCode;

import java.util.HashMap;
import java.util.Map;

public enum Palette_PaintErrorCode implements GeneralErrorCode {
    EXCEEDED_PAINT_MAXIMUM_LIMIT("7002", "최대 페인트 개수를 초과하였습니다.");
    private final String errorCode;
    private final String errorMessage;

    Palette_PaintErrorCode(String errorCode, String errorMessage) {
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
