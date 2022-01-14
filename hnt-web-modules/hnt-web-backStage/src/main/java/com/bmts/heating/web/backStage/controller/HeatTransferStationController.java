package com.bmts.heating.web.backStage.controller;

import com.bmts.heating.commons.basement.model.db.entity.HeatTransferStation;
import com.bmts.heating.commons.basement.model.db.response.HeatTransferStationResponse;
import com.bmts.heating.commons.basement.model.db.response.station.HeatTransferStationInfoResponse;
import com.bmts.heating.commons.entiy.baseInfo.request.HeatTransferStationDto;
import com.bmts.heating.commons.entiy.baseInfo.request.FreezeDto;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.HeatTransferStationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "换热站基础信息管理")
@RestController
@RequestMapping("/heatTransferStation")
public class HeatTransferStationController {

    @Autowired
    private HeatTransferStationService heatTransferStationService;


    @ApiOperation(value = "分页查询",response = HeatTransferStationResponse[].class)
    @PostMapping("/page")
    public Response page(@RequestBody HeatTransferStationDto info, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        if (userId != null) {
            info.setUserId(userId);
        }
        return heatTransferStationService.page(info);
    }

    @ApiOperation(value = "新增")
    @PostMapping
    public Response insert(@RequestBody HeatTransferStation info, HttpServletRequest request) {
        Integer userId = JwtUtils.getUserId(request);
        if (userId != null) {
            info.setUserId(userId);
        }
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            info.setCreateUser(userName);
        }
        return heatTransferStationService.insert(info);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping
    public Response delete(@RequestParam int id) {
        return heatTransferStationService.delete(id);
    }

    @ApiOperation(value = "修改")
    @PutMapping
    public Response update(@RequestBody HeatTransferStation info, HttpServletRequest request) {
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            info.setUpdateUser(userName);
        }
        return heatTransferStationService.update(info);
    }

    @ApiOperation(value = "单站详细信息-编辑时使用",response = Boolean.class)
    @GetMapping("/{id}")
    public Response detail(@PathVariable Integer id) {
        return heatTransferStationService.detail(id);
    }

    @ApiOperation(value = "查询换热站详细信息-关联热源、控制柜的详细信息",response = HeatTransferStationInfoResponse.class)
    @GetMapping
    public Response info(@RequestParam int id) {
        return heatTransferStationService.getInfo(id);
    }

    @ApiOperation(value = "冻结或解冻")
    @PostMapping("/freeze")
    public Response freeze(@RequestBody FreezeDto dto, HttpServletRequest request) {
        String userName = JwtUtils.getUserName(request);
        if (StringUtils.isNotBlank(userName)) {
            dto.setName(userName);
        }
        return heatTransferStationService.freeze(dto);
    }


    @ApiOperation(value = "查询热力站是否重名")
    @PostMapping("/repeat")
    public Response repeat(@RequestBody HeatTransferStation info) {
        return heatTransferStationService.repeat(info);
    }


    @ApiOperation(value = "根据id排序")
    @PutMapping("/sort")
    public Response updateSort(@RequestBody List<String> list){
        return heatTransferStationService.updateById(list);
    }
}
