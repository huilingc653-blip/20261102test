package com.bytter.biz.ttpay.controller;
import com.bytter.biz.ttpay.entity.TpShopType;
import com.bytter.biz.ttpay.service.TpShopTypeService;
import com.bytter.biz.ttpay.vo.TpShopTypeVO;
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
@RequestMapping("/ttpay/shopType")
public class TpShopTpyeController extends BaseController {
    @Autowired
    public TpShopTypeService tpShopTypeService;
    /**
     * 参数配置列表
     *
     * @param model
     * @return
     */
    @GetMapping("/list")
    //////@RequiresPermissions(TtPayConstant.TTPAY_SHOPTYPE_VIEW)
    public String list(Model model) {
        return "ttpay/listShopTypeConfig";
    }
    /**
     * 商户类别维护列表
     *
     * @param input
     * @return 列表数据
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "商户维护列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    //////@RequiresPermissions(TtPayConstant.TTPAY_SHOPTYPE_VIEW)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput queryDatatable(@RequestBody DataTablesInput input) {
        return tpShopTypeService.getQueryDataTablesUnion(input);
    }
    //////@RequiresPermissions(TtPayConstant.TTPAY_SHOPTYPE_ADD)
    @GetMapping("/add")
    public String add() {
        return "ttpay/addShopTypeConfig";
    }
    //////@RequiresPermissions(TtPayConstant.TTPAY_SHOPTYPE_ADD)
    @LogMessage(content = "添加商户类别", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(TpShopType tpShopType) {
        ICurrentUser user = getCurrentUser();
        tpShopTypeService.saveTpShopType(tpShopType,user);
        return success();
    }
    /**
     * 获取类型名称
     * @param model
     * @return
     */
    @GetMapping("/getTypeName")
    @ResponseBody
    public List<TpShopTypeVO> getTypeName(Model model){
        //获取字典模块名称
        List<TpShopTypeVO> list=tpShopTypeService.getTypeName();
        return list;
    }
    @GetMapping("/update/{id}")
    //////@RequiresPermissions(TtPayConstant.TTPAY_SHOPTYPE_EDIT)
    public String update(@PathVariable("id") String id, Model model) {
        TpShopType tpShopType = tpShopTypeService.get(id);
        model.addAttribute("tpShopType", tpShopType);
        return "ttpay/updateShopTypeConfig";
    }
    //////@RequiresPermissions(TtPayConstant.TTPAY_SHOPTYPE_EDIT)
    @LogMessage(content = "修改商户类别", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(TpShopType tpShopType) {
        ICurrentUser user = getCurrentUser();
        tpShopTypeService.updateTpShopType(tpShopType,user);
        return success();
    }
    //////@RequiresPermissions(TtPayConstant.TTPAY_SHOPTYPE_VIEW)
    @LogMessage(content = "查询商户类别", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        TpShopType tpShopType = tpShopTypeService.get(id);
        model.addAttribute("tpShopType", tpShopType);
        return "ttpay/viewShopTypeConfig";
    }
    //////@RequiresPermissions(TtPayConstant.TTPAY_SHOPTYPE_DEL)
    @LogMessage(content = "删除商户类别", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson deleteBRGradeIndex(@PathVariable("id") String id) {
        TpShopType tpShopType = tpShopTypeService.get(id);
        tpShopType.setStatus(InlConstant.DELETE);
        tpShopTypeService.updateEntity(tpShopType);
        return success();
    }
}
