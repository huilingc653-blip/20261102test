package com.bytter.finance.domain.lease.service;
import com.bytter.biz.payment.api.constant.ExpenseConstant;
import com.bytter.core.constant.NetbankConstant;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.biz.payment.api.service.INisBillheadService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.utils.*;
import com.bytter.finance.constant.*;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.lease.dao.LeasePayDao;
import com.bytter.finance.domain.lease.entity.CmsFinancingLeaseInfo;
import com.bytter.finance.domain.lease.entity.LeaseCapitalPlan;
import com.bytter.finance.domain.lease.entity.LeaseCapitalPlanDel;
import com.bytter.finance.domain.lease.entity.LeasePay;
import com.bytter.finance.domain.lease.vo.LeaseCapitalPlanDelVO;
import com.bytter.finance.domain.lease.vo.LeaseCapitalPlanVO;
import com.bytter.finance.domain.lease.vo.LeasePayVO;
import com.bytter.finance.enums.AllocationMessageEnum;
import com.bytter.finance.enums.CmsLeaseEnum;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IExternalCorpService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.ExternalCorpBankAccDTO;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
/**
 * @author mof
 * @version 1.0
 * @Title: Service
 * @Description: 租赁租金支付表
 * @date 2022-04-15 11:09:43
 */
@Service("leasePayService")
@Transactional
@WorkFlow(value = { CmsWorkFlowConstant.CMS_LEASEPAY_INFO })
@DubboService(interfaceClass = IWorkFlowListener.class,group = CmsWorkFlowConstant.CMS_LEASEPAY_INFO )
public class LeasePayService extends BaseService<LeasePay, String> implements IWorkFlowListener {
    @Autowired
    private LeasePayDao leasePayDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private ICorpService corpServiceImpl;
    @DubboReference(check = false)
    private IConfigService configService;
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
    private LeaseCapitalPlanService leaseCapitalPlanService;
    @Autowired
    private LeaseCapitalPlanDelService leaseCapitalPlanDelService;
    @Autowired
    private CmsFinancingLeaseInfoService financingLeaseInfoService;
    @Override
    public BaseDao<LeasePay, String> getEntityDao() {
        return leasePayDao;
    }
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        List<String> corpAuthList = corpService.getUserCorpIds(ResourceConstant.CMS_LEASE_LEASEPAY_QUERY,
                dataTablesInput.getCurrentUserId());
        // 筛选添加租金计划的数据
        List<String> leaseList = leaseCapitalPlanService.getAllDataByPlan(corpAuthList);
        DataTablesOutput output = leaseCapitalPlanService.getInfoByIds(dataTablesInput, leaseList);
        return output;
    }
    public LeasePayVO getById(String id) {
        if (EmptyUtils.isEmpty(id)) {
            return new LeasePayVO();
        }
        List<LeasePayVO> list = leasePayDao.getById(id);
        if (ListUtils.isNullOrEmpty(list)) {
            return new LeasePayVO();
        }
        return list.get(0);
    }
    public List<LeasePayVO> getByLeaseId(String leaseId) {
        if (EmptyUtils.isEmpty(leaseId)) {
            return null;
        }
        List<LeasePayVO> list = leasePayDao.getByLeaseId(leaseId);
        return list;
    }
    public LeasePayVO getByLeasePlanDelId(String leasePlanDelId) {
        if (EmptyUtils.isEmpty(leasePlanDelId)) {
            return null;
        }
        return leasePayDao.getByLeasePlanDelId(leasePlanDelId);
    }
    public List<LeaseCapitalPlanDelVO> queryInterestList(String leaseId) {
        if (EmptyUtils.isEmpty(leaseId)) {
            new ArrayList<LeaseCapitalPlanDelVO>();
        }
        LeaseCapitalPlan info = leaseCapitalPlanService.get(leaseId);
        List<LeaseCapitalPlanDelVO> leaseCapitalPlanDelVoList = new ArrayList<LeaseCapitalPlanDelVO>();
        if (StringUtils.isNotEmpty(info.getLeaseId())) {
            leaseCapitalPlanDelVoList = leaseCapitalPlanDelService.getInfoByLeaseId(info.getLeaseId());
        }
        return leaseCapitalPlanDelVoList;
    }
    /**
     * 功能介绍 : 查询发放剩余额度（发放金额 - 已还金额）<br>
     *
     * @param leaseId
     *            发放编号
     * @param passageSign
     *            在途标识 1查询在途金额 2已还 其他为 两个之和 <b>日期：</b>2017-8-7<br>
     * @version 1.0
     * @author lvwl
     */
    public BigDecimal completeSendBalance(String leaseId, String passageSign) {
        // 获取总租金
        LeaseCapitalPlanVO leaseCapitalPlanVO = leaseCapitalPlanService.getByLeaseId(leaseId);
        // 获取已还的租金金额
        BigDecimal repayMoney = leasePayDao.getRepayMoneyBySendBillCode(leaseCapitalPlanVO.getId(), passageSign);
        return leaseCapitalPlanVO.getPayMoney().subtract(repayMoney);
    }
    /**
     * 功能介绍 : 暂存或提交付款<br>
     */
    public void tempOrSubmit(LeasePayVO vo, String saveType, String tempOrSubmitSign, ICurrentUser user, String sign) {
        LeasePay leasePay = new LeasePay();
        BeanUtils.copyNotNullProperties(vo, leasePay);
        validata(leasePay);// TODO 验证借款利息预计明细 是否重复选择 和是否最小日期
        // 修改租赁表信息
        CmsFinancingLeaseInfo cmsFinancingLeaseInfo = financingLeaseInfoService.get(vo.getLeaseId());
        if ("1".equals(saveType)) {// 提交
            leasePay.setStatus(CmsConstant.COMMITAPPROVE);
            cmsFinancingLeaseInfo.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_71);
        } else {// 暂存
            leasePay.setStatus(CmsConstant.TEMP);
            cmsFinancingLeaseInfo.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_70);
        }
        if (EmptyUtils.isEmpty(leasePay.getId())) {
            leasePay.setCreateBy(user.getUsername());
            leasePay.setCreateDate(new Date());
            leasePay.setCreateBy(user.getLoginName());
            super.saveEntity(leasePay);
            // 保存租金计划子表
            String id = "";
            id = leasePay.getId();
            vo.setId(id);
        } else {
            leasePay = this.get(leasePay.getId());
            leasePay.setUpdateBy(user.getLoginName());
            leasePay.setUpdateBy(user.getUsername());
            leasePay.setUpdateDate(DateUtils.getDate());
            this.saveOrUpdate(leasePay);
        }
        // 修改租赁表总状态
        financingLeaseInfoService.saveOrUpdate(cmsFinancingLeaseInfo);
        if (EmptyUtils.isNotEmpty(leasePay.getLeasePlanId())) {
            /// **借款利息预计明细ID 用来查询还款日期标记。通过还款日期标记查询这个日期内的全部利息明细*/
            // 归还利息明细之后标记对应的还款计划为已还。不能修改还款计划 并且不能重新生成借款利息预计明细
            if (leasePay.getMoney().compareTo(BigDecimal.ZERO) > 0) {// 本金还款才需要更新还款计划未已还
                LeaseCapitalPlanDel schedule = leaseCapitalPlanDelService.get(leasePay.getLeasePlanDelId());
                schedule.setIsRepay(CmsConstant.IS_TRUE);// 已还
                leasePayDao.saveOrUpdate(schedule);
            }
        }
        if ("1".equals(tempOrSubmitSign)) {// 处于修改状态标志
            if ("1".equals(saveType)) {// 提交
                msService.insertProcessMsg(vo.getLeaseId(), LeaseConstant.LEASEPAY_LEASE_LEASE_NO,
                        CmsProcessMsgConstant.MSGA_REPAIR_MODIFY, CmsProcessMsgConstant.MSGA_REPAY_UPDATE_SUBMIT, user);
                msService.insertProcessMsg(vo.getLeasePlanDelId(), LeaseConstant.LEASEPAY_LEASE_LEASE_NO,
                        CmsProcessMsgConstant.MSGA_REPAIR_MODIFY, CmsProcessMsgConstant.MSGA_REPAY_UPDATE_SUBMIT, user);
                 getStartHandler().handler(CmsWorkFlowConstant.CMS_LEASEPAY_INFO, vo.getId(), vo, vo.getCorpId(),
                        user.getId());
            } else {
                msService.insertProcessMsg(vo.getLeaseId(), LeaseConstant.LEASEPAY_LEASE_LEASE_NO,
                        CmsProcessMsgConstant.MSGA_MODIFY, CmsProcessMsgConstant.MSGA_REPAY_UPDATE_TEMP, user);
                msService.insertProcessMsg(vo.getLeasePlanDelId(), LeaseConstant.LEASEPAY_LEASE_LEASE_NO,
                        CmsProcessMsgConstant.MSGA_MODIFY, CmsProcessMsgConstant.MSGA_REPAY_UPDATE_TEMP, user);
            }
        } else if ("0".equals(tempOrSubmitSign) || EmptyUtils.isEmpty(tempOrSubmitSign)) {
            if ("1".equals(saveType)) {// 提交
                msService.insertProcessMsg(vo.getLeaseId(), LeaseConstant.LEASEPAY_LEASE_LEASE_NO,
                        CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_REPAY_SUBMIT, user);
                msService.insertProcessMsg(vo.getLeasePlanDelId(), LeaseConstant.LEASEPAY_LEASE_LEASE_NO,
                        CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_REPAY_SUBMIT, user);
                 getStartHandler().handler(CmsWorkFlowConstant.CMS_LEASEPAY_INFO, vo.getId(), vo, vo.getCorpId(),
                        user.getId());
            } else {
                msService.insertProcessMsg(vo.getLeaseId(), LeaseConstant.LEASEPAY_LEASE_LEASE_NO,
                        CmsProcessMsgConstant.MSGA_INPUT, CmsProcessMsgConstant.MSGA_REPAY_TEMP, user);
                msService.insertProcessMsg(vo.getLeasePlanDelId(), LeaseConstant.LEASEPAY_LEASE_LEASE_NO,
                        CmsProcessMsgConstant.MSGA_INPUT, CmsProcessMsgConstant.MSGA_REPAY_TEMP, user);
            }
        }
    }
    /**
     * 还款申请验证
     */
    private void validata(LeasePay leasePay) {
        if (leasePay.getPayDate() == null) {
            throw new ParameterException(CmsLeaseEnum.CMS_LEASE_LEASEPAY_PAYDATE_EMPTY);
        }
        if (EmptyUtils.isEmpty(leasePay.getMoney())) {
            throw new ParameterException(CmsLeaseEnum.CMS_LEASE_LEASEPAY_MONEYT_EMPTY);
        }
        // 租金（总金额）
        if (EmptyUtils.isEmpty(leasePay.getLeaseMoney())) {
            throw new ParameterException(CmsLeaseEnum.CMS_LEASE_LEASEPAY_LEASEMONEY_EMPTY);
        }
        // 还款金额不能超过 待发放金额
        BigDecimal sendBalance = completeSendBalance(leasePay.getLeaseId(), null);
        LeasePay oldRepay = null;
        if (!EmptyUtils.isEmpty(leasePay.getId())) {
            oldRepay = get(leasePay.getId());
            sendBalance = sendBalance.add(oldRepay.getMoney());
        }
        if (leasePay.getMoney().compareTo(sendBalance) == 1) {
            throw new ParameterException(CmsLeaseEnum.CMS_LEASE_LEASEPAY_MONEY_SENDBALANCE);
        }
        // 线上还款的必填项 验证
        if (CmsConstant.ONLINE_TRUE.equals(leasePay.getOnlineSign())) {
            /*
             * if (EmptyUtils.isEmpty(leasePay.getInterest())) { throw new
             * ParameterException(CmsLeaseEnum.CMS_LEASE_LEASEPAY_INTEREST_EMPTY); }
             */
            if (EmptyUtils.isEmpty(leasePay.getPurpose())) {
                throw new ParameterException(CmsLeaseEnum.CMS_LEASE_LEASEPAY_PURPOSE_EMPTY);
            }
            if (EmptyUtils.isEmpty(leasePay.getOnlinePaymentAcc())) {
                throw new ParameterException(CmsLeaseEnum.CMS_LEASE_LEASEPAY_ONLINEPAYMENTACC_EMPTY);
            }
            if (EmptyUtils.isEmpty(leasePay.getOnlineAccName())) {
                throw new ParameterException(CmsLeaseEnum.CMS_LEASE_LEASEPAY_ONLINEACCNAME_EMPTY);
            }
            if (EmptyUtils.isEmpty(leasePay.getOnlineBankName())) {
                throw new ParameterException(CmsLeaseEnum.CMS_LEASE_LEASEPAY_ONLINEBANKNAME_EMPTY);
            }
            if (!EmptyUtils.isEmpty(leasePay.getId())) {
                if (CmsConstant.ONLINE_TRUE.equals(oldRepay.getOnlineSign())
                        && CmsConstant.ONLINE_FALSE.equals(leasePay.getOnlineSign())) {
                    if (CmsConstant.FLOWWAIT != oldRepay.getStatus()) {
                        throw new ParameterException(AllocationMessageEnum.NIS_ALLOCATION_MESSAGE_STATUS_DELETE);
                    }
                }
            }
        }
    }
    /**
     * 生成还款单号
     */
    private String getLoanRepayBillCode() {
        return SequenceSingleton.getSequence(SequenceEnum.CMS_PROVIDE_LOANREPAY_BILL_NO,
                ProvideConstant.REPAY_BILL_CODE_PREFIX);
    }
    /**
     * 功能介绍 : 还款申请审批列表 - 分页<br>
     */
    @Transactional(readOnly = true)
    public DataTablesOutput dataTableLeasePayApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(CmsWorkFlowConstant.CMS_LEASEPAY_INFO,
                dataTablesInput);
        DataTablesOutput dp = leasePayDao.dataTableLeasePayApprove(dataTablesInput,
                workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }
    /***************** 审批BEGIN ********************/
    /**
     * 审批启动完成 执行
     */
    public void flowStarted(IWorkFlowResult workFlowResult) {
        // TODO Auto-generated method stub
    }
    /**
     * 审批流程结束 执行
     */
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();
        String processDefinitionKey = workFlowResult.getProcessDefinitionKey();
        boolean result = workFlowResult.isPass();
        ICurrentUser user = workFlowResult.getUser();
        LeasePay leasePay = this.get(businessKey,true);
        // 修改租赁表信息
        CmsFinancingLeaseInfo cmsFinancingLeaseInfo = financingLeaseInfoService.get(leasePay.getLeaseId());
        if (CmsWorkFlowConstant.CMS_LEASEPAY_INFO.equals(processDefinitionKey)) {// 还款申请审批
            if (result) {// 审批通过
                cmsFinancingLeaseInfo.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_101);
                msService.insertProcessMsg(leasePay.getLeaseId(), LeaseConstant.LEASEPAY_LEASE_LEASE_NO,
                        CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_REPAY_CHECKOK, user);
                msService.insertProcessMsg(leasePay.getLeaseId(), LeaseConstant.LEASEPAY_LEASE_LEASE_NO,
                        CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_REPAY_CHECKOK, user);
                if (CmsConstant.ONLINE_TRUE.equals(leasePay.getOnlineSign())) {// 线上还款审批通过
                    leasePay.setStatus(CmsConstant.APPROVED);
                    // 走付款办理流程
                    NisBillheadDTO nisBillhead = new NisBillheadDTO();
                    String id = UUID.randomUUID().toString().replace("-", "");
                    nisBillhead.setId(id);
                    nisBillhead.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_BILLHEAD));
                    CorpDTO corp = corpServiceImpl.get(leasePay.getCorpId());
                    nisBillhead.setCorpId(leasePay.getCorpId());// 单位
                    nisBillhead.setCorpName(corp.getName());// 单位
                    BtBankAccDTO bank = iBtBankAccService.getBankAccInfo(leasePay.getOnlinePaymentAcc());
                    if (!StringUtils.isNotEmpty(bank)) {
                        List<String> acclist = new ArrayList<>();
                        acclist.add(leasePay.getOnlinePaymentAcc());
                        // 根据账号查询客商账户信息
                        ExternalCorpBankAccDTO externalCorpBankAcc = externalCorpService
                                .getExternalCorpBankAccByIdAndAcc(null, leasePay.getOnlinePaymentAcc());
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
                    nisBillhead.setPayeeAcc(leasePay.getOnlinePaymentAcc());// 账户
                    nisBillhead.setPayeeAccName(leasePay.getOnlineAccName());// 账户名称
                    nisBillhead.setPayeeBank(leasePay.getOnlineBankName());// 开户行名称
                    nisBillhead.setPayMoney(leasePay.getMoney());// 金额
                    nisBillhead.setWishPayday(leasePay.getPayDate());// 支付日期
                    // 根据调拨类型设置结算方式
                    FcVoucherTypeDTO voucherType = null;
                    voucherType = fcVoucherTypeService.getSysFcVoucherType(NetbankConstant.PAY_TYPE_34);
                    if (voucherType != null) {
                        nisBillhead.setVoucherTypeId(voucherType.getId());// 付款方式默认外付网银
                    }
                    nisBillhead.setPurpose(leasePay.getPurpose());// 用途
                    nisBillhead.setStatus(ExpenseConstant.STATUE_TEMP);// 状态
                    nisBillhead.setBillFrom(NetbankConstant.BILLFROM_9);// 单据来源:1-支付结算
                    nisBillhead.setIsPayed(NetbankConstant.ISPAYED_0); // 是否已付款标志 默认否
                    nisBillhead.setIsQuick(NetbankConstant.ISQUICK_0);// 加急标记默认为不加急
                    nisBillhead.setIsForindividual(NetbankConstant.FORINDIVIDUAL_0);// 是否对私付款 默认为对公
                    nisBillheadService.saveNisBillhead(nisBillhead, user);
                    // 计划子表添加支付编号
                    LeaseCapitalPlanDel delVo = leaseCapitalPlanDelService.get(leasePay.getLeasePlanDelId());
                    delVo.setPayNo(id);
                    leaseCapitalPlanDelService.saveOrUpdate(delVo);
                } else {// 线下还款
                    leasePay.setStatus(CmsConstant.APPROVED);
                }
            } else {// 打回
                cmsFinancingLeaseInfo.setBizStatus(CmsConstant.LEASE_BIZ_STATUS__8);
                leasePay.setStatus(CmsConstant.BACK);
                // 租赁租金计划明细表状态改为未还
                LeaseCapitalPlanDel schedule = leaseCapitalPlanDelService.get(leasePay.getLeasePlanDelId());
                schedule.setIsRepay(CmsConstant.IS_FALSE);// 未还
                leasePayDao.saveOrUpdate(schedule);
                msService.insertProcessMsg(leasePay.getLeaseId(), LeaseConstant.LEASEPAY_LEASE_LEASE_NO,
                        CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_REPAY_CHECKBACK, user);
                msService.insertProcessMsg(leasePay.getLeasePlanDelId(), LeaseConstant.LEASEPAY_LEASE_LEASE_NO,
                        CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_REPAY_CHECKBACK, user);
            }
            financingLeaseInfoService.saveOrUpdate(cmsFinancingLeaseInfo);
            this.saveOrUpdate(leasePay);
        }
    }
    /***************** 审批END ********************/
}
