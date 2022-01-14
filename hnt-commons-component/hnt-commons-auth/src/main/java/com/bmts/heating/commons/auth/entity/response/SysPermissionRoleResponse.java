package com.bmts.heating.commons.auth.entity.response;

import com.bmts.heating.commons.basement.model.db.entity.SysPermission;
import lombok.Data;

@Data
public class SysPermissionRoleResponse extends SysPermission {
    int roleId;
}
