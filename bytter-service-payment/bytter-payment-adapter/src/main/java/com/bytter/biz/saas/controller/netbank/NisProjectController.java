package com.bytter.biz.saas.controller.netbank;

import com.bytter.biz.netbank.entity.NisProject;
import com.bytter.biz.netbank.service.NisProjectService;
import com.bytter.biz.netbank.vo.NisProjectVO;
import com.bytter.biz.payment.api.enums.NisProjectMessageEnum;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.poi.util.ExportUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
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
@Api(value = "/v1-payment/netbank/nisproject", tags = "项目维护管理")
@RestController("v1NisProjectController")
@RequestMapping("/v1-payment/netbank/nisproject")
public class NisProjectController extends BaseController {
    @Autowired
    private NisProjectService nisProjectService;

    /**
     * 项目维护列表数据加载
     *
     * @param dataTablesInput
     * @return
     */
    @ApiOperation("项目维护列表数据加载列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<NisProjectVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(nisProjectService.getDataTablesUnion(input));
    }


    /**
     * 项目维护更新页面跳转
     *
     * @param id
     * @return
     */
    @ApiOperation("项目维护详情，项目维护更新页面跳转")
    @GetMapping("/update/{id}")
    public AjaxJson<NisProject> update(@PathVariable("id") String id) {
        return AjaxJson.success(nisProjectService.getById(id));
    }

    /**
     * 项目维护列表详情页面
     *
     * @param id
     * @return
     */
    @ApiOperation("项目维护列表详情页面")
    @LogMessage(content = "查询项目维护列表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<NisProjectVO> view(@PathVariable("id") String id) {
        return AjaxJson.success(nisProjectService.getByUnion(id));
    }

    /**
     * 获取所有的项目
     *
     * @return
     */
    @ApiOperation("获取所有的项目")
    @PostMapping("json")
    @ResponseBody
    public AjaxJson<List<NisProject>> getListAll() {
        List<NisProject> list = nisProjectService.getListAll();
        return AjaxJson.success(list);
    }

    /**
     * 获取所有的项目
     *
     * @return
     */
    @ApiOperation("获取所有的项目")
    @GetMapping("/listNisProjectByCdSign")
    @ResponseBody
    public AjaxJson<List<NisProject>> listNisProjectByCdSign(String cdSign) {
        return AjaxJson.success(this.nisProjectService.listNisProjectByCdSign(cdSign));
    }


    @ApiOperation("添加项目维护表")
    @LogMessage(content = "添加项目维护表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody NisProject nisProject) {
        if (nisProjectService.findProjectCode(nisProject.getProjectCode()) != null) {
            throw new ParameterException(NisProjectMessageEnum.NIS_PROJECT_MESSAGE_CODE_UNIQUE);
        }
        nisProject.setCreateBy(this.getCurrentUser().getLoginName());
        nisProject.setCreateDate(DateUtils.getDate());
        nisProjectService.save(nisProject, LocaleContextHolder.getLocale().toString());
        return success();
    }


    /**
     * 修改项目信息
     *
     * @param nisProject
     * @return
     */
    @ApiOperation("修改项目维护表")
    @LogMessage(content = "修改项目维护表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(@RequestBody NisProject nisProject) {
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
     * 删除项目数据
     *
     * @param id
     * @return
     */
    @ApiOperation("删除业务数据")
    @LogMessage(content = "删除业务数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        NisProject nisProject = nisProjectService.getById(id);
        this.nisProjectService.deleteNisProject(nisProject);
        return success();
    }

    /**
     * 导出全部
     *
     * @return
     */
    @ApiOperation("导出全部")
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
