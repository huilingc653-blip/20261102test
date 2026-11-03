package com.bytter.bill.domain.billbank.pool.service.impl;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.billbank.dao.BillBankDao;
import com.bytter.bill.domain.billbank.entity.BillAbVoucherinfo;
import com.bytter.bill.domain.billbank.pool.dao.BillPoolQueryImpDao;
import com.bytter.bill.domain.billbank.pool.entity.*;
import com.bytter.bill.domain.billbank.pool.service.*;
import com.bytter.bill.domain.billbank.pool.utils.BillPoolConstant;
import com.bytter.bill.domain.billbank.pool.vo.*;
import com.bytter.bill.domain.billbank.util.StringUtil;
import com.bytter.bill.domain.billbank.vo.BankAcc;
import com.bytter.bill.domain.billbank.vo.VAccListVo;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;
/**
 * <br>
 * <b>功能：</b>票据池查询服务接口<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-18 <br>
 */
@Service("billPoolQueryImpService")
@Transactional
public class BillPoolQueryImpService implements IBillPoolQueryService {
	private Logger logger= LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private BillPoolImpService billPoolImpService;
	
	@Autowired
	private BillPoolQueryImpDao billPoolQueryImpDao;
	
	@Autowired
	private BillPoolBailImpService billPoolBailImpService;
	
	@Autowired
	private BillPoolManageImpService billPoolManageImpService;
	
	@Autowired
	private BillPoolDtlImpService billPoolDtlImpService;
	
	@Autowired
	private BillPoolBankExcImpService billPoolBankExcImpService;
	
	@Autowired
	private BillPoolLimitImpService billPoolLimitImpService;
	
	@Autowired
	private BillPoolIntoApplyImpService billPoolIntoApplyImpService;
	
	@Autowired
	private BillPoolOutApplyImpService billPoolOutApplyImpService;
	
	@Autowired
	private BillBankDao billBankDao;
	
