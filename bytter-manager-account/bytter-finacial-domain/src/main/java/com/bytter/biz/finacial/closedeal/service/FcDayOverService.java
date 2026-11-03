package com.bytter.biz.finacial.closedeal.service;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.util.CollectionUtils;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.finacial.accbook.entity.FcAccbookDtl;
import com.bytter.biz.finacial.accbook.service.FcAccbookDtlService;
import com.bytter.biz.finacial.accbook.service.FcAccbookService;
import com.bytter.biz.finacial.api.constant.CloseConstant;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.enums.closedeal.CloseMessageEnum;
import com.bytter.biz.finacial.api.vo.FcAccBalanceVO;
import com.bytter.biz.finacial.closedeal.dao.FcDayCloseDao;
import com.bytter.biz.finacial.closedeal.dao.FcDayOverDao;
import com.bytter.biz.finacial.closedeal.dao.FcDayStartDao;
import com.bytter.biz.finacial.closedeal.entity.*;
import com.bytter.biz.finacial.closedeal.vo.*;
import com.bytter.biz.finacial.outaccbook.service.ClFcOutAccBalanceService;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.check.api.ICentralityDataManageService;
import com.bytter.check.api.IFcCompareAccService;
import com.bytter.check.api.IFcCompareCenterService;
import com.bytter.check.dto.FcCompareAccBeanDTO;
import com.bytter.check.dto.FcCompareAccDTO;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.InnerAccountConstant;
import com.bytter.core.utils.*;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.IFcSubjectDayService;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.FcDayCloseDTO;
import com.bytter.system.api.dto.FcMonthCloseDTO;
import com.bytter.system.api.dto.FcSubjectDayBeanDTO;
import com.bytter.system.api.vo.FcAccVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 日结执行service
 *
 * @author liyh
 */
@Service("fcDayOverService")
@Transactional
public class FcDayOverService extends FcCloseBaseService {
    @Autowired
    private FcDayOverDao overDao;
    @Autowired
    private FcDayCloseDao dayCloseDao;
    @Autowired
    private FcDayStartDao dayStartDao;
    @Autowired
    private ClFcAccBalanceService balanceService;
    @Autowired
    private ClFcAccuBalanceService accuBalanceService;
    @DubboReference(check = false)
    private IFcSubjectDayService iFcSubjectDayService;
    @Autowired
    private FcCarryService carryService;
    /*@Autowired
    private CentralityDataManageService compareAccService;*/
    @DubboReference
    private ICentralityDataManageService compareAccService;
    @Autowired
    private FcCorpInterestDtlService interestDtlService;
    @Autowired
    private FcMonthLedgerService monthLedgerService;
    @Autowired
    private FcIncomeStatementService incomeService;
    @Autowired
    private FcBalanceSheetService sheetService;
    @Autowired
    private ClFcOutAccBalanceService outBalanceService;
    @Autowired
    private FcCashFlowService cashFlowService;
    @DubboReference(check = false)
    private IFcAccService fcAccService;

    @DubboReference(check = false)
    private IFcVoucherTypeService fcVoucherTypeServiceImpl;

    @DubboReference(check = false)
    private IFcCompareCenterService ifcCompareCenterService;

    @Autowired
    private FcAccbookService fcAccbookService;
    @DubboReference(check = false)
    private IFcCompareAccService iFcCompareAccService;

    @Autowired
    private FcAccbookDtlService fcAccbookDtlService;

