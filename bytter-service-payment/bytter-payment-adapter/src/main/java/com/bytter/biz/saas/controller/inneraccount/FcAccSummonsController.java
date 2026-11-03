package com.bytter.biz.saas.controller.inneraccount;

import com.bytter.bill.api.bill.dto.AbAccrecordsDTO;
import com.bytter.bill.api.bill.service.IAccebillService;
import com.bytter.biz.finacial.api.vo.FcAccSummonsVO;
import com.bytter.biz.finacial.api.vo.VoucherInfoVO;
import com.bytter.biz.inneraccount.service.FcAccSummonsService;
import com.bytter.biz.inneraccount.vo.v1.FcAccSummonsPageVo;
import com.bytter.biz.inneraccount.vo.v1.FcAccSummonsPrintVo;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.payment.api.dto.NisAllocationReqDTO;
import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.biz.payment.api.service.IBisExcService;
import com.bytter.biz.payment.api.service.INisAllocationReqService;
import com.bytter.biz.payment.api.service.INisBillheadService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.api.ICmsLoanService;
import com.bytter.finance.dto.*;
import com.bytter.increment.api.dto.ChoInnerFinanceDTO;
import com.bytter.increment.api.service.IChoInnerFinanceService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.workflow.dto.TaskResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

//////import org.apache.shiro.authz.annotation.RequiresPermissions;
@Api(value = "内部传票凭证打印",tags = "内部传票凭证打印")
@RestController("v1FcAccSummonsController")
@RequestMapping("/v1-payment/inneraccount/fcaccsummons")
public class FcAccSummonsController extends BaseController{
	@Autowired
	private FcAccSummonsService fcAccSummonsService;
	@Autowired
	private IWorkFlowService workFlowService;
	@DubboReference(check = false)
	private ICmsLoanService cmsLoanService;
	@DubboReference(check = false)
	private INisAllocationReqService nisAllocationReqService;
	@DubboReference(check = false)
	private IBisExcService bisExcService;
	@DubboReference(check = false)
	private INisBillheadService nisBillheadService;
	@DubboReference(check = false)
	private IChoInnerFinanceService choInnerFinanceService;
	@DubboReference(check = false)
	private IUserService userService;
	@DubboReference(check = false)
	private IAccebillService accebillService;

