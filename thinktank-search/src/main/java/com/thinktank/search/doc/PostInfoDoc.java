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
import java.util.List;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉21⽇ 18:50
 * @Description: 帖子信息文档实体类
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "post_info")
public class PostInfoDoc {
    @Id
    private Long id;

    @Field(type = FieldType.Keyword, index = false)
    private Long blockId;

    @Field(type = FieldType.Keyword, index = false)
    private String blockName;

    @NotEmpty(groups = QueryValidation.class, message = "帖子标题不能为空")
    @Field(type = FieldType.Text, searchAnalyzer = "ik_max_word")
    private String title;

    @Field(type = FieldType.Text, searchAnalyzer = "ik_max_word")
    private String content;

    @Field(type = FieldType.Keyword, index = false)
    private List<String> images;

    @Field(type = FieldType.Keyword, index = false)
    private Long userId;

    @Field(type = FieldType.Keyword, index = false)
    private String username;

    @Field(type = FieldType.Date, index = false, format = DateFormat.date_hour_minute_second)
    private LocalDateTime createTime;
}
