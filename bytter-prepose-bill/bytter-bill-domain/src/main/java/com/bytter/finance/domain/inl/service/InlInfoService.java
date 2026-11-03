package com.bytter.finance.domain.inl.service;

import cn.hutool.core.util.StrUtil;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.credit.domain.entity.RatItemCustom;
import com.bytter.credit.domain.service.RatItemCustomService;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.api.IRatCpUrationService;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.dto.RatItemCustomDTO;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.finance.constant.*;
import com.bytter.finance.domain.basedata.dao.RepaymentScheduleDao;
import com.bytter.finance.domain.basedata.entity.CmsRepaymentSchedule;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.basedata.service.RepayTypeService;
import com.bytter.finance.domain.basedata.service.RepaymentScheduleService;
import com.bytter.finance.domain.basedata.vo.RepayTypeVO;
import com.bytter.finance.domain.inl.InlUtil;
import com.bytter.finance.domain.inl.dao.InlInfoDao;
import com.bytter.finance.domain.inl.entity.CmsInlInfo;
import com.bytter.finance.domain.inl.entity.CmsInlSend;
import com.bytter.finance.domain.inl.vo.CmsInlInfoVO;
import com.bytter.finance.domain.inl.vo.GuaContractCmsInlVO;
import com.bytter.finance.dto.GuaContractCmsInlDTO;
import com.bytter.finance.enums.BaseDataMessageEnum;
import com.bytter.guarantee.api.IConService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.constant.GuaranteeConstant;
import com.bytter.guarantee.dto.ConInfoDTO;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.vo.BtBankAccVO;
import com.bytter.system.api.vo.FcAccListVO;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
@WorkFlow(InlWorkFlowConstant.CMS_INL_INFO)
@DubboService(interfaceClass = IWorkFlowListener.class,group = InlWorkFlowConstant.CMS_INL_INFO)
@Service("inlInfoService")
public class InlInfoService extends BaseService<CmsInlInfo, String> implements IWorkFlowListener {
    @Autowired
    private InlInfoDao inlInfoDao;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie contractOccupServie;
    @Autowired
    private IWorkFlowService workFlowService;
    @Autowired
    private CmsProcessMsgService msService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
    private IFcAccService fcAccService;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie guaContractOccupServie;
    @Autowired
    private IRatCpUrationService ratCpUrationService;
    @Resource
    private IConService conCommonService;
    @Autowired
    private InlSendService inlSendService;
    @Autowired
    private IRatUrationService ratUrationService;
    @Autowired
    private RepaymentScheduleService repaymentScheduleService;
    @Autowired
    private RepaymentScheduleDao repaymentScheduleDao;
    @DubboReference(check = false)
    private IFbsBudgetProcessService fbsBudgetProcessService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
    private IDictService dictService;
    @Autowired
    private RepayTypeService repayTypeService;
    @Autowired
    private RatItemCustomService ratItemCustomService;

