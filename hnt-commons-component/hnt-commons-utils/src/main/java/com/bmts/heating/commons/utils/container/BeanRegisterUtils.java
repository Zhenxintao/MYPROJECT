package com.bmts.heating.commons.utils.container;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Component
public class BeanRegisterUtils {
    private final static Logger LOGGER = LoggerFactory.getLogger(BeanRegisterUtils.class);

    /**
     * 向Spring容器中注入bean(构造器注入)
     * @param beanName bean名称
     * @param beanClass bean Class对象
     * @param constructorArgs 参数
     * @param <T>
     */
    public static <T> void registerBean(String beanName, Class<T> beanClass, Object ... constructorArgs) {
        if (Objects.isNull(beanClass)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("beanClass为空，无法注入:", beanName);
            }
            return;
        }

        //构建BeanDefinitionBuilder
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(beanClass);

        //添加Bean对象构造函数的参数
        Optional.ofNullable(constructorArgs).ifPresent(argArr ->
                Arrays.stream(argArr).forEach(builder::addConstructorArgValue));

        //从builder中获取到BeanDefinition对象
        BeanDefinition beanDefinition = builder.getRawBeanDefinition();

        //获取spring容器中的IOC容器
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) SpringBeanFactory.getApplicationContext().getAutowireCapableBeanFactory();

        //向IOC容器中注入bean对象
        defaultListableBeanFactory.registerBeanDefinition(beanName, beanDefinition);
    }
}
