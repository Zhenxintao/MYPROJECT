package com.bmts.heating.web.scada.service.history.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bmts.heating.commons.basement.model.db.entity.SourceFirstNetBaseView;
import com.bmts.heating.commons.basement.model.db.entity.StationFirstNetBaseView;
import com.bmts.heating.commons.entiy.baseInfo.cache.FirstNetBase;
import com.bmts.heating.commons.entiy.baseInfo.request.QueryBaseDataDto;
import com.bmts.heating.commons.entiy.gathersearch.request.*;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.commons.utils.tdengine.TdAggregateTableIndex;
import com.bmts.heating.commons.utils.tdengine.TdTableIndex;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import com.bmts.heating.web.scada.service.history.HistoryDataService;
import com.google.gson.JsonObject;
import com.sun.el.lang.ELArithmetic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HistoryDataServiceImpl implements HistoryDataService {

    @Autowired
    private TSCCRestTemplate template;

    private final String gatherSearch = "gather_search";

    private final String baseServer = "bussiness_baseInfomation";

    @Override
    public Response queryHistoryData(QueryHistoryDataDto dto) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            if (dto.getEndTime() <= dto.getStartTime()) {
                return Response.fail("查询选择时间范围错误！请重新选择时间范围后再进行查询");
            }
            if (dto.getPoints().size() <= 0) {
                return Response.fail("该页面列表未配置点位信息");
            }
            if (dto.getGroupId().size() <= 0) {
                return Response.fail("查询失败！请选择具体换热站或热源再进行查询！");
            }
//        //检查点位信息是否在td历史库中存在
//        CheckTdPointDto checkTdPointDto = new CheckTdPointDto(){{setType(dto.getLevel());setPoints(dto.getPoints());}};
//        List<TdPointConfig> tdPointConfigs= getHistoryPointsInfo(checkTdPointDto);
//        if (dto.getPoints().size()<=0){
//            return Response.success(resultMap);
//        }
            dto.setPoints(dto.getPoints());
            //组装Td历史查询参数
            QueryTdDto queryTdDto = new QueryTdDto();
            setQueryTdDto(dto, queryTdDto);
            //获取历史数据信息
            Map<String, Object> getHistoryData = getHistoryDataInfo(queryTdDto);
            if (getHistoryData.get("data") == null) {
                return Response.success(resultMap);
            }
            JSONArray array = JSON.parseArray(JSONObject.toJSONString(getHistoryData.get("data")));
            if (array.size() <= 0) {
                return Response.success(resultMap);
            }
            List<Map> mapList = new ArrayList<>();
            //查询换热站基础信息并组装历史数据
            if (dto.getLevel() == 1) {
                QueryBaseDataDto queryBaseDataDto = new QueryBaseDataDto() {{
                    setSystemIds(dto.getGroupId());
                    setSystemNumber(1);
                }};
                List<StationFirstNetBaseView> stations = getStationBaseInfo(queryBaseDataDto);
                for (Object object : array) {
                    JSONObject jsonObj = (JSONObject) JSONObject.toJSON(object);
                    StationFirstNetBaseView baseView = stations.stream().filter(s -> Objects.equals(s.getHeatSystemId().toString(), jsonObj.get("relevanceId").toString())).findFirst().orElse(null);
                    if (baseView != null) {
                        jsonObj.put("heatCabinetName", baseView.getHeatCabinetName());
                        jsonObj.put("heatTransferStationName", baseView.getHeatTransferStationName());
                        jsonObj.put("heatSystemName", baseView.getHeatSystemName());
                        mapList.add(jsonObj);
                    }
                }
            } else {
                //查询热源基础信息并组装历史数据
                QueryBaseDataDto queryBaseDataDto = new QueryBaseDataDto() {{
                    setSystemIds(dto.getGroupId());
                    setSystemNumber(0);
                }};
                List<SourceFirstNetBaseView> sources = getSourceBaseInfo(queryBaseDataDto);
                for (Object object : array) {
                    JSONObject jsonObj = (JSONObject) JSONObject.toJSON(object);
                    SourceFirstNetBaseView baseView = sources.stream().filter(s -> Objects.equals(s.getHeatSystemId().toString(), jsonObj.get("relevanceId").toString())).findFirst().orElse(null);
                    jsonObj.put("heatCabinetName", baseView.getHeatCabinetName());
                    jsonObj.put("heatSourceName", baseView.getHeatSourceName());
                    jsonObj.put("heatSystemName", baseView.getHeatSystemName());
                    mapList.add(jsonObj);
                }
            }
            resultMap.put("data", mapList);
            resultMap.put("total", getHistoryData.get("total"));
            return Response.success(resultMap);
        } catch (Exception e) {
            return Response.success(resultMap);
        }
    }

    /**
     * 获取热源基础数据信息
     */
    @Override
    public Response querySourceAllInfo(Integer id, Integer userId) {
        if (userId == -1) {
            QueryBaseDataDto dto = new QueryBaseDataDto();
            dto.setSystemNumber(-1);
            return Response.success(getSourceBaseInfo(dto));
        }
        return Response.success(getSourceByUserId(userId));
    }

    /**
     * 获取换热站基础数据信息
     */
    public List<StationFirstNetBaseView> getStationBaseInfo(QueryBaseDataDto dto) {
        return Arrays.stream(template.post("/common/queryStationBaseData", dto, baseServer, StationFirstNetBaseView[].class)).collect(Collectors.toList());
    }

    /**
     * 获取热源基础数据信息
     */
    public List<SourceFirstNetBaseView> getSourceBaseInfo(QueryBaseDataDto dto) {
        return Arrays.stream(template.post("/common/querySourceBaseData", dto, baseServer, SourceFirstNetBaseView[].class)).collect(Collectors.toList());
    }

    /**
     * 获取用户热源权限
     */
    public List<SourceFirstNetBaseView> getSourceByUserId(Integer userId) {
        List<SourceFirstNetBaseView> list = Arrays.asList(template.get("/common/querySourceInfoByUserId?id=" + userId, baseServer, SourceFirstNetBaseView[].class));
        return list;
    }

    /**
     * 获取Td历史基础数据
     */
    public Map getHistoryDataInfo(QueryTdDto dto) {
        return template.post("/tdEngineHistory/queryHistoryData", dto, gatherSearch, Map.class);
    }


    public void setQueryTdDto(QueryHistoryDataDto queryHistoryDataDto, QueryTdDto queryTdDto) {
        if (queryHistoryDataDto.getLevel() == 1) {
            if (queryHistoryDataDto.getDateType() == 1) {
                queryTdDto.setTableName(TdAggregateTableIndex.STATION_MINUTE.getIndex());
            } else if(queryHistoryDataDto.getDateType()==2){
                queryTdDto.setTableName(TdAggregateTableIndex.STATION_HOUR.getIndex());
            }else if(queryHistoryDataDto.getDateType()==3){
                queryTdDto.setTableName(TdAggregateTableIndex.STATION_HOUR_AVG.getIndex());
            }else {
                queryTdDto.setTableName(TdAggregateTableIndex.STATION_DAY_AVG.getIndex());
            }
        } else {
            if (queryHistoryDataDto.getDateType() == 1) {
                queryTdDto.setTableName(TdAggregateTableIndex.SOURCE_MINUTE.getIndex());
            } else if(queryHistoryDataDto.getDateType() == 2){
                queryTdDto.setTableName(TdAggregateTableIndex.SOURCE_HOUR.getIndex());
            }else if(queryHistoryDataDto.getDateType() == 3){
                queryTdDto.setTableName(TdAggregateTableIndex.SOURCE_HOUR_AVG.getIndex());
            }else {
                queryTdDto.setTableName(TdAggregateTableIndex.SOURCE_DAY_AVG.getIndex());
            }
        }
        queryTdDto.setStartTime(queryHistoryDataDto.getStartTime());
        queryTdDto.setEndTime(queryHistoryDataDto.getEndTime());
        queryTdDto.setPoints(queryHistoryDataDto.getPoints());
        queryTdDto.setGroupId(queryHistoryDataDto.getGroupId());
        queryTdDto.setQueryTypeTd(QueryTypeTd.history);
        tdHistoryPageTd(queryHistoryDataDto.getCurrentPage(), queryHistoryDataDto.getSize(), queryTdDto);
        tdHistorySortTypeTd(queryHistoryDataDto.getSortType(), queryTdDto);
    }

    private void tdHistoryPageTd(Integer currentPage, Integer size, QueryTdDto queryTdDto) {
        if (currentPage == null || size == null) {
            queryTdDto.setLimit(null);
            queryTdDto.setOffset(null);
        } else {
            queryTdDto.setLimit(size);
            if (currentPage > 1) {
                queryTdDto.setOffset(size * (currentPage - 1));
            } else {
                queryTdDto.setOffset(0);
            }
        }
    }

    private void tdHistorySortTypeTd(Boolean sortType, QueryTdDto queryTdDto) {
        if (sortType == null) {
            queryTdDto.setOrder("ASC");
            return;
        }
        if (sortType) {
            queryTdDto.setOrder("ASC");
        } else {
            queryTdDto.setOrder("DESC");
        }
    }
}
