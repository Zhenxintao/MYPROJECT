package com.bmts.heating.commons.db.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.bmts.heating.commons.basement.model.db.entity.PointAlarmView;
import com.bmts.heating.commons.basement.model.db.entity.PointStandardAlarmView;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * VIEW Mapper 接口
 * </p>
 *
 * @author naming
 * @since 2021-04-28
 */
public interface PointStandardAlarmViewMapper extends BaseMapper<PointStandardAlarmView> {

    @Select("SELECT *  FROM pointStandardAlarmView " +
            " ${ew.customSqlSegment}")
    List<PointAlarmView> listAlarm(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);

}
