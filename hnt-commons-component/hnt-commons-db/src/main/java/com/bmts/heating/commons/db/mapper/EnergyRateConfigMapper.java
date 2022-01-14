package com.bmts.heating.commons.db.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.bmts.heating.commons.basement.model.db.entity.EnergyRateConfig;
import com.bmts.heating.commons.basement.model.db.response.energy.EnergyRateConfigResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface EnergyRateConfigMapper extends BaseMapper<EnergyRateConfig> {

	@Select("SELECT hts.id parentId,hts.name,dic.name chargeType,erc.* FROM heatTransferStation hts INNER JOIN energy_rate_config erc ON hts.id = erc.targetId " +
			" INNER JOIN dic ON dic.id = erc.chargeTypeId" +
			" ${ew.customSqlSegment}")
	IPage<EnergyRateConfigResponse> pageStation(IPage page, @Param(Constants.WRAPPER) Wrapper wrapper);


	@Select("SELECT hs.id parentId,hs.name,dic.name chargeType,erc.* FROM heatSource hs INNER JOIN energy_rate_config erc ON hs.id = erc.targetId " +
			" INNER JOIN dic ON dic.id = erc.chargeTypeId " +
			" ${ew.customSqlSegment}")
	IPage<EnergyRateConfigResponse> pageSource(IPage page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
