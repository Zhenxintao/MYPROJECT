package com.bmts.heating.web.balance.service.impl;

import com.bmts.heating.commons.basement.model.db.entity.BalanceMembers;
import com.bmts.heating.commons.basement.model.db.entity.LogOperationBalance;
import com.bmts.heating.commons.basement.model.db.entity.StationFirstNetBaseView;
import com.bmts.heating.commons.entiy.balance.pojo.balanceNet.BalanceNetAddDto;
import com.bmts.heating.commons.entiy.balance.pojo.balanceNet.BalanceNetSystemListDto;
import com.bmts.heating.commons.entiy.balance.pojo.balanceNet.BalanceTableQueryDto;
import com.bmts.heating.commons.entiy.balance.pojo.balanceNet.basement.BalanceLogDto;
import com.bmts.heating.commons.entiy.balance.pojo.balanceNet.basement.BalanceNet;
import com.bmts.heating.commons.entiy.baseInfo.cache.PointCache;
import com.bmts.heating.commons.entiy.baseInfo.cache.PointRank;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.entiy.gathersearch.request.DataRouteParam;
import com.bmts.heating.commons.entiy.gathersearch.response.cache.BalanceRealValue;
import com.bmts.heating.commons.entiy.gathersearch.response.cache.PointDetail;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.balance.service.BalanceNewBaseService;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class BalanceNetBaseServiceImpl implements BalanceNewBaseService {

    @Autowired
    private TSCCRestTemplate tsccRestTemplate;

    private final String baseUrl = "/netbalancebase";

    private final String applicationNetBalance = "application_netbalance";

    @Override
    public Response insert(BalanceNetAddDto balanceNetAddDto) {
        BalanceNet balanceNet = balanceNetAddDto.getBalanceNet();
        Response post = tsccRestTemplate.doHttp(baseUrl + "/edit", balanceNet, applicationNetBalance, Response.class, HttpMethod.POST);
        if (post.getData() != null) {
            Integer data = (Integer) post.getData();
            if (data > 0) {
                balanceNetAddDto.getBalanceNetLimits().forEach(e -> e.setBalanceNetId(data));
                return tsccRestTemplate.doHttp(baseUrl + "/config/limit", balanceNetAddDto.getBalanceNetLimits(), applicationNetBalance, Response.class, HttpMethod.POST);
            }
            Response.fail("基础信息保存失败");
        }
        return Response.fail();
    }

    @Override
    public Response delete(Integer id) {
        return tsccRestTemplate.delete(baseUrl + "/" + id, applicationNetBalance, Response.class);
    }

    @Override
    public Response query(int id) {
        return tsccRestTemplate.get(baseUrl + "/" + id, applicationNetBalance, Response.class);
    }

    @Override
    public Response query(Integer userId) {
        return tsccRestTemplate.get(baseUrl + "/all/" + userId, applicationNetBalance, Response.class);
    }

    @Override
    public Response querySystemList(BalanceNetSystemListDto dto) {
        int page = dto.getCurrentPage();
        int size = dto.getPageCount();
        //获取全部系统信息
        List<StationFirstNetBaseView> firstNetBases = Arrays.asList(tsccRestTemplate.get("/common/firstNetBase", "bussiness_baseInfomation", StationFirstNetBaseView[].class));
        //获取其他网加载过的系统
        List<BalanceMembers> balanceMembers = Arrays.asList(tsccRestTemplate.get("netbalance/memebers/contains", applicationNetBalance, BalanceMembers[].class));
        Set<Integer> collect = balanceMembers.stream().filter(e -> e.getLevel() == TreeLevel.HeatSystem.level()).map(BalanceMembers::getRelevanceId).collect(Collectors.toSet());
        Stream<StationFirstNetBaseView> firstNetBaseStream = firstNetBases
                .stream()
                .filter(e -> !"0".equals(e.getNumber()))
                .filter(e -> !collect.contains(e.getHeatSystemId()))
                .filter(e -> e.getHeatTransferStationId() != 0 && e.getHeatCabinetId() != 0)
                .filter(StationFirstNetBaseView::getStatus);
        //关键字过滤
        firstNetBaseStream = firstNetBaseStream.filter(e -> filterKeywords(e, dto.getKeyWord()));
        Set<StationFirstNetBaseView> res = firstNetBaseStream.collect(Collectors.toSet());
        //分页逻辑
        Set<StationFirstNetBaseView> firstNetBasesList = res.stream().sorted(Comparator.comparing(StationFirstNetBaseView::getHeatSystemId))
                .skip((page - 1) * size).limit((page) * size)//分页
                .collect(Collectors.toSet());
        HashMap<Object, Object> result = new HashMap<>();
        result.put("count", res.size());
        result.put("data", firstNetBasesList);
        return Response.success(result);
    }

    private Boolean filterKeywords(StationFirstNetBaseView e, String keyWord) {
        boolean b = StringUtils.containsIgnoreCase(e.getHeatTransferStationName(), keyWord);
        boolean b1 = StringUtils.containsIgnoreCase(e.getHeatSystemName(), keyWord);
        return b || b1;
    }

    private final String gatherSearch = "gather_search";
    private final String baseServer = "bussiness_baseInfomation";

    public List<StationFirstNetBaseView> firstNetBase() {
        return Arrays.asList(tsccRestTemplate.get("/common/firstNetBase", baseServer, StationFirstNetBaseView[].class));
    }

    public List<PointCache> monitorData(Map<Integer, String[]> mapRequest) {
        DataRouteParam param = new DataRouteParam();
        param.setMap(mapRequest);
        return Arrays.asList(tsccRestTemplate.post("/route/data", param, gatherSearch, PointCache[].class));
    }

    public List<BalanceMembers> getMembersByNet(Integer id, Integer level) {
        return Arrays.asList(tsccRestTemplate.get("/netbalance/members/" + id + "/" + level, applicationNetBalance, BalanceMembers[].class));
    }

    @Override
    public Map<String, Object> table(BalanceTableQueryDto dto) {
        List<StationFirstNetBaseView> firstNetBaseList = this.firstNetBase();
        if (StringUtils.isNotEmpty(dto.getKeyWord())) {
            //关键字过滤
            firstNetBaseList = firstNetBaseList.stream().filter(e -> filterKeywords(e, dto.getKeyWord())).collect(Collectors.toList());
        }
        List<BalanceMembers> membersByNet = getMembersByNet(dto.getBalanceNetId(), dto.getLevel() == null ? TreeLevel.HeatSystem.level() : dto.getLevel());
        int page = dto.getCurrentPage();
        int size = dto.getPageCount();

        //获取全部站点基础信息,并根据网信息过滤
        List<StationFirstNetBaseView> finalFirstNetBaseList = firstNetBaseList;
        List<BalanceRealValue> firstNetBaseListPart =
                membersByNet.stream().map(e -> filterBalance(e, finalFirstNetBaseList)).collect(Collectors.toList());
        //分页逻辑
        //计算数据条数
        long count = firstNetBaseListPart.size();
        if (StringUtils.isNotBlank(dto.getSortName())) {
            //按照参量排序
            firstNetBaseListPart = this.buildSort(dto, firstNetBaseListPart).stream()
                    .skip((page - 1) * size).limit(size)//分页
                    .collect(Collectors.toList());
        } else {
            //默认
            firstNetBaseListPart = firstNetBaseListPart.stream().sorted(Comparator.comparing(BalanceRealValue::getHeatSystemId))
                    .skip((page - 1) * size).limit(size)//分页
                    .collect(Collectors.toList());
        }

        //获取数据
        Map<String, Object> res = new HashMap<>();
        List<PointCache> pointCacheArrayList = null;
        if (firstNetBaseListPart.size() > 0) {
            pointCacheArrayList = this.monitorData(this.packageParam(firstNetBaseListPart, dto.getColumnName()));
            res.put("data", this.packageResult(pointCacheArrayList, firstNetBaseListPart));
        } else {
            res.put("data", null);
        }
        res.put("total", count);
        return res;
    }

    //获取全网平衡基础信息
    @Override
    public Response selNetBalanceInfo(int id) {
        return tsccRestTemplate.get("/netbalance/selNetBalanceInfo/" + id, applicationNetBalance, Response.class);
    }

    public PointRank getRank(String pointName, boolean isAsc) {
        return tsccRestTemplate.doHttp("/rank/".concat(pointName).concat("/").concat(String.valueOf(isAsc)), null, gatherSearch, PointRank.class, HttpMethod.GET);
    }

    public List<BalanceRealValue> buildSort(BaseDto dto, List<BalanceRealValue> source) {
        //排序
        PointRank rank = getRank(dto.getSortName(), dto.isSortAsc());
        if (rank != null) {
            List<BalanceRealValue> results = new LinkedList<>();
            List<BalanceRealValue> blanks = source.stream().filter(x -> !rank.getMap().containsKey(x.getHeatSystemId())).collect(Collectors.toList());
            if (dto.isSortAsc())
            //升序把空行前置
            {
                results.addAll(blanks);
            }
            rank.getMap().forEach((k, v) -> {
                source.stream().filter(x -> x.getHeatSystemId() == k).findFirst().ifPresent(results::add);
            });
            if (!dto.isSortAsc())
            //空行后置
            {
                results.addAll(blanks);
            }

            return results;
        }
        log.error("real data rank  returned null");
        return source.stream()
                .sorted(Comparator.comparing(BalanceRealValue::getHeatSystemId))//排序
                .skip((dto.getCurrentPage() - 1) * dto.getPageCount())
                .limit((dto.getCurrentPage()) * dto.getPageCount())//分页
                .collect(Collectors.toList());
    }

    private BalanceRealValue filterBalance(BalanceMembers e, List<StationFirstNetBaseView> firstNetBases) {
        BalanceRealValue balanceRealValue = new BalanceRealValue();
        firstNetBases.forEach(j -> {
            if (j.getHeatSystemId().equals(e.getRelevanceId())) {
                balanceRealValue.setCompanyName(j.getHeatStationOrgName());
                balanceRealValue.setStationName(j.getHeatTransferStationName());
                balanceRealValue.setSystemName(j.getHeatSystemName());
                balanceRealValue.setCabinetName(j.getHeatCabinetName());

//				balanceRealValue.setHeatNetArea(j.getHeatNetArea());
                balanceRealValue.setHeatSourceArea(j.getHeatSourceArea());
                balanceRealValue.setHeatStationArea(j.getHeatStationArea());
                balanceRealValue.setHeatSystemArea(j.getHeatSystemArea());
                balanceRealValue.setHeatStationNetArea(j.getHeatStationNetArea());

                balanceRealValue.setStationId(j.getHeatTransferStationId());
                balanceRealValue.setHeatSystemId(j.getHeatSystemId());
                balanceRealValue.setStatus(e.getStatus());
                balanceRealValue.setCompensation(e.getCompensation());
            }
        });
        return balanceRealValue;
    }


    //打包参数
    private Map<Integer, String[]> packageParam(List<BalanceRealValue> firstNetBaseList, String[] columnNames) {
        Map<Integer, String[]> paramMap = new HashMap<>();
        firstNetBaseList.forEach(e -> paramMap.put(e.getHeatSystemId(), columnNames));
        return paramMap;
    }

    //打包返回数据
    private List<BalanceRealValue> packageResult(List<PointCache> realData, List<BalanceRealValue> balanceRealValues) {
        Map<Integer, List<PointCache>> collect = realData.stream().collect(Collectors.groupingBy(PointCache::getRelevanceId));
        List<BalanceRealValue> res = new ArrayList<>();
        balanceRealValues.forEach(e -> {
            if (collect.get(e.getHeatSystemId()) != null) {
                List<PointDetail> pointDetails = collect.get(e.getHeatSystemId()).stream().map(point -> {
                    e.setQualityStrap(point.getQualityStrap());
                    e.setTimeStamp(getDateTimeOfTimestamp(point.getTimeStrap()));
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
                e.setPointDetailList(pointDetails);
            }
            res.add(e);
        });
        return res;
    }


    public static LocalDateTime getDateTimeOfTimestamp(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    @Override
    public Response list() {
        return tsccRestTemplate.get(baseUrl + "/select", applicationNetBalance, Response.class);
    }

    @Override
    public Response insertLog(LogOperationBalance dto) {
        return tsccRestTemplate.doHttp("/netbalancebase/insertLogOperationBalance", dto, applicationNetBalance, Response.class, HttpMethod.POST);
    }

    @Override
    public Response insertLogById(BalanceLogDto dto) {
        return tsccRestTemplate.doHttp("/netbalancebase/insertLogById", dto, applicationNetBalance, Response.class, HttpMethod.POST);
    }

    @Override
    public Response insertLogByIds(BalanceLogDto dto) {
        return tsccRestTemplate.doHttp("/netbalancebase/insertLogByIds", dto, applicationNetBalance, Response.class, HttpMethod.POST);
    }
}
