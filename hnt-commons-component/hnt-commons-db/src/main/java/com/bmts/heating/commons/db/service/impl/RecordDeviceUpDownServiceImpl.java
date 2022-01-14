package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.RecordDeviceUpDown;
import com.bmts.heating.commons.db.mapper.RecordDeviceUpDownMapper;
import com.bmts.heating.commons.db.service.RecordDeviceUpDownService;
import com.bmts.heating.commons.entiy.baseInfo.response.RecordDeviceUpDownCurve;
import com.bmts.heating.commons.entiy.baseInfo.response.RecordDeviceUpDownResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author naming
 * @since 2020-11-09
 */
@Service
public class RecordDeviceUpDownServiceImpl extends ServiceImpl<RecordDeviceUpDownMapper, RecordDeviceUpDown> implements RecordDeviceUpDownService {

    @Autowired
    private RecordDeviceUpDownMapper recordDeviceUpDownMapper;

    @Override
    public IPage<RecordDeviceUpDownResponse> queyPage(Page<RecordDeviceUpDownResponse> page, Wrapper wrapper) {
        return recordDeviceUpDownMapper.queyPage(page, wrapper);
    }

    @Override
    public List<RecordDeviceUpDownCurve> countDevice(Wrapper wrapper) {
        return recordDeviceUpDownMapper.countDevice(wrapper);
    }
}
