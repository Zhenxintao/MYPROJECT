package com.bmts.heating.web.backStage.service.impl;

import com.bmts.heating.commons.basement.model.db.entity.QuartzJob;
import com.bmts.heating.commons.container.performance.config.SavantServices;
import com.bmts.heating.commons.entiy.quartzjob.request.QueryJobDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.QuartzJobService;
import com.bmts.heating.web.backStage.utils.BackRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuartzJobServiceImpl extends SavantServices implements QuartzJobService {
@Autowired
private BackRestTemplate backRestTemplate;

private final String  baseServer = "application_job";

    @Override
    public Response insertJob(QuartzJob dto) {
       return backRestTemplate.post("/insertJob",dto,baseServer);
    }

    @Override
    public Response removeJob(Integer id) {
        return backRestTemplate.delete("/removeJob",id,baseServer);
    }

    @Override
    public Response updJob(QuartzJob dto) {
        return backRestTemplate.put("/updJob",dto,baseServer);
    }

    @Override
    public Response queryJob(QueryJobDto dto) {
        return backRestTemplate.post("/queryJob",dto,baseServer);
    }
}
