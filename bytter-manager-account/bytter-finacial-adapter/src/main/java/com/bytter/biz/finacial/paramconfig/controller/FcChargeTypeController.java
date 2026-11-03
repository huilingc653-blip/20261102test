package com.bytter.biz.finacial.paramconfig.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bytter.biz.finacial.paramconfig.entity.FcChargeType;
import com.bytter.biz.finacial.paramconfig.entity.FcChargeTypeDtl;
import com.bytter.biz.finacial.paramconfig.service.FcChargeTypeService;
import com.bytter.biz.finacial.paramconfig.vo.FcChargeTypeVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.BankTypeDTO;
/**   
 * @Title: Controller  
 * @Description: 费用类型表
 * @author onlineGenerator
 * @date 2017-05-15 15:14:21
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/paramconfig/fcchargetype")
public class FcChargeTypeController extends BaseController{
	@Autowired
	private FcChargeTypeService fcChargeTypeService;
	
	/**
	* 费用类型表列表 页面跳转
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCCHARGETYPE_APPLY)
	@LogMessage(content = "查询费用类型表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "finacial/paramconfig/fcchargtype/listFcChargeType";
	}
	
	/**
	 * <b>功能介绍:银行类别树</b><br>
	 * <b>日期：</b>2017年6月15日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 */
	@GetMapping("/bannkTypeTree")
    @ResponseBody
    public Object bannkTypeTree(String net) {
		List<BankTypeDTO> bankTypes = fcChargeTypeService.getBankTypeValid();
        List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
        for (BankTypeDTO bankType : bankTypes) {
        	Map<String, Object> map = new HashMap<String, Object>();
        	map.put("id", bankType.getId());
        	map.put("name", bankType.getTypeName());
        	tempList.add(map);
		}
        return tempList;
	}
	
    /**
    * 费用类型表 table列表
    *
    * @return
    */
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCCHARGETYPE_APPLY)
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return fcChargeTypeService.listFcChargeType(dataTablesInput);
    }
	/**
	* 费用类型表新增页面跳转
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCCHARGETYPE_APPLY)
	@GetMapping("/add")
	public String add(FcChargeType fcChargeType, Model model) {
		if (StringUtils.isNotEmpty(fcChargeType.getId())) {
			fcChargeType = fcChargeTypeService.get(fcChargeType.getId());
			model.addAttribute("fcChargeTypePage", fcChargeType);
		}
		return "finacial/paramconfig/fcchargtype/addFcChargeType";
	}
	/**
	* 费用类型表编辑页面跳转
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCCHARGETYPE_APPLY)
	@GetMapping("/update")
	public String update(FcChargeType fcChargeType, Model model) {
		if (StringUtils.isNotEmpty(fcChargeType.getId())) {
			fcChargeType = fcChargeTypeService.get(fcChargeType.getId());
			model.addAttribute("fcChargeTypePage", fcChargeType);
		}
		List<String> bankTypes = fcChargeTypeService.findByBankTypeByChargeId(fcChargeType.getId());//已关联银行类别
		StringBuilder bank = new StringBuilder();
		int i=0;
		for (String bankType : bankTypes) {
			if(i!=0){
				bank.append(","+bankType);
			}else{
				bank.append(bankType);
			}
			i++;
		}
		model.addAttribute("bankTypes", bank.toString());
		return "finacial/paramconfig/fcchargtype/updateFcChargeType";
	}
	
	
	/**
	 * <b>功能介绍:获取费用明细</b><br>
	 * <b>日期：</b>2017年6月15日<br>
	 * <b>作者：</b>caozx<br>
	 * @version 1.0
	 */
	@PostMapping("/listFcChargeTypeDtls")
    @ResponseBody
	public List<FcChargeTypeDtl> listFcChargeTypeDtls(String fcChargeTypeId){
		return fcChargeTypeService.findByChargeTypeDtls(fcChargeTypeId);
	}
	/**
	* 费用类型表详情页
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCCHARGETYPE_APPLY)
	@LogMessage(content = "查询费用类型表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		//FcChargeType fcChargeType = fcChargeTypeService.get(id);
		FcChargeTypeVo fcChargeType = fcChargeTypeService.findById(id);
		List<String> bankTypes = fcChargeTypeService.findByBankTypeByChargeId(fcChargeType.getId());//已关联银行类别
		List<String> bankTypeNames = fcChargeTypeService.findByBankTypeNameByBankTypeID(bankTypes);
		StringBuilder bankNames = new StringBuilder();
		int i=0;
		for (String typeName : bankTypeNames) {
			if(i!=0){
				bankNames.append(","+typeName);
			}else{
				bankNames.append(typeName);
			}
			i++;
		}
		model.addAttribute("bankNames", bankNames.toString());
		
		model.addAttribute("fcChargeTypePage", fcChargeType);
		return "finacial/paramconfig/fcchargtype/viewFcChargeType";
	}
	/**
	 * 添加费用类型表
	 * @param
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCCHARGETYPE_APPLY)
	@LogMessage(content = "添加费用类型表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(FcChargeTypeVo fcChargeTypeVo){
		fcChargeTypeService.saveFcChargeType(fcChargeTypeVo,getUserLoginName());
		return success();
	}
	/**
	* 修改费用类型表
	* @param
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCCHARGETYPE_APPLY)
	@LogMessage(content = "修改费用类型表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(FcChargeTypeVo fcChargeTypeVo){
		fcChargeTypeService.updateFcChargeType(fcChargeTypeVo,getUserLoginName());
		return  success();
	}
	/**
	* 删除费用类型表
	* @param id
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCCHARGETYPE_APPLY)
	@LogMessage(content = "删除费用类型表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		FcChargeType fcChargeType = fcChargeTypeService.get(id);
		fcChargeTypeService.deleteFcChargeType(fcChargeType);
		return success();
	}
	/**
	* 批量删除费用类型表
	* @param ids
	* @return
	*/
	////@RequiresPermissions("paramconfig:fcchargetype:delete")
	@LogMessage(content = "批量删除费用类型表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson deleteDelete(@ModelAttribute("ids")String ids){
		for(String id : ids.split(",")){
			FcChargeType fcChargeType = fcChargeTypeService.get(id);
			fcChargeTypeService.deleteEntity(fcChargeType);
		}
		return success();
	}
}
