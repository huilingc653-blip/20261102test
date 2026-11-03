package com.bytter.finance.domain.provide.dao;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.domain.basedata.entity.RepaymentSchedule;
import com.bytter.finance.domain.provide.entity.CmsProvideInterest;
import com.bytter.finance.domain.provide.vo.*;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.IFcInstrateAdjustService;
import com.bytter.system.api.IRateMaintenceService;
import com.bytter.system.api.dto.InstRateBeanDto;
import com.bytter.system.api.dto.TbRateMaintenceDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Repository;
import java.util.*;
/**
 * @Title: Dao
 * @Description: 借款利息预计明细
 * @author onlineGenerator
 * @date 2017-08-24 15:50:26
 * @version 1.0
 *
 */
@Repository
public class CmsProvideInterestDao extends BaseDao<CmsProvideInterest, String> {
    @DubboReference(check = false)
    private IRateMaintenceService rateService;
    /**
     * 根据发放单号查询利息预计明细
     *
     * @return
     */
    public List<CmsProvideInterest> queryInterestList(String sendBillCode, String geRepayMoney, String leRepayMoney,
                                                      String geRepayAccrual, String legRepayAccrual, String gegRepaymentDate, String legRepaymentDate) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer();
        sql.append("select i.id as id,i.repay_date as repayDate,i.interest_start_date as interestStartDate,i.bln_inst_date as blnInstDate, ");
        sql.append(" i.days as days,i.interest_rate as interestRate,i.loan_money as loanMoney,i.repay_money as repayMoney,i.real_interest as realInterest, ");
        sql.append(" i.left_money as leftMoney,i.interest as interest,i.pay_interest as payInterest,i.total_interest as totalInterest,i.status as status,i.sign_repay_date as signRepayDate, i.REPAYMENT_SCHEDULE_ID repaymentScheduleId  ");
        sql.append(" from cms_provide_interest i ");
        sql.append(" where 1=1 ");
        sql.append(" and i.bill_code = :billCode ");
        sql.append(" and i.interest_sign = '1' ");//测算
        if (StringUtils.isNotBlank(geRepayMoney)) {
            sql.append(" and i.repay_money >= " + geRepayMoney);
        }
        if (StringUtils.isNotBlank(leRepayMoney)) {
            sql.append(" and i.repay_money <= " + leRepayMoney);
        }
        if (StringUtils.isNotBlank(geRepayAccrual)) {
            sql.append(" and i.pay_interest  >= " + geRepayAccrual);
        }
        if (StringUtils.isNotBlank(legRepayAccrual)) {
            sql.append(" and i.pay_interest  <= " + legRepayAccrual);
        }
        if (StringUtils.isNotBlank(gegRepaymentDate)) {
            sql.append(" and i.sign_repay_date >= to_date('" + gegRepaymentDate + "', 'yyyy-MM-dd')");
        }
        if (StringUtils.isNotBlank(legRepaymentDate)) {
            sql.append(" and i.sign_repay_date <= to_date('" + legRepaymentDate + "', 'yyyy-MM-dd')");
        }

        params.put("billCode", sendBillCode);

