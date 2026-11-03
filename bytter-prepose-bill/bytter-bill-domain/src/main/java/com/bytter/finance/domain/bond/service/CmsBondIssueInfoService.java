package com.bytter.finance.domain.bond.service;

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
import com.bytter.core.constant.AccebillConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.CmsProcessMsgConstant;
import com.bytter.finance.constant.CmsWorkFlowConstant;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.bond.dao.CmsBondIssueInfoDao;
import com.bytter.finance.domain.bond.entity.CmsBondIssueInfo;
import com.bytter.finance.domain.bond.entity.CmsBondPay;
import com.bytter.finance.domain.bond.entity.CmsBondRegisterInfo;
import com.bytter.finance.domain.bond.entity.CmsBondRepayPlan;
import com.bytter.finance.domain.bond.utils.BondUtils;
import com.bytter.finance.domain.bond.vo.CmsBondIssueInfoVo;
import com.bytter.finance.domain.bond.vo.CmsBondRepayPlanDtlVo;
import com.bytter.finance.domain.bond.vo.CmsBondRepayPlanVo;
import com.bytter.finance.domain.directFinance.dao.DfIntermediateCostDao;
import com.bytter.finance.domain.directFinance.entity.DfIntermediateCost;
import com.bytter.finance.domain.directFinance.vo.DfIntermediateCostVO;
import com.bytter.finance.dto.GuaContractCmsBondDTO;
import com.bytter.finance.enums.BaseDataMessageEnum;
import com.bytter.finance.enums.BondMessageEnum;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaPledgeOccupyService;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.guarantee.dto.FcBondDTO;
import com.bytter.guarantee.dto.GuaPledgeOccupyDTO;
import com.bytter.guarantee.enums.FcBondMessageEnum;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.workflow.api.IWorkFlowService;
import com.google.common.collect.Lists;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
/**
 * 债券发行service
 *
 * @author ouyj
 */
