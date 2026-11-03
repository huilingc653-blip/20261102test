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
import com.bytter.system.entity.BtAccNature;
import com.bytter.system.service.BtAccNatureService;
import com.bytter.system.vo.BtAccNatureVO;
/**
 * @Title: Controller
 * @Description: 账户性质表
 * @author onlineGenerator
 * @date 2017-05-12 17:06:43
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/bankacc/btaccnature")
public class BtAccNatureController extends BaseController {
	@Autowired
	private BtAccNatureService btAccNatureService;
	/**
	* 账户性质表列表 页面跳转
	*
	* @return
	*/
	////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTACCNATURE_APPLY)
	@LogMessage(content = "查询账户性质表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "bankacc/listBtAccNature";
	}
    /**
    * 账户性质表 table列表
    *
    * @return
    */
	////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTACCNATURE_APPLY)
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
		return btAccNatureService.dataTable(dataTablesInput);
    }
	/**
	* 账户性质表新增页面跳转
	*
	* @return
	*/
	////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTACCNATURE_APPLY)
	@GetMapping("/add")
	public String add(BtAccNature btAccNature, Model model) {
		if (StringUtils.isNotEmpty(btAccNature.getId())) {
			btAccNature = btAccNatureService.get(btAccNature.getId());
			model.addAttribute("btAccNaturePage", btAccNature);
		}
		return "bankacc/addBtAccNature";
	}
	/**
	* 账户性质表编辑页面跳转
	*
	* @return
	*/
	////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTACCNATURE_APPLY)
	@GetMapping("/update")
	public String update(BtAccNature btAccNature, Model model) {
		if (StringUtils.isNotEmpty(btAccNature.getId())) {
			btAccNature = btAccNatureService.get(btAccNature.getId());
			model.addAttribute("btAccNaturePage", btAccNature);
		}
		return "bankacc/updateBtAccNature";
	}
	/**
	* 账户性质表详情页
	*
	* @return
	*/
	////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTACCNATURE_APPLY)
	@LogMessage(content = "查询账户性质表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		BtAccNatureVO btAccNature = btAccNatureService.getVO(id);
		model.addAttribute("btAccNaturePage", btAccNature);
		return "bankacc/viewBtAccNature";
	}
	/**
	 * 添加账户性质表
	 * @param btAccNature
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTACCNATURE_APPLY)
	@LogMessage(content = "添加账户性质表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(BtAccNature btAccNature){
		//判断账户性质编码是否重复
		boolean result = this.btAccNatureService.isRepeatCurCode(btAccNature.getId(), btAccNature.getNatureCode());
		if(result){
			throw new ParameterException(NetbankMessageEnum.NIS_ACCNATURE_MESSAGE_NATURECODE_UNIQUE);
		}
		btAccNature.setCreateBy(this.getCurrentUser().getLoginName());
		btAccNature.setCreateDate(DateUtils.getDate());
		btAccNatureService.save(btAccNature, LocaleContextHolder.getLocale().toString());
		return success();
	}
	/**
	* 修改账户性质表
	* @param btAccNature
	* @return
	*/
	////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTACCNATURE_APPLY)
	@LogMessage(content = "修改账户性质表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(BtAccNature btAccNature){
		//判断账户性质编码是否重复
		boolean result = this.btAccNatureService.isRepeatCurCode(btAccNature.getId(), btAccNature.getNatureCode());
		if(result){
			throw new ParameterException(NetbankMessageEnum.NIS_ACCNATURE_MESSAGE_NATURECODE_UNIQUE);
		}
		BtAccNature t = btAccNatureService.get(btAccNature.getId());
		BeanUtils.copyNotNullProperties(btAccNature, t);
		t.setUpdateBy(this.getCurrentUser().getLoginName());
		t.setUpdateDate(DateUtils.getDate());
		this.btAccNatureService.saveOrUpdate(t, LocaleContextHolder.getLocale().toString());
		return  success();
	}
	/**
	* 删除账户性质表
	* @param id
	* @return
	*/
	////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTACCNATURE_APPLY)
	@LogMessage(content = "删除账户性质表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		BtAccNature btAccNature = btAccNatureService.get(id);
		btAccNatureService.deleteBtAccNature(btAccNature);
		return success();
	}
	/**
	* 批量删除账户性质表
	* @param ids
	* @return
	*/
	////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTACCNATURE_APPLY)
	@LogMessage(content = "批量删除账户性质表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(@RequestParam("ids")String ids){
		this.btAccNatureService.batchDelete(ids);
		return success();
	}
	@GetMapping("/json")
    @ResponseBody
    public List<BtAccNatureVO> json(){
        return btAccNatureService.getValidAccNature();
    }
}
