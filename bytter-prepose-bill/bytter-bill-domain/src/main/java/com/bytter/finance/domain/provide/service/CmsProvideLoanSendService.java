package com.bytter.finance.domain.provide.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.BusOriType;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.SysRaceCode;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.constant.InterestConstant;
import com.bytter.biz.finacial.api.dto.*;
import com.bytter.biz.finacial.api.enums.accbook.AccbookMessageEnum;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.finance.constant.*;
import com.bytter.finance.domain.basedata.dao.RepaymentScheduleDao;
import com.bytter.finance.domain.basedata.entity.CmsRepaymentSchedule;
import com.bytter.finance.domain.basedata.entity.RepayType;
import com.bytter.finance.domain.basedata.entity.RepaymentSchedule;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.basedata.service.CmsRepaymentScheduleService;
import com.bytter.finance.domain.basedata.service.RepayTypeService;
import com.bytter.finance.domain.basedata.vo.CmsProvideLoanSendCheckVO;
import com.bytter.finance.domain.basedata.vo.RepaymentScheduleVO;
import com.bytter.finance.domain.precative.service.CmsPrecativeLoanSendService;
import com.bytter.finance.domain.provide.dao.CmsProvideLoanSendDao;
import com.bytter.finance.domain.provide.dao.ProvideSyndicatedSendDao;
import com.bytter.finance.domain.provide.entity.CmsLoanInfoViewPageVO;
import com.bytter.finance.domain.provide.entity.CmsProvideLoanInfo;
import com.bytter.finance.domain.provide.entity.ProvideLoanSend;
import com.bytter.finance.domain.provide.entity.ProvideSyndicatedSend;
import com.bytter.finance.domain.provide.utils.ProvideUtils;
import com.bytter.finance.domain.provide.vo.LoanInfoVO;
import com.bytter.finance.domain.provide.vo.LoanSendVO;
import com.bytter.finance.dto.CmsProvideLoanRepayDTO;
import com.bytter.finance.dto.CmsProvideSendDTO;
import com.bytter.finance.dto.FinanceLoanDTO;
import com.bytter.finance.dto.FinanceTypeLoanDTO;
import com.bytter.finance.enums.PrecativeMessageEnum;
import com.bytter.finance.enums.ProvideMessageEnum;
import com.bytter.guarantee.api.IConService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaPledgeOccupyService;
import com.bytter.guarantee.dto.ConInfoRecordDTO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.*;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.system.api.dto.*;
import com.bytter.workflow.api.IWorkFlowService;
import com.google.common.collect.Lists;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 银行借款发放表
 * @date 2017-08-03 16:40:48
 */
