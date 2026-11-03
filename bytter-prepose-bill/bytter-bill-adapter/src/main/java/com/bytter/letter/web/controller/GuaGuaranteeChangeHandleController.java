package com.bytter.letter.web.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.letter.domain.service.GuaGuaranteeChangeHandleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
/**
 * 
 * @author luxy
 * @version 1.0
 * @Title:GuaGuaranteeChangeHandleController
 * @Description:保函更改办理控制层
 * @date 2019年1月14日 上午11:49:17
 */
@Controller
@RequestMapping("/letter/guarantee/guaGuaranteeChangeHandle")
public class GuaGuaranteeChangeHandleController extends BaseController {
    @Autowired
    private GuaGuaranteeChangeHandleService LgChangeHandleService;
    /**
     * 保函申请更改办理数据列表页面
     */
    @GetMapping("/list")
    public String list() {
        return "cms/guarantee/listGuaGuaranteeChangeHandle";
    }
    /**
     * 查询保函申请更改可办理数据列表
     */
    @LogMessage(content = "查询保函申请更改可办理数据列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput datatable(@RequestBody DataTablesInput input) {
        return LgChangeHandleService.datatableList(input, this.getCurrentUser());
    }
    /**
     * 完成更改
     */
    @LogMessage(content = "执行保函申请更改操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/submit/{id}")
    @ResponseBody
    public AjaxJson submit(@PathVariable String id) {
        LgChangeHandleService.submit(id, this.getCurrentUser());
        return success();
    }
    /**
     * 拒绝更改
     */
    @LogMessage(content = "执行拒绝保函申请更改操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/refuse/{id}")
    @ResponseBody
    public AjaxJson refuse(@PathVariable String id) {
        LgChangeHandleService.refuse(id, this.getCurrentUser());
        return success();
    }
}
