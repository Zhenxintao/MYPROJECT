package com.bmts.heating.commons.entiy.baseInfo.request;

import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ApiModel("换热站查询类")
@Data
public class HeatTransferStationDto extends BaseDto {

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

	@ApiModelProperty("组织架构Id")
	private Integer organizationId;

	@ApiModelProperty("热源Id")
	private Integer heatSourceId;

	@ApiModelProperty("状态:true 正常，false 冻结")
	private Boolean status;
}
