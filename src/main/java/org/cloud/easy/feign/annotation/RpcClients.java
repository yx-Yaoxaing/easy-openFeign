package org.cloud.easy.feign.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcClients {

    String url() default "http://127.0.0.1";

    String path() default "/";

    String name() default "";

}
