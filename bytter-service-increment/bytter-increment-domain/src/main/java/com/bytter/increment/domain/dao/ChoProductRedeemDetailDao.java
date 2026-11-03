package com.bytter.increment.domain.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.increment.domain.entity.ChoProductRedeemDetail;
import com.bytter.increment.domain.vo.ChoProductRedeemDetailVO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: Dao
 * @Description: 理财产品
 * @author shenc
 * @date 2019-03-26 14:03:30
 * @version 1.0
 *
 */
@Repository
public class ChoProductRedeemDetailDao extends BaseDao<ChoProductRedeemDetail,String> {
    /**
     * 根据ID获取Vo显示实体
     * @param cprId 赎回主表id
     * @return
     */
    public List<ChoProductRedeemDetailVO> getChoProductRedeemDetailVOById(String cprId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select prd.id as id, pr.bank_type_id as bankTypeId,");
        sql.append(" pr.bank_name as bankName,pr.bank_Info_Id as bankInfoId,pr.product_code as productCode, ");
        sql.append(" pr.product_type as productType, pr.bank_acc as bankAcc, pr.buy_money as buyMoney, ");
        sql.append(" pr.buy_date as buyDate, pr.day_limit as dayLimit, pr.cash_date as cashDate, ");
        sql.append(" pr.refer_income_rate as referIncomeRate,pr.END_REFER_INCOME_RATE as endReferIncomeRate,pr.refer_income_money as referIncomeMoney, pr.END_REFER_INCOME_MONEY as endReferIncomeMoney, pr.income_clause as incomeClause,");
        sql.append(" pr.cur_id as curId, pr.interest_start_date as interestStartDate,pr.deadline_date as deadlineDate, ");
        sql.append(" pr.is_move_up as isMoveUp, pr.capital_source as capitalSource, pr.charge_money as reqChargeMoney, pr.sub_fee_money as reqSubFeeMoney,");
        sql.append(" pt.FINANCIAL_TYPE as financialType, pr.PAY_WAY as payWay, pr.CONTRACT_NO as contractNo, ");
        sql.append(" pr.INCOME_TYPE as incomeType, pr.IS_STORE as isStore, pr.START_CONTRACT_RATE as startContractRate, pr.END_CONTRACT_RATE as endContractRate, ");
        sql.append(" prd.redeem_money as redeemMoney, prd.redeem_date as redeemDate, prd.income_rate as incomeRate, ");
        sql.append(" pr.daily_net_val as dailyNetVal, pr.portion as portion, prd.REDEEM_DAILY_NET_VAL as redeemDailyNetVal, prd.REDEEM_PORTION as redeemPortion,");
        sql.append(" prd.income_money as incomeMoney,prd.sub_fee_money as subFeeMoney, prd.redeem_charge_money as redeemChargeMoney, ");
        sql.append(" case when pr.IS_MAINTAIN_VAL is null then '1' else pr.IS_MAINTAIN_VAL end isMaintainVal, ");
        sql.append(" prd.charge_money as chargeMoney,prd.req_Id as reqId,prd.rmk as rmk,prd.transfer_date as transferDate ");
        sql.append(" from cho_product_redeem_detail prd ");
        sql.append(" left join cho_product_req pr on pr.id = prd.req_id ");
        sql.append(" left join cho_product_type pt on pt.product_code = pr.product_code ");
        sql.append(" where 1 = 1 ");
        sql.append(" and prd.cpr_id = :cprId ");
        Map<String,String> params = new HashMap<>();
        params.put("cprId",cprId);
        return listBySQLAliasToBean(sql.toString(),params,ChoProductRedeemDetailVO.class);
    }

    /**
     * 根据赎回id查询赎回明细
     * @param cprId 赎回主表id
     * @return
     */
    public List<ChoProductRedeemDetail> listbyCprId(String cprId){
        StringBuilder hql = new StringBuilder();
        hql.append(" from ChoProductRedeemDetail d where d.cprId = :cprId ");
        Map<String,String> params = new HashMap<>();
        params.put("cprId",cprId);
        return listByHQLAliasToBean(hql.toString(),params,ChoProductRedeemDetail.class);
    }

    /**
     * 根据赎回id删除赎回明细
     * @param cprId 赎回主表id
     * @return
     */
    public int delByCprId(String cprId){
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from cho_product_redeem_detail where cpr_id = :cprId ");
        Map<String,String> params = new HashMap<>();
        params.put("cprId",cprId);
        return this.getSession().createSQLQuery(sql.toString()).setProperties(params).executeUpdate();
    }

}