        sql.append(" order by i.repay_date,i.repay_money ");
        return listBySQLAliasToBean(sql.toString(), params, CmsProvideInterest.class);
    }
    /**
     * 根据发放单号查询利息预计明细
     *
     * @return
     */
    public List<CmsProvideInterest> ListInterestList(String sendBillCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer();
        sql.append(
                "select i.id as id,i.repay_date as repayDate,i.interest_start_date as interestStartDate,i.bln_inst_date as blnInstDate, ");
        sql.append(
                " i.days as days,i.interest_rate as interestRate,i.loan_money as loanMoney,i.repay_money as repayMoney,i.real_interest as realInterest, ");
        sql.append(
                " i.left_money as leftMoney,i.interest as interest,i.pay_interest as payInterest,i.total_interest as totalInterest,i.status as status,i.sign_repay_date as signRepayDate ");
        sql.append(" from cms_provide_interest i ");
        sql.append(" where 1=1 ");
        sql.append(" and i.bill_code = :billCode ");
        sql.append(" and i.status = 0 ");// 未还
        sql.append(" and i.interest_sign = '1'");
        params.put("billCode", sendBillCode);
        sql.append("order by i.repay_date,i.repay_money ");
        return listBySQLAliasToBean(sql.toString(), params, CmsProvideInterest.class);
    }
    /**
     * 根据还款计划ID查询这个还款日期标记内的全部利息预计明细ID
     *
     * @return
     */
    public List<String> getRepayDateInterestsSignById(String sendCode, Date signRepayDate) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer();
        sql.append("select id as ID from cms_provide_interest i where i.bill_code = :billCode ");
        sql.append(" and i.interest_sign = '1' ");// 测算
        sql.append(" and sign_repay_date = BT_TO_DATE(:signRepayDate,'yyyy-MM-dd') ");
        params.put("billCode", sendCode);
        params.put("signRepayDate", DateUtils.format(signRepayDate, "yyyy-MM-dd"));
        List<Map<String, Object>> list = listBySQL(sql.toString(), params);
        List<String> result = new ArrayList<String>();
        for (Map<String, Object> map : list) {
            result.add(map.get("ID").toString());
        }
        return result;
    }
    /**
     * 根据还款计划ID查询这个还款日期标记内的全部利息预计明细ID
     *
     * @return
     */
    public List<CmsProvideInterest> getRepayDateInterestsBySignAndSendCode(String sendCode, Date signRepayDate) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer hql = new StringBuffer();
        hql.append(" from CmsProvideInterest i ");
        hql.append(" where 1=1 ");
        hql.append(" and i.billCode = :bill_code ");
        hql.append(" and i.signRepayDate = BT_TO_DATE(:signRepayDate,'yyyy-MM-dd') ");
        hql.append(" and i.interestSign = '1' ");// 测算
        hql.append(" and i.status = '1'");// 已还
        params.put("bill_code", sendCode);
        params.put("signRepayDate", DateUtils.format(signRepayDate, "yyyy-MM-dd"));
        return listByHQL(hql.toString(), params);
    }
    /**
     * 根据还款计划ID查询利息预计明细
     *
     * @return
     */
    public List<CmsProvideInterest> queryInterestListByScheduleId(String repaymentScheduleId) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer hql = new StringBuffer();
        hql.append(" from CmsProvideInterest i ");
        hql.append(" where 1=1 ");
        hql.append(" and i.repaymentScheduleId = :repaymentScheduleId ");
        hql.append(" and i.interestSign = '1' ");// 测算
        hql.append(" and i.status = '1'");// 已还
        params.put("repaymentScheduleId", repaymentScheduleId);
        return listByHQL(hql.toString(), params);
    }
    /**
     * <b>功能：</b>删除测算利息<br>
     * 不删除 已使用的还款计划 所生成的 利息明细
     *
     * @param sendBillCode
     *            发放表billCode
     */
    public void deleteProvideInterest(String sendBillCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer();
        sql.append("delete from cms_provide_interest  where 1 = 1 ");
        sql.append(" and bill_code = :billCode ");
        params.put("billCode", sendBillCode);
        // 不删除 已使用的还款计划 所生成的 利息明细
        sql.append(" and status = 0 ");
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    /**
     * <br>
     * <b>功能：</b>获取未使用的借款还款计划<br>
     *
     * @param sendBillCode
     * @return
     */
    public List<RepaymentSchedule> getRepayMentScheduleByBillCode(String sendId) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select schedule.* from cms_repayment_scheduledetailed schedule ");
        sql.append("inner join cms_repayment_schedule crs on crs.id = schedule.relation_id ");
        sql.append("where (schedule.valid_sign <> '" + CmsConstant.DELETED
                + "' or schedule.valid_sign is null) and crs.bill_no=:sendId ");
        // sql.append(" and schedule.is_repay = '0' ");
        params.put("sendId", sendId);
        sql.append(" ORDER BY predice_repay_date ");
        return listBySQLAliasToBean(sql.toString(), params, RepaymentSchedule.class);
    }
    /**
     * <br>
     * <b>功能：</b>获取未使用的借款还款计划<br>
     *
     * @param sendId
     * @param repayCycles
     * @return
     */
    public List<RepaymentSchedule> getRepayMentScheduleByBillCode(String sendId, Set<String> repayCycles) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select schedule.* from cms_repayment_scheduledetailed schedule ");
        sql.append("inner join cms_repayment_schedule crs on crs.id = schedule.relation_id ");
        sql.append("where (schedule.valid_sign <> '" + CmsConstant.DELETED
                + "' or schedule.valid_sign is null) and crs.bill_no=:sendId ");
        if (null != repayCycles && !repayCycles.isEmpty()) {
            sql.append(" and exists (select 1 from cms_provide_loan_send c where c.id = crs.bill_no ");
            String hqlSelect = "";
            for (String repayCycle : repayCycles) {
                if (EmptyUtils.isEmpty(repayCycle)) {
                    continue;
                }
                String[] cycleArr = repayCycle.split(",");
                if (null != cycleArr && cycleArr.length >= 3) {
                    hqlSelect += " (c.repay_cycle = '" + cycleArr[0] + "' and c.cur_code = '" + cycleArr[1] + "' "
                            + " and c.reference_rate_type = '" + cycleArr[2] + "' ) OR";
                }
            }
            if (EmptyUtils.isNotEmpty(hqlSelect)) {
                hqlSelect = hqlSelect.substring(0, hqlSelect.length() - 2);
                sql.append(" and ( " + hqlSelect + " )  and c.status >= 1 ) ");
            }
        }
        // sql.append(" and schedule.is_repay = '0' ");
        params.put("sendId", sendId);
        sql.append(" ORDER BY predice_repay_date ");
        return listBySQLAliasToBean(sql.toString(), params, RepaymentSchedule.class);
    }
    public List<InstRateBean> getLoanRateMaintence(Set<String> repayCycles) {
        List<InstRateBeanDto> loanRateMaintence = rateService.getLoanRateMaintence(repayCycles);
        return BeanUtils.copyListProperties(loanRateMaintence,InstRateBean.class);
    }
    /**
     * <br>
     * <b>功能：</b>获取计息方式调整记录<br>
     *
     * @param billCode
     * @param repayCycles
     * @return
     */
    public List<InterestWayBean> getInterestWaysByBillCode(String sendId) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(
                " SELECT a.interest_way as interestWay,a.send_code as billCode,a.expiry_day as interestDay,a.active_date as effectDate,");
        sql.append(
                " INTEREST_FREQUENCY_TYPE as interestFrequencyType,INTEREST_FREQUENCY_NEXT as interestFrequencyNext,");
        sql.append(
                " INTEREST_FREQUENCY as interestFrequency,INTEREST_FREQUENCY_NUM as interestFrequencyNum,'02' as isStanderd,");
        sql.append(" a.withholding_day as predrawDay FROM cms_interest_way a ");
        sql.append(" WHERE 1 = 1 ");
        sql.append(" and a.active_date is not null ");
        if (EmptyUtils.isNotEmpty(sendId)) {
            sql.append("  and a.send_code = :billCode ");
            params.put("billCode", sendId);
        }
        sql.append("  ORDER BY a.send_code,a.active_date,a.create_date ");
        List<InterestWayBean> list = listBySQLAliasToBean(sql.toString(), params, InterestWayBean.class);
        return list;
    }
    /**
     * <br>
     * <b>功能：</b>获取计息方式调整记录<br>
     *
     * @param billCode
     * @param repayCycles
     * @return
     */
    public List<InterestWayBean> getInterestWaysByBillCode(String sendId, Set<String> repayCycles) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = " SELECT a.interest_way as interestWay,a.send_code as billCode,a.expiry_day as interestDay,a.active_date as effectDate,"
                + "'02' as isStanderd,"
                + " a.withholding_day as predrawDay FROM cms_interest_way a WHERE a.active_date is not null ";
        if (EmptyUtils.isNotEmpty(sendId)) {
            sql += " and a.send_code = :billCode ";
            params.put("billCode", sendId);
        }
        if (null != repayCycles && !repayCycles.isEmpty()) {
            sql += " and exists (select 1 from cms_provide_loan_send c where c.id = a.send_code ";
            String hqlSelect = "";
            for (String repayCycle : repayCycles) {
                if (EmptyUtils.isEmpty(repayCycle)) {
                    continue;
                }
                String[] cycleArr = repayCycle.split(",");
                if (null != cycleArr && cycleArr.length >= 3) {
                    hqlSelect += " (c.repay_cycle = '" + cycleArr[0] + "' and c.cur_code = '" + cycleArr[1] + "' "
                            + " and c.reference_rate_type = '" + cycleArr[2] + "' ) OR";
                }
            }
            if (EmptyUtils.isNotEmpty(hqlSelect)) {
                hqlSelect = hqlSelect.substring(0, hqlSelect.length() - 2);
                sql += " and ( " + hqlSelect + " )  and c.status >= 95 )";
            }
        }
        sql += " ORDER BY a.send_code,a.active_date ";
        List<InterestWayBean> list = listBySQLAliasToBean(sql, params, InterestWayBean.class);
        return list;
    }
    public List<IstFloatBean> getInterestFloatsByBillCode(String sendId) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = " SELECT a.floating_Mode as floatWay,a.bill_Code as billCode,a.enter_date as effectDate,"
                + "'02' as floatWayStanderd,"
                + "a.float_mode_next as floatWayNext,a.POINTS_TEXT as pointsText,BT_NVL_NUM(a.fixedrate,0) fixRate  FROM cms_floating_mode a WHERE a.enter_date is not null ";
        if (EmptyUtils.isNotEmpty(sendId)) {
            sql += " and a.bill_Code = :billCode ";
            params.put("billCode", sendId);
        } else {
            return null;
        }
        sql += " and a.enter_date = (select max(enter_date) from cms_floating_mode where bill_code = :billCode )  ";
        sql += " ORDER BY a.bill_Code,a.enter_date";
        List<IstFloatBean> list = listBySQLAliasToBean(sql, params, IstFloatBean.class);
        return list;
    }
    public List<IstFloatBean> getOrderInterestFloatsByBillCode(String sendId) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = " SELECT a.floating_Mode as floatWay,a.bill_Code as billCode,a.enter_date as effectDate,"
                + "'02' as floatWayStanderd,"
                + " a.float_mode_next as floatWayNext,a.POINTS_TEXT as pointsText,BT_NVL_NUM(a.fixedrate,0) fixRate,REFERENCE_RATE_TYPE as referenceRateType,REPAY_CYCLE as repayCycle  "
                + " FROM cms_floating_mode a WHERE a.enter_date is not null ";
        if (EmptyUtils.isNotEmpty(sendId)) {
            sql += " and a.bill_Code = :billCode ";
            params.put("billCode", sendId);
        } else {
            return null;
        }
        sql += " ORDER BY a.bill_Code,a.create_date desc";
        List<IstFloatBean> list = listBySQLAliasToBean(sql, params, IstFloatBean.class);
        return list;
    }
    public List<IstFloatBean> getInterestFloatsByBillCode(String sendId, Set<String> repayCycles) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = " SELECT a.floating_Mode as floatWay,a.bill_Code as billCode,a.enter_date as effectDate,"
                + "'02' as floatWayStanderd,"
                + "a.float_mode_next as floatWayNext,BT_NVL_NUM(a.rate,0) fixRate  FROM cms_floating_mode a WHERE a.enter_date is not null ";
        if (EmptyUtils.isNotEmpty(sendId)) {
            sql += " and a.bill_Code = :billCode ";
            params.put("billCode", sendId);
        }
        if (null != repayCycles && !repayCycles.isEmpty()) {
            sql += " and exists (select 1 from cms_provide_loan_send c where c.id = a.bill_Code ";
            String hqlSelect = "";
            for (String repayCycle : repayCycles) {
                if (EmptyUtils.isEmpty(repayCycle)) {
                    continue;
                }
                String[] cycleArr = repayCycle.split(",");
                if (null != cycleArr && cycleArr.length >= 3) {
                    hqlSelect += " (c.repay_cycle = '" + cycleArr[0] + "' and c.cur_code = '" + cycleArr[1] + "' "
                            + " and c.reference_rate_type = '" + cycleArr[2] + "' ) OR";
                }
            }
            if (EmptyUtils.isNotEmpty(hqlSelect)) {
                hqlSelect = hqlSelect.substring(0, hqlSelect.length() - 2);
                sql += " and ( " + hqlSelect + " )  and c.status >= 95 )";
            }
        }
        sql += " ORDER BY a.bill_Code,a.enter_date";
        List<IstFloatBean> list = listBySQLAliasToBean(sql, params, IstFloatBean.class);
        return list;
    }
    public List<RateAdjustBean> getRateAdjustsByInfoId(String id) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = " select * from CMS_PROVIDE_LOAN_RATE where 1=1 ";
        if (EmptyUtils.isNotEmpty(id)) {
            sql += " and bill_code = :billCode ";
            params.put("billCode", id);
        } else {
            return null;
        }
        sql += " ORDER BY enter_date desc,create_date desc ";
        // sql += " and enter_date = (select max(enter_date) from CMS_PROVIDE_LOAN_RATE where bill_code = :billCode ) ";
        List<RateAdjustBean> list = listBySQLAliasToBean(sql, params, RateAdjustBean.class);
        if (!EmptyUtils.isEmptyList(list) && list.size() > 1) {
            List<RateAdjustBean> result = new ArrayList<RateAdjustBean>();
            RateAdjustBean rateAdjustBean = new RateAdjustBean();
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    rateAdjustBean = list.get(0);
                } else {
                    // 获取最新的调整利率，调整展期利率，调整逾期利率
                    if (null == rateAdjustBean.getDeferredRate()) {
                        rateAdjustBean.setDeferredRate(list.get(i).getDeferredRate());
                    }
                    if (null == rateAdjustBean.getOverdueRate()) {
                        rateAdjustBean.setOverdueRate(list.get(i).getOverdueRate());
                    }
                    if (null == rateAdjustBean.getPlRate()) {
                        rateAdjustBean.setPlRate(list.get(i).getPlRate());
                    }
                }
                if (null != rateAdjustBean.getPlRate() && null != rateAdjustBean.getDeferredRate()
                        && null != rateAdjustBean.getOverdueRate()) {
                    break;
                }
            }
            result.add(rateAdjustBean);
            return result;
        }
        return list;
    }
    public List<CmsProvideInterest> listMatureRepay(Date date) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select send.bill_code as billCode,inter.* ");
        sql.append(" from cms_provide_interest inter");
        sql.append(" inner join cms_provide_loan_send send on send.bill_code = inter.bill_code ");
        sql.append(" where inter.interest_sign = '1' and send.status = :status ");
        // sql.append(" and (inter.repay_sign = :repaySign or inter.repay_sign is null) ");
        sql.append(" and inter.repay_date <= :endDate ");
        sql.append("  and inter.status = 0 ");
        sql.append(" order by inter.repay_date ");
        params.put("status", CmsConstant.COMPELETOTAL);
        // params.put("repaySign", CommonConstant.SEND_SIGN_NO);
        params.put("endDate", date);
        return listBySQLAliasToBean(sql.toString(), params, CmsProvideInterest.class);
    }
    /**
     * 根据单据编号查询利息预计明细
     *
     * @return
     */
    public List<CmsProvideInterest> queryPayedInterest(String billCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer hql = new StringBuffer();
        hql.append(" from CmsProvideInterest i ");
        hql.append(" where 1=1 ");
        hql.append(" and i.billCode = :billCode ");
        hql.append(" and i.interestSign = '1' ");// 测算
        hql.append(" and i.status = '1'");// 已还
        hql.append(" order by i.repayDate ");
        params.put("billCode", billCode);
        return listByHQL(hql.toString(), params);
    }
    /**
     * 更新这个还款日期标记内的全部利息预计明细为已还
     *
     * @param params
     */
    public void updateInterestBySignRepayDate(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "update cms_provide_interest set status = 1 where bill_code = :billCode and SIGN_REPAY_DATE = BT_TO_DATE(:signRepayDate,'yyyy-MM-dd') and INTEREST_SIGN = '1'");
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    /**
     * 查询到期提醒日期内数据
     *
     * @return
     */
    public List<CmsProvideInterestVO> listNotifyInterest(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("select interest.*,info.cur_name as curName from CMS_PROVIDE_INTEREST interest");
        sql.append(" inner join CMS_INL_SEND send ");
        sql.append(" on interest.bill_code = send.bill_no");
        //TODO 补充币种名称信息
        sql.append(" left join CMS_INL_INFO info on send.inl_info_id = info.id ");
        sql.append(" where 1 = 1");
        sql.append(" and interest.repay_Date >= BT_TO_DATE(:startDate,'yyyy-MM-dd')");
        sql.append(" and interest.repay_Date <= BT_TO_DATE(:endDate,'yyyy-MM-dd')");
        sql.append(" and interest.interest_sign = '1'");
        sql.append(" and interest.status = 0");
        return listBySQLAliasToBean(sql.toString(), params, CmsProvideInterestVO.class);
    }

    public List<CmsProvideInterest> getProvideInterestBySendCode(String sendBillCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer hql = new StringBuffer();
        hql.append(" from CmsProvideInterest i ");
        hql.append(" where i.billCode = :billCode ");
        hql.append(" and i.status = '1'");//已还
        hql.append(" order by i.repayDate ");
        params.put("billCode", sendBillCode);
        return listByHQL(hql.toString(), params);
    }

    public CmsProvideInterestVO sumLeftMoney(String sendCode, String status) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer();
        sql.append(" select sum(REPAY_MONEY) repayMoney from cms_provide_interest i where i.bill_code = :billCode and i.status = :status ");
        params.put("billCode", sendCode);
        params.put("status", status);
        return getBySQLAliasToBean(sql.toString(), params, CmsProvideInterestVO.class);
    }

    public CmsProvideInterestVO queryInterestMaxAndMinDate(String sendCode, String status) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer();
        if (status.equals(CommonConstant.YES)) {
            sql.append(" select max(sign_repay_date) signRepayDate from cms_provide_interest i where i.bill_code = :billCode and i.status = :status and interest_sign = '1' ");
        } else {
            sql.append(" select min(sign_repay_date) signRepayDate from cms_provide_interest i where i.bill_code = :billCode and i.status = :status and interest_sign = '1' ");
        }
        params.put("billCode", sendCode);
        params.put("status", status);
        return getBySQLAliasToBean(sql.toString(), params, CmsProvideInterestVO.class);
    }

    public void deleteBySendICode(String billCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer();
        sql.append("delete from cms_provide_interest  where bill_code = :billCode ");
        params.put("billCode", billCode);
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
}
