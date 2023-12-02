package com.thinktank.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉06⽇ 14:41
 * @Description: 代码生成器
 * @Version: 1.0
 */
public class Generator {
    private final static String url = "jdbc:mysql://192.168.88.150:3306/thinktank?serverTimezone=UTC&characterEncoding=utf8";
    private final static String username = "root";
    private final static String password = "pp520";

    public static void main(String[] args) {
        // 当前项目根路径
        String currentProjectPath = System.getProperty("user.dir");
        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("pippi") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .outputDir(currentProjectPath + "\\thinktank-generator\\src\\main\\java"); // 指定输出目录
                })
                .dataSourceConfig(builder -> builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                    int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                    if (typeCode == Types.SMALLINT) {
                        // 自定义类型转换
                        return DbColumnType.INTEGER;
                    }
                    return typeRegistry.getColumnType(metaInfo);

                }))
                .packageConfig(builder -> {
                    builder.parent("com.thinktank") // 设置父包名
                            .moduleName("generator") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, currentProjectPath + "\\thinktank-generator\\src\\main\\resources\\com\\thinktank\\generator\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    List<String> list = new ArrayList<>();
//                    list.add("block_application_block");
//                    list.add("block_application_master");
//                   list.add("block_follow");
//                    list.add("block_big_type");
//                    list.add("block_info");
//                    list.add("block_small_type");
//                    list.add("message_private");
//                    list.add("mq_task");
//                    list.add("post_comment_likes");
//                    list.add("post_comments");
//                    list.add("post_info");
//                    list.add("post_likes");
//                    list.add("post_report_type");
//                    list.add("post_reports");
//                    list.add("post_score");
//                    list.add("sys_menu");
//                    list.add("sys_role");
//                    list.add("sys_role_menu");
//                    list.add("sys_user");
//                    list.add("sys_user_role");

                    builder.addInclude(list) // 设置需要生成的表名
                            .entityBuilder()
                            .idType(IdType.ASSIGN_ID) // id采用雪花算法生成
                            .addTableFills(new Column("create_time", FieldFill.INSERT))
                            .addTableFills(new Column("update_time", FieldFill.INSERT_UPDATE))
                            .logicDeleteColumnName("del_flag"); // 逻辑删除
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateConfig(builder ->
                        builder.disable(
                                TemplateType.CONTROLLER,
                                TemplateType.SERVICE,
                                TemplateType.SERVICE_IMPL))
                .execute();
    }
}
