package com.bmts.heating.middleground.history.joggle;

import com.bmts.heating.commons.entiy.gathersearch.request.QueryEsDto;
import com.bmts.heating.middleground.history.service.HistoryData;
import com.bmts.heating.middleground.history.service.TdEngineHistoryData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Api(tags = "历史数据报表")
@Slf4j
@RestController
@RequestMapping("/historySearch")
public class RepoTableJoogle {

    //    @Autowired
//    private HistoryData historyData;
    @Autowired
    private TdEngineHistoryData historyData;
//    @Autowired
//    private TdEngineHistoryData historyData;
    @ApiOperation("查询历史表格数据")
    @PostMapping("/table")
    public Collection<Map<String, String>> table(@RequestBody QueryEsDto dto) {
        if (dto.getCurrentPage() == null || dto.getCurrentPage() <= 0) {
            return new ArrayList<>();
        }
        if (StringUtils.isEmpty(dto.getField())) {
            dto.setField("timeStrap");
        }
        if (dto.getSortType() == null) {
            dto.setSortType(true);
        }
        dto.setIncludeTotal(true);
        return historyData.findHistoryData(dto);
    }

    @ApiOperation("查询能耗数据")
    @PostMapping("/energy")
    public Collection<Map<String, Object>> energy(@RequestBody QueryEsDto dto) {
        if (dto.getCurrentPage() == null || dto.getCurrentPage() <= 0) {
            return new ArrayList<>();
        }
        if (!StringUtils.isEmpty(dto.getField())) {
            dto.setField("timeStrap");
        }
        if (dto.getSortType() == null) {
            dto.setSortType(true);
        }
        dto.setIncludeTotal(true);
        return historyData.findHistoryEnergyData(dto);
    }


}
