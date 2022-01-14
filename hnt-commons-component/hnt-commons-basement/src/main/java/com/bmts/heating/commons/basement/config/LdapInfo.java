//package com.bmts.heating.commons.basement.config;
//
//
//import lombok.Data;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Description;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.stereotype.Component;
//
//@Data
//@Component
//@PropertySource(value = "classpath:application-ldap.yml")
//@ConfigurationProperties(prefix = "spring")
//@Description("ldap配置信息")
//public class LdapInfo {
//
//    @Value("${spring.ldap.urls}")
//    private String urls;
//    @Value("${spring.ldap.base}")
//    private String base;
//}