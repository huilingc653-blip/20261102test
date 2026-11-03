package com.bytter.credential.domain.international.service;

import com.alibaba.nacos.client.naming.utils.CollectionUtils;
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
import com.bytter.credential.domain.international.dao.ArrivalLetterDao;
import com.bytter.credential.domain.international.dao.MortgageLetterDao;
import com.bytter.credential.domain.international.entity.LcBillArrival;
import com.bytter.credential.domain.international.entity.LcMortgage;
import com.bytter.credential.domain.international.vo.LcBillArrivalVO;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bytter.system.api.ICommonUtilService;

@Service("mortgageletterservice")
@Transactional
@WorkFlow(value = {LcWorkFlowConstant.LC_RECEIVE_MORTGAGE })
public class MortgageLetterService extends BaseService<LcMortgage,String> implements IWorkFlowListener {
    @Autowired
    private ArrivalLetterDao arrivalLetterDao;    
    @Autowired
    private LcMessageService lcMessage;
    @Autowired
	private IWorkFlowService workFlowService;
    @DubboReference(check = false)
	private INotificationService notificationService;
    @Autowired
    private MortgageLetterDao mortgageLetterDao;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
    private ICommonUtilService ICommonUtilService;
    
    /**申请抵押列表**/
    @Transactional(readOnly = true)
    public DataTablesOutput queryMortgage(DataTablesInput input, ICurrentUser user){
        List<String> corpAuth = corpService.getUserCorpIds(InternationalResourceConstant.INTERNATIONAL_CREDIT_MORTGAGE_QUERY, user.getId());
        DataTablesOutput output = arrivalLetterDao.queryIncoming(input,corpAuth,"M");
        List<LcBillArrivalVO> list = output.getData();
        if(CollectionUtils.isEmpty(list)) 
        {
        	return output;
        }
        Map<String,String> mapCorp = ICommonUtilService.mapListCorp();
        Map<String,String> mapCur = ICommonUtilService.mapListCurrency();
        for(LcBillArrivalVO vo:list) 
        {
        	if(mapCorp!=null && mapCorp.size()>0) {
        		vo.setCorpName(mapCorp.get(vo.getCorpId()));
        	}
        	if(mapCur!=null && mapCur.size()>0) {
        		vo.setCurName(mapCur.get(vo.getCurId()));
        	}
        }
        output.setData(list);
        return output;
    }
    /**申请抵押列表**/
    @Transactional(readOnly = true)
    public DataTablesOutput queryBackMortgage(DataTablesInput input, ICurrentUser user){
        List<String> corpAuth = corpService.getUserCorpIds(InternationalResourceConstant.INTERNATIONAL_CREDIT_MORTGAGE_QUERY, user.getId());
        return mortgageLetterDao.queryBackIncoming(input,corpAuth);
    }
    
