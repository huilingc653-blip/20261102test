package com.bytter.biz.foreign.controller;

import com.bytter.biz.foreign.service.FexTranConfigTemplateService;
import com.bytter.biz.foreign.vo.FexTranTemplateConfigDetailVO;
import com.bytter.biz.foreign.vo.FexTranTemplateConfigParamVO;
import com.bytter.biz.foreign.vo.FexTranTemplateConfigVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.jqgrid.TreeGridResponse;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ResourceConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 交易模板配置操作日志
 */
@Controller
@RequestMapping("/foreign/fexTranConfigTemplate")
public class FexTranConfigTemplateController extends BaseController {

    @Autowired
    private FexTranConfigTemplateService fexTranConfigTemplateService;

    /**
     * 交易模板配置操作询页面
     *
     * @return
     */
    //RequiresPermissions(ResourceConstant.FEX_TRANCONFIGTEMPLATE_QUERY)
    @GetMapping("/index")
    public String index() {
        return "foreign/fexConfigTemplate/fexConfigTemplateIndex";
    }

    /**
     * 新增页面跳转
     *
     * @return
     */
    //RequiresPermissions(ResourceConstant.FEX_TRANCONFIGTEMPLATE_APPLY)
    @GetMapping("/add")
    public String add() {
        return "foreign/fexConfigTemplate/addFexConfigTemplate";
    }


    /**
     * 跳转查案页面
     *
     * @param id
     * @param model
     * @return
     */
    //RequiresPermissions(ResourceConstant.FEX_TRANCONFIGTEMPLATE_APPLY)
    @GetMapping("update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        FexTranTemplateConfigVO detail = fexTranConfigTemplateService.view(id);
        model.addAttribute("details", detail);
        return "foreign/fexConfigTemplate/updateFexConfigTemplate";
    }

    /**
     * 跳转查案页面
     *
     * @param id
     * @param model
     * @return
     */
    //RequiresPermissions(ResourceConstant.FEX_TRANCONFIGTEMPLATE_QUERY)
    @GetMapping("view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        FexTranTemplateConfigVO detail = fexTranConfigTemplateService.view(id);
        model.addAttribute("details", detail);
        return "foreign/fexConfigTemplate/viewFexConfigTemplate";
    }


