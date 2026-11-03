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
import com.bytter.biz.finacial.api.service.IFcBusinessRuleDefineService;
import com.bytter.biz.finacial.api.service.ITallyService;
import com.bytter.biz.finacial.paramconfig.service.FcBusinessRuleDefineService;
import com.bytter.biz.finacial.paramconfig.vo.FcOtherBusinessRuleDefineVo;
import com.bytter.biz.finacial.paramconfig.vo.FcVoucherEntryDefineVo;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.enums.IMessageEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.BtBankAccCurDTO;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.tally.dao.BtNetDtlDownDao;
import com.bytter.tally.vo.FcAccbookNotifyVO;
import com.bytter.tally.vo.FcBusRaceVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author lijinquan
 * @description:
 * @date 2024-11-11 10:10
 */
@Service("btNetDtlDownService")
@Transactional
@Slf4j
public class BtNetDtlDownService {

    @Autowired
    private BtNetDtlDownDao btNetDtlDownDao;
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
    @DubboReference(check = false)
    private IFcBusinessRuleDefineService iFcBusinessRuleDefineService;

    /**
     * 英文状态下逗号作为分隔符
     */
    public static final String COMMA_SPLIT = ",";

    public List<BtNetOtherDtlDTO> queryTallyData(String num) {
        StringBuilder configSql = new StringBuilder();
        String uses = configService.getConfig("fcl", "paymentDtlUses").getValue(); //付款流水用途包含
        String remark = configService.getConfig("fcl", "paymentDtlRemark").getValue(); //付款流水摘要包含
        String postscript = configService.getConfig("fcl", "paymentDtlPostscript").getValue(); //付款流水附言包含
        configSql.append(" (1=1");
        if(StringUtils.isNotBlank(uses)){
            uses = uses.replace("，",",");
            String[] split = uses.split(",");
            configSql.append(" and (t.uses is null or (1=1 ");
            for (String s : split) {
                configSql.append(" and t.uses not like '%").append(s).append("%'");
            }
            configSql.append(" )) ");
        }
        if(StringUtils.isNotBlank(remark)){
            remark = remark.replace("，",",");
            String[] split = remark.split(",");
            configSql.append(" and (t.remark is null or (1=1 ");
            for (String s : split) {
                configSql.append(" and t.remark not like '%").append(s).append("%'");
            }
            configSql.append(" )) ");
        }
        if(StringUtils.isNotBlank(postscript)){
            postscript = postscript.replace("，",",");
            String[] split = postscript.split(",");
            configSql.append(" and (t.postscript is null or (1=1 ");
            for (String s : split) {
                configSql.append(" and t.postscript not like '%").append(s).append("%'");
            }
            configSql.append(" )) ");
        }
        configSql.append(" )");
        return btNetDtlDownDao.queryTallyData(num,configSql.toString());
    }

    /**
     * 根据系统凭证种类查询业务规则
     */
    public FcBusinessRuleDefineDTO getBusinessRuleExp(String sysRaceCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sysRaceCode", sysRaceCode);
        FcBusinessRuleDefineDTO entity = iFcBusinessRuleDefineService.getBusinessRuleExp(map);
        return entity;
    }

