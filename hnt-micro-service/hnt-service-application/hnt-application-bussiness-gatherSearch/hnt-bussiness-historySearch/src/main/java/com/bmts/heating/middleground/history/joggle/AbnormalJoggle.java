package com.bmts.heating.middleground.history.joggle;

import com.bmts.heating.commons.basement.model.db.entity.CommonHeatSeason;
import com.bmts.heating.commons.entiy.baseInfo.request.AbnormalDto;
import com.bmts.heating.commons.entiy.baseInfo.request.tdengine.Abnormal;
import com.bmts.heating.commons.entiy.gathersearch.response.history.AlarmCountIndex;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.middleground.history.service.AbnormalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "异常数据管理")
@Slf4j
@RestController
@RequestMapping("/abnormal")
public class AbnormalJoggle {

    @Autowired
    private AbnormalService abnormalService;

    @ApiOperation("查询数据")
    @PostMapping("/page")
    public Response pageTd(@RequestBody AbnormalDto dto) {
        List<Abnormal> abnormals = abnormalService.pageAbnormal(dto);
        return Response.success(abnormals);
    }

    @ApiOperation("查询供暖期间报警总数")
    @PostMapping("/count")
    public Response count(@RequestBody AbnormalDto abnormalDto){
        Integer count = abnormalService.alarmCountIndex(abnormalDto);
        return Response.success(count);
    }

}
