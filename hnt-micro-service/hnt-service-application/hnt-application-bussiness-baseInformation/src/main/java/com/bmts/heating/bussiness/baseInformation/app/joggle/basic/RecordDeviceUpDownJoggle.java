package com.bmts.heating.bussiness.baseInformation.app.joggle.basic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.RecordDeviceUpDown;
import com.bmts.heating.commons.db.service.RecordDeviceUpDownService;
import com.bmts.heating.commons.entiy.baseInfo.request.RecordDeviceUpDownDto;
import com.bmts.heating.commons.entiy.baseInfo.response.RecordDeviceUpDownCurve;
import com.bmts.heating.commons.entiy.baseInfo.response.RecordDeviceUpDownCurveList;
import com.bmts.heating.commons.entiy.baseInfo.response.RecordDeviceUpDownResponse;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName: RecordDeviceUpDownJoggle
 * @Description: 设备启停记录管理
 * @Author: pxf
 * @Date: 2021/1/15 15:13
 * @Version: 1.0
 */

@Api(tags = "设备启停记录管理")
@RestController
@RequestMapping("/device")
@Slf4j
public class RecordDeviceUpDownJoggle {

    @Autowired
    private RecordDeviceUpDownService recordDeviceUpDownService;

    @ApiOperation("分页查询")
    @PostMapping("/page")
    public Response page(@RequestBody RecordDeviceUpDownDto dto) {
        Page<RecordDeviceUpDownResponse> page = new Page<>(dto.getCurrentPage(), dto.getPageCount());
        QueryWrapper<RecordDeviceUpDown> queryWrapper = new QueryWrapper<>();
        if (dto.getStartTime() != null) {
            queryWrapper.ge("rd.createTime", dto.getStartTime());
        }
        if (dto.getEndTime() != null) {
            queryWrapper.le("rd.createTime", dto.getEndTime());
        }
        if (dto.getType() != null) {
            queryWrapper.eq("rd.type", dto.getType());
        }
        if (dto.getHeatTransferStationId() != null) {
            queryWrapper.eq("rd.heatTransferStationId", dto.getHeatTransferStationId());
        }
        return Response.success(recordDeviceUpDownService.queyPage(page, queryWrapper));
    }

    @ApiOperation("查询曲线")
    @PostMapping("/curve")
    public Response curve(@RequestBody RecordDeviceUpDownDto dto) {
        QueryWrapper<RecordDeviceUpDown> queryWrapperTrue = new QueryWrapper<>();
        QueryWrapper<RecordDeviceUpDown> queryWrapperFalse = new QueryWrapper<>();
        if (dto.getStartTime() != null) {
            queryWrapperTrue.ge("rd.createTime", dto.getStartTime());
            queryWrapperFalse.ge("rd.createTime", dto.getStartTime());
        }
        if (dto.getEndTime() != null) {
            queryWrapperTrue.le("rd.createTime", dto.getEndTime());
            queryWrapperFalse.le("rd.createTime", dto.getEndTime());
        }
        if (dto.getType() != null) {
            queryWrapperTrue.eq("rd.type", dto.getType());
            queryWrapperTrue.groupBy("rd.type");

            queryWrapperFalse.eq("rd.type", dto.getType());
            queryWrapperFalse.groupBy("rd.type");
        }
        if (dto.getHeatTransferStationId() != null) {
            queryWrapperTrue.eq("rd.heatTransferStationId", dto.getHeatTransferStationId());
            queryWrapperTrue.groupBy("rd.heatTransferStationId");

            queryWrapperFalse.eq("rd.heatTransferStationId", dto.getHeatTransferStationId());
            queryWrapperFalse.groupBy("rd.heatTransferStationId");
        }
        queryWrapperTrue.groupBy("DATE_FORMAT( rd.createTime, '%Y-%m-%d' ) ");
        queryWrapperTrue.groupBy("rd.operation");
        queryWrapperFalse.groupBy("DATE_FORMAT( rd.createTime, '%Y-%m-%d' ) ");
        queryWrapperFalse.groupBy("rd.operation");
        // 查询设备开启的曲线数据
        queryWrapperTrue.eq("rd.operation", true);
        List<RecordDeviceUpDownCurve> listTrue = recordDeviceUpDownService.countDevice(queryWrapperTrue);
        // 查询设备停止的曲线数据
        queryWrapperFalse.eq("rd.operation", false);
        List<RecordDeviceUpDownCurve> listFalse = recordDeviceUpDownService.countDevice(queryWrapperFalse);
        RecordDeviceUpDownCurveList list = new RecordDeviceUpDownCurveList();
        list.setListTrue(listTrue);
        list.setListFalse(listFalse);
        return Response.success(list);
    }

}
