package com.bytter.credential.domain.international.service;

import com.bytter.core.constant.BudgetConstant;
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
import com.bytter.core.constant.NetbankConstant;
import com.bytter.core.enums.AccebillMessageEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credential.api.constant.*;
import com.bytter.credential.api.dto.GuaContractInternationalDTO;
import com.bytter.credential.api.enums.LcMessageEnum;
import com.bytter.credential.domain.international.dao.ReceiveLetterDao;
import com.bytter.credential.domain.international.entity.LcEnrol;
import com.bytter.credential.domain.international.vo.LcEnrolVO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service("receiveLetterService")
@Transactional
@WorkFlow(value = { LcWorkFlowConstant.LC_RECEIVE_STORAGE })
public class ReceiveLetterService extends BaseService<LcEnrol,String> implements IWorkFlowListener {
    @Autowired
    private ReceiveLetterDao receiveLetterDao;    
    @Autowired
    private LcMessageService lcMessage;
    @Autowired
	private IWorkFlowService workFlowService;
    @DubboReference(check = false)
	private INotificationService notificationService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IBtBankService bankService;
    @DubboReference(check = false)
	private IFbsBudgetProcessService fbsBudgetProcessService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @DubboReference(check = false)
    private IFcExcrateAdjustService fcExcrateAdjustService;
    /**
     * 更新
     * @param entity
     */
    public void update(LcEnrol entity, ICurrentUser user){
        entity.setUpdateDate(new Date());
        entity.setUpdateBy(user.getLoginName());
        super.updateEntity(entity);
    }
    
    
    /**
     * 功能介绍 : 入库或登记<br>
     *  0登记，11入库
     */
    public void tempOrSubmit(LcEnrolVO lcEnrolVO, ICurrentUser user) {
        LcEnrol entity = new LcEnrol();
        BeanUtils.copyNotNullProperties(lcEnrolVO, entity);
        //单位名称
        String corpName=corpService.getCorpName(lcEnrolVO.getCorpId());
        entity.setCorpName(corpName);
        //币种名称赋值
        BtCurrencyDTO currencyDTO = currencyService.getObjectById(lcEnrolVO.getIssuingCurId());
        entity.setCurName(currencyDTO.getCurName());
        BigDecimal nativeCreditMoney=lcEnrolVO.getCreditMoney().multiply(lcEnrolVO.getExchangeRate()[0]).setScale(2,BigDecimal.ROUND_HALF_UP);
        entity.setNativeCreditMoney(nativeCreditMoney);
        BigDecimal ratExchangeRate=fcExcrateAdjustService.getExchangeRateBy(lcEnrolVO.getIssuingCurId(),lcEnrolVO.getConvertCurId(), DateUtils.getDateTime(), FcExcrateAdjustConstant.RATE_TYPE_CONVERT).setScale(6);
                // 授信相关
        entity.setItemCode(lcEnrolVO.getRatItemCode());
//        entity.setExchangeRate(lcEnrolVO.getRatExchangeRate());
        entity.setExchangeRate(ratExchangeRate);
        verifyAdd(entity);
        if (entity.getStatus() == 11) {//提交审批-入库
            entity.setStatus(InternationalConstant.COMMITAPPROVE);
            //entity.setBizStatus(CmsConstant.BUSINESSSTATE_61);//收到信用证入库提交待审批
        }else if (entity.getStatus() == 0) {//登记
            entity.setStatus(InternationalConstant.TEMP);
            //entity.setBizStatus(CmsConstant.BUSINESSSTATE_60);//登记
        }
        entity.setLcType("1");
        entity.setLcNo(getLcNo());
        entity.setCreateBy(user.getLoginName());
        if (EmptyUtils.isEmpty(entity.getCreateDate())) {
            entity.setCreateDate(new Date());
        }
        super.saveEntity(entity);
        bankService.saveBtBank(entity.getIssuingBank(),null);
        //            消息提醒处理
        notificationService.complete(NotificationConstant.INTERNATIONAL.INTERNATIONAL_RECEIVE_UPDATE, entity.getId(),user.getId());
        //写入计划执行
        lcEnrolVO.setId(entity.getId());
        lcEnrolVO.setLcNo(entity.getLcNo());
        lcEnrolVO.setCorpId(entity.getCorpId());
        this.writeBudgetProcess(lcEnrolVO, user);
        //审批
        if (entity.getStatus() == 11) {// 提交审批
            lcMessage.insertProcessMsg(entity.getId(),entity.getLcNo(), LcEnrolConstant.LC_RECEIVE_BILL_CODE_PREFIX, LcMsgConstant.MSGA_SUBMIT, "收到信用证入库提交", user);
            //默认审批通过
            this.getEntityDao().flush();
             getStartHandler().handler(LcWorkFlowConstant.LC_RECEIVE_STORAGE, entity.getId(), entity, entity.getCorpId(), user.getId());
        }else{
            lcMessage.insertProcessMsg(entity.getId(),entity.getLcNo(),LcEnrolConstant.LC_RECEIVE_BILL_CODE_PREFIX, LcMsgConstant.MSGA_INPUT, "收到信用证登记提交", user);
           
        }
        
    }
    
