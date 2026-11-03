package com.bytter.biz.finacial.interest.service;

import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.biz.finacial.api.dto.FcInterestDtlDTO;
import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.biz.finacial.api.enums.interest.OperateType;
import com.bytter.biz.finacial.inneraccount.service.FcOnlineLedgerService;
import com.bytter.biz.finacial.interest.bean.AccInterestInfo;
import com.bytter.biz.finacial.interest.bean.AccTypeKeyBean;
import com.bytter.biz.finacial.interest.bean.InstrateAdjustBean;
import com.bytter.biz.finacial.interest.dao.FcEntrustLoanRepayDao;
import com.bytter.biz.finacial.interest.dao.FcInterestDtlDao;
import com.bytter.biz.finacial.interest.entity.FcAccuBalancef;
import com.bytter.biz.finacial.interest.entity.FcInterestDtl;
import com.bytter.biz.finacial.interest.entity.FcInterestDtlTemp;
import com.bytter.biz.finacial.interest.entity.FcLoanBalanceDtl;
import com.bytter.biz.finacial.interest.service.proccessor.BaseInterestProcessor;
import com.bytter.biz.finacial.interest.vo.InterestInfoContainer;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.IFcInstrateAdjustService;
import com.bytter.system.api.IInnerAccountService;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;

