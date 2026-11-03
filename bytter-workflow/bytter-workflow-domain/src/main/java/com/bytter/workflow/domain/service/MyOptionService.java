package com.bytter.workflow.domain.service;

import com.bytter.workflow.constant.OptionConstant;
import com.bytter.workflow.domain.dao.OptionDao;
import com.bytter.workflow.domain.entity.FlowCategory;
import com.bytter.workflow.domain.entity.Option;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhoul on 2017/8/3.
 *
 * @history
 * 1. 2020-01-02  chenlw  优化定制显示字段；新增initStableExtendParamsByCategoryKey()方法
 */
@Service("myOptionService")
@Transactional
public class MyOptionService extends BaseService<Option, String> {

    @Autowired
    private OptionDao optionDao;

    @Autowired
    private FlowCategoryService flowCategoryService;

    private static final String OPTION_CACHE_NAME = "optionCache";

    @Override
    public BaseDao<Option, String> getEntityDao() {
        return optionDao;
    }

    /**
     * 2019-01-15
     * chenlw
     * work：查询单据配置
     *
     * @param categoryKey 流程分类KEY
     * @return 单据配置
     */
    @Cacheable(value = OPTION_CACHE_NAME, key = "#categoryKey + '_type4'")
    public Option getInvoiceConfigByCategoryKey(String categoryKey) {
        return optionDao.getInvoiceConfigByCategoryKey(categoryKey);
    }

    /**
     * 获取定制显示的字段列表
     * 后续操作对列表进行操作，因此不用缓存，否则会导致不正确的结果。
     *
     * @param categoryKey  流程分类KEY
     * @return 定制显示的字段列表
     */
    // @Cacheable(value = OPTION_CACHE_NAME, key = "#categoryKey + '_type5'")
    public List<Option> getExtendParamsListByCategoryKey(String categoryKey) {
        return optionDao.getExtendParamsListByCategoryKey(categoryKey);
    }

    /**
     * 获取定制显示的字段列表（状态为可见的）
     * 后续操作对列表进行操作，因此不用缓存，否则会导致不正确的结果。
     *
     * @param categoryKey   流程分类KEY
     * @return 定制显示的字段列表（状态为可见的）
     */
    // @Cacheable(value = OPTION_CACHE_NAME, key = "#categoryKey + '_type5_visible1'")
    public List<Option> getVisibleExtendParamsListByCategoryKey(String categoryKey) {
        return optionDao.getVisibleExtendParamsListByCategoryKey(categoryKey);
    }

    public List<Option> listByCategoryId(String categoryId) {
        return optionDao.listByCategoryId(categoryId);
    }

    @Cacheable(value = OPTION_CACHE_NAME, key = "#categoryId + '_type1'")
    public List<Option> listConditionByCategoryId(String categoryId) {
        return optionDao.listConditionByCategoryId(categoryId);
    }

    @Cacheable(value = OPTION_CACHE_NAME, key = "#categoryId + '_type3'")
    public Option getMessageTemplateByCategory(String categoryId) {
        return optionDao.getMessageTemplateByCategory(categoryId);
    }

    /**
     * 新增或保存流程配置
     *
     * @param optionList 配置列表
     */
    @CacheEvict(value = OPTION_CACHE_NAME, allEntries = true)
    public void saveOrUpdate(List<Option> optionList) {
        List<Option> temp = new ArrayList<>();
        for (Option option : optionList) {
            if (StringUtils.isBlank(option.getId())) {
                option.setCreateTime(new Date());
            } else {
                option.setUpdateTime(new Date());
            }
            temp.add(option);
        }
        optionDao.batchSaveOrUpdate(temp);
    }

    @Override
    @CacheEvict(value = OPTION_CACHE_NAME, allEntries = true)
    public void delete(String id) {
        optionDao.delete(id);
    }

