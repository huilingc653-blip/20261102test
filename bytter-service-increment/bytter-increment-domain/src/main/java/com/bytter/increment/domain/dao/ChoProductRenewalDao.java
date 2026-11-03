package com.bytter.increment.domain.dao;
import com.bytter.increment.domain.entity.ChoProductRedeem;
import com.bytter.increment.domain.entity.ChoProductRenewal;
import com.bytter.increment.api.constant.ChoProductReqConstant;
import com.bytter.increment.domain.vo.ChoProductRedeemVo;
import com.bytter.increment.domain.vo.ChoProductReqVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.*;
/**
 * 理财产品续期Dao
 *
 * @author ouyj
 */
@Repository
public class ChoProductRenewalDao extends BaseDao<ChoProductRenewal, String> {
    /**
     * 获取列表数据
     *
     * @param dataTablesInput 输入参数
     * @param joinSql         单位sql
     * @return 列表数据
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql,boolean isRenewal) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select prd.bill_no as billNo, pr.corp_id as corpId, pr.corp_name as corpName, pr.bank_type_id as bankTypeId,pr.bank_type_name as bankTypeName, ");
        sql.append(" pr.bank_name as bankName,pr.bank_Info_Id as bankInfoId,pr.product_code as productCode,pt.product_name as productName, ");
        sql.append(" pr.product_type as productType, pr.bank_acc as bankAcc, pr.buy_money as buyMoney, pr.MOVE_UP_RMK as moveUpRmk, ");
        sql.append(" pr.cur_id as curId, pr.cur_name as curName, pr.interest_start_date as interestStartDate, ");
        sql.append(" pr.buy_date as buyDate, pr.day_limit as dayLimit, pr.cash_date as cashDate, ");
        sql.append(" case when pw.deadline_date is not null then pw.deadline_date else pr.deadline_date end as deadlineDate,  ");
        sql.append(" pt.FINANCIAL_TYPE as financialType, pr.PAY_WAY as payWay, pr.CONTRACT_NO as contractNo, ");
        sql.append(" pr.INCOME_TYPE as incomeType, pr.IS_STORE as isStore, pr.START_CONTRACT_RATE as startContractRate, pr.END_CONTRACT_RATE as endContractRate, ");
        sql.append(" prd.redeem_money as redeemMoney, prd.redeem_date as redeemDate, prd.income_rate as incomeRate, pw.status as status, pw.status as renewalStatus,");
        sql.append(" prd.income_money as incomeMoney, prd.redeem_charge_money as redeemChargeMoney, pr.income_Clause as incomeClause, ");
        sql.append(" pr.REFER_INCOME_RATE as referIncomeRate,pr.is_move_up as isMoveUp,pr.capital_source as capitalSource, ");
        sql.append(" pr.refer_income_money as referIncomeMoney,pr.rmk as rmk, pr.sub_fee_money as reqSubFeeMoney, pr.charge_money as reqChargeMoney, ");
        sql.append(" prd.sub_fee_money as subFeeMoney, prd.charge_money as chargeMoney, pw.id as renewalId, ");
        if (isRenewal) {
            sql.append(" pr.id as id, pr.id as redeemId ");
        } else {
            sql.append(" prd.id as id, pr.id as reqId ");
        }
        sql.append(" from cho_product_req pr ");
        sql.append(" left join cho_product_type pt on pt.product_code = pr.product_code ");
        sql.append(" left join sys_corp sc on sc.id = pr.corp_id ");
        sql.append(" left join bt_bank_type bt on bt.id = pr.bank_Type_Id ");
        sql.append(" left join bt_currency cur on cur.id = pr.cur_id");
        sql.append(" left join cho_product_renewal pw on pw.redeem_id = pr.id and pw.status != :status_2 ");
        sql.append(" left join (SELECT REDEEM_ID AS REDEEMID, MAX(CREATE_DATE) AS MAXDATE FROM CHO_PRODUCT_RENEWAL ");
        sql.append(" WHERE status != :status_2 GROUP BY REDEEM_ID) PWW ON PR.ID = PWW.REDEEMID ");
        sql.append(" left join cho_product_redeem prd on pr.id = prd.req_id ");
        sql.append(" left join ( select req_id as reqId, MAX(CREATE_DATE) as maxDate from cho_product_redeem group by req_id  ) prr on prr.reqId = pr.id ");
        sql.append(" where 1 = 1 ");
        sql.append(" and pr.status != :status110 ");
        sql.append(" and pr.biz_status in (:bizStatusArr) ");
        sql.append(" AND (PWW.MAXDATE = PW.CREATE_DATE OR pww.MAXDATE IS NULL) ");
        sql.append(" and (prr.maxDate = prd.create_date or prr.maxDate is null) ");
        sql.append(" and pt.FINANCIAL_TYPE = 0 ");
        HashMap<String, Object> params = new HashMap<>(2);
        // 是否新增续期表格数据
        if (isRenewal) {
            params.put("bizStatusArr", new Integer[]{ChoProductReqConstant.BIZ_STATUS4, ChoProductReqConstant.BIZ_STATUS8,
                    ChoProductReqConstant.BIZ_STATUS12});
        } else {
            sql.append(" and pw.status in (:statusArr) ");
            params.put("bizStatusArr", new Integer[]{ChoProductReqConstant.BIZ_STATUS4, ChoProductReqConstant.BIZ_STATUS8,
                    ChoProductReqConstant.BIZ_STATUS9, ChoProductReqConstant.BIZ_STATUS10,
                    ChoProductReqConstant.BIZ_STATUS11, ChoProductReqConstant.BIZ_STATUS12});
        }
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        Class classObj = isRenewal ? ChoProductReqVo.class : ChoProductRedeemVo.class;
        params.put("status95", ChoProductReqConstant.QUERY_STATUS_CHECK);
        params.put("status110", ChoProductReqConstant.QUERY_STATUS_FINISH);
        params.put("status_2", ChoProductReqConstant.STATUE_DEL);
        params.put("statusArr", new String[]{ChoProductReqConstant.QUERY_STATUS_BACK, ChoProductReqConstant.QUERY_STATUS_TEMP
                                            ,ChoProductReqConstant.QUERY_STATUS_WAIT_CHECK});
        return getDynamicBySQL(dataTablesInput, sql.toString(), classObj, "prd", false, params);
    }
    /**
     * 获取列表数据
     *
     * @param dataTablesInput
     */
    public DataTablesOutput getDataTablesUnionForSelect(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select prd.id as id, prd.bill_no as billNo, prd.corp_id as corpId, prd.corp_name as corpName, pr.bank_type_id as bankTypeId,");
        sql.append(" pr.bank_name as bankName,pr.bank_Info_Id as bankInfoId,pr.product_code as productCode,pt.product_name as productName, ");
        sql.append(" pr.product_type as productType, pr.bank_acc as bankAcc, pr.buy_money as buyMoney, ");
        sql.append(" pr.cur_id as curId, pr.cur_name as curName, pr.interest_start_date as interestStartDate,pr.deadline_date as deadlineDate, ");
        sql.append(" prd.redeem_money as redeemMoney, prd.redeem_date as redeemDate, prd.income_rate as incomeRate, ");
        sql.append(" prd.income_money as incomeMoney,prd.sub_fee_money as subFeeMoney, prd.redeem_charge_money as redeemChargeMoney, ");
        sql.append(" prd.charge_money as chargeMoney, prd.status as status ");
        sql.append(" from cho_product_redeem prd ");
        sql.append(" left join cho_product_req pr on pr.id = prd.req_id ");
        sql.append(" left join cho_product_type pt on pt.product_code = pr.product_code ");
        sql.append(" where 1 = 1 and prd.status in ('95') and (prd.COLLECTION_CONFIRM = '0' or prd.COLLECTION_CONFIRM is null)");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), ChoProductRedeemVo.class, "prd", false);
    }
    /**
     * 根据ID获取Vo显示实体
     *
     * @param id
     * @return
     */
    public ChoProductRedeemVo getChoProductRedeemVoById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select pr.id as id, pw.id as renewalId, prd.bill_no as billNo, pr.corp_id as corpId, pr.corp_name as corpName, pr.bank_type_id as bankTypeId, ");
        sql.append(" pr.bank_type_name as bankTypeName,pr.bank_name as bankName,pr.bank_Info_Id as bankInfoId,pr.product_code as productCode,pt.product_name as productName, ");
        sql.append(" pr.product_type as productType, pr.bank_acc as bankAcc, pr.buy_money as buyMoney, ");
        sql.append(" pr.buy_date as buyDate, pw.day_limit as dayLimit, pr.cash_date as cashDate, ");
        sql.append(" pr.refer_income_rate as referIncomeRate, pr.income_clause as incomeClause,");
        sql.append(" pr.cur_id as curId, pr.cur_name as curName, pr.interest_start_date as interestStartDate, ");
        sql.append(" case when pw.deadline_date is not null then pw.deadline_date else pr.deadline_date end as deadlineDate,  ");
        sql.append(" case when pw.refer_income_money is not null then pw.refer_income_money else pr.refer_income_money end as referIncomeMoney,  ");
        sql.append(" pr.is_move_up as isMoveUp, pr.capital_source as capitalSource, pr.charge_money as reqChargeMoney, pr.sub_fee_money as reqSubFeeMoney,");
        sql.append(" pt.FINANCIAL_TYPE as financialType, pr.PAY_WAY as payWay, pr.CONTRACT_NO as contractNo, ");
        sql.append(" pr.INCOME_TYPE as incomeType, pr.IS_STORE as isStore, pr.START_CONTRACT_RATE as startContractRate, pr.END_CONTRACT_RATE as endContractRate, ");
        sql.append(" prd.redeem_money as redeemMoney, prd.redeem_date as redeemDate, prd.income_rate as incomeRate, ");
        sql.append(" prd.income_money as incomeMoney,prd.sub_fee_money as subFeeMoney, prd.redeem_charge_money as redeemChargeMoney, ");
        sql.append(" prd.charge_money as chargeMoney, prd.status as status,prd.req_Id as reqId,pr.rmk as rmk,prd.transfer_date as transferDate, ");
        sql.append(" pr.MOVE_UP_RMK as moveUpRmk ");
        sql.append(" from cho_product_renewal pw ");
        sql.append(" left join cho_product_req pr on pr.id = pw.redeem_id ");
        sql.append(" left join cho_product_redeem prd on prd.req_id = pr.id ");
        sql.append(" left join cho_product_type pt on pt.product_code = pr.product_code ");
        sql.append(" where 1 = 1 ");
        sql.append(" and pw.id = :id ");
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, ChoProductRedeemVo.class);
    }
    /**
     * 审批列表数据
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput, String businessSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select pw.id as id, prd.bill_no as billNo, pr.corp_id as corpId, sc.name as corpName, pr.bank_info_id as bankInfoId, ");
        sql.append(" pr.bank_name as bankName,pr.product_code as productCode,pt.product_name as productName, ");
        sql.append(" pr.product_type as productType, pr.bank_acc as bankAcc, pr.buy_money as buyMoney,pr.buy_date as buyDate, ");
        sql.append(" pr.cur_id as curId, pr.cur_name as curName, pr.interest_start_date as interestStartDate, ");
        sql.append(" case when pw.deadline_date is not null then pw.deadline_date else pr.deadline_date end as deadlineDate,  ");
        sql.append(" prd.redeem_money as redeemMoney, prd.redeem_date as redeemDate, prd.income_rate as incomeRate, ");
        sql.append(" prd.income_money as incomeMoney,prd.sub_fee_money as subFeeMoney, prd.redeem_charge_money as redeemChargeMoney, ");
        sql.append(" prd.charge_money as chargeMoney, pr.bank_type_id as bankTypeId, pr.bank_type_name as bankTypeName,pt.FINANCIAL_TYPE as financialType,pr.PAY_WAY as payWay, ");
        sql.append(" pr.CONTRACT_NO as contractNo,pr.START_CONTRACT_RATE as startContractRate, pr.END_CONTRACT_RATE as endContractRate, ");
        sql.append(" pr.REFER_INCOME_RATE as referIncomeRate, prd.id as renewalId ");
        sql.append(" from cho_product_renewal pw ");
        sql.append(" left join cho_product_req pr on pr.id = pw.redeem_id ");
        sql.append(" left join cho_product_redeem prd on prd.req_id = pr.id ");
        sql.append(" left join cho_product_type pt on pt.product_code = pr.product_code ");
        sql.append(" left join sys_corp sc on sc.id = pr.corp_id ");
        sql.append(" where 1 = 1 and pw.status = 11 ");
        sql.append(" and " + businessSql);
        return getDynamicBySQL(dataTablesInput, sql.toString(), ChoProductRedeemVo.class, "prd", false);
    }
    /**
     * 查找赎回状态为待审批和审批通过的指定赎回产品
     *
     * @param reqId
     * @return
     */
    public List<ChoProductRedeem> listProductRedeemed(String reqId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from cho_product_redeem prd where prd.req_id = :reqId and prd.status in ('11','95') ");
        Map<String, Object> params = new HashMap<>();
        params.put("reqId", reqId);
        return listBySQLAliasToBean(sql.toString(), params, ChoProductRedeem.class);
    }
    /**
     * 查找赎回状态为审批通过的指定赎回产品
     *
     * @param reqId
     * @return
     */
    public List<ChoProductRedeem> listProductRedeemApproved(String reqId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from cho_product_redeem prd where prd.req_id = :reqId and prd.status = 95 order by prd.redeem_date,prd.create_date ");
        Map<String, Object> params = new HashMap<>();
        params.put("reqId", reqId);
        return listBySQLAliasToBean(sql.toString(), params, ChoProductRedeem.class);
    }
    /**
     * 查找赎回状态为审批通过的指定赎回产品
     *
     * @param reqIds
     * @return
     */
    public List<ChoProductRedeem> listRedeemedApprovedByReqIds(Set<String> reqIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select prd.req_id as reqId,sum(prd.REDEEM_MONEY) as redeemMoney from cho_product_redeem prd where prd.req_id in (:reqIds) and prd.status = 95 group by prd.req_id ");
        Map<String, Object> params = new HashMap<>();
        params.put("reqIds", reqIds);
        return listBySQLAliasToBean(sql.toString(), params, ChoProductRedeem.class);
    }
    /**
     * 获取指定登记产品的赎回数据
     *
     * @param reqId
     */
    public List<ChoProductRedeem> listChoProductRedeemByReqId(String reqId) {
        StringBuilder hql = new StringBuilder();
        hql.append(" from ChoProductRedeem prd where prd.reqId = :reqId order by prd.redeemDate desc");
        Map params = new HashMap();
        params.put("reqId", reqId);
        return listByHQLAliasToBean(hql.toString(), params, ChoProductRedeem.class);
    }
    /**
     * 获取指定登记产品的赎回数据(去掉已删除的记录)
     *
     * @param reqId
     */
    public List<ChoProductRedeem> listRedeemUnlessDelByReqId(String reqId) {
        StringBuilder hql = new StringBuilder();
        hql.append(" from ChoProductRedeem prd where prd.reqId = :reqId and prd.status <> -2 order by prd.redeemDate desc");
        Map params = new HashMap();
        params.put("reqId", reqId);
        return listByHQLAliasToBean(hql.toString(), params, ChoProductRedeem.class);
    }
    public List<ChoProductRedeem> listByReqIdRedeemDate(String reqId, Date redeemDate) {
        StringBuilder hql = new StringBuilder();
        hql.append(" from ChoProductRedeem prd where prd.reqId = :reqId and prd.redeemDate<= :redeemDate ");
        hql.append(" and prd.status = 95 order by prd.redeemDate desc");
        Map params = new HashMap();
        params.put("reqId", reqId);
        params.put("redeemDate", redeemDate);
        return listByHQLAliasToBean(hql.toString(), params, ChoProductRedeem.class);
    }
    /**
     * 根据单位统计登记的理财信息
     *
     * @param corpId
     * @return
     */
    public int getProductRedeemCountByCorpId(String corpId) {
        String sql = "select count(*) from cho_product_redeem where corp_id=:corpId";
        HashMap<String, Object> params = new HashMap<>();
        params.put("corpId", corpId);
        //0-暂存、11-待审批、-1-审批打回
        sql += " and status in ('0','11','-1')";
        return ((Number) this.createSQLQuery(sql, params).uniqueResult()).intValue();
    }
    /**
     * 根据登记id查询续期列表
     * @param id 登记id
     * @return 续期数据
     */
    public List<ChoProductRenewal> listChoProductRenewalByReqId(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT CASE WHEN pw.update_date IS NULL THEN pw.create_date ELSE pw.update_date END AS updateDate,");
        sql.append(" pw.deadline_date AS deadlineDate, pw.status AS status FROM cho_product_renewal pw ");
        sql.append(" LEFT JOIN cho_product_req pr ON pr.id = pw.redeem_id ");
        sql.append(" WHERE pw.status != :status_2  ");
        sql.append( " and pr.id = :id ");
        sql.append(" ORDER BY updateDate DESC ");
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("id", id);
        params.put("status_2", ChoProductReqConstant.STATUE_DEL);
        return this.listBySQLAliasToBean(sql.toString(), params, ChoProductRenewal.class);
    }
}
