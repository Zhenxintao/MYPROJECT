package com.bmts.heating.commons.db.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmts.heating.commons.basement.model.db.entity.EquipmentInfo;
import com.bmts.heating.commons.basement.model.db.entity.PointConfig;
import com.bmts.heating.commons.basement.model.db.entity.PointStandard;
import com.bmts.heating.commons.basement.model.db.entity.SourceFirstNetBaseView;
import com.bmts.heating.commons.db.mapper.EquipmentInfoMapper;
import com.bmts.heating.commons.db.service.EquipmentInfoService;
import com.bmts.heating.commons.db.service.PointConfigService;
import com.bmts.heating.commons.db.service.PointStandardService;
import com.bmts.heating.commons.db.service.SourceFirstNetBaseViewService;
import com.bmts.heating.commons.entiy.common.TreeLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author naming
 * @since 2020-11-09
 */
@Service
public class EquipmentInfoServiceImpl extends ServiceImpl<EquipmentInfoMapper, EquipmentInfo> implements EquipmentInfoService {

    // public static String[] STATION_FIELDS_INIT = {"HM_HT", "WM_FT", "Ep"};
    public static String[] SOURCE_FIELDS_INIT = {"HeatSourceTotalHeat_MtrG", "HeatSourceFTSupply", "HeatSourceEp"};

    @Autowired
    private PointStandardService pointStandardService;

    @Autowired
    private SourceFirstNetBaseViewService sourceFirstNetBaseViewService;
    @Autowired
    private PointConfigService pointConfigService;

    @Override
    public boolean configSourcePoint() {
        PointStandard mtrg = pointStandardService.getOne(Wrappers.<PointStandard>lambdaQuery().eq(PointStandard::getColumnName, SOURCE_FIELDS_INIT[0])
                .eq(PointStandard::getLevel, 2));
        PointStandard ftsupply = pointStandardService.getOne(Wrappers.<PointStandard>lambdaQuery().eq(PointStandard::getColumnName, SOURCE_FIELDS_INIT[1])
                .eq(PointStandard::getLevel, 2));
        PointStandard sourceEp = pointStandardService.getOne(Wrappers.<PointStandard>lambdaQuery().eq(PointStandard::getColumnName, SOURCE_FIELDS_INIT[2])
                .eq(PointStandard::getLevel, 2));
        // 查询所有热源
        // 获取热源 关系数据
        List<SourceFirstNetBaseView> listSource = sourceFirstNetBaseViewService.list();
        Map<Integer, List<SourceFirstNetBaseView>> sourceMap = listSource.stream().collect(Collectors.groupingBy(e -> e.getHeatSourceId()));
        for (Integer sourceId : sourceMap.keySet()) {
            List<SourceFirstNetBaseView> listSystem = sourceMap.get(sourceId);
            for (SourceFirstNetBaseView sourceSystem : listSystem) {
                if (sourceSystem.getNumber() == 0) {
                    List<PointConfig> pointConfigList = new ArrayList<>();
                    LocalDateTime nowTime = LocalDateTime.now();
                    // 先查询是否有这些点，如果没有然后再进行配置
                    List<PointConfig> list = pointConfigService.list(Wrappers.<PointConfig>lambdaQuery().eq(PointConfig::getRelevanceId, sourceSystem.getHeatSystemId()));
                    PointConfig pointMtrg = list.stream().filter(e -> Objects.equals(e.getPointStandardId(), mtrg.getId())).findFirst().orElse(null);
                    if (pointMtrg == null) {
                        PointConfig pointConfig = new PointConfig();
                        pointConfig.setPointStandardId(mtrg.getId());
                        pointConfig.setRelevanceId(sourceSystem.getHeatSystemId());
                        pointConfig.setCreateTime(nowTime);
                        pointConfig.setLevel(TreeLevel.HeatSystem.level());
                        pointConfig.setCreateUser("数据初始化配置");
                        pointConfigList.add(pointConfig);

                    }
                    PointConfig pointFtsupply = list.stream().filter(e -> Objects.equals(e.getPointStandardId(), ftsupply.getId())).findFirst().orElse(null);
                    if (pointFtsupply == null) {
                        PointConfig pointConfig = new PointConfig();
                        pointConfig.setPointStandardId(ftsupply.getId());
                        pointConfig.setRelevanceId(sourceSystem.getHeatSystemId());
                        pointConfig.setCreateTime(nowTime);
                        pointConfig.setLevel(TreeLevel.HeatSystem.level());
                        pointConfig.setCreateUser("数据初始化配置");
                        pointConfigList.add(pointConfig);
                    }
                    PointConfig pointSourceEp = list.stream().filter(e -> Objects.equals(e.getPointStandardId(), sourceEp.getId())).findFirst().orElse(null);
                    if (pointSourceEp == null) {
                        PointConfig pointConfig = new PointConfig();
                        pointConfig.setPointStandardId(sourceEp.getId());
                        pointConfig.setRelevanceId(sourceSystem.getHeatSystemId());
                        pointConfig.setCreateTime(nowTime);
                        pointConfig.setLevel(TreeLevel.HeatSystem.level());
                        pointConfig.setCreateUser("数据初始化配置");
                        pointConfigList.add(pointConfig);
                    }
                    if (!CollectionUtils.isEmpty(pointConfigList)) {
                        pointConfigService.saveBatch(pointConfigList);
                    }
                }
            }
        }
        return true;
    }
}
