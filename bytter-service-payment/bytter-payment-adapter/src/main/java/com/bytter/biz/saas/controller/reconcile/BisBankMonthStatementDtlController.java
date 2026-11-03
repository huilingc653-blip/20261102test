package com.bytter.biz.saas.controller.reconcile;

import com.bytter.biz.bdlink.vo.BisBankMonthStatementDtlVO;
import com.bytter.biz.reconcile.service.BisBankMonthStatementDtlService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Api(value = "对账单查询",tags = "对账单查询")
@RestController("v1BisBankMonthStatementDtlController")
@RequestMapping("/v1-payment/bisBank/bisBankMonthStatementDtl")
public class BisBankMonthStatementDtlController extends BaseController {

    @Autowired
    private BisBankMonthStatementDtlService bisBankMonthStatementDtlService;

    @ApiOperation("对账单查询 Table")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BisBankMonthStatementDtlVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(bisBankMonthStatementDtlService.getDataTablesUnion(input, this.getCurrentUser()));
    }

    /**
     * 预览页面加载图片
     *
     * @return
     */
    @ApiOperation(value = "预览页面加载图片",notes = "无返回值")
    @GetMapping("/initImg")
    public void initImg(String id, HttpServletRequest request, HttpServletResponse response) {
        this.bisBankMonthStatementDtlService.initImgV2(id,request,response);
    }

    /**
     * 下载
     *
     * @return
     */
    @ApiOperation("下载")
    @GetMapping("/download")
    public void download(String id, HttpServletRequest request, HttpServletResponse response) {
        bisBankMonthStatementDtlService.download(id, request, response);
    }
    /**
     * 批量下载
     *
     * @return
     */
    @ApiOperation("批量下载")
    @PostMapping("/batchDownload")
    public void batchDownload(String ids, HttpServletRequest request, HttpServletResponse response) {
        bisBankMonthStatementDtlService.batchDownload(ids, request, response);
    }
}
