package com.bytter.bill.domain.billbank.dao;
import com.bytter.bill.domain.billbank.entity.EDraftExcBill;
import com.bytter.bill.domain.billbank.util.StringUtil;
import com.bytter.core.utils.BeanUtils;
import com.bytter.bill.domain.billbank.util.EDraftConstant;
import com.bytter.bill.domain.billbank.vo.EDraftExcBillVo;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 
 * <b>描述：</b>指令查询表（支付信用指令，背面信息指令)查询Dao<br>
 * <b>作者：</b>zhenghj<br>
 * <b>日期：</b> 2018-04-27 <br>
 *
 */
@Repository("eDraftExcBillDao")
public class EDraftExcBillDao extends BaseDao<EDraftExcBill,String> {

	public List<EDraftExcBill> queryAllEDraftExcByParam(String origNo,String payerAcc,String msgType,String sbbllRngStrtSn, String sbbllRngEndSn,String... otherParam) {
		StringBuffer hql = new StringBuffer("FROM EDraftExcBill WHERE 1=1 ");
		Map<String, Object> param = new HashMap<String, Object>();
		if(!StringUtil.isBlank_new(origNo)) {
			hql.append(" AND origNo = :origNo");
			param.put("origNo", origNo);
		}

		if(!StringUtil.isBlank_new(payerAcc)) {
			hql.append(" AND payerAcc = :payerAcc");
			param.put("payerAcc",  payerAcc);
		}

		if(!StringUtil.isBlank_new(msgType)) {
			hql.append(" AND msgType = :msgType");
			param.put("msgType",  msgType);
		}

		if(StringUtils.isNotBlank(sbbllRngStrtSn)){
			hql.append(" AND sbbllRngStrtSn = :sbbllRngStrtSn");
			param.put("sbbllRngStrtSn",  sbbllRngStrtSn);
		}

		if(StringUtils.isNotBlank(sbbllRngEndSn)){
			hql.append(" AND sbbllRngEndSn = :sbbllRngEndSn");
			param.put("sbbllRngEndSn",  sbbllRngEndSn);
		}

		hql.append(" order by operDate desc");

		return this.listByHQL(hql.toString(), param);
	}

	/**  *
	 * 
	 * <b>方法描述：</b>根据票据号+账号 查询状态不等于（-1，19）时指令表数据<br>
	 * <b>作者：</b>zhenghj<br>
	 * <b>日期：</b> 2018-04-18 <br>
	 * @param origNo 票据号
	 * @param payerAcc 持票账号
	 * @param msgType 消息类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<EDraftExcBill> queryEDraftExcByParam(String origNo,String payerAcc,String msgType,String sbbllRngStrtSn, String sbbllRngEndSn) {
		StringBuffer hql = new StringBuffer("FROM EDraftExcBill WHERE status not in (-1,19) and signId is null ");
		Map<String, Object> param = new HashMap<String, Object>();
		if(!StringUtil.isBlank_new(origNo)) {
			hql.append(" AND origNo = :origNo");
			param.put("origNo", origNo);
		}
		
		if(!StringUtil.isBlank_new(payerAcc)) {
			hql.append(" AND payerAcc = :payerAcc");
			param.put("payerAcc",  payerAcc);
		}
		
		if(!StringUtil.isBlank_new(msgType)) {
			hql.append(" AND msgType = :msgType");
			param.put("msgType",  msgType);
		}
		if(StringUtils.isNotBlank(sbbllRngStrtSn)){
			hql.append(" AND sbbllRngStrtSn = :sbbllRngStrtSn");
			param.put("sbbllRngStrtSn",  sbbllRngStrtSn);
		}
		if(StringUtils.isNotBlank(sbbllRngEndSn)){
			hql.append(" AND sbbllRngEndSn = :sbbllRngEndSn");
			param.put("sbbllRngEndSn",  sbbllRngEndSn);
		}
		hql.append(" order by operDate desc");
		return this.listByHQL(hql.toString(), param);
	}
	
	/**
	 * 
	 * <b>方法描述：</b>根据参数删除指定指令表的数据<br>
	 * <b>作者：</b>zhenghj<br>
	 * <b>日期：</b> 2018-04-18 <br>
	 * @param origNo 票据号
	 * @param payerAcc 持票账号
	 * @param msgType 消息类型
	 */
	public void deleteByParam(String origNo,String payerAcc,String msgType) {
		StringBuffer hql = new StringBuffer(" DELETE EDraftExcBill WHERE 1=1 ");
		Map<String, Object> param = new HashMap<String, Object>();
		if(origNo!=null) {
			hql.append(" AND origNo = :origNo");
			param.put("origNo", origNo);
		}
		
		if(payerAcc!=null) {
			hql.append(" AND payerAcc LIKE :payerAcc");
			param.put("payerAcc",  payerAcc);
		}
		
		if(payerAcc!=null) {
			hql.append(" AND msgType = :msgType");
			param.put("msgType",  msgType);
		}
		this.batchExecute(hql.toString(),param);
	}
	
