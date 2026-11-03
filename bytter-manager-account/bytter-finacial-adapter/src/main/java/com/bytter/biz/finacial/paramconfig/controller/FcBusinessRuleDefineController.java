package com.bytter.biz.finacial.paramconfig.controller;
import com.bytter.biz.finacial.paramconfig.entity.FcBusinessRuleDefine;
import com.bytter.biz.finacial.paramconfig.entity.FcSubject;
import com.bytter.biz.finacial.paramconfig.service.FcBusinessRuleDefineService;
import com.bytter.biz.finacial.paramconfig.vo.FcBusinessRuleDefineVo;
import com.bytter.biz.finacial.paramconfig.vo.FcSubjectVo;
import com.bytter.biz.finacial.paramconfig.vo.FcSysBusRaceVO;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
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
 * @Description: FC_BUSINESS_RULE_DEFINE
 * @author onlineGenerator
 * @date 2017-05-22 19:23:12
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/paramconfig/fcbusinessruledefine")
public class FcBusinessRuleDefineController extends BaseController {
	@Autowired
	private FcBusinessRuleDefineService fcBusinessRuleDefineService;
	@DubboReference(check = false)
	private IFcVoucherTypeService fcVoucherTypeService;
	/**
	* FC_BUSINESS_RULE_DEFINE列表 页面跳转
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCBUSINESSRULEDEFINE_APPLY)
	@LogMessage(content = "查询FC_BUSINESS_RULE_DEFINE列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "finacial/paramconfig/businessRule/listFcBusinessRuleDefine";
	}
    /**
    * FC_BUSINESS_RULE_DEFINE table列表
    *
    * @return
    */
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCBUSINESSRULEDEFINE_APPLY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return fcBusinessRuleDefineService.getBusninessRuleVoList(dataTablesInput);
    }

    /**
     * 功能介绍 ：初始化记账规则。<br>
     * <b>日期：</b>2017年11月20日<br>
     * @version 1.0
     * @author liyh
     */
    @LogMessage(content = "查询FC_BUSINESS_RULE_DEFINE列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/initList")
	public String initList(Model model) {
        return "finacial/paramconfig/businessRule/listInitFcBusinessRuleDefine";
	}


    @RequestMapping("/initDataTable")
    @ResponseBody
    public DataTablesOutput initDataTable(@RequestBody  DataTablesInput dataTablesInput){
		return fcBusinessRuleDefineService.getInitBusninessRuleVoList(dataTablesInput);
    }

	/**
	* FC_BUSINESS_RULE_DEFINE新增页面跳转
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCBUSINESSRULEDEFINE_APPLY)
	@GetMapping("/add")
	public String add(FcBusinessRuleDefineVo fcBusinessRuleDefine, Model model) {
		if (StringUtils.isNotEmpty(fcBusinessRuleDefine.getId())) {
			fcBusinessRuleDefine = fcBusinessRuleDefineService.getBusinessRuleDefineVoById(fcBusinessRuleDefine.getId());
			model.addAttribute("fcBusinessRuleDefinePage", fcBusinessRuleDefine);
		}
		return "finacial/paramconfig/businessRule/addFcBusinessRuleDefine";
	}

	/**
	 * 功能介绍  : 获取有效的记账付款方式。<br>
	 * <b>日期：</b>2017年11月20日<br>
	 * @version 1.0
	 * @author liyh
	 */
	@GetMapping("/getFcVoucherType")
    @ResponseBody
    public List<FcVoucherTypeDTO> getAccountVoucherType(){
        return fcVoucherTypeService.getFcVoucherTypeAll();
    }


	/**
	* FC_BUSINESS_RULE_DEFINE编辑页面跳转
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCBUSINESSRULEDEFINE_APPLY)
	@GetMapping("/update")
	public String update(FcBusinessRuleDefineVo fcBusinessRuleDefine, Model model) {
		if (StringUtils.isNotEmpty(fcBusinessRuleDefine.getId())) {
			fcBusinessRuleDefine = fcBusinessRuleDefineService.getBusinessRuleDefineVoById(fcBusinessRuleDefine.getId());
			model.addAttribute("fcBusinessRuleDefinePage", fcBusinessRuleDefine);
		}
		return "finacial/paramconfig/businessRule/updateFcBusinessRuleDefine";
	}
	/**
	* FC_BUSINESS_RULE_DEFINE详情页
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCBUSINESSRULEDEFINE_APPLY)
	@LogMessage(content = "查询FC_BUSINESS_RULE_DEFINE详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		//FcBusinessRuleDefineVo fcBusinessRuleDefine = fcBusinessRuleDefineService.getBusinessRuleDefineVoById(id);
		FcBusinessRuleDefineVo fcBusinessRuleDefine = fcBusinessRuleDefineService.getBusRuleDefineVoById(id);
		model.addAttribute("fcBusinessRuleDefinePage", fcBusinessRuleDefine);
		return "finacial/paramconfig/businessRule/viewFcBusinessRuleDefine";
	}

	/**
	 * 功能介绍 : 显示初始化的记账规则。<br>
	 * <b>日期：</b>2017年11月20日<br>
	 * @version 1.0
	 * @author liyh
	 */
	@LogMessage(content = "查询FC_BUSINESS_RULE_DEFINE详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/viewInit/{id}")
	public String viewInit(@PathVariable("id") String id, Model model) {
		FcBusinessRuleDefineVo fcBusinessRuleDefine = fcBusinessRuleDefineService.getInitBusRuleDefineVoById(id);
		model.addAttribute("fcBusinessRuleDefinePage", fcBusinessRuleDefine);
		return "finacial/paramconfig/businessRule/viewFcBusinessRuleDefine";
	}

	/**
	 * 功能介绍 : 初始化保存。<br>
	 * <b>日期：</b>2017年11月20日<br>
	 * @version 1.0
	 * @author liyh
	 */
	@LogMessage(content = "添加FC_BUSINESS_RULE_DEFINE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/saveInit")
	@ResponseBody
	public AjaxJson saveInit(String idKeys) {
		fcBusinessRuleDefineService.saveInitBusinessRuleDefine(idKeys);
		return success();
	}

	/**
	 * 功能介绍 : 记账规则管理初始化<br>
	 * <b>日期：</b>2017年11月21日<br>
	 * @version 1.0
	 * @author liyh
	 */
	@LogMessage(content = "初始化记账规则",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/ruleInit")
	@ResponseBody
	public AjaxJson ruleInit() {
		fcBusinessRuleDefineService.initBusinessRuleDefine();
		return success();
	}

	/**
	 * 添加FC_BUSINESS_RULE_DEFINE
	 * @param fcBusinessRuleDefine
	 * @return
	 */
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCBUSINESSRULEDEFINE_APPLY)
	@LogMessage(content = "添加FC_BUSINESS_RULE_DEFINE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(FcBusinessRuleDefineVo fcBusinessRuleDefine) {
		fcBusinessRuleDefineService.saveBusinessRule(fcBusinessRuleDefine, getCurrentUser());
		return success();
	}
	/**
	* 修改FC_BUSINESS_RULE_DEFINE
	* @param fcBusinessRuleDefine
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCBUSINESSRULEDEFINE_APPLY)
	@LogMessage(content = "修改FC_BUSINESS_RULE_DEFINE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(FcBusinessRuleDefineVo fcBusinessRuleDefine){
		fcBusinessRuleDefineService.updateBusinessRule(fcBusinessRuleDefine, getCurrentUser());
		return  success();
	}
	/**
	* 删除FC_BUSINESS_RULE_DEFINE
	* @param id
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.FINACIAL_BASICDATA_FCBUSINESSRULEDEFINE_APPLY)
	@LogMessage(content = "删除FC_BUSINESS_RULE_DEFINE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		fcBusinessRuleDefineService.deleteBusinessRule(id);
		return success();
	}
	/**
	* 批量删除FC_BUSINESS_RULE_DEFINE
	* @param ids
	* @return
	*/
	////@RequiresPermissions("paramconfig:fcbusinessruledefine:delete")
	@LogMessage(content = "批量删除FC_BUSINESS_RULE_DEFINE",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson deleteDelete(@ModelAttribute("ids")String ids){
		for(String id : ids.split(",")){
			FcBusinessRuleDefine fcBusinessRuleDefine = fcBusinessRuleDefineService.get(id);
			fcBusinessRuleDefineService.deleteEntity(fcBusinessRuleDefine);
		}
		return success();
	}

	/**
	 * 获取有效的业务种类
	 * @return
	 */
	@GetMapping("/json")
    @ResponseBody
    public List<FcSysBusRaceVO> json(){
		List<FcSysBusRaceVO> raceList=fcBusinessRuleDefineService.getFcSysBusRace();
		List<FcSysBusRaceVO> raceList1=new ArrayList<FcSysBusRaceVO>();
		if (raceList != null && raceList.size() > 0) {
			for (FcSysBusRaceVO fc : raceList) {
				FcSysBusRaceVO busR = new FcSysBusRaceVO();
				fc.setSysRaceName(fc.getSysRaceCode() + "-" + fc.getSysRaceName());
				BeanUtils.copyProperties(fc, busR);
				raceList1.add(busR);
			}
		}
        return raceList1;
    }

	/**
	 * 获取有效的科目
	 * @return
	 */
	@GetMapping("/jsonSubject")
    @ResponseBody
    public List<FcSubject> jsonSubject(){
		List<FcSubjectVo> subjectList=fcBusinessRuleDefineService.listValidSubject();
		List<FcSubject> subjectList1=new ArrayList<FcSubject>();
		if (subjectList != null && subjectList.size() > 0) {
			for (FcSubjectVo su : subjectList) {
				FcSubject fcSub = new FcSubject();
				su.setSubjectName(su.getSubjectCode() + "-" + su.getSubjectName());
				BeanUtils.copyProperties(su, fcSub);
				subjectList1.add(fcSub);
			}
		}
        return subjectList1;
    }

}
