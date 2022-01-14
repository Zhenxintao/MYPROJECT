package com.bmts.heating.commons.entiy.baseInfo.request.monitor;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class MonitorDto extends BaseDto {
    //建筑类型
    @ApiModelProperty("建筑类型")
    private List<Integer> buildType;
    //保温结构
    @ApiModelProperty("保温结构")
    private List<Integer> insulationConstruction;
    //支付方式
    @ApiModelProperty("支付方式")
    private List<Integer> payType;
    //供热方式
    @ApiModelProperty("供热方式")
    private List<Integer> heatType;
    //管理方式
    @ApiModelProperty("管理方式")
    private List<Integer> manageType;
    //站点类型
    @ApiModelProperty("站点类型")
    private List<Integer> StationType;
    //站点类型
    @ApiModelProperty("选中换热站")
    private List<Integer> stationIds;
    //分公司
    @ApiModelProperty("组织架构Id")
    private List<Integer> organizationId;

    @ApiModelProperty("热源Id")
    private List<Integer> heatSourceId;

    @ApiModelProperty("关联参量")
    private String[] columnName;

    @ApiModelProperty("热力站状态:true 正常，false 冻结")
    private Boolean status;

    @ApiModelProperty("是否导出:默认为FALSE")
    private Boolean export = false;

}
