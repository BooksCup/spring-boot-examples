package com.bc.netty.server.service.impl;

import com.bc.netty.server.exception.ErrorParamsException;
import com.bc.netty.server.service.DemoService;

public class DemoServiceImpl implements DemoService {
    @Override
    public double division(int numberA, int numberB) throws ErrorParamsException {
        if (numberB == 0) {
            throw new ErrorParamsException("除数不能为0");
        }
        return numberA / numberB;
    }
}
