package com.bytter.biz.bond.controller;


import com.bytter.biz.allocation.service.NisAllocationReqService;
import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bond.service.FcBondAddService;
import com.bytter.biz.payment.api.dto.NisAllocationReqDTO;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.api.IFcBondAddService;
import com.bytter.guarantee.api.IFcBondBalService;
import com.bytter.guarantee.api.IFcBondDetailService;
import com.bytter.guarantee.dto.FcBondAddDTO;
import com.bytter.guarantee.vo.FcBondBalVO;
import com.bytter.guarantee.vo.FcBondDetailVO;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.UserDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 *
 * @author luxy
 * @version 1.0
 * @Title:FcBondAddController
 * @Description: 追加保证金
 * @date 2018年9月11日 上午10:05:48
 */
@Controller
@RequestMapping("/bond/fcbondAddition")
public class FcBondAddController extends BaseController {
	@Autowired
	private FcBondAddService fcBondAddService;
	@DubboReference(check = false)
	private IFcBondAddService iFcBondAddService;
	@DubboReference(check = false)
	private IFcBondDetailService fcBondDetailService;
	@DubboReference(check = false)
	private IFcBondBalService fcBondBalService;
	@DubboReference(check = false)
	private IBisAccBalService iBisAccBalService;
	@DubboReference(check = false)
	private IUserService userService;
	@Autowired
	NisAllocationReqService nisAllocationReqService;
	/**
	 * 追加保证金列表页面
	 */
	//////@RequiresPermissions(FcBondConstant.NETBANK_FCBONDADD_DEPOSIT_QUERY)
	@RequestMapping("/listBondAddition")
	public String listBondAddition(Model model) {
		return "bond/listFcBondAddition";
	}
	/**
	 * 追加保证金表 table列表
	 *
	 * @param dataTablesInput
	 * @return
	 */
	//////@RequiresPermissions(FcBondConstant.NETBANK_FCBONDADD_DEPOSIT_QUERY)
	@SuppressWarnings("rawtypes")
	@PostMapping("/BondAdddatatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return iFcBondAddService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
	}
	/**
	 * 打开选择保证金账户页面
	 *
	 * @paramb BondAddflag 当为1时表示打开追加保证金页面
	 * @return
	 */
	@GetMapping("/selectBondAccPage")
	public String selectExternalCorpPage(String bondAddflag, Model model) {
		return "bond/selectBondAccPage";
	}
	/**
	 * 获取存出保证金账户列表
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/getSelectBondAcc.json")
	@ResponseBody
	public DataTablesOutput getSelectBondAcc(@RequestBody DataTablesInput dataTablesInput) {
		NisAllocationReqDTO NisAllocationReqDTO=new NisAllocationReqDTO();
		NisAllocationReqDTO.setBillFrom("3");
		List<NisAllocationReqDTO> list= nisAllocationReqService.getNisAllocationReqBillFrom(NisAllocationReqDTO);
		if(list.size()>0) {
			String erpIds="";
			for (NisAllocationReqDTO nisAll : list) {
				erpIds+=nisAll.getErpId()+",";
			}
			erpIds=erpIds.substring(0,erpIds.length()-1);
			dataTablesInput.getFormdata().put("erpIds",erpIds);
		}

		return iFcBondAddService.queryBondAccDataTables(dataTablesInput, this.getCurrentUser());
	}
	/**
	 * @author luxy 新增追加保证金页面
	 */
	//////@RequiresPermissions(FcBondConstant.NETBANK_FCBONDADD_DEPOSIT_APPLY)
	@RequestMapping("/addBondAdditionMoney/{bondId}")
	@LogMessage(content = "新增追加保证金", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	public String addBondAdditionMoney(@PathVariable("bondId") String id, Model model) {
		if (StringUtils.isNotEmpty(id)) {
			FcBondDetailVO fcVo = fcBondDetailService.getFcBondById(id);
			FcBondBalVO fcbondbal = fcBondBalService.getBondBalByBondId(id);
//			List<FcBondAddDTO> addHisList = queryAddHis(id);// 追加历史记录
			fcVo.setBalMoney(fcbondbal.getBondMoney().add(fcbondbal.getAddMoney()).subtract(fcbondbal.getBackMoney()));//保证金账户可用余额=保证金录入金额+追加金额-转回金额
			fcVo.setAvailMoney(fcbondbal.getBondMoney().add(fcbondbal.getAddMoney()).subtract(fcbondbal.getBackMoney())
					.subtract(fcbondbal.getApplyMoney()));//保证金账户可用余额=保证金录入金额+追加金额-转回金额-占用金额
			model.addAttribute("fcBond", fcVo);
			model.addAttribute("addMoney", fcbondbal.getAddMoney());
//			model.addAttribute("addHisList", addHisList);
		}
		return "bond/FcBondAddition";
	}
	@PostMapping("/addHisDatatable")
	@ResponseBody
	public DataTablesOutput addHisDatatable(@RequestBody DataTablesInput dataTablesInput) {
		return iFcBondAddService.addHisDatatable(dataTablesInput, this.getCurrentUser());
	}
	/**
	 * 保存追加保证金
	 *
	 * @param saveType
	 *            提交/暂存标志位，1-提交 0-暂存
	 */
	//////@RequiresPermissions(FcBondConstant.NETBANK_FCBONDADD_DEPOSIT_APPLY)
	@PostMapping("/saveFcBondAdd")
	@LogMessage(content = "保存追加保证金", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@ResponseBody
	public AjaxJson saveFcBondAdd(FcBondAddDTO fcBondAddDTO, String saveType) {
		fcBondAddService.saveFcBondAdd(fcBondAddDTO, saveType, getCurrentUser());
		return success();
	}
	/**
	 * @author luxy
	 * @param id
	 *            追加保证金的id 编辑追加保证金页面
	 */
	//////@RequiresPermissions(FcBondConstant.NETBANK_FCBONDADD_DEPOSIT_APPLY)
	@GetMapping("/editBondAddition/{id}")
	@LogMessage(content = "编辑追加保证金", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	public String editBond(@PathVariable("id") String id, Model model) {
		if (StringUtils.isNotEmpty(id)) {
			FcBondAddDTO fcbondAddDTO = iFcBondAddService.queryAddVOById(id);
			FcBondDetailVO fcBondVo = fcBondDetailService.getFcBondById(fcbondAddDTO.getBondId());
//			List<FcBondAddDTO> addHisList = queryAddHis(fcbondAddDTO.getBondId());// 追加历史记录
			if(StringUtils.isNotEmpty(fcbondAddDTO.getOutBankAcc())) {
				BisAccBalDTO bankInfo = iBisAccBalService.getLastBisAccBalBy(fcbondAddDTO.getOutBankAcc(), fcBondVo.getCurId());
				if (null != bankInfo) {
					fcbondAddDTO.setOutBankAccMoney(bankInfo.getAvailBal());
				}else {
					fcbondAddDTO.setOutBankAccMoney(BigDecimal.ZERO);
				}
			}
			model.addAttribute("fcbondAddDTO", fcbondAddDTO);
			model.addAttribute("fcBond", fcBondVo);
//			model.addAttribute("addHisList", addHisList);
		}
		return "bond/editFcBondAddition";
	}
	/**
	 * @author luxy
	 * @param id
	 *            追加保证金的id 查看追加保证金的信息
	 */
	//////@RequiresPermissions(FcBondConstant.NETBANK_FCBONDADD_DEPOSIT_QUERY)
	@GetMapping("/viewBondAddition/{id}")
	@LogMessage(content = "查看追加保证金", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	public String viewBondAddition(@PathVariable("id") String id, Model model) {
		if (StringUtils.isNotEmpty(id)) {
			FcBondAddDTO fcbondAddDTO = iFcBondAddService.queryAddVOById(id);
			FcBondDetailVO fcBondVo = fcBondDetailService.getFcBondById(fcbondAddDTO.getBondId());
			List<FcBondAddDTO> addHisList = queryAddHis(fcbondAddDTO.getBondId());// 追加历史记录
			if(StringUtils.isNotEmpty(fcbondAddDTO.getOutBankAcc())) {
				BisAccBalDTO bankInfo = iBisAccBalService.getLastBisAccBalBy(fcbondAddDTO.getOutBankAcc(), fcbondAddDTO.getCurId());
				if (null != bankInfo) {
					fcbondAddDTO.setOutBankAccMoney(bankInfo.getAvailBal());
				}else {
					fcbondAddDTO.setOutBankAccMoney(BigDecimal.ZERO);
				}
			}
			model.addAttribute("fcbondAddDTO", fcbondAddDTO);
			model.addAttribute("fcBond", fcBondVo);
			model.addAttribute("addHisList", addHisList);
		}
		return "bond/viewFcBondAddition";
	}
	/**
	 * @author luxy
	 * @param id
	 *            追加保证金的id 删除追加保证金
	 */
	//////@RequiresPermissions(FcBondConstant.NETBANK_FCBONDADD_DEPOSIT_APPLY)
	@PostMapping("/deleteBondAddition/{id}")
	@LogMessage(content = "删除追加保证金", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@ResponseBody
	public AjaxJson deleteBondAddtion(@PathVariable("id") String id) {
		iFcBondAddService.deleteBondAdd(id, this.getCurrentUser());
		return success();
	}
	/**
	 * 查询历史追加明细
	 *
	 */
	public List<FcBondAddDTO> queryAddHis(String bondId) {
		List<FcBondAddDTO> voList= iFcBondAddService.queryAddHis(bondId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for(FcBondAddDTO vo:voList) {
			UserDTO user=userService.getByLoginName(vo.getCreateBy());
			vo.setCreateByName(user.getUsername());
			if (vo.getAddDate() != null){
				vo.setAddDateStr(sdf.format(vo.getAddDate()));
			}
		}
		return voList;
	}
	/******************** 审批 **********************/
	/**
	 * 追加保证金审批列表
	 */
	@GetMapping("/listApprove")
	public String listApprove(String flowStatus, String cycleTime, Model model) {
		model.addAttribute("flowStatus", flowStatus);
		model.addAttribute("cycleTime", cycleTime);
		return "bond/listFcBondAddApprove";
	}
	/**
	 * 追加保证金审批表 table列表
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/bondAddApproveTable")
	@ResponseBody
	public DataTablesOutput bondAddApproveTable(@RequestBody DataTablesInput dataTablesInput) {
		return iFcBondAddService.getApproveDataTables(dataTablesInput);
	}
	/**
	 * 审批详情页面
	 */
	@GetMapping("/infoApprove")
	public String infoApprove(String id, String taskId, Model model) {
		if (StringUtils.isNotEmpty(id)) {
			FcBondAddDTO fcbondAddDTO = iFcBondAddService.queryAddVOById(id);
			FcBondDetailVO fcBondVo = fcBondDetailService.getFcBondById(fcbondAddDTO.getBondId());
			List<FcBondAddDTO> addHisList = queryAddHis(fcbondAddDTO.getBondId());// 追加历史记录
			model.addAttribute("fcbondAddDTO", fcbondAddDTO);
			model.addAttribute("fcBond", fcBondVo);
			model.addAttribute("addHisList", addHisList);
			model.addAttribute("taskId", taskId);// 必须要做此步骤
		}
		return "bond/viewFcBondAddApprove";
	}
	/**
	 * 手机审批详情页面
	 */
	@GetMapping("/mobileApprove")
	public String mobileApprove(String businessId, String taskId, Model model) {
		if (StringUtils.isNotEmpty(businessId)) {
			FcBondAddDTO fcbondAddDTO = iFcBondAddService.queryAddVOById(businessId);
			FcBondDetailVO fcBondVo = fcBondDetailService.getFcBondById(fcbondAddDTO.getBondId());
			List<FcBondAddDTO> addHisList = queryAddHis(fcbondAddDTO.getBondId());// 追加历史记录
			model.addAttribute("fcbondAddDTO", fcbondAddDTO);
			model.addAttribute("fcBond", fcBondVo);
			if(addHisList!=null&&addHisList.size()>0){
                model.addAttribute("addHisList", addHisList);
            }
			model.addAttribute("taskId", taskId);// 必须要做此步骤
		}
		return "bond/mobileAddApprove";
	}
}
