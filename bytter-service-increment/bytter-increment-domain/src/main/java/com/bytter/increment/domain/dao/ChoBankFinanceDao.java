package com.bytter.increment.domain.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.BdlinkConstant;
import com.bytter.core.constant.NetbankConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.api.constant.BankFinanceConstant;
import com.bytter.increment.domain.entity.ChoBankFinance;
import com.bytter.increment.domain.vo.ChoBankFinanceVO;
import com.bytter.workflow.api.IWorkFlowService;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
/**
* @Title: Dao
* @Description: CHO_BANK_FINANCE
* @author onlineGenerator
* @date 2017-08-18 14:03:30
* @version 1.0
*
*/
@Repository
public class ChoBankFinanceDao extends BaseDao<ChoBankFinance,String>{

	/**
	 * 查询理财信息
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput,String joinSql) {
		StringBuilder sql=new StringBuilder();
		sql.append("select f.id as id,f.bill_No as billNo,f.corp_Id as corpId,f.corp_name as corpName,f.bus_Type as busType,f.pay_Bill_Id as payBillId,");
		sql.append(" f.payer_Acc_Id as payerAccId,f.payer_Acc as payerAcc,f.balance as balance,f.payee_Acc_Id as payeeAccId,f.payee_Acc as payeeAcc,");
		sql.append(" f.cur_Id as curId,f.cur_name as curName,f.pay_Money as payMoney,f.day_Limit as dayLimit,f.date_Unit as dateUnit,f.bursar_Bill_No as bursarBillNo,");
		sql.append(" f.certificate_Way as certificateWay,f.wish_Payday as wishPayday,f.interest_Rate as interestRate,f.purpose as purpose,f.add_Word as addWord,");
		sql.append(" f.notify_Time as notifyTime,f.is_Bl as isBl,f.bank_Bill_No as bankBillNo,f.bl_Interest as blInterest,f.bl_Interest_Rate as blInterestRate,");
		sql.append(" f.status as status,f.username as createBy,f.create_Date as createDate,f.update_By as updateBy,f.update_Date as updateDate,");
		sql.append(" f.acc_Name as payerAccName,f.bank_Name as payerBank,e.voucher_Stat as voucherStat,e.id as excId,f.interest_Start_Date as interestStartDate,f.deadline_Date as deadlineDate,");
		sql.append(" f.is_Online as isOnline,f.bank_Acc_Online as bankAccOnline");
		sql.append(" from cho_bank_finance f ");
		sql.append(" left join bis_finance_exc e on f.id = e.cbs_bill_id ");
		sql.append(" where 1=1 ");
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
		return getDynamicBySQL(dataTablesInput, sql.toString(), ChoBankFinanceVO.class, "f", false);
	}
	/**
	 * 查询理财信息(组件使用)
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnionForSelect(DataTablesInput dataTablesInput,String joinSql) {
		StringBuilder sql=new StringBuilder();
		sql.append("select f.id as id,f.bill_No as billNo,f.corp_Id as corpId,f.corp_name as corpName,f.bus_Type as busType,f.pay_Bill_Id as payBillId,");
		sql.append(" f.payer_Acc_Id as payerAccId,f.payer_Acc as payerAcc,f.balance as balance,f.payee_Acc_Id as payeeAccId,f.payee_Acc as payeeAcc,");
		sql.append(" f.cur_Id as curId,f.cur_name as curName,f.pay_Money as payMoney,f.day_Limit as dayLimit,f.date_Unit as dateUnit,f.bursar_Bill_No as bursarBillNo,");
		sql.append(" f.certificate_Way as certificateWay,f.wish_Payday as wishPayday,f.interest_Rate as interestRate,f.purpose as purpose,f.add_Word as addWord,");
		sql.append(" f.notify_Time as notifyTime,f.is_Bl as isBl,f.bank_Bill_No as bankBillNo,f.bl_Interest as blInterest,f.bl_Interest_Rate as blInterestRate,");
		sql.append(" f.status as status,f.username as createBy,f.create_Date as createDate,f.update_By as updateBy,f.update_Date as updateDate,");
		sql.append(" f.acc_Name as payerAccName,f.bank_Name as payerBank,e.voucher_Stat as voucherStat,f.interest_Start_Date as interestStartDate,f.deadline_Date as deadlineDate,");
		sql.append(" f.is_Online as isOnline,f.bank_Acc_Online as bankAccOnline");
		sql.append(" from cho_bank_finance f ");
		sql.append(" left join bis_finance_exc e on f.id = e.cbs_bill_id ");
		sql.append(" where 1=1 ");
		if(StringUtils.isNotEmpty(joinSql)) {
			sql.append(joinSql);
		}
		sql.append(" and (f.COLLECTION_CONFIRM = '0' or f.COLLECTION_CONFIRM is null)");
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
		return getDynamicBySQL(dataTablesInput, sql.toString(), ChoBankFinanceVO.class, "f", false);
	}
	/**
	 * 根据ID获取理财信息
	 * @param id
	 * @return
	 */
	public ChoBankFinanceVO getByIdUnion(String id){
		StringBuilder sql=new StringBuilder();
		sql.append("select f.id as id,f.bill_No as billNo,f.corp_Id as corpId,f.corp_name as corpName,f.bus_Type as busType,f.pay_Bill_Id as payBillId,");
		sql.append(" f.payer_Acc_Id as payerAccId,f.payer_Acc as payerAcc,f.balance as balance,f.payee_Acc_Id as payeeAccId,f.payee_Acc as payeeAcc,");
		sql.append(" f.cur_Id as curId,f.cur_name as curName,f.pay_Money as payMoney,f.day_Limit as dayLimit,f.date_Unit as dateUnit,f.bursar_Bill_No as bursarBillNo,");
		sql.append(" f.certificate_Way as certificateWay,f.wish_Payday as wishPayday,f.interest_Rate as interestRate,f.purpose as purpose,f.add_Word as addWord,");
		sql.append(" f.notify_Time as notifyTime,f.is_Bl as isBl,f.bank_Bill_No as bankBillNo,f.bl_Interest as blInterest,f.bl_Interest_Rate as blInterestRate,");
		sql.append(" f.status as status,f.create_By as createBy,f.create_Date as createDate,f.update_By as updateBy,f.update_Date as updateDate,");
		sql.append(" f.acc_Name as payerAccName,f.bank_Name as payerBank,f.interest_Start_Date as interestStartDate,f.deadline_Date as deadlineDate,");
		sql.append(" f.is_Online as isOnline,f.bank_Acc_Online as bankAccOnline");
		sql.append(" from cho_bank_finance f where f.id=:id");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, ChoBankFinanceVO.class);
	}
	public DataTablesOutput<ChoBankFinanceVO> dataTableApprove(DataTablesInput dataTablesInput, String businessSql) {
		StringBuilder sql=new StringBuilder();
		sql.append("select f.id as id,f.bill_No as billNo,f.corp_Id as corpId,f.corp_name as corpName,f.bus_Type as busType,f.pay_Bill_Id as payBillId,");
		sql.append(" f.payer_Acc_Id as payerAccId,f.payer_Acc as payerAcc,f.balance as balance,f.payee_Acc_Id as payeeAccId,f.payee_Acc as payeeAcc,");
		sql.append(" f.cur_Id as curId,f.cur_name as curName,f.pay_Money as payMoney,f.day_Limit as dayLimit,f.date_Unit as dateUnit,f.bursar_Bill_No as bursarBillNo,");
		sql.append(" f.certificate_Way as certificateWay,f.wish_Payday as wishPayday,f.interest_Rate as interestRate,f.purpose as purpose,f.add_Word as addWord,");
		sql.append(" f.notify_Time as notifyTime,f.is_Bl as isBl,f.bank_Bill_No as bankBillNo,f.bl_Interest as blInterest,f.bl_Interest_Rate as blInterestRate,");
		sql.append(" f.status as status,f.create_By as createBy,f.create_Date as createDate,f.update_By as updateBy,f.update_Date as updateDate,");
		sql.append(" f.acc_Name as payerAccName,f.bank_Name as payerBank,f.interest_Start_Date as interestStartDate,f.deadline_Date as deadlineDate,");
		sql.append(" f.is_Online as isOnline,f.bank_Acc_Online as bankAccOnline");
		sql.append(" from cho_bank_finance f  where 1=1");
		if (!StringUtils.isBlank(businessSql)){
			sql.append(" and " + businessSql);
		}
		String flowStatus = dataTablesInput.getParameter().get("flowStatus");
		//已审批不过滤状态
		if(!IWorkFlowService.PASS.equals(flowStatus)){
			sql.append(" and f.status<> ").append(NetbankConstant.STATUE_CHECK);
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), ChoBankFinanceVO.class, "f", false);
	}
	/**
	 * 存款类业务查询
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesQuery(DataTablesInput dataTablesInput,String joinSql) {
		StringBuilder sql=new StringBuilder();
		sql.append("select f.id as id,f.bill_No as billNo,f.corp_Id as corpId,f.corp_name as corpName,f.bus_Type as busType,f.pay_Bill_Id as payBillId,");
		sql.append(" f.payer_Acc_Id as payerAccId,f.payer_Acc as payerAcc,f.balance as balance,f.payee_Acc_Id as payeeAccId,f.payee_Acc as payeeAcc,");
		sql.append(" f.cur_Id as curId,f.cur_name as curName,f.pay_Money as payMoney,f.day_Limit as dayLimit,f.date_Unit as dateUnit,f.bursar_Bill_No as bursarBillNo,");
		sql.append(" f.certificate_Way as certificateWay,f.wish_Payday as wishPayday,f.interest_Rate as interestRate,f.purpose as purpose,f.add_Word as addWord,");
		sql.append(" f.notify_Time as notifyTime,f.is_Bl as isBl,f.bank_Bill_No as bankBillNo,f.bl_Interest as blInterest,f.bl_Interest_Rate as blInterestRate,");
		sql.append(" f.status as status,f.create_By as createBy,f.create_Date as createDate,f.update_By as updateBy,f.update_Date as updateDate,");
		sql.append(" f.acc_Name as payerAccName,f.bank_Name as payerBank,e.voucher_Stat as voucherStat,e.id as excId,f.time_balance as timeBalance,f.interest_Start_Date as interestStartDate,f.deadline_Date as deadlineDate,");
		sql.append(" f.is_Online as isOnline,f.bank_Acc_Online as bankAccOnline,");
		//已支取金额包含待审批、（交易指令）待发送、（交易指令）已发送的，暂存、打回、交易失败的不包含
		//例如：定期有100万，定转活审批通过了20万，待审批的10万，待发送的15万，交易失败的30万，那新增一笔定转活时，交易金额为<=55万。
		String drawMoneyExpr = "(case f.is_online when '0' then (select sum(t.pay_money) from cho_bank_finance t where t.pay_bill_id = f.id and t.status > "
				+ BankFinanceConstant.STATUE_TEMP + ") else "
				+ "(select sum(t.pay_money) from cho_bank_finance t left join bis_finance_exc e1 on t.id = e1.cbs_bill_id where t.pay_bill_id = f.id and t.status > "
				+ BankFinanceConstant.STATUE_TEMP + " and (e1.voucher_stat in ('" + BdlinkConstant.WAIT_FILL + "','"
				+ BdlinkConstant.PRE_SEND + "','" + BdlinkConstant.SUCCESS + "') or e1.voucher_stat is null)) end)";
		sql.append(" ").append(drawMoneyExpr).append(" as drawMoney,");
		sql.append(" (f.pay_money - bt_nvl_num(").append(drawMoneyExpr).append(", 0)) as drawBal");
		sql.append(" from cho_bank_finance f ");
		sql.append(" left join bis_finance_exc e on f.id = e.cbs_bill_id ");
		sql.append(" where (f.bus_type = "+ BankFinanceConstant.BUS_TYPE_C2T +" or f.bus_type = " + BankFinanceConstant.BUS_TYPE_C2N+")");
		if(StringUtils.isNotEmpty(joinSql)) {
			sql.append(joinSql);
		}
		//起息日期
		String  gedInterestStartDate= (String) dataTablesInput.getFormdata().get("ged_interestStartDate");
		String  ledInterestStartDate= (String) dataTablesInput.getFormdata().get("led_interestStartDate");
		if(StringUtils.isNotEmpty(gedInterestStartDate)){
			gedInterestStartDate= gedInterestStartDate + " 00:00:00";
			dataTablesInput.getFormdata().put("ged_interestStartDate", gedInterestStartDate);
		}
		if(StringUtils.isNotEmpty(ledInterestStartDate)){
			ledInterestStartDate = ledInterestStartDate + " 23:59:59";
			dataTablesInput.getFormdata().put("led_interestStartDate", ledInterestStartDate);
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), ChoBankFinanceVO.class, "", false);
	}
	/**
	 * 银行存款应收未收利息统计表
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesReport(DataTablesInput dataTablesInput,String joinSql) {
		//截止日期
		String  calEndDate= (String) dataTablesInput.getFormdata().get("calEndDate");
		StringBuilder sql=new StringBuilder();
		sql.append("select f.id as id,f.bill_No as billNo,f.corp_Id as corpId,f.corp_name as corpName,f.bus_Type as busType,f.pay_Bill_Id as payBillId,");
		sql.append(" f.payer_Acc_Id as payerAccId,f.payer_Acc as payerAcc,f.balance as balance,f.payee_Acc_Id as payeeAccId,f.payee_Acc as payeeAcc,");
		sql.append(" f.cur_Id as curId,f.cur_name as curName,f.pay_Money as payMoney,f.day_Limit as dayLimit,f.date_Unit as dateUnit,f.bursar_Bill_No as bursarBillNo,");
		sql.append(" f.certificate_Way as certificateWay,f.wish_Payday as wishPayday,f.interest_Rate as interestRate,f.purpose as purpose,f.add_Word as addWord,");
		sql.append(" f.notify_Time as notifyTime,f.is_Bl as isBl,f.bank_Bill_No as bankBillNo,f.bl_Interest as blInterest,f.bl_Interest_Rate as blInterestRate,");
		sql.append(" f.status as status,f.create_By as createBy,f.create_Date as createDate,f.update_By as updateBy,f.update_Date as updateDate,");
		sql.append(" f.acc_Name as payerAccName,f.bank_Name as payerBank,e.voucher_Stat as voucherStat,e.id as excId,f.time_balance as timeBalance,f.interest_Start_Date as interestStartDate,f.deadline_Date as deadlineDate,");
		sql.append(" f.is_Online as isOnline,f.bank_Acc_Online as bankAccOnline,");
		sql.append(" (case f.is_online when '0' then (select sum(t.pay_money) from cho_bank_finance t where t.pay_bill_id = f.id and t.status = 95) else ");
		sql.append(" (select sum(t.pay_money) from cho_bank_finance t left join bis_finance_exc e1 on t.id = e1.cbs_bill_id where t.pay_bill_id = f.id and e1.voucher_stat = '0') end) as drawMoney,");
		sql.append(" (case f.is_online when '0' then (select max(t.wish_payday) from cho_bank_finance t where t.pay_bill_id = f.id and t.status = 95) else ");
		sql.append(" (select max(t.wish_payday) from cho_bank_finance t left join bis_finance_exc e1 on t.id = e1.cbs_bill_id where t.pay_bill_id = f.id and e1.voucher_stat = '0') end) as maxWishPayday,");
		sql.append(" bt_to_date('" + calEndDate + "','YYYY-MM-DD') as calEndDate");
		sql.append(" from cho_bank_finance f  ");
		sql.append(" left join bis_finance_exc e on f.id = e.cbs_bill_id ");
		sql.append(" where (f.bus_type = "+ BankFinanceConstant.BUS_TYPE_C2T +" or f.bus_type = " + BankFinanceConstant.BUS_TYPE_C2N+")");
		if(StringUtils.isNotEmpty(joinSql)) {
			sql.append(joinSql);
		}
		//起息日期
		String  gedInterestStartDate= (String) dataTablesInput.getFormdata().get("ged_interestStartDate");
		String  ledInterestStartDate= (String) dataTablesInput.getFormdata().get("led_interestStartDate");
		if(StringUtils.isNotEmpty(gedInterestStartDate)){
			gedInterestStartDate= gedInterestStartDate + " 00:00:00";
			dataTablesInput.getFormdata().put("ged_interestStartDate", gedInterestStartDate);
		}
		if(StringUtils.isNotEmpty(ledInterestStartDate)){
			ledInterestStartDate = ledInterestStartDate + " 23:59:59";
			dataTablesInput.getFormdata().put("led_interestStartDate", ledInterestStartDate);
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), ChoBankFinanceVO.class, "f", false);
	}
	/**
	 * 根据存款单ID获取存款明细
	 *
	 * @param payBillId
	 * @return
	 */
	public List<ChoBankFinanceVO> listByPayBillId(String payBillId) {
		StringBuilder sql=new StringBuilder();
		sql.append("select f.id as id,f.bill_No as billNo,f.corp_Id as corpId,f.corp_name as corpName,f.bus_Type as busType,f.pay_Bill_Id as payBillId,");
		sql.append(" f.payer_Acc_Id as payerAccId,f.payer_Acc as payerAcc,f.balance as balance,f.payee_Acc_Id as payeeAccId,f.payee_Acc as payeeAcc,");
		sql.append(" f.cur_Id as curId,f.cur_name as curName,f.pay_Money as payMoney,f.day_Limit as dayLimit,f.date_Unit as dateUnit,f.bursar_Bill_No as bursarBillNo,");
		sql.append(" f.certificate_Way as certificateWay,f.wish_Payday as wishPayday,f.interest_Rate as interestRate,f.purpose as purpose,f.add_Word as addWord,");
		sql.append(" f.notify_Time as notifyTime,f.is_Bl as isBl,f.bank_Bill_No as bankBillNo,f.bl_Interest as blInterest,f.bl_Interest_Rate as blInterestRate,");
		sql.append(" f.status as status,f.create_By as createBy,f.create_Date as createDate,f.update_By as updateBy,f.update_Date as updateDate,");
		sql.append(" f.acc_Name as payerAccName,f.bank_Name as payerBank,e.voucher_Stat as voucherStat,e.Id as excId,f.interest_Start_Date as interestStartDate,f.deadline_Date as deadlineDate,");
		sql.append(" f.is_Online as isOnline,f.bank_Acc_Online as bankAccOnline");
		sql.append(" from cho_bank_finance f ");
		sql.append(" left join bis_finance_exc e on f.id = e.cbs_bill_id ");
		sql.append(" where f.pay_Bill_Id = :payBillId");
		sql.append(" and f.status = '95' and ((f.is_online = '0') or (f.is_online = '1' and e.voucher_stat = '0'))");
		sql.append(" order by f.wish_Payday");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("payBillId", payBillId);
		return this.listBySQLAliasToBean(sql.toString(), params, ChoBankFinanceVO.class);
	}
	/**
	 * 根据单位和业务类型统计存款信息
	 * @param corpId
	 * @param busType
	 * @return
	 */
	public int getBankFinanceCountByCorpId(String corpId, Integer busType){
		String sql="select count(*) from cho_bank_finance where corp_id=:corpId and bus_Type=:busType";
		HashMap<String,Object> params=new HashMap<>();
		params.put("corpId",corpId);
		params.put("busType",busType);
		//0-暂存、11-待审批、-1-审批打回
		sql+=" and status in ('0','11','-1')";
		return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
	}
	/**
	 * 根据单位和业务类型统计待补录的存款信息
	 * @param corpId
	 * @param busType
	 * @return
	 */
	public int getBankFinanceBlCountByCorpId(String corpId, Integer busType){
		String sql="select count(*) from cho_bank_finance c where c.corp_id=:corpId and c.bus_Type=:busType";
		//95-审批通过
		sql+=" and c.status=:status and c.is_bl=:isBl";
		HashMap<String,Object> params=new HashMap<>();
		params.put("corpId",corpId);
		params.put("busType",busType);
		params.put("status", BankFinanceConstant.STATUE_CHECK);
		params.put("isBl", BankFinanceConstant.BL_0);
		return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
	}
	/**
	 * 存款类业务查询
	 * @param busType
	 * @param createDate
	 * @param createDate
	 * @return
	 */
	public List<ChoBankFinanceVO> listByBusType(String busType, Date createDate, Date deadlineDate, String joinSql) {
		StringBuilder sql=new StringBuilder();
		sql.append("select f.id as id,f.bill_No as billNo,f.corp_Id as corpId,f.corp_name as corpName,f.bus_Type as busType,f.pay_Bill_Id as payBillId,");
		sql.append(" f.payer_Acc_Id as payerAccId,f.payer_Acc as payerAcc,f.balance as balance,f.payee_Acc_Id as payeeAccId,f.payee_Acc as payeeAcc,");
		sql.append(" f.cur_Id as curId,f.cur_name as curName,f.pay_Money as payMoney,f.day_Limit as dayLimit,f.date_Unit as dateUnit,f.bursar_Bill_No as bursarBillNo,");
		sql.append(" f.certificate_Way as certificateWay,f.wish_Payday as wishPayday,f.interest_Rate as interestRate,f.purpose as purpose,f.add_Word as addWord,");
		sql.append(" f.notify_Time as notifyTime,f.is_Bl as isBl,f.bank_Bill_No as bankBillNo,f.bl_Interest as blInterest,f.bl_Interest_Rate as blInterestRate,");
		sql.append(" f.status as status,f.create_By as createBy,f.create_Date as createDate,f.update_By as updateBy,f.update_Date as updateDate,");
		sql.append(" b.acc_Name as payerAccName,b.bank_Name as payerBank,e.voucher_Stat as voucherStat,e.exc_Id as excId,f.time_balance as timeBalance,f.interest_Start_Date as interestStartDate,f.deadline_Date as deadlineDate,");
		//已支取金额包含待审批、（交易指令）待发送、（交易指令）已发送的，暂存、打回、交易失败的不包含
		//例如：定期有100万，定转活审批通过了20万，待审批的10万，待发送的15万，交易失败的30万，那新增一笔定转活时，交易金额为<=55万。
		sql.append(" (case f.is_online when '0' then (select sum(t.pay_money) from cho_bank_finance t where t.pay_bill_id = f.id and t.status > " + BankFinanceConstant.STATUE_TEMP + ") else ");
		sql.append(" (select sum(t.pay_money) from cho_bank_finance t left join bis_finance_exc e1 on t.id = e1.cbs_bill_id where t.pay_bill_id = f.id and t.status > " + BankFinanceConstant.STATUE_TEMP);
		sql.append("  and (e1.voucher_stat in ('" + BdlinkConstant.WAIT_FILL + "','" + BdlinkConstant.PRE_SEND + "','" + BdlinkConstant.SUCCESS + "') or e1.voucher_stat is null) and t.create_date<=:createDate) end) as drawMoney");
		sql.append(" from cho_bank_finance f left join bt_bank_acc b on f.payer_Acc_Id = b.id");
		sql.append(" left join bis_finance_exc e on f.id = e.cbs_bill_id ");
		sql.append(" where f.bus_type = :busType");
		sql.append(" and f.create_date <=:createDate");
		if(StringUtils.isNotEmpty(joinSql)) {
			sql.append(joinSql);
		}
		HashMap<String, Object> params = new HashMap<String, Object>();
		//到期日期：大于等于系统当前日期，小于等于到期日期
		if(StringUtils.isNotEmpty(deadlineDate)) {
			sql.append(" and f.deadline_Date>=:sysDate and f.deadline_Date<=:deadlineDate");
			params.put("sysDate", DateUtils.getDateYMD());
			params.put("deadlineDate", deadlineDate);
		}
		params.put("busType", busType);
		params.put("createDate", createDate);
		return this.listBySQLAliasToBean(sql.toString(), params, ChoBankFinanceVO.class);
	}

	public ChoBankFinance getChoBankFinanceByVoucherNo(String voucherNo){
		StringBuilder sql=new StringBuilder("select * from cho_bank_finance b left join bis_finance_exc e on b.id = e.cbs_bill_id");
		sql.append(" where e.id =:voucherNo");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("voucherNo", voucherNo);
		return getBySQLAliasToBean(sql.toString(), params, ChoBankFinance.class);
	}
}
