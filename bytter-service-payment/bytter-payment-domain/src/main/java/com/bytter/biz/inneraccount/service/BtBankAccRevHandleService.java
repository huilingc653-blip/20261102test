package com.bytter.biz.inneraccount.service;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.inneraccount.entity.BisVirtualAccExc;
import com.bytter.biz.inneraccount.entity.BtBankAccVirtual;
import com.bytter.biz.bdlink.api.constant.BtBankAccMessageConstant;
import com.bytter.biz.netbank.entity.BtBankAccUse;
import com.bytter.biz.netbank.service.BtBankAccUseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.workflow.bpm.IWorkFlowListener;
import com.bytter.workflow.bpm.annotation.WorkFlow;
import com.bytter.workflow.dto.IWorkFlowResult;
import com.bytter.workflow.dto.WorkFlowBusiness;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.constant.*;
import com.bytter.core.utils.*;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.BisBifInitDTO;
import com.bytter.system.api.dto.BtBankAccAppDTO;
import com.bytter.system.api.dto.BtBankAccCurDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.enums.BankInfoMessageEnum;
import com.bytter.system.api.vo.BtBankAccCurVO;
import com.bytter.system.api.vo.BtBankAccVO;
import com.bytter.system.api.vo.BtBankAccVirtualVO;
import com.bytter.workflow.api.IWorkFlowService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Service
 * @Description: 银行账号变更
 * @date 2017-05-23 17:37:39
 */
