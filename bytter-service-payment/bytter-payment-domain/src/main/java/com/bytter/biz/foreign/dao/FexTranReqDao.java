package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexTranReq;
import com.bytter.biz.foreign.vo.FexTranReqVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.biz.payment.api.constant.NetbankConstant;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: Dao
 * @Description: 交易管理表
 * @author huangx
 * @date 2020-02-24 15:20:33
 * @version 1.0
 *
 */
@Repository
public class FexTranReqDao extends BaseDao<FexTranReq,String> {
    /**
     * 交易管理表通用查@param sqlWhere
     * 用于查看单笔数据
     * @return
     */
    private String getCommonSql() {
        StringBuilder sql=new StringBuilder();
        sql.append("select t.id as id,t.bill_No as billNo,t.tran_App_Dtl_Id as tranAppDtlId,a.bill_No as tranAppDtlNo,t.tran_Type as tranType,t.is_Swap_Tran as isSwapTran,t.pro_Id as proId,p.pro_Name as proName,");
        sql.append(" t.template_id as templateId,t.template_name as templateName,t.version_no as versionNo, t.bank_pay_type_id as bankPayTypeId, t.bank_pay_type_name as bankPayTypeName,");
        sql.append(" t.fex_Bus_Type as fexBusType,t.fex_Tran_Type as fexTranType,t.delivery_Type as deliveryType,t.delivery_Cur_Id as deliveryCurId,t.buy_Cur_Id as buyCurId,t.sell_Cur_Id as sellCurId,");
        sql.append(" t.buy_Money as buyMoney,t.sell_Money as sellMoney,t.contract_Rate as contractRate,t.spot_Rate as spotRate,t.bank_Note as bankNote,t.tran_Date as tranDate,t.settle_Date as settleDate,t.delivery_Deadline as deliveryDeadline,");
        sql.append(" t.bood_Type as boodType,t.bood_Acc_Id as boodAccId,t.bood_Acc as boodAcc,t.bood_Ratio as boodRatio,t.bood_Bank_Note as boodBankNote,t.bond_Cur_Id as bondCurId,t.contract_No as contractNo,");
        sql.append(" t.forward_Points as forwardPoints, t.bus_No as busNo, t.purpose as purpose, t.purpose_Dtl as purposeDtl, t.funds_Source as fundsSource, t.invoice_No as invoiceNo, t.acc_Nature as accNature, ");
        sql.append(" t.fex_Prop as fexProp, t.bood_Rmk as boodRmk, t.rmk as rmk, t.rmk_Local as rmkLocal, t.attachment_Id as attachmentId, t.cotp_Id as cotpId, t.cotp_Acc_Id as cotpAccId,t.cotp_Bank as cotpBank, t.cotp_Bank_Acc as cotpBankAcc, t.cotp_Acc_Name as cotpAccName, t.cur_Id as curId, t.cd_Sign as cdSign,");
        sql.append(" t.pay_Corp_Id as payCorpId, t.pay_Money as payMoney, t.pay_Money_Convert as payMoneyConvert, t.is_Quick as isQuick, t.corp_Acc_Id as corpAccId, t.corp_Bank as corpBank, t.corp_Bank_Acc as corpBankAcc,t.corp_acc_cur_id as corpAccCurId, t.corp_Acc_Name as corpAccName, t.remit_Method as remitMethod,");
        sql.append(" t.corp_Name_En as corpNameEn, t.corp_Addr_En as corpAddrEn, t.corp_Contact as corpContact, t.corp_Contact_Tel as corpContactTel,t.cost_Sign as costSign,t.cost_Acc_Id as costAccId, t.cost_Acc_Id2 as costAccId2, ");
        sql.append(" t.cost_Acc as costAcc, t.cost_Acc2 as costAcc2,t.pay_Type as payType, t.ship_Date as shipDate, t.is_Bonded as isBonded, t.tran_Code1 as tranCode1, t.tran_Amt1 as tranAmt1, t.tran_Cur_Id1 as tranCurId1,t.cotp_Acc_Cur_Id as cotpAccCurId, ");
        sql.append(" t.tran_Add_Word1 as tranAddWord1, t.tran_Code2 as tranCode2, t.tran_Amt2 as tranAmt2, t.tran_Cur_Id2 as tranCurId2, t.tran_Add_Word2 as tranAddWord2, t.remit_Type as remitType,t.remit_Nature as remitNature,t.status as status, t.corp_Id as corpId, ");
        sql.append(" t.capital_Use as capitalUse, t.src_Bank as srcBank, t.src_Bank_Acc as srcBankAcc, t.src_Acc_Id as srcAccId,t.src_Acc_Name as srcAccName, t.target_Bank as targetBank, t.target_Acc_Id as targetAccId,t.target_Bank_Acc as targetBankAcc, t.target_Acc_Name as targetAccName, t.target_Corp as targetCorp, t.agent_Bank as agentBank, ");
        sql.append(" t.agent_Bank_Acc as agentBankAcc, t.country_Id as countryId, case when t.tran_Type = '3' THEN c.name_zh ELSE c.name_en END as nameZh, t.cotp_No as cotpNo, t.tran_Addr as tranAddr,t.tran_Group as tranGroup,g.group_Name as tranGroupName,t.tran_Attr as tranAttr,t.swap_Type as swapType,t.partner_Id as partnerId, ");
        sql.append(" t.fixed_Money as fixedMoney,");
        sql.append(" t.create_By as createBy, t.create_Date as createDate, t.update_By as updateBy, t.update_Date as updateDate,t.Settle_bus_type as settleBusType,t.auto_Cal as autoCal, ");
        sql.append(" t.volatility as volatility,t.bus_Direction as busDirection,t.updown_Direction as updownDirection,t.expires_Date as expiresDate,t.avg_Begin_Date as avgBeginDate,t.avg_End_Date as avgEndDate,");
        sql.append(" t.avg_Days as avgDays,t.valid_Standard as validStandard,t.except_Low_Val as exceptLowVal,t.except_Hign_Val as exceptHignVal,t.tran_Settle_Method as tranSettleMethod,t.tran_Addr_Id as tranAddrId,");
        sql.append(" t.premium_Date as premiumDate,t.premium_Cur as premiumCur,t.premium_Point as premiumPoint,t.premium_Ratio as premiumRatio,t.premium_Rate as premiumRate,t.premium_Money as premiumMoney,t.is_Refund as isRefund,");
        sql.append(" t.refund_Point as refundPoint,t.refund_Ratio as refundRatio,t.refund_Money as refundMoney,t.refund_Time as refundTime,t.cotp_Agent as cotpAgent,t.cotp_Clear_House as cotpClearHouse,");
        sql.append(" p.fex_Option1 as fexOption1,p.fex_Option2 as fexOption2,p.fex_Option3 as fexOption3,p.fex_Option_Type as fexOptionType,");
        sql.append(" p.is_Show_Agent as isShowAgent,p.is_Show_Clear_House as isShowClearHouse,targetAcc.is_domestic_bank as isDomesticBank,targetAcc.bank_code as targetBankCode,");
        sql.append(" t.target_Acc_Cur_Id as targetAccCurId,t.dif_Bank as difBank,t.is_Payed as isPayed,t.bursar_bill_no as bursarBillNo,");
        sql.append(" t.acc_type as accType,t.payee_Type as payeeType,t.remit_Route as remitRoute,t.remit_Use as remitUse,t.rel_bill_no as relBillNo,");
        sql.append(" t.branch_Code as branchCode,t.branch_Name as branchName,t.arrival_Date as arrivalDate,t.declare_No as declareNo,t.add_Word as addWord,");
        sql.append(" t.cost_Cur_Id as costCurId,t.cost_Money as costMoney,t.bank_Pay_Type as bankPayType,t.split_id as splitId,t.merge_id as mergeId,t.orig_bill_no as origBillNo,t.origin_Country as originCountry, originCountry.name_zh as originCountryName, ");
        sql.append(" t.cotp_Country as cotpCountry,t.cotp_Address as cotpAddress,t.cotp_Prov as cotpProv,t.cotp_City as cotpCity,t.bill_From as billFrom,");
        sql.append(" case when t.tran_Type = '3' THEN cotpCountry.name_zh ELSE cotpCountry.name_en END as cotpCountryName,cotpAddress.name_zh as cotpAddressName,t.time_Stamp as timeStamp,");
        sql.append(" t.goods_Amount as goodsAmount,t.advance_Charge_Amount as advanceChargeAmount,t.advance_Contract_Rate as advanceContractRate,t.rpt_Gate_Date as rptGateDate,t.rpt_Gate_Ent_Id as rptGateEntId,t.rpt_Gate_Type as rptGateType,");
        sql.append(" t.general_Trade_Amount as generalTradeAmount,t.feed_Amount as feedAmount,t.other_Trade_Amount as otherTradeAmount,t.border_Trade_Amount as borderTradeAmount,t.custom_Amount as customAmount,t.offshore_Amount as offshoreAmount,");
        sql.append(" t.other_Amount as otherAmount,t.invest_Amt as investAmt,t.invest_No as investNo,t.invest_Apply_Nbr as investApplyNbr,t.often_Amt as oftenAmt,t.often_No as oftenNo,");
        sql.append(" t.direct_Investment_Cert_No as directInvestmentCertNo,t.advance_Rate as advanceRate,t.check_Day as checkDay");
        sql.append(" from fex_tran_req t left join fex_product p on t.pro_id=p.id");
        sql.append(" left join fex_tran_template_config tc on t.template_id=tc.Id");
        sql.append(" left join sys_country_cust c on t.country_Id=c.id");
        //交易对手国家地区
        sql.append(" left join sys_country_cust cotpCountry on cotpCountry.id=t.cotp_Country");
        //交易对手地点
        sql.append(" left join sys_country_cust cotpAddress on cotpAddress.id=t.cotp_Address");
        //交易对手城市
//        sql.append(" left join sys_region cotpCity on cotpCity.id=t.cotp_City");
        //交易组
        sql.append(" left join fex_tran_group g on g.id=t.tran_Group");
        //交易需求明细
        sql.append(" left join fex_tran_app_dtl dtl on dtl.id=t.tran_App_Dtl_Id");
        //交易需求主
        sql.append(" left join fex_tran_app a on a.id=dtl.app_id");
        //目标账户
        sql.append(" left join bt_bank_acc targetAcc on targetAcc.id=t.target_Acc_Id");
        //起运国
        sql.append(" left join sys_country_cust originCountry on originCountry.id=t.origin_Country");
        sql.append(" where 1=1 ");
        return sql.toString();
    }

