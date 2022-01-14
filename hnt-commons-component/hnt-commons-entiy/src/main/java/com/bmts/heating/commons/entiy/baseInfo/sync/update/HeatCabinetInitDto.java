package com.bmts.heating.commons.entiy.baseInfo.sync.update;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author naming
 * @description
 * @date 2021/6/2 13:37
 **/
@ApiModel("控制柜同步实体")
@Data
public class HeatCabinetInitDto {

    /**
     * 同步编号
     */
    @ApiModelProperty(value = "同步编号", required = true)
    private Integer num;

    /**
     * 同步父级编号
     */
    @ApiModelProperty(value = "同步父级编号", required = true)
    private Integer parentNum;


    @ApiModelProperty(value = "归属 1.热力站 2.热源 3.热网", required = true)
    private Integer type;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称", required = true)
    private String name;

    /**
     * ip地址
     */
    @ApiModelProperty(value = "ip地址")
    private String ipAddress;

    /**
     * 端口号
     */
    @ApiModelProperty(value = "端口号")
    private Integer port;

    /**
     * 通讯服务器ip
     */
    @ApiModelProperty(value = "通讯服务器ip")
    private String serverIp;

    /**
     * 通讯服务器端口
     */
    @ApiModelProperty(value = "通讯服务器端口")
    private Integer serverPort;

    /**
     * 传输卡号
     */
    @ApiModelProperty(value = "传输卡号")
    private String transferCard;

    /**
     * 通信协议:1.天时PLC 2.天时474 3.天时448
     */
    @ApiModelProperty("通信协议:1.天时 2.其他")
    private Integer communicationProtocol;

    /**
     * 通讯方式: 1.ADSL 2.光纤传输 3.无线传输 4.GPRS 5.CDMA 6.3G猫
     */
    @ApiModelProperty("通讯方式: 1.光纤 2.ADSL 3.无线4G 4.无线DTU 5.其他")
    private Integer communicationWay;

}
