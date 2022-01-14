package com.bmts.heating.bussiness.baseInformation.app.joggle.energy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bmts.heating.commons.basement.model.db.entity.EnergyDevice;
import com.bmts.heating.commons.basement.model.db.response.EnergyDeviceResponse;
import com.bmts.heating.commons.db.service.EnergyDeviceService;
import com.bmts.heating.commons.entiy.baseInfo.request.energy.EnergyDeviceDto;
import com.bmts.heating.commons.utils.restful.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/energy/device")
@Api("能耗设备配置")
public class EnergyDeviceJoggle {
	@Autowired
	private EnergyDeviceService energyDeviceService;

	@PostMapping("/insert")
	@ApiOperation("添加能耗设备配置")
	public Response insertEnergyDevice(@RequestBody EnergyDevice energyDevice){
		energyDevice.setCreateTime(LocalDateTime.now());
		if (energyDeviceService.save(energyDevice)){
			return Response.success("添加成功");
		}
		return Response.fail("添加失败");
	}

	@PutMapping("/update")
	@ApiOperation("修改能耗设备配置")
	public Response updateEnergyDevice(@RequestBody EnergyDevice energyDevice){
		EnergyDevice byId = energyDeviceService.getById(energyDevice.getId());
		if (ObjectUtils.isNotEmpty(byId)){
			energyDevice.setUpdateTime(LocalDateTime.now());
			if (energyDeviceService.updateById(energyDevice)){
				return Response.success("修改成功");
			}
		}
		return Response.fail("该配置不存在");
	}

	@DeleteMapping("/delete/{id}")
	@ApiOperation("删除能耗设备")
	public Response deleteEnergyDevice(@PathVariable Integer id){
		if (ObjectUtils.isNotEmpty(energyDeviceService.getById(id))){
			if (energyDeviceService.removeById(id)){
				return Response.success("删除成功");
			}
		}
		return Response.fail("该配置不存在");
	}


	@PostMapping("/search")
	@ApiOperation("查询能耗设备")
	public Response searchEnergyDevice(@RequestBody EnergyDeviceDto energyDeviceDto){
		QueryWrapper<EnergyDevice> wrapper = new QueryWrapper<>();
		if (StringUtils.isNotBlank(energyDeviceDto.getKeyWord())){
			wrapper.like("ed.name",energyDeviceDto.getKeyWord());
		}
		if (energyDeviceDto.getType()!= 0){
			wrapper.eq("ed.type",energyDeviceDto.getType());
		}
		if (energyDeviceDto.getStatus()!= 0){
			wrapper.eq("ed.status",energyDeviceDto.getStatus());
		}
		if (energyDeviceDto.getRelevanceId()!= 0){
			wrapper.eq("ed.relevanceId",energyDeviceDto.getRelevanceId());
		}
		if (StringUtils.isNotBlank(energyDeviceDto.getSortName())){
			if (energyDeviceDto.isSortAsc()){
				wrapper.orderByAsc(energyDeviceDto.getSortName());
			}else{
				wrapper.orderByDesc(energyDeviceDto.getSortName());
			}
		}
		IPage<EnergyDevice> page = new Page<>(energyDeviceDto.getCurrentPage(),energyDeviceDto.getPageCount());
		IPage<EnergyDeviceResponse> pages = null;
		if (energyDeviceDto.getLevel() != 0){
			wrapper.eq("ed.level",energyDeviceDto.getLevel());
			if (energyDeviceDto.getLevel()==2){
				//站
				pages = energyDeviceService.getPageSource(page, wrapper);
			}else{
				//源
				pages = energyDeviceService.getPageStation(page, wrapper);
			}
		}
		return Response.success(pages);
	}

	@GetMapping("/searchById/{id}")
	@ApiOperation("根据id查询单条")
	public Response searchEnergyDeviceById(@PathVariable Integer id){
		return Response.success(energyDeviceService.getById(id));
	}

}
