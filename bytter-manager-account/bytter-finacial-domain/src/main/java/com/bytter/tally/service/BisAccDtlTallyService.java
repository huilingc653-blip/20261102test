package com.bytter.tally.service;

import com.alibaba.excel.util.CollectionUtils;
import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.NbconsoleConstant;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.dto.*;
import com.bytter.biz.finacial.api.enums.accbook.AccbookMessageEnum;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.biz.finacial.api.enums.bankCollect.CollectMessageEnum;
import com.bytter.biz.finacial.api.service.IFcAccbookService;
import com.bytter.biz.finacial.api.service.IFcBusRaceService;
import com.bytter.biz.finacial.api.service.IFcBusinessRuleDefineService;
import com.bytter.biz.finacial.api.service.ITallyService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.*;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.tally.dao.BisAccDtlTallyDao;
import com.bytter.tally.dto.FcAccbookPageDTO;
import com.bytter.tally.vo.*;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName BisAccDtlTallyService
 * @Author zx
 * @Description 下拨手工记账服务类
 * @Date 2024/10/29 15:20
 */
@Service("bisAccDtlTallyService")
@Transactional
@Slf4j
public class BisAccDtlTallyService {
    @DubboReference(check = false)
    private ICorpService corpService;

    @DubboReference(check = false)
    private IBtCurrencyService currencyService;

    @DubboReference(check = false)
    private IBtBankAccService bankAccService;

    @DubboReference(check = false)
    private IFcBusRaceService iFcBusRaceService;

    @DubboReference(check = false)
    private IFcBusinessRuleDefineService fcBusinessRuleDefineService;

    @DubboReference(check = false)
    private IFcAccbookService iFcAccbookService;

    @DubboReference(check = false)
    private ITallyService tallyService;

    @DubboReference(check = false)
    private INotificationService notificationService;

    @DubboReference(check = false)
    private IConfigService configService;

    @DubboReference(check = false)
    private IFcAccService fcAccService;

    @Autowired
    private BtNetChalkService btNetChalkService;

    @Autowired
    private BisAccDtlTallyDao bisAccDtlTallyDao;

    /**
     * 下拨获取总账户付款流水
     *
     * @param dataTablesInput
     * @param type
     * @param resource
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput queryBisAccDtlTally(DataTablesInput dataTablesInput, String type, String resource, String userId) {
        List<String> corpAuth = corpService.getUserCorpIds(resource, userId);
        DataTablesOutput<BisAccDtlTallyVO> data = bisAccDtlTallyDao.queryBisAccDtlTally(dataTablesInput, type, userId, corpAuth);
        if (!CollectionUtils.isEmpty(data.getData())) {
            List<String> queryCorpIds = data.getData().stream().map(BisAccDtlTallyVO::getCorpId).collect(Collectors.toList());
            List<String> curIds = data.getData().stream().map(BisAccDtlTallyVO::getCurId).collect(Collectors.toList());
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
     * 通过流水id查询数据，构建凭证
     *
     * @param id
     * @param opType
     * @param userId
     * @return
     */
    public FcAccbookPageDTO createAccbook(String id, String opType, String userId) {
        return processAccbook(id, opType, userId);
    }

