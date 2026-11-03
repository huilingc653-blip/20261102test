package com.bytter.increment.domain.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.api.constant.ChoProductReqConstant;
import com.bytter.increment.api.dto.ChoProductReqDTO;
import com.bytter.increment.domain.entity.ChoProductReq;
import com.bytter.increment.domain.vo.ChoProductDetailedVo;
import com.bytter.increment.domain.vo.ChoProductReqVo;
import com.bytter.system.api.dto.BankTypeDTO;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: 理财产品
 * @author shenc
 * @date 2019-03-26 14:03:30
 * @version 1.0
 *
 */
@Repository
public class ChoProductReqDao extends BaseDao<ChoProductReq,String> {
    /**
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select pr.id as id, pr.corp_id as corpId, pr.corp_name as corpName, pr.bank_info_id as bankInfoId, pr.bank_name as bankName, ");
        sql.append(" pr.product_code as productCode, pt.product_name as productName, pr.product_type as productType,pr.bank_Type_Id as bankTypeId,pr.bank_Type_name as bankTypeName,");
        sql.append(" pr.INCOME_CLAUSE as incomeClause,pr.bank_acc as bankAcc,pr.buy_money as buyMoney,pr.cur_id as curId, pr.cur_name as curName, ");
        sql.append(" pr.interest_start_date as interestStartDate, pr.Deadline_Date as deadlineDate,pr.refer_income_rate as referIncomeRate, ");
        sql.append(" pr.is_move_up as isMoveUp, pr.status as status, pt.FINANCIAL_TYPE as financialType, pr.PAY_WAY as payWay, pr.CONTRACT_NO as contractNo, ");
        sql.append(" pr.INCOME_TYPE as incomeType, pr.IS_STORE as isStore, pr.START_CONTRACT_RATE as startContractRate, pr.END_CONTRACT_RATE as endContractRate, ");
        sql.append(" pr.IS_MAINTAIN_VAL as isMaintainVal, pr.END_REFER_INCOME_MONEY as endReferIncomeMoney, pr.END_REFER_INCOME_RATE as endReferIncomeRate, ");
        sql.append(" pr.REFER_INCOME_MONEY as referIncomeMoney, pr.move_up_rmk as moveUpRmk,pr.rmk as rmk,  pr.charge_money as chargeMoney, ");
        sql.append("  pr.sub_fee_money as subFeeMoney, pr.attachment_id as attachmentId, pr.is_guaranteed as isGuaranteed,pr.capital_source as capitalSource,");
        sql.append(" pr.daily_net_val as dailyNetVal, pr.portion as portion,");
        sql.append(" pr.buy_date as buyDate  ,pr.buy_apply_status as buyApplyStatus  ");
        sql.append(" from cho_product_req pr ");
        sql.append(" left join cho_product_type pt on pt.product_code = pr.product_code ");
        sql.append(" where 1 = 1 ");
        sql.append(" and pr.status in ('0','-1') ");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        return getDynamicBySQL(dataTablesInput,sql.toString(), ChoProductReqVo.class,"pr",false);
    }
    /**
     * 根据ID查找
     * @param id
     * @return
     */
    public ChoProductReqVo getChoProductReqVoById(String id){
        StringBuilder sql = new StringBuilder();
        sql.append(" select pr.id as id, pr.corp_id as corpId, sc.name as corpName, pr.bank_info_id as bankInfoId, pr.bank_name as bankName, ");
        sql.append(" pr.product_code as productCode, pt.product_name as productName, pr.product_type as productType, ");
        sql.append(" pr.INCOME_CLAUSE as incomeClause,pr.bank_acc as bankAcc,pr.buy_money as buyMoney,pr.cur_id as curId, cur.cur_name as curName, ");
        sql.append(" pr.interest_start_date as interestStartDate, pr.Deadline_Date as deadlineDate,pr.refer_income_rate as referIncomeRate, ");
        sql.append(" pr.is_move_up as isMoveUp, pr.status as status, pr.bank_type_id as bankTypeId, bt.type_name as bankTypeName,pr.bill_no as billNo, ");
        sql.append(" pr.cash_date as cashDate, pr.sub_fee_money as subFeeMoney, pr.attachment_id as attachmentId, ");
        sql.append(" pr.charge_money as chargeMoney, pr.move_up_rmk as moveUpRmk,pr.rmk as rmk, ");
        sql.append(" pr.daily_net_val as dailyNetVal, pr.portion as portion,");
        sql.append(" pr.bank_acc_id as bankAccId, pr.buy_date as buyDate, pr.day_limit as dayLimit, pr.refer_income_money as referIncomeMoney, ");
        sql.append(" pr.create_by as createBy, pr.create_date as createDate,pr.is_guaranteed as isGuaranteed,pr.capital_source as capitalSource,  ");
        sql.append(" pt.FINANCIAL_TYPE as financialType, pr.PAY_WAY as payWay, pr.CONTRACT_NO as contractNo, ");
        sql.append(" pr.END_REFER_INCOME_MONEY as endReferIncomeMoney, pr.END_REFER_INCOME_RATE as endReferIncomeRate, ");
        sql.append(" case when pr.IS_MAINTAIN_VAL is null then '1' else pr.IS_MAINTAIN_VAL end isMaintainVal, ");
        sql.append(" pr.INCOME_TYPE as incomeType, pr.IS_STORE as isStore, pr.START_CONTRACT_RATE as startContractRate, pr.END_CONTRACT_RATE as endContractRate, pr.CERTIFICATE_WAY as certificateWay ");
        sql.append(" ,pr.buy_apply_status as buyApplyStatus ");
        sql.append(" from cho_product_req pr ");
        sql.append(" left join cho_product_type pt on pt.product_code = pr.product_code ");
        sql.append(" left join sys_corp sc on sc.id = pr.corp_id ");
        sql.append(" left join bt_bank_type bt on bt.id = pr.bank_Type_Id ");
        sql.append(" left join bt_currency cur on cur.id = pr.cur_id");
        sql.append(" where 1 = 1");
        sql.append(" and pr.id = :id");
        Map<String,String> params = new HashMap<>();
        params.put("id",id);
        return getBySQLAliasToBean(sql.toString(),params, ChoProductReqVo.class);
    }
    public DataTablesOutput<ChoProductReqVo> dataTableApprove(DataTablesInput dataTablesInput, String businessSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select pr.id as id, pr.corp_id as corpId, pr.corp_name as corpName, pr.bank_info_id as bankInfoId, pr.bank_name as bankName, ");
        sql.append(" pr.product_code as productCode, pt.product_name as productName, pr.product_type as productType, ");
        sql.append(" pr.INCOME_CLAUSE as incomeClause,pr.bank_acc as bankAcc,pr.buy_money as buyMoney,pr.cur_id as curId, pr.cur_name as curName, ");
        sql.append(" pr.interest_start_date as interestStartDate, pr.Deadline_Date as deadlineDate,pr.refer_income_rate as referIncomeRate, ");
        sql.append(" pt.FINANCIAL_TYPE as financialType, ");
        sql.append(" pr.is_move_up as isMoveUp, pr.status as status, pr.bank_type_id as bankTypeId, pr.bank_Type_name as bankTypeName,pr.is_guaranteed as isGuaranteed,pr.capital_source as capitalSource, ");
        sql.append(" pr.INCOME_TYPE as incomeType, pr.IS_STORE as isStore, pr.START_CONTRACT_RATE as startContractRate, pr.END_CONTRACT_RATE as endContractRate, ");
        sql.append(" pr.buy_date as buyDate, pr.CONTRACT_NO as contractNo, pr.PAY_WAY as payWay ");
        sql.append(" from cho_product_req pr ");
        sql.append(" left join cho_product_type pt on pt.product_code = pr.product_code ");
        sql.append(" where 1 = 1 ");
        sql.append(" and pr.status = '11' ");
        if (!StringUtils.isBlank(businessSql)){
            sql.append(" and " + businessSql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), ChoProductReqVo.class, "f", false);
    }
    public DataTablesOutput getDataTablesValidSign(DataTablesInput dataTablesInput, Date deadlineDate, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select pr.id as id, pr.bill_no as billNo, pr.bank_type_id as bankTypeId, pr.bank_Type_name as bankTypeName,pr.bank_name as bankName, ");
        sql.append(" pr.product_code as productCode, pr.product_type as productType,pr.is_move_up as isMoveUp, ");
        sql.append(" pt.product_name as productName, pr.cur_id as curId, pr.cur_name as curName, pr.refer_income_money as referIncomeMoney,pr.corp_id as corpId, pr.corp_name as corpName, ");
        sql.append(" pr.bank_acc_id as bankAccId, pr.bank_acc as bankAcc, pr.buy_money as buyMoney, pr.income_clause as incomeClause, ");
        sql.append(" pr.buy_date as buyDate, pr.day_limit as dayLimit, pr.interest_start_date as interestStartDate, ");
        sql.append(" pr.create_by as createBy, pr.create_date as createDate,pr.is_guaranteed as isGuaranteed,pr.capital_source as capitalSource,  ");
        sql.append(" pt.FINANCIAL_TYPE as financialType, pr.PAY_WAY as payWay, pr.CONTRACT_NO as contractNo, ");
        sql.append(" pr.sub_fee_money as subFeeMoney, pr.charge_money as chargeMoney, ");
        sql.append(" pr.daily_net_val as dailyNetVal, pr.portion as portion,");
        sql.append(" pr.INCOME_TYPE as incomeType, pr.IS_STORE as isStore, pr.START_CONTRACT_RATE as startContractRate, pr.END_CONTRACT_RATE as endContractRate, ");
        sql.append(" pr.IS_MAINTAIN_VAL as isMaintainVal, pr.END_REFER_INCOME_MONEY as endReferIncomeMoney, pr.END_REFER_INCOME_RATE as endReferIncomeRate, ");
        sql.append("  pr.move_up_rmk as moveUpRmk,pr.rmk as rmk, ");
        sql.append(" pr.deadline_date as deadlineDate, pr.cash_date as cashDate, pr.refer_income_rate as  referIncomeRate, pr.CERTIFICATE_WAY as certificateWay ");
        sql.append(" from ( select * from cho_product_req pr1 where pr1.is_move_up = 1  ");
        sql.append(" union select * from cho_product_req pr2 where pr2.is_move_up = 0 and pr2.deadline_date <= :deadlineDate) pr ");
        sql.append(" left join cho_product_type pt on pt.product_code = pr.product_code ");
        sql.append(" where 1 = 1 and pr.status = 95 and (pr.redeem_status not in ('0','11') or pr.redeem_status is null) ");
        // 当有理财续期未审批通过数据时，无法做理财赎回
        sql.append(" AND PR.ID NOT IN (SELECT PRR.id FROM CHO_PRODUCT_RENEWAL PW LEFT JOIN CHO_PRODUCT_REQ PRR ");
        sql.append(" ON PW.REDEEM_ID = PRR.ID WHERE (PW.STATUS IN (-1, 0, 11))) ");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        Map params = new HashMap();
        params.put("deadlineDate",deadlineDate);
        return getDynamicBySQL(dataTablesInput,sql.toString(), ChoProductReqVo.class,"pr",false,params);
    }
    public List<ChoProductReqVo> listChoProducted(List<String> corpIds, ChoProductDetailedVo detailedVo){
        StringBuilder sql = new StringBuilder();
        Map<String,Object> params = new HashMap<>();
        sql.append(" select pr.id as id,pr.corp_id as corpId, pr.corp_name as corpName, pr.bank_type_id as bankTypeId, pr.bank_type_name as bankTypeName,pr.bank_name as bankName, ");
        sql.append(" pr.product_type as productType, pr.product_code as productCode, pt.product_name as productName, ");
        sql.append(" pr.bank_acc as bankAcc,pr.buy_money as buyMoney,pr.cur_id as curId, pr.cur_name as curName, pr.income_clause as incomeClause, ");
        sql.append(" pr.buy_date as buyDate,pr.interest_start_date as interestStartDate, ");
        sql.append(" pr.day_limit as dayLimit,pr.deadline_date as deadlineDate,pr.refer_income_money as referIncomeMoney, ");
        sql.append(" pr.refer_income_rate as referIncomeRate,prd.lastRedeemDate as lastRedeemDate ");
        sql.append("   from cho_product_req pr ");
        sql.append("   left join (select req_id as reqId, max(redeem_date) as lastRedeemDate from cho_product_redeem r where r.status = '95' ");
        sql.append(" and r.create_date = (select max(create_date) from cho_product_redeem r2 where r.req_id=r2.req_id and r.redeem_date=r2.redeem_date)");
        sql.append("   group by req_id) prd on pr.id = prd.reqId ");
        sql.append("   left join cho_product_type pt on pt.product_code = pr.product_code ");
        sql.append("  where 1 = 1 ");
        sql.append("    and pr.status >= 95 ");
        //加入手动选择的单位过滤
        if (null != detailedVo.getCorpId() && !StringUtils.isBlank(detailedVo.getCorpId().trim())){
            sql.append(" and "+ SqlUtil.getInSql(detailedVo.getCorpId().trim().split(","),"pr.corp_id",true));
        }else{
            //页面单位选择框为空,根本用户的资源权限过滤单位
            sql.append(" and "+ SqlUtil.getInSql(corpIds,"pr.corp_id",true));
        }
        //银行类别过滤
        if (null != detailedVo.getBankTypeId() && !StringUtils.isBlank(detailedVo.getBankTypeId().trim())){
            sql.append(" and "+ SqlUtil.getInSql(detailedVo.getBankTypeId().trim().split(","),"pr.bank_type_id",true));
        }
        //产品类别过滤
        if (null != detailedVo.getProductType() && !StringUtils.isBlank(detailedVo.getProductType().trim())){
            sql.append(" and pr.product_type = :productType ");
            params.put("productType",detailedVo.getProductType().trim());
        }
        //过滤币别
        if (null != detailedVo.getCurId() && !StringUtils.isBlank(detailedVo.getCurId().trim())){
            sql.append(" and "+ SqlUtil.getInSql(detailedVo.getCurId().trim().split(","),"pr.cur_id",true));
        }
        //过滤是否到期
        if (null != detailedVo.getIsDeadline() && !StringUtils.isBlank(detailedVo.getIsDeadline().trim())){
            String[] isDeadlineArr = detailedVo.getIsDeadline().split(CommonConstant.COMMA_SPLIT);
            if (isDeadlineArr.length < 2) {
                if (CommonConstant.YES.equals(isDeadlineArr[0])) {
                    sql.append(" and pr.deadline_date < :deadlineDate ");
                } else {
                    sql.append(" and (pr.deadline_date >= :deadlineDate or pr.deadline_date is null) ");
                }
                params.put("deadlineDate", new Date());
            }
        }
        //过滤查询开始时间
        if (!EmptyUtils.isEmpty(detailedVo.getCurrentStartDate())){
            sql.append(" and (prd.lastRedeemDate >= :startDate or prd.lastRedeemDate is null or pr.status <> '110') ");
            params.put("startDate",detailedVo.getCurrentStartDate());
        }
        //过滤查询结束时间
        if (!EmptyUtils.isEmpty(detailedVo.getCurrentEndDate())){
            sql.append(" and pr.interest_start_date <= :endDate ");
            params.put("endDate",detailedVo.getCurrentEndDate());
        }
        sql.append(" order by pr.product_code ");
        return this.listBySQLAliasToBean(sql.toString(),params, ChoProductReqVo.class);
    }
    /**
     * 根据单位统计登记的理财信息
     * @param corpId
     * @param busType del:单位删除 disable:禁用 change:上级变更
     * @return
     */
    public int getProductReqCountByCorpId(String corpId, String busType){
        String sql="select count(*) from cho_product_req where corp_id=:corpId and status<>:status";
        HashMap<String,Object> params=new HashMap<>();
        params.put("corpId",corpId);
        params.put("status", CommonConstant.STATUE_DELETE);
        if(!"del".equals(busType)){
            //0-暂存、11-待审批、-1-审批打回
            sql+=" and status in ('0','11','-1')";
        }
        return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
    }
    /**
     * 根据单位统计登记的未全部赎回的理财信息
     * @param corpId
     * @return
     */
    public int getProductReqRedeemCountByCorpId(String corpId){
        String sql="select count(*) from cho_product_req where corp_id=:corpId and status>=:statusStart and status<:statusEnd";
        HashMap<String,Object> params=new HashMap<>();
        params.put("corpId",corpId);
        params.put("statusStart", ChoProductReqConstant.STATUE_CHECK);
        params.put("statusEnd", ChoProductReqConstant.STATUE_FINISH);
        return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
    }
    /**
     * 更新理财登记表的业务状态
     *
     * @param choProductReq 登记实体
     */
    public void updateChoProductReq(ChoProductReq choProductReq) {
        StringBuilder sql = new StringBuilder("update cho_product_req set bill_no = bill_no ");
        HashMap<String,Object> params=new HashMap<>(4);
        if (choProductReq.getBizStatus() != null) {
            sql.append(" ,biz_status = :bizStatus ");
            params.put("bizStatus", choProductReq.getBizStatus());
        }
        if (choProductReq.getRedeemStatus() != null) {
            sql.append(" ,redeem_status = :redeemStatus ");
            params.put("redeemStatus", choProductReq.getRedeemStatus());
        }
        if (choProductReq.getStatus() != null) {
            sql.append(" ,status = :status ");
            params.put("status", choProductReq.getStatus());
        }
        sql.append(" where id = :id ");
        params.put("id", choProductReq.getId());
        this.batchSQLExecute(sql.toString(), params);
    }

