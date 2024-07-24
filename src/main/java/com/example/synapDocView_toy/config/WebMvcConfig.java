package com.example.synapDocView_toy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/hwp/**") // 해당 경로의 요청이 오면
                .addResourceLocations("classpath:/uploads/hwp/")   // resources/uploads/hwp/ 폴더에 있는 파일 제공
                .setCachePeriod(0);
    }
}