package com.bmts.heating.commons.entiy.baseInfo.request;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("标准点表导入类")
@Data
public class PointStandardImportDto extends BaseUser {

    @ApiModelProperty("1.只读 2.可读可写 3.只写")
    private Integer pointConfig;


}
