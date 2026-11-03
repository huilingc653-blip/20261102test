package com.bytter.biz.mobile.controller;

import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.bdlink.vo.BisAccDtlVO;
import com.bytter.biz.inneraccount.vo.BtBankAccMobileVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.common.rest.RestInput;
import com.bytter.core.common.rest.RestResponse;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.vo.BisAccAttributeVO;
import com.bytter.system.api.vo.BtBankAccVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;


/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 银行账号app
 * @date 2017-06-29 13:51:01
 */
@Controller
@RequestMapping("/api/bankacc")
public class BtBankAccMobileController extends BaseController {

	@DubboReference(check = false)
	private IBankAccMobileService iBankAccMobileService;

	@DubboReference(check = false)
	private IBtBankAccService iBtBankAccService; //这个里面的Service有很多没有实现的逻辑，1367行开始

	@DubboReference(check = false)
	private ICorpService corpService;

	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;

	@DubboReference(check = false)
	private IBisAccAttributeService iBisAccAttributeService;

	@DubboReference(check = false)
	private IDictService dictService;

	@DubboReference(check = false)
	private IBtBankAccCommonService iBtBankAccCommonService;

	@DubboReference(check = false)
	private IBisAccDtlService iBisAccDtlService;

	@DubboReference(check = false)
	private IBisAccBalService iBisAccBalService;

	@DubboReference(check = false)
	private IBankTypeService bankTypeService;

	@DubboReference(check = false)
	private IUserService userService;
	@DubboReference(check = false)
	private IAccountGrantService accountGrantService;

	/**
	 * APP报表-结算管理
	 *
	 * @param model model
	 * @return
	 */
	@RequestMapping("/appNetbankReportIndex")
	public String appNetbankReportIndex(Model model) {
		String userId = getCurrentUser().getId();
		UserDTO userDTO = userService.getByUserId(userId);
		CorpDTO corp = userDTO.getCorp();
		model.addAttribute("corpId", corp.getId());
		model.addAttribute("netId", corp.getNetId());
		return "bankacc/appNetbankReportIndex";
	}

	/**
	 * 银行账号查询
	 */
	@RequestMapping("/mobileBankAcc")
	public String getbankAcc(Model model){
		List<BisAccAttributeDTO> validAttribute = iBisAccAttributeService.getValidAttribute();//账户属性
		List<BisAccAttributeVO> listAttribute = BeanUtils.copyListProperties(validAttribute, BisAccAttributeVO.class);
		List<DictDTO> list = dictService.list("btBankAcc.accType");
		List<BankTypeDTO> bankTypeDTOS = bankTypeService.listByValidSign(CommonConstant.VALID_SIGN_YES);
		model.addAttribute("attribute", listAttribute);
		model.addAttribute("bankAccType", list);
		model.addAttribute("bankType", bankTypeDTOS);
		return "mobile/bankacc/mobileBankAcc";
	}
	/**
	 * 银行账号查询数据列表
	 */
	@PostMapping("/getBankAccMoblieList")
	@ResponseBody
	public Object getBankAccMoblieList(BtBankAccVO vo){
		StringBuilder sql = new StringBuilder();
		String corpAuthSql = SqlUtil.getInSql(
				corpService.getUserCorpIds(ResourceConstant.NETBANK_BANKACC_BTBANKACCQUERY_QUERY, this.getUserId()), "b.corp_id",
				true);
		sql.append(" and ").append(corpAuthSql);

		List<BtBankAccVO> list = iBtBankAccService.getListByVO(vo,sql);
		List<BankTypeDTO> bankTypeDTOS = bankTypeService.listByValidSign(CommonConstant.VALID_SIGN_YES);
		for(BtBankAccVO entity:list){
			if(StringUtils.isBlank(entity.getBankTypeId())){
				break;
			}
			for(BankTypeDTO dto:bankTypeDTOS){
				if(entity.getBankTypeId().equals(dto.getId())){
					entity.setIcon(dto.getIcon());
					break;
				}
			}
		}
		return list;
	}