    /**
     * 根据单位id，到期日期获取理财产品信息
     * @param corpId
     * @param deadlineDate
     * @return
     */
    public List<ChoProductReqDTO> listChoProductReqByDate(String corpId,Date deadlineDate){
        Map<String, Object> params = new HashMap<>();
        params.put("corpId",corpId);
        params.put("deadlineDate",deadlineDate);
        params.put("status",ChoProductReqConstant.STATUE_CHECK);
        StringBuffer sql = new StringBuffer();
        sql.append(" select pr.id AS id,pr.corp_id AS corpId,pr.bank_info_id AS bankInfoId,pr.bank_name AS bankName,");
        sql.append(" pr.product_code AS productCode,pr.product_type AS productType,pr.bank_type_id AS bankTypeId,");
        sql.append(" pr.income_clause AS incomeClause,pr.bank_acc AS bankAcc,pr.buy_money AS buyMoney,pr.cur_id AS curId,");
        sql.append(" pr.interest_start_date AS interestStartDate,pr.deadline_date AS deadlineDate,pr.refer_income_rate AS referIncomeRate,");
        sql.append(" pr.is_move_up AS isMoveUp,pr.status AS status,pr.pay_way AS payWay,pr.contract_no AS contractNo,");
        sql.append(" pr.income_type as incomeType, pr.is_store as isStore, pr.start_contract_rate as startContractRate, pr.end_contract_rate as endContractRate,");
        sql.append(" pr.is_maintain_val as isMaintainVal, pr.end_refer_income_money as endReferIncomeMoney, pr.end_refer_income_rate as endReferIncomeRate, ");
        sql.append(" pr.refer_income_money as referIncomeMoney, pr.move_up_rmk as moveUpRmk,pr.rmk as rmk,  pr.charge_money as chargeMoney, ");
        sql.append(" pr.sub_fee_money as subFeeMoney, pr.attachment_id as attachmentId, pr.is_guaranteed as isGuaranteed,pr.capital_source as capitalSource,");
        sql.append(" pr.daily_net_val as dailyNetVal, pr.portion as portion,pr.buy_date as buyDate  ,pr.buy_apply_status as buyApplyStatus  ");
        sql.append(" from cho_product_req pr where 1 = 1");
        sql.append(" and pr.corp_id =:corpId and pr.deadline_date > :deadlineDate and pr.status =:status");
        return this.listBySQLAliasToBean(sql.toString(),params, ChoProductReqDTO.class);
    }

