package com.bytter.report.domain.cms.dao;

import com.bytter.report.domain.cms.vo.CmsLoanStatementVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luxy
 * @Title: CmsLoanStatementDao
 * @ProjectName T3(3.2)
 * @Description: TODO
 * @date 2020/1/13 11:21
 */
@Repository
public class CmsLoanStatementDao extends BaseDao<CmsLoanStatementVO,String> {


    /**
     * 查询贷款情况数据
     * @param input
     * @return
     */
    public DataTablesOutput getStatementVOs(DataTablesInput input,List<String> userCorps){
        String curId=(String)input.getFormdata().get("curId");
        String corps=(String)input.getFormdata().get("corpId");//查询条件
        List<String> corpIds = null;
        if(EmptyUtils.isNotEmpty(corps)) {
            corpIds = StringUtils.splitToList(",", corps);
        }

        StringBuilder sql=new StringBuilder();
        sql.append(" select * from ( ");
        String loanSign=(String)input.getFormdata().get("loanSign");
        if(StringUtils.isNotEmpty(loanSign)){
            if(loanSign.equals("01")){//内部贷款
                sql.append(this.queryLoanSql(curId,userCorps));
            }else if(loanSign.equals("02")){//银行借款
                sql.append(this.queryProvideSql(curId,userCorps));
            }else if(loanSign.equals("03")){//委托贷款
                sql.append(this.queryPrecativeSql(curId,userCorps));
            }
        }else{
            sql.append(this.queryLoanSql(curId,userCorps));
            sql.append(" t1 union ");
            sql.append(this.queryProvideSql(curId,userCorps));
            sql.append(" t2 union ");
            sql.append(this.queryPrecativeSql(curId,userCorps));
        }
        sql.append(" temp )dataSum ");
        if(EmptyUtils.isNotEmpty(corpIds)){
            sql.append(" where ").append(getSQLIn(corpIds, 900, "dataSum.corpId"));
        }
        Map<String,Object> params=new HashMap<String,Object>();
        if(!StringUtils.isBlank(curId)){
            params.put("curId",curId);
        }
        setParams(input,params);
        return getDynamicBySQL(input, sql.toString(), CmsLoanStatementVO.class, "", false,params);
//        return listBySQLAliasToBean(sql.toString(),params,CmsLoanStatementVO.class);
    }

    private  void setParams(DataTablesInput input,Map<String,Object> params){
        Date searchEndDate=new Date();//查询截止日期
        String endDate=(String)input.getFormdata().get("endDate")+"-01";
        Date eDate=DateUtils.str2Date(endDate,DateUtils.date_sdf());
        Date estiSearchEndDate=DateUtils.getLastMonthDate(eDate);//预计查询截止日期(取查询结束所在月的最后一天)
        int dayDiff=DateUtils.dayDiffs(DateUtils.getMonthEndTime(),estiSearchEndDate);
        if(dayDiff==0){//如果预计查询结束日期的当月最后一天和系统当前时间月份的最后一天为同一天，则截止查询日期取系统当前日期
            searchEndDate=DateUtils.getDate();
        }else{
            searchEndDate=estiSearchEndDate;
        }
        Date lastEndDate=DateUtils.rollDate(estiSearchEndDate,-1,0,0);//查询去年同期的结束时间（历史日期取预计查询截止日期）
        params.put("curEndDate",searchEndDate);
        params.put("lastEndDate",lastEndDate);
    }

    /**
     * 公共查询语句
     */
    public StringBuilder querySqlCommon(StringBuilder sql){
        sql.append("  BT_NVL(curData.corp_id,lastData.corp_id) corpId, ")
                .append( " BT_NVL(curData.cur_id,lastData.cur_id) as curId, ")
                .append( " BT_NVL_NUM(curData.contract_money, 0) as curContractMoney, ")
                .append( " BT_NVL_NUM(curData.send_money, 0) as curSendMoney,")
                .append( " BT_NVL_NUM(curData.repay_money, 0) as curRepayMoney, ")
                .append( " BT_NVL_NUM(lastData.contract_money, 0) as lastContractMoney, ")
                .append( " BT_NVL_NUM(lastData.send_money, 0) as lastSendMoney, ")
                .append( " BT_NVL_NUM(lastData.repay_money, 0) as lastRepayMoney ")
                .append( " from ( ");
        return sql;
    }

