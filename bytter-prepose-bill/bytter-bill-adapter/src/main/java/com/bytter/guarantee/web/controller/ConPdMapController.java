package com.bytter.guarantee.web.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.guarantee.domain.service.ConPdMapDtlService;
import com.bytter.guarantee.domain.service.ConPdMapService;
import com.bytter.guarantee.domain.vo.ConPdMapDtlVO;
import com.bytter.guarantee.domain.vo.ConPdMapVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * @author wangzy
 * @Title: ConPdMapController
 * @Description: 合同类型要素映射Controller
 * @date 2019/11/8 13:34
 */
@Controller
@RequestMapping("/contract/conPdMap")
public class ConPdMapController extends BaseController {
    @Autowired
    private ConPdMapService conPdMapService;
    @Autowired
    private ConPdMapDtlService conPdMapDtlService;
    /**
     * 合同类型要素页面跳转
     *
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String listConPdMap(Model model) {
        return "contract/listConPdMap";
    }
    /**
     * 合同类型要素映射 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询合同类型要素映射", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        return conPdMapService.getDataTablesUnion(input);
    }
    /**
     * 合同类型要素新增页面跳转
     *
     * @param model
     * @return
     */
    @GetMapping("/add")
    public String addConPdMap(Model model) {
        model.addAttribute("isAdd", true);
        return "contract/addConPdMap";
    }
    /**
     * 合同类型要素映射编辑页面跳转
     *
     * @param model
     * @return
     */
    @GetMapping("/update")
    public String updateConPdMap(ConPdMapVO conPdMapVO, Model model) {
        model.addAttribute("pdDtlList",
                conPdMapDtlService.listConPdMapDtlVO(conPdMapVO.getPdCode(), conPdMapVO.getTypeId()));
        model.addAttribute("typeName", conPdMapService.getConTypeVO(conPdMapVO.getTypeId()).getTypeName());
        model.addAttribute("pdCode", conPdMapVO.getPdCode());
        model.addAttribute("typeId", conPdMapVO.getTypeId());
        model.addAttribute("isUpdate", true);
        return "contract/addConPdMap";
    }
    /**
     * 根据合同类型Id查询关键要素
     *
     * @param conTypeId
     *            合同类型Id
     * @return 关键要素List
     */
    @PostMapping("/listTypeField")
    @ResponseBody
    public List<ConPdMapDtlVO> listTypeField(String conTypeId, String pdCode) {
        return conPdMapDtlService.listConPdMapDtlVO(pdCode, conTypeId);
    }
    /**
     * @param
     * @return 结果
     * @Title: 合同类型关系映射保存或修改
     */
    @LogMessage(content = "合同类型关系映射保存或修改", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveOrUpdate")
    @ResponseBody
    public AjaxJson saveOrUpdateConPdMapDtl(ConPdMapVO info) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(conPdMapService.saveOrUpdateConPdMapDtl(info, this.getCurrentUser()));
        ajaxJson.setMsg("操作成功");
        return ajaxJson;
    }
    /**
     * @param
     * @return 结果
     * @Title: 合同类型关系映射删除
     */
    @LogMessage(content = "合同类型关系映射删除", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete")
    @ResponseBody
    public AjaxJson deleteConPdMap(String id) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(conPdMapService.deleteConPdMapAndDtl(id));
        ajaxJson.setMsg("操作成功");
        return ajaxJson;
    }
}
