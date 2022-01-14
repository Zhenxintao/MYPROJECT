package com.bmts.heating.commons.utils.container;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;

public class RegistryBeanConfig {

    public static <T> T registerBean(ApplicationContext context, String name, Class<T> clazz, Object... args) {
        if(context.containsBean(name)) {
            Object bean = context.getBean(name);
            if (bean.getClass().isAssignableFrom(clazz)) {
                return (T) bean;
            } else {
                throw new RuntimeException("BeanName 重复 " + name);
            }
        }


        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        for (Object arg : args) {
            beanDefinitionBuilder.addConstructorArgValue(arg);
        }
        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();

        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) SpringBeanFactory.getBeanFactory();
        beanFactory.registerBeanDefinition(name, beanDefinition);
        return context.getBean(name, clazz);
    }
    public static void removeBean(ApplicationContext applicationContext, Class cls){
        String name = cls.getSimpleName();
        name = name.replaceFirst(name.substring(0,1), name.substring(0,1).toLowerCase());
        removeBean(applicationContext, name);
    }

    public static void removeBean(ApplicationContext applicationContext, String beanName){
        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) SpringBeanFactory.getBeanFactory();
        beanFactory.removeBeanDefinition(beanName);
    }

}
