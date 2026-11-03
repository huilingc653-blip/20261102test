package com.bytter.biz.inneraccount.controller;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.finacial.api.service.IFcAccWriteoffService;
import com.bytter.biz.finacial.api.vo.FcAccWriteoffVO;
import com.bytter.biz.inneraccount.service.FcAccCancelService;
import com.bytter.biz.inneraccount.service.FcAccManageService;
import com.bytter.biz.inneraccount.vo.FcCancelAccountVO;
import com.bytter.biz.bdlink.api.constant.AllocationConstant;
import com.bytter.biz.bdlink.api.constant.ResourceConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.IFcCorpPoolService;
import com.bytter.system.api.dto.FcCorpPoolDTO;
import com.bytter.system.api.vo.FcAccVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**   
 * @Title: Controller  
 * @Description: 内部账户注销信息
 * @author onlineGenerator
 * @date 2017-05-25 19:36:12
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/inneraccount/fcacccancel")
public class FcAccCancelController extends BaseController {
	@Autowired
	private FcAccManageService fcAccService;
	@Autowired
	private FcAccCancelService fcAccCancelService;
	@DubboReference(check = false)
	private IFcAccWriteoffService fcAccWriteoffService;
    @DubboReference(check = false)
	private IFcCorpPoolService fcCorpPoolService;
	@DubboReference(check = false)
	private IConfigService configService;
	@DubboReference(check = false)
	private ICorpService corpService;
	@DubboReference(check = false)
	private IDictService dictService;
	/**
	* 内部账户注销信息表列表 页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(value = {ResourceConstant.NETBANK_INNERACCOUNT_FCACC_CANCELQUERY,
//			ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_CANCELQUERY},
//			logical = Logical.OR)
	@LogMessage(content = "查询内部账户信息表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list/{poolSign}")
	public String list(@PathVariable("poolSign") String poolSign, Model model) {
		model.addAttribute("poolSign", poolSign);
        return "inneraccount/listFcAccCancel";
	}
	/**
	 * 内部账户注销待复核信息表列表 页面跳转
	 *
	 * @return
	 */
	//////@RequiresPermissions(value = {ResourceConstant.NETBANK_INNERACCOUNT_FCACC_CANCELQUERY,
//			ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_CANCELQUERY},
//			logical = Logical.OR)
	@LogMessage(content = "查询待复核的内部账户信息表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/listCheck/{poolSign}")
	public String listCheck(@PathVariable("poolSign") String poolSign, Model model) {
		model.addAttribute("poolSign", poolSign);
		return "inneraccount/listFcAccCancelCheck";
	}
    /**
    * 内部注销待复核信息表 table列表
    *及
    * @return
    */
	@SuppressWarnings("rawtypes")
	//////@RequiresPermissions(value = {ResourceConstant.NETBANK_INNERACCOUNT_FCACC_CANCELQUERY,
//			ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_CANCELQUERY},
//			logical = Logical.OR)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
		String poolSign = (String) dataTablesInput.getFormdata().get("poolSign");
		if(AllocationConstant.POOL_SIGN_1.equals(poolSign)){
			return fcAccCancelService.getDataTablesUnionForPool(dataTablesInput, getCurrentUser());
		}else {
			return fcAccCancelService.getDataTablesUnion(dataTablesInput, getCurrentUser());
		}
    }
	/**
	* 内部账户信息表编辑页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(value = {ResourceConstant.NETBANK_INNERACCOUNT_FCACC_CANCELAPPLY,
//			ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_CANCELAPPLY},
//			logical = Logical.OR)
	@GetMapping("/update")
	public String update(FcAccVO fcAcc, Model model) {
		if (StringUtils.isNotEmpty(fcAcc.getId())) {
			fcAcc = fcAccService.getFcAcc(fcAcc.getId());
			model.addAttribute("fcAccPage", fcAcc);
		}
		return "inneraccount/cancelFcAcc";
	}
	/**
	* 注销提交
	* @param fcAcc
	* @return
	*/
	//////@RequiresPermissions(value = {ResourceConstant.NETBANK_INNERACCOUNT_FCACC_CANCELAPPLY,
