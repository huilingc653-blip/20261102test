package com.bytter.biz.finacial.interest.service.proccessor;

import com.bytter.biz.finacial.accbook.entity.FcAccbook;
import com.bytter.biz.finacial.accbook.entity.FcAccbookDtl;
import com.bytter.biz.finacial.accbook.vo.FcAccbookVoucherVO;
import com.bytter.biz.finacial.accbook.vo.VoucherWrapper;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.biz.finacial.api.constant.SubjectConstant;
import com.bytter.biz.finacial.api.enums.interest.InterestProcessType;
import com.bytter.biz.finacial.interest.bean.AccInstrateBean;
import com.bytter.biz.finacial.interest.bean.AccInterestInfo;
import com.bytter.biz.finacial.interest.bean.InstrateAccuBean;
import com.bytter.biz.finacial.interest.bean.InstrateAdjustBean;
import com.bytter.biz.finacial.interest.entity.FcAccuBalancef;
import com.bytter.biz.finacial.interest.entity.FcInterestDtl;
import com.bytter.biz.finacial.interest.entity.FcInterestDtlTemp;
import com.bytter.biz.finacial.interest.vo.FcFinacialAccbookDtlVO;
import com.bytter.biz.finacial.interest.vo.FcFinacialAccbookVO;
import com.bytter.biz.finacial.interest.vo.InterestInfoContainer;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.dto.CmsPrecativeLoanDeferredDTO;
import com.bytter.finance.enums.LoanMessageEnum;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.dto.FcAccDTO;
import com.google.common.collect.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
/**
 * <br>
 * <b>类功能：</b>委托贷款利息适配器<br>
 * <b>作者：</b>caozx<br>
 * <b>日期：</b> 2017年8月29日 <br>
 * <b>版权所有：<b>版权所有(C) 2017，拜特科技<br>
 */
@Transactional
@Service("interest_EntrustInterestProvider")
public class EntrustInterestProvider extends BaseInterestProvider implements Serializable {

    @DubboReference(check = false)
    private IConfigService iConfigService;

	/**
	 * 计算利息
	 */
	public AccInterestInfo calcInterest(AccInterestInfo accInterestInfo) {
		sysRateType = accInterestInfo.getContainer().getSysRateType();//委托贷款只计息不预提
		accInterestInfo.setInterestDtls(processInterestDtl(
                accInterestInfo.getInterestAccuBalances().get(InterestProcessType.INTEREST),
                accInterestInfo.getContainer()));
		return accInterestInfo;
	}
	
	/**
	 * <b>功能介绍：</b>处理利息明细<br>
	 * <b>日期：</b>2017年8月25日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param accAccuBalances
	 * @param interestContainer
	 * @return
	 */
	private List<FcInterestDtlTemp> processInterestDtl(Map<String, List<FcAccuBalancef>> accAccuBalances,
            InterestInfoContainer interestContainer) {
		List<FcInterestDtlTemp> interestDtls = new ArrayList<FcInterestDtlTemp>();
        Calendar endInterestCalendar = Calendar.getInstance();//止息日期
        endInterestCalendar.setTime(interestContainer.getEndInstDate());
        endInterestCalendar.set(Calendar.HOUR, 0);
        endInterestCalendar.set(Calendar.MINUTE, 0);
        endInterestCalendar.set(Calendar.SECOND, 0);
        if (null != accAccuBalances) {
            Map<String, List<CmsPrecativeLoanDeferredDTO>> deferredMap = getAccDeferredMap();//展期信息
            Map<String, InstrateAdjustBean> instrateAdjustInfos = interestContainer.getInstrateAdjustInfos();
            for (Entry<String, List<FcAccuBalancef>> entry : accAccuBalances.entrySet()) {
                String account = entry.getKey();
                Map<String, List<InstrateAccuBean>> accInstrateTypeAccuMap = new HashMap<String, List<InstrateAccuBean>>();
                AccInstrateBean rateInfo = null;
                Map<String, AccInstrateBean> accCurRates = new HashMap<String, AccInstrateBean>();
                for (FcAccuBalancef accuBalance : entry.getValue()) {
                    rateInfo = getAccInstrateInfo(accuBalance, instrateAdjustInfos, accCurRates, endInterestCalendar, deferredMap.get(account));
                    String instrtType = rateInfo.getInstrtType();
                    List<InstrateAccuBean> accInstrateTypeAccus = accInstrateTypeAccuMap.get(instrtType);
                    if (accInstrateTypeAccus == null) {
                        accInstrateTypeAccus = new ArrayList<InstrateAccuBean>();
                        accInstrateTypeAccuMap.put(instrtType, accInstrateTypeAccus);
                    }
                    accInstrateTypeAccus.add(new InstrateAccuBean(account, interestContainer.getInterestAcc().get(accuBalance.getAccount()), rateInfo
                            .getStartDate(), rateInfo.getInstrateTime(), rateInfo.getNewInstrate(), 
                            getCumuByAccuBalance(accuBalance), accuBalance.getBalanceDate()));
                }
                interestDtls.addAll(packInterestDtls(accInstrateTypeAccuMap, interestContainer));
            }
        }
        Calendar calendar = Calendar.getInstance();
        for (FcInterestDtlTemp interestDtl : interestDtls) {
            // 利息 = 积数 X 日利率(利率/利率类型)
            interestDtl.setInterest(interestDtl.getCumulation().multiply(interestDtl.getInterestRate())
                    .divide(sysRateType, 2, BigDecimal.ROUND_HALF_UP));
            calendar.setTime(interestDtl.getInterestStartDate());
            calendar.add(Calendar.DAY_OF_MONTH, interestDtl.getDays() - 1);
            interestDtl.setBlnInstDate(calendar.getTime());
        }
        return transDtlTemps(interestDtls);
	}
	
