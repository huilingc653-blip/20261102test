package com.bytter.biz.finacial.paramconfig.controller;
import com.bytter.biz.finacial.paramconfig.entity.FcBusRace;
import com.bytter.biz.finacial.paramconfig.service.FcBusRaceService;
import com.bytter.biz.finacial.paramconfig.vo.FcBusRaceVo;
import com.bytter.biz.finacial.paramconfig.vo.FcSysBusRaceVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.biz.finacial.api.constant.ResourceConstant;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
/**   
 * @Title: Controller  
 * @Description: 业务种类
 * @author lixi
 * @date 2017-05-17 10:17:16
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/paramconfig/fcbusrace")
public class FcBusRaceController extends BaseController {
	@Autowired
	private FcBusRaceService fcBusRaceService;
	/**
	* 业务种类列表 页面跳转
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCBUSRACE_APPLY)
	@LogMessage(content = "查询业务种类列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "finacial/paramconfig/fcbusrace/listFcBusRace";
	}
	
    /**
    * 业务种类 table列表
    */
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCBUSRACE_APPLY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return fcBusRaceService.findListBysql(dataTablesInput);
    }
    
	/**
	* 业务种类新增页面跳转
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCBUSRACE_APPLY)
	@GetMapping("/add")
	public String add(FcBusRace fcBusRace, Model model) {
		if (StringUtils.isNotEmpty(fcBusRace.getId())) {
			FcBusRaceVo race = fcBusRaceService.queryById(fcBusRace.getId());
			model.addAttribute("fcBusRacePage", race);
		}
		return "finacial/paramconfig/fcbusrace/addFcBusRace";
	}
	
	/**
	* 业务种类编辑页面跳转
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCBUSRACE_APPLY)
	@GetMapping("/update")
	public String update(FcBusRace fcBusRace, Model model) {
		if (StringUtils.isNotEmpty(fcBusRace.getId())) {
			fcBusRace = fcBusRaceService.get(fcBusRace.getId());
			model.addAttribute("fcBusRacePage", fcBusRace);
		}
		return "finacial/paramconfig/fcbusrace/updateFcBusRace";
	}
	
	/**
	* 业务种类详情页
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCBUSRACE_APPLY)
	@LogMessage(content = "查询业务种类详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		//FcBusRace fcBusRace = fcBusRaceService.get(id);
		FcBusRaceVo fcBusRace = fcBusRaceService.findFcBusRaceById(id);
		model.addAttribute("fcBusRacePage", fcBusRace);
		return "finacial/paramconfig/fcbusrace/viewFcBusRace";
	}
	
	/**
	* 业务种类批量修改详情页
	*/
	@LogMessage(content = "查询业务种类详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/toBatchUpdate")
	public String toBatchUpdate(FcBusRaceVo fcBusRaceVo, Model model) {
		//FcBusRace fcBusRace = fcBusRaceService.get(ids);
		model.addAttribute("fcBusRaceVoPage", fcBusRaceVo);
		model.addAttribute("method", "batchUpdate");
		return "finacial/paramconfig/fcbusrace/editFcBusRace";
	}
	
	/**
	* 业务种类批量复制详情页
	*/
	@LogMessage(content = "查询业务种类详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/toCopyFcBusRace")
	public String toCopyFcBusRace(FcBusRaceVo fcBusRaceVo, Model model) {
		//FcBusRace fcBusRace = fcBusRaceService.get(ids);
		model.addAttribute("fcBusRaceVoPage", fcBusRaceVo);
		model.addAttribute("method", "copyFcBusRace");
		return "finacial/paramconfig/fcbusrace/editFcBusRace";
	}
	/**
	 * 添加业务种类
	 */
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCBUSRACE_APPLY)
	@LogMessage(content = "添加业务种类",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(FcBusRaceVo fcBusRaceVo){
		FcBusRace fcBusRace = new FcBusRace();
		BeanUtils.copyNotNullProperties(fcBusRaceVo, fcBusRace);
		String[] netCodeArr = fcBusRaceVo.getNetIds().split(",");
		fcBusRaceService.save(fcBusRace, netCodeArr, getUserId());
		return success();
	}
	
	/**
	* 修改业务种类
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCBUSRACE_APPLY)
	@LogMessage(content = "修改业务种类",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(FcBusRace fcBusRace){
		fcBusRaceService.update(fcBusRace, getUserId());
		return  success();
	}
	
	/**
	* 删除业务种类
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCBUSRACE_APPLY)
	@LogMessage(content = "删除业务种类",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		fcBusRaceService.deleteFcbusRace(id);
		return success();
	}
	
	/**
	* 批量修改
	*/
	////@RequiresPermissions("btsummary:btsummary:delete")
	@LogMessage(content = "批量删除业务种类",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchUpdate")
	@ResponseBody
	public AjaxJson batchUpdate(FcBusRaceVo fcBusRaceVo){
		String netId = fcBusRaceVo.getNetIds();
		String[] idArr = fcBusRaceVo.getIds().split(",");
		fcBusRaceService.batchUpdate(idArr, netId, getUserId());
		return success();
	}
	
	/**
	* 批量复制
	*/
	////@RequiresPermissions("btsummary:btsummary:delete")
	@LogMessage(content = "批量复制业务种类",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/copyFcBusRace")
	@ResponseBody
	public AjaxJson copyFcBusRace(FcBusRaceVo fcBusRaceVo){
		String ids = fcBusRaceVo.getIds();
		String netIds = fcBusRaceVo.getNetIds();
		fcBusRaceService.copyFcBusRace(ids, netIds, getUserId());
		return success();
	}
	
	/**
	 * 系统业务种类
	 */
	@GetMapping("/json")
	@ResponseBody
	public List<FcSysBusRaceVO> getListBySysRaceCode() {
		return fcBusRaceService.getListBySysRaceCode();
	}
	
	/**
	 * 获取网点拥有的业务总类
	 */
	@GetMapping("/netJson")
	@ResponseBody
	public List<FcBusRace> getNetRaceCode(String netId) {
		if(EmptyUtils.isEmpty(netId)){
			return new ArrayList<FcBusRace>();
		}else{
			return fcBusRaceService.getNetRaceCode(netId);
		}
	}
	
	
	/**初始化**/
	@PostMapping("/initData")
	@ResponseBody
	public AjaxJson initData(){
		fcBusRaceService.initData();
		return success();
	}
}
