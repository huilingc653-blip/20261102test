package com.bytter.tally.service;

import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.NbconsoleConstant;
import com.bytter.biz.finacial.api.dto.*;
import com.bytter.biz.finacial.api.enums.accbook.AccbookMessageEnum;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.biz.finacial.api.enums.bankCollect.CollectMessageEnum;
import com.bytter.biz.finacial.api.service.IFcAccbookService;
import com.bytter.biz.finacial.api.service.IFcBusRaceService;
import com.bytter.biz.finacial.api.service.ITallyService;
import com.bytter.biz.finacial.paramconfig.service.FcBusinessRuleDefineService;
import com.bytter.biz.finacial.paramconfig.vo.FcNisBusTypeRuleVO;
import com.bytter.biz.finacial.paramconfig.vo.FcOtherBusinessRuleDefineVo;
import com.bytter.biz.finacial.paramconfig.vo.FcVoucherEntryDefineVo;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.enums.IMessageEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.system.api.dto.*;
import com.bytter.tally.dao.BtNetOtherReceiveDtlDao;
import com.bytter.tally.vo.FcAccbookNotifyVO;
import com.bytter.tally.vo.FcBusRaceVo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author lijinquan
 * @description:
 * @date 2024-11-01 14:54
 */
@Service("btNetOtherReceiveDtlService")
@Transactional
@Slf4j
public class BtNetOtherReceiveDtlService {

    @Autowired
    private BtNetOtherReceiveDtlDao btNetOtherReceiveDtlDao;
    @DubboReference(check = false)
    private IBtBankAccService bankAccService;
    @DubboReference(check = false)
    private IFcAccService fcAccService;
    @DubboReference(check = false)
    private IFcAccbookService iFcAccbookService;
    @DubboReference(check = false)
    private IFcBusRaceService iFcBusRaceService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private FcBusinessRuleDefineService fcBusinessRuleDefineService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IUserService userService;
    @DubboReference(check = false)
    private IBisAccDtlService bisAccDtlService;
    @DubboReference(check = false)
    private ITallyService tallyService;
    
    /**
     * 英文状态下逗号作为分隔符
     */
    public static final String COMMA_SPLIT = ",";
    
    public List<BtNetOtherDtlDTO> queryTallyData(String num) {
        return btNetOtherReceiveDtlDao.queryTallyData(num);
    }

