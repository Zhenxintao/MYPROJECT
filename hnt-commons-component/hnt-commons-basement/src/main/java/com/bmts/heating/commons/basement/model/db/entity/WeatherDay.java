package com.bmts.heating.commons.basement.model.db.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author naming
 * @since 2021-04-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WeatherDay implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 最高气温
     */
    private BigDecimal high;

    /**
     * 最低气温
     */
    private BigDecimal low;

    /**
     * 风力
     */
    private String power;

    /**
     * 天气状况
     */
    private String info;

    /**
     * 平均气温
     */
    @TableField("avgTemp")
    private BigDecimal avgTemp;

    /**
     * 风向
     */
    private String direct;

    /**
     * 预测时间
     */
    @TableField("forecastTime")
    private LocalDateTime forecastTime;

    /**
     * 创建时间
     */
    @TableField("createTime")
    private LocalDateTime createTime;


}
