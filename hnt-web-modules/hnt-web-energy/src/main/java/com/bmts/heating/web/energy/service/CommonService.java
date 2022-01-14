package com.bmts.heating.web.energy.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.bmts.heating.commons.basement.model.db.entity.*;
import com.bmts.heating.commons.basement.model.db.response.energy.ReachStandardResponse;
import com.bmts.heating.commons.entiy.baseInfo.cache.FirstNetBase;
import com.bmts.heating.commons.entiy.baseInfo.response.WeatherTempComparison;
import com.bmts.heating.commons.entiy.energy.EvalulateReachStandard;
import com.bmts.heating.commons.entiy.gathersearch.request.QueryEsBucketDto;
import com.bmts.heating.commons.entiy.gathersearch.request.TimeRange;
import com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine.GetEnergyPointConfig;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import com.bmts.heating.web.energy.pojo.EnergyCompareDto;
import com.bmts.heating.web.energy.pojo.EnergyInfoDto;
import com.bmts.heating.web.energy.service.config.EnergyUnitStandardConfigService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CommonService {
    @Autowired
    private TSCCRestTemplate template;

    private final String baseServer = "bussiness_baseInfomation";
    private final String gatherSearch = "gather_search";
    /**
     * 热、水、电
     */
    public static final String[] STATION_FIELDS = {"HM_HT", "WM_FT", "Ep"};
    public static final String[] SOURCE_FIELDS = {"HeatSourceTotalHeat_MtrG", "HeatSourceFTSupply", "HeatSourceEp"};

    /**
     * 获取0号系统所有网源站信息,0号系统不存在则获取一次侧
     *
     * @return FirstNetBase。。。
     */
    public List<StationFirstNetBaseView> filterFirstNetBase() {
//        return Arrays.asList(template.get("/common/firstNetBase/first", baseServer, StationFirstNetBaseView[].class));
        return Arrays.asList(template.get("/common/firstNetBase/firstSystemOne", baseServer, StationFirstNetBaseView[].class));

    }

    /**
     * 根据分公司id获取一次侧所有网源站信息
     *
     * @return FirstNetBase。。。
     */
    public List<StationFirstNetBaseView> filterFirstNetBaseByOrgId(Integer orgId) {
//        return Arrays.asList(template.get("/common/firstNetBase/first/" + orgId, baseServer, StationFirstNetBaseView[].class));
        return Arrays.asList(template.get("/common/firstNetBase/firstSystemOne/" + orgId, baseServer, StationFirstNetBaseView[].class));
    }

    public WeatherDay queryDayWeather(String day) {
        String json = template.get("/weather/day/" + day, baseServer, String.class);
        return JSONObject.parseObject(json, WeatherDay.class);

//        return template.get("/weather/day/" + day, baseServer, WeatherDay.class);
    }

    /**
     * 获取热源所有信息
     *
     * @return FirstNetBase。。。
     */
    public List<SourceFirstNetBaseView> filterFirstNetBaseSource() {
        return Arrays.stream(template.get("/common/sourceFirstNetBase", baseServer, SourceFirstNetBaseView[].class))
                .collect(Collectors.toList());
    }

    /**
     * 获取当前用户下所有有权限的站id
     *
     * @param id 用户id
     * @return station_id
     */
    public List<Integer> getStationByUserId(Integer id) {
        return Arrays.asList(template.get("/common/station/user/" + id, baseServer, Integer[].class));
    }

    /**
     * 根据当前的组织架构id获取当前组织下所有最小粒度的组织信息
     *
     * @param id 组织架构parent_id
     * @return 最底层的组织架构id
     */
    public List<Integer> getOrgByParentIds(List<Integer> id) {
        return Arrays.asList(template.post("/heatOrganizationService/orgIds", id, baseServer, Integer[].class));
    }

    /**
     * 查询对比天气预报
     *
     * @return WeatherTempComparison
     */
    public WeatherTempComparison weatherInfo() {
        HashMap map = (HashMap) template.get("common/weather/queryTempComparison", baseServer, Response.class).getData();
        return JSON.parseObject(JSON.toJSONString(map), WeatherTempComparison.class);
    }

    /**
     * 获取时间段均温
     *
     * @return 温度
     */
    public HashMap avgTemperature(TimeRange timeRange) {
        HashMap map;
        map = (HashMap) template.post("common/weather/avg", timeRange, baseServer, Response.class).getData();
        if (map != null) {
            return map;
        }
        return new HashMap();

    }

    /**
     * 获取配置折算信息
     *
     * @return EnergyUnitStandardConfig.class
     */
    public EnergyUnitStandardConfig queryUnitStandardConfig() {
        return template.get("/energy/unitStandard/info", baseServer, EnergyUnitStandardConfig.class);
    }


    /**
     * 查询达标数据
     *
     * @param evalulateReachStandard
     * @return
     */
    public List<ReachStandardResponse> queryReach(EvalulateReachStandard evalulateReachStandard) {
        try {
            List<ReachStandardResponse> result = Arrays.asList(template.doHttp("/energy/evaluate/home", evalulateReachStandard, gatherSearch, ReachStandardResponse[].class, HttpMethod.POST));
            return result;

        } catch (Exception e) {
            log.error("查询达标数据出错 {}", e);
            return new ArrayList<>();
        }
    }

    public List<Object> queryEnergy(QueryEsBucketDto dto) {
        Object[] postResult = template.doHttp("/energy/bucket", dto, gatherSearch, Object[].class, HttpMethod.POST);
        if (postResult == null) {
            return new ArrayList<>();
        }
        List<Object> objects = Arrays.asList(postResult);
        return objects;
    }

    /**
     * 获取水电热能耗点位信息
     *
     * @return FirstNetBase。。。
     */
    public GetEnergyPointConfig getEnergyPointConfig() {
        return template.get("/common/getEnergyPointConfig", baseServer, GetEnergyPointConfig.class);
    }

    /**
     * 获取当前日期供暖季信息
     *
     * @return FirstNetBase。。。
     */
    public Map getPresentHeatSeason() {
        Map commonHeatSeason= template.get("/common/getPresentHeatSeason", baseServer, Map.class);
        return commonHeatSeason;
    }

    //region source、net、station  route

//    /**
//     * 网、源、站基础信息获取
//     *
//     * @param type        1:热源 2:热网 3:热力站
//     * @param relevanceId 相关id
//     * @return Collect<FirstNetBase>
//     */
//    public List<StationFirstNetBaseView> route(Integer type, Integer relevanceId) {
//        List<SourceFirstNetBaseView> firstNetBases;
//        switch (type) {
//            case 1:
//                /*热源*/
//                firstNetBases = this.baseSource(relevanceId);
//                break;
//            case 2:
//                /*热网*/
//                firstNetBases = this.baseHeatNet(relevanceId);
//                break;
//            case 3:
//                /*热力站*/
//                firstNetBases = this.baseStation(relevanceId);
//                break;
//            default:
//                throw new RuntimeException(" {type} not available");
//        }
//        return firstNetBases;
//    }

    public List<SourceFirstNetBaseView> baseSource(Integer relevanceId) {
        List<SourceFirstNetBaseView> firstNetBases = this.filterFirstNetBaseSource();
        if (relevanceId != null) {
            firstNetBases = firstNetBases.stream().filter(e -> e.getHeatSourceId().equals(relevanceId)).collect(Collectors.toList());
        }
        return firstNetBases;
    }

    public List<StationFirstNetBaseView> baseHeatNet(Integer relevanceId) {
        List<StationFirstNetBaseView> firstNetBases = this.filterFirstNetBase();
        //源-站
        Map<Integer, List<StationFirstNetBaseView>> map = firstNetBases.stream().collect(Collectors.groupingBy(StationFirstNetBaseView::getHeatSourceId));
        List<StationFirstNetBaseView> firstNetBaseViews = new ArrayList<>(16);
        if (relevanceId != null) {
            List<NetSource> netSources = this.netJoinSource(relevanceId);
            for (NetSource source : netSources) {
                List<StationFirstNetBaseView> views = map.get(source.getSourceId());
                if (CollectionUtils.isNotEmpty(views)) {
                    firstNetBaseViews.addAll(views);
                }
            }
        } else {
            return firstNetBases;
        }
        return firstNetBaseViews;
    }

    public List<StationFirstNetBaseView> baseStation(Integer relevanceId) {
        List<StationFirstNetBaseView> firstNetBases = this.filterFirstNetBase();
        if (relevanceId != null) {
            firstNetBases = firstNetBases.stream().filter(e -> e.getHeatTransferStationId().equals(relevanceId)).collect(Collectors.toList());
        }
        return firstNetBases;
    }
    //endregion

    public List<StationFirstNetBaseView> metaStation(List<Integer> ids) {
        List<StationFirstNetBaseView> firstNetBases = this.filterFirstNetBase();
        if (CollectionUtils.isNotEmpty(ids)) {
            firstNetBases = firstNetBases.stream().filter(e -> ids.contains(e.getHeatTransferStationId())).collect(Collectors.toList());
        }
        return firstNetBases;
    }
    public List<SourceFirstNetBaseView> metaSource(List<Integer> ids) {
        List<SourceFirstNetBaseView> firstNetBases = this.filterFirstNetBaseSource();
        if (CollectionUtils.isNotEmpty(ids)) {
            firstNetBases = firstNetBases.stream().filter(e -> ids.contains(e.getHeatSystemId())).collect(Collectors.toList());
        }
        return firstNetBases;
    }
    //region route 水电热
    public String[] routeEnergySource(Integer energyType) {
        switch (energyType) {
            case 1:
                return new String[]{"HeatSourceFTSupply"};
            case 2:
                return new String[]{"HeatSourceEp"};
            case 3:
                return new String[]{"HeatSourceTotalHeat_MtrG"};
            default:
                throw new RuntimeException("param is blank");
        }
    }

    public String[] routeEnergyStation(Integer energyType) {
        switch (energyType) {
            case 1:
                return new String[]{"WM_FT"};
            case 2:
                return new String[]{"Ep"};
            case 3:
                return new String[]{"HM_HT"};
            default:
                throw new RuntimeException("param is blank");
        }
    }
    //endregion

    //region

    /**
     * 集合分页排序
     *
     * @param list List<E>
     * @param dto  页面入参 sartName 对应<E>的属性字段必须为数值类型
     * @param <E>  排序对象
     * @return E
     */
    public static <E> List<E> sorted(List<E> list, EnergyInfoDto dto) {
        int start = 0;
        int end = dto.getPageCount();
        if (dto.getCurrentPage() > 1) {
            start = (dto.getCurrentPage() - 1) * dto.getPageCount();
        }
        if (StringUtils.isNotEmpty(dto.getSortName())) {
            if (dto.isSortAsc()) {
                list = list
                        .stream()
                        .sorted(Comparator
                                .comparing(o -> getAttribute(o, dto.getSortName())))
                        .skip(start)
                        .limit(end)
                        .collect(Collectors
                                .toList());
            } else {
                list = list
                        .stream()
                        .sorted(Comparator
                                .comparing(o -> getAttribute(o, dto.getSortName())).reversed())
                        .skip(start)
                        .limit(end)
                        .collect(Collectors
                                .toList());
            }
        } else {
            list = list
                    .stream()
                    .skip(start)
                    .limit(end)
                    .collect(Collectors
                            .toList());
        }
        return list;
    }

    private static Double getAttribute(Object o, String field) {
        Double r = 0.0;
        try {
            Field f = o.getClass().getDeclaredField(field);
            f.setAccessible(true);
            r = (Double) f.get(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }
    //endregion

    //region 获取网-源关联信息

    public List<NetSource> netJoinSource(Integer id) {
        return Arrays.asList(template.get("/common/netJoinSource/" + id, baseServer, NetSource[].class));
    }

    //endRegion
}
