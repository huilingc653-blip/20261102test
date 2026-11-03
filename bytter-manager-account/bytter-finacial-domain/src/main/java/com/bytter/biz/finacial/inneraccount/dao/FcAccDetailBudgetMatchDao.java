package com.bytter.biz.finacial.inneraccount.dao;
import com.bytter.biz.finacial.api.vo.FcAccDetailBudgetMatchVO;
import com.bytter.biz.bdlink.api.constant.BisAccBalConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
/**
 * @author wangzx
 * @version 1.0
 * @Title: Dao
 * @Description: 内部流水计划查询
 * @date 2018-03-23
 */
@Repository
public class FcAccDetailBudgetMatchDao extends BaseDao<FcAccDetailBudgetMatchVO, String> {
    //	@SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(getSql());
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        String fbsStatus = (String) dataTablesInput.getFormdata().get("eqs_fbsStatus");
        HashMap<String, Object> params = new HashMap<>();
        if (StringUtils.isNotBlank(fbsStatus)) {
            if (BisAccBalConstant.FBS_STATUS_N.equals(fbsStatus)) {
                sql.append(" and (f.fbsStatus is null or f.fbsStatus = '0') ");
            } else {
                sql.append(" and f.fbsStatus =:fbsStatus");
                params.put("fbsStatus", fbsStatus);
            }
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), FcAccDetailBudgetMatchVO.class, "f", false, params);
    }
    /**
     * 根据id查询流水明细
     *
     * @param id
     * @return
     */
    public FcAccDetailBudgetMatchVO getInfoById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(getSql());
        sql.append(" and f.id =:id");
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, FcAccDetailBudgetMatchVO.class);
    }
    /**
     * 公共查询
     *
     * @return
     */
    public String getSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("select f.id,f.corpId,f.tallyDate,f.blnctrAcc,f.accName,f.oppAccNo,f.oppAccName,f.curId,f.debitMoney,f.creditMoney,f.cdSign,f.voucherNo,f.orderNo,f.fbsStatus,f.dayCloseSign,f.summaryContent ");///暂缺对方账户和对方账户名称
        sql.append(", (case when f.fbsStatus in (1,2) then '1' else '0' end) checkboxHide from (");///暂缺对方账户和对方账户名称
        sql.append(" select b.id as id,c.corp_id as corpId,a.tally_date as tallyDate, c.blnctr_acc as blnctrAcc,c.acc_name as accName, e.blnctr_acc as oppAccNo,e.acc_name as oppAccName,b.cur_code as curId, b.debit_money as debitMoney,b.credit_money as creditMoney,");
        sql.append(" case when b.debit_money <> 0 then '1' else '0' end as cdSign,a.voucher_no as voucherNo,a.order_no as orderNo, b.fbs_status as fbsStatus,'1' as dayCloseSign,b.summary_content as summaryContent");
        sql.append(" from fc_accbook a, fc_accbook_dtl b, fc_acc c, fc_accbook_dtl d, fc_acc e ");
        sql.append(" where a.id = b.book_id and a.id = d.book_id and b.book_id = d.book_id and b.account <> d.account and b.account = c.blnctr_acc and d.account = e.blnctr_acc ");
        sql.append(" and a.voucher_state = 'N' and a.inner_sign = '1' and (a.bus_ori_type = '10' or (a.bus_ori_type = '30' and b.credit_money <> 0 and c.acc_type = '01'))");//日结前
        sql.append(" union all ");
        sql.append(" select b.id as id, c.corp_id as corpId, a.tally_date as tallyDate, c.blnctr_acc as blnctrAcc, c.acc_name as accName, e.blnctr_acc as oppAccNo,e.acc_name as oppAccName, b.cur_code as curId, b.debit_money as debitMoney, b.credit_money as creditMoney,");
        sql.append(" case when b.debit_money <> 0 then '1' else '0' end as cdSign, a.voucher_no as voucherNo, a.order_no as orderNo, b.fbs_status as fbsStatus,'2' as dayCloseSign,b.summary_content as summaryContent");
        sql.append(" from fc_accbook_blned a, fc_accbook_blned_dtl b, fc_acc c, fc_accbook_blned_dtl d, fc_acc e ");
        sql.append(" where a.id = b.book_id and a.id = d.book_id and b.book_id = d.book_id and b.account <> d.account and b.account = c.blnctr_acc and d.account = e.blnctr_acc ");
        sql.append(" and a.voucher_state = 'N' and a.inner_sign = '1' and (a.bus_ori_type = '10' or (a.bus_ori_type = '30' and b.credit_money <> 0 and c.acc_type = '01'))) f " );//日结后
        sql.append(" where 1=1 ");
        return sql.toString();
    }
}
