package com.bytter.credential.domain.international.service;

import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.NetbankConstant;
import com.bytter.core.enums.AccebillMessageEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credential.api.constant.*;
import com.bytter.credential.api.enums.LcMessageEnum;
import com.bytter.credential.domain.international.dao.SupplementCreditDao;
import com.bytter.credential.domain.international.entity.LcBillArrival;
import com.bytter.credential.domain.international.entity.LcEnrol;
import com.bytter.credential.domain.international.utils.CreditUtil;
import com.bytter.credential.domain.international.vo.GuaContractInternationalVO;
import com.bytter.credential.domain.international.vo.LcEnrolVO;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.constant.GuaranteeConstant;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2018-12-10 18:32
 * @Version 1.0
 */
@Transactional
@Service("supplementCreditService")
@WorkFlow(value = {LcWorkFlowConstant.LC_SUPPLEMENT_CREDIT})
public class SupplementCreditService extends BaseService<LcEnrol, String> implements IWorkFlowListener {
    @Autowired
    private SupplementCreditDao supplementCreditDao;
    @Autowired
    private LcMessageService lcMessageService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IRatUrationService urationService;
    @Autowired
    private IFcBondHookService bondHookService;
    @DubboReference
    private IGuaContractOccupServie contractOccupServie;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @DubboReference(check = false)
    private IGuaContractOccupServie guaContractOccupServie;
    @Autowired(required = false)
    private CreatedCreditService createdCreditService;
    @DubboReference(check = false)
    private IFbsBudgetProcessService fbsBudgetProcessService;
    /**
     * 更新
     *
     * @param lcEnrol
     * @param user
     */
    public void updateById(LcEnrol lcEnrol, ICurrentUser user) {
        lcEnrol.setUpdateDate(new Date());
        lcEnrol.setUpdateBy(user.getLoginName());
        updateEntity(lcEnrol);
    }
    /**
     * 暂存
     *
     * @param lcEnrolVO
     * @param user
     */
    public void saveTmp(LcEnrolVO lcEnrolVO, ICurrentUser user, String guaContract) {
        LcEnrol lcEnrol = new LcEnrol();
        BeanUtils.copyNotNullProperties(lcEnrolVO, lcEnrol);
        // 授信相关
        lcEnrol.setItemCode(lcEnrolVO.getRatItemCode());
        lcEnrol.setExchangeRate(lcEnrolVO.getRatExchangeRate());
        LcEnrol lcEnrolEntity = get(lcEnrol.getId());
        /*校验信用证号*/
        if (StringUtils.isNotEmpty(lcEnrol.getCreditNo()) && !lcEnrol.getCreditNo().equals(lcEnrolEntity.getCreditNo())) {
            validateCreditNo(lcEnrol.getCreditNo());
        }
        //            授信处理-先删除旧冻结授信
        deleteUseRation(lcEnrolEntity);
        //            调用保证金业务处理
        CreditUtil.handleBond(lcEnrolEntity, lcEnrol, bondHookService, user);
        //            担保挂接业务处理 0暂存，1在审使用，2使用中，3释放,4打回
        CreditUtil.handleGua(lcEnrol, lcEnrolEntity, guaContractOccupServie, 1, guaContract);
        BeanUtils.copyNotNullProperties(lcEnrol, lcEnrolEntity);
        lcEnrolEntity.setCorpId(lcEnrolEntity.getApplicant());
        /*设置补录暂存状态*/
        lcEnrolEntity.setStatus(InternationalConstant.SUPPLEMENTTEMPSTORE);
        lcEnrolEntity.setUpdateBy(user.getLoginName());
        lcEnrolEntity.setUpdateDate(new Date());
        updateEntity(lcEnrolEntity);
        //            授信业务处理--重新占用额度
        handleRatUration(lcEnrolEntity, InternationalConstant.COMMITSUPPLEMENTAPPROVE);
        //写入计划执行
        lcEnrolVO.setId(lcEnrolEntity.getId());
        lcEnrolVO.setLcNo(lcEnrolEntity.getLcNo());
        lcEnrolVO.setCorpId(lcEnrolEntity.getCorpId());
        lcEnrolVO.setStatus(lcEnrolEntity.getStatus());
        createdCreditService.writeBudgetProcess(lcEnrolVO, user);
    }
    /**
     * 查询所有的开证信息（当前用户权限范围内）
     *
     * @param dataTablesInput
     * @param userId
     * @return
     */
    public DataTablesOutput listSupplmentCredit(DataTablesInput dataTablesInput, String userId) {
        List<String> corpIds = corpService.getUserCorpIds(InternationalResourceConstant.INTERNATIONAL_CREDIT_SUPPLEMENTCREDIT_QUERY, userId);
        return supplementCreditDao.listSupplementCredit(dataTablesInput, corpIds);
    }
    /**
     * 查询所有的开证补录审批信息（当前用户权限范围内）
     *
     * @param dataTablesInput
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput listSupplmentCreditApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(LcWorkFlowConstant.LC_SUPPLEMENT_CREDIT, dataTablesInput);
        DataTablesOutput dataTablesOutput = null;
        try {
            dataTablesOutput = supplementCreditDao.listSupplementCreditApprove(dataTablesInput, workFlowBusiness.getBusinessIdList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return workFlowBusiness.merge(dataTablesOutput);
    }
    /**
     * 保存提交
     *
     * @param lcEnrolVO
     * @param user
     */
    public void save(LcEnrolVO lcEnrolVO, ICurrentUser user, String guaContract) {
        LcEnrol lcEnrol = new LcEnrol();
        BeanUtils.copyNotNullProperties(lcEnrolVO, lcEnrol);
        // 授信相关
        lcEnrol.setItemCode(lcEnrolVO.getRatItemCode());
        lcEnrol.setExchangeRate(lcEnrolVO.getRatExchangeRate());
        LcEnrol lcEnrolEntity = get(lcEnrol.getId());
        /*校验信用证号*/
        if (lcEnrolEntity.getCreditNo() != null && !lcEnrolEntity.getCreditNo().equals(lcEnrol.getCreditNo())) {
            validateCreditNo(lcEnrol.getCreditNo());
        }
        //            授信处理-先删除旧冻结授信
        deleteUseRation(lcEnrolEntity);
        //            调用保证金业务处理
        CreditUtil.handleBond(lcEnrolEntity, lcEnrol, bondHookService, user);
        //            担保挂接业务处理 0暂存，1在审使用，2使用中，3释放,4打回
        CreditUtil.handleGua(lcEnrol, lcEnrolEntity, guaContractOccupServie, 1, guaContract);
//        entityPropertiesSet(lcEnrol, lcEnrolEntity);
        BeanUtils.copyNotNullProperties(lcEnrol, lcEnrolEntity);
        lcEnrolEntity.setCorpId(lcEnrolEntity.getApplicant());
        lcEnrolEntity.setStatus(InternationalConstant.COMMITSUPPLEMENTAPPROVE);
        lcEnrolEntity.setUpdateBy(user.getLoginName());
        lcEnrolEntity.setUpdateDate(new Date());
        updateEntity(lcEnrolEntity);
        //            重新挂接授信业务处理
        handleRatUration(lcEnrolEntity, InternationalConstant.COMMITSUPPLEMENTAPPROVE);
        //        打回数据角标处理
        notificationService.complete(NotificationConstant.INTERNATIONAL.INTERNATIONAL_SUPPLEMENT_CREDIT_BACK, lcEnrolEntity.getId(), user.getId());
        //写入计划执行
        lcEnrolVO.setId(lcEnrolEntity.getId());
        lcEnrolVO.setLcNo(lcEnrolEntity.getLcNo());
        lcEnrolVO.setStatus(lcEnrolEntity.getStatus());
        lcEnrolVO.setCorpId(lcEnrolEntity.getCorpId());
        createdCreditService.writeBudgetProcess(lcEnrolVO, user);
        //审批
        if (lcEnrolEntity.getStatus() == 21) {// 提交审批
            try {
                lcMessageService.insertProcessMsg(lcEnrolEntity.getId(), lcEnrolEntity.getLcNo(), LcEnrolConstant.LC_BILL_CODE_PREFIX, LcMsgConstant.MSGA_SUBMIT, "信用证开证补录", user);
                //默认审批通过
                getEntityDao().flush();
                 getStartHandler().handler(LcWorkFlowConstant.LC_SUPPLEMENT_CREDIT, lcEnrolEntity.getId(), lcEnrolEntity, lcEnrolEntity.getCorpId(), user.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            lcMessageService.insertProcessMsg(lcEnrolEntity.getId(), lcEnrolEntity.getLcNo(), LcEnrolConstant.LC_BILL_CODE_PREFIX, LcMsgConstant.MSGA_REPAIR, "信用证开证补录", user);
        }
    }
    @Override
    public BaseDao getEntityDao() {
        return supplementCreditDao;
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        /*判断业务主键是否为空*/
        if (StringUtils.isEmpty(workFlowResult.getBusinessKey())) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        LcEnrol lcEnrol = get(workFlowResult.getBusinessKey());
        int status;
        if (workFlowResult.getProcessDefinitionKey().equals(LcWorkFlowConstant.LC_SUPPLEMENT_CREDIT)) {
            /*请在通过或不通过中调用自己的处理方法*/
            if (workFlowResult.isPass()) {
                /*状态设置*/
                status = InternationalConstant.SUPPLEMENTAPPROVED;
                /*日志插入*/
                handelMsg(lcEnrol, "信用证开证补录审批通过", LcMsgConstant.MSGA_CHECKOK, workFlowResult);
                /*消息发送*/
                sendMsg(lcEnrol, NotificationConstant.INTERNATIONAL.INTERNATIONAL_SUPPLEMENT_CREDIT_PASS);
                //计划执行状态同步更新
                this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_CREDIT, BudgetConstant.PROCESS_SYSNAME_INTERNATIONAL,
                        lcEnrol.getLcNo(), NetbankConstant.STATUE_CHECK, workFlowResult.getUser());
            } else {
                /*状态设置*/
                status = InternationalConstant.SUPPLEMENTBACK;
                /*日志插入*/
                handelMsg(lcEnrol, "信用证开证补录审批打回", LcMsgConstant.MSGA_ROLLBACK, workFlowResult);
                /*消息发送*/
                sendMsg(lcEnrol, NotificationConstant.INTERNATIONAL.INTERNATIONAL_SUPPLEMENT_CREDIT_BACK);
                //计划执行状态同步更新
                this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_CREDIT, BudgetConstant.PROCESS_SYSNAME_INTERNATIONAL,
                        lcEnrol.getLcNo(), NetbankConstant.STATUE_BACK, workFlowResult.getUser());
            }
            lcEnrol.setStatus(status);
            saveOrUpdate(lcEnrol);
            /*审批通过则正式占用*/
            if (InternationalConstant.SUPPLEMENTAPPROVED == lcEnrol.getStatus()) {
                handleRatUration(lcEnrol, InternationalConstant.SUPPLEMENTAPPROVED);
                //        担保业务处理 0暂存，1在审使用，2使用中，3释放,4打回
                CreditUtil.handleGua(lcEnrol, null, guaContractOccupServie, 2, null);
            }
        }
    }
    /**
     * 入库操作
     *
     * @param lcEnrol
     * @param user
     */
    public void storageCredit(LcEnrol lcEnrol, ICurrentUser user) {
        LcEnrol lcEnrolEntity = get(lcEnrol.getId());
        BeanUtils.copyNotNullProperties(lcEnrol, lcEnrolEntity);
        lcEnrolEntity.setStatus(InternationalConstant.STORAGE);
        lcEnrolEntity.setUpdateBy(user.getLoginName());
        lcEnrolEntity.setUpdateDate(new Date());
        updateEntity(lcEnrolEntity);
        /*收证完成后创建到单登记*/
//        createLcBillArrival(lcEnrolEntity, user);
    }
    /**
     * 打回操作
     *
     * @param id
     * @param reason
     * @param user
     */
    public void refuse(String id, String reason, ICurrentUser user) {
        LcEnrol lcEnrol = get(id);
        lcEnrol.setStatus(InternationalConstant.BACK);
        updateEntity(lcEnrol);
        /*日志插入*/
        lcMessageService.insertProcessMsg(lcEnrol.getId(), lcEnrol.getLcNo(), LcEnrolConstant.LC_BILL_CODE_PREFIX, LcMsgConstant.MSGA_ROLLBACK, reason, user);
        /*消息发送*/
        sendMsg(lcEnrol, InternationalResourceConstant.INTERNATIONAL_CREDIT_SUPPLEMENTCREDIT_UPDATE);
        /**
         * 打回
         */
        if (InternationalConstant.ISHANGCREDIT_Y.equals(lcEnrol.getIsHangCredit())) {
            RatUrationDTO ratUrationDTO = new RatUrationDTO();
            CreditUtil.setLCCreditRatUrationDTO(lcEnrol, ratUrationDTO, lcEnrol.getCreditMoney(), "1");
            urationService.backUseRation(ratUrationDTO);
        }
//        担保业务处理 0暂存，1在审使用，2使用中，3释放,4打回
        CreditUtil.handleGua(lcEnrol, null, guaContractOccupServie, 4, null);
//        保证金释放处理,此处不释放保证金，删除时才释放保证金
//        CreditUtil.releaseBond(lcEnrol, bondHookService);
        //计划执行状态同步更新
        this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_CREDIT, BudgetConstant.PROCESS_SYSNAME_INTERNATIONAL,
                lcEnrol.getLcNo(), NetbankConstant.STATUE_BACK, user);
    }
    /**
     * 授信冻结占用(业务数据提交待审批)
     *
     * @param lcEnrol
     */
    public void handleRatUration(LcEnrol lcEnrol, Integer status) {
//        判断是否挂授信
        if (InternationalConstant.ISHANGCREDIT_Y.equals(lcEnrol.getIsHangCredit())) {
            RatUrationDTO ratUrationDTO = new RatUrationDTO();
            CreditUtil.CreateRatUrationDTO(lcEnrol, ratUrationDTO, currencyService.getNatualCurrency());
            if (status == InternationalConstant.SUPPLEMENTTEMPSTORE) {
                urationService.tempUseRation(ratUrationDTO);//暂存调用
            } else if (status == InternationalConstant.COMMITSUPPLEMENTAPPROVE) {
                urationService.addUseRation(ratUrationDTO);//提交调用
            } else if (status == InternationalConstant.SUPPLEMENTAPPROVED) {
                urationService.passUseRation(ratUrationDTO);//审批通过调用
            } else if (status == InternationalConstant.SUPPLEMENTBACK) {
                urationService.backUseRation(ratUrationDTO);//打回调用
            }
        }
    }
    /**
     * 授信冻结占用(业务暂存后删除)
     *
     * @param lcEnrol
     */
    public void deleteUseRation(LcEnrol lcEnrol) {
        //            判断以前是否冻结授信，如果是冻结则删除冻结
        if (InternationalConstant.ISHANGCREDIT_Y.equals(lcEnrol.getIsHangCredit())) {
            RatUrationDTO ratUrationDTO = new RatUrationDTO();
            CreditUtil.CreateRatUrationDTO(lcEnrol, ratUrationDTO, currencyService.getNatualCurrency());
//            urationService.releaseRation(ratUrationDTO);//删除前先释放授信
            urationService.backUseRation(ratUrationDTO);//先还原冻结额度在删除
            urationService.deleteUseRation(ratUrationDTO);//删除授信
        }
    }
    /**
     * 创建到单登记
     *
     * @param lcEnrol
     */
    public void createLcBillArrival(LcEnrol lcEnrol, ICurrentUser user) {
        LcBillArrival lcBillArrival = new LcBillArrival();
        /*到单流水号*/
        lcBillArrival.setArrivalCode(CreditUtil.getArrivalNo());
        /*到单类型 开证为0*/
        lcBillArrival.setArrivalType(0);
        /*到单号*/
        /*lcBillArrival.setArrivalNo("");*/
        /*信用证登记ID*/
        lcBillArrival.setLcId(lcEnrol.getId());
        /*单位编号*/
        lcBillArrival.setCorpId(lcEnrol.getCorpId());
        /*本次来单金额*/
        lcBillArrival.setBillMoney(lcEnrol.getCreditMoney());
        /*本次付款金额*/
        lcBillArrival.setBillMoney(lcEnrol.getCreditMoney());
        /*币别*/
        lcBillArrival.setCurId(lcEnrol.getIssuingCurId());
        /*汇率*/
        lcBillArrival.setExchange(lcEnrol.getExchangeRate());
        /*手续费*/
        lcBillArrival.setHandlingCharge(lcEnrol.getChargeMoney());
        /*到单日期*/
        lcBillArrival.setPaymentDate(new Date());
        /*期限*/
/*        lcBillArrival.setPeriod();
        交单银行
        lcBillArrival.setHandDocBank();
        交单银行编号
        lcBillArrival.setDocBankNo();
        付汇日期
        lcBillArrival.setPayDate();
        增值税款金额
        lcBillArrival.setAddTaxMoney();
        关税款金额
        lcBillArrival.setTaxMoney();
        缴税日期
        lcBillArrival.setTaxDate();
        税款种类
        lcBillArrival.setTaxType();
        核销单号
        lcBillArrival.setVbCode();
        取回日期
        lcBillArrival.setGetDate();*/
        /*远期天数*/
        lcBillArrival.setDelayDates(String.valueOf(lcEnrol.getDelayDates() == null ? "0" : lcEnrol.getDelayDates()));
        /*受益人*/
        lcBillArrival.setBeneficiary(lcEnrol.getBeneficiary());
        /*备注*/
        lcBillArrival.setRmk(lcEnrol.getRmk());
        /*状态*/
        lcBillArrival.setStatus(InternationalConstant.TEMP);
        /*创建人登录名称*/
        lcBillArrival.setCreateBy(user.getLoginName());
        /*创建日期*/
        lcBillArrival.setCreateDate(new Date());
        supplementCreditDao.save(lcBillArrival);
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    /**
     * 审批日志
     *
     * @param lcEnrol
     * @param msg
     * @param workFlowResult
     * @param messageType
     */
    private void handelMsg(LcEnrol lcEnrol, String msg, int messageType, IWorkFlowResult workFlowResult) {
        lcMessageService.insertProcessMsg(lcEnrol.getId(), lcEnrol.getLcNo(), LcEnrolConstant.LC_BILL_CODE_PREFIX, messageType, msg, workFlowResult.getUser());
    }
    /**
     * 审批消息推送通知
     *
     * @param lcEnrol
     * @param tag
     */
    private void sendMsg(LcEnrol lcEnrol, String tag) {
        notificationService.send(lcEnrol.getId(), lcEnrol.getCorpId(), tag, lcEnrol);
    }
    /**
     * 将lcEnrol部分属性设置到lcEnrolEntity中
     *
     * @param lcEnrol
     * @param lcEnrolEntity
     */
    public void entityPropertiesSet(LcEnrol lcEnrol, LcEnrol lcEnrolEntity) {
        /*信用证类型*/
//        lcEnrolEntity.setLcInter(lcEnrol.getLcInter());
        /*开证人*/
        lcEnrolEntity.setApplicant(lcEnrol.getApplicant());
        /*设置单位为开证人*/
        lcEnrolEntity.setCorpId(lcEnrol.getApplicant());
        /*信用证号*/
        lcEnrolEntity.setCreditNo(lcEnrol.getCreditNo());
        /*是否代开信用证*/
        lcEnrolEntity.setIsReCredit(lcEnrol.getIsReCredit());
        /*被代开单位*/
        lcEnrolEntity.setReCorpCode(lcEnrol.getReCorpCode());
        /*代开信用证号*/
        lcEnrolEntity.setReCreditNo(lcEnrol.getReCreditNo());
        /*合同号*/
        lcEnrolEntity.setContractNo(lcEnrol.getContractNo());
        /*受益人*/
        lcEnrolEntity.setBeneficiary(lcEnrol.getBeneficiary());
        /*开证日期*/
        lcEnrolEntity.setIssuingDate(lcEnrol.getIssuingDate());
        /*信用证有效期*/
        lcEnrolEntity.setExpiryDate(lcEnrol.getExpiryDate());
        /*开证人账号*/
        lcEnrolEntity.setApplicantAcc(lcEnrol.getApplicantAcc());
        /*开证人账号id*/
        lcEnrolEntity.setApplicantAccId(lcEnrol.getApplicantAccId());
        /*受益人账号*/
        lcEnrolEntity.setBeneficiaryAcc(lcEnrol.getBeneficiaryAcc());
        /*受益人开证行*/
        lcEnrolEntity.setBeneficiaryBank(lcEnrol.getBeneficiaryBank());
        /*开证人地址*/
        lcEnrolEntity.setAddr(lcEnrol.getAddr());
        /*受益人地址*/
        lcEnrolEntity.setAddrBen(lcEnrol.getAddrBen());
        /*开证人邮编*/
        lcEnrolEntity.setPostNum(lcEnrol.getPostNum());
        /*受益人邮编*/
        lcEnrolEntity.setPostNumBen(lcEnrol.getPostNumBen());
        /*期限*/
        lcEnrolEntity.setTimeLimit(lcEnrol.getTimeLimit());
        /*是否需要许可证*/
        lcEnrolEntity.setNeedLicence(lcEnrol.getNeedLicence());
        /*许证号码*/
        lcEnrolEntity.setLicenceCode(lcEnrol.getLicenceCode());
        /*开证银行*/
        lcEnrolEntity.setIssuingBank(lcEnrol.getIssuingBank());
        /*付手续费账号*/
        lcEnrolEntity.setPayChargeAacc(lcEnrol.getPayChargeAacc());
        /*代开证银行*/
        lcEnrolEntity.setReBank(lcEnrol.getReBank());
        /*汇率*/
        lcEnrolEntity.setExchangeRate(lcEnrol.getExchangeRate());
        /*开证币种*/
        lcEnrolEntity.setIssuingCurId(lcEnrol.getIssuingCurId());
        /*开证金额*/
        lcEnrolEntity.setCreditMoney(lcEnrol.getCreditMoney());
        /*手续费币别*/
        lcEnrolEntity.setChargeCurId(lcEnrol.getChargeCurId());
        /*手续费*/
        lcEnrolEntity.setChargeMoney(lcEnrol.getChargeMoney());
        /*远期天数*/
        lcEnrolEntity.setDelayDates(lcEnrol.getDelayDates());
        /*承兑金额*/
        lcEnrolEntity.setAcceptanceMoney(lcEnrol.getAcceptanceMoney());
        /*承兑费用*/
        lcEnrolEntity.setAcceptanceFee(lcEnrol.getAcceptanceFee());
        /*其他费用*/
        lcEnrolEntity.setOtherFee(lcEnrol.getOtherFee());
        /*贴现费率*/
        lcEnrolEntity.setDiscountRate(lcEnrol.getDiscountRate());
        /*贴现息*/
        lcEnrolEntity.setDiscountInterest(lcEnrol.getDiscountInterest());
        /*贸易亏损*/
        lcEnrolEntity.setTradeDeficit(lcEnrol.getTradeDeficit());
        /*综合成本*/
        lcEnrolEntity.setCompCost(lcEnrol.getCompCost());
        /*应回款金额*/
        lcEnrolEntity.setRebackMoney(lcEnrol.getRebackMoney());
        /*敞口金额*/
        lcEnrolEntity.setOpenMoney(lcEnrol.getOpenMoney());
        /*用途*/
        lcEnrolEntity.setPurpose(lcEnrol.getPurpose());
        /*备注*/
        lcEnrolEntity.setRmk(lcEnrol.getRmk());
        /**
         * 授信部分字段
         */
        /*是否挂授信*/
        lcEnrolEntity.setIsHangCredit(lcEnrol.getIsHangCredit());
        /*授信模式*/
        lcEnrolEntity.setRatmodel(lcEnrol.getRatmodel());
        /*额度编码*/
        lcEnrolEntity.setRationCode(lcEnrol.getRationCode());
        /*占用授信代码*/
        lcEnrolEntity.setItemCode(lcEnrol.getItemCode());
        /*授信额度*/
        lcEnrolEntity.setItemMoney(lcEnrol.getItemMoney());
//        是否折算本位币
        lcEnrolEntity.setIsNativeCur(lcEnrol.getIsNativeCur());
//        折算本位币后金额
        lcEnrolEntity.setNativeCreditMoney(lcEnrol.getNativeCreditMoney());
        lcEnrolEntity.setIsOpenRat(lcEnrol.getIsOpenRat());
        lcEnrolEntity.setBillName(lcEnrol.getBillName());
        /**
         * 保证金部分字段
         */
//        保证金占比
        lcEnrolEntity.setBondPercent(lcEnrol.getBondPercent());
//        保证金金额
        lcEnrolEntity.setBondMoney(lcEnrol.getBondMoney());
//        保证金合同号
        lcEnrolEntity.setBondContract(lcEnrol.getBondContract());
//        是否挂保证金
        lcEnrolEntity.setIsHangBond(lcEnrol.getIsHangBond());
//        保证金id
        lcEnrolEntity.setBondId(lcEnrol.getBondId());
//        保证金账户
        lcEnrolEntity.setBondBankAcc(lcEnrol.getBondBankAcc());
//        保证金币别
        lcEnrolEntity.setBondCurId(lcEnrol.getBondCurId());
//        是否溢短装
        lcEnrolEntity.setIsSpillOver(lcEnrol.getIsSpillOver());
//        溢短装比例
        lcEnrolEntity.setSpillOverRate(lcEnrol.getSpillOverRate());
    }
    private void validateCreditNo(String creditNo) {
        Map<String, Object> params = new HashedMap();
        params.put("creditNo", creditNo);
        if (supplementCreditDao.getcreditNoCount(params) > 0) {
            throw new ParameterException(LcMessageEnum.LC_CREATE_CREDIT_NO);
        }
    }
    /**
     * 获取国际结算挂接合同数据
     *
     * @param input 查询参数
     * @param user  用户信息
     * @return 列表数据
     */
    public DataTablesOutput<GuaContractInternationalVO> getGuaForInternational(DataTablesInput input, ICurrentUser user) {
        String sysCode = input.getFormdata().get("eqs_sysCode").toString();
        String startDate = input.getFormdata().get("ged_useDate").toString();
        String endDate = input.getFormdata().get("led_useDate").toString();
        String status = input.getFormdata().get("eqg_status").toString();
        String contractId = input.getFormdata().get("eqs_contractId") == null ? null : input.getFormdata().get("eqs_contractId").toString();
        input.setFormdata(null);
        //获取挂接授信的业务id集合和封装参数
        List<GuaContractOccupDTO> guaList = contractOccupServie.getGua(sysCode, startDate, endDate, status,contractId);
        List<String> corpIds = corpService.getUserCorpIds(GuaranteeConstant.CMS_GUARANTEE_CHANGE_QUERY, user.getId());
        if (EmptyUtils.isEmpty(guaList)) {
            return new DataTablesOutput<>();
        }
        Map<String, GuaContractOccupDTO> guaMap = new HashMap<>(guaList.size());
        List<String> businessIdList = new ArrayList<>();
        guaList.forEach(item -> {
            guaMap.put(item.getBusinessId(), item);
            businessIdList.add(item.getBusinessId());
        });
        //组装参数
        DataTablesOutput<GuaContractInternationalVO> dataTablesOutput = supplementCreditDao.getGuaForInternational(input, corpIds, businessIdList);
        Map<String, CorpDTO> corpMap=corpService.getCorpMap();
        if (EmptyUtils.isNotEmpty(dataTablesOutput.getData())) {
            for (GuaContractInternationalVO item : dataTablesOutput.getData()) {
                if (guaMap.containsKey(item.getId())) {
                    item.setStatus(guaMap.get(item.getId()).getStatus());
                    item.setUseDate(guaMap.get(item.getId()).getUseDate());
                    item.setApplicantName(corpMap.get(item.getApplicant()).getName());
                }
            }
        }
        return dataTablesOutput;
    }
}
