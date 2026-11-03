package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.vo.FexCorpPoolBankaccQuotaVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import org.springframework.stereotype.Repository;

/**
 * @author wangzx
 * @version 1.0
 * @Title: dao
 * @Description: 资金池账户额度查询
 * @date 2020-03-11 10:31:56
 */
@Repository
public class FexCorpPoolBankaccQuotaDao extends BaseDao<FexCorpPoolBankaccQuotaVO, String> {

    /**
     * 指令列表查询
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append("select p.pool_id as poolId, c.pool_name as poolName, p.cur_id as curId, cur.cur_name as curName, cur.english_code as englishCode,")
                .append(" p.rate_type as rateTypeId, f.cur_id as accCurId, accCur.english_code as accEnglishCode, accCur.cur_name as accCurName,")
                .append(" f.corp_id as corpId, sc.name as corpName, f.acc_id as accId, acc.bank_acc as bankAcc,");
        sql.append("case when t1.pay_money is null and t2.pay_money is null then null ");
        sql.append(" when t1.pay_money is not null and t2.pay_money is null then (t1.pay_money - bt_nvl_num(t2.pay_money,0))");
        sql.append(" when t1.pay_money is null and t2.pay_money is not null then (bt_nvl_num(t1.pay_money,0) - t2.pay_money)");
        sql.append(" when t1.pay_money is not null and t2.pay_money is not null then (t1.pay_money - t2.pay_money) end as externalDebtBal, ");//外债余额
        sql.append("e1.limit_money as externalDebtLimit, ");//外债限额
        sql.append("case when t4.pay_money is null and t3.pay_money is null then null ");
        sql.append(" when t4.pay_money is not null and t3.pay_money is null then (t4.pay_money - bt_nvl_num(t3.pay_money,0))");
        sql.append(" when t4.pay_money is null and t3.pay_money is not null then (bt_nvl_num(t4.pay_money,0) - t3.pay_money)");
        sql.append(" when t4.pay_money is not null and t3.pay_money is not null then (t4.pay_money - t3.pay_money) end as externalLoanBal,");//对外放款余额
        sql.append("e2.limit_money as externalLoanLimit, ");//对外放款限额
        sql.append("lr.pay_money as inflowBal, ");//流入余额
        sql.append("lc.pay_money as outflowBal, ");//流出余额
        sql.append("case when lr.pay_money is null and lc.pay_money is null then null ");
        sql.append(" when lr.pay_money is not null and lc.pay_money is null then (lr.pay_money - bt_nvl_num(lc.pay_money,0))");
        sql.append(" when lr.pay_money is null and lc.pay_money is not null then (bt_nvl_num(lr.pay_money,0) - lc.pay_money)");
        sql.append(" when lr.pay_money is not null and lc.pay_money is not null then (lr.pay_money - lc.pay_money) end as netInflowBal,");//净流入额
        sql.append("e3.limit_money as netInflowLimit ");//净流入限额
        sql.append(" from fex_corp_pool_bankacc f left join fex_corp_pool p on f.parent_id = p.id left join fc_corp_pool c on p.pool_id = c.id");
        sql.append(" left join sys_corp sc on f.corp_id = sc.id");
        sql.append(" left join bt_currency cur on p.cur_id = cur.id");
        sql.append(" left join bt_currency accCur on f.cur_id = accCur.id");
        sql.append(" left join bt_bank_acc acc on f.acc_id = acc.id");
        //外债余额
        //收款
        sql.append(" left join (").append(getCommonSql(ForeignConstant.FEX_TRAN_SIGN_0,ForeignConstant.FEX_TRAN_SIGN_0,false));
        sql.append(" ) t1 on acc.bank_acc = t1.corp_bank_acc and f.cur_id = t1.cur_id");
        //付款
        sql.append(" left join (").append(getCommonSql(ForeignConstant.FEX_TRAN_SIGN_1,ForeignConstant.FEX_TRAN_SIGN_0,false));
        sql.append(" ) t2 on acc.bank_acc = t2.corp_bank_acc and f.cur_id = t2.cur_id");
        sql.append(" left join fex_corp_pool_limit e1 on f.id = e1.related_id and e1.limit_type = '1'");
        //对外放款
        //收款
        sql.append(" left join (").append(getCommonSql(ForeignConstant.FEX_TRAN_SIGN_0,ForeignConstant.FEX_TRAN_SIGN_1,false));
        sql.append(" ) t3 on acc.bank_acc = t3.corp_bank_acc and f.cur_id = t3.cur_id");
        //付款
        sql.append(" left join (").append(getCommonSql(ForeignConstant.FEX_TRAN_SIGN_1,ForeignConstant.FEX_TRAN_SIGN_1,false));
        sql.append(" ) t4 on acc.bank_acc = t4.corp_bank_acc and f.cur_id = t4.cur_id");
        sql.append(" left join fex_corp_pool_limit e2 on f.id = e2.related_id and e2.limit_type = 2");
        //流入余额
        sql.append(" left join (").append(getCommonSql(ForeignConstant.FEX_TRAN_SIGN_0,"",true));
        sql.append(" ) lr on acc.bank_acc = lr.corp_bank_acc and f.cur_id = lr.cur_id ");
        //流出余额
        sql.append(" left join (").append(getCommonSql(ForeignConstant.FEX_TRAN_SIGN_1,"",true));
        sql.append(" ) lc on acc.bank_acc = lc.corp_bank_acc and f.cur_id = lc.cur_id ");
        sql.append(" left join fex_corp_pool_limit e3 on f.id = e3.related_id and e3.limit_type = 3 where f.parent_sign = '1' ");
        sql.append(joinSql);
        return getDynamicBySQL(dataTablesInput, sql.toString(), FexCorpPoolBankaccQuotaVO.class, "f", false);
    }

    /**
     * 查询交易类型和产品的交易公共方法
     * @param cdSign
     * @param tranSign
     * @param inflowOut
     * @return
     */
    public String getCommonSql(String cdSign, String tranSign, boolean inflowOut){
        StringBuilder sql = new StringBuilder();
        sql.append("select l.corp_bank_acc, l.cur_id, sum(bt_nvl_num(l.pay_money, 0)) as pay_money");
        sql.append(" from fex_cash_flow l inner join fex_tran_req r on l.tran_bill_id = r.id where l.status = 95");
        sql.append(" and l.tran_type = '3' and l.cd_sign = '"+cdSign+"'");
        sql.append(" and exists (select * from fex_corp_pool_bankacc f left join bt_bank_acc b on f.acc_id = b.id where 1 = 1");
        sql.append(" and b.bank_acc = l.corp_bank_acc and exists (select 1 from fex_corp_pool_tran t where t.tran_type = l.tran_type");
        sql.append(" and t.bankacc_id = f.id and t.pro_id = r.pro_id");
        sql.append(" and t.tran_type = '3' ");
        if(inflowOut){
            sql.append(" and r.acc_nature = '2' ");
        }else{
            sql.append(" and r.acc_nature in ('1','2') ");
        }
        if(StringUtils.isNotBlank(tranSign)){
            sql.append("and t.tran_sign = '"+tranSign+"'");
        }
        sql.append(" )) group by l.corp_bank_acc, l.cur_id");
        return sql.toString();
    }
}
