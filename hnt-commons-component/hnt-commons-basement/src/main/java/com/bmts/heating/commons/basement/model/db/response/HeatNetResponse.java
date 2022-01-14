package com.bmts.heating.commons.basement.model.db.response;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ApiModel("热网列表响应类")
@Data
public class HeatNetResponse {
    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 简拼
     */
    @ApiModelProperty("简拼")
    private String logogram;

    /**
     * 编号
     */
    @ApiModelProperty("编号")
    private String code;

    /**
     * 地址
     */
    @ApiModelProperty("地址")
    private String address;

    /**
     * 热网标识:1.虚拟 2.真实
     */
    @ApiModelProperty("热网标识:1.虚拟 2.真实")
    private Integer flag;

    /**
     * 热网类型： 1.环网 2.分网
     */
    @ApiModelProperty("热网类型： 1.环网 2.分网")
    private Integer type;

    @ApiModelProperty("供热面积")
    private BigDecimal heatArea;
    @ApiModelProperty("所属公司")
    private int heatOrganizationId;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    @TableField("createUser")
    private String createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @TableField("createTime")
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    @TableField("updateUser")
    private String updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    @TableField("updateTime")
    private LocalDateTime updateTime;
    @ApiModelProperty("描述")
    private String description;
    private Integer id;
    @ApiModelProperty("所属公司")
    private String heatOrganizationName;
}
