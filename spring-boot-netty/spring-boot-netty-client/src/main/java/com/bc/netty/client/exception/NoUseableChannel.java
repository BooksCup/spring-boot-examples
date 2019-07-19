package com.bc.netty.client.exception;

public class NoUseableChannel extends RuntimeException {

    public NoUseableChannel() {
        super();
    }

    public NoUseableChannel(String message) {
        super(message);
    }
}
