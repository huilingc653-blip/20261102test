package com.bytter.biz.finacial.paramconfig.controller;
import com.bytter.biz.finacial.paramconfig.entity.FcBusRacePool;
import com.bytter.biz.finacial.paramconfig.entity.FcSysBusRacePool;
import com.bytter.biz.finacial.paramconfig.service.FcBusRacePoolService;
import com.bytter.biz.finacial.paramconfig.vo.FcBusRacePoolVo;
import com.bytter.biz.finacial.paramconfig.vo.FcBusRaceVo;
import com.bytter.system.api.IFcCorpPoolService;
import com.bytter.system.api.dto.FcCorpPoolDTO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
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
 * @Description: 资金池业务种类
 * @author zhengxn
 * @date 2019-5-6
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/paramconfig/fcbusracepool")
public class FcBusRacePoolController extends BaseController {
	@Autowired
	private FcBusRacePoolService fcBusRacePoolService;
	@DubboReference(check = false)
	private IFcCorpPoolService iFcCorpPoolService;
	/**
	* 业务种类列表 页面跳转----
	*/
	//@RequiresPermissions("paramconfig:fcbusracepool:query")
	@LogMessage(content = "查询资金池业务种类列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "finacial/paramconfig/fcbusracepool/listFcBusRacePool";
	}
	
