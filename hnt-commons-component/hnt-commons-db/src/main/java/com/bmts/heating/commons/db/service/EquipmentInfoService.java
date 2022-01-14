package com.bmts.heating.commons.db.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bmts.heating.commons.basement.model.db.entity.EquipmentInfo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author naming
 * @since 2020-11-09
 */
public interface EquipmentInfoService extends IService<EquipmentInfo> {

    boolean configSourcePoint();

}