    /**
     * 交易管理表通用查@param sqlWhere
     * 用于查看列表
     * @return
     */
    private String getCommonSqlJoin() {
        StringBuilder sql=new StringBuilder();
        sql.append("select t.id as id,t.bill_No as billNo,t.tran_App_Dtl_Id as tranAppDtlId,a.bill_No as tranAppDtlNo,t.tran_Type as tranType,t.is_Swap_Tran as isSwapTran,t.pro_Id as proId,p.pro_Name as proName,");
        sql.append(" t.template_id as templateId,t.template_name as templateName,t.version_no as versionNo, t.bank_pay_type_id as bankPayTypeId, t.bank_pay_type_name as bankPayTypeName,");
        sql.append(" t.fex_Bus_Type as fexBusType,t.fex_Tran_Type as fexTranType,t.delivery_Type as deliveryType,t.delivery_Cur_Id as deliveryCurId,t.buy_Cur_Id as buyCurId,t.sell_Cur_Id as sellCurId,");
        sql.append(" t.buy_Money as buyMoney,t.sell_Money as sellMoney,t.contract_Rate as contractRate,t.spot_Rate as spotRate,t.bank_Note as bankNote,t.tran_Date as tranDate,t.settle_Date as settleDate,t.delivery_Deadline as deliveryDeadline,");
        sql.append(" t.bood_Type as boodType,t.bood_Acc_Id as boodAccId,t.bood_Acc as boodAcc,t.bood_Ratio as boodRatio,t.bood_Bank_Note as boodBankNote,t.bond_Cur_Id as bondCurId,t.contract_No as contractNo,");
        sql.append(" t.forward_Points as forwardPoints, t.bus_No as busNo, t.purpose as purpose, t.purpose_Dtl as purposeDtl, t.funds_Source as fundsSource, t.invoice_No as invoiceNo, t.acc_Nature as accNature, ");
        sql.append(" t.fex_Prop as fexProp, t.bood_Rmk as boodRmk, t.rmk as rmk, t.rmk_Local as rmkLocal, t.attachment_Id as attachmentId, t.cotp_Id as cotpId, t.cotp_Acc_Id as cotpAccId,t.cotp_Bank as cotpBank, t.cotp_Bank_Acc as cotpBankAcc, t.cotp_Acc_Name as cotpAccName, t.cur_Id as curId, t.cd_Sign as cdSign,");
        sql.append(" t.pay_Corp_Id as payCorpId, t.pay_Money as payMoney, t.pay_Money_Convert as payMoneyConvert, t.is_Quick as isQuick, t.corp_Acc_Id as corpAccId, t.corp_Bank as corpBank, t.corp_Bank_Acc as corpBankAcc,t.corp_acc_cur_id as corpAccCurId, t.corp_Acc_Name as corpAccName, t.remit_Method as remitMethod,");
        sql.append(" t.corp_Name_En as corpNameEn, t.corp_Addr_En as corpAddrEn, t.corp_Addr as corpAddr, t.corp_Contact as corpContact, t.corp_Contact_Tel as corpContactTel,t.cost_Sign as costSign,t.cost_Acc_Id as costAccId, t.cost_Acc_Id2 as costAccId2, ");
        sql.append(" t.cost_Acc as costAcc, t.cost_Acc2 as costAcc2,t.pay_Type as payType, t.ship_Date as shipDate, t.is_Bonded as isBonded, t.tran_Code1 as tranCode1, t.tran_Amt1 as tranAmt1, t.tran_Cur_Id1 as tranCurId1,t.cotp_Acc_Cur_Id as cotpAccCurId, ");
        sql.append(" t.tran_Add_Word1 as tranAddWord1, t.tran_Code2 as tranCode2, t.tran_Amt2 as tranAmt2, t.tran_Cur_Id2 as tranCurId2, t.tran_Add_Word2 as tranAddWord2, t.remit_Type as remitType,t.remit_Nature as remitNature,t.status as status, t.corp_Id as corpId, ");
        sql.append(" t.capital_Use as capitalUse, t.src_Bank as srcBank, t.src_Bank_Acc as srcBankAcc, t.src_Acc_Id as srcAccId,t.src_Acc_Name as srcAccName, t.target_Bank as targetBank, t.target_Acc_Id as targetAccId,t.target_Bank_Acc as targetBankAcc, t.target_Acc_Name as targetAccName, t.target_Corp as targetCorp, t.agent_Bank as agentBank, ");
        sql.append(" t.agent_Bank_Acc as agentBankAcc, t.country_Id as countryId, case when t.tran_Type = '3' THEN c.name_zh ELSE c.name_en END as nameZh, t.cotp_No as cotpNo, t.tran_Addr as tranAddr,t.tran_Group as tranGroup,g.group_Name as tranGroupName,t.tran_Attr as tranAttr,t.swap_Type as swapType,t.partner_Id as partnerId, ");
        sql.append(" t.fixed_Money as fixedMoney,");
        sql.append(" t.create_By as createBy, t.create_Date as createDate, t.update_By as updateBy, t.update_Date as updateDate,t.Settle_bus_type as settleBusType,t.auto_Cal as autoCal, ");
        sql.append(" t.volatility as volatility,t.bus_Direction as busDirection,t.updown_Direction as updownDirection,t.expires_Date as expiresDate,t.avg_Begin_Date as avgBeginDate,t.avg_End_Date as avgEndDate,");
        sql.append(" t.avg_Days as avgDays,t.valid_Standard as validStandard,t.except_val as exceptVal,t.except_Low_Val as exceptLowVal,t.except_Hign_Val as exceptHignVal,t.tran_Settle_Method as tranSettleMethod,t.tran_Addr_Id as tranAddrId,");
        sql.append(" t.premium_Date as premiumDate,t.premium_Cur as premiumCur,t.premium_Point as premiumPoint,t.premium_Ratio as premiumRatio,t.premium_Rate as premiumRate,t.premium_Money as premiumMoney,t.PREMIUM_MONEY_OF_PREMIUM_CUR as premiumMoneyOfPremiumCur,t.is_Refund as isRefund,");
        sql.append(" t.refund_Point as refundPoint,t.refund_Ratio as refundRatio,t.refund_Money as refundMoney,t.refund_Time as refundTime,t.cotp_Agent as cotpAgent,t.cotp_Clear_House as cotpClearHouse,");
        sql.append(" p.fex_Option1 as fexOption1,p.fex_Option2 as fexOption2,p.fex_Option3 as fexOption3,p.fex_Option_Type as fexOptionType,");
        sql.append(" p.is_Show_Agent as isShowAgent,p.is_Show_Clear_House as isShowClearHouse,targetAcc.is_domestic_bank as isDomesticBank,targetAcc.bank_code as targetBankCode,");
        sql.append(" t.target_Acc_Cur_Id as targetAccCurId,t.dif_Bank as difBank,t.is_Payed as isPayed,t.bursar_bill_no as bursarBillNo,");
        sql.append(" t.acc_type as accType,t.payee_Type as payeeType,t.remit_Route as remitRoute,t.remit_Use as remitUse,t.rel_bill_no as relBillNo,");
        sql.append(" t.branch_Code as branchCode,t.branch_Name as branchName,t.arrival_Date as arrivalDate,t.declare_No as declareNo,t.add_Word as addWord,");
        sql.append(" t.cost_Cur_Id as costCurId,t.cost_Money as costMoney,t.bank_Pay_Type as bankPayType,t.split_id as splitId,t.merge_id as mergeId,t.orig_bill_no as origBillNo,t.origin_Country as originCountry, originCountry.name_zh as originCountryName, ");
        sql.append(" t.cotp_Country as cotpCountry,t.cotp_Address as cotpAddress,t.cotp_Prov as cotpProv,t.cotp_City as cotpCity,t.bill_From as billFrom,");
        sql.append(" t.cash_Item as cashItem,i.item_name as cashItemName,t.foreign_Contract_No as foreignContractNo,t.save_fields as saveFields,t.invoice_date as invoiceDate, ");
        sql.append(" t.invoice_total as invoiceTotal, t.deduction_amount as deductionAmount, t.invoice_amount as invoiceAmount,t.regulatory_Type as regulatoryType, ");
        sql.append(" t.fps_id as fpsId, t.e_mail as eMail, t.isd_code as isdCode,t.phone_code as phoneCode,t.aiin as aiIn,t.hkid as hkId,t.bnf_Relate as bnfRelate, ");
        sql.append(" case when t.tran_Type = '3' THEN cotpCountry.name_zh ELSE cotpCountry.name_en END as cotpCountryName, cotpAddress.name_zh as cotpAddressName,cotpAddress.name_en as cotpAddressNameEn,t.time_Stamp as timeStamp");
        //扩展信息
        sql.append(" ,fx.TRAN_CUR_PAIR_ID as tranCurPairId,fx.position_money as positionMoney,fx.quotation_method as quotationMethod,fx.quotation_cur_id as quotationCurId,fx.quotation_refer_cur_id as quotationReferCurId ");
        sql.append(" ,fx.CONVERT_CUR_ID as convertCurId,fx.CONVERT_CUR_REFER_ID as convertCurReferId,fx.convert_rate as convertRate,fx.tran_Rate_Type as tranRateType,  ");
        sql.append(" t.goods_Amount as goodsAmount,t.advance_Charge_Amount as advanceChargeAmount,t.advance_Contract_Rate as advanceContractRate,t.rpt_Gate_Date as rptGateDate,t.rpt_Gate_Ent_Id as rptGateEntId,t.rpt_Gate_Type as rptGateType,");
        sql.append(" t.general_Trade_Amount as generalTradeAmount,t.feed_Amount as feedAmount,t.other_Trade_Amount as otherTradeAmount,t.border_Trade_Amount as borderTradeAmount,t.custom_Amount as customAmount,t.offshore_Amount as offshoreAmount,");
        sql.append(" t.other_Amount as otherAmount,t.invest_Amt as investAmt,t.invest_No as investNo,t.invest_Apply_Nbr as investApplyNbr,t.often_Amt as oftenAmt,t.often_No as oftenNo,");
        sql.append(" t.direct_Investment_Cert_No as directInvestmentCertNo,t.advance_Rate as advanceRate,t.check_Day as checkDay,");
        sql.append(" (case when f.blendSum=0 or f.blendSum is null then 0 when f.flowSum=f.blendSum then 1 when f.flowSum>f.blendSum then 2 else 0 end) as blendSign,");
        sql.append(" (select max(exc.voucher_stat) from bis_foreign_exc exc where exc.cbs_bill_id = t.id");
        sql.append(" or exists (select 1 from fex_cash_flow cash where cash.id = exc.cbs_bill_id and cash.tran_bill_id = t.id and cash.status <> -2)) as voucherStat");
        sql.append(" from fex_tran_req t left join fex_product p on t.pro_id=p.id");
        sql.append(" left join fex_tran_template_config tc on t.template_id=tc.Id");
        sql.append(" left join sys_country_cust c on t.country_Id=c.id");
        //交易对手国家地区
        sql.append(" left join sys_country_cust cotpCountry on cotpCountry.id=t.cotp_Country");
        //交易对手地点
        sql.append(" left join sys_country_cust cotpAddress on cotpAddress.id=t.cotp_Address");
        //交易对手城市
//        sql.append(" left join sys_region cotpCity on cotpCity.id=t.cotp_City");
        //交易组
        sql.append(" left join fex_tran_group g on g.id=t.tran_Group");
        //交易需求明细
        sql.append(" left join fex_tran_app_dtl dtl on dtl.id=t.tran_App_Dtl_Id");
        //交易需求主
        sql.append(" left join fex_tran_app a on a.id=dtl.app_id");
        //目标账户
        sql.append(" left join bt_bank_acc targetAcc on targetAcc.id=t.target_Acc_Id");
        //起运国
        sql.append(" left join sys_country_cust originCountry on originCountry.id=t.origin_Country");
        //现金流项目
        sql.append(" left join fc_cash_item i on t.cash_Item=i.id");
        //扩展信息
        sql.append(" left join fex_tran_req_ext fx on t.id=fx.req_id");
        //流水勾对状态
        sql.append(" left join (select cash.tran_bill_id,count(*) flowSum,sum(case when (cash.blend_sign='1' OR cash.bank_flow_sign='1') then 1 else 0 end) blendSum from fex_cash_flow cash where cash.bill_no is not null group by cash.tran_bill_id) f on f.tran_bill_id=t.id");
        sql.append(" where 1=1");
        return sql.toString();
    }

