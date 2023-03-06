package org.cloud.easy.feign;


import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

public class RpcClientFactoryBean implements FactoryBean {

    private Class<?> type;

    private BeanFactory beanFactory;

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public Object getObject() throws Exception {

        MethodAnnotationHandle handle = new MethodAnnotationHandle(type, beanFactory, url);
        handle.handleMethod();


        // 创建代理对象
        Rpc.RpcInvocationHandler rpcInvocationHandler = new Rpc.RpcInvocationHandler(null);
        Class<?>[] interfaces = {type};
        Object proxyInstance = Proxy.newProxyInstance(RpcClientFactoryBean.class.getClassLoader(), interfaces, rpcInvocationHandler);

        return proxyInstance;
    }

    @Override
    public Class<?> getObjectType() {
        return this.type;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
