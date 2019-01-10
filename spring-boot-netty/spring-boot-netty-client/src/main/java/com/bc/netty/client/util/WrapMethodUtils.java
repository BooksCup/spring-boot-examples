package com.bc.netty.client.util;

import com.bc.netty.common.entity.MethodInvokeMeta;

import java.lang.reflect.Method;

public class WrapMethodUtils {
    /**
     * 封装 method 的元数据信息
     *
     * @param interfaceClass 接口类
     * @param method         方法
     * @param args           参数列表
     * @return 封装的对象
     */
    public static MethodInvokeMeta readMethod(Class interfaceClass, Method method, Object[] args) {
        MethodInvokeMeta methodInvokeMeta = new MethodInvokeMeta();
        methodInvokeMeta.setInterfaceClass(interfaceClass);
        methodInvokeMeta.setArgs(args);
        methodInvokeMeta.setMethodName(method.getName());
        methodInvokeMeta.setReturnType(method.getReturnType());
        Class<?>[] parameterTypes = method.getParameterTypes();
        methodInvokeMeta.setParameterTypes(parameterTypes);
        return methodInvokeMeta;
    }
}
