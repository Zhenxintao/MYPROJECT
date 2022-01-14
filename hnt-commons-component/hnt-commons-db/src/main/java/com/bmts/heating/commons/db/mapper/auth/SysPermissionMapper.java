package com.bmts.heating.commons.db.mapper.auth;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.bmts.heating.commons.basement.model.db.entity.SysDataPerm;
import com.bmts.heating.commons.basement.model.db.entity.SysPermission;
import com.bmts.heating.commons.basement.model.db.response.SysDataPermResponse;
import com.bmts.heating.commons.entiy.auth.response.SysPermissionRoleResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author naming
 * @since 2020-07-27
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {
    @Select("SELECT sp.*,sr.id roleId,dp.checked FROM sys_role_permission srp \n" +
            "            LEFT JOIN sys_role sr ON srp.role_id=sr.id\n" +
            "            LEFT JOIN sys_user_role sur ON sur.role_id=sr.id\n" +
            "            LEFT JOIN sys_user su ON su.id=sur.user_id \n" +
            "            INNER JOIN sys_permission sp ON sp.id=srp.permission_id " +
            "            LEFT JOIN sys_data_perm dp ON sr.id=dp.roleId ${ew.customSqlSegment}")
    List<SysPermissionRoleResponse> selectPermAndMenuByUser(@Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("SELECT sp.*,sr.id roleId FROM sys_role_permission srp \n" +
            "            LEFT JOIN sys_role sr ON srp.role_id=sr.id\n" +
            "            LEFT JOIN sys_user_role sur ON sur.role_id=sr.id\n" +
            "            LEFT JOIN sys_user su ON su.id=sur.user_id \n" +
            "            INNER JOIN sys_permission sp ON sp.id=srp.permission_id " +
            "            ${ew.customSqlSegment}")
    List<SysPermission> selectPermsByUser(@Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("SELECT sp.* FROM sys_permission sp LEFT JOIN sys_role_permission srp ON sp.id=srp.permission_id WHERE " +
            "srp.role_id=#{role_id}")
    List<SysPermission> selectPermsByRole(@Param("role_id") int role_id);

    @Select("SELECT * FROM sys_user su LEFT JOIN sys_user_role sur ON sur.user_id=su.id \n" +
            "LEFT JOIN sys_role sr ON sr.id=sur.role_id\n" +
            "LEFT JOIN sys_data_perm sdp ON sr.id=sdp.roleId ${ew.customSqlSegment}")
    List<SysDataPermResponse> selectDataPerm(@Param(Constants.WRAPPER) Wrapper wrapper);
}
