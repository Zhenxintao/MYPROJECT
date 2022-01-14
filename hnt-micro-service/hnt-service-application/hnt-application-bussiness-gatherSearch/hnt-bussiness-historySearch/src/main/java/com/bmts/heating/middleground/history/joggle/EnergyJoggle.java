package com.bmts.heating.middleground.history.joggle;

import com.bmts.heating.commons.entiy.gathersearch.request.QueryEsBucketDto;
import com.bmts.heating.commons.entiy.gathersearch.request.QueryEsDto;
import com.bmts.heating.commons.entiy.gathersearch.request.TimeRange;
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

import java.util.*;

@Api(tags = "能耗历史数据管理")
@Slf4j
@RestController
@RequestMapping("/energy")
public class EnergyJoggle {

//    @Autowired
//    private HistoryData historyData;
    @Autowired
    private TdEngineHistoryData historyData;
    @ApiOperation("查询历史数据")
    @PostMapping("/table")
    public Collection<Map<String, Object>> table(@RequestBody QueryEsDto dto) {
        if (dto.getCurrentPage() == null || dto.getCurrentPage() <= 0) {
            return new ArrayList<>();
        }
        if (StringUtils.isEmpty(dto.getField())) {
            dto.setField("timeStrap");
        } else {
            switch (dto.getField()) {
                case "heat":
                    dto.setField("HM_HT.consumption");
                    break;
                case "water":
                    dto.setField("WM_FT.consumption");
                    break;
                case "electricity":
                    dto.setField("Ep.consumption");
                    break;
                case "unitHeat":
                    dto.setField("HM_HT.unitConsumption");
                    break;
                case "unitWater":
                    dto.setField("WM_FT.unitConsumption");
                    break;
                case "unitElectricity":
                    dto.setField("Ep.unitConsumption");
                    break;
                default:
                    throw new RuntimeException("dto-- Field is blank");
            }
        }
        if (dto.getSortType() == null) {
            dto.setSortType(true);
        }
        return historyData.findHistoryEnergyData(dto);
    }


    @ApiOperation("查询能耗聚合数据")
    @PostMapping("/bucket")
    public List<Map<String, String>> bucket(@RequestBody QueryEsBucketDto dto) {
            List<Map<String, String>> maps = historyData.bucketEnergyData(dto);
            log.info("{}", maps);
            return maps;
    }

}
