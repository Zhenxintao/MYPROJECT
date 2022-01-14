package com.bmts.heating.commons.entiy.baseInfo.request.copy;

import com.bmts.heating.commons.entiy.baseInfo.pojo.HeatCabinet;
import com.bmts.heating.commons.entiy.baseInfo.pojo.HeatSystem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName CopyCabinetDto
 * @Author pxf
 * @Date 2020/11/29 13:42
 **/
@Data
@ApiModel(value = "控制柜实体")
public class CopyCabinetDto extends HeatCabinet {

    @ApiModelProperty("系统集合")
    List<HeatSystem> listHeatSystem;
    @ApiModelProperty("控制柜名称")
    String heatCabinetName;
}
