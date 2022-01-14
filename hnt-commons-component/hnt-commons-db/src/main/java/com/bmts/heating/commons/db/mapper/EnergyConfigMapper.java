package com.bmts.heating.commons.db.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.bmts.heating.commons.basement.model.db.entity.EnergyConfig;
import com.bmts.heating.commons.basement.model.db.response.EnergyConfigResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface EnergyConfigMapper extends BaseMapper<EnergyConfig> {

	@Select("select ec.* , ps.name PointStandardName,ps.columnName " +
			"from energy_config ec left join pointStandard ps on ec.pointStandardId = ps.id ${ew.customSqlSegment}")
	IPage<EnergyConfigResponse> page(IPage page, @Param(Constants.WRAPPER) Wrapper wrapper);

	@Select("select * from energy_config ec left join pointStandard ps on ec.pointStandardId = ps.id ${ew.customSqlSegment}")
	List<EnergyConfigResponse> queryConfig(@Param(Constants.WRAPPER) Wrapper wrapper);
}
