package com.bytter.bill.domain.billbank.dao;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.billbank.entity.BillAbAccrecords;
import com.bytter.bill.domain.billbank.entity.BillAbVoucherinfo;
import com.bytter.bill.domain.billbank.util.EDraftConstant;
import com.bytter.bill.domain.billbank.vo.VoucherInfoVo;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtil;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * <br>
 * <b>功能：</b>票据流水表DAO<br>
 * <b>作者：</b>郑鸿杰<br>
 * <b>日期：</b> 2017-07-5 <br>
 */
@SuppressWarnings("serial")
@Repository("billAccrecordsDao")
public class BillAccrecordsDao extends BaseDao<BillAbAccrecords,String> {
	/**
	 * 根据ID获取流水信息
	 * @param id
	 * @return
	 */
//	@SuppressWarnings("unchecked")
	public BillAbAccrecords getBillAbAccrecordsById(String id){
		if(StringUtil.isEmpty(id)){
			return null;
		}
//		this.flush();
//		Map<String,Object> params=new HashMap<String, Object>();
//		params.put("id", id);
//		List<BillAbAccrecords> list=this.listByHQL("from BillAbAccrecords where id=:id", params);
//		if(list!=null&&list.size()>0){
		BillAbAccrecords info=this.get(id, true);
		if(info!=null){
			this.refresh(info);
		}
		return info;
//		}
//		return null;
	}
	/**
	 * <b>功能：</b>根据票据号查询票据表是否存在数据<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-12-28 <br>
	 * @param VoucherId 票据id
	 * @return briefId 业务状态
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String  getAccrecordsInfoByVoucherIdAndBriefId(String VoucherId,String briefId){
		String recordId=null;
		StringBuffer sqlselect = new StringBuffer();
		sqlselect.append("SELECT  A.ID FROM AB_ACCRECORDS A WHERE A.VID=:vid AND A.BRIEF_ID=:briefId and A.OPT_STATUS not in ('-2','-3','-4') order by create_date desc ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("vid", VoucherId);
		params.put("briefId", EDraftConstant.getT2BriefId(briefId));
		List<Map<String, Object>> objStr = this.listBySQL(sqlselect.toString(), params);
		if (objStr != null && !objStr.isEmpty()) {
			for (Object object : objStr) {
				Map ary = (Map) object;
				recordId=ary.get("ID").toString();
				break;
			}
		}
		return recordId;
	}
	public List<BillAbAccrecords>  getAccrecordsInfoByVoucherIdAndStatus(String VoucherId,String status){
		String recordId=null;
		StringBuffer sqlselect = new StringBuffer();
		sqlselect.append("SELECT  A.* FROM AB_ACCRECORDS A WHERE A.VID=:vid AND A.status=:status order by A.create_date desc ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("vid", VoucherId);
		params.put("status", status);
		List<Map<String, Object>> objStr = this.listBySQL(sqlselect.toString(), params);
		if (objStr != null && !objStr.isEmpty()) {
			return BeanUtils.copyMapListProperties(objStr, BillAbAccrecords.class);
		}
		return null;
	}
	public List<BillAbAccrecords>  getAccrecordsInfoByVoucherIdAndStatus(String VoucherId,String vvid, String status){
		StringBuffer sqlselect = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		sqlselect.append("SELECT  A.* FROM AB_ACCRECORDS A WHERE 1=1 ");
		if(!StringUtil.isEmpty(VoucherId)){
			sqlselect.append(" and A.VID=:vid ");
			params.put("vid", VoucherId);
		}
		if(!StringUtil.isEmpty(vvid)){
			sqlselect.append(" and A.vvid = :vvid ");
			params.put("vvid", vvid);
		}
		if(!StringUtil.isEmpty(status)){
			sqlselect.append(" AND A.status=:status ");
			params.put("status", status);
		}
		sqlselect.append(" order by A.create_date desc ");
		List<Map<String, Object>> objStr = this.listBySQL(sqlselect.toString(), params);
		if (objStr != null && !objStr.isEmpty()) {
			return BeanUtils.copyMapListProperties(objStr, BillAbAccrecords.class);
		}
		return null;
	}
	/**
	 * <b>功能：</b>根据查询参数获取票据信息<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2019-1-21 <br>
	 * @param params 查询参数
	 * @return 票据信息
	 */
	@SuppressWarnings("unchecked")
	public List<VoucherInfoVo> queryVoucherInfoVoByParams(Map<String, Object> params) {
		List<VoucherInfoVo> vcInfoList = new ArrayList<VoucherInfoVo>();
		StringBuffer sqlselect = new StringBuffer();
		sqlselect.append("select z.id       as voucherId,"
				+ "l.id            as recordId,"
				+ "z.orig_no      as origNo,"
				+ "z.SBBLL_RNG_STRT_SN      as sbbllRngStrtSn,"
				+ "z.SBBLL_RNG_END_SN      as sbbllRngEndSn,"
				+ "z.BL_BSN_TPCD      as blBsnTpcd,"
				+ "z.bill_type    as billType,"
				+ "z.Start_Date   as startDate,"
				+ "z.end_date     as endDate,"
				+ "z.bill_money   as billMoney,"
				+ "z.banendrsmtmk as banendrsmtmk,"
				+ "z.payee        as payee,"
				+ "z.payeeacc     as payeeAcc,"
				+ "z.payee_bank_name as payeeBankName,"
				+ "z.payeebankno  as payeeBankNo,"
				+ "z.payer        as payer,"
				+ "z.payeracc     as payerAcc,"
				+ "z.payerbankno  as payerBankNo,"
				+ "z.payer_bank_name as payerBankName,"
				+ "z.acceptor     as acceptor,"
				+ "z.acceptoracc  as acceptorAcc,"
				+ "z.acceptorbankno as acceptorBankNo,"
				+ "z.acceptor_bank_name as acceptorBankName,"
				+ "c.box_status   as boxStatus,"
				+ "z.bill_status  as billStatus,"
				+ "z.bill_flag    as billFlag,"
				+ "z.payer        as openbill,"
				+ "z.payer        as payerName,"
				+ "bt_concat2('1008',l.brief_id) as briefId,"
				+ "l.bif_code      as bifCode,"
				+ "l.return_code   as returnCode,"
				+ "l.return_msg    as returnMsg,"
				+ "l.apply_date    as tallyDate,"
				+ "l.req_nm        as acctName,"
				+ "l.req_acct_id   as acctId,"
				+ "l.req_acctsvcr  as acctAcctsvcr,"
				+ "l.req_acctsvcr_ag as agcyAcctsvcr,"
				+ "l.rec_nm        as backAcctName,"
				+ "l.rec_acct_id   as backacctId,"
				+ "l.rec_acctsvcr  as backacctAcctsvcr,"
				+ "l.opt_status    as status,"
				+ "l.brief_id      as msgtype,"
//                + "l.bankendrsmtmk as transfer,"
				+ "z.bill_money    as money,"
				+ "c.corp_id       as corpId,"
				+ "l.msg_id     as msgId,"
				+ "l.bill_id     as billId ");
		sqlselect.append(" from AB_VOUCHERINFO_VICE c "
				+ " left join AB_VOUCHERINFO z  on z.id = c.vid and (z.OLD_DRAFT_ID is null or z.OLD_DRAFT_ID not like '%-del')  "
				+ " left join AB_ACCRECORDS l on c.id = l.vvid")
				.append(" where  1=1 ");
		StringBuffer sql = new StringBuffer();
		if (params.containsKey("recordId")) {
			sql.append(" and l.id =:recordId ");
		}
		sqlselect.append(sql);
		List<Map<String, Object>> objStr = this.listBySQL(sqlselect.toString(), params);
		vcInfoList = BeanUtils.copyMapListProperties(objStr, VoucherInfoVo.class);
		return vcInfoList;
	}
	/**
	 * 根据票据号获取应收票据目前持有流水信息
	 * @param origNo 票据号
	 */
	public List<BillAbAccrecords> getHoldInfo(String origNo,String sbbllRngStrtSn,String sbbllRngEndSn){
		Map<String,Object> params=new HashMap<String, Object>();
    	StringBuffer hql= new StringBuffer("from BillAbAccrecords a,BillAbVoucherinfo v where a.vid=v.id ");
    	hql.append(" and v.origNo=:origNo  ");
    	if(sbbllRngStrtSn != null){
			hql.append(" and v.sbbllRngStrtSn=:sbbllRngStrtSn  ");
			params.put("sbbllRngStrtSn", sbbllRngStrtSn);
		}
		if(sbbllRngEndSn != null){
			hql.append(" and v.sbbllRngEndSn=:sbbllRngEndSn  ");
			params.put("sbbllRngEndSn", sbbllRngEndSn);
		}

    	hql.append(" and a.status=:status ");
    	hql.append(" and a.optStatus=:optStatus ");
    	hql.append(" and a.validSign=:validSign ");
    	hql.append(" and ((a.briefId in (:briefId) and a.sendSign=:sendSign)  ");
    	hql.append(" or (a.briefId in (:briefId6001) and v.billFlag = :billFlag11 ");
    	hql.append(" and EXISTS( select 1 from BillBtBankAcc ba where ba.bankAcc=a.reqAcctId and ba.electricBill='1' and ba.circulationMode='1' ))) ");
    	hql.append(" order by a.businessDate desc,a.createDate desc ");
    	params.put("origNo", origNo);
    	params.put("sendSign", AccebillConstant.AB_BILL_SEND_SIGN_Y);
    	params.put("status", AccebillConstant.AB_BILL_BUS_STATUS95);
    	params.put("optStatus", AccebillConstant.AB_BILL_OPT_STATUS19);
    	params.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_1);
    	params.put("briefId", new String[]{
    			AccebillConstant.BRIEF_STATUS_9003,
    			AccebillConstant.BRIEF_STATUS_9010,
    			AccebillConstant.BRIEF_STATUS_9012,
    			AccebillConstant.BRIEF_STATUS_9018,
    			AccebillConstant.BRIEF_STATUS_9019});
		params.put("billFlag11",Integer.parseInt(AccebillConstant.AB_BILL_FLAG_ELECT));
		params.put("briefId6001", new String[]{AccebillConstant.BRIEF_STATUS_6001});
		@SuppressWarnings("rawtypes")
		List list=this.listByHQL(hql.toString(), params);
		List<BillAbAccrecords> billAccrecordsList=new ArrayList<BillAbAccrecords>();
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				BillAbAccrecords accInfo=(BillAbAccrecords)((Object[])list.get(i))[0];
				BillAbVoucherinfo vouInfo=(BillAbVoucherinfo)((Object[])list.get(i))[1];
				if(vouInfo!=null&&accInfo!=null){
					accInfo.setBillMoney(vouInfo.getBillMoney());
				}
				billAccrecordsList.add(accInfo);
			}
		}
		return billAccrecordsList ;
	}
	/**
	 * 根据业务id修改票据明细表指令状态
	 * @param busId 业务ID
	 * @param optStatus 指令状态
	 * @param reCode 银行接口返回代码
	 * @param reMsg 银行返回内容
	 * @param validSign 有效标记
	 * @return
	 */
	public int updateAccrecordsByBusId(String busId,String optStatus,String reCode,String reMsg,String validSign){
		return updateAccrecordsByBusId(busId, optStatus, reCode, reMsg, validSign, null);
	}
	/**
	 * 根据业务id修改票据明细表指令状态
	 * @param busId 业务ID
	 * @param billid 外部系统票据ID
	 * @return
	 */
	public int updateAccrecordsBillidByBusId(String busId,String billid){
		return updateAccrecordsByBusId(busId, null, null, null, null, billid);
	}
	/**
	 * 根据业务id修改票据明细表指令状态
	 * @param busId 业务ID
	 * @param optStatus 指令状态
	 * @param reCode 银行接口返回代码
	 * @param reMsg 银行返回内容
	 * @param validSign 有效标记
	 * @param billid 外部系统票据ID
	 * @return
	 */
	public int updateAccrecordsCnAccrecordsByBusId(String busId,String cnAccrecords){
		if(StringUtil.isEmpty(busId) ||StringUtil.isEmpty(cnAccrecords)){
			return 0;
		}
		StringBuffer sql = new StringBuffer("update AB_ACCRECORDS set  ");
		Map<String, Object> params = new HashMap<String, Object>();
		if(StringUtil.isNotEmpty(cnAccrecords)){
			sql.append("cn_Accrecords=:cnAccrecords");
			params.put("cnAccrecords", cnAccrecords);
		}
		sql.append(" where id=:busId ");
		params.put("busId", busId);
		return this.batchSQLExecute(sql.toString(), params);
	}
	/**
	 * 根据业务id修改票据明细表指令状态
	 * @param busId 业务ID
	 * @param optStatus 指令状态
	 * @param reCode 银行接口返回代码
	 * @param reMsg 银行返回内容
	 * @param validSign 有效标记
	 * @param billid 外部系统票据ID
	 * @return
	 */
	public int updateAccrecordsByBusId(String busId,String optStatus,String reCode,String reMsg,String validSign,String billid){
		if(StringUtil.isEmpty(busId)
				||(StringUtil.isEmpty(optStatus)
				&&StringUtil.isEmpty(reCode)
				&&StringUtil.isEmpty(reMsg)
				&&StringUtil.isEmpty(validSign)
				&&StringUtil.isEmpty(billid))){
			return 0;
		}
		StringBuffer sql = new StringBuffer("update AB_ACCRECORDS set  ");
		Map<String, Object> params = new HashMap<String, Object>();
		boolean isSet=false;
		if(StringUtil.isNotEmpty(optStatus)){
			sql.append("opt_status=:optStatus");
			params.put("optStatus", optStatus);
			isSet=true;
		}
		if(StringUtil.isNotEmpty(reCode)){
			if(isSet){
				sql.append(",");
			}else{
				isSet=true;
			}
			sql.append("return_code=:reCode");
			params.put("reCode", reCode);
		}
		if(StringUtil.isNotEmpty(reMsg)){
			if(isSet){
				sql.append(",");
			}else{
				isSet=true;
			}
			sql.append("return_msg=:reMsg");
			params.put("reMsg", reMsg);
		}
		if(StringUtil.isNotEmpty(validSign)){
			if(isSet){
				sql.append(",");
			}else{
				isSet=true;
			}
			sql.append("VALID_SIGN=:validSign");
			params.put("validSign", validSign);
		}
		if(StringUtil.isNotEmpty(billid)){
			if(isSet){
				sql.append(",");
			}else{
				isSet=true;
			}
			sql.append("BILL_ID=:billid");
			params.put("billid", billid);
		}
		sql.append(" where id=:busId ");
		params.put("busId", busId);
		return this.batchSQLExecute(sql.toString(), params);
	}
	/**
	 * 根据查询条件获取票据流水信息
	 * @param params 查询条件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public BillAbAccrecords getBillAbAccrecordsByParams(Map<String,Object> params){
		if (params == null || params.isEmpty()) {
			return null;
		}
		StringBuffer hql = new StringBuffer("from BillAbAccrecords where 1=1 ");
		if (params.containsKey("vid")) {
			hql.append(" and vid =:vid ");
		}
		if (params.containsKey("vvid")) {
			hql.append(" and vvid =:vvid ");
		}
		if (params.containsKey("reqAcc")) {
			hql.append(" and reqAcctId =:reqAcc ");
		}
		if (params.containsKey("validSign")) {
			hql.append(" and validSign =:validSign ");
		}
		if (params.containsKey("status")) {
			hql.append(" and status =:status ");
		}
		if (params.containsKey("briefIds")) {
			hql.append(" and briefId in (:briefIds) ");
		}
		hql.append(" order by createDate desc ");
		List<BillAbAccrecords> list=this.listByHQL(hql.toString(), params);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	/**
	 * 根据流水id修改质押转托管确认状态
	 * @param busId 流水id
	 * @param poolPledgeStatus 质押转托管确认状态:-1质押转托管申请失败、 0质押转托管确认失败、1质押转托管确认在途
	 * @param reMsg 返回消息
	 * @return
	 */
	public int updatePoolPledgeStatusByBusId(String busId,String poolPledgeStatus,String reMsg){
		StringBuffer sql = new StringBuffer("update AB_ACCRECORDS set  ");
		Map<String, Object> params = new HashMap<String, Object>();
		sql.append("pool_Pledge_Status=:poolPledgeStatus");
		params.put("poolPledgeStatus", poolPledgeStatus);
		if(StringUtil.isNotEmpty(reMsg)){
			sql.append(",return_msg=:reMsg");
			params.put("reMsg", reMsg);
		}
		sql.append(" where id=:busId ");
		params.put("busId", busId);
		return this.batchSQLExecute(sql.toString(), params);
	}
}
