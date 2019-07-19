package com.bc.netty.client.action;

import com.bc.netty.client.rpc.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MainAction {
    /**
     * MainAction 日志输出
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MainAction.class);

    /**
     * 测试业务
     */
    @Resource
    private DemoService demoService;

    /**
     * 真正远程调用的方法
     *
     * @throws InterruptedException interruptedException
     */
    public void call() throws InterruptedException {
        // 用于模拟服务器正常启动后，人工调用远程服务代码
        Thread.sleep(10 * 1000);
        LOGGER.warn("[准备进行业务测试]");
        LOGGER.warn("[rpc测试] ");
        LOGGER.warn("[异常测试]");
        try {
            double division = demoService.division(3, 0);
            LOGGER.warn("[异常测试结果] {}", division);
        } catch (Exception e) {
            LOGGER.error("[服务器异常] {}", e.getMessage());
        }
    }
}
