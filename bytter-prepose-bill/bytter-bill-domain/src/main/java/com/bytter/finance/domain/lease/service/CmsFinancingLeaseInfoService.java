package com.bytter.finance.domain.lease.service;

import cn.hutool.core.util.StrUtil;
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
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.*;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.directFinance.dao.DfIntermediateCostDao;
import com.bytter.finance.domain.directFinance.entity.DfIntermediateCost;
import com.bytter.finance.domain.directFinance.vo.DfIntermediateCostVO;
import com.bytter.finance.domain.lease.dao.CmsFinancingLeaseInfoDao;
import com.bytter.finance.domain.lease.entity.CmsFinancingLeaseInfo;
import com.bytter.finance.domain.lease.leaseUtils.LeaseUtils;
import com.bytter.finance.domain.lease.vo.CmsFinancingLeaseInfoVO;
import com.bytter.finance.domain.lease.vo.GuaContractCmsLeaseVO;
import com.bytter.finance.dto.GuaContractCmsLeaseDTO;
import com.bytter.finance.enums.CmsLeaseEnum;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaPledgeOccupyService;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.guarantee.dto.FcBondDTO;
import com.bytter.guarantee.constant.GuaranteeConstant;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import com.bytter.guarantee.dto.GuaPledgeOccupyDTO;
import com.bytter.guarantee.enums.FcBondMessageEnum;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IFcExcrateAdjustService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
/**
 * 融资租赁申请
 *
 * @author yangl
 */
