package com.bytter.biz.ttpay.controller;
import com.bytter.biz.ttpay.entity.TpPayType;
import com.bytter.biz.ttpay.service.TpPayTypeService;
import com.bytter.biz.ttpay.vo.TpPayTypeVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.finance.constant.InlConstant;
import com.bytter.biz.payment.api.constant.TtPayConstant;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@Controller
@RequestMapping("/ttpay/tpPayType")
public class TpPayTypeController extends BaseController {
    @Autowired
    private TpPayTypeService tpPayTypeService;
    /**
     * 参数配置列表
     *
     * @param model
     * @return
     */
    @GetMapping("/list")
    //////@RequiresPermissions(TtPayConstant.TTPAY_PAYTYPE_VIEW)
    public String list(Model model) {
        return "ttpay/listTpPayType";
    }
    /**
     * 商户类别维护列表
     *
     * @param input
     * @return 列表数据
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "商户维护列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    //////@RequiresPermissions(TtPayConstant.TTPAY_PAYTYPE_VIEW)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput queryDatatable(@RequestBody DataTablesInput input) {
        return tpPayTypeService.getQueryDataTablesUnion(input);
    }
    //////@RequiresPermissions(TtPayConstant.TTPAY_PAYTYPE_ADD)
    @GetMapping("/add")
    public String add() {
        return "ttpay/addTpPayType";
    }
    //////@RequiresPermissions(TtPayConstant.TTPAY_PAYTYPE_ADD)
    @LogMessage(content = "添加商户类别", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(TpPayType tpPayType) {
        ICurrentUser user = getCurrentUser();
        tpPayTypeService.saveTpShopType(tpPayType, user);
        return success();
    }
    /**
     * 获取类型名称
     *
     * @param model
     * @return
     */
    @GetMapping("/getTypeName")
    @ResponseBody
    public List<TpPayTypeVo> getTypeName(Model model) {
        //获取字典模块名称
        List<TpPayTypeVo> list = tpPayTypeService.getTypeName();
        return list;
    }
    @GetMapping("/update/{id}")
    //////@RequiresPermissions(TtPayConstant.TTPAY_PAYTYPE_EDIT)
    public String update(@PathVariable("id") String id, Model model) {
        TpPayType tpShopType = tpPayTypeService.get(id);
        model.addAttribute("tpShopType", tpShopType);
        return "ttpay/updateTpPayType";
    }
    //////@RequiresPermissions(TtPayConstant.TTPAY_PAYTYPE_EDIT)
    @LogMessage(content = "修改商户类别", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(TpPayType tpPayType) {
        ICurrentUser user = getCurrentUser();
        tpPayTypeService.updateTpShopType(tpPayType, user);
        return success();
    }
    //////@RequiresPermissions(TtPayConstant.TTPAY_PAYTYPE_VIEW)
    @LogMessage(content = "查询商户类别", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        TpPayType tpPayType = tpPayTypeService.get(id);
        model.addAttribute("tpShopType", tpPayType);
        return "ttpay/viewTpPayType";
    }
    //////@RequiresPermissions(TtPayConstant.TTPAY_PAYTYPE_DEL)
    @LogMessage(content = "删除商户类别", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson deleteBRGradeIndex(@PathVariable("id") String id) {
        TpPayType tpPayType = tpPayTypeService.get(id);
        tpPayType.setStatus(InlConstant.DELETE);
        tpPayTypeService.updateEntity(tpPayType);
        return success();
    }
}
