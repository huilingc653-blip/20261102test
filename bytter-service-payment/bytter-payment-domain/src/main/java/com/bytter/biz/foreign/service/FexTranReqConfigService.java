package com.bytter.biz.foreign.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.bytter.biz.foreign.dao.FexTranReqConfigDao;
import com.bytter.biz.foreign.entity.FexTranConfigOperationLog;
import com.bytter.biz.foreign.entity.FexTranReqConfig;
import com.bytter.biz.foreign.entity.FexTranReqConfigVersion;
import com.bytter.biz.foreign.entity.FexTranTemplateConfig;
import com.bytter.biz.foreign.vo.FexCustFieldRuleVO;
import com.bytter.biz.foreign.vo.FexTemplateConfigVo;
import com.bytter.biz.foreign.vo.FexTranReqConfigImportVo;
import com.bytter.biz.foreign.vo.FexTranReqConfigVo;
import com.bytter.biz.foreign.vo.FexTranReqFieldVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.IDictService;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IUserExcelVerifyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("fexTranReqConfigService")
public class FexTranReqConfigService extends BaseService<FexTranReqConfig, String>  implements IUserExcelVerifyHandler<FexTranReqConfigImportVo> {

    @Autowired
    private FexTranReqConfigVersionService fexTranReqConfigVersionService;

    @Autowired
    private FexTranReqFieldService fexTranReqFieldService;

    @Autowired
    private FexCustFieldRuleService fexCustFieldRuleService;

    @Autowired
    private FexTranReqConfigDao fexTranReqConfigDao;

    @Autowired
    private IDictService dictService;

    @Autowired
    private FexTranConfigTemplateService fexTranConfigTemplateService;


    @Autowired
    private FexTranConfigOperationLogService fexTranConfigOperationLogService;

    @Override
    public BaseDao<FexTranReqConfig, String> getEntityDao() {
        return fexTranReqConfigDao;
    }


    private static final String YES = "是";
    private static final String NO = "否";

    /**
     *
     * 获取模板配置
     *
     * @param templateId 模板ID
     * @param custFieldIds 前端信息自定义字段
     * @return
     */
    public List<FexTranReqConfigVo> getTemplateConfig(String templateId, String versionNo, String custFieldIds){
        Set<String> custFieldIdList = new HashSet<>();
        // 配置数据
        List<FexTranReqConfigVo> templateConfig = fexTranReqConfigDao.getTemplateConfig(templateId, versionNo);
        if (!CollectionUtil.isEmpty(templateConfig)){
            Map<String, List<FexTranReqConfigVo>> areaMap = templateConfig.stream()
                    .collect(Collectors.groupingBy(
                            vo -> vo.getFieldArea() == null ? "" : vo.getFieldArea()
                    ));
            if (StringUtils.isEmpty(custFieldIds)){
                List<FexTranReqConfigVo> otherInfo = areaMap.get("otherInfo");
                if (CollectionUtil.isNotEmpty(otherInfo)){
                    List<String> fieldIds = otherInfo.stream().map(FexTranReqConfigVo::getFieldId).collect(Collectors.toList());
                    custFieldIdList.addAll(fieldIds);
                }
            }
        }
        if (StringUtils.isNotEmpty(custFieldIds)){
            custFieldIdList.addAll(Arrays.asList(custFieldIds.split(",")));
        }
        List<String> custFieldIdListNew = new ArrayList<>(custFieldIdList);

        // 配置节点
        List<FexTranReqConfigVo> defTemplateConfig = getDefFexTranReqConfigs(templateId, custFieldIdListNew);

        // 给节点赋值配置数据
        if (!CollectionUtil.isEmpty(templateConfig)){
            Map<String, FexTranReqConfigVo> templateConfigMap = templateConfig.stream()
                    .collect(Collectors.toMap(
                            FexTranReqConfigVo::getFieldCode,
                            v -> v,
                            (existing, replacement) -> existing // 重复时保留第一个，不抛异常
                    ));

            for (FexTranReqConfigVo defFexTranReqConfigVo : defTemplateConfig){
                FexTranReqConfigVo fexTranReqConfigVo = templateConfigMap.get(defFexTranReqConfigVo.getFieldCode());
                if (null != fexTranReqConfigVo){
                    BeanUtil.copyProperties(fexTranReqConfigVo, defFexTranReqConfigVo);
                }
            }

            defTemplateConfig.sort(Comparator.comparingInt(FexTranReqConfigVo::getFieldSort));
        }
        return buildTree(defTemplateConfig);
    }

