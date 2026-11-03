package com.bytter.finance.domain.provide.dao;
import cn.hutool.core.collection.CollectionUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.basedata.vo.CmsProvideLoanSendCheckVO;
import com.bytter.finance.domain.provide.entity.ProvideLoanSend;
import com.bytter.finance.domain.provide.vo.LoanSendVO;
import com.bytter.finance.dto.CmsProvideLoanRepayDTO;
import com.bytter.finance.dto.CmsProvideSendDTO;
import com.bytter.finance.dto.FinanceLoanDTO;
import com.bytter.finance.dto.FinanceTypeLoanDTO;
import com.bytter.system.api.dto.TbRateMaintenceDTO;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.*;
/**
 * @Title: Dao
 * @Description: 银行借款发放表
 * @author onlineGenerator
 * @date 2017-08-03 16:40:48
 * @version 1.0
 */
@Repository
public class CmsProvideLoanSendDao extends BaseDao<ProvideLoanSend, String> {
    /**
     * 功能介绍 : 查询借款发放列表<br>
     */
    public DataTablesOutput getDataSendTables(DataTablesInput dataTablesInput, List<String> corpAuth) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select send.id as id,info.corp_id as corpId,info.corp_name as corpName,send.bill_code as billCode,info.bill_code as loanCode,info.bank_name as bankName,info.create_date as createDate, ");
        sql.append(" info.cur_id as curId,info.cur_name as curName,info.pl_money as contractSumMoney,(info.pl_money - BT_NVL_NUM(c.usedmoney,0)) as waitRepayMoney,info.loan_send_out_type as loanSendOutType, ");
        sql.append(" repayType.repay_name repayTypeName1,");
        sql.append(" send.contract_code as contractCode,send.money as money,info.repay_type as repayType,info.pl_term as plTerm,info.tern_unit as ternUnit,send.status as status,BT_NVL_CHAR(send.biz_status,info.biz_status) as bizStatus ");
        sql.append(" from cms_provide_loan_info info ");
        sql.append(" left join cms_provide_loan_send send on info.bill_code = send.loan_code and send.status >= -1 and send.status < 102 ");
        sql.append(" left join (select loan_code, sum(money) usedmoney from cms_provide_loan_send where status > 0 group by loan_code) c on c.loan_code = info.bill_code ");
        sql.append(" left join CMS_REPAY_TYPE repayType on info.repay_type = repayType.id");
        sql.append(" where info.status = 96 and (c.usedmoney < info.pl_money or c.usedmoney is null)");
        sql.append(" and not exists(select 1 from cms_provide_loan_send send where  info.bill_code = send.loan_code  and (send.status = 97) ) ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "info.corp_id"));
        OrderString orderString = new OrderString("info.create_date", "desc");
        dataTablesInput.setOrderString(orderString);
        return getDynamicBySQL(dataTablesInput, sql.toString(), LoanSendVO.class, null, false);
    }
    /**
     * 功能介绍 : 查询借款发放审批<br>
     */
    public DataTablesOutput getDataSendAppvoerTables(DataTablesInput dataTablesInput, List<String> ids) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        //TODO 补充名称相关信息
        sql.append(
                "select send.id as id,info.corp_id as corpId,send.bill_code as billCode,info.bill_code as loanCode,info.bank_name as bankName,info.create_date as createDate, ");
        sql.append(
                " info.cur_id as curId,info.pl_money as contractSumMoney,info.loan_send_out_type as loanSendOutType, ");
        sql.append("info.cur_name as curName,info.corp_name as corpName,");
        sql.append(
                " send.contract_code as contractCode,send.money as money,info.repay_type as repayType,repayType.repay_name repayTypeName1,send.pl_term as plTerm,info.tern_unit as ternUnit,send.status as status,send.biz_status as bizStatus ");
        sql.append(" from cms_provide_loan_info info ");
//        sql.append(" left join bt_bank bank on bank.sys_bank_code = info.bank_id ");
        sql.append(" left join cms_provide_loan_send send on info.bill_code = send.loan_code");
        sql.append(" left join CMS_REPAY_TYPE repayType on info.repay_type = repayType.id");
        // sql.append(" where send.status = 11 ");//工作流根据id查询数据 不用数据状态过滤
        sql.append(" where 1 = 1");
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sql.append(" and ").append(getSQLIn(ids, 900, "send.id"));
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), LoanSendVO.class, null, false);
    }
    /**
     * 功能介绍 : 根据借款单号查询发放信息<br>
     */
    public ProvideLoanSend getSendByBillCode(String billCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select send.* ");
        sql.append(" from cms_provide_loan_info info ");
//        sql.append(" left join bt_bank bank on bank.sys_bank_code = info.bank_id ");
        sql.append(
                " left join cms_provide_loan_send send on info.bill_code = send.loan_code and send.status >= -1 and send.status < 102 ");
        sql.append(
                " left join (select loan_code, sum(money) usedmoney from cms_provide_loan_send where status > 0 group by loan_code) c on c.loan_code = info.bill_code ");
        sql.append("  where info.status = 96 and (c.usedmoney < info.pl_money or c.usedmoney is null)");
        sql.append(" and info.bill_code=:billCode");
        params.put("billCode", billCode);
        return this.getBySQLAliasToBean(sql.toString(), params, ProvideLoanSend.class);
    }
    /**
     * 功能介绍 : 根据发放单号查询发放信息<br>
     */
    public ProvideLoanSend getSendBySendBillCode(String sendBillCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select * from cms_provide_loan_send where bill_code=:billCode ");
        params.put("billCode", sendBillCode);
        return this.getBySQLAliasToBean(sql.toString(), params, ProvideLoanSend.class);
    }
    /**
     * 功能介绍 : 查询发放次数<br>
     */
    public Integer getSendCountByBillCode(String billCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "select count(*) as cn from cms_provide_loan_send where loan_code=:billCode";
        params.put("billCode", billCode);
        Map rs = getBySQL(sql, params);
        Integer count = null;
        if (rs.containsKey("cn")) {
            count = new Integer(rs.get("cn").toString());
        }
        if (rs.containsKey("CN")) {
            count = new Integer(rs.get("CN").toString());
        }
        return count;
    }
    /**
     * 功能介绍 : 查询发放金额<br>
     */
    public BigDecimal getSendMoneyByBillCode(String billCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "select loan_code, BT_NVL_NUM(sum(money),0) usedmoney from cms_provide_loan_send where status >= 102 and loan_code=:billCode group by loan_code ";
        params.put("billCode", billCode);
        Map rs = getBySQL(sql, params);
        if (rs == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal count = null;
        if (rs.containsKey("usedmoney")) {
            count = new BigDecimal(rs.get("usedmoney").toString());
        }
        if (rs.containsKey("USEDMONEY")) {
            count = new BigDecimal(rs.get("USEDMONEY").toString());
        }
        return count;
    }
    /**
     * 功能介绍 : 根据发放单号查询LoanSendVO<br>
     */
    public LoanSendVO getLoanSendVOBySendBillCode(String sendBillCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select send.*,i.REPAY_CYCLE_NAME as repayCycleName, ");
        sql.append(" i.create_by as infoCreateBy ,i.update_by as infoUpdateBy, ");
        sql.append(" repayType.repay_name repayTypeName1,accrualType.accrual_name accrualTypeName");
        sql.append(" from cms_provide_loan_send send ");
        sql.append(" left join cms_provide_loan_info i on i.bill_code = send.loan_code ");
//        sql.append(" left join tb_base_rate base on base.id = send.reference_rate_type ");// 基准利率类型
        sql.append(" left join CMS_REPAY_TYPE repayType on send.repay_type=repayType.id");
        sql.append(" left join CMS_ACCRUAL_TYPE accrualType on send.accrual_type=accrualType.id");
        sql.append(" where send.bill_code=:billCode");
        params.put("billCode", sendBillCode);
        LoanSendVO vo = this.getBySQLAliasToBean(sql.toString(), params, LoanSendVO.class);
        return vo;
    }
    /***
     * 根据billCode删除 打回或暂存的发放数据
     */
    public void delSendByBillCode(String billCode) {
    }
    /**
     * 根据id修改还款方式
     *
     * @param id
     */
    public void updateRepayType(String id, String repayType) {
        Map<String, String> params = new HashMap<String, String>();
        StringBuilder sql = new StringBuilder();
        sql.append("update cms_provide_loan_send set repay_type =:repayType where id=:id");
        params.put("repayType", repayType);
        params.put("id", id);
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    /**
     * 功能介绍 : 判断发放合同号是否重复<br>
     *
     * @return 重复：true 否则false
     */
    public boolean checkContractNoRepeat(ProvideLoanSend send) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select BT_NVL_NUM(count(send.contract_code),0) as cou from cms_provide_loan_send send ");
        sql.append(" where send.contract_code = :contractNo ");
        if (!EmptyUtils.isEmpty(send.getId())) {
            sql.append(" and send.id <> :id ");
            params.put("id", send.getId());
        }
        params.put("contractNo", send.getContractCode());
        Map rs = getBySQL(sql.toString(), params);
        if (rs == null) {
            return false;
        }
        BigDecimal count = null;
        if (rs.containsKey("cou")) {
            count = new BigDecimal(rs.get("cou").toString());
        }
        if (rs.containsKey("COU")) {
            count = new BigDecimal(rs.get("COU").toString());
        }
        return count.compareTo(BigDecimal.ZERO) != 0;
    }
    /** 根据billCode查询发放集合 */
    public List<LoanSendVO> getLoanSendVOListByBillCode(String billCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select send.*,info.tern_unit as ternUnit from cms_provide_loan_send send");
        sql.append(" left join cms_provide_loan_info info on info.bill_code = send.loan_code");
        sql.append(" where send.loan_code=:billCode");
        params.put("billCode", billCode);
        return this.listBySQLAliasToBean(sql.toString(), params, LoanSendVO.class);
    }
    public List<ProvideLoanSend> getLoanSendListByBillCode(String billCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select * from cms_provide_loan_send where loan_code=:billCode ");
        params.put("billCode", billCode);
        return listBySQLAliasToBean(sql.toString(), params, ProvideLoanSend.class);
    }
    /** 查询记账完成待还款的发放单 */
    public List<ProvideLoanSend> getLoanSendCompeletotalByBillCode(String billCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select * from cms_provide_loan_send where loan_code=:billCode and status > 95");
        params.put("billCode", billCode);
        return listBySQLAliasToBean(sql.toString(), params, ProvideLoanSend.class);
    }
    /**
     * <br>
     * <b>功能：</b>获取贷款期限的借款发放信息<br>
     *
     * @param repayCycles
     * @param sendBillCode
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ProvideLoanSend> getLoanSendsByRepayCycle(String sendBillCode, Set<String> repayCycles) {
        Map<String, Object> params = new HashMap<String, Object>();
        String hql = " from ProvideLoanSend a " + " where a.status >= 1 ";
        if (EmptyUtils.isNotEmpty(sendBillCode)) {
            hql += " and a.billCode = :billCode ";
            params.put("billCode", sendBillCode);
        }
        if (null != repayCycles && !repayCycles.isEmpty()) {
            String hqlSelect = "";
            for (String repayCycle : repayCycles) {
                if (EmptyUtils.isEmpty(repayCycle)) {
                    continue;
                }
                String[] cycleArr = repayCycle.split(",");
                if (null != cycleArr && cycleArr.length >= 3) {
                    hqlSelect += " (a.repayCycle = '" + cycleArr[0] + "' and a.curCode = '" + cycleArr[1] + "' "
                            + "and a.referenceRateType = '" + cycleArr[2] + "') OR";
                }
            }
            if (EmptyUtils.isNotEmpty(hqlSelect)) {
                hqlSelect = hqlSelect.substring(0, hqlSelect.length() - 2);
                hql += " and ( " + hqlSelect + " ) ";
            }
        }
        hql += " order by a.sendDate ";
        return createQuery(hql, params).list();
    }
    public List<ProvideLoanSend> listSendByStatus(TbRateMaintenceDTO maintence) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" from ProvideLoanSend send where send.status >= 102 and send.status < 110 ");
        hql.append(" and send.floatWay != '" + CmsConstant.FLOAT_WAY_01 + "'");
        hql.append(" and send.referenceRateType ='" + maintence.getBaseRateId() + "'");
        hql.append(" and send.repayCycle ='" + maintence.getCreditPeriodId() + "'");
        return listByHQL(hql.toString(), params);
    }
    public List<ProvideLoanSend> listMatureSend(Date date) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" from ProvideLoanSend send where send.status = :status  ");
        hql.append(" and (send.sendSign = :sendSign or send.sendSign is null) and send.repayDate <= :endDate ");
        hql.append(" order by send.repayDate ");
        params.put("sendSign", CommonConstant.SEND_SIGN_NO);
        params.put("status", CmsConstant.COMPELETOTAL);
        params.put("endDate", date);
        return listByHQL(hql.toString(), params);
    }
    /**
     * <b>功能介绍：</b>查询内部贷款发放到期<br>
     * <b>日期：</b>2017年11月16日<br>
     * <b>作者：</b>xxx<br>
     *
     * @version 1.0
     * @param netId
     * @param endDate
     * @return
     */
    public List<CmsProvideLoanSendCheckVO> getBankLoadSendInfoByEndDate(String netId, Date endDate) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select a.bill_code as billCode,b.corp_name as corpName,a.LOAN_ACC as loanAcc, ");
        sql.append(" a.create_date as enterDate,b.cur_name as curName,a.MONEY as money, ");
        sql.append(" a.send_date as sendDate,a.repay_date as deadlineDate,a.REPAY_TYPE as repayType, repayType.repay_name as repayTypeName, ");
        sql.append(" b.pl_class as plClass,a.PL_TERM as plterm,b.TERN_UNIT as ternUnit ");
        sql.append(" from CMS_PROVIDE_LOAN_SEND a ");
        sql.append(" left join CMS_PROVIDE_LOAN_info b on a.LOAN_CODE = b.BILL_CODE ");
        sql.append(" left join CMS_REPAY_TYPE repayType on a.REPAY_TYPE = repayType.id ");
//        sql.append(" left join sys_corp sc on sc.id = a.CORP_ID ");
//        sql.append(" left join BT_CURRENCY bc on bc.id = b.CUR_ID ");
        sql.append(" where (a.status > :status0 and a.status < :status1 ) ");
        if (EmptyUtils.isNotEmpty(netId)) {
            sql.append("and b.corp_id = :netId ");
            params.put("netId", netId);
        }
        if (!EmptyUtils.isEmpty(endDate)) {
            sql.append("and a.repay_date <= :endDate ");
            params.put("endDate", endDate);
        }
        params.put("status0", CmsConstant.DELETED);
        params.put("status1", CmsConstant.FINISH);
        return listBySQLAliasToBean(sql.toString(), params, CmsProvideLoanSendCheckVO.class);
    }
    /**
     * 获取查询语句--记账
     */
    public StringBuilder getSql() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select info.net_id as netId,info.corp_id as corpId, info.bill_code as loanCode,info.contract_no as contractCode, ");
        sql.append(" s.repay_date as sendRepayDate, info.bank_id as bankId, s.bill_code as billCode,");
        sql.append(" info.contract_term as contractTerm,info.pl_money as plMoney,info.pl_term as plTerm,info.tern_unit as ternUnit,");
        sql.append(" info.pl_account as plAccount,info.pl_date as plDate,info.repay_type as repayType,info.cur_id as curId,");
        //TODO 补充名称相关信息
        sql.append(" info.corp_name as corpName,info.cur_name as curName,");
        sql.append(" s.money as money,s.send_date as sendDate,s.loan_acc as loanAcc,info.loan_send_out_type as loanSendOutType,");
        sql.append(" r.bill_code as repayBillCode,r.send_code as sendCode,r.repay_money as repayMoney,r.repay_date as repayDate,");
        sql.append(" s.contract_code as sendContractCode,r.online_sign as onlineSign,");
        sql.append(" r.repay_accrual as repayAccrual,r.repay_acc as repayAcc,r.repayway as repayway,r.online_money as onlineMoney, ");
        sql.append(" r.online_reality_interest as onlineRealityInterest ");
        return sql;
    }
    /**
     * 查询发放账务数据 --记账
     */
    public DataTablesOutput getProvideSendDataTables(DataTablesInput dataTablesInput, List<String> corpAuth, String opType, ICurrentUser user) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(getSql());
        sql.append(" ,s.tally_sign as tallySign from cms_provide_loan_info info  ");
        sql.append(" left join cms_provide_loan_send s on s.loan_code = info.bill_code ");
        sql.append(" left join cms_provide_loan_repay r on s.bill_code = r.send_code ");
