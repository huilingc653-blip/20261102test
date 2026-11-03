package com.bytter.biz.budget.service.impl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.alibaba.fastjson2.JSONArray;
import com.bytter.biz.budget.spi.dto.FbsBudgetProjectDetailDTO;
import com.bytter.biz.budget.vo.FbsBudgetProjectDetailVO;
import com.bytter.core.base.entity.ShiroUser;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.constant.NetbankConstant;
//import io.seata.rm.tcc.api.BusinessActionContext;
//import io.seata.rm.tcc.api.BusinessActionContextParameter;
//import io.seata.rm.tcc.api.LocalTCC;
//import io.seata.rm.tcc.api.TwoPhaseBusinessAction;
//import io.seata.rm.tcc.interceptor.ActionContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bytter.biz.budget.entity.FbsBudgetProcess;
import com.bytter.biz.budget.service.FbsBudgetProcessService;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.budget.spi.vo.NisWageVO;
import com.bytter.biz.budget.vo.FbsBudgetProcessVO;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;

import javax.annotation.Resource;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

/**
 * @author zhangxb
 * @date 2017/5/22 0022.
 */
//@LocalTCC
@Service("fbsBudgetProcessServiceImpl")
//@Transactional
@Slf4j
@DubboService
public class FbsBudgetProcessServiceImpl implements IFbsBudgetProcessService {
	@Autowired
	private FbsBudgetProcessService fbsBudgetProcessService;
	/**
	 * 写入计划执行
	 *
	 * @param list
	 */
	@Override
	public void writeBudgetProcess(List<FbsBudgetProcessDTO> list, ICurrentUser user) {
		List<FbsBudgetProcess> fbpList = BeanUtils.copyListProperties(list, FbsBudgetProcess.class);
		fbsBudgetProcessService.writeBudgetProcess(fbpList, user);
	}

	/**
	 * TCC：更新
	 * name = 全局唯一
	 */
	/*@TwoPhaseBusinessAction(name = "IFbsBudgetProcessService-save", commitMethod = "commitSave", rollbackMethod = "rollbackSave", useTCCFence = true)
	@Override
	public void prepareSave( @BusinessActionContextParameter(paramName = "list") List<FbsBudgetProcessDTO> list ,
							 @BusinessActionContextParameter(paramName = "resetDTOList") List<FbsBudgetProcessDTO> resetDTOList ,
							 @BusinessActionContextParameter(paramName = "billNo") String billNo ,
							 @BusinessActionContextParameter(paramName = "user") ICurrentUser user) {
		log.info("FbsBudgetProcessServiceImpl-prepareSave");
		fbsBudgetProcessService.deleteBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, billNo);
	}

	@Override
	public boolean commitSave(BusinessActionContext actionContext) {
		log.info("FbsBudgetProcessServiceImpl-commitSave");
        try {
			ShiroUser user = actionContext.getActionContext("user", ShiroUser.class);
			UserContextHelper.helper().setUser(user);
            UserContextHelper.helper().setTTLTenant(user.getTenantId());
			JSONArray jsonArray = actionContext.getActionContext("list", JSONArray.class);
			List<FbsBudgetProcessDTO> list = jsonArray.toList(FbsBudgetProcessDTO.class);
			List<FbsBudgetProcess> fbpList = BeanUtils.copyListProperties(list, FbsBudgetProcess.class);
			List<FbsBudgetProcess> budgetProcessByBillNo = fbsBudgetProcessService.getBudgetProcessByBillNo(fbpList.get(0).getBusinessName(), fbpList.get(0).getProcessSysname(), fbpList.get(0).getBillNo());
			if(EmptyUtils.isEmpty(budgetProcessByBillNo)){
				fbsBudgetProcessService.writeBudgetProcess(fbpList, user);
			}

			String sysVoucherCode = actionContext.getActionContext("sysVoucherCode", String.class);
			//获取系统结算方式
			//FcVoucherTypeVO voucherType = fcVoucherTypeService.getByIdUnion(nisBillhead.getVoucherTypeId());
			// 应付票据不修改资金计划
			if (!NetbankConstant.PAY_TYPE_49.equals(sysVoucherCode)) {
				//计划执行状态同步更新;
				this.fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, fbpList.get(0).getBillNo(), NetbankConstant.STATUE_CHECK, user);
			}

        } finally {
			UserContextHelper.helper().clearTTLTenant();
        }
        return true;
	}

	@Override
	public boolean rollbackSave(BusinessActionContext actionContext) {
		log.info("FbsBudgetProcessServiceImpl-rollbackSave");
        try {
			ShiroUser user = actionContext.getActionContext("user", ShiroUser.class);
			UserContextHelper.helper().setUser(user);
			UserContextHelper.helper().setTTLTenant(user.getTenantId());
			JSONArray jsonArray = actionContext.getActionContext("resetDTOList", JSONArray.class);
			List<FbsBudgetProcessDTO> resetDTOList = jsonArray.toList(FbsBudgetProcessDTO.class);
            if (CollectionUtils.isNotEmpty(resetDTOList)){
//			List<FbsBudgetProcess> resetList = JSON.parseArray(jsonString, FbsBudgetProcess.class);
                List<FbsBudgetProcess> resetList = BeanUtils.copyListProperties(resetDTOList, FbsBudgetProcess.class);
                fbsBudgetProcessService.writeBudgetProcess(resetList, user);
            }
        } finally {
			UserContextHelper.helper().clearTTLTenant();
        }
        return true;
	}
*/
	/**
	 * 更新计划执行
	 * 
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billNo         业务单据号
	 * @param status		  更新状态
	 */
	@Override
	public void updateBudgetProcess(String businessName, String processSysname, String billNo, Integer status, ICurrentUser user) {
		fbsBudgetProcessService.updateBudgetProcess(businessName, processSysname, billNo, status, user);
	}

