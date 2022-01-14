package com.bmts.heating.web.netApp.controller;

import com.bmts.heating.commons.container.performance.config.ConnectionToken;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.container.performance.exception.MicroException;
import com.bmts.heating.commons.utils.compute.RestfulUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping("rest")
@Api(description = "rest接口测试")
public class RestFulController extends SavantServices {

    private Logger LOGGER = LoggerFactory.getLogger(RestFulController.class);
    @Autowired
    private RestTemplate restTemplate;

    @ApiOperation(value = "应用微服务调用(数据中台)",notes = "调用中台",httpMethod = "GET")
    @ApiImplicitParam(dataType = "int",name = "id",value = "测试ID",required = true)
    @RequestMapping("/test1/{id}")
    public String getRealDatas1(@PathVariable("id") int id){
        ConnectionToken cd=null;
        String backinfo=null;
        try {
            try {
                cd=super.getToken("application_pro1");
                String url= RestfulUtils.resolutionAddress(cd.getHost(),cd.getPort());
                url+="/pro/data/"+id;
                backinfo=restTemplate.getForObject(url,String.class);
                LOGGER.info("返回远程调用结果:-------------{}",backinfo);
            } catch (Exception e) {
                LOGGER.warn("错误:{}",e.getCause());
                backinfo=e.getCause()+"";
            }
           // backinfo=cd.getService_name()+"-----------"+id;
        }  finally {
            super.backToken("application_pro1",cd);
        }
        return backinfo;
    }
    @ApiOperation(value = "采集grpc调用",notes = "测试采集服务grpc功能",httpMethod = "GET")
    @ApiImplicitParam(dataType = "int",name = "id",value = "测试ID",required = true)
    @RequestMapping("/monitor/{id}")
    public String getMonitorData(@PathVariable("id") int id){
        ConnectionToken cd=null;
        String backinfo=null;
        try {
            try {
                cd=super.getToken("application_pro1");
                String url= RestfulUtils.resolutionAddress(cd.getHost(),cd.getPort());
                url+="/pro/monitor/"+id;
                backinfo=restTemplate.getForObject(url,String.class);
                LOGGER.info("返回远程调用结果:-------------{}",backinfo);
            } catch (MicroException e) {
                LOGGER.warn("错误:{}",e.getCause());
                backinfo=e.getCause()+"";
            }
            // backinfo=cd.getService_name()+"-----------"+id;
        }  finally {
            super.backToken("application_pro1",cd);
        }
        return backinfo;
    }
    @ApiOperation(value = "微服务信息",notes = "获取系统所有微服务性能信息",httpMethod = "GET")
    @RequestMapping("/test/performance")
    public String getMicroServiceInfo(){
        ConnectionToken cd=null;
        String backinfo=null;
        try {
            cd=super.getToken("application_performance");
            String url= RestfulUtils.resolutionAddress(cd.getHost(),cd.getPort());
            //url+="/performance/services/list/application_performance/application";
            url+="/performance/type/list";
            backinfo=restTemplate.getForObject(url,String.class);
            LOGGER.info("返回远程调用结果:-------------{}",backinfo);
        } catch (MicroException e) {
            e.printStackTrace();
        } finally {
            super.backToken("application_performance",cd);
        }
        return backinfo;
    }
}
