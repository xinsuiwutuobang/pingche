package com.yf.pingche.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>
 *
 * </p>
 *
 * @author yangfei
 * @since 2019-05-18
 */
@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${resource-access-patterns}")
    private String resourceAccessPathPatterns;
    @Value("${upload-path}")
    private String uploadPath;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 设置上传文件访问路径http://127.0.0.1:8010/api/resource/202007131702166707824.png
        registry.addResourceHandler(resourceAccessPathPatterns)
                .addResourceLocations("file:" + uploadPath);
    }
}
