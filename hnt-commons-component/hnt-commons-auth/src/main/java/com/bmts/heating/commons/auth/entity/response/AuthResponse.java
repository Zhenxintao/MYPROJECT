package com.bmts.heating.commons.auth.entity.response;

import com.bmts.heating.commons.basement.model.db.entity.SysPermission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 查询功能及数据权限
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse implements Serializable {
    /**
     * 数据id 站id 或者热源id
     */
    int dataId;
    List<PermsResponse> perms;
}