	/**
	 * <b>功能介绍：</b>获取委托贷款账号展期信息<br>
	 * <b>日期：</b>2017年8月25日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @return
	 */
	private Map<String, List<CmsPrecativeLoanDeferredDTO>> getAccDeferredMap() {
		List<CmsPrecativeLoanDeferredDTO> accDeferreds = iCmsLoanService.getCmsPrecativeDeferreds();
		Map<String, List<CmsPrecativeLoanDeferredDTO>> map = new HashMap<String, List<CmsPrecativeLoanDeferredDTO>>();
		for (CmsPrecativeLoanDeferredDTO deferred : accDeferreds) {
			//获取委托贷款展期对应的贷款账号
            String loanAcc = iCmsLoanService.getCmsPrecativeLoanSendByBillCode(deferred.getLiCode()).getLoanAcc();
            List<CmsPrecativeLoanDeferredDTO> deferreds = map.get(loanAcc);//委托贷款展期信息
            if (null == deferreds) {
                deferreds = new ArrayList<CmsPrecativeLoanDeferredDTO>();
                map.put(loanAcc, deferreds);
            }
            deferreds.add(deferred);
        }
		for (Entry<String, List<CmsPrecativeLoanDeferredDTO>> entry : map.entrySet()) {
            List<CmsPrecativeLoanDeferredDTO> list = entry.getValue();
            Collections.sort(list, new Comparator<CmsPrecativeLoanDeferredDTO>() {
                public int compare(CmsPrecativeLoanDeferredDTO o1, CmsPrecativeLoanDeferredDTO o2) {
                    return o1.getCreateDate().compareTo(o2.getCreateDate());
                }
            });
        }
        return map;
	}
	
