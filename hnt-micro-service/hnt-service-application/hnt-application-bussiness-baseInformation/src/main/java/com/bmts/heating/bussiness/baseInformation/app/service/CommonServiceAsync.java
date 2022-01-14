package com.bmts.heating.bussiness.baseInformation.app.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.bussiness.baseInformation.app.utils.BaseInfoUrlConn;
import com.bmts.heating.commons.basement.model.db.entity.WebPageConfig;
import com.bmts.heating.commons.db.service.WebPageConfigService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @ClassName: CommonServiceAsync
 * @Description: 处理异步业务
 * @Author: pxf
 * @Date: 2021/7/12 17:42
 * @Version: 1.0
 */

@Component
public class CommonServiceAsync {

    private static Logger logger = LoggerFactory.getLogger(CommonServiceAsync.class);

    @Autowired
    private WebPageConfigService webPageConfigService;

    // 进行接口调用，重启采集线程
    @Async("asyncUrlConnExecutor")
    public void reloadMonitor() {
        WebPageConfig monitorReloadUrl = webPageConfigService.getOne(Wrappers.<WebPageConfig>lambdaQuery().eq(WebPageConfig::getConfigKey, "monitorReloadUrl"));
        if (monitorReloadUrl != null && StringUtils.isNotBlank(monitorReloadUrl.getJsonConfig())) {
            String jsonConfig = monitorReloadUrl.getJsonConfig();
            JSONObject strJson = JSONObject.parseObject(jsonConfig);
            // 返回的请求路径
            String url = strJson.getString("reloadUrl");
            BaseInfoUrlConn.doGet(url);
        }
    }

    // 进行接口调用，加载报警实时数据
    @Async("asyncUrlConnExecutor")
    public void loadAlarm() {
        WebPageConfig monitorReloadUrl = webPageConfigService.getOne(Wrappers.<WebPageConfig>lambdaQuery().eq(WebPageConfig::getConfigKey, "monitorLoadAlarm"));
        if (monitorReloadUrl != null && StringUtils.isNotBlank(monitorReloadUrl.getJsonConfig())) {
            String jsonConfig = monitorReloadUrl.getJsonConfig();
            JSONObject strJson = JSONObject.parseObject(jsonConfig);
            // 返回的请求路径
            String url = strJson.getString("loadAlarmUrl");
            BaseInfoUrlConn.doGet(url);
        }
    }

}
