package com.bytter.bill.domain.billbank.dao;
import com.bytter.bill.domain.billbank.entity.BillSign;
import com.bytter.bill.domain.billbank.service.impl.EDraftExcBillService;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.billbank.tool.NewDraftDealMethod;
import com.bytter.bill.domain.billbank.util.BeanUtils;
import com.bytter.bill.domain.billbank.util.EDraftConstant;
import com.bytter.bill.domain.billbank.entity.BillAbVoucherinfo;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtil;
import com.bytter.system.api.ICorpService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
/**
* @Title: Dao
* @Description:  票据签收Dao
* @author onlineGenerator
* @date 2017-06-21 15:36:42
* @version 1.0
* todo 迁移需要改造 dao层反向依赖service层 不合理
*/
@Repository("billSignDao")
public class BillSignDao extends BaseDao<BillSign,String>{
	private static final Logger logger = LoggerFactory.getLogger(BillSignDao.class);
	@Autowired
	private BillVoucherInfoDao billVoucherInfoDao;
	@Autowired
	@Lazy
	private EDraftExcBillService edraftExcBillService;
	@DubboReference(check = false)
	private ICorpService corpServiceImpl;
	/**
	 * 根据msgId查询签收表数据
	 * @param msgId
	 * @return
	 */
	public List<BillSign> queryBillSignListBySerialId(String msgId){
		StringBuilder hql=new StringBuilder("from BillSign b where b.msgid=:msgid ");
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("msgId", msgId);
		return this.listByHQL(hql.toString(), params);
	}
	/**
	 * <b>功能：</b>根据票据号，业务类型查询签收表数据<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2018-4-9 <br>
	 * @param origNo 票据号
	 * @param msgType  业务类型
	 */
	public  List<BillSign> queryBillSignListByOrigNoAndMsgType(String origNo,String msgType){
		String hql = "FROM BillSign a WHERE a.origNo=:origNo and msgtype=:msgType ";
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("origNo", origNo);
		params.put("msgType", msgType);
		return this.listByHQL(hql, params);
	}
	/**
	 * <b>功能：</b>根据票据号，状态查询签收表数据<br>
	 */
	public  List<BillSign> queryBillSignListByOrigNoAndStatus(String origNo,Integer status){
		String hql = "FROM BillSign a WHERE a.origNo=:origNo and status=:status order by a.createDate desc ";
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("origNo", origNo);
		params.put("status", status);
		return this.listByHQL(hql, params);
	}

