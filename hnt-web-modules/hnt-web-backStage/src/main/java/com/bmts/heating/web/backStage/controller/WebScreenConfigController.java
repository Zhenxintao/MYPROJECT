package com.bmts.heating.web.backStage.controller;

import com.bmts.heating.commons.basement.model.db.entity.WebScreenConfig;
import com.bmts.heating.commons.entiy.baseInfo.request.WebScreenConfigDto;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.WebScreenConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "界面显示配置")
@RestController
@RequestMapping("/screen")
public class WebScreenConfigController {

	@Autowired
	private WebScreenConfigService webScreenConfigService;

	@GetMapping("/query")
	@ApiOperation(value = "查询")
	public Response queryWebScreenConfig(){
		return webScreenConfigService.queryWebScreenConfig();
	}

	@PostMapping("page")
	@ApiOperation("分页查询")
	public Response queryWebScreenConfigPage(@RequestBody WebScreenConfigDto webScreenConfigDto){
		return webScreenConfigService.queryPage(webScreenConfigDto);
	}


	@PostMapping("/insert")
	@ApiOperation(value = "添加配置")
	public Response insertWebScreenConfig(@RequestBody WebScreenConfig webScreenConfig, HttpServletRequest request){
		Integer userId = JwtUtils.getUserId(request);
		if (userId != null){
			webScreenConfig.setCreatorId(userId);
		}
		String userName = JwtUtils.getUserName(request);
		if (StringUtils.isNotBlank(userName)){
			webScreenConfig.setCreatorName(userName);
		}
		return webScreenConfigService.insertWebScreenConfig(webScreenConfig);
	}

	@PutMapping("/update")
	@ApiOperation(value = "修改配置")
	public Response updateWebScreenConfig(@RequestBody WebScreenConfig webScreenConfig,HttpServletRequest request){
		String userName = JwtUtils.getUserName(request);
		if (StringUtils.isNotBlank(userName)){
			webScreenConfig.setCreatorName(userName);
		}
		return webScreenConfigService.updateWebScreenConfig(webScreenConfig);
	}

	@DeleteMapping("/delete")
	@ApiOperation(value = "删除配置")
	public Response deleteWebScreenConfig(@RequestParam("id") int id){
		return webScreenConfigService.removeWebScreenConfig(id);
	}


	@GetMapping("/detail")
	@ApiOperation("查询详情")
	public Response queryDetail(@RequestParam("id") int id){
		return webScreenConfigService.queryDetail(id);
	}

	@PostMapping("/queryPageKey")
	@ApiOperation("查询页面唯一标识")
	public Response queryPageKey(@RequestBody WebScreenConfig webScreenConfig){
		return webScreenConfigService.queryPageKey(webScreenConfig);
	}

}
