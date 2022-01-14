package com.bmts.heating.commons.basement.model.db.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("热网、热源、热力站响应实体")
public class HeatAreaChangeHistoryResponse {
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

    @ApiModelProperty("名字")
    private  String name;
}
