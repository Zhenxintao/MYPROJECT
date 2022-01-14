package com.bmts.heating.commons.basement.model.ldap.pojo.model;

import lombok.Data;

/**
 * @Author: pxf
 * @Description: 总公司
 * @Date: Create in 2020/9/24 11:27
 * @Modified by
 */
@Data
public class ParentCompanyBO {
    private String id;
    /**
     * 名称和描述
     */
    private String description;
    /**
     * 公司状态
     */
    private Integer status;

}
