package com.bytter.biz.allocation.controller;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.allocation.entity.BisGatherPolicyAccount;
import com.bytter.biz.allocation.entity.BisGatherPolicyAgreement;
import com.bytter.biz.allocation.service.BisGatherPolicyAccountService;
import com.bytter.biz.allocation.service.BisGatherPolicyAgreementService;
import com.bytter.biz.allocation.vo.BisGatherPolicyAccountVO;
import com.bytter.biz.allocation.vo.BisGatherPolicyAgreementVO;
import com.bytter.biz.inneraccount.entity.BisGatherPolicy;
import com.bytter.biz.inneraccount.service.BisGatherPolicyService;
import com.bytter.biz.bdlink.api.enums.AllocationMessageEnum;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.FcAccDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
////import org.apache.shiro.authz.annotation.Logical;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * @Title: Controller
 * @Description: 调拨策略表
 * @author onlineGenerator
 * @date 2017-06-16 14:38:52
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/allocation/bisgatherpolicy")
public class BisGatherPolicyController extends BaseController {
	@Autowired
	private BisGatherPolicyService bisGatherPolicyService;
	@Autowired
	private BisGatherPolicyAccountService bisGatherPolicyAccountService;
	@DubboReference(check = false)
	private IAccountGrantService accountGrantService;
	@DubboReference(check = false)
	private IDictService dictService;
	@DubboReference(check = false)
	private ICorpService iCorpService;
	@DubboReference(check = false)
	private IBtBankAccService btBankAccService;
	@Autowired
	private BisGatherPolicyAgreementService bisGatherPolicyAgreementService;

	/**
	* 调拨策略表列表 页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_ALLOCATION_BISGATHERPOLICY_QUERY)
	@LogMessage(content = "查询调拨策略表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "allocation/listBisGatherPolicy";
	}
    /**
    * 调拨策略表 table列表
    *
    * @return
    */
    @SuppressWarnings("rawtypes")
	//////@RequiresPermissions(ResourceConstant.NETBANK_ALLOCATION_BISGATHERPOLICY_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return bisGatherPolicyService.getDataTables(dataTablesInput);
    }
	/**
	* 调拨策略表新增页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_ALLOCATION_BISGATHERPOLICY_APPLY)
	@GetMapping("/add")
	public String add(BisGatherPolicy bisGatherPolicy, Model model) {
		if (StringUtils.isNotEmpty(bisGatherPolicy.getId())) {
			bisGatherPolicy = bisGatherPolicyService.get(bisGatherPolicy.getId());
			model.addAttribute("bisGatherPolicyPage", bisGatherPolicy);
		}
		model.addAttribute("defDate", DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
		return "allocation/addBisGatherPolicy";
	}
	/**
	* 调拨策略表编辑页面跳转
	*
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_ALLOCATION_BISGATHERPOLICY_APPLY)
	@GetMapping("/update")
	public String update(BisGatherPolicy bisGatherPolicy, Model model) {
		if (StringUtils.isNotEmpty(bisGatherPolicy.getId())) {
			bisGatherPolicy = bisGatherPolicyService.get(bisGatherPolicy.getId(), true);
			if (ObjectUtil.isNotEmpty(bisGatherPolicy) &&  "6".equals(bisGatherPolicy.getGatherMode())) {
				// 去除小数点后面的0，直接去掉。不进行四舍五入
				bisGatherPolicy.setGatherAmt(bisGatherPolicy.getGatherAmt().setScale(0, BigDecimal.ROUND_DOWN));
			}
			if (ObjectUtil.isNotEmpty(bisGatherPolicy) &&  "5".equals(bisGatherPolicy.getGatherMode())) {
				// 去除小数点后面的0，直接去掉。不进行四舍五入
				bisGatherPolicy.setMinAmt(bisGatherPolicy.getMinAmt().setScale(0, BigDecimal.ROUND_DOWN));
			}
			model.addAttribute("bisGatherPolicyPage", bisGatherPolicy);
		}
		return "allocation/updateBisGatherPolicy";
	}
	/**
	* 调拨策略表详情页
	*
	* @return
	*/
	//////@RequiresPermissions(value={ResourceConstant.NETBANK_ALLOCATION_BISGATHERPOLICY_QUERY,
