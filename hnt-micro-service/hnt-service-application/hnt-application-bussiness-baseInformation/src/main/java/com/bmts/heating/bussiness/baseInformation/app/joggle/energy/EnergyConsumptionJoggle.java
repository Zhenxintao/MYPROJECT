package com.bmts.heating.bussiness.baseInformation.app.joggle.energy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.bussiness.baseInformation.app.utils.ExcelUtils;
import com.bmts.heating.commons.basement.config.ExcelColumn;
import com.bmts.heating.commons.basement.model.db.entity.EnergyConsumption;
import com.bmts.heating.commons.db.service.EnergyConsumptionService;
import com.bmts.heating.commons.entiy.baseInfo.request.EnergyConsumptionDto;
import com.bmts.heating.commons.entiy.baseInfo.request.energyConsumption.Datas;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Api(tags = "能耗导入")
@RestController
@RequestMapping("/energyConsumption")
@Slf4j
public class EnergyConsumptionJoggle {
	@Autowired
	private EnergyConsumptionService energyConsumptionService;

	private String path = ".\\hnt-web-modules\\hnt-web-backStage\\src\\main\\resources\\file\\";

	private String fileName = null;

	@Autowired
	private EnergyAsync energyAsync;

	@PostMapping("/import")
	@ApiOperation("能耗导入")
	@Transactional(rollbackFor = Exception.class)
	public Response importPowerConsumptionStatistics(@RequestParam MultipartFile file, EnergyConsumptionDto power,HttpServletResponse response){
		try{
			String s = UUID.randomUUID().toString().replace("-", "");
			fileName = file.getOriginalFilename().substring(0, file.getOriginalFilename().indexOf("."));
			EnergyConsumption energyConsumption1 = new EnergyConsumption();
			//读取excel内容
			List<EnergyConsumption> energyConsumptions = ExcelUtils.readExcel("", EnergyConsumption.class, file);
			if (!CollectionUtils.isEmpty(energyConsumptions)){
				//把文件保存到数据库
				//energyConsumption1.setId(power.getId());
				energyConsumption1.setName(fileName);
				energyConsumption1.setUrl(path+fileName+s+".xlsx");
				energyConsumption1.setEnergyType(power.getEnergyType());
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
				energyConsumption1.setCreateTime(LocalDateTime.now());
				LocalDateTime startTime = LocalDateTime.parse(power.getStartTime(), formatter);
				LocalDateTime endTime = LocalDateTime.parse(power.getEndTime(), formatter);
				Duration between = Duration.between(startTime, endTime);
				if (between.toDays() >= 1){
					energyConsumption1.setStartTime(startTime);
					energyConsumption1.setEndTime(endTime);
				}else{
					return Response.fail("结束时间要大于开始时间，至少一天");
				}
				energyConsumption1.setIsCaculate(power.getIsCaculate());
				energyConsumption1.setType(power.getType());
				boolean save = energyConsumptionService.save(energyConsumption1);
				if(save){
					energyAsync.insert(energyConsumptions, power);
					//如果保存数据库成功 直接写成excel并保存
					//this.writeExcel(response, energyConsumptions, EnergyConsumption.class,s);
					return Response.success(fileName+s+".xlsx");
					//return Response.success("导入成功");
				}else{
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				}
			}
		}catch (Exception e){
			log.error("数据插入td库失败");
		}
		return Response.fail("导入失败");
	}

	@GetMapping("/download")
	@ApiOperation(value = "能耗统计下载")
	public Response downLoad(@RequestParam String id) {
		EnergyConsumption byId = energyConsumptionService.getById(Integer.valueOf(id));
		return Response.success(byId);
	}


	@PostMapping("/findAll")
	@ApiOperation("查询全部")
	public Response findAll(@RequestBody EnergyConsumptionDto energyConsumptionDto){
		QueryWrapper<EnergyConsumption> wrapper = new QueryWrapper<>();
		Page<EnergyConsumption> page = null;
		if (energyConsumptionDto.getEnergyType() != null && energyConsumptionDto.getType() != null){
			wrapper.eq("energyType",energyConsumptionDto.getEnergyType()).eq("type",energyConsumptionDto.getType());
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
		if (StringUtils.isNotBlank(energyConsumptionDto.getStartTime())&&StringUtils.isNotBlank(energyConsumptionDto.getEndTime())){
			wrapper.ge("startTime",LocalDateTime.parse(energyConsumptionDto.getStartTime(),formatter));
			wrapper.le("endTime",LocalDateTime.parse(energyConsumptionDto.getEndTime(),formatter));
		}
		if (energyConsumptionDto.getCurrentPage()!= 0 && energyConsumptionDto.getPageCount() != 0){
			page = new Page<>(energyConsumptionDto.getCurrentPage(), energyConsumptionDto.getPageCount());
		}
		Page<EnergyConsumption> page1 = energyConsumptionService.page(page, wrapper);
		if (page1 != null){
			return Response.success(page1);
		}
		return Response.fail("数据不存在");
	}

	@DeleteMapping("/delete")
	@ApiOperation("删除")
	public Response delete(@RequestParam String id){
		boolean b = energyConsumptionService.removeById(Integer.valueOf(id));
		if(b){
			return Response.success("删除成功");
		}
		return Response.fail("删除失败");
	}

	@PutMapping("/update")
	@ApiOperation("修改是否参与计算")
	public Response update(@RequestBody EnergyConsumption energyConsumption){
		EnergyConsumption byId = energyConsumptionService.getById(energyConsumption.getId());
		if (byId != null){
			byId.setIsCaculate(energyConsumption.getIsCaculate());
			boolean b = energyConsumptionService.updateById(byId);
			return Response.success(b);
		}
		return Response.fail("不存在该数据");
	}



}