    /**
     * 判断是否自动复核
     */
    public boolean isAutoCheck() {
        boolean isAutoCheck = CmsConstant.IS_TRUE.equals(configService.getConfig("fcl", "tally_check").getValue());
        return isAutoCheck;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String processAutoTally(BtNetChalkDTO btNetChalk,FcOtherBusinessRuleDefineDTO ruleDefine) {
        String msgType = ""; //1.该单位的自动记账人或自动复核人为空 2.fcAccbook凭证构建失败
        FcAccbookVoucherDTO fcAccbook = processAutoAccbook(btNetChalk, ruleDefine, btNetChalk.getCreateBy());
        if (fcAccbook != null) {
            String autoUser = getAutoAccounter(btNetChalk.getCorpId(), true);//记账人
            String checkUser = getAutoCheckUser(btNetChalk.getCorpId(), true);//复核人
            if (!"false".equals(autoUser) && !"false".equals(checkUser)) {
                submit(fcAccbook, btNetChalk.getId(), autoUser,checkUser);
            }else {
                msgType = "1";
            }
        }else {
            msgType = "2";
        }
        return msgType;
    }


    public FcAccbookVoucherDTO processAutoAccbook(BtNetChalkDTO btNetChalkVo, FcOtherBusinessRuleDefineDTO ruleDefine, String userId) {
        FcAccbookVoucherDTO entity = new FcAccbookVoucherDTO();
        FcAccbookDTO accBook = new FcAccbookDTO();
        FcAccbookExtDTO bookExt = new FcAccbookExtDTO();
        List<FcAccbookDtlDTO> accbookDtls = null;
        accbookDtls = getAutoAccbookDtls(ruleDefine, btNetChalkVo);
        if (accbookDtls == null) {
            return null;
        }
//        bookExt.setExternalCorp(btNetChalkVo.getOppAccName());//外单位
//        bookExt.setExternalAcc(btNetChalkVo.getOppAccNo());//外单位账号
//        bookExt.setExternalBank(btNetChalkVo.getOppBankName());//外单位开户行
//        BtBankAccDTO bankAcc = bankAccService.getBankAccByBankAccAndCurId(btNetChalkVo.getOppAccNo(), btNetChalkVo.getCurId());
//        if (!EmptyUtils.isEmpty(bankAcc)) {
//            bookExt.setProv(bankAcc.getProv());//所在省市
//            bookExt.setCity(bankAcc.getCity());
//            BisAccDtlDTO bisAcc = bisAccDtlService.getById(bankAcc.getId());
//            if (!EmptyUtils.isEmpty(bisAcc)) {
//                bookExt.setRmk(bisAcc.getUses());
//            } else {
//                bookExt.setRmk("");
//            }
//        }
        accBook.setRmk(btNetChalkVo.getPurpose());//备注
        accBook.setEmergencySign(FinacialConstant.EMERGENCYSIGN_G);//加急标志
        FcBusRaceVo fcbusRace = getFcBusRaceId(ruleDefine.getSysRaceCode());
        if (fcbusRace != null) {
            accBook.setBusRaceId(fcbusRace.getId());
            accBook.setBusRaceName(fcbusRace.getRaceName());
        }
        accBook.setVoucherType(ruleDefine.getVoucherType());
        accBook.setInterestStartDate(btNetChalkVo.getTransTime());
        accBook.setTallyDate(btNetChalkVo.getTransTime());
        accBook.setOperatorCode(userId);
        accBook.setCheckerCode(userId);
        entity.setAccbook(accBook);
        entity.setBookExt(bookExt);
        entity.setBookDtls(accbookDtls);
        return entity;
    }


    /**
     * 构造银企接口的会计分录
     */
    public List<FcAccbookDtlDTO> getAutoAccbookDtls(FcOtherBusinessRuleDefineDTO ruleDefine, BtNetChalkDTO btNetChalk) {
        List<FcVoucherEntryDefineDTO> entrys = ruleDefine.getVouchers();
        FcAccbookDtlDTO accbookDtl = null;
        List<FcAccbookDtlDTO> accbookDtls = new ArrayList<FcAccbookDtlDTO>();
        for (FcVoucherEntryDefineDTO entry : entrys) {
            if (entry.getBalanceWay().intValue() == 1 && entry.getEntryType().intValue() == 10) {
                //借方
                accbookDtl = new FcAccbookDtlDTO();
                accbookDtl.setCreditMoney(BigDecimal.ZERO);
                accbookDtl.setDebitMoney(btNetChalk.getMoney());
                accbookDtl.setLineNo(1);

                //规则默认配置内部账户
                if(StringUtils.isNotEmpty(entry.getBlnctrAcc())){
                    FcAccDTO fcAcc = fcAccService.getAccDTOByAccount(entry.getBlnctrAcc());
                    if(fcAcc != null){
                        //科目id相等
                        if(fcAcc.getSubjectId().equals(entry.getSubjectCode())){
                            accbookDtl.setAccount(fcAcc.getBlnctrAcc());
                            accbookDtl.setAccName(fcAcc.getAccName());
                        }else {
                            accbookDtls = null;
                            break;
                        }
                    }else {
                        accbookDtls = null;
                        break;
                    }
                }else {//规则未配置默认内部账户
                    List<BtBankAccCurDTO> accList = btNetOtherReceiveDtlDao.getCountertAcc(btNetChalk.getBankAcc());
                    if (accList != null && accList.size() > 0 && accList.get(0).getCounterAcc() != null) {
                        String counterAcc = accList.get(0).getCounterAcc();
                        FcAccDTO fcAcc = fcAccService.getAccDTOByAccount(counterAcc);
                        accbookDtl.setAccount(fcAcc.getBlnctrAcc());
                        accbookDtl.setAccName(fcAcc.getAccName());
                    } else {
                        accbookDtls = null;
                        break;
                    }
                }
            } else if (entry.getBalanceWay().intValue() == -1 && entry.getEntryType().intValue() == 10) {
                //贷方
                accbookDtl = new FcAccbookDtlDTO();
                accbookDtl.setDebitMoney(BigDecimal.ZERO);
                accbookDtl.setCreditMoney(btNetChalk.getMoney());
                accbookDtl.setLineNo(2);

                //规则默认配置内部账户
                if(StringUtils.isNotEmpty(entry.getBlnctrAcc())){
                    FcAccDTO fcAcc = fcAccService.getAccDTOByAccount(entry.getBlnctrAcc());
                    if(fcAcc != null){
                        //科目id相等
                        if(fcAcc.getSubjectId().equals(entry.getSubjectCode())){
                            accbookDtl.setAccount(fcAcc.getBlnctrAcc());
                            accbookDtl.setAccName(fcAcc.getAccName());
                        }else {
                            accbookDtls = null;
                            break;
                        }
                    }else {
                        accbookDtls = null;
                        break;
                    }
                }else {//规则未配置默认内部账户，则通过对方账户匹配内部账户
                    List<BtBankAccCurDTO> accList = btNetOtherReceiveDtlDao.getCountertAcc(btNetChalk.getOppAccNo(),entry.getSubjectCode());
                    if (accList != null && accList.size() > 0 && accList.get(0).getCounterAcc() != null) {
                        String counterAcc = accList.get(0).getCounterAcc();
                        FcAccDTO fcAcc = fcAccService.getAccDTOByAccount(counterAcc);
                        accbookDtl.setAccount(fcAcc.getBlnctrAcc());
                        accbookDtl.setAccName(fcAcc.getAccName());
                    } else {
                        accbookDtls = null;
                        break;
                    }
                }

            }
            accbookDtl.setSummaryContent(btNetChalk.getPurpose());
            accbookDtl.setOrigVoucherNo(btNetChalk.getId());
            accbookDtl.setOrigVoucherId(btNetChalk.getId());
            accbookDtl.setVoucherType(entry.getVoucherType());
            accbookDtls.add(accbookDtl);
        }
        return accbookDtls;
    }


//    /**
//     * 判断是否跨网点收款
//     */
//    public String getBtNetChalkAcc(FcAccbookPageDTO entity, String payeeCorpId, BtBankAccDTO payerAccInfo, FcAccbookExtVO bookExt) {
//        String payeeBlnctrAcc = "";
//        String superCurrentAcc = "";
//        String payerCorpId = payerAccInfo.getCorpId();//付款单位
//        CorpDTO payerCorp = corpService.get(payerCorpId);
//        Integer type = payerCorp.getType();//0-网点，1-单位
//        if (!payerCorp.getId().equals(payeeCorpId) && (type == 0)) {//跨网点调拨
//            String payerSuperNetId = payerCorp.getParentId() == null ? payerCorp.getId() : payerCorp.getParentId();//获取上级网点id
//            String blnctrAcc = bankAccService.getBlnctrAcc(payerAccInfo.getBankAcc(), payerAccInfo.getCurList().get(0));
//            payeeBlnctrAcc = blnctrAcc;//本级关联联行账号-内部账号
//            if (payerSuperNetId.equals(payeeCorpId)) {//说明是跨网点调拨,付款方上级网点和收款方网点对比
//                //上级内部账号
//                superCurrentAcc = bankAccService.getCountertAcc(payerAccInfo.getBankAcc()).get(0).getSuperCounterAcc();
//            } else {
//                superCurrentAcc = bankAccService.getCountertAcc(payerAccInfo.getBankAcc()).get(0).getCounterAcc();
//            }
//            bookExt.setStartBank(payerAccInfo.getBankName());//委托收款的收款银行
//            bookExt.setStartCorp(payerCorp.getName());//委托收款的收款单位
//            bookExt.setStartCorpAcc(payeeBlnctrAcc);//第三方账号,子账号银行活期,委托收款的收款账号
//        } else {
//            superCurrentAcc = bankAccService.getCountertAcc(payerAccInfo.getBankAcc()).get(0).getCounterAcc();
//        }
//        return superCurrentAcc;
//    }

    /**
     * 判断付方账号,如果为空或不是系统中存在的账号，则需要手工选择贷方账号
     */
    public boolean isExistBankAcc(String bankAcc) {
        BtBankAccDTO bank = bankAccService.getBankAccInfo(bankAcc);
        return bank != null;
    }


    /**
     * 功能介绍 : 初始化手工记账凭证
     */
    private void setAccBookVoucher(FcAccbookVoucherDTO voucher) {
        createVoucher(voucher);
        List<FcAccbookDtlDTO> bookDtls = new ArrayList<FcAccbookDtlDTO>();
        // 初始化数据
        for (FcAccbookDtlDTO bookDtl : voucher.getBookDtls()) {
            if (null == bookDtl.getDebitMoney()) {
                bookDtl.setDebitMoney(BigDecimal.ZERO);
            }
            if (null == bookDtl.getCreditMoney()) {
                bookDtl.setCreditMoney(BigDecimal.ZERO);
            }
            // 如果明细数据为空，则不保存非空明细
            if (StringUtils.isEmpty(bookDtl.getSummaryContent()) && StringUtils.isEmpty(bookDtl.getAccount()) && null == bookDtl.getDebitMoney() && null == bookDtl.getCreditMoney()) {
            } else {
                bookDtls.add(bookDtl);
            }
        }
        // 明细进行先借后贷的排序
        Collections.sort(bookDtls, new Comparator<FcAccbookDtlDTO>() {
            public int compare(FcAccbookDtlDTO o1, FcAccbookDtlDTO o2) {
                return o1.getCreditMoney().abs().compareTo(BigDecimal.ZERO);
            }
        });
        voucher.setBookDtls(bookDtls);
    }


    /**
     * 获取网点自动记账人
     */
    public String getAutoAccounter(String netId, Boolean isAutoTally) {
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.FCL, "autoAccounter");
        String paramValue = configDTO.getValue();
        if (EmptyUtils.isEmpty(paramValue)) {//自动记账人为空
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_AUTOCHECKER_NULL);
        }
        paramValue = paramValue.replaceAll("；", ";").replaceAll("，", ";").replaceAll(",", ";");
        String[] netUsers = paramValue.split(";");//分割
        Map<String, String> userInfo = new HashMap<String, String>();
        for (String netUser : netUsers) {
            String[] temps = netUser.split("=");//'='分割为网点和记账人
            if (temps.length == 2) {
                userInfo.put(temps[0].trim(), temps[1].trim());
            }
        }
        CorpDTO corpDTO = corpService.get(netId);
        String userCode = userInfo.get(corpDTO.getCode());//获取记账人
        if (EmptyUtils.isEmpty(userCode)) {
            if (isAutoTally) {
                return "false";
            }
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_AUTOCHECKER_NETNULL, corpDTO.getName() + "(" + corpDTO.getCode() + ")");
        }
        com.bytter.system.api.dto.UserDTO userDTO = userService.getByLoginName(userCode);
        if (null != userDTO) {
            return userDTO.getId();
        } else {
            if (isAutoTally) {
                return "false";
            }
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_USER_NOTEXIST, userCode);
        }
    }

    /**
     * 获取网点自动复核人
     */
    public String getAutoCheckUser(String netId, Boolean isAutoTally) {
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.FCL, "autoChecker");
        String paramValue = configDTO.getValue();
        if (EmptyUtils.isEmpty(paramValue)) {//自动记账人为空
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_AUTOCHECKER_NULL);
        }
        paramValue = paramValue.replaceAll("；", ";").replaceAll("，", ";").replaceAll(",", ";");
        String[] netUsers = paramValue.split(";");//分割
        Map<String, String> userInfo = new HashMap<String, String>();
        for (String netUser : netUsers) {
            String[] temps = netUser.split("=");//'='分割为网点和记账人
            if (temps.length == 2) {
                userInfo.put(temps[0].trim(), temps[1].trim());
            }
        }
        CorpDTO corpDTO = corpService.get(netId);
        String userCode = userInfo.get(corpDTO.getCode());//获取复核人
        if (EmptyUtils.isEmpty(userCode)) {
            if (isAutoTally) {
                return "false";
            }
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_AUTOCHECKER_NETNULL, corpDTO.getName() + "(" + corpDTO.getCode() + ")");
        }
        UserDTO userDTO = userService.getByLoginName(userCode);
        if (null != userDTO) {
            return userDTO.getId();
        } else {
            if (isAutoTally) {
                return "false";
            }
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_USER_NOTEXIST, userCode);
        }
    }

    private void createVoucher(FcAccbookVoucherDTO voucher) {
        voucher.getAccbook().setBusOriType(getOriType());
    }

    /**
     * 返回记账的业务种类
     */
    public Integer getOriType() {
        return FinaBookConstant.BusOriType.BIS_ACC_DTL_OTHER_RECEIVE.get();
    }

    //凭证封装对象
    private VoucherWrapperDTO setVoucherWrapperDTO(FcAccbookVoucherDTO fcAccbook, String userId) {
        VoucherWrapperDTO voucher = new VoucherWrapperDTO();
        voucher.setBusGlobalId(fcAccbook.getAccbook().getBusGlobalId());
        voucher.addTarget(fcAccbook);
        voucher.setProcessType(ProcessType.PROCESS_COMMON);
        voucher.setUserCode(userId);//设置用户
        voucher.setAutoAccounter(false);
        return voucher;
    }


    /**
     * 功能介绍 : 记账提交凭证对象
     */
    public void submit(FcAccbookVoucherDTO fcAccbook, String id, String autoUser,String checkUser) {
        check(fcAccbook);
        BtNetChalkDTO entity = queryBtNetChalkDTO(id);
        setAccBookVoucher(fcAccbook);
        VoucherWrapperDTO voucher = setVoucherWrapperDTO(fcAccbook, autoUser);
        boolean isAutoCheck = true; //自动记账复核通过
        if (isAutoCheck) {
            //获取自动复核员用户
            voucher.setCheckUser(checkUser);
            voucher.setAutoCheck(true);
        }

        FcAccbookDTO accbook = iFcAccbookService.submitFcAccbook(voucher);
        //待记账消息提醒完成
        notificationService.complete(NotificationConstant.FINACIAL.NETCHALK_TALLY, entity.getId(), autoUser);
        //如果自动复核，则复核凭证通过
        completeEditNotificationInfo(voucher, autoUser);
        completeCheckNotificationInfo(voucher, autoUser);
        String eState = entity.getEstate();//原状态
        entity.setOrderNo(accbook.getOrderNo());
        entity.setOperatorCode(accbook.getOperatorCode());
        entity.setTallyDate(accbook.getTallyDate());
        entity.setVoucherNo(accbook.getVoucherNo());
        entity.setCheckerCode(accbook.getCheckerCode());
        entity.setCheckTime(accbook.getCheckTime());
        entity.setBookId(accbook.getId());
        entity.setBusGlobalId(accbook.getBusGlobalId());
        entity.setEstate(NbconsoleConstant.NbcEstate.E_STATE_WAIT_CHECK.getValue());
        //更新付款单记账信息
        int count = 0;
        String newEState = NbconsoleConstant.NbcEstate.E_STATE_WAIT_CHECK.getValue();
        List<String> idList = Lists.newArrayList();
        idList.add(entity.getId());
        if (isAutoCheck) {
            //更新付款单复核信息
            count = btNetOtherReceiveDtlDao.updateAutoCheckSign(entity, idList);
        } else {
            //记账提交
            count = btNetOtherReceiveDtlDao.updateTallySign(entity, idList, newEState);
        }

        if (count < 1) {
            throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_STATUSCHANGE);//状态已改变，刷新页面重试
        }

    }


    /**
     * 提交前数据校验
     */
    private void check(FcAccbookVoucherDTO fcAccbook) {
        if (EmptyUtils.isEmpty(fcAccbook.getAccbook().getVoucherType())) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_VOUCHERTYPE_NO);
        }
        if (EmptyUtils.isEmpty(fcAccbook.getAccbook().getInterestStartDate())) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_INTERESTSTARTDATE_NO);
        }
        if (EmptyUtils.isEmpty(fcAccbook.getAccbook().getTallyDate())) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_TALLYDATE_NO);
        }
        if (EmptyUtils.isEmpty(fcAccbook.getAccbook().getEmergencySign())) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_EMERGENCYSIGN_NO);
        }
        if (EmptyUtils.isEmpty(fcAccbook.getAccbook().getBusRaceId())) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_BUSRACE_NO);
        }
        for (FcAccbookDtlDTO accbookDtl : fcAccbook.getBookDtls()) {
            if (EmptyUtils.isEmpty(accbookDtl.getAccount())) {
                throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_ACCOUNT_NO);
            }
            if (EmptyUtils.isEmpty(accbookDtl.getSummaryContent())) {
                throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_SUMMARYCONTENT_NO);
            }
        }
    }

    /**
     * 获取业务总类id
     */
    public FcBusRaceVo getFcBusRaceId(String sysRaceCode) {
        List<FcBusRaceDTO> fcBusRaceDTOS = iFcBusRaceService.findBySysRaceCode(sysRaceCode);
        List<FcBusRaceVo> raceList = BeanUtils.copyListProperties(fcBusRaceDTOS, FcBusRaceVo.class);
        if (raceList != null && raceList.size() > 0) {
            return raceList.get(0);
        }
        return null;
    }


    /**
     * 发送消息提醒
     */
    private void sendCheckNotificationInfo(VoucherWrapperDTO voucher, String userId) {
        for (FcAccbookVoucherDTO voucherVO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherVO.getAccbook();
            // 发送消息复核提醒
            FcAccbookNotifyVO notifyVO = new FcAccbookNotifyVO();
            notifyVO.setOrderNo(accbook.getOrderNo());
            List<FcAccbookDtlDTO> bookDtls = voucherVO.getBookDtls();
            BigDecimal money = BigDecimal.ZERO;
            if (!EmptyUtils.isEmptyList(bookDtls)) {
                for (FcAccbookDtlDTO accbookDtl : bookDtls) {
                    if (null != accbookDtl.getDebitMoney()) {
                        money = money.add(accbookDtl.getDebitMoney());
                    }
                }
            }
            notifyVO.setMoney(money);
            notificationService.sendIgnoreMySelf(accbook.getId(), accbook.getNetId(), NotificationConstant.FINACIAL.NETCHALK_CHECK, notifyVO, userId);
        }
    }



    /**
     * <br>
     * <b>功能：</b> 设置凭证网点 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2022/10/9 0009 <br>
     */
    public void setVoucherNet(VoucherWrapperDTO voucher) {
        FcAccbookVoucherDTO voucherVO = voucher.getBookVoucher();
        if (!EmptyUtils.isEmpty(voucherVO)) {
            String netCode = voucherVO.getNetCode();//记账网点
            if (!EmptyUtils.isEmpty(netCode)) {
                return;
            }
            FcAccbookDTO accbook = voucherVO.getAccbook();
            netCode = accbook.getNetId();//记账网点
            if (!EmptyUtils.isEmpty(netCode)) {
                voucher.setNetId(netCode);
                return;
            }
            List<FcAccbookDtlDTO> accbookDtls = voucherVO.getBookDtls();
            if (!EmptyUtils.isEmptyList(accbookDtls)) {
                FcAccbookDtlDTO accbookDtl = accbookDtls.get(0);
                String account = accbookDtl.getAccount();
                FcAccDTO accDTO = fcAccService.getAccDTOByAccount(account);
                netCode = accDTO.getNetId();//账号网点
                if (!EmptyUtils.isEmpty(netCode)) {
                    voucher.setNetId(netCode);
                }
            }
        }
    }


    /**
     * 消息完成
     */
    private void completeEditNotificationInfo(VoucherWrapperDTO voucher, String userId) {
        for (FcAccbookVoucherDTO voucherVO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherVO.getAccbook();
            notificationService.complete(NotificationConstant.FINACIAL.NETCHALK_EDIT, accbook.getId(), userId);
        }
    }

    /**
     * 消息完成
     */
    private void completeCheckNotificationInfo(VoucherWrapperDTO voucher, String userId) {
        for (FcAccbookVoucherDTO voucherVO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherVO.getAccbook();
            //打回改账处，通知提醒记账人
            notificationService.complete(NotificationConstant.FINACIAL.NETCHALK_CHECK, accbook.getId(), userId);
        }
    }

    /**
     * 查询BtNetChalkDTO
     */
    @Transactional(readOnly = true)
    public BtNetChalkDTO queryBtNetChalkDTO(String id) {
        return tallyService.queryBtNetChalkDTO(id);
    }

    /**
     * <b>功能：</b>对外查询业务规则<br>
     * <b>作者：</b>lixi<br>
     * <b>日期：</b>2017-8-16<br>
     */
    public Map<String,FcOtherBusinessRuleDefineDTO> getBusinessRuleDefines(List<String> netIds){
        List<FcOtherBusinessRuleDefineDTO> dtoList = new ArrayList<FcOtherBusinessRuleDefineDTO>();
                
        List<FcOtherBusinessRuleDefineVo> voList = btNetOtherReceiveDtlDao.getBusinessRuleExp(netIds);
        for (FcOtherBusinessRuleDefineVo vo : voList) {
            FcOtherBusinessRuleDefineDTO dto = new FcOtherBusinessRuleDefineDTO();
            BeanUtils.copyNotNullProperties(vo, dto);
            List<FcVoucherEntryDefineVo> define = fcBusinessRuleDefineService.getVoucher(vo.getId());
            if(EmptyUtils.isEmptyList(define)){
                continue; //记账规则分录不能为空
            }
            List<FcVoucherEntryDefineDTO> tempEntry = BeanUtils.copyListProperties(define, FcVoucherEntryDefineDTO.class);
            dto.setVouchers(tempEntry);
            dtoList.add(dto);
        }
        
        if(dtoList.size() == 0) return new HashMap<>();

        Map<String,FcOtherBusinessRuleDefineDTO> defineMap = new HashMap<>();
        for (FcOtherBusinessRuleDefineDTO dto : dtoList) {
            defineMap.put(dto.getNetId(),dto);
        }
        return defineMap;
    }

    /**
     * <b>功能：</b>对外查询业务规则<br>
     * <b>作者：</b>lixi<br>
     * <b>日期：</b>2017-8-16<br>
     */
    public Map<String,List<FcOtherBusinessRuleDefineDTO>> getBusinessRuleDefinesBy(List<String> netIds){
        List<FcOtherBusinessRuleDefineDTO> dtoList = new ArrayList<FcOtherBusinessRuleDefineDTO>();

        List<FcOtherBusinessRuleDefineVo> voList = btNetOtherReceiveDtlDao.getBusinessRuleExp(netIds);
        for (FcOtherBusinessRuleDefineVo vo : voList) {
            FcOtherBusinessRuleDefineDTO dto = new FcOtherBusinessRuleDefineDTO();
            BeanUtils.copyNotNullProperties(vo, dto);
            List<FcVoucherEntryDefineVo> define = fcBusinessRuleDefineService.getVoucher(vo.getId());
            if(EmptyUtils.isEmptyList(define)){
                continue; //记账规则分录不能为空
            }
            List<FcVoucherEntryDefineDTO> tempEntry = BeanUtils.copyListProperties(define, FcVoucherEntryDefineDTO.class);
            dto.setVouchers(tempEntry);
            dtoList.add(dto);
        }

        if(dtoList.size() == 0) return new HashMap<>();

        Map<String,List<FcOtherBusinessRuleDefineDTO>> map = new HashMap<>();
        for (FcOtherBusinessRuleDefineDTO dto : dtoList) {
            String key = dto.getNetId() + ";" + dto.getCurCode();
            if(map.containsKey(key)){
                map.get(key).add(dto);
            }else {
                List<FcOtherBusinessRuleDefineDTO> list = new ArrayList<>();
                list.add(dto);
                map.put(key,list);
            }
        }
        return map;
    }

    /**
     * 记录失败原因
     * @param id
     * @param estate
     * @param message
     */
    public void updateTallyFailMsg(String id, String estate, String message) {
        btNetOtherReceiveDtlDao.updateTallyFailMsg(id,estate,message);
    }

    public String getLang(IMessageEnum exceptionEnum, String... params) {
        String langKey = buildLangKey(exceptionEnum);
        String langContext = btNetOtherReceiveDtlDao.getByCodeAndKey("zh_CN",langKey);
        langContext = MessageFormat.format(langContext, params);
        return langContext;
    }

    /**
     * 组装langKey
     *
     * @param exceptionEnum
     * @return
     */
    private String buildLangKey(IMessageEnum exceptionEnum) {
        String enumName = exceptionEnum.getEnum().name();
        return enumName.replace("_", ".").toLowerCase();
    }

    public boolean checkRuleDefine(BtNetOtherDtlDTO dto, FcOtherBusinessRuleDefineDTO ruleDefineDTO) throws Exception{
        //是否满足匹配规则
        Boolean allCheck = false;
        List<Boolean> bList = new ArrayList<>();
        //同级间的关系匹配规则  01-或者  02-并且
        List<String> matchRuleList = new ArrayList<>();

        List<List<FcNisBusTypeRuleDTO>> nisBusTypeRuleDTOList =  this.getNisBusTypeRuleDTOByBusId(ruleDefineDTO.getId());
        for(List<FcNisBusTypeRuleDTO> list : nisBusTypeRuleDTOList) { //开始匹配
            Boolean isCheck = false;
            String matchRuleTemp = "";
            int count = 0;
            for (FcNisBusTypeRuleDTO nisBusTypeRuleDTO : list) {
                if (StringUtils.isNotEmpty(nisBusTypeRuleDTO.getMatchElements())) {
                    String matchRule = nisBusTypeRuleDTO.getMatchRule().trim();
                    //取第一个的
                    if(count ==0){
                        matchRuleTemp = matchRule;
                    }
                    if (StringUtils.isEmpty(matchRule.trim()) || matchRule.equals("02")) {
                        if(count>0 && !isCheck){
                            continue;
                        }
                        count ++;
                        /**
                         *  将属性的首字符大写，方便构造get，set方法
                         */
                        String upName = nisBusTypeRuleDTO.getMatchElements().substring(0, 1).toUpperCase() + nisBusTypeRuleDTO.getMatchElements().substring(1);
                        if (upName.equals("CorpId")) {
                            upName = "CorpName";
                        }
                        if (upName.equals("CurId")) {
                            upName = "CurName";
                        }
                        Method m = dto.getClass().getMethod("get" + upName, new Class[0]);
                        if (!"Amt".equals(upName)) {
                            // 调用getter方法获取属性值
                            String value = (String) m.invoke(dto);
                            String matchRelations = nisBusTypeRuleDTO.getMatchRelations();
                            String matchContent = nisBusTypeRuleDTO.getMatchContent();
                            if(value == null || StringUtils.isEmpty(value.trim())){
                                value = "";
                            }
                            if(StringUtils.isEmpty(matchContent.trim())){
                                matchContent = "";
                            }
                            if ("1".equals(matchRelations)) {
                                //包含
                                String[] matchContents = matchContent.split(this.COMMA_SPLIT);
                                for (String contents:matchContents) {
                                    isCheck = value.contains(contents);
                                    if (isCheck) {
                                        break;
                                    }
                                }

                            } else if ("2".equals(matchRelations)) {
                                //不包含
                                String[] matchContents = matchContent.split(this.COMMA_SPLIT);
                                for (String contents:matchContents) {
                                    isCheck = !value.contains(contents);
                                    if (!isCheck) {
                                        break;
                                    }
                                }
                            } else if ("3".equals(matchRelations)) {
                                //等于
                                isCheck = value.equals(matchContent);

                            } else if ("4".equals(matchRelations)) {
                                //不等于
                                isCheck = !value.equals(matchContent);
                            } else if ("9".equals(matchRelations)) {
                                //在......之内
                                String[] matchContents = matchContent.split(this.COMMA_SPLIT);
                                for (String contents:matchContents) {
                                    if (value.equals(contents)) {
                                        isCheck = true;
                                        break;
                                    }
                                    isCheck = false;
                                }
                            } else if ("10".equals(matchRelations)) {
                                //不在......之内
                                String[] matchContents = matchContent.split(this.COMMA_SPLIT);
                                for (String contents:matchContents) {
                                    if (value.equals(contents)) {
                                        isCheck = false;
                                        break;
                                    }
                                    isCheck = true;
                                }
                            }else if ("11".equals(matchRelations)) {
                                //为空
                                isCheck = value == matchContent;
                            }else if ("12".equals(matchRelations)) {
                                //不为空
                                isCheck = value != matchContent;;
                            }else {
                                isCheck = false;
                            }
                        }else{
                            BigDecimal value = (BigDecimal) m.invoke(dto);
                            String matchRelations = nisBusTypeRuleDTO.getMatchRelations();
                            String matchContent = nisBusTypeRuleDTO.getMatchContent();
                            if ("1".equals(matchRelations) || "2".equals(matchRelations)
                                    || "3".equals(matchRelations) || "4".equals(matchRelations)) {
                                //判断金额为整数
                                if (new BigDecimal(value.intValue()).compareTo(value) == 0) {
                                    int b = value.intValue();
                                    //判断是否为奇数
                                    if ("奇数".equals(matchContent)) {
                                        if (b % 2 != 0) {
                                            isCheck = true;
                                        } else {
                                            isCheck = false;
                                        }
                                        //判断是否为偶数
                                    } else if ("偶数".equals(matchContent)) {
                                        if (b % 2 == 0) {
                                            isCheck = true;
                                        } else {
                                            isCheck = false;
                                        }
                                    } else {
                                        isCheck = false;
                                    }
                                } else {
                                    isCheck = false;
                                }
                            } else {
                                BigDecimal matchAmt = BigDecimal.ZERO;
                                try{
                                    matchAmt = new BigDecimal(matchContent);
                                }catch (Exception e) {
                                    matchContent = null;
                                }
                                if (StringUtils.isBlank(matchContent)) {
                                    isCheck = false;
                                } else {
                                    if ("5".equals(matchRelations)) {
                                        //大于
                                        isCheck = value.compareTo(matchAmt)>0 ? true:false;
                                    } else if ("6".equals(matchRelations)) {
                                        //大于等于
                                        isCheck = value.compareTo(matchAmt)>=0 ? true:false;
                                    } else if ("7".equals(matchRelations)) {
                                        //小于
                                        isCheck = value.compareTo(matchAmt)<0 ? true:false;
                                        //小于等于
                                    } else if ("8".equals(matchRelations)) {
                                        isCheck = value.compareTo(matchAmt)<=0 ? true:false;
                                    } else {
                                        isCheck = false;
                                    }
                                }
                            }
                        }
                    } else if (matchRule.equals("01")) {
                        if (isCheck) {

                        } else {
                            /**
                             *  将属性的首字符大写，方便构造get，set方法
                             */
                            String upName = nisBusTypeRuleDTO.getMatchElements().substring(0, 1).toUpperCase() + nisBusTypeRuleDTO.getMatchElements().substring(1);
                            if (upName.equals("CorpId")) {
                                upName = "CorpName";
                            }
                            if (upName.equals("CurId")) {
                                upName = "CurName";
                            }
                            Method m = dto.getClass().getMethod("get" + upName, new Class[0]);
                            if (!"Amt".equals(upName)) {
                                // 调用getter方法获取属性值
                                String value = (String) m.invoke(dto);
                                String matchRelations = nisBusTypeRuleDTO.getMatchRelations();
                                String matchContent = nisBusTypeRuleDTO.getMatchContent();
                                if(value == null || StringUtils.isEmpty(value.trim())){
                                    value = "";
                                }
                                if(StringUtils.isEmpty(matchContent.trim())){
                                    matchContent = "";
                                }
                                if ("1".equals(matchRelations)) {
                                    //包含
                                    String[] matchContents = matchContent.split(this.COMMA_SPLIT);
                                    for (String contents:matchContents) {
                                        isCheck = value.contains(contents);
                                        if (isCheck) {
                                            break;
                                        }
                                    }
                                } else if ("2".equals(matchRelations)) {
                                    //不包含
                                    String[] matchContents = matchContent.split(this.COMMA_SPLIT);
                                    for (String contents:matchContents) {
                                        isCheck = !value.contains(contents);
                                        if (!isCheck) {
                                            break;
                                        }
                                    }
                                } else if ("3".equals(matchRelations)) {
                                    //等于
                                    isCheck = value.equals(matchContent);
                                } else if ("4".equals(matchRelations)) {
                                    //不等于
                                    isCheck = !value.equals(matchContent);
                                } else if ("9".equals(matchRelations)) {
                                    //在......之内
                                    String[] matchContents = matchContent.split(this.COMMA_SPLIT);
                                    for (String contents:matchContents) {
                                        if (value.equals(contents)) {
                                            isCheck = true;
                                            break;
                                        }
                                        isCheck = false;
                                    }
                                } else if ("10".equals(matchRelations)) {
                                    //不在......之内
                                    String[] matchContents = matchContent.split(this.COMMA_SPLIT);
                                    for (String contents:matchContents) {
                                        if (value.equals(contents)) {
                                            isCheck = false;
                                            break;
                                        }
                                        isCheck = true;
                                    }
                                }else if ("11".equals(matchRelations)) {
                                    //为空
                                    isCheck = value == matchContent;
                                }else if ("12".equals(matchRelations)) {
                                    //不为空
                                    isCheck = value != matchContent;;
                                }else {
                                    isCheck = false;
                                }
                            }else{
                                BigDecimal value = (BigDecimal) m.invoke(dto);
                                String matchRelations = nisBusTypeRuleDTO.getMatchRelations();
                                String matchContent = nisBusTypeRuleDTO.getMatchContent();
                                if ("1".equals(matchRelations) || "2".equals(matchRelations)
                                        || "3".equals(matchRelations) || "4".equals(matchRelations)) {
                                    //判断金额为整数
                                    if (new BigDecimal(value.intValue()).compareTo(value) == 0) {
                                        int b = value.intValue();
                                        //判断是否为奇数
                                        if ("奇数".equals(matchContent)) {
                                            if (b % 2 != 0) {
                                                isCheck = true;
                                            }
                                            //判断是否为偶数
                                        } else if ("偶数".equals(matchContent)) {
                                            if (b % 2 == 0) {
                                                isCheck = true;
                                            }
                                        }
                                    }
                                } else {
                                    BigDecimal matchAmt = BigDecimal.ZERO;
                                    try{
                                        matchAmt = new BigDecimal(matchContent);
                                    }catch (Exception e) {
                                        matchContent = null;
                                    }
                                    if (StringUtils.isBlank(matchContent)) {
                                        isCheck = false;
                                    } else {
                                        if ("5".equals(matchRelations)) {
                                            //大于
                                            isCheck = value.compareTo(matchAmt)>0 ? true:false;
                                        } else if ("6".equals(matchRelations)) {
                                            //大于等于
                                            isCheck = value.compareTo(matchAmt)>=0 ? true:false;
                                        } else if ("7".equals(matchRelations)) {
                                            //小于
                                            isCheck = value.compareTo(matchAmt)<0 ? true:false;
                                            //小于等于
                                        } else if ("8".equals(matchRelations)) {
                                            isCheck = value.compareTo(matchAmt)<=0 ? true:false;
                                        } else {
                                            isCheck = false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            bList.add(isCheck);
            matchRuleList.add(matchRuleTemp);
        }
        for(int i =0;i<bList.size();i++){
            Boolean b = bList.get(i);
            String matchRule = matchRuleList.get(i);
            if(i == 0){
                allCheck = b;
            }else{
                if (StringUtils.isEmpty(matchRule.trim()) || matchRule.equals("02")) {
                    if(allCheck && b){
                        allCheck = true;
                    }else{
                        allCheck = false;
                    }
                }else{
                    if(b || allCheck){
                        allCheck = true;
                    }else{
                        allCheck = false;
                    }
                }
            }
        }
        return allCheck;
    }

    /**
     * 根据业务类别获取匹配规则
     * @param busId
     * @return
     */

    public List<List<FcNisBusTypeRuleDTO>> getNisBusTypeRuleDTOByBusId(String busId) {
        List<List<FcNisBusTypeRuleDTO>> list = new ArrayList<>();
        List<FcNisBusTypeRuleVO> nisBusTypeRuleVOParenthesisList = btNetOtherReceiveDtlDao.getByByBusId(busId);
        List<FcNisBusTypeRuleVO> tempList = null;
        //是否开括号了
        Boolean isParenthesis = false;
        //没有括号的条件放一起
        List<FcNisBusTypeRuleVO> tempListNoParenthesis = new ArrayList<>();
        for(FcNisBusTypeRuleVO vo :nisBusTypeRuleVOParenthesisList){
            if("(".equals(vo.getLeftParenthesis())){
                isParenthesis = true;
                tempList = new ArrayList<>();
                tempList.add(vo);
            }else if(")".equals(vo.getRightParenthesis())){
                tempList.add(vo);
                List<FcNisBusTypeRuleDTO> tempDTOList = new ArrayList<>();
                if(!EmptyUtils.isEmpty(tempList)){
                    tempDTOList = BeanUtils.copyListProperties(tempList,FcNisBusTypeRuleDTO.class);
                }
                isParenthesis = false;
                if(tempDTOList.size()>0) {
                    list.add(tempDTOList);
                }
            }else if(isParenthesis){
                tempList.add(vo);
            }else{
                tempListNoParenthesis.add(vo);
            }
        }
        List<FcNisBusTypeRuleDTO> tempDTONoParenthesisList = new ArrayList<>();
        if(!EmptyUtils.isEmpty(tempListNoParenthesis)){
            tempDTONoParenthesisList = BeanUtils.copyListProperties(tempListNoParenthesis,FcNisBusTypeRuleDTO.class);
        }
        if(tempDTONoParenthesisList.size()>0) {
            list.add(tempDTONoParenthesisList);
        }
        return list;
    }
    
}