    /**
     * 列表（树形）查询
     *
     * @param vo
     * @return
     */
    //RequiresPermissions(ResourceConstant.FEX_TRANCONFIGTEMPLATE_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public TreeGridResponse<FexTranTemplateConfigVO> dataTable(@RequestBody FexTranTemplateConfigParamVO vo) {
        List<FexTranTemplateConfigVO> result = fexTranConfigTemplateService.list(vo);
        return new TreeGridResponse<>(result);
    }


    /**
     * 模板列表查询
     *
     * @return
     */
    @GetMapping("/configTemplateList")
    @ResponseBody
    public List<FexTranTemplateConfigDetailVO> configTemplateList() {
        DataTablesOutput<FexTranTemplateConfigDetailVO> list = this.configTemplateChildrenList(new DataTablesInput());
        return list.getData();
    }

    /**
     * 交易配置模板保存
     *
     * @param vo
     * @return
     */
    //RequiresPermissions(ResourceConstant.FEX_TRANCONFIGTEMPLATE_APPLY)
    @LogMessage(content = "交易配置模板保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody FexTranTemplateConfigVO vo) {
        ICurrentUser user = getCurrentUser();
        fexTranConfigTemplateService.save(vo, user.getId());
        return success();
    }

    /**
     * 交易配置模板删除
     *
     * @param id
     * @return
     */
    //RequiresPermissions(ResourceConstant.FEX_TRANCONFIGTEMPLATE_APPLY)
    @LogMessage(content = "交易配置模板删除", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        fexTranConfigTemplateService.delete(id, getUserId());
        return success();
    }

    /**
     * 交易配置模板拆分
     *
     * @param id
     * @return
     */
    //RequiresPermissions(ResourceConstant.FEX_TRANCONFIGTEMPLATE_APPLY)
    @LogMessage(content = "交易配置模板拆分", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/split/{id}")
    @ResponseBody
    public AjaxJson split(@PathVariable("id") String id) {
        fexTranConfigTemplateService.split(id, getCurrentUser());
        return success();
    }

    /**
     * 交易配置模板失效
     *
     * @param id
     * @return
     */
    //RequiresPermissions(ResourceConstant.FEX_TRANCONFIGTEMPLATE_APPLY)
    @LogMessage(content = "交易配置模板失效", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/validFalse/{id}")
    @ResponseBody
    public AjaxJson validFalse(@PathVariable("id") String id) {
        fexTranConfigTemplateService.valid(id, CommonConstant.NO, getUserId());
        return success();
    }

    /**
     * 交易配置模板生效  1有效 0无效
     *
     * @param id
     * @return
     */
    //RequiresPermissions(ResourceConstant.FEX_TRANCONFIGTEMPLATE_APPLY)
    @LogMessage(content = "交易配置模板生效", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/validTrue/{id}")
    @ResponseBody
    public AjaxJson validTrue(@PathVariable("id") String id) {
        fexTranConfigTemplateService.valid(id, CommonConstant.YES, getUserId());
        return success();
    }


    /**
     * 交易配置模板修改
     *
     * @param vo
     * @return
     */
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(@RequestBody FexTranTemplateConfigVO vo) {
        ICurrentUser user = getCurrentUser();
        fexTranConfigTemplateService.modify(vo, user.getId());
        return success();
    }


    /**
     * 模板列表查询
     *
     * @return
     */
    @PostMapping("/configTemplateChildrenList")
    @ResponseBody
    public DataTablesOutput<FexTranTemplateConfigDetailVO> configTemplateChildrenList(@RequestBody DataTablesInput vo) {
        Object bankTypeId = vo.getFormdata().get("bankTypeId");
        Object bifId = vo.getFormdata().get("bifId");
        Object countryId = vo.getFormdata().get("countryId");
        if (!EmptyUtils.isEmpty(bankTypeId) && !EmptyUtils.isEmpty(bankTypeId.toString())) {
            vo.getFormdata().put("bankTypeIds", Arrays.stream(bankTypeId.toString().split(",")).collect(Collectors.toList()));
        }
        if (!EmptyUtils.isEmpty(bifId) && !EmptyUtils.isEmpty(bifId.toString())) {
            vo.getFormdata().put("bifIds", Arrays.stream(bifId.toString().split(",")).collect(Collectors.toList()));
        }
        if (!EmptyUtils.isEmpty(countryId) && !EmptyUtils.isEmpty(countryId.toString())) {
            vo.getFormdata().put("countryIds", Arrays.stream(countryId.toString().split(",")).collect(Collectors.toList()));
        }
        DataTablesOutput<FexTranTemplateConfigDetailVO> data = fexTranConfigTemplateService.configTemplateChildrenList(vo);
        if (!data.getData().isEmpty()) {
            data.getData().forEach(e -> {
                e.setTemplateName(e.getTemplateName() + "（" + e.getBankTypeName() + "-" + e.getBifName() + "-" + e.getCountryZhName() + "-" + e.getPayTypeName() + "）");
            });
        }
        return data;
    }

    /**
     * 所有子模板
     *
     * @return
     */
    @PostMapping("/allChildrenList")
    @ResponseBody
    public List<FexTranTemplateConfigDetailVO> allChildrenList() {
        FexTranTemplateConfigParamVO vo=new FexTranTemplateConfigParamVO();
        return fexTranConfigTemplateService.listOfChildren(vo);
    }

    @PostMapping("/listOfParentPage")
    @ResponseBody
    public DataTablesOutput<FexTranTemplateConfigVO> listOfParentPage(@RequestBody DataTablesInput vo) {
        Object bankTypeId = vo.getFormdata().get("bankTypeId");
        Object bifId = vo.getFormdata().get("bifId");
        Object countryId = vo.getFormdata().get("countryId");
        if (!EmptyUtils.isEmpty(bankTypeId) && !EmptyUtils.isEmpty(bankTypeId.toString())) {
            vo.getFormdata().put("bankTypeIds", Arrays.stream(bankTypeId.toString().split(",")).collect(Collectors.toList()));
        }
        if (!EmptyUtils.isEmpty(bifId) && !EmptyUtils.isEmpty(bifId.toString())) {
            vo.getFormdata().put("bifIds", Arrays.stream(bifId.toString().split(",")).collect(Collectors.toList()));
        }
        if (!EmptyUtils.isEmpty(countryId) && !EmptyUtils.isEmpty(countryId.toString())) {
            vo.getFormdata().put("countryIds", Arrays.stream(countryId.toString().split(",")).collect(Collectors.toList()));
        }
        return fexTranConfigTemplateService.listOfParentPage(vo);
    }


    /**
     * 跳转模板选择
     *
     * @return
     */
    @GetMapping("/fexConfigTemplateChoose")
    public String fexConfigTemplateChoose() {
        return "foreign/fexConfigTemplate/fexConfigTemplateChoose";
    }

    /**
     * 跳转模板选择（多选）
     *
     * @return
     */
    @GetMapping("/fexConfigTemplateMulselect")
    public String fexConfigTemplateMulselectChoose() {
        return "foreign/fexConfigTemplate/fexConfigTemplateMulselect";
    }


    /**
     * 根据银行账号获取模板
     *
     * @param bankAcc
     * @param payTypeId
     * @return
     */
    @PostMapping("/getTemplateByBankAcc")
    @ResponseBody
    public AjaxJson getTemplateByBankAcc(String bankAcc, String payTypeId) {
        FexTranTemplateConfigVO templateByBankAcc = fexTranConfigTemplateService.getTemplateByBankAcc(bankAcc, payTypeId);
        return success(templateByBankAcc);
    }

    /**
     * 查询模板信息
     *
     * @param fexTranTemplateConfigParam
     * @return
     */
    @PostMapping("/getTemplateConfig")
    @ResponseBody
    public FexTranTemplateConfigVO getTemplateConfig(@RequestBody FexTranTemplateConfigVO fexTranTemplateConfigParam) {
        return fexTranConfigTemplateService.getTemplateConfig(fexTranTemplateConfigParam);
    }

    /**
     * 根据ID查询模板详情
     *
     * @param id
     * @return
     */
    @GetMapping("viewjson/{id}")
    @ResponseBody
    public AjaxJson viewjson(@PathVariable("id") String id) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(true);
        ajaxJson.setObj(fexTranConfigTemplateService.view(id));
        return ajaxJson;
    }

    /**
     * 校验该支付类型是否配置交易模板
     *
     * @param payTypeId
     * @return
     */
    @GetMapping("validPayTypeConfig/{payTypeId}")
    @ResponseBody
    public AjaxJson validPayTypeConfig(@PathVariable("payTypeId") String payTypeId) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(true);
        FexTranTemplateConfigParamVO vo = new FexTranTemplateConfigParamVO();
        vo.setPayTypeId(payTypeId);
        List<FexTranTemplateConfigDetailVO> data = fexTranConfigTemplateService.listOfChildren(vo);
        if (!EmptyUtils.isEmpty(data)) {
            String msg = data.stream().map(e -> e.getTemplateNo() + "-" + e.getTemplateNameParent()).distinct().collect(Collectors.joining("、"));
            ajaxJson.setObj(true);
            ajaxJson.setMsg("该支付类型已被【" + msg + "】交易模板数据引用");
        } else {
            ajaxJson.setObj(false);
        }
        return ajaxJson;
    }
}