@Service("cmsBondIssueInfoService")
@Transactional(rollbackFor = Exception.class)
@WorkFlow(CmsWorkFlowConstant.CMS_BOND_ISSUE_INFO)
@DubboService(interfaceClass = IWorkFlowListener.class,group = CmsWorkFlowConstant.CMS_BOND_ISSUE_INFO)
public class CmsBondIssueInfoService extends BaseService<CmsBondIssueInfo, String> implements IWorkFlowListener {
    @Autowired
    private CmsBondPayService cmsBondPayService;
    @Autowired
    private CmsBondIssueInfoDao cmsBondIssueInfoDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private IWorkFlowService workFlowService;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie guaContractOccupyService;
    @Resource(name = "fcBondHookServiceImp")
    private IFcBondHookService fcBondHookService;
    @Resource(name = "guaPledgeOccupyServiceImpl")
    private IGuaPledgeOccupyService pledgeOccupyService;
    @Autowired
    private CmsProcessMsgService cmsLogService;
    @Autowired
    private DfIntermediateCostDao dfIntermediateCostDao;
    @Autowired
    private CmsBondRegisterInfoService cmsBondRegisterInfoService;
    @Autowired
    private CmsBondRepayPlanService cmsBondRepayPlanService;
    @Autowired
    private CmsBondRepayPlanDtlService cmsBondRepayPlanDtlService;
    @Override
    public BaseDao<CmsBondIssueInfo, String> getEntityDao() {
        return cmsBondIssueInfoDao;
    }
    /**
     * 获取债券发行列表数据
     *
     * @param dataTablesInput
     *            前端参数
     * @param user
     *            当前用户
     * @return 债券发行列表数据
     */
    public DataTablesOutput getDataTable(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpList = corpService.getUserCorpIds(ResourceConstant.CMS_BOND_ISSUE_QUERY, user.getId());
        return cmsBondIssueInfoDao.getDataTable(dataTablesInput, corpList, false);
    }
    /**
     * 获取债券发行查询列表数据
     *
     * @param dataTablesInput
     *            前端参数
     * @param user
     *            当前用户
     * @return 获取债券发行查询列表数据
     */
    public DataTablesOutput getQueryDataTable(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpList = corpService.getUserCorpIds(ResourceConstant.CMS_BOND_ISSUE_QUERYLIST, user.getId());
        return cmsBondIssueInfoDao.getDataTable(dataTablesInput, corpList, true);
    }
    /**
     * 获取债券发行详情列表数据
     *
     * @param dataTablesInput
     *            前端参数
     * @return 获取债券发行详情列表数据
     */
    public DataTablesOutput getIssueDtlDatatable(DataTablesInput dataTablesInput) {
        return cmsBondIssueInfoDao.getIssueDtlDatatable(dataTablesInput);
    }
    /**
     * 债券发行审批列表数据
     *
     * @param input
     *            前端查询参数
     * @return 债券发行审批列表数据
     */
    public DataTablesOutput dataTableApprove(DataTablesInput input) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(CmsWorkFlowConstant.CMS_BOND_ISSUE_INFO,
                input);
        DataTablesOutput dp = cmsBondIssueInfoDao.dataTableApprove(input, workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }
    /**
     * 债券发行根据债券注册id查询第一次发行的信息
     *
     * @param registerId
     * @return
     */
    public CmsBondIssueInfoVo getByRegisterId(String registerId) {
        if (EmptyUtils.isEmpty(registerId)) {
            return new CmsBondIssueInfoVo();
        }
        List<CmsBondIssueInfoVo> list = cmsBondIssueInfoDao.getRegisterById(registerId);
        if (ListUtils.isNullOrEmpty(list)) {
            return new CmsBondIssueInfoVo();
        }
        return list.get(0);
    }
    public CmsBondIssueInfoVo getByIssueId(String issueId) {
        return cmsBondIssueInfoDao.getByIssueId(issueId);
    }
    public List<CmsBondIssueInfoVo> getRegisterById(String registerId) {
        return cmsBondIssueInfoDao.getRegisterById(registerId);
    }
    /**
     * 根据债券Id获取债券发行信息，包含发行未还本金
     *
     * @param registerId
     *            债劵注册id
     * @return
     */
    public List<CmsBondIssueInfoVo> getRegisterAndUnpaidMoneyById(String registerId) {
        List<CmsBondIssueInfoVo> list = cmsBondIssueInfoDao.getRegisterById(registerId);
        for (CmsBondIssueInfoVo vo : list) {
            // 最大输入金额控制
            BigDecimal sendBalance = cmsBondPayService.completeSendBalance(vo.getId(), null);
            vo.setUnpaidMoney(sendBalance);
        }
        return list;
    }
    /**
     * 根据债券Id获取有效的债券发行编号
     *
     * @return 有效的债券类型集合
     */
    public List<CmsBondIssueInfoVo> listValidBondIssueNum(String registerId) {
        return cmsBondIssueInfoDao.listValidBondIssueNum(registerId);
    }
    /**
     * 根据债券Id获取有效的债券发行编号
     *
     * @return 有效的债券类型集合
     */
    public List<CmsBondIssueInfoVo> listValidBondIssueByRegisterId(String registerId) {
        return cmsBondIssueInfoDao.listValidBondIssueByRegisterId(registerId);
    }
    /**
     * 新增债券发行数据
     *
     * @param issueInfoVo
     *            债券发行实体
     * @param user
     *            当前用户
     */
    public void saveBondIssueInfo(CmsBondIssueInfoVo issueInfoVo, ICurrentUser user) {
        CmsBondIssueInfo issueInfo = BeanUtils.copyNewInstanceProperties(issueInfoVo, CmsBondIssueInfo.class);
        if (!CollectionUtils.isEmpty(cmsBondIssueInfoDao.getBondByBondIssueNum(issueInfoVo.getBondIssueNum(), null))) {
            throw new ParameterException(BondMessageEnum.CMS_BASEDATA_ISSUE_ISEXIST);
        }
        // 担保挂接处理状态
        int guaStatus = 0;
        int messageType = CmsProcessMsgConstant.MSGA_INPUT;
        String msg = String.valueOf(CmsProcessMsgConstant.MSGA_INPUT);
        if (CmsConstant.COMMITAPPROVE == issueInfo.getStatus()) {
            messageType = CmsProcessMsgConstant.MSGA_SUBMIT;
            msg = String.valueOf(CmsProcessMsgConstant.MSGA_SUBMIT);
            issueInfo.setBizStatus(CmsConstant.BOND_BIZ_STATUS_11);
            guaStatus = 1;
        } else {
            issueInfo.setBizStatus(CmsConstant.BOND_BIZ_STATUS_0);
        }
        issueInfo.setCreateBy(user.getId());
        issueInfo.setCreateDate(new Date());
        this.saveEntity(issueInfo);
        issueInfoVo.setId(issueInfo.getId());
        String planId = issueInfoVo.getPlanId();
        if (StringUtils.isNotEmpty(planId)) {
            CmsBondRepayPlan cmsBondRepayPlan = cmsBondRepayPlanService.get(planId);
            cmsBondRepayPlan.setIssueId(issueInfo.getId());
            cmsBondRepayPlanService.save(cmsBondRepayPlan);
        }
        // 担保处理 0暂存，1在审使用，2使用中，3释放,4打回,5发放打回
        BondUtils.handleGua(issueInfo, null, guaContractOccupyService, guaStatus, issueInfo.getGuaranteeContract());
        // 判断是否挂接了保证金
        if ((issueInfoVo.getIsHookBail() != null) && (issueInfoVo.getIsHookBail().equals(FcBondConstant.IS_HOOK_Y))) {
            BigDecimal availMoney;
            // 判断占用金额是否大于可用余额
            FcBondDTO fcBondDTO = fcBondHookService.getFcBondMsgById(issueInfoVo.getBondId());
            BigDecimal applyMoney = issueInfoVo.getOriBailMoney();
            availMoney = fcBondDTO.getAvailMoney();
            BigDecimal dif = availMoney.subtract(applyMoney);
            if ((dif.compareTo(BigDecimal.ZERO)) < 0) {
                // 占用金额大于可用余额
                throw new ParameterException(FcBondMessageEnum.FCBOND_APPLYMONEY_CHECK);
            }
            // 新增占用信息
            issueInfoVo.setCreateBy(user.getLoginName());
            this.addFcBondApply(issueInfoVo);
        }
        // 保存第三方费率信息
        if (!CollectionUtils.isEmpty(issueInfoVo.getCostTypeDtl())) {
            Map<String, Object> param = new HashMap<>(1);
            param.put("id", issueInfoVo.getId());
            dfIntermediateCostDao.deleteIntermediateCost(param);
            List<DfIntermediateCost> costList = new ArrayList<>();
            for (DfIntermediateCost cost : issueInfoVo.getCostTypeDtl()) {
                if (!"".equals(cost.getCostTypeId().trim())) {
                    cost.setDirectFinanceId(issueInfoVo.getId());
                    costList.add(cost);
                }
            }
            dfIntermediateCostDao.batchSave(costList);
        }
        // 挂接抵质押物
        if (issueInfoVo.getIsHookPledge() != null && issueInfoVo.getIsHookPledge().equals(CmsConstant.IS_TRUE)) {
            // 设置抵质押物占用数据
            pledgeOccupyService.freezePledgeOccupy(this.setPledgeOccupyInfo(issueInfoVo));
        }
        // 启动工作流
        if (CmsConstant.COMMITAPPROVE == issueInfo.getStatus()) {
            CmsBondRegisterInfo registerInfo = cmsBondRegisterInfoService.get(issueInfo.getRegisterId(), true);
             getStartHandler().handler(CmsWorkFlowConstant.CMS_BOND_ISSUE_INFO, issueInfoVo.getId(), issueInfoVo,
                    registerInfo.getCorpId(), user.getId());
        }
        cmsLogService.insertProcessMsg(issueInfoVo.getId(), CmsConstant.BOND_ISSUE_BILL_CODE_PREFIX, messageType, msg,
                user);
    }
    /**
     * 修改债券发行数据
     *
     * @param issueInfoVo
     *            债券发行实体
     * @param user
     *            当前用户
     */
    public void updateBondIssueInfo(CmsBondIssueInfoVo issueInfoVo, ICurrentUser user) {
        CmsBondIssueInfo issueInfo = BeanUtils.copyNewInstanceProperties(issueInfoVo, CmsBondIssueInfo.class);
        if (!CollectionUtils.isEmpty(
                cmsBondIssueInfoDao.getBondByBondIssueNum(issueInfoVo.getBondIssueNum(), issueInfoVo.getId()))) {
            throw new ParameterException(BondMessageEnum.CMS_BASEDATA_ISSUE_ISEXIST);
        }
        // 担保挂接处理状态
        int guaStatus = 0;
        int messageType = CmsProcessMsgConstant.MSGA_INPUT;
        String msg = String.valueOf(CmsProcessMsgConstant.MSGA_INPUT);
        if (CmsConstant.COMMITAPPROVE == issueInfo.getStatus()) {
            messageType = CmsProcessMsgConstant.MSGA_SUBMIT;
            msg = String.valueOf(CmsProcessMsgConstant.MSGA_SUBMIT);
            issueInfo.setBizStatus(CmsConstant.BOND_BIZ_STATUS_11);
            guaStatus = 1;
        } else {
            issueInfo.setBizStatus(CmsConstant.BOND_BIZ_STATUS_0);
        }
        CmsBondIssueInfo oldIssueInfo = this.get(issueInfo.getId(), true);
        BeanUtils.copyNotNullProperties(issueInfoVo, oldIssueInfo);
        oldIssueInfo.setUpdateBy(user.getId());
        oldIssueInfo.setUpdateDate(new Date());
        this.updateEntity(oldIssueInfo);
        String planId = issueInfoVo.getPlanId();
        if (StringUtils.isNotEmpty(planId)) {
            // 如果修改了发行编号或发行金额则删除之前还款计划
            String issueId = issueInfoVo.getId();
            CmsBondRepayPlanVo cmsBondRepayPlanVo = cmsBondRepayPlanService.getByIssueId(issueId);
            String oldPlanId = cmsBondRepayPlanVo.getId();
            if (StringUtils.isNotEmpty(oldPlanId) && !oldPlanId.equals(planId)) {
                cmsBondRepayPlanDtlService.deleteBondRepayPlanDtl(Lists.newArrayList(oldPlanId));
                cmsBondRepayPlanService.delete(oldPlanId);
            }
            CmsBondRepayPlan cmsBondRepayPlan = cmsBondRepayPlanService.get(planId);
            cmsBondRepayPlan.setIssueId(issueInfo.getId());
            cmsBondRepayPlanService.saveOrUpdate(cmsBondRepayPlan);
        }
        // 担保处理 0暂存，1在审使用，2使用中，3释放,4打回,5发放打回
        BondUtils.handleGua(issueInfo, oldIssueInfo, guaContractOccupyService, guaStatus,
                issueInfo.getGuaranteeContract());
        // 判断是否挂接了保证金
        if ((issueInfoVo.getIsHookBail() != null) && (issueInfoVo.getIsHookBail().equals(FcBondConstant.IS_HOOK_Y))) {
            // 判断是新增还是修改,查询结果是包含了已删除的
            FcBondApplyDTO fcBondApplyDTO = fcBondHookService.getFcBondApply(issueInfoVo.getId(),
                    issueInfoVo.getBondIssueNum(), issueInfoVo.getBondId());
            FcBondApplyDTO fcBondApplyPre = fcBondHookService.getFcBondApply(issueInfoVo.getId(),
                    issueInfoVo.getBondIssueNum(), issueInfoVo.getBondIdPre());
            BigDecimal availMoney = null;
            // 判断占用金额是否大于可用余额
            FcBondDTO fcBondDTO = fcBondHookService.getFcBondMsgById(issueInfoVo.getBondId());
            BigDecimal applyMoney = issueInfoVo.getOriBailMoney();
            if (fcBondApplyPre == null) {
                // 之前未挂接过保证金
                availMoney = fcBondDTO.getAvailMoney();
            } else {
                // 编辑状态可用金额要加上修改之前的额度
                availMoney = fcBondDTO.getAvailMoney().add(fcBondApplyPre.getApplyMoney());
            }
            BigDecimal dif = availMoney.subtract(applyMoney);
            if ((dif.compareTo(BigDecimal.ZERO)) < 0) {
                // 占用金额大于可用余额
                throw new ParameterException(FcBondMessageEnum.FCBOND_APPLYMONEY_CHECK);
            }
            if (fcBondApplyDTO != null) {
                // fcBondApplyDTO不为空即为修改
                this.updateFcBondApply(issueInfoVo, fcBondApplyDTO);
            } else {// 否则即为新增
                if (fcBondApplyPre != null) {
                    // 查询修改之前占用的保证金信息，不为空进行数据修改（修改状态为释放）
                    fcBondApplyPre.setUpdateBy(user.getLoginName());
                    fcBondApplyPre.setUpdateDate(DateUtils.getDate());
                    fcBondHookService.updateFcBondApplyData(fcBondApplyPre);
                }
                // 新增占用信息
                this.addFcBondApply(issueInfoVo);
            }
        }
        // 保存第三方费率信息
        if (!CollectionUtils.isEmpty(issueInfoVo.getCostTypeDtl())) {
            Map<String, Object> param = new HashMap<>(1);
            param.put("id", issueInfoVo.getId());
            dfIntermediateCostDao.deleteIntermediateCost(param);
            List<DfIntermediateCost> costList = new ArrayList<>();
            for (DfIntermediateCost cost : issueInfoVo.getCostTypeDtl()) {
                if (!"".equals(cost.getCostTypeId().trim())) {
                    cost.setDirectFinanceId(issueInfoVo.getId());
                    costList.add(cost);
                }
            }
            dfIntermediateCostDao.batchSave(costList);
        }
        // 挂接抵质押物
        if (issueInfoVo.getIsHookPledge() != null && issueInfoVo.getIsHookPledge().equals(CmsConstant.IS_TRUE)) {
            // 设置抵质押物占用数据
            pledgeOccupyService.freezePledgeOccupy(this.setPledgeOccupyInfo(issueInfoVo));
        } else {
            // 将原占用信息释放
            pledgeOccupyService.releasePledgeOccupy(issueInfoVo.getId(), issueInfoVo.getBondIssueNum());
        }
        // 启动工作流
        if (CmsConstant.COMMITAPPROVE == issueInfo.getStatus()) {
            CmsBondRegisterInfo registerInfo = cmsBondRegisterInfoService.get(issueInfo.getRegisterId(), true);
             getStartHandler().handler(CmsWorkFlowConstant.CMS_BOND_ISSUE_INFO, issueInfoVo.getId(), issueInfoVo,
                    registerInfo.getCorpId(), user.getId());
        }
        cmsLogService.insertProcessMsg(issueInfoVo.getId(), CmsConstant.BOND_ISSUE_BILL_CODE_PREFIX, messageType, msg,
                user);
    }
    /**
     * 新增保证金占用信息
     *
     * @param vo
     *            债券发行vo
     */
    public void addFcBondApply(CmsBondIssueInfoVo vo) {
        FcBondApplyDTO dto = this.setFcBondApplyDTO(vo, new FcBondApplyDTO());
        dto.setCreateBy(vo.getCreateBy());
        dto.setCreateDate(DateUtils.getDate());
        fcBondHookService.addFcBondApply(dto);
    }
    /**
     * 更新保证金占用信息
     *
     * @param vo
     *            债券发行vo
     * @param dto
     *            保证金信息
     */
    public void updateFcBondApply(CmsBondIssueInfoVo vo, FcBondApplyDTO dto) {
        FcBondApplyDTO fcBondApplyDTO = this.setFcBondApplyDTO(vo, dto);
        fcBondApplyDTO.setUpdateBy(vo.getUpdateBy());
        fcBondApplyDTO.setUpdateDate(DateUtils.getDate());
        fcBondHookService.updateFcBondApply(fcBondApplyDTO);
    }
    /**
     * 设置保证金占用信息的值
     *
     * @param vo
     *            债券发行vo
     * @return 保证金信息
     */
    public FcBondApplyDTO setFcBondApplyDTO(CmsBondIssueInfoVo vo, FcBondApplyDTO dto) {
        // 占用类型0:业务占用，1手工占用
        dto.setApplyType(FcBondConstant.APPLY_TYPE_BUSINESS);
        // 0银行授信1银行借款2承兑汇票7融资租赁8债券
        dto.setItemType(FcBondConstant.ITEM_TYPE_BOND);
        // 项目单号
        dto.setItemNo(vo.getBondIssueNum());
        // 占用保证金的项目id
        dto.setItemId(vo.getId());
        // 单位id
        dto.setCorpId(vo.getCorpId());
        // 项目金额
        dto.setItemMoney(vo.getRmbIssueTotal());
        // 保证金比例
        dto.setBondPercent(vo.getBailMoneyRate());
        // 项目开始时间
        dto.setStartDate(vo.getIssueStartDate());
        // 项目结束时间
        dto.setEndDate(vo.getIssueEndDate());
        // 保证金合同
        dto.setBondContract(vo.getBailMoneyContract());
        // 保证金id
        dto.setBondId(vo.getBondId());
        // 保证金金额,即占用金额
        dto.setApplyMoney(vo.getOriBailMoney());
        // 备注
        dto.setRemark(vo.getIssueRmk());
        // 状态为占用
        dto.setStatus(FcBondConstant.STATUE_APPLY);
        dto.setCorpId(vo.getCorpId());
        dto.setCorpName(vo.getCorpName());
        return dto;
    }
    /**
     * 设置占用抵质押物数据
     *
     * @param issueInfoVo
     *            债券发行vo
     * @return 抵质押物集合
     */
    public List<GuaPledgeOccupyDTO> setPledgeOccupyInfo(CmsBondIssueInfoVo issueInfoVo) {
        List<GuaPledgeOccupyDTO> pledgeOccupyList = new ArrayList<>();
        if (!EmptyUtils.isEmptyArray(issueInfoVo.getPledgeId()) && issueInfoVo.getPledgeId().length > 0) {
            for (int i = 0; i < issueInfoVo.getPledgeId().length; i++) {
                if (EmptyUtils.isEmpty(issueInfoVo.getPledgeId()[i])
                        || EmptyUtils.isEmpty(issueInfoVo.getPledgeMoney()[i])) {
                    continue;
                }
                GuaPledgeOccupyDTO pledgeOccupyDTO = new GuaPledgeOccupyDTO();
                pledgeOccupyDTO.setSysCode(ConfigConstant.CMSBOND);
                pledgeOccupyDTO.setBusinessId(issueInfoVo.getId());
                pledgeOccupyDTO.setItemNo(issueInfoVo.getBondIssueNum());
                pledgeOccupyDTO.setStartDate(issueInfoVo.getIssueStartDate());
                pledgeOccupyDTO.setEndDate(issueInfoVo.getIssueEndDate());
                pledgeOccupyDTO.setPledgeId(issueInfoVo.getPledgeId()[i]);
                pledgeOccupyDTO.setOccupyMoney(issueInfoVo.getPledgeMoney()[i]);
                pledgeOccupyDTO.setCurId(issueInfoVo.getBondCur());
                pledgeOccupyList.add(pledgeOccupyDTO);
            }
        }
        return pledgeOccupyList;
    }
    /**
     * 根据债券发行id查询对应第三方费用信息
     *
     * @param id
     *            债券发行id
     * @return 第三方费用信息
     */
    public List<DfIntermediateCostVO> listIntermediateCostByDfId(String id) {
        Map<String, Object> param = new HashMap<>(1);
        param.put("directFinanceId", id);
        return dfIntermediateCostDao.listIntermediateCostByDfId(param);
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();
        boolean result = workFlowResult.isPass();
        ICurrentUser user = workFlowResult.getUser();
        if (StringUtils.isEmpty(businessKey)) {
            throw new BusinessException(BaseDataMessageEnum.MESSAGE_ERROR_VALISNOTNULL);
        }
        CmsBondIssueInfo issueInfo = this.get(businessKey,true);
        int messageType = result ? CmsProcessMsgConstant.MSGA_BOND_ISSUE_CHECKOK
                : CmsProcessMsgConstant.MSGA_BOND_ISSUE_CHECKBACK;
        String msg = String.valueOf(result ? CmsProcessMsgConstant.MSGA_BOND_ISSUE_CHECKOK
                : CmsProcessMsgConstant.MSGA_BOND_ISSUE_CHECKBACK);
        int guaStatus;
        // 发行金额
        BigDecimal issueMoney = issueInfo.getIssueMoney();
        // 总已还金额
        BigDecimal sumMoney = BigDecimal.ZERO;
        if (result) {
            // 审批通过
            issueInfo.setStatus(CmsConstant.APPROVED);
            issueInfo.setBizStatus(CmsConstant.BOND_BIZ_STATUS_95);
            // 挂接的还款计划状态修改为审批通过
            CmsBondRepayPlanVo cmsBondRepayPlanVo = cmsBondRepayPlanService.getByIssueId(businessKey);
            if (StringUtils.isNotEmpty(cmsBondRepayPlanVo.getId())) {
                CmsBondRepayPlan cmsBondRepayPlan = BeanUtils.copyNewInstanceProperties(cmsBondRepayPlanVo,
                        CmsBondRepayPlan.class);
                cmsBondRepayPlan.setStatus(CmsConstant.LEASE_APPROVED_95);
                cmsBondRepayPlan.setIsSave(CmsConstant.IS_FALSE);
                cmsBondRepayPlan.setUpdateDate(new Date());
                cmsBondRepayPlan.setUpdateBy(user.getId());
                cmsBondRepayPlanService.updateEntity(cmsBondRepayPlan);
                // 已还还款计划明细需要新增还款处理数据
                List<CmsBondRepayPlanDtlVo> cmsBondRepayPlanDtlVoList = cmsBondRepayPlanDtlService
                        .listPlanDtlByIssueId(businessKey);
                for (CmsBondRepayPlanDtlVo delVo : cmsBondRepayPlanDtlVoList) {
                    if (CmsConstant.REPAYMENT_SCHEDULE_ISREPAYYES.equals(delVo.getIsRepay())) {
                        // 新增还款处理数据
                        CmsBondPay cmsBondPay = new CmsBondPay();
                        cmsBondPay.setRegisterId(cmsBondRepayPlan.getRegisterId());
                        cmsBondPay.setIssueId(cmsBondRepayPlan.getIssueId());
                        cmsBondPay.setOnlineSign(CmsConstant.IS_FALSE);
                        cmsBondPay.setRepayMoney(delVo.getRepayAmount());
                        cmsBondPay.setRepayTotalMoney(delVo.getRepayAmount());
                        cmsBondPay.setStatus(CmsConstant.APPROVED);
                        cmsBondPay.setIsPayInterest(CmsConstant.IS_FALSE);
                        cmsBondPay.setPayDate(delVo.getRepayDate());
                        cmsBondPay.setCreateBy(user.getLoginName());
                        cmsBondPay.setCreateDate(new Date());
                        cmsBondPayService.saveEntity(cmsBondPay);
                        sumMoney = sumMoney.add(delVo.getRepayAmount());
                        // 修改还款单id
                        cmsBondRepayPlanDtlService.updatePayId(delVo.getId(), cmsBondPay.getId());
                    }
                }
            } else {
                // 如果没有挂接还款计划，则新增一笔空的还款计划主表数据
                CmsBondRepayPlan cmsBondRepayPlan = new CmsBondRepayPlan();
                cmsBondRepayPlan.setIsSave(CmsConstant.IS_TRUE);
                cmsBondRepayPlan.setStatus(CmsConstant.TEMP);
                cmsBondRepayPlan.setRegisterId(issueInfo.getRegisterId());
                cmsBondRepayPlan.setIssueId(issueInfo.getId());
                cmsBondRepayPlan.setCreateDate(new Date());
                cmsBondRepayPlan.setCreateBy(user.getLoginName());
                CmsBondRegisterInfo registerInfo = cmsBondRegisterInfoService.get(issueInfo.getRegisterId(), true);
                cmsBondRepayPlan.setCorpId(registerInfo.getCorpId());
                cmsBondRepayPlanService.save(cmsBondRepayPlan);
            }
            // 担保处理 0暂存，1在审使用，2使用中，3释放,4打回,5发放打回
            guaStatus = 2;
            /// 抵质押物处理开始(补录审批通过后正式占用额度)
            if (issueInfo.getIsHookPledge() != null && issueInfo.getIsHookPledge().equals(CmsConstant.IS_TRUE)) {
                List<GuaPledgeOccupyDTO> occupyList = pledgeOccupyService.getPledgeOccupy(issueInfo.getId(),
                        issueInfo.getBondIssueNum());
                pledgeOccupyService.addPledgeOccupy(occupyList);
            }
        } else {
            // 打回
            issueInfo.setStatus(CmsConstant.BACK);
            issueInfo.setBizStatus(CmsConstant.BOND_BIZ_STATUS__1);
            // 担保处理 0暂存，1在审使用，2使用中，3释放,4打回,5发放打回
            guaStatus = 4;
        }
        BondUtils.handleGua(issueInfo, null, guaContractOccupyService, guaStatus, null);
        cmsLogService.insertProcessMsg(issueInfo.getId(), CmsConstant.BOND_ISSUE_BILL_CODE_PREFIX, messageType, msg,
                user);
        // 如果在发行挂接还款计划中就已经全部还款，则直接释放
        if (issueMoney.compareTo(sumMoney) == 0) {
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
        this.updateEntity(issueInfo);
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    /**
     * 根据发行id删除对应数据
     *
     * @param id
     *            发行id
     * @param user
     *            当前用户
     */
    public void deleteBondIssueInfo(String id, ICurrentUser user) {
        CmsBondIssueInfo issueInfo = this.get(id);
        issueInfo.setIsDelete(CmsConstant.IS_TRUE);
        issueInfo.setStatus(CmsConstant.LEASE_DELETED_2);
        issueInfo.setBizStatus(CmsConstant.BOND_BIZ_STATUS__2);
        issueInfo.setRegisterId(null);
        issueInfo.setUpdateDate(new Date());
        issueInfo.setUpdateBy(user.getId());
        // 如果挂接了保证金,则修改保证金占用信息的状态为"释放"
        FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(issueInfo.getId(), issueInfo.getBondIssueNum());
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
        this.updateEntity(issueInfo);
    }
    public List<CmsBondIssueInfoVo> getRegisterByIdAndOperator(String registerId, String operator) {
        List<CmsBondIssueInfoVo> list = new ArrayList<>();
        List<CmsBondIssueInfoVo> cmsBondIssueInfoVoList = getRegisterById(registerId);
        for (CmsBondIssueInfoVo cmsBondIssueInfoVo : cmsBondIssueInfoVoList) {
            CmsBondRepayPlanVo cmsBondRepayPlanVo = cmsBondRepayPlanService.getByIssueId(cmsBondIssueInfoVo.getId());
            String isSave = cmsBondRepayPlanVo.getIsSave();
            Integer status = cmsBondRepayPlanVo.getStatus();
            // 计划登录的页面的添加
            if (AccebillConstant.BILL_OPERATE_ADD.equals(operator)) {
                if (!CmsConstant.IS_FALSE.equals(isSave) && !CommonConstant.STATUE_WAIT_CHECK.equals(status)) {
                    list.add(cmsBondIssueInfoVo);
                }
            } else {
                if (CmsConstant.IS_FALSE.equals(isSave) && !CommonConstant.STATUE_WAIT_CHECK.equals(status)) {
                    list.add(cmsBondIssueInfoVo);
                }
            }
        }
        return list;
    }
    /**
     * 修改发行表状态
     * 
     * @param issueIds
     * @param bizStatus
     */
    public void updateStatus(List<String> issueIds, String bizStatus) {
        cmsBondIssueInfoDao.updateStatus(issueIds, bizStatus);
    }
    /**
     * 获取所有挂接担保的债券数据
     */
    public List<GuaContractCmsBondDTO> getGuaCmsBond() {
        return cmsBondIssueInfoDao.getGuaCmsBond();
    }
}
