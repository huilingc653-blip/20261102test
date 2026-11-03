package com.bytter.biz.netbank.controller;

import com.alibaba.excel.util.CollectionUtils;
import com.bytter.biz.inneraccount.service.BtBankAccHandleService;
import com.bytter.biz.netbank.entity.BtPayType;
import com.bytter.biz.netbank.entity.FcReceiptType;
import com.bytter.biz.netbank.service.BtPayTypeService;
import com.bytter.biz.netbank.service.FcReceiptTypeService;
import com.bytter.biz.netbank.vo.BtPayTypeVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.IMessageEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.vo.BtBankAccHandleVO;
import com.bytter.system.api.vo.BtBankAccVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Title: Controller
 * @Description: 单据类型Controller
 * @author onlineGenerator
 * @date 2017-05-12 13:44:54
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/netbank/receipttype")
public class FcReceiptTypeController extends BaseController {
	@Autowired
	private FcReceiptTypeService fcReceiptTypeService;
	/**
	* 系统结算方式表列表 页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTPAYTYPE_APPLY)
	@LogMessage(content = "单据类型列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "netbank/listFcReceiptType";
	}
    /**
    * 单据类型表 table列表
    *
    * @return
    */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTPAYTYPE_APPLY)
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return fcReceiptTypeService.getDataTables(dataTablesInput);
    }
	/**
	* 系统结算方式表新增页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTPAYTYPE_APPLY)
	@GetMapping("/add")
	public String add(FcReceiptType fcReceiptType, Model model) {
		if (StringUtils.isNotEmpty(fcReceiptType.getId())) {
			fcReceiptType = fcReceiptTypeService.get(fcReceiptType.getId());
			model.addAttribute("fcReceiptTypePage", fcReceiptType);
		}
		return "netbank/addFcReceiptType";
	}
	/**
	* 系统结算方式表编辑页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTPAYTYPE_APPLY)
	@GetMapping("/update/{id}")
	public String update(@PathVariable("id") String id, Model model) {
		if (StringUtils.isNotEmpty(id)) {
			model.addAttribute("fcReceiptTypePage", fcReceiptTypeService.get(id, true));
		}
		return "netbank/updateFcReceiptType";
	}
	/**
	* 系统结算方式表详情页
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTPAYTYPE_APPLY)
	@LogMessage(content = "单据类型详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		FcReceiptType fcReceiptType = fcReceiptTypeService.get(id,true);
		model.addAttribute("fcReceiptTypePage", fcReceiptType);
		return "netbank/viewFcReceiptType";
	}
	/**
	 * 添加系统结算方式表
	 * @param btPayType
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTPAYTYPE_APPLY)
	@LogMessage(content = "添加系统结算方式表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(FcReceiptType fcReceiptType){
		if(fcReceiptType.getValidSign()==null){
			fcReceiptType.setValidSign("1");
		}
		fcReceiptTypeService.save(fcReceiptType);
		return success();
	}
	/**
	* 修改系统结算方式表
	* @param btPayType
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTPAYTYPE_APPLY)
	@LogMessage(content = "修改系统结算方式表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(FcReceiptType fcReceiptType){
		FcReceiptType t = fcReceiptTypeService.get(fcReceiptType.getId());
		BeanUtils.copyNotNullProperties(fcReceiptType, t);
		fcReceiptTypeService.saveOrUpdate(t);
		return  success();
	}
	/**
	* 删除系统结算方式表
	* @param id
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTPAYTYPE_APPLY)
	@LogMessage(content = "删除系统结算方式表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		fcReceiptTypeService.deleteById(id);

		return success();
	}
//	/**
//	* 批量删除系统结算方式表
//	* @param ids
//	* @return
//	*/
//	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTPAYTYPE_APPLY)
//	@LogMessage(content = "批量删除系统结算方式表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
//	@PostMapping("/batchDelete")
//	@ResponseBody
//	public AjaxJson deleteDelete(@ModelAttribute("ids")String ids){
//		for(String id : ids.split(CommonConstant.COMMA_SPLIT)){
//			BtPayType btPayType = btPayTypeService.get(id);
//			btPayTypeService.deleteEntity(btPayType);
//		}
//		return success();
//	}
	@GetMapping("/json")
    @ResponseBody
    public List<FcReceiptType> json(){
        return fcReceiptTypeService.getValidFcReceiptType();
    }
}
