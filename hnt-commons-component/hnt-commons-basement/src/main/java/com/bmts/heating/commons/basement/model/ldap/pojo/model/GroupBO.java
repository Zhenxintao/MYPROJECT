package com.bmts.heating.commons.basement.model.ldap.pojo.model;

import lombok.Data;

/**
 * @Author: pxf
 * @Description: 集团
 * @Date: Create in 2020/9/24 14:34
 * @Modified by
 */
@Data
public class GroupBO {

    private String id;
    /**
     * 名称和描述
     */
    private String description;
    /**
     * 状态
     */
    private Integer status;
}
