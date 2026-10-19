package com.paintfinder.global.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paintfinder.global.error.ErrorCode;
import com.paintfinder.global.error.GlobalException;

import java.io.IOException;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * json string 값을 특정 dto로 변환해 주는 utility class
     * @param jsonString
     * @param dtoClass
     * @return 해당 dto를 채워 반환해줌
     * @param <T>
     */
    public static <T> T parseJson(String jsonString, Class<T> dtoClass){
        try{
            return objectMapper.readValue(jsonString, dtoClass);
        } catch (IOException e){
            throw new GlobalException(ErrorCode.INVALID_JSON);
        }
    }
}
