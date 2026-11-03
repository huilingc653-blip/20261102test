package com.bytter.biz.ttpay.dao;
import com.bytter.biz.ttpay.entity.TpPayment;
import com.bytter.biz.ttpay.vo.TpPaymentVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author mof
 * @version 1.0
 * @Title: DAO
 * @Description: 第三方支付查询信息Dao
 * @date 2022-05-16
 */
@Repository
public class TpPaymentQueryDao extends BaseDao<TpPayment, String> {
    /**
     * 付款通知表通用查@param sqlWhere
     *
     * @return
     */
    private String getCommonSql() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select n.id as id, n.bill_no as billNo,n.corp_id as corpId,n.bus_title as busTitle,n.purpose as purpose,n.add_word as addWord,");
        sql.append(" n.payer_acc as payerAcc,n.payer_acc_name as payerAccName,n.pay_type as payType,sp.shoptype as payTypeName,n.pay_money as payMoney,n.bus_Type as busType,t.bus_type_name as busTypeName,");
        sql.append(" n.payee_acc as payeeAcc,n.payee_mode as payeeMode,n.payee_acc_name as payeeAccName,n.wish_payday as wishPayDay,n.status,n.rmk,n.cur_id as curId,n.IS_FORINDIVIDUAL as isforindividual,");
        sql.append(" n.attachment_id as attachmentId,n.create_date as createDate,n.create_by as createBy,n.update_date as updateDate,n.update_by as updateBy,n.create_username as createUserName,");
        sql.append(" n.voucher_stat as voucherStat,n.trans_time as transTime,n.TRAND_NO as trandNo,n.BANK_ORDER_NO as bankOrderNo,n.BANK_PAY_NO as bankPayNo,n.RETURN_MSG as returnMsg,n.return_time as returnTime ");
        sql.append(" from tp_payment n");
        sql.append(" left join tp_shoptype sp on n.pay_type = sp.code");
        sql.append(" left join Nis_Bus_Type t on n.bus_Type = t.id");
        //sql.append(" left join sys_corp sc on n.corp_id = sc.id");
        sql.append(" where 1=1");
        return sql.toString();
    }
    /**
     * 查询第三方支付信息
     *
     * @param dataTablesInput 查询条件
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, List<String> corpAuthList) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" and ").append(SqlUtil.getInSqlByParams(corpAuthList, "t.CORP_ID", params));
        return getDynamicBySQL(dataTablesInput, sql.toString(), TpPaymentVO.class, "", false, params);
    }
    /**
     * 根据付款ID获取付款信息
     *
     * @param id
     * @return
     */
    public TpPaymentVO getByIdUnion(String id) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and  n.id=:id");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, TpPaymentVO.class);
    }
    /**
     * 查询付款单
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesQuery(DataTablesInput dataTablesInput, String joinSql, String isMergeAndSplit) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), TpPaymentVO.class, "n", false);
    }
}
