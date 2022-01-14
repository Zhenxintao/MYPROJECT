package com.bmts.heating.commons.basement.model.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author zxt
 * @since 2021-02-25
 */
@Data
@EqualsAndHashCode()
@Accessors(chain = true)
@TableName("web_point_unit_detail")
@ApiModel("配置页面信息点位")
public class WebPointUnitDetail {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 配置单位外键
     */
    @TableField("webPointSearchUnitId")
    private Integer webPointSearchUnitId;

    /**
     * 标准点外键
     */
    @TableField("pointStandardId")
    private Integer pointStandardId;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;
}