    /**登记修改列表**/
    @Transactional(readOnly = true)
    public DataTablesOutput queryAll(DataTablesInput input, ICurrentUser user){
        List<String> corpAuth = corpService.getUserCorpIds(InternationalResourceConstant.INTERNATIONAL_CREDIT_RECEIVE_QUERY, user.getId());
        return receiveLetterDao.queryAll(input,corpAuth);
    }
    /**清零收到信用证登记信息**/
    public void reset(String id, ICurrentUser user){
        LcEnrol lcEnrol = this.get(id);
        lcEnrol.setStatus(101);
        this.update(lcEnrol, user);
    }
    
    /**删除收到信用证登记信息**/
    public void delLcEnrol(String id, ICurrentUser user){
       LcEnrol lcEnrol =  this.get(id);
        lcEnrol.setStatus(InternationalConstant.DELETED);
       // lcEnrol.setBizStatus(InternationalConstant.BUSINESSSTATE_2_);
        //            消息提醒处理
        notificationService.complete(NotificationConstant.INTERNATIONAL.INTERNATIONAL_RECEIVE_UPDATE, lcEnrol.getId(),user.getId());
        this.update(lcEnrol, user);
        //删除资金计划
        fbsBudgetProcessService.releaseBudgetProcess(BudgetConstant.BUSINESS_NAME_RECEIVE, BudgetConstant.PROCESS_SYSNAME_INTERNATIONAL, lcEnrol.getLcNo());
    }
    /**通过信用证单号查询实体**/
    public LcEnrol getInfoByLcNo(String lcNo){
        return receiveLetterDao.getInfoByLcNo(lcNo);
    }
    /**
     * <b>功能介绍：</b>生成信用证单号<br>
     */
    public String getLcNo(){
        return SequenceSingleton.getSequence(SequenceEnum.LC_ENROL_BILL_NO,LcEnrolConstant.LC_RECEIVE_BILL_CODE_PREFIX);
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }

