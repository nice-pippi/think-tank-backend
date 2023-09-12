package com.thinktank.generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author pippi
 * @since 2023-09-11
 */
@TableName("post_report_type")
@ApiModel(value = "PostReportType对象", description = "")
public class PostReportType implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("举报类型id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Integer id;

    @ApiModelProperty("举报类型名称")
    private String typeName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return "PostReportType{" +
            "id = " + id +
            ", typeName = " + typeName +
        "}";
    }
}
