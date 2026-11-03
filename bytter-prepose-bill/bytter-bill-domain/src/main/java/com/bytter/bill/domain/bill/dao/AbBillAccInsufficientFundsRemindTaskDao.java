package com.bytter.bill.domain.bill.dao;

import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class AbBillAccInsufficientFundsRemindTaskDao extends BaseDao<AbAccrecordsVO, String>  {

    /**
    * @Description 获取还款和票据到期的数据
    * @author zhous
    * @date 2025/6/25 16:34
    */
    public List<AbAccrecordsVO> listAccInsufficientFundsReminder(Map<String, Object> params, boolean isRangeQuery) {
        StringBuilder sql = new StringBuilder("select m.bankAcc,BT_NVL_NUM(m.repayMoney,0) as repayMoney,BT_NVL_NUM(m.billMoney,0) as billMoney,BT_NVL_NUM(m.releasePaymentMoney,0) as releasePaymentMoney,");
        sql.append("BT_NVL_NUM(m.billNums,0) as billNums,bba.corp_id as corpId,bba.acc_name as reqNm,bba.bank_name as bankName,sc.name as corpName from ");
        sql.append(" ( select a.bankAcc,a.repayMoney,b.billMoney,b.releasePaymentMoney,b.billNums from ");
        getLoanSql(sql,isRangeQuery);
        sql.append(" left join ");
        getBillInfoSql(sql,isRangeQuery);
        sql.append(" on a.bankAcc = b.bankAcc ");
        sql.append(" union ");
        sql.append(" select b.bankAcc,a.repayMoney,b.billMoney,b.releasePaymentMoney,b.billNums from  ");
        getBillInfoSql(sql,isRangeQuery);
        sql.append(" left join ");
        getLoanSql(sql,isRangeQuery);
        sql.append(" on a.bankAcc = b.bankAcc ");
        sql.append(" ) m ");
        sql.append(" left join bt_bank_acc bba on bba.bank_acc = m.bankAcc ");
        sql.append(" left join sys_corp sc on bba.corp_id = sc.id ");
        return this.listBySQLAliasToBean(sql.toString(), params, AbAccrecordsVO.class);
    }

    //票据到期sql
    private void getBillInfoSql(StringBuilder sql, boolean isRangeQuery) {
        sql.append(" ( select c.bank_acc as bankAcc,sum(z.bill_money) as billMoney,sum(z.bill_money - z.bill_money*(case when c.BAIL_PERCENT is null then 0 else c.BAIL_PERCENT end)) as  releasePaymentMoney,count(1) as billNums ");
        sql.append(" from AB_VOUCHERINFO_VICE c ");
        sql.append(" left join AB_VOUCHERINFO z on z.id=c.vid ");
        sql.append(" left join AB_ACCRECORDS l on c.id = l.vvid ");
        sql.append(" left join AB_ACCRECORDS lt on c.id = lt.vvid and lt.brief_id='004' and lt.opt_status='19' ");
        sql.append(" left join AB_ACCRECORDS laa on c.id = laa.vvid and laa.brief_id='6120' and laa.opt_status='19' ");
        sql.append(" where c.type='0' and c.valid_sign != 3 and l.status = 95 and l.opt_status = 19 and z.orig_no is not null and z.orig_no <>'' ");
        sql.append(" and ((l.send_sign = '1' and l.BRIEF_ID = '003') or (l.send_sign = '0' and l.BRIEF_ID = '001')) and lt.id is null and laa.id is null ");
        sql.append(" and (c.bail_percent != 1 or c.bail_percent is null )");
        if(!isRangeQuery){
            sql.append(" and z.end_date = :endDate1  ");
        }else{
            sql.append(" and z.end_date >= :endDate1 and z.end_date <= :endDate2 ");
        }
        sql.append(" group by  c.bank_acc ) b  ");
    }

    //借款还款信息sql
    private void getLoanSql(StringBuilder sql, boolean isRangeQuery) {
        sql.append(" ( select cps.ONLINE_PAYMENT_ACC as bankAcc,BT_NVL_NUM(sum(case when cp.status ='0' then cp.REPAY_MONEY else 0 end),0) ");
        sql.append(" + BT_NVL_NUM(sum(case when cp.status ='0' then (case when cp.REPAY_MONEY > 0 then cp.INTEREST else cp.PAY_INTEREST end   ) else 0 end) ,0) as repayMoney  ");
        sql.append(" from cms_provide_loan_send cps left join cms_provide_interest cp on cps.bill_code = cp.bill_code ");
        sql.append(" where 1=1 ");
        if(!isRangeQuery){
            sql.append(" and cp.REPAY_DATE = :endDate1 ");
        }else{
            sql.append(" and cp.REPAY_DATE >= :endDate1 and cp.REPAY_DATE <= :endDate2 ");
        }
        sql.append(" group by  cps.ONLINE_PAYMENT_ACC ) a ");
    }

}
