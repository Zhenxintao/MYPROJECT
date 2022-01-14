package com.bmts.heating.web.scada.service.monitor.impl;

import com.alibaba.fastjson.JSONObject;
import com.bmts.heating.commons.entiy.baseInfo.cache.FirstNetBase;
import com.bmts.heating.commons.entiy.baseInfo.cache.PointCache;
import com.bmts.heating.commons.entiy.baseInfo.request.HeatSourceTableDto;
import com.bmts.heating.commons.entiy.baseInfo.response.HeatSourcePointRealData;
import com.bmts.heating.commons.entiy.baseInfo.response.HeatSourceRealDataResponse;
import com.bmts.heating.commons.entiy.baseInfo.response.PointConfigResponse;
import com.bmts.heating.commons.entiy.gathersearch.request.DataRouteParam;
import com.bmts.heating.commons.entiy.gathersearch.response.cache.PointDetail;
import com.bmts.heating.commons.entiy.gathersearch.response.cache.SourceCacheRealValue;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import com.bmts.heating.web.scada.converter.SourcePointLRealConverter;
import com.bmts.heating.web.scada.service.monitor.SourceMonitorService;
import com.bmts.heating.web.scada.service.monitor.impl.common.CommonService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
//@EnableScheduling//开启定时
public class SourceMonitorServiceImpl extends CommonService implements SourceMonitorService {

    @Autowired
    private TSCCRestTemplate tsccRestTemplate;

    private final String gatherSearch = "gather_search";
    private final String baseServer = "bussiness_baseInfomation";

    @Override
    public List<FirstNetBase> sourceFirstNetBase(Integer userId) {
        return Arrays.asList(tsccRestTemplate.get("/common/querySourceInfoByUserId?id=" + userId, baseServer, FirstNetBase[].class));
    }

    public List<PointCache> monitorData(Map<Integer, String[]> mapRequest) {
        DataRouteParam param = new DataRouteParam();
        param.setMap(mapRequest);
        return Arrays.asList(tsccRestTemplate.post("/route/data", param, gatherSearch, PointCache[].class));
    }

    private Boolean filterKeywords(FirstNetBase e, String keyWord) {
        boolean b = StringUtils.containsIgnoreCase(e.getHeatTransferStationName(), keyWord);
        boolean b1 = StringUtils.containsIgnoreCase(e.getHeatSystemName(), keyWord);
        return b || b1;
    }

    @Override
    public Map<String, Object> table(HeatSourceTableDto dto, Integer userId) {
        List<FirstNetBase> firstNetBaseList = this.sourceFirstNetBase(userId)
                .stream()
                .filter(e -> e.getHeatSourceId() != 0)
                .filter(e -> StringUtils.containsIgnoreCase(e.getHeatSourceName(), dto.getKeyWord()))
                .collect(Collectors.toList());
        int page = dto.getCurrentPage();
        int size = dto.getPageCount();
        if (firstNetBaseList.size() <= 0) {
            return null;
        }
        long count = firstNetBaseList.size();
        if (StringUtils.isNotBlank(dto.getSortName())) {
            firstNetBaseList = super.buildSortSource(dto, firstNetBaseList).stream()
                    .skip((page - 1) * size).limit(size)//分页
                    .collect(Collectors.toList());
        } else {
            //默认
            firstNetBaseList = firstNetBaseList.stream().sorted(Comparator.comparing(FirstNetBase::getHeatSystemId))//排序
                    .skip((page - 1) * size).limit(size)//分页
                    .collect(Collectors.toList());
        }
        List<PointCache> pointCacheArrayList = this.monitorData(super.packageParamSource(firstNetBaseList, dto.getColumnName()));
        Map<String, Object> res = new HashMap<>();
        res.put("total", count);
        res.put("data", this.packageResult(pointCacheArrayList, firstNetBaseList));
        return res;
    }


    @Override
    public Map<String, Object> getPointDataBySource(List<FirstNetBase> sList, String[] points) {
        List<PointCache> pointCacheArrayList = this.monitorData(super.packageParamSource(sList, points));
        Map<String, Object> res = new HashMap<>();
        res.put("data", this.packageResult(pointCacheArrayList, sList));
        return res;
    }

    @Override
    public List<PointConfigResponse> queryPointConfigExist(Integer id) {
        DataRouteParam param = new DataRouteParam();
        Response res = tsccRestTemplate.get("/pointConfig/queryPointConfigExist/" + id, param, baseServer);
        String json = JSONObject.toJSONString(res.getData());
        List<PointConfigResponse> pointConfigResponses = JSONObject.parseArray(json, PointConfigResponse.class);
        return pointConfigResponses;
    }

