package org.cloud.easy.feign;


import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.Method;

public class MethodAnnotationHandle {

    private Class<?> type;

    private BeanFactory beanFactory;

    private String url;


    public MethodAnnotationHandle() {}

    public MethodAnnotationHandle(Class<?> type, BeanFactory beanFactory, String url) {
        this.type = type;
        this.beanFactory = beanFactory;
        this.url = url;
    }


    public void handleMethod() {

        Method[] methods = type.getMethods();

        // 取出当前接口中 所有的方法 然后拿到每一个方法的注解
        for (Method method : methods) {

        }


    }
}
