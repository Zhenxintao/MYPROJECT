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


/**
 * <p>
 *
 * </p>
 *
 * @author zxt
 * @since 2021-06-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("serviceStation")
@ApiModel("平台换热站id与客服收费换热站id关系实体")
public class ServiceStation {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 平台换热站Id
     */
    @ApiModelProperty("平台换热站Id")
    @TableField("heatStationId")
    private String  heatStationId;

    /**
     * 客服收费对应换热站Id
     */
    @ApiModelProperty("客服收费对应换热站Id")
    @TableField("serviceStationId")
    private String  serviceStationId;

}
