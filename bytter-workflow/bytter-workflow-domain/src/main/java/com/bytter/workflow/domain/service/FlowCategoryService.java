package com.bytter.workflow.domain.service;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytter.core.base.entity.IdEntity;
import com.bytter.core.common.exception.AssertHelper;
import com.bytter.core.constant.SysResourceConstant;
import com.bytter.core.dict.DictInDTO;
import com.bytter.core.dict.DictInjectProvider;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.utils.TreeUtil;
import com.bytter.system.api.IMenuService;
import com.bytter.workflow.api.IFlowCategoryService;
import com.bytter.workflow.constant.FlowCategoryConstant;
import com.bytter.workflow.domain.entity.FlowCategory;
import com.bytter.workflow.domain.mapper.FlowCategoryMapper;
import com.bytter.workflow.domain.vo.FlowCategoryVo;
import com.bytter.workflow.dto.FlowCategoryDTO;
import com.bytter.workflow.enums.FlowCategoryMessageEnum;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import static com.bytter.workflow.constant.FlowCategoryConstant.FLOW_CATEGORY_DEFAULT_PARENT;
import static com.bytter.workflow.constant.FlowCategoryConstant.FLOW_CATEGORY_TYPE_APPROVE;
/**
 * @author zhoul
 * @version 1.0
 */
@Service
@Transactional
public class FlowCategoryService extends ServiceImpl<FlowCategoryMapper,FlowCategory> implements IFlowCategoryService {
    private static final String FLOW_CATEGORY_CACHE_NAME = "flowCategoryCache";
    private final IMenuService menuService;
    
    @Autowired
    private FlowCategoryMapper flowCategoryMapper;
    
//    @Autowired
//    private ILicenseService licenseService;
    
    public FlowCategoryService(IMenuService menuServiceI) {
        this.menuService = menuServiceI;
    }
    public String getFullCategoryNameByKey(String key) {
        return getFullCategoryNameByKey(key, null);
    }
    /**
     * 获取分类名称
     *
     * @param key   分类key （唯一）
     * @param split 分隔符
     */
    public String getFullCategoryNameByKey(String key, String split) {
        StringBuffer sb = new StringBuffer();
        FlowCategory flowCategory = findByKey(key);
        if (flowCategory != null) {
            getParentIds(flowCategory.getId(), sb, StringUtils.isBlank(split) ? " > " : split);
        }
        return sb.toString();
    }
    public String getCategoryNameByKey(String key) {
        FlowCategory flowCategory = findByKey(key);
        if (flowCategory != null) {
            return flowCategory.getName();
        }
        return null;
    }
    /**
     * 递归查询流程分类的名称  父级分类名称 > 二级分类名称 > 三级分类名称
     *
     */
    private void getParentIds(String id, StringBuffer sb, String split) {
        FlowCategory flowCategory = this.findById(id);
        if (flowCategory == null) {
            return;
        }
        if (StringUtils.isNotBlank(flowCategory.getParentId())) {
            sb.insert(0, flowCategory.getName());
            sb.insert(0, split);
            getParentIds(flowCategory.getParentId(), sb, split);
        } else {
            sb.insert(0, flowCategory.getName());
        }
    }
    /**
     * 将第三层的分类统一提取到第二层
     * todo 有时间可以优化
     */
    /**
     * 将第三层的分类统一提取到第二层
     */
    public List<Map<String, Object>> makeSecondLevelCategory(String userId) {
        List<FlowCategory> flowCategoriesTemp = listToTree();
        List<FlowCategory> flowCategories = new ArrayList<>(flowCategoriesTemp.size());

        // 只显示移动端审批流程
        List<String> isNotMobileApprovalKeyList = getIsNotMobileApprovalKey();
        Set<String> isNotMobileApprovalKeySet = new HashSet<>(isNotMobileApprovalKeyList);

        // 菜单权限过滤
        Set<String> userHavePermissionMenuIdSet = menuService.getEnableMenuIdSetByUserId(userId);

        for (FlowCategory flowCategory : flowCategoriesTemp) {
//            if (!licenseService.isMenusAuth(flowCategory.getMenuId())) {
//                // 移动端流程分类审批加入License控制。
//                continue;
//            }
            if (!userHavePermissionMenuIdSet.contains(flowCategory.getMenuId())) {
                // 没有菜单权限则不显示
                continue;
            }
            if (isNotMobileApprovalKeySet.contains(flowCategory.getCateKey())) {
                // 过滤非移动端审批流程分类
                continue;
            }
            flowCategories.add(flowCategory);
        }
        List<Map<String, Object>> firstList = new ArrayList<>();
        for (FlowCategory flowCategory : flowCategories) {
            Map<String, Object> map = makeFlowCategory(flowCategory);
            List<FlowCategory> children = flowCategory.getChildren();
            if (children != null && !children.isEmpty()) {
                for (FlowCategory fc2 : children) {
                    if (isNotMobileApprovalKeySet.contains(fc2.getCateKey())) {
                        // 过滤非移动端审批流程分类
                        continue;
                    }
                    List<FlowCategory> children2 = fc2.getChildren();
                    List<Map<String, Object>> listMap = (List<Map<String, Object>>) map.get("data");
                    if (listMap == null) {
                        listMap = new ArrayList<>();
                    }
                    if (children2 != null && !children2.isEmpty()) {
                        List<Map<String, Object>> thirdList = new ArrayList<>();
                        for (FlowCategory fc3 : children2) {
                            if (isNotMobileApprovalKeySet.contains(fc3.getCateKey())) {
                                // 过滤非移动端审批流程分类
                                continue;
                            }
                            Map<String, Object> thirdCategory = makeFlowCategory(fc3);
                            thirdList.add(thirdCategory);
                        }
                        if (!thirdList.isEmpty()) {
                            listMap.addAll(thirdList);
                        }
                    } else {
                        listMap.add(makeFlowCategory(fc2));
                    }
                    map.put("data", listMap);
                }
            }
            firstList.add(map);
        }
        return firstList;
    }
    
