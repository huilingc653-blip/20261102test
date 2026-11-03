package com.bytter.check.web.controller;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.bytter.check.reconcile.service.BisBankEnterpriseCheckService;
import com.bytter.check.reconcile.service.BisTrackingService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/bis/bankBusQuery")
public class BisBankCheckBusQueryController extends BaseController{
   @Autowired
   private BisBankEnterpriseCheckService bisBankEnterpriseCheckService;
   @Autowired
   private BisTrackingService bisTrackingService;
   //////@RequiresPermissions(ReceiptConstant.BIS_CHECK_BUS_QUERY)
   @GetMapping("/list")
   public String list(Model model) {
	   model.addAttribute("dateStart", DateUtils.format(DateUtils.getLastMonthStartTime(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
       model.addAttribute("dateEnd", DateUtils.format(DateUtils.getLastMonthEndTime(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
	   return "reconcile/listBisBankchekQuery";
   }
   @SuppressWarnings("rawtypes")
   @PostMapping("/datatable")
   @ResponseBody
   //////@RequiresPermissions(ReceiptConstant.BIS_CHECK_BUS_QUERY)
   public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
       return bisBankEnterpriseCheckService.getCheckBusData(dataTablesInput);
   }
}