    public List<FexTranReqConfigVo> getTemplateConfig(String templateId, String versionNo){
        FexTranTemplateConfig fexTranTemplateConfig = fexTranConfigTemplateService.get(templateId);
        if (StringUtils.isNotEmpty(fexTranTemplateConfig.getParentId())){
            templateId = fexTranTemplateConfig.getParentId();
        }
        List<FexTranReqConfigVo> templateConfig = fexTranReqConfigDao.getTemplateConfig(templateId, versionNo);
        return templateConfig;
    }

    /**
     *
     * 获取模板配置(区域树)
     *
     * @param templateId
     * @return
     */
    public List<FexTranReqConfigVo> getTemplateConfigByAereTree(String templateId, String versionNo){
        // 取父模板的配置
        FexTranTemplateConfig fexTranTemplateConfig = fexTranConfigTemplateService.get(templateId);
        if (StringUtils.isNotEmpty(fexTranTemplateConfig.getParentId())){
            templateId = fexTranTemplateConfig.getParentId();
        }
        List<FexTranReqConfigVo> templateConfig = fexTranReqConfigDao.getTemplateConfig(templateId, versionNo);
        if (CollectionUtil.isEmpty(templateConfig)){
            templateConfig = getDefFexTranReqConfigs(templateId, null);
        }
        List<FexTranReqConfigVo> fexTranReqConfigTree = buildTree(templateConfig);
        Map<String, List<FexTranReqConfigVo>> fexTranReqConfigMap = fexTranReqConfigTree.stream().collect(Collectors.groupingBy(FexTranReqConfigVo::getFieldArea));
        List<FexTranReqConfigVo> areaTree = new ArrayList<>();
        List<DictDTO> areaList = dictService.list("fex.templateField.dataArea");
        for (DictDTO area : areaList){
            // 主体单位和交易对手信息属于基本信息
            if("corpInfo".equals(area.getCode()) || "cotpInfo".equals(area.getCode())){
                continue;
            }
            FexTranReqConfigVo fexTranReqConfigVo = new FexTranReqConfigVo();
            fexTranReqConfigVo.setFieldCode(area.getCode());
            fexTranReqConfigVo.setFieldName(area.getName());
            List<FexTranReqConfigVo> children = fexTranReqConfigMap.get(area.getCode());
            fexTranReqConfigVo.setChildren(children);
            fexTranReqConfigVo.setDataType("area");
            areaTree.add(fexTranReqConfigVo);
        }
        return areaTree;
    }

    /**
     *
     *
     * 获取默认配置
     *
     * @return
     */
    public List<FexTranReqConfigVo> getDefFexTranReqConfigs(String templateId, List<String> custFieldIds){
        List<FexTranReqConfigVo> templateConfig = new ArrayList<>();
        List<FexTranReqFieldVo> allValidFields = fexTranReqFieldService.getAllValidFields();
        for (FexTranReqFieldVo fexTranReqFieldVo : allValidFields){
            FexTranReqConfigVo fexTranReqConfigVo = new FexTranReqConfigVo();
            fexTranReqConfigVo.setTemplateId(templateId);
            fexTranReqConfigVo.setFieldSort(fexTranReqFieldVo.getFieldSort());
            fexTranReqConfigVo.setFieldId(fexTranReqFieldVo.getId());
            fexTranReqConfigVo.setFieldCode(fexTranReqFieldVo.getFieldCode());
            fexTranReqConfigVo.setFieldName(fexTranReqFieldVo.getFieldName());
            fexTranReqConfigVo.setIsSys(Integer.valueOf(CommonConstant.YES));
            fexTranReqConfigVo.setIsUse(Integer.valueOf(CommonConstant.YES));
            fexTranReqConfigVo.setIsRequired(Integer.valueOf(CommonConstant.NO));
            fexTranReqConfigVo.setIsVisible(Integer.valueOf(CommonConstant.NO));
            fexTranReqConfigVo.setIsLength(Integer.valueOf(CommonConstant.NO));
            fexTranReqConfigVo.setIsLimitedDict(Integer.valueOf(CommonConstant.NO));
            fexTranReqConfigVo.setDataType(fexTranReqFieldVo.getDataType());
            fexTranReqConfigVo.setDataClass(fexTranReqFieldVo.getDataClass());
            fexTranReqConfigVo.setFieldDictGroup(fexTranReqFieldVo.getDataGroup());
            fexTranReqConfigVo.setFieldArea(fexTranReqFieldVo.getFieldArea());
            fexTranReqConfigVo.setFieldParentCode(fexTranReqFieldVo.getFieldParentCode());
            if ("hidden".equalsIgnoreCase(fexTranReqFieldVo.getDataType())){
                continue;
            }
            templateConfig.add(fexTranReqConfigVo);
        }

        if (!CollectionUtil.isEmpty(custFieldIds)){
            List<FexTranReqConfigVo> custFields = getCustFields(custFieldIds, templateId, templateConfig.get(templateConfig.size()-1).getFieldSort());
            templateConfig.addAll(custFields);
        }
        return templateConfig;
    }

