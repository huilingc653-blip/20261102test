package com.bytter.system.web.controller;
import com.bytter.system.service.SdCertService;
import com.bytter.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@RequestMapping("/sys/sdCert")
public class SdCertController extends BaseController {
    @Autowired
    private SdCertService sdCertService;
    @PostMapping("/check")
    @ResponseBody
    public Boolean check(String serialNumber, String source, String signUserData, String userCode) {
        return sdCertService.check(serialNumber, source, signUserData, userCode);
    }
}
