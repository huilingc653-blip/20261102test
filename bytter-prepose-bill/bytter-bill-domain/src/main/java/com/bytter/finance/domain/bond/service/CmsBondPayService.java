package com.bytter.finance.domain.bond.service;

import com.bytter.biz.payment.api.constant.ExpenseConstant;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.biz.payment.api.service.INisBillheadService;
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
import com.bytter.core.constant.NetbankConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.finance.constant.*;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.bond.dao.CmsBondPayDao;
import com.bytter.finance.domain.bond.entity.CmsBondIssueInfo;
import com.bytter.finance.domain.bond.entity.CmsBondPay;
import com.bytter.finance.domain.bond.entity.CmsBondRepayPlanDtl;
import com.bytter.finance.domain.bond.utils.BondUtils;
import com.bytter.finance.domain.bond.vo.CmsBondIssueInfoVo;
import com.bytter.finance.domain.bond.vo.CmsBondPayVo;
import com.bytter.finance.domain.bond.vo.CmsBondRepayPlanDtlVo;
import com.bytter.finance.enums.AllocationMessageEnum;
import com.bytter.finance.enums.CmsBondMessageEnum;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaPledgeOccupyService;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IExternalCorpService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.ExternalCorpBankAccDTO;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
/**
 * @author mof
 * @version 1.0
 * @Title: Service
 * @Description: 债券还款
 * @date 2022-06-06 10:07:46
 */