	@ApiOperation("内部传票凭证 Table")
	@PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<FcAccSummonsVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput){
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		return success(fcAccSummonsService.getDataTablesUnion(input,getCurrentUser()));
    }
	/**
	* 传票凭证详情页
	*
	* @return
	*/
	//////@RequiresPermissions("netbank:bank:fcaccsummons:apply")
	@ApiOperation(value = "查询凭证详情"
			,notes = "查询凭证详情跳转页面:\n" +
					 "  1.返回对象中 使用ErrorSign来标识是否错误 若ErrorSign为true跳转inneraccount/viewSummonsError" +
					 "  2.如果返回的对象中listInfo为空 或者 voucherType为空 则跳转inneraccount/viewSummonsError\n" +
					 "  3.根据VoucherType不同 跳转不同页面：\n" +
					 "     ①: type为10 付款 跳转 inneraccount/viewSummonsPayQuery\n" +
					 "     ②: type为20 收款 跳转 inneraccount/viewSummonsGatQuery\n" +
					 "	   ③: type为30 内转 跳转 inneraccount/viewSummonsTraQuery")
	@LogMessage(content = "查询凭证详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view")
	@ResponseBody
	public AjaxJson<FcAccSummonsPageVo> view(VoucherInfoVO voucherInfoVo){
		FcAccSummonsPageVo pageVo = new FcAccSummonsPageVo();
		List<VoucherInfoVO> voucherInfo = fcAccSummonsService.getVoucherInfo(voucherInfoVo);
		boolean isTra = false;
		boolean isAllocation = false;
		if(voucherInfo.size() > 0){
			String code = voucherInfo.get(0).getSysVoucherCode();
			isTra = "30".equals(code) || "31".equals(code);
			isAllocation = "10".equals(code);
		}
		if(voucherInfo.size() != 2){
			pageVo.setErrorSign(true);
			return success(pageVo);
		}
		//获取经办人和审批人
		if(!EmptyUtils.isEmptyList(voucherInfo)){
			voucherInfo = getPeople(voucherInfo);
		}

		//是否开户办理
		pageVo.setIsTra(isTra);
		//是否拨付
		pageVo.setIsAllocation(isAllocation);
		pageVo.setListInfo(voucherInfo);
		pageVo.setViewSign(EmptyUtils.isEmpty(voucherInfoVo.getViewSign()) ? "0" : voucherInfoVo.getViewSign());
		pageVo.setVoucherType(voucherInfoVo.getVoucherType());
		return success(pageVo);
	}

	/**
	 * 批量打印
	 *
	 * @return
	 */
	@ApiOperation(value = "批量打印跳转前数据获取",notes = "批量打印跳转:\n" +
			"  1.返回对象中 使用ErrorSign来标识是否错误 若ErrorSign为true跳转inneraccount/viewSummonsError" +
			"  2.成功获取list不为空(list中的对象是Map<String,List<VoucherInfoVo>>)则跳转inneraccount/printAllSummons")
	@GetMapping("/batchPrint")
	@ResponseBody
	public AjaxJson<FcAccSummonsPrintVo> batchPrint(String orderNos, String voucherTypes) {
		FcAccSummonsPrintVo pageVo = new FcAccSummonsPrintVo();
		pageVo.setErrorSign(false);
		List<Map<String, List<VoucherInfoVO>>> list = new ArrayList<>();
		if (!EmptyUtils.isEmpty(orderNos) && !EmptyUtils.isEmpty(voucherTypes)) {
			String[] orderNoStr = orderNos.split(CommonConstant.COMMA_SPLIT);
			String[] voucherTypeStr = voucherTypes.split(CommonConstant.COMMA_SPLIT);
			for (int i = 0; i < orderNoStr.length; i++) {
				Map<String, List<VoucherInfoVO>> infoMap = new HashMap<>();
				String voucherType = voucherTypeStr[i];
				String orderNo = orderNoStr[i];
				VoucherInfoVO serachVo = new VoucherInfoVO();
				serachVo.setVoucherType(voucherType);
				serachVo.setOrderNo(orderNo);
				List<VoucherInfoVO> infoList = fcAccSummonsService.getVoucherInfo(serachVo);
				if(infoList.size() != 2){
					pageVo.setErrorSign(true);
					return success(pageVo);
				}
				//获取经办人和审批人
				if(!EmptyUtils.isEmptyList(infoList)){
					infoList = getPeople(infoList);
				}
				if (!EmptyUtils.isEmptyList(infoList) && infoList.size() == 2) {
					infoMap.put(voucherType+","+orderNo, infoList);
					list.add(infoMap);
				}
			}
		}
		pageVo.setList(list);
		return success(pageVo);
	}
	/**
	 * 根据业务来源类型和原始凭证号查找制单人和审批人
	 * @param infoList
	 * @return
	 */
	public List<VoucherInfoVO> getPeople(List<VoucherInfoVO> infoList){
		if(!EmptyUtils.isEmpty(infoList.get(0).getBusOriType()) && !EmptyUtils.isEmpty(infoList.get(0).getOrigVoucherNo())){
			Integer busOriType = infoList.get(0).getBusOriType();//业务来源
			String origVoucherNo = infoList.get(0).getOrigVoucherNo();//原始凭证号
			//银企、网点间来账没有业务单据
			if(!EmptyUtils.isEmpty(busOriType) && !EmptyUtils.isEmpty(origVoucherNo)){
				List<TaskResponseDTO> taskResponseDTO = null;
				UserDTO userDto = null;
				if(busOriType == 33 || busOriType == 34 || busOriType == 32) {
					//资金调拨
					NisAllocationReqDTO dto = nisAllocationReqService.getNisAllocationReqByBillNo(origVoucherNo);
					if(!EmptyUtils.isEmpty(dto) && !EmptyUtils.isEmpty(dto.getCreateBy())){
						userDto = userService.getByLoginName(dto.getCreateBy());
						taskResponseDTO = workFlowService.listCommentsByBusinessId(dto.getId());
					}
				}else if(busOriType == 48){
					//中心申请付款
					BisExcDTO dto = bisExcService.getBisExcByVoucherNo(origVoucherNo);
					if(!EmptyUtils.isEmpty(dto) && !EmptyUtils.isEmpty(dto.getCreateBy())){
						userDto = userService.getByLoginName(dto.getCreateBy());
						taskResponseDTO = workFlowService.listCommentsByBusinessId(dto.getId());
					}
				}else if(busOriType == 46){
					//委托贷款
					CmsPrecativeLoanSendDTO dto = cmsLoanService.getCmsPrecativeLoanSendByBillCode(origVoucherNo);
					if(!EmptyUtils.isEmpty(dto) && !EmptyUtils.isEmpty(dto.getCreateBy())){
						userDto = userService.getByLoginName(dto.getCreateBy());
						taskResponseDTO = workFlowService.listCommentsByBusinessId(dto.getId());
					}
				}else if(busOriType == 47){
					//委托还款
					CmsPrecativeLoanRepayDTO dto = cmsLoanService.getCmsPrecativeLoanRepayByBillCode(origVoucherNo);
					if(!EmptyUtils.isEmpty(dto) && !EmptyUtils.isEmpty(dto.getCreateBy())){
						userDto = userService.getByLoginName(dto.getCreateBy());
						taskResponseDTO = workFlowService.listCommentsByBusinessId(dto.getId());
					}
				}else if(busOriType == 37){
					//内部
					CmsLoanSendDTO dto = cmsLoanService.getLoanSendByBillCode(origVoucherNo);
					if(!EmptyUtils.isEmpty(dto) && !EmptyUtils.isEmpty(dto.getCreateBy())){
						userDto = userService.getByLoginName(dto.getCreateBy());
						taskResponseDTO = workFlowService.listCommentsByBusinessId(dto.getId());
					}
				}else if(busOriType == 38){
					//内部还款
					CmsLoanRepayDTO dto = cmsLoanService.getCmsLoanRepayByBillCode(origVoucherNo);
					if(!EmptyUtils.isEmpty(dto) && !EmptyUtils.isEmpty(dto.getCreateBy())){
						userDto = userService.getByLoginName(dto.getCreateBy());
						taskResponseDTO = workFlowService.listCommentsByBusinessId(dto.getId());
					}
				}else if(busOriType == 35){
					//借款发放
					CmsProvideSendDTO dto = cmsLoanService.getCmsProvideLoanSendByBillCode(origVoucherNo);
					if(!EmptyUtils.isEmpty(dto) && !EmptyUtils.isEmpty(dto.getCreateBy())){
						userDto = userService.getByLoginName(dto.getCreateBy());
						taskResponseDTO = workFlowService.listCommentsByBusinessId(dto.getId());
					}
				}else if(busOriType == 36 || busOriType == 55){
					//借款
					CmsProvideLoanRepayDTO dto = cmsLoanService.getCmsProvideLoanRepayByBillCode(origVoucherNo, "");
					if(!EmptyUtils.isEmpty(dto) && !EmptyUtils.isEmpty(dto.getCreateBy())){
						userDto = userService.getByLoginName(dto.getCreateBy());
						taskResponseDTO = workFlowService.listCommentsByBusinessId(dto.getId());
					}
				}else if(busOriType == 30){
					//单位申请付款
					NisBillheadDTO dto = nisBillheadService.getNisBillheadByBillNo(origVoucherNo);
					if(!EmptyUtils.isEmpty(dto) && !EmptyUtils.isEmpty(dto.getCreateBy())){
						userDto = userService.getByLoginName(dto.getCreateBy());
						taskResponseDTO = workFlowService.listCommentsByBusinessId(dto.getId());
					}
				}else if((busOriType >= 80) && (busOriType <= 109)){
					origVoucherNo = infoList.get(0).getOrigVoucherId();//原始凭证号
					//票据接口根据id查的,所以特殊处理origVoucherNo放的是id.
					AbAccrecordsDTO dto = accebillService.abAccBookInfo(origVoucherNo, null);
					if(!EmptyUtils.isEmpty(dto) && !EmptyUtils.isEmpty(dto.getCreateBy())){
						userDto = userService.getByLoginName(dto.getCreateBy());
						taskResponseDTO = workFlowService.listCommentsByBusinessId(origVoucherNo);
					}
				}else if((busOriType >= 113) && (busOriType <= 116)) {
					//内部存款
					ChoInnerFinanceDTO dto = choInnerFinanceService.getInnerDepositByBillNo(origVoucherNo);
					if(!EmptyUtils.isEmpty(dto) && !EmptyUtils.isEmpty(dto.getCreateBy())){
						userDto = userService.getByLoginName(dto.getCreateBy());
						taskResponseDTO = workFlowService.listCommentsByBusinessId(dto.getId());
					}
				}
				if (!EmptyUtils.isEmpty(userDto)){
					infoList.get(0).setCreateName(userDto.getUsername());
				}
				if(!EmptyUtils.isEmptyList(taskResponseDTO)){
					String approveName = "";
					for(int i = 0; i < taskResponseDTO.size(); i++) {
						//增加审批过的用户才显示判断
						if ("1".equals(taskResponseDTO.get(i).getTaskPassFlag())){
							if (i == 0) {
								approveName = taskResponseDTO.get(i).getUserName();
							} else {
								approveName += "," + taskResponseDTO.get(i).getUserName();
							}
						}
					}
					infoList.get(0).setApproveName(approveName);
				}
			}
		}
		return infoList;
	}
}