//        sql.append(" left join bt_bank bank on bank.sys_bank_code = info.bank_id ");
        sql.append(" where 1= 1 ");
        if ("Init".equals(opType)) {
            sql.append(" and s.status = 97 and ( s.tally_Sign ='' or s.tally_Sign ='0' or s.tally_sign is null ) ");
        }
        if ("Edit".equals(opType)) {
            sql.append(" and s.status = 97 and (s.tally_Sign in (5,4,3))and s.tally_code = '").append(user.getId())
                    .append("'");
        }
        if ("Check".equals(opType)) {
            sql.append(" and s.status = 97 and (s.tally_Sign ='3')  and s.tally_code != '").append(user.getId())
                    .append("'");
        }
        if(ListUtils.isNotNullOrEmpty(corpAuth)){
            sql.append(" and info.corp_id in (:corpAuth)");
            params.put("corpAuth", corpAuth);
        }

        return getDynamicBySQL(dataTablesInput, sql.toString(), LoanSendVO.class, null, false, params);
    }
    /**
     * 查询还款账务数据 --记账
     */
    public DataTablesOutput getProvideRepayDataTables(DataTablesInput dataTablesInput, List<String> corpAuth,
            String opType, ICurrentUser user, String onlineSign, String confirm) {
        StringBuilder sql = new StringBuilder();
        sql.append(getSql());
        sql.append(" ,r.tally_sign as tallySign from cms_provide_loan_info info  ");
        sql.append(" left join cms_provide_loan_send s on info.bill_code = s.loan_code ");
        sql.append(" left join cms_provide_loan_repay r on s.bill_code = r.send_code ");
//        sql.append(" left join bt_bank bank on bank.sys_bank_code = info.bank_id ");
        sql.append(" where 1= 1 ");
        if ("Init".equals(opType)) {
            sql.append(" and r.status = 97 and ( r.tally_Sign ='' or r.tally_Sign ='0' or r.tally_sign is null ) ");
        }
        if ("Edit".equals(opType)) {
            sql.append(" and r.status = 97 and (r.tally_Sign in (5,4,3)) and r.tally_code = '").append(user.getId())
                    .append("'");
        }
        if ("Check".equals(opType)) {
            sql.append(" and r.status = 97 and (r.tally_Sign ='3')  and r.tally_code != '").append(user.getId())
                    .append("'");
        }
        if (EmptyUtils.isNotEmpty(onlineSign)) {
            sql.append(" and r.online_sign = " + onlineSign);
        }
        if (EmptyUtils.isNotEmpty(confirm) && confirm != null) {
            sql.append(" and r.online_sign is null");
        }
        if (CollectionUtil.isNotEmpty(corpAuth) && !StringUtils.isNotEmpty(dataTablesInput.getFormdata().get("eqs_corpId"))){
            sql.append(" and (" + getSQLIn(corpAuth, 900, "info.corp_id") + ")");

        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), LoanSendVO.class, null, false);
    }
    /**
     * 功能介绍 : 查询未关联流水单据的借款发放完成数据列表
     */
    public DataTablesOutput getDataSendOverTables(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select send.id as id,info.corp_id as corpId,send.bill_code as billCode,info.bill_code as loanCode,info.bank_name as bankName,info.create_date as createDate, ");
        sql.append(
                " info.bank_id as bankCode,info.cur_id as curId,info.pl_money as plMoney,(info.pl_money - BT_NVL_NUM(c.usedmoney,0)) as waitRepayMoney,info.loan_send_out_type as loanSendOutType, ");
        //TODO 补充名称相关信息
        sql.append(" info.corp_name as corpName,info.cur_name as curName, ");
        sql.append(
                " send.contract_code as contractCode,send.money as money,info.repay_type as repayType,info.pl_term as plTerm,info.tern_unit as ternUnit,send.status as status,BT_NVL_CHAR(send.biz_status,info.biz_status) as bizStatus, ");
        sql.append(" send.send_date as sendDate,send.repay_date as repayDate,info.pl_rate as plRate ");
        sql.append(" from cms_provide_loan_send send ");

        sql.append(
                " left join cms_provide_loan_info info on info.bill_code = send.loan_code and send.status >= 102 and send.status < 110 ");
        //TODO 由于bt_input_bank_info 的 记录未在结果集中展示，暂时去掉，录入是从bt_bank 获取数据的
//        sql.append(" left join bt_bank bank on bank.sys_bank_code = info.bank_id ");
//        sql.append(" left join  bt_input_bank_info i ");
//        sql.append(" on i.sys_bank_code = info.bank_id ");
//        sql.append(" inner join bt_bank_type t ");
//        sql.append(" on i.bank_prefix = t.bank_prefix ");
//        sql.append(" and (t.bank_name_prefix is null or ");
//        sql.append(" i.bank_name like bt_concat2(t.bank_name_prefix, '%')) ");
        sql.append(" left join bt_bank_type t on t.id = info.bank_type ");
        sql.append(
                " left join (select loan_code, sum(money) usedmoney from cms_provide_loan_send where status > 0 group by loan_code) c on c.loan_code = info.bill_code ");
        sql.append(
                " where info.status >= 96 and info.status <110 and (c.usedmoney <= info.pl_money or c.usedmoney is null)");
        sql.append(
                " and not exists(select 1 from cms_provide_loan_send send where  info.bill_code = send.loan_code  and (send.status = 97) ) ");
        sql.append(" and (send.BANK_FLOW_SIGN=0 or send.BANK_FLOW_SIGN is null )");
        return getDynamicBySQL(input, sql.toString(), LoanSendVO.class, null, false);
    }
    /**
     * 单位校验使用-待记账
     *
     * @Param corpId
     * @return
     */
    public Integer verifyCorpDisableByCorpIdTally(String corpId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(info.id) ");
        sql.append(" from cms_provide_loan_info info  ");
        sql.append(" left join cms_provide_loan_send s on s.loan_code = info.bill_code ");
        sql.append(" left join cms_provide_loan_repay r on s.bill_code = r.send_code");
        //TODO 录入是从bt_bank 获取数据的
//        sql.append(" left join bt_bank bank on bank.sys_bank_code = info.bank_id ");
        sql.append(" where 1= 1 ");
        sql.append(" and s.status = 97 and (s.tally_Sign ='' or s.tally_Sign ='0' or s.tally_sign is null)");
        sql.append(" and info.corp_id = :corpId");
        Map<String, Object> param = new HashMap<>();
        param.put("corpId", corpId);
        return Integer.valueOf(createSQLQuery(sql.toString(), param).uniqueResult().toString());
    }
    /**
     * 根据单位查询是否存在相关业务单据
     *
     * @param corpId
     * @return
     */
    public int queryBusBillByCorpId(String corpId, String status, String bizStatus) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select count(*) from cms_provide_loan_send  where 1=1 and corp_id=:corpId ");
        if (EmptyUtils.isNotEmpty(bizStatus)) {
            sql.append(" and BIZ_STATUS in(:bizStatus) ");
            params.put("bizStatus", bizStatus.split(","));
        }
        if (EmptyUtils.isNotEmpty(status)) {
            sql.append(" and status in(:status) ");
            params.put("status", status.split(","));
        }
        params.put("corpId", corpId);
        return Integer.valueOf(createSQLQuery(sql.toString(), params).uniqueResult().toString());
    }
    /**
     * 根据单位查询未还款完成的数据
     *
     * @param corpId
     * @return
     */
    public int queryRepayingDataByCorpId(String corpId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(*) from ");
        sql.append(" (select * ").append(" from cms_provide_loan_send send ")
                .append(" left join (select sum(repay_money) as sum_money, send_code ")
                .append(" from cms_provide_loan_repay plr ").append(" where plr.status <> -2 ")
                .append(" group by send_code) repaySum ").append(" on repaySum.send_code = send.bill_code ")
                .append(" where send.status in (102, 104) ")
                .append(" and ((repaySum.sum_money is null and send.money > 0) or ")
                .append(" send.money > repaySum.sum_money) ").append(" and not exists (select 1 ")
                .append(" from cms_provide_loan_repay plr ").append(" where plr.send_code = send.bill_code ")
                .append(" and (plr.status in (97, 0, 95, 11))) and send.corp_id=:corpId ) aa ");
        Map<String, Object> params = new HashMap<>();
        params.put("corpId", corpId);
        return Integer.valueOf(createSQLQuery(sql.toString(), params).uniqueResult().toString());
    }
    /**
     * 查询发放账务数据 --待记账
     */
    public List<CmsProvideSendDTO> queryTallyProvideSend() {
        StringBuilder sql = new StringBuilder();
        //TODO 补充单位名称
        sql.append(" select s.id as id,s.money as money,info.corp_id as corpId,info.corp_name as corpName ");
        sql.append(" from cms_provide_loan_info info  ");
        sql.append(" left join cms_provide_loan_send s on s.loan_code = info.bill_code ");
        sql.append(" left join cms_provide_loan_repay r on s.bill_code = r.send_code");
        //TODO 录入是从bt_bank 获取数据的
//        sql.append(" left join bt_bank bank on bank.sys_bank_code = info.bank_id ");
        sql.append(" where 1= 1 ");
        sql.append(" and s.status = 97 and (s.tally_Sign ='' or s.tally_Sign ='0' or s.tally_sign is null)");
        //TODO 程序上已排除了记账数据
//        sql.append(" and not exists( select 1 from fc_tally_remind_dtl d where d.bill_id = s.id ) ");
        return listBySQLAliasToBean(sql.toString(), null, CmsProvideSendDTO.class);
    }
    /**
     * 查询还款账务数据 --记账
     */
    public List<CmsProvideLoanRepayDTO> queryTallyProvideRepay(String onlineSign) {
        StringBuilder sql = new StringBuilder();
        //TODO 补充单位名称
        sql.append(
                " select r.id as id,bt_nvl(r.repay_money,0) as repayMoney ,bt_nvl(r.repay_accrual,0) as repayAccrual,bt_nvl(r.online_money,0) as onlineMoney,info.corp_id as corpId,info.corp_name as corpName ");
        sql.append(" from cms_provide_loan_info info  ");
        sql.append(" left join cms_provide_loan_send s on info.bill_code = s.loan_code ");
        sql.append(" left join cms_provide_loan_repay r on s.bill_code = r.send_code");
        //TODO 录入是从bt_bank 获取数据的
//        sql.append(" left join bt_bank bank on bank.sys_bank_code = info.bank_id ");
        sql.append(" where 1= 1 ");
        sql.append(" and r.status = 97 and (r.tally_Sign ='' or r.tally_Sign ='0' or r.tally_sign is null)");
        if (EmptyUtils.isNotEmpty(onlineSign)) {
            sql.append(" and r.online_sign = " + onlineSign);
        }
        //TODO 程序上已排除了记账数据
//        sql.append(" and not exists( select 1 from fc_tally_remind_dtl d where d.bill_id = r.id ) ");
        return listBySQLAliasToBean(sql.toString(), null, CmsProvideLoanRepayDTO.class);
    }
    /**
     * <br>
     * <b>功能：</b> 查询借款融资情况 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/12/8 0008 <br>
     */
    public List<FinanceLoanDTO> queryProvideFinance(List<String> corpAuth, String curId, Date queryDate) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select '01' as busType, send.corp_id , send.corp_name, ");
        sql.append("(bt_nvl_num(sum((BT_NVL_NUM(send.money, 0) - BT_NVL_NUM(repay.sum_repay_money, 0))),0)) as money, ");
