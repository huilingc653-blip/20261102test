package com.bytter.increment.domain.service.impl;

import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.DepositReportConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.api.constant.BankFinanceConstant;
import com.bytter.increment.api.dto.ChoBankFinanceDTO;
import com.bytter.increment.api.dto.ChoProductRedeemDTO;
import com.bytter.increment.api.dto.DepositReportCommonDTO;
import com.bytter.increment.api.service.IChoBankFinanceService;
import com.bytter.increment.domain.entity.ChoBankFinance;
import com.bytter.increment.domain.entity.ChoProductRedeem;
import com.bytter.increment.domain.service.ChoBankFinanceService;
import com.bytter.increment.domain.service.ChoProductRedeemService;
import com.bytter.increment.domain.vo.ChoBankFinanceVO;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: CHO_BANK_FINANCE
 * @date 2017-08-18 14:03:30
 */
@Service("choBankFinanceServiceImpl")
@Transactional
@DubboService
public class ChoBankFinanceServiceImpl implements IChoBankFinanceService {
    @Autowired
    private ChoBankFinanceService choBankFinanceService;
    @Autowired
    private ChoProductRedeemService choProductRedeemService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IFcExcrateAdjustService fcExcrateAdjustService;

    /**
     * 获取银行存款信息
     *
     * @param user     用户
     * @param resource 资源
     * @param busType  10-活期转定期（定期存款） 12-活期转通知（通知存款）
     * @return
     */
    @Override
    public DepositReportCommonDTO getBankFinanceBalByUser(ICurrentUser user, String resource, String busType, String corpId) {
        DepositReportCommonDTO depositReport = new DepositReportCommonDTO();
        if (BankFinanceConstant.BUS_TYPE_C2T.toString().equals(busType)) {
            //定期存款
            depositReport.setBusType(DepositReportConstant.BUSTYPE_1);
        } else if (BankFinanceConstant.BUS_TYPE_C2N.toString().equals(busType)) {
            //通知存款
            depositReport.setBusType(DepositReportConstant.BUSTYPE_2);
        }
        // 当前余额
        BigDecimal curBalance = this.getDepositBalance(user, resource, busType, DateUtils.getDateTime(), corpId);
        depositReport.setBalance(curBalance);
        // 上月末余额
        Date preMonthDate = DateUtils.getLastMonthDate(DateUtils.addMonth(DateUtils.getDate(), -1));
        BigDecimal preMonthBalance = this.getDepositBalance(user, resource, busType, preMonthDate, corpId);
        // 环比增长率=（本期数-上期数）/上期数 × 100 %
        BigDecimal monthOnMonth = NumberUtil.calculateMonthOnMonth(curBalance, preMonthBalance);
        depositReport.setMonthOnMonth(monthOnMonth);
        return depositReport;
    }
    /**
     * 根据日期获取存款余额
     *
     * @param user
     * @param resource
     * @param busType
     * @param searchDate
     * @return
     */
    private BigDecimal getDepositBalance(ICurrentUser user, String resource, String busType, Date searchDate, String corpId) {
        //获取当前用户所属单位
        CorpDTO corp = this.corpService.get(corpId);
        //获取存款数据
        List<ChoBankFinanceVO> listBankFinance = this.choBankFinanceService.listByBusType(user, resource, busType, searchDate, null);
        //余额
        BigDecimal balance = BigDecimal.ZERO;
        for (ChoBankFinanceVO vo : listBankFinance) {
            //当前单据的存款余额=存款金额-已取款金额
            BigDecimal curBal = vo.getDrawMoney() == null ? vo.getPayMoney() : (vo.getPayMoney().subtract(vo.getDrawMoney()));
            //判断存款币种与用户所属单位的本位币是否一致，不同则需要折算成单位本位币
            if (corp.getCurId().equals(vo.getCurId())) {
                balance = balance.add(curBal);
            } else {
                BigDecimal exChangeRate = this.fcExcrateAdjustService.getExchangeRateBy(vo.getCurId(), corp.getCurId(), searchDate, FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
                balance = balance.add(curBal.multiply(exChangeRate));
            }
        }
        return balance;
    }
    /**
     * 获取七天到期存款
     *
     * @param user     用户
     * @param resource 资源
     * @return
     */
    @Override
    public BigDecimal getSevenDayDeposit(ICurrentUser user, String resource, String corpId) {
        //获取当前用户所属单位
        CorpDTO corp = this.corpService.get(corpId);
        //获取定期存款数据
        Date deadlineDate = DateUtils.addDay(DateUtils.getDate(), 7);
        List<ChoBankFinanceVO> listBankFinance = this.choBankFinanceService.listByBusType(user, resource, BankFinanceConstant.BUS_TYPE_C2N.toString(), DateUtils.getDateTime(), deadlineDate);
        //余额
        BigDecimal balance = BigDecimal.ZERO;
        if (CollectionUtils.isEmpty(listBankFinance)) {
            return balance;
        }
        for (ChoBankFinanceVO vo : listBankFinance) {
            //当前单据的存款余额=存款金额-已取款金额
            BigDecimal curBal = vo.getDrawMoney() == null ? vo.getPayMoney() : (vo.getPayMoney().subtract(vo.getDrawMoney()));
            //判断存款币种与用户所属单位的本位币是否一致，不同则需要折算成单位本位币
            if (corp.getCurId().equals(vo.getCurId())) {
                balance = balance.add(curBal);
            } else {
                BigDecimal exChangeRate = this.fcExcrateAdjustService.getExchangeRateByNoCheck(vo.getCurId(), corp.getCurId(), DateUtils.getDateTime(), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
                if (null == exChangeRate) {
                    exChangeRate = BigDecimal.ONE;
                }
                balance = balance.add(curBal.multiply(exChangeRate));
            }
        }
        return balance;
    }
    @Override
    public void collectionConfirmBankFinance(String confirm, String businessId) {
        if (StringUtils.isBlank(businessId)) {
            return;
        }
        ChoBankFinance choBankFinance = choBankFinanceService.get(businessId, false);
        if (CommonConstant.YES.equals(confirm)) {
            choBankFinance.setCollectionConfirm(CommonConstant.YES);
        } else {
            choBankFinance.setCollectionConfirm(CommonConstant.NO);
        }
        choBankFinanceService.updateEntity(choBankFinance);
    }
    @Override
    public void collectionConfirmProductRedeem(String confirm, String businessId) {
        if (StringUtils.isBlank(businessId)) {
            return;
        }
        ChoProductRedeem choProductRedeem = choProductRedeemService.get(businessId, false);
        if (CommonConstant.YES.equals(confirm)) {
            choProductRedeem.setCollectionConfirm(CommonConstant.YES);
        } else {
            choProductRedeem.setCollectionConfirm(CommonConstant.NO);
        }
        choProductRedeemService.update(choProductRedeem);
    }
    @Override
    public ChoProductRedeemDTO getProductRedeemEntity(String id) {
        if(StringUtils.isNotBlank(id)){
            ChoProductRedeem choProductRedeem = choProductRedeemService.get(id);
            return BeanUtils.copyNewInstanceProperties(choProductRedeem,ChoProductRedeemDTO.class);
        }
        return null;
    }
    @Override
    public ChoBankFinanceDTO getBankFinanceEntity(String id) {
        if(StringUtils.isNotBlank(id)){
            ChoBankFinance choBankFinance = choBankFinanceService.get(id);
            return BeanUtils.copyNewInstanceProperties(choBankFinance,ChoBankFinanceDTO.class);
        }
        return null;
    }
	@Override
	public ChoBankFinanceDTO getBankFinanceByVoucherNo(String voucherNo) {
		ChoBankFinance finance = choBankFinanceService.getBankFinanceByVoucherNo(voucherNo);
		ChoBankFinanceDTO dto = new ChoBankFinanceDTO();
		BeanUtils.copyNotNullProperties(finance, dto);
		return dto;
	}
	@Override
	public int updateBankFinanceByVoucherNo(ChoBankFinanceDTO choBankFinanceDTO) {
		try {
			ChoBankFinance entity = new ChoBankFinance();
			BeanUtils.copyNotNullProperties(choBankFinanceDTO, entity);
			choBankFinanceService.update(entity);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}
}