@WorkFlow(CmsWorkFlowConstant.CMS_BONDPAY)
@DubboService(interfaceClass = IWorkFlowListener.class,group = CmsWorkFlowConstant.CMS_BONDPAY)
@Service("cmsBondPayService")
@Transactional
public class CmsBondPayService extends BaseService<CmsBondPay, String> implements IWorkFlowListener {
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private CmsBondPayDao cmsBondPayDao;
    @DubboReference(check = false)
    private ICorpService corpServiceImpl;
    @Autowired
    private CmsProcessMsgService msService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private IBtBankAccService iBtBankAccService;
    @DubboReference(check = false)
    private INisBillheadService nisBillheadService;
    @DubboReference(check = false)
    private IExternalCorpService externalCorpService;
    @DubboReference(check = false)
    private IFcVoucherTypeService fcVoucherTypeService;
    @Autowired
    private CmsBondIssueInfoService cmsBondIssueInfoService;
    @Autowired
    private CmsBondRepayPlanService cmsBondRepayPlanService;
    @Autowired
    private CmsBondRegisterInfoService cmsBondRegisterInfoService;
    @Autowired
    private CmsBondRepayPlanDtlService cmsBondRepayPlanDtlService;
    @Resource(name = "fcBondHookServiceImp")
    private IFcBondHookService fcBondHookService;
    @Resource(name = "guaPledgeOccupyServiceImpl")
    private IGuaPledgeOccupyService pledgeOccupyService;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie guaContractOccupyService;
    @Override
    public BaseDao<CmsBondPay, String> getEntityDao() {
        return cmsBondPayDao;
    }
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        List<String> corpAuthList = corpService.getUserCorpIds(ResourceConstant.CMS_BOND_CMSBONDPAY_QUERY,
                dataTablesInput.getCurrentUserId());
        // 筛选已经添加了还款计划的债券信息
        List<String> registerIdList = cmsBondRepayPlanService.getAllDataByPlan(corpAuthList);
        DataTablesOutput output = cmsBondRegisterInfoService.getDataTablesByIds(dataTablesInput, registerIdList);
        return output;
    }
    public CmsBondPayVo getById(String id) {
        if (EmptyUtils.isEmpty(id)) {
            return new CmsBondPayVo();
        }
        List<CmsBondPayVo> list = cmsBondPayDao.getById(id);
        if (ListUtils.isNullOrEmpty(list)) {
            return new CmsBondPayVo();
        }
        return list.get(0);
    }
    /**
     * 根据债券主表id和是否付息查询债券还款信息
     *
     * @param registerId
     *            债券主表id
     * @param isPayInterest
     *            是否付息
     * @return 债券还款信息
     */
    public List<CmsBondPayVo> getByRegisterId(String registerId, String isPayInterest) {
        List<CmsBondPayVo> list = cmsBondPayDao.getByRegisterId(registerId, isPayInterest);
        return list;
    }
    public List<CmsBondPayVo> getBondPayByRegisterId(String registerId) {
        List<CmsBondPayVo> list = cmsBondPayDao.getBondPayByRegisterId(registerId);
        return list;
    }
    /**
     * 校验该发行是否还有在审批中的数据
     *
     * @param issueId
     *            债券发行id
     * @return 债券还款信息
     */
    public void checkCmsBondPayStatus(String issueId) {
        List<CmsBondPayVo> list = cmsBondPayDao.getInfoByStatus(issueId);
        if (list.size() > 0) {
            throw new ParameterException(CmsBondMessageEnum.CMS_BOND_BONDPAY_STATUS_COMMITAPPROVE);
        }
    }
    /**
     * 功能介绍 : 查询发放剩余额度（发放金额 - 已还金额）<br>
     *
     * @param issueId
     *            债券发行Id
     * @param passageSign
     *            在途标识 1查询在途金额 2已还 其他为 两个之和 <b>日期：</b>2017-8-7<br>
     */
    public BigDecimal completeSendBalance(String issueId, String passageSign) {
        // 获取发行总金额
        CmsBondIssueInfoVo cmsBondIssueInfoVo = cmsBondIssueInfoService.getByIssueId(issueId);
        if (EmptyUtils.isEmpty(cmsBondIssueInfoVo)) {
            return BigDecimal.ZERO;
        }
        // 获取已还的租金金额
        BigDecimal repayMoney = cmsBondPayDao.getRepayMoneyByIssueId(issueId, passageSign);
        BigDecimal money = cmsBondIssueInfoVo.getIssueMoney().subtract(repayMoney);
        return money;
    }
    /**
     * 功能介绍 : 暂存或提交付款<br>
     */
    public void tempOrSubmit(CmsBondPayVo vo, String saveType, String tempOrSubmitSign, ICurrentUser user,
            String sign) {
        CmsBondPay cmsBondPay = new CmsBondPay();
        BeanUtils.copyNotNullProperties(vo, cmsBondPay);
        validata(cmsBondPay);// TODO 验证借款利息预计明细 是否重复选择 和是否最小日期
        // 修改债券发行表信息
        CmsBondIssueInfo cmsBondIssueInfo = cmsBondIssueInfoService.get(vo.getIssueId());
        if ("1".equals(saveType)) {// 提交
            cmsBondPay.setStatus(CmsConstant.COMMITAPPROVE);
            cmsBondIssueInfo.setBizStatus(CmsConstant.BOND_BIZ_STATUS_71);
        } else {// 暂存
            cmsBondPay.setStatus(CmsConstant.TEMP);
            cmsBondIssueInfo.setBizStatus(CmsConstant.BOND_BIZ_STATUS_70);
        }
        BigDecimal repayTotalMoney;
        if (CmsConstant.IS_TRUE.equals(cmsBondPay.getIsPayInterest())) {
            repayTotalMoney = cmsBondPay.getRepayMoney().add(cmsBondPay.getInterest());
        } else {
            repayTotalMoney = cmsBondPay.getRepayMoney();
        }
        if (EmptyUtils.isEmpty(cmsBondPay.getId())) {
            cmsBondPay.setRepayTotalMoney(repayTotalMoney);
            cmsBondPay.setCreateBy(user.getUsername());
            cmsBondPay.setCreateDate(new Date());
            cmsBondPay.setCreateBy(user.getLoginName());
            super.saveEntity(cmsBondPay);
            // 保存租金计划子表
            String id = "";
            id = cmsBondPay.getId();
            vo.setId(id);
        } else {
            cmsBondPay = this.get(cmsBondPay.getId());
            cmsBondPay.setRepayTotalMoney(repayTotalMoney);
            cmsBondPay.setUpdateBy(user.getLoginName());
            cmsBondPay.setUpdateBy(user.getUsername());
            cmsBondPay.setUpdateDate(DateUtils.getDate());
            this.saveOrUpdate(cmsBondPay);
        }
        // 修改债券发行表总状态
        cmsBondIssueInfoService.saveOrUpdate(cmsBondIssueInfo);

        // 回写还款计划明细的payId
        if (EmptyUtils.isNotEmpty(vo.getPlanDtlId())) {
            cmsBondRepayPlanDtlService.updatePayId(vo.getPlanDtlId(), cmsBondPay.getId());
        }
        

        if ("1".equals(tempOrSubmitSign)) {// 处于修改状态标志
            if ("1".equals(saveType)) {// 提交
                msService.insertProcessMsg(vo.getIssueId(), BondConstant.BONDPAY_BOND_NO,
                        CmsProcessMsgConstant.MSGA_REPAIR_MODIFY, CmsProcessMsgConstant.MSGA_REPAY_UPDATE_SUBMIT, user);
                msService.insertProcessMsg(vo.getPlanDtlId(), BondConstant.BONDPAY_BOND_NO,
                        CmsProcessMsgConstant.MSGA_REPAIR_MODIFY, CmsProcessMsgConstant.MSGA_REPAY_UPDATE_SUBMIT, user);
                 getStartHandler().handler(CmsWorkFlowConstant.CMS_BONDPAY, vo.getId(), vo, vo.getCorpId(),
                        user.getId());
            } else {
                msService.insertProcessMsg(vo.getIssueId(), BondConstant.BONDPAY_BOND_NO,
                        CmsProcessMsgConstant.MSGA_MODIFY, CmsProcessMsgConstant.MSGA_REPAY_UPDATE_TEMP, user);
                msService.insertProcessMsg(vo.getPlanDtlId(), BondConstant.BONDPAY_BOND_NO,
                        CmsProcessMsgConstant.MSGA_MODIFY, CmsProcessMsgConstant.MSGA_REPAY_UPDATE_TEMP, user);
            }
        } else if ("0".equals(tempOrSubmitSign) || EmptyUtils.isEmpty(tempOrSubmitSign)) {
            if ("1".equals(saveType)) {// 提交
                msService.insertProcessMsg(vo.getIssueId(), BondConstant.BONDPAY_BOND_NO,
                        CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_REPAY_SUBMIT, user);
                msService.insertProcessMsg(vo.getPlanDtlId(), BondConstant.BONDPAY_BOND_NO,
                        CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_REPAY_SUBMIT, user);
                 getStartHandler().handler(CmsWorkFlowConstant.CMS_BONDPAY, vo.getId(), vo, vo.getCorpId(),
                        user.getId());
            } else {
                msService.insertProcessMsg(vo.getIssueId(), BondConstant.BONDPAY_BOND_NO,
                        CmsProcessMsgConstant.MSGA_INPUT, CmsProcessMsgConstant.MSGA_REPAY_TEMP, user);
                msService.insertProcessMsg(vo.getPlanDtlId(), BondConstant.BONDPAY_BOND_NO,
                        CmsProcessMsgConstant.MSGA_INPUT, CmsProcessMsgConstant.MSGA_REPAY_TEMP, user);
            }
        }
    }
    /**
     * 还款申请验证
     */
    private void validata(CmsBondPay cmsBondPay) {
        if (cmsBondPay.getPayDate() == null) {
            throw new ParameterException(CmsBondMessageEnum.CMS_BOND_BONDPAY_PAYDATE_EMPTY);
        }
        if (EmptyUtils.isEmpty(cmsBondPay.getRepayMoney())) {
            throw new ParameterException(CmsBondMessageEnum.CMS_BOND_BONDPAY_REPAYMONEY_EMPTY);
        }
        // 还款金额不能超过 未还款金额
        BigDecimal sendBalance = completeSendBalance(cmsBondPay.getIssueId(), null);
        CmsBondPay oldRepay = null;
        if (!EmptyUtils.isEmpty(cmsBondPay.getId())) {
            oldRepay = get(cmsBondPay.getId());
            sendBalance = sendBalance.add(oldRepay.getRepayMoney());
        }
        if (cmsBondPay.getRepayMoney().compareTo(sendBalance) == 1) {
            throw new ParameterException(CmsBondMessageEnum.CMS_BOND_BONDPAY_REPAYMONEY_SENDBALANCE);
        }
        // 是否付息
        if (CmsConstant.IS_TRUE.equals(cmsBondPay.getIsPayInterest())) {
            if (EmptyUtils.isEmpty(cmsBondPay.getInterest())) {
                throw new ParameterException(CmsBondMessageEnum.CMS_BOND_BONDPAY_INTEREST_EMPTY);
            }
        }
        // 线上还款的必填项 验证
        if (CmsConstant.ONLINE_TRUE.equals(cmsBondPay.getOnlineSign())) {
            if (EmptyUtils.isEmpty(cmsBondPay.getPurpose())) {
                throw new ParameterException(CmsBondMessageEnum.CMS_BOND_BONDPAY_PURPOSE_EMPTY);
            }
            if (EmptyUtils.isEmpty(cmsBondPay.getOnlinePaymentAcc())) {
                throw new ParameterException(CmsBondMessageEnum.CMS_BOND_BONDPAY_ONLINEPAYMENTACC_EMPTY);
            }
            if (EmptyUtils.isEmpty(cmsBondPay.getOnlineAccName())) {
                throw new ParameterException(CmsBondMessageEnum.CMS_BOND_BONDPAY_ONLINEACCNAME_EMPTY);
            }
            if (EmptyUtils.isEmpty(cmsBondPay.getOnlineBankName())) {
                throw new ParameterException(CmsBondMessageEnum.CMS_BOND_BONDPAY_ONLINEBANKNAME_EMPTY);
            }
            if (!EmptyUtils.isEmpty(cmsBondPay.getId())) {
                if (CmsConstant.ONLINE_TRUE.equals(oldRepay.getOnlineSign())
                        && CmsConstant.ONLINE_FALSE.equals(cmsBondPay.getOnlineSign())) {
                    if (CmsConstant.FLOWWAIT != oldRepay.getStatus()) {
                        throw new ParameterException(AllocationMessageEnum.NIS_ALLOCATION_MESSAGE_STATUS_DELETE);
                    }
                }
            }
        }
    }
    /**
     * 功能介绍 : 还款申请审批列表 - 分页<br>
     */
    @Transactional(readOnly = true)
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(CmsWorkFlowConstant.CMS_BONDPAY,
                dataTablesInput);
        DataTablesOutput dp = cmsBondPayDao.dataTableBondPayApprove(dataTablesInput,
                workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }
    /***************** 审批BEGIN ********************/
    /**
     * 审批启动完成 执行
     */
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
        // TODO Auto-generated method stub
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
        CmsBondPay pay = this.get(businessKey,true);
        // 修改债券发行表信息
        CmsBondIssueInfo cmsBondIssueInfo = cmsBondIssueInfoService.get(pay.getIssueId());
        if (CmsWorkFlowConstant.CMS_BONDPAY.equals(processDefinitionKey)) {// 还款申请审批
            if (result) {// 审批通过
                cmsBondIssueInfo.setBizStatus(CmsConstant.BOND_BIZ_STATUS_101);
                msService.insertProcessMsg(pay.getId(), BondConstant.BONDPAY_BOND_NO,
                        CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_REPAY_CHECKOK, user);
                msService.insertProcessMsg(pay.getIssueId(), BondConstant.BONDPAY_BOND_NO,
                        CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_REPAY_CHECKOK, user);
                if (CmsConstant.ONLINE_TRUE.equals(pay.getOnlineSign())) {// 线上还款审批通过
                    pay.setStatus(CmsConstant.APPROVED);
                    // 走付款办理流程
                    NisBillheadDTO nisBillhead = new NisBillheadDTO();
                    String id = UUID.randomUUID().toString().replace("-", "");
                    nisBillhead.setId(id);
                    nisBillhead.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_BILLHEAD));
                    CorpDTO corp = corpServiceImpl.get(pay.getCorpId());
                    nisBillhead.setCorpId(pay.getCorpId());// 单位
                    nisBillhead.setCorpName(corp.getName());// 单位
                    BtBankAccDTO bank = iBtBankAccService.getBankAccInfo(pay.getOnlinePaymentAcc());
                    if (!StringUtils.isNotEmpty(bank)) {
                        List<String> acclist = new ArrayList<>();
                        acclist.add(pay.getOnlinePaymentAcc());
                        // 根据账号查询客商账户信息
                        ExternalCorpBankAccDTO externalCorpBankAcc = externalCorpService
                                .getExternalCorpBankAccByIdAndAcc(null, pay.getOnlinePaymentAcc());
                        if (externalCorpBankAcc != null) {
                            nisBillhead.setAccId(externalCorpBankAcc.getId());// 账号
                            nisBillhead.setPayeeBankCode(externalCorpBankAcc.getBankSourceCode());// 收款方开户行行号
                            nisBillhead.setProv(externalCorpBankAcc.getAccProvince());// 所在省
                            nisBillhead.setCity(externalCorpBankAcc.getAccCity());// 所在市
                        }
                    } else {
                        nisBillhead.setAccId(bank.getId());// 账号
                        nisBillhead.setPayeeBankCode(bank.getBankCode());// 收款方开户行行号
                        nisBillhead.setCurId(bank.getCurId());// 币种
                        nisBillhead.setProv(bank.getProv());// 所在省
                        nisBillhead.setCity(bank.getCity());// 所在市
                    }
                    nisBillhead.setPayeeAcc(pay.getOnlinePaymentAcc());// 账户
                    nisBillhead.setPayeeAccName(pay.getOnlineAccName());// 账户名称
                    nisBillhead.setPayeeBank(pay.getOnlineBankName());// 开户行名称
                    nisBillhead.setPayMoney(pay.getRepayTotalMoney());// 金额
                    nisBillhead.setWishPayday(pay.getPayDate());// 支付日期
                    // 根据调拨类型设置结算方式
                    FcVoucherTypeDTO voucherType = null;
                    voucherType = fcVoucherTypeService.getSysFcVoucherType(NetbankConstant.PAY_TYPE_34);
                    if (voucherType != null) {
                        nisBillhead.setVoucherTypeId(voucherType.getId());// 付款方式默认外付网银
                    }
                    nisBillhead.setPurpose(pay.getPurpose());// 用途
                    nisBillhead.setStatus(ExpenseConstant.STATUE_TEMP);// 状态
                    nisBillhead.setBillFrom(NetbankConstant.BILLFROM_11);// 单据来源:1-债券还款支付
                    nisBillhead.setIsPayed(NetbankConstant.ISPAYED_0); // 是否已付款标志 默认否
                    nisBillhead.setIsQuick(NetbankConstant.ISQUICK_0);// 加急标记默认为不加急
                    nisBillhead.setIsForindividual(NetbankConstant.FORINDIVIDUAL_0);// 是否对私付款 默认为对公
                    nisBillheadService.saveNisBillhead(nisBillhead, user);
                    pay.setPayNo(id);
                } else {// 线下还款
                    pay.setStatus(CmsConstant.APPROVED);
                }
                CmsBondIssueInfo issueInfo = cmsBondIssueInfoService.get(cmsBondIssueInfo.getId(), true);
                getSession().flush();
                getSession().clear();
                // 未还本金
                BigDecimal unpaidMoney = this.completeSendBalance(cmsBondIssueInfo.getId(), null);
                // 全部还款完则释放额度
                if (BigDecimal.ZERO.compareTo(unpaidMoney) == 0) {
                    // 如果挂接了保证金,则修改保证金占用信息的状态为"释放"
                    FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(issueInfo.getId(),
                            issueInfo.getBondIssueNum());
                    if (dto != null) {
                        // 不为空说明挂接了保证金
                        dto.setStatus(FcBondConstant.STATUE_RELEASE);
                        dto.setUpdateBy(user.getLoginName());
                        dto.setUpdateDate(DateUtils.getDate());
                        fcBondHookService.updateFcBondApply(dto);
                    }
                    // 抵质押物处理,释放冻结额度
                    if (EmptyUtils.isNotEmpty(issueInfo.getIsHookPledge())
                            && CmsConstant.IS_TRUE.equals(issueInfo.getIsHookPledge())) {
                        pledgeOccupyService.releasePledgeOccupy(issueInfo.getId(), issueInfo.getBondIssueNum());
                    }
                    // 担保处理 0暂存，1在审使用，2使用中，3释放,4打回,5发放打回
                    BondUtils.handleGua(issueInfo, null, guaContractOccupyService, 3, issueInfo.getGuaranteeContract());
                }

                if (EmptyUtils.isNotEmpty(pay.getId())) {
                    /// **借款利息预计明细ID 用来查询还款日期标记。通过还款日期标记查询这个日期内的全部利息明细*/
                    // 归还利息明细之后标记对应的还款计划为已还。不能修改还款计划 并且不能重新生成借款利息预计明细
                    if (pay.getRepayMoney().compareTo(BigDecimal.ZERO) > 0) {// 本金还款才需要更新还款计划未已还
                        CmsBondRepayPlanDtlVo cmsBondRepayPlanDtl = cmsBondRepayPlanDtlService.getByPayId(pay.getId());
                        if(cmsBondRepayPlanDtl != null){
                            CmsBondRepayPlanDtl dtl = cmsBondRepayPlanDtlService.get(cmsBondRepayPlanDtl.getId());
                            //不能刚提交就已还
                            dtl.setIsRepay(CmsConstant.IS_TRUE);// 已还
                            dtl.setPayId(pay.getId());
                            cmsBondRepayPlanDtlService.saveOrUpdate(dtl);
                        }

                    }
                }
            } else {// 打回
                cmsBondIssueInfo.setBizStatus(CmsConstant.BOND_BIZ_STATUS__7);
                pay.setStatus(CmsConstant.DELETED);
                // 债券还款计划明细表状态改为未还
                CmsBondRepayPlanDtlVo cmsBondRepayPlanDtlVo = cmsBondRepayPlanDtlService.getByPayId(pay.getId());
                CmsBondRepayPlanDtl cmsBondRepayPlanDtl = new CmsBondRepayPlanDtl();
                BeanUtils.copyProperties(cmsBondRepayPlanDtlVo, cmsBondRepayPlanDtl);
                cmsBondRepayPlanDtl.setIsRepay(CmsConstant.IS_FALSE);// 未还
                cmsBondPayDao.saveOrUpdate(cmsBondRepayPlanDtl);
                msService.insertProcessMsg(pay.getId(), BondConstant.BONDPAY_BOND_NO,
                        CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_REPAY_CHECKBACK, user);
                msService.insertProcessMsg(pay.getIssueId(), BondConstant.BONDPAY_BOND_NO,
                        CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_REPAY_CHECKBACK, user);
            }
            cmsBondIssueInfoService.saveOrUpdate(cmsBondIssueInfo);
            this.saveOrUpdate(pay);
        }
    }
    /***************** 审批END ********************/
}