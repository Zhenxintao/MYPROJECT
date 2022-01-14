package com.bmts.heating.commons.db.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bmts.heating.commons.basement.model.db.entity.DefaultRealHeaders;
import com.bmts.heating.commons.entiy.baseInfo.response.DefaultRealHeadersDto;

import java.util.List;

/**
 * <p>
 * 业务 service
 * </p>
 *
 * @author pxf
 * @since 2020-11-16
 */
public interface DefaultRealHeadersService extends IService<DefaultRealHeaders> {


    List<DefaultRealHeadersDto> listHeaders(QueryWrapper queryWrapper);
}
