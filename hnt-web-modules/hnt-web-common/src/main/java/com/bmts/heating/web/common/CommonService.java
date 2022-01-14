package com.bmts.heating.web.common;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.bmts.heating.commons.basement.model.db.entity.SourceFirstNetBaseView;
import com.bmts.heating.commons.basement.model.db.entity.StationFirstNetBaseView;
import com.bmts.heating.commons.entiy.gathersearch.request.tdengine.QueryAggregateHistoryDto;
import com.bmts.heating.commons.entiy.gathersearch.response.history.tdengine.HistoryBaseDataResponse;
import com.bmts.heating.web.config.utils.TSCCRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommonService {
    @Autowired
    private TSCCRestTemplate template;

    private final String baseServer = "bussiness_baseInfomation";

    private final String gatherSearch = "gather_search";

    public List<StationFirstNetBaseView> filterFirstNetBase() {
        return Arrays.asList(template.get("/common/firstNetBase", baseServer, StationFirstNetBaseView[].class));

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

    public List<StationFirstNetBaseView> metaStation(List<Integer> ids) {
        List<StationFirstNetBaseView> firstNetBases = this.filterFirstNetBase();
        if (CollectionUtils.isNotEmpty(ids)) {
            firstNetBases = firstNetBases.stream().filter(e -> ids.contains(e.getHeatSystemId())).collect(Collectors.toList());
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


    public HistoryBaseDataResponse convergeHistory(QueryAggregateHistoryDto dto) {
        HistoryBaseDataResponse historyEnergyDataResponse = template
                .doHttp("/tdEngineHistory/queryHistoryAggregate", dto, gatherSearch, HistoryBaseDataResponse.class, HttpMethod.POST);
        return historyEnergyDataResponse;
    }

}
