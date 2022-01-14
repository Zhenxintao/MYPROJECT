package com.bmts.heating.commons.basement.model.ldap.pojo.model;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @Author: pxf
 * @Description: 单元
 * @Date: Create in 2020/9/24 11:27
 * @Modified by
 */
@Data
public class UnitBO {
    private String id;
    /**
     * 名称和描述
     */
    private String description;

    /**
     * 单元编号
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
     * 协议类型
     */
    private String protocolType;

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
