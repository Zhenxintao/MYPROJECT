package com.bmts.heating.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerWebConfig {

    @Autowired
    private DocConfig docConfig;

    @Bean
    public Docket createRestFulApi(Environment environment) {
        //配置swagger环境，生产环境(prod)不展示界面,调试环境展示界面(dev)
        Profiles profiles = Profiles.of("dev");
        Boolean flag= environment.acceptsProfiles(profiles);
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiWebInfo())
                .enable(flag)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    //构建 api文档的详细信息函数
    private ApiInfo apiWebInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title(docConfig.getTitle())
                .description(docConfig.getDescription())
                .version(docConfig.getVersion())
                .build();
    }
}
