package com.bmts.heating.commons.entiy.forecast;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author naming
 * @description
 * @date 2021/4/20 15:57
 **/
@ApiModel("负荷预测配置corn")
@Data
public class CronDto {
    @DateTimeFormat(pattern="HH:mm")
    @ApiModelProperty("小时或者阶段预测时间点")
    private String cron;
    @DateTimeFormat(pattern="HH:mm")
    @ApiModelProperty("七天预测时间点")
    private String weekCron;
    @ApiModelProperty("星期几")
    private Integer week;
}
