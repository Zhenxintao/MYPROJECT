package com.bmts.heating.commons.basement.model.db.response;

import com.bmts.heating.commons.basement.model.db.entity.HeatSystem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName HeatCabinetResponse
 * @Author pxf
 * @Date 2021/1/6 11:45
 **/
@Data
public class HeatCabinetResponse {

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

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("用户id")
    private Integer userId;


    @ApiModelProperty("机组数量")
    private Integer heatSystemCount;

    @ApiModelProperty("机组实体类")
    private List<HeatSystem> listSystem;

}
