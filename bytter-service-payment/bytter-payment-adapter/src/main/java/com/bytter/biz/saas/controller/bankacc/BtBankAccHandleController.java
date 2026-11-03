package com.bytter.biz.saas.controller.bankacc;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.bdlink.api.constant.BtBankAccMessageConstant;
import com.bytter.biz.inneraccount.entity.BtBankAccVirtual;
import com.bytter.biz.inneraccount.service.BtBankAccHandleService;
import com.bytter.biz.inneraccount.service.BtBankAccVirtualService;
import com.bytter.biz.inneraccount.vo.v1.BtBankAccHandlePageVO;
import com.bytter.biz.inneraccount.vo.v1.BtBankAccSetBillInfoVO;
import com.bytter.biz.netbank.entity.BtBankAccUse;
import com.bytter.biz.netbank.entity.FcBankAccGroup;
import com.bytter.biz.netbank.entity.FcReceiptType;
import com.bytter.biz.netbank.service.BtBankAccUseService;
import com.bytter.biz.netbank.service.FcBankAccGroupService;
import com.bytter.biz.netbank.service.FcReceiptTypeService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.bpm.persistence.WorkFlowPersistence;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
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
import com.bytter.system.api.vo.BtBankAccHandleVO;
import com.bytter.system.api.vo.BtBankAccVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Api(value = "银行账户开户办理",tags = "银行账户开户办理")
@RestController("v1BtBankAccHandleController")
@RequestMapping("/v1-payment/bankacc/btbankacchandle")
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
	 * BT_BANK_ACC table列表
	 *
	 * @return
	 */
	@ApiOperation("银行账户开户办理列表")
	@PostMapping("/datatable")
	@ResponseBody
	public AjaxJson<DataTablesOutput<BtBankAccHandleVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		return success(btBankAccHandleService.getDataTablesUnion(input, this.getCurrentUser()));
	}
	/**
	 * BT_BANK_ACC编辑页面跳转
	 *
	 * @return
	 */
	@ApiOperation(value = "开户办理跳转编辑页面",
			notes = "原先vo与btBankAccPage重复 直接使用btBankAccPage替换掉即可 在这个对象中包含所需要的内容")
	@GetMapping("/getInfoBeforeUpdate")
	@ResponseBody
	public AjaxJson<BtBankAccHandlePageVO> getInfoBeforeUpdate(
			@ApiParam(value = "table列表的对应id",required = true) String id,
			@ApiParam(value = "table列表的对应isFrom字段",required = true) String isFrom) {
		BtBankAccHandlePageVO pageVO = new BtBankAccHandlePageVO();
		int dataListSize = 0;
		// 银行类型是否8500
		String bankTypeIs8500 = "0";
		if ("1".equals(isFrom)) {
			if (StringUtils.isNotEmpty(id)) {
				BtBankAccDTO btBankAcc = btBankAccHandleService.getBtBankAccVOById(id);
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

				pageVO.setCorpName(btBankAcc.getCorpName());
				pageVO.setCurId(curId);
				pageVO.setElectricBill(btBankAcc.getElectricBill());
				//银行类别对应的境内外标识
				BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(btBankAcc.getBankTypeId());
				pageVO.setIsDomesticBankType(btBankAcc.getIsDomesticBank() ==null ? "":btBankAcc.getIsDomesticBank());

				String isThirdParty = "";
				String bindingBankAcc = "";
				if(!EmptyUtils.isEmpty(bankTypeDTO) && EmptyUtils.isNotEmpty(bankTypeDTO.getIsThirdParty())){
					isThirdParty = bankTypeDTO.getIsThirdParty();
				}
				pageVO.setIsThirdParty(isThirdParty);
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
				pageVO.setBtBankAccPage(btBankAcc);
				//预留印鉴处理end
				pageVO.setIsFrom(isFrom);
				//是否允许变更为保证金户
				String isAllowedChangeDeposit = "1";
				//获取本位币
				BtCurrencyDTO natualCur = btCurrencyService.getNatualCurrency();
				//账号币别为多币别时，都不允许变更为保证金
				if(StringUtils.isNotEmpty(curId) && (curId.length()>32)){
					isAllowedChangeDeposit = "0";
				}
				pageVO.setIsAllowedChangeDeposit(isAllowedChangeDeposit);
				//招行 虚拟户 线上办理
				if (BtBankAccConstant.ACCTYPE_09.equals(btBankAcc.getAccType()) && CommonConstant.YES.equals(btBankAcc.getIsOnlineHandle())
						&& BtBankAccConstant.BIS_BIF_CODE_CMB.equals(btBankAcc.getBifCode())) {
					List<BtBankAccVirtual> listVirtual = btBankAccVirtualService.getListByBankAccId(btBankAcc.getId());
					pageVO.setListVirtual(listVirtual);
					dataListSize = listVirtual.size();
				}
			}
		} else {
			if (StringUtils.isNotEmpty(id)) {
				BtBankAccAppDTO btBankAccApp = btBankAccAppService.getBankAccApp(id);
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

				pageVO.setCorpName(btBankAccApp.getCorpName());
				pageVO.setCurId(btBankAccApp.getCurId());
				pageVO.setCurName(btBankAccApp.getCurName());
				pageVO.setElectricBill(btBankAcc.getElectricBill());
				pageVO.setTodate(DateUtils.formatDate(date));
				pageVO.setIsFrom(isFrom);
				//银行类别对应的境内外标识
				BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(btBankAccApp.getBankTypeId());
				pageVO.setIsDomesticBankType(btBankAcc.getIsDomesticBank() ==null ? "":btBankAcc.getIsDomesticBank());
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
				pageVO.setIsThirdParty(isThirdParty);
//				model.addAttribute("bindingBankAcc",bindingBankAcc);
				if (!EmptyUtils.isEmpty(bankTypeDTO) && "8500".equals(bankTypeDTO.getBankType())){
					bankTypeIs8500 = "1";
				}
				//预留印鉴处理begin
				BtBankAccVO accVo = new BtBankAccVO();
				btBankAcc.setSealDirectoryList(new String[3]);
				pageVO.setBtBankAccPage(btBankAcc);
				//预留印鉴处理end
				//是否允许变更为保证金户
				String isAllowedChangeDeposit = "1";
				//获取本位币
				BtCurrencyDTO natualCur = btCurrencyService.getNatualCurrency();
				//账号币别为多币别时，都不允许变更为保证金
				if(StringUtils.isNotEmpty(curId) && (curId.length()>32)){
					isAllowedChangeDeposit = "0";
				}
				pageVO.setIsAllowedChangeDeposit(isAllowedChangeDeposit);
			}
		}
		pageVO.setDataListSize(dataListSize);
		pageVO.setBankTypeIs8500(bankTypeIs8500);
		return success(pageVO);
	}
	/**
	 * BT_BANK_ACC详情页
	 *
	 * @return
	 */
	@ApiOperation(value = "查询银行账户开户办理",
			notes = "原先btBankAcc 与 btBankAccPage内容重复 直接全部使用btBankAccPage即可")
	@LogMessage(content = "查询BT_BANK_ACC详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	@ResponseBody
	public AjaxJson<BtBankAccHandlePageVO> view(@PathVariable("id") String id) {
		BtBankAccHandlePageVO pageVO = new BtBankAccHandlePageVO();
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
		pageVO.setIsThirdParty(isThirdParty);
		pageVO.setBindingBankAcc(bindingBankAcc);
		//预留印鉴处理begin
		String sealDirectory = btBankAcc.getSealDirectory();
		String[] sealDirectoryList = new String[3];
		if(StringUtils.isNotBlank(sealDirectory)){
			String[] split = sealDirectory.split(CommonConstant.COMMA_SPLIT);
			System.arraycopy(split, 0, sealDirectoryList, 0, split.length);
		}
		btBankAcc.setSealDirectoryList(sealDirectoryList);
		//预留印鉴处理end
		pageVO.setBtBankAccPage(btBankAcc);
		pageVO.setBusType(BtBankAccMessageConstant.BUS_TYPE_APPHANDLE);
		Boolean isShowUKeyOther = false;
		if(EmptyUtils.isNotEmpty(btBankAcc.getuKeyFunction()) &&  btBankAcc.getuKeyFunction().contains("9")){
			isShowUKeyOther = true;
		}
		pageVO.setIsShowUKeyOther(isShowUKeyOther);
		//如果是虚拟户 需要查询对应的实体账号
		if (BtBankAccConstant.ACCTYPE_09.equals(btBankAcc.getAccType()) && StringUtils.isNotEmpty(btBankAcc.getEntityAccId())){
			BtBankAccDTO bankAccById = btBankAccService.getBankAccById(btBankAcc.getEntityAccId());
			if(bankAccById != null){
				btBankAcc.setEntityAcc(bankAccById.getBankAcc());
			}
		}
		//招行 虚拟户 线上办理
		if (BtBankAccConstant.ACCTYPE_09.equals(btBankAcc.getAccType()) && CommonConstant.YES.equals(btBankAcc.getIsOnlineHandle())
				&& BtBankAccConstant.BIS_BIF_CODE_CMB.equals(btBankAcc.getBifCode())) {
			List<BtBankAccVirtual> listVirtual = btBankAccVirtualService.getListByBankAccId(btBankAcc.getId());
			pageVO.setListVirtual(listVirtual);
		}
		return success(pageVO);
	}
	/**
	 * 添加BT_BANK_ACC
	 *
	 * @param btBankAccVO
	 * @return
	 */
	@ApiOperation("添加银行账户开户办理")
	@LogMessage(content = "添加BT_BANK_ACC", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/save")
	@ResponseBody
	public AjaxJson save(@RequestBody BtBankAccVO btBankAccVO) {
		btBankAccHandleService.insertField(btBankAccVO);//用于银行预留印鉴的id拼接
		System.out.println(btBankAccHandleService);
		btBankAccHandleService.toSave(btBankAccVO, this.getCurrentUser(), btBankAccVO.getStatus().toString());
		return success();
	}
	/**
	 * 修改BT_BANK_ACC
	 *
	 * @param btBankAccVO
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACC_APPLY)
	@ApiOperation("修改银行账户开户办理")
	@LogMessage(content = "修改BT_BANK_ACC", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson modify(@RequestBody BtBankAccVO btBankAccVO) {
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
	@ApiOperation("删除银行账户开户办理")
	@LogMessage(content = "打回BT_BANK_ACC_APP", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id) {
		this.btBankAccHandleService.deleteBtBankAcc(id,this.getCurrentUser());
		return success();
	}

	/**
	 * 保存导入数据
	 *
	 * @param list
	 * @return
	 */
	@ApiOperation("保存导入数据")
	@PostMapping("/saveImport")
	@ResponseBody
	public AjaxJson saveImport(@RequestBody List<BtBankAccVO> list) {
		list = list.stream().filter(btb -> StringUtils.isNotEmpty(btb.getBankAcc()) && StringUtils.isNotEmpty(btb.getBankName())).collect(Collectors.toList());
		this.btBankAccHandleService.saveImport(list, this.getCurrentUser());
		return success();
	}
	/**
	 * 根据银行接口代码得到银行账号，用与银企直联
	 * BT_BANK_ACC table列表
	 *
	 * @return
	 */
	//未找到使用地方
//	@SuppressWarnings("rawtypes")
//	@PostMapping("/getDataTableByBifCode")
//	@ResponseBody
//	public DataTablesOutput getDataTableByBifCode(@RequestBody DataTablesInput dataTablesInput) {
//		return btBankAccHandleService.getDataTableByBifCode(dataTablesInput, this.getCurrentUser());
//	}
	/**
	 * 判断账号是否有父子关系
	 *
	 * @param id
	 * @return
	 */
	@ApiOperation("判断账号是否有父子关系")
	@PostMapping("/isHasPrantAndChild")
	@ResponseBody
	public AjaxJson isHasPrantAndChild(String id) {
		AjaxJson j = success();
		j.setObj(btBankAccHandleService.isHasPrantAndChild(id));
		return j;
	}
	/**
	* 银行多选组件 begin
	* author: qinl
	* date: 2020-04-16
	*/
//	@GetMapping("/mulSelectUserPage")
//	public String mulSelectUserPage(BtBankAccVO bankAcc, Model model) {
//		model.addAttribute("bankAcc", bankAcc);
//		return "bankacc/mulSelectUserPage";
//	}
	@ApiOperation("银行账户多选组件 查询")
	@GetMapping("/mulViewUserPage")
	@ResponseBody
	public AjaxJson<List<BtBankAccHandleVO>> mulViewUserPage(String id) {
		if(StringUtils.isBlank(id)){
			throw new ParameterException(CheckMessageEnum.FIN_CHECK_CENTRALIYDATAMANAGER_BANKACC_EMPTY);
		}
		DataTablesInput dataTablesInput = new DataTablesInput();
		Map<String,Object> map = new HashMap<>();
		map.put("ins_id",id);
		dataTablesInput.setFormdata(map);
		DataTablesOutput dataTablesUnion = btBankAccHandleService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
		return success(dataTablesUnion.getData());
	}
	/*** 银行多选组件 end*/
	/**
	 * 批量设置票据账户
	 * @param  accId 单据id
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACC_APPLY)
	@ApiOperation("批量设置票据账户前的参数获取")
	@GetMapping("/toSetBillInfo/{accId}")
	@ResponseBody
	public AjaxJson<BtBankAccSetBillInfoVO> toSetPayerAcc(@PathVariable("accId") String accId) {
		BtBankAccSetBillInfoVO vo = new BtBankAccSetBillInfoVO();
		if(!EmptyUtils.isEmpty(accId)){
			if(!EmptyUtils.isEmpty(accId.split(CommonConstant.COMMA_SPLIT)) && accId.split(CommonConstant.COMMA_SPLIT).length == 1){
				BtBankAccDTO btBankAcc = btBankAccService.getBtBankAcc(accId);
				vo.setElectricBill(btBankAcc.getElectricBill() == null ? "0" : btBankAcc.getElectricBill());
				vo.setCirculationMode(btBankAcc.getCirculationMode() == null ? "0" : btBankAcc.getCirculationMode());
			}else{
				vo.setElectricBill("0");
				vo.setCirculationMode("0");
			}
		}
		return success(vo);
	}
	/**
	 * 批量设置票据账户
	 *
	 * @return
	 */
	@ApiOperation("批量设置票据账户")
	@LogMessage(content = "批量设置票据账户", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
	@PostMapping("/batchSetBillInfo")
	@ResponseBody
	public AjaxJson batchSetPayerAcc(@RequestBody BtBankAccVO btBankAccVO) {
		btBankAccHandleService.batchSetBillInfo(btBankAccVO, this.getCurrentUser());
		return success();
	}

	/**
	 * 加载审核列表数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@ApiOperation("加载银行账户开户办理审核列表数据")
	@PostMapping("/dataTableApprove")
	@ResponseBody
	public AjaxJson<DataTablesOutput<WorkFlowPersistence>> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		return success(this.btBankAccHandleService.dataTableApprove(input));
	}
	/**
	 * 跳转至审核详情界面
	 *
	 * @param id
	 * @param taskId
	 * @return String
	 */
	@ApiOperation("跳转审批详情页面前的数据获取")
	@GetMapping("/approve")
	@ResponseBody
	public AjaxJson<BtBankAccHandlePageVO> approve(String id, String taskId) {
		BtBankAccHandlePageVO pageVO = new BtBankAccHandlePageVO();
		if (StringUtils.isNotEmpty(id)) {
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
				if (ObjectUtil.isNotEmpty(accUseName)){
					accUseName = accUseName.substring(0,accUseName.length()-1);
				}
				btBankAcc.setAccUseName(accUseName);
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
				if(ObjectUtil.isNotEmpty(fcReceiptTypeStr)){
					btBankAcc.setReceiptName(fcReceiptTypeStr.toString());
				}
			}
			//预留印鉴处理begin
			String sealDirectory = btBankAcc.getSealDirectory();
			String[] sealDirectoryList = new String[3];
			if(StringUtils.isNotBlank(sealDirectory)){
				String[] split = sealDirectory.split(CommonConstant.COMMA_SPLIT);
				System.arraycopy(split, 0, sealDirectoryList, 0, split.length);
			}

			btBankAcc.setSealDirectoryList(sealDirectoryList);
			//预留印鉴处理end
			btBankAcc.setReceiptName(btBankAcc.getReceiptName());
			btBankAcc.setFcAccGroupName(btBankAcc.getFcAccGroupName());
			pageVO.setBtBankAccPage(btBankAcc);
			pageVO.setBusType(BtBankAccMessageConstant.BUS_TYPE_APPHANDLE);
			pageVO.setTaskId(taskId);
			return success(pageVO);
		}
		return success();
	}

	/**
	 * 跳转至手机审核详情界面
	 * @param businessId
	 * @param taskId
	 * @return
	 */
	@ApiOperation(value = "手机审核详情界面",
			notes = "由原先的btBankAcc返回参数 变为了btBankAccPage")
	@GetMapping("/mobileApprove")
	@ResponseBody
	public AjaxJson<BtBankAccHandlePageVO> mobileApprove(String businessId, String taskId) {
		BtBankAccHandlePageVO pageVO = new BtBankAccHandlePageVO();
		if (StringUtils.isNotEmpty(businessId)) {
			BtBankAccDTO btBankAcc = btBankAccCommonService.getBtBankAccVOById(businessId);
			pageVO.setBtBankAccPage(btBankAcc);
			pageVO.setBtBankAccPage(btBankAcc);
			pageVO.setTaskId(taskId);
		}
		return success(pageVO);
	}


	@ApiOperation("批量设置非直联")
	@LogMessage(content = "批量设置非直联", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/batchUnOnline")
	@ResponseBody
	public AjaxJson batchUnOnline(@RequestParam("ids") String ids) {
		btBankAccHandleService.batchUnOnline(ids,getCurrentUser());
		return success();
	}

}
