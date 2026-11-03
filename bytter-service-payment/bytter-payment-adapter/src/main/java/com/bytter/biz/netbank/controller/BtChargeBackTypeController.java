package com.bytter.biz.netbank.controller;
import com.bytter.biz.netbank.entity.BtChargeBackType;
import com.bytter.biz.netbank.service.BtChargeBackTypeService;
import com.bytter.biz.netbank.vo.BtChargeBackTypeVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * @Title: Controller
 * @Description: 退单类型表
 * @author onlineGenerator
 * @date 2017-05-17 14:26:13
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/netbank/btchargebacktype")
public class BtChargeBackTypeController extends BaseController {
	@Autowired
	private BtChargeBackTypeService btChargeBackTypeService;
	/**
	* 退单类型表列表 页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTCHARGEBACKTYPE_APPLY)
	@LogMessage(content = "查询退单类型表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "netbank/listBtChargeBackType";
	}
    /**
    * 退单类型表 table列表
    *
    * @return
    */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTCHARGEBACKTYPE_APPLY)
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return btChargeBackTypeService.getDataTablesUnion(dataTablesInput);
    }
	/**
	* 退单类型表新增页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTCHARGEBACKTYPE_APPLY)
	@GetMapping("/add")
	public String add(BtChargeBackType btChargeBackType, Model model) {
		if (StringUtils.isNotEmpty(btChargeBackType.getId())) {
			btChargeBackType = btChargeBackTypeService.get(btChargeBackType.getId());
			model.addAttribute("btChargeBackTypePage", btChargeBackType);
		}
		return "netbank/addBtChargeBackType";
	}
	/**
	* 退单类型表编辑页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTCHARGEBACKTYPE_APPLY)
	@GetMapping("/update")
	public String update(BtChargeBackType btChargeBackType, Model model) {
		if (StringUtils.isNotEmpty(btChargeBackType.getId())) {
			btChargeBackType = btChargeBackTypeService.getById(btChargeBackType.getId());
			model.addAttribute("btChargeBackTypePage", btChargeBackType);
		}
		return "netbank/updateBtChargeBackType";
	}
	/**
	* 退单类型表详情页
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTCHARGEBACKTYPE_APPLY)
	@LogMessage(content = "查询退单类型表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		BtChargeBackTypeVO btChargeBackType = btChargeBackTypeService.getByIdUnion(id);
		model.addAttribute("btChargeBackTypePage", btChargeBackType);
		return "netbank/viewBtChargeBackType";
	}
	/**
	 * 添加退单类型表
	 * @param btChargeBackType
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTCHARGEBACKTYPE_APPLY)
	@LogMessage(content = "添加退单类型表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(BtChargeBackType btChargeBackType){
		if (btChargeBackTypeService.findByCode(btChargeBackType.getBusType()) != null) {
            throw new ParameterException(NetbankMessageEnum.NIS_BACKTYPE_MESSAGE_BUSTYPE_UNIQUE);
        }
		btChargeBackType.setCreateBy(this.getCurrentUser().getLoginName());
		btChargeBackType.setCreateDate(DateUtils.getDate());
		this.btChargeBackTypeService.save(btChargeBackType, LocaleContextHolder.getLocale().toString());
		return success();
	}
	/**
	* 修改退单类型表
	* @param btChargeBackType
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTCHARGEBACKTYPE_APPLY)
	@LogMessage(content = "修改退单类型表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(BtChargeBackType btChargeBackType){
		BtChargeBackType t = btChargeBackTypeService.getById(btChargeBackType.getId());
		BeanUtils.copyNotNullProperties(btChargeBackType, t);
		t.setUpdateBy(this.getCurrentUser().getLoginName());
		t.setUpdateDate(DateUtils.getDate());
		this.btChargeBackTypeService.saveOrUpdate(t, LocaleContextHolder.getLocale().toString());
		return  success();
	}
	/**
	* 删除退单类型表
	* @param id
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTCHARGEBACKTYPE_APPLY)
	@LogMessage(content = "删除退单类型表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		BtChargeBackType btChargeBackType = btChargeBackTypeService.get(id);
		btChargeBackTypeService.deleteEntity(btChargeBackType);
		return success();
	}
	/**
	* 批量删除退单类型表
	* @param ids
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTCHARGEBACKTYPE_APPLY)
	@LogMessage(content = "批量删除退单类型表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson deleteDelete(@RequestParam("ids") String ids){
		for(String id : ids.split(CommonConstant.COMMA_SPLIT)){
			BtChargeBackType btChargeBackType = btChargeBackTypeService.get(id);
			btChargeBackTypeService.deleteEntity(btChargeBackType);
		}
		return success();
	}
	/**
	 * 获取有效的退单类型
	 *
	 * @return
	 */
	@GetMapping("/json")
	@ResponseBody
	public List<BtChargeBackTypeVO> json() {
		return btChargeBackTypeService.getValidChargeBackType();
	}
}