    /**
     * 查询内部贷款数据语句
     * @return
     */
    public StringBuilder queryLoanSql(String curId,List<String> userCorps){
        StringBuilder sql=new StringBuilder();
        //mysql中没有full join,使用union来达到两张表全连接的目的
        sql.append("select * from (");
        sql.append("(select '01' as loan_sign, ");
        this.querySqlCommon(sql);
        //查询本期金额
        sql.append(this.queryLoanCommonSql(curId,userCorps,"cur"));
        sql.append("  ) curData ");
        sql.append("  left JOIN (");
        //查询去年同期
        sql.append(this.queryLoanCommonSql(curId,userCorps,"last"));
        sql.append(" ) lastData ");
        sql.append(" on curData.corp_id = lastData.corp_id ");
        sql.append(" and curData.cur_id = lastData.cur_id ");
        sql.append(" ) ")
                .append(" union ");
        sql.append("(select '01' as loan_sign, ");
        this.querySqlCommon(sql);
        //查询本期金额
        sql.append(this.queryLoanCommonSql(curId,userCorps,"cur"));
        sql.append("  ) curData ");
        sql.append("  right JOIN (");
        //查询去年同期
        sql.append(this.queryLoanCommonSql(curId,userCorps,"last"));
        sql.append(" ) lastData ");
        sql.append(" on curData.corp_id = lastData.corp_id ");
        sql.append(" and curData.cur_id = lastData.cur_id )");
        sql.append(" ) ");
        return sql;
    }

    /**
     * 内部贷款查询公共语句
     * @param dataFlag 数据标志："cur":当前查询日期 "last":去年同期
     */
    public StringBuilder queryLoanCommonSql(String curId,List<String> corpIds,String dataFlag){
        StringBuilder commonSql=new StringBuilder();//内部贷款公共查询部分
        commonSql.append(" select k.corp_id, ")
                .append(" k.cur_id, ")
                .append(" BT_NVL_NUM(sum(k.loan_money), 0) contract_money, ")
                .append(" BT_NVL_NUM(sum(k.send_money), 0) send_money, ")
                .append(" BT_NVL_NUM(sum(l.repay_money), 0) repay_money ")
                .append(" from ")
                .append(" (select loanInfo.*, send.send_money ")
                .append(" from  ")
                .append(" (select info.bill_code, ")
                .append(" info.corp_id    as corp_id, ")
                .append(" info.cur_code   as cur_id, ")
                .append(" info.loan_money ")
                .append(" from cms_loan_info info ")
                .append(" where info.status >= 96 ")
                .append(" and info.create_date<=:" + dataFlag + "EndDate ");
        if (!StringUtils.isBlank(curId)) {
            commonSql.append(" and info.cur_code =:curId ");
        }
        if(StringUtils.isNotEmpty(corpIds)){
            commonSql.append(" and ").append(getSQLIn(corpIds, 900, "info.corp_id"));
        }
        commonSql.append(" ) loanInfo ")
                .append(" left join ")
                .append(" (select BT_NVL_NUM(sum(cls.loan_money), 0) as send_money, ")
                .append(" cls.loan_code ")
                .append(" from cms_loan_send cls ")
                .append(" where cls.status in (102,103,104,110) ")
                .append(" and cls.loan_date <=:" + dataFlag + "EndDate ")
                .append(" group by cls.loan_code) send ")
                .append(" on loanInfo.bill_code=send.loan_code ")
                .append(" ) k ")
                .append(" left join ")
                .append(" (select send.loan_code,BT_NVL_NUM(sum(repay.repay_money), 0) repay_money")
                .append(" from  ")
                .append(" (select clr.send_code,BT_NVL_NUM(sum(repay_money), 0) repay_money ")
                .append(" from cms_loan_repay clr ")
                .append(" where clr.status >= 102 ")
                .append(" and clr.status <> 120 ")
                .append(" and clr.repay_date <=:" + dataFlag + "EndDate ")
                .append(" group by clr.send_code) repay ")
                .append(" left join ")
                .append(" (select cls.bill_code, cls.loan_code")
                .append(" from cms_loan_send cls ")
                .append(" where cls.status in (102,103,104,110) ")
                .append(" )send ")
                .append(" on repay.send_code = send.bill_code ")
                .append(" group by send.loan_code ")
                .append(" ) l")
                .append(" on k.bill_code = l.loan_code ")
                .append(" group by k.corp_id, k.cur_id ");
        return commonSql;
    }


