package com.bmts.heating.commons.entiy.baseInfo.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author naming
 * @since 2020-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("模板管理")
public class PointTemplateConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模板key 自定义匹配规则 例如：Station:PointCollect:1  以冒号分隔，以后切换到redis也方便存储
     */
    @ApiModelProperty("模板key 自定义匹配规则 例如：Station:PointCollect:1  以冒号分隔，以后切换到redis也方便存储")
    private String templateKey;

    @ApiModelProperty("预留 例如源需要模板存储站也需要 则用此字段做区分 具体查哪一条用key的匹配规则,2:控制量，3:采集量")
    private Integer type;

    /**
     * 模板value json形式存储
     */
    @ApiModelProperty("模板value json形式存储")
    private String templateValue;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;
    @ApiModelProperty("描述")
    private String description;

    /**
     * 删除人
     */
    @ApiModelProperty("删除人")
    private String deleteUser;

    /**
     * 删除时间
     */
    @ApiModelProperty("删除时间")
    private LocalDateTime deleteTime;
    /**
     * 删除标识
     */
    @ApiModelProperty("删除标识")
    private Boolean deleteFlag;

    private Integer id;

    @ApiModelProperty("用户id")
    private Integer userId;

    /**
     * true 正常，false 冻结
     */
    @ApiModelProperty("true 正常，false 冻结")
    private Boolean status;

}
