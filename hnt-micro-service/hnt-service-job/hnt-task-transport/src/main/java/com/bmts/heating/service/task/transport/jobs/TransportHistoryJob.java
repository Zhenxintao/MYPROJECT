package com.bmts.heating.service.task.transport.jobs;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bmts.heating.service.task.transport.pojo.TransportHistoryDto;
import com.bmts.heating.service.task.transport.service.TransportHistoryService;
import com.bmts.heating.service.task.transport.utils.UrlConnUtil;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Component("transport_point_history")
public class TransportHistoryJob implements Job {

    private final Logger logger = LoggerFactory.getLogger(TransportHistoryJob.class);
    @Autowired
    private TransportHistoryService transportHistoryService;

    @Value("${transport.historyUrl}")
    private String historyUrl;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        // 从接口读取数据
        String getData = UrlConnUtil.doGet(this.historyUrl);
        // 进行格式转换  TransportHistoryDto
        if (getData == null) {
            logger.error("请求长输实时数据格错误！---{ }", getData);
        } else {
            // 处理业务
            JSONObject responseJson = JSONObject.parseObject(getData);
            // 返回的code 码
            String code = responseJson.getString("code");
            if (StringUtils.isBlank(code)) {
                return;
            }
            // 请求成功
            if (Objects.equals(code, "200")) {
                // 返回的数据
                String data = responseJson.getString("data");
                List<TransportHistoryDto> transportHistoryDtos = JSONArray.parseArray(data, TransportHistoryDto.class);
                if (!CollectionUtils.isEmpty(transportHistoryDtos)) {
                    // 插入历史TD库
                    transportHistoryService.intoHistory(transportHistoryDtos);
                }
            }

        }

        logger.info("---完成长输历史数据入库！---");

    }
}
