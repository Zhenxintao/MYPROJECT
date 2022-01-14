package com.bmts.heating.web.scada.service.monitor.impl.common;

import com.bmts.heating.commons.basement.model.db.entity.StationFirstNetBaseView;
import com.bmts.heating.commons.entiy.baseInfo.cache.FirstNetBase;
import com.bmts.heating.commons.entiy.baseInfo.cache.PointRank;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.entiy.baseInfo.request.monitor.MonitorBeyondDto;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public abstract class CommonService {

    @Autowired
    private TSCCRestTemplate tsccRestTemplate;
    private final String baseServer = "bussiness_baseInfomation";
    private final String gatherSearch = "gather_search";

    /**
     * 根据传入dto判断是否按照参量排序
     *
     * @param dto
     * @param source
     * @return
     */
    public List<StationFirstNetBaseView> beyondSort(MonitorBeyondDto dto, List<StationFirstNetBaseView> source) {
        //排序
        PointRank rank = getBeyondRank(dto);
        if (rank != null) {
            List<StationFirstNetBaseView> results = new LinkedList<>();
            rank.getMap().forEach((k, v) -> {
                source.stream().filter(x -> x.getHeatSystemId().equals(k)).findFirst().ifPresent(results::add);
            });
            return results;
        }
        log.error("real data rank  returned null");
        return Collections.emptyList();
    }

    /**
     * 根据传入dto判断是否按照参量排序
     *
     * @param dto
     * @param source
     * @return
     */
    public List<StationFirstNetBaseView> buildSort(BaseDto dto, List<StationFirstNetBaseView> source) {
        //排序
        PointRank rank = getRank(dto.getSortName(), dto.isSortAsc());
        if (rank != null) {
            List<StationFirstNetBaseView> results = new LinkedList<>();
            List<StationFirstNetBaseView> blanks = source.stream().filter(x -> !rank.getMap().containsKey(x.getHeatSystemId()) && !Objects.equals(x.getNumber(), "0"))
                    .collect(Collectors.toList());
            //if (dto.isSortAsc())
            ////升序把空行前置
            //{
            //    results.addAll(blanks);
            //}

            rank.getMap().forEach((k, v) -> {
                source.stream().filter(x -> x.getHeatSystemId().equals(k)).findFirst().ifPresent(results::add);
            });

            //if (!dto.isSortAsc())
            ////空行后置
            //{
            //    results.addAll(blanks);
            //}

            results.addAll(blanks);
            return results;
        }
        log.error("real data rank  returned null");
        return source.stream()
                .sorted(Comparator.comparing(StationFirstNetBaseView::getHeatSystemId))//排序
                .skip((dto.getCurrentPage() - 1) * dto.getPageCount())
                .limit((dto.getCurrentPage()) * dto.getPageCount())//分页
                .collect(Collectors.toList());
    }

    /**
     * 根据传入dto判断是否按照参量排序
     *
     * @param dto
     * @param source
     * @return
     */
    public List<FirstNetBase> buildSortSource(BaseDto dto, List<FirstNetBase> source) {
        //排序
        PointRank rank = getRank(dto.getSortName(), dto.isSortAsc());
        if (rank != null) {
            List<FirstNetBase> results = new LinkedList<>();
            List<FirstNetBase> blanks = source.stream().filter(x -> !rank.getMap().containsKey(x.getHeatSystemId())).collect(Collectors.toList());
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
                .sorted(Comparator.comparing(FirstNetBase::getHeatSystemId))//排序
                .skip((dto.getCurrentPage() - 1) * dto.getPageCount())
                .limit((dto.getCurrentPage()) * dto.getPageCount())//分页
                .collect(Collectors.toList());
    }

    public PointRank getRank(String pointName, boolean isAsc) {
        return tsccRestTemplate.doHttp("/rank/".concat(pointName).concat("/").concat(String.valueOf(isAsc)), null, gatherSearch, PointRank.class, HttpMethod.GET);
    }

    public PointRank getBeyondRank(MonitorBeyondDto dto) {
        return tsccRestTemplate.doHttp("/rank/beyond", dto, gatherSearch, PointRank.class, HttpMethod.POST);
    }

    //打包参数
    public Map<Integer, String[]> packageParamSource(List<FirstNetBase> firstNetBaseList, String[] columnNames) {
        Map<Integer, String[]> paramMap = new HashMap<>();
        firstNetBaseList.forEach(e -> paramMap.put(e.getHeatSystemId(), columnNames));
        return paramMap;
    }

    //打包参数
    public Map<Integer, String[]> packageParam(List<StationFirstNetBaseView> firstNetBaseList, String[] columnNames) {
        Map<Integer, String[]> paramMap = new HashMap<>();
        firstNetBaseList.forEach(e -> paramMap.put(e.getHeatSystemId(), columnNames));
        return paramMap;
    }
}
