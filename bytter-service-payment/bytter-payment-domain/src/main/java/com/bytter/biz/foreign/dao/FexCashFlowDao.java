package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexCashFlow;
import com.bytter.biz.foreign.vo.FexCashFlowVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.biz.payment.api.constant.BdlinkConstant;
import com.bytter.biz.payment.api.constant.BtBankAccConstant;
import com.bytter.biz.payment.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.constant.NisBusTypeConstant;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: Dao
 * @Description: 结算现金流表
 * @author huangx
 * @date 2020-02-24 15:20:33
 * @version 1.0
 *
 */
@Repository
public class FexCashFlowDao extends BaseDao<FexCashFlow,String> {
    /**
     * 交易管理表通用查@param sqlWhere
     *
     * @return
     */
    private String getCommonSql() {
        StringBuilder sql=new StringBuilder();
        sql.append("select t.id as id,t.bill_No as billNo,t.tran_Bill_Id as tranBillId,r.bill_No as tranBillNo,r.tran_date as tranDate,t.tran_Type as tranType,t.cash_Flow_Date as cashFlowDate,t.settle_Type as settleType,");
        sql.append(" t.corp_Id as corpId,t.corp_Acc_Id as corpAccId, t.corp_Bank as corpBank, t.corp_Bank_Acc as corpBankAcc, t.corp_Acc_Name as corpAccName,t.cash_Flow_Type as cashFlowType, ");
        sql.append(" t.cotp_Id as cotpId,t.cotp_Bank as cotpBank, t.cotp_Acc_Id as cotpAccId,t.cotp_Bank_Acc as cotpBankAcc, t.cotp_Acc_Name as cotpAccName, ");
        sql.append(" t.cur_Id as curId, t.cd_Sign as cdSign,t.pay_Money as payMoney,t.settle_Date as settleDate, t.facts_Money as factsMoney,t.facts_Date as factsDate,t.settle_No as settleNo,t.rmk as rmk,");
        sql.append(" t.attachment_Id as attachmentId,t.voucher_Type_Id as voucherTypeId, v.voucher_Name as voucherTypeName,t.status as status,t.LANDING_LIMIT_RESULT as landingLimitResult, t.pay_Status as payStatus, r.remit_Type as remitType,");
        sql.append(" r.bood_Type as boodType,r.bood_Acc as boodAcc,r.bood_Ratio as boodRatio,r.bood_Bank_Note as boodBankNote,r.bond_Cur_Id as bondCurId,r.is_Quick as isQuick,");
        sql.append(" r.pro_Id as proId,p.pro_Name as proName,r.contract_Rate as contractRate,g.bill_No as tranGroupBillNo,g.group_Name as tranGroupName,r.acc_Nature as accNature,");
        //针对资金调拨交易的现金流做特殊处理
        sql.append(" t.cotp_id as allocatCotpId,t.cotp_bank as allocatCotpBank,t.dif_Bank as difBank,t.payer_cur_Id as payerCurId,t.payee_cur_Id as payeeCurId,");
        //资金收付交易增加会计凭证号
        sql.append(" r.bursar_Bill_No as bursarBillNo,r.is_Payed as isPayed,t.bill_From as billFrom,t.rel_Bill_No as relBillNo,");
        sql.append(" t.create_By as createBy, t.create_Date as createDate, t.update_By as updateBy, t.update_Date as updateDate,exc.voucher_stat as voucherStat, ");
        sql.append(" t.cash_item as cashItem,i.item_name as cashItemName,t.bank_flow_sign as bankFlowSign,t.blend_sign as blendSign");
        sql.append(" from fex_cash_flow t left join fex_tran_req r on t.tran_bill_id=r.id");
        sql.append(" left join fc_voucher_type v on v.id=t.voucher_Type_Id");
        sql.append(" left join fex_product p on r.pro_Id=p.id");
        sql.append(" left join fex_tran_group g on g.id=r.tran_Group");
        sql.append(" left join bis_foreign_exc exc on exc.cbs_bill_id=t.id");
        sql.append(" left join fc_cash_item i on t.cash_Item=i.id");
        sql.append(" where 1=1");
        return sql.toString();
    }

