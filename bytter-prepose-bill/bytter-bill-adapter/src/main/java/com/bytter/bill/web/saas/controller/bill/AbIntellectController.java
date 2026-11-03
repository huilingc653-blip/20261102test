package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.service.AbBillBankAutoDtlService;
import com.bytter.bill.domain.bill.service.AbIntellectService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.bill.vo.AbBillPayAndReceVO;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoViceVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "/v1-preposebill/bill/accebill/intellect",tags = "智能选票")
@RestController("v1AbIntellectController")
@RequestMapping("/v1-preposebill/bill/accebill/intellect")
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
    @PostMapping("/accebillIntellectPage")
    public AjaxJson< Map<String,Object>> accebillIntellectPage(@RequestBody AbVoucherinfoViceVO reVo) {
         Map<String,Object> model=new HashMap<>();
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
            model.put("bifCodes", null);
        }
        model.put("params", reVo);
        return AjaxJson.success(model);
    }
	
	/**
	 *列表显示票据信息
	 *@param dataTablesInput
	 *@return
	 */
	@ApiOperation("列表显示票据信息")
	@PostMapping("/accebillIntellectDatatable")
	public AjaxJson<DataTablesOutput<AbVoucherinfoViceVO> > accebillIntellectDataTable(@RequestBody V1DataTablesInput dataTablesInput){
        DataTablesInput input=dataTablesInput.convertAndInitColumns();
		return AjaxJson.success(abIntellectService.listAccebillInfoDataTable(input));
	}
	/**
	 * 票据详细信息查看
	 * @return
	 */
    @ApiOperation("票据详细信息查看")
	@PostMapping("/accebillViewPage")
	public AjaxJson<List<AbVoucherinfoViceVO> > accebillViewPage(@RequestBody AbVoucherinfoViceVO reVo) {
		return AjaxJson.success(abVoucherinfoViceService.listAccebillInfoView(reVo));
	}
	/**
	* 根据单位ID和业务类型判断是否挂预算
	* @param corpId
	* @return
	*/
    @ApiOperation("根据单位ID和业务类型判断是否挂预算")
	@PostMapping("/isBudget")
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
    @ApiOperation("是否启用线上清算")
	@PostMapping("/isEOnlinePay")
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
    @ApiOperation("查询未挂接担保数据")
	@PostMapping("/guaranteeDatatable")
	public AjaxJson<DataTablesOutput<AbBillPayAndReceVO>> guaranteeDataTable(@RequestBody V1DataTablesInput dataTablesInput){
         DataTablesInput input=dataTablesInput.convertAndInitColumns();
		return AjaxJson.success(abVoucherinfoViceService.listGuaranteeDataTable(input));
	}

}
