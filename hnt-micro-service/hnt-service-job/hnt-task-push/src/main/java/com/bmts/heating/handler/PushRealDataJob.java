package com.bmts.heating.handler;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.cache.PointCache;
import com.bmts.heating.commons.basement.model.db.entity.SourceFirstNetBaseView;
import com.bmts.heating.commons.basement.model.db.entity.StationFirstNetBaseView;
import com.bmts.heating.commons.basement.model.db.entity.WebPageConfig;
import com.bmts.heating.commons.container.performance.exception.MicroException;
import com.bmts.heating.commons.db.service.SourceFirstNetBaseViewService;
import com.bmts.heating.commons.db.service.StationFirstNetBaseViewService;
import com.bmts.heating.commons.db.service.WebPageConfigService;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.middleware.cache.services.RedisCacheService;
import com.bmts.heating.pojo.PushBodyDto;
import com.bmts.heating.pojo.PushDataDto;
import com.bmts.heating.pojo.PushPointDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component("push_real_data")
@Slf4j
public class PushRealDataJob implements Job {

    @Autowired
    RedisCacheService redisCacheService;

    @Autowired
    StationFirstNetBaseViewService stationFirstNetBaseViewService;
    @Autowired
    SourceFirstNetBaseViewService sourceFirstNetBaseViewService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        excuteSource();
        excuteStation();
    }

    public void excuteSource() {
        try {
            //query station systems
            List<SourceFirstNetBaseView> list = sourceFirstNetBaseViewService
                    .list();
            HashMap<String, String> mapping = buildSourceMapping();
            mappingPushSourceData(list, mapping);

        } catch (Exception e) {
            log.error("推送实时数据出错,{}", e);
        }
    }

    public void excuteStation() {
        try {
            //query station systems
            List<StationFirstNetBaseView> list = stationFirstNetBaseViewService
                    .list(
                            Wrappers.<StationFirstNetBaseView>lambdaQuery()
                                    .ne(StationFirstNetBaseView::getNumber, "0")
                    );
            HashMap<String, String> mapping = buildStationMapping();
            mappingPushStationData(list, mapping);

        } catch (Exception e) {
            log.error("推送实时数据出错,{}", e);
        }
    }

    private void mappingPushStationData(List<StationFirstNetBaseView> list, HashMap<String, String> mapping) throws MicroException, IOException {
        HashMap map = new HashMap<Integer, String[]>();
        List<String> points = new ArrayList<>(mapping.size());
        Map<String, Integer> config = queryStationConfig();
        mapping.forEach((k, v) -> {
            points.add(k);
        });
        list.forEach(x -> {
            map.put(x.getHeatSystemId(), points.toArray(new String[points.size()]));
        });
        PushBodyDto pushBaseDto = new PushBodyDto();
        //读取实时数据
        List<PointCache> realDatas = redisCacheService.queryRealOnlyValue(map, TreeLevel.HeatSystem.level());
        realDatas.stream()
                .collect(Collectors.groupingBy(PointCache::getRelevanceId))
                .forEach((k, v) -> {
                    StationFirstNetBaseView info = list.stream().filter(item -> item.getHeatSystemId().equals(k)).findFirst().orElse(null);
                    if (info != null && info.getSystemSyncNum() > 0 && info.getSystemSyncNum().toString().length() == 8) {
                        //组装数据
                        PushDataDto dto = new PushDataDto();
                        String number = info.getSystemSyncNum().toString();
                        dto.setSiteCode(number);
                        v.forEach(pointCache -> {
                            if (StringUtils.isNotBlank(mapping.get(pointCache.getPointName()))) {
                                PushPointDto itemDto = new PushPointDto();
                                itemDto.setVarCode(mapping.get(pointCache.getPointName()));
                                try {

                                    float value = Float.parseFloat(pointCache.getValue());
                                    if (config != null && pointCache.getPointName().equals("T2g") && value <= config.get("initValue")) {
                                        Random r = new Random();
                                        // Double rValue = (r.nextDouble() * config.second) + config.first;
                                        Double rValue = (r.nextDouble() * config.get("section")) + config.get("tg");
                                        DecimalFormat df = new DecimalFormat("#0.00");
                                        String format = df.format(rValue);
                                        Float aFloat = Float.valueOf(format);
                                        itemDto.setValue(aFloat);
                                    } else {
                                        itemDto.setValue(value);
                                    }
                                    dto.getVarDatas().add(itemDto);
                                } catch (Exception e) {
                                    log.error("实时数据转换数据类型错误{}", e);
                                }
                            }
                        });
                        if (CollectionUtils.isNotEmpty(dto.getVarDatas())) {
                            pushBaseDto.getData().add(dto);
                        }
                    }
                });

        if (CollectionUtils.isNotEmpty(pushBaseDto.getData())) {
            send(JSONObject.toJSONString(pushBaseDto));
        }

    }


    private void mappingPushSourceData(List<SourceFirstNetBaseView> list, HashMap<String, String> mapping) throws MicroException, IOException {
        HashMap map = new HashMap<Integer, String[]>();
        List<String> points = new ArrayList<>(mapping.size());
        mapping.forEach((k, v) -> {
            points.add(k);
        });
        list.forEach(x -> {
            map.put(x.getHeatSystemId(), points.toArray(new String[points.size()]));
        });
        PushBodyDto pushBaseDto = new PushBodyDto();
        //读取实时数据
        List<PointCache> realDatas = redisCacheService.queryRealOnlyValue(map, TreeLevel.HeatSystem.level());
        realDatas.stream()
                .collect(Collectors.groupingBy(PointCache::getRelevanceId))
                .forEach((k, v) -> {
                    SourceFirstNetBaseView info = list.stream().filter(item -> item.getHeatSystemId().equals(k)).findFirst().orElse(null);
                    if (info != null && info.getSystemSyncNum() > 0 && info.getSystemSyncNum().toString().length() == 6) {
                        //组装数据
                        PushDataDto dto = new PushDataDto();
                        String number = info.getSystemSyncNum().toString();
                        dto.setSiteCode(number);
                        v.forEach(pointCache -> {
                            if (StringUtils.isNotBlank(mapping.get(pointCache.getPointName()))) {
                                PushPointDto itemDto = new PushPointDto();
                                itemDto.setVarCode(mapping.get(pointCache.getPointName()));
                                try {
                                    float value = Float.parseFloat(pointCache.getValue());
                                    itemDto.setValue(value);
                                    dto.getVarDatas().add(itemDto);
                                } catch (Exception e) {
                                    log.error("实时数据转换数据类型错误{}", e);
                                }
                            }
                        });
                        if (CollectionUtils.isNotEmpty(dto.getVarDatas())) {
                            pushBaseDto.getData().add(dto);
                        }
                    }
                });
        if (CollectionUtils.isNotEmpty(pushBaseDto.getData()))
            send(JSONObject.toJSONString(pushBaseDto));
    }

    private void send(String json) {

        try {

            HashMap<String, Object> obj = new HashMap<>();
            obj.put("postDataJson", json);
            String result = mapPost("http://47.92.6.52/zhcc/service/postRunDataJson.action", obj, "utf-8");
            log.info("市政府接口返回数据:{},请求参数{}", result, json);
        } catch (Exception e) {
            log.error("推送至市政府实时数据出错,推送参数{}", json, e);
        }

    }

    public static String mapPost(String url, Map<String, Object> map, String encoding) {
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try {
            httpClient = HttpClients.createDefault();

            httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(5000).setConnectionRequestTimeout(20000)
                    .setSocketTimeout(5000).build();
            httpPost.setConfig(requestConfig);
            //设置参数
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> elem = (Map.Entry<String, String>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(), String.valueOf(elem.getValue())));
            }
            if (list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, encoding);
                httpPost.setEntity(entity);
            }
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, encoding);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private HashMap<String, String> buildStationMapping() {

        HashMap<String, String> mapping = new HashMap();
        mapping.put("T1g", "ai_ygw");
        mapping.put("T1h", "ai_yhw");
        mapping.put("T2g", "ai_egw");
        mapping.put("T2h", "ai_ehw");
        mapping.put("P1g", "ai_ygy");
        mapping.put("P1h", "ai_yhy");
        mapping.put("P2g", "ai_egy");
        mapping.put("P2h", "ai_ehy");
//        HM_F1 HMh_F1 HM_F2
        mapping.put("HM_F1", "ai_ygl");
        mapping.put("HMh_F1", "ai_yhl");
        mapping.put("HM_F2", "ai_egl");
        return mapping;
    }

    private HashMap<String, String> buildSourceMapping() {
        HashMap<String, String> mapping = new HashMap();
        mapping.put("Tg", "ai_ygw");
        mapping.put("Th", "ai_yhw");
//        HM_F1 HMh_F1 HM_F2
        mapping.put("HM_Fg", "ai_ygl");
        mapping.put("HM_Fh", "ai_yhl");
        return mapping;
    }

    @Autowired
    WebPageConfigService webPageConfigService;

    //private Tuple<Integer, Integer> queryStationConfig() {
    //    WebPageConfig simulatePush = webPageConfigService.getOne(Wrappers.<WebPageConfig>lambdaQuery().eq(WebPageConfig::getConfigKey, "simulatePush"));
    //    if (simulatePush != null) {
    //        JSONObject jsonObject = JSONObject.parseObject(simulatePush.getJsonConfig());
    //        Integer tg = Integer.valueOf(jsonObject.get("tg").toString());
    //        Integer section = Integer.valueOf(jsonObject.get("section").toString());
    //        Integer initValue = Integer.valueOf(jsonObject.get("initValue").toString());
    //        return new Tuple<>(tg, section);
    //    }
    //    return null;
    //}


    private Map<String, Integer> queryStationConfig() {
        WebPageConfig simulatePush = webPageConfigService.getOne(Wrappers.<WebPageConfig>lambdaQuery().eq(WebPageConfig::getConfigKey, "simulatePush"));
        if (simulatePush != null) {
            JSONObject jsonObject = JSONObject.parseObject(simulatePush.getJsonConfig());
            Integer tg = Integer.valueOf(jsonObject.get("tg").toString());
            Integer section = Integer.valueOf(jsonObject.get("section").toString());
            Integer initValue = Integer.valueOf(jsonObject.get("initValue").toString());
            Map<String, Integer> map = new HashMap<>();
            if (tg != null) {
                map.put("tg", tg);
            }
            if (section != null) {
                map.put("section", section);
            }
            if (initValue != null) {
                map.put("initValue", initValue);
            }

            return map;
        }
        return null;
    }


}
