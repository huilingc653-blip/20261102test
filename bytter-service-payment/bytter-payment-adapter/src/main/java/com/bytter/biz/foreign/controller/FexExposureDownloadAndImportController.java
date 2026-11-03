package com.bytter.biz.foreign.controller;

import com.bytter.biz.foreign.service.FexExposureDownloadAndImportService;
import com.bytter.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Title: Controller
 * @Description: 单位敞口上报导入/导出
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/foreign/fexExposureDownloadAndImport")
public class FexExposureDownloadAndImportController extends BaseController {

    @Autowired(required=false)
    private FexExposureDownloadAndImportService fexExposureDownloadAndImportService;

    /**
     * 下载敞口上报模板
     *
     * @param request
     * @param response
     * @param corpId
     * @param templateId
     * @param curId
     * @throws Exception
     */
    @GetMapping("/downloadExposure")
    public void downloadExposure(HttpServletRequest request, HttpServletResponse response, String corpId, String templateId, String curId) throws Exception {
        this.fexExposureDownloadAndImportService.downloadExposure(request, response, corpId, templateId, curId);
    }
}
