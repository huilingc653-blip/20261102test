package com.bytter.increment.web.controller;
import com.bytter.increment.domain.entity.ChoProductRenewal;
import com.bytter.increment.domain.service.ChoProductRenewalService;
import com.bytter.increment.api.constant.ChoMessageConstant;
import com.bytter.increment.domain.vo.ChoProductRedeemVo;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * 理财续期Controller
 *
 * @author ouyj
 */
@Controller
@RequestMapping("/chowmatistic/choproductRenewal")
public class ChoProductRenewalController extends BaseController {
    @Autowired
    private ChoProductRenewalService choProductRenewalService;
    @GetMapping("/list")
    public String list() {
        return "chowmatistic/listChoProductRenewal";
    }
    /**
     * 查询理财续期列表
     * @param dataTablesInput 前端传参
     * @return 列表数据
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return choProductRenewalService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }
    @GetMapping("/select")
    public String selectProduct(Model model){
        model.addAttribute("isRenewal", 1);
        return "chowmatistic/selectChoProductRadio";
    }
    @PostMapping("/datatableForSelect")
    @ResponseBody
    public DataTablesOutput dataTableForSelect(@RequestBody DataTablesInput dataTablesInput) {
        return choProductRenewalService.getDataTablesUnionForSelect(dataTablesInput, this.getCurrentUser());
    }
    @GetMapping("/add")
    public String add(ChoProductRenewal entity, Model model) {
        if (StringUtils.isNotEmpty(entity.getId())) {
            entity = choProductRenewalService.get(entity.getId());
            model.addAttribute("choProductRedeemPage", entity);
        }
        return "chowmatistic/addChoProductRenewal";
    }
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(ChoProductRenewal entity) {
        choProductRenewalService.saveChoProductRedeem(entity, getCurrentUser());
        return success();
    }
    @PostMapping("/batchSubmit")
    @ResponseBody
    public AjaxJson batchSubmit(@RequestParam("ids") String ids) {
        choProductRenewalService.batchSubmit(ids, this.getCurrentUser());
        return success();
    }
    @GetMapping("/update")
    public String update(ChoProductRenewal choProductReq, Model model) {
        if (StringUtils.isNotEmpty(choProductReq.getId())) {
            ChoProductRedeemVo choProductReqVo = choProductRenewalService.getChoProductRedeemVoById(choProductReq.getId());
            model.addAttribute("choProductRedeemPage", choProductReqVo);
        }
        return "chowmatistic/updateChoProductRenewal";
    }
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(ChoProductRenewal entity) {
        choProductRenewalService.toModify(entity, getCurrentUser());
        return success();
    }
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        ChoProductRenewal entity = choProductRenewalService.get(id, true);
        this.choProductRenewalService.deleteEntity(entity);
        return success();
    }
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            ChoProductRedeemVo choProductReqVo = choProductRenewalService.getChoProductRedeemVoById(id);
            model.addAttribute("choProductRedeemPage", choProductReqVo);
            model.addAttribute("busType", ChoMessageConstant.BUS_TYPE_PRODUCT_RENEWAL);
        }
        return "chowmatistic/viewChoProductRenewal";
    }
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        return "chowmatistic/listChoProductRenewalApprove";
    }
    /**
     * 跳转至审核详情界面
     *
     * @param id
     * @param taskId
     * @param model
     * @return
     */
    @GetMapping("/approve")
    public String approve(String id, String taskId, Model model) {
        ChoProductRedeemVo vo = new ChoProductRedeemVo();
        if (StringUtils.isNotEmpty(id)) {
            vo = this.choProductRenewalService.getChoProductRedeemVoById(id);
            model.addAttribute("choProductRedeemPage", vo);
            model.addAttribute("taskId", taskId);
            model.addAttribute("busType", ChoMessageConstant.BUS_TYPE_PRODUCT_REDEEM);
        }
        return "chowmatistic/approveChoProductRenewal";
    }
    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
        return this.choProductRenewalService.dataTableApprove(dataTablesInput);
    }
}