	/**  *
	 * <b>功能：</b>根据票据号，业务类型  查签收表关联流水表，流水表id 降序 取第一条数据，状态等于19 -3 -4写签收数据<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2018-04-08 <br>
	 * @param origNo 票据号
	 * @param msgType  业务编号
	 * @param recAcctId  签收人账号
	 * @param serialId  是否同一张票的标识号
	 * @return boolean 是否存在
	 */
	public boolean queryBillSignAccrecordsByOrigNoAndMsgType(String origNo,String msgType,String recAcctId,String serialId,String sbbllRngStrtSn, String sbbllRngEndSn){
		boolean isExist = false;
		StringBuffer sqlselect = new StringBuffer();
		sqlselect.append("SELECT A.OPT_STATUS,A.RECID,R.STATUS,A.VALID_SIGN,A.SIGN_UP_FLAG,R.SERIAL_ID,R.UPDATE_DATE ");
		sqlselect.append(" FROM AB_BILL_RECEIVEBIZ R LEFT JOIN  AB_ACCRECORDS A  ON R.ID=A.RECID  ");
		sqlselect.append(" WHERE R.ORIG_NO=:origNo and SBBLL_RNG_STRT_SN = :sbbllRngStrtSn and SBBLL_RNG_END_SN = :sbbllRngEndSn AND R.MSGTYPE=:msgType AND R.REC_ACCT_ID=:recAcctId ");
		sqlselect.append(" ORDER BY R.BUSSDT DESC,R.CREATE_DATE DESC,A.CREATE_DATE DESC  ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("origNo", origNo);
		params.put("msgType", msgType);
		params.put("recAcctId", recAcctId);
		params.put("sbbllRngStrtSn", sbbllRngStrtSn);
		params.put("sbbllRngEndSn", sbbllRngEndSn);
		List<Map<String, Object>> objStr = this.listBySQL(sqlselect.toString(), params);
		if(objStr.size()>0){
			Map<String, Object> obj=objStr.get(0);
			if((String.valueOf(obj.get("OPT_STATUS")).equals("19")
					&& ((!"1".equals(String.valueOf(obj.get("VALID_SIGN"))) &&!"003".equals(msgType)&&!"020".equals(msgType)&&!"021".equals(msgType))
					||"SU01".equals(String.valueOf(obj.get("SIGN_UP_FLAG"))))
			)
					||String.valueOf(obj.get("OPT_STATUS")).equals("-3")
					||String.valueOf(obj.get("STATUS")).equals("-3")
					||String.valueOf(obj.get("STATUS")).equals("-4")
					||String.valueOf(obj.get("OPT_STATUS")).equals("-4")
					||(String.valueOf(obj.get("STATUS")).equals("19")&&String.valueOf(obj.get("OPT_STATUS")).equals("null"))){
				int diffNum =100;
				if(!String.valueOf(obj.get("UPDATE_DATE")).equals("null")){
					Calendar calSrc=Calendar.getInstance();
					calSrc.setTime(DateUtils.str2Date(String.valueOf(obj.get("UPDATE_DATE")),new SimpleDateFormat(DateUtils.DATETIMEFORMAT)));
					diffNum =DateUtils.dateDiff('m',DateUtils.getCalendar(),calSrc);
				}
				//添加是否同一张票的标识号（同意签收或拒签时发给银行，返回成功，银行在之前返回了待签收数据，时间差导致待签收数据重复，半小时内不写入）
				isExist = !StringUtil.isNotEmpty(serialId) || !String.valueOf(obj.get("SERIAL_ID")).equals(serialId) || diffNum >= 30;
			}else{
				isExist=false;
			}
		}else{
			isExist = true;
		}
		return isExist;
	}
	/**用于业务并发缓存处理**/
	private static final Map<String,Boolean> billCache=new ConcurrentHashMap<String,Boolean>();
	public boolean isInCache(String key) {
		if (billCache.containsKey(key)) {
			return true;
		} else {
			billCache.put(key, true);
			return false;
		}
	}
	public void removeFromCache(String key) {
		billCache.remove(key);
	}
	
	/* 插入单条记录,键值由配置文件中键的生成策略决定,返回值为主键 */
	@SuppressWarnings("rawtypes")
	public void save(BillSign billSign) {
		List<String> subRngNoList = NewDraftDealMethod.subTicketIntervalLenLack(billSign.getSbbllRngStrtSn(),billSign.getSbbllRngEndSn());
		billSign.setSbbllRngStrtSn(subRngNoList.get(0));
		billSign.setSbbllRngEndSn(subRngNoList.get(1));
		String key=billSign.getOrigNo()+"_"+(billSign.getSbbllRngStrtSn() == null ? "0" : billSign.getSbbllRngStrtSn())
		+"_"+(billSign.getSbbllRngEndSn() == null ? "0" : billSign.getSbbllRngEndSn())+"_"+billSign.getMsgtype()+"_"+billSign.getRecAcctId();
		if(isInCache(key)){
			logger.info(key+"，写入签收数据存在并发数据处理不执行数据操作");
			return;
		}
		try{
		billSign.setCreateDate(new Date());
		//判断是否写签收数据
		if(this.queryBillSignAccrecordsByOrigNoAndMsgType(billSign.getOrigNo(),billSign.getMsgtype(),billSign.getRecAcctId(),billSign.getSerialId(), billSign.getSbbllRngStrtSn(), billSign.getSbbllRngEndSn())){
			//根据业务接收人的账号写入接收人组织id、接收人组织机构代码、接收人开户行
			List<Map<String,Object>> objectList = queryVAccListVo(billSign.getRecAcctId());
			if (objectList != null && !objectList.isEmpty()) {
				for (Object object : objectList) {
					Map ary = (Map)object;
					if(StringUtil.isEmpty(billSign.getRecNm())){
						billSign.setRecNm(ary.get("ACCNAME")==null?"":ary.get("ACCNAME").toString());
					}
					if(StringUtil.isEmpty(billSign.getRecAcctsvcr())){
						billSign.setRecAcctsvcr(ary.get("BANKCODE") == null  ? "" : ary.get("BANKCODE").toString());
					}
					if(StringUtil.isEmpty(billSign.getRecCorpId())){
						billSign.setRecCorpId(ary.get("CORPID")==null?"":ary.get("CORPID").toString());
					}
					billSign.setRecCmonid(ary.get("ACCOUNTLICENCE") == null ? "" : getCommId(ary.get("ACCOUNTLICENCE").toString()));
					//新票据改造
					billSign.setBlRcvUnnIdNo(ary.get("SOCCODE") == null ? null : ary.get("SOCCODE").toString());
					billSign.setBlRcvIdTpcd(ary.get("ELECTRICBILL") == null ? null : ("0".equals(ary.get("ELECTRICBILL").toString()) ? "DT02" : "DT01"));
				}
			}
//			else{
//				objectList=getCorpIdByAcc(billSign.getRecAcctId());
//				if (objectList != null && !objectList.isEmpty()) {
//					for (Object object : objectList) {
//						Map ary = (Map)object;
//						if(billSign.getRecNm()==null){
//							billSign.setRecNm(ary.get("ACCNAME")==null?"":ary.get("ACCNAME").toString());
//						}
//						if(billSign.getRecAcctsvcr()==null){
//							billSign.setRecAcctsvcr(ary.get("BANKCODE") == null  ? "" : ary.get("BANKCODE").toString());
//						}
//						if(billSign.getRecCorpId()==null){
//							billSign.setRecCorpId(ary.get("CORPID")==null?"":ary.get("CORPID").toString());
//						}
//						billSign.setRecAcctsvcr(ary.get("BANKCODE") == null  ? "" : ary.get("BANKCODE").toString());
//					}
//				}
//			}

//			billSign.setId(UUID.randomUUID().toString().replace("-", ""));
			BillAbVoucherinfo voucherInfo=billVoucherInfoDao.getBillVoucherInfoByOrigNo(billSign.getOrigNo(),billSign.getSbbllRngStrtSn(),billSign.getSbbllRngEndSn());
			if(voucherInfo!=null){
				//复制缺失的出票人承兑人和收票信息到签收表
				initBillAbVoucherinfoToSignInfo(voucherInfo, billSign);
			}
			//初始化缺失的发起人和接收人信息
			if(AccebillConstant.AB_MSGTYPE_ACCEPT.equals(billSign.getMsgtype())){
				initReqRecInfo(billSign, billSign.getPayeracc(), billSign.getPayer(), billSign.getPayerbankno(), billSign.getAcceptoracc(), billSign.getAcceptor(), billSign.getAcceptorbankno());
			}else if(AccebillConstant.AB_MSGTYPE_RECBILL.equals(billSign.getMsgtype())){
				initReqRecInfo(billSign, billSign.getPayeracc(), billSign.getPayer(), billSign.getPayerbankno(),billSign.getPayeeacc(), billSign.getPayee(), billSign.getPayeebankno());
			}
			billSign.setCreateDate(new Date());
			if(("1".equals(billSign.getBlgrdbagcrclPrmtInd()))||("1".equals(billSign.getNewStmIssuofpprInd()) && !"0".equals(billSign.getSbbllRngEndSn()))){
				billSign.setBlgrdbagcrclPrmtInd("1");
			}else {
				billSign.setBlgrdbagcrclPrmtInd("0");
			}
			this.saveOrUpdate(billSign);
			
			if(voucherInfo==null){
				edraftExcBillService.saveEDraftExcBill(null, billSign.getOrigNo(), billSign.getRecAcctId(), EDraftConstant.MSG_TYPE_BILLINFO,billSign.getId(),
						billSign.getSbbllRngStrtSn(), billSign.getSbbllRngEndSn());
				edraftExcBillService.saveEDraftExcBill(null, billSign.getOrigNo(), billSign.getRecAcctId(), EDraftConstant.MSG_TYPE_BACKINFO,billSign.getId(),
						billSign.getSbbllRngStrtSn(), billSign.getSbbllRngEndSn());
			}else{
				edraftExcBillService.saveEDraftExcBill(voucherInfo.getId(), billSign.getOrigNo(), billSign.getRecAcctId(), EDraftConstant.MSG_TYPE_BILLINFO,billSign.getId(),
						billSign.getSbbllRngStrtSn(), billSign.getSbbllRngEndSn());
				edraftExcBillService.saveEDraftExcBill(voucherInfo.getId(), billSign.getOrigNo(), billSign.getRecAcctId(), EDraftConstant.MSG_TYPE_BACKINFO,billSign.getId(),
						billSign.getSbbllRngStrtSn(), billSign.getSbbllRngEndSn());
				//复制缺失的出票人承兑人和收票信息到签收表
				initBillAbVoucherinfoToSignInfo(voucherInfo, billSign);
			}
			this.flush();
		}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("billSign："+ billSign);
			logger.error("写入签收数据异常："+e.getMessage(), e);
			throw new RuntimeException(e);
		}finally{
			removeFromCache(key);
		}
	}
	/**
	 * 初始化缺失的发起人和接收人信息
	 */
	public void initReqRecInfo(BillSign billSign,String reqAcctId,String reqNm,String reqAcctsvcr,String recAcctId,String recNm,String recAcctsvcr){
		if(StringUtil.isEmpty(billSign.getReqAcctId())&&StringUtil.isNotEmpty(reqAcctId)){
			billSign.setReqAcctId(reqAcctId);
		}
		if(StringUtil.isEmpty(billSign.getReqNm())&&StringUtil.isNotEmpty(reqNm)){
			billSign.setReqNm(reqNm);
		}
		if(StringUtil.isEmpty(billSign.getReqAcctsvcr())&&StringUtil.isNotEmpty(reqAcctsvcr)){
			billSign.setReqAcctsvcr(reqAcctsvcr);
		}
		if(StringUtil.isEmpty(billSign.getRecAcctId())&&StringUtil.isNotEmpty(recAcctId)){
			billSign.setRecAcctId(recAcctId);
		}
		if(StringUtil.isEmpty(billSign.getRecNm())&&StringUtil.isNotEmpty(recNm)){
			billSign.setRecNm(recNm);
		}
		if(StringUtil.isEmpty(billSign.getRecAcctsvcr())&&StringUtil.isNotEmpty(recAcctsvcr)){
			billSign.setRecAcctsvcr(recAcctsvcr);
		}
	}
	/**
	 * 根据系统已有的票据信息补齐代签收数据缺少的票据要素
	 * @param voucherInfo
	 * @param billSign
	 */
	public void initBillAbVoucherinfoToSignInfo(BillAbVoucherinfo voucherInfo,BillSign billSign){
		try {
			BeanUtils.copyNotNullProperties(voucherInfo,billSign,"id","createDate","updateDate","createBy","createName","updateBy","updateName");
		} catch (Exception e) {
			throw new RuntimeException("对象属性复制出错");
		}
		/*if(StringUtil.isEmpty(billSign.getPayer())){
			billSign.setPayer(voucherInfo.getPayer());
		}
		if(StringUtil.isEmpty(billSign.getPayeracc())){
			billSign.setPayeracc(voucherInfo.getPayeracc());
		}
		if(StringUtil.isEmpty(billSign.getPayerbankno())){
			billSign.setPayerbankno(voucherInfo.getPayerbankno());
		}
		if(StringUtil.isEmpty(billSign.getPayerbankName())){
			billSign.setPayerbankName(voucherInfo.getPayerBankName());
		}
		if(StringUtil.isEmpty(billSign.getPayee())){
			billSign.setPayee(voucherInfo.getPayee());
		}
		if(StringUtil.isEmpty(billSign.getPayeeacc())){
			billSign.setPayeeacc(voucherInfo.getPayeeacc());
		}
		if(StringUtil.isEmpty(billSign.getPayeebankno())){
			billSign.setPayeebankno(voucherInfo.getPayeebankno());
		}
		if(StringUtil.isEmpty(billSign.getPayeebankName())){
			billSign.setPayeebankName(voucherInfo.getPayeeBankName());
		}
		if(StringUtil.isEmpty(billSign.getAcceptor())){
			billSign.setAcceptor(voucherInfo.getAcceptor());
		}
		if(StringUtil.isEmpty(billSign.getAcceptoracc())){
			billSign.setAcceptoracc(voucherInfo.getAcceptoracc());
		}
		if(StringUtil.isEmpty(billSign.getAcceptorbankno())){
			billSign.setAcceptorbankno(voucherInfo.getAcceptorbankno());
		}
		if(StringUtil.isEmpty(billSign.getAcceptorbankName())){
			billSign.setAcceptorbankName(voucherInfo.getAcceptorBankName());
		}
		//新一代票据改造
		if(StringUtil.isEmpty(billSign.getBlRmtrUnnIdNo())){
			billSign.setBlRmtrUnnIdNo(voucherInfo.getBlRmtrUnnIdNo());
		}
		if(StringUtil.isEmpty(billSign.getBlAcptrIdTpcd())){
			billSign.setBlAcptrIdTpcd(voucherInfo.getBlAcptrIdTpcd());
		}
		if(StringUtil.isEmpty(billSign.getBlRcvpymtpsIdTpcd())){
			billSign.setBlRcvpymtpsIdTpcd(voucherInfo.getBlRcvpymtpsIdTpcd());
		}
		if(StringUtil.isEmpty(billSign.getBlRcvpymtpsIdTpcd())){
			billSign.setBlRcvpymtpsIdTpcd(voucherInfo.getBlRcvpymtpsIdTpcd());
		}
		if(StringUtil.isEmpty(billSign.getBlRcvpymtpsIdTpcd())){
			billSign.setBlRcvpymtpsIdTpcd(voucherInfo.getBlRcvpymtpsIdTpcd());
		}*/
	}
	
	/**  *
	 * <b>功能：</b>根据银行账号获取单位ID与组织机构代码<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-20 <br>
	 * @param bankAcc 银行账号
	 * @return 票据信息
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryVAccListVo(String bankAcc){
		String  sql ="SELECT C.ID AS CORPID, C.ACCOUNT_LICENCE AS ACCOUNTLICENCE, B.BANK_CODE AS BANKCODE,B.ACC_NAME ACCNAME" +
				" ,c.SOC_CODE as socCode, b.ELECTRIC_BILL as electricBill From sys_corp c,bt_bank_acc b where c.id=b.corp_id and b.bank_acc='"+bankAcc+"'";
		List<Map<String,Object>> objStr =this.listBySQL(sql,null);
		return objStr;
	}
	
	/**  *
	 * <b>功能：</b>根据银行账号获取单位ID与组织机构代码<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-8-3 <br>
	 * @param acc 银行账号
	 * @return 票据信息
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getCorpIdByAcc(String acc){
		String  sql ="SELECT C.ID AS CORPID,C.ACCOUNT_LICENCE AS ACCOUNTLICENCE,E.BANK_SOURCECODE AS BANKCODE FROM SYS_EXTERNAL_CORP_BANKACC E,SYS_EXTERNAL_CORP S,sys_corp c WHERE E.EXTERNAL_CORP_ID=S.ID AND S.CORP_ID=C.ID AND e.external_acc='"+acc+"'";
		List<Map<String,Object>> objStr =this.listBySQL(sql,null);
		return objStr;
	}
	/**
	 * 根据单位信息获取组织机构代码
	 * @param accountLicence
	 * @return
	 */
	public String getCommId(String accountLicence){
		String commId=accountLicence;
		if(StringUtil.isNotEmpty(commId)&&commId.length()==18){
			commId=commId.substring(8,16)+"-"+commId.charAt(16);
		}
		if(StringUtil.isEmpty(commId)||commId.length()!=10){
			//throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_COMMIDFALSE);
		}
		return commId;
	}
	/**
	 * <b>功能：</b>根据联行号获取银行名称<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-6-22<br>
	 * @param sysBankCode 联行号
	 * @return 票据信息
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> queryBankNameByPaysyBbankCode(String sysBankCode){
		String  sql ="select t.bank_name,t.ibps_code from BT_INPUT_BANK_INFO t where t.sys_bank_code='"+sysBankCode+"'";
		List<Object[]> objStr = this.listBySQL(sql,null);
		return objStr;
	}
	/**
	 * <b>功能：</b>根据签收ID修改签收状态<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2017-8-10 <br>
	 * @param id 签收ID
	 * @param status 状态
	 */
	public int updateReceivebizStatusById(String id,int status) {
		Map<String, Object> params=new HashMap<String, Object>();
		StringBuffer updatesql =new StringBuffer("update AB_BILL_RECEIVEBIZ t set STATUS=:STATUS WHERE id =:id");
		params.put("STATUS",status);
		params.put("id",id);
		return createSQLQuery(updatesql.toString(),params).executeUpdate();
	}
	/**
	 * 根据签收类型和签收账户进行设置待签收数据为已签收
	 * @param origNo 票据号
	 * @param msgType 签收类型
	 * @param recAccId 签收账户
	 */
	public void updateBillSignIsSignOK(String origNo,String msgType,String recAccId){
		String sql=" update ab_bill_receivebiz set status=:statusNew " +
				"where orig_no=:origNo and msgtype=:msgType and rec_acct_id=:recAccId and status in (:statusOld) ";
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("origNo", origNo);
		params.put("msgType", msgType);
		params.put("recAccId", recAccId);
		params.put("statusOld", new Integer[]{AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_PENDING,AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_REPULSE});
		params.put("statusNew", AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_OK);
		this.createSQLQuery(sql,params).executeUpdate();
	}
	/**
	 * 根据签收表ID设置待签收数据为已撤回
	 * @param signId 签收表ID
	 */
	public void updateBillSignIsSignRecal(String signId){
		String sql=" update ab_bill_receivebiz set status=:statusNew " +
				" where id=:signId and status in (:statusOld) ";
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("signId", signId);
		params.put("statusNew", AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_RECALL);
		params.put("statusOld", new Integer[]{AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_PENDING,AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_REPULSE});
		this.createSQLQuery(sql,params).executeUpdate();
	}
}
