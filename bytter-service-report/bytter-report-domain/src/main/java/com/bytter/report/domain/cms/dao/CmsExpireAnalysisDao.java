package com.bytter.report.domain.cms.dao;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.report.domain.cms.vo.RatBkrationVO;
import com.bytter.report.domain.cms.vo.*;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CmsConstant;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2020-01-19 14:13
 * @Version 1.0
 */
@Repository
public class CmsExpireAnalysisDao extends BaseDao<RatBkrationVO,String> {

    /**
     * 报表查询
     * @param sql
     * @param input
     * @return
     */
    public DataTablesOutput getCmsLoanDataTable(StringBuilder sql, DataTablesInput input, List<String> corpIds) {
        String newSql = getDataTableCommonSql(sql, input.getFormdata(), corpIds);
        input.setFormdata(new HashMap<String, Object>());
        return getDynamicBySQL(input, newSql, CmsExpireAnalysisVO.class, "", false, null);
    }

    /**
    *app报表列表查询
    *@Param sql
    *@Param params
    *@return
    */
    public List<CmsExpireAnalysisVO> getMobileCmsLoanDataTable(StringBuilder sql, Map<String, Object> params, List<String> corpIds) {
        String newSql = getDataTableCommonSql(sql, params, corpIds);
        return listBySQLAliasToBean(newSql, new HashMap<String, Object>(), CmsExpireAnalysisVO.class);
    }

    /**
     * 返回sql拼接条件
     *
     * @return
     * @Param sql
     * @Param input
     */
    private String getDataTableCommonSql(StringBuilder sql, Map<String, Object> params, List<String> corpIds) {
        /**
         * 单位
         */
        sql.append(" and (");
        sql.append(getSQLIn(corpIds, 900, "cmsLoanInfo.corpId"));
        sql.append(" ) ");
        if (params != null) {
            /**
             * 币种
             */
            if (null != params.get("curId") && StringUtils.isNotEmpty(params.get("curId").toString())) {
                sql.append(" and cmsLoanInfo.curId = '" + (params.get("curId").toString() + "'"));
            }

            /**
             * 融资种类
             */
            if (null != params.get("loanType") && StringUtils.isNotEmpty(params.get("loanType").toString())) {
                sql.append(" and cmsLoanInfo.loanType = '" + params.get("loanType").toString() + "'");
            }

            /**
             * 剩余期限
             */
            if (null != params.get("expireAnalysis") && StringUtils.isNotEmpty(params.get("expireAnalysis").toString())) {
                sql.append(" and cmsLoanInfo.expireAnalysis = '" + params.get("expireAnalysis").toString() + "' ");
            }
        }
        return sql.toString();
    }

    /**
     * 内部贷款明细查询
     * @param corpId
     * @param curId
     * @param sqlCondition
     * @return
     */
    public DataTablesOutput getCmsLoanDtl(String corpId, String curId,String sqlCondition){
        StringBuilder sql = getLoanDtlSql();
        sql.append(" and cls.status = 102");
        if (ObjectUtil.isNotEmpty(sqlCondition)){
            sql.append(" and " + sqlCondition);
        }
        if (ObjectUtil.isNotEmpty(corpId)){
            sql.append(" and  cli.corp_id = '"+corpId+"' ");
        }
        if (ObjectUtil.isNotEmpty(curId)){
            sql.append(" and cli.cur_code = '"+curId+"' ");
        }
        return getDynamicBySQL(new DataTablesInput(), sql.toString(), CmsLoanDtlVO.class, "cls", false, null);
    }

    /**
     * 委托贷款明细查询
     * @param corpId
     * @param curId
     * @param sqlCondition
     * @return
     */
    public DataTablesOutput getCmsPrecativeDtl(String corpId, String curId,String sqlCondition){
        StringBuilder sql = getPrecativeDtlSql();
        sql.append(" and cpls.status = 102 ");
        if (ObjectUtil.isNotEmpty(sqlCondition)){
            sql.append(" and " + sqlCondition);
        }
        if (ObjectUtil.isNotEmpty(corpId)){
            sql.append(" and  cpli.LOAN_OPP = '"+corpId+"' ");
        }
        if (ObjectUtil.isNotEmpty(curId)){
            sql.append(" and  cpli.cur_id = '"+curId+"' ");
        }
        return getDynamicBySQL(new DataTablesInput(), sql.toString(), CmsPrecativeDtlVO.class, "cpls", false, null);
    }

