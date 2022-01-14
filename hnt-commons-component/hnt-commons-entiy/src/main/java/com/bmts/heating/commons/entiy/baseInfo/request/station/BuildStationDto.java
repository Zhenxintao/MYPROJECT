package com.bmts.heating.commons.entiy.baseInfo.request.station;

import com.bmts.heating.commons.entiy.baseInfo.pojo.HeatTransferStation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName BuildStationDto
 * @Author naming
 * @Date 2020/11/21 16:23
 **/
@Data
@ApiModel("建站参数实体")
public class BuildStationDto extends HeatTransferStation {
    @ApiModelProperty("控制柜集合")
    private List<BuildCabinet> cabinets;
}
