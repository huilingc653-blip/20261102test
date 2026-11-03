package com.bytter.biz.bdlinks.dao;

import cn.hutool.core.util.StrUtil;
import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.bdlinks.entity.BisExc;
import com.bytter.biz.bdlinks.vo.BisExcVO;
import com.bytter.biz.payment.api.constant.NisDemandConstant;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.workflow.api.IWorkFlowService;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
/**
 * @author onlineGenerator
 * @version 1.0
 * <p>
 * Bis_Exc b
 * bis_bif_init t
 * sys_corp c
 * bis_exc_proc b
 * bis_bif_init t
 * bt_bank_acc acc
 * nis_billhead n
 * Fc_Voucher_Type f
 * <p>
 * <p>
 * t.name as bifName,
 * t.id as bif_Code,
 * acc.corp_id as payerCorpId,
 * f.voucher_Name as voucherTypeName,
 * f.sys_Voucher_Code as sysVoucherCode,
 * n.bill_no as cbs_Bill_No,
 * <p>
 * <p>
 * alter table Bis_Exc  add  NAME varchar(30) DEFAULT '';
 * alter table Bis_Exc  add  bif_id varchar(32) DEFAULT '';
 * alter table Bis_Exc  add  corp_id varchar(32) DEFAULT '';//存在
 * alter table Bis_Exc  add  voucher_name varchar(20) DEFAULT '';
 * alter table Bis_Exc  add  sys_voucher_code varchar(2) DEFAULT '';
 * <p>
 * alter table bis_exc_proc  add  bif_id varchar(32) DEFAULT '';
 * alter table bis_exc_proc  add  corp_id varchar(32) DEFAULT '';//存在
 * alter table bis_exc_proc  add  NAME varchar(30) DEFAULT '';
 * alter table bis_exc_proc  add  bill_no varchar(25) DEFAULT '';
 * <p>
 * 银企直联域的表
 * @Title: Dao
 * @Description: BIS_EXC
 * @date 2017-06-28 16:31:56
 */
