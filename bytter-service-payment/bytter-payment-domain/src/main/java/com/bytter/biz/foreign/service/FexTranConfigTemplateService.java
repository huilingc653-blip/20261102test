package com.bytter.biz.foreign.service;

import com.bytter.biz.bdlinks.dao.BisForeignExcDao;
import com.bytter.biz.foreign.dao.FexTranAppDao;
import com.bytter.biz.foreign.dao.FexTranConfigTemplateDao;
import com.bytter.biz.foreign.dao.FexTranReqDao;
import com.bytter.biz.foreign.entity.FexTranTemplateConfig;
import com.bytter.biz.foreign.enums.FexTranConfigMessageEnum;
import com.bytter.biz.foreign.vo.FexTranTemplateConfigDetailVO;
import com.bytter.biz.foreign.vo.FexTranTemplateConfigParamVO;
import com.bytter.biz.foreign.vo.FexTranTemplateConfigVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.base.entity.IdEntity;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.system.api.dto.BisBifInitDTO;
import com.bytter.system.api.dto.DictDTO;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.IBisBifInitService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.ISysCountryCustService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ZZF
 * @version 1.0
 * @Title: Dao
 * @Desc: 交易模板配置
 * @Date: 2025/11/20$ 9:24$
 */
@Service("fexTranConfigTemplateService")
@Transactional
public class FexTranConfigTemplateService extends BaseService<FexTranTemplateConfig, String> {

    @Autowired
    private FexTranConfigTemplateDao fexTranConfigTemplateDao;
    @Autowired
    private IDictService dictService;

    @Autowired
    private FexTranReqConfigService fexTranReqConfigService;

    @Autowired(required = false)
    private IBtBankAccService btBankAccService;

    @Autowired(required = false)
    private IBisBifInitService bisBifInitService;

    @Autowired(required = false)
    private IBankTypeService iBankTypeService;

    @Autowired(required = false)
    private ISysCountryCustService sysCountryCustService;

    @Autowired(required = false)
    private FexTranAppDao fexTranAppDao;

    @Autowired(required = false)
    private FexTranReqDao fexTranReqDao;

    @Autowired(required = false)
    private  BisForeignExcDao bisForeignExcDao;

   public static final String childTemplateNameFormat="%s（%s-%s-%s-%s（%s））";
    @Override
    public BaseDao<FexTranTemplateConfig, String> getEntityDao() {
        return fexTranConfigTemplateDao;
    }

    /**
     * 列表查询
     *
     * @param vo
     * @return
     */
    public List<FexTranTemplateConfigVO> list(FexTranTemplateConfigParamVO vo) {
        List<FexTranTemplateConfigVO> parents = fexTranConfigTemplateDao.listOfParent(vo);
        //查询所有配置
        List<FexTranTemplateConfigDetailVO> childern = fexTranConfigTemplateDao.list(vo);
        //查询模板数据
        Map<String, List<FexTranTemplateConfigDetailVO>> childerMap = childern.stream().collect(Collectors.groupingBy(FexTranTemplateConfigDetailVO::getParentId));
        Map<String, String> dictMap = dictService.list("valid_sign").stream()
                .collect(Collectors.toMap(DictDTO::getCode, DictDTO::getName));
        for (FexTranTemplateConfigVO e : parents) {
            if (EmptyUtils.isNotEmpty(e.getValidSign())) {
                e.setValidSignName(dictMap.get(e.getValidSign()));
            }
            List<FexTranTemplateConfigDetailVO> list = childerMap.get(e.getId());
            if (!EmptyUtils.isEmpty(list)) {
                list.forEach(info -> {
                    info.setIsSplit(list.size()>1);
                    info.setValidSignParent(e.getValidSign());
                    info.setTemplateName(null);
                    if (EmptyUtils.isEmpty(info.getValidSign())) {
                        info.setValidSignName(dictMap.get(e.getValidSign()));
                    }
                });
                e.setBankTypeName(list.stream().map(FexTranTemplateConfigDetailVO::getBankTypeName).filter(EmptyUtils::isNotEmpty).distinct().collect(Collectors.joining("、")));
                e.setBifName(list.stream().map(FexTranTemplateConfigDetailVO::getBifName).filter(EmptyUtils::isNotEmpty).distinct().collect(Collectors.joining("、")));
                e.setCountryZhName(list.stream().map(FexTranTemplateConfigDetailVO::getCountryZhName).filter(EmptyUtils::isNotEmpty).distinct().collect(Collectors.joining("、")));
                e.setPayTypeName(list.stream().map(FexTranTemplateConfigDetailVO::getPayTypeName).filter(EmptyUtils::isNotEmpty).distinct().collect(Collectors.joining("、")));
                e.setChildren(list);
            }
        }
        return parents;
    }

