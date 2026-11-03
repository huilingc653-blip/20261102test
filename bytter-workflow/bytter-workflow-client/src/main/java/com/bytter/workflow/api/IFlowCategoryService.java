package com.bytter.workflow.api;
import com.bytter.workflow.dto.FlowCategoryDTO;
import java.util.List;
/**
 * @author chenlw
 * @date 2020/03/04
 */
public interface IFlowCategoryService {
    FlowCategoryDTO findByNotificationId(String notificationId);
    FlowCategoryDTO findDtoByKey(String key);
    /**
     * 获取流程分类审批权限
     * @param categoryId 流程分类
     * @return
     */
    List<String> getApproveNewResByCategoryId(String categoryId);
    /**
     * 获取流程分类审批权限
     * @param key 流程分类
     * @return
     */
    List<String> getApproveNewResByKey(String key);
}
