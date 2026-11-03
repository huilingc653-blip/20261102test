package com.bytter.biz.bankacc.controller;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.inneraccount.service.BtBankAccVirtualService;
import com.bytter.biz.netbank.entity.FcBankAccGroup;
import com.bytter.biz.netbank.entity.FcReceiptType;
import com.bytter.biz.netbank.service.FcBankAccGroupService;
import com.bytter.biz.netbank.service.FcReceiptTypeService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BtBankAccConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.poi.util.ExportUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.IBtBankAccCommonService;
import com.bytter.system.api.IBtBankAccQueryService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.BtBankAccAppDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.vo.BtBankAccVO;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
//import com.bytter.biz.inneraccount.service.BtBankAccHandleService;
/**   
 * @Title: Controller  
 * @Description: BT_BANK_ACC
 * @author onlineGenerator
 * @date 2017-06-02 17:50:16
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bankacc/btbankaccquery")
public class BtBankAccQueryController extends BaseController {
	@DubboReference(check = false)
	private IBtBankAccQueryService btBankAccQueryService;//变接口
	@DubboReference(check = false)
	private IBtBankAccCommonService btBankAccCommonService;//变接口
//	@Autowired
//	private BtBankAccHandleService btBankAccHandleService;//变接口
	@Autowired
	private BtBankAccVirtualService btBankAccVirtualService;
	@Autowired
	private FcBankAccGroupService fcBankAccGroupService;
	@Autowired
	private FcReceiptTypeService fcReceiptTypeService;
	@DubboReference(check = false)
	private IBankTypeService bankTypeService;
	@DubboReference(check = false)
	private IBtBankAccService btBankAccService;
	/**
	* BT_BANK_ACC列表 页面跳转
	*
	* @return
	*/
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCQUERY_QUERY)
	@LogMessage(content = "查询BT_BANK_ACC列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "bankacc/listBtBankAccQuery";
	}
    /**
    * BT_BANK_ACC table列表
    *
    * @return
    */
    @SuppressWarnings("rawtypes")
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCQUERY_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
    	return btBankAccQueryService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }

	@ApiOperation("分页查询银行账户列表 携带账户全部币别")
	@PostMapping("/datatableWithAllCurId")
	@ResponseBody
	public DataTablesOutput<BtBankAccVO> datatableWithAllCurId(@RequestBody DataTablesInput dataTablesInput) {
		return btBankAccQueryService.getDataTablesUnionWithAllCurId(dataTablesInput, this.getCurrentUser());
	}
	/**
	* BT_BANK_ACC详情页
	*
	* @return
	*/
	////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BTBANKACCQUERY_QUERY)
	@LogMessage(content = "查询BT_BANK_ACC详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view")
	public String view(BtBankAccVO acc,Model model) {
		BtBankAccDTO vo = btBankAccCommonService.getBtBankAccVOById(acc.getId(), acc.getCurId());
		//银行账号查询附件查询是开户时的附件
		BtBankAccAppDTO app = btBankAccCommonService.getBtBankAccAppByBankAcc(vo.getBankAcc());
		if(!EmptyUtils.isEmpty(app)){
			vo.setAttachmentId(app.getAttachmentId());
		}
		//查询所有账户组
		List<FcBankAccGroup> validPayType = fcBankAccGroupService.getValidPayType();
		HashMap<String, String> map = new HashMap<>();
		for (FcBankAccGroup fcBankAccGroup : validPayType) {
			map.put(fcBankAccGroup.getAccGroupCode(),fcBankAccGroup.getAccGroupName());
		}
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
			if(ObjectUtil.isNotEmpty(fcReceiptTypeStr)) vo.setReceiptName(fcReceiptTypeStr.toString());
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
		BtBankAccDTO btBankAcc = btBankAccCommonService.getBtBankAcc(vo.getId());
		//银行类别对应的境内外标识
		BankTypeDTO bankTypeDTO = bankTypeService.getObjectById(btBankAcc.getBankTypeId());
		String isThirdParty = "";
		String bindingBankAcc = "";
		if(!EmptyUtils.isEmpty(bankTypeDTO) && EmptyUtils.isNotEmpty(bankTypeDTO.getIsThirdParty())){
			isThirdParty = bankTypeDTO.getIsThirdParty();
		}
		if(isThirdParty.equals("1")){
			if(!EmptyUtils.isEmpty(vo.getBindingBankAccStr())){
				bindingBankAcc = vo.getBindingBankAccStr();
			}
		}
		model.addAttribute("isThirdParty",isThirdParty);
		model.addAttribute("bindingBankAcc",bindingBankAcc);
		model.addAttribute("btBankAcc",btBankAcc);
		model.addAttribute("btBankAccPage", vo);
		model.addAttribute("listReq",btBankAccQueryService.listReqByBankAcc(btBankAcc.getBankAcc()));
		//招行 虚拟户 线上办理
		if (BtBankAccConstant.ACCTYPE_09.equals(vo.getAccType()) && CommonConstant.YES.equals(vo.getIsOnlineHandle())
				&& BtBankAccConstant.BIS_BIF_CODE_CMB.equals(vo.getBifCode())) {
			List<com.bytter.biz.inneraccount.entity.BtBankAccVirtual> listVirtual = btBankAccVirtualService.getListByBankAccId(vo.getId());
			model.addAttribute("listVirtual", listVirtual);
		}
		return "bankacc/viewBtBankAccQuery";
	}
	
	/**
	 * 跳转多选银行行号控件页面
	 */
	@GetMapping("/mulBankAccInfo")
	public String mulBankAccInfo(String netId,String curId,String resource,Model model){
		model.addAttribute("netId", netId);
		model.addAttribute("curId", curId);
		model.addAttribute("resource", resource);
		return "bankacc/mulBankAccInfo";
	}
	
	/**
	 * 多选银行账号控件获取数据
	 */
	@PostMapping("/getNotDeleteAndValid")
	@ResponseBody
	public DataTablesOutput getNotDeleteAndValid(@RequestBody DataTablesInput input){
		return btBankAccQueryService.getNotDeleteAndValid(input,this.getCurrentUser());
	}
	/**
	 * 导出全部
	 *
	 * @return
	 */
	@RequestMapping("/exportAll")
	public void exportAll(HttpServletResponse response, String dataTablesInputParam, String exportExcelHeadListParam,
						  String totalParam, String fileNameParam) throws Exception {
		ExportUtils.exportByStream(response, dataTablesInputParam, exportExcelHeadListParam, new ExportUtils.ExportData() {
			@Override
			public List<?> getData(DataTablesInput dataTablesInput, ICurrentUser user) {
				DataTablesOutput<?> dataTablesOutput = btBankAccQueryService.getDataTablesUnion(dataTablesInput, user);
				return dataTablesOutput.getData();
			}
		}, totalParam, fileNameParam, "银行账户查询",5000, BtBankAccVO.class);
	}

	/**
	 * 银行账户添加附件界面跳转
	 *
	 * @return
	 */
	@LogMessage(content = "银行账户添加附件界面跳转",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/update")
	public String update(BtBankAccVO acc,Model model) {
		BtBankAccDTO vo = btBankAccCommonService.getBtBankAccVOById(acc.getId(), acc.getCurId());

		//预留印鉴处理begin
		String sealDirectory = vo.getSealDirectory();
		String[] sealDirectoryList = new String[3];
		if(StringUtils.isNotBlank(sealDirectory)){
			String[] split = sealDirectory.split(CommonConstant.COMMA_SPLIT);
			System.arraycopy(split, 0, sealDirectoryList, 0, split.length);
		}
		vo.setSealDirectoryList(sealDirectoryList);
		//预留印鉴处理end
		BtBankAccDTO btBankAcc = btBankAccCommonService.getBtBankAcc(vo.getId());
		model.addAttribute("btBankAcc",btBankAcc);
		model.addAttribute("btBankAccPage", vo);
		model.addAttribute("listReq",btBankAccQueryService.listReqByBankAcc(btBankAcc.getBankAcc()));
		//招行 虚拟户 线上办理
		if (BtBankAccConstant.ACCTYPE_09.equals(vo.getAccType()) && CommonConstant.YES.equals(vo.getIsOnlineHandle())
				&& BtBankAccConstant.BIS_BIF_CODE_CMB.equals(vo.getBifCode())) {
			List<com.bytter.biz.inneraccount.entity.BtBankAccVirtual> listVirtual = btBankAccVirtualService.getListByBankAccId(vo.getId());
			model.addAttribute("listVirtual", listVirtual);
		}
		return "bankacc/updateBtBankAccQuery";
	}

	/**
	 *
	 * @Title: 稽核策略维护新增或修改操作
	 * @param  vo
	 * @return
	 */
	@LogMessage(content = "稽核策略维护新增或修改操作",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/addAttachment")
	@ResponseBody
	public AjaxJson addAttachment(BtBankAccVO vo){
		btBankAccQueryService.addAttachment(vo.getId(),vo.getAttachmentId());
		return success();
	}
}
