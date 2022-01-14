package com.bmts.heating.commons.basement.second.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @since 2021-01-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("iot_pointConfigSecond")
public class PointConfigSecond implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * '点类型 1.真实点 2.虚拟点  3.配置点'
     */
    @TableField("pointType")
    private Integer pointType;

    /**
     * 点位数据类型 : string double bool int
     */
    @TableField("dataType")
    private String dataType;

    /**
     * 设备id
     */
    @TableField("deviceId")
    private Integer deviceId;

    /**
     * 等级
     */
    @TableField("pointLevel")
    private Integer pointLevel;

    /**
     * 点名称
     */
    @TableField("pointName")
    private String pointName;

    /**
     * 注释
     */
    @TableField("memo")
    private String memo;

    /**
     * 创建人编号 当前用户ID
     */
    @TableField("creatorId")
    private Integer creatorId;
    /**
     * 创建人姓名
     */
    @TableField("creatorName")
    private String creatorName;
    /**
     * 创建日期 默认为当前时间
     */
    @TableField("createDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createDate;

    /**
     * 修改时间
     */
    @TableField("updateTime")
    private LocalDateTime updateTime;

    /**
     * 删除标识
     */
    @TableField("isDelete")
    private Boolean isDelete;


}
