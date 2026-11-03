package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexForeignReq;
import com.bytter.biz.foreign.vo.FexForeignReqVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * @author huangx
 * @version 1.0
 * @Title: Dao
 * @Description: 外币汇款申请表
 * @date 2019-07-15 15:20:33
 */
@Repository
public class FexForeignReqDao extends BaseDao<FexForeignReq, String> {

    /**
     * 外币汇款申请表通用查@param sqlWhere
     *
     * @return
     */
    private String getCommonSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("select f.id as id,f.bill_No as billNo,f.corp_Id as corpId,f.remit_Type as remitType,f.pay_Date as payDate,f.cur_Id as curId,f.bank_Type_Id as bankTypeId,");
        sql.append(" f.acc_Id as accId,f.payer_Acc as payerAcc,f.payer_Acc_Name as payerAccName,f.payer_Bank as payerBank,f.payer_Name as payerName,f.payer_Tel as payerTel,");
        sql.append(" f.payer_Name_En as payerNameEn,f.payer_Add_En as payerAddEn,f.payee_Acc as payeeAcc,f.payee_Acc_Name as payeeAccName,f.payee_Add as payeeAdd,f.payee_Swift_Code as payeeSwiftCode,");
        sql.append(" f.payee_Bank as payeeBank,f.payee_Bank_Add as payeeBankAdd,f.payee_Country_Code as payeeCountryCode,f.payee_Agent_Bank as payeeAgentBank,f.payee_Agent_Bank_Add as payeeAgentBankAdd,f.payee_Agent_Acc as payeeAgentAcc,");
        sql.append(" f.pay_Money as payMoney,f.foreign_Exrate as foreignExrate,f.foreign_Money as foreignMoney,f.add_Word as addWord,f.rmk as rmk,f.quick_Sign as quickSign,f.remit_Nature as remitNature,f.remit_Method as remitMethod,");
        sql.append(" f.cost_Sign as costSign,f.pay_Type as payType,f.ship_Date as shipDate,f.is_Bonded as isBonded,f.bus_No as busNo,f.contract_No as contractNo,f.invoice_No as invoiceNo,");
        sql.append(" f.tran_Code1 as tranCode1,f.tran_Amt1 as tranAmt1,f.tran_Cur_Id1 as tranCurId1,f.tran_Add_Word1 as tranAddWord1,f.tran_Code2 as tranCode2,f.tran_Amt2 as tranAmt2,f.tran_Cur_Id2 as tranCurId2,f.tran_Add_Word2 as tranAddWord2,");
        sql.append(" f.status as status,f.attachment_Id as attachmentId,f.create_By as createBy,f.create_Date as createDate,f.update_By as updateBy,f.update_Date as updateDate,");
        sql.append(" c.voucher_Stat as voucherStat,c.id as bisForeignExcId,s.name_Zh as payeeCountryName,f.arrival_Date as arrivalDate,f.declare_No as declareNo,");
        sql.append(" f.acc_type as accType,f.payee_Type as payeeType,f.remit_Route as remitRoute,f.remit_Use as remitUse,f.acc_Nature as accNature,");
        sql.append(" f.payee_Agent_Swift_Code as payeeAgentSwiftCode,f.is_payed as isPayed,f.bill_from as billFrom,f.erp_id as erpId,f.dif_Bank as difBank,f.bank_Pay_Type as bankPayType,f.cash_Item as cashItem");
        sql.append(" from Fex_Foreign_Req f left join bis_Foreign_exc c on f.id = c.cbs_Bill_Id");
        sql.append(" left join sys_country_cust s on s.id=f.payee_Country_Code");
        sql.append(" where 1=1");
        return sql.toString();
    }

    /**
     * 查询外币汇款申请单
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), FexForeignReqVO.class, "f", false);
    }

    /**
     * 根据ID获取外币汇款申请单信息
     *
     * @param id
     * @return
     */
    public FexForeignReqVO getByIdUnion(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("select f.id as id,f.bill_No as billNo,f.corp_Id as corpId,f.remit_Type as remitType,f.pay_Date as payDate,f.cur_Id as curId,f.bank_Type_Id as bankTypeId,");
        sql.append(" f.acc_Id as accId,f.payer_Acc as payerAcc,f.payer_Acc_Name as payerAccName,f.payer_Bank as payerBank,f.payer_Name as payerName,f.payer_Tel as payerTel,");
        sql.append(" f.payer_Name_En as payerNameEn,f.payer_Add_En as payerAddEn,f.payee_Acc as payeeAcc,f.payee_Acc_Name as payeeAccName,f.payee_Add as payeeAdd,f.payee_Swift_Code as payeeSwiftCode,");
        sql.append(" f.payee_Bank as payeeBank,f.payee_Bank_Add as payeeBankAdd,f.payee_Country_Code as payeeCountryCode,f.payee_Agent_Bank as payeeAgentBank,f.payee_Agent_Bank_Add as payeeAgentBankAdd,f.payee_Agent_Acc as payeeAgentAcc,");
        sql.append(" f.pay_Money as payMoney,f.foreign_Exrate as foreignExrate,f.foreign_Money as foreignMoney,f.add_Word as addWord,f.rmk as rmk,f.quick_Sign as quickSign,f.remit_Nature as remitNature,f.remit_Method as remitMethod,");
        sql.append(" f.cost_Sign as costSign,f.pay_Type as payType,f.ship_Date as shipDate,f.is_Bonded as isBonded,f.bus_No as busNo,f.contract_No as contractNo,f.invoice_No as invoiceNo,");
        sql.append(" f.tran_Code1 as tranCode1,f.tran_Amt1 as tranAmt1,f.tran_Cur_Id1 as tranCurId1,f.tran_Add_Word1 as tranAddWord1,f.tran_Code2 as tranCode2,f.tran_Amt2 as tranAmt2,f.tran_Cur_Id2 as tranCurId2,f.tran_Add_Word2 as tranAddWord2,");
        sql.append(" f.status as status,f.attachment_Id as attachmentId,f.create_By as createBy,f.create_Date as createDate,f.update_By as updateBy,f.update_Date as updateDate,");
        sql.append(" t.tran_name as tranName1,c.voucher_Stat as voucherStat,c.id as bisForeignExcId,s.name_Zh as payeeCountryName,f.branch_Code as branchCode,f.branch_Name as branchName,");
        sql.append(" f.acc_type as accType,f.payee_Type as payeeType,f.remit_Route as remitRoute,f.remit_Use as remitUse,f.arrival_Date as arrivalDate,f.declare_No as declareNo,f.acc_Nature as accNature,");
        sql.append(" f.payee_Agent_Swift_Code as payeeAgentSwiftCode,f.is_payed as isPayed,f.bill_from as billFrom,f.erp_id as erpId,f.dif_Bank as difBank,f.bank_Pay_Type as bankPayType,");
        sql.append(" f.cash_Item as cashItem,i.item_name as cashItemName, f.bnf_Relate as bnfRelate");
        sql.append(" from Fex_Foreign_Req f left join bis_Foreign_exc c on f.id = c.cbs_Bill_Id");
        sql.append(" left join fex_tran_info t on t.tran_code = f.tran_code1");
        sql.append(" left join sys_country_cust s on s.id=f.payee_Country_Code");
        sql.append(" left join fc_cash_item i on f.cash_Item=i.id");
        sql.append(" where 1=1");
        sql.append(" and  f.id=:id");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, FexForeignReqVO.class);
    }

    /**
     * 审批列表查询
     *
     * @param dataTablesInput
     * @param businessSql
     * @return
     */
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput, String businessSql) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        String flowStatus = dataTablesInput.getParameter().get("flowStatus");
        //已审批不过滤状态
        if(!IWorkFlowService.PASS.equals(flowStatus)) {
            sql.append(" and  f.status<> ").append(ForeignConstant.STATUE_CHECK);
        }
        sql.append(" and ").append(businessSql);
        return getDynamicBySQL(dataTablesInput, sql.toString(), FexForeignReqVO.class, "f", false);
    }

    /**
     * 获取审批单位集合
     *
     * @param businessSql
     * @return
     */
    public List<String> getCorpIdsByDataTableApprove(String businessSql) {
        StringBuilder sql = new StringBuilder();
        sql.append("select f.corp_Id from Fex_Foreign_Req f where 1=1 ");
        sql.append(" and ").append(businessSql);
        return this.getSession().createSQLQuery(sql.toString()).list();
    }
}
