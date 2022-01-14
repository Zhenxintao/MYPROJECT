package com.bmts.heating.commons.basement.model.ldap.pojo.model;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @Author: pxf
 * @Description: 热源
 * @Date: Create in 2020/9/25 9:46
 * @Modified by
 */
@Data
public class HeatingSourceBO {
    private String id;
    /**
     * 名称和描述
     */
    private String description;
    /**
     * 热源地址
     */
    private String address;
    /**
     * 所属公司组织 id
     */
    private String orgId;
    /**
     * 所属热网 id
     */
    private String heatingNetId;
    /**
     * 建站日期
     */
    private String createTime;
    /**
     * 热源编号
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
     * 热源状态
     */
    private String status;
    /**
     * 热源类型
     */
    private String type;
    /**
     * 是否是重点站
     */
    private String important;
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