    /**
     * 功能介绍 : 日结过程处理。<br>
     * <b>日期：</b>2017年7月11日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void exceDayClose(FcDayCloseVO closeVO, ICurrentUser user) {
        /**
         * 日结过程：
         * 1、处理表外账务
         * 2、查询流水号的借贷方金额是否平衡
         * 3、检查各科目的复核联机余额借贷平衡
         * 3.1、调整账户余额不平的记录
         * 5、生成每日余额和科目日结单
         * 6、各科目借贷批量余额借贷是否平衡
         * 7、检查账户的联机笔数与批量笔数是否平衡(实时余额的笔数=日结日的笔数+账务笔数)
         * 8、检查账户的联机复核余额与批量余额是否相等(实时余额=日结日的余额+账务金额)
         * 9、生成科目日结单（第5点已产生）
         * 10、产生积数余额表上个工作日以后至当天的积数
         * 11、在转移流水账之前更新流水账余额(保证流水账余额按照复核顺序变化)
         * 12、生成单位对账单数据
         * 13、当天流水账转已结流水账表
         * 14、记录本次日结记录信息
         * 15、生成单位利息清单出错
         * 16、更新网点日结日期
         */
        String overType = CloseConstant.CarryType.CARRY_TYPE_DAY_OVER.get();
        String netId = closeVO.getNetCode();
        Date closeDate = closeVO.getCloseDate();
        Date lastCloseDate = closeVO.getLastCloseDate();
        processOutAccbook(closeVO, user);
        checkMoneyGroupByOrderNo(closeVO);
        checkSubjectMoney(closeVO, true);
        List<FcAccDTO> fcAccDTOS = fcAccService.getFcAccByNetId(netId);
        List<String> accList = null;
        if (ListUtils.isNotNullOrEmpty(fcAccDTOS)) {
            accList = fcAccDTOS.stream().map(FcAccDTO::getBlnctrAcc).collect(Collectors.toList());
        }
        //查询账务相关明细数据
        List<AccbookBalanceVO> accbookBalanceVOs = overDao.queryAccbookBalance(netId, closeDate, accList);
        //TODO 账务明细数据属性填充
        fullAccbookBalProperties(accbookBalanceVOs, fcAccDTOS);
        if (null == accbookBalanceVOs) {
            accbookBalanceVOs = new ArrayList<AccbookBalanceVO>();
        }
        //最后一日的账户余额数据
        List<AccBalanceBean> dayBalList = generateAccBalance(netId, closeDate, lastCloseDate, accbookBalanceVOs, user);
        checkAccBalanceMoney(closeVO);
        checkAccountCnt(closeVO);
        checkAccountBalance(closeVO);
        //generateSubjectDay(closeVO);
        generateAccuBalance(closeVO, user);
        updateAccbookDtlBalance(closeVO, dayBalList, accbookBalanceVOs);
        generateCompareAcc(closeVO);
        closeAccbook(closeVO);
        saveFcCarry(closeVO, user, overType);
        generateCorpInterest(closeVO, user);
        updateNetCloseDate(closeVO, user, overType);
    }

    private void fullAccbookBalProperties(List<AccbookBalanceVO> accbookBalanceVOs, List<FcAccDTO> fcAccDTOS) {

        if (ListUtils.isNullOrEmpty(accbookBalanceVOs) || accbookBalanceVOs.size() < 1) {
            return;
        }
        // if (ListUtils.isNullOrEmpty(fcAccDTOS) || fcAccDTOS.size() < 1) {return;}

        Map<String, FcAccDTO> accountMap = new HashMap<>();
        if (ObjectUtil.isNotEmpty(fcAccDTOS)) {
            accountMap = fcAccDTOS.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, Function.identity()));
        }

        List<String> voucherTypeIdList = accbookBalanceVOs.stream()
                .filter(accbookBalance -> !StringUtils.isNotBlank(accbookBalance.getSysVoucherType()))
                .map(AccbookBalanceVO::getSysVoucherType)
                .collect(Collectors.toList());
        //TODO 从结算录入
        List<FcVoucherTypeDTO> fcVoucherTypeDOList = fcVoucherTypeServiceImpl.getFcVoucherTypeByIds(voucherTypeIdList);
        Map<String, FcVoucherTypeDTO> voucherMap = fcVoucherTypeDOList.stream().collect(Collectors.toMap(FcVoucherTypeDTO::getId, Function.identity()));

        Map<String, FcAccDTO> finalAccountMap = accountMap;
        accbookBalanceVOs.forEach(accbookBal -> {
            FcAccDTO fcAccDTO = finalAccountMap.get(accbookBal.getAccount());
            if (ObjectUtil.isNotEmpty(fcAccDTO)) {
                accbookBal.setAccName(fcAccDTO.getAccName());
                accbookBal.setCurId(fcAccDTO.getCurId());
                accbookBal.setSubjectId(fcAccDTO.getSubjectId());
                accbookBal.setBalanceWay(fcAccDTO.getBalanceWay());
            }
            if (ObjectUtil.isEmpty(accbookBal.getBalanceWay())) {
                // 借方金额为零则余额方向：-1，借进（1）贷出（-1）
                int balanceWay = accbookBal.getDebitMoney().compareTo(BigDecimal.ZERO) == 0 ? -1 : 1;
                accbookBal.setBalanceWay(balanceWay);
            }

            if (!StringUtils.isBlank(accbookBal.getSysVoucherType()) && !EmptyUtils.isEmpty(voucherMap.get(accbookBal.getSysVoucherType()))) {
                //TODO 自定义付款方式补充
                accbookBal.setCashTransSign(voucherMap.get(accbookBal.getSysVoucherType()).getCashTransSign());
                accbookBal.setSysVoucherType(voucherMap.get(accbookBal.getSysVoucherType()).getSysVoucherCode());
            }
        });
    }

    /**
     * 功能介绍 : 处理表外账务。<br>
     * <b>日期：</b>2017年7月10日<br>
     *
     * @return
     * @version 1.0
     * @author liyh
     */
    public void processOutAccbook(FcDayCloseVO dayClose, ICurrentUser user) {
        //带表外账务完成再处理
        FcDayCloseDTO closeDTO = new FcDayCloseDTO();
        BeanUtils.copyProperties(dayClose, closeDTO);
        outBalanceService.generateOutAccBalance(closeDTO, user);
    }

    /**
     * 功能介绍 : 查询流水号的借贷方金额是否平衡。<br>
     * <b>日期：</b>2017年7月6日<br>
     *
     * @return
     * @version 1.0
     * @author liyh
     */
    public List<Map<String, Object>> checkMoneyGroupByOrderNo(FcDayCloseVO dayClose) {
        Map<String, String> map = fcAccService.getBlnctrAccAndAccNameByNetId(dayClose.getNetCode());
        Set<String> strings = map.keySet();
        if (!strings.isEmpty()) {
            List<Map<String, Object>> list = overDao.checkMoneyGroupByOrderNo(dayClose, Lists.newArrayList(strings));
            if (null != list && !list.isEmpty() && list.size() > 0) {
                String orderNo = (String) list.get(0).get("ORDERNO");
                throw new ParameterException(CloseMessageEnum.FIN_CLOSE_ORDER_NO_BALANCE, orderNo);
            }
            return list;
        }
        return null;
    }

    /**
     * 功能介绍 : 检查各科目的复核联机余额借贷平衡。<br>
     * <b>日期：</b>2017年7月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcAccountBalanceVo> checkSubjectMoney(FcDayCloseVO dayClose, boolean adjustFlag) {
        Map<String, Integer> accountAndBalanceWayMap = fcAccService.getBlnctrAccAndBalanceWayByNetIdAndNotAccType(dayClose.getNetCode(), InnerAccountConstant.FCACC_OFFBALANCE);
        Set<String> key = accountAndBalanceWayMap.keySet();
        if (!key.isEmpty()) {
            List<FcAccountBalanceVo> list = overDao.checkSubjectMoney(dayClose, Lists.newArrayList(key));
            BigDecimal debitMoney = BigDecimal.ZERO, creditMoney = BigDecimal.ZERO;
            if (null != list && !list.isEmpty() && list.size() > 0) {
                //借贷方余额是否平衡
                for (FcAccountBalanceVo balanceVo : list) {
                    balanceVo.setBalanceWay(accountAndBalanceWayMap.get(balanceVo.getAccount()));
                    Integer balanceWay = balanceVo.getBalanceWay();//余额方向
                    BigDecimal balance = balanceVo.getBalance();//余额
                    if (FinacialConstant.BALANCE_WAY_DEBIT.equals(balanceWay.toString())) {
                        debitMoney = balance;//借方余额合计
                    } else {
                        creditMoney = balance;//贷方余额合计
                    }
                }
                if (debitMoney.compareTo(creditMoney) != 0) {
                    if (!adjustFlag) {
                        throw new ParameterException(CloseMessageEnum.FIN_CLOSE_SUBJECT_BALANCE);
                    }
                    //余额不平，则调整余额
                    adjustSubjectMoney(dayClose);
                }
            }
            // 分组求和; TODO 这里有个小问题 BigDecimal转Double 求和 精度丢失问题
            if (list != null) {
                Map<Integer, Double> map = list.stream().collect(Collectors.groupingBy(FcAccountBalanceVo::getBalanceWay, Collectors.summingDouble(a -> a.getBalance().doubleValue())));
                return map.entrySet().stream().map(m -> {
                    FcAccountBalanceVo fcAccountBalanceVo = new FcAccountBalanceVo();
                    fcAccountBalanceVo.setBalanceWay(m.getKey());
                    fcAccountBalanceVo.setBalance(BigDecimal.valueOf(m.getValue()));
                    return fcAccountBalanceVo;
                }).collect(Collectors.toList());
            }
            return list;
        }
        return null;
    }

    /**
     * 功能介绍 : 科目余额不平的情况下调整账户余额(余额回溯)。<br>
     * <b>日期：</b>2017年7月10日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void adjustSubjectMoney(FcDayCloseVO dayClose) {
        //TODO 太复杂了，后续再实现
    }

    /**
     * 功能介绍 : 生成每日余额和科目日结单。<br>
     * <b>日期：</b>2017年7月10日<br>
     *
     * @return
     * @version 1.0
     * @author liyh
     */
    public List<AccBalanceBean> generateAccBalance(String netId, Date closeDate, Date lastDate,
                                                   List<AccbookBalanceVO> accbookBalanceVOs, ICurrentUser user) {
        //获取昨日余额
        List<AccBalanceBean> balanceList = balanceService.queryBalanceByNetDate(netId,
                lastDate);
        if (null == balanceList) {
            balanceList = new ArrayList<AccBalanceBean>();
        }
        //账务数据按照日期分类
        Map<String, List<AccbookBalanceVO>> dateMap = new HashMap<String, List<AccbookBalanceVO>>();
        for (AccbookBalanceVO balanceVO : accbookBalanceVOs) {
            Date balanceDate = balanceVO.getTallyDate();
            String balanceDateStr = DateUtils.formatDate(balanceDate);
            List<AccbookBalanceVO> balanceVOs = dateMap.get(balanceDateStr);
            if (null == balanceVOs) {
                balanceVOs = new ArrayList<AccbookBalanceVO>();
                dateMap.put(balanceDateStr, balanceVOs);
            }
            balanceVOs.add(balanceVO);
        }
        //循环余额日期
        Date tempDate = lastDate;
        List<AccBalanceBean> dayBalList = balanceList;
        //List<FcSubjectDayBean> daySubList = subDayList;
        while (tempDate.compareTo(closeDate) < 0) {
            String balanceDateStr = DateUtils.formatDate(DateUtils.addDay(tempDate, 1));//取下一日的账务明细
            List<AccbookBalanceVO> balanceVOs = dateMap.get(balanceDateStr);
            if (null == balanceVOs) {
                balanceVOs = new ArrayList<AccbookBalanceVO>();
            }
            //递归调用 //TODO 里面对dayBalList-做了处理 导致有些subjectId为null
            dayBalList = geneAccBalance(tempDate, netId, dayBalList, balanceVOs, closeDate, user);
            tempDate = DateUtils.addDay(tempDate, 1);
        }
        overDao.flush();
        return dayBalList;
    }

    /**
     * 功能介绍 : 根据昨日余额+当天的账务金额生成当天的余额；
     * 昨日余额和当日余额表数据汇总生成科目日结单。<br>
     * <b>日期：</b>2017年7月10日<br>
     *
     * @param netId
     * @param closeDate
     * @version 1.0
     * @author liyh
     */
    public List<AccBalanceBean> geneAccBalance(Date yesterDate, String netId, List<AccBalanceBean> balanceList,
                                               List<AccbookBalanceVO> accbookBalanceVOs, Date closeDate, ICurrentUser user) {
        List<AccBalanceBean> dayBalList = new ArrayList<AccBalanceBean>();
        // 获取相关余额信息
        Map<String, FcSubjectDayBean> subjectMap = new HashMap<String, FcSubjectDayBean>();
        Map<String, AccBalanceBean> balanceMap = new HashMap<String, AccBalanceBean>();
        Map<String, String> accountAndSubjectIdMap = balanceList.stream().collect(Collectors.toMap(AccBalanceBean::getAccount, AccBalanceBean::getSubjectId, (a, b) -> a));
        for (AccbookBalanceVO balanceVO : accbookBalanceVOs) {
            String account = balanceVO.getAccount();
            AccBalanceBean balanceBean = balanceMap.get(account);
            if (null == balanceBean) {
                balanceBean = new AccBalanceBean();
                balanceBean.setAccount(account);
                balanceBean.setAccName(balanceVO.getAccName());
                balanceBean.setCurId(balanceVO.getCurId());
                balanceBean.setBalanceDate(DateUtils.addDay(yesterDate, 1));
                balanceBean.setSubjectId(accountAndSubjectIdMap.get(account));
                balanceMap.put(account, balanceBean);
            }
            String sysVoucherType = balanceVO.getSysVoucherType();// 系统凭证类型
            String redblueSign = balanceVO.getRedblueSign();// 红蓝标识
            String cashTransSign = balanceVO.getCashTransSign();//现金转账标识
            if (NetbankConstant.PAY_TYPE_05.equals(sysVoucherType)) {
                balanceBean.addTrCnt(1);// 内转笔数增加
                balanceBean.addTrDebitBalance(balanceVO.getDebitMoney());// 内转借方金额
                balanceBean.addTrCreditBalance(balanceVO.getCreditMoney());// 内转贷方金额
            }
            if (FinacialConstant.REDBLUE_SIGN_RED.equals(redblueSign)) {
                balanceBean.addTrRush(1);// 冲账笔数增加
                balanceBean.addRushDebitBalance(balanceVO.getDebitMoney());// 冲账借方金额
                balanceBean.addRushCreditBalance(balanceVO.getCreditMoney());// 冲账贷方金额
            }
            balanceBean.addCnt(1);// 总笔数增加
            balanceBean.addDebitBalance(balanceVO.getDebitMoney());// 借方金额
            balanceBean.addCreditBalance(balanceVO.getCreditMoney());// 贷方金额
            BigDecimal balance = balanceVO.getDebitMoney().subtract(balanceVO.getCreditMoney()).multiply(new BigDecimal(balanceVO.getBalanceWay()));
            balanceBean.addBalance(balance);//设置账户余额为发生额相加
            if (0 != balanceVO.getDebitMoney().compareTo(BigDecimal.ZERO)) {
                balanceBean.addDebitCnt(1);// 借方笔数增加
            } else {
                balanceBean.addCreditCnt(1);// 贷方笔数增加
            }
            /**
             * 科目日结单数据
             */
            String subjectId = balanceVO.getSubjectId();//科目
            String curId = balanceVO.getCurId();//币别
            String subDayKey = subjectId + "_" + curId;
            FcSubjectDayBean subDayBean = subjectMap.get(subDayKey);
            if (null == subDayBean) {
                subDayBean = new FcSubjectDayBean(netId, subjectId, curId);
                subDayBean.setOptDate(DateUtils.addDay(yesterDate, 1));
                subjectMap.put(subDayKey, subDayBean);
            }
            if (NetbankConstant.SIGN_TRAN.equals(cashTransSign)) {//现金转账标识-转账
                if (0 != balanceVO.getDebitMoney().compareTo(BigDecimal.ZERO)) {
                    subDayBean.addTrDebitCnt(1);// 转账借方笔数增加
                    subDayBean.addTrDebitMoney(balanceVO.getDebitMoney());// 转账借方金额增加
                } else {
                    subDayBean.addTrCreditCnt(1);// 转账贷方笔数增加
                    subDayBean.addTrCreditMoney(balanceVO.getCreditMoney());// 转账贷方金额增加
                }
            } else {//现金标识
                if (0 != balanceVO.getDebitMoney().compareTo(BigDecimal.ZERO)) {
                    subDayBean.addCashDebitCnt(1);// 现金借方笔数增加
                    subDayBean.addCashDebitMoney(balanceVO.getDebitMoney());// 现金借方金额增加
                } else {
                    subDayBean.addCashCreditCnt(1);// 现金贷方笔数增加
                    subDayBean.addCashCreditMoney(balanceVO.getCreditMoney());// 现金贷方金额增加
                }
            }
        }
        List<AccBalanceBean> accBalList = new ArrayList<AccBalanceBean>();
        // 循环账户每日余额数据
        for (AccBalanceBean accBalance : balanceList) {
            Date accStartDate = accBalance.getAccStartDate();//账户启用日期
            Date cancelDate = accBalance.getCancelDate();//账户注销日期
            Date balanceDate = DateUtils.addDay(yesterDate, 1);// 下一日的余额
            String account = accBalance.getAccount();
            BigDecimal balance = accBalance.getBalance();// 账户余额
            AccBalanceBean balanceBean = balanceMap.get(account);
            balanceMap.remove(account);
            if (null == balanceBean) {
                balanceBean = new AccBalanceBean();
                balanceBean.setBalanceDate(balanceDate);
                balanceBean.setAccount(account);
                balanceBean.setAccName(accBalance.getAccName());
                balanceBean.setCurId(accBalance.getCurId());
                balanceBean.setSubjectId(accBalance.getSubjectId());
            }
            if (accStartDate.compareTo(balanceDate) > 0) {//账户启用日期大于余额日期
                balanceBean.setSaveFlag(false);//不保存账户余额
            }
            if (null != cancelDate && cancelDate.compareTo(yesterDate) <= 0) {//账户注销日期小于余额日期
                balanceBean.setSaveFlag(false);//不保存账户余额
            }
            balanceBean.setAccStartDate(accBalance.getAccStartDate());
            balanceBean.setCancelDate(accBalance.getCancelDate());
            balanceBean.setYesterBalance(balance);
            balanceBean.addCnt(accBalance.getCnt());// 账户笔数
            balanceBean.addBalance(balance);// 新增账户余额
            if (DateUtils.dayDiffs(balanceDate, closeDate) == 0) {
                balanceBean.setTallyDaySign("1");//记账日标识
            } else {
                balanceBean.setTallyDaySign("0");//记账日标识
            }
            dayBalList.add(balanceBean);//新的每日余额
            if (balanceBean.isSaveFlag()) {
                accBalList.add(balanceBean);
                //balanceService.saveAccBalance(balanceBean, user);
            }
            /**
             * 科目日结单数据来源于账户余额表，统计账户余额表数据也可以生成科目日结单
             */
            String subjectId = accBalance.getSubjectId();//科目
            String curId = accBalance.getCurId();//币别
            String subDayKey = subjectId + "_" + curId;
            FcSubjectDayBean subDayBean = subjectMap.get(subDayKey);
            if (null == subDayBean) {
                subDayBean = new FcSubjectDayBean(netId, subjectId, curId);
                subDayBean.setOptDate(balanceDate);
                subjectMap.put(subDayKey, subDayBean);
            }
            subDayBean.addYestBalance(balance);//昨日余额
            subDayBean.addTodayBalance(balanceBean.getBalance());//今日余额
        }
        Set<String> accountList = balanceMap.keySet();
        if (accountList.size() > 0) {
            for (String account : accountList) {
                FcAccVO accBalance = fcAccService.getFcAccVOByBlnctrAcc(account);
                AccBalanceBean accBalanceBean = balanceMap.get(account);
                Date accStartDate = accBalance.getAccStartDate();//账户启用日期
                Date cancelDate = accBalance.getCancelDate();//账户注销日期
                //如果账户启用日期在上次日结日期之后，则判断一下是否上次日结日期余额是否存在。
                if (yesterDate.compareTo(accStartDate) < 0) {
                    FcAccBalanceVO balance = overDao.queryBalanceByDate(account, yesterDate);
                    if (null == balance){
                        AccBalanceBean balanceBean = new AccBalanceBean();
                        balanceBean.setBalanceDate(yesterDate);
                        balanceBean.setAccount(account);
                        balanceBean.setAccName(accBalance.getAccName());
                        balanceBean.setBalanceWay(accBalance.getBalanceWay());
                        balanceBean.setCurId(accBalance.getCurId());
                        balanceBean.setSubjectId(accBalance.getSubjectId());
                        balanceBean.setAccStartDate(accBalance.getAccStartDate());
                        balanceBean.setCancelDate(accBalance.getCancelDate());
                        if (DateUtils.dayDiffs(yesterDate, closeDate) == 0) {
                            balanceBean.setTallyDaySign("1");//记账日标识
                        } else {
                            balanceBean.setTallyDaySign("0");//记账日标识
                        }
                        accBalList.add(balanceBean);
                    }
                }
                Date balanceDate = DateUtils.addDay(yesterDate, 1);// 下一日的余额
                BigDecimal balance = accBalanceBean.getBalance();// 账户余额
                AccBalanceBean balanceBean = new AccBalanceBean();
                balanceBean.setBalanceDate(balanceDate);
                balanceBean.setAccount(account);
                balanceBean.setAccName(accBalance.getAccName());
                balanceBean.setBalanceWay(accBalance.getBalanceWay());
                balanceBean.setCurId(accBalance.getCurId());
                balanceBean.setSubjectId(accBalance.getSubjectId());
                if (accBalanceBean.getCreditMoney().compareTo(BigDecimal.ZERO) == 1) {
                    balanceBean.setCreditMoney(accBalanceBean.getCreditMoney());
                }
                if (accBalanceBean.getDebitMoney().compareTo(BigDecimal.ZERO) == 1) {
                    balanceBean.setDebitMoney(accBalanceBean.getDebitMoney());
                }
                if (accStartDate.compareTo(balanceDate) > 0) {//账户启用日期大于余额日期
                    balanceBean.setSaveFlag(false);//不保存账户余额
                }
                if (null != cancelDate && cancelDate.compareTo(yesterDate) <= 0) {//账户注销日期小于余额日期
                    balanceBean.setSaveFlag(false);//不保存账户余额
                }
                balanceBean.setAccStartDate(accBalance.getAccStartDate());
                balanceBean.setCancelDate(accBalance.getCancelDate());
                balanceBean.setYesterBalance(balance);
                balanceBean.addCnt(0);// 账户笔数
                balanceBean.addBalance(balance);// 新增账户余额
                if (DateUtils.dayDiffs(balanceDate, closeDate) == 0) {
                    balanceBean.setTallyDaySign("1");//记账日标识
                } else {
                    balanceBean.setTallyDaySign("0");//记账日标识
                }
                dayBalList.add(balanceBean);//新的每日余额
                if (balanceBean.isSaveFlag()) {
                    accBalList.add(balanceBean);
                }
                /**
                 * 科目日结单数据来源于账户余额表，统计账户余额表数据也可以生成科目日结单
                 */
                String subjectId = accBalance.getSubjectId();//科目
                String curId = accBalance.getCurId();//币别
                String subDayKey = subjectId + "_" + curId;
                FcSubjectDayBean subDayBean = subjectMap.get(subDayKey);
                if (null == subDayBean) {
                    subDayBean = new FcSubjectDayBean(netId, subjectId, curId);
                    subDayBean.setOptDate(balanceDate);
                    subjectMap.put(subDayKey, subDayBean);
                }
                subDayBean.addYestBalance(balance);//昨日余额
                subDayBean.addTodayBalance(balanceBean.getBalance());//今日余额
            }
        }

        if (EmptyUtils.isNotEmpty(accBalList)) {
            balanceService.saveAccBalance(accBalList, closeDate, user);//批量保存
        }
        //保存科目日结单数据，插入账号没有发生账务的科目余额表数据
        ArrayList<FcSubjectDayBean> fcSubjectDayBeans = new ArrayList<>(subjectMap.values());
        List<FcSubjectDayBeanDTO> fcSubjectDayBeanDTOS = BeanUtils.copyListProperties(fcSubjectDayBeans, FcSubjectDayBeanDTO.class);
        iFcSubjectDayService.saveSubjectDayBeans(fcSubjectDayBeanDTOS);
        return dayBalList;
    }

    /**
     * 功能介绍 : 检查各科目每日批量余额借贷是否平衡。<br>
     * <b>日期：</b>2017年7月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<FcAccountBalanceVo> checkAccBalanceMoney(FcDayCloseVO dayClose) {
        Map<String, Integer> accountAndBalanceWayMap = fcAccService.getBlnctrAccAndBalanceWayByNetIdAndNotAccType(dayClose.getNetCode(), InnerAccountConstant.FCACC_OFFBALANCE);
        if (EmptyUtils.isEmpty(accountAndBalanceWayMap) || accountAndBalanceWayMap.size() < 1) {
            return null;
        }
        Set<String> key = accountAndBalanceWayMap.keySet();
        List<FcAccountBalanceVo> list = overDao.checkAccBalanceMoney(dayClose, Lists.newArrayList(key));
        AtomicReference<BigDecimal> debitMoney = new AtomicReference<>(BigDecimal.ZERO);
        AtomicReference<BigDecimal> creditMoney = new AtomicReference<>(BigDecimal.ZERO);
        if (null != list && !list.isEmpty() && list.size() > 0) {
            //借贷方余额是否平衡
            for (FcAccountBalanceVo balanceVo : list) {
                balanceVo.setBalanceWay(accountAndBalanceWayMap.get(balanceVo.getAccount()));
                Integer balanceWay = balanceVo.getBalanceWay();//余额方向
                BigDecimal balance = balanceVo.getBalance();//余额
//                BigDecimal balancef= balance.setScale(0,BigDecimal.ROUND_HALF_UP);
                balanceVo.setCalBalance(balance);
                /*if (FinacialConstant.BALANCE_WAY_DEBIT.equals(balanceWay.toString())) {
                    debitMoney = balance;//借方余额合计
                } else {
                    creditMoney = balance;//贷方余额合计Collectors.summ.summingLong(FcAccountBalanceVo::getCalBalance)
                }*/
            }
