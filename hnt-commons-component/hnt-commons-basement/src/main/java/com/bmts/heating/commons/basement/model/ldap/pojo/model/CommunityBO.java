package com.bmts.heating.commons.basement.model.ldap.pojo.model;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @Author: pxf
 * @Description: 小区
 * @Date: Create in 2020/9/24 11:28
 * @Modified by
 */
@Data
public class CommunityBO {
    private String id;
    /**
     * 名称和描述
     */
    private String description;
    /**
     * 小区地址
     */
    private String address;
    /**
     * 小区编号
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
     * 收费方式
     */
    private String chargeType;
    /**
     * 供热方式
     */
    private String heatingType;
    /**
     * 管理方式
     */
    private String manageType;


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
     * 离热源距离
     */
    private String distance;

}
