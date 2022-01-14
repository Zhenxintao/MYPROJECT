package com.bmts.heating.commons.entiy.gathersearch.response.history;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author naming
 * @description 点配置信息
 * @date 2021/1/8 9:14
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PointBasicInfo {
    @ApiModelProperty("单位")
    private String unitValue;
    @ApiModelProperty("单位分类")
    private String unitType;
    @ApiModelProperty("点中文名称")
    private String pointDes;
    @ApiModelProperty("点标签名")
    private String pointName;
    @ApiModelProperty("显示颜色(预留)")
    private String color;
    @ApiModelProperty("曲线显示形态(预留)")
    private String disableType;
}
