package com.bmts.heating.commons.basement.model.ldap.pojo.model;

import lombok.Data;

/**
 * @Author: pxf
 * @Description: 设备
 * @Date: Create in 2020/9/24 11:37
 * @Modified by
 */
@Data
public class DeviceInfoBO {
    private String id;
    /**
     * 名称和描述
     */
    private String description;
}
