package com.bmts.heating.commons.utils.container;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

@Component
@Description("获取全局容器bean")
public final class SpringBeanFactory implements ApplicationContextAware, BeanFactoryAware {
    private static ApplicationContext context;
    private static BeanFactory beanFactory;

    public static <T> T getBean(Class<T> c){
        return context.getBean(c);
    }


    public static <T> T getBean(String name,Class<T> clazz){
        return context.getBean(name,clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
    /**
     * 通过name获取 Bean
     */
    public static Object getBean(String name) {
        Object object=null;
        try{
             object= context.getBean(name);
        }catch (Exception e){
        }
        return object;
    }
    /**
     * 获取 applicationContext 的值
     * @return applicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return context;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
    /**
     * 获取 beanFactory 的值
     * @return BeanFactory
     */
    public static BeanFactory getBeanFactory(){
        return beanFactory;
    }
}
