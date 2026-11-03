package com.bytter.system.saas.controller.subject;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.subject.constant.FinacialConstant;
import com.bytter.subject.entity.FcSubject;
import com.bytter.subject.service.FcSubjectService;
import com.bytter.subject.vo.FcSubjectSortVo;
import com.bytter.subject.vo.FcSubjectVo;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.FcSubjectDTO;
import com.bytter.system.api.dto.I18nDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apiguardian.api.API;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
/**   
 * @Title: Controller  
 * @Description: 会计科目
 * @author 吕伟林
 * @date 2017-05-12 14:18:39
 * @version V1.0   
 * TODO Controller 页面是否需要迁移到 基础模块？
 */
@Api(value = "会计科目",tags = "会计科目")
@RestController("v1FcSubjectController")
@RequestMapping("/v1-system/finacial/paramconfig/fcsubject")
public class FcSubjectController extends BaseController {
	@Autowired
	private FcSubjectService fcSubjectService;
	@Autowired
	private Ii18nService i18nServiceI;

	/**
	* 会计科目编辑页面跳转
	* @author 吕伟林
 	* @date 2017-05-27
	*/
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_PARAMCONFIG_SUBJECT_EDIT)
	@GetMapping("/update/{id}")
	public String update(@PathVariable("id") String id, Model model) {
		if (StringUtils.isNotEmpty(id)) {
			FcSubjectDTO fcSubject = fcSubjectService.findSubjectById(id);
            if (EmptyUtils.isEmpty(fcSubject.getIsCashItem())) {
                fcSubject.setIsCashItem(Integer.parseInt(CommonConstant.VALID_SIGN_NO));
            }
			model.addAttribute("fcSubjectPage", fcSubject);
		}
		return "finacial/paramconfig/fcsubject/updateFcSubject";
	}
	
	
	/**
	 * 会计科目导入页面
	 * @author 吕伟林
 	 * @date 2017-05-27
	 */
	@GetMapping("/importSubject")
	public String importSubject(Model model){
		model.addAttribute("listData",fcSubjectService.listAllInitSubject());
		return "finacial/paramconfig/fcsubject/importFcSubject";
	}
	
	/**
	 * 导入会计科目
	 * @author 吕伟林
 	 * @date 2017-05-27
	 */
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_PARAMCONFIG_SUBJECT_IMPORT)
	@LogMessage(content = "会计科目导入",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@PostMapping("/importSubjectDate")
	@ResponseBody
	public AjaxJson importSubjectDate(){
		fcSubjectService.importSubjectInit();
		return success();
	}
	
	
	/**
	* 会计科目详情页
	* @author 吕伟林
 	* @date 2017-05-27
	*/
	@ApiOperation("查询会计科目详情")
	@LogMessage(content = "查询会计科目详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		FcSubjectDTO fcSubject = fcSubjectService.findSubjectById(id);
		model.addAttribute("fcSubjectPage", fcSubject);
		return "finacial/paramconfig/fcsubject/viewFcSubject";
	}
	/**
	 * 添加会计科目
	 * @author 吕伟林
 	 * @date 2017-05-27
	 */
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_PARAMCONFIG_SUBJECT_EDIT)
	@ApiOperation("添加会计科目")
	@LogMessage(content = "添加会计科目",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(FcSubject fcSubject){
		fcSubjectService.checkAddSubject(fcSubject);
 		fcSubjectService.save(fcSubject, getUserId());
		return success();
	}
	
	/**
	 * 修改会计科目
	 * @author 吕伟林
	 * @date 2017-05-27
	 */
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_PARAMCONFIG_SUBJECT_EDIT)
	@ApiOperation("修改会计科目")
	@LogMessage(content = "修改会计科目",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson modify(FcSubject fcSubject) throws BusinessException{
		FcSubject t = fcSubjectService.get(fcSubject.getId());
		BeanUtils.copyNotNullProperties(fcSubject, t);
		BigDecimal a=fcSubject.getInterestRate();
		t.setInterestRate(a);
		fcSubjectService.checkAddSubject(t);
	 	fcSubjectService.saveOrUpdate(t);
	 	String locale = LocaleContextHolder.getLocale().toString();
		I18nDTO i18nDTO = new I18nDTO();
		i18nDTO.setKeyId(t.getId());
		i18nDTO.setLocale(locale);
		i18nDTO.setContext(t.getSubjectName());
		i18nServiceI.saveOrUpdateBusinessBaseContext(i18nDTO);
	 	
		return success();
	}
	
	/**
	* 删除会计科目
	* @author 吕伟林
 	* @date 2017-05-27
	*/
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_PARAMCONFIG_SUBJECT_EDIT)
	@ApiOperation("删除会计科目")
	@LogMessage(content = "删除会计科目",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id) throws BusinessException{
		FcSubject fcSubject = fcSubjectService.get(id);
		fcSubjectService.checkDelSubject(fcSubject);
 		fcSubjectService.deleteEntity(fcSubject);
		return  success();
	}
	
	/**
	* 停用会计科目
	* @author 吕伟林
 	* @date 2017-05-27
	*/
	//@RequiresPermissions(FinacialResourceConstant.FINACIAL_PARAMCONFIG_SUBJECT_EDIT)
	@ApiOperation("停用会计科目")
	@LogMessage(content = "停用会计科目",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/valid/{id}")
	@ResponseBody
	public AjaxJson valid(@PathVariable("id") String id){
		fcSubjectService.validSubject(id);
		return  success();
	}
	
	
	/******************
	* 对外url获取数据
	*******************/
	
	/**
	 * 获取系统科目
	 * @author 吕伟林
 	 * @date 2017-05-27
	 */
	@ApiOperation("获取系统科目")
	@GetMapping("/jsonSysCode")
	@ResponseBody
	public AjaxJson<List<FcSubjectSortVo>> jsonSysCode(){
		return success(fcSubjectService.listSysSubject());
	}
	
	/**
	 * 获取全部会计科目
	 * @author 吕伟林
 	 * @date 2017-05-27
	 */
	@ApiOperation("获取全部会计科目")
	@GetMapping("/jsonSubjectDate")
	@ResponseBody
	public AjaxJson<List<FcSubjectVo>> jsonSubjectDate(){
		return success(fcSubjectService.listValidSubject(null));
	}
	/**
	 * 获取一级会计科目
	 *
	 * @return
	 */
	@ApiOperation("获取一级会计科目")
	@GetMapping("/findSubjectByFatherId")
	@ResponseBody
	public AjaxJson<List<FcSubjectDTO>> findSubjectByFatherId(){
		return success(fcSubjectService.findSubjectByFatherId());
	}
	/**
	 * 根据 科目类别 查询会计科目
	 * @author 吕伟林
 	 * @date 2017-05-27
	 */
	@ApiOperation("根据 科目类别 查询会计科目")
	@GetMapping("/jsonSubjectDate/{accType}")
	@ResponseBody
	public AjaxJson<List<FcSubjectDTO>> jsonSubjectByFatherId(@PathVariable("accType") String accType){
		FcSubject fs = null;
		if(EmptyUtils.isNotEmpty(accType)){
			fs =  new FcSubject();
			fs.setAccType(accType);
		}
		return success(fcSubjectService.findSubjectByAccType(accType, FinacialConstant.VALID_TRUE));
	}
	
	@RequestMapping("/queryAllSubject")
	@ResponseBody
	public AjaxJson<List<FcSubjectDTO>> queryAllSubject(){
		return success(fcSubjectService.queryAllSubject("1"));
	}
	
}
