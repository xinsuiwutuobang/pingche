package com.yf.pingche.config;

import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.yf.pingche.properties.SwaggerProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import springfox.documentation.RequestHandler;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.common.SwaggerPluginSupport;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.Arrays;

import static springfox.documentation.schema.Annotations.findPropertyAnnotation;
import static springfox.documentation.swagger.schema.ApiModelProperties.findApiModePropertyAnnotation;

@Slf4j
@Configuration
@EnableSwagger2
@EnableKnife4j
@ConditionalOnProperty(prefix = "swagger", name = "enable", havingValue = "true")
public class Swagger2Config {
    @Autowired
    private SwaggerProperties swaggerProperties;
    private static final String SPLIT_COMMA = ",";
    private static final String SPLIT_SEMICOLON = ";";
    private Class<?>[] ignoredParameterTypes = new Class[]{
            ServletRequest.class,
            ServletResponse.class,
            HttpServletRequest.class,
            HttpServletResponse.class,
            HttpSession.class,
            ApiIgnore.class
    };

    @Bean
    public Docket createRestApi() {
        String[] basePackages = getBasePackages();
        ApiSelectorBuilder select =
                new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select();
        if (ArrayUtils.isEmpty(basePackages)) {
            select.apis(RequestHandlerSelectors.withClassAnnotation(Api.class));
        } else {
            select.apis(basePackage(basePackages));
        }
        Docket docket =
                select.paths(PathSelectors.any()).build().enable(swaggerProperties.isEnable())
                        .ignoredParameterTypes(ignoredParameterTypes);
        return docket;
    }


    public String[] getBasePackages() {
        log.debug("swaggerProperties = " + swaggerProperties);
        String basePackage = swaggerProperties.getBasePackage();
        if (StringUtils.isBlank(basePackage)) {
            throw new RuntimeException("Swagger basePackage不能为空");
        }
        String[] basePackages = null;
        if (basePackage.contains(SPLIT_COMMA)) {
            basePackages = basePackage.split(SPLIT_COMMA);
        } else if (basePackage.contains(SPLIT_SEMICOLON)) {
            basePackages = basePackage.split(SPLIT_SEMICOLON);
        } else {
            basePackages = new String[]{basePackage};
        }
        log.info("swagger scan basePackages:" + Arrays.toString(basePackages));
        return basePackages;
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .version(swaggerProperties.getVersion())
                .build();
    }
    public static Predicate<RequestHandler> basePackage(final String[] basePackages) {
        return input -> declaringClass(input).transform(handlerPackage(basePackages)).or(true);
    }
    @SuppressWarnings("deprecation")
    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }
    private static Function<Class<?>, Boolean> handlerPackage(final String[] basePackages) {
        return input -> {
            // 循环判断匹配
            for (String strPackage : basePackages) {
                boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                if (isMatch) {
                    return true;
                }
            }
            return false;
        };
    }
    /**
     * 按照类中字段顺序显示
     */
    @Component
    public static class ApiModelPropertyBuilderPlugin implements ModelPropertyBuilderPlugin {

        @Override
        public void apply(ModelPropertyContext context) {
            try {
                Optional<BeanPropertyDefinition> beanPropertyDefinitionOptional = context.getBeanPropertyDefinition();
                Optional<ApiModelProperty> annotation = Optional.absent();
                if (context.getAnnotatedElement().isPresent()) {
                    annotation = annotation.or(findApiModePropertyAnnotation(context.getAnnotatedElement().get()));
                }
                if (context.getBeanPropertyDefinition().isPresent()) {
                    annotation = annotation.or(findPropertyAnnotation(context.getBeanPropertyDefinition().get(), ApiModelProperty.class));
                }
                if (beanPropertyDefinitionOptional.isPresent()) {
                    BeanPropertyDefinition beanPropertyDefinition = beanPropertyDefinitionOptional.get();
                    if (annotation.isPresent() && annotation.get().position() != 0) {
                        return;
                    }
                    AnnotatedField annotatedField = beanPropertyDefinition.getField();
                    if (annotatedField == null) {
                        return;
                    }
                    Class<?> clazz = annotatedField.getDeclaringClass();
                    Field[] fields = clazz.getDeclaredFields();
                    // 获取当前字段对象
                    Field field = clazz.getDeclaredField(annotatedField.getName());
                    boolean required = false;
                    // 获取字段注解
                    NotNull notNull = field.getDeclaredAnnotation(NotNull.class);
                    NotBlank notBlank = field.getDeclaredAnnotation(NotBlank.class);
                    if (notNull != null || notBlank != null) {
                        required = true;
                    }
                    int position = ArrayUtils.indexOf(fields, field);
                    if (position != -1) {
                        context.getBuilder().position(position).required(required);
                    }
                }
            } catch (Exception exception) {
                log.error("Swagger ApiModelProperty预处理异常", exception);
            }
        }

        @Override
        public boolean supports(DocumentationType delimiter) {
            return SwaggerPluginSupport.pluginDoesApply(delimiter);
        }
    }
}
