package com.bytter.bill.web.saas.controller.billPool;

import com.bytter.bill.domain.billPool.service.AbPoolSignInfoService;
import com.bytter.bill.domain.billPool.vo.AbPoolSignInfoVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Title: Controller
 * @Description: 票据池票据池签约信息控制器
 * @author zhengg
 * @date 2020-03-13 15:25:10
 * @version 1.0
 */
@Api(value = "/v1-preposebill/billPool/signInfo", tags = "票据池签约信息")
@RestController("v1AbPoolSignInfoController")
@RequestMapping("/v1-preposebill/billPool/signInfo")
public class AbPoolSignInfoController extends BaseController {
	@Autowired
	private AbPoolSignInfoService abPoolSignInfoService;

	/**
	 * 查询票据池签约信息明细
	 */
	@ApiOperation("查询票据池签约信息明细")
	@GetMapping("/viewInfo")
	public AjaxJson<AbPoolSignInfoVO> viewInfo(String id) {
		return success(abPoolSignInfoService.getAbPoolSignInfoVOById(id));
	}

	/**
	 * 查询票据池票据池签约信息列表数据
	 */
	@SuppressWarnings("rawtypes")
	@LogMessage(content = "查询票据池票据池签约信息列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
	@ApiOperation("查询票据池票据池签约信息列表数据")
	@PostMapping("/datatableQuery")
	public AjaxJson<DataTablesOutput> datatableQuery(@RequestBody V1DataTablesInput dataTablesInput) {
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		return success(abPoolSignInfoService.datatableQuery(input));
	}

}
