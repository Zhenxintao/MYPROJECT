package com.bmts.heating.commons.entiy.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author naming
 * @description
 * @date 2021/1/14 20:19
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("单位分类响应实体")
public class PointUnitType {
    private int id;
    @ApiModelProperty("单位名称 例如 ℃")
    private String unitName;
    @ApiModelProperty("单位分类名称 例如: 温度 压力")
    private String unitTypeName;
}