    /**
     * 银行借款明细查询
     * @param corpId
     * @param curId
     * @param sqlCondition
     * @return
     */
    public DataTablesOutput getCmsProvideDtl(String corpId, String curId,String sqlCondition){
        StringBuilder sql = getprovideDtlSql();
        sql.append(" and cpls.status = 102   ");
        if (ObjectUtil.isNotEmpty(sqlCondition)){
            sql.append(" and " + sqlCondition);
        }
        if (ObjectUtil.isNotEmpty(corpId)){
            sql.append(" and  cpls.corp_id = '"+corpId+"' ");
        }
        if (ObjectUtil.isNotEmpty(curId)){
            sql.append(" and  cpls.cur_code = '"+curId+"' ");
        }
        return getDynamicBySQL(new DataTablesInput(), sql.toString(), CmsProvideDtlVO.class, "cpls", false, null);
    }


    /**
     * 内部贷款明细查询
     * @param corpId
     * @param curId
     * @param sqlCondition
     * @return
     */
    public List<CmsLoanDtlVO> getMobileLoanDtl(String corpId, String curId,String sqlCondition){
        StringBuilder sql = getLoanDtlSql();
        sql.append(" and cls.status = 102");
        sql.append(" and " + sqlCondition);
        sql.append(" and  cli.corp_id = '"+corpId+"' ");
        sql.append(" and cli.cur_code = '"+curId+"' ");
        return listBySQLAliasToBean(sql.toString(), new HashMap<String, Object>(), CmsLoanDtlVO.class);
    }

    /**
     * 委托贷款明细查询
     * @param corpId
     * @param curId
     * @param sqlCondition
     * @return
     */
    public List<CmsPrecativeDtlVO> getMobilePrecativeDtl(String corpId, String curId,String sqlCondition){
        StringBuilder sql = getPrecativeDtlSql();
        sql.append(" and cpls.status = 102 ");
        sql.append(" and " + sqlCondition);
        sql.append(" and  cpli.LOAN_OPP = '"+corpId+"' ");
        sql.append(" and  cpli.cur_id = '"+curId+"' ");
        return listBySQLAliasToBean(sql.toString(), new HashMap<String, Object>(), CmsPrecativeDtlVO.class);
    }

    /**
     * 银行借款明细查询
     * @param corpId
     * @param curId
     * @param sqlCondition
     * @return
     */
    public List<CmsProvideDtlVO> getMobileProvideDtl(String corpId, String curId,String sqlCondition){
        StringBuilder sql = getprovideDtlSql();
        sql.append(" and cpls.status = 102   ");
        sql.append(" and " + sqlCondition);
        sql.append(" and  cpls.corp_id = '"+corpId+"' ");
        sql.append(" and  cpls.cur_code = '"+curId+"' ");
        return listBySQLAliasToBean(sql.toString(), new HashMap<String, Object>(), CmsProvideDtlVO.class);
    }

