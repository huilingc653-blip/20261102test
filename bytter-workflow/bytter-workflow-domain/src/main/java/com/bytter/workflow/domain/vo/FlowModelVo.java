package com.bytter.workflow.domain.vo;
import cn.hutool.core.codec.Base64;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.StringUtils;
import com.bytter.workflow.domain.entity.FlowModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.nio.charset.StandardCharsets;
import java.util.Date;
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FlowModelVo {
    private String modelId;
    private String modelName;
    private String modelData;
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
    private String corpCode;
    private String corpName;
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
    private String createBy;
    private String updateBy;
    /**
     * 模型创建时间
     */
    private Date modelCreateTime;
    /**
     * 模型修改时间
     */
    private Date modelLastUpdateTime;
    public static FlowModelVo from(FlowModel flowModel) {
        return new FlowModelVo().setModelId(flowModel.getId())
                .setModelName(flowModel.getName())
                .setCategoryId(flowModel.getCategoryId())
                .setCorpId(flowModel.getCorpId())
                .setCorpCode(flowModel.getCorpCode())
                .setCorpName(flowModel.getCorpName())
                .setModelVersion(flowModel.getModelVersion())
                .setModelCreateTime(flowModel.getCreateDate())
                .setModelLastUpdateTime(flowModel.getUpdateDate())
                .setDeploymentId(flowModel.getDeploymentId())
                .setDeploymentVersion(flowModel.getDeploymentVersion())
                .setStatus(flowModel.getStatus())
                .setModelData(StringUtils.isBlank(flowModel.getModelData()) ?
                        CommonConstant.EMPTY_STR : Base64.encode(flowModel.getModelData(), StandardCharsets.UTF_8));
    }
}
