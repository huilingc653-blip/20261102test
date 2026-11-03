package com.bytter.biz.foreign.service;

import cn.hutool.core.collection.CollectionUtil;
import com.bytter.biz.foreign.dao.FexCotpCustInfoDao;
import com.bytter.biz.foreign.dao.FexCustFieldRuleDao;
import com.bytter.biz.foreign.dao.FexTranConfigTemplateDao;
import com.bytter.biz.foreign.entity.FexCustFieldRule;
import com.bytter.biz.foreign.vo.FexCustFieldRuleVO;
import com.bytter.biz.foreign.vo.FexTranReqConfigVo;
import com.bytter.biz.foreign.vo.FexTranReqFieldVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.biz.payment.api.enums.ForeignMessageEnum;
import com.bytter.system.api.enums.BaseDataMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Title: Service
 * @Description: 自定义字段维护Service
 * @author zhengxn
 * @date 2020-02-17 14:20:33
 * @version 1.0
 *
 */
@Service("fexCustFieldRuleService")
@Transactional
public class FexCustFieldRuleService extends BaseService<FexCustFieldRule, String> {
    @Autowired
    private FexCustFieldRuleDao fexCustFieldRuleDao;
    @Autowired
    private FexCotpCustInfoDao fexCotpCustInfoDao;
    @Autowired
    private FexTranReqFieldService fexTranReqFieldService;

    @Autowired
    private FexTranReqConfigService fexTranReqConfigService;
    @Autowired
    private FexTranConfigTemplateDao fexTranConfigTemplateDao;

    @Override
    public BaseDao<FexCustFieldRule, String> getEntityDao() {
        return fexCustFieldRuleDao;
    }

    /**
     * 查询外汇产品
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        return fexCustFieldRuleDao.getDataTable(dataTablesInput);
    }



    /**
     * 保存
     * @param fexCustFieldRule
     * @param userId
     * @return
     */	
    public void save(FexCustFieldRule fexCustFieldRule, String userId){
        //检查是否重复
        checkFieldName(fexCustFieldRule);
        fexCustFieldRule.setCreateBy(userId);
        fexCustFieldRule.setCreateDate(new Date());
        fexCustFieldRule.setValidSign(FinacialConstant.VALID_TRUE);
        super.saveEntity(fexCustFieldRule);
        
        fexCustFieldRuleDao.flush();
    }

    /**
     * 更新
     * @param fexCustFieldRule
     */
    public void update(FexCustFieldRule fexCustFieldRule, String userId){
        //判断FieldName是否重复
        checkFieldName(fexCustFieldRule);
        fexCustFieldRule.setUpdateBy(userId);
        fexCustFieldRule.setUpdateDate(new Date());

        if (ForeignConstant.TRAN_TYPE_4.equals(fexCustFieldRule.getAppRange2())) {
            // 修改为无效需要校验引用
            if (CommonConstant.NO.equals(fexCustFieldRule.getValidSign())){
                List<FexTranReqConfigVo> fexTranReqConfigVos = fexTranReqConfigService.listByReferenced(fexCustFieldRule.getId());
                if (CollectionUtil.isNotEmpty(fexTranReqConfigVos)){

                    List<String> templateNameList = fexTranReqConfigVos.stream().map(e->e.getTemplateNo()+"-"+e.getTemplateName()).collect(Collectors.toList());
                    String templateNames = String.join(",", templateNameList);
                    throw new ParameterException(ForeignMessageEnum.FEX_CUST_FIELD_REFERENCED_EXIST, templateNames);
                }
            }
            // 修改引用字段名称
            List<FexTranReqConfigVo> fexTranReqConfigVos = fexTranReqConfigService.listByFieldId(fexCustFieldRule.getId());
            if (CollectionUtil.isNotEmpty(fexTranReqConfigVos)){
                FexTranReqConfigVo fexTranReqConfigVo = fexTranReqConfigVos.get(0);
                if (!fexCustFieldRule.getFieldName().equals(fexTranReqConfigVo.getFieldName())){
                    fexTranReqConfigService.updatefieldName(fexCustFieldRule.getId(), fexCustFieldRule.getFieldName());
                }
            }
        }

        super.updateEntity(fexCustFieldRule);
    }

