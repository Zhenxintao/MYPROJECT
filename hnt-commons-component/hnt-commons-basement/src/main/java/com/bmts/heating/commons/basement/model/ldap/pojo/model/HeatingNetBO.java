package com.bmts.heating.commons.basement.model.ldap.pojo.model;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @Author: pxf
 * @Description: 热网
 * @Date: Create in 2020/9/25 9:45
 * @Modified by
 */
@Data
public class HeatingNetBO {
    private String id;
    /**
     * 名称和描述
     */
    private String description;
    /**
     * 热网地址
     */
    private String address;
    /**
     * 所属组织 id
     */
    private String orgId;
    /**
     * 建站日期
     */
    private String createTime;
    /**
     * 热网编号
     */
    private Integer code;
    /**
     * 供热面积
     */
    private BigDecimal heatingArea;
    /**
     * 在网面积
     */
    private BigDecimal netArea;
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
     * 热网状态
     */
    private String status;
    /**
     * 热网类型
     */
    private String type;
    /**
     * 热网IP地址
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
     * 出厂管径
     */
    private Integer caliber;
    /**
     * 协议类型
     */
    private String protocolType;

}
