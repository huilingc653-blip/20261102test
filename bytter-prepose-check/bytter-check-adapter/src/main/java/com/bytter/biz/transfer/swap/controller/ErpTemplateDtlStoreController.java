package com.bytter.biz.transfer.swap.controller;
import com.bytter.biz.transfer.swap.entity.ErpTemplateDtlStore;
import com.bytter.biz.transfer.swap.service.ErpTemplateDtlStoreService;
import com.bytter.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
/**
 * Created by zhouj on 2018/5/29.
 */
@Controller
@RequestMapping("/erp/swap/templatedtlstore")
public class ErpTemplateDtlStoreController extends BaseController {
    @Autowired
    private ErpTemplateDtlStoreService templateDtlStoreService;
    
    @GetMapping("/list/{id}")
    @ResponseBody
    public List<ErpTemplateDtlStore> list(@PathVariable("id") String id) {
        return templateDtlStoreService.selectDtlMapById(id);
    }
}
