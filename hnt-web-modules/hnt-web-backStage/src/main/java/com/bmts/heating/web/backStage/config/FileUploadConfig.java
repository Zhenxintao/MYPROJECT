package com.bmts.heating.web.backStage.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class FileUploadConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//获取文件的真实路径
		String path = System.getProperty("user.dir")+"\\hnt-web-modules\\hnt-web-backStage\\src\\main\\resources\\file\\";
		registry.addResourceHandler("/file/**").addResourceLocations("file:"+path);
	}
}