    @Override
    public List<HeatSourceRealDataResponse> getRealData(HeatSourceTableDto dto) {
        //1、查询所有的热源
        List<FirstNetBase> firstNetBaseList = this.sourceFirstNetBase(dto.getUserId());
        //2、查询热源所有的点
        List<PointConfigResponse> pointResponse = new ArrayList<>();
        firstNetBaseList.stream().forEach(o -> {
            List<PointConfigResponse> pointConfigResponses = this.queryPointConfigExist(o.getHeatSystemId());
            pointResponse.addAll(pointConfigResponses);
        });
        if (!CollectionUtils.isEmpty(pointResponse)) {
            List<String> columnList = pointResponse.stream().map(PointConfigResponse::getColumnName).collect(Collectors.toList());
            List<PointCache> pointCacheArrayList = this.monitorData(super.packageParamSource(firstNetBaseList, columnList.toArray(new String[columnList.size()])));
            // 按照系统进行分组
            Map<Integer, List<PointCache>> systemMap = pointCacheArrayList.stream().collect(Collectors.groupingBy(PointCache::getRelevanceId));
            // 按照热源进行分组
            List<HeatSourceRealDataResponse> realData = new ArrayList<>();
            firstNetBaseList.stream().forEach(e -> {
                List<PointCache> pointCaches = systemMap.get(e.getHeatSystemId());
                if (!CollectionUtils.isEmpty(pointCaches)) {
                    HeatSourceRealDataResponse source = new HeatSourceRealDataResponse();
                    source.setSourceId(e.getHeatSourceId());
                    source.setSourceName(e.getHeatSourceName());
                    source.setNumber(Integer.parseInt(e.getNumber()));
                    source.setSystemName(e.getHeatSystemName());
                    List<HeatSourcePointRealData> pointList = new ArrayList<>();
                    pointCaches.stream().forEach(x -> {
                        HeatSourcePointRealData heatSourcePointRealData = SourcePointLRealConverter.INSTANCE.domainToInfo2(x);
                        PointConfigResponse pointConfigResponse = pointResponse.stream().filter(a -> Objects.equals(a.getColumnName(), x.getPointName()))
                                .findFirst().orElse(null);
                        if (pointConfigResponse != null && pointConfigResponse.getName() != null) {
                            heatSourcePointRealData.setDescription(pointConfigResponse.getName());
                        }
                        pointList.add(heatSourcePointRealData);
                    });
                    source.setPoints(pointList);
                    realData.add(source);
                }
            });
            return realData;
        }
        return Collections.emptyList();
    }


    //打包返回数据
    private List<SourceCacheRealValue> packageResult(List<PointCache> realData, List<FirstNetBase> baseStream) {
        Map<Integer, List<PointCache>> collect = realData.stream().collect(Collectors.groupingBy(PointCache::getRelevanceId));
        List<SourceCacheRealValue> res = new ArrayList<>();
        baseStream.forEach(e -> {
            SourceCacheRealValue cacheRealValue = new SourceCacheRealValue();
            cacheRealValue.setCompanyName(e.getHeatSourceOrgName());
            cacheRealValue.setHeatSourceName(e.getHeatSourceName());
            cacheRealValue.setSystemName(e.getHeatSystemName());
            cacheRealValue.setCabinetName(e.getHeatCabinetName());
            cacheRealValue.setArea(e.getHeatSystemArea());
            cacheRealValue.setHeatSourceId(e.getHeatSourceId());
            cacheRealValue.setHeatSystemId(e.getHeatSystemId());
            if (collect.get(e.getHeatSystemId()) != null) {
                List<PointDetail> pointDetails = collect.get(e.getHeatSystemId()).stream().map(point -> {
                    cacheRealValue.setQualityStrap(point.getQualityStrap());
                    PointDetail pointDetail = new PointDetail();
                    pointDetail.setPointId(point.getPointId());
                    pointDetail.setValue(point.getValue());
                    pointDetail.setColumnName(point.getPointName());
                    pointDetail.setAccidentLower(point.getAccidentLower());
                    pointDetail.setAccidentHigh(point.getAccidentHigh());
                    pointDetail.setRunningLower(point.getRunningLower());
                    pointDetail.setAccidentHigh(point.getRunningHigh());
                    pointDetail.setRangeLower(point.getRangeLower());
                    pointDetail.setDescriptionJson(point.getDescriptionJson());
                    pointDetail.setTimeStamp(point.getTimeStrap());
                    pointDetail.setQualityStrap(point.getQualityStrap());
                    return pointDetail;
                }).collect(Collectors.toList());
                cacheRealValue.setPointDetailList(pointDetails);
            }
            res.add(cacheRealValue);
        });
        return res;
    }

//	@Scheduled(cron = "0 * * * * ?")
//	private void tableTest(){
//		System.out.println("------------------------------------------------");
//		HeatSourceTableDto dto = new HeatSourceTableDto();
//		String [] args = new String[]{"T1h", "HM_T1h", "T1g", "HM_FT1", "HM_HT1", "T2g_SP_Time_0", "T2g_SP_Time_4", "CV1_MSP"};
//		dto.setColumnName(args);
//		dto.setCurrentPage(1);
//		dto.setPageCount(10);
//		System.out.println(this.table(dto));
//	}
}
