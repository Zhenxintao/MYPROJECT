package com.bmts.heating.commons.entiy.gathersearch.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
/**
 * @author zxt
 * @description 全网平衡首页曲线
 * @date 2021/1/8 9:25
 **/
@Data
@ApiModel("全网平衡首页曲线")
public class BalanceCurveDto {
        @ApiModelProperty("开始时间")
        private LocalDateTime startTime;
        @ApiModelProperty("结束时间")
        private LocalDateTime endTime;
        @ApiModelProperty("全网平衡id")
        private int id;
}