    /**
     *
     * 获取自定义字段
     *
     * @param custFieldIds
     * @param templateId
     * @param lastFieldSort
     * @return
     */
    private List<FexTranReqConfigVo> getCustFields(List<String> custFieldIds, String templateId, int lastFieldSort){
        List<FexTranReqConfigVo> custFexTranReqConfig = new ArrayList<>();
        List<FexCustFieldRuleVO> fexTranCustInfoVOS = fexCustFieldRuleService.getByIds(custFieldIds);
        if (!CollectionUtil.isEmpty(fexTranCustInfoVOS)) {
            Set<String> custFieldIdSet = new HashSet<>(custFieldIds);
            for (FexCustFieldRuleVO fexCustFieldRuleVO : fexTranCustInfoVOS){
                if (custFieldIdSet.add(fexCustFieldRuleVO.getId())){
                    continue;
                }
                lastFieldSort ++;
                FexTranReqConfigVo fexTranReqConfigVo = new FexTranReqConfigVo();
                fexTranReqConfigVo.setFieldArea("otherInfo");
                fexTranReqConfigVo.setTemplateId(templateId);
                fexTranReqConfigVo.setFieldSort(lastFieldSort);
                fexTranReqConfigVo.setFieldId(fexCustFieldRuleVO.getId());
                fexTranReqConfigVo.setFieldCode(fexCustFieldRuleVO.getFieldCode());
                fexTranReqConfigVo.setFieldName(fexCustFieldRuleVO.getFieldName());
                fexTranReqConfigVo.setIsSys(Integer.valueOf(CommonConstant.NO));
                fexTranReqConfigVo.setIsUse(Integer.valueOf(CommonConstant.YES));
                fexTranReqConfigVo.setIsRequired(Integer.valueOf(CommonConstant.YES));
                fexTranReqConfigVo.setIsVisible(Integer.valueOf(CommonConstant.YES));
                fexTranReqConfigVo.setIsLength(Integer.valueOf(CommonConstant.NO));
                if("1".equals(fexCustFieldRuleVO.getFieldType())){
                    fexTranReqConfigVo.setDataType("text");
                } else if("2".equals(fexCustFieldRuleVO.getFieldType())){
                    fexTranReqConfigVo.setDataType("number");
                    fexTranReqConfigVo.setDataClass("bt-biz-moneyType");
                } else if("3".equals(fexCustFieldRuleVO.getFieldType())){
                    fexTranReqConfigVo.setDataType("day");
                    fexTranReqConfigVo.setDataClass("bt-biz-dateType");
                } else if("4".equals(fexCustFieldRuleVO.getFieldType())){
                    fexTranReqConfigVo.setDataType("time");
                    fexTranReqConfigVo.setDataClass("bt-biz-dateType");
                } else if("5".equals(fexCustFieldRuleVO.getFieldType())){
                    fexTranReqConfigVo.setDataType("daytime");
                    fexTranReqConfigVo.setDataClass("bt-biz-dateType");
                } else if ("6".equals(fexCustFieldRuleVO.getFieldType())){
                    fexTranReqConfigVo.setDataType("select");
                    fexTranReqConfigVo.setDataClass("bt-biz-dict");
                    fexTranReqConfigVo.setFieldDictGroup(fexCustFieldRuleVO.getDictGroup());
                }
                custFexTranReqConfig.add(fexTranReqConfigVo);
            }
        }

        return custFexTranReqConfig;
    }