    /**
     * 删除
     *
     * @param id
     * @param userId
     */
    public void delete(String id, String userId) {
        FexTranTemplateConfig p = fexTranConfigTemplateDao.get(id);
        if (p == null) {
            throw new BusinessException(FexTranConfigMessageEnum.DATA_NOT_EXIST);
        }
        validateUnFinishedBill(id);
        if (EmptyUtils.isEmpty(p.getParentId())) {
            FexTranTemplateConfig vo = new FexTranTemplateConfig();
            vo.setParentId(p.getId());
            vo.setIsDelete(CommonConstant.NO);
            List<FexTranTemplateConfig> data = fexTranConfigTemplateDao.listByCondition(vo);
            if (EmptyUtils.isNotEmpty(data)) {
                throw new BusinessException(FexTranConfigMessageEnum.FEX_CONFIG_TEMPLATE_HAS_DETAIL_DATA);
            }
        }
        p.setUpdateBy(userId);
        p.setIsDelete(CommonConstant.YES);
        fexTranConfigTemplateDao.saveOrUpdate(p);
    }

    /**
     * 生效
     *
     * @param id
     */
    public void valid(String id, String flag, String userId) {
        FexTranTemplateConfig p = fexTranConfigTemplateDao.get(id);
        if (p == null) {
            throw new BusinessException(FexTranConfigMessageEnum.DATA_NOT_EXIST);
        }
        validateUnFinishedBill(id);
        p.setValidSign(flag);
        p.setUpdateBy(userId);
        fexTranConfigTemplateDao.saveOrUpdate(p);
        if (EmptyUtils.isEmpty(p.getParentId())) {
            FexTranTemplateConfig vo = new FexTranTemplateConfig();
            vo.setParentId(p.getId());
            vo.setIsDelete(CommonConstant.NO);
            List<FexTranTemplateConfig> data = fexTranConfigTemplateDao.listByCondition(vo);
            data.forEach(info -> {
                info.setValidSign(flag);
                info.setUpdateBy(userId);
                fexTranConfigTemplateDao.saveOrUpdate(info);
            });
        }
    }


    /**
     * 拆分
     *
     * @param id
     */
    public void split(String id, ICurrentUser user) {
        String userId=user.getId();
        Date now = new Date();
        String templateNo = SequenceSingleton.getSequence(SequenceEnum.FEX_TRAN_TEMPLATE_CONFIG_NO);
        FexTranTemplateConfig rowChild = fexTranConfigTemplateDao.get(id);
        if (rowChild == null) {
            throw new BusinessException(FexTranConfigMessageEnum.DATA_NOT_EXIST);
        }
        validateUnFinishedBill(id);
        if (EmptyUtils.isEmpty(rowChild.getParentId())) {
            return;
        }
        if (CommonConstant.YES.equals(rowChild.getValidSign())) {
            FexTranTemplateConfig rowParent = fexTranConfigTemplateDao.get(rowChild.getParentId());
            FexTranTemplateConfig parent = FexTranTemplateConfig.builder()
                    .templateNo(templateNo)
                    .templateName(rowParent.getTemplateName())
                    .validSign(rowParent.getValidSign())
                    .createBy(userId)
                    .createDate(now)
                    .isDelete(CommonConstant.NO).build();
            fexTranConfigTemplateDao.save(parent);
            FexTranTemplateConfig child = new FexTranTemplateConfig();
            BeanUtils.copyProperties(rowChild, child);
            child.setCreateBy(userId);
            child.setCreateDate(now);
            child.setParentId(parent.getId());
            child.setId(null);
            fexTranConfigTemplateDao.save(child);
            //删除原数据
            rowChild.setIsDelete(CommonConstant.YES);
            fexTranConfigTemplateDao.saveOrUpdate(rowChild);
            fexTranReqConfigService.copyTemplateConfig(rowParent.getId(),parent.getId(),user);
        }
    }

