package com.bmts.heating.web.scada.controller.patrol;

import com.bmts.heating.commons.basement.model.db.entity.CommonHeatSeason;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.scada.service.patrol.PatrolStationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "巡站")
@Slf4j
@RestController
@RequestMapping("patrolStation")
public class PatrolStationController {

    @Autowired
    private PatrolStationService patrolStationService;

    @ApiOperation(value = "本月巡站统计", response = CommonHeatSeason.class)
    @GetMapping
    public Response statPatrol() {
        return patrolStationService.statPatrol();
    }
}
