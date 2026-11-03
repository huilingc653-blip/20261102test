package com.bytter.workflow.domain.service;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytter.workflow.domain.entity.FlowCategory;
import com.bytter.workflow.domain.entity.FlowDelegationRel;
import com.bytter.workflow.domain.mapper.FlowDelegationRelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class FlowDelegationRelService extends ServiceImpl<FlowDelegationRelMapper, FlowDelegationRel> {
    private final FlowCategoryService flowCategoryService;
    public FlowDelegationRelService(FlowCategoryService flowCategoryService) {
        this.flowCategoryService = flowCategoryService;
    }
    /**
     * 根据 转授ID删除 关系表
     * @param delegationId 转授ID
     */
    public boolean deleteByDelegationId(String delegationId) {
        return new LambdaUpdateChainWrapper<>(FlowDelegationRel.class)
                .eq(FlowDelegationRel::getDelegationId, delegationId)
                .remove();
    }
    /**
     * 根据 转授ID删除 关系表
     * @param delegationIds 转授ID
     */
    public boolean deleteByDelegationIds(List<String> delegationIds) {
        return new LambdaUpdateChainWrapper<>(FlowDelegationRel.class)
                .in(FlowDelegationRel::getDelegationId, delegationIds).remove();
    }
    /**
     * 添加流程转授与分类对应关系表
     */
    public boolean cleanAndSaveByIds(String delegationId, List<String> cateIds) {
        Map<String, FlowCategory> categoryMap = flowCategoryService.mapGroupById(cateIds);
        List<FlowDelegationRel> flowDelegationRels = cateIds.stream()
                .map(cateId -> Optional.ofNullable(categoryMap.get(cateId))
                        .map(category -> new FlowDelegationRel()
                                .setCateId(cateId).setCateKey(category.getCateKey()))
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return cleanAndSave(delegationId, flowDelegationRels);
    }
    /**
     * 添加流程转授与分类对应关系表
     */
    public boolean cleanAndSave(String delegationId, List<FlowDelegationRel> flowDelegationRels) {
        flowDelegationRels.forEach(item -> item.setDelegationId(delegationId));
        this.deleteByDelegationId(delegationId);
        return this.saveBatch(flowDelegationRels);
    }
    /**
     * 根据转授id获取转授业务
     * @return
     */
    public List<FlowDelegationRel> listByDelegationId(String delegationId) {
        return new LambdaQueryChainWrapper<>(FlowDelegationRel.class)
                .eq(FlowDelegationRel::getDelegationId, delegationId).list();
    }
    /**
     * 根据转授id获取转授业务分类key
     */
    public String[] listCateKeysByDelegationId(String delegationId) {
        List<FlowDelegationRel> flowDelegationRels = this.listByDelegationId(delegationId);
        return flowDelegationRels.stream().map(FlowDelegationRel::getCateKey).toArray(String[]::new);
    }
    /**
     * 根据转授id获取转授业务分类key
     */
    public List<String> listCateIdsByDelegationId(String delegationId) {
        List<FlowDelegationRel> flowDelegationRels = listByDelegationId(delegationId);
        return flowDelegationRels.stream().map(FlowDelegationRel::getCateId).collect(Collectors.toList());
    }
}