	@Override
	public List<PoolBaseVO> queryPoolByBifCode(String bifCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode);
		}
		return billPoolImpService.queryPoolAndReqAccByBifCode(bifCode);
	}
	
	@Override
	public List<PoolBaseVO> queryPoolAndReqAccByBifCode(String bifCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode);
		}
		List<PoolBaseVO> baseVoList = new ArrayList<PoolBaseVO>();
		List<PoolBillVoucherVO> intoVoucherVoList = billPoolIntoApplyImpService.queryIntoApplyResultByBifCode(bifCode, null);
		List<PoolBillVoucherVO> outVoucherVoList = billPoolOutApplyImpService.queryOutApplyResultByBifCode(bifCode, null);
		PoolBaseVO baseVo;
		if(intoVoucherVoList!=null && !intoVoucherVoList.isEmpty()){
			for(PoolBillVoucherVO voucherVo: intoVoucherVoList){
				baseVo = new PoolBaseVO();
				baseVo.setPoolNo(voucherVo.getPoolNo());
				baseVo.setPoolName(voucherVo.getPoolName());
				baseVo.setBifCode(voucherVo.getBifCode());
				baseVo.setReqAcc(voucherVo.getReqAcc());
				baseVo.setReqName(voucherVo.getReqName());
				baseVoList.add(baseVo);
			}
		}
		if(outVoucherVoList!=null && !outVoucherVoList.isEmpty()){
			for(PoolBillVoucherVO voucherVo: outVoucherVoList){
				baseVo = new PoolBaseVO();
				baseVo.setPoolNo(voucherVo.getPoolNo());
				baseVo.setPoolName(voucherVo.getPoolName());
				baseVo.setBifCode(voucherVo.getBifCode());
				baseVo.setReqAcc(voucherVo.getReqAcc());
				baseVo.setReqName(voucherVo.getReqName());
				baseVoList.add(baseVo);
			}
		}
		
		return billPoolImpService.queryPoolByBifCode(bifCode);
	}
	@Override
	public synchronized void savePoolLimitInfo(List<PoolLimitInfoVO> paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("paramsVo="+paramsVo.toString());
		}
		if(paramsVo==null||paramsVo.size()<1){
			return;
		}
		Map<String,PoolBaseVO> poolMap=new HashMap<String,PoolBaseVO>();
		for(PoolLimitInfoVO info:paramsVo){
			String corpId=billPoolImpService.getCorpIdByCorpNameOrOrigNo(info.getCustName(), info.getBankOrgCode());
			//System.out.println("根据CustName:"+info.getCustName()+"  或者 BankOrgCode:"+info.getBankOrgCode()+" 获取corpId");
			if(StringUtil.isEmpty(corpId)){
				// 获取申请人银行信息
				BankAcc bankAcc = billBankDao.getBankAccByAcctId(info.getReqAcc());
				System.out.println("corpId为空时根据ReqAcc："+info.getReqAcc());
				if(bankAcc==null || StringUtil.isEmpty(bankAcc.getCorp_id())){
					System.out.println("获取corpId:空");
					continue;
				}else{
					System.out.println("获取corpId:"+corpId);
					corpId = bankAcc.getCorp_id();
				}
				
			}
			if(StringUtil.isEmpty(corpId)){
				continue;
			}
			info.setCorpId(corpId);
			//System.out.println("根据BusId:"+info.getBusId()+" 或者PoolNo:"+info.getPoolNo()+"获取票据池id");
			if(StringUtil.isEmpty(info.getBusId())&&StringUtil.isEmpty(info.getPoolNo())){
				continue;
			}
			if(StringUtil.isBlank(info.getBusId())&&StringUtil.isNotEmpty(info.getPoolNo())){
				PoolBaseVO poolInfo=billPoolImpService.queryPoolByPoolNo(info.getPoolNo(),info.getBifCode(),poolMap);
				System.out.println("poolInfo:"+poolInfo==null?"空":poolInfo.getBusId()+"  接口代码bifCode："+info.getBifCode());
				if(poolInfo==null){
					continue;
				}
				info.setBusId(poolInfo.getBusId());
			}
			AbPoolLimit poolLimit=setAbPoolLimit(info);
			AbPoolSignInfo poolSignInfo=setAbPoolSignInfo(info);
			System.out.println("保存额度信息：poolLimit="+poolLimit);
			logger.info("保存额度信息：poolLimit="+poolLimit);
			billPoolQueryImpDao.saveOrUpdate(poolLimit);
			//System.out.println("保存额度信息结束");
			billPoolQueryImpDao.saveOrUpdate(poolSignInfo);
			if(StringUtil.isNotEmpty(info.getBailBankAcc())){
				AbPoolBail poolBail=setAbPoolBail(info);
				billPoolQueryImpDao.saveOrUpdate(poolBail);
			}
			//对历史明细进行设置
			addAbPoolLimitHis(poolLimit);
		}
	}
	
	private void initPoolLimitDtlInfo(List<PoolLimitVO> paramsVo){
		if(paramsVo==null||paramsVo.size()<1){
			return;
		}
		
		//判断根据日期数据是降序则根据日期升序排列
		if(paramsVo.size()>1
				&&paramsVo.get(0).getAppDate()!=null
				&&paramsVo.get(paramsVo.size()-1).getAppDate()!=null
				&&paramsVo.get(0).getAppDate().getTime()<paramsVo.get(paramsVo.size()-1).getAppDate().getTime()){
			Collections.reverse(paramsVo);
		}
		//再根据日期和erpid进行再次升序排序操作
		Collections.sort(paramsVo, new Comparator<PoolLimitVO>() {
            @Override
            public int compare(PoolLimitVO o1, PoolLimitVO o2) {
            	if(o1.getAppDate().getTime()>o2.getAppDate().getTime()){
            		return 1;
            	}else if(o1.getAppDate().getTime()<o2.getAppDate().getTime()){
            		return -1;
            	}else{
            		if(o1.getBankId()!=null
            				&&o2.getBankId()!=null){
            			return o1.getBankId().compareTo(o2.getBankId());
            		}
            	}
                return 0;
            }
        });
	}
	
	@Override
	public synchronized void savePoolLimitDtlInfo(List<PoolLimitVO> paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("paramsVo="+paramsVo.toString());
		}
		if(paramsVo==null||paramsVo.size()<1){
			return;
		}
		initPoolLimitDtlInfo(paramsVo);
		
		PoolBaseVO poolInfo=billPoolImpService.queryPoolByPoolNo(paramsVo.get(0).getPoolNo(),paramsVo.get(0).getBifCode());
		Map<String,String> busTypeMap=new HashMap<String,String>();
		Map<String,String> corpMap=new HashMap<String,String>();
		Map<String,List<AbPoolLimitDtl>> corpLimitDtl=new HashMap<String,List<AbPoolLimitDtl>>();
		if(poolInfo!=null){
//			String uuid=UUID.randomUUID().toString().replace("-","").substring(0,27);
//			List<AbPoolLimitDtl> addList=new ArrayList<AbPoolLimitDtl>();
			for(int i=0;i<paramsVo.size();i++){
				PoolLimitVO info=paramsVo.get(i);
				info.setPoolNo(poolInfo.getBusId());
				AbPoolLimitDtl limitDtl=new AbPoolLimitDtl();
				String corpId=billPoolImpService.getCorpId(info.getApplyCorpName(), info.getApplyBankOrgCode(), corpMap);
				if(StringUtil.isEmpty(corpId)){
					continue;
				}
				limitDtl.setCorpId(corpId);
				info.setCorpId(corpId);
				if(StringUtil.isNotEmpty(info.getBustypeDtlCode())){
					String busTypeId=billPoolLimitImpService.getBusTypeId(info.getBustypeCode(), info.getBustypeDtlCode(), busTypeMap);
					if(StringUtil.isEmpty(busTypeId)){
						continue;
					}
					limitDtl.setBustypeId(busTypeId);
					info.setBusId(busTypeId);
				}
//				//判断是否操作重复数据存在则不入库存（corpid+(erpId 或者 金额+日期)）
//				if(billPoolQueryImpDao.isLimitCheckRepeated(limitDtl)){
//					continue;
//				}
//				BeanUtils.copyNotNullProperties(info, limitDtl);
//				String indexStr=("00000"+i);
//				limitDtl.setId(uuid+indexStr.substring(indexStr.length()-5,indexStr.length()));
//				limitDtl.setId(UUID.randomUUID().toString().replace("-", ""));
				limitDtl.setErpId(info.getBankId());
				limitDtl.setLimtType(info.getLimitType());
				limitDtl.setBpId(poolInfo.getBusId());
				limitDtl.setApplyMoney(info.getApplyMoney());
				limitDtl.setPoolMoney(info.getFinalMoney());
				limitDtl.setOldMoney(info.getOldMoney());
				limitDtl.setCreateTime(info.getAppDate());
				limitDtl.setEndDate(info.getEndDate());
				limitDtl.setRmk(info.getRmk());
				limitDtl.setUpdateTime(new Date());
				if(!corpLimitDtl.containsKey(limitDtl.getCorpId())){
					corpLimitDtl.put(limitDtl.getCorpId(), new ArrayList<AbPoolLimitDtl>());
				}
				corpLimitDtl.get(limitDtl.getCorpId()).add(limitDtl);
//				addList.add(limitDtl);
			}
			if(!corpLimitDtl.isEmpty()){
				//删除历史数据，以最新的数据为准
				Date beginDate= DateUtils.getDateYMD(paramsVo.get(0).getAppDate());
				Date endDate=DateUtils.getDateYMD(paramsVo.get(paramsVo.size()-1).getAppDate());
				if(beginDate!=null&&endDate!=null){
					Map<String,Object> delParams=new HashMap<String,Object>();
					delParams.put("bpId", poolInfo.getBusId());
					delParams.put("corpIds", corpLimitDtl.keySet().toArray());
					if(beginDate.getTime()>endDate.getTime()){
						delParams.put("beginDate", endDate);
						delParams.put("endDate", DateUtils.addDay(beginDate,1));
					}else{
						delParams.put("beginDate", beginDate);
						delParams.put("endDate", DateUtils.addDay(endDate,1));
					}
					billPoolQueryImpDao.deleteLimitDtl(delParams);
					for(String key:corpLimitDtl.keySet()){
						List<AbPoolLimitDtl> limitDtl=corpLimitDtl.get(key);
						Map<String,Object> params=new HashMap<String,Object>();
						params.put("bpId", poolInfo.getBusId());
						params.put("corpId", key);
						params.put("startDate", limitDtl.get(0).getCreateTime());
						BigDecimal finalStartMoney=billPoolQueryImpDao.getPoolMoneyByParams(params);
//						for(int i=limitDtl.size()-1;i>=0;i--){
						for(int i=0;i<limitDtl.size();i++){
							AbPoolLimitDtl info=limitDtl.get(i);
//							if(info.getPoolMoney()==null){
//								info.setPoolMoney(finalMoney);
//								if(info.getApplyMoney()!=null){
//									info.setOldMoney(info.getPoolMoney().subtract(info.getApplyMoney()));
//								}
//							}
							if(info.getOldMoney()==null || info.getOldMoney().doubleValue() == 0 ){
								if(i==0){
									info.setOldMoney(finalStartMoney);
								}else{
									AbPoolLimitDtl infoBefor=limitDtl.get(i-1);
									info.setOldMoney(infoBefor.getPoolMoney());
								}
							}
							if((info.getApplyMoney()==null||info.getApplyMoney().compareTo(info.getPoolMoney())==0) && info.getPoolMoney()!=null && info.getOldMoney()!=null){
								info.setApplyMoney(info.getPoolMoney().subtract(info.getOldMoney()));
							}
						}
						billPoolQueryImpDao.batchJdbcSave(limitDtl);
					}
					
				}
//				billPoolQueryImpDao.batchJdbcSave(addList);
			}
		}
	}
	
	
	/**
	 * 初始化额度信息表
	 * @param info
	 * @return
	 */
	private AbPoolLimit setAbPoolLimit(PoolLimitInfoVO info){
		if (logger.isDebugEnabled()) {
			logger.debug("info="+info.toString());
		}
		AbPoolLimit poolLimit=billPoolQueryImpDao.getAbPoolLimitByCorpIdDpId(info.getCorpId(),info.getBusId());
		if(poolLimit==null){
			poolLimit=new AbPoolLimit();
		}
		poolLimit.setCreateTime(new Date());
		poolLimit.setBpId(info.getBusId());
		poolLimit.setCorpId(info.getCorpId());
		poolLimit.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
		//保证金额度
		poolLimit.setBailMoney(info.getBailMoney());
		//已用额度
		poolLimit.setLockMoney(info.getUsedMoney());
		//已用低风险金额
		poolLimit.setUsedLowRiskMoney(info.getUsedLowriskMoney());
		//已用非低风险金额
		poolLimit.setUsedInLowRiskMoney(info.getUsedInLowriskMoney());
		//票据质押额度
		poolLimit.setPledgeMoney(info.getPledgeMoney());
		//票据池总额度
		poolLimit.setPoolMoney(info.getPoolMoney());
		//低风险额度
		poolLimit.setLowRiskMoney(info.getLowriskMoney());
		//非低风险额度
		poolLimit.setInLowRiskMoney(info.getInLowriskMoney());
		poolLimit.setAdjustMoney(info.getAdjustMoney());
		//判断可用额度为空时则计算
		if(info.getValidMoney()==null){ 
			if(poolLimit.getPoolMoney()!=null &&poolLimit.getLockMoney()!=null){
				poolLimit.setValidMoney(poolLimit.getPoolMoney().subtract(poolLimit.getLockMoney()));
			}
			if(poolLimit.getLowRiskMoney()!=null &&poolLimit.getUsedLowRiskMoney()!=null){
				poolLimit.setValidLowRiskMoney(poolLimit.getLowRiskMoney().subtract(poolLimit.getUsedLowRiskMoney()));
			}
			if(poolLimit.getInLowRiskMoney()!=null &&poolLimit.getUsedInLowRiskMoney()!=null){
				poolLimit.setValidInLowRiskMoney(poolLimit.getInLowRiskMoney().subtract(poolLimit.getUsedInLowRiskMoney()));
			}
		}else{
			//设置可用额度
			poolLimit.setValidMoney(info.getValidMoney());
			//可用低风险额度
			poolLimit.setValidLowRiskMoney(info.getValidLowriskMoney());
			//可用非低风险额度
			poolLimit.setValidInLowRiskMoney(info.getValidInLowriskMoney());
		}
		return poolLimit;
	}
	
	/**
	 * 根据额度信息表设置历史额度信息
	 * @param info
	 * @return
	 */
	private void addAbPoolLimitHis(AbPoolLimit info){
		if (logger.isDebugEnabled()) {
			logger.debug("info="+info.toString());
		}
		Date todayDate=DateUtils.getDateYMD();
		AbPoolLimitHis poolLimitHis=billPoolQueryImpDao.getAbPoolLimitHisByCorpIdDpId(info.getCorpId(),info.getBpId(),todayDate);
		String id=null;
		if(poolLimitHis==null){
			poolLimitHis=new AbPoolLimitHis();
		}else{
			id=poolLimitHis.getId();
		}
		BeanUtils.copyNotNullProperties(info, poolLimitHis);
		poolLimitHis.setLimitDate(todayDate);
		poolLimitHis.setId(id);
		billPoolQueryImpDao.saveOrUpdate(poolLimitHis);
	}
	
	/**
	 * 初始化票据池签约信息表
	 * @param info
	 * @return
	 */
	private AbPoolSignInfo setAbPoolSignInfo(PoolLimitInfoVO info){
		if (logger.isDebugEnabled()) {
			logger.debug("info="+info.toString());
		}
		AbPoolSignInfo poolSignInfo=billPoolQueryImpDao.getAbPoolSignInfoByCorpIdDpId(info.getCorpId(),info.getBusId());
		if(poolSignInfo!=null){
			poolSignInfo.setUpdateTime(new Date());
		}else{
			poolSignInfo=new AbPoolSignInfo();
			poolSignInfo.setCreateTime(new Date());
			poolSignInfo.setUpdateTime(new Date());
		}
		poolSignInfo.setBpId(info.getBusId());
		poolSignInfo.setCorpId(info.getCorpId());
		poolSignInfo.setBankAcc(info.getBailBankAcc());
		poolSignInfo.setValidSign(info.getPoolStatus());
		poolSignInfo.setBailMoney(info.getBailMoney());
		poolSignInfo.setConNo(info.getConNo());
		poolSignInfo.setConDate(info.getConDate());
		poolSignInfo.setOrigCode(info.getBankOrgCode());
		poolSignInfo.setOrigName(info.getBankOrgName());
		poolSignInfo.setSignOrgNo(info.getSignOrigNo());
		poolSignInfo.setSignOrgName(info.getSignOrigName());
		poolSignInfo.setSignDate(info.getSignDate());
		return poolSignInfo;
	}
	/**
	 * 初始化保证金信息表
	 * @param info
	 * @return
	 */
	private AbPoolBail setAbPoolBail(PoolLimitInfoVO info){
		AbPoolBail abPoolBail=billPoolBailImpService.getBillPoolBailByBankAcc(info.getBailBankAcc());
		if(abPoolBail==null){
			abPoolBail=new AbPoolBail();
			abPoolBail.setCreateTime(new Date());
		}
		abPoolBail.setBailDate(new Date());
		abPoolBail.setBpId(info.getBusId());
		abPoolBail.setCorpId(info.getCorpId());
		abPoolBail.setBankAcc(info.getBailBankAcc());
		abPoolBail.setValidSign(info.getPoolStatus());
		abPoolBail.setBailMoney(info.getBailMoney());
		if (logger.isDebugEnabled()) {
			logger.debug("abPoolBail="+abPoolBail.toString());
		}
		return abPoolBail;
	}
	
	@Override
	public synchronized void savePoolInOutInfo(List<PoolBillVoucherVO> paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("paramsVo="+paramsVo.toString());
		}
		if(paramsVo==null||paramsVo.size()<1){
			return;
		}
		saveAbPoolDtlBook(paramsVo);
	}
	@Override
	public synchronized void savePoolPdtstInfo(List<PoolBillVoucherVO> paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("paramsVo="+paramsVo.toString());
		}
		if(paramsVo==null||paramsVo.size()<1){
			return;
		}
		saveAbPoolDtlBook(paramsVo);
	}
	@SuppressWarnings("unchecked")
	@Override
	public synchronized void savePoolEmitInfo(List<PoolEmitFinaApplyVO> paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("paramsVo="+paramsVo.toString());
		}
		if(paramsVo==null||paramsVo.size()<1){
			return;
		}
		Map<String,String> corpMap=new HashMap<String,String>();
		//循环补齐要素先根据日期和单位id和池id删除然后再写入数据
		for(PoolEmitFinaApplyVO info:paramsVo){
			//根据客户名称匮乏机构名称获取单位id
			String corpName=StringUtil.isNotEmpty(info.getCorpName())?info.getCorpName():info.getBankOrgName();
			String corpId=billPoolImpService.getCorpId(corpName, info.getBankOrgCode(), corpMap);
			if(StringUtil.isEmpty(corpId)){
				continue;
			}
			//根据银行接口代码查询票据池
			List<PoolBaseVO> poolInfoList=billPoolImpService.queryPoolByBifCode(info.getBifCode());
			if(poolInfoList==null || poolInfoList.isEmpty()){
				continue;
			}
			PoolBaseVO poolInfo = poolInfoList.get(0);
			//根据申请代码、单位ID和、池Id和申请日期获取出票融资申请登记薄信息
			AbPoolFinaApplyBook book=billPoolQueryImpDao.getAbPoolFinaApplyBookByParams(info.getBusAppNo(),corpId,poolInfo.getBusId(),DateUtils.getDateYMD(info.getApplyDate()));
			if(book==null){
				book=new AbPoolFinaApplyBook();
			}
	
			book.setApplyCode(info.getBusAppNo());//申请代码
			book.setCustId(info.getCustId());//客户标识号
			book.setCorpId(corpId);//单位id
			book.setCorpName(corpName);//单位名称
			book.setBpId(poolInfo.getBusId());//票据池id
			//设置登记薄值
			setFinaApplyBook(info, book);
			if(StringUtil.isBlank(book.getId())){
				book.setCreateTime(new Date());
			}else{
				book.setUpdateTime(new Date());
			}
			billPoolQueryImpDao.saveOrUpdate(book);
			//出票融资申请明细表是否存在票据，不存在则新增
			if(info.getFinaApplyDtlVo()!=null && !info.getFinaApplyDtlVo().isEmpty()){
				//根据登记薄id获取明细
				AbPoolFinaApplyDtl applyDtl = billPoolQueryImpDao.getAbPoolFinaApplyDtlByParams(book.getId(),null);
				for(AbPoolFinaApplyDtlVo applyDtlVo : info.getFinaApplyDtlVo()){
					saveOrUpdateBookDtl(book, applyDtl, applyDtlVo);
				}
			}
			
		}
	}
	/**
	 * 设置登记薄值
	 * @param info
	 * @param book
	 */
	private void setFinaApplyBook(PoolEmitFinaApplyVO info,
			AbPoolFinaApplyBook book) {
		if(StringUtil.isNotEmpty(info.getFinanceAmt())){
			book.setFinanceAmt(info.getFinanceAmt());//融资金额
		}
		if(StringUtil.isNotEmpty(info.getBillType())){
			book.setBillType(info.getBillType());//票据类型
		}
		if(StringUtil.isNotEmpty(info.getApplyDate())){
			book.setApplyDate(DateUtils.getDateYMD(info.getApplyDate()));//申请日期
		}
		if(StringUtil.isNotEmpty(info.getStartDate())){
			book.setStDate(DateUtils.getDateYMD(info.getStartDate()));//起始日期
					}
		if(StringUtil.isNotEmpty(info.getEndDate())){
			book.setEndDate(DateUtils.getDateYMD(info.getEndDate()));//到期日期
		}
		if(StringUtil.isNotEmpty(info.getApplyStatus())){
			book.setApplyStatus(info.getApplyStatus());//申请状态
		}
		if(StringUtil.isNotEmpty(info.getApplyStatusName())){
			book.setApplyStatusName(info.getApplyStatusName());//申请状态名称
		}
		if(StringUtil.isNotEmpty(info.getBizCode())){
			book.setBizCode(info.getBizCode());//业务品种代码
			if("301".equals(info.getBizCode())){
				book.setBillType(10);
			}
		}
		if(StringUtil.isNotEmpty(info.getBizName())){
			book.setBizName(info.getBizName());//业务品种名称
		}
		if(StringUtil.isNotEmpty(info.getRmk())){
			book.setRemark(info.getRmk());//备注
		}
		if(StringUtil.isNotEmpty(info.getCurrency())){
			book.setCurrency(info.getCurrency());//货币种类
		}
		if(StringUtil.isNotEmpty(info.getCurrName())){
			book.setCurrname(info.getCurrName());//货币种类
		}
		if(StringUtil.isNotEmpty(info.getBusinessType())){
			book.setBusinessType(info.getBusinessType());//业务类型
		}
		if(StringUtil.isNotEmpty(info.getBusinessName())){
			book.setBusinessName(info.getBusinessName());//业务类型名称
		}
		if(StringUtil.isNotEmpty(info.getType())){
			book.setType(info.getType());//商票交易类型
		}
		if(StringUtil.isNotEmpty(info.getSrvrId())){
			book.setSrvrId(info.getSrvrId());//商票网银跟踪号
		}
		if(StringUtil.isNotEmpty(info.getFinPurpose())){
			book.setFinPurpose(info.getFinPurpose());// 融资用途
		}
		if(StringUtil.isNotEmpty(info.getApplyDesc())){
			book.setApplyDesc(info.getApplyDesc());//申请说明
		}
		if(StringUtil.isNotEmpty(info.getReturnReason())){
			book.setReturnReason(info.getReturnReason());//退回原因
		}
	}
	/**
	 * 更新或保存出票融资登记薄明细数据
	 * @param book 票融资登记薄
	 * @param applyDtl
	 * @param applyDtlVo
	 */
	private void saveOrUpdateBookDtl(AbPoolFinaApplyBook book,
			AbPoolFinaApplyDtl applyDtl, AbPoolFinaApplyDtlVo applyDtlVo) {
		AbPoolFinaApplyDtl bookDtl;
		if(applyDtl==null){
			bookDtl = new AbPoolFinaApplyDtl();
			bookDtl.setBookId(book.getId());
			bookDtl.setCorpId(book.getCorpId());
			bookDtl.setApplyCode(book.getApplyCode());
			bookDtl.setCustId(book.getCustId());
			bookDtl.setOrigNo(applyDtlVo.getOrigNo());
			bookDtl.setBillMoney(applyDtlVo.getBillMoney());
			bookDtl.setBusType(applyDtlVo.getBusType());
			bookDtl.setBillStatus(applyDtlVo.getBillStatus());
			bookDtl.setPayer(applyDtlVo.getPayer());
			bookDtl.setPayerAcc(applyDtlVo.getPayerAcc());
			bookDtl.setPayerBankNo(applyDtlVo.getPayerBankNo());
			bookDtl.setPayee(applyDtlVo.getPayee());
			bookDtl.setPayeeAcc(applyDtlVo.getPayeeAcc());
			bookDtl.setPayeeBankno(applyDtlVo.getPayeeBankno());
			bookDtl.setAcceptor(applyDtlVo.getAcceptor());
			bookDtl.setAcceptorAcc(applyDtlVo.getAcceptorAcc());
			bookDtl.setAcceptorBankNo(applyDtlVo.getAcceptorBankNo());
			bookDtl.setStartDate(applyDtlVo.getStartDate());
			bookDtl.setEndDate(applyDtlVo.getEndDate());
			bookDtl.setBillType(applyDtlVo.getBillType());
			bookDtl.setInvcNo(applyDtlVo.getInvcNo());
			bookDtl.setRealpayAmt(applyDtlVo.getRealpayAmt());
			bookDtl.setRedmrealpayAmt(applyDtlVo.getRedmrealpayAmt());
			bookDtl.setBanendrsmtmk(applyDtlVo.getBanendrsmtmk());
			bookDtl.setPromsfee(applyDtlVo.getPromsfee());
			bookDtl.setCreateDate(new Date());
		}else{
			bookDtl=billPoolQueryImpDao.getAbPoolFinaApplyDtlByParams(book.getId(),applyDtlVo.getOrigNo());
			if(StringUtil.isNotEmpty(applyDtlVo.getBillStatus())){
				bookDtl.setBillStatus(applyDtlVo.getBillStatus());
			}
			if(StringUtil.isNotEmpty(applyDtlVo.getPromsfee())){
				bookDtl.setPromsfee(applyDtlVo.getPromsfee());
			}
			bookDtl.setUpdateDate(new Date());
		}
		billPoolQueryImpDao.saveOrUpdate(bookDtl);
	}
	@SuppressWarnings("unchecked")
	@Override
	public synchronized void savePoolBailInfo(List<PoolBailVO> paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("paramsVo="+paramsVo.toString());
		}
		if(paramsVo==null||paramsVo.size()<1){
			return;
		}
		Map<String,AbPoolBailBook> deleMap=new HashMap<String, AbPoolBailBook>();
		Map<String,String> corpMap=new HashMap<String,String>();
		Map<String,PoolBaseVO> poolMap=new HashMap<String,PoolBaseVO>();
		List<AbPoolBailBook> addList=new ArrayList<AbPoolBailBook>();
		//循环补齐要素先根据日期和单位id和池id删除然后再写入数据
		for(PoolBailVO info:paramsVo){
			String corpName=StringUtil.isNotEmpty(info.getCorpName())?info.getCorpName():info.getBankOrgName();
			String corpId=billPoolImpService.getCorpId(corpName, info.getBankOrgCode(), corpMap);
			if(StringUtil.isEmpty(corpId)){
				continue;
			}
			PoolBaseVO poolInfo=billPoolImpService.queryPoolByPoolNo(info.getPoolNo(),info.getBifCode(),poolMap);
			if(poolInfo==null){
				continue;
			}
			AbPoolBailBook bailBook=new AbPoolBailBook();
			String key=corpId+"_"+poolInfo.getBusId()+"_"+DateUtils.formatDate(info.getAppDate());
			if(!deleMap.containsKey(key)){
				deleMap.put(key, bailBook);
			}
			bailBook.setBailAcc(info.getBankAcc());
			bailBook.setBpId(poolInfo.getBusId());
			bailBook.setCorpId(corpId);
			bailBook.setCreateTime(new Date());
			bailBook.setExtId(info.getBankId());
			bailBook.setMoney(info.getMoney());
			bailBook.setOppAcc(info.getOppBankAcc());
			bailBook.setOppAccName(info.getOppAccName());
			bailBook.setOppBankName(info.getOppBankName());
			bailBook.setOppBankNo(info.getOppBankNo());
//			bailBook.setTransDate(DateUtils.getDateYMD(info.getAppDate()));
			bailBook.setTransDate(info.getAppDate());
			bailBook.setType(info.getType());
			addList.add(bailBook);
		}
		if(addList.size()>0){
			billPoolQueryImpDao.deletePoolBail(deleMap);
			billPoolQueryImpDao.batchJdbcSave(addList);
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public synchronized void savePoolHoldInfo(List<PoolBillVoucherVO> paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("paramsVo="+paramsVo.toString());
		}
		if(paramsVo==null||paramsVo.size()<1){
			return;
		}
		Map<String,Boolean> origNoMap=new HashMap<String,Boolean>();
//		Map<String,AbPoolHoldBook> deleMap=new HashMap<String, AbPoolHoldBook>();
		Map<String,Object> deleMap=new HashMap<String,Object>();
		Map<String,String> corpMap=new HashMap<String,String>();
		Map<String,PoolBaseVO> poolMap=new HashMap<String,PoolBaseVO>();
		List<AbPoolHoldBook> addList=new ArrayList<AbPoolHoldBook>();
		List<BillAbVoucherinfo> addVoucherList=new ArrayList<BillAbVoucherinfo>();
		Date beginDate=null;
		Date endDate=null;
		Map<String,Boolean> keyCheck=new HashMap<String,Boolean>();
		//循环补齐要素先根据日期和单位id和池id删除然后再写入数据
		for(PoolBillVoucherVO info:paramsVo){
			String corpName=StringUtil.isNotEmpty(info.getCorpName())?info.getCorpName():info.getBankOrgName();
			String corpId=billPoolImpService.getCorpId(corpName, info.getBankOrgCode(), corpMap);
			//System.out.println("开始写入在池交易明细corpId===="+corpId+" corpName="+info.getCorpName()+" BankOrgName="+info.getBankOrgCode()+" ReqAcc="+info.getReqAcc());
			if(StringUtil.isEmpty(corpId)){
				// 获取申请人银行信息
				BankAcc bankAcc = billBankDao.getBankAccByAcctId(info.getReqAcc());
				if(bankAcc==null || StringUtil.isEmpty(bankAcc.getCorp_id())){
					continue;
				}else{
					corpId = bankAcc.getCorp_id();
					corpMap.put(corpId, corpId);
				}
				
			}
			if(StringUtil.isEmpty(corpId)){
				continue;
			}
			PoolBaseVO poolInfo=billPoolImpService.queryPoolByPoolNo(info.getPoolNo(),info.getBifCode(),poolMap);
			//System.out.println("开始写入在池交易明细poolInfo===="+poolInfo);
			if(poolInfo==null){
				continue;
			}
			deleMap.put("bpId", poolInfo.getBusId());
			//对票据重复数据进行剔除
			String key=corpId+"_"+poolInfo.getBusId()+"_"+info.getOrigNo();
			if(keyCheck.containsKey(key)){
				continue;
			}else{
				keyCheck.put(key, true);
			}
			
			if(beginDate==null){
				beginDate=info.getTradeDate();
				endDate=info.getTradeDate();
			}else{
				if(info.getTradeDate().getTime()<beginDate.getTime()){
					beginDate=info.getTradeDate();
				}
				if(info.getTradeDate().getTime()>endDate.getTime()){
					endDate=info.getTradeDate();
				}
			}
			
			AbPoolHoldBook book=new AbPoolHoldBook();
//			String key=corpId+"_"+poolInfo.getBusId()+"_"+DateUtils.formatDate(info.getTradeDate());
//			if(!deleMap.containsKey(key)){
//				deleMap.put(key, book);
//			}
			book.setBpId(poolInfo.getBusId());
			book.setCorpId(corpId);
			book.setExtId(info.getBankId());
			book.setOrigNo(info.getOrigNo());
			book.setInoutType(info.getInoutType());
//			book.setCreateTime(DateUtils.getDateYMD(info.getTradeDate()));
			book.setCreateTime(info.getTradeDate());
			book.setUpdateTime(new Date());
			book.setVoucherVO(info);
			book.setLimitMoney(info.getLimitMoney());
			if(book.getLimitMoney()==null && info.getEndDate()!=null && info.getEndDate().getTime()>(new Date().getTime())){
				book.setLimitMoney(info.getBillMoney());
			}
			addList.add(book);
			//判断票据主表是否存在票据，不存在则新增
			if(StringUtil.isNotEmpty(info.getOrigNo())
					&&!isSaveBillAbVoucherinfo(info.getOrigNo(), origNoMap)){
				BillAbVoucherinfo voucher=getBillAbVoucherinfo(info);
				addVoucherList.add(voucher);
			}
		}
		if(addList.size()>0){
			int size=billPoolQueryImpDao.batchJdbcSave(addVoucherList);
			logger.debug("写入主表："+size);
			deleMap.put("corpId", corpMap.values().toArray());
			deleMap.put("beginDate", DateUtils.getDateYMD(beginDate));
			deleMap.put("endDate", DateUtils.addDay(DateUtils.getDateYMD(endDate), 1));
			
			logger.debug("deleMap："+deleMap.toString());
			size=billPoolQueryImpDao.deletePoolHoldBook(deleMap);
			
			logger.debug("删除持有账簿："+size);
			size=billPoolQueryImpDao.batchJdbcSave(addList);
			logger.debug("写入持有账簿："+size);
			//对持有池内票据进行票据明细表录入操作，保证能正常出池和池转换
			billPoolManageImpService.addPoolDtlOp(addList);
			logger.debug("入库成功！");
			//System.out.println("开始写入在池交易明细入库成功！");
		}
	}
	@Override
	public synchronized void savePoolIsPayInfo(List<PoolBillVoucherVO> paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("paramsVo="+paramsVo.toString());
		}
		if(paramsVo==null||paramsVo.size()<1){
			return;
		}
		saveAbPoolDtlBook(paramsVo, true);
	}
	/**
	 * 写入交易明细表账簿数据
	 * @param paramsVo
	 */
	private void saveAbPoolDtlBook(List<PoolBillVoucherVO> paramsVo){
		if (logger.isDebugEnabled()) {
			logger.debug("paramsVo="+paramsVo.toString());
		}
		saveAbPoolDtlBook(paramsVo, false);
	}
	
	/**
	 * 写入交易明细表账簿数据
	 * @param paramsVo
	 * @param keyIsBillflag
	 */
	@SuppressWarnings("unchecked")
	private void saveAbPoolDtlBook(List<PoolBillVoucherVO> paramsVo,boolean keyIsBillflag){
		//System.out.println("开始写入出入池明细薄============");
		Map<String,AbPoolDtlBook> deleMap=new HashMap<String, AbPoolDtlBook>();
		Map<String,String> corpMap=new HashMap<String,String>();
		Map<String,Boolean> origNoMap=new HashMap<String,Boolean>();
		Map<String,PoolBaseVO> poolMap=new HashMap<String,PoolBaseVO>();
		List<AbPoolDtlBook> addList=new ArrayList<AbPoolDtlBook>();
		List<AbPoolDtlBook> inDtlList=new ArrayList<AbPoolDtlBook>();
		List<AbPoolDtlBook> outDtlList=new ArrayList<AbPoolDtlBook>();
		List<BillAbVoucherinfo> addVoucherList=new ArrayList<BillAbVoucherinfo>();
		//循环补齐要素先根据日期和单位id和池id删除然后再写入数据
		for(PoolBillVoucherVO info:paramsVo){
			String corpName=StringUtil.isNotEmpty(info.getCorpName())?info.getCorpName():info.getBankOrgName();
			String corpId=billPoolImpService.getCorpId(corpName, info.getBankOrgCode(), corpMap);
			if(StringUtil.isEmpty(corpId)){
				// 获取申请人银行信息
				BankAcc bankAcc = billBankDao.getBankAccByAcctId(info.getReqAcc());
				if(bankAcc==null || StringUtil.isEmpty(bankAcc.getCorp_id())){
					continue;
				}else{
					corpId = bankAcc.getCorp_id();
					corpMap.put(corpId, corpId);
				}
				
			}
			//System.out.println("开始写入出入池明细薄corpId===="+corpId+" corpName="+info.getCorpName()+" BankOrgName="+info.getBankOrgName()+" ReqAcc="+info.getReqAcc());
			PoolBaseVO poolInfo=billPoolImpService.queryPoolByPoolNo(info.getPoolNo(),info.getBifCode(),poolMap);
			//System.out.println("开始写入出入池明细薄poolInfo===="+poolInfo);
			if(poolInfo==null){
				continue;
			}
			AbPoolDtlBook book=new AbPoolDtlBook();
			String key=corpId+"_"+poolInfo.getBusId()+"_"+info.getBriefId()+"_"+DateUtils.formatDate(info.getTradeDate());
			if(keyIsBillflag){
				key+="_"+info.getBillFlag();
			}
			if(!deleMap.containsKey(key)){
				deleMap.put(key, book);
			}
			book.setBankendrsmtmk(info.getBanendrsmtmk());
			book.setBpId(poolInfo.getBusId());
			book.setCorpId(corpId);
			book.setExtId(info.getBankId());
			book.setOrigNo(info.getOrigNo());
			book.setInoutType(info.getInoutType());
			book.setRsn(info.getRsn());
			book.setSttlmmk(info.getSttlmmk());
//			book.setCreateTime(DateUtils.getDateYMD(info.getTradeDate()));
			book.setCreateTime(info.getTradeDate());
			book.setUpdateTime(new Date());
			book.setBriefId(info.getBriefId());
			addList.add(book);
			
			if("8019".equals(book.getBriefId())){
				outDtlList.add(book);
			}else if("8018".equals(book.getBriefId())){
				inDtlList.add(book);
			}
			
			//判断票据主表是否存在票据，不存在则新增
			if(StringUtil.isNotEmpty(info.getOrigNo())
					&&!isSaveBillAbVoucherinfo(info.getOrigNo(), origNoMap)){
				BillAbVoucherinfo voucher=getBillAbVoucherinfo(info);
				addVoucherList.add(voucher);
			}
		}
		//System.out.println("开始写入出入池明细薄保存addList.size===="+addList.size());
		if(addList.size()>0){
			int size=billPoolQueryImpDao.batchJdbcSave(addVoucherList);
			logger.debug("写入主表："+size);
			size=billPoolQueryImpDao.deletePoolDtlBook(deleMap,keyIsBillflag);
			logger.debug("删除交易明细表："+size);
			size=billPoolQueryImpDao.batchJdbcSave(addList);
			logger.debug("写入出入池登记簿："+size);
			//修改在池登记簿的入池日期
			if(inDtlList.size()>0){
				for(AbPoolDtlBook inDtlInfo:inDtlList){
					size=billPoolQueryImpDao.updatePoolHoldBookCreateTimeByDtlBook(inDtlInfo);
					logger.debug("修改在池登记簿的入池日期："+size+",inDtlList="+inDtlList.toString());
				}
			}
			
			//如果有出池记录则处理在池登记簿和处理可出池数据处理
			if(outDtlList.size()>0){
				for(AbPoolDtlBook outDtlInfo:outDtlList){
					//处理在池登记簿
					size=billPoolQueryImpDao.deletePoolHoldBookByDtlBook(outDtlInfo);
					logger.debug("删除处理在池登记簿："+size+",inDtlList="+outDtlInfo.toString());
					//处理可出池数据为失效
//					billPoolQueryImpDao.deletePoolHoldBookByDtlBook(outDtlInfo);
					AbPoolDtl poolDtl=billPoolDtlImpService.getAbPoolDtlHoldByParams(outDtlInfo.getBpId(),outDtlInfo.getCorpId(),outDtlInfo.getOrigNo());
					if(poolDtl!=null 
							&& outDtlInfo.getCreateTime().getTime()>poolDtl.getCreateTime().getTime()
							&& AccebillConstant.AB_BILL_VALID_SIGN_1.equals(poolDtl.getValidSign())){
						//设置出池记录
						billPoolManageImpService.addOutPoolDtlOp(poolDtl,outDtlInfo.getCreateTime());
						logger.debug("处理可出池数据为失效完成,poolDtl="+poolDtl.toString());
					}
				}
			}
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public void savePoolFinaApplyInfo(List<PoolBillVoucherVO> paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("paramsVo="+paramsVo.toString());
		}
		Map<String,AbPoolDtlBook> deleMap=new HashMap<String, AbPoolDtlBook>();
		Map<String,String> corpMap=new HashMap<String,String>();
		Map<String,Boolean> origNoMap=new HashMap<String,Boolean>();
		Map<String,PoolBaseVO> poolMap=new HashMap<String,PoolBaseVO>();
		List<AbPoolDtlBook> addList=new ArrayList<AbPoolDtlBook>();
		List<BillAbVoucherinfo> addVoucherList=new ArrayList<BillAbVoucherinfo>();
		//循环补齐要素先根据日期和单位id和池id删除然后再写入数据
		for(PoolBillVoucherVO info:paramsVo){
			String corpName=StringUtil.isNotEmpty(info.getCorpName())?info.getCorpName():info.getBankOrgName();
			String corpId=billPoolImpService.getCorpId(corpName, info.getBankOrgCode(), corpMap);
			if(StringUtil.isEmpty(corpId)){
				continue;
			}
			PoolBaseVO poolInfo=billPoolImpService.queryPoolByPoolNo(info.getPoolNo(),info.getBifCode(),poolMap);
			if(poolInfo==null){
				continue;
			}
			AbPoolDtlBook book=new AbPoolDtlBook();
			String key=corpId+"_"+poolInfo.getBusId()+"_"+info.getBriefId()+"_"+DateUtils.formatDate(info.getTradeDate());
			if(!deleMap.containsKey(key)){
				deleMap.put(key, book);
			}
			book.setBankendrsmtmk(info.getBanendrsmtmk());
			book.setBpId(poolInfo.getBusId());
			book.setCorpId(corpId);
			book.setExtId(info.getBankId());
			book.setOrigNo(info.getOrigNo());
			book.setInoutType(info.getInoutType());
			book.setRsn(info.getRsn());
			book.setSttlmmk(info.getSttlmmk());
			book.setCreateTime(DateUtils.getDateYMD(info.getTradeDate()));
			book.setUpdateTime(new Date());
			book.setBriefId(info.getBriefId());
			addList.add(book);
			//判断票据主表是否存在票据，不存在则新增
			if(StringUtil.isNotEmpty(info.getOrigNo())
					&&!isSaveBillAbVoucherinfo(info.getOrigNo(), origNoMap)){
				BillAbVoucherinfo voucher=getBillAbVoucherinfo(info);
				addVoucherList.add(voucher);
			}
		}
		if(addList.size()>0){
			billPoolQueryImpDao.batchJdbcSave(addVoucherList);
			billPoolQueryImpDao.deletePoolDtlBook(deleMap,false);
			billPoolQueryImpDao.batchJdbcSave(addList);
		}
	}
	
	/**
	 * 判断票据号是否存在，存在返回true，不存在返回false
	 * @param origNo
	 * @param origNoMap
	 * @return
	 */
	private boolean isSaveBillAbVoucherinfo(String origNo,Map<String,Boolean> origNoMap){
		if(origNoMap.containsKey(origNo)){
			return origNoMap.get(origNo);
		}
		boolean isOrigNo=billPoolQueryImpDao.isOrigNo(origNo);
		origNoMap.put(origNo, true);//避免重复数据写入
		return isOrigNo;
	}
	
	/**
	 * 设置票据要素
	 * @param info
	 * @return
	 */
	private BillAbVoucherinfo getBillAbVoucherinfo(PoolBillVoucherVO info){
		BillAbVoucherinfo vouche=new BillAbVoucherinfo();
		BeanUtils.copyNotNullProperties(info, vouche);
		vouche.setId(null);
		return vouche;
	}
	
	/**
	 * 设置票据要素
	 * @param info
	 * @return
	 */
	private BillAbVoucherinfo getBillAbVoucherinfo(PoolEmitFinaApplyVO info){
		BillAbVoucherinfo vouche=new BillAbVoucherinfo();
		BeanUtils.copyNotNullProperties(info, vouche);
		vouche.setId(null);
		return vouche;
	}
	
	@Override
	public List<VAccListVo> queryBillAccByBifCode(String bifCode) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode);
		}
		List<VAccListVo> list = billBankDao.queryVAccListVoNew(bifCode);
		if (logger.isDebugEnabled() && list!=null) {
			logger.debug("list="+list.toString());
		}
		return list;
	}
	@Override
	public List<PoolQueryExcVO> queryPoolLimitExcByBifCode(String bifCode) {
		return queryPoolExcByBifCode(bifCode, BillPoolConstant.POOL_EXC_QUERY_TYPE_LIMIT);
	}
	@Override
	public List<PoolQueryExcVO> queryPoolLimitDtlExcByBifCode(String bifCode) {
		return queryPoolExcByBifCode(bifCode, BillPoolConstant.POOL_EXC_QUERY_TYPE_LIMITDTL);
	}
	@Override
	public List<PoolQueryExcVO> queryPoolInOutExcByBifCode(String bifCode) {
		return queryPoolExcByBifCode(bifCode, BillPoolConstant.POOL_EXC_QUERY_TYPE_INOUT);
	}
	@Override
	public List<PoolQueryExcVO> queryPoolBailExcByBifCode(String bifCode) {
		return queryPoolExcByBifCode(bifCode, BillPoolConstant.POOL_EXC_QUERY_TYPE_BAIL);
	}
	@Override
	public List<PoolQueryExcVO> queryPoolHoldExcByBifCode(String bifCode) {
		return queryPoolExcByBifCode(bifCode, BillPoolConstant.POOL_EXC_QUERY_TYPE_HOLD);
	}
	@Override
	public List<PoolQueryExcVO> queryPoolExcByBifCode(String bifCode,String queryType) {
		if (logger.isDebugEnabled()) {
			logger.debug("bifCode="+bifCode+",queryType="+queryType);
		}
		List<PoolQueryExcVO> list = billPoolQueryImpDao.queryPoolExcByBifCode(bifCode,queryType);
		if (logger.isDebugEnabled() && list!=null) {
			logger.debug("list="+list.toString());
		}
		return list;
	}
	@Override
	public boolean updatePoolQueryExc(String busId, String reCode, String reMsg) {
		PoolQueryExcVO paramsVo=new PoolQueryExcVO();
		paramsVo.setBusId(busId);
		paramsVo.setReCode(reCode);
		paramsVo.setReMsg(reMsg);
		billPoolBankExcImpService.updateBillPoolBankExc(paramsVo);
		String optStatus=AccebillConstant.AB_BILL_OPT_STATUS17;
		if(AccebillConstant.BANK_RE_CODE0.equals(reCode)){
			optStatus=AccebillConstant.AB_BILL_OPT_STATUS19;
		}else if(AccebillConstant.BANK_RE_CODE2.equals(reCode)){
			optStatus=AccebillConstant.AB_BILL_OPT_STATUS_1;
		}
		return billPoolQueryImpDao.updatePoolQueryExc(busId,optStatus);
	}
	@Override
	public List<PoolQueryExcVO> queryPoolExcByBifCode(String bifCode) {
		String queryType=BillPoolConstant.POOL_EXC_QUERY_TYPE_LIMIT+","
				+BillPoolConstant.POOL_EXC_QUERY_TYPE_INOUT+","
				+BillPoolConstant.POOL_EXC_QUERY_TYPE_BAIL+","
				+BillPoolConstant.POOL_EXC_QUERY_TYPE_HOLD+","
				+BillPoolConstant.POOL_EXC_QUERY_TYPE_LIMITDTL;
		return queryPoolExcByBifCode(bifCode,queryType);
	}
	
	@Override
	public synchronized void saveAbPoolBailInfo(List<PoolBailVO> paramsVo) {
		if (logger.isDebugEnabled()) {
			logger.debug("paramsVo="+paramsVo.toString());
		}
		if(paramsVo==null||paramsVo.size()<1){
			return;
		}
		Map<String,String> corpMap=new HashMap<String,String>();
		Map<String,PoolBaseVO> poolMap=new HashMap<String,PoolBaseVO>();
		for(PoolBailVO info:paramsVo){
			String corpName=StringUtil.isNotEmpty(info.getCorpName())?info.getCorpName():info.getBankOrgName();
			String corpId=billPoolImpService.getCorpId(corpName, info.getBankOrgCode(), corpMap);
			if(StringUtil.isEmpty(corpId) && StringUtil.isNotEmpty(info.getBankAcc())){
				// 获取保证金账户银行信息
				BankAcc bankAcc = billBankDao.getBankAccByAcctId(info.getBankAcc());
				if(bankAcc==null || StringUtil.isEmpty(bankAcc.getCorp_id())){
					continue;
				}else{
					corpId = bankAcc.getCorp_id();
					corpMap.put(corpId, corpId);
				}
				
			}
			info.setCorpId(corpId);
			if(StringUtil.isEmpty(info.getBpId())&&StringUtil.isEmpty(info.getPoolNo())){
				continue;
			}
			if(StringUtil.isBlank(info.getBusId())&&StringUtil.isNotEmpty(info.getPoolNo())){
				PoolBaseVO poolInfo=billPoolImpService.queryPoolByPoolNo(info.getPoolNo(),info.getBifCode(),poolMap);
				if(poolInfo==null){
					continue;
				}
				info.setBpId(poolInfo.getBusId());
			}
			AbPoolBail poolBail=setAbPoolBail(info);
			billPoolQueryImpDao.saveOrUpdate(poolBail);
			System.out.println("保存保证金信息：poolBail="+poolBail);
			logger.info("保存保证金信息：poolBail="+poolBail);
		}
	}
	
	/**
	 * 初始化保证金信息表
	 * @param info
	 * @return
	 */
	private AbPoolBail setAbPoolBail(PoolBailVO info){
		AbPoolBail abPoolBail=billPoolBailImpService.getBillPoolBailByBankAcc(info.getBankAcc());
		if(abPoolBail==null){
			abPoolBail=new AbPoolBail();
			abPoolBail.setCreateTime(new Date());
		}
		abPoolBail.setBailDate(new Date());
		abPoolBail.setBpId(info.getBpId());
		abPoolBail.setCorpId(info.getCorpId());
		abPoolBail.setBankAcc(info.getBankAcc());
		abPoolBail.setValidSign(AccebillConstant.AB_BILL_VALID_SIGN_1);
		abPoolBail.setBailMoney(info.getMoney());
		if (logger.isDebugEnabled()) {
			logger.debug("abPoolBail="+abPoolBail.toString());
		}
		return abPoolBail;
	}
}
