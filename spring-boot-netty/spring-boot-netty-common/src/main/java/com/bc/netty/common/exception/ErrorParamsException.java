package com.bc.netty.common.exception;

/**
 * 参数错误异常
 *
 * @author zhou
 */
public class ErrorParamsException extends RuntimeException {
    private static final long serialVersionUID = -1;

    public ErrorParamsException() {
        super();
    }

    public ErrorParamsException(String message) {
        super(message);
    }
}
