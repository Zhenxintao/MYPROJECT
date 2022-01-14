package com.bmts.heating.commons.entiy.baseInfo.request.copy;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName CopySourceDto
 * @Author naming
 * @Date 2020/11/19 20:06
 **/
@Data
@ApiModel("复制站参数详情")
public class CopySourceDto {
	@ApiModelProperty(value = "控制柜id")
	private int heatCabinetId;
	@ApiModelProperty(value = "控制柜名称")
	private String heatCabinetName;
	@ApiModelProperty(value = "系统id 数组")
	private List<Integer> heatSystemIds;

}