    /**
     * 内部贷款
     *
     * @param isInclude
     * @param curId
     * @param BiRemainTerm
     * @param moneyType
     * @param corpIds
     * @return
     */
    public List<CmsExpireAnalysisCurVO> getCmsLoanBar(String isInclude, String curId, String BiRemainTerm, String moneyType, List<String> corpIds, String isConvert, String commonExchangeSql) {
        StringBuilder sql = new StringBuilder();
//        sql.append(" select bt_nvl_num(sum(leftMoney),0) from (");
        sql.append("select ");
        sql.append("bt_nvl_num(sum(");
//        金额单位
        if (CmsConstant.MONEY_TYPE_1000.equals(moneyType)) {
            sql.append("       (cls.loan_money - bt_nvl_num(repay_info.repayMoney,0))/1000 ");
        } else if (CmsConstant.MONEY_TYPE_10000.equals(moneyType)) {
            sql.append("       (cls.loan_money - bt_nvl_num(repay_info.repayMoney,0))/10000 ");
        } else if (CmsConstant.MONEY_TYPE_100000000.equals(moneyType)) {
            sql.append("       (cls.loan_money - bt_nvl_num(repay_info.repayMoney,0))/100000000 ");
        } else {
            sql.append("       cls.loan_money - bt_nvl_num(repay_info.repayMoney,0)");
        }
        sql.append(" ),0) leftMoney,");
        sql.append(" cls.cur_code as curId");
        sql.append("  from CMS_LOAN_SEND cls ");
        sql.append("  left join (select sum(bt_nvl_num(repay_money, 0)) repayMoney, send_code ");
        sql.append("               from CMS_LOAN_REPAY ");
        sql.append("              where 1=1");
        if (CmsConstant.IS_FALSE.equals(isInclude)) {
            sql.append(" and status >= 11 ");
        }
        sql.append("              group by send_code) repay_info ");
        sql.append("    on cls.bill_code = repay_info.send_code ");
        sql.append(" where cls.status = 102 ");
        if (StringUtils.isNotEmpty(curId)) {
            sql.append(" and cls.cur_code = '" + curId + "' ");
        }
        sql.append(" and " + BiRemainTerm);
        if (null != corpIds && corpIds.size() > 0) {
            sql.append(" and ( ");
            sql.append(getSQLIn(corpIds, 900, "cls.corp_id"));
            sql.append(" ) ");
        }
        sql.append("  group by cls.cur_code ");
//        sql.append(" ) ");
        return listBySQLAliasToBean(sql.toString(), new HashMap<String, Object>(), CmsExpireAnalysisCurVO.class);
//        return createSQLQuery(sql.toString()).uniqueResult().toString();

    }

    /**
     * 委托贷款
     * @param isInclude
     * @param curId
     * @param BiRemainTerm
     * @param moneyType
     * @param corpIds
     * @return
     */
    public List<CmsExpireAnalysisCurVO> getCmsPrecativeBar(String isInclude,String curId,String BiRemainTerm,String moneyType, List<String> corpIds, String isConvert, String commonExchangeSql){
        StringBuilder sql = new StringBuilder();
//        sql.append(" select bt_nvl_num(sum(leftMoney),0) from (");
        sql.append("select ");
        sql.append("bt_nvl_num(sum(");
//        金额单位
        if (CmsConstant.MONEY_TYPE_1000.equals(moneyType)) {
            sql.append("       (cpls.money - bt_nvl_num(repay_info.repayMoney,0))/1000 ");
        } else if (CmsConstant.MONEY_TYPE_10000.equals(moneyType)) {
            sql.append("       (cpls.money - bt_nvl_num(repay_info.repayMoney,0))/10000 ");
        } else if (CmsConstant.MONEY_TYPE_100000000.equals(moneyType)) {
            sql.append("       (cpls.money - bt_nvl_num(repay_info.repayMoney,0))/100000000 ");
        } else{
            sql.append("       cpls.money - bt_nvl_num(repay_info.repayMoney,0)  ");
        }
        sql.append(" ),0) leftMoney,");
        sql.append(" cpli.cur_id as curId");
        sql.append("  from CMS_PRECATIVE_LOAN_SEND cpls ");
        sql.append("  left join CMS_PRECATIVE_LOAN_INFO cpli ");
        sql.append("    on cpls.father_code = cpli.bill_code ");
        sql.append("  left join (select sum(bt_nvl_num(REPAY_MONEY,0)) repayMoney, send_code ");
        sql.append("               from CMS_PRECATIVE_LOAN_REPAY ");
        sql.append("              where 1 = 1");
        if (CmsConstant.IS_FALSE.equals(isInclude)) {
            sql.append(" and status >= 11 ");
        }
        sql.append("              group by send_code) repay_info ");
        sql.append("    on cpls.bill_code = repay_info.send_code ");
        sql.append(" where cpls.status = 102 ");
        if (StringUtils.isNotEmpty(curId)) {
            sql.append(" and cpli.cur_id = '" + curId + "' ");
        }
        sql.append(" and " + BiRemainTerm);
        if (null != corpIds && corpIds.size() > 0) {
            sql.append(" and ( ");
            sql.append(getSQLIn(corpIds,900,"cpli.LOAN_OPP"));
            sql.append(" ) ");
        }
        sql.append("  group by cpli.cur_id ");
//        sql.append(" ) ");
        return listBySQLAliasToBean(sql.toString(), new HashMap<String, Object>(), CmsExpireAnalysisCurVO.class);
//        return createSQLQuery(sql.toString()).uniqueResult().toString();
    }