    private String getCommonSqlJoinForeign() {
        StringBuilder sql=new StringBuilder();
        sql.append("select t.id as id,t.bill_No as billNo,t.tran_App_Dtl_Id as tranAppDtlId,a.bill_No as tranAppDtlNo,t.tran_Type as tranType,t.is_Swap_Tran as isSwapTran,t.pro_Id as proId,p.pro_Name as proName,");
        sql.append(" t.template_id as templateId,t.template_name as templateName,t.version_no as versionNo, t.bank_pay_type_id as bankPayTypeId, t.bank_pay_type_name as bankPayTypeName,");
        sql.append(" t.fex_Bus_Type as fexBusType,t.fex_Tran_Type as fexTranType,t.delivery_Type as deliveryType,t.delivery_Cur_Id as deliveryCurId,t.buy_Cur_Id as buyCurId,t.sell_Cur_Id as sellCurId,");
        sql.append(" t.buy_Money as buyMoney,t.sell_Money as sellMoney,t.contract_Rate as contractRate,t.spot_Rate as spotRate,t.bank_Note as bankNote,t.tran_Date as tranDate,t.settle_Date as settleDate,t.delivery_Deadline as deliveryDeadline,");
        sql.append(" t.bood_Type as boodType,t.bood_Acc_Id as boodAccId,t.bood_Acc as boodAcc,t.bood_Ratio as boodRatio,t.bood_Bank_Note as boodBankNote,t.bond_Cur_Id as bondCurId,t.contract_No as contractNo,");
        sql.append(" t.forward_Points as forwardPoints, t.bus_No as busNo, t.purpose as purpose, t.purpose_Dtl as purposeDtl, t.funds_Source as fundsSource, t.invoice_No as invoiceNo, t.acc_Nature as accNature, ");
        sql.append(" t.fex_Prop as fexProp, t.bood_Rmk as boodRmk, t.rmk as rmk, t.rmk_Local as rmkLocal, t.attachment_Id as attachmentId, t.cotp_Id as cotpId, t.cotp_Acc_Id as cotpAccId,t.cotp_Bank as cotpBank, t.cotp_Bank_Acc as cotpBankAcc, t.cotp_Acc_Name as cotpAccName, t.cur_Id as curId, t.cd_Sign as cdSign,");
        sql.append(" t.pay_Corp_Id as payCorpId, t.pay_Money as payMoney, t.pay_Money_Convert as payMoneyConvert, t.is_Quick as isQuick, t.corp_Acc_Id as corpAccId, t.corp_Bank as corpBank, t.corp_Bank_Acc as corpBankAcc,t.corp_acc_cur_id as corpAccCurId, t.corp_Acc_Name as corpAccName, t.remit_Method as remitMethod,");
        sql.append(" t.corp_Name_En as corpNameEn, t.corp_Addr_En as corpAddrEn, t.corp_Addr as corpAddr, t.corp_Contact as corpContact, t.corp_Contact_Tel as corpContactTel,t.cost_Sign as costSign,t.cost_Acc_Id as costAccId, t.cost_Acc_Id2 as costAccId2, ");
        sql.append(" t.cost_Acc as costAcc, t.cost_Acc2 as costAcc2,t.pay_Type as payType, t.ship_Date as shipDate, t.is_Bonded as isBonded, t.tran_Code1 as tranCode1, t.tran_Amt1 as tranAmt1, t.tran_Cur_Id1 as tranCurId1,t.cotp_Acc_Cur_Id as cotpAccCurId, ");
        sql.append(" t.tran_Add_Word1 as tranAddWord1, t.tran_Code2 as tranCode2, t.tran_Amt2 as tranAmt2, t.tran_Cur_Id2 as tranCurId2, t.tran_Add_Word2 as tranAddWord2, t.remit_Type as remitType,t.remit_Nature as remitNature,t.status as status, t.corp_Id as corpId, ");
        sql.append(" t.capital_Use as capitalUse, t.src_Bank as srcBank, t.src_Bank_Acc as srcBankAcc, t.src_Acc_Id as srcAccId,t.src_Acc_Name as srcAccName, t.target_Bank as targetBank, t.target_Acc_Id as targetAccId,t.target_Bank_Acc as targetBankAcc, t.target_Acc_Name as targetAccName, t.target_Corp as targetCorp, t.agent_Bank as agentBank, ");
        sql.append(" t.agent_Bank_Acc as agentBankAcc, t.country_Id as countryId, case when t.tran_Type = '3' THEN c.name_zh ELSE c.name_en END as nameZh, t.cotp_No as cotpNo, t.tran_Addr as tranAddr,t.tran_Group as tranGroup,g.group_Name as tranGroupName,t.tran_Attr as tranAttr,t.swap_Type as swapType,t.partner_Id as partnerId, ");
        sql.append(" t.fixed_Money as fixedMoney,");
        sql.append(" t.create_By as createBy, t.create_Date as createDate, t.update_By as updateBy, t.update_Date as updateDate,t.Settle_bus_type as settleBusType,t.auto_Cal as autoCal, ");
        sql.append(" t.volatility as volatility,t.bus_Direction as busDirection,t.updown_Direction as updownDirection,t.expires_Date as expiresDate,t.avg_Begin_Date as avgBeginDate,t.avg_End_Date as avgEndDate,");
        sql.append(" t.avg_Days as avgDays,t.valid_Standard as validStandard,t.except_Low_Val as exceptLowVal,t.except_Hign_Val as exceptHignVal,t.tran_Settle_Method as tranSettleMethod,t.tran_Addr_Id as tranAddrId,");
        sql.append(" t.premium_Date as premiumDate,t.premium_Cur as premiumCur,t.premium_Point as premiumPoint,t.premium_Ratio as premiumRatio,t.premium_Rate as premiumRate,t.premium_Money as premiumMoney,t.is_Refund as isRefund,");
        sql.append(" t.refund_Point as refundPoint,t.refund_Ratio as refundRatio,t.refund_Money as refundMoney,t.refund_Time as refundTime,t.cotp_Agent as cotpAgent,t.cotp_Clear_House as cotpClearHouse,");
        sql.append(" p.fex_Option1 as fexOption1,p.fex_Option2 as fexOption2,p.fex_Option3 as fexOption3,p.fex_Option_Type as fexOptionType,");
        sql.append(" p.is_Show_Agent as isShowAgent,p.is_Show_Clear_House as isShowClearHouse,targetAcc.is_domestic_bank as isDomesticBank,targetAcc.bank_code as targetBankCode,");
        sql.append(" t.target_Acc_Cur_Id as targetAccCurId,t.dif_Bank as difBank,t.is_Payed as isPayed,t.bursar_bill_no as bursarBillNo,");
        sql.append(" t.acc_type as accType,t.payee_Type as payeeType,t.remit_Route as remitRoute,t.remit_Use as remitUse,t.rel_bill_no as relBillNo,");
        sql.append(" t.branch_Code as branchCode,t.branch_Name as branchName,t.arrival_Date as arrivalDate,t.declare_No as declareNo,t.add_Word as addWord,");
        sql.append(" t.cost_Cur_Id as costCurId,t.cost_Money as costMoney,t.bank_Pay_Type as bankPayType,t.split_id as splitId,t.merge_id as mergeId,t.orig_bill_no as origBillNo,t.origin_Country as originCountry, originCountry.name_zh as originCountryName, ");
        sql.append(" t.cotp_Country as cotpCountry,t.cotp_Address as cotpAddress,t.cotp_Prov as cotpProv,t.cotp_City as cotpCity,t.bill_From as billFrom,");
        sql.append(" t.cash_Item as cashItem,i.item_name as cashItemName,t.foreign_Contract_No as foreignContractNo,t.save_fields as saveFields,t.invoice_date as invoiceDate, ");
        sql.append(" t.invoice_total as invoiceTotal, t.deduction_amount as deductionAmount, t.invoice_amount as invoiceAmount,t.regulatory_Type as regulatoryType, ");
        sql.append(" t.fps_id as fpsId, t.e_mail as eMail, t.isd_code as isdCode,t.phone_code as phoneCode,t.aiin as aiIn,t.hkid as hkId,t.bnf_Relate as bnfRelate, ");
        sql.append(" case when t.tran_Type = '3' THEN cotpCountry.name_zh ELSE cotpCountry.name_en END as cotpCountryName, cotpAddress.name_zh as cotpAddressName,cotpAddress.name_en as cotpAddressNameEn,t.time_Stamp as timeStamp,");
        sql.append(" fx.tran_cur_pair_id as tranCurPairId,fx.position_money as positionMoney,");
        sql.append(" (case when f.blendsum=0 then 0 when f.flowsum=f.blendsum then 1 when f.flowsum>f.blendsum then 2 else 0 end) blendSign, ");
        sql.append(" (select max(exc.voucher_stat) from bis_foreign_exc exc where exc.cbs_bill_id = t.id");
        sql.append(" or exists (select 1 from fex_cash_flow cash where cash.id = exc.cbs_bill_id and cash.tran_bill_id = t.id and cash.status <> -2)) as voucherStat,");
        sql.append(" t.goods_Amount as goodsAmount,t.advance_Charge_Amount as advanceChargeAmount,t.advance_Contract_Rate as advanceContractRate,t.rpt_Gate_Date as rptGateDate,t.rpt_Gate_Ent_Id as rptGateEntId,t.rpt_Gate_Type as rptGateType,");
        sql.append(" t.general_Trade_Amount as generalTradeAmount,t.feed_Amount as feedAmount,t.other_Trade_Amount as otherTradeAmount,t.border_Trade_Amount as borderTradeAmount,t.custom_Amount as customAmount,t.offshore_Amount as offshoreAmount,");
        sql.append(" t.other_Amount as otherAmount,t.invest_Amt as investAmt,t.invest_No as investNo,t.invest_Apply_Nbr as investApplyNbr,t.often_Amt as oftenAmt,t.often_No as oftenNo,");
        sql.append(" t.direct_Investment_Cert_No as directInvestmentCertNo,t.advance_Rate as advanceRate,t.check_Day as checkDay");
        sql.append(" from fex_tran_req t left join fex_product p on t.pro_id=p.id");
        sql.append(" left join fex_tran_template_config tc on t.template_id=tc.Id");
        sql.append(" left join sys_country_cust c on t.country_Id=c.id");
        //交易对手国家地区
        sql.append(" left join sys_country_cust cotpCountry on cotpCountry.id=t.cotp_Country");
        //交易对手地点
        sql.append(" left join sys_country_cust cotpAddress on cotpAddress.id=t.cotp_Address");
        //交易组
        sql.append(" left join fex_tran_group g on g.id=t.tran_Group");
        //交易需求明细
        sql.append(" left join fex_tran_app_dtl dtl on dtl.id=t.tran_App_Dtl_Id");
        //交易需求主
        sql.append(" left join fex_tran_app a on a.id=dtl.app_id");
        //目标账户
        sql.append(" left join bt_bank_acc targetAcc on targetAcc.id=t.target_Acc_Id");
        //起运国
        sql.append(" left join sys_country_cust originCountry on originCountry.id=t.origin_Country");
        //现金流项目
        sql.append(" left join fc_cash_item i on t.cash_Item=i.id");
        //扩展信息
        sql.append(" left join fex_tran_req_ext fx on t.id=fx.req_id");
        //流水勾兑状态
        sql.append(" left join (select cash.tran_bill_id,count(*) flowSum,sum(case when (cash.blend_sign='1' OR cash.bank_flow_sign='1') then 1 else 0 end) blendSum from fex_cash_flow cash where cash.bill_no is not null group by cash.tran_bill_id) f on f.tran_bill_id = t.id");
        sql.append(" where 1=1");
        return sql.toString();
    }

