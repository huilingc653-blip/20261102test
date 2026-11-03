package com.bytter.biz.finacial.interest.service.proccessor;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.finacial.accbook.entity.FcAccbook;
import com.bytter.biz.finacial.accbook.entity.FcAccbookDtl;
import com.bytter.biz.finacial.accbook.service.FcAccbookService;
import com.bytter.biz.finacial.accbook.vo.FcAccbookVoucherVO;
import com.bytter.biz.finacial.accbook.vo.VoucherWrapper;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.biz.finacial.api.constant.SubjectConstant;
import com.bytter.biz.finacial.api.dto.FcBusinessRuleDefinePoolDTO;
import com.bytter.biz.finacial.api.dto.FcVoucherEntryDefinePoolDTO;
import com.bytter.biz.finacial.api.enums.interest.InterestMessageEnum;
import com.bytter.biz.finacial.api.enums.interest.InterestProcessType;
import com.bytter.biz.finacial.api.enums.interest.OperateType;
import com.bytter.biz.finacial.api.service.IFcBusinessRuleDefineService;
import com.bytter.biz.finacial.inneraccount.service.FcOnlineLedgerService;
import com.bytter.biz.finacial.interest.bean.*;
import com.bytter.biz.finacial.interest.dao.FcInterestDao;
import com.bytter.biz.finacial.interest.dao.FcPredrawRushDtlDao;
import com.bytter.biz.finacial.interest.dto.FcPredrawRushDtlDTO;
import com.bytter.biz.finacial.interest.entity.*;
import com.bytter.biz.finacial.interest.service.FcEntrustLoanRepayService;
import com.bytter.biz.finacial.interest.service.FcInterestDtlService;
import com.bytter.biz.finacial.interest.utils.InterestHelper;
import com.bytter.biz.finacial.interest.vo.FcFinacialAccbookDtlVO;
import com.bytter.biz.finacial.interest.vo.FcFinacialAccbookVO;
import com.bytter.biz.finacial.interest.vo.FcPredrawDtlTempVO;
import com.bytter.biz.finacial.interest.vo.InterestInfoContainer;
import com.bytter.biz.finacial.paramconfig.service.FcBusRacePoolService;
import com.bytter.biz.finacial.paramconfig.service.FcBusRaceService;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.ConstantBean;
import com.bytter.core.utils.*;
import com.bytter.finance.api.ICmsLoanService;
import com.bytter.increment.api.service.IFcAgreedDepositService;
import com.bytter.increment.api.service.IFcAgreedDetailService;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * <br>
 * <b>类功能：</b>利息计息适配器基类<br>
 * <b>作者：</b>caozx<br>
 * <b>日期：</b> 2017年7月11日 <br>
 * <b>版权所有：<b>版权所有(C) 2017，拜特科技<br>
 */
@Service("BaseInterestProvider")
@Transactional
public abstract class BaseInterestProvider implements IInterestProvider {
    /**
     *  日志对象
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 根据系统利率类型转换日利率的除数
     */
    protected BigDecimal sysRateType = new BigDecimal(InterestConstant.RATE_YEAR_DIV);
    private Map<String, BigDecimal> rushMap = new HashMap<String, BigDecimal>();
    @DubboReference(check = false)
    protected IFcAccService iFcAccService;
    @Autowired
    private FcBusRaceService fcBusRaceService;
    @Autowired
    private FcBusRacePoolService fcBusRacePoolService;
    @DubboReference(check = false)
    protected IFcSubjectService iFcSubjectService;
    @DubboReference(check = false)
    protected ICorpService iCorpService;
    @DubboReference(check = false)
    protected IFcCorpPoolService corpPoolService;
    @DubboReference(check = false)
    protected IBtCurrencyService iBtCurrencyService;
    @Autowired
    protected FcAccbookService fcAccbookService;
    @Autowired
    protected FcOnlineLedgerService iFcOnlineLedgerService;
    @Autowired
    protected FcInterestDao fcInterestDao;
    @DubboReference(check = false)
    protected ICmsLoanService iCmsLoanService;
    @DubboReference(check = false)
    protected IInnerAccountService innerAccountService;
    @Autowired
    protected FcInterestDtlService fcInterestDtlService;
    @DubboReference(check = false)
    protected IFcVoucherTypeService iFcVoucherTypeService;
    @DubboReference(check = false)
    protected IFcBusinessRuleDefineService ruleDefineService;
    @DubboReference(check = false)
    protected IConfigService configService;
    @DubboReference(check = false)
    protected IFcAgreedDepositService agreedDepositService;
    @DubboReference(check = false)
    protected IFcAgreedDetailService agreedDetailService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
    IInputBankInfoService iInputBankInfoService;
    @Autowired(required = false)
    private FcEntrustLoanRepayService loanRepayService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private IFcInstrateAdjustService iFcInstrateAdjustService;

