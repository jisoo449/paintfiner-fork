package com.paintfinder.global.error;

public class GlobalException extends RuntimeException{
    private final GeneralErrorCode generalErrorCode;

    public GlobalException(GeneralErrorCode generalErrorCode) {
        // 상위 클래스인 RuntimeException의 생성자에 에러 메시지를 전달. 예외 객체 생성시 메시지 설정
        super(generalErrorCode.getErrorMessage());

        // CustomException 클래스의 필드인 errorCode 초기화. 예외 객체 생성시 해당 에러 코드 설정
        this.generalErrorCode = generalErrorCode;
    }

    public GeneralErrorCode getGeneralErrorCode(){
        return generalErrorCode;
    }

    // TODO : 정의되지 않은 에러 코드의 경우를 처리하는 생성자 필요(예외처리의 예외처리?)
}