@Service("cmsProvideLoanSendService")
@Transactional
@WorkFlow(value = { CmsWorkFlowConstant.CMS_PROVIDE_LOAN_SEND })
@DubboService(interfaceClass = IWorkFlowListener.class, group = CmsWorkFlowConstant.CMS_PROVIDE_LOAN_SEND)
public class CmsProvideLoanSendService extends BaseService<ProvideLoanSend, String>
        implements IWorkFlowListener, ITaskService, ICorpDisableVerifyService {
    @Autowired
    private CmsProvideLoanSendDao cmsProvideLoanSendDao;
    @Autowired
    private LoanInfoService loanInfoService;
    @Autowired
    private RepaymentScheduleDao repaymentScheduleDao;
    @Autowired
    private CmsRepaymentScheduleService cmsRepaymentScheduleService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private IInnerAccountService innerAccountService;
    @DubboReference(check = false)
    private IConfigService configService;
    @Autowired
    private CmsPrecativeLoanSendService cmsPrecativeLoanSendService;
    @Autowired
    private CmsProvideInterestService cmsProvideInterestService;
    @DubboReference(check = false)
    private ICorpService corpServiceImpl;
    @DubboReference(check = false)
    private IFbsBudgetProcessService fbsBudgetProcessService;
    @Autowired
    private CmsProcessMsgService msService;
    @DubboReference(check = false)
    private IFcSubjectService fcSubjectService;
    @Autowired
    private ProvideLoanDeferredService provideLoanDeferredService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IFcDayCloseService fcDayCloseService;
    @Autowired
    private IRatUrationService urationService;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie guaContractOccupServie;
    @Resource
    private IConService conCommonService;
    @DubboReference(check = false)
    private IUserService userService;
    @DubboReference(check = false)
    private IAccountGrantService iAccountGrantService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @Resource(name = "guaPledgeOccupyServiceImpl")
    private IGuaPledgeOccupyService pledgeOccupyService;
    @Autowired
    private ProvideSyndicatedSendDao provideSyndicatedSendDao;
    @Autowired
    private RepayTypeService repayTypeService;
    @DubboReference(check = false)
    private IBaseRateService iBaseRateService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private IBtBankService btBankService;
    @DubboReference(check = false)
    private IBtBankAccService iBtBankAccService;
    @DubboReference(check = false)
    private IFcSubjectService iFcSubjectService;

    /**
     * 功能介绍 : 查询借款发放列表<br>
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataSendTables(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_PROVIDE_SEND_QUERY, user.getId());
        DataTablesOutput dataSendTables = cmsProvideLoanSendDao.getDataSendTables(dataTablesInput, corpAuth);
        addDictValue(dataSendTables.getData());
        return dataSendTables;
    }

    //todo 字典数据填充
    private void addDictValue(List<LoanSendVO> sendVOS) {
        if (CollectionUtil.isEmpty(sendVOS)) {
            return;
        }
        Map<String, Map<String, DictDTO>> dictMap = dictService.listDictMap("cms.loan.loanSendOutType","Cms.FloatWay", "Cms.PointsProportion");
        for (LoanSendVO sendVO : sendVOS) {
            if (StringUtils.isNotEmpty(sendVO.getFloatWay())) {
                sendVO.setFloatWayName(dictMap.get("Cms.FloatWay").get(sendVO.getFloatWay()).getName());
            }
            if (StringUtils.isNotEmpty(sendVO.getPointsProportionSelect())) {
                sendVO.setPointsProportionSelectName(dictMap.get("Cms.PointsProportion").get(sendVO.getPointsProportionSelect()).getName());
            }
            if (StringUtils.isNotEmpty(sendVO.getLoanSendOutType())) {
                sendVO.setLoanSendOutTypeName(dictMap.get("cms.loan.loanSendOutType").get(sendVO.getLoanSendOutType()).getName());
            }
        }

    }
    /**
     * 功能介绍 : 查询借款发放审批<br>
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataSendAppvoerTables(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService
                .getWorkFlowBusiness(CmsWorkFlowConstant.CMS_PROVIDE_LOAN_SEND, dataTablesInput);
        DataTablesOutput dp = cmsProvideLoanSendDao.getDataSendAppvoerTables(dataTablesInput,
                workFlowBusiness.getBusinessIdList());
        addDictValue(dp.getData());
        return workFlowBusiness.merge(dp);
    }
    /**
     * 功能介绍 : 根据借款单号查询发放信息<br>
     *
     * @param sendBillCode
     *            借款信息表billCode
     */
    @Transactional(readOnly = true)
    public ProvideLoanSend getSendByBillCode(String sendBillCode, String billCode) {
        ProvideLoanSend send = null;
        if (EmptyUtils.isEmpty(sendBillCode) || "null".equals(sendBillCode.trim())) {
            send = new ProvideLoanSend();
        } else {
            send = cmsProvideLoanSendDao.getSendBySendBillCode(sendBillCode);
        }
        if (EmptyUtils.isEmpty(send.getId())) {
            Integer count = cmsProvideLoanSendDao.getSendCountByBillCode(billCode);
            send.setSendTime(count + 1);// 新增 显示发放次数
            LoanInfoVO loanInfoVO = loanInfoService.getLoanInfoVOByBillCode(billCode);
            if (CmsConstant.LOAN_SEND_OUT_TYPE_ONE.equals(loanInfoVO.getLoanSendOutType())) {// 一次性发放
                send.setMoney(loanInfoVO.getPlMoney());
                send.setRepayDate(loanInfoVO.getRepayDate());
            }
            send.setCreateDate(new Date());
            send.setSendDate(loanInfoVO.getPlDate());
            send.setCorpId(loanInfoVO.getCorpId());
            send.setLoanCode(loanInfoVO.getBillCode());
            send.setRepayType(loanInfoVO.getRepayType());
            send.setAccrualType(loanInfoVO.getAccrualType());
            send.setCurCode(loanInfoVO.getCurId());
            send.setInterestWay(loanInfoVO.getInterestWay());
            send.setFloatWay(loanInfoVO.getFloatWay());
            send.setRateEntriesEffectDate(loanInfoVO.getRateEntriesEffectDate());
            send.setReferenceRateType(loanInfoVO.getReferenceRateType());
            send.setRepayCycle(loanInfoVO.getRepayCycle());
            send.setBenchmarkRate(loanInfoVO.getBenchmarkRate());
            send.setPointsProportionSelect(loanInfoVO.getPointsProportionSelect());
            send.setPointsProportionText(loanInfoVO.getPointsProportionText());
            send.setPlRate(loanInfoVO.getPlRate());
            send.setOverdueRate(loanInfoVO.getOverdueRate());
            send.setDeferredRate(loanInfoVO.getDeferredRate());
            send.setFloatWayNext(loanInfoVO.getFloatWayNext());
            send.setDeferredPoints(loanInfoVO.getDeferredPoints());
            send.setOverdueProportion(loanInfoVO.getOverdueProportion());
            send.setOnlinePaymentAcc(loanInfoVO.getOnlinePaymentAcc());
            send.setOnlineAccName(loanInfoVO.getOnlineAccName());
            send.setOnlinePaymentAccId(loanInfoVO.getOnlinePaymentAccId());
            send.setOnlineBankName(loanInfoVO.getOnlineBankName());
            send.setPlTerm(loanInfoVO.getPlTerm() == null ? BigDecimal.ZERO : new BigDecimal(loanInfoVO.getPlTerm()));
        }
        return send;
    }
    /**
     * 功能介绍 : 根据发放单号查询发放信息<br>
     */
    @Transactional(readOnly = true)
    public ProvideLoanSend getSendBySendBillCode(String sendBillCode) {
        return cmsProvideLoanSendDao.getSendBySendBillCode(sendBillCode);
    }
    /**
     * 功能介绍 : 根据发放单号查询发放VO<br>
     */
    @Transactional(readOnly = true)
    public LoanSendVO getLoanSendVOBySendBillCode(String sendBillCode) {
        LoanSendVO loanSendVO = cmsProvideLoanSendDao.getLoanSendVOBySendBillCode(sendBillCode);
        //TODO 利率统一处理
        Optional.ofNullable(loanSendVO).ifPresent(loanSendDO -> {
            if (StringUtils.isNotEmpty(loanSendDO.getReferenceRateType())){
                TbBaseRateDTO baseRateDTO = iBaseRateService.getBaseRateById(loanSendDO.getReferenceRateType());
                Optional.ofNullable(baseRateDTO).ifPresent(rate -> {
                    loanSendVO.setReferenceRateTypeName(rate.getBaseTypeName());
                    loanSendVO.setIsBankBaseRate(rate.getIsBankBaseRate());
                });
            }
        });
        addDictValue(Lists.newArrayList(loanSendVO));
        return loanSendVO;
    }

    /**
     * 设置银团借款发放信息
     */
    public void setSyndicatedSend(String plClass, String sendId, Model model) {
        if (BaseDataConstant.SYNDICATED.equals(plClass)) {
            model.addAttribute("syndicatedSend", provideSyndicatedSendDao.getSendByProvideLoanSendId(sendId));
        }
    }

    public void setSyndicatedSend(String plClass, String sendId, Map<String,Object> model) {
        if (BaseDataConstant.SYNDICATED.equals(plClass)) {
            model.put("syndicatedSend", provideSyndicatedSendDao.getSendByProvideLoanSendId(sendId));
        }
    }

    public void setSyndicatedSend(String plClass, String sendId, CmsLoanInfoViewPageVO pageVO) {
        if (BaseDataConstant.SYNDICATED.equals(plClass)) {
            pageVO.setSyndicatedSend(provideSyndicatedSendDao.getSendByProvideLoanSendId(sendId));
        }
    }
    /**
     * 功能介绍 : 查询发放金额<br>
     *
     * @param billCode
     *            借款信息表BillCode
     */
    @Transactional(readOnly = true)
    public BigDecimal getSendMoneyByBillCode(String billCode) {
        return cmsProvideLoanSendDao.getSendMoneyByBillCode(billCode);
    }
    @Transactional(readOnly = true)
    public List<ProvideLoanSend> listSendByStatus(TbRateMaintenceDTO maintence) {
        return cmsProvideLoanSendDao.listSendByStatus(maintence);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void updateSend(ProvideLoanSend entity) {
        super.updateEntity(entity);
    }
    /**
     * 功能介绍 : 暂存或提交<br>
     */
    public void tempOrSubmit(LoanSendVO vo, String saveType, ICurrentUser user) {
        ProvideLoanSend send = new ProvideLoanSend();
        BeanUtils.copyNotNullProperties(vo, send);
        validata(send);
        if ("1".equals(saveType)) {// 提交
            send.setStatus(CmsConstant.COMMITAPPROVE);
            send.setBizStatus(CmsConstant.BUSINESSSTATE_32);
        } else {// 暂存
            send.setStatus(CmsConstant.TEMP);
            send.setBizStatus(CmsConstant.BUSINESSSTATE_30);
        }
        CmsProvideLoanInfo info = loanInfoService.getLoanInfoByBillCode(send.getLoanCode());
        ProvideLoanSend loanSend = null;
        if (EmptyUtils.isEmpty(send.getId())) {
            loanSend = send;
            loanSend.setBillCode(getLoanSendBillCode());
            loanSend.setCreateDate(DateUtils.getDate());
            loanSend.setCreateBy(user.getLoginName());
            loanSend.setBankName(info.getBankName());
            loanSend.setBankTypeName(info.getBankTypeName());
            loanSend.setBankCode(info.getBankCode());
            loanSend.setRepayCycleName(info.getRepayCycleName());
            loanSend.setCurName(info.getCurName());
            loanSend.setCorpName(info.getCorpName());
            loanSend.setCorpCode(info.getCorpCode());

            this.save(loanSend, user);
            notificationService.complete(NotificationConstant.CMS.PROVIDE_SEND, info.getId(), user.getId());
        } else {
            loanSend = this.get(send.getId());
            BeanUtils.copyNotNullProperties(send, loanSend);
            this.saveOrUpdate(loanSend);// saveOrUpdate无法新增
            notificationService.complete(NotificationConstant.CMS.PROVIDE_SEND, loanSend.getId(), user.getId());
        }
        if (!loanSend.getRepayType().equals(info.getRepayType())) {
            info.setRepayType(loanSend.getRepayType());
            loanInfoService.saveOrUpdate(info);
        }
        // todo:资金预算
        if (this.isBudget(send.getCorpId(), BaseDataConstant.IS_BORROW_BUDGET,
                BaseDataConstant.PLOAN_SEND_HANG_BUDGET)) {
            // 占用资金计划
            this.batchEngross(send, vo, user, saveType);
        }
        RepayType repayType = repayTypeService.get(send.getRepayType());
        if (EmptyUtils.isEmpty(vo.getPlanId())) {// 还款计划
            // 利息本金均为随借随还
            if (!BaseDataConstant.FREQUENCY_TYPE_N.equals(repayType.getInterestFrequencyType())
                    && !BaseDataConstant.FREQUENCY_TYPE_N.equals(repayType.getCapitalFrequencyType())) {
                throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANSEND_REPAYMENTSCHEDULEMPTY);
            }
        } else if ("1".equals(vo.getPlanStatus())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANSEND_REPAYMENTSCHEDULRESAVE);
        } else {
            repaymentScheduleDao.updateRepayMentBill(vo.getPlanId(), ProvideConstant.SEND_BILL_CODE_PREFIX,
                    send.getId());// 更新还款计划的编号和类型
        }
        // 当借款种类为“银团贷款”时，保存银团明细信息
        if (BaseDataConstant.SYNDICATED.equals(info.getPlClass())) {
            if (vo.getSyndicatedSendList().size() > 0) {
                Map<String, Object> param = new HashMap<>();
                param.put("id", loanSend.getId());
                provideSyndicatedSendDao.deleteLoanSyndicated(param);
                List<ProvideSyndicatedSend> syndicatedList = new ArrayList<>();
                for (ProvideSyndicatedSend syndicated : vo.getSyndicatedSendList()) {
                    // 通过银行开户行号筛选保存的数据
                    if (!StringUtils.isEmpty(syndicated.getBankCode())) {
                        syndicated.setDirectFinanceId(info.getId());
                        syndicated.setProvideLoanSendId(loanSend.getId());
                        syndicatedList.add(syndicated);
                    }
                }
                provideSyndicatedSendDao.batchSave(syndicatedList);
            }
        }
        if ("1".equals(saveType)) {// 提交
            msService.insertProcessMsg(loanSend.getBillCode(), ProvideConstant.SEND_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_SEND_SUBMIT, user);
             getStartHandler().handler(CmsWorkFlowConstant.CMS_PROVIDE_LOAN_SEND, loanSend.getId(), loanSend,
                    loanSend.getCorpId(), user.getId());
        } else {
            msService.insertProcessMsg(loanSend.getBillCode(), ProvideConstant.SEND_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_INPUT, CmsProcessMsgConstant.MSGA_SEND_TEMP, user);
        }
        logger.debug("tempOrSubmit tempOrSubmit loanSend=" + JsonUtils.toJSONString(loanSend));
    }
    /**
     * 功能介绍 : 发放完成 修改info表状态<br>
     *
     * @param billCode
     *            借款信息表BillCode <b>日期：</b>2017-8-7<br>
     * @version 1.0
     * @author lvwl
     */
    public void sendComplete(String billCode) {
        cmsProvideLoanSendDao.flush();
        BigDecimal sendSumMoney = getSendMoneyByBillCode(billCode);
        CmsProvideLoanInfo info = loanInfoService.getLoanInfoByBillCode(billCode);
        if (info.getPlMoney().compareTo(sendSumMoney) == 0) {// 发放金额等于借款合同金额
            info.setStatus(CmsConstant.COMPELETOTAL);
            info.setBizStatus(CmsConstant.BUSINESSSTATE_42);
            loanInfoService.saveOrUpdate(info);
        }
    }
    public void createSendByInfo(CmsProvideLoanInfo loan, ICurrentUser user) {
        // 一次性发放 并且 跳过发放
        if (CmsConstant.LOAN_SEND_OUT_TYPE_ONE.equals(loan.getLoanSendOutType())
                && CommonConstant.YES.equals(loan.getAcrossSend())) {
            // 创建发放信息
            ProvideLoanSend send = new ProvideLoanSend();
            send.setMoney(loan.getPlMoney());
            send.setSendDate(loan.getPlDate());// 发放日期
            send.setRepayDate(loan.getRepayDate());// 到期日期
            send.setCorpId(loan.getCorpId());
            send.setLoanCode(loan.getBillCode());
            send.setRepayType(loan.getRepayType());// 设置还款方式
            send.setAccrualType(loan.getAccrualType());// 设置预提方式
            send.setCurCode(loan.getCurId());
            send.setInterestWay(loan.getInterestWay());
            send.setFloatWay(loan.getFloatWay());
            send.setFloatWayNext(loan.getFloatWayNext());// 二级浮动方式
            send.setRateEntriesEffectDate(loan.getRateEntriesEffectDate());
            send.setReferenceRateType(loan.getReferenceRateType());
            send.setRepayCycle(loan.getRepayCycle());
            send.setPointsProportionSelect(loan.getPointsProportionSelect());
            send.setPointsProportionText(loan.getPointsProportionText());
            send.setBenchmarkRate(loan.getBenchmarkRate());// 基准利率
            send.setPlRate(loan.getPlRate());
            send.setOverdueRate(loan.getOverdueRate());
            send.setDeferredRate(loan.getDeferredRate());
            send.setStatus(CmsConstant.COMPELETOTAL);
            send.setBizStatus(CmsConstant.BUSINESSSTATE_42);
            send.setBillCode(getLoanSendBillCode());
            send.setFiscalQuarter(loan.getFiscalQuarter());
            send.setExpiryDate(loan.getExpiryDate());// 计息日
            send.setAccruedDailyDate(loan.getAccruedDailyDate());// 预提日
            send.setBankName(loan.getBankName());// 银行名称
            send.setBankTypeName(loan.getBankTypeName());// 银行类别名称
            send.setBankCode(loan.getBankCode());// 联行号
            send.setCurName(loan.getCurName());// 币别名称
            send.setCorpName(loan.getCorpName());// 单位名称
            send.setCorpCode(loan.getCorpCode());// 单位编码
            send.setRepayCycleName(loan.getRepayCycleName());// 贷款期限名称
            send.setSendTime(1);
            send.setOnlinePaymentAcc(loan.getOnlinePaymentAcc());
            send.setOnlineAccName(loan.getOnlineAccName());
            send.setOnlineBankName(loan.getOnlineBankName());
            send.setOnlinePaymentAccId(loan.getOnlinePaymentAccId());
            // 补录中新增 季度 与预算
            send.setPlTerm(new BigDecimal(loan.getPlTerm() == null ? 0 : loan.getPlTerm()));
            send.setCreateDate(new Date());
            ConfigDTO config = configService.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS,
                    BaseDataConstant.IS_BORROW_TALLY);
            String value = config.getValue();
            CorpDTO corp = corpServiceImpl.get(send.getCorpId());
            if (CmsConstant.IS_TRUE.equals(value) && CorpConstant.CORP_TYPE_CODE_NET.equals(corp.getType())) {// 是否记账
                if (isAutoAccounting()) {// 是否自动记账
                    this.submit(send, user);
                    send.setStatus(CmsConstant.COMPELETOTAL);
                    send.setBizStatus(CmsConstant.BUSINESSSTATE_42);
                    // 设置info表状态
                    loan.setStatus(CmsConstant.COMPELETOTAL);
                    loan.setBizStatus(CmsConstant.BUSINESSSTATE_42);
                } else {
                    send.setStatus(CmsConstant.WAITTOTAL);
                    send.setBizStatus(CmsConstant.BUSINESSSTATE_38);// TODO
                }
            } else {
                // 设置info表状态
                loan.setStatus(CmsConstant.COMPELETOTAL);
                loan.setBizStatus(CmsConstant.BUSINESSSTATE_42);
            }
            this.save(send, user);
            if (CmsConstant.IS_TRUE.equals(value) && CorpConstant.CORP_TYPE_CODE_NET.equals(corp.getType())) {// 是否记账
                if (isAutoAccounting()) {// 是否自动记账
                    UserDTO userDTO = userService.getByLoginName(loan.getCreateBy());
                    handleContract(loan.getConId(), send, userDTO);// 借款补录跳过发放，自动记账时生成履约合同信息
                } // 手工记账时在记账完成后生成履约信息
            } else {
                UserDTO userDTO = userService.getByLoginName(loan.getCreateBy());
                handleContract(loan.getConId(), send, userDTO);// 借款补录跳过发放，不记账时生成履约合同信息
            }
            // 设置info表状态
            // loan.setStatus(CmsConstant.COMPELETOTAL);
            // loan.setBizStatus(CmsConstant.BUSINESSSTATE_42);
            msService.insertProcessMsg(loan.getBillCode(), ProvideConstant.INFO_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_SEND_JUMP, user);
            // 复制预算信息
            List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();
            List<FbsBudgetProcessDTO> historyList = fbsBudgetProcessService.getBudgetProcessByBillNo(
                    BudgetConstant.BUSINESS_NAME_PROVIDE_SEND, BudgetConstant.PROCESS_SYSNAME_CMS, loan.getBillCode());
            for (FbsBudgetProcessDTO dto : historyList) {
                FbsBudgetProcessDTO fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
                BeanUtils.copyNotNullProperties(dto, fbsBudgetProcessDTO);
                fbsBudgetProcessDTO.setId(null);
                fbsBudgetProcessDTO.setStatus(BudgetConstant.STATUE_CHECK);// 状态:11在途，95占用
                fbsBudgetProcessDTO.setBillNo(send.getBillCode());// 业务单据号
                list.add(fbsBudgetProcessDTO);
            }
            // 修改时，先删除之前的计划执行数据再保存
            this.fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_PROVIDE_SEND,
                    BudgetConstant.PROCESS_SYSNAME_CMS, loan.getBillCode());
            this.fbsBudgetProcessService.writeBudgetProcess(list, user);
            // 复制还款计划
            CmsRepaymentSchedule infoCmsRepay = repaymentScheduleDao.getCmsRepaymentScheduleByBillCode(loan.getId());
            if (infoCmsRepay != null) {
                CmsRepaymentSchedule sendCmsRepay = new CmsRepaymentSchedule();
                BeanUtils.copyNotNullProperties(infoCmsRepay, sendCmsRepay);
                sendCmsRepay.setBillNo(send.getId());
                sendCmsRepay.setCmsType(ProvideConstant.SEND_BILL_CODE_PREFIX);
                sendCmsRepay.setId(null);
                cmsRepaymentScheduleService.saveEntity(sendCmsRepay);// 复制还款计划主体表
                List<RepaymentScheduleVO> infoRepay = repaymentScheduleDao.getRepayMentScheduleByBillCode(loan.getId());
                List<RepaymentSchedule> sendRepay = BeanUtils.copyListProperties(infoRepay, RepaymentSchedule.class);
                for (RepaymentSchedule rs : sendRepay) {
                    rs.setBillCode(send.getId());
                    rs.setInternalSign(ProvideConstant.SEND_BILL_CODE_PREFIX);
                    rs.setCreateName(user.getUsername());
                    rs.setCreateBy(user.getLoginName());
                    rs.setCreateDate(new Date());
                    rs.setId(null);
                    rs.setRelationId(sendCmsRepay.getId());
                }
                repaymentScheduleDao.batchSave(sendRepay);
            }
            // 根据还款计划和计息方式生成利息明细数据
            cmsProvideInterestService.adjustProvideInterest(send, user);
        } else {
            ProvideLoanSend send = new ProvideLoanSend();
            send.setMoney(loan.getPlMoney());
            notificationService.send(loan.getId(), loan.getCorpId(), NotificationConstant.CMS.PROVIDE_SEND, send);// 添加发放申请的消息提示
        }
    }
    /**
     * 计算期限
     */
    public BigDecimal countPlTerm(Date start, Date end, String unit) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(start);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(end);
        int cou = 0;
        if (CmsConstant.CMS_LOAN_TERN_UNIT_DAY.equals(unit)) {// 日
            cou = DateUtils.dateDiff('d', endCal, startCal);
        } else {// 月
            cou = DateUtils.dateDiff('d', endCal, startCal);// TODO
        }
        return new BigDecimal(cou);
    }
    /**
     * 功能介绍 : <br>
     *
     * @param billCode
     *            info表billCode
     * @param refuseWhy
     *            打回原因 <b>日期：</b>2017-8-8<br>
     *            <br>
     * @author lvwl
     */
    public void back(String billCode, String refuseWhy, ICurrentUser user) {
        CmsProvideLoanInfo info = loanInfoService.getLoanInfoByBillCode(billCode);
        info.setStatus(CmsConstant.APPROVED);
        info.setBizStatus(CmsConstant.BUSINESSSTATE_27);
        info.setRefuseWhy(refuseWhy);
        loanInfoService.saveOrUpdate(info);
        // 授信挂接
        if (!EmptyUtils.isEmpty(info.getRatmodel()) && CmsConstant.IS_TRUE.equals(info.getRatmodel())) {
            RatUrationDTO urationDTO = new RatUrationDTO(ConfigConstant.CMS, info.getId());
            urationService.preBackUseRation(urationDTO);
        }
        // 担保处理 0暂存，1在审使用，2使用中，3释放,4打回,5发放打回
        ProvideUtils.handleGua(info, null, guaContractOccupServie, 5, null);
        notificationService.complete(NotificationConstant.CMS.PROVIDE_SEND, info.getId(), user.getId());
        notificationService.send(info.getId(), info.getCorpId(), NotificationConstant.CMS.PROVIDE_REPAIR, info);
        msService.insertProcessMsg(info.getBillCode(), ProvideConstant.INFO_BILL_CODE_PREFIX,
                CmsProcessMsgConstant.MSGA_REPAIRBACK, CmsProcessMsgConstant.MSGA_SEND_BACK, user);
        List<LoanSendVO> sendList = cmsProvideLoanSendDao.getLoanSendVOListByBillCode(billCode);
        if (!EmptyUtils.isEmptyList(sendList)) {
            for (LoanSendVO send : sendList) {
                // todo:删除之前的计划执行数据再保存
                this.fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_PROVIDE_SEND,
                        BudgetConstant.PROCESS_SYSNAME_CMS, send.getBillCode());
                // 删除还款计划
                repaymentScheduleDao.deleteRepaymentByBillCode(send.getId());
                // 删除发放暂存数据
                cmsProvideLoanSendDao.delete(send.getId());
                notificationService.complete(NotificationConstant.CMS.PROVIDE_SEND, send.getId(), user.getId());
            }
        }
        // 合同处理
        String relateCon = configService.getConfig(ConfigConstant.CMS, BaseDataConstant.BANK_RELEATE_CON).getValue();// 是否对接合同管理
        String relateConType = configService.getConfig(ConfigConstant.CMS, BaseDataConstant.BANK_RELATE_CON_TYPE)
                .getValue();// 对接合同管理管理模式
        if (null != relateCon && relateCon.equals(CmsConstant.RELATED_CONTRACT_YES)) {// 对接合同管理
            if (null != relateConType && relateConType.equals(CmsConstant.BUSINESS_CREATE_CON)
                    && info.getConId() != null) {// 业务生成合同且合同id不为空
                conCommonService.unHookConOp(info.getConId(), info.getId(), user);// 取消
                info.setConId(null);
                loanInfoService.saveOrUpdate(info);
            }
        }
        // 抵质押物处理
        if (EmptyUtils.isNotEmpty(info.getIsRelatePledge()) && info.getIsRelatePledge().equals(CmsConstant.IS_TRUE)) {
            pledgeOccupyService.backPledgeOccupy(info.getId(), info.getBillCode());
        }
    }
    /**
     * 功能介绍 : 判断发放合同号是否重复<br>
     *
     * @return 重复：true 否则false
     */
    @Transactional(readOnly = true)
    public boolean checkContractNoRepeat(ProvideLoanSend send) {
        return cmsProvideLoanSendDao.checkContractNoRepeat(send);
    }
    /**
     * 根据billCode查询发放集合
     */
    @Transactional(readOnly = true)
    public List<LoanSendVO> getLoanSendVOListByBillCode(String billCode) {
        return cmsProvideLoanSendDao.getLoanSendVOListByBillCode(billCode);
    }
    /**
     * 根据billCode查询发放集合
     */
    @Transactional(readOnly = true)
    public List<ProvideLoanSend> getLoanSendListByBillCode(String billCode) {
        return cmsProvideLoanSendDao.getLoanSendListByBillCode(billCode);
    }
    /**
     * <br>
     * <b>功能：</b>获取贷款期限的借款发放信息<br>
     *
     * @param repayCycles
     * @param sendBillCode
     * @return
     */
    @Transactional(readOnly = true)
    public List<ProvideLoanSend> getLoanSendsByRepayCycle(String sendBillCode, Set<String> repayCycles) {
        return cmsProvideLoanSendDao.getLoanSendsByRepayCycle(sendBillCode, repayCycles);
    }
    /**
     * 占用预算
     *
     * @param entity
     * @param vo
     *            借款发放VO
     */
    public void batchEngross(ProvideLoanSend entity, LoanSendVO vo, ICurrentUser user, String saveType) {
        if (!EmptyUtils.isEmptyArray(vo.getItemCode())) {
            List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();
            for (int i = 0; i < vo.getItemCode().length; i++) {
                FbsBudgetProcessDTO fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
                if ("1".equals(saveType)) {
                    fbsBudgetProcessDTO.setStatus(BudgetConstant.STATUE_WAIT_CHECK);// 状态:11在途，95占用
                } else {
                    fbsBudgetProcessDTO.setStatus(BudgetConstant.STATUE_TEMP);// 状态:11在途，95占用
                }
                fbsBudgetProcessDTO.setBillNo(entity.getBillCode());// 业务单据号
                fbsBudgetProcessDTO.setBillId(entity.getId());
                fbsBudgetProcessDTO.setItemCode(vo.getItemCode()[i]);// 科目代码
                fbsBudgetProcessDTO.setItemName(vo.getItemName()[i]);// 科目名称
                fbsBudgetProcessDTO.setProcessMoney(vo.getProcessMoney()[i]);// 执行金额
                fbsBudgetProcessDTO.setProcessDate(entity.getSendDate());// 执行日期
                fbsBudgetProcessDTO.setCorpId(entity.getCorpId());// 单位id
                fbsBudgetProcessDTO.setCurId(entity.getCurCode());// 币别
                fbsBudgetProcessDTO.setBusinessName(BudgetConstant.BUSINESS_NAME_PROVIDE_SEND);// 执行业务名称
                fbsBudgetProcessDTO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_CMS);// 执行系统名称
                fbsBudgetProcessDTO.setCreateBy(user.getLoginName());
                fbsBudgetProcessDTO.setCreateDate(DateUtils.getDate());
                if (vo.getExchangeRate() != null) {
                    fbsBudgetProcessDTO.setExchangeRate(vo.getExchangeRate()[i]);
                    fbsBudgetProcessDTO.setProcessCurMoney(vo.getProcessCurMoney()[i]);
                }
                list.add(fbsBudgetProcessDTO);
            }
            // 修改时，先删除之前的计划执行数据再保存
            this.fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_PROVIDE_SEND,
                    BudgetConstant.PROCESS_SYSNAME_CMS, entity.getBillCode());
            this.fbsBudgetProcessService.writeBudgetProcess(list, user);
        }
    }
    /**
     * 查询记账完成待还款的发放单
     */
    @Transactional(readOnly = true)
    public List<ProvideLoanSend> getLoanSendCompeletotalByBillCode(String billCode) {
        return cmsProvideLoanSendDao.getLoanSendCompeletotalByBillCode(billCode);
    }
    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(ProvideLoanSend entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(String id) {
        super.delete(id);
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(ProvideLoanSend entity, ICurrentUser user) {
        if (!EmptyUtils.isEmpty(user)) {
            entity.setUpdateName(user.getUsername());
            entity.setUpdateBy(user.getId());
        }
        entity.setUpdateDate(new Date());
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(ProvideLoanSend entity, ICurrentUser user) {
        entity.setUpdateName(user.getUsername());
        entity.setUpdateDate(new Date());
        entity.setUpdateBy(user.getId());
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(ProvideLoanSend entity, ICurrentUser user) {
        if (EmptyUtils.isEmpty(entity.getId())) {
            entity.setCreateName(user.getUsername());
            entity.setCreateDate(new Date());
            entity.setCreateBy(user.getLoginName());
        } else {
            entity.setUpdateName(user.getUsername());
            entity.setUpdateDate(new Date());
            entity.setUpdateBy(user.getLoginName());
        }
        super.saveOrUpdate(entity);
    }
    public BaseDao<ProvideLoanSend, String> getEntityDao() {
        return cmsProvideLoanSendDao;
    }
    /**
     * 根据id修改还款方式
     *
     * @param id
     */
    public void updateRepayType(String id, String repayType) {
        cmsProvideLoanSendDao.updateRepayType(id, repayType);
    }
    /**
     * 是否挂机预算系统
     *
     * @param corpId
     *            单位ID
     * @param briefId1
     *            业务是否挂预算
     * @param briefId2
     *            业务挂预算科目
     * @return
     */
    public boolean isBudget(String corpId, String briefId1, String briefId2) {
        return StringUtils.isNotEmpty(corpId) && fbsBudgetProcessService.corpCtlBudget(corpId)
                && isBudget(briefId1, briefId2);
    }
    /**
     * 是否挂机预算系统
     *
     * @param briefId1
     *            业务是否挂预算
     * @param briefId2
     *            业务挂预算科目
     * @return
     */
    public boolean isBudget(String briefId1, String briefId2) {
        if (configService.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, briefId1).toBoolean()) {
            String budgetItem = configService.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, briefId2).toString();
            return StringUtils.isNotEmpty(budgetItem);
        }
        return false;
    }
    /**
     * 生成发放单号
     */
    private String getLoanSendBillCode() {
        return SequenceSingleton.getSequence(SequenceEnum.CMS_PROVIDE_LOANSEND_BILL_NO,
                ProvideConstant.SEND_BILL_CODE_PREFIX);
    }
    /***************** 审批BEGIN ********************/
    /**
     * 审批启动完成 执行
     */
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    /**
     * 审批流程结束 执行
     */
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();
        String processDefinitionKey = workFlowResult.getProcessDefinitionKey();
        boolean result = workFlowResult.isPass();
        ICurrentUser user = workFlowResult.getUser();
        String value = null;
        CorpDTO corp = null;
        if (CmsWorkFlowConstant.CMS_PROVIDE_LOAN_SEND.equals(processDefinitionKey)) {// 申请审批
            ProvideLoanSend send = this.get(businessKey,true);
            if (result) {// 审批通过
                corp = corpServiceImpl.get(send.getCorpId());
                ConfigDTO config = configService.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS,
                        BaseDataConstant.IS_BORROW_TALLY);
                value = config.getValue();
                send.setStatus(CmsConstant.COMPELETOTAL);
                send.setBizStatus(CmsConstant.BUSINESSSTATE_42);
                msService.insertProcessMsg(send.getBillCode(), ProvideConstant.INFO_BILL_CODE_PREFIX,
                        CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_SEND_CHECKOK, user);
                // 修改资金计划为占用状态
                this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PROVIDE_SEND,
                        BudgetConstant.PROCESS_SYSNAME_CMS, send.getBillCode(), BudgetConstant.STATUE_CHECK, user);
            } else {// 打回
                send.setStatus(CmsConstant.BACK);
                send.setBizStatus(CmsConstant.BUSINESSSTATE_37);
                msService.insertProcessMsg(send.getBillCode(), ProvideConstant.INFO_BILL_CODE_PREFIX,
                        CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_SEND_CHECKBACK, user);
                notificationService.send(send.getId(), send.getCorpId(), NotificationConstant.CMS.PROVIDE_SEND, send);// 添加发放申请的消息提示
                this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PROVIDE_SEND,
                        BudgetConstant.PROCESS_SYSNAME_CMS, send.getBillCode(), BudgetConstant.STATUE_BACK, user);
            }
            this.saveOrUpdate(send);
            getEntityDao().flush();
            if (CmsWorkFlowConstant.CMS_PROVIDE_LOAN_SEND.equals(processDefinitionKey)) {// 申请审批
                if (result) {// 审批通过
                    if (CmsConstant.IS_TRUE.equals(value) && CorpConstant.CORP_TYPE_CODE_NET.equals(corp.getType())) {// 是否记账
                        Date tallyDate = fcDayCloseService.getDayCloseFinalDate(corp.getId());// 获取网点日结日期
                        // 发放日期必须大于当前网点已日结日期
                        dateCompare(send.getSendDate(), tallyDate, corp.getName());// TODO
                        if (isAutoAccounting()) {// 是否自动记账
                            this.submit(send, user);
                            sendComplete(send.getLoanCode());// 发放完处理 TODO
                            // 合同处理
                            UserDTO userDTO = userService.getByLoginName(send.getCreateBy());
                            handleContract(null, send, userDTO);
                        } else {
                            send.setStatus(CmsConstant.WAITTOTAL);
                            send.setBizStatus(CmsConstant.BUSINESSSTATE_38);
                        }
                    } else {
                        sendComplete(send.getLoanCode());// 发放完处理 TODO
                        // 合同处理
                        UserDTO userDTO = null;
                        if (StringUtils.isNotEmpty(send.getCreateBy())) {
                            userDTO = userService.getByLoginName(send.getCreateBy());
                        } else {
                            userDTO = userService.getByLoginName(send.getUpdateBy());
                        }
                        handleContract(null, send, userDTO);
                    }
                }
            }
            // 根据还款计划和计息方式生成利息明细数据
            cmsProvideInterestService.adjustProvideInterest(send, user);
            cmsProvideLoanSendDao.flush();
        }
    }
    /***************** 审批END ********************/
    /***************** 记账BEGIN **********************/
    /**
     * 功能介绍 手工记账发放列表。<br>
     * <b>日期：</b>2018年4月9日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    public DataTablesOutput getProvideDataTables(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_PROVIDE_SEND_QUERY, user.getId());
        String opType = (String) dataTablesInput.getFormdata().get("opType");
        DataTablesOutput output = cmsProvideLoanSendDao.getProvideSendDataTables(dataTablesInput, corpAuth, opType, user);
        List<LoanSendVO> list = output.getData();
        if(CollectionUtils.isEmpty(list)) {
            return new DataTablesOutput();
        }
        List<String> codes = list.stream().map(LoanSendVO::getBankId).collect(Collectors.toList());
        List<BtBankDTO> listDTOs = btBankService.getListByCodes(codes);
        Map<String, String> dtoMap = listDTOs.stream().collect(Collectors.toMap(BtBankDTO::getBankCode, BtBankDTO::getBankName));
        if(!CollectionUtils.isEmpty(codes) && !CollectionUtils.isEmpty(listDTOs)) {
            for(LoanSendVO vo : list)
            {
                if(dtoMap.containsKey(vo.getBankId()))
                    vo.setBankName(dtoMap.get(vo.getBankId()));
            }
        }

        output.setData(list);
        return output;
    }
    /**
     * 功能介绍 手工记账还款列表。<br>
     * <b>日期：</b>2018年4月9日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    public DataTablesOutput getProvideRepayDataTables(DataTablesInput dataTablesInput, ICurrentUser user,
            String onlineSign, String confirm) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_PROVIDE_REPAY_QUERY, user.getId());
        String opType = (String) dataTablesInput.getFormdata().get("opType");
        DataTablesOutput output = cmsProvideLoanSendDao.getProvideRepayDataTables(dataTablesInput, corpAuth, opType, user, onlineSign,confirm);
        List<LoanSendVO> list = output.getData();
        if(CollectionUtils.isEmpty(list)) {
        	return new DataTablesOutput();
        }
        List<String> codes = list.stream().map(LoanSendVO::getBankId).collect(Collectors.toList());
        List<BtBankDTO> listDTOs = btBankService.getListByCodes(codes);
        Map<String, String> dtoMap = listDTOs.stream().collect(Collectors.toMap(BtBankDTO::getBankCode, BtBankDTO::getBankName));
        if(!CollectionUtils.isEmpty(codes) && !CollectionUtils.isEmpty(listDTOs)) {
        	for(LoanSendVO vo : list)
        	{
        		if(dtoMap.containsKey(vo.getBankId()))
        		vo.setBankName(dtoMap.get(vo.getBankId()));
        	}
        }

        output.setData(list);
        return output;
    }
    /**
     * 调用记账
     */
    public void submit(ProvideLoanSend send, ICurrentUser user) {
        FcAccbookVoucherDTO accbook = processAccbook(send, user);
        // TODO
        VoucherWrapperDTO voucher = cmsPrecativeLoanSendService.submit(accbook, BusOriType.NB_BANK_LOAN.get(), user);
        FcAccbookDTO acc = voucher.getTarget().get(0).getAccbook();
        List<FcAccbookDtlDTO> bookDtls = voucher.getTarget().get(0).getBookDtls();
        send.setOrderNo(acc.getOrderNo());
        send.setVoucherNo(acc.getVoucherNo());
    }
    /**
     * 功能：创建凭证信息
     */
    public FcAccbookVoucherDTO processAccbook(ProvideLoanSend send, ICurrentUser user) {
        FcAccbookVoucherDTO entity = new FcAccbookVoucherDTO();
        FcBusinessRuleDefineDTO ruleDefine = cmsPrecativeLoanSendService
                .getBusinessRuleExp(SysRaceCode.NB_BANK_LOAN.get());
        entity = processTallyAccbook(send, ruleDefine, user);
        return entity;
    }
    /**
     * 设置凭证信息
     */
    public FcAccbookVoucherDTO processTallyAccbook(ProvideLoanSend send, FcBusinessRuleDefineDTO ruleDefine,
            ICurrentUser user) {
        FcAccbookVoucherDTO entity = new FcAccbookVoucherDTO();
        FcAccbookDTO accBook = new FcAccbookDTO();
        entity.setAccbook(accBook);
        List<FcAccbookDtlDTO> accbookDtls = null;
        accbookDtls = getAccbookDtls(ruleDefine, send, user);
        /*
         * CorpDTO corp = corpServiceImpl.get(send.getCorpId()); String netId = "";
         * if(EmptyUtils.isEmpty(corp.getNetId())){//顶级网点 没有netid netId = corp.getId(); }else{ netId = corp.getNetId();
         * }
         */
        // TODO
        accBook.setBusRaceId(cmsPrecativeLoanSendService.getFcBusRaceId(send.getCorpId(),
                SysRaceCode.NB_BANK_LOAN, user == null ? " " : user.getId()));
        accBook.setVoucherType(ruleDefine.getVoucherType());
        accBook.setTallyDate(send.getSendDate());
        accBook.setInterestStartDate(send.getSendDate());
        accBook.setEmergencySign(FinacialConstant.EMERGENCYSIGN_G);
        entity.setAccbook(accBook);
        entity.setBookDtls(accbookDtls);
        return entity;
    }
    /**
     * 功能：记账时的会计分录构造
     */
    public List<FcAccbookDtlDTO> getAccbookDtls(FcBusinessRuleDefineDTO ruleDefine, ProvideLoanSend send,
            ICurrentUser user) {
        List<FcVoucherEntryDefineDTO> entrys = ruleDefine.getVouchers();
        FcAccbookDtlDTO accbookDtl = null;
        this.collectionSort(entrys);
        List<FcAccbookDtlDTO> accbookDtls = new ArrayList<FcAccbookDtlDTO>();
        LoanInfoVO infoVo = loanInfoService.getLoanInfoVOByBillCode(send.getLoanCode());
        CmsProvideLoanInfo info = loanInfoService.get(infoVo.getId());// 跳过发放根据code不能取到缓存数据
        int balanceWay;
        for (FcVoucherEntryDefineDTO entry : entrys) {
            if (entry.getGroupNum() == null) {
                throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_GROUPNUM_IS_NULL);
            }
            FcSubjectDTO sub = fcSubjectService.findSubjectById(entry.getSubjectCode());
            accbookDtl = new FcAccbookDtlDTO();
            accbookDtl.setCreditMoney(BigDecimal.ZERO);
            accbookDtl.setDebitMoney(BigDecimal.ZERO);
            if (10 == entry.getEntryType().intValue()) {
                balanceWay = entry.getBalanceWay().intValue();
                /**
                 * 借：1020银行活期存款 网点 贷：2110银行借款 网点
                 */
                if (1 == balanceWay) {
                    // 根据开户行查找银行活期账户，如果没有对应开户行的银行活期，则不根据开户行查找，取任意的银行活期账号
                    List<FcAccDTO> list = innerAccountService.listAccountByBankParams(send.getCurCode(), null,
                            send.getCorpId(), entry.getSubjectCode(), info.getBankId(), null);
                    if (EmptyUtils.isEmptyList(list)) {
                        throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_FCACC_IS_NULL);
                        // list =
                        // innerAccountService.listAccountByParams(send.getCurCode(),null,send.getCorpId(),entry.getSubjectCode());
                    }
                    if (EmptyUtils.isEmptyList(list)) {
                        throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_FCACC_IS_NULL);
                    }
                    FcAccDTO dto = list.get(0);
                    accbookDtl.setAccount(dto.getBlnctrAcc());
                    send.setBlnctrAcc(dto.getBlnctrAcc());// 保存银行活期账号
                    accbookDtl.setDebitMoney(send.getMoney());
                    accbookDtl.setAccName(dto.getAccName());
                } else if (-1 == balanceWay) {
                    Date deferredDate = provideLoanDeferredService.getMaxDeferredDate(send.getBillCode());
                    deferredDate = EmptyUtils.isEmpty(deferredDate) ? null : deferredDate;// send.getRepayDate():
                                                                                          // deferredDate;
                    FcAccDTO dto = new FcAccDTO();
                    dto.setNetId(send.getCorpId());
                    dto.setAccName(infoVo.getCorpName());
                    dto.setSubjectId(entry.getSubjectCode());
                    dto.setAutoCheck(true);
                    dto.setCorpId(send.getCorpId());
                    dto.setCurId(send.getCurCode());
                    dto.setInterestSign(CmsConstant.IS_TRUE);
                    dto.setInterestRate(send.getPlRate());
                    dto.setAccOpenTime(new Date());
                    dto.setAccStartDate(send.getSendDate());
                    dto.setNotifyType(send.getPlTerm().intValue());// (int)(send.getRepayDate().getTime() -
                                                                   // send.getSendDate().getTime())/(1000*60*60*24)
                    dto.setEndUnit(info.getTernUnit());// CmsConstant.CMS_LOAN_TERN_UNIT_DAY
                    dto.setAccType(InnerAccountConstant.FCACC_BANK_BORROW);
                    dto.setBalanceWay(sub.getBalanceWay());
                    dto.setDelayDeadlineDate(deferredDate);
                    dto.setDeadlineDate(send.getRepayDate());
                    dto.setDelayRate(send.getDeferredRate());
                    dto.setContractNo(info.getContractNo());
                    if (InterestConstant.PredrawSign.PREDRAW_SIGN_NO_SEASON_ANTICIPATE.get()
                            .equals(send.getInterestWay())
                            || InterestConstant.PredrawSign.PREDRAW_SIGN_NO_SEASON_NO_ANTICIPATE.get()
                                    .equals(send.getInterestWay())) {
                        dto.setPredrawSign(send.getInterestWay());
                    } else {
                        dto.setPredrawSign(InterestConstant.PredrawSign.PREDRAW_SIGN_NO_SEASON_ANTICIPATE.get());
                    }
                    dto.setOverdueRate(send.getOverdueRate());
                    dto.setInterestStartDate(send.getSendDate());
                    String account = innerAccountService.saveOrUpdate(dto, user);
                    send.setLoanAcc(account);// 将新开账号保存在发放表中
                    accbookDtl.setAccount(account);
                    accbookDtl.setCreditMoney(send.getMoney());
                    accbookDtl.setAccName(dto.getAccName());
                    // 自动授权
                    String netId = corpService.getNetByCorpId(send.getCorpId()).getNetId();
                    List<String> tallyUsers = getAutoCheckUser(netId);
                    tallyUsers.add(user.getId());
                    FcAccDTO newLoanAcc = innerAccountService.getByBlnctrAcc(account);
                    setAccountGrant(newLoanAcc.getId(), newLoanAcc.getCorpId(), tallyUsers);
                }
                accbookDtl.setCurCode(send.getCurCode());
                accbookDtl.setOrigVoucherNo(send.getBillCode());
                accbookDtl.setSummaryContent(info.getPlPurpose());
                accbookDtls.add(accbookDtl);
            }
        }
        return accbookDtls;
    }
    /**
     * 启用日期必须大于当前网点已日结日期
     *
     * @param date1
     * @param date2
     */
    protected void dateCompare(Date date1, Date date2, String corpId) {
        if (DateUtils.dayDiffs(date1, date2) < 0) {
            throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_SENDDATE_PASS_CLOSEDATE, corpId,
                    DateUtils.formatDate(date2));
        }
    }
    /****************** 记账END ************************/
    /***************** 验证BEGIN ********************/
    private void validata(ProvideLoanSend send) {
        if (isTally()) {
            CorpDTO corp = corpServiceImpl.get(send.getCorpId());
            Date tallyDate = fcDayCloseService.getDayCloseFinalDate(corp.getNetId());// 获取网点日结日期
            if (DateUtils.dayDiffs(send.getSendDate(), tallyDate) < 0) {// 发放日期已日结
                throw new ParameterException(PrecativeMessageEnum.CMS_PRECATIVE_SENDDATE_PASS_CLOSEDATE,
                        corpServiceImpl.get(corp.getNetId()).getName(), DateUtils.formatDate(tallyDate));
            }
        }
        if (EmptyUtils.isEmpty(send.getContractCode())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANSEND_CONTRACTNOEMPTY);
        }
        if (checkContractNoRepeat(send)) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANSEND_CONTRACTCODEREPEAT);
        }
        if (send.getMoney() == null || send.getMoney().compareTo(BigDecimal.ZERO) == 0) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANSEND_SENDMONEYZERO);
        }
        if (send.getSendDate() == null) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANSEND_SENDDATEEMPTY);
        }
        if (send.getRepayDate() == null) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANSEND_REPAYDATEEMPTY);
        }
        if (send.getSendDate().getTime() > send.getRepayDate().getTime()) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANSEND_SENDDATELEREPAYDATE);
        }
        // 还款方式校验
        if (EmptyUtils.isEmpty(send.getRepayType())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_REPAYTYPEEMPTY);
        }
        // 计提方式校验
        if (EmptyUtils.isEmpty(send.getAccrualType())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_ACCRUALTYPEEMPTY);
        }
        /*
         * if (EmptyUtils.isEmpty(send.getInterestWay())) { throw new
         * ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANSEND_INTERESTWAYEMPTY); }
         */
        if (EmptyUtils.isEmpty(send.getFloatWay())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_FLOATWAYEMPTY);
        }
        if (!CmsConstant.FLOAT_WAY_01.equals(send.getFloatWay()) && send.getRateEntriesEffectDate() == null) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_EFFECTDATEEMPTY);
        }
        /**
         * 利率验证 利率可以为0
         */
        // if(send.getPlRate() == null || send.getPlRate().compareTo(BigDecimal.ZERO) == 0){
        // throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_PlRATEEMPTY);
        // }
        if (send.getOverdueRate() == null || send.getOverdueRate().compareTo(BigDecimal.ZERO) == 0) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_OVERDUERATEEMPTY);
        }
    }
    /**
     * 定时任务 提醒发放到期
     */
    @Override
    @XxlJob("CmsProvideLoanSendService")
    public void run(String arguments) {
        ConfigDTO configDTO = configService.getConfig(ConfigConstant.CMS, BaseDataConstant.PROVIDE_CONFIG_NOTIFYDAYS);// 授信到期提醒天数
        Integer notifyDays = configDTO.toInteger(0);
        Date endDate = DateUtils.getDateYMD();
        endDate = DateUtils.addDay(endDate, notifyDays);
        // 查询到期的发放数据
        List<ProvideLoanSend> sends = cmsProvideLoanSendDao.listMatureSend(endDate);
        if (!EmptyUtils.isEmptyList(sends)) {
            for (ProvideLoanSend send : sends) {
                notificationService.send(send.getId(), send.getCorpId(), NotificationConstant.CMS.PROVIDE_MATURE, send);
                logger.info("借款发放到期提醒，编码：" + send.getBillCode() + "，合同：" + send.getContractCode());
                send.setSendSign(CommonConstant.SEND_SIGN_YES);// 更改发送标识
                this.updateEntity(send);
            }
        }
    }
    /**
     * <b>功能介绍：</b>查询内部贷款发放到期<br>
     * <b>日期：</b>2017年11月16日<br>
     * <b>作者：</b>xxx<br>
     *
     * @param netId
     * @param endDate
     * @return
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public List<CmsProvideLoanSendCheckVO> getBankLoadSendInfoByEndDate(String netId, Date endDate) {
        return cmsProvideLoanSendDao.getBankLoadSendInfoByEndDate(netId, endDate);
    }
    /**
     * 判断是否自动记账
     */
    public boolean isAutoAccounting() {
        ConfigDTO config = configService.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS,
                BaseDataConstant.IS_BORROW_AUTOMATIC_TALLY);
        // 自动记账
        return CmsConstant.IS_TRUE.equals(config.getValue());
    }
    /**
     * 判断是否记账
     */
    public boolean isTally() {
        // 是否记账
        return CmsConstant.IS_TRUE.equals(configService.getConfig("cms", "is_borrow_tally").getValue());
    }
    /**
     * 功能：对业务规则排序
     */
    public void collectionSort(List<FcVoucherEntryDefineDTO> entrys) {
        Collections.sort(entrys, new Comparator<FcVoucherEntryDefineDTO>() {
            public int compare(FcVoucherEntryDefineDTO o1, FcVoucherEntryDefineDTO o2) {
                return o1.getGroupNum().compareTo(o2.getGroupNum());
            }
        });
    }
    /**
     * 合同履约处理
     */
    public void handleContract(String conId, ProvideLoanSend send, ICurrentUser user) {
        cmsProvideLoanSendDao.flush();
        String relateCon = configService.getConfig(ConfigConstant.CMS, BaseDataConstant.BANK_RELEATE_CON).getValue();// 是否对接合同管理
        String relateConType = configService.getConfig(ConfigConstant.CMS, BaseDataConstant.BANK_RELATE_CON_TYPE)
                .getValue();// 对接合同管理管理模式
        String conSegment = configService.getConfig(ConfigConstant.CMS, BaseDataConstant.BANK_RELATE_CON_SEGMENT)
                .toFormatString();// 履约环节
        if (null != relateCon && relateCon.equals(CmsConstant.RELATED_CONTRACT_YES) && // 对接合同管理
        // null!=relateConType&&relateConType.equals(CmsConstant.BUSINESS_CREATE_CON)&&
                conSegment.contains(CmsConstant.SEGMENT_LOAN)) {// 履约环节包括放款履约
            ConInfoRecordDTO recordDTO = new ConInfoRecordDTO();
            if (null != conId) {
                recordDTO.setConId(conId);// 合同ID 必填
            } else {
                LoanInfoVO infoVo = loanInfoService.getLoanInfoVOByBillCode(send.getLoanCode());
                recordDTO.setConId(infoVo.getConId());// 合同ID 必填
            }
            recordDTO.setOuterId(send.getId());// 履约业务挂接ID 必填
            recordDTO.setOpMsg(CmsConstant.PROVIDE_LOAN_SEND);// 履约原因 必填
            recordDTO.setOpDate(send.getSendDate());// 履约时间 必填
            recordDTO.setOpNo(send.getBillCode());// 履约申请编号 选填
            recordDTO.setOpMoney(send.getMoney());// 履约金额 选填
            conCommonService.performanceConOp(recordDTO, user);
        }
    }
    /**
     * 获取自动记账人
     */
    public List<String> getAutoCheckUser(String netId) {
        ConfigDTO tallyConfig = configService.getConfig(ConfigConstant.FCL, "autoAccounter");
        ConfigDTO checkeConfig = configService.getConfig(ConfigConstant.FCL, "autoChecker");
        String tallyValue = tallyConfig.getValue();
        if (EmptyUtils.isEmpty(tallyValue)) {// 自动记账人为空
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_AUTOACCOUNTER_NULL);
        }
        String checkValue = checkeConfig.getValue();
        if (EmptyUtils.isEmpty(checkValue)) {// 自动复核人为空
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_AUTOCHECKER_NULL);
        }
        tallyValue = tallyValue.replaceAll("；", ";").replaceAll("，", ";").replaceAll(",", ";");
        checkValue = checkValue.replaceAll("；", ";").replaceAll("，", ";").replaceAll(",", ";");
        String[] netTallyUsers = tallyValue.split(";");// 分割
        String[] netCheckUsers = checkValue.split(";");// 分割
        Map<String, String> tallyUserInfo = new HashMap<String, String>();
        for (String netUser : netTallyUsers) {
            String[] temps = netUser.split("=");// '='分割为网点和记账人
            tallyUserInfo.put(temps[0].trim(), temps[1].trim());
        }
        CorpDTO corpDTO1 = corpService.get(netId);
        String userCode1 = tallyUserInfo.get(corpDTO1.getCode());// 获取记账人
        UserDTO userDTO1 = userService.getByLoginName(userCode1);
        if (null == corpDTO1 || null == userCode1) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_AUTOACCOUNTER_NULL);
        }
        Map<String, String> userInfo = new HashMap<String, String>();
        for (String netUser : netCheckUsers) {
            String[] temps = netUser.split("=");// '='分割为网点和记账人
            userInfo.put(temps[0].trim(), temps[1].trim());
        }
        CorpDTO corpDTO = corpService.get(netId);
        String userCode = userInfo.get(corpDTO.getCode());// 获取复核人
        UserDTO userDTO = userService.getByLoginName(userCode);
        if (null != userDTO && null != userDTO1) {
            List<String> users = new ArrayList<>();
            users.add(userDTO.getId());
            users.add(userDTO1.getId());
            return users;
        }
        if (null == userDTO || null == corpDTO) {
            throw new ParameterException(AccbookMessageEnum.FIN_BOOK_TALLY_AUTOCHECKER_NULL);
        }
        return new ArrayList<>();
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
    /**
     * 功能介绍 : 查询发放完成的数据列表<br>
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataSendOverTables(DataTablesInput input) {
        return cmsProvideLoanSendDao.getDataSendOverTables(input);
    }
    /**
     * 根据借款发放单据id银行借款发放流水认领功能挂接标记为挂接
     *
     * @param id
     */
    public void relateBankFlow(String id) {
        if (EmptyUtils.isNotEmpty(id)) {
            ProvideLoanSend provideLoanSend = this.get(id);
            provideLoanSend.setBankFlowSign(CmsConstant.IS_TRUE);
        }
    }
    /**
     * 根据借款发放单据id银行借款发放流水认领功能挂接标记为未挂接
     *
     * @param id
     */
    public void cancelBankFlow(String id) {
        if (EmptyUtils.isNotEmpty(id)) {
            ProvideLoanSend provideLoanSend = this.get(id);
            provideLoanSend.setBankFlowSign(CmsConstant.IS_FALSE);
        }
    }
    /**
     * 单位禁用校验-待记账
     *
     * @return
     * @Param corpId
     */
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        List<CorpVerifyErrorDTO> corpVerifyErrorDTOS = new ArrayList<>();
        if (cmsProvideLoanSendDao.verifyCorpDisableByCorpIdTally(corpId) > 0) {
            CorpVerifyErrorDTO corpVerifyErrorDTO = new CorpVerifyErrorDTO();
            corpVerifyErrorDTO.setCorpId(corpId);
            corpVerifyErrorDTO.setSysCode(SysConfigConstant.Module.FCL);
            corpVerifyErrorDTO.setBusCode(SysConfigConstant.BusType.FCL_TALLY);
            corpVerifyErrorDTO
                    .setErrorMsg(multiLangServiceI.getLang(CorpVerifyMessageConstant.FIN_TALLY_VALID_DISABLE_ERROR));
            corpVerifyErrorDTOS.add(corpVerifyErrorDTO);
        }
        if (EmptyUtils.isNotEmpty(corpId)) {
            int dataSize1 = cmsProvideLoanSendDao.queryBusBillByCorpId(corpId, null, CmsConstant.BUSINESSSTATE_72);// 展期待补录
            if (dataSize1 > 0) {
                corpVerifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.CMS_PROVIDELOAN,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.CMS_VALID_DISABLE_PROLOAN_DEFERING_ERROR)));
            }
            int dataSize2 = cmsProvideLoanSendDao.queryBusBillByCorpId(corpId, null, CmsConstant.BUSINESSSTATE_78);// 展期待处理
            if (dataSize2 > 0) {
                corpVerifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.CMS_PROVIDELOAN,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.CMS_VALID_DISABLE_PROLOAN_DEFERING_ERROR)));
            }
            int dataSize3 = cmsProvideLoanSendDao.queryRepayingDataByCorpId(corpId);// 未还款完成的数据
            if (dataSize3 > 0) {
                corpVerifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.CMS_PROVIDELOAN,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.CMS_VALID_DISABLE_PROLOAN_REPAYING_ERROR)));
            }
            // 发放暂存，审批打回，待审批数据
            int dataSize4 = cmsProvideLoanSendDao.queryBusBillByCorpId(corpId,
                    CmsConstant.TEMP + "," + CmsConstant.COMMITAPPROVE + "," + CmsConstant.BACK, null);
            if (dataSize4 > 0) {
                corpVerifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.CMS_PROVIDELOAN,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.CMS_VALID_DISABLE_PROLOAN_SEND_ERROR)));
            }
            // 借款展期暂存，审批打回，待审批数据
            int dataSize5 = cmsProvideLoanSendDao.queryBusBillByCorpId(corpId, null, CmsConstant.BUSINESSSTATE_60 + ","
                    + CmsConstant.BUSINESSSTATE_61 + "," + CmsConstant.BUSINESSSTATE_67);
            if (dataSize5 > 0) {
                corpVerifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.CMS_PROVIDELOAN,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.CMS_VALID_DISABLE_PROLOAN_DEFER_ERROR)));
            }
        }
        return corpVerifyErrorDTOS;
    }
    /**
     * 查询发放账务数据 --待记账
     */
    public List<CmsProvideSendDTO> queryTallyProvideSend() {
        return cmsProvideLoanSendDao.queryTallyProvideSend();
    }
    /**
     * 查询还款账务数据 --记账
     */
    public List<CmsProvideLoanRepayDTO> queryTallyProvideRepay(String onlineSign) {
        return cmsProvideLoanSendDao.queryTallyProvideRepay(onlineSign);
    }
    /**
     * <br>
     * <b>功能：</b> 查询借款融资情况 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/12/8 0008 <br>
     */
    public List<FinanceLoanDTO> queryProvideFinance(List<String> corpAuth, String curId, Date queryDate) {
        return cmsProvideLoanSendDao.queryProvideFinance(corpAuth, curId, queryDate);
    }
    /**
     * <br>
     * <b>功能：</b> 查询借款利息净支出情况 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/12/8 0008 <br>
     */
    public List<FinanceLoanDTO> queryInterestFinance(List<String> corpAuth, String curId, Date queryDate) {
        return cmsProvideLoanSendDao.queryInterestFinance(corpAuth, curId, queryDate);
    }
    /**
     * <br>
     * <b>功能：</b> 查询借款各融资品种的融资情况 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/12/8 0008 <br>
     */
    public List<FinanceTypeLoanDTO> queryProvideTypeFinance(List<String> corpAuth, String curId, Date queryDate) {
        return cmsProvideLoanSendDao.queryProvideTypeFinance(corpAuth, curId, queryDate);
    }
}
