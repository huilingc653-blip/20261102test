package com.bytter.biz.bond.controller;

import com.alibaba.excel.Empty;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.api.IFcBondPreserveService;
import com.bytter.guarantee.vo.FcBondPreserveVO;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BtBankAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 *
 * @author luxy
 * @version 1.0
 * @Title:FcBondPreserveController
 * @Description:保证金维护控制层
 * @date 2018年9月18日 上午10:03:29
 */
@Controller
@RequestMapping("/bond/fcbondpreserve")
public class FcBondPreserveController extends BaseController {
	@DubboReference(check = false)
	private IFcBondPreserveService fcBondPreserveService;
	@DubboReference(check = false)
	private IBtBankAccService iBtBankAccService;
	@DubboReference(check = false)
	private ICorpService corpService;
	/**
	 * 跳转到维护列表页面
	 */
	//////@RequiresPermissions(FcBondConstant.NETBANK_FCBONDORESERVE_DEPOSIT_QUERY)
	@GetMapping("/getPreserveList")
	public String getPreserveList(Model model) {
		return "bond/listFcBondPreserve";
	}
	/**
	 * 获取维护列表的数据
	 */
	//////@RequiresPermissions(FcBondConstant.NETBANK_FCBONDORESERVE_DEPOSIT_QUERY)
	@SuppressWarnings("rawtypes")
	@PostMapping("/getDataTable")
	@ResponseBody
	public DataTablesOutput getDataTable(@RequestBody DataTablesInput dataTablesInput) {
		DataTablesOutput output = fcBondPreserveService.getPreserveTable(dataTablesInput, this.getCurrentUser());
		List<FcBondPreserveVO> voList =	output.getData();
        if(EmptyUtils.isEmpty(voList)){
            return output;
        }
		for(FcBondPreserveVO vo:voList){
			//查询账号
			BtBankAccDTO accDTO= iBtBankAccService.getBankAccById(vo.getBankAccId());
			if(null != accDTO){
				vo.setBankAcc(accDTO.getBankAcc());
				vo.setAccName(accDTO.getAccName());
				vo.setBankName(accDTO.getBankName());
				vo.setCorpId(accDTO.getCorpId());
				vo.setCorpName(corpService.getCorpName(accDTO.getCorpId()));
				vo.setCurId(accDTO.getCurId());
				vo.setCurName(accDTO.getCurName());
			}
		}
		return output;
	}
	/**
	 * 新增数据
	 */
	//////@RequiresPermissions(FcBondConstant.NETBANK_FCBONDORESERVE_DEPOSIT_APPLY)
	@GetMapping("/addBondItem")
	public String addBondItem(Model model) {
		model.addAttribute("inputDate", new Date());
		return "bond/addFcBondItem";
	}
	/**
	 * 保存数据
	 */
	@PostMapping("/saveFcBondItem")
	@ResponseBody
	public AjaxJson tempOrSubmit(FcBondPreserveVO fcBondPreserveVO, String saveType) {
		fcBondPreserveService.tempOrsubmit(fcBondPreserveVO, saveType, this.getCurrentUser());
		return success();
	}
	/**
	 * 编辑数据
	 */
	//////@RequiresPermissions(FcBondConstant.NETBANK_FCBONDORESERVE_DEPOSIT_APPLY)
	@GetMapping("/editBondItem/{id}")
	public String editBondItem(@PathVariable("id") String id, Model model) {
		FcBondPreserveVO fcBondPreserveVO = fcBondPreserveService.getFcBondItemById(id);
		model.addAttribute("fcBondPreserveVO", fcBondPreserveVO);
		return "bond/editFcBondItem";
	}
	/**
	 * 查看保证金项目信息
	 */
	//////@RequiresPermissions(FcBondConstant.NETBANK_FCBONDORESERVE_DEPOSIT_QUERY)
	@GetMapping("/viewBondItem/{id}")
	public String viewBondItem(@PathVariable("id") String id, Model model) {
		FcBondPreserveVO vo = fcBondPreserveService.getFcBondItemById(id);
		//查询账号
		BtBankAccDTO accDTO= iBtBankAccService.getBankAccById(vo.getBankAccId());
		if(null != accDTO){
			vo.setBankAcc(accDTO.getBankAcc());
			vo.setAccName(accDTO.getAccName());
			vo.setBankName(accDTO.getBankName());
			vo.setCorpId(accDTO.getCorpId());
			vo.setCorpName(corpService.getCorpName(accDTO.getCorpId()));
			vo.setCurId(accDTO.getCurId());
			vo.setCurName(accDTO.getCurName());
		}
		model.addAttribute("fcBondPreserveVO", vo);
		return "bond/viewFcBondItem";
	}
	/**
	 * 删除保证金项目
	 */
	//////@RequiresPermissions(FcBondConstant.NETBANK_FCBONDORESERVE_DEPOSIT_APPLY)
	@PostMapping("/deleteBondItem/{id}")
	@ResponseBody
	public AjaxJson deleteBondItem(@PathVariable("id") String id) {
		fcBondPreserveService.deleteFcBondItem(id, this.getCurrentUser());
		return success();
	}
	/**
	 * 释放保证金项目占用金额
	 */
	@PostMapping("/releaseItemAmount/{id}")
	@ResponseBody
	public AjaxJson releaseItemAmount(@PathVariable("id") String id) {
		fcBondPreserveService.releaseItemAmount(id, this.getCurrentUser());
		return success();
	}
	/**
	 * 打开选择保证金账户页面
	 *
	 * @return
	 */
	@GetMapping("/selectBondAccPage")
	public String selectExternalCorpPage(String curId, String corpId, String bankCode, Date startDate, Date endDate, String bailBankControlType, Model model) {
		if(StringUtils.isNotEmpty(curId)){
			model.addAttribute("curId", curId);
		}
		if (StringUtils.isNotEmpty(corpId)) {
			model.addAttribute("corpId", corpId);
		}
		if (StringUtils.isNotEmpty(bankCode)) {
			model.addAttribute("bankCode", bankCode);
		}
		if (StringUtils.isNotEmpty(startDate)) {
			model.addAttribute("startDate",startDate);
		}
		if (StringUtils.isNotEmpty(endDate)) {
			model.addAttribute("endDate", endDate);
		}
		if (StringUtils.isNotEmpty(bailBankControlType)) {
			model.addAttribute("bailBankControlType", bailBankControlType);
		}
		return "bond/selectBondAccPageByTerms";
	}
	/**
	 * 根据网点和银行获取存出保证金账户列表
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/getSelectBondAccByTerms")
	@ResponseBody
	public DataTablesOutput getSelectBondAccByTerms(@RequestBody DataTablesInput dataTablesInput, String corpId,
                                                    String bankCode, String curId) {
		return fcBondPreserveService.queryBondAccDataTables(dataTablesInput, corpId, bankCode, curId,this.getCurrentUser());
	}
	/******************** 审批 **********************/
	/**
	 * 保证金项目审批列表
	 */
	@GetMapping("/listApprove")
	public String listApprove(String flowStatus, String cycleTime, Model model) {
		model.addAttribute("flowStatus", flowStatus);
		model.addAttribute("cycleTime", cycleTime);
		return "bond/listFcBondItemApprove";
	}
	/**
	 * 保证金项目审批表 table列表
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/bondItemApproveTable")
	@ResponseBody
	public DataTablesOutput bondItemApproveTable(@RequestBody DataTablesInput dataTablesInput) {
		return fcBondPreserveService.getApproveDataTables(dataTablesInput);
	}
	/**
	 * 审批详情页面
	 */
	@GetMapping("/infoApprove")
	public String infoApprove(String id, String taskId, Model model) {
		if (StringUtils.isNotEmpty(id)) {
			FcBondPreserveVO fcBondPreserveVO = fcBondPreserveService.getFcBondItemById(id);
			model.addAttribute("fcBondPreserveVO", fcBondPreserveVO);
			model.addAttribute("taskId", taskId);// 必须要做此步骤
		}
		return "bond/viewItemApproveDetail";
	}
	/**
	 * 手机审批详情页面
	 */
	@GetMapping("/mobileApprove")
	public String mobileApprove(String businessId, String taskId, Model model) {
		if (StringUtils.isNotEmpty(businessId)) {
			FcBondPreserveVO fcBondPreserveVO = fcBondPreserveService.getFcBondItemById(businessId);
			model.addAttribute("fcBondPreserveVO", fcBondPreserveVO);
			model.addAttribute("taskId", taskId);// 必须要做此步骤
		}
		return "bond/mobilePreserveApprove";
	}
}
