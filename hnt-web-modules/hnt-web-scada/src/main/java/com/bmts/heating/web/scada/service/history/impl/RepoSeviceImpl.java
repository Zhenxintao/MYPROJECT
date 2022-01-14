package com.bmts.heating.web.scada.service.history.impl;

import com.bmts.heating.commons.basement.model.db.entity.StationFirstNetBaseView;
import com.bmts.heating.commons.entiy.baseInfo.cache.FirstNetBase;
import com.bmts.heating.commons.entiy.gathersearch.request.ProductReportDto;
import com.bmts.heating.commons.entiy.gathersearch.request.QueryEsDto;
import com.bmts.heating.commons.entiy.gathersearch.request.SingleStationRepoDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import com.bmts.heating.web.scada.service.history.RepoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RepoSeviceImpl implements RepoService {

    @Autowired
    private TSCCRestTemplate template;

    private final String gatherSearch = "gather_search";

    private final String baseServer = "bussiness_baseInfomation";

    @Override
    public Response table(SingleStationRepoDto singleStationRepoDto) {
        try {
            if (singleStationRepoDto.getStationId() == null) {
                return Response.paramError();
            }
            QueryEsDto dto = new QueryEsDto();
            List<FirstNetBase> firstNetBases = this.filterFirstNetBase(singleStationRepoDto.getStationId());
            dto.setHeatSystemId(firstNetBases
                    .stream()
                    .filter(FirstNetBase::isStatus)
                    .map(FirstNetBase::getHeatSystemId)
                    .distinct().toArray(Integer[]::new));
            this.packageSingleParam(singleStationRepoDto, dto);
            Map[] post = template.post("/historySearch/table", dto, gatherSearch, Map[].class);
            List<Map> maps = new ArrayList<>();
            if (post != null && post.length > 0) {
                maps = Arrays.asList(post);
            }else {
                Map<String, Object> resMap = new HashMap();
                return Response.success(resMap);
            }
            List<Map> arrList = new ArrayList<>(maps);
            Map<String, Object> resMap = new HashMap();
            Map<Object, List<Map>> groupMap = arrList.stream().collect(Collectors.groupingBy(s -> s.get("timeStrap") == null ? "totalNull" : s.get("timeStrap")));
            List<Map> responseList = new ArrayList<>();
            resMap.put("total", Integer.valueOf(groupMap.get("totalNull").get(0).get("total").toString()));
            groupMap.remove("totalNull");
            groupMap.forEach((time, value) -> this.contains(firstNetBases, value));
            groupMap.forEach((time, value) -> responseList.addAll(value));
            resMap.put("data", responseList);
            return Response.success(resMap);
        } catch (Exception e) {
            return Response.fail();
        }

    }

//    @Override
//    public Response table(SingleStationRepoDto singleStationRepoDto) {
//        if (singleStationRepoDto.getStationId() == null) {
//            return Response.paramError();
//        }
//        QueryEsDto dto = new QueryEsDto();
//        List<FirstNetBase> firstNetBases = this.filterFirstNetBase(singleStationRepoDto.getStationId());
//        dto.setHeatSystemId(firstNetBases
//                .stream()
//                .filter(FirstNetBase::isStatus)
//                .map(FirstNetBase::getHeatSystemId)
//                .distinct().toArray(Integer[]::new));
//        this.packageSingleParam(singleStationRepoDto, dto);
//        Map[] post = template.post("/historySearch/table", dto, gatherSearch, Map[].class);
//        List<Map> maps = new ArrayList<>();
//        if (post != null && post.length > 0) {
//            maps = Arrays.asList(post);
//        }
//        List<Map> arrList = new ArrayList<>(maps);
//        Map<String, Object> resMap = new HashMap();
//        arrList.forEach(map -> this.contains(firstNetBases, map, resMap));
//        arrList.remove(arrList.size() - 1);
//        resMap.put("data", arrList);
//        return Response.success(resMap);
//    }

    @Override
    public Response productReport(ProductReportDto param) {
        try {
            if (CollectionUtils.isEmpty(param.getStationId())) {
                return Response.success();
            }
            QueryEsDto dto = new QueryEsDto();
            List<FirstNetBase> firstNetBases = this.filterFirstNetBases(param.getStationId());
            dto.setHeatSystemId(firstNetBases
                    .stream()
                    .filter(FirstNetBase::isStatus)
                    .map(FirstNetBase::getHeatSystemId)
                    .distinct().toArray(Integer[]::new));
            this.packageAllParam(param, dto);
            Map[] post = template.post("/historySearch/table", dto, gatherSearch, Map[].class);
            List<Map> maps = new ArrayList<>();
            if (post != null && post.length > 0) {
                maps = Arrays.asList(post);
            }
            else {
                return Response.success();
            }
            List<Map> arrList = new ArrayList<>(maps);
            Map<String, Object> resMap = new HashMap();
            resMap.put("total", Integer.valueOf(arrList.get(arrList.size() - 1).get("total").toString()));
            arrList.remove(arrList.size() - 1);
            for (Integer id : param.getStationId()) {
                List<FirstNetBase> groupStation = firstNetBases.stream().filter(s -> id.equals(s.getHeatTransferStationId())).collect(Collectors.toList());
                List<Map> mapList = new ArrayList<>();
                for (FirstNetBase firstNetBase : groupStation) {
                    arrList.forEach(map -> {
                        if (map.size() == 0) {
                            return;
                        }
                        Integer systemId = Integer.valueOf(map.get("relevanceId").toString());
                        if (systemId == firstNetBase.getHeatSystemId()) {
                            mapList.add(map);
                        }
                    });
                }
                if (mapList.size() > 0) {
                    this.contains(groupStation, mapList);
                }
            }
            List<Map> responseMapList = arrList.stream().filter(s -> s.size() > 0).collect(Collectors.toList());
            resMap.put("data", responseMapList);
            return Response.success(resMap);
        } catch (Exception e) {
            return  Response.success();
        }

    }

    public List<FirstNetBase> filterFirstNetBase(Integer stationId) {
        return Arrays.stream(template.get("/common/firstNetBase/", baseServer, FirstNetBase[].class)).filter(e -> e.getHeatTransferStationId() == stationId).collect(Collectors.toList());
    }

    public List<FirstNetBase> filterFirstNetBases(List<Integer> stationIds) {
        FirstNetBase[] firstNetBases = template.get("/common/firstNetBase/", baseServer, FirstNetBase[].class);
        List<FirstNetBase> list = Arrays.stream(firstNetBases).filter(e -> stationIds.contains(e.getHeatTransferStationId())).collect(Collectors.toList());
        return list;
    }

    private void contains(List<FirstNetBase> firstNetBases, List<Map> value) {
        FirstNetBase base = firstNetBases.stream().filter(s -> s.getNumber().equals("0")).findFirst().orElse(null);
        Map zeroMap = value.stream().filter(s -> s.get("relevanceId").toString().equals(base.getHeatSystemId() + "")).findFirst().orElse(null);
        if (zeroMap != null) {
            zeroMap.remove("relevanceId");
            zeroMap.remove("timeStrap");
            value.remove(zeroMap);
            for (FirstNetBase firstNetBase : firstNetBases.stream().filter(s -> !s.getNumber().equals("0")).collect(Collectors.toList())) {
                for (Map map : value) {
                    if (map.get("relevanceId").equals(firstNetBase.getHeatSystemId() + "")) {
                        map.put("heatTransferStationName", firstNetBase.getHeatTransferStationName());
                        map.put("heatCabinetName", firstNetBase.getHeatCabinetName());
                        map.put("heatSystemName", firstNetBase.getHeatSystemName());
                        map.put("heatSystemArea", firstNetBase.getHeatSystemArea());
                        map.putAll(zeroMap);
                        return;
                    }
                }
            }
        } else {
            for (FirstNetBase firstNetBase : firstNetBases.stream().filter(s -> !s.getNumber().equals("0")).collect(Collectors.toList())) {
                for (Map map : value) {
                    if (map.get("relevanceId").equals(firstNetBase.getHeatSystemId() + "")) {
                        map.put("heatTransferStationName", firstNetBase.getHeatTransferStationName());
                        map.put("heatCabinetName", firstNetBase.getHeatCabinetName());
                        map.put("heatSystemName", firstNetBase.getHeatSystemName());
                        map.put("heatSystemArea", firstNetBase.getHeatSystemArea());
                        return;
                    }
                }
            }
        }
    }

//    private void contains(List<FirstNetBase> firstNetBases, Map<java.io.Serializable, java.io.Serializable> map, Map<String, Object> resMap) {
//        for (FirstNetBase firstNetBase : firstNetBases) {
//            if (map.get("relevanceId") == null && map.get("total") != null) {
//                resMap.put("total", Integer.parseInt((String) map.get("total")));
//            } else if (map.get("relevanceId").equals(firstNetBase.getHeatSystemId() + "")) {
//                map.put("heatTransferStationName", firstNetBase.getHeatTransferStationName());
//                map.put("heatCabinetName", firstNetBase.getHeatCabinetName());
//                map.put("heatSystemName", firstNetBase.getHeatSystemName());
//                map.put("heatSystemArea", firstNetBase.getHeatSystemArea());
//                return;
//            }
//        }
//    }

    private void packageSingleParam(SingleStationRepoDto singleStationRepoDto, QueryEsDto dto) {
        //时间处理
        if (singleStationRepoDto.getStartTime() != null) {
            dto.setStart(singleStationRepoDto.getStartTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        } else {
            LocalDateTime now = LocalDateTime.now();
            dto.setStart(now.plusHours(8).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        }
        if (singleStationRepoDto.getEndTime() != null) {
            dto.setEnd(singleStationRepoDto.getEndTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        } else {
            LocalDateTime now = LocalDateTime.now();
            dto.setEnd(now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        }
        this.chooseQueryType(singleStationRepoDto.getQueryType(), dto);
        this.chooseSourceType(singleStationRepoDto.getSourceType(), dto);

        if (!CollectionUtils.isEmpty(singleStationRepoDto.getFields())) {
            List<String> fields = singleStationRepoDto.getFields();
            fields.add("relevanceId");
            fields.add("timeStrap");
            dto.setIncludeFields(fields.toArray(new String[0]));
        }
        //分页处理
        dto.setCurrentPage(singleStationRepoDto.getCurrentPage());
        dto.setSize(singleStationRepoDto.getSize());
        dto.setField(singleStationRepoDto.getField());
        dto.setSortType(singleStationRepoDto.getSortType());
    }

    private void packageAllParam(ProductReportDto proDto, QueryEsDto dto) {
        //时间处理
        if (proDto.getDateTime() != null) {
            dto.setStart(proDto.getDateTime().atTime(0, 0, 0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - 1);
            dto.setEnd(proDto.getDateTime().atTime(0, 0, 0).plusDays(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - 1);
        } else {
            LocalDate now = LocalDate.now();
            LocalDateTime localDateTime = now.atTime(0, 0, 0);
            dto.setStart(localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - 1);
            dto.setEnd(localDateTime.plusDays(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() - 1);
        }

        this.chooseQueryType(proDto.getQueryType(), dto);
        this.chooseSourceType(proDto.getSourceType(), dto);

        if (!CollectionUtils.isEmpty(proDto.getFields())) {
            List<String> fields = proDto.getFields();
            fields.add("relevanceId");
            fields.add("timeStrap");
            dto.setIncludeFields(fields.toArray(new String[0]));
        }
        //分页处理
        dto.setCurrentPage(proDto.getCurrentPage());
        dto.setSize(proDto.getSize());
        dto.setField(proDto.getField());
        dto.setSortType(proDto.getSortType());
    }

    private void chooseQueryType(int flag, QueryEsDto dto) {
        switch (flag) {
            case 1:
                dto.realData();
                break;
            case 2:
                dto.hour();
                break;
            case 3:
                dto.hourAvg();
                break;
            case 4:
                dto.day();
                break;
        }
    }

    private void chooseSourceType(int flag, QueryEsDto dto) {
        switch (flag) {
            case 1:
                dto.routeFirst();
                break;
            case 2:
                dto.routeSecond();
                break;
            case 3:
                dto.routeIndoorTemp();
                break;
            case 4:
                dto.routeEnergy();
                break;
        }
    }
}
