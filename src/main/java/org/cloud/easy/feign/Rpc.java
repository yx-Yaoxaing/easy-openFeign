package org.cloud.easy.feign;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

public abstract class Rpc {



    static class RpcInvocationHandler implements InvocationHandler {

        private HashMap<Method,MethodHandler> map = new HashMap<>();

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            MethodHandler methodHandler = map.get(method);
            Object result = methodHandler.invoke(args);
            return result;
        }

        public RpcInvocationHandler(HashMap<Method,MethodHandler> map){
            map.putAll(map);
        }

    }

}
