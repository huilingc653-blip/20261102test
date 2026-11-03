package com.bytter.finance.domain.security.service;

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
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.CmsProcessMsgConstant;
import com.bytter.finance.constant.CmsWorkFlowConstant;
import com.bytter.finance.constant.SecurityConstant;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.directFinance.dao.DfIntermediateCostDao;
import com.bytter.finance.domain.directFinance.entity.DfIntermediateCost;
import com.bytter.finance.domain.directFinance.vo.DfIntermediateCostVO;
import com.bytter.finance.domain.security.dao.CmsSecurityIssueDao;
import com.bytter.finance.domain.security.entity.CmsSecurityIssue;
import com.bytter.finance.domain.security.utils.SecurityUtils;
import com.bytter.finance.domain.security.vo.CmsSecurityIssueVO;
import com.bytter.finance.dto.GuaContractCmsSecurityDTO;
import com.bytter.finance.enums.BaseDataMessageEnum;
import com.bytter.finance.enums.SecurityMessageEnum;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaPledgeOccupyService;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.guarantee.dto.FcBondDTO;
import com.bytter.guarantee.dto.GuaPledgeOccupyDTO;
import com.bytter.guarantee.enums.FcBondMessageEnum;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.workflow.api.IWorkFlowService;
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
 * 资产证券化发行
 *
 * @author tongqr
 */
