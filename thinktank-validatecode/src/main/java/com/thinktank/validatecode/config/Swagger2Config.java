package com.thinktank.validatecode.config;

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
 * @CreateTime: 2023年09⽉10⽇ 17:08
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
                .apis(RequestHandlerSelectors.basePackage("com.thinktank.validatecode.controller"))
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
                .title("验证码模块接口文档")
                // 设置接口描述
                .description("用于生成以及校验验证码")
                // 设置版本
                .version("1.0")
                // 构建
                .build();
    }
}
