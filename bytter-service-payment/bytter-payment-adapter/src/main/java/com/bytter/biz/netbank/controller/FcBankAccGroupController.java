package com.bytter.biz.netbank.controller;

import com.bytter.biz.inneraccount.service.BtBankAccHandleService;
import com.bytter.biz.netbank.entity.FcBankAccGroup;
import com.bytter.biz.netbank.service.FcBankAccGroupService;
import com.bytter.biz.netbank.vo.FcBankAccGroupVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.system.api.IOperationAreaService;
import com.bytter.system.api.dto.OperationAreaDTO;
import com.bytter.system.api.vo.BtBankAccVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 银行账户组维护Controller
 *
 */
@Controller
@RequestMapping("/netbank/bankAccGroup")
public class FcBankAccGroupController extends BaseController {
	@Autowired
	private FcBankAccGroupService fcBankAccGroupService;

	@DubboReference(check = false)
	private IOperationAreaService operationAreaService;

	@Autowired
	private BtBankAccHandleService btBankAccHandleService;
	/**
	* 系统结算方式表列表 页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTPAYTYPE_APPLY)
	@LogMessage(content = "银行账户组列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "netbank/listFcBankAccGroup";
	}
	/**
	 * 系统结算方式表列表 页面跳转
	 *
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTPAYTYPE_APPLY)
	@LogMessage(content = "放大镜筛选单据类型维护",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/viewList")
	public String viewList(Model model) {
		return "netbank/fcAccGroup";
	}
    /**
    * 银行账户组列表 table列表
    *
    * @return
    */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTPAYTYPE_APPLY)
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return fcBankAccGroupService.getDataTablesUnion(dataTablesInput);
    }
	/**
	* 系统结算方式表新增页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTPAYTYPE_APPLY)
	@GetMapping("/add")
	public String add(FcBankAccGroup fcBankAccGroup, Model model) {
		if (StringUtils.isNotEmpty(fcBankAccGroup.getId())) {
			fcBankAccGroup = fcBankAccGroupService.get(fcBankAccGroup.getId());
			model.addAttribute("fcBankAccGroupPage", fcBankAccGroup);
		}
		return "netbank/addFcBankAccGroup";
	}
	/**
	* 系统结算方式表编辑页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTPAYTYPE_APPLY)
	@GetMapping("/update/{id}")
	public String update(@PathVariable("id") String id, Model model) {
		if (StringUtils.isNotEmpty(id)) {
			model.addAttribute("fcBankAccGroupPage", fcBankAccGroupService.getById(id));
		}
		return "netbank/updateFcBankAccGroup";
	}

	@LogMessage(content = "修改银行账号组",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(FcBankAccGroupVO fcBankAccGroupVO){
		fcBankAccGroupService.update(fcBankAccGroupVO);


		return  success();
	}

	/**
	* 系统结算方式表详情页
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTPAYTYPE_APPLY)
	@LogMessage(content = "单据类型详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		model.addAttribute("fcBankAccGroupPage", fcBankAccGroupService.getById(id));
		return "netbank/viewFcBankAccGroup";
	}
	/**
	 * 添加系统结算方式表
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTPAYTYPE_APPLY)
	@LogMessage(content = "添加系统结算方式表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(FcBankAccGroupVO fcBankAccGroupVO){
		fcBankAccGroupService.save(fcBankAccGroupVO);

		return success();
	}

	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTPAYTYPE_APPLY)

	/**
	* 删除系统结算方式表
	* @param id
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BTPAYTYPE_APPLY)
	@LogMessage(content = "删除系统结算方式表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
//		FcBankAccGroup fcBankAccGroup = fcBankAccGroupService.get(id);
//		List<BtBankAccVO> btBankAccVOByAccGroupCode = btBankAccHandleService.getBtBankAccVOByAccGroupCode(fcBankAccGroup.getAccGroupCode());
//		Map<String,BtBankAccVO> map = new HashMap<>();
//		AjaxJson json = new AjaxJson();
//		if (btBankAccVOByAccGroupCode==null && btBankAccVOByAccGroupCode.size()==0){
//			json.setSuccess(true);
//			fcBankAccGroupService.deleteById(id);
//		}else{
//			for (BtBankAccVO btBankAccVO : btBankAccVOByAccGroupCode) {
//				String fcAccGroupCode = btBankAccVO.getFcAccGroupCode();
//				if (EmptyUtils.isNotEmpty(fcAccGroupCode)){
//					List<String> fcAccGroupCodeList = Arrays.asList(fcAccGroupCode.split(","));
//					for (String s : fcAccGroupCodeList) {
//						map.put(s,btBankAccVO);
//					}
//				}
//			}
//			if (!map.containsKey(fcBankAccGroup.getAccGroupCode())) {
//				json.setSuccess(true);
//				fcBankAccGroupService.deleteById(id);
//			}else{
//				json.setSuccess(false);
//				json.setMsg("此账户组已绑定银行账号，无法删除！");
//			}
//		}
//		return json;
		fcBankAccGroupService.deleteById(id);
		return success();
	}

	/**
	 * excel 导入
	 * @param
	 * @return
	 */
	@LogMessage(content = "添加贷款信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/saveImport")
	@ResponseBody
	public AjaxJson saveImport(@RequestBody List<FcBankAccGroupVO> fcBankAccGroupVOList) {

		fcBankAccGroupService.saveImport(fcBankAccGroupVOList, getCurrentUser());
		return success();
	}
}