    /**
     * 交易管理表通用查@param sqlWhere（关联现金流及指令，主要用于资金收付交易，交易和现金流指令一对一的情况）
     *
     * @return
     */
    private String getCommonSqlJoinCashFlow() {
        StringBuilder sql=new StringBuilder();
        sql.append("select t.id as id,t.bill_No as billNo,t.tran_App_Dtl_Id as tranAppDtlId,a.bill_No as tranAppDtlNo,t.tran_Type as tranType,t.is_Swap_Tran as isSwapTran,t.pro_Id as proId,p.pro_Name as proName,");
        sql.append(" t.template_id as templateId,t.template_name as templateName,t.version_no as versionNo, t.bank_pay_type_id as bankPayTypeId, t.bank_pay_type_name as bankPayTypeName,");
        sql.append(" t.fex_Bus_Type as fexBusType,t.fex_Tran_Type as fexTranType,t.delivery_Type as deliveryType,t.delivery_Cur_Id as deliveryCurId,t.buy_Cur_Id as buyCurId,t.sell_Cur_Id as sellCurId,");
        sql.append(" t.buy_Money as buyMoney,t.sell_Money as sellMoney,t.contract_Rate as contractRate,t.spot_Rate as spotRate,t.bank_Note as bankNote,t.tran_Date as tranDate,t.settle_Date as settleDate,t.delivery_Deadline as deliveryDeadline,");
        sql.append(" t.bood_Type as boodType,t.bood_Acc_Id as boodAccId,t.bood_Acc as boodAcc,t.bood_Ratio as boodRatio,t.bood_Bank_Note as boodBankNote,t.bond_Cur_Id as bondCurId,t.contract_No as contractNo,");
        sql.append(" t.forward_Points as forwardPoints, t.bus_No as busNo, t.purpose as purpose, t.purpose_Dtl as purposeDtl, t.funds_Source as fundsSource, t.invoice_No as invoiceNo, t.acc_Nature as accNature, ");
        sql.append(" t.fex_Prop as fexProp, t.bood_Rmk as boodRmk, t.rmk as rmk, t.rmk_Local as rmkLocal, t.attachment_Id as attachmentId, t.cotp_Id as cotpId, t.cotp_Acc_Id as cotpAccId,t.cotp_Bank as cotpBank, t.cotp_Bank_Acc as cotpBankAcc, t.cotp_Acc_Name as cotpAccName, t.cur_Id as curId, t.cd_Sign as cdSign,");
        sql.append(" t.pay_Corp_Id as payCorpId, t.pay_Money as payMoney, t.pay_Money_Convert as payMoneyConvert, t.is_Quick as isQuick, t.corp_Acc_Id as corpAccId, t.corp_Bank as corpBank, t.corp_Bank_Acc as corpBankAcc,t.corp_acc_cur_id as corpAccCurId, t.corp_Acc_Name as corpAccName, t.remit_Method as remitMethod,");
        sql.append(" t.corp_Name_En as corpNameEn, t.corp_Addr_En as corpAddrEn, t.corp_Contact as corpContact, t.corp_Contact_Tel as corpContactTel,t.cost_Sign as costSign,t.cost_Acc_Id as costAccId, t.cost_Acc_Id2 as costAccId2, ");
        sql.append(" t.cost_Acc as costAcc, t.cost_Acc2 as costAcc2,t.pay_Type as payType, t.ship_Date as shipDate, t.is_Bonded as isBonded, t.tran_Code1 as tranCode1, t.tran_Amt1 as tranAmt1, t.tran_Cur_Id1 as tranCurId1,t.cotp_Acc_Cur_Id as cotpAccCurId, ");
        sql.append(" t.tran_Add_Word1 as tranAddWord1, t.tran_Code2 as tranCode2, t.tran_Amt2 as tranAmt2, t.tran_Cur_Id2 as tranCurId2, t.tran_Add_Word2 as tranAddWord2, t.remit_Type as remitType,t.remit_Nature as remitNature,t.status as status, t.corp_Id as corpId, ");
        sql.append(" t.capital_Use as capitalUse, t.src_Bank as srcBank, t.src_Bank_Acc as srcBankAcc, t.src_Acc_Id as srcAccId,t.src_Acc_Name as srcAccName, t.target_Bank as targetBank, t.target_Acc_Id as targetAccId,t.target_Bank_Acc as targetBankAcc, t.target_Acc_Name as targetAccName, t.target_Corp as targetCorp, t.agent_Bank as agentBank, ");
        sql.append(" t.agent_Bank_Acc as agentBankAcc, t.country_Id as countryId, case when t.tran_Type = '3' THEN c.name_zh ELSE c.name_en END as nameZh, t.cotp_No as cotpNo, t.tran_Addr as tranAddr,t.tran_Group as tranGroup,g.group_Name as tranGroupName,t.tran_Attr as tranAttr,t.swap_Type as swapType,t.partner_Id as partnerId, ");
        sql.append(" t.fixed_Money as fixedMoney,");
        sql.append(" t.create_By as createBy, t.create_Date as createDate, t.update_By as updateBy, t.update_Date as updateDate,t.Settle_bus_type as settleBusType,t.auto_Cal as autoCal, case when t.tran_Type = '3' THEN cotpCountry.name_zh ELSE cotpCountry.name_en END as cotpCountryName, cotpCountry.id as cotpCountry, ");
        sql.append(" t.volatility as volatility,t.bus_Direction as busDirection,t.updown_Direction as updownDirection,t.expires_Date as expiresDate,t.avg_Begin_Date as avgBeginDate,t.avg_End_Date as avgEndDate,");
        sql.append(" t.avg_Days as avgDays,t.valid_Standard as validStandard,t.except_Low_Val as exceptLowVal,t.except_Hign_Val as exceptHignVal,t.tran_Settle_Method as tranSettleMethod,t.tran_Addr_Id as tranAddrId,");
        sql.append(" t.premium_Date as premiumDate,t.premium_Cur as premiumCur,t.premium_Point as premiumPoint,t.premium_Ratio as premiumRatio,t.premium_Rate as premiumRate,t.premium_Money as premiumMoney,t.is_Refund as isRefund,");
        sql.append(" t.refund_Point as refundPoint,t.refund_Ratio as refundRatio,t.refund_Money as refundMoney,t.refund_Time as refundTime,t.cotp_Agent as cotpAgent,t.cotp_Clear_House as cotpClearHouse,");
        sql.append(" p.fex_Option1 as fexOption1,p.fex_Option2 as fexOption2,p.fex_Option3 as fexOption3,p.fex_Option_Type as fexOptionType,");
        sql.append(" p.is_Show_Agent as isShowAgent,p.is_Show_Clear_House as isShowClearHouse,targetAcc.is_domestic_bank as isDomesticBank,targetAcc.bank_code as targetBankCode,");
        sql.append(" t.target_Acc_Cur_Id as targetAccCurId,t.dif_Bank as difBank,t.is_Payed as isPayed,t.bursar_bill_no as bursarBillNo,t.bill_From as billFrom,");
        sql.append(" t.acc_type as accType,t.payee_Type as payeeType,t.remit_Route as remitRoute,t.remit_Use as remitUse,t.rel_bill_no as relBillNo,");
        sql.append(" t.branch_Code as branchCode,t.branch_Name as branchName,t.arrival_Date as arrivalDate,t.declare_No as declareNo,t.add_Word as addWord,");
        sql.append(" t.cost_Cur_Id as costCurId,t.cost_Money as costMoney,t.bank_Pay_Type as bankPayType,f.pay_Status as payStatus,f.LANDING_LIMIT_RESULT as landingLimitResult,exc.voucher_stat as voucherStat,t.split_id as splitId,t.merge_id as mergeId,t.orig_bill_no as origBillNo,t.time_Stamp as timeStamp,  ");
        sql.append(" t.goods_Amount as goodsAmount,t.advance_Charge_Amount as advanceChargeAmount,t.advance_Contract_Rate as advanceContractRate,t.rpt_Gate_Date as rptGateDate,t.rpt_Gate_Ent_Id as rptGateEntId,t.rpt_Gate_Type as rptGateType,");
        sql.append(" t.general_Trade_Amount as generalTradeAmount,t.feed_Amount as feedAmount,t.other_Trade_Amount as otherTradeAmount,t.border_Trade_Amount as borderTradeAmount,t.custom_Amount as customAmount,t.offshore_Amount as offshoreAmount,");
        sql.append(" t.other_Amount as otherAmount,t.invest_Amt as investAmt,t.invest_No as investNo,t.invest_Apply_Nbr as investApplyNbr,t.often_Amt as oftenAmt,t.often_No as oftenNo,");
        sql.append(" t.direct_Investment_Cert_No as directInvestmentCertNo,t.advance_Rate as advanceRate,t.check_Day as checkDay");
        sql.append(" from fex_tran_req t left join fex_product p on t.pro_id=p.id");
        sql.append(" left join fex_tran_template_config tc on t.template_id=tc.Id");
        sql.append(" left join sys_country_cust c on t.country_Id=c.id");
        sql.append(" left join sys_country_cust cotpCountry on cotpCountry.id=t.cotp_Country");
        //交易组
        sql.append(" left join fex_tran_group g on g.id=t.tran_Group");
        //交易需求明细
        sql.append(" left join fex_tran_app_dtl dtl on dtl.id=t.tran_App_Dtl_Id");
        //交易需求主
        sql.append(" left join fex_tran_app a on a.id=dtl.app_id");
        //目标账户
        sql.append(" left join bt_bank_acc targetAcc on targetAcc.id=t.target_Acc_Id");
        //关联现金流
        sql.append(" left join fex_cash_flow f on f.tran_bill_id = t.id and f.status <> -2");
        //关联指令
        sql.append(" left join bis_foreign_exc exc on exc.cbs_bill_id=f.id");
        //扩展信息
        sql.append(" left join fex_tran_req_ext fx on t.id=fx.req_id");
        sql.append(" where 1=1");
        return sql.toString();
    }

