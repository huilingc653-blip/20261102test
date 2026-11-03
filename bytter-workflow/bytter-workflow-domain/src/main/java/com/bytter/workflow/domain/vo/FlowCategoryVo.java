package com.bytter.workflow.domain.vo;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.entity.TreeEntity;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.workflow.domain.entity.FlowCategory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;
/**
 * Created by zhoul on 2017/9/27.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class FlowCategoryVo extends TreeEntity<FlowCategoryVo> {
    private String id;
    private String parentId;
    private String name;
    private String type;
    private String cateKey;
    private String url;
    private Integer sort;
    private String mobileUrl;
    private List<FlowCategoryVo> children;
    /**
     * 必须挂审批、0不是（默认），1是
     */
    private boolean mustApprove;
    /**
     * 必须挂审批、0不是（默认），1是
     * 兼容老vm
     */
    private String mustApproveStr;
    private String batchRevokeAbleStr;
    /**
     * 前端显示
     */
    private String mobileApprovalFlag;
    /**
     * 业务：决定了业务流程列表页是否有撤回功能
     */
    private boolean batchRevokeAble;
    /**
     * 资源值
     */
    private String resourceContent;
    /**
     * 菜单ID
     */
    private String menuId;

    private String menuName;
    public static FlowCategoryVo from(FlowCategory flowCategory) {
        FlowCategoryVo flowCategoryVo = BeanUtils.copyNewInstanceProperties(flowCategory, FlowCategoryVo.class);
        flowCategoryVo.setMustApproveStr(flowCategory.isMustApprove() ? CommonConstant.YES : CommonConstant.NO);
        flowCategoryVo.setBatchRevokeAbleStr(flowCategory.isBatchRevokeAble() ? CommonConstant.YES : CommonConstant.NO);
        flowCategoryVo.setMobileApprovalFlag(flowCategory.isMobileAveAble() ? CommonConstant.YES : CommonConstant.NO);
        return flowCategoryVo;
    }
}
