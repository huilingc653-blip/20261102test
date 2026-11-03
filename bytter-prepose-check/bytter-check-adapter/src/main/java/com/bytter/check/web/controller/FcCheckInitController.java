package com.bytter.check.web.controller;
import com.bytter.biz.finacial.api.constant.FinacialResourceConstant;
import com.bytter.check.service.FcCheckInitService;
import com.bytter.check.vo.FcCheckInitVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BtBankAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**   
 * @Title: Controller  
 * @Description: 银行中心余额初始表
 * @author LIXI
 * @date 2017-05-31 11:15:24
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/finacial/check/fccheckinit")
public class FcCheckInitController extends BaseController {
	@Autowired
	private FcCheckInitService fcCheckInitService;
	@DubboReference(check = false)
	private ICorpService corpService;
	/**
	* 银行中心余额初始表列表 页面跳转
	*/
	@LogMessage(content = "查询银行中心余额初始表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "check/fccheckinit/listFcCheckInit";
	}
    /**
    * 银行中心余额初始表 table列表
    */
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_BALANCE_INIT_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput,String checkType){
		return fcCheckInitService.getFcCheckInit(dataTablesInput,checkType,getUserId());
    }
	/**
	* 银行中心余额初始表页面跳转
	*/
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_BALANCE_INIT)
	@GetMapping("/add")
	public String add(FcCheckInitVo fci, Model model) {

		FcCheckInitVo fccheckinitvo = fcCheckInitService.fcCheckInit(fci);
		//进行了余额调节表编制以及结转的账号不能修改初始化日期和余额
		boolean isAdjust = fcCheckInitService.checkAdjust(fci);
		model.addAttribute("isAdjust", isAdjust);
		model.addAttribute("fcCheckInitPage", fccheckinitvo);
		return "check/fccheckinit/addFcCheckInit";
	}
	/**
	 * 添加银行中心余额初始表
	 */
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_CENTRALITY_BALANCE_INIT)
	@LogMessage(content = "添加银行中心余额初始表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(FcCheckInitVo fcCheckInitVo){
		fcCheckInitService.saveOrUpdate(fcCheckInitVo, getUserId());
		return success();
	}
	/**
	* 银行中心余额初始表列表 页面跳转
	*/
	////@RequiresPermissions("check:fccheckinit:index")
	@LogMessage(content = "查询银行中心余额初始表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/erpList")
	public String erpList(Model model) {
        return "check/fccheckinit/erpListFcCheckInit";
	}
	@GetMapping("/listfcerpcheckinit")
	public String getListFcErpCheckInit(){
		return "check/fcCheckErpInit/listFcErpCheckInit";
	}
	
	/**ERP余额初始化列表**/
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_ERP_BALANCE_INIT_QUERY)
	@PostMapping("/queryCheckInitList")
	@ResponseBody
	public DataTablesOutput queryCheckInitList(@RequestBody DataTablesInput dataTablesInput,String checkType){
		List<String> corpAuth = corpService.getUserCorpIds(FinacialResourceConstant.FINACIAL_CHECK_ERP_BALANCE_INIT_QUERY,getCurrentUser().getId());
		return fcCheckInitService.queryCheckInitList(dataTablesInput,null, null, checkType, corpAuth);
	}
	/**
	 * ERP余额初始表页面跳转
	 */
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_ERP_BALANCE_INIT)
	@GetMapping("/adderp")
	public String addErp(String bankAcc,String checkType, String curId, Model model) {
		DataTablesInput input = new  DataTablesInput();
		DataTablesOutput top = fcCheckInitService.queryCheckInitList(input,bankAcc, curId, checkType,null);
		List<Map<String,Object>> list = top.getData();
		Map<String,Object> map = new HashMap<>();
		if(!EmptyUtils.isEmptyList(list)){
			map = list.get(0);
		}
		model.addAttribute("vo", map);
		return "check/fcCheckErpInit/addFcCheckErpInit";
	}
	
	/**erp余额初始化保存**/
	@PostMapping("/erpsave")
	@ResponseBody
	public AjaxJson erpSave(FcCheckInitVo fcCheckInitVo){
		fcCheckInitService.erpSaveOrUpdate(fcCheckInitVo);
		return success();
	}
    /**
    * <br>
    * <b>功能：</b> 查询对账账号 <br>
    * <b>作者：</b>liyh<br>
    * <b>日期：</b> 2022/1/7 0007 <br>
    */
    @PostMapping("/jsonBankAccData")
    @ResponseBody
    public List<BtBankAccDTO> getBankAccsErpData(@RequestParam(required = false) String corpId, @RequestParam(required = false) String checkType) {
        List<BtBankAccDTO> list = fcCheckInitService.getInitBankAccsData(corpId, getCurrentUser().getId(), checkType);
        return list;
    }
	
}
