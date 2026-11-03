package com.bytter.bill.web.bill.controller;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.domain.bill.service.AbBillBankAutoDtlService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoViceVO;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.GuaContractBillVO;
import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.biz.payment.api.service.INisBillheadService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.StringUtils;
import com.google.common.base.Joiner;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Controller
 * @Description: 票据从表信息
 * @author onlineGenerator
 * @date 2017-05-31 15:25:10
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abvoucherinfoVice")
public class AbVoucherinfoViceController extends BaseController {
	@Autowired
	private AbVoucherinfoViceService abVoucherinfoViceService;
	@Autowired
	private AbBillBankAutoDtlService abBillBankAutoDtlService;
	@DubboReference(check = false)
	private INisBillheadService nisBillheadService;
	/**
	 * 票据详细信息查看
	 * @return 前端路径
	 */
	@GetMapping("/accebillInfoPage")
	public String accebillInfoPage(AbVoucherinfoViceVO reVo, Model model) {
		if("1".equals(reVo.getIsEmitBill())){
//			reVo.setIsPayed("0");
			reVo.setEndorse(null);
		}else{
			reVo.setPayee(null);
			List<String> bifCodeList = abBillBankAutoDtlService.getBifCodeByAutoType(AccebillConstant.AB_CONFIG_ISPOOLENDORSE);
			String bifCodes=null;
			if(bifCodeList!=null && !bifCodeList.isEmpty()){
				bifCodes=Joiner.on(",").join(bifCodeList);
			}
			model.addAttribute("bifCodes", bifCodes);
		}
		model.addAttribute("params", reVo);
		return "bill/accebill/accebillInfoPage";
	}
	/**
	 * 单位持有应付票据信息
	 * @return 前端路径
	 */
	@GetMapping("/accebillInfoPayPage")
	public String accebillInfoPayPage(AbVoucherinfoViceVO reVo, Model model) {
		reVo.setEndorse(null);
		model.addAttribute("params", reVo);
		return "bill/accebill/accebillInfoPayPage";
	}
	/**
	 * 单位持有应收票据信息
	 * @return 前端路径
	 */
	@GetMapping("/accebillInfoReceivePage")
	public String accebillInfoReceivePage(AbVoucherinfoViceVO reVo, Model model) {
		reVo.setPayee(null);
		List<String> bifCodeList = abBillBankAutoDtlService.getBifCodeByAutoType(AccebillConstant.AB_CONFIG_ISPOOLENDORSE);
		String bifCodes=null;
		if(bifCodeList!=null && !bifCodeList.isEmpty()){
			bifCodes=Joiner.on(",").join(bifCodeList);
		}
		model.addAttribute("bifCodes", bifCodes);
		model.addAttribute("params", reVo);
		if(StringUtils.isNotEmpty(reVo.getPayId())){
			NisBillheadDTO nisBillheadDto = nisBillheadService.getNisBillheadById(reVo.getPayId());
			model.addAttribute("nisBillhead", nisBillheadDto);
		}
		return "bill/accebill/accebillInfoReceivePage";
	}
	/**
	 *列表显示票据信息
	 *@param dataTablesInput
	 *@return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/accebillInfoDatatable")
	@ResponseBody
	public DataTablesOutput accebillInfoDataTable(@RequestBody  DataTablesInput dataTablesInput){
		return abVoucherinfoViceService.listAccebillInfoDataTable(dataTablesInput);
	}
	/**
	 * 票据详细信息查看
	 * @return
	 */
	@GetMapping("/accebillViewPage")
	public String accebillViewPage(AbVoucherinfoViceVO reVo, Model model) {
		List<AbVoucherinfoViceVO> infoList = abVoucherinfoViceService.getAccebillDataViewList(reVo);
		model.addAttribute("infoList", infoList);
		model.addAttribute("isEmitBill", reVo.getIsEmitBill());
		//showPayMoney控制页面是否展示支付金额
		model.addAttribute("showPayMoney",(StringUtils.isBlank(reVo.getPayId()) ? "0" : "1"));
		return "bill/accebill/accebillViewPage";
	}
	/**
	 * 票据详细信息查看
	 * @return
	 */
	@PostMapping("/accebillDataView")
	@ResponseBody
	public DataTablesOutput accebillDataView(AbVoucherinfoViceVO reVo, Model model) {
		DataTablesOutput dataTablesOutput=new DataTablesOutput();
		if(StrUtil.isNotEmpty(reVo.getId())) {
			List<AbVoucherinfoViceVO> infoList = abVoucherinfoViceService.getAccebillDataViewList(reVo);
			if (CollectionUtil.isNotEmpty(infoList)) {
				dataTablesOutput.setData(infoList);
				dataTablesOutput.setRecordsTotal(infoList.size());
			}else{
				dataTablesOutput.setData(new ArrayList());
			}
		}else{
			dataTablesOutput.setData(new ArrayList());
		}
		return dataTablesOutput;
	}


	/**
	* 根据单位ID和业务类型判断是否挂预算
	* @param ids
	* @return
	*/
	@PostMapping("/isBudget")
	@ResponseBody
	public AjaxJson isBudget(String corpId,String briefId){
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
	public DataTablesOutput guaranteeDataTable(@RequestBody  DataTablesInput dataTablesInput){
		return abVoucherinfoViceService.listGuaranteeDataTable(dataTablesInput);
	}
	/**
	 * 获取挂接担保合同的票据数据
	 *
	 * @param dataTablesInput 查询参数
	 * @return 列表数据
	 */
	@ResponseBody
	@PostMapping("/getGuaContract")
	public DataTablesOutput<GuaContractBillVO> getUseChangelist(@RequestBody DataTablesInput dataTablesInput) {
		return abVoucherinfoViceService.getGuaForBill(dataTablesInput, getCurrentUser());
	}
}
