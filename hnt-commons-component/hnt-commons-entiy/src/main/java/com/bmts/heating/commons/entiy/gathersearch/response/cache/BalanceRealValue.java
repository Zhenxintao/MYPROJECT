package com.bmts.heating.commons.entiy.gathersearch.response.cache;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@ApiModel("全网平衡列表响应类")
@Data
public class BalanceRealValue {

	@ApiModelProperty("公司")
	private String companyName;
	@ApiModelProperty("公司")
	private Integer companyId;
	@ApiModelProperty("站点")
	private String stationName;
	@ApiModelProperty("站点id")
	private int stationId;
	@ApiModelProperty("系统id")
	private int heatSystemId;
	@ApiModelProperty("控制柜")
	private String cabinetName;
	@ApiModelProperty("系统")
	private String systemName;
	@ApiModelProperty("质量戳")
	private int qualityStrap;

	@ApiModelProperty("热网供热面积")
	private BigDecimal heatNetArea;
	@ApiModelProperty("热源供热面积")
	private BigDecimal heatSourceArea;
	@ApiModelProperty("换热站在网面积")
	private BigDecimal heatStationNetArea;
	@ApiModelProperty("系统供热面积")
	private BigDecimal heatSystemArea;
	@ApiModelProperty("换热站供热面积")
	private BigDecimal heatStationArea;

	@ApiModelProperty("时间")
	private LocalDateTime timeStamp;
	@ApiModelProperty("是否参与全网平衡计算")
	private Boolean status;
	@ApiModelProperty("补偿值")
	private BigDecimal compensation;

	@ApiModelProperty("点明细")
	private List<PointDetail> pointDetailList;
}
