package com.bmts.heating.bussiness.baseInformation.app.joggle.point;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmts.heating.commons.basement.model.db.entity.PointTemplateConfig;
import com.bmts.heating.commons.basement.model.db.entity.TemplatePoint;
import com.bmts.heating.commons.db.service.PointTemplateConfigService;
import com.bmts.heating.commons.db.service.TemplatePointService;
import com.bmts.heating.commons.entiy.baseInfo.request.FreezeDto;
import com.bmts.heating.commons.entiy.baseInfo.request.PointTemplateConfigDto;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Api(tags = "模板管理")
@RestController
@RequestMapping("/pointTemplateConfig")
@Slf4j
public class PointTemplateConfigJoggle {
    @Autowired
    PointTemplateConfigService pointTemplateConfigService;
    @Autowired
    private TemplatePointService templatePointService;

    @ApiOperation("新增")
    @PostMapping
    public Response insert(@RequestBody PointTemplateConfig info) {
        Response response = Response.fail();
        info.setCreateTime(LocalDateTime.now());
        info.setTemplateKey("Station");
        if (pointTemplateConfigService.save(info)) {
            return Response.success();
        }
        return response;
    }

    @ApiOperation("修改")
    @PutMapping
    public Response update(@RequestBody PointTemplateConfig info) {
        Response response = Response.fail();
        PointTemplateConfig pointTemplateConfig = pointTemplateConfigService.getById(info.getId());
        if (pointTemplateConfig == null)
            return Response.fail();
        info.setUpdateTime(LocalDateTime.now());
        if (pointTemplateConfigService.updateById(info))
            return Response.success();
        return response;
    }

    @ApiOperation("删除")
    @DeleteMapping
    public Response delete(@RequestParam int id) {
        Response response = Response.fail();
        QueryWrapper<TemplatePoint> wrapper = new QueryWrapper<>();
        wrapper.eq("pointTemplateConfigId",id);
        templatePointService.remove(wrapper);
        if (pointTemplateConfigService.removeById(id))
            return Response.success();
        return response;
    }

    @ApiOperation("详情")
    @GetMapping
    public Response detail(@RequestParam int id) {
        PointTemplateConfig byId = pointTemplateConfigService.getById(id);
        return Response.success(byId);
    }

    @ApiOperation("查询")
    @PostMapping("/query")
    public Response query(@RequestBody PointTemplateConfigDto dto) {
        QueryWrapper<PointTemplateConfig> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(dto.getKeyWord()))
            queryWrapper.like("templateValue", dto.getKeyWord());
        return Response.success(pointTemplateConfigService.list(queryWrapper));
    }

    @ApiOperation("冻结或解冻")
    @PostMapping("/freeze")
    public Response freeze(@RequestBody FreezeDto dto) {
        PointTemplateConfig heatStation = new PointTemplateConfig();
        heatStation.setId(dto.getId());
        // `status`  'true 解冻，false 冻结',
        heatStation.setStatus(dto.getStatus());
        if (StringUtils.isNotBlank(dto.getName())) {
            heatStation.setUpdateUser(dto.getName());
        }
        heatStation.setUpdateTime(LocalDateTime.now());
        return pointTemplateConfigService.updateById(heatStation) ? Response.success() : Response.fail();
    }


}
