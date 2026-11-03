package com.bytter.finance.domain.precative.dao;
import com.bytter.biz.finacial.api.constant.FinacialManualConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.basedata.entity.CmsRepaymentSchedule;
import com.bytter.finance.domain.basedata.vo.CmsPrecativeLoanSendCheckVO;
import com.bytter.finance.domain.precative.entity.CmsPrecativeLoanSend;
import com.bytter.finance.domain.precative.vo.CmsPrecativeLoanInfoVo;
import com.bytter.finance.domain.precative.vo.PrecativeLoanSendVo;
import com.bytter.finance.dto.CmsPrecativeLoanSendDTO;
import com.bytter.finance.enums.PrecativeMessageEnum;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: 委托贷款发放表
 * @author onlineGenerator
 * @date 2017-07-29 10:45:29
 * @version 1.0
 *
 */
@Repository
public class CmsPrecativeLoanSendDao extends BaseDao<CmsPrecativeLoanSend, String> {
    /**
     * 查询发放信息
     */
    public DataTablesOutput getLoanSend(DataTablesInput dataTablesInput, List<String> corpAuth) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = getSql();
        sb.append(
                " from cms_precative_loan_info a left join cms_precative_loan_send b on  a.bill_code = b.father_code  and b.status >= -1 and b.status < 102  ");
        sb.append(
                " left join (select father_code, sum(money) usedmoney  from cms_precative_loan_send where status >= 102 group by father_code) c on c.father_code =  a.bill_code ");
        sb.append(
                " left join (select father_code, sum(money) sendApproveMoney  from cms_precative_loan_send  where (status !=96 and status != 97 and (status >= 95 or status>=10)) group by father_code) d on d.father_code = a.bill_code ");
        sb.append(
                " where a.status = 96 and (b.status <> 97 or b.status is null) and (c.usedmoney < a.money or c.usedmoney is null) ");
        sb.append(" and ").append(getSQLIn(corpAuth, 900, "a.precative_id"));
        return getDynamicBySQL(dataTablesInput, sb.toString(), PrecativeLoanSendVo.class, null, false);
    }
    /**
     * 查询发放账务数据
     */
    public DataTablesOutput getPrecativeDataTables(DataTablesInput dataTablesInput, List<String> corpAuth, String opType, ICurrentUser user) {
        StringBuilder sb = new StringBuilder();
        sb.append(getSql());
        sb.append(
                " from cms_precative_loan_info a left join cms_precative_loan_send b on a.bill_code = b.father_code ");
        sb.append(
                " left join (select father_code, sum(money) usedmoney  from cms_precative_loan_send where status >= 102 group by father_code) c on c.father_code =  a.bill_code ");
        sb.append(
                " left join (select father_code, sum(money) sendApproveMoney  from cms_precative_loan_send  where (status !=96 and (status >= 95 or status>=10)) group by father_code) d on d.father_code = a.bill_code ");
        if (FinacialManualConstant.MANUAL_STATUS_INIT.equals(opType)) {
            sb.append(
                    " where b.status = 97 and (b.tally_Sign ='' or b.tally_Sign ='0' or b.tally_Sign='6' or b.tally_sign is null)");
        } else if (FinacialManualConstant.MANUAL_STATUS_EDIT.equals(opType)) {
            sb.append(" where b.status = 97 and (b.tally_Sign in (5,4,3)) and b.tally_code = '").append(user.getId())
                    .append("'");
        } else if (FinacialManualConstant.MANUAL_STATUS_CHECK.equals(opType)) {
            sb.append(" where b.status = 97 and (b.tally_Sign ='3') and b.tally_code != '").append(user.getId())
                    .append("'");
        } else {
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_TALLYDATA_ERROR);
        }
        if(!StringUtils.isNotEmpty(dataTablesInput.getFormdata().get("eqs_precativeId"))){
            sb.append(" and (" + getSQLIn(corpAuth, 900, "a.corp_Id") + ")");
        }
        return getDynamicBySQL(dataTablesInput, sb.toString(), PrecativeLoanSendVo.class, null, false);
    }
    /**
     * 获取查询语句
     */
    public StringBuilder getSql() {
        StringBuilder sb = new StringBuilder();
        sb.append(
                " select a.id as infoId , b.id as id ,a.corp_Id as corpId , a.loan_send_out_type as loanSendOutType , a.bill_code as  infoBillCode , a.biz_status as infoBizStatus , a.precative_id as precativeId , ");
        sb.append(
                " a.cur_id as curId , a.term as term, a.money as infoMoney , a.create_date as infoCreateDate, a.status as infoStatus , ");
        sb.append(
                " a.pl_type as plType , a.start_date as startDate , a.end_date as endDate , a.loan_opp as loanOpp , a.contract_code as infoContractCode, b.bill_code as billCode  ,");
        sb.append(
                " b.float_way as floatWay , b.Repay_Type as RepayType , b.biz_status as bizStatus,  b.Interest_Way as InterestWay , ");
        sb.append(
                " b.Rate_Entries_Effect_Date as RateEntriesEffectDate , a.Reference_Rate_Type as ReferenceRateType , b.deadline_date as deadlineDate , ");
        sb.append(
                " b.Repay_Cycle as RepayCycle ,b.send_Time as sendTime, b.Points_Proportion_Select as PointsProportionSelect , ");
        sb.append(
                " b.Points_Proportion_Text as PointsProportionText , b.Rate as  Rate , b.Overdue_Rate as OverdueRate , ");
        sb.append(
                " b.contract_code as contractCode, b.money as money, b.send_date as sendDate , b.status as status, b.father_code as fatherCode , b.TALLY_SIGN  as tallySign, ");
        sb.append(
                " a.tern_unit as ternUnit, BT_NVL_NUM(d.sendApproveMoney,0) as sendApproveMoney , (a.money  - BT_NVL_NUM(d.sendApproveMoney,0)) as issuedMoney, ");
        //TODO 补充名称相关信息
        sb.append(
                " a.cur_name as curName,a.precative_name as precativeName,a.loanopp_name as loanOppName,a.corp_name as corpName ");
        return sb;
    }
    /***
     * 判断发放数据能否打回
     */
    public boolean isCanCallBack(String fatherCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "select 1 from cms_precative_loan_send a where a.father_code=:fatherCode and (a.status !=96 and a.status>=95)";
        params.put("fatherCode", fatherCode);
        List list = createSQLQuery(sql, params).list();
        return EmptyUtils.isEmptyList(list);
    }

    /**
     * 根据id修改还款方式
     *
     * @param id
     */
    public void updateRepayType(String id, String repayType) {
        Map<String, String> params = new HashMap<String, String>();
        StringBuilder sql = new StringBuilder();
        sql.append("update cms_precative_loan_send set repay_type =:repayType where id=:id");
        params.put("repayType", repayType);
        params.put("id", id);
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    /***
     * 根据Info表billCode查询发放信息（查询实体，删除时候用）
     */
    public List<CmsPrecativeLoanSend> getLoanSendByFatherCode(String fatherCode) {
        return listBy("fatherCode", fatherCode);
    }
    public CmsPrecativeLoanSend getByBillCode(String billCode) {
        return getUniqueBy("billCode", billCode);
    }
    /***
     * 根据Info表billCode查询所有发放信息
     */
    public List<PrecativeLoanSendVo> getLoanSendVoByFatherCode(String fatherCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        //TODO 暂时补充名称相关信息
        sb.append(" select a.create_date as createDate, a.id as id, a.bill_code as billCode, a.contract_code as contractCode, ");
        sb.append(" a.money as money, a.send_date as sendDate, a.rate as rate, a.term as term, a.tern_unit as ternUnit, ");
        sb.append(" a.biz_status as bizStatus, a.status as status, a.father_code as fatherCode, a.precative_id as precativeId, ");
        sb.append(" info.cur_name as curName, info.precative_name as precativeName, info.loanopp_name as loanOppName, info.corp_name as corpName ");
        sb.append(" from cms_precative_loan_send a ");
        sb.append("left join cms_precative_loan_info info on info.bill_code = a.father_code where a.father_Code=:fatherCode ");
        params.put("fatherCode", fatherCode);
        return listBySQLAliasToBean(sb.toString(), params, PrecativeLoanSendVo.class);
    }
    /**
     * 根据Info表billCode查询sendVo
     */
    public List<PrecativeLoanSendVo> getPrecativeLoanSendVoByBillCode(String billCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append(getSql());
        sb.append(
                " from cms_precative_loan_info a left join cms_precative_loan_send b on  a.bill_code = b.father_code  and b.status >= -1 and b.status < 102  ");
        sb.append(
                " left join (select father_code, sum(money) usedmoney  from cms_precative_loan_send where status >= 102 group by father_code) c on c.father_code =  a.bill_code ");
        sb.append(
                " left join (select father_code, sum(money) sendApproveMoney  from cms_precative_loan_send  where (status !=96 and (status >= 95 or status>=10)) group by father_code) d on d.father_code = a.bill_code ");
        sb.append(" where 1=1 and  a.bill_code =:billCode ");
        params.put("billCode", billCode);
        return listBySQLAliasToBean(sb.toString(), params, PrecativeLoanSendVo.class);
    }
    /***
     * 根据send表billCode查询发放sendVo
     */
    public PrecativeLoanSendVo getSendByBillCode(String billCode, String type) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append(" select a.float_way floatWay,a.interest_way interesWays,a.reference_rate_type referenceRateType,a.repay_cycle repayCycle,a.points_proportion_select pointsProportionSelect," +
                "a.*, a.create_date as createDate, drd.maxDate as deferredRepayDate,info.create_by as infoCreateBy,info.update_by as infoUpdateBy, ");
        //TODO 补充名称相关信息
        sb.append(" info.cur_name as curName,info.precative_name as precativeName,info.loanopp_name as loanOppName,info.corp_name as corpName, t.repay_name as repayTypes, ");
        sb.append(" BT_NVL_NUM(d.sendApproveMoney,0) as sendApproveMoney from cms_precative_loan_send a ");
        sb.append(" left join cms_precative_loan_info info on info.bill_code = a.father_code");
        sb.append(" left join cms_repay_type t on a.repay_type = t.id ");
        sb.append(" left join (select father_code, sum(money) sendApproveMoney  from cms_precative_loan_send  where (status !=96 and (status >= 95 or status>=10)) group by father_code) d on d.father_code = a.father_code ");
        sb.append(" left join ( select db.li_code,max(db.deferred_repay_date) as maxDate from cms_precative_loan_deferred db where db.status = 110 group by db.li_code ) drd");
        sb.append(" on drd.li_code = a.bill_code");
//        sb.append(" left join tb_base_rate base on base.id = a.reference_rate_type ");// 基准利率类型
//        sb.append(" left join tb_credit_period period on period.id = a.repay_cycle ");// 周期
        if ("id".equals(type)) {
            sb.append(" where a.id=:id ");
            params.put("id", billCode);
        } else {
            sb.append(" where a.bill_code=:billCode  ");
            params.put("billCode", billCode);
        }
        return getBySQLAliasToBean(sb.toString(), params, PrecativeLoanSendVo.class);
    }
    /**
     * 根据fatherCode查询send信息（查询发放次数）
     */
    public List<PrecativeLoanSendVo> getSendByFatherCode(String fatherCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append(
                " select a.bill_code as  infoBillCode , a.precative_id as precativeId , a.cur_id as curId , a.term as term, a.money as infoMoney , a.create_date as infoCreateDate, a.status as infoStatus , ");
        sb.append(
                " a.pl_type as plType , a.start_date as startDate , a.end_date as endDate , a.loan_opp as loanOpp , a.contract_code as infoContractCode, b.bill_code as billCode , ");
        //TODO 补充名称相关信息
        sb.append(" a.cur_name as curName,a.precative_name as precativeName,a.loanopp_name as loanOppName,a.corp_name as corpName, ");
        sb.append(
                " b.contract_code as contractCode, b.money as money, b.send_date as sendDate , b.status as status, b.father_code as fatherCode , a.tern_unit as ternUnit ");
        sb.append(
                " from cms_precative_loan_send b inner join cms_precative_loan_info a on  a.bill_code = b.father_code ");
        sb.append(" where b.father_Code =:fatherCode and b.status <> 0 ");// 过滤暂存的数据
        params.put("fatherCode", fatherCode);
        return listBySQLAliasToBean(sb.toString(), params, PrecativeLoanSendVo.class);
    }
    /**
     * 根据申请编号获取放款次数
     */
    public Integer getCmsLoanSendCountByloanCode(String fatherCode) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(
                "select count(1) sendCount from cms_precative_loan_send b inner join cms_precative_loan_info a on  a.bill_code = b.father_code where b.father_Code =:fatherCode  and b.status >=102 ");
        params.put("fatherCode", fatherCode);
        BigDecimal sendCount = (BigDecimal) createSQLQuery(sql.toString(), params).uniqueResult();
        return sendCount.intValue();
    }
    /**
     * 判断该委托贷款是否存在业务中的发放记录
     *
     * @return 有1数据返回 true 无数据返回 false 多条数据异常
     */
    public boolean isExsitBusinessSend(String fatherCode) {
        boolean flag = false;
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "select 1 from cms_precative_loan_send a where a.father_code=:fatherCode and (a.status <102 and a.status!=-1)";
        params.put("fatherCode", fatherCode);
        List list = createSQLQuery(sql, params).list();
        if (!EmptyUtils.isEmptyList(list) && list.size() == 1) {
            flag = true;
        } else if (!EmptyUtils.isEmptyList(list) || list.size() == 0) {
            flag = false;
        } else {
            // 委托贷款单号不允许同时存在多条业务中的数据,数据异常
        }
        return flag;
    }
    /***
     * 判断发放编号是否唯一
     */
    public boolean isExsitContractCode(String contractCode, String billCode) {
        boolean flag = false;
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder(
                "select 1 from cms_precative_loan_send a where a.contract_Code=:contractCode  ");
        params.put("contractCode", contractCode);
        if (!EmptyUtils.isEmpty(billCode)) {
            sql.append(" and a.bill_code !=:billCode ");
            params.put("billCode", billCode);
        }
        List list = createSQLQuery(sql.toString(), params).list();
        if (!EmptyUtils.isEmptyList(list) && list.size() == 1) {
            flag = true;
        } else if (!EmptyUtils.isEmptyList(list) || list.size() == 0) {
            flag = false;
        } else {
            // 委托贷款发放编号已存在多条数据,数据异常
        }
        return flag;
    }
    /***
     * 根据发放编号查询Info表
     */
    public CmsPrecativeLoanInfoVo getInfoBySendCode(String billCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append(
                " select a.id as id , a.precative_id as precativeId , a.corp_id as corpId , a.is_bank as isBank , a.bank_code as bankCode , a.contract_Code as contractCode ,");
        sb.append(
                " a.bill_code as billCode , a.loan_Opp as loanOpp , a.cur_Id as curId , a.start_date as startDate , a.end_date as endDate , a.term as term , a.tern_unit as ternUnit , ");
        sb.append(
                " a.rate as rate , a.money as money , a.poundage_rate as poundageRate , a.poundage as poundage , a.pl_Type as plType , a.surety_Explain as suretyExplain  ,a.gua_Surety_Code as guaSuretyCode  , ");
        sb.append(
                " a.status as status , a.purpose as purpose , a.rmk as rmk , a.refuse_Why as refuseWhy , a.repay_Type as repayType ,a.float_Way as floatWay , a.create_Date as createDate ,  ");
        sb.append(
                " a.update_Date as updateDate ,a.loan_send_out_type as loanSendOutType , a.interest_way as interestWay , a.overdue_rate as overdueRate , a.rate_entries_effect_date as rateEntriesEffectDate ,");
        //TODO 补充名称相关信息
        sb.append(" a.bank_name as bankName,a.cur_name as curName,a.precative_name as precativeName,a.loanopp_name as loanOppName,a.corp_name as corpName, ");
        sb.append(
                " a.skip_loan_send_out as skipLoanSendOut , a.repay_cycle as repayCycle , a.biz_status as bizStatus ");
        sb.append(
                " from cms_precative_loan_info a left join cms_precative_loan_send s on a.bill_code = s.father_code ");
        sb.append(" where s.bill_code=:billCode  ");
        params.put("billCode", billCode);
        return getBySQLAliasToBean(sb.toString(), params, CmsPrecativeLoanInfoVo.class);
    }
    /**
     * 加载审核列表数据
     *
     * @param input
     * @return
     */
    public DataTablesOutput dataTableApprove(DataTablesInput input, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        //TODO 补充名称相关信息
        sql.append(
                " SELECT a.cur_id as curId,a.cur_name as curName,a.precative_name as precativeName,a.loanopp_name as loanOppName,a.corp_name as corpName,b.*  FROM ");
        sql.append(
                " cms_precative_loan_send b ,cms_precative_loan_info a where a.bill_code=b.father_code ");
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sql.append(" and ").append(getSQLIn(ids, 900, "b.id"));
        }
        return getDynamicBySQL(input, sql.toString(), PrecativeLoanSendVo.class, "", false);
    }
    /**
     * 查询到期日之前的发放信息
     */
    public List<CmsPrecativeLoanSend> querySendListDTOs(Date endDate) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select s from CmsPrecativeLoanSend s where s.status>:Dstatus and s.status < :status  ");
        hql.append(" and (s.sendSign = :sendSign or s.sendSign is null) and s.deadlineDate <= :endDate ");
        hql.append(" order by s.deadlineDate ");
        params.put("sendSign", CommonConstant.SEND_SIGN_NO);
        params.put("Dstatus", CmsConstant.DELETED);
        params.put("status", CmsConstant.FINISH);
        params.put("endDate", endDate);
        return listByHQL(hql.toString(), params);
    }
    /**
     * 查询委托贷款发放到期<br>
     */
    public List<CmsPrecativeLoanSendCheckVO> getPrecativeLoanInfoByEndDate(String precativeId, Date endDate) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
