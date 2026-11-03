package com.bytter.bill.domain.billbank.service.impl;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.billbank.dao.*;
import com.bytter.bill.domain.billbank.entity.*;
import com.bytter.bill.domain.billbank.pool.service.BillPoolManageImpService;
import com.bytter.bill.domain.billbank.service.IEDraftExcBillService;
import com.bytter.bill.domain.billbank.tool.NewDraftDealMethod;
import com.bytter.bill.domain.billbank.tool.TypeConverter;
import com.bytter.bill.domain.billbank.util.BeanUtil;
import com.bytter.bill.domain.billbank.util.EDraftConstant;
import com.bytter.bill.domain.billbank.vo.*;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;
@Service("eDraftExcBillService")
@Transactional
public class EDraftExcBillService  implements IEDraftExcBillService {
	@Autowired
	private BillBankDao billBankDao;
    @Autowired
    private EDraftExcBillDao eDraftExcBillDao;
    @Autowired
    private PaymentCreditDao paymentCreditDao;
    @Autowired
    private BillAbBackbillinfoDao abBackbillinfoDao;
    @Autowired
    private BillVoucherInfoDao billVoucherInfoDao;
	@Autowired
	private BillAccrecordsDao billAccrecordsDao;
	@Autowired
	private BillAbDiscountDao billAbDiscountDao;
	@Autowired
	private BillSignDao billSignDao;
	@Autowired
	private BillAcceptanceRequestService billAcceptanceRequestService;
	@Autowired
	private BillIssuanceRequestService billIssuanceRequestService;
	@Autowired
	private BillEndorseRequestService billEndorseRequestService;
	@Autowired
	private BillCollateralizationRequestService billCollateralizationRequestService;
	@Autowired
	private BillRepurchasedCollateralizationRequestService billRepurchasedCollateralizationRequestService;
	@Autowired
	private BillRepurchaseDiscountRedemptionService billRepurchaseDiscountRedemptionService;
	@Autowired
	private BillPoolManageImpService billPoolManageImpService;
	private Logger logger= LoggerFactory.getLogger(this.getClass());
	
	
	public synchronized void saveEDraftExcBill(String vhoucherId,String origNo,String acctId,String msgType,String signId, String sbbllRngStrtSn, String sbbllRngEndSn){
		saveEDraftExcBill(vhoucherId, null, origNo, acctId, msgType, null,signId,sbbllRngStrtSn,sbbllRngEndSn);
	}
	
	/**  *
	 * 新增查询指令
	 * @param excBill 新增指令信息
	 */
	public synchronized EDraftExcBill saveEDraftExcBill(EDraftExcBill excBill){
		if(excBill != null && excBill.getId() == null){
			eDraftExcBillDao.save(excBill);
		}
		return excBill;
	}


