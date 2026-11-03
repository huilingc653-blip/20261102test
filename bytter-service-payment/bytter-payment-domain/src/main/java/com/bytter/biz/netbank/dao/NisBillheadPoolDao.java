package com.bytter.biz.netbank.dao;
import com.bytter.biz.netbank.entity.NisBillheadPool;
import com.bytter.biz.netbank.vo.NisBillheadPoolVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 付款通知表
 * @date 2017-05-23 15:20:33
 */
@Repository
public class NisBillheadPoolDao extends BaseDao<NisBillheadPool, String> {
    /**
     * 付款通知表通用查@param sqlWhere
     *
     * @return
     */
    private String getCommonSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("select n.id as id,n.bill_No as billNo,n.corp_Id as corpId,n.voucher_Type_Id as voucherTypeId,f.voucher_Type as voucherType,f.voucher_Name as voucherName,f.sys_Voucher_Code as sysVoucherCode,");
        sql.append(" f.is_Account as isAccount,f.send_Mode as sendMode,n.acc_Id as accId,n.payer_Acc as payerAcc,n.payer_Acc_Name as payerAccName,n.payer_Bank as payerBank,n.down_Acc_Id as downAccId,");
        sql.append(" n.cur_Id as curId,n.pay_Money as payMoney,n.payee_Acc as payeeAcc,n.payee_Acc_Name as payeeAccName,n.payee_Bank_Code as payeeBankCode,");
        sql.append(" n.payee_Bank as payeeBank,n.prov as prov,n.city as city,n.wish_Payday as wishPayday,n.is_Quick as isQuick,n.is_Forindividual as isForindividual,");
        sql.append(" n.bif_Code as bifCode,n.dif_Bank as difBank,n.area_Sign as areaSign,n.bursar_Bill_No as bursarBillNo,n.purpose as purpose,n.add_Word as addWord,n.order_No as orderNo,n.voucher_No as voucherNo,");
        sql.append(" n.direct_Pay_Flag as directPayFlag,n.ca_Info_Id as caInfoId,n.tally_sign as tallySign,n.tally_Date as tallyDate,n.bill_money as billMoney,");
        sql.append(" n.erp_Id as erpId,t.id as busTypeId,n.bus_Type as busType,t.bus_Type_Name as busTypeName,n.orig_split_id as origSplitId,n.bus_Corp_Id as busCorpId,n.bus_Personnel as busPersonnel,");
        sql.append(" n.bill_From as billFrom,n.split_Id as splitId,n.merge_Id as mergeId,n.status as status,");//c.bank_acc as downAcc,c.acc_name as downAccName,");
        sql.append(" n.create_By as createBy,n.create_Date as createDate,n.update_By as updateBy,n.update_Date as updateDate,n.attachment_Id as attachmentId,n.is_payed as isPayed, ");
        sql.append(" n.is_Advance as isAdvance,n.rmk as rmk,n.work_Capital_Ratio as workCapitalRatio,n.take_Back_Date as takeBackDate,f.settl_Type as settlType,n.is_Open_Bill as isOpenBill,");
        sql.append(" s.bill_No as splitBillNo,f.is_budget as isBudget,n.is_Auto_Pay as isAutoPay,n.policy_Id as policyId,n.BILL_TYPE as billType,n.BILL_FLAG as billFlag,n.SCHEDULING_DATE as schedulingDate,n.SCHEDULING_ELEMENTS_ID as schedulingElementsId,np.name as schedulingElementsName ");
        sql.append(" from NIS_BILLHEAD_POOL n left join Fc_Voucher_Type f on  n.voucher_Type_Id = f.id");
        sql.append(" left join NIS_BILLHEAD_POOL s on n.split_id = s.id");
        sql.append(" left join Nis_Bus_Type t on n.bus_Type = t.id");
        sql.append(" left join NIS_PAY_SCHEDULING_ELEMENTS np on  n.SCHEDULING_ELEMENTS_ID = np.id where 1=1");
        //sql.append(" left join bt_bank_acc c on n.down_Acc_Id = c.id");
        return sql.toString();
    }
    /**
     * 查询付款单
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql, String isMergeAndSplit) {
        StringBuilder sql = new StringBuilder();
        sql.append("select n.id as id,n.bill_No as billNo,n.corp_Id as corpId,n.voucher_Type_Id as voucherTypeId,f.voucher_Type as voucherType,f.voucher_Name as voucherName,f.sys_Voucher_Code as sysVoucherCode,");
        sql.append(" f.is_Account as isAccount,n.acc_Id as accId,n.payer_Acc as payerAcc,n.payer_Acc_Name as payerAccName,n.payer_Bank as payerBank,n.down_Acc_Id as downAccId,");
        sql.append(" n.cur_Id as curId,n.pay_Money as payMoney,n.payee_Acc as payeeAcc,n.payee_Acc_Name as payeeAccName,n.payee_Bank_Code as payeeBankCode,");
        sql.append(" n.payee_Bank as payeeBank,n.prov as prov,n.city as city,n.wish_Payday as wishPayday,n.is_Quick as isQuick,n.is_Forindividual as isForindividual,");
        sql.append(" n.bif_Code as bifCode,n.dif_Bank as difBank,n.area_Sign as areaSign,n.bursar_Bill_No as bursarBillNo,n.purpose as purpose,n.add_Word as addWord,n.order_No as orderNo,n.voucher_No as voucherNo,");
        sql.append(" n.direct_Pay_Flag as directPayFlag,n.ca_Info_Id as caInfoId,n.tally_sign as tallySign,n.tally_Date as tallyDate,n.bill_money as billMoney,");
        sql.append(" n.erp_Id as erpId,t.id as busTypeId,n.bus_Type as busType,t.bus_Type_Name as busTypeName,n.orig_split_id as origSplitId,");
        sql.append(" n.bill_From as billFrom,n.split_Id as splitId,n.merge_Id as mergeId,n.status as status,n.bus_Corp_Id as busCorpId,n.bus_Personnel as busPersonnel,");
        sql.append(" n.create_By as createBy,n.create_Date as createDate,n.update_By as updateBy,n.update_Date as updateDate,n.attachment_Id as attachmentId,n.is_payed as isPayed, ");
        sql.append(" n.is_Advance as isAdvance,n.work_Capital_Ratio as workCapitalRatio,n.take_Back_Date as takeBackDate,f.settl_Type as settlType,n.is_Open_Bill as isOpenBill, ");
        sql.append(" n.SCHEDULING_ELEMENTS_ID as schedulingElementsId, n.SCHEDULING_DATE as schedulingDate,n.NIS_BILLHEAD_ID as nisBillheadId,np.name as schedulingElementsName, ");
        if (CommonConstant.YES.equals(isMergeAndSplit)) {
            sql.append("s.bill_No as splitBillNo,");
        }
        sql.append(" f.is_budget as isBudget,n.is_Auto_Pay as isAutoPay,n.policy_Id as policyId,n.BILL_TYPE as billType,n.BILL_FLAG as billFlag ");
        sql.append(" from NIS_BILLHEAD_POOL n left join Fc_Voucher_Type f on  n.voucher_Type_Id = f.id");
        sql.append(" left join NIS_PAY_SCHEDULING_ELEMENTS np on  n.SCHEDULING_ELEMENTS_ID = np.id");
        if (CommonConstant.YES.equals(isMergeAndSplit)) {
            sql.append(" left join NIS_BILLHEAD_POOL s on n.split_id = s.id");
        }
        sql.append(" left join Nis_Bus_Type t on n.bus_Type = t.id");
        sql.append(" where 1=1");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), NisBillheadPoolVO.class, "n", false);
    }
    /**
     * 更新打印次数
     *
     * @param id
     */
    public void updatePrint(String id) {
        String hql = "update BisBankReceiptDtl set prints = bt_nvl_num(prints, 0) + 1  where id = :id ";
        Map<String, String> values = new HashMap<>();
        values.put("id", id);
        this.batchExecute(hql.toString(), values);
    }
    /**
     * 根据付款ID获取付款信息
     *
     * @param id
     * @return
     */
    public NisBillheadPoolVO getByIdUnion(String id) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and  n.id=:id");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, NisBillheadPoolVO.class);
    }
    /**
     * 根据付款ID获取付款信息(不关联其他表)
     *
     * @param id
     * @return
     */
    public NisBillheadPoolVO getVoById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("select n.id as id,n.bill_No as billNo,n.corp_Id as corpId,n.voucher_Type_Id as voucherTypeId,f.voucher_Type as voucherType,f.voucher_Name as voucherName,f.sys_Voucher_Code as sysVoucherCode,");
        sql.append(" f.is_Account as isAccount,f.send_Mode as sendMode,n.acc_Id as accId,n.payer_Acc as payerAcc,n.payer_Acc_Name as payerAccName,n.payer_Bank as payerBank,n.down_Acc_Id as downAccId,");
        sql.append(" n.cur_Id as curId,n.pay_Money as payMoney,n.payee_Acc as payeeAcc,n.payee_Acc_Name as payeeAccName,n.payee_Bank_Code as payeeBankCode,");
        sql.append(" n.payee_Bank as payeeBank,n.prov as prov,n.city as city,n.wish_Payday as wishPayday,n.is_Quick as isQuick,n.is_Forindividual as isForindividual,");
        sql.append(" n.bif_Code as bifCode,n.dif_Bank as difBank,n.area_Sign as areaSign,n.bursar_Bill_No as bursarBillNo,n.purpose as purpose,n.add_Word as addWord,n.order_No as orderNo,n.voucher_No as voucherNo,");
        sql.append(" n.direct_Pay_Flag as directPayFlag,n.ca_Info_Id as caInfoId,n.tally_sign as tallySign,n.tally_Date as tallyDate,");
        sql.append(" n.erp_Id as erpId,n.bus_Type as busType,n.orig_split_id as origSplitId,n.bill_money as billMoney,");
        sql.append(" n.bill_From as billFrom,n.split_Id as splitId,n.merge_Id as mergeId,n.status as status,n.bus_Corp_Id as busCorpId,n.bus_Personnel as busPersonnel,");
        sql.append(" n.create_By as createBy,n.create_Date as createDate,n.update_By as updateBy,n.update_Date as updateDate,n.attachment_Id as attachmentId,n.is_payed as isPayed, ");
        sql.append(" n.is_Advance as isAdvance,n.work_Capital_Ratio as workCapitalRatio,n.take_Back_Date as takeBackDate,f.settl_Type as settlType,n.is_Open_Bill as isOpenBill,");
        sql.append(" f.is_budget as isBudget,n.is_Auto_Pay as isAutoPay,n.policy_Id as policyId,n.BILL_TYPE as billType,n.BILL_FLAG as billFlag,n.SCHEDULING_ELEMENTS_ID as schedulingElementsId,SCHEDULING_DATE as schedulingDate,n.TIME_STAMP as timeStamp ");
        sql.append(" from NIS_BILLHEAD_POOL n left join Fc_Voucher_Type f on  n.voucher_Type_Id = f.id");
        sql.append(" where n.id=:id");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, NisBillheadPoolVO.class);
    }
    /**
     * 根据外部单号获取付款信息
     *
     * @param erpId
     */
    public List<NisBillheadPoolVO> listByErpId(String erpId) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and  n.erp_Id=:erpId");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("erpId", erpId);
        return listBySQLAliasToBean(sql.toString(), params, NisBillheadPoolVO.class);
    }
    /**
     * 根据外部单号获取付款信息
     *
     * @param erpId
     */
    public NisBillheadPoolVO getByErpId(String erpId) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and  n.erp_Id=:erpId and (n.status = "+ NetbankConstant.STATUE_TEMP+" or n.status = "+ NetbankConstant.STATUE_BACK+") ");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("erpId", erpId);
        List<NisBillheadPoolVO> list = listBySQLAliasToBean(sql.toString(), params, NisBillheadPoolVO.class);
        if(!EmptyUtils.isEmpty(list) && list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }
    /**
     * 根据合并单号获取合并之前的明细单
     *
     * @param mergeId
     */
    public List<NisBillheadPoolVO> listByMergeId(String mergeId) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and  n.merge_id=:mergeId");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("mergeId", mergeId);
        return listBySQLAliasToBean(sql.toString(), params, NisBillheadPoolVO.class);
    }
    /**
     * 审批列表查询
     *
     * @param dataTablesInput
     * @param businessSql
     * @return
     */
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput, String businessSql) {
        StringBuilder sql = new StringBuilder();
        sql.append("select n.id as id,n.bill_No as billNo,n.corp_Id as corpId,n.voucher_Type_Id as voucherTypeId,f.voucher_Type as voucherType,f.voucher_Name as voucherName,f.sys_Voucher_Code as sysVoucherCode,");
        sql.append(" f.is_Account as isAccount,n.acc_Id as accId,n.payer_Acc as payerAcc,n.payer_Acc_Name as payerAccName,n.payer_Bank as payerBank,n.down_Acc_Id as downAccId,");
        sql.append(" n.cur_Id as curId,n.pay_Money as payMoney,n.payee_Acc as payeeAcc,n.payee_Acc_Name as payeeAccName,n.payee_Bank_Code as payeeBankCode,n.bus_Corp_Id as busCorpId,n.bus_Personnel as busPersonnel,");
        sql.append(" n.payee_Bank as payeeBank,n.prov as prov,n.city as city,n.wish_Payday as wishPayday,n.is_Quick as isQuick,n.is_Forindividual as isForindividual,");
        sql.append(" n.bif_Code as bifCode,n.dif_Bank as difBank,n.area_Sign as areaSign,n.bursar_Bill_No as bursarBillNo,n.purpose as purpose,n.add_Word as addWord,n.order_No as orderNo,n.voucher_No as voucherNo,");
        sql.append(" n.direct_Pay_Flag as directPayFlag,n.ca_Info_Id as caInfoId,n.tally_sign as tallySign,n.tally_Date as tallyDate,n.erp_Id as erpId,n.bus_Type as busType,t.bus_Type_Name as busTypeName,");
        sql.append(" n.bill_From as billFrom,n.split_Id as splitId,n.merge_Id as mergeId,n.status as status,n.is_Auto_Pay as isAutoPay,n.bill_money as billMoney,");
        sql.append(" n.create_By as createBy,n.create_Date as createDate,n.update_By as updateBy,n.update_Date as updateDate,n.attachment_Id as attachmentId,n.is_payed as isPayed, ");
        sql.append(" n.is_Advance as isAdvance,n.work_Capital_Ratio as workCapitalRatio,n.take_Back_Date as takeBackDate,f.settl_Type as settlType,n.is_Open_Bill as isOpenBill");
        sql.append(" from NIS_BILLHEAD_POOL n left join Fc_Voucher_Type f on  n.voucher_Type_Id = f.id");
        sql.append(" left join Nis_Bus_Type t on n.bus_Type = t.id where 1=1");
        String flowStatus = dataTablesInput.getParameter().get("flowStatus");
        //已审批不过滤状态
        if(!IWorkFlowService.PASS.equals(flowStatus)){
            sql.append(" and  n.status<> ").append(NetbankConstant.STATUE_CHECK);
        }
        sql.append(" and ").append(businessSql);
        return getDynamicBySQL(dataTablesInput, sql.toString(), NisBillheadPoolVO.class, "n", false);
    }
    /**
     * 根据付款账号获取付款单信息
     *
     * @param blnctrAcc
     * @return
     */
    public List<NisBillheadPool> listByPayerAcc(String blnctrAcc) {
        StringBuilder hql = new StringBuilder(" from NisBillheadPool n where n.payerAcc = :payerAcc and n.status>:statusBegin and n.status<:statusEnd ");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("payerAcc", blnctrAcc);
        params.put("statusBegin", NetbankConstant.STATUE_DEL);
        params.put("statusEnd", NetbankConstant.STATUE_CHECK);
        return this.listByHQLAliasToBean(hql.toString(), params, NisBillheadPool.class);
    }
    /**
     * 根据付款账号和币别获取付款单的付款金额总和
     * @param bankAcc 付款账号
     * @param curId 币别
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    public List<NisBillheadPool> getSum(String bankAcc, String curId, String startTime, String endTime) {
        //付款办理表
        StringBuilder sql = new StringBuilder();
        sql.append("select corp_id,pay_money,cur_id from NIS_BILLHEAD_POOL ");
        sql.append(" WHERE 1=1 ");
        sql.append(" and bt_to_char ( wish_payday , 'YYYY-MM-DD' ) >= '").append(startTime).append("'");
        sql.append(" and bt_to_char ( wish_payday , 'YYYY-MM-DD' ) <= '").append(endTime).append("'");
        sql.append(" and payer_acc = '").append(bankAcc).append("'");
        sql.append(" and status <> ").append(NetbankConstant.STATUE_DEL);
        List<NisBillheadPool> NisBillheadPool1 = this.listBySQLAliasToBean(sql.toString(), null, NisBillheadPool.class);
        //资金调拨表
        StringBuilder sql2 = new StringBuilder();
        sql2.append("select corp_id,money as payMoney,cur_id from NIS_ALLOCATION_REQ ");
        sql2.append(" WHERE 1=1 ");
        sql2.append(" and bt_to_char ( wish_payday , 'YYYY-MM-DD' ) >= '").append(startTime).append("'");
        sql2.append(" and bt_to_char ( wish_payday , 'YYYY-MM-DD' ) <= '").append(endTime).append("'");
        sql2.append(" and payer_acc = '").append(bankAcc).append("'");
        sql2.append(" and status <> ").append(NetbankConstant.STATUE_DEL);
        List<NisBillheadPool> NisBillheadPool2 = this.listBySQLAliasToBean(sql2.toString(), null, NisBillheadPool.class);
        //银行理财
        StringBuilder sql3 = new StringBuilder();
        sql3.append("select corp_id,pay_money,cur_id from CHO_BANK_FINANCE ");
        sql3.append(" WHERE 1=1 ");
        sql3.append(" and bt_to_char ( wish_payday , 'YYYY-MM-DD' ) >= '").append(startTime).append("'");
        sql3.append(" and bt_to_char ( wish_payday , 'YYYY-MM-DD' ) <= '").append(endTime).append("'");
        sql3.append(" and payer_acc = '").append(bankAcc).append("'");
        sql3.append(" and status <> ").append(NetbankConstant.STATUE_DEL);
        List<NisBillheadPool> NisBillheadPool3 = this.listBySQLAliasToBean(sql3.toString(), null, NisBillheadPool.class);
        //外汇
        StringBuilder sql4 = new StringBuilder();
        sql4.append("select corp_id,pay_money,cur_id from Fex_Foreign_Req ");
        sql4.append(" WHERE 1=1 ");
        sql4.append(" and bt_to_char ( PAY_DATE , 'YYYY-MM-DD' ) >= '").append(startTime).append("'");
        sql4.append(" and bt_to_char ( PAY_DATE , 'YYYY-MM-DD' ) <= '").append(endTime).append("'");
        sql4.append(" and payer_acc = '").append(bankAcc).append("'");
        sql4.append(" and status <> ").append(NetbankConstant.STATUE_DEL);
        List<NisBillheadPool> NisBillheadPool4 = this.listBySQLAliasToBean(sql4.toString(), null, NisBillheadPool.class);
        //代发工资
        StringBuilder sql5 = new StringBuilder();
        sql5.append("select  corp_id,wages as payMoney,cur_id from nis_wage ");
        sql5.append(" WHERE 1=1 ");
        sql5.append(" and bt_to_char ( wish_payday , 'YYYY-MM-DD' ) >= '").append(startTime).append("'");
        sql5.append(" and bt_to_char ( wish_payday , 'YYYY-MM-DD' ) <= '").append(endTime).append("'");
        sql5.append(" and payer_acc = '").append(bankAcc).append("'");
        sql5.append(" and status <> ").append(NetbankConstant.STATUE_DEL);
        List<NisBillheadPool> NisBillheadPool5 = this.listBySQLAliasToBean(sql5.toString(), null, NisBillheadPool.class);
        NisBillheadPool1.addAll(NisBillheadPool2);
        NisBillheadPool1.addAll(NisBillheadPool3);
        NisBillheadPool1.addAll(NisBillheadPool4);
        NisBillheadPool1.addAll(NisBillheadPool5);
        return NisBillheadPool1;
    }
    /**
     * 根据单位获取付款单的集合
     * @param corpId 单位
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    public List<NisBillheadPool> getListByCorpAndCurId(String corpId, String startTime, String endTime) {
        //付款办理表
        StringBuilder sql = new StringBuilder();
        sql.append("select corp_id,pay_money,cur_id from NIS_BILLHEAD_POOL ");
        sql.append(" WHERE 1=1 ");
        sql.append(" and bt_to_char ( wish_payday , 'YYYY-MM-DD' ) >= '").append(startTime).append("'");
        sql.append(" and bt_to_char ( wish_payday , 'YYYY-MM-DD' ) <= '").append(endTime).append("'");
        sql.append(" and corp_id = '").append(corpId).append("'");
        sql.append(" and status <> ").append(NetbankConstant.STATUE_DEL);
        List<NisBillheadPool> NisBillheadPool1 = this.listBySQLAliasToBean(sql.toString(), null, NisBillheadPool.class);
        //资金调拨表
        StringBuilder sql2 = new StringBuilder();
        sql2.append("select b.corp_id,money as payMoney,cur_id from NIS_ALLOCATION_REQ a");
        sql2.append(" left join bt_bank_acc b on b.BANK_ACC = a.PAYER_ACC ");
        sql2.append(" WHERE 1=1 ");
        sql2.append(" and bt_to_char ( a.wish_payday , 'YYYY-MM-DD' ) >= '").append(startTime).append("'");
        sql2.append(" and bt_to_char ( a.wish_payday , 'YYYY-MM-DD' ) <= '").append(endTime).append("'");
        sql2.append(" and b.corp_id = '").append(corpId).append("'");
        sql2.append(" and a.status <> ").append(NetbankConstant.STATUE_DEL);
        List<NisBillheadPool> NisBillheadPool2 = this.listBySQLAliasToBean(sql2.toString(), null, NisBillheadPool.class);
        //银行理财
        /*StringBuilder sql3 = new StringBuilder();
        sql3.append("select corp_id,pay_money,cur_id from CHO_BANK_FINANCE ");
        sql3.append(" WHERE 1=1 ");
        sql3.append(" and bt_to_char ( wish_payday , 'YYYY-MM-DD' ) >= '").append(startTime).append("'");
        sql3.append(" and bt_to_char ( wish_payday , 'YYYY-MM-DD' ) <= '").append(endTime).append("'");
        sql3.append(" and corp_id = '").append(corpId).append("'");
        sql3.append(" and status <> ").append(NetbankConstant.STATUE_DEL);
        sql3.append(" and bus_type in ('").append(BankFinanceConstant.BUS_TYPE_C2T).append("',");
        sql3.append(" '").append(BankFinanceConstant.BUS_TYPE_C2N).append("')");
        List<NisBillheadPool> NisBillheadPool3 = this.listBySQLAliasToBean(sql3.toString(), null, NisBillheadPool.class);*/
        //外汇
        StringBuilder sql4 = new StringBuilder();
        sql4.append("select corp_id,pay_money,cur_id from FEX_FOREIGN_REQ ");
        sql4.append(" WHERE 1=1 ");
        sql4.append(" and bt_to_char ( PAY_DATE , 'YYYY-MM-DD' ) >= '").append(startTime).append("'");
        sql4.append(" and bt_to_char ( PAY_DATE , 'YYYY-MM-DD' ) <= '").append(endTime).append("'");
        sql4.append(" and corp_id = '").append(corpId).append("'");
        sql4.append(" and status <> ").append(NetbankConstant.STATUE_DEL);
        List<NisBillheadPool> NisBillheadPool4 = this.listBySQLAliasToBean(sql4.toString(), null, NisBillheadPool.class);
        //代发工资
        StringBuilder sql5 = new StringBuilder();
        sql5.append("select corp_id,wages as payMoney,cur_id from nis_wage ");
        sql5.append(" WHERE 1=1 ");
        sql5.append(" and bt_to_char ( wish_payday , 'YYYY-MM-DD' ) >= '").append(startTime).append("'");
        sql5.append(" and bt_to_char ( wish_payday , 'YYYY-MM-DD' ) <= '").append(endTime).append("'");
        sql5.append(" and corp_id = '").append(corpId).append("'");
        sql5.append(" and status <> ").append(NetbankConstant.STATUE_DEL);
        List<NisBillheadPool> NisBillheadPool5 = this.listBySQLAliasToBean(sql5.toString(), null, NisBillheadPool.class);
        NisBillheadPool1.addAll(NisBillheadPool2);
        //NisBillheadPool1.addAll(NisBillheadPool3);
        NisBillheadPool1.addAll(NisBillheadPool4);
        NisBillheadPool1.addAll(NisBillheadPool5);
        return NisBillheadPool1;
    }
    public List<NisBillheadPool> getMoneyById(String id){
        //付款办理表
        StringBuilder sql = new StringBuilder();
        sql.append("select corp_id,pay_money,cur_id,wish_payday as createDate from NIS_BILLHEAD_POOL ");
        sql.append(" WHERE 1=1 ");
        sql.append(" and id = '").append(id).append("'");
        List<NisBillheadPool> NisBillheadPool1 = this.listBySQLAliasToBean(sql.toString(), null, NisBillheadPool.class);
        //资金调拨表
        StringBuilder sql2 = new StringBuilder();
        sql2.append("select corp_id,money as payMoney,cur_id,wish_payday as createDate from NIS_ALLOCATION_REQ ");
        sql2.append(" WHERE 1=1 ");
        sql2.append(" and id = '").append(id).append("'");
        List<NisBillheadPool> NisBillheadPool2 = this.listBySQLAliasToBean(sql2.toString(), null, NisBillheadPool.class);
        //银行理财
        StringBuilder sql3 = new StringBuilder();
        sql3.append("select corp_id,pay_money,cur_id,wish_payday as createDate from CHO_BANK_FINANCE ");
        sql3.append(" WHERE 1=1 ");
        sql3.append(" and id = '").append(id).append("'");
        List<NisBillheadPool> NisBillheadPool3 = this.listBySQLAliasToBean(sql3.toString(), null, NisBillheadPool.class);
        //外汇
        StringBuilder sql4 = new StringBuilder();
        sql4.append("select corp_id,pay_money,cur_id,PAY_DATE as createDate from FEX_FOREIGN_REQ ");
        sql4.append(" WHERE 1=1 ");
        sql4.append(" and id = '").append(id).append("'");
        List<NisBillheadPool> NisBillheadPool4 = this.listBySQLAliasToBean(sql4.toString(), null, NisBillheadPool.class);
        //代发工资
        StringBuilder sql5 = new StringBuilder();
        sql5.append("select corp_id,wages as payMoney,cur_id,wish_payday as createDate from nis_wage ");
        sql5.append(" WHERE 1=1 ");
        sql5.append(" and id = '").append(id).append("'");
        List<NisBillheadPool> NisBillheadPool5 = this.listBySQLAliasToBean(sql5.toString(), null, NisBillheadPool.class);
        //费用报销
        StringBuilder sql6 = new StringBuilder();
        sql6.append("select b.corp_id,b.pay_money,b.cur_id,b.wish_payday as createDate from NIS_EXPENSE_DETAIL a ");
        sql6.append(" left join NIS_BILLHEAD_POOL b on a.bill_id= b.id where  1=1 ");
        sql6.append(" and a.id = '").append(id).append("'");
        List<NisBillheadPool> NisBillheadPool6 = this.listBySQLAliasToBean(sql6.toString(), null, NisBillheadPool.class);
        NisBillheadPool1.addAll(NisBillheadPool2);
        NisBillheadPool1.addAll(NisBillheadPool3);
        NisBillheadPool1.addAll(NisBillheadPool4);
        NisBillheadPool1.addAll(NisBillheadPool5);
        NisBillheadPool1.addAll(NisBillheadPool6);
        return NisBillheadPool1;
    }
    /**
     * 重复付款校验
     *
     * @param corpId    单位
     * @param payMoney  金额
     * @param payeeAcc  收款账号
     * @param beginDate 开始日期
     * @param endDate   结束日期
     * @param id        单据号（编辑时校验需要）
     * @param tempFlag  true - 查询暂存打回状态， false-查询已提交之后的状态数据
     * @return
     */
    public List<NisBillheadPool> listByRePay(String corpId, BigDecimal payMoney, String payeeAcc, String beginDate, String endDate, String curId, String id, boolean tempFlag) {
        StringBuilder hql = new StringBuilder();
        hql.append(" from NisBillheadPool n where n.corpId = :corpId and n.curId = :curId and n.payMoney = :payMoney and n.payeeAcc = :payeeAcc ");
        hql.append(" and n.createDate >= :beginDate and n.createDate <= :endDate ");
        if (tempFlag) {
            hql.append("and n.status in (").append(NetbankConstant.STATUE_TEMP).append(",").append(NetbankConstant.STATUE_BACK).append(")");
        } else {
            hql.append("and n.status not in (").append(NetbankConstant.STATUE_TEMP).append(",").append(NetbankConstant.STATUE_BACK).append(",").append(NetbankConstant.STATUE_DEL).append(")");
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("corpId", corpId);
        params.put("curId", curId);
        params.put("payMoney", payMoney);
        params.put("payeeAcc", payeeAcc);
        params.put("beginDate", DateUtils.parse(beginDate));
        params.put("endDate", DateUtils.parse(endDate));
        if (!StringUtils.isBlank(id)) {
            hql.append(" and n.id <> :id ");
            params.put("id", id);
        }
        return listByHQLAliasToBean(hql.toString(), params, NisBillheadPool.class);
    }
    /**
     * 根据单号获取付款信息
     *
     * @param billNo
     */
    public NisBillheadPoolVO getByBillNo(String billNo) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and n.bill_no=:billNo");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("billNo", billNo);
        return this.getBySQLAliasToBean(sql.toString(), params, NisBillheadPoolVO.class);
    }
    /**
     * 获取拆分明细
     *
     * @param splitId
     * @return
     */
    public List<NisBillheadPool> listNisBillheadBySplitId(String splitId) {
        return this.listBy("splitId", splitId);
    }
    /**
     * 获取审批单位集合
     *
     * @param businessSql
     * @return
     */
    public List<String> getCorpIdsByDataTableApprove(String businessSql) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct n.corp_Id from NIS_BILLHEAD_POOL n left join Fc_Voucher_Type f on  n.voucher_Type_Id = f.id ");
        sql.append(" left join Nis_Bus_Type t on n.bus_Type = t.id");
        sql.append(" left join bt_bank_acc c on n.down_Acc_Id = c.id where 1=1 ");
        sql.append(" and ").append(businessSql);
        return this.getSession().createSQLQuery(sql.toString()).list();
    }
    /**
     * 外系统根据时间获取付款单
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public List<NisBillheadPoolVO> listByTime(String beginDate, String endDate) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and (n.status = 95 or n.status = 100) and n.create_date >= bt_to_date(:beginDate,'yyyy-MM-dd') and n.create_date <= bt_to_date(:endDate,'yyyy-MM-dd')");
        HashMap<String, Object> params = new HashMap<>();
        params.put("beginDate", beginDate);
        params.put("endDate", endDate);
        return listBySQLAliasToBean(sql.toString(), params, NisBillheadPoolVO.class);
    }
    /**
     * 根据外部单号获取付款单信息
     *
     * @param erpId
     * @return
     */
    public NisBillheadPoolVO getNisBillheadByErpId(String erpId, String billFrom) {
        StringBuilder sql = new StringBuilder();
        sql.append("select n.id as id,n.bill_No as billNo,n.corp_Id as corpId,n.voucher_Type_Id as voucherTypeId,f.voucher_Name as voucherName,f.sys_Voucher_Code as sysVoucherCode,f.is_Account as isAccount,");
        sql.append(" n.acc_Id as accId,n.payer_Acc as payerAcc,n.payer_Acc_Name as payerAccName,n.payer_Bank as payerBank,n.down_Acc_Id as downAccId,");
        sql.append(" n.cur_Id as curId,n.pay_Money as payMoney,n.payee_Acc as payeeAcc,n.payee_Acc_Name as payeeAccName,n.payee_Bank_Code as payeeBankCode,");
        sql.append(" n.payee_Bank as payeeBank,n.prov as prov,n.city as city,n.wish_Payday as wishPayday,n.is_Quick as isQuick,n.is_Forindividual as isForindividual,");
        sql.append(" n.bif_Code as bifCode,n.dif_Bank as difBank,n.area_Sign as areaSign,n.bursar_Bill_No as bursarBillNo,n.purpose as purpose,n.add_Word as addWord,n.order_No as orderNo,n.voucher_No as voucherNo,");
        sql.append(" n.direct_Pay_Flag as directPayFlag,n.ca_Info_Id as caInfoId,n.tally_Date as tallyDate,n.erp_Id as erpId,");
        sql.append(" t.id as busTypeId,n.bus_Type as busType,t.bus_Type_Name as busTypeName,n.orig_split_id as origSplitId,n.bus_Corp_Id as busCorpId,n.bus_Personnel as busPersonnel,");
        sql.append(" n.bill_From as billFrom,n.split_Id as splitId,n.merge_Id as mergeId,n.status as status,c.bank_acc as downAcc,c.acc_name as downAccName,");
        sql.append(" n.create_By as createBy,n.create_Date as createDate,n.update_By as updateBy,n.update_Date as updateDate,n.attachment_Id as attachmentId,n.is_payed as isPayed");
        sql.append(",b.voucher_Stat as voucherStat,b.id as bisExcId,n.bill_money as billMoney, ");
        sql.append(" n.is_Advance as isAdvance,n.work_Capital_Ratio as workCapitalRatio,n.take_Back_Date as takeBackDate,n.is_Open_Bill as isOpenBill");
        sql.append(" from NIS_BILLHEAD_POOL n left join Fc_Voucher_Type f on  n.voucher_Type_Id = f.id");
        sql.append(" left join Nis_Bus_Type t on n.bus_Type = t.id");
        sql.append(" left join Bis_exc b on n.id = b.cbs_bill_id and b.voucher_From = " + BdlinkConstant.FROM_NIS_PAY);
        sql.append(" left join bt_bank_acc c on n.down_Acc_Id = c.id where 1=1");
        sql.append(" and  n.erp_Id=:erpId and n.bill_from = :billFrom ");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("erpId", erpId);
        params.put("billFrom", billFrom);
        return getBySQLAliasToBean(sql.toString(), params, NisBillheadPoolVO.class);
    }
    /**
     * 根据用户流程查询审批付款单据中的账号
     *
     * @param businessSql
     * @return
     */
    public List<NisBillheadPoolVO> getPayerAcc(String businessSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select distinct n.payer_acc as payerAcc from NIS_BILLHEAD_POOL n where 1=1");
        sql.append(" and ").append(businessSql);
        sql.append(" and  n.status<> ").append(NetbankConstant.STATUE_CHECK);
        return listBySQLAliasToBean(sql.toString(), null, NisBillheadPoolVO.class);
    }
    /**
     * 根据单位统计付款信息
     * @param corpId
     * @return
     */
    public int getBillheadCountByCorpId(String corpId){
        String sql="select count(*) from NIS_BILLHEAD_POOL where corp_id=:corpId";
        HashMap<String,Object> params=new HashMap<>();
        params.put("corpId",corpId);
        //0-暂存、11-待审批、-1-审批打回
        sql+=" and status in ('0','11','-1')";
        return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
    }
    /**
     * 带原状态更新付款单
     * @param vo
     * @param oldStatus
     * @return
     */
    public int updateNisBillheadStatus(NisBillheadPoolVO vo, Integer[] oldStatus) {
        StringBuilder hql=new StringBuilder();
        hql.append(" update NisBillheadPool set corpId=:corpId,voucherTypeId=:voucherTypeId,accId=:accId,payerAcc=:payerAcc,payerAccName=:payerAccName,");
        hql.append(" payerBank=:payerBank,downAccId=:downAccId,curId=:curId,payMoney=:payMoney,payeeAcc=:payeeAcc,");
        hql.append(" payeeAccName=:payeeAccName,payeeBankCode=:payeeBankCode,payeeBank=:payeeBank,bankTypeId=:bankTypeId,");
        hql.append(" prov=:prov,city=:city,wishPayday=:wishPayday,isQuick=:isQuick,isForindividual=:isForindividual,bifCode=:bifCode,");
        hql.append(" difBank=:difBank,areaSign=:areaSign,bursarBillNo=:bursarBillNo,purpose=:purpose,addWord=:addWord,directPayFlag=:directPayFlag,");
        hql.append(" caInfoId=:caInfoId,tallyDate=:tallyDate,orderNo=:orderNo,voucherNo=:voucherNo,erpId=:erpId,busType=:busType,");
        hql.append(" billFrom=:billFrom,splitId=:splitId,origSplitId=:origSplitId,mergeId=:mergeId,status=:status,attachmentId=:attachmentId,");
        hql.append(" isPayed=:isPayed,innerSign=:innerSign,tallySign=:tallySign,isAdvance=:isAdvance,workCapitalRatio=:workCapitalRatio,");
        hql.append(" takeBackDate=:takeBackDate,isOpenBill=:isOpenBill,bankSerialNo=:bankSerialNo,isAutoPay=:isAutoPay,policyId=:policyId,nisBillheadId=:nisBillheadId,");
        hql.append(" billType=:billType,billFlag=:billFlag,busCorpId=:busCorpId,busPersonnel=:busPersonnel,timeStamp=:timeStamp,schedulingElementsId=:schedulingElementsId,schedulingDate=:schedulingDate");
        Map<String,Object> params = new HashMap<>();
        params.put("corpId", vo.getCorpId());
        params.put("voucherTypeId", vo.getVoucherTypeId());
        params.put("accId", vo.getAccId());
        params.put("payerAcc", vo.getPayerAcc());
        params.put("payerAccName", vo.getPayerAccName());
        params.put("payerBank", vo.getPayerBank());
        params.put("downAccId", vo.getDownAccId());
        params.put("curId", vo.getCurId());
        params.put("payMoney", vo.getPayMoney());
        params.put("payeeAcc", vo.getPayeeAcc());
        params.put("payeeAccName", vo.getPayeeAccName());
        params.put("payeeBankCode", vo.getPayeeBankCode());
        params.put("payeeBank", vo.getPayeeBank());
        params.put("bankTypeId", vo.getBankTypeId());
        params.put("prov", vo.getProv());
        params.put("city", vo.getCity());
        params.put("wishPayday", vo.getWishPayday());
        params.put("isQuick", vo.getIsQuick());
        params.put("isForindividual", vo.getIsForindividual());
        params.put("bifCode", vo.getBifCode());
        params.put("difBank", vo.getDifBank());
        params.put("areaSign", vo.getAreaSign());
        params.put("bursarBillNo", vo.getBursarBillNo());
        params.put("purpose", vo.getPurpose());
        params.put("addWord", vo.getAddWord());
        params.put("directPayFlag", vo.getDirectPayFlag());
        params.put("caInfoId", vo.getCaInfoId());
        params.put("tallyDate", vo.getTallyDate());
        params.put("orderNo", vo.getOrderNo());
        params.put("voucherNo", vo.getVoucherNo());
        params.put("erpId", vo.getErpId());
        params.put("busType", vo.getBusType());
        params.put("billFrom", vo.getBillFrom());
        params.put("splitId", vo.getSplitId());
        params.put("origSplitId", vo.getOrigSplitId());
        params.put("mergeId", vo.getMergeId());
        params.put("status", vo.getStatus());
        params.put("attachmentId", vo.getAttachmentId());
        params.put("isPayed", vo.getIsPayed());
        params.put("innerSign", vo.getInnerSign());
        params.put("tallySign", vo.getTallySign());
        params.put("isAdvance", vo.getIsAdvance());
        params.put("workCapitalRatio", vo.getWorkCapitalRatio());
        params.put("takeBackDate", vo.getTakeBackDate());
        params.put("isOpenBill", vo.getIsOpenBill());
        params.put("bankSerialNo", vo.getBankSerialNo());
        params.put("isAutoPay", vo.getIsAutoPay());
        params.put("policyId", vo.getPolicyId());
        params.put("billType", vo.getBillType());
        params.put("billFlag", vo.getBillFlag());
        params.put("busCorpId", vo.getBusCorpId());
        params.put("busPersonnel", vo.getBusPersonnel());
        params.put("timeStamp", vo.getTimeStamp());
        params.put("schedulingElementsId", vo.getSchedulingElementsId());
        params.put("schedulingDate", vo.getSchedulingDate());
        params.put("nisBillheadId", vo.getNisBillheadId());
        //考虑历史数据，票据金额为空则默认写入付款金额
        if (vo.getBillMoney() != null) {
            hql.append(",bill_money=:billMoney ");
            params.put("billMoney", vo.getBillMoney());
        }
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
     * 获取ERP待处理单据
     * @return
     */
    public List<NisBillheadPoolVO> listErpTodoBill() {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and n.bill_From=:billFrom and n.status=:status");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("billFrom", NetbankConstant.BILLFROM_6);
        params.put("status", NetbankConstant.STATUE_TEMP);
        return listBySQLAliasToBean(sql.toString(), params, NisBillheadPoolVO.class);
    }
    /**
     * 获取需要转移成付款单的
     * @return
     */
    public List<NisBillheadPoolVO> listPoolTodoBill() {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        String nowDate = DateUtils.format(DateUtils.getDate(),"yyyy-MM-dd");
        sql.append(" and to_char(n.SCHEDULING_DATE,'yyyy-MM-dd') = '"+nowDate+"' and n.status=:status");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("status", NetbankConstant.STATUE_TEMP);
        return listBySQLAliasToBean(sql.toString(), params, NisBillheadPoolVO.class);
    }
}
