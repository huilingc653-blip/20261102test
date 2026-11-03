package com.bytter.biz.saas.controller.bankacc;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.bdlink.api.constant.BtBankAccMessageConstant;
import com.bytter.biz.inneraccount.entity.BtBankAccVirtual;
import com.bytter.biz.inneraccount.service.BtBankAccHandleService;
import com.bytter.biz.inneraccount.service.BtBankAccRevHandleService;
import com.bytter.biz.inneraccount.service.BtBankAccVirtualService;
import com.bytter.biz.inneraccount.vo.v1.BtBankAccRevHandlePageVO;
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
import com.bytter.system.api.dto.BtBankAccAppDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.vo.BtBankAccVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(value = "银行账号变更办理", tags = "银行账号变更办理")
@RestController("v1BtBankAccRevHandleController")
@RequestMapping("/v1-payment/bankacc/btbankaccrevhandle")
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
	 * 银行账号申请表 table列表
	 *
	 * @return
	 */
	@ApiOperation("银行账号变更办理")
	@PostMapping("/datatable")
	@ResponseBody
	public AjaxJson<DataTablesOutput<BtBankAccAppDTO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		return success(btBankAccRevHandleService.getDataTablesUnion(input, this.getCurrentUser()));
	}
	/**
	 * 银行账号申请表编辑页面跳转
	 *
	 * @return
	 */
	@ApiOperation(value = "变更办理跳转编辑页面",
			notes = "原先vo与btBankAccPage重复 直接使用btBankAccPage替换掉即可 在这个对象中包含所需要的内容")
	@GetMapping("/getInfoBeforeUpdate")
	@ResponseBody
	public AjaxJson<BtBankAccRevHandlePageVO> getInfoBeforeUpdate(String id, String bankAcc) {
		BtBankAccRevHandlePageVO pageVO = new BtBankAccRevHandlePageVO();
		if (StringUtils.isNotEmpty(id)) {
			BtBankAccDTO btBankAcc = btBankAccCommonService.getBtBankAccByBankAcc(bankAcc);
			String accId = btBankAcc.getId();
			String curId = btBankAccCommonService.setCurId(id, "app");
			BtBankAccAppDTO app = btBankAccAppService.getById(id);
			String appId = app.getId();
			BeanUtils.copyNotNullProperties(app, btBankAcc);
			btBankAcc.setId(accId);
			pageVO.setAppId(appId);
			pageVO.setCurId(curId);
			//预留印鉴处理begin
			String sealDirectory = btBankAcc.getSealDirectory();
			String[] sealDirectoryList = new String[3];
			if(StringUtils.isNotBlank(sealDirectory)){
				String[] split = sealDirectory.split(CommonConstant.COMMA_SPLIT);
				System.arraycopy(split, 0, sealDirectoryList, 0, split.length);
			}
			btBankAcc.setSealDirectoryList(sealDirectoryList);
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
			btBankAcc.setCurId(curId);
			pageVO.setBtBankAccPage(btBankAcc);
			pageVO.setTodate(DateUtils.formatDate(DateUtils.getDate()));
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
			//银行类别对应的境内外标识
			pageVO.setIsDomesticBankType(btBankAcc.getIsDomesticBank() ==null ? "":btBankAcc.getIsDomesticBank());
			//招行 虚拟户 线上办理
			int dataListSize = 0;
			if (BtBankAccConstant.ACCTYPE_09.equals(btBankAcc.getAccType()) && CommonConstant.YES.equals(btBankAcc.getIsOnlineHandle())
					&& BtBankAccConstant.BIS_BIF_CODE_CMB.equals(btBankAcc.getBifCode())) {
				List<BtBankAccVirtual> listVirtual = btBankAccVirtualService.getListByBankAccId(btBankAcc.getId());
				pageVO.setListVirtual(listVirtual);
				dataListSize = listVirtual.size();
			}
			pageVO.setDataListSize(dataListSize);
			return success(pageVO);
		}
		return success();
	}
	/**
	 * 银行账号申请表详情页
	 *
	 * @return
	 */
	@ApiOperation(value = "变更办理跳转查看页面",
			notes = "原先vo与btBankAccPage重复 直接使用btBankAccPage替换掉即可 在这个对象中包含所需要的内容")
	@LogMessage(content = "查询银行账号申请表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{id}")
	public AjaxJson<BtBankAccRevHandlePageVO> view(@PathVariable("id") String id, Model model) {
		BtBankAccRevHandlePageVO pageVO = new BtBankAccRevHandlePageVO();
		BtBankAccDTO bankAccDTO = btBankAccCommonService.getBtBankAccVOById(id);
		//查询所有账户组
		List<FcBankAccGroup> validPayType = fcBankAccGroupService.getValidPayType();
		HashMap<String, String> map = new HashMap<>();
		for (FcBankAccGroup fcBankAccGroup : validPayType) {
			map.put(fcBankAccGroup.getAccGroupCode(),fcBankAccGroup.getAccGroupName());
		}
		//查询账户组名称
		String fcAccGroupCode = bankAccDTO.getFcAccGroupCode();
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
			bankAccDTO.setFcAccGroupName(accGroupName.toString());
		}
		//获取所有的单据类型
		List<FcReceiptType> validFcReceiptType = fcReceiptTypeService.getValidFcReceiptType();
		HashMap<String, String> fcReceiptMap = new HashMap<>();
		for (FcReceiptType fcReceiptType : validFcReceiptType) {
			fcReceiptMap.put(fcReceiptType.getReceiptCode(),fcReceiptType.getReceiptName());
		}
		if (EmptyUtils.isNotEmpty(bankAccDTO.getReceiptCode())){
			List<String> receiptCodeList = Arrays.asList(bankAccDTO.getReceiptCode().split(","));
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
			if(ObjectUtil.isNotEmpty(fcReceiptType)){
				bankAccDTO.setReceiptName(fcReceiptType.toString());
			}
		}
		pageVO.setBtBankAccPage(bankAccDTO);
		if (BtBankAccConstant.ACCTYPE_09.equals(bankAccDTO.getAccType()) && StringUtils.isNotEmpty(bankAccDTO.getEntityAccId())){
			BtBankAccDTO bankAccById = btBankAccService.getBankAccById(bankAccDTO.getEntityAccId());
			if(bankAccById != null){
				bankAccDTO.setEntityAcc(bankAccById.getBankAcc());
			}
		}
		return success(pageVO);
	}
	/**
	 * 修改银行账号申请表
	 *
	 * @param vo
	 * @return
	 */
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCREVHANDLE_APPLY)
	@ApiOperation("修改提交")
	@LogMessage(content = "修改银行账号申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson modify(@RequestBody BtBankAccVO vo) {
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
	@ApiOperation("删除")
	@LogMessage(content = "删除银行账号申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
	@PostMapping("/delete/{id}")
	@ResponseBody
	public AjaxJson delete(@PathVariable("id") String id) {
		this.btBankAccRevHandleService.back(id, this.getCurrentUser());
		return success();
	}
	/**
	 * 加载审核列表数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@ApiOperation(value = "加载银行账户变更办理审核列表数据")
	@PostMapping("/dataTableApprove")
	@ResponseBody
	public AjaxJson<DataTablesOutput<WorkFlowPersistence>> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
		DataTablesInput input = dataTablesInput.convertAndInitColumns();
		return success(this.btBankAccRevHandleService.dataTableApprove(input));
	}
	/**
	 * 跳转至审核详情界面
	 *
	 * @param id
	 * @param taskId
	 * @return String
	 */
	@ApiOperation(value = "变更办理跳转查看页面",
			notes = "原先btBankAcc与btBankAccPage重复 直接使用btBankAccPage替换掉即可 在这个对象中包含所需要的内容\n" +
					"原先的app.id是传输过来的id 直接赋值原id即可")
	@GetMapping("/approve")
	@ResponseBody
	public AjaxJson<BtBankAccRevHandlePageVO> approve(String id, String taskId) {
		BtBankAccRevHandlePageVO pageVO = new BtBankAccRevHandlePageVO();
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
			//预留印鉴处理begin
			String sealDirectory = vo.getSealDirectory();
			String[] sealDirectoryList = new String[3];
			if(StringUtils.isNotBlank(sealDirectory)){
				String[] split = sealDirectory.split(CommonConstant.COMMA_SPLIT);
				System.arraycopy(split, 0, sealDirectoryList, 0, split.length);
			}
			vo.setSealDirectoryList(sealDirectoryList);
			//预留印鉴处理end
			pageVO.setBtBankAccPage(vo);
			pageVO.setBusType(BtBankAccMessageConstant.BUS_TYPE_REVHANDLE);
			pageVO.setTaskId(taskId);
			return success(pageVO);
		}
		return success();
	}
	/**
	 * 跳转至手机审核详情界面
	 *
	 * @param businessId
	 * @param taskId
	 * @return String
	 */
	@ApiOperation(value = "变更办理跳转查看页面",
			notes = "原先btBankAcc与btBankAccPage重复 直接使用btBankAccPage替换掉即可 在这个对象中包含所需要的内容\n")
	@GetMapping("/mobileApprove")
	@ResponseBody
	public AjaxJson<BtBankAccRevHandlePageVO> mobileApprove(String businessId, String taskId) {
		BtBankAccRevHandlePageVO pageVO = new BtBankAccRevHandlePageVO();
		if (StringUtils.isNotEmpty(businessId)) {
            BtBankAccAppDTO app = btBankAccCommonService.getBtBankAccApp(businessId);
			BtBankAccDTO acc = btBankAccCommonService.getBtBankAccByBankAcc(app.getBankAcc());
			BtBankAccDTO btBankAcc = btBankAccCommonService.getBtBankAccVOById(acc.getId());
			pageVO.setBtBankAccPage(btBankAcc);
			pageVO.setApp(app);
			pageVO.setTaskId(taskId);
			return success(pageVO);
		}
		return success();
	}
}
