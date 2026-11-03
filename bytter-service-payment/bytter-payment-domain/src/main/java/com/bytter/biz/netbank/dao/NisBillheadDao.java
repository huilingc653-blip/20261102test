package com.bytter.biz.netbank.dao;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.bdlink.api.constant.BtBankAccConstant;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.finacial.api.constant.NbconsoleConstant;
import com.bytter.biz.netbank.entity.NisBill;
import com.bytter.biz.netbank.entity.NisBillhead;
import com.bytter.biz.netbank.vo.*;
import com.bytter.biz.payment.api.dto.NisBillBankSerialNoDTO;
import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.biz.payment.api.dto.VNisPayBillDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.workflow.api.IWorkFlowService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class NisBillheadDao extends BaseDao<NisBillhead, String> {
	final Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 付款通知表通用查@param sqlWhere
     *
     * @return
     */
    private String getCommonSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("select n.id as id,n.bill_No as billNo,n.corp_Id as corpId,n.voucher_Type_Id as voucherTypeId,");
        sql.append(" f.voucher_Type as voucherType,f.voucher_Name as voucherName,f.sys_Voucher_Code as sysVoucherCode,");
        sql.append(" f.is_Account as isAccount,f.send_Mode as sendMode,n.acc_Id as accId,n.payer_Acc as payerAcc,");
        sql.append(" n.payer_Acc_Name as payerAccName,n.payer_Bank as payerBank,n.down_Acc_Id as downAccId,");
        sql.append(" n.cur_Id as curId,n.pay_Money as payMoney,n.payee_Acc as payeeAcc,n.payee_Acc_Name as payeeAccName,n.payee_Bank_Code as payeeBankCode,");
        sql.append(" n.payee_Bank as payeeBank,n.prov as prov,n.city as city,n.wish_Payday as wishPayday,n.is_Quick as isQuick,n.is_Forindividual as isForindividual,");
        sql.append(" n.bif_Code as bifCode,n.dif_Bank as difBank,n.area_Sign as areaSign,n.bursar_Bill_No as bursarBillNo,");
        sql.append(" n.purpose as purpose,n.add_Word as addWord,n.order_No as orderNo,n.voucher_No as voucherNo,");
        sql.append(" n.direct_Pay_Flag as directPayFlag,n.ca_Info_Id as caInfoId,n.tally_sign as tallySign,n.tally_Date as tallyDate,n.bill_money as billMoney,");
        sql.append(" n.erp_Id as erpId,t.id as busTypeId,n.bus_Type as busType,t.bus_Type_Name as busTypeName,");
        sql.append(" n.orig_split_id as origSplitId,n.bus_Corp_Id as busCorpId,n.bus_Personnel as busPersonnel,");
        sql.append(" n.bill_From as billFrom,n.split_Id as splitId,n.merge_Id as mergeId,n.status as status,n.LANDING_LIMIT_RESULT as landingLimitResult,");
        sql.append(" n.create_By as createBy,n.create_Date as createDate,n.update_By as updateBy,n.update_Date as updateDate,");
        sql.append(" n.attachment_Id as attachmentId,n.is_payed as isPayed, n.approve_Date as approveDate, ");
        sql.append(" n.is_Advance as isAdvance,n.rmk as rmk,n.work_Capital_Ratio as workCapitalRatio,n.take_Back_Date as takeBackDate,");
        sql.append(" f.settl_Type as settlType,n.is_Open_Bill as isOpenBill,n.NIS_BILLHEADPOOL_ID as nisBillheadPoolId,");
        sql.append(" s.bill_No as splitBillNo,f.is_budget as isBudget,n.is_Auto_Pay as isAutoPay,n.policy_Id as policyId,");
        sql.append(" n.BILL_TYPE as billType,n.BILL_FLAG as billFlag,n.bp_id as bpId,n.SCHEDULING_DATE as schedulingDate,np.name as schedulingElementsName, ");
        // 增加收款人统一社会信用代码字段查询
        sql.append(" n.BL_RCVPYMTPS_UNN_ID_NO as blRcvpymtpsUnnIdNo,n.sharing_date as sharingDate,n.real_payer_acc_id as realPayerAccId, ");
        // 共享业务类型关联
        sql.append(" n.SHARE_BUS_TYPE_ID as shareBusTypeId, nsbt.SHARE_BUS_TYPE as shareBusType, nsbt.SHARE_BUS_TYPE_NAME as shareBusTypeName, ");
        sql.append(" n.SCF_IS_OPENED as scfIsOpened, n.SCF_PAYABLE_OPEN_AMT as scfPayableOpenAmt, n.SCF_TRANSFER_AMT as scfTransferAmt ");
        sql.append(" from Nis_Billhead n left join Fc_Voucher_Type f on  n.voucher_Type_Id = f.id");
        sql.append(" left join Nis_Billhead s on n.split_id = s.id");
        sql.append(" left join Nis_Bus_Type t on n.bus_Type = t.id");
        sql.append(" left join NIS_SHARE_BUS_TYPE nsbt on n.SHARE_BUS_TYPE_ID = nsbt.id");
        sql.append(" left join NIS_PAY_SCHEDULING_ELEMENTS np on  n.SCHEDULING_ELEMENTS_ID = np.id where 1=1 ");
        //sql.append(" left join bt_bank_acc c on n.down_Acc_Id = c.id where 1=1");
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
        sql.append("select b.voucher_Stat as voucherStat,n.id as id,n.bill_No as billNo,n.corp_Id as corpId,n.voucher_Type_Id as voucherTypeId,f.voucher_Type as voucherType,f.voucher_Name as voucherName,f.sys_Voucher_Code as sysVoucherCode,");
        sql.append(" f.is_Account as isAccount,n.acc_Id as accId,n.payer_Acc as payerAcc,n.payer_Acc_Name as payerAccName,n.payer_Bank as payerBank,n.down_Acc_Id as downAccId,");
        sql.append(" n.cur_Id as curId,n.pay_Money as payMoney,n.payee_Acc as payeeAcc,n.payee_Acc_Name as payeeAccName,n.payee_Bank_Code as payeeBankCode,");
        sql.append(" n.payee_Bank as payeeBank,n.prov as prov,n.city as city,n.wish_Payday as wishPayday,n.is_Quick as isQuick,n.is_Forindividual as isForindividual,");
        sql.append(" n.bif_Code as bifCode,n.dif_Bank as difBank,n.area_Sign as areaSign,n.bursar_Bill_No as bursarBillNo,n.purpose as purpose,n.add_Word as addWord,n.order_No as orderNo,n.voucher_No as voucherNo,");
        sql.append(" n.direct_Pay_Flag as directPayFlag,n.ca_Info_Id as caInfoId,n.tally_sign as tallySign,n.tally_Date as tallyDate,n.bill_money as billMoney,");
        sql.append(" n.erp_Id as erpId,t.id as busTypeId,n.bus_Type as busType,t.bus_Type_Name as busTypeName,n.orig_split_id as origSplitId, n.approve_Date as approveDate,");
        sql.append(" n.bill_From as billFrom,n.split_Id as splitId,n.merge_Id as mergeId,n.status as status,n.LANDING_LIMIT_RESULT as landingLimitResult,n.bus_Corp_Id as busCorpId,n.bus_Personnel as busPersonnel,");
        sql.append(" n.create_By as createBy,n.create_Date as createDate,n.update_By as updateBy,n.update_Date as updateDate,n.attachment_Id as attachmentId,n.is_payed as isPayed, ");
        sql.append(" n.is_Advance as isAdvance,n.work_Capital_Ratio as workCapitalRatio,n.take_Back_Date as takeBackDate,f.settl_Type as settlType,n.is_Open_Bill as isOpenBill,");
        sql.append(" e.bus_bill_type as busBillType,n.op_code, ");
        // 共享业务类型关联
        sql.append(" n.SHARE_BUS_TYPE_ID as shareBusTypeId, nsbt.SHARE_BUS_TYPE as shareBusType, nsbt.SHARE_BUS_TYPE_NAME as shareBusTypeName, ");
        if (CommonConstant.YES.equals(isMergeAndSplit)) {
            sql.append("s.bill_No as splitBillNo,");
        }
        // 增加收款人统一社会信用代码字段查询
        sql.append(" n.BL_RCVPYMTPS_UNN_ID_NO as blRcvpymtpsUnnIdNo, ");
        sql.append(" f.is_budget as isBudget,n.is_Auto_Pay as isAutoPay,n.policy_Id as policyId,n.BILL_TYPE as billType,n.BILL_FLAG as billFlag,n.SCHEDULING_DATE as schedulingDate,np.name as schedulingElementsName,n.NIS_BILLHEADPOOL_ID as nisBillheadPoolId ");
        sql.append(" from Nis_Billhead n left join Fc_Voucher_Type f on  n.voucher_Type_Id = f.id");
        sql.append(" left join erp_payinfo e on e.id = n.erp_id ");
        if (CommonConstant.YES.equals(isMergeAndSplit)) {
            sql.append(" left join Nis_Billhead s on n.split_id = s.id");
        }
        sql.append(" left join Nis_Bus_Type t on n.bus_Type = t.id");
        sql.append(" left join NIS_SHARE_BUS_TYPE nsbt on n.SHARE_BUS_TYPE_ID = nsbt.id");
        sql.append(" left join NIS_PAY_SCHEDULING_ELEMENTS np on  n.SCHEDULING_ELEMENTS_ID = np.id");
        sql.append(" left join Bis_exc b on n.id = b.cbs_bill_id and b.voucher_From = '" + BdlinkConstant.FROM_NIS_PAY+"'");
        sql.append(" where 1=1");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        //业务类别查询条件变为多选了
        String eqs_busType = (String) dataTablesInput.getFormdata().get("eqs_busType");
        if(EmptyUtils.isNotEmpty(eqs_busType)){
            String[] eqs_busTypes = eqs_busType.split(",");
            sql.append(" and  "+SqlUtil.getInSql(eqs_busTypes,"n.bus_Type",true));
            dataTablesInput.getFormdata().remove("eqs_busType");
        }

        return getDynamicBySQL(dataTablesInput, sql.toString(), NisBillheadVO.class, "n", false);
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
        this.batchExecute(hql, values);
    }

    /**
     * 根据付款ID获取付款信息
     *
     * @param id
     * @return
     */
    public NisBillheadVO getByIdUnion(String id) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(this.getCommonSql() + " and  n.id=:id", params, NisBillheadVO.class);
    }

    /**
     * 根据付款ID获取付款信息(不关联其他表)
     *
     * @param id
     * @return
     */
    public NisBillheadVO getVoById(String id) {
        String sql = "select n.id as id,n.bill_No as billNo,n.corp_Id as corpId,n.voucher_Type_Id as voucherTypeId,f.voucher_Type as voucherType,f.voucher_Name as voucherName,f.sys_Voucher_Code as sysVoucherCode," +
                " f.is_Account as isAccount,f.send_Mode as sendMode,n.acc_Id as accId,n.payer_Acc as payerAcc,n.payer_Acc_Name as payerAccName,n.payer_Bank as payerBank,n.down_Acc_Id as downAccId," +
                " n.cur_Id as curId,n.pay_Money as payMoney,n.payee_Acc as payeeAcc,n.payee_Acc_Name as payeeAccName,n.payee_Bank_Code as payeeBankCode," +
                " n.payee_Bank as payeeBank,n.prov as prov,n.city as city,n.wish_Payday as wishPayday,n.is_Quick as isQuick,n.is_Forindividual as isForindividual," +
                " n.bif_Code as bifCode,n.dif_Bank as difBank,n.area_Sign as areaSign,n.bursar_Bill_No as bursarBillNo,n.purpose as purpose,n.add_Word as addWord,n.order_No as orderNo,n.voucher_No as voucherNo," +
                " n.direct_Pay_Flag as directPayFlag,n.ca_Info_Id as caInfoId,n.tally_sign as tallySign,n.tally_Date as tallyDate," +
                " n.erp_Id as erpId,n.bus_Type as busType,n.orig_split_id as origSplitId,n.bill_money as billMoney, n.approve_Date as approveDate," +
                " n.bill_From as billFrom,n.split_Id as splitId,n.merge_Id as mergeId,n.status as status,n.LANDING_LIMIT_RESULT as landingLimitResult,n.bus_Corp_Id as busCorpId,n.bus_Personnel as busPersonnel," +
                " n.create_By as createBy,n.create_Date as createDate,n.update_By as updateBy,n.update_Date as updateDate,n.attachment_Id as attachmentId,n.is_payed as isPayed, " +
                " n.is_Advance as isAdvance,n.work_Capital_Ratio as workCapitalRatio,n.take_Back_Date as takeBackDate,f.settl_Type as settlType,n.is_Open_Bill as isOpenBill," +
                " f.is_budget as isBudget,n.is_Auto_Pay as isAutoPay,n.policy_Id as policyId,n.BILL_TYPE as billType,n.BILL_FLAG as billFlag, " +
                " n.SCF_IS_OPENED as scfIsOpened, n.SCF_PAYABLE_OPEN_AMT as scfPayableOpenAmt, n.SCF_TRANSFER_AMT as scfTransferAmt " +
                " from Nis_Billhead n left join Fc_Voucher_Type f on  n.voucher_Type_Id = f.id" +
                " where n.id=:id";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql, params, NisBillheadVO.class);
    }

    /**
     * 根据外部单号获取付款信息
     *
     * @param erpId
     */
    public List<NisBillheadVO> listByErpId(String erpId) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("erpId", erpId);
        return listBySQLAliasToBean(this.getCommonSql() + " and  n.erp_Id=:erpId", params, NisBillheadVO.class);
    }

    /**
     * 根据共享结算单号list获取付款信息
     *
     * @param bursarBillNoList
     */
    public List<NisBillheadVO> listByBursarBillNos(List<String> bursarBillNoList) {
        if(CollectionUtil.isEmpty(bursarBillNoList)){
            return CollectionUtil.newArrayList();
        }
        HashMap<String, Object> params = new HashMap<>();
        StringBuilder sql=new StringBuilder(2000);
        sql.append(this.getCommonSql());
        sql.append(" and ").append(SqlUtil.getInSqlByParams(bursarBillNoList,"n.bursar_bill_no","bursarBillNoList",params));
        return listBySQLAliasToBean(sql.toString(), params, NisBillheadVO.class);
    }

    /**
     * 根据外部单号获取付款信息
     *
     * @param erpId
     */
    public NisBillheadVO getByErpId(String erpId) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("erpId", erpId);
        List<NisBillheadVO> list = listBySQLAliasToBean(this.getCommonSql() + " and  n.erp_Id=:erpId and (n.status = " + NetbankConstant.STATUE_TEMP + " or n.status = " + NetbankConstant.STATUE_BACK + ") ", params, NisBillheadVO.class);
        if (!EmptyUtils.isEmpty(list) && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 根据合并单号获取合并之前的明细单
     *
     * @param mergeId
     */
    public List<NisBillheadVO> listByMergeId(String mergeId) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("mergeId", mergeId);
        return listBySQLAliasToBean(this.getCommonSql() + " and  n.merge_id=:mergeId", params, NisBillheadVO.class);
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
        sql.append(" n.is_Advance as isAdvance,n.work_Capital_Ratio as workCapitalRatio,n.take_Back_Date as takeBackDate,f.settl_Type as settlType,n.is_Open_Bill as isOpenBill, n.approve_Date as approveDate");
        sql.append(" from Nis_Billhead n left join Fc_Voucher_Type f on  n.voucher_Type_Id = f.id");
        sql.append(" left join Nis_Bus_Type t on n.bus_Type = t.id where 1=1");
        String flowStatus = dataTablesInput.getParameter().get("flowStatus");
        //已审批不过滤状态
        if (!IWorkFlowService.PASS.equals(flowStatus)) {
            sql.append(" and  n.status<> ").append(NetbankConstant.STATUE_CHECK);
        }
        if (!EmptyUtils.isEmpty(businessSql)) {
            sql.append(businessSql);
        }
        //审批时间
        String gedApproveDate = (String) dataTablesInput.getFormdata().get("ged_approveDate");
        String ledApproveDate = (String) dataTablesInput.getFormdata().get("led_approveDate");
        if (StringUtils.isNotEmpty(gedApproveDate)) {
            gedApproveDate = gedApproveDate + " 00:00:00";
            dataTablesInput.getFormdata().put("ged_approveDate", gedApproveDate);
        }
        if (StringUtils.isNotEmpty(ledApproveDate)) {
            ledApproveDate = ledApproveDate + " 23:59:59";
            dataTablesInput.getFormdata().put("led_approveDate", ledApproveDate);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), NisBillheadVO.class, "n", false);
    }

    /**
     * 根据付款账号获取付款单信息
     *
     * @param blnctrAcc
     * @return
     */
    public List<NisBillhead> listByPayerAcc(String blnctrAcc) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("payerAcc", blnctrAcc);
        params.put("statusBegin", NetbankConstant.STATUE_DEL);
        params.put("statusEnd", NetbankConstant.STATUE_CHECK);
        return this.listByHQLAliasToBean(" from NisBillhead n where n.payerAcc = :payerAcc and n.status>:statusBegin and n.status<:statusEnd ", params, NisBillhead.class);
    }

    /**
     * 用于 查询符合 资金需求清单 条件的数据
     * @return //TODO
     */
    public List<NisBillheadVO> listForFundingRequirements(){
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.* from nis_billhead a   ");
        sql.append(" left join fc_voucher_type b on a.voucher_type_id = b.ID ");
        sql.append(" left join nis_demand d on a.id = d.pay_bill_id ");
        sql.append(" where a.status = :status and a.IS_PAYED = :isPayed  and b.sys_voucher_code = :sysVoucherCode ");
        sql.append(" and a.wish_payday <= :wishPayday ");
        sql.append(" and d.pay_bill_id is null ");//没有生成需求单的
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("status", NetbankConstant.STATUE_TEMP);
        params.put("isPayed", NetbankConstant.ISPAYED_0);
        params.put("sysVoucherCode", "34");
        params.put("wishPayday", DateUtils.getIntegralEndTime(new Date()));
        List<NisBillheadVO> list=this.listBySQLAliasToBean(sql.toString(), params, NisBillheadVO.class);
        return list;
    }

    /**
     * 用于 查询符合 资金需求清单 以前生成过需求单的
     * @return //TODO
     */
    public List<NisBillheadVO> listForFundingRequirementsToHasDemand(){
        StringBuilder sql = new StringBuilder();
        sql.append(" select distinct a.* from nis_billhead a    ");
        sql.append(" left join fc_voucher_type b on a.voucher_type_id = b.ID  ");
        sql.append(" left join nis_demand d on a.id = d.pay_bill_id  ");
        sql.append(" where a.status = :status and a.IS_PAYED = :isPayed  and b.sys_voucher_code = :sysVoucherCode ");
        sql.append("  and d.pay_bill_id is not null and d.DEMAND_DATE < :date ");// 之前生成过但是没付的 今天都要再生成一次
        sql.append("  and a.id not in (select a.id from nis_billhead a    ");
                            sql.append(" left join fc_voucher_type b on a.voucher_type_id = b.ID  ");
                            sql.append(" left join nis_demand d on a.id = d.pay_bill_id  ");
                            sql.append(" where a.status = :status and a.IS_PAYED = :isPayed  and b.sys_voucher_code = :sysVoucherCode ");
                            sql.append("  and d.pay_bill_id is not null and d.DEMAND_DATE >= :date) ");// 今天生成过的都不会再生成
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("status", NetbankConstant.STATUE_TEMP);
        params.put("isPayed", NetbankConstant.ISPAYED_0);
        params.put("sysVoucherCode", "34");
        params.put("date", DateUtils.getDateYMD());
        return this.listBySQLAliasToBean(sql.toString(), params, NisBillheadVO.class);
    }

    /**
     * 根据付款账号和币别获取付款单的付款金额总和
     *
     * @param bankAcc   付款账号
     * @param curId     币别
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public List<NisBillhead> getSum(String bankAcc, String curId, String startTime, String endTime) {
        //付款办理表
        String sql = "select corp_id,pay_money,cur_id from NIS_BILLHEAD " +
                " WHERE 1=1 " +
                " and bt_to_char ( wish_payday , 'YYYY-MM-DD' ) >= '" + startTime + "'" +
                " and bt_to_char ( wish_payday , 'YYYY-MM-DD' ) <= '" + endTime + "'" +
                " and payer_acc = '" + bankAcc + "'" +
                " and status <> " + NetbankConstant.STATUE_DEL;
        List<NisBillhead> nisBillhead1 = this.listBySQLAliasToBean(sql, null, NisBillhead.class);
        //资金调拨表
        String sql2 = "select corp_id,money as payMoney,cur_id from NIS_ALLOCATION_REQ " +
                " WHERE 1=1 " +
                " and bt_to_char ( wish_payday , 'YYYY-MM-DD' ) >= '" + startTime + "'" +
                " and bt_to_char ( wish_payday , 'YYYY-MM-DD' ) <= '" + endTime + "'" +
                " and payer_acc = '" + bankAcc + "'" +
                " and status <> " + NetbankConstant.STATUE_DEL;
        List<NisBillhead> nisBillhead2 = this.listBySQLAliasToBean(sql2, null, NisBillhead.class);
        //银行理财
        String sql3 = "select corp_id,pay_money,cur_id from CHO_BANK_FINANCE " +
                " WHERE 1=1 " +
                " and bt_to_char ( wish_payday , 'YYYY-MM-DD' ) >= '" + startTime + "'" +
                " and bt_to_char ( wish_payday , 'YYYY-MM-DD' ) <= '" + endTime + "'" +
                " and payer_acc = '" + bankAcc + "'" +
                " and status <> " + NetbankConstant.STATUE_DEL;
        List<NisBillhead> nisBillhead3 = this.listBySQLAliasToBean(sql3, null, NisBillhead.class);
        //外汇
        String sql4 = "select corp_id,pay_money,cur_id from Fex_Foreign_Req " +
                " WHERE 1=1 " +
                " and bt_to_char ( PAY_DATE , 'YYYY-MM-DD' ) >= '" + startTime + "'" +
                " and bt_to_char ( PAY_DATE , 'YYYY-MM-DD' ) <= '" + endTime + "'" +
                " and payer_acc = '" + bankAcc + "'" +
                " and status <> " + NetbankConstant.STATUE_DEL;
        List<NisBillhead> nisBillhead4 = this.listBySQLAliasToBean(sql4, null, NisBillhead.class);
        //代发工资
        String sql5 = "select  corp_id,wages as payMoney,cur_id from nis_wage " +
                " WHERE 1=1 " +
                " and bt_to_char ( wish_payday , 'YYYY-MM-DD' ) >= '" + startTime + "'" +
                " and bt_to_char ( wish_payday , 'YYYY-MM-DD' ) <= '" + endTime + "'" +
                " and payer_acc = '" + bankAcc + "'" +
                " and status <> " + NetbankConstant.STATUE_DEL;
        List<NisBillhead> nisBillhead5 = this.listBySQLAliasToBean(sql5, null, NisBillhead.class);
        nisBillhead1.addAll(nisBillhead2);
        nisBillhead1.addAll(nisBillhead3);
        nisBillhead1.addAll(nisBillhead4);
        nisBillhead1.addAll(nisBillhead5);
        return nisBillhead1;
    }

    /**
     * 根据单位获取付款单的集合
     *
     * @param corpId    单位
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    public List<NisBillhead> getListByCorpAndCurId(String corpId, String startTime, String endTime) {
        //付款办理表
        String sql = "select corp_id,pay_money,cur_id from NIS_BILLHEAD " +
                " WHERE 1=1 " +
                " and bt_to_char ( wish_payday , 'YYYY-MM-DD' ) >= '" + startTime + "'" +
                " and bt_to_char ( wish_payday , 'YYYY-MM-DD' ) <= '" + endTime + "'" +
                " and corp_id = '" + corpId + "'" +
                " and status <> " + NetbankConstant.STATUE_DEL;
        List<NisBillhead> nisBillhead1 = this.listBySQLAliasToBean(sql, null, NisBillhead.class);
        //资金调拨表
//        String sql2 = "select b.corp_id,money as payMoney,cur_id from NIS_ALLOCATION_REQ a" +
//                " left join bt_bank_acc b on b.BANK_ACC = a.PAYER_ACC " +
        String sql2 = "select money as payMoney,cur_id from NIS_ALLOCATION_REQ a" +
                " WHERE 1=1 " +
                " and bt_to_char ( a.wish_payday , 'YYYY-MM-DD' ) >= '" + startTime + "'" +
                " and bt_to_char ( a.wish_payday , 'YYYY-MM-DD' ) <= '" + endTime + "'" +
                " and b.corp_id = '" + corpId + "'" +
                " and a.status <> " + NetbankConstant.STATUE_DEL;
        List<NisBillhead> nisBillhead2 = this.listBySQLAliasToBean(sql2, null, NisBillhead.class);
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
        List<NisBillhead> nisBillhead3 = this.listBySQLAliasToBean(sql3.toString(), null, NisBillhead.class);*/
        //外汇
        String sql4 = "select corp_id,pay_money,cur_id from FEX_FOREIGN_REQ " +
                " WHERE 1=1 " +
                " and bt_to_char ( PAY_DATE , 'YYYY-MM-DD' ) >= '" + startTime + "'" +
                " and bt_to_char ( PAY_DATE , 'YYYY-MM-DD' ) <= '" + endTime + "'" +
                " and corp_id = '" + corpId + "'" +
                " and status <> " + NetbankConstant.STATUE_DEL;
        List<NisBillhead> nisBillhead4 = this.listBySQLAliasToBean(sql4, null, NisBillhead.class);
        //代发工资
        String sql5 = "select corp_id,wages as payMoney,cur_id from nis_wage " +
                " WHERE 1=1 " +
                " and bt_to_char ( wish_payday , 'YYYY-MM-DD' ) >= '" + startTime + "'" +
                " and bt_to_char ( wish_payday , 'YYYY-MM-DD' ) <= '" + endTime + "'" +
                " and corp_id = '" + corpId + "'" +
                " and status <> " + NetbankConstant.STATUE_DEL;
        List<NisBillhead> nisBillhead5 = this.listBySQLAliasToBean(sql5, null, NisBillhead.class);
        nisBillhead1.addAll(nisBillhead2);
        /*nisBillhead1.addAll(nisBillhead3);*/
        nisBillhead1.addAll(nisBillhead4);
        nisBillhead1.addAll(nisBillhead5);
        return nisBillhead1;
    }

    public List<NisBillhead> getMoneyById(String id) {
        //付款办理表
        String sql = "select corp_id,pay_money,cur_id,wish_payday as createDate from NIS_BILLHEAD " +
                " WHERE 1=1 " +
                " and id = '" + id + "'";
        List<NisBillhead> nisBillhead1 = this.listBySQLAliasToBean(sql, null, NisBillhead.class);
        //资金调拨表
        String sql2 = "select corp_id,money as payMoney,cur_id,wish_payday as createDate from NIS_ALLOCATION_REQ " +
                " WHERE 1=1 " +
                " and id = '" + id + "'";
        List<NisBillhead> nisBillhead2 = this.listBySQLAliasToBean(sql2, null, NisBillhead.class);
//        //银行理财
//        String sql3 = "select corp_id,pay_money,cur_id,wish_payday as createDate from CHO_BANK_FINANCE " +
//                " WHERE 1=1 " +
//                " and id = '" + id + "'";
//        List<NisBillhead> nisBillhead3 = this.listBySQLAliasToBean(sql3, null, NisBillhead.class);
        //外汇
        String sql4 = "select corp_id,pay_money,cur_id,PAY_DATE as createDate from FEX_FOREIGN_REQ " +
                " WHERE 1=1 " +
                " and id = '" + id + "'";
        List<NisBillhead> nisBillhead4 = this.listBySQLAliasToBean(sql4, null, NisBillhead.class);
        //代发工资
        String sql5 = "select corp_id,wages as payMoney,cur_id,wish_payday as createDate from nis_wage " +
                " WHERE 1=1 " +
                " and id = '" + id + "'";
        List<NisBillhead> nisBillhead5 = this.listBySQLAliasToBean(sql5, null, NisBillhead.class);
        //费用报销
        String sql6 = "select b.corp_id,b.pay_money,b.cur_id,b.wish_payday as createDate from NIS_EXPENSE_DETAIL a " +
                " left join NIS_BILLHEAD b on a.bill_id= b.id where  1=1 " +
                " and a.id = '" + id + "'";
        List<NisBillhead> nisBillhead6 = this.listBySQLAliasToBean(sql6, null, NisBillhead.class);
        nisBillhead1.addAll(nisBillhead2);
//        nisBillhead1.addAll(nisBillhead3);
        nisBillhead1.addAll(nisBillhead4);
        nisBillhead1.addAll(nisBillhead5);
        nisBillhead1.addAll(nisBillhead6);
        return nisBillhead1;
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
    public List<NisBillhead> listByRePay(String corpId, BigDecimal payMoney, String payeeAcc, String beginDate, String endDate, String curId, String id, boolean tempFlag) {
        StringBuilder hql = new StringBuilder();
        hql.append(" from NisBillhead n where n.corpId = :corpId and n.curId = :curId and n.payMoney = :payMoney and n.payeeAcc = :payeeAcc ");
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
        return listByHQLAliasToBean(hql.toString(), params, NisBillhead.class);
    }

    /**
     * 先付款后记账待记账
     * 冗余  bt_bank_acc 表  //TODO 数据同步操作
     * c.bank_acc as downAcc,  `BANK_ACC` varchar(60) COLLATE utf8_bin DEFAULT NULL,
     * c.acc_name `ACC_NAME` varchar(140) COLLATE utf8_bin DEFAULT NULL COMMENT '账户名称',
     * 这两个字段 到 Nis_Billhead
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnionByPayAccount(DataTablesInput dataTablesInput, ICurrentUser user, String corpAuthSql) {
        Object opType = dataTablesInput.getFormdata().get("opType");
        StringBuilder sql = new StringBuilder();
        sql.append("select * from (select nb.id as id,nb.bill_No ,nb.corp_Id ,nb.voucher_Type_Id ,f.voucher_Name as voucherName,f.sys_Voucher_Code as sysVoucherCode,f.is_Account as isAccount,");
        sql.append(" nb.acc_Id as accId,nb.payer_Acc as payerAcc,nb.payer_Acc_Name as payerAccName,nb.payer_Bank as payerBank,nb.down_Acc_Id as downAccId,");
        sql.append(" nb.cur_Id as curId,nb.pay_Money ,nb.payee_Acc ,nb.payee_Acc_Name ,nb.payee_Bank_Code as payeeBankCode,nb.tally_Sign as tally_sign,nb.tally_code, ");
        sql.append(" nb.payee_Bank as payeeBank,nb.prov as prov,nb.city as city,nb.wish_Payday ,nb.is_Quick as isQuick,nb.is_Forindividual as isForindividual,");
        sql.append(" nb.bif_Code as bifCode,nb.dif_Bank as difBank,nb.area_Sign as areaSign,nb.bursar_Bill_No as bursarBillNo,nb.purpose as purpose,nb.add_Word as addWord,nb.order_No as orderNo,nb.voucher_No as voucherNo,");
        sql.append(" nb.direct_Pay_Flag as directPayFlag,nb.ca_Info_Id as caInfoId,nb.tally_Date as tallyDate,nb.erp_Id as erpId,");
        sql.append(" t.id as busTypeId,nb.bus_Type as busType,t.bus_Type_Name as busTypeName,nb.orig_split_id as origSplitId,nb.bill_money as billMoney,");
        sql.append(" nb.bill_From ,nb.split_Id as splitId,nb.merge_Id as mergeId,nb.status as status,nb.bank_acc as downAcc,nb.acc_name as downAccName,nb.is_payed as isPayed,");
        sql.append(" nb.create_By as createBy,nb.create_Date as createDate,nb.update_By as updateBy,nb.update_Date as updateDate,nb.attachment_Id as attachmentId,b.voucher_Stat as voucherStat,b.id as bisExcId,");
        sql.append(" nb.is_Advance as isAdvance,nb.work_Capital_Ratio as workCapitalRatio,nb.take_Back_Date as takeBackDate,nb.is_Open_Bill as isOpenBill,");
        sql.append(" b.trans_time transTime");//        除现付内转以外取指令表交易日期
        sql.append(" from Nis_Billhead nb left join Fc_Voucher_Type f on  nb.voucher_Type_Id = f.id");
        sql.append(" left join Nis_Bus_Type t on nb.bus_Type = t.id");
        sql.append(" left join Bis_exc b on nb.id = b.cbs_bill_id and b.voucher_From = " + BdlinkConstant.FROM_NIS_PAY);
//        sql.append(" left join bt_bank_acc c on nb.down_Acc_Id = c.id ");
        sql.append(" where 1=1");
        sql.append(" and (b.voucher_stat = '" + BdlinkConstant.SUCCESS + "' or nb.is_payed = '").append(NetbankConstant.ISPAYED_1).append("' ) ");
        //付款成功的才进行记账
        sql.append(" and nb.status = " + NetbankConstant.STATUE_CHECK + " and f.is_account = '" + CommonConstant.YES + "' ");
        sql.append(" and f.sys_voucher_code not in( ").append(NetbankConstant.PAY_TYPE_04).append(",").append(NetbankConstant.PAY_TYPE_05).append(")");
        sql.append(" union ");
        sql.append(" select nb.id as id,nb.bill_No ,nb.corp_Id ,nb.voucher_Type_Id ,f.voucher_Name as voucherName,f.sys_Voucher_Code as sysVoucherCode,f.is_Account as isAccount,");
        sql.append(" nb.acc_Id as accId,nb.payer_Acc as payerAcc,nb.payer_Acc_Name as payerAccName,nb.payer_Bank as payerBank,nb.down_Acc_Id as downAccId,");
        sql.append(" nb.cur_Id as curId,nb.pay_Money ,nb.payee_Acc ,nb.payee_Acc_Name ,nb.payee_Bank_Code as payeeBankCode,nb.tally_Sign as tally_sign ,nb.tally_code, ");
        sql.append(" nb.payee_Bank as payeeBank,nb.prov as prov,nb.city as city,nb.wish_Payday ,nb.is_Quick as isQuick,nb.is_Forindividual as isForindividual,");
        sql.append(" nb.bif_Code as bifCode,nb.dif_Bank as difBank,nb.area_Sign as areaSign,nb.bursar_Bill_No as bursarBillNo,nb.purpose as purpose,nb.add_Word as addWord,nb.order_No as orderNo,nb.voucher_No as voucherNo,");
        sql.append(" nb.direct_Pay_Flag as directPayFlag,nb.ca_Info_Id as caInfoId,nb.tally_Date as tallyDate,nb.erp_Id as erpId,");
        sql.append(" t.id as busTypeId,nb.bus_Type as busType,t.bus_Type_Name as busTypeName,nb.orig_split_id as origSplitId,nb.bill_money as billMoney,");
        sql.append(" nb.bill_From ,nb.split_Id as splitId,nb.merge_Id as mergeId,nb.status as status,nb.bank_acc as downAcc,nb.acc_name as downAccName,nb.is_payed as isPayed,");
        sql.append(" nb.create_By as createBy,nb.create_Date as createDate,nb.update_By as updateBy,nb.update_Date as updateDate,nb.attachment_Id as attachmentId,'' as voucherStat,'' as bisExcId,");
        sql.append(" nb.is_Advance as isAdvance,nb.work_Capital_Ratio as workCapitalRatio,nb.take_Back_Date as takeBackDate,nb.is_Open_Bill as isOpenBill,");
        sql.append(" nb.wish_payday transTime");        //现付，内转取期望支付日期为交易日期
        sql.append(" from Nis_Billhead nb left join Fc_Voucher_Type f on  nb.voucher_Type_Id = f.id");
        sql.append(" left join Nis_Bus_Type t on nb.bus_Type = t.id");
//        sql.append(" left join bt_bank_acc c on nb.down_Acc_Id = c.id ");
        sql.append(" where 1=1");
        //付款成功的才进行记账
        sql.append(" and nb.status = " + NetbankConstant.STATUE_CHECK + " and f.is_account = '" + CommonConstant.YES + "' ");
        sql.append(" and f.sys_voucher_code  in( ").append(NetbankConstant.PAY_TYPE_04).append(",").append(NetbankConstant.PAY_TYPE_05).append(")");
        sql.append(" ) n where 1 = 1 ");
        if ("Init".equals(opType)) {
            sql.append(" and (n.tally_Sign ='' or n.tally_Sign ='0' or  n.tally_sign is null) ");
        } else if ("Check".equals(opType)) {
            sql.append(" and (n.tally_Sign = '3' ) and n.tally_code != '").append(user.getId()).append("'");
        } else if ("Edit".equals(opType)) {
            sql.append(" and (n.tally_Sign in(5,4,3)) and n.tally_code = '").append(user.getId()).append("'");
        }
        if (StringUtils.isNoneBlank(corpAuthSql)) {
            sql.append(corpAuthSql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), NisBillheadVO.class, "n", false);
    }

    /**
     * 先记账后付款待记账
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnionByaccountPay(DataTablesInput dataTablesInput, ICurrentUser user, String corpAuthSql) {
        Object opType = dataTablesInput.getFormdata().get("opType");
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and n.status = " + NetbankConstant.STATUE_CHECK + " and f.is_account = '1' ");
        if ("Init".equals(opType)) {
            sql.append(" and (n.tally_Sign ='' or n.tally_Sign ='0' or  n.tally_sign is null) ");
        } else if ("Check".equals(opType)) {
            sql.append(" and (n.tally_Sign = '3' ) and n.tally_code != '").append(user.getId()).append("'");
        } else if ("Edit".equals(opType)) {
            sql.append(" and (n.tally_Sign in(5,4,3)) and n.tally_code = '").append(user.getId()).append("'");
        }
        if (StringUtils.isNotEmpty(corpAuthSql)) {
            sql.append(corpAuthSql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), NisBillheadVO.class, "n", false);
    }

    /**
     * 自动记账按金额记账
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnionByAccountMoney(DataTablesInput dataTablesInput, String joinSql, ICurrentUser user) {
        Object opType = dataTablesInput.getFormdata().get("opType");
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        sql.append(" and n.status = " + NetbankConstant.STATUE_CHECK + " and f.is_account = '1' ");
        if ("Init".equals(opType)) {
            sql.append(" and (n.tally_Sign ='' or n.tally_Sign ='0' or  n.tally_sign is null) ");
        } else if ("Check".equals(opType)) {
            sql.append(" and (n.tally_Sign = '3' ) and n.tally_code != '").append(user.getId()).append("'");
        } else if ("Edit".equals(opType)) {
            sql.append(" and (n.tally_Sign in(5,4,3)) and n.tally_code = '").append(user.getId()).append("'");
        }
        sql.append(joinSql);
        return getDynamicBySQL(dataTablesInput, sql.toString(), NisBillheadVO.class, "n", false);
    }

    /**
     * 根据单号获取付款信息
     *
     * @param billNo
     */
    public NisBillheadVO getByBillNo(String billNo) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("billNo", billNo);
        return this.getBySQLAliasToBean(this.getCommonSql() + " and n.bill_no=:billNo", params, NisBillheadVO.class);
    }

    /**
     * 根据ERPID获取付款单号和指令状态（针对兑付状态使用）。
     *
     * @param erpIdList ERP编号集合。
     * @return 付款信息。
     */
    public List<NisBillheadVO> listNisBillInfoByErpId(List<String> erpIdList) {
        String sql = "select n.id,n.STATUS as status,n.BILL_NO as billNo,n.IS_PAYED as isPayed,b.VOUCHER_STAT as voucherStat,n.ERP_ID as erpId " +
                " from NIS_BILLHEAD n left join bis_exc b on n.ID = b.CBS_BILL_ID where ERP_ID in(:erpIdList)";
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("erpIdList", erpIdList);
        return this.listBySQLAliasToBean(sql, params, NisBillheadVO.class);
    }

    /**
     * 获取拆分明细
     *
     * @param splitId
     * @return
     */
    public List<NisBillhead> listNisBillheadBySplitId(String splitId) {
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
        sql.append("select distinct n.corp_Id from Nis_Billhead n left join Fc_Voucher_Type f on  n.voucher_Type_Id = f.id ");
        sql.append(" left join Nis_Bus_Type t on n.bus_Type = t.id  where 1=1 ");
        if (!EmptyUtils.isEmpty(businessSql)) {
            sql.append(" and ").append(businessSql);
        }
        return this.getSession().createSQLQuery(sql.toString()).list();
    }

    /**
     * 查询付款单
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesQuery(DataTablesInput dataTablesInput, String joinSql, String isMergeAndSplit) {
        StringBuilder sql = new StringBuilder();
        sql.append("select n.id as id,n.bill_No as billNo,n.corp_Id as corpId,n.voucher_Type_Id as voucherTypeId,f.voucher_Name as voucherName,");
        sql.append(" n.acc_Id as accId,n.payer_Acc as payerAcc,n.payer_Acc_Name as payerAccName,n.payer_Bank as payerBank,n.down_Acc_Id as downAccId,");
        sql.append(" n.cur_Id as curId,n.pay_Money as payMoney,n.payee_Acc as payeeAcc,n.payee_Acc_Name as payeeAccName,n.payee_Bank_Code as payeeBankCode,");
        sql.append(" n.payee_Bank as payeeBank,n.prov as prov,n.city as city,n.wish_Payday as wishPayday,n.is_Quick as isQuick,n.is_Forindividual as isForindividual,");
        sql.append(" n.bif_Code as bifCode,n.dif_Bank as difBank,n.area_Sign as areaSign,n.bursar_Bill_No as bursarBillNo,n.purpose as purpose,n.add_Word as addWord,n.order_No as orderNo,n.voucher_No as voucherNo,");
        sql.append(" t.id as busTypeId,n.bus_Type as busType,t.bus_Type_Name as busTypeName,n.orig_split_id as origSplitId,");
        sql.append(" n.tally_Date as tallyDate,n.erp_Id as erpId,n.tally_sign as tallySign,");
        sql.append(" n.bill_From as billFrom,n.split_Id as splitId,n.merge_Id as mergeId,n.status as status,n.is_Auto_Pay as isAutoPay,");
        sql.append(" n.create_By as createBy,n.create_Date as createDate,n.LANDING_LIMIT_RESULT as landingLimitResult,n.update_By as updateBy,n.update_Date as updateDate,n.attachment_Id as attachmentId,n.is_payed as isPayed ");
        sql.append(" ,b.voucher_Stat as voucherStat,b.id as bisExcId,n.bill_money as billMoney, n.approve_Date as approveDate,nd.bill_no as demandBillNo,be.voucher_stat as demandVoucherStat, ");
        //sql.append(",b.voucher_Stat as voucherStat,b.id as bisExcId,n.bill_money as billMoney, n.approve_Date as approveDate, ");
        if (CommonConstant.YES.equals(isMergeAndSplit)) {
            sql.append(" s.bill_No as splitBillNo, ");
        }
        sql.append(" n.BILL_TYPE as billType,n.BILL_FLAG as billFlag,nd.id as demandId,n.op_code as opCode,f.sys_voucher_code as sysVoucherCode ");
        sql.append(" ,be.trans_time as transTime,n.sharing_date as sharingDate,BT_NVL(n.sync_status,'F') as isSendGx, ");
        // 共享业务类型关联
        sql.append(" n.SHARE_BUS_TYPE_ID as shareBusTypeId, nsbt.SHARE_BUS_TYPE as shareBusType, nsbt.SHARE_BUS_TYPE_NAME as shareBusTypeName, ");
        sql.append(" bcbt.BUS_TYPE_NAME chargeBackName,b.next_return_msg nextReturnMsg ");
        sql.append(" from Nis_Billhead n left join Fc_Voucher_Type f on  n.voucher_Type_Id = f.id");
        if (CommonConstant.YES.equals(isMergeAndSplit)) {
            sql.append(" left join Nis_Billhead s on n.split_id = s.id");
        }
        sql.append(" left join Nis_Bus_Type t on n.bus_Type = t.id");
        sql.append(" left join NIS_SHARE_BUS_TYPE nsbt on n.SHARE_BUS_TYPE_ID = nsbt.id");
        //sql.append(" left join nis_demand nd on n.id = nd.pay_bill_id ");
        sql.append(" left join (");
        sql.append("   select * from nis_demand");
        sql.append("   where (pay_bill_id, bill_no) in (");
        sql.append("       select pay_bill_id, max(bill_no)");
        sql.append("       from nis_demand");
        sql.append("       group by pay_bill_id");
        sql.append("   )");
        sql.append(" ) nd on n.id = nd.pay_bill_id");

        sql.append(" left join bis_exc be on nd.id = be.cbs_bill_id ");
        sql.append(" left join Bis_exc b on n.id = b.cbs_bill_id and b.voucher_From = '" + BdlinkConstant.FROM_NIS_PAY+"'");
        sql.append(" left join erp_payinfo e on n.erp_id = e.id ");
        sql.append(" LEFT JOIN bt_charge_back_type bcbt ON e.BUS_TYPE = bcbt.BUS_TYPE ");
        sql.append(" where 1=1");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        // eqs_isAutoPay
        String eqs_isAutoPay = (String) dataTablesInput.getFormdata().get("eqs_isAutoPay");
        if (ObjectUtil.isNotEmpty(eqs_isAutoPay) && CommonConstant.NO.equals(eqs_isAutoPay)){
            sql.append(" and ( n.is_Auto_Pay = '0' or n.is_Auto_Pay is null or n.is_Auto_Pay = '' ) ");
            dataTablesInput.getFormdata().remove("eqs_isAutoPay");
        }
        String status = (String) dataTablesInput.getFormdata().get("eqi_status");
        if (NetbankConstant.STATUE_CHECK.toString().equals(status)) {
            sql.append(" and (n.tally_sign is null or n.tally_sign = '' or n.tally_sign='" + NbconsoleConstant.NbcEstate.E_STATE_CHECK.getValue() + "')");
        }
        String isSendGx = (String) dataTablesInput.getFormdata().get("eqs_isSendGx");
        if (StringUtils.isNotBlank(isSendGx)) {
            sql.append(" and BT_NVL(n.sync_status,'F') = '").append(isSendGx).append("'");
        }
        //业务类别查询条件变为多选了
        String eqs_busType = (String) dataTablesInput.getFormdata().get("eqs_busType");
        if(EmptyUtils.isNotEmpty(eqs_busType)){
            String[] eqs_busTypes = eqs_busType.split(",");
            sql.append(" and  "+SqlUtil.getInSql(eqs_busTypes,"n.bus_Type",true));
            dataTablesInput.getFormdata().remove("eqs_busType");
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), NisBillheadVO.class, "n", false);
    }

    /**
     * 根据业务单据号得到流水id
     *
     * @param billId
     * @return
     */
    public List<String> getDtlIdByBillId(String billId) {
        String sql = " select  DTL_ID from NIS_BILL_BANK_SERIAL_NO where  bill_id=:billId ";
        HashMap<String, Object> params = new HashMap<>();
        params.put("billId", billId);
        return this.getSession().createSQLQuery(sql).setProperties(params).list();
    }

    /**
     * 根据付款单ID集合批量查询交易时间。
     * @param billIdList 付款单ID集合。
     * @return 数据集合。
     */
    public List<NisBillDataVo> listDataByBillId(List<String> billIdList) {
        if (ObjectUtil.isEmpty(billIdList)){
            return ListUtil.empty();
        }
        String sql = "select bsn.bill_id as billId, bsn.DTL_ID as dtlId,d.trans_time as transTime " +
                " from NIS_BILL_BANK_SERIAL_NO bsn left join bis_acc_dtl d on bsn.DTL_ID = d.id " +
                " where " + SqlUtil.getInSql(billIdList, "bsn.bill_id", true) + " and d.trans_time is not null";
        return this.listBySQLAliasToBean(sql, null, NisBillDataVo.class);
    }

    /**
     * 查询付款单
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesExecute(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append("select n.id as id,n.bill_No as billNo,n.corp_Id as corpId,n.voucher_Type_Id as voucherTypeId,f.voucher_Name as voucherName,f.sys_Voucher_Code as sysVoucherCode,f.is_Account as isAccount,");
        sql.append(" n.acc_Id as accId,n.payer_Acc as payerAcc,n.payer_Acc_Name as payerAccName,n.payer_Bank as payerBank,n.down_Acc_Id as downAccId,c.bank_acc as downAcc,c.acc_name as downAccName,");
        sql.append(" n.cur_Id as curId,n.pay_Money as payMoney,n.payee_Acc as payeeAcc,n.payee_Acc_Name as payeeAccName,n.payee_Bank_Code as payeeBankCode,");
        sql.append(" n.payee_Bank as payeeBank,n.prov as prov,n.city as city,n.wish_Payday as wishPayday,n.is_Quick as isQuick,n.is_Forindividual as isForindividual,");
        sql.append(" n.bif_Code as bifCode,n.dif_Bank as difBank,n.area_Sign as areaSign,n.bursar_Bill_No as bursarBillNo,n.purpose as purpose,n.add_Word as addWord,n.order_No as orderNo,n.voucher_No as voucherNo,");
        sql.append(" n.direct_Pay_Flag as directPayFlag,n.ca_Info_Id as caInfoId,n.tally_Date as tallyDate,n.erp_Id as erpId,n.tally_sign as tallySign,");
        sql.append(" t.id as busTypeId,n.bus_Type as busType,t.bus_Type_Name as busTypeName,n.orig_split_id as origSplitId,n.bus_Corp_Id as busCorpId,n.bus_Personnel as busPersonnel,");
        sql.append(" n.bill_From as billFrom,n.split_Id as splitId,n.merge_Id as mergeId,n.status as status,n.is_Auto_Pay as isAutoPay,");
        sql.append(" n.create_By as createBy,n.create_Date as createDate,n.update_By as updateBy,n.update_Date as updateDate,n.attachment_Id as attachmentId,n.is_payed as isPayed");
        sql.append(",n.bill_money as billMoney,pc.bank_acc as downAccPrent,pc.acc_name as downAccNamePrent, n.approve_Date as approveDate, ");
        sql.append(" n.is_Advance as isAdvance,n.work_Capital_Ratio as workCapitalRatio,n.take_Back_Date as takeBackDate,n.is_Open_Bill as isOpenBill,");
        sql.append(" n.BILL_TYPE as billType,n.BILL_FLAG as billFlag ");
        sql.append(" from Nis_Billhead n left join Fc_Voucher_Type f on  n.voucher_Type_Id = f.id");
        sql.append(" left join Nis_Bus_Type t on n.bus_Type = t.id");
        //
        sql.append(" left join bt_bank_acc c on n.down_Acc_Id = c.id");
        sql.append(" left join bt_bank_acc_cur bc on n.down_Acc_Id = bc.bank_acc_id and bc.cur_id=n.cur_id");
        sql.append(" left join bt_bank_acc pc on pc.id = bc.prent_acc_id");
        //
        sql.append(" left join bis_exc exc on exc.cbs_bill_id = n.id");
        sql.append(" where 1=1");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        sql.append(" and (exc.voucher_no is null or exc.voucher_no = '' or exc.voucher_stat in ('" + BdlinkConstant.BACK + "', '" + BdlinkConstant.INVALIDATE + "', '" + BdlinkConstant.DELETE + "'))");
        return getDynamicBySQL(dataTablesInput, sql.toString(), NisBillheadVO.class, "n", false);
    }

    @SuppressWarnings("rawtypes")
    public DataTablesOutput<NisBillheadVO> getDataTablesExecuteNew(DataTablesInput dataTablesInput, String joinSql, List ids) {
        StringBuilder sql = new StringBuilder();
        sql.append("select n.id as id,n.bill_No as billNo,n.corp_Id as corpId,n.voucher_Type_Id as voucherTypeId,f.voucher_Name as voucherName,f.sys_Voucher_Code as sysVoucherCode,f.is_Account as isAccount,");
        sql.append(" n.acc_Id as accId,n.payer_Acc as payerAcc,n.payer_Acc_Name as payerAccName,n.payer_Bank as payerBank,n.down_Acc_Id as downAccId,");
        sql.append(" n.cur_Id as curId,n.pay_Money as payMoney,n.payee_Acc as payeeAcc,n.payee_Acc_Name as payeeAccName,n.payee_Bank_Code as payeeBankCode,");
        sql.append(" n.payee_Bank as payeeBank,n.prov as prov,n.city as city,n.wish_Payday as wishPayday,n.is_Quick as isQuick,n.is_Forindividual as isForindividual,");
        sql.append(" n.bif_Code as bifCode,n.dif_Bank as difBank,n.area_Sign as areaSign,n.bursar_Bill_No as bursarBillNo,n.purpose as purpose,n.add_Word as addWord,n.order_No as orderNo,n.voucher_No as voucherNo,");
        sql.append(" n.direct_Pay_Flag as directPayFlag,n.ca_Info_Id as caInfoId,n.tally_Date as tallyDate,n.erp_Id as erpId,n.tally_sign as tallySign,");
        sql.append(" t.id as busTypeId,n.bus_Type as busType,t.bus_Type_Name as busTypeName,n.orig_split_id as origSplitId,n.bus_Corp_Id as busCorpId,n.bus_Personnel as busPersonnel,");
        sql.append(" n.bill_From as billFrom,n.split_Id as splitId,n.merge_Id as mergeId,n.status as status,n.is_Auto_Pay as isAutoPay,");
        sql.append(" n.create_By as createBy,n.create_Date as createDate,n.update_By as updateBy,n.update_Date as updateDate,n.attachment_Id as attachmentId,n.is_payed as isPayed,");
        sql.append(" n.bill_money as billMoney, n.approve_Date as approveDate, ");
        sql.append(" n.is_Advance as isAdvance,n.work_Capital_Ratio as workCapitalRatio,n.take_Back_Date as takeBackDate,n.is_Open_Bill as isOpenBill,");
        // 增加收款人统一社会信用代码字段查询
        sql.append(" n.BL_RCVPYMTPS_UNN_ID_NO as blRcvpymtpsUnnIdNo, ");
        // 共享业务类型关联
        sql.append(" n.SHARE_BUS_TYPE_ID as shareBusTypeId, nsbt.SHARE_BUS_TYPE as shareBusType, nsbt.SHARE_BUS_TYPE_NAME as shareBusTypeName, ");
        sql.append(" n.BILL_TYPE as billType,n.BILL_FLAG as billFlag ");
        sql.append(" from Nis_Billhead n left join Fc_Voucher_Type f on  n.voucher_Type_Id = f.id");
        sql.append(" left join Nis_Bus_Type t on n.bus_Type = t.id");
        sql.append(" left join NIS_SHARE_BUS_TYPE nsbt on n.SHARE_BUS_TYPE_ID = nsbt.id");
        sql.append(" where 1=1");
        if (StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        sql.append(" and ").append("(n.voucher_stat is null or n.voucher_stat = '' or n.voucher_stat in ('" + BdlinkConstant.INVALIDATE + "', '" + BdlinkConstant.BACK + "', '" + BdlinkConstant.DELETE + "'))");
        return getDynamicBySQL(dataTablesInput, sql.toString(), NisBillheadVO.class, "n", false);
    }

    /**
     * <br>
     * <b>功能：</b>查询付款计划（企e金服接口）<br>
     * <b>作者：</b>黄茜<br>
     * <b>日期：</b>2018-11-25<br>
     *
     * @param corpflag
     * @param corpCode
     * @return
     */
    public BigDecimal queryNisBillhead(String corpflag, String corpCode, Date wishPayday) {
        StringBuilder sql = new StringBuilder();
        sql.append("select bt_nvl_num(sum(n.pay_money), 0) as payMoney");
        sql.append(" from nis_billhead n left join bis_exc e on n.id = e.cbs_bill_id left join sys_corp c on n.corp_id = c.id");
        sql.append(" where n.is_payed = '0' and n.status > -2 and (e.cbs_bill_id is null or e.voucher_stat != '0')");
        sql.append(" and bt_to_char(n.wish_payday,'yyyy-MM-dd') = :wishPayday");
        //1-网点；2-网点+单位
        if ("1".equals(corpflag)) {
            sql.append(" and c.code = :corpCode");
        } else {
            sql.append(" and c.net_id = (select id from sys_corp where code = :corpCode)");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("wishPayday", DateUtils.formatDate(wishPayday, "yyyy-MM-dd"));
        params.put("corpCode", corpCode);
        Object result = this.createSQLQuery(sql.toString(), params).uniqueResult();
        BigDecimal payMoney = BigDecimal.ZERO;
        if (!EmptyUtils.isEmpty(result)) {
            payMoney = (BigDecimal) result;
        }
        return payMoney;
    }

    /**
     * 外系统根据时间获取付款单
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public List<NisBillheadVO> listByTime(String beginDate, String endDate) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("beginDate", beginDate);
        params.put("endDate", endDate);
        return listBySQLAliasToBean(this.getCommonSql() + " and (n.status = 95 or n.status = 100) and n.create_date >= bt_to_date(:beginDate,'yyyy-MM-dd') and n.create_date <= bt_to_date(:endDate,'yyyy-MM-dd')", params, NisBillheadVO.class);
    }

    /**
     * 根据外部单号获取付款单信息
     *
     * @param erpId
     * @return
     */
    public NisBillheadVO getNisBillheadByErpId(String erpId, String billFrom) {
        String sql = "select n.id as id,n.bill_No as billNo,n.corp_Id as corpId,n.voucher_Type_Id as voucherTypeId,f.voucher_Name as voucherName,f.sys_Voucher_Code as sysVoucherCode,f.is_Account as isAccount," +
                " n.acc_Id as accId,n.payer_Acc as payerAcc,n.payer_Acc_Name as payerAccName,n.payer_Bank as payerBank,n.down_Acc_Id as downAccId," +
                " n.cur_Id as curId,n.pay_Money as payMoney,n.payee_Acc as payeeAcc,n.payee_Acc_Name as payeeAccName,n.payee_Bank_Code as payeeBankCode," +
                " n.payee_Bank as payeeBank,n.prov as prov,n.city as city,n.wish_Payday as wishPayday,n.is_Quick as isQuick,n.is_Forindividual as isForindividual," +
                " n.bif_Code as bifCode,n.dif_Bank as difBank,n.area_Sign as areaSign,n.bursar_Bill_No as bursarBillNo,n.purpose as purpose,n.add_Word as addWord,n.order_No as orderNo,n.voucher_No as voucherNo," +
                " n.direct_Pay_Flag as directPayFlag,n.ca_Info_Id as caInfoId,n.tally_Date as tallyDate,n.erp_Id as erpId," +
                " t.id as busTypeId,n.bus_Type as busType,t.bus_Type_Name as busTypeName,n.orig_split_id as origSplitId,n.bus_Corp_Id as busCorpId,n.bus_Personnel as busPersonnel," +
                " n.bill_From as billFrom,n.split_Id as splitId,n.merge_Id as mergeId,n.status as status," +//c.bank_acc as downAcc,c.acc_name as downAccName," +
                " n.create_By as createBy,n.create_Date as createDate,n.update_By as updateBy,n.update_Date as updateDate,n.attachment_Id as attachmentId,n.is_payed as isPayed" +
                ",n.voucher_Stat as voucherStat,n.bill_money as billMoney, " +
                //",b.voucher_Stat as voucherStat,b.id as bisExcId,n.bill_money as billMoney, " +
                " n.is_Advance as isAdvance,n.work_Capital_Ratio as workCapitalRatio,n.take_Back_Date as takeBackDate,n.is_Open_Bill as isOpenBill" +
                " from Nis_Billhead n left join Fc_Voucher_Type f on  n.voucher_Type_Id = f.id" +
                " left join Nis_Bus_Type t on n.bus_Type = t.id" +
                //" left join Bis_exc b on n.id = b.cbs_bill_id and b.voucher_From = " + BdlinkConstant.FROM_NIS_PAY +
                //" left join bt_bank_acc c on n.down_Acc_Id = c.id where 1=1" +
                " where n.erp_Id=:erpId and n.bill_from = :billFrom ";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("erpId", erpId);
        params.put("billFrom", billFrom);
        return getBySQLAliasToBean(sql, params, NisBillheadVO.class);
    }

    /**
     * 根据用户流程查询审批付款单据中的账号
     *
     * @param businessSql
     * @return
     */
    public List<NisBillheadVO> getPayerAcc(String businessSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select distinct n.payer_acc as payerAcc from nis_billhead n where 1=1 ");
        if (!EmptyUtils.isEmpty(businessSql)) {
            sql.append(" and ").append(businessSql);
        }
        sql.append(" and  n.status<> " + NetbankConstant.STATUE_CHECK);
        return listBySQLAliasToBean(sql.toString(), null, NisBillheadVO.class);
    }

    /**
     * 根据银行流水获取付款单
     *
     * @param dataTablesInput
     * @return
     */
    /**
     * 根据银行流水获取付款单
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnionForDtl(DataTablesInput dataTablesInput,String table,String joinSql) {
        StringBuilder sql = new StringBuilder();
        StringBuilder sql1 = new StringBuilder();
        DataTablesOutput output = null;
        String eqs_bankSerialNo = (String) dataTablesInput.getFormdata().get("eqs_bankSerialNo");
        sql.append(" select distinct n.*,nt.bus_type_name as busTypeName,b.bank_serial_no as bankSerialNo from "+table+" n ");
        sql.append(" left join NIS_BILL_BANK_SERIAL_NO b on n.id = b.bill_id and n.isFrom = b.is_from ");
        sql.append(" left join bis_exc exc on n.id = exc.cbs_bill_id  ");
        sql.append(" left join nis_bus_type nt on n.busType = nt.id  ");
        sql.append(" left join bt_Bank_acc acc on n.payerAcc=acc.bank_acc");
        sql.append(" where 1=1");
        if(EmptyUtils.isNotEmpty(joinSql)){
            sql.append(joinSql);
        }
        sql1.append(sql);
        sql1.append(" and b.bank_serial_no = '" + eqs_bankSerialNo + "' ");
        output = dealDynamicBySQL(dataTablesInput, sql1.toString(), VNisPayBillVO.class, "n", false);
        if (output.getData().size() == 0 && !"#".equals(eqs_bankSerialNo)) {
            String gedTransTime = (String) dataTablesInput.getFormdata().get("wishPayday");
            String ledTransTime = (String) dataTablesInput.getFormdata().get("endWishPayday");
            StringBuilder transTimeSql = new StringBuilder();
            if (StringUtils.isNotEmpty(gedTransTime)) {
                gedTransTime = gedTransTime + " 00:00:00";
                transTimeSql.append(" and  case when exc.trans_time  is not null then exc.trans_time  else ");
                transTimeSql.append(" n.wishPayday end  >= '"+ gedTransTime+ "'");
            }
            if (StringUtils.isNotEmpty(ledTransTime)) {
                ledTransTime = ledTransTime + " 23:59:59";
                transTimeSql.append(" and  case when exc.trans_time  is not null then exc.trans_time  else ");
                transTimeSql.append(" n.wishPayday end  <= '"+ ledTransTime+ "'");
            }
            dataTablesInput.getFormdata().remove("wishPayday");
            if (StringUtils.isNotBlank(transTimeSql.toString())) {
                sql.append(transTimeSql);
            }
            if(!dataTablesInput.getFormdata().containsKey("eqs_blendSign")) {
                sql.append(" and (b.bank_serial_no is null or  b.bank_serial_no = '') ");
            }
            if(dataTablesInput.getFormdata().containsKey("eqs_payeeAcc")){
                String payeeAcc = (String) dataTablesInput.getFormdata().get("eqs_payeeAcc");
                if(EmptyUtils.isNotEmpty(payeeAcc)) {
                    if (payeeAcc.equals("null")) {
                        sql.append(" and payeeAcc = '" + payeeAcc + "'");
                    }
                }
            }
            //付款账号查询条件取指令表的付款账号。防止需要记账的付款单的付款账号为内部账号
            if(dataTablesInput.getFormdata().containsKey("likes_payerAcc")){
                String payerAcc = (String) dataTablesInput.getFormdata().get("likes_payerAcc");
                if(EmptyUtils.isNotEmpty(payerAcc)) {
                    sql.append(" and case when exc.PAYER_ACC is not null then exc.PAYER_ACC  else n.payerAcc  end like '%"+payerAcc+"%'");
                }
                dataTablesInput.getFormdata().remove("likes_payerAcc");
            }
            //用途
            if(dataTablesInput.getFormdata().containsKey("likes_purpose")){
                String purpose = (String) dataTablesInput.getFormdata().get("likes_purpose");
                if(EmptyUtils.isNotEmpty(purpose)) {
                    sql.append(" and n.purpose like '%"+purpose+"%'");
                }
            }
            output = getDynamicBySQL(dataTablesInput, sql.toString(), VNisPayBillVO.class, "n", false);
        }
        List<VNisPayBillVO> listVo = output.getData();
        List<VNisPayBillDTO> listDto = new ArrayList<>();
        listDto = BeanUtils.copyListProperties(listVo, VNisPayBillDTO.class);
        output.setData(listDto);
        return output;
    }

    /**
     * 根据银行流水获取付款单
     * 功能：退款勾兑
     *
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput getDataTablesUnionForRefundblending(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        DataTablesOutput output = null;
        String eqs_bankSerialNo = (String) dataTablesInput.getFormdata().get("eqs_bankSerialNo");
        sql.append(" select n.id as id,n.bill_No as billNo,n.corp_Id as corpId,f.voucher_name as voucherName,n.payer_Acc as payerAcc,n.payer_Acc_Name as payerAccName, ");
        sql.append(" n.cur_Id as curId,n.wish_payday as wishPayday,n.pay_Money as payMoney,n.payee_Acc as payeeAcc,n.payee_Acc_Name as payeeAccName, ");
        sql.append(" n.purpose as purpose,t.id as busTypeId,n.bus_Type as busType,n.status as status,n.rmk ");
        sql.append(" from Nis_Billhead n ");
        sql.append(" left join Fc_Voucher_Type f on  n.voucher_Type_Id = f.id ");
        sql.append(" left join Nis_Bus_Type t on n.bus_Type = t.id ");
        sql.append(" left join Bis_exc b on n.id = b.cbs_bill_id and b.voucher_From = '1' ");
        sql.append(" left join bt_bank_acc c on n.payer_acc = c.bank_acc ");
        sql.append(" where f.sys_voucher_code in('" + NetbankConstant.PAY_TYPE_34 + "','" + NetbankConstant.PAY_TYPE_02 + "','"
                + NetbankConstant.PAY_TYPE_33 + "','" + NetbankConstant.PAY_TYPE_35 + "','" + NetbankConstant.PAY_TYPE_07 + "')  ");
//        sql.append(" and n.dif_bank = '" + CommonConstant.YES + "'");
        String blendSign = (String) dataTablesInput.getFormdata().get("blendSign");
        String type = (String) dataTablesInput.getFormdata().get("type");
        // 规则条件
        if (ObjectUtil.isNotEmpty(dataTablesInput.getFormdata().get("ruleSql"))){
            sql.append(dataTablesInput.getFormdata().get("ruleSql"));
            dataTablesInput.getFormdata().remove("ruleSql");
        }
        //未勾兑的需要控制状态
        if (BtBankAccConstant.BLENDSIGN_0.equals(blendSign)) {
            sql.append(" and n.status >= " + NetbankConstant.STATUE_CHECK);
            sql.append(" and (n.is_payed = '1' or c.is_online = '0' or b.voucher_stat = '0' or n.voucher_stat = '0')");
            // 未勾兑的时候，增加条件，n.id不存在NIS_BILL_BANK_SERIAL_NO_REFUND表中
            sql.append(" and n.id not in (select bill_id from Nis_Bill_Bank_Serial_No_Refund where bill_id = n.id and is_from = 'nis_billhead' )");
        }
        if (!"#".equals(eqs_bankSerialNo)) {
            String gedTransTime = (String) dataTablesInput.getFormdata().get("wishPayday");
            String ledTransTime = (String) dataTablesInput.getFormdata().get("wishPayday");
            if (StringUtils.isNotEmpty(gedTransTime)) {
                gedTransTime = gedTransTime + " 00:00:00";
                dataTablesInput.getFormdata().put("ged_wishPayday", gedTransTime);
            }
            if (StringUtils.isNotEmpty(ledTransTime)) {
                ledTransTime = ledTransTime + " 23:59:59";
                dataTablesInput.getFormdata().put("led_wishPayday", ledTransTime);
            }
            if (StringUtils.isBlank(blendSign) ||BtBankAccConstant.BLENDSIGN_0.equals(blendSign)) {
                if (EmptyUtils.isNotEmpty(type) && "auto".equals(type)) {
                    sql.append(" and (n.bank_serial_no is null or  n.bank_serial_no = '') ");
                }
            } else {
                sql.append(" and n.bank_serial_no = '" + eqs_bankSerialNo + "' ");
            }
            output = getDynamicBySQL(dataTablesInput, sql.toString(), NisBillheadVO.class, "n", false);
        }
        List<NisBillheadDTO> listDto = new ArrayList<>();
        if (output == null) {
            output = new DataTablesOutput();
        }
        List<NisBillheadVO> listVo = output.getData();
        listDto = BeanUtils.copyListProperties(listVo, NisBillheadDTO.class);
        output.setData(listDto);
        return output;
    }


    /**
     * 根据开始时间结束时间获取付款单
     * * 功能：退款勾兑定时任务
     * @param beginDate
     * @param endDate
     * @return
     */
    public List<NisBillheadDTO> getDataTablesUnionForRefundblending(Date beginDate,Date endDate) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select n.id as id,n.bill_No as billNo,n.corp_Id as corpId,f.voucher_name as voucherName,n.payer_Acc as payerAcc,n.payer_Acc_Name as payerAccName, ");
        sql.append(" n.cur_Id as curId,n.wish_payday as wishPayday,n.pay_Money as payMoney,n.payee_Acc as payeeAcc,n.payee_Acc_Name as payeeAccName, ");
        sql.append(" n.purpose as purpose,t.id as busTypeId,n.bus_Type as busType,n.status as status,n.rmk ");
        sql.append(" from Nis_Billhead n ");
        sql.append(" left join Fc_Voucher_Type f on  n.voucher_Type_Id = f.id ");
        sql.append(" left join Nis_Bus_Type t on n.bus_Type = t.id ");
        sql.append(" left join Bis_exc b on n.id = b.cbs_bill_id and b.voucher_From = '1' ");
        sql.append(" left join bt_bank_acc c on n.payer_acc = c.bank_acc ");
        sql.append(" where f.sys_voucher_code in('" + NetbankConstant.PAY_TYPE_34 + "','" + NetbankConstant.PAY_TYPE_02 + "','" + NetbankConstant.PAY_TYPE_33 + "','" + NetbankConstant.PAY_TYPE_35 + "')  ");
        sql.append(" and (n.bank_serial_no is null or  n.bank_serial_no = '') ");
        sql.append(" and n.status >= " + NetbankConstant.STATUE_CHECK);
        sql.append(" and (n.is_payed = '1' or c.is_online = '0' or b.voucher_stat = '0' or n.voucher_stat = '0')");
        sql.append(" and n.wish_payday >= :beginDate and n.wish_payday <= :endDate ");
        params.put("beginDate",beginDate);
        params.put("endDate",endDate);
        List<NisBillheadDTO> list = this.listBySQLAliasToBean(sql.toString(),params,NisBillheadDTO.class);
        return list;
    }

    public List<NisBillheadDTO> getDataTablesUnionForRefundblendingNew(Date beginDate,Date endDate,int rownum) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select n.id as id,n.bill_No as billNo,n.corp_Id as corpId,f.voucher_name as voucherName,n.payer_Acc as payerAcc,n.payer_Acc_Name as payerAccName, ");
        sql.append(" n.cur_Id as curId,n.wish_payday as wishPayday,n.pay_Money as payMoney,n.payee_Acc as payeeAcc,n.payee_Acc_Name as payeeAccName, ");
        sql.append(" n.purpose as purpose,t.id as busTypeId,n.bus_Type as busType,n.status as status,n.rmk,n.BURSAR_BILL_NO as bursarBillNo ");
        sql.append(" from Nis_Billhead n ");
        sql.append(" left join Fc_Voucher_Type f on  n.voucher_Type_Id = f.id ");
        sql.append(" left join Nis_Bus_Type t on n.bus_Type = t.id ");
        sql.append(" where f.sys_voucher_code in('" + NetbankConstant.PAY_TYPE_34 + "','" + NetbankConstant.PAY_TYPE_02 + "','" + NetbankConstant.PAY_TYPE_33 + "','" + NetbankConstant.PAY_TYPE_35 + "')  ");
        sql.append(" and (n.bank_serial_no is null or  n.bank_serial_no = '') ");
        sql.append(" and n.status >= " + NetbankConstant.STATUE_CHECK);
        sql.append(" and n.wish_payday >= :beginDate and n.wish_payday <= :endDate ");
        params.put("beginDate",beginDate);
        params.put("endDate",endDate);
        if(rownum<=4000) {
        	sql.append(" order by n.create_date desc limit "+rownum);
        }else {
        	sql.append(" order by n.create_date desc limit "+(rownum-2000)+","+rownum);
        }
        List<NisBillheadDTO> list = this.listBySQLAliasToBean(sql.toString(),params,NisBillheadDTO.class);
        return list;
    }

    public int getCountDataTablesUnionForRefundblending(Date beginDate,Date endDate) {
    	 StringBuilder sql = new StringBuilder();
         Map<String, Object> params = new HashMap<>();
         sql.append(" select count(*) from (");
         sql.append(" select n.id as id,n.bill_No as billNo,n.corp_Id as corpId,f.voucher_name as voucherName,n.payer_Acc as payerAcc,n.payer_Acc_Name as payerAccName, ");
         sql.append(" n.cur_Id as curId,n.wish_payday as wishPayday,n.pay_Money as payMoney,n.payee_Acc as payeeAcc,n.payee_Acc_Name as payeeAccName, ");
         sql.append(" n.purpose as purpose,t.id as busTypeId,n.bus_Type as busType,n.status as status,n.rmk ");
         sql.append(" from Nis_Billhead n ");
         sql.append(" left join Fc_Voucher_Type f on  n.voucher_Type_Id = f.id ");
         sql.append(" left join Nis_Bus_Type t on n.bus_Type = t.id ");
         sql.append(" where f.sys_voucher_code in('" + NetbankConstant.PAY_TYPE_34 + "','" + NetbankConstant.PAY_TYPE_02 + "','" + NetbankConstant.PAY_TYPE_33 + "','" + NetbankConstant.PAY_TYPE_35 + "')  ");
         sql.append(" and (n.bank_serial_no is null or  n.bank_serial_no = '') ");
         sql.append(" and n.status >= " + NetbankConstant.STATUE_CHECK);
         sql.append(" and n.wish_payday >= :beginDate and n.wish_payday <= :endDate ");
         params.put("beginDate",beginDate);
         params.put("endDate",endDate);
         sql.append(" ) as acou");
    	return ((Number) this.createSQLQuery(sql.toString(), params).uniqueResult()).intValue();
    }



    /**
     * 根据银行流水得到付款单
     *
     * @param bankSerialNo
     * @return
     */
    public List<NisBillheadVO> listNisBillheadByBankSerialNo(String bankSerialNo) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("bankSerialNo", bankSerialNo);
        return listBySQLAliasToBean(this.getCommonSql() + " and  n.bank_serial_no=:bankSerialNo", params, NisBillheadVO.class);
    }

    /**
     * Erp接口（WebService）获取付款单交易状态信息
     *
     * @param serialNoErp 外部系统id
     * @return
     */
    public List<Map<Object, Object>> getVoucherStatByWs(String serialNoErp) {
        Map<String, Object> map = new HashMap<>();
        map.put("serialNoErp", serialNoErp);
        List<Map<Object, Object>> list = this.listBySQL("select status,STATE_DESC,bill_no from view_erp_payinfo where SERIAL_NO_ERP=:serialNoErp", map);
        if (list != null && list.size() > 0) {
            return list;
        } else {
            return null;
        }
    }

    /**
     * 根据支付策略获取付款单
     *
     * @return 满足支付策略的付款单集合
     */
    public List<NisBillheadVO> listByBisPayPolicy(String joinSql, Map<String, Object> params) {
        String sql = "select n.id as id,n.bill_No as billNo,n.corp_Id as corpId,n.voucher_Type_Id as voucherTypeId,f.voucher_Name as voucherName,f.sys_Voucher_Code as sysVoucherCode,f.is_Account as isAccount," +
                " f.send_Mode as sendMode,n.acc_Id as accId,n.payer_Acc as payerAcc,n.payer_Acc_Name as payerAccName,n.payer_Bank as payerBank,n.down_Acc_Id as downAccId," +
                " n.cur_Id as curId,n.pay_Money as payMoney,n.payee_Acc as payeeAcc,n.payee_Acc_Name as payeeAccName,n.payee_Bank_Code as payeeBankCode," +
                " n.payee_Bank as payeeBank,n.prov as prov,n.city as city,n.wish_Payday as wishPayday,n.is_Quick as isQuick,n.is_Forindividual as isForindividual," +
                " n.bif_Code as bifCode,n.dif_Bank as difBank,n.area_Sign as areaSign,n.bursar_Bill_No as bursarBillNo,n.purpose as purpose,n.add_Word as addWord,n.order_No as orderNo,n.voucher_No as voucherNo," +
                " n.direct_Pay_Flag as directPayFlag,n.ca_Info_Id as caInfoId,n.tally_Date as tallyDate,n.erp_Id as erpId," +
                " t.id as busTypeId,n.bus_Type as busType,t.bus_Type_Name as busTypeName,n.bill_money as billMoney," +
                " n.SHARE_BUS_TYPE_ID as shareBusTypeId, " +
                " n.bill_From as billFrom,n.split_Id as splitId,n.merge_Id as mergeId,n.status as status, n.approve_Date as approveDate," +
                " n.create_By as createBy,n.create_Date as createDate,n.update_By as updateBy,n.update_Date as updateDate,n.attachment_Id as attachmentId,n.is_payed as isPayed,n.is_auto_pay as isAutoPay," +
                " n.is_Advance as isAdvance,n.work_Capital_Ratio as workCapitalRatio,n.take_Back_Date as takeBackDate,n.is_Open_Bill as isOpenBill,f.is_budget as isBudget" +
                " from Nis_Billhead n " +
               // 在自动支付时，只支付生成了需求清单并且状态为“已处理”的付款单，其他新增的付款单不做自动支付。// 先注释。后续上线再打开
              //  " join nis_demand d on n.id = d.pay_bill_id and d.status = 3 " +
                " left join Fc_Voucher_Type f on  n.voucher_Type_Id = f.id " +
                " left join Nis_Bus_Type t on n.bus_Type = t.id" +
                " where 1=1 and n.status=:status and f.sys_Voucher_Code = :sysVoucherCode and n.wish_Payday<=:wishPayday  " +
                //根据策略获取
                joinSql;

        params.put("status", NetbankConstant.STATUE_TEMP.toString());
        params.put("sysVoucherCode", NetbankConstant.PAY_TYPE_34);
        params.put("wishPayday", DateUtils.getDateYMD());
//        params.put("billFrom", NetbankConstant.BILLFROM_1);
        return listBySQLAliasToBean(sql, params, NisBillheadVO.class);
    }

    /**
     * 根据策略获取付款单
     *
     * @param policyId
     */
    public List<NisBillheadVO> listByPolicyId(String policyId) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("policyId", policyId);
        params.put("status", NetbankConstant.STATUE_CHECK.toString());
        params.put("sysVoucherCode", NetbankConstant.PAY_TYPE_34);
        params.put("wishPayday", DateUtils.getDateYMD());
        params.put("billFrom", NetbankConstant.BILLFROM_1);
        return listBySQLAliasToBean(this.getCommonSql() + " and  n.policy_Id=:policyId and n.status=:status and f.sys_Voucher_Code = :sysVoucherCode and n.wish_Payday=:wishPayday and n.bill_From=:billFrom ", params, NisBillheadVO.class);
    }

    /**
     * 根据单位统计付款信息
     *
     * @param corpId
     * @return
     */
    public int getBillheadCountByCorpId(String corpId) {
        String sql = "select count(*) from nis_billhead where corp_id=:corpId";
        HashMap<String, Object> params = new HashMap<>();
        params.put("corpId", corpId);
        //0-暂存、11-待审批、-1-审批打回
        sql += " and status in ('0','11','-1')";
        return ((Number) this.createSQLQuery(sql, params).uniqueResult()).intValue();
    }

    /**
     * 根据单位统计付款信息（记账）
     *
     * @param corpId
     * @return
     */
    public int getBillheadCountByCorpIdForTally(String corpId) {
        String sql = "select count(*) from nis_billhead n left join fc_voucher_type t on n.voucher_type_id = t.id  where corp_id=:corpId";
        HashMap<String, Object> params = new HashMap<>();
        params.put("corpId", corpId);
        sql += " and t.is_account = '1' and t.sys_voucher_code in ( '02', '05', '33','35' ) and status >=95 and (tally_sign is null or tally_sign<>'1')";
        return ((Number) this.createSQLQuery(sql, params).uniqueResult()).intValue();
    }

    /**
     * 付款单id得到勾对的流水ID
     *
     * @param id
     * @return
     */
    public List<EBankMoreBillVO> getMoredtl(String id) {
        String sql = "select b.BILL_ID,b.IS_FROM,b.DTL_ID from NIS_BILL_BANK_SERIAL_NO b where b.BILL_ID=:id";
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        return listBySQLAliasToBean(sql, params, EBankMoreBillVO.class);
    }

    public List<NisBillBankSerialNoDTO> getBlendingDate(NisBillBankSerialNoDTO dto) {
        StringBuilder sql = new StringBuilder("select bill_id AS billId,bank_serial_no AS bankSerialNo,is_from AS isFrom ,dtl_id AS dtlId,paymentblending_date AS paymentblendingDate" +
                " from NIS_BILL_BANK_SERIAL_NO b where 1=1");
        HashMap<String, Object> params = new HashMap<>();
        if (StringUtils.isNotEmpty(dto.getBillId())) {
            sql.append(" and bill_id =:bill_id");
            params.put("bill_id", dto.getBillId());
        }
        if (StringUtils.isNotEmpty(dto.getDtlId())) {
            sql.append(" and dtl_id =:dtlId");
            params.put("dtlId", dto.getDtlId());
        }
        return listBySQLAliasToBean(sql.toString(), params, NisBillBankSerialNoDTO.class);
    }

    public List<NisBillBankSerialNoDTO> getBlendingList(List<String> dtlIdS) {
        StringBuilder sql = new StringBuilder("select bill_id AS billId,bank_serial_no AS bankSerialNo,is_from AS isFrom ,dtl_id AS dtlId,paymentblending_date AS paymentblendingDate" +
                " from NIS_BILL_BANK_SERIAL_NO b where 1=1");
        HashMap<String, Object> params = new HashMap<>();
        if (!EmptyUtils.isEmptyList(dtlIdS)) {
            sql.append(" and ").append(getSQLIn(dtlIdS, 900, "dtl_id"));
        }
        return listBySQLAliasToBean(sql.toString(), params, NisBillBankSerialNoDTO.class);
    }

    /**
     * 根据流水id得到关联的电子回单id
     *
     * @param id
     * @return
     */
    public List<BisBankDtlBillVO> getMorereceipt(String id) {
        String sql = "select brd.RECEIPT_ID,brd.DTL_ID,brd.RULES_NAME from BIS_RECEIPT_DTL brd where brd.DTL_ID=:id";
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        return listBySQLAliasToBean(sql, params, BisBankDtlBillVO.class);
    }

    /**
     * 先记账后付款待记账
     */
    public List<NisBillheadDTO> queryTallyUnionByAccountPay() {
        String sql = "select n.id as id,n.pay_Money as payMoney,n.corp_id as corpId " +
                " from Nis_Billhead n left join Fc_Voucher_Type f on  n.voucher_Type_Id = f.id" +
                " left join Nis_Billhead s on n.split_id = s.id" +
                " left join Nis_Bus_Type t on n.bus_Type = t.id" +
                " left join bt_bank_acc c on n.down_Acc_Id = c.id where 1=1" +
                " and n.status = " + NetbankConstant.STATUE_CHECK + " and f.is_account = '1' " +
                " and (n.tally_Sign ='' or n.tally_Sign ='0' or  n.tally_sign is null) " +
                " and not exists( select 1 from fc_tally_remind_dtl d where d.bill_id = n.id ) ";
        return listBySQLAliasToBean(sql, null, NisBillheadDTO.class);
    }

    /**
     * 先付款后记账待记账
     */
    public List<NisBillheadDTO> queryTallyByPayAccount() {
        String sql = "select * from (select nb.id as id,nb.pay_Money, nb.corp_id as corpId " +
                " from Nis_Billhead nb left join Fc_Voucher_Type f on  nb.voucher_Type_Id = f.id" +
                " left join Nis_Bus_Type t on nb.bus_Type = t.id" +
                " left join Bis_exc b on nb.id = b.cbs_bill_id and b.voucher_From = " + BdlinkConstant.FROM_NIS_PAY +
                " left join bt_bank_acc c on nb.down_Acc_Id = c.id where 1=1" +
                " and (b.voucher_stat = '" + BdlinkConstant.SUCCESS + "' or nb.is_payed = '" + NetbankConstant.ISPAYED_1 + "' ) " +
                //付款成功的才进行记账
                " and nb.status = " + NetbankConstant.STATUE_CHECK + " and f.is_account = '" + CommonConstant.YES + "' " +
                " and f.sys_voucher_code not in( " + NetbankConstant.PAY_TYPE_04 + "," + NetbankConstant.PAY_TYPE_05 + ")" +
                " union " +
                " select nb.id as id,nb.pay_Money , nb.corp_id as corpId " +
                " from Nis_Billhead nb left join Fc_Voucher_Type f on  nb.voucher_Type_Id = f.id" +
                " left join Nis_Bus_Type t on nb.bus_Type = t.id" +
                " left join bt_bank_acc c on nb.down_Acc_Id = c.id where 1=1" +
                //付款成功的才进行记账
                " and nb.status = " + NetbankConstant.STATUE_CHECK + " and f.is_account = '" + CommonConstant.YES + "' " +
                " and f.sys_voucher_code  in( " + NetbankConstant.PAY_TYPE_04 + "," + NetbankConstant.PAY_TYPE_05 + ")" +
                " ) n where 1 = 1 " +
                " and (n.tally_Sign ='' or n.tally_Sign ='0' or  n.tally_sign is null) " +
                " and not exists( select 1 from fc_tally_remind_dtl d where d.bill_id = n.id ) ";
        return listBySQLAliasToBean(sql, null, NisBillheadDTO.class);
    }

    /**
     * 带原状态更新付款单
     *
     * @param vo
     * @param oldStatus
     * @return
     */
    public int updateNisBillheadStatus(NisBillheadVO vo, Integer[] oldStatus) {
        StringBuilder hql = new StringBuilder();
        hql.append(" update NisBillhead set corpId=:corpId,voucherTypeId=:voucherTypeId,accId=:accId,payerAcc=:payerAcc,payerAccName=:payerAccName,");
        hql.append(" payerBank=:payerBank,downAccId=:downAccId,curId=:curId,payMoney=:payMoney,payeeAcc=:payeeAcc,");
        hql.append(" payeeAccName=:payeeAccName,payeeBankCode=:payeeBankCode,payeeBank=:payeeBank,bankTypeId=:bankTypeId,");
        hql.append(" prov=:prov,city=:city,wishPayday=:wishPayday,isQuick=:isQuick,isForindividual=:isForindividual,bifCode=:bifCode,");
        hql.append(" difBank=:difBank,areaSign=:areaSign,bursarBillNo=:bursarBillNo,purpose=:purpose,addWord=:addWord,directPayFlag=:directPayFlag,");
        hql.append(" caInfoId=:caInfoId,tallyDate=:tallyDate,orderNo=:orderNo,voucherNo=:voucherNo,erpId=:erpId,busType=:busType,shareBusTypeId=:shareBusTypeId,");
        hql.append(" billFrom=:billFrom,splitId=:splitId,origSplitId=:origSplitId,mergeId=:mergeId,status=:status,attachmentId=:attachmentId,");
        hql.append(" isPayed=:isPayed,innerSign=:innerSign,tallySign=:tallySign,isAdvance=:isAdvance,workCapitalRatio=:workCapitalRatio,");
        hql.append(" takeBackDate=:takeBackDate,isOpenBill=:isOpenBill,bankSerialNo=:bankSerialNo,isAutoPay=:isAutoPay,policyId=:policyId,");
        hql.append(" billType=:billType,billFlag=:billFlag,busCorpId=:busCorpId,busPersonnel=:busPersonnel,timeStamp=:timeStamp,voucherStat=:voucherStat,rmk=:rmk,");
        hql.append(" scfTransferAmt=:scfTransferAmt");
        Map<String, Object> params = new HashMap<>();
        params.put("rmk", vo.getRmk());
        params.put("scfTransferAmt", vo.getScfTransferAmt());
        params.put("corpId", vo.getCorpId());
        params.put("voucherTypeId", vo.getVoucherTypeId());
        params.put("voucherStat", vo.getVoucherStat());
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
        params.put("shareBusTypeId", vo.getShareBusTypeId());
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
        if (vo.getApproveDate() != null) {
            hql.append(",approve_Date=:approveDate ");
            params.put("approveDate", vo.getApproveDate());
        }
        if (ObjectUtil.isNotEmpty(vo.getSharingDate())){
            hql.append(",sharingDate=:sharingDate ");
            params.put("sharingDate", vo.getSharingDate());
        }
        hql.append(" where id=:id and status in (:oldStatus) ");
        params.put("id", vo.getId());
        params.put("oldStatus", oldStatus);
        return this.batchExecute(hql.toString(), params);
    }

    /**
     * 获取账户在途余额
     *
     * @param account
     * @param curId
     * @return
     */
    public BigDecimal getUseBal(String account, String curId) {
        String sql = "select sum(pay_money) as payMoney from nis_billhead n" + " left join fc_voucher_type t on n.voucher_type_id=t.id" +
                " where n.payer_acc=:payerAcc and n.cur_id=:curId " +
                " and n.status>=" + NetbankConstant.STATUE_BACK +
                " and ((t.is_account = '1' and (n.tally_sign <> '1' or n.tally_sign is null or n.tally_sign='' or n.status<=" + NetbankConstant.STATUE_CHECK + "))" +
                " or (t.is_account = '0' and n.status<" + NetbankConstant.STATUE_CHECK + "))";
        HashMap params = new HashMap();
        params.put("payerAcc", account);
        params.put("curId", curId);
        Object payMoney = this.createSQLQuery(sql, params).uniqueResult();
        BigDecimal userMoney = BigDecimal.ZERO;
        if (!EmptyUtils.isEmpty(payMoney)) {
            userMoney = (BigDecimal) payMoney;
        }
        return userMoney;
    }

    /**
     * 获取ERP待处理单据
     *
     * @return
     */
    public List<NisBillheadVO> listErpTodoBill() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("billFrom", NetbankConstant.BILLFROM_6);
        params.put("status", NetbankConstant.STATUE_TEMP);
        return listBySQLAliasToBean(this.getCommonSql() + " and n.bill_From=:billFrom and n.status=:status", params, NisBillheadVO.class);
    }

    /**
     * 根据付款ID获取付款信息
     *
     * @param id
     * @return
     */
    public List<NisBillheadVO> listByIdUnion(String id) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        if (StringUtils.isBlank(id)) {
            return null;
        } else {
            String[] ids = id.split(CommonConstant.COMMA_SPLIT);
            sql.append(" and " + SqlUtil.getInSql(Arrays.asList(ids), "n.id", true));
        }
        return listBySQLAliasToBean(sql.toString(), null, NisBillheadVO.class);
    }

    /**
     * 根据付款ID获取付款信息
     *
     * @param ids
     * @return
     */
    public List<NisBillheadVO> listByIdsForApprove(List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append("select n.id as id,n.bill_No as billNo,n.corp_Id as corpId,n.voucher_Type_Id as voucherTypeId,f.sys_Voucher_Code as sysVoucherCode,");
        sql.append(" n.payer_Acc as payerAcc,n.cur_Id as curId,n.pay_Money as payMoney");
        sql.append(" from Nis_Billhead n left join Fc_Voucher_Type f on  n.voucher_Type_Id = f.id");
        if (EmptyUtils.isEmpty(ids)) {
            return null;
        } else {
            sql.append(" where " + SqlUtil.getInSql(ids, "n.id", true));
        }
        return listBySQLAliasToBean(sql.toString(), null, NisBillheadVO.class);
    }


    public List<NisBillheadVO> listByErpIds(List<String> erpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from Nis_Billhead n ");
        if (EmptyUtils.isEmpty(erpIds)) {
            return null;
        } else {
            sql.append(" where " + SqlUtil.getInSql(erpIds, "n.erp_id", true));
        }
        return listBySQLAliasToBean(sql.toString(), null, NisBillheadVO.class);
    }

    public List<NisBillheadDTO> getCmsInlSendId(List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append("select n.acc_id as accId,n.erp_id as erpId,n.id as id ");
        sql.append(" from Nis_Billhead n ");
        if (EmptyUtils.isEmpty(ids)) {
            return new ArrayList<>();
        } else {
            sql.append(" where " + SqlUtil.getInSql(ids, "n.erp_id", true));
        }
        return listBySQLAliasToBean(sql.toString(), null, NisBillheadDTO.class);
    }

    @SuppressWarnings("unchecked")
    public List<String> listBillId(List<String> listId) {
        String sql = "select bn.bill_id as billId,bn.pay_id as payId from nis_bill b " +
                " LEFT JOIN nis_billhead n on b.pay_id=n.id " +
                " LEFT JOIN nis_bill bn on b.pay_id=bn.pay_id " +
                " LEFT JOIN fc_voucher_type t on t.id=n.voucher_type_id " +
                " where t.sys_voucher_code=:sysEndorsePayType and n.status=:status and b.bill_id in(:listIds) ";
        Map<String, Object> params = new HashMap<>();
        params.put("sysEndorsePayType", NetbankConstant.PAY_TYPE_50);
        params.put("status", NetbankConstant.STATUE_CHECK);
        params.put("listIds", listId);
        List<NisBill> list=listBySQLAliasToBean(sql,params,NisBill.class);
        List<String> reList=new ArrayList<>();
        if(CollectionUtil.isNotEmpty(list)){
            list.forEach(info->{
                reList.add(StrUtil.builder(info.getBillId(),",",info.getPayId()).toString());
            });
        }
        return reList;
//        return this.getSession().createSQLQuery(sql).setProperties(params).list();
    }

    @SuppressWarnings("unchecked")
    public List<String> listPayIdByBill(List<String> listId) {
        String sql = "select n.id as payId from nis_billhead n LEFT JOIN fc_voucher_type t on t.id=n.voucher_type_id " +
                " where t.sys_voucher_code=:sysBackType and n.status in (:status) and n.id in (:listIds)";
        Map<String, Object> params = new HashMap<>();
        params.put("sysBackType", NetbankConstant.PAY_TYPE_49);
        params.put("status", new Integer[]{NetbankConstant.STATUE_BILL_COMPLETE,NetbankConstant.STATUE_CHECK});
        params.put("listIds", listId);
        List<String>  payIds = this.getSession().createSQLQuery(sql).setProperties(params).list();
        return payIds;
    }

    public int updateEditPassSign(String id, String oriTallySign) {
        String sb = " update nis_billhead set tally_sign = :tallySign " +
                " where 1 = 1 and id = :id and tally_sign = :oriTallySign  ";
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("oriTallySign", oriTallySign);
        param.put("tallySign", "3");
        return createSQLQuery(sb, param).executeUpdate();
    }

    /**
     * 更新付款单记账信息
     */
    public int updateAutoCheckSign(String id, String orderNo, String tallyCode, String voucherNo, Integer status) {
        String sb = " update nis_billhead set order_no = :orderNo, tally_Code = :tallyCode, " +
                " voucher_no = :voucherNo, status = :status, tally_sign = :tallySign " +
                " where 1 = 1  " +
                " and (tally_sign = '0' or tally_sign = '' or tally_sign is null ) " +
                " and id = :id  ";
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("orderNo", orderNo);
        param.put("tallyCode", tallyCode);
        param.put("voucherNo", voucherNo);
        param.put("status", status);
        param.put("tallySign", "1");
        return createSQLQuery(sb, param).executeUpdate();
    }

    public int updateTallySign(String id, String orderNo, String tallyCode) {
        String sb = " update nis_billhead set order_no = :orderNo, tally_Code = :tallyCode, tally_Sign = :tallySign " +
                " where 1 = 1  " +
                " and (tally_sign = '0' or tally_sign = '' or tally_sign is null ) " +
                " and id = :id  ";
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("orderNo", orderNo);
        param.put("tallyCode", tallyCode);
        param.put("tallySign", "3");
        return createSQLQuery(sb, param).executeUpdate();
    }

    public int updateCheckSign(String id, String voucherNo, Integer status) {
        String sb = " update nis_billhead set voucher_no = :voucherNo , status = :status, tally_sign = :tallySign " +
                " where 1 = 1 and id = :id and tally_sign = '3' ";
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("voucherNo", voucherNo);
        param.put("status", status);
        param.put("tallySign", "1");
        return createSQLQuery(sb, param).executeUpdate();
    }

    public int updateEditBackSign(String id, String oriTallySign) {
        String sb = " update nis_billhead set order_no = null, tally_Code = null, tally_Sign = :tallySign " +
                " where 1 = 1 and id = :id and tally_sign = :oriTallySign  ";
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("oriTallySign", oriTallySign);
        param.put("tallySign", "0");
        return createSQLQuery(sb, param).executeUpdate();
    }

    public int updateCheckBackSign(String id, String voucherNo) {
        String sb = " update nis_billhead set tally_sign = :tallySign " +
                " where 1 = 1 and id = :id and tally_sign = '3' ";
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("voucherNo", voucherNo);
        param.put("tallySign", "4");
        return createSQLQuery(sb, param).executeUpdate();
    }

    /**
     * 更新交易状态
     *
     * @return
     */
    public int updateVoucherState(String id, String voucherStat) {
        String sb = " update nis_billhead set voucher_stat = :voucherStat " +
                " where  id = :id";
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("voucherStat", voucherStat);
        return createSQLQuery(sb, param).executeUpdate();
    }

    public NisBillhead getNisBillheadVOByVoucherNo(String voucherNo) {
        StringBuilder sql = new StringBuilder("select * from nis_billhead n");
        sql.append(" where n.voucher_No=:voucherNo");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("voucherNo", voucherNo);
        return getBySQLAliasToBean(sql.toString(), params, NisBillhead.class);
    }

    /**
     * 插入中间关联表
     *
     * @param billId
     * @param isFrom
     * @param bankSerialNo
     */
    public void insetBillBankSerialNo(String billId, String isFrom, String bankSerialNo, String dtlId) {
        String sql = " insert into NIS_BILL_BANK_SERIAL_NO(bill_id,BANK_SERIAL_NO,IS_FROM,DTL_ID) values(:billId,:bankSerialNo,:isFrom,:dtlId)";
        HashMap<String, Object> params = new HashMap<>();
        params.put("billId", billId);
        params.put("bankSerialNo", bankSerialNo);
        params.put("isFrom", isFrom);
        params.put("dtlId", dtlId);
        this.getSession().createSQLQuery(sql).setProperties(params).executeUpdate();
    }

    /**
     * 删除中间关联表
     *
     * @param dtlId
     */
    public void deleteBillBankSerialNoByDtlId(String dtlId) {
        String sql = " delete from NIS_BILL_BANK_SERIAL_NO where   DTL_ID=:dtlId ";
        HashMap<String, Object> params = new HashMap<>();
        params.put("dtlId", dtlId);
        this.getSession().createSQLQuery(sql).setProperties(params).executeUpdate();
    }

    /**
     * 删除中间关联表
     *
     * @param billId
     */
    public void deleteBillBankSerialNoByBillId(String billId) {
        String sql = " delete from NIS_BILL_BANK_SERIAL_NO where   bill_id=:billId ";
        HashMap<String, Object> params = new HashMap<>();
        params.put("billId", billId);
        this.getSession().createSQLQuery(sql).setProperties(params).executeUpdate();
    }

    /**
     * 删除退款勾兑中间关联表
     *
     * @param billId
     */
    public void deleteBillBankSerialNoRefundByBillId(String billId) {
        String sql = " delete from NIS_BILL_BANK_SERIAL_NO_REFUND where   bill_id=:billId ";
        HashMap<String, Object> params = new HashMap<>();
        params.put("billId", billId);
        this.getSession().createSQLQuery(sql).setProperties(params).executeUpdate();
    }

    /**
     * 根据业务单据号得到流水id
     *
     * @param billId
     * @return
     */
    public List<String> getDtlId(String billId) {
        String sql = " select  DTL_ID from NIS_BILL_BANK_SERIAL_NO where  bill_id=:billId ";
        HashMap<String, Object> params = new HashMap<>();
        params.put("billId", billId);
        return this.getSession().createSQLQuery(sql).setProperties(params).list();
    }

    /**
     * 根据流水id获取业务单号
     *
     * @param dtlId
     * @return
     */
    public List<String> getBillId(String dtlId) {
        String sql = " select  bill_id from NIS_BILL_BANK_SERIAL_NO where  DTL_ID=:dtlId ";
        HashMap<String, Object> params = new HashMap<>();
        params.put("dtlId", dtlId);
        return this.getSession().createSQLQuery(sql).setProperties(params).list();
    }

    /**
     * 付款方式为‘代付’的付款执行自动发送
     * @return
     */
    public List<NisBillheadVO> listByAutoSendPaymentJob() {
        StringBuilder sql = new StringBuilder();
        sql.append("select n.id as id,n.bill_No as billNo,n.corp_Id as corpId,n.voucher_Type_Id as voucherTypeId,f.voucher_Name as voucherName,f.sys_Voucher_Code as sysVoucherCode,f.is_Account as isAccount,");
        sql.append(" n.acc_Id as accId,n.payer_Acc as payerAcc,n.payer_Acc_Name as payerAccName,n.payer_Bank as payerBank,n.down_Acc_Id as downAccId,");
        sql.append(" n.cur_Id as curId,n.pay_Money as payMoney,n.payee_Acc as payeeAcc,n.payee_Acc_Name as payeeAccName,n.payee_Bank_Code as payeeBankCode,");
        sql.append(" n.payee_Bank as payeeBank,n.prov as prov,n.city as city,n.wish_Payday as wishPayday,n.is_Quick as isQuick,n.is_Forindividual as isForindividual,");
        sql.append(" n.bif_Code as bifCode,n.dif_Bank as difBank,n.area_Sign as areaSign,n.bursar_Bill_No as bursarBillNo,n.purpose as purpose,n.add_Word as addWord,n.order_No as orderNo,n.voucher_No as voucherNo,");
        sql.append(" n.direct_Pay_Flag as directPayFlag,n.ca_Info_Id as caInfoId,n.tally_Date as tallyDate,n.erp_Id as erpId,n.tally_sign as tallySign,");
        sql.append(" t.id as busTypeId,n.bus_Type as busType,t.bus_Type_Name as busTypeName,n.orig_split_id as origSplitId,n.bus_Corp_Id as busCorpId,n.bus_Personnel as busPersonnel,");
        sql.append(" n.bill_From as billFrom,n.split_Id as splitId,n.merge_Id as mergeId,n.status as status,n.is_Auto_Pay as isAutoPay,");
        sql.append(" n.create_By as create_By,n.create_Date as createDate,n.update_By as updateBy,n.update_Date as updateDate,n.attachment_Id as attachmentId,n.is_payed as isPayed,");
        sql.append(" n.bill_money as billMoney, n.approve_Date as approveDate, ");
        sql.append(" n.is_Advance as isAdvance,n.work_Capital_Ratio as workCapitalRatio,n.take_Back_Date as takeBackDate,n.is_Open_Bill as isOpenBill,");
        // 增加收款人统一社会信用代码字段查询
        sql.append(" n.BL_RCVPYMTPS_UNN_ID_NO as blRcvpymtpsUnnIdNo,n.real_payer_acc_id as realPayerAccId, ");
        sql.append(" n.BILL_TYPE as billType,n.BILL_FLAG as billFlag ");
        sql.append(" from Nis_Billhead n left join Fc_Voucher_Type f on  n.voucher_Type_Id = f.id");
        sql.append(" left join Nis_Bus_Type t on n.bus_Type = t.id");
        sql.append(" where f.SYS_VOUCHER_CODE = '33' and (n.is_payed = '0' or n.is_payed is null) ");
        sql.append(" and n.status in (" + com.bytter.biz.payment.api.constant.NetbankConstant.STATUE_CHECK + ") and (n.voucher_no is null or n.voucher_no='') and (n.order_no is null or n.order_no='') ");
        sql.append(" and ").append("(n.voucher_stat is null or n.voucher_stat = '' or n.voucher_stat in ('" + BdlinkConstant.INVALIDATE + "', '" + BdlinkConstant.BACK + "', '" + BdlinkConstant.DELETE + "'))");
        return this.listBySQLAliasToBean(sql.toString(),null,NisBillheadVO.class);
    }

    public BigDecimal getCountNisBillHead(String corpId, String settlType, String beginMonth, String endMonth) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(n.id) from nis_billhead n left join fc_voucher_type t on n.voucher_type_id = t.id");
        sql.append(" where n.status >= ").append(NetbankConstant.STATUE_BACK);
        if(!EmptyUtils.isBlank(corpId)){
            sql.append(" and n.CORP_ID = '").append(corpId).append("' ");
        }
        if(!EmptyUtils.isBlank(settlType)){
            sql.append(" and t.SETTL_TYPE = ").append(settlType);
        }
        if(!EmptyUtils.isBlank(beginMonth)){
            sql.append(" and n.CREATE_DATE >= '").append(beginMonth).append("-01 00:00:00'");
        }
        if(!EmptyUtils.isBlank(endMonth)){
            sql.append(" and n.CREATE_DATE < '").append(endMonth).append("-01 00:00:00'");
        }
        HashMap params = new HashMap();
        Object count = this.createSQLQuery(sql.toString(), params).uniqueResult();
        BigDecimal userCount = BigDecimal.ZERO;
        if (!EmptyUtils.isEmpty(count)) {
            userCount = (BigDecimal) count;
        }
        return userCount;
    }

    /**
     * 更新付款单银行落地限额处理结果。
     */
    public void updateLandingLimitResult(String id, String landingLimitResult) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("landingLimitResult", landingLimitResult);
        batchSQLExecute("update NIS_BILLHEAD set LANDING_LIMIT_RESULT=:landingLimitResult where ID=:id", params);
    }

    /**
     * 翻转付款单银行落地限额处理结果（互斥：仅当落地标识仍为 0-待执行才更新，0/A/R 终态不可再翻）。
     *
     * @return 更新行数；0 = 落地标识已非 0（已被并发回调翻转收尾/未处于落地待执行），调用方应中止收尾
     */
    public int updateLandingLimitResultIfPending(String id, String landingLimitResult) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("landingLimitResult", landingLimitResult);
        return batchSQLExecute("update NIS_BILLHEAD set LANDING_LIMIT_RESULT=:landingLimitResult "
                + "where ID=:id and LANDING_LIMIT_RESULT='0'", params);
    }
}
