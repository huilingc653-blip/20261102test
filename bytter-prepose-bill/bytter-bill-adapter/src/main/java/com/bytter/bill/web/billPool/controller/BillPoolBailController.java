package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.domain.billbank.vo.VoucherInfoVo;
import com.bytter.bill.domain.billbank.pool.vo.PoolBailVO;
import com.bytter.bill.domain.billbank.pool.service.impl.BillPoolBailImpService;
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
 * <b>功能：</b>保证金申请服务接口<br>
 * <b>作者：</b>郑刚<br>
 * <b>日期：</b> 2020-02-17 <br>
 */
@Controller
@RequestMapping("/billPool/bail")
public class BillPoolBailController extends BaseController {
	BillPoolBailImpService  billPoolBailImpService;
	
	/**
	 * <b>功能：</b>获取待发送保证金存入申请数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-12 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	@PostMapping("/queryBailInByBifCode")
	@ResponseBody
	public AjaxJson queryBailInByBifCode(String bifCode, String reCode) {
		List<PoolBailVO> voList = billPoolBailImpService.queryBailInByBifCode(bifCode, reCode);
		return success(voList);
	}
	
	/**
	 * <b>功能：</b>获取等待结果保证金存入数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-12 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	@PostMapping("/queryBailInResultByBifCode")
	@ResponseBody
	public AjaxJson  queryBailInResultByBifCode(String bifCode, String reCode){
		List<PoolBailVO> voList = billPoolBailImpService.queryBailInResultByBifCode(bifCode, reCode);
		return success(voList);
	}
	/**
	 * <b>功能：修改保证金存入的指令状态</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-12 <br>
	 * @param paramsVo 修改数据的参数
	 * @return
	 */
	@PostMapping("/updatePoolBailInApply")
	@ResponseBody
	public AjaxJson  updatePoolBailInApply(@RequestBody PoolBailVO paramsVo){
		billPoolBailImpService.updatePoolBailInApply(paramsVo);
		return success();
	}
	/**
	 * <b>功能：</b>获取待发送保证金提取申请数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-12 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	@PostMapping("/queryBailOutByBifCode")
	@ResponseBody
	public AjaxJson queryBailOutByBifCode(String bifCode, String reCode){
		List<PoolBailVO> voList = billPoolBailImpService.queryBailOutByBifCode(bifCode, reCode);
		return success(voList);
	}
	
	/**
	 * <b>功能：</b>获取等待结果保证金提取数据<br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-12 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return
	 */
	@PostMapping("/queryBailOutResultByBifCode")
	@ResponseBody
	public AjaxJson queryBailOutResultByBifCode(String bifCode, String reCode){
		List<PoolBailVO> voList = billPoolBailImpService.queryBailOutResultByBifCode(bifCode, reCode);
		return success(voList);
	}
	/**
	 * <b>功能：修改保证金提取的指令状态</b><br>
	 * <b>作者：</b>郑刚<br>
	 * <b>日期：</b> 2020-02-12 <br>
	 * @param paramsVo 修改数据的参数
	 * @return
	 */
	@PostMapping("/updatePoolBailOut")
	@ResponseBody
	public AjaxJson  updatePoolBailOut(@RequestBody PoolBailVO paramsVo){
		billPoolBailImpService.updatePoolBailOut(paramsVo);
		return success();
	}
}
