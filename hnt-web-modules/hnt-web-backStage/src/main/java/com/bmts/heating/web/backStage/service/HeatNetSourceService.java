package com.bmts.heating.web.backStage.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.HeatNetSource;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface HeatNetSourceService {
    Response insertNetSource(List<HeatNetSource> info);

    Response deleteNetById(int id);

    Response deleteSourceById(int id);

    Response deleteNetSourceById(int id);

    Response deleteNetSource(HeatNetSource dto);

    Response queryNetSource();

    Response updNetSource(HeatNetSource info);

}
