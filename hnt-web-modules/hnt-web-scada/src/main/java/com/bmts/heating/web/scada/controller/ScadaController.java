package com.bmts.heating.web.scada.controller;

import com.bmts.heating.commons.entiy.baseInfo.request.HeatSourceTableDto;
import com.bmts.heating.commons.entiy.baseInfo.response.HeatSourceRealDataResponse;
import com.bmts.heating.commons.entiy.converge.HeatStationRealDto;
import com.bmts.heating.commons.entiy.converge.HeatStationRealResponseDto;
import com.bmts.heating.commons.jwt.annotation.PassToken;
import com.bmts.heating.commons.utils.auth.SHA1;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.scada.service.monitor.MonitorService;
import com.bmts.heating.web.scada.service.monitor.SourceMonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "查询对接接口")
@RequestMapping("scada")
public class ScadaController {

    @Autowired
    private MonitorService monitorService;
    @Autowired
    private SourceMonitorService sourceMonitorService;


    @Value("${auth.appkey}")
    private String appkey;


    @ApiOperation(value = "获取实时数据接口", response = HeatStationRealResponseDto.class)
    @PostMapping("/monitor/real")
    @PassToken
    public Response real(@RequestBody HeatStationRealDto dto, HttpServletRequest request) {
        if (!validate(request)) {
            log.error("validate header cause execption");
            return Response.fail("没有访问权限！");
        }
        if (dto.getPointNames().length == 0 || CollectionUtils.isEmpty(dto.getStationIds())) {
            return Response.success();
        }
        // 进行数据查询
        List<HeatStationRealResponseDto> realData = monitorService.getRealData(dto);
        return Response.success(realData);
    }


    @ApiOperation(value = "获取热源实时数据接口", response = HeatSourceRealDataResponse.class)
    @PostMapping("/monitor/source")
    @PassToken
    public Response source1(HttpServletRequest request) {
        if (!validate(request)) {
            log.error("validate header cause execption");
            return Response.fail("没有访问权限！");
        }
        HeatSourceTableDto dto = new HeatSourceTableDto();
        dto.setUserId(-1);
        try {
            List<HeatSourceRealDataResponse> realData = sourceMonitorService.getRealData(dto);
            return Response.success(realData);
        } catch (Exception e) {
            return Response.fail();
        }

    }

    private boolean validate(HttpServletRequest request) {
        try {
            String nonce = request.getHeader("nonce");
            String nonce1 = request.getHeader("nonce1");
            String appKey = appkey;
            String timestamp = request.getHeader("timestamp");
            log.info("nonce---{},nonce1---{},appKey---{},timestamp---{}", nonce, nonce1, appKey, timestamp);
            if (StringUtils.isBlank(nonce) || StringUtils.isBlank(nonce1) || StringUtils.isBlank(appKey) || StringUtils.isBlank(timestamp)) {
                return false;
            }
            String checkSumHeader = request.getHeader("checkSum");
            log.info("checkSumHeader---{}", checkSumHeader);
            if (SHA1.encode(nonce.concat(nonce1).concat(appKey).concat(timestamp)).toLowerCase().equals(checkSumHeader)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("validate header cause exception {}", e);
            return false;
        }

    }


    //@ApiOperation(value = "获取热源实时数据接口", response = HeatSourceRealDataResponse.class)
    //@PostMapping("/monitor/source")
    //@PassToken
    //public Response source(HttpServletRequest request) {
    //    try {
    //        if (!validate(request)) {
    //            log.error("validate header cause execption");
    //            return Response.fail("没有访问权限！");
    //        }
    //        List<HeatSourceRealDataResponse> resdata = new ArrayList<>();
    //        HeatSourceTableDto dto = new HeatSourceTableDto();
    //        dto.setUserId(-1);
    //
    //        dto.setCurrentPage(1);
    //        dto.setPageCount(Integer.MAX_VALUE);
    //        dto.setKeyWord("");
    //
    //
    //        Integer adminUserId = -1;
    //        // dto.setColumnName(pointId.toArray(new String[pointId.size()]));
    //        //1、查询所有的热源
    //        List<FirstNetBase> fnb = sourceMonitorService.sourceFirstNetBase(adminUserId);
    //        //2、查询热源所有的点
    //        List<PointConfigResponse> pointResponse = new ArrayList<>();
    //        fnb.stream().forEach(o -> {
    //            List<PointConfigResponse> pointConfigResponses = sourceMonitorService.queryPointConfigExist(o.getHeatSystemId());
    //            pointResponse.addAll(pointConfigResponses);
    //        });
    //
    //        List<String> pointList = new ArrayList<>();
    //        pointResponse.stream().distinct().forEach(x -> {
    //            pointList.add(x.getColumnName());
    //        });
    //        dto.setColumnName(pointList.toArray(new String[pointList.size()]));
    //
    //        //3、查询热源、点实时数据
    //        Map<String, Object> res = sourceMonitorService.table(dto, -1);
    //        if (res == null) return Response.success(resdata);
    //
    //        if (res.get("data") != null) {
    //            List<SourceCacheRealValue> list = (ArrayList) res.get("data");
    //            if (!CollectionUtils.isEmpty(list)) {
    //                list.stream().forEach(e -> {
    //                    HeatSourceRealDataResponse source = new HeatSourceRealDataResponse();
    //                    source.setSourceId(e.getHeatSourceId());
    //                    source.setSourceName(e.getHeatSourceName());
    //                    // source.setNumber();
    //                    List<HeatSourcePointRealData> point = new ArrayList<>();
    //                    List<PointDetail> pointDetailList = e.getPointDetailList();
    //                    if (!CollectionUtils.isEmpty(pointDetailList)) {
    //                        pointDetailList.stream().forEach(x -> {
    //                            HeatSourcePointRealData heatSourcePointRealData = SourcePointLRealConverter.INSTANCE.domainToInfo(x);
    //                            PointConfigResponse pointConfigResponse = pointResponse.stream().filter(a -> Objects.equals(a.getColumnName(), x.getColumnName())).findFirst().orElse(null);
    //                            if (pointConfigResponse != null && pointConfigResponse.getName() != null) {
    //                                heatSourcePointRealData.setDescription(pointConfigResponse.getName());
    //                            }
    //                            point.add(heatSourcePointRealData);
    //                        });
    //                    }
    //                    source.setPoints(point);
    //                    resdata.add(source);
    //                });
    //            }
    //        }
    //        return Response.success(resdata);
    //    } catch (Exception e) {
    //        return Response.fail();
    //    }
    //
    //}

}