    /**
     * 银行借款
     * @param isInclude
     * @param curId
     * @param BiRemainTerm
     * @param moneyType
     * @param corpIds
     * @return
     */
    public List<CmsExpireAnalysisCurVO> getCmsProvideBar(String isInclude,String curId,String BiRemainTerm,String moneyType, List<String> corpIds, String isConvert, String commonExchangeSql){
        StringBuilder sql = new StringBuilder();
//        sql.append(" select bt_nvl_num(sum(leftMoney),0) from (");
        sql.append("select ");
        sql.append("bt_nvl_num(sum(");
//        金额单位
        if (CmsConstant.MONEY_TYPE_1000.equals(moneyType)) {
            sql.append("       (cpls.money - bt_nvl_num(repay_info.repayMoney,0))/1000 ");
        } else if (CmsConstant.MONEY_TYPE_10000.equals(moneyType)) {
            sql.append("       (cpls.money - bt_nvl_num(repay_info.repayMoney,0))/10000 ");
        } else if (CmsConstant.MONEY_TYPE_100000000.equals(moneyType)) {
            sql.append("       (cpls.money - bt_nvl_num(repay_info.repayMoney,0))/100000000 ");
        } else{
            sql.append("       cpls.money - bt_nvl_num(repay_info.repayMoney,0)  ");
        }
        sql.append(" ),0) leftMoney,");
        sql.append("  cpls.cur_code as curId");
        sql.append("  from cms_provide_loan_send cpls ");
        sql.append("  left join (select sum(bt_nvl_num(REPAY_MONEY,0)) repayMoney, send_code ");
        sql.append("               from CMS_PROVIDE_LOAN_REPAY t ");
        sql.append("              where 1 = 1");
        if (CmsConstant.IS_FALSE.equals(isInclude)) {
            sql.append(" and status >= 11 ");
        }
        sql.append("              group by send_code) repay_info ");
        sql.append("    on cpls.bill_code = repay_info.send_code ");
        sql.append("  where cpls.status = 102 ");
        if (StringUtils.isNotEmpty(curId)) {
            sql.append(" and cpls.cur_code = '" + curId + "' ");
        }
        sql.append(" and " + BiRemainTerm);
        if (null != corpIds && corpIds.size() > 0) {
            sql.append(" and ( ");
            sql.append(getSQLIn(corpIds,900,"cpls.corp_id"));
            sql.append(" ) ");
        }
        sql.append("  group by cpls.cur_code ");
//        sql.append(" ) ");
        return listBySQLAliasToBean(sql.toString(), new HashMap<String, Object>(), CmsExpireAnalysisCurVO.class);
//        return createSQLQuery(sql.toString()).uniqueResult().toString();
    }

