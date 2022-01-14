package com.bmts.heating.commons.db.mapper;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.bmts.heating.commons.basement.model.db.entity.EnergyDevice;
import com.bmts.heating.commons.basement.model.db.response.EnergyDeviceResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface EnergyDeviceMapper extends BaseMapper<EnergyDevice> {

	@Select("SELECT ed.*,sfnb.heatSystemName as heatSysName,sfnb.heatTransferStationName as heatName FROM energy_device ed" +
			" INNER JOIN stationFirstNetBaseView sfnb ON sfnb.heatSystemId = ed.relevanceId ${ew.customSqlSegment} ")
	IPage<EnergyDeviceResponse> getPageStation(IPage<EnergyDevice> page, @Param(Constants.WRAPPER) Wrapper wrapper);

	@Select("SELECT ed.*,sfnb.heatSystemName as heatSysName,sfnb.heatSourceName as heatName FROM energy_device ed" +
			" INNER JOIN sourceFirstNetBaseView sfnb ON sfnb.heatSystemId = ed.relevanceId  ${ew.customSqlSegment} ")
	IPage<EnergyDeviceResponse> getPageSource(IPage<EnergyDevice> page, @Param(Constants.WRAPPER) Wrapper wrapper);

}
