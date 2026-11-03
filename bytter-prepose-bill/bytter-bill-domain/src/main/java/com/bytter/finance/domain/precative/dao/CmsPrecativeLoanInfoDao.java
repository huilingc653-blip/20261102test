package com.bytter.finance.domain.precative.dao;
import com.bytter.finance.domain.basedata.entity.CmsRepaymentSchedule;
import com.bytter.finance.domain.precative.entity.CmsPrecativeLoanInfo;
import com.bytter.finance.domain.precative.vo.CmsPrecativeLoanInfoVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: 委托贷款信息表
 * @author
 * @date 2017-07-20 14:00:39
 * @version 1.0
 *
 */
@Repository
public class CmsPrecativeLoanInfoDao extends BaseDao<CmsPrecativeLoanInfo, String> {
    /***
     * 查询委托贷款暂存和打回数据
     */
    public DataTablesOutput queryPrecativeLoanInfoDao(DataTablesInput input, String type, List<String> corpAuth) {
        StringBuilder sb = new StringBuilder();
        sb.append(getSql());
        sb.append(
                " from cms_precative_loan_info a where 1=1  ");
        if ("repair".equals(type)) {
            sb.append(" and a.status in( 95 ) ");
        } else if ("Edit".equals(type)) {
            sb.append(" and a.status in( -1 , 0 ) ");
        }
        sb.append(" and ").append(getSQLIn(corpAuth, 900, "a.precative_id"));
        return getDynamicBySQL(input, sb.toString(), CmsPrecativeLoanInfoVo.class, null, false);
    }
    public StringBuilder getSql() {
        StringBuilder sb = new StringBuilder();
        sb.append(
                " select a.id as id ,a.refuse_why as refuseWhy, a.float_way_next as floatWayNext, a.precative_id as precativeId , a.corp_id as corpId , a.is_bank as isBank , a.bank_code as bankCode , a.contract_code as contractCode ,");
        sb.append(
                " a.bill_code as billCode , a.loan_Opp as loanOpp , a.cur_Id as curId , a.start_date as startDate , a.end_date as endDate , a.term as term , a.tern_unit as ternUnit , ");
        sb.append(
                " a.rate as rate , a.money as money , a.poundage_rate as poundageRate , a.poundage as poundage , a.pl_Type as plType , a.surety_Explain as suretyExplain  ,a.gua_Surety_Code as guaSuretyCode  , ");
        sb.append(
                " a.repair_Op as repairOp , a.status as status , a.purpose as purpose , a.rmk as rmk , a.repay_Type as repayType ,a.float_Way as floatWay , a.create_Date as createDate ,  ");
        sb.append(
                " a.update_Date as updateDate ,a.loan_send_out_type as loanSendOutType , a.interest_way as interestWay , a.overdue_rate as overdueRate , a.rate_entries_effect_date as rateEntriesEffectDate ,");
        sb.append(
                " a.skip_loan_send_out as skipLoanSendOut , a.repay_cycle as repayCycle , a.biz_status as bizStatus ,BT_NVL_NUM(a.points_Proportion_Text , 0) as pointsProportionText ,a.points_Proportion_Select as pointsProportionSelect,a.annex_Name as annexName ,");
        sb.append(
                " a.reference_Rate_Type as referenceRateType , a.ratetimeoftakeeffect as ratetimeoftakeeffect,a.con_type_id as conTypeId,a.con_id as conId, ");
        sb.append(
                " a.bank_name as bankName , a.cur_name as curName,a.precative_name as precativeName,a.loanopp_name as loanOppName,a.corp_name as corpName ");
        return sb;
    }
    /***
     * 通过billCode查询Info信息
     */
    public CmsPrecativeLoanInfoVo getLoanInfoByBillCode(String billCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append(getSql());
        sb.append(" , base.base_type_name as baseTypeName ,period.period_name as periodName ");
        sb.append(" from cms_precative_loan_info a ");
        sb.append(" left join tb_base_rate base on base.id = a.reference_rate_type ");// 基准利率类型
        sb.append(" left join tb_credit_period period on period.id = a.repay_cycle   ");// 周期
        sb.append(" where 1=1 and bill_Code=:billCode ");
        params.put("billCode", billCode);
        return this.getBySQLAliasToBean(sb.toString(), params, CmsPrecativeLoanInfoVo.class);
    }

