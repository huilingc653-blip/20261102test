package com.bytter.credential.domain.international.service;

import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
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
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.NetbankConstant;
import com.bytter.core.enums.AccebillMessageEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credential.api.constant.*;
import com.bytter.credential.api.enums.LcMessageEnum;
import com.bytter.credential.domain.international.dao.CreatedCreditDao;
import com.bytter.credential.domain.international.dao.IncomingLetterDao;
import com.bytter.credential.domain.international.entity.LcEnrol;
import com.bytter.credential.domain.international.utils.CreditUtil;
import com.bytter.credential.domain.international.vo.LcEnrolVO;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.enums.CurrencyMessageEnum;
import com.bytter.workflow.api.IWorkFlowService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.commons.collections.map.HashedMap;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2018-11-30 18:01
 * @Version 1.0
 */
@Transactional
@Service("createdCreditService")
@WorkFlow(value = {LcWorkFlowConstant.LC_CREATE_CREDIT})
public class CreatedCreditService extends BaseService<LcEnrol, String> implements IWorkFlowListener, ITaskService, ICorpDeleteVerifyService, ICorpDisableVerifyService {
    @Autowired
    private CreatedCreditDao createdCreditDao;
    @DubboReference(check = false)
	private ICorpService corpService;
    @Autowired
    private LcMessageService lcMessage;
    @Autowired
	private IWorkFlowService workFlowService;
    @DubboReference(check = false)
	private INotificationService notificationService;
    @DubboReference(check = false)
	private IConfigService configService;
    @DubboReference(check = false)
	private IBtBankAccService bankAccService;
    @DubboReference(check = false)
	private IRatUrationService urationService;
    @Autowired(required = false)
    private IFcBondHookService bondHookService;
    @DubboReference(check = false)
	private IBtCurrencyService currencyService;
    @DubboReference(check = false)
	private IGuaContractOccupServie guaContractOccupServie;
    @Resource
    private IMultiLangService multiLangServiceI;
    @Autowired
    private SupplementPaymentService supplementPaymentService;
    @Autowired
    private LcDepositService lcDepositService;
    @Autowired
	private IncomingLetterDao incomingLetterDao;
    @DubboReference(check = false)
	private IFbsBudgetProcessService fbsBudgetProcessService;
    @DubboReference(check = false)
    private IBtBankService bankService;
    /**
     * 保存开证数据
     *
     * @param lcEnrolVO
     */
    public void saveCredit(LcEnrolVO lcEnrolVO, ICurrentUser user,String guaContract) {
        LcEnrol lcEnrol = new LcEnrol();
        BeanUtils.copyNotNullProperties(lcEnrolVO, lcEnrol);
        BtCurrencyDTO btCurrencyDTO = currencyService.getObjectById(lcEnrolVO.getIssuingCurId());
        if(!EmptyUtils.isEmpty(btCurrencyDTO)){
            lcEnrol.setCurName(btCurrencyDTO.getCurName());
        }
        BtBankDTO btBankDTO=bankService.getBankName(lcEnrolVO.getIssuingBank());
        lcEnrol.setBankName(btBankDTO.getBankName());
        // 授信相关
        lcEnrol.setItemCode(lcEnrolVO.getRatItemCode());
        BigDecimal ratExchangeRate = lcEnrolVO.getRatExchangeRate();
        if (null == ratExchangeRate) {
            throw new ParameterException(CurrencyMessageEnum.BASIC_BTCURRENCY_EXCRATE_ADJUST_NULL);
        }
        lcEnrol.setExchangeRate(ratExchangeRate);
        if (null == lcEnrol.getId()) {
            if (StringUtils.isNotEmpty(lcEnrol.getCreditNo())) {
                validateCreditNo(lcEnrol.getCreditNo());
            }
            lcEnrol.setCreateBy(user.getLoginName());
            lcEnrol.setCreateDate(new Date());
            lcEnrol.setLcType("0");
            /*单位id即开证人单位id*/
            lcEnrol.setCorpId(lcEnrol.getApplicant());
            String corpName=corpService.getCorpName(lcEnrol.getApplicant());
            lcEnrol.setCorpName(corpName);
            lcEnrol.setStatus(InternationalConstant.COMMITAPPROVE);
            lcEnrol.setLcNo(CreditUtil.getLcNo());
            super.saveOrUpdate(lcEnrol);
            //            调用授信处理
            handleRatUration(lcEnrol);
            //            调用保证金业务处理
            CreditUtil.handleBond(null,lcEnrol,bondHookService,user);
            //            担保挂接业务处理 0暂存，1在审使用，2使用中，3释放,4打回
            CreditUtil.handleGua(lcEnrol, null, guaContractOccupServie,1,guaContract);
        } else {
            LcEnrol lcEnrolEntity = get(lcEnrol.getId());
            /*校验合同号*/
            if (StringUtils.isNotEmpty(lcEnrol.getCreditNo()) && !lcEnrol.getCreditNo().equals(lcEnrolEntity.getCreditNo())) {
                validateCreditNo(lcEnrol.getCreditNo());
            }
            //            授信处理-先删除旧冻结授信
            deleteUseRation(lcEnrolEntity);
            //            调用保证金业务处理
            CreditUtil.handleBond(lcEnrolEntity,lcEnrol,bondHookService,user);
//            担保挂接业务处理 0暂存，1在审使用，2使用中，3释放,4打回
            CreditUtil.handleGua(lcEnrol, lcEnrolEntity, guaContractOccupServie, 1, guaContract);
            BeanUtils.copyNotNullProperties(lcEnrol,lcEnrolEntity);
            lcEnrolEntity.setCorpId(lcEnrolEntity.getApplicant());
//            entityPropertiesSet(lcEnrol, lcEnrolEntity);
            lcEnrolEntity.setUpdateDate(new Date());
            lcEnrolEntity.setUpdateBy(user.getLoginName());
            lcEnrolEntity.setStatus(InternationalConstant.COMMITAPPROVE);
            updateEntity(lcEnrolEntity);
            //            授信处理-再审占用
            handleRatUration(lcEnrolEntity);
            lcEnrol = lcEnrolEntity;
        }
//        打回数据角标处理
        notificationService.complete(NotificationConstant.INTERNATIONAL.INTERNATIONAL_CREDIT_BACK,lcEnrol.getId(),user.getId());
        //写入计划执行
        lcEnrolVO.setId(lcEnrol.getId());
        lcEnrolVO.setLcNo(lcEnrol.getLcNo());
        lcEnrolVO.setStatus(lcEnrol.getStatus());
        lcEnrolVO.setCorpId(lcEnrol.getCorpId());
        this.writeBudgetProcess(lcEnrolVO, user);
        //审批
        if (lcEnrol.getStatus() == 11) {// 提交审批
            try {
                lcMessage.insertProcessMsg(lcEnrol.getId(), lcEnrol.getLcNo(), LcEnrolConstant.LC_RECEIVE_BILL_CODE_PREFIX, LcMsgConstant.MSGA_SUBMIT, "信用证开证", user);
                //默认审批通过
                this.getEntityDao().flush();
                 getStartHandler().handler(LcWorkFlowConstant.LC_CREATE_CREDIT, lcEnrol.getId(), lcEnrol, lcEnrol.getCorpId(), user.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            lcMessage.insertProcessMsg(lcEnrol.getId(), lcEnrol.getLcNo(), LcEnrolConstant.LC_RECEIVE_BILL_CODE_PREFIX, LcMsgConstant.MSGA_INPUT, "信用证开证", user);
        }
    }
    /**
     * 暂存开证数据
     *
     * @param lcEnrolVO
     */
    public void saveCreditTmp(LcEnrolVO lcEnrolVO, ICurrentUser user, String guaContract) {
        LcEnrol lcEnrol = new LcEnrol();
        BeanUtils.copyNotNullProperties(lcEnrolVO, lcEnrol);
        BtCurrencyDTO btCurrencyDTO = currencyService.getObjectById(lcEnrolVO.getIssuingCurId());
        if(!EmptyUtils.isEmpty(btCurrencyDTO)){
            lcEnrol.setCurName(btCurrencyDTO.getCurName());
        }
        BtBankDTO btBankDTO=bankService.getBankName(lcEnrolVO.getIssuingBank());
        lcEnrol.setBankName(btBankDTO.getBankName());
        // 授信相关
        lcEnrol.setItemCode(lcEnrolVO.getRatItemCode());
        lcEnrol.setExchangeRate(lcEnrolVO.getRatExchangeRate());
        if (null == lcEnrol.getId()) {
            if (StringUtils.isNotEmpty(lcEnrol.getCreditNo())) {
                validateCreditNo(lcEnrol.getCreditNo());
            }
            lcEnrol.setCreateBy(user.getLoginName());
            lcEnrol.setCreateDate(new Date());
            /*单位id即开证人单位id*/
            lcEnrol.setCorpId(lcEnrol.getApplicant());
            String corpName=corpService.getCorpName(lcEnrol.getApplicant());
            lcEnrol.setCorpName(corpName);
            /*0表示开证*/
            lcEnrol.setLcType("0");
            lcEnrol.setLcNo(CreditUtil.getLcNo());
            lcEnrol.setStatus(InternationalConstant.TEMPSTORE);
            super.saveOrUpdate(lcEnrol);
//            调用授信处理
            handleRatUration(lcEnrol);
//            调用保证金业务处理
            CreditUtil.handleBond(null, lcEnrol, bondHookService, user);
            //            担保挂接业务处理 0暂存，1在审使用，2使用中，3释放,4打回
            CreditUtil.handleGua(lcEnrol, null, guaContractOccupServie, 0, guaContract);
        } else {
            LcEnrol lcEnrolEntity = get(lcEnrol.getId());
            /*校验信用证号*/
            if (StringUtils.isNotEmpty(lcEnrol.getCreditNo()) && !lcEnrol.getCreditNo().equals(lcEnrolEntity.getCreditNo())) {
                validateCreditNo(lcEnrol.getCreditNo());
            }
//            授信处理-先删除旧冻结授信
            deleteUseRation(lcEnrolEntity);
            CreditUtil.handleBond(lcEnrolEntity, lcEnrol, bondHookService, user);
            //            担保挂接业务处理 0暂存，1在审使用，2使用中，3释放,4打回
            CreditUtil.handleGua(lcEnrol, lcEnrolEntity, guaContractOccupServie, 0, guaContract);
//            entityPropertiesSet(lcEnrol, lcEnrolEntity);
            BeanUtils.copyNotNullProperties(lcEnrol, lcEnrolEntity);
            lcEnrolEntity.setCorpId(lcEnrolEntity.getApplicant());
            lcEnrolEntity.setStatus(InternationalConstant.TEMPSTORE);
            saveOrUpdate(lcEnrolEntity);
//            授信处理-新增冻结授信
            handleRatUration(lcEnrolEntity);
            //            调用保证金业务处理
//        打回数据角标处理
//        notificationService.complete(NotificationConstant.INTERNATIONAL.INTERNATIONAL_CREDIT_BACK,lcEnrolEntity.getId(),user.getId());
        }
        //写入计划执行
        lcEnrolVO.setId(lcEnrol.getId());
        lcEnrolVO.setLcNo(lcEnrol.getLcNo());
        lcEnrolVO.setStatus(lcEnrol.getStatus());
        lcEnrolVO.setCorpId(lcEnrol.getCorpId());
        this.writeBudgetProcess(lcEnrolVO, user);
    }
    /**
     * 写入计划执行
     *
     * @param lcEnrolVO
     */
    public void writeBudgetProcess(LcEnrolVO lcEnrolVO, ICurrentUser user) {
        if (!EmptyUtils.isEmptyArray(lcEnrolVO.getItemCode())) {
            //写入之前先删除
            fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_CREDIT, BudgetConstant.PROCESS_SYSNAME_INTERNATIONAL, lcEnrolVO.getLcNo());
            List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();
            for (int i = 0; i < lcEnrolVO.getItemCode().length; i++) {
                FbsBudgetProcessDTO fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
                fbsBudgetProcessDTO.setCreateBy(lcEnrolVO.getCreateBy());
                fbsBudgetProcessDTO.setCreateDate(lcEnrolVO.getUpdateDate());
                fbsBudgetProcessDTO.setUpdateBy(user.getLoginName());
                fbsBudgetProcessDTO.setUpdateDate(DateUtils.getDate());
                if(InternationalConstant.COMMITAPPROVE == lcEnrolVO.getStatus() ||
                        InternationalConstant.COMMITSUPPLEMENTAPPROVE == lcEnrolVO.getStatus()){
                    fbsBudgetProcessDTO.setStatus(InternationalConstant.COMMITAPPROVE);
                } else {
                    fbsBudgetProcessDTO.setStatus(InternationalConstant.TEMP);
                }
                fbsBudgetProcessDTO.setBillNo(lcEnrolVO.getLcNo());
                fbsBudgetProcessDTO.setBillId(lcEnrolVO.getId());
                fbsBudgetProcessDTO.setItemCode(lcEnrolVO.getItemCode()[i]);
                fbsBudgetProcessDTO.setItemName(lcEnrolVO.getItemName()[i]);
                fbsBudgetProcessDTO.setProcessMoney(lcEnrolVO.getProcessMoney()[i]);
                if(lcEnrolVO.getExchangeRate()!=null) {
                    fbsBudgetProcessDTO.setExchangeRate(lcEnrolVO.getExchangeRate()[i]);
                    fbsBudgetProcessDTO.setProcessCurMoney(lcEnrolVO.getProcessCurMoney()[i]);
                }
                fbsBudgetProcessDTO.setCurId(lcEnrolVO.getIssuingCurId());
                fbsBudgetProcessDTO.setProcessDate(lcEnrolVO.getIssuingDate());
                fbsBudgetProcessDTO.setCorpId(lcEnrolVO.getCorpId()==null?lcEnrolVO.getApplicant():lcEnrolVO.getCorpId());
                fbsBudgetProcessDTO.setPayerAcc(lcEnrolVO.getApplicantAcc());
                fbsBudgetProcessDTO.setPayeeAcc(lcEnrolVO.getBeneficiaryAcc());
                fbsBudgetProcessDTO.setPayeeAccName(lcEnrolVO.getBeneficiary());
                fbsBudgetProcessDTO.setPayeeBank(lcEnrolVO.getBeneficiaryBank());
                fbsBudgetProcessDTO.setPurpose(lcEnrolVO.getPurpose());
                fbsBudgetProcessDTO.setSettlType(BudgetConstant.SETTLTYPE_2);
                fbsBudgetProcessDTO.setBusinessName(BudgetConstant.BUSINESS_NAME_CREDIT);
                fbsBudgetProcessDTO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_INTERNATIONAL);
                list.add(fbsBudgetProcessDTO);
            }
            this.fbsBudgetProcessService.writeBudgetProcess(list, user);
        } else {
            //更新资金计划执行
            fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_CREDIT, BudgetConstant.PROCESS_SYSNAME_INTERNATIONAL,
                    lcEnrolVO.getLcNo(), lcEnrolVO.getStatus(), user);
        }
    }
    /**
     * 授信冻结占用(业务数据提交待审批)
     * @param lcEnrol
     */
    public void handleRatUration(LcEnrol lcEnrol) {
//        判断是否挂授信
        if (InternationalConstant.ISHANGCREDIT_Y.equals(lcEnrol.getIsHangCredit())) {
            RatUrationDTO ratUrationDTO = new RatUrationDTO();
            CreditUtil.CreateRatUrationDTO(lcEnrol, ratUrationDTO,currencyService.getNatualCurrency());
            if (lcEnrol.getStatus() == InternationalConstant.TEMPSTORE) {
                urationService.tempUseRation(ratUrationDTO);//暂存调用
            } else if (lcEnrol.getStatus() == InternationalConstant.COMMITAPPROVE) {
                urationService.addUseRation(ratUrationDTO);//提交调用
            } else if (lcEnrol.getStatus() == InternationalConstant.APPROVED) {
                urationService.passUseRation(ratUrationDTO);//审批通过调用
            } else if (lcEnrol.getStatus() == InternationalConstant.BACK) {
                ratUrationDTO.setAllRelease(true);
                urationService.backUseRation(ratUrationDTO);//打回调用
            }
        }
    }
    /**
     * 授信冻结占用(业务暂存后删除)
     * @param lcEnrol
     */
    public void deleteUseRation(LcEnrol lcEnrol) {
        //            判断以前是否冻结授信，如果是冻结则删除冻结
        if (InternationalConstant.ISHANGCREDIT_Y.equals(lcEnrol.getIsHangCredit())) {
            RatUrationDTO ratUrationDTO = new RatUrationDTO();
            CreditUtil.CreateRatUrationDTO(lcEnrol, ratUrationDTO,currencyService.getNatualCurrency());
//            CreditUtil.setLCCreditRatUrationDTO(lcEnrol,ratUrationDTO,lcEnrol.getCreditMoney());
            urationService.deleteUseRation(ratUrationDTO);//删除授信
        }
    }
    /**
     * 更新开证数据
     *
     * @param lcEnrol
     */
    public void updateCredit(LcEnrol lcEnrol, ICurrentUser user) {
        super.updateEntity(lcEnrol);
    }
    /**
     * 删除
     *
     * @param id
     * @param user
     */
    public void deleteCredit(String id, ICurrentUser user) {
        LcEnrol lcEnrol = get(id);
        lcEnrol.setUpdateBy(user.getLoginName());
        lcEnrol.setUpdateDate(new Date());
        lcEnrol.setStatus(InternationalConstant.DELETED);
        updateEntity(lcEnrol);
//        删除保证金相关信息，释放占用额度
        CreditUtil.deleteBond(lcEnrol,bondHookService);
//        释放授信
        deleteUseRation(lcEnrol);
        //删除资金计划
        fbsBudgetProcessService.releaseBudgetProcess(BudgetConstant.BUSINESS_NAME_CREDIT, BudgetConstant.PROCESS_SYSNAME_INTERNATIONAL, lcEnrol.getLcNo());
    }
    /**
     * 根据id查找
     *
     * @param id
     */
    public LcEnrol getById(String id) {
        return createdCreditDao.get(id);
    }
    public LcEnrol getLcEnrolById(String id) {
        List<LcEnrol> list = createdCreditDao.getLcEnrolById(id);
        if(list.size()>0){
            return list.get(0);
        }
        return null;
    }
    @Override
    public BaseDao<LcEnrol, String> getEntityDao() {
        return createdCreditDao;
    }
    /**
     * 查询所有的开证信息（当前用户权限范围内）
     *
     * @param dataTablesInput
     * @param userId
     * @return
     */
    public DataTablesOutput listCredit(DataTablesInput dataTablesInput, String userId) {
        List<String> corpIds = corpService.getUserCorpIds(InternationalResourceConstant.INTERNATIONAL_CREDIT_CREATECREDIT_QUERY, userId);
        return createdCreditDao.listCredit(dataTablesInput, corpIds);
    }
    /**
     * 信用证报表查询
     *
     * @param dataTablesInput
     * @param user
     * @return
     */
    public DataTablesOutput listReportCredits(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpIds = corpService.getUserCorpIds(InternationalResourceConstant.INTERNATIONAL_CREDIT_CREATECREDIT_REPORT_QUERY, user.getId());
        return createdCreditDao.listReportCredits(dataTablesInput, corpIds);
    }
    /**
     * 查询所有的开证审批中单据（当前用户权限范围内）
     *
     * @param dataTablesInput
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput listCreditApprove(DataTablesInput dataTablesInput, String userId) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(LcWorkFlowConstant.LC_CREATE_CREDIT, dataTablesInput);
        DataTablesOutput dataTablesOutput = null;
        try {
            dataTablesOutput = createdCreditDao.listCreditApprove(dataTablesInput, workFlowBusiness.getBusinessIdList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return workFlowBusiness.merge(dataTablesOutput);
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        /*判断业务主键是否为空*/
        if (StringUtils.isEmpty(workFlowResult.getBusinessKey())) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        LcEnrol lcEnrol = get(workFlowResult.getBusinessKey());
        int status;
        if (workFlowResult.getProcessDefinitionKey().equals(LcWorkFlowConstant.LC_CREATE_CREDIT)) {
            /*请在通过或不通过中调用自己的处理方法*/
            if (workFlowResult.isPass()) {
                /*状态设置*/
                status = InternationalConstant.APPROVED;
                /*日志插入*/
                handelMsg(lcEnrol, "信用证开证审批通过", LcMsgConstant.MSGA_CHECKOK, workFlowResult);
                /*消息发送*/
                sendMsg(lcEnrol, NotificationConstant.INTERNATIONAL.INTERNATIONAL_CREDIT_PASS);
                //开证登记审批通过后，计划还是暂存状态，开证补录审批通过后更新为占用
                this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_CREDIT, BudgetConstant.PROCESS_SYSNAME_INTERNATIONAL,
                        lcEnrol.getLcNo(), NetbankConstant.STATUE_TEMP, workFlowResult.getUser());
            } else {
                /*状态设置*/
                status = InternationalConstant.BACK;
                /*日志插入*/
                handelMsg(lcEnrol, "信用证开证审批打回", LcMsgConstant.MSGA_ROLLBACK, workFlowResult);
                /*消息发送*/
                sendMsg(lcEnrol, NotificationConstant.INTERNATIONAL.INTERNATIONAL_CREDIT_BACK);
                //计划执行状态同步更新
                this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_CREDIT, BudgetConstant.PROCESS_SYSNAME_INTERNATIONAL,
                        lcEnrol.getLcNo(), NetbankConstant.STATUE_BACK, workFlowResult.getUser());
            }
            lcEnrol.setStatus(status);
            saveOrUpdate(lcEnrol);
