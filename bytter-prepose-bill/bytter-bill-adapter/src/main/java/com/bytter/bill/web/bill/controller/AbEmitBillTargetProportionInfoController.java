package com.bytter.bill.web.bill.controller;

import com.bytter.bill.domain.bill.entity.AbEmitBillTargetProportionInfo;
import com.bytter.bill.domain.bill.service.AbEmitBillTargetProportionInfoService;
import com.bytter.bill.domain.bill.vo.AbEmitBillTargetProportionInfoVO;
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
import java.util.List;

/**
* @Description 银承开票占比目标维护
* @author zhous
* @date 2025/4/22 10:30
*/
@Controller
@RequestMapping("/bill/accebill/billTargetProportion")
public class AbEmitBillTargetProportionInfoController extends BaseController {

    @Autowired
    private AbEmitBillTargetProportionInfoService abEmitBillTargetProportionInfoService;

    /**
     * 票据保证金利息维护列表 页面跳转
     *
     * @return
     */
    @GetMapping("/list")
    public String list(Model model) {
        return "bill/accebill/listAbEmitBillTargetProportionInfo";
    }

    /**
     * 票据保证金利息维护列表查询
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "银承开票占比目标维护列表查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        return abEmitBillTargetProportionInfoService.dataTable(input);
    }

    /**
     * 手动新增票据保证金利息
     *
     * @return
     */
    @GetMapping("/add")
    public String add() {
        return "bill/accebill/addAbEmitBillTargetProportionInfo";
    }

    /**
     * 编辑票据保证金利息
     *
     * @return
     */
    @GetMapping("/update")
    public String update(String id, Model model) {
        if(StringUtils.isNotBlank(id)){
            AbEmitBillTargetProportionInfo abEmitBillTargetProportionInfo = abEmitBillTargetProportionInfoService.get(id, true);
            model.addAttribute("info",abEmitBillTargetProportionInfo);
        }
        return "bill/accebill/updateAbEmitBillTargetProportionInfo";
    }

    /**
     * @Description 银承开票占比目标维护新增或修改操作
     * @author zhous
     * @date 2025/4/22 13:34
     */
    @LogMessage(content = "银承开票占比目标新增或修改操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/saveOrUpdate")
    @ResponseBody
    public AjaxJson saveOrUpdate(AbEmitBillTargetProportionInfoVO info, HttpServletRequest request) {
        abEmitBillTargetProportionInfoService.saveOrUpdate(info, this.getCurrentUser());
        return success();
    }

    /**
    * @Description 删除记录
    * @author zhous
    * @date 2025/3/6 9:49
    */
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        abEmitBillTargetProportionInfoService.deleteById(id, this.getCurrentUser());
        return success();
    }

    /**
     * 导入银承开票占比目标维护
     *
     * @param list
     * @return
     */
    @LogMessage(content = "导入银承开票占比目标维护信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<AbEmitBillTargetProportionInfoVO> list) {
        abEmitBillTargetProportionInfoService.saveImport(list, this.getCurrentUser());
        return success();
    }

}
