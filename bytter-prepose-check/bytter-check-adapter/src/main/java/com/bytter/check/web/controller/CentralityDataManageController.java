package com.bytter.check.web.controller;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.check.entity.FcCompareAcc;
import com.bytter.check.service.CentralityDataManageService;
import com.bytter.check.service.ErpDataManagerService;
import com.bytter.check.vo.CentralityDataVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
/**   
 * @Title: Controller  
 * @Description: 中心数据管理
 * @author 吕伟林
 * @date 2017-05-31 15:28:08
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/finacial/check/centralityDataManage")
public class CentralityDataManageController extends BaseController {
	@Autowired
	private CentralityDataManageService centralityDataManageService;
	@Autowired
	private ErpDataManagerService erpDataManagerService;
	/**
	 * 
	 * 功能介绍 : 进入《对账单录入》列表查询页面<br>	
	 * @param model
	 * @return
	 * <b>日期：</b>2017-5-31<br> <br>
	 * @version 1.0
	 * @author lvwl
	 */
	@LogMessage(content = "查询FC_COMPARE_ACC列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
		model.addAttribute("checkType", FinacialConstant.CHECK_TYPE_CEN);
        return "check/centralityDataManage/listFcCompareAcc";
	}
   
	/**
	 * 
	 * 功能介绍 : 获取《对账单录入》列表数据<br>	
	 * @param dataTablesInput
	 * @return
	 * <b>日期：</b>2017-5-31<br> <br>
	 * @version 1.0
	 * @author lvwl
	 */
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
    	DataTablesOutput out = centralityDataManageService.dataTable(dataTablesInput,getUserId());
		return out;
    }
    
	/**
	* FC_COMPARE_ACC新增页面跳转
	*
	* @return
	*/
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_EDIT)
	@GetMapping("/add")
	public String add(FcCompareAcc fcCompareAcc, Model model) {
		if (StringUtils.isNotEmpty(fcCompareAcc.getId())) {
			fcCompareAcc = centralityDataManageService.get(fcCompareAcc.getId());
			model.addAttribute("fcCompareAccPage", fcCompareAcc);
		}
		//页面默认值
		model.addAttribute("redblueSign",FinacialConstant.REDBLUE_SIGN_BLUE);
		model.addAttribute("compareSign",FinacialConstant.COMPARE_SIGN_NO);
		return "check/centralityDataManage/addFcCompareAcc";
	}
	/**
	* FC_COMPARE_ACC编辑页面跳转
	*
	* @return
	*/
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_EDIT)
	@GetMapping("/update/{id}")
	public String update(@PathVariable("id") String id, Model model) {
		FcCompareAcc fcCompareAcc = new FcCompareAcc();
		if (StringUtils.isNotEmpty(id)) {
			fcCompareAcc = centralityDataManageService.get(id);
		}
		model.addAttribute("fcCompareAccPage", fcCompareAcc);
		return "check/centralityDataManage/updateFcCompareAcc";
	}
	/**
	 * 添加FC_COMPARE_ACC
	 * @param
	 * @return
	 */
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_EDIT)
	@LogMessage(content = "添加FC_COMPARE_ACC",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(CentralityDataVO dataVo){
		centralityDataManageService.saveList(dataVo);
		return success();
	}
	/**
	* 修改FC_COMPARE_ACC
	* @param fcCompareAcc
	* @return
	*/
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_EDIT)
	@LogMessage(content = "修改FC_COMPARE_ACC",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(FcCompareAcc fcCompareAcc){
		centralityDataManageService.saveCheck(fcCompareAcc, 0);
		FcCompareAcc t = centralityDataManageService.get(fcCompareAcc.getId());
		BeanUtils.copyNotNullProperties(fcCompareAcc, t);
		centralityDataManageService.saveOrUpdate(t);
		return  success();
	}
	/**
	* 删除FC_COMPARE_ACC
	* @param id
	* @return
	*/
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_DELETE)
	@LogMessage(content = "删除FC_COMPARE_ACC",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		FcCompareAcc fcCompareAcc = centralityDataManageService.get(id);
		centralityDataManageService.deleteEntity(fcCompareAcc);
		return success();
	}
	/**
	* 批量删除FC_COMPARE_ACC
	* @param ids
	* @return
	*/
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_DELETE)
	@LogMessage(content = "批量删除FC_COMPARE_ACC",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(@RequestParam("ids[]") List<String> ids){
		centralityDataManageService.batchDelete(ids);
		return success();
	}
	
	
	/**
     * 
     * 功能介绍 : 中心对账数据生成列表<br>	
     * @param model
     * @return
     * <b>日期：</b>2017-6-6<br> <br>
     * @version 1.0
     * @author lvwl
     */
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_CREATE_QUERY)
    @GetMapping("/listGen")
    public String listGen(Model model){
    	model.addAttribute("chenkType", FinacialConstant.CHECK_TYPE_CEN);
    	return "check/centralityDataManage/listGenFcCompareAcc";
    }
    
    /**
     * 
     * 功能介绍 : 获取《中心对账数据生》列表数据<br>	
     * @param dataTablesInput
     * @return
     * <b>日期：</b>2017-6-6<br> <br>
     * @version 1.0
     * @author lvwl
     */
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_CREATE_QUERY)
    @PostMapping("/genDataTable")
    @ResponseBody
    public DataTablesOutput genDataTable(@RequestBody DataTablesInput dataTablesInput){
    	DataTablesOutput out = centralityDataManageService.listGenAcc(dataTablesInput,getUserId());
		return out;
    }
    
    /**
     * 
     * 功能介绍 : 中心对账数据生成<br>	
     * @param params
     * @return
     * <b>日期：</b>2017-6-6<br> <br>
     * @version 1.0
     * @author lvwl
     */
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_CREATE)
    @LogMessage(content = "中心对账数据生成",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/doAccImportAgain")
	@ResponseBody
    public AjaxJson doAccImportAgain(@RequestBody Map<String,Object> params){
    	centralityDataManageService.doAccImportAgain(params);
		return success();
    }
	
	/**
	 * 
	 * 功能介绍 : url获取 ：凭证号、流水号信息<br>	
	 * @return
	 * <b>日期：</b>2017-6-2<br> <br>
	 * @version 1.0
	 * @author lvwl
	 */
	@PostMapping("/jsonVoucherNo")
	@ResponseBody
	public List<Map<String,String>> jsonVoucherNo(){
		//TODO
		//获取信息
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("voucherNo", "15000");
		map.put("orderNo", "201705160006");
		list.add(map);
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("voucherNo", "16000");
		map2.put("orderNo", "201705160009");
		list.add(map2);
		return list;
	}
	
	/**
	 * 
	 * 功能介绍 : url获取：中心账号、银行账号、开户行<br>	
	 * @return
	 * <b>日期：</b>2017-6-2<br> <br>
	 * @version 1.0
	 * @author lvwl
	 */
	@PostMapping("/jsonCompAcc")
	@ResponseBody
	public List<Map<String,String>> jsonCompAcc(){
		//TODO
		//获取信息
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("compAcc", "20015625");
		map.put("bankAcc", "01618800189834");
		map.put("bankName", "中国银行某山沟支行");
		list.add(map);
		return list;
	}
	
	
	@PostMapping("/validAccount")
	@ResponseBody
	public List<CentralityDataVO> getValidAccount(){
		return centralityDataManageService.getValidAccount(getUserId());
	}
}