    /**
     * 查看详情
     *
     * @param id
     * @return
     */
    public FexTranTemplateConfigVO view(String id) {
        FexTranTemplateConfigVO result = new FexTranTemplateConfigVO();
        FexTranTemplateConfig p = fexTranConfigTemplateDao.get(id);
        if (p == null) {
            throw new BusinessException(FexTranConfigMessageEnum.DATA_NOT_EXIST);
        }
        Map<String, String> dictMap = dictService.list("valid_sign").stream()
                .collect(Collectors.toMap(DictDTO::getCode, DictDTO::getName));
        BeanUtils.copyProperties(p, result);
        if (EmptyUtils.isNotEmpty(result.getValidSign())) {
            result.setValidSignName(dictMap.get(result.getValidSign()));
        }
        FexTranTemplateConfigParamVO vo = new FexTranTemplateConfigParamVO();
        vo.setParentId(p.getId());
        List<FexTranTemplateConfigDetailVO> details = fexTranConfigTemplateDao.list(vo);
        result.setChildren(details);
        return result;
    }

    /**
     * 新增
     *
     * @param param
     */
    public void save(FexTranTemplateConfigVO param, String userId) {
        Date now = new Date();
        //校验一级模板，模板编号相同不能添加
        List<FexTranTemplateConfig> rawDatas = fexTranConfigTemplateDao.listByCondition(FexTranTemplateConfig.builder().templateNo(param.getTemplateNo()).build());
        if (!EmptyUtils.isEmpty(rawDatas)) {
            throw new ParameterException(FexTranConfigMessageEnum.FEX_CONFIG_TEMPLATE_NO_EXISTED);
        }
        validateDuplicateData(param.getChildren(), true);
        FexTranTemplateConfig p = FexTranTemplateConfig.builder()
                .templateNo(param.getTemplateNo())
                .templateName(param.getTemplateName())
                .templateDesc(param.getTemplateDesc())
                .validSign(param.getValidSign())
                .remark(param.getRemark())
                .isDelete(CommonConstant.NO)
                .createBy(userId)
                .createDate(now)
                .build();
        fexTranConfigTemplateDao.save(p);
        List<FexTranTemplateConfigDetailVO> childrenParam = param.getChildren();
        if (!EmptyUtils.isEmpty(childrenParam)) {

            List<FexTranTemplateConfig> children = new ArrayList<>();
            childrenParam.forEach(e -> {
                String str =     String.format(childTemplateNameFormat, p.getTemplateName(),e.getBankTypeName(),e.getBifName(),e.getCountryZhName(),e.getPayTypeName(),e.getPayTypeCode(),e.getPayTypeName());
                FexTranTemplateConfig child = FexTranTemplateConfig.builder()
                        .templateName(str)
                        .parentId(p.getId())
                        .bankTypeId(e.getBankTypeId())
                        .bifId(e.getBifId())
                        .countryId(e.getCountryId())
                        .payTypeId(e.getPayTypeId())
                        .validSign(param.getValidSign())
                        .sort(e.getSort()).isDelete(CommonConstant.NO).createBy(userId).createDate(now)
                        .build();
                children.add(child);
            });
            fexTranConfigTemplateDao.batchSave(children);
        }
    }