@WorkFlow(NisWorkFlowConstant.BT_BANKACC_ACC_REV_HANDLE)
@DubboService(interfaceClass = IWorkFlowListener.class, group = NisWorkFlowConstant.BT_BANKACC_ACC_REV_HANDLE)
@Service("btBankAccRevHandleService")
@Transactional
public class BtBankAccRevHandleService implements IWorkFlowListener {
	@DubboReference(check = false)
	private ICorpService corpService;
	@Autowired
	private BtBankAccMessageService btBankAccMessageService;
	@DubboReference(check = false)
	private IBisBifInitService bisBifInitService;
	@DubboReference(check = false)
	private INotificationService notificationService;
	@DubboReference(check = false)
	private IBtBankAccAppService btBankAccAppService;
	@DubboReference(check = false)
	private IBtBankAccRevService btBankAccRevService;
	@DubboReference(check = false)
	private IBtBankAccService btBankAccService;
	@DubboReference(check = false)
	private IBtBankAccAppCurService btBankAccAppCurService;
	@DubboReference(check = false)
	private IBtBankAccCurService btBankAccCurService;
	@DubboReference(check = false)
	private IBtBankAccCommonService btBankAccCommonService;
	@Autowired
	private IWorkFlowService workFlowService;
	@Autowired
	private BtBankAccVirtualService btBankAccVirtualService;
	@Autowired
	private BisVirtualAccExcService bisVirtualAccExcService;
	@Autowired
	private BtBankAccUseService btBankAccUseService;
	/**
	 * 显示列表
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user) {
		StringBuilder sql = new StringBuilder();
		String corpAuthSql = SqlUtil.getInSql(
				corpService.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BTBANKACCREVHANDLE_QUERY, user.getId()), "acc.corp_id",
				true);
		sql.append(" and ").append(corpAuthSql);
		sql.append(" and (acc.status = " + BtBankAccConstant.STATUE_CHECK + ") ");
		DataTablesOutput output = btBankAccAppService.getDataTablesUnion(dataTablesInput, sql.toString(), BtBankAccConstant.BUSTYPE_2);
		this.btBankAccCommonService.setCurName(output);
		List<BtBankAccAppDTO> data = output.getData();
		List<BtBankAccUse> btBankAccUses = btBankAccUseService.listAll();
		for (BtBankAccAppDTO entity : data){
			// 设置账号用途
			String accUseName = "";
			if (ObjectUtil.isNotEmpty(entity.getAccUseCode())){
				for (BtBankAccUse accUse : btBankAccUses){
					if(entity.getAccUseCode().contains(accUse.getUseCode())){
						if("".equals(accUseName)) {
							accUseName = accUse.getUseName();
						}else{
							accUseName += ","+accUse.getUseName();
						}
					}
				}
			}
			entity.setAccUseName(accUseName);
		}
		return output;
	}
	public void toModify(BtBankAccVO btBankAcc, ICurrentUser user) {
		BtBankAccDTO acc = this.btBankAccCommonService.getBtBankAcc(btBankAcc.getId());
		if (btBankAccCommonService.isBankAcc(btBankAcc.getBankAcc()) && !(btBankAcc.getBankAcc().equals(acc.getBankAcc()))) {
			throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BANKACC_UNIQUE);
		}
		BeanUtils.copyNotNullProperties(btBankAcc, acc);
		String curIdStr = acc.getCurId();
		if (EmptyUtils.isEmpty(curIdStr)) {
			throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_CURID_NULL,"","");
		}
		String[] curIds = curIdStr.split(",");
		if (curIds.length < 1) {
			throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_CURID_NULL,"","");
		}
		if (!EmptyUtils.isBlank(btBankAcc.getBifCode())) {
			BisBifInitDTO bisBifInitDTO = this.bisBifInitService.getObjectById(btBankAcc.getBifCode());
			if (!EmptyUtils.isEmpty(bisBifInitDTO) && !bisBifInitDTO.getBankTypeId().equalsIgnoreCase(btBankAcc.getBankTypeId())) {
				throw new ParameterException(BankInfoMessageEnum.BASIC_BUSINESS_MESSAGE_BIFINIT_EQ_BANKTYPEID);
			}
		}
		/**
		 * 更新银行账号申请表 开始
		 */
		BtBankAccAppDTO app = this.btBankAccCommonService.getBtBankAccApp(btBankAcc.getAppId());
		BtBankAccAppDTO t = new BtBankAccAppDTO();
		BeanUtils.copyNotNullProperties(app, t);
		t.setStatus(BtBankAccConstant.STATUE_CANCELLATION);
		t.setId(btBankAcc.getAppId());
		t.setCurId("");
		t.setUpdateBy(user.getLoginName());
		t.setUpdateDate(DateUtils.getDate());
		this.btBankAccAppService.saveOrUpdate(t);
		//生成日志
		btBankAccMessageService.saveMessage(t.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_UPDATE, "", BtBankAccMessageConstant.BUS_TYPE_REVHANDLE, user);
		// 消息提醒
		notificationService.complete(NotificationConstant.NETBANK.BANKACC_REV_HANDLE, t.getId(), user.getId());
//		notificationService.sendToUserByLoginName(t.getId(), t.getCreateBy(), NotificationConstant.NETBANK.BANKACC_REV_HANDLE_SUCCESS, t);
		/*for (int i = 0; i < curIds.length; i++) {
			BtBankAccAppCur accCur = new BtBankAccAppCur();
			if ("all".equals(curIds[i])) {
				continue;
			}
			BtBankAccAppCurVO cur = this.btBankAccCommonService.getBtBankAccAppCur(t.getId(), curIds[i]);
			if (EmptyUtils.isEmpty(cur)) {
				accCur.setAppId(t.getId());
				accCur.setCurId(curIds[i]);
				this.getEntityDao().save(accCur);
			}
		}*/
		this.btBankAccAppCurService.saveByAppId(t.getId(), curIds);
		/**
		 * 结束
		 */
		/**
		 * 更新银行账号表 开始
		 */
		acc.setCurId("");
		acc.setStatus(BtBankAccConstant.STATUE_WAIT_CHECK);
		btBankAccService.saveOrUpdateObj(acc);
		for (int i = 0; i < curIds.length; i++) {
			BtBankAccCurVO accCur = new BtBankAccCurVO();
			if ("all".equals(curIds[i])) {
				continue;
			}
			BtBankAccCurDTO cur = this.btBankAccCommonService.getBtBankAccCur(acc.getId(), curIds[i]);
			if (EmptyUtils.isEmpty(cur)) {
				accCur.setBankAccId(acc.getId());
				accCur.setCurId(curIds[i]);
				btBankAccCurService.save(accCur);
			}
		}
		//招行 虚拟户 线上办理 保存虚拟信息
		btBankAccVirtualService.deleteByBankAccId(btBankAcc.getId());
		if (BtBankAccConstant.ACCTYPE_09.equals(btBankAcc.getAccType()) && CommonConstant.YES.equals(btBankAcc.getIsOnlineHandle())
				&& BtBankAccConstant.BIS_BIF_CODE_CMB.equals(btBankAcc.getBifCode())) {
			if (!EmptyUtils.isEmptyList(btBankAcc.getBankAccVirtual())) {
				List<BtBankAccVirtualVO> listDtl = new ArrayList<BtBankAccVirtualVO>();
				for (BtBankAccVirtualVO bankAccVirtual : btBankAcc.getBankAccVirtual()) {
					if (StringUtils.isNotBlank(bankAccVirtual.getVirtualNo())) {
						bankAccVirtual.setBankAccId(btBankAcc.getId());
						listDtl.add(bankAccVirtual);
					}
				}
				List<BtBankAccVirtual> listentity = BeanUtils.copyListProperties(listDtl, BtBankAccVirtual.class);
				btBankAccVirtualService.batchSave(listentity);
			}
		}
		/**
		 * 结束
		 */
		//开启审批
		 getStartHandler().handler(NisWorkFlowConstant.BT_BANKACC_ACC_REV_HANDLE, t.getId(), t, t.getCorpId(), user.getId());
	}
	/**
	 * 打回
	 *
	 * @param id
	 */
	public void back(String id, ICurrentUser user) {
		BtBankAccAppDTO btbankaccapp = this.btBankAccAppService.getById(id);
		btbankaccapp.setStatus(-1);
		btbankaccapp.setStatus(BtBankAccConstant.STATUE_BACK);
		this.btBankAccAppService.saveOrUpdate(btbankaccapp);
		//生成日志
		btBankAccMessageService.saveMessage(btbankaccapp.getId(), BtBankAccMessageConstant.MESSAGE_TYPE_BACK, "", BtBankAccMessageConstant.BUS_TYPE_REV, user);
		// 消息提醒
		notificationService.complete(NotificationConstant.NETBANK.BANKACC_REV_HANDLE, id, user.getId());
		notificationService.sendToUserByLoginName(btbankaccapp.getId(), btbankaccapp.getCreateBy(), NotificationConstant.NETBANK.BANKACC_REV, btbankaccapp);
	}
	@Override
	public void flowCompleted(IWorkFlowResult workFlowResult) {
		ICurrentUser user = workFlowResult.getUser();
        BtBankAccAppDTO bankAccApp = this.btBankAccAppService.getById(workFlowResult.getBusinessKey());
		Integer appStatus = workFlowResult.isPass() ? BtBankAccConstant.STATUE_CANCELLATION : BtBankAccConstant.STATUE_CHECK;
        Integer status = workFlowResult.isPass() ? BtBankAccConstant.STATUE_CHECK : BtBankAccConstant.STATUE_BACK;
        bankAccApp.setStatus(appStatus);
        bankAccApp.setUpdateBy(user.getLoginName());
        bankAccApp.setUpdateDate(DateUtils.getDate());
        Integer messageStatus = workFlowResult.isPass() ? BtBankAccMessageConstant.MESSAGE_TYPE_CHECK_PASS : BtBankAccMessageConstant.MESSAGE_TYPE_CHECK_BACK;
		BtBankAccDTO bankAcc = this.btBankAccCommonService.getBtBankAccByBankAcc(bankAccApp.getBankAcc());
		bankAcc.setStatus(status);
		bankAcc.setUpdateBy(user.getLoginName());
		bankAcc.setUpdateDate(DateUtils.getDate());
		this.btBankAccCommonService.saveOrUpdateNew(bankAcc);
		this.btBankAccAppService.saveOrUpdate(bankAccApp);
		//生成日志
		btBankAccMessageService.saveMessage(bankAcc.getId(), messageStatus, "", BtBankAccMessageConstant.BUS_TYPE_REVHANDLE, user);
		btBankAccMessageService.saveMessage(bankAccApp.getId(), messageStatus, "", BtBankAccMessageConstant.BUS_TYPE_REVHANDLE, user);
		// 消息提醒
		notificationService.sendToUserByLoginName(bankAcc.getId(), bankAcc.getCreateBy(), NotificationConstant.NETBANK.BANKACC_REV_HANDLE_SUCCESS, bankAcc);
		//审批通过
		if (workFlowResult.isPass()) {
			//招行 虚拟户 线上办理
			if (BtBankAccConstant.ACCTYPE_09.equals(bankAcc.getAccType()) && CommonConstant.YES.equals(bankAcc.getIsOnlineHandle())
					&& BtBankAccConstant.BIS_BIF_CODE_CMB.equals(bankAcc.getBifCode())) {
				List<BtBankAccVirtual> listVirtual = btBankAccVirtualService.getListByBankAccId(bankAcc.getId());
				if (!EmptyUtils.isEmptyList(listVirtual)) {
					List<BisVirtualAccExc> listExc = new ArrayList<>();
					HashMap<String, BisVirtualAccExc> excMap = bisVirtualAccExcService.getBisVirtualAccExcMap(bankAcc.getId());
					for (BtBankAccVirtual bankAccVirtual:listVirtual) {
						BisVirtualAccExc exc = new BisVirtualAccExc();
						BeanUtils.copyNotNullProperties(bankAccVirtual, exc);
						exc.setId("");
						exc.setBankAcc(bankAcc.getId());
						exc.setReqDate(DateUtils.getDate());
						exc.setCreateBy(user.getLoginName());
						exc.setCreateDate(DateUtils.getDate());
						exc.setVoucherStat(BdlinkConstant.PRE_SEND);
						exc.setBifCode(bankAcc.getBifCode());
						if (excMap.containsKey(bankAccVirtual.getBankAccId()+CommonConstant.COMMA_SPLIT+bankAccVirtual.getVirtualNo())) {
							exc.setBusType("2"); //编辑
						} else {
							exc.setBusType("1"); //新增
						}
						listExc.add(exc);
					}
					bisVirtualAccExcService.batchSave(listExc);
				}
			}
		}
	}
	@Override
	public void flowStarted(IWorkFlowResult workFlowResult) {
	}
	public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput) {
		WorkFlowBusiness workFlowBusiness = workFlowService.getWorkFlowBusiness(NisWorkFlowConstant.BT_BANKACC_ACC_REV_HANDLE, dataTablesInput);
		List<String> businessIdList = workFlowBusiness.getBusinessIdList();
		String businessSql = "";
		if(!EmptyUtils.isEmpty(businessIdList)){
			businessSql = SqlUtil.getInSql(businessIdList, "acc.id", true);
		}
		DataTablesOutput output = this.btBankAccRevService.dataTableApprove(dataTablesInput, businessSql);
		this.btBankAccCommonService.setCurName(output);
		List<BtBankAccAppDTO> data = output.getData();
		List<BtBankAccUse> btBankAccUses = btBankAccUseService.listAll();
		for (BtBankAccAppDTO entity : data){
			// 设置账号用途
			String accUseName = "";
			if (ObjectUtil.isNotEmpty(entity.getAccUseCode())){
				for (BtBankAccUse accUse : btBankAccUses){
					if(entity.getAccUseCode().contains(accUse.getUseCode())){
						if("".equals(accUseName)) {
							accUseName = accUse.getUseName();
						}else{
							accUseName += ","+accUse.getUseName();
						}
					}
				}
			}
			entity.setAccUseName(accUseName);
		}
		return workFlowBusiness.merge(output);
	}
}
