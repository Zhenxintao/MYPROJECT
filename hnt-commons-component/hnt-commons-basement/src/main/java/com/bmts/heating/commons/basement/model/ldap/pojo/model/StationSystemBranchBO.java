package com.bmts.heating.commons.basement.model.ldap.pojo.model;

import lombok.Data;

/**
 * @Author: pxf
 * @Description: 系统分支
 * @Date: Create in 2020/9/24 11:35
 * @Modified by
 */
@Data
public class StationSystemBranchBO {
    private String id;
    /**
     * 名称和描述
     */
    private String description;
}
