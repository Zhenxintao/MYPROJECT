package com.bmts.heating.commons.db.mapper.auth;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.bmts.heating.commons.basement.model.db.entity.SysDataRelevance;
import com.bmts.heating.commons.entiy.auth.response.SysPermissionRoleResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zxt
 * @since 2021-10-27
 */
public interface SysDataRelevanceMapper extends BaseMapper<SysDataRelevance> {
    @Select("SELECT r.* FROM sys_user s LEFT JOIN sys_user_role u on s.id = u.user_id  JOIN sys_data_relevance r on u.role_id=r.roleId ${ew.customSqlSegment}")
    List<SysDataRelevance> querySourceRoleInfo(@Param(Constants.WRAPPER) Wrapper wrapper);
}
