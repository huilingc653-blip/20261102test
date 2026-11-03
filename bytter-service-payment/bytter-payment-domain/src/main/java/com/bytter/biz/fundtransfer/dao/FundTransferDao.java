package com.bytter.biz.fundtransfer.dao;

import com.bytter.biz.allocation.vo.BisAccGatherPolicyVO;
import com.bytter.biz.allocation.vo.ExcRulesManageAdCorpVO;
import com.bytter.biz.fundtransfer.bean.BisAccGatherPolicySearchBean;
import com.bytter.biz.inneraccount.entity.BisAccGatherPolicy;
import com.bytter.biz.payment.api.constant.BtBankAccConstant;
import com.bytter.biz.payment.api.dto.BisAccDtlDTO;
import com.bytter.biz.payment.api.dto.BtBankAccDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.dto.BisAccBalDTO;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资金调拨相关查询Dao
 *
 * @author fangcf
 * @date 2026-04-13
 */
@Repository
public class FundTransferDao extends BaseDao<BisAccGatherPolicy, String> {

    /**
     * 根据查询bea获取List<BisAccGatherPolicyVO>
     *
     * @param searchBean
     * @return java.util.List<com.bytter.biz.allocation.vo.BisAccGatherPolicyVO>
     */
    public List<BisAccGatherPolicyVO> getBisAccGatherPolicyList(BisAccGatherPolicySearchBean searchBean) {
        Map params = new HashMap<>();
        String sql = "select a.TENANT_ID as tenantId, a.policyId id,a.policyId policyId,a.bank_acc bankAcc,a.bank_acc_id bankAccId,a.id gatherPolicyId,"
                + "a.gather_policy_code gatherPolicyCode,a.gather_policy_name gatherPolicyName,a.def_date defDate,"
                + "a.launch_time launchTime,a.gather_type gatherType,a.frq_mode frqMode,a.serial_day serialDay,"
                + "a.min_amt minAmt,a.max_amt maxAmt,a.gather_mode gatherMode,a.gather_amt gatherAmt,"
                + "a.down_fixed_amt downFixedAmt,a.app_next_time appNextTime,a.differ_scale differScale,"
                + "a.is_quick isQuick,a.valid_sign validSign,a.bif_code_id bifCodeId,a.parent_acc parentBankAcc,a.bif_code_id bifCode,"
                + "a.parent_acc_id parentAccId,a.first_time firstTime,a.next_time nextTime,"
                + "a.bank_sourcecode bankSourceCode,a.cur_id curId"
                + " from v_bis_acc_gather_policy a where 1 = 1 ";
        if (searchBean.getNextTime() != null) {
            sql += " and (a.next_time < :nextTime or a.next_time is null)";
            params.put("nextTime", searchBean.getNextTime());
        }
        if (searchBean.getGatherType() != null) {
            sql += " and a.gather_type in :gatherType";
            params.put("gatherType", searchBean.getGatherType());
        }
        if (searchBean.getGatherMode() != null) {
            sql += " and a.gather_mode in :gatherMode";
            params.put("gatherMode", searchBean.getGatherMode());
        }
        return listBySQLAliasToBean(sql, params, BisAccGatherPolicyVO.class);
    }

    /**
     * 更新下次下拨时间
     *
     * @param updateBisAccGatherPolicy
     * @return
     */
    public int updateBisAccGatherPolicyNextTime(BisAccGatherPolicyVO updateBisAccGatherPolicy) {
        String sql = "update bis_acc_gather_policy set next_time = :expectedNextTime where id = :id";
        HashMap<String, Object> params = new HashMap();
        params.put("expectedNextTime", updateBisAccGatherPolicy.getExpectedNextTime());
        params.put("id", updateBisAccGatherPolicy.getId());
        return createSQLQuery(sql, params).executeUpdate();
    }

    /**
     * 根据系统结算方式获取有效的付款方式信息
     *
     * @param sysVoucherCode
     * @return
     */
    public String getSysVoucherCode(String sysVoucherCode, String tenantId) {
        String sql = "select id id from fc_voucher_type t where t.sys_voucher_code = :sysVoucherCode and t.valid_sign = '1' and t.tenant_Id = :tenantId limit 1 ";
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("sysVoucherCode", sysVoucherCode);
        params.put("tenantId", tenantId);
        SQLQuery query = getSession().createSQLQuery(sql);
        return (String) query.setProperties(params).setProperties(params).uniqueResult();
    }