    public void saveTemplateConfig(FexTemplateConfigVo fexTemplateConfigVo, ICurrentUser user) {
        // 上个版本配置
        List<FexTranReqConfigVo> oldTemplateConfig = null;
        Map<String, List<FexTranReqConfigVo>>       fexTemplateConfigVoMap=new HashMap<>();
        String versionNo = fexTranReqConfigVersionService.getMaxVersionNo(fexTemplateConfigVo.getTemplateId());
        if (StringUtils.isNotEmpty(versionNo)){
            oldTemplateConfig = fexTranReqConfigDao.getTemplateConfig(fexTemplateConfigVo.getTemplateId(), versionNo);

          fexTemplateConfigVoMap=  oldTemplateConfig.stream().collect(Collectors.groupingBy(FexTranReqConfigVo::getFieldId));
        }
        // 如果是已有版本，新增版本号存配置，否则就用新的版本号
        if(StringUtils.isEmpty(versionNo) && StringUtils.isNotEmpty(fexTemplateConfigVo.getVersionNo())){
            versionNo = fexTemplateConfigVo.getVersionNo();
        } else if (StringUtils.isEmpty(fexTemplateConfigVo.getVersionNo()) || fexTemplateConfigVo.getVersionNo().equals(versionNo)){
            versionNo = SequenceSingleton.getSequence(SequenceEnum.FEX_TEMPLATE_CONFIG_VERSION_NO);
        }
        // 删除旧配置
        fexTranReqConfigDao.delByTemplateId(fexTemplateConfigVo.getTemplateId(), versionNo);
        fexTranReqConfigVersionService.delByTemplateId(fexTemplateConfigVo.getTemplateId(), versionNo);
        // 保存版本信息
        FexTranReqConfigVersion fexTranReqConfigVersion = new FexTranReqConfigVersion();
        fexTranReqConfigVersion.setTemplateId(fexTemplateConfigVo.getTemplateId());
        fexTranReqConfigVersion.setVersionNo(versionNo);
        fexTranReqConfigVersion.setCreateBy(user.getId());
        fexTranReqConfigVersion.setCreateDate(new Date());
        fexTranReqConfigVersionService.saveEntity(fexTranReqConfigVersion);
        // 保存配置明细
        List<FexTranReqConfig> fexTranReqConfigs = BeanUtil.copyToList(fexTemplateConfigVo.getTemplateConfigs(), FexTranReqConfig.class);

        for (FexTranReqConfig fexTranReqConfig : fexTranReqConfigs) {
            fexTranReqConfig.setTemplateConfigVersionId(fexTranReqConfigVersion.getId());
            fexTranReqConfig.setVersionNo(fexTranReqConfigVersion.getVersionNo());
            if (EmptyUtils.isEmpty(fexTemplateConfigVoMap.get(fexTranReqConfig.getFieldId()))) {
                fexTranReqConfig.setIsRequired(Integer.valueOf(CommonConstant.NO));
                fexTranReqConfig.setIsVisible(Integer.valueOf(CommonConstant.YES));
            }
        }

        // 保存日志
        FexTranConfigOperationLog log = new FexTranConfigOperationLog();
        if (null != oldTemplateConfig){
            log.setBeforeData(JSONUtil.toJsonStr(oldTemplateConfig));
        }
        log.setAfterData(JSONUtil.toJsonStr(fexTemplateConfigVo));
        saveFexTranConfigOperationLog(  log,  user);
        batchSave(fexTranReqConfigs);
    }

