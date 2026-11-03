package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.domain.billbank.pool.vo.PoolBillVoucherVO;
import com.bytter.bill.domain.billbank.pool.service.impl.BillPoolIntoApplyImpService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
/**
 * <br>
 * <b>功能：</b>票据入池申请服务接口<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-18 <br>
 */
@Controller
@RequestMapping("/billPool/intoApply")
public class BillPoolIntoApplyController extends BaseController {
	@Autowired
	BillPoolIntoApplyImpService billPoolIntoApplyImpService;
	
	/**
	 * <b>功能：</b>获取待发送票据入池申请数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	@PostMapping("/queryIntoApplyByBifCode")
	@ResponseBody
	public AjaxJson queryIntoApplyByBifCode(String bifCode, String reCode){
		List<PoolBillVoucherVO> voList = billPoolIntoApplyImpService.queryIntoApplyByBifCode(bifCode, reCode);
		return success(voList);
	}
	
	/**
	 * <b>功能：</b>获取等待结果票据入池数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	@PostMapping("/queryIntoApplyResultByBifCode")
	@ResponseBody
	public AjaxJson queryIntoApplyResultByBifCode(String bifCode, String reCode){
		List<PoolBillVoucherVO> voList = billPoolIntoApplyImpService.queryIntoApplyResultByBifCode(bifCode, reCode);
		return success(voList);
	}
	/**
	 * <b>功能：修改票据入池的指令状态</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param paramsVo 修改数据的参数
	 * @return
	 */
	@PostMapping("/updatePoolIntoApply")
	@ResponseBody
	public AjaxJson  updatePoolIntoApply(@RequestBody PoolBillVoucherVO paramsVo){
		 billPoolIntoApplyImpService.updatePoolIntoApply(paramsVo);
		return success();
	}
	
//
//	/**
//	 * <b>功能：</b>获取待发送再次票据入池申请数据<br>
//	 * <b>作者：</b>郑刚<br>
//	 * <b>日期：</b> 2020-02-18 <br>
//	 * @param bifCode 银行接口代码
//	 * @param reCode 银行返回代码
//	 * @return
//	 */
//	List<PoolBillVoucherVO> queryAgainIntoApplyByBifCode(String bifCode,String reCode);
//	
//	/**
//	 * <b>功能：</b>获取等待结果再次票据入池数据<br>
//	 * <b>作者：</b>郑刚<br>
//	 * <b>日期：</b> 2020-02-18 <br>
//	 * @param bifCode 银行接口代码
//	 * @param reCode 银行返回代码
//	 * @return
//	 */
//	List<PoolBillVoucherVO> queryAgainIntoApplyResultByBifCode(String bifCode,String reCode);
//
//	/**
//	 * <b>功能：修改再次票据入池的指令状态</b><br>
//	 * <b>作者：</b>郑刚<br>
//	 * <b>日期：</b> 2020-02-18 <br>
//	 * @param paramsVo 修改数据的参数
//	 * @return
//	 */
//	@PostMapping("/updatePoolIntoApply")
//	@ResponseBody
//	public AjaxJson updatePoolAgainIntoApply(PoolBillVoucherVO paramsVo){
//		billPoolIntoApplyImpService.updatePoolIntoApply(paramsVo);
//		return success();
//	}
	/**
	 * <b>功能：</b>获取待发送票据托管入池数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	@PostMapping("/queryIntoManagedByBifCode")
	@ResponseBody
	public AjaxJson queryIntoManagedByBifCode(String bifCode, String reCode){
		List<PoolBillVoucherVO> voList = billPoolIntoApplyImpService.queryIntoManagedByBifCode(bifCode, reCode);
		return success(voList);
	}
	
	/**
	 * <b>功能：</b>获取等待结果票据托管入池数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	@PostMapping("/queryIntoManagedResultByBifCode")
	@ResponseBody
	public AjaxJson queryIntoManagedResultByBifCode(String bifCode, String reCode){
		List<PoolBillVoucherVO> voList = billPoolIntoApplyImpService.queryIntoManagedResultByBifCode(bifCode, reCode);
		return success(voList);
	}
	/**
	 * <b>功能：修改票据托管入池的指令状态</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param paramsVo 修改数据的参数
	 * @return
	 */
	@PostMapping("/updatePoolIntoManaged")
	@ResponseBody
	public AjaxJson updatePoolIntoManaged(@RequestBody PoolBillVoucherVO paramsVo){
		billPoolIntoApplyImpService.updatePoolIntoManaged(paramsVo);
		return success();
	}
	/**
	 * <b>功能：</b>获取待发送票据质押入池数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	@PostMapping("/queryIntoPledgeByBifCode")
	@ResponseBody
	public AjaxJson  queryIntoPledgeByBifCode(String bifCode, String reCode){
		List<PoolBillVoucherVO> voList = billPoolIntoApplyImpService.queryIntoPledgeByBifCode(bifCode, reCode);
		return success(voList);
	}
	
	/**
	 * <b>功能：</b>获取等待结果票据质押入池数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	@PostMapping("/queryIntoPledgeResultByBifCode")
	@ResponseBody
	public AjaxJson queryIntoPledgeResultByBifCode(String bifCode, String reCode){
		List<PoolBillVoucherVO> voList = billPoolIntoApplyImpService.queryIntoPledgeResultByBifCode(bifCode, reCode);
		return success(voList);
	}
	/**
	 * <b>功能：修改票据质押入池的指令状态</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param paramsVo 修改数据的参数
	 * @return
	 */
	@PostMapping("/updatePoolIntoPledge")
	@ResponseBody
	public AjaxJson updatePoolIntoPledge(@RequestBody PoolBillVoucherVO paramsVo){
		billPoolIntoApplyImpService.updatePoolIntoPledge(paramsVo);
		return success();
	}
	
	/**
	 * <b>功能：</b>获取待发送删除票据数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	@PostMapping("/queryDeleteBillByBifCode")
	@ResponseBody
	public AjaxJson  queryDeleteBillByBifCode(String bifCode, String reCode){
		List<PoolBillVoucherVO> voList = billPoolIntoApplyImpService.queryDeleteBillByBifCode(bifCode, reCode);
		return success(voList);
	}
	
	/**
	 * <b>功能：</b>获取等待结果删除票据数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	@PostMapping("/queryDeleteBillResultByBifCode")
	@ResponseBody
	public AjaxJson queryDeleteBillResultByBifCode(String bifCode, String reCode){
		List<PoolBillVoucherVO> voList = billPoolIntoApplyImpService.queryDeleteBillResultByBifCode(bifCode, reCode);
		return success(voList);
	}
	/**
	 * <b>功能：修改删除票据的指令状态</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param paramsVo 修改数据的参数
	 * @return
	 */
	@PostMapping("/updateDeleteBill")
	@ResponseBody
	public AjaxJson updateDeleteBill(@RequestBody PoolBillVoucherVO paramsVo){
		billPoolIntoApplyImpService.updateDeleteBill(paramsVo);
		return success();
	}
}
