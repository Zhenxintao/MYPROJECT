package com.bmts.heating.web.scada.service.base;

import com.bmts.heating.commons.entiy.baseInfo.request.ConfigSysUserListDto;
import com.bmts.heating.commons.utils.restful.Response;

public interface ConfigSysUserListService {


    Response listConfigSysUser(int userId);

    Response addConfig(ConfigSysUserListDto dto);

    Response updateConfig(ConfigSysUserListDto dto);

    Response delConfig(int id);


    Response getDetail(int id);


}
