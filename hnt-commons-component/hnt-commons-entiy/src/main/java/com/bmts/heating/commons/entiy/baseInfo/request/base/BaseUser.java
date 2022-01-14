package com.bmts.heating.commons.entiy.baseInfo.request.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName BaseUser
 * @Author naming
 * @Date 2020/11/20 9:54
 **/
@Data
public class BaseUser {
    
    @ApiModelProperty("操作人")
    private String userName;

    private int userId;

    @ApiModelProperty("操作人IP")
    private String ip;
}
