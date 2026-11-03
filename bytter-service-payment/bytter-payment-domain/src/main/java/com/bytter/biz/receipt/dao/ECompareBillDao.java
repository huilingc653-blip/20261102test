package com.bytter.biz.receipt.dao;

import com.bytter.biz.receipt.entity.ECompareBill;
import com.bytter.biz.receipt.vo.ECompareBillVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 对账单电子回单表
* @author onlineGenerator
* @date 2017-07-04 15:30:28
* @version 1.0
*
*/
@Repository
public class ECompareBillDao extends BaseDao<ECompareBill,String> {
    /**
     * 查询对账单电子回单表
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listForUnion(DataTablesInput input, String commonSql){
        StringBuilder sql = new StringBuilder();
        sql.append("select DISTINCT a.acc_no as accNo,a.acc_name as accName,");
        sql.append("a.corp_id as corpId,a.bill_type as billType,a.bill_no as billNo,");
        sql.append("a.cur_id as curId, a.status as status,");
        sql.append(" b.prints as prints, ");
        sql.append(" BT_TO_CHAR(a.ENCRY_DATE,'yyyy-MM-dd') AS encryDateStr ");
        //sql.append(" (select sum(debit_money) from e_compare_bill where bill_no = a.bill_no) as sumDebitMoney, ");
        //sql.append(" (select sum(credit_money) from e_compare_bill where bill_no = a.bill_no) as sumCreditMoney ");
        sql.append(" from (select acc_no,acc_name,corp_id,bill_type,bill_no,cur_id,status,ENCRY_DATE from e_compare_bill group by acc_no,acc_name,corp_id,bill_type,bill_no,cur_id,status,ENCRY_DATE) a  ");
        sql.append(" left join (select bill_no,corp_id,max(prints) as prints from e_bill_print group by bill_no,corp_id ) b on a.BILL_NO = b.BILL_NO and a.corp_id = b.corp_id ");
        sql.append(" where 1=1 ");
        if(StringUtils.isNotEmpty(commonSql)){
            sql.append(commonSql);
        }
        return getDynamicBySQL(input, sql.toString(), ECompareBillVO.class, "", false);
    }
    /**
     * 根据单据号获取VO
     * @param billNo 单据号
     * @return
     */
    public List<ECompareBillVO> getVOByBillNo(String billNo){
        StringBuilder sql=new StringBuilder();
        Map<String,Object> params=new HashMap<String,Object>();
        sql.append("select DISTINCT a.acc_no as accNo, a.acc_name as accName,");
        sql.append("a.corp_ID as corpId, a.bill_type as billType,a.bill_no as billNo,");
        sql.append("a.summary_comment as summaryComment, a.tally_date as tallyDate,a.cur_id as curId,");
        sql.append("a.bill_code as billCode,a.interest_start_date as interestStartDate,a.debit_money as debit_money,");
        sql.append("a.credit_money as creditMoney,a.balance as balance,a.voucher_no as voucherNo,");
        sql.append("a.status as status,a.prints as prints,validate_code as validateCode,");
        sql.append("subscriber as subscriber,a.encry_date as encryDate,");
        sql.append("(balance-(debit_money-credit_money)) as beginBalanceWay,a.line_no as lineNo ");
        sql.append(" FROM e_compare_bill a,e_bill_print e ");
        sql.append(" WHERE e.bill_no = a.bill_no and a.corp_id =e.corp_id");
        if(StringUtils.isNotEmpty(billNo)){
            sql.append(" AND A.BILL_NO =:billNo");
            params.put("billNo", billNo);
        }
        sql.append(" order by tally_date,VOUCHER_NO ");
        List<ECompareBillVO> infoList=this.listBySQLAliasToBean(sql.toString(), params, ECompareBillVO.class);
        return infoList;
    }
    public ECompareBillVO getSumCompareBills(String billNo) {
        StringBuilder sql=new StringBuilder();
        sql.append("select bill_no as billNo, sum(debit_money) as sumDebitMoney, sum(credit_money) as sumCreditMoney, sum(balance) as sumBalance " );
        sql.append(" from e_compare_bill where bill_no = :billNo ");
        sql.append(" group by bill_no ");
        Map<String,Object> params=new HashMap<>();
        params.put("billNo", billNo);
        List<ECompareBillVO> infoList=this.listBySQLAliasToBean(sql.toString(), params, ECompareBillVO.class);
        if(infoList!=null&&infoList.size()>0){
            return infoList.get(0);
        }
        return null;
    }
    /**
     * 根据条件查询电子回单数量
     * @param voucherNo
     * @param account
     * @param lineNo
     * @return
     */
    public BigDecimal getCountCompareBill(String voucherNo,String account,BigDecimal lineNo){
        String sql = " SELECT COUNT(*) FROM e_compare_bill WHERE voucher_no = :voucherNo " +
                     " AND ACC_NO = :account AND LINE_NO = :lineNo AND status <> -1 ";
        Map<String,Object> params=new HashMap<>();
        params.put("voucherNo", voucherNo);
        params.put("account", account);
        params.put("lineNo", lineNo);
        BigDecimal count = (BigDecimal) createSQLQuery(sql, params).uniqueResult();
        return count;
    }
    /**
     * 根据单据号获取VO
     * @param voucherNo
     * @param account
     * @return
     */
    public ECompareBillVO getVOByVoucherNo(String voucherNo, String account){
        Map<String,Object> params=new HashMap<String,Object>();
        StringBuilder sql=new StringBuilder();
        sql.append("SELECT bill.bill_no as billNo, ");
        sql.append("max(bill.prints) as prints, ");
        sql.append("max(print.prints) as prints2 ");
        sql.append("FROM E_COMPARE_BILL bill ");
        sql.append("inner join e_bill_print print on bill.bill_no=print.bill_no ");
        sql.append("where 1=1 and bill.status <> -1 ");
        if(StringUtils.isNotEmpty(voucherNo)){
            sql.append("AND bill.voucher_no =:voucherNo");
            params.put("voucherNo", voucherNo);
        }
        if(StringUtils.isNotEmpty(account)){
            sql.append("AND bill.acc_no =:account");
            params.put("account", account);
        }
        sql.append("group by bill.bill_no ");
        List<ECompareBillVO> infoList=this.listBySQLAliasToBean(sql.toString(), params, ECompareBillVO.class);
        if(infoList!=null&&infoList.size()>0){
            return infoList.get(0);
        }
        return null;
    }
    /**
     * 更新对账电子回单
     * @param voucherNo
     * @param accNo
     * @param lineNo
     * @param user
     * @return
     */
    public int updateECompareBill(String voucherNo, String accNo, int lineNo, ICurrentUser user){
        StringBuilder hql=new StringBuilder("update ECompareBill set updateBy=:updateBy ");
        Map<String,Object> params = new HashMap<>();
        hql.append(",updateDate=:updateDate ,status = -1 where status <> -1 ");
        params.put("updateBy",user.getLoginName());
        params.put("updateDate", DateUtils.getDate());
        if(StringUtils.isNotEmpty(voucherNo)){
            hql.append(" and voucherNo=:voucherNo ");
            params.put("voucherNo", voucherNo);
        }
        if(StringUtils.isNotEmpty(accNo)){
            hql.append(" AND accNo =:accNo");
            params.put("accNo", accNo);
        }
        if(StringUtils.isNotEmpty(lineNo)){
            hql.append(" AND lineNo =:lineNo");
            params.put("lineNo", lineNo);
        }
        return this.batchExecute(hql.toString(), params);
    }
    /**
     * 删除对账电子回单
     * @param voucherNo
     * @param accNo
     * @param lineNo
     * @return
     */
    public int deleteECompareBill(String voucherNo, String accNo, int lineNo){
        StringBuilder hql=new StringBuilder(" delete ECompareBill  ");
        Map<String,Object> params = new HashMap<>();
        hql.append(" where status <> -1 ");
        if(StringUtils.isNotEmpty(voucherNo)){
            hql.append(" and voucherNo=:voucherNo ");
            params.put("voucherNo", voucherNo);
        }
        if(StringUtils.isNotEmpty(accNo)){
            hql.append(" AND accNo =:accNo");
            params.put("accNo", accNo);
        }
        if(StringUtils.isNotEmpty(lineNo)){
            hql.append(" AND lineNo =:lineNo");
            params.put("lineNo", lineNo);
        }
        return this.batchExecute(hql.toString(), params);
    }

    /**
     * 功能介绍 : 按记账日期区间更新对账电子回单余额<br>
     * <b>日期：</b>2022年4月29日<br>
     * @version 1.0
     * @author chenf
     */
    public void updateCompareBillBalance(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE E_COMPARE_BILL")
                .append(" SET BALANCE =:bal")
                .append(" WHERE E_COMPARE_BILL.VOUCHER_NO =:voucherNo ")
                .append(" AND E_COMPARE_BILL.ACC_NO =:compAcc ")
                .append(" AND E_COMPARE_BILL.LINE_NO =:lineNo ")
                .append(" AND E_COMPARE_BILL.TALLY_DATE >=:tallyStartDate ")
                .append(" AND E_COMPARE_BILL.TALLY_DATE <:tallyEndDate ")
                .append(" AND E_COMPARE_BILL.BALANCE =:balance ");
        createSQLQuery(sql.toString(), params).executeUpdate();
    }

}
