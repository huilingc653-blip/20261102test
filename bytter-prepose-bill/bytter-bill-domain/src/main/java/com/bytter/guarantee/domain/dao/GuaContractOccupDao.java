package com.bytter.guarantee.domain.dao;

import cn.hutool.core.date.DateUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.domain.entity.GuaContractOccup;
import com.bytter.guarantee.domain.vo.*;
import com.bytter.guarantee.dto.GuaContractDTO;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author luxy
 * @Title: GuaContractOccupDao
 * @ProjectName T2_IDEA
 * @Description: 担保合同占用数据访问层
 * @date 2019/4/30 19:50
 */
@Repository
public class GuaContractOccupDao extends BaseDao<GuaContractOccup, String> {
    /**
     * 根据业务id查询担保合同占用信息
     */
    public List<GuaContractOccup> queryGuaContractByBusinessId(String businessId, Integer status) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM GUA_CONTRACT_OCCUP guaOccup where guaOccup.Business_Id=:businessId")
                .append(" and guaOccup.status=:status ");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("businessId", businessId);
        map.put("status", status);
        return listBySQLAliasToBean(sql.toString(), map, GuaContractOccup.class);
    }
    /**
     * 根据业务id和担保合同id查询担保合同占用信息
     */
    public GuaContractOccup queryGuaContractByParams(String businessId, String contractId, Integer status) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM GUA_CONTRACT_OCCUP guaOccup where guaOccup.Business_Id=:businessId")
                .append(" and  guaOccup.contract_Id=:contractId and guaOccup.status=:status ");// 使用中的状态
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("businessId", businessId);
        params.put("contractId", contractId);
        params.put("status", status);
        return getBySQLAliasToBean(sql.toString(), params, GuaContractOccup.class);
    }
    /**
     * 根据业务id删除担保合同占用信息
     */
    public void deleteGuaContractByBusinessId(String businessId, String sysCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(
                "delete FROM GUA_CONTRACT_OCCUP  where GUA_CONTRACT_OCCUP.Business_Id=:businessId and GUA_CONTRACT_OCCUP.syscode=:sysCode");
        params.put("businessId", businessId);
        params.put("sysCode", sysCode);
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    /**
     * 根据业务id和担保合同id查询担保合同占用信息
     */
    public List<GuaContractOccup> queryGuaContractByParams(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("FROM GuaContractOccup guaOccup ");
        sql.append(" where 1 = 1");
        sql.append(" and guaOccup.businessId=:businessId ");
        sql.append(" and guaOccup.status in (:status)  ");// 使用中的状态
        if (params != null && params.get("contractIds") != null) {
            // sql.append(" and guaOccup.contract_Id in (:contractIds)");
            sql.append(" and " + getSQLIn((List<String>) params.get("contractIds"), 900, "guaOccup.contractId"));
        }
        sql.append(" order by  guaOccup.useDate desc");
        return listByHQLAliasToBean(sql.toString(), params, GuaContractOccup.class);
    }
    /**
     * 根据担保合同id获取使用详情
     */
//    public DataTablesOutput getUseContractList(DataTablesInput input, List<String> corpIds) {
//        StringBuilder sql = new StringBuilder();
//        /*
//         * sql.append("select n.rat_code as itemNo,n.contract_no as ratContractNo,n.rat_mode as ratMode,")
//         * .append(" n.money as money,n.begindate as beginDate,n.enddate as enddate,m.use_date as useDate,")
//         * .append(" m.status as status,m.contract_id as contractId,m.syscode as syscode  ")
//         * .append(" from gua_contract_occup m,rat_bkration n where m.business_id=n.id ")
//         * .append(" and m.status <> 0 ");//不取暂存状态的数据
//         */
//        sql.append("select m.item_no as itemNo, ");
//        sql.append("       n.contract_no as ratContractNo, ");
//        sql.append("       n.rat_mode as ratMode, ");
//        sql.append("       n.money as money, ");
//        sql.append("       n.begindate as beginDate, ");
//        sql.append("       n.enddate as endDate, ");
//        sql.append("       m.use_date as useDate, ");
//        sql.append("       m.status as status");
//        sql.append("  from gua_contract_occup m ");
//        sql.append("  left join rat_bkration n ");
//        sql.append("    on m.business_id = n.id ");
//        sql.append("  left join gua_contract_info gci ");
//        sql.append("    on m.contract_id = gci.id ");
//        sql.append("where m.status <> 0  and n.is_hang_guarantee=1 ");
//        // sql.append(" and "+getSQLIn(corpIds,900,"gci.corp_id"));
//        return getDynamicBySQL(input, sql.toString(), GuaContractOccupVO.class, "m", false);
//    }
    public DataTablesOutput getUseContractList(DataTablesInput input, List<String> businessIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("select m.business_id as id,m.item_no as itemNo, ");
        sql.append(" m.use_date as useDate, m.status as status ");
        sql.append(" from gua_contract_occup m ");
        sql.append(" left join gua_contract_info gci on m.contract_id = gci.id ");
        sql.append("where m.status <> 0 ");
        String businessSql = SqlUtil.getInSql(businessIds, "m.business_id", true);
        sql.append(" and ").append(businessSql).append("  ");
        return getDynamicBySQL(input, sql.toString(), GuaContractOccupVO.class, "m", false);
    }
    /**
     * 银行借款挂接担保查询
     * 
     * @param dataTablesInput
     * @param corpIds
     * @return
     */
//    public DataTablesOutput getGuaForCmsProvideLoan(DataTablesInput dataTablesInput, List<String> corpIds) {
//        StringBuilder sql = new StringBuilder();
//        sql.append("select cpli.bill_code as billCode, ");
//        sql.append("       cpli.bank_id as bankId, ");
//        sql.append("       bank.bank_name as bankName,");
//        sql.append("       cpli.contract_no as contractNo, ");
//        sql.append("       cpli.cur_id as curId, ");
//        sql.append("       cur.cur_name as curName, ");
//        sql.append("       cpli.pl_money plMoney, ");
//        sql.append("       cpli.repay_date as repayDate, ");
//        sql.append("       gco.use_date useDate, ");
//        sql.append("       gco.status as status");
//        sql.append("  from gua_contract_occup gco ");
//        sql.append("  left join cms_provide_loan_info cpli ");
//        sql.append("    on gco.business_id = cpli.id ");
//        sql.append("  left join bt_bank bank ");
//        sql.append("    on cpli.bank_id = bank.sys_bank_code ");
//        sql.append("  left join gua_contract_info gci ");
//        sql.append("    on gco.contract_id = gci.id ");
//        sql.append("  left join BT_CURRENCY cur ");
//        sql.append("    on cpli.cur_id = cur.id ");
//        sql.append("where gco.status <> 0   and cpli.is_hang_guarantee=1 ");
//        // sql.append(" and "+getSQLIn(corpIds,900,"cgi.corp_id"));
//        return getDynamicBySQL(dataTablesInput, sql.toString(), GuaContractCmsProvideLoanVO.class, "gco", false);
//    }
    public DataTablesOutput getGuaForCmsProvideLoan(DataTablesInput dataTablesInput, List<String> businessIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("select gco.business_id as id,");
        sql.append(" gco.use_date useDate, gco.status as status ");
        sql.append(" from gua_contract_occup gco ");
        sql.append(" left join gua_contract_info gci on gco.contract_id = gci.id ");
        sql.append(" where gco.status <> 0 ");
        String businessSql = SqlUtil.getInSql(businessIds, "gco.business_id", true);
        sql.append(" and ").append(businessSql).append("  ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), GuaContractCmsProvideLoanVO.class, "gco", false);
    }
    /**
     * 国际结算挂接担保查询
     * 
     * @param dataTablesInput
     * @param corpIds
     * @return
     */
//    public DataTablesOutput getGuaForInternational(DataTablesInput dataTablesInput, List<String> corpIds) {
//        StringBuilder sql = new StringBuilder();
//        sql.append("select lc.applicant, ");
//        sql.append("       corp.name as applicantName, ");
//        sql.append("       lc.beneficiary as beneficiary, ");
//        sql.append("       lc.issuing_bank as issuingBank, ");
//        sql.append("       bank.bank_name as bankName, ");
//        sql.append("       lc.credit_no as creditNo, ");
//        sql.append("       lc.lc_inter as lcInter, ");
//        sql.append("       lc.credit_money as creditMoney, ");
//        sql.append("       lc.contract_no as contractNo, ");
//        sql.append("       lc.issuing_date as issuingDate, ");
//        sql.append("       lc.expiry_date as expiryDate, ");
//        sql.append("       gco.use_date as useDate, ");
//        sql.append("       gco.status as status");
//        sql.append("  from gua_contract_occup gco ");
//        sql.append("  left join lc_enrol lc ");
//        sql.append("    on gco.business_id = lc.id ");
//        sql.append("  left join gua_contract_info gci ");
//        sql.append("    on gco.contract_id = gci.id ");
//        sql.append("  left join SYS_CORP corp ");
//        sql.append("    on lc.applicant = corp.id ");
//        sql.append("  left join bt_bank bank ");
//        sql.append("    on lc.issuing_bank = bank.sys_bank_code ");
//        sql.append("where gco.status <> 0 and lc.lc_type = '0' and lc.is_hang_guarantee=1 ");// 0开证1收证
//        // sql.append(" and "+getSQLIn(corpIds,900,"cgi.corp_id"));
//        return getDynamicBySQL(dataTablesInput, sql.toString(), GuaContractInternationalVO.class, "gco", false);
//    }
    public DataTablesOutput getGuaForInternational(DataTablesInput dataTablesInput, List<String> businessIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("select gco.business_id as id,");
        sql.append(" gco.use_date useDate, gco.status as status ");
        sql.append(" from gua_contract_occup gco ");
        sql.append(" left join gua_contract_info gci on gco.contract_id = gci.id ");
        sql.append(" where gco.status <> 0 ");
        String businessSql = SqlUtil.getInSql(businessIds, "gco.business_id", true);
        sql.append(" and ").append(businessSql).append("  ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), GuaContractInternationalVO.class, "gco", false);
    }
    /**
     * 承兑汇票挂接担保查询
     * 
     * @param dataTablesInput
     * @param corpIds
     * @return
     */
//    public DataTablesOutput getGuaForbill(DataTablesInput dataTablesInput, List<String> corpIds) {
//        StringBuilder sql = new StringBuilder();
//        sql.append("select vice.app_no as appNo, ");
//        sql.append("       v.bill_type as billType, ");
//        sql.append("       v.BILL_FLAG as billFlag, ");
//        sql.append("       v.orig_no as origNo, ");
//        sql.append("       v.bill_money as billMoney, ");
//        sql.append("       v.Payer as payer, ");
//        sql.append("       v.Payee as payee, ");
//        sql.append("       v.acceptor as acceptor, ");
//        sql.append("       v.start_date as startDate, ");
//        sql.append("       v.end_date as endDate,");
//        sql.append("       gco.use_date as useDate,");
//        sql.append("       gco.status as status");
//        sql.append("  from gua_contract_occup gco");
//        sql.append("  left join gua_contract_info gci");
//        sql.append("    on gco.contract_id = gci.id");
//        sql.append("  left join ab_voucherinfo_vice vice");
//        sql.append("    on gco.business_id = vice.id");
//        sql.append("  left join ab_voucherinfo v");
//        sql.append("    on vice.vid = v.id ");
//        sql.append(" where gco.status <> 0  and vice.bear_model=1 ");
//        // sql.append(" and "+getSQLIn(corpIds,900,"cgi.corp_id"));
//        return getDynamicBySQL(dataTablesInput, sql.toString(), GuaContractBillVO.class, "gco", false);
//    }
    public DataTablesOutput getGuaForbill(DataTablesInput dataTablesInput, List<String> businessIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("select gco.business_id as id,");
        sql.append(" gco.use_date as useDate, gco.status as status");
        sql.append(" from gua_contract_occup gco ");
        sql.append(" left join gua_contract_info gci on gco.contract_id = gci.id ");
        sql.append(" where gco.status <> 0 ");
        String businessSql = SqlUtil.getInSql(businessIds, "gco.business_id", true);
        sql.append(" and ").append(businessSql).append("  ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), GuaContractBillVO.class, "gco", false);
    }
    /**
     * 内部贷款挂接担保查询
     * 
     * @param dataTablesInput
     * @param corpIds
     * @return
     */
//    public DataTablesOutput getGuaForloan(DataTablesInput dataTablesInput, List<String> corpIds) {
    public DataTablesOutput getGuaForloan(DataTablesInput dataTablesInput, List<String> businessIds) {
        //TODO 内部贷款SQL改造重构
        StringBuilder sql = new StringBuilder();
        sql.append("select gco.business_id as id,");
        sql.append(" gco.use_date as useDate, gco.status as status");
        sql.append(" from gua_contract_occup gco ");
        sql.append("where gco.status <> 0 ");
        String businessSql = SqlUtil.getInSql(businessIds, "gco.business_id", true);
        sql.append(" and ").append(businessSql).append("  ");
//        sql.append("select cli.bill_code as billCode, ");
//        sql.append("       cli.name as loanGenre, ");
//        sql.append("       cli.contract_code as contractCode, ");
//        sql.append("       cli.cur_code as curCode, ");
//        sql.append("       cur.cur_name as curName, ");
//        sql.append("       cli.loan_money as loanMoney, ");
//        sql.append("       cli.loan_date as loanDate, ");
//        sql.append("       cli.repay_date as repayDate, ");
//        sql.append("       gco.use_date as useDate, ");
//        sql.append("       gco.status as status");
//        sql.append("  from gua_contract_occup gco ");
//        sql.append("  left join  ");
//        sql.append(
//                " ( select loan.*,lType.name  from  CMS_LOAN_INFO loan left join cms_loan_type lType on loan.loan_genre=lType.code  where lType.type=1)cli ");
//        sql.append("    on gco.business_id = cli.id ");
//        sql.append("  left join BT_CURRENCY cur ");
//        sql.append("    on cli.cur_code = cur.id ");
//        sql.append("where gco.status <> 0  and cli.is_hang_guarantee=1 ");
        // sql.append(" and "+getSQLIn(corpIds,900,"cgi.corp_id"));
//        return getDynamicBySQL(dataTablesInput, sql.toString(), GuaContractCmsLoanVO.class, "gco", false);
        return getDynamicBySQL(dataTablesInput, sql.toString(), GuaContractCmsLoanVO.class, "gco", false);
    }
    /**
     * 内部贷款挂接担保查询
     * 
     * @param dataTablesInput
     * @param corpIds
     * @return
     */
//    public DataTablesOutput getGuaForInl(DataTablesInput dataTablesInput, List<String> corpIds) {
//        StringBuilder sql = new StringBuilder();
//        sql.append("select info.bill_No, ");
//        sql.append("       info.gua_contract,");
//        sql.append("       info.cur_id,");
//        sql.append("       info.money,");
//        sql.append("       info.start_date,");
//        sql.append("       info.end_date,");
//        sql.append("       gco.use_date as useDate, ");
//        sql.append("       gco.status as status");
//        sql.append("  from gua_contract_occup gco ");
//        sql.append("  left join CMS_INL_INFO info");
//        sql.append("    on gco.business_id = info.id ");
//        sql.append("where gco.status <> 0  and info.is_hang_guarantee = '1' ");
//        // sql.append(" and "+getSQLIn(corpIds,900,"cgi.corp_id"));
//        return getDynamicBySQL(dataTablesInput, sql.toString(), GuaContractCmsInlVO.class, "gco", false);
//    }
    public DataTablesOutput getGuaForInl(DataTablesInput dataTablesInput, List<String> businessIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("select gco.business_id as id,");
        sql.append(" gco.use_date as useDate, gco.status as status");
        sql.append(" from gua_contract_occup gco ");
        sql.append("where gco.status <> 0 ");
        String businessSql = SqlUtil.getInSql(businessIds, "gco.business_id", true);
        sql.append(" and ").append(businessSql).append("  ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), GuaContractCmsInlVO.class, "gco", false);
    }
    /**
     * 债券挂接担保查询
     * 
     * @param input
     *            前端传参
     * @param corpIds
     *            单位id集合
     * @return 表格数据
     */
//    public DataTablesOutput getGuaForCmsBond(DataTablesInput input, List<String> corpIds) {
//        StringBuilder sql = new StringBuilder();
//        sql.append("select info.BOND_ISSUE_NUM as bondIssueNum, ");
//        sql.append("       r.BOND_CUR as oriCurId,");
//        sql.append("       info.issue_money as oriMoney,");
//        sql.append("       info.issue_start_date as issueStartDate,");
//        sql.append("       info.issue_end_date as issueEndDate,");
//        sql.append("       gco.use_date as useDate, ");
//        sql.append("       gco.status as status");
//        sql.append("  from gua_contract_occup gco ");
//        sql.append("  left join CMS_BOND_ISSUE_INFO info on gco.business_id = info.id");
//        sql.append("  left join CMS_BOND_REGISTER_INFO r on info.register_id = r.id  ");
//        sql.append("where gco.status <> 0  and info.IS_HOOK_GUARANTEE = '1' ");
//        return getDynamicBySQL(input, sql.toString(), GuaContractCmsBondVo.class, "gco", false);
//    }
    public DataTablesOutput getGuaForCmsBond(DataTablesInput input, List<String> businessIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("select info.BOND_ISSUE_NUM as bondIssueNum, ");
        sql.append("       bc.cur_name as oriCurName,");
        sql.append("       info.issue_money as oriMoney,");
        sql.append("       info.issue_start_date as issueStartDate,");
        sql.append("       info.issue_end_date as issueEndDate,");
        sql.append("       gco.use_date as useDate, ");
        sql.append("       gco.status as status");
        sql.append("  from gua_contract_occup gco ");
        sql.append("  inner join CMS_BOND_ISSUE_INFO info on gco.business_id = info.id");
        sql.append("  left join CMS_BOND_REGISTER_INFO r on info.register_id = r.id  ");
        sql.append("  LEFT JOIN bt_currency bc ON bc.id = r.BOND_CUR ");
        sql.append("where gco.status <> 0 ");
        return getDynamicBySQL(input, sql.toString(), GuaContractCmsBondVo.class, "gco", false);
    }
    /**
     * 融资租赁挂接担保查询
     * 
     * @param dataTablesInput
     * @param corpIds
     * @return
     */
//    public DataTablesOutput getGuaForLease(DataTablesInput dataTablesInput, List<String> corpIds) {
//        StringBuilder sql = new StringBuilder();
//        sql.append("select info.LEASE_NO, ");
//        sql.append("       info.LEASE_CODE,");
//        sql.append("       info.ORI_CURID,");
//        sql.append("       info.ORI_MONEY,");
//        sql.append("       info.BEGIN_DATE,");
//        sql.append("       info.END_DATE,");
//        sql.append("       gco.use_date as useDate, ");
//        sql.append("       gco.status as status");
//        sql.append("  from gua_contract_occup gco ");
//        sql.append("  left join CMS_FINANCING_LEASE_INFO info");
//        sql.append("    on gco.business_id = info.id ");
//        sql.append("where gco.status <> 0  and info.is_hang_guarantee = '1' ");
//        // sql.append(" and "+getSQLIn(corpIds,900,"cgi.corp_id"));
//        return getDynamicBySQL(dataTablesInput, sql.toString(), GuaContractCmsLeaseVO.class, "gco", false);
//    }
    public DataTablesOutput getGuaForLease(DataTablesInput dataTablesInput, List<String> businessIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("select gco.business_id as id,");
        sql.append(" gco.use_date as useDate, gco.status as status");
        sql.append(" from gua_contract_occup gco ");
        sql.append("where gco.status <> 0 ");
        String businessSql = SqlUtil.getInSql(businessIds, "gco.business_id", true);
        sql.append(" and ").append(businessSql).append("  ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), GuaContractCmsLeaseVO.class, "gco", false);
    }
    /**
     * 资产证券化挂接担保查询
     * 
     * @param dataTablesInput
     * @param corpIds
     * @return
     */
//    public DataTablesOutput getGuaForSecurity(DataTablesInput dataTablesInput, List<String> corpIds) {
//        StringBuilder sql = new StringBuilder();
//        sql.append("select iss.SECURITY_ISSUE_CODE as securityIssueCode, ");
//        sql.append("       iss.UNDERWRITER as underwriter,");
//        sql.append("      iss.BATCH_NO as batchNo,");
//        sql.append("      iss.LEVEL_NAME as levelName,");
//        sql.append("       iss.ISSUE_MONEY as issueMoney,");
//        sql.append("       iss.ISSUE_DATE as issueDate,");
//        sql.append("      iss.EXPECT_END_DATE as expectEndDate,");
//        sql.append("       gco.use_date as useDate, ");
//        sql.append("       gco.status as status");
//        sql.append("  from gua_contract_occup gco ");
//        sql.append("  left join CMS_SECURITY_ISSUE iss");
//        sql.append("    on gco.business_id = iss.id ");
//        sql.append("where gco.status <> 0  and iss.is_hang_guarantee = '1' ");
//        // sql.append(" and "+getSQLIn(corpIds,900,"cgi.corp_id"));
//        return getDynamicBySQL(dataTablesInput, sql.toString(), GuaContractCmsSecurityVO.class, "gco", false);
//    }
    public DataTablesOutput getGuaForSecurity(DataTablesInput dataTablesInput, List<String> businessIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("select iss.SECURITY_ISSUE_CODE as securityIssueCode, ");
        sql.append("       iss.UNDERWRITER as underwriter,");
        sql.append("       iss.BATCH_NO as batchNo,");
        sql.append("       iss.LEVEL_NAME as levelName,");
        sql.append("       iss.ISSUE_MONEY as issueMoney,");
        sql.append("       iss.ISSUE_DATE as issueDate,");
        sql.append("       iss.EXPECT_END_DATE as expectEndDate,");
        sql.append("       gco.use_date as useDate, ");
        sql.append("       gco.status as status ");
        sql.append("  from gua_contract_occup gco ");
        sql.append("  inner join CMS_SECURITY_ISSUE iss on gco.business_id = iss.id ");
        sql.append("where gco.status <> 0 ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), GuaContractCmsSecurityVO.class, "gco", false);
    }
    /**
     * 根据业务id和业务编号查询担保合同占用信息
     */
    public List<GuaContractOccup> queryGuaContractOccupy(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("FROM GuaContractOccup guaOccup ");
        sql.append(" where 1 = 1");
        sql.append(" and guaOccup.businessId=:businessId ");
        if (params.get("itemNo") != null) {
            sql.append(" and guaOccup.itemNo=:itemNo ");
        }
        return listByHQLAliasToBean(sql.toString(), params, GuaContractOccup.class);
    }
    /**
     * 状态修改
     * 
     * @param params
     */
    public void updateGuaStatus(Map<String, Object> params, Integer status) {
        StringBuilder sql = new StringBuilder();
        sql.append(" update GUA_CONTRACT_OCCUP set status = " + status + " where ");
        sql.append(" business_id = :businessId ");
        sql.append(" and status in (:status) ");
        if (null != params && params.get("contractIds") != null
                && ((List<String>) params.get("contractIds")).size() > 0) {
            sql.append(" and CONTRACT_ID in (:contractIds) ");
        }
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    /**
     * 获取担保占用信息
     * 
     * @param params
     * @return
     */
    public List<GuaContractDTO> getGuaContractOccup(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select gco.contract_id id,");
        sql.append("        gci.contract_code contractCode,");
        sql.append("        gci.cur_name curName,");
        sql.append("        gci.gua_no guaNo,");
        sql.append("        gci.start_date startDate,");
        sql.append("        gci.end_date endDate,");
        sql.append("        gci.cur_id curId,");
        sql.append("        gci.gua_money guaMoney,");
        sql.append("        gci.gua_person guaPerson,");
        sql.append("        gco.gua_rmk rmk,");
        sql.append("        gco.status status");
        sql.append("   from gua_contract_occup gco");
        sql.append("   left join GUA_CONTRACT_INFO gci");
        sql.append("     on gco.contract_id = gci.id");
//        sql.append("   left join bt_currency cur ");
//        sql.append("     on gci.cur_id = cur.id");
        sql.append("     where gco.business_id = :businessId ");
        if (params != null && params.get("itemNo") != null) {
            sql.append("           and gco.item_no = :itemNo  ");
        }
        return listBySQLAliasToBean(sql.toString(), params, GuaContractDTO.class);
    }

    /**
     * 查询担保被占用情况
     * @return
     */
    public List<GuaContractOccupDTO> listUseGua(String sysCode, String startDate, String endDate, String status) {
        StringBuilder sql = new StringBuilder();
        sql.append("select m.business_id as id,m.business_id as businessId,m.item_no as itemNo, ");
        sql.append(" m.use_date as useDate, m.status as status ");
        sql.append(" from gua_contract_occup m ");
        Map<String, Object> params = new HashMap<>(4);
        sql.append(" where m.sysCode = :sysCode and m.status <>0 ");
        if (StringUtils.isNotEmpty(startDate)) {
            sql.append(" and m.use_date >= :startDate ");
            params.put("startDate", DateUtil.beginOfDay(DateUtil.parse(startDate,"yyyy-MM-dd")).toJdkDate());
        }
        if (StringUtils.isNotEmpty(endDate)) {
            sql.append(" and :endDate >= m.use_date ");
            params.put("endDate", DateUtil.endOfDay(DateUtil.parse(endDate,"yyyy-MM-dd")).toJdkDate());
        }
        if (StringUtils.isNotEmpty(status)) {
            sql.append(" and m.status = :status ");
            params.put("status", status);
        }
        params.put("sysCode", sysCode);
        return listBySQLAliasToBean(sql.toString(), params, GuaContractOccupDTO.class);
    }
}