@Transactional
@Service("ist_FcEntrustLoanRepayService")
public class FcEntrustLoanRepayService extends BaseService<FcInterestDtl, String> implements Serializable {
    @DubboReference(check = false)
	private IFcAccService iFcAccService;
    @Autowired
    private FcAccuBalancefService fcAccuBalancefService;
    @Autowired
    private FcInterestService fcInterestService;
    @Autowired
	private FcOnlineLedgerService fcOnlineLedgerService;
    @Autowired
    private FcEntrustValidationService validationService;
    @Autowired
    private FcLoanBalanceDtlService fcLoanBalanceDtlService;
    @Autowired
    private FcEntrustLoanRepayDao loanRepayDao;
    @Autowired
    private FcInterestDtlDao fcInterestDtlDao;
    @DubboReference
    private IFcInstrateAdjustService iFcInstrateAdjustService;
    @DubboReference
    private IInnerAccountService innerAccountService;
    public FcEntrustLoanRepayDao getEntityDao() {
        return loanRepayDao;
    }
    /**
     * <b>功能介绍：</b>计算利息<br>
     * <b>日期：</b>2017年8月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param loanAcc
     * @param accuBalances
     * @param userBean
     * @param accType
     * @param endInstDate
     * @param interestTime
     * @return
     * @version 1.0
     */
    public List<FcInterestDtl> calAccInterest(String loanAcc, List<FcAccuBalancef> accuBalances,
                                              ICurrentUser userBean, String accType, Date endInstDate, Date interestTime) {
        if (null == interestTime) {
            interestTime = DateUtils.getDate();
        }
        InterestInfoContainer interestInfoContainer = new InterestInfoContainer();
        FcAccDTO accDTO = iFcAccService.getAccDTOByAccount(accuBalances.get(0).getAccount());
        interestInfoContainer.setNetCode(accDTO.getNetId());
        interestInfoContainer.setTallyDate(new Date());
        interestInfoContainer.setEndInstDate(endInstDate);
//        interestInfoContainer.setUserBean(new ShiroUser(userBean.getId(), userBean.getLoginName(), userBean.getUsername()));
        interestInfoContainer.setAccTypeCodes(new String[]{accType});
        interestInfoContainer.setAccuBalances(accuBalances);
        interestInfoContainer.setInstrateAdjustInfos(this.queryInterestAdjustsByAcc(loanAcc));
        interestInfoContainer.setSysFixInterestWay(fcInterestService.getSysFixInstWay()); //定期分段计息方式
        interestInfoContainer.setSysRateType(new BigDecimal(fcInterestService.getDivisorBySysRateType())); //系统利率类型
        interestInfoContainer.setInstPunish(false);//是否支持罚息
//        interestInfoContainer.setNotifyDraw(InterestHelper.isNotifyDraw());//通知存款取款是否提前预约
        interestInfoContainer.setOpType(OperateType.PART_INTEREST);//个别计息标识
        interestInfoContainer.setNotifyDraw(true);
		interestInfoContainer.setInterestTime(interestTime);
		new BaseInterestProcessor(interestInfoContainer, iFcAccService).calRePayMoneyInterest();
        List<FcInterestDtl> interestDtls = new ArrayList<FcInterestDtl>();
        List<FcInterestDtlTemp> interestDtlTemps = new ArrayList<FcInterestDtlTemp>();
        Map<AccTypeKeyBean, AccInterestInfo> map = interestInfoContainer.getAccTypeInterestInfos();
        for (Entry<AccTypeKeyBean, AccInterestInfo> entry : map.entrySet()) {
            interestDtlTemps.addAll(entry.getValue().getInterestDtls());
        }
        try {
            for (FcInterestDtlTemp istFcInterestDtlTemp : interestDtlTemps) {
                FcInterestDtl interestDtl = new FcInterestDtl();
                BeanUtils.copyProperties(istFcInterestDtlTemp, interestDtl);
                interestDtl.setId(null);
                interestDtl.setCurCode(accDTO.getCurId());
                interestDtl.setInterestTime(interestTime);
                interestDtls.add(interestDtl);
            }
            return interestDtls;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<FcInterestDtl>();
        }
    }
    /**
     * <b>功能介绍：</b>计算指定金额的利息<br>
     * <b>日期：</b>2017年8月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param account
     * @param money
     * @param orderNo
     * @param endInstDate
     * @return
     * @version 1.0
     */
    public BigDecimal calTellerMoneyInterest(String account, BigDecimal money, String orderNo, Date endInstDate, ICurrentUser user) {
        if (null == money) {
            money = BigDecimal.ZERO;
        }
        List<FcInterestDtlDTO> interestDtls = getTellerMoneyInterests(account, money, orderNo, endInstDate, user);
        BigDecimal interest = BigDecimal.ZERO;
        if (interestDtls == null || interestDtls.size() == 0) {
            return interest;
        }
        for (FcInterestDtlDTO interestDtl : interestDtls) {
            if (StringUtils.isNotEmpty(interestDtl.getInterest())) {
                interest = interest.add(interestDtl.getInterest());
            }
        }
        return interest.setScale(2);
    }
    /**
     * <b>功能介绍：</b>计算指定金额的利息明细<br>
     * <b>日期：</b>2017年8月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param account
     * @param money
     * @param orderNo
     * @param endInstDate
     * @return
     * @version 1.0
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<FcInterestDtlDTO> getTellerMoneyInterests(String account, BigDecimal money, String orderNo, Date endInstDate, ICurrentUser user) {
        List<FcInterestDtl> interestDtls = new ArrayList<FcInterestDtl>();
        Map<String, List> map = processAccInterestDtls(account, money, user, orderNo, null, endInstDate);
        interestDtls = map.get("interestDtls");
        if (interestDtls == null) {
            interestDtls = new ArrayList<FcInterestDtl>();
        }
        List<FcInterestDtlDTO> dtls = BeanUtils.copyListProperties(interestDtls, FcInterestDtlDTO.class);
        for (FcInterestDtlDTO interestDtl : dtls) {
            logger.info("利息明细：" + interestDtl.getAccount() + ";" + interestDtl.getInterestStartDate() + ";"
                    + interestDtl.getBlnInstDate() + "," + interestDtl.getInterestRate() + "," + interestDtl.getInterest());
        }
        return dtls;
    }
    /**
     * <b>功能介绍：</b>处理账户利息<br>
     * <b>日期：</b>2017年8月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param account      账号
     * @param money        金额
     * @param userBean     用户
     * @param orderNo      流水号(业务ID)
     * @param interestTime 计息时间
     * @param endInstDate  止息日期
     * @return
     * @version 1.0
     */
    @SuppressWarnings("rawtypes")
    public Map<String, List> processAccInterestDtls(String account, BigDecimal money, ICurrentUser userBean,
                                                    String orderNo, Date interestTime, Date endInstDate) {
        if (EmptyUtils.isEmpty(interestTime)) {
            interestTime = DateUtils.getDate();
        }
        Map<String, List> returnMap = new HashMap<String, List>();
        FcAccDTO fcc = iFcAccService.getAccDTOByAccount(account);
        if (EmptyUtils.isEmpty(fcc)) {
            return returnMap;
        }
        String accType = fcc.getAccType();
        List<FcAccuBalancef> accuBalances = null;
        String accForDeduct = account;
        List<String> accc = new ArrayList<>();
        accc.add(account);
        BigDecimal balance = new BigDecimal(0);
        try {
            if (InnerAccountConstant.FCACC_BUSINESS_BEGULAR.equals(accType) || InnerAccountConstant.FCACC_BANK_BEGULAR.equals(accType) || InnerAccountConstant.FCACC_BANK_DEPOSIT.equals(accType)) {
                accuBalances = new ArrayList<FcAccuBalancef>();
                FcAccuBalancef accuBalance = new FcAccuBalancef();
                accuBalance.setAccount(fcc.getBlnctrAcc());
                List<FcOnlineLedgerDTO> ledger = fcOnlineLedgerService.getLedgerDTOsByAccSet(accc);
                if (!EmptyUtils.isEmptyList(ledger)) {
                    balance = ledger.get(0).getCheckBalance();//用复核联机余额作为积数余额
                }
                if (EmptyUtils.isNotEmpty(orderNo)) {
                    List<FcInterestDtl> interestDtls = fcInterestService.getInterestsByOrderNo(orderNo);
                    BigDecimal tellerMoney = BigDecimal.ZERO;
                    if (interestDtls != null && interestDtls.size() > 0) {
                        FcInterestDtl interestDtl = interestDtls.get(0);
                        if (interestDtl.getDays() != 0) {
                            tellerMoney = interestDtl.getCumulation().divide(BigDecimal.valueOf(interestDtl.getDays()),
                                    2, BigDecimal.ROUND_HALF_UP);
                        }
                    }
                    balance = balance.add(tellerMoney);
                }
                accuBalance.setCumu(balance);
                //endInstDate = DateUtils.addDay(fcInterestService.getDayOverDate(fcc.getNetId()), 1);
                accuBalances.add(accuBalance);
            } else {
//				if(!EmptyUtils.isEmpty(endInstDate)){
//					endInstDate = DateUtils.addDay(endInstDate, 1);//积数包含止息日期
//				}
                if (InnerAccountConstant.FCACC_INNER_ACCOUNT.equals(accType) || InnerAccountConstant.FCACC_OFFBALANCE.equals(accType)) {
                    accForDeduct = fcc.getAccForDeduct();
                }
                accuBalances = new ArrayList<FcAccuBalancef>();
                List<FcAccuBalancef> fcAccuBalances = this.getEntityDao().queryAccuBalances(accForDeduct, endInstDate);//查询账户所有积数
                if (!EmptyUtils.isEmptyList(fcAccuBalances)) {
                    endInstDate = fcAccuBalances.get(fcAccuBalances.size() - 1).getBalanceDate();
                }
                if (EmptyUtils.isNotEmpty(orderNo)) {//获取已计息的积数
                    List<FcLoanBalanceDtl> balanceDtls = loanRepayDao.getAccuBalanceDtlsByBusinessId(orderNo);
                    if (!EmptyUtils.isEmptyList(balanceDtls)) {
                        List<FcAccuBalancef> accuBalancesTemp = new ArrayList<FcAccuBalancef>();//已计息积数
                        for (FcLoanBalanceDtl balanceDtl : balanceDtls) {
                            FcAccuBalancef balanceTemp = new FcAccuBalancef();
                            BeanUtils.copyProperties(fcAccuBalancefService.get(balanceDtl.getBalanceId(), true), balanceTemp);
                            balanceTemp.setId(null);
                            accuBalancesTemp.add(balanceTemp);
                        }
                        for (FcAccuBalancef accuBalance : accuBalancesTemp) {
                            FcAccuBalancef accuBalance2 = isInBalanceDate(accuBalance.getBalanceDate(),//已计息日期的未计息积数
                                    fcAccuBalances);
                            FcAccuBalancef balanceTemp = new FcAccuBalancef();
                            if (accuBalance2 != null) {
                                BeanUtils.copyProperties(accuBalance2, balanceTemp);
                                balanceTemp.setCumu(accuBalance2.getCumu().add(accuBalance.getCumu()));
                                //合并计算积数
                                if (accuBalance2.getAdjustCumu() != null) {
                                    if (accuBalance.getAdjustCumu() != null) {
                                        balanceTemp.setAdjustCumu(accuBalance2.getAdjustCumu().add(accuBalance.getAdjustCumu()));
                                    }
                                } else {
                                    balanceTemp.setAdjustCumu(accuBalance.getAdjustCumu());
                                }
                                balanceTemp.setId(null);
                                accuBalance2.setCalSign("1");//accuBalance2合并已计息积数
                            } else {
                                BeanUtils.copyProperties(accuBalance, balanceTemp);
                                balanceTemp.setId(null);
                            }
                            accuBalances.add(balanceTemp);//accuBalances合并之后的积数
                        }
                        for (FcAccuBalancef accuBalance : fcAccuBalances) {//循环所有积数
                            boolean flag = true;
                            for (FcLoanBalanceDtl balanceDtl : balanceDtls) {//已计息积数
                                if (accuBalance.getBalanceDate()
                                        .compareTo(fcAccuBalancefService.get(balanceDtl.getBalanceId(), true).getBalanceDate()) == 0) {
                                    flag = false;
                                    break;
                                }
                            }
                            if (flag && "0".equals(accuBalance.getCalSign())) {
                                FcAccuBalancef balanceTemp = new FcAccuBalancef();
                                BeanUtils.copyProperties(accuBalance, balanceTemp);
                                balanceTemp.setId(null);
                                accuBalances.add(balanceTemp);//没有计息的积数
                            }
                        }
                    } else {
                        accuBalances = fcAccuBalances;
                    }
                } else {
                    accuBalances = fcAccuBalances;
                }
                if (EmptyUtils.isEmptyList(accuBalances)) {
                    return returnMap;
                }
            }
            List<FcInterestDtl> interestDtls = null;
            if (money != null && money.compareTo(BigDecimal.ZERO) > 0) {
                //2、对积数进行拆分
                List<FcAccuBalancef> repayAccuBalances = new ArrayList<FcAccuBalancef>();//归还积数
                List<FcAccuBalancef> unPayAccuBalances = new ArrayList<FcAccuBalancef>();//剩余未归还积数
                FcAccuBalancef rePayAccuBalance = new FcAccuBalancef();
                FcAccuBalancef unPayAccuBalance = new FcAccuBalancef();
                for (FcAccuBalancef istFcAccuBalance : accuBalances) {
                    rePayAccuBalance = new FcAccuBalancef();
                    unPayAccuBalance = new FcAccuBalancef();
                    if (istFcAccuBalance.getCumu().compareTo(money) >= 0) {
                        //积数等于还款金额,该积数为本次还款的积数，不需要拆分
                        BeanUtils.copyProperties(istFcAccuBalance, rePayAccuBalance);
                        rePayAccuBalance.setCumu(money);
                        rePayAccuBalance.setId(null);
                        rePayAccuBalance.setInterestSign(InterestConstant.INTEREST_SIGN_YES);
                        rePayAccuBalance.setInterestTime(DateUtils.getDate());
                        BeanUtils.copyProperties(istFcAccuBalance, unPayAccuBalance);
                        unPayAccuBalance.setCumu(istFcAccuBalance.getCumu().subtract(money));
                        //计算拆分调整积数=原调整积数*(取款金额/原积数)
                        if (istFcAccuBalance.getAdjustCumu() != null) {
                            BigDecimal aAdjustCumu = new BigDecimal(0);
                            if (istFcAccuBalance.getCumu().compareTo(new BigDecimal(0)) != 0) {
                                aAdjustCumu = (istFcAccuBalance.getAdjustCumu().multiply(money)).divide(istFcAccuBalance.getCumu(), 2);
                            } else {
                                aAdjustCumu = istFcAccuBalance.getAdjustCumu();
                            }
                            rePayAccuBalance.setAdjustCumu(aAdjustCumu);
                            unPayAccuBalance.setAdjustCumu(istFcAccuBalance.getAdjustCumu().subtract(aAdjustCumu));
                        }
                        repayAccuBalances.add(rePayAccuBalance);
                        unPayAccuBalances.add(unPayAccuBalance);
                    } else {
                        //积数小于还款金额，积数全部计算
                        BeanUtils.copyProperties(istFcAccuBalance, rePayAccuBalance);
                        rePayAccuBalance.setId(null);
                        rePayAccuBalance.setInterestSign(InterestConstant.INTEREST_SIGN_YES);
                        rePayAccuBalance.setInterestTime(DateUtils.getDate());
                        repayAccuBalances.add(rePayAccuBalance);
                        BeanUtils.copyProperties(istFcAccuBalance, unPayAccuBalance);
                        unPayAccuBalance.setCumu(BigDecimal.ZERO);
                        unPayAccuBalance.setAdjustCumu(null);
                        unPayAccuBalances.add(unPayAccuBalance);
                    }
                }
                if (EmptyUtils.isEmptyList(repayAccuBalances)) {
                    return returnMap;
                }
                if (!(InnerAccountConstant.FCACC_BUSINESS_BEGULAR.equals(accType)
                        || InnerAccountConstant.FCACC_BANK_BEGULAR.equals(accType)
                        || InnerAccountConstant.FCACC_BANK_DEPOSIT.equals(accType))) {
                    endInstDate = DateUtils.addDay(endInstDate, 1);
                }
                interestDtls = this.calAccInterest(accForDeduct, repayAccuBalances, userBean, accType, endInstDate, interestTime);
                returnMap.put("repayAccuBalances", repayAccuBalances);
                returnMap.put("unPayAccuBalances", unPayAccuBalances);
            } else {
                if (!(InnerAccountConstant.FCACC_BUSINESS_BEGULAR.equals(accType)
                        || InnerAccountConstant.FCACC_BANK_BEGULAR.equals(accType)
                        || InnerAccountConstant.FCACC_BANK_DEPOSIT.equals(accType))) {
                    endInstDate = DateUtils.addDay(endInstDate, 1);
                }
                interestDtls = this.calAccInterest(accForDeduct, accuBalances, userBean, accType, endInstDate, interestTime);
                returnMap.put("accuBalances", accuBalances);
            }
            returnMap.put("interestDtls", interestDtls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnMap;
    }
    /**
     * <b>功能介绍：</b>判断指定时间的未计息积数是否存在<br>
     * <b>日期：</b>2017年8月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param balanceDate
     * @param accuBalances
     * @return
     * @version 1.0
     */
    public FcAccuBalancef isInBalanceDate(Date balanceDate, List<FcAccuBalancef> accuBalances) {
        if (EmptyUtils.isEmptyList(accuBalances)) {
            return null;
        } else {
            for (FcAccuBalancef fcAccuBalance : accuBalances) {
                if (balanceDate.compareTo(fcAccuBalance.getBalanceDate()) == 0
                        && "0".equals(fcAccuBalance.getCalSign())) {
                    return fcAccuBalance;
                }
            }
        }
        return null;
    }
    /**
     * <b>功能介绍：</b>处理利息和积数信息<br>
     * <b>日期：</b>2017年8月26日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param account
     * @param money
     * @param orderNo
     * @param user
     * @param depositMangerId
     * @version 1.0
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void processAccuBalances(String account, BigDecimal money, String orderNo, ICurrentUser user,
                                    String depositMangerId, Date endInstDate) {
        if (EmptyUtils.isEmpty(user)) {
//			user = getUserVo();
        }
        boolean isPartMoney = false;
        if (null == money) {
            money = BigDecimal.ZERO;
        }
        if (money.compareTo(BigDecimal.ZERO) > 0) {
            isPartMoney = true;
        }
        if (EmptyUtils.isNotEmpty(orderNo)) {
            deleteTellerMoneyInterest(account, orderNo, depositMangerId, isPartMoney);
        }
        Date interestTime = DateUtils.getDate();
        fcInterestService.interestRushPredraw(account, user, depositMangerId, money, interestTime);
        Map<String, List> map = processAccInterestDtls(account, money, user, "", interestTime, endInstDate);
        //1、保存利息
        List<FcInterestDtl> interestDtls = map.get("interestDtls");
        if (EmptyUtils.isEmptyList(interestDtls)) {
            return;
        }
        for (FcInterestDtl interestDtl : interestDtls) {
            interestDtl.setOrderNo(orderNo);
            interestDtl.setBusStatus(InterestConstant.INTEREST_STATUE_CHECK);
            interestDtl.setInterestSign(InterestConstant.INTEREST_SIGN_IST);
            interestDtl.setGenerateMode(InterestConstant.GENERATEMODE_INTEREST_PART);
            interestDtl.setOperatorCode(user.getId());
            interestDtl.setInterestTime(interestTime);
            interestDtl.setCreateBy(user.getId());
            interestDtl.setCreateTime(interestTime);
            FcAccDTO dto = innerAccountService.getByBlnctrAcc(interestDtl.getAccount());
            interestDtl.setAccName(dto.getAccName());
            interestDtl.setAccType(dto.getAccType());
            interestDtl.setCorpId(dto.getCorpId());
            interestDtl.setNetId(dto.getNetId());
        }
        fcInterestDtlDao.batchSave(interestDtls);
        //2、更新积数
        FcAccDTO acc = iFcAccService.getAccDTOByAccount(interestDtls.get(0).getAccount());
        if (EmptyUtils.isEmpty(acc)) {
            acc = iFcAccService.getAccDTOByAccount(account);
        }
        updateTellerAccuBalance(acc, money, depositMangerId, map, interestTime);
    }
    /**
     * <b>功能介绍：</b>更新取款金额利息积数<br>
     * <b>日期：</b>2017年8月26日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param fcAcc
     * @param money
     * @param businessId
     * @param map
     * @param interestTime
     * @version 1.0
     */
    @SuppressWarnings("unchecked")
    public void updateTellerAccuBalance(FcAccDTO fcAcc, BigDecimal money, String businessId, Map<String, List> map, Date interestTime) {
        String accType = fcAcc.getAccType();
        if (null == interestTime) {
            interestTime = DateUtils.getDate();
        }
        if (!InnerAccountConstant.FCACC_BUSINESS_BEGULAR.equals(accType)
                && !InnerAccountConstant.FCACC_BANK_BEGULAR.equals(accType)
                && !InnerAccountConstant.FCACC_BANK_DEPOSIT.equals(accType)) {
            if (money.compareTo(BigDecimal.ZERO) > 0) { //指定金额
                List<FcAccuBalancef> repayAccuBalances = map.get("repayAccuBalances");//指定金额积数
                List<FcAccuBalancef> unPayAccuBalances = map.get("unPayAccuBalances");//剩余积数
                batchSaveLoanRepayDtl(repayAccuBalances, businessId, interestTime);
                if (!EmptyUtils.isEmptyList(unPayAccuBalances)) {
                    updateOrDelAccuBalances(unPayAccuBalances); //删除或更新余额积数
                }
            } else {//全部计息
                List<FcAccuBalancef> accuBalances = map.get("accuBalances");
                batchSaveLoanRepayDtl(accuBalances, businessId, interestTime);
            }
        }
    }
    /**
     * <b>功能介绍：</b>批量保存还款信息与积数的关系<br>
     * <b>日期：</b>2017年8月26日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param repayAccuBalances
     * @param businessId
     * @param interestTime
     * @version 1.0
     */
    public void batchSaveLoanRepayDtl(List<FcAccuBalancef> repayAccuBalances, String businessId, Date interestTime) {
        if (EmptyUtils.isEmptyList(repayAccuBalances)) {
            return;
        }
        for (FcAccuBalancef accuBalance : repayAccuBalances) {
            accuBalance.setInterestSign(InterestConstant.INTEREST_SIGN_YES);
            accuBalance.setInterestTime(interestTime);
            fcAccuBalancefService.saveOrUpdate(accuBalance);
            FcLoanBalanceDtl dtl = new FcLoanBalanceDtl();
            dtl.setBalanceId(accuBalance.getId());
            dtl.setLoanRepayId(businessId);
            dtl.setSysCode(InterestConstant.INTEREST_BUSINESS_CMS_CODE);
            fcLoanBalanceDtlService.save(dtl);
        }
    }
    /**
     * <b>功能介绍：</b>删除或更新余额积数<br>
     * <b>日期：</b>2017年8月26日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param accuBalances
     * @version 1.0
     */
    public void updateOrDelAccuBalances(List<FcAccuBalancef> accuBalances) {
        for (FcAccuBalancef accuBalance : accuBalances) {
            if (accuBalance.getCumu().compareTo(new BigDecimal(0)) > 0) {
                fcAccuBalancefService.updateEntity(accuBalance);
            } else {
                fcAccuBalancefService.delete(accuBalance.getId());
            }
        }
    }
    /**
     * <b>功能介绍：</b>更新积数<br>
     * <b>日期：</b>2017年8月26日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param account
     * @param orderNo
     * @param depositMangerId
     * @param isPartMoney
     * @version 1.0
     */
    public void deleteTellerMoneyInterest(String account, String orderNo, String depositMangerId, boolean isPartMoney) {
        FcAccDTO fcAcc = iFcAccService.getAccDTOByAccount(account);
        String accType = fcAcc.getAccType();
        if (!InnerAccountConstant.FCACC_BUSINESS_BEGULAR.equals(accType)
                && !InnerAccountConstant.FCACC_BANK_BEGULAR.equals(accType)
                && !InnerAccountConstant.FCACC_BANK_DEPOSIT.equals(accType)) {
            if (isPartMoney) {
                List<FcLoanBalanceDtl> balanceDtls = loanRepayDao.getAccuBalanceDtlsByBusinessId(depositMangerId);
                List<FcAccuBalancef> accuBalances = processAccuBalance(balanceDtls, account);
                updateListBatch(accuBalances);
            } else {//更新积数计息标识
                loanRepayDao.updateAccuBalanceByInterest(account, depositMangerId);
            }
            loanRepayDao.deleteAccuBalanceDtlsByOrderNo(depositMangerId);
        }
        //2、删除已冲销的预提信息
        if (StringUtils.isNotEmpty(depositMangerId)) {
            this.fcInterestService.deleteRushPredrawByDepositMangerId(depositMangerId);
        }
        //3、删除利息
        this.deleteInterestByOrderNo(orderNo);
    }
    /**
     * <b>功能介绍：</b>根据流水号删除利息<br>
     * <b>日期：</b>2017年12月18日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param orderNo
     * @version 1.0
     */
    public void deleteInterestByOrderNo(String orderNo) {
        this.fcInterestService.deleteInterestByOrderNo(orderNo);
    }
    /**
     * <b>功能介绍：</b>构造积数<br>
     * <b>日期：</b>2017年8月26日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param repayAccuBalanceDtls
     * @param account
     * @return
     * @version 1.0
     */
    private List<FcAccuBalancef> processAccuBalance(List<FcLoanBalanceDtl> repayAccuBalanceDtls,
                                                    String account) {
        if (EmptyUtils.isEmptyList(repayAccuBalanceDtls)) {
            return new ArrayList<FcAccuBalancef>();
        }
        Date startDate = fcAccuBalancefService.get(repayAccuBalanceDtls.get(0).getBalanceId(), true).getBalanceDate();
        Date endDate = fcAccuBalancefService.get(repayAccuBalanceDtls.get(repayAccuBalanceDtls.size() - 1).getBalanceId(), true).getBalanceDate();
        List<FcAccuBalancef> unInterestBalances = this.getUnInterestAccuBalances(account, startDate, endDate);
        List<FcAccuBalancef> accuBalances = new ArrayList<FcAccuBalancef>();
        List<FcAccuBalancef> unUseAccuBalances = new ArrayList<FcAccuBalancef>();
        for (FcLoanBalanceDtl repayAccuBalanceDtl : repayAccuBalanceDtls) {
            FcAccuBalancef DtlBalance = fcAccuBalancefService.get(repayAccuBalanceDtl.getBalanceId(), true);
            FcAccuBalancef accuBalance2 = new FcAccuBalancef();
            FcAccuBalancef accuBalance = isInBalanceDate(DtlBalance.getBalanceDate(),
                    unInterestBalances);
            if (null == accuBalance) {
                BeanUtils.copyProperties(DtlBalance, accuBalance2);
            } else {
                unUseAccuBalances.add(DtlBalance);
                BeanUtils.copyProperties(accuBalance, accuBalance2);
                accuBalance2.setCumu(accuBalance2.getCumu().add(DtlBalance.getCumu()));
                //还原调整积数
                if (accuBalance.getAdjustCumu() != null) {
                    if (DtlBalance.getAdjustCumu() != null) {
                        accuBalance2.setAdjustCumu(accuBalance.getAdjustCumu().add(DtlBalance.getAdjustCumu()));
                    }
                } else {
                    accuBalance2.setAdjustCumu(DtlBalance.getAdjustCumu());
                }
                accuBalance.setCalSign("1");
            }
            accuBalance2.setInterestSign(InterestConstant.INTEREST_SIGN_NO);
            accuBalance2.setInterestTime(null);
            accuBalances.add(accuBalance2);
        }
        if (unUseAccuBalances.isEmpty() || unUseAccuBalances.size() > 0) {
            this.deleteListBatch(unUseAccuBalances);
        }
        return accuBalances;
    }
    /**
     * <b>功能介绍：</b>获取账号指定区间的未计息积数<br>
     * <b>日期：</b>2017年8月26日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param account
     * @param startDate
     * @param endDate
     * @return
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public List<FcAccuBalancef> getUnInterestAccuBalances(String account, Date startDate, Date endDate) {
        return loanRepayDao.getUnInterestBalances(account, startDate, endDate);
    }
    /**
     * <b>功能介绍：</b>批量删除数据<br>
     * <b>日期：</b>2017年8月26日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param list
     * @version 1.0
     */
    public void deleteListBatch(List list) {
        this.validationService.deleteListBatch(list);
    }
    /**
     * <b>功能介绍：</b>批量更新数据<br>
     * <b>日期：</b>2017年8月26日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param list
     * @version 1.0
     */
    public void updateListBatch(List list) {
        this.validationService.updateListBatch(list);
    }
    /**
     * <b>功能介绍：</b>根据账户获取账户利率调整信息<br>
     * <b>日期：</b>2017年8月26日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param blnctrAcc
     * @return
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public Map<String, InstrateAdjustBean> queryInterestAdjustsByAcc(String blnctrAcc) {
//        return this.loanRepayDao.queryInterestAdjustsByAcc(blnctrAcc);
        Map<String, com.bytter.system.api.bean.InstrateAdjustBean> sMap = iFcInstrateAdjustService.queryInterestAdjustsByAcc(blnctrAcc);
        Map<String, InstrateAdjustBean> map = new HashMap<>();
        BeanUtils.copyMapProperties(sMap,map.getClass());
        return map;
    }
    /**
    * <br>
    * <b>功能：</b> 还原拆分积数信息 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2021/4/22 0022 <br>
    */
    public void reduceAccuBalance(String account, String businessId) {
        List<FcAccuBalancef> accuBalancefs = loanRepayDao.queryAccuBalances(account, null);//所有 未计息的积数
        List<FcLoanBalanceDtl> balanceDtls = loanRepayDao.getAccuBalanceDtlsByBusinessId(businessId);//本次还息关联的积数
        List<String> balanceIds = new ArrayList<>();
        for (FcLoanBalanceDtl dtl : balanceDtls) {
            balanceIds.add(dtl.getBalanceId());//积数ID
        }
        List<FcAccuBalancef> istBalanceDtls = loanRepayDao.queryAccuBalancesByIds(balanceIds);
        //还原成正常积数
        megerAccuBalance(istBalanceDtls, accuBalancefs);
        //删除关联关系
        loanRepayDao.deleteAccuBalanceDtlsByOrderNo(businessId);
    }
    /**
     * <br>
     * <b>功能：</b> 合并已计息与未计息积数 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/4/22 0022 <br>
     */
    public void megerAccuBalance(List<FcAccuBalancef> repayAccuBalanceDtls, List<FcAccuBalancef> unInterestBalances){
        List<FcAccuBalancef> accuBalances = new ArrayList<FcAccuBalancef>();
        List<FcAccuBalancef> unUseAccuBalances = new ArrayList<FcAccuBalancef>();
        List<String> accuIds = new ArrayList<String>();//积数ID
        for (FcAccuBalancef DtlBalance : repayAccuBalanceDtls) {
            FcAccuBalancef accuBalance2 = new FcAccuBalancef();
            FcAccuBalancef accuBalance = isInBalanceDate(DtlBalance.getBalanceDate(), unInterestBalances);//是否存在未计息积数
            if (null == accuBalance) {
                BeanUtils.copyProperties(DtlBalance, accuBalance2);
            } else {
                unUseAccuBalances.add(DtlBalance);
                accuIds.add(DtlBalance.getId());
                BeanUtils.copyProperties(accuBalance, accuBalance2);
                accuBalance2.setCumu(accuBalance2.getCumu().add(DtlBalance.getCumu()));
                //还原调整积数
                if (accuBalance.getAdjustCumu() != null) {
                    if (DtlBalance.getAdjustCumu() != null) {
                        accuBalance2.setAdjustCumu(accuBalance.getAdjustCumu().add(DtlBalance.getAdjustCumu()));
                    }
                } else {
                    accuBalance2.setAdjustCumu(DtlBalance.getAdjustCumu());
                }
                accuBalance.setCalSign("1");
            }
            accuBalance2.setInterestSign(InterestConstant.INTEREST_SIGN_NO);
            //accuBalance2.setInterestTime(null);
            accuBalances.add(accuBalance2);
        }
        if (accuIds.isEmpty() || accuIds.size() > 0) {
            loanRepayDao.deleteAccuBalances(accuIds);
        }
        updateListBatch(accuBalances);//批量更新积数信息
    }
    /**
    * <br>
    * <b>功能：</b> 更新积数为已计息，并保留积数与还款的对应关系 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2021/4/22 0022 <br>
    */
    public void saveLoanBalanceDtls(String businessId, List<String> balanceDtls, Date interestTime) {
        if (EmptyUtils.isEmptyList(balanceDtls)) {
            return;
        }
        List<FcLoanBalanceDtl> loanBalanceDtls = new ArrayList<>();
        for(String balanceId : balanceDtls) {
            FcLoanBalanceDtl balanceDtl = new FcLoanBalanceDtl();
            balanceDtl.setLoanRepayId(businessId);
            balanceDtl.setBalanceId(balanceId);
            loanBalanceDtls.add(balanceDtl);
        }
        getEntityDao().batchSave(loanBalanceDtls);
        loanRepayDao.updateAccuBalancesInterestSign(balanceDtls, interestTime);
    }
}
