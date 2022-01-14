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
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class EsDocConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 列名
     */
    @ApiModelProperty("列名")
    @TableField("pointName")
    private String pointName;

    /**
     * 数据类型
     */
    @ApiModelProperty("数据类型")
    @TableField("dataType")
    private Integer dataType;

    /**
     * 是否参与汇聚
     */
    @ApiModelProperty("是否参与汇聚")
    @TableField("isConverge")
    private Boolean isConverge;

    /**
     * 汇聚类型小时
     */
    @ApiModelProperty("汇聚类型-小时")
    @TableField("convergeTypeHour")
    private Integer convergeTypeHour;

    /**
     * 汇聚类型天
     */
    @ApiModelProperty("汇聚类型-天")
    @TableField("convergeTypeDay")
    private Integer convergeTypeDay;


    /**
     * 数据类型名称
     */
    @ApiModelProperty("数据类型名称")
    @TableField(exist = false)
    private String dataTypeName;


    public void setDataType(Integer dataType) {
        if (dataType != null) {
            this.dataTypeName = DataType.getValue(dataType);
        }
        this.dataType = dataType;
    }

}
