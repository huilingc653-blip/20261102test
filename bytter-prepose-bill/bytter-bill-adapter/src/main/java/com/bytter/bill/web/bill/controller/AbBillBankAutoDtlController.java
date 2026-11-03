package com.bytter.bill.web.bill.controller;
import com.bytter.bill.api.bill.enums.AccebillMessageEnum;
import com.bytter.bill.domain.bill.entity.AbBillBankAutoDtl;
import com.bytter.bill.domain.bill.service.AbBillBankAutoDtlService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * @Title: Controller
 * @Description: 票据银行对于自动业务配置
 * @author zhenghj
 * @date 2017-09-28
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abbillbankautodtl")
public class AbBillBankAutoDtlController extends BaseController {
	@Autowired
	private AbBillBankAutoDtlService abBillBankAutoDtlService;
	/**
	* 票据银行对于自动业务配置维护列表 页面跳转
	*
	* @return
	*/
	@LogMessage(content = "票据银行对于自动业务配置维护列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
		return "bill/accebill/listAbBillBankAutoDtl";
	}
    /**
    * 票据银行对于自动业务配置 table列表
    *
    * @return
    */
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return abBillBankAutoDtlService.listAbBillBankAutoDtl(dataTablesInput);
    }
	@GetMapping("/getBillBankAutoDtlByBifCode")
	@ResponseBody
	public AbBillBankAutoDtl getBillBankAutoDtlByBifCode(String bifCode){
		return abBillBankAutoDtlService.getDataByBifCode(bifCode);
	}
	/**
	* 票据银行对于自动业务配置维护新增页面跳转
	*
	* @return
	*/
	@GetMapping("/add")
	public String add(AbBillBankAutoDtl abBillBankAutoDtl, Model model) {
		if (StringUtils.isNotEmpty(abBillBankAutoDtl.getId())) {
			abBillBankAutoDtl = abBillBankAutoDtlService.get(abBillBankAutoDtl.getId());
			model.addAttribute("abBillBankAutoDtlPage", abBillBankAutoDtl);
		}
		return "bill/accebill/addAbBillBankAutoDtl";
	}
	/**
	* 票据银行对于自动业务配置维护编辑页面跳转
	*
	* @return
	*/
	@GetMapping("/update")
	public String update(AbBillBankAutoDtl abBillBankAutoDtl, Model model) {
		if (StringUtils.isNotEmpty(abBillBankAutoDtl.getId())) {
			abBillBankAutoDtl = abBillBankAutoDtlService.get(abBillBankAutoDtl.getId());
			model.addAttribute("abBillBankAutoDtlPage", abBillBankAutoDtl);
		}
		return "bill/accebill/updateAbBillBankAutoDtl";
	}
	/**
	* 票据银行对于自动业务配置维护详情页
	*
	* @return
	*/
	@LogMessage(content = "查询票据银行对于自动业务配置维护详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		AbBillBankAutoDtl abBillBankAutoDtl = abBillBankAutoDtlService.get(id);
		model.addAttribute("abBillBankAutoDtlPage", abBillBankAutoDtl);
		return "bill/accebill/viewAbBillBankAutoDtl";
	}
	/**
	 * 添加票据银行对于自动业务配置
	 * @param abBillBankAutoDtl
	 * @return
	 */
	@LogMessage(content = "添加票据银行对于自动业务配置",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(AbBillBankAutoDtl abBillBankAutoDtl){
		if (abBillBankAutoDtlService.isExistMulByBifCode(abBillBankAutoDtl.getBifCode())) {
			throw new ParameterException(AccebillMessageEnum.ACCEBILL_MESSAGE_ERROR_BANKAUTO_BIFCODE_REPEAT);
		}
		// 创建人信息
		if(EmptyUtils.isBlank(abBillBankAutoDtl.getCreateBy())){
			abBillBankAutoDtl.setCreateBy(this.getCurrentUser().getLoginName());
			abBillBankAutoDtl.setCreateDate(DateUtils.getDate());
		}
		abBillBankAutoDtlService.save(abBillBankAutoDtl);
		return success();
	}
	/**
	* 修改票据银行对于自动业务配置
	* @param abBillBankAutoDtl
	* @return
	*/
	@LogMessage(content = "修改票据银行对于自动业务配置",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(AbBillBankAutoDtl abBillBankAutoDtl){
		AbBillBankAutoDtl t = abBillBankAutoDtlService.get(abBillBankAutoDtl.getId());
		BeanUtils.copyNotNullProperties(abBillBankAutoDtl, t);
		// 修改人信息
		t.setUpdateBy(this.getCurrentUser().getLoginName());
		t.setUpdateDate(DateUtils.getDate());
		abBillBankAutoDtlService.saveOrUpdate(t);
		return  success();
	}
	/**
	* 删除票据银行对于自动业务配置
	* @param id
	* @return
	*/
	@LogMessage(content = "删除票据银行对于自动业务配置",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		AbBillBankAutoDtl abBillBankAutoDtl = abBillBankAutoDtlService.get(id);
		abBillBankAutoDtlService.deleteEntity(abBillBankAutoDtl);
		return success();
	}
	/**
	* 批量删除银行类型维护
	* @param ids
	* @return
	*/
	@LogMessage(content = "批量删除票据银行对于自动业务配置",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete/{ids}")
	@ResponseBody
	public AjaxJson deleteDelete(@PathVariable("ids")String ids){
		for(String id : ids.split(",")){
			AbBillBankAutoDtl abBillBankAutoDtl = abBillBankAutoDtlService.get(id);
			abBillBankAutoDtlService.deleteEntity(abBillBankAutoDtl);
		}
		return success();
	}
}
