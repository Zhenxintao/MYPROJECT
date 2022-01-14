package com.bmts.heating.web.scada.controller.history;

import com.bmts.heating.commons.entiy.gathersearch.request.CurveDto;
import com.bmts.heating.commons.entiy.gathersearch.response.history.CurveResponse;
import com.bmts.heating.commons.entiy.gathersearch.response.history.WaterPowerHeatCurveResponse;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.scada.service.base.CurveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author naming
 * @description
 * @date 2021/1/8 10:13
 **/
@Api(tags = "历史曲线")
@RestController
@RequestMapping("/history")
public class CurveController {

    @Autowired
    private CurveService curveService;

    @ApiOperation(value = "查询历史曲线", response = CurveResponse.class)
    @PostMapping("/curve")
    public Response curve(@RequestBody CurveDto param) {
        return curveService.curve(param);
    }

    @ApiOperation(value = "查询大表格数据曲线", response = CurveResponse.class)
    @PostMapping("/dataCurve")
    public Response dataCurve(@RequestBody CurveDto param) {
        return curveService.dataCurve(param);
    }

    @ApiOperation(value = "查询日曲线对比", response = CurveResponse.class)
    @PostMapping("/dayCurveContrast")
    public Response dayCurveContrast(@RequestBody CurveDto param) {
        return curveService.dayCurveContrast(param);
    }

    @ApiOperation(value = "每日对比曲线", response = WaterPowerHeatCurveResponse.class)
    @PostMapping("/daily")
    public Response daily() {
        List<WaterPowerHeatCurveResponse> list = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            WaterPowerHeatCurveResponse respo = new WaterPowerHeatCurveResponse();
            respo.setTime(LocalDateTime.now().minusHours(i).toLocalDate().atTime(LocalDateTime.now().minusHours(i).getHour(), 0, 0));
            respo.setHeat(getRandom());
            respo.setWater(getRandom());
            respo.setPower(getRandom());
            respo.setTempOut(getTemper());
            list.add(respo);
        }

        return Response.success(list);
    }

    @ApiOperation(value = "查询单站能耗曲线", response = CurveResponse.class)
    @PostMapping("/energyCurve")
    public Response energyCurve(@RequestBody CurveDto param) {
        return curveService.energyCurve(param);
    }

    private BigDecimal getRandom() {
        double min = 0.001;
        double max = 100; // 总和
        int scl = 3; // 小数最大位数
        int pow = (int) Math.pow(10, scl); // 用于提取指定小数位
        double val = Math.floor((Math.random() * (max - min) + min) * pow) / pow;
        return BigDecimal.valueOf(val);
    }

    private BigDecimal getTemper() {
        double min = 1.00;
        double max = 20; // 总和
        int scl = 2; // 小数最大位数
        int pow = (int) Math.pow(10, scl); // 用于提取指定小数位
        double val = Math.floor((Math.random() * (max - min) + min) * pow) / pow;
        return BigDecimal.valueOf(val);
    }

}
