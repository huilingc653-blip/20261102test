package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexTranGroupRuleDao;
import com.bytter.biz.foreign.entity.FexTranGroupRule;
import com.bytter.biz.foreign.entity.FexTranGroupRuleDtl;
import com.bytter.biz.foreign.vo.FexTranGroupRuleDtlVO;
import com.bytter.biz.foreign.vo.FexTranGroupRuleVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.biz.payment.api.enums.ForeignMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qinl
 * @version 1.0
 * @description: 外汇交易组规则
 * @date 2020-02-18
 */
@Service("fexTranGroupRuleService")
@Transactional
public class FexTranGroupRuleService extends BaseService<FexTranGroupRule, String> {
    @Autowired
    private FexTranGroupRuleDao fexTranGroupRuleDao;
    @Autowired
    private FexTranGroupRuleDtlService fexTranGroupRuleDtlService;


    @Override
    public BaseDao<FexTranGroupRule, String> getEntityDao() {
        return fexTranGroupRuleDao;
    }


    public List<FexTranGroupRule> listByValidSign(Boolean validSign) {
        return fexTranGroupRuleDao.selectListByValidSign(validSign);
    }

    public FexTranGroupRule getByRuleName(String ruleName) {
        return fexTranGroupRuleDao.getByRuleName(ruleName);
    }

    /**
     * 保存
     * @param fexTranGroupRuleVO
     */
    public void saveRuleAndDetails(FexTranGroupRuleVO fexTranGroupRuleVO) {
        List<FexTranGroupRuleDtlVO> detailList = fexTranGroupRuleVO.getFexTranGroupRuleDtlVOList();
        if (detailList == null) {
            throw new ParameterException(ForeignMessageEnum.FEX_TRAN_MESSAGE_TRANGROUP_DETAILS_NULL);
        }
        FexTranGroupRule fexTranGroupRule = this.getByRuleName(fexTranGroupRuleVO.getRuleName());
        if (fexTranGroupRule != null) {//如果存在
            throw new ParameterException(ForeignMessageEnum.FEX_TRAN_MESSAGE_TRANGROUP_ISEXIST);
        }
        //保存规则
        FexTranGroupRule rule = BeanUtils.copyNewInstanceProperties(fexTranGroupRuleVO, FexTranGroupRule.class);
        this.saveEntity(rule);
        //保存规则详细，list集合去重操作
        List<FexTranGroupRuleDtl> ruleDtls = this.uniqueDetail(detailList, rule.getId());
        fexTranGroupRuleDtlService.batchSave(ruleDtls);
    }

    /**
     * 更新
     * @param fexTranGroupRuleVO
     */
    public void updateRuleAndDetails(FexTranGroupRuleVO fexTranGroupRuleVO) {
        List<FexTranGroupRuleDtlVO> detailList = fexTranGroupRuleVO.getFexTranGroupRuleDtlVOList();
        if (detailList == null) {
            throw new ParameterException(ForeignMessageEnum.FEX_TRAN_MESSAGE_TRANGROUP_DETAILS_NULL);
        }
        FexTranGroupRule fexTranGroupRule = this.get(fexTranGroupRuleVO.getId());
        BeanUtils.copyProperties(fexTranGroupRuleVO, fexTranGroupRule);
        List<FexTranGroupRuleDtlVO> fexTranGroupRuleDtlVOS = fexTranGroupRuleDtlService.listByRuleId(fexTranGroupRule.getId());
        this.updateEntity(fexTranGroupRule);
        List<String> ids = new ArrayList<>();
        for(FexTranGroupRuleDtlVO vo : fexTranGroupRuleDtlVOS){
            ids.add(vo.getId());
        }
        fexTranGroupRuleDtlService.delete(ids);
        List<FexTranGroupRuleDtl> ruleDtls = this.uniqueDetail(detailList, fexTranGroupRule.getId());
        fexTranGroupRuleDtlService.batchSave(ruleDtls);
    }

    /**
     * 去重操作
     * @param detailList
     * @return
     */
    private List<FexTranGroupRuleDtl> uniqueDetail(List<FexTranGroupRuleDtlVO> detailList,String ruleId){
        List<FexTranGroupRuleDtl> list = new ArrayList<>();
        Map<String,FexTranGroupRuleDtlVO> map= new HashMap<>();
        for (FexTranGroupRuleDtlVO vo : detailList) {
            if(StringUtils.isNotBlank(vo.getTranType())&&StringUtils.isNotBlank(vo.getProId())){
                vo.setId(null);
                vo.setRuleId(ruleId);
                map.put(vo.getRuleId()+vo.getProId(), vo);
            }
        }
        for(String s:map.keySet()){
            FexTranGroupRuleDtlVO vo = map.get(s);
            FexTranGroupRuleDtl entity = BeanUtils.copyNewInstanceProperties(vo, FexTranGroupRuleDtl.class);
            list.add(entity);
        }
        return list;
    }
}
