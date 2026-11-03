package com.bytter.biz.saas.controller.bdlinks.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.excel.util.ObjectUtils;
import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.bdlinks.entity.BisGatherCmd;
import com.bytter.biz.bdlinks.service.BisGatherCmdService;
import com.bytter.biz.bdlinks.vo.BisGatherCmdVO;
import com.bytter.biz.bdlinks.vo.v1.BisGatherCmdPageVO;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.payment.api.dto.NisAllocationReqDTO;
import com.bytter.biz.payment.api.service.INisAllocationReqService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.util.ExportUtils;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IRegionService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.RegionDTO;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.system.api.enums.UserMessageEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
/**
 * @Title: Controller  
 * @Description: 收款指令表
 * @author onlineGenerator
 * @date 2017-07-04 15:57:19
 * @version 1.0
 *
 */

@Api(value = "收款指令管理",tags = "收款指令管理")
@RestController("v1BisGatherCmdController")
@RequestMapping("/v1-payment/bdlink/bisgathercmd")
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
    * 收款指令表 table列表
    *
    * @return
    */
    @ApiOperation("收款指令表 table列表")
	@PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BisGatherCmdVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput){
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		return success(bisGatherCmdService.getDataTablesUnion(input, this.getCurrentUser()));
    }
	/**
	 * 导出全部
	 *
	 * @return
	 */
	@ApiOperation("导出全部")
	@PostMapping("/exportAll")
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
	* 收款指令表详情页
	*
	* @return
	*/
	@ApiOperation("收款指令表详情页数据查询")
	@LogMessage(content = "查询收款指令表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public AjaxJson<BisGatherCmdPageVO> view(@PathVariable("id") String id, Model model) {
		BisGatherCmdPageVO pageVO = new BisGatherCmdPageVO();
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
		pageVO.setBisGatherCmdPage(bisGatherCmd);
		pageVO.setBusType(NisMessageConstant.BUS_TYPE_BDLINKGATHER);
		String rmk = "";
		if (StringUtils.isNotBlank(bisGatherCmd.getCbsBillId())) {
			NisAllocationReqDTO nisAllocationReqDTO = nisAllocationReqService.getNisAllocationReqByBillNo(bisGatherCmd.getCbsBillNo());
			if (nisAllocationReqDTO != null) {
				rmk = nisAllocationReqDTO.getRmk();
			}
		}
		pageVO.setRmk(rmk);
		return success(pageVO);
	}

	/**
	* 发送收款指令表
	* @param id
	* @return
	*/
	@ApiOperation("发送收款指令表")
	@LogMessage(content = "发送收款指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
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
	@ApiOperation("批量发送收款指令表")
	@LogMessage(content = "批量发送收款指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
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
	@ApiOperation("退回收款指令表")
	@LogMessage(content = "退回收款指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
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
	@ApiOperation("批量退回收款指令表")
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
	@ApiOperation("重发收款指令表")
	@LogMessage(content = "重发收款指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
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
	@ApiOperation("批量重发收款指令表")
	@LogMessage(content = "批量重发收款指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
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
	@ApiOperation("作废收款指令表")
	@LogMessage(content = "作废收款指令表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
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
	@ApiOperation("批量作废收款指令表")
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
	 * 收款指令
	 *
	 * @return
	 */
	@ApiOperation("设为支付成功/失败")
	@LogMessage(content = "设为支付成功/失败", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/setPayResult/{id}/{voucherStat}")
	public AjaxJson<BisGatherCmdPageVO> setPayResult(@PathVariable("id") String id,
							   @PathVariable("voucherStat") String voucherStat) {
		BisGatherCmdPageVO pageVO = new BisGatherCmdPageVO();
		BisGatherCmdVO bisGatherCmd = bisGatherCmdService.getByIdUnion(id);
		pageVO.setBisGatherCmdPage(bisGatherCmd);
		pageVO.setVoucherStat(voucherStat);
		return success(pageVO);
	}

	/**
	 * 保存设置结果
	 * @param bisGatherCmdVO
	 * @return
	 */
	@ApiOperation("保存设置结果")
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
