package com.bytter.workflow.domain.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bytter.core.base.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;
import javax.persistence.Version;
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("act_extend_model")
public class FlowModel extends BaseEntity {
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
    private String corpName;
    private String corpCode;
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
     * 数据库乐观锁
     */
    @Version
    private Integer version;
    /**
     * 部署版本
     */
    private Integer deploymentVersion;
    @AllArgsConstructor
    @Getter
    public enum Status{
        STOP(0),
        START(1),
        ;
        private final int code;
    }
}
