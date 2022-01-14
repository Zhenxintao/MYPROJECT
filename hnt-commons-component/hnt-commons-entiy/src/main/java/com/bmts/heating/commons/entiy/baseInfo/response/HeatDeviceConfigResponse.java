package com.bmts.heating.commons.entiy.baseInfo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName: HeatDeviceConfigResponse
 * @Description: 关联设备响应类
 * @Author: pxf
 * @Date: 2020/12/11 15:55
 * @Version: 1.0
 */

@Data
@ApiModel("关联设备响应类")
public class HeatDeviceConfigResponse {

    @ApiModelProperty("主键id")
    private Integer id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("编号")
    private String code;

    @ApiModelProperty("所属设备节点")
    private String nodeCode;

    /**
     * 关联 设备表 id
     */
    @ApiModelProperty("设备表 id ")
    private Integer deviceConfigId;

    /**
     * 关联id ： 所属换热站id 或热源id
     */
    @ApiModelProperty("关联id ： 所属换热站id 或热源id")
    private Integer relevanceId;

    /**
     * 类型：1、所属换热站    2、所属热源
     */
    @ApiModelProperty("类型：1、所属换热站    2、所属热源")
    private Integer type;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;


}
