package com.thinktank.generator.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
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
@TableName("message_private")
@ApiModel(value = "MessagePrivate对象", description = "")
public class MessagePrivate implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("私信id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("发送者用户id")
    private Long sendUserId;

    @ApiModelProperty("接收者用户id")
    private Long acceptUserId;

    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("是否已读（0:未读 1:已读）")
    private Integer readFlag;

    @ApiModelProperty("发送时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(Long sendUserId) {
        this.sendUserId = sendUserId;
    }

    public Long getAcceptUserId() {
        return acceptUserId;
    }

    public void setAcceptUserId(Long acceptUserId) {
        this.acceptUserId = acceptUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getReadFlag() {
        return readFlag;
    }

    public void setReadFlag(Integer readFlag) {
        this.readFlag = readFlag;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "MessagePrivate{" +
            "id = " + id +
            ", sendUserId = " + sendUserId +
            ", acceptUserId = " + acceptUserId +
            ", content = " + content +
            ", readFlag = " + readFlag +
            ", createTime = " + createTime +
        "}";
    }
}
