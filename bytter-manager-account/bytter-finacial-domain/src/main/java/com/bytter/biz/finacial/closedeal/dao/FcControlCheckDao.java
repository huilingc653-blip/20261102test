package com.bytter.biz.finacial.closedeal.dao;
import com.bytter.biz.finacial.closedeal.bean.*;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.OuterFinacialConstant;
import com.bytter.system.api.dto.FcDayCloseDTO;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.core.base.AbstractDao;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 日结检查Dao
 * @author liyh
 *
 */
@Repository
public class FcControlCheckDao extends AbstractDao {
	private Map<String, Object> getCloseParams(FcDayCloseDTO dayClose){
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("netId", dayClose.getNetCode());
        params.put("endDate", DateUtils.addDay(dayClose.getCloseDate(), 1));
		return params;
	}
	
	/**
	 * 功能介绍 : 账户管理 添加初始化未复核。<br>
	 * <b>日期：</b>2017年7月12日<br>
	 * @version 1.0
	 * @author liyh
	 * 由结算提供
	 */
	/*public List<FcAccInitCheckBean> findNoCheckAccInit(FcDayCloseDTO dayClose) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select distinct c.account as account,a.acc_name as accName,");
		sql.append(" c.cumulation as cumulation,c.balance as balance,c.status as status,");
		sql.append(" c.year_balance as yearBalance,c.year_total as yearTotal,su.username as operator");
		sql.append(" from FC_INIT_ACC c  join  fc_acc a on c.account=a.blnctr_acc "); // fc_acc 基础域，acc_name； FC_INIT_ACC 结算域
		sql.append(" left join sys_user su on c.create_by = su.LOGIN_NAME "); //sys_user 基础域
		sql.append(" where c.status in (:status) ");
		sql.append(" and a.net_id = :netId and c.checker_code is null");
		
        Map<String, Object> params = getCloseParams(dayClose);
        params.put("status", new Integer[]{-1, 0, 11});
        return listBySQLAliasToBean(sql.toString(), params, FcAccInitCheckBean.class);
    }*/
	
	/**
	 * 功能介绍 : 存在冻结或解冻扣划未复核的记录。<br>
	 * <b>日期：</b>2017年7月12日<br>
	 * @version 1.0
	 * @author liyh
	 * 表 fc_freeze 被迁移到结算模块
	 */
	/*public List<FcFreezeCheckBean> findNoCheckFreeze(FcDayCloseDTO dayClose) {
		StringBuilder sql = new StringBuilder();
		*//*sql.append(" select a.BLNCTR_ACC as blnctrAcc,b.ACC_NAME as accName,a.FREEZE_SIGN as freezeSign, ")
			.append(" a.FREEZE_TYPE as freezeType,a.BEGIN_DATE as beginDate,a.END_DATE as endDate,")
			.append(" a.FREEZE_MONEY as freezeMoney,a.ACCU_FREEZE_MONEY as accuFreezeMoney, ")
			.append(" a.STATUS as status,c.username as freezeOperator,a.OPERATE_TIME as operateTime ")
			.append(" from fc_freeze a, fc_acc b,sys_user c ")
			.append(" where a.BLNCTR_ACC = b.BLNCTR_ACC ")
			.append(" and a.status>=-1 and a.status<=11 and a.freeze_Sign in ('C','T') ")
			.append(" and a.FREEZE_OPERATOR = c.LOGIN_NAME ")
			.append(" and b.NET_ID = :netId ")
			.append(" and a.OPERATE_TIME < :endDate ");*//*
		sql.append(" select a.BLNCTR_ACC as blnctrAcc,a.FREEZE_SIGN as freezeSign,a.FREEZE_OPERATOR as freezeOperator, ")
				.append(" a.FREEZE_TYPE as freezeType,a.BEGIN_DATE as beginDate,a.END_DATE as endDate,")
				.append(" a.FREEZE_MONEY as freezeMoney,a.ACCU_FREEZE_MONEY as accuFreezeMoney, ")
				.append(" a.STATUS as status,a.OPERATE_TIME as operateTime ")
				.append(" from fc_freeze a ")
				.append(" where a.status>=-1 and a.status<=11 and a.freeze_Sign in ('C','T') ")
				.append(" and a.OPERATE_TIME < :endDate ");
		// sys_user 根据 LOGIN_NAME --> username；fc_acc 根据条件 netId, BLNCTR_ACC --> acc_name
        Map<String, Object> params = getCloseParams(dayClose);
        return listBySQLAliasToBean(sql.toString(), params, FcFreezeCheckBean.class);
    }*/
	
