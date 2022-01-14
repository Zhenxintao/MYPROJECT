package com.bmts.heating.commons.basement.model.ldap.pojo.vo;

import com.bmts.heating.commons.basement.model.cache.SearchBase;
import lombok.Data;

import java.util.List;

/**
 * @Author: naming
 * @Description: 入参
 * @Date: Create in 2020/9/24 11:45
 * @Modified by
 */
@Data
public class PointConstructionVo extends SearchBase {
    private List<String> StartDn;
    private String EndObjectClassName;
    private List<String> points;
}
