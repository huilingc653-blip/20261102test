package com.bytter.report.web.cms.controller;

import com.bytter.report.domain.cms.service.CmsLeaseReportService;
import com.bytter.report.domain.cms.vo.CmsLeaseReportVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.system.api.ICorpService;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cms/report/cmsLease")
public class CmsLeaseReportController extends BaseController {
    @Autowired
    private ICorpService corpService;

    @Autowired
    private CmsLeaseReportService cmsLeaseReportService;
    /**
     * 融资租赁申请列表-页面跳转
     */
    //@RequiresPermissions(ResourceConstant.CMS_LEASE_REPORT_VIEW)
    @LogMessage(content = "融资租赁统计表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "cms/report/cmsLease/listLeaseReportInfo";
    }

    //@RequiresPermissions(ResourceConstant.CMS_LEASE_REPORT_VIEW)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        DataTablesOutput output = cmsLeaseReportService.getDatatables(input,getCurrentUser());
        return output;
    }


    @LogMessage(content = "跳转融资租赁明细列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{leaseId}")
    public String viewList(@PathVariable("leaseId") String leaseId, Model model) {
        if (StringUtils.isNotEmpty(leaseId)) {
            CmsLeaseReportVO cmsLeaseReportVO = cmsLeaseReportService.getByLeaseId(leaseId);
            if(!EmptyUtils.isEmpty(cmsLeaseReportVO)) {
                model.addAttribute("cmsLeaseReportVO",cmsLeaseReportVO);
            }
        }
        return "cms/report/cmsLease/viewListLeaseDetail";
    }

    //@RequiresPermissions(ResourceConstant.CMS_LEASE_REPORT_VIEW)
    @PostMapping("/detailDatatable")
    @ResponseBody
    public DataTablesOutput detailDataTable(@RequestBody DataTablesInput input,String leaseId) {
        DataTablesOutput output = cmsLeaseReportService.getDetailDatatables(input,leaseId);
        return output;
    }
}
