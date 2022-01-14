package com.bmts.heating.commons.db.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.bmts.heating.commons.basement.model.db.entity.EnergyPrice;
import com.bmts.heating.commons.basement.model.db.response.energy.EnergyPriceResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface EnergyPriceMapper extends BaseMapper<EnergyPrice> {

	@Select("SELECT dic.name chargeName,chs.heatYear,ep.* FROM energy_price ep INNER JOIN dic ON ep.chargeTypeId = dic.id " +
			" INNER JOIN commonHeatSeason chs ON chs.id = ep.commonSeasonId ${ew.customSqlSegment}")
	IPage<EnergyPriceResponse> page(IPage page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