	/**  *
	 * 新增查询指令
	 * @param bifCode 银行接口代码
	 * @param recordId 对应查询流水号
	 * @param msgType 对应查询类型 （支付信用01，背面信息02，正面信息03）
	 */
	public synchronized void saveEDraftExcBill(String vhoucherId,String recordId,String origNo,String acctId,String msgType,String bifCode,String signId,
											   String sbbllRngStrtSn, String sbbllRngEndSn ){
		try{
			if(origNo!=null&&acctId!=null){
				List<EDraftExcBill> excBillListInfo=eDraftExcBillDao.queryEDraftExcByParam(origNo, acctId, msgType,sbbllRngStrtSn, sbbllRngEndSn);
				if(excBillListInfo!=null&&excBillListInfo.size()>0){
					return;
				}
				EDraftExcBill exc=new EDraftExcBill();
				exc.setPayerAcc(acctId);
				exc.setOrigNo(origNo);
				exc.setSbbllRngStrtSn(sbbllRngStrtSn);
				exc.setSbbllRngEndSn(sbbllRngEndSn);
				if(StringUtil.isBlank_new(bifCode)&&!StringUtil.isBlank_new(acctId)){
					BankAcc bankAcc=billBankDao.getBankAccByAcctId(acctId);
					if(bankAcc!=null){
						exc.setBifCode(bankAcc.getBifCode());
					}else{
						return;
					}
				}else{
					exc.setBifCode(bifCode);
				}
				exc.setMsgType(msgType);
				exc.setRecordId(recordId);
				exc.setSignId(signId);
				exc.setVoucherId(vhoucherId);
				exc.setStatus(16);
				exc.setOperDate(new Date());
				eDraftExcBillDao.save(exc);
				//System.out.println("保存查询指令ID："+exc.getId()+"票据号"+origNo+"，账号："+acctId);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 新增查询指令
	 * @param recordId 对应查询流水号
	 * @param msgType 对应查询类型 （支付信用01，背面信息02，正面信息03）
	 */
	public void saveEDraftExcBill(String recordId,String msgType){
		try{
			if(recordId!=null){
				Map<String, Object> params=new HashMap<String, Object>();
				params.put("recordId", recordId);
				List<VoucherInfoVo> billInfoList=billAccrecordsDao.queryVoucherInfoVoByParams(params);
				VoucherInfoVo billInfo=null;
				if(billInfoList!=null&&billInfoList.size()>0){
					billInfo=billInfoList.get(0);
				}else{
					return;
				}
				saveEDraftExcBill(billInfo.getVoucherId(), billInfo.getRecordId(), billInfo.getOrigNo(), billInfo.getAcctId(), msgType,
						billInfo.getBifCode(),null, billInfo.getSbbllRngStrtSn(), billInfo.getSbbllRngEndSn());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
    /**
     *
     * <b>方法描述：</b>支付信用 ，查询待发送指令业务接口：根据消息类型（支付信用类型01）,状态16的指令数据。<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2018-04-19 <br>
     * @param bifCode 银行接口代码
     */
    public List<EDraftExcBillVo> queryEDraftExcBillPaymentCredit(String bifCode) {
//        return eDraftExcBillDao.queryEDraftExcByParam(EDraftConstant.MSG_TYPE_PAYMENTCREDIT, EDraftConstant.MSG_STATUS_16,bifCode);
        List<EDraftExcBill> inforList=eDraftExcBillDao.queryEDraftExcByParam(EDraftConstant.MSG_TYPE_PAYMENTCREDIT, EDraftConstant.MSG_STATUS_16,bifCode);
    	List<EDraftExcBillVo> reList= BeanUtil.copyListProperties(inforList, EDraftExcBillVo.class);
    	return reList;
    }

	public EDraftExcBill queryEDraftExcBillById(String id) {
		if(id == null){
			return null;
		}
		EDraftExcBill excBill = eDraftExcBillDao.queryEDraftExcById(id);
		return excBill;
	}

    /**  *
     * <b>功能：</b>支付信用--成功修改指令状态为19，删除原本支付信用数据，新增支付信用数据<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2018-04-19 <br>
     * @param paymentCredit 支付信用实体
     */
    public void updateEDraftExcSuccessStatusAndAddPaymentCredit(PaymentCreditVo paymentCredit) {
        //根据票据号与账号修改指令表状态
    	eDraftExcBillDao.updateAccrecordsStatusByRecordId(paymentCredit.getBillCode(), paymentCredit.getAcctId(), EDraftConstant.MSG_STATUS_19,paymentCredit.getReturnCode(),paymentCredit.getReturnMsg()
				,EDraftConstant.MSG_TYPE_PAYMENTCREDIT,paymentCredit.getSbbllRngStrtSn(),paymentCredit.getSbbllRngEndSn());
        //根据票据号删除原来的支付信用数据
        paymentCreditDao.deletePaymentCreditListByBillCode(paymentCredit.getBillCode(),paymentCredit.getSbbllRngStrtSn(),paymentCredit.getSbbllRngEndSn());
        //新增支付信用数据
        addPaymentCredit(paymentCredit);
    }
    /**
     *
     * <b>方法描述：</b>背面信息 ，查询待发送指令业务接口：根据消息类型（背面信息类型02）查询状态等于16的指令数据。<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2018-04-20 <br>
     * @param bifCode 银行接口代码
     */
    public List<EDraftExcBillVo> queryEDraftExcBillBackInfo(String bifCode) {
//        return eDraftExcBillDao.queryEDraftExcByParam(EDraftConstant.MSG_TYPE_BACKINFO, EDraftConstant.MSG_STATUS_16,bifCode);
//    	List<EDraftExcBill> inforList=eDraftExcBillDao.queryEDraftExcByParam(EDraftConstant.MSG_TYPE_BACKINFO, EDraftConstant.MSG_STATUS_16,bifCode);
    	List<EDraftExcBillVo> inforList=eDraftExcBillDao.queryEDraftExcVoByParam(EDraftConstant.MSG_TYPE_BACKINFO, EDraftConstant.MSG_STATUS_16,bifCode);
    	for(EDraftExcBillVo vo:inforList){
    		if(!StringUtil.isBlank_new(vo.getOrigNo())){
				if(vo.getOrigNo().startsWith("1") || vo.getOrigNo().startsWith("5") || vo.getOrigNo().startsWith("8")){
					vo.setBillType(EDraftConstant.BILL_TYPE_10);
				}else if(vo.getOrigNo().startsWith("2") || vo.getOrigNo().startsWith("6") || vo.getOrigNo().startsWith("7")){
					vo.setBillType(EDraftConstant.BILL_TYPE_11);
				}
			}
    	}
    	return inforList;
    }
//
//    /**
//     * <b>功能：</b>背面信息--成功修改指令状态为19，删除原本数据，新增背面信息数据<br>
//     * <b>作者：</b>郑鸿杰<br>
//     * <b>日期：</b> 2018-04-20 <br>
//     * @param billAbBackbillInfo 背面信息
//     */
//
//    public void updateEDraftExcSuccessStatusAndAddBackInfo(
//            BillAbBackbillinfo billAbBackbillInfo) {
//        //根据票据号与账号修改指令表状态
//        eDraftExcBillDao.updateAccrecordsStatusByRecordId(billAbBackbillInfo.getOrigNo(), billAbBackbillInfo.getReqAcctId(), EDraftConstant.MSG_STATUS_19,billAbBackbillInfo.getReturnCode(),billAbBackbillInfo.getReturnMsg());
//        //根据票据号删除原来的背面信息数据
//        abBackbillinfoDao.deleteByOrigNo(billAbBackbillInfo.getOrigNo());
//        //新增支付信用数据
//        abBackbillinfoDao.saveOrUpdate(billAbBackbillInfo);
//    }
//    
//	/**
//	  * <b>功能：</b>背面信息--成功修改指令状态为19，删除原本数据，新增背面信息数据<br>
//	  * <b>作者：</b>郑鸿杰<br>
//	  * <b>日期：</b> 2018-04-20 <br>
//	  * @param BillAbBackbillinfoList 背面集合信息 
//	 */
//
//	public void updateEDraftExcSuccessStatusAndAddBackInfo(
//			List<BillAbBackbillinfoVo> billAbBackbillinfoList) {
////		//根据票据号与账号修改指令表状态
////		eDraftExcBillDao.updateAccrecordsStatusByRecordId(billAbBackbillInfo.getOrigNo(), billAbBackbillInfo.getReqAcctId(), EDraftConstant.MSG_STATUS_19,billAbBackbillInfo.getReturnCode(),billAbBackbillInfo.getReturnMsg());
////		//根据票据号删除原来的背面信息数据
////		abBackbillinfoDao.deleteByOrigNo(billAbBackbillInfo.getOrigNo());
////		//新增支付信用数据
////		abBackbillinfoDao.saveOrUpdate(billAbBackbillInfo);
//		
//		Map<String,Boolean> origNo=new HashMap<String,Boolean>();
////		//先根据票据号删除背面信息再保存
//		for(BillAbBackbillinfoVo billAbBackbillinfo:billAbBackbillinfoList){
//			if(!origNo.containsKey(billAbBackbillinfo.getOrigNo())){
//				//根据票据号与账号修改指令表状态
//				eDraftExcBillDao.updateAccrecordsStatusByRecordId(billAbBackbillinfo.getOrigNo(), null, EDraftConstant.MSG_STATUS_19,billAbBackbillinfo.getReturnCode(),billAbBackbillinfo.getReturnMsg(),EDraftConstant.MSG_TYPE_BACKINFO);
//				//根据票据号删除原来的背面信息数据
//				abBackbillinfoDao.deleteByOrigNo(billAbBackbillinfo.getOrigNo());
//				origNo.put(billAbBackbillinfo.getOrigNo(), true);
//			}
//		}
//		List<BillAbBackbillinfo> infoList=BeanUtil.copyListProperties(billAbBackbillinfoList, BillAbBackbillinfo.class);
//		//abBackbillinfoDao.batchSave(billAbBackbillinfoList);
//		abBackbillinfoDao.batchSave(infoList);
//	}
	/**
	  * <b>功能：</b>背面信息--成功修改指令状态为19，删除原本数据，新增背面信息数据<br>
	  * <b>作者：</b>郑鸿杰<br>
	  * <b>日期：</b> 2018-04-20 <br>
	  * @param billAbBackbillinfoList 背面集合信息
	 */
	public void updateEDraftExcSuccessStatusAndAddBackInfo(
			List<BillAbBackbillinfoVo> billAbBackbillinfoList) {
		if (logger.isDebugEnabled()) {
			logger.debug("打印接口过来的票据背面信息="+billAbBackbillinfoList);
		}
		Map<String,List<BillAbBackbillinfo>> origNo=new HashMap<String,List<BillAbBackbillinfo>>();
		List<BillAbBackbillinfo> infoList=new ArrayList<BillAbBackbillinfo>();
		
		Map<String,String> origNoMsgMap =new HashMap<String, String>();
		
		List<BillAbBackbillinfo> origNoBillInfo=null;
//		//先根据票据号删除背面信息再保存
		for(BillAbBackbillinfoVo billAbBackbillinfo:billAbBackbillinfoList){

			List<String> strings = NewDraftDealMethod.subTicketIntervalLenLack(billAbBackbillinfo.getSbbllRngStrtSn(),billAbBackbillinfo.getSbbllRngEndSn());
			billAbBackbillinfo.setSbbllRngStrtSn(strings.get(0));
			billAbBackbillinfo.setSbbllRngEndSn(strings.get(1));

			String origNoMsgKey=billAbBackbillinfo.getOrigNo()+"_"+billAbBackbillinfo.getSbbllRngStrtSn()+"_"+billAbBackbillinfo.getSbbllRngEndSn()+"_"+billAbBackbillinfo.getBustype();
			String origNoAndRngNo = billAbBackbillinfo.getOrigNo()+"&"+(billAbBackbillinfo.getSbbllRngStrtSn() == null ? "0" : billAbBackbillinfo.getSbbllRngStrtSn())
					+"&"+(billAbBackbillinfo.getSbbllRngEndSn() == null ? "0" : billAbBackbillinfo.getSbbllRngEndSn());
			List<BillAbBackbillinfo> backInfoListOld= new ArrayList<BillAbBackbillinfo>();
			if(backInfoListOld!=null&&!backInfoListOld.isEmpty() && origNo.get(origNoAndRngNo)!=null && !origNo.get(billAbBackbillinfo.getOrigNo()).isEmpty()){
				if(origNo.get(origNoAndRngNo).size()<=backInfoListOld.size()){
					continue;
				}
			}
			if(!origNo.containsKey(origNoAndRngNo)){
				origNoBillInfo= new ArrayList<BillAbBackbillinfo>();
				//根据票据号与账号修改指令表状态
				eDraftExcBillDao.updateAccrecordsStatusByRecordId(billAbBackbillinfo.getOrigNo(), null, EDraftConstant.MSG_STATUS_19,billAbBackbillinfo.getReturnCode(),billAbBackbillinfo.getReturnMsg()
						,EDraftConstant.MSG_TYPE_BACKINFO,billAbBackbillinfo.getSbbllRngStrtSn(),billAbBackbillinfo.getSbbllRngEndSn());
				//根据票据号删除原来的背面信息数据
				abBackbillinfoDao.deleteByOrigNoAndRngNo(billAbBackbillinfo.getOrigNo(),billAbBackbillinfo.getSbbllRngStrtSn(),billAbBackbillinfo.getSbbllRngEndSn());
				origNo.put(origNoAndRngNo, origNoBillInfo);
			}
			if(StringUtil.isNotEmpty(billAbBackbillinfo.getBustype())&&!origNoMsgMap.containsKey(origNoMsgKey)){
				BillAbBackbillinfo newInfo=new BillAbBackbillinfo();
				newInfo.setMsdt(new Date());
				BeanUtil.copyProperties(billAbBackbillinfo, newInfo);
				infoList.add(newInfo);
				origNoBillInfo.add(newInfo);
				if(AccebillConstant.AB_BUSTYPE_ACCEPT.equals(billAbBackbillinfo.getBustype())
						||AccebillConstant.AB_BUSTYPE_RECBILL.equals(billAbBackbillinfo.getBustype())
						||AccebillConstant.AB_BUSTYPE_HINT_PAYER.equals(billAbBackbillinfo.getBustype())
						||AccebillConstant.AB_BUSTYPE_HINT_PAYER_OVERDUE.equals(billAbBackbillinfo.getBustype())){
					origNoMsgMap.put(origNoMsgKey, origNoMsgKey);
				}
			}
		}
		//abBackbillinfoDao.batchSave(billAbBackbillinfoList);
//		abBackbillinfoDao.batchSave(infoList);
		
		
		//处理已做出去的票据操作补充做出去的流水信息
		Iterator<String> origNoIterator=origNo.keySet().iterator();
		while(origNoIterator.hasNext()){
			String origNoStr=origNoIterator.next();
			String[] arr = origNoStr.split("&");
			opBillHoldCheck(arr[0], origNo.get(origNoStr),arr[1],arr[2]);
		}
	}
	
	/**
	 * 对目前持有票据流水和背面信息进行比较，自动补充在网银操作的业务
	 * @param origNo
	 * @param backInfoList
	 */
	public void opBillHoldCheck(String origNo,List<BillAbBackbillinfo> backInfoList,String sbbllRngStrtSn, String sbbllRngEndSn){
		if(backInfoList==null){
			List<BillAbBackbillinfo> backInfoListOld=new ArrayList<BillAbBackbillinfo>();
			backInfoList= BeanUtils.copyListProperties(backInfoListOld,BillAbBackbillinfo.class);
			//abBackbillinfoDao.deleteByOrigNo(origNo);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("打印票据背面信息数量="+origNo+"="+backInfoList.size());
		}
		//获取票据主表信息
		BillAbVoucherinfo voucherinfo=null;
		boolean isSeqNum = false;
		boolean isDesc=false;//是否降序标识
		String excUuid = null;
		Integer endorsementCount = 0;
		if(backInfoList!=null&&backInfoList.size()>0){
			for(int i=0;i<backInfoList.size();i++){
				isSeqNum = backInfoList.get(i).getSeqnum() != null && !backInfoList.get(i).getSeqnum().equals("");
			}
			if(isSeqNum && backInfoList.size()>1){
				//System.out.println("执行银行顺序背面信息写入：开始");
				Date backBusDate=null;
				if(AccebillConstant.BRIEF_STATUS_001.equals(backInfoList.get(0).getBustype())
						||AccebillConstant.BRIEF_STATUS_002.equals(backInfoList.get(0).getBustype())
						||AccebillConstant.NEW_DRAFT_BUS_TYPE_ET01.equals(backInfoList.get(0).getBustype())){
					isDesc=false;
				}else if(AccebillConstant.BRIEF_STATUS_001.equals(backInfoList.get(backInfoList.size()-1).getBustype())
						||AccebillConstant.BRIEF_STATUS_002.equals(backInfoList.get(backInfoList.size()-1).getBustype())
						||AccebillConstant.NEW_DRAFT_BUS_TYPE_ET01.equals(backInfoList.get(backInfoList.size()-1).getBustype())){
					isDesc=true;
				}else{
					for(int i=0;i<backInfoList.size();i++){
						BillAbBackbillinfo backInfo=backInfoList.get(i);
						backInfo.setId(null);
						Date busDate= DateUtils.getDateYMD(backInfo.getBussdt());
						if(backBusDate==null){
							backBusDate=busDate;
						}else if(backBusDate.getTime()>busDate.getTime()){
							isDesc=true;
							break;
						}else if(backBusDate.getTime()<busDate.getTime()){
							break;
						}
					}
				}
				if(isDesc && backInfoList.get(0).getSeqnum().compareTo(backInfoList.get(1).getSeqnum())==-1){
					//System.out.println("isDesc执行银行顺序背面信息写入：降序改为升序-1");
					if (logger.isDebugEnabled()) {
						logger.debug("isDesc执行银行顺序背面信息写入：降序改为升序-1");
					}
					Collections.sort(backInfoList, new Comparator<BillAbBackbillinfo>() {
			            public int compare(BillAbBackbillinfo o1, BillAbBackbillinfo o2) {
			                return o2.getSeqnum().compareTo(o1.getSeqnum());
			            }
			        });
				}
				if(isDesc && backInfoList.get(0).getSeqnum().compareTo(backInfoList.get(1).getSeqnum())==1){
					//System.out.println("isDesc执行银行顺序背面信息写入：降序改为升序1");
					if (logger.isDebugEnabled()) {
						logger.debug("isDesc执行银行顺序背面信息写入：降序改为升序1");
					}
					Collections.sort(backInfoList, new Comparator<BillAbBackbillinfo>() {
			            public int compare(BillAbBackbillinfo o1, BillAbBackbillinfo o2) {
			                return o2.getSeqnum().compareTo(o1.getSeqnum());
			            }
			        });
				}
				if(!isDesc && backInfoList.get(0).getSeqnum().compareTo(backInfoList.get(1).getSeqnum())==1){
					//System.out.println("!isDesc执行银行顺序背面信息写入：降序改为升序1");
					if (logger.isDebugEnabled()) {
						logger.debug("!isDesc执行银行顺序背面信息写入：降序改为升序1");
					}
					Collections.sort(backInfoList, new Comparator<BillAbBackbillinfo>() {
			            public int compare(BillAbBackbillinfo o1, BillAbBackbillinfo o2) {
			                return o1.getSeqnum().compareTo(o2.getSeqnum());
			            }
			        });
				}
				
			}else{
				//如果是降序转换成升序
				List<BillAbBackbillinfo> backInfoListNew=new ArrayList<BillAbBackbillinfo>();
				Date backBusDate=null;
				if(AccebillConstant.BRIEF_STATUS_001.equals(backInfoList.get(0).getBustype())
						||AccebillConstant.BRIEF_STATUS_002.equals(backInfoList.get(0).getBustype())
						||AccebillConstant.NEW_DRAFT_BUS_TYPE_ET01.equals(backInfoList.get(0).getBustype())){
					isDesc=false;
				}else if(AccebillConstant.BRIEF_STATUS_001.equals(backInfoList.get(backInfoList.size()-1).getBustype())
						||AccebillConstant.BRIEF_STATUS_002.equals(backInfoList.get(backInfoList.size()-1).getBustype())
						||AccebillConstant.NEW_DRAFT_BUS_TYPE_ET01.equals(backInfoList.get(backInfoList.size()-1).getBustype())){
					isDesc=true;
				}else{
					for(int i=0;i<backInfoList.size();i++){
						BillAbBackbillinfo backInfo=backInfoList.get(i);
						backInfo.setId(null);
						Date busDate=DateUtils.getDateYMD(backInfo.getBussdt());
						if(backBusDate==null){
							backBusDate=busDate;
						}else if(backBusDate.getTime()>busDate.getTime()){
							isDesc=true;
							break;
						}else if(backBusDate.getTime()<busDate.getTime()){
							break;
						}
					}
				}
	
				//对重复数据进行清理1、相同业务类型；2、相同发起方3、相同接收方4、相同推送日期；发起方和接收方必须不相同
				for(int i=0;i<backInfoList.size();i++) {
					BillAbBackbillinfo backInfo = backInfoList.get(i);
					Date busDate=DateUtils.getDateYMD(backInfo.getBussdt());
					int forSize=0;
					//避免存在多个重复数据所以用循环来处理，一旦没有重复数据则跳出循环
					while (true) {
						forSize++;
						if ((i + 1) < backInfoList.size()
								&& !String.valueOf(backInfo.getReqNm()).equals(backInfo.getRecNm())) {
							BillAbBackbillinfo lastBackInfo = backInfoList.get(i + 1);
							if (String.valueOf(backInfo.getBustype()).equals(lastBackInfo.getBustype())
									&& String.valueOf(backInfo.getReqNm()).equals(lastBackInfo.getReqNm())
									&& String.valueOf(backInfo.getRecNm()).equals(lastBackInfo.getRecNm())
									&& busDate.getTime() == DateUtils.getDateYMD(lastBackInfo.getBussdt()).getTime()) {
								backInfoList.remove(i + 1);
							}else{
								break;
							}
						}else{
							break;
						}
						//避免死循环次数大于100跳出循环
						if(forSize>100){
							break;
						}
					}
				}
	
				//识别到降序标识进行升序处理
				if(isDesc){
					int size=backInfoList.size();
					for(int i=size-1;i>=0;i--){
						backInfoList.get(i).setMsdt(new Date());
						backInfoListNew.add(backInfoList.get(i));
					}
					backInfoList=backInfoListNew;
				}
	
				//对集合里面数据按照日期进行升序排序
				Collections.sort(backInfoList);
	
				//重新循环对001、002、003数据进行往第一和第二第三方
	//			backInfoListNew=new ArrayList<BillAbBackbillinfo>();
				List<BillAbBackbillinfo> msg001=new ArrayList<BillAbBackbillinfo>();
				List<BillAbBackbillinfo> msg002=new ArrayList<BillAbBackbillinfo>();
				List<BillAbBackbillinfo> msg003=new ArrayList<BillAbBackbillinfo>();
	
				//获取票据主表信息
					voucherinfo=billVoucherInfoDao.getBillVoucherInfoByOrigNo(origNo,sbbllRngStrtSn,sbbllRngEndSn);
	
				//根据日期进行分升序分map相同日期的方一个集合后期用来内部排序处理
				Map<Long,List<BillAbBackbillinfo>> busDateMap=new LinkedHashMap<Long,List<BillAbBackbillinfo>>();
				for(int i=0;i<backInfoList.size();i++){
					BillAbBackbillinfo backInfo=backInfoList.get(i);
					if(AccebillConstant.BRIEF_STATUS_001.equals(backInfo.getBustype())){
						if (msg001.size() == 0) {
							if(voucherinfo!=null) {
								backInfo.setReqNm(voucherinfo.getPayer());
								backInfo.setReqAcctId(voucherinfo.getPayeracc());
								backInfo.setRecNm(voucherinfo.getPayee());
								backInfo.setRecAcctId(voucherinfo.getPayeeacc());
								backInfo.setBussdt(DateUtils.getDateYMD(voucherinfo.getStartDate()));
							}
							msg001.add(backInfo);
							if ((i + 1) < backInfoList.size()) {
								//处理保证流水
								if (AccebillConstant.BRIEF_STATUS_017.equals(backInfoList.get(i + 1).getBustype())
										&&(backInfo.getReqNm()==null||backInfo.getReqNm().equals(backInfoList.get(i + 1).getReqNm()))) {
									msg001.add(backInfoList.get(i + 1));
									i++;
								}
							}
						}
					}else if(AccebillConstant.BRIEF_STATUS_002.equals(backInfo.getBustype()) || AccebillConstant.NEW_DRAFT_BUS_TYPE_ET01.equals(backInfo.getBustype())){
						if(msg002.size()==0){
							if(voucherinfo!=null) {
								backInfo.setReqNm(voucherinfo.getPayer());
								backInfo.setReqAcctId(voucherinfo.getPayeracc());
								backInfo.setRecNm(voucherinfo.getAcceptor());
								backInfo.setRecAcctId(voucherinfo.getAcceptoracc());
							}
							msg002.add(backInfo);
							//处理保证流水
							if((i+1)<backInfoList.size()){
								if((AccebillConstant.BRIEF_STATUS_017.equals(backInfoList.get(i+1).getBustype()) || AccebillConstant.NEW_DRAFT_BUS_TYPE_ET02.equals(backInfoList.get(i+1).getBustype()))
										&&backInfo.getReqNm().equals(backInfoList.get(i + 1).getReqNm())){
									msg002.add(backInfoList.get(i+1));
									i++;
								}
							}
						}
					}else if(AccebillConstant.BRIEF_STATUS_003.equals(backInfo.getBustype())||AccebillConstant.NEW_DRAFT_BUS_TYPE_ET18.equals(backInfo.getBustype())){
						if(msg003.size()==0) {
							if(voucherinfo!=null) {
								backInfo.setReqNm(voucherinfo.getPayer());
								backInfo.setReqAcctId(voucherinfo.getPayeracc());
								backInfo.setRecNm(voucherinfo.getPayee());
								backInfo.setRecAcctId(voucherinfo.getPayeeacc());
							}
							msg003.add(backInfo);
						}
					}else{
						if(!busDateMap.containsKey(backInfo.getBussdt().getTime())){
							busDateMap.put(backInfo.getBussdt().getTime(),new ArrayList<BillAbBackbillinfo>());
						}
						busDateMap.get(backInfo.getBussdt().getTime()).add(backInfo);
	//					backInfoListNew.add(backInfo);
					}
				}
	
				Date startPayeeDate= null;
				Date endPayeeDate= null;
				if(voucherinfo!=null){
					startPayeeDate=DateUtils.getDateYMD(voucherinfo.getStartDate());
					endPayeeDate=DateUtils.getDateYMD(voucherinfo.getEndDate());
				}
				if(!busDateMap.isEmpty()){
					List<BillAbBackbillinfo> listBack=(List<BillAbBackbillinfo>) busDateMap.values().toArray()[0];
					endPayeeDate=listBack.get(0).getBussdt();
				}
	
				backInfoListNew=new ArrayList<BillAbBackbillinfo>();
				if(msg001.size()>0){
					backInfoListNew.addAll(msg001);
				}
				if(msg002.size()>0){
					for(BillAbBackbillinfo info:msg002){
						if(voucherinfo!=null && info.getBussdt().getTime()>endPayeeDate.getTime()){
							info.setBussdt(startPayeeDate);
						}else{
							startPayeeDate=DateUtils.getDateYMD(info.getBussdt());
						}
					}
					backInfoListNew.addAll(msg002);
				}
				if(msg003.size()>0){
					for(BillAbBackbillinfo info:msg003){
						if(voucherinfo!=null && info.getBussdt().getTime()>endPayeeDate.getTime()){
							info.setBussdt(startPayeeDate);
						}
					}
					backInfoListNew.addAll(msg003);
				}
				if(!busDateMap.isEmpty()){
					//用于设置单日重复数据的起始人员
					String sendNm=null;
					if(voucherinfo!=null){
						sendNm=voucherinfo.getPayee();
					}
					for(List<BillAbBackbillinfo> listBack:busDateMap.values()){
						if(listBack!=null){
							if(listBack.size()>1){
								if(StringUtil.isNotEmpty(sendNm)) {
									List<BillAbBackbillinfo> backInfoNew = new ArrayList<BillAbBackbillinfo>();
									Map<String, List<BillAbBackbillinfo>> backMapReq = new LinkedHashMap<String, List<BillAbBackbillinfo>>();//发起方的map集合
									Map<String, List<BillAbBackbillinfo>> backMapReqGua = new LinkedHashMap<String, List<BillAbBackbillinfo>>();//发起方保证的map集合
									//多个日期相同数据进行排序处理
									for (BillAbBackbillinfo backInfo : listBack) {
										if(AccebillConstant.BRIEF_STATUS_017.equals(listBack.get(0).getBustype()) || AccebillConstant.NEW_DRAFT_BUS_TYPE_ET02.equals(listBack.get(0).getBustype())) {
											if (!backMapReqGua.containsKey(backInfo.getReqNm())) {
												backMapReqGua.put(backInfo.getReqNm(), new ArrayList<BillAbBackbillinfo>());
											}
											backMapReqGua.get(backInfo.getReqNm()).add(backInfo);
										}else{
											if (!backMapReq.containsKey(backInfo.getReqNm())) {
												backMapReq.put(backInfo.getReqNm(), new ArrayList<BillAbBackbillinfo>());
											}
											backMapReq.get(backInfo.getReqNm()).add(backInfo);
										}
									}
									boolean isOld = false;
									int forSize=0;
									while (true) {
										forSize++;
										if(forSize>backMapReq.size()){
											break;
										}
										//先把保证的背面信息写入
										if(backMapReqGua.containsKey(sendNm)){
											backInfoNew.addAll(backMapReqGua.get(sendNm));
										}
										if (backMapReq.containsKey(sendNm)) {
											List<BillAbBackbillinfo> sendBackList = backMapReq.get(sendNm);
											if (sendBackList.size() == 1) {
												backInfoNew.addAll(sendBackList);
												sendNm = sendBackList.get(0).getRecNm();
											}else{
												List<BillAbBackbillinfo> reqInfoNew = new ArrayList<BillAbBackbillinfo>();
												for(BillAbBackbillinfo info:sendBackList){
													if(info.getReqNm().equals(info.getRecNm())){
														backInfoNew.add(info);
													}else{
														reqInfoNew.add(info);
														sendNm = info.getRecNm();
													}
												}
												if(reqInfoNew.size()>1){
													isOld=true;
													break;
												}else{
													backInfoNew.addAll(reqInfoNew);
												}
											}
										} else {
											break;
										}
										//避免死循环次数大于100跳出循环
										if(forSize>100){
											break;
										}
									}
									if (isOld || backInfoListNew.size() == 0) {
										sendNm = null;
										backInfoListNew.addAll(listBack);
									} else {
										backInfoListNew.addAll(backInfoNew);
									}
								}else{
									backInfoListNew.addAll(listBack);
								}
							}else{
								backInfoListNew.addAll(listBack);
								if(!AccebillConstant.BRIEF_STATUS_017.equals(listBack.get(0).getBustype())
										 && !AccebillConstant.BRIEF_STATUS_018.equals(listBack.get(0).getBustype())
										&& !AccebillConstant.BRIEF_STATUS_019.equals(listBack.get(0).getBustype())
										&& !AccebillConstant.NEW_DRAFT_BUS_TYPE_ET02.equals(listBack.get(0).getBustype())
										&& !AccebillConstant.NEW_DRAFT_BUS_TYPE_ET03.equals(listBack.get(0).getBustype())
										&& !AccebillConstant.NEW_DRAFT_BUS_TYPE_ET04.equals(listBack.get(0).getBustype())){
									sendNm=listBack.get(0).getRecNm();
								}
							}
						}
					}
				}
				backInfoList=backInfoListNew;
			}
			String uuid=UUID.randomUUID().toString().replace("-","").substring(0,29);
			for(int i=0;i<backInfoList.size();i++){
				BillAbBackbillinfo backInfo=backInfoList.get(i);
				Date msdt=DateUtils.getDateYMD(backInfo.getMsdt());
				backInfo.setMsdt(backInfo.getMsdt());
//				String indexStr=("000"+i);
//				backInfo.setId(uuid+indexStr.substring(indexStr.length()-3,indexStr.length()));
				backInfo.setId(null);
				if(AccebillConstant.BRIEF_STATUS_010.equals(backInfo.getBustype())
						||AccebillConstant.NEW_DRAFT_BUS_TYPE_ET05.equals(backInfo.getBustype())){
					endorsementCount++;
				}
				if(StringUtil.isNotEmpty(backInfo.getUuid())&&StringUtil.isBlank_new(excUuid)){
					excUuid = backInfo.getUuid();
				}
			}
			abBackbillinfoDao.batchSave(backInfoList);
			//根据票据号和子票区间修改正面信息的背书次数
			billVoucherInfoDao.updateVoucherinfoEndorsementCount(origNo,sbbllRngStrtSn,sbbllRngEndSn,endorsementCount);
		}
		//System.out.println("backInfoList保存是："+backInfoList);
		if (logger.isDebugEnabled()) {
			logger.debug("backInfoList保存是："+backInfoList);
		}
		if(backInfoList!=null&&backInfoList.size()>0&&StringUtil.isNotEmpty(origNo)){
			
			
			if(voucherinfo==null){
				//获取票据主表信息
				voucherinfo=billVoucherInfoDao.getBillVoucherInfoByOrigNo(origNo,sbbllRngStrtSn,sbbllRngEndSn);
			}
			List<BillAbAccrecords> holdInfoList=billAccrecordsDao.getHoldInfo(origNo,sbbllRngStrtSn,sbbllRngEndSn);
			for(BillAbAccrecords holdInfo:holdInfoList){
				//1、先找到票据号目前最新持有的票据流水信息
//				BillAbAccrecords holdInfo=billAccrecordsDao.getHoldInfo(origNo);
				if(holdInfo!=null&&StringUtil.isNotEmpty(holdInfo.getBriefId())){
	//				String bustype=holdInfo.getBriefId().substring(1);
					String bustype=null;
					if(holdInfo.getBriefId().startsWith("9")){
						bustype=holdInfo.getBriefId().substring(1);
					}else if(AccebillConstant.BRIEF_STATUS_6001.equals(holdInfo.getBriefId())){
						if(AccebillConstant.AB_BILL_SOURCE_REC.toString().equals(holdInfo.getBillSource())
								&&StringUtil.isNotEmpty(holdInfo.getRecNm())
								&&StringUtil.isNotEmpty(voucherinfo.getPayer())
								&&StringUtil.isNotEmpty(voucherinfo.getPayee())){
							if(holdInfo.getReqNm().equals(voucherinfo.getPayee())
									&&holdInfo.getRecNm().equals(voucherinfo.getPayer())){
								bustype=AccebillConstant.AB_MSGTYPE_RECBILL;
							}else{
								bustype=AccebillConstant.AB_MSGTYPE_RECITE;
							}
						}else if(AccebillConstant.AB_BILL_SOURCE_ENDORSE.toString().equals(holdInfo.getBillSource())){
							bustype=AccebillConstant.AB_MSGTYPE_RECITE;
						}else if(AccebillConstant.AB_BILL_SOURCE_PLEDGE.toString().equals(holdInfo.getBillSource())){
							bustype=AccebillConstant.AB_MSGTYPE_PLEDGE;
						}else{
							continue;
						}
					}else{
						continue;
					}
					int size=backInfoList.size();
					Date holdBusDate=DateUtils.getDateYMD(holdInfo.getBusinessDate());
	
					boolean isUpdateHold=false;
					//判断当前持有业务信息和背面信息最后一条数据是否是同一个是则进行设置交易对手信息设置
					if(StringUtil.isEmpty(holdInfo.getRecAcctId())
							||StringUtil.isEmpty(holdInfo.getRecNm())){
						BillAbBackbillinfo backInfo=backInfoList.get(backInfoList.size()-1);
						isUpdateHold=setHoldBIllBackInfo(holdInfo, backInfo, bustype, holdBusDate);
					}
					
					if(AccebillConstant.BRIEF_STATUS_003.equals(bustype)){
						for(int i=0;i<size;i++){
							if((AccebillConstant.BRIEF_STATUS_010.equals(backInfoList.get(i).getBustype())
									||AccebillConstant.BRIEF_STATUS_011.equals(backInfoList.get(i).getBustype())
									||AccebillConstant.NEW_DRAFT_BUS_TYPE_ET13.equals(backInfoList.get(i).getBustype())
									||AccebillConstant.BRIEF_STATUS_018.equals(backInfoList.get(i).getBustype())
									||AccebillConstant.NEW_DRAFT_BUS_TYPE_ET03.equals(backInfoList.get(i).getBustype())
									||AccebillConstant.NEW_DRAFT_BUS_TYPE_ET05.equals(backInfoList.get(i).getBustype())
									||AccebillConstant.NEW_DRAFT_BUS_TYPE_ET09.equals(backInfoList.get(i).getBustype())
									||((AccebillConstant.BRIEF_STATUS_020.equals(backInfoList.get(i).getBustype())
											||AccebillConstant.BRIEF_STATUS_021.equals(backInfoList.get(i).getBustype()))
											&&StringUtil.isNotEmpty(backInfoList.get(i).getSgnupmk())
											&&!backInfoList.get(i).getSgnupmk().equals("拒付")))
									&&String.valueOf(backInfoList.get(i).getReqNm()).equals(holdInfo.getReqNm())){
								//System.out.println("判断003进去持有写入");
								saveAbAccrecordsByBillAbBackbillinfo(holdInfo, backInfoList.get(i));
								isUpdateHold=false;
								break;
							}else{
								continue;
							}
						}
					}else{
						if(isSeqNum && backInfoList.size()>1){
							//System.out.println("流水：开始");
							if(isDesc && backInfoList.get(0).getSeqnum().compareTo(backInfoList.get(1).getSeqnum())==-1){
								//System.out.println("流水执行银行顺序背面信息写入：降序改为升序-1");
								Collections.sort(backInfoList, new Comparator<BillAbBackbillinfo>() {
						            public int compare(BillAbBackbillinfo o1, BillAbBackbillinfo o2) {
						                return o2.getSeqnum().compareTo(o1.getSeqnum());
						            }
						        });
							}
							if(isDesc && backInfoList.get(0).getSeqnum().compareTo(backInfoList.get(1).getSeqnum())==1){
								//System.out.println("流水执行银行顺序背面信息写入：降序改为升序1");
								Collections.sort(backInfoList, new Comparator<BillAbBackbillinfo>() {
						            public int compare(BillAbBackbillinfo o1, BillAbBackbillinfo o2) {
						                return o2.getSeqnum().compareTo(o1.getSeqnum());
						            }
						        });
							}
							if(!isDesc && backInfoList.get(0).getSeqnum().compareTo(backInfoList.get(1).getSeqnum())==1){
								//System.out.println("!流水执行银行顺序背面信息写入：降序改为升序1");
								Collections.sort(backInfoList, new Comparator<BillAbBackbillinfo>() {
						            public int compare(BillAbBackbillinfo o1, BillAbBackbillinfo o2) {
						                return o1.getSeqnum().compareTo(o2.getSeqnum());
						            }
						        });
							}
							
						}
						for(int i=size-1;i>=0;i--){
							BillAbBackbillinfo backInfo=backInfoList.get(i);
							if(StringUtil.isEmpty(backInfo.getBustype())){
								continue;
							}
							Date backBusDate=DateUtils.getDateYMD(backInfo.getBussdt());
							if(backBusDate.getTime()>holdBusDate.getTime()){
								continue;
							}
	
							if((backInfo.getBustype().equals(AccebillConstant.BRIEF_STATUS_020)
								||backInfo.getBustype().equals(AccebillConstant.BRIEF_STATUS_021)
									||backInfo.getBustype().equals(AccebillConstant.NEW_DRAFT_BUS_TYPE_ET09))
									&&(StringUtil.isEmpty(backInfo.getSgnupmk())
										||backInfo.getSgnupmk().equals("拒付"))){
								continue;
							}
							if(backInfo.getBustype().equals(bustype)
									&&String.valueOf(holdInfo.getReqNm()).equals(backInfo.getRecNm())
									&&(StringUtil.isBlank_new(holdInfo.getRecNm())||String.valueOf(holdInfo.getRecNm()).equals(backInfo.getReqNm()))){
								if(String.valueOf(backInfo.getRecNm()).equals(backInfo.getReqNm())&&(i+1)==size){
									if(i==0){
										backInfo=backInfoList.get(i);
									}else{
										backInfo=backInfoList.get(i-1);
									}
									if(backInfo!=null
											&&backInfo.getBustype().equals(bustype)
											&&DateUtils.getDateYMD(backInfo.getBussdt()).getTime()<=holdBusDate.getTime()
											&&String.valueOf(holdInfo.getReqNm()).equals(backInfo.getRecNm())
											&&!String.valueOf(holdInfo.getReqNm()).equals(holdInfo.getRecNm())){
										saveAbAccrecordsByBillAbBackbillinfo(holdInfo, backInfoList.get(i));
										isUpdateHold=false;
									}
								}else if((i+1)!=size){
									setHoldBIllBackInfo(holdInfo, backInfo, bustype, holdBusDate);
									saveAbAccrecordsByBillAbBackbillinfo(holdInfo, backInfoList.get(i+1));
									isUpdateHold=false;
								}
								break;
							}
						}
						//添加收票登记进来的线下电票，同步背面信息时已经背书出去补充流水
						for(int i=size-1;i>=0;i--){
							BillAbBackbillinfo backInfo=backInfoList.get(i);
							if(StringUtil.isEmpty(backInfo.getBustype())){
								continue;
							}
							Date backBusDate=DateUtils.getDateYMD(backInfo.getBussdt());
							if(backBusDate.getTime()<holdBusDate.getTime()){
								continue;
							}
							if(backInfo.getBustype().equals(bustype) && "6001".equals(holdInfo.getBriefId())
									&&String.valueOf(holdInfo.getReqNm()).equals(backInfo.getReqNm()) && (i+1)==size){
								saveAbAccrecordsByBillAbBackbillinfo(holdInfo, backInfoList.get(i));
								isUpdateHold=false;
								break;
							}
						}
					}
					if(isUpdateHold){
						billAccrecordsDao.update(holdInfo);
					}
				}
			}
		}
	}
	
	/**
	 * 判断当前持有业务信息和背面信息最后一条数据是否是同一个是则进行设置交易对手信息设置
	 * @param holdInfo
	 * @param backInfo
	 * @param bustype
	 * @param holdBusDate
	 * @return
	 */
	public boolean setHoldBIllBackInfo(BillAbAccrecords holdInfo,BillAbBackbillinfo backInfo,String bustype,Date holdBusDate){
		boolean isUpdateHold=false;
		Date backBusDate=DateUtils.getDateYMD(backInfo.getBussdt());
		String bustypeNew=AccebillConstant.BUS_TYPE_OLD_TO_NEW.get(bustype);
		if(bustype.equals(backInfo.getBustype())||bustypeNew.equals(backInfo.getBustype())//系统业务类型
				&&String.valueOf(holdInfo.getReqNm()).equals(backInfo.getRecNm())//持票账户名称和背面新接收人名称相同
				&&backBusDate.getTime()<=holdBusDate.getTime()//持有日期大于等于背面信息交易日期
				){
			if(StringUtil.isEmpty(holdInfo.getRecAcctId())&&StringUtil.isNotEmpty(backInfo.getReqAcctId())){
				holdInfo.setRecAcctId(backInfo.getReqAcctId());
				isUpdateHold=true;
			}
			if(StringUtil.isEmpty(holdInfo.getRecNm())&&StringUtil.isNotEmpty(backInfo.getReqNm())){
				holdInfo.setRecNm(backInfo.getReqNm());
				isUpdateHold=true;
			}
		}
		return isUpdateHold;
	}
	
	/**
	 * 通过背面新自动填充网银已做业务数据
	 * @param holdInfo
	 * @param backInfo
	 */
	public void saveAbAccrecordsByBillAbBackbillinfo(BillAbAccrecords holdInfo,BillAbBackbillinfo backInfo){
		if((backInfo.getBustype().equals(AccebillConstant.BRIEF_STATUS_020)
				||backInfo.getBustype().equals(AccebillConstant.BRIEF_STATUS_021)
				||backInfo.getBustype().equals(AccebillConstant.NEW_DRAFT_BUS_TYPE_ET09))
				&&(StringUtil.isEmpty(backInfo.getSgnupmk())
				||backInfo.getSgnupmk().equals("拒付"))){
			return;
		}
		Date holdBusDate=DateUtils.getDateYMD(holdInfo.getBusinessDate());
		Date backBusDate=DateUtils.getDateYMD(backInfo.getBussdt());
		if(holdBusDate.getTime()>backBusDate.getTime()
				||AccebillConstant.BRIEF_STATUS_002.equals(backInfo.getBustype())
				||AccebillConstant.NEW_DRAFT_BUS_TYPE_ET01.equals(backInfo.getBustype())
				||AccebillConstant.BRIEF_STATUS_003.equals(backInfo.getBustype())){
			return;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("自己写入流水1:holdBusDate="+holdBusDate.getTime());
		}
		holdInfo.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_2);
		billAccrecordsDao.update(holdInfo);
		BillAbAccrecords accrecords=new BillAbAccrecords();
//		accrecords.setVid(holdInfo.getVid());
//		accrecords.setVvid(holdInfo.getVvid());
//		accrecords.setStatus(AccebillConstant.AB_BILL_BUS_STATUS95);
//		accrecords.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
//		accrecords.setOptStatus(AccebillConstant.AB_BILL_OPT_STATUS19);
//		accrecords.setSendSign(AccebillConstant.AB_BILL_SEND_SIGN_Y);
//		accrecords.setEnterStockOld(AccebillConstant.AB_ENTER_STOCK_CORP);
		BeanUtil.copyProperties(holdInfo, accrecords);
		accrecords.setId(null);
		accrecords.setRecid(null);
		accrecords.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
		accrecords.setBriefId(backInfo.getBustype());
		if(backInfo.getBustype().startsWith("ET")){
			accrecords.setBriefId(AccebillConstant.BUS_TYPE_NEW_TO_OLD.get(backInfo.getBustype()));
		}
		accrecords.setLastid(holdInfo.getId());
		accrecords.setApplyDate(backInfo.getBussdt());
		accrecords.setBusinessDate(backInfo.getBussdt());
		accrecords.setRecAcctId(backInfo.getRecAcctId());
		accrecords.setRecAcctsvcr(backInfo.getRecAcctsvcr());
		accrecords.setRecCmonid(backInfo.getRecCmonid());
		accrecords.setRecRole(backInfo.getRecRole());
		accrecords.setRecNm(backInfo.getRecNm());
		accrecords.setRmk(backInfo.getRmrkbysgnr());
		accrecords.setBankendrsmtmk(backInfo.getBankendrsmtmk());
		accrecords.setSttlmmk(backInfo.getSttlmmk());
//		accrecords.setSignUpFlag(backInfo.getSgnupmk());
		accrecords.setSignUpFlag(AccebillConstant.AB_SGNUPMK_Y);
		accrecords.setCreateBy(null);
		accrecords.setCreateDate(new Date());
		accrecords.setCreateName(null);
		accrecords.setUpdateBy(null);
		accrecords.setUpdateDate(null);
		accrecords.setUpdateName(null);
		accrecords.setReturnCode(null);
		accrecords.setReturnMsg(null);
		if(StringUtil.isEmpty(accrecords.getSttlmmk())){
			accrecords.setSttlmmk(AccebillConstant.AB_STTLMMK_N);
		}
		billAccrecordsDao.save(accrecords);
		if(AccebillConstant.BRIEF_STATUS_011.equals(accrecords.getBriefId())){
			BillAbDiscount discount=new BillAbDiscount();
			discount.setRecordId(accrecords.getId());
			discount.setDiscountDate(backInfo.getBussdt());
			discount.setRpdmk(backInfo.getRpdmk());
			discount.setBuyerinterestRatio(new BigDecimal(1));
			discount.setBuyerinterestMoney(new BigDecimal(0));
			discount.setPaydate(0);
			discount.setInterestRates(backInfo.getIntrstrate());
			discount.setAoaccninfId(backInfo.getAoaccninfId());
			discount.setAoaccninfAc(backInfo.getAoaccninfAc());
			discount.setInvcNb(backInfo.getInvcnb());
			discount.setTxlCtrctNb(backInfo.getTxlctrctnb());
			discount.setRpdopendt(backInfo.getRpdopendt());
			discount.setRpdduedt(backInfo.getRpdduedt());
			discount.setRealMoney(backInfo.getAmt());
			if(discount.getRealMoney()==null){
				discount.setRealMoney(holdInfo.getBillMoney());
			}
			discount.setInterest(holdInfo.getBillMoney().subtract(discount.getRealMoney()));
			billAbDiscountDao.save(discount);
		}
	}
    /**
     * <b>功能：</b>失败修改状态为-1<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2018-04-19 <br>
     * @param origNo 票据号
     * @param acctId  账号
     * @param returnCode 返回代码
     * @param returnMsg  返回消息
     */
    public void updateEDraftExcFailStatusByOrigNoAndAcctId(String origNo,
                                                           String acctId,String returnCode,String returnMsg,String sbbllRngStrtSn, String sbbllRngEndSn) {
//        eDraftExcBillDao.updateAccrecordsStatusByRecordId(origNo, acctId, EDraftConstant.MSG_STATUS_1,returnCode,returnMsg);
    	eDraftExcBillDao.updateAccrecordsStatusByRecordId(origNo, acctId, EDraftConstant.MSG_STATUS_1,returnCode,returnMsg,null,sbbllRngStrtSn,sbbllRngEndSn);
    }
    /**
     * <b>功能：</b>新增支付信用数据<br>
     * <b>作者：</b>郑鸿杰<br>
     * <b>日期：</b> 2018-04-19 <br>
     * @param paymentCredit  支付信用
     */
    public void addPaymentCredit(PaymentCreditVo paymentCredit){
    	PaymentCredit info=new PaymentCredit();
    	BeanUtil.copyProperties(paymentCredit, info);
    	info.setEnterDate(new Date());
        paymentCreditDao.save(info);
    }
	/**
	 * <b>功能：</b>根据指令流水ID修改返利代码和返回原因<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2018-09-04 <br>
	 * @param id 指令表ID
	 * @param returnCode 返回代码
	 * @param returnMsg  返回消息
	 */
	public void updateEDraftExcById(String id, String returnCode, String returnMsg) {
		Integer status=null;
		if("2".equals(returnCode)){
			status=EDraftConstant.MSG_STATUS_1;
		}else if("0".equals(returnCode)){
			status=EDraftConstant.MSG_STATUS_19;
		}
		eDraftExcBillDao.updateEDraftExcByIdOrOrigNo(id, null, null, status, returnCode, returnMsg,null,null,null);
	}
	
	/**
	 * <b>功能：</b>根据指令流水ID修改状态为重新发送<br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2021-11-19 <br>
	 * @param id 指令表ID
	 */
	public void updateEDraftExcRepeatById(String id) {
		if(StringUtil.isBlank(id)){
			return;
		}
		eDraftExcBillDao.updateEDraftExcRepeatById(id);
	}
	
	/**
	 * 
	 * <b>方法描述：</b>正面信息 ，查询待发送指令业务接口：根据消息类型（背面信息类型03）查询状态等于16的指令数据。<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2018-07-11 <br>
	 * @param bifCode 银行接口代码
	 */
	public List<EDraftExcBillVo> queryEDraftExcBillInfo(String bifCode) {
//		List<EDraftExcBill> inforList=eDraftExcBillDao.queryEDraftExcByParam(EDraftConstant.MSG_TYPE_BILLINFO, EDraftConstant.MSG_STATUS_16,bifCode);
//		List<EDraftExcBillVo> reList=BeanUtil.copyListProperties(inforList, EDraftExcBillVo.class);
		List<EDraftExcBillVo> reList=eDraftExcBillDao.queryEDraftExcVoByParam(EDraftConstant.MSG_TYPE_BILLINFO, EDraftConstant.MSG_STATUS_16,bifCode);
		for(EDraftExcBillVo vo:reList){
    		if(!StringUtil.isBlank_new(vo.getOrigNo())){
				if(vo.getOrigNo().startsWith("1") || vo.getOrigNo().startsWith("5") || vo.getOrigNo().startsWith("8")){
					vo.setBillType(EDraftConstant.BILL_TYPE_10);
				}else if(vo.getOrigNo().startsWith("2") || vo.getOrigNo().startsWith("6") || vo.getOrigNo().startsWith("7")){
					vo.setBillType(EDraftConstant.BILL_TYPE_11);
				}
			}
    	}
		return reList;
	}
	/**
	  * <b>功能：</b>正面信息--成功修改指令状态为19，修改票据正面信息数据<br>
	  * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2018-07-11 <br>
	  * @param voucherInfo 正面集合信息 
	 */
	public void updateEDraftExcSuccessStatusAndAddBillInfo(VoucherInfoVo voucherInfo){
		if(voucherInfo == null){
			return;
		}
		if(logger.isDebugEnabled()){
			logger.debug("updateEDraftExcSuccessStatusAndAddBillInfo======"+voucherInfo.toString());
		}
		List<String> subRngNoList = NewDraftDealMethod.subTicketIntervalLenLack(voucherInfo.getSbbllRngStrtSn(),voucherInfo.getSbbllRngEndSn());
		voucherInfo.setSbbllRngStrtSn(subRngNoList.get(0));
		voucherInfo.setSbbllRngEndSn(subRngNoList.get(1));
		EDraftExcBill draftExc=null;
		if(StringUtil.isNotEmpty(voucherInfo.getUuid())){
			draftExc=eDraftExcBillDao.get(voucherInfo.getUuid());
		}
		boolean isUpdate = true;
		BillAbVoucherinfo oldVoucherInfo=null;
		if(draftExc!=null && StringUtil.isNotEmpty(draftExc.getVoucherId())){
			oldVoucherInfo = billVoucherInfoDao.getBillAbVoucherinfoById(draftExc.getVoucherId());
			if(!voucherInfo.getSbbllRngStrtSn().equals(oldVoucherInfo.getSbbllRngStrtSn())
					||!voucherInfo.getSbbllRngEndSn().equals(oldVoucherInfo.getSbbllRngEndSn())){
				eDraftExcBillDao.updateEDraftExcByIdOrOrigNo(voucherInfo.getUuid(), voucherInfo.getOrigNo(), null, EDraftConstant.MSG_STATUS_19, null, null
						, EDraftConstant.MSG_TYPE_BILLINFO,voucherInfo.getSbbllRngStrtSn(),voucherInfo.getSbbllRngEndSn());
				return;
			}
		}
		if(oldVoucherInfo==null){
			oldVoucherInfo = billVoucherInfoDao.getBillVoucherInfoByOrigNo(voucherInfo.getOrigNo(), voucherInfo.getSbbllRngStrtSn(), voucherInfo.getSbbllRngEndSn());
		}
		if(oldVoucherInfo==null){
			oldVoucherInfo=new BillAbVoucherinfo();
		}/*else{
			//当库存状态为收票待签或已签收时，返回的正面状态为提示承兑状态则不修改票据状态
			if((AccebillConstant.AB_BILL_STATUS_030001.equals(oldVoucherInfo.getBillStatus()) || AccebillConstant.AB_BILL_STATUS_030006.equals(oldVoucherInfo.getBillStatus())) 
					&& (AccebillConstant.AB_BILL_STATUS_020001.equals(voucherInfo.getBillStatus()) || AccebillConstant.AB_BILL_STATUS_020006.equals(voucherInfo.getBillStatus()))){
				isUpdate=false;
			}
			if((AccebillConstant.NEWBILL_STATUS_CS03.equals(oldVoucherInfo.getBillStatus()) || AccebillConstant.NEWBILL_STATUS_CS02.equals(oldVoucherInfo.getBillStatus()))
					&& (AccebillConstant.NEWBILL_STATUS_CS01.equals(voucherInfo.getBillStatus()) || AccebillConstant.NEWBILL_STATUS_CS02.equals(voucherInfo.getBillStatus()))){
				isUpdate=false;
			}
		}*/

		oldVoucherInfo.setSbbllRngStrtSn(subRngNoList.get(0));
		oldVoucherInfo.setSbbllRngEndSn(subRngNoList.get(1));

		if(isUpdate){
			if(voucherInfo.getBillMoney()!=null){
				oldVoucherInfo.setBillMoney(voucherInfo.getBillMoney()); // 票据金额
			}
			if(voucherInfo.getStartDate()!=null){
				oldVoucherInfo.setStartDate(voucherInfo.getStartDate()); // 出票日
			}
			if(voucherInfo.getEndDate()!=null){
				oldVoucherInfo.setEndDate(voucherInfo.getEndDate()); // 到期日
			}
			if(StringUtil.isNotEmpty(voucherInfo.getBanendrsmtmk())){
				oldVoucherInfo.setBanendrsmtmk(voucherInfo.getBanendrsmtmk()); // 转让标识
			}
			oldVoucherInfo.setBillFlag(11); // 电票

			if(voucherInfo.getBillType()!=null){
				oldVoucherInfo.setBillType(voucherInfo.getBillType()); // 票据类型
			}else if(oldVoucherInfo.getBillType() == null && StringUtil.isNotEmpty(voucherInfo.getOrigNo())){
				oldVoucherInfo.setBillType(voucherInfo.getOrigNo().startsWith("5") || voucherInfo.getOrigNo().startsWith("1") ? 10 : 11);
			}
			
			if(StringUtil.isNotEmpty(voucherInfo.getBillStatus())){
				oldVoucherInfo.setBillStatus(voucherInfo.getBillStatus()); // 票据状态
			}
			if(StringUtil.isNotEmpty(voucherInfo.getEbillBlCrclIndc())){
				oldVoucherInfo.setEbillBlCrclIndc(voucherInfo.getEbillBlCrclIndc()); // 票据状态
			}
//				if(voucherInfo.getAcceptorDate()!=null){
//					oldVoucherInfo.setAcceptorDate(voucherInfo.getAcceptorDate());//票据承兑日
//				}
//				
			if(StringUtil.isNotEmpty(voucherInfo.getPayer())){
				oldVoucherInfo.setPayer(voucherInfo.getPayer());
			}
			if(StringUtil.isNotEmpty(voucherInfo.getPayerAcc())){
				oldVoucherInfo.setPayeracc(voucherInfo.getPayerAcc());
			}
			if(StringUtil.isNotEmpty(voucherInfo.getPayerBankName())){
				oldVoucherInfo.setPayerBankName(voucherInfo.getPayerBankName());
			}
			if(StringUtil.isNotEmpty(voucherInfo.getPayerBankNo())){
				oldVoucherInfo.setPayerbankno(voucherInfo.getPayerBankNo());
			}
			if(StringUtil.isNotEmpty(voucherInfo.getPayee())){
				oldVoucherInfo.setPayee(voucherInfo.getPayee());
			}
			if(StringUtil.isNotEmpty(voucherInfo.getPayeeAcc())){
				oldVoucherInfo.setPayeeacc(voucherInfo.getPayeeAcc());
			}
			if(StringUtil.isNotEmpty(voucherInfo.getPayeeBankName())){
				oldVoucherInfo.setPayeeBankName(voucherInfo.getPayeeBankName());
			}
			if(StringUtil.isNotEmpty(voucherInfo.getPayeeBankNo())){
				oldVoucherInfo.setPayeebankno(voucherInfo.getPayeeBankNo());
			}
			if(StringUtil.isNotEmpty(voucherInfo.getAcceptor())){
				oldVoucherInfo.setAcceptor(voucherInfo.getAcceptor());
			}
			if(StringUtil.isNotEmpty(voucherInfo.getAcceptorAcc())){
				oldVoucherInfo.setAcceptoracc(voucherInfo.getAcceptorAcc());
			}
			if(StringUtil.isNotEmpty(voucherInfo.getAcceptorBankName())){
				oldVoucherInfo.setAcceptorBankName(voucherInfo.getAcceptorBankName());
			}
			if(StringUtil.isNotEmpty(voucherInfo.getAcceptorBankNo())){
				oldVoucherInfo.setAcceptorbankno(voucherInfo.getAcceptorBankNo());
			}

			//设置附加信息
			if(StringUtil.isNotEmpty(voucherInfo.getRiskBillType())){
				oldVoucherInfo.setRiskBillType(voucherInfo.getRiskBillType());
			}
			if(StringUtil.isNotEmpty(voucherInfo.getDisountIncIf())){
				oldVoucherInfo.setDisountIncIf(voucherInfo.getDisountIncIf());
			}
			if(StringUtil.isNotEmpty(voucherInfo.getCashIncIf())){
				oldVoucherInfo.setCashIncIf(voucherInfo.getCashIncIf());
			}
			if(StringUtil.isNotEmpty(voucherInfo.getAvoidClaimDebtsIf())){
				oldVoucherInfo.setAvoidClaimDebtsIf(voucherInfo.getAvoidClaimDebtsIf());
			}
			if(StringUtil.isNotEmpty(voucherInfo.getPayerChannelName())){
				oldVoucherInfo.setPayerChannelName(voucherInfo.getPayerChannelName());
			}
			if(StringUtil.isNotEmpty(voucherInfo.getAcceptorChannelName())){
				oldVoucherInfo.setAcceptorChannelName(voucherInfo.getAcceptorChannelName());
			}
			if(StringUtil.isNotEmpty(voucherInfo.getPayeeChannelName())){
				oldVoucherInfo.setPayeeChannelName(voucherInfo.getPayeeChannelName());
			}

			billBankDao.initBankName(oldVoucherInfo);
			if(!StringUtil.isBlank_new(oldVoucherInfo.getId())){
				billVoucherInfoDao.update(oldVoucherInfo);
				//判断票据状态是否是已签收状态 修改流水状态，优化发送给银行，银行返回慢或者丢失，通过正面信息查询修改
				this.isVoucherInfoSetStatus(voucherInfo,oldVoucherInfo);
				//增加判断票据状态是票据已结清，但是票据是手工登记、或者是签收进来，银行做了自动托收的业务，把最新的6001业务票据改为-2已删除
				/*if(EDraftConstant.BILL_STATUS_000000.equals(voucherInfo.getBillStatus())){
					List<BillAbAccrecords> holdInfoList=billAccrecordsDao.getHoldInfo(voucherInfo.getOrigNo());
					for(BillAbAccrecords holdInfo:holdInfoList){
						if(holdInfo!=null&&StringUtil.isNotEmpty(holdInfo.getBriefId()) 
								&& ("6001".equals(holdInfo.getBriefId())
									|| "9003".equals(holdInfo.getBriefId())
									|| "9010".equals(holdInfo.getBriefId())
								)){
							//生成新的流水信息(托收申请流水)
							saveCollectionApplyAccrecords(oldVoucherInfo, holdInfo);
							//修改上一笔流水为无效
							billBankDao.updateAccrecordsValidSignByRecordId(holdInfo.getId(),AccebillConstant.AB_BILL_VALID_SIGN_2);
						}
					}
				}*/
			}else{
				oldVoucherInfo.setOrigNo(voucherInfo.getOrigNo());
				if(StringUtil.isNotEmpty(voucherInfo.getNewStmIssuofpprInd()) && "1".equals(voucherInfo.getNewStmIssuofpprInd())){
					oldVoucherInfo.setNewStmIssuofpprInd("1");
				} else if(StringUtil.isNotEmpty(voucherInfo.getOrigNo())) {
					//5、6开头为新票
					if(voucherInfo.getOrigNo().startsWith("5") || voucherInfo.getOrigNo().startsWith("6")){
						oldVoucherInfo.setNewStmIssuofpprInd("1");
					}else {
						oldVoucherInfo.setNewStmIssuofpprInd("0");
					}
				}
				if(!"0".equals(oldVoucherInfo.getSbbllRngEndSn())){
					//子票区间为0，说明是旧票或者不可拆分票据，BlgrdbagcrclPrmtInd为0
					oldVoucherInfo.setBlgrdbagcrclPrmtInd("1");
				}else {
					//子票区间为0，说明是旧票或者不可拆分票据，BlgrdbagcrclPrmtInd为0
					oldVoucherInfo.setBlgrdbagcrclPrmtInd("0");
				}
				billVoucherInfoDao.save(oldVoucherInfo);
				
				List<EDraftExcBill> excBillListInfo=eDraftExcBillDao.queryEDraftExcByParam(voucherInfo.getOrigNo(), "", EDraftConstant.MSG_TYPE_BILLINFO,
						voucherInfo.getSbbllRngStrtSn(), voucherInfo.getSbbllRngEndSn());
				String acctId="";
				if(excBillListInfo!=null&&excBillListInfo.size()>0){
					acctId=excBillListInfo.get(0).getPayerAcc();
					this.saveEDraftExcBill(oldVoucherInfo.getId(), oldVoucherInfo.getOrigNo(), acctId, EDraftConstant.MSG_TYPE_BACKINFO, excBillListInfo.get(0).getSignId(),voucherInfo.getSbbllRngStrtSn(), voucherInfo.getSbbllRngEndSn());
				}
			}

			//设置增信信息
			if(voucherInfo.getAddCreditInfoVoList()!=null && voucherInfo.getAddCreditInfoVoList().size()>0){
				BillAddCreditInfo addCreditInfo=null;
				List<BillAddCreditInfo> addCreditInfoList=new ArrayList<BillAddCreditInfo>(voucherInfo.getAddCreditInfoVoList().size());
				for(AddCreditInfoVo info : voucherInfo.getAddCreditInfoVoList() ){
					addCreditInfo =new BillAddCreditInfo();
					addCreditInfo.setVid(oldVoucherInfo.getId());
					BeanUtil.copyProperties(info,addCreditInfo);
					addCreditInfo.setCreateTime(new Date());
					addCreditInfoList.add(addCreditInfo);
				}
				billVoucherInfoDao.delAddCreditInfoByVid(oldVoucherInfo.getId());
				billVoucherInfoDao.batchSave(addCreditInfoList);
			}

			//根据票据号与账号修改指令表状态
			
			if(draftExc!=null){
				eDraftExcBillDao.updateEDraftExcByIdOrOrigNo(voucherInfo.getUuid(), voucherInfo.getOrigNo(), null, EDraftConstant.MSG_STATUS_19, null, null
						, EDraftConstant.MSG_TYPE_BILLINFO,voucherInfo.getSbbllRngStrtSn(),voucherInfo.getSbbllRngEndSn());
				//查询正面信息的时候，有些银行同时返回了多张票号相同但是子票区间不同的信息
				if(!(draftExc.getSbbllRngStrtSn()+draftExc.getSbbllRngEndSn()).equals(voucherInfo.getSbbllRngStrtSn()+voucherInfo.getSbbllRngEndSn())){
					return;
				}
				if(draftExc!=null
						&&StringUtil.isNotEmpty(draftExc.getSignId())){
					BillSign billSign=billSignDao.get(draftExc.getSignId(),true);
					if(billSign!=null
							&& StringUtil.isNotEmpty(billSign.getId())
							&& voucherInfo.getSbbllRngStrtSn().equals(billSign.getSbbllRngStrtSn())
							&& voucherInfo.getSbbllRngEndSn().equals(billSign.getSbbllRngEndSn())){
						//如果是待签收数据查询的正面信息则 票据状态已经不是待签收状态则修改待签收数据未已撤回
//						if(StringUtil.isNotEmpty(voucherInfo.getBillStatus())
//								&&voucherInfo.getBillStatus().length()==6
//								&&!"01".equals(voucherInfo.getBillStatus().substring(4))
//								&&(AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_PENDING==billSign.getStatus()
//									||AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_REPULSE==billSign.getStatus())){
////							billSignDao.updateBillSignIsSignRecal(draftExc.getSignId());
//							billSign.setStatus(AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_RECALL);
//						}
						/*if(StringUtil.isNotEmpty(oldVoucherInfo.getBanendrsmtmk())){
							billSign.setBankendrsmtmk(oldVoucherInfo.getBanendrsmtmk()); // 转让标识
						}*/
						if(oldVoucherInfo.getBillMoney()!=null){
							billSign.setBillMoney(oldVoucherInfo.getBillMoney()); // 票据金额
						}
						if(oldVoucherInfo.getStartDate()!=null){
							billSign.setStartDate(oldVoucherInfo.getStartDate()); // 出票日
						}
						if(oldVoucherInfo.getEndDate()!=null){
							billSign.setEndDate(oldVoucherInfo.getEndDate()); // 到期日
						}

						if(billSign.getBillType() == null && StringUtil.isNotEmpty(billSign.getOrigNo())){
							billSign.setBillType(billSign.getOrigNo().startsWith("5") || billSign.getOrigNo().startsWith("1") ? 10 : 11);
						}

						if(oldVoucherInfo.getBillType()!=null){
							billSign.setBillType(oldVoucherInfo.getBillType()); // 票据类型
						}
						
						if(StringUtil.isNotEmpty(oldVoucherInfo.getBillStatus())){
							billSign.setBillStatus(oldVoucherInfo.getBillStatus()); // 票据状态
						}
						if(StringUtil.isNotEmpty(oldVoucherInfo.getEbillBlCrclIndc())){
							billSign.setEbillBlCrclIndcd(oldVoucherInfo.getEbillBlCrclIndc()); // 流通标志
						}
						if(StringUtil.isNotEmpty(oldVoucherInfo.getPayer())){
							billSign.setPayer(oldVoucherInfo.getPayer());
						}
						if(StringUtil.isNotEmpty(oldVoucherInfo.getPayeracc())){
							billSign.setPayeracc(oldVoucherInfo.getPayeracc());
						}
						if(StringUtil.isNotEmpty(oldVoucherInfo.getPayerBankName())){
							billSign.setPayerbankName(oldVoucherInfo.getPayerBankName());
						}
						if(StringUtil.isNotEmpty(oldVoucherInfo.getPayerbankno())){
							billSign.setPayerbankno(oldVoucherInfo.getPayerbankno());
						}
						if(StringUtil.isNotEmpty(oldVoucherInfo.getPayee())){
							billSign.setPayee(oldVoucherInfo.getPayee());
						}
						if(StringUtil.isNotEmpty(oldVoucherInfo.getPayeeacc())){
							billSign.setPayeeacc(oldVoucherInfo.getPayeeacc());
						}
						if(StringUtil.isNotEmpty(oldVoucherInfo.getPayeeBankName())){
							billSign.setPayeebankName(oldVoucherInfo.getPayeeBankName());
						}
						if(StringUtil.isNotEmpty(oldVoucherInfo.getPayeebankno())){
							billSign.setPayeebankno(oldVoucherInfo.getPayeebankno());
						}
						if(StringUtil.isNotEmpty(oldVoucherInfo.getAcceptor())){
							billSign.setAcceptor(oldVoucherInfo.getAcceptor());
						}
						if(StringUtil.isNotEmpty(oldVoucherInfo.getAcceptoracc())){
							billSign.setAcceptoracc(oldVoucherInfo.getAcceptoracc());
						}
						if(StringUtil.isNotEmpty(oldVoucherInfo.getAcceptorBankName())){
							billSign.setAcceptorbankName(oldVoucherInfo.getAcceptorBankName());
						}
						eDraftExcBillDao.update(billSign);
					}
				}
			}else{
				eDraftExcBillDao.updateAccrecordsStatusByRecordId(voucherInfo.getOrigNo(), null, EDraftConstant.MSG_STATUS_19,null,null
						,EDraftConstant.MSG_TYPE_BILLINFO,voucherInfo.getSbbllRngStrtSn(),voucherInfo.getSbbllRngEndSn());
			}
		}
	}
	private void saveCollectionApplyAccrecords(BillAbVoucherinfo oldVoucherInfo, BillAbAccrecords holdInfo) {
		// 生成新的流水信息(托收申请流水)
		BillAbAccrecords entity = new BillAbAccrecords();
		entity.setVid(oldVoucherInfo.getId());// 票据主表ID
		entity.setVvid(holdInfo.getVvid());// 票据从表ID
		entity.setLastid(holdInfo.getId());// 保存上一笔流水ID
		entity.setLastbillcode(holdInfo.getBriefId());// 保存上一笔业务类型
		entity.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);// 有效标志  0未完成；1可用；2已结束
		entity.setStatus(AccebillConstant.AB_BILL_BUS_STATUS95);// 业务处理状态：-2已删除；-1打回；0 暂存；10提交；11-94审批中；95 已完成
		entity.setOptStatus(AccebillConstant.AB_BILL_OPT_STATUS19);//操作状态：15待复核；16待发送；17已发送待回应；18已回应待签收；19已签收成功业务完成；-1发送失败；-2打回；-3撤回；-4拒签
		entity.setSendSign(AccebillConstant.AB_BILL_SEND_SIGN_N);//是否发送指令标志：0否；1是 ；
		entity.setReturnCode("0");//返回代码
		entity.setReturnMsg("查询正面信息自动生成托收申请流水，银行自动托收");//处理结果
		entity.setBriefId(AccebillConstant.BRIEF_STATUS_020);// 流水业务类型020 托收申请
		entity.setInsideSing(holdInfo.getInsideSing());// 内部业务标志
		entity.setEnterStockOld(holdInfo.getEnterStockNew());// 把上一条所属库存保存到旧的所属库存
		entity.setBailType(holdInfo.getBailType());//保证类型:01开票保证，02承兑保证；03背书保证
		entity.setBankendrsmtmk(holdInfo.getBankendrsmtmk());// 不得转让标记：EM00可再转让；EM01 不得转让
		entity.setApplyDate(new Date());// 申请日期
		entity.setBusinessDate(new Date());// 业务办理日期
		entity.setBifCode(holdInfo.getBifCode());
		entity.setCnAccrecords(holdInfo.getCnAccrecords());
		//entity.setSignUpFlag(voucher.getSignUpFlag());
		entity.setBillFlag(oldVoucherInfo.getBillFlag());
		entity.setSttlmmk(AccebillConstant.AB_STTLMMK_Y);
		// .......业务申请人...，业务接收人....
		entity.setReqRole(holdInfo.getReqRole());//业务申请人类别
		entity.setReqNm(holdInfo.getReqNm());//业务申请人
		entity.setReqCmonid(holdInfo.getReqCmonid());//业务申请人组织机构代码
		entity.setReqAcctId(holdInfo.getReqAcctId());//业务申请人账号
		entity.setReqAcctsvcr(holdInfo.getReqAcctsvcr());//业务申请人开户行号
		entity.setReqAcctsvcrAg(holdInfo.getReqAcctsvcrAg());//业务申请人承接行号
		entity.setRecNm(oldVoucherInfo.getAcceptor());// 业务接收人名称
		// entity.setRecCmonid(abVoucherinfo.getA);//业务接收人组织机构代码
		entity.setRecAcctId(oldVoucherInfo.getAcceptoracc());// 业务接收人账号
		entity.setRecAcctsvcr(oldVoucherInfo.getAcceptorbankno());// 业务接收人开户行号
		entity.setCreateDate(new Date());
		billAccrecordsDao.save(entity);
	}
	
	/**
	 * 判断票据状态是否是已签收状态 修改流水状态，优化发送给银行，银行返回慢或者丢失，通过正面信息查询修改
	 * @param voucherInfo
	 * @param oldVoucherInfo
	 */
	private void isVoucherInfoSetStatus(VoucherInfoVo voucherInfo,
			BillAbVoucherinfo oldVoucherInfo) {
		if(TypeConverter.isSignByBillStatus(voucherInfo.getBillStatus())){
			//获取签收方同意签收流水
			String aidSign = billBankDao.getAccrecordsIdByOrigNoSign(oldVoucherInfo.getOrigNo(),voucherInfo.getSbbllRngStrtSn(),voucherInfo.getSbbllRngEndSn());
			//修改签收方流水信息
			if(StringUtil.isNotEmpty(aidSign)){
				String[] reArr = aidSign.split(",");
				aidSign = reArr[0];
				String briefId = reArr[1];
				voucherInfo.setRecordId(aidSign);
				voucherInfo.setVoucherId(oldVoucherInfo.getId());
				String returnCdoe = "0";
				String returnMsg = "通过正面信息交易查询交易成功";
				if(EDraftConstant.BILL_STATUS_020006.equals(voucherInfo.getBillStatus())
						|| (AccebillConstant.NEWBILL_STATUS_CS02.equals(voucherInfo.getBillStatus()) && AccebillConstant.CIRCULATE_TF0101.equals(voucherInfo.getEbillBlCrclIndc()))){
					billBankDao.updateVoucherInfoViceBoxStatusByRecordIdId(voucherInfo.getRecordId(), AccebillConstant.AB_BOX_STATUS_Y);
					//根据流水ID更新票据流水表信息
					billBankDao.updateAccrecordsInfoByRecordId2(voucherInfo.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS19, null, returnCdoe, returnMsg, voucherInfo.getBillId());
				}else if(EDraftConstant.BILL_STATUS_030006.equals(voucherInfo.getBillStatus()) || AccebillConstant.BRIEF_STATUS_9003.equals(briefId)){
					//修改票据表信息boxStatus：业务处理完成（11）bill_status:提示收票已签收(030006)
					billBankDao.updateVoucherInfoViceBoxStatusByRecordIdId(voucherInfo.getRecordId(), AccebillConstant.AB_BOX_STATUS_Y);
					//根据流水ID更新票据流水表信息 修改流水表status:签收成功 19
					billBankDao.updateAccrecordsInfoByRecordId2(voucherInfo.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS19, AccebillConstant.BRIEF_STATUS_9003, returnCdoe, returnMsg, voucherInfo.getBillId());
					//针对同意签收是否有自动入池判断
					billPoolManageImpService.signAutoPoolOp(voucherInfo.getRecordId());
				}else if(EDraftConstant.BILL_STATUS_100006.equals(voucherInfo.getBillStatus()) || AccebillConstant.BRIEF_STATUS_9010.equals(briefId)){
					//修改票据表信息boxStatus：业务处理完成（11）bill_status:背书已签收(100006)
					billBankDao.updateVoucherInfoViceBoxStatusByRecordIdId(voucherInfo.getRecordId(), AccebillConstant.AB_BOX_STATUS_Y);
					//修改流水表status:签收成功 19
					billBankDao.updateAccrecordsInfoByRecordId(voucherInfo.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS19,returnCdoe,returnMsg, voucherInfo.getBillId());
					//针对同意签收是否有自动入池判断
					billPoolManageImpService.signAutoPoolOp(voucherInfo.getRecordId());
				}else if(EDraftConstant.BILL_STATUS_120006.equals(voucherInfo.getBillStatus()) || AccebillConstant.BRIEF_STATUS_9012.equals(briefId)){
					//修改票据表信息boxStatus：业务处理完成（11）
					billBankDao.updateVoucherInfoViceBoxStatusByRecordIdId(voucherInfo.getRecordId(), AccebillConstant.AB_BOX_STATUS_Y);
					//根据流水ID更新票据流水表信息 修改流水表status:签收成功 19
					billBankDao.updateAccrecordsInfoByRecordId(voucherInfo.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS19, null, returnCdoe, returnMsg, voucherInfo.getBillId());
					//针对质押解除业务进行票据池处理
					billPoolManageImpService.accebillBusPoolOp(voucherInfo.getRecordId());
				}else if(EDraftConstant.BILL_STATUS_180006.equals(voucherInfo.getBillStatus()) || AccebillConstant.BRIEF_STATUS_9018.equals(briefId)){
					//根据票据ID更新票据表信息
					billBankDao.updateVoucherInfoViceBoxStatusByRecordIdId(voucherInfo.getRecordId(), AccebillConstant.AB_BOX_STATUS_Y);
					//根据流水ID更新票据流水表信息 修改流水表status:签收成功 19
					billBankDao.updateAccrecordsInfoByRecordId2(voucherInfo.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS19, AccebillConstant.BRIEF_STATUS_9018, returnCdoe, returnMsg, voucherInfo.getBillId());
				}else if(EDraftConstant.BILL_STATUS_190006.equals(voucherInfo.getBillStatus()) || AccebillConstant.BRIEF_STATUS_9019.equals(briefId)){
					billBankDao.updateVoucherInfoViceBoxStatusByRecordIdId(voucherInfo.getRecordId(), AccebillConstant.AB_BOX_STATUS_Y);
					//根据流水ID更新票据流水表信息 修改流水表status:签收成功 19
					billBankDao.updateAccrecordsInfoByRecordId2(voucherInfo.getRecordId(), AccebillConstant.AB_BILL_OPT_STATUS19, AccebillConstant.BRIEF_STATUS_9019, returnCdoe, returnMsg, voucherInfo.getBillId());
					//针对质押解除业务成功进行票据池处理
					billPoolManageImpService.accebillBusPoolOp(voucherInfo.getRecordId());
				}
			}
			
		}
	}
}
