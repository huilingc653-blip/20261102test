package com.bytter.system.bankacc.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ExceptionStackUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.AccountDTO;
import com.bytter.system.api.dto.AccountSearchDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.vo.BtBankAccVO;
import com.bytter.system.constants.BtBankAccConstant;
import com.bytter.system.constants.ResourceConstant;
import com.bytter.system.entity.BtBankAcc;
import com.bytter.system.entity.BtBankAccApp;
import com.bytter.system.service.BtBankAccCommonService;
import com.bytter.system.service.BtBankAccService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.RequestContextUtils;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: BT_BANK_ACC
 * @date 2017-06-02 17:50:16
 */
@Controller
@RequestMapping("/bankacc/btbankacc")
public class BtBankAccController extends BaseController {
	@Autowired
	private BtBankAccService btBankAccService;
	@Autowired
	private IBtBankAccService btBankAccServiceI;
	@Autowired
	private BtBankAccCommonService btBankAccCommonService;
	/**
	 * BT_BANK_ACC列表 页面跳转
	 *
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACC_QUERY)
	@LogMessage(content = "查询BT_BANK_ACC列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
		return "bankacc/listBtBankAcc";
	}
	/**
	 * BT_BANK_ACC table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACC_QUERY)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return btBankAccService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
	}
	/**
	 * BT_BANK_ACC新增页面跳转
	 *
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACC_APPLY)
	@GetMapping("/add")
	public String add(BtBankAcc btBankAcc, Model model) {
		if (StringUtils.isNotEmpty(btBankAcc.getId())) {
			btBankAcc = btBankAccService.get(btBankAcc.getId());
			model.addAttribute("btBankAccPage", btBankAcc);
		}
		return "bankacc/addBtBankAcc";
	}
	/**
	 * BT_BANK_ACC编辑页面跳转
	 *
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACC_APPLY)
	@GetMapping("/update")
	public String update(BtBankAcc btBankAcc, Model model) {
		if (StringUtils.isNotEmpty(btBankAcc.getId())) {
			BtBankAccApp btbankaccApp = btBankAccCommonService.getBtBankAccApp(btBankAcc.getId());
			com.bytter.system.api.vo.BtBankAccAppVO vo = btBankAccCommonService.getBtBankAccAppVOById(btbankaccApp.getId());
			Date date = DateUtils.getDate();
			model.addAttribute("curId", vo.getCurId());
			model.addAttribute("corpName", vo.getCorpName());
			model.addAttribute("btBankAccPage", btbankaccApp);
			model.addAttribute("todate", DateUtils.formatDate(date));
		}
		return "bankacc/updateBtBankAcc";
	}
	/**
	 * BT_BANK_ACC详情页
	 *
	 * @return
	 */
	////@RequiresPermissions(value = {ResourceConstant.NETBANK_ALLOCATION_BISACCGATHERPOLICY_QUERY,
//			ResourceConstant.NETBANK_BANKACC_BTBANKACC_QUERY},
//			logical = Logical.OR)
	@LogMessage(content = "查询BT_BANK_ACC详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		BtBankAccApp btbankaccApp = btBankAccCommonService.getBtBankAccApp(id);
		model.addAttribute("btBankAccPage", btbankaccApp);
		return "bankacc/viewBtBankAcc";
	}
	/**
	 * 添加BT_BANK_ACC
	 *
	 * @param btBankAcc
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACC_APPLY)
	@LogMessage(content = "添加BT_BANK_ACC", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(BtBankAcc btBankAcc) {
		btBankAccService.save(btBankAcc);
		return success();
	}
	/**
	 * 修改BT_BANK_ACC
	 *
	 * @param btBankAcc
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACC_APPLY)
	//TODO修改账户的方法放到结算去掉，这样不会出现基础域依赖其他域
//	@LogMessage(content = "修改BT_BANK_ACC", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
//	@PostMapping("/modify")
//	@ResponseBody
//	public AjaxJson update(BtBankAcc btBankAcc) {
//		btBankAccServiceImpl.toModify(btBankAcc, this.getCurrentUser());
//		return success();
//	}
	/**
	 * 删除BT_BANK_ACC
	 *
	 * @param id
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACC_APPLY)
	@LogMessage(content = "打回BT_BANK_ACC_APP", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id) {
		this.btBankAccService.back(id, this.getCurrentUser());
		return success();
	}
	/**
	 * 批量删除BT_BANK_ACC
	 *
	 * @param ids
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACC_APPLY)
	@LogMessage(content = "批量删除BT_BANK_ACC", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
		for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
			BtBankAcc btBankAcc = btBankAccService.get(id);
			btBankAccService.deleteEntity(btBankAcc);
		}
		return success();
	}
	/******************
	 * 对外url获取数据
	 *******************/
	/**
	 * 获取银行类型维护列表
	 *
	 * @return
	 */
	@GetMapping("/json")
	@ResponseBody
	public List<BtBankAcc> getBankaccList() {
		return btBankAccCommonService.listEffectiveBtBankAcc();
	}
	/**
	 * 根据账号id得到账号及父类账户信息
	 *
	 * @return
	 */
	@RequestMapping(params = "getBankAccInfoById")
	@ResponseBody
	public AjaxJson getBankAccAndParentInfoById(String id, String curId) {
		AjaxJson json = new AjaxJson();
		com.bytter.system.api.vo.BtBankAccVO vo = this.btBankAccServiceI.getByUnion(id, curId);
		json.setSuccess(EmptyUtils.isEmpty(vo) ? false : true);
		if (!EmptyUtils.isEmpty(vo) && !EmptyUtils.isBlank(vo.getPrentAccId())) {
			com.bytter.system.api.vo.BtBankAccVO parentBankAcc = btBankAccServiceI.getByUnion(vo.getPrentAccId(), vo.getCurId());
			vo.setParentBankAcc(parentBankAcc);
		}
		json.setObj(vo);
		return json;
	}
	/**
	 * 根据单位id得到银行账号列表
	 *
	 * @return
	 */
	@GetMapping("/jsonListByCorpId/{corpId}")
	@ResponseBody
	public List<BtBankAcc> getbankAccListbycorpId(@PathVariable("corpId") String corpId) {
		return btBankAccService.getListByCorpId(corpId);
	}
	@PostMapping("/getbankAccDataTable")
	@ResponseBody
	public List<BtBankAcc> getbankAccDataTable(String corpId) {
		return btBankAccService.getListByCorpId(corpId);
	}
	/**
	 * 根据账号id得到账号
	 * zuoy 2017-07-06
	 *
	 * @return
	 */
	@RequestMapping(params = "getBankAccById")
	@ResponseBody
	public AjaxJson getBankAccById(String id, String curId) {
		AjaxJson json = new AjaxJson();
		com.bytter.system.api.vo.BtBankAccVO vo = this.btBankAccServiceI.getByUnion(id, curId);
		json.setSuccess(EmptyUtils.isEmpty(vo) ? false : true);
		json.setObj(vo);
		return json;
	}
	/**
	 * 根据单位ID查询离线银行账户信息
	 * zuoy 2017-08-24
	 *
	 * @param corpId   单位ID
	 * @param isOnline 是否上线
	 * @return
	 */
	@RequestMapping(params = "getListByCorpIdAndIsOnline")
	@ResponseBody
	public List<BtBankAccDTO> getListByCorpIdAndIsOnline(String corpId, String isOnline, String resource) {
		try {
			AccountSearchDTO dto = new AccountSearchDTO(true);
			dto.setUserId(getUserId());
			if (!EmptyUtils.isBlank(corpId)) {
				dto.setCorpId(corpId);
			}
			if (!EmptyUtils.isBlank(isOnline)) {
				dto.setIsOnline(isOnline);
				if (BtBankAccConstant.ISONLINE_0.equals(isOnline)) {
					dto.setAccountType(AccountGrantConstant.OFFLINE_BANK_TYPE);
				} else {
					dto.setAccountType(AccountGrantConstant.ONLINE_BANK_TYPE);
				}
			} else {
				dto.setAccountType(AccountGrantConstant.OFFLINE_BANK_TYPE + "," + AccountGrantConstant.ONLINE_BANK_TYPE);
			}
			dto.setPermission(AccountGrantConstant.TRANSFER_ACCOUNT);
			if (!EmptyUtils.isBlank(resource)) {
				if (ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_APPLY.contains(resource)) {
					dto.setResource(ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_APPLY);
				} else if (ResourceConstant.NETBANK_BANKACC_BISACCBAL_SELECTBANKACCEND.contains(resource)) {
					dto.setResource(ResourceConstant.NETBANK_BANKACC_BISACCBAL_SELECTBANKACCEND);
				}
			}
			return this.btBankAccService.getBankAccList(dto);
		} catch (Exception e) {
			logger.error(e.toString());
			ExceptionStackUtils.getStack(e);
			return new ArrayList<>();
		}
	}
	/**
	 * 根据单位ID查询离线银行账户信息
	 * zuoy 2017-08-24
	 *
	 * @param isOnline 是否上线
	 * @param resource
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/getListByCorpIdAndNotOnline")
	@ResponseBody
	public List<BtBankAccDTO> getListByCorpIdAndNotOnline(String isOnline, String resource) {
		try {
			AccountSearchDTO dto = new AccountSearchDTO(true);
			dto.setUserId(getUserId());
			if (!EmptyUtils.isBlank(isOnline)) {
				dto.setIsOnline(isOnline);
				if (BtBankAccConstant.ISONLINE_0.equals(isOnline)) {
					dto.setAccountType(AccountGrantConstant.OFFLINE_BANK_TYPE);
				} else {
					dto.setAccountType(AccountGrantConstant.ONLINE_BANK_TYPE);
				}
			} else {
				dto.setAccountType(AccountGrantConstant.OFFLINE_BANK_TYPE + "," + AccountGrantConstant.ONLINE_BANK_TYPE);
			}
			//查询明细的权限
			dto.setPermission(AccountGrantConstant.QUERY_DETAIL);
			if (!EmptyUtils.isBlank(resource)) {
				if (ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_APPLY.contains(resource)) {
					dto.setResource(ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_APPLY);
				} else if (ResourceConstant.NETBANK_BANKACC_BISACCBAL_SELECTBANKACCEND.contains(resource)) {
					dto.setResource(ResourceConstant.NETBANK_BANKACC_BISACCBAL_SELECTBANKACCEND);
				}
			}
			return this.btBankAccService.getBankAccList(dto);
		} catch (Exception e) {
			logger.error(e.toString());
			ExceptionStackUtils.getStack(e);
			return new ArrayList<>();
		}
	}
	/**
	 * 获取实体账号
	 *
	 * @param btBankAccApp
	 * @return
	 */
	@GetMapping("/getEntityAcc")
	@ResponseBody
	public List<BtBankAccVO> getEntityAcc(BtBankAccApp btBankAccApp) {
		return btBankAccService.getEntityAcc(btBankAccApp);
	}

