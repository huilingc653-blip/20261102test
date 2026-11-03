package com.bytter.report.domain.cms.dao;

import com.bytter.report.domain.cms.vo.*;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luxy
 * @Title: RatBkAmountCalculateDao
 * @ProjectName T3.4
 * @Description: TODO
 * @date 2020/7/28 14:05
 */
@Repository
public class RatBkAmountCalculateDao extends BaseDao {

    public List<RatBkCalculateVO> queryRatBkDisList(RatBkCalculateSearchVO searchVO, List<String> corpAuth) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("  select bb.sys_bank_code as bankCode, bb.bank_name  as bankName,rat.corp_id as corpId, rat.corp_name as corpName,")
                .append(" rb.cur_code  as curCode, rb.cur_name as curName,rb.begindate, rb.enddate,rat.item_code as itemCode,rat.itemName as itemName,rat.item_flag as itemFlag,")
                .append(" rat.money,rat.maxMoney,rat.useMoney,rat.approveMoney,rat.syscode,rat.bill_id as billId,rat.bill_no as billNo, ")
                .append(" rat.init_cur_code as billCurCode,rat.exchange_rate as exchangeRate")
                .append(" from ( ")
                .append(" select n.*, u.syscode, u.bill_id, u.bill_no, u.init_cur_code,u.exchange_rate ")
                .append(" from (select m.corp_id, m.corp_name,")
                .append(" m.ITEM_CODE, ")
                .append(" m.itemName, ")
                .append(" m.money, ")
                .append(" m.maxMoney,")
                .append(" m.item_flag, ")
                .append(" m.rat_code, ")
                .append(" m.dis_code, ")
                .append(" m.item_order,")
                .append(" bt_nvl_num(ratApply.money, 0) useMoney, ")
                .append(" bt_nvl_num(ratAway.money, 0) approveMoney ")
                .append(" from (SELECT RBD.corp_id, RBD.corp_name,")
                .append(" RBD.RAT_CODE, ")
                .append(" A.dis_code, ")
                .append(" A.ITEM_CODE, ")
                .append(" A.itemName, ")
                .append(" A.MONEY, ")
                .append(" A.maxMONEY,")
                .append(" A.ITEM_FLAG, ")
                .append(" A.item_order, ")
                .append(" A.comp_Sign ")
                .append(" FROM RAT_BKRATION_DIS RBD, ")
                .append(" (SELECT RBDD.DIS_CODE, ")
                .append(" RBDD.ITEM_CODE, ")
                .append(" ric.NAME as itemName, ")
                .append(" RBDD.ITEM_FLAG, ")
                .append(" RBDD.item_order, ")
                .append(" RBDD.comp_Sign, ")
                .append(" SUM(RBDD.MONEY + RBDD.SUPER_BY_RELIEF_MONEY - RBDD.SUPER_RELIEF_MONEY) MONEY, ")
                .append(" SUM(RBDD.max_MONEY + RBDD.BY_RELIEF_max_MONEY -RBDD.RELIEF_max_MONEY) as maxMONEY ")
                .append(" FROM RAT_BKRATION_DIS        RBD, ")
                .append("  RAT_BKRATION_DIS_DETAIL RBDD, ")
                .append("  RAT_ITEM_CUSTOM ric ")
                .append(" WHERE RBDD.DIS_CODE = RBD.id and rbdd.ITEM_CODE = ric.ID ")
                .append(" GROUP BY RBDD.DIS_CODE, ")
                .append(" RBDD.ITEM_CODE, ")
                .append(" RBDD.ITEM_FLAG, ")
                .append(" RBDD.item_order, ")
                .append(" RBDD.comp_Sign, ric.NAME) A ")
                .append(" WHERE RBD.id = A.DIS_CODE ");
        if (!EmptyUtils.isEmpty(searchVO.getCorpId())) {//单位不为空
            sql.append(" AND " + SqlUtil.getInSql(searchVO.getCorpId().split(","), "RBD.corp_id", true));
        } else if (!EmptyUtils.isEmptyList(corpAuth)) {
            sql.append(" AND ").append(getSQLIn(corpAuth, 900, "RBD.corp_id"));
        }
        if(EmptyUtils.isNotEmpty(searchVO.getItemCode())){
            sql.append(" and A.ITEM_CODE in(:itemCode) ");
            params.put("itemCode",searchVO.getItemCode().split(","));
        }
        sql.append(" AND RBD.STATUS = 95) M ")
                .append(" left join (select urat.rat_code, ")
                .append(" urat.dis_code, ")
                .append(" urat.item_code, ")
                .append(" sum(urat.money) money ")
                .append(" from rat_uration urat ")
                .append(" where urat.rat_type = 0 ")
                .append(" and urat.status = 2 ")
                .append(" group by urat.rat_code, urat.dis_code, urat.item_code) ratApply ")
                .append(" on m.dis_code = ratApply.dis_code ")
                .append(" and m.rat_code = ratApply.rat_code ")
                .append(" and m.item_code = ratApply.item_code ")
                .append(" left join (select urat.rat_code, ")
                .append(" urat.dis_code, ")
                .append(" urat.item_code, ")
                .append(" sum(urat.money) money ")
                .append(" from rat_uration urat ")
                .append(" where urat.rat_type = 0 ")
                .append(" and urat.status = 1 ")
                .append(" group by urat.rat_code, urat.dis_code, urat.item_code) ratAway ")
                .append(" on m.dis_code = ratAway.dis_code ")
                .append(" and m.rat_code = ratAway.rat_code ")
                .append(" and m.item_code = ratAway.item_code ) N")
                .append(" left join rat_uration u ")
                .append(" on n.ITEM_CODE = u.item_code ")
                .append("  and n.dis_code = u.dis_code ")
                .append(" and u.status in (1, 2) ")
                .append(" and u.rat_type = 0 ")
                .append(" ) rat,")
                .append(" bt_bank bb, ")
                .append(" rat_bkration rb ")
                .append(" where rat.rat_Code = rb.id ")
                .append(" and rb.BANK_CODE = bb.sys_bank_code ");
        if (EmptyUtils.isNotEmpty(searchVO.getCurCode())) {
            sql.append("  and rb.cur_code in(:curCode) ");
            params.put("curCode", searchVO.getCurCode().split(","));
        }
        if (EmptyUtils.isNotEmpty(searchVO.getBankCode())) {
            sql.append("  and rb.bank_code=:bankCode ");
            params.put("bankCode", searchVO.getBankCode());
        }
        sql.append(" order by rat.corp_id, rat.rat_Code, rat.item_Order ");
        return listBySQLAliasToBean(sql.toString(), params, RatBkCalculateVO.class);
    }

    /**
     * 查询占用授信
     */
    public BigDecimal queryRatUse(String sysCode, String billId,  String billNo,Date calEndDate){
        StringBuilder sql=new StringBuilder();
        if(EmptyUtils.isNotEmpty(sysCode)){
            switch (sysCode) {
                case "cms":
                    queryProvideUseRatMoney(sql);
                    break;
                case "gua":
                    queryGuaranteeUseRatMoney(sql);
                    break;
                default:
                    quryRatUseMoney(sql);
            }
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("billId",billId);
        params.put("billNo",billNo);
        params.put("calEndDate",calEndDate);
        Map<String, Object> result = getBySQL(sql.toString(), params);
        BigDecimal calReleaseMoney=BigDecimal.ZERO;
        if(null!=result&&null!=result.get("MONEY")){
            calReleaseMoney=new BigDecimal(result.get("MONEY").toString());
        }
        return calReleaseMoney;
    }

    /**
     * 查询银行借款预计释放额度
     * @param sql
     * @return
     */
    public StringBuilder queryProvideUseRatMoney(StringBuilder sql){
        sql.append(" select sum(provide.money) money from ( ");
        sql.append(" select sum(ist.repay_money) as money ")
                .append(" from cms_provide_interest ist ")
                .append(" where ist.bill_code in ")
                .append(" (select bill_code ")
                .append(" from cms_provide_loan_send ")
                .append(" where loan_code =:billNo ) ")
                .append(" and ist.status=0 ")//查询还款明细中未还款的数据
                .append(" and ist.interest_sign=1 ")
                .append(" and ist.repay_date <=:calEndDate  ");
        sql.append(" union ");
        sql.append(" select sum(repay.repay_money) as money ")//查询还款中的数据
                .append(" from cms_provide_loan_repay repay ")
                .append(" where repay.send_code in ")
                .append(" ((select bill_code ")
                .append(" from cms_provide_loan_send ")
                .append(" where loan_code =:billNo)) ")
                .append(" and repay.biz_status not in (-2, 110, 48) ")
                .append(" and repay.repay_date <= :calEndDate ");
        sql.append(" ) provide ");
        return sql;
    }

    /**
     * 查询保函预计释放额度
     * @param sql
     * @return
     */
    public StringBuilder queryGuaranteeUseRatMoney(StringBuilder sql){
        sql.append("  select gua.guarantee_money as money ")
                .append(" from gua_guarantee_info gua ")
                .append("  where gua.status in(11,95,96,97,98,103)  ")
//                .append(" and gua.status < 100")
                .append(" and gua.id = :billId ")
                .append(" and (gua.end_date <=:calEndDate or ")
                .append(" gua.expect_end_date <=:calEndDate ) ");
        return sql;
    }

    /**
     * 查询手工占用授信预计释放额度
     * @param sql
     * @return
     */
    public StringBuilder quryRatUseMoney(StringBuilder sql){
        sql.append("  select sum(u.money) as money ")
                .append(" from rat_uration u ")
                .append("  where u.rat_type = 0 ")
                .append(" and u.status in(1,2) ")
                .append(" and u.bill_id = :billId ")
                .append(" and u.end_date <=:calEndDate");
        return sql;
    }
}
