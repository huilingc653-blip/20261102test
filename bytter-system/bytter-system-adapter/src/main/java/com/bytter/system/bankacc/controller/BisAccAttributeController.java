package com.bytter.system.bankacc.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
import com.bytter.system.api.enums.NetbankMessageEnum;
import com.bytter.system.constants.ResourceConstant;
import com.bytter.system.entity.BisAccAttribute;
import com.bytter.system.service.BisAccAttributeService;
import com.bytter.system.vo.BisAccAttributeVO;
/**
 * @author onlineGenerator
 * @version V1.0
 * @Title: Controller
 * @Description: 账户属性表
 * @date 2017-05-12 17:18:17
 */
@Controller
@RequestMapping("/bankacc/bisaccattribute")
public class BisAccAttributeController extends BaseController {
	@Autowired
	private BisAccAttributeService bisAccAttributeService;
	/**
	 * 账户属性表列表 页面跳转
	 *
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISACCATTRIBUTE_APPLY)
	@LogMessage(content = "查询账户属性表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
		return "bankacc/listBisAccAttribute";
	}
	/**
	 * 判断账户属性是否在使用中
	 *
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISACCATTRIBUTE_APPLY)
	@LogMessage(content = "判断账户属性是否在使用中", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/queryUse/{id}")
	@ResponseBody
	public String queryUse(@PathVariable("id") String id) {
		if(this.bisAccAttributeService.queryUse(id)){   //使用中
			return "true";
		}
		return "false";   //未使用
	}
	/**
	 * 账户属性表 table列表
	 *
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISACCATTRIBUTE_APPLY)
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return bisAccAttributeService.dataTable(dataTablesInput);
	}
	/**
	 * 账户属性表新增页面跳转
	 *
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISACCATTRIBUTE_APPLY)
	@GetMapping("/add")
	public String add(BisAccAttribute bisAccAttribute, Model model) {
		if (StringUtils.isNotEmpty(bisAccAttribute.getId())) {
			bisAccAttribute = bisAccAttributeService.get(bisAccAttribute.getId());
			model.addAttribute("bisAccAttributePage", bisAccAttribute);
		}
		return "bankacc/addBisAccAttribute";
	}
	/**
	 * 账户属性表编辑页面跳转
	 *
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISACCATTRIBUTE_APPLY)
	@GetMapping("/update")
	public String update(BisAccAttribute bisAccAttribute, Model model) {
		if (StringUtils.isNotEmpty(bisAccAttribute.getId())) {
			bisAccAttribute = bisAccAttributeService.get(bisAccAttribute.getId());
			model.addAttribute("bisAccAttributePage", bisAccAttribute);
		}
		return "bankacc/updateBisAccAttribute";
	}
	/**
	 * 账户属性表详情页
	 *
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISACCATTRIBUTE_APPLY)
	@LogMessage(content = "查询账户属性表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		BisAccAttributeVO bisAccAttribute = bisAccAttributeService.getVO(id);
		model.addAttribute("bisAccAttributePage", bisAccAttribute);
		return "bankacc/viewBisAccAttribute";
	}
	/**
	 * 添加账户属性表
	 *
	 * @param bisAccAttribute
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISACCATTRIBUTE_APPLY)
	@LogMessage(content = "添加账户属性表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(BisAccAttribute bisAccAttribute) {
		if (bisAccAttributeService.findByCode(bisAccAttribute.getAttributeCode()) != null) {
			throw new ParameterException(NetbankMessageEnum.NIS_ACCATTR_MESSAGE_ATTRIBUTECODE_UNIQUE);
		}
		bisAccAttribute.setCreateBy(this.getCurrentUser().getLoginName());
		bisAccAttribute.setCreateDate(DateUtils.getDate());
		this.bisAccAttributeService.save(bisAccAttribute, LocaleContextHolder.getLocale().toString());
		return success();
	}
	/**
	 * 修改账户属性表
	 *
	 * @param bisAccAttribute
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISACCATTRIBUTE_APPLY)
	@LogMessage(content = "修改账户属性表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(BisAccAttribute bisAccAttribute) {
		BisAccAttribute t = bisAccAttributeService.get(bisAccAttribute.getId());
		BeanUtils.copyNotNullProperties(bisAccAttribute, t);
		t.setUpdateBy(this.getCurrentUser().getLoginName());
		t.setUpdateDate(DateUtils.getDate());
		this.bisAccAttributeService.saveOrUpdate(t, LocaleContextHolder.getLocale().toString());
		return success();
	}
	/**
	 * 删除账户属性表
	 *
	 * @param id
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISACCATTRIBUTE_APPLY)
	@LogMessage(content = "删除账户属性表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id) {
		BisAccAttribute bisAccAttribute = bisAccAttributeService.get(id);
		bisAccAttributeService.deleteBisAccAttribute(bisAccAttribute);
		return success();
	}
	/**
	 * 批量删除账户属性表
	 *
	 * @param ids
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISACCATTRIBUTE_APPLY)
	@LogMessage(content = "批量删除账户属性表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson deleteDelete(@RequestParam("ids") String ids) {
		this.bisAccAttributeService.batchDelete(ids);
		return success();
	}
	@GetMapping("/json")
	@ResponseBody
	public List<BisAccAttributeVO> json() {
		return bisAccAttributeService.getValidAttribute();
	}
}