//            授信业务处理  打回处理
            if (lcEnrol.getStatus() == InternationalConstant.BACK) {
                handleRatUration(lcEnrol);
                //        担保业务处理 0暂存，1在审使用，2使用中，3释放,4打回
                CreditUtil.handleGua(lcEnrol, null, guaContractOccupServie, 4, null);
            }
        }
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    /**
     * 审批日志
     *
     * @param lcEnrol
     * @param msg
     * @param workFlowResult
     * @param messageType
     */
    private void handelMsg(LcEnrol lcEnrol, String msg, int messageType, IWorkFlowResult workFlowResult) {
        lcMessage.insertProcessMsg(lcEnrol.getId(), lcEnrol.getLcNo(), LcEnrolConstant.LC_BILL_CODE_PREFIX, messageType, msg, workFlowResult.getUser());
    }
    /**
     * 审批消息推送通知
     *
     * @param lcEnrol
     * @param tag
     */
    private void sendMsg(LcEnrol lcEnrol, String tag) {
        notificationService.send(lcEnrol.getId(), lcEnrol.getCorpId(), tag, lcEnrol);
    }
    /**
     * 将lcEnrol部分属性设置到lcEnrolEntity中
     *
     * @param lcEnrol
     * @param lcEnrolEntity
     */
    public void entityPropertiesSet(LcEnrol lcEnrol, LcEnrol lcEnrolEntity) {
        /*信用证类型*/
        /*lcEnrolEntity.setLcInter(lcEnrol.getLcInter());*/
        /*开证人*/
        lcEnrolEntity.setApplicant(lcEnrol.getApplicant());
        /*设置单位为开证人*/
        lcEnrolEntity.setCorpId(lcEnrol.getApplicant());
        /*信用证号*/
        lcEnrolEntity.setCreditNo(lcEnrol.getCreditNo());
        /*是否代开信用证*/
        lcEnrolEntity.setIsReCredit(lcEnrol.getIsReCredit());
        /*被代开单位*/
        lcEnrolEntity.setReCorpCode(lcEnrol.getReCorpCode());
        /*代开信用证号*/
        lcEnrolEntity.setReCreditNo(lcEnrol.getReCreditNo());
        /*合同号*/
        lcEnrolEntity.setContractNo(lcEnrol.getContractNo());
        /*受益人*/
        lcEnrolEntity.setBeneficiary(lcEnrol.getBeneficiary());
        /*开证日期*/
        lcEnrolEntity.setIssuingDate(lcEnrol.getIssuingDate());
        /*信用证有效期*/
        lcEnrolEntity.setExpiryDate(lcEnrol.getExpiryDate());
        /*开证人账号*/
        lcEnrolEntity.setApplicantAcc(lcEnrol.getApplicantAcc());
        /*开证人账号id*/
        lcEnrolEntity.setApplicantAccId(lcEnrol.getApplicantAccId());
        /*受益人账号*/
        lcEnrolEntity.setBeneficiaryAcc(lcEnrol.getBeneficiaryAcc());
        /*受益人开证行*/
        lcEnrolEntity.setBeneficiaryBank(lcEnrol.getBeneficiaryBank());
        /*开证人地址*/
        lcEnrolEntity.setAddr(lcEnrol.getAddr());
        /*受益人地址*/
        lcEnrolEntity.setAddrBen(lcEnrol.getAddrBen());
        /*开证人邮编*/
        lcEnrolEntity.setPostNum(lcEnrol.getPostNum());
        /*受益人邮编*/
        lcEnrolEntity.setPostNumBen(lcEnrol.getPostNumBen());
        /*期限*/
        lcEnrolEntity.setTimeLimit(lcEnrol.getTimeLimit());
        /*是否需要许可证*/
        lcEnrolEntity.setNeedLicence(lcEnrol.getNeedLicence());
        /*许证号码*/
        lcEnrolEntity.setLicenceCode(lcEnrol.getLicenceCode());
        /*开证银行*/
        lcEnrolEntity.setIssuingBank(lcEnrol.getIssuingBank());
        /*付手续费账号*/
        lcEnrolEntity.setPayChargeAacc(lcEnrol.getPayChargeAacc());
        /*代开证银行*/
        lcEnrolEntity.setReBank(lcEnrol.getReBank());
        /*汇率*/
        lcEnrolEntity.setExchangeRate(lcEnrol.getExchangeRate());
        /*开证币种*/
        lcEnrolEntity.setIssuingCurId(lcEnrol.getIssuingCurId());
        /*开证金额*/
        lcEnrolEntity.setCreditMoney(lcEnrol.getCreditMoney());
        /*手续费币别*/
        lcEnrolEntity.setChargeCurId(lcEnrol.getChargeCurId());
        /*手续费*/
        lcEnrolEntity.setChargeMoney(lcEnrol.getChargeMoney());
        /*远期天数*/
        lcEnrolEntity.setDelayDates(lcEnrol.getDelayDates());
        /*承兑金额*/
        lcEnrolEntity.setAcceptanceMoney(lcEnrol.getAcceptanceMoney());
        /*承兑费用*/
        lcEnrolEntity.setAcceptanceFee(lcEnrol.getAcceptanceFee());
        /*其他费用*/
        lcEnrolEntity.setOtherFee(lcEnrol.getOtherFee());
        /*贴现费率*/
        lcEnrolEntity.setDiscountRate(lcEnrol.getDiscountRate());
        /*贴现息*/
        lcEnrolEntity.setDiscountInterest(lcEnrol.getDiscountInterest());
        /*贸易亏损*/
        lcEnrolEntity.setTradeDeficit(lcEnrol.getTradeDeficit());
        /*综合成本*/
        lcEnrolEntity.setCompCost(lcEnrol.getCompCost());
        /*应回款金额*/
        lcEnrolEntity.setRebackMoney(lcEnrol.getRebackMoney());
        /*敞口金额*/
        lcEnrolEntity.setOpenMoney(lcEnrol.getOpenMoney());
        /*用途*/
        lcEnrolEntity.setPurpose(lcEnrol.getPurpose());
        /*备注*/
        lcEnrolEntity.setRmk(lcEnrol.getRmk());
        /**
         * 授信部分字段
         */
        /*是否挂授信*/
        lcEnrolEntity.setIsHangCredit(lcEnrol.getIsHangCredit());
        /*授信模式*/
        lcEnrolEntity.setRatmodel(lcEnrol.getRatmodel());
        /*额度编码*/
        lcEnrolEntity.setRationCode(lcEnrol.getRationCode());
        /*占用授信代码*/
        lcEnrolEntity.setItemCode(lcEnrol.getItemCode());
        /*授信额度*/
        lcEnrolEntity.setItemMoney(lcEnrol.getItemMoney());
//        是否折算本位币
        lcEnrolEntity.setIsNativeCur(lcEnrol.getIsNativeCur());
//        折算本位币后金额
        lcEnrolEntity.setNativeCreditMoney(lcEnrol.getNativeCreditMoney());
        lcEnrolEntity.setIsOpenRat(lcEnrol.getIsOpenRat());
        lcEnrolEntity.setBillName(lcEnrol.getBillName());
        /**
         * 保证金部分字段
         */
//        保证金占比
        lcEnrolEntity.setBondPercent(lcEnrol.getBondPercent());
//        保证金金额
        lcEnrolEntity.setBondMoney(lcEnrol.getBondMoney());
//        保证金合同号
        lcEnrolEntity.setBondContract(lcEnrol.getBondContract());
//        是否挂保证金
        lcEnrolEntity.setIsHangBond(lcEnrol.getIsHangBond());
//        保证金id
        lcEnrolEntity.setBondId(lcEnrol.getBondId());
//        保证金账户
        lcEnrolEntity.setBondBankAcc(lcEnrol.getBondBankAcc());
//        保证金币别
        lcEnrolEntity.setBondCurId(lcEnrol.getBondCurId());
//        是否溢短装
        lcEnrolEntity.setIsSpillOver(lcEnrol.getIsSpillOver());
//        溢短装比例
        lcEnrolEntity.setSpillOverRate(lcEnrol.getSpillOverRate());
    }
    private void validateCreditNo(String creditNo) {
        Map<String, Object> params = new HashedMap();
        params.put("creditNo", creditNo);
        if (createdCreditDao.getcreditNoCount(params) > 0) {
            throw new ParameterException(LcMessageEnum.LC_CREATE_CREDIT_NO);
        }
    }
    /**
     *  获取银行账号
     * @param corpId
     * @return
     */
    public List<BtBankAccDTO> getBankAccsByCorpId(String corpId,String bankCode) {
//        防止加载全部数据导致加载慢，则直接返回空list
        if (StringUtils.isEmpty(corpId) && StringUtils.isEmpty(bankCode)) {
            return new ArrayList<BtBankAccDTO>();
        }
        BtBankAccDTO btBankAccDTO = new BtBankAccDTO();
        if (StringUtils.isNotEmpty(corpId)) {
            btBankAccDTO.setCorpId(corpId);
        }
        if (StringUtils.isNotEmpty(bankCode)) {
            btBankAccDTO.setBankCode(bankCode);
        }
        List<BtBankAccDTO> accList = bankAccService.getBankAccListAll(btBankAccDTO);
        return accList;
    }
    /**
    *更新业务表担保挂接
    *@Param busId
    *@Param guaContractDTO
    *@return
    */
    public void updateLcGuaById(String busId){
        LcEnrol lcEnrol = createdCreditDao.get(busId);
        lcEnrol.setIsHangGuarantee(InternationalConstant.IS_HANG_GUARANTEE_YES);
        updateEntity(lcEnrol);
    }
    /**
    *删除担保挂接信息
    *@return
    */
    public void deleteLcGuaById(String busId){
        LcEnrol lcEnrol = createdCreditDao.get(busId);
        lcEnrol.setIsHangGuarantee(InternationalConstant.IS_HANG_GUARANTEE_NO);
        updateEntity(lcEnrol);
    }
    /**
    *查询没有挂接担保的数据
    *@Param input
    *@Param user
    *@return
    */
    public DataTablesOutput listLcNotGua(DataTablesInput input) {
       return createdCreditDao.listLcNotGua(input);
    }
    @Override
    @XxlJob("CreatedCreditService")
    public void run(String arguments) {
        /*获取配置数据天数*/
        ConfigDTO config = configService.getConfig(LcConfigConstant.LC_CONFIG_TYPE, LcConfigConstant.LC_CONFIG_EXPIRYDAYS);
        if (config == null) {
//            默认值
            config = new ConfigDTO("10");
        }
        int day = 10;
        try {
            day = Integer.valueOf(config.getValue());
        } catch (Exception e) {
            day = 10;
        }
        Map<String, Object> params = new HashedMap();
        Date exprireDate = DateUtils.addDay(DateUtils.getDate(), day);
        params.put("expiryDays", config.getValue());
        params.put("exprireDate", exprireDate);
        params.put("today", DateUtils.addDay(DateUtils.getDate(), -1));
        List<LcEnrol> lcEnrols = createdCreditDao.getLcEnrolbyExpiryDays(params);
        notificationService.completeListByTag(NotificationConstant.INTERNATIONAL.INTERNATIONAL_CREDIT_EXPIRY);
        if (!EmptyUtils.isEmptyList(lcEnrols)) {
            for (LcEnrol lcEnrol : lcEnrols) {
                //消息提醒
                notificationService.sendToUserByResPermission(InternationalResourceConstant.INTERNATIONAL_CREDIT_CREATECREDIT_ADD,
                        NotificationConstant.INTERNATIONAL.INTERNATIONAL_CREDIT_EXPIRY, lcEnrol);
            }
        }
    }
    /**
    *国际结算业务单位禁用校验
    *@Param corpId
    *@return
    */
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
        List<LcEnrol> lcEnrols = createdCreditDao.verifyCorpDeleteByCorpId(corpId);
        List<CorpVerifyErrorDTO> corpVerifyErrorDTOS = new ArrayList<>();
        if (null != lcEnrols && lcEnrols.size() > 0) {
            for (LcEnrol lcEnrol : lcEnrols) {
                CorpVerifyErrorDTO corpVerifyErrorDTO = new CorpVerifyErrorDTO();
                corpVerifyErrorDTO.setCorpId(corpId);
                corpVerifyErrorDTO.setSysCode(SysConfigConstant.Module.INTERNATIONAL);
                if ("0".equals(lcEnrol.getLcType())) {
                    corpVerifyErrorDTO.setErrorMsg(multiLangServiceI.getLang(CorpVerifyMessageConstant.INTERNATIONAL_VALID_DEL_LC1_ERROR));
                    corpVerifyErrorDTO.setBusCode(SysConfigConstant.BusType.INTERNATIONAL_OPEN);
                    corpVerifyErrorDTOS.add(corpVerifyErrorDTO);
                    break;
                }
            }
            for (LcEnrol lcEnrol : lcEnrols) {
                CorpVerifyErrorDTO corpVerifyErrorDTO = new CorpVerifyErrorDTO();
                corpVerifyErrorDTO.setCorpId(corpId);
                corpVerifyErrorDTO.setSysCode(SysConfigConstant.Module.INTERNATIONAL);
                if ("1".equals(lcEnrol.getLcType())) {
                    corpVerifyErrorDTO.setErrorMsg(multiLangServiceI.getLang(CorpVerifyMessageConstant.INTERNATIONAL_VALID_DEL_LC2_ERROR));
                    corpVerifyErrorDTO.setBusCode(SysConfigConstant.BusType.INTERNATIONAL_RECEIVE);
                    corpVerifyErrorDTOS.add(corpVerifyErrorDTO);
                    break;
                }
            }
        }
        return corpVerifyErrorDTOS;
    }
    /**
    *国际结算业务单位禁用校验
    *@Param corpId
    *@return
    */
    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        List<LcEnrol> lcEnrols = createdCreditDao.verifyCorpDisableByCorpId(corpId);
        List<CorpVerifyErrorDTO> corpVerifyErrorDTOS = new ArrayList<>();
        if (null != lcEnrols && lcEnrols.size() > 0) {
            for (LcEnrol lcEnrol : lcEnrols) {
                BigDecimal allPaymentMoney = supplementPaymentService.getAllPaymentMoneyByLcId(lcEnrol);
                BigDecimal allDepositMoney = lcDepositService.getAllPaymentMoneyByLcId(lcEnrol);
                if (lcEnrol.getCreditMoney().compareTo(allPaymentMoney.add(allDepositMoney)) > 0) {
                    CorpVerifyErrorDTO corpVerifyErrorDTO = new CorpVerifyErrorDTO();
                    corpVerifyErrorDTO.setCorpId(corpId);
                    corpVerifyErrorDTO.setSysCode(SysConfigConstant.Module.INTERNATIONAL);
                    corpVerifyErrorDTO.setErrorMsg(multiLangServiceI.getLang(CorpVerifyMessageConstant.INTERNATIONAL_VALID_DISBALE_LC1_ERROR));
                    corpVerifyErrorDTO.setBusCode(SysConfigConstant.BusType.INTERNATIONAL_OPEN);
                    corpVerifyErrorDTOS.add(corpVerifyErrorDTO);
                    break;
                }
            }
        }
        List<LcEnrol> lcEnrols2 = createdCreditDao.verifyCorpDisableByCorpIdForReceive(corpId);
        if (null != lcEnrols2 && lcEnrols2.size() > 0) {
            for (LcEnrol lcEnrol : lcEnrols2) {
                Integer count = incomingLetterDao.getLcIncomingByLcId(lcEnrol.getId());
//                if (lcEnrol.getCreditMoney().compareTo(totalMoney) > 0) {
                if (count <= 0) {
                    CorpVerifyErrorDTO corpVerifyErrorDTO = new CorpVerifyErrorDTO();
                    corpVerifyErrorDTO.setCorpId(corpId);
                    corpVerifyErrorDTO.setSysCode(SysConfigConstant.Module.INTERNATIONAL);
                    corpVerifyErrorDTO.setErrorMsg(multiLangServiceI.getLang(CorpVerifyMessageConstant.INTERNATIONAL_VALID_DISABLE_LC2_ERROR));
                    corpVerifyErrorDTO.setBusCode(SysConfigConstant.BusType.INTERNATIONAL_RECEIVE);
                    corpVerifyErrorDTOS.add(corpVerifyErrorDTO);
                    break;
                }
            }
        }
        //开证暂存，打回，审批中数据校验
        CorpVerifyErrorDTO corpVerifyErrorDTO1 = null;
        String where1 = " corp_id = '" + corpId + "' and status in(-1,1,2,11,21) and lc_Type = '0'";
        Integer count1 = createdCreditDao.verifyTempData("LC_ENROL", where1);
        if (count1 > 0) {
            corpVerifyErrorDTO1 = createMsg(corpId, multiLangServiceI.getLang(CorpVerifyMessageConstant.INTERNATIONAL_VALID_DISABLE_LC3_ERROR), SysConfigConstant.BusType.INTERNATIONAL_OPEN);
        }
        //开证到单暂存数据校验