    /**
     * 交易管理列表查询
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput<FexTranReqVO> getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = null;
        String tranType = (String) dataTablesInput.getFormdata().get("tranType");
        String opType = (String) dataTablesInput.getFormdata().get("opType");

        HashMap<String, Object> params = new HashMap<>();
        if (ForeignConstant.TRAN_TYPE_4.equals(tranType) && "query".equals(opType)) {
            sql = new StringBuilder(this.getCommonSqlJoinCashFlow());
        } else if (ForeignConstant.TRAN_TYPE_2.equals(tranType)) {
            sql = new StringBuilder(this.getCommonSqlJoin());
            String tranCurPairId = (String) dataTablesInput.getFormdata().get("tranCurPairId");
            if (StringUtils.isNotEmpty(tranCurPairId)) {
                sql.append(" and fx.tran_cur_pair_id = :tranCurPairId");
                params.put("tranCurPairId", tranCurPairId);
            }
        } else {
            sql = new StringBuilder(this.getCommonSqlJoin());
        }
        if(StringUtils.isNotEmpty(joinSql)){
            sql.append(joinSql);
        }
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
        return getDynamicBySQL(dataTablesInput, sql.toString(), FexTranReqVO.class, "t", false, params);
    }

    /**
     *
     * 买卖交易管理列表
     *
     * @param dataTablesInput
     * @param joinSql
     * @return
     */
    public DataTablesOutput getDataTablesUnionForeign(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = null;
        sql = new StringBuilder(this.getCommonSqlJoinForeign());
        if(StringUtils.isNotEmpty(joinSql)){
            sql.append(joinSql);
        }
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
        if(dataTablesInput.getFormdata().get("ins_blendSign")!=null&&!StringUtils.isBlank(dataTablesInput.getFormdata().get("ins_blendSign").toString())){
            String blendSign = dataTablesInput.getFormdata().get("ins_blendSign").toString();
            String[] blendSignArr = blendSign.split(",");
            sql.append(" and( ");
            for (int i = 0; i < blendSignArr.length; i++) {
                String blendSignItem = blendSignArr[i];
                if (0 < i) {
                    sql.append(" or ");
                }
                if("0".equals(blendSignItem)){
                    sql.append("  (f.blendSum=0 or f.blendSum is null) ");
                }else if("1".equals(blendSignItem)){
                    sql.append("  (f.blendSum=f.flowSum AND blendSum > 0) ");
                }else if("2".equals(blendSignItem)){
                    sql.append("  (f.blendSum<f.flowSum AND blendSum > 0) ");
                }
            }
            sql.append(" ) ");
            dataTablesInput.getFormdata().remove("ins_blendSign");
        }
        HashMap<String, Object> params = new HashMap<>();
        if (dataTablesInput.getFormdata().get("eqi_status") ==null){
            sql.append(" and t.status <> :dleStatus ");
            params.put("dleStatus", -2);
        }
        if(dataTablesInput.getFormdata().get("eqs_tranCurPairId") !=null
                && !StringUtils.isBlank(dataTablesInput.getFormdata().get("eqs_tranCurPairId").toString())){
            String tranCurPairId = dataTablesInput.getFormdata().get("eqs_tranCurPairId").toString();
            String[] tranCurPairArr = tranCurPairId.split(",");
            int paramIndex = 0;
            sql.append(" and (");
            for (String pair : tranCurPairArr) {
                String[] tranCurPairIdArr = pair.split("_");
                if (paramIndex > 0) {
                    sql.append(" or ");
                }
                // 使用带索引的参数名，避免重复
                String baseIdParam = "tranCurPairBaseId_" + paramIndex;
                String referIdParam = "tranCurPairReferId_" + paramIndex;
                sql.append("(fx.tran_cur_pair_base_id = :").append(baseIdParam).append(" and fx.tran_cur_pair_refer_id = :").append(referIdParam).append(")");
                params.put(baseIdParam, tranCurPairIdArr[0].trim());
                params.put(referIdParam, tranCurPairIdArr[1].trim());
                paramIndex++;
            }
            sql.append(")");
            dataTablesInput.getFormdata().remove("eqs_tranCurPairId");
        }
        if(dataTablesInput.getFormdata().get("geg_positionMoney") !=null
                && !StringUtils.isBlank(dataTablesInput.getFormdata().get("geg_positionMoney").toString())){
            sql.append(" and fx.position_money >= ").append(dataTablesInput.getFormdata().get("geg_positionMoney"));
        }
        if(dataTablesInput.getFormdata().get("leg_positionMoney") !=null
                && !StringUtils.isBlank(dataTablesInput.getFormdata().get("leg_positionMoney").toString())){
            sql.append(" and fx.position_money <= ").append(dataTablesInput.getFormdata().get("leg_positionMoney"));
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), FexTranReqVO.class, "t", false, params);
    }

    public DataTablesOutput<FexTranReqVO> getDataTablesForeignApprove(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = null;
        sql = new StringBuilder(this.getCommonSqlJoinForeign());
        if(StringUtils.isNotEmpty(joinSql)){
            sql.append(" and ").append(joinSql);
        }
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
        if(dataTablesInput.getFormdata().get("eqs_blendSign")!=null&&!StringUtils.isBlank(dataTablesInput.getFormdata().get("eqs_blendSign").toString())){
            String blendSign = dataTablesInput.getFormdata().get("eqs_blendSign").toString();
            // 与列表页/展示逻辑一致：0-未勾对(含null)、1-已勾对、2-部分勾对
            if("0".equals(blendSign)){
                sql.append(" and (f.blendSum=0 or f.blendSum is null) ");
            }else if("1".equals(blendSign)){
                sql.append(" and (f.blendSum=f.flowSum AND f.blendSum > 0) ");
            }else if("2".equals(blendSign)){
                sql.append(" and (f.blendSum<f.flowSum AND f.blendSum > 0) ");
            }
            dataTablesInput.getFormdata().remove("eqs_blendSign");
        }
        HashMap<String, Object> params = new HashMap<>();
        if (dataTablesInput.getFormdata().get("eqi_status") ==null){
            sql.append(" and t.status <> :dleStatus ");
            params.put("dleStatus", -2);
        }
        if(dataTablesInput.getFormdata().get("eqs_tranCurPairId") !=null
                && !StringUtils.isBlank(dataTablesInput.getFormdata().get("eqs_tranCurPairId").toString())){
            String tranCurPairId = dataTablesInput.getFormdata().get("eqs_tranCurPairId").toString();
            String[] tranCurPairArr = tranCurPairId.split(",");
            int paramIndex = 0;
            sql.append(" and (");
            for (String pair : tranCurPairArr) {
                String[] tranCurPairIdArr = pair.split("_");
                if (paramIndex > 0) {
                    sql.append(" or ");
                }
                String baseIdParam = "tranCurPairBaseId_" + paramIndex;
                String referIdParam = "tranCurPairReferId_" + paramIndex;
                sql.append("(fx.tran_cur_pair_base_id = :").append(baseIdParam).append(" and fx.tran_cur_pair_refer_id = :").append(referIdParam).append(")");
                params.put(baseIdParam, tranCurPairIdArr[0].trim());
                params.put(referIdParam, tranCurPairIdArr[1].trim());
                paramIndex++;
            }
            sql.append(")");
            dataTablesInput.getFormdata().remove("eqs_tranCurPairId");
        }
        if(dataTablesInput.getFormdata().get("geg_positionMoney") !=null
                && !StringUtils.isBlank(dataTablesInput.getFormdata().get("geg_positionMoney").toString())){
            sql.append(" and fx.position_money >= ").append(dataTablesInput.getFormdata().get("geg_positionMoney"));
        }
        if(dataTablesInput.getFormdata().get("leg_positionMoney") !=null
                && !StringUtils.isBlank(dataTablesInput.getFormdata().get("leg_positionMoney").toString())){
            sql.append(" and fx.position_money <= ").append(dataTablesInput.getFormdata().get("leg_positionMoney"));
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), FexTranReqVO.class, "t", false, params);
    }

    /**
     * 根据ID获取交易信息
     * @param id
     * @return
     */
    public FexTranReqVO getByIdUnion(String id){
        StringBuilder sql=new StringBuilder(this.getCommonSqlJoin());
        sql.append(" and t.id=:id");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, FexTranReqVO.class);
    }

    public List<FexTranReqVO> getByIds(List<String> ids){
        StringBuilder sql=new StringBuilder(this.getCommonSqlJoin());
        sql.append(" and ").append(getSQLIn(ids, 500, "t.id"));
        HashMap<String, String> params = new HashMap<String, String>();
        return listBySQLAliasToBean(sql.toString(), params, FexTranReqVO.class);
    }

    /**
     * 根据billNo获取交易信息
     * @param billNo
     * @return
     */
    public FexTranReqVO getByBillNoUnion(String billNo){
        StringBuilder sql=new StringBuilder(this.getCommonSqlJoin());
        sql.append(" and t.bill_no = :billNo");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("billNo", billNo);
        return getBySQLAliasToBean(sql.toString(), params, FexTranReqVO.class);
    }

    /**
     * 根据交易组获取交易信息
     * @param tranGroup
     * @return
     */
    public List<FexTranReqVO> listVOByTranGroup(String tranGroup){
        StringBuilder sql=new StringBuilder(this.getCommonSql());
        sql.append(" and t.tran_Group=:tranGroup");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("tranGroup", tranGroup);
        return listBySQLAliasToBean(sql.toString(), params, FexTranReqVO.class);
    }

    /**
     * 获取审批信息
     * @param dataTablesInput
     * @param businessSql
     * @return
     */
    public DataTablesOutput<FexTranReqVO> dataTableApprove(DataTablesInput dataTablesInput, String businessSql) {
        StringBuilder sql=new StringBuilder(this.getCommonSqlJoin());
        sql.append(" and " + businessSql);
        String flowStatus = dataTablesInput.getParameter().get("flowStatus");
        //已审批不过滤状态
        if(!IWorkFlowService.PASS.equals(flowStatus)){
            sql.append(" and  t.status<> ").append(NetbankConstant.STATUE_CHECK);
        }
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
        if(dataTablesInput.getFormdata().get("eqs_blendSign")!=null&&!StringUtils.isBlank(dataTablesInput.getFormdata().get("eqs_blendSign").toString())){
            String blendSign = dataTablesInput.getFormdata().get("eqs_blendSign").toString();
            if("0".equals(blendSign)){
                sql.append(" and (f.blendSum=0 or f.blendSum is null) ");
            }else if("1".equals(blendSign)){
                sql.append(" and (f.blendSum=f.flowSum AND f.blendSum > 0) ");
            }else if("2".equals(blendSign)){
                sql.append(" and (f.blendSum<f.flowSum AND f.blendSum > 0) ");
            }
            dataTablesInput.getFormdata().remove("eqs_blendSign");
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), FexTranReqVO.class, "t", false);
    }

    /**
     * 根据单位和业务类型统计交易信息
     * @param corpId
     * @param tranType
     * @param busType del:单位删除 disable:禁用 change:上级变更
     * @return
     */
    public int getTranReqCountByCorpId(String corpId, String tranType, String busType){
        String sql="select count(*) from fex_tran_req where corp_id=:corpId and tran_Type=:tranType";
        HashMap<String,Object> params=new HashMap<>();
        params.put("corpId",corpId);
        params.put("tranType",tranType);
        if(!"del".equals(busType)){
            //0-暂存、11-待审批、-1-审批打回
            sql+=" and status in ('0','11','-1')";
        }
        return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
    }

    /**
     * 根据合并单号获取合并之前的明细单
     *
     * @param mergeId
     */
    public List<FexTranReqVO> listByMergeId(String mergeId) {
        StringBuilder sql = new StringBuilder(this.getCommonSqlJoinCashFlow());
        sql.append(" and  t.merge_id=:mergeId");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("mergeId", mergeId);
        return listBySQLAliasToBean(sql.toString(), params, FexTranReqVO.class);
    }

    /**
     * 带原状态更新付款单
     * @param vo
     * @param oldStatus
     * @return
     */
    public int updateFexTranReqStatus(FexTranReqVO vo, Integer[] oldStatus) {
        StringBuilder hql=new StringBuilder();
        hql.append(" update FexTranReq set tranAppDtlId=:tranAppDtlId,tranType=:tranType,isSwapTran=:isSwapTran,proId=:proId,fexBusType=:fexBusType,");
        hql.append(" fexTranType=:fexTranType,deliveryType=:deliveryType,deliveryCurId=:deliveryCurId,buyCurId=:buyCurId,sellCurId=:sellCurId,");
        hql.append(" buyMoney=:buyMoney,sellMoney=:sellMoney,contractRate=:contractRate,foreignContractNo=:foreignContractNo,");
        hql.append(" spotRate=:spotRate,bankNote=:bankNote,tranDate=:tranDate,settleDate=:settleDate,deliveryDeadline=:deliveryDeadline,boodType=:boodType,");
        hql.append(" boodAccId=:boodAccId,boodAcc=:boodAcc,boodRatio=:boodRatio,boodBankNote=:boodBankNote,bondCurId=:bondCurId,contractNo=:contractNo,");
        hql.append(" forwardPoints=:forwardPoints,busNo=:busNo,purpose=:purpose,purposeDtl=:purposeDtl,fundsSource=:fundsSource,invoiceNo=:invoiceNo,");
        hql.append(" accNature=:accNature,fexProp=:fexProp,boodRmk=:boodRmk,rmk=:rmk,rmkLocal=:rmkLocal,attachmentId=:attachmentId,");
        hql.append(" cotpId=:cotpId,cotpBank=:cotpBank,cotpAccId=:cotpAccId,cotpBankAcc=:cotpBankAcc,cotpAccName=:cotpAccName,cotpAccCurId=:cotpAccCurId,");
        hql.append(" cotpCountry=:cotpCountry,cotpAddress=:cotpAddress,cotpProv=:cotpProv,cotpCity=:cotpCity,curId=:curId,");
        hql.append(" cdSign=:cdSign,payCorpId=:payCorpId,payMoney=:payMoney,payMoneyConvert=:payMoneyConvert,isQuick=:isQuick,");
        hql.append(" corpAccId=:corpAccId,corpAccCurId=:corpAccCurId,corpBank=:corpBank,corpBankAcc=:corpBankAcc,corpAccName=:corpAccName, ");
        hql.append(" remitType=:remitType,remitMethod=:remitMethod,corpNameEn=:corpNameEn,corpAddrEn=:corpAddrEn,corpAddr=:corpAddr, ");
        hql.append(" corpContact=:corpContact,corpContactTel=:corpContactTel,costSign=:costSign,costAccId=:costAccId,costAcc=:costAcc, ");
        hql.append(" costAccId2=:costAccId2,costAcc2=:costAcc2,payType=:payType,shipDate=:shipDate,isBonded=:isBonded,tranCode1=:tranCode1, ");
        hql.append(" tranAmt1=:tranAmt1,tranCurId1=:tranCurId1,tranAddWord1=:tranAddWord1,tranCode2=:tranCode2,tranAmt2=:tranAmt2, ");
        hql.append(" tranCurId2=:tranCurId2,tranAddWord2=:tranAddWord2,remitNature=:remitNature,status=:status,corpId=:corpId, ");
        hql.append(" capitalUse=:capitalUse,srcBank=:srcBank,srcAccId=:srcAccId,srcBankAcc=:srcBankAcc,srcAccName=:srcAccName, ");
        hql.append(" targetBank=:targetBank,targetAccId=:targetAccId,targetBankAcc=:targetBankAcc,targetAccName=:targetAccName,targetAccCurId=:targetAccCurId, ");
        hql.append(" targetCorp=:targetCorp,agentBank=:agentBank,agentBankAcc=:agentBankAcc,countryId=:countryId,cotpNo=:cotpNo, ");
        hql.append(" tranAddr=:tranAddr,tranGroup=:tranGroup,swapType=:swapType,partnerId=:partnerId,tranAttr=:tranAttr, ");
        hql.append(" settleBusType=:settleBusType,autoCal=:autoCal,fixedMoney=:fixedMoney,volatility=:volatility,busDirection=:busDirection, ");
        hql.append(" updownDirection=:updownDirection,expiresDate=:expiresDate,avgBeginDate=:avgBeginDate,avgEndDate=:avgEndDate,avgDays=:avgDays, ");
        hql.append(" validStandard=:validStandard,exceptLowVal=:exceptLowVal,exceptHignVal=:exceptHignVal,tranSettleMethod=:tranSettleMethod,tranAddrId=:tranAddrId, ");
        hql.append(" premiumDate=:premiumDate,premiumCur=:premiumCur,premiumPoint=:premiumPoint,premiumRatio=:premiumRatio,premiumRate=:premiumRate, ");
        hql.append(" premiumMoney=:premiumMoney,isRefund=:isRefund,refundPoint=:refundPoint,refundRatio=:refundRatio,refundMoney=:refundMoney, ");
        hql.append(" refundTime=:refundTime,cotpAgent=:cotpAgent,cotpClearHouse=:cotpClearHouse,difBank=:difBank,isPayed=:isPayed, ");
        hql.append(" bursarBillNo=:bursarBillNo,accType=:accType,payeeType=:payeeType,remitRoute=:remitRoute,remitUse=:remitUse, ");
        hql.append(" relBillNo=:relBillNo,branchCode=:branchCode,branchName=:branchName,arrivalDate=:arrivalDate,declareNo=:declareNo, ");
        hql.append(" addWord=:addWord,costCurId=:costCurId,costMoney=:costMoney,bankPayType=:bankPayType,originCountry=:originCountry, ");
        hql.append(" splitId=:splitId,mergeId=:mergeId,origBillNo=:origBillNo,cashItem=:cashItem,invoiceDate=:invoiceDate, ");
        hql.append(" invoiceTotal=:invoiceTotal,deductionAmount=:deductionAmount,invoiceAmount=:invoiceAmount,saveFields=:saveFields,fpsId=:fpsId, ");
        hql.append(" eMail=:eMail,isdCode=:isdCode,phoneCode=:phoneCode,aiIn=:aiIn,hkId=:hkId,bnfRelate=:bnfRelate,regulatoryType=:regulatoryType, ");
        hql.append(" billFrom=:billFrom,timeStamp=:timeStamp,premiumMoneyOfPremiumCur=:premiumMoneyOfPremiumCur, ");
        hql.append(" goodsAmount=:goodsAmount,advanceChargeAmount=:advanceChargeAmount,advanceContractRate=:advanceContractRate,rptGateDate=:rptGateDate,");
        hql.append(" rptGateEntId=:rptGateEntId,rptGateType=:rptGateType,generalTradeAmount=:generalTradeAmount,feedAmount=:feedAmount,otherTradeAmount=:otherTradeAmount,");
        hql.append(" borderTradeAmount=:borderTradeAmount,customAmount=:customAmount,offshoreAmount=:offshoreAmount,otherAmount=:otherAmount,investAmt=:investAmt,");
        hql.append(" investNo=:investNo,investApplyNbr=:investApplyNbr,oftenAmt=:oftenAmt,oftenNo=:oftenNo,directInvestmentCertNo=:directInvestmentCertNo,");
        hql.append(" advanceRate=:advanceRate,checkDay=:checkDay,");
        hql.append(" templateId=:templateId,templateName=:templateName,versionNo=:versionNo,bankPayTypeName=:bankPayTypeName,bankPayTypeId=:bankPayTypeId");

        Map<String,Object> params = new HashMap<>();
        params.put("tranAppDtlId", vo.getTranAppDtlId());
        params.put("tranType", vo.getTranType());
        params.put("isSwapTran", vo.getIsSwapTran());
        params.put("proId", vo.getProId());
        params.put("fexBusType", vo.getFexBusType());
        params.put("fexTranType", vo.getFexTranType());
        params.put("deliveryType", vo.getDeliveryType());
        params.put("deliveryCurId", vo.getDeliveryCurId());
        params.put("buyCurId", vo.getBuyCurId());
        params.put("sellCurId", vo.getSellCurId());
        params.put("buyMoney", vo.getBuyMoney());
        params.put("sellMoney", vo.getSellMoney());
        params.put("contractRate", vo.getContractRate());
        params.put("foreignContractNo", vo.getForeignContractNo());
        params.put("spotRate", vo.getSpotRate());
        params.put("bankNote", vo.getBankNote());
        params.put("tranDate", vo.getTranDate());
        params.put("settleDate", vo.getSettleDate());
        params.put("deliveryDeadline", vo.getDeliveryDeadline());
        params.put("boodType", vo.getBoodType());
        params.put("boodAccId", vo.getBoodAccId());
        params.put("boodAcc", vo.getBoodAcc());
        params.put("boodRatio", vo.getBoodRatio());
        params.put("boodBankNote", vo.getBoodBankNote());
        params.put("bondCurId", vo.getBondCurId());
        params.put("contractNo", vo.getContractNo());
        params.put("forwardPoints", vo.getForwardPoints());
        params.put("busNo", vo.getBusNo());
        params.put("purpose", vo.getPurpose());
        params.put("purposeDtl", vo.getPurposeDtl());
        params.put("fundsSource", vo.getFundsSource());
        params.put("invoiceNo", vo.getInvoiceNo());
        params.put("accNature", vo.getAccNature());
        params.put("fexProp", vo.getFexProp());
        params.put("boodRmk", vo.getBoodRmk());
        params.put("rmk", vo.getRmk());
        params.put("rmkLocal", vo.getRmkLocal());
        params.put("attachmentId", vo.getAttachmentId());
        params.put("cotpId", vo.getCotpId());
        params.put("cotpBank", vo.getCotpBank());
        params.put("cotpAccId", vo.getCotpAccId());
        params.put("cotpBankAcc", vo.getCotpBankAcc());
        params.put("cotpAccName", vo.getCotpAccName());
        params.put("cotpAccCurId", vo.getCotpAccCurId());
        params.put("cotpCountry", vo.getCotpCountry());
        params.put("cotpAddress", vo.getCotpAddress());
        params.put("cotpProv", vo.getCotpProv());
        params.put("cotpCity", vo.getCotpCity());
        params.put("curId", vo.getCurId());
        params.put("cdSign", vo.getCdSign());
        params.put("payCorpId", vo.getPayCorpId());
        params.put("payMoney", vo.getPayMoney());
        params.put("payMoneyConvert", vo.getPayMoneyConvert());
        params.put("isQuick", vo.getIsQuick());
        params.put("corpAccId", vo.getCorpAccId());
        params.put("corpAccCurId", vo.getCorpAccCurId());
        params.put("corpBank", vo.getCorpBank());
        params.put("corpBankAcc", vo.getCorpBankAcc());
        params.put("corpAccName", vo.getCorpAccName());
        params.put("remitType", vo.getRemitType());
        params.put("remitMethod", vo.getRemitMethod());
        params.put("corpNameEn", vo.getCorpNameEn());
        params.put("corpAddrEn", vo.getCorpAddrEn());
        params.put("corpAddr", vo.getCorpAddr());
        params.put("corpContact", vo.getCorpContact());
        params.put("corpContactTel", vo.getCorpContactTel());
        params.put("costSign", vo.getCostSign());
        params.put("costAccId", vo.getCostAccId());
        params.put("costAcc", vo.getCostAcc());
        params.put("costAccId2", vo.getCostAccId2());
        params.put("costAcc2", vo.getCostAcc2());
        params.put("payType", vo.getPayType());
        params.put("shipDate", vo.getShipDate());
        params.put("isBonded", vo.getIsBonded());
        params.put("tranCode1", vo.getTranCode1());
        params.put("tranAmt1", vo.getTranAmt1());
        params.put("tranCurId1", vo.getTranCurId1());
        params.put("tranAddWord1", vo.getTranAddWord1());
        params.put("tranCode2", vo.getTranCode2());
        params.put("tranAmt2", vo.getTranAmt2());
        params.put("tranCurId2", vo.getTranCurId2());
        params.put("tranAddWord2", vo.getTranAddWord2());
        params.put("remitNature", vo.getRemitNature());
        params.put("status", vo.getStatus());
        params.put("corpId", vo.getCorpId());
        params.put("capitalUse", vo.getCapitalUse());
        params.put("srcBank", vo.getSrcBank());
        params.put("srcAccId", vo.getSrcAccId());
        params.put("srcBankAcc", vo.getSrcBankAcc());
        params.put("srcAccName", vo.getSrcAccName());
        params.put("targetBank", vo.getTargetBank());
        params.put("targetAccId", vo.getTargetAccId());
        params.put("targetBankAcc", vo.getTargetBankAcc());
        params.put("targetAccName", vo.getTargetAccName());
        params.put("targetAccCurId", vo.getTargetAccCurId());
        params.put("targetCorp", vo.getTargetCorp());
        params.put("agentBank", vo.getAgentBank());
        params.put("agentBankAcc", vo.getAgentBankAcc());
        params.put("countryId", vo.getCountryId());
        params.put("cotpNo", vo.getCotpNo());
        params.put("tranAddr", vo.getTranAddr());
        params.put("tranGroup", vo.getTranGroup());
        params.put("swapType", vo.getSwapType());
        params.put("partnerId", vo.getPartnerId());
        params.put("tranAttr", vo.getTranAttr());
        params.put("settleBusType", vo.getSettleBusType());
        params.put("autoCal", vo.getAutoCal());
        params.put("fixedMoney", vo.getFixedMoney());
        params.put("volatility", vo.getVolatility());
        params.put("busDirection", vo.getBusDirection());
        params.put("updownDirection", vo.getUpdownDirection());
        params.put("expiresDate", vo.getExpiresDate());
        params.put("avgBeginDate", vo.getAvgBeginDate());
        params.put("avgEndDate", vo.getAvgEndDate());
        params.put("avgDays", vo.getAvgDays());
        params.put("validStandard", vo.getValidStandard());
        params.put("exceptLowVal", vo.getExceptLowVal());
        params.put("exceptHignVal", vo.getExceptHignVal());
        params.put("tranSettleMethod", vo.getTranSettleMethod());
        params.put("tranAddrId", vo.getTranAddrId());
        params.put("premiumDate", vo.getPremiumDate());
        params.put("premiumCur", vo.getPremiumCur());
        params.put("premiumPoint", vo.getPremiumPoint());
        params.put("premiumRatio", vo.getPremiumRatio());
        params.put("premiumRate", vo.getPremiumRate());
        params.put("premiumMoney", vo.getPremiumMoney());
        params.put("isRefund", vo.getIsRefund());
        params.put("refundPoint", vo.getRefundPoint());
        params.put("refundRatio", vo.getRefundRatio());
        params.put("refundMoney", vo.getRefundMoney());
        params.put("refundTime", vo.getRefundTime());
        params.put("cotpAgent", vo.getCotpAgent());
        params.put("cotpClearHouse", vo.getCotpClearHouse());
        params.put("difBank", vo.getDifBank());
        params.put("isPayed", vo.getIsPayed());
        params.put("bursarBillNo", vo.getBursarBillNo());
        params.put("accType", vo.getAccType());
        params.put("payeeType", vo.getPayeeType());
        params.put("remitRoute", vo.getRemitRoute());
        params.put("remitUse", vo.getRemitUse());
        params.put("relBillNo", vo.getRelBillNo());
        params.put("branchCode", vo.getBranchCode());
        params.put("branchName", vo.getBranchName());
        params.put("arrivalDate", vo.getArrivalDate());
        params.put("declareNo", vo.getDeclareNo());
        params.put("addWord", vo.getAddWord());
        params.put("costCurId", vo.getCostCurId());
        params.put("costMoney", vo.getCostMoney());
        params.put("bankPayType", vo.getBankPayType());
        params.put("originCountry", vo.getOriginCountry());
        params.put("splitId", vo.getSplitId());
        params.put("mergeId", vo.getMergeId());
        params.put("origBillNo", vo.getOrigBillNo());
        params.put("cashItem", vo.getCashItem());
        params.put("invoiceDate", vo.getInvoiceDate());
        params.put("invoiceTotal", vo.getInvoiceTotal());
        params.put("deductionAmount", vo.getDeductionAmount());
        params.put("invoiceAmount", vo.getInvoiceAmount());
        params.put("saveFields", vo.getSaveFields());
        params.put("fpsId", vo.getFpsId());
        params.put("eMail", vo.geteMail());
        params.put("isdCode", vo.getIsdCode());
        params.put("phoneCode", vo.getPhoneCode());
        params.put("aiIn", vo.getAiIn());
        params.put("hkId", vo.getHkId());
        params.put("bnfRelate", vo.getBnfRelate());
        params.put("regulatoryType", vo.getRegulatoryType());
        params.put("billFrom", vo.getBillFrom());
        params.put("timeStamp", vo.getTimeStamp());
        params.put("premiumMoneyOfPremiumCur",vo.getPremiumMoneyOfPremiumCur());
        params.put("goodsAmount", vo.getGoodsAmount());
        params.put("advanceChargeAmount", vo.getAdvanceChargeAmount());
        params.put("advanceContractRate", vo.getAdvanceContractRate());
        params.put("rptGateDate", vo.getRptGateDate());
        params.put("rptGateEntId", vo.getRptGateEntId());
        params.put("rptGateType", vo.getRptGateType());
        params.put("generalTradeAmount", vo.getGeneralTradeAmount());
        params.put("feedAmount", vo.getFeedAmount());
        params.put("otherTradeAmount", vo.getOtherTradeAmount());
        params.put("borderTradeAmount", vo.getBorderTradeAmount());
        params.put("customAmount", vo.getCustomAmount());
        params.put("offshoreAmount", vo.getOffshoreAmount());
        params.put("otherAmount", vo.getOtherAmount());
        params.put("investAmt", vo.getInvestAmt());
        params.put("investNo", vo.getInvestNo());
        params.put("investApplyNbr", vo.getInvestApplyNbr());
        params.put("oftenAmt", vo.getOftenAmt());
        params.put("oftenNo", vo.getOftenNo());
        params.put("directInvestmentCertNo", vo.getDirectInvestmentCertNo());
        params.put("advanceRate", vo.getAdvanceRate());
        params.put("checkDay", vo.getCheckDay());
        params.put("templateId", vo.getTemplateId());
        params.put("templateName", vo.getTemplateName());
        params.put("versionNo", vo.getVersionNo());
        params.put("bankPayTypeName", vo.getBankPayTypeName());
        params.put("bankPayTypeId", vo.getBankPayTypeId());

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
        hql.append(" where id=:id and status in (:oldStatus) ");
        params.put("id", vo.getId());
        params.put("oldStatus", oldStatus);
        return this.batchExecute(hql.toString(), params);
    }
    /**
     * 带原状态更新付款单
     * @param ids
     * @param newStatus
     * @param oldStatus
     * @return
     */
    public int updateFexTranReqStatus(List<String> ids, Integer newStatus,Integer[] oldStatus) {
        StringBuilder hql=new StringBuilder();
        hql.append(" update FexTranReq set status=:newStatus,timeStamp=:timeStamp ");
        hql.append(" where status in (:oldStatus) ");
        if (!CollectionUtils.isEmpty(ids)){
            hql.append(" and "+getSQLIn(ids,900,"id"));
        }
        Map<String,Object> params = new HashMap<>();
        params.put("oldStatus", oldStatus);
        params.put("newStatus", newStatus);
        params.put("timeStamp", String.valueOf(new Date().getTime()));
        return this.batchExecute(hql.toString(), params);
    }
    /**
     * 通过状态获取外汇买卖的交易数据的id
     */
    public List<String> getIdsByStatus(Integer status) {
            String hql = "select id from FexTranReq where status=:status and tranType='1'";
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("status", status);
            return this.listByHQL(hql, params);
    }

    /**
     *
     * 获取未结束交易单
     *
     * @param templateIds
     * @return
     */
    public List<String> getUnFinishedBillNos(List<String> templateIds) {
        if(EmptyUtils.isEmpty(templateIds)){
            return new ArrayList<>();
        }
        // 0-暂存；-1-打回；11-待审批
        String sql = "select bill_no from fex_tran_req where  status in ('0','-1','11') ";
        String sqlWithIn = sql + " and " + getSQLIn(templateIds, 900, "template_id");
        return this.getSession().createSQLQuery(sqlWithIn).list();
    }
}
