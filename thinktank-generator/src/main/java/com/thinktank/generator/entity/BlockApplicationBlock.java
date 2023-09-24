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
 * @since 2023-09-24
 */
@TableName("block_application_block")
@ApiModel(value = "BlockApplicationBlock对象", description = "")
public class BlockApplicationBlock implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("申请id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("申请用户id")
    private Long userId;

    @ApiModelProperty("小分类板块id")
    private Long smallTypeId;

    @ApiModelProperty("板块名称")
    private String blockName;

    @ApiModelProperty("板块介绍")
    private String description;

    @ApiModelProperty("申请创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("处理结果（0:待处理 1:已通过 2:驳回）")
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSmallTypeId() {
        return smallTypeId;
    }

    public void setSmallTypeId(Long smallTypeId) {
        this.smallTypeId = smallTypeId;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BlockApplicationBlock{" +
            "id = " + id +
            ", userId = " + userId +
            ", smallTypeId = " + smallTypeId +
            ", blockName = " + blockName +
            ", description = " + description +
            ", createTime = " + createTime +
            ", updateTime = " + updateTime +
            ", status = " + status +
        "}";
    }
}
