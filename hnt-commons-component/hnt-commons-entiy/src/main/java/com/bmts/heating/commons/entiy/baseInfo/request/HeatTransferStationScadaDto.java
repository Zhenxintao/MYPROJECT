package com.bmts.heating.commons.entiy.baseInfo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("scada换热站查询类")
@Data
public class HeatTransferStationScadaDto {

    // sortName 会有sql 注入问题

    @ApiModelProperty("关键字")
    private String keyWord;
    @ApiModelProperty("当前页")
    private int currentPage = 1;
    @ApiModelProperty("页大小")
    private int pageCount = 20;
    @ApiModelProperty("操作人")
    private int userId;
    @ApiModelProperty("状态:true 正常，false 冻结")
    private Boolean status;
}
