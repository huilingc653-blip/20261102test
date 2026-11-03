package com.bytter.biz.reconcile.controller;
import com.bytter.biz.reconcile.service.BisBankMonthStatementDtlService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
@Controller
@RequestMapping("/bisBank/bisBankMonthStatementDtl")
public class BisBankMonthStatementDtlController extends BaseController {
    @Autowired
    private BisBankMonthStatementDtlService bisBankMonthStatementDtlService;
    /**
     * 对账单查询
     * @param model
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BIS_BISBANKMONTHSTATEMENTDTL_QUERY)
    @GetMapping("/list")
    public String list(Model model) {
        return "reconcile/listBisBankMonthStatementDtl";
    }
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    //////@RequiresPermissions(ResourceConstant.NETBANK_BIS_BISBANKMONTHSTATEMENTDTL_QUERY)
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return bisBankMonthStatementDtlService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }
    /**
     * 预览
     *
     * @return
     */
    @GetMapping("/view")
    public String view(String id, Model model) {
        model.addAttribute("id", id);
        return "reconcile/viewBisBankMonthStatementDtl";
    }
    /**
     * 预览页面加载图片
     *
     * @return
     */
    @GetMapping("/initImg")
    public void initImg(String id, HttpServletRequest request, HttpServletResponse response) {
        this.bisBankMonthStatementDtlService.initImgV2(id,request,response);
    }
    /**
     * 列表打印
     *
     * @return
     */
    @GetMapping("/print")
    public String print(String id, Model model) {
        model.addAttribute("id", id);
        return "reconcile/printBisBankMonthStatementDtl";
    }
    /**
     * 批量预览
     *
     * @return
     */
    @GetMapping("/batchPreview")
    public String batchPreview(String ids, Model model) {
        List<String> listId = new ArrayList<>();
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            listId.add(id);
        }
        model.addAttribute("listId", listId);
        return "reconcile/viewBisBankMonthStatementDtlBatch";
    }
    /**
     * 批量打印
     *
     * @return
     */
    @GetMapping("/batchPrint")
    public String batchPrint(String ids, Model model) {
        List<String> listId = new ArrayList<>();
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            listId.add(id);
        }
        model.addAttribute("listId", listId);
        return "reconcile/printBisBankMonthStatementDtlBatch";
    }
    /**
     * 预览页面加载图片
     *
     * @return
     */
    @GetMapping("/download")
    public void download(String id, HttpServletRequest request, HttpServletResponse response) {
        bisBankMonthStatementDtlService.download(id, request, response);
    }
    /**
     * 批量预览
     *
     * @return
     */
    @RequestMapping("/batchDownload")
    public void batchDownload(String ids, HttpServletRequest request, HttpServletResponse response) {
        bisBankMonthStatementDtlService.batchDownload(ids, request, response);
    }
}
