package com.bytter.credential.domain.international.service;

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
import com.bytter.core.enums.AccebillMessageEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credential.api.constant.*;
import com.bytter.credential.domain.international.dao.LcDepositDao;
import com.bytter.credential.domain.international.entity.*;
import com.bytter.credential.domain.international.utils.CreditUtil;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2018-12-21 10:12
 * @Version 1.0
 */
@Service("lcDepositService")
@Transactional
@WorkFlow(value = LcWorkFlowConstant.LC_DEPOSIT)
public class LcDepositService extends BaseService<LcDeposit, String> implements IWorkFlowListener {
    @Autowired
    private LcDepositDao lcDepositDao;
    @DubboReference(check = false)
	private ICorpService corpService;
    @Autowired
    private LcMessageService lcMessage;
    @Autowired
	private IWorkFlowService workFlowService;
    @DubboReference(check = false)
	private INotificationService notificationService;
    @Autowired
    private BillArrivalService billArrivalService;
    @Autowired
    private CreatedCreditService creditService;
    @DubboReference(check = false)
	private IRatUrationService urationService;
    @DubboReference(check = false)
	private IBtCurrencyService currencyService;
    @Autowired
    private IFcBondHookService bondHookService;
    @Autowired
    private SupplementPaymentService supplementPaymentService;
    /**
     * 付汇申请列表
     * @param dataTablesInput
     * @param user
     * @return
     */
    public DataTablesOutput listArrival(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpIds = corpService.getUserCorpIds(InternationalResourceConstant.INTERNATIONAL_CREDIT_PAYMENT_QUERY, user.getId());
        return lcDepositDao.listArrival(dataTablesInput,corpIds);
    }
    /**
     * 押汇补录暂存查询
     * @param dataTablesInput
     * @param user
     * @return
     */
    public DataTablesOutput queryDeposit(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpIds = corpService.getUserCorpIds(InternationalResourceConstant.INTERNATIONAL_CREDIT_PAYMENT_QUERY, user.getId());
        return lcDepositDao.listDeposit(dataTablesInput,corpIds);
    }
    /**
     * 还押汇列表查询
     * @param dataTablesInput
     * @param user
     * @return
     */
    public DataTablesOutput querylcDepositRepay(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpIds = corpService.getUserCorpIds(InternationalResourceConstant.INTERNATIONAL_CREDIT_DEPOSIT_REPAY_QUERY, user.getId());
        return lcDepositDao.listlcDepositRepay(dataTablesInput,corpIds);
    }
    /**
     * 保存数据，进入审批流
     * @param EntitySrc
     * @param user
     */
    public void saveOrTmpDeposit(LcDeposit EntitySrc, ICurrentUser user) {
        LcDeposit entityTarget = null;
        if (null == EntitySrc.getId()) {
            EntitySrc.setCreateDate(new Date());
            EntitySrc.setCreateBy(user.getLoginName());
            /*0:进口(开证);1:出口(收到)*/
            EntitySrc.setDepositNo(CreditUtil.getDepostNo());
            EntitySrc.setReapyMoney(new BigDecimal(0));
            EntitySrc.setLeftMoney(EntitySrc.getDepositMoney());
            BtCurrencyDTO btCurrencyDTO = currencyService.getObjectById(EntitySrc.getCurId());
            EntitySrc.setCurName(btCurrencyDTO.getCurName());
            saveEntity(EntitySrc);
            entityTarget = EntitySrc;
            LcBillArrival lcBillArrival = billArrivalService.get(EntitySrc.getArrivalId());
            lcBillArrival.setStatus(InternationalConstant.BILLARRIVALDEPOSIT);
            billArrivalService.updateEntity(lcBillArrival);
        } else {
            entityTarget = get(EntitySrc.getId());
            BeanUtils.copyNotNullProperties(EntitySrc,entityTarget);
            entityTarget.setUpdateBy(user.getLoginName());
            entityTarget.setUpdateDate(new Date());
            updateEntity(entityTarget);
        }
        //        打回数据角标处理
        notificationService.complete(NotificationConstant.INTERNATIONAL.INTERNATIONAL_DEPOSIT_BACK, entityTarget.getId(), user.getId());
        //审批
        if (entityTarget.getStatus() == 11) {// 提交审批
            try {
                lcMessage.insertProcessMsg(entityTarget.getId(), entityTarget.getDepositNo(), LcEnrolConstant.LC_DEPOSIT, LcMsgConstant.MSGA_SUBMIT, "押汇审批提交", user);
                //默认审批通过
                getEntityDao().flush();
                 getStartHandler().handler(LcWorkFlowConstant.LC_DEPOSIT, entityTarget.getId(), entityTarget, entityTarget.getCorpId(), user.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            lcMessage.insertProcessMsg(entityTarget.getId(),entityTarget.getDepositNo(),LcEnrolConstant.LC_DEPOSIT, LcMsgConstant.MSGA_INPUT, "押汇暂存", user);
        }
    }
    /**
     * 押汇申请审批列表页面
     * @param dataTablesInput
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput listDepositApprove(DataTablesInput dataTablesInput){
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(LcWorkFlowConstant.LC_DEPOSIT, dataTablesInput);
        DataTablesOutput dataTablesOutput = null;
        try {
            dataTablesOutput = lcDepositDao.listDepositApprove(dataTablesInput,workFlowBusiness.getBusinessIdList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return workFlowBusiness.merge(dataTablesOutput);
    }
    /**
     * 取消押汇
     * @param id
     * @param user
     */
    public void cancelDeposit(String id, ICurrentUser user) {
        LcDeposit lcDeposit = get(id);
        lcDeposit.setUpdateBy(user.getLoginName());
        lcDeposit.setUpdateDate(new Date());
        lcDeposit.setStatus(InternationalConstant.DELETED);
        updateEntity(lcDeposit);
        /**
         * 更新到单状态，还原数据
         */
        LcBillArrival lcBillArrival = billArrivalService.get(lcDeposit.getArrivalId());
        lcBillArrival.setStatus(InternationalConstant.BILLARRIVALSTORAGE);
        billArrivalService.updateEntity(lcBillArrival);
    }
    /**
     * 还押汇操作
     * @param lcDepositRepay
     * @param user
     */
    public void saveLcDepositRepay(LcDepositRepay lcDepositRepay, ICurrentUser user) {
        lcDepositRepay.setCreateBy(user.getLoginName());
        lcDepositRepay.setCreateDate(new Date());
        lcDepositRepay.setRepayNo(CreditUtil.getDepostRepayNo());
        //        暂时给0值，将来在根据需求更改
        lcDepositRepay.setStatus(0);
        lcDepositDao.save(lcDepositRepay);
        LcDeposit lcDeposit = get(lcDepositRepay.getDepositId());
        /*还押汇总额*/
        lcDeposit.setReapyMoney(lcDeposit.getReapyMoney().add(lcDepositRepay.getRepayMoney()));
        /*还押汇剩余金额*/
        lcDeposit.setLeftMoney(lcDeposit.getDepositMoney().subtract(lcDeposit.getReapyMoney()));
        /*设置状态未还押汇*/
        if (lcDeposit.getReapyMoney().compareTo(lcDeposit.getDepositMoney()) == 0 && lcDeposit.getLeftMoney().compareTo(new BigDecimal(0)) == 0) {
            /*设置业务状态为结束*/
            lcDeposit.setStatus(InternationalConstant.BUSINESSFINISH);
        } else {
            lcDeposit.setStatus(InternationalConstant.DEOPSITREPAY);
        }
        updateEntity(lcDeposit);
//        释放授信操作
        handleUration(lcDepositRepay);
//        释放保证金
        handleBond(lcDepositRepay);
    }
    /**
     * 释放授信
     * @param lcDepositRepay
     */
    public void handleUration(LcDepositRepay lcDepositRepay) {
        LcDeposit lcDeposit = get(lcDepositRepay.getDepositId());
        LcBillArrival lcBillArrival = billArrivalService.get(lcDeposit.getArrivalId());
        LcEnrol lcEnrol = creditService.get(lcBillArrival.getLcId());
        if (InternationalConstant.ISHANGCREDIT_Y.equals(lcEnrol.getIsHangCredit())) {
            RatUrationDTO ratUrationDTO = new RatUrationDTO();
            BtCurrencyDTO nativeCurrencyDTO= currencyService.getNatualCurrency();
            if (!nativeCurrencyDTO.getId().equals(lcEnrol.getIssuingCurId())) {
                //接口会自动折算汇率
                    CreditUtil.setLCCreditRatUrationDTO(lcEnrol, ratUrationDTO, lcDepositRepay.getRepayMoney(), lcDepositRepay.getIsAllRelease());
            } else {
//                本位币释放授信
                CreditUtil.setLCCreditRatUrationDTO(lcEnrol, ratUrationDTO, lcDepositRepay.getRepayMoney(), lcDepositRepay.getIsAllRelease());
            }
            urationService.releaseRation(ratUrationDTO);
        }
    }
    /**
     * 保证金释放处理
     * @param lcDepositRepay
     */
    public void handleBond(LcDepositRepay lcDepositRepay){
        LcDeposit lcDeposit = get(lcDepositRepay.getDepositId());
        LcBillArrival lcBillArrival = billArrivalService.get(lcDeposit.getArrivalId());
        LcEnrol lcEnrol = creditService.get(lcBillArrival.getLcId());
        if (InternationalConstant.ISHANGBOND_Y.equals(lcEnrol.getIsHangBond())) {
            lcDepositDao.flush();
            CreditUtil.handleReleaseBond(lcEnrol,supplementPaymentService,this,bondHookService);
        }
    }
    /**
     * 通过信用证ID获取该信用证下所有还押汇金额
     * @param lcEnrol
     * @return
     */
    public BigDecimal getAllPaymentMoneyByLcId(LcEnrol lcEnrol){
        Map<String, Object> params = new HashMap<>();
        params.put("lcId", lcEnrol.getId());
        Map result = lcDepositDao.getAllDepositRepayMoneyByLcId(params);
        return new BigDecimal(Double.valueOf(result.get("money").toString()));
    }
    /**
     * 根据押汇id获取展期信息
     * @param depositId
     * @return
     */
    public LcDepositDelay getLcDepositDelayByDepositId(String depositId) {
        Map<String, Object> params = new HashedMap();
        LcDepositDelay lcDepositDelay = null;
        params.put("depositId", depositId);
        List<LcDepositDelay> LcDepositDelays = lcDepositDao.getMaxDateDepositDelayByDepositId(params);
        if (LcDepositDelays != null && LcDepositDelays.size() > 0) {
            lcDepositDelay = LcDepositDelays.get(0);
        }
        return lcDepositDelay;
    }
    /**
     * 审批日志
     * @param lcDeposit
     * @param msg
     * @param workFlowResult
     * @param messageType
     */
    private void handelMsg(LcDeposit lcDeposit, String msg, int messageType, IWorkFlowResult workFlowResult){
        lcMessage.insertProcessMsg(lcDeposit.getId(),lcDeposit.getDepositNo(),LcEnrolConstant.LC_DEPOSIT, messageType, msg, workFlowResult.getUser());
    }
    /**
     * 审批消息推送通知
     * @param lcDeposit
     * @param tag
     */
    private void sendMsg(LcDeposit lcDeposit, String tag) {
        notificationService.send(lcDeposit.getId(), lcDeposit.getCorpId(), tag, lcDeposit);
    }
    @Override
    public BaseDao<LcDeposit, String> getEntityDao() {
        return lcDepositDao;
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        /*判断业务主键是否为空*/
        if (StringUtils.isEmpty(workFlowResult.getBusinessKey())) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        LcDeposit lcDeposit = get(workFlowResult.getBusinessKey());
        int status;
        if (workFlowResult.getProcessDefinitionKey().equals(LcWorkFlowConstant.LC_DEPOSIT)) {
            /*请在通过或不通过中调用自己的处理方法*/
            if (workFlowResult.isPass()) {
                /*状态设置*/
                if ("1".equals(lcDeposit.getIsSkipProcess())) {
                    //                当选择跳过补录的时候，则置96补录完成状态
                    status = InternationalConstant.DEOPSITSUPPLEMENT;
                } else {
                    status = InternationalConstant.DEOPSITAPPROVED;
                }
                /*日志插入*/
                handelMsg(lcDeposit, "出口押汇审批通过", LcMsgConstant.MSGA_CHECKOK, workFlowResult);
                /*消息发送*/
                sendMsg(lcDeposit, NotificationConstant.INTERNATIONAL.INTERNATIONAL_DEPOSIT_PASS);
            } else {
                /*状态设置*/
                status = InternationalConstant.BACK;
                /*日志插入*/
                handelMsg(lcDeposit, "出口押汇审批打回", LcMsgConstant.MSGA_ROLLBACK, workFlowResult);
                /*消息发送*/
                sendMsg(lcDeposit, NotificationConstant.INTERNATIONAL.INTERNATIONAL_DEPOSIT_BACK);
            }
            lcDeposit.setStatus(status);
            saveOrUpdate(lcDeposit);
        }
    }
    /**
     * 进口押汇数据查询
     * @return
     */
    public DataTablesOutput listReportDepositDataTable(DataTablesInput input, ICurrentUser user) {
        List<String> corpIds = corpService.getUserCorpIds(InternationalResourceConstant.INTERNATIONAL_CREDIT_DEPOSIT_REPORT_QUERY, user.getId());
        return lcDepositDao.listReportDepositDataTable(input, corpIds);
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
}
