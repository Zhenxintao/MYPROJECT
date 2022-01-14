package com.bmts.heating.monitor.second.joggle;

import com.bmts.heating.commons.entiy.second.request.device.DeviceFaultDto;
import com.bmts.heating.commons.entiy.second.request.device.DeviceInfoDto;
import com.bmts.heating.commons.redis.second.RedisDeviceService;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.monitor.second.service.KafkaCommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

/**
 * @ClassName: DeviceJoggle
 * @Description: 设备数据
 * @Author: pxf
 * @Date: 2021/4/19 10:20
 * @Version: 1.0
 */

@RestController
@Slf4j
@Api(tags = "设备数据")
@RequestMapping("/device")
public class DeviceJoggle {

    private static Random random = new Random();

    @Autowired
    private KafkaCommonService kafkaCommonService;

    @Autowired
    private RedisDeviceService redisDeviceService;


    @PostMapping("/receive")
    @ApiOperation("接收数据")
    public Response receiveData(@RequestBody List<DeviceInfoDto> listInfo) {
        if (!CollectionUtils.isEmpty(listInfo)) {
            listInfo.stream().forEach(e -> {
                kafkaCommonService.sendDeviceInfo(e);
            });
        }

        return Response.success();
    }

    @PostMapping("/fault")
    @ApiOperation("设备故障")
    public Response faultData(@RequestBody List<DeviceFaultDto> listDto) {
        if (!CollectionUtils.isEmpty(listDto)) {
            listDto.stream().forEach(e -> {
                kafkaCommonService.sendDeviceFault(e);
            });
        }
        return Response.success();
    }


}