	/**
	 * TCC：更新
	 * name = 全局唯一
	 */
	/*@TwoPhaseBusinessAction(name = "IFbsBudgetProcessService-update", commitMethod = "commitUpdate", rollbackMethod = "rollbackUpdate", useTCCFence = true)
	@Override
	public void prepareUpdate( @BusinessActionContextParameter(paramName = "billNo") String billNo,
							   @BusinessActionContextParameter(paramName = "status") Integer status,
							   @BusinessActionContextParameter(paramName = "user") ICurrentUser user) {
		log.info("FbsBudgetProcessServiceImpl-prepareUpdate");
		//这个比较特殊，prepare 阶段可以做前置校验，不修改数据
	}

	@Override
	public boolean commitUpdate(BusinessActionContext actionContext) {
		log.info("FbsBudgetProcessServiceImpl-commitUpdate");
		ICurrentUser user = actionContext.getActionContext("user", ICurrentUser.class);
		String billNo = actionContext.getActionContext("billNo", String.class);
		Integer status = actionContext.getActionContext("status", Integer.class);
		fbsBudgetProcessService.updateBudgetProcess(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, billNo, status, user);
		return true;
	}

	@Override
	public boolean rollbackUpdate(BusinessActionContext actionContext) {
		log.info("FbsBudgetProcessServiceImpl-rollbackUpdate");
		return true;
	}*/

