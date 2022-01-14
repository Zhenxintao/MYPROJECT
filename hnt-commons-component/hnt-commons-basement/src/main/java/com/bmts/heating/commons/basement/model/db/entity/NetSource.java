package com.bmts.heating.commons.basement.model.db.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("热网-热源")
public class NetSource {

	@ApiModelProperty("热源Id")
	private Integer sourceId;

	@ApiModelProperty("热源名称")
	private String sourceName;

	@ApiModelProperty("热网Id")
	private Integer netId;

	@ApiModelProperty("热网名称")
	private String netName;
}