    private void saveFexTranConfigOperationLog( FexTranConfigOperationLog log, ICurrentUser user){
        log.setOperationContent("交易模板配置编辑");
        log.setCreateBy(user.getLoginName());
        log.setCreateName(user.getLoginName());
        log.setCreateDate(new Date());
        log.setIsDelete(CommonConstant.NO);
        log.setOperationPage(CommonConstant.NO);
        fexTranConfigOperationLogService.save(log);
    }


    /**
     * 复制模板配置
     * @param oldId
     * @param newId
     */
    public void copyTemplateConfig(String oldId,String newId, ICurrentUser user){
        String versionNo = fexTranReqConfigVersionService.getMaxVersionNo(oldId);
        if (StringUtils.isNotEmpty(versionNo)) {
            FexTranReqConfigVersion configVersion = fexTranReqConfigVersionService.getByTemplateId(oldId, versionNo);
            List<FexTranReqConfigVo> oldTemplateConfig = fexTranReqConfigDao.getTemplateConfig(oldId, versionNo);
            if (!EmptyUtils.isEmpty(oldTemplateConfig)) {
                FexTemplateConfigVo fexTemplateConfigVo = new FexTemplateConfigVo();
                versionNo = SequenceSingleton.getSequence(SequenceEnum.FEX_TEMPLATE_CONFIG_VERSION_NO);
                configVersion.setId(null);
                configVersion.setTemplateId(newId);
                configVersion.setVersionNo(versionNo);
                configVersion.setCreateBy(user.getId());
                configVersion.setCreateDate(new Date());
                configVersion.setUpdateBy(null);
                configVersion.setUpdateDate(null);
                fexTranReqConfigVersionService.saveEntity(configVersion);
                oldTemplateConfig.forEach(fexTranReqConfig -> {
                    fexTranReqConfig.setId(null);
                    fexTranReqConfig.setTemplateId(newId);
                    fexTranReqConfig.setTemplateConfigVersionId(configVersion.getId());
                    fexTranReqConfig.setVersionNo(configVersion.getVersionNo());
                    fexTranReqConfig.setCreateBy(user.getId());
                    fexTranReqConfig.setCreateDate(new Date());
                    fexTranReqConfig.setUpdateBy(null);
                    fexTranReqConfig.setUpdateDate(null);
                });
                List<FexTranReqConfig> data=  BeanUtil.copyToList(oldTemplateConfig, FexTranReqConfig.class);
                batchSave(data);
                // 保存日志
                fexTemplateConfigVo.setVersionNo(versionNo);
                fexTemplateConfigVo.setTemplateId(newId);
                fexTemplateConfigVo.setTemplateConfigs(oldTemplateConfig);
                FexTranConfigOperationLog log = new FexTranConfigOperationLog();
                log.setAfterData(JSONUtil.toJsonStr(fexTemplateConfigVo));
                saveFexTranConfigOperationLog(log, user);
            }
        }
    }


