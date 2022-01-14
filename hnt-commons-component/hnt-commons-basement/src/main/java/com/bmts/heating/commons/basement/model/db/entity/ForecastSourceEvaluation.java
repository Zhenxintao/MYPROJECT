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

@Data
@TableName("forecast_source_evaluation")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ForecastSourceEvaluation implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("maxExtent")
    private BigDecimal maxExtent;
    @TableField("minExtent")
    private BigDecimal minExtent;
    @TableField("evaluationIndex")
    private String evaluationIndex;
}
