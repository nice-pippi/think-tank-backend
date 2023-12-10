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
 * @since 2023-12-09
 */
@TableName("message_chat_room")
@ApiModel(value = "MessageChatRoom对象", description = "")
public class MessageChatRoom implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("聊天室id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("最新聊天内容")
    private String latestContent;

    @ApiModelProperty("用户a")
    private Long userIdA;

    @ApiModelProperty("用户b")
    private Long userIdB;

    @ApiModelProperty("创建聊天时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("聊天记录更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLatestContent() {
        return latestContent;
    }

    public void setLatestContent(String latestContent) {
        this.latestContent = latestContent;
    }

    public Long getUserIdA() {
        return userIdA;
    }

    public void setUserIdA(Long userIdA) {
        this.userIdA = userIdA;
    }

    public Long getUserIdB() {
        return userIdB;
    }

    public void setUserIdB(Long userIdB) {
        this.userIdB = userIdB;
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
        return "MessageChatRoom{" +
            "id = " + id +
            ", latestContent = " + latestContent +
            ", userIdA = " + userIdA +
            ", userIdB = " + userIdB +
            ", createTime = " + createTime +
            ", updateTime = " + updateTime +
        "}";
    }
}
