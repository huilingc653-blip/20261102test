package com.bytter.biz.finacial.interest.service.impl;
import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.biz.finacial.api.dto.FcFinacialAccbookDTO;
import com.bytter.biz.finacial.api.dto.FcInterestDtlDTO;
import com.bytter.biz.finacial.api.enums.interest.InterestMessageEnum;
import com.bytter.biz.finacial.api.service.IFcInterestService;
import com.bytter.biz.finacial.interest.dao.FcInterestDtlDao;
import com.bytter.biz.finacial.interest.entity.FcInterestDtl;
import com.bytter.biz.finacial.interest.service.FcEntrustLoanRepayService;
import com.bytter.biz.finacial.interest.service.FcInterestDtlService;
import com.bytter.biz.finacial.interest.service.FcInterestService;
import com.bytter.biz.finacial.interest.service.proccessor.PredrawVoucherProvider;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
@Transactional
@Service("fcInterestServiceImpl")
@DubboService
public class FcInterestServiceImpl implements IFcInterestService, Serializable {
    @Autowired
    private FcEntrustLoanRepayService repayService;
    @Autowired
    private FcInterestDtlService fcInterestDtlService;
    @Autowired
    private FcInterestService interestService;
    @Autowired
    private PredrawVoucherProvider predrawVoucherProvider;
    @DubboReference(check = false)
	private IFcInterestService fcInterestService;
    @Autowired
    private FcInterestDtlDao fcInterestDtlDao;
    @Override
    public BigDecimal calTellerMoneyInterest(String account, BigDecimal money, String orderNo, Date endInstDate, ICurrentUser user) {
        return repayService.calTellerMoneyInterest(account, money, orderNo, endInstDate, user);
    }
    @Override
    public List<FcInterestDtlDTO> getTellerMoneyInterests(String account, BigDecimal money, String orderNo,
                                                          Date endInstDate, ICurrentUser user) {
        return repayService.getTellerMoneyInterests(account, money, orderNo, endInstDate, user);
    }
    @Override
    public void processTellerMoneyBalance(String account, BigDecimal money, String orderNo, ICurrentUser user,
                                          String depositMangerId, Date endInstDate) {
        repayService.processAccuBalances(account, money, orderNo, user, depositMangerId, endInstDate);
    }
    @Override
    public void deleteAccInterest(String account, String orderNo, String depositMangerId) {
        repayService.deleteTellerMoneyInterest(account, orderNo, depositMangerId, false);
    }
    @Override
    public List<FcInterestDtlDTO> getFcInterestDtlDTOByVoucherNo(String account, String vourcherNo) {
        return fcInterestDtlService.getFcInterestDtlDTOByVourcherNo(account, vourcherNo);
    }
    @Override
    public List<FcInterestDtlDTO> getFcInterestDtlDTOByOrderNo(String account, String orderNo, String eReceiptNo) {
        return fcInterestDtlService.getFcInterestDtlDTOByOrderNo(account, orderNo, eReceiptNo);
    }
    @Override
    public BigDecimal getRushPreDrawMoney(String account, BigDecimal tellerMoney, String depositManagerId) {
        return interestService.getRushPreDrawMoney(account, tellerMoney, depositManagerId);
    }
    public void processRushPredraw(String account, ICurrentUser user, String depositManagerId, BigDecimal tellerMoney, Date interestTime) {
        interestService.interestRushPredraw(account, user, depositManagerId, tellerMoney, interestTime);
    }
    @Override
    public boolean existUnOverInterest(String blnctrAcc) {
        return interestService.existUnOverInterest(blnctrAcc);
    }
    @Override
    public void processEReceiptNo(String account, String orderNo, String eReceiptNo) {
        interestService.processEReceiptNo(account, orderNo, eReceiptNo);
    }
    /**
     * 创建预提凭证
     */
    @Override
    public List<FcFinacialAccbookDTO> createPredrawVoucher(FcInterestDtlDTO fcInterestDtlDTO, Date tallyDate, String userId, Integer certificateWay) {
        return predrawVoucherProvider.createPredrawVoucher(fcInterestDtlDTO, tallyDate, userId, certificateWay);
    }
    @Override
    public void saveFcInterestDtl(List<FcInterestDtlDTO> fcInterestDtlDTOS, String userId) {
        List<FcInterestDtl> fcInterestDtls = new ArrayList<>();
        for (FcInterestDtlDTO fcInterestDtlDTO : fcInterestDtlDTOS) {
            if (fcInterestDtlDTO.getInterestTime() == null) {
                throw new ParameterException(InterestMessageEnum.FIN_FCINTERESTDTL_INTERESTTIME_NOTNULL);
            }
            if (StringUtils.isEmpty(fcInterestDtlDTO.getAccount())) {
                throw new ParameterException(InterestMessageEnum.FIN_FCINTERESTDTL_ACCOUNT_NOTNULL);
            }
            if (StringUtils.isEmpty(fcInterestDtlDTO.getCurCode())) {
                throw new ParameterException(InterestMessageEnum.FIN_FCINTERESTDTL_CURCODE_NOTNULL);
            }
            if (StringUtils.isEmpty(fcInterestDtlDTO.getInterestType())) {
                throw new ParameterException(InterestMessageEnum.FIN_FCINTERESTDTL_INTERESTTYPE_NOTNULL);
            }
            if (null == fcInterestDtlDTO.getInterestStartDate()) {
                throw new ParameterException(InterestMessageEnum.FIN_FCINTERESTDTL_INTERESTSTARTDATE_NOTNULL);
            }
            if (null == fcInterestDtlDTO.getBlnInstDate()) {
                throw new ParameterException(InterestMessageEnum.FIN_FCINTERESTDTL_BLNINSTDATE_NOTNULL);
            }
            if (null == fcInterestDtlDTO.getDays() && fcInterestDtlDTO.getDays() == 0) {
                throw new ParameterException(InterestMessageEnum.FIN_FCINTERESTDTL_DAYS_NOTNULL);
            }
            if (null == fcInterestDtlDTO.getCumulation()) {
                throw new ParameterException(InterestMessageEnum.FIN_FCINTERESTDTL_CUMULATION_NOTNULL);
            }
            if (null == fcInterestDtlDTO.getInterestRate()) {
                throw new ParameterException(InterestMessageEnum.FIN_FCINTERESTDTL_INTERESTRATE_NOTNULL);
            }
            if (null == fcInterestDtlDTO.getInterest()) {
                throw new ParameterException(InterestMessageEnum.FIN_FCINTERESTDTL_INTEREST_NOTNULL);
            }
            if (StringUtils.isEmpty(fcInterestDtlDTO.getInterestSign())) {
                throw new ParameterException(InterestMessageEnum.FIN_FCINTERESTDTL_INTERESTSIGN_NOTNULL);
            }
            if (StringUtils.isEmpty(fcInterestDtlDTO.getOperatorCode())) {
                throw new ParameterException(InterestMessageEnum.FIN_FCINTERESTDTL_OPERATORCODE_NOTNULL);
            }
            FcInterestDtl fcInterestDtl = new FcInterestDtl();
            BeanUtils.copyProperties(fcInterestDtlDTO, fcInterestDtl);
            Calendar time = Calendar.getInstance();
            time.get(Calendar.HOUR_OF_DAY);//获取小时
            time.get(Calendar.MINUTE);//获取分钟
            time.get(Calendar.SECOND);//获取秒
            String istDate = DateUtils.formatDate(fcInterestDtlDTO.getInterestTime());
            String hms = time.get(Calendar.HOUR_OF_DAY) + ":" + time.get(Calendar.MINUTE) + ":" + time.get(Calendar.SECOND);
            StringBuilder insterestDate = new StringBuilder(istDate);
            insterestDate.append(" " + hms);
            Date date = DateUtils.str2Date(insterestDate.toString(), DateUtils.datetimeFormat());
            fcInterestDtl.setGenerateMode(InterestConstant.GENERATEMODE_INTEREST_PART);
            fcInterestDtl.setBusStatus(InterestConstant.INTEREST_STATUE_CHECK);
            if (StringUtils.isNotEmpty(userId)) {
                fcInterestDtl.setCheckerCode(userId);
            } else {
                fcInterestDtl.setCheckerCode(fcInterestDtlDTO.getOperatorCode());
            }
            fcInterestDtl.setCheckDate(DateUtils.getDateTime());
            fcInterestDtl.setInterestTime(date);
            fcInterestDtls.add(fcInterestDtl);
        }
        interestService.saveFcInterestDtl(fcInterestDtls);
    }
    /**
     * 根据账号查询利息明细
     *
     * @param blnctrAcc 内部账号
     * @return 利息明细
     */
    @Override
    public List<FcInterestDtlDTO> getFcInterestDtlByAcc(String blnctrAcc, String isPool) {
        return fcInterestDtlService.getFcInterestDtlByAcc(blnctrAcc, isPool);
    }
}