    /**
     * 修改配置模板
     *
     * @param param
     * @param userId
     */
    public void modify(FexTranTemplateConfigVO param, String userId) {
        Date now = new Date();
        //校验一级模板，模板编号相同不能添加
        List<FexTranTemplateConfig> rawDatas = fexTranConfigTemplateDao.listByCondition(FexTranTemplateConfig.builder().templateNo(param.getTemplateNo()).build());
        if (!rawDatas.isEmpty() && !param.getId().equals(rawDatas.get(0).getId())) {
            throw new ParameterException(FexTranConfigMessageEnum.FEX_CONFIG_TEMPLATE_NO_EXISTED);
        }
        validateDuplicateData(param.getChildren(), false);
        validateUnFinishedBill(param.getId());
        FexTranTemplateConfig parent = fexTranConfigTemplateDao.get(param.getId(),true);
        if (!EmptyUtils.isEmpty(param.getChildren())) {
            validateDuplicateData(param.getChildren(), false);
            List<String> rawDChildrenId = fexTranConfigTemplateDao.listByCondition(FexTranTemplateConfig.builder().parentId(param.getId()).build()).stream().map(IdEntity::getId).collect(Collectors.toList());
            List<FexTranTemplateConfigDetailVO> adds = param.getChildren().stream().filter(e -> EmptyUtils.isEmpty(e.getId())).collect(Collectors.toList());
            List<FexTranTemplateConfigDetailVO> updates = param.getChildren().stream().filter(e -> EmptyUtils.isNotEmpty(e.getId())).collect(Collectors.toList());

            Set<String> updateIds = updates.stream()
                    .map(FexTranTemplateConfigDetailVO::getId)
                    .collect(Collectors.toSet());

            List<String> deleteIds = rawDChildrenId.stream()
                    .filter(id -> !updateIds.contains(id))
                    .collect(Collectors.toList());

            List<FexTranTemplateConfig> childrenAdd = new ArrayList<>();
            adds.forEach(e -> {
                String str =    String.format(childTemplateNameFormat,param.getTemplateName(), e.getBankTypeName(),e.getBifName() ,e.getCountryZhName() , e.getPayTypeCode(),e.getPayTypeName());
                FexTranTemplateConfig child = FexTranTemplateConfig.builder()
                        .templateName(str)
                        .parentId(param.getId())
                        .bankTypeId(e.getBankTypeId())
                        .bifId(e.getBifId())
                        .countryId(e.getCountryId())
                        .payTypeId(e.getPayTypeId())
                        .validSign(param.getValidSign())
                        .sort(e.getSort())
                        .isDelete(CommonConstant.NO)
                        .createBy(userId)
                        .createDate(now)
                        .build();
                childrenAdd.add(child);
            });
            fexTranConfigTemplateDao.batchSave(childrenAdd);
            if (EmptyUtils.isNotEmpty(deleteIds)) {
                fexTranConfigTemplateDao.deleteByIds(deleteIds, userId);
            }
            List<FexTranTemplateConfig> childrenUpdate = new ArrayList<>();
            updates.forEach(e -> {
                String str =String.format(childTemplateNameFormat,param.getTemplateName(), e.getBankTypeName(),e.getBifName(),e.getCountryZhName(),e.getPayTypeCode(),e.getPayTypeName() );
                        FexTranTemplateConfig child = new FexTranTemplateConfig();
                child.setTemplateName(str);
                child.setParentId(param.getId());
                child.setBankTypeId(e.getBankTypeId());
                child.setBifId(e.getBifId());
                child.setCountryId(e.getCountryId());
                child.setPayTypeId(e.getPayTypeId());
                child.setValidSign(param.getValidSign());
                child.setSort(e.getSort());
                child.setCreateBy(userId);
                child.setCreateDate(now);
                child.setIsDelete(CommonConstant.NO);
                child.setId(e.getId());
                childrenUpdate.add(child);
            });
            if (EmptyUtils.isNotEmpty(childrenUpdate)) {
                fexTranConfigTemplateDao.batchUpdate(childrenUpdate);
            }
        }
        parent.setTemplateNo(param.getTemplateNo());
        parent.setTemplateName(param.getTemplateName());
        parent.setTemplateDesc(param.getTemplateDesc());
        parent.setValidSign(param.getValidSign());
        parent.setRemark(param.getRemark());
        parent.setUpdateBy(userId);
        fexTranConfigTemplateDao.update(parent);
    }

