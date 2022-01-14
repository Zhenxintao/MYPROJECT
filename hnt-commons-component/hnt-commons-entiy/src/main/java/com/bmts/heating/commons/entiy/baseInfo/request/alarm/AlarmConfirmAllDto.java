package com.bmts.heating.commons.entiy.baseInfo.request.alarm;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("批量确认实体类")
public class AlarmConfirmAllDto extends BaseUser {
    @ApiModelProperty("报警id集合")
    private Integer[] ids;
}
