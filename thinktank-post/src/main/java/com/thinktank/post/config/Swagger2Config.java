package com.thinktank.post.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @Author: 弘
 * @CreateTime: 2023年010⽉20⽇ 19:05
 * @Description: Swagger2配置类
 * @Version: 1.0
 */
@EnableOpenApi
@Configuration
public class Swagger2Config {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                // 指定构建api文档的详细信息的方法：apiInfo()
                .apiInfo(apiInfo())
                .select()
                // 指定要生成api接口的包路径，这里把controller作为包路径，生成controller中的所有接口
                .apis(RequestHandlerSelectors.basePackage("com.thinktank.post.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 构建api文档的详细信息
     *
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 设置页面标题
                .title("帖子模块接口文档")
                // 设置接口描述
                .description("用于帖子管理、点赞以及评论等操作")
                // 设置版本
                .version("1.0")
                // 构建
                .build();
    }
}
