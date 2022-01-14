package com.bmts.heating.web.backStage.controller;


import com.bmts.heating.commons.basement.model.db.entity.EnergyConsumption;
import com.bmts.heating.commons.entiy.baseInfo.request.EnergyConsumptionDto;
import com.bmts.heating.commons.utils.restful.Response;
import com.bmts.heating.web.backStage.service.EnergyConsumptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Map;


@Api(tags = "能耗统计导入")
@RestController
@RequestMapping("energyConsumption")
@Slf4j
public class EnergyConsumptionController {

	@Autowired
	private EnergyConsumptionService energyConsumptionService;

	@PostMapping("/import")
	@ApiOperation(value = "能耗统计导入")

	public Response importPowerConsumptionStatistics(@RequestParam MultipartFile file, EnergyConsumptionDto power) {
		if (power.getStartTime() != null && power.getEndTime() != null) {
			Response response = energyConsumptionService.importExcel(file, power);
			if (response.getCode() == 200){
				try {
					String fileName = (String) response.getData();
					File path = new File(ResourceUtils.getURL("classpath:").getPath()+"file/");
					File upload = new File(path.getAbsolutePath(),fileName);
					file.transferTo(upload);
				}catch (IOException e) {
					e.printStackTrace();
				}
				return Response.success("导入成功");
			}
		}
		return Response.fail("导入失败");
	}

	@GetMapping("/download")
	@ApiOperation(value = "能耗统计下载")
	public void downLoad(@RequestParam String id, HttpServletResponse response) {
		Response response1 = energyConsumptionService.downLoad(id);
		Map<String, Object> byId = (Map<String, Object>) response1.getData();
		String url = (String) byId.get("url");
		String fileName = (String) byId.get("name");
		String name = StringUtils.substring(url, 60);
		if (byId != null) {
			try {
				//读取文件
				//InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("file/"+name);
				InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("file/"+name);
				InputStream fis = new BufferedInputStream(resourceAsStream);
				byte[] buffer = new byte[fis.available()];
				fis.read(buffer);
				// 清空response
				response.reset();
				// 设置response的Header
				response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName+".xlsx", "utf-8"));
				//response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
				response.addHeader("Content-Length", "" + buffer.length);
				OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
				response.setContentType("application/octet-stream");
				toClient.write(buffer);
				fis.close();
				toClient.flush();
				toClient.close();
			} catch (IOException e) {
				log.error("下载失败{}",e);
				e.printStackTrace();
			}
		}
	}

		@PostMapping("/findAll")
		@ApiOperation("查询全部表格")
		public Response findALl (@RequestBody EnergyConsumptionDto energyConsumptionDto){
			Response response = energyConsumptionService.findAll(energyConsumptionDto);
			return response;
		}

		@DeleteMapping("/delete")
		@ApiOperation("删除")
		public Response delete (@RequestParam String id){
			Response response = energyConsumptionService.delete(id);
			return response;
		}

		@PutMapping("/update")
		@ApiOperation("修改是否参与计算")
		public Response update (@RequestBody EnergyConsumption energyConsumption){
			Response response = energyConsumptionService.update(energyConsumption);
			return response;
		}
}