//            分组求和
//          Map<Integer, Long> balanceMap = list.stream().collect(Collectors.groupingBy(FcAccountBalanceVo::getBalanceWay, Collectors.summingLong(FcAccountBalanceVo::getCalBalance)));
            Map<Integer, BigDecimal> balanceMap = list.stream().collect(
                    Collectors.groupingBy(FcAccountBalanceVo::getBalanceWay, Collectors.mapping(FcAccountBalanceVo::getCalBalance, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));

            balanceMap.keySet().forEach(a -> {
                if (FinacialConstant.BALANCE_WAY_DEBIT.equals(a.toString())) {
                    debitMoney.set(balanceMap.get(a));//借方余额合计
                } else {
                    creditMoney.set(balanceMap.get(a));//贷方余额合计
                }
            });

            if (debitMoney.get().compareTo(creditMoney.get()) != 0) {
                throw new ParameterException(CloseMessageEnum.FIN_CLOSE_FC_ACC_BALANCE);
            }
        }
        if (list != null) {
            Map<Integer, Double> map = list.stream().collect(Collectors.groupingBy(FcAccountBalanceVo::getBalanceWay, Collectors.summingDouble(a -> a.getBalance().doubleValue())));
            return map.entrySet().stream().map(m -> {
                FcAccountBalanceVo fcAccountBalanceVo = new FcAccountBalanceVo();
                fcAccountBalanceVo.setBalanceWay(m.getKey());
                fcAccountBalanceVo.setBalance(BigDecimal.valueOf(m.getValue()));
                return fcAccountBalanceVo;
            }).collect(Collectors.toList());
        }
        return list;
    }

    /**
     * 功能介绍 : 检查账户的联机笔数与批量笔数是否平衡(实时余额的笔数=日结日的笔数+账务笔数)。<br>
     * <b>日期：</b>2017年7月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<String> checkAccountCnt(FcDayCloseVO dayClose) {
        checkAccountCurrentCnt(dayClose, true);
        checkAccountCurrentCnt(dayClose, false);
        return null;
    }

    public List<String> checkAccountCurrentCnt(FcDayCloseVO dayClose, boolean checkSign) {
        Map<String, Integer> map = fcAccService.getBlnctrAccAndBalanceWayByNetIdAndNotAccType(dayClose.getNetCode(), InnerAccountConstant.FCACC_OFFBALANCE);
        if (EmptyUtils.isEmpty(map) || map.size() < 1) {
            return null;
        }
        List<String> blnctrAccs = new ArrayList<>(map.keySet());
        List<String> list = overDao.checkAccountCnt(dayClose, checkSign, blnctrAccs);
        if (null != list && !list.isEmpty() && list.size() > 0) {
            //根据有问题的账号昨日余额的笔数重新计算
            overDao.updateAccountTallyCnt(dayClose, list, checkSign);
        }
        return list;
    }

    /**
     * 功能介绍 : 检查账户的联机复核余额与批量余额是否相等(实时余额=日结日的余额+账务金额)。<br>
     * <b>日期：</b>2017年7月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public List<String> checkAccountBalance(FcDayCloseVO dayClose) {
        checkAccountCurrentBalance(dayClose, true);
        checkAccountCurrentBalance(dayClose, false);
        return null;
    }

    public List<String> checkAccountCurrentBalance(FcDayCloseVO dayClose, boolean checkSign) {
        //TODO 获取非表外账户列表
        Map<String, Integer> accAndWayList = fcAccService.getBlnctrAccAndBalanceWayByNetIdAndNotAccType(dayClose.getNetCode(), InnerAccountConstant.FCACC_OFFBALANCE);
        if (CollectionUtils.isEmpty(accAndWayList)) {
            return null;
        }
        List<String> accList = accAndWayList.entrySet().stream().map(entry -> entry.getKey()).collect(Collectors.toList());
        List<String> list = overDao.checkAccountBalance(dayClose, checkSign, accList);
        if (null != list && !list.isEmpty() && list.size() > 0) {
            //根据有问题的账号昨日余额的笔数重新计算
            overDao.updateAccountTallyBalance(dayClose, list, checkSign);
            //throw new ParameterException(CloseMessageEnum.FIN_CLOSE_ACCLEDGER_BALANCE);
        }
        return list;
    }

    /**
     * 功能介绍 : 生成科目日结单。<br>
     * <b>日期：</b>2017年7月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void generateSubjectDay(FcDayCloseVO dayClose) {
        //生成每日余额表中已实现
        //generateAccBalance(netId, closeDate, lastDate);
    }

    /**
     * 功能介绍 : 产生积数余额表上个工作日以后至当天的积数。<br>
     * <b>日期：</b>2017年7月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void generateAccuBalance(FcDayCloseVO dayClose, ICurrentUser user) {
        /**
         * 协定积数暂时未处理，待后续上理财管理再处理协定积数
         *
         */
        accuBalanceService.generateAccuBalance(dayClose, user);
    }

    /**
     * 功能介绍 : 产生资金池主体单位的积数余额表上个工作日以后至当天的积数。<br>
     * <b>日期：</b>2019年4月30日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void generatePoolAccuBalance(FcDayCloseVO dayClose, ICurrentUser user) {
        /**
         * 协定积数处理，待后续上理财管理再处理协定积数
         *
         */
        accuBalanceService.generateAccuBalance(dayClose, user);
    }

    /**
     * 功能介绍 : 在转移流水账之前更新流水账余额。<br>
     * <b>日期：</b>2017年7月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateAccbookDtlBalance(FcDayCloseVO dayClose, List<AccBalanceBean> dayBalList,
                                        List<AccbookBalanceVO> accbookBalanceVOs) {
        Map<String, AccBalanceBean> balanceMap = new HashMap<String, AccBalanceBean>();
        for (AccBalanceBean bean : dayBalList) {
            String account = bean.getAccount();
            balanceMap.put(account, bean);
        }
        //循环账务数据
        for (AccbookBalanceVO balanceVO : accbookBalanceVOs) {
            String dtlId = balanceVO.getDtlId();
            String account = balanceVO.getAccount();//账号
            AccBalanceBean balanceBean = balanceMap.get(account);
            if (null == balanceBean) {
                //应该是存在的，如果不存在数据库中查询
                List<AccBalanceBean> beans = balanceService.queryBalanceByAccDate(account, dayClose.getCloseDate());
                if (null == beans || beans.isEmpty() || beans.size() < 1) {
                    throw new ParameterException(CloseMessageEnum.FIN_CLOSE_ACC_BALANCE_NOEXIST);
                }
                balanceBean = beans.get(0);
                balanceMap.put(account, balanceBean);
            }
            BigDecimal money = balanceVO.getDebitMoney().subtract(balanceVO.getCreditMoney())
                    .multiply(new BigDecimal(balanceVO.getBalanceWay()));//发生额
            BigDecimal balance = balanceBean.getYesterBalance().add(money);//账务明细交易后的余额
            balanceBean.setYesterBalance(balance);//设置计算后的余额
            //账务明细上的余额与重新计算后的余额不一致，则需要更新账务明细上的余额
            if (balance.compareTo(balanceVO.getBalance()) != BigDecimal.ZERO.intValue()) {
                //更新账务明细余额, update by liusy  增加原余额校验
                if (1 != overDao.updateFcAccbookDtlBalance(dtlId, balance, balanceVO.getBalance())) {
                    throw new ParameterException(CloseMessageEnum.FIN_ACCBOOKBALANCE_UPODATE_ERROR);
                }
            }
        }
    }

    /**
     * 功能介绍 : 生成单位对账单数据。<br>
     * <b>日期：</b>2017年7月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void generateCompareAcc(FcDayCloseVO dayClose) {
        List<String> accounts = fcAccService.getBlnctrAccByNetIdAndIfCompare(dayClose.getNetCode(), "1");
        if (EmptyUtils.isEmpty(accounts) || accounts.size() < 1) {
            return;
        }
        //手动生成对账单数据
        List<FcCompareAccBean> list = overDao.generateCompareAcc(dayClose, accounts);
        if (EmptyUtils.isEmpty(list) || list.size() < 1) {
            return;
        }
        //剔除已存在的企业对账数据
        doFcCompareAcc(list);
        if (list.size() < 1) {
            return;
        }
        //不存在对账单中且账务未作废，则生成对账单
        for (FcCompareAccBean compareBean : list) {
            compareBean.setCreateTime(DateUtils.getDate());
            FcCompareAccBeanDTO fcCompareAccBeanDTO = new FcCompareAccBeanDTO();
            BeanUtils.copyProperties(compareBean, fcCompareAccBeanDTO);
            compareAccService.saveCompareAccBean(fcCompareAccBeanDTO);
        }
        //反日结后，账务重新作废复核通过，则删除已作废账务的对账单数据
        // FC_COMPARE_ACC 迁移到资金核对模块
        List<String> voucherNos = fcAccbookService.getVoucherNoByNetIdAndVoucherState(dayClose.getNetCode(), "B");
        if (voucherNos != null && voucherNos.size() > 0) {
            iFcCompareAccService.deleteFcCompareAccByVoucherNoAndCloseDate(voucherNos, dayClose.getCloseDate());
        }
//        overDao.cancelCompareAcc(dayClose);

        // 1.表 FC_ACCBOOK_DTL 有netId字段，不需要查询 fc_accbook 表; TODO FC_ACCBOOK_DTL 表的 netId 字段没有值
        List<FcAccbookDtl> lists = fcAccbookDtlService.getFcAccbookDtlByNetId(dayClose.getNetCode());
        if (CollectionUtils.isEmpty(lists)) {
            return;
        }

        //	2.select b.id, b.BALANCE from FC_COMPARE_ACC b where b.TALLY_DATE = :closeDate
        //		AND b.COMP_ACC = FC_ACCBOOK_DTL.ACCOUNT
        //		AND b.LINE_NO = FC_ACCBOOK_DTL.LINE_NO
        //		AND b.BALANCE <> FC_ACCBOOK_DTL.BALANCE
        List<FcCompareAccDTO> fcCompareAccDTOList = lists.stream().map(a -> {
            FcCompareAccDTO dto = new FcCompareAccDTO();
            dto.setCompAcc(a.getAccount());
            dto.setLineNo(BigDecimal.valueOf(a.getLineNo()));
            dto.setBalance(a.getBalance());
            dto.setTallyDate(dayClose.getCloseDate());
            return dto;
        }).collect(Collectors.toList());

        List<FcCompareAccDTO> dtos = iFcCompareAccService.getBalanceByCondition(fcCompareAccDTOList);

        //	批量更新
        //	3.update FC_COMPARE_ACC set BALANCE = :balance where id = :id
        // String sql = " update fc_compare_acc set balance = "+fcCompareAccDTO.getBalance()+" where id = "+fcCompareAccDTO.getId();
        //        Map<String, Object> params = new HashMap<String, Object>();
        //        createSQLQuery(sql, params).executeUpdate();
        iFcCompareAccService.updateById(dtos);
    }

    //剔除已存在的企业对账数据
    private void doFcCompareAcc(List<FcCompareAccBean> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<FcCompareAccBeanDTO> dtos = BeanUtils.copyListProperties(list, FcCompareAccBeanDTO.class);
        List<FcCompareAccBeanDTO> compAccList = ifcCompareCenterService.getCompAccByOrderNo(dtos);
        List<FcCompareAccBean> reduce = list.stream().filter(
                a -> !compAccList.stream().map(
                                b -> b.getOrderNo() + "&" + b.getLineNo()).collect(Collectors.toList())
                        .contains(a.getOrderNo() + "&" + a.getLineNo())).collect(Collectors.toList());
        list.clear();
        list.addAll(reduce);

    }

    /**
     * 功能介绍 : 当天流水账明细转已结流水账表<br>
     * <b>日期：</b>2017年7月6日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void closeAccbook(FcDayCloseVO dayClose) {
        /**
         * 转移流水账步骤
         * 先备份到已结流水账明细数据、附加表数据、主表
         * 再删除当日流水账明细数据、附加表数据、主表
         */
        overDao.closeAccbookDtl(dayClose);
        overDao.closeAccbookExt(dayClose);
        overDao.closeAccbook(dayClose);
        overDao.deleteAccbookDtl(dayClose);
        overDao.deleteAccbookExt(dayClose);
        overDao.deleteAccbook(dayClose);
    }

    /**
     * 功能介绍 : 生成单位利息明细<br>
     * <b>日期：</b>2017年7月10日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void generateCorpInterest(FcDayCloseVO dayClose, ICurrentUser user) {
        interestDtlService.generateCorpInterest(dayClose, user);
    }

    /**
     * 功能介绍 : 记录本次日结记录<br>
     * <b>日期：</b>2017年7月10日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void saveFcCarry(FcDayCloseVO dayClose, ICurrentUser user, String overType) {
        FcCarry carry = new FcCarry();
        carry.setNetId(dayClose.getNetCode());
        carry.setCarryType(overType);//日结类型
        carry.setCarryTime(DateUtils.getDate());
        carry.setOperatorCode(user.getId());
        carry.setVoucherStartDate(DateUtils.addDay(dayClose.getLastCloseDate(), 1));
        carry.setVoucherEndDate(dayClose.getCloseDate());
        carry.setCreateBy(user.getId());
        carry.setCarryTime(DateUtils.getDate());
        carryService.saveEntity(carry);
    }

    /**
     * 功能介绍 : 更新本次日结记录<br>
     * <b>日期：</b>2017年7月10日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateNetCloseDate(FcDayCloseVO dayClose, ICurrentUser user, String overType) {
        FcDayClose fcDayClose = dayCloseDao.getDayCloseDateByType(dayClose.getNetCode(), overType);
/*		fcDayClose.setCloseDate(dayClose.getCloseDate());//设置新的已日结日期
		fcDayClose.setLastCloseDate(dayClose.getLastCloseDate());//上次日结日期
		fcDayClose.setUpdateBy(user.getId());//更新用户
		fcDayClose.setUpdateTime(DateUtils.getDate());//更新日期
		dayCloseDao.update(fcDayClose);*/
