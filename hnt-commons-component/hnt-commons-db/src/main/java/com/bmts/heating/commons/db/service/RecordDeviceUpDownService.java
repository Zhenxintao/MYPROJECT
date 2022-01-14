package com.bmts.heating.commons.db.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bmts.heating.commons.basement.model.db.entity.RecordDeviceUpDown;
import com.bmts.heating.commons.entiy.baseInfo.response.RecordDeviceUpDownCurve;
import com.bmts.heating.commons.entiy.baseInfo.response.RecordDeviceUpDownResponse;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author naming
 * @since 2020-11-09
 */
public interface RecordDeviceUpDownService extends IService<RecordDeviceUpDown> {

    IPage<RecordDeviceUpDownResponse> queyPage(Page<RecordDeviceUpDownResponse> page, Wrapper wrapper);


    List<RecordDeviceUpDownCurve> countDevice(Wrapper wrapper);
}
