package com.bytter.biz.transfer.swap.controller;
import com.bytter.biz.transfer.swap.service.ErpTemplateDatadefineService;
import com.bytter.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * Created by zhouj on 2018/5/18.
 * 模板类型凭证头和明细
 */
@Controller
@RequestMapping("/erp/swap/templatedatadefine")
public class ErpTemplateDatadefineController extends BaseController {
    @Autowired
    private ErpTemplateDatadefineService templateDatadefineService;
    @GetMapping("/json")
    @ResponseBody
    public Object json(boolean flag) {
        return templateDatadefineService.listDataByDtl(flag);
    }
}
