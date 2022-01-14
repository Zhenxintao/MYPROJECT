package com.bmts.heating.signalr.customer.firstnet;

import com.bmts.heating.commons.container.signalr.config.SignalRCallBackConfig;
import com.bmts.heating.commons.container.signalr.handler.SignalRCustomerHandler;
import com.bmts.heating.signalr.customer.service.AlarmService;
import com.microsoft.signalr.Action1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AlarmHandler implements SignalRCustomerHandler {

    @Autowired
    AlarmService alarmService;
    @Override
    public SignalRCallBackConfig build() {
        SignalRCallBackConfig signalRCallBackConfig =new SignalRCallBackConfig();
        signalRCallBackConfig.setParam(String.class);
        //注册方法名
        signalRCallBackConfig.setTarget("alarmDeal");
        //注册回调 param 为string json数据
        signalRCallBackConfig.setAction((Action1<String>) param -> {
            alarmService.dealFirst(param);
        });
        return signalRCallBackConfig;
    }
}