    /**
    * 业务种类 table列表-----
    */
    //@RequiresPermissions("paramconfig:fcbusracepool:query")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return fcBusRacePoolService.findListBysql(dataTablesInput);
    }
    
	/**
	* 业务种类新增页面跳转-----
	*/
	//@RequiresPermissions("paramconfig:fcbusracepool:apply")
	@GetMapping("/add")
	public String add(FcBusRacePool fcBusRace, Model model) {
		if (StringUtils.isNotEmpty(fcBusRace.getId())) {
			fcBusRace = fcBusRacePoolService.get(fcBusRace.getId());
			model.addAttribute("fcBusRacePage", fcBusRace);
		}
		return "finacial/paramconfig/fcbusracepool/addFcBusRacePool";
	}
	
	/**
	* 业务种类编辑页面跳转------
	*/
	//@RequiresPermissions("paramconfig:fcbusracepool:apply")
	@GetMapping("/update")
	public String update(FcBusRacePool fcBusRace, Model model) {
		if (StringUtils.isNotEmpty(fcBusRace.getId())) {
			fcBusRace = fcBusRacePoolService.get(fcBusRace.getId());
			model.addAttribute("fcBusRacePage", fcBusRace);
		}
		return "finacial/paramconfig/fcbusracepool/updateFcBusRacePool";
	}
	
	/**
	* 业务种类详情页---------
	*/
	//@RequiresPermissions("paramconfig:fcbusracepool:query")
	@LogMessage(content = "查询资金池业务种类详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		//FcBusRace fcBusRace = fcBusRacePoolService.get(id);
		FcBusRaceVo fcBusRace = fcBusRacePoolService.findFcBusRacePoolById(id);
		model.addAttribute("fcBusRacePage", fcBusRace);
		return "finacial/paramconfig/fcbusracepool/viewFcBusRacePool";
	}
	
	/**
	* 资金池业务种类批量修改详情页
	*/
	@LogMessage(content = "查询资金池业务种类批量修改详情页",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/toBatchUpdate")
	public String toBatchUpdate(FcBusRacePoolVo fcBusRacePoolVo, Model model) {
		//FcBusRace fcBusRace = fcBusRacePoolService.get(ids);
		model.addAttribute("fcBusRaceVoPage", fcBusRacePoolVo);
		model.addAttribute("method", "batchUpdate");
		return "finacial/paramconfig/fcbusracepool/editFcBusRacePool";
	}
	
	/**
	* 业务种类批量复制详情页
	*/
	@LogMessage(content = "查询资金池业务种类详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/toCopyFcBusRace")
	public String toCopyFcBusRace(FcBusRacePoolVo fcBusRacePoolVo, Model model) {
		//FcBusRace fcBusRace = fcBusRacePoolService.get(ids);
		model.addAttribute("fcBusRaceVoPage", fcBusRacePoolVo);
		model.addAttribute("method", "copyFcBusRace");
		return "finacial/paramconfig/fcbusracepool/editFcBusRacePool";
	}
	/**
	 * 添加业务种类
	 */
	//@RequiresPermissions("paramconfig:fcbusracepool:apply")
	@LogMessage(content = "添加资金池业务种类",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(FcBusRacePoolVo fcBusRacePoolVo){
		FcBusRacePool fcBusRacePool = new FcBusRacePool();
		BeanUtils.copyNotNullProperties(fcBusRacePoolVo, fcBusRacePool);
		String[] poolIds = fcBusRacePoolVo.getPoolId().split(",");
		fcBusRacePoolService.save(fcBusRacePool, poolIds, getUserId());
		return success();
	}
	
	/**
	* 修改业务种类
	*/
	//@RequiresPermissions("paramconfig:fcbusracepool:apply")
	@LogMessage(content = "修改资金池业务种类",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(FcBusRacePool fcBusRace){
		fcBusRacePoolService.update(fcBusRace, getUserId());
		return  success();
	}
	
	/**
	* 删除业务种类
	*/
	//@RequiresPermissions("paramconfig:fcbusracepool:apply")
	@LogMessage(content = "删除资金池业务种类",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		fcBusRacePoolService.deleteFcbusRacePool(id);
		return success();
	}
	
	/**
	* 批量修改
	*/
	//@RequiresPermissions("paramconfig:fcbusracepool:apply")
	@LogMessage(content = "批量修改资金池业务种类",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchUpdate")
	@ResponseBody
	public AjaxJson batchUpdate(FcBusRacePoolVo fcBusRacePoolVo){
		String poolId = fcBusRacePoolVo.getPoolId();
		String[] idArr = fcBusRacePoolVo.getIds().split(",");
		fcBusRacePoolService.batchUpdate(idArr, poolId, getUserId());
		return success();
	}
	
	/**
	* 批量复制-----
	*/
	//@RequiresPermissions("paramconfig:fcbusracepool:apply")
	@LogMessage(content = "批量复制资金池业务种类",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/copyFcBusRace")
	@ResponseBody
	public AjaxJson copyFcBusRace(FcBusRacePoolVo fcBusRacePoolVo){
		String ids = fcBusRacePoolVo.getIds();
		String poolIds = fcBusRacePoolVo.getPoolId();
		fcBusRacePoolService.copyFcBusRacePool(ids, poolIds, getUserId());
		return success();
	}
	
	/**
	 * 系统业务种类-------
	 */
	@GetMapping("/json")
	@ResponseBody
	public List<FcSysBusRacePool> getListBySysRaceCode() {
		return fcBusRacePoolService.getListBySysRaceCode();
	}
	
	/**
	 * 获取资金池拥有的业务总类----
	 */
	@GetMapping("/netJson")
	@ResponseBody
	public List<FcBusRacePool> getNetRaceCode(String netId) {
		if(EmptyUtils.isEmpty(netId)){
			return new ArrayList<FcBusRacePool>();
		}else{
			return fcBusRacePoolService.getPoolIdRaceCode(netId);
		}
	}
	
	/**初始化**/
	@PostMapping("/initData")
	@ResponseBody
	public AjaxJson initData(){
		fcBusRacePoolService.initData();
		return success();
	}
	
	/**
	 * 获取资金池主体
	 * @return
	 */
	@GetMapping("/getCorpPools")
	@ResponseBody
	public List<FcCorpPoolDTO> getUserCorpPools() {
		String resource = "paramconfig:fcbusracepool:query";
		return iFcCorpPoolService.getUserCorpPools(this.getCurrentUser(), resource);
	}
}
