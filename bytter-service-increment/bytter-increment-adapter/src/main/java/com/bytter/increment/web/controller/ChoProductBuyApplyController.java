package com.bytter.increment.web.controller;
import com.bytter.core.utils.JsonUtils;
import com.bytter.increment.domain.entity.ChoProductReq;
import com.bytter.increment.domain.service.ChoProductBuyApplyService;
import com.bytter.increment.domain.service.ChoProductReqService;
import com.bytter.increment.domain.vo.ChoProductReqVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * 理财产量购买申请Controller
 *
 * @author ouyj
 */
@Controller
@RequestMapping("/chowmatistic/choproductBuyApply")
public class ChoProductBuyApplyController extends BaseController {
    @Autowired
    private ChoProductBuyApplyService choProductBuyApplyService;
    @Autowired
    private ChoProductReqService choProductReqService;
    @GetMapping("/list")
    public String list(Model model) {
        return "chowmatistic/listChoProductBuyApply";
    }
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return choProductBuyApplyService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }
    @GetMapping("/add")
    public String add(ChoProductReq choProductReq, Model model) {
        return "chowmatistic/addChoProductBuyApply";
    }
    @GetMapping("/update")
    public String update(ChoProductReq choProductReq, Model model) {
        if (StringUtils.isNotEmpty(choProductReq.getId())) {
            ChoProductReqVo choProductReqVo = choProductReqService.getChoProductReqVoById(choProductReq.getId());
            model.addAttribute("choProductReqPage", choProductReqVo);
        }
        return "chowmatistic/updateChoProductBuyApply";
    }
    @PostMapping("/batchSubmit")
    @ResponseBody
    public AjaxJson batchSubmit(@RequestParam("ids") String ids){
        choProductBuyApplyService.batchSubmit(ids, this.getCurrentUser());
        return success();
    }
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(ChoProductReq choProduct) {
        choProduct.setCreateBy(this.getCurrentUser().getLoginName());
        choProduct.setCreateDate(DateUtils.getDate());
        choProductBuyApplyService.saveChoProductReq(choProduct, getCurrentUser());
        return success();
    }
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(ChoProductReq choProductReq) {
        ChoProductReq t = choProductReqService.get(choProductReq.getId(), true);
        BeanUtils.copyProperties(choProductReq, t);
        choProductBuyApplyService.modifyChoProductReq(t, getCurrentUser());
        return success();
    }
    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput 前端传参
     * @return 审批列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
        return this.choProductBuyApplyService.dataTableApprove(dataTablesInput);
    }
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id){
        this.choProductBuyApplyService.delete(id);
        return success();
    }
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, Model model){
        model.addAttribute("flowStatus", flowStatus);
        return "chowmatistic/listChoProductBuyApplyApprove";
    }
    /**
     * 自动生成合同号
     *
     * @return 合同号
     */
    @LogMessage(content = "自动生成合同号", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/createContractNo")
    @ResponseBody
    public String createContractNo() {
        AjaxJson json = new AjaxJson();
        String sequenceNo = choProductBuyApplyService.createContractNo();
        json.setObj(sequenceNo);
        json.setSuccess(true);
        return JsonUtils.toJSONString(json);
    }
}
