package com.bmts.heating.commons.entiy.baseInfo.request;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseUser;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class HeatAreaChangeDto extends BaseUser {
    private Integer id;

    private BigDecimal newValue;

    private Integer level;

    private Integer relevanceId;

    private LocalDateTime createTime;

    private Boolean status = true;
}