    /**
     * 判断配置数据无重复
     *
     * @param param
     */
    private Boolean validateDuplicateData(List<FexTranTemplateConfigDetailVO> param, Boolean isSave) {
        if (EmptyUtils.isNotEmpty(param)) {
            //空配置数据校验
            if (param.stream().anyMatch(e -> StringUtils.isEmpty(e.getBankTypeId()) || StringUtils.isEmpty(e.getBifId()) || StringUtils.isEmpty(e.getCountryId()) || StringUtils.isEmpty(e.getPayTypeId()))) {
                throw new ParameterException(FexTranConfigMessageEnum.NOT_CONFIG_NULL_DATA);
            }

            //重复数据校验
            Map<String, List<FexTranTemplateConfigDetailVO>> map = param.stream().collect(Collectors.groupingBy(e -> e.getBankTypeId() + "|" + e.getBifId() + "|" + e.getCountryId() + "|" + e.getPayTypeId()));
            for (String id : map.keySet()) {
                if (map.get(id).size() > 1) {
                    throw new ParameterException(FexTranConfigMessageEnum.NOT_CONFIG_DUPLICATE_DATA);
                }
            }

            List<FexTranTemplateConfigDetailVO> list = fexTranConfigTemplateDao.batchCheckDuplicate(param);
            List<String> errors = new ArrayList<>();
            if (EmptyUtils.isNotEmpty(list)) {
                if (isSave) {
                    throw new BusinessException(FexTranConfigMessageEnum.FEX_CONFIG_TEMPLATE_HAS_DETAIL_DATA_CONFIGS, list.stream().map(e -> e.getTemplateNo() + "-" + e.getTemplateName()).distinct().collect(Collectors.joining("、")));
                } else {
                    //修改数据校验，排序本条数据
                    Map<String, String> paramMap = param.stream().filter(e -> StringUtils.isNotEmpty(e.getId())).collect(Collectors.toMap(FexTranTemplateConfigDetailVO::getId, e -> e.getTemplateNo() + "-" + e.getTemplateName()));
                    for (FexTranTemplateConfigDetailVO detailVO : list) {
                        if (!paramMap.containsKey(detailVO.getId())) {
                            errors.add(detailVO.getTemplateNo() + "-" + detailVO.getTemplateName());
                        }
                    }
                    if (!errors.isEmpty()) {
                        throw new BusinessException(FexTranConfigMessageEnum.FEX_CONFIG_TEMPLATE_HAS_DETAIL_DATA_CONFIGS, errors.stream().distinct().collect(Collectors.joining("、")));
                    }
                }
            }
        } else {
            throw new BusinessException(FexTranConfigMessageEnum.NOT_CONFIG_NULL_DATA);
        }
        return true;
    }

    /**
     * 查询二级数据配置
     *
     * @param vo
     * @return
     */
    public DataTablesOutput<FexTranTemplateConfigDetailVO> configTemplateChildrenList(DataTablesInput vo) {
        //查询所有配置
        DataTablesOutput<FexTranTemplateConfigDetailVO> childern = fexTranConfigTemplateDao.page(vo);
        childern.getData().forEach(info -> {
            // 最新版本号
            String maxVersionNo = fexTranReqConfigService.getMaxVersionNo(info.getParentId());
            info.setVersionNo(maxVersionNo);
        });
        return childern;
    }

    /**
     * 模板配置分页
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput<FexTranTemplateConfigVO> listOfParentPage(DataTablesInput dataTablesInput) {
        DataTablesOutput<FexTranTemplateConfigVO> datas = fexTranConfigTemplateDao.listOfParentPage(dataTablesInput);
        if (!EmptyUtils.isEmpty(datas) && (!EmptyUtils.isEmpty(datas.getData()))) {
            FexTranTemplateConfigParamVO param = new FexTranTemplateConfigParamVO();
            param.setValidSign(CommonConstant.YES);
            param.setParentIds(datas.getData().stream().map(FexTranTemplateConfigVO::getId).collect(Collectors.toList()));
            List<FexTranTemplateConfigDetailVO> childern = fexTranConfigTemplateDao.list(param);
            if (!EmptyUtils.isEmpty(childern)) {
                //查询模板数据
                Map<String, List<FexTranTemplateConfigDetailVO>> childerMap = childern.stream().collect(Collectors.groupingBy(FexTranTemplateConfigDetailVO::getParentId));
                Map<String, String> dictMap = dictService.list("valid_sign").stream()
                        .collect(Collectors.toMap(DictDTO::getCode, DictDTO::getName));
                for (FexTranTemplateConfigVO e : datas.getData()) {
                    if (EmptyUtils.isNotEmpty(e.getValidSign())) {
                        e.setValidSignName(dictMap.get(e.getValidSign()));
                    }
                    List<FexTranTemplateConfigDetailVO> list = childerMap.get(e.getId());
                    if (!EmptyUtils.isEmpty(list)) {
                        list.forEach(info -> {
                            if (EmptyUtils.isEmpty(info.getValidSign())) {
                                info.setValidSignName(dictMap.get(e.getValidSign()));
                            }
                        });
                        e.setBankTypeName(list.stream().map(FexTranTemplateConfigDetailVO::getBankTypeName).filter(EmptyUtils::isNotEmpty).distinct().collect(Collectors.joining("、")));
                        e.setBifName(list.stream().map(FexTranTemplateConfigDetailVO::getBifName).filter(EmptyUtils::isNotEmpty).distinct().collect(Collectors.joining("、")));
                        e.setCountryZhName(list.stream().map(FexTranTemplateConfigDetailVO::getCountryZhName).filter(EmptyUtils::isNotEmpty).distinct().collect(Collectors.joining("、")));
                        e.setPayTypeName(list.stream().map(FexTranTemplateConfigDetailVO::getPayTypeName).filter(EmptyUtils::isNotEmpty).distinct().collect(Collectors.joining("、")));
                        e.setChildren(list);
                    }
                }
            }

        }
        return datas;
    }

    /**
     * 根据四要素查询模版
     *
     * @param fexTranTemplateConfigParam
     * @return
     */
    public FexTranTemplateConfigVO getTemplateConfig(FexTranTemplateConfigVO fexTranTemplateConfigParam) {
        if (StringUtils.isEmpty(fexTranTemplateConfigParam.getBankTypeId())
                || StringUtils.isEmpty(fexTranTemplateConfigParam.getCountryId())
                || StringUtils.isEmpty(fexTranTemplateConfigParam.getBifId())
                || StringUtils.isEmpty(fexTranTemplateConfigParam.getPayTypeId())) {
            return null;
        }
        FexTranTemplateConfigVO templateConfig = fexTranConfigTemplateDao.getTemplateConfig(fexTranTemplateConfigParam);
        if (null != templateConfig) {
            String versionNo = fexTranReqConfigService.getMaxVersionNo(templateConfig.getId());
            templateConfig.setVersionNo(versionNo);
            if(EmptyUtils.isNotEmpty(templateConfig.getBankTypeName())&&
                    EmptyUtils.isNotEmpty(templateConfig.getBifName())&&
                    EmptyUtils.isNotEmpty(templateConfig.getCountryZhName())&&
                    EmptyUtils.isNotEmpty(templateConfig.getBankPayType())&&
                    EmptyUtils.isNotEmpty(templateConfig.getPayTypeName())
            )
            templateConfig.setTemplateName( String.format(childTemplateNameFormat,templateConfig.getTemplateName(),templateConfig.getBankTypeName(),templateConfig.getBifName(),templateConfig.getCountryZhName(),templateConfig.getBankPayType(),templateConfig.getPayTypeName()));
        }
        return templateConfig;
    }


