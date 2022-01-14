package com.bmts.heating.commons.db.mapper.second;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.bmts.heating.commons.basement.second.db.entity.PointConfigSecond;
import com.bmts.heating.commons.entiy.second.request.device.PointConfigSecondDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author naming
 * @since 2020-11-09
 */
public interface PointConfigSecondMapper extends BaseMapper<PointConfigSecond> {

    @Select("SELECT  pcs.*, ibd.eType,  ibd.deviceName,  dic.`code`  " +
            "FROM  iot_pointConfigSecond pcs  " +
            "LEFT JOIN iot_basicDevice ibd ON ibd.id = pcs.deviceId  " +
            "LEFT JOIN dic ON dic.`name` = ibd.eType  " +
            "AND dic.pid = ( SELECT  id FROM  dic  WHERE  dic.`code` = 'iot_basicDevice'  )  " +
            " ${ew.customSqlSegment} ")
    List<PointConfigSecondDto> secondQueryPoint(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);
}
