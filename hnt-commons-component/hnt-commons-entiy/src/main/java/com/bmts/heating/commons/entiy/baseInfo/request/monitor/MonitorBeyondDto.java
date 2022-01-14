package com.bmts.heating.commons.entiy.baseInfo.request.monitor;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MonitorBeyondDto {

    @ApiModelProperty("操作人")
    private int userId;

    @ApiModelProperty("当前页")
    private int currentPage = 1;
    
    @ApiModelProperty("页大小")
    private int pageCount = 20;

    @ApiModelProperty("查询类型：-2 小于，-1小于等于，0 等于，1大于等于，2大于")
    private Integer type;

    @ApiModelProperty("查询参量")
    private String columnName;

    @ApiModelProperty("设置值")
    private double value;

    @ApiModelProperty("热力站状态:true 正常，false 冻结")
    private Boolean status;

    @ApiModelProperty("是否导出:默认为FALSE")
    private Boolean export = false;

}
