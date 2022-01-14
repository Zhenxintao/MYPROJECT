package com.bmts.heating.commons.entiy.baseInfo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("点表改变（添加、删除）实体类")
public class PointStandardChangeDto {
    @ApiModelProperty("id集合")
    Integer[] pointStandardIds;
}
