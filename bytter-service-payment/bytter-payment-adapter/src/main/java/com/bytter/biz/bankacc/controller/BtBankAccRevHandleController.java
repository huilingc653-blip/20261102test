package com.bytter.biz.bankacc.controller;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.inneraccount.entity.BtBankAccVirtual;
import com.bytter.biz.inneraccount.service.BtBankAccHandleService;
import com.bytter.biz.inneraccount.service.BtBankAccRevHandleService;
import com.bytter.biz.inneraccount.service.BtBankAccVirtualService;
import com.bytter.biz.bdlink.api.constant.BtBankAccMessageConstant;
import com.bytter.biz.netbank.entity.BtBankAccUse;
import com.bytter.biz.netbank.entity.FcBankAccGroup;
import com.bytter.biz.netbank.entity.FcReceiptType;
import com.bytter.biz.netbank.service.BtBankAccUseService;
import com.bytter.biz.netbank.service.FcBankAccGroupService;
import com.bytter.biz.netbank.service.FcReceiptTypeService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.BtBankAccAppDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.vo.BtBankAccAppVO;
import com.bytter.system.api.vo.BtBankAccVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 银行账号变更办理
 * @date 2017-05-23 17:37:39
 */
@Controller
@RequestMapping("/bankacc/btbankaccrevhandle")
public class BtBankAccRevHandleController extends BaseController {
	@DubboReference(check = false)
	private IBtBankAccAppService btBankAccAppService;
	@Autowired
	private BtBankAccRevHandleService btBankAccRevHandleService;
    @DubboReference(check = false)
	private IBtBankAccCommonService btBankAccCommonService;
    @DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	@Autowired
	private BtBankAccHandleService btBankAccHandleService;
    @DubboReference(check = false)
	private IBankTypeService bankTypeService;
	@Autowired
	private BtBankAccVirtualService btBankAccVirtualService;
	@Autowired
	private FcBankAccGroupService fcBankAccGroupService;
	@Autowired
	private FcReceiptTypeService fcReceiptTypeService;
	@Autowired
	private BtBankAccUseService bankAccUseService;
	@DubboReference(check = false)
	private IBtBankAccService btBankAccService;
	/**
	 * 银行账号申请表列表 页面跳转
	 *
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCREVHANDLE_QUERY)
	@LogMessage(content = "查询银行账号申请表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
		return "bankacc/listBtBankAccRevHandle";
	}
	/**
	 * 银行账号申请表 table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCREVHANDLE_QUERY)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return btBankAccRevHandleService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
	}
	/**
	 * 银行账号申请表编辑页面跳转
	 *
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCREVHANDLE_APPLY)
	@GetMapping("/update")
	public String update(BtBankAccAppVO vo, Model model) {
		if (StringUtils.isNotEmpty(vo.getId())) {
			BtBankAccDTO btBankAcc = btBankAccCommonService.getBtBankAccByBankAcc(vo.getBankAcc());
			String accId = btBankAcc.getId();
			String curId = btBankAccCommonService.setCurId(vo.getId(), "app");
			BtBankAccAppDTO app = btBankAccAppService.getById(vo.getId());
			String appId = app.getId();
			BeanUtils.copyNotNullProperties(app, btBankAcc);
			btBankAcc.setId(accId);
			model.addAttribute("curId", curId);
			model.addAttribute("appId", appId);
			//预留印鉴处理begin
			BtBankAccVO accVo = new BtBankAccVO();
			String sealDirectory = btBankAcc.getSealDirectory();
			String[] sealDirectoryList = new String[3];
			if(StringUtils.isNotBlank(sealDirectory)){
				String[] split = sealDirectory.split(CommonConstant.COMMA_SPLIT);
				System.arraycopy(split, 0, sealDirectoryList, 0, split.length);
			}
			accVo.setSealDirectoryList(sealDirectoryList);
			//查询所有账户组
			List<FcBankAccGroup> validPayType = fcBankAccGroupService.getValidPayType();
			HashMap<String, String> map = new HashMap<>();
			for (FcBankAccGroup fcBankAccGroup : validPayType) {
				map.put(fcBankAccGroup.getAccGroupCode(),fcBankAccGroup.getAccGroupName());
			}
			//查询账户组名称
			String fcAccGroupCode = btBankAcc.getFcAccGroupCode();
			if (EmptyUtils.isNotEmpty(fcAccGroupCode)){
				List<String> codes = Arrays.asList(fcAccGroupCode.split(","));
				StringBuilder accGroupName = new StringBuilder();
				for(int i = 0;i<=codes.size()-1;i++){
					if (i==codes.size()-1){
						if (map.containsKey(codes.get(i))){
							accGroupName.append(map.get(codes.get(i)));
						}
					}else{
						if (map.containsKey(codes.get(i))){
							accGroupName.append(map.get(codes.get(i))+",");
						}
					}
				}
				btBankAcc.setFcAccGroupName(accGroupName.toString());
			}
			model.addAttribute("vo", accVo);
			//预留印鉴处理end
			model.addAttribute("btBankAccPage", btBankAcc);
			model.addAttribute("todate", DateUtils.formatDate(DateUtils.getDate()));
			//是否允许变更为保证金户
			String isAllowedChangeDeposit = "1";
			//获取本位币
			BtCurrencyDTO natualCur = btCurrencyService.getNatualCurrency();
			//账号币别为多币别时，都不允许变更为保证金
			if(StringUtils.isNotEmpty(curId) && (curId.length()>32)){
				isAllowedChangeDeposit = "0";
			}
			model.addAttribute("isAllowedChangeDeposit", isAllowedChangeDeposit);
			//银行类别对应的境内外标识
			BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(app.getBankTypeId());
			model.addAttribute("isDomesticBankType", bankTypeDTO==null ? "":bankTypeDTO.getIsDomesticBank());
			//招行 虚拟户 线上办理
			int dataListSize = 0;
			if (BtBankAccConstant.ACCTYPE_09.equals(btBankAcc.getAccType()) && CommonConstant.YES.equals(btBankAcc.getIsOnlineHandle())
					&& BtBankAccConstant.BIS_BIF_CODE_CMB.equals(btBankAcc.getBifCode())) {
				List<BtBankAccVirtual> listVirtual = btBankAccVirtualService.getListByBankAccId(btBankAcc.getId());
				model.addAttribute("listVirtual", listVirtual);
				dataListSize = listVirtual.size();
			}
			model.addAttribute("dataListSize", dataListSize);
		}
		return "bankacc/updateBtBankAccRevHandle";
	}
	/**
	 * 银行账号申请表详情页
	 *
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCREVHANDLE_QUERY)
	@LogMessage(content = "查询银行账号申请表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		BtBankAccDTO vo = btBankAccCommonService.getBtBankAccVOById(id);
		//查询所有账户组
		List<FcBankAccGroup> validPayType = fcBankAccGroupService.getValidPayType();
		HashMap<String, String> map = new HashMap<>();
		for (FcBankAccGroup fcBankAccGroup : validPayType) {
			map.put(fcBankAccGroup.getAccGroupCode(),fcBankAccGroup.getAccGroupName());
		}
		//查询账户组名称
		String fcAccGroupCode = vo.getFcAccGroupCode();
		if (EmptyUtils.isNotEmpty(fcAccGroupCode)){
			List<String> codes = Arrays.asList(fcAccGroupCode.split(","));
			StringBuilder accGroupName = new StringBuilder();
			for(int i = 0;i<=codes.size()-1;i++){
				if (i==codes.size()-1){
					if (map.containsKey(codes.get(i))){
						accGroupName.append(map.get(codes.get(i)));
					}
				}else{
					if (map.containsKey(codes.get(i))){
						accGroupName.append(map.get(codes.get(i))+",");
					}
				}
			}
			vo.setFcAccGroupName(accGroupName.toString());
		}
		//获取所有的单据类型
		List<FcReceiptType> validFcReceiptType = fcReceiptTypeService.getValidFcReceiptType();
		HashMap<String, String> fcReceiptMap = new HashMap<>();
		for (FcReceiptType fcReceiptType : validFcReceiptType) {
			fcReceiptMap.put(fcReceiptType.getReceiptCode(),fcReceiptType.getReceiptName());
		}
		if (EmptyUtils.isNotEmpty(vo.getReceiptCode())){
			List<String> receiptCodeList = Arrays.asList(vo.getReceiptCode().split(","));
			StringBuilder fcReceiptType = new StringBuilder();
			for(int i = 0;i<=receiptCodeList.size()-1;i++){
				if (i==receiptCodeList.size()-1){
					if (fcReceiptMap.containsKey(receiptCodeList.get(i))){
						fcReceiptType.append(fcReceiptMap.get(receiptCodeList.get(i)));
					}
				}else{
					if (fcReceiptMap.containsKey(receiptCodeList.get(i))){
						fcReceiptType.append(fcReceiptMap.get(receiptCodeList.get(i))+",");
					}
				}
			}
			if(ObjectUtil.isNotEmpty(fcReceiptType)) vo.setReceiptName(fcReceiptType.toString());
		}
		model.addAttribute("btBankAccAppPage", vo);
		return "bankacc/viewBtBankAccRev";
	}
	/**
	 * 修改银行账号申请表
	 *
	 * @param vo
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCREVHANDLE_APPLY)
	@LogMessage(content = "修改银行账号申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(BtBankAccVO vo) {
		if (BtBankAccConstant.ACCTYPE_02.equals(vo.getAccType())) {
			vo.setRegularDate(BudgetConstant.getRegularsDate(vo.getRegulars()));
		}
		btBankAccHandleService.insertField(vo);//用于银行预留印鉴的id拼接
		btBankAccRevHandleService.toModify(vo, this.getCurrentUser());
		return success();
	}
	/**
	 * 删除银行账号申请表
	 *
	 * @param id
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCREVHANDLE_APPLY)
	@LogMessage(content = "删除银行账号申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id) {
		this.btBankAccRevHandleService.back(id, this.getCurrentUser());
		return success();
	}
	/**
	 * 跳转至审核列表
	 *
	 * @return String
	 */
	@GetMapping("/listApprove")
	public String listApprove( String flowStatus, String cycleTime, Model model) {
		model.addAttribute("flowStatus", flowStatus);
		model.addAttribute("cycleTime", cycleTime);
		return "bankacc/listBtBankAccHandleRevApprove";
	}
	/**
	 * 加载审核列表数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/dataTableApprove")
	@ResponseBody
	public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
		return this.btBankAccRevHandleService.dataTableApprove(dataTablesInput);
	}
	/**
	 * 跳转至审核详情界面
	 *
	 * @param id
	 * @param taskId
	 * @param model
	 * @return String
	 */
	@GetMapping("/approve")
	public String approve(String id, String taskId, Model model) {
		if (StringUtils.isNotEmpty(id)) {
            BtBankAccAppDTO app = btBankAccCommonService.getBtBankAccApp(id);
			BtBankAccDTO btBankAcc = btBankAccCommonService.getBtBankAccByBankAcc(app.getBankAcc());
			BtBankAccDTO vo = btBankAccCommonService.getBtBankAccVOById(btBankAcc.getId());
            if (EmptyUtils.isEmpty(vo.getIsOnline())) {
				vo.setIsOnline(BtBankAccConstant.ISONLINE_1);
			}
			if (EmptyUtils.isEmpty(vo.getIsCapitalPool())) {
				vo.setIsCapitalPool(BtBankAccConstant.ISCAPITALPOOL_0);
			}
			if (EmptyUtils.isEmpty(vo.getValidSign())) {
				vo.setValidSign(BtBankAccConstant.VALIDSIGN_1);
			}
			// 账号用途
			if (ObjectUtil.isNotEmpty(vo.getAccUseCode())){
				String accUseName = "";
				String[] strings = vo.getAccUseCode().split(",");
				List<BtBankAccUse> btBankAccUses = bankAccUseService.listAll();
				for (String code : strings){
					for (BtBankAccUse use : btBankAccUses){
						if (code.equals(use.getUseCode())){
							accUseName += use.getUseName()+",";
							break;
						}
					}
				}
				if (ObjectUtil.isNotEmpty(accUseName)) accUseName = accUseName.substring(0,accUseName.length()-1);
				vo.setAccUseName(accUseName);
			}
			/*if (BtBankAccConstant.ISONLINE_0.equals(btBankAcc.getIsOnline())) {
				if (EmptyUtils.isEmpty(btBankAcc.getInitBalance())) {
					btBankAcc.setInitBalance(BigDecimal.valueOf(0.00));
				}
				if (EmptyUtils.isEmpty(btBankAcc.getInitBalanceDate())) {
					btBankAcc.setInitBalanceDate(new Date());
				}
			}else {
				btBankAcc.setInitBalance(null);
				btBankAcc.setInitBalanceDate(null);
			}*/
			model.addAttribute("btBankAcc",btBankAcc);
			//预留印鉴处理begin
			String sealDirectory = vo.getSealDirectory();
			String[] sealDirectoryList = new String[3];
			if(StringUtils.isNotBlank(sealDirectory)){
				String[] split = sealDirectory.split(CommonConstant.COMMA_SPLIT);
				System.arraycopy(split, 0, sealDirectoryList, 0, split.length);
			}
			vo.setSealDirectoryList(sealDirectoryList);
			if (BtBankAccConstant.ACCTYPE_09.equals(vo.getAccType()) && StringUtils.isNotEmpty(vo.getEntityAccId())){
				BtBankAccDTO bankAccById = btBankAccService.getBankAccById(vo.getEntityAccId());
				if(bankAccById != null){
					vo.setEntityAcc(bankAccById.getBankAcc());
				}
			}
			//预留印鉴处理end
			model.addAttribute("btBankAccPage", vo);
			model.addAttribute("app", app);
			model.addAttribute("busType", BtBankAccMessageConstant.BUS_TYPE_REVHANDLE);
			model.addAttribute("taskId", taskId);
		}

		return "bankacc/approveBtBankAccRevHandle";
	}
	/**
	 * 跳转至手机审核详情界面
	 *
	 * @param businessId
	 * @param taskId
	 * @param model
	 * @return String
	 */
	@GetMapping("/mobileApprove")
	public String mobileApprove(String businessId, String taskId, Model model) {
		if (StringUtils.isNotEmpty(businessId)) {
            BtBankAccAppDTO app = btBankAccCommonService.getBtBankAccApp(businessId);
			BtBankAccDTO acc = btBankAccCommonService.getBtBankAccByBankAcc(app.getBankAcc());
			BtBankAccDTO btBankAcc = btBankAccCommonService.getBtBankAccVOById(acc.getId());
			model.addAttribute("btBankAcc", btBankAcc);
			model.addAttribute("app", app);
			model.addAttribute("taskId", taskId);
		}
		return "bankacc/viewApproveBtBankAccRevHandle";
	}
}