	/**
	 * 获取绑定账号
	 */
	@PostMapping("/getBindingBankAcc")
	@ResponseBody
	public List<BtBankAccVO> getBindingBankAcc(BtBankAccApp btBankAccApp) {
		return btBankAccService.getBindingBankAcc(btBankAccApp);
	}
	/**
	 * 银行账号申请表详情页
	 *
	 * @return
	 */
	@LogMessage(content = "查询银行账号申请表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/showOldBankAcc/{corpId}")
	public String showOldBankAcc(@PathVariable("corpId") String corpId, Model model) {
		model.addAttribute("corpId", corpId);
		return "bankacc/listBtBankAccOld";
	}
	/**
	 * 加载审核列表数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/dataTableOldBankAcc")
	@ResponseBody
	public DataTablesOutput dataTableOldBankAcc(@RequestBody DataTablesInput dataTablesInput) {
		return this.btBankAccService.dataTableOldBankAcc(dataTablesInput, getCurrentUser());
	}
	@PostMapping("/getListBy")
	@ResponseBody
	public List<AccountDTO> getListBy(AccountSearchDTO accountSearchDTO) {
		accountSearchDTO.setUserId(getUserId());
		return this.btBankAccServiceI.listBy(accountSearchDTO);
	}
	
	/**
	 * 
	* @Title: getListBy  
	* @Description: TODO(增加稽查银行账户信息查询)  
	* @param @param accountSearchDTO
	* @param @return    参数  
	* @return List<AccountDTO>    返回类型  
	* @author Jinyong.cao
	* @throws
	 */
	@PostMapping("/getRasBankListBy")
	@ResponseBody
	public List<AccountDTO> getRasBankListBy(AccountSearchDTO accountSearchDTO,HttpServletRequest request) {
		return this.btBankAccService.getRasBankListBy(accountSearchDTO,String.valueOf(RequestContextUtils.getLocale(request)));
	}
	/**
	 * 账号选择
	 * @return
	 */
	@GetMapping("/selectBankAcc")
	public String selectBankAcc(BtBankAccVO bankAcc, Model model) {
		bankAcc.setStatus(CommonConstant.STATUE_CHECK);
		model.addAttribute("params", bankAcc);
		return "bankacc/selectBankAcc";
	}
	/**
	 * 查询已选择账号
	 * @return
	 */
	@GetMapping("/selectBankAccView")
	public String selectBankAccView(BtBankAccVO bankAcc, Model model) {
		List<BtBankAccVO> list = btBankAccService.selectBankAccView(bankAcc);
		model.addAttribute("list", list);
		return "bankacc/selectBankAccView";
	}
	/**
	 * 根据银行接口代码得到银行账号，用与银企直联
	 * BT_BANK_ACC table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/getDataTableByBifCode")
	@ResponseBody
	public DataTablesOutput getDataTableByBifCode(@RequestBody DataTablesInput dataTablesInput) {
		return btBankAccService.getDataTableByBifCode(dataTablesInput, this.getCurrentUser());
	}

	@PostMapping("/bindingBankAccDatatable")
	@ResponseBody
	public DataTablesOutput bindingBankAccDatatable(@RequestBody DataTablesInput dataTablesInput) {
		return btBankAccService.bindingBankAccDatatable(dataTablesInput);
	}
}