	/**
	 * <b>方法描述：</b>根据消息类型和消息状态查询指令表数据<br>
	 * <b>作者：</b>zhenghj<br>
	 * <b>日期：</b> 2018-04-18 <br>
	 * @param msgType 消息类型
	 * @param status 状态
	 * @param bifCode 银行接口代码
	 */
	@SuppressWarnings("unchecked")
	public List<EDraftExcBill> queryEDraftExcByParam(String msgType,Integer status,String bifCode) {
		StringBuffer hql = new StringBuffer("FROM EDraftExcBill WHERE origNo is not null ");
		Map<String, Object> param = new HashMap<String, Object>();
		if(msgType!=null) {
			hql.append(" AND msgType = :msgType");
			param.put("msgType", msgType);
		}
		
		if(bifCode!=null) {
			hql.append(" AND bifCode in (:bifCode)");
			param.put("bifCode", StringUtil.split(bifCode, ","));
		}
		
		if(status!=null) {
			hql.append(" AND status = :status");
			param.put("status",  status);
		}
		return this.listByHQL(hql.toString(), param);
	}

	public EDraftExcBill queryEDraftExcById(String id) {
		if(id == null){
			return null;
		}
		StringBuffer hql = new StringBuffer("select * FROM AB_DRAFT_EXC WHERE id = :id");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id",id);
		List list = this.listBySQL(hql.toString(), param);
		if(list == null || list.size() == 0){
			return null;
		}
		List<EDraftExcBill> excBills = BeanUtils.copyMapListProperties(list,EDraftExcBill.class);
		return excBills.get(0);
	}
	
//	
//	/**
//	 * <b>功能：</b>根据票据号，账号更新指令表状态<br>
//	 * <b>作者：</b>郑鸿杰<br>
//	 * <b>日期：</b> 2018-04-19 <br>
//	 * @param origNo 票据号
//	 * @param acctId 账号
//	 * @param status 状态
//	 * @param returnCode 返回代码
//	 * @param returnMsg  返回消息
//	 */
//	public void updateAccrecordsStatusByRecordId(String origNo,String acctId,Integer status,String returnCode,String returnMsg) {
//		StringBuffer updatesql =new StringBuffer("update E_DRAFT_EXC t set STATUS=:status");
//		Map<String, Object> param = new HashMap<String, Object>();
//		if(returnCode!=null) {
//			updatesql.append(",RETURN_CODE=:returnCode");
//			param.put("returnCode", returnCode);
//		}
//		if(returnMsg!=null) {
//			updatesql.append(",RETURN_MSG=:returnMsg");
//			param.put("returnMsg", returnMsg);
//		}
//		updatesql.append(" WHERE ORIG_NO =:origNo and PAYERACC=:acctId");
//		
//		param.put("status", status);
//		param.put("origNo", origNo);
//		param.put("acctId", acctId);
//		createSQLQuery(updatesql.toString(),param).executeUpdate();
//	}
	
	
	/**
	 * <b>功能：</b>根据票据号，账号更新指令表状态<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2018-04-19 <br>
	 * @param origNo 票据号
	 * @param acctId 账号
	 * @param status 状态
	 * @param returnCode 返回代码
	 * @param returnMsg  返回消息
	 * @param msgType  指令类型
	 */
	public void updateAccrecordsStatusByRecordId(String origNo,String acctId,Integer status,String returnCode,String returnMsg,String msgType,String sbbllRngStrtSn, String sbbllRngEndSn) {
//		StringBuffer updatesql =new StringBuffer("update E_DRAFT_EXC t set STATUS=:status");
//		Map<String, Object> param = new HashMap<String, Object>();
//		if(!StringUtil.isBlank(returnCode)) {
//			updatesql.append(",RETURN_CODE=:returnCode");
//			param.put("returnCode", returnCode);
//		}
//		if(!StringUtil.isBlank(returnMsg)) {
//			updatesql.append(",RETURN_MSG=:returnMsg");
//			param.put("returnMsg", returnMsg);
//		}
//		updatesql.append(" WHERE ORIG_NO =:origNo ");
//		if(!StringUtil.isBlank(acctId)) {
//			updatesql.append(" and PAYERACC=:acctId");
//			param.put("acctId", acctId);
//		}
//		param.put("status", status);
//		param.put("origNo", origNo);
//		this.updateDelBySql(updatesql.toString(),param);
		updateEDraftExcByIdOrOrigNo(null, origNo, acctId, status, returnCode, returnMsg, msgType,sbbllRngStrtSn,sbbllRngEndSn);
	}
	