//        sql.append(" select s.bill_code as billCode,s.loan_acc as loanAcc, sc.name as precativeId,sp.name as loanOpp,");
        //TODO 补充名称相关信息
        sql.append(" select s.bill_code as billCode,s.loan_acc as loanAcc, i.precative_name as precativeId,i.loanopp_name as loanOpp,");
        sql.append(" s.contract_Code as contractCode ,");
        sql.append(" s.money as money,s.send_Date as startDate,s.deadline_Date as endDate, s.rate as rate, ");
//        sql.append(" s.create_date as loanDate,bc.cur_name as curName ");
        sql.append(" s.create_date as loanDate,i.cur_name as curName ");
        sql.append(
                " from cms_precative_loan_send s left join cms_precative_loan_info i on s.father_code = i.bill_code ");
//        sql.append(" left join sys_corp sc on sc.id = i.precative_id ");
//        sql.append(" left join sys_corp sp on sp.id = i.loan_opp ");
//        sql.append(" left join BT_CURRENCY bc on bc.id = i.cur_id ");
        sql.append(" where (s.status > :status0 and s.status < :status1 ) ");
        if (EmptyUtils.isNotEmpty(precativeId)) {
            sql.append("and i.corp_id = :precativeId ");
            params.put("precativeId", precativeId);
        }
        if (!EmptyUtils.isEmpty(endDate)) {
            sql.append("and s.deadline_Date <= :endDate ");
            params.put("endDate", endDate);
        }
        params.put("status0", CmsConstant.DELETED);
        params.put("status1", CmsConstant.FINISH);
        return listBySQLAliasToBean(sql.toString(), params, CmsPrecativeLoanSendCheckVO.class);
    }
    /**
     * 判断是否可以发放
     */
    public boolean validateSendStatus(String fatherCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder(
                "select count(1) from cms_precative_loan_send s where s.father_Code=:fatherCode and s.status > :status0 and s.status < :status1 ");
        params.put("fatherCode", fatherCode);
        params.put("status0", CmsConstant.DELETED);
        params.put("status1", CmsConstant.COMPELETOTAL);
        BigDecimal count = (BigDecimal) createSQLQuery(sql.toString(), params).uniqueResult();
        if (count.intValue() > 0) {
            return true;
        }
        return false;
    }
    /**
     * 发放次数是否正确
     */
    public boolean validateSendTime(String fatherCode, int sendTime) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder(
                "select count(1) from cms_precative_loan_send s where s.father_Code=:fatherCode and s.status>=:status and s.send_time = :sendTime ");
        params.put("fatherCode", fatherCode);
        params.put("status", CmsConstant.COMPELETOTAL);
        params.put("sendTime", sendTime);
        BigDecimal count = (BigDecimal) createSQLQuery(sql.toString(), params).uniqueResult();
        if (count.intValue() > 0) {
            return true;
        }
        return false;
    }
    /**
     * 根据贷款单号获取已发放金额
     */
    public BigDecimal getSendedMoney(String fatherCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder(
                "select BT_NVL_NUM(sum(s.money),0) as sendedMoney from cms_precative_loan_send s where s.father_Code = :fatherCode and s.status != "
                        + CmsConstant.DELETED);
        params.put("fatherCode", fatherCode);
        return (BigDecimal) createSQLQuery(sql.toString(), params).uniqueResult();
    }
    /**
     * 单位校验使用-待记账
     *
     * @Param corpId
     * @return
     */
    public Integer verifyCorpDisableByCorpId(String corpId) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select count(a.id) ");
        sb.append(
                " from cms_precative_loan_info a left join cms_precative_loan_send b on a.bill_code = b.father_code ");
        sb.append(
                " left join (select father_code, sum(money) usedmoney  from cms_precative_loan_send where status >= 102 group by father_code) c on c.father_code =  a.bill_code ");
        sb.append(
                " left join (select father_code, sum(money) sendApproveMoney  from cms_precative_loan_send  where (status !=96 and (status >= 95 or status>=10)) group by father_code) d on d.father_code = a.bill_code ");
        sb.append(
                " where b.status = 97 and (b.tally_Sign ='' or b.tally_Sign ='0' or b.tally_Sign='6' or b.tally_sign is null)");
        sb.append(" and a.precative_id = :corpId");
        Map<String, Object> param = new HashMap<>();
        param.put("corpId", corpId);
        return Integer.valueOf(createSQLQuery(sb.toString(), param).uniqueResult().toString());
    }
    /** 查询待记账发放数据 */
    public List<CmsPrecativeLoanSendDTO> queryTallyPrecativeSend() {
        StringBuilder sb = new StringBuilder();
        sb.append(" select b.id as id,b.money as money,b.precative_id as precativeId ");
        sb.append(
                " from cms_precative_loan_info a left join cms_precative_loan_send b on a.bill_code = b.father_code ");
        sb.append(
                " left join (select father_code, sum(money) usedmoney  from cms_precative_loan_send where status >= 102 group by father_code) c on c.father_code =  a.bill_code ");
        sb.append(
                " left join (select father_code, sum(money) sendApproveMoney  from cms_precative_loan_send  where (status !=96 and (status >= 95 or status>=10)) group by father_code) d on d.father_code = a.bill_code ");
        sb.append(
                " where b.status = 97 and (b.tally_Sign ='' or b.tally_Sign ='0' or b.tally_Sign='6' or b.tally_sign is null)");
        //TODO 排除已记账记录
//        sb.append(" and not exists( select 1 from fc_tally_remind_dtl d where d.bill_id = b.id ) ");
        return listBySQLAliasToBean(sb.toString(), null, CmsPrecativeLoanSendDTO.class);
    }
    /***
     * 根据Info表billCode查询所有发放信息
     */
    public CmsPrecativeLoanSendDTO getPrecativeLoanSendVoByVo(CmsPrecativeLoanSendDTO vo) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        //TODO 暂时补充名称相关信息
        sb.append(" select a.*,info.cur_name as curName,info.precative_name as precativeName,info.loanopp_name as loanOppName,info.corp_name as corpName from cms_precative_loan_send a ");
        sb.append("left join cms_precative_loan_info info on info.bill_code = a.father_code where 1=1 ");
        if(StringUtils.isNotEmpty( vo.getLoanAcc())) {
            sb.append(" and a.loan_acc=:loanAcc");
            params.put("loanAcc", vo.getLoanAcc());
        }
        if(StringUtils.isNotEmpty( vo.getFatherCode())) {
            sb.append(" and a.father_Code=:fatherCode ");
            params.put("fatherCode", vo.getFatherCode());
        }
        return getBySQLAliasToBean(sb.toString(), params, CmsPrecativeLoanSendDTO.class);
    }
}
