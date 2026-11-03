package com.bytter.increment.domain.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.NetbankConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.api.constant.InnerFinanceConstant;
import com.bytter.increment.api.dto.ChoInnerFinanceDTO;
import com.bytter.increment.domain.entity.ChoInnerFinance;
import com.bytter.increment.domain.vo.ChoInnerFinanceVO;
import com.bytter.workflow.api.IWorkFlowService;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: 内部理财信息表
 * @author huangx
 * @date 2019-11-1 14:03:30
 * @version 1.0
 *
 */
@Repository
public class ChoInnerFinanceDao extends BaseDao<ChoInnerFinance,String> {
    /**
     * 内部理财信息表通用查@param sqlWhere
     *
     * @return
     */
    private String getCommonSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("select f.id as id,f.bill_No as billNo,f.corp_Id as corpId,f.corp_name as corpName,f.bus_Type as busType,");
        sql.append(" f.payer_Acc_Id as payerAccId,f.payer_Acc as payerAcc,f.net_id as netId,");
        sql.append(" f.payee_Acc_Id as payeeAccId,f.payee_Acc as payeeAcc,f.capital_Acc_Id as capitalAccId,f.interest_Acc_Id as interestAccId," );
        sql.append( "f.deposit_Rate_Type as depositRateType,");
        sql.append(" f.cur_Id as curId,f.cur_name as curName,f.pay_Money as payMoney,f.day_Limit as dayLimit,f.date_Unit as dateUnit," );
        sql.append(" f.certificate_Way as certificateWay,f.interest_Start_Date as interestStartDate,");
        sql.append(" f.deadline_Date as deadlineDate,f.interest_Rate as interestRate,f.purpose as purpose,f.add_Word as addWord," );
        sql.append("f.pay_Date as payDate,f.related_Bill_Id as relatedBillId,");
        sql.append(" f.expect_Interest as expectInterest,f.status as status,f.tally_Date as tallyDate,f.order_No as orderNo," );
        sql.append("f.voucher_No as voucherNo,f.tally_Sign as tallySign,f.tally_Code as tallyCode,");
        sql.append(" f.create_By as createBy,f.create_Date as createDate,f.update_By as updateBy,f.update_Date as updateDate,");
        sql.append(" f.notice_Type as noticeType,f.period_name as depositRateTypeName,r.bill_No as relatedBillNo,");
        sql.append(" f.payer_acc_name as payerAccName,f.payee_acc_name as payeeAccName,");
        sql.append(" f.capital_acc as capitalAcc,f.capital_acc_name as capitalAccName,f.interest_acc as interestAcc,");
        sql.append(" f.interest_acc_name as interestAccName,f.acc_interest_rate as accInterestRate");
        sql.append(" from cho_inner_finance f");
        sql.append(" left join cho_inner_finance r on f.related_Bill_Id=r.id");
        sql.append(" where 1=1");
        return sql.toString();
    }
    /**
     * 查询理财信息
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput,String joinSql) {
        StringBuilder sql=new StringBuilder(this.getCommonSql());
        if(StringUtils.isNotEmpty(joinSql)) {
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
        return getDynamicBySQL(dataTablesInput, sql.toString(), ChoInnerFinanceVO.class, "f", false);
    }
    /**
     * 根据ID获取理财信息
     * @param id
     * @return
     */
    public ChoInnerFinanceVO getByIdUnion(String id){
        StringBuilder sql=new StringBuilder();
        sql.append("select f.id as id,f.bill_No as billNo,f.corp_Id as corpId,f.corp_name as corpName,f.bus_Type as busType,f.payer_Acc_Id as payerAccId,f.payer_Acc as payerAcc,f.net_id as netId,");
        sql.append(" f.payee_Acc_Id as payeeAccId,f.payee_Acc as payeeAcc,f.capital_Acc_Id as capitalAccId,f.interest_Acc_Id as interestAccId,f.deposit_Rate_Type as depositRateType,");
        sql.append(" f.cur_Id as curId,f.cur_name as curName,f.pay_Money as payMoney,f.day_Limit as dayLimit,f.date_Unit as dateUnit,f.certificate_Way as certificateWay,f.interest_Start_Date as interestStartDate,");
        sql.append(" f.deadline_Date as deadlineDate,f.interest_Rate as interestRate,f.purpose as purpose,f.add_Word as addWord,f.pay_Date as payDate,f.related_Bill_Id as relatedBillId,");
        sql.append(" f.expect_Interest as expectInterest,f.status as status,f.tally_Date as tallyDate,f.order_No as orderNo,f.voucher_No as voucherNo,f.tally_Sign as tallySign,f.tally_Code as tallyCode,");
        sql.append(" f.create_By as createBy,f.create_Date as createDate,f.update_By as updateBy,f.update_Date as updateDate,f.notice_Type as noticeType,f.period_name as depositRateTypeName,r.bill_No as relatedBillNo,");
        sql.append(" f.payer_acc_name as payerAccName,f.payee_acc_name as payeeAccName,f.capital_acc as capitalAcc,f.capital_acc_name as capitalAccName,f.interest_acc as interestAcc,f.interest_acc_name as interestAccName,");
        sql.append(" (select bt_nvl_num(sum(t.pay_money),0) from cho_inner_finance t where t.related_Bill_Id = f.id and t.status > " + InnerFinanceConstant.STATUE_BACK);
        sql.append(" and t.bus_type in ("+ InnerFinanceConstant.BUS_TYPE_T2C+","+InnerFinanceConstant.BUS_TYPE_N2C+")) as drawMoney,f.acc_interest_rate as accInterestRate,");
        sql.append(" (select bt_nvl_num(sum(t.pay_money),0) from cho_inner_finance t where t.related_Bill_Id = f.id and t.status >= " + InnerFinanceConstant.STATUE_BACK+" and t.status <=" + InnerFinanceConstant.STATUE_CHECK);
        sql.append(" and t.bus_type ="+InnerFinanceConstant.BUS_TYPE_N2B+") as bookMoney");
        sql.append(" from cho_inner_finance f");
        sql.append(" left join cho_inner_finance r on f.related_Bill_Id=r.id");
        sql.append(" where f.id=:id");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, ChoInnerFinanceVO.class);
    }
    /**
     * 根据billNo获取理财信息
     * @param billNo
     * @return
     */
    public ChoInnerFinanceVO getByBillNo(String billNo){
        StringBuilder sql=new StringBuilder();
        sql.append("select f.id as id,f.bill_No as billNo,f.corp_Id as corpId,f.corp_name as corpName,f.bus_Type as busType,f.payer_Acc_Id as payerAccId,f.payer_Acc as payerAcc,f.net_id as netId,");
        sql.append(" f.payee_Acc_Id as payeeAccId,f.payee_Acc as payeeAcc,f.capital_Acc_Id as capitalAccId,f.interest_Acc_Id as interestAccId,f.deposit_Rate_Type as depositRateType,");
        sql.append(" f.cur_Id as curId,f.cur_name as curName,f.pay_Money as payMoney,f.day_Limit as dayLimit,f.date_Unit as dateUnit,f.certificate_Way as certificateWay,f.interest_Start_Date as interestStartDate,");
        sql.append(" f.deadline_Date as deadlineDate,f.interest_Rate as interestRate,f.purpose as purpose,f.add_Word as addWord,f.pay_Date as payDate,f.related_Bill_Id as relatedBillId,");
        sql.append(" f.expect_Interest as expectInterest,f.status as status,f.tally_Date as tallyDate,f.order_No as orderNo,f.voucher_No as voucherNo,f.tally_Sign as tallySign,f.tally_Code as tallyCode,");
        sql.append(" f.create_By as createBy,f.create_Date as createDate,f.update_By as updateBy,f.update_Date as updateDate,f.notice_Type as noticeType,f.period_name as depositRateTypeName,r.bill_No as relatedBillNo,");
        sql.append(" f.payer_acc_name as payerAccName,f.payee_acc_Name as payeeAccName,f.capital_acc as capitalAcc,f.capital_acc_name as capitalAccName,f.interest_acc as interestAcc,f.interest_acc_name as interestAccName,");
        sql.append(" (select bt_nvl_num(sum(t.pay_money),0) from cho_inner_finance t where t.related_Bill_Id = f.id and t.status > " + InnerFinanceConstant.STATUE_BACK);
        sql.append(" and t.bus_type in ("+InnerFinanceConstant.BUS_TYPE_T2C+","+InnerFinanceConstant.BUS_TYPE_N2C+")) as drawMoney,f.acc_interest_rate as accInterestRate,");
        sql.append(" (select bt_nvl_num(sum(t.pay_money),0) from cho_inner_finance t where t.related_Bill_Id = f.id and t.status >= " + InnerFinanceConstant.STATUE_BACK+" and t.status <=" + InnerFinanceConstant.STATUE_CHECK);
        sql.append(" and t.bus_type ="+InnerFinanceConstant.BUS_TYPE_N2B+") as bookMoney");
        sql.append(" from cho_inner_finance f");
        sql.append(" left join cho_inner_finance r on f.related_Bill_Id=r.id");
        sql.append(" where f.bill_no = :billNo");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("billNo", billNo);
        return getBySQLAliasToBean(sql.toString(), params, ChoInnerFinanceVO.class);
    }
    public DataTablesOutput<ChoInnerFinanceVO> dataTableApprove(DataTablesInput dataTablesInput, String businessSql) {
        StringBuilder sql=new StringBuilder(this.getCommonSql());
        sql.append(" and " + businessSql);
        String flowStatus = dataTablesInput.getParameter().get("flowStatus");
        //已审批不过滤状态
        if(!IWorkFlowService.PASS.equals(flowStatus)){
            sql.append(" and f.status<> ").append(NetbankConstant.STATUE_CHECK);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), ChoInnerFinanceVO.class, "f", false);
    }
    /**
     * 存款类业务查询
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesQuery(DataTablesInput dataTablesInput,String joinSql) {
        StringBuilder sql = new StringBuilder();
        sql.append("select f.id as id,f.bill_No as billNo,f.corp_Id as corpId,f.corp_name as corpName,f.bus_Type as busType,f.payer_Acc_Id as payerAccId,f.payer_Acc as payerAcc,f.net_id as netId,");
        sql.append(" f.payee_Acc_Id as payeeAccId,f.payee_Acc as payeeAcc,f.capital_Acc_Id as capitalAccId,f.interest_Acc_Id as interestAccId,f.deposit_Rate_Type as depositRateType,");
        sql.append(" f.cur_Id as curId,f.cur_name as curName,f.pay_Money as payMoney,f.day_Limit as dayLimit,f.date_Unit as dateUnit,f.certificate_Way as certificateWay,f.interest_Start_Date as interestStartDate,");
        sql.append(" f.deadline_Date as deadlineDate,f.interest_Rate as interestRate,f.purpose as purpose,f.add_Word as addWord,f.pay_Date as payDate,f.related_Bill_Id as relatedBillId,");
        sql.append(" f.expect_Interest as expectInterest,f.status as status,f.tally_Date as tallyDate,f.order_No as orderNo,f.voucher_No as voucherNo,f.tally_Sign as tallySign,f.tally_Code as tallyCode,");
        sql.append(" f.create_By as createBy,f.create_Date as createDate,f.update_By as updateBy,f.update_Date as updateDate,f.notice_Type as noticeType,f.period_name as depositRateTypeName,r.bill_No as relatedBillNo,");
        sql.append(" f.payer_acc_name as payerAccName,f.payee_acc_Name as payeeAccName,f.capital_acc as capitalAcc,f.capital_acc_name as capitalAccName,f.interest_acc as interestAcc,f.interest_acc_name as interestAccName,");
        sql.append(" (select bt_nvl_num(sum(t.pay_money),0) from cho_inner_finance t where t.related_Bill_Id = f.id and t.status >= " + InnerFinanceConstant.STATUE_BACK);
        sql.append(" and t.bus_type in ("+InnerFinanceConstant.BUS_TYPE_T2C+","+InnerFinanceConstant.BUS_TYPE_N2C+")) as drawMoney,f.acc_interest_rate as accInterestRate,");
        sql.append(" (select bt_nvl_num(sum(t.pay_money),0) from cho_inner_finance t where t.related_Bill_Id = f.id and t.status >= " + InnerFinanceConstant.STATUE_BACK+" and t.status <=" + InnerFinanceConstant.STATUE_CHECK);
        sql.append(" and t.bus_type ="+InnerFinanceConstant.BUS_TYPE_N2B+") as bookMoney");
        sql.append(" from cho_inner_finance f");
        sql.append(" left join cho_inner_finance r on f.related_Bill_Id=r.id");
        sql.append(" where 1=1 and f.status <> " + InnerFinanceConstant.STATUE_DEL);
        //交易日期
        String gedTranDate = (String) dataTablesInput.getFormdata().get("ged_tranDate");
        String ledTranDate = (String) dataTablesInput.getFormdata().get("led_tranDate");
        if(StringUtils.isNotEmpty(gedTranDate)){
            sql.append(" and ((f.bus_type in ("+InnerFinanceConstant.BUS_TYPE_C2T+","+InnerFinanceConstant.BUS_TYPE_C2N+") and f.interest_start_date >= bt_to_date('"+gedTranDate+"', 'yyyy-MM-dd' ))");
            sql.append("or (f.bus_type not in ("+InnerFinanceConstant.BUS_TYPE_C2T+","+InnerFinanceConstant.BUS_TYPE_C2N+") and f.pay_date>=bt_to_date('"+gedTranDate+"', 'yyyy-MM-dd')))");
        }
        if(StringUtils.isNotEmpty(ledTranDate)){
            sql.append(" and ((f.bus_type in ("+InnerFinanceConstant.BUS_TYPE_C2T+","+InnerFinanceConstant.BUS_TYPE_C2N+") and f.interest_start_date <= bt_to_date('"+ledTranDate+"', 'yyyy-MM-dd' ))");
            sql.append(" or (f.bus_type not in ("+InnerFinanceConstant.BUS_TYPE_C2T+","+InnerFinanceConstant.BUS_TYPE_C2N+") and f.pay_date<=bt_to_date('"+ledTranDate+"', 'yyyy-MM-dd')))");
        }
        if(StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), ChoInnerFinanceVO.class, "f", false);
    }
    /**
     * 根据业务类型和状态获取内部理财信息集合
     * @param busType 业务类型 10-活期转定期 11-定期转活期 12-活期转通知 13-通知转活期 14-通知预约 15-取消通知预约
     * @param status 状态
     * @return
     */
    public DataTablesOutput listByBusType(DataTablesInput dataTablesInput,Integer busType, String status, String opType, String userId, List<String> corpAuth) {
        StringBuilder sql=new StringBuilder(this.getCommonSql());
        sql.append(" and f.bus_Type = "+ busType);
        if("Init".equals(opType)){
            sql.append(" and f.status in (95,99) and (f.tally_Sign ='' or f.tally_Sign in(0,6) or f.tally_sign is null)");
        }
        if("Edit".equals(opType)){
            sql.append(" and f.status in (96,98) and (f.tally_Sign in (5,4,3))and f.tally_code = '").append(userId).append("'");;
        }
        if("Check".equals(opType)){
            sql.append(" and f.status = 97 and (f.tally_Sign ='3')  and f.tally_code != '").append(userId).append("'");;
        }
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "f.corp_id"));
        return getDynamicBySQL(dataTablesInput,sql.toString(), ChoInnerFinanceDTO.class,"",false);
    }
    /**
     * 根据ID获取理财信息
     * @param id
     * @return
     */
    public ChoInnerFinanceDTO getInnerDepositById(String id){
        StringBuilder sql=new StringBuilder(this.getCommonSql());
        sql.append(" and f.id=:id");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, ChoInnerFinanceDTO.class);
    }
    /**
     * 根据billNo获取理财信息
     * @param billNo
     * @return
     */
    public ChoInnerFinanceDTO getInnerDepositByBillNo(String billNo){
        StringBuilder sql=new StringBuilder(this.getCommonSql());
        sql.append(" and f.bill_no=:billNo");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("billNo", billNo);
        return getBySQLAliasToBean(sql.toString(), params, ChoInnerFinanceDTO.class);
    }
    /**
     * 内部存款应收利息表
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesReport(DataTablesInput dataTablesInput,String joinSql) {
        //截止日期
        String  calEndDate= (String) dataTablesInput.getFormdata().get("calEndDate");
        StringBuilder sql = new StringBuilder();
        sql.append("select f.id as id,f.bill_No as billNo,f.corp_Id as corpId,f.corp_name as corpName,f.bus_Type as busType,f.payer_Acc_Id as payerAccId,f.payer_Acc as payerAcc,f.payer_acc_name as payerAccName,");
        sql.append(" f.payee_Acc_Id as payeeAccId,f.payee_Acc as payeeAcc,f.capital_Acc_Id as capitalAccId,f.interest_Acc_Id as interestAccId,f.deposit_Rate_Type as depositRateType,");
        sql.append(" f.cur_Id as curId,f.cur_name as curName,f.pay_Money as payMoney,f.day_Limit as dayLimit,f.date_Unit as dateUnit,f.certificate_Way as certificateWay,f.interest_Start_Date as interestStartDate,");
        sql.append(" f.deadline_Date as deadlineDate,f.interest_Rate as interestRate,f.purpose as purpose,f.add_Word as addWord,f.pay_Date as payDate,f.related_Bill_Id as relatedBillId,");
        sql.append(" f.expect_Interest as expectInterest,f.status as status,f.tally_Date as tallyDate,f.order_No as orderNo,f.voucher_No as voucherNo,f.tally_Sign as tallySign,f.tally_Code as tallyCode,");
        sql.append(" f.create_By as createBy,f.create_Date as createDate,f.update_By as updateBy,f.update_Date as updateDate,f.notice_Type as noticeType,f.acc_interest_rate as accInterestRate,");
        sql.append(" (select bt_nvl_num(sum(t.pay_money),0) from cho_inner_finance t where t.related_Bill_Id = f.id and t.status = " + InnerFinanceConstant.STATUE_TALLY_CHECKPASS);
        sql.append(" and t.bus_type in ("+InnerFinanceConstant.BUS_TYPE_T2C+","+InnerFinanceConstant.BUS_TYPE_N2C+")) as drawMoney,");
        sql.append(" bt_to_date('" + calEndDate + "','YYYY-MM-DD') as calEndDate");
        sql.append(" from cho_inner_finance f ");
        //取定期存款和活期存款信息
        sql.append(" where f.bus_type in ("+InnerFinanceConstant.BUS_TYPE_C2T+","+InnerFinanceConstant.BUS_TYPE_C2N+")");
        //记账复核通过的
        sql.append(" and f.status = "+InnerFinanceConstant.STATUE_TALLY_CHECKPASS);
        //起息日期小于等于截止日期
        sql.append(" and f.interest_Start_Date <= bt_to_date('" + calEndDate + "','YYYY-MM-DD')");
        //取款未完成的
        sql.append(" and ((select bt_nvl_num(sum(t.pay_money), 0) from cho_inner_finance t where t.related_Bill_Id = f.id and t.status ="+InnerFinanceConstant.STATUE_TALLY_CHECKPASS+") < f.pay_money)");
        if(StringUtils.isNotEmpty(joinSql)) {
            sql.append(joinSql);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), ChoInnerFinanceVO.class, "f", false);
    }
    /**
     * 根据存款单ID获取存款明细
     *
     * @param relatedBillId
     * @return
     */
    public List<ChoInnerFinanceVO> listByRelatedBillId(String relatedBillId) {
        StringBuilder sql=new StringBuilder();
        sql.append("select f.id as id,f.bill_No as billNo,f.corp_Id as corpId,f.corp_name as corpName,f.bus_Type as busType,f.payer_Acc_Id as payerAccId,f.payer_Acc as payerAcc,");
        sql.append(" f.payee_Acc_Id as payeeAccId,f.payee_Acc as payeeAcc,f.capital_Acc_Id as capitalAccId,f.interest_Acc_Id as interestAccId,f.deposit_Rate_Type as depositRateType,");
        sql.append(" f.cur_Id as curId,f.cur_name as curName,f.pay_Money as payMoney,f.day_Limit as dayLimit,f.date_Unit as dateUnit,f.certificate_Way as certificateWay,f.interest_Start_Date as interestStartDate,");
        sql.append(" f.deadline_Date as deadlineDate,f.interest_Rate as interestRate,f.purpose as purpose,f.add_Word as addWord,f.pay_Date as payDate,f.related_Bill_Id as relatedBillId,");
        sql.append(" f.expect_Interest as expectInterest,f.status as status,f.tally_Date as tallyDate,f.order_No as orderNo,f.voucher_No as voucherNo,f.tally_Sign as tallySign,f.tally_Code as tallyCode,");
        sql.append(" f.create_By as createBy,f.create_Date as createDate,f.update_By as updateBy,f.update_Date as updateDate,f.notice_Type as noticeType");
        sql.append(" from cho_inner_finance f");
        sql.append(" where f.related_Bill_Id=:relatedBillId");
        sql.append(" and f.status="+InnerFinanceConstant.STATUE_TALLY_CHECKPASS);
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("relatedBillId", relatedBillId);
        return this.listBySQLAliasToBean(sql.toString(), params, ChoInnerFinanceVO.class);
    }
    /**
     * 根据业务类型和状态获取内部理财信息集合
     * @param busType 业务类型 10-活期转定期 11-定期转活期 12-活期转通知 13-通知转活期 14-通知预约 15-取消通知预约
     * @param beginDate 开始日期
     * @param endDate 结束日期
     * @return
     */
    public List<ChoInnerFinanceVO> listByBusType(Integer busType, Date beginDate, Date endDate) {
        StringBuilder sql = new StringBuilder();
        sql.append("select f.id as id,f.bill_No as billNo,f.corp_Id as corpId,f.corp_name as corpName,f.bus_Type as busType,f.payer_Acc_Id as payerAccId,f.payer_Acc as payerAcc,f.net_id as netId,");
        sql.append(" f.payee_Acc_Id as payeeAccId,f.payee_Acc as payeeAcc,f.capital_Acc_Id as capitalAccId,f.interest_Acc_Id as interestAccId,f.deposit_Rate_Type as depositRateType,");
        sql.append(" f.cur_Id as curId,f.cur_name as curName,f.pay_Money as payMoney,f.day_Limit as dayLimit,f.date_Unit as dateUnit,f.certificate_Way as certificateWay,f.interest_Start_Date as interestStartDate,");
        sql.append(" f.deadline_Date as deadlineDate,f.interest_Rate as interestRate,f.purpose as purpose,f.add_Word as addWord,f.pay_Date as payDate,f.related_Bill_Id as relatedBillId,");
        sql.append(" f.expect_Interest as expectInterest,f.status as status,f.tally_Date as tallyDate,f.order_No as orderNo,f.voucher_No as voucherNo,f.tally_Sign as tallySign,f.tally_Code as tallyCode,");
        sql.append(" f.create_By as createBy,f.create_Date as createDate,f.update_By as updateBy,f.update_Date as updateDate,f.notice_Type as noticeType,f.period_name as depositRateTypeName,r.bill_No as relatedBillNo,");
        sql.append(" f.payer_acc_name as payerAccName,f.payee_acc_Name as payeeAccName,f.capital_acc as capitalAcc,f.capital_acc_name as capitalAccName,f.interest_acc as interestAcc,f.interest_acc_name as interestAccName,");
        sql.append(" (select bt_nvl_num(sum(t.pay_money),0) from cho_inner_finance t where t.related_Bill_Id = f.id and t.status >= " + InnerFinanceConstant.STATUE_BACK);
        sql.append(" and t.bus_type in ("+InnerFinanceConstant.BUS_TYPE_T2C+","+InnerFinanceConstant.BUS_TYPE_N2C+")) as drawMoney,f.acc_interest_rate as accInterestRate");
        sql.append(" from cho_inner_finance f");
        sql.append(" left join cho_inner_finance r on f.related_Bill_Id=r.id");
        sql.append(" where 1=1");
        HashMap<String, Object> params = new HashMap<String, Object>();
        if(InnerFinanceConstant.BUS_TYPE_N2B.equals(busType)) {
            //通知预约：预约日期过滤
            sql.append(" and f.status=:status and f.bus_Type=:busType and f.pay_Date>=:beginDate and f.pay_Date<=:endDate");
            params.put("status", InnerFinanceConstant.STATUE_CHECK);
        }else{
            //定期存款：到期日期过滤
            sql.append(" and f.status=:status and f.bus_Type=:busType and f.deadline_Date>=:beginDate and f.deadline_Date<=:endDate");
            sql.append(" and f.pay_Date is null");
            params.put("status", InnerFinanceConstant.STATUE_TALLY_CHECKPASS);
        }
        params.put("busType", busType);
        params.put("beginDate", beginDate);
        params.put("endDate", endDate);
        return listBySQLAliasToBean(sql.toString(), params, ChoInnerFinanceVO.class);
    }
    /**
     * 根据单位和业务类型统计存款信息
     * @param corpId
     * @param busType
     * @return
     */
    public int getInnerFinanceCountByCorpId(String corpId, Integer busType){
        String sql="select count(*) from cho_inner_finance where corp_id=:corpId and bus_Type=:busType";
        HashMap<String,Object> params=new HashMap<>();
        params.put("corpId",corpId);
        params.put("busType",busType);
        //0-暂存、11-待审批、-1-审批打回
        sql+=" and status in ('0','11','-1')";
        return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
    }
    /**
     * 根据单位和业务类型统计存款信息（记账）
     * @param corpId
     * @return
     */
    public int getInnerFinanceCountByCorpIdForTally(String corpId){
        String sql="select count(*) from cho_inner_finance where corp_id=:corpId";
        HashMap<String,Object> params=new HashMap<>();
        params.put("corpId",corpId);
        sql+=" and bus_Type in ('10','11','12','13') and status >=95 and (tally_sign is null or tally_sign<>'1')";
        return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
    }
    /**
     * 根据单位和业务类型统计未完成取款的存款信息
     * @param corpId
     * @param busType
     * @return
     */
    public int getInnerFinanceDrawCountByCorpId(String corpId, Integer busType){
        String sql="select count(*) from cho_inner_finance c where c.corp_id=:corpId and c.bus_Type=:busType and c.status=:status";
        sql+=" and c.pay_money > (select bt_nvl_num(sum(d.pay_money),0) from cho_inner_finance d where d.related_Bill_Id=c.id and d.status=:status)";
        HashMap<String,Object> params=new HashMap<>();
        params.put("corpId",corpId);
        params.put("busType",busType);
        params.put("status",InnerFinanceConstant.STATUE_TALLY_CHECKPASS);
        return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
    }
    /**
     * 查询待记账数据 // todo:fc_tally_remind_dtl
     */
    public List<ChoInnerFinanceDTO> queryTallyByBusType(Integer busType) {
        StringBuilder sql=new StringBuilder();
        sql.append("select f.id as id,f.pay_Money as payMoney ,f.corp_id as corpId ");
        sql.append(" from cho_inner_finance f");
        sql.append(" where 1=1");
        sql.append(" and not exists( select 1 from fc_tally_remind_dtl d where d.bill_id = f.id ) ");
        sql.append(" and f.bus_Type = "+ busType);
        sql.append(" and f.status in (95,99) and (f.tally_Sign ='' or f.tally_Sign in(0,6) or f.tally_sign is null)");
        return listBySQLAliasToBean(sql.toString(),null,ChoInnerFinanceDTO.class);
    }
    /**
    * <br>
    * <b>功能：</b> 查询通知取款最近的通知预约记录 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2021/12/16 0016 <br>
    */
    public ChoInnerFinanceDTO queryLastN2BData(ChoInnerFinanceDTO entity) {
        StringBuilder sql=new StringBuilder();
        sql.append("select f.id as id,f.status as status ,f.bus_Type as busType ");
        sql.append(" from cho_inner_finance f");
        sql.append(" where 1=1 ");
        sql.append(" and f.payer_acc_id = :payerAccId ");
        sql.append(" and f.bus_type = :busType ");
        sql.append(" and f.status = :status ");
        sql.append(" order by f.pay_date, f.create_date ");
        HashMap<String,Object> params=new HashMap<>();
        params.put("payerAccId", entity.getPayerAccId());
        params.put("busType", InnerFinanceConstant.BUS_TYPE_N2B);
        params.put("status", InnerFinanceConstant.STATUE_CHECK);
        List<ChoInnerFinanceDTO> list = listBySQLAliasToBean(sql.toString(), params, ChoInnerFinanceDTO.class);
        if (!EmptyUtils.isEmptyList(list)) {
            return list.get(0);
        }
        return null;
    }
    /**
     * <br>
     * <b>功能：</b> 更新通知预约记录 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/12/16 0016 <br>
     */
    public void updateLastN2BData(ChoInnerFinanceDTO entity) {
        StringBuilder sql = new StringBuilder();
        sql.append("update cho_inner_finance f set status = :newStatus ");
        sql.append(" where 1=1 ");
        sql.append(" and f.id = :id ");
        sql.append(" and f.bus_type = :busType ");
        sql.append(" and f.status = :status ");
        HashMap<String,Object> params=new HashMap<>();
        params.put("id", entity.getId());
        params.put("busType", InnerFinanceConstant.BUS_TYPE_N2B);
        params.put("status", InnerFinanceConstant.STATUE_CHECK);
        params.put("newStatus", InnerFinanceConstant.STATUE_COMPLETE);
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    public List<ChoInnerFinanceDTO> getChoInnerFinance(Integer[] status, Integer billType) {
        Map<String, Object> pageParams = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select f.* from cho_inner_finance f ");
        sql.append(" where f.status in (:status) and f.bus_type = :billType ");
        sql.append(" order by f.payer_acc desc");
        pageParams.put("status", status);
        pageParams.put("billType", billType);
        return listBySQLAliasToBean(sql.toString(), pageParams, ChoInnerFinanceDTO.class);
    }

    /**
     * 用于上面 this.getNoticeAccount 方法的
     * @param blnctrAccs 帐号过滤？
     * @return
     */
    public List<String> filterBankAcc(List<String> blnctrAccs){
        StringBuffer sql = new StringBuffer();
        sql.append(" select b.payee_acc as blnctrAcc from cho_inner_finance b where 1=1 ");
        sql.append(" and ").append(SqlUtil.getInSql(blnctrAccs, "b.payee_acc", true));
        Map<String, String> params = Maps.newHashMap();
        return this.listBySQLAliasToBean(sql.toString(), params, String.class);
    }
}
