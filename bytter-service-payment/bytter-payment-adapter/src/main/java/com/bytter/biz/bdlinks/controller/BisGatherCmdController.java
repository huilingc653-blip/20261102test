package com.bytter.biz.bdlinks.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.excel.util.ObjectUtils;
import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.bdlinks.entity.BisGatherCmd;
import com.bytter.biz.bdlinks.service.BisGatherCmdService;
import com.bytter.biz.bdlinks.vo.BisExcVO;
import com.bytter.biz.bdlinks.vo.BisGatherCmdVO;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.payment.api.dto.NisAllocationReqDTO;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.payment.api.service.INisAllocationReqService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.poi.util.ExportUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IRegionService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.RegionDTO;

import com.bytter.system.api.dto.UserDTO;
import com.bytter.system.api.enums.UserMessageEnum;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
/**
 * @Title: Controller  
 * @Description: 收款指令表 TODO
 * @author onlineGenerator
 * @date 2017-07-04 15:57:19
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bdlink/bisgathercmd")
public class BisGatherCmdController extends BaseController {
	@Autowired
	private BisGatherCmdService bisGatherCmdService;
	@DubboReference(check = false)
	private INisAllocationReqService nisAllocationReqService;
	@DubboReference(check = false)
	private IBtBankAccService btBankAccService;
	@DubboReference(check = false)
	private IRegionService regionService;
	@DubboReference(check = false)
	private IUserService userService;
	/**
	* 收款指令发送
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.NETBANK_BDLINK_BISGATHERCMD_SENDQUERY)
	@LogMessage(content = "查询收款指令表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/listSend")
	public String listSend(Model model) {
        return "bdlink/listBisGatherCmdSend";
	}
	
	/**
	* 收款指令追踪
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.NETBANK_BDLINK_BISGATHERCMD_TRACEQUERY)
	@LogMessage(content = "查询收款指令表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/listTrace")
	public String listTrace(Model model) {
        return "bdlink/listBisGatherCmdTrace";
	}
	
	/**
	* 收款指令查询
	*
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.NETBANK_BDLINK_BISGATHERCMD_QUERY)
	@LogMessage(content = "查询收款指令表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "bdlink/listBisGatherCmd";
	}
    /**
    * 收款指令表 table列表
    *
    * @return
    */
    @SuppressWarnings("rawtypes")
	//@RequiresPermissions(value={ResourceConstant.NETBANK_BDLINK_BISGATHERCMD_QUERY,
