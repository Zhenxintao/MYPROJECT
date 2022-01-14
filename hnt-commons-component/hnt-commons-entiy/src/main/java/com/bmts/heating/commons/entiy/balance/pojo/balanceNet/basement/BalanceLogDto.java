package com.bmts.heating.commons.entiy.balance.pojo.balanceNet.basement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@ApiModel("全网平衡日志记录实体类")
@Data
public class BalanceLogDto {
    @ApiModelProperty("全网平衡Id")
    private Integer id;
    /**
     * 操作人名称
     */
    @ApiModelProperty("操作人名称")
    private String userName;

    /**
     * 操作时间
     */
    @ApiModelProperty("操作时间")
    private LocalDateTime createTime;

    /**
     * 操作者IP
     */
    @ApiModelProperty("ip地址")
    private String ipAddress;

    /**
     * 设备名称
     */
    @ApiModelProperty("操作模块")
    private String module;


    @ApiModelProperty("操作按钮")
    private String button;

    /**
     * 操作模式
     */
    @ApiModelProperty("操作描述头，例如：(添加:xxx)")
    private String descriptionHead;

    /**
     * 操作模式
     */
    @ApiModelProperty("操作描述尾，例如：(xxx:已成功！)")
    private String descriptionTrail;

    /**
     * 配置系统挂网
     */
    @ApiModelProperty("配置系统集合挂网")
    private String ids;
}
