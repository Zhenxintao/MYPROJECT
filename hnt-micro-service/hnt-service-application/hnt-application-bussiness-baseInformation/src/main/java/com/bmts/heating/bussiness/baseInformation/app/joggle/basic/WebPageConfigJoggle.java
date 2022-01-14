package com.bmts.heating.bussiness.baseInformation.app.joggle.basic;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.WebPageConfig;
import com.bmts.heating.commons.db.service.WebPageConfigService;
import com.bmts.heating.commons.entiy.baseInfo.request.base.BaseDto;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/webPageConfig")
@Api(tags = "通用配置管理")
public class WebPageConfigJoggle {

	@Autowired
	private WebPageConfigService webPageConfigService;

	@ApiOperation("新增")
	@PostMapping("/save")
	public Response saveWebPageConfig(@RequestBody WebPageConfig webPageConfig){
		if (webPageConfigService.save(webPageConfig)){
			return Response.success();
		}
		return Response.fail("配置保存失败");
	}


	@ApiOperation("修改")
	@PutMapping("/update")
	public Response updateWebPageConfig(@RequestBody WebPageConfig webPageConfig){
		if(webPageConfigService.update(webPageConfig,new QueryWrapper<WebPageConfig>().eq("configKey",webPageConfig.getConfigKey()))){
			return Response.success();
		}
		return Response.fail("修改失败");
	}

	@ApiOperation("删除")
	@DeleteMapping("/delete")
	public Response deleteWebPageConfig(@RequestParam String configKey){
		if (webPageConfigService.remove(new QueryWrapper<WebPageConfig>().eq("configKey",configKey))){
			return Response.success();
		}
		return Response.fail("删除失败");
	}

	@ApiOperation("查询")
	@PostMapping("/searchPage")
	public Response searchWebPageConfig(@RequestBody BaseDto baseDto){
		IPage<WebPageConfig> page = new Page<>(baseDto.getCurrentPage(),baseDto.getPageCount());
		QueryWrapper<WebPageConfig> queryWrapper = new QueryWrapper<>();
		if (StringUtils.isNotBlank(baseDto.getKeyWord())){
			queryWrapper.eq("configKey",baseDto.getKeyWord());
		}
		return Response.success(webPageConfigService.page(page,queryWrapper));
	}
}