//		修改为通过sql来更新数据
        dayCloseDao.updateCloseDate(dayClose.getCloseDate(), user.getId(), dayClose.getLastCloseDate(), fcDayClose.getId());
    }

    /**
     * 功能介绍 : 反日结过程处理。<br>
     * <b>日期：</b>2017年7月11日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void exceReDayClose(FcDayCloseDTO dayClose, ICurrentUser user, boolean isPool) {
        /**
         * 1、表外账务还原
         * 2、已结流水账转当天流水账表
         * 3、删除当天账户批量余额
         * 4、删除未计息的积数
         * 5、恢复已调整积数累计
         * 6、删除科目日结单当天数据
         * 7、删除中心对账数据
         * 8、记录本次反日结信息
         * 9、还原单位利息明细
         * 10、更新日结标志
         */
        String carryType = CloseConstant.CarryType.CARRY_TYPE_DAY_OVER.get();
        reverseOutAccbook(dayClose);
        reverseAccbook(dayClose);
        deleteAccBalance(dayClose);
        deleteNoIstAccuBalance(dayClose);
        recoverAdjAccuBalance(dayClose, user, isPool);
        deleteSubjectDay(dayClose);
//		deleteCompareAcc(dayClose); 屏蔽删除对账单（反日结会删除对账单吗，导致已对账数据出现问题）
        reverseFcCarry(dayClose, user, carryType);
        deleteCorpInterest(dayClose);
        reverseDayCloseDate(dayClose, carryType, user);
    }

    /**
     * 功能介绍 : 资金池反日结过程处理。<br>
     * <b>日期：</b>2017年7月11日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void exceRePoolDayClose(FcDayCloseDTO dayClose, ICurrentUser user, boolean isPool) {
        /**
         * 2、已结流水账转当天流水账表
         * 3、删除当天账户批量余额
         * 4、删除未计息的积数
         * 5、恢复已调整积数累计
         * 6、删除科目日结单当天数据
         * 8、记录本次反日结信息
         * 10、更新日结标志
         */
        String carryType = CloseConstant.CarryType.CARRY_TYPE_POOL_OVER.get();
        reverseAccbook(dayClose);
        deleteAccBalance(dayClose);
        deleteNoIstAccuBalance(dayClose);
        recoverAdjAccuBalance(dayClose, user, isPool);
        //表已经迁移到sys 删除调用服务
        deleteSubjectDay(dayClose);
        reverseFcCarry(dayClose, user, carryType);
        reverseDayCloseDate(dayClose, carryType, user);
    }

    /**
     * 功能介绍 : 已结流水账转当天流水账表。<br>
     * <b>日期：</b>2017年7月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void reverseOutAccbook(FcDayCloseDTO dayClose) {
        //表外账务还原
        /**
         * 更新日结标识为已结标识'0'
         * Delete Fc_Out_Subject_Day
         * Delete Fc_Out_Acc_Balance
         */
        outBalanceService.reverseOutAccbookOverSign(dayClose);
        deleteSubjectDay(dayClose);
        Map<String, String> map = fcAccService.getBlnctrAccAndAccNameByNetId(dayClose.getNetCode());
        if (map.size() != 0) {
            ArrayList<String> accounts = new ArrayList<>(map.keySet());
            overDao.deleteOutAccBalance(dayClose, accounts);
        }
    }

    /**
     * 功能介绍 : 还原内部账务。<br>
     * <b>日期：</b>2017年7月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void reverseAccbook(FcDayCloseDTO dayClose) {
        /**
         * 还原流水账步骤
         * 先还原已结流水账明细数据、附加表数据、主表到当日表
         * 再删除已日结流水账明细数据、附加表数据、主表
         */
        overDao.reverseAccbookDtl(dayClose);
        overDao.reverseAccbookExt(dayClose);
        overDao.reverseAccbook(dayClose);
        overDao.deleteAccbookBlnedDtl(dayClose);
        overDao.deleteAccbookBlnedExt(dayClose);
        overDao.deleteAccbookBlned(dayClose);
    }

    /**
     * 功能介绍 : 删除当天账户批量余额。<br>
     * <b>日期：</b>2017年7月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void deleteAccBalance(FcDayCloseDTO dayClose) {
        Map<String, String> map = fcAccService.getBlnctrAccAndAccNameByNetId(dayClose.getNetCode());
        if (map.size() != 0) {
            ArrayList<String> accounts = new ArrayList<>(map.keySet());
            overDao.deleteAccBalance(dayClose, accounts);
        }
    }

    /**
     * 功能介绍 : 删除未计息的积数。<br>
     * <b>日期：</b>2017年7月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void deleteNoIstAccuBalance(FcDayCloseDTO dayClose) {
        Map<String, String> map = fcAccService.getBlnctrAccAndAccNameByNetId(dayClose.getNetCode());
        if (map.size() != 0) {
            ArrayList<String> accounts = new ArrayList<>(map.keySet());
            overDao.deleteNoIstAccuBalance(dayClose, accounts);
        }

    }

    /**
     * 功能介绍 : 恢复已调整积数累计。<br>
     * <b>日期：</b>2017年7月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void recoverAdjAccuBalance(FcDayCloseDTO dayClose, ICurrentUser user, boolean isPool) {
        /**
         * 协定积数暂时未处理，待后续上理财管理再处理协定积数
         */
        FcDayCloseVO dayCloseVO = new FcDayCloseVO();
        BeanUtils.copyProperties(dayClose, dayCloseVO);
        accuBalanceService.recoverAdjAccuBalance(dayCloseVO, user, isPool);
    }

    /**
     * 功能介绍 : 删除科目日结单当天数据。<br>
     * <b>日期：</b>2017年7月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void deleteSubjectDay(FcDayCloseDTO dayClose) {
        iFcSubjectDayService.deleteSubjectDay(dayClose);
    }

    /**
     * 功能介绍 : 删除中心对账数据。<br>
     * <b>日期：</b>2017年7月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void deleteCompareAcc(FcDayCloseDTO dayClose) {
        Map<String, String> map = fcAccService.getBlnctrAccAndAccNameByNetId(dayClose.getNetCode());
        if (map.size() != 0) {
            ArrayList<String> accounts = new ArrayList<>(map.keySet());
            overDao.deleteCompareAcc(dayClose, accounts);
        }

    }

    /**
     * 功能介绍 : 记录本次反日结信息。<br>
     * <b>日期：</b>2017年7月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void reverseFcCarry(FcDayCloseDTO dayClose, ICurrentUser user, String carryType) {
        FcCarry carry = carryService.queryNoReverseFcCarries(dayClose.getNetCode(), carryType);
        carry.setRestoreCarryOptor(user.getId());//反日结操作用户
        carry.setRestoreCarryTime(DateUtils.getDate());//反日结时间
        carry.setUpdateBy(user.getId());
        carry.setUpdateTime(DateUtils.getDate());
        carryService.updateEntity(carry);
    }

    /**
     * 功能介绍 : 还原单位利息明细。<br>
     * <b>日期：</b>2017年7月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void deleteCorpInterest(FcDayCloseDTO dayClose) {
        overDao.deleteCorpInterest(dayClose);
    }

    /**
     * 功能介绍 : 更新日结日期。<br>
     * <b>日期：</b>2017年7月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void reverseDayCloseDate(FcDayCloseDTO dayClose, String carryType, ICurrentUser user) {
        //新的未日结记录
        FcCarry carry = carryService.queryNoReverseFcCarries(dayClose.getNetCode(), carryType);
        FcDayClose fcDayClose = dayCloseDao.getDayCloseDateByType(dayClose.getNetCode(), carryType);
        if (null != carry) {
            fcDayClose.setCloseDate(carry.getVoucherEndDate());//最新的未日结记录
            fcDayClose.setLastCloseDate(carry.getVoucherStartDate());
        } else {
            fcDayClose.setCloseDate(dayClose.getLastCloseDate());//最新的未日结记录
            fcDayClose.setLastCloseDate(dayClose.getLastCloseDate());//最新的未日结记录
        }
//		dayCloseDao.update(fcDayClose);
        dayCloseDao.updateCloseDate(fcDayClose.getCloseDate(), user.getId(), fcDayClose.getLastCloseDate(), fcDayClose.getId());
    }

    /**
     * 功能介绍 : 是否存在损益类科目不为零。<br>
     * <b>日期：</b>2017年7月17日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public boolean isIncomBlanceZero(FcMonthCloseDTO closeDTO) {
        return iFcSubjectDayService.isIncomBlanceZero(closeDTO);
    }

    /**
     * 功能介绍 : 月结过程处理。<br>
     * <b>日期：</b>2017年7月11日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void exceMonthClose(FcMonthCloseDTO closeDTO, ICurrentUser user) {
        /**
         * 1、产生月总账
         * 2、产生资产负债表
         * 3、产生资产损益表
         * 4、产生现金流量表
         * 5、插入结转记录
         * 6、更新月结完成月份
         */
        List<FcMonthLedger> monthLedgers = generateMonthLedger(closeDTO);
        if (null == monthLedgers || monthLedgers.isEmpty()) {
            //return;
        }
        generateBalanceSheet(closeDTO);//资产负债表处理
        generateIncomeStatement(closeDTO);//资产损益表处理
        generateCashFlow(closeDTO, user);//现金流量处理
        saveMonthCarry(closeDTO, user);//记录本次月结记录
        updateMonthCloseDate(closeDTO, user);//更新本次月结记录
    }

    /**
     * 功能介绍 : 产生月总账。<br>
     * <b>日期：</b>2017年7月17日<br>
     *
     * @return
     * @version 1.0
     * @author liyh
     */
    public List<FcMonthLedger> generateMonthLedger(FcMonthCloseDTO closeDTO) {
        return monthLedgerService.generateMonthLedger(closeDTO);
    }

    /**
     * 功能介绍 : 资产负债表处理。<br>
     * <b>日期：</b>2017年7月17日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void generateBalanceSheet(FcMonthCloseDTO closeDTO) {
        sheetService.generateReportDate(closeDTO);
    }

    /**
     * 功能介绍 : 资产损益表处理。<br>
     * <b>日期：</b>2017年7月17日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void generateIncomeStatement(FcMonthCloseDTO closeDTO) {
        incomeService.generateIncomeReportDate(closeDTO);
    }

    /**
     * 功能介绍 : 现金流量处理。<br>
     * <b>日期：</b>2017年7月17日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void generateCashFlow(FcMonthCloseDTO closeDTO, ICurrentUser user) {
        Date startDate = DateUtils.parse(closeDTO.getCloseYear() + "-" + (closeDTO.getCloseMonth() > 9 ? closeDTO.getCloseMonth() : "0" + closeDTO.getCloseMonth()) + "-" + "01", "yyyy-MM-dd");
        Date endDate = DateUtils.addDay(DateUtils.addMonth(startDate, 1), -1);
        List<FcCashFlow> fcCashFlows = balanceService.listDataByMonth(closeDTO.getNetCode(), startDate, endDate);
//		先删除旧数据在保存
        cashFlowService.deleteByMonth(closeDTO.getNetCode(), startDate, endDate);
        if (ListUtils.isNotNullOrEmpty(fcCashFlows)) {
            for (FcCashFlow fcCashFlow : fcCashFlows) {
                fcCashFlow.setCreateBy(user.getId());
                fcCashFlow.setCreateTime(new Date());
            }
            cashFlowService.batchSave(fcCashFlows);
        }
    }

    /**
     * 功能介绍 : 记录本次月结记录<br>
     * <b>日期：</b>2017年7月18日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void saveMonthCarry(FcMonthCloseDTO closeDTO, ICurrentUser user) {
        FcCarry carry = new FcCarry();
        carry.setNetId(closeDTO.getNetCode());
        carry.setCarryType(CloseConstant.CarryType.CARRY_TYPE_MONTH_OVER.get());//月结类型
        carry.setCarryTime(DateUtils.getDate());
        carry.setOperatorCode(user.getId());
        carry.setVoucherStartDate(closeDTO.getCloseDate());//月结开始日期
        carry.setVoucherEndDate(closeDTO.getEndDate());//月结结束日期
        carry.setCreateBy(user.getId());
        carry.setCarryTime(DateUtils.getDate());
        carryService.saveEntity(carry);
    }

    /**
     * 功能介绍 : 更新本次月结记录<br>
     * <b>日期：</b>2017年7月18日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void updateMonthCloseDate(FcMonthCloseDTO closeDTO, ICurrentUser user) {
        FcDayClose fcDayClose = dayCloseDao.getMonthCloseDate(closeDTO.getNetCode());
        Calendar cal = Calendar.getInstance();
        cal.setTime(fcDayClose.getCloseDate());
        cal.add(Calendar.MONTH, 1);//年月+1
        fcDayClose.setCloseDate(cal.getTime());//设置新的已月结日期
        fcDayClose.setLastCloseDate(fcDayClose.getCloseDate());//上次月结日期
        fcDayClose.setUpdateBy(user.getId());//更新用户
        fcDayClose.setUpdateTime(DateUtils.getDate());//更新日期
        dayCloseDao.update(fcDayClose);
    }

    /**
     * 功能介绍 : 反日结过程处理。<br>
     * <b>日期：</b>2017年7月11日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void exceReMonthClose(FcMonthCloseDTO closeDTO, ICurrentUser user) {
        /**
         * 1、转移月总账数据
         * 2、转移月总账总量数据
         * 3、转移资产负债数据
         * 4、转移资产损益数据
         * 5、恢复现金流量明细
         * 6、记录本次反月结信息
         * 7、更新月结标志
         */
        monthLedgerService.reverseMonthLedger(closeDTO);
        sheetService.reverseBalanceSheet(closeDTO);
        incomeService.reverseIncomeStatement(closeDTO);
        reverseMonthCarry(closeDTO, user);
        reverseMonthCloseDate(closeDTO, user);
    }

    /**
     * 功能介绍 : 记录本次反月结信息。<br>
     * <b>日期：</b>2017年7月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void reverseMonthCarry(FcMonthCloseDTO closeDTO, ICurrentUser user) {
        FcCarry carry = carryService.queryNoReverseFcCarries(closeDTO.getNetCode(),
                CloseConstant.CarryType.CARRY_TYPE_MONTH_OVER.get());//月结记录
        carry.setRestoreCarryOptor(user.getId());//反日结操作用户
        carry.setRestoreCarryTime(DateUtils.getDate());//反日结时间
        carry.setUpdateBy(user.getId());
        carry.setUpdateTime(DateUtils.getDate());
        carryService.updateEntity(carry);
    }

    /**
     * 功能介绍 : 更新日结日期。<br>
     * <b>日期：</b>2017年7月14日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void reverseMonthCloseDate(FcMonthCloseDTO closeDTO, ICurrentUser user) {
        FcDayClose fcDayClose = dayCloseDao.getMonthCloseDate(closeDTO.getNetCode());
        //月份减1
        Calendar cal = Calendar.getInstance();
        cal.setTime(fcDayClose.getCloseDate());
        cal.add(Calendar.MONTH, -1);//年月-1
        fcDayClose.setCloseDate(cal.getTime());//设置新的已月结日期
        fcDayClose.setLastCloseDate(fcDayClose.getCloseDate());//上次月结日期
        fcDayClose.setUpdateBy(user.getId());//更新用户
        fcDayClose.setUpdateTime(DateUtils.getDate());//更新日期
        dayCloseDao.update(fcDayClose);
    }

    /**
     * 功能介绍 : 跨日日结是否存在未结转的账务。<br>
     * <b>日期：</b>2017年7月19日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public boolean existAccbookOverDays(FcDayCloseVO closeVO) {
        return overDao.existAccbookOverDays(closeVO);
    }

    /**
     * 功能介绍 : 日结过程处理。<br>
     * <b>日期：</b>2017年7月11日<br>
     *
     * @version 1.0
     * @author liyh
     */
    public void excePoolDayClose(FcDayCloseVO closeVO, ICurrentUser user) {
        /**
         * 日结过程：
         * 2、查询流水号的借贷方金额是否平衡
         * 3、检查各科目的复核联机余额借贷平衡
         * 3.1、调整账户余额不平的记录
         * 5、生成每日余额和科目日结单
         * 6、各科目借贷批量余额借贷是否平衡
         * 7、检查账户的联机笔数与批量笔数是否平衡(实时余额的笔数=日结日的笔数+账务笔数)
         * 8、检查账户的联机复核余额与批量余额是否相等(实时余额=日结日的余额+账务金额)
         * 9、生成科目日结单（第5点已产生）
         * 10、产生积数余额表上个工作日以后至当天的积数(包含协定积数)
         * 11、在转移流水账之前更新流水账余额(保证流水账余额按照复核顺序变化)
         * 13、当天流水账转已结流水账表
         * 14、记录本次日结记录信息
         * 16、更新网点日结日期
         */
        String overType = CloseConstant.CarryType.CARRY_TYPE_POOL_OVER.get();
        String netId = closeVO.getNetCode();
        Date closeDate = closeVO.getCloseDate();
        Date lastCloseDate = closeVO.getLastCloseDate();
        checkMoneyGroupByOrderNo(closeVO);
        checkSubjectMoney(closeVO, true);

        List<FcAccDTO> fcAccDTOS = fcAccService.getFcAccByNetId(netId);
        List<String> accList = null;
        if (ListUtils.isNotNullOrEmpty(fcAccDTOS)) {
            accList = fcAccDTOS.stream().map(FcAccDTO::getBlnctrAcc).collect(Collectors.toList());
            //查询账务相关明细数据
            List<AccbookBalanceVO> accbookBalanceVOs = overDao.queryAccbookBalance(netId, closeDate, accList);
            //TODO 账务明细数据属性填充
            fullAccbookBalProperties(accbookBalanceVOs, fcAccDTOS);
            if (null == accbookBalanceVOs) {
                accbookBalanceVOs = new ArrayList<AccbookBalanceVO>();
            }
            //最后一日的账户余额数据
            List<AccBalanceBean> dayBalList = generateAccBalance(netId, closeDate, lastCloseDate, accbookBalanceVOs, user);
            checkAccBalanceMoney(closeVO);
            checkAccountCnt(closeVO);
            checkAccountBalance(closeVO);
            generatePoolAccuBalance(closeVO, user);
            updateAccbookDtlBalance(closeVO, dayBalList, accbookBalanceVOs);
            closeAccbook(closeVO);
            saveFcCarry(closeVO, user, overType);
            updateNetCloseDate(closeVO, user, overType);
        }
    }

    /**
     * 日初处理
     */
    public void disposeAgreeAccu(FcDayStartVO dayStart, ICurrentUser user) {
        FcDayCloseVO closeVO = new FcDayCloseVO();
        closeVO.setCloseDate(dayStart.getStartDate());
        closeVO.setLastCloseDate(dayStart.getLastStartDate());
        closeVO.setNetCode(dayStart.getNetCode());
        //accuBalanceService.disposeAgreeAccu(closeVO,user);
        String overType = CloseConstant.CarryType.CARRY_TYPE_DAY_START.get();
        saveFcCarry(closeVO, user, overType);
        updateNetStartDate(dayStart, user);
    }

    /**
     * 反日初
     */
    public void ReverseDayStart(FcDayStartVO dayStart, ICurrentUser user) {
        FcDayCloseVO closeVO = new FcDayCloseVO();
        closeVO.setCloseDate(dayStart.getStartDate());
        closeVO.setLastCloseDate(dayStart.getLastStartDate());
        closeVO.setNetCode(dayStart.getNetCode());
        FcDayCloseDTO closeDto = new FcDayCloseDTO();
        closeDto.setCloseDate(dayStart.getStartDate());
        closeDto.setLastCloseDate(dayStart.getLastStartDate());
        closeDto.setNetCode(dayStart.getNetCode());
        //把活期账号日初的协定积数删除，重新生成协定账号的协定积数
        //accuBalanceService.ReverseDayStartAgreeAccu(closeVO,user);
        String carryType = CloseConstant.CarryType.CARRY_TYPE_DAY_START.get();
        reverseFcCarry(closeDto, user, carryType);
        reverseDayStartDate(dayStart, carryType);
    }

    /**
     * 功能介绍 : 更新本次日初记录<br>
     */
    public void updateNetStartDate(FcDayStartVO dayStart, ICurrentUser user) {
        FcDayStart fcDayStart = dayStartDao.getDayStartDateByType(dayStart.getNetCode());
        if (fcDayStart == null) {
            fcDayStart = new FcDayStart();
            fcDayStart.setNetCode(dayStart.getNetCode());
        }
        fcDayStart.setStartDate(dayStart.getStartDate());//设置新的已日结日期
        fcDayStart.setLastStartDate(dayStart.getLastStartDate());//上次日结日期
        fcDayStart.setUpdateBy(user.getId());//更新用户
        fcDayStart.setUpdateTime(DateUtils.getDate());//更新日期
        dayStartDao.saveOrUpdate(fcDayStart);
    }

    /**
     * 功能介绍 : 更新日初日期。
     */
    public void reverseDayStartDate(FcDayStartVO dayStart, String carryType) {
        //新的未日结记录
        FcCarry carry = carryService.queryNoReverseFcCarries(dayStart.getNetCode(), carryType);
        FcDayStart fcDayClose = dayStartDao.getDayStartDateByType(dayStart.getNetCode());
        if (null != carry) {
            fcDayClose.setStartDate(carry.getVoucherEndDate());//最新的未日初记录
            fcDayClose.setLastStartDate(carry.getVoucherStartDate());
        } else {
            fcDayClose.setStartDate(dayStart.getLastStartDate());//最新的未日初记录
            fcDayClose.setLastStartDate(dayStart.getLastStartDate());//最新的日初记录
        }
        dayStartDao.update(fcDayClose);
    }
}
