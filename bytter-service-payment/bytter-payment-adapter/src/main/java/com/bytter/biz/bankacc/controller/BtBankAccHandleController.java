package com.bytter.biz.bankacc.controller;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.inneraccount.entity.BtBankAccVirtual;
import com.bytter.biz.inneraccount.service.BtBankAccHandleService;
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
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.CheckMessageEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.BtBankAccAppDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.vo.BtBankAccVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: BT_BANK_ACC
 * @date 2017-06-02 17:50:16
 */
@Controller
@RequestMapping("/bankacc/btbankacchandle")
public class BtBankAccHandleController extends BaseController {
	@Autowired
	private BtBankAccHandleService btBankAccHandleService;
	@DubboReference(check = false)
	private IBtBankAccCommonService btBankAccCommonService;
	@SuppressWarnings("unused")
	@Resource
    private IMultiLangService multiLangServiceI;
	@DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
	@Autowired(required=false)
	private IDictService dictService;
	@DubboReference(check = false)
	private IBtBankAccService btBankAccService;
	@DubboReference(check = false)
	private IBankTypeService bankTypeService;
	@Autowired
	private BtBankAccVirtualService btBankAccVirtualService;
	@DubboReference(check = false)
	private IBtBankAccAppService btBankAccAppService;
	@Autowired
	private FcBankAccGroupService fcBankAccGroupService;
	@Autowired
	private FcReceiptTypeService fcReceiptTypeService;

