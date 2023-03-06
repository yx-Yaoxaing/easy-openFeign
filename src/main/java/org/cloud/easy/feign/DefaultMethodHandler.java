package org.cloud.easy.feign;


import org.springframework.beans.factory.BeanFactory;

public class DefaultMethodHandler implements MethodHandler {

    private String url;

    private BeanFactory beanFactory;

    private String methodPath;



    @Override
    public Object invoke(Object[] var1) throws Throwable {
        return null;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public String getMethodPath() {
        return methodPath;
    }

    public void setMethodPath(String methodPath) {
        this.methodPath = methodPath;
    }
}
