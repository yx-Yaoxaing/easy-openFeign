package org.cloud.easy.feign;


public class RpcClientRequest {

    private String requestUrl;

    private String requestMethod;

    public RpcClientRequest(String requestUrl, String requestMethod) {
        this.requestUrl = requestUrl;
        this.requestMethod = requestMethod;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public String getRequestMethod() {
        return requestMethod;
    }
}
