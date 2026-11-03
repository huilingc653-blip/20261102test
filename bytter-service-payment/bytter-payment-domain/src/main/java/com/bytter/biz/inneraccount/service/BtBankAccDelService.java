package com.bytter.biz.inneraccount.service;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.NisWorkFlowConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.BtBankAccMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.BtBankAccAppDTO;
import com.bytter.system.api.enums.NetbankMessageEnum;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 银行账号变更
 * @date 2017-05-23 17:37:39
 */
@WorkFlow(NisWorkFlowConstant.BT_BANKACC_DEL)
@DubboService(interfaceClass = IWorkFlowListener.class, group = NisWorkFlowConstant.BT_BANKACC_DEL)
@Service("btBankAccDelService")
@Transactional
public class BtBankAccDelService implements IWorkFlowListener {
	@DubboReference(check = false)
	private IBtBankAccDelService btBankAccDelService;
	@DubboReference(check = false)
	private IBtBankAccAppService btBankAccAppService;
	@Autowired
	private IWorkFlowService workFlowService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@Autowired
	private BtBankAccMessageService btBankAccMessageService;
	@DubboReference(check = false)
	private INotificationService notificationService;
//	@Autowired
//	private BtBankAccCommonService btBankAccCommonService;
	@DubboReference(check = false)
	private IBtBankAccAppCurService btBankAccAppCurService;
	@DubboReference(check = false)
	private IBisAccBalService bisAccBalService;
	@DubboReference(check = false)
	private IBtBankAccService btBankAccServiceI;
	/**
	 * 删除对象
	 *
	 * @param entity
	 */
	public void deleteEntity(BtBankAccAppDTO entity, ICurrentUser user) {
		btBankAccAppService.deleteEntity(entity);
		notificationService.complete(NotificationConstant.NETBANK.BANKACC_DEL, entity.getId(), user.getId());
	}
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
		StringBuilder sql = new StringBuilder();
		String corpAuthSql = SqlUtil.getInSql(
				corpService.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BTBANKACCDEL_QUERY, user.getId()), "acc.corp_id",
				true);
		sql.append(" and ").append(corpAuthSql);
		DataTablesOutput output = btBankAccDelService.getDataTablesUnion(dataTablesInput, sql.toString());
		//this.btBankAccCommonService.setCurName(output);
		return output;
	}
	/**
	 * 判断账户余额是否为零
	 *
	 * @param bankAcc 账户
	 * @return BisAccBalDTO
	 */
	private boolean checkAccBalZero(String bankAcc, String[] curIds){
		boolean result = false;
		BigDecimal bal = BigDecimal.ZERO;
		for(String curId : curIds){
			BigDecimal lastBal = this.bisAccBalService.getLastByBalDate(bankAcc, curId);
			if(!EmptyUtils.isEmpty(lastBal)){
				bal = bal.add(lastBal);
			}
		}
		if(bal.doubleValue() > BigDecimal.ZERO.doubleValue()){
			result = true;
		}
		return result;
	}
	/**
	 * 注销暂存提交
	 * @param btBankAccApp
	 * @param user
	 */
	public void toModify(BtBankAccAppDTO btBankAccApp, ICurrentUser user) {
		String curIdStr = btBankAccApp.getCurId();
		String[] curIds = curIdStr.split(",");
		if(checkAccBalZero(btBankAccApp.getBankAcc(), curIds)){
			throw new ParameterException(NetbankMessageEnum.NIS_BANKACC_MESSAGE_BAL_NOT_ZERO);
		}
		BtBankAccAppDTO t = this.btBankAccAppService.getById(btBankAccApp.getId());
		boolean isSave = false;
		if (null == t || t.getId() == null) {
			t = new BtBankAccAppDTO();
			isSave = true;
		}
		BeanUtils.copyNotNullProperties(btBankAccApp, t);
		if ("0".equals(btBankAccApp.getFlag())) {
			t.setStatus(BtBankAccConstant.STATUE_TEMP);
		} else {
			t.setStatus(BtBankAccConstant.STATUE_WAIT_CHECK);
		}
		/**
		 * 注销申请
		 */
		t.setBusType("3");
		t.setCurId("");
		if (isSave) {
			t.setAccNo(btBankAccAppService.getSequence(SequenceEnum.ACC_NO));
			t.setCreateBy(user.getLoginName());
			t.setCreateDate(DateUtils.getDate());
			this.btBankAccAppService.save(t);
		} else {
			t.setUpdateBy(user.getLoginName());
			t.setUpdateDate(DateUtils.getDate());
			this.btBankAccAppService.saveOrUpdate(t);
		}

		// 修改账号表是否注销状态
		btBankAccServiceI.updateBankAccClose(btBankAccApp.getBankAcc(), CommonConstant.YES);

		/**
		 * 先删除相关联的币别中间表
		 */
		/*List<BtBankAccAppCur> appCurList = null;//this.btBankAccCommonService.listBtBankAccAppCurById(t.getId());
		if(!EmptyUtils.isEmptyList(appCurList)){
			for(BtBankAccAppCur appCur: appCurList){
				this.btBankAccAppCurService.delete(appCur.getId());
			}
		}
		for (int i = 0; i < curIds.length; i++) {
			BtBankAccAppCur accCur = new BtBankAccAppCur();
			if ("all".equals(curIds[i])) {
				continue;
			}
			accCur.setAppId(t.getId());
			accCur.setCurId(curIds[i]);
			this.getEntityDao().save(accCur);
		}*/
		this.btBankAccAppCurService.delByAppId(t.getId());
		this.btBankAccAppCurService.saveByAppId(t.getId(), curIds);
		if (BtBankAccConstant.STATUE_TEMP.equals(t.getStatus())) {
			//生成日志
			btBankAccMessageService.saveMessage(t.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_ADD, "", BtBankAccMessageConstant.BUS_TYPE_DEL, user);
		} else {
			//生成日志
			btBankAccMessageService.saveMessage(t.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_SUBMIT, "", BtBankAccMessageConstant.BUS_TYPE_DEL, user);
			 getStartHandler().handler(NisWorkFlowConstant.BT_BANKACC_DEL, t.getId(), t, t.getCorpId(), user.getId());
		}
		notificationService.complete(NotificationConstant.NETBANK.BANKACC_DEL, t.getId(), user.getId());
		notificationService.sendToUserByLoginName(t.getId(), t.getCreateBy(), NotificationConstant.NETBANK.BANKACC_DEL_HANDLE, t);
	}
	@Override
	public void flowStarted(IWorkFlowResult workFlowResult) {
		// TODO Auto-generated method stub
	}
	@Override
	public void flowCompleted(IWorkFlowResult workFlowResult) {
		ICurrentUser user = workFlowResult.getUser();
		BtBankAccAppDTO bankAccApp = this.btBankAccAppService.getById(workFlowResult.getBusinessKey());
		Integer status = workFlowResult.isPass() ? BtBankAccConstant.STATUE_CHECK : BtBankAccConstant.STATUE_BACK;
		bankAccApp.setStatus(status);
		bankAccApp.setUpdateBy(user.getLoginName());
		bankAccApp.setUpdateDate(DateUtils.getDate());
		this.btBankAccAppService.saveOrUpdate(bankAccApp);
		// 如果是不通过，并且是注销的，把btBankAcc的isColse改成0
		if (!workFlowResult.isPass() && "3".equals(bankAccApp.getBusType())) {
			btBankAccServiceI.updateBankAccClose(bankAccApp.getBankAcc(), CommonConstant.NO);
		}
		Integer messageStatus = workFlowResult.isPass() ? BtBankAccMessageConstant.MESSAGE_TYPE_CHECK_PASS : BtBankAccMessageConstant.MESSAGE_TYPE_CHECK_BACK;
		//生成日志
		btBankAccMessageService.saveMessage(bankAccApp.getId(), messageStatus, "", BtBankAccMessageConstant.BUS_TYPE_DEL, user);
		// 消息提醒
		notificationService.send(bankAccApp.getId(), bankAccApp.getCorpId(), NotificationConstant.NETBANK.BANKACC_DEL_HANDLE, bankAccApp);
	}
	/**
	 * 删除
	 *
	 * @param id
	 * @param user
	 */
	public void delete(String id, ICurrentUser user) {
		BtBankAccAppDTO btBankAccApp = this.btBankAccAppService.getById(id);
		btBankAccAppService.deleteEntity(btBankAccApp);
		if(ObjectUtil.isNotEmpty(btBankAccApp.getBankAcc())){
			btBankAccServiceI.updateBankAccClose(btBankAccApp.getBankAcc(), CommonConstant.NO);
		}
		// 消息提醒
		notificationService.complete(NotificationConstant.NETBANK.BANKACC_DEL, id, user.getId());
	}
}
