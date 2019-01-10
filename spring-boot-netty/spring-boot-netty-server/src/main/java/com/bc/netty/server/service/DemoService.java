package com.bc.netty.server.service;


import com.bc.netty.common.exception.ErrorParamsException;

public interface DemoService {
    double division(int numberA, int numberB) throws ErrorParamsException;
}
