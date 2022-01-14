package com.bmts.heating.commons.entiy.baseInfo.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
 * @since 2020-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("控制柜")
public class HeatCabinet implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * ip地址
     */
    @ApiModelProperty("ip地址")
    private String ipAddress;

    /**
     * 端口号
     */
    @ApiModelProperty("端口号")
    private Integer port;

    /**
     * 通讯服务器ip
     */
    @ApiModelProperty("通讯服务器ip")
    private String serverIp;

    /**
     * 通讯服务器端口
     */
    @ApiModelProperty("通讯服务器端口")
    private Integer serverPort;

    /**
     * 传输卡号
     */
    @ApiModelProperty("传输卡号")
    private String transferCard;

    /**
     * 控制器编码
     */
    @ApiModelProperty("控制器编码")
    private String controlCoding;

    /**
     * 通信协议:1.天时PLC 2.天时474 3.天时448
     */
    @ApiModelProperty("信协议:1.天时PLC 2.天时474 3.天时448")
    private Integer communicationProtocol;

    /**
     * 通讯方式: 1.ADSL 2.光纤传输 3.无线传输 4.GPRS 5.CDMA 6.3G猫
     */
    @ApiModelProperty("通讯方式: 1.ADSL 2.光纤传输 3.无线传输 4.GPRS 5.CDMA 6.3G猫")
    private Integer communicationWay;

    /**
     * 所属换热站
     */
    @ApiModelProperty("所属换热站")
    private Integer heatTransferStationId;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty(" 修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("描述")
    private String description;

    /**
     * 删除人
     */
    @ApiModelProperty("删除人")
    private String deleteUser;

    /**
     * 删除时间
     */
    @ApiModelProperty("删除时间")
    private LocalDateTime deleteTime;
    /**
     * 删除标识
     */
    @ApiModelProperty("删除标识")
    private Boolean deleteFlag;

    private Integer id;

    @ApiModelProperty("用户id")
    private Integer userId;


}