	/**
	 * 银行账号查询数据列表 - 分页
	 */
	@RequestMapping("/getBankAccMobliePageList")
	@ResponseBody
	public Object getBankAccMobliePageList(@Param("pageNumber") Integer pageNumber,
										   @Param("pageSize") Integer pageSize,
										   BtBankAccVO vo) {
		RestInput restInput = new RestInput();
		restInput.put("pageNumber", pageNumber);
		restInput.put("pageSize", pageSize);
		IPageResult<BtBankAccVO> restResponse = iBtBankAccService.getPageListByVO(restInput, vo, getCurrentUser());
		List<BtBankAccVO> list = restResponse.getList();
		List<BankTypeDTO> bankTypeDTOS = bankTypeService.listByValidSign(CommonConstant.VALID_SIGN_YES);
		for (BtBankAccVO entity : list) {
			if (StringUtils.isBlank(entity.getBankTypeId())) {
				break;
			}
			for (BankTypeDTO dto : bankTypeDTOS) {
				if (entity.getBankTypeId().equals(dto.getId())) {
					entity.setIcon(dto.getIcon());
					break;
				}
			}
		}
		return list;
	}

	/**
	 * 银行账号详情
	 */
	@RequestMapping("/mobileBankAccDetails")
	public String mobileBankAccDetails(Model model,String id,String curId){
		BtBankAccDTO dto = iBtBankAccCommonService.getBtBankAccVOById(id, curId);
		BtBankAccVO vo = BeanUtils.copyNewInstanceProperties(dto,BtBankAccVO.class);
		model.addAttribute("vo", vo);
		return "mobile/bankacc/mobileBankAccDetails";
	}

	/**
	 * 银行账号余额查询
	 */
	@RequestMapping("/mobileBankAccBal")
	public String mobileBankAccBal(Model model,String id,String curId){
		List<BtBankAccDTO> accList = accountGrantService.listDTOByMultiVal(this.getCurrentUser().getId(), ResourceConstant.NETBANK_BANKACC_BISACCBAL_QUERY, null, AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE, AccountGrantConstant.QUERY_BALANCE);
		if(accList!=null && accList.size()>0){
			HashMap<String, BtBankAccDTO> bankAccMap = new HashMap<>();
			for(BtBankAccDTO bankAcc:accList){
				bankAccMap.put(bankAcc.getId(), bankAcc);
			}
			if(bankAccMap.containsKey(id)){
				BtBankAccDTO btBankAccDTO = iBtBankAccCommonService.getBtBankAccVOById(id, curId);
				BtBankAccVO bankAcc = BeanUtils.copyNewInstanceProperties(btBankAccDTO,BtBankAccVO.class);
				model.addAttribute("bankAcc", bankAcc);
				com.bytter.biz.bdlink.api.dto.BisAccBalDTO bisAccBalDTO = iBisAccBalService.getLastBisAccBalBy(bankAcc.getBankAcc(), bankAcc.getCurId());
				BisAccBalDTO lastBisAccBalBy = BeanUtils.copyNewInstanceProperties(bisAccBalDTO,BisAccBalDTO.class);
				model.addAttribute("bal", lastBisAccBalBy);
				return "mobile/bankacc/mobileBankAccBal";
			}
		}
		model.addAttribute("bankAcc", null);
		model.addAttribute("bal",null);
		return "mobile/bankacc/mobileBankAccBal";
	}