    /**
    *内部贷款明细查询
    *@Param
    *@return
    */
    private StringBuilder getLoanDtlSql(){
        StringBuilder sql = new StringBuilder();
        sql.append("select cls.loan_code billCode, ");
        sql.append("       cli.net_code, ");
        sql.append("       corp.name netName, ");
        sql.append("       corp1.name corpName, ");
        sql.append("       cls.contract_code contractNo, ");
        sql.append("       cls.bill_code sendBillCode, ");
        sql.append("       cls.loan_acc sendAcc, ");
        sql.append("       cli.cur_code curId, ");
//        sql.append("       cur.cur_name curName, ");
        sql.append("       cli.loan_money money, ");
        sql.append("       cli.loan_date loanDate, ");
        sql.append("       cli.repay_date repayDate, ");
        sql.append("       clt.name loanGenre, ");
        sql.append("       cli.loan_term loanTerm, ");
        sql.append("       cli.tern_unit ternUnit, ");
        sql.append("       cls.loan_money sendMoney, ");
        sql.append("       bt_nvl_num(repayInfo.repayMoney,0) repayMoney, ");
        sql.append("       bt_nvl_num(repayingInfo.repayingMoney,0) repayingMoney, ");
        sql.append("       (cls.loan_money - bt_nvl_num(repayInfo.repayMoney,0)) leftMoney ");
        sql.append("  from CMS_LOAN_SEND cls ");
        sql.append("  left join CMS_LOAN_INFO cli ");
        sql.append("    on cls.loan_code = cli.bill_code ");
        sql.append("  left join sys_corp corp ");
        sql.append("    on cli.net_code = corp.id ");
        sql.append("  left join sys_corp corp1 ");
        sql.append("    on cli.corp_id = corp1.id ");
        sql.append("  left join sys_corp corp2 ");
        sql.append("    on cli.corp_id = corp2.id ");
//        sql.append("  left join bt_currency cur ");
//        sql.append("    on cli.cur_code = cur.id ");
        sql.append("  left join cms_loan_type clt ");
        sql.append("    on cli.loan_genre = clt.code and clt.status != -2 and clt.type = 1");
        sql.append("  left join (select bt_nvl_num(sum(repay_money),0) repayMoney, send_code sendCode ");
        sql.append("               from CMS_LOAN_REPAY ");
        sql.append("              where status = 102 ");
        sql.append("              group by send_code) repayInfo ");
        sql.append("    on repayInfo.sendCode = cls.bill_code ");
        sql.append("  left join (select bt_nvl_num(sum(repay_money),0) repayingMoney, send_code sendCode ");
        sql.append("               from CMS_LOAN_REPAY ");
        sql.append("              where status >= 11 ");
        sql.append("                and status != 102 ");
        sql.append("              group by send_code) repayingInfo ");
        sql.append("    on repayingInfo.sendCode = cls.bill_code ");
        sql.append("where 1 = 1 ");
        return sql;
    }