    @Autowired
    private FcPredrawRushDtlDao fcPredrawRushDtlDao;
    public IMultiLangService getIMultiLangService(){
        return multiLangServiceI;
    }
    /**
     * <b>功能介绍：</b>获取系统参数-批量计息是否自动复核<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    public boolean isBatchInstAutoCheck(){
        return configService.getConfig("ist", "batchInstAutoCheck").toInteger().intValue()==1;
    }
    protected String operatorCode;
    protected String tallyCode;
    public String getOperatorCode() {
        return operatorCode;
    }
    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }
    public String getTallyCode() {
        return tallyCode;
    }
    @Override
    public List<FcAccuBalancef> queryAccuBalanceInfos(String netCode, String accTypeCode, Date endInstDate) {
        return fcInterestDao.queryAccuBalanceForHave(netCode, new String[] { accTypeCode }, endInstDate);
    }
    public BigDecimal queryAccuBalanceByDate(String account,Date endInstDate, Date interestStartDate) {
        return fcInterestDao.queryAccuBalanceByDate(account,endInstDate, interestStartDate);
    }
    @Override
    public List<FcAccuBalancef> queryAccAccuBalance(String account, Date endInstDate) {
        return fcInterestDao.queryAccuBalanceForAcc(account, endInstDate);
    }
    @Override
    public List<AccInstrateBean> queryInstRateAdjustInfos(String netCode, String accTypeCode, Date endInstDate, Integer[] corpIds, String account) {
//        fcInterestDao.queryInstRateAdjustInfos(netCode, accTypeCode, endInstDate, corpIds, account);
        List<com.bytter.system.api.bean.AccInstrateBean> accInstrateBeans = iFcInstrateAdjustService.queryInstRateAdjustInfos(netCode, accTypeCode, endInstDate, corpIds, account);
        List<AccInstrateBean> accInstrateBeans1 = BeanUtils.copyListProperties(accInstrateBeans, AccInstrateBean.class);
        return accInstrateBeans1;
    }
    @Override
    public List<AccInstrateZoneBean> queryInstZoneAdjustInfos(String netCode, String accTypeCode, Date endInstDate, Integer[] corpIds) {
        //TODO 查询计息账户的余额区间调整记录
        return null;
    }
    /**
     * <b>功能介绍：</b>保存明细到临时表<br>
     * <b>日期：</b>2017年7月11日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    @Override
    public void saveTempInterest(AccInterestInfo accInterestInfo, String netId, ICurrentUser user, String generateMode) {
        if (accInterestInfo.getInterestDtls().size() > 0) {
            beforeSaveInterest(accInterestInfo.getInterestDtls());
            List<FcInterestDtlTemp> interestDtls = accInterestInfo.getInterestDtls();
            this.fillFcInterestDtlTempData(interestDtls, user);
            fcInterestDao.addBatchByHql(interestDtls);
            //发送消息，TODO 后续需要和老T6一致，当前设置 个别计息 没有消息复核提示
            if (!StringUtils.equals(generateMode, InterestConstant.GENERATEMODE_INTEREST_PART)) {
                for(FcInterestDtlTemp fcInterestDtlTemp : accInterestInfo.getInterestDtls()){
                    if(fcInterestDtlTemp.getBusStatus().equals(InterestConstant.INTEREST_WAIT_CHECK)) {
                        notificationService.sendIgnoreMySelf(fcInterestDtlTemp.getId(), netId, NotificationConstant.FINACIAL.INTEREST_CHECK, fcInterestDtlTemp.getOperatorCode());
                    }
                }
            }

        }
        if (accInterestInfo.getPredrawDtls()!=null && accInterestInfo.getPredrawDtls().size() > 0) {
            for (FcPredrawDtlTempVO predrawDtl : accInterestInfo.getPredrawDtls()) {
                predrawDtl.setInterest(predrawDtl.getInterest().subtract(BigDecimal.ZERO));
                beforeSaveInterest(predrawDtl.getInterestDtls());
                List<FcInterestDtlTemp> interestDtls = predrawDtl.getInterestDtls();
                this.fillFcInterestDtlTempData(interestDtls, user);
                fcInterestDao.addBatchByHql(interestDtls);
            }
            List<FcPredrawDtlTemp> fcPredrawDtlTemp = BeanUtils.copyListProperties(accInterestInfo.getPredrawDtls(),FcPredrawDtlTemp.class);
            fcInterestDao.addBatchByHql(fcPredrawDtlTemp);
            accInterestInfo.setPredrawDtls(BeanUtils.copyListProperties(fcPredrawDtlTemp,FcPredrawDtlTempVO.class));
        }
    }
    private void fillFcInterestDtlTempData(List<FcInterestDtlTemp> fcInterestDtlTemps, ICurrentUser userBean ){
        List<String> accounts = fcInterestDtlTemps.stream().map(FcInterestDtlTemp::getAccount).collect(Collectors.toList());
        List<FcAccDTO> fcAccDTOS = iFcAccService.getFcAccByBlnctrAccs(accounts);
        Map<String, FcAccDTO> map = fcAccDTOS.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, Function.identity()));

        fcInterestDtlTemps.forEach(a -> {
            BtCurrencyDTO btCurrencyDTO = new BtCurrencyDTO();
            if (StringUtils.isBlank(a.getCurCode())){
                String curId = map.get(a.getAccount()).getCurId();
                btCurrencyDTO = iBtCurrencyService.getObjectById(curId);
            }
            if (StringUtils.isNotBlank(a.getCurCode())){
                btCurrencyDTO = iBtCurrencyService.getObjectById(a.getCurCode());
            }
            if (!Objects.isNull(btCurrencyDTO)){
                a.setCurId(btCurrencyDTO.getId());
                a.setCurName(btCurrencyDTO.getCurName());
                a.setCurCode(btCurrencyDTO.getCurCode());
            }
            FcAccDTO fcAccDTO = map.get(a.getAccount());
            if (fcAccDTO != null){
                a.setAccName(fcAccDTO.getAccName());
                a.setAccType(fcAccDTO.getAccType());
                a.setNetId(fcAccDTO.getNetId());
                a.setCorpId(fcAccDTO.getCorpId());
            }
            a.setOperatorCode(userBean.getId());
        });
    }
    /**
     * <b>功能介绍：</b>利息明细保存前设置<br>
     * <b>日期：</b>2017年7月11日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    private void beforeSaveInterest(List<FcInterestDtlTemp> interestDtls) {
        for (FcInterestDtlTemp interestDtl : interestDtls) {
            interestDtl.setCumulation(interestDtl.getCumulation().subtract(BigDecimal.ZERO));
        }
    }
    @Override
    public void processInterestDtls(AccInterestInfo accInterestInfo) {
        InterestInfoContainer container = accInterestInfo.getContainer();
        String netCode = container.getNetCode();
        //结转利息数据
        List<FcInterestDtlTemp> interestDtlTemps = accInterestInfo.getInterestDtls();
        if (interestDtlTemps != null && interestDtlTemps.size() > 1) {
            Collections.sort(interestDtlTemps, new Comparator<FcInterestDtlTemp>() {
                @Override
                public int compare(FcInterestDtlTemp o1, FcInterestDtlTemp o2) {
                    if (o1.getAccount().equals(o2.getAccount())) {
                        return o1.getInterestStartDate().compareTo(o2.getInterestStartDate());
                    }
                    return o1.getAccount().compareTo(o2.getAccount());
                }
            });
        }
        List<String> interestDtlIds = new ArrayList<String>();
        if (interestDtlTemps != null && interestDtlTemps.size() > 0) {
            for (FcInterestDtlTemp interestDtlTemp : interestDtlTemps) {
                interestDtlIds.add(interestDtlTemp.getId());
            }
        }
        List<FcInterestDtl> interestDtls = copyInterestDtls(interestDtlTemps, container);
        this.fillData(interestDtls);
        fcInterestDao.addBatchByHql(interestDtls);
        //结转预提数据
        List<FcPredrawDtlTempVO> predrawDtlTemps = accInterestInfo.getPredrawDtls();
        List<String> predrawIds = new ArrayList<String>();
        if (predrawDtlTemps != null && predrawDtlTemps.size() > 0) {
            List<FcPredrawDtl> predrawDtls = new ArrayList<FcPredrawDtl>();
            for (FcPredrawDtlTempVO predrawDtlTemp : predrawDtlTemps) {
                FcPredrawDtl predrawDtl = new FcPredrawDtl();
                BeanUtils.copyProperties(predrawDtlTemp,predrawDtl);
                predrawDtl.setId(null);
                predrawDtl.setInterestDtls(copyInterestDtls(predrawDtlTemp.getInterestDtls(), container));
                List<FcInterestDtl> interestDtlsTemp = predrawDtl.getInterestDtls();
                if (interestDtlsTemp != null && interestDtlsTemp.size() > 0) {
                    if (StringUtils.isNotEmpty(interestDtlsTemp.get(0).getOrderNo())) {
                        predrawDtl.setOrderNo(interestDtlsTemp.get(0).getOrderNo());
                    }
                }
                if (StringUtils.isEmpty(predrawDtl.getOrderNo())) {//凭证流水号为空，则生成虚拟流水号，供冲预提使用
                    String orderNo = DateUtils.date2Str(predrawDtl.getInterestTime(), DateUtils.yyyymmddhhmmss);
                    predrawDtl.setOrderNo(orderNo + iCorpService.get(netCode).getCode());//预提流水号设置
                }
                predrawDtls.add(predrawDtl);
                List<FcInterestDtl> interestDtls1 = predrawDtl.getInterestDtls();
                this.fillData(interestDtls1);
                fcInterestDao.addBatchByHql(interestDtls1);
                predrawIds.add(predrawDtlTemp.getId());
            }

            String fcAccIdS = "";
            for (FcPredrawDtl vo : predrawDtls) {
                fcAccIdS+="'"+vo.getAccount()+"',";
            }
            String condition=" and t.blnctr_acc in("+fcAccIdS.substring(0,fcAccIdS.length()-1)+")";
            List<FcAccDTO> fcAccDTOS = iFcAccService.queryBySomeCondPgd(condition);
            Map<String, FcAccDTO> map = fcAccDTOS.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, Function.identity()));
            predrawDtls.forEach(a -> {
                FcAccDTO fcAccDTO = map.get(a.getAccount());
                a.setAccName(fcAccDTO.getAccName());
                a.setAccType(fcAccDTO.getAccType());
                a.setNetId(fcAccDTO.getNetId());
                a.setCorpId(fcAccDTO.getCorpId());
                a.setCorpName(fcAccDTO.getCorpName());
                a.setCurId(fcAccDTO.getCurId());
                a.setCurCode(fcAccDTO.getCurCode());
                a.setCurName(fcAccDTO.getCurName());
            });
            fcInterestDao.addBatchByHql(predrawDtls);
        }
        fcInterestDao.deleteCheckedInstsAndPres(interestDtlIds, predrawIds);
    }
    private void fillData(List<FcInterestDtl> fcInterestDtls){
        List<String> accounts = fcInterestDtls.stream().map(FcInterestDtl::getAccount).collect(Collectors.toList());
        List<FcAccDTO> fcAccDTOS = iFcAccService.getFcAccByBlnctrAccs(accounts);
        Map<String, FcAccDTO> map = fcAccDTOS.stream().collect(Collectors.toMap(FcAccDTO::getBlnctrAcc, Function.identity()));
        fcInterestDtls.forEach(a -> {
            FcAccDTO fcAccDTO = map.get(a.getAccount());
            a.setAccName(fcAccDTO.getAccName());
            a.setAccType(fcAccDTO.getAccType());
            a.setNetId(fcAccDTO.getNetId());
            a.setCorpId(fcAccDTO.getCorpId());
            a.setCurId(fcAccDTO.getCurId());
            a.setCurName(fcAccDTO.getCurName());
        });
    }
    /**
     * <b>功能介绍：</b>复制利息数据<br>
     * <b>日期：</b>2017年9月11日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     * @param interestDtlTemps
     * @param container
     * @return
     * @throws Exception
     */
    private List<FcInterestDtl> copyInterestDtls(List<FcInterestDtlTemp> interestDtlTemps, InterestInfoContainer container) {
        String userCode = "";//container.getUserBean().getId();
        if (interestDtlTemps != null && interestDtlTemps.size() > 1) {
            Collections.sort(interestDtlTemps, new Comparator<FcInterestDtlTemp>() {
                @Override
                public int compare(FcInterestDtlTemp o1, FcInterestDtlTemp o2) {
                    if (o1.getAccount().equals(o2.getAccount())) {
                        return o1.getInterestStartDate().compareTo(o2.getInterestStartDate());
                    }
                    return o1.getAccount().compareTo(o2.getAccount());
                }
            });
        }
        List<FcInterestDtl> interestDtls = new ArrayList<FcInterestDtl>();
        if (interestDtlTemps != null && interestDtlTemps.size() > 0) {
            // 获取UserBean信息
            if (StringUtils.isEmpty(userCode)) {
//                userCode = fcInterestDtlService.getUserVo().getId();
            }
            for (FcInterestDtlTemp interestDtlTemp : interestDtlTemps) {
                FcInterestDtl interestDtl = new FcInterestDtl();
                BeanUtils.copyProperties(interestDtlTemp,interestDtl);
                interestDtl.setId(null);
                interestDtl.setBusStatus(InterestConstant.INTEREST_STATUE_CHECK);
                interestDtl.setPredrawDtlId(null);
                if (StringUtils.isEmpty(interestDtl.getOrderNo())) {//凭证流水号为空，则生成虚拟流水号，供冲预提使用
                    String orderNo = DateUtils.date2Str(interestDtl.getInterestTime(), DateUtils.yyyymmddhhmmss);
                    String netCode = "";
                    if (container.isPoolSign()) {
                        FcCorpPoolDTO corpPoolDTO = corpPoolService.getById(container.getNetCode());
                        if (null != corpPoolDTO) {
                            netCode = corpPoolDTO.getPoolCode();
                        }
                    } else {
                        CorpDTO corpDTO = iCorpService.get(container.getNetCode());
                        if (null != corpDTO) {
                            netCode = corpDTO.getCode();
                        }
                    }
                    interestDtl.setOrderNo(orderNo + netCode);////利息流水号设置
                }
                interestDtl.setCheckerCode(userCode);
                interestDtl.setCheckDate(DateUtils.getDate());
                interestDtls.add(interestDtl);
            }
        }
        return interestDtls;
    }
    /**
     * <b>功能介绍：</b>查询未冲销的预提信息<br>
     * <b>日期：</b>2017年11月2日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     * @param netCode
     * @param accTypeCode
     * @param curCode
     * @param endDate
     * @return
     */
    @Override
    public List<FcPredrawDtl> queryUnRushPredrawDtls(String netCode, String accTypeCode, String curCode, Date endDate) {
        InterestCommonBean bean = new InterestCommonBean(netCode, accTypeCode, null, endDate, "");
        return fcInterestDao.queryUnRushPredrawDtls(bean, curCode, "temp");
    }
    /**
     * <b>功能介绍：</b>创建预提凭证<br>
     * <b>日期：</b>2017年11月2日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     * @param accInterestInfo
     */
    @Override
    public void createPredrawVoucher(AccInterestInfo accInterestInfo) {
        List<FcInterestDtlTemp> interestDtls = accInterestInfo.getInterestDtls();
        InterestParamBean paramBean = new InterestParamBean(true, accInterestInfo.getContainer().isAccInCenter(),
                accInterestInfo.getContainer().isMoreVoucherDtl());
        Map<String, FcFinacialAccbookVO> accbooksMap = createCommonVoucher(interestDtls,
                accInterestInfo.getContainer().getTallyDate(),
                /*accInterestInfo.getContainer().getUserBean().getId()*/"", paramBean); // TODO
        if (accbooksMap != null) {
            accInterestInfo.setPredrawAccbook(new ArrayList<FcFinacialAccbookVO>(accbooksMap.values()));
        }
    }
    /**
     * <br>
     * <b>功能：</b>设置记账人<br>
     * <b>作者：</b>caozx<br>
     * <b>日期：</b> 2013-3-1 <br>
     * @param userCode
     */
    @Override
    public void setAccbookTallyCode(String userCode){
        this.tallyCode = userCode;
    }
    /**
     * <b>功能介绍：</b>冲预提<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    @Override
    public void rushPredrawn(AccInterestInfo accInterestInfo) {
        InterestInfoContainer container = accInterestInfo.getContainer();
        String opCode="";
        if(container.getUserBean()==null){
            opCode = container.getAccBean().getNetCode();
            setOperatorCode(opCode);
        }else {
            opCode = container.getUserBean().getId();
            setOperatorCode(opCode);  //复核的时候，不是使用当前登录用户数据
        }
        String netCode = accInterestInfo.getAccTypeKeyBean().getnetId();
        String curCode = accInterestInfo.getAccTypeKeyBean().getcurId();
        BigDecimal rushMoney = container.getRushMoney();//手工冲预提金额
        BigDecimal tellerMoney = container.getTellerMoney();//手工取款金额
        getAccRushInterest(netCode, accInterestInfo.getAccTypeKeyBean().getaccType(), container.getEndInstDate(), container.getAccount());
        if (null == accInterestInfo || null == accInterestInfo.getPredrawDtls()) {
            return;
        }
        List<FcPredrawDtl> predrawDtls = accInterestInfo.getFcPredrawDtls();
        if (predrawDtls == null || predrawDtls.size() == 0) {
            return;
        }
        OperateType operateType = container.getOpType();
        Date interestTime = container.getInterestTime();
        List<FcPredrawRushDtl> predrawRushDtls = new ArrayList<FcPredrawRushDtl>();
        String commonSummary = multiLangServiceI.getLang("table.fcPredrawRushDtl.strikeInterest");//冲预提利息
        BigDecimal tot = BigDecimal.ZERO;//预提利息总额
        FcVoucherTypeDTO voucherType = getVoucherBySysVoucherCode();
        BigDecimal leftMoney = rushMoney;//本次冲预提剩余金额
        BigDecimal strikeInterest = BigDecimal.ZERO;//每次实际冲预提金额
        //存放当前账号的凭证明细信息
        Map<String, FcFinacialAccbookDtlVO> accAccbookDtlMap = new HashMap<String, FcFinacialAccbookDtlVO>();
        Map<String, FcAccDTO> payableCurrentAccMap = new HashMap<String, FcAccDTO>();//单位应付利息账号
        for (FcPredrawDtl istFcPredrawDtl : predrawDtls) {
            com.bytter.system.api.dto.FcAccDTO fcAcc = iFcAccService.getAccDTOByAccount(istFcPredrawDtl.getAccount());
            if (!canRushPredraw(container, fcAcc)) {//TODO 判断是否可以做冲预提
                continue;
            }
            FcPredrawRushDtl predrawRushDtl = new FcPredrawRushDtl();
            predrawRushDtl.setUpdateTime(new Date());
            predrawRushDtl.setCreateTime(new Date());
            predrawRushDtl.setUpdateBy(opCode);
            predrawRushDtl.setCreateBy(opCode);
            predrawRushDtl.setPredrawDtlId(istFcPredrawDtl.getId());
            predrawRushDtl.setOperatorCode(opCode);
            predrawRushDtl.setStrikeDate(interestTime);
            String orderNo = DateUtils.formatDate(istFcPredrawDtl.getInterestTime(), "yyyyMMddHHmmss");//设置冲预提orderNo
            predrawRushDtl.setOrderNo(orderNo + iCorpService.get(netCode).getCode());
            if (StringUtils.isNotEmpty(container.getDepositManagerId())) {
                predrawRushDtl.setDepositManagerId(container.getDepositManagerId());
            }
            if (OperateType.ADD == operateType) {
                predrawRushDtl.setStatus(InterestConstant.INTEREST_STATUE_TEMP);
            } else if (OperateType.SUBMIT == operateType) {
                predrawRushDtl.setStatus(InterestConstant.INTEREST_WAIT_CHECK);
            } else if (OperateType.CHECK == operateType) {
                predrawRushDtl.setStatus(InterestConstant.INTEREST_STATUE_CHECK);
            }
            BigDecimal rushInterest = rushMap.get(istFcPredrawDtl.getId());
            strikeInterest = istFcPredrawDtl.getInterest();
            List<String> accs = new ArrayList<String>();
            accs.add(istFcPredrawDtl.getAccount());
            //计算取款或还款冲预提金额
            if (tellerMoney != null && tellerMoney.compareTo(BigDecimal.ZERO) != 0) {
                BigDecimal checkBalance = iFcOnlineLedgerService.getLedgerDTOsByAccSet(accs).get(0).getCheckBalance();
                if (checkBalance.doubleValue() > 0) {
                    //strikeInterest = rushInterest.multiply(tellerMoney).divide(checkBalance, 2, BigDecimal.ROUND_HALF_UP);
                    strikeInterest = tellerMoney.multiply(istFcPredrawDtl.getInterest()).divide(container.getSumRushMoney(), 2, BigDecimal.ROUND_HALF_UP);
                }
            }
            boolean isAccInCenter = container.isAccInCenter();
            //没有则冲剩余的
            if (rushMoney != null && rushMoney.compareTo(BigDecimal.ZERO) > 0) {//有则冲部分金额
                if (rushInterest != null) {
                    if (rushInterest.compareTo(leftMoney) > 0) {//应冲>实冲
                        strikeInterest = leftMoney;
                        predrawRushDtl.setStrikeInterest(strikeInterest);
                        predrawRushDtls.add(predrawRushDtl);
                        tot = rushMoney;
                        FcFinacialAccbookDtlVO creditAccBookDtl = accAccbookDtlMap.get(fcAcc.getBlnctrAcc());
                        if (creditAccBookDtl == null) {
                            FcAccDTO payableCurrentAcc = null;
                            String debitIstAcc = getCreditAccout(fcAcc, false);//获取计息账户的应收应付利息户
                            if (EmptyUtils.isNotEmpty(debitIstAcc)) {
                                payableCurrentAcc = iFcAccService.getAccDTOByAccount(debitIstAcc);//获取账户信息
                            }
                            if (EmptyUtils.isEmpty(payableCurrentAcc)) {
                                String corpKey = fcAcc.getCorpId();
                                if (isAccInCenter) {
                                    corpKey = netCode;
                                }
                                payableCurrentAcc = payableCurrentAccMap.get(corpKey);
                                if (payableCurrentAcc == null) {
                                    //int entrySign = configService.getConfig("ist", "entrySign").toInteger().intValue();
                                    if (isAccInCenter) {
                                        payableCurrentAcc = findInterestCreditSysAcc(netCode, netCode, curCode,"","");//网点应付利息
                                        payableCurrentAccMap.put(corpKey, payableCurrentAcc);
                                    } else {
                                        String bankId = null;
                                        if("02".equals(fcAcc.getAccType()) || "04".equals(fcAcc.getAccType())){
                                            bankId = fcAcc.getBankCode();
                                        }
                                        payableCurrentAcc = findInterestCreditSysAcc(null, fcAcc.getCorpId(), curCode, bankId, "");//单位应付利息
                                    }
                                }
                            }
                            //冲预提凭证
                            if (InterestHelper.isMoreCreditAccBook(fcAcc.getAccType())) {
                                creditAccBookDtl = createAccbookDtl(payableCurrentAcc,
                                        commonSummary + "账号：" + fcAcc.getBlnctrAcc() + fcAcc.getAccName(), BigDecimal.ZERO,
                                        BigDecimal.ZERO.subtract(strikeInterest), voucherType,opCode);
                            } else {
                                creditAccBookDtl = createAccbookDtl(payableCurrentAcc,
                                        commonSummary + "账号：" + fcAcc.getBlnctrAcc() + fcAcc.getAccName(),
                                        BigDecimal.ZERO.subtract(strikeInterest), BigDecimal.ZERO, voucherType,opCode);
                            }
                            accAccbookDtlMap.put(fcAcc.getBlnctrAcc(), creditAccBookDtl);
                        } else {
                            creditAccBookDtl.getCreditMoney().subtract(strikeInterest);
                        }
                        break;
                    } else {
                        strikeInterest = rushInterest;
                        rushMap.remove(istFcPredrawDtl.getId());
                    }
                    leftMoney = leftMoney.subtract(strikeInterest);
                }
            }else{
                if (rushInterest != null) {
                    if (rushInterest.compareTo(strikeInterest) <= 0) {
                        strikeInterest = rushInterest;//冲剩余的预提
                        rushMap.remove(istFcPredrawDtl.getId());
                    }
                }
            }
            tot = tot.add(strikeInterest);
            predrawRushDtl.setStrikeInterest(strikeInterest);
            if (strikeInterest == null || strikeInterest.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }
            predrawRushDtls.add(predrawRushDtl);
            BigDecimal debitMoney = BigDecimal.ZERO;
            BigDecimal creditMoney = BigDecimal.ZERO;
            FcFinacialAccbookDtlVO creditAccBookDtl = accAccbookDtlMap.get(fcAcc.getBlnctrAcc());
            if (creditAccBookDtl == null) {
                FcAccDTO payableCurrentAcc = null;
                String debitIstAcc = getCreditAccout(fcAcc, false);//获取计息账户的应收应付利息户
                if (EmptyUtils.isNotEmpty(debitIstAcc)) {
                    payableCurrentAcc = iFcAccService.getAccDTOByAccount(debitIstAcc);//获取账户信息
                }
                if (EmptyUtils.isEmpty(payableCurrentAcc)) {
                    String corpKey = fcAcc.getCorpId();
                    if (isAccInCenter) {
                        corpKey = netCode;
                    }
                    payableCurrentAcc = payableCurrentAccMap.get(corpKey);
                    if (payableCurrentAcc == null) {
                        //int entrySign = configService.getConfig("ist", "entrySign").toInteger().intValue();
                        if (isAccInCenter) {
                            payableCurrentAcc = findInterestCreditSysAcc(corpKey, corpKey, curCode,"","");//网点应付利息
                            payableCurrentAccMap.put(corpKey, payableCurrentAcc);
                        } else {
                            String bankId = null;
                            if("02".equals(fcAcc.getAccType()) || "04".equals(fcAcc.getAccType())){
                                bankId = fcAcc.getBankCode();
                            }
                            payableCurrentAcc = findInterestCreditSysAcc(null, fcAcc.getCorpId(), curCode, bankId, "");//单位应付利息
                        }
                    }
                }
                if (InterestHelper.isMoreCreditAccBook(fcAcc.getAccType())) {
                    debitMoney = BigDecimal.ZERO;
                    creditMoney = BigDecimal.ZERO.subtract(strikeInterest);
                } else {
                    debitMoney = BigDecimal.ZERO.subtract(strikeInterest);
                    creditMoney = BigDecimal.ZERO;
                }
                //冲预提凭证
                creditAccBookDtl = createAccbookDtl(payableCurrentAcc, commonSummary + "账号：" + fcAcc.getBlnctrAcc()
                        + fcAcc.getAccName(), debitMoney, creditMoney, voucherType,opCode);
                accAccbookDtlMap.put(fcAcc.getBlnctrAcc(), creditAccBookDtl);
            } else {
                if (InterestHelper.isMoreCreditAccBook(fcAcc.getAccType())) {
                    creditAccBookDtl.setCreditMoney(creditAccBookDtl.getCreditMoney().subtract(strikeInterest));
                } else {
                    creditAccBookDtl.setDebitMoney(creditAccBookDtl.getDebitMoney().subtract(strikeInterest));
                }
            }
        }
        if (predrawRushDtls == null || predrawRushDtls.size() == 0) {
            return;
        }
        InterestParamBean paramBean = new InterestParamBean(true, accInterestInfo.getContainer().isAccInCenter(),
                accInterestInfo.getContainer().isMoreVoucherDtl());
        paramBean.setRush(true);//设置为冲预提
        accInterestInfo.setRushAccbook(new ArrayList<FcFinacialAccbookVO>(processCommonAccbooks(accAccbookDtlMap,
                accInterestInfo.getContainer().getTallyDate(), curCode, commonSummary, voucherType, paramBean,opCode).values()));
        if (OperateType.RUSH == operateType) {
            return;
        }
        for (FcPredrawDtl predrawDtl : predrawDtls) {
            predrawDtl.setStrikeSign(InterestConstant.STRIKE_SIGN_ALL);
        }
        if (OperateType.ADD == operateType || OperateType.SUBMIT == operateType
                || (OperateType.CHECK == operateType && accInterestInfo.isAutoRush())) {
            // predrawRushDtls
            List<String> predrawDtlIdS = predrawRushDtls.stream().map(FcPredrawRushDtl::getPredrawDtlId).collect(Collectors.toList());
            List<BigDecimal> strikeInterestBig = predrawRushDtls.stream().map(FcPredrawRushDtl::getStrikeInterest).collect(Collectors.toList());
            List<String> strikeInterestS = Lists.newArrayList();
            strikeInterestBig.stream().forEach(vo->{
                strikeInterestS.add(String.valueOf(vo));
            });
            List<String> orderNoS = predrawRushDtls.stream().map(FcPredrawRushDtl::getOrderNo).collect(Collectors.toList());
            List<FcPredrawRushDtl> newPredrawRushDtls = new ArrayList<FcPredrawRushDtl>();
            //modified 需要判断是否存在记录，如果存在记录，则不添加到 FC_PREDRAW_RUSH_DTL
            List<FcPredrawRushDtlDTO> fcPredrawRushDtlDTOS = fcPredrawRushDtlDao.queryByPredrawDtlIdAndStrikeInterestAndOrderNo(predrawDtlIdS
                    , strikeInterestS, orderNoS);
            if(CollectionUtil.isEmpty(fcPredrawRushDtlDTOS)){
                newPredrawRushDtls.addAll(predrawRushDtls);
            }else {
                predrawRushDtls.stream().forEach(item ->{
                    if(!CollectionUtil.isEmpty(fcPredrawRushDtlDTOS)){
                        fcPredrawRushDtlDTOS.stream().forEach(dbs -> {
                            if ( !item.getPredrawDtlId().equals(dbs.getPredrawDtlId()) && !item.getOrderNo().equals(dbs.getOrderNo())
                                    && !(item.getStrikeInterest().compareTo(dbs.getStrikeInterest()) == 0)){
                                newPredrawRushDtls.add(item);
                            }
                        });
                    }
                });
            }
            if (!CollectionUtil.isEmpty(newPredrawRushDtls)){
                fcInterestDao.addBatchByHql(newPredrawRushDtls);//这里进行了insert into FC_PREDRAW_RUSH_DTL
            }
        }
        if (tot.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }
        if (!accInterestInfo.isNeedSave()) { //保存冲预提凭证
            return;
        }
        saveRushPredrawVoucher(accInterestInfo.getRushAccbook(), predrawRushDtls);
        sortRushVoucher(accInterestInfo);
        //更新积数的计息日期为冲预提的时间
        fcInterestDao.updateAccuBalancesInterestTime(predrawDtls, interestTime);
    }
    /**
     * <b>功能介绍：</b>保存冲预提内部凭证<br>
     * <b>日期：</b>2017年7月19日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    public void saveRushPredrawVoucher(List<FcFinacialAccbookVO> accbooks, final List<FcPredrawRushDtl> predrawRushDtls) {
        //按照账号和冲预提明细保存凭证信息
        for (FcFinacialAccbookVO accbookVO : accbooks) {
            List<FcFinacialAccbookVO> accbookVOs = new ArrayList<FcFinacialAccbookVO>();
            accbookVOs.add(accbookVO);
            VoucherWrapper voucher = createVoucherWrapper(accbookVOs);
            fcAccbookService.submitFcAccbook(voucher);
//            String orderNo = voucher.getTarget().get(0).getAccbook().getOrderNo();
//            for (FcPredrawRushDtl dtl : predrawRushDtls) {
//                dtl.setOrderNo(orderNo);
//            }
        }
    }
    /**
     * <b>功能介绍：</b>判断是否可以冲预提<br>
     * <b>日期：</b>2017年7月19日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    private boolean canRushPredraw(InterestInfoContainer interestInfoContainer, FcAccDTO fcAcc) {
        if (interestInfoContainer.isBatchRushPre()) {
            return true;
        }
        InterestProcessType interestProcessType = InterestProcessType.NONE;
        String accPredrawSign = fcAcc.getPredrawSign(); //账户预提类型
        if (InterestConstant.PredrawSign.PREDRAW_SIGN_SEASON_ANTICIPATE.get().equals(accPredrawSign)) { //季末结息预提
            if (interestInfoContainer.isSeasonEnd()) { //季末
                if (InterestConstant.PREDRAW_RUSH_WAY_INTEREST_DAY.equals(interestInfoContainer.getSysPredrawRushWay())) { //结息方式为计息日预提冲销
                    if (interestInfoContainer.isSysInstDay()) { //计息日则冲预提
                        interestProcessType = InterestProcessType.INTEREST;
                    } else { // 非计息日则不冲
                        interestProcessType = InterestProcessType.NONE;
                    }
                } else if (InterestConstant.PREDRAW_RUSH_WAY_MONTH_END.equals(interestInfoContainer.getSysPredrawRushWay())) {//月末预提月初冲销
                    interestProcessType = InterestProcessType.NONE;
                } else { //结息方式为不控制则冲预提
                    interestProcessType = InterestProcessType.INTEREST;
                }
            } else { // 非季末则不冲
                interestProcessType = InterestProcessType.NONE;
            }
        } else if (InterestConstant.PredrawSign.PREDRAW_SIGN_SEASON_NO_ANTICIPATE.get().equals(accPredrawSign)) { //季末结息不预提
            interestProcessType = InterestProcessType.NONE;
        } else if (InterestConstant.PredrawSign.PREDRAW_SIGN_MONTH_INTEREST.get().equals(accPredrawSign)) { //每月结息
            interestProcessType = InterestProcessType.NONE;
        } else if (InterestConstant.PredrawSign.PREDRAW_SIGN_NO_SEASON_ANTICIPATE.get().equals(accPredrawSign)) { //季末不结息预提
            interestProcessType = InterestProcessType.NONE;
        } else if (InterestConstant.PredrawSign.PREDRAW_SIGN_NO_SEASON_NO_ANTICIPATE.get().equals(accPredrawSign)) { //季末不结息不预提
            //季末不结息不预提即每次不冲
            interestProcessType = InterestProcessType.NONE;
        }
        if (InterestProcessType.INTEREST == interestProcessType) {
            return true;
        }
        return false;
    }
    /**
     * <b>功能介绍：</b>查询账户待冲销利息<br>
     * <b>日期：</b>2017年7月19日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    public void getAccRushInterest(String netCode, String accTypeCode, Date endDate, String account) {
        List<Map<String, Object>> list = fcInterestDao.getAccRushInterest(netCode, accTypeCode, endDate, account);
        if (list != null) {
            for (Map<String, Object> map : list) {
                String predraw_dtl_id = (String) map.get("PREDRAW_DTL_ID");
                BigDecimal rushMoney = (BigDecimal) map.get("RUSHMONEY");
                rushMap.put(predraw_dtl_id, rushMoney);
            }
        }
    }
    @Override
    public void savePredrawRushDtl(AccInterestInfo accInterestInfo) {
        String netCode = accInterestInfo.getAccTypeKeyBean().getnetId();
        OperateType operateType = accInterestInfo.getContainer().getOpType();
        //提交在止息日期之前的打回的凭证信息
        Integer afterStatus = InterestConstant.INTEREST_WAIT_CHECK;
        Integer[] beforeStatus = new Integer[] {};
        if (OperateType.ADD == operateType) {
            afterStatus = InterestConstant.INTEREST_STATUE_TEMP;
        } else if (OperateType.SUBMIT == operateType) {
            afterStatus = InterestConstant.INTEREST_WAIT_CHECK;
            beforeStatus = new Integer[] { InterestConstant.INTEREST_BACK, InterestConstant.INTEREST_STATUE_TEMP };
        } else if (OperateType.CHECK == operateType) {
            afterStatus = InterestConstant.INTEREST_STATUE_CHECK;
            beforeStatus = new Integer[] { InterestConstant.INTEREST_WAIT_CHECK };
        } else if (OperateType.BACK == operateType) {
            afterStatus = InterestConstant.INTEREST_BACK;
            beforeStatus = new Integer[] { InterestConstant.INTEREST_WAIT_CHECK };
        }
        if (OperateType.ADD != operateType) {
            fcInterestDao.updatePredrawRushStatus(netCode, accInterestInfo.getAccTypeKeyBean().getaccType(),
                    accInterestInfo.getContainer().getEndInstDate(), beforeStatus, afterStatus, accInterestInfo
                            .getContainer().getAccount());
        }
    }
    /**
     * <b>功能介绍：</b>统计利息明细信息<br>
     * <b>日期：</b>2017年7月9日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    protected List<FcInterestDtlTemp> packInterestDtls(Map<String, List<InstrateAccuBean>> accInstrateTypeAccuMap,
                                                       InterestInfoContainer interestContainer) {
        List<FcInterestDtlTemp> interestDtls = new ArrayList<FcInterestDtlTemp>();
        Map<String, FcInterestDtlTemp> rateAccuCountMap = null;
        FcInterestDtlTemp fcInterestDtl = null;
        for (Entry<String, List<InstrateAccuBean>> entry : accInstrateTypeAccuMap.entrySet()) {
            rateAccuCountMap = new HashMap<String, FcInterestDtlTemp>();
            Map<String, String> balanceMap = new HashMap<String, String>();
            Date lastBalanceDate = null;
            String startDateStr = "";
            for (InstrateAccuBean accuInfo : entry.getValue()) {
                BigDecimal instrate = accuInfo.getInstrate();
                if (instrate == null) {
                    instrate = new BigDecimal(0);
                }
                String rate = NumberUtil.formatNumeric(instrate, "#,###.000000");
                Date balanceDate = accuInfo.getBalanceDate();
                Date startDate = accuInfo.getStartDate();
                if (lastBalanceDate != null) {
                    if (dayDiffs(balanceDate, lastBalanceDate) != 1) {//和上一个日期相差1
                        startDateStr = DateUtils.date2Str(balanceDate, DateUtils.date_sdf);
                    }
                } else {
                    startDateStr = DateUtils.date2Str(balanceDate, DateUtils.date_sdf);
                }
                //上次积数日期和计息的开始日期作为一条利息明细
                startDateStr += "_" + DateUtils.date2Str(startDate, DateUtils.date_sdf) + "_" + rate;
                lastBalanceDate = balanceDate;
                fcInterestDtl = rateAccuCountMap.get(startDateStr);
                if (fcInterestDtl == null) {//设置利息明细信息
                    fcInterestDtl = createInterestDtl(interestContainer);
                    fcInterestDtl.setAccount(accuInfo.getAccount());
                    fcInterestDtl.setCurCode(accuInfo.getFcAcc().getCurId());
                    fcInterestDtl.setInterestType(entry.getKey());
                    if (accuInfo.getInstrate() == null) {
                        accuInfo.setInstrate(new BigDecimal(0));
                    }
                    fcInterestDtl.setInterestRate(accuInfo.getInstrate());
                    fcInterestDtl.setInterestStartDate(accuInfo.getBalanceDate());
                    fcInterestDtl.setInterestSign(InterestConstant.INTEREST_SIGN_IST);
                    rateAccuCountMap.put(startDateStr, fcInterestDtl);
                }
                String account = balanceMap.get(DateUtils.date2Str(accuInfo.getBalanceDate(), DateUtils.yyyyMMdd));
                if (StringUtils.isBlank(account)) {
                    fcInterestDtl.setDays(fcInterestDtl.getDays() + 1);
                    balanceMap.put(DateUtils.date2Str(accuInfo.getBalanceDate(), DateUtils.yyyyMMdd), accuInfo.getAccount());
                }
                fcInterestDtl.setCumulation(fcInterestDtl.getCumulation().add(accuInfo.getACumu()));
                startDateStr = startDateStr.split("_")[0];
            }
            interestDtls.addAll(rateAccuCountMap.values());
        }
        return interestDtls;
    }
    /**
     * <b>功能介绍：</b>创建并初始化利息明细信息<br>
     * <b>日期：</b>2017年7月9日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    protected FcInterestDtlTemp createInterestDtl(InterestInfoContainer interestContainer) {
        FcInterestDtlTemp interestDtl = new FcInterestDtlTemp();
        interestDtl.setInterestTime(interestContainer.getInterestTime());
        interestDtl.setDays(0);
        interestDtl.setCumulation(BigDecimal.ZERO);
        interestDtl.setInterestRate(BigDecimal.ZERO);
        interestDtl.setInterest(BigDecimal.ZERO);
        //
        interestDtl.setGenerateMode(interestContainer.getGenerateMode());
        interestDtl.setBusStatus(InterestConstant.INTEREST_STATUE_TEMP);
        interestDtl.setTallyDate(interestContainer.getTallyDate());
//        interestDtl.setOperatorCode(interestContainer.getUserBean().getId());
        interestDtl.setUserSessionId(interestContainer.getSessionId());
        if (OperateType.SUBMIT.equals(interestContainer.getOpType())) {
            interestDtl.setBusStatus(InterestConstant.INTEREST_WAIT_CHECK);
        } else {
            interestDtl.setBusStatus(InterestConstant.INTEREST_STATUE_TEMP);
        }
        return interestDtl;
    }
    /**
     * <b>功能介绍：</b>合并利息区间利息<br>
     * <b>日期：</b>2017年7月9日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    @SuppressWarnings("finally")
    protected List<FcInterestDtlTemp> transDtlTemps(List<FcInterestDtlTemp> interestDtls) {
        if (interestDtls == null || interestDtls.size() <= 1) {
            return interestDtls;
        }
        List<FcInterestDtlTemp> temps = new ArrayList<FcInterestDtlTemp>();
        try {
            Collections.sort(interestDtls, new Comparator<FcInterestDtlTemp>() {
                @Override
                public int compare(FcInterestDtlTemp o1, FcInterestDtlTemp o2) {
                    if (o1.getAccount().equals(o2.getAccount())) {
                        return o1.getInterestStartDate().compareTo(o2.getInterestStartDate());
                    } else {
                        return o1.getAccount().compareTo(o2.getAccount());
                    }
                }
            });
            Map<String, List<FcInterestDtlTemp>> istMap = new HashMap<String, List<FcInterestDtlTemp>>();
            for (FcInterestDtlTemp dtl : interestDtls) {
                List<FcInterestDtlTemp> temps2 = istMap.get(dtl.getAccount());
                if (temps2 == null) {
                    temps2 = new ArrayList<FcInterestDtlTemp>();
                    istMap.put(dtl.getAccount(), temps2);
                }
                temps2.add(dtl);
            }
            for(Entry<String, List<FcInterestDtlTemp>> entry : istMap.entrySet()){
                List<FcInterestDtlTemp> dtlTemps = entry.getValue();
                FcInterestDtlTemp tempDtl = dtlTemps.get(0);
                //合并相邻时间段相同利率的利息
                for (int i = 1, size = dtlTemps.size(); i < size; i++) {
                    FcInterestDtlTemp currDtl = dtlTemps.get(i);
                    Date endDate = tempDtl.getBlnInstDate();
                    Date startDate = currDtl.getInterestStartDate();
                    if(getDateDiff(startDate, endDate) == -1) {//相差一天
                        //且利率、利率类型相同
                        if (tempDtl.getInterestRate().compareTo(currDtl.getInterestRate()) == 0
                                && tempDtl.getInterestType().equals(currDtl.getInterestType())) {
                            //可以合并
                            FcInterestDtlTemp newDtl = new FcInterestDtlTemp();
                            BeanUtils.copyNotNullProperties(tempDtl,newDtl);
                            newDtl.setCumulation(tempDtl.getCumulation().add(currDtl.getCumulation()));
                            newDtl.setInterest(tempDtl.getInterest().add(currDtl.getInterest()));
                            newDtl.setBlnInstDate(currDtl.getBlnInstDate());
                            newDtl.setDays(tempDtl.getDays() + currDtl.getDays());
                            FcInterestDtlTemp newDtl2 = new FcInterestDtlTemp();
                            BeanUtils.copyNotNullProperties(newDtl,newDtl2);
                            tempDtl = newDtl2;
                        } else {
                            FcInterestDtlTemp newDtl = new FcInterestDtlTemp();
                            BeanUtils.copyNotNullProperties(tempDtl,newDtl);
                            temps.add(newDtl);
                            FcInterestDtlTemp newDtl2 = new FcInterestDtlTemp();
                            BeanUtils.copyNotNullProperties(currDtl,newDtl2);
                            tempDtl = newDtl2;
                        }
                    } else {
                        FcInterestDtlTemp newDtl = new FcInterestDtlTemp();
                        BeanUtils.copyNotNullProperties(tempDtl,newDtl);
                        temps.add(newDtl);
                        FcInterestDtlTemp newDtl2 = new FcInterestDtlTemp();
                        BeanUtils.copyNotNullProperties(currDtl,newDtl2);
                        tempDtl = newDtl2;
                    }
                }
                FcInterestDtlTemp newDtl = new FcInterestDtlTemp();
                BeanUtils.copyNotNullProperties(tempDtl,newDtl);
                temps.add(newDtl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            /**
             * 防止组合利息的偏差，重新根据积数和利率计算一次利息
             */
            Calendar calendar = Calendar.getInstance();
            for (FcInterestDtlTemp interestDtl : temps) {
                // 利息 = 积数 X 日利率(利率/利率类型)
                interestDtl.setInterest(interestDtl.getCumulation().multiply(interestDtl.getInterestRate())
                        .divide(sysRateType, 2, BigDecimal.ROUND_HALF_UP));
                calendar.setTime(interestDtl.getInterestStartDate());
                calendar.add(Calendar.DAY_OF_MONTH, interestDtl.getDays() - 1);
                interestDtl.setBlnInstDate(calendar.getTime());
            }
            return temps;
        }
    }
    /**
     * <b>功能介绍：</b>生成预提凭证<br>
     * <b>日期：</b>2017年7月11日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    public void processPredrawnVoucher(AccInterestInfo accInterestInfo) {
        List<FcInterestDtlTemp> interestDtls = new ArrayList<FcInterestDtlTemp>();
        List<FcPredrawDtlTempVO> predrawDtlTemps = accInterestInfo.getPredrawDtls();
        setOperatorCode(accInterestInfo.getContainer().getUserBean().getId());
        for (FcPredrawDtlTempVO predrawDtlTemp : predrawDtlTemps) {
            if (predrawDtlTemp.getInterestDtls() != null) {
                interestDtls.addAll(predrawDtlTemp.getInterestDtls());
            }
        }
        if (interestDtls == null || interestDtls.size() == 0) {
            return;
        }
        //构建预提凭证
        InterestParamBean paramBean = new InterestParamBean(true, accInterestInfo.getContainer().isAccInCenter(),
                accInterestInfo.getContainer().isMoreVoucherDtl());
        Map<String, FcFinacialAccbookVO> accbookMap = createCommonVoucher(interestDtls, accInterestInfo.getContainer()
                .getTallyDate(), accInterestInfo.getContainer().getUserBean().getId(), paramBean);
        if (accbookMap == null) {
            return;
        }
        accInterestInfo.setPredrawAccbook(new ArrayList<FcFinacialAccbookVO>(accbookMap.values()));
        sortPredrawVoucher(accInterestInfo);
        saveInterestVoucher(accbookMap, interestDtls, paramBean);
    }
    /**
     * <b>功能介绍：</b>保存计息凭证<br>
     * <b>日期：</b>2017年7月11日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    public void saveInterestVoucher(Map<String, FcFinacialAccbookVO> accbookMap, List<FcInterestDtlTemp> interestDtls, InterestParamBean paramBean){
        boolean isMoreVoucherDtl = paramBean.isMoreVoucherDtl();
        if (isMoreVoucherDtl) {
            List<FcFinacialAccbookVO> accbooks = new ArrayList<FcFinacialAccbookVO>();
            if (accbookMap.isEmpty()) {
                return;
            }
            accbooks.addAll(accbookMap.values());
            saveVoucher(accbooks, interestDtls);
        } else {//一借一贷
            //利息按照账号分组
            Map<String, List<FcInterestDtlTemp>> interestMap = groupByInterestAcc(interestDtls);
            //按照账号和利息保存凭证信息
            for (Entry<String, FcFinacialAccbookVO> entry : accbookMap.entrySet()) {
                List<FcFinacialAccbookVO> accbooks = new ArrayList<FcFinacialAccbookVO>();
                if (StringUtils.isBlank(entry.getKey()) || entry.getValue() == null
                        || interestMap.get(entry.getKey()) == null) {
                    continue;
                }
                accbooks.add(entry.getValue());
                saveVoucher(accbooks, new ArrayList<FcInterestDtlTemp>(interestMap.get(entry.getKey())));
            }
        }
    }
    /**
     * <b>功能介绍：</b>保存内部凭证<br>
     * <b>日期：</b>2017年7月11日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    public void saveVoucher(List<FcFinacialAccbookVO> accbooks, final List<FcInterestDtlTemp> interestDtlTemps) {
        VoucherWrapper voucher = createVoucherWrapper(accbooks);
        fcAccbookService.submitFcAccbook(voucher);
        String orderNo = voucher.getTarget().get(0).getAccbook().getOrderNo();
        for (FcInterestDtlTemp dtl : interestDtlTemps) {
            dtl.setOrderNo(orderNo);
        }
    }
    /**
     * <b>功能介绍：</b>创建内部凭证<br>
     * <b>日期：</b>2017年7月11日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    public VoucherWrapper createVoucherWrapper(List<FcFinacialAccbookVO> accbooks) {
        VoucherWrapper voucher = new VoucherWrapper();
        List<FcAccbookVoucherVO> target = new ArrayList<FcAccbookVoucherVO>();
        for (FcFinacialAccbookVO fcAccbookVoucher : accbooks) {//将FcFinacialAccbookVO转化为FcAccbookVoucherVO
            FcAccbookVoucherVO fcAccbookVourcher = new FcAccbookVoucherVO();
            FcAccbook accbook = new FcAccbook();
            BeanUtils.copyProperties(fcAccbookVoucher, accbook);
            // operatorCode he  checkerCode 同一个 ，有问题
            if (!StringUtils.isBlank(fcAccbookVoucher.getOperatorCode())) {
                accbook.setOperatorCode(fcAccbookVoucher.getOperatorCode());
            }
            if (!StringUtils.isBlank(fcAccbookVoucher.getCheckerCode())) {
                accbook.setCheckerCode(fcAccbookVoucher.getCheckerCode());
            }
            fcAccbookVourcher.setAccbook(accbook);
            List<FcAccbookDtl> bookDtls = new ArrayList<FcAccbookDtl>();
            List<FcFinacialAccbookDtlVO>  dtl = fcAccbookVoucher.getFcAccbookDtls();
            bookDtls = BeanUtils.copyListProperties(dtl,FcAccbookDtl.class);
            fcAccbookVourcher.setBookDtls(bookDtls);
            fcAccbookVourcher.setRush(fcAccbookVoucher.isRush());
            fcAccbookVourcher.setNewRush(fcAccbookVoucher.isNewRush());
            target.add(fcAccbookVourcher);
        }
        if (accbooks.get(0).getOperatorCode() != null){
            voucher.setUserCode(accbooks.get(0).getOperatorCode());// 记账人
        }
        if (StringUtils.isNotBlank(getTallyCode())){
            voucher.setUserCode(getTallyCode());// 记账人
        }
        voucher.setOriType(FinaBookConstant.BusOriType.IST_INTEREST_BOOK.get());
        voucher.setTarget(target);
        for (FcFinacialAccbookVO accbook : accbooks) {
            if (!StringUtils.isBlank(getTallyCode())) {
                accbook.setOperatorCode(getTallyCode());
            }
            if (!StringUtils.isBlank(getOperatorCode())) {
                accbook.setOperatorCode(getOperatorCode());
                accbook.setCheckerCode(getOperatorCode());
            }
        }
        voucher.setAutoAccounter(false);//自动记账
        voucher.setAutoCheck(true);//自动复核
        if (isBatchInstAutoCheck()) {
            voucher.setHaveCheckUser(false);//指定复核人
        } else {
            voucher.setHaveCheckUser(true);//指定复核人
            voucher.setCheckUser(accbooks.get(0).getCheckerCode());// 添加复核员代码
        }
        return voucher;
    }
    /**
     * <b>功能介绍：</b>按照账号分组利息信息<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    protected Map<String, List<FcInterestDtlTemp>> groupByInterestAcc(List<FcInterestDtlTemp> interestDtls){
        Map<String, List<FcInterestDtlTemp>> interestMap = new HashMap<String, List<FcInterestDtlTemp>>();
        for (FcInterestDtlTemp interest : interestDtls) {
            List<FcInterestDtlTemp> interestDtlTemps = interestMap.get(interest.getAccount());
            if (interestDtlTemps == null) {
                interestDtlTemps = new ArrayList<FcInterestDtlTemp>();
                interestMap.put(interest.getAccount(), interestDtlTemps);
            }
            interestDtlTemps.add(interest);
        }
        return interestMap;
    }
    /**
     * <b>功能介绍：</b>构建凭证<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    public Map<String, FcFinacialAccbookVO> createCommonVoucher(List<FcInterestDtlTemp> interestDtls, Date tallyDate,
                                                                String checkUserId, InterestParamBean paramBean) {
        FcVoucherTypeDTO voucherType = getVoucherBySysVoucherCode();
        FcAccDTO netAccDto = iFcAccService.getAccDTOByAccount(interestDtls.get(0).getAccount());
        String curId = netAccDto.getCurId();
        BigDecimal debitMoney = BigDecimal.ZERO;
        BigDecimal creditMoney = BigDecimal.ZERO;
        Calendar tallyDateCan = Calendar.getInstance();
        tallyDateCan.setTime(tallyDate);
        String commonSummary = (tallyDateCan.get(Calendar.MONTH) + 1) + multiLangServiceI.getLang(InterestConstant.FINACIAL_INTEREST_MONTH);
        String interestSummary = multiLangServiceI.getLang(InterestConstant.FINACIAL_INTEREST_SUMMARY);//结息
        String accSummary = multiLangServiceI.getLang(InterestConstant.FINACIAL_INTEREST_ACCOUNT);//账号
        List<FcVoucherEntryDefinePoolDTO> vouchers = null;
        if(paramBean.isPoolSign()) {
            //获取资金池主体的记账规则
            FcBusinessRuleDefinePoolDTO ruleDefineDTO = ruleDefineService.getBusinessRulePoolExp(netAccDto.getNetId(), curId,
                    FinaBookConstant.SysRaceCode.NB_POOL_INTEREST.get());
            vouchers = ruleDefineDTO.getVouchers();
        }
        Map<String, FcFinacialAccbookDtlVO> accAccbookDtlMap = new HashMap<String, FcFinacialAccbookDtlVO>();
        String creditSummary = commonSummary;
        Map<String, FcAccDTO> payableCurrentAccMap = new HashMap<String, FcAccDTO>();
        for (FcInterestDtlTemp interestDtl : interestDtls) {
            if (interestDtl.getInterest().compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }
            String creditInterestAcc = interestDtl.getAccount();//计息账号
            FcAccDTO interestAcc = iFcAccService.getAccDTOByAccount(interestDtl.getAccount());
            String corpCode = interestAcc.getCorpId();//获取账号单位
            String netCode = interestAcc.getNetId();//获取账号网点
            String opUserId = interestDtl.getOperatorCode();//计息提交人
            FcFinacialAccbookDtlVO creditAccbookDtl = accAccbookDtlMap.get(creditInterestAcc);
            if (creditAccbookDtl == null) {
                FcAccDTO fcAcc = null;
                if (paramBean.isPredraw()) {
                    //预提凭证
                    creditSummary = commonSummary + "预提利息";//table.predraw.interest
                    //优先获取计息户对应的利息相关户
                    if (paramBean.isMoreVoucherDtl()) {//多条明细
                        fcAcc = payableCurrentAccMap.get(netCode);
                        if(null == fcAcc) {
                            fcAcc = findInterestCreditSysAcc(netCode, netCode, curId, null,checkUserId);//网点应付利息-企业活期存款账户
                            payableCurrentAccMap.put(netCode, fcAcc);
                        }
                    }else {
                        String creditAcc = getCreditAccout(interestAcc, false);//获取计息账户的应收应付利息户
                        if (EmptyUtils.isNotEmpty(creditAcc)) {
                            fcAcc = iFcAccService.getAccDTOByAccount(creditAcc);//获取账户信息
                        } else {
                            fcAcc = payableCurrentAccMap.get(corpCode);
                            if (fcAcc == null) {
                                //int entrySign = configService.getConfig("ist", "entrySign").toInteger().intValue();
                                String bankId = null;
                                if (paramBean.isAccInCenter()) {//网点
                                    fcAcc = findInterestCreditSysAcc(netCode, netCode, curId,bankId,checkUserId);//网点应付利息-企业活期存款账户
                                    payableCurrentAccMap.put(corpCode, fcAcc);
                                } else {
                                    if("02".equals(interestAcc.getAccType()) || "04".equals(interestAcc.getAccType())){
                                        bankId = interestAcc.getBankCode();
                                    }
                                    fcAcc = findInterestCreditSysAcc(null, corpCode, curId,bankId,checkUserId);//单位应付利息-企业活期存款账户
                                    //payableCurrentAccMap.put(corpCode, fcAcc);
                                }
                            }
                        }
                    }
                } else {
                    //结息凭证
                    creditSummary = commonSummary + interestSummary;
                    if (paramBean.isPoolSign()) {
                        //取记账分录贷方的会计科目
                        String subjectId = "";
                        for (FcVoucherEntryDefinePoolDTO dto : vouchers) {
                            if (FinacialConstant.BALANCE_WAY_CREDIT.equals(dto.getBalanceWay().toString())) {
                                subjectId = dto.getSubjectCode();
                                break;
                            }
                        }
                        if (EmptyUtils.isEmpty(subjectId)) {
                            throw new ParameterException(InterestMessageEnum.FIN_INTEREST_NOTSET_SUBJECT, FinaBookConstant.SysRaceCode.NB_POOL_INTEREST.getName());
                        }
                        fcAcc = getPoolCreditSysAcc(netCode, curId, subjectId);//其他应付款
                    } else {
                        //企业活期账号
                        fcAcc = getCurrentAcc(interestDtl, corpCode, curId, interestAcc);
                    }
                }
                if (InterestHelper.isMoreCreditAccBook(interestAcc.getAccType())) {
                    debitMoney = BigDecimal.ZERO;
                    creditMoney = interestDtl.getInterest();
                } else {
                    debitMoney = interestDtl.getInterest();
                    creditMoney = BigDecimal.ZERO;
                }
                creditAccbookDtl = createAccbookDtl(fcAcc, creditSummary + "，" + accSummary + "：" + interestAcc.getBlnctrAcc()
                        + " " + interestAcc.getAccName(), debitMoney, creditMoney, voucherType,checkUserId);
                creditAccbookDtl.setOpUserId(opUserId);//计息提交人
                accAccbookDtlMap.put(creditInterestAcc, creditAccbookDtl);
            } else {
                String accTypeCode = interestAcc.getAccType();
                if (InterestHelper.isMoreCreditAccBook(accTypeCode)) {
                    creditAccbookDtl.setCreditMoney(creditAccbookDtl.getCreditMoney().add(interestDtl.getInterest()));
                } else {
                    creditAccbookDtl.setDebitMoney(creditAccbookDtl.getDebitMoney().add(interestDtl.getInterest()));
                }
            }
        }
        return processCommonAccbooks(accAccbookDtlMap, tallyDate, curId, creditSummary, voucherType, paramBean, checkUserId);
    }
    /**
     * <b>功能介绍：</b>生成一借一贷会计凭证<br>
     * <b>日期：</b>2017年7月14日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    private Map<String, FcFinacialAccbookVO> processCommonAccbooks(Map<String, FcFinacialAccbookDtlVO> accAccbookDtlMap,
                                                                   Date tallyDate, String curCode, String commonSummary,
                                                                   FcVoucherTypeDTO voucherType, InterestParamBean paramBean, String checkUserId) {
        Map<String, FcFinacialAccbookVO> accbooksMap = new HashMap<String, FcFinacialAccbookVO>();
        Map<String, FcAccDTO> debitAccMap = new HashMap<String, FcAccDTO>();
        BigDecimal debitMoney = BigDecimal.ZERO;
        BigDecimal creditMoney = BigDecimal.ZERO;
        //借方凭证
        FcFinacialAccbookDtlVO debitAccbookDtl = null;
        FcAccDTO debitAcc = null;
        if (paramBean.isMoreVoucherDtl()) {//多条明细
            //XXX 多借一贷/一借多贷的计息凭证类型暂时不做
            Map<String, List<FcFinacialAccbookDtlVO>> dtlMap = new HashMap<String, List<FcFinacialAccbookDtlVO>>();
            FcFinacialAccbookVO accbook = null;
            Map<String, FcFinacialAccbookVO> accbooksMap2 = new HashMap<String, FcFinacialAccbookVO>();
            for (Entry<String, FcFinacialAccbookDtlVO> entry : accAccbookDtlMap.entrySet()) {
                FcFinacialAccbookDtlVO creditAccbcookDtl = entry.getValue();//贷方凭证
                String corpCode = creditAccbcookDtl.getFcAcc().getCorpId();//获取账号单位
                String netCode = creditAccbcookDtl.getFcAcc().getNetId();//获取账号网点
                String keyCode = netCode;
                debitAcc = debitAccMap.get(keyCode);
                if (debitAcc == null) {
                    debitAcc = findInterestDebitSysAcc(netCode, netCode, curCode, null, null, null);//网点利息支出
                    debitAccMap.put(netCode, debitAcc);
                }
                List<FcFinacialAccbookDtlVO> dtls = dtlMap.get(keyCode);
                if (dtls == null) {
                    dtls = new ArrayList<FcFinacialAccbookDtlVO>();
                }
                dtls.add(entry.getValue());
                dtlMap.put(keyCode, dtls);
                accbook = accbooksMap2.get(keyCode);
                if (accbook == null) { //此处参数 有问题，不能2个都为 checkUserId
                    accbook = initAccbook(tallyDate, paramBean.isRush(), netCode, checkUserId, checkUserId, false, paramBean.getAccBean());
                    accbook.setFcAccbookDtls(new ArrayList<FcFinacialAccbookDtlVO>());
                }
                accbook.setOperatorCode(checkUserId);
                accbooksMap2.put(keyCode, accbook);//一张凭证
            }
            //汇总计算借方金额合计
            for (Entry<String, FcFinacialAccbookVO> entry : accbooksMap2.entrySet()) {
                String keyCodeStr = entry.getKey();
                List<FcFinacialAccbookDtlVO> dtls = dtlMap.get(keyCodeStr);
                debitMoney = BigDecimal.ZERO;
                creditMoney = BigDecimal.ZERO;
                FcFinacialAccbookVO accbook2 = entry.getValue();
                accbook2.setFcAccbookDtls(new ArrayList<FcFinacialAccbookDtlVO>());
                for (FcFinacialAccbookDtlVO accbookDtl : dtls) {
                    if (accbookDtl.getCreditMoney().compareTo(BigDecimal.ZERO) != 0) {
                        creditMoney = BigDecimal.ZERO;
                        debitMoney = debitMoney.add(accbookDtl.getCreditMoney());
                    } else if (accbookDtl.getDebitMoney().compareTo(BigDecimal.ZERO) != 0) {
                        creditMoney = creditMoney.add(accbookDtl.getDebitMoney());
                        debitMoney = BigDecimal.ZERO;
                    } else {
                        continue;
                    }
                }
                //借方凭证
                if (debitMoney.compareTo(BigDecimal.ZERO) == 0 && creditMoney.compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }
                debitAcc = debitAccMap.get(keyCodeStr);
                debitAccbookDtl = createAccbookDtl(debitAcc, commonSummary, debitMoney, creditMoney,
                        voucherType, checkUserId);
                if (creditMoney.compareTo(BigDecimal.ZERO) != 0) {
                    if (creditMoney.compareTo(BigDecimal.ZERO) > 0) {
                        accbook2.getFcAccbookDtls().add(debitAccbookDtl);
                        accbook2.getFcAccbookDtls().addAll(dtls);
                    } else {
                        accbook2.getFcAccbookDtls().addAll(dtls);
                        accbook2.getFcAccbookDtls().add(debitAccbookDtl);
                    }
                } else {
                    if (debitMoney.compareTo(BigDecimal.ZERO) > 0) {
                        accbook2.getFcAccbookDtls().addAll(dtls);
                        accbook2.getFcAccbookDtls().add(debitAccbookDtl);
                    } else {
                        accbook2.getFcAccbookDtls().add(debitAccbookDtl);
                        accbook2.getFcAccbookDtls().addAll(dtls);
                    }
                }
                List<FcFinacialAccbookDtlVO> accbookDtls = accbook2.getFcAccbookDtls();
                final boolean isRush = paramBean.isRush();
                if(isRush){
                    Collections.sort(accbookDtls, new Comparator<FcFinacialAccbookDtlVO>() {
                        @Override
                        public int compare(FcFinacialAccbookDtlVO o1, FcFinacialAccbookDtlVO o2) {
                            return o1.getDebitMoney().compareTo(o2.getDebitMoney());
                        }
                    });
                } else {
                    Collections.sort(accbookDtls, new Comparator<FcFinacialAccbookDtlVO>() {
                        @Override
                        public int compare(FcFinacialAccbookDtlVO o1, FcFinacialAccbookDtlVO o2) {
                            if (o1.getDebitMoney().compareTo(BigDecimal.ZERO) != 0) {
                                return -1;
                            }
                            if (o2.getDebitMoney().compareTo(BigDecimal.ZERO) != 0) {
                                return 1;
                            }
                            return o2.getDebitMoney().compareTo(o1.getDebitMoney());
                        }
                    });
                }
                accbook2.setOperatorCode(checkUserId);
                accbooksMap.put(keyCodeStr, accbook2);
            }
        }else{//一借一贷
            for (Entry<String, FcFinacialAccbookDtlVO> entry : accAccbookDtlMap.entrySet()) {
                FcFinacialAccbookDtlVO creditAccbcookDtl = entry.getValue();//贷方凭证
                String corpCode = creditAccbcookDtl.getFcAcc().getCorpId();//获取账号单位
                String netCode = creditAccbcookDtl.getFcAcc().getNetId();//获取账号网点
                String account = entry.getKey();//获取账号
                String opUserId = creditAccbcookDtl.getOpUserId();
                FcFinacialAccbookVO accbook = initAccbook(tallyDate, false,netCode, checkUserId, opUserId, paramBean.isPoolSign(), paramBean.getAccBean());
                accbook.setFcAccbookDtls(new ArrayList<FcFinacialAccbookDtlVO>());
                FcAccDTO interestAcc = iFcAccService.getAccDTOByAccount(account);
                
                if (paramBean.isPoolSign()) {//借方为资金池活期账户
                    debitAcc = debitAccMap.get(entry.getKey());//资金池活期账户
                    if (debitAcc == null) {
                        debitAcc = iFcAccService.getPoolAccDTOByAccount(entry.getKey());
                        debitAccMap.put(entry.getKey(), debitAcc);
                    }
                } else {
                    //int entrySign = configService.getConfig("ist", "entrySign").toInteger().intValue();
                    //优先获取计息户对应的利息相关户
                    String debitIstAcc = getDebitAccout(interestAcc, !paramBean.isPredraw());//获取计息账户的应收应付利息户
                    if (EmptyUtils.isNotEmpty(debitIstAcc)) {
                        debitAcc = iFcAccService.getAccDTOByAccount(debitIstAcc);//获取账户信息
                    } else {
                        if (paramBean.isAccInCenter()) {
                            debitAcc = debitAccMap.get(netCode);
                        } else {
                            debitAcc = debitAccMap.get(corpCode);
                        }
                    }
                    if (debitAcc == null) {
                        String bankId = null;
                        if (paramBean.isAccInCenter()) {
                            debitAcc = findInterestDebitSysAcc(netCode, netCode, curCode,account,bankId,checkUserId);//网点利息支出
                            debitAccMap.put(netCode, debitAcc);
                        } else {
                            if("02".equals(interestAcc.getAccType()) || "04".equals(interestAcc.getAccType())){
                                bankId = interestAcc.getBankCode();
                            }
                            debitAcc = findInterestDebitSysAcc(null, corpCode, curCode,account,bankId,checkUserId);//单位利息支出
                            //debitAccMap.put(corpCode, debitAcc);
                        }
                    }
                }
                if (creditAccbcookDtl.getCreditMoney().compareTo(BigDecimal.ZERO) != 0) {
                    creditMoney = BigDecimal.ZERO;
                    debitMoney = creditAccbcookDtl.getCreditMoney();
                } else if (creditAccbcookDtl.getDebitMoney().compareTo(BigDecimal.ZERO) != 0) {
                    creditMoney = creditAccbcookDtl.getDebitMoney();
                    debitMoney = BigDecimal.ZERO;
                } else {
                    continue;
                }
                //借方凭证
                debitAccbookDtl = createAccbookDtl(debitAcc, commonSummary, debitMoney, creditMoney,
                        voucherType,checkUserId);
                if (creditAccbcookDtl.getCreditMoney().compareTo(BigDecimal.ZERO) != 0) {
                    accbook.getFcAccbookDtls().add(debitAccbookDtl);
                    accbook.getFcAccbookDtls().add(creditAccbcookDtl);
                } else {
                    accbook.getFcAccbookDtls().add(creditAccbcookDtl);
                    accbook.getFcAccbookDtls().add(debitAccbookDtl);
                }
                accbooksMap.put(entry.getKey(), accbook);
            }
        }
        return accbooksMap;
    }
    /**
     * <b>功能介绍：</b>获取单位企业活期账号(按照扣息账号规则)<br>
     * <b>日期：</b>2017年7月14日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    protected FcAccDTO getCurrentAcc(FcInterestDtlTemp interestDtl, String corpCode, String curId, FcAccDTO interestAcc) {
        String oriAccount = "";
        Set<String> corpCodes = new HashSet<String>();
        corpCodes.add(corpCode);
        /*List<FcAccDTO> accs = 
        		iFcAccService.findBySysSubjectCode(SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.get(),InnerAccountConstant.FCACC_BUSINESS_ACCOUNT, null, corpCodes,
        				curId);*/
        List<FcAccDTO> accs = iFcAccService.getFcAccBySysSubjectId(SubjectConstant.SysSubject.INTEREST_EXPENSES_CURRENT_DEPOSIT.get(), null, corpCodes,
                curId);
        for (FcAccDTO fcAcc : accs) {
            if(!StringUtils.isBlank(oriAccount) && !fcAcc.getBlnctrAcc().equals(oriAccount)){
                return fcAcc;
            }
            return fcAcc;
        }
        return null;
    }
    /**
     * <b>功能介绍：</b>组装账户利率信息<br>
     * <b>日期：</b>2017年8月25日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     * @param instrateAdjustBean
     * @param accCurRates
     * @param instrtTypeRate
     * @param instrtType
     * @param accuBalance
     * @return
     */
    protected AccInstrateBean packRateInfo(InstrateAdjustBean instrateAdjustBean,
                                           Map<String, AccInstrateBean> accCurRates, BigDecimal instrtTypeRate, String instrtType,
                                           FcAccuBalancef accuBalance) {
        AccInstrateBean currRate = accCurRates.get(instrtType);
        if (currRate != null) {
            Date curRateDate = currRate.getInstrateTime();//利率生效日
            if (accuBalance.getBalanceDate().compareTo(curRateDate) < 0) {//假如计算的日期比使用的利率时间小的话，则使用当前的利率
                return currRate;
            }
        }
        boolean isAdjust = false;
        List<AccInstrateBean> adjustRates = null;
        if (instrateAdjustBean != null) {//判断是否存在调整利率
            adjustRates = instrateAdjustBean.getAccInstratesByAdjustType(instrtType);
            if (adjustRates != null && !adjustRates.isEmpty()) {
                isAdjust = true;
            }
        }
        AccInstrateBean rateInfo = null;
        if (isAdjust) {//存在调整利率的情况
            rateInfo = getRateByAdjust(accuBalance.getBalanceDate(), adjustRates, currRate);
            rateInfo.setAccount(accuBalance.getAccount());
            rateInfo.setInstrtType(instrtType);
        } else {
            Date startDate;
            Date instrateTime;
            if (currRate == null) {
                startDate = accuBalance.getBalanceDate();
                instrateTime = accuBalance.getBalanceDate();
            } else {
                startDate = currRate.getStartDate();
                instrateTime = currRate.getInstrateTime();
                //如果上次积数的日期和本次的积数日期相差 > 1,则用新的开始日期计算
                if(DateUtils.dayDiffs(accuBalance.getBalanceDate(), instrateTime) >= 0){
                    instrtTypeRate = currRate.getNewInstrate();
                } else {
                    if (null != currRate.getOldInstrate()) {
                        instrtTypeRate = currRate.getOldInstrate();
                    }
                }
            }
            rateInfo = new AccInstrateBean(accuBalance.getAccount(), instrtType, startDate, instrateTime, instrtTypeRate,
                    null);
        }
        accCurRates.put(instrtType, rateInfo);
        return rateInfo;
    }
    /**
     * <b>功能介绍：</b>计算账户调整利率信息<br>
     * <b>日期：</b>2017年8月25日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     * @param balanceDate
     * @param adjustRates
     * @param currRate
     * @return
     */
    protected AccInstrateBean getRateByAdjust(Date balanceDate, List<AccInstrateBean> adjustRates,
                                              AccInstrateBean currRate) {
        BigDecimal rate = null;
        BigDecimal oldRate = null;
        Date rateDate = null;
        int i = 0;
        int adjustSize = adjustRates.size();
        for (Iterator<AccInstrateBean> it = adjustRates.iterator(); it.hasNext();) {
            AccInstrateBean adjustRateInfo = it.next();
            BigDecimal newInstrate = adjustRateInfo.getNewInstrate();
            BigDecimal oldInstrate = adjustRateInfo.getOldInstrate();
            Date adjustTime = adjustRateInfo.getInstrateTime();
            rateDate = adjustTime;
            if (balanceDate.compareTo(adjustTime) < 0) {
                rate = oldInstrate;
                rateDate = adjustTime;
                break;
            } else if (balanceDate.compareTo(adjustTime) == 0) {
                rate = newInstrate;
                oldRate = oldInstrate;
                rateDate = adjustTime;
                break;
            } else if (i == adjustSize - 1) {
                if (null != currRate) {
                    if (currRate.getInstrateTime() != null && currRate.getInstrateTime().compareTo(balanceDate) < 0) {
                        currRate.setInstrateTime(balanceDate);
                        currRate.setStartDate(balanceDate);
                    }
                    return currRate;
                }
                rate = newInstrate;
                rateDate = adjustTime;
                break;
            } else {
                it.remove();
            }
            i++;
        }
        return new AccInstrateBean(null, null, balanceDate, rateDate, rate, oldRate);
    }
    /**
     * <b>功能介绍：</b>获取账户的积数，如果有调整积数<br>
     * <b>日期：</b>2017年8月25日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     * @param accuBalance
     * @return
     */
    public BigDecimal getCumuByAccuBalance(FcAccuBalancef accuBalance) {
        BigDecimal acumu = accuBalance.getCumu();
        if (accuBalance.getAdjustCumu() != null) {
            acumu = acumu.add(accuBalance.getAdjustCumu());
        }
        return acumu;
    }
    /**
     * <b>功能介绍：</b>获取借方账号<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    protected FcAccDTO findInterestDebitSysAcc(String netCode, String corpCode, String curCode,String account,String bankId,String userId) {
        return findCommonInterestSysAcc(netCode, corpCode, curCode, getDebitSysAcc(),bankId,userId);
    }
    /**
     * <b>功能介绍：</b>获取贷方账号<br>
     * <b>日期：</b>2017年7月14日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    private FcAccDTO findInterestCreditSysAcc(String netCode, String corpCode, String curCode,String bankId,String userId) {
        return findCommonInterestSysAcc(netCode, corpCode, curCode, getCreditSysAcc(),bankId,userId);
    }
    /**
     * <b>功能介绍：</b>获取借方系统科目<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    public ConstantBean<String> getDebitSysAcc(){
        return null;
    }
    /**
     * <b>功能介绍：</b>获取贷方系统科目<br>
     * <b>日期：</b>2017年7月14日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    public ConstantBean<String> getCreditSysAcc(){
        return null;
    }
    /**
     * <b>功能介绍：</b>获取借方记账账号<br>
     * <b>日期：</b>2020年6月16日<br>
     * <b>作者：</b>liyh<br>
     * @version 1.0
     */
    public String getDebitAccout(FcAccDTO fcAccDTO, boolean interestSign) {
        String interestAcc = null;
        if (interestSign) {//计息
            if (InnerAccountConstant.FCACC_BUSINESS_ACCOUNT.equals(fcAccDTO.getAccType())
                    || InnerAccountConstant.FCACC_AGREEMENT.equals(fcAccDTO.getAccType())) {//企业活期 协定
                return fcAccDTO.getInterestInExp();//借利息支出
            } else if (InnerAccountConstant.FCACC_LOAN.equals(fcAccDTO.getAccType())) {//放款
                return fcAccDTO.getAccForDeduct();//借企业活期
            }
        } else {
            if (InnerAccountConstant.FCACC_BUSINESS_ACCOUNT.equals(fcAccDTO.getAccType())
                    || InnerAccountConstant.FCACC_BANK_BORROW.equals(fcAccDTO.getAccType())
                    || InnerAccountConstant.FCACC_BUSINESS_BEGULAR.equals(fcAccDTO.getAccType())
                    || InnerAccountConstant.FCACC_BANK_DEPOSIT.equals(fcAccDTO.getAccType())
                    || InnerAccountConstant.FCACC_AGREEMENT.equals(fcAccDTO.getAccType())) {//企业活期、银行借款、企业定期、企业通知 协定存款
                return fcAccDTO.getInterestInExp();//借利息支出
            } else if (InnerAccountConstant.FCACC_BANK_DEMAND.equals(fcAccDTO.getAccType()) ||
                    InnerAccountConstant.FCACC_LOAN.equals(fcAccDTO.getAccType()) ||
                    InnerAccountConstant.FCACC_BANK_BEGULAR.equals(fcAccDTO.getAccType())) {//银行活期、企业放款、银行定期
                return fcAccDTO.getInterestRecPay();//借应收利息户
            }
        }
        return null;
    }
    /**
     * <b>功能介绍：</b>获取贷方记账账号<br>
     * <b>日期：</b>2020年6月16日<br>
     * <b>作者：</b>liyh<br>
     * @version 1.0
     */
    public String getCreditAccout(FcAccDTO fcAccDTO, boolean interestSign) {
        if (interestSign) {//计息
            if (InnerAccountConstant.FCACC_BUSINESS_ACCOUNT.equals(fcAccDTO.getAccType())) {//企业活期
                return fcAccDTO.getBlnctrAcc();//贷企业活期
            } else if (InnerAccountConstant.FCACC_LOAN.equals(fcAccDTO.getAccType())) {//放款
                return fcAccDTO.getInterestInExp();//贷利息收入
            } else if (InnerAccountConstant.FCACC_LOAN.equals(fcAccDTO.getAccType())) {//协定
                return fcAccDTO.getRelationAcc();//贷 关联企业活期
            }
        } else {
            if (InnerAccountConstant.FCACC_BUSINESS_ACCOUNT.equals(fcAccDTO.getAccType())
                    || InnerAccountConstant.FCACC_BANK_BORROW.equals(fcAccDTO.getAccType())
                    || InnerAccountConstant.FCACC_BUSINESS_BEGULAR.equals(fcAccDTO.getAccType())
                    || InnerAccountConstant.FCACC_BANK_DEPOSIT.equals(fcAccDTO.getAccType())
                    || InnerAccountConstant.FCACC_AGREEMENT.equals(fcAccDTO.getAccType())) {//企业活期、银行借款、企业定期、企业通知 协定
                return fcAccDTO.getInterestRecPay();//贷应付利息
            } else if (InnerAccountConstant.FCACC_BANK_DEMAND.equals(fcAccDTO.getAccType()) ||
                    InnerAccountConstant.FCACC_LOAN.equals(fcAccDTO.getAccType()) ||
                    InnerAccountConstant.FCACC_BANK_BEGULAR.equals(fcAccDTO.getAccType())) {//银行活期、企业放款、银行定期
                return fcAccDTO.getInterestInExp();//贷利息收入
            }
        }
        return null;
    }
    /**
     * <b>功能介绍：</b>获取利息系统科目账号<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    private FcAccDTO findCommonInterestSysAcc(String netCode, String corpCode, String curCode,
                                              ConstantBean<String> sysAccConstantBean,String bankId,String userId) {
        Set<String> corpCodes = null;
        String subjectId = sysAccConstantBean.get();
        if (!StringUtils.isBlank(corpCode)) {
            corpCodes = new HashSet<String>();
            corpCodes.add(corpCode);
        }
        FcAccDTO commonSysAcc = findAccBySysSubjectCode(subjectId, netCode, corpCodes, curCode,
                sysAccConstantBean.getName(),bankId,corpCode,userId).get(0);
        if (commonSysAcc == null) {
            String corpStr = netCode;
            if (!StringUtils.isBlank(corpCode)) {
                corpStr = corpCode;
            }
            String corpName = iCorpService.get(corpStr).getName();//获取单位名称
            String currentName = "";
            if (StringUtils.isNotEmpty(curCode)) {
                currentName = iBtCurrencyService.getObjectById(curCode).getCurName();
            }
            //单位{0},币别{1},没有开设{2}账户。
            logger.debug("单位["+corpName+"],币别["+currentName+"],没有开设["+sysAccConstantBean.getName()+"]银行代码为["+bankId+"]账户。");
            if(StringUtils.isNotEmpty(bankId) ){
                String bankName = iInputBankInfoService.getByBankCode(bankId).getBankTypeName();
                throw new ParameterException(InterestMessageEnum.FININTEREST_CORPCURBANKNOTACC, corpName, currentName, sysAccConstantBean.getName(),bankName);
            }else {
                throw new ParameterException(InterestMessageEnum.FIN_INTEREST_CORPCURNOTACC, corpName, currentName, sysAccConstantBean.getName());
            }
         }
        return commonSysAcc;
    }
    /**
     * <b>功能介绍：</b>根据系统科目查询账户<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    protected List<FcAccDTO> findAccBySysSubjectCode(String sysSubjectId, String netCode, Set<String> corpCods, String curCode,
                                                     String sysSubjectName,String bankId,String corpId,String userId) {
        List<FcAccDTO> accs = null;
        if(StringUtils.isNotEmpty(bankId)){
            FcSubjectDTO subjectDTO = iFcSubjectService.findSubjectByAccTypeSubCode(sysSubjectId,"07");
            accs = innerAccountService.listAccountByBankParams(curCode,netCode,corpId,subjectDTO.getId(),bankId,userId);
        }else {
            accs = iFcAccService.getFcAccBySysSubjectIdFilterBankCode(null,sysSubjectId, netCode, corpCods, curCode);
        }
        
        if (accs.isEmpty()) {
            accs = new ArrayList<FcAccDTO>();
            accs.add(null);
        }
        return accs;
    }
    /**
     * <b>功能介绍：</b>初始化凭证信息<br>
     * <b>日期：</b>2017年8月29日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     * @param tallyDate
     * @param isRush
     * @param netId
     * @return
     */
    public FcFinacialAccbookVO initAccbook(Date tallyDate, boolean isRush,String netId,String checkUserId,
                                           String opUserId, boolean poolSign, AccInterestBean accBean) {
        FcFinacialAccbookVO accbook = new FcFinacialAccbookVO();
        accbook.setTallyDate(tallyDate);
        accbook.setInterestStartDate(tallyDate);
        accbook.setEnrolTime(new Date());
        accbook.setEmergencySign(FinacialConstant.EMERGENCYSIGN_G);
        accbook.setVoucherState(FinaBookConstant.VoucherStatus.STATUS_TEMP.get());
        if (poolSign) {
            accbook.setBusRaceId(fcBusRacePoolService.getAndSavePoolBusRace(netId, FinaBookConstant.SysRaceCode.NB_POOL_INTEREST, checkUserId));
            accbook.setBusRaceName(fcBusRacePoolService.get(accbook.getBusRaceId()).getRaceName());
            accbook.setGenerateMode(FinaBookConstant.GenerateMode.BANK_POOL.get());
            accbook.setBusOriType(FinaBookConstant.BusOriType.NB_POOL_INTEREST.get());//资金池计息
        } else {
            accbook.setBusRaceId(fcBusRaceService.getAndSaveNetBusRace(netId, FinaBookConstant.SysRaceCode.IST_INTEREST, checkUserId));
            accbook.setBusRaceName(fcBusRaceService.get(accbook.getBusRaceId()).getRaceName());
            accbook.setGenerateMode(FinaBookConstant.GenerateMode.INTEREST.get());
            accbook.setBusOriType(FinaBookConstant.BusOriType.IST_INTEREST_BOOK.get());
        }
        accbook.setOperatorCode(opUserId);//记账人
        accbook.setCheckerCode(checkUserId);//复核人
        accbook.setVoucherType(getVoucherBySysVoucherCode().getId());
        accbook.setFcAccbookDtls(new ArrayList<FcFinacialAccbookDtlVO>());
        if(!EmptyUtils.isEmpty(accBean)){
            if(!EmptyUtils.isEmpty(accBean.getAffixAmount())){
                accbook.setAffixAmount(accBean.getAffixAmount());
            }
        }
        return accbook;
    }
    /**
     * <b>功能介绍：</b>构建凭证明细<br>
     * <b>日期：</b>2017年7月10日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    protected FcFinacialAccbookDtlVO createAccbookDtl(FcAccDTO fcAcc, String commonSummary, BigDecimal debitMoney,
                                                      BigDecimal creditMoney, FcVoucherTypeDTO voucherType, String opUserId) {
        FcFinacialAccbookDtlVO debitAccbookDtl = new FcFinacialAccbookDtlVO();
        debitAccbookDtl.setAccName(fcAcc.getAccName());
        debitAccbookDtl.setAccount(fcAcc.getBlnctrAcc());
        // todo:数据转换
        com.bytter.biz.finacial.api.dto.FcAccDTO fcAccDTO = BeanUtils.copyNewInstanceProperties(fcAcc, com.bytter.biz.finacial.api.dto.FcAccDTO.class);
        debitAccbookDtl.setFcAcc(fcAccDTO);
        debitAccbookDtl.setSummaryContent(commonSummary);
        debitAccbookDtl.setDebitMoney(debitMoney);
        debitAccbookDtl.setCreditMoney(creditMoney);
        debitAccbookDtl.setVoucherTypeDis(voucherType);
        debitAccbookDtl.setVoucherType(debitAccbookDtl.getVoucherTypeDis().getId());
        debitAccbookDtl.setOpUserId(opUserId);
        return debitAccbookDtl;
    }
    protected FcPredrawDtlTempVO createPredrawDtl(InterestInfoContainer interestContainer) {
        FcPredrawDtlTempVO predrawDtl = new FcPredrawDtlTempVO();
        predrawDtl.setInterestTime(interestContainer.getInterestTime());
        predrawDtl.setInterest(BigDecimal.ZERO);
        predrawDtl.setStrikeSign(InterestConstant.STRIKE_SIGN_PART);
        predrawDtl.setSign(InterestConstant.PREDRAW_SIGN_PDW);
        predrawDtl.setInterestDtls(new ArrayList<FcInterestDtlTemp>());
        return predrawDtl;
    }
    /**
     * <b>功能介绍：</b>根据系统凭证种类获取凭证种类，默认系统凭证种类为内转<br>
     * <b>日期：</b>2017年9月11日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     * @param sysVoucherCode
     * @return
     */
    protected FcVoucherTypeDTO getVoucherBySysVoucherCode(String... sysVoucherCode) {
        if (sysVoucherCode.length == 0) {
            return iFcVoucherTypeService.getSysFcVoucherType(NetbankConstant.PAY_TYPE_05);
        }
        return iFcVoucherTypeService.getSysFcVoucherType(sysVoucherCode[0]);
    }
    /**
     * <br>
     * <b>功能：</b> 查找资金池记账规则贷方账户(其他应付款账户) <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2019/5/22 <br>
     */
    public FcAccDTO getPoolCreditSysAcc(String netCode, String curId, String subjectId) {
        List<FcAccDTO> accDTOS = iFcAccService.getNetPoolAccBySubject(netCode, curId, subjectId, InnerAccountConstant.POOL_SIGN_1);
        if (EmptyUtils.isEmptyList(accDTOS)) {
            FcSubjectDTO subjectDTO = iFcSubjectService.findSubjectById(subjectId);
            throw new ParameterException(InterestMessageEnum.FIN_INTEREST_NOTOPEN_SUBJECTACC, "(" + subjectDTO.getSubjectCode()+")" + subjectDTO.getSubjectName());
        }
        return accDTOS.get(0);
    }
    /**
     * <b>功能介绍：</b> 根据两个日期求相差天数 正数表示d1大于d2天数 负数表示d1小于d2天数<br>
     * <b>日期：</b>2017年7月9日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    protected int dayDiffs(Date d1, Date d2) {
        if (d1 == null || d2 == null) {
            return 0;
        }
        long tt = d1.getTime() - d2.getTime();
        int days = (int) (tt / (24 * 60 * 60 * 1000));
        return days;
    }
    /**
     * <b>功能介绍：</b>计算年月日天数差<br>
     * <b>日期：</b>2017年7月9日<br>
     * <b>作者：</b>caozx<br>
     * @version 1.0
     */
    protected int getDateDiff(Date startDate,Date endDate){
        long number = (endDate.getTime() - startDate.getTime())
                / (1000 * 60 * 60 * 24);
        double number_s = (endDate.getTime() - startDate.getTime())
                % (1000 * 60 * 60 * 24);
        if (number_s > 0) {
            number = number + 1;
        }
        if(number_s<0){
            number = number -1;
        }
        return (int) number;
    }
    public void sortInterestVoucher(AccInterestInfo accInterestInfo) {
        List<FcFinacialAccbookVO> accbooks = accInterestInfo.getInterestAccbook();
        sortAccbookDtl(accbooks);
    }
    public void sortAccbookDtl(List<FcFinacialAccbookVO> accbooks) {
        if(EmptyUtils.isEmptyList(accbooks)){
            return;
        }
        for(FcFinacialAccbookVO accbookVO : accbooks) {
            List<FcFinacialAccbookDtlVO> fcAccbookDtls = accbookVO.getFcAccbookDtls();
            Collections.sort(fcAccbookDtls, new Comparator<FcFinacialAccbookDtlVO>(){//网点排序
                public int compare(FcFinacialAccbookDtlVO o1, FcFinacialAccbookDtlVO o2) {
                    //借贷
                    if(o1.getDebitMoney().compareTo(BigDecimal.ZERO) != 0 && o2.getDebitMoney().compareTo(BigDecimal.ZERO) == 0){
                        return -1;
                    }
                    //贷借
                    if(o1.getDebitMoney().compareTo(BigDecimal.ZERO) == 0 && o2.getDebitMoney().compareTo(BigDecimal.ZERO) != 0){
                        return 1;
                    }
                    if(o1.getFcAcc().getCorpId().equals(o2.getFcAcc().getCorpId())){
                        return o2.getAccount().compareTo(o1.getAccount());
                    } else {
                        return o2.getFcAcc().getCorpId().compareTo(o1.getFcAcc().getCorpId());
                    }
                }
            });
        }
    }
    public void sortPredrawVoucher(AccInterestInfo accInterestInfo) {
        List<FcFinacialAccbookVO> accbooks = accInterestInfo.getPredrawAccbook();
        sortAccbookDtl(accbooks);
    }
    public void sortRushVoucher(AccInterestInfo accInterestInfo) {
        List<FcFinacialAccbookVO> accbooks = accInterestInfo.getRushAccbook();
        sortAccbookDtl(accbooks);
    }
    /**
     * <br>
     * <b>功能：</b> 更新积数的计息日期(不能是已计息的数据) <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/4/22 0022 <br>
     */
    @Override
    public void updateAccuBalancesInterestSign(String businessId, List<String> ids, Date interestTime) {
        //保存还款ID与拆分积数的关联关系
        //loanRepayService.saveLoanBalanceDtls(businessId, ids, interestTime);
    }
}
