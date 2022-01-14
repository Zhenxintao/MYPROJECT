package com.bmts.heating.commons.basement.model.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author naming
 * @description
 * @date 2021/4/20 15:38
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ForecastSourceCron implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("corn")
    private String corn;
    @TableField("weekCron")
    private String weekCron;

}
