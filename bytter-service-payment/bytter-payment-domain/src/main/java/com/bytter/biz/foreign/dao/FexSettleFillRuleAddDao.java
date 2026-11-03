package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexSettleFillRuleAdd;
import com.bytter.biz.foreign.vo.FexSettleFillRuleVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luxy
 * @Title: FexSettleFillRuleAddDao
 * @ProjectName T3(3.2)
 * @Description: 结算自动填充规则补充数据访问层
 * @date 2020/2/19 11:29
 */
@Repository
public class FexSettleFillRuleAddDao extends BaseDao<FexSettleFillRuleAdd,String> {

    /**
     * 根据规则id查询规则补充字段
     */
    public List<FexSettleFillRuleAdd> getRuleAddByRuleId(String ruleId){
        Map<String,String> map=new HashMap<>();
        String hql = " from FexSettleFillRuleAdd b where b.ruleId = :ruleId";
        map.put("ruleId",ruleId);
        return listByHQLAliasToBean(hql, map, FexSettleFillRuleAdd.class);
    }

    /**
     * 根据规则id删除规则补充数据
     * @param ruleId
     */
    public void deleteRuleAddByRuleId(String ruleId){
        Map<String,String> map=new HashMap<>();
        String sql = " delete from FEX_SETTLE_FILL_RULE_ADD where RULE_ID =:ruleId ";
        map.put("ruleId",ruleId);
        this.getSession().createSQLQuery(sql).setProperties(map).executeUpdate();
    }

    /**
     * 根据规则条件查询规则补充数据
     * @param searchVo
     * @return
     */
    public List<FexSettleFillRuleAdd> listRuleAddByRule(FexSettleFillRuleVO searchVo){
        StringBuilder hql = new StringBuilder("select b.id as id,b.ruleId as ruleId,b.fieldName as fieldName,b.fieldVal as fieldVal from FexSettleFillRuleAdd b left join FexSettleFillRule f on b.ruleId=f.id where 1 = 1");
        Map<String,String> params=new HashMap<>();
        //单位
        if(StringUtils.isNotBlank(searchVo.getCorpId())){
            hql.append(" and (f.corpId=:corpId or f.corpId is null)");
            params.put("corpId",searchVo.getCorpId());
        }
        //现金流形式
        if(StringUtils.isNotBlank(searchVo.getCashFlowMethod())){
            hql.append(" and (f.cashFlowMethod=:cashFlowMethod or f.cashFlowMethod is null)");
            params.put("cashFlowMethod",searchVo.getCashFlowMethod());
        }
        //交易类型
        if(StringUtils.isNotBlank(searchVo.getTranType())){
            hql.append(" and (f.tranType=:tranType or f.tranType is null)");
            params.put("tranType",searchVo.getTranType());
        }
        //产品
        if(StringUtils.isNotBlank(searchVo.getProId())){
            hql.append(" and (f.proId=:proId or f.proId is null)");
            params.put("proId",searchVo.getProId());
        }
        //币种
        if(StringUtils.isNotBlank(searchVo.getCurId())){
            hql.append(" and (f.curId=:curId or f.curId is null)");
            params.put("curId",searchVo.getCurId());
        }
        //支付方式
        if(StringUtils.isNotBlank(searchVo.getVoucherTypeId())){
            hql.append(" and (f.voucherTypeId=:voucherTypeId or f.voucherTypeId is null)");
            params.put("voucherTypeId",searchVo.getVoucherTypeId());
        }
        //交易对手
        if(StringUtils.isNotBlank(searchVo.getCotpId())){
            hql.append(" and (f.cotpId=:cotpId or f.cotpId is null)");
            params.put("cotpId",searchVo.getCotpId());
        }
        //交易对手国家地区
        if(StringUtils.isNotBlank(searchVo.getCountryId())) {
            hql.append(" and (f.countryId=:countryId or f.countryId is null)");
            params.put("countryId", searchVo.getCountryId());
        }
        //收付方向
        if(StringUtils.isNotBlank(searchVo.getCdSign())){
            hql.append(" and (f.cdSign=:cdSign or f.cdSign is null)");
            params.put("cdSign",searchVo.getCdSign());
        }
        //银行账户
        if(StringUtils.isNotBlank(searchVo.getAccId())){
            hql.append(" and (f.accId=:accId or f.accId is null)");
            params.put("accId",searchVo.getAccId());
        }
        hql.append(" and f.validSign='1'");
        return listByHQLAliasToBean(hql.toString(), params, FexSettleFillRuleAdd.class);
    }
}