	/**
	 * 释放计划执行
	 *
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billNo         业务单据号
	 * @return
	 */
	@Override
	public void releaseBudgetProcess(String businessName, String processSysname, String billNo) {
		fbsBudgetProcessService.releaseBudgetProcess(businessName, processSysname, billNo);
	}
	/**
	 * 删除计划执行
	 *
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billNo         业务单据号
	 * @return
	 */
	@Override
	public void deleteBudgetProcess(String businessName, String processSysname, String billNo) {
		fbsBudgetProcessService.deleteBudgetProcess(businessName, processSysname, billNo);
	}
	/**
	 * 获取业务单据计划执行信息
	 *
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billNo         业务单据号
	 * @return
	 */
	@Override
	public List<FbsBudgetProcessDTO> getBudgetProcessByBillNo(String businessName, String processSysname, String billNo) {
		List<FbsBudgetProcessDTO> fbpList = new ArrayList<FbsBudgetProcessDTO>();
		List<FbsBudgetProcess> list = fbsBudgetProcessService.getBudgetProcessByBillNo(businessName, processSysname, billNo);
		if (!EmptyUtils.isEmptyList(list)) {
			fbpList = BeanUtils.copyListProperties(list, FbsBudgetProcessDTO.class);
		}
		return fbpList;
	}
	/**
	 * 单位是否控制资金计划
	 * true/false
	 * @param corpId
	 * @return
	 */
	@Override
	public boolean corpCtlBudget(String corpId) {
		return this.fbsBudgetProcessService.corpCtlBudget(corpId);
	}
	/**
	 * 判断是否有科目计划执行
	 * @param corpId
	 * @param itemCode
	 * @param wishDate
	 * @return
	 */
	@Override
	public boolean isCorpHaveItemCode(String corpId, String itemCode,Date wishDate) {
		return this.fbsBudgetProcessService.isCorpHaveItemCode(corpId,itemCode,wishDate);
	}
	/**
	 * 释放计划执行（票据专用）
	 * 业务逆向流程做打回/退回，调用计划执行释放接口
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billId         业务单ID
	 * @return
	 */
	@Override
	public void updateBudgetProcessForBill(String businessName, String processSysname, String billId, Integer status, ICurrentUser user) {
		List<FbsBudgetProcess> list = fbsBudgetProcessService.getBudgetProcessByBillId(businessName, processSysname, billId);
		if (!EmptyUtils.isEmptyList(list)) {
			for(FbsBudgetProcess fbsBudgetProcess :list){
				fbsBudgetProcess.setStatus(status);
				fbsBudgetProcessService.update(fbsBudgetProcess, user);
			}
		}
	}
	/**
	 * 释放计划执行（票据专用）
	 * 业务逆向流程做打回/退回，调用计划执行释放接口
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billId         业务单ID
	 * @return
	 */
	@Override
	public void releaseBudgetProcessForBill(String businessName, String processSysname, String billId) {
		fbsBudgetProcessService.releaseBudgetProcessByBillId(businessName, processSysname, billId);
	}
	/**
	 * 删除计划执行（票据专用）
	 * 业务【删除】，调用计划执行删除接口
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billId         业务单ID
	 * @return
	 */
	@Override
	public void deleteBudgetProcessForBill(String businessName, String processSysname, String billId) {
		fbsBudgetProcessService.deleteBudgetProcessByBillId(businessName, processSysname, billId);
	}
	/**
	 * 获取业务单据计划执行信息（票据专用）
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billId         业务单ID
	 * @return
	 */
	@Override
	public List<FbsBudgetProcessDTO> getBudgetProcessByBillNoForBill(String businessName, String processSysname,
			String billId) {
		List<FbsBudgetProcessDTO> fbpList = new ArrayList<FbsBudgetProcessDTO>();
		List<FbsBudgetProcess> list = fbsBudgetProcessService.getBudgetProcessByBillId(businessName, processSysname, billId);
		if (!EmptyUtils.isEmptyList(list)) {
			fbpList = BeanUtils.copyListProperties(list, FbsBudgetProcessDTO.class);
		}
		return fbpList;
	}
	/**
	 * 获取业务单据计划执行信息(多个业务单合计执行金额)
	 *
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billNo         业务单据号
	 * @return
	 */
	@Override
	public List<FbsBudgetProcessDTO> getSumBudgetProcessByBillNo(String businessName, String processSysname, List<String> billNo) {
		List<FbsBudgetProcessDTO> fbpList = new ArrayList<FbsBudgetProcessDTO>();
		List<FbsBudgetProcessVO> list = fbsBudgetProcessService.getSumBudgetProcessByBillNo(businessName, processSysname, billNo);
		if (!EmptyUtils.isEmptyList(list)) {
			fbpList = BeanUtils.copyListProperties(list, FbsBudgetProcessDTO.class);
		}
		return fbpList;
	}
	/**
	 * 更新计划的执行日期
	 *
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billNo         业务单据号
	 * @param processDate	执行日期
	 */
	@Override
	public void updateBudgetProcessDate(String businessName, String processSysname, String billNo, Date processDate, ICurrentUser user) {
		fbsBudgetProcessService.updateBudgetProcessDate(businessName, processSysname, billNo, processDate, user);
	}
	
