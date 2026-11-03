package com.bytter.finance.domain.lease.dao;
import com.bytter.finance.domain.lease.entity.LeasePay;
import com.bytter.finance.domain.lease.vo.CmsFinancingLeaseInfoVO;
import com.bytter.finance.domain.lease.vo.LeasePayVO;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author mof
 * @version 1.0
 * @Title: Dao
 * @Description: 租赁租金支付表
 * @date 2022-04-15 14:22:49
 */
@Repository
public class LeasePayDao extends BaseDao<LeasePay, String> {
    /**
     * 查询数据集
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, List<String> corpAuthList,
            List<String> leaseList) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select p.id as id, p.status as status, ");
        sql.append(
                " l.id as leaseId,l.lessor as lessor,l.lease_type as leaseType, l.corp_id as corpId,l.lease_no as leaseNo, ");
        sql.append(
                " l.lease_code as leaseCode, l.supplier as supplier, l.pay_code as payCode,l.ori_curid as oriCurid, l.ori_money as oriMoney, ");
        //TODO 补充名称信息
        sql.append(" l.corp_name as corpName,l.ori_cur_name as oriCurName,");
        sql.append(" l.begin_date as beginDate, l.end_date as endDate, l.status as leaseStatus ");
        sql.append(" from CMS_LEASE_CAPITAL_PLAN plan ");
        sql.append(" left join CMS_FINANCING_LEASE_INFO l on plan.lease_id = l.id ");
        sql.append(" left join ( ");
        // 从支付表获取已经还款金额，二期使用
        /*
         * sql.append(" select sum(pay.money) sum_money, pay.lease_plan_id as leasePlanId ");
         * sql.append(" from CMS_LEASE_PAY pay where pay.status > '" + CmsConstant.COMPELETOTAL + "')");
         */
        // 从租金计划明细表获取已还金额
        sql.append(" select sum(del.leaseMonry) sum_money, del.lease_plan_id ");
        sql.append(" from CMS_LEASE_CAPITAL_PLANDEL del where del.valid_sign <> '" + CmsConstant.DELETED + "' ");
        sql.append(" and del.is_repay = '" + CmsConstant.REPAYMENT_SCHEDULE_ISREPAYYES
                + "' )  sumLeaseMonry sumLease on sumLease.lease_plan_id = plan.id ");
        sql.append(" left join CMS_FINANCING_LEASE_INFO l on p.lease_id = l.id ");
        sql.append(" where 1 = 1 and l.status not in('0','11')");
        Map<String, Object> params = new HashMap<>();
        sql.append(" and ").append(SqlUtil.getInSql(corpAuthList, "l.corp_Id", true));
        sql.append(" and ").append(SqlUtil.getInSql(leaseList, "l.id", true));
        return getDynamicBySQL(dataTablesInput, sql.toString(), LeasePayVO.class, "", false, params);
    }
    public List<LeasePayVO> getById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select p.id as id, p.lease_plan_id as leasePlanId, p.lease_plandel_id as leasePlanDelId, ");
        sql.append(" p.online_payment_acc as onlinePaymentAcc, p.online_acc_name as onlineAccName, ");
        sql.append(
                " p.online_bank_name as onlineBankName, p.status as status, p.online_sign as onlineSign, p.purpose as purpose, ");
        sql.append(
                " p.rmk as rmk, p.pay_date as payDate, p.money as money, p.interest as interest, p.lease_money as lease_money, ");
        sql.append(
                " p.create_By as createBy, p.create_Date as createDate, p.update_By as updateBy, p.update_Date as updateDate ");
        sql.append(" from CMS_LEASE_PAY p ");
        sql.append(" where 1 = 1 ");
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotBlank(id)) {
            sql.append(" and p.ID= :id ");
            params.put("id", id);
        }
        return listBySQLAliasToBean(sql.toString(), params, LeasePayVO.class);
    }
    public List<LeasePayVO> getByLeaseId(String leaseId) {
        if (EmptyUtils.isEmpty(leaseId)) {
            return new ArrayList<>();
        }
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select p.id as id, p.lease_id as leaseId, p.lease_plan_id as leasePlanId, p.lease_plandel_id as leasePlanDelId, ");
        sql.append(" p.online_payment_acc as onlinePaymentAcc, p.online_acc_name as onlineAccName, ");
        sql.append(
                " p.online_bank_name as onlineBankName, p.status as status, p.online_sign as onlineSign, p.purpose as purpose, ");
        sql.append(
                " p.rmk as rmk, p.pay_date as payDate, p.money as money, p.interest as interest, p.lease_money as lease_money, ");
        sql.append(
                " p.create_By as createBy, p.create_Date as createDate, p.update_By as updateBy, p.update_Date as updateDate ");
        sql.append(" from CMS_LEASE_PAY p ");
        sql.append(" where 1 = 1 ");
        Map<String, String> params = new HashMap<String, String>();
        if (StringUtils.isNotBlank(leaseId)) {
            sql.append(" and p.lease_id= :leaseId ");
            params.put("leaseId", leaseId);
        }
        List<LeasePayVO> list = listBySQLAliasToBean(sql.toString(), params, LeasePayVO.class);
        return list;
    }
    public LeasePayVO getByLeasePlanDelId(String leasePlanDelId) {
        if (EmptyUtils.isEmpty(leasePlanDelId)) {
            return null;
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" select p.id as id, p.lease_plan_id as leasePlanId, p.lease_plandel_id as leasePlanDelId, ");
        sql.append(" p.online_payment_acc as onlinePaymentAcc, p.online_acc_name as onlineAccName, ");
        sql.append(
                " p.online_bank_name as onlineBankName, p.status as status, p.online_sign as onlineSign, p.purpose as purpose, ");
        sql.append(
                " p.rmk as rmk, p.pay_date as payDate, p.money as money, p.interest as interest, p.lease_money as lease_money, ");
        sql.append(
                " p.create_By as createBy, p.create_Date as createDate, p.update_By as updateBy, p.update_Date as updateDate ");
        sql.append(" from CMS_LEASE_PAY p ");
        sql.append(" where 1 = 1 ");
        Map<String, String> params = new HashMap<String, String>();
        if (StringUtils.isNotBlank(leasePlanDelId)) {
            sql.append(" and p.lease_plan_id= :leasePlanDelId ");
            params.put("leasePlanDelId", leasePlanDelId);
        }
        List<LeasePayVO> list = listBySQLAliasToBean(sql.toString(), params, LeasePayVO.class);
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        return list.get(0);
    }
    /**
     * 功能介绍 : 查询还款金额<br>
     *
     * @param leasePlanId
     *            发放编号
     * @param passageSign
     *            在途标识 1查询在途金额 2已还 其他为 无状态查询
     */
    public BigDecimal getRepayMoneyBySendBillCode(String leasePlanId, String passageSign) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder("select BT_NVL_NUM(sum(money),0) SUMMONEY from cms_lease_pay where 1=1 ");
        if ("1".equals(passageSign)) {// 还款在途
            sql.append(" and status >= :status1");
            sql.append(" and status < :status");
            params.put("status1", CmsConstant.BACK);
            params.put("status", CmsConstant.COMPELETOTAL);
        } else if ("2".equals(passageSign)) {// 已还金额
            sql.append(" and status >= :status ");
            params.put("status", CmsConstant.COMPELETOTAL);
        } else {//
            sql.append(" and status >= :status1");
            params.put("status1", CmsConstant.BACK);
        }
        sql.append(" and LEASE_PLAN_ID =:leasePlanId ");
        params.put("leasePlanId", leasePlanId);
        Map rs = getBySQL(sql.toString(), params);
        if (rs == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal count = new BigDecimal(rs.get("SUMMONEY").toString());
        return count;
    }
    public DataTablesOutput dataTableLeasePayApprove(DataTablesInput dataTablesInput, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select p.id as id, p.lease_plan_id as leasePlanId, p.lease_plandel_id as leasePlanDelId, ");
        sql.append(" p.online_payment_acc as onlinePaymentAcc, p.online_acc_name as onlineAccName, ");
        sql.append(
                " p.online_bank_name as onlineBankName, p.status as status, p.online_sign as onlineSign, p.purpose as purpose, ");
        sql.append(
                " p.rmk as rmk, p.pay_date as payDate, p.money as money, p.interest as interest, p.lease_money as lease_money, ");
        sql.append(
                " p.create_By as createBy, p.create_Date as createDate, p.update_By as updateBy, p.update_Date as updateDate, ");
        sql.append(
                " l.id as leaseId,l.lessor as lessor,l.lease_type as leaseType, l.corp_id as corpId,l.lease_no as leaseNo, ");
        //TODO 补充名称信息
        sql.append(" l.corp_name as corpName,l.ori_cur_name as oriCurName,l.rmb_money rmbMoney,");
        sql.append(
                " l.lease_code as leaseCode, l.supplier as supplier, l.pay_code as payCode, l.ori_curid as oriCurid, l.ori_money as oriMoney, ");
        sql.append(" l.begin_date as beginDate, l.end_date as endDate, l.status as leaseStatus ");
        sql.append(" from CMS_LEASE_PAY p ");
        sql.append(" left join CMS_FINANCING_LEASE_INFO l on p.lease_id = l.id ");
        sql.append(" where 1 = 1");
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sql.append(" and ").append(getSQLIn(ids, 900, "p.id"));
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), LeasePayVO.class, null, false);
    }
}
