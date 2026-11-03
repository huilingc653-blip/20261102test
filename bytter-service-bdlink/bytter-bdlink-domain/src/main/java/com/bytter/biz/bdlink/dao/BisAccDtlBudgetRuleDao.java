package com.bytter.biz.bdlink.dao;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.bdlink.api.dto.BisAccDtlBudgetRuleDTO;
import com.bytter.biz.bdlink.vo.BisAccDtlBudgetRuleVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.BisAccBalConstant;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 银行账号流水明细表
 * @date 2017-07-05 11:20:30
 */
@Repository
public class BisAccDtlBudgetRuleDao extends BaseDao<BisAccDtlBudgetRuleVO, String> {
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, String joinSql) {
		StringBuilder sql = new StringBuilder();
		StringBuilder sqlCdSign0 = new StringBuilder();
		StringBuilder sqlCdSign1 = new StringBuilder();
		String cdSign = (String) dataTablesInput.getFormdata().get("eqs_cdSign");
		sql.append("select distinct id,transTime,bankAcc,accName,bankName,oppAccNo,oppAccName,oppAccBank,curId,curName,amt,remark," );
		sql.append("cdSign,fbsStatus,accNo,settlType,rmk,bankSerialNo,processDateTime,billId,mergeId,splitId,busType,uses,postscript,corpId,bankTypeId ");
		sql.append(" from ( ");
		sqlCdSign0.append(" select dtl.id as id, dtl.trans_time as transTime,");
		sqlCdSign0.append("dtl.bank_acc as bankAcc,dtl.acc_name as accName,dtl.bank_name as bankName,");
		sqlCdSign0.append("dtl.opp_acc_no as oppAccNo,dtl.opp_acc_name as oppAccName,dtl.opp_acc_bank as oppAccBank,");
		sqlCdSign0.append("dtl.cur_id as curId,cur.cur_name as curName,dtl.amt as amt,dtl.remark as remark,dtl.cd_sign as cdSign,");
		sqlCdSign0.append("dtl.fbs_status as fbsStatus, dtl.acc_no as accNo,process.process_date as processDateTime,dtl.settl_type as settlType,dtl.rmk as rmk,dtl.bank_serial_no as bankSerialNo, ");
		sqlCdSign0.append(" dtl.bill_id as billId ,dtl.merge_id as mergeId,dtl.split_id as splitId,dtl.bus_type as busType,dtl.uses,dtl.postscript, bba.corp_id  as corpId ");
		sqlCdSign0.append(" , bba.bank_type_id as bankTypeId");
		sqlCdSign0.append(" from bis_acc_dtl dtl left join bt_bank_acc bba on dtl.bank_acc = bba.bank_acc ");
		sqlCdSign0.append(" left join fbs_budget_process process on process.bill_id = dtl.id ");
		sqlCdSign0.append(" and process.business_Name='"+ BudgetConstant.BUSINESS_NAME_BISACCDTL +"'");
		sqlCdSign0.append(" and process.process_Sysname='"+ BudgetConstant.PROCESS_SYSNAME_NIS +"'");
		sqlCdSign0.append(" left join nis_bus_type t on dtl.bus_type = t.id and t.cd_sign='0' ");
		sqlCdSign0.append(" left join bt_currency cur on dtl.cur_id = cur.id ");
		sqlCdSign0.append(" where dtl.cd_sign = '0' and (dtl.status is null or dtl.status=95)  ");
//		sql.append(" union all ");


		sqlCdSign1.append(" select dtl.id as id,dtl.trans_time as transTime,");
		sqlCdSign1.append("dtl.bank_acc as bankAcc,dtl.acc_name as accName,dtl.bank_name as bankName,");
		sqlCdSign1.append("dtl.opp_acc_no as oppAccNo,dtl.opp_acc_name as oppAccName,dtl.opp_acc_bank as oppAccBank,");
		sqlCdSign1.append("dtl.cur_id as curId,cur.cur_name as curName,dtl.amt as amt,dtl.remark as remark,dtl.cd_sign as cdSign,");
		sqlCdSign1.append("dtl.fbs_status as fbsStatus,  dtl.acc_no as accNo,process.process_date as processDateTime,dtl.settl_type as settlType,dtl.rmk as rmk,dtl.bank_serial_no as bankSerialNo,  ");
		sqlCdSign1.append(" dtl.bill_id as billId ,dtl.merge_id as mergeId,dtl.split_id as splitId,dtl.bus_type as busType,dtl.uses,dtl.postscript, bba.corp_id  as corpId");
		sqlCdSign1.append(" , bba.bank_type_id as bankTypeId");
		sqlCdSign1.append(" from bis_acc_dtl dtl left join bt_bank_acc bba on dtl.bank_acc = bba.bank_acc ");
		sqlCdSign1.append(" left join fbs_budget_process process on process.bill_id = dtl.id ");
		sqlCdSign1.append(" left join bt_currency cur on dtl.cur_id = cur.id ");
		//账户在线流水明细过滤掉(付款办理，资金调拨，费用报销，代发工资)
		sqlCdSign1.append(" where dtl.cd_sign = 1 and (dtl.status is null or dtl.status=95) ");
		sqlCdSign1.append(" and (dtl.bank_serial_no IS NULL  or dtl.bank_serial_no = '' or not exists(SELECT bank_serial_no FROM bis_exc b WHERE b.VOUCHER_STAT = '0' AND dtl.bank_serial_no = b.bank_serial_no) )");
		sqlCdSign1.append(" and (dtl.BLEND_SIGN = '"+ BtBankAccConstant.BLENDSIGN_0+"' or dtl.BLEND_SIGN is null or dtl.BLEND_SIGN='')   ");
		if("0".equals(cdSign)){
			sql.append(sqlCdSign0);
		}else{
			sql.append(sqlCdSign1);
		}
		sql.append(" ) t ");
		sql.append(" where 1=1 ");
		if (StringUtils.isNotEmpty(joinSql)) {
			sql.append(joinSql);
		}
		//交易时间
		String gedTransTime = (String) dataTablesInput.getFormdata().get("ged_transTime");
		String ledTransTime = (String) dataTablesInput.getFormdata().get("led_transTime");
		if(StringUtils.isNotEmpty(gedTransTime)){
			gedTransTime = gedTransTime + " 00:00:00";
			dataTablesInput.getFormdata().put("ged_transTime", gedTransTime);
		}
		if(StringUtils.isNotEmpty(ledTransTime)){
			ledTransTime = ledTransTime + " 23:59:59";
			dataTablesInput.getFormdata().put("led_transTime", ledTransTime);
		}

		//交易金额
		String gedAmt = (String) dataTablesInput.getFormdata().get("othergeg_amt");
		String ledAmt = (String) dataTablesInput.getFormdata().get("otherleg_amt");
		if(StringUtils.isNotEmpty(gedAmt)){
			dataTablesInput.getFormdata().put("geg_amt", gedAmt);
		}
		if(StringUtils.isNotEmpty(ledAmt)){
			dataTablesInput.getFormdata().put("leg_amt", ledAmt);
		}

		String fbsStatus = (String) dataTablesInput.getFormdata().get("eqs_fbsStatus");
		HashMap<String,Object> params = new HashMap<>();
		if(StringUtils.isNotBlank(fbsStatus)){
			if(BisAccBalConstant.FBS_STATUS_N.equals(fbsStatus)){
				sql.append(" and (t.fbsStatus is null or t.fbsStatus = '0' or t.fbsStatus='') ");
			}else{
				sql.append(" and t.fbsStatus =:fbsStatus");
				params.put("fbsStatus",fbsStatus);
			}
		}
		//其他付款流水账号特殊处理
		String bankAcc = (String) dataTablesInput.getFormdata().get("likes_otherBankAcc");
		if(StringUtils.isNotEmpty(bankAcc)){
			dataTablesInput.getFormdata().put("likes_bankAcc",bankAcc);
		}
		Object bankAccList = dataTablesInput.getFormdata().get("bankAccList");
		if(ObjectUtil.isNotEmpty(bankAccList)){
			sql.append("  and  t.bankAcc in (:bankAccList)");
			params.put("bankAccList",bankAccList);
		}

		return getDynamicBySQL(dataTablesInput, sql.toString(), BisAccDtlBudgetRuleDTO.class, "t", false,params);
	}
	/**
	 * 根据id查到流水明细
	 * @param id
	 * @return
	 */
	public BisAccDtlBudgetRuleDTO getBisAccDtlByIdUnion(String id){
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct b.id as id,b.bank_acc as bankAcc,b.acc_name as accName,b.bank_name as bankName,b.bif_code as bifCode,");
		sql.append(" b.opp_acc_no as oppAccNO,b.opp_acc_name as oppAccName,b.opp_acc_bank as oppAccBank,b.cd_sign as cdSign,b.amt as amt,b.bal as bal,");
		sql.append(" b.cur_id as curId,b.remark as remark,b.trans_time as transTime,b.rb_sign as rbSign, b.acc_no as accNo,b.uses as uses,b.postscript as postscript, ");
		sql.append(" b.settl_type as settlType,b.bill_id as billId,b.merge_id as mergeId,b.split_id as splitId,b.bus_type as busType,b.rmk as rmk ");
		sql.append(" from bis_acc_dtl b ");
		sql.append(" where b.id =:id");
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("id",id);
		return getBySQLAliasToBean(sql.toString(),params,BisAccDtlBudgetRuleDTO.class);
	}
	/**
	 * 根据合并id查到流水明细
	 * @param mergeId
	 * @return
	 */
	public List<BisAccDtlBudgetRuleDTO> getBisAccDtlByMergeId(String mergeId){
		StringBuilder sql = new StringBuilder();
		sql.append("select b.id as id,b.bank_acc as bankAcc,b.acc_name as accName,b.bank_name as bankName,b.bif_code as bifCode,");
		sql.append(" b.opp_acc_no as oppAccNO,b.opp_acc_name as oppAccName,b.opp_acc_bank as oppAccBank,b.cd_sign as cdSign,b.amt as amt,b.bal as bal,");
		sql.append(" b.cur_id as curId,b.remark as remark,b.trans_time as transTime,b.rb_sign as rbSign, b.acc_no as accNo,b.uses as uses,b.postscript as postscript, ");
		sql.append(" b.settl_type as settlType,b.bill_id as billId,b.merge_id as mergeId,b.split_id as splitId,b.bus_type as busType ");
		sql.append(" from bis_acc_dtl b ");
		sql.append(" where b.merge_id =:mergeId");
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("mergeId",mergeId);
		return listBySQLAliasToBean(sql.toString(),params,BisAccDtlBudgetRuleDTO.class);
	}
}
