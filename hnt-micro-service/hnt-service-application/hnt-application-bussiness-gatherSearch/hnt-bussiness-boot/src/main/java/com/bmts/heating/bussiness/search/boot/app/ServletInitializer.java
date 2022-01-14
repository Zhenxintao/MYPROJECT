package com.bmts.heating.bussiness.search.boot.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author naming
 * @description
 * @date 2021/3/29 9:55
 **/
@Slf4j
public class ServletInitializer extends SpringBootServletInitializer {
    public ServletInitializer() {
        log.info("初始化 ServletInitializer");
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(GatherSearchApplication.class);
    }
}