    /**
     * 组装模板参数
     *
     * @param bankAcc
     * @return
     */
    public FexTranTemplateConfigVO getTemplateByBankAcc(String bankAcc, String payTypeId) {
        if (EmptyUtils.isEmpty(bankAcc) || EmptyUtils.isEmpty(payTypeId)) {
            return null;
        }
        FexTranTemplateConfigVO param = new FexTranTemplateConfigVO();
        // 支付类型
        param.setPayTypeId(payTypeId);
        // 获取银行账户信息
        BtBankAccDTO bankAccDTO = btBankAccService.getBankAccInfo(bankAcc);
        if (bankAccDTO == null) {
            return null;
        }
        param.setBankTypeId(bankAccDTO.getBankTypeId());
        // 获取bifId
        BisBifInitDTO bifInit = bisBifInitService.getObjectById(bankAccDTO.getBifCode());
        if (bifInit != null) {
            param.setBifId(bifInit.getId());
        }
        // 获取countryId
        param.setCountryId(bankAccDTO.getCountryId());
        return getTemplateConfig(param);
    }

    /**
     * 组装模板参数
     *
     * @param bankAcc
     * @param isChangeTemplate
     * @return
     */
    public FexTranTemplateConfigVO getTemplateConfigParam(String bankAcc, Boolean isChangeTemplate) {
        FexTranTemplateConfigVO param = new FexTranTemplateConfigVO();
        if (!Boolean.TRUE.equals(isChangeTemplate)) {
            return param;
        }

        // 获取银行账户信息
        BtBankAccDTO bankAccDTO = btBankAccService.getBankAccInfo(bankAcc);
        param.setBankTypeId(bankAccDTO.getBankTypeId());

        // 获取bifId
        BisBifInitDTO bifInit = bisBifInitService.getByBifCode(bankAccDTO.getBifCode());
        if (bifInit != null) {
            param.setBifId(bifInit.getId());
        }

        // 获取countryId
        param.setCountryId(bankAccDTO.getCountryId());
        return param;
    }

    public FexTranTemplateConfigVO getTemplateConfigById(String id) {
        return fexTranConfigTemplateDao.getTemplateConfigById(id);
    }

