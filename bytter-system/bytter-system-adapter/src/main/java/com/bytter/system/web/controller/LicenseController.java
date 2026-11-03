package com.bytter.system.web.controller;

import com.bytter.system.service.LicenseService;
import com.bytter.core.base.BaseController;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by zhoul on 2017/12/14.
 */
@Controller
@RequestMapping("/sys/license")
public class LicenseController extends BaseController {

    @Resource
    private LicenseService licenseService;

    //    该页面方法不用考虑垂直越权的控制，不然会导致license过期时无法跳转页面修改
    @PostMapping("/update")
    @ResponseBody
    public Object update(@RequestParam(value = "license") MultipartFile license) {
        try {
            InputStream inputStream = license.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            String licenseContext = licenseService.getLicenseContext(inputStreamReader);
            licenseService.initLicense(licenseContext);
            licenseService.removeAll();
            licenseService.updateLicenseFile(licenseContext);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success();
    }

    @GetMapping("/detail")
    public void detail(Model model) {
        Map<String, Object> licenseNode = licenseService.getLicenseCache();
        Long endDate = (Long) licenseNode.get("endDate");
        if (!EmptyUtils.isEmpty(endDate)) {
            try {
                Date _endDate = DateUtils.getDate(endDate);
                Date dueDate = DateUtils.str2Date("2099-01-01", new SimpleDateFormat("yyyy-MM-dd"));
                if (DateUtils.compareDate(_endDate, dueDate) >= 0) {
                    String endDateStr = "永久授权";
                    model.addAttribute("_endDate", endDateStr);
                }
            } catch (Exception e) {
            }
        } else {
            String endDateStr = "永久授权";
            model.addAttribute("_endDate", endDateStr);
        }
        model.addAttribute("licenseNode", licenseNode);
        long now = System.currentTimeMillis();
        model.addAttribute("licenseStatus", endDate == null ? false : (now < endDate));
    }
}
