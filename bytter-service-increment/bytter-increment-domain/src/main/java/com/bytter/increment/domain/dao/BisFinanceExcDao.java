package com.bytter.increment.domain.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bytter.core.constant.BdlinkConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.constant.CommonConstant;
import com.bytter.increment.domain.entity.BisFinanceExc;
import com.bytter.increment.domain.vo.BisFinanceExcVO;
import org.springframework.stereotype.Repository;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
/**
 * @Title: Dao
 * @Description: BIS_FINANCE_EXC
 * @author onlineGenerator
 * @date 2017-08-23 11:08:59
 * @version 1.0
 *
 */
@Repository
public class BisFinanceExcDao extends BaseDao<BisFinanceExc,String>{
	/**
	 //TODO数据同步到 bis_finance_exc
	 * 指令表通用查@param sqlWhere
	 * --table bis_bif_init
	 * alter table bis_query_cmd  add  BIF_NAME varchar(30) DEFAULT '';
	 * @return
	 */
	private String getCommonSql() {
		StringBuilder sql=new StringBuilder();
		sql.append("select b.id as id,b.voucher_No as voucherNo,b.cbs_Bill_Id as cbsBillId,b.cbs_Bill_No as cbsBillNo,b.corp_id as corpId,b.req_Date as reqDate,");
		sql.append(" b.bif_Code as bifCode,b.payer_Acc as payerAcc,b.payer_Acc_Name as payerAccName,b.payer_Bank as payerBank,");
		sql.append(" b.cur_Id as curId,b.pay_Money as payMoney,b.purpose as purpose,b.wish_Payday as wishPayday,b.certificate_Way as certificateWay,");
		sql.append(" b.day_Limit as dayLimit,b.date_Unit as dateUnit,b.bursar_Bill_No as bursarBillNo,b.payee_Acc as payeeAcc,b.payee_Acc_Name as payeeAccName,");
		sql.append(" b.bus_Type as busType,b.open_Date as openDate,b.curr_Balance as currBalance,b.time_Balance as timeBalance,b.interest_Rate as interestRate,");
		sql.append(" b.bank_Serial_No as bankSerialNo,b.voucher_Stat as voucherStat,b.trans_Time as transTime,b.return_Time as returnTime,");
		sql.append(" b.first_Return_Msg as firstReturnMsg,b.next_Return_Msg as nextReturnMsg,b.resend_Sign as resendSign,");
		sql.append(" sc.name as corpName,bc.cur_name as curName,bbi.name as bifName ");
		sql.append(" from bis_finance_exc b ");
		sql.append(" left join sys_corp sc on b.corp_id = sc.id ");
		sql.append(" left join bt_currency bc on b.cur_id = bc.id ");
		sql.append(" left join bis_bif_init bbi on b.bif_code = bbi.id ");
		sql.append(" where 1=1 ");
		return sql.toString();
	}
	/**
	 * 指令列表查询
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput,String joinSql) {
		StringBuilder sql=new StringBuilder(this.getCommonSql());
		if(StringUtils.isNotEmpty(joinSql)){
			sql.append(joinSql);
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), BisFinanceExcVO.class, "b", false);
	}
	/**
	 * 根据ID获取指令
	 * @param id
	 * @return
	 */
	public BisFinanceExcVO getByIdUnion(String id){
		StringBuilder sql=new StringBuilder(this.getCommonSql());
		sql.append(" and b.id=:id");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		return getBySQLAliasToBean(sql.toString(), params, BisFinanceExcVO.class);
	}
	
