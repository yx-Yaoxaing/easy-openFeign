package org.cloud.easy.feign;


import org.cloud.easy.feign.annotation.EnableRpcClients;
import org.cloud.easy.feign.annotation.RpcClients;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class RpcClientsRegistrar implements ImportBeanDefinitionRegistrar , ResourceLoaderAware, EnvironmentAware {

    AtomicInteger atomicInteger = new AtomicInteger(1);

    private Environment environment;
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {

        registerRpcClientBeanDefinitions(importingClassMetadata,registry);

    }

    private void registerRpcClientBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        LinkedHashSet<BeanDefinition> candidateComponents = new LinkedHashSet();
        Set<String> basePackages = this.getBasePackages(annotationMetadata);
        ClassPathScanningCandidateComponentProvider scanner = this.getScanner();
        Iterator<String> iterator = basePackages.iterator();
        while (iterator.hasNext()) {
            String basePackage = iterator.next();
            Set<BeanDefinition> beanDefinitionSet = scanner.findCandidateComponents(basePackage);
            candidateComponents.addAll(beanDefinitionSet);
        }
        Iterator<BeanDefinition> definitionIterator = candidateComponents.iterator();
        while (definitionIterator.hasNext()) {
            BeanDefinition beanDefinition = definitionIterator.next();
            AnnotatedBeanDefinition abd = (AnnotatedBeanDefinition)beanDefinition;
            AnnotationMetadata metadata = abd.getMetadata();
            Assert.isTrue(metadata.isInterface(), "@FeignClient can only be specified on an interface");
            Map<String, Object> attributes = metadata.getAnnotationAttributes(RpcClients.class.getCanonicalName());
            // 逻辑处理
            this.registerRpcClientToIocContainer(registry, metadata, attributes);
        }
    }

    private void registerRpcClientToIocContainer(BeanDefinitionRegistry registry, AnnotationMetadata annotationMetadata, Map<String, Object> attributes) {
        ConfigurableBeanFactory beanFactory = registry instanceof ConfigurableBeanFactory ? (ConfigurableBeanFactory)registry : null;

        String className = annotationMetadata.getClassName();
        Class clazz = ClassUtils.resolveClassName(className, (ClassLoader)null);
        String url = (String) attributes.get("url");
        String name = (String) attributes.get("name");
        RpcClientFactoryBean rpcClientFactoryBean = new RpcClientFactoryBean();
        rpcClientFactoryBean.setType(clazz);
        rpcClientFactoryBean.setBeanFactory(beanFactory);
        rpcClientFactoryBean.setUrl(url);
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz, () -> {
            try {
                return rpcClientFactoryBean.getObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
        String beanName = beanNameGenerate() + (name == null ? atomicInteger.incrementAndGet() : name);
        registry.registerBeanDefinition(beanName,beanDefinitionBuilder.getBeanDefinition());
    }

    public static String beanNameGenerate(){
        return "#"+UUID.randomUUID().toString()+"#";
    }


    private ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                boolean isCandidate = false;
                if (beanDefinition.getMetadata().isIndependent() && !beanDefinition.getMetadata().isAnnotation()) {
                    isCandidate = true;
                }

                return isCandidate;
            }
        };
    }

    private Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {

        Map<String, Object> attributes = importingClassMetadata.getAnnotationAttributes(EnableRpcClients.class.getCanonicalName());
        Set<String> basePackages = new HashSet();
        String[] var4 = (String[])((String[])attributes.get("value"));
        int var5 = var4.length;

        int var6;
        String pkg;
        for(var6 = 0; var6 < var5; ++var6) {
            pkg = var4[var6];
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        var4 = (String[])((String[])attributes.get("basePackages"));
        var5 = var4.length;

        for(var6 = 0; var6 < var5; ++var6) {
            pkg = var4[var6];
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        Class[] var8 = (Class[])((Class[])attributes.get("basePackageClasses"));
        var5 = var8.length;

        for(var6 = 0; var6 < var5; ++var6) {
            Class<?> clazz = var8[var6];
            basePackages.add(ClassUtils.getPackageName(clazz));
        }
        if (basePackages.isEmpty()) {
            basePackages.add(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }
        return basePackages;
    }


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {

    }
}
