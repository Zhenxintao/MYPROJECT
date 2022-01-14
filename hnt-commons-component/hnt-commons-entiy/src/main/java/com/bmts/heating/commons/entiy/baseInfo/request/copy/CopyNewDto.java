package com.bmts.heating.commons.entiy.baseInfo.request.copy;

import com.bmts.heating.commons.entiy.baseInfo.pojo.HeatTransferStation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName CopyNewDto
 * @Author pxf
 * @Date 2020/11/29 13:42
 **/
@Data
@ApiModel(value = "复制到新建站实体")
public class CopyNewDto extends CopyDto {

    @ApiModelProperty(value = "新建热力站实体")
    private HeatTransferStation heatTransferStation;

}