/*        if (null == corpVerifyErrorDTO1) {
            String where4 = " corp_id = '" + corpId + "' and status in(0) and ARRIVAL_TYPE = '0'";
            Integer count4 = createdCreditDao.verifyTempData("LC_BILL_ARRIVAL", where4);
            if (count4 > 0) {
                corpVerifyErrorDTO1 = createMsg(corpId, multiLangServiceI.getLang(CorpVerifyMessageConstant.INTERNATIONAL_VALID_DISABLE_LC3_ERROR), SysConfigConstant.BusType.INTERNATIONAL_OPEN);
            }
        }*/
        //开证付汇打回，暂存，审批中数据校验
        if (null == corpVerifyErrorDTO1) {
            String where5 = " corp_id = '" + corpId + "' and status in(-1,0,11)";
            Integer count5 = createdCreditDao.verifyTempData("LC_PAYMENT", where5);
            if (count5 > 0) {
                corpVerifyErrorDTO1 = createMsg(corpId, multiLangServiceI.getLang(CorpVerifyMessageConstant.INTERNATIONAL_VALID_DISABLE_LC3_ERROR), SysConfigConstant.BusType.INTERNATIONAL_OPEN);
            }
        }
        //开证押汇暂存，审批中数据校验
        if (null == corpVerifyErrorDTO1) {
            String where6 = " corp_id = '" + corpId + "' and status in(0,11)";
            Integer count6 = createdCreditDao.verifyTempData("LC_DEPOSIT", where6);
            if (count6 > 0) {
                corpVerifyErrorDTO1 = createMsg(corpId, multiLangServiceI.getLang(CorpVerifyMessageConstant.INTERNATIONAL_VALID_DISABLE_LC3_ERROR), SysConfigConstant.BusType.INTERNATIONAL_OPEN);
            }
        }
        //开证押汇展期暂存,审批中，打回数据校验
        if (null == corpVerifyErrorDTO1) {
            String where7 = " corp_id = '" + corpId + "' and status in(-1,0,11)";
            Integer count7 = createdCreditDao.verifyTempData("LC_DEPOSIT_DELAY", where7);
            if (count7 > 0) {
                corpVerifyErrorDTO1 = createMsg(corpId, multiLangServiceI.getLang(CorpVerifyMessageConstant.INTERNATIONAL_VALID_DISABLE_LC3_ERROR), SysConfigConstant.BusType.INTERNATIONAL_OPEN);
            }
        }
        //收证暂存，打回，审批中数据校验
        String where2 = " corp_id = '" + corpId + "' and status in(-1,0,11) and lc_Type = '1'";
        Integer count2 = createdCreditDao.verifyTempData("LC_ENROL", where2);
        CorpVerifyErrorDTO corpVerifyErrorDTO2 = null;
        if (count2 > 0) {
            corpVerifyErrorDTO2 = createMsg(corpId, multiLangServiceI.getLang(CorpVerifyMessageConstant.INTERNATIONAL_VALID_DISABLE_LC4_ERROR), SysConfigConstant.BusType.INTERNATIONAL_RECEIVE);
        }
        //收证交单暂存，打回，审批中数据校验
        if (null == corpVerifyErrorDTO2) {
            String where3 = " corp_id = '" + corpId + "' and status in(-1,0,11) and ARRIVAL_TYPE = '1'";
            Integer count3 = createdCreditDao.verifyTempData("LC_BILL_ARRIVAL", where3);
            if (count3 > 0) {
                corpVerifyErrorDTO2 = createMsg(corpId, multiLangServiceI.getLang(CorpVerifyMessageConstant.INTERNATIONAL_VALID_DISABLE_LC4_ERROR), SysConfigConstant.BusType.INTERNATIONAL_RECEIVE);
            }
        }
        //收证出口押汇打回审批中数据校验
        if (null == corpVerifyErrorDTO2) {
            String where8 = " corp_id = '" + corpId + "' and status in(-1,11)";
            Integer count8 = createdCreditDao.verifyTempData("LC_MORTGAGE", where8);
            if (count8 > 0) {
                corpVerifyErrorDTO2 = createMsg(corpId, multiLangServiceI.getLang(CorpVerifyMessageConstant.INTERNATIONAL_VALID_DISABLE_LC4_ERROR), SysConfigConstant.BusType.INTERNATIONAL_RECEIVE);
            }
        }
        if (null != corpVerifyErrorDTO2) {
            corpVerifyErrorDTOS.add(corpVerifyErrorDTO2);
        }
        if (null != corpVerifyErrorDTO1) {
            corpVerifyErrorDTOS.add(corpVerifyErrorDTO1);
        }
        return corpVerifyErrorDTOS;
    }
    private CorpVerifyErrorDTO createMsg(String corpId,String errorMsg,String busCode){
        CorpVerifyErrorDTO corpVerifyErrorDTO = new CorpVerifyErrorDTO();
        corpVerifyErrorDTO.setCorpId(corpId);
        corpVerifyErrorDTO.setSysCode(SysConfigConstant.Module.INTERNATIONAL);
        corpVerifyErrorDTO.setErrorMsg(errorMsg);
        corpVerifyErrorDTO.setBusCode(busCode);
        return corpVerifyErrorDTO;
    }

    public void injectValue(LcEnrolVO lcEnrolVO){
        Map<String, BtCurrencyDTO> currencyMap = currencyService.getCurrency();
        BtCurrencyDTO BbondCur = currencyMap.get(lcEnrolVO.getBondCurId());
        if (null != BbondCur){
            lcEnrolVO.setBondCurName(BbondCur.getCurName());
        }

        BtCurrencyDTO chargeCur = currencyMap.get(lcEnrolVO.getChargeCurId());
        if (null != chargeCur){
            lcEnrolVO.setChargeCurName(chargeCur.getCurName());
        }
    }
}
