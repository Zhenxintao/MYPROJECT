package com.bmts.heating.commons.entiy.baseInfo.request;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("标准点表查询类")
@Data
public class PointStandardSearchDto extends BaseDto {

    @ApiModelProperty("网测类型 0.公用 1.一次测 2.二次测")
    private Integer netFlag;

    @ApiModelProperty("类型 1.AI模拟量 2.DI 数字量 3.TX 日期时间")
    private int type;

}
