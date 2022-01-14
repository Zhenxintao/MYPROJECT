package com.bmts.heating.commons.entiy.baseInfo.request.monitor;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel("sing-参数汇总查询类")
@EqualsAndHashCode(callSuper = true)
@Data
public class MonitorSingleDto extends BaseDto {
	//分公司
	@ApiModelProperty("组织架构Id")
	private Integer organizationId;

	@ApiModelProperty("热源Id")
	private Integer heatSourceId;

	@ApiModelProperty("换热站Id")
	private Integer stationId;

	@ApiModelProperty("关联参量")
	private String[] columnName;

	@ApiModelProperty("是否导出:默认为FALSE")
	private Boolean export = false;
}