    /**
     * 获取银行账号信息List<BtBankAccDTO>
     *
     * @param bankAccIds
     * @return java.util.List<com.bytter.facade.netbank.dto.BtBankAccDTO>
     */
    public List<BtBankAccDTO> getBankAccByBankAccIds(List<String> bankAccIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select acc.id as id,acc.bank_acc as bankAcc,acc.is_online as isOnline,acc.acc_name as accName,acc.parent_sign as parentSign,acc.corp_id as corpId, ");
        sql.append(" acc.acc_type as accType,acc.attribute_id as attributeId,acc.nature_id as natureId, ");
        sql.append(" acc.bank_type_id as bankTypeId,acc.bank_code as bankCode,acc.bank_name as bankName,acc.prov as prov,acc.city as city,acc.bif_code as bifCode,acc.electric_bill as electricBill, ");
        sql.append(" acu.cur_id curId ,acc.tenant_id tenantId");
        sql.append(" from bt_bank_acc acc ");
        sql.append(" left join bt_bank_acc_cur acu ");
        sql.append(" on acu.bank_acc_id=acc.id ");
        sql.append(" where acc.id in (:bankAccIds) ");
        sql.append(" and acc.valid_Sign = '" + BtBankAccConstant.VALIDSIGN_1 + "' and acc.status = " + BtBankAccConstant.STATUE_CHECK);
        Map<String, Object> params = new HashMap<>();
        params.put("bankAccIds", bankAccIds);
        List mapList = listBySQL(sql.toString(), params);
        if (EmptyUtils.isEmpty(mapList)) {
            return null;
        }
        return listBySQLAliasToBean(sql.toString(), params, BtBankAccDTO.class);
    }

    /**
     * 根据id查找银行余额信息
     *
     * @param id
     * @return BisAccBalVO
     */
    public BisAccBalDTO getByUnion(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.id,b.acc_no,b.bank_acc,b.corp_id,b.cur_id,b.get_date,b.bal_date,b.bal,b.avail_bal,b.frz_bal, ");
        sql.append(" b.status,a.acc_name as accName,a.bank_name as bankName,b.return_Msg as returnMsg ");
        sql.append(" from bis_acc_bal b ");
        sql.append(" left join bt_bank_acc a on b.bank_acc = a.bank_acc ");
        sql.append(" where a.valid_sign = :validSign and b.id = :id ");
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("validSign", CommonConstant.VALID_SIGN_YES);
        return getBySQLAliasToBean(sql.toString(), params, BisAccBalDTO.class);
    }

    /**
     * 根据id查找银行余额信息
     *
     * @param bankAcc 银行账号
     * @param curId   币别
     * @param balDate 余额日期
     * @return BisAccBalVO
     */
    public List<BisAccBalDTO> listByUnion(String bankAcc, String curId, Date balDate) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.id,b.acc_no,b.bank_acc,b.corp_id,b.cur_id,b.get_date,b.bal_date,b.bal,b.avail_bal,b.frz_bal, ");
        sql.append(" b.status,a.acc_name as accName,a.bank_name as bankName,b.return_Msg as returnMsg ");
        sql.append(" from bis_acc_bal b ");
        sql.append(" left join bt_bank_acc a on b.bank_acc = a.bank_acc and a.valid_sign = :validSign ");
        sql.append(" where b.bank_acc = :bankAcc and b.cur_id = :curId and b.bal_Date >= bt_to_date(:balStartDate, 'yyyy-MM-dd') and b.bal_Date < bt_to_date(:balEndDate, 'yyyy-MM-dd') ");
        HashMap<String, Object> params = new HashMap<>();
        params.put("validSign", CommonConstant.VALID_SIGN_YES);
        params.put("bankAcc", bankAcc);
        params.put("curId", curId);

