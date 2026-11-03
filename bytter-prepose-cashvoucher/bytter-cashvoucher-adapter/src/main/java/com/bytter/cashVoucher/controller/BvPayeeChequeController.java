package com.bytter.cashVoucher.controller;

import com.bytter.bill.api.bill.constant.BlankvouConstant;
import com.bytter.bill.api.bill.constant.PayeeChequeConstant;
import com.bytter.bill.api.bill.enums.BlankvouMessageEnum;
import com.bytter.cashVoucher.entity.BvLog;
import com.bytter.cashVoucher.entity.BvPayeeCheque;
import com.bytter.cashVoucher.service.BvLogService;
import com.bytter.cashVoucher.service.BvPayeeChequeService;
import com.bytter.cashVoucher.vo.BvPayeeChequeVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * @Title: BvPayeeChequeController  
 * @Description: 支票收款登记控制器类
 * @author Jinyong.Cao
 * @date 2018-06-13 13:06:40
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/blankvou/bvpayeecheque")
public class BvPayeeChequeController extends BaseController {
	@Autowired
	private BvPayeeChequeService bvPayeeChequeService;
	
	@Autowired
	private BvLogService bvLogService;
	
	/**
	* 支票收款管理列表 页面跳转
	*
	* @return
	*/
	@GetMapping("/list")
	public String list(Model model){
		return "bill/blankvou/listBvPayeeCheque";
	}
	
	
	/**
	 * 支票收款信息表 table列表
	 * @return
	 */ 
	@SuppressWarnings("rawtypes")
	@LogMessage(content = "查询凭证入库信息列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
		input.getFormdata().put("ini_status", "0,-1,1");
		input.getFormdata().put("checkboxHide","1");
		return bvPayeeChequeService.getDataTablesUnion(input,this.getCurrentUser(),PayeeChequeConstant.AB_BILL_RESOURCE_BVPAYEECHEQUE_QUERY);
	}
	
	/**
	* 支票收款入库信息表新增页面跳转
	* @return
	*/
	@GetMapping("/add")
	public String add(BvPayeeCheque bvPayeeCheque, Model model) {
		if (StringUtils.isNotEmpty(bvPayeeCheque.getId())) {
			bvPayeeCheque = bvPayeeChequeService.get(bvPayeeCheque.getId());
			model.addAttribute("bvPayeeChequePage", bvPayeeCheque);
		}
		return "bill/blankvou/addBvPayeeCheque";
	}
	
	/**
	*  新增 、保存、修改
	* @return
	*/
	@LogMessage(content = "支票收款信息入库",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@PostMapping("/saveOrstorage/{operate}")
	@ResponseBody
	public AjaxJson saveOrstorage(@PathVariable("operate") String operate,BvPayeeCheque  bvPayeeCheque){
		boolean flag = true;
		//ID不为空
		if(!StringUtils.isNotEmpty(bvPayeeCheque.getId())){
			flag = bvPayeeChequeService.checkPayeeChequeNoUnique(bvPayeeCheque, operate);
			if(!flag){
				throw new BusinessException(BlankvouMessageEnum.BILL_BLANKVOU_BVPAYEECHEQUE_SUBMIT_EXIST);
			}
		}
		AjaxJson ajaxJson = success();
		if(flag){
			bvPayeeChequeService.baseSaveOrUpdate(bvPayeeCheque, operate,this.getCurrentUser());
		}else{
			ajaxJson.setSuccess(false);
		}
		return ajaxJson;
	}
	
	
	/**
	* 支票收款信息详情页
	*
	* @return
	*/
	@LogMessage(content = "支票收支信息详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		BvPayeeChequeVO vo = bvPayeeChequeService.getDataTablesRow(id,null,this.getCurrentUser());
		List<BvLog> bvlogList = bvLogService.listBvLog(vo.getId());
		model.addAttribute("bvlogList", bvlogList);
		model.addAttribute("bvPayeeChequePage", vo);
		return "bill/blankvou/viewbvPayeeCheque";
	}
	
	/**
	* 批量提交
	* @return
	*/
	@LogMessage(content = "支票收款信息批量入库",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_BATCHSUCCE)
	@PostMapping("/batchStorage/{operate}")
	@ResponseBody
	public AjaxJson batchStorage(@PathVariable("operate") String operate,String ids){
		AjaxJson ajaxJson = success();
		bvPayeeChequeService.batchBtorage(ids,operate,this.getCurrentUser());
		return  ajaxJson;
	}
	
	
	
	/**
	* 删除支票收款信息表
	* @param id
	* @return
	*/
	@LogMessage(content = "删除支票收支信息",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}") 
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		AjaxJson ajaxJson = success();
		ajaxJson.setSuccess(bvPayeeChequeService.deleteById(id,this.getCurrentUser()));
		return ajaxJson;
	}
	
	
	/**
	* 批量删除支票信息表
	* @param ids
	* @return
	*/
	@LogMessage(content = "批量删除支票收款信息表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(String ids){
		AjaxJson ajaxJson = success();
		ajaxJson.setSuccess(bvPayeeChequeService.batchDelete(ids,this.getCurrentUser()));
		return ajaxJson;
	}
	
	
	/**
	* 支票收款信息表编辑页面跳转
	*
	* @return 
	*/
	@GetMapping("/update/{id}")
	public String update(@PathVariable("id") String id ,Model model) {
		BvPayeeChequeVO vo = bvPayeeChequeService.getDataTablesRow(id,BlankvouConstant.BILL_OPERATE_EDIT,this.getCurrentUser());
		model.addAttribute("bvPayeeChequeVO", vo);
		return "bill/blankvou/updateBbvPayeeCheque";
	}
	
	
	/**
	 * 导入保证申请
	 * @param list
	 * @return
	 */
	@LogMessage(content = "导入收款信息操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_Import)
	@PostMapping("/saveImport")
	@ResponseBody
	public AjaxJson saveImport(@RequestBody List<BvPayeeChequeVO> list) {
		AjaxJson json=success();
		boolean flag=bvPayeeChequeService.saveImport(list,this.getCurrentUser());
		json.setSuccess(flag);
		return success();
	}
	
}
