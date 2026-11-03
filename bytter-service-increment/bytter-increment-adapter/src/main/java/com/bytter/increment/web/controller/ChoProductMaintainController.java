package com.bytter.increment.web.controller;
import com.bytter.increment.domain.entity.ChoProductReq;
import com.bytter.increment.domain.service.ChoProductMaintainService;
import com.bytter.increment.domain.service.ChoProductReqService;
import com.bytter.increment.domain.vo.ChoProductReqVo;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * 理财维护Controller
 *
 * @author ouyj
 */
@Controller
@RequestMapping("/chowmatistic/choproductMaintain")
public class ChoProductMaintainController extends BaseController {
    @Autowired
    private ChoProductMaintainService choProductMaintainService;
    @Autowired
    private ChoProductReqService choProductReqService;
    @GetMapping("/list")
    public String list(Model model) {
        return "chowmatistic/listChoProductMaintain";
    }
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return choProductMaintainService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }
    @GetMapping("/update")
    public String update(ChoProductReq choProductReq, Model model) {
        if (StringUtils.isNotEmpty(choProductReq.getId())) {
            ChoProductReqVo choProductReqVo = choProductReqService.getChoProductReqVoById(choProductReq.getId());
            model.addAttribute("choProductReqPage", choProductReqVo);
        }
        return "chowmatistic/updateChoProductMaintain";
    }
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(ChoProductReq choProductReq) {
        ChoProductReq t = choProductMaintainService.get(choProductReq.getId(), true);
        BeanUtils.copyProperties(choProductReq, t);
        choProductMaintainService.toModify(t, getCurrentUser());
        return success();
    }
}
