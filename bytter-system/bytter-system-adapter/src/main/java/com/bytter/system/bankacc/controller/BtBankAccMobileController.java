//package com.bytter.system.bankacc.controller;
//
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.util.HashMap;
//import java.util.List;
//import javax.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import com.bytter.core.base.BaseController;
//import com.bytter.core.common.model.json.AjaxJson;
//import com.bytter.core.common.rest.RestInput;
//import com.bytter.core.common.rest.RestResponse;
//import com.bytter.core.constant.CommonConstant;
//import com.bytter.core.utils.StringUtils;
//import com.bytter.system.api.IAccountGrantService;
//import com.bytter.system.api.IBankTypeService;
//import com.bytter.system.api.IBisAccBalService;
//import com.bytter.system.api.IBisAccDtlService;
//import com.bytter.system.api.IBtCurrencyService;
//import com.bytter.system.api.ICorpService;
//import com.bytter.system.api.IDictService;
//import com.bytter.system.api.IUserService;
//import com.bytter.system.api.constant.AccountGrantConstant;
//import com.bytter.system.api.dto.BankTypeDTO;
//import com.bytter.system.api.dto.BisAccBalDTO;
//import com.bytter.system.api.dto.BtBankAccDTO;
//import com.bytter.system.api.dto.BtCurrencyDTO;
//import com.bytter.system.api.dto.CorpDTO;
//import com.bytter.system.api.dto.DictDTO;
//import com.bytter.system.api.dto.UserDTO;
//import com.bytter.system.constants.ResourceConstant;
//import com.bytter.system.service.BankAccMobieService;
//import com.bytter.system.service.BisAccAttributeService;
//import com.bytter.system.service.BisAccBalService;
//import com.bytter.system.service.BisAccDtlService;
//import com.bytter.system.service.BtBankAccCommonService;
//import com.bytter.system.service.BtBankAccService;
//import com.bytter.system.vo.BisAccAttributeVO;
//import com.bytter.system.vo.BisAccDtlVO;
//import com.bytter.system.vo.BtBankAccMobileVO;
//import com.bytter.system.vo.BtBankAccVO;
//
//
///**
// * @author onlineGenerator
// * @version 1.0
// * @Title: Controller
// * @Description: 银行账号app
// * @date 2017-06-29 13:51:01
// */
////TODO 属于BI
//@Controller
//@RequestMapping("/api/bankacc")
//public class BtBankAccMobileController extends BaseController {
//
//	@Autowired
//	private BankAccMobieService bankAccMobieService;
//	@Autowired
//	private BtBankAccService btBankAccService;
//	@Autowired(required=false)
//	private ICorpService corpServiceI;
//	@Autowired(required=false)
//	 private IBtCurrencyService btCurrencyServiceI;
//
//	@Autowired
//	private BisAccAttributeService bisAccAttributeService;
//	@Autowired(required=false)
//	private IDictService dictService;
//	@Autowired(required = false)
//	private BtBankAccCommonService btBankAccCommonService;
//	@Autowired
//	private BisAccBalService bisAccBalService;
//	@Autowired
//	private BisAccDtlService bisAccDtlService;
//	@Autowired(required = false)
//	private IBisAccDtlService iBisAccDtlService;
//	@Autowired(required = false)
//	private IBisAccBalService iBisAccBalService;
//	@Autowired(required = false)
//	private IBankTypeService bankTypeService;
//	@Autowired(required = false)
//	private IUserService userService;
//	@Autowired(required = false)
//	private IAccountGrantService accountGrantService;
//
//	/**
//	 * APP报表-结算管理
//	 *
//	 * @param model model
//	 * @return
//	 */
//	@RequestMapping("/appNetbankReportIndex")
//	public String appNetbankReportIndex(Model model) {
//		String userId = getCurrentUser().getId();
//		UserDTO userDTO = userService.getByUserId(userId);
//		CorpDTO corp = userDTO.getCorp();
//		model.addAttribute("corpId", corp.getId());
//		model.addAttribute("netId", corp.getNetId());
//		return "bankacc/appNetbankReportIndex";
//	}
//
//	/**
//	 * 银行账号查询
//	 */
//	@RequestMapping("/mobileBankAcc")
//	public String getbankAcc(Model model){
//		List<BisAccAttributeVO> listAttribute = bisAccAttributeService.getValidAttribute();//账户属性
//		List<DictDTO> list = dictService.list("btBankAcc.accType");
//		model.addAttribute("attribute", listAttribute);
//		model.addAttribute("bankAccType", list);
//		return "bankacc/mobileBankAcc";
//	}
//	/**
//	 * 银行账号查询数据列表
//	 */
//	@PostMapping("/getBankAccMoblieList")
//	@ResponseBody
//	public Object getBankAccMoblieList(BtBankAccVO vo){
//		List<BtBankAccVO> list = btBankAccService.getListByVO(vo,this.getCurrentUser());
//		List<BankTypeDTO> bankTypeDTOS = bankTypeService.listByValidSign(CommonConstant.VALID_SIGN_YES);
//		for(BtBankAccVO entity:list){
//			if(StringUtils.isBlank(entity.getBankTypeId())){
//				break;
//			}
//			for(BankTypeDTO dto:bankTypeDTOS){
//				if(entity.getBankTypeId().equals(dto.getId())){
//					entity.setIcon(dto.getIcon());
//					break;
//				}
//			}
//		}
//		return list;
//	}
//
//	/**
//	 * 银行账号查询数据列表 - 分页
//	 */
//	@RequestMapping("/getBankAccMobliePageList")
//	@ResponseBody
//	public Object getBankAccMobliePageList(@Param("pageNumber") Integer pageNumber,
//										   @Param("pageSize") Integer pageSize,
//										   BtBankAccVO vo) {
//		RestInput restInput = new RestInput();
//		restInput.put("pageNumber", pageNumber);
//		restInput.put("pageSize", pageSize);
//		RestResponse<BtBankAccVO> restResponse = btBankAccService.getPageListByVO(restInput, vo, getCurrentUser());
//		List<BtBankAccVO> list = restResponse.getData();
//		List<BankTypeDTO> bankTypeDTOS = bankTypeService.listByValidSign(CommonConstant.VALID_SIGN_YES);
//		for (BtBankAccVO entity : list) {
//			if (StringUtils.isBlank(entity.getBankTypeId())) {
//				break;
//			}
//			for (BankTypeDTO dto : bankTypeDTOS) {
//				if (entity.getBankTypeId().equals(dto.getId())) {
//					entity.setIcon(dto.getIcon());
//					break;
//				}
//			}
//		}
//		return list;
//	}
//
//	/**
//	 * 银行账号详情
//	 */
//	@RequestMapping("/mobileBankAccDetails")
//	public String mobileBankAccDetails(Model model,String id,String curId){
//		BtBankAccVO vo = btBankAccCommonService.getBtBankAccVOById(id,curId);
//		model.addAttribute("vo", vo);
//		return "bankacc/mobileBankAccDetails";
//	}
//
//	/**
//	 * 银行账号余额查询
//	 */
//	@RequestMapping("/mobileBankAccBal")
//	public String mobileBankAccBal(Model model,String id,String curId){
//		List<BtBankAccDTO> accList = accountGrantService.listDTOByMultiVal(this.getCurrentUser().getId(), ResourceConstant.NETBANK_BANKACC_BISACCBAL_QUERY, null, AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE, AccountGrantConstant.QUERY_BALANCE);
//		if(accList!=null && accList.size()>0){
//			HashMap<String, BtBankAccDTO> bankAccMap = new HashMap<>();
//			for(BtBankAccDTO bankAcc:accList){
//				bankAccMap.put(bankAcc.getId(), bankAcc);
//			}
//			if(bankAccMap.containsKey(id)){
//				BtBankAccVO bankAcc = btBankAccCommonService.getBtBankAccVOById(id,curId);
//				model.addAttribute("bankAcc", bankAcc);
//				BisAccBalDTO lastBisAccBalBy = iBisAccBalService.getLastBisAccBalBy(bankAcc.getBankAcc(), bankAcc.getCurId());
//				model.addAttribute("bal", lastBisAccBalBy);
//				return "bankacc/mobileBankAccBal";
//			}
//		}
//		model.addAttribute("bankAcc", null);
//		model.addAttribute("bal",null);
//		return "bankacc/mobileBankAccBal";
//	}
//
//	/**
//	 * 银行账号流水查询
//	 */
//	@RequestMapping("/mobileBankAccDtl")
//	public String mobileBankAccDtl(Model model,String id,String curId){
//		List<BtBankAccDTO> accList =  accountGrantService.listDTOByMultiVal(this.getCurrentUser().getId(), ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_QUERY, null, AccountGrantConstant.ONLINE_BANK_TYPE + CommonConstant.COMMA_SPLIT + AccountGrantConstant.OFFLINE_BANK_TYPE, AccountGrantConstant.QUERY_DETAIL);
//		if(accList!=null && accList.size()>0){
//			HashMap<String, BtBankAccDTO> bankAccMap = new HashMap<>();
//			for(BtBankAccDTO bankAcc:accList){
//				bankAccMap.put(bankAcc.getId(), bankAcc);
//			}
//			if(bankAccMap.containsKey(id)){
//				BtBankAccVO bankAcc = btBankAccCommonService.getBtBankAccVOById(id,curId);
//				List<BisAccDtlVO> list = bisAccDtlService.getListByBankAcc(bankAcc.getBankAcc(), bankAcc.getCurId(),"");
//				BigDecimal sumIncome = new BigDecimal(0);//收入总金额
//				BigDecimal sumPay = new BigDecimal(0);//支出总金额
//				for(BisAccDtlVO vo:list){
//					if(CommonConstant.YES.equals(vo.getCdSign())){
//						sumPay=sumPay.add(vo.getAmt());
//					}else{
//						sumIncome=sumIncome.add(vo.getAmt());
//					}
//				}
//				model.addAttribute("sumIncome",sumIncome);
//				model.addAttribute("sumPay",sumPay);
//				model.addAttribute("vo",bankAcc);
//				return "bankacc/mobileBankAccDtl";
//			}
//		}
//		model.addAttribute("vo",null);
//		return "bankacc/mobileBankAccDtl";
//	}
//
//	/**
//	 * 银行账号流水查询数据接口
//	 */
//	@PostMapping("/mobileBankAccDtlData")
//	@ResponseBody
//	public Object mobileBankAccDtlData(Model model, String id, String curId, ModelMap map,String remark){
//		BtBankAccVO bankAcc = btBankAccCommonService.getBtBankAccVOById(id,curId);
//		if(StringUtils.isBlank(remark)){
//			remark="";
//		}
//		List<BisAccDtlVO> listByBankAcc = bisAccDtlService.getListByBankAcc(bankAcc.getBankAcc(), bankAcc.getCurId(),remark);
//		return listByBankAcc;
//	}
//
//	/**
//	 * 银行账号流水查询数据接口 - 分页
//	 */
//	@PostMapping("/mobileBankAccDtlPaginationData")
//	@ResponseBody
//	public Object mobileBankAccDtlData(@Param("pageNumber") Integer pageNumber,
//									   @Param("pageSize") Integer pageSize,
//									   String id,
//									   String curId,
//									   String remark) {
//		BtBankAccVO bankAcc = btBankAccCommonService.getBtBankAccVOById(id, curId);
//		if (StringUtils.isBlank(remark)) {
//			remark = "";
//		}
//		remark = StringUtils.getUtf8String(remark);
//		RestInput restInput = new RestInput();
//		restInput.put("pageNumber", pageNumber);
//		restInput.put("pageSize", pageSize);
//		return bisAccDtlService.getPageListByBankAcc(restInput, bankAcc.getBankAcc(), bankAcc.getCurId(), remark);
//	}
//
//	/**
//	 * 银行账号流水查询
//	 */
//	@RequestMapping("/mobileBankAccDtlInfo")
//	public String mobileBankAccDtlInfo(Model model,String id,String curId,String flag){
//		BisAccDtlVO byUnion = bisAccDtlService.getByUnion(id);
//		model.addAttribute("dtl",byUnion);
//		model.addAttribute("chinese",StringUtils.chineseMoney(byUnion.getAmt()));
//		return "bankacc/mobileBankAccDtlInfo";
//	}
//
//	/**
//	 * BIS_ACC_BAL列表 页面跳转
//	 *
//	 * @return
//	 */
//	@PostMapping("/mobileBal")
//	public String getBal(Model model,String corpId,String beginDate,String endDate) {
//		List<CorpDTO> corpList = corpService.listUserCorp(ResourceConstant.NETBANK_BANKACC_BISACCBAL_QUERY,getUserId());
//		//获取有权限的银行账号
//		List<BtBankAccMobileVO> bankList = bankAccMobieService.getGrantBankAcc(getUserId(), corpId, "bal");
//		List<BtCurrencyDTO> curList = btCurrencyService.getBtCurrency();
//		model.addAttribute("corpList",corpList);
//		model.addAttribute("bankList", bankList);
//		model.addAttribute("userId", getUserId());
//		model.addAttribute("corpId", corpId);
//		model.addAttribute("beginDate", beginDate);
//		model.addAttribute("endDate", endDate);
//		model.addAttribute("curList", curList);
//		return "bankacc/mobileBal";
//	}
//	/**
//	 * BIS_ACC_BAL列表 页面跳转
//	 *
//	 * @return
//	 */
//	@GetMapping("/mobileBal2")
//	public String getBalget(Model model) {
//		List<CorpDTO> corpList = corpService.listUserCorp(ResourceConstant.NETBANK_BANKACC_BISACCBAL_QUERY,getUserId());
//		//获取有权限的银行账号
////		List<BtBankAccMobileVO> bankList = bankAccMobieService.getGrantBankAcc(getUserId(), corpId, "bal");
//		List<BtCurrencyDTO> curList = btCurrencyService.getBtCurrency();
//		model.addAttribute("corpList",corpList);
////		model.addAttribute("bankList", bankList);
//		model.addAttribute("userId", getUserId());
////		model.addAttribute("corpId", corpId);
////		model.addAttribute("beginDate", beginDate);
////		model.addAttribute("endDate", endDate);
//		model.addAttribute("curList", curList);
//		return "bankacc/mobileBal";
//	}
//
//	@PostMapping("/mobileBalList")
//	public AjaxJson getBalList(String corpId, String beginDate, String endDate, String bankAccId,String curId, HttpServletResponse response) throws IOException {
//		String html = bankAccMobieService.getBal(corpId,getUserId(), beginDate,endDate,bankAccId,curId);
//		response.setContentType("text/plain;charset=UTF-8");
//		response.getWriter().println(html);
//		return null;
//	}
//
//	/**
//	 * 银行账号明细表列表 页面跳转
//	 *
//	 * @return
//	 */
//	@PostMapping("/mobileDtl")
//	public String getDtl(Model model,String corpId) {
//		List<CorpDTO> corpList = corpService.listUserCorp(ResourceConstant.NETBANK_BANKACC_BTBANKACCDTL_MANAGERQUERY,getUserId());
//		//获取有权限的银行账号
//		List<BtBankAccMobileVO> bankList = bankAccMobieService.getGrantBankAcc(getUserId(), corpId, "dtl");
//		List<BtCurrencyDTO> curList = btCurrencyService.getBtCurrency();
//		model.addAttribute("corpList",corpList);
//		model.addAttribute("bankList", bankList);
//		model.addAttribute("userId", getUserId());
//		model.addAttribute("corpId", corpId);
//		model.addAttribute("curList", curList);
//		return "bankacc/mobileDtl";
//	}
//
//	@PostMapping("/mobileDtlList")
//	public AjaxJson getDtlList(String corpId, String beginDate, String endDate, String bankAcc,String beginMoney,String endMoney,String oppBankAcc,String curId,HttpServletResponse response) throws IOException {
//		String html = bankAccMobieService.getDtl(corpId,getUserId(),beginDate, endDate,bankAcc,beginMoney,endMoney,oppBankAcc,curId);
//		response.setContentType("text/plain;charset=UTF-8");
//		response.getWriter().println(html);
//		return null;
//	}
//}