	/**
	 * 批量写入执行计划
	 */
	@Override
	public void writeBatchBudgetProcess(List<NisWageVO> nisWageList, ICurrentUser user) {
		if(CollectionUtils.isNotEmpty(nisWageList)) {
			List<String> listWageNo = nisWageList.stream().map(NisWageVO::getWageNo).collect(Collectors.toList());
			fbsBudgetProcessService.deleteBatchBudgetProcess(BudgetConstant.BUSINESS_NAME_WAGE, BudgetConstant.PROCESS_SYSNAME_NIS, listWageNo);//finish
			List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();
			List<NisWageVO> listEmptyItemCode = new ArrayList<NisWageVO>();
			for(NisWageVO nisWageVO : nisWageList) {
				if (!EmptyUtils.isEmptyArray(nisWageVO.getItemCode())) {
					for (int i = 0; i < nisWageVO.getItemCode().length; i++) {
						FbsBudgetProcessDTO fbsBudgetProcessDTO = this.bulidFbsBudgetProcessDTO(nisWageVO, user);//只是一个转换，无需优化
						fbsBudgetProcessDTO.setItemCode(nisWageVO.getItemCode()[i]);
						fbsBudgetProcessDTO.setItemName(nisWageVO.getItemName()[i]);
						fbsBudgetProcessDTO.setProcessMoney(nisWageVO.getProcessMoney()[i]);
						if(fbsBudgetProcessDTO.getExchangeRate()!=null) {
							fbsBudgetProcessDTO.setExchangeRate(nisWageVO.getExchangeRate()[i]);
							fbsBudgetProcessDTO.setProcessCurMoney(nisWageVO.getProcessCurMoney()[i]);
						}
						list.add(fbsBudgetProcessDTO);
					}
					
				} else {
					listEmptyItemCode.add(nisWageVO);
				}
		  }
		  this.writeBudgetProcess(list, user);//批量写入
		  //计划执行状态同步更新（批量提交） //finish
		  this.fbsBudgetProcessService.updateBatchBudgetProcess(BudgetConstant.BUSINESS_NAME_WAGE, BudgetConstant.PROCESS_SYSNAME_NIS,listEmptyItemCode, user);	
			
	   }
	}

	@Override
	public BigDecimal getNisBillBudgetProcessMoneyByItem(String corpId,String billNo,Date processDate,String itemCode, FbsBudgetProjectDetailDTO itemDetailDTO) {
		FbsBudgetProjectDetailVO itemDetailVO = new FbsBudgetProjectDetailVO();
		BeanUtils.copyProperties(itemDetailDTO, itemDetailVO);
		BigDecimal processMoney = this.fbsBudgetProcessService.getBudgetProcessMoneyByDetail(corpId, itemCode, processDate, billNo, "", itemDetailVO);
		return processMoney;
	}

	/**
	 * 构建计划执行
	 *
	 * @param nisWage
	 * @param user
	 * @return
	 */
	private FbsBudgetProcessDTO bulidFbsBudgetProcessDTO(NisWageVO nisWage, ICurrentUser user) {
		FbsBudgetProcessDTO fbsBudgetProcessDTO = new FbsBudgetProcessDTO();
		fbsBudgetProcessDTO.setCreateBy(user.getLoginName());
		fbsBudgetProcessDTO.setCreateDate(DateUtils.getDate());
		fbsBudgetProcessDTO.setUpdateBy(user.getLoginName());
		fbsBudgetProcessDTO.setUpdateDate(DateUtils.getDate());
		fbsBudgetProcessDTO.setStatus(nisWage.getStatus());
		fbsBudgetProcessDTO.setBillNo(nisWage.getWageNo());
		fbsBudgetProcessDTO.setBillId(nisWage.getId());
		fbsBudgetProcessDTO.setProcessDate(nisWage.getWishPayday());
		fbsBudgetProcessDTO.setCorpId(nisWage.getCorpId());
		fbsBudgetProcessDTO.setCurId(nisWage.getCurId());
		fbsBudgetProcessDTO.setPayerAcc(nisWage.getPayerAcc());
		fbsBudgetProcessDTO.setPurpose(nisWage.getAddWord());
		fbsBudgetProcessDTO.setWishPayday(nisWage.getWishPayday());
		fbsBudgetProcessDTO.setBusinessName(BudgetConstant.BUSINESS_NAME_WAGE);
		fbsBudgetProcessDTO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_NIS);
		return fbsBudgetProcessDTO;
	}
	
}