    /***
     * 通过billCode查询Info信息
     */
    public CmsPrecativeLoanInfoVo getLoanInfoById(String id) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append(getSql());
//        sb.append(
//                " , base.base_type_name as baseTypeName ,period.period_name as periodName , bt.bank_name as bankName ");
        sb.append(" from cms_precative_loan_info a ");
//        sb.append(" left join tb_base_rate base on base.id = a.reference_rate_type ");// 基准利率类型
//        sb.append(" left join tb_credit_period period on period.id = a.repay_cycle  ");// 周期
        sb.append(" where 1=1 and a.id=:id ");
        params.put("id", id);
        return this.getBySQLAliasToBean(sb.toString(), params, CmsPrecativeLoanInfoVo.class);
    }
    /***
     * 判断合同编号在数据库中是否存在
     */
    public boolean isExistsContractCode(String contractCode, String billCode) {
        boolean flag = false;
        StringBuilder hql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        hql.append("from CmsPrecativeLoanInfo a where a.contractCode =:contractCode");
        if (!EmptyUtils.isEmpty(billCode)) {
            hql.append(" and a.billCode !=:billCode ");
            params.put("billCode", billCode);
        }
        params.put("contractCode", contractCode);
        List<CmsPrecativeLoanInfo> list = createQuery(hql.toString(), params).list();
        if (!EmptyUtils.isEmptyList(list) && list.size() == 1) {
            flag = true;
        } else if (!EmptyUtils.isEmptyList(list) || list.size() == 0) {
            flag = false;
        } else {
            // 委托贷款发放编号已存在多条数据,数据异常
        }
        return flag;
    }
    /**
     * 加载审核列表数据
     * 
     * @param
     * @return
     */
    public DataTablesOutput<CmsPrecativeLoanInfoVo> dataTableApprove(DataTablesInput input, List<String> ids) {
        StringBuilder sb = new StringBuilder();
        sb.append(getSql());
        sb.append(" from cms_precative_loan_info a where 1=1 ");
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sb.append(" and ").append(getSQLIn(ids, 900, "id"));
        }
        return getDynamicBySQL(input, sb.toString(), CmsPrecativeLoanInfoVo.class, "", false);
    }
    /**
     * 根据传入的委托人查询出所有的借款人
     */
    public List<CmsPrecativeLoanInfo> getUnionCorps(List<String> preIds) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append(
                " select a.loan_opp as loanOpp , a.precative_id as precativeId from cms_precative_loan_info a where 1=1 ");
        sb.append(" and a.precative_id in (:precativeIds) or a.loan_opp in (:loanOpp)");
        params.put("precativeIds", preIds);
        params.put("loanOpp", preIds);
        return listBySQLAliasToBean(sb.toString(), params, CmsPrecativeLoanInfo.class);
    }
    /***
     * 通过repay表billCode查询Info信息
     */
    public CmsPrecativeLoanInfoVo getLoanInfoRepayBillCode(String billCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        sb.append(getSql());
        sb.append(" ,r.repay_accrual as repayAccrual");
        sb.append(" from cms_precative_loan_repay r left join cms_precative_loan_send b on r.send_code=b.bill_code ");
        sb.append(" left join cms_precative_loan_info a on b.father_code=a.bill_code ");
        sb.append(" where 1=1 ");
        sb.append(" and r.bill_code=:billCode ");
        params.put("billCode", billCode);
        return this.getBySQLAliasToBean(sb.toString(), params, CmsPrecativeLoanInfoVo.class);
    }
    /**
     * 单位校验使用
     * 
     * @Param corpId
     * @return
     */
    public Integer verifyCorpDeleteByCorpId(String corpId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) from CMS_PRECATIVE_LOAN_INFO where 1 = 1 and status != -2 and PRECATIVE_ID = ?");
        return Integer.valueOf(createSQLQuery(sql.toString(), corpId).uniqueResult().toString());
    }
    /**
     * 单位校验使用 -暂存,待审批,审批打回
     * 
     * @Param corpId
     * @return
     */
    public Integer verifyCorpDisableByCorpId(String corpId) {
        String sql = "select count(*) from cms_precative_loan_info where 1 = 1 and status not in (95,96) and status <> -2 and precative_id = ? ";
        Integer num = Integer.valueOf(createSQLQuery(sql, corpId).uniqueResult().toString());
        String sql1 = "select count(*) from cms_precative_loan_send where 1 = 1 and status in (0,11,-1) and precative_id = ?";
        Integer num1 = Integer.valueOf(createSQLQuery(sql1, corpId).uniqueResult().toString());
        String sql2 = "select count(*) from cms_precative_loan_repay where 1 = 1 and status in (0,11,-1) and corp_id = ?";
        Integer num2 = Integer.valueOf(createSQLQuery(sql2, corpId).uniqueResult().toString());
        String sql3 = "select count(*) from cms_precative_loan_deferred where 1 = 1 and (status in (0,11,-1) or biz_status = 77 ) and corp_id = ?";
        Integer num3 = Integer.valueOf(createSQLQuery(sql3, corpId).uniqueResult().toString());
        num = num + num1 + num2 + num3;
        return num;
    }
    /**
     * 单位校验使用 --待审批-借款方
     * 
     * @Param corpId
     * @return
     */
    public Integer verifyCorpDisableByCorpId2(String corpId) {
        String sql = "select count(*) from cms_precative_loan_info where 1 = 1 and status = 11  and status <> -2 and loan_opp = ?";
        return Integer.valueOf(createSQLQuery(sql, corpId).uniqueResult().toString());
    }
    /**
     * 单位校验使用 -补录、待发放
     * 
     * @Param corpId
     * @return
     */
    public Integer verifyCorpDisableByCorpId1(String corpId) {
        String sql = "select count(*) from cms_precative_loan_info where 1 = 1 and status in (95) and precative_id = ?";
        Integer num = Integer.valueOf(createSQLQuery(sql, corpId).uniqueResult().toString());
        String sql1 = "select count(*) from cms_precative_loan_info i  where 1 = 1  and i.status in (96)  and i.precative_id = ? "
                + "and  not exists (select 1 from cms_precative_loan_send s  where s.father_code  = i.bill_code ) ";
        Integer num1 = Integer.valueOf(createSQLQuery(sql1, corpId).uniqueResult().toString());
        return num + num1;
    }
}
