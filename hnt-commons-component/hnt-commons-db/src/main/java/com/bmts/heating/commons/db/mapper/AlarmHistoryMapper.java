package com.bmts.heating.commons.db.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.AlarmHistory;
import com.bmts.heating.commons.entiy.baseInfo.response.AlarmHistoryResponse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author naming
 * @since 2020-12-19
 */
public interface AlarmHistoryMapper extends BaseMapper<AlarmHistory> {


    @Select("SELECT  ah.*, hs.`name` AS systemName FROM  alarmHistory ah   LEFT JOIN heatSystem  hs ON ah.heatSystemId = hs.id   " +
            " ${ew.customSqlSegment}")
    IPage<AlarmHistoryResponse> pageAlarmHis(Page<AlarmHistory> page, @Param(Constants.WRAPPER) Wrapper wrapper);


    @Select("SELECT  ah.*, hs.`name` AS systemName FROM  alarmHistory ah   LEFT JOIN heatSystem  hs ON ah.heatSystemId = hs.id   WHERE ah.id = #{id}")
    AlarmHistoryResponse queryId(@Param("id") int id);

}