//			ResourceConstant.NETBANK_BDLINK_BISGATHERCMD_SENDQUERY,
//			ResourceConstant.NETBANK_BDLINK_BISGATHERCMD_TRACEQUERY},
//	logical = Logical.OR)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return bisGatherCmdService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }
	/**
	 * 导出全部
	 *
	 * @return
	 */
	@RequestMapping("/exportAll")
	public void exportAll(HttpServletResponse response, String dataTablesInputParam, String exportExcelHeadListParam,
						  String totalParam, String fileNameParam) throws Exception {
		ExportUtils.exportByStream(response, dataTablesInputParam, exportExcelHeadListParam, new ExportUtils.ExportData() {
			@Override
			public List<?> getData(DataTablesInput dataTablesInput, ICurrentUser user) {
				DataTablesOutput dataTablesOutput = bisGatherCmdService.getDataTablesUnion(dataTablesInput, user);
				List<BisGatherCmdVO> list = dataTablesOutput.getData();
				BisGatherCmdVO bisGatherCmdVO = new BisGatherCmdVO();
				bisGatherCmdVO.setGatherAmt(BigDecimal.ZERO);
				for(BisGatherCmdVO vo:list){
					bisGatherCmdVO.setGatherAmt(bisGatherCmdVO.getGatherAmt().add(vo.getGatherAmt()));
					bisGatherCmdVO.setVoucherNo("汇总");
				}
				list.add(bisGatherCmdVO);
				return dataTablesOutput.getData();
			}
		}, totalParam, fileNameParam, "收款指令查询",5000, BisGatherCmdVO.class);
	}
	/**
	* 收款指令表新增页面跳转
	*
	* @return
	*/
	@GetMapping("/add")
	public String add(BisGatherCmd bisGatherCmd, Model model) {
		if (StringUtils.isNotEmpty(bisGatherCmd.getId())) {
			bisGatherCmd = bisGatherCmdService.get(bisGatherCmd.getId());
			model.addAttribute("bisGatherCmdPage", bisGatherCmd);
		}
		return "bdlink/addBisGatherCmd";
	}
	/**
	* 收款指令表编辑页面跳转
	*
	* @return
	*/
	@GetMapping("/update")
	public String update(BisGatherCmd bisGatherCmd, Model model) {
		if (StringUtils.isNotEmpty(bisGatherCmd.getId())) {
			bisGatherCmd = bisGatherCmdService.get(bisGatherCmd.getId());
			model.addAttribute("bisGatherCmdPage", bisGatherCmd);
		}
		return "bdlink/updateBisGatherCmd";
	}
	/**
	* 收款指令表详情页
	*
	* @return
	*/
	@LogMessage(content = "查询收款指令表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		BisGatherCmdVO bisGatherCmd = bisGatherCmdService.getByIdUnion(id);
		BtBankAccDTO childAcc = btBankAccService.getBankAccById(bisGatherCmd.getChildAccId());
		BtBankAccDTO parentAcc = btBankAccService.getBankAccById(bisGatherCmd.getParentAccId());
        Map<String, RegionDTO> mapRegion = regionService.getRegionMap();
        if(!ObjectUtils.isEmpty(childAcc)) {
	        bisGatherCmd.setChildProv(childAcc.getProv());
	        bisGatherCmd.setChildProvName(mapRegion.get(childAcc.getCity())!=null ? mapRegion.get(childAcc.getCity()).getName() : "");
	        bisGatherCmd.setChildCity(childAcc.getProv());
	        bisGatherCmd.setChildCityName(mapRegion.get(childAcc.getCity())!=null ? mapRegion.get(childAcc.getCity()).getName() : "");
        }
        if(!ObjectUtils.isEmpty(parentAcc)) {
	        bisGatherCmd.setParentProv(parentAcc.getProv());
	        bisGatherCmd.setParentProvName(mapRegion.get(parentAcc.getProv())!=null ? mapRegion.get(parentAcc.getProv()).getName() : "");
	        bisGatherCmd.setParentCity(parentAcc.getCity());
	        bisGatherCmd.setParentCityName(mapRegion.get(parentAcc.getCity())!=null ? mapRegion.get(parentAcc.getCity()).getName() : "");
        }
		model.addAttribute("bisGatherCmdPage", bisGatherCmd);
		model.addAttribute("busType", NisMessageConstant.BUS_TYPE_BDLINKGATHER);
		String rmk = "";
		if (StringUtils.isNotBlank(bisGatherCmd.getCbsBillId())) {
			NisAllocationReqDTO nisAllocationReqDTO = nisAllocationReqService.getNisAllocationReqByBillNo(bisGatherCmd.getCbsBillNo());
			if (nisAllocationReqDTO != null) {
				rmk = nisAllocationReqDTO.getRmk();
			}
		}
		model.addAttribute("rmk", rmk);
		return "bdlink/viewBisGatherCmd";
	}
	/**
	 * 添加收款指令表
	 * @param bisGatherCmd
	 * @return
	 */
	@LogMessage(content = "添加收款指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(BisGatherCmd bisGatherCmd){
		bisGatherCmdService.save(bisGatherCmd);
		return success();
	}
	/**
	* 修改收款指令表
	* @param bisGatherCmd
	* @return
	*/
	@LogMessage(content = "修改收款指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(BisGatherCmd bisGatherCmd){
		BisGatherCmd t = bisGatherCmdService.get(bisGatherCmd.getId());
		BeanUtils.copyNotNullProperties(bisGatherCmd, t);
		bisGatherCmdService.saveOrUpdate(t);
		return  success();
	}
	/**
	* 发送收款指令表
	* @param id
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.NETBANK_BDLINK_BISGATHERCMD_SENDAPPLY)
	@LogMessage(content = "修改收款指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/send/{id}")
	@ResponseBody
	public AjaxJson send(@PathVariable("id") String id){
		BisGatherCmd bisGatherCmd = bisGatherCmdService.get(id, true);
		//审批通过和待发送状态才可以提交
		if (bisGatherCmd != null && !BdlinkConstant.WAIT_FILL.equals(bisGatherCmd.getCmdStat()) && !BdlinkConstant.PASS.equals(bisGatherCmd.getCmdStat())) {
			throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisGatherCmd.getVoucherNo());
		}
		bisGatherCmdService.sendBisGatherCmd(bisGatherCmd, this.getCurrentUser());
		return success();
	}
	/**
	* 批量发送收款指令表
	* @param ids
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.NETBANK_BDLINK_BISGATHERCMD_SENDAPPLY)
	@LogMessage(content = "修改收款指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/batchSend")
	@ResponseBody
	public AjaxJson batchSend(@RequestParam("ids")String ids){
		for(String id : ids.split(CommonConstant.COMMA_SPLIT)){
			BisGatherCmd bisGatherCmd = bisGatherCmdService.get(id, true);
			//审批通过和待发送状态才可以提交
			if (bisGatherCmd != null && !BdlinkConstant.WAIT_FILL.equals(bisGatherCmd.getCmdStat()) && !BdlinkConstant.PASS.equals(bisGatherCmd.getCmdStat())) {
				throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisGatherCmd.getVoucherNo());
			}
			bisGatherCmdService.sendBisGatherCmd(bisGatherCmd, this.getCurrentUser());
		}
		return success();
	}
	/**
	* 退回收款指令表
	* @param id
	* @return
	*/
	//@RequiresPermissions(value={ResourceConstant.NETBANK_BDLINK_BISGATHERCMD_SENDAPPLY,
