package com.bmts.heating.commons.basement.model.ldap.pojo.model;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @Author: pxf
 * @Description: 控制柜 dtu
 * @Date: Create in 2020/9/24 11:31
 * @Modified by
 */
@Data
public class ControlHousingBO {
    private String id;
    /**
     * 名称和描述
     */
    private String description;
    /**
     * 控制柜地址
     */
    private String address;
    /**
     * 所属公司组织 id
     */
    private String orgId;
    /**
     * 所属热源 id
     */
    private String heatingSourceId;
    /**
     * 建站日期
     */
    private String createTime;
    /**
     * 改造日期
     */
    private String updateTime;
    /**
     * 控制柜编号
     */
    private Integer code;


    /**
     * 经度坐标
     */
    private BigDecimal longitude;
    /**
     * 纬度坐标
     */
    private BigDecimal latitude;
    /**
     * 负责人名称
     */
    private String contactsName;
    /**
     * 负责人电话
     */
    private String contactsPhone;
    /**
     * 控制柜状态
     */
    private String status;

    /**
     * 站点IP地址
     */
    private String ip;
    /**
     * 端口号
     */
    private Integer port;
    /**
     * 通讯服务器IP
     */
    private String connectIp;
    /**
     * 通讯方式
     */
    private String connectMode;
    /**
     * 传输卡号
     */
    private BigInteger connectCard;

    /**
     * 保温结构
     */
    private String warmType;

    /**
     * 管理方式
     */
    private String manageType;


    /**
     * 管路布置
     */
    private String layoutType;
    /**
     * 控制器编码
     */
    private Integer controlsCode;
    /**
     * 协议类型
     */
    private String protocolType;

    /**
     * 管道管径
     */
    private Integer caliber;
    /**
     * 是否自动发送命令
     */
    private String autoSend;
    /**
     * 调节比例
     */
    private String adjustRatio;
    /**
     * DTU地势
     */
    private String terrain;
    /**
     * 离热源距离
     */
    private String distance;

}