	/**
	 * <b>功能介绍：</b>获取账户利率信息<br>
	 * <b>日期：</b>2017年8月25日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param accuBalance
	 * @param instrateAdjustInfos
	 * @param accCurRates
	 * @param endInterestCalendar
	 * @param deferreds 展期信息(取展期申请的展期开始日期和展期利率)
	 * @return
	 */
	private AccInstrateBean getAccInstrateInfo(FcAccuBalancef accuBalance,
            Map<String, InstrateAdjustBean> instrateAdjustInfos, Map<String, AccInstrateBean> accCurRates,
            Calendar endInterestCalendar, List<CmsPrecativeLoanDeferredDTO> deferreds) {
		String account = accuBalance.getAccount();
		FcAccDTO acc = iFcAccService.getAccDTOByAccount(account);
		AccInstrateBean rateInfo = null;
        Calendar deadlineDate = Calendar.getInstance();
        deadlineDate.setTime(new Date(acc.getDeadlineDate().getTime()));//到期日期
        deadlineDate.set(Calendar.HOUR, 0);
        deadlineDate.set(Calendar.MINUTE, 0);
        deadlineDate.set(Calendar.SECOND, 0);
        Calendar balanceCalendar = Calendar.getInstance();
        balanceCalendar.setTime(accuBalance.getBalanceDate());//到期日期
        balanceCalendar.set(Calendar.HOUR, 0);
        balanceCalendar.set(Calendar.MINUTE, 0);
        balanceCalendar.set(Calendar.SECOND, 0);
        String instrtType = "";
        BigDecimal interestRate = new BigDecimal(0);
        if (balanceCalendar.compareTo(deadlineDate) >= 0) {//已到期
            //是否展期
            if (StringUtils.isNotEmpty(String.valueOf(acc.getDelayRate())) && null != acc.getDelayDeadlineDate()) {//申请展期
                Object[] obj = setAccPreDeferredInfo(accuBalance.getBalanceDate(), acc, deferreds);
                instrtType = (String) obj[0];
                interestRate = (BigDecimal) obj[1];
            } else { //未申请展期（正常、逾期）
                instrtType = InterestConstant.INSTRT_TYPE_OVERDUE;
                interestRate = acc.getOverdueRate();
            }
        } else {//未到期，按贷款利率计算利息
            instrtType = InterestConstant.INSTRT_TYPE_NORMAL;
            interestRate = acc.getInterestRate();
        }
        InstrateAdjustBean instrateAdjustBean = instrateAdjustInfos.get(account);
        rateInfo = packRateInfo(instrateAdjustBean, accCurRates, interestRate, instrtType, accuBalance);
        return rateInfo;
	}
	
	
	/**
	 * <b>功能介绍：</b>获取展期利率<br>
	 * <b>日期：</b>2017年8月25日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param balanceDate
	 * @param fcAcc
	 * @param deferreds
	 * @return
	 */
	private Object[] setAccPreDeferredInfo(Date balanceDate, FcAccDTO fcAcc, List<CmsPrecativeLoanDeferredDTO> deferreds) {
        String instrtType = "";
        BigDecimal interestRate = new BigDecimal(0);
        
        if (null != deferreds) {
            for (CmsPrecativeLoanDeferredDTO deferred : deferreds) {
                Date startDate = deferred.getDeferredStartDate();//展期开始日
                if (null == startDate) {//历史数据未写展期开始日，以账户到期日为准
                    startDate = fcAcc.getDeadlineDate();//账户到期日
                }
                
                Date endDate = deferred.getDeferredRepayDate();//展期到期日
                if (balanceDate.compareTo(startDate) < 0) {
                    instrtType = InterestConstant.INSTRT_TYPE_OVERDUE;//逾期利率
                    interestRate = fcAcc.getOverdueRate();
                } else {
                    if (balanceDate.compareTo(endDate) < 0) {
                        instrtType = InterestConstant.INSTRT_TYPE_DELAY;//展期利率
                        interestRate = deferred.getDeferredRate();//展期利率
                    }
                }
            }
            if (StringUtils.isEmpty(instrtType)) {
                instrtType = InterestConstant.INSTRT_TYPE_OVERDUE;//逾期利率
                interestRate = fcAcc.getOverdueRate();
            }
        } else {
            instrtType = InterestConstant.INSTRT_TYPE_OVERDUE;//逾期利率
            interestRate = fcAcc.getOverdueRate();//逾期利率
        }
        return new Object[]{instrtType, interestRate};
    }
	
	
	public void processInterestVoucher(AccInterestInfo accInterestInfo) {
//        setOperatorCode(accInterestInfo.getContainer().getUserBean().getLoginName());
        final List<FcInterestDtlTemp> interestDtls = accInterestInfo.getInterestDtls();
        if (null == interestDtls || interestDtls.size() == 0) {
            return;
        }
        String netCode = accInterestInfo.getAccTypeKeyBean().getnetId();
        String curCode = accInterestInfo.getAccTypeKeyBean().getcurId();
        String commonSummary = "委托贷款结息";
        List<Map<String, Object>> list = createInterestVoucher(interestDtls, accInterestInfo, commonSummary, netCode, curCode);
        saveInterestVoucher(accInterestInfo, list);
	}
	