	@Autowired
	private BtBankAccUseService bankAccUseService;
	/**
	 * BT_BANK_ACC列表 页面跳转
	 *
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACC_QUERY)
	@LogMessage(content = "查询BT_BANK_ACC列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
		return "bankacc/listBtBankAccHandle";
	}
	/**
	 * BT_BANK_ACC table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACC_QUERY)
	@PostMapping("/datatable")
	@ResponseBody
	public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
		return btBankAccHandleService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
	}
	/**
	 * BT_BANK_ACC新增页面跳转
	 *
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACC_CREATE)
	@GetMapping("/add")
	public String add(BtBankAccDTO btBankAcc, Model model) {
		if (StringUtils.isNotEmpty(btBankAcc.getId())) {
			btBankAcc = btBankAccCommonService.getBtBankAccVOById(btBankAcc.getId());
			model.addAttribute("btBankAccPage", btBankAcc);
			model.addAttribute("todate", DateUtils.formatDate(DateUtils.getDate()));
		} else {
			model.addAttribute("todate", DateUtils.formatDate(DateUtils.getDate()));

		}
		//印鉴复选框
		return "bankacc/addBtBankAccHandle";
	}
	/**
	 * BT_BANK_ACC编辑页面跳转
	 *
	 * @return
	 */
	////@RequiresPermissions(value={ResourceConstant.NETBANK_BANKACC_BTBANKACC_UPDATE,
//			ResourceConstant.NETBANK_BANKACC_BTBANKACC_APPLY},
//			logical= Logical.OR)
	@GetMapping("/update")
	public String update(BtBankAccVO btBankAccVO, Model model) {
		int dataListSize = 0;
		// 银行类型是否8500
		String bankTypeIs8500 = "0";
		if ("1".equals(btBankAccVO.getIsFrom())) {
			if (StringUtils.isNotEmpty(btBankAccVO.getId())) {
				BtBankAccDTO btBankAcc = btBankAccHandleService.getBtBankAccVOById(btBankAccVO.getId());
				String curId = this.btBankAccCommonService.setCurId(btBankAcc.getId(), BtBankAccConstant.FORM_ACC);
				if (EmptyUtils.isEmpty(btBankAcc.getIsOnline())) {
					btBankAcc.setIsOnline(BtBankAccConstant.ISONLINE_1);
				}
				if (EmptyUtils.isEmpty(btBankAcc.getIsCapitalPool())) {
					btBankAcc.setIsCapitalPool(BtBankAccConstant.ISCAPITALPOOL_0);
				}
				if (EmptyUtils.isEmpty(btBankAcc.getValidSign())) {
					btBankAcc.setValidSign(BtBankAccConstant.VALIDSIGN_1);
				}
				if (EmptyUtils.isEmpty(btBankAcc.getIsOffshoreAccount())) {
					btBankAcc.setIsOffshoreAccount(BtBankAccConstant.NO);
				}
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

				model.addAttribute("corpName", btBankAcc.getCorpName());
				model.addAttribute("curId", curId);
				model.addAttribute("btBankAccPage", btBankAcc);
				model.addAttribute("electricBill", btBankAcc.getElectricBill());
				//银行类别对应的境内外标识
				BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(btBankAcc.getBankTypeId());
				model.addAttribute("isDomesticBankType", bankTypeDTO==null ? "":bankTypeDTO.getIsDomesticBank());
				String isThirdParty = "";
				String bindingBankAcc = "";
				if(!EmptyUtils.isEmpty(bankTypeDTO) && EmptyUtils.isNotEmpty(bankTypeDTO.getIsThirdParty())){
					isThirdParty = bankTypeDTO.getIsThirdParty();
				}
//				if(isThirdParty.equals("1")){
//					BtBankAccDTO bindingBankAccDto =  btBankAccService.getBankAccById(btBankAcc.getBindingBankAcc());
//					if(!EmptyUtils.isEmpty(bindingBankAccDto)){
//						bindingBankAcc = bindingBankAccDto.getBankAcc();
//					}
//				}
				model.addAttribute("isThirdParty",isThirdParty);
//				model.addAttribute("bindingBankAcc",bindingBankAcc);
				if (!EmptyUtils.isEmpty(bankTypeDTO) && "8500".equals(bankTypeDTO.getBankType())){
					bankTypeIs8500 = "1";
				}
				//预留印鉴处理begin
				String sealDirectory = btBankAcc.getSealDirectory();
				String[] sealDirectoryList = new String[3];
				if(StringUtils.isNotBlank(sealDirectory)){
					String[] split = sealDirectory.split(CommonConstant.COMMA_SPLIT);
					System.arraycopy(split, 0, sealDirectoryList, 0, split.length);
				}
				btBankAcc.setSealDirectoryList(sealDirectoryList);
				model.addAttribute("vo", btBankAcc);
				//预留印鉴处理end
				model.addAttribute("todate", DateUtils.formatDate(DateUtils.getDate()));
				model.addAttribute("isFrom", btBankAccVO.getIsFrom());
				//是否允许变更为保证金户
				String isAllowedChangeDeposit = "1";
				//获取本位币
				BtCurrencyDTO natualCur = btCurrencyService.getNatualCurrency();
				//账号币别为多币别时，都不允许变更为保证金
				if(StringUtils.isNotEmpty(curId) && (curId.length()>32)){
					isAllowedChangeDeposit = "0";
				}
				model.addAttribute("isAllowedChangeDeposit", isAllowedChangeDeposit);
				//招行 虚拟户 线上办理
				if (BtBankAccConstant.ACCTYPE_09.equals(btBankAcc.getAccType()) && CommonConstant.YES.equals(btBankAcc.getIsOnlineHandle())
						&& BtBankAccConstant.BIS_BIF_CODE_CMB.equals(btBankAcc.getBifCode())) {
					List<BtBankAccVirtual> listVirtual = btBankAccVirtualService.getListByBankAccId(btBankAcc.getId());
					model.addAttribute("listVirtual", listVirtual);
					dataListSize = listVirtual.size();
				}
			}
		} else {
			if (StringUtils.isNotEmpty(btBankAccVO.getId())) {
				BtBankAccAppDTO btBankAccApp = btBankAccAppService.getBankAccApp(btBankAccVO.getId());
				String curId = this.btBankAccCommonService.setCurId(btBankAccApp.getId(), BtBankAccConstant.FORM_APP);
				Date date = DateUtils.getDate();
				BtBankAccDTO btBankAcc = new BtBankAccDTO();
				BeanUtils.copyNotNullProperties(btBankAccApp, btBankAcc);
				if (EmptyUtils.isEmpty(btBankAcc.getIsOnline())) {
					btBankAcc.setIsOnline(BtBankAccConstant.ISONLINE_1);
				}
				if (EmptyUtils.isEmpty(btBankAcc.getIsCapitalPool())) {
					btBankAcc.setIsCapitalPool(BtBankAccConstant.ISCAPITALPOOL_0);
				}
				if (EmptyUtils.isEmpty(btBankAcc.getValidSign())) {
					btBankAcc.setValidSign(BtBankAccConstant.VALIDSIGN_1);
				}
				if (EmptyUtils.isEmpty(btBankAcc.getIsOffshoreAccount())) {
					btBankAcc.setIsOffshoreAccount(BtBankAccConstant.NO);
				}
//				if (EmptyUtils.isEmpty(btBankAcc.getRates())) {
//					btBankAcc.setRates(new BigDecimal("0.0035"));
//				}
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
				btBankAcc.setRatesFloat("0");
				btBankAcc.setInterestCycle(-1);
				btBankAcc.setInterestDate("");
				btBankAcc.setIsOnlineHandle(CommonConstant.NO);
				model.addAttribute("curId", btBankAccApp.getCurId());
				model.addAttribute("curName", btBankAccApp.getCurName());
				model.addAttribute("corpName", btBankAccApp.getCorpName());
				model.addAttribute("btBankAccPage", btBankAcc);
				model.addAttribute("todate", DateUtils.formatDate(date));
				model.addAttribute("isFrom", btBankAccVO.getIsFrom());
				//银行类别对应的境内外标识
				BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(btBankAccApp.getBankTypeId());
				model.addAttribute("isDomesticBankType", bankTypeDTO==null ? "":bankTypeDTO.getIsDomesticBank());
				String isThirdParty = "";
				String bindingBankAcc = "";
				if(!EmptyUtils.isEmpty(bankTypeDTO) && EmptyUtils.isNotEmpty(bankTypeDTO.getIsThirdParty())){
					isThirdParty = bankTypeDTO.getIsThirdParty();
				}
//				if(isThirdParty.equals("1")){
//					BtBankAccDTO bindingBankAccDto = btBankAccService.getBankAccById(btBankAcc.getBindingBankAcc());
//					if(!EmptyUtils.isEmpty(bindingBankAccDto)){
//						bindingBankAcc = bindingBankAccDto.getBankAcc();
//					}
//				}
				model.addAttribute("isThirdParty",isThirdParty);
//				model.addAttribute("bindingBankAcc",bindingBankAcc);
				if (!EmptyUtils.isEmpty(bankTypeDTO) && "8500".equals(bankTypeDTO.getBankType())){
					bankTypeIs8500 = "1";
				}
				//预留印鉴处理begin
				BtBankAccVO accVo = new BtBankAccVO();
				accVo.setSealDirectoryList(new String[3]);
				model.addAttribute("vo", accVo);
				//预留印鉴处理end
				//是否允许变更为保证金户
				String isAllowedChangeDeposit = "1";
				//获取本位币
				BtCurrencyDTO natualCur = btCurrencyService.getNatualCurrency();
				//账号币别为多币别时，都不允许变更为保证金
				if(StringUtils.isNotEmpty(curId) && (curId.length()>32)){
					isAllowedChangeDeposit = "0";
				}
				model.addAttribute("isAllowedChangeDeposit", isAllowedChangeDeposit);
			}
		}
		model.addAttribute("dataListSize", dataListSize);
		model.addAttribute("bankTypeIs8500",bankTypeIs8500);
		if(StringUtils.isNotEmpty(btBankAccVO.getFlag()) && btBankAccVO.getFlag().equals("kh"))
		{
			return "bankacc/updateBtBankAccHandleOpenhu";//开户办理，银行账号不能编辑BUG
		}
		return "bankacc/updateBtBankAccHandle";
	}
	/**
	 * BT_BANK_ACC详情页
	 *
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACC_QUERY)
	@LogMessage(content = "查询BT_BANK_ACC详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		BtBankAccDTO btBankAcc =  btBankAccHandleService.getBtBankAccVOById(id);
		if (EmptyUtils.isEmpty(btBankAcc.getIsOnline())) {
			btBankAcc.setIsOnline(BtBankAccConstant.ISONLINE_1);
		}
		if (EmptyUtils.isEmpty(btBankAcc.getIsCapitalPool())) {
			btBankAcc.setIsCapitalPool(BtBankAccConstant.ISCAPITALPOOL_0);
		}
		if (EmptyUtils.isEmpty(btBankAcc.getValidSign())) {
			btBankAcc.setValidSign(BtBankAccConstant.VALIDSIGN_1);
		}
		// 账户用途
		if (ObjectUtil.isNotEmpty(btBankAcc.getAccUseCode())){
			String accUseName = "";
			String[] strings = btBankAcc.getAccUseCode().split(",");
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
			btBankAcc.setAccUseName(accUseName);
		}
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
		//获取所有的单据类型
		List<FcReceiptType> validFcReceiptType = fcReceiptTypeService.getValidFcReceiptType();
		HashMap<String, String> fcReceiptMap = new HashMap<>();
		for (FcReceiptType fcReceiptType : validFcReceiptType) {
			fcReceiptMap.put(fcReceiptType.getReceiptCode(),fcReceiptType.getReceiptName());
		}
		if (EmptyUtils.isNotEmpty(btBankAcc.getReceiptCode())){
			List<String> receiptCodeList = Arrays.asList(btBankAcc.getReceiptCode().split(","));
			StringBuilder fcReceiptTypeStr = new StringBuilder();
			for(int i = 0;i<=receiptCodeList.size()-1;i++){
				if (i==receiptCodeList.size()-1){
					if (fcReceiptMap.containsKey(receiptCodeList.get(i))){
						fcReceiptTypeStr.append(fcReceiptMap.get(receiptCodeList.get(i)));
					}
				}else{
					if (fcReceiptMap.containsKey(receiptCodeList.get(i))){
						fcReceiptTypeStr.append(fcReceiptMap.get(receiptCodeList.get(i))+",");
					}
				}
			}
			if(ObjectUtil.isNotEmpty(fcReceiptTypeStr)) btBankAcc.setReceiptName(fcReceiptTypeStr.toString());
		}
		//银行类别对应的境内外标识
		BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(btBankAcc.getBankTypeId());
		btBankAcc.setBankTypeName(bankTypeDTO.getTypeName());
		String isThirdParty = "";
		String bindingBankAcc = "";
		if(!EmptyUtils.isEmpty(bankTypeDTO) && EmptyUtils.isNotEmpty(bankTypeDTO.getIsThirdParty())){
			isThirdParty = bankTypeDTO.getIsThirdParty();
		}
		if(isThirdParty.equals("1")){
			BtBankAccDTO bindingBankAccDto =  btBankAccService.getBankAccById(btBankAcc.getBindingBankAcc());
			if(!EmptyUtils.isEmpty(bindingBankAccDto)){
				bindingBankAcc = bindingBankAccDto.getBankAcc();
			}
		}
		model.addAttribute("isThirdParty",isThirdParty);
		model.addAttribute("bindingBankAcc",bindingBankAcc);
		model.addAttribute("btBankAcc",btBankAcc);
		//预留印鉴处理begin
		String sealDirectory = btBankAcc.getSealDirectory();
		String[] sealDirectoryList = new String[3];
		if(StringUtils.isNotBlank(sealDirectory)){
			String[] split = sealDirectory.split(CommonConstant.COMMA_SPLIT);
			System.arraycopy(split, 0, sealDirectoryList, 0, split.length);
		}
		btBankAcc.setSealDirectoryList(sealDirectoryList);
		//预留印鉴处理end
		model.addAttribute("btBankAccPage", btBankAcc);
		model.addAttribute("busType", BtBankAccMessageConstant.BUS_TYPE_APPHANDLE);
		Boolean isShowUKeyOther = false;
		if(EmptyUtils.isNotEmpty(btBankAcc.getuKeyFunction()) &&  btBankAcc.getuKeyFunction().contains("9")){
			isShowUKeyOther = true;
		}
		model.addAttribute("isShowUKeyOther", isShowUKeyOther);
		//招行 虚拟户 线上办理
		if (BtBankAccConstant.ACCTYPE_09.equals(btBankAcc.getAccType()) && CommonConstant.YES.equals(btBankAcc.getIsOnlineHandle())
				&& BtBankAccConstant.BIS_BIF_CODE_CMB.equals(btBankAcc.getBifCode())) {
			List<BtBankAccVirtual> listVirtual = btBankAccVirtualService.getListByBankAccId(btBankAcc.getId());
			model.addAttribute("listVirtual", listVirtual);
		}
		return "bankacc/viewBtBankAccHandle";
	}
	/**
	 * 添加BT_BANK_ACC
	 *
	 * @param btBankAccVO
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACC_APPLY)
	@LogMessage(content = "添加BT_BANK_ACC", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(BtBankAccVO btBankAccVO, String status) {
		btBankAccHandleService.insertField(btBankAccVO);//用于银行预留印鉴的id拼接
		btBankAccHandleService.toSave(btBankAccVO, this.getCurrentUser(), status);
		return success();
	}
	/**
	 * 修改BT_BANK_ACC
	 *
	 * @param btBankAccVO
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACC_APPLY)
	@LogMessage(content = "修改BT_BANK_ACC", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(BtBankAccVO btBankAccVO) {
		btBankAccHandleService.insertField(btBankAccVO);//用于银行预留印鉴的id拼接
		btBankAccHandleService.toModify(btBankAccVO, this.getCurrentUser());
		return success();
	}
	/**
	 * 删除BT_BANK_ACC
	 *
	 * @param id
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACC_APPLY)
	@LogMessage(content = "打回BT_BANK_ACC_APP", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id) {
		this.btBankAccHandleService.deleteBtBankAcc(id,this.getCurrentUser());
		return success();
	}
	/**
	 * 批量删除BT_BANK_ACC
	 *
	 * @param ids
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACC_APPLY)
	@LogMessage(content = "批量删除BT_BANK_ACC", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/batchDelete")
	@ResponseBody
	public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
		/*for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
			BtBankAcc btBankAcc = btBankAccHandleService.get(id);
			btBankAccHandleService.deleteEntity(btBankAcc);
		}*/
		return success();
	}
	/**
	 * 保存导入数据
	 *
	 * @param list
	 * @return
	 */
	@PostMapping("/saveImport")
	@ResponseBody
	public AjaxJson saveImport(@RequestBody List<BtBankAccVO> list) {
		this.btBankAccHandleService.saveImport(list, this.getCurrentUser());
		return success();
	}
	/**
	 * 根据银行接口代码得到银行账号，用与银企直联
	 * BT_BANK_ACC table列表
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/getDataTableByBifCode")
	@ResponseBody
	public DataTablesOutput getDataTableByBifCode(@RequestBody DataTablesInput dataTablesInput) {
		return btBankAccHandleService.getDataTableByBifCode(dataTablesInput, this.getCurrentUser());
	}
	/**
	 * 判断账号是否有父子关系
	 *
	 * @param id
	 * @return
	 */
	@PostMapping("/isHasPrantAndChild")
	@ResponseBody
	public AjaxJson isHasPrantAndChild(String id) {
		AjaxJson j = new AjaxJson();
		j.setSuccess(btBankAccHandleService.isHasPrantAndChild(id));
		return j;
	}
	/**
	* 银行多选组件 begin
	* author: qinl
	* date: 2020-04-16
	*/
	@GetMapping("/mulSelectUserPage")
	public String mulSelectUserPage(BtBankAccVO bankAcc, Model model) {
		model.addAttribute("bankAcc", bankAcc);
		return "bankacc/mulSelectUserPage";
	}
	@GetMapping("/mulViewUserPage")
	public String mulViewUserPage(BtBankAccVO bankAcc, Model model) {
		if(StringUtils.isBlank(bankAcc.getId())){
			throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTRALIYDATAMANAGER_BANKACC_EMPTY);
		}
		DataTablesInput dataTablesInput = new DataTablesInput();
		Map<String,Object> map = new HashMap<>();
		map.put("ins_id",bankAcc.getId());
		dataTablesInput.setFormdata(map);
		DataTablesOutput dataTablesUnion = btBankAccHandleService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
		model.addAttribute("infoList", dataTablesUnion.getData());
		return "bankacc/mulViewUserPage";
	}
	/*** 银行多选组件 end*/
	/**
	 * 批量设置票据账户
	 * @param  accId 单据id
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACC_APPLY)
	@GetMapping("/toSetBillInfo/{accId}")
	public String toSetPayerAcc(@PathVariable("accId") String accId, Model model) {
		if(!EmptyUtils.isEmpty(accId)){
			if(!EmptyUtils.isEmpty(accId.split(CommonConstant.COMMA_SPLIT)) && accId.split(CommonConstant.COMMA_SPLIT).length == 1){
				BtBankAccDTO btBankAcc = btBankAccService.getBtBankAcc(accId);
				model.addAttribute("electricBill", btBankAcc.getElectricBill() == null ? "0" : btBankAcc.getElectricBill());
				model.addAttribute("circulationMode", btBankAcc.getCirculationMode() == null ? "0" : btBankAcc.getCirculationMode());
			}else{
				model.addAttribute("electricBill", "0");
				model.addAttribute("circulationMode", "0");
			}
		}
		return "bankacc/batchSetBillInfo";
	}
	/**
	 * 批量设置票据账户
	 *
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACC_APPLY)
	@LogMessage(content = "批量设置票据账户", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/batchSetBillInfo")
	@ResponseBody
	public AjaxJson batchSetPayerAcc(BtBankAccVO btBankAccVO) {
		btBankAccHandleService.batchSetBillInfo(btBankAccVO, this.getCurrentUser());
		return success();
	}
	@GetMapping("/listByType")
	public String listByType(String type,Model model) {
		type = type.substring(0,1);
		model.addAttribute("type",type);
		return "bankacc/listBtBankAccHandles";
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
		return "bankacc/listBtBankAccHandleApprove";
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
		return this.btBankAccHandleService.dataTableApprove(dataTablesInput);
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
			BtBankAccDTO vo = btBankAccCommonService.getBtBankAccVOById(id);
			BtBankAccDTO btBankAcc = btBankAccHandleService.getBtBankAccVOById(id);

			if (EmptyUtils.isEmpty(btBankAcc.getIsOnline())) {
				btBankAcc.setIsOnline(BtBankAccConstant.ISONLINE_1);
			}
			if (EmptyUtils.isEmpty(btBankAcc.getIsCapitalPool())) {
				btBankAcc.setIsCapitalPool(BtBankAccConstant.ISCAPITALPOOL_0);
			}
			if (EmptyUtils.isEmpty(btBankAcc.getValidSign())) {
				btBankAcc.setValidSign(BtBankAccConstant.VALIDSIGN_1);
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
			if (BtBankAccConstant.ISONLINE_0.equals(btBankAcc.getIsOnline())) {
				if (EmptyUtils.isEmpty(btBankAcc.getInitBalance())) {
					btBankAcc.setInitBalance(BigDecimal.valueOf(0.00));
				}
				if (EmptyUtils.isEmpty(btBankAcc.getInitBalanceDate())) {
					btBankAcc.setInitBalanceDate(new Date());
				}
			}else {
				btBankAcc.setInitBalance(null);
				btBankAcc.setInitBalanceDate(null);
			}
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
			//获取所有的单据类型
			List<FcReceiptType> validFcReceiptType = fcReceiptTypeService.getValidFcReceiptType();
			HashMap<String, String> fcReceiptMap = new HashMap<>();
			for (FcReceiptType fcReceiptType : validFcReceiptType) {
				fcReceiptMap.put(fcReceiptType.getReceiptCode(),fcReceiptType.getReceiptName());
			}
			if (EmptyUtils.isNotEmpty(btBankAcc.getReceiptCode())){
				List<String> receiptCodeList = Arrays.asList(btBankAcc.getReceiptCode().split(","));
				StringBuilder fcReceiptTypeStr = new StringBuilder();
				for(int i = 0;i<=receiptCodeList.size()-1;i++){
					if (i==receiptCodeList.size()-1){
						if (fcReceiptMap.containsKey(receiptCodeList.get(i))){
							fcReceiptTypeStr.append(fcReceiptMap.get(receiptCodeList.get(i)));
						}
					}else{
						if (fcReceiptMap.containsKey(receiptCodeList.get(i))){
							fcReceiptTypeStr.append(fcReceiptMap.get(receiptCodeList.get(i))+",");
						}
					}
				}
				if(ObjectUtil.isNotEmpty(fcReceiptTypeStr)) btBankAcc.setReceiptName(fcReceiptTypeStr.toString());
			}
			model.addAttribute("btBankAcc",btBankAcc);
			//预留印鉴处理begin
			String sealDirectory = btBankAcc.getSealDirectory();
			String[] sealDirectoryList = new String[3];
			if(StringUtils.isNotBlank(sealDirectory)){
				String[] split = sealDirectory.split(CommonConstant.COMMA_SPLIT);
				System.arraycopy(split, 0, sealDirectoryList, 0, split.length);
			}
			vo.setSealDirectoryList(sealDirectoryList);
			//预留印鉴处理end
			vo.setReceiptName(btBankAcc.getReceiptName());
			vo.setFcAccGroupName(btBankAcc.getFcAccGroupName());
			model.addAttribute("btBankAccPage", vo);
			model.addAttribute("busType", BtBankAccMessageConstant.BUS_TYPE_APPHANDLE);
			model.addAttribute("taskId", taskId);
			//如果是虚拟户 需要查询对应的实体账号
			if (BtBankAccConstant.ACCTYPE_09.equals(vo.getAccType()) && StringUtils.isNotEmpty(vo.getEntityAccId())){
				BtBankAccDTO bankAccById = btBankAccService.getBankAccById(vo.getEntityAccId());
				if(bankAccById != null){
					vo.setEntityAcc(bankAccById.getBankAcc());
				}
			}
		}
		return "bankacc/approveBtBankAccHandle";
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
			BtBankAccDTO btBankAcc = btBankAccCommonService.getBtBankAccVOById(businessId);
			model.addAttribute("btBankAcc", btBankAcc);
			model.addAttribute("taskId", taskId);
		}
		return "bankacc/viewApproveBtBankAcHandle";
	}

	@GetMapping("/chooseBindingBankAcc")
	public String chooseBindingBankAcc() {
		return "bankacc/chooseBindingBankAcc";
	}

	@GetMapping("/viewBindingBankAcc")
	public String viewBindingBankAcc() {
		return "bankacc/viewBindingBankAcc";
	}

	@LogMessage(content = "批量设置非直联", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/batchUnOnline")
	@ResponseBody
	public AjaxJson batchUnOnline(@RequestParam("ids") String ids) {
		btBankAccHandleService.batchUnOnline(ids,getCurrentUser());
		return success();
	}

}
