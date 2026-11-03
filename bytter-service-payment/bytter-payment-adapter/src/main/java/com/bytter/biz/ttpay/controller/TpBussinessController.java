package com.bytter.biz.ttpay.controller;
import com.bytter.biz.ttpay.service.TpBussinessService;
import com.bytter.biz.ttpay.vo.TpBussinessTextVO;
import com.bytter.biz.ttpay.vo.TpBussinessVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.biz.payment.api.constant.TtPayConstant;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@Controller
@RequestMapping("/ttpay/bussiness")
public class TpBussinessController extends BaseController {
    @Autowired
    private TpBussinessService tpBussinessService;
    @GetMapping("/list")
    //////@RequiresPermissions(TtPayConstant.TTPAY_SHOP_BUSSNISS_VIEW)
    public String list(Model model) {
        return "ttpay/listBussinessConfig";
    }
    /**
     * 商户维护列表
     *
     * @param input
     * @return 列表数据
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "商户维护列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    //////@RequiresPermissions(TtPayConstant.TTPAY_SHOP_BUSSNISS_VIEW)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput queryDatatable(@RequestBody DataTablesInput input) {
        return tpBussinessService.getQueryDataTablesUnion(input);
    }
    //////@RequiresPermissions(TtPayConstant.TTPAY_SHOP_BUSSNISS_ADD)
    @GetMapping("/add")
    public String add() {
        return "ttpay/addBussinessConfig";
    }
    //////@RequiresPermissions(TtPayConstant.TTPAY_SHOP_BUSSNISS_ADD)
    @LogMessage(content = "添加商户", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(TpBussinessVO tpBussinessVO) {
        tpBussinessService.checkUnion(tpBussinessVO);
        ICurrentUser user = getCurrentUser();
        tpBussinessService.saveTpBussiness(tpBussinessVO, user);
        return success();
    }
    @GetMapping("/update/{id}")
    //////@RequiresPermissions(TtPayConstant.TTPAY_SHOP_BUSSNISS_EDIT)
    public String update(@PathVariable("id") String id, Model model) {
        TpBussinessVO tpBussiness = tpBussinessService.getTpBussiness(id);
        model.addAttribute("tpBussiness", tpBussiness);
        return "ttpay/updateBussinessConfig";
    }
    @PostMapping("/listTpBussinessTextVO")
    @ResponseBody
    public List<TpBussinessTextVO> listLeaseCapitalPlanDel(String tpBusinessId) {
        List<TpBussinessTextVO> list = tpBussinessService.getTpBussinessTextVOById(tpBusinessId);
        return list;
    }
    //////@RequiresPermissions(TtPayConstant.TTPAY_SHOP_BUSSNISS_EDIT)
    @LogMessage(content = "修改商户", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(TpBussinessVO tpBussinessVO) {
        tpBussinessService.checkUnion(tpBussinessVO);
        ICurrentUser user = getCurrentUser();
        tpBussinessService.updateTpBussiness(tpBussinessVO, user);
        return success();
    }
    //////@RequiresPermissions(TtPayConstant.TTPAY_SHOP_BUSSNISS_DEL)
    @LogMessage(content = "删除商户", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson deleteTpBussiness(@PathVariable("id") String id) {
        ICurrentUser user = getCurrentUser();
        tpBussinessService.deleteTpBussiness(id, user);
        return success();
    }
    //////@RequiresPermissions(TtPayConstant.TTPAY_SHOP_BUSSNISS_VIEW)
    @LogMessage(content = "查询商户类别", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        TpBussinessVO tpBussiness = tpBussinessService.getTpBussiness(id);
        model.addAttribute("tpBussiness", tpBussiness);
        List<TpBussinessTextVO> tpBussinessTextVO = tpBussiness.getTpBussinessTextVOS();
        model.addAttribute("tpBussinessTextVO", tpBussinessTextVO);
        return "ttpay/viewTpBussiness";
    }
}
