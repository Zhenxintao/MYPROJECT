package com.bmts.heating.service.config.app;

import com.bmts.heating.commons.utils.compute.JSnowflake;
import com.bmts.heating.commons.utils.compute.MicroServiceTools;
import com.bmts.heating.commons.utils.compute.ServiceType;
import com.bmts.heating.commons.utils.container.SpringBeanFactory;
import com.bmts.heating.distribution.adapter.DatasCenterAdapter;
import com.bmts.heating.distribution.config.DistributionLock;
import com.bmts.heating.distribution.config.GovernConfig;
import com.bmts.heating.distribution.config.GovernConnection;
import com.bmts.heating.distribution.config.NodeValue;
import com.bmts.heating.service.config.pojo.MicroServerNode;
import com.bmts.heating.service.config.pojo.TokenConfig;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 服务令牌(与服务耦合度高,弃用)
 */
//@Component
public class ServiceTokensBucket {

    private Logger LOGGER= LoggerFactory.getLogger(ServiceTokensBucket.class);

    //@Autowired
    private MicroServerNode microServerNode= SpringBeanFactory.getBean(MicroServerNode.class);
    @Autowired
    private DatasCenterAdapter datasCenterAdapter;
    @Autowired
    private GovernConfig governConfig;
    @Autowired
    private TokenConfig tokenConfig;
    private final long EPOCH = 1596211200000L;
    private JSnowflake jSnowflake= new JSnowflake(1L,1L,EPOCH);

    Gson gson= new Gson();
    private String lock_path;   //consul锁路径
    private String lock_service;    //当前服务实例的锁路径
    private String service_path;    //当前服务实例的注册路径
    private long maxTokens; //最大令牌数

    /**
     * 初始化
     */
    public void initBucket(){
        LOGGER.info("{------------------------------------------}",microServerNode.getOriginal_tokens());
        LOGGER.info("{----------------------------}",governConfig.getApplication_url());
        this.lock_path=governConfig.getLock();//获得consul锁路径;
        this.lock_service=lock_path+"/"+tokenConfig.getServiceType()+"/"+microServerNode.getApplication_name()+"/"+microServerNode.getService_name();  //获得当前服务实例的锁路径
        //获得当前服务实例的注册路径
        this.service_path= MicroServiceTools.getTypeUrl(ServiceType.valueOf(tokenConfig.getServiceType()))+"/"+microServerNode.getApplication_name()+"/"+microServerNode.getService_name();
//        if(tokenConfig.getServiceType().equals("grpc")){
//            this.service_path=governConfig.getGrpc_url()+"/"+microServerNode.getApplication_name()+"/"+microServerNode.getService_name();
//        }else if(tokenConfig.getServiceType().equals("application")){
//            this.service_path=governConfig.getApplication_url()+"/"+microServerNode.getApplication_name()+"/"+microServerNode.getService_name();
//        }

        NodeValue nv =datasCenterAdapter.checkAndGetNodeExist(service_path);
        if(nv!=null){
            String value=nv.getValue();
            MicroServerNode ac=gson.fromJson(value,MicroServerNode.class);
            this.maxTokens=ac.getOriginal_tokens();    //设置最大令牌数
            LOGGER.info("当前服务实例提供的最大令牌数:{}",this.maxTokens);
        }
    }

    /**
     * 启动令牌自增线程
     */
    public void startRemotTokens(){
        this.initBucket();
        RemoteControl remoteControl = new RemoteControl();
        new Thread(remoteControl).start();
    }

    /**
     * 自增令牌功能
     */
    public void raiseTokens(){
        //抢占该服务实例的分布式锁
        DistributionLock DBL=null;
        try{
            DBL=datasCenterAdapter.grabCreateSessionKey(lock_service,jSnowflake.nextId()+"");
            if(DBL.isGetLock()){
                //获得分布式锁,为服务实例增加token
                long addTokens=this.increaseTokens(service_path);
                LOGGER.info("当前服务增加了{}个令牌..........",addTokens);
            }
        }finally {
            if(DBL.getSessionId()!=null||!"".equals(DBL.getSessionId())){
                //释放分布式锁
                datasCenterAdapter.releaseSessionKey(lock_service,DBL.getSessionName(),DBL.getSessionId());
                LOGGER.info("服务{}:{}释放分布式锁..............",microServerNode.getApplication_name(),microServerNode.getService_name());
            }
        }
    }

    /**
     * 增加令牌
     * @param service_path  //服务实例的注册路径
     * @return  增加数量
     */
    public long increaseTokens(String service_path){
        long increment=tokenConfig.getIncrement();
        NodeValue nv =datasCenterAdapter.checkAndGetNodeExist(service_path);
        if(nv!=null){
            long newTokens=0;
            String value=nv.getValue();
            GovernConnection ac=gson.fromJson(value,GovernConnection.class);
            long nowTokens=ac.getTokens();
            if(this.maxTokens-nowTokens>=increment){
                //未溢出,增加令牌
                nowTokens=nowTokens+increment;
                ac.setTokens(nowTokens);
                newTokens=increment;
            }else if(this.maxTokens-nowTokens<increment&&this.maxTokens-nowTokens>0){
                newTokens=this.maxTokens-nowTokens;
                nowTokens=nowTokens+newTokens;
                ac.setTokens(nowTokens);
            }
            datasCenterAdapter.updateTokensToServices(service_path,ac);
            return newTokens;
        }else{
            return 0;
        }
    }

    /**
     * 启动类
     */
    class RemoteControl implements Runnable{
        @Override
        public void run() {
            while (true){
                raiseTokens();
                try {
                    Thread.sleep(tokenConfig.getCircle());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
