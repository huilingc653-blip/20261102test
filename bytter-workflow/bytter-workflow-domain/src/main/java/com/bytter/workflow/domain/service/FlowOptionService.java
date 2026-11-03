package com.bytter.workflow.domain.service;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytter.core.utils.StringUtils;
import com.bytter.workflow.constant.FlowOptioneConst;
import com.bytter.workflow.domain.entity.FlowCategory;
import com.bytter.workflow.domain.entity.FlowOption;
import com.bytter.workflow.domain.mapper.FlowOptionMapper;
import com.bytter.workflow.domain.vo.FlowOptionVO;
import org.assertj.core.util.Lists;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;
/**
 * Created by zhoul on 2017/8/3.
 *
 * @history
 * 1. 2020-01-02  chenlw  优化定制显示字段；新增initStableExtendParamsByCategoryKey()方法
 */
@Service("optionService")
@Transactional
public class FlowOptionService extends ServiceImpl<FlowOptionMapper, FlowOption> {
    private final FlowCategoryService flowCategoryService;
    private static final String OPTION_CACHE_NAME = "optionCache";
    //固定配置
    private static List<FlowOption> fixedOption;
    public FlowOptionService(FlowCategoryService flowCategoryService) {
        this.flowCategoryService = flowCategoryService;
    }
    /**
     * 初始化固定配置
     */
    @PostConstruct
    public List<FlowOption> initFixedOption() {
        fixedOption = Arrays.stream(FlowOptioneConst.OptionExtend.values())
                .map(ele -> FlowOption.createExtendOption(ele.getName(), ele.getFieldName(), ele.ordinal()))
                .collect(Collectors.toList());
        return fixedOption;
    }
    public List<FlowOption> listByCateKeyAndType(String cateKey, FlowOptioneConst.OptionType optionType) {
        FlowCategory flowCategory = flowCategoryService.findByKey(cateKey);
        if (Objects.isNull(flowCategory)) {
            return Lists.emptyList();
        }
        return listByByCateIdAndType(flowCategory.getId(), optionType);
    }
    public List<FlowOption> listByByCateIdAndType(String cateId, FlowOptioneConst.OptionType optionType) {
        return new LambdaQueryChainWrapper<>(FlowOption.class)
                .eq(FlowOption::getCategory,cateId)
                .eq(FlowOption::getType, optionType.getCode())
                .list();
    }
    /**
     * 2019-01-15
     * chenlw
     * work：查询单据配置
     * todo 只取第一个(暂时遗留原始逻辑)？
     * @param categoryKey 流程分类KEY
     * @return 单据配置
     */
    @Cacheable(value = OPTION_CACHE_NAME, key = "#categoryKey + '_type4'")
    public FlowOption getInvoiceConfigByCategoryKey(String categoryKey) {
        List<FlowOption> list = listByCateKeyAndType(categoryKey, FlowOptioneConst.OptionType._04);
        return list.isEmpty() ? null : list.get(0);
    }
    /**
     * 获取定制显示的字段列表
     * 后续操作对列表进行操作，因此不用缓存，否则会导致不正确的结果。
     *
     * @param categoryKey  流程分类KEY
     * @return 定制显示的字段列表
     */
    public List<FlowOption> getExtendParamsListByCategoryKey(String categoryKey) {
        return listByCateKeyAndType(categoryKey, FlowOptioneConst.OptionType._05);
    }
    /**
     * 获取定制显示的字段列表（状态为可见的）
     * 后续操作对列表进行操作，因此不用缓存，否则会导致不正确的结果。
     *
     * @param categoryKey   流程分类KEY
     * @return 定制显示的字段列表（状态为可见的）
     */
    public List<FlowOption> getVisibleExtendParamsListByCategoryKey(String categoryKey) {
        return getExtendParamsListByCategoryKey(categoryKey).stream()
                .filter(FlowOption::isVisible).collect(Collectors.toList());
    }
    public List<FlowOptionVO> listByCategoryId(String categoryId) {
        List<FlowOption> list = new LambdaQueryChainWrapper<>(FlowOption.class)
                .eq(FlowOption::getCategory, categoryId)
                .orderByAsc(FlowOption::getCreateTime)
                .list();
        return list.stream().map(FlowOptionVO::from).collect(Collectors.toList());
    }

