package com.bytter.biz.finacial.accbook.service.base;
import com.bytter.biz.finacial.accbook.entity.*;
import com.bytter.biz.finacial.accbook.service.FcAccbookBlnedService;
import com.bytter.biz.finacial.accbook.service.FcAccbookInvalidService;
import com.bytter.biz.finacial.accbook.service.FcAccbookRushService;
import com.bytter.biz.finacial.accbook.service.FcChargeAccrueService;
import com.bytter.biz.finacial.accbook.vo.FcAccbookVoucherVO;
import com.bytter.biz.finacial.accbook.vo.VoucherWrapper;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.BusOriType;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.enums.accbook.OperateType;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.finacial.paramconfig.entity.*;
import com.bytter.biz.finacial.paramconfig.service.*;
import com.bytter.biz.finacial.inneraccount.service.FcOnlineLedgerService;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.ApplicationContextUtil;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
/**
 * 手续费凭证处理器
 * 
 * @author liyh
 *
 */
@Component
@Service("chargeProcessor")
@Transactional
public class ChargeProcessor extends BaseService implements Serializable {
	@Autowired
	private FcOnlineLedgerService fcOnlineLedgerService;
	@Autowired
	private FcChargeRuleService ruleService;
	@Autowired
	private FcChargeTypeService chargeTypeService;
	@Autowired
	private ChargeTypeBankTypeRelService chargeTypeBankService;
	@Autowired
	private FcChargeTypeDtlService chargeTypeDtlService;
	@Autowired
	private FcChargeAccrueService chargeAccrueService;
	@Autowired
	private FcBusRaceService busRaceService;
	@Autowired
	private FcAccbookBlnedService bookBlnedService;
	@Autowired
	private FcAccbookRushService rushService;
	@Autowired
	private FcAccbookInvalidService invalidService;
	@DubboReference(check = false)
	private IFcDayCloseService dayCloseService;
	
	public FcOnlineLedgerService getLedgerService() {
		if (fcOnlineLedgerService == null) {
			fcOnlineLedgerService = ApplicationContextUtil.getContext().getBean(FcOnlineLedgerService.class);
		}
		return fcOnlineLedgerService;
	}
	
	public FcChargeRuleService getRuleService() {
		if (ruleService == null) {
			ruleService = ApplicationContextUtil.getContext().getBean(FcChargeRuleService.class);
		}
		return ruleService;
	}
	public FcChargeTypeService getChargeTypeService() {
		if (chargeTypeService == null) {
			chargeTypeService = ApplicationContextUtil.getContext().getBean(FcChargeTypeService.class);
		}
		return chargeTypeService;
	}
	public ChargeTypeBankTypeRelService getChargeTypeBankTypeRelService() {
		if (chargeTypeBankService == null) {
			chargeTypeBankService = ApplicationContextUtil.getContext().getBean(ChargeTypeBankTypeRelService.class);
		}
		return chargeTypeBankService;
	}
	public FcChargeTypeDtlService getChargeTypeDtlService() {
		if (chargeTypeDtlService == null) {
			chargeTypeDtlService = ApplicationContextUtil.getContext().getBean(FcChargeTypeDtlService.class);
		}
		return chargeTypeDtlService;
	}
	public FcChargeAccrueService getChargeAccrueService() {
		if (chargeAccrueService == null) {
			chargeAccrueService = ApplicationContextUtil.getContext().getBean(FcChargeAccrueService.class);
		}
		return chargeAccrueService;
	}
	public FcBusRaceService getBusRaceService() {
		if (busRaceService == null) {
			busRaceService = ApplicationContextUtil.getContext().getBean(FcBusRaceService.class);
		}
		return busRaceService;
	}
	public FcAccbookBlnedService getAccbookBlnedService() {
		if (bookBlnedService == null) {
			bookBlnedService = ApplicationContextUtil.getContext().getBean(FcAccbookBlnedService.class);
		}
		return bookBlnedService;
	}
	public FcAccbookRushService getRushService() {
		if (rushService == null) {
			rushService = ApplicationContextUtil.getContext().getBean(FcAccbookRushService.class);
		}
		return rushService;
	}
	public FcAccbookInvalidService getInvalidService() {
		if (invalidService == null) {
			invalidService = ApplicationContextUtil.getContext().getBean(FcAccbookInvalidService.class);
		}
		return invalidService;
	}
	
