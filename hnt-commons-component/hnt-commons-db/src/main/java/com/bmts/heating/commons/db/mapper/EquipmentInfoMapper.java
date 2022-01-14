package com.bmts.heating.commons.db.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.bmts.heating.commons.basement.model.db.entity.EquipmentInfo;
import com.bmts.heating.commons.basement.model.db.response.EquipmentInfoResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zxt
 * @since 2021-07-20
 */
public interface EquipmentInfoMapper extends BaseMapper<EquipmentInfo> {
    @Select("SELECT e.id as equipmentInfoId,e.equipmentName,e.equipmentCode,p.id as pointStandardId,s.id as equipmentInfoPointStandardId,p.* FROM equipmentInfo e LEFT JOIN equipmentInfoPointStandard s on e.id = s.equipmentInfoId LEFT JOIN pointStandard p on s.pointStandardId = p.id ${ew.customSqlSegment}")
    List<EquipmentInfoResponse> EquipmentInfoResponse(@Param(Constants.WRAPPER) Wrapper wrapper);
}
