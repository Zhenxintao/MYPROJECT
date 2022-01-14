package com.bmts.heating.bussiness.baseInformation.app.handler;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.cache.PointCache;
import com.bmts.heating.commons.basement.model.db.entity.EsDocConfig;
import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
import com.bmts.heating.commons.db.service.EsDocConfigService;
import com.bmts.heating.commons.db.service.PointStandardService;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import com.bmts.heating.commons.history.pojo.EsDataType;
import com.bmts.heating.commons.history.service.PointInputService;
import com.bmts.heating.commons.utils.es.EsEnergyIndex;
import com.bmts.heating.commons.utils.es.EsIndex;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.bmts.heating.commons.redis.utils.RedisKeyConst.First_REAL_DATA;

/**
 * @author naming
 * @description
 * @date 2021/1/12 11:19
 **/
@EnableAsync
@Service
@Slf4j
@EnableScheduling
public class EsHandler {
    /**
     * 新增es配置列
     *
     * @param pointStandardId 标准点Id
     * @param type 数据类型
     */
    @Autowired
    PointStandardService pointStandardService;
    @Autowired
    EsDocConfigService esDocConfigService;
    @Autowired
    PointInputService pointInputService;
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Async
    public void configColumn(int pointStandardId, int dataType) {
        try {
            PointStandard pointStandard = pointStandardService.getById(pointStandardId);
            if (pointStandard == null) {
                log.error("param error caused by eshandler");
                return;
            }
            EsDocConfig one = esDocConfigService.getOne(Wrappers.<EsDocConfig>lambdaQuery().eq(EsDocConfig::getPointName, pointStandard.getColumnName()));
            if (one == null) {
                //插入操作
                EsDocConfig esDocConfig = new EsDocConfig();
                esDocConfig.setPointName(pointStandard.getColumnName());
                esDocConfig.setDataType(dataType);
                if (!esDocConfigService.save(esDocConfig))
                    log.error("save es_doc_config error");
                //调用es jar包动态mapping
                Map<String, String> map = new HashMap<>();
                map.put(esDocConfig.getPointName(), EsDataType.getName(dataType));
                log.info("当前索引mapping--------||{}||", pointInputService.addFields(EsIndex.FIRST_REAL_DATA, map));
                log.info("当前索引mapping--------||{}||", pointInputService.addFields(EsIndex.FIRST_HOUR_AVG, map));
                log.info("当前索引mapping--------||{}||", pointInputService.addFields(EsIndex.FIRST_DAY, map));
                log.info("当前索引mapping--------||{}||", pointInputService.addFields(EsIndex.FIRST_HOUR, map));
            }
        } catch (Exception e) {
            log.error("eshandler deal db caused exception {}", e);
        }
    }

    @Async
    public void configColumn(String pointStandardName, int dataType) {
        if (StringUtil.isNullOrEmpty(pointStandardName)) {
            log.error("param error caused by eshandler");
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put(pointStandardName, EsDataType.getName(dataType));
        log.info("当前索引mapping--------||{}||", pointInputService.addFields(EsIndex.FIRST_REAL_DATA, map));
        log.info("当前索引mapping--------||{}||", pointInputService.addFields(EsIndex.FIRST_HOUR_AVG, map));
        log.info("当前索引mapping--------||{}||", pointInputService.addFields(EsIndex.FIRST_DAY, map));
        log.info("当前索引mapping--------||{}||", pointInputService.addFields(EsIndex.FIRST_HOUR, map));

    }

    @Async
    public void configEnergyColumn(String pointStandardName) {
        if (StringUtil.isNullOrEmpty(pointStandardName)) {
            log.error("param error caused by eshandler");
            return;
        }
        log.info("当前索引mapping--------||{}||", pointInputService.addEnergyFields(EsEnergyIndex.ENERGY_CONVERGE_HOUR, pointStandardName));
        log.info("当前索引mapping--------||{}||", pointInputService.addEnergyFields(EsEnergyIndex.ENERGY_CONVERGE_DAY, pointStandardName));

    }

}
