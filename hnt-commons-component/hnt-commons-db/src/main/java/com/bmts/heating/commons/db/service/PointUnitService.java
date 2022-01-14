package com.bmts.heating.commons.db.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bmts.heating.commons.basement.model.db.entity.PointUnit;
import com.bmts.heating.commons.basement.model.db.response.PointStandardResponse;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author ${author}
 * @since 2020-11-27
 */
public interface PointUnitService extends IService<PointUnit> {
    List<PointStandardResponse> listPoint(QueryWrapper queryWrapper);
}
