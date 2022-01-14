package com.bmts.heating.commons.basement.model.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("energy_device")
@ApiModel("能耗设备实体类")
public class EnergyDevice implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId(value = "Id", type = IdType.AUTO)
	private Integer Id;

	@TableField("name")
	private String name;

	@TableField("relevanceId")
	private Integer relevanceId;

	@TableField("level")
	private Integer level;

	@TableField("type")
	private Integer type;

	@TableField("replaceTime")
	private LocalDateTime replaceTime;

	@TableField("replaceUser")
	private String replaceUser;

	@TableField("createTime")
	private LocalDateTime createTime;

	@TableField("updateTime")
	private LocalDateTime updateTime;

	@TableField("status")
	private Integer status;

}
