package com.bmts.heating.commons.basement.model.db.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;

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
@EqualsAndHashCode()
@Accessors(chain = true)
@TableName("heatCabinet")
@ApiModel("控制柜")
public class HeatCabinet implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    @TableField("name")
    private String name;

    /**
     * ip地址
     */
    @ApiModelProperty("ip地址")
    @TableField(value = "ipAddress", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.INTEGER)
    private String ipAddress;

    /**
     * 端口号
     */
    @ApiModelProperty("端口号")
    @TableField(value = "port", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.INTEGER)
    private Integer port;

    /**
     * 通讯服务器ip
     */
    @ApiModelProperty("通讯服务器ip")
    @TableField(value = "serverIp", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.VARCHAR)
    private String serverIp;

    /**
     * 通讯服务器端口
     */
    @ApiModelProperty("通讯服务器端口")
    @TableField(value = "serverPort", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.VARCHAR)
    private Integer serverPort;

    /**
     * 传输卡号
     */
    @ApiModelProperty("传输卡号")
    @TableField(value = "transferCard", updateStrategy = FieldStrategy.IGNORED, jdbcType = JdbcType.INTEGER)
    private String transferCard;

    /**
     * 控制器编码
     */
    @ApiModelProperty("控制器编码")
    @TableField("controlCoding")
    private String controlCoding;

    /**
     * 通信协议:1.天时PLC 2.天时474 3.天时448
     */
    @ApiModelProperty("信协议:1.天时PLC 2.天时474 3.天时448")
    @TableField("communicationProtocol")
    private Integer communicationProtocol;

    /**
     * 通讯方式: 1.ADSL 2.光纤传输 3.无线传输 4.GPRS 5.CDMA 6.3G猫
     */
    @ApiModelProperty("通讯方式: 1.ADSL 2.光纤传输 3.无线传输 4.GPRS 5.CDMA 6.3G猫")
    @TableField("communicationWay")
    private Integer communicationWay;

    /**
     * 所属换热站
     */
    @ApiModelProperty("所属换热站")
    @TableField("heatTransferStationId")
    private Integer heatTransferStationId;


//    /**
//     * 所属 热网
//     */
//    @ApiModelProperty("所属热网")
//    @TableField("heatNetId")
//    private Integer heatNetId;

    /**
     * 所属 热源
     */
    @ApiModelProperty("所属热源")
    @TableField("heatSourceId")
    private Integer heatSourceId;


    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    @TableField("createUser")
    private String createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @TableField("createTime")
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    @TableField("updateUser")
    private String updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty(" 修改时间")
    @TableField("updateTime")
    private LocalDateTime updateTime;

    @ApiModelProperty("描述")
    @TableField("description")
    private String description;

    /**
     * 删除人
     */
    @ApiModelProperty("删除人")
    @TableField("deleteUser")
    private String deleteUser;

    /**
     * 删除时间
     */
    @ApiModelProperty("删除时间")
    @TableField("deleteTime")
    private LocalDateTime deleteTime;
    /**
     * 删除标识
     */
    @ApiModelProperty("删除标识")
    @TableField("deleteFlag")
    private Boolean deleteFlag;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户id")
    @TableField("userId")
    private Integer userId;

    @ApiModelProperty("同步编号")
    @TableField("syncNumber")
    private Integer syncNumber;

    @ApiModelProperty("同步父级编号")
    @TableField("syncParentNum")
    private Integer syncParentNum;

}
