package com.bmts.heating.commons.db.mapper;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.EsDocConfig;
import com.bmts.heating.commons.basement.model.db.entity.EsDocConfigResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author naming
 * @since 2021-01-12
 */
public interface EsDocConfigMapper extends BaseMapper<EsDocConfig> {

	@Select("SELECT *,ps.name pointStandardName FROM es_doc_config es left join pointStandard ps on es.pointName = ps.columnName ${ew.customSqlSegment}")
	Page<EsDocConfigResponse> page(Page<EsDocConfigResponse> page, @Param(Constants.WRAPPER) Wrapper wrapper);

}
