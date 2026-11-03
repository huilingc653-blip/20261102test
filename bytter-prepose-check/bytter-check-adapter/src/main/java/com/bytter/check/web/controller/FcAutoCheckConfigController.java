package com.bytter.check.web.controller;
import com.bytter.check.entity.FcAutoCheckConfig;
import com.bytter.check.service.FcAutoCheckConfigService;
import com.bytter.check.service.FcAutoCheckCorpService;
import com.bytter.check.service.FcAutoCheckResultService;
import com.bytter.check.vo.FcAutoCheckConfigVO;
import com.bytter.check.vo.FcAutoCheckCorpVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
@Controller
@RequestMapping("/finacial/check/fcautocheckconfig")
public class FcAutoCheckConfigController extends BaseController {
    @Autowired
    private FcAutoCheckConfigService fcAutoCheckConfigService;
    @Autowired
    private FcAutoCheckCorpService fcAutoCheckCorpService;
    @Autowired
    private FcAutoCheckResultService fcAutoCheckResultService;
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_FCAUTOCHECKCONFIG_QUERY)
    @LogMessage(content = "FC_OUTO_CHECK_CONFIG列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "check/fcautocheckconfig/listFcAutoCheckConfig";
    }
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_AOTORESULT_QUERY)
    @GetMapping("/listResult")
    public String listResult(Model model) {
        return "check/fcautocheckconfig/listFcAutoCheckResult";
    }
    /**
     * 自动对账配置信息列表 - 数据
     */
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_FCAUTOCHECKCONFIG_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return fcAutoCheckConfigService.getDataTable(dataTablesInput);
    }
    /**
     * 自动对账结果查询 - 数据
     */
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_AOTORESULT_QUERY)
    @PostMapping("/resultDatatable")
    @ResponseBody
    public DataTablesOutput resultDatatable(@RequestBody DataTablesInput dataTablesInput) {
        return fcAutoCheckResultService.getDataTable(dataTablesInput);
    }
    /**
     * 获取自动对账配置新增页路径
     *
     * @return
     */
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_FCAUTOCHECKCONFIG_OP)
    @GetMapping("/add")
    public String add(Model model) {
        return "check/fcautocheckconfig/addFcAutoCheckConfig";
    }
    /**
     * 新增自动对账配置
     *
     * @param fcAutoCheckConfigVO 自动对账配置信息
     * @return 是否新增成功
     */
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_FCAUTOCHECKCONFIG_OP)
    @LogMessage(content = "新增自动对账配置信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(FcAutoCheckConfigVO fcAutoCheckConfigVO) {
        fcAutoCheckConfigService.save(fcAutoCheckConfigVO, this.getCurrentUser());
        return success();
    }
    /**
     * 获取自动对账配置新增页路径
     *
     * @return
     */
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_FCAUTOCHECKCONFIG_OP)
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        FcAutoCheckConfig fcAutoCheckConfig = fcAutoCheckConfigService.get(id);
        int size = fcAutoCheckCorpService.getByCode(fcAutoCheckConfig.getAutoCheckCode()).size();
        model.addAttribute("size", size);//是否有关联单位
        model.addAttribute("fcAutoCheckConfig", fcAutoCheckConfig);
        return "check/fcautocheckconfig/updateFcAutoCheckConfig";
    }
    /**
     * 修改自动对账配置
     *
     * @param fcAutoCheckConfigVO 自动对账配置信息
     * @return 是否成功
     */
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_FCAUTOCHECKCONFIG_OP)
    @LogMessage(content = "修改自动对账配置信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(FcAutoCheckConfigVO fcAutoCheckConfigVO,HttpServletRequest request) {
        fcAutoCheckConfigService.modify(fcAutoCheckConfigVO, this.getCurrentUser(),request);
        return success();
    }
    /**
     * 删除自动对账配置
     *
     * @param id 自动对账配置信息id
     * @return 是否成功
     */
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_FCAUTOCHECKCONFIG_OP)
    @LogMessage(content = "删除自动对账配置信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id,HttpServletRequest request) {
        fcAutoCheckConfigService.delete(id, this.getCurrentUser());
        request.getSession().setAttribute("fcAutoCheckConfig", null);
        request.getSession().setAttribute("erpAutoCheckConfig", null);
        return success();
    }
    /**
     * 获取自动对账配置关联单位
     *
     * @return
     */
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_FCAUTOCHECKCONFIG_OP)
    @GetMapping("/withCorp/{id}")
    public String withCorp(@PathVariable("id") String id, Model model, HttpServletRequest request) {
        FcAutoCheckConfig fcAutoCheckConfig = fcAutoCheckConfigService.get(id);
        model.addAttribute("autoCheckCode", fcAutoCheckConfig.getAutoCheckCode());
        model.addAttribute("checkType", fcAutoCheckConfig.getCheckType());
        if (fcAutoCheckConfig.getCheckType().equals("0")) {
            List<FcAutoCheckCorpVO> checkCorpVOS = (List<FcAutoCheckCorpVO>) request.getSession().getAttribute("fcAutoCheckConfig");
            if (EmptyUtils.isEmpty(checkCorpVOS)) {
                checkCorpVOS = fcAutoCheckCorpService.getAllCheckCorp("0", this.getCurrentUser());
                request.getSession().setAttribute("fcAutoCheckConfig", checkCorpVOS);
            }
            model.addAttribute("checkCorpVOS", checkCorpVOS);
        } else {
            List<FcAutoCheckCorpVO> erpcheckCorpVOS = (List<FcAutoCheckCorpVO>) request.getSession().getAttribute("erpAutoCheckConfig");
            if (EmptyUtils.isEmpty(erpcheckCorpVOS)) {
                erpcheckCorpVOS = fcAutoCheckCorpService.getAllCheckCorp("1", this.getCurrentUser());
                request.getSession().setAttribute("erpAutoCheckConfig", erpcheckCorpVOS);
            }
            model.addAttribute("checkCorpVOS", erpcheckCorpVOS);
        }
        model.addAttribute("validSign", fcAutoCheckConfig.getValidSign());//是否是有效的配置
        return "check/fcautocheckconfig/fcAutoCheckConfigWithCorp";
    }
    /**
     * 新增自动对账关联单位
     *
     * @param parm 自动对账单位关联信息
     * @return 是否新增成功
     */
    //@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_FCAUTOCHECKCONFIG_OP)
    @LogMessage(content = "新增自动对账关联单位", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/saveWithCorp")
    @ResponseBody
    public AjaxJson saveWithCorp(@RequestBody Map<String, String> parm, HttpServletRequest request) {
        if (parm.get("checkType").equals("0")) {
            request.getSession().setAttribute("fcAutoCheckConfig", null);
        } else {
            request.getSession().setAttribute("erpAutoCheckConfig", null);
        }
        fcAutoCheckCorpService.saveWithCorp(parm, this.getCurrentUser());
        return success();
    }
}
