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
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.*;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.directFinance.dao.*;
import com.bytter.finance.domain.directFinance.entity.DfDirectFinanceInfo;
import com.bytter.finance.domain.directFinance.entity.DfDirectFinanceRepayTmp;
import com.bytter.finance.domain.directFinance.entity.DfIntermediateCost;
import com.bytter.finance.domain.directFinance.entity.DfThirdPartyAgency;
import com.bytter.finance.domain.directFinance.utils.DfUtils;
import com.bytter.finance.domain.directFinance.vo.DfDirectFinanceInfoVO;
import com.bytter.finance.domain.directFinance.vo.DfDirectFinanceRepayTmpVO;
import com.bytter.finance.domain.directFinance.vo.DfIntermediateCostVO;
import com.bytter.finance.domain.directFinance.vo.DfThirdPartyAgencyVO;
import com.bytter.finance.dto.FinanceLoanDTO;
import com.bytter.finance.dto.FinanceTypeLoanDTO;
import com.bytter.finance.enums.BaseDataMessageEnum;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-12-03 10:41
 * @Version 1.0
 */
@WorkFlow(DfWorkflowConstant.DF_DIRECT_FINANCE_INFO)
@DubboService(interfaceClass = IWorkFlowListener.class,group = DfWorkflowConstant.DF_DIRECT_FINANCE_INFO)
@Service("dfDirectFinanceInfoService")
@Transactional
public class DfDirectFinanceInfoService extends BaseService<DfDirectFinanceInfo, String>
        implements IWorkFlowListener, ICorpDeleteVerifyService, ICorpDisableVerifyService {
    @Autowired
    private DfDirectFinanceInfoDao dfDirectFinanceInfoDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    private CmsProcessMsgService msService;
    @DubboReference(check = false)
    private IUserService userService;
    @Autowired
    private DfIntermediateCostDao dfIntermediateCostDao;
    @Autowired
    private DfBusinessTypeDao dfBusinessTypeDao;
    @Autowired
    private DfThirdPartyAgencyDao dfThirdPartyAgencyDao;
    @Autowired
    private DfDirectFinanceRepayTmpDao dfDirectFinanceRepayTmpDao;
    @DubboReference(check = false)
    private IFbsBudgetProcessService fbsBudgetProcessService;
    @DubboReference(check = false)
    private IConfigService configService;
    @Resource
    private IMultiLangService multiLangServiceI;
    /**
     * 打回暂存列表
     *
     * @param input
     * @param user
     * @return
     */
    public DataTablesOutput listDfDirectFinanceInfo(DataTablesInput input, ICurrentUser user) {
        List<String> corpIds = corpService.getUserCorpIds(ResourceConstant.DF_DIRECT_FINANCE_INFO_QUERY, user.getId());
        return dfDirectFinanceInfoDao.listDfDirectFinanceInfo(input, corpIds);
    }
    /**
     * 审批列表
     *
     * @param input
     * @param user
     * @return
     */
    public DataTablesOutput listDfDirectFinanceInfoApprove(DataTablesInput input, ICurrentUser user) {
        WorkFlowBusiness workFlowBusiness = workFlowService
                .getWorkFlowBusiness(DfWorkflowConstant.DF_DIRECT_FINANCE_INFO, input);
        DataTablesOutput dataTablesOutput = dfDirectFinanceInfoDao.listDfDirectFinanceInfoApprove(input,
                workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dataTablesOutput);
    }
    /**
     * 数据保存
     *
     * @param dfDirectFinanceInfoVO
     * @param user
     */
    public void saveDfDirectFinanceInfo(DfDirectFinanceInfoVO dfDirectFinanceInfoVO, ICurrentUser user) {
        DfDirectFinanceInfo dfDirectFinanceInfo = null;
        if (StringUtils.isNotEmpty(dfDirectFinanceInfoVO.getId())) {
            dfDirectFinanceInfo = dfDirectFinanceInfoDao.get(dfDirectFinanceInfoVO.getId());
            dfDirectFinanceInfoVO.setUpdateBy(user.getId());
            dfDirectFinanceInfoVO.setUpdateDate(new Date());
            dfDirectFinanceInfoVO.setCreateBy(dfDirectFinanceInfo.getCreateBy());
            dfDirectFinanceInfoVO.setCreateDate(dfDirectFinanceInfo.getCreateDate());
        } else {
            dfDirectFinanceInfo = new DfDirectFinanceInfo();
            dfDirectFinanceInfoVO.setCreateBy(user.getId());
            dfDirectFinanceInfoVO.setCreateDate(new Date());
            dfDirectFinanceInfoVO.setBillCode(DfUtils.getDfdirectFinanceInfoNo());
        }
        BeanUtils.copyProperties(dfDirectFinanceInfoVO, dfDirectFinanceInfo);
        dfDirectFinanceInfoDao.saveOrUpdate(dfDirectFinanceInfo);
        dfDirectFinanceInfoVO.setBillCode(dfDirectFinanceInfo.getBillCode());
        otherHandle(dfDirectFinanceInfoVO, dfDirectFinanceInfo.getId(), user);
        // 打回数据角标处理
        if (DfConstant.COMMITAPPROVE == dfDirectFinanceInfo.getStatus()) {
            notificationService.complete(NotificationConstant.CMS.DF_DIRECT_FINANCE_INFIO, dfDirectFinanceInfo.getId(),
                    user.getId());
             getStartHandler().handler(DfWorkflowConstant.DF_DIRECT_FINANCE_INFO, dfDirectFinanceInfo.getId(),
                    dfDirectFinanceInfo, dfDirectFinanceInfo.getCorpId(), user.getId());
        }
    }
    /**
     * 其它业务数据处理
     *
     * @param dfDirectFinanceInfoVO
     * @param id
     */
    private void otherHandle(DfDirectFinanceInfoVO dfDirectFinanceInfoVO, String id, ICurrentUser user) {
        dfDirectFinanceInfoVO.setId(id);
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        List<DfThirdPartyAgencyVO> dfThirdPartyAgencyVOS = dfDirectFinanceInfoVO.getDfThirdPartyAgencyVOS();
        List<DfIntermediateCostVO> dfIntermediateCostVOS = dfDirectFinanceInfoVO.getDfIntermediateCostVOS();
        List<DfDirectFinanceRepayTmpVO> dfDirectFinanceRepayTmpVOS = dfDirectFinanceInfoVO
                .getDfDirectFinanceRepayTmpVOS();
        /**
         * 第三方机构数据处理
         */
        saveDfThirdPartyAgency(dfThirdPartyAgencyVOS, id, params);
        /**
         * 中间费用数据处理
         */
        saveDfIntermediateCost(dfIntermediateCostVOS, id, params);
        /**
         * 还款计划数据处理
         */
        saveDfDirectFinanceRepayTmp(dfDirectFinanceRepayTmpVOS, id, params);
        /**
         * 判断是否挂接挂接资金计划
         */
        if (DfConstant.IS_TRUE.equals(getIsBudget().getValue())) {
            budgetHandle(dfDirectFinanceInfoVO, user);
        }
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
     * 第三方机构数据处理
     *
     * @param dfThirdPartyAgencyVOS
     * @param id
     * @param params
     */
    private void saveDfThirdPartyAgency(List<DfThirdPartyAgencyVO> dfThirdPartyAgencyVOS, String id,
                                        Map<String, Object> params) {
        List<DfThirdPartyAgency> dfThirdPartyAgencies = new ArrayList<>();
        for (DfThirdPartyAgencyVO dfThirdPartyAgencyVO : dfThirdPartyAgencyVOS) {
            if (StringUtils.isEmpty(dfThirdPartyAgencyVO.getAgencyType())
                    && StringUtils.isEmpty(dfThirdPartyAgencyVO.getAgencyName())) {
                continue;
            } else {
                DfThirdPartyAgency dfThirdPartyAgency = new DfThirdPartyAgency();
                BeanUtils.copyProperties(dfThirdPartyAgencyVO, dfThirdPartyAgency);
                dfThirdPartyAgency.setDirectFinanceId(id);
                dfThirdPartyAgencies.add(dfThirdPartyAgency);
            }
        }
        dfThirdPartyAgencyDao.deleteThirdPartyAgency(params);
        dfThirdPartyAgencyDao.batchSave(dfThirdPartyAgencies);
    }
    /**
     * 中间费用数据处理
     *
     * @param dfIntermediateCostVOS
     * @param id
     * @param params
     */
    private void saveDfIntermediateCost(List<DfIntermediateCostVO> dfIntermediateCostVOS, String id,
                                        Map<String, Object> params) {
        List<DfIntermediateCost> dfIntermediateCosts = new ArrayList<>();
        for (DfIntermediateCostVO dfIntermediateCostVO : dfIntermediateCostVOS) {
            if (StringUtils.isEmpty(dfIntermediateCostVO.getCostTypeId()) && null == dfIntermediateCostVO.getRate()
                    && null == dfIntermediateCostVO.getAdvanceFees()) {
                continue;
            } else {
                DfIntermediateCost dfIntermediateCost = new DfIntermediateCost();
                BeanUtils.copyProperties(dfIntermediateCostVO, dfIntermediateCost);
                dfIntermediateCost.setDirectFinanceId(id);
                dfIntermediateCosts.add(dfIntermediateCost);
            }
        }
        dfIntermediateCostDao.deleteIntermediateCost(params);
        dfIntermediateCostDao.batchSave(dfIntermediateCosts);
    }
    /**
     * 还款计划数据处理
     *
     * @param dfDirectFinanceRepayTmpVOS
     * @param id
     * @param params
     */
    private void saveDfDirectFinanceRepayTmp(List<DfDirectFinanceRepayTmpVO> dfDirectFinanceRepayTmpVOS, String id,
                                             Map<String, Object> params) {
        List<DfDirectFinanceRepayTmp> dfDirectFinanceRepayTmps = new ArrayList<>();
        for (DfDirectFinanceRepayTmpVO dfDirectFinanceRepayTmpVO : dfDirectFinanceRepayTmpVOS) {
            if (null == dfDirectFinanceRepayTmpVO.getRepayDate() && null == dfDirectFinanceRepayTmpVO.getRepayMoney()
                    && null == dfDirectFinanceRepayTmpVO.getInterest()
                    && null == dfDirectFinanceRepayTmpVO.getServiceCharge()) {
                continue;
            } else {
                DfDirectFinanceRepayTmp dfDirectFinanceRepayTmp = new DfDirectFinanceRepayTmp();
                BeanUtils.copyProperties(dfDirectFinanceRepayTmpVO, dfDirectFinanceRepayTmp);
                dfDirectFinanceRepayTmp.setDirectFinanceId(id);
                dfDirectFinanceRepayTmps.add(dfDirectFinanceRepayTmp);
            }
        }
        dfDirectFinanceRepayTmpDao.deleteDirectFinanceTmp(params);
        dfDirectFinanceRepayTmpDao.batchSave(dfDirectFinanceRepayTmps);
    }
    @Override
    public BaseDao<DfDirectFinanceInfo, String> getEntityDao() {
        return dfDirectFinanceInfoDao;
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        /* 判断业务主键是否为空 */
        if (StringUtils.isEmpty(workFlowResult.getBusinessKey())) {
            throw new BusinessException(BaseDataMessageEnum.MESSAGE_ERROR_VALISNOTNULL);
        }
        String id = workFlowResult.getBusinessKey();
        boolean isPass = workFlowResult.isPass();
        DfDirectFinanceInfo dfDirectFinanceInfo = dfDirectFinanceInfoDao.get(id);
        ICurrentUser user = workFlowResult.getUser();
        if (isPass) {
            dfDirectFinanceInfo.setStatus(DfConstant.APPROVED);
            msService.insertProcessMsg(dfDirectFinanceInfo.getBillCode(), DfConstant.DF_DIRECT_FINANCE_INFO_CODE,
                    CmsProcessMsgConstant.MSGA_CHECKOK, "审批通过", user);
        } else {
            dfDirectFinanceInfo.setStatus(DfConstant.BACK);
            msService.insertProcessMsg(dfDirectFinanceInfo.getBillCode(), DfConstant.DF_DIRECT_FINANCE_INFO_CODE,
                    CmsProcessMsgConstant.MSGA_ROLLBACK, "审批打回", user);
        }
        // 资金计划挂机处理
        updateBudget(dfDirectFinanceInfo, user);
        dfDirectFinanceInfoDao.update(dfDirectFinanceInfo);
    }
    /**
     * 根据id查询返回VO
     *
     * @param id
     * @return
     */
    public DfDirectFinanceInfoVO getById(String id) {
        DfDirectFinanceInfo dfDirectFinanceInfo = dfDirectFinanceInfoDao.get(id);
        DfDirectFinanceInfoVO dfDirectFinanceInfoVO = new DfDirectFinanceInfoVO();
        BeanUtils.copyProperties(dfDirectFinanceInfo, dfDirectFinanceInfoVO);
        dfDirectFinanceInfoVO
                .setBusinessName(dfBusinessTypeDao.get(dfDirectFinanceInfoVO.getDfBusinessTypeId()).getBusinessName());
        return dfDirectFinanceInfoVO;
    }
    /**
     * 获取第三方机构
     *
     * @param params
     * @return
     */
    public List<DfThirdPartyAgencyVO> listThirdPartyAgencyByDfId(Map<String, Object> params) {
        return dfThirdPartyAgencyDao.listThirdPartyAgencyByDfId(params);
    }
    /**
     * 获取中间费用
     *
     * @param params
     * @return
     */
    public List<DfIntermediateCostVO> listIntermediateCostByDfId(Map<String, Object> params) {
        return dfIntermediateCostDao.listIntermediateCostByDfId(params);
    }
    /**
     * 获取还本付息计划
     *
     * @param params
     * @return
     */
    public List<DfDirectFinanceRepayTmpVO> listDirectFinanceRepayTmpByDfId(Map<String, Object> params) {
        return dfDirectFinanceRepayTmpDao.listDirectFinanceRepayTmpByDfId(params);
    }
    /**
     * 查询报表
     *
     * @return
     */
    public DataTablesOutput listDfDirectFinanceReport(DataTablesInput input) {
        return dfDirectFinanceInfoDao.listDfDirectFinanceReport(input);
    }
    /**
     * 资金计划挂接处理
     *
     * @param dfDirectFinanceInfoVO
     * @param user
     */
    private void budgetHandle(DfDirectFinanceInfoVO dfDirectFinanceInfoVO, ICurrentUser user) {
        if (!EmptyUtils.isEmptyArray(dfDirectFinanceInfoVO.getItemCode())) {
            List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();
            for (int i = 0; i < dfDirectFinanceInfoVO.getItemCode().length; i++) {
                FbsBudgetProcessDTO fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
                if (DfConstant.TEMP == dfDirectFinanceInfoVO.getStatus()) {
                    fbsBudgetProcessDTO.setStatus(BudgetConstant.STATUE_TEMP);// 状态:0暂存,11在途，95占用
                } else {
                    fbsBudgetProcessDTO.setStatus(BudgetConstant.STATUE_WAIT_CHECK);// 状态:0暂存,11在途，95占用
                }
                fbsBudgetProcessDTO.setBillNo(dfDirectFinanceInfoVO.getBillCode());// 业务单据号
                fbsBudgetProcessDTO.setBillId(dfDirectFinanceInfoVO.getId());
                fbsBudgetProcessDTO.setItemCode(dfDirectFinanceInfoVO.getItemCode()[i]);// 科目代码
                fbsBudgetProcessDTO.setItemName(dfDirectFinanceInfoVO.getItemName()[i]);// 科目名称
                fbsBudgetProcessDTO.setProcessMoney(dfDirectFinanceInfoVO.getProcessMoney()[i]);// 执行金额
                fbsBudgetProcessDTO.setProcessDate(dfDirectFinanceInfoVO.getCreateDate());// 执行日期
                fbsBudgetProcessDTO.setCorpId(dfDirectFinanceInfoVO.getCorpId());// 单位id
                fbsBudgetProcessDTO.setCurId(dfDirectFinanceInfoVO.getCurId());// 币别
                fbsBudgetProcessDTO.setBusinessName(BudgetConstant.BUSINESS_NAME_DF_INFO);// 执行业务名称
                fbsBudgetProcessDTO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_CMS);// 执行系统名称
                fbsBudgetProcessDTO.setCreateBy(user.getLoginName());
                fbsBudgetProcessDTO.setCreateDate(DateUtils.getDate());
                list.add(fbsBudgetProcessDTO);
            }
            // 修改时，先删除之前的计划执行数据再保存
            fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_DF_INFO,
                    BudgetConstant.PROCESS_SYSNAME_CMS, dfDirectFinanceInfoVO.getBillCode());
            fbsBudgetProcessService.writeBudgetProcess(list, user);
        }
    }
    /**
     * 更新资金计划挂接
     *
     * @return
     * @Param dfDirectFinanceInfo
     * @Param user
     */
    public void updateBudget(DfDirectFinanceInfo dfDirectFinanceInfo, ICurrentUser user) {
        if (DfConstant.IS_TRUE.equals(getIsBudget().getValue())) {
            if (DfConstant.APPROVED == dfDirectFinanceInfo.getStatus()) {
                fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_DF_INFO,
                        BudgetConstant.PROCESS_SYSNAME_CMS, dfDirectFinanceInfo.getBillCode(),
                        BudgetConstant.STATUE_CHECK, user);
            } else {
                fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_DF_INFO,
                        BudgetConstant.PROCESS_SYSNAME_CMS, dfDirectFinanceInfo.getBillCode(),
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
            fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_DF_INFO,
                    BudgetConstant.PROCESS_SYSNAME_CMS, billCode);
        }
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    /**
     * 直融业务单位删除校验
     *
     * @return
     * @Param corpId
     */
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
        List<CorpVerifyErrorDTO> corpVerifyErrorDTOS = new ArrayList<>();
        if (dfDirectFinanceInfoDao.verifyCorpDeleteByCorpId(corpId) > 0) {
            CorpVerifyErrorDTO corpVerifyErrorDTO = new CorpVerifyErrorDTO();
            corpVerifyErrorDTO.setCorpId(corpId);
            corpVerifyErrorDTO.setSysCode(SysConfigConstant.Module.CMS);
            corpVerifyErrorDTO.setBusCode(SysConfigConstant.BusType.CMS_DF);
            corpVerifyErrorDTO.setErrorMsg(multiLangServiceI.getLang(CorpVerifyMessageConstant.CMS_VALID_DEL_DF_ERROR));
            corpVerifyErrorDTOS.add(corpVerifyErrorDTO);
        }
        return corpVerifyErrorDTOS;
    }
    /**
     * 直融业务单位禁用校验
     *
     * @return
     * @Param corpId
     */
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        List<CorpVerifyErrorDTO> corpVerifyErrorDTOS = new ArrayList<>();
        if (dfDirectFinanceInfoDao.verifyCorpDisableByCorpId(corpId) > 0) {
            CorpVerifyErrorDTO corpVerifyErrorDTO = new CorpVerifyErrorDTO();
            corpVerifyErrorDTO.setCorpId(corpId);
            corpVerifyErrorDTO.setSysCode(SysConfigConstant.Module.CMS);
            corpVerifyErrorDTO.setBusCode(SysConfigConstant.BusType.CMS_DF);
            corpVerifyErrorDTO
                    .setErrorMsg(multiLangServiceI.getLang(CorpVerifyMessageConstant.CMS_VALID_DISABLE_DF_ERROR));
            corpVerifyErrorDTOS.add(corpVerifyErrorDTO);
        }
        return corpVerifyErrorDTOS;
    }
    /**
     * <br>
     * <b>功能：</b> 查询直接融资情况 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/12/8 0008 <br>
     */
    public List<FinanceLoanDTO> queryInlFinance(List<String> corpAuth, String curId, Date queryDate) {
        return dfDirectFinanceInfoDao.queryInlFinance(corpAuth, curId, queryDate);
    }
    /**
     * <br>
     * <b>功能：</b> 查询直接融资的合计融资情况 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/12/8 0008 <br>
     */
    public List<FinanceTypeLoanDTO> queryInlTypeFinance(List<String> corpAuth, String curId, Date queryDate) {
        return dfDirectFinanceInfoDao.queryInlTypeFinance(corpAuth, curId, queryDate);
    }
    /**
     * <br>
     * <b>功能：</b> 查询直接融资的利息支持情况 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2020/12/8 0008 <br>
     */
    public List<FinanceLoanDTO> queryInlInterest(List<String> corpAuth, String curId, Date queryDate) {
        return dfDirectFinanceInfoDao.queryInlInterest(corpAuth, curId, queryDate);
    }
}
