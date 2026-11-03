package com.bytter.biz.netbank.controller;
import com.bytter.biz.netbank.entity.BtPayType;
import com.bytter.biz.netbank.service.BtPayTypeService;
import com.bytter.biz.netbank.vo.BtPayTypeVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * @Title: Controller
 * @Description: 系统结算方式表
 * @author onlineGenerator
 * @date 2017-05-12 13:44:54
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/netbank/btpaytype")
public class BtPayTypeController extends BaseController {
	@Autowired
	private BtPayTypeService btPayTypeService;
	/**
	* 系统结算方式表列表 页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTPAYTYPE_APPLY)
	@LogMessage(content = "查询系统结算方式表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "netbank/listBtPayType";
	}
    /**
    * 系统结算方式表 table列表
    *
    * @return
    */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTPAYTYPE_APPLY)
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return btPayTypeService.getDataTables(dataTablesInput);
    }
	/**
	* 系统结算方式表新增页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTPAYTYPE_APPLY)
	@GetMapping("/add")
	public String add(BtPayType btPayType, Model model) {
		if (StringUtils.isNotEmpty(btPayType.getId())) {
			btPayType = btPayTypeService.get(btPayType.getId());
			model.addAttribute("btPayTypePage", btPayType);
		}
		return "netbank/addBtPayType";
	}
	/**
	* 系统结算方式表编辑页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTPAYTYPE_APPLY)
	@GetMapping("/update")
	public String update(BtPayType btPayType, Model model) {
		if (StringUtils.isNotEmpty(btPayType.getId())) {
			btPayType = btPayTypeService.get(btPayType.getId());
			model.addAttribute("btPayTypePage", btPayType);
		}
		return "netbank/updateBtPayType";
	}
	/**
	* 系统结算方式表详情页
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTPAYTYPE_APPLY)
	@LogMessage(content = "查询系统结算方式表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		BtPayType btPayType = btPayTypeService.get(id);
		model.addAttribute("btPayTypePage", btPayType);
		return "netbank/viewBtPayType";
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
	public AjaxJson save(BtPayType btPayType){
		btPayTypeService.save(btPayType);
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
	public AjaxJson update(BtPayType btPayType){
		BtPayType t = btPayTypeService.get(btPayType.getId());
		BeanUtils.copyNotNullProperties(btPayType, t);
		btPayTypeService.saveOrUpdate(t);
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
		BtPayType btPayType = btPayTypeService.get(id);
		btPayTypeService.deleteEntity(btPayType);
		return success();
	}
	/**
	* 批量删除系统结算方式表
	* @param ids
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTPAYTYPE_APPLY)
	@LogMessage(content = "批量删除系统结算方式表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson deleteDelete(@ModelAttribute("ids")String ids){
		for(String id : ids.split(CommonConstant.COMMA_SPLIT)){
			BtPayType btPayType = btPayTypeService.get(id);
			btPayTypeService.deleteEntity(btPayType);
		}
		return success();
	}
	@GetMapping("/json")
    @ResponseBody
    public List<BtPayTypeVO> json(){
        return btPayTypeService.getValidPayType();
    }
}
