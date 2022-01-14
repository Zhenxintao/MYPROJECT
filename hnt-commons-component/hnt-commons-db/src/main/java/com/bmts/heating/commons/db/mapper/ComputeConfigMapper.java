package com.bmts.heating.commons.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bmts.heating.commons.basement.model.db.entity.ComputeConfig;
import com.bmts.heating.commons.basement.model.db.response.ComputeConfigResponse;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ComputeConfigMapper extends BaseMapper<ComputeConfig> {


	@Select("SELECT ps.id pointStandardId,ps.name,ps.columnName,cc.id id FROM pointStandard ps INNER JOIN compute_config cc ON ps.id = cc.pointstandardid WHERE cc.computeId = #{computeId}")
	List<ComputeConfigResponse> getPointStandardByComputeConfigId(Integer computeId);
}
