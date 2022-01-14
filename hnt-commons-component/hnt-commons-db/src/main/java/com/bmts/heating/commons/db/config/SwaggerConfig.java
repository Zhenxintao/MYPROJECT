//package com.bmts.heating.commons.db.config;
//
//import com.google.common.base.Predicates;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.ParameterBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.schema.ModelRef;
//import springfox.documentation.service.Parameter;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @Author: naming
// * @Description:
// * @Date: Create in 2020/9/24 15:52
// * @Modified by
// */
//@Configuration
//@EnableSwagger2
//public class SwaggerConfig {
//    @Bean
//    public Docket createRestApi() {
//        ParameterBuilder token = new ParameterBuilder();
//        List<Parameter> pars = new ArrayList<>(1);
//        token.name("authorization").description("令牌").modelRef(new ModelRef("String")).parameterType("header").required(false).build();
//        pars.add(token.build());
//        return new Docket(DocumentationType.SWAGGER_2)
//                .pathMapping("/")
//                .apiInfo(new ApiInfoBuilder()
//                        .version("1.0")
//                        .build())
//                .select()
//                .apis(RequestHandlerSelectors.any())
//                .paths(Predicates.not(PathSelectors.regex("/error.*")))// 错误路径不监控
//                .paths(PathSelectors.any())
//                .build();
//
//
//    }
//}
