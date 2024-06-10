package com.webflux.demo.common.config;

public class CustomException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public CustomException(String type){
        System.out.println("CustomException: " + type + "의 예외가 발생했습니다.");
    }
}