//        sql.append(" bt_nvl_num(sd.money,0) as money, ");
        sql.append(" (bt_nvl_num(sb.bondMoney1,0) -  bt_nvl_num(sp.bondMoney2, 0)) as bondMoney ");
        sql.append(" from ");
        //TODO 无用表
//        sql.append(" sys_corp sc ");
//        sql.append(" left join (select ");
//        sql.append(" (select ");
        //TODO 补充单位名称
//        sql.append(" info.corp_id as corpId, info.corp_name as corpName, ");
//        sql.append(" sum((BT_NVL_NUM(send.money, 0) - BT_NVL_NUM(repay.sum_repay_money, 0))) as money ");
        sql.append(" cms_provide_loan_send send ");
        sql.append(" inner join cms_provide_loan_info info on send.loan_code = info.bill_code ");
        sql.append(" left join (select cr.send_code, ");
        sql.append(" sum(cr.repay_money) as sum_repay_money ");
        sql.append("   from cms_provide_loan_repay cr ");
        sql.append("   where cr.status >= 102 ");
        if (!EmptyUtils.isEmpty(queryDate)) {
            sql.append("and cr.repay_date <= :queryDate ");
            params.put("queryDate", queryDate);
        }
        sql.append("   group by cr.send_code) repay on repay.send_code = send.bill_code ");
        sql.append(" left join (select ");
        sql.append(" info2.corp_id as corpId, ");
        sql.append(" sum(BT_NVL_NUM(info2.bailmoney, 0)) as bondMoney1 ");
        sql.append(" from cms_provide_loan_info info2 ");
        sql.append("   where info2.status > -2 ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "info2.corp_id"));
        if (EmptyUtils.isNotEmpty(curId)) {
            sql.append(" and info2.bond_cur_id = :curId ");
            params.put("curId", curId);
        }
        if (!EmptyUtils.isEmpty(queryDate)) {
            sql.append(" and info2.pl_date <= :queryDate ");
            params.put("queryDate", queryDate);
        }
        sql.append(" group by info2.corp_id) sb on sb.corpId = send.corp_id ");
//        sql.append(" group by info2.corp_id ) sb ");
        sql.append(" left join (select ");
        sql.append(" info3.corp_id as corpId, ");
        sql.append(" sum(BT_NVL_NUM(info3.bailmoney, 0)) as bondMoney2 ");
        sql.append(" from cms_provide_loan_info info3, ");
        sql.append(" cms_provide_loan_send send3, ");
        sql.append(" (select send_code,max(repay_date) as repay_date ");
        sql.append(" from cms_provide_loan_repay group by send_code) repay3 ");
        sql.append(
                " where info3.bill_code = send3.loan_code and repay3.send_code = send3.bill_code and info3.status = 110 ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "info3.corp_id"));
        if (EmptyUtils.isNotEmpty(curId)) {
            sql.append(" and info3.bond_cur_id = :curId ");
            params.put("curId", curId);
        }
        if (!EmptyUtils.isEmpty(queryDate)) {
            sql.append(" and repay3.repay_date <= :queryDate ");
        }
        sql.append(" group by info3.corp_id ) sp on sp.corpId = send.corp_id ");
        sql.append(" where 1 = 1 ");
        sql.append(" and send.status >= 102 ");
        sql.append(" and BT_NVL_NUM(send.money, 0) > BT_NVL_NUM(repay.sum_repay_money, 0) ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "info.corp_id"));
        if (EmptyUtils.isNotEmpty(curId)) {
            sql.append(" and info.cur_id = :curId ");
            params.put("curId", curId);
        }
        if (!EmptyUtils.isEmpty(queryDate)) {
            sql.append(" and send.send_date <= :queryDate ");
            params.put("queryDate", queryDate);
        }
//        sql.append(" group by info.corp_id ) sd on sd.corpId = send.corp_id ");

//        sql.append(" group by info3.corp_id ) sp ");
        sql.append(" group by send.corp_id, send.corp_name,sb.bondMoney1,sp.bondMoney2 ");
        sql.append(" order by send.corp_id ");
        return listBySQLAliasToBean(sql.toString(), params, FinanceLoanDTO.class);
    }
    /**
     * <br>
     * <b>功能：</b> 查询借款利息净支出情况 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/12/8 0008 <br>
     */
    public List<FinanceLoanDTO> queryInterestFinance(List<String> corpAuth, String curId, Date queryDate) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select '04' as busType, sd.corpId , sd.corpName, sd.money ");
        sql.append(" from ");
        sql.append(" (select ");
        //TODO 补充单位名称
        sql.append(" info.corp_id as corpId,info.corp_name as corpName, ");
        sql.append(" sum(BT_NVL_NUM(repay.sum_repay_accrual, 0)) as money ");
        sql.append(" from cms_provide_loan_send send ");
        sql.append(" inner join cms_provide_loan_info info on send.loan_code = info.bill_code ");
        sql.append(" left join (select cr.send_code, ");
        sql.append(" sum(cr.repay_accrual) as sum_repay_accrual ");
        sql.append("   from cms_provide_loan_repay cr ");
        sql.append("   where cr.status >= 102 ");
        if (!EmptyUtils.isEmpty(queryDate)) {
            sql.append("and cr.repay_date <= :queryDate ");
            params.put("queryDate", queryDate);
        }
        sql.append("   group by cr.send_code) repay on repay.send_code = send.bill_code ");
        sql.append(" where 1 = 1 and info.status > -2 ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "info.corp_id"));
        if (EmptyUtils.isNotEmpty(curId)) {
            sql.append(" and info.cur_id = :curId ");
            params.put("curId", curId);
        }
        if (!EmptyUtils.isEmpty(queryDate)) {
            sql.append(" and send.send_date <= :queryDate ");
            params.put("queryDate", queryDate);
        }
        sql.append(" group by info.corp_id,info.corp_name ) sd ");
        sql.append(" where 1 = 1 ");
//        sql.append(" where sd.corpId = sc.id ");
        sql.append(" order by sd.corpId ");
        return listBySQLAliasToBean(sql.toString(), params, FinanceLoanDTO.class);
    }
    /**
     * <br>
     * <b>功能：</b> 查询借款各融资品种的融资情况 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/12/8 0008 <br>
     */
    public List<FinanceTypeLoanDTO> queryProvideTypeFinance(List<String> corpAuth, String curId, Date queryDate) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select ");
        sql.append(" type.code as typeCode, type.name as typeName,  ");
        sql.append(" bt_nvl_num(sd.money,0) as money, ");
        sql.append(" (bt_nvl_num(sb.bondMoney1,0) -  bt_nvl_num(sp.bondMoney2, 0)) as bondMoney ");
        sql.append(" from cms_loan_type type ");
        sql.append(" left join (select ");
        sql.append(" info.pl_class as plClass, ");
        sql.append(" sum((BT_NVL_NUM(send.money, 0) - BT_NVL_NUM(repay.sum_repay_money, 0))) as money ");
        sql.append(" from cms_provide_loan_send send ");
        sql.append(" inner join cms_provide_loan_info info on send.loan_code = info.bill_code ");
        sql.append(" left join (select cr.send_code, ");
        sql.append(" sum(cr.repay_money) as sum_repay_money ");
        sql.append("   from cms_provide_loan_repay cr ");
        sql.append("   where cr.status >= 102 ");
        if (!EmptyUtils.isEmpty(queryDate)) {
            sql.append(" and cr.repay_date <= :queryDate ");
            params.put("queryDate", queryDate);
        }
        sql.append("   group by cr.send_code) repay on repay.send_code = send.bill_code ");
        sql.append(" left join cms_loan_type type on type.code = info.pl_class ");
        sql.append("  and type.status != -2 and type = '2' ");
        sql.append(" where send.status >= 102 ");
        sql.append(" and BT_NVL_NUM(send.money, 0) > BT_NVL_NUM(repay.sum_repay_money, 0) ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "info.corp_id"));
        if (EmptyUtils.isNotEmpty(curId)) {
            sql.append(" and info.cur_id = :curId ");
            params.put("curId", curId);
        }
        if (!EmptyUtils.isEmpty(queryDate)) {
            sql.append(" and send.send_date <= :queryDate ");
            params.put("queryDate", queryDate);
        }
        sql.append(" group by info.pl_class) sd on sd.plClass = type.code ");
        sql.append(" left join (select ");
        sql.append(" info2.pl_class as plClass, ");
        sql.append(" sum(BT_NVL_NUM(info2.bailmoney, 0)) as bondMoney1 ");
        sql.append(" from cms_provide_loan_info info2 ");
        sql.append("   where info2.status > -2 ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "info2.corp_id"));
        if (EmptyUtils.isNotEmpty(curId)) {
            sql.append(" and info2.bond_cur_id = :curId ");
            params.put("curId", curId);
        }
        if (!EmptyUtils.isEmpty(queryDate)) {
            sql.append(" and info2.pl_date <= :queryDate ");
        }
        sql.append(" group by info2.pl_class ) sb on type.code = sb.plClass ");
        sql.append(" left join (select ");
        sql.append(" info3.pl_class as plClass, ");
        sql.append(" sum(BT_NVL_NUM(info3.bailmoney, 0)) as bondMoney2 ");
        sql.append(" from cms_provide_loan_info info3, ");
        sql.append(" cms_provide_loan_send send3, ");
        sql.append(" (select send_code,max(repay_date) as repay_date ");
        sql.append(" from cms_provide_loan_repay group by send_code) repay3 ");
        sql.append(
                " where info3.bill_code = send3.loan_code and repay3.send_code = send3.bill_code and info3.status = 110 ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "info3.corp_id"));
        if (EmptyUtils.isNotEmpty(curId)) {
            sql.append(" and info3.bond_cur_id = :curId ");
            params.put("curId", curId);
        }
        if (!EmptyUtils.isEmpty(queryDate)) {
            sql.append(" and repay3.repay_date <= :queryDate ");
        }
        sql.append(" group by info3.pl_class ) sp on type.code = sp.plClass ");
        sql.append(" where type = '2'");
        sql.append(" order by type.code ");
        return listBySQLAliasToBean(sql.toString(), params, FinanceTypeLoanDTO.class);
    }
}
