package com.bytter.check.web.controller;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.check.entity.FcCompareAcc;
import com.bytter.check.service.ErpDataManagerService;
import com.bytter.check.vo.ErpDataVO;
import com.bytter.check.vo.FcCompareAccVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.dto.AccountSearchDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
/**   
 * @Title: Controller  
 * @Description: REP数据管理
 * @author 吕伟林
 * @date 2017-06-08
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/finacial/check/erpDataManager")
public class ErpDataManagerController extends BaseController{
	@Autowired
	private ErpDataManagerService erpDataManagerService;
	@DubboReference(check = false)
	private IBtBankAccService btBankAccServiceImpl;
	/**
	 * 
	 * 功能介绍 : 进入《ERP数据维护》列表查询页面<br>	
	 * @param model
	 * @return
	 * <b>日期：</b>2017-5-31<br> <br>
	 * @version 1.0
	 * @author lvwl
	 */
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_ERP_DATA_QUERY)
	@LogMessage(content = "查询ERP数据维护列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
		model.addAttribute("checkType", FinacialConstant.CHECK_TYPE_ERP);
        return "check/erpDataManager/listErpDataManager";
	}
   
	/**
	 * 
	 * 功能介绍 : 获取《ERP数据维护》列表数据<br>	
	 * @param dataTablesInput
	 * @return
	 * <b>日期：</b>2017-5-31<br> <br>
	 * @version 1.0
	 * @author lvwl
	 */
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_ERP_DATA_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
    	//DataTablesOutput out = erpDataManagerService.getDataTables(dataTablesInput);
		DataTablesOutput out = erpDataManagerService.getErpDataTables(dataTablesInput,getUserId());
		return out;
    }
    
	/**
	* ERP数据维护新增页面跳转
	* @return
	*/
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_ERP_DATA_EDIT)
	@GetMapping("/add")
	public String add(FcCompareAcc fcCompareAcc, Model model) {
		if (StringUtils.isNotEmpty(fcCompareAcc.getId())) {
			fcCompareAcc = erpDataManagerService.get(fcCompareAcc.getId());
			model.addAttribute("fcCompareAccPage", fcCompareAcc);
		}
		//页面默认值
		model.addAttribute("redblueSign",FinacialConstant.REDBLUE_SIGN_BLUE);
		model.addAttribute("compareSign",FinacialConstant.COMPARE_SIGN_NO);
		return "check/erpDataManager/addErpDataManager";
	}
	
	/**
	* ERP数据维护编辑页面跳转
	* @return
	*/
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_ERP_DATA_EDIT)
	@GetMapping("/update/{id}")
	public String update(@PathVariable("id") String id, Model model) {
		FcCompareAcc fcCompareAcc = new FcCompareAcc();
		if (StringUtils.isNotEmpty(id)) {
			fcCompareAcc = erpDataManagerService.get(id);
		}
		model.addAttribute("fcCompareAccPage", fcCompareAcc);
		return "check/erpDataManager/updateErpDataManager";
	}
	/**
	 * 添加ERP数据
	 * @param 
	 * @return
	 */
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_ERP_DATA_EDIT)
	@LogMessage(content = "添加FC_COMPARE_ACC",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(ErpDataVO dataVo){
		erpDataManagerService.saveList(dataVo,"");
		return success();
	}
	/**
	* 修改ERP数据
	* @param fcCompareAcc
	* @return
	*/
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_ERP_DATA_EDIT)
	@LogMessage(content = "修改ERP数据",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(FcCompareAcc fcCompareAcc){
		FcCompareAcc t = erpDataManagerService.get(fcCompareAcc.getId());
		BeanUtils.copyNotNullProperties(fcCompareAcc, t);
		erpDataManagerService.saveOrUpdate(t);
		return  success();
	}
	/**
	* 删除ERP数据
	* @param id
	* @return
	*/
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_ERP_DATA_DELETE)
	@LogMessage(content = "删除ERP数据",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id) {
		erpDataManagerService.deleteEntity(id);
		return success();
	}
	/**
	* 批量删除ERP数据
	* @param ids
	* @return
	*/
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_ERP_DATA_DELETE)
	@LogMessage(content = "批量删除ERP数据",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(@RequestParam("ids[]") List<String> ids){
        for(String id : ids) {
		    erpDataManagerService.deleteEntity(id);
        }
		return success();
	}
	
	/**
	 * 
	 * 功能介绍 : ERP接口导入列表<br>	
	 * @param model
	 * @return
	 * <b>日期：</b>2017-6-9<br> <br>
	 * @version 1.0
	 * @author lvwl
	 */
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_ERP_DATA_INTERFACE_QUERY)
	@LogMessage(content = "查询ERP接口导入列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/listInter")
	public String listInter(Model model) {
		
        return "check/erpDataManager/listInterErpDataManager";
	}
	
	/**
	 * 
	 * 功能介绍 : 获取ERP接口数据（列表）<br>	
	 * @param dataTablesInput
	 * @return
	 * <b>日期：</b>2017-6-9<br> <br>
	 * @version 1.0
	 * @author lvwl
	 */
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_ERP_DATA_INTERFACE_QUERY)
	@PostMapping("/interDataTable")
    @ResponseBody
    public DataTablesOutput interDataTable(@RequestBody DataTablesInput dataTablesInput){
    	DataTablesOutput out = erpDataManagerService.listInter(dataTablesInput,getUserId());
		return out;
    }
	/**
	 * 功能介绍 : ERP接口导入提取<br>
	 *
	 * @param
	 * @return <b>日期：</b>2017-6-13<br> <br>
	 * @version 1.0
	 * @author lvwl
	 */
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_ERP_DATA_INTERFACE_EXTRACT)
	@LogMessage(content = "ERP接口导入提取", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/distillInter")
	@ResponseBody
	public AjaxJson distillInter(@RequestParam(value = "ids", required = false) String ids,
								 @RequestParam(value = "beginDate", required = false) String beginDate,
								 @RequestParam(value = "endDate", required = false) String endDate,
								 @RequestParam(value = "corpId", required = false) String corpId,
								 @RequestParam(value = "bankAcc", required = false) String bankAcc) {
		List<String> idss = new ArrayList<String>();
		if (StringUtils.isNotEmpty(ids)) {
			String[] id = ids.split(",");
			if (id != null && id.length > 0) {
				for (int i = 0; i < id.length; i++) {
					idss.add(id[i]);
				}
			}
		}
		erpDataManagerService.distillInter(idss);
		erpDataManagerService.updateCompAccExt(idss, beginDate, endDate, corpId, bankAcc, getCurrentUser().getId());//把中间表id对应的状态改为1
		return success();
	}
    
	
	/**
	 * 功能介绍 : ERP接口导入数据生成(保存)<br>	
	 * @param ids
	 * @return
	 * <b>日期：</b>2017-6-13<br> <br>
	 * @version 1.0
	 * @author lvwl
	 */
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_ERP_DATA_INTERFACE_SAVE)
	@LogMessage(content = "ERP接口导入数据生成",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/doAccInter")
	@ResponseBody
    public AjaxJson doAccInter(@RequestParam("ids") String ids,@RequestParam("bankAcc") String bankAcc,@RequestParam("curId") String curId){
		String[] id = ids.split(",");
		List<String> idss = new ArrayList<String>();
		if(id != null && id.length > 0){
			for(int i = 0; i < id.length; i++){
				idss.add(id[i]);
			}
		}
		erpDataManagerService.doAccInter(idss,bankAcc,curId);
		erpDataManagerService.updateCompAccExt(idss, null, null, null, null, null);
		return success();
    }
	
	
	/**
	 * 功能介绍 :ERP数据导入页面 <br>	
	 * @param
	 * @return
	 * <b>日期：</b>2017-6-13<br> <br>
	 * @version 1.0
	 * @author lvwl
	 */
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_ERP_DATA_IMPROT)
	@LogMessage(content = "ERP数据导入页面",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/improtData")
	public String improtData(Model model) {
        return "check/erpDataManager/improtTemplateDataManager";
	}
	/**ERP数据导入*/
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_ERP_DATA_IMPROT)
	@PostMapping("/erpImpDatatable")
	@ResponseBody
	public DataTablesOutput erpImpDatatable(@RequestBody  DataTablesInput dataTablesInput){
		return erpDataManagerService.erpImpDatatable(dataTablesInput,getUserId());
	}
	
	/**
	 * 功能介绍 : 导入文件数据保存<br>	
	 * @param 
	 * @return
	 * <b>日期：</b>2017-6-13<br> <br>
	 * @version 1.0
	 * @author lvwl
	 */
	@LogMessage(content = "ERP数据导入",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@PostMapping("/impDataSave")
	@ResponseBody
	public AjaxJson impDataSave(@RequestBody List<FcCompareAccVO> list, String bankAcc, String curId){
		erpDataManagerService.impDataSave(list, bankAcc,curId);
		return success();
	}
	
	
	/**
	 * 
	 * 功能介绍 : url获取：银行账号、开户行、财务账号<br>	
	 * @return
	 * <b>日期：</b>2017-6-2<br> <br>
	 * @version 1.0
	 * @author lvwl
	 */
	@PostMapping("/jsonBankAcc")
	@ResponseBody
	public List<BtBankAccDTO> jsonBankAcc(){
		//TODO
		//获取信息
		AccountSearchDTO accountSearchDTO = new AccountSearchDTO();
		/*accountSearchDTO.setDistinct(true);
		accountSearchDTO.setBifCode("2");*/
        accountSearchDTO.setContainDelBankAcc(false);
		List<BtBankAccDTO> list = btBankAccServiceImpl.getBankAccByAccountDTO(accountSearchDTO);
		
		return list;
	}
	
	/**
	 * erp导入银行帐号
	 * @return
	 */
	@PostMapping("/jsonBankAccErpData")
	@ResponseBody
	public List<BtBankAccDTO> getBankAccsErpData(){
		List<BtBankAccDTO> list = erpDataManagerService.getBankAccsErpData(getCurrentUser());
		return list;
	}
	/**
	 * 跳转多选银行行号控件页面
	 */
	@GetMapping("/erpMulBankAccInfo")
	public String mulBankAccInfo(String netId,String curId,String resource,Model model){
		model.addAttribute("netId", netId);
		model.addAttribute("curId", curId);
		model.addAttribute("resource", resource);
		return "check/fcCompareErp/erpMulBankAccInfo";
	}
	/**
	 * 多选银行账号控件获取数据
	 */
	@PostMapping("/getBankAccsErpData")
	@ResponseBody
	public DataTablesOutput getBankAccsErpData(@RequestBody DataTablesInput input){
		return erpDataManagerService.getBankAccsErpData(input,this.getCurrentUser());
	}
	
}
