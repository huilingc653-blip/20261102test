package com.bytter.supply.domain.payable.service;


import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.supply.domain.payable.dao.ScfPayConfigDao;
import com.bytter.supply.domain.payable.dao.ScfPayFieldConfigDao;
import com.bytter.supply.domain.payable.dao.ScfPayTemplateConfigDao;
import com.bytter.supply.domain.payable.entity.ScfPayTemplateConfig;
import com.bytter.supply.domain.payable.dto.ScfPayTemplateConfigDTO;
import com.bytter.supply.domain.payable.entity.ScfPayConfig;
import com.bytter.supply.domain.payable.entity.ScfPayFieldConfig;
import com.bytter.supply.domain.payable.vo.ScfPayConfigVO;
import com.bytter.supply.enums.SupplyMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service("scfPayConfigService")
@Transactional(rollbackFor = Exception.class)
public class ScfPayConfigService extends BaseService<ScfPayConfig, String> {

    @Autowired
    private ScfPayConfigDao scfPayConfigDao;

    @Autowired
    private ScfPayFieldConfigDao scfPayFieldConfigDao;

    @Autowired
    private ScfPayTemplateConfigDao scfPayTemplateConfigDao;

    @Override
    public BaseDao<ScfPayConfig, String> getEntityDao() {
        return scfPayConfigDao;
    }

    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        return scfPayConfigDao.getDataTablesUnion(dataTablesInput);
    }

    public void saveScfPayConfig(ScfPayConfigVO vo) {
        ScfPayConfig entity = new ScfPayConfig();
        BeanUtils.copyNotNullProperties(vo, entity);
        scfPayConfigDao.save(entity);
    }

    public void modify(ScfPayConfigVO vo) {
        ScfPayConfig entity = new ScfPayConfig();
        BeanUtils.copyNotNullProperties(vo, entity);
        scfPayConfigDao.update(entity);
    }

    public void deleteColumn(String id) {
        ScfPayConfig entity = this.get(id);
        this.deleteEntity(entity);
    }

    public String getMaxVersionNo(String templateId) {
        return scfPayConfigDao.getMaxVersionNo(templateId);
    }

    /**
     * 查询模板字段配置
     * 逻辑：先查SCF_PAY_CONFIG是否已有该模板的配置，有则返回；无则从SCF_PAY_FIELD_CONFIG（预设字段表）读取默认配置
     *
     * @param templateId 模板ID
     * @return 字段配置列表
     */
    public List<ScfPayConfigVO> getPayTemplateFieldConfig(String templateId) {
        // 1. 查询该模板是否已有配置
        List<ScfPayConfigVO> configList = scfPayConfigDao.getTemplateConfig(templateId);

        // 2. 如果没有配置则直接返回报错信息 该供应链金融平台未配置应付债权凭证页面模版，请联系管理端配置相应模版
        if (EmptyUtils.isEmpty(configList)) {
            throw new BusinessException(SupplyMessageEnum.SCF_NOT_CONFIG_PLATFORM);
        }

        return configList;
    }

    /**
     * 保存模板字段配置
     * 逻辑：先删旧配置，再批量保存新配置
     *
     * @param dto  前端传来的配置数据
     * @param userId 当前用户ID
     */
    public void saveTemplateConfig(ScfPayTemplateConfigDTO dto, String userId) {
        Date now = new Date();
        String templateId = dto.getTemplateId();

        // 1. 删除该模板的旧配置
        scfPayConfigDao.deleteByTemplateId(templateId);

        // 2. 批量保存新配置
        List<ScfPayConfigVO> configVOs = dto.getTemplateConfigs();
        if (EmptyUtils.isNotEmpty(configVOs)) {
            List<ScfPayConfig> configList = new ArrayList<>();
            for (ScfPayConfigVO vo : configVOs) {
                ScfPayConfig entity = new ScfPayConfig();
                BeanUtils.copyNotNullProperties(vo, entity);
                entity.setTemplateId(templateId);
                entity.setCreateBy(userId);
                entity.setCreateDate(now);
                entity.setUpdateBy(userId);
                entity.setUpdateDate(now);
                entity.setIsDeleted(Integer.valueOf(CommonConstant.NO));
                configList.add(entity);
            }
            batchSave(configList);
        }
    }

    /**
     * 根据平台ID查询模板字段配置
     * 逻辑：只取平台关联的且模板版本有效的配置，无效（平台未配置/模板已禁用/配置为空）则返回默认模板
     *
     * @param platformId 平台ID
     * @return 字段配置列表
     */
    public List<ScfPayConfigVO> getPayTemplateFieldConfigByPlatformId(String platformId) {
        ScfPayTemplateConfig templateConfig = scfPayTemplateConfigDao.getValidBaseInfoByPlatformId(platformId);
        if (templateConfig != null && StringUtils.isNotEmpty(templateConfig.getVersionId())) {
            List<ScfPayConfigVO> configList = scfPayConfigDao.getTemplateConfig(templateConfig.getVersionId());
            if(EmptyUtils.isNotEmpty(configList)){
                return configList;
            }
        }
        return getDefaultPayTemplateFieldConfig();
    }

    public List<ScfPayConfigVO> getDefaultPayTemplateFieldConfig() {
        List<ScfPayFieldConfig> fieldConfigList = scfPayFieldConfigDao.listBy("validSign", CommonConstant.VALID_SIGN_YES);
        List<ScfPayConfigVO> result = new ArrayList<>();
        if (EmptyUtils.isNotEmpty(fieldConfigList)) {
            for (ScfPayFieldConfig field : fieldConfigList) {
                ScfPayConfigVO vo = new ScfPayConfigVO();
                vo.setTemplateId("default");
                vo.setFieldId(field.getId());
                vo.setFieldCode(field.getFieldCode());
                vo.setFieldName(field.getFieldName());
                vo.setFieldType(field.getFieldType());
                vo.setFieldSort(field.getFieldSort());
                vo.setFieldArea(field.getFieldArea());
                vo.setFieldDefVal(field.getFieldDefVal());
                vo.setDataType(field.getDataType());
                vo.setFieldDictGroup(field.getDataGroup());
                vo.setIsVisible(field.getIsVisible() != null ? field.getIsVisible() : Integer.valueOf(CommonConstant.NO));
                vo.setIsRequired(field.getIsRequired() != null ? field.getIsRequired() : Integer.valueOf(CommonConstant.NO));
                vo.setIsSys(Integer.valueOf(CommonConstant.YES)); // 预设字段
                vo.setIsLimitLength(field.getIsLimitLength() != null ? field.getIsLimitLength() : Integer.valueOf(CommonConstant.NO));
                vo.setIsLimitDict(Integer.valueOf(CommonConstant.NO));
                vo.setFieldLength(field.getFieldLength());
                result.add(vo);
            }
            // 按 fieldSort 排序
            result.sort(Comparator.comparingInt(v -> v.getFieldSort() != null ? v.getFieldSort() : Integer.MAX_VALUE));
        }
        return result;
    }
}
