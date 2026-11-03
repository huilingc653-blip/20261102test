package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.domain.billbank.pool.vo.PoolBaseVO;
import com.bytter.bill.domain.billbank.pool.vo.PoolBillVoucherVO;
import com.bytter.bill.domain.billbank.pool.vo.PoolLimitVO;
import com.bytter.bill.domain.billbank.pool.service.impl.BillPoolLimitAllotImpService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
/**
 * <br>
 * <b>功能：</b>额度分配服务接口<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-17 <br>
 */
@Controller
@RequestMapping("/billPool/limitAllot")
public class BillPoolLimitAllotController extends BaseController {
	BillPoolLimitAllotImpService billPoolLimitAllotImpService;
	
	/**
	 * <b>功能：</b>获取待发送额度分配数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-17 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	@PostMapping("/queryLimitAllotByBifCode")
	@ResponseBody
	public AjaxJson queryLimitAllotByBifCode(String bifCode, String reCode){
		List<PoolLimitVO> voList = billPoolLimitAllotImpService.queryLimitAllotByBifCode(bifCode, reCode);
		return success(voList);
	}
	
	/**
	 * <b>功能：</b>获取等待结果额度数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-17 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	@PostMapping("/queryLimitAllotResultByBifCode")
	@ResponseBody
	public AjaxJson queryLimitAllotResultByBifCode(String bifCode, String reCode){
		List<PoolLimitVO> voList = billPoolLimitAllotImpService.queryLimitAllotResultByBifCode(bifCode, reCode);
		return success(voList);
	}
	/**
	 * <b>功能：修改额度的指令状态</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-17 <br>
	 * @param paramsVo 修改数据的参数
	 * @return
	 */
	@PostMapping("/updatePoolLimitAllot")
	@ResponseBody
	public AjaxJson updatePoolLimitAllot(@RequestBody PoolLimitVO paramsVo){
		billPoolLimitAllotImpService.updatePoolLimitAllot(paramsVo);
		return success();
	}
	/**
	 * <b>功能：</b>获取需要查询复核数据的票据池<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-18 <br>
	 * @param bifCode 银行接口代码
	 * @return 票据池信息
	 */
	@PostMapping("/queryCheckPoolByBifCode")
	@ResponseBody
	public AjaxJson queryCheckPoolByBifCode(String bifCode){
		List<PoolBaseVO> voList = billPoolLimitAllotImpService.queryCheckPoolByBifCode(bifCode);
		return success(voList);
	}
	/**
	 * <b>功能：对获取的待复核数据进行入库（业务中数据不替换，有失败数据和已完成数据进行替换）</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-17 <br>
	 * @param paramsVo 修改数据的参数
	 * @return
	 */
	@PostMapping("/savePoolLimitAllotCheck")
	@ResponseBody
	public AjaxJson savePoolLimitAllotCheck(@RequestBody List<PoolLimitVO> paramsVo){
		billPoolLimitAllotImpService.savePoolLimitAllotCheck(paramsVo);
		return success();
	}
	/**
	 * <b>功能：</b>获取待发送额度分配复核数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-17 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	@PostMapping("/queryLimitAllotCheckByBifCode")
	@ResponseBody
	public AjaxJson  queryLimitAllotCheckByBifCode(String bifCode, String reCode){
		List<PoolLimitVO> voList = billPoolLimitAllotImpService.queryLimitAllotCheckByBifCode(bifCode, reCode);
		return success(voList);
	}
	
	/**
	 * <b>功能：</b>获取等待结果额度分配复核数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-17 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	@PostMapping("/queryLimitAllotCheckResultByBifCode")
	@ResponseBody
	public AjaxJson  queryLimitAllotCheckResultByBifCode(String bifCode, String reCode){
		List<PoolLimitVO> voList = billPoolLimitAllotImpService.queryLimitAllotCheckResultByBifCode(bifCode, reCode);
		return success(voList);
	}
	/**
	 * <b>功能：修改额度分配复核的指令状态</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-17 <br>
	 * @param paramsVo 修改数据的参数
	 * @return
	 */
	@PostMapping("/updatePoolLimitAllotCheck")
	@ResponseBody
	public AjaxJson updatePoolLimitAllotCheck(@RequestBody PoolLimitVO paramsVo){
		billPoolLimitAllotImpService.updatePoolLimitAllotCheck(paramsVo);
		return success();
	}
}
