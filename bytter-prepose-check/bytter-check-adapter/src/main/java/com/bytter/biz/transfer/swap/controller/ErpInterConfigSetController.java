package com.bytter.biz.transfer.swap.controller;
import com.bytter.biz.transfer.swap.service.ErpInterConfigSetService;
import com.bytter.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * Created by zhouj on 2018/5/28.
 */
@Controller
@RequestMapping("/erp/swap/interconfigset")
public class ErpInterConfigSetController extends BaseController {
    @Autowired
    private ErpInterConfigSetService erpInterConfigSetService;
}
