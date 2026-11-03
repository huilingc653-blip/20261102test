package com.bytter.biz.inneraccount.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bytter.biz.payment.api.dto.BisExcDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bytter.bill.api.bill.dto.AbAccrecordsDTO;
import com.bytter.bill.api.bill.service.IAccebillService;
import com.bytter.biz.payment.api.service.IBisExcService;
import com.bytter.biz.finacial.api.vo.VoucherInfoVO;
import com.bytter.biz.inneraccount.service.FcAccSummonsService;
import com.bytter.biz.payment.api.dto.NisAllocationReqDTO;
import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.biz.payment.api.service.INisAllocationReqService;
import com.bytter.biz.payment.api.service.INisBillheadService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.api.ICmsLoanService;
import com.bytter.finance.dto.CmsLoanRepayDTO;
import com.bytter.finance.dto.CmsLoanSendDTO;
import com.bytter.finance.dto.CmsPrecativeLoanRepayDTO;
import com.bytter.finance.dto.CmsPrecativeLoanSendDTO;
import com.bytter.finance.dto.CmsProvideLoanRepayDTO;
import com.bytter.finance.dto.CmsProvideSendDTO;
import com.bytter.increment.api.dto.ChoInnerFinanceDTO;
import com.bytter.increment.api.service.IChoInnerFinanceService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.UserDTO;
import com.bytter.workflow.api.IWorkFlowService;
import com.bytter.workflow.dto.TaskResponseDTO;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
@Controller
@RequestMapping("/inneraccount/fcaccsummons")
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
	//////@RequiresPermissions("netbank:bank:fcaccsummons:apply")
	@SuppressWarnings("rawtypes")
	@PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return fcAccSummonsService.getDataTablesUnion(dataTablesInput,getCurrentUser());
    }
	//////@RequiresPermissions("netbank:bank:fcaccsummons:apply")
	@LogMessage(content = "查询内部传票凭证信息表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "inneraccount/listFcAccSummonsQuery";
	}
	/**
	* 传票凭证详情页
	*
	* @return
	*/
	//////@RequiresPermissions("netbank:bank:fcaccsummons:apply")
	@LogMessage(content = "查询凭证详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view")
	public String view(VoucherInfoVO voucherInfoVo, Model model){
		List<VoucherInfoVO> voucherInfo = fcAccSummonsService.getVoucherInfo(voucherInfoVo);
		boolean isTra = false;
		boolean isAllocation = false;
		if(voucherInfo.size() > 0){
			String code = voucherInfo.get(0).getSysVoucherCode();
			isTra = "30".equals(code) || "31".equals(code);
			isAllocation = "10".equals(code);
		}
		if(voucherInfo.size() != 2){
			return "inneraccount/viewSummonsError";
		}
		//获取经办人和审批人
		if(!EmptyUtils.isEmptyList(voucherInfo)){
			voucherInfo = getPeople(voucherInfo);
		}
		model.addAttribute("isTra", isTra);
		model.addAttribute("isAllocation", isAllocation);
		model.addAttribute("listInfo", voucherInfo);
		model.addAttribute("viewSign", EmptyUtils.isEmpty(voucherInfoVo.getViewSign()) ? "0" : voucherInfoVo.getViewSign());
		if(StringUtils.isNotBlank(voucherInfoVo.getVoucherType())){
			return "inneraccount"+getForwardByType(Integer.parseInt(voucherInfoVo.getVoucherType()));
		}else {
			return "inneraccount/viewSummonsError";
		}
	}
	/**
     * 根据类型跳转相应的页面
     * @param type
     * @return
     */
    public String getForwardByType(int type){
		/**单位资金计划  10 付款   20 收款  30内转 40活转定 50活转通*/
		String forward = "";
		switch(type){
			case 10 :
				forward = "/viewSummonsPayQuery";
				break;
			case 20 : 
				forward = "/viewSummonsGatQuery";
				break;
			case 30 :
				forward = "/viewSummonsTraQuery";
			default :
				break;
		}
        return forward;
	}
	/**
	 * 批量打印
	 *
	 * @return
	 */
	@GetMapping("/batchPrint")
	public String batchPrint(String orderNos, String voucherTypes,Model model) {
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
					return "inneraccount/viewSummonsError";
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
		model.addAttribute("list", list);
		return "inneraccount/printAllSummons";
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
