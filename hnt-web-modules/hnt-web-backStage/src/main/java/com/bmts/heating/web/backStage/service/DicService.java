package com.bmts.heating.web.backStage.service;

import com.bmts.heating.commons.basement.model.db.entity.Dic;
import com.bmts.heating.commons.entiy.baseInfo.request.DicDto;
import com.bmts.heating.commons.utils.restful.Response;

public interface DicService {

    Response insert(Dic info);

    Response delete(int id);

    Response update(Dic info);

    Response detail(int id);

    Response query(DicDto dto);

    Response queryAll();

}