	/**
	 * 银行账号流水查询
	 */
	@RequestMapping("/mobileBankAccDtl")
	public String mobileBankAccDtl(Model model,String id,String curId){
		List<BtBankAccDTO> accList =  accountGrantService.listDTOByMultiValAccId(this.getCurrentUser().getId(), ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_QUERY, null, AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE, AccountGrantConstant.QUERY_DETAIL,id);
		if(accList!=null && accList.size()>0){
			HashMap<String, BtBankAccDTO> bankAccMap = new HashMap<>();
			for(BtBankAccDTO bankAcc:accList){
				bankAccMap.put(bankAcc.getId(), bankAcc);
			}
			if(bankAccMap.containsKey(id)){
				BtBankAccDTO dto = iBtBankAccCommonService.getBtBankAccVOById(id, curId);
				BtBankAccVO bankAcc = BeanUtils.copyNewInstanceProperties(dto, BtBankAccVO.class);
				BigDecimal income = iBisAccDtlService.getListByBankAccAmt(bankAcc.getBankAcc(), bankAcc.getCurId(),"",CommonConstant.NO);
				BigDecimal pay = iBisAccDtlService.getListByBankAccAmt(bankAcc.getBankAcc(), bankAcc.getCurId(),"",CommonConstant.YES);
				model.addAttribute("sumIncome",income);
				model.addAttribute("sumPay",pay);
				model.addAttribute("vo",bankAcc);
				return "mobile/bankacc/mobileBankAccDtl";
			}
		}
		model.addAttribute("vo",null);
		return "mobile/bankacc/mobileBankAccDtl";
	}

	/**
	 * 银行账号流水查询数据接口
	 */
	@PostMapping("/mobileBankAccDtlData")
	@ResponseBody
	public Object mobileBankAccDtlData(Model model, String id, String curId, ModelMap map,String remark){
		BtBankAccDTO dto = iBtBankAccCommonService.getBtBankAccVOById(id, curId);
		BtBankAccVO bankAcc = BeanUtils.copyNewInstanceProperties(dto, BtBankAccVO.class);
		if(StringUtils.isBlank(remark)){
			remark="";
		}
		List<BisAccDtlVO> listByBankAcc = iBisAccDtlService.getListByBankAcc(bankAcc.getBankAcc(), bankAcc.getCurId(), remark, null, null);
		return listByBankAcc;
	}

	/**
	 * 银行账号流水查询数据接口 - 分页
	 * 【具体-银行流水查询-内部的下方数据的接口】
	 */
	@PostMapping("/mobileBankAccDtlPaginationData")
	@ResponseBody
	public Object mobileBankAccDtlData(@Param("pageNumber") Integer pageNumber,
									   @Param("pageSize") Integer pageSize,
									   String id,
									   String curId,
									   String remark,
									   String beginDate,
									   String endDate) {
		BtBankAccDTO dto = iBtBankAccCommonService.getBtBankAccVOById(id, curId);
		BtBankAccVO bankAcc = BeanUtils.copyNewInstanceProperties(dto, BtBankAccVO.class);
		if (StringUtils.isBlank(remark)) {
			remark = "";
		}
		remark = StringUtils.getUtf8String(remark);
		RestInput restInput = new RestInput();
		restInput.put("pageNumber", pageNumber);
		restInput.put("pageSize", pageSize);
		IPageResult<BisAccDtlVO> iPageResult = iBisAccDtlService.getPageListByBankAcc(restInput, bankAcc.getBankAcc(), bankAcc.getCurId(), remark, beginDate, endDate);

		//统计收入支出，不分页
		BigDecimal sumIncome = iBisAccDtlService.getListWithTransTime(bankAcc.getBankAcc(), bankAcc.getCurId(),"",CommonConstant.NO, beginDate, endDate);
		BigDecimal sumPay = iBisAccDtlService.getListWithTransTime(bankAcc.getBankAcc(), bankAcc.getCurId(),"",CommonConstant.YES, beginDate, endDate);

		RestResponse response = iPageResult.convert(RestResponse.class);
		response.setSumIncome(sumIncome);
		response.setSumPay(sumPay);
		return response;
	}

	/**
	 * 银行账号流水查询--详情
	 */
	@RequestMapping("/mobileBankAccDtlInfo")
	public String mobileBankAccDtlInfo(Model model,String id,String curId,String flag){
		com.bytter.biz.bdlink.api.dto.BisAccDtlDTO dto = iBisAccDtlService.getByUnion(id);
		BisAccDtlVO byUnion = BeanUtils.copyNewInstanceProperties(dto, BisAccDtlVO.class);
		model.addAttribute("dtl",byUnion);
		model.addAttribute("chinese",StringUtils.chineseMoney(byUnion.getAmt()));
		return "mobile/bankacc/mobileBankAccDtlInfo";
	}

