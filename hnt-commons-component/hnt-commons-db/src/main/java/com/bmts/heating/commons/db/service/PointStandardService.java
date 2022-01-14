package com.bmts.heating.commons.db.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
import com.bmts.heating.commons.basement.model.db.response.PointStandardResponse;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author naming
 * @since 2020-11-09
 */
public interface PointStandardService extends IService<PointStandard> {

    Page<PointStandardResponse> queryPointStandard(Page page, QueryWrapper queryWrapper);

    List<PointStandardResponse> listPointStandard(QueryWrapper queryWrapper);
}
