package com.bytter.biz.saas.controller.inneraccount;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.bdlink.api.constant.AllocationConstant;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.biz.inneraccount.dto.v1.FcAccOperatorDTO;
import com.bytter.biz.inneraccount.service.FcAccChangeService;
import com.bytter.biz.inneraccount.service.FcAccManageService;
import com.bytter.biz.inneraccount.vo.v1.*;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.FcCorpPoolDTO;
import com.bytter.system.api.vo.FcAccHistoryPoolVO;
import com.bytter.system.api.vo.FcAccHistoryVO;
import com.bytter.system.api.vo.FcAccVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
@Api(value = "内部账户改户信息",tags = "内部账户改户信息")
@RestController("v1FcAccChangeController")
@RequestMapping("/v1-payment/inneraccount/fcaccchange")
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
	 * 可改户申请的数据列表
	 * @param dataTablesInput
	 * @return DataTablesOutput
	 */
	@ApiOperation("可改户申请的数据列表")
	@PostMapping("/datatable")
	@ResponseBody
	public AjaxJson<DataTablesOutput<FcAccHistoryVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput){
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		return success(fcAccChangeService.getDataTablesUnion(input, getCurrentUser()));
	}

	/**
	 * 资金池可改户申请的数据列表
	 * @param dataTablesInput
	 * @return DataTablesOutput
	 */
	@ApiOperation("资金池可改户申请的数据列表")
	@PostMapping("/poolDatatable")
	@ResponseBody
	public AjaxJson<DataTablesOutput<FcAccHistoryPoolVO>> poolDataTable(@RequestBody V1DataTablesInput dataTablesInput){
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		return success(fcAccChangeService.getDataTablesUnionForPool(input, getCurrentUser()));
	}
	/**
	 * 复核数据列表
	 * @param dataTablesInput
	 * @return DataTablesOutput
	 */
	@ApiOperation("复核数据列表")
	@PostMapping("/checkDataTable")
	@ResponseBody
	public AjaxJson<DataTablesOutput<FcAccHistoryVO>> checkDataTable(@RequestBody V1DataTablesInput dataTablesInput){
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		return success(fcAccChangeService.getCheckDataTable(input, this.getCurrentUser()));
	}
	/**
	 * 资金池复核数据列表
	 * @param dataTablesInput
	 * @return DataTablesOutput
	 */
	@ApiOperation("资金池复核数据列表")
	@PostMapping("/checkPoolDataTable")
	@ResponseBody
	public AjaxJson<DataTablesOutput<FcAccHistoryVO>> checkPoolDataTable(@RequestBody V1DataTablesInput dataTablesInput){
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		return success(fcAccChangeService.getCheckDataTableForPool(input, this.getCurrentUser()));
	}

	@ApiOperation(value = "查询内部账户信息表详情",
					notes = "原先的跳转逻辑：\n" +
							"1.如果账户的accState为N 正常\n" +
							"  ①.viewType为cancel 或者 cancelFL时跳转inneraccount/cancelFcAcc\n" +
							"  ②.viewType为change 则跳转inneraccount/changeFcAcc\n" +
							"  ③.其他情况跳转inneraccount/viewFcAcc\n" +
							"2.账户状态accState为其他状态\n" +
							"  ①.viewType为change 跳转inneraccount/changeFcAcc\n" +
							"  ②.viewType为check 跳转inneraccount/checkFcAccChange\n" +
							"  ③.其他情况跳转inneraccount/viewFcAcc\n" +
							"账户正常情况下返回对象值 与 非正常 返回值不同")
	@LogMessage(content = "查询内部账户信息表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}/{accState}/{viewType}")
	public AjaxJson view(@PathVariable("id") String id, @PathVariable("accState") String accState, @PathVariable("viewType") String viewType, Model model) {
		AjaxJson ajaxJson = new AjaxJson<>();
		//如果状态正常，表示目前数据还在主表当中
		if (InnerAccountConstant.ACC_STATE_NORMAL.equals(accState)) {
			FcAccCommonVO fcAccCommonVo = new FcAccCommonVO();
			fcAccManageService.search(id,fcAccCommonVo);
			fcAccCommonVo.setIsView("v");
			if("cancel".equals(viewType) || "cancelFL".equals(viewType)) {
				fcAccCommonVo.setIsFl(viewType);
			}else if("change".equals(viewType)){
			}else{
				if(InnerAccountConstant.LOG_TYPE_ALL.equals(viewType)) {
					//为日志查询指定
					fcAccCommonVo.setLogType(InnerAccountConstant.LOG_TYPE_ALL);
				}
			}
			ajaxJson.setObj(fcAccCommonVo);
			ajaxJson.setSuccess(true);
			return ajaxJson;
		}
		FcAccChangeHistoryVO fcAccChangeHistoryVO = new FcAccChangeHistoryVO();
		search(id,fcAccChangeHistoryVO);
		if("change".equals(viewType)){
			fcAccChangeHistoryVO.setIsView("v");
		}else if("check".equals(viewType)){
			fcAccChangeHistoryVO.setIsView("C");
		}else{
			if(InnerAccountConstant.LOG_TYPE_ALL.equals(viewType)) {
				//为日志查询指定
				fcAccChangeHistoryVO.setLogType(InnerAccountConstant.LOG_TYPE_ALL);
			}
			fcAccChangeHistoryVO.setIsView("v");
			model.addAttribute("isView", "v");
		}
		ajaxJson.setObj(fcAccChangeHistoryVO);
		ajaxJson.setSuccess(true);
		return ajaxJson;
	}

	/**
	 * 查看复核公用
	 * @param id
	 */
	private void search(String id, FcAccChangeHistoryVO fcAccChangeHistoryVO){
		FcAccHistoryVO fcAccHistoryVO = fcAccChangeService.findFcAccHistoryVOById(id);
		int accAccNoRule = configService.getConfig("fcs","accAccnoRule").toInteger(1).intValue();
		if(AllocationConstant.POOL_SIGN_1.equals(fcAccHistoryVO.getPoolSign())){
			fcAccHistoryVO = fcAccChangeService.findFcAccHistoryVOByIdForPool(id);
			accAccNoRule = configService.getConfig("pool", "poolAccnoRule").toInteger(1).intValue();
			FcCorpPoolDTO fcCorpPoolDTO = fcCorpPoolService.getById(fcAccHistoryVO.getNetId());
			fcAccHistoryVO.setNetPoolName(fcCorpPoolDTO.getPoolName());
			fcAccChangeHistoryVO.setNetCode(fcCorpPoolDTO.getPoolCode());
			fcAccChangeHistoryVO.setCorpCode((StringUtils.isEmpty(fcAccHistoryVO.getCorpId()) || corpService.get(fcAccHistoryVO.getCorpId())==null)?"":corpService.get(fcAccHistoryVO.getCorpId()).getCode());

		}else{
			fcAccChangeHistoryVO.setNetCode((StringUtils.isEmpty(fcAccHistoryVO.getNetId()) || corpService.get(fcAccHistoryVO.getNetId())==null)?"": corpService.get(fcAccHistoryVO.getNetId()).getCode());
			fcAccChangeHistoryVO.setCorpCode((StringUtils.isEmpty(fcAccHistoryVO.getCorpId()) || corpService.get(fcAccHistoryVO.getCorpId())==null)?"":corpService.get(fcAccHistoryVO.getCorpId()).getCode());
			fcAccChangeHistoryVO.setUnionNetCode((StringUtils.isEmpty(fcAccHistoryVO.getUnionNet()) || corpService.get(fcAccHistoryVO.getUnionNet())==null)?"":corpService.get(fcAccHistoryVO.getUnionNet()).getCode());
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
		setDictName(fcAccHistoryVO);
		fcAccChangeHistoryVO.setFcAccPage(fcAccHistoryVO);
		//顺序号生成模式
		int seqGeneratingMode = configService.getConfig("fcs","seqGeneratingMode").toInteger(1).intValue();
		fcAccHistoryVO.setSeqNumModel(seqGeneratingMode);
		//根据资金池标志生成内部账号生成法则
		String reVal = fcAccService.getRuleVaql(AllocationConstant.POOL_SIGN_0);
		int relen = reVal.length();
		fcAccHistoryVO.setRenlen(relen);
		//前后端分离后端生成顺序号  之前逻辑根据长度 以及 生成模式  取截取原内部账号的位数
		String seqNumFromBlnctrAcc = extractSeqNumFromBlnctrAcc(accAccNoRule, fcAccHistoryVO.getBlnctrAcc());
		fcAccChangeHistoryVO.getFcAccPage().setSequenceNumber(seqNumFromBlnctrAcc);
	}
	/**
	 * 页面跳转
	 *
	 * @return
	 */
	@ApiOperation(value = "/businessAccount 对账号做操作前获取账号相关内容 原先是做页面跳转用",
			notes = "这里需要根据账号类型的accTypeCode跳转不同的页面\n" +
					"01 FCACC_BUSINESS_ACCOUNT, \"FcAccBusinessAccount\");\n" +
					"02 FCACC_BANK_DEMAND, \"FcAccBankDemand\");\n" +
					"03 FCACC_LOAN, \"FcAccLoan\");\n" +
					"04 FCACC_BANK_BORROW, \"FcAccBankBorrow\");\n" +
					"05 FCACC_BUSINESS_BEGULAR, \"FcAccBusinessBegular\");\n" +
					"06 FCACC_BANK_BEGULAR, \"FcAccBankBegular\");\n" +
					"07 FCACC_INNER_ACCOUNT, \"FcAccInnerAccount\");\n" +
					"08 FCACC_BANK_DEPOSIT, \"FcAccBankDeposit\");\n" +
					"09 FCACC_LOANBYMANDATE, \"FcAccLoanByMandate\");\n" +
					"10 FCACC_ENTRUSTED_DEPOSIT, \"FcAccEntrustedDeposit\");\n" +
					"14 FCACC_OFFBALANCE, \"FcAccOffBalance\");\n" +
					"16 FCACC_AGREEMENT, \"FcAccAgreement\");")
	@ApiResponses({
			@ApiResponse(code = 200, message = "返回值的fcAccPage会有两种对象",response = FcAccOperatorVO.class),
	})
	@LogMessage(content = "页面跳转",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/businessAccount")
	public AjaxJson businessAccount(FcAccOperatorDTO fcAccOperatorDTO) {
		String id          = fcAccOperatorDTO.getId();
		String accTypeCode = fcAccOperatorDTO.getAccTypeCode();
		String type        = fcAccOperatorDTO.getType()  ; //add or update
		String opType      = fcAccOperatorDTO.getOpType(); //改户时有值  == change
		if (StringUtils.isNotEmpty(id)) {
			FcAccHistoryVO fcAccHistoryVO = fcAccChangeService.findFcAccHistoryVOById(id);
			if(!EmptyUtils.isEmpty(fcAccHistoryVO)){
				FcAccHistoryOperatorVO accountVO = new FcAccHistoryOperatorVO();
				accountVO.setFcAccPage(fcAccHistoryVO);
				accountVO.setOpType(opType);
				accountVO.setType(type);
				accountVO.setAccTypeCode(accTypeCode);
				return success(accountVO);
			}else{
				FcAccVO acc = fcAccService.findFcAccById(id);
				if(!EmptyUtils.isEmpty(acc)){
					FcAccOperatorVO accountVO = new FcAccOperatorVO();
					accountVO.setFcAccPage(acc);
					accountVO.setOpType(opType);
					accountVO.setType(type);
					accountVO.setAccTypeCode(accTypeCode);
					return success(accountVO);
				}
				return success();
			}
		}
		return success();
	}
	/**
	 * 改户(暂存 or 提交）
	 * @param fcAccHistoryVO
	 * @return
	 */
	@ApiOperation("/change 改户暂存内部账户信息表")
	@LogMessage(content = "改户暂存内部账户信息表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/change")
	@ResponseBody
	public AjaxJson change(@RequestBody FcAccHistoryVO fcAccHistoryVO){
		fcAccChangeService.change(fcAccHistoryVO,getCurrentUser());
		return  success();
	}
	/**
	 * 取消--还原改户申请前的数据
	 * @param id
	 * @return
	 */
	@ApiOperation("/cancel/{id} 取消--还原改户申请前的数据")
	@LogMessage(content = "还原改户申请前的数据",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/cancel/{id}")
	@ResponseBody
	public AjaxJson cancel(@PathVariable("id") String id){
		fcAccChangeService.canelEntity(id,getCurrentUser());
		return success();
	}
	@ApiOperation("/checkPassOrBack 改户复核通过或者失败")
	@LogMessage(content = "改户复核通过或者失败",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/checkPassOrBack")
	@ResponseBody
	public AjaxJson checkPassOrBack(@RequestBody FcAccHistoryVO fcAccHistoryVO){
		fcAccChangeService.check(fcAccHistoryVO,getCurrentUser(),false);
		return  success();
	}
	/**
	 * 获取资金池
	 * @return
	 */
	@ApiOperation("/getUserCorpPools 获取资源权限的资金池")
	@GetMapping("/getUserCorpPools")
	@ResponseBody
	public AjaxJson<List<FcCorpPoolDTO>> getUserCorpPools() {
		String resource = ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_CHANGEQUERY;
		return success(fcCorpPoolService.getUserCorpPools(this.getCurrentUser(), resource));
	}
	/**
	 * 获取资源权限的资金池的子单位(包含主体单位本身)
	 * @param poolId
	 * @return
	 */
	@ApiOperation("/getUserCorpPoolChilds 获取资源权限的资金池的子单位(包含主体单位本身)")
	@GetMapping("/getUserCorpPoolChilds")
	@ResponseBody
	public AjaxJson<List<FcCorpPoolDTO>> getUserCorpPoolChilds(String poolId) {
		String resource = ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_CHANGEQUERY;
		return success(fcCorpPoolService.getAllUserCorpPoolChilds(this.getCurrentUser(), resource, poolId));
	}



	private void setDictName(FcAccHistoryVO fcAccHistoryVO) {
		if(StringUtils.isNotEmpty(fcAccHistoryVO.getReferenceRateType())){
			fcAccHistoryVO.setReferenceRateTypeName(dictService.get("reference.RateType", fcAccHistoryVO.getReferenceRateType()));
		}
		if (StringUtils.isNotEmpty(fcAccHistoryVO.getInterestSign())){
			fcAccHistoryVO.setInterestSignName(dictService.get("fcAcc.interestSign", fcAccHistoryVO.getInterestSign()));
		}
		if (StringUtils.isNotEmpty(fcAccHistoryVO.getPredrawSign())){
			fcAccHistoryVO.setPredrawSignName(dictService.get("Admin.PredrawSign", fcAccHistoryVO.getPredrawSign()));
		}
		if (StringUtils.isNotEmpty(fcAccHistoryVO.getIfCompare())){
			fcAccHistoryVO.setIfCompareName(dictService.get("sf_yn", fcAccHistoryVO.getIfCompare()));
		}
		if (StringUtils.isNotEmpty(fcAccHistoryVO.getOverdraftSign())){
			fcAccHistoryVO.setOverdraftSignName(dictService.get("fcAcc.overdraftSign", fcAccHistoryVO.getOverdraftSign()));
		}
		if (StringUtils.isNotEmpty(fcAccHistoryVO.getNotifyType())){
			fcAccHistoryVO.setNotifyTypeName(dictService.get("choBankFinance.noticeDayLimit", fcAccHistoryVO.getNotifyType()));
		}
		if (StringUtils.isNotEmpty(fcAccHistoryVO.getEndUnit())){
			fcAccHistoryVO.setEndUnitName(dictService.get("fcAcc.endUnit", fcAccHistoryVO.getEndUnit()));
		}
	}


	public String extractSeqNumFromBlnctrAcc(int accAccNoRule, String blnctrAcc) {
		if (StringUtils.isEmpty(blnctrAcc)) {
			return "";
		}

		int tempLength;
		switch (accAccNoRule) {
			case 1:
				tempLength = 3;
				break;
			case 2:
				tempLength = 4;
				break;
			case 3:
				tempLength = 6;
				break;
			case 4:
				tempLength = 4;
				break;
			default:
				tempLength = 4;
		}

		if (blnctrAcc.length() < tempLength) {
			return blnctrAcc;
		}

		return blnctrAcc.substring(blnctrAcc.length() - tempLength);
	}
}
