package com.bytter.biz.saas.controller.netbank;

import com.bytter.biz.netbank.entity.NisPersonnelDetail;
import com.bytter.biz.netbank.service.NisPersonnelDetailService;
import com.bytter.biz.netbank.vo.NisPersonnelDetailVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 职工信息维护表
 * @date 2017-05-26 09:31:22
 */
@Api(value = "/v1-payment/netbank/nispersonneldetail", tags = "职工信息维护")
@RestController("v1NisPersonnelDetailController")
@RequestMapping("/v1-payment/netbank/nispersonneldetail")
public class NisPersonnelDetailController extends BaseController {
    @Autowired
    private NisPersonnelDetailService nisPersonnelDetailService;

    /**
     * 职工信息维护表 table列表
     *
     * @return
     */
    @ApiOperation("职工信息维护表 table列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<NisPersonnelDetailVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(nisPersonnelDetailService.getDataTablesUnion(input, getCurrentUser()));
    }

    /**
     * 职工信息维护表编辑页面跳转,职工信息维护表新增页面跳转
     *
     * @return
     */
    @ApiOperation("根据ID查询职工信息维护详情")
    @GetMapping("/update/{id}")
    public AjaxJson<NisPersonnelDetail> update(@PathVariable("id") String id) {
        return AjaxJson.success(nisPersonnelDetailService.getById(id));
    }

    /**
     * 职工信息维护表详情页
     *
     * @return
     */
    @ApiOperation("职工信息维护表详情页")
    @LogMessage(content = "查询职工信息维护表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<NisPersonnelDetailVO> view(@PathVariable("id") String id) {
        return AjaxJson.success(nisPersonnelDetailService.getByUnion(id));
    }


    /**
     * 添加职工 信息维护表
     *
     * @param nisPersonnelDetail
     * @return
     */
    @ApiOperation("添加职工信息维护表")
    @LogMessage(content = "添加职工信息维护表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody NisPersonnelDetail nisPersonnelDetail) {
        nisPersonnelDetailService.save(nisPersonnelDetail, this.getCurrentUser());
        return success();
    }

    /**
     * 修改职工信息维护表
     *
     * @param nisPersonnelDetail
     * @return
     */
    @ApiOperation("添加职工信息维护修改职工信息维护表表")
    @LogMessage(content = "修改职工信息维护表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(@RequestBody NisPersonnelDetail nisPersonnelDetail) {
        NisPersonnelDetail t = nisPersonnelDetailService.getById(nisPersonnelDetail.getId());
        BeanUtils.copyProperties(nisPersonnelDetail, t);
        nisPersonnelDetailService.saveOrUpdate(t);
        return success();
    }

    /**
     * 删除职工信息维护表
     *
     * @param id
     * @return
     */
    @ApiOperation("删除职工信息维护表")
    @LogMessage(content = "删除职工信息维护表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        NisPersonnelDetail nisPersonnelDetail = nisPersonnelDetailService.getById(id);
        nisPersonnelDetailService.deleteNisPersonnelDetail(nisPersonnelDetail);
        return success();
    }

    /**
     * 批量删除职工信息维护表
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量删除职工信息维护表")
    @LogMessage(content = "批量删除职工信息维护表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson batchDelete(@RequestParam("ids") String ids) {
        this.nisPersonnelDetailService.batchDelete(ids);
        return success();
    }


    /**
     * 保存导入数据
     *
     * @param list
     * @return
     */
    @ApiOperation("保存导入数据")
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<NisPersonnelDetailVO> list) {
        this.nisPersonnelDetailService.saveImport(list, this.getCurrentUser());
        return success();
    }

}