    /**
     * 创建凭证信息
     *
     * @param id
     * @param opType
     * @param userId
     * @return
     */
    public FcAccbookPageDTO processAccbook(String id, String opType, String userId) {
        FcAccbookPageDTO entity = new FcAccbookPageDTO();
        //根据流水Id查询记账数据
        BisAccDtlTallyVO bisAccDtlTallyVO = bisAccDtlTallyDao.getBisAccDtlInfoById(id);
        if (EmptyUtils.isEmpty(bisAccDtlTallyVO)) {
            throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_BTNETCHALK_NULL);
        }
        VoucherRenderControlVo controlVo = new VoucherRenderControlVo();
        controlVo.setExternalAccEditable(false);
        controlVo.setExternalCorpEditable(false);
        controlVo.setBankEditable(false);
        controlVo.setCityEditable(false);
        controlVo.setProvinceEditable(false);
        controlVo.setSummaryEditable(false);
        List<VoucherRenderControlDTO> controlBeans = getControlBeanEdit(FinaBookConstant.SysRaceCode.NB_BIS_ACC_DTL_COLLECT.get(), 2);
        if (NbconsoleConstant.OpType.INIT.get().equals(opType)) {
            FcBusinessRuleDefineDTO ruleDefine = getBusinessRuleExp(FinaBookConstant.SysRaceCode.NB_BIS_ACC_DTL_COLLECT.get());
            entity = processTallyAccbook(bisAccDtlTallyVO, controlVo, controlBeans, ruleDefine, userId);
        } else {
            if (EmptyUtils.isEmpty(bisAccDtlTallyVO.getBookId())) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOK_NULL);
            }
            List<FcAccbookDtlDTO> bookDtls = tallyService.getBookDtlVo(bisAccDtlTallyVO.getBookId());
            FcAccbookDTO accBook = iFcAccbookService.getAccBookVoById(bisAccDtlTallyVO.getBookId());
            accBook.setBusGlobalId(bisAccDtlTallyVO.getBusGlobalId());
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
            bookExt.setExternalCorp(bisAccDtlTallyVO.getOppAccName());//外单位户名
            bookExt.setExternalAcc(bisAccDtlTallyVO.getOppAccNo());//外单位户名
            bookExt.setExternalBank(bisAccDtlTallyVO.getOppBankName());//外单位开户行
            BtBankAccDTO bankAcc = bankAccService.getBankAccByBankAccAndCurId(bisAccDtlTallyVO.getOppAccNo(), bisAccDtlTallyVO.getCurId());
            if (!EmptyUtils.isEmpty(bankAcc)) {
                bookExt.setProv(bankAcc.getProv());//所在省市
                bookExt.setCity(bankAcc.getCity());
                bookExt.setRmk(bisAccDtlTallyVO.getPurpose() == null ? "" : bisAccDtlTallyVO.getPurpose());
            }
            bookExt.setRmk(bisAccDtlTallyVO.getPurpose());
            entity.setBookDtls(bookDtls);
            entity.setBookExt(bookExt);
            entity.setAccbook(accBook);
        }
        return entity;
    }

    /**
     * 设置凭证信息
     */
    public FcAccbookPageDTO processTallyAccbook(BisAccDtlTallyVO bisAccDtlTallyVO, VoucherRenderControlVo controlBean, List<VoucherRenderControlDTO> controlBeans, FcBusinessRuleDefineDTO ruleDefine, String userId) {
        FcAccbookPageDTO entity = new FcAccbookPageDTO();
        FcAccbookDTO accBook = new FcAccbookDTO();
        FcAccbookExtVO bookExt = new FcAccbookExtVO();
        entity.setAccbook(accBook);
        List<FcAccbookDtlDTO> accbookDtls = null;
        accbookDtls = getAccbookDtls(entity, ruleDefine, controlBeans, bisAccDtlTallyVO, bookExt);
        bookExt.setExternalCorp(bisAccDtlTallyVO.getOppAccName());//外单位
        bookExt.setExternalAcc(bisAccDtlTallyVO.getOppAccNo());//外单位账号
        bookExt.setExternalBank(bisAccDtlTallyVO.getOppBankName());//外单位开户行
        BtBankAccDTO bankAcc = bankAccService.getBankAccByBankAccAndCurId(bisAccDtlTallyVO.getOppAccNo(), bisAccDtlTallyVO.getCurId());
        if (!EmptyUtils.isEmpty(bankAcc)) {
            bookExt.setProv(bankAcc.getProv());//所在省市
            bookExt.setCity(bankAcc.getCity());
            bookExt.setRmk(bisAccDtlTallyVO.getPurpose() == null ? "" : bisAccDtlTallyVO.getPurpose());
        }
        accBook.setRmk(bisAccDtlTallyVO.getPurpose());//备注
        //String netCode = btNetChalkVo.getCorpId();
        FcBusRaceVo fcbusRace = getFcBusRaceId(bisAccDtlTallyVO.getCorpId(), userId);
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
     * 构造银企接口的会计分录
     */
    public List<FcAccbookDtlDTO> getAccbookDtls(FcAccbookPageDTO entity, FcBusinessRuleDefineDTO ruleDefine, List<VoucherRenderControlDTO> controlBean, BisAccDtlTallyVO bisAccDtlTallyVO, FcAccbookExtVO bookExt) {
        List<FcVoucherEntryDefineDTO> entrys = ruleDefine.getVouchers();
        FcAccbookDtlDTO accbookDtl = null;
        List<FcAccbookDtlDTO> accbookDtls = new ArrayList<FcAccbookDtlDTO>();
        for (FcVoucherEntryDefineDTO entry : entrys) {
            //借方，银行活期
            if (entry.getBalanceWay().intValue() == 1 && entry.getEntryType().intValue() == 10) {
                accbookDtl = new FcAccbookDtlDTO();
                accbookDtl.setCreditMoney(BigDecimal.ZERO);
                accbookDtl.setDebitMoney(bisAccDtlTallyVO.getMoney());
                VoucherRenderControlDTO bean = controlBean.get(1);
                bean.setAccountEditable(true);
                bean.setSummaryEditable(true);
                bean.setVoucherTypeEditable(false);
                accbookDtl.setSummaryContent(bisAccDtlTallyVO.getPurpose());
                accbookDtl.setOrigVoucherNo(bisAccDtlTallyVO.getId());
                accbookDtl.setOrigVoucherId(bisAccDtlTallyVO.getId());
                accbookDtl.setVoucherType(entry.getVoucherType());
                accbookDtl.setAccount(bisAccDtlTallyVO.getOppCntAccount());
                accbookDtl.setAccName(bisAccDtlTallyVO.getOppCntAccName());

                accbookDtl.setControlVo(bean);
                accbookDtls.add(accbookDtl);
                //贷方，企业活期
            } else if (entry.getBalanceWay().intValue() == -1 && entry.getEntryType().intValue() == 10) {
                accbookDtl = new FcAccbookDtlDTO();
                accbookDtl.setAccount(bisAccDtlTallyVO.getCntAccount());
                accbookDtl.setAccName(bisAccDtlTallyVO.getCntAccName());
                accbookDtl.setDebitMoney(BigDecimal.ZERO);
                accbookDtl.setCreditMoney(bisAccDtlTallyVO.getMoney());
                accbookDtl.setSummaryContent(bisAccDtlTallyVO.getPurpose());
                accbookDtl.setOrigVoucherNo(bisAccDtlTallyVO.getId());
                accbookDtl.setOrigVoucherId(bisAccDtlTallyVO.getId());
                accbookDtl.setVoucherType(entry.getVoucherType());

                VoucherRenderControlDTO vc = new VoucherRenderControlDTO();
                vc.setAccountEditable(true);
                vc.setSummaryEditable(true);
                accbookDtl.setControlVo(vc);
                accbookDtls.add(accbookDtl);
            }
        }
        return accbookDtls;
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
     * 获取业务总类id
     */
    public FcBusRaceVo getFcBusRaceId(String netCode, String userId) {
        CorpDTO corp = corpService.getNetByCorpId(netCode);
        netCode = corp.getNetId();
        List<FcBusRaceDTO> fcBusRaceDTOS = iFcBusRaceService.findBySysRaceCode(FinaBookConstant.SysRaceCode.NB_BIS_ACC_DTL_COLLECT.get());
        List<FcBusRaceVo> raceList = BeanUtils.copyListProperties(fcBusRaceDTOS, FcBusRaceVo.class);
        FcBusRaceVo tempRace = null;
        for (FcBusRaceVo busRace : raceList) {
            String netId = busRace.getNetId();
            if (!StringUtils.isEmpty(netId) && netCode.equals(netId)) {
                tempRace = busRace;
                break;
            } else if (EmptyUtils.isEmpty(netId)) {
                tempRace = busRace;
            }
        }
        if (null == tempRace) {
            tempRace = new FcBusRaceVo();
            tempRace.setNetId(netCode);
            tempRace.setRaceCode(FinaBookConstant.SysRaceCode.NB_BIS_ACC_DTL_COLLECT.get());
            tempRace.setRaceName(FinaBookConstant.SysRaceCode.NB_BIS_ACC_DTL_COLLECT.getName());
            tempRace.setSysRaceCode(FinaBookConstant.SysRaceCode.NB_BIS_ACC_DTL_COLLECT.get());
            FcBusRaceDTO fcBusRaceDTO = new FcBusRaceDTO();
            BeanUtils.copyProperties(tempRace, fcBusRaceDTO);
            iFcBusRaceService.save(fcBusRaceDTO, new String[]{netCode}, userId);
            tempRace = raceList.get(0);
            List<FcBusRaceDTO> fcBusRaceDTOS1 = iFcBusRaceService.findBySysRaceCode(FinaBookConstant.SysRaceCode.NB_BIS_ACC_DTL_COLLECT.get());
            FcBusRaceDTO fcBusRaceDTO1 = fcBusRaceDTOS1.get(0);
            BeanUtils.copyProperties(fcBusRaceDTO1, tempRace);
        }
        return tempRace;
    }


    /**
     * 暂存下拨流水记账凭证
     */
    public void save(FcAccbookVoucherDTO fcAccbook, String id, String userId) {
        check(fcAccbook);
        //根据流水Id查询记账数据
        BisAccDtlTallyVO entity = bisAccDtlTallyDao.getBisAccDtlInfoById(id);
        //暂存
        String eState = entity.getEstate();//原状态
        if (NbconsoleConstant.NbcEstate.E_STATE_WAIT_CHECK.getValue().equals(eState) || NbconsoleConstant.NbcEstate.E_STATE_PASS_BACK.getValue().equals(eState) || NbconsoleConstant.NbcEstate.E_STATE_TEMP.getValue().equals(eState)) {//改账
            throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_STATUSCHANGE);//状态已改变，刷新页面重试
        }
        setAccBookVoucher(fcAccbook);
        VoucherWrapperDTO voucher = setVoucherWrapperDTO(fcAccbook, userId);
        VoucherWrapperDTO voucherWrapperDTO = new VoucherWrapperDTO();
        BeanUtils.copyProperties(voucher, voucherWrapperDTO);
        FcAccbookDTO accbook = iFcAccbookService.saveFcAccbook(voucherWrapperDTO);

        entity.setOperatorCode(UserContextHelper.helper().getCurrentUser().getId());
        entity.setBusGlobalId(accbook.getBusGlobalId());
        entity.setBookId(accbook.getId());
        entity.setEstate(NbconsoleConstant.NbcEstate.E_STATE_TEMP.getValue());

        //更新收款记账信息
        int count = 0;
        String newEState = NbconsoleConstant.NbcEstate.E_STATE_TEMP.getValue();
        List<String> idList = Lists.newArrayList();
        idList.add(entity.getId());
        if (NbconsoleConstant.NbcEstate.E_STATE_WAIT_CHECK.getValue().equals(eState) || NbconsoleConstant.NbcEstate.E_STATE_PASS_BACK.getValue().equals(eState) || NbconsoleConstant.NbcEstate.E_STATE_TEMP.getValue().equals(eState)) {//改账
            //改账暂存
            count = bisAccDtlTallyDao.updateEditPassSign(idList, newEState);
        } else {
            //记账暂存
            count = bisAccDtlTallyDao.updateTallySign(entity, idList, newEState);
        }
        if (count < 1) {
            throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_STATUSCHANGE);//状态已改变，刷新页面重试
        }
        //待记账消息提醒完成
        notificationService.complete(NotificationConstant.FINACIAL.NETCHALK_TALLY, entity.getId(), userId);
    }


    /**
     * 功能介绍 : 记账提交凭证对象
     */
    public void submit(FcAccbookVoucherDTO fcAccbook, String id, String userId) {
        check(fcAccbook);
        //根据流水Id查询记账数据
        BisAccDtlTallyVO entity = bisAccDtlTallyDao.getBisAccDtlInfoById(id);
        setAccBookVoucher(fcAccbook);
        VoucherWrapperDTO voucher = setVoucherWrapperDTO(fcAccbook, userId);
        boolean isAutoCheck = btNetChalkService.isAutoCheck();
        if (isAutoCheck) {
            //获取自动复核员用户
            btNetChalkService.setCheckUser(voucher);
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
        entity.setOperatorCode(accbook.getOperatorCode());
        entity.setBusGlobalId(accbook.getBusGlobalId());
        entity.setBookId(accbook.getId());
        entity.setEstate(NbconsoleConstant.NbcEstate.E_STATE_WAIT_CHECK.getValue());
        //更新付款单记账信息
        int count = 0;
        String newEState = NbconsoleConstant.NbcEstate.E_STATE_WAIT_CHECK.getValue();
        List<String> idList = Lists.newArrayList();
        idList.add(entity.getId());
        if (NbconsoleConstant.NbcEstate.E_STATE_WAIT_CHECK.getValue().equals(eState) || NbconsoleConstant.NbcEstate.E_STATE_PASS_BACK.getValue().equals(eState) || NbconsoleConstant.NbcEstate.E_STATE_TEMP.getValue().equals(eState)) {//改账
            if (isAutoCheck) {
                //改账自动复核
                count = bisAccDtlTallyDao.updateEditAutoCheckSign(entity, idList);
            } else {
                //改账提交
                count = bisAccDtlTallyDao.updateEditPassSign(idList, newEState);
            }
        } else {
            if (isAutoCheck) {
                //更新付款单复核信息
                count = bisAccDtlTallyDao.updateAutoCheckSign(entity, idList);
            } else {
                //记账提交
                count = bisAccDtlTallyDao.updateTallySign(entity, idList, newEState);
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
        return FinaBookConstant.BusOriType.BIS_ACC_DTL_DOWN.get();
    }


    /**
     * 构建凭证
     */
    public FcAccbookPageDTO createAccbookTotal(String ids, String opType, String userId) {
        String[] idArr = ids.split(",");
        //用第一条数据来构建凭证
        FcAccbookPageDTO fcAccbookPageDTO = processAccbookTotal(idArr[0], opType, userId);

        List<BisAccDtlTallyVO> bisAccDtlTallyVOS = bisAccDtlTallyDao.listBisAccDtlInfo(Arrays.asList(idArr));
        String bankAcc = bisAccDtlTallyVOS.get(0).getBankAcc();
        String transTime = DateUtils.date2Str(bisAccDtlTallyVOS.get(0).getTransTime(),DateUtils.date_sdf());

        BigDecimal totalMoney = BigDecimal.ZERO;
        for (BisAccDtlTallyVO bisAccDtlTallyVO : bisAccDtlTallyVOS) {
            if(!bankAcc.equals(bisAccDtlTallyVO.getBankAcc())){
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_BANKACC_NOTSAME);
            }
            if(!transTime.equals(DateUtils.date2Str(bisAccDtlTallyVO.getTransTime(),DateUtils.date_sdf()))){
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_TRANSDATE_NOTSAME);
            }
            totalMoney = totalMoney.add(bisAccDtlTallyVO.getMoney());
        }

        fcAccbookPageDTO.getBookDtls().get(0).setDebitMoney(totalMoney); //借方金额

        fcAccbookPageDTO.getBookDtls().get(1).setCreditMoney(totalMoney); //贷方金额
        fcAccbookPageDTO.getBookDtls().get(1).setAccount(null);
        fcAccbookPageDTO.getBookDtls().get(1).setAccName(null);

        return fcAccbookPageDTO;
    }

    /**
     * 创建凭证信息
     */
    public FcAccbookPageDTO processAccbookTotal(String id, String opType, String userId) {
        FcAccbookPageDTO entity = new FcAccbookPageDTO();
        //根据流水Id查询记账数据
        BisAccDtlTallyVO bisAccDtlTallyVO = bisAccDtlTallyDao.getBisAccDtlInfoById(id);
        if (EmptyUtils.isEmpty(bisAccDtlTallyVO)) {
            throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_BTNETCHALK_NULL);
        }
        VoucherRenderControlVo controlVo = new VoucherRenderControlVo();
        controlVo.setExternalAccEditable(false);
        controlVo.setExternalCorpEditable(false);
        controlVo.setBankEditable(false);
        controlVo.setCityEditable(false);
        controlVo.setProvinceEditable(false);
        controlVo.setSummaryEditable(false);
        List<VoucherRenderControlDTO> controlBeans = getControlBeanEdit(FinaBookConstant.SysRaceCode.NB_BIS_ACC_DTL_COLLECT.get(), 2);
        if (NbconsoleConstant.OpType.INIT.get().equals(opType)) {
            FcBusinessRuleDefineDTO ruleDefine = getBusinessRuleExp(FinaBookConstant.SysRaceCode.NB_BIS_ACC_DTL_COLLECT.get());
            entity = processTallyAccbookTotal(bisAccDtlTallyVO, controlVo, controlBeans, ruleDefine, userId);
        } else {
            if (EmptyUtils.isEmpty(bisAccDtlTallyVO.getBookId())) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOK_NULL);
            }
            List<FcAccbookDtlDTO> bookDtls = tallyService.getBookDtlVo(bisAccDtlTallyVO.getBookId());
            FcAccbookDTO accBook = iFcAccbookService.getAccBookVoById(bisAccDtlTallyVO.getBookId());
            accBook.setBusGlobalId(bisAccDtlTallyVO.getBusGlobalId());
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
    public FcAccbookPageDTO processTallyAccbookTotal(BisAccDtlTallyVO bisAccDtlTallyVO, VoucherRenderControlVo controlBean, List<VoucherRenderControlDTO> controlBeans, FcBusinessRuleDefineDTO ruleDefine, String userId) {
        FcAccbookPageDTO entity = new FcAccbookPageDTO();
        FcAccbookDTO accBook = new FcAccbookDTO();
        FcAccbookExtVO bookExt = new FcAccbookExtVO();
        entity.setAccbook(accBook);
        List<FcAccbookDtlDTO> accbookDtls = null;
        accbookDtls = getAccbookDtls(entity, ruleDefine, controlBeans, bisAccDtlTallyVO, bookExt);

        accBook.setRmk(bisAccDtlTallyVO.getPurpose());//备注
        FcBusRaceVo fcbusRace = getFcBusRaceId(bisAccDtlTallyVO.getCorpId(), userId);
        accBook.setEmergencySign(FinacialConstant.EMERGENCYSIGN_G);//加急标志
        accBook.setBusRaceId(fcbusRace.getId());
        accBook.setRaceName(fcbusRace.getRaceName());
        accBook.setVoucherType(ruleDefine.getVoucherType());
        entity.setAccbook(accBook);
        entity.setBookDtls(accbookDtls);
        return entity;
    }

    /**
     * 合并暂存下拨流水记账凭证
     *
     * @param fcAccbook
     * @param idList
     * @param userId
     * @return
     */
    public void mergeSave(FcAccbookVoucherDTO fcAccbook, List<String> idList, String userId) {
        check(fcAccbook);
        setAccBookVoucher(fcAccbook);
        // 合并记账，对方账号信息设置为空
        fcAccbook.setBookExt(new FcAccbookExtDTO());
        VoucherWrapperDTO voucher = setVoucherWrapper(fcAccbook, userId);
        FcAccbookDTO accbook = iFcAccbookService.saveFcAccbook(voucher);
        BisAccDtlTallyVO entity = new BisAccDtlTallyVO();
        entity.setOperatorCode(accbook.getOperatorCode());
        entity.setBookId(accbook.getId());
        entity.setBusGlobalId(accbook.getBusGlobalId());
        entity.setEstate(NbconsoleConstant.NbcEstate.E_STATE_TEMP.getValue());

        // 记账暂存，更新收款记账信息
        String newEState = NbconsoleConstant.NbcEstate.E_STATE_TEMP.getValue();
        int count = bisAccDtlTallyDao.updateTallySign(entity, idList, newEState);
        if (count < idList.size()) {
            throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_STATUSCHANGE);//状态已改变，刷新页面重试
        }
        //待记账消息提醒完成
        idList.forEach(id -> notificationService.complete(NotificationConstant.FINACIAL.NETCHALK_TALLY, id, userId));
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

    /**
     * 合并记账构建凭证
     *
     * @param idList
     * @param opType
     * @param userId
     * @return
     */
    public FcAccbookPageDTO batchCreateAccbook(List<String> idList, String opType, String userId) {
        List<BisAccDtlTallyVO> chalkList = bisAccDtlTallyDao.listBisAccDtlInfo(idList);
        // 合并记账前数据校验
        this.mergeTallyCheck(chalkList, idList);
        // 多个流水时，获取最晚的交易日期的流水实体
        BisAccDtlTallyVO bisAccDtlTallyVO = chalkList.stream().sorted(Comparator.comparing(BisAccDtlTallyVO::getTransTime).reversed()).collect(Collectors.toList()).stream().findFirst().orElse(chalkList.get(0));
        // 定义返回值对象
        FcAccbookPageDTO entity = new FcAccbookPageDTO();
        if (NbconsoleConstant.OpType.INIT.get().equals(opType)) {
            // 根据系统凭证种类查询业务记账规则
            FcBusinessRuleDefineDTO ruleDefine = getBusinessRuleExp(FinaBookConstant.SysRaceCode.NB_BIS_ACC_DTL_COLLECT.get());
            // 获取业务种类对象
            FcBusRaceVo fcbusRace = getFcBusRaceId(bisAccDtlTallyVO.getCorpId(), userId);

            // 账务凭证主档表记录
            FcAccbookDTO accBook = new FcAccbookDTO();
            accBook.setBusRaceId(fcbusRace.getId());
            accBook.setRaceName(fcbusRace.getRaceName());
            accBook.setInterestStartDate(bisAccDtlTallyVO.getTransTime());
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
            if (EmptyUtils.isEmpty(bisAccDtlTallyVO.getBookId())) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOK_NULL);
            }
            List<FcAccbookDtlDTO> bookDtls = tallyService.getBookDtlVo(bisAccDtlTallyVO.getBookId());
            if (org.springframework.util.CollectionUtils.isEmpty(bookDtls)) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_QUERY_ACCBOOKDTL_NULL);
            }
            // 账务凭证主档表记录
            FcAccbookDTO accbook = iFcAccbookService.getAccBookVoById(bisAccDtlTallyVO.getBookId());
            accbook.setBusGlobalId(bisAccDtlTallyVO.getBusGlobalId());
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

    private List<FcAccbookDtlDTO> batchProcessAccbookDtls(List<BisAccDtlTallyVO> chalkList, FcBusinessRuleDefineDTO ruleDefine) {
        List<FcAccbookDtlDTO> accbookDtls = Lists.newArrayList();
        List<FcVoucherEntryDefineDTO> entrys = ruleDefine.getVouchers();
        chalkList.forEach(bisAccDtlTallyVO -> entrys.forEach(entry -> {
            //借方，银行活期
            if (entry.getBalanceWay().intValue() == 1 && entry.getEntryType().intValue() == 10) {
                FcAccbookDtlDTO accbookDtl = new FcAccbookDtlDTO();
                accbookDtl.setCreditMoney(BigDecimal.ZERO);
                accbookDtl.setDebitMoney(bisAccDtlTallyVO.getMoney());
                accbookDtl.setSummaryContent(bisAccDtlTallyVO.getPurpose());
                accbookDtl.setOrigVoucherNo(bisAccDtlTallyVO.getId());
                accbookDtl.setOrigVoucherId(bisAccDtlTallyVO.getId());
                accbookDtl.setVoucherType(entry.getVoucherType());
                accbookDtl.setAccount(bisAccDtlTallyVO.getOppCntAccount());
                accbookDtl.setAccName(bisAccDtlTallyVO.getOppCntAccName());

                VoucherRenderControlDTO bean = new VoucherRenderControlDTO();
                bean.setAccountEditable(true);
                bean.setSummaryEditable(true);
                bean.setCashFluxEditable(true);
                bean.setVoucherTypeEditable(false);
                accbookDtl.setControlVo(bean);
                accbookDtls.add(accbookDtl);
                //贷方，企业活期
            } else if (entry.getBalanceWay().intValue() == -1 && entry.getEntryType().intValue() == 10) {
                FcAccbookDtlDTO accbookDtl = new FcAccbookDtlDTO();
                accbookDtl.setAccount(bisAccDtlTallyVO.getCntAccount());
                accbookDtl.setAccName(bisAccDtlTallyVO.getCntAccName());
                VoucherRenderControlDTO vc = new VoucherRenderControlDTO();
                vc.setAccountEditable(true);
                vc.setSummaryEditable(true);
                accbookDtl.setControlVo(vc);
                accbookDtl.setDebitMoney(BigDecimal.ZERO);
                accbookDtl.setCreditMoney(bisAccDtlTallyVO.getMoney());
                accbookDtl.setSummaryContent(bisAccDtlTallyVO.getPurpose());
                accbookDtl.setOrigVoucherNo(bisAccDtlTallyVO.getId());
                accbookDtl.setOrigVoucherId(bisAccDtlTallyVO.getId());
                accbookDtl.setVoucherType(entry.getVoucherType());
                accbookDtls.add(accbookDtl);
            }
        }));
        return accbookDtls;
    }

    private void mergeTallyCheck(List<BisAccDtlTallyVO> chalkList, List<String> idList) {
        if (org.springframework.util.CollectionUtils.isEmpty(idList) || org.springframework.util.CollectionUtils.isEmpty(chalkList) || chalkList.size() < 2) {
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
        boolean isAutoCheck = btNetChalkService.isAutoCheck();
        if (isAutoCheck) {
            //获取自动复核员用户
//            setCheckUser(voucher);
            voucher.setAutoCheck(true);
        }
        FcAccbookDTO accbook = iFcAccbookService.submitFcAccbook(voucher);

        List<BisAccDtlTallyVO> entityList = bisAccDtlTallyDao.listBisAccDtlInfo(idList);
        //待记账消息提醒完成
        entityList.forEach(entity -> notificationService.complete(NotificationConstant.FINACIAL.NETCHALK_TALLY, entity.getId(), userId));
        //如果自动复核，则复核凭证通过
        completeEditNotificationInfo(voucher, userId);
        completeCheckNotificationInfo(voucher, userId);

        BisAccDtlTallyVO entity = new BisAccDtlTallyVO();
        entity.setOperatorCode(accbook.getOperatorCode());
        entity.setBookId(accbook.getId());
        entity.setBusGlobalId(accbook.getBusGlobalId());
        entity.setEstate(NbconsoleConstant.NbcEstate.E_STATE_WAIT_CHECK.getValue());
        // 记账提交待复核
        String newEState = NbconsoleConstant.NbcEstate.E_STATE_WAIT_CHECK.getValue();
        int count = 0;
        if ("MODIFY".equals(opType)) {
            if (isAutoCheck) {
                //改账自动复核
                count = bisAccDtlTallyDao.updateEditAutoCheckSign(entity, idList);
            } else {
                //改账提交
                count = bisAccDtlTallyDao.updateEditPassSign(idList, newEState);
            }
        } else if ("SUBMIT".equals(opType)) {
            if (isAutoCheck) {
                //记账自动复核
                count = bisAccDtlTallyDao.updateAutoCheckSign(entity, idList);
            } else {
                //记账提交
                count = bisAccDtlTallyDao.updateTallySign(entity, idList, newEState);
            }
        }
        if (count < idList.size()) {
            throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_STATUSCHANGE);//状态已改变，刷新页面重试
        }

        if (!isAutoCheck) {
            sendCheckNotificationInfo(voucher, userId);
        }
    }

    /**
     * 取消
     */
    public void cancelBtNetChalk(String id, ICurrentUser user) {
        BisAccDtlTallyVO bisAccDtlTallyVO = bisAccDtlTallyDao.getBisAccDtlInfoById(id);
        bisAccDtlTallyVO.setEstate(NbconsoleConstant.NbcEstate.E_STATE_UNAVAIL.get());
        bisAccDtlTallyDao.updateBisAccDtlEState(bisAccDtlTallyVO);
        //待记账消息提醒完成
        notificationService.complete(NotificationConstant.FINACIAL.NETCHALK_TALLY, id, user.getId());
    }

    /**
     * 打回下拨流水记账
     *
     * @param fcAccbook
     * @param id
     * @param user
     * @return
     */
    public void backFcAccbook(FcAccbookVoucherDTO fcAccbook, String id, ICurrentUser user) {
        createVoucher(fcAccbook);
        String opType = fcAccbook.getOpType().name();
        BisAccDtlTallyVO entity = bisAccDtlTallyDao.getBisAccDtlInfoById(id);
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
            count = bisAccDtlTallyDao.updateEditBackSign(id, eState);//改账打回
            if (count < 1) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_STATUSCHANGE);//状态已改变，刷新页面重试
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("money", NumberUtil.formatNumeric(entity.getMoney(), "#,##0.00"));
            notificationService.sendToUserByResPermission(entity.getId(), ResourceConstant.FCL_BT_NET_CHALK_COMMON_QUERY, NotificationConstant.FINACIAL.NETCHALK_TALLY, map, entity.getCorpId());
        } else if ("CHECK".equals(opType)) {
            entity.setEstate(NbconsoleConstant.NbcEstate.E_STATE_PASS_BACK.getValue());
            count = bisAccDtlTallyDao.updateCheckBackSign(id);//复核打回
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
     * 通过bookId查询下拨流水
     *
     * @param bookId
     * @return
     */
    public List<String> listByBookId(String bookId) {
        return bisAccDtlTallyDao.listByBookId(bookId);
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
     * 下拨流水合并打回
     *
     * @param fcAccbook
     * @param idList
     * @param user
     * @return
     */
    public void mergeBackFcAccbook(FcAccbookVoucherDTO fcAccbook, List<String> idList, ICurrentUser user) {
        createVoucher(fcAccbook);
        String opType = fcAccbook.getOpType().name();
        VoucherWrapperDTO Voucher = setVoucherWrapper(fcAccbook, user.getId());
        completeEditNotificationInfo(Voucher, user.getId());
        completeCheckNotificationInfo(Voucher, user.getId());
        List<BisAccDtlTallyVO> entityList = bisAccDtlTallyDao.listBisAccDtlInfo(idList);

        if ("MODIFY".equals(opType)) {
            // 改账打回
            iFcAccbookService.deleteFcAccbook(Voucher);
            int count = bisAccDtlTallyDao.mergeUpdateEditBackSign(idList);//改账打回
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
            int count = bisAccDtlTallyDao.mergeUpdateCheckBackSign(idList);
            if (count < idList.size()) {
                throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_STATUSCHANGE);//状态已改变，刷新页面重试
            }
            iFcAccbookService.backFcAccbook(Voucher);
            sendBackNotificationInfo(Voucher);
        }
    }


    /**
     * 复核下拨流水记账
     *
     * @param fcAccbook
     * @param id
     * @param user
     * @return
     */
    public void createVoucher(FcAccbookVoucherDTO fcAccbook, String id, ICurrentUser user) {
        createVoucher(fcAccbook);
        VoucherWrapperDTO Voucher = setVoucherWrapperDTO(fcAccbook, user.getId());
        BisAccDtlTallyVO entity = bisAccDtlTallyDao.getBisAccDtlInfoById(id);
        checkFcAccbook(Voucher, entity, user.getId());
    }

    /***
     * 复核
     */
    public void checkFcAccbook(VoucherWrapperDTO Voucher, BisAccDtlTallyVO entity, String userId) {

        VoucherWrapperDTO voucherWrapperDTO = new VoucherWrapperDTO();
        BeanUtils.copyProperties(Voucher, voucherWrapperDTO);
        voucherWrapperDTO = iFcAccbookService.checkFcAccbook(voucherWrapperDTO);
        FcAccbookDTO accbook = voucherWrapperDTO.getTarget().get(0).getAccbook();
        entity.setEstate(NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.getValue());
        List<String> idList = Lists.newArrayList();
        idList.add(entity.getId());
        int count = bisAccDtlTallyDao.updateCheckSign(entity, idList);//改账打回
        if (count < 1) {
            throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_STATUSCHANGE);//状态已改变，刷新页面重试
        }
        completeCheckNotificationInfo(Voucher, userId);
        completeEditNotificationInfo(Voucher, userId);
    }

    /**
     * 合并复核下拨流水记账
     *
     * @param fcAccbook
     * @param idList
     * @param user
     * @return
     */
    public void mergeCreateVoucher(FcAccbookVoucherDTO fcAccbook, List<String> idList, ICurrentUser user) {
        createVoucher(fcAccbook);
        // 合并记账，对方账号信息设置为空
        fcAccbook.setBookExt(new FcAccbookExtDTO());
        VoucherWrapperDTO voucher = setVoucherWrapper(fcAccbook, user.getId());

        VoucherWrapperDTO dto = iFcAccbookService.checkFcAccbook(voucher);

        FcAccbookDTO accbook = dto.getTarget().get(0).getAccbook();
        BisAccDtlTallyVO entity = new BisAccDtlTallyVO();
        entity.setEstate(NbconsoleConstant.NbcEstate.E_STATE_CHECK_PASS.getValue());

        int count = bisAccDtlTallyDao.updateCheckSign(entity, idList);
        if (count < idList.size()) {
            throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_STATUSCHANGE);//状态已改变，刷新页面重试
        }

        completeCheckNotificationInfo(voucher, user.getId());
        completeEditNotificationInfo(voucher, user.getId());
    }

}