@Service("cmsSecurityIssueService")
@Transactional(rollbackFor = Exception.class)
@WorkFlow(CmsWorkFlowConstant.CMS_SECURITY_ISSUE)
@DubboService(interfaceClass = IWorkFlowListener.class,group = CmsWorkFlowConstant.CMS_SECURITY_ISSUE)
public class CmsSecurityIssueService extends BaseService<CmsSecurityIssue, String> implements IWorkFlowListener {
    @Autowired
    private CmsSecurityIssueDao cmsSecurityIssueDao;
    @Resource(name = "fcBondHookServiceImp")
    private IFcBondHookService fcBondHookService;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie guaContractOccupServie;
    @Autowired
    private DfIntermediateCostDao dfIntermediateCostDao;
    @Resource(name = "guaPledgeOccupyServiceImpl")
    private IGuaPledgeOccupyService pledgeOccupyService;
    @Autowired
    private IWorkFlowService workFlowService;
    @Autowired
    private CmsProcessMsgService cmsLogService;
    @Override
    public BaseDao<CmsSecurityIssue, String> getEntityDao() {
        return cmsSecurityIssueDao;
    }
    /*
     * 根据登记id得到发行明细
     */
    public List<CmsSecurityIssueVO> getIssueForInfoId(String infoId, String flag) {
        return cmsSecurityIssueDao.getIssueForInfoId(infoId, flag);
    }
    /*
     * 根据登记id得到成功的发行明细
     */
    public List<CmsSecurityIssueVO> getSuccessIssueForInfoId(String infoId, List<String> bizStatuss) {
        return cmsSecurityIssueDao.getSuccessIssueForInfoId(infoId, bizStatuss);
    }
    /**
     * 资产证券化登记审批列表数据
     *
     * @param input
     *            前端查询参数
     * @return 资产证券化登记审批列表数据
     */
    public DataTablesOutput dataTableApprove(DataTablesInput input) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(CmsWorkFlowConstant.CMS_SECURITY_ISSUE,
                input);
        DataTablesOutput dp = cmsSecurityIssueDao.dataTableApprove(input, workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }
    /**
     * 新增资产证券化发行
     *
     * @param cmsSecurityIssueVO
     *            资产证券化发行实体
     * @param user
     *            当前用户
     */
    @Transactional
    public void saveSecurityIssue(CmsSecurityIssueVO cmsSecurityIssueVO, ICurrentUser user) {
        CmsSecurityIssue cmsSecurityIssue = BeanUtils.copyNewInstanceProperties(cmsSecurityIssueVO,
                CmsSecurityIssue.class);
        cmsSecurityIssue.setGuaRmk(cmsSecurityIssueVO.getGuaranteeRmk());
        if (!CollectionUtils
                .isEmpty(cmsSecurityIssueDao.getSecurityIssueByCode(cmsSecurityIssueVO.getSecurityIssueCode(), null))) {
            throw new ParameterException(SecurityMessageEnum.CMS_BASEDATA_SECURITY_ISSECODE_CHECK);
        }
        // 担保挂接处理状态
        int guaStatus = 0;
        int messageType = CmsProcessMsgConstant.MSGA_INPUT;
        String msg = String.valueOf(CmsProcessMsgConstant.MSGA_INPUT);
        if (CmsConstant.COMMITAPPROVE == cmsSecurityIssue.getStatus()) {
            messageType = CmsProcessMsgConstant.MSGA_SUBMIT;
            msg = String.valueOf(CmsProcessMsgConstant.MSGA_SUBMIT);
            cmsSecurityIssue.setBizStatus(CmsConstant.SECURITY_BIZ_STATUS_11);
            guaStatus = 1;
        } else {
            cmsSecurityIssue.setBizStatus(CmsConstant.SECURITY_BIZ_STATUS_0);
        }
        cmsSecurityIssue.setCreateBy(user.getId());
        cmsSecurityIssue.setCreateDate(new Date());
        Map<String,Object> map = new HashMap<>();
        map.put("updateDate",new Date());
        Map<String,Object> map1 = new HashMap<>();
        map1.put("securityIssueCode",cmsSecurityIssue.getSecurityIssueCode());
        map1.put("tenantId",user.getTenantId());
        //加锁，保证SecurityIssueCode在当前租户下唯一（SecurityIssueCode+TenantId是唯一索引）
        int update = cmsSecurityIssueDao.updateByParam(map,map1);
        if(update > 0){
            throw new BusinessException(SecurityMessageEnum.CMS_BASEDATA_SECURITY_ISSECODE_CHECK);
        }
        this.saveEntity(cmsSecurityIssue);
        cmsSecurityIssueVO.setId(cmsSecurityIssue.getId());
        String planId = cmsSecurityIssueVO.getPlanId();
        // 担保处理 0暂存，1在审使用，2使用中，3释放,4打回,5发放打回
        SecurityUtils.handleGua(cmsSecurityIssue, null, guaContractOccupServie, guaStatus,
                cmsSecurityIssue.getGuaContract());
        // 判断是否挂接了保证金
        if ((cmsSecurityIssueVO.getIsHookBond() != null)
                && (cmsSecurityIssueVO.getIsHookBond().equals(FcBondConstant.IS_HOOK_Y))) {
            BigDecimal availMoney;
            // 判断占用金额是否大于可用余额
            FcBondDTO fcBondDTO = fcBondHookService.getFcBondMsgById(cmsSecurityIssueVO.getBondId());
            BigDecimal applyMoney = cmsSecurityIssueVO.getThisIssueMoney();
            availMoney = fcBondDTO.getAvailMoney();
            BigDecimal dif = availMoney.subtract(applyMoney);
            if ((dif.compareTo(BigDecimal.ZERO)) < 0) {
                // 占用金额大于可用余额
                throw new ParameterException(FcBondMessageEnum.FCBOND_APPLYMONEY_CHECK);
            }
            // 新增占用信息
            cmsSecurityIssueVO.setCreateBy(user.getLoginName());
            this.addFcBondApply(cmsSecurityIssueVO, cmsSecurityIssue);
        }
        // 保存第三方费率信息
        if (!CollectionUtils.isEmpty(cmsSecurityIssueVO.getCostTypeDtl())) {
            Map<String, Object> param = new HashMap<>(1);
            param.put("id", cmsSecurityIssueVO.getId());
            dfIntermediateCostDao.deleteIntermediateCost(param);
            List<DfIntermediateCost> costList = new ArrayList<>();
            for (DfIntermediateCost cost : cmsSecurityIssueVO.getCostTypeDtl()) {
                if (!"".equals(cost.getCostTypeId().trim())) {
                    cost.setDirectFinanceId(cmsSecurityIssueVO.getId());
                    costList.add(cost);
                }
            }
            dfIntermediateCostDao.batchSave(costList);
        }
        // 挂接抵质押物
        if (cmsSecurityIssueVO.getIsRelatePledge() != null
                && cmsSecurityIssueVO.getIsRelatePledge().equals(CmsConstant.IS_TRUE)) {
            // 设置抵质押物占用数据
            pledgeOccupyService
                    .freezePledgeOccupy(this.setPledgeOccupyInfo(cmsSecurityIssueVO, cmsSecurityIssue.getId()));
        }
        // 启动工作流
        if (CmsConstant.COMMITAPPROVE == cmsSecurityIssue.getStatus()) {
             getStartHandler().handler(CmsWorkFlowConstant.CMS_SECURITY_ISSUE, cmsSecurityIssue.getId(),
                    cmsSecurityIssue, cmsSecurityIssueVO.getCorpId(), user.getId());
        }
        cmsLogService.insertProcessMsg(cmsSecurityIssue.getId(), SecurityConstant.CMS_SECURITY_INFO_CODE, messageType,
                msg, user);
    }
    /**
     * 新增资产证券化发行
     *
     * @param cmsSecurityIssueVO
     *            资产证券化发行实体
     * @param user
     *            当前用户
     */
    @Transactional
    public void modifySecurityIssue(CmsSecurityIssueVO cmsSecurityIssueVO, ICurrentUser user) {
        CmsSecurityIssue cmsSecurityIssue = BeanUtils.copyNewInstanceProperties(cmsSecurityIssueVO,
                CmsSecurityIssue.class);
        cmsSecurityIssue.setGuaRmk(cmsSecurityIssueVO.getGuaranteeRmk());
        Map<String,Object> map = new HashMap<>();
        map.put("updateDate",new Date());
        Map<String,Object> map1 = new HashMap<>();
        map1.put("id",cmsSecurityIssue.getId());
        //加锁
        cmsSecurityIssueDao.updateByParam(map,map1);
        if (!CollectionUtils.isEmpty(cmsSecurityIssueDao
                .getSecurityIssueByCode(cmsSecurityIssueVO.getSecurityIssueCode(), cmsSecurityIssue.getId()))) {
            throw new ParameterException(SecurityMessageEnum.CMS_BASEDATA_SECURITY_ISSECODE_CHECK);
        }
        // 担保挂接处理状态
        int guaStatus = 0;
        int messageType = CmsProcessMsgConstant.MSGA_INPUT;
        String msg = String.valueOf(CmsProcessMsgConstant.MSGA_INPUT);
        if (CmsConstant.COMMITAPPROVE == cmsSecurityIssue.getStatus()) {
            messageType = CmsProcessMsgConstant.MSGA_SUBMIT;
            msg = String.valueOf(CmsProcessMsgConstant.MSGA_SUBMIT);
            cmsSecurityIssue.setBizStatus(CmsConstant.SECURITY_BIZ_STATUS_11);
            guaStatus = 1;
        } else {
            cmsSecurityIssue.setBizStatus(CmsConstant.SECURITY_BIZ_STATUS_0);
        }
        cmsSecurityIssue.setCreateBy(user.getId());
        cmsSecurityIssue.setCreateDate(new Date());
        this.updateEntity(cmsSecurityIssue);
        String planId = cmsSecurityIssueVO.getPlanId();
        if (StringUtils.isNotEmpty(planId)) {
            // CmsBondRepayPlan cmsBondRepayPlan = cmsBondRepayPlanService.get(planId);
            // cmsBondRepayPlan.setIssueId(issueInfo.getId());
            // cmsBondRepayPlanService.save(cmsBondRepayPlan);
        }
        // 担保处理 0暂存，1在审使用，2使用中，3释放,4打回,5发放打回
        SecurityUtils.handleGua(cmsSecurityIssue, null, guaContractOccupServie, guaStatus,
                cmsSecurityIssue.getGuaContract());
        // 判断是否挂接了保证金
        if ((cmsSecurityIssueVO.getIsHookBond() != null)
                && (cmsSecurityIssueVO.getIsHookBond().equals(FcBondConstant.IS_HOOK_Y))) {

            FcBondApplyDTO dto = fcBondHookService.getFcBondApply(cmsSecurityIssueVO.getId(), cmsSecurityIssueVO.getSecurityIssueCode(),
                    cmsSecurityIssueVO.getBondId());
            FcBondApplyDTO fcBondApplyDTOPre = fcBondHookService.getFcBondApply(cmsSecurityIssueVO.getId(), cmsSecurityIssueVO.getSecurityIssueCode(),
                    cmsSecurityIssueVO.getBondIdPre());

            BigDecimal availMoney;
            // 判断占用金额是否大于可用余额
            FcBondDTO fcBondDTO = fcBondHookService.getFcBondMsgById(cmsSecurityIssueVO.getBondId());
            BigDecimal applyMoney = cmsSecurityIssueVO.getThisIssueMoney();
            availMoney = fcBondDTO.getAvailMoney();
            BigDecimal dif = availMoney.subtract(applyMoney);
            if ((dif.compareTo(BigDecimal.ZERO)) < 0) {
                // 占用金额大于可用余额
                throw new ParameterException(FcBondMessageEnum.FCBOND_APPLYMONEY_CHECK);
            }
            // 新增占用信息
            cmsSecurityIssueVO.setCreateBy(user.getLoginName());

            if (dto == null) {// 没有则新增
                if (fcBondApplyDTOPre != null) {// 查询修改之前占用的保证金信息，不为空进行数据修改（修改状态为释放）
                    fcBondApplyDTOPre.setUpdateBy(user.getLoginName());
                    fcBondApplyDTOPre.setUpdateDate(DateUtils.getDate());
                    fcBondHookService.updateFcBondApplyData(fcBondApplyDTOPre);
                }
                this.addFcBondApply(cmsSecurityIssueVO, cmsSecurityIssue);
            } else {
                // 修改保证金占用信息
                this.updateFcBondApply(cmsSecurityIssueVO, dto, user);
            }
        }

        if ((cmsSecurityIssueVO.getIsHookBond() == null) || (cmsSecurityIssueVO.getIsHookBond().equals(FcBondConstant.IS_HOOK_N))) {// 不挂保证金
            FcBondApplyDTO fcBondApplyDTO = fcBondHookService.getFcBondApplyByItemIds(cmsSecurityIssueVO.getId(),
                    cmsSecurityIssueVO.getSecurityIssueCode());
            if (fcBondApplyDTO != null) {// fcBondApplyDTO不为空,修改保证金的状态为已释放
                fcBondApplyDTO.setStatus(FcBondConstant.STATUE_RELEASE);
                fcBondApplyDTO.setUpdateBy(user.getLoginName());
                fcBondApplyDTO.setUpdateDate(DateUtils.getDate());
                fcBondHookService.updateFcBondApply(fcBondApplyDTO);
            }
        }

        // 保存第三方费率信息
        if (!CollectionUtils.isEmpty(cmsSecurityIssueVO.getCostTypeDtl())) {
            Map<String, Object> param = new HashMap<>(1);
            param.put("id", cmsSecurityIssueVO.getId());
            dfIntermediateCostDao.deleteIntermediateCost(param);
            List<DfIntermediateCost> costList = new ArrayList<>();
            for (DfIntermediateCost cost : cmsSecurityIssueVO.getCostTypeDtl()) {
                if (!"".equals(cost.getCostTypeId().trim())) {
                    cost.setDirectFinanceId(cmsSecurityIssueVO.getId());
                    costList.add(cost);
                }
            }
            dfIntermediateCostDao.batchSave(costList);
        }
        if ((cmsSecurityIssueVO.getIsHookBond() == null)
                || (cmsSecurityIssueVO.getIsHookBond().equals(FcBondConstant.IS_HOOK_N))) {// 不挂保证金
            FcBondApplyDTO fcBondApplyDTO = fcBondHookService.getFcBondApplyByItemIds(cmsSecurityIssue.getId(),
                    cmsSecurityIssue.getSecurityIssueCode());
            if (fcBondApplyDTO != null) {// fcBondApplyDTO不为空,修改保证金的状态为已释放
                fcBondApplyDTO.setStatus(FcBondConstant.STATUE_RELEASE);
                fcBondApplyDTO.setUpdateBy(cmsSecurityIssue.getUpdateBy());
                fcBondApplyDTO.setUpdateDate(DateUtils.getDate());
                fcBondHookService.updateFcBondApply(fcBondApplyDTO);
            }
        }
        // 挂接抵质押物
        if (cmsSecurityIssueVO.getIsRelatePledge() != null
                && cmsSecurityIssueVO.getIsRelatePledge().equals(CmsConstant.IS_TRUE)) {
            // 设置抵质押物占用数据
            pledgeOccupyService
                    .freezePledgeOccupy(this.setPledgeOccupyInfo(cmsSecurityIssueVO, cmsSecurityIssue.getId()));
        }
        // 启动工作流
        if (CmsConstant.COMMITAPPROVE == cmsSecurityIssue.getStatus()) {
             getStartHandler().handler(CmsWorkFlowConstant.CMS_SECURITY_ISSUE, cmsSecurityIssue.getId(),
                    cmsSecurityIssue, cmsSecurityIssueVO.getCorpId(), user.getId());
        }
        cmsLogService.insertProcessMsg(cmsSecurityIssue.getId(), SecurityConstant.CMS_SECURITY_INFO_CODE, messageType,
                msg, user);
    }

