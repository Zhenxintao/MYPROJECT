package com.bmts.heating.commons.entiy.quartzjob.request;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("定时任务查询实体类")
public class QueryJobDto extends BaseDto {
    private Boolean status;
    private Integer type;
}
