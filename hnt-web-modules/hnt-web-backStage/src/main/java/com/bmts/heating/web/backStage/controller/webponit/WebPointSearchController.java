package com.bmts.heating.web.backStage.controller.webponit;

import com.bmts.heating.commons.basement.model.db.entity.PointUnit;
import com.bmts.heating.commons.basement.model.db.response.PointStandardResponse;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.PointTypeSearchDto;
import com.bmts.heating.commons.entiy.baseInfo.request.pointConfig.SelectPointConfigDto;
import com.bmts.heating.commons.entiy.baseInfo.response.SelectPointConfigResponse;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.AlarmConfigService;
import com.bmts.heating.web.backStage.service.webpoint.WebPointSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "配置各页面选择点位信息")
@RestController
@RequestMapping("/webPagePointConfig")
public class WebPointSearchController {
    @Autowired
    private WebPointSearchService webPointSearchService;

    @ApiOperation(value = "根据标准点")
    @PostMapping("/queryUnitType")
    public Response queryUnitType(@RequestBody PointTypeSearchDto pointTypeSearchDto) {
        return webPointSearchService.queryPointType(pointTypeSearchDto);
    }

    @ApiOperation(value = "查询单位分类",response = PointUnit[].class)
    @GetMapping("/queryType")
    public Response queryType() {
        return webPointSearchService.queryType();
    }

    @ApiOperation(value = "添加页面选择点位信息",response = PointStandardResponse[].class)
    @PostMapping("/selectPontConfig")
    public Response selectPointConfig(@RequestBody SelectPointConfigDto selectPointConfigDto) {
       return webPointSearchService.selectPointConfig(selectPointConfigDto);
    }

    @ApiOperation(value = "获取配置页面数据状态",response = SelectPointConfigResponse.class)
    @GetMapping("/pagePointConfigStatus/{pageKey}")
    public Response searchPointConfigStatus(@PathVariable String pageKey) {
        return webPointSearchService.searchPointConfigStatus(pageKey);
    }

}
