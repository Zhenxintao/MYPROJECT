package com.bmts.heating.commons.basement.model.db.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@TableName("alarmStorage")
@Accessors(chain = true)
@ApiModel("告警信息")
public class AlarmStorage implements Serializable {

    @ApiModelProperty("自增编号")
    @TableId(value = "id", type = IdType.AUTO)
    private  Integer id;

    @ApiModelProperty("户号")
    @TableField("sysId")
    private  String sysId;

    @ApiModelProperty("户表或NB表等类型")
    private  String type;

    @ApiModelProperty("值信息；可空")
    private  String value;

    @ApiModelProperty("告警信息")
    private  String description;
}
