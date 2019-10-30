/*
 * @(#) SwaggerConfig.java
 * @Author:xinyz(xinyz@usky.com.cn) 2017/5/18
 * @Copyright (c) 2002-2017 usky.com Limited. All rights reserved.
 */
package com.ai.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author xinyz(xinyz @ usky.com.cn) 2017年5月18日
 * @version 1.0
 * @Function Swagger配置
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * api(swagger bean配置)
     *
     * @return bean
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("/api/.*"))
                .build()
                .tags(new Tag("my-redis", "my-redis"), getTags())
                .apiInfo(apiInfo());
    }

    private Tag[] getTags() {
        Tag[] tags = {
                new Tag("redis-test", "redis 测试"),
        };
        return tags;
    }

    /**
     * apiInfo(api信息配置)
     *
     * @return api信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("my-redis").description("DESCRIPTION").version("VERSION").termsOfServiceUrl("http://terms-of-services.url")
                .license("LICENSE").licenseUrl("http://url-to-license.com").build();
    }
}
