package com.bmts.heating.commons.entiy.gathersearch.response.cache;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@ApiModel("源-实时数据列表响应类")
@Data
public class SourceCacheRealValue {
	@ApiModelProperty("公司")
	private String companyName;
	@ApiModelProperty("热源")
	private String heatSourceName;
	@ApiModelProperty("热源id")
	private int heatSourceId;
	@ApiModelProperty("系统id")
	private int heatSystemId;
	@ApiModelProperty("控制柜")
	private String cabinetName;
	@ApiModelProperty("系统")
	private String systemName;
	@ApiModelProperty("质量戳")
	private int qualityStrap;
	@ApiModelProperty("面积")
	private BigDecimal area;
	@ApiModelProperty("经度")
	private BigDecimal longitude;
	@ApiModelProperty("纬度")
	private BigDecimal latitude;
	@ApiModelProperty("点明细")
	private List<PointDetail> pointDetailList;
}
