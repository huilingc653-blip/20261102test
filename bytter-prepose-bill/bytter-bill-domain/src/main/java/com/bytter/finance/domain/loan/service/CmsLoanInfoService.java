package com.bytter.finance.domain.loan.service;

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
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.credit.api.IRatCpUrationService;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.finance.constant.*;
import com.bytter.finance.domain.basedata.dao.RepaymentScheduleDao;
import com.bytter.finance.domain.basedata.entity.CmsRepaymentSchedule;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.basedata.service.LoanRateMaintenceService;
import com.bytter.finance.domain.basedata.service.LoanTypeService;
import com.bytter.finance.domain.basedata.service.RepaymentScheduleService;
import com.bytter.finance.domain.loan.dao.CmsLoanInfoDao;
import com.bytter.finance.domain.loan.dao.CmsLoanSendDao;
import com.bytter.finance.domain.loan.entity.CmsContract;
import com.bytter.finance.domain.loan.entity.CmsLoanInfo;
import com.bytter.finance.domain.loan.entity.CmsLoanSend;
import com.bytter.finance.domain.loan.vo.CmsLoanInfoVO;
import com.bytter.finance.domain.loan.vo.CmsLoanSendVO;
import com.bytter.finance.domain.loan.vo.GuaContractCmsLoanVO;
import com.bytter.finance.dto.CmsLoanInfoDTO;
import com.bytter.finance.dto.GuaContractCmsLoanDTO;
import com.bytter.finance.dto.LoanTypeDTO;
import com.bytter.finance.enums.BaseDataMessageEnum;
import com.bytter.finance.enums.LoanMessageEnum;
import com.bytter.finance.enums.ProvideMessageEnum;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.constant.GuaranteeConstant;
import com.bytter.guarantee.domain.service.impl.ConCommonService;
import com.bytter.guarantee.dto.ConInfoDTO;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.*;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 贷款信息表
 * @date 2017-07-21 09:46:16
 */