    /**
     * 判断是否重复
     * @param fexCustFieldRule
     * @return
     */
    public void checkFieldName(FexCustFieldRule fexCustFieldRule) {
        String fieldName = fexCustFieldRule.getFieldName();
        List<FexCustFieldRule> rules = findAllBy(fexCustFieldRule.getId(),fieldName, null);
        if(!rules.isEmpty() && rules.size() > 0){
            throw new ParameterException(ForeignMessageEnum.FEX_PRODUCT_MESSAGE_NAME_UNIQUE);
        }
        if (ForeignConstant.TRAN_TYPE_4.equals(fexCustFieldRule.getAppRange2())) {
            String fieldCode = fexCustFieldRule.getFieldCode();
            String labelBank = fexCustFieldRule.getLabelBank();
            // 校验自定义字段
            List<FexCustFieldRule> codeList = findAllBy(fexCustFieldRule.getId(), null, fieldCode);
            if (CollectionUtil.isNotEmpty(codeList)) {
                throw new ParameterException(ForeignMessageEnum.FEX_CUST_FIELD_MESSAGE_CODE_UNIQUE);
            }

            // 校验系统预置字段
            List<FexTranReqFieldVo> validFields = fexTranReqFieldService.getAllValidFields();
            if (CollectionUtil.isNotEmpty(validFields)) {
                boolean exists = validFields.stream()
                        .anyMatch(vo -> fieldCode.equals(vo.getFieldCode()));
                if (exists) {
                    throw new ParameterException(ForeignMessageEnum.FEX_CUST_FIELD_MESSAGE_FIELDCODE_UNIQUE);
                }

                boolean existsLabelBank = validFields.stream()
                        .anyMatch(vo -> labelBank.equals(vo.getLabelBank()));
                if (existsLabelBank) {
                    throw new ParameterException(ForeignMessageEnum.FEX_CUST_FIELD_MESSAGE_CODE_UNIQUE);
                }
            }
        }
    }

    /**
     * 根据字段名称查询
     * @param fieldName 字段名称
     */
    @Transactional(readOnly = true)
    public List<FexCustFieldRule> findAllBy(String id,String fieldName, String fieldCode){
        return fexCustFieldRuleDao.findAllBy(id, fieldName, fieldCode);
    }

    /**
     * 删除外汇产品信息
     *
     * @param id
     */
    public void deleteEntity(String id, ICurrentUser user) {
        // 引用校验
        FexCustFieldRule fexCustFieldRule = get(id);
        if (ForeignConstant.TRAN_TYPE_4.equals(fexCustFieldRule.getAppRange2())) {
            List<FexTranReqConfigVo> fexTranReqConfigVos = fexTranReqConfigService.listByReferenced(fexCustFieldRule.getId());
            if (CollectionUtil.isNotEmpty(fexTranReqConfigVos)) {
                List<String> templateNameList = fexTranReqConfigVos.stream().map(e -> e.getTemplateNo() + "-" + e.getTemplateName()).collect(Collectors.toList());
                String templateNames = String.join(",", templateNameList);
                throw new ParameterException(ForeignMessageEnum.FEX_CUST_FIELD_REFERENCED_EXIST, templateNames);
            }
        }
        Boolean isOk = fexCotpCustInfoDao.getCustInfoByBusFieldId(id);
        if(isOk){
            throw new ParameterException(BaseDataMessageEnum.CMS_LOAN_DATA_REFERENCED_EXIST);
        }
        this.delete(id);
    }

    public FexCustFieldRuleVO getRuleById(String id){
        return fexCustFieldRuleDao.getRuleById(id);
    }


    /**
    * 获取交易对手自定义字段
    *@Param cotpGroupId
    *@return 
    */
    public List<FexCustFieldRuleVO> listByCotp(String cotpGroupId){
       return fexCustFieldRuleDao.listByCotp(cotpGroupId);
    }

    /**
     * 根据应用范围获取自定义规则
     * @param appRange1
     * @param appRange2
     * @return
     */
    public List<FexCustFieldRuleVO> listByAppRange(String appRange1, String appRange2){
        return fexCustFieldRuleDao.listByAppRange(appRange1, appRange2);
    }


    public List<FexCustFieldRuleVO> getByIds(List<String> custFieldIds) {
        return fexCustFieldRuleDao.getByIds(custFieldIds);
    }

    public List<FexCustFieldRuleVO> getByFieCodes(List<String> fexCustFieCodeList) {
        return fexCustFieldRuleDao.getByFieCodes(fexCustFieCodeList);
    }
}
