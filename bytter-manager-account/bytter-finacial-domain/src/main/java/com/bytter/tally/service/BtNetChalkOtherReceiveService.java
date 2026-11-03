package com.bytter.tally.service;

import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.NbconsoleConstant;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.dto.*;
import com.bytter.biz.finacial.api.enums.accbook.AccbookMessageEnum;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.biz.finacial.api.enums.bankCollect.CollectMessageEnum;
import com.bytter.biz.finacial.api.enums.paramconfig.ParamConfigMessageEnum;
import com.bytter.biz.finacial.api.service.IFcAccbookService;
import com.bytter.biz.finacial.api.service.IFcBusRaceService;
import com.bytter.biz.finacial.api.service.IFcBusinessRuleDefineService;
import com.bytter.biz.finacial.api.service.ITallyService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.tally.dao.BisAccDtlTallyDao;
import com.bytter.tally.dao.BtNetChalkOtherReceiveDao;
import com.bytter.tally.dao.BtNetOtherReceiveDtlDao;
import com.bytter.tally.dto.FcAccbookPageDTO;
import com.bytter.tally.entity.TallyBtNetChalk;
import com.bytter.tally.vo.*;
import com.google.common.collect.Lists;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName BtNetChalkOtherReceiveService
 * @Author zx
 * @Description 其他收款手工记账服务类
 * @Date 2024/11/8 9:17
 */
@Service("btNetChalkOtherReceiveService")
@Transactional
@Slf4j
public class BtNetChalkOtherReceiveService {

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
    @DubboReference(check = false)
    private IFcBusinessRuleDefineService fcBusinessRuleDefineService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IUserService userService;
    @DubboReference(check = false)
    private IBisAccDtlService bisAccDtlService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
    private ITallyService tallyService;
    @Autowired
    private BtNetChalkOtherReceiveDao btNetChalkOtherReceiveDao;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;

    @Autowired
    private BtNetOtherReceiveDtlService btNetOtherReceiveDtlService;
    @Autowired
    private BtNetOtherReceiveDtlDao btNetOtherReceiveDtlDao;


    /**
     * 查询BtNetChalkDTO
     */
    @Transactional(readOnly = true)
    public BtNetChalkDTO queryBtNetChalkDTO(String id) {
        return tallyService.queryBtNetChalkDTO(id);
    }

    //查询数据
    @Transactional(readOnly = true)
    public DataTablesOutput queryBtNetChalk(DataTablesInput dataTablesInput, String type, String resource, String userId) {
        List<String> corpAuth = corpService.getUserCorpIds(resource, userId);
        DataTablesOutput<BtNetChalkDTO> data = btNetChalkOtherReceiveDao.queryBtNetChalk(dataTablesInput, type, userId, corpAuth);
        if (!com.alibaba.excel.util.CollectionUtils.isEmpty(data.getData())) {
            List<String> queryCorpIds = data.getData().stream().map(BtNetChalkDTO::getCorpId).collect(Collectors.toList());
            List<String> curIds = data.getData().stream().map(BtNetChalkDTO::getCurId).collect(Collectors.toList());
            Map<String, CorpDTO> corpMap = corpService.listCorpDtoByCorpIds(queryCorpIds);
            Map<String, BtCurrencyDTO> curMap = currencyService.getCurrencyByIds(curIds);
            data.getData().stream().forEach(item -> {
                if (corpMap.containsKey(item.getCorpId())) {
                    item.setCorpName(corpMap.get(item.getCorpId()).getName());
                }
                if (curMap.containsKey(item.getCurId())) {
                    item.setCurName(curMap.get(item.getCurId()).getCurName());
                }
            });
        }
        return data;
    }

    /**
     * 构建凭证
     */
    public FcAccbookPageDTO createAccbook(String id, String opType, String userId) {
        return processAccbook(id, opType, userId);
    }