	/**
	 * <b>功能介绍：</b>保存利息凭证<br>
	 * <b>日期：</b>2017年8月29日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param accInterestInfo
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public void saveInterestVoucher(AccInterestInfo accInterestInfo, List<Map<String, Object>> list) {
		//凭证封装对象
		for (Map<String, Object> map : list) {
		    VoucherWrapper voucher = new VoucherWrapper();
			List<FcAccbookVoucherVO> target = new ArrayList<FcAccbookVoucherVO>();
			
			List<FcInterestDtlTemp> interestDtls = (List<FcInterestDtlTemp>) map.get("interestDtls");
			FcFinacialAccbookVO accbookVO = (FcFinacialAccbookVO) map.get("accbook");
            List<FcAccbookDtl> accbookDtls = BeanUtils.copyListProperties((List<FcFinacialAccbookDtlVO>) map.get("accbookDtls"), FcAccbookDtl.class);
            //凭证对象
            FcAccbookVoucherVO voucherVo = new FcAccbookVoucherVO();
            FcAccbook accbook = new FcAccbook();
            BeanUtils.copyProperties(accbookVO, accbook);
            voucherVo.setAccbook(accbook);
            voucherVo.setBookDtls(accbookDtls);
            target.add(voucherVo);
            voucher.setUserCode(accInterestInfo.getContainer().getUserBean().getId());
            voucher.setCheckUser(accInterestInfo.getContainer().getUserBean().getId());
    		voucher.setTarget(target);
    		voucher.setOriType(FinaBookConstant.BusOriType.IST_INTEREST_BOOK.get());
    		voucher.setAutoCheck(true);//自动复核
            voucher.setCheckUser(accInterestInfo.getContainer().getUserBean().getId());
            fcAccbookService.submitFcAccbook(voucher);
            String orderNo = voucher.getTarget().get(0).getAccbook().getOrderNo();
            String voucherNo=voucher.getTarget().get(0).getAccbook().getVoucherNo();
            List<FcInterestDtl> fcInterestDtls = new ArrayList<>();
            for (FcInterestDtlTemp interetDtl : interestDtls) {//利息明细设置凭证流水号
                //FcInterestDtl istDtl = new FcInterestDtl();
                interetDtl.setOrderNo(orderNo);
                //istDtl.setVoucherNo(voucherNo);
                //BeanUtils.copyProperties(interetDtl, istDtl);
                //fcInterestDtls.add(istDtl);
            }
            //fcInterestDtlService.batchSave(fcInterestDtls);
		}
	}
	
	/**
	 * <b>功能介绍：</b>构建批量结息凭证<br>
	 * <b>日期：</b>2017年8月29日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param interestDtls
	 * @param accInterestInfo
	 * @param commonSummary
	 * @param netCode
	 * @param curCode
	 * @return
	 */
	private List<Map<String, Object>> createInterestVoucher(List<FcInterestDtlTemp> interestDtls, AccInterestInfo accInterestInfo, String commonSummary, String netCode,
            String curCode) {
		Date tallyDate = accInterestInfo.getContainer().getTallyDate();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String opUserId = interestDtls.get(0).getOperatorCode();
        Map<String, List<FcInterestDtlTemp>> interestMap = new HashMap<String, List<FcInterestDtlTemp>>();
        for (FcInterestDtlTemp interestDtl : interestDtls) {
                interestDtl.setRepayMoney(interestDtl.getInterest());
            List<FcInterestDtlTemp> interestDtlsTemp = interestMap.get(interestDtl.getAccount());
            if (null == interestDtlsTemp) {
                interestDtlsTemp = new ArrayList<FcInterestDtlTemp>();
                interestMap.put(interestDtl.getAccount(), interestDtlsTemp);
            }
            interestDtlsTemp.add(interestDtl);
        }
        for (Entry<String, List<FcInterestDtlTemp>> entry : interestMap.entrySet()) {
            String corpCode = "";//贷款方单位
            String PrecationCorp = "";//委托方单位
            List<FcInterestDtlTemp> dtls = entry.getValue();
            BigDecimal interest = BigDecimal.ZERO;
            if (!dtls.isEmpty()) {
                for (FcInterestDtlTemp interestDtl : dtls) {
                    interest = interest.add(interestDtl.getInterest());
                    if(EmptyUtils.isEmpty(accInterestInfo.getContainer().getInterestAcc())){
                    	corpCode = iFcAccService.getAccDTOByAccount(interestDtl.getAccount()).getCorpId();
                    	PrecationCorp = iFcAccService.getAccDTOByAccount(interestDtl.getAccount()).getPrecationCorp();
                    }else{
                    	corpCode = accInterestInfo.getContainer().getInterestAcc().get(interestDtl.getAccount()).getCorpId();//贷款方单位
                    	PrecationCorp = accInterestInfo.getContainer().getInterestAcc().get(interestDtl.getAccount()).getPrecationCorp();//委托方
                    }
                }
            }
            if (interest.compareTo(BigDecimal.ZERO) > 0) {
                Map<String, Object> tempMap = createInnerModelVoucher(tallyDate, commonSummary, netCode,
                        curCode, interest, corpCode, PrecationCorp,/*accInterestInfo.getContainer().getUserBean().getId()*/"", opUserId);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("accbook", tempMap.get("accbook"));
                map.put("accbookDtls", tempMap.get("accbookDtls"));
                map.put("interestDtls", dtls);
                list.add(map);
            }
        }
        return list;
    }
	
