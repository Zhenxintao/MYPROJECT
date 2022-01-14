package com.bmts.heating.web.backStage.controller.station;

import com.alibaba.fastjson.JSONObject;
import com.bmts.heating.commons.entiy.baseInfo.request.station.TemplateStationDto;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.station.SaveTemplateByStationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;


@RestController
@Api(tags = "保存模板")
@Slf4j
@RequestMapping("/StationTemplate")
public class SaveTemplateByStationController {

	@Autowired
	private SaveTemplateByStationService saveTemplateByStationService;

	@ApiOperation(value = "根据系统获取所有机组", response = JSONObject.class)
	@GetMapping("cache/{id}")
	public Response queryHeatSystemInCache(@PathVariable Integer id) {
		return saveTemplateByStationService.queryHeatSystemInCache(id);
	}

	@ApiOperation(value = "根据系统获取所有机组", response = Map.class)
	@GetMapping("{id}")
	public Response queryHeatSystem(@PathVariable Integer id) {
		return saveTemplateByStationService.queryHeatSystem(id);
	}

	@ApiOperation(value = "保存模板",response = TemplateStationDto[].class)
	@PostMapping
	public Response insert(@RequestBody Collection<TemplateStationDto> dtoList, HttpServletRequest request) {
		Integer userId = JwtUtils.getUserId(request);
		String userName = JwtUtils.getUserName(request);
		dtoList.forEach(e -> {
			e.setUserName(userName);
			e.setUserId(userId);
		});
		return saveTemplateByStationService.insert(dtoList);
	}

}




