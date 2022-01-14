package com.bmts.heating.web.auth.base.dto;

import com.bmts.heating.commons.basement.model.db.entity.SysUserGroup;
import lombok.Data;

import java.util.List;

@Data
public class SysUserGroupPojo  extends SysUserGroup {
    private List<Integer> userIds;
}
