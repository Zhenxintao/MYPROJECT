package com.bmts.heating.commons.basement.model.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("页面显示配置")
@EqualsAndHashCode()
@Accessors(chain = true)
@TableName("web_screen_config")
public class WebScreenConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id",type = IdType.AUTO)
	private Integer id;

	@TableField("pageKey")
	private String pageKey;

	@TableField("content")
	private String content;

	@TableField("name")
	private String name;

	@TableField("creatorId")
	private Integer creatorId;

	@TableField("createDate")
	private LocalDateTime createDate;

	@TableField("creatorName")
	private String creatorName;

	@TableField("eventName")
	private String eventName;

	@TableField("action")
	private String action;

	@TableField("pageIndex")
	private String pageIndex;
}