    /**
     * 功能介绍 : 申请抵押提交<br>
     *  11提交审批
     */
    public void tempOrSubmit(LcMortgage entity, ICurrentUser user) {
        entity.setStatus(InternationalConstant.COMMITAPPROVE);
        entity.setMortgageNo(getMortgageNo());
        entity.setCreateBy(user.getLoginName());
        entity.setCreateDate(new Date());
        super.saveEntity(entity);
        
        LcBillArrival arrival = arrivalLetterDao.get(entity.getArrivalId());
        arrival.setStatus(InternationalConstant.COMMITAPPROVE);
        arrivalLetterDao.saveOrUpdate(arrival);
        /*消息提醒处理*/
        notificationService.complete(NotificationConstant.INTERNATIONAL.INTERNATIONAL_MORTGAGE_UPDATE, entity.getLcId(),user.getId());
        //审批，
        if (entity.getStatus() == 11) {// 提交审批
            lcMessage.insertProcessMsg(entity.getId(),entity.getMortgageNo(), LcEnrolConstant.LC_RECEIVE_BILL_CODE_MORTGAGE, LcMsgConstant.MSGA_SUBMIT, "申请抵押提交", user);
            //默认审批通过
            this.getEntityDao().flush();
             getStartHandler().handler(LcWorkFlowConstant.LC_RECEIVE_MORTGAGE, entity.getId(), entity, entity.getCorpId(), user.getId());
        }
    }
    /**
     * 修改操作
     * @version 1.0
     * @author zhengxn
     */
    public void saveOrUpdate(LcMortgage entity, ICurrentUser user){
        LcMortgage lcMortgage = this.get(entity.getId());
        entity.setStatus(InternationalConstant.COMMITAPPROVE);
        entity.setUpdateBy(user.getLoginName());
        entity.setUpdateDate(new Date());
        BeanUtils.copyNotNullProperties(entity,lcMortgage);
        super.saveOrUpdate(lcMortgage);
        /*消息提醒处理*/
        notificationService.complete(NotificationConstant.INTERNATIONAL.INTERNATIONAL_MORTGAGE_UPDATE, entity.getId(),user.getId());
        //审批
        if (lcMortgage.getStatus() == 11) {// 提交审批
            lcMessage.insertProcessMsg(lcMortgage.getId(),lcMortgage.getMortgageNo(), LcEnrolConstant.LC_RECEIVE_BILL_CODE_MORTGAGE, LcMsgConstant.MSGA_SUBMIT, "申请抵押提交", user);
            //默认审批通过
            this.getEntityDao().flush();
             getStartHandler().handler(LcWorkFlowConstant.LC_RECEIVE_MORTGAGE, lcMortgage.getId(), lcMortgage, lcMortgage.getCorpId(), user.getId());
        }
    }
    @Override
    public void flowStarted(IWorkFlowResult workFlowResult) {
    }
    @Override
    public void flowCompleted(IWorkFlowResult workFlowResult) {
        if(StringUtils.isEmpty(workFlowResult.getBusinessKey())){
            throw new BusinessException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_VALISNOTNULL);
        }
        LcMortgage lcMortgage = this.get(workFlowResult.getBusinessKey(), true);
        Integer status = null;
        if(workFlowResult.getProcessDefinitionKey().equals(LcWorkFlowConstant.LC_RECEIVE_MORTGAGE)){//提交审批
            status = workFlowResult.isPass()? InternationalConstant.APPROVED:InternationalConstant.BACK;
           //bizStatus = workFlowResult.isPass()?CmsConstant.BUSINESSSTATE_72:CmsConstant.BUSINESSSTATE_67;//收到信用证待交单or审批打回
            if(workFlowResult.isPass()){
                lcMessage.insertProcessMsg(lcMortgage.getId(),lcMortgage.getMortgageNo(), LcEnrolConstant.LC_RECEIVE_BILL_CODE_MORTGAGE, LcMsgConstant.MSGA_CHECKOK, "申请抵押审批通过", workFlowResult.getUser());
                //                审批通过
                LcBillArrival arrival = arrivalLetterDao.get(lcMortgage.getArrivalId());
                arrival.setStatus(InternationalConstant.MORTGAGE);
                arrivalLetterDao.saveOrUpdate(arrival);
            }else{
                lcMessage.insertProcessMsg(lcMortgage.getId(),lcMortgage.getMortgageNo(), LcEnrolConstant.LC_RECEIVE_BILL_CODE_MORTGAGE, LcMsgConstant.MSGA_ROLLBACK, "申请抵押审批打回", workFlowResult.getUser());
                notificationService.send(lcMortgage.getId(), lcMortgage.getCorpId(), NotificationConstant.INTERNATIONAL.INTERNATIONAL_MORTGAGE_UPDATE, lcMortgage);
            }
        }
        
        lcMortgage.setStatus(status);
        mortgageLetterDao.saveOrUpdate(lcMortgage);
    }
    
    /**取消/注销 抵押**/
    public void cancel(String id,ICurrentUser user,Integer status){
        LcMortgage lcMortgage = this.get(id);
        if(InternationalConstant.DELETED == status) {
            lcMortgage.setStatus(InternationalConstant.DELETED);
        }else if (InternationalConstant.CANCEL == status){//注销
            lcMortgage.setStatus(InternationalConstant.CANCEL);
        }
        lcMortgage.setUpdateBy(user.getLoginName());
        lcMortgage.setUpdateDate(new Date());
        lcMortgage.setDelBy(user.getLoginName());
        lcMortgage.setDelDate(new Date());
        this.saveOrUpdate(lcMortgage);
        LcBillArrival arrival = arrivalLetterDao.get(lcMortgage.getArrivalId());
        arrival.setStatus(InternationalConstant.APPROVED);
        arrivalLetterDao.saveOrUpdate(arrival);
    }
    /**
     * 功能介绍 : 申请审批列表<br>
     */
    @Transactional(readOnly = true)
    public DataTablesOutput getDataInfoApproveTables(DataTablesInput dataTablesInput) {
        WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(LcWorkFlowConstant.LC_RECEIVE_MORTGAGE,dataTablesInput);
        DataTablesOutput dp = null;
        try {
            dp = mortgageLetterDao.getDataInfoApproveTables(dataTablesInput,workFlowBusiness.getBusinessIdList());
        }catch (Exception e){
            e.printStackTrace();
        }
        return workFlowBusiness.merge(dp);
    }
    
    /**
     * <b>功能介绍：</b>生成抵押单号<br>
     */
    public String getMortgageNo(){
       return SequenceSingleton.getSequence(SequenceEnum.LC_INCOMING_BILL_NO, LcEnrolConstant.LC_RECEIVE_BILL_CODE_MORTGAGE);
    }
    
    @Override
    public BaseDao<LcMortgage, String> getEntityDao() {
        return mortgageLetterDao;
    }
}