//			ResourceConstant.NETBANK_ALLOCATION_BISACCGATHERPOLICY_QUERY},
//			logical= Logical.OR)
	@LogMessage(content = "查询调拨策略表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		BisGatherPolicy bisGatherPolicy = bisGatherPolicyService.get(id, true);
		if (ObjectUtil.isNotEmpty(bisGatherPolicy) && "6".equals(bisGatherPolicy.getGatherMode())) {
			// 去除小数点后面的0，直接去掉。不进行四舍五入
			bisGatherPolicy.setGatherAmt(bisGatherPolicy.getGatherAmt().setScale(0, BigDecimal.ROUND_DOWN));
		}
		if (ObjectUtil.isNotEmpty(bisGatherPolicy) && "5".equals(bisGatherPolicy.getGatherMode())) {
			// 去除小数点后面的0，直接去掉。不进行四舍五入
			bisGatherPolicy.setMinAmt(bisGatherPolicy.getMinAmt().setScale(0, BigDecimal.ROUND_DOWN));
		}
		model.addAttribute("bisGatherPolicyPage", bisGatherPolicy);
		return "allocation/viewBisGatherPolicy";
	}
	/**
	 * 添加调拨策略表
	 * @param bisGatherPolicy
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_ALLOCATION_BISGATHERPOLICY_APPLY)
	@LogMessage(content = "添加调拨策略表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(BisGatherPolicy bisGatherPolicy){
		//判断策略编码是否唯一
		if(bisGatherPolicyService.findByCode(bisGatherPolicy.getGatherPolicyCode())!=null){
			throw new ParameterException(AllocationMessageEnum.NIS_ALLOCATION_MESSAGE_GATHERPOLICYCODE_UNIQUE);
		}
		if (bisGatherPolicy!=null){
			//4固定余额 3固定额度 0根据前一日归集金额下拨
			if ("0".equals(bisGatherPolicy.getGatherMode())){
				//将frq_mode和serial_day置空
				bisGatherPolicy.setFrqMode(null);
				bisGatherPolicy.setSerialDay(null);
			}
			if ("4".equals(bisGatherPolicy.getGatherMode())){
				//将downFixedAmt置空
				bisGatherPolicy.setDownFixedAmt(null);
			}
			if ("3".equals(bisGatherPolicy.getGatherMode())){
				//将gatherAmt置空
				bisGatherPolicy.setGatherAmt(null);
			}
		}
		String launchTime = DateUtils.formatDate(bisGatherPolicy.getDefDate(),"HH:mm:ss").replace(":", "");
		bisGatherPolicy.setLaunchTime(launchTime);
		String endTime = DateUtils.formatDate(bisGatherPolicy.getEndDate(),"HH:mm:ss").replace(":", "");
		bisGatherPolicy.setEndTime(endTime);
		bisGatherPolicyService.save(bisGatherPolicy, this.getCurrentUser());
		return success();
	}
	/**
	* 修改调拨策略表
	* @param bisGatherPolicy
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_ALLOCATION_BISGATHERPOLICY_APPLY)
	@LogMessage(content = "修改调拨策略表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(BisGatherPolicy bisGatherPolicy){
		bisGatherPolicyService.updateBisGatherPolicy(bisGatherPolicy);
		return success();
	}
	/**
	* 删除调拨策略表
	* @param id
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_ALLOCATION_BISGATHERPOLICY_APPLY)
	@LogMessage(content = "删除调拨策略表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id){
		BisGatherPolicy bisGatherPolicy = bisGatherPolicyService.get(id);
		bisGatherPolicyService.deleteEntity(bisGatherPolicy);
		return success();
	}
	/**
	* 批量删除调拨策略表
	* @param ids
	* @return
	*/
	//////@RequiresPermissions(ResourceConstant.NETBANK_ALLOCATION_BISGATHERPOLICY_APPLY)
	@LogMessage(content = "批量删除调拨策略表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson deleteDelete(@ModelAttribute("ids")String ids){
		for(String id : ids.split(CommonConstant.COMMA_SPLIT)){
			BisGatherPolicy bisGatherPolicy = bisGatherPolicyService.get(id);
			bisGatherPolicyService.deleteEntity(bisGatherPolicy);
		}
		return success();
	}
	/**
	 * 批量启用调拨策略
	 *
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_ALLOCATION_BISGATHERPOLICY_APPLY)
	@LogMessage(content = "批量启用调拨策略", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/batchEnable")
	@ResponseBody
	public AjaxJson batchEnable(@RequestParam("ids") String ids) {
		this.bisGatherPolicyService.batchUpdateValidSign(ids, CommonConstant.YES);
		return success();
	}
	/**
	 * 批量停用调拨策略
	 *
	 * @return
	 */
	//////@RequiresPermissions(ResourceConstant.NETBANK_ALLOCATION_BISGATHERPOLICY_APPLY)
	@LogMessage(content = "批量停用调拨策略", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/batchStop")
	@ResponseBody
	public AjaxJson batchStop(@RequestParam("ids") String ids) {
		this.bisGatherPolicyService.batchUpdateValidSign(ids, CommonConstant.NO);
		return success();
	}

	/********************************资金中心调拨账户配置*******************************************************/
	@LogMessage(content = "资金中心调拨账户配置列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_SELECT)
	@GetMapping("/accountList")
	public String accountList(Model model) {
		return "allocation/bisGatherPolicyAccountList";
	}

	@PostMapping("/accountDatatable")
	@ResponseBody
	public DataTablesOutput accountDatatable(@RequestBody DataTablesInput dataTablesInput){
		return bisGatherPolicyAccountService.getAccountDatatable(dataTablesInput);
	}

	@GetMapping("/addAccount")
	public String addAccount( Model model) {
		return "allocation/addBisGatherPolicyAccount";
	}

	@PostMapping("/getPayerAccQuery")
	@ResponseBody
	public List<BtBankAccDTO> getPayerAccQuery(String resource,String corpCode) {
		String corpId = "";
		if (ObjectUtil.isNotEmpty(corpCode)){
			CorpDTO corp = iCorpService.getCorpByCode(corpCode);
			if (ObjectUtil.isNotEmpty(corp)){
				corpId = corp.getId();
			}
		}
		List<BtBankAccDTO> listAcc = new ArrayList<>();
		//获取转账权限的银行账号
		List<BtBankAccDTO> listBankAccTemp = accountGrantService.listDTONotUnionByMultiVal(this.getUserId(), corpId, resource, null, "2,3", AccountGrantConstant.TRANSFER_ACCOUNT);
		for (BtBankAccDTO dto : listBankAccTemp) {
			dto.setIsOnlineStr(dictService.get("sf_yn", dto.getIsOnline()));
			listAcc.add(dto);
		}
		List<FcAccDTO> listFcAcc = accountGrantService.listDTONotUnionByMultiVal(this.getUserId(), corpId, resource, null, "1", AccountGrantConstant.TRANSFER_ACCOUNT);
		if (!EmptyUtils.isEmptyList(listFcAcc)) {
			for (FcAccDTO fcAcc : listFcAcc) {
				//过滤企业活期账号
				if (InnerAccountConstant.FCACC_BUSINESS_ACCOUNT.equals(fcAcc.getAccType())) {
					BtBankAccDTO bankAcc = new BtBankAccDTO();
					bankAcc.setId(fcAcc.getId());
					bankAcc.setBankAcc(fcAcc.getBlnctrAcc());
					bankAcc.setAccName(fcAcc.getAccName());
					bankAcc.setCurId(fcAcc.getCurId());
					bankAcc.setCurName(fcAcc.getCurName());
					bankAcc.setBankName(fcAcc.getNetName());
					bankAcc.setBalance(fcAcc.getFcBalance());
					listAcc.add(bankAcc);
				}
			}
		}
		return listAcc;
	}

	@LogMessage(content = "添加资金中心调拨账户配置",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/saveAccount")
	@ResponseBody
	public AjaxJson saveAccount(BisGatherPolicyAccountVO policyAccountVO){
		//判断账号
		if (bisGatherPolicyAccountService.checkExist("bankAcc",policyAccountVO.getBankAcc(),null)){
			throw new ParameterException(AllocationMessageEnum.NIS_ALLOCATION_POLICY_ACCOUNT_EXIST,policyAccountVO.getBankAcc());
		}
		// 判断优先级是否唯一
		if(bisGatherPolicyAccountService.checkExist("priority",policyAccountVO.getPriority(),null)){
			throw new ParameterException(AllocationMessageEnum.NIS_ALLOCATION_POLICY_PRIORITY_EXIST,policyAccountVO.getPriority().toString());
		}
		BtBankAccDTO bankAccInfo = btBankAccService.getBankAccInfo(policyAccountVO.getBankAcc());
		if(ObjectUtil.isNotEmpty(bankAccInfo)){
			policyAccountVO.setBankName(bankAccInfo.getBankName());
			policyAccountVO.setCorpName(bankAccInfo.getCorpName());
		}
		bisGatherPolicyAccountService.save(policyAccountVO, this.getCurrentUser());
		return success();
	}

	@LogMessage(content = "删除资金中心调拨账户配置",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/deleteAccount/{id}")
	@ResponseBody
	public AjaxJson deleteAccount(@PathVariable("id") String id){
		BisGatherPolicyAccount policyAccount = bisGatherPolicyAccountService.get(id);
		bisGatherPolicyAccountService.deleteEntity(policyAccount);
		return success();
	}

	@LogMessage(content = "查询资金中心调拨账户配置",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/viewAccount/{id}")
	public String viewAccount(@PathVariable("id") String id, Model model) {
		BisGatherPolicyAccount policyAccount = bisGatherPolicyAccountService.get(id, true);
		model.addAttribute("policyAccountPage", policyAccount);
		return "allocation/viewBisGatherPolicyAccount";
	}

	@GetMapping("/updateAccount")
	public String updateAccount(BisGatherPolicyAccount policyAccount, Model model) {
		if (StringUtils.isNotEmpty(policyAccount.getId())) {
			policyAccount = bisGatherPolicyAccountService.get(policyAccount.getId(), true);
			model.addAttribute("policyAccountPage", policyAccount);
		}
		return "allocation/updateBisGatherPolicyAccount";
	}

	@LogMessage(content = "修改资金中心调拨账户配置",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modifyAccount")
	@ResponseBody
	public AjaxJson modifyAccount(BisGatherPolicyAccountVO policyAccountVO){
		// 判断账号是否唯一
		if (bisGatherPolicyAccountService.checkExist("bankAcc",policyAccountVO.getBankAcc(),policyAccountVO.getId())){
			throw new ParameterException(AllocationMessageEnum.NIS_ALLOCATION_POLICY_ACCOUNT_EXIST,policyAccountVO.getBankAcc());
		}
		// 判断优先级是否唯一
		if(bisGatherPolicyAccountService.checkExist("priority",policyAccountVO.getPriority(),policyAccountVO.getId())){
			throw new ParameterException(AllocationMessageEnum.NIS_ALLOCATION_POLICY_PRIORITY_EXIST,policyAccountVO.getPriority().toString());
		}
		BtBankAccDTO bankAccInfo = btBankAccService.getBankAccInfo(policyAccountVO.getBankAcc());
		if(ObjectUtil.isNotEmpty(bankAccInfo)){
			policyAccountVO.setBankName(bankAccInfo.getBankName());
			policyAccountVO.setCorpName(bankAccInfo.getCorpName());
		}
		bisGatherPolicyAccountService.modifyAccount(policyAccountVO,this.getCurrentUser());
		return success();
	}

	/********************************内部资金归集管理协议签订*******************************************************/
	@LogMessage(content = "内部资金归集管理协议签订列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_SELECT)
	@GetMapping("/agreementList")
	public String agreementList(Model model) {
		return "allocation/bisGatherPolicyAgreementList";
	}

	@PostMapping("/agreementDatatable")
	@ResponseBody
	public DataTablesOutput agreementDatatable(@RequestBody DataTablesInput dataTablesInput){
		return bisGatherPolicyAgreementService.getDataTables(dataTablesInput);
	}

	@GetMapping("/addAgreement")
	public String addAgreement( Model model) {
		return "allocation/addBisGatherPolicyAgreement";
	}

	@LogMessage(content = "添加内部资金归集管理协议签订",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
	@PostMapping("/saveAgreement")
	@ResponseBody
	public AjaxJson saveAgreement(BisGatherPolicyAgreementVO policyAgreementVO){
		//判断单位
		String[] corpIds = policyAgreementVO.getCorpId().split(CommonConstant.COMMA_SPLIT);
		for (String corpId : corpIds){
			if (bisGatherPolicyAgreementService.checkCorpExist(corpId,null)){
				CorpDTO corpDTO = iCorpService.get(corpId);
				throw new ParameterException(AllocationMessageEnum.NIS_ALLOCATION_POLICY_AGREEMENT_EXIST,corpDTO.getName());
			}
		}
		bisGatherPolicyAgreementService.save(policyAgreementVO, this.getCurrentUser());
		return success();
	}

	@LogMessage(content = "删除内部资金归集管理协议签订",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
	@PostMapping("/deleteAgreement/{id}")
	@ResponseBody
	public AjaxJson deleteAgreement(@PathVariable("id") String id){
		BisGatherPolicyAgreement policyAgreement = bisGatherPolicyAgreementService.get(id);
		bisGatherPolicyAgreementService.deleteEntity(policyAgreement);
		return success();
	}

	@GetMapping("/updateAgreement")
	public String updateAgreement(BisGatherPolicyAgreement policyAgreement, Model model) {
		if (StringUtils.isNotEmpty(policyAgreement.getId())) {
			policyAgreement = bisGatherPolicyAgreementService.get(policyAgreement.getId(), true);
			model.addAttribute("policyAgreementPage", policyAgreement);
		}
		return "allocation/updateBisGatherPolicyAgreement";
	}

	@LogMessage(content = "修改内部资金归集管理协议签订",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modifyAgreement")
	@ResponseBody
	public AjaxJson modifyAgreement(BisGatherPolicyAgreementVO policyAgreementVO){
		//判断单位
		String[] corpIds = policyAgreementVO.getCorpId().split(CommonConstant.COMMA_SPLIT);
		for (String corpId : corpIds){
			if (bisGatherPolicyAgreementService.checkCorpExist(corpId,policyAgreementVO.getId())){
				CorpDTO corpDTO = iCorpService.get(corpId);
				throw new ParameterException(AllocationMessageEnum.NIS_ALLOCATION_POLICY_AGREEMENT_EXIST,corpDTO.getName());
			}
		}
		bisGatherPolicyAgreementService.modify(policyAgreementVO,this.getCurrentUser());
		return success();
	}

	@PostMapping("/saveAgreementImport")
	@ResponseBody
	public AjaxJson saveAgreementImport(@RequestBody List<BisGatherPolicyAgreementVO> list) {
		bisGatherPolicyAgreementService.saveImport(list, this.getCurrentUser());
		return success();
	}

}
