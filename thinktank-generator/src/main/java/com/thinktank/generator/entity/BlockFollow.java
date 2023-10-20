package com.thinktank.generator.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2023-10-20
 */
@TableName("block_follow")
@ApiModel(value = "BlockFollow对象", description = "")
public class BlockFollow implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("板块id")
    private Long blockId;

    @ApiModelProperty("关注用户id")
    private Long userId;

    @ApiModelProperty("关注时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    public Long getBlockId() {
        return blockId;
    }

    public void setBlockId(Long blockId) {
        this.blockId = blockId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "BlockFollow{" +
            "blockId = " + blockId +
            ", userId = " + userId +
            ", createTime = " + createTime +
        "}";
    }
}
