package com.bmts.heating.web.backStage.service;

import com.bmts.heating.commons.basement.model.db.entity.DefaultRealHeaders;
import com.bmts.heating.commons.utils.restful.Response;

import java.util.List;

public interface DefaultRealHeadersService {

    Response query(int type);

    Response add(DefaultRealHeaders param);

    Response addBatch(List<DefaultRealHeaders> param);

    Response update(DefaultRealHeaders param);

    Response delete(int id);


}
