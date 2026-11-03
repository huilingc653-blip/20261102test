package com.bytter.finance.domain.bond.service;
import com.bytter.biz.payment.api.constant.ExpenseConstant;
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
import com.bytter.core.constant.NetbankConstant;
import com.bytter.core.utils.*;
import com.bytter.finance.constant.*;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.bond.dao.CmsBondPayDao;
import com.bytter.finance.domain.bond.entity.CmsBondIssueInfo;
import com.bytter.finance.domain.bond.entity.CmsBondPay;
import com.bytter.finance.domain.bond.vo.CmsBondPayVo;
import com.bytter.finance.enums.AllocationMessageEnum;
import com.bytter.finance.enums.CmsBondMessageEnum;
import com.bytter.system.api.IBtBankAccService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
/**
 * @author mof
 * @version 1.0
 * @Title: Service
 * @Description: 债券还息
 * @date 2022-06-06 10:07:46
 */
@WorkFlow(CmsWorkFlowConstant.CMS_BONDPAY_INTEREST)
@DubboService(interfaceClass = IWorkFlowListener.class,group = CmsWorkFlowConstant.CMS_BONDPAY_INTEREST)
@Service("cmsBondPayInterestService")
@Transactional
public class CmsBondPayInterestService extends BaseService<CmsBondPay, String> implements IWorkFlowListener {
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
    @Autowired
    private CmsBondPayService cmsBondPayService;
    @DubboReference(check = false)
    private INisBillheadService nisBillheadService;
    @DubboReference(check = false)
    private IExternalCorpService externalCorpService;
    @DubboReference(check = false)
    private IFcVoucherTypeService fcVoucherTypeService;
    @Autowired
    private CmsBondIssueInfoService cmsBondIssueInfoService;
    @Override
    public BaseDao<CmsBondPay, String> getEntityDao() {
        return cmsBondPayDao;
    }
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuthList = corpService.getUserCorpIds(ResourceConstant.CMS_BOND_CMSBONDPAYINTEREST_QUERY,
                user.getId());
        DataTablesOutput output = cmsBondPayDao.getDataTablesUnion(dataTablesInput, corpAuthList);
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
    public List<CmsBondPayVo> getByRegisterId(String registerId) {
        if (EmptyUtils.isEmpty(registerId)) {
            return null;
        }
        List<CmsBondPayVo> list = cmsBondPayDao.getByRegisterId(registerId, CmsConstant.IS_FALSE);
        return list;
    }
    public List<CmsBondPayVo> getBondPayInterestByRegisterId(String registerId) {
        List<CmsBondPayVo> list = cmsBondPayDao.getBondPayInterestByRegisterId(registerId);
        return list;
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
            cmsBondIssueInfo.setBizStatus(CmsConstant.BOND_BIZ_STATUS_102);
        } else {// 暂存
            cmsBondPay.setStatus(CmsConstant.TEMP);
            cmsBondIssueInfo.setBizStatus(CmsConstant.BOND_BIZ_STATUS_80);
        }
        if (EmptyUtils.isEmpty(cmsBondPay.getId())) {
            cmsBondPay.setRepayTotalMoney(cmsBondPay.getInterest());
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
            cmsBondPay.setRepayTotalMoney(cmsBondPay.getInterest());
            cmsBondPay.setUpdateBy(user.getLoginName());
            cmsBondPay.setUpdateBy(user.getUsername());
            cmsBondPay.setUpdateDate(DateUtils.getDate());
            this.saveOrUpdate(cmsBondPay);
        }
        // 修改债券发行表总状态
        cmsBondIssueInfoService.saveOrUpdate(cmsBondIssueInfo);
        if ("1".equals(tempOrSubmitSign)) {// 处于修改状态标志
            if ("1".equals(saveType)) {// 提交
                msService.insertProcessMsg(vo.getId(), BondConstant.BONDPAY_BOND_NO,
                        CmsProcessMsgConstant.MSGA_REPAIR_MODIFY, CmsProcessMsgConstant.MSGA_REPAY_UPDATE_SUBMIT, user);
                msService.insertProcessMsg(vo.getId(), BondConstant.BONDPAY_BOND_NO,
                        CmsProcessMsgConstant.MSGA_REPAIR_MODIFY, CmsProcessMsgConstant.MSGA_REPAY_UPDATE_SUBMIT, user);
                 getStartHandler().handler(CmsWorkFlowConstant.CMS_BONDPAY_INTEREST, vo.getId(), vo, vo.getCorpId(),
                        user.getId());
            } else {
                msService.insertProcessMsg(vo.getId(), BondConstant.BONDPAY_BOND_NO, CmsProcessMsgConstant.MSGA_MODIFY,
                        CmsProcessMsgConstant.MSGA_REPAY_UPDATE_TEMP, user);
                msService.insertProcessMsg(vo.getId(), BondConstant.BONDPAY_BOND_NO, CmsProcessMsgConstant.MSGA_MODIFY,
                        CmsProcessMsgConstant.MSGA_REPAY_UPDATE_TEMP, user);
            }
        } else if ("0".equals(tempOrSubmitSign) || EmptyUtils.isEmpty(tempOrSubmitSign)) {
            if ("1".equals(saveType)) {// 提交
                msService.insertProcessMsg(vo.getId(), BondConstant.BONDPAY_BOND_NO, CmsProcessMsgConstant.MSGA_SUBMIT,
                        CmsProcessMsgConstant.MSGA_REPAY_SUBMIT, user);
                msService.insertProcessMsg(vo.getId(), BondConstant.BONDPAY_BOND_NO, CmsProcessMsgConstant.MSGA_SUBMIT,
                        CmsProcessMsgConstant.MSGA_REPAY_SUBMIT, user);
                 getStartHandler().handler(CmsWorkFlowConstant.CMS_BONDPAY_INTEREST, vo.getId(), vo, vo.getCorpId(),
                        user.getId());
            } else {
                msService.insertProcessMsg(vo.getId(), BondConstant.BONDPAY_BOND_NO, CmsProcessMsgConstant.MSGA_INPUT,
                        CmsProcessMsgConstant.MSGA_REPAY_TEMP, user);
                msService.insertProcessMsg(vo.getId(), BondConstant.BONDPAY_BOND_NO, CmsProcessMsgConstant.MSGA_INPUT,
                        CmsProcessMsgConstant.MSGA_REPAY_TEMP, user);
            }
        }
    }
    /**
     * 还款申请验证
     */
    private void validata(CmsBondPay cmsBondPay) {
        if (EmptyUtils.isEmpty(cmsBondPay.getInterest())) {
            throw new ParameterException(CmsBondMessageEnum.CMS_BOND_BONDPAY_INTEREST_EMPTY);
        }
        CmsBondPay oldRepay = null;
        if (!EmptyUtils.isEmpty(cmsBondPay.getId())) {
            oldRepay = get(cmsBondPay.getId());
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
    public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService
                .getWorkFlowBusiness(CmsWorkFlowConstant.CMS_BONDPAY_INTEREST, dataTablesInput);
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
        if (CmsWorkFlowConstant.CMS_BONDPAY_INTEREST.equals(processDefinitionKey)) {// 还款申请审批
            if (result) {// 审批通过
                cmsBondIssueInfo.setBizStatus(CmsConstant.BOND_BIZ_STATUS_102);
                msService.insertProcessMsg(pay.getId(), BondConstant.BONDPAY_BOND_NO,
                        CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_REPAY_CHECKOK, user);
                msService.insertProcessMsg(pay.getId(), BondConstant.BONDPAY_BOND_NO,
                        CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_REPAY_CHECKOK, user);
                if (CmsConstant.ONLINE_TRUE.equals(pay.getOnlineSign())) {// 线上还款审批通过
                    pay.setStatus(CmsConstant.APPROVED);
                    // 走付款办理流程
                    NisBillheadDTO nisBillhead = new NisBillheadDTO();
                    String id = UUID.randomUUID().toString().replace("-", "");
                    nisBillhead.setId(id);
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
            } else {// 打回
                cmsBondIssueInfo.setBizStatus(CmsConstant.BOND_BIZ_STATUS__8);
                pay.setStatus(CmsConstant.BACK);
                msService.insertProcessMsg(pay.getId(), BondConstant.BONDPAY_BOND_NO,
                        CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_REPAY_CHECKBACK, user);
                msService.insertProcessMsg(pay.getId(), BondConstant.BONDPAY_BOND_NO,
                        CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_REPAY_CHECKBACK, user);
            }
            cmsBondIssueInfoService.saveOrUpdate(cmsBondIssueInfo);
            this.saveOrUpdate(pay);
        }
    }
    /***************** 审批END ********************/
}
