package com.bmts.heating.commons.db.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.RecordDeviceUpDown;
import com.bmts.heating.commons.entiy.baseInfo.response.RecordDeviceUpDownCurve;
import com.bmts.heating.commons.entiy.baseInfo.response.RecordDeviceUpDownResponse;
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
public interface RecordDeviceUpDownMapper extends BaseMapper<RecordDeviceUpDown> {


    @Select("SELECT rd.*,hts.`name` AS stationName FROM `record_deviceUpDown` rd LEFT JOIN  heatTransferStation hts ON hts.id = rd.heatTransferStationId  " +
            "  ${ew.customSqlSegment}")
    IPage<RecordDeviceUpDownResponse> queyPage(Page<RecordDeviceUpDownResponse> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("SELECT DATE_FORMAT( rd.createTime, '%Y-%m-%d' ) AS createTime,WEEKDAY(rd.createTime) + 1 AS `week`, " +
            " COUNT(rd.id) AS count, rd.type, rd.operation ,rd.heatTransferStationId  FROM record_deviceUpDown rd  " +
            "  ${ew.customSqlSegment}")
    List<RecordDeviceUpDownCurve> countDevice(@Param(Constants.WRAPPER) Wrapper wrapper);
}
