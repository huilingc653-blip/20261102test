package com.bytter.biz.netbank.controller;
import com.bytter.biz.netbank.entity.NisProject;
import com.bytter.biz.netbank.service.NisProjectService;
import com.bytter.biz.netbank.vo.NisProjectVO;
import com.bytter.biz.payment.api.enums.NisProjectMessageEnum;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.poi.util.ExportUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
/**
 * @author shenc
 * @version 1.0
 * @Title
 * @Description:
 * @date 2018/5/14 16:03
 */
@Controller
@RequestMapping("/netbank/nisproject")
public class NisProjectController extends BaseController {
    @Autowired
    private NisProjectService nisProjectService;
    /**
     * 项目维护列表页面跳转
     *
     * @param model
     * @return
     */
//    @RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_NISPROJECT_APPLY)
    @LogMessage(content = "查询项目维护表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "netbank/listNisProject";
    }
    /**
     * 项目维护列表数据加载
     *
     * @param dataTablesInput
     * @return
     */
//    @RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_NISPROJECT_APPLY)
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return nisProjectService.getDataTablesUnion(dataTablesInput);
    }
    //    @RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_NISPROJECT_APPLY)
    @GetMapping("/add")
    public String add(NisProject nisProject, Model model) {
        if (StringUtils.isNotEmpty(nisProject.getId())) {
            nisProject = nisProjectService.getById(nisProject.getId());
            model.addAttribute("nisProjectPage", nisProject);
        }
        return "netbank/addNisProject";
    }
    //    @RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_NISPROJECT_APPLY)
    @LogMessage(content = "添加项目维护表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(NisProject nisProject) {
        if (nisProjectService.findProjectCode(nisProject.getProjectCode()) != null) {
            throw new ParameterException(NisProjectMessageEnum.NIS_PROJECT_MESSAGE_CODE_UNIQUE);
        }
        nisProject.setCreateBy(this.getCurrentUser().getLoginName());
        nisProject.setCreateDate(DateUtils.getDate());
        nisProjectService.save(nisProject, LocaleContextHolder.getLocale().toString());
        return success();
    }
    /**
     * 项目维护更新页面跳转
     *
     * @param nisProject
     * @param model
     * @return
     */
//    @RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_NISPROJECT_APPLY)
    @GetMapping("/update")
    public String update(NisProject nisProject, Model model) {
        if (StringUtils.isNotEmpty(nisProject.getId())) {
            nisProject = nisProjectService.getById(nisProject.getId());
            model.addAttribute("nisProjectPage", nisProject);
        }
        return "netbank/updateNisProject";
    }
    /**
     * 修改项目信息
     *
     * @param nisProject
     * @return
     */
//    @RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_NISPROJECT_APPLY)
    @LogMessage(content = "修改项目维护表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(NisProject nisProject) {
        if (nisProjectService.findProjectCode(nisProject.getProjectCode()) != null) {
            if (!nisProject.getId().equals(nisProjectService.findProjectCode(nisProject.getProjectCode()).getId())) {
                throw new ParameterException(NisProjectMessageEnum.NIS_PROJECT_MESSAGE_CODE_UNIQUE);
            }
        }
        NisProject t = nisProjectService.getById(nisProject.getId());
        nisProject.setUpdateBy(this.getCurrentUser().getLoginName());
        nisProject.setUpdateDate(DateUtils.getDate());
        BeanUtils.copyProperties(nisProject, t);
        nisProjectService.saveOrUpdate(t, LocaleContextHolder.getLocale().toString());
        return success();
    }
    /**
     * 项目维护列表详情页面
     *
     * @param id
     * @param model
     * @return
     */
//    @RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_NISPROJECT_APPLY)
    @LogMessage(content = "查询项目维护列表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        NisProjectVO nisProjectVO = nisProjectService.getByUnion(id);
        model.addAttribute("nisProjectPage", nisProjectVO);
        return "netbank/viewNisProject";
    }
    /**
     * 删除项目数据
     *
     * @param id
     * @return
     */
//    @RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_NISPROJECT_APPLY)
    @LogMessage(content = "删除业务数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        NisProject nisProject = nisProjectService.getById(id);
        this.nisProjectService.deleteNisProject(nisProject);
        return success();
    }
    /**
     * 获取所有的项目
     *
     * @return
     */
    @PostMapping("json")
    @ResponseBody
    public List<NisProject> getListAll() {
        List<NisProject> list = nisProjectService.getListAll();
        return list;
    }
    /**
     * 获取所有的项目
     *
     * @return
     */
    @GetMapping("/listNisProjectByCdSign")
    @ResponseBody
    public List<NisProject> listNisProjectByCdSign(String cdSign) {
        return this.nisProjectService.listNisProjectByCdSign(cdSign);
    }
    /**
     * 导出全部
     *
     * @return
     */
    @RequestMapping("/exportAll")
    public void exportAll(HttpServletResponse response, String dataTablesInputParam, String exportExcelHeadListParam,
                          String totalParam, String fileNameParam) throws Exception {
        ExportUtils.exportByStream(response, dataTablesInputParam, exportExcelHeadListParam, new ExportUtils.ExportData() {
            @Override
            public List<?> getData(DataTablesInput dataTablesInput, ICurrentUser user) {
                DataTablesOutput<?> dataTablesOutput = nisProjectService.getDataTablesUnion(dataTablesInput);
                return dataTablesOutput.getData();
            }
        }, totalParam, fileNameParam, 5000, NisProjectVO.class);
    }
}
