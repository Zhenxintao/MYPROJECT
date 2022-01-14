package com.bmts.heating.bussiness.baseInformation.app.joggle.common;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmts.heating.commons.basement.model.db.entity.PointCollectStandardType;
import com.bmts.heating.commons.db.service.PointCollectStandardTypeService;
import com.bmts.heating.commons.db.service.PointUnitService;
import com.bmts.heating.commons.entiy.common.response.PointUnitType;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author naming
 * @description
 * @date 2021/1/14 18:37
 **/
@Api(tags = "点公用基本信息")
@RestController
@RequestMapping("pointBasic")
public class PointBasicJoggle {
    @Autowired
    private PointCollectStandardTypeService pointCollectStandardTypeService;


    @ApiOperation("根据点类型code获取标准点  例如：AI DI TX 等")
    @GetMapping("/point/type/{code}")
    public List<PointCollectStandardType> queryPointByType(@PathVariable String code) {
        List<PointCollectStandardType> list = pointCollectStandardTypeService.list(Wrappers.<PointCollectStandardType>lambdaQuery().eq(PointCollectStandardType::getCode, code));
        return list;
    }


}
