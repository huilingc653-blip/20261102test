package com.bytter.biz.finacial.paramconfig.controller;
import com.bytter.biz.finacial.api.enums.paramconfig.ParamConfigMessageEnum;
import com.bytter.biz.finacial.paramconfig.entity.FcBusRacePool;
import com.bytter.biz.finacial.paramconfig.entity.FcBusinessRuleDefinePool;
import com.bytter.biz.finacial.paramconfig.entity.FcSysBusRacePool;
import com.bytter.biz.finacial.paramconfig.service.FcBusRacePoolService;
import com.bytter.biz.finacial.paramconfig.service.FcBusinessRuleDefinePoolService;
import com.bytter.biz.finacial.paramconfig.vo.FcBusinessRuleDefinePoolVo;
import com.bytter.biz.finacial.paramconfig.vo.FcBusinessRuleDefineVo;
import com.bytter.biz.finacial.paramconfig.vo.FcSubjectPoolVO;
import com.bytter.system.api.dto.FcCorpPoolDTO;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IFcCorpPoolService;
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
 * @Description: FC_BUSINESS_RULE_DEFINE_POOL
 * @author onlineGenerator
 * @date 2019-5-6
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/paramconfig/fcbusinessruledefinepool")
public class FcBusinessRuleDefinePoolController extends BaseController {
	@Autowired
	private FcBusinessRuleDefinePoolService fcBusinessRuleDefinePoolService;
	@DubboReference(check = false)
	private IFcVoucherTypeService fcVoucherTypeService;
	@DubboReference(check = false)
	private IFcCorpPoolService iFcCorpPoolService;
	@Autowired
	private FcBusRacePoolService fcBusRacePoolService;
	/**
	*  列表页面跳转-----
	*/
	//@RequiresPermissions("paramconfig:fcbusinessruledefinepool:query")
	@LogMessage(content = "查询FC_BUSINESS_RULE_DEFINE_POOL列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "finacial/paramconfig/businessRulePool/listFcBusinessRuleDefinePool";
	}
  
	
    /**
    * 列表页面 table列表------
    */
    //@RequiresPermissions("paramconfig:fcbusinessruledefinepool:query")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return fcBusinessRuleDefinePoolService.getBusninessRuleVoList(dataTablesInput);
    }
    
    /**
     * 功能介绍 ：初始化记账规则。<br>
     */
    @LogMessage(content = "初始化记账规则列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/initList")
	public String initList(Model model) {
        return "finacial/paramconfig/businessRulePool/listInitFcBusinessRuleDefinePool";
	}
   
    
    @RequestMapping("/initDataTable")
    @ResponseBody
    public DataTablesOutput initDataTable(@RequestBody  DataTablesInput dataTablesInput){
		return fcBusinessRuleDefinePoolService.getInitBusninessRuleVoList(dataTablesInput);
    }
    
	/**
	* FC_BUSINESS_RULE_DEFINE_POOL新增页面跳转------
	*
	* @return
	*/
	//@RequiresPermissions("paramconfig:fcbusinessruledefinepool:apply")
	@GetMapping("/add")
	public String add(FcBusinessRuleDefinePoolVo fcBusinessRuleDefine, Model model) {
		if (StringUtils.isNotEmpty(fcBusinessRuleDefine.getId())) {
			fcBusinessRuleDefine = fcBusinessRuleDefinePoolService.getBusinessRuleDefinePoolVoById(fcBusinessRuleDefine.getId());
			model.addAttribute("fcBusinessRuleDefinePage", fcBusinessRuleDefine);
		}
		return "finacial/paramconfig/businessRulePool/addFcBusinessRuleDefinePool";
	}
	
	/**
	 * 功能介绍  : 获取有效的记账付款方式。<br>---------------
	 */
	@GetMapping("/getFcVoucherType")
    @ResponseBody
    public List<FcVoucherTypeDTO> getAccountVoucherType(){
        return fcVoucherTypeService.getFcVoucherTypeAll();
    }
	
	
	/**
	* FC_BUSINESS_RULE_DEFINE_POOL编辑页面跳转-------
	*/
	//@RequiresPermissions("paramconfig:fcbusinessruledefinepool:apply")
	@GetMapping("/update")
	public String update(FcBusinessRuleDefinePoolVo fcBusinessRuleDefinePool, Model model) {
		if (StringUtils.isNotEmpty(fcBusinessRuleDefinePool.getId())) {
			fcBusinessRuleDefinePool = fcBusinessRuleDefinePoolService.getBusinessRuleDefinePoolVoById(fcBusinessRuleDefinePool.getId());
			model.addAttribute("fcBusinessRuleDefinePage", fcBusinessRuleDefinePool);
		}
		return "finacial/paramconfig/businessRulePool/updateFcBusinessRuleDefinePool";
	}
	
	/**
	* FC_BUSINESS_RULE_DEFINE_POOL详情页----------
	*/
	//@RequiresPermissions("paramconfig:fcbusinessruledefinepool:query")
	@LogMessage(content = "查询FC_BUSINESS_RULE_DEFINE_POOL详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		FcBusinessRuleDefinePoolVo fcBusinessRuleDefine = fcBusinessRuleDefinePoolService.getBusRuleDefinePoolVoById(id);
		model.addAttribute("fcBusinessRuleDefinePage", fcBusinessRuleDefine);
		return "finacial/paramconfig/businessRulePool/viewFcBusinessRuleDefinePool";
	}
	
	/**
	 * 功能介绍 : 显示初始化的记账规则。<br>
	 */
	@LogMessage(content = "查询FC_BUSINESS_RULE_DEFINE_POOL详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/viewInit/{id}")
	public String viewInit(@PathVariable("id") String id, Model model) {
		FcBusinessRuleDefineVo fcBusinessRuleDefine = fcBusinessRuleDefinePoolService.getInitBusRuleDefineVoById(id);
		model.addAttribute("fcBusinessRuleDefinePage", fcBusinessRuleDefine);
		return "finacial/paramconfig/businessRulePool/viewFcBusinessRuleDefinePool";
	}
	
	/**
	 * 功能介绍 : 初始化保存。<br>
	 * <b>日期：</b>2017年11月20日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	@LogMessage(content = "添加FC_BUSINESS_RULE_DEFINE_POOL",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/saveInit")
	@ResponseBody
	public AjaxJson saveInit(String idKeys) {
		fcBusinessRuleDefinePoolService.saveInitBusinessRuleDefine(idKeys);
		return success();
	}
	
	/**
	 * 功能介绍 : 记账规则初始化。<br>---------
	 */
	@LogMessage(content = "初始化资金池记账规则",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/ruleInit")
	@ResponseBody
	public AjaxJson ruleInit() {
		fcBusinessRuleDefinePoolService.initBusinessRuleDefine();
		return success();
	}
	
	/**
	 * 添加FC_BUSINESS_RULE_DEFINE_POOL-----------
	 * @param fcBusinessRuleDefinePoolVo
	 * @return
	 */
	//@RequiresPermissions("paramconfig:fcbusinessruledefinepool:apply")
	@LogMessage(content = "添加FC_BUSINESS_RULE_DEFINE_POOL",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(FcBusinessRuleDefinePoolVo fcBusinessRuleDefinePoolVo) {
		fcBusinessRuleDefinePoolService.saveBusinessRule(fcBusinessRuleDefinePoolVo, getCurrentUser());
		return success();
	}
	/**
	* 修改FC_BUSINESS_RULE_DEFINE_POOL-----------
	* @param fcBusinessRuleDefinePool
	* @return
	*/
	//@RequiresPermissions("paramconfig:fcbusinessruledefinepool:apply")
	@LogMessage(content = "修改FC_BUSINESS_RULE_DEFINE_POOL",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(FcBusinessRuleDefinePoolVo fcBusinessRuleDefinePool){
		fcBusinessRuleDefinePoolService.updateBusinessRule(fcBusinessRuleDefinePool, getCurrentUser());
		return  success();
	}
	/**
	* 删除FC_BUSINESS_RULE_DEFINE--------
	*/
	//@RequiresPermissions("paramconfig:fcbusinessruledefinepool:apply")
	@LogMessage(content = "删除FC_BUSINESS_RULE_DEFINE_POOL",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		FcBusinessRuleDefinePool rulePool = fcBusinessRuleDefinePoolService.get(id);
		if(rulePool.getPoolId() != null) {
			List<FcBusRacePool> busRacePools = fcBusRacePoolService.findByPoolIdSysRaceCode(rulePool.getSysRaceCode(), rulePool.getPoolId());
			if(busRacePools.size()>0) {
				boolean isUsed = fcBusRacePoolService.isUsed(busRacePools.get(0).getId(), busRacePools.get(0).getPoolId(),rulePool.getCurId(),rulePool.getVoucherType());
				if (isUsed == true) {
					throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_MESSAGE_RULE_ISUSED);
				}
			}
		}
		fcBusinessRuleDefinePoolService.deleteBusinessRule(id);
		return success();
	}
	/**
	* 批量删除FC_BUSINESS_RULE_DEFINE
	* @param ids
	* @return
	*/
	//@RequiresPermissions("paramconfig:fcbusinessruledefinepool:apply")
	@LogMessage(content = "批量删除FC_BUSINESS_RULE_DEFINE_POOL",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson deleteDelete(@ModelAttribute("ids")String ids){
		for(String id : ids.split(",")){
			FcBusinessRuleDefinePool fcBusinessRuleDefine = fcBusinessRuleDefinePoolService.get(id);
			fcBusinessRuleDefinePoolService.deleteEntity(fcBusinessRuleDefine);
		}
		return success();
	}
	
	/**
	 * 获取有效的业务种类-----
	 * @return
	 */
	@GetMapping("/json")
    @ResponseBody
    public List<FcSysBusRacePool> json(){
		List<FcSysBusRacePool> raceList=fcBusinessRuleDefinePoolService.getFcSysBusRacePool();
		List<FcSysBusRacePool> raceList1=new ArrayList<FcSysBusRacePool>();
		if (raceList != null && raceList.size() > 0) {
			for (FcSysBusRacePool fc : raceList) {
				FcSysBusRacePool busR = new FcSysBusRacePool();
				fc.setSysRaceName(fc.getSysRaceCode() + "-" + fc.getSysRaceName());
				BeanUtils.copyProperties(fc, busR);
				raceList1.add(busR);
			}
		}
        return raceList1;
    }
	
	/**
	 * 获取有效的科目-----------
	 * @return
	 */
	@GetMapping("/jsonSubject")
    @ResponseBody
    public List<FcSubjectPoolVO> jsonSubject(){
		List<FcSubjectPoolVO> subjectPoolList = fcBusinessRuleDefinePoolService.listValidSubject();
		List<FcSubjectPoolVO> subjectList1 = new ArrayList<FcSubjectPoolVO>();
		if (subjectPoolList != null && subjectPoolList.size() > 0) {
			for (FcSubjectPoolVO su : subjectPoolList) {
				FcSubjectPoolVO fcSub = new FcSubjectPoolVO();
				su.setSubjectName(su.getSubjectCode() + "-" + su.getSubjectName());
				BeanUtils.copyProperties(su, fcSub);
				subjectList1.add(fcSub);
			}
		}
        return subjectList1;
    }
	/**
	 * 获取资金池主体
	 * @return
	 */
	@GetMapping("/getCorpPools")
	@ResponseBody
	public List<FcCorpPoolDTO> getUserCorpPools() {
		String resource = "paramconfig:fcbusinessruledefinepool:query";
		return iFcCorpPoolService.getUserCorpPools(this.getCurrentUser(), resource);
	}
}
