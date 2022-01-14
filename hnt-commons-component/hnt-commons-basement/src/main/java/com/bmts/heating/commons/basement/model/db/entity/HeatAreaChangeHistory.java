package com.bmts.heating.commons.basement.model.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("heatAreaChangeHistory")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class HeatAreaChangeHistory implements Serializable {
    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("oldValue")
    private BigDecimal oldValue;

    @TableField("newValue")
    private BigDecimal newValue;

    @TableField("level")
    private Integer level;

    @TableField("relevanceId")
    private Integer relevanceId;

    @TableField("createTime")
    private LocalDateTime createTime;

    @TableField("status")
    private Boolean status = true;
}