    public List<FlowOption> listConditionByCategoryId(String categoryId) {
        return listByByCateIdAndType(categoryId, FlowOptioneConst.OptionType._01);
    }

    public FlowOption getMessageTemplateByCategory(String categoryId) {
        List<FlowOption> list = listByByCateIdAndType(categoryId, FlowOptioneConst.OptionType._03);
        return list.isEmpty() ? null : list.get(0);
    }
    /**
     * 新增或保存流程配置
     *
     * @param optionList 配置列表
     */
    @CacheEvict(value = OPTION_CACHE_NAME, allEntries = true)
    public void saveOrUpdate(List<FlowOption> optionList) {
        Date now = new Date();
        List<FlowOption> saveList = optionList.stream().peek(ele -> createdOrUpdateTime(ele, now))
                .collect(Collectors.toList());
        super.saveOrUpdateBatch(saveList);
    }
    private void createdOrUpdateTime(FlowOption flowOption,Date now) {
        if (StringUtils.isBlank(flowOption.getId())) {
            flowOption.setCreateTime(now);
        } else {
            flowOption.setUpdateTime(now);
        }
    }
    @CacheEvict(value = OPTION_CACHE_NAME, allEntries = true)
    public void delete(String id) {
        super.removeById(id);
    }
    /**
     * 2019-03-04
     * chenlw
     * 获取固定显示的字段列表（当流程没有配置显示字段时，则显示固定字段列表）
     *
     * @return 固定显示的字段列表
     */
    private List<FlowOption> getStableExtendParams() {
        return Optional.ofNullable(fixedOption).orElse(this.initFixedOption());
    }
    public List<FlowOption> getAppendStaleExtendParamsByCategoryId(String categoryId, FlowOption invoiceOption) {
        FlowCategory flowCategory = flowCategoryService.findById(categoryId);
        return getAppendStaleExtendParams(flowCategory, invoiceOption);
    }
    public List<FlowOption> getAppendStaleExtendParamsByCategoryKey(String categoryKey, FlowOption invoiceOption) {
        FlowCategory flowCategory = flowCategoryService.findByKey(categoryKey);
        return getAppendStaleExtendParams(flowCategory, invoiceOption);
    }
    /**
     * 获取定制显示字段的固定显示字段列表
     *
     * @param flowCategory 流程分类
     * @param invoiceOption 单据配置
     * @return 定制显示字段列表
     */
    public List<FlowOption> getAppendStaleExtendParams(FlowCategory flowCategory, FlowOption invoiceOption) {
        List<FlowOption> appendExtendParams = new ArrayList<>();
        if (Objects.isNull(flowCategory)) {
            return appendExtendParams;
        }
        if (invoiceOption == null) {
            invoiceOption = getInvoiceConfigByCategoryKey(flowCategory.getCateKey());
        }
        if (invoiceOption != null && !StringUtils.isEmpty(invoiceOption.getFieldName())) {
            // 有单据配置时，就使用单据配置来初始化业务单号字段，否则返回默认的固定字段列表。
            String name = StringUtils.isEmpty(invoiceOption.getName()) ? FlowOptioneConst.OptionExtend.BUSINESS_ID.getName() : invoiceOption.getName();
            appendExtendParams.add(FlowOption.createExtendOption(name, invoiceOption.getFieldName(), 0));
            appendExtendParams.addAll(getAppendStaleExtendParamsExceptInvoice());
        } else {
            appendExtendParams.addAll(getStableExtendParams());
        }
        return appendExtendParams;
    }
    /**
     * 获取固定显示字段列表（申请单位，申请人，申请时间）。  不包含业务单号的字段
     *
     * @return 固定显示字段列表（申请单位，申请人，申请时间）
     */
    public List<FlowOption> getAppendStaleExtendParamsExceptInvoice() {
        return getStableExtendParams().stream()
                .filter(ele -> !ele.getFieldName().equalsIgnoreCase(FlowOptioneConst.OptionExtend.BUSINESS_ID.getFieldName()))
                .collect(Collectors.toList());
    }

    public void saveOrUpdateVO(List<FlowOptionVO> optionList) {
        List<FlowOption> flowOptions = optionList.stream().map(FlowOption::from).collect(Collectors.toList());
        this.saveOrUpdate(flowOptions);
    }
}
