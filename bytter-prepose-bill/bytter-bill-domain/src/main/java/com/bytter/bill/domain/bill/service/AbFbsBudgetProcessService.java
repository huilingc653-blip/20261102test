package com.bytter.bill.domain.bill.service;
import com.bytter.bill.domain.bill.entity.AbAccrecords;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * 计划执行接口服务类
 * @author zhenghj
 *
 */
@Service("abFbsBudgetProcessService")
@Transactional
public class AbFbsBudgetProcessService extends BaseService<String, AbAccrecords> {
	
	@DubboReference(check = false)
	private IFbsBudgetProcessService fbsBudgetProcessService;
	@Override
	public BaseDao<String, AbAccrecords> getEntityDao() {
		return null;
	}
	/**
	 * 获取业务单据计划执行信息（票据专用）
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billId         业务单ID
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<FbsBudgetProcessDTO> getBudgetProcessByBillNoForBill(String businessName, String processSysname, String billId){
		if(fbsBudgetProcessService==null){
			return null;
		}
		return fbsBudgetProcessService.getBudgetProcessByBillNoForBill(businessName,processSysname,billId);
	}
	/**
	 * 释放计划执行
	 * 业务逆向流程做打回/退回，调用计划执行释放接口
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billNo         业务单据号
	 * @return
	 */
	public void releaseBudgetProcess(String businessName, String processSysname, String billNo){
		if(fbsBudgetProcessService==null){
			return;
		}
		fbsBudgetProcessService.releaseBudgetProcess(businessName,processSysname,billNo);
	}
	/**
	 * 释放计划执行（票据专用）
	 * 业务逆向流程做打回/退回，调用计划执行释放接口
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billId         业务单ID
	 * @return
	 */
	public void releaseBudgetProcessForBill(String businessName, String processSysname, String billId){
		if(fbsBudgetProcessService==null){
			return;
		}
		fbsBudgetProcessService.releaseBudgetProcessForBill(businessName,processSysname,billId);
	}
	/**
	 * 单位是否上报资金计划
	 * true/false
	 * @param corpId
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean corpCtlBudget(String corpId){
		if(fbsBudgetProcessService==null){
			return false;
		}
		return fbsBudgetProcessService.corpCtlBudget(corpId);
	}
	/**
	 * 写入计划执行
	 * 业务新增/编辑，调用计划执行写入接口
	 * 业务【暂存】，写入计划执行状态为0-暂存
	 * 业务【提交】，写入计划执行状态为11-在途
	 * @param list
	 */
	public void writeBudgetProcess(List<FbsBudgetProcessDTO> list, ICurrentUser user){
		if(fbsBudgetProcessService==null){
			return;
		}
		fbsBudgetProcessService.writeBudgetProcess(list,user);
	}
	/**
	 * 删除计划执行（票据专用）
	 * 业务【删除】，调用计划执行删除接口
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billId         业务单ID
	 * @return
	 */
	public void deleteBudgetProcessForBill(String businessName, String processSysname, String billId){
		if(fbsBudgetProcessService==null){
			return;
		}
		fbsBudgetProcessService.deleteBudgetProcessForBill(businessName,processSysname,billId);
	}
	/**
	 * 更新计划执行（票据专用）
	 * 业务【审批打回】，更新计划执行状态为-1-打回
	 * 业务【审批通过】，更新计划执行状态为95-占用
	 */
	public void updateBudgetProcessForBill(String businessName, String processSysname, String billId, Integer status, ICurrentUser user){
		if(fbsBudgetProcessService==null){
			return;
		}
		fbsBudgetProcessService.updateBudgetProcessForBill(businessName,processSysname,billId,status,user);
	}
	/**
	 * 更新计划执行
	 * 业务【审批打回】，更新计划执行状态为-1-打回
	 * 业务【审批通过】，更新计划执行状态为95-占用
	 */
	public void updateBudgetProcess(String businessName, String processSysname, String billNo, Integer status, ICurrentUser user){
		if(fbsBudgetProcessService==null){
			return;
		}
		fbsBudgetProcessService.updateBudgetProcess(businessName, processSysname, billNo, status, user);
	}
	/**
	 * 删除计划执行
	 * 业务【删除】，调用计划执行删除接口
	 * @param businessName   执行业务名称
	 * @param processSysname 执行系统名称
	 * @param billNo         业务单据号
	 * @return
	 */
	public void deleteBudgetProcess(String businessName, String processSysname, String billNo){
		if(fbsBudgetProcessService==null){
			return;
		}
		fbsBudgetProcessService.deleteBudgetProcess(businessName, processSysname, billNo);
	}
}