    public LcEnrolVO viewPerfectlyLcEnrolInfo(LcEnrolVO lcEnrolVo){
        BtCurrencyDTO btCurrencyDTO = currencyService.getObjectById(lcEnrolVo.getConvertCurId());
        lcEnrolVo.setConvertCurName(btCurrencyDTO.getCurName());
         return lcEnrolVo;
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        if(StringUtils.isEmpty(workFlowResult.getBusinessKey())){
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        LcEnrol lcEnrol = this.get(workFlowResult.getBusinessKey(), true);
        Integer status = null;
        String bizStatus = null;
        if(workFlowResult.getProcessDefinitionKey().equals(LcWorkFlowConstant.LC_RECEIVE_STORAGE)){//提交审批
            status = workFlowResult.isPass()? InternationalConstant.APPROVED:InternationalConstant.BACK;
           //bizStatus = workFlowResult.isPass()?CmsConstant.BUSINESSSTATE_72:CmsConstant.BUSINESSSTATE_67;//收到信用证待交单or审批打回
            if(workFlowResult.isPass()){
                lcMessage.insertProcessMsg(lcEnrol.getId(),lcEnrol.getLcNo(),LcEnrolConstant.LC_RECEIVE_BILL_CODE_PREFIX, LcMsgConstant.MSGA_CHECKOK, "收到信用证入库审批通过", workFlowResult.getUser());
                notificationService.send(lcEnrol.getId(), lcEnrol.getCorpId(), NotificationConstant.INTERNATIONAL.INTERNATIONAL_RECEIVE_ARRIVAL, lcEnrol);
                //计划执行状态同步更新
                this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_RECEIVE, BudgetConstant.PROCESS_SYSNAME_INTERNATIONAL,
                        lcEnrol.getLcNo(), NetbankConstant.STATUE_CHECK, workFlowResult.getUser());
            }else{
                lcMessage.insertProcessMsg(lcEnrol.getId(),lcEnrol.getLcNo(),LcEnrolConstant.LC_RECEIVE_BILL_CODE_PREFIX, LcMsgConstant.MSGA_ROLLBACK, "收到信用证入库审批打回", workFlowResult.getUser());
                notificationService.send(lcEnrol.getId(), lcEnrol.getCorpId(), NotificationConstant.INTERNATIONAL.INTERNATIONAL_RECEIVE_UPDATE, lcEnrol);
                //计划执行状态同步更新
                this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_RECEIVE, BudgetConstant.PROCESS_SYSNAME_INTERNATIONAL,
                        lcEnrol.getLcNo(), NetbankConstant.STATUE_BACK, workFlowResult.getUser());
            }
        }
        lcEnrol.setStatus(status);
        //lcEnrol.setBizStatus(bizStatus);
        receiveLetterDao.saveOrUpdate(lcEnrol);
    }
    /**
     * 修改操作
     * @version 1.0
     * @author zhengxn 
     */
    public void saveOrUpdate(LcEnrolVO lcEnrolVO, ICurrentUser user){
        LcEnrol entity = new LcEnrol();
        BeanUtils.copyNotNullProperties(lcEnrolVO, entity);
        // 授信相关
        entity.setItemCode(lcEnrolVO.getRatItemCode());
        entity.setExchangeRate(lcEnrolVO.getRatExchangeRate());
        LcEnrol lcEnrol = this.get(entity.getId());
        verifyAdd(entity);
        if (entity.getStatus() == 11) {//提交审批-入库
            entity.setStatus(InternationalConstant.COMMITAPPROVE);
            //entity.setBizStatus(CmsConstant.BUSINESSSTATE_61);//收到信用证入库提交待审批
        }else if (entity.getStatus() == 0) {//登记
            entity.setStatus(InternationalConstant.TEMP);
            //entity.setBizStatus(CmsConstant.BUSINESSSTATE_60);//登记
        }
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(new Date());
        BeanUtils.copyNotNullProperties(entity,lcEnrol);
        super.saveOrUpdate(lcEnrol);
//            消息提醒处理
        notificationService.complete(NotificationConstant.INTERNATIONAL.INTERNATIONAL_RECEIVE_UPDATE, entity.getId(),user.getId());
        //写入计划执行
        lcEnrolVO.setId(entity.getId());
        lcEnrolVO.setLcNo(entity.getLcNo());
        lcEnrolVO.setCorpId(entity.getCorpId());
        this.writeBudgetProcess(lcEnrolVO, user);
        //审批
        if (lcEnrol.getStatus() == 11) {// 提交审批
            lcMessage.insertProcessMsg(lcEnrol.getId(),lcEnrol.getLcNo(), LcEnrolConstant.LC_RECEIVE_BILL_CODE_PREFIX, LcMsgConstant.MSGA_SUBMIT, "收到信用证入库提交", user);
            //默认审批通过
            this.getEntityDao().flush();
             getStartHandler().handler(LcWorkFlowConstant.LC_RECEIVE_STORAGE, lcEnrol.getId(), lcEnrol, lcEnrol.getCorpId(), user.getId());
        }else{
            lcMessage.insertProcessMsg(lcEnrol.getId(),lcEnrol.getLcNo(),LcEnrolConstant.LC_RECEIVE_BILL_CODE_PREFIX, LcMsgConstant.MSGA_INPUT, "收到信用证登记提交", user);
        }
        
    }
    /**
     * 功能介绍 : 申请审批列表<br>
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataInfoApproveTables(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(LcWorkFlowConstant.LC_RECEIVE_STORAGE,dataTablesInput);
        DataTablesOutput dp = null;
        try {
             dp = receiveLetterDao.getDataInfoApproveTables(dataTablesInput,workFlowBusiness.getBusinessIdList());
        }catch (Exception e){
            e.printStackTrace();
        }
        
        return workFlowBusiness.merge(dp);
    }
    
    
    /**
     *	功能介绍 : 判断信用证号是否重复<br>	
     *	@return 重复：true    否则false
     */
    @Transactional(readOnly = true)
    public boolean checkCreditNoRepeat(LcEnrol lcEnrol){
        return receiveLetterDao.checkCreditNoRepeat(lcEnrol);
    }
    /** 后台验证信息 
     *  @version 1.0
     * @author zhengxn <b>
     * **/
    public void verifyAdd(LcEnrol lcEnrol) {
        if(checkCreditNoRepeat(lcEnrol)){
            throw new ParameterException(LcMessageEnum.LC_RECEIVE_CREDIT_NO);
        }
    }
    /**
    *收证查询报表
    *@Param input
    *@Param user
    *@return
    */
    public DataTablesOutput queryReportDataTable(DataTablesInput input, ICurrentUser user) {
        List<String> corpAuth = corpService.getUserCorpIds(InternationalResourceConstant.INTERNATIONAL_CREDIT_RECEIVECREDIT_REPORT_QUERY, user.getId());
        return receiveLetterDao.queryReportDataTable(input, corpAuth);
    }
    @Override
    public BaseDao<LcEnrol, String> getEntityDao() {
        return receiveLetterDao;
    }
    /**
     * 写入计划执行
     *
     * @param lcEnrolVO
     */
    public void writeBudgetProcess(LcEnrolVO lcEnrolVO, ICurrentUser user) {
        if (!EmptyUtils.isEmptyArray(lcEnrolVO.getItemCode())) {
            //写入之前先删除
            fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_RECEIVE, BudgetConstant.PROCESS_SYSNAME_INTERNATIONAL, lcEnrolVO.getLcNo());
            List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();
            for (int i = 0; i < lcEnrolVO.getItemCode().length; i++) {
                FbsBudgetProcessDTO fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
                fbsBudgetProcessDTO.setCreateBy(lcEnrolVO.getCreateBy());
                fbsBudgetProcessDTO.setCreateDate(lcEnrolVO.getUpdateDate());
                fbsBudgetProcessDTO.setUpdateBy(user.getLoginName());
                fbsBudgetProcessDTO.setUpdateDate(DateUtils.getDate());
                fbsBudgetProcessDTO.setStatus(lcEnrolVO.getStatus());
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
                fbsBudgetProcessDTO.setCorpId(lcEnrolVO.getCorpId());
                fbsBudgetProcessDTO.setPayerAcc(lcEnrolVO.getApplicantAcc());
                fbsBudgetProcessDTO.setPayeeAcc(lcEnrolVO.getBeneficiaryAcc());
                fbsBudgetProcessDTO.setPayeeAccName(lcEnrolVO.getBeneficiary());
                fbsBudgetProcessDTO.setPayeeBank(lcEnrolVO.getBeneficiaryBank());
                fbsBudgetProcessDTO.setPurpose(lcEnrolVO.getPurpose());
                fbsBudgetProcessDTO.setSettlType(BudgetConstant.SETTLTYPE_2);
                fbsBudgetProcessDTO.setBusinessName(BudgetConstant.BUSINESS_NAME_RECEIVE);
                fbsBudgetProcessDTO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_INTERNATIONAL);
                list.add(fbsBudgetProcessDTO);
            }
            this.fbsBudgetProcessService.writeBudgetProcess(list, user);
        } else {
            //更新资金计划执行
            fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_RECEIVE, BudgetConstant.PROCESS_SYSNAME_INTERNATIONAL,
                    lcEnrolVO.getLcNo(), lcEnrolVO.getStatus(), user);
        }
    }
    /**
     * 获取挂接授信的信用证ID集合
     * @return
     */
    public List<String> getIdByRat(){
        return receiveLetterDao.getIdByRat();
    }
    /**
     * 获取所有挂接担保的信用证数据
     */
    public List<GuaContractInternationalDTO> getGuaForInternational() {
        return receiveLetterDao.getGuaForInternational();
    }
}