	public IFcDayCloseService getDayCloseService() {
		if (dayCloseService == null) {
			dayCloseService = ApplicationContextUtil.getContext().getBean(IFcDayCloseService.class);
		}
		return dayCloseService;
	}
	public void processResult(VoucherWrapper voucher, Map<String, FcOnlineLedgerDTO> ledgerMap) {
		if (voucher.isCharge() || voucher.getOpType() != OperateType.CHECK) {
			return;
		}
		if (ProcessType.PROCESS_COMMON == voucher.getProcessType()) {//正常凭证
			processCommonCharge(voucher, ledgerMap);
		} else if (ProcessType.PROCESS_INVALID == voucher.getProcessType()) {//作废
			processInvalidCharge(voucher);
		} else if (ProcessType.PROCESS_RUSH == voucher.getProcessType()) {//冲账
			processRushCharge(voucher, ledgerMap);
		}
	}
	/**
     * <br>
     * <b>功能：</b>记账流程处理手续费<br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2011-6-15 <br>
     * @param voucher
     */
    private void processCommonCharge(VoucherWrapper voucher, Map<String, FcOnlineLedgerDTO> ledgerMap) {
        //是否逐笔计算手续费
        boolean isStep = false;
        List<FcChargeRule> busRaceChargeRules = findChargeAccrueByVoucher(voucher);
        for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
        	FcAccbook accbook = voucherVO.getAccbook();
        	FcAccbookExt bookExt = voucherVO.getBookExt();
            List<FcChargeAccrue> chargeAccrues = new ArrayList<FcChargeAccrue>();
            
            List<FcAccbookDtl> accbookDtls = voucherVO.getBookDtls();
            //明细大于两条不处理
            if(accbookDtls.size() > 2){
            	continue;
            }
            //贷方银行活期的银行类型
            String bankType = null;
            for (FcAccbookDtl dtl : accbookDtls) {
            	FcOnlineLedgerDTO ledgerDTO  = ledgerMap.get(dtl.getAccount());
            	if(dtl.getCreditMoney().compareTo(BigDecimal.ZERO) > 0){
            		bankType = ledgerDTO.getBankType();//银行类型
            	}
            }
            
            for (FcAccbookDtl dtl : accbookDtls) {
            	FcOnlineLedgerDTO ledgerDTO  = ledgerMap.get(dtl.getAccount());
                List<FcChargeRule> chargeRules = matchChargeRule(busRaceChargeRules, dtl, accbook.getBusRaceId(), ledgerDTO, accbook);
                if (chargeRules.isEmpty()) {
                    continue;
                }
                //update  如果为企业活期账户才会有手续费收取账户
                if (InnerAccountConstant.FCACC_BUSINESS_ACCOUNT.equals(ledgerDTO.getAccType()) 
                        && !StringUtils.isEmpty(ledgerDTO.getChargeAcc()) ) {//没有设置手续费账户不处理
                	String difBank = "";
                	String areaSign = "";
                	if (null != bookExt) {
						difBank = bookExt.getDifBank();
						areaSign = bookExt.getAreaSign();
					}
					chargeAccrues.addAll(createAndSaveChargeAccrue(voucher.getUserCode(), accbook.getTallyDate(), dtl,
							chargeRules, accbook.getEmergencySign(), bankType, difBank, areaSign, ledgerDTO,
							accbook.getVoucherType()));
                }
            }
            if (isStep && !chargeAccrues.isEmpty()) {
                //生成并复核手续费凭证
                getChargeAccrueService().createChargeVoucher(voucher.getBusGlobalId(), voucher.getUserCode(), chargeAccrues);
            } else {
                for (FcChargeAccrue chargeAccrue : chargeAccrues) {
                	chargeAccrue.setCreateTime(DateUtils.getDate());
                	chargeAccrue.setCreateBy(voucher.getUserCode());
                	getChargeAccrueService().save(chargeAccrue);
                }
            }
        }
    }
    /**
     * 功能介绍 : 查找手续费规则。<br>
     * <b>日期：</b>2017年7月4日<br>
     * @version 1.0
     * @author liyh 
     */
    private List<FcChargeRule> findChargeAccrueByVoucher(VoucherWrapper voucher) {
        Set<String> busRaceIds = new HashSet<String>();
        Set<String> voucherTypeCodes = new HashSet<String>();
        String netCode = null;
        for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
        	FcAccbook accbook = voucherVO.getAccbook();
            busRaceIds.add(accbook.getBusRaceId());
            voucherTypeCodes.add(accbook.getVoucherType());
            netCode = accbook.getNetId();
        }
        return getRuleService().queryByBusRaceAndVoucherType(busRaceIds, voucherTypeCodes,netCode);
    }
    /**
     * <br>
     * <b>功能：</b>匹配手续费规则<br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2011-6-17 <br>
     * @param accbook 
     */
    private List<FcChargeRule> matchChargeRule(List<FcChargeRule> busRaceChargeRules, FcAccbookDtl dtl,
    		String busRaceId, FcOnlineLedgerDTO ledgerDTO, FcAccbook accbook) {
        List<FcChargeRule> chargeRules = new ArrayList<FcChargeRule>();
        String dtlSubjectCode = ledgerDTO.getSubjectCode();
        String dtlVoucherTypeCode = accbook.getVoucherType();//凭证种类
        for (FcChargeRule chargeRule : busRaceChargeRules) {
        	//业务种类
        	String raceId = chargeRule.getRaceCode();//系统业务种类
        	FcBusRace busRace = getBusRaceService().get(busRaceId);
        	//系统品种比较
        	if(!(StringUtils.isEmpty(raceId) || busRace.getSysRaceCode().equals(raceId))){
        		continue;
        	}
        	//凭证类型
            String voucherType = chargeRule.getVoucherType();
            if (!(StringUtils.isEmpty(voucherType) || voucherType.equals(dtlVoucherTypeCode))) {
                continue;
            }
            //借贷方科目
            String debitSubject = chargeRule.getDebitSubjCode();
            if (!StringUtils.isEmpty(debitSubject) && dtlSubjectCode.startsWith(debitSubject)) {
                chargeRules.add(chargeRule);
            } else {
                String creditSubject = chargeRule.getCreditSubjCode();
                if (!StringUtils.isEmpty(creditSubject) && dtlSubjectCode.startsWith(creditSubject)) {
                    chargeRules.add(chargeRule);
                }
            }
        }
        if (chargeRules.size() > 1) {
        	//摘要
            List<FcChargeRule> chargeRuleResult = new ArrayList<FcChargeRule>();
            String dtlSummary = dtl.getSummaryContent();
            for (FcChargeRule chargeRule : chargeRules) {
                if (StringUtils.isEmpty(chargeRule.getSummary())
                		|| chargeRule.getSummary().equals(dtlSummary)) {
                    chargeRuleResult.add(chargeRule);
                }
            }
            return chargeRuleResult;
        } else {
            return chargeRules;
        }
    }
    /**
     * 功能介绍 : 作废冲手续费计提。<br>
     * <b>日期：</b>2017年7月6日<br>
     * @version 1.0
     * @author liyh 
     */
    private void processInvalidCharge(VoucherWrapper voucher) {
        if (voucher.getBookVoucher().getAccbook().getBusOriType().equals(BusOriType.HANDLE_CHARGE.get())) {
            processChargeVoucherInvalid(voucher);
        } else {
            processNotChargeVoucherInvalid(voucher);
        }
    }
	/**
	 * 功能介绍 : 手续费凭证作废处理。<br>
	 * <b>日期：</b>2017年7月6日<br>
	 * @version 1.0
	 * @author liyh 
	 */
    private void processChargeVoucherInvalid(VoucherWrapper voucher) {
        Set<String> bookIds = new HashSet<String>();
        for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
        	bookIds.add(voucherVO.getAccbook().getId());
        }
        
        //查询手续费凭证对应的计提记录
        //获取账务关联的手续费明细
    	List<FcChargeAccrue> chargeAccrues = getChargeAccrueService().queryChargeAccrueByOrderNo(bookIds);
        for (FcChargeAccrue chargeAccrue : chargeAccrues) {//更新手续费计提记录为未入账
            chargeAccrue.setTallySign(FinacialConstant.TALLY_SIN_N);
            chargeAccrue.setCheckerCode(null);
            chargeAccrue.setCheckTime(null);
            chargeAccrue.setOrderNo("");
            chargeAccrue.setStatus(FinacialConstant.CHARGE_STATUS_0);//标识删除
            getChargeAccrueService().update(chargeAccrue);
        }
    }
    /**
     * 功能介绍 : 处理非手续费凭证（原凭证产生了手续费凭证，将原凭证作废，则手续费凭证也作废）。<br>
     * <b>日期：</b>2017年11月7日<br>
     * @version 1.0
     * @author liyh 
     */
    private void processNotChargeVoucherInvalid(VoucherWrapper voucher) {
    	Set<String> bookIds = new HashSet<String>();
        for (FcAccbookVoucherVO voucherVO : voucher.getTarget()) {
        	bookIds.add(voucherVO.getAccbook().getId());
        }
        
        List<Map<String, Object>> chargeAccbooks = getInvalidService().findTargetChargeAccbooksBy(bookIds);
        if (EmptyUtils.isEmptyList(chargeAccbooks)) {//未产生手续费计提记录，则不作废也不删除计提明细
			return;
		}
        for (Map<String, Object> map : chargeAccbooks) {
			String tallySign = (String) map.get("TALLYSIGN");//计提标识
        	if (FinacialConstant.TALLY_SIN_Y.equals(tallySign)) {//已计提
				//作废手续费计提凭证
        		String bookId = (String) map.get("BOOKID");//手续费凭证记账ID
        		FcAccbook fcAccbook = getInvalidService().get(bookId, true);
    			invalidChargeVoucher(fcAccbook.getBusGlobalId(), voucher.getUserCode());//作废手续费凭证
			}
        	String chargeId = (String) map.get("ID");//计提ID
        	getChargeAccrueService().invalidChargeAccrue(chargeId);//标识计提记录删除
		}
    }
    
    /**
     * 功能介绍 : 作废凭证。<br>
     * <b>日期：</b>2017年11月8日<br>
     * @version 1.0
     * @author liyh 
     */
    public void invalidChargeVoucher(String busGlobalId, String userCode) {
    	VoucherWrapper invalidChargeVoucher = new VoucherWrapper();
    	invalidChargeVoucher.setAutoCheck(true);
        invalidChargeVoucher.setCharge(true);
        invalidChargeVoucher.setProcessType(ProcessType.PROCESS_INVALID);
        invalidChargeVoucher.setBusGlobalId(busGlobalId);
        invalidChargeVoucher.setUserCode(userCode);//设置操作用户
        
        FcAccbookExt bookExt = new FcAccbookExt();
		bookExt.setInvalidReason("手续费凭证作废");
		FcAccbookVoucherVO invalidVoucher = new FcAccbookVoucherVO();
		invalidVoucher.setBookExt(bookExt);
		invalidChargeVoucher.addTarget(invalidVoucher);
		
        getInvalidService().processInvalidVoucherSubmit(invalidChargeVoucher);
        //getInvalidService().processInvalidVoucherCheck(invalidChargeVoucher);
    }
    /**
     * 功能介绍 : 冲账流程手续费计提处理。<br>
     * <b>日期：</b>2017年7月5日<br>
     * @version 1.0
     * @author liyh 
     */
    private void processRushCharge(VoucherWrapper voucher, Map<String, FcOnlineLedgerDTO> ledgerMap) {
    	/**
    	 * 1、是否存在已计提的手续费记录，如果存在则还原已计提的手续费记录，未计提的记录不用处理
    	 * 存在则判断：
    	 * 2.1、如果手续费记录未计提，则直接删除手续费计提记录
		 * 2.2、如果已计提，则判断手续费凭证是否日结，未日结，则作废，已日结，则冲销
    	 */
        processRushVoucherCharge(voucher.getUserCode(), voucher.getBusGlobalId());
    }
    /**
     * 功能介绍 : 冲账流程,冲已计提手续费。<br>
     * <b>日期：</b>2017年7月5日<br>
     * @version 1.0
     * @author liyh 
     */
    private void processRushVoucherCharge(String userCode, String busGlobalId) {
    	List<FcAccbook> books = getInvalidService().getBooksByBusGlobalId(busGlobalId);
    	Set<String> bookIds = new HashSet<String>();
        for (FcAccbook book : books) {
        	if (!EmptyUtils.isEmpty(book.getOrigVoucherNo())) {
        		bookIds.add(book.getOrigVoucherNo());//冲销凭证的原凭证ID
			}
        }
        List<Map<String, Object>> list = getInvalidService().findTargetChargeAccbooksBy(bookIds);
    	//list = getAccbookBlnedService().findTargetChargeAccbooksBy(origBookId);
        
        if (EmptyUtils.isEmptyList(list)) {//未产生手续费计提记录，则不需要冲销手续费记录
			return;
		}
        //冲账凭证
    	for (Map<String, Object> map : list) {
    		String tallySign = (String) map.get("TALLYSIGN");//计提标识
        	if (FinacialConstant.TALLY_SIN_Y.equals(tallySign)) {//已计提
        		String bookId = (String) map.get("BOOKID");//手续费凭证记账ID
        		//判断是否已日结
	        	FcAccbook fcAccbook = getInvalidService().get(bookId, true);
	        	if (null != fcAccbook) {
	        		invalidChargeVoucher(fcAccbook.getBusGlobalId(), userCode);//作废手续费凭证
				} else {
					FcAccbookBlned fcAccbookBlned = getAccbookBlnedService().get(bookId, true);
					if (null != fcAccbookBlned) {
						rushChargeVoucher(fcAccbookBlned, userCode);//冲销手续费凭证
					}
				}
        	}
        	String chargeId = (String) map.get("ID");//计提ID
        	getChargeAccrueService().invalidChargeAccrue(chargeId);//标识计提记录删除
		}
    }
    
    /**
     * 功能介绍 ：冲销手续费凭证。<br>
     * <b>日期：</b>2017年11月8日<br>
     * @version 1.0
     * @author liyh 
     */
    private void rushChargeVoucher(FcAccbookBlned fcAccbookBlned, String userCode){
    	VoucherWrapper rushChargeVoucher = new VoucherWrapper();
		FcAccbookVoucherVO voucherVO = new FcAccbookVoucherVO();
		rushChargeVoucher.setAutoCheck(true);
		rushChargeVoucher.setCharge(true);
		rushChargeVoucher.setBusGlobalId(fcAccbookBlned.getBusGlobalId());
		rushChargeVoucher.setOriType(BusOriType.RUSH.get());
		rushChargeVoucher.setOpType(OperateType.SUBMIT);
		rushChargeVoucher.setUserCode(userCode);
		rushChargeVoucher.setProcessType(ProcessType.PROCESS_RUSH);
		FcAccbook rushAccbook = new FcAccbook();
		rushAccbook.setBusGlobalId(rushChargeVoucher.getBusGlobalId());
		rushAccbook.setOperatorCode(rushChargeVoucher.getUserCode());
		rushAccbook.setOrigVoucherNo(fcAccbookBlned.getId());//已结凭证Id
		String netId = fcAccbookBlned.getNetId();
		Date tallyDate = getDayCloseService().getDayCloseFinalDate(netId);//日结日期
		rushAccbook.setTallyDate(DateUtils.addDay(tallyDate, 1));//日结日期次日
		//rushAccbook.setInterestStartDate(time);
		FcAccbookExt bookExt = new FcAccbookExt();
		bookExt.setDesReason("冲手续费计提");
		
		voucherVO.setAccbook(rushAccbook);
		voucherVO.setBookExt(bookExt);
		voucherVO.setRush(true);
		rushChargeVoucher.addTarget(voucherVO);
		//提交手续费冲销凭证
		getRushService().submitRushVoucher(rushChargeVoucher);
		//getRushService().checkRushVoucher(rushChargeVoucher);
    }
    
    /**
     * 功能介绍 : 生成手续费明细。<br>
     * <b>日期：</b>2017年7月4日<br>
     * @version 1.0
     * @author liyh 
     */
    private List<FcChargeAccrue> createAndSaveChargeAccrue(String userCode, Date tallyDate, FcAccbookDtl dtl,
            List<FcChargeRule> chargeRules, String emergencySign, String bankType, String difBank, 
            String areaSign, FcOnlineLedgerDTO ledgerDTO, String voucherType) {
        List<FcChargeAccrue> chargeAccrues = new ArrayList<FcChargeAccrue>();
        for (FcChargeRule chargeRule : chargeRules) {
        	List<ChargeTypeChargeRuleRel> chargeRuleRels = getRuleService().listChargeTypeChargeRules(chargeRule.getId());
        	for (ChargeTypeChargeRuleRel chargeRuleRel : chargeRuleRels) {
				String chargeTypeId = chargeRuleRel.getChargeTypeId();
				FcChargeType chargeType = getChargeTypeService().get(chargeTypeId, true);
            	//银行类型
            	if(!StringUtils.isEmpty(bankType)){
            		List<ChargeTypeBankTypeRel> bankTypes = getChargeTypeBankTypeRelService().getChargeTypeRelsByTypeId(chargeTypeId);
            		StringBuffer types = new StringBuffer();
            		for(ChargeTypeBankTypeRel bank : bankTypes){
            			types.append(bank.getBankType()).append(",");
            		}
            		//未匹配到银行类型
            		if(types.length() > 0 && types.indexOf(bankType) == -1){
            			continue;
            		}
            	}
            	//加急标志
            	String emgSign = chargeType.getEmergencySign();
				if (!StringUtils.isEmpty(emgSign)
						&& !FinaBookConstant.EmergencySign.EMERGENCY_SIGN_NULL.get().equals(emgSign)
						&& !emgSign.equals(emergencySign)) {
            		continue;
            	}
            	//币别
            	if(!(StringUtils.isEmpty(chargeType.getCurCode())
            		|| chargeType.getCurCode().equals(ledgerDTO.getCurId()))){
            		continue;
            	}
            	//跨行标志
            	if(!StringUtils.isEmpty(difBank)){
            		if(!(StringUtils.isEmpty(chargeType.getDifBank()) 
            				|| chargeType.getDifBank().equals(difBank))){
            			continue;
            		}
            	}else{
            		if(!NetbankConstant.DIFBANK_0.equals(chargeType.getDifBank())){//非同行
            			//continue;
            		}
            	}
            	//地域标志
            	if(!StringUtils.isEmpty(areaSign)){
            		if(!(StringUtils.isEmpty(chargeType.getAreaSign()) 
            				|| chargeType.getAreaSign().equals(areaSign))){
            			continue;
            		}
            	}else{
            		if(!NetbankConstant.AREASIGN_0.equals(chargeType.getDifBank())){//非同城
            			//continue;
            		}
            	}
                FcChargeAccrue chargeAccrue = new FcChargeAccrue();
                if(StringUtils.isBlank(ledgerDTO.getChargeAcc())){
                	continue;
                }
                chargeAccrue.setAccount(ledgerDTO.getChargeAcc());
                chargeAccrue.setChargeTypeId(chargeType.getId());
                chargeAccrue.setChargeRuleId(chargeRule.getId());
                chargeAccrue.setVoucherType(voucherType);
                chargeAccrue.setChargeMoney(calcCharge(dtl, chargeType));
                chargeAccrue.setTallySign(FinacialConstant.TALLY_SIN_N);//未记账标识
                chargeAccrue.setStatus(FinacialConstant.CHARGE_STATUS_1);//正常状态
                chargeAccrue.setTargetOrderNo(dtl.getBookId());
                chargeAccrue.setOperatorCode(userCode);//操作人
                chargeAccrue.setTargetTallyDate(tallyDate);
                chargeAccrues.add(chargeAccrue);
            }
        }
        return chargeAccrues;
    }
    /**
     * 功能介绍 : 计算费用。<br>
     * <b>日期：</b>2017年7月4日<br>
     * @version 1.0
     * @author liyh 
     */
    private BigDecimal calcCharge(FcAccbookDtl dtl, FcChargeType chargeType) {
        BigDecimal dtlMoney;
        if (dtl.getCreditMoney().compareTo(BigDecimal.ZERO) != 0) {
            dtlMoney = dtl.getCreditMoney();
        } else {
            dtlMoney = dtl.getDebitMoney();
        }
        List<FcChargeTypeDtl> chargeDtls = getChargeTypeDtlService().getChargeTypeDtlByTypeId(chargeType.getId());
        
        //最小范围金额从小到大排序
        Collections.sort(chargeDtls, new Comparator<FcChargeTypeDtl>() {
            public int compare(FcChargeTypeDtl dtl1, FcChargeTypeDtl dtl2) {
                return dtl1.getMinRangeMoney().compareTo(dtl2.getMinRangeMoney());
            }
        });
        BigDecimal chargeMoney;
        BigDecimal charge = chargeType.getCharge() == null ? BigDecimal.ZERO : chargeType.getCharge();
        for (FcChargeTypeDtl chargeDtl : chargeDtls) {
            if (dtlMoney.compareTo(chargeDtl.getMinRangeMoney()) >= 0
                    && dtlMoney.compareTo(chargeDtl.getMaxRangeMoney()) <= 0) {
                if (FinacialConstant.CALC_TYPE_FIXED.equals(chargeDtl.getCalcType().toString())) {//取固定值
                    chargeMoney = chargeDtl.getFixChargeMoney();
                } else {//按比例
                    chargeMoney = dtlMoney.multiply(chargeDtl.getPercent()).divide(
                            BigDecimal.valueOf(100L));
                }
                if (chargeDtl.getMaxCharge() != null && chargeMoney.compareTo(chargeDtl.getMaxCharge()) > 0) {
                    chargeMoney = chargeDtl.getMaxCharge();
                }
                return chargeMoney.add(charge);
            }
        }
        return charge;
    }
    @Override
    public BaseDao getEntityDao() {
        return null;
    }
}
