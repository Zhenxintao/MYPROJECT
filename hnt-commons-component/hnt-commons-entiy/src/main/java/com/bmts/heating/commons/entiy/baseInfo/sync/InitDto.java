package com.bmts.heating.commons.entiy.baseInfo.sync;

import com.bmts.heating.commons.entiy.baseInfo.sync.update.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author naming
 * @description
 * @date 2021/6/2 13:51
 **/
@ApiModel("基础数据同步")
@Data
public class InitDto {

    @ApiModelProperty("热网同步")
    List<HeatNetInitDto> heatNetInitDtos;
    @ApiModelProperty("热源同步")
    List<HeatSourceInitDto> heatSourceInitDtos;
    @ApiModelProperty("热力站同步")
    List<HeatStationInitDto> heatStationInitDtos;
    @ApiModelProperty("控制柜同步")
    List<HeatCabinetInitDto> heatCabinetInitDtos;
    @ApiModelProperty("系统同步")
    List<HeatSystemInitDto> heatSystemInitDtos;
    @ApiModelProperty("点同步")
    List<HeatPointInitDto> heatPointInitDtos;

}