//			ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_CANCELAPPLY},
//			logical = Logical.OR)
	@LogMessage(content = "注销提交内部账户信息表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/submit")
	@ResponseBody
	public AjaxJson submit(FcAccVO fcAcc){
		return  success();
	}
	/**
	 * 注销保存
	 * @param fcCancelAccountVO
	 * @return
	 */
	//////@RequiresPermissions(value = {ResourceConstant.NETBANK_INNERACCOUNT_FCACC_CANCELAPPLY,
//			ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_CANCELAPPLY},
//			logical = Logical.OR)
	@LogMessage(content = "注销保存",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save/{opType}")
	@ResponseBody
	public AjaxJson save(@RequestBody FcCancelAccountVO fcCancelAccountVO, @PathVariable("opType") String opType){
		fcAccCancelService.saveCancel(fcCancelAccountVO,opType,this.getCurrentUser());
		return success();
	}
	/**
	 * 注销编辑
	 * @param fcCancelAccountVO
	 * @return
	 */
	//////@RequiresPermissions(value = {ResourceConstant.NETBANK_INNERACCOUNT_FCACC_CANCELAPPLY,
//			ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_CANCELAPPLY},
//			logical = Logical.OR)
	@LogMessage(content = "注销编辑",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/edit/{opType}")
	@ResponseBody
	public AjaxJson edit(@RequestBody FcCancelAccountVO fcCancelAccountVO, @PathVariable("opType") String opType){
		fcAccCancelService.editCancel(fcCancelAccountVO,opType,this.getCurrentUser());
		return success();
	}
	/**
	* 取消--还原注销申请前的数据
	* @param id
	* @return
	*/
	//////@RequiresPermissions(value = {ResourceConstant.NETBANK_INNERACCOUNT_FCACC_CANCELAPPLY,
//			ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_CANCELAPPLY},
//			logical = Logical.OR)
	@LogMessage(content = "还原改户申请前的数据",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/cancel/{id}")
	@ResponseBody
	public AjaxJson cancel(@PathVariable("id") String id){
		fcAccCancelService.canelEntity(id,getCurrentUser());
		return success();
	}
	//////@RequiresPermissions(value = {ResourceConstant.NETBANK_INNERACCOUNT_FCACC_CANCELAPPLY,
//			ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_CANCELAPPLY},
//			logical = Logical.OR)
	@LogMessage(content = "复核编辑页面跳转",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@GetMapping("/check")
	public String check(FcAccVO fcAcc, Model model) {
		if (StringUtils.isNotEmpty(fcAcc.getId())) {
			FcAccVO acc =  fcAccService.getFcAcc(fcAcc.getId());
			FcAccWriteoffVO writeoff = null;//fcAccWriteoffService.getFcAccWriteoff(acc.getBlnctrAcc());
			String busGlobalId="";
			if(writeoff != null && StringUtils.isNotEmpty(writeoff.getBusGlobalId())){
				busGlobalId = writeoff.getBusGlobalId();
			}
			search(fcAcc.getId(),model);
			model.addAttribute("busGlobalId", busGlobalId);
			return "inneraccount/checkFcAccCancel";
		}
		return "inneraccount/checkFcAccCancel";
	}
	/**
	 * 查看复核公用
	 * @param id
	 * @param model
	 */
	private void search(String id,Model model){
		FcAccVO fcAcc = fcAccService.findFcAccById(id);
		int accAccNoRule = configService.getConfig("fcs","accAccnoRule").toInteger(1).intValue();
		if(AllocationConstant.POOL_SIGN_1.equals(fcAcc.getPoolSign())){
			fcAcc = fcAccService.findFcAccByIdForPool(id);
			fcAcc.setNetId(fcAcc.getNetPoolId());
			fcAcc.setSubjectId(fcAcc.getSubjectPoolId());
			accAccNoRule = configService.getConfig("pool", "poolAccnoRule").toInteger(1).intValue();
			FcCorpPoolDTO fcCorpPoolDTO = fcCorpPoolService.getById(fcAcc.getNetPoolId());
			model.addAttribute("netCode", fcCorpPoolDTO.getPoolCode());
			model.addAttribute("corpCode", (StringUtils.isEmpty(fcAcc.getCorpId()) || corpService.get(fcAcc.getCorpId())==null)?"":corpService.get(fcAcc.getCorpId()).getCode());
		}else{
			model.addAttribute("netCode", (StringUtils.isEmpty(fcAcc.getNetId()) || corpService.get(fcAcc.getNetId())==null)?"": corpService.get(fcAcc.getNetId()).getCode());
			model.addAttribute("corpCode", (StringUtils.isEmpty(fcAcc.getCorpId()) || corpService.get(fcAcc.getCorpId())==null)?"":corpService.get(fcAcc.getCorpId()).getCode());
			model.addAttribute("unionNetCode", (StringUtils.isEmpty(fcAcc.getUnionNet()) || corpService.get(fcAcc.getUnionNet())==null)?"":corpService.get(fcAcc.getUnionNet()).getCode());
		}
		if (ObjectUtil.isNotEmpty(fcAcc.getAccType())){
			fcAcc.setAccTypeName(dictService.get("InnerAccount.AccType", fcAcc.getAccType()));
		}
		fcAcc.setAccAccNoRule(accAccNoRule);
		List<FcAccVO> accListByExpPay = fcAccService.getAccListByExpPay(fcAcc);
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
		fcAcc.setInterestAcc(interestAccTemp);
		model.addAttribute("fcAccPage", fcAcc);
		//顺序号生成模式
		int seqGeneratingMode = configService.getConfig("fcs","seqGeneratingMode").toInteger(1).intValue();
		model.addAttribute("seqNumPage", seqGeneratingMode);
		//根据资金池标志生成内部账号生成法则
		String reVal = fcAccService.getRuleVaql(AllocationConstant.POOL_SIGN_0);
		int relen = reVal.length();
		model.addAttribute("relen", relen);
	}
	//////@RequiresPermissions(value = {ResourceConstant.NETBANK_INNERACCOUNT_FCACC_CANCELAPPLY,
//			ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_CANCELAPPLY},
//			logical = Logical.OR)
	@LogMessage(content = "注销复核通过或者打回",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/checkPassOrBack")
	@ResponseBody
	public AjaxJson checkPassOrBack(FcAccVO fcAcc){
		fcAccCancelService.check(fcAcc,this.getCurrentUser());
		return  success();
	}
	/**
	 * 验证公共入口
	 * @param request
	 * @return
	 */
	@LogMessage(content = "验证是否可以注销",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/validFcAcc")
	@ResponseBody
	public AjaxJson validFcAcc(HttpServletRequest request) {
		String msg="";
		AjaxJson json=new AjaxJson();
		String fcAccId=request.getParameter("id");
		if (StringUtils.isNotEmpty(fcAccId)) {
			FcAccVO fcAcc = fcAccService.getFcAcc(fcAccId);
			fcAccCancelService.checkCommonEnter(fcAcc,getCurrentUser());
			if (fcAcc != null) {
				//非资金池的企业活期 并且 联机余额或者复核余额不为0 才需要调出记账分录　验证是否有效
				if(fcAccCancelService.isCurrType(fcAcc.getAccType()) && fcAccCancelService.validAmt(fcAcc.getBlnctrAcc())
						&& !AllocationConstant.POOL_SIGN_1.equals(fcAcc.getPoolSign())){
					fcAccCancelService.getCancelPreData(fcAcc.getBlnctrAcc(),getCurrentUser());
					Map<String, Object> map = new HashMap<>();
					map.put("isProcess", "1");
					json.setAttributes(map);
				}
			}
		}
		json.setObj(msg);
		json.setSuccess(true);
		return json;
	}
	/**
	 *
	 * 注销申请页面跳转
	 * @return
	 */
	@LogMessage(content = "注销申请页面跳转",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@PostMapping("/viewFcCancelAccount")
	public String viewFcCancelAccount(HttpServletRequest request, Model model) {
		String account = request.getParameter("account"); //内部账号
		if (StringUtils.isNotEmpty(account)) {
			FcCancelAccountVO fcCancelAccountVO=fcAccCancelService.getCancelPreData(account,getCurrentUser());
			if("0".equals(fcCancelAccountVO.getIsNeedBusRule())){//不需要定义会计分录
				return "inneraccount/viewFcCancelAccount01";
			}
			model.addAttribute("listCancelVOPage", fcCancelAccountVO);
			Integer size = 0;
			if(!EmptyUtils.isEmpty(fcCancelAccountVO.getFcAccbookVoucher())
					&& !EmptyUtils.isEmpty(fcCancelAccountVO.getFcAccbookVoucher().getBookDtls())
					&& fcCancelAccountVO.getFcAccbookVoucher().getBookDtls().size() >0){
				size = fcCancelAccountVO.getFcAccbookVoucher().getBookDtls().size();
			}
			model.addAttribute("size", size);
		}
		return "inneraccount/viewFcCancelAccount";
	}
	/**
	 *
	 * 注销编辑页面跳转
	 * @return
	 */
	@LogMessage(content = "注销编辑页面跳转",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@PostMapping("/editFcCancelAccount")
	public String editFcCancelAccount(HttpServletRequest request, Model model) {
		return cancelEditPreData(request,model,"inneraccount/editFcCancelAccount");
	}
	/**
	 *
	 * 注销复核页面跳转
	 * @return
	 */
	@LogMessage(content = "注销复核页面跳转",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@PostMapping("/checkFcCancelAccount")
	public String checkFcCancelAccount(HttpServletRequest request, Model model) {
		return cancelEditPreData(request,model,"inneraccount/checkFcCancelAccount");
	}
	/**
	 *  获取记账分录页面
	 * @param request
	 * @param model
	 * @param returnPage
	 * @return
	 */
	private String cancelEditPreData(HttpServletRequest request, Model model,String returnPage) {
		String account = request.getParameter("account"); //内部账号
		if (StringUtils.isNotEmpty(account)) {
			FcCancelAccountVO fcCancelAccountVO = fcAccCancelService.getCancelEditPreData(account,getCurrentUser());
			if("0".equals(fcCancelAccountVO.getIsNeedBusRule())){//不需要定义会计分录
				return "inneraccount/viewFcCancelAccount01";
			}
			model.addAttribute("listCancelVOPage", fcCancelAccountVO);
			Integer size = 0;
			if(!EmptyUtils.isEmpty(fcCancelAccountVO.getFcAccbookVoucher())
					&& !EmptyUtils.isEmpty(fcCancelAccountVO.getFcAccbookVoucher().getBookDtls())
					&& fcCancelAccountVO.getFcAccbookVoucher().getBookDtls().size() >0){
				size = fcCancelAccountVO.getFcAccbookVoucher().getBookDtls().size();
			}
			model.addAttribute("size", size);
		}
		return returnPage;
	}
    /**
     * 获取资金池
     * @return
     */
    @RequestMapping(params = "getUserCorpPools")
    @ResponseBody
    public List<FcCorpPoolDTO> getUserCorpPools() {
        String resource = ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_CANCELQUERY;
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
        String resource = ResourceConstant.NETBANK_INNERACCOUNT_FCACC_POOL_CANCELQUERY;
        return fcCorpPoolService.getAllUserCorpPoolChilds(this.getCurrentUser(), resource, poolId);
    }
}
