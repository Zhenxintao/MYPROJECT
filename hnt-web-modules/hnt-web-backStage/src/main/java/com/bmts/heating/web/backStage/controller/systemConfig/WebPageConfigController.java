package com.bmts.heating.web.backStage.controller.systemConfig;

import com.bmts.heating.commons.basement.model.db.entity.WebPageConfig;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.jwt.utils.JwtUtils;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.systemConfig.WebPageConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Api(tags = "通用配置管理")
@RestController
@RequestMapping("/webPageConfig")
public class WebPageConfigController {

	@Autowired
	private WebPageConfigService webPageConfigService;

	@ApiOperation("新增")
	@PostMapping("/save")
	public Response saveWebPageConfig(@RequestBody WebPageConfig webPageConfig, HttpServletRequest request){
		webPageConfig.setUserId(JwtUtils.getUserId(request));
		return webPageConfigService.save(webPageConfig);
	}


	@ApiOperation("修改")
	@PutMapping("/update")
	public Response updateWebPageConfig(@RequestBody WebPageConfig webPageConfig){
		return webPageConfigService.update(webPageConfig);
	}

	@ApiOperation("删除")
	@DeleteMapping("/delete")
	public Response deleteWebPageConfig(@RequestParam String configKey){
		return  webPageConfigService.delete(configKey);
	}

	@ApiOperation("查询")
	@PostMapping("/searchPage")
	public Response searchWebPageConfig(@RequestBody BaseDto baseDto){
		return webPageConfigService.searchPage(baseDto);
	}
}