	public BisFinanceExcVO getByVoucherNo(String voucherNo){
		StringBuilder sql=new StringBuilder(this.getCommonSql());
		sql.append(" and b.voucher_No=:voucherNo");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("voucherNo", voucherNo);
		return getBySQLAliasToBean(sql.toString(), params, BisFinanceExcVO.class);
	}
	/**
	 * 获取交易成功/失败的指令（供指令成功/失败提醒）
	 * @param flag true-成功 false-失败
	 * @return
	 */
	public List<BisFinanceExcVO> listByVoucherStat(boolean flag){
		StringBuilder sql=new StringBuilder(this.getCommonSql());
		if(flag){
			sql.append(" and b.voucher_Stat in ('"+BdlinkConstant.SUCCESS+"')");
		}else{
			sql.append(" and b.voucher_Stat in ('"+ BdlinkConstant.BANK_FAILED+"','"+BdlinkConstant.INTERFACE_FAILED+"')");
		}
		//取未生成消息提醒的指令
		sql.append(" and (b.notice_sign <> '1' OR b.notice_sign is null or b.notice_sign='')");
		return listBySQLAliasToBean(sql.toString(), null, BisFinanceExcVO.class);
	}
	/**
	 * 根据单位统计理财指令
	 * @param corpId
	 * @return
	 */
	public int getFinanceExcCountByCorpId(String corpId){
		String sql="select count(*) from bis_finance_exc where corp_id=:corpId";
		HashMap<String,Object> params=new HashMap<>();
		params.put("corpId",corpId);
		//6-待发送、3-准备发送、1-等待结果、2-银行处理失败、9-接口处理失败
		sql += " and voucher_stat in ('1','3','6','2','9')";
		return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
	}
	/**
	 * 指令状态更新（针对指令发送、指令退回、指令重发、指令作废等操作，带状态更新指令）
	 * @param bisFinanceExc
	 * @param voucherStat
	 * @return
	 */
	public int updateBisFinanceExcVoucherStat(BisFinanceExc bisFinanceExc, String voucherStat){
		StringBuilder hql=new StringBuilder("update BisFinanceExc set voucherStat = :voucherStat");
		Map<String,Object> params = new HashMap<>();
		if(BdlinkConstant.DELETE.equals(voucherStat)) {
			//指令重发
			hql.append(",cbsBillId=:cbsBillId,cbsBillNo=:cbsBillNo");
			if (EmptyUtils.isBlank(bisFinanceExc.getCbsBillId())) {
				//业务ID为空，则取指令本身的ID为业务ID
				params.put("cbsBillId", bisFinanceExc.getId() + "_D");
				params.put("cbsBillNo", bisFinanceExc.getVoucherNo() + "_D");
			} else {
				params.put("cbsBillId", bisFinanceExc.getCbsBillId() + "_D");
				params.put("cbsBillNo", bisFinanceExc.getCbsBillNo() + "_D");
			}
		}
		if(BdlinkConstant.PRE_SEND.equals(voucherStat)) {
			//指令发送，状态从w-待发送更新为3-准备发送
			hql.append(",reqDate=:reqDate");
			//更新指令申请日期
			params.put("reqDate", DateUtils.getDate());
		}
		hql.append(" where id=:id");
		if(BdlinkConstant.PRE_SEND.equals(voucherStat)){
			//指令发送，状态从w-待发送更新为3-准备发送
			hql.append(" and voucherStat = '"+BdlinkConstant.WAIT_FILL+"'");
		}
		if(BdlinkConstant.DELETE.equals(voucherStat) || BdlinkConstant.INVALIDATE.equals(voucherStat)) {
			//指令重发/作废，更新状态控制必须是9-接口处理失败或者2-银行处理失败
			hql.append(" and voucherStat in ('"+BdlinkConstant.INTERFACE_FAILED+"','"+BdlinkConstant.BANK_FAILED+"')");
		}
		if(BdlinkConstant.BACK.equals(voucherStat)){
			//指令退回，更新状态控制必须是6-待发送或者9-接口处理失败或者2-银行处理失败
			hql.append(" and voucherStat in ('"+BdlinkConstant.WAIT_FILL+"','"+BdlinkConstant.INTERFACE_FAILED+"','"+BdlinkConstant.BANK_FAILED+"')");
		}
		params.put("id", bisFinanceExc.getId());
		params.put("voucherStat", voucherStat);
		return this.batchExecute(hql.toString(), params);
	}
	/**
	 * 指令对应业务单号更新（指令生成时判断原指令状态）
	 * @param bisFinanceExc
	 * @return
	 */
	public int updateBisFinanceExcCbsBillNo(BisFinanceExc bisFinanceExc){
		StringBuilder hql=new StringBuilder("update BisFinanceExc set cbsBillId=:cbsBillId,cbsBillNo=:cbsBillNo");
		hql.append(" where id=:id and voucherStat in ('"+BdlinkConstant.BACK+"','"+BdlinkConstant.INVALIDATE+"')");
		Map<String,Object> params = new HashMap<>();
		params.put("id", bisFinanceExc.getId());
		params.put("cbsBillId", bisFinanceExc.getCbsBillId() + "_D");
		params.put("cbsBillNo", bisFinanceExc.getCbsBillNo() + "_D");
		return this.batchExecute(hql.toString(), params);
	}
	/**
	 * 批量修改提醒标志
	 * @param ids
	 */
	public void updateNoticeSign(List<String> ids){
		StringBuilder sql=new StringBuilder();
		sql.append("update bis_finance_exc set notice_sign=:noticeSign");
		sql.append(" where").append(SqlUtil.getInSql(ids, "id", true));
		HashMap<String, String> params = new HashMap<>();
		params.put("noticeSign", CommonConstant.SEND_SIGN_YES);
		this.batchSQLExecute(sql.toString(), params);
	}
}