    /**
     * 判断是否自动复核
     */
    public boolean isAutoCheck() {
        boolean isAutoCheck = CmsConstant.IS_TRUE.equals(configService.getConfig("fcl", "tally_check").getValue());
        return isAutoCheck;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String processAutoTally(BtNetChalkDTO btNetChalk,FcBusinessRuleDefineDTO ruleDefine) {
        StringBuilder message = new StringBuilder();
        FcAccbookVoucherDTO fcAccbook = processAutoAccbook(btNetChalk, ruleDefine, btNetChalk.getCreateBy());
        if (fcAccbook != null) {
            String autoUser = getAutoAccounter(btNetChalk.getCorpId(), true);//记账人
            String checkUser = getAutoCheckUser(btNetChalk.getCorpId(), true);//复核人
            if (!"false".equals(autoUser) && !"false".equals(checkUser)) {
                submit(fcAccbook, btNetChalk.getId(), autoUser,checkUser);
            }else {
                message.append("该单位的自动记账人或自动复核人为空！");
            }
        }else {
            message.append("fcAccbook凭证构建失败！");
        }
        return message.toString();
    }


    public FcAccbookVoucherDTO processAutoAccbook(BtNetChalkDTO btNetChalkVo, FcBusinessRuleDefineDTO ruleDefine, String userId) {
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
    public List<FcAccbookDtlDTO> getAutoAccbookDtls(FcBusinessRuleDefineDTO ruleDefine, BtNetChalkDTO btNetChalk) {
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

                List<BtBankAccCurDTO> accList = btNetDtlDownDao.getCountertAccBy(btNetChalk.getOppAccNo(),entry.getSubjectCode());
                if (accList != null && accList.size() > 0 && accList.get(0).getCounterAcc() != null) {
                    String counterAcc = accList.get(0).getCounterAcc();
                    FcAccDTO fcAcc = fcAccService.getAccDTOByAccount(counterAcc);
                    accbookDtl.setAccount(fcAcc.getBlnctrAcc());
                    accbookDtl.setAccName(fcAcc.getAccName());
                } else {
                    accbookDtls = null;
                    break;
                }
            } else if (entry.getBalanceWay().intValue() == -1 && entry.getEntryType().intValue() == 10) {
                //贷方
                accbookDtl = new FcAccbookDtlDTO();
                accbookDtl.setDebitMoney(BigDecimal.ZERO);
                accbookDtl.setCreditMoney(btNetChalk.getMoney());
                accbookDtl.setLineNo(2);

                List<BtBankAccCurDTO> accList = btNetDtlDownDao.getCountertAccBy(btNetChalk.getBankAcc(),entry.getSubjectCode());

                if (accList != null && accList.size() > 0 && accList.get(0).getCounterAcc() != null) {
                    String counterAcc = accList.get(0).getCounterAcc();
                    FcAccDTO fcc = fcAccService.getAccDTOByAccount(counterAcc);
                    accbookDtl.setAccount(fcc.getBlnctrAcc());
                    accbookDtl.setAccName(fcc.getAccName());
                } else {
                    accbookDtls = null;
                    break;
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
        com.bytter.system.api.dto.CorpDTO corpDTO = corpService.get(netId);
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
        com.bytter.system.api.dto.CorpDTO corpDTO = corpService.get(netId);
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
        return FinaBookConstant.BusOriType.BIS_ACC_DTL_DOWN.get();
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

        setAccBookVoucher(fcAccbook);
        VoucherWrapperDTO voucher = setVoucherWrapperDTO(fcAccbook, autoUser);
        boolean isAutoCheck = true; //自动记账复核通过
        if (isAutoCheck) {
            //获取自动复核员用户
            voucher.setCheckUser(checkUser);
            voucher.setAutoCheck(true);
        }

        FcAccbookDTO accbook = iFcAccbookService.submitFcAccbook(voucher);
        //更新记账信息
        int count = 0;
        if (isAutoCheck) {
            //复核
            count = btNetDtlDownDao.updateTallySign(id,accbook.getId(),accbook.getBusGlobalId(),
                    NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.getValue(),accbook.getVoucherNo(),accbook.getOrderNo());
        } else {
            //记账
            count = btNetDtlDownDao.updateTallySign(id,accbook.getId(),accbook.getBusGlobalId(),
                    NbconsoleConstant.NbcEstate.E_STATE_WAIT_CHECK.getValue(),accbook.getVoucherNo(),accbook.getOrderNo());
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

        List<FcOtherBusinessRuleDefineVo> voList = btNetDtlDownDao.getBusinessRuleExp(netIds);
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
     * 记录失败原因
     * @param id
     * @param estate
     * @param message
     */
    public void updateTallyFailMsg(String id, String estate, String message) {
        btNetDtlDownDao.updateTallyFailMsg(id,estate,message);
    }

    public String getLang(IMessageEnum exceptionEnum, String... params) {
        String langKey = buildLangKey(exceptionEnum);
        String langContext = btNetDtlDownDao.getByCodeAndKey("zh_CN",langKey);
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
}