    /**
     * 批量保存模板自定义字段
     *
     * @param templateIds 模板ID，多个用逗号分隔
     * @param custFieldIds 自定义字段ID，多个用逗号分隔
     * @param user 当前用户
     */
    public void batchSaveTemplateConfig(String templateIds, String custFieldIds, ICurrentUser user) {
        // 1. 空值判断
        if (StringUtils.isBlank(templateIds) || StringUtils.isBlank(custFieldIds)) {
            return;
        }

        // 字符串转数组
        String[] templateIdArr = templateIds.split(",");
        List<String> custFieldIdList = Arrays.asList(custFieldIds.split(","));

        for (String templateId : templateIdArr) {
            FexTranTemplateConfig fexTranTemplateConfig = fexTranConfigTemplateService.get(templateId);
            String realTemplateId = templateId;
            // 生成新版本号
            String newVersionNo = SequenceSingleton.getSequence(SequenceEnum.FEX_TEMPLATE_CONFIG_VERSION_NO);
            if (StringUtils.isNotEmpty(fexTranTemplateConfig.getParentId())){
                realTemplateId = fexTranTemplateConfig.getParentId();
            }
            // 获取当前最大版本号
            String maxVersionNo = fexTranReqConfigVersionService.getMaxVersionNo(realTemplateId);
            List<FexTranReqConfigVo> newConfigList;

            if (StringUtils.isEmpty(maxVersionNo)) {
                // 无历史版本 → 获取默认配置
                newConfigList = getDefFexTranReqConfigs(realTemplateId, custFieldIdList);
            } else {
                // 有历史版本 → 基于最新版本生成新版本
                List<FexTranReqConfigVo> oldConfigList = fexTranReqConfigDao.getTemplateConfig(realTemplateId, maxVersionNo);
                // 空集合保护
                if (CollectionUtil.isEmpty(oldConfigList)) {
                    oldConfigList = new ArrayList<>();
                }

                // 获取最后一个排序值（安全判断）
                int lastSort = 0;
                if (!oldConfigList.isEmpty()) {
                    lastSort = oldConfigList.get(oldConfigList.size() - 1).getFieldSort();
                }

                // 获取本次选中的自定义字段
                List<FexTranReqConfigVo> custFields = getCustFields(custFieldIdList, realTemplateId, lastSort);
                // 旧字段编码转map（用于去重）
                Map<String, FexTranReqConfigVo> oldFieldMap = oldConfigList.stream()
                        .collect(Collectors.toMap(
                                FexTranReqConfigVo::getFieldCode,
                                Function.identity(),
                                (existing, replacement) -> existing
                        ));

                // 只保留选中的字段 + 去重
                newConfigList = oldConfigList;
                for (FexTranReqConfigVo custField : custFields) {
                    String fieldCode = custField.getFieldCode();
                    // // 不存在 → 新增
                    if (!oldFieldMap.containsKey(fieldCode)) {
                        newConfigList.add(custField);
                    }
                }
            }

            for (FexTranReqConfigVo fexTranReqConfigVo : newConfigList){
                fexTranReqConfigVo.setTemplateId(templateId);
            }

            // 组装并保存新版本
            FexTemplateConfigVo fexTemplateConfigVo = new FexTemplateConfigVo();
            fexTemplateConfigVo.setTemplateId(templateId);
            fexTemplateConfigVo.setVersionNo(newVersionNo);
            fexTemplateConfigVo.setTemplateConfigs(newConfigList);
            saveTemplateConfig(fexTemplateConfigVo, user);
        }
    }


    public String getMaxVersionNo(String templateId){
        FexTranTemplateConfig fexTranTemplateConfig = fexTranConfigTemplateService.get(templateId);
        if (StringUtils.isNotEmpty(fexTranTemplateConfig.getParentId())){
            templateId = fexTranTemplateConfig.getParentId();
        }
        return fexTranReqConfigVersionService.getMaxVersionNo(templateId);
    }


    /**
     * 构建树形结构（根据 fieldParentCode -> fieldParentCode 匹配）
     */
    private List<FexTranReqConfigVo> buildTree(List<FexTranReqConfigVo> list) {
        if (CollectionUtil.isEmpty(list)) {
            return new ArrayList<>();
        }

        // 1. 全局按 fieldSort 排序（所有节点）
        list.sort(Comparator.comparingInt(FexTranReqConfigVo::getFieldSort));

        // 2. 【正确】按【父区域字段】分组：key = fieldParentArea
        Map<String, List<FexTranReqConfigVo>> parentMap = list.stream()
                .collect(Collectors.groupingBy(
                        vo -> vo.getFieldParentCode() == null ? "" : vo.getFieldParentCode()
                ));

        // 3. 顶级节点：fieldParentArea 为 null / 空
        List<FexTranReqConfigVo> rootList = list.stream()
                .filter(vo -> vo.getFieldParentCode() == null || vo.getFieldParentCode().trim().isEmpty())
                .collect(Collectors.toList());

        // 4. 循环设置子节点（非递归，避免栈溢出）
        for (FexTranReqConfigVo root : rootList) {
            setChildren(root, parentMap);
        }

        // 整棵树 从上到下、父子连续 重新排序号（1、2、3、4… 连续往下）
        AtomicInteger sortNum = new AtomicInteger(1);
        resetTreeSort(rootList, sortNum);
        return rootList;
    }

