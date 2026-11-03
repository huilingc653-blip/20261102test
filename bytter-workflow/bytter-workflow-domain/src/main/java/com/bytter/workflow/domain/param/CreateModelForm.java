package com.bytter.workflow.domain.param;
import com.bytter.core.common.exception.AssertHelper;
import com.bytter.core.constant.CommonConstant;
import com.bytter.workflow.domain.entity.FlowCategory;
import com.bytter.workflow.domain.entity.FlowModel;
import com.bytter.workflow.domain.service.FlowCategoryService;
import com.bytter.workflow.enums.FlowCategoryMessageEnum;
import lombok.Data;
import java.util.Objects;
/**
 * 模型创建
 */
@Data
public class CreateModelForm {
    private String flowCategoryId;
    private String flowCategoryName;
    private String corpId;
    private String corpCode;
    private String corpName;
    private String revokeAble;
    public FlowModel toEntity(FlowCategoryService categoryService) {
        FlowCategory category = categoryService.findById(this.flowCategoryId);
        AssertHelper.isTrue(Objects.nonNull(category), FlowCategoryMessageEnum.BPM_FLOW_CATEGORY_KEY_NOT_NULL);
        return new FlowModel().setName(category.getName())
                .setCategoryId(category.getId())
                .setModelVersion(1)
                .setCorpId(this.corpId)
                .setCorpName(this.corpName)
                .setCorpCode(this.corpCode)
                .setRevokeAble(CommonConstant.YES.equals(revokeAble))
                .setMustApprove(category.isMustApprove())
                .setMobileAveAble(category.isMobileAveAble())
                .setProDefKey(category.getCateKey())
                .setStatus(FlowModel.Status.START.getCode());
    }
}
