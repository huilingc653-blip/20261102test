package com.bytter.biz.wage.controller;
import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.wage.entity.NisWageDetail;
import com.bytter.biz.wage.service.NisWageDetailService;
import com.bytter.biz.wage.service.NisWageService;
import com.bytter.biz.wage.vo.NisWageDetailVO;
import com.bytter.biz.wage.vo.NisWageVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBisBifRuleService;
import com.bytter.system.api.dto.BisBifRuleDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**   
 * @Title: Controller  
 * @Description: NIS_WAGE_DETAIL
 * @author onlineGenerator
 * @date 2018-01-08 17:19:59
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/wage/niswagedetail")
public class NisWageDetailController extends BaseController {
	@Autowired
	private NisWageDetailService nisWageDetailService;
	@Autowired
	private NisWageService nisWageService;
	@DubboReference(check = false)
	private IBisBifRuleService bisBifRuleService;
	/**
	* NIS_WAGE_DETAIL列表 页面跳转
	*
	* @return
	*/
	@LogMessage(content = "查询NIS_WAGE_DETAIL列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/list")
	public String list(Model model) {
        return "wage/listNisWageDetail";
	}
    /**
    * NIS_WAGE_DETAIL table列表
    *
    * @return
    */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody  DataTablesInput dataTablesInput){
		return nisWageDetailService.getDataTablesUnion(dataTablesInput);
    }
	/**
	* NIS_WAGE_DETAIL编辑页面跳转
	*
	* @return
	*/
	@GetMapping("/update")
	public String update(NisWageDetail nisWageDetail, Model model) {
		if (StringUtils.isNotEmpty(nisWageDetail.getId())) {
			nisWageDetail = nisWageDetailService.get(nisWageDetail.getId());
			NisWageVO nisWage= nisWageService.getByIdUnion(nisWageDetail.getWageId());
			model.addAttribute("nisWageDetailPage", nisWageDetail);
			model.addAttribute("nisWagePage", nisWage);
			BisBifRuleDTO bisBifRuleDTO = bisBifRuleService.findByBifId(nisWage.getBifCode());
			String isMustCode = CommonConstant.YES;
			if (bisBifRuleDTO != null && StringUtils.isNotBlank(bisBifRuleDTO.getIsMustBankCode())) {
				isMustCode = bisBifRuleDTO.getIsMustBankCode();
			}
			model.addAttribute("isMustCode", isMustCode);
		}
		return "wage/updateNisWageDetail";
	}
	/**
	* 修改NIS_WAGE_DETAIL
	* @param nisWageDetail
	* @return
	*/
	@LogMessage(content = "修改NIS_WAGE_DETAIL",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
	@PostMapping("/modify")
	@ResponseBody
	public AjaxJson update(NisWageDetailVO nisWageDetail){
		nisWageDetailService.updateNisWageDetail(nisWageDetail, this.getCurrentUser());
		return  success();
	}
	/**
	 * NIS_WAGE详情页
	 *
	 * @return
	 */
	@LogMessage(content = "查询NIS_WAGE详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
	@GetMapping("/view/{wageId}/{opType}")
	public String view(@PathVariable("wageId") String wageId, @PathVariable("opType") String opType, Model model) {
		model.addAttribute("wageId", wageId);
		if(CommonConstant.NO.equals(opType)){
			//查询交易成功的明细
			model.addAttribute("voucherStat", BdlinkConstant.SUCCESS);
		}else if(CommonConstant.YES.equals(opType)) {
			//查询等待结果的明细
			model.addAttribute("voucherStat", BdlinkConstant.WAIT_RESULT);
		}else{
			//查询交易失败的明细
			model.addAttribute("voucherStat", BdlinkConstant.BANK_FAILED+CommonConstant.COMMA_SPLIT+BdlinkConstant.INTERFACE_FAILED);
		}
		return "wage/viewNisWageDetail";
	}
}