    /**
     * 交易管理列表查询
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql=new StringBuilder(this.getCommonSql());
        if(StringUtils.isNotEmpty(joinSql)){
            sql.append(joinSql);
        }
        sql.append(" and t.status<>"+ CommonConstant.STATUE_DELETE);
        //申请日期
        String gedCreateDate = (String) dataTablesInput.getFormdata().get("ged_createDate");
        String ledCreateDate = (String) dataTablesInput.getFormdata().get("led_createDate");
        if(StringUtils.isNotEmpty(gedCreateDate)){
            gedCreateDate = gedCreateDate + " 00:00:00";
            dataTablesInput.getFormdata().put("ged_createDate", gedCreateDate);
        }
        if(StringUtils.isNotEmpty(ledCreateDate)){
            ledCreateDate = ledCreateDate + " 23:59:59";
            dataTablesInput.getFormdata().put("led_createDate", ledCreateDate);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), FexCashFlowVO.class, null, false);
    }

    /**
     * 根据ID获取交易信息
     * @param id
     * @return
     */
    public FexCashFlowVO getByIdUnion(String id){
        StringBuilder sql=new StringBuilder(this.getCommonSql());
        sql.append(" and t.id=:id");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, FexCashFlowVO.class);
    }

    /**
     * 根据billNo获取交易信息
     * @param billNo
     * @return
     */
    public FexCashFlowVO getByBillNoUnion(String billNo){
        StringBuilder sql=new StringBuilder(this.getCommonSql());
        sql.append(" and t.bill_No=:billNo");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("billNo", billNo);
        return getBySQLAliasToBean(sql.toString(), params, FexCashFlowVO.class);
    }

    /**
     * 根据交易获取交易信息
     * @param tranBillId
     * @return
     */
    public List<FexCashFlowVO> listByTranBillIdUnion(String tranBillId){
        StringBuilder sql=new StringBuilder(this.getCommonSql());
        sql.append(" and t.tran_Bill_Id=:tranBillId and t.status<>"+CommonConstant.STATUE_DELETE);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("tranBillId", tranBillId);
        sql.append(" order by t.create_date");
        return listBySQLAliasToBean(sql.toString(), params, FexCashFlowVO.class);
    }

    /**
     * 根据交易获取交易信息
     * @param settleNo
     * @return
     */
    public List<FexCashFlowVO> listBySettleNoUnion(String settleNo){
        StringBuilder sql=new StringBuilder(this.getCommonSql());
        sql.append(" and t.settle_No=:settleNo and t.settle_Type=:settleType");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("settleNo", settleNo);
        params.put("settleType", ForeignConstant.SETTLE_TYPE_1);
        sql.append(" order by t.create_date");
        return listBySQLAliasToBean(sql.toString(), params, FexCashFlowVO.class);
    }

    /**
     * 获取审批信息
     * @param dataTablesInput
     * @param businessSql
     * @return
     */
    public DataTablesOutput<FexCashFlowVO> dataTableApprove(DataTablesInput dataTablesInput, String businessSql) {
        StringBuilder sql=new StringBuilder(this.getCommonSql());
        sql.append(" and " + businessSql);
        String flowStatus = dataTablesInput.getParameter().get("flowStatus");
        //已审批不过滤状态
        if(!IWorkFlowService.PASS.equals(flowStatus)){
            sql.append(" and  t.status<> ").append(NetbankConstant.STATUE_CHECK);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), FexCashFlowVO.class, "t", false);
    }

    /**
     * 交易管理列表查询
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesReport(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql=new StringBuilder(this.getCommonSql());
        if(StringUtils.isNotEmpty(joinSql)){
            sql.append(joinSql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), FexCashFlowVO.class, "t", false);
    }

    /**
     * 根据交易类型获取现金流
     * @param tranBillId
     * @param cdSign
     * @return
     */
    public List<FexCashFlowVO> listByTranBillId(String tranBillId, String cdSign){
        StringBuilder sql=new StringBuilder(this.getCommonSql());
        sql.append(" and t.tran_Bill_Id=:tranBillId");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("tranBillId", tranBillId);
        if(StringUtils.isNotBlank(cdSign)) {
            sql.append(" and t.cd_Sign=:cdSign");
            params.put("cdSign", cdSign);
        }
        sql.append(" and t.status<>"+CommonConstant.STATUE_DELETE);
        sql.append(" order by t.create_date");
        return listBySQLAliasToBean(sql.toString(), params, FexCashFlowVO.class);
    }

    /**
     * 根据单位统计现金流
     * @param corpId
     * @return
     */
    public int getCashFlowCountByCorpId(String corpId){
        String sql="select count(*) from fex_cash_flow where corp_id=:corpId";
        HashMap<String,Object> params=new HashMap<>();
        params.put("corpId",corpId);
        //0-暂存、11-待审批、-1-审批打回
        sql+=" and status in ('0','11','-1')";
        return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
    }

    /**
     * 根据银行流水获取现金流
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnionForRefundCheck(DataTablesInput dataTablesInput, String joinSql) {
        DataTablesOutput output = null;
        StringBuilder sql=new StringBuilder();
        sql.append("select t.id as id,t.bill_No as billNo,t.tran_Bill_Id as tranBillId,r.bill_No as tranBillNo,r.tran_date as tranDate,t.tran_Type as tranType,t.cash_Flow_Date as cashFlowDate,t.settle_Type as settleType,");
        sql.append(" t.corp_Id as corpId,t.corp_Acc_Id as corpAccId, t.corp_Bank as corpBank, t.corp_Bank_Acc as corpBankAcc, t.corp_Acc_Name as corpAccName,t.cash_Flow_Type as cashFlowType, ");
        sql.append(" t.cotp_Id as cotpId,t.cotp_Bank as cotpBank,t.cotp_Acc_Id as cotpAccId,t.cotp_Bank_Acc as cotpBankAcc, t.cotp_Acc_Name as cotpAccName, ");
        sql.append(" t.cur_Id as curId, t.cd_Sign as cdSign,t.pay_Money as payMoney,t.settle_Date as settleDate, t.facts_Money as factsMoney,t.facts_Date as factsDate,t.settle_No as settleNo,t.rmk as rmk,");
        sql.append(" t.attachment_Id as attachmentId,t.voucher_Type_Id as voucherTypeId, v.voucher_Name as voucherTypeName,t.status as status, t.pay_Status as payStatus, r.remit_Type as remitType,");
        sql.append(" r.bood_Type as boodType,r.bood_Acc as boodAcc,r.bood_Ratio as boodRatio,r.bood_Bank_Note as boodBankNote,r.bond_Cur_Id as bondCurId,r.is_Quick as isQuick,");
        sql.append(" r.pro_Id as proId,p.pro_Name as proName,r.contract_Rate as contractRate,g.bill_No as tranGroupBillNo,g.group_Name as tranGroupName,r.acc_Nature as accNature,");
        //针对资金调拨交易的现金流做特殊处理
        sql.append(" t.cotp_id as allocatCotpId,t.cotp_bank as allocatCotpBank,t.dif_Bank as difBank,t.payer_cur_Id as payerCurId,t.payee_cur_Id as payeeCurId,");
        //资金收付交易增加会计凭证号
        sql.append(" r.bursar_Bill_No as bursarBillNo,r.is_Payed as isPayed,");
        sql.append(" t.create_By as createBy, t.create_Date as createDate, t.update_By as updateBy, t.update_Date as updateDate,exc.voucher_stat as voucherStat ");
        sql.append(" from fex_cash_flow t left join fex_tran_req r on t.tran_bill_id=r.id");
        sql.append(" left join fc_voucher_type v on v.id=t.voucher_Type_Id");
        sql.append(" left join fex_product p on r.pro_Id=p.id");
        sql.append(" left join fex_tran_group g on g.id=r.tran_Group");
        sql.append(" left join bis_foreign_exc exc on exc.cbs_bill_id=t.id");
        sql.append(" left join bt_bank_acc acc on acc.bank_acc=t.corp_bank_acc");
        // 付款现金流
        sql.append(" where t.cd_sign = '" + NisBusTypeConstant.CD_SIGN_PAYER + "'");
        // 资金收付交易 和 资金调拨交易
        sql.append(" and t.tran_Type in ('" + ForeignConstant.TRAN_TYPE_3 + "','" + ForeignConstant.TRAN_TYPE_4 + "')");
        if(StringUtils.isNotEmpty(joinSql)){
            sql.append(joinSql);
        }
        String opType = (String) dataTablesInput.getFormdata().get("opType");
        if (!"2".equals(opType)) {
            // 过滤掉已退款状态的现金流
            sql.append(" and t.pay_status not in ('" + ForeignConstant.PAY_STATUS_REFUNDED + "')");
            String blendSign = (String) dataTablesInput.getFormdata().get("blendSign");
            String type = (String) dataTablesInput.getFormdata().get("type");
            String bankSerialNo = (String) dataTablesInput.getFormdata().get("eqs_bankSerialNo");
            if (!"#".equals(bankSerialNo)) {
                String gedSettleDate = (String) dataTablesInput.getFormdata().get("ged_settleDate");
                String ledSettleDate = (String) dataTablesInput.getFormdata().get("led_settleDate");
                if (StringUtils.isNotEmpty(gedSettleDate)) {
                    gedSettleDate = gedSettleDate + " 00:00:00";
                    dataTablesInput.getFormdata().put("ged_settleDate", gedSettleDate);
                }
                if (StringUtils.isNotEmpty(ledSettleDate)) {
                    ledSettleDate = ledSettleDate + " 23:59:59";
                    dataTablesInput.getFormdata().put("led_settleDate", ledSettleDate);
                }
                if (StringUtils.isBlank(blendSign) || BtBankAccConstant.BLENDSIGN_0.equals(blendSign)) {
                    if (EmptyUtils.isNotEmpty(type) && "auto".equals(type)) {
                        sql.append(" and (t.bank_serial_no is null or t.bank_serial_no = '') ");
                    }
                    sql.append(" and t.status = " + CommonConstant.STATUE_CHECK);
                    sql.append(" and (exc.voucher_stat = '" + BdlinkConstant.SUCCESS + "' or (acc.is_online='" + CommonConstant.NO + "' and t.pay_status='"+CommonConstant.YES+"'))");
                } else {
                    sql.append(" and t.bank_serial_no = '" + bankSerialNo + "' ");
                }
            }
        }
        output = getDynamicBySQL(dataTablesInput, sql.toString(), FexCashFlowVO.class, "t", false);
        if(output == null) {
            output = new DataTablesOutput();
        }
        return output;
    }

    /**
     * 更新银行流水号
     * @param id
     * @param bankSerialNo
     * @return
     */
    public int updateBankSerialNo(String id, String bankSerialNo, String payStatus){
        StringBuilder sql = new StringBuilder();
        sql.append("update fex_cash_flow set bank_Serial_No = :bankSerialNo");
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotBlank(payStatus)) {
            sql.append(",pay_Status = :payStatus");
            params.put("payStatus",payStatus);
        }
        sql.append(" where id = :id");
        params.put("id", id);
        params.put("bankSerialNo",bankSerialNo);
        return this.batchSQLExecute(sql.toString(), params);
    }

    /**
     * 带原状态更新现金流
     * @param vo
     * @param oldStatus
     * @return
     */
    public int updateFexCashFlowStatus(FexCashFlowVO vo, Integer[] oldStatus) {
        StringBuilder hql=new StringBuilder();
        hql.append(" update FexCashFlow set tranBillId=:tranBillId,tranType=:tranType,cashFlowDate=:cashFlowDate,settleType=:settleType,corpId=:corpId,");
        hql.append(" cashFlowType=:cashFlowType,corpAccId=:corpAccId,corpBank=:corpBank,corpBankAcc=:corpBankAcc,corpAccName=:corpAccName,");
        hql.append(" cotpId=:cotpId,cotpBank=:cotpBank,cotpAccId=:cotpAccId,cotpBankAcc=:cotpBankAcc,cotpAccName=:cotpAccName,");
        hql.append(" curId=:curId,payMoney=:payMoney,cdSign=:cdSign,settleDate=:settleDate,voucherTypeId=:voucherTypeId,factsMoney=:factsMoney,");
        hql.append(" factsDate=:factsDate,settleNo=:settleNo,rmk=:rmk,attachmentId=:attachmentId,status=:status,payStatus=:payStatus,");
        hql.append(" difBank=:difBank,bankFlowSign=:bankFlowSign,oldStatus=:oldStatus,payerCurId=:payerCurId,payeeCurId=:payeeCurId, ");
        hql.append(" cashItem=:cashItem,bankSerialNo=:bankSerialNo,billFrom=:billFrom,relBillNo=:relBillNo");

        Map<String,Object> params = new HashMap<>();
        params.put("tranBillId", vo.getTranBillId());
        params.put("tranType", vo.getTranType());
        params.put("cashFlowDate", vo.getCashFlowDate());
        params.put("settleType", vo.getSettleType());
        params.put("corpId", vo.getCorpId());
        params.put("cashFlowType", vo.getCashFlowType());
        params.put("corpAccId", vo.getCorpAccId());
        params.put("corpBank", vo.getCorpBank());
        params.put("corpBankAcc", vo.getCorpBankAcc());
        params.put("corpAccName", vo.getCorpAccName());
        params.put("cotpId", vo.getCotpId());
        params.put("cotpBank", vo.getCotpBank());
        params.put("cotpAccId", vo.getCotpAccId());
        params.put("cotpBankAcc", vo.getCotpBankAcc());
        params.put("cotpAccName", vo.getCotpAccName());
        params.put("curId", vo.getCurId());
        params.put("payMoney", vo.getPayMoney());
        params.put("cdSign", vo.getCdSign());
        params.put("settleDate", vo.getSettleDate());
        params.put("voucherTypeId", vo.getVoucherTypeId());
        params.put("factsMoney", vo.getFactsMoney());
        params.put("factsDate", vo.getFactsDate());
        params.put("settleNo", vo.getSettleNo());
        params.put("rmk", vo.getRmk());
        params.put("attachmentId", vo.getAttachmentId());
        params.put("status", vo.getStatus());
        params.put("payStatus", vo.getPayStatus());
        params.put("difBank", vo.getDifBank());
        params.put("bankFlowSign", vo.getBankFlowSign());
        params.put("oldStatus", vo.getOldStatus());
        params.put("payerCurId", vo.getPayerCurId());
        params.put("payeeCurId", vo.getPayeeCurId());
        params.put("cashItem", vo.getCashItem());
        params.put("bankSerialNo", vo.getBankSerialNo());
        params.put("relBillNo", vo.getRelBillNo());
        params.put("billFrom", vo.getBillFrom());
        if (StringUtils.isNotEmpty(vo.getCreateBy())) {
            hql.append(",createBy=:createBy ");
            params.put("createBy", vo.getCreateBy());
        }
        if (vo.getCreateDate() != null) {
            hql.append(",createDate=:createDate ");
            params.put("createDate", vo.getCreateDate());
        }
        if (StringUtils.isNotEmpty(vo.getUpdateBy())) {
            hql.append(",updateBy=:updateBy ");
            params.put("updateBy", vo.getUpdateBy());
        }
        if (vo.getUpdateDate() != null) {
            hql.append(",updateDate=:updateDate ");
            params.put("updateDate", vo.getUpdateDate());
        }
        hql.append(" where id=:id ");
        List<Integer> list = Arrays.asList(oldStatus);
        hql.append("and "+getSQLIn(list,900,"status"));
        params.put("id", vo.getId());
        return this.batchExecute(hql.toString(), params);
    }

    public int updateVoucherState(String id, String voucherStat) {
        String sb = " update FEX_CASH_FLOW set voucher_stat = :voucherStat " +
                " where  id = :id";
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("voucherStat", voucherStat);
        return createSQLQuery(sb, param).executeUpdate();
    }

    /**
     * 更新资金收付明细银行落地限额处理结果。
     */
    public void updateLandingLimitResult(String id, String landingLimitResult) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("landingLimitResult", landingLimitResult);
        batchSQLExecute("update FEX_CASH_FLOW set LANDING_LIMIT_RESULT=:landingLimitResult where ID=:id", params);
    }

    /**
     * 翻转资金收付明细银行落地限额处理结果（互斥：仅当落地标识仍为 0-待执行才更新，0/A/R 终态不可再翻）。
     *
     * @return 更新行数；0 = 落地标识已非 0（已被并发回调翻转收尾/未处于落地待执行），调用方应中止收尾
     */
    public int updateLandingLimitResultIfPending(String id, String landingLimitResult) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("landingLimitResult", landingLimitResult);
        return batchSQLExecute("update FEX_CASH_FLOW set LANDING_LIMIT_RESULT=:landingLimitResult "
                + "where ID=:id and LANDING_LIMIT_RESULT='0'", params);
    }
}
