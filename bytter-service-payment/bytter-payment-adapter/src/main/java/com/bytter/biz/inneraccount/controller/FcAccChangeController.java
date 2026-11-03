package com.bytter.biz.inneraccount.controller;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.inneraccount.service.FcAccChangeService;
import com.bytter.biz.inneraccount.service.FcAccManageService;
import com.bytter.biz.bdlink.api.constant.AllocationConstant;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.FcCorpPoolDTO;
import com.bytter.system.api.vo.FcAccHistoryVO;
import com.bytter.system.api.vo.FcAccVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
////import org.apache.shiro.authz.annotation.Logical;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * @Title: Controller
 * @Description: 内部账户改户信息
 * @author onlineGenerator
 * @date 2017-05-25 19:36:12
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/inneraccount/fcaccchange")
public class FcAccChangeController extends BaseController {
	@Autowired
	private FcAccChangeService fcAccChangeService;
	@Autowired
	private FcAccManageService fcAccManageService;
	@DubboReference(check = false)
	private IConfigService configService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IDictService dictService;
	@DubboReference(check = false)
	private IFcCorpPoolService fcCorpPoolService;
	@Autowired
	private FcAccManageService fcAccService;
	@DubboReference(check = false)
	private IFcAccService fcAccServiceImpl;
	/**
	 * 内部账户信息表列表 页面跳转
	 *
	 * @return
	 */
	//////@RequiresPermissions(value = {ResourceConstant.NETBANK_INNERACCOUNT_FCACC_CHANGEQUERY,
//			ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_CHANGEQUERY},
//			logical = Logical.OR)
	@LogMessage(content = "查询内部账户信息表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list/{poolSign}")
	public String list(@PathVariable("poolSign") String poolSign, Model model) {
		model.addAttribute("poolSign", poolSign);
		return "inneraccount/listFcAccChange";
	}
	/**
	 * 内部账户信息表列表 页面跳转
	 *
	 * @return
	 */
	//////@RequiresPermissions(value = {ResourceConstant.NETBANK_INNERACCOUNT_FCACC_CHANGEQUERY,
//			ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_CHANGEQUERY},
//			logical = Logical.OR)
	@LogMessage(content = "查询待复核的内部账户信息表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/listCheck/{poolSign}")
	public String listCheck(@PathVariable("poolSign") String poolSign, Model model) {
		model.addAttribute("poolSign", poolSign);
		return "inneraccount/listFcAccChangeCheck";
	}
	/**
	 * 可改户申请的数据列表
	 * @param dataTablesInput
	 * @return DataTablesOutput
	 */
	@SuppressWarnings("rawtypes")
	//////@RequiresPermissions(value = {ResourceConstant.NETBANK_INNERACCOUNT_FCACC_CHANGEQUERY,
//			ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_CHANGEQUERY},
//			logical = Logical.OR)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
		String poolSign = (String) dataTablesInput.getFormdata().get("poolSign");
		if(AllocationConstant.POOL_SIGN_1.equals(poolSign)){
			return fcAccChangeService.getDataTablesUnionForPool(dataTablesInput, getCurrentUser());
		}else {
			return fcAccChangeService.getDataTablesUnion(dataTablesInput, getCurrentUser());
		}
	}
	/**
	 * 复核数据列表
	 * @param dataTablesInput
	 * @return DataTablesOutput
	 */
	//////@RequiresPermissions(value = {ResourceConstant.NETBANK_INNERACCOUNT_FCACC_CHANGEQUERY,
