package com.bmts.heating.commons.db.service.second;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bmts.heating.commons.basement.second.db.entity.PointConfigSecond;
import com.bmts.heating.commons.entiy.second.request.device.PointConfigSecondDto;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author naming
 * @since 2020-11-09
 */
public interface PointConfigSecondService extends IService<PointConfigSecond> {

    List<PointConfigSecondDto> secondQueryPoint(QueryWrapper wrapper);
}