    /**
    *委托贷款明细查询
    *@Param
    *@return
    */
    private StringBuilder getPrecativeDtlSql(){
        StringBuilder sql = new StringBuilder();
        sql.append("select cpls.father_code billCode, ");
        sql.append("       corp1.name precativeName, ");
        sql.append("       corp2.name corpName, ");
        sql.append("       corp3.name loanOppName, ");
        sql.append("       cpls.contract_code contractCode, ");
        sql.append("       cpls.bill_code sendBillCode, ");
//        sql.append("       cur.cur_name curName, ");
        sql.append("       cpli.cur_id curId, ");
        sql.append("       cpli.money money, ");
        sql.append("       cpli.start_date startDate, ");
        sql.append("       cpli.end_date endDate, ");
        sql.append("       cpli.term term, ");
        sql.append("       cpli.tern_unit ternUnit, ");
        sql.append("       cpls.money sendMoney, ");
        sql.append("       bt_nvl_num(repayInfo.repayMoney,0) repayMoney, ");
        sql.append("       bt_nvl_num(repayingInfo.repayingMoney,0) repayingMoney, ");
        sql.append("       (cpls.money - bt_nvl_num(repayInfo.repayMoney,0)) leftMoney ");
        sql.append("  from CMS_PRECATIVE_LOAN_SEND cpls ");
        sql.append("  left join CMS_PRECATIVE_LOAN_INFO cpli ");
        sql.append("    on cpls.father_code = cpli.bill_code ");
        sql.append("  left join sys_corp corp1 ");
        sql.append("    on cpls.precative_id = corp1.id ");
        sql.append("  left join sys_corp corp2 ");
        sql.append("    on cpli.corp_id = corp2.id ");
        sql.append("  left join sys_corp corp3 ");
        sql.append("    on cpli.loan_opp = corp3.id ");
//        sql.append("  left join bt_currency cur ");
//        sql.append("    on cpli.cur_id = cur.id ");
        sql.append("  left join (select bt_nvl_num(sum(repay_money),0) repayMoney, send_code ");
        sql.append("               from CMS_PRECATIVE_LOAN_REPAY ");
        sql.append("              where status = 102 ");
        sql.append("              group by send_code) repayInfo ");
        sql.append("    on cpls.bill_code = repayInfo.send_code ");
        sql.append("  left join (select bt_nvl_num(sum(repay_money),0) repayingMoney, send_code ");
        sql.append("               from CMS_PRECATIVE_LOAN_REPAY ");
        sql.append("              where status >= 11 ");
        sql.append("                and status != 102 ");
        sql.append("              group by send_code) repayingInfo ");
        sql.append("    on cpls.bill_code = repayingInfo.send_code ");
        sql.append(" where 1 = 1 ");
        return sql;
    }

    /**
    *银行借款明细查询
    *@Param
    *@return
    */
    private StringBuilder getprovideDtlSql(){
        StringBuilder sql = new StringBuilder();
        sql.append("select bank.bank_name bankName, ");
        sql.append("       corp.name corpName, ");
        sql.append("       cpls.loan_code billCode, ");
//        sql.append("       cpls.contract_code contractNo, ");
        sql.append("       cpli.contract_no contractNo, ");
        sql.append("       cpls.bill_code sendBillCode, ");
//        sql.append("       cur.cur_name curName, ");
        sql.append("       cpls.cur_code curId, ");
        sql.append("       cpli.pl_money money, ");
        sql.append("       cpli.pl_date plDate, ");
        sql.append("       cpli.repay_date repayDate, ");
        sql.append("       cpli.contract_term contractTerm, ");
        sql.append("       cpls.money sendMoney, ");
        sql.append("       bt_nvl_num(repayInfo.repayMoney,0) repayMoney, ");
        sql.append("       bt_nvl_num(repayingInfo.repayingMoney,0) repayingMoney,");
        sql.append("       (cpls.money - bt_nvl_num(repayInfo.repayMoney,0)) leftMoney ");
        sql.append("  from CMS_PROVIDE_LOAN_SEND cpls ");
        sql.append("  left join CMS_PROVIDE_LOAN_INFO cpli ");
        sql.append("    on cpls.loan_code = cpli.bill_code ");
        sql.append("  left join bt_bank bank ");
        sql.append("    on cpli.bank_id = bank.sys_bank_code ");
        sql.append("  left join sys_corp corp ");
        sql.append("    on cpls.corp_id = corp.id ");
//        sql.append("  left join bt_currency cur ");
//        sql.append("    on cpls.cur_code = cur.id ");
        sql.append("  left join (select bt_nvl_num(sum(repay_money),0) repayMoney, send_code ");
        sql.append("               from CMS_PROVIDE_LOAN_REPAY ");
        sql.append("              where status = 102 ");
        sql.append("              group by send_code) repayInfo ");
        sql.append("    on cpls.bill_code = repayInfo.send_code ");
        sql.append("  left join (select bt_nvl_num(sum(repay_money),0) repayingMoney, send_code ");
        sql.append("               from CMS_PROVIDE_LOAN_REPAY ");
        sql.append("              where status >= 11 and status != 102 ");
        sql.append("              group by send_code) repayingInfo ");
        sql.append("    on cpls.bill_code = repayingInfo.send_code ");
        sql.append("  where 1 = 1  ");
        return sql;
    }

}