    /**
     * 设置子节点（修复：正确关联 fieldParentCode）
     */
    private void setChildren(FexTranReqConfigVo parent, Map<String, List<FexTranReqConfigVo>> parentMap) {
        String parentFieldCode = parent.getFieldCode();

        // 获取子节点
        List<FexTranReqConfigVo> children = parentMap.get(parentFieldCode);

        if (CollectionUtil.isNotEmpty(children)) {
            parent.setChildren(children);

            // 子节点继续找自己的子节点
            for (FexTranReqConfigVo child : children) {
                setChildren(child, parentMap);
            }
        }
    }

    /**
     * 递归重置排序号（父 → 子 → 孙 连续往下）
     */
    private void resetTreeSort(List<FexTranReqConfigVo> nodes, AtomicInteger sortNum) {
        if (CollectionUtil.isEmpty(nodes)) {
            return;
        }

        for (FexTranReqConfigVo node : nodes) {
            // 先设置当前节点的排序号
            node.setFieldSort(sortNum.getAndIncrement());

            // 递归设置子节点（子节点紧接着排下去）
            resetTreeSort(node.getChildren(), sortNum);
        }
    }

    /**
     *
     * 导入
     *
     * @param list
     * @param currentUser
     */
    public void saveImport(List<FexTemplateConfigVo> list, ICurrentUser currentUser) {

    }


    @Override
    public ExcelVerifyHanlderResult verifyHandler(FexTranReqConfigImportVo fexTranReqConfigVo, Object... objects) {
        return null;
    }


    @Override
    public void verifyHandler(List<FexTranReqConfigImportVo> list, Object... objects) {
        List<FexTranReqFieldVo> allValidFields = fexTranReqFieldService.getAllValidFields();
        Map<String, FexTranReqFieldVo> fieldMap = allValidFields.stream()
                .collect(Collectors.toMap(
                        FexTranReqFieldVo::getFieldName,
                        vo -> vo,
                        (oldVal, newVal) -> oldVal // key冲突保留旧值
                ));
        if (!EmptyUtils.isEmptyList(list)) {
            for (FexTranReqConfigImportVo fexTranReqConfigVo : list){
                FexTranReqFieldVo fexTranReqFieldVo = fieldMap.get(fexTranReqConfigVo.getFieldName());
                if (null == fexTranReqFieldVo){
                    fexTranReqConfigVo.setErrorMsg("无此字段");
                }
                if (!YES.equals(fexTranReqConfigVo.getIsVisible())){
                    fexTranReqConfigVo.setIsVisible(NO);
                }
                if (!YES.equals(fexTranReqConfigVo.getIsRequired())){
                    fexTranReqConfigVo.setIsRequired(NO);
                }
                if (!YES.equals(fexTranReqConfigVo.getIsLength())){
                    fexTranReqConfigVo.setIsLength(NO);
                }
                if (!YES.equals(fexTranReqConfigVo.getIsLimitedDict())){
                    fexTranReqConfigVo.setIsLimitedDict(NO);
                }
            }
        }
    }


    public List<FexTranReqConfigVo> listByFieldId(String fieldId) {
        return fexTranReqConfigDao.listByFieldId(fieldId);
    }

    public void updatefieldName(String fieldId, String fieldName) {
        fexTranReqConfigDao.updatefieldName(fieldId, fieldName);
    }

    public List<FexTranReqConfigVo> listByReferenced(String fieldId) {
        return fexTranReqConfigDao.listByReferenced(fieldId);
    }

    /**
     * 获取模板必填字段
     * @param templateId
     * @param versionNo
     * @return
     */
    public List<FexTranReqConfigVo> getRequiredFields(String templateId, String versionNo) {
        FexTranTemplateConfig fexTranTemplateConfig = fexTranConfigTemplateService.get(templateId);
        if (StringUtils.isNotEmpty(fexTranTemplateConfig.getParentId())){
            templateId = fexTranTemplateConfig.getParentId();
        }
        List<FexTranReqConfigVo> templateConfig = fexTranReqConfigDao.getTemplateConfig(templateId, versionNo);
        List<FexTranReqConfigVo> requiredList = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(templateConfig)){
            // 过滤 isRequired = 1 的数据
            requiredList = templateConfig.stream()
                    .filter(vo -> vo.getIsRequired() != null && vo.getIsRequired() == 1)
                    .collect(Collectors.toList());
        }
        return requiredList;
    }
}
