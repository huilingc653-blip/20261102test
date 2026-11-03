package com.bytter.biz.finacial.interest.dao;
import com.bytter.biz.finacial.interest.entity.FcInterestDtl;
import com.bytter.biz.finacial.interest.vo.FcInterestDtlVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.biz.finacial.api.dto.FcInterestDtlDTO;
import org.springframework.stereotype.Repository;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: 利息管理--利息明细查询表
 * @Description: FC_INTEREST_DTL
 * @date 2017-06-23 14:34:54
 */
@Repository
public class FcInterestDtlDao extends BaseDao<FcInterestDtl, String> {
    public DataTablesOutput listAccount(DataTablesInput input, List<String> accounts, String isPool) {
        /*StringBuilder sql = new StringBuilder();
        sql.append("select dtl.account,acc.acc_name as accName, c.cur_name,dtl.cur_code,dtl.interest_start_date,");
        sql.append("dtl.bln_inst_date,dtl.days,dtl.cumulation,dtl.interest_rate,dtl.interest,dtl.interest_sign,dtl.interest_time");
        sql.append(" from fc_interest_dtl dtl ");
        sql.append(" inner join fc_acc acc on acc.blnctr_acc = dtl.account");
        sql.append(" left join  bt_currency c on dtl.cur_code = c.cur_code");
        sql.append(" where 1=1 ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "acc.corp_id"));
        if ("1".equals(isPool)) {
            sql.append(" and acc.pool_sign = '1' ");
        } else {
            sql.append(" and acc.pool_sign != '1' ");
        }*/
        StringBuilder sql = new StringBuilder();
        sql.append(" select dtl.account,dtl.cur_code as curCode,dtl.interest_start_date as interestStartDate,");
        sql.append(" dtl.bln_inst_date as blnInstDate, dtl.days,dtl.cumulation,dtl.interest_rate as interestRate,");
        sql.append(" dtl.interest,dtl.interest_sign as interestSign,dtl.interest_time as interestTime");
        sql.append(" from fc_interest_dtl dtl ");
        sql.append(" where 1=1 ");
        sql.append(" and ").append(getSQLIn(accounts, 900, "dtl.account"));
        DataTablesOutput dynamicBySQL = getDynamicBySQL(input, sql.toString(), FcInterestDtlVo.class, "", false);
        return dynamicBySQL;
    }
    /**
     * <b>功能介绍：</b>根据流水号或者账号查询利息明细<br>
     * <b>日期：</b>2017年8月31日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param account
     * @param vourcherNo
     * @return
     * @version 1.0
     */
    public List<FcInterestDtlVo> getFcInterestDtlDTOByVourcherNo(String account, String vourcherNo) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append("SELECT a.id,a.predraw_dtl_id,a.interest_time,a.account,a.cur_code,");
        sql.append("a.interest_type,a.interest_start_date,a.bln_inst_date,a.days,a.cumulation,a.interest_rate,a.repay_money,");
        sql.append("a.interest,a.interest_sign,a.rmk,a.generate_mode,a.order_no,a.e_receipt_no,");
        sql.append("a.bus_status,a.operator_code,a.tally_date,a.checker_code,a.check_date,a.create_time,");
        sql.append("a.create_by,a.update_time,a.update_by,a.voucher_no FROM FC_INTEREST_DTL a ");
        sql.append("WHERE 1=1 ");
        if (EmptyUtils.isNotEmpty(vourcherNo)) {
            sql.append("AND a.Voucher_No = :voucherNo ");
            params.put("voucherNo", vourcherNo);
        }
        if (EmptyUtils.isNotEmpty(account)) {
            sql.append("and a.Account = :account ");
            params.put("account", account);
        }
        sql.append("order by a.INTEREST_START_DATE, a.BLN_INST_DATE");
        return listBySQLAliasToBean(sql.toString(), params, FcInterestDtlVo.class);
    }
    /**
     * <b>功能介绍：</b>根据流水号账号回单号查询利息明细<br>
     * <b>日期：</b>2017年11月17日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param account
     * @param orderNo
     * @param eReceiptNo
     * @return
     * @version 1.0
     */
    public List<FcInterestDtlVo> getFcInterestDtlDTOByOrderNo(String account, String orderNo, String eReceiptNo) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append("SELECT a.id,a.predraw_dtl_id,a.interest_time,a.account,a.cur_code,");
        sql.append("a.interest_type,a.interest_start_date,a.bln_inst_date,a.days,a.cumulation,a.interest_rate,a.repay_money,");
        sql.append("a.interest,a.interest_sign,a.rmk,a.generate_mode,a.order_no,a.e_receipt_no,");
        sql.append("a.bus_status,a.operator_code,a.tally_date,a.checker_code,a.check_date,a.create_time,");
        sql.append("a.create_by,a.update_time,a.update_by,a.voucher_no FROM FC_INTEREST_DTL a ");
        sql.append("WHERE 1=1 ");
        if (EmptyUtils.isNotEmpty(orderNo)) {
            sql.append("AND a.order_no = :orderNo ");
            params.put("orderNo", orderNo);
        }
        if (EmptyUtils.isNotEmpty(account)) {
            sql.append("and a.Account = :account ");
            params.put("account", account);
        }
        if (EmptyUtils.isNotEmpty(eReceiptNo)) {
            sql.append("and a.e_receipt_no = :eReceiptNo ");
            params.put("eReceiptNo", eReceiptNo);
        }
        sql.append("order by a.INTEREST_START_DATE, a.BLN_INST_DATE");
        return listBySQLAliasToBean(sql.toString(), params, FcInterestDtlVo.class);
    }
    public List<FcInterestDtlDTO> getFcInterestDtlByAcc(String account, String isPool) {
        /*StringBuilder sql = new StringBuilder();
        sql.append(" select dtl.account,acc.acc_name as accName, c.cur_name,dtl.cur_code,dtl.interest_start_date, ");
        sql.append(" dtl.bln_inst_date,dtl.days,dtl.cumulation,dtl.interest_rate,dtl.interest,dtl.interest_sign,dtl.interest_time ");
        sql.append(" from fc_interest_dtl dtl ");
        sql.append(" inner join fc_acc acc on acc.blnctr_acc = dtl.account ");
        sql.append(" left join bt_currency c on dtl.cur_code = c.cur_code ");
        sql.append(" where 1=1 ");
        Map<String, Object> params = new HashMap<String, Object>();
        if ("1".equals(isPool)) {
            sql.append(" and acc.pool_sign = '1' ");
        } else {
            sql.append(" and acc.pool_sign != '1' ");
        }
        if (EmptyUtils.isNotEmpty(account)) {
            sql.append(" and acc.blnctr_acc = :account ");
            params.put("account", account);
        }*/
        StringBuilder sql = new StringBuilder();
        sql.append(" select dtl.account,dtl.cur_code,dtl.interest_start_date, ");
        sql.append(" dtl.bln_inst_date,dtl.days,dtl.cumulation,dtl.interest_rate,dtl.interest,dtl.interest_sign,dtl.interest_time ");
        sql.append(" from fc_interest_dtl dtl ");
        sql.append(" where 1=1 ");
        Map<String, Object> params = new HashMap<String, Object>();
        if (EmptyUtils.isNotEmpty(account)) {
            sql.append(" and dtl.account = :account ");
            params.put("account", account);
        }
        return listBySQLAliasToBean(sql.toString(), params, FcInterestDtlDTO.class);
    }
}
