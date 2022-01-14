package com.bmts.heating.service.task.storm.jobs;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.DicView;
import com.bmts.heating.commons.db.service.DicViewService;
import com.bmts.heating.service.task.storm.pojo.SecondMongoDto;
import com.bmts.heating.service.task.storm.service.SecondHistoryService;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @ClassName: SecondHistoryJob
 * @Description: 二网历史数据
 * @Author: pxf
 * @Date: 2022/1/12 14:30
 * @Version: 1.0
 */

@Component("second_history_data")
public class SecondHistoryJob implements Job {


    @Autowired
    private SecondHistoryService secondHistoryService;

    @Autowired
    private DicViewService dicViewService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<DicView> dicList = dicViewService.list(Wrappers.<DicView>lambdaQuery().eq(DicView::getFlag, "iot_basicBuildTree"));
        if (!CollectionUtils.isEmpty(dicList)) {
            dicList.stream().forEach(e -> {
                if (StringUtils.isNotBlank(e.getCode())) {
                    List<SecondMongoDto> mongoList = mongoTemplate.findAll(SecondMongoDto.class, e.getCode());
                    secondHistoryService.insertTdHistory(mongoList);
                }
            });
        }

    }


}