    /**
     * 2019-03-04
     * chenlw
     * 获取固定显示的字段列表（当流程没有配置显示字段时，则显示固定字段列表）
     *
     * @return 固定显示的字段列表
     */
    private List<Option> getStableExtendParams() {
        List<Option> list = new ArrayList<>();
        // 单据号
        list.add(new Option(OptionConstant.EXTEND_PARAM_BUSINESSID_KEY_NAME,
                OptionConstant.EXTEND_PARAM_BUSINESSID_KEY, OptionConstant.TYPE_EXTEND_PARAMS,
                OptionConstant.EXTEND_PARAMS_VISIBLE, 0, OptionConstant.EXTEND_PARAMS_IS_STABLE_FIELD));
        // 申请单位
        list.addAll(getExtendParamsExceptInvoice());
        return list;
    }


    public List<Option> getAppendStaleExtendParamsByCategoryId(String categoryId, Option invoiceOption) {
        FlowCategory flowCategory = flowCategoryService.getById(categoryId);
        return getAppendStaleExtendParams(flowCategory, invoiceOption);
    }

    public List<Option> getAppendStaleExtendParamsByCategoryKey(String categoryKey, Option invoiceOption) {
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
    public List<Option> getAppendStaleExtendParams(FlowCategory flowCategory, Option invoiceOption) {
        List<Option> appendExtendParams = new ArrayList<>();
        if (flowCategory != null) {
            if (invoiceOption == null) {
                invoiceOption = getInvoiceConfigByCategoryKey(flowCategory.getCateKey());
            }
            if (invoiceOption != null && !StringUtils.isEmpty(invoiceOption.getFieldName())) {
                // 有单据配置时，就使用单据配置来初始化业务单号字段，否则返回默认的固定字段列表。
                String name;
                if (StringUtils.isEmpty(invoiceOption.getName())) {
                    name = OptionConstant.EXTEND_PARAM_BUSINESSID_KEY_NAME;
                } else {
                    name = invoiceOption.getName();
                }
                appendExtendParams.add(new Option(name, invoiceOption.getFieldName(), OptionConstant.TYPE_EXTEND_PARAMS,
                        OptionConstant.EXTEND_PARAMS_VISIBLE, 0, OptionConstant.EXTEND_PARAMS_IS_STABLE_FIELD));
                appendExtendParams.addAll(getExtendParamsExceptInvoice());
            } else {
                appendExtendParams.addAll(getStableExtendParams());
            }
        }
        return appendExtendParams;
    }


    /**
     * 获取固定显示字段列表（申请单位，申请人，申请时间）。  不包含业务单号的字段
     *
     * @return 固定显示字段列表（申请单位，申请人，申请时间）
     */
    public List<Option> getAppendStaleExtendParamsExceptInvoice() {
        List<Option> appendStableExtendParams = new ArrayList<>(getExtendParamsExceptInvoice());
        return appendStableExtendParams;
    }



    private List<Option> getExtendParamsExceptInvoice() {
        List<Option> initStableExtendParams = new ArrayList<>();
        // 申请单位
        initStableExtendParams.add(new Option(OptionConstant.EXTEND_PARAM_CORP_NAME_KEY_NAME,
                OptionConstant.EXTEND_PARAM_CORP_NAME_KEY, OptionConstant.TYPE_EXTEND_PARAMS,
                OptionConstant.EXTEND_PARAMS_VISIBLE, 1, OptionConstant.EXTEND_PARAMS_IS_STABLE_FIELD));
        // 申请人
        initStableExtendParams.add(new Option(OptionConstant.EXTEND_PARAM_USERNAME_KEY_NAME,
                OptionConstant.EXTEND_PARAM_USERNAME_KEY, OptionConstant.TYPE_EXTEND_PARAMS,
                OptionConstant.EXTEND_PARAMS_VISIBLE, 2, OptionConstant.EXTEND_PARAMS_IS_STABLE_FIELD));
        // 申请时间
        initStableExtendParams.add(new Option(OptionConstant.EXTEND_PARAM_CREATE_TIME_KEY_NAME,
                OptionConstant.EXTEND_PARAM_CREATE_TIME_KEY, OptionConstant.TYPE_EXTEND_PARAMS,
                OptionConstant.EXTEND_PARAMS_VISIBLE, 3, OptionConstant.EXTEND_PARAMS_IS_STABLE_FIELD));
        return initStableExtendParams;
    }


}
