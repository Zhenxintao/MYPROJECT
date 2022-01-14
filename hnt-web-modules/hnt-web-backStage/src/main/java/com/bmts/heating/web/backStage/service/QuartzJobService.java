package com.bmts.heating.web.backStage.service;


import com.bmts.heating.commons.basement.model.db.entity.QuartzJob;
import com.bmts.heating.commons.entiy.quartzjob.request.QueryJobDto;
import com.bmts.heating.commons.utils.restful.Response;

public interface QuartzJobService {
    Response insertJob(QuartzJob dto);

    Response removeJob(Integer id);

    Response updJob(QuartzJob dto);

    Response queryJob(QueryJobDto dto);
}