    /**
     * 修改保证金占用的相关信息
     *
     * @param vo
     */
    public void updateFcBondApply(CmsSecurityIssueVO vo, FcBondApplyDTO dto, ICurrentUser user) {
        dto.setApplyType(FcBondConstant.APPLY_TYPE_BUSINESS);// 占用类型0:业务占用，1手工占用
        dto.setItemType(FcBondConstant.ITEM_TYPE_SECURITY);// 9资产证券化
        dto.setCorpId(vo.getCorpId());// 单位id
        dto.setItemMoney(vo.getThisIssueMoney());// 项目金额
        dto.setBondPercent(vo.getBailmoneyRate());// 保证金比例
        dto.setStartDate(vo.getIssueDate());// 项目开始时间
        dto.setEndDate(vo.getLegalEndDate());// 项目结束时间
        dto.setBondContract(vo.getBailmoneyContract());// 保证金合同
        dto.setBondId(vo.getBondId());// 保证金id
        dto.setApplyMoney(vo.getOriBailmoney());// 保证金金额,即占用金额
        dto.setRemark(vo.getRmk());// 备注
        dto.setStatus(FcBondConstant.STATUE_APPLY);// 状态为占用
        dto.setUpdateBy(user.getLoginName());
        dto.setUpdateDate(DateUtils.getDate());
        fcBondHookService.updateFcBondApply(dto);// 修改保证金占用的相关信息
    }

