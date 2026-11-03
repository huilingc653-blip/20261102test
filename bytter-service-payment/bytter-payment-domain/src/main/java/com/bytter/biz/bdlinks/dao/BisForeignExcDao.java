package com.bytter.biz.bdlinks.dao;

import com.bytter.biz.bdlinks.entity.BisForeignExc;
import com.bytter.biz.bdlinks.vo.BisForeignExcVO;
import com.bytter.biz.payment.api.constant.BdlinkConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @Title: Dao
* @Description: 外币汇款指令表
* @author onlineGenerator
* @date 2017-07-04 15:57:19
* @version 1.0
*
*/
@Repository
public class BisForeignExcDao extends BaseDao<BisForeignExc,String> {

	/**
	 * 外币汇款指令表通用查@param sqlWhere
	 *
	 * @return
	 */
	private String getCommonSql() {
		StringBuilder sql=new StringBuilder();
		sql.append("select b.id as id,b.voucher_No as voucherNo,b.corp_Id as corpId,b.cbs_Bill_Id as cbsBillId,b.cbs_Bill_No as cbsBillNo,b.remit_Type as remitType,b.bank_Type_Id as bankTypeId,");
		sql.append(" b.req_Date as reqDate,b.payer_Acc as payerAcc,b.payer_Acc_Name as payerAccName,b.payer_Bank as payerBank,b.cur_Id as curId,b.pay_Money as payMoney,");
		sql.append(" b.payee_Acc as payeeAcc,b.payee_Acc_Name as payeeAccName,b.payee_Swift_Code as payeeSwiftCode,b.payee_Bank as payeeBank,b.quick_Sign as quickSign,");
		sql.append(" b.bif_Code as bifCode,t.name as bifName,b.add_Word as addWord,b.cmd_Type as cmdType,b.bank_Serial_No as bankSerialNo,b.voucher_Stat as voucherStat,");
		sql.append(" b.trans_Time as transTime,b.return_Time as returnTime,b.first_Return_Msg as firstReturnMsg,b.next_Return_Msg as nextReturnMsg,b.voucher_From as voucherFrom,");
		sql.append(" b.dif_Bank as difBank,b.fex_Bus_Type as fexBusType,b.create_By as createBy, b.create_Date as createDate, b.update_By as updateBy, b.update_Date as updateDate, ");
		sql.append(" b.payer_Name_En as payerNameEn,b.payer_Addr_En as payerAddrEn,b.payee_Name_En as payeeNameEn,b.payee_Addr_En as payee_Addr_En,b.payee_Bank_Code as payeeBankCode,b.bank_Org_Code as bankOrgCode,");
		sql.append(" b.verify_Code as verifyCode,b.notice_Sign as noticeSign,b.resend_Sign as resendSign,b.file_Name as fileName,b.arrival_Date as arrivalDate,b.declare_No as declareNo,b.acc_nature as accNature, ");
		sql.append(" b.contract_Rate as contractRate,b.actual_Trans_Money as actualTransMoney,b.actual_Trans_Rate as actualTransRate,b.payee_Prov as payeeProv,b.payee_City as payeeCity,");
		sql.append(" b.payer_cur_Id as payerCurId,b.payee_cur_Id as payeeCurId,b.rem_chg_tol_amt_cur as remChgTolAmtCur,b.rem_chg_tol_amt as remChgTolAmt,b.payer_Country_Code as payerCountryCode,");
		sql.append(" b.local_Clear_No as localClearNo, b.exc_Source as excSource,b.ded_acc_cur_id as dedAccCurId ");
		sql.append(" from Bis_Foreign_Exc b left join bis_bif_init t on b.bif_Code = t.id ");
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
		//申请日期
		String gedRunTime = (String) dataTablesInput.getFormdata().get("ged_reqDate");
		String ledRunTime = (String) dataTablesInput.getFormdata().get("led_reqDate");
		if(StringUtils.isNotEmpty(gedRunTime)){
			gedRunTime = gedRunTime + " 00:00:00";
			dataTablesInput.getFormdata().put("ged_reqDate", gedRunTime);
		}
 		if(StringUtils.isNotEmpty(ledRunTime)){
			ledRunTime = ledRunTime + " 23:59:59";
			dataTablesInput.getFormdata().put("led_reqDate", ledRunTime);
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), BisForeignExcVO.class, "b", false);
	}

	/**
	 * 根据ID获取指令
	 * @param id
	 * @return
	 */
	public BisForeignExcVO getByIdUnion(String id){
		StringBuilder sql=new StringBuilder(this.getCommonSql());
		sql.append(" and b.id=:id");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        return getBySQLAliasToBean(sql.toString(), params, BisForeignExcVO.class);
	}

	/**
	 * 根据指定字段查询指令
	 * @param voucherNo
	 * @return
	 */
	public BisForeignExcVO getByVoucherNo(String voucherNo){
		StringBuilder sql=new StringBuilder(this.getCommonSql());
		sql.append(" and b.voucher_No=:voucherNo");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("voucherNo", voucherNo);
		return getBySQLAliasToBean(sql.toString(), params, BisForeignExcVO.class);
	}

	/**
	 * 根据交易状态查询指令集合
	 * @param voucherStat 指令状态
	 * @param joinSql 其他条件
	 * @return
	 */
	public List<BisForeignExcVO> listByVoucherStat(String voucherStat, String joinSql){
		StringBuilder sql=new StringBuilder(this.getCommonSql());
		if(BdlinkConstant.BANK_FAILED.equals(voucherStat) || BdlinkConstant.INTERFACE_FAILED.equals(voucherStat)){
			sql.append(" and b.voucher_Stat in ('"+ BdlinkConstant.BANK_FAILED+"','"+ BdlinkConstant.INTERFACE_FAILED+"')");
		}else{
			sql.append(" and b.voucher_Stat in ('" + voucherStat +"')");
		}
		sql.append(joinSql);
        return listBySQLAliasToBean(sql.toString(), null, BisForeignExcVO.class);
	}

	/**
	 * 指令状态更新（针对指令发送、指令退回、指令重发、指令作废等操作，带状态更新指令）
	 * @param bisForeignExc
	 * @param voucherStat
	 * @return
	 */
	public int updateBisForeignExcVoucherStat(BisForeignExc bisForeignExc, String voucherStat){
		StringBuilder hql=new StringBuilder("update BisForeignExc set voucherStat = :voucherStat");
		Map<String,Object> params = new HashMap<>();
		if(BdlinkConstant.DELETE.equals(voucherStat)) {
			//指令重发
			hql.append(",cbsBillId=:cbsBillId,cbsBillNo=:cbsBillNo");
			if (EmptyUtils.isBlank(bisForeignExc.getCbsBillId())) {
				//业务ID为空，则取指令本身的ID为业务ID
				params.put("cbsBillId", bisForeignExc.getId() + "_D");
				params.put("cbsBillNo", bisForeignExc.getVoucherNo() + "_D");
			} else {
				params.put("cbsBillId", bisForeignExc.getCbsBillId() + "_D");
				params.put("cbsBillNo", bisForeignExc.getCbsBillNo() + "_D");
			}
		}
		if(BdlinkConstant.PRE_SEND.equals(voucherStat)) {
			//指令发送，状态从w-待发送更新为3-准备发送
			hql.append(",reqDate=:reqDate,verifyCode=:verifyCode,signatureCode=:signatureCode");
			//更新指令申请日期
			params.put("reqDate", bisForeignExc.getReqDate());
			//同步更新加密信息
			params.put("verifyCode", bisForeignExc.getVerifyCode());
			//签名
			params.put("signatureCode", bisForeignExc.getSignatureCode());
		}
		hql.append(" where id=:id");
		if(BdlinkConstant.PRE_SEND.equals(voucherStat)){
			//指令发送，状态从w-待发送更新为3-准备发送
			hql.append(" and voucherStat = '"+ BdlinkConstant.WAIT_FILL+"'");
		}
		if(BdlinkConstant.DELETE.equals(voucherStat) || BdlinkConstant.INVALIDATE.equals(voucherStat)) {
			//指令重发/作废，更新状态控制必须是9-接口处理失败或者2-银行处理失败或者Z-验签不通过或者Y-异常交易
			hql.append(" and voucherStat in ('"+ BdlinkConstant.INTERFACE_FAILED+"','"+ BdlinkConstant.BANK_FAILED+"','"+ BdlinkConstant.EXCEPTION+"','"+ BdlinkConstant.DATA_TAMPER+"')");
		}
		if(BdlinkConstant.BACK.equals(voucherStat)){
			//指令退回，更新状态控制必须是6-待发送或者9-接口处理失败或者2-银行处理失败或者Z-验签不通过或者Y-异常交易
			hql.append(" and voucherStat in ('"+ BdlinkConstant.WAIT_FILL+"','"+ BdlinkConstant.INTERFACE_FAILED+"','"+ BdlinkConstant.BANK_FAILED+"','"+ BdlinkConstant.EXCEPTION+"','"+ BdlinkConstant.DATA_TAMPER+"')");
		}
		params.put("id", bisForeignExc.getId());
		params.put("voucherStat", voucherStat);
		return this.batchExecute(hql.toString(), params);
	}

	/**
	 * 指令对应业务单号更新（指令生成时判断原指令状态）
	 * @param bisForeignExc
	 * @return
	 */
	public int updateBisForeignExcCbsBillNo(BisForeignExc bisForeignExc){
		StringBuilder hql=new StringBuilder("update BisForeignExc set cbsBillId=:cbsBillId,cbsBillNo=:cbsBillNo");
		hql.append(" where id=:id and voucherStat in ('"+ BdlinkConstant.BACK+"','"+ BdlinkConstant.INVALIDATE+"')");
		Map<String,Object> params = new HashMap<>();
		params.put("id", bisForeignExc.getId());
		params.put("cbsBillId", bisForeignExc.getCbsBillId() + "_D");
		params.put("cbsBillNo", bisForeignExc.getCbsBillNo() + "_D");
		return this.batchExecute(hql.toString(), params);
	}

	/**
	 * 批量修改提醒标志
	 * @param ids
	 */
	public void updateNoticeSign(List<String> ids){
		StringBuilder sql=new StringBuilder();
		sql.append("update bis_foreign_exc set notice_sign=:noticeSign");
		sql.append(" where").append(SqlUtil.getInSql(ids, "id", true));
		HashMap<String, String> params = new HashMap<>();
		params.put("noticeSign", CommonConstant.SEND_SIGN_YES);
		this.batchSQLExecute(sql.toString(), params);
	}

	/**
	 * 更新银行流水号
	 * @param cbsBillId
	 * @param bankSerialNo
	 * @return
	 */
	public int updateBankSerialNoByCbsBillId(String cbsBillId, String bankSerialNo, String refundFlag){
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sql.append("update bis_foreign_exc set bank_Serial_No = :bankSerialNo");
		String statusWhere = "";
		if (CommonConstant.YES.equals(refundFlag)) {
			// 已勾对的交易，更新为指令无效
			sql.append(",voucher_stat = :voucherStat");
			params.put("voucherStat", BdlinkConstant.INVALIDATE);
			statusWhere = " and voucher_stat = :oldVoucherStat";
			params.put("oldVoucherStat", BdlinkConstant.SUCCESS);
		} else if (CommonConstant.NO.equals(refundFlag)) {
			// 取消勾对的交易，还原状态为指令成功
			sql.append(",voucher_stat = :voucherStat");
			params.put("voucherStat", BdlinkConstant.SUCCESS);
			statusWhere = " and voucher_stat = :oldVoucherStat";
			params.put("oldVoucherStat", BdlinkConstant.INVALIDATE);
		}
		sql.append(" where cbs_bill_id = :cbsBillId").append(statusWhere);
		params.put("cbsBillId", cbsBillId);
		params.put("bankSerialNo", bankSerialNo);
		return this.batchSQLExecute(sql.toString(), params);
	}
	/**
	 * 根据交易状态和交易表id集合过滤交易表id
	 * @param voucherStat 指令状态
	 * @return
	 */
	public List<String> listFexTranReqIdsByStatus(String voucherStat,List<String> tranBillIdList){
		StringBuilder sql=new StringBuilder();
		sql.append("select r.id as id from bis_foreign_exc b  left join fex_tran_req r on r.id = b.cbs_bill_id where b.voucher_stat = :voucherStat");
		if (CollectionUtils.isNotEmpty(tranBillIdList)) {
			sql.append(" and "+getSQLIn(tranBillIdList,900,"r.id"));
		}
		Map<String, Object> params = new HashMap<>();
		params.put("voucherStat", voucherStat);
		Query query = createSQLQuery(sql.toString(), params);
		List<String> list = query.list();
		return list;
	}


 //查询资金收付交易 未完结状态的交易单号
 public List<String> getUnFinishedBillNos(List<String> templateIds) {
     if (EmptyUtils.isEmpty(templateIds)) {
         return new ArrayList<>();
     }
     // 未完成状态集合
     String orderStatus = "('1','2','3','6','9','w','R','p','8','F','S','E','W','T','Y','Z')";
     String inSql = getSQLIn(templateIds, 900, "ftr.template_id");
     StringBuilder sb = new StringBuilder();
     sb.append("SELECT DISTINCT ftr.BILL_NO ")
             .append("FROM BIS_FOREIGN_EXC fta ")
             .append("INNER JOIN fex_cash_flow fcf ON fcf.id = fta.CBS_BILL_ID ")
             .append("INNER JOIN fex_tran_req ftr ON ftr.id = fcf.TRAN_BILL_ID ")
             .append("WHERE fta.VOUCHER_STAT IN ").append(orderStatus)
             .append(" AND ").append(inSql);
     sb.append(" UNION  ");
     sb.append("SELECT DISTINCT ftr.BILL_NO ")
             .append("FROM fex_tran_req ftr ")
             .append("INNER JOIN fex_cash_flow fcf ON fcf.TRAN_BILL_ID = ftr.id ")
             .append("LEFT JOIN BIS_FOREIGN_EXC fta ON fta.CBS_BILL_ID = fcf.id ")
             .append("WHERE fcf.pay_status ='0' and fta.voucher_stat is null")
             .append(" AND ").append(inSql);
     return this.getSession().createSQLQuery(sb.toString()).list();
 }
}
