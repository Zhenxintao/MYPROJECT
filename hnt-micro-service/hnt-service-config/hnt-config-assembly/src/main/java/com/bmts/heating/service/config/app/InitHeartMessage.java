package com.bmts.heating.service.config.app;

import com.bmts.heating.commons.heartbeat.config.ServiceInitEntry;
import com.bmts.heating.commons.heartbeat.pojo.*;
import com.bmts.heating.service.config.pojo.MicroServerNode;
import com.bmts.heating.service.config.pojo.TokenConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

@Configuration
public class InitHeartMessage implements ServiceInitEntry {

    @Autowired
    @SuppressWarnings("all")
    private MicroServerNode microServerNode;
    @Autowired
    private TokenConfig tokenConfig;

    /**
     * 初始化心跳数据(application)
     * @return
     */
    @Override
    @Bean("local_application_heart_message")
    @Description("将本地application服务写入心跳数据包中")
    public HeartMessagePackage initHeartApplictionMessage() {
        HeartMessagePackage message = new HeartMessagePackage();
        message.setApplication_id(microServerNode.getApplication_id());
        message.setApplication_name(microServerNode.getApplication_name());
        message.setServer_host(microServerNode.getService_host());
        message.setServer_port(microServerNode.getService_port());
        message.setService_name(microServerNode.getService_name());
        message.setTokens(microServerNode.getTokens());
        message.setIncrement(tokenConfig.getIncrement());
        message.setCircle(tokenConfig.getCircle());
        message.setServer_code(Constants.ServerType.APPLICATION);

        //配置服务连接池信息
//        message.setMaxIdle(applicationServerNode.getMaxIdle());
//        message.setMaxTotal(applicationServerNode.getMaxTotal());
//        message.setMinIdle(applicationServerNode.getMinIdle());
//        message.setMaxWaitMillis(applicationServerNode.getMaxWaitMillis());
//        message.setLifo(applicationServerNode.isLifo());
//        message.setBlockWhenExhausted(applicationServerNode.isBlockWhenExhausted());
        return message;
    }

    /**
     * 将当前服务的服务名和实例名写入bean,本地令牌缓存可以获取当前服务对象的信息
     * @return
     */
    @Override
    @Bean("simpleMicroInfo")
    public String deployService(){
        return microServerNode.getApplication_name()+","+microServerNode.getService_name();
    }

    /**
     * 初始化心跳数据(grpc)
     * @return
     */
    @Override
    @Bean(value = "local_heart_message")
    @Description("将本地服务信息写入心跳数据包中")
    public HeartMessagePackage initHeartGrpcMessage(){
        HeartMessagePackage hm = new HeartMessagePackage();
        hm.setApplication_id(microServerNode.getApplication_id());
        hm.setApplication_name(microServerNode.getApplication_name());
        hm.setService_name(microServerNode.getService_name());
        hm.setServer_host(microServerNode.getService_host());
        hm.setServer_port(microServerNode.getService_port());
        hm.setTokens(microServerNode.getTokens());
        hm.setIncrement(tokenConfig.getIncrement());
        hm.setCircle(tokenConfig.getCircle());
        hm.setServer_code(Constants.ServerType.GRPC);

        //配置服务连接池信息
//        hm.setMaxIdle(grpcServerNode.getMaxIdle());
//        hm.setMaxTotal(grpcServerNode.getMaxTotal());
//        hm.setMinIdle(grpcServerNode.getMinIdle());
//        hm.setMaxWaitMillis(grpcServerNode.getMaxWaitMillis());
//        hm.setLifo(grpcServerNode.isLifo());
//        hm.setBlockWhenExhausted(grpcServerNode.isBlockWhenExhausted());

        return hm;
    }

    /**
     * 初始化心跳数据(web)
     * @return
     */
    @Override
    @Bean("local_web_heart_message")
    @Description("将本地web服务写入心跳数据包中")
    public HeartMessagePackage initHeartWebMessage() {
        HeartMessagePackage message = new HeartMessagePackage();
        message.setApplication_id(microServerNode.getApplication_id());
        message.setApplication_name(microServerNode.getApplication_name());
        message.setServer_host(microServerNode.getService_host());
        message.setServer_port(microServerNode.getService_port());
        message.setService_name(microServerNode.getService_name());
        message.setTokens(microServerNode.getTokens());
        message.setServer_code(Constants.ServerType.WEB);

        //配置服务连接池信息
//        message.setMaxIdle(applicationServerNode.getMaxIdle());
//        message.setMaxTotal(applicationServerNode.getMaxTotal());
//        message.setMinIdle(applicationServerNode.getMinIdle());
//        message.setMaxWaitMillis(applicationServerNode.getMaxWaitMillis());
//        message.setLifo(applicationServerNode.isLifo());
//        message.setBlockWhenExhausted(applicationServerNode.isBlockWhenExhausted());
        return message;
    }
}
