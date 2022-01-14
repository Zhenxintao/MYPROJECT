package com.bmts.heating.commons.entiy.baseInfo.request.station;

import com.bmts.heating.commons.entiy.baseInfo.pojo.HeatCabinet;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName BuildCabinet
 * @Author naming
 * @Date 2020/11/21 16:26
 **/
@Data
@ApiModel("控制柜信息")
public class BuildCabinet extends HeatCabinet {
    @ApiModelProperty("系统集合")
    List<BuildSystem> buildSystems;
}
