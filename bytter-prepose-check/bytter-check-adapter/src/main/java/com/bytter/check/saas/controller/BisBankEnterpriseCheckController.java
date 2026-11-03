package com.bytter.check.saas.controller;

import com.bytter.check.reconcile.service.BisBankEnterpriseCheckService;
import com.bytter.check.reconcile.vo.BisBankEnterpriseCheckVo;
import com.bytter.check.vo.BisBankEnterpriseCheckListConfigVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.DateUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 银企对账
 *
 */
@Api(value = "银企对账余额对账",tags = "银企对账余额对账")
@RestController("v1BisBankEnterpriseCheckController")
@RequestMapping("/v1-check/bis/bankEnterprise")
public class BisBankEnterpriseCheckController extends BaseController{
	@Autowired
   private BisBankEnterpriseCheckService bisBankEnterpriseCheckService;
   @DubboReference(check = false)
	private IBtCurrencyService iBtCurrencyService;

   @ApiOperation("列表页的配置信息")
   @GetMapping("/getListConfig")
   @ResponseBody
   public AjaxJson<BisBankEnterpriseCheckListConfigVO> getListConfig() {
	   BisBankEnterpriseCheckListConfigVO configVO = new BisBankEnterpriseCheckListConfigVO();
	   //获取人民币
	   BtCurrencyDTO rmbCur = iBtCurrencyService.getRMBCurrency();
	   //人民币id
	   configVO.setCurId(rmbCur.getId());
	   // 默认上个月 yyyy-MM
	   configVO.setDateStart(DateUtils.formatDate(DateUtils.addMonth(DateUtils.getDateYMD(), -1)).substring(0,7));
	   configVO.setDateEnd(DateUtils.format(DateUtils.getLastMonthEndTime(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
	   return success(configVO);
   }
   @ApiOperation("银企余额对账")
   @PostMapping("/datatable")
   @ResponseBody
   public AjaxJson<DataTablesOutput<BisBankEnterpriseCheckVo>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
	   DataTablesInput input = dataTablesInput.convertAndInitColumns();
	   return success(bisBankEnterpriseCheckService.getDataTablesUnion(input));
   }
   @ApiOperation("手动同步")
   @PostMapping("/manualExc")
   @ResponseBody
   public AjaxJson manualExc(BisBankEnterpriseCheckVo vo){
		AjaxJson ajaxJson = success();
		try {
			bisBankEnterpriseCheckService.manualSynchronize(vo,this.getCurrentUser());
			ajaxJson.setSuccess(true);
		} catch (RuntimeException e) {
			ajaxJson.setSuccess(false);
		}
		return ajaxJson;
   }

	@ApiOperation("/optSure接口")
    @PostMapping("/optSure")
	@ResponseBody
	public AjaxJson optSure(BisBankEnterpriseCheckVo bean) {
		return bisBankEnterpriseCheckService.opSure(bean, this.getCurrentUser());
	}
}
