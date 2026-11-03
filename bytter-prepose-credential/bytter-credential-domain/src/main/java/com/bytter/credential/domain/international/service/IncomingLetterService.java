package com.bytter.credential.domain.international.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.enums.AccebillMessageEnum;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credential.api.constant.*;
import com.bytter.credential.domain.international.dao.ArrivalLetterDao;
import com.bytter.credential.domain.international.dao.IncomingLetterDao;
import com.bytter.credential.domain.international.entity.LcBillArrival;
import com.bytter.credential.domain.international.entity.LcIncoming;
import com.bytter.credential.domain.international.vo.LcBillArrivalVO;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.workflow.bpm.IWorkFlowListener;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
@Service("incomingletterservice")
@Transactional
@WorkFlow(value = {LcWorkFlowConstant.LC_RECEIVE_INCOMING })
public class IncomingLetterService extends BaseService<LcIncoming,String> implements IWorkFlowListener {
    @Autowired
    private ArrivalLetterDao arrivalLetterDao;    
    @Autowired
    private LcMessageService lcMessage;
    @DubboReference(check = false)
	private INotificationService notificationService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @Autowired
	private IncomingLetterDao incomingLetterDao;
    @DubboReference
    private IBtCurrencyService btCurrencyService;

    /**到期付款列表**/
    @Transactional(readOnly = true)
    public DataTablesOutput queryIncoming(DataTablesInput input, ICurrentUser user){
        List<String> corpAuth = corpService.getUserCorpIds(InternationalResourceConstant.INTERNATIONAL_CREDIT_INCOMING_QUERY, user.getId());
        DataTablesOutput dataTablesOutput = arrivalLetterDao.queryIncoming(input, corpAuth, "C");
        List<LcBillArrivalVO> data = dataTablesOutput.getData();
        if (ListUtils.isNotNullOrEmpty(data)){
            data.stream().forEach(item -> {
                if (StringUtils.isBlank(item.getCurName())){
//                    corpService.get(item.get)
                    item.setCurName(btCurrencyService.getObjectById(item.getCurId()).getCurName());
                }
                if (StringUtils.isBlank(item.getCorpName())){
                    item.setCorpName(corpService.get(item.getCorpId()).getName());
                }
            });
        }
        dataTablesOutput.setData(data);
        return dataTablesOutput;
    }
    /**到期付款列表**/
    @Transactional(readOnly = true)
    public DataTablesOutput selectDatatable(DataTablesInput input, ICurrentUser user){
        return arrivalLetterDao.selectDatatable(input);
    }
    /**
     * 功能介绍 : 到期付款提交<br>
     *  6提交
     */
    public void tempOrSubmit(LcIncoming entity, ICurrentUser user) {
        entity.setStatus(InternationalConstant.FINISH);
        entity.setIncomeNo(getIncomeNo());
        entity.setCreateBy(user.getLoginName());
        entity.setCreateDate(new Date());
        super.saveEntity(entity);
        
        LcBillArrival arrival = arrivalLetterDao.get(entity.getArrivalId());
        arrival.setStatus(InternationalConstant.FINISH);
        arrivalLetterDao.saveOrUpdate(arrival);
        notificationService.complete(NotificationConstant.INTERNATIONAL.INTERNATIONAL_RECEIVE_ARRIVAL, arrival.getLcId(),user.getId());
        notificationService.complete(NotificationConstant.INTERNATIONAL.INTERNATIONAL_ARRIVAL_ARRIVAL, arrival.getId(),user.getId());
      /*  //审批，
        if (entity.getStatus() == 11) {// 提交审批
            lcMessage.insertProcessMsg(entity.getId(),entity.getIncomeNo(), null, LcMsgConstant.MSGA_SUBMIT, "到期付款提交", user);
            //默认审批通过
            this.getEntityDao().flush();
             getStartHandler().handler(LcWorkFlowConstant.LC_RECEIVE_INCOMING, entity.getId(), entity, entity.getCorpId(), user.getId());
        }*/
        
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        if(StringUtils.isEmpty(workFlowResult.getBusinessKey())){
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        LcIncoming lcIncoming = this.get(workFlowResult.getBusinessKey(), true);
        Integer status = null;
        String bizStatus = null;
        if(workFlowResult.getProcessDefinitionKey().equals(LcWorkFlowConstant.LC_RECEIVE_INCOMING)){//提交审批
            status = workFlowResult.isPass()? InternationalConstant.FINISH:InternationalConstant.BACK;
           //bizStatus = workFlowResult.isPass()?CmsConstant.BUSINESSSTATE_72:CmsConstant.BUSINESSSTATE_67;//收到信用证待交单or审批打回
            if(workFlowResult.isPass()){
                lcMessage.insertProcessMsg(lcIncoming.getId(),lcIncoming.getIncomeNo(),null, LcMsgConstant.MSGA_CHECKOK, "到期付款审批通过", workFlowResult.getUser());
                //                收款审批通过
                LcBillArrival arrival = arrivalLetterDao.get(lcIncoming.getArrivalId());
                arrival.setStatus(InternationalConstant.FINISH);
                arrivalLetterDao.saveOrUpdate(arrival);
            }else{
                lcMessage.insertProcessMsg(lcIncoming.getId(),lcIncoming.getIncomeNo(),null, LcMsgConstant.MSGA_ROLLBACK, "到期付款审批打回", workFlowResult.getUser());
                notificationService.send(lcIncoming.getId(), lcIncoming.getCorpId(), NotificationConstant.INTERNATIONAL.INTERNATIONAL_INCOMING_UPDATE, lcIncoming);
            }
        }
        lcIncoming.setStatus(status);
        incomingLetterDao.saveOrUpdate(lcIncoming);
    }
    /**
     * <b>功能介绍：</b>生成交单单号<br>
     */
    public String getIncomeNo(){
        return SequenceSingleton.getSequence(SequenceEnum.LC_INCOMING_BILL_NO, LcEnrolConstant.LC_RECEIVE_BILL_CODE_INCOMING);
    }
    
    @Override
    public BaseDao<LcIncoming, String> getEntityDao() {
        return incomingLetterDao;
    }
}