//			ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_CHANGEQUERY},
//			logical = Logical.OR)
	@PostMapping("/checkDataTable")
	@ResponseBody
	public DataTablesOutput checkDataTable(@RequestBody DataTablesInput dataTablesInput){
		String poolSign = (String) dataTablesInput.getFormdata().get("poolSign");
		if(AllocationConstant.POOL_SIGN_1.equals(poolSign)) {
			return fcAccChangeService.getCheckDataTableForPool(dataTablesInput, this.getCurrentUser());
		}else{
			return fcAccChangeService.getCheckDataTable(dataTablesInput, this.getCurrentUser());
		}
	}
	@LogMessage(content = "查询内部账户信息表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}/{accState}/{viewType}")
	public String view(@PathVariable("id") String id,@PathVariable("accState") String accState,@PathVariable("viewType") String viewType, Model model) {
		//如果状态正常，表示目前数据还在主表当中
		if (InnerAccountConstant.ACC_STATE_NORMAL.equals(accState)) {
//			return "redirect:/inneraccount/fcacc/view/"+id+"/"+viewType;
			fcAccManageService.search(id,model);
			model.addAttribute("isView", "v");
			if("cancel".equals(viewType) || "cancelFL".equals(viewType)) {
				model.addAttribute("isFL", viewType);
				return "inneraccount/cancelFcAcc";
			}else if("change".equals(viewType)){
				return "inneraccount/changeFcAcc";
			}else{
				if(InnerAccountConstant.LOG_TYPE_ALL.equals(viewType)) {
					//为日志查询指定
					model.addAttribute("logType", InnerAccountConstant.LOG_TYPE_ALL);
				}
				return "inneraccount/viewFcAcc";
			}
		}
		search(id,model);
		if("change".equals(viewType)){
			model.addAttribute("isView", "v");
			return "inneraccount/changeFcAcc";
		}else if("check".equals(viewType)){
			model.addAttribute("isView", "C");
			return "inneraccount/checkFcAccChange";
		}else{
			if(InnerAccountConstant.LOG_TYPE_ALL.equals(viewType)) {
				//为日志查询指定
				model.addAttribute("logType", InnerAccountConstant.LOG_TYPE_ALL);
			}
			model.addAttribute("isView", "v");
			return "inneraccount/viewFcAcc";
		}
	}
	/**
	 * 查看复核公用
	 * @param id
	 * @param model
	 */
	private void search(String id,Model model){
		FcAccHistoryVO fcAccHistoryVO = fcAccChangeService.findFcAccHistoryVOById(id);
		int accAccNoRule = configService.getConfig("fcs","accAccnoRule").toInteger(1).intValue();
		if(AllocationConstant.POOL_SIGN_1.equals(fcAccHistoryVO.getPoolSign())){
			fcAccHistoryVO = fcAccChangeService.findFcAccHistoryVOByIdForPool(id);
			accAccNoRule = configService.getConfig("pool", "poolAccnoRule").toInteger(1).intValue();
			FcCorpPoolDTO fcCorpPoolDTO = fcCorpPoolService.getById(fcAccHistoryVO.getNetId());
			fcAccHistoryVO.setNetPoolName(fcCorpPoolDTO.getPoolName());
			model.addAttribute("netCode", fcCorpPoolDTO.getPoolCode());
			model.addAttribute("corpCode", (StringUtils.isEmpty(fcAccHistoryVO.getCorpId()) || corpService.get(fcAccHistoryVO.getCorpId())==null)?"":corpService.get(fcAccHistoryVO.getCorpId()).getCode());
		}else{
			model.addAttribute("netCode", (StringUtils.isEmpty(fcAccHistoryVO.getNetId()) || corpService.get(fcAccHistoryVO.getNetId())==null)?"": corpService.get(fcAccHistoryVO.getNetId()).getCode());
			model.addAttribute("corpCode", (StringUtils.isEmpty(fcAccHistoryVO.getCorpId()) || corpService.get(fcAccHistoryVO.getCorpId())==null)?"":corpService.get(fcAccHistoryVO.getCorpId()).getCode());
			model.addAttribute("unionNetCode", (StringUtils.isEmpty(fcAccHistoryVO.getUnionNet()) || corpService.get(fcAccHistoryVO.getUnionNet())==null)?"":corpService.get(fcAccHistoryVO.getUnionNet()).getCode());
		}
		// 获取账户类别
		if (ObjectUtil.isNotEmpty(fcAccHistoryVO.getAccType())){
			fcAccHistoryVO.setAccTypeName(dictService.get("InnerAccount.AccType",fcAccHistoryVO.getAccType()));
		}

		fcAccHistoryVO.setAccAccNoRule(accAccNoRule);
		List<FcAccHistoryVO> accListByExpPay = fcAccChangeService.getAccListByExpPay(fcAccHistoryVO);
		String interestAccTemp = "";
		if(!EmptyUtils.isEmptyList(accListByExpPay)){
			for(int i = 0; i < accListByExpPay.size(); i++){
				if(i == 0){
					interestAccTemp = accListByExpPay.get(i).getBlnctrAcc();
				}else{
					interestAccTemp += ";" + accListByExpPay.get(i).getBlnctrAcc();
				}
			}
		}
		fcAccHistoryVO.setInterestAcc(interestAccTemp);
		model.addAttribute("fcAccPage", fcAccHistoryVO);
	}
	/**
	 * 页面跳转
	 *
	 * @return
	 */
	@LogMessage(content = "页面跳转",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/businessAccount")
	public String businessAccount(HttpServletRequest request, Model model) {
		String id=request.getParameter("id");
		String accTypeCode = request.getParameter("accTypeCode");
		String type=request.getParameter("type");//add or update
		String opType=request.getParameter("opType"); //改户时有值  == change
		if (StringUtils.isNotEmpty(id)) {
			FcAccHistoryVO fcAccHistoryVO = fcAccChangeService.findFcAccHistoryVOById(id);
			if(!EmptyUtils.isEmpty(fcAccHistoryVO)){
				model.addAttribute("fcAccPage", fcAccHistoryVO);
			}else{
				com.bytter.system.api.vo.FcAccVO acc = fcAccService.findFcAccById(id);
				if(!EmptyUtils.isEmpty(acc)){
					model.addAttribute("fcAccPage", acc);
				}
			}
			model.addAttribute("opType", opType);
		}
		return "inneraccount/"+type+InnerAccountConstant.AccTypeMap.get(accTypeCode);
	}
	/**
	 * 改户(暂存 or 提交）
	 * @param fcAccHistoryVO
	 * @return
	 */
	//////@RequiresPermissions(value = {ResourceConstant.NETBANK_INNERACCOUNT_FCACC_CHANGEAPPLY,
//			ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_CHANGEAPPLY},
//			logical = Logical.OR)
	@LogMessage(content = "改户暂存内部账户信息表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/change")
	@ResponseBody
	public AjaxJson change(FcAccHistoryVO fcAccHistoryVO){
		fcAccChangeService.change(fcAccHistoryVO,getCurrentUser());
		return  success();
	}
	/**
	 * 取消--还原改户申请前的数据
	 * @param id
	 * @return
	 */
	//////@RequiresPermissions(value = {ResourceConstant.NETBANK_INNERACCOUNT_FCACC_CHANGEAPPLY,
//			ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_CHANGEAPPLY},
//			logical = Logical.OR)
	@LogMessage(content = "还原改户申请前的数据",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/cancel/{id}")
	@ResponseBody
	public AjaxJson cancel(@PathVariable("id") String id){
		fcAccChangeService.canelEntity(id,getCurrentUser());
		return success();
	}
	//////@RequiresPermissions(value = {ResourceConstant.NETBANK_INNERACCOUNT_FCACC_CHANGEAPPLY,
//			ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_CHANGEAPPLY},
//			logical = Logical.OR)
	@LogMessage(content = "复核编辑页面跳转",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@GetMapping("/check")
	public String check(FcAccVO fcAcc, Model model) {
		return "inneraccount/checkFcAccChange";
	}
	//////@RequiresPermissions(value = {ResourceConstant.NETBANK_INNERACCOUNT_FCACC_CHANGEAPPLY,
//			ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_CHANGEAPPLY},
//			logical = Logical.OR)
	@LogMessage(content = "改户复核通过或者失败",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/checkPassOrBack")
	@ResponseBody
	public AjaxJson checkPassOrBack(FcAccHistoryVO fcAccHistoryVO){
		fcAccChangeService.check(fcAccHistoryVO,getCurrentUser(),false);
		return  success();
	}
	/**
	 * 获取资金池
	 * @return
	 */
	@RequestMapping(params = "getUserCorpPools")
	@ResponseBody
	public List<FcCorpPoolDTO> getUserCorpPools() {
		String resource = ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_CHANGEQUERY;
		return fcCorpPoolService.getUserCorpPools(this.getCurrentUser(), resource);
	}
	/**
	 * 获取资金池
	 * @param poolId
	 * @return
	 */
	@RequestMapping(params = "getUserCorpPoolChilds")
	@ResponseBody
	public List<FcCorpPoolDTO> getUserCorpPoolChilds(String poolId) {
		String resource = ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_CHANGEQUERY;
		return fcCorpPoolService.getAllUserCorpPoolChilds(this.getCurrentUser(), resource, poolId);
	}
}