    /**
     * 构建凭证
     */
    public FcAccbookPageDTO createAccbookTotal(String ids, String opType, String userId) {
        String[] idArr = ids.split(",");
        //用第一条数据来构建凭证
        FcAccbookPageDTO fcAccbookPageDTO = processAccbookTotal(idArr[0], opType, userId);

        List<BtNetChalkDTO> btNetChalkDTOS = tallyService.listBtNetChalkVo(Arrays.asList(idArr));
        String bankAcc = btNetChalkDTOS.get(0).getBankAcc();
        String transTime = DateUtils.date2Str(btNetChalkDTOS.get(0).getTransTime(),DateUtils.date_sdf());

        BigDecimal totalMoney = BigDecimal.ZERO;
        for (BtNetChalkDTO btNetChalkDTO : btNetChalkDTOS) {
            if(!bankAcc.equals(btNetChalkDTO.getBankAcc())){
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_BANKACC_NOTSAME);
            }
            if(!transTime.equals(DateUtils.date2Str(btNetChalkDTO.getTransTime(),DateUtils.date_sdf()))){
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_TRANSDATE_NOTSAME);
            }
            totalMoney = totalMoney.add(btNetChalkDTO.getMoney());
        }

        fcAccbookPageDTO.getBookDtls().get(0).setDebitMoney(totalMoney); //借方金额

        fcAccbookPageDTO.getBookDtls().get(1).setCreditMoney(totalMoney); //贷方金额
        fcAccbookPageDTO.getBookDtls().get(1).setAccount(null);
        fcAccbookPageDTO.getBookDtls().get(1).setAccName(null);

        return fcAccbookPageDTO;
    }

    /**
     * 取消
     */
    public void cancelBtNetChalk(String id, ICurrentUser user) {
        BtNetChalkDTO btNetChalk = tallyService.queryBtNetChalk(id);
        btNetChalk.setEstate(NbconsoleConstant.NbcEstate.E_STATE_UNAVAIL.get());
        tallyService.updateBtNetChalkEntity(btNetChalk);
        //待记账消息提醒完成
        notificationService.complete(NotificationConstant.FINACIAL.NETCHALK_TALLY, id, user.getId());
    }

    /**
     * 元素编辑控制
     */
    public List<VoucherRenderControlDTO> getControlBeanEdit(String sysCode, int size) {
        List<VoucherRenderControlDTO> controBeans = new ArrayList<VoucherRenderControlDTO>();
        VoucherRenderControlDTO controBean = null;
        for (int i = 0; i < size; i++) {
            controBean = new VoucherRenderControlDTO();
            controBean.setSummaryEditable(true);
            controBean.setVoucherTypeEditable(true);
            controBean.setCashFluxEditable(true);
            controBeans.add(controBean);
        }
        return controBeans;
    }

    /**
     * 根据系统凭证种类查询业务规则
     */
    public FcBusinessRuleDefineDTO getBusinessRuleExp(String sysRaceCode) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sysRaceCode", sysRaceCode);
        FcBusinessRuleDefineDTO entity = fcBusinessRuleDefineService.getBusinessRuleExp(map);
        return entity;
    }

    /**
     * 创建凭证信息
     */
    public FcAccbookPageDTO processAccbook(String id, String opType, String userId) {
        FcAccbookPageDTO entity = new FcAccbookPageDTO();
        BtNetChalkDTO btnetchalkvo = this.queryBtNetChalkDTO(id);
        if (EmptyUtils.isEmpty(btnetchalkvo)) {
            throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_BTNETCHALK_NULL);
        }
        VoucherRenderControlVo controlVo = new VoucherRenderControlVo();
        controlVo.setExternalAccEditable(false);
        controlVo.setExternalCorpEditable(false);
        controlVo.setBankEditable(false);
        controlVo.setCityEditable(false);
        controlVo.setProvinceEditable(false);
        controlVo.setSummaryEditable(false);
        List<VoucherRenderControlDTO> controlBeans = getControlBeanEdit(FinaBookConstant.SysRaceCode.NB_BANK_COLLECT.get(), 2);
        if (NbconsoleConstant.OpType.INIT.get().equals(opType)) {
            List<String> netIds = new ArrayList<>();
            netIds.add(btnetchalkvo.getCorpId());
            Map<String, FcOtherBusinessRuleDefineDTO> defineMap = btNetOtherReceiveDtlService.getBusinessRuleDefines(netIds);
            FcOtherBusinessRuleDefineDTO ruleDefine = defineMap.get(btnetchalkvo.getCorpId());
            if(null == ruleDefine){
                throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_BUSRACE_NOTDEFINE);
            }

            entity = processTallyAccbook(btnetchalkvo, controlVo, controlBeans, ruleDefine, userId);
        } else {
            if (EmptyUtils.isEmpty(btnetchalkvo.getBookId())) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOK_NULL);
            }
            List<FcAccbookDtlDTO> bookDtls = tallyService.getBookDtlVo(btnetchalkvo.getBookId());
            FcAccbookDTO accBook = iFcAccbookService.getAccBookVoById(btnetchalkvo.getBookId());
            accBook.setBusGlobalId(btnetchalkvo.getBusGlobalId());
            if (null == bookDtls || bookDtls.size() == 0) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOKDTL_NULL);
            }
            VoucherRenderControlVo bean = new VoucherRenderControlVo();
            if (NbconsoleConstant.OpType.EDIT.get().equals(opType)) {
                controlVo.setSummaryEditable(true);
                bean.setSummaryEditable(true);
                bean.setAccountEditable(true);
            }
            //对象转换
            VoucherRenderControlDTO controlDTO = new VoucherRenderControlDTO();
            BeanUtils.copyNotNullProperties(controlVo, controlDTO);
            bookDtls.get(0).setControlVo(controlDTO);
            VoucherRenderControlDTO renderControlDTO = new VoucherRenderControlDTO();
            BeanUtils.copyNotNullProperties(bean, renderControlDTO);
            bookDtls.get(1).setControlVo(renderControlDTO);
            FcAccbookExtVO bookExt = new FcAccbookExtVO();
            bookExt.setExternalCorp(btnetchalkvo.getOppAccName());//外单位户名
            bookExt.setExternalAcc(btnetchalkvo.getOppAccNo());//外单位户名
            bookExt.setExternalBank(btnetchalkvo.getOppBankName());//外单位开户行
            BtBankAccDTO bankAcc = bankAccService.getBankAccByBankAccAndCurId(btnetchalkvo.getOppAccNo(), btnetchalkvo.getCurId());
            if (!EmptyUtils.isEmpty(bankAcc)) {
                bookExt.setProv(bankAcc.getProv());//所在省市
                bookExt.setCity(bankAcc.getCity());
                com.bytter.biz.bdlink.api.dto.BisAccDtlDTO bisAcc = bisAccDtlService.getById(bankAcc.getId());
                if (!EmptyUtils.isEmpty(bisAcc)) {
                    bookExt.setRmk(bisAcc.getUses());
                } else {
                    bookExt.setRmk("");
                }
            }
            bookExt.setRmk(btnetchalkvo.getPurpose());
            entity.setBookDtls(bookDtls);
            entity.setBookExt(bookExt);
            entity.setAccbook(accBook);
        }
        return entity;
    }


    /**
     * 创建凭证信息
     */
    public FcAccbookPageDTO processAccbookTotal(String id, String opType, String userId) {
        FcAccbookPageDTO entity = new FcAccbookPageDTO();
        BtNetChalkDTO btnetchalkvo = this.queryBtNetChalkDTO(id);
        if (EmptyUtils.isEmpty(btnetchalkvo)) {
            throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_BTNETCHALK_NULL);
        }
        VoucherRenderControlVo controlVo = new VoucherRenderControlVo();
        controlVo.setExternalAccEditable(false);
        controlVo.setExternalCorpEditable(false);
        controlVo.setBankEditable(false);
        controlVo.setCityEditable(false);
        controlVo.setProvinceEditable(false);
        controlVo.setSummaryEditable(false);
        List<VoucherRenderControlDTO> controlBeans = getControlBeanEdit(FinaBookConstant.SysRaceCode.NB_BANK_COLLECT.get(), 2);
        if (NbconsoleConstant.OpType.INIT.get().equals(opType)) {
            List<String> netIds = new ArrayList<>();
            netIds.add(btnetchalkvo.getCorpId());
            Map<String, FcOtherBusinessRuleDefineDTO> defineMap = btNetOtherReceiveDtlService.getBusinessRuleDefines(netIds);
            FcOtherBusinessRuleDefineDTO ruleDefine = defineMap.get(btnetchalkvo.getCorpId());
            if(null == ruleDefine){
                throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_BUSRACE_NOTDEFINE);
            }
            entity = processTallyAccbookTotal(btnetchalkvo, controlVo, controlBeans, ruleDefine, userId);
        } else {
            if (EmptyUtils.isEmpty(btnetchalkvo.getBookId())) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOK_NULL);
            }
            List<FcAccbookDtlDTO> bookDtls = tallyService.getBookDtlVo(btnetchalkvo.getBookId());
            FcAccbookDTO accBook = iFcAccbookService.getAccBookVoById(btnetchalkvo.getBookId());
            accBook.setBusGlobalId(btnetchalkvo.getBusGlobalId());
            if (null == bookDtls || bookDtls.size() == 0) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOKDTL_NULL);
            }
            VoucherRenderControlVo bean = new VoucherRenderControlVo();
            if (NbconsoleConstant.OpType.EDIT.get().equals(opType)) {
                controlVo.setSummaryEditable(true);
                bean.setSummaryEditable(true);
                bean.setAccountEditable(true);
            }
            //对象转换
            VoucherRenderControlDTO controlDTO = new VoucherRenderControlDTO();
            BeanUtils.copyNotNullProperties(controlVo, controlDTO);
            bookDtls.get(0).setControlVo(controlDTO);
            VoucherRenderControlDTO renderControlDTO = new VoucherRenderControlDTO();
            BeanUtils.copyNotNullProperties(bean, renderControlDTO);
            bookDtls.get(1).setControlVo(renderControlDTO);

            entity.setBookDtls(bookDtls);
            entity.setAccbook(accBook);
        }
        return entity;
    }


    /**
     * 设置凭证信息
     */
    public FcAccbookPageDTO processTallyAccbookTotal(BtNetChalkDTO btNetChalkVo, VoucherRenderControlVo controlBean, List<VoucherRenderControlDTO> controlBeans, FcOtherBusinessRuleDefineDTO ruleDefine, String userId) {
        FcAccbookPageDTO entity = new FcAccbookPageDTO();
        FcAccbookDTO accBook = new FcAccbookDTO();
        FcAccbookExtVO bookExt = new FcAccbookExtVO();
        entity.setAccbook(accBook);
        List<FcAccbookDtlDTO> accbookDtls = null;
        accbookDtls = getAccbookDtls(entity, ruleDefine, controlBeans, btNetChalkVo, bookExt);

        accBook.setRmk(btNetChalkVo.getPurpose());//备注

        FcBusRaceVo fcbusRace = getFcBusRaceId(ruleDefine.getSysRaceCode());
        accBook.setEmergencySign(FinacialConstant.EMERGENCYSIGN_G);//加急标志
        accBook.setBusRaceId(fcbusRace.getId());
        accBook.setRaceName(fcbusRace.getRaceName());
        accBook.setVoucherType(ruleDefine.getVoucherType());
        entity.setAccbook(accBook);

        entity.setBookDtls(accbookDtls);
        return entity;
    }

    /**
     * 设置凭证信息
     */
    public FcAccbookPageDTO processTallyAccbook(BtNetChalkDTO btNetChalkVo, VoucherRenderControlVo controlBean, List<VoucherRenderControlDTO> controlBeans, FcOtherBusinessRuleDefineDTO ruleDefine, String userId) {
        FcAccbookPageDTO entity = new FcAccbookPageDTO();
        FcAccbookDTO accBook = new FcAccbookDTO();
        FcAccbookExtVO bookExt = new FcAccbookExtVO();
        entity.setAccbook(accBook);
        List<FcAccbookDtlDTO> accbookDtls = null;
        accbookDtls = getAccbookDtls(entity, ruleDefine, controlBeans, btNetChalkVo, bookExt);
        bookExt.setExternalCorp(btNetChalkVo.getOppAccName());//外单位
        bookExt.setExternalAcc(btNetChalkVo.getOppAccNo());//外单位账号
        bookExt.setExternalBank(btNetChalkVo.getOppBankName());//外单位开户行
        BtBankAccDTO bankAcc = bankAccService.getBankAccByBankAccAndCurId(btNetChalkVo.getOppAccNo(), btNetChalkVo.getCurId());
        if (!EmptyUtils.isEmpty(bankAcc)) {
            bookExt.setProv(bankAcc.getProv());//所在省市
            bookExt.setCity(bankAcc.getCity());
            BisAccDtlDTO bisAcc = bisAccDtlService.getById(bankAcc.getId());
            if (!EmptyUtils.isEmpty(bisAcc)) {
                bookExt.setRmk(bisAcc.getUses());
            } else {
                bookExt.setRmk("");
            }
        }
        accBook.setRmk(btNetChalkVo.getPurpose());//备注
        FcBusRaceVo fcbusRace = getFcBusRaceId(ruleDefine.getSysRaceCode());
        accBook.setEmergencySign(FinacialConstant.EMERGENCYSIGN_G);//加急标志
        accBook.setBusRaceId(fcbusRace.getId());
        accBook.setRaceName(fcbusRace.getRaceName());
        accBook.setVoucherType(ruleDefine.getVoucherType());
        entity.setAccbook(accBook);
        entity.setBookExt(bookExt);
        entity.setBookDtls(accbookDtls);
        return entity;
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
     * 构造银企接口的会计分录
     */
    public List<FcAccbookDtlDTO> getAccbookDtls(FcAccbookPageDTO entity, FcOtherBusinessRuleDefineDTO ruleDefine, List<VoucherRenderControlDTO> controlBean, BtNetChalkDTO btNetChalk, FcAccbookExtVO bookExt) {
        List<FcVoucherEntryDefineDTO> entrys = ruleDefine.getVouchers();
        FcAccbookDtlDTO accbookDtl = null;
        List<FcAccbookDtlDTO> accbookDtls = new ArrayList<FcAccbookDtlDTO>();
        List<BtBankAccCurDTO> countertAcc = bankAccService.getCountertAcc(btNetChalk.getBankAcc());
        for (FcVoucherEntryDefineDTO entry : entrys) {
            //借方，银行活期
            if (entry.getBalanceWay().intValue() == 1 && entry.getEntryType().intValue() == 10) {
                accbookDtl = new FcAccbookDtlDTO();
                accbookDtl.setCreditMoney(BigDecimal.ZERO);
                accbookDtl.setDebitMoney(btNetChalk.getMoney());
                VoucherRenderControlDTO bean = controlBean.get(1);
                bean.setAccountEditable(false);
                bean.setSummaryEditable(true);
                bean.setVoucherTypeEditable(false);
                accbookDtl.setSummaryContent(btNetChalk.getPurpose());
                accbookDtl.setOrigVoucherNo(btNetChalk.getBankSerial());
                accbookDtl.setOrigVoucherId(btNetChalk.getId());
                accbookDtl.setVoucherType(entry.getVoucherType());
                //借方为本方内部账号(银行活期)
                FcAccDTO fcAcc = new FcAccDTO();

                //本方内部账号为空,则默认取银行账号对应的内部账号，也可选择其他内部账号
                bean.setAccountEditable(true);
                String counterAcc = null;
                if (!EmptyUtils.isEmptyList(countertAcc)) {
                    counterAcc = countertAcc.get(0).getCounterAcc();
                }
                if (null != counterAcc) {
                    fcAcc.setBlnctrAcc(counterAcc);
                }
                //}
                if (EmptyUtils.isNotEmpty(fcAcc.getBlnctrAcc())) {
                    FcAccDTO fc = fcAccService.getAccDTOByAccount(fcAcc.getBlnctrAcc());
                    fcAcc.setAccName(fc.getAccName());
                    accbookDtl.setAccount(fcAcc.getBlnctrAcc());
                    accbookDtl.setAccName(fcAcc.getAccName());
                }
                accbookDtl.setControlVo(bean);
                accbookDtls.add(accbookDtl);
                //贷方，企业活期
            } else if (entry.getBalanceWay().intValue() == -1 && entry.getEntryType().intValue() == 10) {
                accbookDtl = new FcAccbookDtlDTO();

                List<BtBankAccCurDTO> accList = btNetOtherReceiveDtlDao.getCountertAccByCorpId(btNetChalk.getCorpId(),entry.getSubjectCode());

                if (accList != null && accList.size() > 0 && accList.get(0).getCounterAcc() != null) {
                    String counterAcc = accList.get(0).getCounterAcc();
                    FcAccDTO fcc = fcAccService.getAccDTOByAccount(counterAcc);
                    accbookDtl.setAccount(fcc.getBlnctrAcc());
                    accbookDtl.setAccName(fcc.getAccName());
                }
                VoucherRenderControlDTO vc = new VoucherRenderControlDTO();
                vc.setAccountEditable(true);
                vc.setSummaryEditable(true);
                accbookDtl.setControlVo(vc);
                accbookDtl.setDebitMoney(BigDecimal.ZERO);
                accbookDtl.setCreditMoney(btNetChalk.getMoney());
                accbookDtl.setSummaryContent(btNetChalk.getPurpose());
                accbookDtl.setOrigVoucherNo(btNetChalk.getBankSerial());
                accbookDtl.setOrigVoucherId(btNetChalk.getId());
                accbookDtl.setVoucherType(entry.getVoucherType());
                accbookDtls.add(accbookDtl);
            }
        }
        return accbookDtls;
    }

    /**
     * 判断付方账号,如果为空或不是系统中存在的账号，则需要手工选择贷方账号
     */
    public boolean isExistBankAcc(String bankAcc) {
        BtBankAccDTO bank = bankAccService.getBankAccInfo(bankAcc);
        return bank != null;
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
     * 功能介绍 : 记账提交凭证对象
     */
    public void submit(FcAccbookVoucherDTO fcAccbook, String id, String userId) {
        check(fcAccbook);
        //BtNetChalk entity = this.queryBtNetChalk(id);
        BtNetChalkDTO entity = queryBtNetChalkDTO(id);
        setAccBookVoucher(fcAccbook);
        VoucherWrapperDTO voucher = setVoucherWrapperDTO(fcAccbook, userId);
        boolean isAutoCheck = this.isAutoCheck();
        if (isAutoCheck) {
            //获取自动复核员用户
            setCheckUser(voucher);
            voucher.setAutoCheck(true);
        }
//		fcAccbookService.submitFcAccbook(voucher);
        VoucherWrapperDTO voucherWrapperDTO = new VoucherWrapperDTO();
        BeanUtils.copyProperties(voucher, voucherWrapperDTO);
        FcAccbookDTO accbook = iFcAccbookService.submitFcAccbook(voucherWrapperDTO); //？？？一直跟下面 又有这个方法的调用
        //待记账消息提醒完成
        notificationService.complete(NotificationConstant.FINACIAL.NETCHALK_TALLY, entity.getId(), userId);
        //如果自动复核，则复核凭证通过
        completeEditNotificationInfo(voucher, userId);
        completeCheckNotificationInfo(voucher, userId);
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
        if (NbconsoleConstant.NbcEstate.E_STATE_WAIT_CHECK.getValue().equals(eState) || NbconsoleConstant.NbcEstate.E_STATE_PASS_BACK.getValue().equals(eState) || NbconsoleConstant.NbcEstate.E_STATE_TEMP.getValue().equals(eState)) {//改账
            if (isAutoCheck) {
                //改账自动复核
                count = tallyService.updateEditAutoCheckSign(entity, idList);
            } else {
                //改账提交
                count = tallyService.updateEditPassSign(idList, newEState);
            }
        } else {
            if (isAutoCheck) {
                //更新付款单复核信息
                count = tallyService.updateAutoCheckSign(entity, idList);
            } else {
                //记账提交
                count = tallyService.updateTallySign(entity, idList, newEState);
            }
        }
        if (count < 1) {
            throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_STATUSCHANGE);//状态已改变，刷新页面重试
        }
        if (!isAutoCheck) {
            sendCheckNotificationInfo(voucher, userId);
        }
    }

    /**
     * 判断是否自动复核
     */
    public boolean isAutoCheck() {
        boolean isAutoCheck = CmsConstant.IS_TRUE.equals(configService.getConfig("fcl", "tally_check").getValue());
        return isAutoCheck;
    }

    /**
     * 设置自动复核人
     */
    public void setCheckUser(VoucherWrapperDTO voucher) {
        String netCode = voucher.getNetId();//记账网点
        if (EmptyUtils.isEmpty(netCode)) {
            setVoucherNet(voucher);
            netCode = voucher.getNetId();//记账网点
        }
        if (!EmptyUtils.isEmpty(netCode)) {
            voucher.setCheckUser(getAutoCheckUser(netCode, false));
            return;
        }
        FcAccbookVoucherDTO voucherVO = voucher.getBookVoucher();
        if (!EmptyUtils.isEmpty(voucherVO)) {
            netCode = voucherVO.getNetCode();//记账网点
            if (!EmptyUtils.isEmpty(netCode)) {
                voucher.setCheckUser(getAutoCheckUser(netCode, false));
                return;
            }
            FcAccbookDTO accbook = voucherVO.getAccbook();
            netCode = accbook.getNetId();//记账网点
            if (!EmptyUtils.isEmpty(netCode)) {
                voucher.setCheckUser(getAutoCheckUser(netCode, false));
                return;
            }
            List<FcAccbookDtlDTO> accbookDtls = voucherVO.getBookDtls();
            if (!EmptyUtils.isEmptyList(accbookDtls)) {
                FcAccbookDtlDTO accbookDtl = accbookDtls.get(0);
                String account = accbookDtl.getAccount();
                FcAccDTO accDTO = fcAccService.getAccDTOByAccount(account);
                netCode = accDTO.getNetId();//账号网点
                if (!EmptyUtils.isEmpty(netCode)) {
                    voucher.setCheckUser(getAutoCheckUser(netCode, false));
                }
            }
        }
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
     * 暂存FC_ACCBOOK
     */
    public void save(FcAccbookVoucherDTO fcAccbook, String id, String userId) {
        check(fcAccbook);
        BtNetChalkDTO entity = this.queryBtNetChalkDTO(id);
        //暂存
        String eState = entity.getEstate();//原状态
        if (NbconsoleConstant.NbcEstate.E_STATE_WAIT_CHECK.getValue().equals(eState) || NbconsoleConstant.NbcEstate.E_STATE_PASS_BACK.getValue().equals(eState) || NbconsoleConstant.NbcEstate.E_STATE_TEMP.getValue().equals(eState)) {//改账
            throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_STATUSCHANGE);//状态已改变，刷新页面重试
        }
        setAccBookVoucher(fcAccbook);
        VoucherWrapperDTO voucher = setVoucherWrapperDTO(fcAccbook, userId);
//		fcAccbookService.saveFcAccbook(voucher);
        VoucherWrapperDTO voucherWrapperDTO = new VoucherWrapperDTO();
        BeanUtils.copyProperties(voucher, voucherWrapperDTO);
        FcAccbookDTO accbook = iFcAccbookService.saveFcAccbook(voucherWrapperDTO);
        entity.setOrderNo(accbook.getOrderNo());
        entity.setOperatorCode(UserContextHelper.helper().getCurrentUser().getId());
        entity.setTallyDate(accbook.getTallyDate());
        entity.setVoucherNo(null);
        entity.setBookId(accbook.getId());
        entity.setBusGlobalId(accbook.getBusGlobalId());
        entity.setEstate(NbconsoleConstant.NbcEstate.E_STATE_TEMP.getValue());
        //更新收款记账信息
        int count = 0;
        String newEState = NbconsoleConstant.NbcEstate.E_STATE_TEMP.getValue();
        List<String> idList = Lists.newArrayList();
        idList.add(entity.getId());
        if (NbconsoleConstant.NbcEstate.E_STATE_WAIT_CHECK.getValue().equals(eState) || NbconsoleConstant.NbcEstate.E_STATE_PASS_BACK.getValue().equals(eState) || NbconsoleConstant.NbcEstate.E_STATE_TEMP.getValue().equals(eState)) {//改账
            //改账暂存
            count = tallyService.updateEditPassSign(idList, newEState);
        } else {
            //记账暂存
            count = tallyService.updateTallySign(entity, idList, newEState);
        }
        if (count < 1) {
            throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_STATUSCHANGE);//状态已改变，刷新页面重试
        }
        //待记账消息提醒完成
        notificationService.complete(NotificationConstant.FINACIAL.NETCHALK_TALLY, entity.getId(), userId);
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

    private void createVoucher(FcAccbookVoucherDTO voucher) {
        voucher.getAccbook().setBusOriType(getOriType());
    }

    /**
     * 返回记账的业务种类
     */
    public Integer getOriType() {
        return FinaBookConstant.BusOriType.BIS_ACC_DTL_OTHER_RECEIVE.get();
    }

    /***
     * 打回
     */
    public void backFcAccbook(FcAccbookVoucherDTO fcAccbook, String id, ICurrentUser user) {
        createVoucher(fcAccbook);
        String opType = fcAccbook.getOpType().name();
        BtNetChalkDTO entity = this.queryBtNetChalkDTO(id);
        VoucherWrapperDTO Voucher = setVoucherWrapperDTO(fcAccbook, user.getCorpId());
        Voucher.getTarget().get(0).getAccbook().setId(entity.getBookId());
        completeEditNotificationInfo(Voucher, user.getId());
        completeCheckNotificationInfo(Voucher, user.getId());
        //更新收款记账信息
        int count = 0;
        String eState = entity.getEstate();//原状态
        if ("MODIFY".equals(opType)) {
            entity.setBookId(null);
            entity.setOrderNo(null);
            entity.setBusGlobalId(null);

            VoucherWrapperDTO voucherWrapperDTO = new VoucherWrapperDTO();
            BeanUtils.copyProperties(Voucher, voucherWrapperDTO);
            iFcAccbookService.deleteFcAccbook(voucherWrapperDTO);
            count = tallyService.updateEditBackSign(id, eState);//改账打回
            if (count < 1) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_STATUSCHANGE);//状态已改变，刷新页面重试
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("money", NumberUtil.formatNumeric(entity.getMoney(), "#,##0.00"));
            notificationService.sendToUserByResPermission(entity.getId(), ResourceConstant.FCL_BT_NET_CHALK_COMMON_QUERY, NotificationConstant.FINACIAL.NETCHALK_TALLY, map, entity.getCorpId());
        } else if ("CHECK".equals(opType)) {
            entity.setEstate(NbconsoleConstant.NbcEstate.E_STATE_PASS_BACK.getValue());
            count = tallyService.updateCheckBackSign(id);//复核打回
            if (count < 1) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_STATUSCHANGE);//状态已改变，刷新页面重试
            }
            VoucherWrapperDTO voucherWrapperDTO = new VoucherWrapperDTO();
            BeanUtils.copyProperties(Voucher, voucherWrapperDTO);
            iFcAccbookService.backFcAccbook(voucherWrapperDTO);
            sendBackNotificationInfo(Voucher);
        }

    }

    /**
     * 构造复核传输对象
     */
    public void createVoucher(FcAccbookVoucherDTO fcAccbook, String id, ICurrentUser user) {
        createVoucher(fcAccbook);
        VoucherWrapperDTO Voucher = setVoucherWrapperDTO(fcAccbook, user.getId());
        BtNetChalkDTO entity = this.queryBtNetChalkDTO(id);
        checkFcAccbook(Voucher, entity, user.getId());
    }

    /***
     * 复核
     */
    public void checkFcAccbook(VoucherWrapperDTO Voucher, BtNetChalkDTO entity, String userId) {
        VoucherWrapperDTO voucherWrapperDTO = new VoucherWrapperDTO();
        BeanUtils.copyProperties(Voucher, voucherWrapperDTO);
        voucherWrapperDTO = iFcAccbookService.checkFcAccbook(voucherWrapperDTO);
        FcAccbookDTO accbook = voucherWrapperDTO.getTarget().get(0).getAccbook();
        entity.setVoucherNo(accbook.getVoucherNo());
        entity.setCheckerCode(accbook.getCheckerCode());
        entity.setCheckTime(accbook.getCheckTime());
        entity.setEstate(NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.getValue());
        List<String> idList = Lists.newArrayList();
        idList.add(entity.getId());
        int count = tallyService.updateCheckSign(entity, idList);//改账打回
        if (count < 1) {
            throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_STATUSCHANGE);//状态已改变，刷新页面重试
        }
        completeCheckNotificationInfo(Voucher, userId);
        completeEditNotificationInfo(Voucher, userId);
    }

    /**
     * 判断是否跨网点收款
     */
    public String getBtNetChalkAcc(FcAccbookPageDTO entity, String payeeCorpId, BtBankAccDTO payerAccInfo, FcAccbookExtVO bookExt) {
        String payeeBlnctrAcc = "";
        String superCurrentAcc = "";
        String payerCorpId = payerAccInfo.getCorpId();//付款单位
        CorpDTO payerCorp = corpService.get(payerCorpId);
        Integer type = payerCorp.getType();//0-网点，1-单位
        if (!payerCorp.getId().equals(payeeCorpId) && (type == 0)) {//跨网点调拨
            String payerSuperNetId = payerCorp.getParentId() == null ? payerCorp.getId() : payerCorp.getParentId();//获取上级网点id
            String blnctrAcc = bankAccService.getBlnctrAcc(payerAccInfo.getBankAcc(), payerAccInfo.getCurList().get(0));
            payeeBlnctrAcc = blnctrAcc;//本级关联联行账号-内部账号
            if (payerSuperNetId.equals(payeeCorpId)) {//说明是跨网点调拨,付款方上级网点和收款方网点对比
                //上级内部账号
                superCurrentAcc = bankAccService.getCountertAcc(payerAccInfo.getBankAcc()).get(0).getSuperCounterAcc();
            } else {
                superCurrentAcc = bankAccService.getCountertAcc(payerAccInfo.getBankAcc()).get(0).getCounterAcc();
            }
            bookExt.setStartBank(payerAccInfo.getBankName());//委托收款的收款银行
            bookExt.setStartCorp(payerCorp.getName());//委托收款的收款单位
            bookExt.setStartCorpAcc(payeeBlnctrAcc);//第三方账号,子账号银行活期,委托收款的收款账号
        } else {
            superCurrentAcc = bankAccService.getCountertAcc(payerAccInfo.getBankAcc()).get(0).getCounterAcc();
        }
        return superCurrentAcc;
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
     * 发送消息提醒
     */
    private void sendBackNotificationInfo(VoucherWrapperDTO voucher) {
        for (FcAccbookVoucherDTO voucherVO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherVO.getAccbook();
            // 打回改账处，通知提醒记账人
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
            notificationService.sendToUser(accbook.getId(), accbook.getOperatorCode(), NotificationConstant.FINACIAL.NETCHALK_EDIT, notifyVO);
        }
    }

    /**
     * 构造银企接口的会计分录
     */
    public List<FcAccbookDtlDTO> getAutoAccbookDtls(FcBusinessRuleDefineDTO ruleDefine, BtNetChalkDTO btNetChalk, FcAccbookExtDTO bookExt) {
        List<FcVoucherEntryDefineDTO> entrys = ruleDefine.getVouchers();
        FcAccbookDtlDTO accbookDtl = null;
        List<FcAccbookDtlDTO> accbookDtls = new ArrayList<FcAccbookDtlDTO>();
        List<BtBankAccCurDTO> countertAcc = bankAccService.getCountertAcc(btNetChalk.getBankAcc());
        for (FcVoucherEntryDefineDTO entry : entrys) {
            //借方，银行活期
            if (entry.getBalanceWay().intValue() == 1 && entry.getEntryType().intValue() == 10) {
                accbookDtl = new FcAccbookDtlDTO();
                accbookDtl.setCreditMoney(BigDecimal.ZERO);
                accbookDtl.setDebitMoney(btNetChalk.getMoney());
                accbookDtl.setLineNo(1);
                //借方为本方内部账号(银行活期)
                FcAccDTO fcAcc = new FcAccDTO();
                //本方内部账号为空,则默认取银行账号对应的内部账号，也可选择其他内部账号
                String counterAcc = null;
                if (!EmptyUtils.isEmptyList(countertAcc)) {
                    counterAcc = countertAcc.get(0).getCounterAcc();
                }
                if (null != counterAcc) {
                    fcAcc.setBlnctrAcc(counterAcc);
                } else {
                    accbookDtls = null;
                    break;
                }
                if (EmptyUtils.isNotEmpty(fcAcc.getBlnctrAcc())) {
                    FcAccDTO fc = fcAccService.getAccDTOByAccount(fcAcc.getBlnctrAcc());
                    fcAcc.setAccName(fc.getAccName());
                    accbookDtl.setAccount(fcAcc.getBlnctrAcc());
                    accbookDtl.setAccName(fcAcc.getAccName());
                }
                //贷方，企业活期
            } else if (entry.getBalanceWay().intValue() == -1 && entry.getEntryType().intValue() == 10) {
                accbookDtl = new FcAccbookDtlDTO();
                boolean isExistBankAcc = false;
                isExistBankAcc = isExistBankAcc(btNetChalk.getOppAccNo() == null ? "" : btNetChalk.getOppAccNo());
                if (!EmptyUtils.isBlank(btNetChalk.getOppAccNo()) && isExistBankAcc) {
                    //是否跨网点归集收款
                    BtBankAccDTO bt = bankAccService.getBankAccInfo(btNetChalk.getOppAccNo());
                    FcAccbookExtVO extVO = new FcAccbookExtVO();
                    BeanUtils.copyProperties(bookExt, extVO);
                    String debitAcc = getBtNetChalkAcc(null, btNetChalk.getCorpId(), bt, extVO);
                    if (debitAcc != null) {
                        FcAccDTO fcc = fcAccService.getAccDTOByAccount(debitAcc);
                        accbookDtl.setAccount(fcc.getBlnctrAcc());
                        accbookDtl.setAccName(fcc.getAccName());
                    } else {
                        accbookDtls = null;
                        break;
                    }
                } else {
                    accbookDtls = null;
                    break;
                }
                accbookDtl.setDebitMoney(BigDecimal.ZERO);
                accbookDtl.setCreditMoney(btNetChalk.getMoney());
                accbookDtl.setLineNo(2);
            }
            accbookDtl.setSummaryContent(btNetChalk.getPurpose());
            accbookDtl.setOrigVoucherNo(btNetChalk.getBankSerial());
            accbookDtl.setOrigVoucherId(btNetChalk.getId());
            accbookDtl.setVoucherType(entry.getVoucherType());
            accbookDtls.add(accbookDtl);
        }
        return accbookDtls;
    }


    /**
     * 功能介绍 : 合并记账提交凭证对象
     */
    public void mergeSubmit(FcAccbookVoucherDTO fcAccbook, List<String> idList, String userId) {
        check(fcAccbook);
        // 操作类型
        String opType = fcAccbook.getOpType().name();
        setAccBookVoucher(fcAccbook);
        // 合并记账，对方账号信息设置为空
        fcAccbook.setBookExt(new FcAccbookExtDTO());
        VoucherWrapperDTO voucher = setVoucherWrapper(fcAccbook, userId);
        boolean isAutoCheck = isAutoCheck();
        if (isAutoCheck) {
            //获取自动复核员用户
//            setCheckUser(voucher);
            voucher.setAutoCheck(true);
        }
        FcAccbookDTO accbook = iFcAccbookService.submitFcAccbook(voucher);

        List<BtNetChalkDTO> entityList = this.listBtNetChalkVo(idList);
        //待记账消息提醒完成
        entityList.forEach(entity -> notificationService.complete(NotificationConstant.FINACIAL.NETCHALK_TALLY, entity.getId(), userId));
        //如果自动复核，则复核凭证通过
        completeEditNotificationInfo(voucher, userId);
        completeCheckNotificationInfo(voucher, userId);

        BtNetChalkDTO entity = new BtNetChalkDTO();
        entity.setOrderNo(accbook.getOrderNo());
        entity.setOperatorCode(accbook.getOperatorCode());
        entity.setTallyDate(accbook.getTallyDate());
        entity.setVoucherNo(accbook.getVoucherNo());
        entity.setCheckerCode(accbook.getCheckerCode());
        entity.setCheckTime(accbook.getCheckTime());
        entity.setBookId(accbook.getId());
        entity.setBusGlobalId(accbook.getBusGlobalId());
        entity.setEstate(NbconsoleConstant.NbcEstate.E_STATE_WAIT_CHECK.getValue());
        // 记账提交待复核
        String newEState = NbconsoleConstant.NbcEstate.E_STATE_WAIT_CHECK.getValue();
        int count = 0;
        if ("MODIFY".equals(opType)) {
            if (isAutoCheck) {
                //改账自动复核
                count = tallyService.updateEditAutoCheckSign(entity, idList);
            } else {
                //改账提交
                count = tallyService.updateEditPassSign(idList, newEState);
            }
        } else if ("SUBMIT".equals(opType)) {
            if (isAutoCheck) {
                //记账自动复核
                count = tallyService.updateAutoCheckSign(entity, idList);
            } else {
                //记账提交
                count = tallyService.updateTallySign(entity, idList, newEState);
            }
        }
        if (count < idList.size()) {
            throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_STATUSCHANGE);//状态已改变，刷新页面重试
        }

        if (!isAutoCheck) {
            sendCheckNotificationInfo(voucher, userId);
        }
    }

    private List<BtNetChalkDTO> listBtNetChalkVo(List<String> idList) {
        return tallyService.listBtNetChalkVo(idList);
    }

    //凭证封装对象
    private VoucherWrapperDTO setVoucherWrapper(FcAccbookVoucherDTO fcAccbook, String userId) {
        VoucherWrapperDTO voucher = new VoucherWrapperDTO();
        voucher.setBusGlobalId(fcAccbook.getAccbook().getBusGlobalId());
        voucher.addTarget(fcAccbook);
        voucher.setProcessType(ProcessType.PROCESS_COMMON);
        voucher.setUserCode(userId);//设置用户
        voucher.setAutoAccounter(false);
        return voucher;
    }

    public void mergeSave(FcAccbookVoucherDTO fcAccbook, List<String> idList, String userId) {
        check(fcAccbook);
        setAccBookVoucher(fcAccbook);
        // 合并记账，对方账号信息设置为空
        fcAccbook.setBookExt(new FcAccbookExtDTO());
        VoucherWrapperDTO voucher = setVoucherWrapper(fcAccbook, userId);
        FcAccbookDTO accbook = iFcAccbookService.saveFcAccbook(voucher);
        BtNetChalkDTO entity = new BtNetChalkDTO();
        entity.setOrderNo(accbook.getOrderNo());
        entity.setOperatorCode(accbook.getOperatorCode());
        entity.setTallyDate(accbook.getTallyDate());
        entity.setVoucherNo(null);
        entity.setBookId(accbook.getId());
        entity.setBusGlobalId(accbook.getBusGlobalId());
        entity.setEstate(NbconsoleConstant.NbcEstate.E_STATE_TEMP.getValue());

        // 记账暂存，更新收款记账信息
        String newEState = NbconsoleConstant.NbcEstate.E_STATE_TEMP.getValue();
        int count = tallyService.updateTallySign(entity, idList, newEState);
        if (count < idList.size()) {
            throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_STATUSCHANGE);//状态已改变，刷新页面重试
        }
        //待记账消息提醒完成
        idList.forEach(id -> notificationService.complete(NotificationConstant.FINACIAL.NETCHALK_TALLY, id, userId));
    }

    public void mergeCreateVoucher(FcAccbookVoucherDTO fcAccbook, List<String> idList, ICurrentUser user) {
        createVoucher(fcAccbook);
        // 合并记账，对方账号信息设置为空
        fcAccbook.setBookExt(new FcAccbookExtDTO());
        VoucherWrapperDTO voucher = setVoucherWrapper(fcAccbook, user.getId());

        VoucherWrapperDTO dto = iFcAccbookService.checkFcAccbook(voucher);

        FcAccbookDTO accbook = dto.getTarget().get(0).getAccbook();
        BtNetChalkDTO entity = new BtNetChalkDTO();
        entity.setVoucherNo(accbook.getVoucherNo());
        entity.setCheckerCode(accbook.getCheckerCode());
        entity.setCheckTime(accbook.getCheckTime());
        entity.setEstate(NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.getValue());

        int count = tallyService.updateCheckSign(entity, idList);
        if (count < idList.size()) {
            throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_STATUSCHANGE);//状态已改变，刷新页面重试
        }

        completeCheckNotificationInfo(voucher, user.getId());
        completeEditNotificationInfo(voucher, user.getId());
    }

    public List<String> listByBookId(String bookId) {
        return tallyService.listByBookId(bookId);
    }

    public BtNetChalkDTO queryBtNetChalkVo(String id) {
        return tallyService.queryBtNetChalkVo(id);
    }

    public FcAccbookPageDTO batchCreateAccbook(List<String> idList, String opType, String userId) {
        List<BtNetChalkDTO> chalkList = tallyService.listBtNetChalkVo(idList);
        // 合并记账前数据校验
        this.mergeTallyCheck(chalkList, idList);
        // 多个流水时，获取最晚的交易日期的流水实体
        BtNetChalkDTO btNetChalk = chalkList.stream().sorted(Comparator.comparing(BtNetChalkDTO::getTransTime).reversed()).collect(Collectors.toList()).stream().findFirst().orElse(chalkList.get(0));
        // 定义返回值对象
        FcAccbookPageDTO entity = new FcAccbookPageDTO();
        if (NbconsoleConstant.OpType.INIT.get().equals(opType)) {
            // 根据系统凭证种类查询业务记账规则
            List<String> netIds = new ArrayList<>();
            netIds.add(chalkList.get(0).getCorpId());
            Map<String, FcOtherBusinessRuleDefineDTO> defineMap = btNetOtherReceiveDtlService.getBusinessRuleDefines(netIds);
            FcOtherBusinessRuleDefineDTO ruleDefine = defineMap.get(chalkList.get(0).getCorpId());            // 获取业务种类对象
            if(null == ruleDefine){
                throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_BUSRACE_NOTDEFINE);
            }
            FcBusRaceVo fcbusRace = getFcBusRaceId(ruleDefine.getSysRaceCode());

            // 账务凭证主档表记录
            FcAccbookDTO accBook = new FcAccbookDTO();
            accBook.setBusRaceId(fcbusRace.getId());
            accBook.setRaceName(fcbusRace.getRaceName());
            accBook.setInterestStartDate(btNetChalk.getTransTime());
            accBook.setVoucherType(ruleDefine.getVoucherType());
            entity.setAccbook(accBook);
            // 合并记账时账务凭证扩展属性隐藏 FcAccbookExtVO bookExt

            // 账务凭证分录明细记录
            List<FcAccbookDtlDTO> accbookDtls = this.batchProcessAccbookDtls(chalkList, ruleDefine);
            entity.setBookDtls(accbookDtls);

            // 现金流项目标识
            boolean cashSubjectFlag = checkCashSubject(accbookDtls);
            String cashFlag = cashSubjectFlag ? "1" : "0";
            entity.setCashFlag(cashFlag);
        } else {
            if (EmptyUtils.isEmpty(btNetChalk.getBookId())) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOK_NULL);
            }
            List<FcAccbookDtlDTO> bookDtls = tallyService.getBookDtlVo(btNetChalk.getBookId());
            if (CollectionUtils.isEmpty(bookDtls)) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOKDTL_NULL);
            }
            // 账务凭证主档表记录
            FcAccbookDTO accbook = iFcAccbookService.getAccBookVoById(btNetChalk.getBookId());
            accbook.setBusGlobalId(btNetChalk.getBusGlobalId());
            entity.setAccbook(accbook);
            // 合并记账时账务凭证扩展属性隐藏 FcAccbookExtVO bookExt

            // 账务凭证分录明细记录
            VoucherRenderControlDTO controlVo = new VoucherRenderControlDTO();
            VoucherRenderControlDTO bean = new VoucherRenderControlDTO();
            if (NbconsoleConstant.OpType.EDIT.get().equals(opType)) {
                controlVo.setSummaryEditable(true);
                bean.setSummaryEditable(true);
                bean.setAccountEditable(true);
            }
            // 通过原始凭证号（银企接口收款流水表的主键id）进行分组，并循环每个分组通过行号正序排序
            Map<String, List<FcAccbookDtlDTO>> map = bookDtls.stream().collect(Collectors.groupingBy(FcAccbookDtlDTO::getOrigVoucherNo)).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, v -> v.getValue().stream().sorted(Comparator.comparing(FcAccbookDtlDTO::getLineNo)).collect(Collectors.toList())));
            map.forEach((k, v) -> {
                v.get(0).setControlVo(controlVo);
                v.get(1).setControlVo(bean);
            });
            entity.setBookDtls(bookDtls);
        }
        return entity;
    }

    private boolean checkCashSubject(List<FcAccbookDtlDTO> accbookDtls) {
        if (accbookDtls.size() < 1) {
            return false;
        }
        List<String> accList = accbookDtls.stream().filter(accBookDtl -> StringUtils.isNotBlank(accBookDtl.getAccount()))
                .map(FcAccbookDtlDTO::getAccount).distinct().collect(Collectors.toList());
        if (accList.size() < 1) {
            return false;
        }
        return tallyService.existCashSubject(accList);
    }

    private List<FcAccbookDtlDTO> batchProcessAccbookDtls(List<BtNetChalkDTO> chalkList, FcOtherBusinessRuleDefineDTO ruleDefine) {
        List<FcAccbookDtlDTO> accbookDtls = Lists.newArrayList();
        List<FcVoucherEntryDefineDTO> entrys = ruleDefine.getVouchers();
        chalkList.forEach(chalk -> entrys.forEach(rule -> {
            //借方，银行活期
            if (rule.getBalanceWay() == 1 && rule.getEntryType() == 10) {
                FcAccbookDtlDTO accbookDtl = new FcAccbookDtlDTO();
                accbookDtl.setCreditMoney(BigDecimal.ZERO);
                accbookDtl.setDebitMoney(chalk.getMoney());
                accbookDtl.setSummaryContent(chalk.getPurpose());
                accbookDtl.setOrigVoucherNo(chalk.getId());
                accbookDtl.setOrigVoucherId(chalk.getId());
                accbookDtl.setVoucherType(rule.getVoucherType());
                //借方为本方内部账号(银行活期)
                FcAccDTO fcAcc = new FcAccDTO();
                List<BtBankAccCurDTO> countertAcc = bankAccService.getCountertAcc(chalk.getBankAcc());
                String counterAcc = null;
                if (!EmptyUtils.isEmptyList(countertAcc)) {
                    counterAcc = bankAccService.getCountertAcc(chalk.getBankAcc()).get(0).getCounterAcc();
                }
                if (null != counterAcc) {
                    fcAcc.setBlnctrAcc(counterAcc);
                }
                if (EmptyUtils.isNotEmpty(fcAcc.getBlnctrAcc())) {
                    FcAccDTO fc = fcAccService.getAccDTOByAccount(fcAcc.getBlnctrAcc());
                    fcAcc.setAccName(fc.getAccName());
                    accbookDtl.setAccount(fcAcc.getBlnctrAcc());
                    accbookDtl.setAccName(fcAcc.getAccName());
                }
                VoucherRenderControlDTO bean = new VoucherRenderControlDTO();
                bean.setAccountEditable(true);
                bean.setSummaryEditable(true);
                bean.setCashFluxEditable(true);
                bean.setVoucherTypeEditable(false);
                accbookDtl.setControlVo(bean);
                accbookDtls.add(accbookDtl);
            } else if (rule.getBalanceWay() == -1 && rule.getEntryType() == 10) {
                //贷方，企业活期
                FcAccbookDtlDTO accbookDtl = new FcAccbookDtlDTO();
                List<BtBankAccCurDTO> accList = btNetOtherReceiveDtlDao.getCountertAccByCorpId(chalk.getCorpId(),rule.getSubjectCode());

                if (accList != null && accList.size() > 0 && accList.get(0).getCounterAcc() != null) {
                    String counterAcc = accList.get(0).getCounterAcc();
                    FcAccDTO fcc = fcAccService.getAccDTOByAccount(counterAcc);
                    accbookDtl.setAccount(fcc.getBlnctrAcc());
                    accbookDtl.setAccName(fcc.getAccName());
                }
                VoucherRenderControlDTO vc = new VoucherRenderControlDTO();
                vc.setAccountEditable(true);
                vc.setSummaryEditable(true);
                accbookDtl.setControlVo(vc);
                accbookDtl.setDebitMoney(BigDecimal.ZERO);
                accbookDtl.setCreditMoney(chalk.getMoney());
                accbookDtl.setSummaryContent(chalk.getPurpose());
                accbookDtl.setOrigVoucherNo(chalk.getId());
                accbookDtl.setOrigVoucherId(chalk.getId());
                accbookDtl.setVoucherType(rule.getVoucherType());
                accbookDtls.add(accbookDtl);
            }
        }));
        return accbookDtls;
    }

    private void mergeTallyCheck(List<BtNetChalkDTO> chalkList, List<String> idList) {
        if (CollectionUtils.isEmpty(idList) || CollectionUtils.isEmpty(chalkList) || chalkList.size() < 2) {
            throw new ParameterException(CollectMessageEnum.NETBANK_MESSAGE_CONFIRM_SELECTDATA);
        }
        if (chalkList.size() != idList.size()) {
            throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_BTNETCHALK_NULL);
        }
        chalkList.forEach(one -> chalkList.forEach(two -> {
            // 结算中心必须一致
            if (!one.getCorpId().equals(two.getCorpId())) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_NETID_NOTSAME);
            }
            // 必须是同月的交易流水
            if (DateUtils.getMonth(one.getTransTime()) != DateUtils.getMonth(two.getTransTime())) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_TRANSTIME_NOTSAME);
            }
            // 必须是同一币种
            if (!one.getCurId().equals(two.getCurId())) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_CURID_NOTSAME);
            }
        }));
    }

    public void mergeBackFcAccbook(FcAccbookVoucherDTO fcAccbook, List<String> idList, ICurrentUser user) {
        createVoucher(fcAccbook);
        String opType = fcAccbook.getOpType().name();
        VoucherWrapperDTO Voucher = setVoucherWrapper(fcAccbook, user.getId());
        completeEditNotificationInfo(Voucher, user.getId());
        completeCheckNotificationInfo(Voucher, user.getId());
        List<BtNetChalkDTO> entityList = this.listBtNetChalkVo(idList);

        if ("MODIFY".equals(opType)) {
            // 改账打回
            iFcAccbookService.deleteFcAccbook(Voucher);
            int count = tallyService.mergeUpdateEditBackSign(idList);//改账打回
            if (count < idList.size()) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_STATUSCHANGE);//状态已改变，刷新页面重试
            }

            entityList.forEach(entity -> {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(entity.getMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(entity.getId(), ResourceConstant.FCL_BT_NET_CHALK_COMMON_QUERY, NotificationConstant.FINACIAL.NETCHALK_TALLY, map, entity.getCorpId());
            });
        } else if ("CHECK".equals(opType)) {
            //复核打回
            int count = tallyService.mergeUpdateCheckBackSign(idList);
            if (count < idList.size()) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_STATUSCHANGE);//状态已改变，刷新页面重试
            }
            iFcAccbookService.backFcAccbook(Voucher);
            sendBackNotificationInfo(Voucher);
        }
    }

}
