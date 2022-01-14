package com.bmts.heating.bussiness.baseInformation.app.joggle.storm;

import com.bmts.heating.commons.db.mapper.StormConfigDetailMapper;
import com.bmts.heating.commons.db.service.StormConfigService;
import com.bmts.heating.commons.entiy.baseInfo.request.StormConfigDto;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author naming
 * @description
 * @date 2021/3/31 16:26
 **/
@RestController
@Api(tags = "拓扑图")
@RequestMapping("/storm")
@Slf4j
public class StromJoggle {
    @Autowired
    StormConfigService stormConfigService;
    @Autowired
    StormConfigDetailMapper stormConfigDetailMapper;

//    @ApiOperation(value = "配置拓扑图")
//    @PostMapping
//    public Response configStorm()
//    {
//
//        StormConfigDto stormConfigDto = new StormConfigDto();
//        StormConfigDto.kjkl ki = new StormConfigDto.kjkl();
//        stormConfigDto.setChilds();
//
//    }

}