    /**
     * 返回CmsInlInfoVO
     *
     * @param id
     * @return
     */
    public CmsInlInfoVO getInfoById(String id) {
        CmsInlInfo cmsInlInfo = inlInfoDao.get(id);
        CmsInlInfoVO cmsInlInfoVO = new CmsInlInfoVO();
        BeanUtils.copyProperties(cmsInlInfo, cmsInlInfoVO);
        //还款方式
        if(StringUtils.isNotEmpty(cmsInlInfoVO.getRepayType())){
            RepayTypeVO repayTypeVO=repayTypeService.getRepayTypeInfoById(cmsInlInfoVO.getRepayType());
            if(!EmptyUtils.isEmpty(repayTypeVO)){
                cmsInlInfoVO.setRepayTypeName(repayTypeVO.getRepayName());
            }
        }
        return cmsInlInfoVO;
    }
    /**
     * 暂存打回数据列表
     *
     * @param input
     * @param user
     * @return
     */
    public DataTablesOutput datatale(DataTablesInput input, ICurrentUser user) {
        List<String> ids = corpService.getUserCorpIds(ResourceConstant.CMS_INL_INFO_QUERY, user.getId());
        return inlInfoDao.dataTable(input, ids);
    }
    /**
     * 还款计划主表
     *
     * @param busId
     */
    public CmsRepaymentSchedule getRepayPlan(String busId) {
        return repaymentScheduleService.getCmsRepaymentScheduleByBillCode(busId);
    }
    /**
     * 暂存打回数据列表
     *
     * @param input
     * @return
     */
    public DataTablesOutput approveDataTable(DataTablesInput input, ICurrentUser user) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(InlWorkFlowConstant.CMS_INL_INFO,
                input);
        return workFlowBusiness.merge(inlInfoDao.listInlDataTableApprove(input, workFlowBusiness.getBusinessIdList()));
    }
    /**
     * 数据保存
     *
     * @param cmsInlInfoVO
     * @param user
     */
    public void save(CmsInlInfoVO cmsInlInfoVO, ICurrentUser user) {
        CmsInlInfo inlInfo = null;
        if (StringUtils.isNotEmpty(cmsInlInfoVO.getId())) {
            inlInfo = inlInfoDao.get(cmsInlInfoVO.getId());
        }
        if (inlInfo != null) {
            cmsInlInfoVO.setUpdateBy(user.getId());
            cmsInlInfoVO.setUpdateDate(new Date());
            if (CommonConstant.NO.equals(cmsInlInfoVO.getIsBudget())
                    && CommonConstant.YES.equals(inlInfo.getIsBudget())) {
                /**
                 * 删除资金计划挂接
                 */
                deleteBudgetProcess(inlInfo);
            }
            String billNo = inlInfo.getBillNo();
            BeanUtils.copyNotNullProperties(cmsInlInfoVO, inlInfo);
            // 拆借编号由后端生成，编辑时不覆盖，避免前端带回上一笔编号导致重复
            inlInfo.setBillNo(billNo);
        } else {
            inlInfo = new CmsInlInfo();
            cmsInlInfoVO.setId(null);
            cmsInlInfoVO.setCreateBy(user.getId());
            cmsInlInfoVO.setCreateDate(new Date());
            cmsInlInfoVO.setBillNo(nextUniqueInlInfoBillNo());
            BeanUtils.copyNotNullProperties(cmsInlInfoVO, inlInfo);
        }
        inlInfo.setBizStatus(inlInfo.getStatus());
        inlInfoDao.saveOrUpdate(inlInfo);
        /**
         * 其他挂接处理
         */
        cmsInlInfoVO.setId(inlInfo.getId());
        otherHandle(inlInfo, cmsInlInfoVO, user);
        // 打回数据角标处理
        if (InlConstant.COMMIT_APPROVE == inlInfo.getStatus()) {
            notificationService.complete(NotificationConstant.CMS.INL_INFIO, inlInfo.getId(), user.getId());
             getStartHandler().handler(InlWorkFlowConstant.CMS_INL_INFO, inlInfo.getId(), inlInfo, inlInfo.getBorrowInCorp(),
                    user.getId());
        }
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        /* 判断业务主键是否为空 */
        if (StringUtils.isEmpty(workFlowResult.getBusinessKey())) {
            throw new BusinessException(BaseDataMessageEnum.MESSAGE_ERROR_VALISNOTNULL);
        }

        String id = workFlowResult.getBusinessKey();
        boolean isPass = workFlowResult.isPass();
        CmsInlInfo cmsInlInfo = inlInfoDao.get(id);
        ICurrentUser user = workFlowResult.getUser();
        CmsInlInfoVO cmsInlInfoVO = new CmsInlInfoVO();
        /**
         * 生成发放数据对象
         */
        CmsInlSend cmsInlSend = createSendData(cmsInlInfo);
        if (isPass) {
            cmsInlInfo.setStatus(InlConstant.APPROVED);
            msService.insertProcessMsg(cmsInlInfo.getBillNo(), InlConstant.CMS_INL_INFO_NO,
                    CmsProcessMsgConstant.MSGA_CHECKOK, "审批通过", user);
            /**
             * 增加发放消息提醒
             */
            notificationService.send(cmsInlInfo.getId(), cmsInlInfo.getBorrowInCorp(),
                    NotificationConstant.CMS.INL_SEND_APP, cmsInlSend);
        } else {
            cmsInlInfo.setStatus(InlConstant.BACK);
            /**
             * 增加消息提醒
             */
            notificationService.send(cmsInlInfo.getId(), cmsInlInfo.getBorrowInCorp(),
                    NotificationConstant.CMS.INL_INFIO, cmsInlInfo);
            msService.insertProcessMsg(cmsInlInfo.getBillNo(), InlConstant.CMS_INL_INFO_NO,
                    CmsProcessMsgConstant.MSGA_ROLLBACK, "审批打回", user);
        }
        // 资金计划挂机处理
        // updateBudget(dfDirectFinanceInfo, user);
        cmsInlInfo.setBizStatus(cmsInlInfo.getStatus());
        BeanUtils.copyProperties(cmsInlInfo, cmsInlInfoVO);

        //补充单位名称 银行类型 用作生成合同
        cmsInlInfoVO.setBorrowInCorpName(corpService.get(cmsInlInfo.getBorrowInCorp()).getName());
        cmsInlInfoVO.setBorrowOutCorpName(corpService.get(cmsInlInfo.getBorrowOutCorp()).getName());
        if (StringUtils.isNotBlank(cmsInlInfo.getRepayType())){
            RepayTypeVO repayTypeVO = repayTypeService.getRepayTypeById(cmsInlInfo.getRepayType());
            cmsInlInfoVO.setRepayTypeName(repayTypeVO != null ? repayTypeVO.getRepayName() : "");
        }
        if (StringUtils.isNotBlank(cmsInlInfo.getCpRatItemCode())){
            RatItemCustom custom = ratItemCustomService.get(cmsInlInfo.getCpRatItemCode());
            cmsInlInfoVO.setCpRatItemCodeName(custom != null ? custom.getName() : "");
        }
        // 其他挂接处理
        otherHandle(cmsInlInfo, cmsInlInfoVO, user);
        /**
         * 跳过发放
         */
        if (CommonConstant.YES.equals(cmsInlInfoVO.getIsSkipSend())) {
            /**
             * 审批通过
             */
            if (isPass) {
                inlSendService.getEntityDao().save(cmsInlSend);
                // 更新还款计划id为发放id
                CmsRepaymentSchedule cmsRepaymentSchedule = repaymentScheduleDao
                        .getCmsRepaymentScheduleByBillCode(cmsInlInfo.getId());
                repaymentScheduleDao.updateRepayMentBill(cmsRepaymentSchedule.getId(),
                        CmsConstant.INL_SEND_BILL_CODE_PREFIX, cmsInlSend.getId());
                /**
                 * 创建付款单
                 */
                inlSendService.createNisBillHead(cmsInlSend, cmsInlInfo, user);
                /**
                 * 创建收款单
                 */
                inlSendService.createNisReceivable(cmsInlSend, cmsInlInfo);
                /**
                 * 生成利息明细
                 */
                inlSendService.adjustProvideInterest(cmsInlSend, user);
            }
            /**
             * 跟新资金计划
             */
            updateBudget(cmsInlInfoVO, user);
        }
        if (isPass) {
            /**
             * 审批通过生成合同
             */
            registerCon(cmsInlInfoVO, user);
        }
        inlInfoDao.update(cmsInlInfo);
    }
    private String nextUniqueInlInfoBillNo() {
        for (int i = 0; i < 20; i++) {
            String billNo = InlUtil.getInlInfoNo();
            if (!inlInfoDao.existsBillNo(billNo)) {
                return billNo;
            }
        }
        throw new BusinessException("拆借编号生成失败，请重试");
    }

    /**
     * 生成发放数据
     *
     * @param cmsInlInfo
     */
    public CmsInlSend createSendData(CmsInlInfo cmsInlInfo) {
        CmsInlSend cmsInlSend = new CmsInlSend();
        BeanUtils.copyProperties(cmsInlInfo, cmsInlSend);
        cmsInlSend.setId(null);
        cmsInlSend.setSendMoney(cmsInlInfo.getMoney());
        cmsInlSend.setInlInfoId(cmsInlInfo.getId());
        cmsInlSend.setBillNo(InlUtil.getInlSendNo());
        cmsInlSend.setSendDate(cmsInlInfo.getStartDate());
        cmsInlSend.setStatus(InlConstant.APPROVED);
        return cmsInlSend;
    }
    public void doFlowCompleted() {
        // 资金计划处理
    }
    /**
     * 查询内部/银行账号
     *
     * @param input
     * @return
     */
    public DataTablesOutput listAcc(DataTablesInput input) {
        DataTablesOutput output;
        String accId = null;
        Map<String, Object> params = input.getFormdata();
        if (null != params && null != params.get("accId") && StringUtils.isNotEmpty(params.get("accId").toString())) {
            accId = params.get("accId").toString();
        } else {
            output = fcAccService.getFcAcc(input);
            List<FcAccListVO> list = output.getData();
            this.setCurrencyFcAcc(list);
            return output;
        }
        BtBankAccDTO bankAccDTO = btBankAccService.getBankAccById(accId);
        if (null != bankAccDTO) {
            output = btBankAccService.getDataTablesUnionAll(input);
            List<BtBankAccVO> list = output.getData();
            this.setCurrencyBankAcc(list);
        } else {
            // 设置账户类别过滤只能选择企业活期
            input.getFormdata().put("eqs_accType", "01");
            output = fcAccService.getFcAcc(input);
            List<FcAccListVO> list = output.getData();
            this.setCurrencyFcAcc(list);
        }
        return output;
    }
    /**
     * 查询银行账号
     *
     * @param input
     * @return
     */
    public DataTablesOutput listBankAcc(DataTablesInput input) {
        DataTablesOutput dataTablesOutput = btBankAccService.getDataTablesUnionAll(input);
        List<BtBankAccVO> list = dataTablesOutput.getData();
        this.setCurrencyBankAcc(list);
        return dataTablesOutput;
    }
    /**
     * 查询内部账号
     *
     * @param input
     * @return
     */
    public DataTablesOutput listFcAcc(DataTablesInput input) {
        DataTablesOutput output;
        String borrowInCorp = null;
        String borrowOutCorp = null;
        Map<String, Object> params = input.getFormdata();
        if (null != params.get("borrowInCorp") && StringUtils.isNotEmpty(params.get("borrowInCorp").toString())) {
            borrowInCorp = params.get("borrowInCorp").toString();
        }
        if (null != params.get("borrowOutCorp") && StringUtils.isNotEmpty(params.get("borrowOutCorp").toString())) {
            borrowOutCorp = params.get("borrowOutCorp").toString();
        }
        //xiangs 备注 下面这个兼容毫无意义 你应该是修改前端的传值 不应该改这
//        input.getFormdata().put("likes_blnctrAcc", params.get("likes_accName"));
//        input.getFormdata().remove("likes_accName");
        if (StringUtils.isNotEmpty(borrowInCorp) && StringUtils.isNotEmpty(borrowOutCorp)) {
            String netId1 = corpService.getNetByCorpId(borrowInCorp).getId();
            String netId2 = corpService.getNetByCorpId(borrowOutCorp).getId();
            if (StringUtils.isNotEmpty(netId1) && StringUtils.isNotEmpty(netId2) && netId1.equals(netId2)) {
                input.getFormdata().put("eqs_accType", "01");
                output = fcAccService.getFcAcc(input);
            } else {
                output = new DataTablesOutput();
            }
        } else {
            output = new DataTablesOutput();
        }

        List<FcAccListVO> list = output.getData();
        this.setCurrencyFcAcc(list);
        return output;
    }
    /**
     * 其他业务处理
     *
     * @param cmsInlInfo
     * @param cmsInlInfoVO
     * @param user
     */
    public void otherHandle(CmsInlInfo cmsInlInfo, CmsInlInfoVO cmsInlInfoVO, ICurrentUser user) {
        /**
         * 担保处理
         */
        doGua(cmsInlInfo);
        /**
         * 授信处理
         */
        doCpRate(cmsInlInfo, "0");
        /**
         * 合同挂接
         */
        hookCon(cmsInlInfo, user);
        /**
         * 取消合同挂接
         */
        unhookCon(cmsInlInfo, user);
        /**
         * 更新还款计划
         */
        if (CommonConstant.YES.equals(cmsInlInfo.getIsSkipSend())) {
            updateRepayPlan(cmsInlInfoVO);
        }
        /**
         * 判断是否挂接挂接资金计划
         */
        if (CommonConstant.YES.equals(getIsBudget().getValue())
                && CommonConstant.YES.equals(cmsInlInfoVO.getIsBudget())) {
            budgetHandle(cmsInlInfoVO, user);
        }
    }
    /**
     * 资金计划挂接处理
     *
     * @param cmsInlInfoVO
     * @param user
     */
    private void budgetHandle(CmsInlInfoVO cmsInlInfoVO, ICurrentUser user) {
        if (!EmptyUtils.isEmptyArray(cmsInlInfoVO.getItemCode())) {
            List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();
            for (int i = 0; i < cmsInlInfoVO.getItemCode().length; i++) {
                FbsBudgetProcessDTO fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
                if (InlConstant.TMP == cmsInlInfoVO.getStatus()) {
                    fbsBudgetProcessDTO.setStatus(BudgetConstant.STATUE_TEMP);// 状态:11在途，95占用
                } else if (InlConstant.APPROVED == cmsInlInfoVO.getStatus()) {
                    fbsBudgetProcessDTO.setStatus(BudgetConstant.STATUE_CHECK);// 状态:11在途，95占用
                } else {
                    fbsBudgetProcessDTO.setStatus(BudgetConstant.STATUE_WAIT_CHECK);// 状态:11在途，95占用
                }
                fbsBudgetProcessDTO.setBillNo(cmsInlInfoVO.getBillNo());// 业务单据号
                fbsBudgetProcessDTO.setBillId(cmsInlInfoVO.getId());
                fbsBudgetProcessDTO.setItemCode(cmsInlInfoVO.getItemCode()[i]);// 科目代码
                fbsBudgetProcessDTO.setItemName(cmsInlInfoVO.getItemName()[i]);// 科目名称
                fbsBudgetProcessDTO.setProcessMoney(cmsInlInfoVO.getProcessMoney()[i]);// 执行金额
                fbsBudgetProcessDTO.setProcessDate(cmsInlInfoVO.getStartDate());// 执行日期
                fbsBudgetProcessDTO.setCorpId(cmsInlInfoVO.getBorrowInCorp());// 单位id
                fbsBudgetProcessDTO.setCurId(cmsInlInfoVO.getCurId());// 币别
                fbsBudgetProcessDTO.setBusinessName(BudgetConstant.BUSINESS_NAME_INL_SEND);// 执行业务名称
                fbsBudgetProcessDTO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_CMS);// 执行系统名称
                fbsBudgetProcessDTO.setCreateBy(user.getLoginName());
                fbsBudgetProcessDTO.setCreateDate(DateUtils.getDate());
                list.add(fbsBudgetProcessDTO);
            }
            // 修改时，先删除之前的计划执行数据再保存
            fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_INL_SEND,
                    BudgetConstant.PROCESS_SYSNAME_CMS, cmsInlInfoVO.getBillNo());
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
    public void updateBudget(CmsInlInfoVO cmsInlInfoVO, ICurrentUser user) {
        /**
         * 判断是否挂接挂接资金计划
         */
        if (CommonConstant.YES.equals(getIsBudget().getValue())
                && CommonConstant.YES.equals(cmsInlInfoVO.getIsBudget())) {
            if (InlConstant.APPROVED == cmsInlInfoVO.getStatus()) {
                fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_INL_SEND,
                        BudgetConstant.PROCESS_SYSNAME_CMS, cmsInlInfoVO.getBillNo(), BudgetConstant.STATUE_CHECK,
                        user);
            } else {
                fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_INL_SEND,
                        BudgetConstant.PROCESS_SYSNAME_CMS, cmsInlInfoVO.getBillNo(), BudgetConstant.STATUE_BACK, user);
            }
        }
    }
    /**
     * 获取是否挂接资金计划配置
     *
     * @return
     */
    private ConfigDTO getIsBudget() {
        return configService.getConfig(ConfigConstant.INL, InlConstant.CMS_INL_IS_BUDGET);
    }
    /**
     * 还款计划数据更新
     *
     * @param cmsInlInfoVO
     */
    public void updateRepayPlan(CmsInlInfoVO cmsInlInfoVO) {
        if (InlConstant.COMMIT_APPROVE == cmsInlInfoVO.getStatus()) {
            repaymentScheduleDao.updateRepayMentBill(cmsInlInfoVO.getPlanId(), CmsConstant.INL_BILL_CODE_PREFIX,
                    cmsInlInfoVO.getId());
            repaymentScheduleDao.flush();
        }
    }
    /**
     * 业务生成合同
     */
    public void registerCon(CmsInlInfoVO cmsInlInfoVO, ICurrentUser user) {
        if (CommonConstant.YES.equals(cmsInlInfoVO.getIsCon()) && CommonConstant.NO.equals(cmsInlInfoVO.getConType())) {
            ConInfoDTO conInfoDTO = new ConInfoDTO();
            conInfoDTO.setConName(cmsInlInfoVO.getBorrowInCorpName() + CmsConstant.INL_CON);// 合同名称 发起单位+产品名称+“合同”
            conInfoDTO.setCorpId(cmsInlInfoVO.getBorrowInCorp());// 合同发起人:申请单位
            conInfoDTO.setConNo(cmsInlInfoVO.getBillNo());// 合同号
            conInfoDTO.setBusId(cmsInlInfoVO.getId());
            conInfoDTO.setTypeId(cmsInlInfoVO.getConTypeId());// 合同类型
            conInfoDTO.setBeginDate(cmsInlInfoVO.getStartDate());// 生效日期
            conInfoDTO.setEndDate(cmsInlInfoVO.getEndDate());// 失效日期
            conInfoDTO.setOutBusData(cmsInlInfoVO);
            if (CommonConstant.YES.equals(cmsInlInfoVO.getIsHangCredit())) {
                RatItemCustomDTO ratItemCustomDTO = ratUrationService
                        .getRatCustomByCode(cmsInlInfoVO.getCpRatItemCode());
                cmsInlInfoVO.setCpRatItemCodeName(ratItemCustomDTO.getName());
            }
            conCommonService.registerConOp(conInfoDTO, InlConstant.CMS_INL_CON_PRO, user);
            cmsInlInfoVO.setConId(conInfoDTO.getId());
        }
    }
    /**
     * 业务挂接合同
     */
    public void hookCon(CmsInlInfo inlInfo, ICurrentUser user) {
        if (CommonConstant.YES.equals(inlInfo.getIsCon()) && CommonConstant.YES.equals(inlInfo.getConType())) {
            if (InlConstant.COMMIT_APPROVE == inlInfo.getStatus()) {
                conCommonService.hookConOp(inlInfo.getConId(), inlInfo.getId(), user);
            }
        }
    }
    /**
     * 取消合同挂接
     *
     * @param inlInfo
     * @param user
     */
    public void unhookCon(CmsInlInfo inlInfo, ICurrentUser user) {
        if (CommonConstant.YES.equals(inlInfo.getIsCon()) && CommonConstant.YES.equals(inlInfo.getConType())) {
            if (inlInfo.getStatus() == InlConstant.BACK) {
                conCommonService.unHookConOp(inlInfo.getConId(), inlInfo.getId(), user);
            }
        }
    }
    /**
     * 授信挂接处理
     *
     * @param cmsInlInfo
     * @param isAllRelease
     */
    public void doCpRate(CmsInlInfo cmsInlInfo, String isAllRelease) {
        if (CommonConstant.YES.equals(cmsInlInfo.getIsHangCredit())) {
            RatUrationDTO ratUrationDTO = new RatUrationDTO();
            createRat(ratUrationDTO, cmsInlInfo, isAllRelease);
            if (cmsInlInfo.getStatus() == InlConstant.TMP) {
                ratCpUrationService.tempUseCpRation(ratUrationDTO);
            } else if (cmsInlInfo.getStatus() == InlConstant.COMMIT_APPROVE) {
                ratCpUrationService.addUseCpRation(ratUrationDTO);
            } else if (cmsInlInfo.getStatus() == InlConstant.APPROVED) {
                ratCpUrationService.passUseCpRation(ratUrationDTO);
            } else if (cmsInlInfo.getStatus() == InlConstant.BACK) {
                ratCpUrationService.backUseCpRation(ratUrationDTO);
            } else if (cmsInlInfo.getStatus() == InlConstant.DELETE) {
                ratCpUrationService.deleteUseCpRation(ratUrationDTO);
            } else if (cmsInlInfo.getStatus() == InlConstant.DEFERRED) {
                ratCpUrationService.backUseCpRation(ratUrationDTO);
                ratCpUrationService.deleteUseCpRation(ratUrationDTO);
            }
        }
    }
    /**
     * 创建挂接DTO
     *
     * @param urationDTO
     * @param cmsInlInfo
     * @param isAllRelease
     */
    public void createRat(RatUrationDTO urationDTO, CmsInlInfo cmsInlInfo, String isAllRelease) {
        urationDTO.setRatCode(cmsInlInfo.getCpRatCode());
        urationDTO.setCurCode(cmsInlInfo.getCurId());// 币别ID
        urationDTO.setBillId(cmsInlInfo.getId());// 业务ID
        urationDTO.setBillNo(cmsInlInfo.getBillNo());// 业务编号
        urationDTO.setSysCode(ConfigConstant.INL);// 内部拆借
        urationDTO.setBusinessName(RationConstant.BUSINESS_NAME_10);// 内部拆借占用
        urationDTO.setBeginDate(cmsInlInfo.getStartDate());// 内部拆借日期
        urationDTO.setEndDate(cmsInlInfo.getEndDate());// 到期日期
        urationDTO.setMoney(cmsInlInfo.getMoney());// 释放/占用金额
        urationDTO.setBillMoney(cmsInlInfo.getMoney());// 业务金额
        urationDTO.setCorpId(cmsInlInfo.getBorrowInCorp());// 网点/单位
        urationDTO.setRatItemCode(cmsInlInfo.getCpRatItemCode());
        if (StringUtils.isNotEmpty(isAllRelease) && CommonConstant.YES.equals(isAllRelease)) {
            // 释放全部授信
            urationDTO.setAllRelease(true);
        }
    }
    /**
     * 担保处理
     */
    public void doGua(CmsInlInfo cmsInlInfo) {
        if (CommonConstant.YES.equals(cmsInlInfo.getIsHangGuarantee())) {
            GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
            createGua(cmsInlInfo, guaContractOccupDTO);
            if (cmsInlInfo.getStatus() == InlConstant.TMP) {
                guaContractOccupServie.tempUseGuaContractOccup(guaContractOccupDTO);
            } else if (cmsInlInfo.getStatus() == InlConstant.COMMIT_APPROVE) {
                guaContractOccupServie.submitUseGuaContractOccup(guaContractOccupDTO);
            } else if (cmsInlInfo.getStatus() == InlConstant.APPROVED) {
                guaContractOccupServie.saveUseGuaContractOccup(guaContractOccupDTO);
            } else if (cmsInlInfo.getStatus() == InlConstant.BACK) {
                guaContractOccupServie.backUseGuaContractOccup(guaContractOccupDTO);
            }
        }
    }
    /**
     * 删除担保挂接
     *
     * @param cmsInlInfoVO
     */
    public void deleteGua(CmsInlInfoVO cmsInlInfoVO) {
        if (CommonConstant.YES.equals(cmsInlInfoVO.getIsHangGuarantee())) {
            guaContractOccupServie.deleteGuaContractOccup(cmsInlInfoVO.getId(), ConfigConstant.INL);
        }
    }
    /**
     * 设置担保挂接数据
     *
     * @param cmsInlInfo
     * @param guaContractOccupDTO
     */
    private static void createGua(CmsInlInfo cmsInlInfo, GuaContractOccupDTO guaContractOccupDTO) {
        guaContractOccupDTO.setBusinessId(cmsInlInfo.getId());
        if (StringUtils.isNotEmpty(cmsInlInfo.getGuaContractId())) {
            if (cmsInlInfo.getGuaContractId().contains(",")) {
                guaContractOccupDTO.setContractIds(Arrays.asList(cmsInlInfo.getGuaContractId().split(",")));
            } else {
                guaContractOccupDTO.setContractIds(Arrays.asList(cmsInlInfo.getGuaContractId()));
            }
        }
        guaContractOccupDTO.setSysCode(ConfigConstant.INL);
        guaContractOccupDTO.setItemNo(cmsInlInfo.getBillNo());
        guaContractOccupDTO.setUseDate(new Date());
    }
    /**
     * 删除资金计划挂接
     *
     * @param cmsInlInfo
     */
    public void deleteBudgetProcess(CmsInlInfo cmsInlInfo) {
        if (CommonConstant.YES.equals(getIsBudget().getValue())
                && CommonConstant.YES.equals(cmsInlInfo.getIsBudget())) {
            fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_INL_SEND,
                    BudgetConstant.PROCESS_SYSNAME_CMS, cmsInlInfo.getBillNo());
        }
    }
    /**
     * 业务挂接释放
     */
    public void releaseBusiness(CmsInlInfo cmsInlInfo, ICurrentUser user) {
        /**
         * 担保处理
         */
        doGua(cmsInlInfo);
        /**
         * 授信处理
         */
        doCpRate(cmsInlInfo, "0");
        /**
         * 合同处理
         */
        unhookCon(cmsInlInfo, user);
        /**
         * 删除资金计划挂接
         */
        deleteBudgetProcess(cmsInlInfo);
    }
    /**
     * 获取挂接的授信信息
     *
     * @param id
     * @return
     */
    public RatUrationDTO getCpratUrationDTO(String id) {
        RatUrationDTO ratUrationDTO = ratCpUrationService.getRatUrationDTOByBillId(ConfigConstant.INL, id);
        return ratUrationDTO;
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    @Override
    public BaseDao getEntityDao() {
        return inlInfoDao;
    }
    public DataTablesOutput<GuaContractCmsInlVO> getGuaForInl(DataTablesInput input, ICurrentUser user) {
        //todo 统一调整
        String sysCode =  StrUtil.toStringOrNull(input.getFormdata().get("eqs_sysCode"));
        String startDate =  StrUtil.toStringOrNull(input.getFormdata().get("ged_useDate"));
        String endDate = StrUtil.toStringOrNull(input.getFormdata().get("led_useDate"));
        String status = StrUtil.toStringOrNull(input.getFormdata().get("eqg_status"));
        String contractId =  StrUtil.toStringOrNull(input.getFormdata().get("eqs_contractId"));
        input.setFormdata(null);
        //获取挂接授信的业务id集合和封装参数
        //获取挂接授信的业务id集合和封装参数
        List<GuaContractOccupDTO> guaList;
        if (StrUtil.isBlank(contractId)) {
            guaList = guaContractOccupServie.listUseGua(sysCode, startDate, endDate, status);
        }else {
            guaList = guaContractOccupServie.getGua(sysCode, startDate, endDate, status,contractId);
        }
        List<String> corpIds = corpService.getUserCorpIds(GuaranteeConstant.CMS_GUARANTEE_CHANGE_QUERY, user.getId());
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
        DataTablesOutput<GuaContractCmsInlVO> dataTablesOutput = inlInfoDao.getGuaForInl(input, corpIds, businessIdList);
        if (EmptyUtils.isNotEmpty(dataTablesOutput.getData())) {
            for (GuaContractCmsInlVO item : dataTablesOutput.getData()) {
                if (guaMap.containsKey(item.getId())) {
                    item.setStatus(guaMap.get(item.getId()).getStatus());
                    item.setUseDate(guaMap.get(item.getId()).getUseDate());
                }
            }
        }
        return dataTablesOutput;
    }
    /**
     * 获取所有挂接担保的内部拆借数据
     */
    public List<GuaContractCmsInlDTO> getGuaInlLoan() {
        return inlInfoDao.getGuaInlLoan();
    }

    public void setCurrencyFcAcc(List<FcAccListVO> list){
        if(null != list && list.size() >0){
            Map<String, BtCurrencyDTO> currencyMap = btCurrencyService.getCurrency();
            if(null != currencyMap){
                for(FcAccListVO vo : list){
                    BtCurrencyDTO btCurrencyDTO = currencyMap.get(vo.getCurId());
                    if(null != btCurrencyDTO){
                        vo.setCurName(btCurrencyDTO.getCurName());
                    }
                }
            }
        }
    }

    public void setCurrencyBankAcc(List<BtBankAccVO> list){
        if(null != list && list.size() >0){
            Map<String, BtCurrencyDTO> currencyMap = btCurrencyService.getCurrency();
            if(null != currencyMap){
                for(BtBankAccVO vo : list){
                    BtCurrencyDTO btCurrencyDTO = currencyMap.get(vo.getCurId());
                    if(null != btCurrencyDTO){
                        vo.setCurName(btCurrencyDTO.getCurName());
                    }
                }
            }
        }
    }

}
