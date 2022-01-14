package com.bmts.heating.commons.entiy.baseInfo.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author naming
 * @since 2021-01-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PointConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关联参量id
     */
    private Integer pointStandardId;

    /**
     * 关联id
     */
    private Integer relevanceId;


    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    private String updateUser;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


    /**
     * 删除标识
     */
    private Boolean deleteFlag;

    /**
     * 删除时间
     */
    private LocalDateTime deleteTime;

    /**
     * 删除人
     */
    private String deleteUser;

    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 默认值1属于系统 2.控制柜 3.站 4.源 5.网
     */
    private Integer level;

    private String pointColumnName;

    @ApiModelProperty("同步编号")
    private String syncNumber;

    @ApiModelProperty("同步父级编号")
    private BigInteger syncParentNum;

}