@Repository
public class BisExcDao extends BaseDao<BisExc, String> {
    /**
     * 付款指令表通用查@param sqlWhere
     *
     * @return
     */
    private String getCommonSql() {
        StringBuilder sbl = new StringBuilder();
        sbl.append(" select b.tenant_id as tenantId, b.id as id,b.voucher_No as voucherNo,b.cbs_Bill_Id as cbsBillId,b.cbs_Bill_No as cbsBillNo, ");
        sbl.append(" b.corp_Id as corpId,b.req_Date as reqDate,b.voucher_Type_Id as voucherTypeId,b.voucher_Name as voucherTypeName, ");
        sbl.append(" b.payer_Acc as payerAcc,b.payer_Acc_Name as payerAccName,b.payer_Bank as payerBank,b.payer_Prov as payerProv,b.payer_City as payerCity, ");
        sbl.append(" b.cur_Id as curId,b.pay_Money as payMoney,b.payee_Acc as payeeAcc,b.payee_Acc_Name as payeeAccName,b.payee_Bank_Code as payeeBankCode," );
        sbl.append(" b.payee_Bank as payeeBank,b.payee_Prov as payeeProv,b.payee_City as payeeCity,b.wish_Payday as wishPayday," );
        sbl.append(" b.is_Quick as isQuick,b.trans_Mode as transMode, b.bif_Code as bifCode,b.name as bifName,b.dif_Bank as difBank,");
        sbl.append(" b.area_Sign as areaSign,b.voucher_From as voucherFrom, b.voucher_From_Private as voucherFromPrivate,b.purpose as purpose, " );
        sbl.append(" b.add_Word as addWord, b.approve_Sign as approveSign,b.bus_Ori_Type as busOriType,b.cmd_Type as cmdType," );
        sbl.append(" b.bank_Serial_No as bankSerialNo, b.voucher_Stat as voucherStat,b.trans_Time as transTime,b.return_Time as returnTime, ");
        sbl.append(" b.first_Return_Msg as firstReturnMsg,b.next_Return_Msg as nextReturnMsg,b.status as status," );
        sbl.append(" b.policy_Id as policyId,b.tally_Sign as tallySign,b.tally_Code as tallyCode,b.bus_Global_Id as busGlobalId,");
        sbl.append(" b.create_By as createBy, b.create_Date as createDate, b.update_By as updateBy, b.update_Date as updateDate, ");
        sbl.append(" b.fail_Reason as failReason,b.resend_Sign as resendSign, ");
        sbl.append(" b.counter_Acc as counterAcc,b.notice_Sign as noticeSign,b.verify_Code as verifyCode,b.sys_Voucher_Code as sysVoucherCode, ");
        sbl.append(" b.corp_id as payerCorpId, '0' as excFrom,b.delay_Time as delayTime from Bis_Exc b  where 1=1 ");
        return String.valueOf(sbl);
    }
    /**
     * 指令列表查询
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
        StringBuilder sql = new StringBuilder();
        String opType = (String) dataTablesInput.getFormdata().get("opType");
        if ("listTrace".equals(opType)) {
            String sqlExc = " select b.id,b.voucher_No,b.cbs_Bill_Id,b.cbs_Bill_No,b.corp_Id,b.req_Date,b.voucher_Type_Id," +
                    " b.payer_Acc,b.payer_Acc_Name,b.payer_Bank,b.payer_Prov,b.payer_City," +
                    " b.cur_Id,b.pay_Money,b.payee_Acc,b.payee_Acc_Name,b.payee_Bank_Code," +
                    " b.payee_Bank,b.payee_Prov,b.payee_City,b.wish_Payday,b.is_Quick,b.trans_Mode," +
                    " b.bif_Code,b.dif_Bank,b.area_Sign,b.voucher_From,b.purpose,b.add_Word," +
                    " b.approve_Sign,b.bus_Ori_Type,b.cmd_Type,b.bank_Serial_No,b.voucher_Stat,b.trans_Time,b.return_Time," +
                    " b.first_Return_Msg,b.next_Return_Msg," +
                    " b.create_By, b.create_Date, b.update_By, b.update_Date,b.fail_Reason,b.resend_Sign, " +
                    " '0' as excFrom,b.corp_id as payerCorpId" +
                    " from Bis_Exc b  where 1=1" +
                    joinSql;
            String sqlExcProc = "select b.id,b.voucher_No,b.cbs_Bill_Id,b.bill_no as cbs_Bill_No,b.corp_Id,b.req_Date,b.voucher_Type_Id," +
                    " b.payer_Acc,b.payer_Acc_Name,b.payer_Bank,b.payer_Prov,b.payer_City," +
                    " b.cur_Id,b.pay_Money,b.payee_Acc,b.payee_Acc_Name,b.payee_Bank_Code," +
                    " b.payee_Bank,b.payee_Prov,b.payee_City,b.wish_Payday,b.is_Quick,b.trans_Mode," +
                    " b.id as bif_Code,b.dif_Bank,b.area_Sign,b.voucher_From,b.purpose,b.add_Word," +
                    " b.approve_Sign,b.bus_Ori_Type,b.cmd_Type,b.bank_Serial_No,b.voucher_Stat,b.trans_Time,b.return_Time," +
                    " b.first_Return_Msg,b.next_Return_Msg," +
                    " b.create_By, b.create_Date, b.update_By, b.update_Date,'' as fail_Reason,'' as resend_Sign, " +
                    " '1' as excFrom,b.corp_id as payerCorpId" +
                    " from bis_exc_proc b " +//TODO 关联结算域的nis_billhead n
                    " where b.voucher_Stat = '" + BdlinkConstant.WAIT_RESULT + "'" +
                    joinSql;
            sql.append(" select b.id as id,b.voucher_No as voucherNo,b.cbs_Bill_Id as cbsBillId,b.cbs_Bill_No as cbsBillNo,b.corp_Id as corpId,b.req_Date as reqDate,b.voucher_Type_Id as voucherTypeId,");
            sql.append(" b.payer_Acc as payerAcc,b.payer_Acc_Name as payerAccName,b.payer_Bank as payerBank,b.payer_Prov as payerProv,b.payer_City as payerCity,");
            sql.append(" b.cur_Id as curId,b.pay_Money as payMoney,b.payee_Acc as payeeAcc,b.payee_Acc_Name as payeeAccName,b.payee_Bank_Code as payeeBankCode,");
            sql.append(" b.payee_Bank as payeeBank,b.payee_Prov as payeeProv,b.payee_City as payeeCity,b.wish_Payday as wishPayday,b.is_Quick as isQuick,b.trans_Mode as transMode,");
            sql.append(" b.bif_Code as bifCode,b.dif_Bank as difBank,b.area_Sign as areaSign,b.voucher_From as voucherFrom,b.purpose as purpose,b.add_Word as addWord,");
            sql.append(" b.approve_Sign as approveSign,b.bus_Ori_Type as busOriType,b.cmd_Type as cmdType,b.bank_Serial_No as bankSerialNo,b.voucher_Stat as voucherStat,b.trans_Time as transTime,b.return_Time as returnTime,");
            sql.append(" b.first_Return_Msg as firstReturnMsg,b.next_Return_Msg as nextReturnMsg,");
            sql.append(" b.create_By as createBy, b.create_Date as createDate, b.update_By as updateBy, b.update_Date as updateDate,b.fail_Reason as failReason,b.resend_Sign as resendSign, ");
            sql.append(" b.corp_id as payerCorpId,b.excFrom");
            sql.append(" from (");
            sql.append(sqlExc).append(" union all ").append(sqlExcProc);
            sql.append(") b where 1=1 ");
        } else {
            if ("listTally".equals(opType) || "listTallyChange".equals(opType) || "listTallyCheck".equals(opType)) {
                sql.append(this.getCommonSql());
            } else {
                sql.append(" select b.id as id,b.voucher_No as voucherNo,b.cbs_Bill_Id as cbsBillId,b.cbs_Bill_No as cbsBillNo,b.corp_Id as corpId,b.req_Date as reqDate,b.voucher_Type_Id as voucherTypeId,");
                sql.append(" b.payer_Acc as payerAcc,b.payer_Acc_Name as payerAccName,b.payer_Bank as payerBank,b.payer_Prov as payerProv,b.payer_City as payerCity,");
                sql.append(" b.cur_Id as curId,b.pay_Money as payMoney,b.payee_Acc as payeeAcc,b.payee_Acc_Name as payeeAccName,b.payee_Bank_Code as payeeBankCode,");
                sql.append(" b.payee_Bank as payeeBank,b.payee_Prov as payeeProv,b.payee_City as payeeCity,b.wish_Payday as wishPayday,b.is_Quick as isQuick,b.trans_Mode as transMode,");
                sql.append(" b.bif_Code as bifCode,b.dif_Bank as difBank,b.area_Sign as areaSign,b.voucher_From as voucherFrom,b.voucher_From_Private as voucherFromPrivate,b.purpose as purpose,b.add_Word as addWord,");
                sql.append(" b.approve_Sign as approveSign,b.bus_Ori_Type as busOriType,b.cmd_Type as cmdType,b.bank_Serial_No as bankSerialNo,b.voucher_Stat as voucherStat,b.trans_Time as transTime,b.return_Time as returnTime,");
                sql.append(" b.first_Return_Msg as firstReturnMsg,b.next_Return_Msg as nextReturnMsg,b.status as status,b.corp_id as payerCorpId");
                sql.append(" from Bis_Exc b where 1=1");
            }
            if (StringUtils.isNotEmpty(joinSql)) {
                sql.append(joinSql);
            }
        }
        //申请日期
        String gedReqDate = (String) dataTablesInput.getFormdata().get("ged_reqDate");
        String ledReqDate = (String) dataTablesInput.getFormdata().get("led_reqDate");
        logger.info("申请开始日期：" + gedReqDate);
        logger.info("申请结束日期：" + ledReqDate);
        if (StringUtils.isNotEmpty(gedReqDate)) {
            gedReqDate = gedReqDate + " 00:00:00";
            dataTablesInput.getFormdata().put("ged_reqDate", gedReqDate);
        }
        if (StringUtils.isNotEmpty(ledReqDate)) {
            ledReqDate = ledReqDate + " 23:59:59";
            dataTablesInput.getFormdata().put("led_reqDate", ledReqDate);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), BisExcVO.class, "b", false);
    }
    /**
     * 根据指定字段查询指令
     *
     * @param cbsBillId
     * @return
     */
    public BisExcVO getByCbsBillIdUnion(String cbsBillId) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("cbsBillId", cbsBillId);
        return getBySQLAliasToBean(this.getCommonSql() + " and b.cbs_Bill_Id=:cbsBillId", params, BisExcVO.class);
    }
    /**
     * 根据指定字段查询指令
     *
     * @param voucherNo
     * @return
     */
    public BisExcVO getByVoucherNo(String voucherNo) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("voucherNo", voucherNo);
        return getBySQLAliasToBean(this.getCommonSql() + " and b.voucher_No=:voucherNo", params, BisExcVO.class);
    }
    /**
     * 根据业务单号获取指令信息
     *
     * @param cbsBillNo
     * @param voucherFrom
     * @return
     */
    public BisExcVO getByCbsBillNo(String cbsBillNo, String voucherFrom) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("cbsBillNo", cbsBillNo);
        params.put("voucherFrom", voucherFrom);
        return getBySQLAliasToBean(this.getCommonSql() + " and b.cbs_Bill_No=:cbsBillNo and b.voucher_From=:voucherFrom", params, BisExcVO.class);
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
        if (StringUtils.isNotEmpty(businessSql)) {
            sql.append(" and ").append(businessSql);
        }
        String flowStatus = dataTablesInput.getParameter().get("flowStatus");
        if (IWorkFlowService.PASS.equals(flowStatus)) {
            //已审批列表由工作流结果驱动（我审批的），此时不再按单据状态过滤：
            //审批回调异常时单据状态可能仍停在待审批，但工作流确实已经审完了
            //未传工作流条件时（未挂接审批流的单位走自动审批，不产生工作流实例）仍按单据状态取数
            if (StringUtils.isEmpty(businessSql)) {
                sql.append(" and b.status in (").append(NetbankConstant.STATUE_CHECK)
                        .append(",").append(NetbankConstant.STATUE_BACK).append(")");
            }
        } else {
            //过滤已审数据
            sql.append(" and b.status<> ").append(NetbankConstant.STATUE_CHECK);
        }
        //申请日期
        String gedReqDate = (String) dataTablesInput.getFormdata().get("ged_reqDate");
        String ledReqDate = (String) dataTablesInput.getFormdata().get("led_reqDate");
        if (StringUtils.isNotEmpty(gedReqDate)) {
            gedReqDate = gedReqDate + " 00:00:00";
            dataTablesInput.getFormdata().put("ged_reqDate", gedReqDate);
        }
        if (StringUtils.isNotEmpty(ledReqDate)) {
            ledReqDate = ledReqDate + " 23:59:59";
            dataTablesInput.getFormdata().put("led_reqDate", ledReqDate);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), BisExcVO.class, "b", false);
    }
    /**
     * 根据ID获取指令
     *
     * @param id
     * @return
     */
    public BisExcVO getByIdUnion(String id) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(this.getCommonSql() + " and b.id=:id", params, BisExcVO.class);
    }
    /**
     * 根据ID获取指令
     *
     * @param id
     * @return
     */
    public BisExcVO getExcProcByIdUnion(String id) {
        String sqlExcProc = " select b.id,b.voucher_No,b.cbs_Bill_Id,b.bill_no as cbs_Bill_No,b.corp_Id,b.req_Date,b.voucher_Type_Id," +
                " b.payer_Acc,b.payer_Acc_Name,b.payer_Bank,b.payer_Prov,b.payer_City," +
                " b.cur_Id,b.pay_Money,b.payee_Acc,b.payee_Acc_Name,b.payee_Bank_Code," +
                " b.payee_Bank,b.payee_Prov,b.payee_City,b.wish_Payday,b.is_Quick,b.trans_Mode," +
                " b.bif_id as bif_Code,b.dif_Bank,b.area_Sign,b.voucher_From,b.purpose,b.add_Word," +
                " b.approve_Sign,b.bus_Ori_Type,b.cmd_Type,b.bank_Serial_No,b.voucher_Stat,b.trans_Time,b.return_Time," +
                " b.first_Return_Msg,b.next_Return_Msg,b.status,'' as policy_Id,'' as tally_Sign,'' as tally_Code,'' as bus_Global_Id," +
                " b.create_By, b.create_Date, b.update_By, b.update_Date,'' as fail_Reason,'' as resend_Sign, " +
                " b.counter_Acc,'' as notice_Sign,'' as verify_Code,'1' as excFrom" +
                " from bis_exc_proc b " +
                " where b.id=:id";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sqlExcProc, params, BisExcVO.class);
    }
    /**
     * 根据交易状态查询指令集合
     *
     * @param voucherStat 指令状态
     * @param joinSql     其他条件
     * @return
     */
    public List<BisExcVO> listByVoucherStat(String voucherStat, String joinSql) {
        StringBuilder sql = new StringBuilder(this.getCommonSql());
        if (BdlinkConstant.BANK_FAILED.equals(voucherStat) || BdlinkConstant.INTERFACE_FAILED.equals(voucherStat)) {
            sql.append(" and b.voucher_Stat in ('" + BdlinkConstant.BANK_FAILED + "','" + BdlinkConstant.INTERFACE_FAILED + "')");
        } else {
            sql.append(" and b.voucher_Stat in ('" + voucherStat + "')");
        }
        sql.append(joinSql);
        return listBySQLAliasToBean(sql.toString(), null, BisExcVO.class);
    }

    /**
     * 根据交易状态查询指令集合
     *
     * @return
     */
    public List<BisExcVO> listByVoucherStatForSuccessJob() {
        StringBuilder sbl = new StringBuilder();
        sbl.append(" select b.id as id,b.voucher_No as voucherNo,b.cbs_Bill_Id as cbsBillId,b.cbs_Bill_No as cbsBillNo, ");
        sbl.append(" b.corp_Id as corpId,b.req_Date as reqDate,b.voucher_Type_Id as voucherTypeId,b.voucher_Name as voucherTypeName, ");
        sbl.append(" b.payer_Acc as payerAcc,b.payer_Acc_Name as payerAccName,b.payer_Bank as payerBank,b.payer_Prov as payerProv,b.payer_City as payerCity, ");
        sbl.append(" b.cur_Id as curId,b.pay_Money as payMoney,b.payee_Acc as payeeAcc,b.payee_Acc_Name as payeeAccName,b.payee_Bank_Code as payeeBankCode," );
        sbl.append(" b.payee_Bank as payeeBank,b.payee_Prov as payeeProv,b.payee_City as payeeCity,b.wish_Payday as wishPayday," );
        sbl.append(" b.is_Quick as isQuick,b.trans_Mode as transMode, b.bif_Code as bifCode,b.name as bifName,b.dif_Bank as difBank,");
        sbl.append(" b.area_Sign as areaSign,b.voucher_From as voucherFrom, b.voucher_From_Private as voucherFromPrivate,b.purpose as purpose, " );
        sbl.append(" b.add_Word as addWord, b.approve_Sign as approveSign,b.bus_Ori_Type as busOriType,b.cmd_Type as cmdType," );
        sbl.append(" b.bank_Serial_No as bankSerialNo, b.voucher_Stat as voucherStat,b.trans_Time as transTime,b.return_Time as returnTime, ");
        sbl.append(" b.first_Return_Msg as firstReturnMsg,b.next_Return_Msg as nextReturnMsg,b.status as status," );
        sbl.append(" b.policy_Id as policyId,b.tally_Sign as tallySign,b.tally_Code as tallyCode,b.bus_Global_Id as busGlobalId,");
        sbl.append(" b.create_By as createBy, b.create_Date as createDate, b.update_By as updateBy, b.update_Date as updateDate, ");
        sbl.append(" b.fail_Reason as failReason,b.resend_Sign as resendSign, ");
        sbl.append(" b.counter_Acc as counterAcc,b.notice_Sign as noticeSign,b.verify_Code as verifyCode,b.sys_Voucher_Code as sysVoucherCode, ");
        sbl.append(" b.corp_id as payerCorpId, '0' as excFrom,b.delay_Time as delayTime,n.op_code as opCode,n.bursar_bill_no as bursarBillNo    ");
        sbl.append(" from Bis_Exc b left join nis_billhead n on b.cbs_bill_id = n.id where 1=1 ");
        sbl.append(" and b.voucher_Stat in ('" + com.bytter.biz.payment.api.constant.BdlinkConstant.SUCCESS + "')");
        sbl.append(" and (b.notice_sign <> '1' OR b.notice_sign is null or b.notice_sign='') ");
        sbl.append(" and n.bill_from = '"+NetbankConstant.BILLFROM_6+"' ");
        return listBySQLAliasToBean(sbl.toString(), null, BisExcVO.class);
    }

    /**
     * 查询资金需求下拨结果集合，未发送提醒的
     * @return
     */
    public List<BisExcVO> listNisDemandDialExcResult() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT n.bill_no cbsBillNo,n.id cbsBillId,b.PAYER_ACC_NAME payerAccName,b.PAYER_ACC payerAcc,b.PAYER_BANK payerBank, ");
        sql.append(" b.PAYEE_ACC_NAME payeeAccName,b.PAYEE_ACC payeeAcc,b.PAYEE_BANK payeeBank,b.PAY_MONEY payMoney,b.VOUCHER_STAT voucherStat,");
        sql.append(" BT_NVL_CHAR (b.NEXT_RETURN_MSG,b.FIRST_RETURN_MSG) nextReturnMsg ,n.demand_type demandType,b.id,n.corp_id corpId ");
        sql.append(" FROM Bis_Exc b  ");
        sql.append(" join nis_demand n on b.cbs_bill_id = n.id ");
        sql.append(" where b.voucher_Stat in (:voucherStat) and b.voucher_from = :voucherFrom ");
        sql.append(" and b.notice_sign <> '1' and n.demand_date >= :demandDateBegin and n.demand_date <= :demandDateEnd ");

        // 指令最终状态
        List<String> voucherStat = new ArrayList<String>();
        voucherStat.add(BdlinkConstant.BANK_FAILED);
        voucherStat.add(BdlinkConstant.INTERFACE_FAILED);
        voucherStat.add(BdlinkConstant.SUCCESS);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("voucherStat", voucherStat);
        params.put("voucherFrom",com.bytter.biz.payment.api.constant.BdlinkConstant.FROM_NIS_DEMAND);
        params.put("demandDateBegin",DateUtils.getIntegralStartTime(new Date()));
        params.put("demandDateEnd", DateUtils.getIntegralEndTime(new Date()));

        return listBySQLAliasToBean(sql.toString(), params, BisExcVO.class);
    }

    /**
     * 根据失败交易状态查询指令集合-外付网银
     * @return
     */
    public List<BisExcVO> listByVoucherStatForPaymentFailJob(String beginDate) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select b.id as id,b.voucher_No as voucherNo,b.cbs_Bill_Id as cbsBillId,b.cbs_Bill_No as cbsBillNo, ");
        sql.append(" b.payer_Acc_Name AS payerAccName,b.pay_Money AS payMoney,b.payee_Acc_Name AS payeeAccName, ");
        sql.append(" BT_NVL_CHAR(b.NEXT_RETURN_MSG,b.FIRST_RETURN_MSG) AS failReason,n.create_By as createBy,b.corp_id corpId, ");
        sql.append(" c.name corpName ");
        sql.append(" from Bis_Exc b ");
        sql.append(" left join nis_billhead n on b.cbs_bill_id = n.id ");
        sql.append(" left join fc_voucher_type fvt on b.VOUCHER_TYPE_ID = fvt.ID  ");
        sql.append(" left join sys_corp c on b.corp_id = c.id ");
        sql.append(" where b.voucher_Stat in ('" + BdlinkConstant.BANK_FAILED + "','" + BdlinkConstant.INTERFACE_FAILED + "')");
        sql.append(" and fvt.SYS_VOUCHER_CODE = '"+NetbankConstant.PAY_TYPE_34+"' ");
        if(StringUtils.isNotBlank(beginDate)){
            sql.append(" and b.REQ_DATE >= '").append(beginDate).append(" 00:00:00'");
        }
        return listBySQLAliasToBean(sql.toString(), null, BisExcVO.class);
    }


    /**
     * 重复付款校验
     *
     * @param corpId     单位
     * @param payMoney   金额
     * @param payeeAcc   收款账号
     * @param id         单据号（编辑时校验需要）
     * @param wishPayday 交易日期
     * @return
     */
    public List<BisExc> listByRePay(String corpId, BigDecimal payMoney, String payeeAcc, String curId, String id, Date wishPayday, String voucherStat, String ids, String type) {
        StringBuilder hql = new StringBuilder();
        HashMap<String, Object> params = new HashMap<String, Object>();
        hql.append(" from BisExc b where b.corpId = :corpId and b.curId = :curId and b.payMoney = :payMoney and b.payeeAcc = :payeeAcc and b.wishPayday = :wishPayday ");
        params.put("corpId", corpId);
        params.put("curId", curId);
        params.put("payMoney", payMoney);
        params.put("payeeAcc", payeeAcc);
        params.put("wishPayday", wishPayday);
        if (!EmptyUtils.isBlank(voucherStat)) {
            hql.append("and b.voucherStat in (").append(voucherStat).append(")");
        } else {
            if ("trace".equalsIgnoreCase(type)) {
                hql.append("and b.voucherStat not in ('").append(BdlinkConstant.INVALIDATE).append("','").append(BdlinkConstant.DELETE).append("','").append(BdlinkConstant.BACK).append("','").append(BdlinkConstant.BANK_FAILED).append("','").append(BdlinkConstant.INTERFACE_FAILED).append("')");
            } else {
                hql.append("and b.voucherStat not in ('").append(BdlinkConstant.INVALIDATE).append("','").append(BdlinkConstant.DELETE).append("','").append(BdlinkConstant.BACK).append("','").append(BdlinkConstant.WAIT_FILL).append("')");
            }
        }
        if (!EmptyUtils.isBlank(ids)) {
            hql.append(" and ").append(SqlUtil.getInSql(ids.split(CommonConstant.COMMA_SPLIT), "b.id", true));
        }
        if (!StringUtils.isBlank(id)) {
            hql.append(" and b.id <> :id ");
            params.put("id", id);
        }
        return (List<BisExc>) this.getSession().createQuery(hql.toString()).setProperties(params).list();
    }
    /**
     * 根据业务单号获取指令信息（代发工资）
     *
     * @param cbsBillNo
     * @param voucherFrom
     * @return
     */
    public List<BisExcVO> getBisExcByCbsBillNoForNisWage(String cbsBillNo, String voucherFrom) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("cbsBillNo", cbsBillNo);
        params.put("voucherFrom", voucherFrom);
        return this.listBySQLAliasToBean(this.getCommonSql() + " and b.cbs_Bill_No=:cbsBillNo and b.voucher_From=:voucherFrom", params, BisExcVO.class);
    }
    /**
     * 根据单位统计付款指令
     *
     * @param corpId
     * @return
     */
    public int getBisExcCountByCorpId(String corpId, String voucherFrom) {
        String sql = "select count(*) from bis_exc where corp_id=:corpId";
        HashMap<String, Object> params = new HashMap<>();
        params.put("corpId", corpId);
        if (BdlinkConstant.FROM_BIS_EXC_PAY.equals(voucherFrom)) {
            //0-暂存、11-待审批、-1-审批打回
            sql += " and status in ('0','11','-1')";
        } else {
            //6-待发送、3-准备发送、1-等待结果、2-银行处理失败、9-接口处理失败
            sql += " and voucher_stat in ('1','3','6','2','9')";
        }
        return ((Number) this.createSQLQuery(sql, params).uniqueResult()).intValue();
    }
    /**
     * 根据单位统计付款指令(资金调拨)
     *
     * @param corpId
     * @return nis_allocation_req c  结算域的表，只做关联查询 ，不用管
     *///通过服务调用
    public int getBisExcCountByCorpIdForAllocation(String corpId) {
        //nis_allocation_req属于结算域，调用结算查询//TODO 确保业务上 插入b.cbs_bill_id都不为空
        String sql = "select count(*) from bis_exc b  where b.corp_id=:corpId and b.cbs_bill_id is not null ";
        HashMap<String, Object> params = new HashMap<>();
        params.put("corpId", corpId);
        //6-待发送、3-准备发送、1-等待结果、2-银行处理失败、9-接口处理失败
        sql += " and b.voucher_stat in ('1','3','6','2','9')";
        sql += " and b.voucher_from in ('2','3')";//请款，下拨
        return ((Number) this.createSQLQuery(sql, params).uniqueResult()).intValue();
    }
    /**
     * 查询待记账数据
     * Fc_Voucher_Type f 这个表只做关联查询，不用管
     * fc_tally_remind_dtl 核算接口
     */
    public List<BisExcDTO> queryTallyBisEx() {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = " select b.id as id,b.pay_Money as payMoney,b.corp_id as corpId " +
                " from Bis_Exc b  where 1=1" +
                " and ((b.voucher_From = '" + BdlinkConstant.FROM_BIS_EXC_PAY + "' and b.bus_ori_type= '" + BdlinkConstant.BUSORITYPE_48 + "') " +
                " or (b.voucher_From = '" + BdlinkConstant.FROM_ALLOCATION_TYPE_AUTO_DOWN + "' and b.bus_ori_type= '" + BdlinkConstant.BUSORITYPE_33 + "')) " +
                " and b.voucher_Stat ='" + BdlinkConstant.SUCCESS + "' " +
                " and (b.tally_Sign ='' or b.tally_Sign ='0' or b.tally_Sign ='6' or  b.tally_sign is null)" +
                " and not exists( select 1 from fc_tally_remind_dtl d where d.bill_id = b.id ) ";//服务调用核算
        return listBySQLAliasToBean(sql, params, BisExcDTO.class);
    }
    /**
     * 指令状态更新（针对指令发送、指令退回、指令重发、指令作废等操作，带状态更新指令）
     *
     * @param bisExc
     * @param voucherStat
     * @return
     */
    public int updateBisExcVoucherStat(BisExc bisExc, String voucherStat) {
        if (bisExc == null || StringUtils.isBlank(voucherStat)) {
            return 0;
        }
        StringBuilder hql = new StringBuilder("update BisExc set voucherStat = :voucherStat");
        Map<String, Object> params = new HashMap<>();
        if (BdlinkConstant.DELETE.equals(voucherStat)) {
            //指令重发
            hql.append(",cbsBillId=:cbsBillId,cbsBillNo=:cbsBillNo");
            if (EmptyUtils.isBlank(bisExc.getCbsBillId())) {
                //业务ID为空，则取指令本身的ID为业务ID
                params.put("cbsBillId", bisExc.getId() + "_D");
                params.put("cbsBillNo", bisExc.getVoucherNo() + "_D");
            } else {
                params.put("cbsBillId", bisExc.getCbsBillId() + "_D");
                params.put("cbsBillNo", bisExc.getCbsBillNo() + "_D");
            }
        } else if (BdlinkConstant.BACK.equals(voucherStat)) {
            //指令退回
            if (BdlinkConstant.FROM_BIS_EXC_PAY.equals(bisExc.getVoucherFrom())) {
                hql.append(",status=:status");
                //自营付款指令退回修改状态为打回
                params.put("status", BdlinkConstant.STATUE_BACK);
            }
        } else if (BdlinkConstant.PRE_SEND.equals(voucherStat)) {
            //指令发送，状态从w-待发送更新为3-准备发送
            hql.append(",reqDate=:reqDate,verifyCode=:verifyCode");
            //更新指令申请日期
            params.put("reqDate", bisExc.getReqDate());
            //同步更新加密信息
            params.put("verifyCode", bisExc.getVerifyCode());
        }
        hql.append(" where id=:id");
        if (BdlinkConstant.PRE_SEND.equals(voucherStat)) {
            //指令发送，状态从w-待发送更新为3-准备发送
            hql.append(" and voucherStat = '" + BdlinkConstant.WAIT_FILL + "'");
        }
        if (BdlinkConstant.DELETE.equals(voucherStat) || BdlinkConstant.INVALIDATE.equals(voucherStat) || BdlinkConstant.WAIT_RESULT.equals(voucherStat)) {
            //指令重发/作废，更新状态控制必须是9-接口处理失败或者2-银行处理失败
            hql.append(" and voucherStat in ('" + BdlinkConstant.INTERFACE_FAILED + "','" + BdlinkConstant.BANK_FAILED + "')");
        }
        if (BdlinkConstant.BACK.equals(voucherStat)) {
            //指令退回，更新状态控制必须是6-待发送或者9-接口处理失败或者2-银行处理失败T-可疑状态
            hql.append(" and voucherStat in ('" + BdlinkConstant.WAIT_FILL + "','" + BdlinkConstant.INTERFACE_FAILED + "','" + BdlinkConstant.BANK_FAILED + "','" + BdlinkConstant.SUSPECTED + "')");
        }
        if (BdlinkConstant.SUCCESS.equals(voucherStat) || BdlinkConstant.BANK_FAILED.equals(voucherStat)) {
            //指令状态重设，更新原指令状态为0-交易成功/2-银行处理失败
            hql.append(" and voucherStat = '" + BdlinkConstant.WAIT_RESULT + "'");
        }
        params.put("id", bisExc.getId());
        params.put("voucherStat", voucherStat);
        return this.batchExecute(hql.toString(), params);
    }
    /**
     * 下拨指令状态更新（等待结果）
     *
     * @param bisExc
     * @param voucherStat
     * @return
     */
    public int updateBisExcProcVoucherStat(BisExcVO bisExc, String voucherStat) {
        if (bisExc == null || StringUtils.isBlank(voucherStat)) {
            return 0;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("id", bisExc.getId());
        params.put("voucherStat", voucherStat);
        return this.batchSQLExecute("update Bis_Exc_Proc set voucher_Stat = :voucherStat" + " where id=:id and voucher_Stat = '" + BdlinkConstant.WAIT_RESULT + "'", params);
    }
    /**
     * 下拨指令状态更新为失败同步更新外付指令（等待结果）
     *
     * @param bisExc
     * @param voucherStat
     * @return
     */
    public int updateBisExcVoucherStatForProc(BisExc bisExc, String voucherStat) {
        if (bisExc == null || StringUtils.isBlank(voucherStat)) {
            return 0;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("id", bisExc.getId());
        params.put("voucherStat", voucherStat);
        return this.batchExecute("update BisExc set voucherStat = :voucherStat" + " where id=:id and voucherStat = '" + BdlinkConstant.PRE_SEND + "'", params);
    }
    /**
     * 指令对应业务单号更新（指令生成时判断原指令状态）
     *
     * @param bisExc
     * @return
     */
    public int updateBisExcCbsBillNo(BisExc bisExc) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", bisExc.getId());
        params.put("cbsBillId", bisExc.getCbsBillId() + "_D");
        params.put("cbsBillNo", bisExc.getCbsBillNo() + "_D");
        return this.batchExecute("update BisExc set cbsBillId=:cbsBillId,cbsBillNo=:cbsBillNo" + " where id=:id and voucherStat in ('" + BdlinkConstant.BACK + "','" + BdlinkConstant.INVALIDATE + "')", params);
    }
    /**
     * 带原状态更新付款指令
     *
     * @param vo
     * @param oldStatus
     * @return
     */
    public int updateBisExcStatus(BisExcVO vo, Integer[] oldStatus) {
        StringBuilder hql = new StringBuilder();
        hql.append(" update BisExc set cbsBillId=:cbsBillId,cbsBillNo=:cbsBillNo,corpId=:corpId,reqDate=:reqDate,counterAcc=:counterAcc,");
        hql.append(" payerAcc=:payerAcc,payerAccName=:payerAccName,payerBank=:payerBank,payerCity=:payerCity,");
        hql.append(" payerProv=:payerProv,curId=:curId,payMoney=:payMoney,voucherTypeId=:voucherTypeId,");
        hql.append(" payeeAcc=:payeeAcc,payeeAccName=:payeeAccName,payeeBankCode=:payeeBankCode,");
        hql.append(" payeeBank=:payeeBank,payeeProv=:payeeProv,payeeCity=:payeeCity,wishPayday=:wishPayday,");
        hql.append(" isQuick=:isQuick,transMode=:transMode,bifCode=:bifCode,difBank=:difBank,areaSign=:areaSign,");
        hql.append(" purpose=:purpose,addWord=:addWord,voucherFrom=:voucherFrom,approveSign=:approveSign,");
        hql.append(" busOriType=:busOriType,cmdType=:cmdType,policyId=:policyId,tallySign=:tallySign,tallyCode=:tallyCode,");
        hql.append(" busGlobalId=:busGlobalId,noticeSign=:noticeSign,bankSerialNo=:bankSerialNo,voucherStat=:voucherStat,");
        hql.append(" transTime=:transTime,status=:status,failReason=:failReason,verifyCode=:verifyCode,timeStamp=:timeStamp,delayTime=:delayTime");
        Map<String, Object> params = new HashMap<>();
        params.put("cbsBillId", vo.getCbsBillId());
        params.put("cbsBillNo", vo.getCbsBillNo());
        params.put("corpId", vo.getCorpId());
        params.put("reqDate", vo.getReqDate());
        params.put("counterAcc", vo.getCounterAcc());
        params.put("payerAcc", vo.getPayerAcc());
        params.put("payerAccName", vo.getPayerAccName());
        params.put("payerBank", vo.getPayerBank());
        params.put("payerCity", vo.getPayerCity());
        params.put("payerProv", vo.getPayerProv());
        params.put("curId", vo.getCurId());
        params.put("payMoney", vo.getPayMoney());
        params.put("voucherTypeId", vo.getVoucherTypeId());
        params.put("payeeAcc", vo.getPayeeAcc());
        params.put("payeeAccName", vo.getPayeeAccName());
        params.put("payeeBankCode", vo.getPayeeBankCode());
        params.put("payeeBank", vo.getPayeeBank());
        params.put("payeeProv", vo.getPayeeProv());
        params.put("payeeCity", vo.getPayeeCity());
        params.put("wishPayday", vo.getWishPayday());
        params.put("isQuick", vo.getIsQuick());
        params.put("transMode", vo.getTransMode());
        params.put("bifCode", vo.getBifCode());
        params.put("difBank", vo.getDifBank());
        params.put("areaSign", vo.getAreaSign());
        params.put("purpose", vo.getPurpose());
        params.put("addWord", vo.getAddWord());
        params.put("voucherFrom", vo.getVoucherFrom());
        params.put("approveSign", vo.getApproveSign());
        params.put("busOriType", vo.getBusOriType());
        params.put("cmdType", vo.getCmdType());
        params.put("policyId", vo.getPolicyId());
        params.put("tallySign", vo.getTallySign());
        params.put("tallyCode", vo.getTallyCode());
        params.put("busGlobalId", vo.getBusGlobalId());
        params.put("noticeSign", vo.getNoticeSign());
        params.put("bankSerialNo", vo.getBankSerialNo());
        params.put("voucherStat", vo.getVoucherStat());
        params.put("transTime", vo.getTransTime());
        params.put("status", vo.getStatus());
        params.put("failReason", vo.getFailReason());
        params.put("verifyCode", vo.getVerifyCode());
        params.put("timeStamp", vo.getTimeStamp());
        params.put("delayTime", vo.getDelayTime());
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
        hql.append("where id=:id and status in (:oldStatus) ");
        params.put("id", vo.getId());
        params.put("oldStatus", oldStatus);
        return this.batchExecute(hql.toString(), params);
    }
    /**
     * 判断转付失败是否是特殊情况（下拨成功，外付失败）
     *
     * @param voucherNo
     * @return
     */
    public BisExcVO getSpecFailForTranPay(String voucherNo, String cbsBillId) {
        String sql = " select b.id as id,b.voucher_No as voucherNo,b.cbs_Bill_Id as cbsBillId,b.corp_Id as corpId,b.req_Date as reqDate," +
                " b.payer_Acc as payerAcc,b.payer_Acc_Name as payerAccName,b.payer_Bank as payerBank,b.payer_Prov as payerProv,b.payer_City as payerCity," +
                " b.cur_Id as curId,b.pay_Money as payMoney,b.payee_Acc as payeeAcc,b.payee_Acc_Name as payeeAccName,b.payee_Bank_Code as payeeBankCode," +
                " b.payee_Bank as payeeBank,b.payee_Prov as payeeProv,b.payee_City as payeeCity,b.wish_Payday as wishPayday," +
                " b.bif_id as bifCode,b.name as bifName,b.voucher_From as voucherFrom,b.purpose as purpose,b.add_Word as addWord," +
                " b.bus_Ori_Type as busOriType,b.cmd_Type as cmdType,b.bank_Serial_No as bankSerialNo,b.voucher_Stat as voucherStat,b.trans_Time as transTime,b.return_Time as returnTime," +
                " b.first_Return_Msg as firstReturnMsg,b.next_Return_Msg as nextReturnMsg " +
                " from Bis_Exc_Proc b  " +
                " where voucher_No=:voucherNo and cbs_Bill_Id=:cbsBillId";
        Map<String, Object> params = new HashMap<>();
        params.put("voucherNo", voucherNo);
        params.put("cbsBillId", cbsBillId);
        List<BisExcVO> list = listBySQLAliasToBean(sql, params, BisExcVO.class);
        if (EmptyUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }
    /**
     * 获取转付主动下拨指令
     *
     * @param id
     * @return
     */
    public BisExcVO getSpecFailById(String id) {
        String sql = " select b.id as id,b.voucher_No as voucherNo,b.cbs_Bill_Id as cbsBillId,b.corp_Id as corpId,b.req_Date as reqDate," +
                " b.payer_Acc as payerAcc,b.payer_Acc_Name as payerAccName,b.payer_Bank as payerBank,b.payer_Prov as payerProv,b.payer_City as payerCity," +
                " b.cur_Id as curId,b.pay_Money as payMoney,b.payee_Acc as payeeAcc,b.payee_Acc_Name as payeeAccName,b.payee_Bank_Code as payeeBankCode," +
                " b.payee_Bank as payeeBank,b.payee_Prov as payeeProv,b.payee_City as payeeCity,b.wish_Payday as wishPayday," +
                " b.bif_id as bifCode,b.name as bifName,b.voucher_From as voucherFrom,b.purpose as purpose,b.add_Word as addWord," +
                " b.bus_Ori_Type as busOriType,b.cmd_Type as cmdType,b.bank_Serial_No as bankSerialNo,b.voucher_Stat as voucherStat,b.trans_Time as transTime,b.return_Time as returnTime," +
                " b.first_Return_Msg as firstReturnMsg,b.next_Return_Msg as nextReturnMsg " +
                " from Bis_Exc_Proc b  " +
                " where b.id=:id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return getBySQLAliasToBean(sql, params, BisExcVO.class);
    }
    /**
     * 批量修改提醒标志
     *
     * @param ids
     */
    public void updateNoticeSign(List<String> ids) {
        String sql = "update bis_exc set notice_sign=:noticeSign" +
                " where" + SqlUtil.getInSql(ids, "id", true);
        HashMap<String, String> params = new HashMap<>();
        params.put("noticeSign", CommonConstant.SEND_SIGN_YES);
        this.batchSQLExecute(sql, params);
    }
    public List<BisExcDTO> getBisExcByCbsId(List<String> billIds) {
        return listBySQLAliasToBean(" select be.CBS_BILL_ID as cbsBillId from bis_exc be where be.voucher_stat = '0' and" +
                getSQLIn(billIds, 900, "be.CBS_BILL_ID"), null, BisExcDTO.class);
    }

    public List<BisExcDTO> getAllBisExcBankSerialNo() {
        return listBySQLAliasToBean(" select distinct be.BANK_SERIAL_NO as bankSerialNo from bis_exc be ", null, BisExcDTO.class);
    }

    public List<BisExcDTO> getBisExcByCbsIdsWithCondition(List<String> billIds) {
        return listBySQLAliasToBean(" select * from bis_exc be where " +
                getSQLIn(billIds, 900, "be.CBS_BILL_ID"), null, BisExcDTO.class);
    }
    /**
     * 根据业务ID集合和指令来源，获取指令集合
     * @param voucherFrom
     * @return
     */
    public List<BisExcDTO> getBisExcByCbsBillIds(List<String> billIds, String voucherFrom) {
        StringBuilder sql = new StringBuilder();
        sql.append("select b.id as id,b.voucher_No as voucherNo,b.voucher_Stat as voucherStat,b.cbs_Bill_Id as cbsBillId from Bis_Exc b");
        sql.append(" where ").append(SqlUtil.getInSql(billIds, "b.cbs_Bill_Id", true));
        if (!StringUtil.isBlank(voucherFrom)) {
            sql.append(" and ").append(SqlUtil.getInSql(voucherFrom.split(CommonConstant.COMMA_SPLIT), "b.voucher_From", true));
        }
        return listBySQLAliasToBean(sql.toString(), null, BisExcDTO.class);
    }
    public List<BisExcDTO> getBisExcByCbsBillIds(List<String> billIds, String voucherFrom,List<String> voucherStat) {
        StringBuilder sql = new StringBuilder();
        sql.append("select b.id as id,b.voucher_No as voucherNo,b.voucher_Stat as voucherStat,b.cbs_Bill_Id as cbsBillId from Bis_Exc b");
        sql.append(" where ").append(SqlUtil.getInSql(billIds, "b.cbs_Bill_Id", true));
        if (!StringUtil.isBlank(voucherFrom)) {
            sql.append(" and ").append(SqlUtil.getInSql(voucherFrom.split(CommonConstant.COMMA_SPLIT), "b.voucher_From", true));
        }
        if(voucherStat!=null){
            sql.append(" and ").append(SqlUtil.getInSql(voucherStat,"b.voucher_Stat", true));
        }
        return listBySQLAliasToBean(sql.toString(), null, BisExcDTO.class);
    }
    /**
     * 根据业务单号集合和指令来源，获取指令集合
     * @param voucherFrom
     * @return
     */
    public List<BisExcDTO> getBisExcByCbsBillNos(List<String> billNos, String voucherFrom) {
        StringBuilder sql = new StringBuilder();
        sql.append("select b.id as id,b.voucher_No as voucherNo,b.voucher_Stat as voucherStat from Bis_Exc b");
        sql.append(" where ").append(SqlUtil.getInSql(billNos, "b.cbs_Bill_No", true));
        if (!StringUtil.isBlank(voucherFrom)) {
            sql.append(" and ").append(SqlUtil.getInSql(voucherFrom.split(CommonConstant.COMMA_SPLIT), "b.voucher_From", true));
        }
        return listBySQLAliasToBean(sql.toString(), null, BisExcDTO.class);
    }

    public List<String> getBisExcCbsBillIdsByVoucherstat() {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from Bis_Exc exc");
        sql.append(" where exc.voucher_no is null or exc.voucher_no = '' or exc.voucher_stat in ('" + BdlinkConstant.BACK + "', '" + BdlinkConstant.INVALIDATE + "', '" + BdlinkConstant.DELETE + "')");
        List<BisExcDTO> list = listBySQLAliasToBean(sql.toString(), null, BisExcDTO.class);
        List<String> ids = list.stream().map(BisExcDTO::getCbsBillId).collect(Collectors.toList());
        return ids;
    }

    public List<BisExcVO> listNisDemandPositionExcResult() {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT n.bill_no cbsBillNo,n.id cbsBillId,b.PAYER_ACC_NAME payerAccName,b.PAYER_ACC payerAcc,b.PAYER_BANK payerBank, ");
        sql.append(" b.PAYEE_ACC_NAME payeeAccName,b.PAYEE_ACC payeeAcc,b.PAYEE_BANK payeeBank,b.PAY_MONEY payMoney,b.VOUCHER_STAT voucherStat,");
        sql.append(" BT_NVL_CHAR (b.NEXT_RETURN_MSG,b.FIRST_RETURN_MSG) nextReturnMsg ,n.demand_money demandMoney,n.pos_num posNum,b.id,b.notice_sign noticeSign, ");
        sql.append(" n.corp_id corpId ");
        sql.append(" FROM Bis_Exc b  ");
        sql.append(" JOIN nis_allocation_req r ON b.CBS_BILL_ID = r.ID ");
        sql.append(" join nis_demand n ON r.ERP_ID = n.ID ");
        sql.append(" where b.voucher_Stat in (:voucherStat) and n.status > :demandStatus ");
        sql.append(" and n.demand_date >= :demandDateBegin and n.demand_date <= :demandDateEnd ");
        sql.append(" order by n.bill_no,b.id ");

        // 指令最终状态
        List<String> voucherStat = new ArrayList<String>();
        voucherStat.add(BdlinkConstant.BANK_FAILED);
        voucherStat.add(BdlinkConstant.INTERFACE_FAILED);
        voucherStat.add(BdlinkConstant.SUCCESS);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("voucherStat", voucherStat);
        params.put("demandStatus", NisDemandConstant.STATUE_CHECK);
        params.put("demandDateBegin",DateUtils.getIntegralStartTime(new Date()));
        params.put("demandDateEnd", DateUtils.getIntegralEndTime(new Date()));

        return listBySQLAliasToBean(sql.toString(), params, BisExcVO.class);
    }

    public List<BisExcVO> listBisExcByDto(BisExcDTO dto) {
        HashMap<String, Object> params = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        if(StrUtil.isNotBlank(dto.getCbsBillNo())){
            params.put("cbsBillNo", dto.getCbsBillNo().split(","));
            builder.append(" and b.cbs_Bill_No in (:cbsBillNo)");
        }
        if(StrUtil.isNotBlank(dto.getVoucherStat())){
            params.put("voucherStat", dto.getVoucherStat().split(","));
            builder.append(" and b.voucher_stat in (:voucherStat)");
        }
        if(dto.getTransTimeNull()){
            builder.append(" and b.trans_time is null");
        }
        if(params.size() <= 0){
            return null;
        }
        return this.listBySQLAliasToBean( this.getCommonSql() + builder, params, BisExcVO.class);
    }
}
