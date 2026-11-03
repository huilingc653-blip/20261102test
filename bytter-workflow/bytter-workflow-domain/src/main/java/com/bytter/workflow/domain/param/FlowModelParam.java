package com.bytter.workflow.domain.param;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = false)
public class FlowModelParam {
    private String id;
    private String name;
    /**
     * 流程定义ID(act_re_prodef)
     */
    private String proDefId;
    /**
     * 流程定义key
     */
    private String proDefKey;
    /**
     * 分类Id
     */
    private String categoryId;
    private String tenantId;
    private String corpId;
    private String deploymentId;
    /**
     * 是否能撤回
     */
    private boolean revokeAble;
    /**
     * 是否能批量撤回
     */
    private boolean batchRevokeAble;
    /**
     * 是否必须挂接审批
     */
    private boolean mustApprove;
    private String modelData;
    /**
     * 是否允许手机审批
     */
    private boolean mobileAveAble;
    /**
     * 0停用 1 启用
     */
    private Integer status;
    /**
     * 模型版本
     */
    private Integer modelVersion;
    /**
     * 部署版本
     */
    private Integer deploymentVersion;
}
