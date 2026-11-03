package com.bytter.biz.inneraccount.controller;

import com.bytter.biz.inneraccount.service.FcUnFreezeService;
import com.bytter.biz.inneraccount.vo.FcFreezeListVO;
import com.bytter.biz.inneraccount.vo.FcFreezeVO;
import com.bytter.biz.payment.api.enums.InnerAccountMessageEnum;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.ICorpService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
////import org.apache.shiro.authz.annotation.Logical;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
/**   
 * @Title: Controller  
 * @Description: 内部账号解冻
 * @author onlineGenerator
 * @date 2017-06-16 17:31:26
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/inneraccount/fcUnfreeze")
public class FcUnFreezeController extends BaseController {
	@Autowired
	private FcUnFreezeService fcUnFreezeService;
	@DubboReference(check = false)
	private ICorpService corpService;
	/**
	* 内部账号解冻列表 页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_FCFREEZE_UNQUERY)
	@LogMessage(content = "查询内部账号冻结解冻列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "inneraccount/listFcUnFreeze";
	}
	/**
	 * 内部账号解冻复核列表 页面跳转
	 *
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_FCFREEZE_UNQUERY)
	@LogMessage(content = "查询内部账号冻结解冻列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/checkList")
	public String checkList(Model model) {
		return "inneraccount/checkListFcUnFreeze";
	}
    /**
    * 内部账号解冻 table列表
    *
    * @return
    */
	@SuppressWarnings("rawtypes")
	//////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_FCFREEZE_UNQUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return fcUnFreezeService.findCanModifyUnFreezes(dataTablesInput,getCurrentUser());
    }
	/**
	 * 内部账号解冻 table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	//////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_FCFREEZE_UNQUERY)
	@PostMapping("/checkDataTable")
	@ResponseBody
	public DataTablesOutput checkDataTable(@RequestBody  DataTablesInput dataTablesInput){
		return fcUnFreezeService.findCanCheckUnFreezes(dataTablesInput,getCurrentUser());
	}
	/**
	 * 可解冻记录列表
	 * @param request
	 * @return
	 */
	@PostMapping("/getTableData")
	@ResponseBody
	public List<FcFreezeListVO> getTableData(HttpServletRequest request) {
		String corpId = request.getParameter("corpId");
		if (StringUtils.isNotEmpty(corpId)) {
			return fcUnFreezeService.findCanUnFreezes(corpId, getUserLoginName());
		}
		return new ArrayList<>();
	}
	/**
	* 内部账号冻结解冻新增页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_FCFREEZE_UNAPPLY)
	@GetMapping("/add")
	public String add(FcFreezeVO fcFreeze, Model model) {
		if (StringUtils.isNotEmpty(fcFreeze.getId())) {
			fcFreeze = fcUnFreezeService.getById(fcFreeze.getId());
			model.addAttribute("fcFreezePage", fcFreeze);
		}
		return "inneraccount/addFcUnFreeze";
	}
	/**
	* 内部账号冻结解冻编辑页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(value={ResourceConstant.NETBANK_INNERACCOUNT_FCFREEZE_UNAPPLY,
//			ResourceConstant.NETBANK_INNERACCOUNT_FCFREEZE_UNQUERY},
//		logical = Logical.OR)
	@GetMapping("/updateOrView")
	public String updateOrView(FcFreezeVO fcFreeze, Model model) {
		if (StringUtils.isNotEmpty(fcFreeze.getId())) {
			FcFreezeListVO fcFreezeListVO= fcUnFreezeService.getEntityById(fcFreeze);
			if(fcFreezeListVO == null){
				throw new ParameterException(InnerAccountMessageEnum.INNER_ACCOUNT_MESSAGE_FREEZE_NOT_EXISTS,fcFreeze.getBlnctrAcc());
			}
			model.addAttribute("fcFreezePage", fcFreezeListVO);
			model.addAttribute("netCode", corpService.get(fcFreezeListVO.getNetId()).getCode());
			model.addAttribute("corpCode", corpService.get(fcFreezeListVO.getCorpId()).getCode());
		}
		if(CommonConstant.SUBMIT_MODIFY.equals(fcFreeze.getOpType())) {
			return "inneraccount/updateFcUnFreeze";
		}else if(CommonConstant.SUBMIT_CHECK.equals(fcFreeze.getOpType())){
			return "inneraccount/checkFcUnFreeze";
		}else{
			return "inneraccount/viewFcUnFreeze";
		}
	}
	/**
	 * 添加内部账号冻结解冻
	 * @param fcFreeze
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_FCFREEZE_UNAPPLY)
	@LogMessage(content = "添加内部账号冻结解冻",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(FcFreezeVO fcFreeze){
		if(CommonConstant.SUBMIT_CHECK.equals(fcFreeze.getOpType())) {
			fcUnFreezeService.checkUnFreeze(fcFreeze,getCurrentUser());
		}else {
			fcUnFreezeService.saveEntity(fcFreeze,getCurrentUser());
		}
		return success();
	}
	/**
	* 删除内部账号冻结解冻
	* @param id
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_FCFREEZE_UNAPPLY)
	@LogMessage(content = "删除内部账号冻结解冻",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		fcUnFreezeService.delete(id,getCurrentUser());
		return success();
	}
}
