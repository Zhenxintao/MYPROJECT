package com.bmts.heating.commons.basement.model.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.models.auth.In;
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
@TableName("web_point_search_unit")
@ApiModel("配置页面信息点位")
public class WebPointSearchUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 单位外键
     */
    @TableField("pointUnitId")
    private Integer pointUnitId;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 页面key
     */
    @TableField("pageKey")
    private String pageKey;


}
