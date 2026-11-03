package com.bytter.biz.foreign.service;

import com.bytter.biz.foreign.dao.FexSettleFillRuleDao;
import com.bytter.biz.foreign.entity.FexSettleFillRule;
import com.bytter.biz.foreign.entity.FexSettleFillRuleAdd;
import com.bytter.biz.foreign.vo.FexSettleFillRuleVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.system.api.ICorpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author luxy
 * @Title: FexSettleFillRuleService
 * @ProjectName T3(3.2)
 * @Description: 结算自动填充规则服务层
 * @date 2020/2/18 10:23
 */
@Service("FexSettleFillRuleService")
public class FexSettleFillRuleService extends BaseService<FexSettleFillRule,String> {
    @Autowired
    private FexSettleFillRuleDao fexSettleFillRuleDao;
    @Autowired
    private FexSettleFillRuleAddService fillRuleAddService;
    @Autowired(required=false)
    private ICorpService corpService;
    @Autowired
    private FexCorpBankAccService fexCorpBankAccService;

    @Override
    public BaseDao<FexSettleFillRule, String> getEntityDao() {
        return fexSettleFillRuleDao;
    }

    /**
     * 查询结算自动填充规则列表数据
     */
    public DataTablesOutput datatable(DataTablesInput input,ICurrentUser user){
        List<String> corps = corpService.getUserCorpIds(ResourceConstant.FOREIGN_FEX_SETTLEFILL_RULE_QUERY, user.getId());
        return fexSettleFillRuleDao.datatable(input,corps);
    }

    /**
     * 保存或修改数据
     */
    public void saveOrUpdateRule(FexSettleFillRuleVO ruleVO, ICurrentUser user){
        FexSettleFillRule rule=new FexSettleFillRule();
        if(null==ruleVO.getId()|| StringUtils.isEmpty(ruleVO.getId())){//新增数据
            BeanUtils.copyNotNullProperties(ruleVO,rule);
            rule.setCreateBy(user.getId());
            rule.setCreateDate(DateUtils.getDate());
        }else{//修改数据
            rule=this.get(ruleVO.getId());
            BeanUtils.copyNotNullProperties(ruleVO,rule,"id");
            rule.setUpdateBy(user.getId());
            rule.setUpdateDate(DateUtils.getDate());
            fillRuleAddService.deleteRuleAdd(ruleVO.getId());//删除之前的规则补充数据
        }
        if (!EmptyUtils.isEmpty(ruleVO.getOppId())) {
            rule.setCotpId(ruleVO.getOppId());
        }
        //判断交易对手与单位共享
        if(ruleVO.getTranType().equals("4") && !EmptyUtils.isEmpty(rule.getCorpId()) && !EmptyUtils.isEmpty(ruleVO.getCotpName())) {
            fexCorpBankAccService.makeExternalWithCorp(ruleVO.getOppId(), rule.getCorpId(), ruleVO.getCotpName());
        }

        this.saveOrUpdate(rule);
        this.getSession().flush();
        fillRuleAddService.saveRuleAdd(ruleVO.getRuleAddList(),rule.getId());//保存规则补充数据
    }


    /**
     * 根据id删除结算自动填充规则
     * @param id
     */
    public void deleteRule(String id){
        fillRuleAddService.deleteRuleAdd(id);//删除规则补充数据
        this.delete(id);
    }

    /**
     * 根据id查询结算自动填充规则
     * @param id
     * @return
     */
    public FexSettleFillRuleVO getFillRuleById(String id){
        FexSettleFillRuleVO ruleVO=fexSettleFillRuleDao.getFillRuleById(id);
        List<FexSettleFillRuleAdd> addList=fillRuleAddService.getRuleAddByRuleId(id);
        ruleVO.setRuleAddList(addList);
        return ruleVO;
    }
}
