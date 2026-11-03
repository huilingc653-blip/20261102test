package com.bytter.biz.saas.controller.bond;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.guarantee.api.IFcBondLogService;
import com.bytter.guarantee.vo.FcBondLogVO;
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 
 * @author fusb
 * @version 1.0
 * @Title FcBondMsgController
 * @Description 保证金日志 Controller
 * @date 2018年9月12日
 */
@Api(value = "/v1-payment/bond/fcbondlog", tags = "保证金日志")
@RestController("v1FcBondMsgController")
@RequestMapping("/v1-payment/bond/fcbondlog")
public class FcBondMsgController extends BaseController {
	@DubboReference(check = false)
	IFcBondLogService fcBondLogService;

	/**
	 * 通过保证金ID查询相应的日志信息
	 * 
	 * @param input
	 * @param bondId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/listlogdata")
	@ResponseBody
	public AjaxJson<DataTablesOutput> listLogDate(@RequestBody DataTablesInput input, String bondId) {
		return success(fcBondLogService.listFcBondMsg(bondId, input));
	}
	/**
	 * 通过保证金ID，从FC_BOND_LOG日志表中得到相应的操作信息
	 * 
	 * @return
	 */
	@GetMapping("/listopmessage/{id}")
	public AjaxJson<List<FcBondLogVO>> list(@PathVariable("id") String id) {
		List<FcBondLogVO> listOpMessage = fcBondLogService.listOpMessage(id);
		return success(listOpMessage);
	}
}