	/**
	 * BIS_ACC_BAL列表 页面跳转
	 *
	 * @return
	 */
	@PostMapping("/mobileBal")
	public String getBal(Model model,String corpId,String beginDate,String endDate) {
		List<CorpDTO> corpList = corpService.listUserCorp(ResourceConstant.NETBANK_BANKACC_BISACCBAL_QUERY,getUserId());
		//获取有权限的银行账号
		List<BtBankAccMobileDTO> bal = iBankAccMobileService.getGrantBankAcc(getUserId(), corpId, "bal");
		List<BtBankAccMobileVO> bankList = BeanUtils.copyListProperties(bal, BtBankAccMobileVO.class);
		List<BtCurrencyDTO> curList = btCurrencyService.getBtCurrency();
		model.addAttribute("corpList",corpList);
		model.addAttribute("bankList", bankList);
		model.addAttribute("userId", getUserId());
		model.addAttribute("corpId", corpId);
		model.addAttribute("beginDate", beginDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("curList", curList);
		return "mobile/bankacc/mobileBal";
	}
	/**
	 * BIS_ACC_BAL列表 页面跳转
	 *
	 * @return
	 */
	@GetMapping("/mobileBal2")
	public String getBalget(Model model) {
		List<CorpDTO> corpList = corpService.listUserCorp(ResourceConstant.NETBANK_BANKACC_BISACCBAL_QUERY,getUserId());
		//获取有权限的银行账号
//		List<BtBankAccMobileVO> bankList = bankAccMobieService.getGrantBankAcc(getUserId(), corpId, "bal");
		List<BtCurrencyDTO> curList = btCurrencyService.getBtCurrency();
		model.addAttribute("corpList",corpList);
//		model.addAttribute("bankList", bankList);
		model.addAttribute("userId", getUserId());
//		model.addAttribute("corpId", corpId);
//		model.addAttribute("beginDate", beginDate);
//		model.addAttribute("endDate", endDate);
		model.addAttribute("curList", curList);
		return "bankacc/mobileBal";
	}

	@PostMapping("/mobileBalList")
	public AjaxJson getBalList(String corpId, String beginDate, String endDate, String bankAccId,String curId, HttpServletResponse response) throws IOException {
		String html = iBankAccMobileService.getBal(corpId,getUserId(), beginDate,endDate,bankAccId,curId);
		response.setContentType("text/plain;charset=UTF-8");
		response.getWriter().println(html);
		return null;
	}

	/**
	 * 银行账号明细表列表 页面跳转
	 *
	 * @return
	 */
	@PostMapping("/mobileDtl")
	public String getDtl(Model model,String corpId) {
		List<CorpDTO> corpList = corpService.listUserCorp(ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_MANAGERQUERY,getUserId());
		//获取有权限的银行账号
		List<BtBankAccMobileDTO> dtl = iBankAccMobileService.getGrantBankAcc(getUserId(), corpId, "dtl");
		List<BtBankAccMobileVO> bankList = BeanUtils.copyListProperties(dtl,BtBankAccMobileVO.class);
		List<BtCurrencyDTO> curList = btCurrencyService.getBtCurrency();
		model.addAttribute("corpList",corpList);
		model.addAttribute("bankList", bankList);
		model.addAttribute("userId", getUserId());
		model.addAttribute("corpId", corpId);
		model.addAttribute("curList", curList);
		return "mobile/bankacc/mobileDtl";
	}

	@PostMapping("/mobileDtlList")
	public AjaxJson getDtlList(String corpId, String beginDate, String endDate, String bankAcc,String beginMoney,String endMoney,String oppBankAcc,String curId,HttpServletResponse response) throws IOException {
		String html = iBankAccMobileService.getDtl(corpId,getUserId(),beginDate, endDate,bankAcc,beginMoney,endMoney,oppBankAcc,curId);
		response.setContentType("text/plain;charset=UTF-8");
		response.getWriter().println(html);
		return null;
	}
}
