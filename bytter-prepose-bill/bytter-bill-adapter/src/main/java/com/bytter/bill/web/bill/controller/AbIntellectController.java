package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbBillBankAutoDtlService;
import com.bytter.bill.domain.bill.service.AbIntellectService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoViceVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**   
 * @Title: Controller  
 * @Description: 智能选票
 * @author zhenghj
 * @date 2022-10-11
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/intellect")
public class AbIntellectController extends BaseController {
	@Autowired
	private AbIntellectService abIntellectService;
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	@Autowired
	private AbBillBankAutoDtlService abBillBankAutoDtlService;
	/**
	 * 智能选票
	 * @return 前端路径
	 */
	@GetMapping("/accebillIntellectPage")
	public String accebillIntellectPage(AbVoucherinfoViceVO reVo, Model model) {
		if("1".equals(reVo.getIsEmitBill())){
//			reVo.setIsPayed("0");
			reVo.setEndorse(null);
		}else{
			reVo.setPayee(null);
			/*List<String> bifCodeList = abBillBankAutoDtlService.getBifCodeByAutoType(AccebillConstant.AB_CONFIG_ISPOOLENDORSE);
			String bifCodes=null;
			if(bifCodeList!=null && !bifCodeList.isEmpty()){
				bifCodes=Joiner.on(",").join(bifCodeList);
			}*/
			model.addAttribute("bifCodes", null);
		}
		model.addAttribute("params", reVo);
		return "bill/accebill/accebillIntellectPage";
	}
	
	
	/**
	 *列表显示票据信息
	 *@param dataTablesInput
	 *@return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/accebillIntellectDatatable")
	@ResponseBody
	public DataTablesOutput accebillIntellectDataTable(@RequestBody DataTablesInput dataTablesInput){
		return abIntellectService.listAccebillInfoDataTable(dataTablesInput);
	}
	/**
	 * 票据详细信息查看
	 * @return
	 */
	@GetMapping("/accebillViewPage")
	public String accebillViewPage(AbVoucherinfoViceVO reVo, Model model) {
		List<AbVoucherinfoViceVO> infoList=abVoucherinfoViceService.listAccebillInfoView(reVo);
		model.addAttribute("infoList", infoList);
		return "bill/accebill/accebillViewPage";
	}
	/**
	* 根据单位ID和业务类型判断是否挂预算
	* @param ids
	* @return
	*/
	@PostMapping("/isBudget")
	@ResponseBody
	public AjaxJson isBudget(String corpId, String briefId){
		boolean isBudget=abVoucherinfoViceService.isBudget(corpId, briefId);
		AjaxJson json=new AjaxJson();
		Map<String, Object> attributes=new HashMap<>();
		attributes.put("isBudget", isBudget);
		json.setAttributes(attributes);
		json.setSuccess(true);
		return json;
	}
	/**
	 * 是否启用线上清算
	 * @return
	 */
	@PostMapping("/isEOnlinePay")
	@ResponseBody
	public AjaxJson isEOnlinePay(){
		AjaxJson json=new AjaxJson();
		boolean isEOnlinePay=abVoucherinfoViceService.isEOnlinePay();
		json.setSuccess(isEOnlinePay);
		return json;
	}
	/**
	 * 查询未挂接担保数据
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/guaranteeDatatable")
	@ResponseBody
	public DataTablesOutput guaranteeDataTable(@RequestBody DataTablesInput dataTablesInput){
		return abVoucherinfoViceService.listGuaranteeDataTable(dataTablesInput);
	}
}
