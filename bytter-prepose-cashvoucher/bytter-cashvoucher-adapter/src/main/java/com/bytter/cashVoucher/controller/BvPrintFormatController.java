package com.bytter.cashVoucher.controller;

import com.bytter.cashVoucher.entity.BvLog;
import com.bytter.cashVoucher.entity.BvPrintFormat;
import com.bytter.cashVoucher.entity.BvPrintFormatDtl;
import com.bytter.cashVoucher.service.BvLogService;
import com.bytter.cashVoucher.service.BvPrintFormatService;
import com.bytter.cashVoucher.vo.BvPrintFormatVo;
import com.bytter.cashVoucher.vo.BvPrintMatchRuleVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**   
 * @Title: Controller  
 * @Description: 凭证打印格式
 * @author onlineGenerator
 * @date 2017-05-31 15:25:10
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/blankvou/bvprintformat")
public class BvPrintFormatController extends BaseController {
	
	@Autowired
	private BvPrintFormatService bvPrintFormatService;
	@Autowired
	private BvLogService bvLogService;
	/**
	* 凭证打印格式列表 页面跳转
	*
	* @return
	*/
	@GetMapping("/list")
	public String list(Model model) {
        return "bill/blankvou/listBvPrintFormat";
	}
	
    /**
    * 凭证打印格式 table列表
    *
    * @return
    */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
	@LogMessage(content = "查询凭证打印格式列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_SELECT)
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return bvPrintFormatService.listBvPrintFormatInfo(dataTablesInput);
    }
    
	/**
	* 凭证打印格式新增页面跳转
	*
	* @return
	*/
	@GetMapping("/add")
	public String add(BvPrintFormat bvPrintFormat, Model model) {
		if (StringUtils.isNotEmpty(bvPrintFormat.getId())) {
			bvPrintFormat = bvPrintFormatService.get(bvPrintFormat.getId());
			model.addAttribute("bvPrintFormatPage", bvPrintFormat);
		}
		String fmtNo= SequenceSingleton.getSequence(SequenceEnum.BV_FMT_NO);
		model.addAttribute("fmtNo", fmtNo);
		return "bill/blankvou/addBvPrintFormat";
	}
	/**
	* 凭证打印格式编辑页面跳转
	*
	* @return
	*/
	@GetMapping("/update")
	public String update(BvPrintFormat bvPrintFormat, Model model) {
		if (StringUtils.isNotEmpty(bvPrintFormat.getId())) {
			bvPrintFormat = bvPrintFormatService.get(bvPrintFormat.getId());
			model.addAttribute("bvPrintFormatPage", bvPrintFormat);
		}
		return "bill/blankvou/updateBvPrintFormat";
	}
	/**
	* 凭证打印格式详情页
	*
	* @return
	*/
	//@RequiresPermissions("bill.blankvou:bvprintformat:view")
	@LogMessage(content = "查询凭证打印格式详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		BvPrintFormatVo bvPrintFormatPage = bvPrintFormatService.getBvPrintFormatVo(id);
		List<BvLog> bvlogList = bvLogService.listBvLog(bvPrintFormatPage.getId());
		model.addAttribute("bvPrintFormatPage", bvPrintFormatPage);
		model.addAttribute("bvlogList", bvlogList);
		return "bill/blankvou/viewBvPrintFormat";
	}
	/**
	 * 添加凭证打印格式
	 * @param bvPrintFormatVo
	 * @return
	 */
	@LogMessage(content = "添加凭证打印格式",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(@RequestBody BvPrintFormatVo bvPrintFormatVo){
		bvPrintFormatService.saveBvPrintFormat(bvPrintFormatVo,this.getCurrentUser());
		return success();
	}
	/**
	* 修改凭证打印格式
	* @param bvPrintFormatVo
	* @return
	*/
	@LogMessage(content = "修改凭证打印格式",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(@RequestBody BvPrintFormatVo bvPrintFormatVo){
		bvPrintFormatService.modifyBvPrintFormat(bvPrintFormatVo,this.getCurrentUser());
		return  success();
	}
	
	/**
	* 凭证打印格式复制页面跳转
	*
	* @return
	*/
	@GetMapping("/copy")
	public String copy(BvPrintFormat bvPrintFormat, Model model) {
		if (StringUtils.isNotEmpty(bvPrintFormat.getId())) {
			bvPrintFormat = bvPrintFormatService.get(bvPrintFormat.getId());
			model.addAttribute("bvPrintFormatPage", bvPrintFormat);
		}
		String fmtNo=SequenceSingleton.getSequence(SequenceEnum.BV_FMT_NO);
		model.addAttribute("fmtNo", fmtNo);
		return "bill/blankvou/copyBvPrintFormat";
	}
	/**
	 * 复制新增凭证打印格式
	 * @param bvPrintFormatVo
	 * @return
	 */
	@LogMessage(content = "复制添加凭证打印格式",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_COPY)
	@PostMapping("/copySave")
	@ResponseBody
	public AjaxJson copySave(@RequestBody BvPrintFormatVo bvPrintFormatVo){
		bvPrintFormatService.copyBvPrintFormat(bvPrintFormatVo,this.getCurrentUser());
		return success();
	}
	
	/**
	* 删除凭证打印格式
	* @param id
	* @return
	*/
	@LogMessage(content = "删除凭证打印格式",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		boolean reBoo=bvPrintFormatService.batchDelete(id,getCurrentUser());
		AjaxJson json=new AjaxJson();
		json.setSuccess(reBoo);
		return json;
	}
	
	/**
	 * 批量删除凭证打印格式
	 * @param ids
	 * @return
	 */
	@LogMessage(content = "批量删除凭证打印格式",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_BATCHDEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(String ids){
		boolean reBoo=bvPrintFormatService.batchDelete(ids,this.getCurrentUser());
		AjaxJson json=new AjaxJson();
		json.setSuccess(reBoo);
		return json;
	}
	
	/**
	* 打印格式编号是否重复
	 * @param opType 操作类型
	 * @param fmtNo 套打编号
	 * @param id
	* @return
	*/
	@PostMapping("/checkFmtNo")
	@ResponseBody
	public AjaxJson checkFmtNo(String opType,String fmtNo,String id){
		boolean reBoo=bvPrintFormatService.checkFmtNo(opType,fmtNo,id);
		AjaxJson json=new AjaxJson();
		json.setSuccess(reBoo);
		return json;
	}
	
	
	/**
	 * 凭证打印格式匹配规则 信息
	 *
	 * @return
	 */
	@PostMapping("/matchRuleDatatable")
	@ResponseBody
	public List<BvPrintMatchRuleVo> matchRuleDatatable(String printId, String opType, String vouTid){
		List<BvPrintMatchRuleVo> matchRuleList=bvPrintFormatService.listPrintMatchRuleInfoByPrintId(printId, opType, vouTid);
		return matchRuleList;
	}
    /**
    * 凭证打印格式打印明细信息
    *
    * @return
    */
    @PostMapping("/printFmtDtlDatatable")
    @ResponseBody
    public List<BvPrintFormatDtl> printFmtDtlDatatable(String printId){
    	List<BvPrintFormatDtl> matchRuleList=bvPrintFormatService.listPrintFmtDtlInfoByPrintId(printId);
		return matchRuleList;
    }
	
	
}
