package com.bytter.finance.domain.provide.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.BusOriType;
import com.bytter.biz.finacial.api.constant.FinaBookConstant.SysRaceCode;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.biz.finacial.api.dto.*;
import com.bytter.biz.finacial.api.service.IFcAccbookService;
import com.bytter.biz.finacial.api.service.IFcInterestService;
import com.bytter.biz.payment.api.dto.NisAllocationReqDTO;
import com.bytter.biz.payment.api.enums.AllocationMessageEnum;
import com.bytter.biz.payment.api.service.INisAllocationReqService;
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
import com.bytter.core.constant.*;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.finance.api.ICmsProvideLoanService;
import com.bytter.finance.constant.*;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.finance.domain.basedata.dao.RepaymentScheduleDao;
import com.bytter.finance.domain.basedata.entity.RepaymentSchedule;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.basedata.service.RepayTypeService;
import com.bytter.finance.domain.basedata.vo.RepayTypeVO;
import com.bytter.finance.domain.loan.service.CmsLoanInfoApplyService;
import com.bytter.finance.domain.precative.service.CmsPrecativeLoanSendService;
import com.bytter.finance.domain.provide.dao.CmsProvideInterestDao;
import com.bytter.finance.domain.provide.dao.CmsProvideLoanRepayDao;
import com.bytter.finance.domain.provide.dao.ProvideSyndicatedDtlDao;
import com.bytter.finance.domain.provide.entity.*;
import com.bytter.finance.domain.provide.utils.ProvideUtils;
import com.bytter.finance.domain.provide.vo.CmsProvideInterestVO;
import com.bytter.finance.domain.provide.vo.LoanRepayBatchVO;
import com.bytter.finance.domain.provide.vo.LoanRepayVO;
import com.bytter.finance.dto.CmsProvideInterestDTO;
import com.bytter.finance.dto.GuaContractCmsProvideLoanDTO;
import com.bytter.finance.enums.ProvideMessageEnum;
import com.bytter.guarantee.api.*;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.ConInfoRecordDTO;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.guarantee.dto.GuaWayDTO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.*;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.system.api.dto.*;
import com.bytter.workflow.api.IWorkFlowService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 借款还款
 * @date 2017-08-08 15:15:54
 */
