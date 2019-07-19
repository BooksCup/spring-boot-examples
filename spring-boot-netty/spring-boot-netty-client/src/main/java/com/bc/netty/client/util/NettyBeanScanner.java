package com.bc.netty.client.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.util.List;

public class NettyBeanScanner implements BeanFactoryPostProcessor {
    /**
     * 装载bean的工厂
     */
    private DefaultListableBeanFactory beanFactory;
    /**
     * 包名
     */
    private String basePackage;
    /**
     * bean名（引用名）
     */
    private String clientName;

    /**
     * 有参构造
     *
     * @param basePackage 待扫描包名
     * @param clientName  netty客户端beanName
     */
    public NettyBeanScanner(String basePackage, String clientName) {
        this.basePackage = basePackage;
        this.clientName = clientName;
    }


    /**
     * 注册远程接口Bean到Spring的bean工厂
     *
     * @param beanFactory 装载bean的工厂
     * @throws BeansException bean异常
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
        // 从目录中加载远程服务的接口
        List<String> resolverClass = PackageClassUtils.resolver(basePackage);
        for (String clazz : resolverClass) {
            // 获取接口名
            String simpleName;
            // 接口全限定名
            if (clazz.lastIndexOf('.') != -1) {
                simpleName = clazz.substring(clazz.lastIndexOf('.') + 1);
            } else {
                simpleName = clazz;
            }
            // 使用建造者模式创建一个Bean定义
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(RPCProxyFactoryBean.class);
            // 对应 RPCProxyFactoryBean 类的 interfaceClass 属性
            beanDefinitionBuilder.addPropertyValue("interfaceClass", clazz);
            // 对应 RPCProxyFactoryBean 的nettyClient 属性  --  已删
//            beanDefinitionBuilder.addPropertyReference("nettyClient", clientName);
            // 注册对bean的定义
            this.beanFactory.registerBeanDefinition(simpleName, beanDefinitionBuilder.getRawBeanDefinition());
        }
    }
}
