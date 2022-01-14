package com.bmts.heating.commons.entiy.balance.pojo;

import com.bmts.heating.commons.entiy.baseInfo.cache.FirstNetBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author naming
 * @description
 * @date 2021/2/2 17:50
 **/
@Data
public class MembersVo extends FirstNetBase {

    private Integer id;

    private Integer balanceNetId;
    /**
     * 组员id
     */
    private Integer relevanceId;

    /**
     * 级别
     */
    private Integer level;

    /**
     * 是否参与全网平衡
     */
    private Boolean status;

    /**
     * 描述信息
     */
    private String description;
    /**
     * 控制方式 1.阀控 2.泵控 3.二次供回均温
     */
    private int controlType;

    @ApiModelProperty("所属补偿大类")
    private int balanceCompensationId;

    @ApiModelProperty(value = "分类名称")
    private String name ;

    @ApiModelProperty(value = "值")
    private String compensationValue;
}
