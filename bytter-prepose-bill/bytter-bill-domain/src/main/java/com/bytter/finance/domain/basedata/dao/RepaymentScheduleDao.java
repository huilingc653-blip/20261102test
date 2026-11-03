package com.bytter.finance.domain.basedata.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.basedata.entity.CmsRepaymentSchedule;
import com.bytter.finance.domain.basedata.entity.RepaymentSchedule;
import com.bytter.finance.domain.basedata.vo.RepaymentScheduleVO;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: 还款计划表
 * @author songkk
 * @date 2017-08-01 09:58:44
 * @version 1.0
 *
 */
@Repository
public class RepaymentScheduleDao extends BaseDao<RepaymentSchedule, String> {
    /**
     * <b>功能介绍：</b>获得贷款发放次数标识 send_time<br>
     * <b>日期：</b>2017年8月1日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings("rawtypes")
    public int getSendTime(String billCode) {
        int send_time = 0;
        String sql = "select send_time from cms_loan_info where bill_code='" + billCode + "'";
        List list = createSQLQuery(sql).list();
        if (!EmptyUtils.isEmptyList(list)) {
            send_time = Integer.parseInt(String.valueOf(list.get(0)));
        }
        return send_time;
    }
    /**
     * <b>功能介绍：</b>根据单号获取还款计划<br>
     * <b>日期：</b>2017年8月1日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public List<RepaymentScheduleVO> getRepayMentScheduleByBillCode(String billCode) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(
                " select sub.id as id,rs.bill_no as billCode,sub.repay_cycel as repayCycel,sub.predice_repay_date as prediceRepayDate, ");
        sql.append(
                " sub.repayment_money as repaymentMoney,sub.repayment_ist as repaymentIst,sub.current_repay_money as currentRepayMoney, ");
        sql.append(
                " sub.reality_repay_date as realityRepayDate,sub.reality_repay_money as realityRepayMoney,sub.surplus_money as surplusMoney, ");
        sql.append(
                " sub.is_repay as isRepay,sub.remark as remark,sub.repayment_begin_date as repaymentBeginDate,sub.internal_sign as internalSign, ");
        sql.append("(select count(1) from (");
        sql.append(
                "select repayment_schedule_id from cms_loan_repay where status != " + CmsConstant.DELETED + "  union ");
        sql.append("select repayment_schedule_id from cms_precative_loan_repay where status != " + CmsConstant.DELETED
                + " union ");
        sql.append("select repayment_schedule_id from cms_provide_loan_repay where status != " + CmsConstant.DELETED
                + " ) dtl where dtl.repayment_schedule_id =sub.id ");
        sql.append(") as used ");
        sql.append(
                " from cms_repayment_scheduledetailed sub left join cms_repayment_schedule rs on sub.relation_id = rs.id ");
        sql.append(" where (sub.valid_sign is null or sub.valid_sign <> '" + CmsConstant.DELETED
                + "') and rs.bill_no=:billCode ");
        sql.append(" order by isRepay desc,prediceRepayDate asc ");
        params.put("billCode", billCode);
        return listBySQLAliasToBean(sql.toString(), params, RepaymentScheduleVO.class);
    }
    /**
     * <b>功能介绍：</b>根据单号获取还款计划<br>
     * <b>日期：</b>2017年8月1日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public List<RepaymentScheduleVO> getRepayScheduleByBillCode(String billCode) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(
                " select sub.id as id,rs.bill_no as billCode,sub.repay_cycel as repayCycel,sub.predice_repay_date as prediceRepayDate, ");
        sql.append(
                " sub.repayment_money as repaymentMoney,sub.repayment_ist as repaymentIst,sub.current_repay_money as currentRepayMoney, ");
        sql.append(
                " sub.reality_repay_date as realityRepayDate,sub.reality_repay_money as realityRepayMoney,sub.surplus_money as surplusMoney, ");
        sql.append(
                " sub.is_repay as isRepay,sub.remark as remark,sub.repayment_begin_date as repaymentBeginDate,sub.internal_sign as internalSign, ");
        sql.append("(select count(1) from (");
        sql.append(
                "select repayment_schedule_id from cms_loan_repay where status != " + CmsConstant.DELETED + "  union ");
        sql.append("select repayment_schedule_id from cms_precative_loan_repay where status != " + CmsConstant.DELETED
                + " union ");
        sql.append("select repayment_schedule_id from cms_provide_loan_repay where status != " + CmsConstant.DELETED
                + " ) dtl where dtl.repayment_schedule_id =sub.id ");
        sql.append(") as used ");
        sql.append(
                " from cms_repayment_scheduledetailed sub left join cms_repayment_schedule rs on sub.relation_id = rs.id ");
        sql.append(" where (sub.valid_sign is null or sub.valid_sign <> '" + CmsConstant.DELETED
                + "') and rs.bill_no=:billCode ");
        sql.append(" and sub.is_Repay='0'");// 未还
        sql.append(" order by prediceRepayDate asc");
        params.put("billCode", billCode);
        return listBySQLAliasToBean(sql.toString(), params, RepaymentScheduleVO.class);
    }
    /**
     * <b>功能介绍：</b>根据单号获取未还款利息<br>
     * <b>日期：</b>2017年8月1日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public List<RepaymentScheduleVO> getsumRepaymentIstByBillCode(String billCode) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select  sub.repayment_ist as repaymentIst");
        sql.append(
                " from cms_repayment_scheduledetailed sub left join cms_repayment_schedule rs on sub.relation_id = rs.id ");
        sql.append(" where (sub.valid_sign is null or sub.valid_sign <> '" + CmsConstant.DELETED
                + "') and rs.bill_no=:billCode and sub.is_repay='0' ");
        sql.append(" order by sub.predice_repay_date asc");
        params.put("billCode", billCode);
        return listBySQLAliasToBean(sql.toString(), params, RepaymentScheduleVO.class);
    }

    /***
     * 通过委托贷款ID查询还款计划信息信息
     */
    @SuppressWarnings("unchecked")
    public CmsRepaymentSchedule findById(String billId) {
        Map<String, Object> params = new HashMap<String, Object>();
        String hql = "from CmsRepaymentSchedule rs where rs.billNo = :billNo and rs.status = :status ";
        params.put("billNo", billId);
        params.put("status", "1");
        List<CmsRepaymentSchedule> list = createQuery(hql, params).list();
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }
    /**
     * 根据id获取还款计划
     *
     * @param id
     * @return
     */
    public List<RepaymentScheduleVO> listRepayMentScheduleById(String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(
                " select sub.id as id,rs.bill_no as billCode,sub.repay_cycel as repayCycel,sub.predice_repay_date as prediceRepayDate, ");
        sql.append(
                " sub.repayment_money as repaymentMoney,sub.repayment_ist as repaymentIst,sub.current_repay_money as currentRepayMoney, ");
        sql.append(
                " sub.reality_repay_date as realityRepayDate,sub.reality_repay_money as realityRepayMoney,sub.surplus_money as surplusMoney, ");
        sql.append(
                " sub.is_repay as isRepay,sub.remark as remark,sub.repayment_begin_date as repaymentBeginDate,sub.internal_sign as internalSign, ");
        sql.append("(select count(1) from (");
        sql.append(
                "select repayment_schedule_id from cms_loan_repay where status != " + CmsConstant.DELETED + "  union ");
        sql.append("select repayment_schedule_id from cms_precative_loan_repay where status != " + CmsConstant.DELETED
                + " union ");
        sql.append("select repayment_schedule_id from cms_provide_loan_repay where status != " + CmsConstant.DELETED
                + " ) dtl where dtl.repayment_schedule_id =sub.id ");
        sql.append(") as used ");
        sql.append(
                " from cms_repayment_scheduledetailed sub left join Cms_Repayment_Schedule rs on sub.relation_id = rs.id ");
        sql.append(" where (sub.valid_sign <> '" + CmsConstant.DELETED + "' or sub.valid_sign is null) and rs.id=:id ");
        sql.append(" order by prediceRepayDate,isRepay asc");
        params.put("id", id);
        return listBySQLAliasToBean(sql.toString(), params, RepaymentScheduleVO.class);
    }
    /**
     * 根据单号删除之前的还款计划子表(修改valid_Sign 为2)
     *
     * @param billCode
     *            单号
     */
    public void deleteRepaymentSchedule(String billCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("isRepay", CmsConstant.REPAYMENT_SCHEDULE_ISREPAYNO);
        params.put("billCode", billCode);
        params.put("validSign", String.valueOf(CmsConstant.DELETED));
        String sql = "update cms_repayment_scheduledetailed set valid_Sign=:validSign where bill_code=:billCode and is_repay=:isRepay ";
        createSQLQuery(sql, params).executeUpdate();
    }
    /**
     * 根据relation_id删除之前的还款计划子表(修改valid_Sign 为2)
     *
     * @param billCode
     *            单号
     */
    public void deleteRepaymentScheduleById(String relationId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("isRepay", CmsConstant.REPAYMENT_SCHEDULE_ISREPAYNO);
        params.put("relationId", relationId);
        params.put("validSign", String.valueOf(CmsConstant.DELETED));
        String hql = "update RepaymentSchedule set valid_Sign=:validSign where relation_Id=:relationId and isRepay=:isRepay";
        this.createQuery(hql, params).executeUpdate();
    }
    /***
     * 根据billCode删除还款计划 -- 物理删除
     *
     * @param billCode
     */
    public void deleteRepaymentByBillCode(String billCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("billCode", billCode);
        String sql1 = "delete from  cms_repayment_scheduledetailed where bill_code=:billCode";
        String sql2 = "delete from  cms_repayment_schedule where bill_no=:billCode";
        createSQLQuery(sql1, params).executeUpdate();
        createSQLQuery(sql2, params).executeUpdate();
    }
    /**
     * <b>功能介绍：</b>更新还款计划的单号和类型<br>
     * <b>日期：</b>2017年8月15日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public void updateRepayMentBill(String id, String cmsType, String billCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder(
                "update cms_repayment_schedule set cms_type = :cmsType,bill_no = :billCode,status = 1 where id = :id");
        StringBuilder sql1 = new StringBuilder(
                "update cms_repayment_scheduledetailed set internal_sign = :cmsType,bill_code = :billCode where relation_id = :id");
        params.put("cmsType", cmsType);
        params.put("billCode", billCode);
        params.put("id", id);
        createSQLQuery(sql.toString(), params).executeUpdate();
        createSQLQuery(sql1.toString(), params).executeUpdate();
    }
    /**
     * 根据单号获取还款计划主表数据
     *
     * @param billCode
     * @return
     */
    public CmsRepaymentSchedule getCmsRepaymentScheduleByBillCode(String billCode) {
        String hql = "from CmsRepaymentSchedule rs where status = 1 and rs.billNo = '" + billCode + "'";
        List<CmsRepaymentSchedule> list = createQuery(hql).list();
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 根据单号获取审批中还款计划主表数据
     *
     * @param billCode
     * @return
     */
    public CmsRepaymentSchedule getApproveScheduleByBillCode(String billCode) {
        String hql = "from CmsRepaymentSchedule rs where status = 11 and rs.billNo = '" + billCode + "'";
        List<CmsRepaymentSchedule> list = createQuery(hql).list();
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }
    public List<RepaymentSchedule> listMatureRepayByLoan(Date date) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select d.*  ");
        sql.append(" from cms_repayment_schedule s");
        sql.append(" left join cms_repayment_scheduledetailed d on s.id = d.relation_id  ");
        sql.append(" inner join cms_loan_send send on send.id = s.bill_no ");
        sql.append(" where d.is_repay <> 1 and d.valid_sign = '1' and send.status = :status ");
        // sql.append(" and (d.repay_sign = :repaySign or d.repay_sign is null) ");
        sql.append(
                " and d.predice_repay_date <= to_date(to_char(:endDate, 'yyyy-MM-dd HH24:MI:SS'),'yyyy-MM-dd HH24:MI:SS')");
        sql.append(" and d.is_repay = 0 ");
        sql.append(" order by d.predice_repay_date ");
        params.put("status", CmsConstant.COMPELETOTAL);
        params.put("repaySign", CommonConstant.SEND_SIGN_NO);
        params.put("endDate", date);
        return listBySQLAliasToBean(sql.toString(), params, RepaymentSchedule.class);
    }
    public List<RepaymentSchedule> listMatureRepayByPrecative(Date date) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select d.* ");
        sql.append(" from cms_repayment_schedule s");
        sql.append(" left join cms_repayment_scheduledetailed d on s.id = d.relation_id  ");
        sql.append(" inner join cms_precative_loan_send send on send.id = s.bill_no ");
        sql.append(" where d.is_repay <> 1 and d.valid_sign = '1' and send.status = :status ");
        // sql.append(" and (d.repay_sign = :repaySign or d.repay_sign is null) ");
        sql.append(
                " and d.predice_repay_date <= to_date(to_char(:endDate, 'yyyy-MM-dd HH24:MI:SS'),'yyyy-MM-dd HH24:MI:SS')");
        sql.append(" and d.is_repay = 0 ");
        sql.append(" order by d.predice_repay_date ");
        params.put("status", CmsConstant.COMPELETOTAL);
        params.put("repaySign", CommonConstant.SEND_SIGN_NO);
        params.put("endDate", date);
        return listBySQLAliasToBean(sql.toString(), params, RepaymentSchedule.class);
    }
    /**
     * 查询所有还款计划
     *
     * @param billCode
     * @return
     */
    public List<RepaymentSchedule> listRepaymentScheduleByBillNo(String billCode) {
        StringBuilder hql = new StringBuilder();
        hql.append(" from RepaymentSchedule where billCode = ?0 and valid_Sign = '1' order by prediceRepayDate");
        return listByHQL(hql.toString(), billCode);
    }

    /**
     * 根据单据号获取未还款的计划明细
     *
     * @param sendId 单据号
     * @param status 是否已还标识
     * @return 明细集合
     */
    public List<RepaymentScheduleVO> getRepaymentScheduleDtlByBillCode(String sendId, String status) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.*,b.loan_rate as loanRate from CMS_REPAYMENT_SCHEDULEDETAILED a left join CMS_REPAYMENT_SCHEDULE b on a.relation_id = b.id where a.bill_code = :sendId and a.is_repay = :status and (a.VALID_SIGN!=-2 or a.VALID_SIGN is null)");
        sql.append(" order by PREDICE_REPAY_DATE asc,SURPLUS_MONEY desc");
        params.put("sendId", sendId);
        params.put("status", status);
        return listBySQLAliasToBean(sql.toString(), params, RepaymentScheduleVO.class);
    }

    public void deleteByBillCode(String billCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer();
        sql.append("delete from CMS_REPAYMENT_SCHEDULEDETAILED  where bill_code = :billCode and is_repay = '0' and (valid_sign = '1' or valid_sign is null)");
        params.put("billCode", billCode);
        createSQLQuery(sql.toString(), params).executeUpdate();
    }

    /**
     * 根据id获取审批中还款计划
     *
     * @param id
     * @return
     */
    public List<RepaymentScheduleVO> listApproveRepayMentScheduleById(String id){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(
                " select sub.id as id,rs.bill_no as billCode,sub.repay_cycel as repayCycel,sub.predice_repay_date as prediceRepayDate, ");
        sql.append(
                " sub.repayment_money as repaymentMoney,sub.repayment_ist as repaymentIst,sub.current_repay_money as currentRepayMoney, ");
        sql.append(
                " sub.reality_repay_date as realityRepayDate,sub.reality_repay_money as realityRepayMoney,sub.surplus_money as surplusMoney, ");
        sql.append(
                " sub.is_repay as isRepay,sub.remark as remark,sub.repayment_begin_date as repaymentBeginDate,sub.internal_sign as internalSign, ");
        sql.append("(select count(1) from (");
        sql.append(
                "select repayment_schedule_id from cms_loan_repay where status != " + CmsConstant.DELETED + "  union ");
        sql.append("select repayment_schedule_id from cms_precative_loan_repay where status != " + CmsConstant.DELETED
                + " union ");
        sql.append("select repayment_schedule_id from cms_provide_loan_repay where status != " + CmsConstant.DELETED
                + " ) dtl where dtl.repayment_schedule_id =sub.id ");
        sql.append(") as used ");
        sql.append(
                " from cms_repayment_scheduledetailed sub left join Cms_Repayment_Schedule rs on sub.relation_id = rs.id ");
        sql.append(" where sub.valid_sign = '" + CmsConstant.COMMITAPPROVE + "'  and rs.id=:id ");
        sql.append(" order by prediceRepayDate,isRepay asc");
        params.put("id", id);
        return listBySQLAliasToBean(sql.toString(), params, RepaymentScheduleVO.class);
    }
}
