package com.bytter.finance.domain.lease.service;

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
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.CmsWorkFlowConstant;
import com.bytter.finance.constant.LeaseConstant;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.finance.domain.directFinance.dao.DfIntermediateCostDao;
import com.bytter.finance.domain.directFinance.entity.DfIntermediateCost;
import com.bytter.finance.domain.lease.dao.LeaseUpdateDao;
import com.bytter.finance.domain.lease.entity.CmsFinancingLeaseInfo;
import com.bytter.finance.domain.lease.entity.LeaseUpdate;
import com.bytter.finance.domain.lease.leaseUtils.LeaseUtils;
import com.bytter.finance.domain.lease.vo.CmsFinancingLeaseInfoVO;
import com.bytter.finance.domain.lease.vo.LeaseListUpdateVO;
import com.bytter.finance.domain.lease.vo.LeaseUpdateVO;
import com.bytter.finance.enums.CmsLeaseEnum;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaPledgeOccupyService;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.guarantee.dto.FcBondDTO;
import com.bytter.guarantee.dto.GuaPledgeOccupyDTO;
import com.bytter.guarantee.enums.FcBondMessageEnum;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
@Service("leaseUpdateService")
@WorkFlow({ CmsWorkFlowConstant.CMS_LEASE_UPDATE })
@DubboService(interfaceClass = IWorkFlowListener.class,group = CmsWorkFlowConstant.CMS_LEASE_UPDATE )
@Transactional
public class LeaseUpdateService extends BaseService<LeaseUpdate, String> implements IWorkFlowListener {
    @Autowired
    private LeaseUpdateDao leaseUpdateDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie guaContractOccupServie;
    @Autowired
    private IWorkFlowService workFlowService;
    @Resource(name = "fcBondHookServiceImp")
    private IFcBondHookService fcBondHookService;
    @Autowired
    private DfIntermediateCostDao dfIntermediateCostDao;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Resource(name = "guaPledgeOccupyServiceImpl")
    private IGuaPledgeOccupyService pledgeOccupyService;
    @Autowired
    private CmsFinancingLeaseInfoService cmsFinancingLeaseInfoService;
    @Override
    public BaseDao<LeaseUpdate, String> getEntityDao() {
        return leaseUpdateDao;
    }
    /**
     * 租赁申请列表数据查询
     *
     * @param dataTablesInput
     * @param user
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getUpdateDatatables(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_LEASE_UPDATE_QUERY, user.getId());
        DataTablesOutput out = leaseUpdateDao.getUpdateDatatables(dataTablesInput, corpAuth);
        for (int i = 0; i < out.getData().size(); i++) {
            LeaseListUpdateVO leaseListUpdateVo = (LeaseListUpdateVO) out.getData().get(i);
            List<LeaseUpdateVO> leaseUpdateVOS = leaseUpdateDao.getExtend(leaseListUpdateVo.getId());
            if (leaseUpdateVOS.size() > 0) {
                leaseListUpdateVo.setModifystatus(leaseUpdateVOS.get(0).getStatus());
                leaseListUpdateVo.setAddLeaseCode(leaseUpdateVOS.get(0).getAddLeaseCode());
                leaseListUpdateVo.setModifyDate(leaseUpdateVOS.get(0).getModifyDate());
                leaseListUpdateVo.setUpdateId(leaseUpdateVOS.get(0).getId());
            }
        }
        return out;
    }
    /**
     * 功能介绍 : 补录审批列表<br>
     *
     * @param dataTablesInput
     *            <b>日期：</b>2022-04-09<br>
     *            <br>
     * @author tongqr
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataUpdateApproveTables(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(CmsWorkFlowConstant.CMS_LEASE_UPDATE,
                dataTablesInput);
        DataTablesOutput dp = leaseUpdateDao.getDataUpdateApproveTables(dataTablesInput,
                workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }
    /**
     * 功能介绍 : 提交或暂存<br>
     *
     * @param vo
     * @param saveType
     *            1提交，0暂存 <b>日期：</b>2000-04-07<br>
     *            <br>
     * @author tongqr
     */
    public void tempOrSubmit(CmsFinancingLeaseInfoVO vo, String saveType, LeaseUpdateVO leaseUpdateVO,
            ICurrentUser user) {
        CmsFinancingLeaseInfo cmsFinancingLeaseInfo = new CmsFinancingLeaseInfo();
        LeaseUpdate leaseUpdate = new LeaseUpdate();
        BeanUtils.copyNotNullProperties(vo, cmsFinancingLeaseInfo);
        BeanUtils.copyNotNullProperties(leaseUpdateVO, leaseUpdate);
        List<CmsFinancingLeaseInfoVO> leaselist = cmsFinancingLeaseInfoService.getLeaseByLeaseCode(vo.getLeaseCode(),
                vo.getId());
        CmsFinancingLeaseInfo lease = cmsFinancingLeaseInfoService.get(cmsFinancingLeaseInfo.getId());
        if (leaselist.size() > 0) {
            throw new ParameterException(CmsLeaseEnum.LEASE_INFO_NO_CHECK);// 合同号已存在
        }
        if ("1".equals(saveType)) {// 提交
            leaseUpdate.setStatus(CmsConstant.LEASE_COMMITAPPROVE_11);
            lease.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_66);
        } else {// 暂存
            leaseUpdate.setStatus(CmsConstant.LEASE_TEMP_0);
            lease.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_65);
        }
        if (StringUtils.isEmpty(leaseUpdate.getId())) {
            this.save(leaseUpdate, user);
        } else {
            this.update(leaseUpdate, user);// 使用saveOrUpdate无法新增数据
        }
        // 担保处理 0暂存，1在审使用，2使用中，3释放,4打回,5发放打回
        if ("1".equals(saveType)) {
            if ("Y".equals(lease.getControlStatus())) {
                throw new ParameterException(CmsLeaseEnum.CMS_BASEDATA_CHECK);// 已存在其他业务
            }
            lease.setControlStatus("Y");
            cmsFinancingLeaseInfoService.updateEntity(lease);
        }
        BeanUtils.copyNotNullProperties(cmsFinancingLeaseInfo, lease);
        // LeaseUtils.handleGua(lease, null, guaContractOccupServie, 3, null);//释放原本的
        // LeaseUtils.handleGua(cmsFinancingLeaseInfo, lease, guaContractOccupServie, 0, vo.getGuaContract());
        // 判断是否挂接了保证金
        if ((vo.getIsHookBond() != null) && (vo.getIsHookBond().equals(FcBondConstant.IS_HOOK_Y))) {
            // 判断是新增还是修改,查询结果是包含了已删除的
            FcBondApplyDTO fcBondApplyDTO = fcBondHookService.getFcBondApply(cmsFinancingLeaseInfo.getId(),
                    cmsFinancingLeaseInfo.getLeaseNo(), vo.getBondId());
            FcBondApplyDTO fcBondApplyDTOPre = fcBondHookService.getFcBondApply(cmsFinancingLeaseInfo.getId(),
                    cmsFinancingLeaseInfo.getLeaseNo(), vo.getBondIdPre());
            BigDecimal availMoney = null;
            // 判断占用金额是否大于可用余额
            FcBondDTO fcBondDTO = fcBondHookService.getFcBondMsgById(vo.getBondId());
            BigDecimal applyMoney = cmsFinancingLeaseInfo.getOriBailmoney();
            if (fcBondApplyDTOPre == null) {// 之前未挂接过保证金
                availMoney = fcBondDTO.getAvailMoney();
            } else {
                availMoney = fcBondDTO.getAvailMoney().add(fcBondApplyDTOPre.getApplyMoney());// 编辑状态可用金额要加上修改之前的额度
            }
            BigDecimal dif = availMoney.subtract(applyMoney);
            if ((dif.compareTo(BigDecimal.ZERO)) == -1) {// 占用金额大于可用余额
                throw new ParameterException(FcBondMessageEnum.FCBOND_APPLYMONEY_CHECK);
            }
            if (fcBondApplyDTO != null) {// fcBondApplyDTO不为空即为修改
                cmsFinancingLeaseInfoService.updateFcBondApply(vo, cmsFinancingLeaseInfo, saveType, fcBondApplyDTO);
            } else {// 否则即为新增
                if (fcBondApplyDTOPre != null) {// 查询修改之前占用的保证金信息，不为空进行数据修改（修改状态为释放）
                    fcBondApplyDTOPre.setUpdateBy(user.getLoginName());
                    fcBondApplyDTOPre.setUpdateDate(DateUtils.getDate());
                    fcBondHookService.updateFcBondApplyData(fcBondApplyDTOPre);
                }
                cmsFinancingLeaseInfoService.addFcBondApply(vo, cmsFinancingLeaseInfo, saveType);// 新增占用信息
            }
        }
        // 保存第三方费率信息
        if (vo.getCostTypeDtl().size() > 0) {
            Map<String, Object> param = new HashMap<>();
            param.put("id", vo.getId());
            dfIntermediateCostDao.deleteIntermediateCost(param);
            List<DfIntermediateCost> costList = new ArrayList<>();
            for (DfIntermediateCost cost : vo.getCostTypeDtl()) {
                if (!cost.getCostTypeId().trim().equals("")) {
                    cost.setDirectFinanceId(lease.getId());
                    costList.add(cost);
                }
            }
            dfIntermediateCostDao.batchSave(costList);
        }
        if ((vo.getIsHookBond() == null) || (vo.getIsHookBond().equals(FcBondConstant.IS_HOOK_N))) {// 不挂保证金
            FcBondApplyDTO fcBondApplyDTO = fcBondHookService.getFcBondApplyByItemIds(cmsFinancingLeaseInfo.getId(),
                    cmsFinancingLeaseInfo.getLeaseNo());
            if (fcBondApplyDTO != null) {// fcBondApplyDTO不为空,修改保证金的状态为已释放
                fcBondApplyDTO.setStatus(FcBondConstant.STATUE_RELEASE);
                fcBondApplyDTO.setUpdateBy(cmsFinancingLeaseInfo.getUpdateBy());
                fcBondApplyDTO.setUpdateDate(DateUtils.getDate());
                fcBondHookService.updateFcBondApply(fcBondApplyDTO);
            }
        }
        if (vo.getIsRelatePledge() != null && vo.getIsRelatePledge().equals(CmsConstant.IS_TRUE)) {// 挂接抵质押物
            List<GuaPledgeOccupyDTO> newOccupyDTOS = cmsFinancingLeaseInfoService.setPledgeOccupyInfo(vo,
                    lease.getId());
            pledgeOccupyService.freezePledgeOccupy(newOccupyDTOS);// 设置抵质押物占用数据
        } else {// 将原占用信息释放
            pledgeOccupyService.releasePledgeOccupy(cmsFinancingLeaseInfo.getId(), vo.getLeaseNo());
        }
        notificationService.complete(NotificationConstant.CMS.LEASE_UPDATE, leaseUpdate.getId(), user.getId());
        if ("1".equals(saveType)) {// 提交 启动审批流程
             getStartHandler().handler(CmsWorkFlowConstant.CMS_LEASE_UPDATE, leaseUpdate.getId(), leaseUpdate,
                    leaseUpdate.getCorpId(), user.getId());
        }
    }
    public BigDecimal getRePayMoney(String leaseId) {
        return leaseUpdateDao.getRePayMoney(leaseId);
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();
        String processDefinitionKey = workFlowResult.getProcessDefinitionKey();
        boolean result = workFlowResult.isPass();
        ICurrentUser user = workFlowResult.getUser();
        LeaseUpdate leaseUpdate = this.get(businessKey,true);
        CmsFinancingLeaseInfo lease = new CmsFinancingLeaseInfo();
        if (CmsWorkFlowConstant.CMS_LEASE_UPDATE.equals(processDefinitionKey)) {// 变更审批
            if (result) {// 审批通过
                leaseUpdate.setStatus(CmsConstant.APPROVED);
                BeanUtils.copyNotNullProperties(leaseUpdate, lease);
                CmsFinancingLeaseInfo cmsInfo = cmsFinancingLeaseInfoService.get(leaseUpdate.getLeaseId());
                lease.setId(leaseUpdate.getLeaseId());
                lease.setStatus(cmsInfo.getStatus());
                lease.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_106);
                lease.setExpendedCount(cmsInfo.getExpendedCount());
                lease.setContinueCount(cmsInfo.getContinueCount());
                // 担保处理 0暂存，1在审使用，2使用中，3释放,4打回,5发放打回
                guaContractOccupServie.deleteGuaContractOccup(lease.getId(), LeaseConstant.LEASE_GUA_SYSCODE);// 释放原本的
                LeaseUtils.handleGua(lease, null, guaContractOccupServie, 1, lease.getGuaContract());
                LeaseUtils.handleGua(lease, null, guaContractOccupServie, 2, null);
                /*** 抵质押物处理开始(补录审批通过后正式占用额度) **/
                if (lease.getIsRelatePledge() != null && lease.getIsRelatePledge().equals(CmsConstant.IS_TRUE)) {// 挂接抵质押物
                    List<GuaPledgeOccupyDTO> occupyDTOS = pledgeOccupyService.getPledgeOccupy(lease.getId(),
                            lease.getLeaseNo());
                    pledgeOccupyService.addPledgeOccupy(occupyDTOS);
                }
                /*** 抵质押物处理结束 **/
            } else {// 打回
                leaseUpdate.setStatus(CmsConstant.BACK);
                lease = cmsFinancingLeaseInfoService.get(leaseUpdate.getLeaseId());
                lease.setBizStatus(CmsConstant.LEASE_BIZ_STATUS__13);
                // msService.insertProcessMsg(loan.getBillCode(),ProvideConstant.INFO_BILL_CODE_PREFIX,
                // CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_ROLLBACK+"" ,user);
                String submitUser = StringUtils.isEmpty(leaseUpdate.getUpdateBy()) ? leaseUpdate.getCreateBy()
                        : leaseUpdate.getUpdateBy();// 提交人
                notificationService.sendToUser(leaseUpdate.getId(), submitUser, NotificationConstant.CMS.LEASE_UPDATE,
                        leaseUpdate);// 添加租赁变更打回的消息提示
            }
            lease.setControlStatus("N");
            cmsFinancingLeaseInfoService.merge(lease);
        }
        this.saveOrUpdate(leaseUpdate);
        this.getSession().flush();
        this.getSession().clear();
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(LeaseUpdate entity, ICurrentUser user) {
        entity.setCreateDate(new Date());
        entity.setCreateBy(user.getId());
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(LeaseUpdate entity, ICurrentUser user) {
        entity.setUpdateDate(new Date());
        entity.setUpdateBy(user.getId());
        super.updateEntity(entity);
    }
}
