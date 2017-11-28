package com.ckcclc.springboot.exception;

import com.ckcclc.springboot.common.ErrorCode;

/**
 * Created by ckcclc on 26/10/2017.
 */
public class BusinessException extends Exception {

    private ErrorCode errorCode;

    public BusinessException() {
        super();
    }

    BusinessException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