    /**
     * 查询银行借款贷款情况数据
     * @return
     */
    public StringBuilder queryProvideSql(String curId,List<String> userCorps){
        //mysql中没有full join,使用union来达到两张表全连接的目的
        StringBuilder sql=new StringBuilder();
        sql.append("select * from (");
        sql.append("(select '02' as loan_sign, ");
        this.querySqlCommon(sql);
        //查询本期金额
        sql.append(this.queryProvideCommonSql(curId,userCorps,"cur"));
        sql.append("  ) curData");
        sql.append("  left JOIN (");
        //查询去年同期
        sql.append(this.queryProvideCommonSql(curId,userCorps,"last"));
        sql.append("  ) lastData");
        sql.append(" on curData.corp_id = lastData.corp_id ");
        sql.append(" and curData.cur_id = lastData.cur_id ");
        sql.append(" ) union ");
        sql.append("(select '02' as loan_sign, ");
        this.querySqlCommon(sql);
        //查询本期金额
        sql.append(this.queryProvideCommonSql(curId,userCorps,"cur"));
        sql.append("  ) curData");
        sql.append("  right JOIN (");
        //查询去年同期
        sql.append(this.queryProvideCommonSql(curId,userCorps,"last"));
        sql.append("  ) lastData");
        sql.append(" on curData.corp_id = lastData.corp_id ");
        sql.append(" and curData.cur_id = lastData.cur_id ");
        sql.append(" )) ");
        return sql;
    }
    /**
     * 银行借款公共查询语句
     */
    public StringBuilder queryProvideCommonSql(String curId,List<String> corpIds,String dataFlag){
        StringBuilder commonSql=new StringBuilder();//内部贷款公共查询部分
        commonSql.append(" select k.corp_id, ")
                .append(" k.cur_id, ")
                .append(" BT_NVL_NUM(sum(k.pl_money), 0) contract_money, ")
                .append(" BT_NVL_NUM(sum(k.send_money), 0) send_money, ")
                .append(" BT_NVL_NUM(sum(l.repay_money), 0) repay_money ")
                .append(" from ")
                .append(" (select loanInfo.*, send.send_money ")
                .append(" from (select info.bill_code, ")
                .append(" info.corp_id, ")
                .append(" info.cur_id, ")
                .append(" info.pl_money ")
                .append(" from cms_provide_loan_info info ")
                .append(" where info.status >= 96 ")
                .append(" and info.create_date<=:" + dataFlag + "EndDate ");
        if (!StringUtils.isBlank(curId)) {
            commonSql.append(" and info.cur_id =:curId ");
        }
        if(StringUtils.isNotEmpty(corpIds)){
            commonSql.append(" and ").append(getSQLIn(corpIds, 900, "info.corp_id"));
        }
        commonSql.append(" ) loanInfo ")
                .append(" left join ")
                .append(" (select BT_NVL_NUM(sum(cls.money), 0) as send_money, ")
                .append(" cls.loan_code ")
                .append(" from cms_provide_loan_send cls ")
                .append(" where cls.status in (102,103,104,110) ")
                .append(" and cls.send_date <=:"+dataFlag+"EndDate ")
                .append(" group by cls.loan_code) send ")
                .append(" on send.loan_code = loanInfo.bill_code ")
                .append(" ) k ")
                .append(" left join (")
                .append(" select send.loan_code,BT_NVL_NUM(sum(repay.repay_money), 0) repay_money  ")
                .append(" from (select BT_NVL_NUM(sum(repay_money), 0) repay_money, ")
                .append(" clr.send_code ")
                .append(" from cms_provide_loan_repay clr ")
                .append(" where clr.status >= 102 ")
                .append(" and clr.status <> 120 ")
                .append("  and clr.repay_date <=:"+dataFlag+"EndDate ")
                .append(" group by clr.send_code) repay ")
                .append(" left join (select cls.bill_code, cls.loan_code ")
                .append(" from cms_provide_loan_send cls ")
                .append(" where cls.status in (102,103,104,110) ")
                .append(" ) send ")
                .append(" on repay.send_code = send.bill_code ")
                .append(" group by send.loan_code ")
                .append(" ) l ")
                .append(" on k.bill_code = l.loan_code ")
                .append(" group by k.corp_id, k.cur_id ");
        return commonSql;
    }

