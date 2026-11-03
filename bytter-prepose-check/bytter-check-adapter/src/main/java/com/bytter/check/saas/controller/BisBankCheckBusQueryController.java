package com.bytter.check.saas.controller;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.bytter.check.reconcile.service.BisBankEnterpriseCheckService;
import com.bytter.check.reconcile.service.BisTrackingService;
import com.bytter.check.reconcile.vo.BisBankEnterpriseCheckVo;
import com.bytter.check.vo.BisBankCheckBusQueryConfigVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.DateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Api(value = "对账结果查询",tags = "对账结果查询")
@RestController("v1BisBankCheckBusQueryController")
@RequestMapping("/v1-check/bis/bankBusQuery")
public class BisBankCheckBusQueryController extends BaseController{
   @Autowired
   private BisBankEnterpriseCheckService bisBankEnterpriseCheckService;


   @ApiOperation("跳转对账结果菜单 配置查询")
   @GetMapping("/getListConfig")
   public AjaxJson<BisBankCheckBusQueryConfigVO> getListConfig() {
      BisBankCheckBusQueryConfigVO configVO = new BisBankCheckBusQueryConfigVO();
      configVO.setDateStart(DateUtils.format(DateUtils.getLastMonthStartTime(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
      configVO.setDateEnd(DateUtils.format(DateUtils.getLastMonthEndTime(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
      return success(configVO);
   }

   @ApiOperation("对账结果查询Table列表")
   @PostMapping("/datatable")
   @ResponseBody
   public AjaxJson<DataTablesOutput<BisBankEnterpriseCheckVo>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
      DataTablesInput input = dataTablesInput.convertAndInitColumns();
      return success(bisBankEnterpriseCheckService.getCheckBusData(input));
   }
}