    public List<String> getCorpIds(){
        StringBuilder sql = new StringBuilder();
        Map<String,Object> params = new HashMap<>();
        sql.append(" SELECT DISTINCT pr.CORP_ID AS corpId ");
        sql.append(" FROM CHO_PRODUCT_REQ pr ");
        return createSQLQuery(sql.toString(), params).list();
    }

    public List<BankTypeDTO> getBankTypeJson(){
        StringBuilder sql = new StringBuilder();
        Map<String,Object> params = new HashMap<>();
        sql.append(" SELECT DISTINCT bbt.ID AS id, bbt.TYPE_NAME AS typeName ");
        sql.append(" FROM CHO_PRODUCT_REQ cpr LEFT JOIN BT_BANK_TYPE bbt ON cpr.BANK_TYPE_ID = bbt.ID ");
        return this.listBySQLAliasToBean(sql.toString(),params, BankTypeDTO.class);
    }

    public List<ChoProductReqVo> getBankInfoJson(String bankTypeIdsSql){
        StringBuilder sql = new StringBuilder();
        Map<String,Object> params = new HashMap<>();
        sql.append(" SELECT DISTINCT cpr.BANK_INFO_ID AS bankInfoId, cpr.BANK_NAME AS bankName ");
        sql.append(" FROM CHO_PRODUCT_REQ cpr WHERE 1=1 and cpr.BANK_NAME is not NULL AND cpr.BANK_INFO_ID IS NOT NULL");
        if (StringUtils.isNotEmpty(bankTypeIdsSql)) {
            sql.append(" AND ").append(bankTypeIdsSql);
        }
        return this.listBySQLAliasToBean(sql.toString(),params, ChoProductReqVo.class);
    }
}
