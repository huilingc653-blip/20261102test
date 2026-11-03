package com.bytter.biz.budget.spi.service;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.bytter.biz.budget.spi.dto.FbsBudgetProcessCommonDTO;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.dto.FbsBudgetProjectDetailDTO;
import com.bytter.biz.budget.spi.vo.NisWageVO;
import com.bytter.core.base.entity.ICurrentUser;
//import io.seata.rm.tcc.api.BusinessActionContext;
//import io.seata.rm.tcc.api.BusinessActionContextParameter;
//import io.seata.rm.tcc.api.LocalTCC;
//import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * 计划执行接口
 * @author huangx
 * @since 2018-03-01
 */
public interface IFbsBudgetProcessService {
	
	/**
	 * 写入计划执行
	 * 业务新增/编辑，调用计划执行写入接口
	 * 业务【暂存】，写入计划执行状态为0-暂存
	 * 业务【提交】，写入计划执行状态为11-在途
	 * @param list 计划执行集合
	 * @param user 用户
	 */
	void writeBudgetProcess(List<FbsBudgetProcessDTO> list, ICurrentUser user);

	/**
	 * TCC：新增
	 * name = 全局唯一
	 * 不是 TM，不要有 BusinessActionContext actionContext 参数
	 */
	/*void prepareSave(
					   List<FbsBudgetProcessDTO> list ,
					   List<FbsBudgetProcessDTO> resetDTOList ,
					   String billNo ,
					   ICurrentUser user);*/
//	boolean commitSave(BusinessActionContext actionContext);
//	boolean rollbackSave(BusinessActionContext actionContext);
	
	/**
	 * 更新计划执行
	 * 业务【审批打回】，更新计划执行状态为-1-打回
	 * 业务【审批通过】，更新计划执行状态为95-占用
	 * @param businessName	执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billNo 业务单号
	 * @param status 状态
	 * @param user 用户
	 */
	void updateBudgetProcess(String businessName, String processSysname, String billNo, Integer status, ICurrentUser user);

	/**
	 * TCC：更新
	 * name = 全局唯一
	 * 不是 TM，不要有 BusinessActionContext actionContext 参数
	 */
	/*void prepareUpdate(
						 String billNo,
						 Integer status,
						 ICurrentUser user);
	boolean commitUpdate(BusinessActionContext actionContext);
	boolean rollbackUpdate(BusinessActionContext actionContext);*/


	/**
	 * 更新计划执行（票据专用）
	 * 业务【审批打回】，更新计划执行状态为-1-打回
	 * 业务【审批通过】，更新计划执行状态为95-占用
	 * @param businessName	执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billId 业务单ID
	 * @param status 状态
	 * @param user 用户
	 */
	void updateBudgetProcessForBill(String businessName, String processSysname, String billId, Integer status, ICurrentUser user);
	
	/**
	 * 释放计划执行
	 * 业务逆向流程做打回/退回，调用计划执行释放接口
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billNo         业务单号
	 */
	void releaseBudgetProcess(String businessName, String processSysname, String billNo);
	
	/**
	 * 释放计划执行（票据专用）
	 * 业务逆向流程做打回/退回，调用计划执行释放接口
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billId         业务单ID
	 */
	void releaseBudgetProcessForBill(String businessName, String processSysname, String billId);
	/**
	 * 删除计划执行
	 * 业务【删除】，调用计划执行删除接口
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billNo         业务单号
	 */
	void deleteBudgetProcess(String businessName, String processSysname, String billNo);
	
	/**
	 * 删除计划执行（票据专用）
	 * 业务【删除】，调用计划执行删除接口
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billId         业务单ID
	 */
	void deleteBudgetProcessForBill(String businessName, String processSysname, String billId);
	/**
	 * 获取业务单据计划执行信息
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billNo         业务单据号
	 * @return 计划执行集合
	 */
	List<FbsBudgetProcessDTO> getBudgetProcessByBillNo(String businessName, String processSysname, String billNo);
	/**
	 * 获取业务单据计划执行信息（票据专用）
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billId         业务单ID
	 * @return 计划执行集合
	 */
	List<FbsBudgetProcessDTO> getBudgetProcessByBillNoForBill(String businessName, String processSysname, String billId);
	
	/**
	 * 单位是否上报资金计划
	 * @param corpId 单位ID
	 * @return true/false
	 */
	boolean corpCtlBudget(String corpId);
	/**
	 * 判断是否有科目计划执行
	 * @param corpId
	 * @param itemCode
	 * @param wishDate
	 * @return
	 */
	boolean isCorpHaveItemCode(String corpId,String itemCode,Date wishDate);
	/**
	 * 获取业务单据计划执行信息(多个业务单合计执行金额)
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billNo         业务单据号
	 * @return 计划执行集合
	 */
	List<FbsBudgetProcessDTO> getSumBudgetProcessByBillNo(String businessName, String processSysname, List<String> billNo);
    /**
     * 更新计划执行的执行日期
     * 业务【审批打回】，更新计划执行状态为-1-打回
     * 业务【审批通过】，更新计划执行状态为95-占用
     * @param businessName	执行业务名称
     * @param processSysname 执行系统名称
     * @param billNo 业务单号
     * @param processDate 执行日期
     * @param user 用户
     */
    void updateBudgetProcessDate(String businessName, String processSysname, String billNo, Date processDate, ICurrentUser user);
    /**
            * 批量写入资金计划
     * @param nisWageList
     * @param user
     */
    void writeBatchBudgetProcess(List<NisWageVO> nisWageList, ICurrentUser user);

	BigDecimal getNisBillBudgetProcessMoneyByItem(String corpId,String billNo,Date processDate,String itemCode, FbsBudgetProjectDetailDTO itemDetailDTO);
}
