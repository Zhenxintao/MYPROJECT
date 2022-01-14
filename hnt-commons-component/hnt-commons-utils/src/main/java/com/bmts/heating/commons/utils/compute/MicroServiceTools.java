package com.bmts.heating.commons.utils.compute;

import com.alibaba.fastjson.JSON;
import com.bmts.heating.distribution.adapter.DatasCenterAdapter;
import com.bmts.heating.distribution.config.GovernConfig;
import com.bmts.heating.distribution.config.GovernConnection;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MicroServiceTools {

    private static GovernConfig governConfig;

    @Autowired
    public void setGovernConfig(GovernConfig governConfig){
        MicroServiceTools.governConfig=governConfig;
    }
    @Autowired
    private DatasCenterAdapter datasCenterAdapter;

    @Data
    public static class AlivedService{
        private String application_name;   //  客户端应用名
        private Long application_id;    //客户端当前节点唯一ID
        private String service_name;    //当前应用服务名
        private String service_host;    //客户端服务地址
        private String service_port;    //客户端服务端口
    }

    /**
     * 获取服务注册地址
     * @param applictionname
     * @param serviceType
     * @return
     */
    public static String getServiceRegisterUrl(String applictionname, ServiceType serviceType){
        if(serviceType==ServiceType.grpc){
            return governConfig.getGrpc_url()+"/"+applictionname;
        }else if(serviceType==ServiceType.application){
            return governConfig.getApplication_url()+"/"+applictionname;
        }else{
            return "";
        }
    }

    /**
     * 获取服务实例锁地址
     * @param servicename   服务名
     * @param serviceType   服务类型
     * @param servicecase   服务实例名
     * @return
     */
    public static String getServiceLockUrl(String servicename, ServiceType serviceType,String servicecase){
        return governConfig.getLock()+"/"+serviceType+"/"+servicename+"/"+servicecase;
    }

    /**
     * 根据服务类型获取服务类型地址
     * @param servicetype   服务类型
     * @return
     */
    public static String getTypeUrl(ServiceType servicetype){
        if(servicetype==ServiceType.grpc){
            return governConfig.getGrpc_url();
        }else if(servicetype==ServiceType.application){
            return governConfig.getApplication_url();
        }else{
            return "";
        }
    }

    /**
     * 获取当前所有存活的服务(grpc、application)
     */
    public String getAlivedServices(){
        List<AlivedService> allAlivedServices= new ArrayList<>();
        List<AlivedService> grpcList= (List<AlivedService>) datasCenterAdapter.getFiles(governConfig.getGrpc_url(), AlivedService.class);
        List<AlivedService> applicationList= (List<AlivedService>) datasCenterAdapter.getFiles(governConfig.getApplication_url(), AlivedService.class);
        allAlivedServices.addAll(grpcList);
        allAlivedServices.addAll(applicationList);
        //System.out.println(JSON.toJSONString(allAlivedServices));
        return JSON.toJSONString(allAlivedServices);
    }
}
