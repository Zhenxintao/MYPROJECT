package com.bmts.heating.web.backStage.controller.base;

import com.bmts.heating.commons.entiy.baseInfo.request.HeatAreaChangeDto;
import com.bmts.heating.commons.entiy.energy.AreaChangeHistoryDto;
import com.bmts.heating.commons.entiy.energy.AreaManagerDto;
import com.bmts.heating.commons.jwt.annotation.PassToken;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.base.AreaManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "面积管理")
@RestController
@RequestMapping("/area/manager")
public class AreaManagerController {

	@Autowired
	private AreaManagerService areaManagerService;

	@ApiOperation("修改面积   系统:1 控制柜:2 换热站:3 热源:4 热网:5 ")
	@PostMapping("/update")
	@PassToken
	public Response update(@RequestBody AreaManagerDto dto){
		return areaManagerService.update(dto);
	}

	@ApiOperation("站-柜-系统列表查询")
	@GetMapping("/{id}")
	@PassToken
	public Response list(@PathVariable Integer id){
		return areaManagerService.list(id);
	}

	@ApiOperation("供热面积修改历史查询")
	@PostMapping("/areaHistory")
	public  Response getAreaChangeHistory(@RequestBody AreaChangeHistoryDto dto){
		return  areaManagerService.getAreaChangeHistoryList(dto);
	}

}
