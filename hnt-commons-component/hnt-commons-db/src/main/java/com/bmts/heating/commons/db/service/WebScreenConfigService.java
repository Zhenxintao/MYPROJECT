package com.bmts.heating.commons.db.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bmts.heating.commons.basement.model.db.entity.WebScreenConfig;
import com.bmts.heating.commons.entiy.baseInfo.request.WebScreenConfigDto;
import org.springframework.stereotype.Service;

@Service
public interface WebScreenConfigService extends IService<WebScreenConfig> {
}
