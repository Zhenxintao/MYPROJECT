package com.bmts.heating.commons.entiy.energy;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ApiModel("传入参数")
@Data
public class AreaChangeHistoryDto extends BaseDto {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("上次更新旧面积")
    private BigDecimal oldValue;

    @ApiModelProperty("新面积")
    private BigDecimal newValue;

    @ApiModelProperty("等级 1.网 2.源 3.站 4.控制柜(0号机组) 5.系统")
    private Integer level;

    @ApiModelProperty("关联id")
    private Integer relevanceId;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("是否有效")
    private Boolean status = true;

    @ApiModelProperty("名字（热网名或热源或热站或系统名，与type保持一致）")
    private  String name;

    @ApiModelProperty("请求类型source：热源，net：热网，station：热站，system:系统")
    private  String type;
}
