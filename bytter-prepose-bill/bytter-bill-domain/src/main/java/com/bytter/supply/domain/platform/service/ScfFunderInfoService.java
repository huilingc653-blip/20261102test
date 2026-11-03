package com.bytter.supply.domain.platform.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.supply.domain.platform.dao.ScfFunderInfoDao;
import com.bytter.supply.domain.platform.entity.ScfFunderInfo;
import com.bytter.supply.domain.platform.vo.ScfFunderInfoVo;
import com.bytter.supply.enums.SupplyMessageEnum;
import com.bytter.system.api.IDictService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Title: ScfFunderInfoService
 * @Description: 资金方信息
 * @version 1.0
 */
@Service("scfFunderInfoService")
@Transactional
public class ScfFunderInfoService extends BaseService<ScfFunderInfo, String> {

    @Autowired
    private ScfFunderInfoDao scfFunderInfoDao;

    @DubboReference(check = false)
    private IDictService dictService;


    @Override
    public void deleteEntity(ScfFunderInfo entity) {
        super.deleteEntity(entity);
    }



    /**
     * 保存
     */
    public void save(ScfFunderInfo entity) {
        // 校验
        verify(entity);
        // 自动生成编码 XJF + 3位流水号
        if (EmptyUtils.isEmpty(entity.getCode())) {
            entity.setCode(generateCode());
        }
        // 校验编码唯一性
        if (isCodeExist(entity.getCode(), null)) {
            throw new ParameterException(SupplyMessageEnum.SCF_FUND_PROVIDER_CODE_EXIST);
        }
        super.saveEntity(entity);
    }

    /**
     * 更新
     */
    public void update(ScfFunderInfo entity) {
        // 校验
        verify(entity);
        ScfFunderInfo dbEntity = get(entity.getId());
        BeanUtils.copyNotNullProperties(entity, dbEntity);
        // 校验编码唯一性（排除自身）
        if (isCodeExist(dbEntity.getCode(), dbEntity.getId())) {
            throw new ParameterException(SupplyMessageEnum.SCF_FUND_PROVIDER_CODE_EXIST);
        }
        super.updateEntity(dbEntity);
    }




    /**
     * 列表查询
     */
    @SuppressWarnings("rawtypes")
    @Transactional(readOnly = true)
    public DataTablesOutput<ScfFunderInfoVo> listScfFunderInfo(DataTablesInput dataTablesInput) {
        return scfFunderInfoDao.listScfFunderInfo(dataTablesInput);
    }

    /**
     * 生成资金方编码 XJF + 3位流水号
     */
    @Transactional(readOnly = true)
    public String generateCode() {
        String maxCode = scfFunderInfoDao.getMaxCode();
        int nextNum = 1;
        if (maxCode != null && maxCode.length() >= 6) {
            String numStr = maxCode.substring(3);
            try {
                nextNum = Integer.parseInt(numStr) + 1;
            } catch (NumberFormatException e) {
                nextNum = 1;
            }
        }
        return String.format("XJF%03d", nextNum);
    }

    /**
     * 根据编码查询资金方是否已存在
     * @param code 资金方编码
     * @param excludeId 排除的ID（修改时传入当前ID）
     * @return true-已存在 false-不存在
     */
    @Transactional(readOnly = true)
    public boolean isCodeExist(String code, String excludeId) {
        List<ScfFunderInfo> list = scfFunderInfoDao.getFunderInfoByCode(code, excludeId);
        return list != null && !list.isEmpty();
    }

    /**
     * 查询资金方列表（有效）
     */
    @Transactional(readOnly = true)
    public List<ScfFunderInfoVo> getFunderJson() {
        List<ScfFunderInfoVo> list = scfFunderInfoDao.getFunderJson();
        if (list != null) {
            for (ScfFunderInfoVo vo : list) {
                vo.setTypeName(dictService.get("scf.fund.institution.type", vo.getType()));
            }
        }
        return list;
    }

    /**
     * 验证
     */
    public void verify(ScfFunderInfo entity) {
        if (EmptyUtils.isEmpty(entity.getType())) {
            throw new ParameterException(SupplyMessageEnum.SCF_FUND_PROVIDER_ORG_TYPE_NULL);
        }
        if (EmptyUtils.isEmpty(entity.getName())) {
            throw new ParameterException(SupplyMessageEnum.SCF_FUND_PROVIDER_NAME_NULL);
        }
        // 当机构类型为银行时，银行行号必录
        if ("2".equals(entity.getType()) && EmptyUtils.isEmpty(entity.getBankNo())) {
            throw new ParameterException(SupplyMessageEnum.SCF_FUND_PROVIDER_BANK_CODE_NULL);
        }
    }

    @Override
    public BaseDao<ScfFunderInfo, String> getEntityDao() {
        return scfFunderInfoDao;
    }
}
