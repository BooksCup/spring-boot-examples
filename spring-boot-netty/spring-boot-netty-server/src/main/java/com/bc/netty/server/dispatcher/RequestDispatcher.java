package com.bc.netty.server.dispatcher;

import com.bc.netty.server.adapter.ServerChannelHandlerAdapter;
import com.bc.netty.server.entity.MethodInvokeMeta;
import com.bc.netty.server.entity.NullWritable;
import com.bc.netty.server.exception.ErrorParamsException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author zhou
 */
@Component
public class RequestDispatcher implements ApplicationContextAware {

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(ServerChannelHandlerAdapter.class);

    /**
     * Spring上下文
     */
    private ApplicationContext applicationContext;

    public void dispatcher(final ChannelHandlerContext channelHandlerContext, final MethodInvokeMeta invokeMeta){
        ChannelFuture channelFuture = null;
        // 指向的接口类
        Class<?> interfaceClass = invokeMeta.getInterfaceClass();
        // 所调用的方法名
        String name = invokeMeta.getMethodName();
        // 方法的参数列表
        Object[] args = invokeMeta.getArgs();
        // 方法的参数列表按顺序对应的类型
        Class<?>[] parameterTypes = invokeMeta.getParameterTypes();
        // 通过Spring获取实际对象
        Object targetObject = this.applicationContext.getBean(interfaceClass);
        // 声明调用的方法对象
        Method method;
        try {
            // 获取调用的方法对象
            method = targetObject.getClass().getMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            // 尚未执行方法调用出现异常
            logger.error("[获取远程方法异常] {}", e.getMessage());
            // 响应给客户端
            channelFuture = channelHandlerContext.writeAndFlush(e);
            return;
        } finally {
            if (channelFuture != null) {
                channelFuture.addListener(ChannelFutureListener.CLOSE);
            }
        }
        try {
            // 执行方法
            Object result = method.invoke(targetObject, args);
            if (result == null) {
                // 方法没有返回值，返回NullWritable对象
                logger.info("{} -> [方法没有返回值，返回NullWritable对象]", this.getClass().getName());
                channelFuture = channelHandlerContext.writeAndFlush(NullWritable.nullWritable());
            } else {
                // 将方法执行结果写入到Channel
                logger.info("{} -> [返回结果] {}", this.getClass().getName(), result);
                channelFuture = channelHandlerContext.writeAndFlush(result);
            }
            /*
            虽然可以通过ChannelFuture的get()方法获取异步操作的结果,但完成时间是无法预测的,若不设置超时时间则有可能导致线程长时间被阻塞;
            若是不能精确的设置超时时间则可能导致I/O操作中断.
            因此,Netty建议通过GenericFutureListener接口执行异步操作结束后的回调.
             */
            /// ChannelFutureListener.CLOSE = new ChannelFutureListener() {
            ///    @Override
            ///    public void operationComplete(ChannelFuture future) {
            ///        future.channel().close();
            ///    }
            /// };
        } catch (Exception e) {
            Throwable targetException = ((InvocationTargetException) e).getTargetException();
            logger.error("{} -> [方法执行异常] {}", this.getClass().getName());
            if (targetException instanceof ErrorParamsException) {
                logger.error("{} -> [异常信息] {}", this.getClass().getName(), targetException.getMessage());
            }
            channelFuture = channelHandlerContext.writeAndFlush(targetException);
        } finally {
            if (channelFuture != null) {
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
