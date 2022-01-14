package com.bmts.heating.commons.db.mapper.auth;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bmts.heating.commons.basement.model.db.entity.SysRole;
import com.bmts.heating.commons.basement.model.db.entity.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liangcy
 * @since 2020-07-27
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
    @Select("SELECT sr.* FROM sys_user_role sur LEFT JOIN sys_role sr " +
            "ON sur.role_id=sr.id WHERE" +
            " sur.user_id=#{user_id}")
    List<SysRole> queryRoleByUser(@Param("user_id") int user_id);
    @Select("SELECT su.* FROM sys_user_role sur LEFT JOIN sys_user su ON sur.user_id=su.id WHERE sur" +
            ".role_id=#{role_id}")
    List<SysUser> queryUsersByRole(@Param("role_id") int role_id);
}