        String balStartDate = DateUtils.formatDate(balDate);
        // 余额日期 + 1
        String balEndDate = DateUtils.formatDate(DateUtils.addDay(balDate, 1));
        params.put("balStartDate", balStartDate);
        params.put("balEndDate", balEndDate);
        return listBySQLAliasToBean(sql.toString(), params, BisAccBalDTO.class);
    }

    /**
     * 获取需要过滤的交易信息bis_policy_filter（用于实时归集过滤）
     *
     * @return parm(key ( 收款账号 + 付款账号)，value（付款账号）)
     */
    public HashMap<String, String> gatherPolicyFilterMap() {
        HashMap<String, String> params = new HashMap();
        String sql = "select payee_acc_no payeeAccNo,payer_acc_no payerAccNo from bis_policy_filter where valid_sign = '1'";
        SQLQuery query = super.getSession().createSQLQuery(sql);
        query.addScalar("payeeAccNo");
        query.addScalar("payerAccNo");
        query.setResultTransformer(Transformers.aliasToBean(ExcRulesManageAdCorpVO.class));
        List<ExcRulesManageAdCorpVO> list = query.list();
        for (ExcRulesManageAdCorpVO bpf : list) {
            params.put(bpf.getPayeeAccNo() + bpf.getPayerAccNo(), bpf.getPayerAccNo());
        }
        return params;
    }

    /**
     * 获取据指定上次归集时间没有完成该归集的且为收入的余额明细及历史明细（收款明细）
     *
     * @param bankAcc        银行账号
     * @param lastGatherDate 上次归集时间
     * @return 返回{@link #BisAccDtl}对象集合，包含属性：serialId、amt、transTime
     */
    public List<BisAccDtlDTO> getBisAccDtl4NotGather(String bankAcc, Date lastGatherDate) {
        String sql = "select a.id, a.amt, a.trans_time transTime, a.opp_acc_no oppAccNo " +
                " from bis_acc_dtl a, v_acc_list b " +
                " where a.bank_acc = b.bank_acc and (a.opp_acc_no <> b.prent_acc or a.opp_acc_no is null) " +
                // 本方账号为收入明细（归集时，归集的账号有收入明细）
                " and a.bank_acc = :bankAcc and a.cd_sign = '0' and a.trans_time >= :lastGatherDate " +
                // 判断是否已经归集
                " and NOT EXISTS (select '1' from bis_gather_cmd gc where gc.dtl_id = a.id) ";
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("bankAcc", bankAcc);
        params.put("lastGatherDate", lastGatherDate);
        return listBySQLAliasToBean(sql, params, BisAccDtlDTO.class);
    }

    /**
     * 根据指定日期判断是否为工作日
     *
     * @param date 日期，格式 yyyy-MM-dd
     * @return true 工作日 / false 非工作日
     */
    public boolean isWorkDaySign(String date) {
        String sql = "select work_day_sign from sys_calendar a where 1=1 and a.create_date=BT_TO_DATE(:date,'yyyy-MM-dd') and a.country_id is null and a.cur_id is null";
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("date", date);
        SQLQuery query = getSession().createSQLQuery(sql);
        // sys_calendar 可能未维护到该日期，此时 uniqueResult() 返回 null，直接 toString() 会抛 NPE
        Object workDaySign = query.setProperties(params).uniqueResult();
        if (workDaySign == null) {
            // 日历未维护时按星期兜底（周一~周五视为工作日）：
            // 既让节假日过滤仍近似生效，也保证 FundTransferBase 中按工作日前后找日期的循环能够收敛
            boolean weekday = isWeekday(date);
            logger.warn("资金调拨：sys_calendar 未维护日期[{}]，按星期兜底判定为[{}]", date, weekday ? "工作日" : "非工作日");
            return weekday;
        }
        return "1".equals(workDaySign.toString());
    }

    /**
     * 按星期判断日期是否为工作日（周一~周五）。
     * <p>仅用于 sys_calendar 未维护时的兜底；日期解析失败时按工作日处理，避免调用方循环无法收敛。
     *
     * @param date 日期，格式 yyyy-MM-dd
     * @return true 工作日 / false 非工作日
     */
    private boolean isWeekday(String date) {
        if (EmptyUtils.isEmpty(date)) {
            return true;
        }
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            return dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY;
        } catch (ParseException e) {
            logger.warn("资金调拨：日期[{}]解析失败，按工作日兜底", date, e);
            return true;
        }
    }


//    /**
//     * TODO：批量增加实时归集指令
//     * @param bisGatherCmds
//     * @return
//     */
//    public int[] addBisGatherCmdBatch(BisGatherCmdDTO... bisGatherCmds) throws Exception {}

}
