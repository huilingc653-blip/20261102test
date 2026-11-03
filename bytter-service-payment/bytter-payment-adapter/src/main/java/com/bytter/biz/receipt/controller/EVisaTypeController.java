package com.bytter.biz.receipt.controller;

import com.bytter.biz.receipt.entity.EVisaType;
import com.bytter.biz.receipt.service.EVisaTypeService;
import com.bytter.biz.receipt.vo.EVisaTypeVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @Title: Controller
 * @Description: 签章类型维护
 * @author onlineGenerator
 * @date 2017-06-05 10:52:28
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/evisatype/evisatype")
public class EVisaTypeController extends BaseController {
	@Autowired
	private EVisaTypeService eVisaTypeService;
	/**
	* 签章类型维护列表 页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_RECEIPT_EVISATYPE_OP)
	@LogMessage(content = "查询签章类型维护列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "receipt/elecbill/listEVisaType";
	}
    /**
    * 签章类型维护 table列表
    *
    * @return
    */
	//////@RequiresPermissions(ResourceConstant.NETBANK_RECEIPT_EVISATYPE_OP)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
    	return eVisaTypeService.findListBysql(dataTablesInput, this.getCurrentUser());
    }
	/**
	* 签章类型维护新增页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_RECEIPT_EVISATYPE_OP)
	@GetMapping("/add")
	public String add(EVisaType eVisaType, Model model) {
		if (StringUtils.isNotEmpty(eVisaType.getId())) {
			eVisaType = eVisaTypeService.get(eVisaType.getId());
			model.addAttribute("eVisaTypePage", eVisaType);
		}
		return "receipt/elecbill/addEVisaType";
	}
	/**
	* 签章类型维护编辑页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_RECEIPT_EVISATYPE_OP)
	@GetMapping("/update")
	public String update(EVisaType eVisaType, Model model) {
		if (StringUtils.isNotEmpty(eVisaType.getId())) {
			eVisaType = eVisaTypeService.getById(eVisaType.getId());
			model.addAttribute("eVisaTypePage", eVisaType);
		}
		return "receipt/elecbill/updateEVisaType";
	}
	/**
	 * 操作签章类型
	 * @param id
	 * @param opType 0 作废 1 恢复
	 * @return
	 */
	@LogMessage(content = "作废签章类型",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/operate")
	@ResponseBody
	public AjaxJson operate(String id,String opType){
		if("0".equals(opType)){
			eVisaTypeService.updateOperate(id,"0");
		}else{
			eVisaTypeService.updateOperate(id,"1");
		}
		return success();
	}
	/**
	* 签章类型维护详情页
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_RECEIPT_EVISATYPE_OP)
	@LogMessage(content = "查询签章类型维护详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		EVisaTypeVO eVisaType = eVisaTypeService.getByIdUnion(id);
		model.addAttribute("eVisaTypePage", eVisaType);
		return "receipt/elecbill/viewEVisaType";
	}
	/**
	 * 添加签章类型维护
	 * @param eVisaType
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_RECEIPT_EVISATYPE_OP)
	@LogMessage(content = "添加签章类型维护",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(EVisaType eVisaType){
		eVisaTypeService.save(eVisaType,getCurrentUser());
		return success();
	}
	/**
	* 修改签章类型维护
	* @param eVisaType
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_RECEIPT_EVISATYPE_OP)
	@LogMessage(content = "修改签章类型维护",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson modify(EVisaType eVisaType){
		EVisaType t = eVisaTypeService.getById(eVisaType.getId());
		BeanUtils.copyNotNullProperties(eVisaType, t);
		t.setUpdateBy(getCurrentUser().getLoginName());
		t.setUpdateDate(DateUtils.getDate());
		eVisaTypeService.saveOrUpdate(t);
		return  success();
	}
	/**
	* 删除签章类型维护
	* @param id
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_RECEIPT_EVISATYPE_OP)
	@LogMessage(content = "删除签章类型维护",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		EVisaType eVisaType = eVisaTypeService.get(id);
		eVisaTypeService.deleteEntity(eVisaType);
		return success();
	}
	/**
	* 批量删除签章类型维护
	* @param ids
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_RECEIPT_EVISATYPE_OP)
	@LogMessage(content = "批量删除签章类型维护",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson deleteDelete(@ModelAttribute("ids")String ids){
		for(String id : ids.split(CommonConstant.COMMA_SPLIT)){
			EVisaType eVisaType = eVisaTypeService.get(id);
			eVisaTypeService.deleteEntity(eVisaType);
		}
		return success();
	}

}
