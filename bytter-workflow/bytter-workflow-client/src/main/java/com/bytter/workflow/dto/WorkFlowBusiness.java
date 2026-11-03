package com.bytter.workflow.dto;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.bpm.persistence.WorkFlowPersistence;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.jqgrid.TableGridInput;
import com.bytter.core.common.jqgrid.TableGridResponse;
import com.bytter.core.dict.DictInDTO;
import com.bytter.core.dict.DictInjectProvider;
import com.bytter.core.dict.DictUtil;
import com.bytter.core.utils.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
/**
 * 工作流业务类
 *
 * @author zhoul
 * @since 2017/8/1.
 */
@Slf4j
public class WorkFlowBusiness implements Serializable {

    private transient DictInjectProvider dictInjectProvider;

    private DictInjectProvider getDictInjectProvider() {
        if (dictInjectProvider == null) {
            try {
                dictInjectProvider = ApplicationContextUtil.getBean(DictInjectProvider.class);
            } catch (Exception e) {
                log.debug("DictInjectProvider not found in Spring context");
            }
        }
        return dictInjectProvider;
    }
    /**
     * 缓存类对应的字典组，避免重复反射
     */
    private final Map<Class<?>, List<String>> dictGroupsCache = new ConcurrentHashMap<>();
    private List<WorkFlowPersistence> data;
    public WorkFlowBusiness() {
        data = new ArrayList<>();
    }
    /**
     * 获取业务ID集合
     *
     * @return 业务ID集合
     */
    public List<String> getBusinessIdList() {
        return data.stream().map(WorkFlowPersistence::getBusinessKey).collect(Collectors.toList());
    }
    /**
     * 将业务数据与工作流数据合并
     *
     * @param businessList
     *            需要合并的集合
     */
    public void merge(List<? extends IBusinessKey> businessList) {
        List<WorkFlowPersistence> temp = new ArrayList<>();
        for (IBusinessKey entity : businessList) {
            for (int i = 0; i < data.size(); i++) {
                WorkFlowPersistence workFlowPersistence = data.get(i);
                if (entity.getBusinessKey().equals(workFlowPersistence.getBusinessKey())) {
                    workFlowPersistence.setData(entity);
                    temp.add(workFlowPersistence);
                }
            }
        }
        setData(temp);
    }
    public DataTablesOutput<WorkFlowPersistence> merge(DataTablesOutput<? extends IBusinessKey> dataTablesOutput) {
        List<? extends IBusinessKey> list = dataTablesOutput.getData();
        injectDictForList(list);
        merge(list);
        return new DataTablesOutput<>(dataTablesOutput, data);
    }
    public TableGridResponse<WorkFlowPersistence> merge(TableGridInput tableGridInput, Integer records,
            List<? extends IBusinessKey> list) {
        merge(list);
        return new TableGridResponse<>(tableGridInput, records, data);
    }
    /**
     * 获取工作流中的数据
     *
     * @return 集合
     */
    public List<WorkFlowPersistence> getData() {
        return data;
    }
    public void setData(List<WorkFlowPersistence> data) {
        this.data = data;
    }
    public void add(WorkFlowPersistence workFlowPersistence) {
        this.data.add(workFlowPersistence);
    }


    private void injectDictForList(List<?> list) {
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        dictInjectProvider= getDictInjectProvider();
        // 如果没有提供者或不支持注入，直接执行原方法
        if (dictInjectProvider == null || !dictInjectProvider.isSupported()) {
            log.debug("DictInjectProvider not available or disabled, skip dict injection");
            return;
        }
        // 获取列表中元素的类型
        Class<?> clazz = list.get(0).getClass();

        // 获取需要字典注入的字段组
        List<String> dictGroups = getDictGroups(clazz);
        if (CollectionUtil.isEmpty(dictGroups)) {
            return;
        }

        try {
            // 调用提供者获取字典数据
            Map<String, Map<String, DictInDTO>> dictMap = dictInjectProvider.listDictMap(dictGroups);
            if (dictMap != null && !dictMap.isEmpty()) {
                // 注入字典值
                DictUtil.handleList(list, dictMap);
                log.debug("成功为 {} 个 {} 对象注入字典值，字典组：{}",
                        list.size(), clazz.getSimpleName(), dictGroups);
            } else {
                log.debug("未获取到字典数据，字典组：{}", dictGroups);
            }
        } catch (Exception e) {
            // 字典注入失败不影响主流程，仅记录日志
            log.error("字典注入失败，class: {}, dictGroups: {}, error: {}",
                    clazz.getSimpleName(), dictGroups, e.getMessage(), e);
        }
    }

    /**
     * 获取类中需要字典注入的字段组
     * 使用缓存提高性能
     */
    private List<String> getDictGroups(Class<?> clazz) {
        return dictGroupsCache.computeIfAbsent(clazz, DictUtil::getDictGroups);
    }

}
