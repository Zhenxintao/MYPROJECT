package com.bmts.heating.commons.entiy.baseInfo.request.copy;

import com.bmts.heating.commons.entiy.baseInfo.pojo.HeatSystem;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @ClassName SystemDto
 * @Author naming
 * @Date 2020/11/19 20:07
 **/
@Data
@ApiModel("复制站点 新建系统")
public class SystemDto extends HeatSystem {
    private String heatSystemName;
}
