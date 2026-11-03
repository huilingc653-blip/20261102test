package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.domain.billPool.service.AbPoolQueryExcService;
import com.bytter.bill.domain.billPool.vo.AbPoolExcVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * @author zhenghj
 * @version 1.0
 * @Title: 票据池指令Controller
 * @Description: 据池指令控制器类
 * @date 2020-11-12
 */
@Controller
@RequestMapping("/billpool/queryExc")
public class AbPoolQueryExcController extends BaseController {
    @Autowired
    private AbPoolQueryExcService abPoolQueryExcService;
    @PostMapping("/manualExc")
    @ResponseBody
    public AjaxJson manualExc(Model model, AbPoolExcVO bean){
        AjaxJson ajaxJson = success();
        try {
            abPoolQueryExcService.manualSynchronize(bean,this.getCurrentUser());
            ajaxJson.setSuccess(true);
        } catch (RuntimeException e) {
            ajaxJson.setSuccess(false);
        }
        return ajaxJson;
    }
}
