package com.bmts.heating.commons.basement.model.ldap.pojo.model;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @Author: pxf
 * @Description: 换热站
 * @Date: Create in 2020/9/24 11:27
 * @Modified by
 */
@Data
public class StationBO {
    private String id;
    /**
     * 名称和描述
     */
    private String description;
    /**
     * 站点地址
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
     * 站点编号
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
     * 站点状态
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
     * 建筑类型
     */
    private String buildType;
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
     * 站点类型
     */
    private String type;
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
     * 是否是重点站
     */
    private String important;
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
     * 热力站地势
     */
    private String terrain;
    /**
     * 离热源距离
     */
    private String distance;
    /**
     * 基础瞬时热量
     */
    private String instantHeat;

}
