package com.bytter.biz.transfer.swap.controller;
import com.bytter.biz.transfer.swap.entity.ErpTemplateTypeDtl;
import com.bytter.biz.transfer.swap.service.ErpTemplateTypeDtlService;
import com.bytter.biz.transfer.swap.vo.ErpTemplateTypeDtlVO;
import com.bytter.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
/**
 * Created by zhouj on 2018/5/19.
 */
@Controller
@RequestMapping("/erp/swap/templatetypedtl")
public class ErpTemplateTypeDtlController extends BaseController {
    @Autowired
    private ErpTemplateTypeDtlService templateTypeDtlService;
    @GetMapping("/json/{id}")
    @ResponseBody
    public List<ErpTemplateTypeDtlVO> json(@PathVariable("id") String id) {
        return templateTypeDtlService.selectTypeDtlByTypeId(id);
    }
    @GetMapping("/show/{id}")
    @ResponseBody
    public List<ErpTemplateTypeDtl> show(@PathVariable("id") String id, boolean ishow) {
        return templateTypeDtlService.selectShowTypeDtlByTypeId(id,ishow);
    }
}
