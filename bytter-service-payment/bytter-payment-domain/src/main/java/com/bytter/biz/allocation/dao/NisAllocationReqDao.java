package com.bytter.biz.allocation.dao;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.allocation.entity.NisAllocationReq;
import com.bytter.biz.allocation.vo.NisallocationPosApproveVO;
import com.bytter.biz.bdlink.api.constant.AllocationConstant;
import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.dto.NisAllocationReqDTO;
import com.bytter.biz.payment.api.dto.NisAllocationReqListDTO;
import com.bytter.biz.report.vo.NisAllocationReqVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.workflow.api.IWorkFlowService;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: NIS_ALLOCATION_REQ
 * @date 2017-05-31 10:25:52
 */
@Repository
public class NisAllocationReqDao extends BaseDao<NisAllocationReq, String> {
    /**
     * 查询列表
     *
     * @param dataTablesInput
     * @param authSql
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String authSql, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ar.id as id, ar.bill_no as billNo, ar.corp_id as corpId, ar.type as type, ar.payer_acc_id as payerAccId, ar.payer_acc as payerAcc, ");
        sql.append(" ar.payer_acc_name as payerAccName, ar.payee_acc_id as payeeAccId, ar.payee_acc as payeeAcc, ar.payee_acc_name as payeeAccName, ");
        sql.append(" ar.cur_id as curId, ar.purpose as purpose, ar.rmk as rmk, ");
        sql.append(" ar.money as money, ar.wish_payday as wishPayday, ar.is_quick as isQuick, ar.dif_bank as difBank, ar.area_sign as areaSign, ");
        sql.append(" ar.create_by as createBy, ar.create_date as createDate, ar.status as status, ar.bill_from as billFrom,ar.attachment_Id as attachmentId, ar.pool_sign as poolSign,");
        sql.append(" ar.ca_Info_Id as caInfoId, ar.tally_Sign as tallySign,");
        sql.append(" ar.payee_Counter_Acc as payeeCounterAcc,b.voucher_Stat as voucherStat,b.id as bisExcId,g.cmd_Stat as cmdStat,g.id as bisGatherCmdId, ");
        sql.append("  bc.CUR_NAME curName,users.USERNAME createByName,sc.NAME corpName ");
        sql.append(" from nis_allocation_req ar ");
        sql.append(" left join Bis_exc b on ar.id = b.cbs_bill_id and b.voucher_From in ('" + BdlinkConstant.FROM_ALLOCATION_TYPE_DOWN + "','" + BdlinkConstant.FROM_ALLOCATION_TYPE_ASK + "','" + BdlinkConstant.FROM_ALLOCATION_TYPE_POS + "')");
        sql.append(" left join Bis_gather_cmd g on ar.id = g.cbs_bill_id and g.voucher_From in ('" + BdlinkConstant.FROM_ALLOCATION_TYPE_UP + "','" + BdlinkConstant.FROM_ALLOCATION_TYPE_TURN + "')");
        sql.append(" left join sys_user users on ar.CREATE_BY=users.LOGIN_NAME ");
        sql.append(" LEFT JOIN bt_currency bc ON ar.CUR_ID = bc.ID ");
        sql.append(" LEFT JOIN sys_corp sc ON ar.CORP_ID = sc.ID ");
        sql.append(" where 1 = 1 ");
        if (StringUtils.isNotEmpty(authSql)) {
            sql.append(authSql);
        }
        //制单日期
        String gedCreateDate = (String) dataTablesInput.getFormdata().get("ged_createDate");
        String ledCreateDate = (String) dataTablesInput.getFormdata().get("led_createDate");
        if (StringUtils.isNotEmpty(gedCreateDate)) {
            gedCreateDate = gedCreateDate + " 00:00:00";
            dataTablesInput.getFormdata().put("ged_createDate", gedCreateDate);
        }
        if (StringUtils.isNotEmpty(ledCreateDate)) {
            ledCreateDate = ledCreateDate + " 23:59:59";
            dataTablesInput.getFormdata().put("led_createDate", ledCreateDate);
        }
        String voucherStat = (String) dataTablesInput.getFormdata().get("eqs_voucherStat");
        if(StringUtils.isNotEmpty(voucherStat)){
            sql.append(" and (b.voucher_Stat = :voucher_stat or g.cmd_Stat = :voucher_stat) ");
            params.put("voucher_stat", voucherStat);
            dataTablesInput.getFormdata().remove("eqs_voucherStat");
        }
        logger.debug("头寸调拨查询sql:" + sql.toString());
        return getDynamicBySQL(dataTablesInput, sql.toString(), NisAllocationReqListDTO.class, "", false, params);
    }

    /**
     * 根据id查找资金调拨信息
     * zuoy 2017-06-22
     *
     * @param id
     * @return NisAllocationReqVO
     */
    public NisAllocationReqListDTO getByUnion(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ar.id as id, ar.bill_no as billNo, ar.corp_id as corpId, ar.type as type, ar.payer_acc_id as payerAccId, ar.payer_acc as payerAcc, ");
        sql.append(" ar.payer_acc_name as payerAccName, ar.payee_acc_id as payeeAccId, ar.payee_acc as payeeAcc, ar.payee_acc_name as payeeAccName, ");
        sql.append(" ar.cur_id as curId, ar.purpose as purpose, ");
        sql.append(" ar.bill_from as billFrom, ");
        sql.append(" ar.money as money, ar.wish_payday as wishPayday, ar.is_quick as isQuick, ar.dif_bank as difBank, ar.area_sign as areaSign, ar.pool_sign as poolSign, ");
        sql.append(" ar.create_by as createBy,ar.rmk as rmk, ar.create_date as createDate, ar.status as status, ar.attachment_Id as attachmentId,ar.ca_Info_Id as caInfoId,ar.erp_Id as erpId, ");
        sql.append(" ar.payee_Counter_Acc as payeeCounterAcc");
        sql.append(" from nis_allocation_req ar ");
        sql.append(" where ar.id = :id ");
        HashMap<String, String> params = new HashMap();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, NisAllocationReqListDTO.class);
    }

    /**
     * 根据外部账号查找资金调拨信息
     * wangzx 2018-01-17
     *
     * @param erpId
     * @return NisAllocationReqVO
     */
    public NisAllocationReqListDTO getNisAllocationReqByErpId(String erpId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ar.id as id, ar.bill_no as billNo, ar.corp_id as corpId, ar.type as type, ar.payer_acc_id as payerAccId, ar.payer_acc as payerAcc, ");
        sql.append(" ar.payer_acc_name as payerAccName, ar.payee_acc_id as payeeAccId, ar.payee_acc as payeeAcc, ar.payee_acc_name as payeeAccName, ");
        sql.append(" ar.cur_id as curId, ar.purpose as purpose, ");
        sql.append(" ar.money as money, ar.wish_payday as wishPayday, ar.is_quick as isQuick, ar.dif_bank as difBank, ar.area_sign as areaSign,ar.pool_sign as poolSign, ");
        sql.append(" ar.create_by as createBy, ar.create_date as createDate, ar.status as status, ar.attachment_Id as attachmentId,ar.ca_Info_Id as caInfoId,ar.erp_Id as erpId, ");
        sql.append(" ar.payee_Counter_Acc as payeeCounterAcc");
        sql.append(" from nis_allocation_req ar ");
        //收款方
        sql.append(" where ar.erp_Id = :erpId ");
        HashMap<String, String> params = new HashMap();
        params.put("erpId", erpId);
        return getBySQLAliasToBean(sql.toString(), params, NisAllocationReqListDTO.class);
    }

    /**
     * 审核查询列表
     *
     * @param dataTablesInput
     * @param businessSql
     * @return
     */
    @SuppressWarnings("unchecked")
    public DataTablesOutput<NisAllocationReqListDTO> getDataTablesApprove(DataTablesInput dataTablesInput, String businessSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ar.id as id, ar.bill_no as billNo, ar.corp_id as corpId, ar.type as type, ar.payer_acc_id as payerAccId, ar.payer_acc as payerAcc, ");
        sql.append(" ar.payer_acc_name as payerAccName, ar.payee_acc_id as payeeAccId, ar.payee_acc as payeeAcc, ar.payee_acc_name as payeeAccName, ");
        sql.append(" ar.cur_id as curId, ar.purpose as purpose, ");
        sql.append(" ar.money as money, ar.wish_payday as wishPayday, ar.is_quick as isQuick, ar.dif_bank as difBank, ar.area_sign as areaSign,ar.pool_sign as poolSign, ");
        sql.append(" ar.create_by as createBy, ar.create_date as createDate, ar.status as status, ar.attachment_Id as attachmentId,ar.ca_Info_Id as caInfoId, ");
        sql.append(" ar.payee_Counter_Acc as payeeCounterAcc");
        sql.append(" from nis_allocation_req ar ");
        //收款方
        sql.append(" where ");
        sql.append(businessSql);
        String flowStatus = dataTablesInput.getParameter().get("flowStatus");
        //已审批不过滤状态
        if (!IWorkFlowService.PASS.equals(flowStatus)) {
            //过滤已审数据
            sql.append(" and ar.status<> ").append(NetbankConstant.STATUE_CHECK);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), NisAllocationReqListDTO.class, "ar", false);
    }

    /**
     * 手工记账查询列表  先记账后付款
     *
     * @param dataTablesInput
     * @param authSql
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesAccount(DataTablesInput dataTablesInput, String authSql, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ar.id as id, ar.bill_no as billNo, ar.corp_id as corpId, ar.type as type, ar.payer_acc_id as payerAccId, ar.payer_acc as payerAcc, ");
        sql.append(" ar.payer_acc_name as payerAccName, ar.payee_acc_id as payeeAccId, ar.payee_acc as payeeAcc, ar.payee_acc_name as payeeAccName, ");
        sql.append(" ar.cur_id as curId, ar.purpose as purpose, ");
        sql.append(" ar.money as money, ar.wish_payday as wishPayday, ar.is_quick as isQuick, ar.dif_bank as difBank, ar.area_sign as areaSign,ar.pool_sign as poolSign, ");
        sql.append(" ar.create_by as createBy, ar.create_date as createDate, ar.status as status, ar.attachment_Id as attachmentId,ar.ca_Info_Id as caInfoId , ar.bill_from as billfrom ,ar.tally_sign as tallySign,");
        sql.append(" ar.order_no as orderNo,ar.rmk as rmk,ar.payee_Counter_Acc as payeeCounterAcc");
        sql.append(" from nis_allocation_req ar ");
        //收款方
        sql.append(" where 1 = 1");
        //非资金池的资金调拨单
        sql.append(" and ar.pool_sign<>'1' ");

        if (StringUtils.isNotEmpty(authSql)) {
            sql.append(authSql);
            if (!StringUtils.isNotEmpty(dataTablesInput.getFormdata().get("likes_corpId"))) {
                sql.append(" and (" + getSQLIn(corpAuth, 900, "ar.corp_id") + ")");
            }
        }
        //sql.append(" order by ar.order_no  ");
        dataTablesInput.setOrderString(new OrderString("ar.order_no", "asc"));
        return getDynamicBySQL(dataTablesInput, sql.toString(), NisAllocationReqListDTO.class, "", false);
    }

    /**
     * 手工记账查询列表 先付款后记账
     *
     * @param dataTablesInput
     * @param authSql
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesAccountAndBisExc(DataTablesInput dataTablesInput, String authSql, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ar.id as id, ar.bill_no as billNo, ar.corp_id as corpId, ar.type as type, ar.payer_acc_id as payerAccId, ar.payer_acc as payerAcc, ");
        sql.append(" ar.payer_acc_name as payerAccName, ar.payee_acc_id as payeeAccId, ar.payee_acc as payeeAcc, ar.payee_acc_name as payeeAccName, ");
        sql.append(" ar.cur_id as curId, ar.purpose as purpose, ");
        sql.append(" ar.money as money, ar.wish_payday as wishPayday, ar.is_quick as isQuick, ar.dif_bank as difBank, ar.area_sign as areaSign,ar.pool_sign as poolSign, ");
        sql.append(" ar.create_by as createBy, ar.create_date as createDate, ar.status as status, ar.attachment_Id as attachmentId,ar.ca_Info_Id as caInfoId , ar.bill_from as billfrom , ar.tally_sign as tallySign,");
        sql.append(" ar.rmk as rmk,ar.payee_Counter_Acc as payeeCounterAcc");
        sql.append(" from nis_allocation_req ar ");
        //收款方
        sql.append(" where 1 = 1 ");
        //付款成功的才进行记账
        //非资金池的资金调拨单
        sql.append(" and ar.pool_sign<>'1'");
        if (StringUtils.isNotEmpty(authSql)) {
            sql.append(authSql);
        }

        if (StringUtils.isNotEmpty(authSql)) {
            sql.append(authSql);
            if (!StringUtils.isNotEmpty(dataTablesInput.getFormdata().get("likes_corpId"))) {
                sql.append(" and (" + getSQLIn(corpAuth, 900, "ar.corp_id") + ")");
            }
        }

//		sql.append(" order by ar.bill_no desc ");
        dataTablesInput.setOrderString(new OrderString("ar.bill_no", "desc"));
        return getDynamicBySQL(dataTablesInput, sql.toString(), NisAllocationReqListDTO.class, "", false);
    }

    /**
     * 根据单号查找资金调拨信息
     * zuoy 2018-03-08
     *
     * @param billNo
     * @return NisAllocationReqVO
     */
    public NisAllocationReqListDTO getNisAllocationReqByBillNo(String billNo) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ar.id as id, ar.bill_no as billNo, ar.corp_id as corpId,ar.bill_from, ar.type as type, ar.payer_acc_id as payerAccId, ar.payer_acc as payerAcc, ");
        sql.append(" ar.payer_acc_name as payerAccName,ar.voucher_no as voucherNo,ar.order_no as orderNo, ar.payee_acc_id as payeeAccId, ");
        sql.append(" ar.payee_acc as payeeAcc, ar.payee_acc_name as payeeAccName, ar.cur_id as curId, ar.purpose as purpose, ar.rmk,");
        sql.append(" ar.money as money, ar.wish_payday as wishPayday, ar.is_quick as isQuick, ar.dif_bank as difBank, ar.area_sign as areaSign,ar.pool_sign as poolSign, ");
        sql.append(" ar.create_by as createBy, ar.create_date as createDate, ar.status as status, ar.attachment_Id as attachmentId,");
        sql.append(" ar.ca_Info_Id as caInfoId,ar.tally_sign as tallySign,ar.payee_Counter_Acc as payeeCounterAcc ");
        sql.append(" from nis_allocation_req ar ");
        //收款方
        sql.append(" where ar.bill_no = :billNo ");
        HashMap<String, String> params = new HashMap();
        params.put("billNo", billNo);
        return getBySQLAliasToBean(sql.toString(), params, NisAllocationReqListDTO.class);
    }

    /**
     * 获取在途的资金调拨单
     *
     * @param corpId
     * @return
     */
    public List<NisAllocationReqListDTO> listNotFinishedNisAllocationReq(String corpId, String authSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ar.id as id, ar.bill_no as billNo, ar.corp_id as corpId, ar.type as type, ar.payer_acc_id as payerAccId, ar.payer_acc as payerAcc, ");
        sql.append(" ar.payer_acc_name as payerAccName, ar.payee_acc_id as payeeAccId, ar.payee_acc as payeeAcc, ar.payee_acc_name as payeeAccName, ");
        sql.append(" ar.cur_id as curId, ar.purpose as purpose, ");
        sql.append(" ar.money as money, ar.wish_payday as wishPayday, ar.is_quick as isQuick, ar.dif_bank as difBank, ar.area_sign as areaSign, ");
        sql.append(" ar.create_by as createBy, ar.create_date as createDate, ar.status as status, ar.bill_from as billFrom,ar.attachment_Id as attachmentId, ar.pool_sign as poolSign,");
        sql.append(" ar.ca_Info_Id as caInfoId,ar.tally_Sign as tallySign,ar.payee_Counter_Acc as payeeCounterAcc");
        sql.append(" from nis_allocation_req ar ");
        //收款方
        sql.append(" where ar.status <> " + AllocationConstant.STATUE_DEL);
        if (StringUtils.isNotEmpty(authSql)) {
            sql.append(authSql);
        }
        sql.append(" and ar.corp_id=:corpId");
        HashMap<String, String> params = new HashMap<>();
        params.put("corpId", corpId);
        return listBySQLAliasToBean(sql.toString(), params, NisAllocationReqListDTO.class);
    }

    /**
     * 根据单位和业务类型统计调拨信息
     *
     * @param corpId
     * @param type
     * @return
     */
    public int getAllocationReqCountByCorpId(String corpId, Integer type) {
        String sql = "select count(*) from nis_allocation_req where corp_id=:corpId and type=:type";
        HashMap<String, Object> params = new HashMap<>();
        params.put("corpId", corpId);
        params.put("type", type);
        //0-暂存、11-待审批、-1-审批打回
        sql += " and status in ('0','11','-1')";
        return ((Number) this.createSQLQuery(sql, params).uniqueResult()).intValue();
    }

    /**
     * 根据单位和业务类型统计调拨信息（记账）
     *
     * @param corpId
     * @return
     */
    public int getAllocationReqCountByCorpIdForTally(String corpId) {
        String sql = "select count(*) from nis_allocation_req where corp_id=:corpId";
        HashMap<String, Object> params = new HashMap<>();
        params.put("corpId", corpId);
        sql += " and type in ('11','12','13') and status >=95 and (tally_sign is null or tally_sign<>'1')";
        return ((Number) this.createSQLQuery(sql, params).uniqueResult()).intValue();
    }

    /**
     * 待记账 先付款后记账
     */
    public List<NisAllocationReqDTO> getTallyBisExc(String authSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ar.id as id,ar.money as money,ar.corp_id as corpId ");
        sql.append(" from nis_allocation_req ar left join bt_bank_acc ba on ar.payer_acc_id = ba.id ");
        sql.append(" left join Bis_exc b on ar.id = b.cbs_bill_id where 1 = 1 ");
        //付款成功的才进行记账
        sql.append(" and b.voucher_stat = '" + BdlinkConstant.SUCCESS + "'");
        //非资金池的资金调拨单
        sql.append(" and ar.pool_sign<>'1'");
        if (StringUtils.isNotEmpty(authSql)) {
            sql.append(authSql);
        }
        sql.append(" and not exists( select 1 from fc_tally_remind_dtl d where d.bill_id = ar.id ) ");
        return listByHQLAliasToBean(sql.toString(), null, NisAllocationReqDTO.class);
    }

    /**
     * 待记账  先记账后付款
     */
    public List<NisAllocationReqDTO> getTallyAccount(String authSql) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ar.id as id, ar.money as money,ar.corp_id as corpId  ");
        sql.append(" from nis_allocation_req ar left join bt_bank_acc ba on ar.payer_acc_id = ba.id ");
        sql.append(" where 1 = 1");
        //非资金池的资金调拨单
        sql.append(" and ar.pool_sign<>'1' ");
        if (StringUtils.isNotEmpty(authSql)) {
            sql.append(authSql);
        }
        sql.append(" and not exists( select 1 from fc_tally_remind_dtl d where d.bill_id = ar.id ) ");
        return listBySQLAliasToBean(sql.toString(), null, NisAllocationReqDTO.class);
    }

    /**
     * 查询还款状态
     */
    public List<NisAllocationReqDTO> getRepayBisExc(NisAllocationReqDTO nisAllocationReqDTO) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select req.erp_id as erpId, req.status AS status,exc.voucher_stat AS voucherStat ");
        sql.append(" from nis_allocation_req req ");
        sql.append(" left join Bis_exc exc on exc.cbs_bill_id = req.id  ");
        //13头寸调拨
        sql.append("where req.type = 13");
        HashMap<String, Object> params = new HashMap<>();
        if (StringUtils.isNotEmpty(nisAllocationReqDTO.getErpId())) {
            sql.append(" and req.erp_id ='" + nisAllocationReqDTO.getErpId() + "'");
            params.put("erp_id", nisAllocationReqDTO.getErpId());
        }
        if (StringUtils.isNotEmpty(nisAllocationReqDTO.getBillNo())) {
            sql.append(" and req.bill_no='" + nisAllocationReqDTO.getBillNo() + "'");
            params.put("bill_no", nisAllocationReqDTO.getBillNo());
        }
        return listBySQLAliasToBean(sql.toString(), null, NisAllocationReqDTO.class);
    }

    /**
     * 带原状态更新资金调拨单
     *
     * @param vo        资金调拨单对象
     * @param oldStatus 原状态控制
     * @return
     */
    public int updateNisAllocationReqStatus(NisAllocationReqListDTO vo, Integer[] oldStatus) {
        StringBuilder hql = new StringBuilder();

        hql.append(" update nis_allocation_req set type=:type,CORP_ID=:corpId,PAYER_ACC_ID=:payerAccId,PAYER_ACC=:payerAcc,PAYER_ACC_NAME=:payerAccName,");
        hql.append(" PAYEE_ACC_ID=:payeeAccId,PAYEE_ACC=:payeeAcc,PAYEE_ACC_NAME=:payeeAccName,CUR_ID=:curId,money=:money,");
        hql.append(" BURSAR_BILL_NO=:bursarBillNo,IS_QUICK=:isQuick,DIF_BANK=:difBank,AREA_SIGN=:areaSign,");
        hql.append(" purpose=:purpose,order_no=:orderNo,VOUCHER_NO=:voucherNo,CA_INFO_ID=:caInfoId,");
        hql.append(" RMK=:rmk,STATUS=:status,ATTACHMENT_ID=:attachmentId,BILL_FROM=:billFrom,ERP_ID=:erpId,TALLY_SIGN=:tallySign,");
        hql.append(" TALLY_CODE=:tallyCode,POOL_SIGN=:poolSign,TIME_STAMP=:timeStamp,PAYEE_COUNTER_ACC=:payeeCounterAcc,VOUCHER_STAT=:voucherStat");
        Map<String, Object> params = new HashMap<>();
        params.put("type", vo.getType());
        params.put("rmk", vo.getRmk());
        params.put("corpId", vo.getCorpId());
        params.put("voucherStat", vo.getVoucherStat());
        params.put("payerAccId", vo.getPayerAccId());
        params.put("payerAcc", vo.getPayerAcc());
        params.put("payerAccName", vo.getPayerAccName());
        params.put("payeeAccId", vo.getPayeeAccId());
        params.put("payeeAcc", vo.getPayeeAcc());
        params.put("payeeAccName", vo.getPayeeAccName());
        params.put("curId", vo.getCurId());
        params.put("money", vo.getMoney());

        params.put("bursarBillNo", vo.getBursarBillNo());
        params.put("isQuick", vo.getIsQuick());
        params.put("difBank", vo.getDifBank());
        params.put("areaSign", vo.getAreaSign());
        params.put("purpose", vo.getPurpose());
        params.put("orderNo", vo.getOrderNo());
        params.put("voucherNo", vo.getVoucherNo());
        params.put("caInfoId", vo.getCaInfoId());
        params.put("status", vo.getStatus());
        params.put("attachmentId", vo.getAttachmentId());
        params.put("billFrom", vo.getBillFrom());
        params.put("erpId", vo.getErpId());
        params.put("tallySign", vo.getTallySign());
        params.put("tallyCode", vo.getTallyCode());
        params.put("poolSign", vo.getPoolSign());
        params.put("timeStamp", vo.getTimeStamp());
        params.put("payeeCounterAcc", vo.getPayeeCounterAcc());
        if (vo.getWishPayday() != null) {
            hql.append(",WISH_PAYDAY=:wishPayday");
            params.put("wishPayday", vo.getWishPayday());
        }
        if (ObjectUtil.isNotEmpty(vo.getTallyDate())) {
            hql.append(",TALLY_DATE=:tallyDate ");
            params.put("tallyDate", vo.getTallyDate());
        }
        if (StringUtils.isNotEmpty(vo.getCreateBy())) {
            hql.append(",CREATE_BY=:createBy ");
            params.put("createBy", vo.getCreateBy());
        }
        if (vo.getCreateDate() != null) {
            hql.append(",CREATE_DATE=:createDate ");
            params.put("createDate", vo.getCreateDate());
        }
        if (StringUtils.isNotEmpty(vo.getUpdateBy())) {
            hql.append(",UPDATE_BY=:updateBy ");
            params.put("updateBy", vo.getUpdateBy());
        }
        if (vo.getUpdateDate() != null) {
            hql.append(",UPDATE_DATE=:updateDate ");
            params.put("updateDate", vo.getUpdateDate());
        }
        hql.append("where id=:id and status in (:oldStatus) ");
        params.put("id", vo.getId());
        params.put("oldStatus", oldStatus);
//        int size = this.batchExecute(hql.toString(), params);
        int size = createSQLQuery(hql.toString(), params).executeUpdate();
        this.flush();
        return size;
    }

    /**
     * 带原状态更新资金调拨单
     *
     * @param vo        资金调拨单对象
     * @return
     */
    public int updateNisAllocationReqStatus(NisAllocationReqListDTO vo) {
        StringBuilder hql = new StringBuilder();
        hql.append(" update NisAllocationReq set type=:type,corpId=:corpId,payerAccId=:payerAccId,payerAcc=:payerAcc,payerAccName=:payerAccName,");
        hql.append(" payeeAccId=:payeeAccId,payeeAcc=:payeeAcc,payeeAccName=:payeeAccName,curId=:curId,money=:money,");
        hql.append(" wishPayday=:wishPayday,bursarBillNo=:bursarBillNo,isQuick=:isQuick,difBank=:difBank,areaSign=:areaSign,");
        hql.append(" purpose=:purpose,tallyDate=:tallyDate,orderNo=:orderNo,voucherNo=:voucherNo,caInfoId=:caInfoId,");
        hql.append(" rmk=:rmk,status=:status,attachmentId=:attachmentId,billFrom=:billFrom,erpId=:erpId,tallySign=:tallySign,");
        hql.append(" tallyCode=:tallyCode,poolSign=:poolSign,timeStamp=:timeStamp,payeeCounterAcc=:payeeCounterAcc,voucherStat=:voucherStat");
        Map<String, Object> params = new HashMap<>();
        params.put("type", vo.getType());
        params.put("rmk", StringUtils.isNotEmpty(vo.getRmk()) ? vo.getRmk() : "");
        params.put("corpId", StringUtils.isNotEmpty(vo.getCorpId()) ? vo.getCorpId() : "");
        params.put("voucherStat", StringUtils.isNotEmpty(vo.getVoucherStat()) ? vo.getVoucherStat() : "");
        params.put("payerAccId", StringUtils.isNotEmpty(vo.getPayerAccId()) ? vo.getPayerAccId() : "");
        params.put("payerAcc", StringUtils.isNotEmpty(vo.getPayerAcc()) ? vo.getPayerAcc() : "");
        params.put("payerAccName", StringUtils.isNotEmpty(vo.getPayerAccName()) ? vo.getPayerAccName() : "");
        params.put("payeeAccId", StringUtils.isNotEmpty(vo.getPayeeAccId()) ? vo.getPayeeAccId() : "");
        params.put("payeeAcc", StringUtils.isNotEmpty(vo.getPayeeAcc()) ? vo.getPayeeAcc() : "");
        params.put("payeeAccName", StringUtils.isNotEmpty(vo.getPayeeAccName()) ? vo.getPayeeAccName() : "");
        params.put("curId", StringUtils.isNotEmpty(vo.getCurId()) ? vo.getCurId() : "");
        params.put("money", vo.getMoney()!=null ? vo.getMoney(): new BigDecimal(0));
        params.put("wishPayday", vo.getWishPayday() !=null ? vo.getWishPayday() : new Date());
        params.put("bursarBillNo", StringUtils.isNotEmpty(vo.getBursarBillNo()) ? vo.getBursarBillNo() : "");
        params.put("isQuick", StringUtils.isNotEmpty(vo.getIsQuick()) ? vo.getIsQuick() : "");
        params.put("difBank", StringUtils.isNotEmpty(vo.getDifBank()) ? vo.getDifBank() : "");
        params.put("areaSign",StringUtils.isNotEmpty( vo.getAreaSign()) ? vo.getAreaSign() : "");
        params.put("purpose", StringUtils.isNotEmpty(vo.getPurpose()) ? vo.getPurpose() : "");
        params.put("tallyDate", vo.getTallyDate() !=null ? vo.getTallyDate() : new Date());
        params.put("orderNo", StringUtils.isNotEmpty(vo.getOrderNo()) ? vo.getOrderNo() : "");
        params.put("voucherNo", StringUtils.isNotEmpty(vo.getVoucherNo()) ? vo.getVoucherNo() : "");
        params.put("caInfoId", StringUtils.isNotEmpty(vo.getCaInfoId()) ? vo.getCaInfoId() : "");
        params.put("status", StringUtils.isNotEmpty(vo.getStatus()) ? vo.getStatus() : "");
        params.put("attachmentId", StringUtils.isNotEmpty(vo.getAttachmentId()) ? vo.getAttachmentId() : "");
        params.put("billFrom", StringUtils.isNotEmpty(vo.getBillFrom()) ? vo.getBillFrom() : "");
        params.put("erpId", StringUtils.isNotEmpty(vo.getErpId()) ? vo.getErpId() : "");
        params.put("tallySign", StringUtils.isNotEmpty(vo.getTallySign()) ? vo.getTallySign() : "");
        params.put("tallyCode", StringUtils.isNotEmpty(vo.getTallyCode()) ? vo.getTallyCode() : "");
        params.put("poolSign", StringUtils.isNotEmpty(vo.getPoolSign()) ? vo.getPoolSign() : "");
        params.put("timeStamp", StringUtils.isNotEmpty(vo.getTimeStamp()) ? vo.getTimeStamp() : String.valueOf(new Date().getTime()));
        params.put("payeeCounterAcc", StringUtils.isNotEmpty(vo.getPayeeCounterAcc()) ? vo.getPayeeCounterAcc() : "");
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
        hql.append("where id=:id ");
        params.put("id", vo.getId());
        int size = this.batchExecute(hql.toString(), params);
        this.flush();
        return size;
    }


    public List<NisAllocationReqDTO> getNisAllocationReqBillFrom(NisAllocationReqDTO dto) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT n.erp_id AS erpId ");
        sql.append(" FROM nis_allocation_req n  ");
        sql.append(" WHERE n.status <> - 2 AND nvl(n.erp_id, '')!='' AND ( (n.status IN (95, 100))  OR (n.status NOT IN (95, 100)) ) ");
        if (StringUtils.isNotEmpty(dto.getBillFrom())) {
            sql.append(" AND n.bill_from = '" + dto.getBillFrom() + "' ");
        }

        return listBySQLAliasToBean(sql.toString(), null, NisAllocationReqDTO.class);
    }

    public int updateVoucherState(String cbsBillId, String voucherStat) {
        String sb = " update nis_allocation_req set voucher_stat = :voucherStat " +
                " where  id = :id";
        Map<String, Object> param = new HashMap<>();
        param.put("id", cbsBillId);
        param.put("voucherStat", voucherStat);
        return createSQLQuery(sb, param).executeUpdate();
    }

    public NisAllocationReq getNisAllocationReqByVoucherNo(String voucherNo) {
        StringBuilder sql = new StringBuilder("select * from nis_allocation_req n");
        sql.append(" where n.voucher_No=:voucherNo");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("voucherNo", voucherNo);
        return getBySQLAliasToBean(sql.toString(), params, NisAllocationReq.class);
    }

    /**
     * 根据月份，资金调拨类型获取调拨单据信息
     *
     * @param queryDate 查询日期
     * @param types     业务类型集合
     * @return
     */
    public List<NisAllocationReqVO> listNisAllocationReq(Date queryDate, List<Integer> types) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" select ar.id as id, ar.bill_no as billNo, ar.corp_id as corpId, ar.type as type, ar.payer_acc_id as payerAccId, ar.payer_acc as payerAcc, ");
        sql.append(" ar.payer_acc_name as payerAccName, ar.payee_acc_id as payeeAccId, ar.payee_acc as payeeAcc, ar.payee_acc_name as payeeAccName, ");
        sql.append(" ar.cur_id as curId, ar.purpose as purpose, ");
        sql.append(" ar.money as money, ar.wish_payday as wishPayday, ar.is_quick as isQuick, ar.dif_bank as difBank, ar.area_sign as areaSign, ");
        sql.append(" ar.create_by as createBy, ar.create_date as createDate, ar.status as status, ar.bill_from as billFrom,ar.attachment_Id as attachmentId, ar.pool_sign as poolSign,");
        sql.append(" ar.ca_Info_Id as caInfoId,ar.tally_Sign as tallySign,ar.payee_Counter_Acc as payeeCounterAcc");
        sql.append(" from nis_allocation_req ar ");
        sql.append(" where DATE_FORMAT(ar.create_date, '%Y-%m') =:queryDate ");
        sql.append(" and ar.type in (:types) ");
        sql.append(" and ar.status =:status ");
        params.put("queryDate", queryDate);
        params.put("types", types);
        params.put("status", NetbankConstant.STATUE_CHECK);
        return listBySQLAliasToBean(sql.toString(), params, NisAllocationReqVO.class);
    }

    /**
     * 根据id获取当前日期的待审批数（用于定时任务请款待审批）
     * @param ids
     * @return
     */
    public int countByIds(List<String> ids) {
        StringBuilder sql = new StringBuilder("select * from nis_allocation_req n ");
        Map<String, Object> params = new HashMap<>();
        sql.append(" where n.id in (:ids) and n.WISH_PAYDAY = :wishPayDay ");
        params.put("ids", ids);
        params.put("wishPayDay",DateUtils.getDateYMD());
        List<NisAllocationReqVO> list = listBySQLAliasToBean(sql.toString(), params, NisAllocationReqVO.class);
        int count = 0;
        if(EmptyUtils.isNotEmpty(list)){
            count = list.size();
        }
        return count;
    }

    /**
     * 根据id获取待审批数（用于定时任务请款待审批）
     * @param ids
     * @return
     */
    public int countByIdsNoDate(List<String> ids) {
        StringBuilder sql = new StringBuilder("select * from nis_allocation_req n ");
        Map<String, Object> params = new HashMap<>();
        sql.append(" where n.id in (:ids) and n.WISH_PAYDAY <= :wishPayDay ");
        params.put("ids", ids);
        params.put("wishPayDay",DateUtils.getDateYMD());
        List<NisAllocationReqVO> list = listBySQLAliasToBean(sql.toString(), params, NisAllocationReqVO.class);
        int count = 0;
        if(EmptyUtils.isNotEmpty(list)){
            count = list.size();
        }
        return count;
    }

    public List<NisallocationPosApproveVO> queryPosApproveList() {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder(128);
        sql.append("SELECT pd.KEY_ as categoryKey,task.ASSIGNEE_ userId,count(exe.BUSINESS_KEY_) as num ");
        sql.append("FROM ACT_RU_TASK task ");
        sql.append("INNER JOIN ACT_RE_PROCDEF pd ON task.PROC_DEF_ID_ = pd.ID_ ");
        sql.append("INNER JOIN ACT_RU_EXECUTION exe ON exe.PROC_DEF_ID_ = pd.ID_ ");
        sql.append("AND exe.PARENT_ID_ IS NULL AND exe.ID_=task.PROC_INST_ID_ AND exe.PROC_INST_ID_=task.PROC_INST_ID_ ");
        sql.append("JOIN nis_allocation_req r ON exe.BUSINESS_KEY_ = r.ID AND BT_TO_CHAR(r.WISH_PAYDAY,'YYYY-MM-DD') = :currentDate ");
        sql.append("WHERE task.SUSPENSION_STATE_ = 1 AND pd.KEY_ = 'netbankAllocationPos' ");
        sql.append("GROUP BY pd.KEY_,task.ASSIGNEE_ ");
        params.put("currentDate",DateUtils.formatDate(new Date()));
        return listBySQLAliasToBean(sql.toString(), params, NisallocationPosApproveVO.class);
    }
}
