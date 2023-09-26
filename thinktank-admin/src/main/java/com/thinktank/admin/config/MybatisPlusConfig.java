package com.thinktank.admin.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;


/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉24⽇ 17.13
 * @Description: MybatisPlus配置
 * @Version: 1.0
 */
@MapperScan("com.thinktank.generator.mapper")
@Configuration
public class MybatisPlusConfig {

}