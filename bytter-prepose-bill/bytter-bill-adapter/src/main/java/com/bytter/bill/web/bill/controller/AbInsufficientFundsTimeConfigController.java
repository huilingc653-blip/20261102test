package com.bytter.bill.web.bill.controller;

import com.bytter.bill.domain.bill.entity.AbInsufficientFundsTimeConfig;
import com.bytter.bill.domain.bill.service.AbInsufficientFundsTimeConfigService;
import com.bytter.bill.domain.bill.vo.AbInsufficientFundsTimeConfigVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description 票据银行余额不足时间配置
 * @author zhous
 * @date 2025/6/24 10:30
 */
@Controller
@RequestMapping("/bill/accebill/bankInsufficientFundsTimeConfig")
public class AbInsufficientFundsTimeConfigController extends BaseController {

    @Autowired
    private AbInsufficientFundsTimeConfigService abInsufficientFundsTimeConfigService;

    /**
     * 票据银行余额不足时间配置 页面跳转
     *
     * @return
     */
    @GetMapping("/list")
    public String list(Model model) {
        return "bill/accebill/listAbInsufficientFundsTimeConfig";
    }

    /**
     * 票据银行余额不足时间配置查询
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "票据银行余额不足时间配置查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        return abInsufficientFundsTimeConfigService.dataTable(input);
    }

    /**
     * 手动新增票据银行余额不足时间配置
     *
     * @return
     */
    @GetMapping("/add")
    public String add() {
        return "bill/accebill/addAbInsufficientFundsTimeConfig";
    }

    /**
     * 编辑票据保证金利息
     *
     * @return
     */
    @GetMapping("/update")
    public String update(String id, Model model) {
        if(StringUtils.isNotBlank(id)){
            AbInsufficientFundsTimeConfig abInsufficientFundsTimeConfig = abInsufficientFundsTimeConfigService.get(id, true);
            model.addAttribute("info",abInsufficientFundsTimeConfig);
        }
        return "bill/accebill/updateAbInsufficientFundsTimeConfig";
    }

    /**
    * @Description 票据银行余额不足时间配置新增或修改操作
    * @author zhous
    * @date 2025/6/24 13:52
    */
    @LogMessage(content = "票据银行余额不足时间配置新增或修改操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/saveOrUpdate")
    @ResponseBody
    public AjaxJson saveOrUpdate(AbInsufficientFundsTimeConfigVO info, HttpServletRequest request) {
        abInsufficientFundsTimeConfigService.saveOrUpdate(info, this.getCurrentUser());
        return success();
    }

    /**
    * @Description 删除配置
    * @author zhous
    * @date 2025/6/24 14:10
    */
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        abInsufficientFundsTimeConfigService.deleteConfig(id, this.getCurrentUser());
        return success();
    }

    /**
     * 批量删除票据银行余额不足时间配置
     * @param ids
     * @return
     */
    @LogMessage(content = "批量删除票据银行余额不足时间配置",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson batchDelete(@RequestParam("ids") String ids){
        abInsufficientFundsTimeConfigService.batchDelete(ids, this.getCurrentUser());
        return success();
    }

    /**
     * 批量禁用
     * @param ids
     * @return
     */
    @LogMessage(content = "批量禁用",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/batchDisable")
    @ResponseBody
    public AjaxJson batchDisable(@RequestParam("ids") String ids){
        abInsufficientFundsTimeConfigService.batchDisable(ids, this.getCurrentUser());
        return success();
    }

}