    /**
     * 根据发行编号查询对应数据
     *
     * @param securityIssueCode
     *            发行编号
     * @param id
     *            id
     * @return 发行数据
     */
    public List<CmsSecurityIssue> getSecurityIssueByCode(String securityIssueCode, String id) {
        return cmsSecurityIssueDao.getSecurityIssueByCode(securityIssueCode, id);
    }
    /**
     * 根据发行id删除对应数据
     *
     * @param id
     *            发行id
     * @param user
     *            当前用户
     */
    public void deleteIssueInfo(String id, ICurrentUser user) {
        CmsSecurityIssue cmsSecurityIssue = this.get(id);
        cmsSecurityIssue.setStatus(CmsConstant.LEASE_DELETED_2);
        cmsSecurityIssue.setSecurityInfoId(null);
        cmsSecurityIssue.setUpdateDate(new Date());
        cmsSecurityIssue.setUpdateBy(user.getId());
        // 如果挂接了保证金,则修改保证金占用信息的状态为"释放"
        FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(cmsSecurityIssue.getId(),
                cmsSecurityIssue.getSecurityIssueCode());
        if (dto != null) {
            // 不为空说明挂接了保证金
            dto.setStatus(FcBondConstant.STATUE_RELEASE);
            dto.setUpdateBy(user.getLoginName());
            dto.setUpdateDate(DateUtils.getDate());
            fcBondHookService.updateFcBondApply(dto);
        }
        // 抵质押物处理,释放冻结额度
        if (EmptyUtils.isNotEmpty(cmsSecurityIssue.getIsRelatePledge())
                && CmsConstant.IS_TRUE.equals(cmsSecurityIssue.getIsRelatePledge())) {
            pledgeOccupyService.releasePledgeOccupy(cmsSecurityIssue.getId(), cmsSecurityIssue.getSecurityIssueCode());
        }
        this.updateEntity(cmsSecurityIssue);
    }
    /**
     * 新增保证金占用信息
     *
     * @param vo
     * @param cmsSecurityIssue
     */
    public void addFcBondApply(CmsSecurityIssueVO vo, CmsSecurityIssue cmsSecurityIssue) {
        FcBondApplyDTO dto = this.setFcBondApplyDTO(vo, cmsSecurityIssue, new FcBondApplyDTO());
        dto.setCreateBy(cmsSecurityIssue.getCreateBy());// 创建人登录名称
        dto.setCreateDate(DateUtils.getDate());// 创建日期
        fcBondHookService.addFcBondApply(dto);
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
    /**
     * 设置保证金占用信息的值
     *
     * @param vo
     * @param cmsSecurityIssue
     * @return
     */
    public FcBondApplyDTO setFcBondApplyDTO(CmsSecurityIssueVO vo, CmsSecurityIssue cmsSecurityIssue,
                                            FcBondApplyDTO dto) {
        dto.setApplyType(FcBondConstant.APPLY_TYPE_BUSINESS);// 占用类型0:业务占用，1手工占用
        dto.setItemType(FcBondConstant.ITEM_TYPE_SECURITY);// 9资产证券化
        dto.setItemNo(cmsSecurityIssue.getSecurityIssueCode());// 项目单号
        dto.setItemId(cmsSecurityIssue.getId());// 占用保证金的项目id
        dto.setCorpId(vo.getCorpId());// 单位id
        // dto.setBankId(vo.getBankId());//联行号
        dto.setItemMoney(vo.getThisIssueMoney());// 项目金额
        dto.setBondPercent(vo.getBailmoneyRate());// 保证金比例
        dto.setStartDate(vo.getIssueDate());// 项目开始时间
        dto.setEndDate(vo.getLegalEndDate());// 项目结束时间
        // dto.setItemUnit(bkrationDTO.getItemUnit());//项目期限
        dto.setBondContract(vo.getBailmoneyContract());// 保证金合同
        dto.setBondId(vo.getBondId());// 保证金id
        dto.setApplyMoney(vo.getOriBailmoney());// 保证金金额,即占用金额
        dto.setRemark(vo.getRmk());// 备注
        dto.setStatus(FcBondConstant.STATUE_APPLY);// 状态为占用
        // if(saveType.equals("1")){
        // dto.setStatus(FcBondConstant.STATUE_WAIT_CHECK);//0暂存,11待审批,-1打回,95占用,-2删除,2已释放
        // }else{
        // dto.setStatus(FcBondConstant.STATUE_TEMP);//0暂存,11待审批,-1打回,95占用,-2删除,2已释放
        // }
        return dto;
    }
    /**
     * 设置占用抵质押物数据
     *
     * @param cmsSecurityIssueVO
     * @param businessId
     * @return
     */
    public List<GuaPledgeOccupyDTO> setPledgeOccupyInfo(CmsSecurityIssueVO cmsSecurityIssueVO, String businessId) {
        List<GuaPledgeOccupyDTO> pledgeOccupyDTOS = new ArrayList<>();
        if (!EmptyUtils.isEmptyArray(cmsSecurityIssueVO.getPledgeId()) && cmsSecurityIssueVO.getPledgeId().length > 0) {
            for (int i = 0; i < cmsSecurityIssueVO.getPledgeId().length; i++) {
                if (EmptyUtils.isEmpty(cmsSecurityIssueVO.getPledgeId()[i])
                        || EmptyUtils.isEmpty(cmsSecurityIssueVO.getPledgeMoney()[i])) {
                    continue;
                }
                GuaPledgeOccupyDTO pledgeOccupyDTO = new GuaPledgeOccupyDTO();
                pledgeOccupyDTO.setSysCode(ConfigConstant.SECURITY);// 融资租赁
                pledgeOccupyDTO.setBusinessId(businessId);
                pledgeOccupyDTO.setItemNo(cmsSecurityIssueVO.getSecurityIssueCode());
                pledgeOccupyDTO.setStartDate(cmsSecurityIssueVO.getIssueDate());
                pledgeOccupyDTO.setEndDate(cmsSecurityIssueVO.getLegalEndDate());
                pledgeOccupyDTO.setPledgeId(cmsSecurityIssueVO.getPledgeId()[i]);
                pledgeOccupyDTO.setOccupyMoney(cmsSecurityIssueVO.getPledgeMoney()[i]);
                pledgeOccupyDTO.setCurId(cmsSecurityIssueVO.getCurId());
                pledgeOccupyDTOS.add(pledgeOccupyDTO);
            }
        }
        return pledgeOccupyDTOS;
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();
        boolean result = workFlowResult.isPass();
        ICurrentUser user = workFlowResult.getUser();
        if (StringUtils.isEmpty(businessKey)) {
            throw new BusinessException(BaseDataMessageEnum.MESSAGE_ERROR_VALISNOTNULL);
        }
        CmsSecurityIssue securityIssue = this.get(businessKey,true);
        int messageType = result ? CmsProcessMsgConstant.MSGA_BOND_ISSUE_CHECKOK
                : CmsProcessMsgConstant.MSGA_BOND_ISSUE_CHECKBACK;
        String msg = String.valueOf(result ? CmsProcessMsgConstant.MSGA_BOND_ISSUE_CHECKOK
                : CmsProcessMsgConstant.MSGA_BOND_ISSUE_CHECKBACK);
        int guaStatus;
        if (result) {
            // 审批通过
            securityIssue.setStatus(CmsConstant.APPROVED);
            securityIssue.setBizStatus(CmsConstant.SECURITY_BIZ_STATUS_95);
            securityIssue.setReToAccountMoney(securityIssue.getThisIssueMoney());
            // 担保处理 0暂存，1在审使用，2使用中，3释放,4打回,5发放打回
            guaStatus = 2;
            /// 抵质押物处理开始(补录审批通过后正式占用额度)
            if (securityIssue.getIsRelatePledge() != null
                    && securityIssue.getIsRelatePledge().equals(CmsConstant.IS_TRUE)) {
                List<GuaPledgeOccupyDTO> occupyList = pledgeOccupyService.getPledgeOccupy(securityIssue.getId(),
                        securityIssue.getSecurityIssueCode());
                pledgeOccupyService.addPledgeOccupy(occupyList);
            }
        } else {
            // 打回
            securityIssue.setStatus(CmsConstant.BACK);
            securityIssue.setBizStatus(CmsConstant.SECURITY_BIZ_STATUS__1);
            // 担保处理 0暂存，1在审使用，2使用中，3释放,4打回,5发放打回
            guaStatus = 4;

            //如果挂接了保证金,打回后释放
            FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(securityIssue.getId(), securityIssue.getSecurityIssueCode());
            if(dto!=null){
                dto.setUpdateBy(user.getLoginName());
                dto.setUpdateDate(DateUtils.getDate());
                fcBondHookService.updateFcBondApply(dto);
            }
        }
        SecurityUtils.handleGua(securityIssue, null, guaContractOccupServie, guaStatus, null);
        cmsLogService.insertProcessMsg(securityIssue.getId(), CmsConstant.BOND_ISSUE_BILL_CODE_PREFIX, messageType, msg,
                user);
        this.updateEntity(securityIssue);
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    /**
     * 获取所有挂接担保的证券数据
     * @return
     */
    public List<GuaContractCmsSecurityDTO> getGuaForSecurity() {
        List<GuaContractCmsSecurityDTO> guaForSecurity = cmsSecurityIssueDao.getGuaForSecurity();
        return guaForSecurity;
    }
}
