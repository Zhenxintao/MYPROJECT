package com.bmts.heating.commons.basement.model.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.bmts.heating.commons.basement.model.enums.DataType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author naming
 * @since 2021-01-12
 */
@Data
@EqualsAndHashCode()
@Accessors(chain = true)
public class EsDocConfigResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;

	/**
	 * 列名
	 */
	@ApiModelProperty("列名")
	private String pointName;

	/**
	 * 数据类型
	 */
	@ApiModelProperty("数据类型")
	private Integer dataType;

	/**
	 * 是否参与汇聚
	 */
	@ApiModelProperty("是否参与汇聚")
	private Boolean isConverge;

	/**
	 * 汇聚类型
	 */
	@ApiModelProperty("汇聚类型-天")
	private Integer convergeTypeDay;
	/**
	 * 汇聚类型
	 */
	@ApiModelProperty("汇聚类型-小时")
	private Integer convergeTypeHour;

	@ApiModelProperty("参量名称")
	private String pointStandardName;

	/**
	 * 数据类型名称
	 */
	@ApiModelProperty("数据类型名称")
	private String dataTypeName;


	public void setDataType(Integer dataType) {
		if (dataType != null) {
			this.dataTypeName = DataType.getValue(dataType);
		}
		this.dataType = dataType;
	}

}
