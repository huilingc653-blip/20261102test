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
import com.bytter.credential.domain.international.dao.LcDepositDao;
import com.bytter.credential.domain.international.dao.LcDepositDelayDao;
import com.bytter.credential.domain.international.entity.LcDeposit;
import com.bytter.credential.domain.international.entity.LcDepositDelay;
import com.bytter.credential.domain.international.utils.CreditUtil;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.workflow.bpm.IWorkFlowListener;
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
 * @Date 2018-12-25 11:32
 * @Version 1.0
 */
@Service("lcDepositDelayService")
@Transactional
@WorkFlow(value = LcWorkFlowConstant.LC_DEPOSIT_DELAY)
public class LcDepositDelayService extends BaseService<LcDepositDelay, String> implements IWorkFlowListener {
    @Autowired
    private LcDepositDelayDao lcDepositDelayDao;
    @DubboReference(check = false)
	private ICorpService corpService;
    @Autowired
    private LcMessageService lcMessage;
    @Autowired
	private IWorkFlowService workFlowService;
    @DubboReference(check = false)
	private INotificationService notificationService;
    @Autowired
    private LcDepositDao lcDepositDao;
    /**
     * 查询押汇补录完成的数据
     * @param dataTablesInput
     * @param user
     * @return
     */
    public DataTablesOutput listlcDepositDelayApply(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpIds = corpService.getUserCorpIds(InternationalResourceConstant.INTERNATIONAL_CREDIT_DEPOSIT_REPAYDELAY_QUERY, user.getId());
        return lcDepositDelayDao.listlcDepositDelayApply(dataTablesInput,corpIds);
    }
    /**
     * 展期列表数据
     * @param dataTablesInput
     * @param user
     * @return
     */
    public DataTablesOutput queryLcDepositDelay(DataTablesInput dataTablesInput, ICurrentUser user) {
        List<String> corpIds = corpService.getUserCorpIds(InternationalResourceConstant.INTERNATIONAL_CREDIT_DEPOSIT_REPAYDELAY_QUERY, user.getId());
        return lcDepositDelayDao.listlcDepositDelay(dataTablesInput,corpIds);
    }
    /**
     * 审批列表数据查询
     * @param dataTablesInput
     * @param user
     * @return
     */
    @Transactional(readOnly = true)
    public DataTablesOutput listlcDepositDelayApprove(DataTablesInput dataTablesInput, ICurrentUser user) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(LcWorkFlowConstant.LC_DEPOSIT_DELAY, dataTablesInput);
        DataTablesOutput dataTablesOutput = null;
        try {
            dataTablesOutput = lcDepositDelayDao.listlcDepositDelayApprove(dataTablesInput,workFlowBusiness.getBusinessIdList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return workFlowBusiness.merge(dataTablesOutput);
    }
    /**
     * 获取押汇最大展期日期
     * @param params
     * @return
     */
    public Map<String, Object> getMaxDate(Map<String, Object> params) {
        return lcDepositDelayDao.getMaxDate(params);
    }
    /**
     * 取消展期
     * @param id
     */
    public void DeletLcDepositDelayt(String id) {
        LcDepositDelay lcDepositDelay = get(id);
        lcDepositDelay.setStatus(InternationalConstant.DELETED);
        updateEntity(lcDepositDelay);
    }
    /**
     * 展期数据保存，进入审批流
     * @param lcDepositDelay
     * @param user
     */
    public void saveLcdepositDelay(LcDepositDelay lcDepositDelay,ICurrentUser user) {
        if (lcDepositDelay != null && lcDepositDelay.getId() == null) {
            lcDepositDelay.setCreateBy(user.getLoginName());
            lcDepositDelay.setCreateDate(new Date());
            lcDepositDelay.setDelayNo(CreditUtil.getDepostDelayNo());
        } else {
            LcDepositDelay tagetEntity = get(lcDepositDelay.getId());
            BeanUtils.copyNotNullProperties(lcDepositDelay, tagetEntity);
            lcDepositDelay.setUpdateBy(user.getLoginName());
            lcDepositDelay.setUpdateDate(new Date());
            lcDepositDelay = tagetEntity;
        }
        saveOrUpdate(lcDepositDelay);
        //        打回数据角标处理
        notificationService.complete(NotificationConstant.INTERNATIONAL.INTERNATIONAL_DEPOSIT_DELAY_BACK, lcDepositDelay.getId(), user.getId());
        //审批
        if (lcDepositDelay.getStatus() == 11) {// 提交审批
            try {
                lcMessage.insertProcessMsg(lcDepositDelay.getId(), lcDepositDelay.getDelayNo(), LcEnrolConstant.LC_DEPOSIT_DELAY, LcMsgConstant.MSGA_SUBMIT, "展期审批提交", user);
                //默认审批通过
                getEntityDao().flush();
                 getStartHandler().handler(LcWorkFlowConstant.LC_DEPOSIT_DELAY, lcDepositDelay.getId(), lcDepositDelay, lcDepositDelay.getCorpId(), user.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            lcMessage.insertProcessMsg(lcDepositDelay.getId(),lcDepositDelay.getDelayNo(),LcEnrolConstant.LC_DEPOSIT_DELAY, LcMsgConstant.MSGA_INPUT, "展期数据保存", user);
        }
    }
    @Override
    public BaseDao<LcDepositDelay, String> getEntityDao() {
        return lcDepositDelayDao;
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        /*判断业务主键是否为空*/
        if (StringUtils.isEmpty(workFlowResult.getBusinessKey())) {
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        LcDepositDelay lcDepositDelay = get(workFlowResult.getBusinessKey());
        int status;
        if (workFlowResult.getProcessDefinitionKey().equals(LcWorkFlowConstant.LC_DEPOSIT_DELAY)) {
            /*请在通过或不通过中调用自己的处理方法*/
            if (workFlowResult.isPass()) {
                /*将押汇表数据更新为已经展期*/
                LcDeposit lcDeposit = lcDepositDao.get(lcDepositDelay.getDepositId());
                lcDeposit.setStatus(InternationalConstant.DEOPSITDELAY);
                lcDepositDao.update(lcDeposit);

                status = InternationalConstant.APPROVED;
                /*日志插入*/
                handelMsg(lcDepositDelay, "展期审批通过", LcMsgConstant.MSGA_CHECKOK, workFlowResult);
                /*消息发送*/
                sendMsg(lcDepositDelay, NotificationConstant.INTERNATIONAL.INTERNATIONAL_DEPOSIT_DELAY_PASS);
            } else {
                /*状态设置*/
                status = InternationalConstant.BACK;
                /*日志插入*/
                handelMsg(lcDepositDelay, "展期审批打回", LcMsgConstant.MSGA_ROLLBACK, workFlowResult);
                /*消息发送*/
                sendMsg(lcDepositDelay, NotificationConstant.INTERNATIONAL.INTERNATIONAL_DEPOSIT_DELAY_BACK);
            }
            lcDepositDelay.setStatus(status);
            saveOrUpdate(lcDepositDelay);
        }
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    /**
     * 审批日志
     * @param lcDepositDelay
     * @param msg
     * @param workFlowResult
     * @param messageType
     */
    private void handelMsg(LcDepositDelay lcDepositDelay, String msg, int messageType, IWorkFlowResult workFlowResult){
        lcMessage.insertProcessMsg(lcDepositDelay.getId(),lcDepositDelay.getDelayNo(),LcEnrolConstant.LC_DEPOSIT_DELAY, messageType, msg, workFlowResult.getUser());
    }
    /**
     * 审批消息推送通知
     * @param lcDepositDelay
     * @param tag
     */
    private void sendMsg(LcDepositDelay lcDepositDelay, String tag) {
        notificationService.send(lcDepositDelay.getId(), lcDepositDelay.getCorpId(), tag, lcDepositDelay);
    }
}
