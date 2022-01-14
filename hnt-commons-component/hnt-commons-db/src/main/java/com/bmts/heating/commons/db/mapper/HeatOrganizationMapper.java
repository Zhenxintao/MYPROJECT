package com.bmts.heating.commons.db.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.bmts.heating.commons.basement.model.db.entity.HeatOrganization;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bmts.heating.commons.basement.model.db.response.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author naming
 * @since 2020-11-09
 */
public interface HeatOrganizationMapper extends BaseMapper<HeatOrganization> {
	@Select("select LAST_INSERT_ID()")
	Integer getInsertId();

	@Select("SELECT org.*,station.id stationId,station.name stationName FROM heatOrganization org LEFT JOIN heatTransferStation station ON org.id=station.heatOrganizationId ${ew.customSqlSegment}")
	List<OrgAndStation> queryOrgAndStation(@Param(Constants.WRAPPER) Wrapper wrapper);
	@Select("SELECT ho.* FROM sys_user_organization sug INNER JOIN sys_user su ON su.id=sug.userId INNER JOIN heatOrganization ho ON ho.id=sug.heatOrganizationId ${ew.customSqlSegment}")
	List<HeatOrganization> queryOrgByUser(@Param(Constants.WRAPPER) Wrapper wrapper);

	@Select("select ht.name stationName,ht.id stationId,hs.name systemName,hs.id systemId,ht.heatOrganizationId heatOrganizationId from heatTransferStation ht left join heatCabinet hc on ht.id = hc.heatTransferStationId left join heatSystem hs on hc.id = hs.heatCabinetId ${ew.customSqlSegment}")
	List<OrgAndStationTree> findStationAndSystem(@Param(Constants.WRAPPER) Wrapper wrapper);

	@Select("SELECT ho.id id,ho.pid pid,ho.level level,ho.name name,ht.id stationId,ht.name stationName FROM heatOrganization ho " +
			"left JOIN heatTransferStation ht ON ho.id = ht.heatOrganizationId ${ew.customSqlSegment}")
	List<OrgAndStationTree> queryOrganizationTreeAndStation1(@Param(Constants.WRAPPER) Wrapper wrapper);

	@Select("SELECT ho.id id,ho.pid pid,ho.level level,ho.name name,ht.id stationId,ht.name stationName FROM heatOrganization ho left JOIN heatTransferStation ht ON ho.id = ht.viceOrgId ${ew.customSqlSegment}")
	List<OrgAndStationTree> queryOrganizationTreeAndStation2(@Param(Constants.WRAPPER) Wrapper wrapper);
}
