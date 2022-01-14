package com.bmts.heating.web.auth.base.dto;

import com.bmts.heating.commons.auth.entity.DataPerm;
import lombok.Data;

import java.util.List;

@Data
public class RolePermissionsDto {
    private Integer id;
    private String roleName;
    private String roleCode;
    private String roleDes;
    private List<Integer> perms;
    private List<DataPerm> orgs;
    private List<Integer> relevanceIds;
}
