package com.bytter.biz.ttpay.controller;
import com.bytter.biz.ttpay.service.TpQuotaQueryService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.biz.payment.api.constant.TtPayConstant;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * @author mof
 * @version 1.0
 * @Title: Controller
 * @Description: 限额查询对应Controller
 * @date 2022-05-05 15:43:05
 */
@Controller
@RequestMapping("/ttpay/quotaQuery")
public class TpQuotaQueryController extends BaseController {
    @Autowired
    private TpQuotaQueryService  tpQuotaQueryService;
    /**
     * 限额查询列表页面
     *
     * @param model
     * @return
     */
    @GetMapping("/list")
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_QUOTAQUERY_QUERY)
    public String list(Model model) {
        return "ttpay/listTpQuotaQuery";
    }
    /**
     * 限额查询列表页面
     *
     * @return
     */
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_QUOTAQUERY_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return tpQuotaQueryService.getDataTablesUnion(dataTablesInput);
    }
}
