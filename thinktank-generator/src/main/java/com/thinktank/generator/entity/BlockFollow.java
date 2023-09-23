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
 * @since 2023-09-23
 */
@TableName("block_follow")
@ApiModel(value = "BlockFollow对象", description = "")
public class BlockFollow implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("板块关注记录id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("小分类板块id")
    private Long smallTypeId;

    @ApiModelProperty("关注用户id")
    private Long userId;

    @ApiModelProperty("关注时间id")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSmallTypeId() {
        return smallTypeId;
    }

    public void setSmallTypeId(Long smallTypeId) {
        this.smallTypeId = smallTypeId;
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
            "id = " + id +
            ", smallTypeId = " + smallTypeId +
            ", userId = " + userId +
            ", createTime = " + createTime +
        "}";
    }
}