	/**
	 * <b>功能介绍：</b>表内核算凭证构建<br>
	 * <b>日期：</b>2017年8月29日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param tallyDate
	 * @param commonSummary
	 * @param netCode
	 * @param curCode
	 * @param money
	 * @param debitCorpCode
	 * @param creditCorpCode
	 * @return
	 */
	private Map<String, Object> createInnerModelVoucher(Date tallyDate, String commonSummary, String netCode,
            String curCode, BigDecimal money, String debitCorpCode, String creditCorpCode,String userId, String opUserId) {
		 List<FcFinacialAccbookDtlVO> accbookDtls = new ArrayList<FcFinacialAccbookDtlVO>();
		 FcFinacialAccbookVO accBook = initAccbook(tallyDate,false,netCode,userId, opUserId,false, null);//初始化凭证
		 FcVoucherTypeDTO voucherType = getVoucherBySysVoucherCode();
	     FcAccDTO debitAcc = findCurrentInterestAcc(curCode,netCode,debitCorpCode);//借方企业活期
	     FcAccDTO creditAcc = findCurrentInterestAcc(curCode,netCode,creditCorpCode);//贷方企业活期
	     FcFinacialAccbookDtlVO debitDtl = new FcFinacialAccbookDtlVO();
	     BeanUtils.copyProperties(createAccbookDtl(debitAcc, commonSummary, money,BigDecimal.ZERO, voucherType,opUserId), debitDtl);
	     accbookDtls.add(debitDtl);
	     FcFinacialAccbookDtlVO creditDtl = new FcFinacialAccbookDtlVO();
	     BeanUtils.copyProperties(createAccbookDtl(creditAcc, commonSummary, BigDecimal.ZERO,money, voucherType,opUserId),creditDtl);
	     accbookDtls.add(creditDtl);
	     Map<String, Object> map = new HashMap<String, Object>();
	     List<FcFinacialAccbookDtlVO> dtlVO = BeanUtils.copyListProperties(accbookDtls, FcFinacialAccbookDtlVO.class);
	     accBook.setFcAccbookDtls(dtlVO);
         if (StringUtils.isNotBlank(userId)){
             accBook.setOperatorCode(userId);
         }else {
             accBook.setOperatorCode(opUserId);
         }

	     map.put("accbook", accBook);
	     map.put("accbookDtls", accbookDtls);
		 return map;
	}
	/**
	 * <b>功能介绍：</b>企业活期存款账户<br>
	 * <b>日期：</b>2017年8月29日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 * @param curCode
	 * @param netCode
	 * @param corpCode
	 * @return
	 */
	private FcAccDTO findCurrentInterestAcc(String curCode,String netCode,String corpCode) {
		//企业活期科目ID
	     String subjectId_01 = iFcSubjectService.findSubjectByCode(SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.get()).getId();
	     List<FcAccDTO> list = innerAccountService.listAccountByParams(curCode, netCode, corpCode,subjectId_01);
        Map<String, String> cropNameMap = iCorpService.getNameByIds(Lists.newArrayList(corpCode, netCode));
	     if(EmptyUtils.isEmptyList(list)){//未维护企业放款账号
	    	logger.debug("对应网点单位企业活期账号不存在");
             throw new ParameterException(LoanMessageEnum.CMS_ACCBOOK_2120_NOTEXISTS, cropNameMap.get(netCode), cropNameMap.get(corpCode));
		 }
	     return list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public void createInterestVoucher(AccInterestInfo accInterestInfo) {
//        setOperatorCode(accInterestInfo.getContainer().getUserBean().getLoginName());
        final List<FcInterestDtlTemp> interestDtls = accInterestInfo.getInterestDtls();
        if(EmptyUtils.isEmptyList(interestDtls)){
        	return;
        }
        String netCode = accInterestInfo.getAccTypeKeyBean().getnetId();
        String curCode = accInterestInfo.getAccTypeKeyBean().getcurId();
        String commonSummary = "委托贷款结息";
        List<Map<String, Object>> list = createInterestVoucher(interestDtls, accInterestInfo, commonSummary, netCode, curCode);
        List<FcFinacialAccbookVO> accbooks = new ArrayList<FcFinacialAccbookVO>();
        for (int i = 0, size = list.size(); i < size; i++) {
            Map<String, Object> map = list.get(i);
            FcFinacialAccbookVO accbookVO = (FcFinacialAccbookVO) map.get("accbook");
            accbookVO.setInterestDtls((List<FcInterestDtlTemp>) map.get("interestDtls"));
            accbooks.add(accbookVO);
        }
        accInterestInfo.setInterestAccbook(accbooks); 
	}
	
}
