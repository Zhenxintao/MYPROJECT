package com.bmts.heating.commons.basement.model.db.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName CabinetPoint
 * @Author naming
 * @Date 2020/11/20 11:45
 **/
@Data
public class CabinetPoint {

    @ApiModelProperty("控制柜名称")
    private String cabiNetName;
    @ApiModelProperty("热力站id")
    private int stationId;
    @ApiModelProperty("点表id")
    private int pointStandardId;
    private int id;
    @ApiModelProperty("系统id")
    private int heatSystemId;
    @ApiModelProperty("模板id")
    private int pointConfigId;
}
