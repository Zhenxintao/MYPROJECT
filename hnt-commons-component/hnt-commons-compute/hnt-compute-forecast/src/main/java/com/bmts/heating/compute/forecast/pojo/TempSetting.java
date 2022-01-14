package com.bmts.heating.compute.forecast.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * forecast_source_core  数据表 的 tempSetting  字段实体类
 */
@Data
public class TempSetting {

    // [{"tempValue":21,"tempType":1,"endTime":"2021-04-01T16:00:00.000Z","startTime":"2021-03-28T16:00:00.000Z"},
    // {"tempValue":23,"tempType":1,"endTime":"2021-04-12T16:00:00.000Z","startTime":"2021-04-01T16:00:00.000Z"}]

    // 修改后的 数据字段 是：   [{"endTime":"2021-04-06T00:00:00","forecastType":2,"startTime":"2021-04-06T00:00:00","temp":222}]

    private BigDecimal temp;

    private Integer forecastType;

    private LocalDateTime startTime;

    private LocalDateTime endTime;


}