    private Map<String, Object> makeFlowCategory(FlowCategoryVo flowCategory) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", flowCategory.getCateKey());
        map.put("name", flowCategory.getName());
        return map;
    }
    /**
     * 根据id删除
     *
     */
    public void delete(String id) {
        super.removeById(id);
    }
    private void nullableCheck(FlowCategory entity) {
        AssertHelper.isTrue(StringUtils.isNotBlank(entity.getCateKey()),FlowCategoryMessageEnum.BPM_FLOW_CATEGORY_KEY_NOT_NULL);
    }
    /**
     * 保存
     *
     * @param entity 流程分类
     */
    public void add(FlowCategory entity) {
        nullableCheck(entity);
        FlowCategory flowCategory = this.findByKey(entity.getCateKey());
        AssertHelper.isTrue(ObjectUtil.isNull(flowCategory), FlowCategoryMessageEnum.BPM_FLOW_CATEGORY_KEY_EXISIS);
        super.save(entity);
    }
    @Cacheable(value = FLOW_CATEGORY_CACHE_NAME, key = "#key")
    public FlowCategoryVo findVoByKey(String key) {
        return Optional.ofNullable(findByKey(key))
                       .map(FlowCategoryVo::from)
                       .orElse(null);
    }
    public FlowCategory findByKey(String key) {
        return new LambdaQueryChainWrapper<>(FlowCategory.class)
                .eq(FlowCategory::getCateKey, key).one();
    }
    @Override
    public FlowCategoryDTO findDtoByKey(String key) {
        return Optional.ofNullable(findByKey(key))
                .map(ele -> BeanUtils.copyNewInstanceProperties(ele, FlowCategoryDTO.class))
                .orElse(null);
    }
    /**
     * 更新
     *
     */
    @CacheEvict(value = FLOW_CATEGORY_CACHE_NAME, allEntries = true)
    public void update(FlowCategory entity) {
        nullableCheck(entity);
        super.updateById(entity);
    }
    /**
     * 当parentId为空则查询parantId为0的流程
     * @param parentId 父节点Id
     * @return 流程分类
     */
    public List<FlowCategoryVo> findByParentId(String parentId) {
        return new LambdaQueryChainWrapper<>(FlowCategory.class)
                .eq(FlowCategory::getParentId, StringUtils.isNotBlank(parentId) ? parentId : FLOW_CATEGORY_DEFAULT_PARENT)
                .list().stream().map(FlowCategoryVo::from)
                .collect(Collectors.toList());
    }
    /**
     * 列出所有审批流程
     *
     * @return 树形数据结构
     */
    public List<FlowCategoryVo> listAllApproveCategoryTreeTable() {
        return TreeUtil.createTreeTier(listAllApproveCategory());
    }
    /**
     * 列出所有审批流程
     *
     * @return 树形数据结构
     */
    public List<FlowCategoryVo> listAllApproveCategoryTree() {
        return TreeUtil.coverTreeList(listAllApproveCategory());
    }
    /**
     * 列出所有审批流程
     *
     */
    public List<FlowCategoryVo> listAllApproveCategory() {
        return new LambdaQueryChainWrapper<>(FlowCategory.class)
                .eq(FlowCategory::getType, FLOW_CATEGORY_TYPE_APPROVE)
                .list().stream().map(item -> BeanUtils.copyNewInstanceProperties(item, FlowCategoryVo.class))
                .collect(Collectors.toList());
    }
    public List<FlowCategoryVo> listApproveCategoryTreeInKey(List<String> cateKeys) {
        return buildTreeByCateKeys(cateKeys);
    }
    //数组组合 todo 需要优化
    private List<FlowCategoryVo>  buildTreeByCateKeys(List<String> cateKeys) {
        List<FlowCategoryVo> newFlowCategory = new ArrayList<>();
        List<FlowCategoryVo> flowCategories = this.listAllApproveCategoryTree();
        for (FlowCategoryVo flowCategory : flowCategories) {
            List<FlowCategoryVo> temp = new ArrayList<>();
            //遍历三级树节点
            List<FlowCategoryVo> childrenListA = flowCategory.getChildren();
            if (childrenListA != null && childrenListA.size() > 0) {
                for (FlowCategoryVo flowCategoryA : childrenListA) {
                    if (cateKeys.contains(flowCategoryA.getCateKey())) {
                        temp.add(flowCategoryA);
                    }
                    List<FlowCategoryVo> childrenListB = flowCategoryA.getChildren();
                    if (childrenListB != null && childrenListB.size() > 0) {
                        for (FlowCategoryVo flowCategoryB : childrenListB) {
                            if (cateKeys.contains(flowCategoryB.getCateKey())) {
                                temp.add(flowCategoryB);
                            }

                            List<FlowCategoryVo> childrenListC = flowCategoryB.getChildren();
                            if (childrenListC != null && childrenListC.size() > 0) {
                                for (FlowCategoryVo flowCategoryC : childrenListC) {
                                    if (cateKeys.contains(flowCategoryC.getCateKey())) {
                                        temp.add(flowCategoryC);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            temp.sort((o1, o2) -> {
                if (o1.getSort() != null && o2.getSort() != null) {
                    return o1.getSort() - o2.getSort();
                }
                return 0;
            });
            if (temp.size() > 0) {
                flowCategory.setChildren(temp);
                newFlowCategory.add(flowCategory);
            }
        }
        return newFlowCategory;
    }
    /**
     * 列出所有在分类key中的审批流程
     *
     */
    public List<FlowCategoryVo> listApproveCategoryInKey(List<String> cateKeys) {
        return new LambdaQueryChainWrapper<>(FlowCategory.class)
                .eq(FlowCategory::getType, FLOW_CATEGORY_TYPE_APPROVE)
                .in(FlowCategory::getCateKey, cateKeys)
                .list().stream().map(FlowCategoryVo::from)
                .collect(Collectors.toList());
    }
    /**
     * 模糊查找key
     *
     */
    public List<String> findKeyByLike(String key) {
        return new LambdaQueryChainWrapper<>(FlowCategory.class)
                .like(FlowCategory::getCateKey, key)
                .list().stream().map(FlowCategory::getCateKey).collect(Collectors.toList());
    }
    public List<String> findKeyByNotLike(String key) {
        return new LambdaQueryChainWrapper<>(FlowCategory.class)
                .notLike(FlowCategory::getCateKey, key)
                .list().stream().map(FlowCategory::getCateKey).collect(Collectors.toList());
    }
    /**
     * 获取不是移动审批的流程分类KEY
     *
     * @return 不是移动审批的流程分类KEY
     */
    public List<String> getIsNotMobileApprovalKey() {
        return new LambdaQueryChainWrapper<>(FlowCategory.class)
                .eq(FlowCategory::isMobileAveAble, FlowCategoryConstant.IS_NOT_MOBILE_APPROVAL)
                .list().stream().map(FlowCategory::getCateKey).collect(Collectors.toList());
    }

    public List<String> getIsNotMobileApprovalKey(List<String> categoryList) {
        return new LambdaQueryChainWrapper<>(FlowCategory.class)
                .in(FlowCategory::getCateKey, categoryList)
                .eq(FlowCategory::isMobileAveAble, FlowCategoryConstant.IS_NOT_MOBILE_APPROVAL)
                .list().stream().map(FlowCategory::getCateKey).collect(Collectors.toList());
    }

    /**
     * 获取是移动审批的流程分类KEY
     *
     * @return 是移动审批的流程分类KEY
     */
    public List<String> getIsMobileApprovalKey() {
        return new LambdaQueryChainWrapper<>(FlowCategory.class)
                // 值为1或空置表示是（默认）
                .eq(FlowCategory::isMobileAveAble, FlowCategoryConstant.IS_MOBILE_APPROVAL)
                .or().isNull(FlowCategory::isMobileAveAble)
                .list().stream().map(FlowCategory::getCateKey).collect(Collectors.toList());
    }
    /**
     *
     * @param mobileAveAble 是否手机审批链接
     * @return 流程分类key
     */
    public List<String> getKeyByMobileAveAble(int mobileAveAble) {
        return new LambdaQueryChainWrapper<>(FlowCategory.class)
                .eq(FlowCategory::isMobileAveAble, mobileAveAble)
                .list().stream().map(FlowCategory::getCateKey).collect(Collectors.toList());
    }
    /**
     * 根据通知类型ID获取流程分类
     *
     * @param notificationId 通知类型ID
     * @return 流程分类
     */
    @Override
    public FlowCategoryDTO findByNotificationId(String notificationId) {
        return new LambdaQueryChainWrapper<>(FlowCategory.class)
                .eq(FlowCategory::getNotificationId, notificationId)
                .oneOpt().map(item->BeanUtils.copyNewInstanceProperties(item,FlowCategoryDTO.class)).orElse(null);
    }
    /**
     * 通过ID查询流程分类
     * @param id 流程分类ID
     * @return 流程分类VO
     */
    public FlowCategory findById(String id) {
        return super.getById(id);
    }
    /**
     * 通过ID查询流程分类
     * @param id 流程分类ID
     * @return 流程分类VO
     */
    public FlowCategoryVo findVoById(String id) {
        return Optional.ofNullable(findById(id))
                .map(FlowCategoryVo::from)
                .orElse(null);
    }
    /**
     * 根据主键获取流程分类对应审批资源
     */
    @Override
    public List<String> getApproveNewResByCategoryId(String categoryId) {
        return buildApproveNewRes(findById(categoryId));
    }
    @Override
    public List<String> getApproveNewResByKey(String key) {
        return buildApproveNewRes(this.findByKey(key));
    }
    private List<String> buildApproveNewRes(FlowCategory flowCategory) {
        List<String> resList = Lists.newArrayList(SysResourceConstant.SYS_FLOW_APPROVE);
        Optional.ofNullable(flowCategory).map(FlowCategory::getResourceContent).ifPresent(resList::add);
        return resList;
    }
    public Set<String> batchRevokeUrl() {
        List<FlowCategoryVo> flowCategories = this.listAllApproveCategory();
        return flowCategories.stream()
                .filter(ele -> StringUtils.isNotBlank(ele.getUrl()) && ele.isBatchRevokeAble())
                .map(FlowCategoryVo::getUrl).collect(Collectors.toSet());
    }
    public Map<String, FlowCategory> mapGroupById(List<String> cateIds) {
        List<FlowCategory> flowCategories = this.listByIds(cateIds);
        return flowCategories.stream().collect(Collectors.toMap(IdEntity::getId, item -> item));
    }
    
    public List<FlowCategory> listToTree() {
    	QueryWrapper<FlowCategory> wrapper = new QueryWrapper<FlowCategory>();
        wrapper.eq("type",FlowCategoryConstant.FLOW_CATEGORY_TYPE_APPROVE);
        wrapper.orderByDesc("sort");
        return listToTree2(flowCategoryMapper.selectList(wrapper));
    }
    
    private Map<String, Object> makeFlowCategory(FlowCategory flowCategory) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", flowCategory.getCateKey());
        map.put("name", flowCategory.getName());
//        map.put("data", flowCategory.getChildren());
        return map;
    }
    
    public List<FlowCategory> listToTree2(List mList) {
        return TreeUtil.coverTreeList(mList);
    }

}
