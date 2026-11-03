package com.bytter.workflow.dto;
import lombok.Builder;
import lombok.Data;
import java.io.Serializable;
/**
 * 流程启动参数
 */
@Data
@Builder
public class PIStartDTO implements Serializable {
    /**
     * 流程分类key
     */
    private String cateKey;
    /**
     * 关联业务ID
     */
    private String businessKey;
    /**
     * 启动流程用户
     */
    private String userId;
    /**
     * 启动单位
     */
    private String corpId;
    /**
     * 评论
     */
    private String comment;
}
