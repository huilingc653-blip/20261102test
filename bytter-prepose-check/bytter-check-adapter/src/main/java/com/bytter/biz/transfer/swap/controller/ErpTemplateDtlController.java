package com.bytter.biz.transfer.swap.controller;
import com.bytter.biz.transfer.swap.entity.ErpTemplateDtl;
import com.bytter.biz.transfer.swap.service.ErpTemplateDtlService;
import com.bytter.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
/**
 * Created by zhouj on 2018/5/27.
 */
@Controller
@RequestMapping("/erp/swap/templatedtl")
public class ErpTemplateDtlController extends BaseController {
    @Autowired
    private ErpTemplateDtlService erpTemplateDtlService;
    @GetMapping("/json/{id}")
    @ResponseBody
    public List<ErpTemplateDtl> json(@PathVariable("id") String id) {
        return erpTemplateDtlService.selectDtlByTypeId(id);
    }
    @GetMapping("/list/{id}")
    @ResponseBody
    public List<ErpTemplateDtl> list(@PathVariable("id") String id) {
        return erpTemplateDtlService.selectDtlMapById(id);
    }
}
