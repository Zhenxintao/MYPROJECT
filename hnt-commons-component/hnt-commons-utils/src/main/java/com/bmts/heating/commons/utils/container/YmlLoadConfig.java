package com.bmts.heating.commons.utils.container;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.List;

@Configuration
@AutoConfigureOrder(1)
public class YmlLoadConfig {

    @Bean
    @Description("统一加载配置")
    public static PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        ClassPathResource kafkaYaml = new ClassPathResource("application-kafka.yml");   //加载kafka配置
        ClassPathResource redisYaml = new ClassPathResource("application-redis.yml");   //加载redis配置
        ClassPathResource heartbeatYaml = new ClassPathResource("application-heartbeat.yml");    //加载heartbeat服务端配置
        ClassPathResource heartclientYaml = new ClassPathResource("heartbeat.yml");    //加载heartbeat客户端配置
        ClassPathResource mysqlYaml = new ClassPathResource("application-mysql.yml");    //加载mysql配置
        ClassPathResource consulYaml = new ClassPathResource("application-consul.yml");    //加载consul配置
        ClassPathResource ldapYaml = new ClassPathResource("application-ldap.yml");    //加载openldap配置
        ClassPathResource colonyYaml = new ClassPathResource("application-colony.yml");    //加载分布式集群配置信息
        ClassPathResource esYaml = new ClassPathResource("application-es.yml");    //加载Elasticsearch集群配置
        List<Resource> rslit = new ArrayList<Resource>();

        if(kafkaYaml.exists())
            rslit.add(kafkaYaml);
        if(redisYaml.exists())
            rslit.add(redisYaml);
        if(heartbeatYaml.exists())
            rslit.add(heartbeatYaml);
        if(heartclientYaml.exists())
            rslit.add(heartclientYaml);
        if(mysqlYaml.exists())
            rslit.add(mysqlYaml);
        if(colonyYaml.exists())
            rslit.add(colonyYaml);
        if(ldapYaml.exists())
            rslit.add(ldapYaml);
        if(consulYaml.exists())
            rslit.add(consulYaml);
        if(esYaml.exists())
            rslit.add(esYaml);
        if(rslit.size()>0){
            Resource[] resources=new Resource[rslit.size()];
            for(int i = 0; i < rslit.size();i++){
                resources[i] = rslit.get(i);
            }
            yaml.setResources(resources);
        }
        configurer.setProperties(yaml.getObject());
        return configurer;
    }
}
