package com.bmts.heating.commons.entiy.energy;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

@ApiModel("energy----保存类")
@Data
public class EnergyCollectConfigAddDto {

	/**
	 * jep表达式
	 * p1=p2+p3
	 */
	@ApiModelProperty("jep表达式")
	private String expression;

	/**
	 * map<p1:EnergyPointResponse>
	 * 参量
	 */
	@ApiModelProperty("对应计算参量 P:obj")
	private Map<String,EnergyPointResponse> map;

	public boolean isValid(){
		return (CollectionUtils.isEmpty(map) || StringUtils.isEmpty(expression));
	}
}