    /**
     * 查询委托贷款数据
     * @return
     */
    public StringBuilder queryPrecativeSql(String curId,List<String> userCorps){
        //mysql中没有full join,使用union来达到两张表全连接的目的
        StringBuilder sql=new StringBuilder();
        sql.append("select * from (");
        sql.append("(select '03' as loan_sign, ");
        this.querySqlCommon(sql);
        //查询本期金额
        sql.append(this.queryPrecativeCommonSql(curId,userCorps,"cur"));
        sql.append(" ) curData ");
        sql.append("  left JOIN (");
        //查询去年同期
        sql.append(this.queryPrecativeCommonSql(curId,userCorps,"last"));
        sql.append(" ) lastData ");
        sql.append(" on curData.corp_id = lastData.corp_id ");
        sql.append(" and curData.cur_id = lastData.cur_id ");
        sql.append(" ) union ");
        sql.append("(select '03' as loan_sign, ");
        this.querySqlCommon(sql);
        //查询本期金额
        sql.append(this.queryPrecativeCommonSql(curId,userCorps,"cur"));
        sql.append(" ) curData ");
        sql.append("  right JOIN (");
        //查询去年同期
        sql.append(this.queryPrecativeCommonSql(curId,userCorps,"last"));
        sql.append(" ) lastData ");
        sql.append(" on curData.corp_id = lastData.corp_id ");
        sql.append(" and curData.cur_id = lastData.cur_id ");
        sql.append(" ))");
        return sql;
    }

    /**
     * 委托贷款公共查询语句
     * @param curId
     * @param corpIds
     * @param dataFlag
     * @return
     */
    public StringBuilder queryPrecativeCommonSql(String curId,List<String> corpIds,String dataFlag){
        StringBuilder commonSql=new StringBuilder();//内部贷款公共查询部分
        commonSql.append(" select k.corp_id,")
                .append(" k.cur_id, ")
                .append(" BT_NVL_NUM(sum(k.money), 0) contract_money, ")
                .append(" BT_NVL_NUM(sum(k.send_money), 0) send_money, ")
                .append(" BT_NVL_NUM(sum(l.repay_money), 0) repay_money ")
                .append(" from  ")
                .append(" (select loanInfo.*, send.send_money ")
                .append(" from (select info.bill_code, ")
                .append(" info.loan_opp as corp_id, ")
                .append(" info.cur_id, ")
                .append(" info.money ")
                .append("  from cms_precative_loan_info info ")
                .append(" where info.status >= 96 ")
                .append(" and info.create_date<=:" + dataFlag + "EndDate ");//录入时间要在查询时间范围内
        if (!StringUtils.isBlank(curId)) {
            commonSql.append(" and info.cur_id=:curId ");
        }
        if(StringUtils.isNotEmpty(corpIds)){
            commonSql.append(" and ").append(getSQLIn(corpIds, 900, "info.loan_opp"));
        }
        commonSql.append(" ) loanInfo ")
                .append(" left join ")
                .append(" (select BT_NVL_NUM(sum(cls.money), 0) as send_money, ")
                .append(" cls.father_code ")
                .append(" from cms_precative_loan_send cls ")
                .append(" where cls.status in (102,103,104,110) ")
                .append(" and cls.send_date <=:"+dataFlag+"EndDate ")
                .append(" group by cls.father_code) send ")
                .append(" on send.father_code = loanInfo.bill_code ")
                .append(" ) k ")
                .append(" left join ( ")
                .append(" select send.father_code,BT_NVL_NUM(sum(repay.repay_money), 0) repay_money  ")
                .append(" from (select BT_NVL_NUM(sum(repay_money), 0) repay_money, ")
                .append(" clr.send_code ")
                .append(" from cms_precative_loan_repay clr ")
                .append(" where clr.status >= 102 ")
                .append(" and clr.status <> 120 ")
                .append(" and clr.repay_date <=:"+dataFlag+"EndDate ")
                .append(" group by clr.send_code) repay ")
                .append(" left join (select cls.bill_code, cls.father_code ")
                .append(" from cms_precative_loan_send cls ")
                .append(" where cls.status in (102,103,104,110) ")
                .append(" ) send ")
                .append(" on repay.send_code = send.bill_code ")
                .append(" group by send.father_code ")
                .append("  ) l ")
                .append(" on k.bill_code = l.father_code ")
                .append(" group by k.corp_id, k.cur_id ");
        return commonSql;
    }



}
