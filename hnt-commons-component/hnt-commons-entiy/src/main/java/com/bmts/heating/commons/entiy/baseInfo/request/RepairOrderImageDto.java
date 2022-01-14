package com.bmts.heating.commons.entiy.baseInfo.request;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("系统logo上传")
@Data
public class RepairOrderImageDto extends BaseUser {
    //    private MultipartFile image;
    @ApiModelProperty("系统名称")
    private String systemName;
}
