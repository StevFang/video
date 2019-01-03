package com.qs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 资源路径映射
 *
 * @author FBin
 * @time 2019/1/3 22:49
 */
@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    @Value("${server.upload.filepath}")
    private String savePath;

    @Value("${server.visit.path}")
    private String visitPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String pathPattern = visitPath + "**";
        String resourceLocation = "file:" + savePath;
        registry.addResourceHandler(pathPattern).addResourceLocations(resourceLocation);
    }
}