	/**
	 * <b>功能：</b>根据票据号，账号更新指令表状态<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2018-04-19 <br>
	 * @param id 指令表ID
	 * @param origNo 票据号
	 * @param acctId 账号
	 * @param status 状态
	 * @param returnCode 返回代码
	 * @param returnMsg  返回消息
	 * @param msgType  指令类型
	 */
	public void updateEDraftExcByIdOrOrigNo(String id,String origNo,String acctId,Integer status,String returnCode,String returnMsg,String msgType,String sbbllRngStrtSn, String sbbllRngEndSn) {
		if((id==null&&StringUtil.isBlank(origNo))
				||(status==null&&StringUtil.isBlank(returnCode))){
			return;
		}
		StringBuffer updatesql =new StringBuffer("update AB_DRAFT_EXC t set ");
		Map<String, Object> param = new HashMap<String, Object>();
		if(status!=null) {
			updatesql.append("STATUS=:status");
			param.put("status", status);
		}
		if(!StringUtil.isBlank(returnCode)) {
			if(status!=null){
				updatesql.append(",");
			}
			updatesql.append("RETURN_CODE=:returnCode");
			param.put("returnCode", returnCode);
		}
		if(!StringUtil.isBlank(returnMsg)) {
			updatesql.append(",RETURN_MSG=:returnMsg");
			param.put("returnMsg", returnMsg);
		}
		updatesql.append(" WHERE 1 = 1 ");
		if(!StringUtil.isBlank(origNo)) {
			updatesql.append(" and ORIG_NO=:origNo");
			param.put("origNo", origNo);
		}
		if(!StringUtil.isBlank(sbbllRngStrtSn)) {
			updatesql.append(" and sbbll_Rng_Strt_Sn=:sbbllRngStrtSn");
			param.put("sbbllRngStrtSn", sbbllRngStrtSn);
		}
		if(!StringUtil.isBlank(sbbllRngEndSn)) {
			updatesql.append(" and sbbll_Rng_End_Sn=:sbbllRngEndSn");
			param.put("sbbllRngEndSn", sbbllRngEndSn);
		}
		if(id!=null) {
			updatesql.append(" and id=:id");
			param.put("id", id);
		}
		if(!StringUtil.isBlank(acctId)) {
			updatesql.append(" and PAYERACC=:acctId");
			param.put("acctId", acctId);
		}
		if(!StringUtil.isBlank(msgType)) {
			updatesql.append(" and MSGTYPE=:msgType");
			param.put("msgType", msgType);
		}
		int size=this.createSQLQuery(updatesql.toString(),param).executeUpdate();
	}
	
	/**
	 * <b>功能：</b>根据指令流水ID修改状态为重新发送<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2021-11-19 <br>
	 * @param id 指令表ID
	 */
	public void updateEDraftExcRepeatById(String id) {
	
		StringBuffer updatesql =new StringBuffer("update AB_DRAFT_EXC t set ");
		Map<String, Object> param = new HashMap<String, Object>();
		updatesql.append("STATUS=:status,RETURN_CODE=:returnCode,RETURN_MSG=:returnMsg,OPER_DATE=:updateDate WHERE id=:id ");
		param.put("updateDate", new Date());
		param.put("status", EDraftConstant.MSG_STATUS_16);
		param.put("returnCode", "");
		param.put("returnMsg", "");
		param.put("id", id);
		int size=this.createSQLQuery(updatesql.toString(),param).executeUpdate();
	}
	
	/**
	 * <b>方法描述：</b>根据消息类型和消息状态查询指令表数据<br>
	 * <b>作者：</b>zhenghj<br>
	 * <b>日期：</b> 2018-04-18 <br>
	 * @param msgType 消息类型
	 * @param status 状态
	 * @param bifCode 银行接口代码
	 */
	@SuppressWarnings("unchecked")
	public List<EDraftExcBillVo> queryEDraftExcVoByParam(String msgType,Integer status,String bifCode) {
		StringBuffer sql = new StringBuffer("select e.SBBLL_RNG_STRT_SN as sbbllRngStrtSn,e.SBBLL_RNG_END_SN as sbbllRngEndSn,e.id,e.payeracc," +
				"e.bif_code as bifCode,e.orig_no as origNo,e.msgtype as msgType,e.status,e.return_code as returnCode,e.return_msg as returnMsg," +
				"e.voucher_id as voucherId,bt_concat2('1008', BT_NVL_CHAR(a.brief_id,bt_concat2('9',r.msgtype))) as briefId, BT_NVL_CHAR(a.bill_id,r.bill_id) as billId from AB_DRAFT_EXC e ");
		sql.append("left join ab_accrecords a on a.id=e.record_id ");
		sql.append("left join ab_bill_receivebiz r on r.id=e.sign_id ");
		sql.append(" where e.orig_no is not null and (e.return_code='3' or e.return_code is null) ");
		Map<String, Object> param = new HashMap<String, Object>();
		if(!StringUtil.isBlank(msgType)) {
			sql.append(" AND e.msgtype = :msgType");
			param.put("msgType", msgType);
		}
		
		if(!StringUtil.isBlank(bifCode)) {
			sql.append(" AND e.bif_Code in (:bifCode) ");
			param.put("bifCode", StringUtil.split(bifCode, ","));
		}
		
		if(status!=null) {
			sql.append(" AND e.status = :status");
			param.put("status",  status);
		}
		sql.append(" order by e.bif_code,e.id ");
		List<EDraftExcBillVo> list = BeanUtils.copyMapListProperties(this.listBySQL(sql.toString(), param), EDraftExcBillVo.class);
		return list;
	}
	
}
