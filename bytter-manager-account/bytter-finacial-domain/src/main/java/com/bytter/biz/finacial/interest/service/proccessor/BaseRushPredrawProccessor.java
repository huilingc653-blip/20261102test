package com.bytter.biz.finacial.interest.service.proccessor;
import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.biz.finacial.api.enums.interest.OperateType;
import com.bytter.biz.finacial.interest.bean.AccInterestInfo;
import com.bytter.biz.finacial.interest.bean.AccTypeKeyBean;
import com.bytter.biz.finacial.interest.bean.InterestCommonBean;
import com.bytter.biz.finacial.interest.entity.FcPredrawDtl;
import com.bytter.biz.finacial.interest.service.FcInterestService;
import com.bytter.biz.finacial.interest.utils.InterestHelper;
import com.bytter.biz.finacial.interest.vo.InterestInfoContainer;
import com.bytter.biz.finacial.interest.vo.InterestResult;
import com.bytter.biz.finacial.inneraccount.service.FcOnlineLedgerService;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.core.utils.ApplicationContextUtil;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
@Transactional
public class BaseRushPredrawProccessor implements Serializable {
	private InterestInfoContainer interestInfoContainer;
	@Autowired
	private FcOnlineLedgerService fcOnlineLedgerService;
	
    public BaseRushPredrawProccessor(InterestInfoContainer interestInfoContainer) {
        this.interestInfoContainer = interestInfoContainer;
        if(null == fcOnlineLedgerService){
        	fcOnlineLedgerService = (FcOnlineLedgerService) ApplicationContextUtil.getContext().getBean("fcOnlineLedgerService");
        }
    }
    /**
     * <b>功能介绍：</b>冲预提<br>
     * <b>日期：</b>2017年7月19日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    public InterestResult rushPredraw() {
        //冲预提
        rushPredrawn();
        saveRushDtl();
        return null;
    }
    /**
     * <b>功能介绍：</b>处理冲预提凭证<br>
     * <b>日期：</b>2017年7月19日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    private void rushPredrawn() {
        if (OperateType.BACK != interestInfoContainer.getOpType()) {
            for (Entry<AccTypeKeyBean, AccInterestInfo> entry : interestInfoContainer.getAccTypeInterestInfos()
                    .entrySet()) {
                InterestHelper.getInterestProvider(entry.getKey().getaccType()).rushPredrawn(entry.getValue());
            }
        }
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void saveRushDtl() {
        //外部接口调用不需更新暂存和打回的预提信息
        if (!interestInfoContainer.isUpdateOriPredraw()) {
            return;
        }
        /**
         * 处理银行借款账号
         * 如果银行借款账号余额不为0，则不做其他处理，如果银行借款账号余额为0，且冲预提金额也为0,则修改银行借款还款为业务已结束
         */
        InterestCommonBean bean = new InterestCommonBean();
        bean.setAccTypeCode(InnerAccountConstant.FCACC_BANK_BORROW);
        bean.setNetCode(interestInfoContainer.getNetCode());
//        bean.setUserBean(interestInfoContainer.getUserBean());
        FcInterestService fcInterestService = (FcInterestService) ApplicationContextUtil.getContext().getBean("fcInterestService");
        List<FcPredrawDtl> predrawDtl = fcInterestService.queryUnRushPredrawDtls(bean, "", "check");
        for (FcPredrawDtl istFcPredrawDtl : predrawDtl) {
        	String account = istFcPredrawDtl.getAccount();
        	List accouts = new ArrayList();
        	accouts.add(account);
            List<FcOnlineLedgerDTO> ledgerDTOList = fcOnlineLedgerService.getLedgerDTOsByAccSet(accouts);
            if(!EmptyUtils.isEmptyList(ledgerDTOList)){
                FcOnlineLedgerDTO fcOnlineLedger = ledgerDTOList.get(0);
                if(fcOnlineLedger!=null && fcOnlineLedger.getOnlineBalance().compareTo(new BigDecimal(0))==0 && fcOnlineLedger.getCheckBalance().compareTo(new BigDecimal(0))==0){
                    BigDecimal rushMoney =  fcInterestService.getRushPreDrawMoney(account, null, null);
                    if(rushMoney.compareTo(BigDecimal.ZERO)==0){
                        //则银行借款还款为业务已经结束
//        			netBankConsoleService.rushRepay(account);
                        //TODO 标记银行借款还款为业务已经结束
                    }
                }
            }
        }
        for (String accTypeCode : interestInfoContainer.getAccTypeCodes()) {
            AccInterestInfo accInterestInfo = new AccInterestInfo();
            accInterestInfo.setContainer(interestInfoContainer);
            accInterestInfo.setAccTypeKeyBean(new AccTypeKeyBean(interestInfoContainer.getNetCode(), "", accTypeCode));
            InterestHelper.getInterestProvider(accTypeCode).savePredrawRushDtl(accInterestInfo);
        }
    }
}