//			ResourceConstant.NETBANK_BDLINK_BISGATHERCMD_TRACEBACK},
//			logical = Logical.OR)
	@LogMessage(content = "修改收款指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/back")
	@ResponseBody
	public AjaxJson back(@RequestParam("id") String id, @RequestParam("backMsg") String backMsg){
		BisGatherCmd bisGatherCmd = bisGatherCmdService.get(id, true);
		//准备发送和交易成功的指令不可以退回
		if (BdlinkConstant.PRE_SEND.equals(bisGatherCmd.getCmdStat()) || BdlinkConstant.SUCCESS.equals(bisGatherCmd.getCmdStat())) {
			throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisGatherCmd.getVoucherNo());
		}
		bisGatherCmdService.backBisGatherCmd(bisGatherCmd, backMsg, this.getCurrentUser());
		return success();
	}
	/**
	* 批量退回收款指令表
	* @param ids
	* @return
	*/
	//@RequiresPermissions(value={ResourceConstant.NETBANK_BDLINK_BISGATHERCMD_SENDAPPLY,
//			ResourceConstant.NETBANK_BDLINK_BISGATHERCMD_TRACEBACK},
//			logical = Logical.OR)
	@LogMessage(content = "修改收款指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/batchBack")
	@ResponseBody
	public AjaxJson batchBack(@RequestParam("ids")String ids, @RequestParam("backMsg") String backMsg){
		for(String id : ids.split(CommonConstant.COMMA_SPLIT)){
			BisGatherCmd bisGatherCmd = bisGatherCmdService.get(id, true);
			//准备发送和交易成功的指令不可以退回
			if (BdlinkConstant.PRE_SEND.equals(bisGatherCmd.getCmdStat()) || BdlinkConstant.SUCCESS.equals(bisGatherCmd.getCmdStat())) {
				throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisGatherCmd.getVoucherNo());
			}
			bisGatherCmdService.backBisGatherCmd(bisGatherCmd, backMsg, this.getCurrentUser());
		}
		return success();
	}
	/**
	* 重发收款指令表
	* @param id
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.NETBANK_BDLINK_BISGATHERCMD_TRACERESEND)
	@LogMessage(content = "修改收款指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/resend/{id}")
	@ResponseBody
	public AjaxJson resend(@PathVariable("id") String id){
		BisGatherCmd bisGatherCmd = bisGatherCmdService.get(id, true);
		//失败状态才可以提交
		if (bisGatherCmd != null && !BdlinkConstant.BANK_FAILED.equals(bisGatherCmd.getCmdStat()) && !BdlinkConstant.INTERFACE_FAILED.equals(bisGatherCmd.getCmdStat())) {
			throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisGatherCmd.getVoucherNo());
		}
		bisGatherCmdService.resendBisGatherCmd(bisGatherCmd, this.getCurrentUser());
		return success();
	}
	/**
	* 批量重发收款指令表
	* @param ids
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.NETBANK_BDLINK_BISGATHERCMD_TRACERESEND)
	@LogMessage(content = "修改收款指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/batchResend")
	@ResponseBody
	public AjaxJson batchResend(@RequestParam("ids")String ids){
		for(String id : ids.split(CommonConstant.COMMA_SPLIT)){
			BisGatherCmd bisGatherCmd = bisGatherCmdService.get(id, true);
			//失败状态才可以提交
			if (bisGatherCmd != null && !BdlinkConstant.BANK_FAILED.equals(bisGatherCmd.getCmdStat()) && !BdlinkConstant.INTERFACE_FAILED.equals(bisGatherCmd.getCmdStat())) {
				throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisGatherCmd.getVoucherNo());
			}
			bisGatherCmdService.resendBisGatherCmd(bisGatherCmd, this.getCurrentUser());
		}
		return success();
	}
	/**
	* 作废收款指令表
	* @param id
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.NETBANK_BDLINK_BISGATHERCMD_TRACEDEFEAT)
	@LogMessage(content = "修改收款指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/defeat/{id}")
	@ResponseBody
	public AjaxJson defeat(@PathVariable("id") String id){
		BisGatherCmd bisGatherCmd = bisGatherCmdService.get(id, true);
		//失败状态才可以提交
		if (bisGatherCmd != null && !BdlinkConstant.BANK_FAILED.equals(bisGatherCmd.getCmdStat()) && !BdlinkConstant.INTERFACE_FAILED.equals(bisGatherCmd.getCmdStat())) {
			throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisGatherCmd.getVoucherNo());
		}
		bisGatherCmdService.defeatBisGatherCmd(bisGatherCmd, this.getCurrentUser());
		return success();
	}
	/**
	* 批量作废收款指令表
	* @param ids
	* @return
	*/
	//@RequiresPermissions(ResourceConstant.NETBANK_BDLINK_BISGATHERCMD_TRACEDEFEAT)
	@LogMessage(content = "修改收款指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/batchDefeat")
	@ResponseBody
	public AjaxJson batchDefeat(@RequestParam("ids")String ids){
		for(String id : ids.split(CommonConstant.COMMA_SPLIT)){
			BisGatherCmd bisGatherCmd = bisGatherCmdService.get(id, true);
			//失败状态才可以提交
			if (bisGatherCmd != null && !BdlinkConstant.BANK_FAILED.equals(bisGatherCmd.getCmdStat()) && !BdlinkConstant.INTERFACE_FAILED.equals(bisGatherCmd.getCmdStat())) {
				throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, bisGatherCmd.getVoucherNo());
			}
			bisGatherCmdService.defeatBisGatherCmd(bisGatherCmd, this.getCurrentUser());
		}
		return success();
	}
	/**
	 * 收款指令表详情页
	 *
	 * @return
	 */
	@LogMessage(content = "查询收款指令表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/viewByCbsBillId/{cbsBillId}")
	public String viewByCbsBillId(@PathVariable("cbsBillId") String cbsBillId, Model model) {
		BisGatherCmdVO bisGatherCmd = bisGatherCmdService.getByCbsBillIdUnion(cbsBillId);
		model.addAttribute("bisGatherCmdPage", bisGatherCmd);
		model.addAttribute("busType", NisMessageConstant.BUS_TYPE_BDLINKGATHER);
		String rmk = "";
		if (StringUtils.isNotBlank(bisGatherCmd.getCbsBillId())) {
			NisAllocationReqDTO nisAllocationReqDTO = nisAllocationReqService.getNisAllocationReqByBillNo(bisGatherCmd.getCbsBillNo());
			if (nisAllocationReqDTO != null) {
				rmk = nisAllocationReqDTO.getRmk();
			}
		}
		model.addAttribute("rmk", rmk);
		return "bdlink/viewBisGatherCmd";
	}

	/**
	 * 收款指令
	 *
	 * @return
	 */
	@LogMessage(content = "查询收款指令表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/setPayResult/{id}/{voucherStat}")
	public String setPayResult(@PathVariable("id") String id, @PathVariable("voucherStat") String voucherStat,
							    Model model) {
		BisGatherCmdVO bisGatherCmd = bisGatherCmdService.getByIdUnion(id);
		model.addAttribute("bisGatherCmdPage", bisGatherCmd);
		model.addAttribute("voucherStat", voucherStat);
		return "bdlink/viewBisGatherCmdSetPayResult";
	}

	/**
	 * 保存设置结果
	 * @param bisGatherCmdVO
	 * @return
	 */
	@LogMessage(content = "修改收款指令表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/savePayResult")
	@ResponseBody
	public AjaxJson savePayResult(BisGatherCmdVO bisGatherCmdVO) {
		ICurrentUser user = this.getCurrentUser();
		UserDTO userDTO = userService.getByLoginName(user.getLoginName());
		String salt = userDTO.getSalt();
		String inputPwd = DigestUtil.md5Hex(bisGatherCmdVO.getValidPassword() + salt);
		if (!inputPwd.equals(userDTO.getPassword())) {
			throw new ParameterException(UserMessageEnum.SYS_USER_PASSWORD_NOT_CORRECT);
		}
		bisGatherCmdService.setPayResultBisExc(bisGatherCmdVO, user);
		return success();
	}
}
