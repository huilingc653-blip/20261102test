package com.bytter.biz.foreign.controller;

import com.bytter.biz.foreign.entity.FexPayerInfo;
import com.bytter.biz.foreign.service.FexPayerInfoService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author huangx
 * @version 1.0
 * @Title: Service
 * @Description: 汇款人信息表
 * @date 2019-07-15 15:20:33
 */
@Controller
@RequestMapping("/foreign/fexpayerinfo")
public class FexPayerInfoController extends BaseController {
    @Autowired
    private FexPayerInfoService fexPayerInfoService;

    /**
     * 根据汇款账号获取汇款人信息
     *
     * @param payerAcc
     * @return
     */
    @RequestMapping("/getFexPayerInfo")
    @ResponseBody
    public AjaxJson getFexPayerInfo(String payerAcc) {
        FexPayerInfo fexPayerInfo = fexPayerInfoService.getByPayerAcc(payerAcc);
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        j.setObj(fexPayerInfo);
        return j;
    }
}
