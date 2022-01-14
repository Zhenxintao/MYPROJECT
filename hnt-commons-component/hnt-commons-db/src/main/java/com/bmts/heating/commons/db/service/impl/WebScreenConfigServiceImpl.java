package com.bmts.heating.commons.db.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.WebScreenConfig;
import com.bmts.heating.commons.db.mapper.WebScreenConfigMapper;
import com.bmts.heating.commons.db.service.WebScreenConfigService;
import com.bmts.heating.commons.entiy.baseInfo.request.WebScreenConfigDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebScreenConfigServiceImpl extends ServiceImpl<WebScreenConfigMapper, WebScreenConfig> implements WebScreenConfigService {

}
