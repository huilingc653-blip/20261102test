package com.bytter.credential.domain.international.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.credential.api.constant.InternationalConstant;
import com.bytter.credential.api.constant.InternationalResourceConstant;
import com.bytter.credential.domain.international.dao.SupplementPaymentDao;
import com.bytter.credential.domain.international.entity.LcBillArrival;
import com.bytter.credential.domain.international.entity.LcEnrol;
import com.bytter.credential.domain.international.entity.LcPayment;
import com.bytter.credential.domain.international.entity.LcPaymentCost;
import com.bytter.credential.domain.international.utils.CreditUtil;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2018-12-20 11:11
 * @Version 1.0
 */
@Transactional
@Service("supplementPaymentService")
public class SupplementPaymentService extends BaseService<LcPayment, String> {
    @Autowired
    private SupplementPaymentDao supplementPaymentDao;
    @DubboReference(check = false)
	private ICorpService corpService;
    @Autowired
    private ArrivalLetterService arrivalLetterService;
    @Autowired
    private CreatedCreditService creditService;
    @DubboReference(check = false)
	private IRatUrationService urationService;
    @DubboReference(check = false)
	private IBtCurrencyService currencyService;
    @DubboReference(check = false)
    private IFcBondHookService bondHookService;
    @Autowired
    private LcDepositService lcDepositService;
    /**
     * 付汇申请审批完成查询
     * @param dataTablesInput
     * @return
     */
    public DataTablesOutput listPaymentApproved(DataTablesInput dataTablesInput, ICurrentUser user){
        List<String> corpIds = corpService.getUserCorpIds(InternationalResourceConstant.INTERNATIONAL_CREDIT_SUPPLEMENT_PAYMENT_QUERY, user.getId());
        return supplementPaymentDao.listPaymentApproved(dataTablesInput,corpIds);
    }
    /**
     * 批量保存费用记录
     * @param feeTypes
     * @param feeMoneys
     * @param user
     * @param paymentId
     */
    public void savePaymentCosts(List<String> feeTypes, List<Double> feeMoneys, ICurrentUser user, String paymentId) {
        List<LcPaymentCost> paymentCosts = new ArrayList<>();
        if (feeTypes != null && feeMoneys != null && feeTypes.size() > 0 && feeMoneys.size() > 0) {
            for (int i = 0; i < feeTypes.size(); i++) {
                LcPaymentCost paymentCost = new LcPaymentCost();
                paymentCost.setFeeType(feeTypes.get(i));
                paymentCost.setFeeMoney(BigDecimal.valueOf(feeMoneys.get(i)));
                paymentCost.setPaymentId(paymentId);
                paymentCost.setCreateBy(user.getLoginName());
                paymentCost.setCreateDate(new Date());
                paymentCosts.add(paymentCost);
            }
        }
        supplementPaymentDao.deletePaymentCosts(paymentId);
        /*采用全删全建的方式更新数据*/
        if (paymentCosts.size() > 0) {
            supplementPaymentDao.batchSave(paymentCosts);
        }
    }
    /**
     * 释放授信
     * @param lcPayment
     */
    public void handleUration(LcPayment lcPayment) {
        LcBillArrival lcBillArrival = arrivalLetterService.get(lcPayment.getArrivalId());
        LcEnrol lcEnrol = creditService.get(lcBillArrival.getLcId());
        if (InternationalConstant.ISHANGCREDIT_Y.equals(lcEnrol.getIsHangCredit())) {
            RatUrationDTO ratUrationDTO = new RatUrationDTO();
            BtCurrencyDTO nativeCurrencyDTO= currencyService.getNatualCurrency();
            if (!nativeCurrencyDTO.getId().equals(lcEnrol.getIssuingCurId())) {
                    //接口会自动折算汇率
                CreditUtil.setLCCreditRatUrationDTO(lcEnrol, ratUrationDTO, lcPayment.getPaymentMoney(), lcPayment.getIsAllRelease());
            } else {
//                本位币释放授信
                CreditUtil.setLCCreditRatUrationDTO(lcEnrol, ratUrationDTO, lcPayment.getPaymentMoney(), lcPayment.getIsAllRelease());
            }
            urationService.releaseRation(ratUrationDTO);
        }
    }
    /**
     * 保证金释放处理
     * @param lcPayment
     */
    public void handleBond(LcPayment lcPayment){
        LcBillArrival lcBillArrival = arrivalLetterService.get(lcPayment.getArrivalId());
        LcEnrol lcEnrol = creditService.get(lcBillArrival.getLcId());
        if (InternationalConstant.ISHANGBOND_Y.equals(lcEnrol.getIsHangBond())) {
            CreditUtil.handleReleaseBond(lcEnrol,this,lcDepositService,bondHookService);
        }
    }
    /**
     * 通过信用证ID获取该信用证下所有付汇汇金额
     * @param lcEnrol
     * @return
     */
    public BigDecimal getAllPaymentMoneyByLcId(LcEnrol lcEnrol){
        Map<String, Object> params = new HashMap<>();
        params.put("lcId", lcEnrol.getId());
        params.put("status",InternationalConstant.BUSINESSFINISH);
        Map result = supplementPaymentDao.getAllPaymentMoneyByLcId(params);
        return new BigDecimal(Double.valueOf(result.get("money").toString()));
    }
    /**
     * 费用纪录明细查询
     * @param params
     * @return
     */
    public List<Object> listPaymentCost(Map<String, Object> params) {
        return supplementPaymentDao.listPaymentCost(params);
    }
    @Override
    public BaseDao<LcPayment, String> getEntityDao() {
        return supplementPaymentDao;
    }
}
