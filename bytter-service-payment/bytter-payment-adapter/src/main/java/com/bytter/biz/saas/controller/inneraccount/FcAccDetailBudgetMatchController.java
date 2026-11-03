package com.bytter.biz.saas.controller.inneraccount;

import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.finacial.api.service.IFcAccbookService;
import com.bytter.biz.finacial.api.vo.FcAccDetailBudgetMatchVO;
import com.bytter.biz.inneraccount.dto.v1.FcAccDetailBudgetBatchDTO;
import com.bytter.biz.inneraccount.service.FcAccDetailBudgetMatchService;
import com.bytter.biz.inneraccount.vo.v1.FcAccDetailBudgetMatchPageVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @Title: Controller
 * @Description: 内部流水资金计划匹配
 * @author onlineGenerator
 * @date 2017-05-25 19:36:12
 * @version 1.0
 *
 */
@Api(value = "内部流水资金计划匹配",tags = "内部流水资金计划匹配")
@RestController("v1FcAccDetailBudgetMatchController")
@RequestMapping("/v1-payment/inneraccount/fcaccdetailbudgetmatch")
public class FcAccDetailBudgetMatchController extends BaseController {
	@Autowired
	private FcAccDetailBudgetMatchService fcAccDetailBudgetMatchService;
	@DubboReference(check = false)
	private IFcAccbookService fcAccbookService;
	@DubboReference(check = false)
	private IFbsBudgetProcessService fbsBudgetProcessService;
	@ApiOperation("查询内部流水资金计划列表")
	@PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<FcAccDetailBudgetMatchVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput){
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		return success(fcAccDetailBudgetMatchService.getDataTablesUnion(input,this.getCurrentUser()));
    }
	/**
	 * 确认
	 * @param fcAccDetailBudgetMatchVO
	 * @return
	 */
	@ApiOperation("流水挂资金计划 列表操作列的 确认按钮")
	@LogMessage(content = "流水挂资金计划", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/sure")
	@ResponseBody
	public AjaxJson sure(@RequestBody FcAccDetailBudgetMatchVO fcAccDetailBudgetMatchVO) {
		if(fcAccDetailBudgetMatchVO != null){
			fcAccbookService.updateFcAccFbsStatus(fcAccDetailBudgetMatchVO.getId(),"1",fcAccDetailBudgetMatchService.isDayCloseStatus(fcAccDetailBudgetMatchVO.getDayCloseSign()));
			fcAccDetailBudgetMatchService.saveFcAccBookBudgetProcess(fcAccDetailBudgetMatchVO,this.getCurrentUser());
		}
		return success();
	}
	/**
	 * 批量确认计划
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_APPLY)
	@ApiOperation("批量处理内部流水资金计划 下方科目列表操作  根据Type区分 Y 确认 D 删除")
	@LogMessage(content = "批量处理内部流水计划", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/batch")
	@ResponseBody
	public AjaxJson batchBusiness(@RequestBody FcAccDetailBudgetBatchDTO dto) {
		fcAccDetailBudgetMatchService.batchBusiness(dto.getIds(), dto.getItemCode(),dto.getItemName(),dto.getProcessDate()
				,this.getCurrentUser(),dto.getType());
		return success();
	}
	/**
	 * 处理逻辑
	 * @param id
	 * @param type H 恢复 R还原 D删除
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_APPLY)
	@ApiOperation("内部流水逻辑处理 列表操作列的 H 恢复 R还原 D删除   dayCloseSign 是日结标志")
	@LogMessage(content = "内部流水逻辑处理", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/business/{id}/{type}/{dayCloseSign}")
	@ResponseBody
	public AjaxJson business(@PathVariable("id") String id,@PathVariable("type") String type,@PathVariable("dayCloseSign") String dayCloseSign) {
		// 逻辑变化
		fcAccDetailBudgetMatchService.business(id, this.getCurrentUser(),type,fcAccDetailBudgetMatchService.isDayCloseStatus(dayCloseSign));
		return success();
	}
	/**
	 * 查询资金计划详情
	 * @param id
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_FCACCDETAILBUDGETMATCH_QUERY)
	@ApiOperation(value = "查询流水资金计划详情")
	@LogMessage(content = "查询流水资金计划详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public AjaxJson<FcAccDetailBudgetMatchPageVO> view(@PathVariable("id") String id) {
		FcAccDetailBudgetMatchVO fcAccDetailBudgetMatchVO = fcAccDetailBudgetMatchService.getInfoById(id);
		AjaxJson<FcAccDetailBudgetMatchPageVO> ajaxJson = new AjaxJson<>();
		FcAccDetailBudgetMatchPageVO pageVO = new FcAccDetailBudgetMatchPageVO();
		String billNo = fcAccDetailBudgetMatchVO.getVoucherNo();
		if (EmptyUtils.isBlank(billNo)) {
			billNo = fcAccDetailBudgetMatchVO.getId();
		}
		billNo = billNo + "_" + fcAccDetailBudgetMatchVO.getCdSign();
		List<FbsBudgetProcessDTO> listdto = fbsBudgetProcessService.getBudgetProcessByBillNo(BudgetConstant.BUSINESS_NAME_FCACCBOOKDTL, BudgetConstant.PROCESS_SYSNAME_NIS, billNo);
		fcAccDetailBudgetMatchService.setItemInfo(fcAccDetailBudgetMatchVO, listdto);
		fcAccDetailBudgetMatchVO.setProcessDate(DateUtils.date2Str(listdto.get(0).getProcessDate(),new SimpleDateFormat("yyyy-MM-dd")));
		pageVO.setFcAccDetailBudgetMatchPage(fcAccDetailBudgetMatchVO);
		pageVO.setBillNo(billNo);
		//资金计划
		pageVO.setBusinessName(BudgetConstant.BUSINESS_NAME_FCACCBOOKDTL);
		pageVO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_NIS);
		ajaxJson.setSuccess(true);
		ajaxJson.setObj(pageVO);
		return ajaxJson;
	}
}
