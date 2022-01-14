package com.bmts.heating.commons.entiy.baseInfo.sync.add;

import com.bmts.heating.commons.entiy.baseInfo.sync.update.HeatOrganizationInitDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author naming
 * @description
 * @date 2021/6/2 13:11
 **/
@Data
@ApiModel("添加组织机构实体")
public class AddHeatOrganizationInitDto {


    @ApiModelProperty("层级总数")
    private Integer levelTotal;
    @ApiModelProperty(required = true)
    private List<HeatOrganizationInitDto> organizationList;


}





