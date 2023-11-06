package com.thinktank.generator.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.thinktank.common.validationgroups.InsertValidation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author pippi
 * @since 2023-11-06
 */
@TableName("post_reports")
@ApiModel(value = "PostReports对象", description = "")
public class PostReports implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("举报id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @NotNull(groups = {InsertValidation.class}, message = "帖子id不能为空")
    @ApiModelProperty("帖子id")
    private Long postId;

    @ApiModelProperty("评论id（若为0代表举报的是帖子）")
    private Long commentId;

    @ApiModelProperty("举报用户id")
    private Long userId;

    @Min(groups = {InsertValidation.class}, value = 1, message = "举报类型非法")
    @Max(groups = {InsertValidation.class}, value = 8, message = "举报类型非法")
    @NotNull(groups = {InsertValidation.class}, message = "举报类型不能为空")
    @ApiModelProperty("举报类型")
    private Integer reportTypeId;

    @ApiModelProperty("举报原因（若举报原因不为其他，则本字段为空）")
    private String reason;

    @ApiModelProperty("处理状态（0:处理中 1:已处理）")
    private Integer status;

    @ApiModelProperty("举报时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("处理时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getReportTypeId() {
        return reportTypeId;
    }

    public void setReportTypeId(Integer reportTypeId) {
        this.reportTypeId = reportTypeId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "PostReports{" +
                "id = " + id +
                ", postId = " + postId +
                ", commentId = " + commentId +
                ", userId = " + userId +
                ", reportTypeId = " + reportTypeId +
                ", reason = " + reason +
                ", status = " + status +
                ", createTime = " + createTime +
                ", updateTime = " + updateTime +
                "}";
    }
}
