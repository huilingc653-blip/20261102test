package com.bytter.biz.inneraccount.controller;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.finacial.api.service.IFcAccDetailBudgetMatchService;
import com.bytter.biz.finacial.api.service.IFcAccbookService;
import com.bytter.biz.finacial.api.vo.FcAccDetailBudgetMatchVO;
import com.bytter.biz.inneraccount.service.FcAccDetailBudgetMatchService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.text.SimpleDateFormat;
import java.util.List;
/**
 * @Title: Controller
 * @Description: 内部账户流水信息
 * @author onlineGenerator
 * @date 2017-05-25 19:36:12
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/inneraccount/fcaccdetailbudgetmatch")
public class FcAccDetailBudgetMatchController extends BaseController {
	@Autowired
	private FcAccDetailBudgetMatchService fcAccDetailBudgetMatchService;
	@DubboReference(check = false)
	private IFcAccbookService fcAccbookService;
	@DubboReference(check = false)
	private IFbsBudgetProcessService fbsBudgetProcessService;
	@SuppressWarnings("rawtypes")
	//////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_FCACCDETAILBUDGETMATCH_QUERY)
	@PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return fcAccDetailBudgetMatchService.getDataTablesUnion(dataTablesInput,this.getCurrentUser());
    }
	//////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_FCACCDETAILBUDGETMATCH_QUERY)
	@LogMessage(content = "查询内部账户流水信息表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "inneraccount/listFcAccDetailBudgetMatch";
	}
	/**
	 * 确认
	 * @param fcAccDetailBudgetMatchVO
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_FCACCDETAILBUDGETMATCH_APPLY)
	@LogMessage(content = "流水挂资金计划", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/sure")
	@ResponseBody
	public AjaxJson sure(FcAccDetailBudgetMatchVO fcAccDetailBudgetMatchVO) {
		if(fcAccDetailBudgetMatchVO != null){
			fcAccbookService.updateFcAccFbsStatus(fcAccDetailBudgetMatchVO.getId(),"1",fcAccDetailBudgetMatchService.isDayCloseStatus(fcAccDetailBudgetMatchVO.getDayCloseSign()));
			fcAccDetailBudgetMatchService.saveFcAccBookBudgetProcess(fcAccDetailBudgetMatchVO,this.getCurrentUser());
		}
		return success();
	}
	/**
	 * 批量确认计划
	 * @param ids
	 * @param type Y 确认 D 删除
	 * @param itemCode 科目代码
	 * @param itemName 科目名称
	 * @param processDate 执行时间
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_APPLY)
	@LogMessage(content = "批量处理内部流水计划", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/batch")
	@ResponseBody
	public AjaxJson batchBusiness(@RequestParam("ids") String ids,@RequestParam("type") String type,@RequestParam("itemCode") String itemCode,
								  @RequestParam("itemName") String itemName,@RequestParam("processDate") String processDate) {
		fcAccDetailBudgetMatchService.batchBusiness(ids, itemCode,itemName,processDate,this.getCurrentUser(),type);
		return success();
	}
	/**
	 * 处理逻辑
	 * @param id
	 * @param type H 恢复 1还原 2删除
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_APPLY)
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
	 * @param model
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_FCACCDETAILBUDGETMATCH_QUERY)
	@LogMessage(content = "查询资金计划详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		FcAccDetailBudgetMatchVO fcAccDetailBudgetMatchVO = fcAccDetailBudgetMatchService.getInfoById(id);
		String billNo = fcAccDetailBudgetMatchVO.getVoucherNo();
		if (EmptyUtils.isBlank(billNo)) {
			billNo = fcAccDetailBudgetMatchVO.getId();
		}
		billNo = billNo + "_" + fcAccDetailBudgetMatchVO.getCdSign();
		List<FbsBudgetProcessDTO> listdto = fbsBudgetProcessService.getBudgetProcessByBillNo(BudgetConstant.BUSINESS_NAME_FCACCBOOKDTL, BudgetConstant.PROCESS_SYSNAME_NIS, billNo);
		fcAccDetailBudgetMatchService.setItemInfo(fcAccDetailBudgetMatchVO, listdto);
		fcAccDetailBudgetMatchVO.setProcessDate(DateUtils.date2Str(listdto.get(0).getProcessDate(),new SimpleDateFormat("yyyy-MM-dd")));
		model.addAttribute("fcAccDetailBudgetMatchPage", fcAccDetailBudgetMatchVO);
		model.addAttribute("billNo", billNo);
		//资金计划
		model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_FCACCBOOKDTL);
		model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_NIS);
		return "inneraccount/viewFcAccDetailBudgetMatch";
	}
}
