package com.bmts.heating.commons.entiy.baseInfo.sync.update;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author naming
 * @description
 * @date 2021/6/2 13:11
 **/
@Data
@ApiModel("组织机构同步实体")
public class HeatOrganizationInitDto {

    /**
     * 同步编号
     */
    @ApiModelProperty(value = "同步编号", required = true)
    private Integer num;
    /**
     * 同步父级编号
     */
    @ApiModelProperty(value = "同步父级编号,没有上级则传0", required = true)
    private Integer parentNum;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称", required = true)
    private String name;


    @ApiModelProperty("描述")
    private String description;


    @ApiModelProperty(value = "是否是终节点：1 是  0 否", required = true)
    private Boolean isEnd;


    @ApiModelProperty(value = "层级", required = true)
    private Integer level;

}





