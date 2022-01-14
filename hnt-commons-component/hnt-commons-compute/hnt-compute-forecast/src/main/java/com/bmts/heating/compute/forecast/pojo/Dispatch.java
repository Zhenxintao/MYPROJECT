package com.bmts.heating.compute.forecast.pojo;

import lombok.Data;

/**
 * forecast_source_core  数据表 的  dispatch  字段实体类
 */
@Data
public class Dispatch {

    // dispatch  '调度 json形式',
    // [{"endTime":"00:30","startTime":"00:00"},{"endTime":"01:00","startTime":"00:30"}]

    private String startTime;

    private String endTime;


}
