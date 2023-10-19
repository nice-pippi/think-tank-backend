package com.thinktank.search.doc;

import com.thinktank.common.validationgroups.QueryValidation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉17⽇ 16:39
 * @Description: 板块信息文档实体类
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "block_info")
public class BlockInfoDoc {
    @Id
    private Long id;

    @Field(type = FieldType.Keyword, index = false)
    private String avatar;

    @NotEmpty(groups = QueryValidation.class,message = "板块名称不能为空")
    @Field(type = FieldType.Text, searchAnalyzer = "ik_max_word")
    private String blockName;

    @Field(type = FieldType.Text, searchAnalyzer = "ik_max_word")
    private String description;

    @Field(type = FieldType.Date, index = false, format = DateFormat.date_hour_minute_second)
    private LocalDateTime createTime;

    @Field(type = FieldType.Date, index = false, format = DateFormat.date_hour_minute_second)
    private LocalDateTime updateTime;
}