@WorkFlow({CmsWorkFlowConstant.CMS_LOAN_INFO, CmsWorkFlowConstant.CMS_LOAN_REPAIR})
@Service("cmsLoanInfoService")
@DubboService(interfaceClass = IWorkFlowListener.class, group = "cmsLoanWorkflow")
@Transactional
public class CmsLoanInfoService extends BaseService<CmsLoanInfo, String>
        implements IWorkFlowListener, ICorpDeleteVerifyService, ICorpDisableVerifyService {
    @Autowired
    private CmsLoanInfoDao cmsLoanInfoDao;
    @Autowired
    private CmsLoanSendDao cmsLoanSendDao;
    @Autowired
    private LoanRateMaintenceService loanRateMaintenceService;
    @DubboReference(check = false)
    private ICorpService iCorpService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @Autowired
    private LoanTypeService loanTypeService;
    @Autowired
    private IWorkFlowService workFlowService;
    @Autowired
    private CmsProcessMsgService msService;
    @DubboReference(check = false)
    private IBtPurposeService btPurposeService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private IConfigService configService;
    @Autowired
    private CmsLoanSendService cmsLoanSendService;
    @Autowired
    private CmsContractService cmsContractService;
    @Autowired
    private RepaymentScheduleDao repaymentScheduleDao;
    @Autowired
    private IRatCpUrationService ucprationService;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie contractOccupServie;
    @Autowired
    private RepaymentScheduleService repaymentScheduleService;
    @Autowired
    private ConCommonService conCommonService;
    @DubboReference(check = false)
    private IBtCurrencyService iBtCurrencyService;
    @DubboReference(check = false)
    private IDictService dictService;
    @DubboReference(check = false)
    private IUserService userService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
    private IBaseRateService iBaseRateService;
    @DubboReference(check = false)
    private ICreditPeriodService iCreditPeriodService;
    @DubboReference(check = false)
    private ICorpService corpService;

    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(CmsLoanInfo entity) {
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
    public void save(CmsLoanInfo entity) {
        super.saveEntity(entity);
    }

    /**
     * 更新
     *
     * @param entity
     */
    public void update(CmsLoanInfo entity) {
        super.updateEntity(entity);
    }

    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(CmsLoanInfo entity) {
        super.saveOrUpdate(entity);
    }

    public BaseDao<CmsLoanInfo, String> getEntityDao() {
        return cmsLoanInfoDao;
    }

    /**
     * <b>功能介绍：</b>页面列表数据<br>
     * <b>日期：</b>2017年7月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getEditDataTables(DataTablesInput input, String pageType, String userId, String source) {
        List<String> corpAuth = iCorpService.getUserCorpIds(source, userId);
        return cmsLoanInfoDao.listCmsLoanInfo(input, pageType, corpAuth, userId);
    }

    public void repairBackInsertMsg(CmsLoanInfo cmsLoanInfo, ICurrentUser user) {
        msService.insertProcessMsg(cmsLoanInfo.getBillCode(), CmsConstant.LOAN_INFO_BILL_CODE_PREFIX,
                CmsProcessMsgConstant.MSGA_REPAIRBACK, CmsProcessMsgConstant.MSGA_REPAIR_BACK, user);
    }

    public void sendBackInsertMsg(CmsLoanInfo cmsLoanInfo, ICurrentUser user) {
        msService.insertProcessMsg(cmsLoanInfo.getBillCode(), CmsConstant.LOAN_INFO_BILL_CODE_PREFIX,
                CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_SEND_BACK, user);
    }

    /**
     * <b>功能介绍：</b>保存贷款申请表<br>
     * <b>日期：</b>2017年8月10日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public void saveLoanInfo(CmsLoanInfoVO cmsLoanInfoVO, ICurrentUser user, RatUrationDTO urationDTO) {
        CmsLoanInfo cmsLoanInfo = new CmsLoanInfo();
        //因为这里的corpName 和 corpId 不是对应关系，这里进行修正
        if (StringUtils.isNotBlank(cmsLoanInfoVO.getCorpId())) {
            CorpDTO corpDTO = corpService.get(cmsLoanInfoVO.getCorpId());
            cmsLoanInfoVO.setCorpName(corpDTO.getName());
        }
        BeanUtils.copyNotNullProperties(cmsLoanInfoVO, cmsLoanInfo);
        if (cmsLoanInfo.getStatus() == 1) {// 提交
            cmsLoanInfo.setStatus(CmsConstant.COMMITAPPROVE);
            cmsLoanInfo.setBizStatus(CmsConstant.BUSINESSSTATE_15);// 待审批
        } else {
            cmsLoanInfo.setStatus(CmsConstant.TEMP);
            cmsLoanInfo.setBizStatus(CmsConstant.BUSINESSSTATE_10);// 暂存
        }
        // String bill_code = getLoanInfoBillCode(CmsConstant.LOAN_INFO_BILL_CODE_PREFIX);//生成编号
        // cmsLoanInfo.setBillCode(bill_code);
        cmsLoanInfo.setCreateDate(new Date());
        setCreateUpdateInfo(1, cmsLoanInfo, user);
        super.saveEntity(cmsLoanInfo);
        this.getEntityDao().flush();
        // 授信挂接
        if (!EmptyUtils.isEmpty(cmsLoanInfo.getIsHangCredit())
                && CmsConstant.IS_TRUE.equals(cmsLoanInfo.getIsHangCredit())) {
            cmsLoanInfoVO.setId(cmsLoanInfo.getId());
            cmsLoanInfoVO.setBillCode(cmsLoanInfo.getBillCode());
            setCmsLoanRatUrationDTO(cmsLoanInfoVO, urationDTO);
            if (cmsLoanInfo.getStatus().equals(CmsConstant.COMMITAPPROVE)) {// 提交
                ucprationService.addUseCpRation(urationDTO);// 授信提交后冻结额度
            } else if (cmsLoanInfo.getStatus().equals(CmsConstant.TEMP)) {// 暂存
                ucprationService.tempUseCpRation(urationDTO);// 授信暂存
            }
        } else {
            if (!EmptyUtils.isEmpty(cmsLoanInfo.getId())) {// 如果之前挂了授信,删除授信占用信息
                ucprationService.deleteUseCpRation(urationDTO);
            }
        }
        // 担保挂接
        if (!EmptyUtils.isEmpty(cmsLoanInfo.getIsHangGuarantee())
                && CmsConstant.IS_TRUE.equals(cmsLoanInfo.getIsHangGuarantee())) {
            this.saveGuaContract(cmsLoanInfo, cmsLoanInfoVO);
        }
        if ("1".equals(cmsLoanInfoVO.getIsOverDistribute()) && "1".equals(cmsLoanInfoVO.getIsOverRepair())) {// 跳过补录发放，必须维护还款计划
            if (EmptyUtils.isEmpty(cmsLoanInfoVO.getPlanId())) {
                throw new BusinessException(LoanMessageEnum.CMS_LOANAPPLY_PLAN_ISNULL);
            }
        }
        // 还款计划挂接
        if (StringUtils.isEmpty(cmsLoanInfoVO.getPlanId())) {
        } else if ("1".equals(cmsLoanInfoVO.getPlanStatus())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANSEND_REPAYMENTSCHEDULRESAVE);
        } else {
            repaymentScheduleDao.updateRepayMentBill(cmsLoanInfoVO.getPlanId(), CmsConstant.LOAN_INFO_BILL_CODE_PREFIX,
                    cmsLoanInfo.getId());// 更新还款计划的编号和类型
        }
        if (cmsLoanInfo.getStatus() == CmsConstant.COMMITAPPROVE) {
            msService.insertProcessMsg(cmsLoanInfo.getBillCode(), CmsConstant.LOAN_INFO_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_INPUT_SUBMIT, user);
            loanInfoApply(cmsLoanInfo, user);// 审批处理
        } else {
            msService.insertProcessMsg(cmsLoanInfo.getBillCode(), CmsConstant.LOAN_INFO_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_INPUT, CmsProcessMsgConstant.MSGA_INPUT + "", user);
        }
    }

    /**
     * 贷款数据转换为授信接口数据
     */
    private void setCmsLoanRatUrationDTO(CmsLoanInfoVO cmsLoanInfoVO, RatUrationDTO urationDTO) {
        urationDTO.setCurCode(cmsLoanInfoVO.getCurCode());// 币别ID
        urationDTO.setBillId(cmsLoanInfoVO.getId());// 业务ID
        urationDTO.setBillNo(cmsLoanInfoVO.getBillCode());// 业务编号
        urationDTO.setSysCode(ConfigConstant.CMS);// 信贷
        urationDTO.setBusinessName(RationConstant.BUSINESS_NAME_02);// 信贷占用
        urationDTO.setContactNo(cmsLoanInfoVO.getContractCode());// 合同号
        urationDTO.setBeginDate(cmsLoanInfoVO.getLoanDate());// 贷款开始日期
        urationDTO.setEndDate(cmsLoanInfoVO.getRepayDate());// 贷款还款日期
        urationDTO.setMoney(cmsLoanInfoVO.getLoanMoney());// 贷款金额
    }

    /**
     * 查询审批通过的担保合同列表
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
//    @Transactional(readOnly = true)
//    public DataTablesOutput getApprovedDataTables(DataTablesInput input, String corpId) {
//        DataTablesOutput output = cmsLoanInfoDao.getApprovedDataTableCheck(input, corpId);
//        return output;
//    }
    /**
     * <b>功能介绍：</b>更新贷款申请表<br>
     * <b>日期：</b>2017年8月10日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public void updateLoanInfo(CmsLoanInfoVO cmsLoanInfoVO, ICurrentUser user, RatUrationDTO urationDTO) {
        cmsLoanInfoVO.setLoanMoney(new BigDecimal(cmsLoanInfoVO.getLoanMoney().doubleValue()));
        BtCurrencyDTO currency = iBtCurrencyService.getObjectById(cmsLoanInfoVO.getCurCode());
        cmsLoanInfoVO.setCurName(currency.getCurName());
        CmsLoanInfo t = this.get(cmsLoanInfoVO.getId(), true);
        BeanUtils.copyNotNullProperties(cmsLoanInfoVO, t);
        copyNullProperties(cmsLoanInfoVO, t);// 非必填属性可修改为空
        Integer opType = cmsLoanInfoVO.getStatus();
        if (opType == 1) {// 提交
            t.setStatus(CmsConstant.COMMITAPPROVE);
            t.setBizStatus(CmsConstant.BUSINESSSTATE_15);
        } else if (opType == 0) {// 暂存
            t.setStatus(CmsConstant.TEMP);
            t.setBizStatus(CmsConstant.BUSINESSSTATE_10);
        } else if (opType == 2) {// 补录
            t.setStatus(CmsConstant.COMMITAPPROVE);
            t.setBizStatus(CmsConstant.BUSINESSSTATE_25);
            t.setRepairOp(user.getLoginName());
            // 补录时判断贷款合同号是否已存在
            if (EmptyUtils.isNotEmpty(cmsLoanInfoVO.getContractCode())
                    && existsContractCode(cmsLoanInfoVO.getContractCode(), t.getBillCode())) {
                throw new ParameterException(LoanMessageEnum.CMS_LOANINFO_CONTRACTCODE_EXISTS);
            }
            if (CmsConstant.RELATED_CONTRACT_NO.equals(getRelatedContract().getValue())) { // 为对接合同管理，置空相关信息
                t.setConId("");
                t.setConTypeId("");
            }
        } else if (cmsLoanInfoVO.getStatus() == -1) {// 打回
            t.setStatus(CmsConstant.BACK);
            t.setBizStatus(CmsConstant.BUSINESSSTATE_12);
        }
        super.saveOrUpdate(t);
        this.getEntityDao().flush();
        // 授信挂接
        if (!EmptyUtils.isEmpty(cmsLoanInfoVO.getIsHangCredit())
                && CmsConstant.IS_TRUE.equals(cmsLoanInfoVO.getIsHangCredit())) {
            cmsLoanInfoVO.setBillCode(t.getBillCode());
            setCmsLoanRatUrationDTO(cmsLoanInfoVO, urationDTO);
            if (opType == 2) {// 在补录时提交,授信的状态变化
                RatUrationDTO oriUrationDTO = new RatUrationDTO(ConfigConstant.CMS, cmsLoanInfoVO.getId());
                oriUrationDTO.setAllRelease(true);
                ucprationService.backUseCpRation(oriUrationDTO);// 释放原授信在审占用
                ucprationService.addUseCpRation(urationDTO);// 冻结授信额度
            } else {// 修改数据时，暂存或提交操作授信的保存方法
                if (t.getStatus().equals(CmsConstant.COMMITAPPROVE)) {
                    ucprationService.addUseCpRation(urationDTO);// 冻结授信额度
                } else if (t.getStatus().equals(CmsConstant.TEMP)) {
                    ucprationService.tempUseCpRation(urationDTO);// 暂存授信
                }
            }
        } else {
            if (!EmptyUtils.isEmpty(cmsLoanInfoVO.getId())) {// 如果之前挂了授信，删除挂接信息
                ucprationService.deleteUseCpRation(urationDTO);
            }
        }
        // if (!"01".equals(cmsLoanInfo.getFloatWay())) {//非固定利率
        // LoanRateMaintenceVO rate =
        // cmsPrecativeLoanInfoService.getRate(DateUtils.formatDate(cmsLoanInfo.getLoanDate(),
        // "yyyy-MM-dd"),cmsLoanInfo.getReferenceRateType(),cmsLoanInfo.getCurCode(),cmsLoanInfo.getRepayCycle());
        // // cmsLoanInfo.setBenchmarkRate(rate.getRate());//设置基础利率,没有这个字段
        // }
        if ("1".equals(cmsLoanInfoVO.getIsOverDistribute())) {// 跳过发放，必须维护还款计划
            if (EmptyUtils.isEmpty(cmsLoanInfoVO.getPlanId())) {
                throw new BusinessException(LoanMessageEnum.CMS_LOANAPPLY_PLAN_ISNULL);
            }
        }
        // 还款计划挂接
        if (StringUtils.isEmpty(cmsLoanInfoVO.getPlanId())) {
        } else if ("1".equals(cmsLoanInfoVO.getPlanStatus())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANSEND_REPAYMENTSCHEDULRESAVE);
        } else {
            repaymentScheduleDao.updateRepayMentBill(cmsLoanInfoVO.getPlanId(), CmsConstant.LOAN_INFO_BILL_CODE_PREFIX,
                    cmsLoanInfoVO.getId());// 更新还款计划的编号和类型
        }
        // 担保挂接
        if (!EmptyUtils.isEmpty(cmsLoanInfoVO.getIsHangGuarantee())
                && CmsConstant.IS_TRUE.equals(cmsLoanInfoVO.getIsHangGuarantee())) {
            this.saveGuaContract(t, cmsLoanInfoVO);
        } else if (!EmptyUtils.isEmpty(cmsLoanInfoVO.getIsHangGuarantee())
                && CmsConstant.IS_FALSE.equals(cmsLoanInfoVO.getIsHangGuarantee())) {
            contractOccupServie.deleteGuaContractOccup(cmsLoanInfoVO.getId(), ConfigConstant.LOAN);
        }
        // 完成通知
        notificationService.complete(NotificationConstant.CMS.LOAN_ADD, t.getId(), user.getId());
        notificationService.complete(NotificationConstant.CMS.LOAN_REPAIR, t.getId(), user.getId());
        if (opType == 1) {
            msService.insertProcessMsg(t.getBillCode(), CmsConstant.LOAN_INFO_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_REPAIR_MODIFY, CmsProcessMsgConstant.MSGA_REPAIR_MODIFY + "", user);
            loanInfoApply(t, user);// 新增审批处理
        } else if (opType == 2) {
            msService.insertProcessMsg(t.getBillCode(), CmsConstant.LOAN_INFO_BILL_CODE_PREFIX,
                    Integer.valueOf(CmsProcessMsgConstant.MSGA_REPAIROP), CmsProcessMsgConstant.MSGA_REPAIROP, user);
            loanInfoRepairApply(t, user);// 补录审批处理
        } else if (opType == -1) {// 打回
            msService.insertProcessMsg(t.getBillCode(), CmsConstant.LOAN_INFO_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_REPAIRBACK, CmsProcessMsgConstant.MSGA_REPAIR_BACK, user);
        } else if (opType == 0) {// 暂存
            msService.insertProcessMsg(t.getBillCode(), CmsConstant.LOAN_INFO_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_MODIFY, CmsProcessMsgConstant.MSGA_MODIFY + "", user);
        }
    }

    /**
     * <b>功能介绍：</b>将非必填属性可设为空<br>
     * <b>日期：</b>2017年9月11日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param source
     * @param target
     * @version 1.0
     */
    public void copyNullProperties(CmsLoanInfoVO source, CmsLoanInfo target) {
        target.setLoanRate(source.getLoanRate());// 贷款利率
        target.setSuretyMoney(source.getSuretyMoney());// 担保品价值
        target.setSuretyExplain(source.getSuretyExplain());// 担保说明
        target.setRmk(source.getRmk());// 备注
    }

    /**
     * <b>功能介绍：</b>新增审批处理方法<br>
     * <b>日期：</b>2017年8月18日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public void loanInfoApply(CmsLoanInfo loanInfo, ICurrentUser user) {
        getStartHandler().handler(CmsWorkFlowConstant.CMS_LOAN_INFO, loanInfo.getId(), loanInfo, loanInfo.getCorpId(),
                user.getId());
    }

    /**
     * <b>功能介绍：</b>补录审批处理方法<br>
     * <b>日期：</b>2017年8月18日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public void loanInfoRepairApply(CmsLoanInfo loanInfo, ICurrentUser user) {
        getStartHandler().handler(CmsWorkFlowConstant.CMS_LOAN_REPAIR, loanInfo.getId(), loanInfo, loanInfo.getCorpId(),
                user.getId());
    }

    /**
     * <b>功能介绍：</b>贷款待发放列表数据<br>
     * <b>日期：</b>2017年8月8日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Transactional(readOnly = true)
    public DataTablesOutput listCmsLoanStaySendData(DataTablesInput input, String userId) {
        List<String> corpAuth = iCorpService.getUserCorpIds(ResourceConstant.CMS_LOAN_SEND_QUERY, userId);
        DataTablesOutput result = cmsLoanInfoDao.listCmsLoanStaySendData(input, corpAuth);
        List<CmsLoanInfoVO> loanInfos = result.getData();
        for (CmsLoanInfoVO loanInfo : loanInfos) {// 设置放款次数
            loanInfo.setSendTime(cmsLoanSendDao.getCmsLoanSendCountByloanCode(loanInfo.getBillCode()));
        }
        result.setData(loanInfos);
        return result;
    }

    /**
     * <b>功能介绍：</b>加载审批列表数据<br>
     * <b>日期：</b>2017年8月18日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Transactional(readOnly = true)
    public DataTablesOutput listAddApprove(DataTablesInput dataTablesInput, String pageType) {
        String flowType = CmsWorkFlowConstant.CMS_LOAN_INFO;
        if ("repair".equals(pageType)) {// 补录流程
            flowType = CmsWorkFlowConstant.CMS_LOAN_REPAIR;
        }
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(flowType, dataTablesInput);
        DataTablesOutput dp = cmsLoanInfoDao.dataTableApprove(dataTablesInput, workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }

    /**
     * <b>功能介绍：</b>生成贷款编号<br>
     * <b>日期：</b>2017年7月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public String getLoanInfoBillCode(String prefix) {
        return SequenceSingleton.getSequence(SequenceEnum.CMS_LOANINFO_ORDER_NO, prefix);
    }

    /**
     * <b>功能介绍：</b>查询贷款类型<br>
     * <b>日期：</b>2017年7月21日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public List<LoanTypeDTO> queryLoanTypeList(String type) {
        return loanTypeService.queryLoanTypeList(type);
    }

    /**
     * <b>功能介绍：</b>根据ID查询贷款申请单<br>
     * <b>日期：</b>2017年7月25日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public CmsLoanInfoVO getCmsLoanInfoById(String id) {
        CmsLoanInfoVO cmsLoanInfo = cmsLoanInfoDao.getCmsLoanInfoById(id);
        //TODO 统一处理期限与利率
        Optional.ofNullable(cmsLoanInfo).ifPresent(cmsLoan -> {
            if (StringUtils.isNotEmpty(cmsLoan.getReferenceRateType())) {
                TbBaseRateDTO baseRateDTO = iBaseRateService.getBaseRateById(cmsLoan.getReferenceRateType());
                Optional.ofNullable(baseRateDTO).ifPresent(rate -> cmsLoanInfo.setReferenceRateTypeName(rate.getBaseTypeName()));
            }
            if (StringUtils.isNotEmpty(cmsLoan.getRepayCycle())) {
                TbCreditPeriodDTO creditPeriodDTO = iCreditPeriodService.getById(cmsLoan.getRepayCycle());
                Optional.ofNullable(creditPeriodDTO).ifPresent(period -> cmsLoanInfo.setRepayCycleName(period.getPeriodName()));
            }
            if (StringUtils.isEmpty(String.valueOf(cmsLoan.getLoanRate()))) {
                TbRateMaintenceDTO tbLoanRateMaintence = loanRateMaintenceService.getLoanRateMaintence(cmsLoan.getCurCode(),
                        cmsLoan.getReferenceRateType(), cmsLoan.getRepayCycle());
                Optional.ofNullable(tbLoanRateMaintence).ifPresent(period -> cmsLoanInfo.setLoanRate(period.getRate()));
            }
            if (StringUtils.isNotEmpty(cmsLoan.getFloatWay())) {
                cmsLoanInfo.setFloatWayName(dictService.get("Cms.FloatWay", cmsLoan.getFloatWay()));
            }
            if (StringUtils.isNotEmpty(cmsLoan.getLoanClass())) {
                cmsLoanInfo.setLoanClassName(dictService.get("cms.LoanClass", cmsLoan.getLoanClass()));
            }

        });
        return cmsLoanInfo;
    }

    @Transactional(readOnly = true)
    public CmsLoanInfoVO getLoanInfoByBillCode(String billCode) {
        CmsLoanInfoVO cmsLoanInfo = cmsLoanInfoDao.getLoanInfoByBillCode(billCode);
        //TODO 补充利率相关信息
        Optional.ofNullable(cmsLoanInfo).ifPresent(cmsLoan -> {
            if (StringUtils.isNotEmpty(cmsLoan.getReferenceRateType())) {
                TbBaseRateDTO baseRateDTO = iBaseRateService.getBaseRateById(cmsLoan.getReferenceRateType());
                Optional.ofNullable(baseRateDTO).ifPresent(rate -> cmsLoanInfo.setReferenceRateTypeName(rate.getBaseTypeName()));
            }
            if (StringUtils.isNotEmpty(cmsLoan.getRepayCycle())) {
                TbCreditPeriodDTO creditPeriodDTO = iCreditPeriodService.getById(cmsLoan.getRepayCycle());
                Optional.ofNullable(creditPeriodDTO).ifPresent(period -> cmsLoanInfo.setRepayCycleName(period.getPeriodName()));
            }
            if (StringUtils.isNotEmpty(cmsLoan.getFloatWay())) {
                cmsLoanInfo.setFloatWayName(dictService.get("Cms.FloatWay", cmsLoan.getFloatWay()));
            }
            if (StringUtils.isNotEmpty(cmsLoan.getLoanClass())) {
                cmsLoanInfo.setLoanClassName(dictService.get("cms.LoanClass", cmsLoan.getLoanClass()));
            }
        });
        return cmsLoanInfo;
    }

    /**
     * <b>功能介绍：</b>查询直属单位网点<br>
     * <b>日期：</b>2017年7月21日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public Map<String, String> getCenNetId(String corpId) {
        Map<String, String> corp = new HashMap<String, String>();
        CorpDTO netCorp = iCorpService.getNetByCorpId(corpId);// 获取单位直属网点
        if (netCorp == null) {// 如果没有直属网点就是本网点
            corp.put("corpId", corpId);
            corp.put("corpName", iCorpService.get(corpId).getName());
        } else {
            corp.put("corpId", netCorp.getId());
            corp.put("corpName", netCorp.getName());
        }
        return corp;
    }

    /**
     * <b>功能介绍：</b>统计是否第一次发放<br>
     * <b>日期：</b>2017年7月29日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public int isFirstPay(String billCode) {
        return cmsLoanInfoDao.isFirstPay(billCode);
    }

    /**
     * <b>功能介绍：</b>设置新增或者修改人信息<br>
     * <b>日期：</b>2017年7月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public void setCreateUpdateInfo(int type, CmsLoanInfo cmsLoanInfo, ICurrentUser user) {
        if (type == 1) {// create
            cmsLoanInfo.setCreateBy(user.getId());
            cmsLoanInfo.setCreateName(user.getUsername());
        } else {
            cmsLoanInfo.setUpdateBy(user.getId());
            cmsLoanInfo.setUpdateName(user.getUsername());
        }
    }

    /**
     * <b>功能介绍：</b>贷款已发放金额<br>
     * <b>日期：</b>2017年9月15日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param loanCode
     * @return
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public BigDecimal getSendedMoney(String loanCode) {
        return cmsLoanInfoDao.getSendedMoney(loanCode);
    }

    /**
     * <b>功能介绍：</b>根据贷款编号查询发放列表<br>
     * <b>日期：</b>2017年8月9日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public List<CmsLoanSendVO> getLoanSendItem(String loanCode, String type) {
        return cmsLoanInfoDao.getLoanSendItem(loanCode, type);
    }

    /**
     * <b>功能介绍：</b>根据loancode查询发放记录<br>
     * <b>日期：</b>2017年11月14日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param loanCode
     * @return
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public List<CmsLoanSend> getCmsLoanSendByLoanCode(String loanCode) {
        return cmsLoanSendDao.getLoanSendByLoanCode(loanCode);
    }

    /**
     * <b>功能介绍：</b>判断合同号是否存在<br>
     * <b>日期：</b>2017年8月10日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public boolean existsContractCode(String contractCode, String billCode) {
        return cmsLoanInfoDao.existsContractCode(contractCode, billCode);
    }

    /**
     * <b>功能介绍：</b>根据billCode查找贷款申请<br>
     * <b>日期：</b>2017年8月22日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @Transactional(readOnly = true)
    public CmsLoanInfo getCmsLoanInfoBybillCode(String billCode) {
        return cmsLoanInfoDao.getCmsLoanInfoBybillCode(billCode);
    }

    public CmsLoanInfoDTO getLoanInfoBybillCode(String billCode) {
        return cmsLoanInfoDao.getLoanInfoBybillCode(billCode);
    }

    /**
     * <b>功能介绍：</b>获取币别对象<br>
     * <b>日期：</b>2017年9月25日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param curID
     * @return
     * @version 1.0
     */
    public BtCurrencyDTO getCurrencyDTO(String curID) {
        return currencyService.getObjectById(curID);
    }

    /**
     * 审批启动完成回调
     */
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }

    /**
     * 审批启动结束回调
     */
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();
        String processDefinitionKey = workFlowResult.getProcessDefinitionKey();
        boolean result = workFlowResult.isPass();
        ICurrentUser user = workFlowResult.getUser();
        logger.info("审批启动结束回调：businessKey:{} ，processDefinitionKey:{} ，result:{}", businessKey, processDefinitionKey, result);
        if (StringUtils.isEmpty(businessKey)) {
            throw new BusinessException(BaseDataMessageEnum.MESSAGE_ERROR_VALISNOTNULL);
        }
        CmsLoanInfo loanInfo = this.get(businessKey, true);// 根据业务id获取数据
        BigDecimal loanMoney = loanInfo.getLoanMoney().setScale(2, BigDecimal.ROUND_HALF_UP);
        loanInfo.setLoanMoney(loanMoney);
        Integer status = 0;
        String bizStatus = "";
        if (CmsWorkFlowConstant.CMS_LOAN_INFO.equals(processDefinitionKey)) {// 新增
            logger.info("【内部贷款申请】审批启动结束回调：businessKey:{} ，result:{}", businessKey, result);
            status = result ? CmsConstant.APPROVED : CmsConstant.BACK;// 判断是否审批通过
            bizStatus = result ? CmsConstant.BUSINESSSTATE_21 : CmsConstant.BUSINESSSTATE_17;
            if (result) {
                msService.insertProcessMsg(loanInfo.getBillCode(), CmsConstant.LOAN_INFO_BILL_CODE_PREFIX,
                        CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_INPUT_CHECKOK, user);
                if ("1".equals(loanInfo.getIsOverRepair())) {// 跳过补录
                    status = result ? CmsConstant.PREPARESEND : CmsConstant.BACK;// 判断是否审批通过
                    bizStatus = result ? CmsConstant.BUSINESSSTATE_31 : CmsConstant.BUSINESSSTATE_17;
                    if (result) {
                        msService.insertProcessMsg(loanInfo.getBillCode(), CmsConstant.LOAN_INFO_BILL_CODE_PREFIX,
                                CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_REPAIR_CHECKOK, user);
                        // 授信正式占用
                        if (!EmptyUtils.isEmpty(loanInfo.getIsHangCredit())
                                && CmsConstant.IS_TRUE.equals(loanInfo.getIsHangCredit())) {
                            RatUrationDTO urationDTO = new RatUrationDTO(ConfigConstant.CMS, businessKey);
                            ucprationService.passUseCpRation(urationDTO);
                        }
                        // 判断是否挂接担保,改变担保合同占用信息的状态
                        if (loanInfo.getIsHangGuarantee() != null
                                && loanInfo.getIsHangGuarantee().equals(CmsConstant.IS_TRUE)) {// 挂接担保
                            GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
                            guaContractOccupDTO.setBusinessId(businessKey);
                            contractOccupServie.saveUseGuaContractOccup(guaContractOccupDTO);
                        }
                        // 对接合同管理
                        this.toRelateCon(loanInfo);
                    } else {
                        msService.insertProcessMsg(loanInfo.getBillCode(), CmsConstant.LOAN_INFO_BILL_CODE_PREFIX,
                                CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_REPAIR_CHECKBACK, user);
                    }
                    loanInfo.setStatus(status);// 更新状态
                    loanInfo.setBizStatus(bizStatus);// 更新业务状态
                    this.update(loanInfo);
                    if ("1".equals(loanInfo.getIsOverDistribute())) {// 跳过发放
                        CmsLoanSend send = new CmsLoanSend();
                        BeanUtils.copyNotNullProperties(loanInfo, send);
                        send.setId("");// 重置id
                        // 查询发放的 还款计划
                        CmsRepaymentSchedule crs = repaymentScheduleService
                                .getCmsRepaymentScheduleByBillCode(loanInfo.getId());
                        send.setPlanId(crs.getId());
                        send.setContractCode(loanInfo.getContractCode());
                        bizStatus = result ? CmsConstant.BUSINESSSTATE_90 : CmsConstant.BUSINESSSTATE_37;
                        send.setStatus(result ? CmsConstant.APPROVED : CmsConstant.DELETED);// 更新发放单据状态
                        send.setBizStatus(bizStatus);
                        send.setPayType("1");
                        send.setSendTime(1);
                        send.setLoanSendOutType("1");// 设置发放方式,一次性发放
                        loanInfo.setRepairOp(user.getLoginName());// 补录员
                        loanInfo.setLoanSendOutType("1");// 设置发放方式，一次性发放
                        send.setLoanCode(loanInfo.getBillCode());
                        if (CmsConstant.LOAN_SEND_OUT_TYPE_ONE.equals(send.getLoanSendOutType())) {// 一次性发放
                            bizStatus = result ? CmsConstant.BUSINESSSTATE_90 : CmsConstant.BUSINESSSTATE_37;// 合同待生成
                        } else {
                            bizStatus = result ? CmsConstant.BUSINESSSTATE_32 : CmsConstant.BUSINESSSTATE_37;// 放款中
                        }
                        cmsLoanSendDao.save(send);
                        loanInfo.setStatus(status);// 更新状态
                        loanInfo.setBizStatus(bizStatus);// 更新业务状态
                        this.update(loanInfo);
                        ConfigDTO config = this.getagAutoHandle();// 自动生成合同
                        if (config.getValue().equals("1")) {
                            CmsContract cmsContract = new CmsContract();
                            cmsContract.setContractCode(loanInfo.getContractCode());
                            cmsContract.setCorpId(loanInfo.getCorpId());
                            cmsContract.setLiCode(loanInfo.getBillCode());
                            autoHandleContract(cmsContract, user);
                        }
                        if (crs != null) {
                            // 更新还款计划
                            repaymentScheduleDao.updateRepayMentBill(crs.getId(),
                                    CmsConstant.LOAN_SEND_BILL_CODE_PREFIX, send.getId());// 更新还款计划的ID和类型
                        }
                    } else {
                        notificationService.send(businessKey, loanInfo.getCorpId(), NotificationConstant.CMS.LOAN_SEND,
                                loanInfo);
                    }
                } else {
                    // 新增通过发送消息给经办人
                    notificationService.sendToUser(businessKey, loanInfo.getCreateBy(),
                            NotificationConstant.CMS.LOAN_REPAIR, loanInfo);
                }
            } else {
                msService.insertProcessMsg(loanInfo.getBillCode(), CmsConstant.LOAN_INFO_BILL_CODE_PREFIX,
                        CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_INPUT_CHECKBACK, user);
                // 判断是否挂接担保,改变担保合同占用信息的状态
                if (loanInfo.getIsHangGuarantee() != null
                        && loanInfo.getIsHangGuarantee().equals(CmsConstant.IS_TRUE)) {// 挂接担保
                    GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
                    guaContractOccupDTO.setBusinessId(businessKey);
                    contractOccupServie.backUseGuaContractOccup(guaContractOccupDTO);
                }
                // 释放授信冻结额度
                if (!EmptyUtils.isEmpty(loanInfo.getIsHangCredit())
                        && CmsConstant.IS_TRUE.equals(loanInfo.getIsHangCredit())) {
                    RatUrationDTO urationDTO = new RatUrationDTO(ConfigConstant.CMS, loanInfo.getId());
                    ucprationService.backUseCpRation(urationDTO);
                }
            }
        } else if (CmsWorkFlowConstant.CMS_LOAN_REPAIR.equals(processDefinitionKey)) {// 补录
            logger.info("【内部贷款补录】审批启动结束回调：businessKey:{} ，result:{}", businessKey, result);
            status = result ? CmsConstant.PREPARESEND : CmsConstant.APPROVED;// 判断是否审批通过
            bizStatus = result ? CmsConstant.BUSINESSSTATE_31 : CmsConstant.BUSINESSSTATE_26;
            if (result) {
                msService.insertProcessMsg(loanInfo.getBillCode(), CmsConstant.LOAN_INFO_BILL_CODE_PREFIX,
                        CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_REPAIR_CHECKOK, user);
                // 授信正式占用
                if (!EmptyUtils.isEmpty(loanInfo.getIsHangCredit())
                        && CmsConstant.IS_TRUE.equals(loanInfo.getIsHangCredit())) {
                    RatUrationDTO urationDTO = new RatUrationDTO(ConfigConstant.CMS, businessKey);
                    ucprationService.passUseCpRation(urationDTO);
                }
                // 判断是否挂接担保,改变担保合同占用信息的状态
                if (loanInfo.getIsHangGuarantee() != null
                        && loanInfo.getIsHangGuarantee().equals(CmsConstant.IS_TRUE)) {// 挂接担保
                    GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
                    guaContractOccupDTO.setBusinessId(businessKey);
                    contractOccupServie.saveUseGuaContractOccup(guaContractOccupDTO);
                }
                loanInfo.setStatus(status);// 更新状态
                loanInfo.setBizStatus(bizStatus);// 更新业务状态
                this.update(loanInfo);
                // 对接合同管理
                this.toRelateCon(loanInfo);
                if ("1".equals(loanInfo.getIsOverDistribute())) {// 跳过发放
                    status = loanInfo.getStatus();
                    CmsLoanSend send = new CmsLoanSend();
                    BeanUtils.copyNotNullProperties(loanInfo, send);
                    send.setId("");// 重置id
                    // 查询发放的 还款计划
                    CmsRepaymentSchedule crs = repaymentScheduleService
                            .getCmsRepaymentScheduleByBillCode(loanInfo.getId());
                    send.setPlanId(crs.getId());
                    send.setContractCode(loanInfo.getContractCode());
                    bizStatus = result ? CmsConstant.BUSINESSSTATE_90 : CmsConstant.BUSINESSSTATE_37;
                    send.setStatus(result ? CmsConstant.APPROVED : CmsConstant.DELETED);// 更新发放单据状态
                    send.setPayType("1");
                    send.setSendTime(1);
                    send.setBizStatus(bizStatus);
                    send.setLoanSendOutType("1");// 设置发放方式,一次性发放
                    loanInfo.setRepairOp(user.getLoginName());// 补录员
                    loanInfo.setLoanSendOutType("1");// 设置发放方式，一次性发放
                    send.setLoanCode(loanInfo.getBillCode());
                    if (CmsConstant.LOAN_SEND_OUT_TYPE_ONE.equals(send.getLoanSendOutType())) {// 一次性发放
                        bizStatus = result ? CmsConstant.BUSINESSSTATE_90 : CmsConstant.BUSINESSSTATE_37;// 合同待生成
                    } else {
                        bizStatus = result ? CmsConstant.BUSINESSSTATE_32 : CmsConstant.BUSINESSSTATE_37;// 放款中
                    }
                    // this.update(loanInfo);
                    loanInfo.setStatus(status);// 更新状态
                    loanInfo.setBizStatus(bizStatus);// 更新业务状态
                    this.update(loanInfo);
                    cmsLoanSendDao.save(send);
                    ConfigDTO config = this.getagAutoHandle();// 自动生成合同
                    if (config.getValue().equals("1")) {
                        CmsContract cmsContract = new CmsContract();
                        cmsContract.setContractCode(loanInfo.getContractCode());
                        cmsContract.setCorpId(loanInfo.getCorpId());
                        cmsContract.setLiCode(loanInfo.getBillCode());
                        autoHandleContract(cmsContract, user);
                    }
                    if (crs != null) {
                        // 更新还款计划
                        repaymentScheduleDao.updateRepayMentBill(crs.getId(), CmsConstant.LOAN_SEND_BILL_CODE_PREFIX,
                                send.getId());// 更新还款计划的ID和类型
                    }
                } else {
                    notificationService.send(businessKey, loanInfo.getCorpId(), NotificationConstant.CMS.LOAN_SEND,
                            loanInfo);
                }
            } else {
                msService.insertProcessMsg(loanInfo.getBillCode(), CmsConstant.LOAN_INFO_BILL_CODE_PREFIX,
                        CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_REPAIR_CHECKBACK, user);
            }
        }
        if (status == CmsConstant.BACK) {
            loanInfo.setRefuseWhy("审批打回，详情查看审批流程！");// 设置打回原因
        }
        loanInfo.setStatus(status);// 更新状态
        loanInfo.setBizStatus(bizStatus);// 更新业务状态
        this.saveOrUpdate(loanInfo);
    }

    // 合同生成
    public void autoHandleContract(CmsContract cmsContract, ICurrentUser user) {
        CmsContract cmsContractInfo = cmsContractService.findCmsContractByliCode(CmsConstant.LOAN_CONTRACTNO_INIT);
        if (cmsContractInfo != null && EmptyUtils.isNotEmpty(cmsContract.getLiCode())) {
            CmsLoanSendVO loanSend = cmsLoanSendService
                    .getCmsLoanSendById(cmsLoanSendService.getLoanSendByBillCode(cmsContract.getLiCode()).getId());
            CmsLoanInfoVO loanInfoVO = getCmsLoanInfoById(cmsLoanInfoDao.findByBillCode(loanSend.getLoanCode()).getId());
            // 合同文本中替代字符
            // 合同编号 #htbh#
            // 贷款人 #jkr#
            // 借款人 #dkr#
            // 借款种类 #dkzl#
            // 币 别 #bb#
            // 金额大写 #je#
            // 贷款用途 #yt#
            // 贷款期限 #qx#
            // 开始日期 #ksrq#
            // 到期日期 #jsrq#
            // 月利率为 #lv#
            String replaceStr[] = {"#htbh#", "#jkr#", "#dkr#", "#dkzl#", "#bb#", "#je#", "#yt#", "#qx#", "#ksrq#",
                    "#jsrq#", "#lv#"};
            String htbh = loanSend.getContractCode();
            String jkr = loanInfoVO.getCorpName();
            String dkr = loanInfoVO.getNetName();
            String dkzl = loanInfoVO.getLoanClassName();
            String bb = loanInfoVO.getCurName();
            String je = NumberUtil.formatNumeric(loanSend.getLoanMoney(), "#,##0.00");
            String yt = loanInfoVO.getPurpose() == null ? "" : loanInfoVO.getPurpose();
            String ksrq = DateUtils.date2Str(loanSend.getLoanDate(), DateUtils.date_sdf_wz());
            String jsrq = DateUtils.date2Str(loanSend.getRepayDate(), DateUtils.date_sdf_wz());
            // String qx = loanSend.getLoanTerm()+""+("2".equals(loanInfo.getTernUnit())?"天":"个月");
            String qx = String.valueOf(loanInfoVO.getLoanTerm()) + ""
                    + ("2".equals(loanInfoVO.getTernUnit()) ? multiLangServiceI.getLang("cms.loan.ternUnit.day")
                    : multiLangServiceI.getLang("cms.loan.ternUnit.month"));
            String lv = cmsContractService.showRate(loanSend.getLoanRate().doubleValue());
            String replaceStrNew[] = {htbh, jkr, dkr, dkzl, bb, je, yt, qx, ksrq, jsrq, lv};
            String contract = cmsContractInfo.getContractInfo();
            for (int i = 0; i < replaceStr.length; i++) {
                if (replaceStrNew[i] == null) {
                    replaceStrNew[i] = "";
                }
                contract = contract.replaceAll(replaceStr[i], replaceStrNew[i]);
            }
            cmsContract.setContractInfo(contract);
            notificationService.complete(NotificationConstant.CMS.LOAN_CONTRACT, loanSend.getId(), user.getId());
            cmsContractService.saveContract(cmsContract, user);
        }
    }

    /***
     * 内部贷款查询
     *
     * @param input
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput dataTableLoanQuery(DataTablesInput input, String userId) {
        List<String> corpAuth = iCorpService.getUserCorpIds(ResourceConstant.CMS_LOAN_INFO_REPORTQUERY, userId);
        return cmsLoanInfoDao.dataTableLoanQuery(input, corpAuth);
    }

    /**
     * 内部贷款是否挂授信
     *
     * @param
     * @return
     * @author luxy
     */
    @Transactional(readOnly = true)
    public ConfigDTO getRelateRat() {
        return configService.getConfig(ConfigConstant.CMS, "isHang_rat");
    }

    /**
     * 内部贷款是否自动生成合同
     *
     * @param
     * @return
     * @author luxy
     */
    @Transactional(readOnly = true)
    public ConfigDTO getagAutoHandle() {
        return configService.getConfig(ConfigConstant.CMS, "agreement_auto_handle");
    }

    /**
     * 内部贷款是否挂接担保
     *
     * @param
     * @return
     * @author luxy
     */
    @Transactional(readOnly = true)
    public ConfigDTO getIsGuarantee() {
        return configService.getConfig(ConfigConstant.CMS, "Is_innloan_gua");
    }

    /**
     * @return com.bytter.biz.sys.spi.dto.ConfigDTO
     * @Description 查询信贷参数是否对接合同管理
     * @Param []
     * @Date 2019/11/12 19:49
     * @Author fusb
     */
    @Transactional(readOnly = true)
    public ConfigDTO getRelatedContract() {
        return configService.getConfig(ConfigConstant.CMS, "loan_relate_con");
    }

    /**
     * @return com.bytter.biz.sys.spi.dto.ConfigDTO
     * @Description 查询信贷参数对接合同管理模式
     * @Param []
     * @Date 2019/11/12 20:05
     * @Author fusb
     */
    @Transactional(readOnly = true)
    public ConfigDTO getRelatedConType() {
        return configService.getConfig(ConfigConstant.CMS, "loan_relate_con_type");
    }

    /**
     * 保存担保合同信息
     *
     * @author luxy
     */
    public void saveGuaContract(CmsLoanInfo cmsLoanInfo, CmsLoanInfoVO cmsLoanInfoVO) {
        GuaContractOccupDTO guaContractOccup = new GuaContractOccupDTO();
        guaContractOccup.setBusinessId(cmsLoanInfo.getId());
        guaContractOccup.setContractIds(cmsLoanInfoVO.getContractIds());
        guaContractOccup.setItemNo(cmsLoanInfo.getBillCode());
        guaContractOccup.setSysCode(ConfigConstant.LOAN);
        guaContractOccup.setUseDate(DateUtils.getDate());
        guaContractOccup.setGuaRmk(cmsLoanInfoVO.getGuaranteeRmk());
        if (cmsLoanInfo.getStatus().equals(CmsConstant.TEMP)) {// 暂存
            contractOccupServie.tempUseGuaContractOccup(guaContractOccup);
        } else if (cmsLoanInfo.getStatus().equals(CmsConstant.COMMITAPPROVE)) {// 提交待审批
            contractOccupServie.submitUseGuaContractOccup(guaContractOccup);
        }
    }

    /**
     * @return void
     * @Description 对接合同管理
     * @Param [loanInfo, user]
     * @Date 2019/11/15 9:35
     * @Author fusb
     */
    public void toRelateCon(CmsLoanInfo loanInfo) {
        // 业务发起人
        UserDTO user = null;
        if (StringUtils.isNotEmpty(loanInfo.getUpdateBy())) {
            user = userService.getByUserId(loanInfo.getUpdateBy());
        } else {
            user = userService.getByUserId(loanInfo.getCreateBy());
        }
        // 查询业务是否已经生成了合同信息
        ConInfoDTO searchDTO = conCommonService.getConInfo("", loanInfo.getId());
        // 如果未对接合同管理
        if (EmptyUtils.isEmpty(searchDTO)) {
            if (StringUtils.isNotEmpty(loanInfo.getConId()) && StringUtils.isEmpty(loanInfo.getConTypeId())) { // 业务选择合同
                conCommonService.hookConOp(loanInfo.getConId(), loanInfo.getId(), user);
            }
            if (StringUtils.isNotEmpty(loanInfo.getConTypeId())) { // 业务生成合同
                CmsLoanInfoVO cmsLoanInfoVO = getCmsLoanInfoById(loanInfo.getId());
                String corpCode = corpService.getCorpCode(cmsLoanInfoVO.getCorpId());
                cmsLoanInfoVO.setLoanGenreName(loanTypeService
                        .queryLoanTypeByCode(cmsLoanInfoVO.getLoanGenre(), BaseDataConstant.TYPE_CREDIT).getName());// 设置贷款类型名称
                String floatWayName = dictService.get("Cms.FloatWay", loanInfo.getFloatWay());
                cmsLoanInfoVO.setFloatWayName(floatWayName);
                ConInfoDTO conDTO = new ConInfoDTO();
                conDTO.setCorpId(cmsLoanInfoVO.getCorpId());
                conDTO.setBusId(cmsLoanInfoVO.getId());
                conDTO.setCorpCode(corpCode);
                conDTO.setConName(cmsLoanInfoVO.getCorpName() + CmsConstant.INNER_LOAN_CON);
                conDTO.setConNo(cmsLoanInfoVO.getContractCode());
                conDTO.setTypeId(cmsLoanInfoVO.getConTypeId());
                conDTO.setBeginDate(cmsLoanInfoVO.getLoanDate());
                conDTO.setEndDate(cmsLoanInfoVO.getRepayDate());
                conDTO.setOutBusData(cmsLoanInfoVO);
                String conId = conCommonService.registerConOpInnerLoan(conDTO, user);
                loanInfo.setConId(conId);
            }
        }
    }

    /***
     * 内部贷款发放完成数据查询
     *
     * @param input
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput loanInfoList(DataTablesInput input, String userId) {
        // List<String> corpAuth = iCorpService.getUserCorpIds(ResourceConstant.CMS_LOAN_INFO_REPORTQUERY,userId);
        List<String> corpAuth = null;
        return cmsLoanInfoDao.loanInfoList(input, corpAuth);
    }

    /**
     * 更新业务挂接担保合同参数
     *
     * @param id
     * @param configSatus
     */
    public void updateGuaranteeConfig(String id, String configSatus) {
        CmsLoanInfo cmsLoanInfo = this.get(id);
        cmsLoanInfo.setIsHangGuarantee(configSatus);// 更新挂接担保合同参数
        this.saveOrUpdate(cmsLoanInfo);
    }

    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
        List<CorpVerifyErrorDTO> corpVerifyErrorDTOS = new ArrayList<>();
        if (cmsLoanInfoDao.verifyCorpDeleteByCorpId(corpId) > 0) {
            CorpVerifyErrorDTO corpVerifyErrorDTO = new CorpVerifyErrorDTO();
            corpVerifyErrorDTO.setCorpId(corpId);
            corpVerifyErrorDTO.setSysCode(SysConfigConstant.Module.CMS);
            corpVerifyErrorDTO.setBusCode(SysConfigConstant.BusType.CMS_LOAN_INFO);
            corpVerifyErrorDTO
                    .setErrorMsg(multiLangServiceI.getLang(CorpVerifyMessageConstant.CMS_VALID_DEL_LOAN_INFO_ERROR));
            corpVerifyErrorDTOS.add(corpVerifyErrorDTO);
        }
        return corpVerifyErrorDTOS;
    }

    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        List<CorpVerifyErrorDTO> corpVerifyErrorDTOS = new ArrayList<>();
        if (cmsLoanInfoDao.verifyCorpDisableByCorpId(corpId) > 0) {// 暂存,待审批,审批打回
            CorpVerifyErrorDTO corpVerifyErrorDTO = new CorpVerifyErrorDTO();
            corpVerifyErrorDTO.setCorpId(corpId);
            corpVerifyErrorDTO.setSysCode(SysConfigConstant.Module.CMS);
            corpVerifyErrorDTO.setBusCode(SysConfigConstant.BusType.CMS_LOAN_INFO);
            corpVerifyErrorDTO
                    .setErrorMsg(multiLangServiceI.getLang(CorpVerifyMessageConstant.CMS_VALID_DISABLE_LOAN_INFO));
            corpVerifyErrorDTOS.add(corpVerifyErrorDTO);
        }
        if (cmsLoanInfoDao.verifyCorpDisableByCorpId1(corpId) > 0) {// 补录、待发放、待生成合同
            CorpVerifyErrorDTO corpVerifyErrorDTO = new CorpVerifyErrorDTO();
            corpVerifyErrorDTO.setCorpId(corpId);
            corpVerifyErrorDTO.setSysCode(SysConfigConstant.Module.CMS);
            corpVerifyErrorDTO.setBusCode(SysConfigConstant.BusType.CMS_LOAN_INFO);
            corpVerifyErrorDTO
                    .setErrorMsg(multiLangServiceI.getLang(CorpVerifyMessageConstant.CMS_VALID_DISABLE_LOAN_INFO1));
            corpVerifyErrorDTOS.add(corpVerifyErrorDTO);
        }
        return corpVerifyErrorDTOS;
    }

    /**
     * 查询内部贷款挂接担保合同数据
     *
     * @param input 查询参数
     * @param user  用户信息
     * @return 列表数据
     */
    public DataTablesOutput<GuaContractCmsLoanVO> getGuaForLoan(DataTablesInput input, ICurrentUser user) {
        String sysCode = input.getFormdata().get("eqs_sysCode").toString();
        String startDate = input.getFormdata().get("ged_useDate").toString();
        String endDate = input.getFormdata().get("led_useDate").toString();
        String status = input.getFormdata().get("eqg_status").toString();
        //input.setFormdata(null);
        //获取挂接授信的业务id集合和封装参数
        //List<GuaContractOccupDTO> guaList = contractOccupServie.getGua(sysCode, startDate, endDate, status);
        List<String> corpIds = iCorpService.getUserCorpIds(GuaranteeConstant.CMS_GUARANTEE_CHANGE_QUERY, user.getId());
//        if (EmptyUtils.isEmpty(guaList)) {
//            return new DataTablesOutput<>();
//        }
//        Map<String, GuaContractOccupDTO> guaMap = new HashMap<>(guaList.size());
//        List<String> businessIdList = new ArrayList<>();
//        guaList.forEach(item -> {
//            guaMap.put(item.getBusinessId(), item);
//            businessIdList.add(item.getBusinessId());
//        });
        //组装参数
        DataTablesOutput<GuaContractCmsLoanVO> dataTablesOutput = cmsLoanInfoDao.getGuaForLoan(input, corpIds);
//        if (EmptyUtils.isNotEmpty(dataTablesOutput.getData())) {
//            for (GuaContractCmsLoanVO item : dataTablesOutput.getData()) {
//                if (guaMap.containsKey(item.getId())) {
//                    item.setStatus(guaMap.get(item.getId()).getStatus());
//                    item.setUseDate(guaMap.get(item.getId()).getUseDate());
//                }
//            }
//        }
        return dataTablesOutput;
    }

    /**
     * 获取所有挂接担保的内部贷款数据
     *
     * @return
     */
    public List<GuaContractCmsLoanDTO> getHangGuaForLoan() {
        return cmsLoanInfoDao.getHangGuaForLoan();
    }
}
