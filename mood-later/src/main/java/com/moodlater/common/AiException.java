package com.moodlater.common;

public class AiException extends RuntimeException {
    private Integer code;

    public AiException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
