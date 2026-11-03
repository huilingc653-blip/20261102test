package com.bytter.biz.inneraccount.controller;
import com.bytter.biz.inneraccount.entity.FcInitAcc;
import com.bytter.biz.inneraccount.service.FcInitAccService;
import com.bytter.biz.inneraccount.vo.FcInitAccListVO;
import com.bytter.biz.inneraccount.vo.FcInitAccVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
/**
 * @Title: Controller
 * @Description: 内部账号初始化
 * @author onlineGenerator
 * @date 2017-06-12 18:12:02
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/inneraccount/fcinitacc")
public class FcInitAccController extends BaseController {
	@Autowired
	private FcInitAccService fcInitAccService;
	/**
	* 内部账号初始化列表 页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_FCINITACC_QUERY)
	@LogMessage(content = "查询内部账号初始化列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "inneraccount/listFcInitAcc";
	}
	/**
	 * 内部账号初始化列表 页面跳转
	 *
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_FCINITACC_QUERY)
	@LogMessage(content = "查询内部账号初始化列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/listCheck")
	public String listCheck(Model model) {
		return "inneraccount/checkFcInitAcc";
	}
    /**
    * 内部账号初始化复核 table列表
    *
    * @return
    */
	@SuppressWarnings("rawtypes")
	//////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_FCINITACC_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return fcInitAccService.getDataTablesUnion(dataTablesInput,getCurrentUser());
    }
	/**
	 * 内部账号初始化table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	//////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_FCINITACC_QUERY)
	@PostMapping("/tableInit")
	@ResponseBody
	public DataTablesOutput tableInit(@RequestBody  DataTablesInput dataTablesInput){
		return fcInitAccService.tableInit(dataTablesInput,getCurrentUser());
	}
	/**
	 * 添加内部账号初始化
	 * @param fcInitAccList
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_FCINITACC_APPLY)
	@LogMessage(content = "添加内部账号初始化",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/saveOrUpdate")
	@ResponseBody
	public AjaxJson save(HttpServletRequest request, FcInitAccListVO fcInitAccList){
		fcInitAccService.saveFcAccbook(fcInitAccList, CommonConstant.SUBMIT_ADD,getCurrentUser());
		return success();
	}
	//////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_FCINITACC_APPLY)
	@LogMessage(content = "内部账号初始化复核",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/check")
	@ResponseBody
	public AjaxJson check(@RequestParam("ids")String ids,@RequestParam("status")String status){
		fcInitAccService.check(ids,status,getCurrentUser());
		return success();
	}
	/**
	* 删除内部账号初始化
	* @param id
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_INNERACCOUNT_FCINITACC_APPLY)
	@LogMessage(content = "删除内部账号初始化",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		FcInitAcc fcInitAcc = fcInitAccService.getById(id);
		fcInitAccService.deleteEntity(fcInitAcc,getCurrentUser());
		return success();
	}
	/**
	 * 导入
	 * @param list
	 * @return
	 */
	@PostMapping("/saveImport")
	@ResponseBody
	public AjaxJson saveImport(@RequestBody List<FcInitAccVO> list) {
		fcInitAccService.saveImport(list,getCurrentUser());
		return success();
	}
}
