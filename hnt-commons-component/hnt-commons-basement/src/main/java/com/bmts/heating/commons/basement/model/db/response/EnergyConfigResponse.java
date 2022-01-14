package com.bmts.heating.commons.basement.model.db.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class EnergyConfigResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	@ApiModelProperty("能耗类型1:热 2:电 3:水")
	private Integer type;

	@ApiModelProperty("是否参与汇聚")
	private Boolean isConverge;

	@ApiModelProperty("汇聚方式")
	private Boolean convergeId;

	@ApiModelProperty("关联参量Id")
	private Integer pointStandardId;

	@ApiModelProperty("参量名称")
	private String pointStandardName;

	@ApiModelProperty("标签名称")
	private String columnName;


}