    /**
     * 申请单保存用：优先按账户+支付类型匹配当前子模版并拼最新名称；
     * 匹配不到再按 templateId 回退。
     */
    public FexTranTemplateConfigVO resolveLatestTemplateForSave(String templateId, String bankAcc, String payTypeId) {
        if (EmptyUtils.isNotEmpty(bankAcc) && EmptyUtils.isNotEmpty(payTypeId)) {
            FexTranTemplateConfigVO matched = getTemplateByBankAcc(bankAcc, payTypeId);
            if (matched != null && EmptyUtils.isNotEmpty(matched.getTemplateName())) {
                return matched;
            }
        }
        if (EmptyUtils.isNotEmpty(templateId)) {
            FexTranTemplateConfigVO byId = getTemplateConfigById(templateId);
            if (byId != null) {
                byId.setTemplateName(formatChildTemplateName(byId));
                return byId;
            }
            FexTranTemplateConfig entity = this.get(templateId);
            if (entity != null) {
                FexTranTemplateConfigVO vo = new FexTranTemplateConfigVO();
                vo.setId(entity.getId());
                vo.setTemplateName(entity.getTemplateName());
                return vo;
            }
        }
        return null;
    }

    private String formatChildTemplateName(FexTranTemplateConfigVO info) {
        if (info == null) {
            return null;
        }
        String parentName = EmptyUtils.isNotEmpty(info.getTemplateNameParent())
                ? info.getTemplateNameParent() : info.getTemplateName();
        if (EmptyUtils.isNotEmpty(info.getBankTypeName())
                && EmptyUtils.isNotEmpty(info.getBifName())
                && EmptyUtils.isNotEmpty(info.getCountryZhName())
                && EmptyUtils.isNotEmpty(info.getBankPayType())
                && EmptyUtils.isNotEmpty(info.getPayTypeName())) {
            return String.format(childTemplateNameFormat, parentName,
                    info.getBankTypeName(), info.getBifName(), info.getCountryZhName(),
                    info.getBankPayType(), info.getPayTypeName());
        }
        return parentName;
    }


    /**
     * 校验模板还有相关交易单（需求申请单，交易单）
     *
     * @param templateId
     */
    public void validateUnFinishedBill(String templateId) {
        List<String> templateIds = Arrays.asList(templateId.split(","));
        List<FexTranTemplateConfigVO> tranTemplateConfigVOS = fexTranConfigTemplateDao.selectByIds(templateIds);
        if (EmptyUtils.isEmpty(tranTemplateConfigVOS)) {
            throw new BusinessException(FexTranConfigMessageEnum.DATA_NOT_EXIST);
        }
        List<String> billNos = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        for (FexTranTemplateConfigVO fexTranTemplateConfig:tranTemplateConfigVOS) {
            if (EmptyUtils.isEmpty(fexTranTemplateConfig.getParentId())) {
                FexTranTemplateConfig param = new FexTranTemplateConfig();
                param.setParentId(templateId);
                param.setIsDelete(CommonConstant.NO);
                List<FexTranTemplateConfig> configs = fexTranConfigTemplateDao.listByCondition(param);
                if (!EmptyUtils.isEmpty(configs)) {
                    ids.addAll(configs.stream().map(FexTranTemplateConfig::getId).collect(Collectors.toList()));
                }
            }
            ids.add(templateId);
            //交易单
            billNos.addAll(fexTranReqDao.getUnFinishedBillNos(ids));
            //需求单
            billNos.addAll(fexTranAppDao.getUnFinishedBillNos(ids));
            //指令状态  等待结果、银行处理失败、准备发送、待发送、接口处理失败、待审批、审批打回、审批通过、已打印支票、指令已提取、指令发送队列成功、指令发送队列失败，等待下次重发、指令已消费、可疑状态、异常交易、验签不通过
            billNos.addAll(bisForeignExcDao.getUnFinishedBillNos(ids));
        }
        if (!EmptyUtils.isEmpty(billNos)) {
            String msg = billNos.stream().distinct().collect(Collectors.joining("、"));
            throw new BusinessException(FexTranConfigMessageEnum.HAS_UNFINISHED_BILL, msg);
        }



    }


    /**
     * 查询二级模板数据
     *
     * @param vo
     * @return
     */
    public List<FexTranTemplateConfigDetailVO> listOfChildren(FexTranTemplateConfigParamVO vo) {
        return fexTranConfigTemplateDao.listOfChildren(vo);
    }


}
