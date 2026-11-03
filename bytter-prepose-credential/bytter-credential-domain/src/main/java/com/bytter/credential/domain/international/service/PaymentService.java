package com.bytter.credential.domain.international.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
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
import com.bytter.credential.domain.international.dao.PaymentDao;
import com.bytter.credential.domain.international.entity.LcBillArrival;
import com.bytter.credential.domain.international.entity.LcPayment;
import com.bytter.credential.domain.international.utils.CreditUtil;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2018-12-19 9:39
 * @Version 1.0
 */
@Transactional
@Service("paymentService")
@WorkFlow(value = { LcWorkFlowConstant.LC_PAYMENT })
public class PaymentService extends BaseService<LcPayment, String> implements IWorkFlowListener {
    @Autowired
    private PaymentDao paymentDao;
    @DubboReference(check = false)
	private ICorpService corpService;
    @Autowired
    private LcMessageService lcMessage;
    @Autowired
	private IWorkFlowService workFlowService;
    @Autowired
    private BillArrivalService billArrivalService;
    @DubboReference(check = false)
	private INotificationService notificationService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    /**
     * 付汇申请列表
     * @param dataTablesInput
     * @param user
     * @return
     */
    public DataTablesOutput listArrival(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpIds = corpService.getUserCorpIds(InternationalResourceConstant.INTERNATIONAL_CREDIT_PAYMENT_QUERY, user.getId());
        return paymentDao.listArrival(dataTablesInput,corpIds);
    }
    /**
     * 付汇修改列表
     * @param dataTablesInput
     * @param user
     * @return
     */
    public DataTablesOutput listPaymentUpdate(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpIds = corpService.getUserCorpIds(InternationalResourceConstant.INTERNATIONAL_CREDIT_PAYMENT_QUERY, user.getId());
        return paymentDao.listPaymentUpdate(dataTablesInput,corpIds);
    }
    /**
     *
     * @param lcPaymentSrc
     * @param user
     */
    public void saveOrTmpPayment(LcPayment lcPaymentSrc, ICurrentUser user) {
        LcPayment lcPayment = null;
        if (lcPaymentSrc.getId() == null) {
            lcPaymentSrc.setCreateBy(user.getLoginName());
            lcPaymentSrc.setCreateDate(new Date());
            lcPaymentSrc.setPaymentNo(CreditUtil.getPaymentNo());
            BtCurrencyDTO btCurrencyDTO = currencyService.getObjectById(lcPaymentSrc.getCurId());
            lcPaymentSrc.setCurName(btCurrencyDTO.getCurName());
            saveEntity(lcPaymentSrc);
            lcPayment = lcPaymentSrc;
            /*修改到单登记状态*/
            LcBillArrival lcBillArrival = billArrivalService.get(lcPayment.getArrivalId());
            /*付汇*/
            lcBillArrival.setStatus(InternationalConstant.BILLARRIVALPAYMENT);
            billArrivalService.updateEntity(lcBillArrival);
        } else {
            lcPayment = get(lcPaymentSrc.getId());
            BeanUtils.copyNotNullProperties(lcPaymentSrc,lcPayment);
            updateEntity(lcPayment);
        }
//        打回数据角标处理
        notificationService.complete(NotificationConstant.INTERNATIONAL.INTERNATIONAL_PAYMENT_BACK, lcPayment.getId(), user.getId());
        //审批
        if (lcPayment.getStatus() == 11) {// 提交审批
            try {
                lcMessage.insertProcessMsg(lcPayment.getId(), lcPayment.getPaymentNo(), LcEnrolConstant.LC_PAYMENT, LcMsgConstant.MSGA_SUBMIT, "付汇审批提交", user);
                //默认审批通过
                getEntityDao().flush();
                 getStartHandler().handler(LcWorkFlowConstant.LC_PAYMENT, lcPayment.getId(), lcPayment, lcPayment.getCorpId(), user.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            lcMessage.insertProcessMsg(lcPayment.getId(),lcPayment.getPaymentNo(),LcEnrolConstant.LC_PAYMENT, LcMsgConstant.MSGA_INPUT, "付汇暂存", user);
        }
    }
    /**
     * 付汇申请审批列表页面
     * @param dataTablesInput
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput listPaymentApprove(DataTablesInput dataTablesInput){
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(LcWorkFlowConstant.LC_PAYMENT, dataTablesInput);
        DataTablesOutput dataTablesOutput = null;
        try {
            dataTablesOutput = paymentDao.listPaymentApprove(dataTablesInput,workFlowBusiness.getBusinessIdList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return workFlowBusiness.merge(dataTablesOutput);
    }
    /**
     * 已押汇付汇金额
     * @param params
     * @return
     */
    public double getSumDepositedAndPaymented(Map<String, Object> params) {
        return Double.valueOf(paymentDao.getSumDepositedAndPaymented(params).get(0).toString());
    }
    /**
     * 押汇付汇在途金额
     * @param params
     * @return
     */
    public double getSumDepositingAndPaymenting(Map<String, Object> params) {
        return Double.valueOf(paymentDao.getSumDepositingAndPaymenting(params).get(0).toString());
    }
    @Override
    public BaseDao<LcPayment, String> getEntityDao() {
        return paymentDao;
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        /*判断业务主键是否为空*/
        if (StringUtils.isEmpty(workFlowResult.getBusinessKey())) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        LcPayment lcPayment = get(workFlowResult.getBusinessKey());
        int status;
        if (workFlowResult.getProcessDefinitionKey().equals(LcWorkFlowConstant.LC_PAYMENT)) {
            /*请在通过或不通过中调用自己的处理方法*/
            if (workFlowResult.isPass()) {
                /*状态设置*/
                status = InternationalConstant.PAYMENAPPROVED;
                /*日志插入*/
                handelMsg(lcPayment, "信用付汇证审批通过", LcMsgConstant.MSGA_CHECKOK, workFlowResult);
                /*消息发送*/
                sendMsg(lcPayment, NotificationConstant.INTERNATIONAL.INTERNATIONAL_PAYMENT_PASS);
            } else {
                /*状态设置*/
                status = InternationalConstant.BACK;
                /*日志插入*/
                handelMsg(lcPayment, "信用证付汇审批打回", LcMsgConstant.MSGA_ROLLBACK, workFlowResult);
                /*消息发送*/
                sendMsg(lcPayment, NotificationConstant.INTERNATIONAL.INTERNATIONAL_PAYMENT_BACK);
            }
            lcPayment.setStatus(status);
            saveOrUpdate(lcPayment);
        }
    }
    /**
     * 取消付汇
     * @param id
     * @param user
     */
    public void cancelPayment(String id, ICurrentUser user) {
        LcPayment lcPayment = get(id);
        LcBillArrival lcBillArrival = billArrivalService.get(lcPayment.getArrivalId());
        lcPayment.setStatus(InternationalConstant.DELETED);
        lcPayment.setUpdateDate(new Date());
        lcPayment.setUpdateBy(user.getLoginName());
//        1 入库
        lcBillArrival.setStatus(1);
        lcBillArrival.setUpdateDate(new Date());
        lcBillArrival.setUpdateBy(user.getLoginName());
        updateEntity(lcPayment);
        billArrivalService.updateEntity(lcBillArrival);
    }
    /**
     * 审批日志
     * @param lcPayment
     * @param msg
     * @param workFlowResult
     * @param messageType
     */
    private void handelMsg(LcPayment lcPayment, String msg, int messageType, IWorkFlowResult workFlowResult){
        lcMessage.insertProcessMsg(lcPayment.getId(),lcPayment.getPaymentNo(),LcEnrolConstant.LC_PAYMENT, messageType, msg, workFlowResult.getUser());
    }
    /**
     * 审批消息推送通知
     * @param lcPayment
     * @param tag
     */
    private void sendMsg(LcPayment lcPayment, String tag) {
        notificationService.send(lcPayment.getId(), lcPayment.getCorpId(), tag, lcPayment);
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
}
