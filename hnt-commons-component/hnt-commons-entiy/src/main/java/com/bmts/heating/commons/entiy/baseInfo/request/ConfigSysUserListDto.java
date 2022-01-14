package com.bmts.heating.commons.entiy.baseInfo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("自定义列表")
@Data
public class ConfigSysUserListDto {

    @ApiModelProperty("id")
    private int id;

    @ApiModelProperty("用户id")
    private int userId;

    @ApiModelProperty("自定义列表名称")
    private String name;

    @ApiModelProperty("是否默认")
    private Boolean isDefault;

    // 站点集合
    @ApiModelProperty("站点集合")
    private List<Integer> staionIds;

    // 组织机构集合
    @ApiModelProperty("组织机构Id 集合")
    private List<Integer> heatOrganizationIds;

    // 参量
    @ApiModelProperty("参量标准点表Id")
    private List<Integer> pointStandardIds;

    // 分类查询
    @ApiModelProperty("分类查询")
    private String typeJson;

}
