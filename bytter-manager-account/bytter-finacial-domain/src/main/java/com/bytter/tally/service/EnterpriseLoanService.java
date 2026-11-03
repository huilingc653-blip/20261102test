package com.bytter.tally.service;

import com.bytter.biz.finacial.api.constant.FinaBookConstant;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.SysRaceCode;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.NbconsoleConstant;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
import com.bytter.biz.finacial.api.dto.*;
import com.bytter.biz.finacial.api.enums.accbook.OperateType;
import com.bytter.biz.finacial.api.enums.accbook.ProcessType;
import com.bytter.biz.finacial.api.enums.bankCollect.CollectMessageEnum;
import com.bytter.biz.finacial.api.service.IFcAccbookDtlService;
import com.bytter.biz.finacial.api.service.IFcAccbookService;
import com.bytter.biz.finacial.api.service.IFcBusinessRuleDefineService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.api.ICmsLoanService;
import com.bytter.finance.api.ICmsProcessMsgService;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.CmsProcessMsgConstant;
import com.bytter.finance.dto.CmsLoanInfoDTO;
import com.bytter.finance.dto.CmsLoanSendDTO;
import com.bytter.finance.enums.PrecativeMessageEnum;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.AccountGrantDTO;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.FcAccPlusDTO;
import com.bytter.tally.vo.*;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
@Service("enterpriseLoanService")
@Transactional
@Slf4j
public class EnterpriseLoanService implements ITaskService {


    @DubboReference(check = false)
    private IAccountGrantService accGrantService;
    @DubboReference(check = false)
    private ICmsLoanService iCmsLoanService;
    @DubboReference(check = false)
    private IInnerAccountService innerAccountService;
    @DubboReference(check = false)
    private IFcAccbookService iFcAccbookService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IFcBusinessRuleDefineService fcBusinessRuleDefineService;
    @DubboReference(check = false)
    private ICmsProcessMsgService cmsProcessMsgService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private IAccountGrantService iAccountGrantService;
    @Autowired
    private PrecativeTallyService precativeTallyService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IFcAccService fcAccService;
    @DubboReference(check = false)
    private IFcAccbookService fcAccBookService;
    @DubboReference(check = false)
    private IFcAccbookDtlService fcAccBookDtlService;

    @DubboReference(check = false)
    private IEnterpriseLoanService iEnterpriseLoanService;

