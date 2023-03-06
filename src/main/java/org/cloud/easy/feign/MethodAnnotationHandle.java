package org.cloud.easy.feign;


import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;

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


    public HashMap<Method, MethodHandler> handleMethod() {

        Method[] methods = type.getMethods();

        HashMap<Method, MethodHandler> map = new HashMap<>(methods.length);
        // 取出当前接口中 所有的方法 然后拿到每一个方法的注解
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            Annotation methodAnnotation = annotations[0];
            if (RequestMapping.class.isInstance(methodAnnotation) || methodAnnotation.annotationType().isAnnotationPresent(RequestMapping.class)) {
                RequestMapping methodMapping = (RequestMapping) AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
                if (methodMapping.value().length > 0) {
                    DefaultMethodHandler defaultMethodHandler = new DefaultMethodHandler();
                    String pathValue = methodMapping.name();
                    defaultMethodHandler.setMethodPath(pathValue);
                    defaultMethodHandler.setUrl(url);
                    defaultMethodHandler.setBeanFactory(beanFactory);
                    map.put(method,defaultMethodHandler);
                }
              }
            }
        return map;
        }
    }