	/**
	 * 功能介绍 : 存在单位申请付款未复核的记录。<br>
	 * <b>日期：</b>2017年7月12日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<NisBillheadCheckBean> findNoCheckNisBillhead(FcDayCloseDTO dayClose) {
        StringBuilder sql=new StringBuilder();
		sql.append("select n.id as id,n.bill_No as billNo,n.corp_Id as corpId,n.voucher_Type_Id as voucherTypeId,f.voucher_Name as voucherName,");
		sql.append(" n.acc_Id as accId,n.payer_Acc as payerAcc,n.payer_Acc_Name as payerAccName,n.payer_Bank as payerBank,n.down_Acc_Id as downAccId,");
		sql.append(" n.cur_Id as curId,n.pay_Money as payMoney,n.payee_Acc as payeeAcc,n.payee_Acc_Name as payeeAccName,n.payee_Bank_Code as payeeBankCode,");
		sql.append(" n.payee_Bank as payeeBank,n.prov as prov,n.city as city,n.wish_Payday as wishPayday,n.is_Quick as isQuick,n.is_Forindividual as isForindividual,");
		sql.append(" n.bif_Code as bifCode,n.dif_Bank as difBank,n.area_Sign as areaSign,n.bursar_Bill_No as bursarBillNo,n.purpose as purpose,n.add_Word as addWord,");
		sql.append(" n.direct_Pay_Flag as directPayFlag,n.ca_Info_Id as caInfoId,n.tally_Date as tallyDate,n.bill_Id as billId,n.erp_Id as erpId,n.bus_Type as busType,t.bus_Type_Name as busTypeName,");
		sql.append(" n.bill_From as billFrom,n.split_Id as splitId,n.merge_Id as mergeId,n.status as status");
		sql.append(" from Nis_Billhead n left join Fc_Voucher_Type f on  n.voucher_Type_Id = f.id");
		sql.append(" left join Nis_Bus_Type t on n.bus_Type = t.id where n.status > 95 and n.status < 99 " );
        Map<String, Object> params = getCloseParams(dayClose);
        return listByHQLAliasToBean(sql.toString(), params, NisBillheadCheckBean.class);
    }
	
	
	/**
	 * 功能介绍 : 存在委托收款未复核的记录。<br>
	 * <b>日期：</b>2017年7月13日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<CgsBillCheckBean> findNoCheckConsignCollection(FcDayCloseDTO dayClose) {
        StringBuffer sql = new StringBuffer("select a.consign_Code as consignCode,a.order_No as orderNo,")
                .append(" a.consign_Date as consignDate,e.corp_name as payeeCorp,a.payee_accounts as payeeAccounts,")
                .append(" a.pact_Code as pactCode,b.cur_name as curName,a.money,b.corp_name as payerCorp,")
                .append(" a.payer_Accounts as payerAccounts,a.resume,a.invoice_Code as invoiceCode,a.charge_Money as chargeMoney,")
                .append(" a.enter_Code as enterCode,a.confirm_Audit_Date as confirmAuditDate,")
                .append(" a.tallier_Code as tallierCode,a.gather_Auditors as gatherAuditors,a.tally_Date as tallyDate,")
                .append(" a.gather_Audit_Date as gatherAuditDate,a.status ");
        StringBuffer sqlfrom = new StringBuffer(" from cgs_bill a, V_fc_acc b,bt_corp c,bt_corp e ")
                .append(" where a.payer_id = c.id and a.payee_id = e.id ")
                .append(" and a.Payee_Accounts = b.BLNCTR_ACC and (a.tally_flag <> 1 or a.finish_flag <> 1)")
                .append(" and a.status > 95 and a.status < 100 ")
                .append(" and b.NET_CODE = :netId and a.consign_Date < :endDate ");
        Map<String, Object> params = getCloseParams(dayClose);
        return listByHQLAliasToBean(sql.append(sqlfrom.toString()).toString(), params, CgsBillCheckBean.class);
	}
	
	
	/**
	 * 功能介绍 : 存在银企接口收款未复核的记录 。<br>
	 * <b>日期：</b>2017年7月13日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<BtNetChalkCheckBean> findNoCheckBankCollectionQuery(FcDayCloseDTO dayClose) {
		String hql = " from BtNetChalk t where t.estate in ('3','4','5') and t.tallyDate < :endDate "
				+ " ";
        Map<String, Object> params = getCloseParams(dayClose);
        return listByHQLAliasToBean(hql, params, BtNetChalkCheckBean.class);
		
	}
	
	/**
	 * 功能介绍 : 账务未复核查询。<br>
	 * <b>日期：</b>2017年7月13日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<FcAccbookCheckBean> queryNoCheckAccBookDtl(FcDayCloseDTO dayClose){
		StringBuilder sql = new StringBuilder();
		sql.append("  select a.bus_ori_type as busOriType,f.race_name as raceName,")
				.append("  a.order_no as orderNo,a.voucher_no as voucherNo,")
				.append("  b.debit_money as debitMoney,b.credit_money as  creditmoney, ")
				.append("  a.tally_date as tallyDate,a.interest_start_date as interestStartDate, ")
				.append("  b.account as account,b.summary_content as rmk,")
				.append("  a.redblue_sign as redblueSign,b.balance as balance, ")
				.append("  a.operator_Code as operatorCode,a.voucher_state as voucherState, ")
				.append("  a.voucher_type as voucherType ")
				.append("  FROM FC_ACCBOOK a ")
				.append("  inner join FC_ACCBOOK_DTL  b on a.ID = b.BOOK_ID ");
		String tableName = "fc_bus_race";
		if (EmptyUtils.isNotEmpty(dayClose.getPoolSign()) && InnerAccountConstant.POOL_SIGN_1.equals(dayClose.getPoolSign())) {
            tableName = "fc_bus_race_pool";
        }
        sql.append("  left join ")
            .append(tableName)
            .append("  f on a.bus_race_id = f.id ")
			.append("  WHERE 1 = 1 ")
			.append("  AND a.tally_date < :endDate ").append(" and a.net_id = :netId ")
			.append("  AND a.voucher_state in (:voucherState) ")
			.append("  AND a.voucher_state <> 'B' ")
			.append("  ORDER BY a.tally_date, a.order_no, b.LINE_NO");

		//TODO fc_accbook 的 net_id 不能为 null
		Map<String, Object> params = getCloseParams(dayClose);
		params.put("voucherState", new String[]{FinaBookConstant.VoucherStatus.STATUS_BACK.get() ,
						FinaBookConstant.VoucherStatus.STATUS_TEMP.get() ,
						FinaBookConstant.VoucherStatus.STATUS_WAIT_CHECK.get() ,
						FinaBookConstant.VoucherStatus.STATUS_INVALID_BACK.get() ,
						FinaBookConstant.VoucherStatus.STATUS_INVALID_TEMP.get() ,
						FinaBookConstant.VoucherStatus.STATUS_INVALID_WAIT_CHECK.get()});
		return listBySQLAliasToBean(sql.toString(), params, FcAccbookCheckBean.class);
	}
	
	/**
	 * 功能介绍 : 账务未复核查询。<br>
	 * <b>日期：</b>2017年7月13日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public List<FcOutAccbookCheckBean> queryNoCheckOutAccBookDtl(FcDayCloseDTO dayClose){
		StringBuilder sql = new StringBuilder();
		/*sql.append("  select a.order_no as orderNo,b.debit_money as debitMoney,b.credit_money as  creditmoney, ")
			.append("  a.tally_date as tallyDate,b.account as account,c.acc_name as accName,")
			.append("  b.summary_content as summaryContent,e.username as operator,a.status as status ")
			.append("  FROM FC_OUT_ACCBOOK      a   ")
			.append("  inner join FC_OUT_ACCBOOK_DTL  b on a.ID = b.out_accbook_id ")
			.append("  inner join FC_ACC          c on b.account = c.blnctr_acc ")
			.append("  inner join sys_user        e on a.operator_Code = e.id ")
			.append("  WHERE 1 = 1 ")
			.append("  AND a.tally_date < :endDate ")
			.append("  AND c.net_id = :netId ")
			.append("  AND a.status in (:voucherState) ")
			.append("  ORDER BY a.tally_date, a.order_no, b.LINE_NO");*/
		sql.append("  select a.order_no as orderNo,b.debit_money as debitMoney,b.credit_money as  creditmoney, ")
				.append("  a.tally_date as tallyDate,b.account as account,a.operator_code as operator,")
				.append("  b.summary_content as summaryContent,a.status as status ")
				.append("  FROM FC_OUT_ACCBOOK      a   ")
				.append("  inner join FC_OUT_ACCBOOK_DTL  b on a.ID = b.out_accbook_id ")
				.append("  WHERE 1 = 1 ")
				.append("  AND a.tally_date < :endDate ")
				.append("  AND a.status in (:voucherState) ")
				.append("  ORDER BY a.tally_date, a.order_no, b.LINE_NO");
		
		Map<String, Object> params = getCloseParams(dayClose);
		params.put("voucherState", new Integer[]{OuterFinacialConstant.OuterStatus.STATUE_TALLY_TEMP.get(),
				OuterFinacialConstant.OuterStatus.STATUE_TALLY_BACK.get() ,
				OuterFinacialConstant.OuterStatus.STATUE_TALLY_WAIT_CHECK.get() ,
				OuterFinacialConstant.OuterStatus.STATUE_INVALID_TEMP.get() ,
				OuterFinacialConstant.OuterStatus.STATUE_INVALID_BACK.get() ,
				OuterFinacialConstant.OuterStatus.STATUE_INVALID_WAIT_CHECK.get() ,
				OuterFinacialConstant.OuterStatus.STATUE_RUSH_TEMP.get() ,
				OuterFinacialConstant.OuterStatus.STATUE_RUSH_BACK.get() ,
				OuterFinacialConstant.OuterStatus.STATUE_RUSH_WAIT_CHECK.get()});
		return listBySQLAliasToBean(sql.toString(), params, FcOutAccbookCheckBean.class);
	}
	
}
