package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.domain.billbank.pool.vo.PoolBillVoucherVO;
import com.bytter.bill.domain.billbank.pool.service.impl.BillPoolIntoRepealImpService;
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
 * <b>功能：</b>票据入池撤销服务接口<br>
 * <b>作者：</b>郑鸿杰<br>
 * <b>日期：</b> 2021-11-25 <br>
 */
@Controller
@RequestMapping("/billPool/intoRepeal")
public class BillPoolIntoRepealController extends BaseController {
	@Autowired
	BillPoolIntoRepealImpService billPoolIntoRepealImpService;
	/**
	 * <b>功能：对获取的可入池撤消数据进行入库（先根据票据号、交易日期判断是否存在数据再新增）</b><br>
	 * <b>作者：</b>郑鸿杰<br>
	 * <b>日期：</b> 2021-11-30 <br>
	 * @param paramsVo 数据的信息
	 */
	@PostMapping("/savePoolIntoRepealInfo")
	@ResponseBody
	public AjaxJson savePoolIntoRepealInfo(@RequestBody List<PoolBillVoucherVO> paramsVo){
		billPoolIntoRepealImpService.savePoolIntoRepealInfo(paramsVo);
		return success();
	}
	
	/**
	 * <b>功能：</b>获取待发送入池撤销申请数据<br>
	 * <b>作者：</b>zhenghj<br>
	 * <b>日期：</b> 2021-11-30 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return 获取待发送入池撤销申请数据
	 */
	@PostMapping("/queryIntoRepealByBifCode")
	@ResponseBody
	public AjaxJson queryIntoRepealByBifCode(String bifCode, String reCode){
		List<PoolBillVoucherVO> voList = billPoolIntoRepealImpService.queryIntoRepealByBifCode(bifCode, reCode);
		return success(voList);
	}
	
	/**
	 * <b>功能：</b>获取等待结果票据入池撤销数据<br>
	 * <b>作者：</b>zhenghj<br>
	 * <b>日期：</b> 2021-11-30 <br>
	 * @param bifCode 银行接口代码
	 * @param reCode 银行返回代码
	 * @return 等待结果票据入池撤销数据
	 */
	@PostMapping("/queryIntoRepealResultByBifCode")
	@ResponseBody
	public AjaxJson  queryIntoRepealResultByBifCode(String bifCode, String reCode){
		List<PoolBillVoucherVO> voList = billPoolIntoRepealImpService.queryIntoRepealResultByBifCode(bifCode, reCode);
		return success(voList);
	}
	/**
	 * <b>功能：修改票据入池撤销的指令状态</b><br>
	 * <b>作者：</b>zhenghj<br>
	 * <b>日期：</b> 2021-11-30 <br>
	 * @param paramsVo 修改数据的参数
	 * @return
	 */
	@PostMapping("/updatePoolIntoRepeal")
	@ResponseBody
	public AjaxJson  updatePoolIntoRepeal(@RequestBody PoolBillVoucherVO paramsVo){
		billPoolIntoRepealImpService.updatePoolIntoRepeal(paramsVo);
		return success();
	}
}
