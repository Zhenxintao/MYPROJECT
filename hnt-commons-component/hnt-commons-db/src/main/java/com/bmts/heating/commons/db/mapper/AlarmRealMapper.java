package com.bmts.heating.commons.db.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.AlarmReal;
import com.bmts.heating.commons.entiy.baseInfo.response.AlarmRealBarResponse;
import com.bmts.heating.commons.entiy.baseInfo.response.AlarmRealResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author naming
 * @since 2020-12-19
 */
public interface AlarmRealMapper extends BaseMapper<AlarmReal> {
    @Select("SELECT a.stationId,s.`name` as StationName,COUNT(a.Id) AS 'stationAlarmCount'  FROM heatTransferStation s left join alarmReal a on s.Id = a.stationId   ${ew.customSqlSegment}")
    List<AlarmRealBarResponse> alarmRealBarList(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);

    @Select("SELECT  ar.*, hs.`name` AS systemName FROM  alarmReal ar   LEFT JOIN heatSystem  hs ON ar.heatSystemId = hs.id   " +
            " ${ew.customSqlSegment}")
    IPage<AlarmRealResponse> pageAlarmReal(Page<AlarmReal> page, @Param(Constants.WRAPPER) Wrapper wrapper);


    @Select("SELECT  ar.*, hs.`name` AS systemName FROM  alarmReal ar   LEFT JOIN heatSystem  hs ON ar.heatSystemId = hs.id WHERE ar.id = #{id}")
    AlarmRealResponse queryId(@Param("id") int id);

}