@Service("cmsFinancingLeaseInfoService")
@WorkFlow({CmsWorkFlowConstant.CMS_FINANCING_LEASE_INFO, CmsWorkFlowConstant.CMS_FINANCING_LEASE_REPAIR})
@Transactional
public class CmsFinancingLeaseInfoService extends BaseService<CmsFinancingLeaseInfo, String>
        implements IWorkFlowListener {
    @Autowired
    private CmsFinancingLeaseInfoDao cmsFinancingLeaseInfoDao;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie guaContractOccupServie;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IFcExcrateAdjustService rateAdjustService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @Resource(name = "fcBondHookServiceImp")
    private IFcBondHookService fcBondHookService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Autowired
    private DfIntermediateCostDao dfIntermediateCostDao;
    @Resource(name = "guaPledgeOccupyServiceImpl")
    private IGuaPledgeOccupyService pledgeOccupyService;
    @Autowired
    private CmsProcessMsgService msService;


    /**
     * <b>功能介绍：</b>生成租赁编号<br>
     * <b>日期：</b>2022年4月8日<br>
     * <b>作者：</b>tongqr<br>
     *
     * @version 1.0
     */
    public String getFinLeaseNo() {
        return SequenceSingleton.getSequence(SequenceEnum.CMS_FINANCING_LEASE_LEASE_NO,
                LeaseConstant.FINANCING_LEASE_LEASE_NO);
    }
    /**
     * <b>功能介绍：</b>根据租赁合同号获取租赁信息<br>
     * <b>日期：</b>2022年4月8日<br>
     * <b>作者：</b>tongqr<br>
     *
     * @version 1.0
     */
    public List<CmsFinancingLeaseInfoVO> getLeaseByLeaseCode(String leaseCode, String id) {
        return cmsFinancingLeaseInfoDao.getLeaseByLeaseCode(leaseCode, id);
    }
    /**
     * <b>功能介绍：</b>根据租赁合同号获取租赁信息<br>
     * <b>日期：</b>2022年4月8日<br>
     * <b>作者：</b>tongqr<br>
     *
     * @version 1.0
     */
    public CmsFinancingLeaseInfoVO getLeaseById(String leaseNo) {
        return cmsFinancingLeaseInfoDao.getLeaseById(leaseNo);
    }
    /**
     * 功能介绍 : 申请审批列表<br>
     *
     * @param dataTablesInput <b>日期：</b>2022-04-09<br>
     *                        <br>
     * @author tongqr
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataInfoApproveTables(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService
                .getWorkFlowBusiness(CmsWorkFlowConstant.CMS_FINANCING_LEASE_INFO, dataTablesInput);
        DataTablesOutput dp = cmsFinancingLeaseInfoDao.getDataInfoApproveTables(dataTablesInput,
                workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }
    /**
     * 功能介绍 : 补录审批列表<br>
     *
     * @param dataTablesInput <b>日期：</b>2022-04-09<br>
     *                        <br>
     * @author tongqr
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataRepairApproveTables(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService
                .getWorkFlowBusiness(CmsWorkFlowConstant.CMS_FINANCING_LEASE_REPAIR, dataTablesInput);
        DataTablesOutput dp = cmsFinancingLeaseInfoDao.getDataInfoApproveTables(dataTablesInput,
                workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }
    /**
     * 租赁申请列表数据查询
     *
     * @param dataTablesInput
     * @param user
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataLeaseTables(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_LEASE_FINANCING_QUERY, user.getId());
        return cmsFinancingLeaseInfoDao.getDataLeaseTable(dataTablesInput, corpAuth);
    }
    @Transactional(readOnly = true)
    public DataTablesOutput getDataleaseDatatables(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_LEASE_FINANCING_QUERY, user.getId());
        return cmsFinancingLeaseInfoDao.getDataleaseDatatables(dataTablesInput, corpAuth);
    }
    /**
     * 融资申请查询列表 - 数据
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getQueryDatatable(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_PROVIDE_INFO_QUERY, user.getId());
        return cmsFinancingLeaseInfoDao.getQueryDatatable(dataTablesInput, corpAuth);
    }
    /**
     * 租赁补录列表数据查询
     *
     * @param dataTablesInput
     * @param user
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getRepairDataLeaseTables(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_LEASE_FINANCING_REPAIR_QUERY,
                user.getId());
        return cmsFinancingLeaseInfoDao.getRepairDataLeaseTable(dataTablesInput, corpAuth);
    }
    /*
     * 判断合同编号唯一性
     */
    public void temOrSub(CmsFinancingLeaseInfoVO vo, String saveType, String myflag, ICurrentUser user) {
        boolean flag = false;
        if (StringUtils.isEmpty(vo.getLeaseCode())) {
            flag = true;
        } else {
            List<CmsFinancingLeaseInfoVO> list = this.getLeaseByLeaseCode(vo.getLeaseCode(), vo.getId());
            if (list.size() < 1) {
                flag = true;
            }
        }
        if (flag) {
            if (StringUtils.isEmpty(vo.getId())) {
                vo.setLeaseNo(this.getFinLeaseNo());
                vo.setControlStatus("N");
            }
            if (myflag.equals("apply")) {
                this.tempOrSubmit(vo, saveType, user);
            } else if (myflag.equals("repair")) {
                this.repairTempOrSubmit(vo, saveType, user);
            }
        } else {
            throw new ParameterException(CmsLeaseEnum.LEASE_INFO_NO_CHECK);// 合同号已存在
        }
    }
    /**
     * 功能介绍 : 提交或暂存<br>
     *
     * @param vo
     * @param saveType 1提交，0暂存 <b>日期：</b>2000-04-07<br>
     *                 <br>
     * @author tongqr
     */
    public void tempOrSubmit(CmsFinancingLeaseInfoVO vo, String saveType, ICurrentUser user) {
        CmsFinancingLeaseInfo cmsFinancingLeaseInfo = new CmsFinancingLeaseInfo();
        BeanUtils.copyNotNullProperties(vo, cmsFinancingLeaseInfo);
        // 担保挂接处理状态
        int guaStatus = 0;
        if ("1".equals(saveType)) {// 提交
            cmsFinancingLeaseInfo.setStatus(CmsConstant.LEASE_COMMITAPPROVE_11);
            cmsFinancingLeaseInfo.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_11);
            guaStatus = 1;
        } else {// 暂存
            cmsFinancingLeaseInfo.setStatus(CmsConstant.LEASE_TEMP_0);
            cmsFinancingLeaseInfo.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_0);
        }
        CmsFinancingLeaseInfo lease = null;
        if (StringUtils.isEmpty(cmsFinancingLeaseInfo.getId())) {
            lease = cmsFinancingLeaseInfo;
            this.save(lease, user);
            // 担保处理 0暂存，1在审使用，2使用中，3释放,4打回,5发放打回
            LeaseUtils.handleGua(lease, null, guaContractOccupServie, guaStatus, vo.getGuaContract());
        } else {
            lease = this.get(cmsFinancingLeaseInfo.getId());
            BeanUtils.copyNotNullProperties(cmsFinancingLeaseInfo, lease);
            this.update(lease, user);// 使用saveOrUpdate无法新增数据
            // 担保处理 0暂存，1在审使用，2使用中，3释放,4打回,5发放打回
            LeaseUtils.handleGua(cmsFinancingLeaseInfo, lease, guaContractOccupServie, guaStatus, vo.getGuaContract());
        }
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
                this.updateFcBondApply(vo, cmsFinancingLeaseInfo, saveType, fcBondApplyDTO);
            } else {// 否则即为新增
                if (fcBondApplyDTOPre != null) {// 查询修改之前占用的保证金信息，不为空进行数据修改（修改状态为释放）
                    fcBondApplyDTOPre.setUpdateBy(user.getLoginName());
                    fcBondApplyDTOPre.setUpdateDate(DateUtils.getDate());
                    fcBondHookService.updateFcBondApplyData(fcBondApplyDTOPre);
                }
                this.addFcBondApply(vo, cmsFinancingLeaseInfo, saveType);// 新增占用信息
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
            List<GuaPledgeOccupyDTO> newOccupyDTOS = this.setPledgeOccupyInfo(vo, lease.getId());
            pledgeOccupyService.freezePledgeOccupy(newOccupyDTOS);// 设置抵质押物占用数据
        } else {// 将原占用信息释放
            pledgeOccupyService.releasePledgeOccupy(cmsFinancingLeaseInfo.getId(), vo.getLeaseNo());
        }
        notificationService.complete(NotificationConstant.CMS.LEASE_ADD, lease.getId(), user.getId());
        if ("1".equals(saveType)) {// 提交 启动审批流程
            msService.insertProcessMsg(lease.getLeaseNo(), LeaseConstant.FINANCING_LEASE_LEASE_NO,
                    CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_SUBMIT + "", user);
             getStartHandler().handler(CmsWorkFlowConstant.CMS_FINANCING_LEASE_INFO, lease.getId(), lease,
                    lease.getCorpId(), user.getId());
        } else {
            msService.insertProcessMsg(lease.getLeaseNo(), LeaseConstant.FINANCING_LEASE_LEASE_NO,
                    CmsProcessMsgConstant.MSGA_INPUT, CmsProcessMsgConstant.MSGA_INPUT + "", user);
        }
    }
    /**
     * 功能介绍 : 补录提交或暂存<br>
     *
     * @param vo
     * @param saveType 1提交，0暂存 <b>日期：</b>2000-04-07<br>
     *                 <br>
     * @author tongqr
     */
    public void repairTempOrSubmit(CmsFinancingLeaseInfoVO vo, String saveType, ICurrentUser user) {
        CmsFinancingLeaseInfo cmsFinancingLeaseInfo = new CmsFinancingLeaseInfo();
        BeanUtils.copyNotNullProperties(vo, cmsFinancingLeaseInfo);
        // 担保挂接处理状态
        int guaStatus = 0;
        if ("1".equals(saveType)) {// 提交
            cmsFinancingLeaseInfo.setStatus(CmsConstant.LEASE_REPAIR_COMMITAPPROVE);
            cmsFinancingLeaseInfo.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_21);
            guaStatus = 1;
        } else {// 暂存
            cmsFinancingLeaseInfo.setStatus(CmsConstant.LEASE_REPAIR_TEMP);
            cmsFinancingLeaseInfo.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_20);
        }
        CmsFinancingLeaseInfo lease = null;
        lease = this.get(cmsFinancingLeaseInfo.getId());
        BeanUtils.copyNotNullProperties(cmsFinancingLeaseInfo, lease);
        this.update(lease, user);// 使用saveOrUpdate无法新增数据
        // 担保处理 0暂存，1在审使用，2使用中，3释放,4打回,5发放打回
        guaContractOccupServie.deleteGuaContractOccup(lease.getId(), LeaseConstant.LEASE_GUA_SYSCODE);// 释放原本的
        LeaseUtils.handleGua(lease, null, guaContractOccupServie, guaStatus, lease.getGuaContract());
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
                this.updateFcBondApply(vo, cmsFinancingLeaseInfo, saveType, fcBondApplyDTO);
            } else {// 否则即为新增
                if (fcBondApplyDTOPre != null) {// 查询修改之前占用的保证金信息，不为空进行数据修改（修改状态为释放）
                    fcBondApplyDTOPre.setUpdateBy(user.getLoginName());
                    fcBondApplyDTOPre.setUpdateDate(DateUtils.getDate());
                    fcBondHookService.updateFcBondApplyData(fcBondApplyDTOPre);
                }
                this.addFcBondApply(vo, cmsFinancingLeaseInfo, saveType);// 新增占用信息
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
            List<GuaPledgeOccupyDTO> newOccupyDTOS = this.setPledgeOccupyInfo(vo, lease.getId());
            // pledgeOccupyService.savePledgeOccupy(newOccupyDTOS,GuaranteeConstant.GUA_PLEDGE_FROZEN,ConfigConstant.LEASE);
            pledgeOccupyService.freezePledgeOccupy(newOccupyDTOS);// 设置抵质押物占用数据
        } else {// 将原占用信息释放
            pledgeOccupyService.releasePledgeOccupy(cmsFinancingLeaseInfo.getId(), vo.getLeaseNo());
        }
        notificationService.complete(NotificationConstant.CMS.LEASE_REPAIR, lease.getId(), user.getId());
        if ("1".equals(saveType)) {// 提交 启动审批流程
            msService.insertProcessMsg(lease.getLeaseNo(), LeaseConstant.FINANCING_LEASE_LEASE_NO,
                    CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_REPAIR_SUBMIT + "", user);
             getStartHandler().handler(CmsWorkFlowConstant.CMS_FINANCING_LEASE_REPAIR, lease.getId(), lease,
                    lease.getCorpId(), user.getId());
        } else {
            msService.insertProcessMsg(lease.getLeaseNo(), LeaseConstant.FINANCING_LEASE_LEASE_NO,
                    CmsProcessMsgConstant.MSGA_MODIFY, CmsProcessMsgConstant.MSGA_REPAIR_TEMP + "", user);
        }
    }
    /**
     * 功能介绍 : 删除融资租赁申请<br>
     */
    public void deleteLease(String id, ICurrentUser user) {
        CmsFinancingLeaseInfo cmsFinancingLeaseInfo = this.get(id);
        cmsFinancingLeaseInfo.setStatus(CmsConstant.LEASE_DELETED_2);
        cmsFinancingLeaseInfo.setBizStatus(CmsConstant.LEASE_BIZ_STATUS__2);
        ///**/如果挂接了保证金,则修改保证金占用信息的状态为"释放"
        FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(cmsFinancingLeaseInfo.getId(),
                cmsFinancingLeaseInfo.getLeaseNo());
        if (dto != null) {// 不为空说明挂接了保证金
            dto.setStatus(FcBondConstant.STATUE_RELEASE);
            dto.setUpdateBy(user.getLoginName());
            dto.setUpdateDate(DateUtils.getDate());
            fcBondHookService.updateFcBondApply(dto);
        }
        // notificationService.complete(NotificationConstant.CMS.PROVIDE_ADD, id, user.getId());
        // 抵质押物处理,释放冻结额度
        if (EmptyUtils.isNotEmpty(cmsFinancingLeaseInfo.getIsRelatePledge())
                && CmsConstant.IS_TRUE.equals(cmsFinancingLeaseInfo.getIsRelatePledge())) {
            pledgeOccupyService.releasePledgeOccupy(cmsFinancingLeaseInfo.getId(), cmsFinancingLeaseInfo.getLeaseNo());
        }
        this.update(cmsFinancingLeaseInfo, user);
    }
    /**
     * 取费率信息
     */
    public List<DfIntermediateCostVO> listIntermediateCostByDfId(String id) {
        Map<String, Object> param = new HashMap<>();
        param.put("directFinanceId", id);
        return dfIntermediateCostDao.listIntermediateCostByDfId(param);
    }
    /**
     * 新增保证金占用信息
     *
     * @param vo
     * @param cmsFinancingLeaseInfo
     */
    public void addFcBondApply(CmsFinancingLeaseInfoVO vo, CmsFinancingLeaseInfo cmsFinancingLeaseInfo,
                               String saveType) {
        FcBondApplyDTO dto = this.setFcBondApplyDTO(vo, cmsFinancingLeaseInfo, saveType, new FcBondApplyDTO());
        dto.setCreateBy(cmsFinancingLeaseInfo.getCreateBy());// 创建人登录名称
        dto.setCreateDate(DateUtils.getDate());// 创建日期
        fcBondHookService.addFcBondApply(dto);
    }
    /**
     * 更新保证金占用信息
     *
     * @param vo
     * @param cmsFinancingLeaseInfo
     * @param saveType
     */
    public void updateFcBondApply(CmsFinancingLeaseInfoVO vo, CmsFinancingLeaseInfo cmsFinancingLeaseInfo,
                                  String saveType, FcBondApplyDTO dto) {
        FcBondApplyDTO fcBondApplyDTO = this.setFcBondApplyDTO(vo, cmsFinancingLeaseInfo, saveType, dto);
        fcBondApplyDTO.setUpdateBy(cmsFinancingLeaseInfo.getUpdateBy());// 创建人登录名称
        fcBondApplyDTO.setUpdateDate(DateUtils.getDate());// 创建日期
        fcBondHookService.updateFcBondApply(fcBondApplyDTO);
    }
    /**
     * 设置保证金占用信息的值
     *
     * @param vo
     * @param cmsFinancingLeaseInfo
     * @return
     */
    public FcBondApplyDTO setFcBondApplyDTO(CmsFinancingLeaseInfoVO vo, CmsFinancingLeaseInfo cmsFinancingLeaseInfo,
                                            String saveType, FcBondApplyDTO dto) {
        dto.setApplyType(FcBondConstant.APPLY_TYPE_BUSINESS);// 占用类型0:业务占用，1手工占用
        dto.setItemType(FcBondConstant.ITEM_TYPE_LEASE);// 0银行授信1银行借款2承兑汇票7融资租赁
        dto.setItemNo(cmsFinancingLeaseInfo.getLeaseNo());// 项目单号
        dto.setItemId(cmsFinancingLeaseInfo.getId());// 占用保证金的项目id
        dto.setCorpId(vo.getCorpId());// 单位id
        // dto.setBankId(vo.getBankId());//联行号
        dto.setItemMoney(vo.getRmbMoney());// 项目金额
        dto.setBondPercent(vo.getBailmoneyRate());// 保证金比例
        dto.setStartDate(vo.getBeginDate());// 项目开始时间
        dto.setEndDate(vo.getEndDate());// 项目结束时间
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
     * @param cmsFinancingLeaseInfoVO
     * @param businessId
     * @return
     */
    public List<GuaPledgeOccupyDTO> setPledgeOccupyInfo(CmsFinancingLeaseInfoVO cmsFinancingLeaseInfoVO,
                                                        String businessId) {
        List<GuaPledgeOccupyDTO> pledgeOccupyDTOS = new ArrayList<>();
        if (!EmptyUtils.isEmptyArray(cmsFinancingLeaseInfoVO.getPledgeId())
                && cmsFinancingLeaseInfoVO.getPledgeId().length > 0) {
            for (int i = 0; i < cmsFinancingLeaseInfoVO.getPledgeId().length; i++) {
                if (EmptyUtils.isEmpty(cmsFinancingLeaseInfoVO.getPledgeId()[i])
                        || EmptyUtils.isEmpty(cmsFinancingLeaseInfoVO.getPledgeMoney()[i])) {
                    continue;
                }
                GuaPledgeOccupyDTO pledgeOccupyDTO = new GuaPledgeOccupyDTO();
                pledgeOccupyDTO.setSysCode(ConfigConstant.LEASE);// 融资租赁
                pledgeOccupyDTO.setBusinessId(businessId);
                pledgeOccupyDTO.setItemNo(cmsFinancingLeaseInfoVO.getLeaseNo());
                pledgeOccupyDTO.setStartDate(cmsFinancingLeaseInfoVO.getBeginDate());
                pledgeOccupyDTO.setEndDate(cmsFinancingLeaseInfoVO.getEndDate());
                pledgeOccupyDTO.setPledgeId(cmsFinancingLeaseInfoVO.getPledgeId()[i]);
                pledgeOccupyDTO.setOccupyMoney(cmsFinancingLeaseInfoVO.getPledgeMoney()[i]);
                pledgeOccupyDTO.setCurId(cmsFinancingLeaseInfoVO.getOriCurid());
                pledgeOccupyDTOS.add(pledgeOccupyDTO);
            }
        }
        return pledgeOccupyDTOS;
    }
    @Override
    public BaseDao<CmsFinancingLeaseInfo, String> getEntityDao() {
        return cmsFinancingLeaseInfoDao;
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        String businessKey = workFlowResult.getBusinessKey();
        String processDefinitionKey = workFlowResult.getProcessDefinitionKey();
        boolean result = workFlowResult.isPass();
        ICurrentUser user = workFlowResult.getUser();
        CmsFinancingLeaseInfo lease = this.get(businessKey,true);
        if (CmsWorkFlowConstant.CMS_FINANCING_LEASE_INFO.equals(processDefinitionKey)) {// 申请审批
            if (result) {// 审批通过
                lease.setStatus(CmsConstant.APPROVED);
                lease.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_95);
                // 担保处理 0暂存，1在审使用，2使用中，3释放,4打回,5发放打回
                LeaseUtils.handleGua(lease, null, guaContractOccupServie, 2, null);
                /*** 抵质押物处理开始(补录审批通过后正式占用额度) **/
                if (lease.getIsRelatePledge() != null && lease.getIsRelatePledge().equals(CmsConstant.IS_TRUE)) {// 挂接抵质押物
                    List<GuaPledgeOccupyDTO> occupyDTOS = pledgeOccupyService.getPledgeOccupy(lease.getId(),
                            lease.getLeaseNo());
                    pledgeOccupyService.addPledgeOccupy(occupyDTOS);
                }
                /*** 抵质押物处理结束 **/
                msService.insertProcessMsg(lease.getLeaseNo(), LeaseConstant.FINANCING_LEASE_LEASE_NO,
                        CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_CHECKOK + "", user);
            } else {// 打回
                lease.setStatus(CmsConstant.BACK);
                lease.setBizStatus(CmsConstant.LEASE_BIZ_STATUS__1);
                // 担保处理 0暂存，1在审使用，2使用中，3释放,4打回,5发放打回
                LeaseUtils.handleGua(lease, null, guaContractOccupServie, 4, null);
                msService.insertProcessMsg(lease.getLeaseNo(), LeaseConstant.FINANCING_LEASE_LEASE_NO,
                        CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_ROLLBACK + "", user);
                String submitUser = StringUtils.isEmpty(lease.getUpdateBy()) ? lease.getCreateBy()
                        : lease.getUpdateBy();// 提交人
                notificationService.sendToUser(lease.getId(), submitUser, NotificationConstant.CMS.LEASE_ADD, lease);// 添加租赁申请的消息提示
            }
        }
        if (CmsWorkFlowConstant.CMS_FINANCING_LEASE_REPAIR.equals(processDefinitionKey)) {// 补录审批
            if (result) {// 审批通过
                lease.setStatus(CmsConstant.LEASE_REPAIR_APPROVED);
                lease.setBizStatus(CmsConstant.LEASE_BIZ_STATUS_96);
                LeaseUtils.handleGua(lease, null, guaContractOccupServie, 2, null);
                /*** 抵质押物处理开始(补录审批通过后正式占用额度) **/
                if (lease.getIsRelatePledge() != null && lease.getIsRelatePledge().equals(CmsConstant.IS_TRUE)) {// 挂接抵质押物
                    List<GuaPledgeOccupyDTO> occupyDTOS = pledgeOccupyService.getPledgeOccupy(lease.getId(),
                            lease.getLeaseNo());
                    pledgeOccupyService.addPledgeOccupy(occupyDTOS);
                }
                /*** 抵质押物处理结束 **/
                msService.insertProcessMsg(lease.getLeaseNo(), LeaseConstant.FINANCING_LEASE_LEASE_NO,
                        CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_REPAIR_CHECKOK + "", user);
            } else {// 打回
                lease.setStatus(CmsConstant.LEASE_REPAIR_BACK);
                lease.setBizStatus(CmsConstant.LEASE_BIZ_STATUS__3);
                String submitUser = StringUtils.isEmpty(lease.getUpdateBy()) ? lease.getCreateBy()
                        : lease.getUpdateBy();// 提交人
                notificationService.sendToUser(lease.getId(), submitUser, NotificationConstant.CMS.LEASE_REPAIR, lease);// 添加租赁补录的消息提示
                msService.insertProcessMsg(lease.getLeaseNo(), LeaseConstant.FINANCING_LEASE_LEASE_NO,
                        CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_REPAIR_CHECKBACK + "", user);
            }
        }
        this.saveOrUpdate(lease);
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
    public void save(CmsFinancingLeaseInfo entity, ICurrentUser user) {
        entity.setCreateDate(new Date());
        entity.setCreateBy(user.getId());
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(CmsFinancingLeaseInfo entity, ICurrentUser user) {
        entity.setUpdateDate(new Date());
        entity.setUpdateBy(user.getId());
        super.updateEntity(entity);
    }
    /**
     * @param curId
     * @param curPreTime
     * @return java.math.BigDecimal
     * @description 获取汇率
     **/
    public BigDecimal getConverRmbRate(String curId, Date curPreTime) {
        BtCurrencyDTO natualCurrency = btCurrencyService.getNatualCurrency();
        return rateAdjustService.getExchangeRateByNoCheck(curId, natualCurrency.getId(), curPreTime,
                FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
    }
    /**
     * 获取所有挂接担保的融资租赁数据
     */
    public List<GuaContractCmsLeaseDTO> getGuaForLeaseInfo() {
        return cmsFinancingLeaseInfoDao.getGuaForLeaseInfo();
    }

    public DataTablesOutput<GuaContractCmsLeaseVO> getGuaContract(DataTablesInput input, ICurrentUser user) {
        String sysCode =  StrUtil.toStringOrNull(input.getFormdata().get("eqs_sysCode"));
        String startDate =  StrUtil.toStringOrNull(input.getFormdata().get("ged_useDate"));
        String endDate = StrUtil.toStringOrNull(input.getFormdata().get("led_useDate"));
        String status = StrUtil.toStringOrNull(input.getFormdata().get("eqg_status"));
        String contractId =  StrUtil.toStringOrNull(input.getFormdata().get("eqs_contractId"));
        // 当contractId不为空时表示被担保合同使用情况
        //获取挂接授信的业务id集合和封装参数
        List<GuaContractOccupDTO> guaList;
        if (StrUtil.isBlank(contractId)) {
            guaList = guaContractOccupServie.listUseGua(sysCode, startDate, endDate, status);
        }else {
            guaList = guaContractOccupServie.getGua(sysCode, startDate, endDate, status,contractId);
        }
        List<String> corpIds = corpService.getUserCorpIds(GuaranteeConstant.CMS_GUARANTEE_CHANGE_QUERY, user.getId());
        return buildGuaContractVO(input, guaList, corpIds);
    }

    private DataTablesOutput<GuaContractCmsLeaseVO> buildGuaContractVO(DataTablesInput input, List<GuaContractOccupDTO> guaList, List<String> corpIds) {
        if (EmptyUtils.isEmpty(guaList)) {
            return new DataTablesOutput<>();
        }
        Map<String, GuaContractOccupDTO> guaMap = new HashMap<>(guaList.size());
        List<String> businessIdList = new ArrayList<>();
        guaList.forEach(item -> {
            guaMap.put(item.getBusinessId(), item);
            businessIdList.add(item.getBusinessId());
        });
        //组装参数
        DataTablesOutput<GuaContractCmsLeaseVO> dataTablesOutput = cmsFinancingLeaseInfoDao.getGuaForLease(input, corpIds, businessIdList);
        if (EmptyUtils.isNotEmpty(dataTablesOutput.getData())) {
            for (GuaContractCmsLeaseVO item : dataTablesOutput.getData()) {
                if (guaMap.containsKey(item.getId())) {
                    item.setStatus(guaMap.get(item.getId()).getStatus());
                    item.setUseDate(guaMap.get(item.getId()).getUseDate());
                }
            }
        }
        return dataTablesOutput;
    }

}
