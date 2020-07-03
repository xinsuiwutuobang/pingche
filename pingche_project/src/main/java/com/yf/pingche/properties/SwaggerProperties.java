package com.yf.pingche.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 *
 * </p>
 *
 * @author yangfei
 * @since 2019-05-18
 */
@Data
@Component
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {
    /**
     * 是否启用
     */
    private boolean enable;
    /**
     * 基础包
     */
    private String basePackage;
    /**
     * 描述
     */
    private String description;

    /**
     * 标题
     */
    private String title;


    /**
     * 版本
     */
    private String version;
}
