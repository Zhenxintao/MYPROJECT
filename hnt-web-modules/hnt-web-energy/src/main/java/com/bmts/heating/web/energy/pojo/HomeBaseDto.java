package com.bmts.heating.web.energy.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("首页基础数据dto")
@Data
public class HomeBaseDto {

	@ApiModelProperty("公司、热网-id")
	private Integer id;
	@ApiModelProperty("1:公司 2:热网")
	private Integer type;

}
