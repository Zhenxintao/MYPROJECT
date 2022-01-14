package com.bmts.heating.grpc.dataCleaning.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

/**
 * spring工具类
 */
@Component
public class SpringContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        T bean = null;
        try {
            bean = (T) applicationContext.getBean(name);
        } catch (Exception e) {
        }
        return bean;
    }

    public static <T> T getBean(Class<T> requiredType) {
        T bean = null;
        try {
            bean = applicationContext.getBean(requiredType);
        } catch (Exception e) {
        }
        return bean;
    }

    public static boolean isContainsBean(String name) {
        return applicationContext.containsBean(name);
    }

    public static void publishEvent(ApplicationEvent event) {
        applicationContext.publishEvent(event);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        SpringContext.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
