package com.bytter.report.domain.cms.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.DfConstant;
import com.bytter.report.domain.cms.vo.CapitalInterestVO;
import com.bytter.report.domain.cms.vo.HomeCapitalInterestVO;
import com.bytter.system.api.constant.BtCurrencyConstant;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CapitalInterestDao extends BaseDao {
    /**
     * 功能介绍 : 查询信贷本息数据。<br>
     * <b>日期：</b>2017年7月28日<br>
     *
     * @param
     * @version 1.0
     * @author tongqr
     */
    public DataTablesOutput listOutputData(DataTablesInput input, List<String> ids, CapitalInterestVO capitalInterestVO) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select * from (");
        //银行借款
        sql.append(" select info.corp_id as corpId,bank.bank_name as borrow,'02' as finType,info.bill_code as billCode,send.cur_code as curId,info.PL_MONEY as borrowMoney,");
        sql.append(" send.BILL_CODE as sendCode,send.MONEY as sendMoney,send.SEND_DATE as sendDate,send.REPAY_DATE as endDate");
        sql.append(" from cms_provide_loan_info info ");
        sql.append(" inner join cms_provide_loan_send send on info.bill_code = send.loan_code ");
        sql.append(" left join (select sum(repay_money) as sum_money,send_code from cms_provide_loan_repay plr where plr.status <> -2 group by send_code) repaySum on repaySum.send_code = send.bill_code ");
        sql.append(" left join bt_bank bank on bank.sys_bank_code = info.bank_id ");
        sql.append(" where send.status in (102,104) and info.status > -1 ");
        sql.append(" and ((repaySum.sum_money is null and send.money > 0) or send.money > repaySum.sum_money)");
        sql.append(" and ").append(getSQLIn(ids, 900, "info.corp_id"));
//        sql.append(" and not exists(select 1 from cms_provide_loan_repay plr where  plr.send_code = send.bill_code  and (plr.status in (97,0,95,11)) ) ");
        sql.append(" union all");
        //内部贷款
        sql.append(" select s.corp_id as corpId,c.name as borrow,'01' as finType,s.loan_code as billCode,s.cur_code as curId,i.LOAN_MONEY as borrowMoney,");
        sql.append(" s.bill_code as sendCode,s.loan_money as sendMoney,s.loan_date as sendDate,s.repay_date as endDate");
        sql.append(" from cms_loan_send s left join cms_loan_info i on s.loan_code = i.bill_code");
        sql.append(" left join sys_corp c on i.net_code=c.id");
        sql.append(" where 1=1");
        sql.append(" and s.status in ("+ CmsConstant.COMPELETOTAL+","+ CmsConstant.EXTENDDATING+","+ CmsConstant.COMPELTEXTENDATE+")");
        sql.append(" and ").append(getSQLIn(ids, 900, "s.corp_id"));
        sql.append(" union all");
        //委托贷款
        sql.append(" select a.PRECATIVE_ID as corpId,sc.name as borrow,'03' as finType,b.bill_Code as billCode,b.cur_id as curId,b.MONEY as borrowMoney,");
        sql.append(" null as sendCode,a.money as sendMoney,a.SEND_DATE as sendDate,a.DEADLINE_DATE as endDate");
        sql.append(" from cms_precative_loan_send a inner join cms_precative_loan_info b on a.father_code = b.bill_code ");
        sql.append(" left join sys_corp sc on b.loan_Opp=sc.id");
        sql.append(" left join (select sum(repay_money) as repayMoney, send_code from cms_precative_loan_repay   where status <> -2 group by send_code ) c ");
        sql.append(" on a.bill_code = c.send_code  where a.status in (102,103,104)  and (c.repayMoney is null or c.repayMoney < a.money) ");
        sql.append(" and ").append(getSQLIn(ids, 900, "a.PRECATIVE_ID"));
        sql.append(" union all");
        //融资租赁
        sql.append(" select l.CORP_ID as corpId,l.LESSOR as borrow,'06' as finType,l.LEASE_NO as billCode,l.ORI_CURID as curId,l.ORI_MONEY as borrowMoney,");
        sql.append(" null as sendCode,null as sendMoney,null as sendDate,l.END_DATE as endDate");
        sql.append(" from CMS_FINANCING_LEASE_INFO l left join CMS_LEASE_CAPITAL_PLAN lp on lp.lease_id = l.id");
        sql.append(" left join (select sum(MONEY) as payMoney,LEASE_ID from CMS_LEASE_PAY p group by p.LEASE_ID) c on c.LEASE_ID=l.id");
        sql.append(" where lp.status =95 and (c.payMoney is null or c.payMoney<l.ORI_MONEY)");
        sql.append(" and ").append(getSQLIn(ids, 900, "l.CORP_ID"));
        sql.append(" union all");
        //内部拆解
        sql.append(" select send1.BORROW_IN_CORP as corpId,sc.name as borrow,'04' as finType,info.BILL_NO as billCode,send1.CUR_ID as curId,info.MONEY as borrowMoney,");
        sql.append(" send1.bill_no as sendCode,send1.SEND_MONEY as sendMoney,send1.SEND_DATE as sendDate,send1.END_DATE as endDate");
        sql.append(" from cms_inl_send send1 ");
        sql.append(" left join CMS_INL_INFO info on info.id=send1.INL_INFO_ID");
        sql.append(" left join (select inl_send_id, sum(repay_money) repayMoney,sum(REPAY_INTEREST) repayInterest from CMS_INL_REPAY ");
        sql.append(" where status = 96 group by inl_send_id) repay ");
        sql.append(" on send1.id = repay.inl_send_id");
        sql.append(" left join sys_corp sc on send1.BORROW_OUT_CORP=sc.id");
        sql.append("  where send1.status = 96 and (repay.repayMoney is null or repay.repayMoney<send1.send_Money)");
        sql.append(" and ").append(getSQLIn(ids, 900, "send1.BORROW_IN_CORP"));
        sql.append(" union all");
        //直融
        sql.append(" select df.CORP_ID as corpId,df.LEADER_UNDERWRITER as borrow,'05' as finType,df.BILL_CODE as billCode,df.CUR_ID as curId,df.MONEY as borrowMoney,");
        sql.append(" null as sendCode,df.SURPLUS_MONEY as sendMoney,df.START_DATE as sendDate,df.END_DATE as endDate");
        sql.append(" from DF_DIRECT_FINANCE_INFO df ");
        sql.append("          left join (select DIRECT_FINANCE_ID, ");
        sql.append("                            BT_NVL_NUM(sum(REPAY_MONEY), 0) allRepayMoney ");
        sql.append("                       from DF_DIRECT_FINANCE_REPAY ");
        sql.append("                      where status >= " + DfConstant.APPROVED + " ");
        sql.append("                      group by DIRECT_FINANCE_ID) allRepay ");
        sql.append("            on df.id = AllRepay.DIRECT_FINANCE_ID ");
        sql.append("         where ");
        sql.append("           (df.status >= " + DfConstant.APPROVED + ") and  (allRepay.allRepayMoney is null or allRepay.allRepayMoney<df.SURPLUS_MONEY)");
        sql.append(" and ").append(getSQLIn(ids, 900, "df.CORP_ID"));
        sql.append(" union all");
        //债券
        sql.append(" select r.CORP_ID as corpId,i.LEAD_UNDERWRITER as borrow,'07' as finType,null as billCode,r.BOND_CUR as curId,r.ISSUE_CUR_TOTAL as borrowMoney,");
        sql.append(" i.BOND_ISSUE_NUM as sendCode,i.ISSUE_MONEY as sendMoney,i.ISSUE_START_DATE as sendDate,i.ISSUE_END_DATE as endDate");
        sql.append(" from CMS_BOND_ISSUE_INFO i left join CMS_BOND_REGISTER_INFO r  on i.REGISTER_ID = r.id");
        sql.append(" left join CMS_BOND_REPAY_PLAN p on p.ISSUE_ID=i.id");
        sql.append(" left join (select rp.ISSUE_ID,sum(rp.REPAY_MONEY) sumPay from CMS_BOND_PAY rp where rp.status=95 group by ISSUE_ID) spay");
        sql.append(" on i.id=spay.ISSUE_ID");
        sql.append(" where (i.is_delete = '0' or i.is_delete is null) ");
        sql.append(" and (r.is_delete = '0' or r.is_delete is null) ");
        sql.append(" and p.status=95 and (spay.sumPay is null or spay.sumPay<i.ISSUE_MONEY)");
        sql.append(" and ").append(getSQLIn(ids, 900, "r.CORP_ID"));
        sql.append(" union all");
        //资产证券化
        sql.append(" select info.CORP_ID as corpId,info.TRUSTEE as borrow,'08' as finType,null as billCode,info.CUR_ID as curId,info.MONEY as borrowMoney,");
        sql.append(" iss.SECURITY_ISSUE_CODE as sendCode,iss.THIS_ISSUE_MONEY as sendMoney,iss.ISSUE_DATE as sendDate,iss.EXPECT_END_DATE as endDate");
        sql.append(" from CMS_SECURITY_ISSUE iss left join CMS_SECURITY_INFO info on iss.SECURITY_INFO_ID=info.id");
        sql.append(" left join (select ta.SECURITY_ISSUE_CODE,sum(ta.TO_ACCOUNT_MONEY) as sumAmoney from CMS_SECURITY_TO_ACCOUNT ta where status=95 group by ta.SECURITY_ISSUE_CODE) sAcc");
        sql.append(" on iss.SECURITY_ISSUE_CODE=sAcc.SECURITY_ISSUE_CODE");
        sql.append(" left join CMS_SECURITY_PLAN plan on plan.SECURITY_ISSUE_CODE=iss.SECURITY_ISSUE_CODE");
        sql.append(" left join (select repay.SECURITY_ISSUE_CODE,sum(REPAY_AMOUNT) sumRepay from CMS_SECURITY_REPAY_SURE repay where repay.status=5 group by repay.SECURITY_ISSUE_CODE) spay");
        sql.append(" on iss.SECURITY_ISSUE_CODE=spay.SECURITY_ISSUE_CODE");
        sql.append(" where plan.status=95 and (spay.sumRepay is null or spay.sumRepay<iss.THIS_ISSUE_MONEY)");
        sql.append(" and sAcc.sumAmoney>=iss.THIS_ISSUE_MONEY");
        sql.append(" and ").append(getSQLIn(ids, 900, "info.CORP_ID"));

        sql.append(" )f where 1=1");
        if(StringUtils.isNotEmpty(capitalInterestVO.getFinType())&&!capitalInterestVO.getFinType().equals(" ")){
            sql.append(" and f.finType=:finType");
            params.put("finType",capitalInterestVO.getFinType());
        }
        if(StringUtils.isNotEmpty(capitalInterestVO.getCorpId())){
            String[] corpList=capitalInterestVO.getCorpId().split(",");
            sql.append(" AND " + SqlUtil.getInSql(corpList, "f.corpId", true));
        }
        if(StringUtils.isNotEmpty(capitalInterestVO.getBorrow())){
            sql.append(" and f.borrow like :borrow");
            params.put("borrow","%"+capitalInterestVO.getBorrow()+"%");
        }
        if(StringUtils.isNotEmpty(capitalInterestVO.getBillCode())){
            sql.append(" and f.billCode like :billCode");
            params.put("billCode","%"+capitalInterestVO.getBillCode()+"%");
        }
        if(StringUtils.isNotEmpty(capitalInterestVO.getSendCode())){
            sql.append(" and f.sendCode like :sendCode");
            params.put("sendCode","%"+capitalInterestVO.getSendCode()+"%");
        }
        if(StringUtils.isNotEmpty(capitalInterestVO.getCurId())&&!capitalInterestVO.getCurId().equals(" ")){
            sql.append(" and f.curId = :curId");
            params.put("curId",capitalInterestVO.getCurId());
        }
        return getDynamicBySQL(input, sql.toString(), CapitalInterestVO.class, null, false,params);
    }

    /*
    指定时间内待还本金利息
     */
    public List<CapitalInterestVO> listRepay(CapitalInterestVO vo, Date beginDate, Date endDate){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        if(vo.getFinType().equals("01")){//内部贷款
            sql.append(" select to_char(PREDICE_REPAY_DATE,'yyyy-MM') as reTime,sum(REPAYMENT_MONEY) as reMoney,sum(REPAYMENT_IST) as reInterest");
            sql.append(" from cms_repayment_schedule rs left join cms_repayment_scheduledetailed sub on sub.relation_id = rs.id");
            sql.append(" left join CMS_LOAN_SEND send on send.id=rs.bill_no");
            sql.append(" where 1=1 and (sub.valid_sign is null or sub.valid_sign <> '" + CmsConstant.DELETED + "') and send.LOAN_CODE=:sendCode ");
            sql.append(" and sub.IS_REPAY='0' and sub.PREDICE_REPAY_DATE>=:startDate and sub.PREDICE_REPAY_DATE<=:endDate");
            sql.append(" group by to_char(sub.PREDICE_REPAY_DATE,'yyyy-MM') order by to_char(sub.PREDICE_REPAY_DATE,'yyyy-MM') ");
            params.put("sendCode",vo.getSendCode());
        }else if(vo.getFinType().equals("02")){//银行借款
            sql.append(" select to_char(i.REPAY_DATE,'yyyy-MM') as reTime,sum(REPAY_MONEY) as reMoney,sum(PAY_INTEREST) as reInterest");
            sql.append(" from cms_provide_interest i ");
            sql.append(" where 1=1 ");
            sql.append(" and i.bill_code = :sendCode ");
            sql.append(" and i.interest_sign = '1' and i.status<>'1' and i.REPAY_DATE>=:startDate and i.REPAY_DATE<=:endDate");
            sql.append(" group by to_char(i.REPAY_DATE,'yyyy-MM') order by to_char(i.REPAY_DATE,'yyyy-MM')");
            params.put("sendCode",vo.getSendCode());
        }else if(vo.getFinType().equals("03")){//委托贷款
            sql.append(" select to_char(PREDICE_REPAY_DATE,'yyyy-MM') as reTime,sum(REPAYMENT_MONEY) as reMoney,sum(REPAYMENT_IST) as reInterest");
            sql.append(" from cms_repayment_schedule rs ,cms_repayment_scheduledetailed sub,cms_precative_loan_send send,cms_precative_loan_info b");
            sql.append("  where 1=1");
            sql.append("  and sub.relation_id = rs.id and (send.id = rs.bill_no or b.id=rs.bill_no) and send.father_code = b.bill_code");
            sql.append(" and (sub.valid_sign is null or sub.valid_sign <> '" + CmsConstant.DELETED + "') and b.bill_code=:billCode ");
            sql.append(" and sub.IS_REPAY='0' and sub.PREDICE_REPAY_DATE>=:startDate and sub.PREDICE_REPAY_DATE<=:endDate");
            sql.append(" group by to_char(sub.PREDICE_REPAY_DATE,'yyyy-MM') order by to_char(sub.PREDICE_REPAY_DATE,'yyyy-MM') ");
            params.put("billCode",vo.getBillCode());
        }else if(vo.getFinType().equals("04")){//内部拆解
            sql.append(" select to_char(i.REPAY_DATE,'yyyy-MM') as reTime,sum(REPAY_MONEY) as reMoney,sum(PAY_INTEREST) as reInterest");
            sql.append(" from cms_provide_interest i ");
            sql.append(" where 1=1 ");
            sql.append(" and i.bill_code = :billCode ");
            sql.append(" and i.interest_sign = '1' and i.status<>'1' and i.REPAY_DATE>=:startDate and i.REPAY_DATE<=:endDate");
            sql.append(" group by to_char(i.REPAY_DATE,'yyyy-MM') order by to_char(i.REPAY_DATE,'yyyy-MM')");
            params.put("billCode",vo.getSendCode());
        }else if(vo.getFinType().equals("05")){//直融
            if(checkMainRepayPlan(vo)){
                sql.append(" select to_char(tem.REPAY_DATE,'yyyy-MM') as reTime,sum(tem.REPAY_MONEY) as reMoney,sum(tem.PAY_INTEREST) as reInterest");
                sql.append(" from cms_provide_interest tem ");
                sql.append(" where 1=1 ");
                sql.append(" and tem.bill_code = :billCode ");
                sql.append(" and tem.interest_sign = '1' and tem.status<>'1' and tem.REPAY_DATE>=:startDate and tem.REPAY_DATE<=:endDate");
                sql.append(" group by to_char(tem.REPAY_DATE,'yyyy-MM') order by to_char(tem.REPAY_DATE,'yyyy-MM')");
                params.put("billCode",vo.getBillCode());
            } else {
                sql.append(" select to_char(tem.REPAY_DATE,'yyyy-MM') as reTime,sum(tem.REPAY_MONEY) as reMoney,sum(tem.INTEREST) as reInterest");
                sql.append(" from DF_DIRECT_FINANCE_REPAY_TMP tem");
                sql.append(" left join DF_DIRECT_FINANCE_INFO info on info.id=tem.DIRECT_FINANCE_ID");
                sql.append(" where 1=1 and info.BILL_CODE=:billCode and tem.REPAY_DATE>=:startDate and tem.REPAY_DATE<=:endDate");
                sql.append(" group by to_char(tem.REPAY_DATE,'yyyy-MM') order by to_char(tem.REPAY_DATE,'yyyy-MM')");
                params.put("billCode",vo.getBillCode());
            }
        }else if(vo.getFinType().equals("06")){//融资租赁
            sql.append(" select to_char(p.pay_date,'yyyy-MM') as reTime,sum(p.LEASE_MONRY) as reMoney,sum(p.LEASE_INTEREST) as reInterest");
            sql.append(" from CMS_LEASE_CAPITAL_PLANDEL p ");
            sql.append(" left join CMS_LEASE_CAPITAL_PLAN plan on p.lease_plan_id = plan.id");
            sql.append(" left join CMS_FINANCING_LEASE_INFO info on info.id=plan.LEASE_id");
            sql.append(" where 1 = 1 and p.valid_sign <> '" + CmsConstant.DELETED + "' ");
            sql.append(" and p.IS_REPAY<>'1' and p.pay_date>=:startDate and p.pay_date<=:endDate and info.LEASE_NO=:billCode");
            sql.append(" group by to_char(p.pay_date,'yyyy-MM') order by to_char(p.pay_date,'yyyy-MM')");
            params.put("billCode",vo.getBillCode());
        }else if(vo.getFinType().equals("07")) {//债券
            sql.append(" select to_char(pdtl.REPAY_DATE,'yyyy-MM')  as reTime,sum(pdtl.REPAY_MONEY) as reMoney,0 as reInterest");
            sql.append(" from CMS_BOND_REPAY_PLAN_DTL pdtl ");
            sql.append(" left join CMS_BOND_REPAY_PLAN plan on plan.id = pdtl.plan_id ");
            sql.append(" left join CMS_BOND_ISSUE_INFO i on i.id = plan.issue_id ");
            sql.append(" where 1 = 1 and pdtl.valid_sign <> '" + CmsConstant.DELETED + "' ");
            sql.append(" and pdtl.IS_REPAY<>'1' and pdtl.REPAY_DATE>=:startDate and pdtl.REPAY_DATE<=:endDate and i.BOND_ISSUE_NUM=:sendCode");
            sql.append(" group by to_char(pdtl.REPAY_DATE,'yyyy-MM') order by to_char(pdtl.REPAY_DATE,'yyyy-MM')");
            params.put("sendCode",vo.getSendCode());
        }else if(vo.getFinType().equals("08")) {//资产证券化
            sql.append(" select to_char(sure.REPAY_DATE,'yyyy-MM') as reTime,sum(sure.REPAY_MONEY) as reMoney,sum(sure.REPAY_INTEREST) as reInterest ");
            sql.append(" from CMS_SECURITY_REPAY_SURE sure where 1=1");
            sql.append(" and sure.IS_REPAY<>'1' and sure.REPAY_DATE>=:startDate and sure.REPAY_DATE<=:endDate and sure.SECURITY_ISSUE_CODE=:sendCode");
            sql.append(" group by to_char(sure.REPAY_DATE,'yyyy-MM') order by to_char(sure.REPAY_DATE,'yyyy-MM')");
            params.put("sendCode",vo.getSendCode());
        }
        params.put("startDate",beginDate);
        params.put("endDate",endDate);
        return listBySQLAliasToBean(sql.toString(),params, CapitalInterestVO.class);
    }

    /**
     * 直融检查是否维护还款计划
     * @param vo
     * @return
     */
    public boolean checkMainRepayPlan(CapitalInterestVO vo) {
        String sql = "select count(*) from CMS_REPAYMENT_SCHEDULE where status = 1 and bill_no = (select id from DF_DIRECT_FINANCE_INFO where bill_code = '" + vo.getBillCode() + "')";
        Integer count = Integer.valueOf(createSQLQuery(sql.toString()).uniqueResult().toString());
        if (count > 0) {
            return true;
        }
        return false;
    }

    /**
  指定时间内待还本金利息
   */
    public List<HomeCapitalInterestVO> initCapitalInterest(HomeCapitalInterestVO capitalInterestVO, List<String> corpAuth){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();

        if(capitalInterestVO.getFinType().equals("01")|| EmptyUtils.isEmpty(capitalInterestVO.getFinType())){//内部贷款
            sql.append(" select to_char(PREDICE_REPAY_DATE,'yyyy-MM') as month,sum(REPAYMENT_MONEY)/:moneyUnit as reMoney,sum(REPAYMENT_IST)/:moneyUnit as reInterest,send.CUR_CODE as curId");
            sql.append(" from cms_repayment_schedule rs left join cms_repayment_scheduledetailed sub on sub.relation_id = rs.id");
            sql.append(" left join CMS_LOAN_SEND send on send.id=rs.bill_no");
            sql.append(" where 1=1 and (sub.valid_sign is null or sub.valid_sign <> '" + CmsConstant.DELETED + "')  ");
            sql.append(" and send.status in ("+ CmsConstant.COMPELETOTAL+","+ CmsConstant.EXTENDDATING+","+ CmsConstant.COMPELTEXTENDATE+")");

            if(!EmptyUtils.isEmpty(capitalInterestVO.getCurId())){
                sql.append(" and send.CUR_CODE=:curId");
            }
            if(!EmptyUtils.isEmpty(capitalInterestVO.getCorpId())) {
                String[] split = capitalInterestVO.getCorpId().split(",");
                List<String> corpList = Arrays.asList(split);
                sql.append(" and "+getSQLIn(corpList,900,"send.CORP_ID"));
            }else{
                sql.append(" and "+getSQLIn(corpAuth,900,"send.CORP_ID"));
            }
            sql.append(" and sub.IS_REPAY='0' and sub.PREDICE_REPAY_DATE>=:startDate and sub.PREDICE_REPAY_DATE<=:endDate");
            sql.append(" group by to_char(sub.PREDICE_REPAY_DATE,'yyyy-MM'),send.CUR_CODE order by to_char(sub.PREDICE_REPAY_DATE,'yyyy-MM') ");

        }else if(capitalInterestVO.getFinType().equals("02")){//银行借款
            //连接借款表为了防止计划表有错误数据，和借款表对不上
            sql.append(" select to_char(i.REPAY_DATE,'yyyy-MM') as month,sum(REPAY_MONEY)/:moneyUnit as reMoney,sum(PAY_INTEREST)/:moneyUnit as reInterest,i.CUR_ID AS curId");
            sql.append(" from cms_provide_interest i ");
            sql.append(" left join cms_provide_loan_send send on send.bill_code=i.bill_code");
            sql.append(" left join cms_provide_loan_info info on info.bill_code = send.loan_code");
            sql.append(" left join (select sum(repay_money) as sum_money,send_code from cms_provide_loan_repay plr where plr.status <> -2 group by send_code) repaySum on repaySum.send_code = send.bill_code");
            sql.append(" where 1=1 ");
            sql.append(" and send.status in (102,104) and info.status > -1");
            sql.append(" and ((repaySum.sum_money is null and send.money > 0) or send.money > repaySum.sum_money)");
            if(!EmptyUtils.isEmpty(capitalInterestVO.getCorpId())) {
                String[] split = capitalInterestVO.getCorpId().split(",");
                List<String> corpList = Arrays.asList(split);
                sql.append(" and "+getSQLIn(corpList,900,"i.CORP_ID"));
            }else{
                sql.append(" and "+getSQLIn(corpAuth,900,"i.CORP_ID"));
            }
            if(!EmptyUtils.isEmpty(capitalInterestVO.getCurId())){
                sql.append(" and i.CUR_ID = :curId");
            }
            sql.append(" and i.interest_sign = '1' and i.status<>'1' and i.REPAY_DATE>=:startDate and i.REPAY_DATE<=:endDate");
            sql.append(" group by to_char(i.REPAY_DATE,'yyyy-MM'),i.CUR_ID order by to_char(i.REPAY_DATE,'yyyy-MM')");

        }else if(capitalInterestVO.getFinType().equals("03")){//委托贷款
            sql.append(" select to_char(PREDICE_REPAY_DATE,'yyyy-MM') as month,sum(REPAYMENT_MONEY)/:moneyUnit as reMoney,sum(REPAYMENT_IST)/:moneyUnit as reInterest,b.CUR_ID as curId");
            sql.append(" from cms_repayment_schedule rs ,cms_repayment_scheduledetailed sub,cms_precative_loan_send send,cms_precative_loan_info b");
//            sql.append(" (select sum(repay_money) as repayMoney, send_code   from cms_precative_loan_repay   where status <> -2   group by send_code) c");
            sql.append(" where sub.relation_id = rs.id and (send.id = rs.bill_no or b.id=rs.bill_no) and send.father_code = b.bill_code");
            sql.append(" and send.status in (102,103,104)  ");
            sql.append(" and (sub.valid_sign is null or sub.valid_sign <> '" + CmsConstant.DELETED + "') ");

            if(!EmptyUtils.isEmpty(capitalInterestVO.getCorpId())) {
                String[] split = capitalInterestVO.getCorpId().split(",");
                List<String> corpList = Arrays.asList(split);
                sql.append(" and "+getSQLIn(corpList,900,"send.PRECATIVE_ID"));
            }else{
                sql.append(" and "+getSQLIn(corpAuth,900,"send.PRECATIVE_ID"));
            }
            if(!EmptyUtils.isEmpty(capitalInterestVO.getCurId())){
                sql.append(" and b.CUR_ID=:curId ");
            }
            sql.append(" and sub.IS_REPAY='0' and sub.PREDICE_REPAY_DATE>=:startDate and sub.PREDICE_REPAY_DATE<=:endDate");
            sql.append(" group by to_char(sub.PREDICE_REPAY_DATE,'yyyy-MM'),b.CUR_ID order by to_char(sub.PREDICE_REPAY_DATE,'yyyy-MM') ");

        }else if(capitalInterestVO.getFinType().equals("04")){//内部拆解
            sql.append(" select to_char(i.REPAY_DATE,'yyyy-MM') as month,sum(REPAY_MONEY)/:moneyUnit as reMoney,sum(PAY_INTEREST)/:moneyUnit as reInterest,i.CUR_ID as curId");
            sql.append(" from cms_provide_interest i ");
            sql.append(" left join cms_inl_send send on i.bill_code=send.bill_no");
            sql.append(" left join (select inl_send_id, sum(repay_money) repayMoney,sum(REPAY_INTEREST) repayInterest from CMS_INL_REPAY ");
            sql.append(" where status = 96 group by inl_send_id) repay ");
            sql.append(" on send.id = repay.inl_send_id");
            sql.append(" where 1=1 ");
            sql.append(" and send.status = 96 and (repay.repayMoney is null or repay.repayMoney<send.send_Money)");
            if(!EmptyUtils.isEmpty(capitalInterestVO.getCorpId())) {
                String[] split = capitalInterestVO.getCorpId().split(",");
                List<String> corpList = Arrays.asList(split);
                sql.append(" and "+getSQLIn(corpList,900,"i.CORP_ID"));
            }else{
                sql.append(" and "+getSQLIn(corpAuth,900,"i.CORP_ID"));
            }
            if(!EmptyUtils.isEmpty(capitalInterestVO.getCurId())){
                sql.append(" and i.CUR_ID = :curId");
            }
            sql.append(" and i.interest_sign = '1' and i.status<>'1' and i.REPAY_DATE>=:startDate and i.REPAY_DATE<=:endDate");
            sql.append(" group by to_char(i.REPAY_DATE,'yyyy-MM'),i.CUR_ID order by to_char(i.REPAY_DATE,'yyyy-MM')");

        }else if(capitalInterestVO.getFinType().equals("05")){//直融
            sql.append(" select to_char(tem.REPAY_DATE,'yyyy-MM') as month,sum(tem.REPAY_MONEY)/:moneyUnit as reMoney,sum(tem.INTEREST)/:moneyUnit as reInterest,info.CUR_ID as curId");
            sql.append(" from DF_DIRECT_FINANCE_REPAY_TMP tem");
            sql.append(" left join DF_DIRECT_FINANCE_INFO info on info.id=tem.DIRECT_FINANCE_ID");
            sql.append("          left join (select DIRECT_FINANCE_ID, ");
            sql.append("                            BT_NVL_NUM(sum(REPAY_MONEY), 0) allRepayMoney ");
            sql.append("                       from DF_DIRECT_FINANCE_REPAY ");
            sql.append("                      where status >= " + DfConstant.APPROVED + " ");
            sql.append("                      group by DIRECT_FINANCE_ID) allRepay ");
            sql.append("            on info.id = AllRepay.DIRECT_FINANCE_ID ");
            sql.append(" where 1=1 and tem.REPAY_DATE>=:startDate and tem.REPAY_DATE<=:endDate");
            sql.append("        and   (info.status >= " + DfConstant.APPROVED + ") and  (allRepay.allRepayMoney is null or allRepay.allRepayMoney<info.SURPLUS_MONEY)");
            if(!EmptyUtils.isEmpty(capitalInterestVO.getCorpId())) {
                String[] split = capitalInterestVO.getCorpId().split(",");
                List<String> corpList = Arrays.asList(split);
                sql.append(" and "+getSQLIn(corpList,900,"info.CORP_ID"));
            }else{
                sql.append(" and "+getSQLIn(corpAuth,900,"info.CORP_ID"));
            }
            if(!EmptyUtils.isEmpty(capitalInterestVO.getCurId())){
                sql.append(" and info.CUR_ID=:curId");
            }
            sql.append(" group by to_char(tem.REPAY_DATE,'yyyy-MM'),info.CUR_ID order by to_char(tem.REPAY_DATE,'yyyy-MM')");

        }else if(capitalInterestVO.getFinType().equals("06")){//融资租赁
            sql.append(" select to_char(p.pay_date,'yyyy-MM') as month,sum(p.LEASE_MONRY)/:moneyUnit as reMoney,sum(p.LEASE_INTEREST)/:moneyUnit as reInterest,info.ORI_CURID as curId");
            sql.append(" from CMS_LEASE_CAPITAL_PLANDEL p ");
            sql.append(" left join CMS_LEASE_CAPITAL_PLAN plan on p.lease_plan_id = plan.id");
            sql.append(" left join CMS_FINANCING_LEASE_INFO info on info.id=plan.LEASE_id");
            sql.append(" left join (select sum(MONEY) as payMoney,LEASE_ID from CMS_LEASE_PAY p group by p.LEASE_ID) c on c.LEASE_ID=info.id");
            sql.append(" where 1 = 1 and p.valid_sign <> '" + CmsConstant.DELETED + "' ");
            sql.append(" and plan.status =95 and (c.payMoney is null or c.payMoney<info.ORI_MONEY)");
            if(!EmptyUtils.isEmpty(capitalInterestVO.getCorpId())) {
                String[] split = capitalInterestVO.getCorpId().split(",");
                List<String> corpList = Arrays.asList(split);
                sql.append(" and "+getSQLIn(corpList,900,"info.CORP_ID"));
            }else{
                sql.append(" and "+getSQLIn(corpAuth,900,"info.CORP_ID"));
            }

            if(!EmptyUtils.isEmpty(capitalInterestVO.getCurId())){
                sql.append(" and info.ORI_CURID=:curId");
            }
            sql.append(" and p.IS_REPAY<>'1' and p.pay_date>=:startDate and p.pay_date<=:endDate ");
            sql.append(" group by to_char(p.pay_date,'yyyy-MM'),info.ORI_CURID order by to_char(p.pay_date,'yyyy-MM')");
        }else if(capitalInterestVO.getFinType().equals("07")) {//债券
            sql.append(" select to_char(pdtl.REPAY_DATE,'yyyy-MM')  as month,sum(pdtl.REPAY_MONEY)/:moneyUnit as reMoney,0/:moneyUnit as reInterest,rinfo.BOND_CUR as curId");
            sql.append(" from CMS_BOND_REPAY_PLAN_DTL pdtl ");
            sql.append(" left join CMS_BOND_REPAY_PLAN plan on plan.id = pdtl.plan_id ");
            sql.append(" left join CMS_BOND_ISSUE_INFO i on i.id = plan.issue_id ");
            sql.append(" left join CMS_BOND_REGISTER_INFO rinfo on rinfo.id=i.REGISTER_ID");
            sql.append(" left join (select rp.ISSUE_ID,sum(rp.REPAY_MONEY) sumPay from CMS_BOND_PAY rp where rp.status=95 group by ISSUE_ID) spay");
            sql.append(" on i.id=spay.ISSUE_ID");
            sql.append(" where 1 = 1 and pdtl.valid_sign <> '" + CmsConstant.DELETED + "' ");
            sql.append(" and (i.is_delete = '0' or i.is_delete is null) ");
            sql.append(" and (rinfo.is_delete = '0' or rinfo.is_delete is null) ");
            sql.append(" and plan.status=95 and (spay.sumPay is null or spay.sumPay<i.ISSUE_MONEY)");
            if(!EmptyUtils.isEmpty(capitalInterestVO.getCorpId())) {
                String[] split = capitalInterestVO.getCorpId().split(",");
                List<String> corpList = Arrays.asList(split);
                sql.append(" and "+getSQLIn(corpList,900,"rinfo.CORP_ID"));
            }else{
                sql.append(" and "+getSQLIn(corpAuth,900,"rinfo.CORP_ID"));
            }
            if(!EmptyUtils.isEmpty(capitalInterestVO.getCurId())){
                sql.append(" and rinfo.BOND_CUR=:curId");
            }
            sql.append(" and pdtl.IS_REPAY<>'1' and pdtl.REPAY_DATE>=:startDate and pdtl.REPAY_DATE<=:endDate ");
            sql.append(" group by to_char(pdtl.REPAY_DATE,'yyyy-MM'),rinfo.BOND_CUR order by to_char(pdtl.REPAY_DATE,'yyyy-MM')");

        }else if(capitalInterestVO.getFinType().equals("08")) {//资产证券化
            sql.append(" select to_char(sure.REPAY_DATE,'yyyy-MM') as month,sum(sure.REPAY_MONEY)/:moneyUnit as reMoney,sum(sure.REPAY_INTEREST)/:moneyUnit as reInterest ,info.CUR_ID as curId");
            sql.append(" from CMS_SECURITY_REPAY_SURE sure ");
            sql.append(" left join CMS_SECURITY_INFO info on info.id=sure.SECURITY_INFO_ID");
            sql.append(" left join CMS_SECURITY_ISSUE iss  on iss.SECURITY_INFO_ID=info.id");
            sql.append(" left join (select ta.SECURITY_ISSUE_CODE,sum(ta.TO_ACCOUNT_MONEY) as sumAmoney from CMS_SECURITY_TO_ACCOUNT ta where status=95 group by ta.SECURITY_ISSUE_CODE) sAcc");
            sql.append(" on iss.SECURITY_ISSUE_CODE=sAcc.SECURITY_ISSUE_CODE");
            sql.append(" left join CMS_SECURITY_PLAN plan on plan.SECURITY_ISSUE_CODE=iss.SECURITY_ISSUE_CODE");
            sql.append(" left join (select repay.SECURITY_ISSUE_CODE,sum(REPAY_AMOUNT) sumRepay from CMS_SECURITY_REPAY_SURE repay where repay.status=5 group by repay.SECURITY_ISSUE_CODE) spay");
            sql.append(" on iss.SECURITY_ISSUE_CODE=spay.SECURITY_ISSUE_CODE");
            sql.append(" where 1=1");
            sql.append(" and plan.status=95 and (spay.sumRepay is null or spay.sumRepay<iss.THIS_ISSUE_MONEY)");
            sql.append(" and sAcc.sumAmoney>=iss.THIS_ISSUE_MONEY");
            if(!EmptyUtils.isEmpty(capitalInterestVO.getCorpId())) {
                String[] split = capitalInterestVO.getCorpId().split(",");
                List<String> corpList = Arrays.asList(split);
                sql.append(" and "+getSQLIn(corpList,900,"info.CORP_ID"));
            }else{
                sql.append(" and "+getSQLIn(corpAuth,900,"info.CORP_ID"));
            }
            if(!EmptyUtils.isEmpty(capitalInterestVO.getCurId())){
                sql.append(" and info.CUR_ID=:curId");
            }
            sql.append(" and sure.IS_REPAY<>'1' and sure.REPAY_DATE>=:startDate and sure.REPAY_DATE<=:endDate ");
            sql.append(" group by to_char(sure.REPAY_DATE,'yyyy-MM'),info.CUR_ID order by to_char(sure.REPAY_DATE,'yyyy-MM')");
        }
        if(!EmptyUtils.isEmpty(capitalInterestVO.getCurId())){
            params.put("curId",capitalInterestVO.getCurId());
        }
        if (StringUtils.isEmpty(capitalInterestVO.getStartDate())){
            params.put("startDate", DateUtils.getFirstMonthDate(DateUtils.getDate()));
        }else{
            params.put("startDate", DateUtils.parse(capitalInterestVO.getStartDate()+"-01","yyyy-MM-dd"));
        }
        if (StringUtils.isEmpty(capitalInterestVO.getEndDate())){
            params.put("endDate", DateUtils.addMonth(DateUtils.getFirstMonthDate(DateUtils.getDate()),12));
        }else{
            params.put("endDate", DateUtils.getLastMonthDate(DateUtils.parse(capitalInterestVO.getEndDate()+"-01","yyyy-MM-dd")));
        }
        params.put("moneyUnit",capitalInterestVO.getMoneyType());
        return listBySQLAliasToBean(sql.toString(),params, HomeCapitalInterestVO.class);
    }

    /**
     * 全景透视-银行借款：本年待还款金额，本年应付利息合计
     *
     * @param capitalInterestVO 封装数据查询对象
     * @param corpAuth          单位权限
     * @return                  集合
     */
    public List<HomeCapitalInterestVO> initCapitalInterestByProvide(HomeCapitalInterestVO capitalInterestVO, List<String> corpAuth){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        if(capitalInterestVO.getFinType().equals("02")){
            //连接借款表为了防止计划表有错误数据，和借款表对不上
            sql.append(" select to_char(i.REPAY_DATE,'yyyy-MM') as month, BT_NVL_NUM(i.REPAY_MONEY, 0)/:moneyUnit as reMoney, BT_NVL_NUM(i.PAY_INTEREST, 0)/:moneyUnit as reInterest, i.cur_id as curId ");
            sql.append(" from cms_provide_interest i ");
            sql.append(" left join cms_provide_loan_send send on send.bill_code=i.bill_code");
            sql.append(" left join cms_provide_loan_info info on info.bill_code = send.loan_code");
            sql.append(" left join (select sum(repay_money) as sum_money,send_code from cms_provide_loan_repay plr where plr.status <> -2 group by send_code) repaySum on repaySum.send_code = send.bill_code");
            sql.append(" where 1=1 ");
            sql.append(" and send.status in (102,104) and info.status > -1");
            sql.append(" and ((repaySum.sum_money is null and send.money > 0) or send.money > repaySum.sum_money)");
            if(!EmptyUtils.isEmpty(capitalInterestVO.getCorpId())) {
                String[] split = capitalInterestVO.getCorpId().split(",");
                List<String> corpList = Arrays.asList(split);
                sql.append(" and "+getSQLIn(corpList,900,"i.CORP_ID"));
            }else{
                sql.append(" and "+getSQLIn(corpAuth,900,"i.CORP_ID"));
            }
            if (StringUtils.isNotEmpty(capitalInterestVO.getCurId()) && !BtCurrencyConstant.ZH_RMB.equals(capitalInterestVO.getCurId())) {
                // 非折合人民币
                sql.append(" and i.CUR_ID = :curId");
                params.put("curId",capitalInterestVO.getCurId());
            }
            sql.append(" and i.interest_sign = '1' and i.status<>'1' and i.REPAY_DATE>=:startDate and i.REPAY_DATE<=:endDate ");
            sql.append(" order by to_char(i.REPAY_DATE,'yyyy-MM')");
            params.put("curId",capitalInterestVO.getCurId());
            if (StringUtils.isEmpty(capitalInterestVO.getStartDate())){
                params.put("startDate", DateUtils.getFirstMonthDate(DateUtils.getDate()));
            }else{
                params.put("startDate", DateUtils.parse(capitalInterestVO.getStartDate()+"-01","yyyy-MM-dd"));
            }
            if (StringUtils.isEmpty(capitalInterestVO.getEndDate())){
                params.put("endDate", DateUtils.addMonth(DateUtils.getFirstMonthDate(DateUtils.getDate()),12));
            }else{
                params.put("endDate", DateUtils.getLastMonthDate(DateUtils.parse(capitalInterestVO.getEndDate()+"-01","yyyy-MM-dd")));
            }
        }
        params.put("moneyUnit",capitalInterestVO.getMoneyType());
        return listBySQLAliasToBean(sql.toString(),params, HomeCapitalInterestVO.class);
    }
}
