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
@TableName("mq_task")
@ApiModel(value = "MqTask对象", description = "")
public class MqTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("任务id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("业务id1")
    private Long businessIdFirst;

    @ApiModelProperty("业务id2")
    private Long businessIdSecond;

    @ApiModelProperty("业务id3")
    private Long businessIdThird;

    @ApiModelProperty("任务状态（0:待处理 1:已处理 2:处理失败）")
    private Integer status;

    @ApiModelProperty("处理失败原因（仅在处理失败时记录该信息）")
    private String reason;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusinessIdFirst() {
        return businessIdFirst;
    }

    public void setBusinessIdFirst(Long businessIdFirst) {
        this.businessIdFirst = businessIdFirst;
    }

    public Long getBusinessIdSecond() {
        return businessIdSecond;
    }

    public void setBusinessIdSecond(Long businessIdSecond) {
        this.businessIdSecond = businessIdSecond;
    }

    public Long getBusinessIdThird() {
        return businessIdThird;
    }

    public void setBusinessIdThird(Long businessIdThird) {
        this.businessIdThird = businessIdThird;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "MqTask{" +
            "id = " + id +
            ", businessIdFirst = " + businessIdFirst +
            ", businessIdSecond = " + businessIdSecond +
            ", businessIdThird = " + businessIdThird +
            ", status = " + status +
            ", reason = " + reason +
        "}";
    }
}