    /**
     * <b>功能介绍：</b>获取记账账号<br>
     * <b>日期：</b>2018年3月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param netId
     * @param corpId
     * @param curId
     * @param userId
     * @param permission
     * @return
     * @version 1.0
     */
    public List<FcAccPlusDTO> getTallyAccount(String netId, String corpId, String curId, String userId, String permission) {
        /*List<String> accList = accGrantService.listByMultiVal(userId, corpId, null, null, AccountGrantConstant.INNER_BANK_TYPE, permission);
        if (EmptyUtils.isEmptyList(accList)) {
            return new ArrayList<FcAccPlusDTO>();
        }
        //可记账内部账号
        return fcAccService.getTallyAccTypeAccList(netId, corpId, curId, accList);*/
        ConfigDTO corpConfig = configService.getConfig("sys", "controlType");
        List<String> accs = null;
        //TODO 判断账号授权是单位级 还是账号级别
        boolean corpGrantFlag = StringUtils.equals(corpConfig.getValue(), CommonConstant.YES);
        if (!corpGrantFlag) {
            accs = accGrantService.listByMultiVal(userId, corpId, null, null, AccountGrantConstant.INNER_BANK_TYPE, permission);
        }
        if (!corpGrantFlag && EmptyUtils.isEmptyList(accs)) {
            return new ArrayList<FcAccPlusDTO>();
        }
        List<FcAccPlusDTO> accDTOs = null;
        if (!corpGrantFlag) {
            accDTOs = iEnterpriseLoanService.getTallyAccTypeAccs(netId, corpId, curId, accs); //可记账内部账号
        } else {
            accDTOs = iEnterpriseLoanService.getTallyAccByCorp(netId, corpId, curId);
        }
        return accDTOs;
    }
    public CmsLoanSendDTO getLoanSendDtoBybillCode(String billCode) {
        return iCmsLoanService.getLoanSendByBillCode(billCode);
    }
    /**
     * <b>功能介绍：</b>记账暂存/提交<br>
     * <b>日期：</b>2018年1月17日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param fcAccbook
     * @param user
     * @version 1.0
     */
    public void saveAccbook(FcAccbookVoucherDTO fcAccbook, ICurrentUser user, String opT, String type) {
        validata(fcAccbook.getBookDtls().get(0).getOrigVoucherNo());
        VoucherWrapperDTO voucher = setVoucherWrapper(fcAccbook, user.getId());
        //TODO 判断是否已记账
        FcAccDTO acc = fcAccbook.getBookDtls().get(0).getAccDTO();
        if ("add".equals(type)) {
            createNewLoanAcc(acc, user, voucher.getBookVoucher().getBookDtls().get(0).getOrigVoucherNo());
            voucher=processVoucher(voucher, "0".equals(opT) ? OperateType.ADD : OperateType.SUBMIT);
        } else if ("edit".equals(type)) {//改账
            updateLoanAcc(acc);
//			voucher = iFcAccbookService.getVoucher(getOriType(), fcAccbook.getAccbook().getBusGlobalId());
            voucher=processVoucher(voucher, OperateType.MODIFY);
        }
        setCmsLoanAcc(voucher, opT, user);
    }
    /**
     * <b>功能介绍：</b>改账打回<br>
     * <b>日期：</b>2018年3月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param fcAccbook
     * @param iuser
     * @version 1.0
     */
    public void editBackAccbook(FcAccbookVoucherDTO fcAccbook, ICurrentUser iuser) {
        validataBack(fcAccbook.getBookDtls().get(0).getOrigVoucherNo());//验证
        VoucherWrapperDTO voucher = iFcAccbookService.getVoucher(getOriType(), fcAccbook.getAccbook().getBusGlobalId());
        voucher.setAutoAccounter(false);
        voucher.setUserCode(iuser.getId());//设置用户
        processVoucher(voucher, OperateType.DELETE);
        setCmsLoanAcc(voucher, "3", iuser);
        innerAccountService.cancelFcAcc(fcAccbook.getBookDtls().get(0).getAccount(), new Date(), iuser);// 贷款账号销户
    }
    /**
     * <b>功能介绍：</b>复核账务<br>
     * <b>日期：</b>2018年3月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param opT
     * @param busGlobalId
     * @version 1.0
     */
    public void checkAccbook(String opT, String busGlobalId, ICurrentUser user) {
        VoucherWrapperDTO voucher = iFcAccbookService.getVoucher(getOriType(), busGlobalId);
        validata(voucher.getBookVoucher().getBookDtls().get(0).getOrigVoucherNo());//验证
        validataBack(voucher.getBookVoucher().getBookDtls().get(0).getOrigVoucherNo());//验证
        voucher.setAutoAccounter(false);
        voucher.setUserCode(user.getId());//设置用户
        voucher = processVoucher(voucher, "1".equals(opT) ? OperateType.CHECK : OperateType.BACK);
        setCmsLoanAcc(voucher, "1".equals(opT) ? "4" : "2", user);
        List<String> users = new ArrayList<>();
        users.add(user.getId());
        com.bytter.system.api.dto.FcAccDTO newLoanAcc = innerAccountService.getByBlnctrAcc(voucher.getBookVoucher().getBookDtls().get(0).getAccount());
        setAccountGrant(newLoanAcc.getId(), newLoanAcc.getCorpId(), users);
    }
    /**
     * <b>功能介绍：</b>设置业务数据<br>
     * <b>日期：</b>2018年3月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param voucher
     * @param opT
     * @version 1.0
     */
    public void setCmsLoanAcc(VoucherWrapperDTO voucher, String opT, ICurrentUser user) {
        CmsLoanSendDTO sendDTO = iCmsLoanService.getLoanSendByBillCode(voucher.getBookVoucher().getBookDtls().get(0).getOrigVoucherNo());
        CmsLoanSendDTO send = new CmsLoanSendDTO();
        FcAccbookVoucherDTO fcAccbook = voucher.getBookVoucher();
        send.setBillCode(fcAccbook.getBookDtls().get(0).getOrigVoucherNo());
        send.setOrderNo(fcAccbook.getAccbook().getOrderNo());
        for (FcAccbookDtlDTO dtl : fcAccbook.getBookDtls()) {
            if (dtl.getCreditMoney().compareTo(BigDecimal.ZERO) > 0) {
                send.setProvideLoanAcc(dtl.getAccount());
            }
            if (dtl.getDebitMoney().compareTo(BigDecimal.ZERO) > 0) {
                send.setLoanAcc(dtl.getAccount());
            }
        }
        if ("0".equals(opT)) {//暂存
            //设置记账员
            send.setOperatorCode(user.getId());
            send.setStatus(NbconsoleConstant.NbcEnterpriseStatus.NBC_STATUS_TEMP.get());
            send.setBizStatus(CmsConstant.BUSINESSSTATE_92);
            cmsProcessMsgService.insertProcessMsg(send.getBillCode(), "A2", CmsProcessMsgConstant.MSGA_TALLYMODIFY, CmsProcessMsgConstant.MSGA_SEND_TALLY_TEMP, user);
            //待记账消息提醒完成
            notificationService.complete(NotificationConstant.FINACIAL.ENTERPRISELOAN_TALLY, sendDTO.getId(), user.getId());
        } else if ("1".equals(opT)) {//提交
            //设置记账员
            send.setOperatorCode(user.getId());
            send.setStatus(NbconsoleConstant.NbcEnterpriseStatus.NBC_STATUS_SUBMIT.get());
            send.setBizStatus(CmsConstant.BUSINESSSTATE_93);
            //待记账消息提醒完成
            notificationService.complete(NotificationConstant.FINACIAL.ENTERPRISELOAN_TALLY, sendDTO.getId(), user.getId());
            if (isAutoCheck()) {
                String netId = corpService.getNetByCorpId(sendDTO.getNetId()).getNetId();
                List<String> users = precativeTallyService.getAutoCheckUser(netId);
                com.bytter.system.api.dto.FcAccDTO newLoanAcc = innerAccountService.getByBlnctrAcc(voucher.getBookVoucher().getBookDtls().get(0).getAccount());
                setAccountGrant(newLoanAcc.getId(), newLoanAcc.getCorpId(), users);
                cmsProcessMsgService.insertProcessMsg(send.getBillCode(), "A2", CmsProcessMsgConstant.MSGA_TALLYSUBMIT, CmsProcessMsgConstant.MSGA_SEND_TALLY_SUBMIT, user);
                send.setStatus(NbconsoleConstant.NbcEnterpriseStatus.NBC_STATUS_PASS.get());
                send.setBizStatus(CmsConstant.BUSINESSSTATE_42);
                cmsProcessMsgService.insertProcessMsg(send.getBillCode(), "A2", CmsProcessMsgConstant.MSGA_TALLYPASS, CmsProcessMsgConstant.MSGA_SEND_TALLY_CHECKOK, user);
            } else {
                cmsProcessMsgService.insertProcessMsg(send.getBillCode(), "A2", CmsProcessMsgConstant.MSGA_TALLYSUBMIT, CmsProcessMsgConstant.MSGA_SEND_TALLY_SUBMIT, user);
                completeEditNotificationInfo(voucher, user);
                completeCheckNotificationInfo(voucher, user);
                sendCheckNotificationInfo(voucher, user);
            }
        } else if ("2".equals(opT)) {//复核打回
            send.setStatus(NbconsoleConstant.NbcEnterpriseStatus.NBC_STATUS_CHECK_BACK.get());
            send.setBizStatus(CmsConstant.BUSINESSSTATE_94);
            cmsProcessMsgService.insertProcessMsg(send.getBillCode(), "A2", CmsProcessMsgConstant.MSGA_TALLYCHECKBACK, CmsProcessMsgConstant.MSGA_SEND_TALLY_CHECKBACK, user);
            completeCheckNotificationInfo(voucher, user);
            sendBackNotificationInfo(voucher);
        } else if ("3".equals(opT)) {//改账打回
            send.setProvideLoanAcc("");
            send.setLoanAcc("");
            send.setOrderNo("");
            send.setStatus(NbconsoleConstant.NbcEnterpriseStatus.NBC_STATUS_MODIFY_BACK.get());
            send.setBizStatus(CmsConstant.BUSINESSSTATE_95);
            cmsProcessMsgService.insertProcessMsg(send.getBillCode(), "A2", CmsProcessMsgConstant.MSGA_EDITTALLYBACK, CmsProcessMsgConstant.MSGA_SEND_EDITTALLY_BACK, user);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("money", NumberUtil.formatNumeric(sendDTO.getLoanMoney(), "#,##0.00"));
            notificationService.sendToUserByResPermission(sendDTO.getId(), ResourceConstant.FCL_LOAN_TALLY_APPLY, NotificationConstant.FINACIAL.ENTERPRISELOAN_TALLY, map, sendDTO.getCorpId());
        } else if ("4".equals(opT)) {//复核通过
            send.setStatus(NbconsoleConstant.NbcEnterpriseStatus.NBC_STATUS_PASS.get());
            send.setBizStatus(CmsConstant.BUSINESSSTATE_42);
            cmsProcessMsgService.insertProcessMsg(send.getBillCode(), "A2", CmsProcessMsgConstant.MSGA_TALLYPASS, CmsProcessMsgConstant.MSGA_SEND_TALLY_CHECKOK, user);
            completeCheckNotificationInfo(voucher, user);
            completeEditNotificationInfo(voucher, user);
        } else if ("-1".equals(opT)) {//记账打回
            String backReason = voucher.getTarget().get(0).getBookExt().getBackReason();
            send.setProvideLoanAcc("");
            send.setLoanAcc("");
            send.setOrderNo("");
            send.setStatus(NbconsoleConstant.NbcEnterpriseStatus.NBC_NET_BACK.get());
            send.setBizStatus(CmsConstant.BUSINESSSTATE_96);
            send.setRefuseWhy(backReason);
            cmsProcessMsgService.insertProcessMsg(send.getBillCode(), "A2", CmsProcessMsgConstant.MSGA_TALLYBACK, CmsProcessMsgConstant.MSGA_SEND_TALLY_BACK, user);
            //待记账消息提醒完成
            notificationService.complete(NotificationConstant.FINACIAL.ENTERPRISELOAN_TALLY, sendDTO.getId(), user.getId());
        }
        iCmsLoanService.updateCmsLoanSend(send);
    }
    /**
     * <b>功能介绍：</b>构建凭证封装对象<br>
     * <b>日期：</b>2018年1月17日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param fcAccbook
     * @param userId
     * @return
     * @version 1.0
     */
    private VoucherWrapperDTO setVoucherWrapper(FcAccbookVoucherDTO fcAccbook, String userId) {
        fcAccbook.getAccbook().setBusOriType(getOriType());
        fcAccbook.getAccbook().setOrigVoucherNo(fcAccbook.getBookDtls().get(0).getOrigVoucherNo());
        VoucherWrapperDTO voucher = new VoucherWrapperDTO();
        voucher.setBusGlobalId(fcAccbook.getAccbook().getBusGlobalId());
        voucher.setOriType(getOriType());
        voucher.addTarget(fcAccbook);
        voucher.setProcessType(ProcessType.PROCESS_COMMON);
        voucher.setUserCode(userId);//设置用户
        voucher.setAutoAccounter(false);//设置用户
        return voucher;
    }
    /**
     * 判断是否自动复核
     */
    public boolean isAutoCheck() {
        boolean isAutoCheck = CmsConstant.IS_TRUE.equals(configService.getConfig("fcl", "tally_check").getValue());
        return isAutoCheck;
    }
    /**
     * <b>功能介绍：</b>处理账务<br>
     * <b>日期：</b>2018年1月17日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param voucher
     * @param opType
     * @version 1.0
     */
    public VoucherWrapperDTO processVoucher(VoucherWrapperDTO voucher, OperateType opType) {
        if (opType == OperateType.ADD) {//记账暂存
            return iFcAccbookService.saveVoucher(voucher);
        } else if (opType == OperateType.SUBMIT) {//记账提交
            if (isAutoCheck()) {
                voucher.setAutoCheck(true);
            }
            return iFcAccbookService.submitVoucher(voucher);
        } else if (opType == OperateType.CHECK) {//复核
            return iFcAccbookService.checkVoucher(voucher);
        } else if (opType == OperateType.BACK) {//打回
            return iFcAccbookService.backVoucher(voucher);
        } else if (opType == OperateType.DELETE) {//删除
            return iFcAccbookService.deleteVoucher(voucher);
        } else if (opType == OperateType.MODIFY) {//改账
            if (isAutoCheck()) {
                voucher.setAutoCheck(true);
            }
            return iFcAccbookService.submitVoucher(voucher);
        }
        return null;
    }
    /**
     * <b>功能介绍：</b>获取账务信息<br>
     * <b>日期：</b>2018年3月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param sendCode
     * @return
     * @version 1.0
     */
    public FcAccbookPageVO getFcAccbookTempInfo(String sendCode) {
        FcAccbookPageVO page = new FcAccbookPageVO();
        FcAccbookDTO fcAccBook = fcAccBookService.getAccBookVOByOrigVoucherNo(sendCode);
        List<FcAccbookDtlDTO> dtoList = fcAccBookDtlService.getPageDtlsByBookId(fcAccBook.getId());
        List<FcAccbookDtlVO> dtl = BeanUtils.copyListProperties(dtoList, FcAccbookDtlVO.class);
        for (FcAccbookDtlVO dtlvo : dtl) {
            dtlvo.setAccDTO(innerAccountService.getByBlnctrAcc(dtlvo.getAccount()));
        }
        FcAccbookVO bookVo = new FcAccbookVO();
        BeanUtils.copyNotNullProperties(fcAccBook, bookVo);
        page.setAccbook(bookVo);
        page.setBookDtls(dtl);
        return page;
    }
    /**
     * <b>功能介绍：</b>创建放款账号<br>
     * <b>日期：</b>2018年1月17日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param fcAcc
     * @param user
     * @return
     * @version 1.0
     */
    public FcAccDTO createNewLoanAcc(FcAccDTO fcAcc, ICurrentUser user, String origVoucherNo) {
        //自动复核
        fcAcc.setAutoCheck(true);
        // todo:数据转换
        com.bytter.system.api.dto.FcAccDTO fcAccDTO = BeanUtils.copyNewInstanceProperties(fcAcc, com.bytter.system.api.dto.FcAccDTO.class);
        String account = innerAccountService.saveOrUpdate(fcAccDTO, user);
        com.bytter.system.api.dto.FcAccDTO newLoanAcc = innerAccountService.getByBlnctrAcc(account);
        CmsLoanSendDTO sendDTO = iCmsLoanService.getLoanSendByBillCode(origVoucherNo);
        List<String> users = new ArrayList<>();
        users.add(user.getId());
        users.add(sendDTO.getInfoCreateBy());
        users.add(sendDTO.getInfoUpdateBy());
        users.add(sendDTO.getSendCreateBy());
        setAccountGrant(newLoanAcc.getId(), newLoanAcc.getCorpId(), users);
        // todo:数据园
        return BeanUtils.copyNewInstanceProperties(newLoanAcc, FcAccDTO.class);
    }
    /**
     * <b>功能介绍：</b>修改放款账号<br>
     * <b>日期：</b>2018年3月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param fcAcc
     * @version 1.0
     */
    public void updateLoanAcc(FcAccDTO fcAcc) {
        com.bytter.system.api.dto.FcAccDTO acc = innerAccountService.getByBlnctrAcc(fcAcc.getBlnctrAcc());
        BeanUtils.copyNotNullProperties(fcAcc, acc);
        innerAccountService.updateFcAcc(acc);
    }
    public Integer getOriType() {
        return FinaBookConstant.BusOriType.NB_ENTERPRISE_LOAN.get();
    }
    /**
     * 构造发放会计分录
     *
     * @param billCode
     * @param opType
     * @param user
     * @return
     */
    public FcAccbookPageVO createAccbook(String billCode, String opType, ICurrentUser user) {
        FcAccbookPageVO entity = new FcAccbookPageVO();
        CmsLoanSendDTO send = iCmsLoanService.getLoanSendByBillCode(billCode);
        List<VoucherRenderControlVo> controlBeans = getControlBeanEdit(2);
        if (NbconsoleConstant.OpType.INIT.get().equals(opType)) {
            FcBusinessRuleDefineDTO ruleDefine = getBusinessRuleExp(SysRaceCode.NB_ENTERPRISE_LOAN.get());
            entity = processTallyAccbook(send, controlBeans, ruleDefine, user);
        }
        return entity;
    }
    private FcAccbookPageVO processTallyAccbook(CmsLoanSendDTO send, List<VoucherRenderControlVo> controlBeans, FcBusinessRuleDefineDTO ruleDefine, ICurrentUser user) {
        CmsLoanInfoDTO info = iCmsLoanService.getLoanInfoBybillCode(send.getLoanCode());
        FcAccbookPageVO entity = new FcAccbookPageVO();
        FcAccbookVO accBook = new FcAccbookVO();
        FcAccbookExtVO bookExt = new FcAccbookExtVO();
        entity.setAccbook(accBook);
        List<FcAccbookDtlVO> accbookDtls = null;
        accbookDtls = getAccbookDtls(ruleDefine, controlBeans, info, send, user);
        accBook.setRmk(info.getRmk());// 备注
        accBook.setEmergencySign(FinacialConstant.EMERGENCYSIGN_G);// 加急标志
		/*FcBusRace fcbusRace = getFcBusRaceId(info.getCorpId(), user.getId());
		accBook.setBusRaceId(fcbusRace.getId());
		accBook.setRaceName(fcbusRace.getRaceName());*/
        accBook.setVoucherType(ruleDefine.getVoucherType());
        entity.setBookExt(bookExt);
        entity.setAccbook(accBook);
        entity.setBookDtls(accbookDtls);
        return entity;
    }
    private List<FcAccbookDtlVO> getAccbookDtls(FcBusinessRuleDefineDTO ruleDefine, List<VoucherRenderControlVo> controlBeans, CmsLoanInfoDTO info, CmsLoanSendDTO send, ICurrentUser user) {
        List<FcVoucherEntryDefineDTO> entrys = ruleDefine.getVouchers();
        FcAccbookDtlVO accbookDtl = null;
        this.collectionSort(entrys);
        List<FcAccbookDtlVO> accbookDtls = new ArrayList<FcAccbookDtlVO>();
        int balanceWay, groupNum;
        for (FcVoucherEntryDefineDTO entry : entrys) {
            if (entry.getGroupNum() == null) {
                throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_GROUPNUM_IS_NULL);
            }
            accbookDtl = new FcAccbookDtlVO();
            accbookDtl.setCreditMoney(BigDecimal.ZERO);
            accbookDtl.setDebitMoney(BigDecimal.ZERO);
            if (null == entry.getSubjectCode()) {
                throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_SUBJECT_IS_NULL);
            }
            if (FinacialConstant.ENTRY_TYPE_10.intValue() == entry.getEntryType().intValue()) {
                balanceWay = entry.getBalanceWay().intValue();
                groupNum = entry.getGroupNum().intValue();
                /**
                 * "借：1120企业放款 申请单位
                 贷：2120企业活期 申请单位"
                 */
                VoucherRenderControlVo bean = controlBeans.get(groupNum - 1);
                if (Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT).intValue() == balanceWay) {
                    bean.setAccountEditable(true);
                    accbookDtl.setDebitMoney(send.getLoanMoney());
                    accbookDtl.setSubjectCode(entry.getSubjectCode());
                } else if (Integer.valueOf(FinacialConstant.BALANCE_WAY_CREDIT).intValue() == balanceWay) {
                    bean.setCreateAccountAble(true);
                    accbookDtl.setCreditMoney(send.getLoanMoney());
                    accbookDtl.setSubjectCode(entry.getSubjectCode());
                }
                accbookDtl.setCurCode(info.getCurCode());
                accbookDtl.setOrigVoucherNo(send.getBillCode());
                accbookDtl.setOrigVoucherId(send.getId());
                accbookDtl.setSummaryContent(info.getPurpose());
                accbookDtl.setControlVo(bean);
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
    public List<VoucherRenderControlVo> getControlBeanEdit(int size) {
        List<VoucherRenderControlVo> controBeans = new ArrayList<VoucherRenderControlVo>();
        VoucherRenderControlVo controBean = null;
        for (int i = 0; i < size; i++) {
            controBean = new VoucherRenderControlVo();
            controBean.setSummaryEditable(true);
            controBean.setVoucherTypeEditable(true);
            controBean.setAccountEditable(false);
            controBeans.add(controBean);
        }
        return controBeans;
    }
    /**
     * 功能：对业务规则排序
     */
    public void collectionSort(List<FcVoucherEntryDefineDTO> entrys) {
        Collections.sort(entrys, new Comparator<FcVoucherEntryDefineDTO>() {
            @Override
            public int compare(FcVoucherEntryDefineDTO o1, FcVoucherEntryDefineDTO o2) {
                return o1.getGroupNum().compareTo(o2.getGroupNum());
            }
        });
    }
    /**
     * 发送消息提醒
     */
    private void sendCheckNotificationInfo(VoucherWrapperDTO voucher, ICurrentUser user) {
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
            notificationService.sendIgnoreMySelf(accbook.getId(), accbook.getNetId(), NotificationConstant.FINACIAL.ENTERPRISELOAN_CHECK, notifyVO, user.getId());
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
            notificationService.sendToUser(accbook.getId(), accbook.getOperatorCode(), NotificationConstant.FINACIAL.ENTERPRISELOAN_EDIT, notifyVO);
        }
    }
    /**
     * 消息完成
     */
    private void completeEditNotificationInfo(VoucherWrapperDTO voucher, ICurrentUser user) {
        for (FcAccbookVoucherDTO voucherVO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherVO.getAccbook();
            notificationService.complete(NotificationConstant.FINACIAL.ENTERPRISELOAN_EDIT, accbook.getId(), user.getId());
        }
    }
    /**
     * 消息完成
     */
    private void completeCheckNotificationInfo(VoucherWrapperDTO voucher, ICurrentUser user) {
        for (FcAccbookVoucherDTO voucherVO : voucher.getTarget()) {
            FcAccbookDTO accbook = voucherVO.getAccbook();
            notificationService.complete(NotificationConstant.FINACIAL.ENTERPRISELOAN_CHECK, accbook.getId(), user.getId());
        }
    }
    private void validata(String sendBillCode) {
        if (EmptyUtils.isNotEmpty(sendBillCode)) {
            CmsLoanSendDTO sendInfo = iCmsLoanService.getLoanSendByBillCode(sendBillCode);
            if (sendInfo != null) {
                if ("42".equals(sendInfo.getBizStatus())) {
                    throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_ISPASS);
                }
            }
        }
    }
    private void validataBack(String sendBillCode) {
        if (EmptyUtils.isNotEmpty(sendBillCode)) {
            CmsLoanSendDTO sendInfo = iCmsLoanService.getLoanSendByBillCode(sendBillCode);
            if (sendInfo != null) {
                if (StringUtils.isNotEmpty(sendInfo.getBizStatus()) && "95".equals(sendInfo.getBizStatus())) {
                    throw new ParameterException(CollectMessageEnum.FIN_COLLECT_MANUAL_ISBACK);
                }
            }
        }
    }
    private void setAccountGrant(String accountId, String corpId, List<String> users) {
        users.removeAll(Collections.singleton(null));
        for (String user : users) {
            AccountGrantDTO ag = new AccountGrantDTO();
            ag.setAccountId(accountId);
            ag.setAccountType(1);
            ag.setUserId(user);
            ag.setCorpId(corpId);
            iAccountGrantService.saveAccountGrant(ag);
        }
    }
    @Override
    @XxlJob("EnterpriseLoanService")
    public void run(String arguments) {
        //查询待记账数据
        List<CmsLoanSendDTO> dataList = iCmsLoanService.querySendTallyData();//查询
        if (!EmptyUtils.isEmptyList(dataList)) {
            for (CmsLoanSendDTO sendDTO : dataList) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("money", NumberUtil.formatNumeric(sendDTO.getLoanMoney(), "#,##0.00"));
                notificationService.sendToUserByResPermission(sendDTO.getId(), ResourceConstant.FCL_LOAN_TALLY_APPLY, NotificationConstant.FINACIAL.ENTERPRISELOAN_TALLY, map, sendDTO.getCorpId());
                log.info("您有一笔内部贷款发放凭证待记账，金额：" + sendDTO.getLoanMoney());
                //更改发送标识
                sendDTO.setRemindSign(CommonConstant.SEND_SIGN_YES);
            }
            iCmsLoanService.batchUpdateSend(dataList);
        }
    }
}
