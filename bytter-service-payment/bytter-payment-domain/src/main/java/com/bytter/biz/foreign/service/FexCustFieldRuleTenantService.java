package com.bytter.biz.foreign.service;

import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.foreign.dao.FexCustFieldRuleTenantDao;
import com.bytter.biz.foreign.entity.FexCustFieldRule;
import com.bytter.biz.foreign.entity.FexCustFieldRuleTenant;
import com.bytter.biz.foreign.vo.FexCustFieldRuleTenantVO;
import com.bytter.biz.payment.api.enums.ForeignMessageEnum;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.system.api.enums.BaseDataMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 租户自定义字段规则Service。
 */
@Service("fexCustFieldRuleTenantService")
@Transactional
public class FexCustFieldRuleTenantService extends BaseService<FexCustFieldRuleTenant, String> {

    @Autowired
    private FexCustFieldRuleTenantDao fexCustFieldRuleTenantDao;

    @Override
    public BaseDao<FexCustFieldRuleTenant, String> getEntityDao() {
        return fexCustFieldRuleTenantDao;
    }


    @Autowired
    private FexCustInfoTenantService fexCustInfoTenantService;

    /**
     * 查询外汇产品
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        return fexCustFieldRuleTenantDao.getDataTable(dataTablesInput);
    }



    /**
     * 保存
     * @param fexCustFieldRule
     * @param userId
     * @return
     */
    public void save(FexCustFieldRuleTenant fexCustFieldRule, String userId){
        //检查是否重复
        checkFieldName(fexCustFieldRule);
        fexCustFieldRule.setCreateBy(userId);
        fexCustFieldRule.setCreateDate(new Date());
        fexCustFieldRule.setValidSign(FinacialConstant.VALID_TRUE);
        super.saveEntity(fexCustFieldRule);

        fexCustFieldRuleTenantDao.flush();
    }

    /**
     * 更新
     * @param fexCustFieldRule
     */
    public void update(FexCustFieldRuleTenant fexCustFieldRule, String userId){
        //判断FieldName是否重复
        checkFieldName(fexCustFieldRule);
        FexCustFieldRuleTenant entity = this.get(fexCustFieldRule.getId());
        // 页面未提交 tenantId/fieldCode/创建信息，需基于原记录合并，避免被空值覆盖
        BeanUtils.copyNotNullProperties(fexCustFieldRule, entity);
        entity.setUpdateBy(userId);
        entity.setUpdateDate(new Date());
        super.updateEntity(entity);
    }

    /**
     * 判断是否重复
     * @param fexCustFieldRule
     * @return
     */
    public void checkFieldName(FexCustFieldRuleTenant fexCustFieldRule) {
        String fieldName = fexCustFieldRule.getFieldName();
        List<FexCustFieldRule> rules = findAllBy(fexCustFieldRule.getId(),fieldName, null);
        if(!rules.isEmpty() && rules.size() > 0){
            throw new ParameterException(ForeignMessageEnum.FEX_PRODUCT_MESSAGE_NAME_UNIQUE);
        }
    }

    /**
     * 根据字段名称查询
     * @param fieldName 字段名称
     */
    @Transactional(readOnly = true)
    public List<FexCustFieldRule> findAllBy(String id,String fieldName, String fieldCode){
        return fexCustFieldRuleTenantDao.findAllBy(id, fieldName, fieldCode);
    }

    /**
     * 删除外汇产品信息
     *
     * @param id
     */
    public void deleteEntity(String id, ICurrentUser user) {
        // 引用校验
        FexCustFieldRuleTenant fexCustFieldRule = get(id);
        Boolean isOk = fexCustInfoTenantService.getCustInfoByBusFieldId(id);
        if(isOk){
            throw new ParameterException(BaseDataMessageEnum.CMS_LOAN_DATA_REFERENCED_EXIST);
        }
        this.delete(id);
    }

    public FexCustFieldRuleTenantVO getRuleById(String id){
        return fexCustFieldRuleTenantDao.getRuleById(id);
    }


    /**
     * 获取交易对手自定义字段
     *@Param cotpGroupId
     *@return
     */
    public List<FexCustFieldRuleTenantVO> listByCotp(String cotpGroupId){
        return fexCustFieldRuleTenantDao.listByCotp(cotpGroupId);
    }

    /**
     * 根据应用范围获取自定义规则
     * @param appRange1
     * @param appRange2
     * @return
     */
    public List<FexCustFieldRuleTenantVO> listByAppRange(String appRange1, String appRange2){
        return fexCustFieldRuleTenantDao.listByAppRange(appRange1, appRange2);
    }


    public List<FexCustFieldRuleTenantVO> getByIds(List<String> custFieldIds) {
        return fexCustFieldRuleTenantDao.getByIds(custFieldIds);
    }

    public List<FexCustFieldRuleTenantVO> getByFieCodes(List<String> fexCustFieCodeList) {
        return fexCustFieldRuleTenantDao.getByFieCodes(fexCustFieCodeList);
    }

    public List<FexCustFieldRuleTenantVO> listCustInfoByBusId(String id) {
        return fexCustFieldRuleTenantDao.listCustInfoByBusId(id);
    }
}
