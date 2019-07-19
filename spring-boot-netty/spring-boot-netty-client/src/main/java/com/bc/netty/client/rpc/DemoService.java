package com.bc.netty.client.rpc;

import com.bc.netty.client.exception.ErrorParamsException;

public interface DemoService {

    /**
     * 除法运算
     *
     * @param numberA 第一个数
     * @param numberB 第二个数
     * @return 结果
     */
    double division(int numberA, int numberB) throws ErrorParamsException;
}
