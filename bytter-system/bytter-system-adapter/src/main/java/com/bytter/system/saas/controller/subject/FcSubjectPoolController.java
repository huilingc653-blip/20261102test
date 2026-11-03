package com.bytter.system.saas.controller.subject;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.subject.constant.FinacialConstant;
import com.bytter.subject.entity.FcSubject;
import com.bytter.subject.entity.FcSubjectPool;
import com.bytter.subject.service.FcSubjectPoolService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.FcSubjectPoolDTO;
import com.bytter.system.api.dto.I18nDTO;
import com.bytter.system.api.enums.ParamConfigMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
/**   
 * @Title: Controller  
 * @Description: 资金池会计科目
 * @author zhengxn
 * @date 2019-4-30
 * @version V1.0   
 * TODO Controller 页面是否需要迁移到 基础模块？
 */
@RestController("v1FcSubjectPoolController")
@RequestMapping("/v1-system/finacial/paramconfig/FcSubjectPool")
public class FcSubjectPoolController extends BaseController {
	@Autowired
	private FcSubjectPoolService fcSubjectPoolService;
	@Autowired
	private IFcAccService fcAccServiceI;;
	@Autowired
	private Ii18nService i18nServiceI;
	/**
	 * 功能介绍 : 资金池会计科目 页面跳转-----
	 * @author zhengxn
	 */
	//@RequiresPermissions("finacial:paramconfig:subjectpool:query")
	@LogMessage(content = "资金池会计科目",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public AjaxJson<Map<String,Object> >list(FcSubjectPool fcSubjectPool) {
        Map<String,Object> model=new HashMap<>();
		model.put("treeDate",fcSubjectPoolService.listTreeData(fcSubjectPool));
		model.put("FcSubjectPoolPool",fcSubjectPool);
        return AjaxJson.success(model);
	}
  
	/**
	 * 资金池会计科目 查询 ----
	 */
	//@RequiresPermissions("finacial:paramconfig:subjectpool:query")
	@LogMessage(content = "查询资金池会计科目列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@PostMapping("/queryList")
    public AjaxJson<Map<String,Object> > queryList(FcSubjectPool fcSubjectPool,HttpServletRequest request){
        Map<String,Object> model=new HashMap<>();
		model.put("treeDate",fcSubjectPoolService.listTreeData(fcSubjectPool));
		model.put("FcSubjectPool",fcSubjectPool);
        return AjaxJson.success(model);
    }
    
    
	/**
	* 新增页面跳转--------
	*/
	//@RequiresPermissions("finacial:paramconfig:subjectpool:apply")
	@GetMapping("/add")
	public AjaxJson<Map<String,Object> > add(String fatherId, String subjectSort) {
        Map<String,Object> model=new HashMap<>();
		FcSubjectPool FcSubjectPool = new FcSubjectPool();
		FcSubjectPool.setFatherId(fatherId);
		FcSubjectPool.setSubjectSort(subjectSort);
		model.put("FcSubjectPoolPage", FcSubjectPool);
		return AjaxJson.success(model);
	}
	
	
	/**
	* 编辑页面跳转-----
	*/
	//@RequiresPermissions("finacial:paramconfig:subjectpool:apply")
	@GetMapping("/update/{id}")
	public AjaxJson<Map<String, Object>> update(@PathVariable("id") String id) {
        Map<String,Object> model=new HashMap<>();
		if (StringUtils.isNotEmpty(id)) {
			FcSubjectPool FcSubjectPool = fcSubjectPoolService.get(id);
			model.put("FcSubjectPoolPage", FcSubjectPool);
		}
		return AjaxJson.success(model);
	}
	/**
	 * 新增保存会计科目--------
	 */
	//@RequiresPermissions("finacial:paramconfig:subjectpool:apply")
	@LogMessage(content = "添加会计科目",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(@RequestBody FcSubjectPool fcSubjectPool){
		fcSubjectPoolService.checkAddSubject(fcSubjectPool);
		fcSubjectPoolService.save(fcSubjectPool, getUserId());
		return success();
	}
	
	/**
	 * 修改会计科目-----
	 */
	//@RequiresPermissions("finacial:paramconfig:subjectpool:apply")
	@LogMessage(content = "修改会计科目",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson modify(@RequestBody  FcSubjectPool fcSubjectPool) throws BusinessException{
		FcSubjectPool t = fcSubjectPoolService.get(fcSubjectPool.getId(),true);
		if(!t.getBalanceWay().equals(fcSubjectPool.getBalanceWay()) || !t.getInterestSign().equals(fcSubjectPool.getInterestSign())) {
			boolean isExsits = fcAccServiceI.isExsits(t.getId());
			if (isExsits) {
				throw new ParameterException(ParamConfigMessageEnum.FIN_PARAMCONFIG_FCSUBJECT_SUBJECTCODE_USE);
			}
		}
		BeanUtils.copyNotNullProperties(fcSubjectPool, t);
		fcSubjectPoolService.checkAddSubject(t);
		fcSubjectPoolService.saveOrUpdate(t);
		String locale = LocaleContextHolder.getLocale().toString();
		I18nDTO i18nDTO = new I18nDTO();
		i18nDTO.setKeyId(t.getId());
		i18nDTO.setLocale(locale);
		i18nDTO.setContext(t.getSubjectName());
		i18nServiceI.saveOrUpdateBusinessBaseContext(i18nDTO);
		return success();
	}
	
	

	/*
	选择页面列表数据 ------
	 */
	@PostMapping("/datatable")
	@ResponseBody
	public AjaxJson<DataTablesOutput<FcSubject>> dataTable(@RequestBody V1DataTablesInput dataTablesInput){
        DataTablesInput input=dataTablesInput.convertAndInitColumns();
		return AjaxJson.success(fcSubjectPoolService.listSelectSubject(input));
	}
	
	
	/**
	 * 保存选择的会计科目-----
	 */
	@LogMessage(content = "保存选择的会计科目",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@PostMapping("/saveSelectSubject")
	@ResponseBody
	public AjaxJson saveSelectSubject(@RequestParam("ids[]") List<String> ids){
		fcSubjectPoolService.saveSelectSubject(ids);
		return success();
	}
	
	
	/**
	* 资金池会计科目详情页----
	*/
	@LogMessage(content = "查询资金池会计科目详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public AjaxJson<Map<String, Object>> view(@PathVariable("id") String id) {
        Map<String,Object> model=new HashMap<>();
		FcSubjectPool FcSubjectPool = fcSubjectPoolService.get(id);
		model.put("FcSubjectPoolPage", FcSubjectPool);
		return AjaxJson.success(model);
	}
	/**
	 * 获取有效的全部会计科目
	 */
	@GetMapping("/jsonSubjectData")
	@ResponseBody
	public AjaxJson<List<FcSubjectPoolDTO>> jsonSubjectDate(){
		return AjaxJson.success(fcSubjectPoolService.findSubjectPoolByInterestSign());
	}
	/**
	 * 根据账户类别获取资金池科目
	 * @param accType
	 * @return
	 */
	@GetMapping("/jsonSubjectDate/{accType}")
	@ResponseBody
	public AjaxJson<List<FcSubjectPoolDTO>> jsonSubjectByFatherId(@PathVariable("accType") String accType){
		return AjaxJson.success(fcSubjectPoolService.findSubjectPoolByAccType(accType, FinacialConstant.VALID_TRUE));
	}
	/**
	 * 获取全部资金池会计科目
	 */
	@GetMapping("/jsonValidSubjectDate")
	@ResponseBody
	public AjaxJson<List<FcSubjectPool>> jsonValidSubjectDate(){
		return AjaxJson.success(fcSubjectPoolService.listValidSubject());
	}
}
