package com.bmts.heating.commons.db.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.bmts.heating.commons.basement.model.db.entity.EnergyInitialCodeConfig;
import com.bmts.heating.commons.basement.model.db.response.energy.EnergyInitialCodeConfigResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface EnergyInitialCodeConfigMapper extends BaseMapper<EnergyInitialCodeConfig> {

	@Select("SELECT hts.id parentId,hts.name,chs.heatYear,eic.* FROM heatTransferStation hts INNER JOIN energy_initial_code_config eic ON hts.id = eic.targetId " +
			" INNER JOIN commonHeatSeason chs ON chs.id = eic.commonSeasonId" +
			" ${ew.customSqlSegment}")
	IPage<EnergyInitialCodeConfigResponse> pageStation(IPage page, @Param(Constants.WRAPPER) Wrapper wrapper);


	@Select("SELECT hs.id parentId,hs.name,chs.heatYear,eic.* FROM heatSource hs INNER JOIN energy_initial_code_config eic ON hs.id = eic.targetId " +
			" INNER JOIN commonHeatSeason chs ON chs.id = eic.commonSeasonId " +
			" ${ew.customSqlSegment}")
	IPage<EnergyInitialCodeConfigResponse> pageSource(IPage page, @Param(Constants.WRAPPER) Wrapper wrapper);

}
