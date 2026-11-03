package com.bytter.increment.web.controller;
import com.bytter.increment.domain.entity.ChoProductNetVal;
import com.bytter.increment.domain.service.ChoProductNetValService;
import com.bytter.increment.domain.service.ChoProductReqService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * 理财产品净值维护Controller
 *
 * @author ouyj
 */
@Controller
@RequestMapping("/chowmatistic/choproductNetVal")
public class ChoProductNetValController extends BaseController {
    @Autowired
    private ChoProductNetValService choProductNetValService;
    @Autowired
    private ChoProductReqService choProductReqService;
    @GetMapping("/list")
    public String list(Model model) {
        return "chowmatistic/listChoProductNetVal";
    }
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return choProductNetValService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }
    @PostMapping("/editDataTable")
    @ResponseBody
    public DataTablesOutput editDataTable(@RequestBody DataTablesInput dataTablesInput) {
        return choProductNetValService.getEditDataTable(dataTablesInput);
    }
    @GetMapping("/add")
    public String add(ChoProductNetVal choProductNetVal, Model model) {
        choProductNetValService.addNetVal(choProductNetVal, model);
        return "chowmatistic/addChoProductNetVal";
    }
    @GetMapping("/updateNetVal")
    public String updateNetVal(ChoProductNetVal choProductNetVal, Model model) {
        return choProductNetValService.updateNetVal(choProductNetVal, model);
    }
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(ChoProductNetVal choProductNetVal) {
        choProductNetVal.setCreateBy(this.getCurrentUser().getLoginName());
        choProductNetVal.setCreateDate(DateUtils.getDate());
        choProductNetValService.save(choProductNetVal);
        return success();
    }
    @GetMapping("/update")
    public String update(ChoProductNetVal choProductNetVal, Model model) {
        return choProductNetValService.toUpdate(choProductNetVal, model);
    }
    @GetMapping("/view")
    public String view(ChoProductNetVal choProductNetVal, Model model) {
        if (StringUtils.isNotEmpty(choProductNetVal.getId())) {
            choProductNetVal = choProductNetValService.get(choProductNetVal.getId());
            model.addAttribute("choProductPage", choProductNetVal);
        }
        return "chowmatistic/updateChoProduct";
    }
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(ChoProductNetVal choProductNetVal) {
        this.choProductNetValService.modify(choProductNetVal, this.getCurrentUser());
        return success();
    }
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        this.choProductNetValService.delete(id);
        return success();
    }
    @PostMapping("/queryRedeemBal")
    @ResponseBody
    public AjaxJson queryRedeemBal(@RequestBody ChoProductNetVal choProductNetVal, Model model) {
        return this.choProductNetValService.queryRedeemBal(choProductNetVal);
    }
}
