package com.bytter.finance.domain.directFinance.service;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.*;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.directFinance.dao.DfDirectFinanceInfoDao;
import com.bytter.finance.domain.directFinance.dao.DfDirectFinanceRepayDao;
import com.bytter.finance.domain.directFinance.dao.DfDirectFinanceRepayTmpDao;
import com.bytter.finance.domain.directFinance.entity.DfDirectFinanceInfo;
import com.bytter.finance.domain.directFinance.entity.DfDirectFinanceRepay;
import com.bytter.finance.domain.directFinance.entity.DfDirectFinanceRepayTmp;
import com.bytter.finance.domain.directFinance.utils.DfUtils;
import com.bytter.finance.domain.directFinance.vo.DfDirectFinanceRepayTmpVO;
import com.bytter.finance.domain.directFinance.vo.DfDirectFinanceRepayVO;
import com.bytter.finance.enums.BaseDataMessageEnum;
import com.bytter.finance.enums.DfMessageEnum;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.ITaskService;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
import com.xxl.job.core.handler.annotation.XxlJob;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-12-13 15:05
 * @Version 1.0
 */
@WorkFlow(DfWorkflowConstant.DF_DIRECT_FINANCE_REPAY)
@DubboService(interfaceClass = IWorkFlowListener.class,group = DfWorkflowConstant.DF_DIRECT_FINANCE_REPAY)
@Service("dfDirectFinanceRepayService")
@Transactional
public class DfDirectFinanceRepayService extends BaseService<DfDirectFinanceRepay, String>
        implements IWorkFlowListener, ITaskService {
    @Autowired
    private DfDirectFinanceRepayDao dfDirectFinanceRepayDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    private IWorkFlowService workFlowService;
    @Autowired
    private CmsProcessMsgService msService;
    @DubboReference(check = false)
    private IFbsBudgetProcessService fbsBudgetProcessService;
    @Autowired
    private DfDirectFinanceInfoDao dfDirectFinanceInfoDao;
    @DubboReference(check = false)
    private IConfigService configService;
    @Autowired
    private DfDirectFinanceRepayTmpDao dfDirectFinanceRepayTmpDao;
    /**
     * 查询待还款数据
     *
     * @param input
     * @param user
     * @return
     */
    public DataTablesOutput listDfDirectFinanceRepay(DataTablesInput input, ICurrentUser user) {
        List<String> corpIds = corpService.getUserCorpIds(ResourceConstant.DF_DIRECT_FINANCE_INFO_QUERY, user.getId());
        return dfDirectFinanceRepayDao.listDfDirectFinanceRepay(input, corpIds);
    }
    /**
     * 打回或者暂存数据
     *
     * @param input
     * @param user
     * @return
     */
    public DataTablesOutput listTempOrback(DataTablesInput input, ICurrentUser user) {
        List<String> corpIds = corpService.getUserCorpIds(ResourceConstant.DF_DIRECT_FINANCE_INFO_QUERY, user.getId());
        return dfDirectFinanceRepayDao.listTempOrback(input, corpIds);
    }
    /**
     * 待审批列表
     *
     * @param input
     * @param user
     * @return
     */
    public DataTablesOutput listApprove(DataTablesInput input, ICurrentUser user) {
        WorkFlowBusiness workFlowBusiness = workFlowService
                .getWorkFlowBusiness(DfWorkflowConstant.DF_DIRECT_FINANCE_REPAY, input);
        DataTablesOutput dataTablesOutput = dfDirectFinanceRepayDao.listApprove(input,
                workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dataTablesOutput);
    }
    /**
     * 保存
     *
     * @param dfDirectFinanceRepayVO
     * @param user
     */
    public void saveDfDirectFinanceRepay(DfDirectFinanceRepayVO dfDirectFinanceRepayVO, ICurrentUser user) {
        // 存在未完成业务时不允许新增数据
        addCheck(dfDirectFinanceRepayVO.getDirectFinanceId());
        DfDirectFinanceRepay dfDirectFinanceRepay = new DfDirectFinanceRepay();
        BeanUtils.copyProperties(dfDirectFinanceRepayVO, dfDirectFinanceRepay);
        if (StringUtils.isEmpty(dfDirectFinanceRepayVO.getBillCode())) {
            dfDirectFinanceRepay.setBillCode(DfUtils.getDfdirectFinanceRepayNo());
        }
        if (StringUtils.isNotEmpty(dfDirectFinanceRepay.getId())) {
            dfDirectFinanceRepay.setUpdateBy(user.getId());
            dfDirectFinanceRepay.setUpdateDate(new Date());
        } else {
            dfDirectFinanceRepay.setCreateBy(user.getId());
            dfDirectFinanceRepay.setCreateDate(new Date());
        }
        dfDirectFinanceRepayDao.saveOrUpdate(dfDirectFinanceRepay);
        // 打回数据角标处理
        notificationService.complete(NotificationConstant.CMS.DF_DIRECT_FINANCE_REPAY, dfDirectFinanceRepay.getId(),
                user.getId());
        // 挂接资金计划处理
        if (DfConstant.IS_TRUE.equals(getIsBudget().getValue())) {
            dfDirectFinanceRepayVO.setBillCode(dfDirectFinanceRepay.getBillCode());
            dfDirectFinanceRepayVO.setId(dfDirectFinanceRepay.getId());
            buggetHandle(dfDirectFinanceRepayVO, user);
        }
        if (DfConstant.COMMITAPPROVE == dfDirectFinanceRepay.getStatus()) {
             getStartHandler().handler(DfWorkflowConstant.DF_DIRECT_FINANCE_REPAY, dfDirectFinanceRepay.getId(),
                    dfDirectFinanceRepay, dfDirectFinanceRepay.getCorpId(), user.getId());
        }
    }
    /**
     * 返回DfDirectFinanceRepayVO
     *
     * @param id
     * @return
     */
    public DfDirectFinanceRepayVO getById(String id) {
        DfDirectFinanceRepayVO dfDirectFinanceRepayVO = new DfDirectFinanceRepayVO();
        BeanUtils.copyProperties(get(id), dfDirectFinanceRepayVO);
        return dfDirectFinanceRepayVO;
    }
    /**
     * 获取是否挂接资金计划配置
     *
     * @return
     */
    private ConfigDTO getIsBudget() {
        return configService.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_DF_BUDGET);
    }
    /**
     * 获取是否挂接资金计划配置
     *
     * @return
     */
    private ConfigDTO getNoticeDays() {
        return configService.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS,
                BaseDataConstant.DIRECT_FINANCE_REPAY_NOTICE_DAYS);
    }
    /**
     * 返回已还款金额
     *
     * @param id
     * @return
     */
    public List<DfDirectFinanceRepayVO> listRepaidMoney(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return dfDirectFinanceRepayDao.listRepaidMoney(params);
    }
    /**
     * 检查是否存在未完成业务，存在则不允许新增
     *
     * @param id
     */
    public void addCheck(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        String count = dfDirectFinanceRepayDao.getUnApprovedById(params);
        if (!"0".equals(count)) {
            throw new ParameterException(DfMessageEnum.DF_REPAY_APPLY_MSG);
        }
    }
    @Override
    public BaseDao<DfDirectFinanceRepay, String> getEntityDao() {
        return dfDirectFinanceRepayDao;
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        /* 判断业务主键是否为空 */
        if (StringUtils.isEmpty(workFlowResult.getBusinessKey())) {
            throw new BusinessException(BaseDataMessageEnum.MESSAGE_ERROR_VALISNOTNULL);
        }
        String id = workFlowResult.getBusinessKey();
        boolean isPass = workFlowResult.isPass();
        DfDirectFinanceRepay dfDirectFinanceRepay = dfDirectFinanceRepayDao.get(id);
        ICurrentUser user = workFlowResult.getUser();
        if (isPass) {
            dfDirectFinanceRepay.setStatus(DfConstant.APPROVED);
            msService.insertProcessMsg(dfDirectFinanceRepay.getBillCode(), DfConstant.DF_DIRECT_FINANCE_REPAY_CODE,
                    CmsProcessMsgConstant.MSGA_CHECKOK, "审批通过", user);
        } else {
            dfDirectFinanceRepay.setStatus(DfConstant.BACK);
            msService.insertProcessMsg(dfDirectFinanceRepay.getBillCode(), DfConstant.DF_DIRECT_FINANCE_REPAY_CODE,
                    CmsProcessMsgConstant.MSGA_ROLLBACK, "审批打回", user);
        }
        // 资金计划挂机而处理
        updateBudget(dfDirectFinanceRepay, user);
        dfDirectFinanceRepayDao.update(dfDirectFinanceRepay);
    }
    /**
     * 获取还款金额
     *
     * @param id
     * @return
     */
    public BigDecimal getAllRepayMoney(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return dfDirectFinanceRepayDao.getAllRepayMoney(params);
    }
    /**
     * 资金计划挂接处理
     *
     * @param dfDirectFinanceRepayVO
     * @param user
     */
    private void buggetHandle(DfDirectFinanceRepayVO dfDirectFinanceRepayVO, ICurrentUser user) {
        if (!EmptyUtils.isEmptyArray(dfDirectFinanceRepayVO.getItemCode())) {
            List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();
            DfDirectFinanceInfo dfDirectFinanceInfo = dfDirectFinanceInfoDao
                    .get(dfDirectFinanceRepayVO.getDirectFinanceId());
            for (int i = 0; i < dfDirectFinanceRepayVO.getItemCode().length; i++) {
                FbsBudgetProcessDTO fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
                if (DfConstant.TEMP == dfDirectFinanceRepayVO.getStatus()) {
                    fbsBudgetProcessDTO.setStatus(BudgetConstant.STATUE_TEMP);// 状态:0暂存,11在途，95占用
                } else {
                    fbsBudgetProcessDTO.setStatus(BudgetConstant.STATUE_WAIT_CHECK);// 状态:0暂存,11在途，95占用
                }
                fbsBudgetProcessDTO.setBillNo(dfDirectFinanceRepayVO.getBillCode());// 业务单据号
                fbsBudgetProcessDTO.setBillId(dfDirectFinanceRepayVO.getId());
                fbsBudgetProcessDTO.setItemCode(dfDirectFinanceRepayVO.getItemCode()[i]);// 科目代码
                fbsBudgetProcessDTO.setItemName(dfDirectFinanceRepayVO.getItemName()[i]);// 科目名称
                fbsBudgetProcessDTO.setProcessMoney(dfDirectFinanceRepayVO.getProcessMoney()[i]);// 执行金额
                fbsBudgetProcessDTO.setProcessDate(dfDirectFinanceRepayVO.getRepayDate());// 执行日期
                fbsBudgetProcessDTO.setCorpId(dfDirectFinanceRepayVO.getCorpId());// 单位id
                fbsBudgetProcessDTO.setCurId(dfDirectFinanceInfo.getCurId());// 币别
                fbsBudgetProcessDTO.setBusinessName(BudgetConstant.BUSINESS_NAME_DF_REPAY);// 执行业务名称
                fbsBudgetProcessDTO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_CMS);// 执行系统名称
                fbsBudgetProcessDTO.setCreateBy(user.getLoginName());
                fbsBudgetProcessDTO.setCreateDate(DateUtils.getDate());
                list.add(fbsBudgetProcessDTO);
            }
            // 修改时，先删除之前的计划执行数据再保存
            fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_DF_REPAY,
                    BudgetConstant.PROCESS_SYSNAME_CMS, dfDirectFinanceRepayVO.getBillCode());
            fbsBudgetProcessService.writeBudgetProcess(list, user);
        }
    }
    public void updateBudget(DfDirectFinanceRepay dfDirectFinanceRepay, ICurrentUser user) {
        if (DfConstant.IS_TRUE.equals(getIsBudget().getValue())) {
            if (DfConstant.APPROVED == dfDirectFinanceRepay.getStatus()) {
                fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_DF_REPAY,
                        BudgetConstant.PROCESS_SYSNAME_CMS, dfDirectFinanceRepay.getBillCode(),
                        BudgetConstant.STATUE_CHECK, user);
            } else {
                fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_DF_REPAY,
                        BudgetConstant.PROCESS_SYSNAME_CMS, dfDirectFinanceRepay.getBillCode(),
                        BudgetConstant.STATUE_BACK, user);
            }
        }
    }
    /**
     * 删除资金计划
     *
     * @return
     */
    public void deleteBudget(String billCode) {
        // 删除资金计划挂接
        if (DfConstant.IS_TRUE.equals(getIsBudget().getValue())) {
            fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_DF_REPAY,
                    BudgetConstant.PROCESS_SYSNAME_CMS, billCode);
        }
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    @Override
    @XxlJob("DfDirectFinanceRepayService")
    public void run(String arguments) {
        ConfigDTO configDTO = getNoticeDays();
        Map<String, Object> params = new HashMap<>();
        if (null == configDTO) {
            params.put("expiryDays", 7);
        } else {
            params.put("expiryDays", configDTO.getValue());
        }
        List<DfDirectFinanceRepayTmp> dfDirectFinanceRepayTmps = dfDirectFinanceRepayTmpDao
                .listDirectFinanceRepayTmpNotice(params);
        if (null != dfDirectFinanceRepayTmps && dfDirectFinanceRepayTmps.size() > 0) {
            for (DfDirectFinanceRepayTmp dfDirectFinanceRepayTmp : dfDirectFinanceRepayTmps) {
                // 消息提醒
                params.put("id", dfDirectFinanceRepayTmp.getDirectFinanceId());
                BigDecimal repayMoney = dfDirectFinanceRepayDao.getAllRepayMoney(params);
                DfDirectFinanceInfo dfDirectFinanceInfo = dfDirectFinanceInfoDao
                        .get(dfDirectFinanceRepayTmp.getDirectFinanceId());
                if (dfDirectFinanceInfo.getSurplusMoney().compareTo(repayMoney) > 0
                        && dfDirectFinanceInfo.getStatus() == DfConstant.APPROVED) {
                    if (null == dfDirectFinanceRepayTmp.getRepayMoney()) {
                        dfDirectFinanceRepayTmp.setRepayMoney(BigDecimal.ZERO);
                    }
                    if (null == dfDirectFinanceRepayTmp.getInterest()) {
                        dfDirectFinanceRepayTmp.setInterest(BigDecimal.ZERO);
                    }
                    DfDirectFinanceRepayTmpVO dfDirectFinanceRepayTmpVO = new DfDirectFinanceRepayTmpVO();
                    BeanUtils.copyProperties(dfDirectFinanceRepayTmp, dfDirectFinanceRepayTmpVO);
                    dfDirectFinanceRepayTmpVO.setCurId(dfDirectFinanceInfo.getCurId());
                    notificationService.send(dfDirectFinanceRepayTmp.getId(),
                            dfDirectFinanceInfoDao.get(dfDirectFinanceRepayTmp.getDirectFinanceId()).getCorpId(),
                            arguments, dfDirectFinanceRepayTmpVO);
                }
            }
        }
    }
}
