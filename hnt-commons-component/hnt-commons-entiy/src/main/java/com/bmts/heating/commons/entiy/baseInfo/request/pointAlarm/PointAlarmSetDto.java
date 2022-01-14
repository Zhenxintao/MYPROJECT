package com.bmts.heating.commons.entiy.baseInfo.request.pointAlarm;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("点表查询类")
@Data
public class PointAlarmSetDto extends BaseDto {

    @ApiModelProperty("1.只读 2.可读可写 3.只写")
    private Integer pointConfig;

    @ApiModelProperty("类型 1.AI模拟量 2.DI 数字量 3.TX 日期时间")
    private int type;

    @ApiModelProperty("网测类型 0.公用 1.一次测 2.二次测")
    private Integer netFlag;

    @ApiModelProperty("换热站或热源Id")
    private Integer heatId;

    @ApiModelProperty("换热站或热源区分")
    private Integer heatType;

    @ApiModelProperty("换热站或热源区分")
    private Integer setType;
}
