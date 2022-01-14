package com.bmts.heating.web.auth.base.dto;


import com.bmts.heating.commons.basement.model.db.entity.SysUser;
import lombok.Data;

import java.util.List;

@Data
public class UserWithRoleDto extends SysUser {
    private List<Integer> roles;
}
