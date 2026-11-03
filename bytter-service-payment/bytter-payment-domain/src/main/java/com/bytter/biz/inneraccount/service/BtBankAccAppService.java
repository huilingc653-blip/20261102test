package com.bytter.biz.inneraccount.service;
import com.bytter.biz.inneraccount.vo.BtBankAccAppVO;
import com.bytter.biz.bdlink.api.constant.BtBankAccMessageConstant;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.NisWorkFlowConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.CorpVerifyMessageConstant;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.BtBankAccAppDTO;
import com.bytter.system.api.dto.CorpVerifyErrorDTO;
import com.bytter.system.api.enums.BankInfoMessageEnum;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 银行账号申请表
 * @date 2017-05-23 17:37:39
 */
//TODO注释的需要处理
@WorkFlow(NisWorkFlowConstant.BT_BANKACC_APP)
@DubboService(interfaceClass = IWorkFlowListener.class, group = NisWorkFlowConstant.BT_BANKACC_APP)
@Service("btBankAccAppService")
@Transactional
public class BtBankAccAppService implements IWorkFlowListener, ICorpDisableVerifyService {
	@DubboReference(check = false)
	private IBtBankAccAppService btBankAccAppService;
	@DubboReference(check = false)
	private IBtBankAccCommonService btBankAccCommonService;
	@DubboReference(check = false)
	private IBtBankService bankService;
	@DubboReference(check = false)
	private IBankTypeService bankTypeService;
	@DubboReference
	private IBtCurrencyService btCurrencyService;
	@DubboReference
	private IRegionService regionService;
	@DubboReference
	private IDictService dictService;
	@Autowired
	private IWorkFlowService workFlowService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@Autowired
	private BtBankAccMessageService btBankAccMessageService;
	@DubboReference(check = false)
	private INotificationService notificationService;
	@Resource
    private IMultiLangService multiLangServiceI;
	@DubboReference(check = false)
	private IBtBankAccAppCurService btBankAccAppCurService;
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
		StringBuilder sql = new StringBuilder();
		String corpAuthSql = SqlUtil.getInSql(
				corpService.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BTBANKACCAPP_QUERY, user.getId()), "acc.corp_id",
				true);
		sql.append(" and ").append(corpAuthSql);
		sql.append(" and (acc.status = " + BtBankAccConstant.STATUE_TEMP + " or acc.status = " + BtBankAccConstant.STATUE_BACK + ") ");
		DataTablesOutput output = btBankAccAppService.getDataTablesUnion(dataTablesInput, sql.toString(), BtBankAccConstant.BUSTYPE_1);
		return output;
	}
	public void toSave(BtBankAccAppDTO btBankAccApp, ICurrentUser user) {
		String curIdStr = btBankAccApp.getCurId();
		String[] curIds = curIdStr.split(",");
		btBankAccApp.setCurId("");
		btBankAccApp.setBusType(BtBankAccConstant.BUSTYPE_1);
		if (EmptyUtils.isEmptyArray(curIds) || EmptyUtils.isBlank(curIdStr)) {
			//{0}币别不能为空{1} 需要用空字符串替换
			throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_CURID_NULL,"","");
		}
		if ("0".equals(btBankAccApp.getFlag())) {
			btBankAccApp.setStatus(BtBankAccConstant.STATUE_TEMP);
		} else {
			btBankAccApp.setStatus(BtBankAccConstant.STATUE_WAIT_CHECK);
		}
		if (EmptyUtils.isBlank(btBankAccApp.getId())) {
			btBankAccApp.setAccNo(this.btBankAccAppService.getSequence(SequenceEnum.ACC_NO));
		}
		btBankAccApp.setCreateBy(user.getLoginName());
		btBankAccApp.setCreateDate(DateUtils.getDate());
		btBankAccApp = this.btBankAccAppService.save(btBankAccApp);
		this.saveCur(curIds, btBankAccApp.getId());
		BtBankAccAppVO btBankAccAppVO = new BtBankAccAppVO();
		BeanUtils.copyNotNullProperties(btBankAccApp,btBankAccAppVO);
		btBankAccAppVO.setCorpName(corpService.get(btBankAccAppVO.getCorpId()).getName());
		if (BtBankAccConstant.STATUE_TEMP.equals(btBankAccApp.getStatus())) {
			//生成日志
			btBankAccMessageService.saveMessage(btBankAccApp.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_ADD, "", BtBankAccMessageConstant.BUS_TYPE_APP, user);
		} else {
			//生成日志
			btBankAccMessageService.saveMessage(btBankAccApp.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_SUBMIT, "", BtBankAccMessageConstant.BUS_TYPE_APP, user);
			 getStartHandler().handler(NisWorkFlowConstant.BT_BANKACC_APP, btBankAccApp.getId(), btBankAccAppVO, btBankAccApp.getCorpId(), user.getId());
		}
		// 消息提醒
		notificationService.complete(NotificationConstant.NETBANK.BANKACC_REQ, btBankAccApp.getId(), user.getId());
	}
	public void toModify(BtBankAccAppDTO btBankAccApp, ICurrentUser user) {
		BtBankAccAppDTO entity = this.btBankAccAppService.getById(btBankAccApp.getId());
		BeanUtils.copyNotNullProperties(btBankAccApp, entity);
		String curIdStr = btBankAccApp.getCurId();
		String[] curIds;
		if (!EmptyUtils.isBlank(curIdStr)) {
			curIds = curIdStr.split(",");
		} else {
			throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_CURID_NULL,"","");
		}
		if ("0".equals(btBankAccApp.getFlag())) {
			entity.setStatus(BtBankAccConstant.STATUE_TEMP);
		} else {
			entity.setStatus(BtBankAccConstant.STATUE_WAIT_CHECK);
		}
		entity.setCurId("");
		entity.setUpdateBy(user.getLoginName());
		entity.setUpdateDate(DateUtils.getDate());
		this.btBankAccAppService.saveOrUpdate(entity);
		// 先删除相关联的币别中间表
		this.btBankAccAppCurService.delByAppId(entity.getId());
		this.saveCur(curIds, entity.getId());
		if (BtBankAccConstant.STATUE_TEMP.equals(entity.getStatus())) {
			//生成日志
			btBankAccMessageService.saveMessage(entity.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_UPDATE, "", BtBankAccMessageConstant.BUS_TYPE_APP, user);
		} else {
			//生成日志
			btBankAccMessageService.saveMessage(entity.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_SUBMIT, "", BtBankAccMessageConstant.BUS_TYPE_APP, user);
			 getStartHandler().handler(NisWorkFlowConstant.BT_BANKACC_APP, entity.getId(), entity, entity.getCorpId(), user.getId());
		}
		notificationService.complete(NotificationConstant.NETBANK.BANKACC_REQ, entity.getId(), user.getId());
	}
	/**
	 * 删除
	 *
	 * @param btBankAccApp
	 */
	public void toDelte(BtBankAccAppDTO btBankAccApp, ICurrentUser user) {
		// 先删除相关联的币别中间表
//		List<BtBankAccAppCur> appCurList = this.btBankAccCommonService.listBtBankAccAppCurById(btBankAccApp.getId());
//		if (!EmptyUtils.isEmpty(appCurList)) {
//			for (BtBankAccAppCur cur : appCurList) {
//				this.getEntityDao().delete(this.getEntityDao().merge(cur));
//			}
//		}
		//生成日志
		btBankAccMessageService.saveMessage(btBankAccApp.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_DELETE, "", BtBankAccMessageConstant.BUS_TYPE_APP, user);
		notificationService.complete(NotificationConstant.NETBANK.BANKACC_REQ, btBankAccApp.getId(), user.getId());
		this.btBankAccAppService.deleteEntity(btBankAccApp);
	}
	/**
	 * 批量删除
	 *
	 * @param ids
	 */
	public void toAllDelte(String ids, ICurrentUser user) {
		for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
			BtBankAccAppDTO btBankAccApp = this.btBankAccAppService.getById(id);
			this.toDelte(btBankAccApp, user);
		}
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
		Integer messageStatus = workFlowResult.isPass() ? BtBankAccMessageConstant.MESSAGE_TYPE_CHECK_PASS : BtBankAccMessageConstant.MESSAGE_TYPE_CHECK_BACK;
		//生成日志
		btBankAccMessageService.saveMessage(bankAccApp.getId(), messageStatus, "", BtBankAccMessageConstant.BUS_TYPE_APP, user);
		// 消息提醒
		notificationService.send(bankAccApp.getId(), bankAccApp.getCorpId(), NotificationConstant.NETBANK.BANKACC_REQ_HANDLE, bankAccApp);
		if (NisWorkFlowConstant.BT_BANKACC_APP.equalsIgnoreCase(workFlowResult.getProcessDefinitionKey())) {
			// 初始化联行号信息 zuoy 2017-07-20
			this.bankService.saveBtBank(bankAccApp.getBankCode(), bankAccApp.getBankTypeId());
		}
	}
	public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
		String sign = "";
		if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("eqs_busType"))) {
			String type = dataTablesInput.getFormdata().get("eqs_busType").toString();
			if (BtBankAccConstant.BUSTYPE_1.equalsIgnoreCase(type)) {
				//申请
				sign = NisWorkFlowConstant.BT_BANKACC_APP;
			} else if (BtBankAccConstant.BUSTYPE_2.equalsIgnoreCase(type)) {
				//变更
				sign = NisWorkFlowConstant.BT_BANKACC_REV;
			} else if (BtBankAccConstant.BUSTYPE_3.equalsIgnoreCase(type)) {
				//注销
				sign = NisWorkFlowConstant.BT_BANKACC_DEL;
			}
		}
		// 银行账户开户申请审批页面,在使用创建日期查询记录时,结束时间需要加上23:59:59,因记录都包含准确的时分秒
		if (!EmptyUtils.isEmpty(dataTablesInput.getFormdata().get("led_createDate"))) {
			StringBuilder ledCreateDate = new StringBuilder(dataTablesInput.getFormdata().get("led_createDate").toString());
			if (ledCreateDate.toString().length() == 10) {
				ledCreateDate.append(" 23:59:59");
				dataTablesInput.getFormdata().put("led_createDate", ledCreateDate.toString());
			}
		}
		WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(sign, dataTablesInput);
		List<String> businessIdList = workFlowBusiness.getBusinessIdList();
		String businessSql = "";
		if(!EmptyUtils.isEmpty(businessIdList)){
			businessSql = SqlUtil.getInSql(businessIdList, "acc.id", true);
		}else {
			return new DataTablesOutput(dataTablesInput.getDraw(), new ArrayList<>(), 0, 0);

		}
		DataTablesOutput output = this.btBankAccAppService.dataTableApprove(dataTablesInput, businessSql);
		btBankAccCommonService.setCurName(output);
		return workFlowBusiness.merge(output);
	}
	/**
	 * 保存币别
	 *
	 * @param curIds
	 * @param id
	 */
	public void saveCur(String[] curIds, String id) {
		this.btBankAccAppCurService.saveByAppId(id, curIds);
	}
	@Override
	public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
		if(StringUtils.isNotEmpty(corpId)){
			List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<CorpVerifyErrorDTO>();
			//单位有在办的银行账户信息，不允许禁用！
			int num = this.btBankAccAppService.getBankAccAppCountByCorpId(corpId);
			if(num>0){
				verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
						SysConfigConstant.Module.NIS,
						SysConfigConstant.BusType.NIS_BANKACC,
						multiLangServiceI.getLang(CorpVerifyMessageConstant.NIS_VALID_DISABLE_BANKACC_ERROR)));
			}
			return verifyErrorDTOS;
		}
		return null;
	}
}