@Service("cmsProvideLoanRepayService")
@Transactional
@WorkFlow(value = {CmsWorkFlowConstant.CMS_PROVIDE_LOAN_REPAY, CmsWorkFlowConstant.CMS_PROVIDE_LOAN_REPAY_REPAIR})
@DubboService(interfaceClass = ICmsProvideLoanService.class)
public class CmsProvideLoanRepayService extends BaseService<CmsProvideLoanRepay, String>
        implements IWorkFlowListener, ICmsProvideLoanService, ICorpDisableVerifyService, ITaskService {
    @Autowired
    private CmsProvideLoanRepayDao cmsProvideLoanRepayDao;
    @Autowired
    private CmsProvideInterestService cmsProvideInterestService;
    @Autowired
    private CmsProvideInterestDao cmsProvideInterestDao;
    @Autowired
    private RepaymentScheduleDao repaymentScheduleDao;
    @Autowired
    private CmsProvideLoanSendService cmsProvideLoanSendService;
    @Autowired
    private LoanInfoService loanInfoService;
    @Autowired
    private IWorkFlowService workFlowService;
    @Autowired
    private CmsPrecativeLoanSendService cmsPrecativeLoanSendService;
    @DubboReference(check = false)
    private IInnerAccountService innerAccountService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private ICorpService corpServiceImpl;
    @Autowired
    private IRatUrationService urationService;
    @DubboReference(check = false)
    private IFbsBudgetProcessService fbsBudgetProcessService;
    @Autowired
    private CmsProcessMsgService msService;
    @DubboReference(check = false)
    private IFcAccService fcAccService;
    @DubboReference(check = false)
    private INotificationService notificationService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private INisAllocationReqService nisAllocationReqService;
    @DubboReference(check = false)
    private IFcAccbookService fcAccbookService;
    @DubboReference(check = false)
    private IUserService userServiceImpl;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @Resource(name = "fcBondHookServiceImp")
    private IFcBondHookService fcBondHookService;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie guaContractOccupServie;
    @Autowired
    private IGuaWayServie guaWayService;
    @Resource
    private IConService conCommonService;
    @DubboReference(check = false)
    private IFcInterestService iFcInterestService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @Resource(name = "guaPledgeOccupyServiceImpl")
    private IGuaPledgeOccupyService pledgeOccupyService;
    @DubboReference(check = false)
    private INisAllocationReqService inisAllocationReqService;
    @Autowired
    private ProvideSyndicatedDtlDao provideSyndicatedDtlDao;
    @Autowired
    private RepayTypeService repayTypeService;
    @Autowired
    private CmsLoanInfoApplyService cmsLoanInfoApplyService;
    @DubboReference(check = false)
    private IDictService iDictService;
    @DubboReference(check = false)
    private ICalendarService calendarService;

    @Autowired
    private CmsProvideInterestTempService tempService;

    @Autowired
    private ScheduleDtlTempService dtlTempService;

    /**
     * 功能介绍 : 还款申请列表 - 分页<br>
     */
    @Transactional(readOnly = true)
    public DataTablesOutput<LoanRepayVO> getDataAppTables(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_PROVIDE_REPAY_QUERY, user.getId());
        DataTablesOutput<LoanRepayVO> data = cmsProvideLoanRepayDao.getDataAppTables(dataTablesInput, corpAuth);
        // 添加最近一期还款的信息
        List<LoanRepayVO> list = data.getData();
        String geRepayMoney = "";
        String leRepayMoney = "";
        String geRepayAccrual = "";
        String legRepayAccrual = "";
        String gegRepaymentDate = "";
        String legRepaymentDate = "";
        Map<String, Object> formdata = dataTablesInput.getFormdata();
        if (formdata.containsKey("geg_sumRepayMoney")) {
            geRepayMoney = String.valueOf(formdata.get("geg_sumRepayMoney"));
        }
        if (formdata.containsKey("leg_sumRepayMoney")) {
            leRepayMoney = String.valueOf(formdata.get("leg_sumRepayMoney"));
        }
        if (formdata.containsKey("geg_sumRepayAccrual")) {
            geRepayAccrual = String.valueOf(formdata.get("geg_sumRepayAccrual"));
        }
        if (formdata.containsKey("leg_sumRepayAccrual")) {
            legRepayAccrual = String.valueOf(formdata.get("leg_sumRepayAccrual"));
        }
        if (formdata.containsKey("geg_repaymentDate")) {
            gegRepaymentDate = String.valueOf(formdata.get("geg_repaymentDate"));
        }
        if (formdata.containsKey("leg_repaymentDate")) {
            legRepaymentDate = String.valueOf(formdata.get("leg_repaymentDate"));
        }
        if (EmptyUtils.isNotEmpty(list)) {
            List<String> plTypes = list.stream().map(LoanRepayVO::getPlType).collect(Collectors.toList());
            Map<String, GuaWayDTO> plTypeMap = guaWayService.getGuaWayNameByIds(plTypes);
            for (LoanRepayVO vo : list) {
                List<CmsProvideInterestVO> cmsProvideInterestVOList = cmsProvideInterestService.queryInterestList(vo.getSendCode()
                        , geRepayMoney, leRepayMoney, geRepayAccrual, legRepayAccrual, gegRepaymentDate, legRepaymentDate);
                if (plTypeMap.containsKey(vo.getPlType())) {
                    vo.setPlTypeName(plTypeMap.get(vo.getPlType()).getName());
                }
                if (ListUtils.isNotNullOrEmpty(cmsProvideInterestVOList)) {
                    boolean che = true;
                    for (CmsProvideInterestVO busVo : cmsProvideInterestVOList) {
                        if (che && Integer.valueOf(CmsConstant.REPAYMENT_SCHEDULE_ISREPAYNO).equals(busVo.getStatus())) {// 未还
                            // 线下还款日期
                            vo.setRepayDate(busVo.getSignRepayDate());
                            // 线下还款本金
                            vo.setRepayMoney(busVo.getCountRepayMoney());
                            // 线下还款利息
                            vo.setRevertAccrual(busVo.getCountPayInterest());
                            // 借款利息预计明细ID
                            vo.setProvideInterestId(busVo.getId());
                            che = false;
                        }
                    }
                }
            }
        }
        return data;
    }
    /**
     * 功能介绍 : 还款跟踪列表 - 分页<br>
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataOnlineRepayFollow(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_PROVIDE_REPAY_QUERY, user.getId());
        DataTablesOutput data = cmsProvideLoanRepayDao.getDataOnlineRepayFollow(dataTablesInput, corpAuth);
        List<LoanRepayVO> list = data.getData();
        if (!EmptyUtils.isEmptyList(list)) {
            // 头寸是否记账参数
            String isAccount = configService
                    .getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_BORROW_TALLY).getValue();
            for (LoanRepayVO vo : list) {

                if(StringUtils.isBlank(vo.getVoucherStat())) {
                    //查询指令
                    NisAllocationReqDTO nisAllocationReqDTO = new NisAllocationReqDTO();
                    nisAllocationReqDTO.setErpId(vo.getId());
                    List<NisAllocationReqDTO> nisAllocationReqDTOList = inisAllocationReqService.getRepayBisExc(nisAllocationReqDTO);
                    for (NisAllocationReqDTO nisAllocationReq : nisAllocationReqDTOList) {
                        vo.setReqStatus(nisAllocationReq.getStatus());
                        vo.setVoucherStat(nisAllocationReq.getVoucherStat());
                    }
                }

                if (CommonConstant.NO.equals(isAccount)) {// 不记账
                    if (NetbankConstant.STATUE_CHECK.equals(vo.getReqStatus())) {// 不记账 头寸数据95流程结束
                        vo.setReqStatus(95);// 95(审批通过)作为流程完成标识，头寸中没有流程完成标识符
                    }
                } else {// 记账
                    if (CorpConstant.CORP_TYPE_CODE_NET == vo.getCorpType()) {// 网点
                        if (NetbankConstant.STATUE_CHECK.equals(vo.getReqStatus())) {// 网点 记账 头寸数据95流程结束
                            vo.setReqStatus(100);// 100(记账复核通过)作为流程完成标识，头寸中没有流程完成标识符
                        }
                    } else {// 单位
                        if (NetbankConstant.STATUE_CHECK.equals(vo.getReqStatus())) {// 单位 不记账 头寸数据95流程结束
                            vo.setReqStatus(95);// 95(审批通过)作为流程完成标识，头寸中没有流程完成标识符
                        }
                    }
                }
            }
        }
        return data;
    }
    /**
     * 功能介绍 : 还款修改列表 - 分页<br>
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataUpdateTables(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_PROVIDE_REPAY_QUERY, user.getId());
        return cmsProvideLoanRepayDao.getDataUpdateTables(dataTablesInput, user.getLoginName(), corpAuth);
    }
    /**
     * 功能介绍 : 还款申请审批列表 - 分页<br>
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataRepayApproveTables(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService
                .getWorkFlowBusiness(CmsWorkFlowConstant.CMS_PROVIDE_LOAN_REPAY, dataTablesInput);
        DataTablesOutput dp = cmsProvideLoanRepayDao.getDataRepayApproveTables(dataTablesInput,
                workFlowBusiness.getBusinessIdList());
        List<LoanRepayVO> data = dp.getData();
        Map<String, DictDTO> dictDTOMap = iDictService.listMap("cms.leasepay.online.sign");
        for (LoanRepayVO vo : data){
            if (ObjectUtil.isNotEmpty(vo.getOnlineSign()) && dictDTOMap.containsKey(vo.getOnlineSign())){
                vo.setOnlineSignName(dictDTOMap.get(vo.getOnlineSign()).getName());
            }
        }
        dp.setData(data);
        return workFlowBusiness.merge(dp);
    }
    /**
     * 功能介绍 : 还款补录列表 - 分页<br>
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataRepairTables(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_PROVIDE_REPAY_QUERY, user.getId());
        return cmsProvideLoanRepayDao.getDataRepairTables(dataTablesInput, corpAuth);
    }
    /**
     * 功能介绍 : 还款补录审批列表 - 分页<br>
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataRepayRepairApproveTables(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService
                .getWorkFlowBusiness(CmsWorkFlowConstant.CMS_PROVIDE_LOAN_REPAY_REPAIR, dataTablesInput);
        DataTablesOutput dp = cmsProvideLoanRepayDao.getDataRepayRepairApproveTables(dataTablesInput,
                workFlowBusiness.getBusinessIdList());
        return workFlowBusiness.merge(dp);
    }
    /**
     * 功能介绍 : 还款计划修改列表 - 分页<br>
     */
    @Transactional(readOnly = true)
    public DataTablesOutput dataRepayPlanable(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_PROVIDE_SEND_QUERY, user.getId());
        DataTablesOutput data = cmsProvideLoanRepayDao.dataRepayPlanable(dataTablesInput, corpAuth);
        // 添加最近一期还款的信息
        List<LoanRepayVO> list = data.getData();
        String geRepayMoney = "";
        String leRepayMoney = "";
        String geRepayAccrual = "";
        String legRepayAccrual = "";
        String gegRepaymentDate = "";
        String legRepaymentDate = "";
        Map<String, Object> formdata = dataTablesInput.getFormdata();
        if (formdata.containsKey("geg_sumRepayMoney")) {
            geRepayMoney = String.valueOf(formdata.get("geg_sumRepayMoney"));
        }
        if (formdata.containsKey("leg_sumRepayMoney")) {
            leRepayMoney = String.valueOf(formdata.get("leg_sumRepayMoney"));
        }
        if (formdata.containsKey("geg_sumRepayAccrual")) {
            geRepayAccrual = String.valueOf(formdata.get("geg_sumRepayAccrual"));
        }
        if (formdata.containsKey("leg_sumRepayAccrual")) {
            legRepayAccrual = String.valueOf(formdata.get("leg_sumRepayAccrual"));
        }
        if (formdata.containsKey("geg_repaymentDate")) {
            gegRepaymentDate = String.valueOf(formdata.get("geg_repaymentDate"));
        }
        if (formdata.containsKey("leg_repaymentDate")) {
            legRepaymentDate = String.valueOf(formdata.get("leg_repaymentDate"));
        }
        List<String> guaIds = list.stream().distinct().map(LoanRepayVO::getPlType).collect(Collectors.toList());
        Map<String, GuaWayDTO> guaMap = null;
        if (EmptyUtils.isNotEmpty(guaIds)) {
            guaMap = guaWayService.getGuaWayNameByIds(guaIds);
        }
        for (LoanRepayVO vo : list) {
            List<CmsProvideInterestVO> cmsProvideInterestVOList = cmsProvideInterestService.queryInterestList(vo.getSendCode()
                    , geRepayMoney, leRepayMoney, geRepayAccrual, legRepayAccrual, gegRepaymentDate, legRepaymentDate);
            if (null != guaMap && guaMap.containsKey(vo.getPlType())) {
                vo.setPlTypeName(guaMap.get(vo.getPlType()).getName());
            }
            if (ListUtils.isNotNullOrEmpty(cmsProvideInterestVOList)) {
                boolean che = true;
                for (CmsProvideInterestVO busVo : cmsProvideInterestVOList) {
                    if (che && Integer.valueOf(CmsConstant.REPAYMENT_SCHEDULE_ISREPAYNO).equals(busVo.getStatus())) {// 未还
                        // 线下还款日期
                        vo.setRepayDate(busVo.getSignRepayDate());
                        // 线下还款本金
                        vo.setRepayMoney(busVo.getCountRepayMoney());
                        // 线下还款利息
                        vo.setRevertAccrual(busVo.getCountPayInterest());
                        // 借款利息预计明细ID
                        vo.setProvideInterestId(busVo.getId());
                        che = false;
                    }
                }
            }
        }
        return data;
    }
    /**
     * 功能介绍 : 计息方式修改列表 - 分页<br>
     */
    @Transactional(readOnly = true)
    public DataTablesOutput dataInterestWayable(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_PROVIDE_SEND_QUERY, user.getId());
        DataTablesOutput data = cmsProvideLoanRepayDao.dataInterestWayable(dataTablesInput, corpAuth);
        // 添加最近一期还款的信息
        List<LoanRepayVO> list = data.getData();
        for (LoanRepayVO vo : list) {
            List<CmsProvideInterestVO> cmsProvideInterestVOList = cmsProvideInterestService.queryInterestList(vo.getSendCode(), "", "", "", "", "", "");
            if (ListUtils.isNotNullOrEmpty(cmsProvideInterestVOList)) {
                boolean che = true;
                for (CmsProvideInterestVO busVo : cmsProvideInterestVOList) {
                    if (che && Integer.valueOf(CmsConstant.REPAYMENT_SCHEDULE_ISREPAYNO).equals(busVo.getStatus())) {// 未还
                        // 线下还款日期
                        vo.setRepayDate(busVo.getSignRepayDate());
                        // 线下还款本金
                        vo.setRepayMoney(busVo.getCountRepayMoney());
                        // 线下还款利息
                        vo.setRevertAccrual(busVo.getCountPayInterest());
                        // 借款利息预计明细ID
                        vo.setProvideInterestId(busVo.getId());
                        che = false;
                    }
                }
            }
        }
        return data;
    }
    public CmsProvideLoanRepay getRepayInfoByBillCode(String billCode) {
        return cmsProvideLoanRepayDao.getRepayInfoByBillCode(billCode);
    }

    /**
     * 根据预计利息明细id得到对应的还款
     * @param interestId
     * @return
     */
    public LoanRepayVO getRepayInfoByInterestId(String interestId){
        return cmsProvideLoanRepayDao.getRepayInfoByInterestId(interestId);
    }

    /**
     * 功能介绍 : 浮动方式修改列表 - 分页<br>
     */
    @Transactional(readOnly = true)
    public DataTablesOutput dataFolatingModeable(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(ResourceConstant.CMS_PROVIDE_SEND_QUERY, user.getId());
        DataTablesOutput data = cmsProvideLoanRepayDao.dataFolatingModeable(dataTablesInput, corpAuth);
        // 添加最近一期还款的信息
        List<LoanRepayVO> list = data.getData();
        for (LoanRepayVO vo : list) {
            List<CmsProvideInterestVO> cmsProvideInterestVOList = cmsProvideInterestService.queryInterestList(vo.getSendCode(), "", "", "", "", "", "");
            if (ListUtils.isNotNullOrEmpty(cmsProvideInterestVOList)) {
                boolean che = true;
                for (CmsProvideInterestVO busVo : cmsProvideInterestVOList) {
                    if (che && Integer.valueOf(CmsConstant.REPAYMENT_SCHEDULE_ISREPAYNO).equals(busVo.getStatus())) {// 未还
                        // 线下还款日期
                        vo.setRepayDate(busVo.getSignRepayDate());
                        // 线下还款本金
                        vo.setRepayMoney(busVo.getCountRepayMoney());
                        // 线下还款利息
                        vo.setRevertAccrual(busVo.getCountPayInterest());
                        // 借款利息预计明细ID
                        vo.setProvideInterestId(busVo.getId());
                        che = false;
                    }
                }
            }
        }
        return data;
    }
    /**
     * 功能介绍 : 暂存或提交还款<br>
     */
    public void tempOrSubmit(LoanRepayVO vo, String saveType, String tempOrSubmitSign, ICurrentUser user, String sign) {
        CmsProvideLoanRepay repay = new CmsProvideLoanRepay();
        BeanUtils.copyNotNullProperties(vo, repay);
        validata(repay);// TODO 验证借款利息预计明细 是否重复选择 和是否最小日期
        if (CommonConstant.YES.equals(saveType)) {// 提交
            repay.setOnlineStatus(1);// 在线还款状态标记为已提交
            repay.setStatus(CmsConstant.COMMITAPPROVE);
            repay.setBizStatus(CmsConstant.BUSINESSSTATE_41);
        } else {// 暂存
            repay.setStatus(CmsConstant.TEMP);
            repay.setBizStatus(CmsConstant.BUSINESSSTATE_40);
        }
        CmsProvideLoanRepay loanRepay = null;
        ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(repay.getSendCode());
        String curId = send.getCurCode();

        if(repay.getRepayDate().compareTo(send.getSendDate())<0){
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_PAYDATELEREPLDATE);
        }

        if (CmsConstant.ONLINE_FALSE.equals(repay.getOnlineSign())) {/**/// 线上转线下清空线上还款字段，线下还款
            if (EmptyUtils.isNotEmpty(sign) && sign.equals(CommonConstant.NO)) {
                CmsProvideLoanRepay rep = this.get(vo.getId());
                repay.setVoucherNo(rep.getVoucherNo());
                this.invalidVoucher(repay);// 账务作废
                repay.setVoucherNo(null);
                repay.setOnlineSign(CommonConstant.NO);
            }
            if (!EmptyUtils.isEmpty(repay.getOnlineRealityInterest())) {
                repay.setOnlineMoney(repay.getRepayMoney().add(repay.getOnlineRealityInterest()));
            }
            repay.setOnlineStatus(null);
            repay.setOnlinePurpose(null);
//            repay.setOnlineRealityInterest(null);
//            repay.setOnlinePaymentAcc(null);
//            repay.setOnlinePaymentAccId(null);
//            repay.setOnlineBankName(null);
            repay.setTallySign(null);

            // 删除头寸数据
            if (EmptyUtils.isNotEmpty(repay.getId())) {
                nisAllocationReqService.deleteNisAllocationReq(repay.getId());
            }
        }
        if (EmptyUtils.isEmpty(repay.getId())) {
            repay.setBillCode(getLoanRepayBillCode());
            CmsProvideLoanInfo info = loanInfoService.getLoanInfoByBillCode(send.getLoanCode());
            repay.setCorpId(send.getCorpId());
            repay.setContractCode(info.getContractNo());
            repay.setLoanAcc(send.getLoanAcc());
            repay.setRepayway(send.getRepayType());
            loanRepay = repay;
            this.save(loanRepay, user);
        } else {
            loanRepay = this.get(repay.getId());
            BeanUtils.copyProperties(repay, loanRepay, "contractCode", "createBy", "createName");
            loanRepay.setUpdateBy(user.getLoginName());
            loanRepay.setUpdateName(user.getUsername());
            loanRepay.setUpdateDate(DateUtils.getDate());
            this.saveOrUpdate(loanRepay);
        }
        send.setBizStatus(CmsConstant.BUSINESSSTATE_33);
        cmsProvideLoanSendService.update(send, user);
        if (CommonConstant.NO.equals(repay.getOnlineSign())) {
            if (CommonConstant.YES.equals(repay.getAcrossRepair())) {// 跳过发放
                // 资金预算
                if (this.isBudget(repay.getCorpId(), BaseDataConstant.IS_BORROW_BUDGET,
                        BaseDataConstant.PLOAN_REPAY_HANG_BUDGET)) {
                    // 占用资金计划
                    this.batchEngross(repay, saveType, curId, vo, user);
                }
            } else {
                this.fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_PROVIDE_REPAY,
                        BudgetConstant.PROCESS_SYSNAME_CMS, repay.getBillCode());
            }
        } else {
            // 资金预算
            if (this.isBudget(repay.getCorpId(), BaseDataConstant.IS_BORROW_BUDGET,
                    BaseDataConstant.PLOAN_REPAY_HANG_BUDGET)) {
                // 占用资金计划
                this.batchEngross(repay, saveType, curId, vo, user);
            }
        }
        String repaymentScheduleId = "";
        /// **借款利息预计明细ID 用来查询还款日期标记。通过还款日期标记查询这个日期内的全部利息明细*/
        if (EmptyUtils.isNotEmpty(repay.getProvideInterestId()) && repay.getIsAdvancedRepay().equals(CommonConstant.NO)) {
            CmsProvideInterest interest = cmsProvideInterestDao.get(repay.getProvideInterestId());
            repaymentScheduleId = interest.getRepaymentScheduleId();
            List<String> interestList = cmsProvideInterestDao.getRepayDateInterestsSignById(interest.getBillCode(),interest.getSignRepayDate());
            if (!EmptyUtils.isEmptyList(interestList)) {
                for (String interestId : interestList) {
                    CmsProvideInterest inte = cmsProvideInterestDao.get(interestId);
                    inte.setStatus(2);// 在途
                    //进行预计利息明细实付利息回写
                    if(repay.getProvideInterestId().equals(interestId)){
                        inte.setRealInterest(repay.getOnlineRealityInterest());
                    }
                    cmsProvideInterestDao.saveOrUpdate(inte);
                }
            }
            // 归还利息明细之后标记对应的还款计划为已还。不能修改还款计划 并且不能重新生成借款利息预计明细
            if (!EmptyUtils.isEmpty(interest.getRepaymentScheduleId())) {// 本金还款才需要更新还款计划为已还
                RepaymentSchedule schedule = repaymentScheduleDao.get(interest.getRepaymentScheduleId());
                // 还款日必须相同
                if(interest.getSignRepayDate().equals(schedule.getPrediceRepayDate())){
                    schedule.setIsRepay(CommonConstant.YES);//已还
                    repaymentScheduleDao.saveOrUpdate(schedule);
                }
            }
        }
        // 当借款种类为“银团贷款”时，保存银团明细信息
        if (BaseDataConstant.SYNDICATED.equals(vo.getPlClass())) {
            if (vo.getSyndicatedDtlList().size() > 0) {
                Map<String, Object> param = new HashMap<>();
                param.put("repayId", loanRepay.getId());
                provideSyndicatedDtlDao.deleteLoanSyndicatedByrepayId(param);
                List<ProvideSyndicatedDtl> syndicatedList = new ArrayList<>();
                CmsProvideLoanInfo cmsProvideLoanInfo = loanInfoService.getLoanInfoByBillCode(send.getLoanCode());
                for (ProvideSyndicatedDtl syndicated : vo.getSyndicatedDtlList()) {
                    // 通过银行开户行号筛选保存的数据
                    if (!StringUtils.isEmpty(syndicated.getBankCode())) {
                        syndicated.setDirectFinanceId(cmsProvideLoanInfo.getId());
                        syndicated.setRepayId(loanRepay.getId());
                        syndicated.setStatus(CommonConstant.STATUE_CHECK);
                        syndicated.setScheduleId(StringUtils.isEmpty(repaymentScheduleId) ? ""
                                : repaymentScheduleDao.get(repaymentScheduleId).getRelationId());
                        syndicated.setScheduleDetailedId(repaymentScheduleId);
                        syndicatedList.add(syndicated);
                    }
                }
                provideSyndicatedDtlDao.batchSave(syndicatedList);
            }
        }
        if ("1".equals(tempOrSubmitSign)) {// 处于修改状态标志
            if ("1".equals(saveType)) {// 提交
                msService.insertProcessMsg(loanRepay.getBillCode(), ProvideConstant.REPAY_BILL_CODE_PREFIX,CmsProcessMsgConstant.MSGA_REPAIR_MODIFY, CmsProcessMsgConstant.MSGA_REPAY_UPDATE_SUBMIT, user);
                msService.insertProcessMsg(loanRepay.getSendCode(), ProvideConstant.REPAY_BILL_CODE_PREFIX,CmsProcessMsgConstant.MSGA_REPAIR_MODIFY, CmsProcessMsgConstant.MSGA_REPAY_UPDATE_SUBMIT, user);
                 getStartHandler().handler(CmsWorkFlowConstant.CMS_PROVIDE_LOAN_REPAY, loanRepay.getId(), loanRepay,loanRepay.getCorpId(), user.getId());
            } else {
                msService.insertProcessMsg(loanRepay.getBillCode(), ProvideConstant.REPAY_BILL_CODE_PREFIX,CmsProcessMsgConstant.MSGA_MODIFY, CmsProcessMsgConstant.MSGA_REPAY_UPDATE_TEMP, user);
                msService.insertProcessMsg(loanRepay.getSendCode(), ProvideConstant.REPAY_BILL_CODE_PREFIX,CmsProcessMsgConstant.MSGA_MODIFY, CmsProcessMsgConstant.MSGA_REPAY_UPDATE_TEMP, user);
            }
        } else if ("0".equals(tempOrSubmitSign) || EmptyUtils.isEmpty(tempOrSubmitSign)) {
            if ("1".equals(saveType)) {// 提交
                msService.insertProcessMsg(loanRepay.getBillCode(), ProvideConstant.REPAY_BILL_CODE_PREFIX,CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_REPAY_SUBMIT, user);
                msService.insertProcessMsg(loanRepay.getSendCode(), ProvideConstant.REPAY_BILL_CODE_PREFIX,CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_REPAY_SUBMIT, user);
                 getStartHandler().handler(CmsWorkFlowConstant.CMS_PROVIDE_LOAN_REPAY, loanRepay.getId(), loanRepay,loanRepay.getCorpId(), user.getId());
            } else {
                msService.insertProcessMsg(loanRepay.getBillCode(), ProvideConstant.REPAY_BILL_CODE_PREFIX,CmsProcessMsgConstant.MSGA_INPUT, CmsProcessMsgConstant.MSGA_REPAY_TEMP, user);
                msService.insertProcessMsg(loanRepay.getSendCode(), ProvideConstant.REPAY_BILL_CODE_PREFIX,CmsProcessMsgConstant.MSGA_INPUT, CmsProcessMsgConstant.MSGA_REPAY_TEMP, user);
            }
        }
        notificationService.complete(NotificationConstant.CMS.PROVIDE_REPAY, loanRepay.getId(), user.getId());
    }
    /**
     * 功能介绍 : 批量线下还款<br>
     */
    public void batchSubmit(LoanRepayVO loanRepayVO, ICurrentUser user) {
        List<LoanRepayBatchVO> loanRepayBatchList = loanRepayVO.getLoanRepayBatchList();
        Date date = new Date();
        for (LoanRepayBatchVO vo : loanRepayBatchList) {
            LoanRepayVO repayVO = new LoanRepayVO();
            BeanUtils.copyProperties(vo, repayVO);
            repayVO.setAcrossRepair(FinacialConstant.BOOLEAN_TRUE);// 跳过还款补录：是。
            repayVO.setOnlineSign(CmsConstant.ONLINE_FALSE);
            repayVO.setTempOrSubmitSign(CmsConstant.IS_FALSE);
            repayVO.setIsAdvancedRepay(FinacialConstant.BOOLEAN_FALSE);// 是否提前还款：否。
            repayVO.setRmk("批量还款");
            repayVO.setOnlineRealityInterest(repayVO.getRepayAccrual());
            repayVO.setCreateDate(date);
            this.tempOrSubmit(repayVO, CmsConstant.IS_TRUE, CmsConstant.IS_FALSE, user, "");
        }
    }
    /**
     * 占用预算
     *
     * @param entity
     * @param vo     借款还款VO
     */
    public void batchEngross(CmsProvideLoanRepay entity, String saveType, String curId, LoanRepayVO vo,
                             ICurrentUser user) {
        if (!EmptyUtils.isEmptyArray(vo.getItemCode())) {
            Integer Status = null;
            List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();
            if ("1".equals(saveType)) {
                Status = BudgetConstant.STATUE_WAIT_CHECK;
            } else {
                Status = BudgetConstant.STATUE_TEMP;
            }
            for (int i = 0; i < vo.getItemCode().length; i++) {
                FbsBudgetProcessDTO fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
                fbsBudgetProcessDTO.setStatus(Status);// 状态:11在途，95占用
                fbsBudgetProcessDTO.setBillNo(entity.getBillCode());// 业务单据号
                fbsBudgetProcessDTO.setBillId(entity.getId());
                fbsBudgetProcessDTO.setItemCode(vo.getItemCode()[i]);// 科目代码
                fbsBudgetProcessDTO.setItemName(vo.getItemName()[i]);// 科目名称
                fbsBudgetProcessDTO.setProcessMoney(vo.getProcessMoney()[i]);// 执行金额
                fbsBudgetProcessDTO.setProcessDate(entity.getRepayDate());// 执行日期
                fbsBudgetProcessDTO.setCorpId(entity.getCorpId());// 单位id
                fbsBudgetProcessDTO.setCurId(curId);// 币别
                fbsBudgetProcessDTO.setBusinessName(BudgetConstant.BUSINESS_NAME_PROVIDE_REPAY);// 执行业务名称
                fbsBudgetProcessDTO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_CMS);// 执行系统名称
                fbsBudgetProcessDTO.setCreateBy(user.getLoginName());
                fbsBudgetProcessDTO.setCreateDate(DateUtils.getDate());
                if (vo.getExchangeRate() != null) {
                    fbsBudgetProcessDTO.setExchangeRate(vo.getExchangeRate()[i]);
                    fbsBudgetProcessDTO.setProcessCurMoney(vo.getProcessCurMoney()[i]);
                }
                list.add(fbsBudgetProcessDTO);
            }
            // 修改时，先删除之前的计划执行数据再保存
            this.fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_PROVIDE_REPAY,
                    BudgetConstant.PROCESS_SYSNAME_CMS, entity.getBillCode());
            this.fbsBudgetProcessService.writeBudgetProcess(list, user);
        }
    }
    /**
     * 功能介绍 : 跳过补录 系统自动补录生成数据<br>
     */
    public void autoRepair(CmsProvideLoanRepay repay, ICurrentUser user) {
        // CmsProvideLoanRepay repay = this.get(id);
        if (FinacialConstant.BOOLEAN_TRUE.equals(repay.getAcrossRepair())) {// 是否跳过补录功能
            // 记账完成处理
            repay.setStatus(CmsConstant.COMPELETOTAL);
            repay.setBizStatus(CmsConstant.BUSINESSSTATE_48);

            ConfigDTO config = configService.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS,BaseDataConstant.IS_BORROW_TALLY);
            String value = config.getValue();
            CorpDTO corp = corpServiceImpl.get(repay.getCorpId());
            if (CmsConstant.IS_TRUE.equals(value) && CorpConstant.CORP_TYPE_CODE_NET.equals(corp.getType())) {// 是否记账
                if (isAutoAccounting()) {// 是否自动记账
                    this.submit(repay, user);// 记账
                    repay.setStatus(CmsConstant.COMPELETOTAL);
                    repay.setBizStatus(CmsConstant.BUSINESSSTATE_42);
                } else {
                    repay.setStatus(CmsConstant.WAITTOTAL);
                    repay.setBizStatus(CmsConstant.BUSINESSSTATE_38);
                }
            }
            this.merge(repay);
            repayComplete(repay, user, 0);// 还款完处理
            // 修改资金计划为占用状态
            this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PROVIDE_REPAY,BudgetConstant.PROCESS_SYSNAME_CMS, repay.getBillCode(), BudgetConstant.STATUE_CHECK, user);
        } else {
            notificationService.send(repay.getId(), repay.getCorpId(), NotificationConstant.CMS.PROVIDE_REPAY_REPAIR,repay);
        }
    }
    public List<CmsProvideInterest> getRepayDateInterestsById(String interestId) {
        if (!EmptyUtils.isEmpty(interestId)) {
            CmsProvideInterest interest = cmsProvideInterestDao.get(interestId);
            if (interest != null) {
                return cmsProvideInterestDao.getRepayDateInterestsBySignAndSendCode(interest.getBillCode(),
                        interest.getSignRepayDate());
            }
        }
        return null;
    }
    /**
     * 功能介绍 : 补录打回<br>
     */
    public void repairBack(String id, String refuseWhy, ICurrentUser user) {
        CmsProvideLoanRepay repay = this.get(id);
        // TODO 打回说明 暂时没加这个字段
        repay.setStatus(CmsConstant.BACK);
        repay.setBizStatus(CmsConstant.BUSINESSSTATE_57);
        repay.setRefuseWhy(refuseWhy);
        this.saveOrUpdate(repay);
        this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PROVIDE_REPAY,
                BudgetConstant.PROCESS_SYSNAME_CMS, repay.getBillCode(), BudgetConstant.STATUE_BACK, user);
        notificationService.complete(NotificationConstant.CMS.PROVIDE_REPAY_REPAIR, id, user.getId());
        notificationService.send(repay.getId(), repay.getCorpId(), NotificationConstant.CMS.PROVIDE_REPAY, repay);// 添加录入申请的消息提示
        msService.insertProcessMsg(repay.getBillCode(), ProvideConstant.REPAY_BILL_CODE_PREFIX,
                CmsProcessMsgConstant.MSGA_REPAIRBACK, CmsProcessMsgConstant.MSGA_REPAY_REPAIR_BACK, user);
        msService.insertProcessMsg(repay.getSendCode(), ProvideConstant.REPAY_BILL_CODE_PREFIX,
                CmsProcessMsgConstant.MSGA_REPAIRBACK, CmsProcessMsgConstant.MSGA_REPAY_REPAIR_BACK, user);
    }
    /**
     * 功能介绍 : 补录暂存或提交还款<br>
     */
    public void repairTempOrSubmit(LoanRepayVO vo, String saveType, ICurrentUser user) {
        CmsProvideLoanRepay repay = new CmsProvideLoanRepay();
        BeanUtils.copyNotNullProperties(vo, repay);
        repairValidata(repay);
        if ("1".equals(saveType)) {// 提交
            repay.setStatus(CmsConstant.REPAIRAPPRAVE);
            repay.setBizStatus(CmsConstant.BUSINESSSTATE_51);
        } else {// 暂存
            repay.setBizStatus(CmsConstant.BUSINESSSTATE_50);
        }
        ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(repay.getSendCode());
        String curId = send.getCurCode();
        CmsProvideLoanRepay loan = this.get(repay.getId());
        BeanUtils.copyNotNullProperties(repay, loan);
        this.saveOrUpdate(loan);
        // 资金预算
        if (this.isBudget(repay.getCorpId(), BaseDataConstant.IS_BORROW_BUDGET,
                BaseDataConstant.PLOAN_REPAY_HANG_BUDGET)) {
            // 占用资金计划
            this.batchEngross(repay, saveType, curId, vo, user);
        }
        if ("1".equals(saveType)) {// 提交
            msService.insertProcessMsg(repay.getBillCode(), ProvideConstant.REPAY_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_REPAY_REPAIR_SUBMIT, user);
            msService.insertProcessMsg(repay.getSendCode(), ProvideConstant.REPAY_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_REPAY_REPAIR_SUBMIT, user);
             getStartHandler().handler(CmsWorkFlowConstant.CMS_PROVIDE_LOAN_REPAY_REPAIR, loan.getId(), loan,
                    loan.getCorpId(), user.getId());
        } else {
            msService.insertProcessMsg(repay.getBillCode(), ProvideConstant.REPAY_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_INPUT, CmsProcessMsgConstant.MSGA_REPAY_REPAIR_TEMP, user);
            msService.insertProcessMsg(repay.getSendCode(), ProvideConstant.REPAY_BILL_CODE_PREFIX,
                    CmsProcessMsgConstant.MSGA_INPUT, CmsProcessMsgConstant.MSGA_REPAY_REPAIR_TEMP, user);
        }
        notificationService.complete(NotificationConstant.CMS.PROVIDE_REPAY_REPAIR, repay.getId(), user.getId());
    }
    /**
     * 功能介绍 : 还款完成 修改send 与 info表状态<br>
     *
     * @param repay <b>日期：</b>2017-8-7<br>
     * @version 1.0
     * @author lvwl
     */
    public void repayComplete(CmsProvideLoanRepay repay, ICurrentUser user, int type) {
        cmsProvideLoanRepayDao.flush();
        UserDTO userDTO = userServiceImpl.getByLoginName(repay.getCreateBy());
        ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(repay.getSendCode());
        send = cmsProvideLoanSendService.get(send.getId());// 临时解决下hibernate缓存问题
        // 如果已还金额等于发放金额 修改发放表状态为业务完成
        BigDecimal sendBalance = this.completeSendBalance(repay.getSendCode(), "2");// 发放余额
        Integer repayPassageCount = this.getRepayAccrualCount(repay.getSendCode());// 未记账的利息
        if (sendBalance.compareTo(BigDecimal.ZERO) <= 0 && (repayPassageCount == null || repayPassageCount.intValue() == 0)) {// 发放金额等于还款总金额 没有还款在途金额
            // 判断业务已完成的 发放金额是否等于借款合同金额
            send.setBizStatus(CmsConstant.BUSINESSSTATE_48);
            send.setStatus(CmsConstant.FINISH);
            cmsProvideLoanSendService.updateEntity(send);
            String loanAcc = send.getLoanAcc();
            if (!EmptyUtils.isEmpty(loanAcc)) {
                // TODO
                innerAccountService.cancelFcAcc(loanAcc, repay.getRepayDate(), user);
            }
        }
        CmsProvideLoanInfo info = loanInfoService.getLoanInfoByBillCode(send.getLoanCode());
        BigDecimal repaySum = cmsProvideLoanRepayDao.getInfoRepayMoneySum(info.getBillCode());
        if (info.getPlMoney().compareTo(repaySum) == 0) {// 借款金额等于还款总金额
            info.setStatus(CmsConstant.FINISH);
            info.setBizStatus(CmsConstant.BUSINESSSTATE_110);
            loanInfoService.saveOrUpdate(info);
        }
        // 授信挂接
        CorpDTO corp = corpServiceImpl.get(repay.getCorpId());
        ConfigDTO config = configService.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS,
                BaseDataConstant.IS_BORROW_TALLY);// 是否记账
        String value = config.getValue();
        if (CmsConstant.IS_TRUE.equals(value) && CorpConstant.CORP_TYPE_CODE_NET.equals(corp.getType())) {// 是否记账
            if (isAutoAccounting()) {// 是否自动记账
                if (!EmptyUtils.isEmpty(info.getRatmodel()) && CmsConstant.IS_TRUE.equals(info.getRatmodel())) {
                    // 授信还款，释放还款的授信额度，如果是还利息则不释放授信额度
                    BigDecimal repayMoney = repay.getRepayMoney();// 还款金额
                    if (repayMoney.compareTo(BigDecimal.ZERO) > 0) {
                        RatUrationDTO urationDTO = new RatUrationDTO(ConfigConstant.CMS, info.getId());
                        urationDTO.setMoney(repayMoney);
                        urationDTO.setBailRate(info.getBailmoneyRate());// 保证金比例
                        if (info.getPlMoney().compareTo(repaySum) == 0) {// 借款金额等于还款总金额
                            urationDTO.setAllRelease(true);// 标识为全部释放
                        }
                        urationService.releaseRation(urationDTO);
                    }
                }
                handleContract(repay, userDTO);// 合同履约处理
                if (info.getPlMoney().compareTo(repaySum) == 0) {// 借款金额等于还款总金额
                    // 担保处理 0暂存，1在审使用，2使用中，3释放,4打回,5发放打回
                    ProvideUtils.handleGua(info, null, guaContractOccupServie, 3, null);
                    this.endContract(info, repay, userDTO);
                    // 释放抵质押物占用额度
                    if (EmptyUtils.isNotEmpty(info.getIsRelatePledge())
                            && info.getIsRelatePledge().equals(CmsConstant.IS_TRUE)) {
                        pledgeOccupyService.releasePledgeOccupy(info.getId(), info.getBillCode());
                    }
                }
            } else {// 手工记账并且是线上还款
                if (repay.getOnlineSign().equals(CmsConstant.ONLINE_TRUE)) {
                    handleContract(repay, userDTO);// 合同履约处理
                    if (info.getPlMoney().compareTo(repaySum) == 0) {// 借款金额等于还款总金额
                        this.endContract(info, repay, userDTO);
                    }
                    // 释放抵质押物占用额度
                    if (EmptyUtils.isNotEmpty(info.getIsRelatePledge())
                            && info.getIsRelatePledge().equals(CmsConstant.IS_TRUE)) {
                        pledgeOccupyService.releasePledgeOccupy(info.getId(), info.getBillCode());
                    }
                }
            }
        } else {// 不记账
            if (!EmptyUtils.isEmpty(info.getRatmodel()) && CmsConstant.IS_TRUE.equals(info.getRatmodel())) {
                // 授信还款，释放还款的授信额度，如果是还利息则不释放授信额度
                BigDecimal repayMoney = repay.getRepayMoney();// 还款金额
                if (repayMoney.compareTo(BigDecimal.ZERO) > 0) {
                    RatUrationDTO urationDTO = new RatUrationDTO(ConfigConstant.CMS, info.getId());
                    urationDTO.setMoney(repayMoney);
                    urationDTO.setBailRate(info.getBailmoneyRate());// 保证金比例
                    if (info.getPlMoney().compareTo(repaySum) == 0) {// 借款金额等于还款总金额
                        urationDTO.setAllRelease(true);// 标识为全部释放
                    }
                    urationService.releaseRation(urationDTO);
                }
            }
            handleContract(repay, userDTO);// 合同履约处理
            if (info.getPlMoney().compareTo(repaySum) == 0) {// 借款金额等于还款总金额
                // 担保处理 0暂存，1在审使用，2使用中，3释放,4打回,5发放打回
                ProvideUtils.handleGua(info, null, guaContractOccupServie, 3, null);
                // 终止合同
                this.endContract(info, repay, userDTO);
                // 释放抵质押物占用额度
                if (EmptyUtils.isNotEmpty(info.getIsRelatePledge())
                        && info.getIsRelatePledge().equals(CmsConstant.IS_TRUE)) {
                    pledgeOccupyService.releasePledgeOccupy(info.getId(), info.getBillCode());
                }
            }
        }
        // 以下代码用于释放项目占用的保证金额度
        if (sendBalance.compareTo(BigDecimal.ZERO) <= 0
                && (repayPassageCount == null || repayPassageCount.intValue() == 0)) {// true:已全部还款
            // 通过项目id得到被占用的保证金信息
            FcBondApplyDTO dto = fcBondHookService.getFcBondApplyByItemIds(info.getId(), info.getBillCode());
            if (dto != null) {// 不为null,则说明挂接了保证金,全部还款完成后,修改保证金的占用信息状态为"已释放",同时更新相关的数据信息
                dto.setStatus(FcBondConstant.STATUE_RELEASE);// 更改状态为已释放
                dto.setUpdateBy(user.getLoginName());// 更新修改人
                dto.setUpdateDate(DateUtils.getDate());// 更新修改时间
                fcBondHookService.updateFcBondApply(dto);// 调用接口方法,更新保证金占用信息
            }
        }
        // 利息明细数据处理
        this.createInterest(repay, send);
    }
    /**
     * 功能介绍 : 查询发放剩余额度（发放金额 - 已还金额）<br>
     *
     * @param sendBillCode 发放编号
     * @param passageSign  在途标识 1查询在途金额 2已还 其他为 两个之和 <b>日期：</b>2017-8-7<br>
     * @version 1.0
     * @author lvwl
     */
    public BigDecimal completeSendBalance(String sendBillCode, String passageSign) {
        BigDecimal repayMoney = this.getRepayMoneyBySendBillCode(sendBillCode, passageSign);
        ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(sendBillCode);
        return send.getMoney().subtract(repayMoney);
    }
    /**
     * 功能介绍 : 查询还款金额<br>
     *
     * @param sendBillCode 发放编号
     * @param passageSign  在途标识 1查询在途金额 2已还 其他为 无状态查询
     */
    public BigDecimal getRepayMoneyBySendBillCode(String sendBillCode, String passageSign) {
        return cmsProvideLoanRepayDao.getRepayMoneyBySendBillCode(sendBillCode, passageSign);
    }
    /**
     * 功能介绍 : 查询还款金额<br>
     *
     * @param sendBillCode 发放编号
     * @param passageSign  在途标识 1查询在途金额 2已还 其他为 无状态查询
     */
    public List getRepayListBySendBillCode(String sendBillCode, String passageSign) {
        List<LoanRepayVO> list = cmsProvideLoanRepayDao.getRepayListBySendBillCode(sendBillCode, passageSign);
        Map<String, DictDTO> onlines = iDictService.listMap("cms.leasepay.online.sign");
        Map<String, DictDTO> bizStatus = iDictService.listMap("cms.bizStatus");
        for (LoanRepayVO vo : list){
            if (ObjectUtil.isNotEmpty(vo.getOnlineSign()) && onlines.containsKey(vo.getOnlineSign())){
                vo.setOnlineSignName(onlines.get(vo.getOnlineSign()).getName());
            }
            if (ObjectUtil.isNotEmpty(vo.getBizStatus()) && bizStatus.containsKey(vo.getBizStatus())){
                vo.setBizStatusName(bizStatus.get(vo.getBizStatus()).getName());
            }
        }
        return list;
    }
    /**
     * 在线显示查询还款信息
     *
     * @param id
     * @return
     */
    public LoanRepayVO queryRepayVOById(String id) {
        return cmsProvideLoanRepayDao.queryRepayVOById(id);
    }
    /**
     * 删除对象
     *
     * @param entity
     */
    public void deleteEntity(CmsProvideLoanRepay entity) {
        super.deleteEntity(entity);
    }
    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(String id, ICurrentUser user) {
        CmsProvideLoanRepay repay = this.get(id);
        if (StringUtils.isNotEmpty(repay.getProvideInterestId()) && !CommonConstant.YES.equals(repay.getIsAdvancedRepay()) ) {
            CmsProvideInterest interest = cmsProvideInterestDao.get(repay.getProvideInterestId());
            //还原还款计划状态
            if (StringUtils.isNotEmpty(interest.getRepaymentScheduleId())) {
                RepaymentSchedule schedule = repaymentScheduleDao.get(interest.getRepaymentScheduleId());
//                List<CmsProvideInterest> list = cmsProvideInterestDao.queryInterestListByScheduleId(interest.getRepaymentScheduleId());
//                if (!EmptyUtils.isEmptyList(list)) {
//                    if (list.size() < 2) {
                schedule.setIsRepay("0");//未还
                repaymentScheduleDao.saveOrUpdate(schedule);
            }
            // 还原利息明细状态
            List<String> interestList = cmsProvideInterestDao.getRepayDateInterestsSignById(interest.getBillCode(),
                    interest.getSignRepayDate());
            if (!EmptyUtils.isEmptyList(interestList)) {
                for (String interestId : interestList) {
                    CmsProvideInterest inte = cmsProvideInterestDao.get(interestId);
                    interest.setStatus(0);// 未还
                    cmsProvideInterestDao.saveOrUpdate(inte);
                }
            }
        }
        // 释放预算信息
        this.fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_PROVIDE_REPAY,
                BudgetConstant.PROCESS_SYSNAME_CMS, repay.getBillCode());
        // this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PROVIDE_REPAY
        // ,BudgetConstant.PROCESS_SYSNAME_CMS, repay.getBillCode(), BudgetConstant.STATUE_NULL, user);
        // 逻辑删除还款数据
        repay.setStatus(CmsConstant.DELETED);
        repay.setBizStatus(CmsConstant.BUSINESSSTATE_2_);// 显示状态删除
        this.update(repay, user);
        msService.insertProcessMsg(repay.getBillCode(), ProvideConstant.REPAY_BILL_CODE_PREFIX,
                CmsProcessMsgConstant.MSGA_DELETE, CmsProcessMsgConstant.MSGA_REPAY_DELETE, user);
        notificationService.complete(NotificationConstant.CMS.PROVIDE_REPAY, repay.getId(), user.getId());
    }
    /**
     * 保存
     *
     * @param entity
     */
    public void save(CmsProvideLoanRepay entity, ICurrentUser user) {
        entity.setCreateName(user.getUsername());
        entity.setCreateDate(new Date());
        entity.setCreateBy(user.getLoginName());
        super.saveEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void update(CmsProvideLoanRepay entity, ICurrentUser user) {
        entity.setUpdateName(user.getUsername());
        entity.setUpdateDate(new Date());
        entity.setUpdateBy(user.getLoginName());
        super.updateEntity(entity);
    }
    /**
     * 更新
     *
     * @param entity
     */
    public void updateRepay(CmsProvideLoanRepay entity) {
        super.updateEntity(entity);
    }
    /**
     * 保存or更新
     *
     * @param entity
     */
    public void saveOrUpdate(CmsProvideLoanRepay entity, ICurrentUser user) {
        if (EmptyUtils.isEmpty(entity.getId())) {
            entity.setCreateName(user.getUsername());
            entity.setCreateDate(new Date());
            entity.setCreateBy(user.getLoginName());
        } else {
            entity.setUpdateName(user.getUsername());
            entity.setUpdateDate(new Date());
            entity.setUpdateBy(user.getLoginName());
        }
        super.saveOrUpdate(entity);
    }
    /**
     * 线上还款撤回
     *
     * @param id
     */
    public void onlineWithdraw(String id, ICurrentUser user) {
        CmsProvideLoanRepay repay = this.get(id);
        repay.setOnlineStatus(0);// 撤回
        this.invalidVoucher(repay);// 账务作废
        repay.setVoucherNo(null);
        repay.setOrderNo(null);
        repay.setOnlineOtherAccCharge(null);
        repay.setBizStatus(CmsConstant.BUSINESSSTATE_47);
        repay.setTallySign(null);
        this.saveOrUpdate(repay);
        nisAllocationReqService.updateNisAllocationReq(id, NetbankConstant.STATUE_DEL, "还款跟踪撤回", user);
    }
    /**
     * 线上还款删除
     *
     * @param id
     */
    public void onlineDel(String id, ICurrentUser user) {
        // this.invalidVoucher(id);
        this.delete(id, user);
        nisAllocationReqService.deleteNisAllocationReq(id);
    }
    /**
     * 线上还款确认以扣款
     *
     * @param id
     * @param user
     */
    public void onlineDeductOk(String id, ICurrentUser user) {
        CmsProvideLoanRepay repay = this.get(id);
        CorpDTO corp = corpServiceImpl.get(repay.getCorpId());
        ConfigDTO config = configService.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS,
                BaseDataConstant.IS_BORROW_TALLY);
        String value = config.getValue();
        if (CmsConstant.IS_TRUE.equals(value) && CorpConstant.CORP_TYPE_CODE_NET.equals(corp.getType())) {// 是否记账
            // if(isAutoAccounting()){//是否自动记账
            this.onlineOKSubmit(repay, user);// 记账
            repay.setStatus(CmsConstant.COMPELETOTAL);
            repay.setBizStatus(CmsConstant.BUSINESSSTATE_48);
            // }else{
            // repay.setStatus(CmsConstant.WAITTOTAL);
            // repay.setBizStatus(CmsConstant.BUSINESSSTATE_38);
            // repay.setTallySign(NbconsoleConstant.NbcEstate.E_STATE_CHECK.getValue());
            // }
        } else {
            repay.setStatus(CmsConstant.COMPELETOTAL);
            repay.setBizStatus(CmsConstant.BUSINESSSTATE_48);
        }
        this.saveOrUpdate(repay);
        repayComplete(repay, user, 1);// 还款完处理
    }
    /**
     * 查询还利息 还款单据的数据 <102 未记账的 sendCode发放单据
     */
    public Integer getRepayAccrualCount(String sendCode) {
        Integer count = cmsProvideLoanRepayDao.getRepayAccrualCount(sendCode);
        return count;
    }
    @Override
    public BaseDao<CmsProvideLoanRepay, String> getEntityDao() {
        return cmsProvideLoanRepayDao;
    }
    /**
     * 是否挂机预算系统
     *
     * @param corpId   单位ID
     * @param briefId1 业务是否挂预算
     * @param briefId2 业务挂预算科目
     * @return
     */
    public boolean isBudget(String corpId, String briefId1, String briefId2) {
        return StringUtils.isNotEmpty(corpId) && fbsBudgetProcessService.corpCtlBudget(corpId)
                && isBudget(briefId1, briefId2);
    }
    /**
     * 是否挂机预算系统
     *
     * @param briefId1 业务是否挂预算
     * @param briefId2 业务挂预算科目
     * @return
     */
    public boolean isBudget(String briefId1, String briefId2) {
        if (configService.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, briefId1).toBoolean()) {
            String budgetItem = configService.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, briefId2).toString();
            return StringUtils.isNotEmpty(budgetItem);
        }
        return false;
    }
    /**
     * 生成还款单号
     */
    private String getLoanRepayBillCode() {
        return SequenceSingleton.getSequence(SequenceEnum.CMS_PROVIDE_LOANREPAY_BILL_NO,
                ProvideConstant.REPAY_BILL_CODE_PREFIX);
    }
    public LoanRepayVO queryRepayVOByBillCode(String billCode, String onlineSign) {
        return cmsProvideLoanRepayDao.queryRepayVOByBillCode(billCode, onlineSign);
    }
    /****************** 数据验证 *******************/
    /**
     * 还款申请验证
     */
    private void validata(CmsProvideLoanRepay repay) {
        if (repay.getRepayDate() == null) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANINFO_REPAYDATEEMPTY);
        }
        if (EmptyUtils.isEmpty(repay.getRepayMoney())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANREPAY_REPAYMONEYEMPTY);
        }
        if (EmptyUtils.isEmpty(repay.getRmk())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANREPAY_RMKEMPTY);
        }
        // 还款金额不能超过 待发放金额
        BigDecimal sendBalance = completeSendBalance(repay.getSendCode(), null);
        CmsProvideLoanRepay oldRepay = null;
        if (!EmptyUtils.isEmpty(repay.getId())) {
            oldRepay = get(repay.getId());
            sendBalance = sendBalance.add(oldRepay.getRepayMoney());
        }
        if (repay.getRepayMoney().compareTo(sendBalance) == 1) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANREPAY_REPAYMONEYLEPLBALANCE);
        }
        // 线上还款的必填项 验证
        if (CmsConstant.ONLINE_TRUE.equals(repay.getOnlineSign())) {
            if (EmptyUtils.isEmpty(repay.getOnlineRealityInterest())) {
                throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANREPAY_ONLINEREALITYINTERESTEMPTY);
            }
            if (EmptyUtils.isEmpty(repay.getOnlineMoney())) {
                throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANREPAY_ONLINEMONEYEMPTY);
            }
            if (EmptyUtils.isEmpty(repay.getOnlinePaymentAcc())) {
                throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANREPAY_ONLINEPAYMENTACCEMPTY);
            }
            if (EmptyUtils.isEmpty(repay.getOnlineAccName())) {
                throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANREPAY_ONLINEACCNAMEEMPTY);
            }
            if (EmptyUtils.isEmpty(repay.getOnlineBankName())) {
                throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANREPAY_ONLINEBANKNAMEEMPTY);
            }
            /** 线上还款时验证状态是否能提交 */
            if (!EmptyUtils.isEmpty(repay.getId()) && CmsConstant.ONLINE_TRUE.equals(repay.getOnlineSign())) {
                NisAllocationReqDTO dto = nisAllocationReqService.getNisAllocationReqByErpId(repay.getId());
                if (dto != null && !AllocationConstant.STATUE_DEL.equals(dto.getStatus())) {
                    throw new ParameterException(AllocationMessageEnum.NIS_ALLOCATION_MESSAGE_STATUS_DELETE);
                }
            }
            if (!EmptyUtils.isEmpty(repay.getId())) {
                if (CmsConstant.ONLINE_TRUE.equals(oldRepay.getOnlineSign())
                        && CmsConstant.ONLINE_FALSE.equals(repay.getOnlineSign())) {
                    if (CmsConstant.FLOWWAIT != oldRepay.getStatus().intValue()) {
                        throw new ParameterException(AllocationMessageEnum.NIS_ALLOCATION_MESSAGE_STATUS_DELETE);
                    }
                }
            }
        }
    }
    /**
     * 还款补录验证
     */
    private void repairValidata(CmsProvideLoanRepay repay) {
        validata(repay);
        if (EmptyUtils.isEmpty(repay.getOnlineRealityInterest())) {
            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANREPAY_REPAYACCRUALEMPTY);
        }
    }
    protected void createInsAllocationReq(CmsProvideLoanRepay repay, ICurrentUser user) {
        NisAllocationReqDTO dto = nisAllocationReqService.getNisAllocationReqByErpId(repay.getId());
        if (EmptyUtils.isEmpty(dto) || EmptyUtils.isEmpty(dto.getId())) {
            dto = new NisAllocationReqDTO();
        }
        ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(repay.getSendCode());
        dto.setErpId(repay.getId());
        dto.setStatus(AllocationConstant.STATUE_TEMP);
        dto.setBillFrom(AllocationConstant.BILL_FROM_2);
        dto.setType(AllocationConstant.ALLOCATION_TYPE_POS);// 头寸调拨
        dto.setIsQuick("0");// 不加急
        dto.setCurId(send.getCurCode());// 币别ID
        dto.setCorpId(repay.getCorpId());
        dto.setPayeeAccId(repay.getOnlinePaymentAccId());
        dto.setPayeeAcc(repay.getOnlinePaymentAcc());
        dto.setPayeeAccName(repay.getOnlineAccName());
        dto.setMoney(repay.getOnlineMoney());
        dto.setPurpose(repay.getOnlinePurpose());
        // 获取还款单据的创建用户或修改用户信息
        if (EmptyUtils.isBlank(repay.getUpdateBy())) {
            user = userServiceImpl.getByLoginName(repay.getCreateBy());
        } else {
            user = userServiceImpl.getByLoginName(repay.getUpdateBy());
        }
        if (!EmptyUtils.isEmpty(dto) && !EmptyUtils.isEmpty(dto.getId())) {
            nisAllocationReqService.updateNisAllocationReq(dto, user);
        } else {
            nisAllocationReqService.saveNisAllocationReq(dto, user);
        }
        NisAllocationReqDTO resultDto = nisAllocationReqService.getNisAllocationReqByErpId(repay.getId());
        if (null != resultDto) {
            repay.setReqBillNo(resultDto.getBillNo());
            repay.setReqStatus(resultDto.getStatus());
        }
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

        CmsProvideLoanRepay repay = this.get(businessKey,true);
        if (CmsWorkFlowConstant.CMS_PROVIDE_LOAN_REPAY.equals(processDefinitionKey)) {// 还款申请审批
            if (result) {// 审批通过
                msService.insertProcessMsg(repay.getBillCode(), ProvideConstant.REPAY_BILL_CODE_PREFIX,CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_REPAY_CHECKOK, user);
                msService.insertProcessMsg(repay.getSendCode(), ProvideConstant.REPAY_BILL_CODE_PREFIX,CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_REPAY_CHECKOK, user);
                //判断是否提前还款，如果是提前还款则判断是否有试算。
                if (repay.getIsAdvancedRepay().equals(CommonConstant.YES)) {
                    repayAdvancedRepayHandle(repay);
                }
                if (CmsConstant.ONLINE_TRUE.equals(repay.getOnlineSign())) {// 线上还款审批通过
                    repay.setStatus(CmsConstant.COMPELETOTAL);// 记账处理
                    repay.setBizStatus(CmsConstant.BUSINESSSTATE_48);
                    CorpDTO corp = corpServiceImpl.get(repay.getCorpId());
                    ConfigDTO config = configService.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS,BaseDataConstant.IS_BORROW_TALLY);
                    String value = config.getValue();
                    if (CmsConstant.IS_TRUE.equals(value) && CorpConstant.CORP_TYPE_CODE_NET.equals(corp.getType())) {// 是否记账
                        if (isAutoAccounting()) {// 是否自动记账
                            /** 线上还款记账 */
                            this.onlineSubmit(repay, user);
                            repay.setStatus(CmsConstant.FLOWWAIT);// 使用流程等待状态
                            repay.setBizStatus(CmsConstant.BUSINESSSTATE_58);// 还款待确认扣款
                            /** todo:数据推送到头寸调拨 */
                            this.createInsAllocationReq(repay, user);
                        } else {
                            repay.setStatus(CmsConstant.WAITTOTAL);
                            repay.setBizStatus(CmsConstant.BUSINESSSTATE_38);
                        }
                    } else {
                        repay.setStatus(CmsConstant.FLOWWAIT);// 使用流程等待状态
                        repay.setBizStatus(CmsConstant.BUSINESSSTATE_58);// 还款待确认扣款
                        /** todo:数据推送到头寸调拨 */
                        this.createInsAllocationReq(repay, user);
                    }
                    this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PROVIDE_REPAY,
                            BudgetConstant.PROCESS_SYSNAME_CMS, repay.getBillCode(), BudgetConstant.STATUE_CHECK, user);
                } else {// 线下还款
                    repay.setStatus(CmsConstant.APPROVED);
                    repay.setBizStatus(CmsConstant.BUSINESSSTATE_52);
                    autoRepair(repay, user);// 跳过补录
                }
            } else {// 打回
                repay.setStatus(CmsConstant.BACK);
                repay.setBizStatus(CmsConstant.BUSINESSSTATE_49);
                repay.setOnlineStatus(null);
                msService.insertProcessMsg(repay.getBillCode(), ProvideConstant.REPAY_BILL_CODE_PREFIX,CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_REPAY_CHECKBACK, user);
                msService.insertProcessMsg(repay.getSendCode(), ProvideConstant.REPAY_BILL_CODE_PREFIX,CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_REPAY_CHECKBACK, user);
                notificationService.send(repay.getId(), repay.getCorpId(), NotificationConstant.CMS.PROVIDE_REPAY,repay);// 添加发放申请的消息提示
                this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PROVIDE_REPAY,BudgetConstant.PROCESS_SYSNAME_CMS, repay.getBillCode(), BudgetConstant.STATUE_BACK, user);
            }
            this.merge(repay);
        }
        if (CmsWorkFlowConstant.CMS_PROVIDE_LOAN_REPAY_REPAIR.equals(processDefinitionKey)) {// 还款补录审批
            if (result) {// 审批通过
                // repay.setStatus(CmsConstant.WAITTOTAL);
                repay.setStatus(CmsConstant.COMPELETOTAL);// 记账处理
                repay.setBizStatus(CmsConstant.BUSINESSSTATE_48);
                ConfigDTO config = configService.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS,BaseDataConstant.IS_BORROW_TALLY);
                String value = config.getValue();
                CorpDTO corp = corpServiceImpl.get(repay.getCorpId());
                // 日志
                msService.insertProcessMsg(repay.getBillCode(), ProvideConstant.REPAY_BILL_CODE_PREFIX,CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_REPAY_REPAIR_CHECKOK, user);
                msService.insertProcessMsg(repay.getSendCode(), ProvideConstant.REPAY_BILL_CODE_PREFIX,CmsProcessMsgConstant.MSGA_CHECKOK, CmsProcessMsgConstant.MSGA_REPAY_REPAIR_CHECKOK, user);
                if (CmsConstant.IS_TRUE.equals(value) && CorpConstant.CORP_TYPE_CODE_NET.equals(corp.getType())) {
                    if (isAutoAccounting()) {// 是否自动记账
                        this.submit(repay, user);// 记账
                        repay.setStatus(CmsConstant.COMPELETOTAL);// 记账处理
                        repay.setBizStatus(CmsConstant.BUSINESSSTATE_48);
                    } else {
                        repay.setStatus(CmsConstant.WAITTOTAL);
                        repay.setBizStatus(CmsConstant.BUSINESSSTATE_38);
                    }
                }
                this.saveOrUpdate(repay);
                //反写预计明细实付利息
                if(EmptyUtils.isNotEmpty(repay.getProvideInterestId())){
                    CmsProvideInterest inte = cmsProvideInterestDao.get(repay.getProvideInterestId());
                    inte.setRealInterest(repay.getOnlineRealityInterest());
                    cmsProvideInterestDao.saveOrUpdate(inte);
                }
                repayComplete(repay, user, 0);// 还款完处理
                // 修改资金计划为占用状态
                this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PROVIDE_REPAY,BudgetConstant.PROCESS_SYSNAME_CMS, repay.getBillCode(), BudgetConstant.STATUE_CHECK, user);
            } else {// 打回
                repay.setStatus(CmsConstant.APPROVED);// 重新补录
                repay.setBizStatus(CmsConstant.BUSINESSSTATE_56);
                this.saveOrUpdate(repay);
                msService.insertProcessMsg(repay.getBillCode(), ProvideConstant.REPAY_BILL_CODE_PREFIX,CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_REPAY_REPAIR_CHECKBACK, user);
                msService.insertProcessMsg(repay.getSendCode(), ProvideConstant.REPAY_BILL_CODE_PREFIX,CmsProcessMsgConstant.MSGA_ROLLBACK, CmsProcessMsgConstant.MSGA_REPAY_REPAIR_CHECKBACK, user);
                notificationService.send(repay.getId(), repay.getCorpId(),NotificationConstant.CMS.PROVIDE_REPAY_REPAIR, repay);
                this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PROVIDE_REPAY,BudgetConstant.PROCESS_SYSNAME_CMS, repay.getBillCode(), BudgetConstant.STATUE_BACK, user);
            }
        }
    }

    /**
     * 如果提前还款则进行还款计划信息明细表、预计利息明细数据处理
     *
     * @param repay 还款信息
     */
    private void repayAdvancedRepayHandle(CmsProvideLoanRepay repay) {
        ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(repay.getSendCode());
        List<CmsProvideInterestTemp> interestTempsList = tempService.getListByBillCode(send.getBillCode());
        if (EmptyUtils.isNotEmpty(interestTempsList)) {
            cmsProvideInterestService.deleteBySendICode(send.getBillCode());
            CmsProvideInterest cmsProvideInterest;
            List<CmsProvideInterest> cmsProvideInterests = new ArrayList<>();
            for (CmsProvideInterestTemp entity : interestTempsList) {
                cmsProvideInterest = new CmsProvideInterest();
                BeanUtils.copyNotNullProperties(entity, cmsProvideInterest);
                //实付利息回写
                if(cmsProvideInterest.getRepayDate().compareTo(repay.getRepayDate())==0){
                    cmsProvideInterest.setRealInterest(repay.getOnlineRealityInterest());
                    cmsProvideInterest.setStatus(2);//在途还款
                    repay.setProvideInterestId(cmsProvideInterest.getId());
                }
                cmsProvideInterests.add(cmsProvideInterest);
            }
            cmsProvideInterestService.batchSave(cmsProvideInterests);
        }
        List<ScheduleDtlTemp> scheduleDtlTempList = dtlTempService.getListByBillCode(send.getId());
        if (EmptyUtils.isNotEmpty(scheduleDtlTempList)) {
            repaymentScheduleDao.deleteByBillCode(send.getId());
            RepaymentSchedule repaymentSchedule;
            List<RepaymentSchedule> cmsProvideInterests = new ArrayList<>();
            for (ScheduleDtlTemp entity : scheduleDtlTempList) {
                repaymentSchedule = new RepaymentSchedule();
                BeanUtils.copyNotNullProperties(entity, repaymentSchedule);
                cmsProvideInterests.add(repaymentSchedule);
            }
            repaymentScheduleDao.batchSave(cmsProvideInterests);
        }
    }
    /***************** 审批END ********************/
    /**
     * 本金和利息都是随借随还时则自动生成数据
     *
     * @param repay
     * @param loanSend
     */
    public void createInterest(CmsProvideLoanRepay repay, ProvideLoanSend loanSend) {
        RepayTypeVO repayTypeVO=repayTypeService.getRepayTypeInfoById(loanSend.getRepayType());
        if(!EmptyUtils.isEmpty(repayTypeVO)) {
            //历史数据兼容性，之前还款方式查不到

//            本金或利息随借随还，并且还款利息id为空，则反向生成利息数据
            if ((BaseDataConstant.FREQUENCY_TYPE_N.equals(repayTypeVO.getCapitalFrequencyType()) ||
                    BaseDataConstant.FREQUENCY_TYPE_N.equals(repayTypeVO.getInterestFrequencyType())) && StringUtils.isEmpty(repay.getProvideInterestId())) {
                CmsProvideInterest interest = new CmsProvideInterest();
//                还款日期
                interest.setRepayDate(repay.getRepayDate());
//                还款金额
                interest.setRepayMoney(repay.getRepayMoney());
//                还款利息
                interest.setPayInterest(EmptyUtils.isEmpty(repay.getRepayAccrual()) ? repay.getOnlineRealityInterest() : repay.getRepayAccrual());
                BigDecimal sendBalance = this.completeSendBalance(repay.getSendCode(), "2");
//                本金
                interest.setLoanMoney(sendBalance.add(repay.getRepayMoney()));
//                剩余本金
                interest.setLeftMoney(sendBalance);
//                实付利息
                interest.setRealInterest(repay.getOnlineRealityInterest());
                interest.setSignRepayDate(repay.getRepayDate());
//                结息
                interest.setInterestSign(CommonConstant.YES);
//                设置已还
                if (repay.getStatus().equals(CmsConstant.COMPELETOTAL)) {
                    interest.setStatus(1);//已还
                } else {
                    interest.setStatus(2);//在途
                }
                interest.setCorpId(loanSend.getCorpId());
                interest.setCurId(loanSend.getCurCode());
                interest.setBillCode(loanSend.getBillCode());
                cmsProvideInterestDao.save(interest);
                repay.setProvideInterestId(interest.getId());
                //更新还款表对应的计划预计明细id
                this.merge(repay);
            }else {

                //获取还款利息
                if (EmptyUtils.isNotEmpty(repay.getProvideInterestId())) {
                    CmsProvideInterest interest = cmsProvideInterestDao.get(repay.getProvideInterestId(), true);
                    if (ObjectUtil.isNotEmpty(interest)) {
                        BigDecimal repayInterest = repay.getOnlineRealityInterest();
                        if (ObjectUtil.isNotEmpty(repayTypeVO)) {
//                            本金随借随还，反写本金数据
                            if (BaseDataConstant.FREQUENCY_TYPE_N.equals(repayTypeVO.getCapitalFrequencyType())) {
                                interest.setRepayMoney(repay.getRepayMoney());
                            }
//                            利息随借随还，反写利息数据
                            if (BaseDataConstant.FREQUENCY_TYPE_N.equals(repayTypeVO.getInterestFrequencyType())) {
                                interest.setPayInterest(repayInterest);
                            }
                        }
                        interest.setRealInterest(repayInterest);//在还款申请是就已经保存了 归还利息
//                            反写还款日期
                        interest.setSignRepayDate(repay.getRepayDate());
                        if (repay.getStatus().equals(CmsConstant.COMPELETOTAL)) {
                            interest.setStatus(1);//已还
                        }
                        cmsProvideInterestDao.saveOrUpdate(interest);
                    }
                }
            }
        }
    }
    /***************** 线下还款记账BEGIN **********************/
    /**
     * 线下还款记账
     */
    public void submit(CmsProvideLoanRepay repay, ICurrentUser user) {
        ConfigDTO config = configService.getConfig("cms", "is_borrow_tally");
        String value = config.getValue();
        CorpDTO corp = corpServiceImpl.get(repay.getCorpId());
        if (CmsConstant.IS_TRUE.equals(value) && CorpConstant.CORP_TYPE_CODE_NET.equals(corp.getType())) {
            // 构建凭证传输对象
            FcAccbookVoucherDTO accbook = processAccbook(repay, user);
            // 记账提交 TODO
            VoucherWrapperDTO voucher = cmsPrecativeLoanSendService.submit(accbook, BusOriType.NB_BANK_REPAYMENT.get(),
                    user);
            // 设置记账返回数据
            FcAccbookDTO acc = voucher.getTarget().get(0).getAccbook();
            // List<FcAccbookDtlDTO> bookDtls = voucher.getTarget().get(0).getBookDtls();
            // 记账流水号
            repay.setOrderNo(acc.getOrderNo());
            // 记账凭证号
            repay.setVoucherNo(acc.getVoucherNo());
        }
    }
    /**
     * 功能：创建凭证信息
     */
    public FcAccbookVoucherDTO processAccbook(CmsProvideLoanRepay repay, ICurrentUser user) {
        FcAccbookVoucherDTO entity = new FcAccbookVoucherDTO();
        FcBusinessRuleDefineDTO ruleDefine = cmsPrecativeLoanSendService
                .getBusinessRuleExp(SysRaceCode.NB_BANK_REPAYMENT.get());
        entity = processTallyAccbook(repay, ruleDefine, user);
        return entity;
    }
    /**
     * 设置凭证信息
     */
    public FcAccbookVoucherDTO processTallyAccbook(CmsProvideLoanRepay repay, FcBusinessRuleDefineDTO ruleDefine,
                                                   ICurrentUser user) {
        FcAccbookVoucherDTO entity = new FcAccbookVoucherDTO();
        FcAccbookDTO accBook = new FcAccbookDTO();
        entity.setAccbook(accBook);
        List<FcAccbookDtlDTO> accbookDtls = null;
        accbookDtls = getAccbookDtls(ruleDefine, repay, user);
        /*
         * CorpDTO corp = corpServiceImpl.get(send.getCorpId()); String netId = "";
         * if(EmptyUtils.isEmpty(corp.getNetId())){//顶级网点 没有netid netId = corp.getId(); }else{ netId = corp.getNetId();
         * }
         */
        // TODO
        accBook.setBusRaceId(cmsPrecativeLoanSendService.getFcBusRaceId(repay.getCorpId(),
                SysRaceCode.NB_BANK_REPAYMENT, user == null ? " " : user.getId()));
        accBook.setVoucherType(ruleDefine.getVoucherType());
        accBook.setTallyDate(repay.getRepayDate());
        accBook.setInterestStartDate(repay.getRepayDate());
        accBook.setEmergencySign(FinacialConstant.EMERGENCYSIGN_G);
        entity.setAccbook(accBook);
        entity.setBookDtls(accbookDtls);
        return entity;
    }
    /**
     * 功能：记账时的会计分录构造
     */
    public List<FcAccbookDtlDTO> getAccbookDtls(FcBusinessRuleDefineDTO ruleDefine, CmsProvideLoanRepay repay,
                                                ICurrentUser user) {
        List<FcVoucherEntryDefineDTO> entrys = ruleDefine.getVouchers();
        FcAccbookDtlDTO accbookDtl = null;
        cmsPrecativeLoanSendService.collectionSort(entrys);
        List<FcAccbookDtlDTO> accbookDtls = new ArrayList<FcAccbookDtlDTO>();
        int balanceWay;
        ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(repay.getSendCode());
        CmsProvideLoanInfo info = loanInfoService.getLoanInfoByBillCode(send.getLoanCode());
        String loanAcc = send.getLoanAcc();// 借款账号
        FcAccDTO fcAccDTO = innerAccountService.getByBlnctrAcc(loanAcc);
        for (FcVoucherEntryDefineDTO entry : entrys) {
            if (entry.getGroupNum() == null) {
                throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_GROUPNUM_IS_NULL);
            }
            accbookDtl = new FcAccbookDtlDTO();
            accbookDtl.setCreditMoney(BigDecimal.ZERO);
            accbookDtl.setDebitMoney(BigDecimal.ZERO);
            balanceWay = entry.getBalanceWay().intValue();
            // BigDecimal interest = BigDecimal.ZERO;
            BigDecimal accruedMoney = BigDecimal.ZERO;// 冲预提金额
            if (repay.getRepayMoney() != null && repay.getRepayMoney().compareTo(BigDecimal.ZERO) != 0) {
                // interest = iFcInterestService.calTellerMoneyInterest(repay.getLoanAcc(), repay.getRepayMoney(), "",
                // repay.getRepayDate(), user);
                accruedMoney = iFcInterestService.getRushPreDrawMoney(send.getLoanAcc(), repay.getRepayMoney(),
                        repay.getId());
            } else {
                // interest = iFcInterestService.calTellerMoneyInterest(repay.getLoanAcc(), send.getMoney(), "",
                // repay.getRepayDate(), user);
                accruedMoney = iFcInterestService.getRushPreDrawMoney(send.getLoanAcc(), null, repay.getId());
            }
            // repay.setRepayAccrual(interest);
            /**
             * "借 利息支出-银行借款" "贷 应付利息-银行借款" 借：2110银行借款 网点 1 借：521004利息支出-银行借款 3 贷：1020银行活期存款 网点 2
             */
            /**
             * 本金 借 x 贷 x
             *
             * 没本金 贷 x 借 x
             */
            if (Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT).intValue() == balanceWay) {
                if (FinacialConstant.ENTRY_TYPE_10.intValue() == entry.getEntryType().intValue()) {
                    FcAccDTO dto = fcAccService.getAccDTOByAccount(send.getLoanAcc());
                    accbookDtl.setAccount(dto.getBlnctrAcc());
                    accbookDtl.setDebitMoney(repay.getRepayMoney());
                    accbookDtl.setAccName(dto.getAccName());
                } else {
                    FcAccDTO dto = null;
                    // 获取利息支出账号
                    if (EmptyUtils.isNotEmpty(fcAccDTO.getInterestInExp())) {// 获取利息支出-银行借款
                        dto = innerAccountService.getByBlnctrAcc(fcAccDTO.getInterestInExp());
                    } else {
                        List<com.bytter.biz.finacial.api.dto.FcAccDTO> dtos = fcAccbookService.getInterestAcc(
                                send.getCurCode(), info.getNetId(), send.getCorpId(), entry.getSubjectCode(), "provide",
                                info.getBankId(), user.getId(), "利息支出-银行借款");// 利息支出-银行借款
                        if (EmptyUtils.isEmptyList(dtos)) {
                            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANACC_IS_NULL);// 结算中心未开设利息支出-银行借款
                        }
                        // todo:数据转换
                        com.bytter.biz.finacial.api.dto.FcAccDTO accDTO = dtos.get(0);
                        dto = BeanUtils.copyNewInstanceProperties(accDTO, FcAccDTO.class);//
                    }
                    accbookDtl.setAccount(dto.getBlnctrAcc());
                    accbookDtl.setAccName(dto.getAccName());
                    if (FinacialConstant.ENTRY_TYPE_40.intValue() == entry.getEntryType().intValue()) {// 利息支出
                        accbookDtl.setDebitMoney(repay.getRepayAccrual());
                    } else if (FinacialConstant.ENTRY_TYPE_30.intValue() == entry.getEntryType()
                            .intValue()) {// 利息支出-银行借款
                        accbookDtl.setDebitMoney(BigDecimal.ZERO.subtract(accruedMoney));
                    }
                }
            } else {
                FcAccDTO dto = null;
                if (FinacialConstant.ENTRY_TYPE_30.intValue() == entry.getEntryType().intValue()) {// 应付利息-银行借款
                    if (EmptyUtils.isNotEmpty(fcAccDTO.getInterestRecPay())) {// 获取应付利息-银行借款
                        dto = innerAccountService.getByBlnctrAcc(fcAccDTO.getInterestRecPay());
                    } else {
                        List<com.bytter.biz.finacial.api.dto.FcAccDTO> dtos = fcAccbookService.getInterestAcc(
                                send.getCurCode(), info.getNetId(), send.getCorpId(), entry.getSubjectCode(), "provide",
                                info.getBankId(), user.getId(), "应付利息-银行借款");// 应付利息-银行借款
                        if (EmptyUtils.isEmptyList(dtos)) {
                            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANACC_IS_NULL);// 结算中心未开设利息支出-银行借款
                        }
                        // todo:数据转换
                        com.bytter.biz.finacial.api.dto.FcAccDTO accDto = dtos.get(0);
                        dto = BeanUtils.copyNewInstanceProperties(accDto, FcAccDTO.class);//
                    }
                    accbookDtl.setAccount(dto.getBlnctrAcc());
                    accbookDtl.setCreditMoney(BigDecimal.ZERO.subtract(accruedMoney));
                    accbookDtl.setAccName(dto.getAccName());
                } else {
                    dto = fcAccService.getAccDTOByAccount(send.getBlnctrAcc());// 发放时使用的银行活期帐号 (本金) 记账为0会自动过滤掉
                    accbookDtl.setAccount(dto.getBlnctrAcc());
                    accbookDtl.setCreditMoney(repay.getRepayMoney().add(repay.getRepayAccrual()));// 利息加本金
                    accbookDtl.setAccName(dto.getAccName());
                }
            }
            accbookDtl.setCurCode(send.getCurCode());// 使用发放币别
            accbookDtl.setOrigVoucherNo(repay.getBillCode());
            accbookDtl.setSummaryContent(repay.getRmk());
            accbookDtls.add(accbookDtl);
        }
        return accbookDtls;
    }
    /****************** 线下记账END ************************/
    /****************** 线上记账begin ************************/
    /**
     * 线上记账
     *
     * @param repay
     * @param user
     */
    public void onlineSubmit(CmsProvideLoanRepay repay, ICurrentUser user) {
        ConfigDTO config = configService.getConfig("cms", "is_borrow_tally");
        String value = config.getValue();
        CorpDTO corp = corpServiceImpl.get(repay.getCorpId());
        if (CmsConstant.IS_TRUE.equals(value) && CorpConstant.CORP_TYPE_CODE_NET.equals(corp.getType())) {
            // 构建凭证传输对象
            FcAccbookVoucherDTO accbook = onlineProcessAccbook(repay, user);
            // 记账提交 TODO
            VoucherWrapperDTO voucher = cmsPrecativeLoanSendService.submit(accbook,
                    BusOriType.NB_BANK_REPAYMENT_ONLINE.get(), user);
            // 设置记账返回数据
            FcAccbookDTO acc = voucher.getTarget().get(0).getAccbook();
            // List<FcAccbookDtlDTO> bookDtls = voucher.getTarget().get(0).getBookDtls();
            // 记账流水号
            repay.setOrderNo(acc.getOrderNo());
            // 记账凭证号
            repay.setVoucherNo(acc.getVoucherNo());
        }
    }
    /**
     * 创建线上还款凭证信息
     *
     * @param repay
     * @param user
     * @return
     */
    public FcAccbookVoucherDTO onlineProcessAccbook(CmsProvideLoanRepay repay, ICurrentUser user) {
        FcAccbookVoucherDTO entity = new FcAccbookVoucherDTO();
        FcBusinessRuleDefineDTO ruleDefine = cmsPrecativeLoanSendService
                .getBusinessRuleExp(SysRaceCode.NB_BANK_REPAYMENT_ONLINE.get());
        entity = onlineProcessTallyAccbook(repay, ruleDefine, user);
        return entity;
    }
    /**
     * 线上还款设置凭证信息
     */
    public FcAccbookVoucherDTO onlineProcessTallyAccbook(CmsProvideLoanRepay repay, FcBusinessRuleDefineDTO ruleDefine,
                                                         ICurrentUser user) {
        FcAccbookVoucherDTO entity = new FcAccbookVoucherDTO();
        FcAccbookDTO accBook = new FcAccbookDTO();
        entity.setAccbook(accBook);
        List<FcAccbookDtlDTO> accbookDtls = null;
        accbookDtls = getOnlineAccbookDtls(ruleDefine, repay, user);
        accBook.setBusRaceId(cmsPrecativeLoanSendService.getFcBusRaceId(repay.getCorpId(),
                SysRaceCode.NB_BANK_REPAYMENT_ONLINE, user == null ? " " : user.getId()));
        accBook.setVoucherType(ruleDefine.getVoucherType());
        accBook.setTallyDate(repay.getRepayDate());
        accBook.setInterestStartDate(repay.getRepayDate());
        accBook.setEmergencySign(FinacialConstant.EMERGENCYSIGN_G);
        entity.setAccbook(accBook);
        entity.setBookDtls(accbookDtls);
        return entity;
    }
    /**
     * 借款自动还款
     */
    @Override
    @XxlJob("CmsProvideLoanRepayService")
    public void run(String arguments) {
        this.logger.info("---启动定时任务银行借款自动还款，开始执行定时任务---");
        List<LoanRepayVO> loanRepayVOList = cmsProvideLoanRepayDao.getAutoDataAppTables();
        for (LoanRepayVO loanRepayVO : loanRepayVOList) {
            List<CmsProvideInterestVO> interestVOList = cmsProvideInterestService.queryAutoInterestList(loanRepayVO.getSendCode());
            if (interestVOList.size() >= 1) {
                for (CmsProvideInterestVO interestVO : interestVOList) {
                    if (DateUtils.format(interestVO.getSignRepayDate(), "yyyy-MM-dd").compareTo(DateUtils.format(DateUtils.getDate(), "yyyy-MM-dd")) <= 0) {
                        LoanRepayVO repayVO = new LoanRepayVO();
                        repayVO.setSendCode(loanRepayVO.getSendCode());
                        repayVO.setCreateDate(new Date());
                        repayVO.setRepayDate(interestVO.getSignRepayDate());
                        repayVO.setRepayMoney(interestVO.getCountRepayMoney());
                        repayVO.setRmk(multiLangServiceI.getLang(CmsConstant.TABLE_CMSPROVIDELOANINFO_AUTOPAY));
                        repayVO.setRepayAccrual(interestVO.getCountPayInterest());
                        repayVO.setOnlineRealityInterest(interestVO.getCountPayInterest());
                        repayVO.setProvideInterestId(interestVO.getId());
                        repayVO.setAcrossRepair(FinacialConstant.BOOLEAN_TRUE);//设置跳过补录
                        repayVO.setOnlineSign(CmsConstant.ONLINE_FALSE);
                        repayVO.setTempOrSubmitSign(CmsConstant.IS_FALSE);
                        repayVO.setIsAdvancedRepay(FinacialConstant.BOOLEAN_FALSE);
                        ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(loanRepayVO.getSendCode());
                        repayVO.setCorpId(send.getCorpId());
                        ICurrentUser userVo = userServiceImpl.getScheduler();
                        getSession().clear();
                        this.tempOrSubmit(repayVO, CmsConstant.IS_TRUE, CmsConstant.IS_FALSE, userVo, "");
                    } else{
                        break;//还款日期按顺序排列，如果大于当前日期了，则进行下一笔借款的还款计划
                    }
                }
            }
        }
        this.logger.info("---结束定时任务银行借款自动还款---");
    }
    /**
     * 功能：记账时的会计分录构造
     */
    public List<FcAccbookDtlDTO> getOnlineAccbookDtls(FcBusinessRuleDefineDTO ruleDefine, CmsProvideLoanRepay repay,
                                                      ICurrentUser user) {
        ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(repay.getSendCode());
        // BigDecimal interest = BigDecimal.ZERO;
        BigDecimal accruedMoney = BigDecimal.ZERO;// 冲预提金额
        if (repay.getRepayMoney() != null && repay.getRepayMoney().compareTo(BigDecimal.ZERO) != 0) {
            // interest = iFcInterestService.calTellerMoneyInterest(repay.getLoanAcc(), repay.getRepayMoney(), "",
            // repay.getRepayDate(), user);
            accruedMoney = iFcInterestService.getRushPreDrawMoney(send.getLoanAcc(), repay.getRepayMoney(),
                    repay.getId());
        } else {
            // interest = iFcInterestService.calTellerMoneyInterest(repay.getLoanAcc(), send.getMoney(), "",
            // repay.getRepayDate(), user);
            accruedMoney = iFcInterestService.getRushPreDrawMoney(send.getLoanAcc(), null, repay.getId());
        }
        // repay.setRepayAccrual(interest);
        List<FcVoucherEntryDefineDTO> entrys = ruleDefine.getVouchers();
        FcAccbookDtlDTO accbookDtl = null;
        cmsPrecativeLoanSendService.collectionSort(entrys);
        List<FcAccbookDtlDTO> accbookDtls = new ArrayList<FcAccbookDtlDTO>();
        int balanceWay;
        CmsProvideLoanInfo info = loanInfoService.getLoanInfoByBillCode(send.getLoanCode());
        String loanAcc = send.getLoanAcc();// 贷款账号
        FcAccDTO fcAccDTO = innerAccountService.getByBlnctrAcc(loanAcc);
        for (FcVoucherEntryDefineDTO entry : entrys) {
            if (entry.getGroupNum() == null) {
                throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_GROUPNUM_IS_NULL);
            }
            accbookDtl = new FcAccbookDtlDTO();
            accbookDtl.setCreditMoney(BigDecimal.ZERO);
            accbookDtl.setDebitMoney(BigDecimal.ZERO);
            balanceWay = entry.getBalanceWay().intValue();
            /**
             * 借：银行借款 借：利息支出-银行借款 贷：其他应付款
             */
            if (Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT).intValue() == balanceWay) {
                if (FinacialConstant.ENTRY_TYPE_10.intValue() == entry.getEntryType().intValue()) {
                    FcAccDTO dto = fcAccService.getAccDTOByAccount(send.getLoanAcc());// 银行借款
                    accbookDtl.setAccount(dto.getBlnctrAcc());
                    accbookDtl.setDebitMoney(repay.getRepayMoney());
                    accbookDtl.setAccName(dto.getAccName());
                } else {
                    FcAccDTO dto = null;
                    // 获取利息支出账号
                    if (EmptyUtils.isNotEmpty(fcAccDTO.getInterestInExp())) {// 获取利息支出-银行借款
                        dto = innerAccountService.getByBlnctrAcc(fcAccDTO.getInterestInExp());
                    } else {
                        List<com.bytter.biz.finacial.api.dto.FcAccDTO> dtos = fcAccbookService.getInterestAcc(
                                send.getCurCode(), info.getNetId(), send.getCorpId(), entry.getSubjectCode(), "provide",
                                info.getBankId(), user.getId(), "利息支出-银行借款");// 利息支出-银行借款
                        if (EmptyUtils.isEmptyList(dtos)) {
                            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANACC_IS_NULL);// 结算中心未开设利息支出-银行借款
                        }
                        // todo:数据转换
                        com.bytter.biz.finacial.api.dto.FcAccDTO accDTO = dtos.get(0);
                        dto = BeanUtils.copyNewInstanceProperties(accDTO, FcAccDTO.class);// 利息支出
                    }
                    accbookDtl.setAccount(dto.getBlnctrAcc());
                    accbookDtl.setAccName(dto.getAccName());
                    if (FinacialConstant.ENTRY_TYPE_40.intValue() == entry.getEntryType().intValue()) {// 利息
                        // 利息支出-银行借款
                        accbookDtl.setDebitMoney(repay.getOnlineRealityInterest());
                    } else if (FinacialConstant.ENTRY_TYPE_30.intValue() == entry.getEntryType()
                            .intValue()) {// 冲预提 利息支出-银行借款
                        accbookDtl.setDebitMoney(BigDecimal.ZERO.subtract(accruedMoney));
                    }
                }
            } else {
                FcAccDTO dto = null;
                if (FinacialConstant.ENTRY_TYPE_30.intValue() == entry.getEntryType().intValue()) {// 应付利息-银行借款
                    if (EmptyUtils.isNotEmpty(fcAccDTO.getInterestRecPay())) {// 获取应付利息-银行借款
                        dto = innerAccountService.getByBlnctrAcc(fcAccDTO.getInterestRecPay());
                    } else {
                        List<com.bytter.biz.finacial.api.dto.FcAccDTO> dtos = fcAccbookService.getInterestAcc(
                                send.getCurCode(), info.getNetId(), send.getCorpId(), entry.getSubjectCode(), "provide",
                                info.getBankId(), user.getId(), "应付利息-银行借款");// 应付利息-银行借款
                        if (EmptyUtils.isEmptyList(dtos)) {
                            throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_LOANACC_IS_NULL);// 结算中心未开设利息支出-银行借款
                        }
                        // todo:数据转换
                        com.bytter.biz.finacial.api.dto.FcAccDTO accDTO = dtos.get(0);
                        dto = BeanUtils.copyNewInstanceProperties(accDTO, FcAccDTO.class);//
                    }
                    accbookDtl.setAccount(dto.getBlnctrAcc());
                    accbookDtl.setAccName(dto.getAccName());
                    accbookDtl.setCreditMoney(BigDecimal.ZERO.subtract(accruedMoney));
                } else {
                    List<FcAccDTO> dtos = innerAccountService.listAccountByParams(send.getCurCode(), repay.getCorpId(),
                            repay.getCorpId(), entry.getSubjectCode());// 其他应付款
                    if (EmptyUtils.isEmptyList(dtos)) {
                        throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_OTHERACC_IS_NULL);// 结算中心未开设 其他应付款
                    }
                    dto = dtos.get(0);// 其他应付款
                    accbookDtl.setAccount(dto.getBlnctrAcc());
                    // accbookDtl.setCreditMoney(repay.getOnlineMoney());//线上还款金额
                    accbookDtl.setCreditMoney(repay.getRepayMoney().add(repay.getOnlineRealityInterest()));// 线上还款金额可修改，会导致借贷不平
                    accbookDtl.setAccName(dto.getAccName());
                }
                repay.setOnlineOtherAccCharge(dto.getBlnctrAcc());// 保存其他应付款帐号
            }
            accbookDtl.setCurCode(send.getCurCode());// 使用发放币别
            accbookDtl.setOrigVoucherNo(repay.getBillCode());
            accbookDtl.setSummaryContent(repay.getRmk());
            accbookDtls.add(accbookDtl);
        }
        return accbookDtls;
    }
    /**
     * 根据参数对其他应付款帐号排序 暂时未定
     */
    private void orderList(List<FcAccDTO> dtos) {
        // 将分段集合按照开户日期降序排列
        Collections.sort(dtos, new Comparator<FcAccDTO>() {
            public int compare(FcAccDTO p1, FcAccDTO p2) {
                Calendar c1 = Calendar.getInstance();
                // c1.setTime(p1.getStartDate());
                Calendar c2 = Calendar.getInstance();
                // c2.setTime(p2.getStartDate());
                return c1.compareTo(c2);
            }
        });
    }
    /****************** 线上记账END ************************/
    /****************** 线上还款记账-确认已扣款BEGIN ************************/
    /**
     * 线上还款--确认已扣款
     *
     * @param repay
     * @param user
     */
    public void onlineOKSubmit(CmsProvideLoanRepay repay, ICurrentUser user) {
        ConfigDTO config = configService.getConfig("cms", "is_borrow_tally");
        String value = config.getValue();
        CorpDTO corp = corpServiceImpl.get(repay.getCorpId());
        if (CmsConstant.IS_TRUE.equals(value) && CorpConstant.CORP_TYPE_CODE_NET.equals(corp.getType())) {
            // 构建凭证传输对象
            FcAccbookVoucherDTO accbook = onlineOKProcessAccbook(repay, user);
            // 记账提交 TODO
            VoucherWrapperDTO voucher = cmsPrecativeLoanSendService.submit(accbook,
                    BusOriType.NB_BANK_REPAYMENT_ONLINE_OK.get(), user);
            // 设置记账返回数据
            FcAccbookDTO acc = voucher.getTarget().get(0).getAccbook();
            // List<FcAccbookDtlDTO> bookDtls = voucher.getTarget().get(0).getBookDtls();
            // 记账流水号
            repay.setOrderNo(acc.getOrderNo());
            // 记账凭证号
            repay.setVoucherNo(acc.getVoucherNo());
        }
    }
    /**
     * 创建 线上还款--确认已扣款 凭证信息
     *
     * @param repay
     * @param user
     * @return
     */
    public FcAccbookVoucherDTO onlineOKProcessAccbook(CmsProvideLoanRepay repay, ICurrentUser user) {
        FcAccbookVoucherDTO entity = new FcAccbookVoucherDTO();
        FcBusinessRuleDefineDTO ruleDefine = cmsPrecativeLoanSendService
                .getBusinessRuleExp(SysRaceCode.NB_BANK_REPAYMENT_ONLINE_OK.get());
        entity = onlineOKProcessTallyAccbook(repay, ruleDefine, user);
        return entity;
    }
    /**
     * 线上还款--确认已扣款 设置凭证信息
     */
    public FcAccbookVoucherDTO onlineOKProcessTallyAccbook(CmsProvideLoanRepay repay,
                                                           FcBusinessRuleDefineDTO ruleDefine, ICurrentUser user) {
        FcAccbookVoucherDTO entity = new FcAccbookVoucherDTO();
        FcAccbookDTO accBook = new FcAccbookDTO();
        entity.setAccbook(accBook);
        List<FcAccbookDtlDTO> accbookDtls = null;
        accbookDtls = getOnlineOKAccbookDtls(ruleDefine, repay);
        accBook.setBusRaceId(cmsPrecativeLoanSendService.getFcBusRaceId(repay.getCorpId(),
                SysRaceCode.NB_BANK_REPAYMENT_ONLINE_OK, user == null ? " " : user.getId()));
        accBook.setVoucherType(ruleDefine.getVoucherType());
        accBook.setTallyDate(repay.getRepayDate());
        accBook.setInterestStartDate(repay.getRepayDate());
        accBook.setEmergencySign(FinacialConstant.EMERGENCYSIGN_G);
        entity.setAccbook(accBook);
        entity.setBookDtls(accbookDtls);
        return entity;
    }
    /**
     * 功能：线上还款--确认已扣款 记账时的会计分录构造
     */
    public List<FcAccbookDtlDTO> getOnlineOKAccbookDtls(FcBusinessRuleDefineDTO ruleDefine, CmsProvideLoanRepay repay) {
        List<FcVoucherEntryDefineDTO> entrys = ruleDefine.getVouchers();
        FcAccbookDtlDTO accbookDtl = null;
        cmsPrecativeLoanSendService.collectionSort(entrys);
        List<FcAccbookDtlDTO> accbookDtls = new ArrayList<FcAccbookDtlDTO>();
        int balanceWay;
        ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(repay.getSendCode());
        CmsProvideLoanInfo info = loanInfoService.getLoanInfoByBillCode(send.getLoanCode());
        CmsProvideLoanRepay repayAccount = get(repay.getId());
        for (FcVoucherEntryDefineDTO entry : entrys) {
            if (entry.getGroupNum() == null) {
                throw new ParameterException(ProvideMessageEnum.CMS_PROVIDE_GROUPNUM_IS_NULL);
            }
            accbookDtl = new FcAccbookDtlDTO();
            accbookDtl.setCreditMoney(BigDecimal.ZERO);
            accbookDtl.setDebitMoney(BigDecimal.ZERO);
            balanceWay = entry.getBalanceWay().intValue();
            /**
             * 借：其他应付款 贷：银行活期
             */
            if (Integer.valueOf(FinacialConstant.BALANCE_WAY_DEBIT).intValue() == balanceWay) {
                FcAccDTO dto = fcAccService.getAccDTOByAccount(repay.getOnlineOtherAccCharge());// 其他应付款帐号
                accbookDtl.setAccount(dto.getBlnctrAcc());
                accbookDtl.setDebitMoney(repay.getRepayMoney().add(repay.getOnlineRealityInterest()));// 线上还款金额可修改，会导致借贷不平
                accbookDtl.setAccName(dto.getAccName());
            } else {
                String blnctrAcc = btBankAccService.getBlnctrAcc(repayAccount.getOnlinePaymentAcc(), info.getCurId());// 银行活期查关联内部账号
                if (EmptyUtils.isEmpty(blnctrAcc)) {
                    throw new ParameterException(ProvideMessageEnum.CMS_BANKACC_RELATION_BLNCTRACC_IS_NULL,
                            repayAccount.getOnlinePaymentAcc());// 银行账户未关联内部账户
                } else {
                    FcAccDTO dto = fcAccService.getAccDTOByAccount(blnctrAcc);
                    accbookDtl.setAccount(blnctrAcc);
                    // accbookDtl.setCreditMoney(repay.getOnlineMoney());//利息加本金
                    accbookDtl.setCreditMoney(repay.getRepayMoney().add(repay.getOnlineRealityInterest()));
                    accbookDtl.setAccName(dto.getAccName());
                }
            }
            accbookDtl.setCurCode(send.getCurCode());// 使用发放币别
            accbookDtl.setOrigVoucherNo(repay.getBillCode());
            accbookDtl.setSummaryContent(info.getPlPurpose());
            accbookDtls.add(accbookDtl);
        }
        return accbookDtls;
    }
    /**
     * 记账作废
     */
    public void invalidVoucher(CmsProvideLoanRepay repay) {
        if (CmsConstant.ONLINE_TRUE.equals(repay.getOnlineSign()) && !EmptyUtils.isEmpty(repay.getVoucherNo())) {
            VoucherWrapperDTO dto = fcAccbookService.getVoucherByVoucherNo(repay.getVoucherNo());
            dto.setAutoCheck(true);// 自动复核
            if (FinacialConstant.OVER_SIGN_NO.equals(dto.getFlag())) {// 作废
                fcAccbookService.invalidVoucher(dto);
                // 资金预算
                this.fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_PROVIDE_REPAY,
                        BudgetConstant.PROCESS_SYSNAME_CMS, repay.getBillCode());
            } else {// 冲销
                fcAccbookService.rushVoucher(dto);
            }
        }
    }
    @Override
    public void invalidVoucher(String repayId) {
        if (!EmptyUtils.isEmpty(repayId)) {
            CmsProvideLoanRepay repay = this.get(repayId);
            ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(repay.getSendCode());
            send.setBizStatus(CmsConstant.BUSINESSSTATE_42);
            this.invalidVoucher(repay);
            repay.setVoucherNo(null);
            repay.setOrderNo(null);
            repay.setOnlineOtherAccCharge(null);
            repay.setOnlineStatus(0);
            repay.setBizStatus(CmsConstant.BUSINESSSTATE_53);
            repay.setTallySign(null);
            this.saveOrUpdate(repay);
            cmsProvideLoanSendService.updateSend(send);
        }
    }
    @Override
    public void occupyGuaContract(String id) {
        loanInfoService.updateGuaranteeConfig(id, CmsConstant.IS_TRUE);
    }
    @Override
    public void cancelGuaContract(String id) {
        loanInfoService.updateGuaranteeConfig(id, CmsConstant.IS_FALSE);
    }
    //
    public void isRepayComplete(String repayId, ICurrentUser user) {
        this.getEntityDao().flush();
        CmsProvideLoanRepay repay = this.get(repayId);
        ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(repay.getSendCode());
        send = cmsProvideLoanSendService.get(send.getId());// 临时解决下hibernate缓存问题
        // 如果已还金额等于发放金额 修改发放表状态为业务完成
        BigDecimal sendBalance = this.completeSendBalance(repay.getSendCode(), "2");// 发放余额
        Integer repayPassageCount = this.getRepayAccrualCount(repay.getSendCode());// 未记账的利息
        if (sendBalance.compareTo(BigDecimal.ZERO) <= 0
                && (repayPassageCount == null || repayPassageCount.intValue() == 0)) {// 发放金额等于还款总金额 没有还款在途金额
            // 判断业务已完成的 发放金额是否等于借款合同金额
            send.setBizStatus(CmsConstant.BUSINESSSTATE_48);
            send.setStatus(CmsConstant.FINISH);
            cmsProvideLoanSendService.updateEntity(send);
            String loanAcc = send.getLoanAcc();
            if (!EmptyUtils.isEmpty(loanAcc)) {
                innerAccountService.cancelFcAcc(loanAcc, repay.getRepayDate(), user);
            }
        }
        CmsProvideLoanInfo info = loanInfoService.getLoanInfoByBillCode(send.getLoanCode());
        BigDecimal repaySum = cmsProvideLoanRepayDao.getInfoRepayMoneySum(info.getBillCode());
        if (info.getPlMoney().compareTo(repaySum) == 0) {// 借款金额等于还款总金额
            info.setStatus(CmsConstant.FINISH);
            info.setBizStatus(CmsConstant.BUSINESSSTATE_110);
            loanInfoService.saveOrUpdate(info);
        }
        //预计利息处理
        if(repay.getStatus().equals(CmsConstant.COMPELETOTAL)) {//还款完成
            CmsProvideInterest interest = cmsProvideInterestDao.get(repay.getProvideInterestId());
            interest.setStatus(1);//已还
            cmsProvideInterestDao.saveOrUpdate(interest);
        }
    }
    /****************** 线上还款记账-确认已扣款END ************************/
    /**
     * 判断是否自动记账
     */
    public boolean isAutoAccounting() {
        ConfigDTO config = configService.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS,
                BaseDataConstant.IS_BORROW_AUTOMATIC_TALLY);
        // 自动记账
        return CmsConstant.IS_TRUE.equals(config.getValue());
    }
    /**
     * 合同履约处理
     */
    public void handleContract(CmsProvideLoanRepay repay, ICurrentUser user) {
        this.getEntityDao().flush();
        String relateCon = configService.getConfig(ConfigConstant.CMS, BaseDataConstant.BANK_RELEATE_CON).getValue();// 是否对接合同管理
        String relateConType = configService.getConfig(ConfigConstant.CMS, BaseDataConstant.BANK_RELATE_CON_TYPE)
                .getValue();// 对接合同管理管理模式
        String conSegment = configService.getConfig(ConfigConstant.CMS, BaseDataConstant.BANK_RELATE_CON_SEGMENT)
                .toFormatString();// 履约环节
        if (null != relateCon && relateCon.equals(CmsConstant.RELATED_CONTRACT_YES) && // 对接合同管理
                // //&&conSegment.equals(CmsConstant.SEGMENT_LOAN)
                // null!=relateConType&&relateConType.equals(CmsConstant.BUSINESS_CREATE_CON)&&
                conSegment.contains(CmsConstant.SEGMENT_REPAY)) {// 履约环节包括还款履约
            ConInfoRecordDTO recordDTO = new ConInfoRecordDTO();
            LoanRepayVO repayVO = this.queryRepayVOByBillCode(repay.getBillCode(), null);
            recordDTO.setConId(repayVO.getConId());// 合同ID 必填
            recordDTO.setOuterId(repay.getId());// 履约业务挂接ID 必填
            recordDTO.setOpMsg(CmsConstant.PROVIDE_LOAN_REPAY);// 履约原因 必填
            recordDTO.setOpDate(repay.getRepayDate());// 履约时间 必填
            recordDTO.setOpNo(repay.getBillCode());// 履约申请编号 选填
            recordDTO.setOpMoney(repay.getRepayMoney());// 履约金额 选填
            conCommonService.performanceConOp(recordDTO, user);
        }
    }
    /**
     * 终止合同处理
     */
    public void endContract(CmsProvideLoanInfo info, CmsProvideLoanRepay repay, ICurrentUser user) {
        if (StringUtils.isNotEmpty(info.getConId())) {
            ConInfoRecordDTO dto = new ConInfoRecordDTO();
            dto.setConId(info.getConId());
            dto.setOuterId(repay.getId());// 终止业务挂接ID
            dto.setOpMsg(CmsConstant.PROVIDE_LOAN_REPAYOVER);// 终止原因：银行借款全部还清
            dto.setOpDate(repay.getRepayDate());
            dto.setOpNo(repay.getBillCode());
            conCommonService.terminateConOp(dto, user);
        }
    }
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
        if (EmptyUtils.isNotEmpty(corpId)) {
            // 还款申请待补录
            int dataSize1 = cmsProvideLoanRepayDao.queryBusBillByCorpId(corpId, null, CmsConstant.BUSINESSSTATE_52);// 还款待补录
            if (dataSize1 > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.CMS_PROVIDELOAN, multiLangServiceI
                        .getLang(CorpVerifyMessageConstant.CMS_VALID_DISABLE_PROLOAN_REPAYWAITREPAIR_ERROR)));
            }
            // 还款申请暂存，待审批，审批打回
            int dataSize2 = cmsProvideLoanRepayDao.queryBusBillByCorpId(corpId,
                    CmsConstant.TEMP + "," + CmsConstant.COMMITAPPROVE + "," + CmsConstant.BACK, null);// 还款待补录
            if (dataSize2 > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.CMS_PROVIDELOAN,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.CMS_VALID_DISABLE_PROLOAN_REPAY_ERROR)));
            }
            // 借款还款补录暂存，待审批，审批打回
            int dataSize3 = cmsProvideLoanRepayDao.queryBusBillByCorpId(corpId, null, CmsConstant.BUSINESSSTATE_50 + ","
                    + CmsConstant.BUSINESSSTATE_51 + "," + CmsConstant.BUSINESSSTATE_56);
            if (dataSize3 > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId, SysConfigConstant.Module.CMS,
                        SysConfigConstant.BusType.CMS_PROVIDELOAN, multiLangServiceI
                        .getLang(CorpVerifyMessageConstant.CMS_VALID_DISABLE_PROLOAN_REPAYREPAIR_ERROR)));
            }
            /** 待记账 */
            if (cmsProvideLoanRepayDao.verifyCorpDisableByCorpId(corpId) > 0) {
                CorpVerifyErrorDTO corpVerifyErrorDTO = new CorpVerifyErrorDTO();
                corpVerifyErrorDTO.setCorpId(corpId);
                corpVerifyErrorDTO.setSysCode(SysConfigConstant.Module.FCL);
                corpVerifyErrorDTO.setBusCode(SysConfigConstant.BusType.FCL_TALLY);
                corpVerifyErrorDTO
                        .setErrorMsg(multiLangServiceI.getLang(CorpVerifyMessageConstant.FIN_TALLY_VALID_DISABLE_ERROR));
                verifyErrorDTOS.add(corpVerifyErrorDTO);
            }
        }
        return verifyErrorDTOS;
    }
    @Override
    public List<GuaContractCmsProvideLoanDTO> getGuaCmsProvideLoan() {
        return cmsLoanInfoApplyService.getGuaCmsProvideLoan();
    }
    @Override
    public BigDecimal queryProvideUseRatMoney(String billNo, String endDate) {
        Date calEndDate = DateUtils.str2Date(endDate, DateUtils.datetimeFormat);
        return cmsProvideLoanRepayDao.queryProvideUseRatMoney(billNo, calEndDate);
    }

    /**
     * 定时任务查询待生成需求单的融资到期
     * @return
     */
    @Override
    public List<CmsProvideInterestDTO> listTaskRepayForNisDemand(){
        ConfigDTO config = configService.getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.PROVIDE_CONFIG_NOTIFYDAYS);
        Integer day = config == null ? 2 : config.toInteger(2);
        Date workDay = calendarService.getWorkDay(DateUtils.getDateYMD(), day);
        if(workDay == null){
            logger.info("工作日为空，定时任务查询待生成需求单的融资到期为空");
            return null;
        }
        return cmsProvideLoanRepayDao.listTaskRepayForNisDemand(workDay);
    }
}
