package com.bmts.heating.web.scada.controller;

import com.bmts.heating.commons.entiy.baseInfo.request.RecordDeviceUpDownDto;
import com.bmts.heating.commons.entiy.baseInfo.response.RecordDeviceUpDownCurveList;
import com.bmts.heating.commons.entiy.baseInfo.response.RecordDeviceUpDownResponse;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.scada.service.base.RecordDeviceUpDownService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = "设备启停记录管理")
@RestController
@RequestMapping("/device")
@Slf4j
public class RecordDeviceUpDownController {

    @Autowired
    private RecordDeviceUpDownService recordDeviceUpDownService;

    @ApiOperation(value = "分页查询", response = RecordDeviceUpDownResponse.class)
    @PostMapping("/page")
    public Response page(@RequestBody RecordDeviceUpDownDto dto) {
        return recordDeviceUpDownService.queyPage(dto);
    }


    @ApiOperation(value = "查询曲线", response = RecordDeviceUpDownCurveList.class)
    @PostMapping("/curve")
    public Response curve(@RequestBody RecordDeviceUpDownDto dto) {
        return recordDeviceUpDownService.countDevice(dto);
    }


}
