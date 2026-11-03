package com.bytter.credit.web.controller.saas;
import com.bytter.biz.payment.api.dto.BisIDsDTO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.domain.entity.RatBkrationDetail;
import com.bytter.credit.domain.service.RatBkrationDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
/**
 * @Title: Controller
 * @Description: 授信录入明细表
 * @author onlineGenerator
 * @date 2017-07-21 14:35:53
 * @version 1.0
 *
 */
@Api(tags = "授信录入明细表", value = "/v1-cms/rat/ratbkrationdetail")
@RestController("v1RatBkrationDetailController")
@RequestMapping("/v1-cms/rat/ratbkrationdetail")
public class RatBkrationDetailController extends BaseController {
    @Autowired
    private RatBkrationDetailService ratBkrationDetailService;
    /**
     * 授信录入明细表 table列表
     *
     * @return
     */
    @ApiOperation("查询授信录入明细表表格数据")
    @LogMessage(content = "查询授信录入明细表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(ratBkrationDetailService.getDataTables(input));
    }
    /**
     * 授信录入明细表新增页面数据
     *
     * @return
     */
    @ApiOperation("授信录入明细表新增页面数据")
    @GetMapping("/add")
    public AjaxJson<RatBkrationDetail> add(RatBkrationDetail ratBkrationDetail) {
        if (StringUtils.isNotEmpty(ratBkrationDetail.getId())) {
            ratBkrationDetail = ratBkrationDetailService.get(ratBkrationDetail.getId());
        }
        return success(ratBkrationDetail);
    }
    /**
     * 授信录入明细表编辑页面数据
     *
     * @return
     */
    @ApiOperation("授信录入明细表编辑页面数据")
    @GetMapping("/update")
    public AjaxJson<RatBkrationDetail> updatePage(RatBkrationDetail ratBkrationDetail) {
        if (StringUtils.isNotEmpty(ratBkrationDetail.getId())) {
            ratBkrationDetail = ratBkrationDetailService.get(ratBkrationDetail.getId());
        }
        return success(ratBkrationDetail);
    }
    /**
     * 授信录入明细表详情
     *
     * @return
     */
    @ApiOperation("查询授信录入明细表详情")
    @LogMessage(content = "查询授信录入明细表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<RatBkrationDetail> view(@PathVariable("id") String id) {
        RatBkrationDetail ratBkrationDetail = ratBkrationDetailService.get(id);
        return success(ratBkrationDetail);
    }
    /**
     * 添加授信录入明细表
     *
     * @param ratBkrationDetail
     * @return
     */
    @ApiOperation("添加授信录入明细表")
    @LogMessage(content = "添加授信录入明细表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody RatBkrationDetail ratBkrationDetail) {
        ratBkrationDetailService.save(ratBkrationDetail);
        return success();
    }
    /**
     * 修改授信录入明细表
     *
     * @param ratBkrationDetail
     * @return
     */
    @ApiOperation("修改授信录入明细表")
    @LogMessage(content = "修改授信录入明细表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson modify(@RequestBody RatBkrationDetail ratBkrationDetail) {
        RatBkrationDetail t = ratBkrationDetailService.get(ratBkrationDetail.getId());
        BeanUtils.copyNotNullProperties(ratBkrationDetail, t);
        ratBkrationDetailService.saveOrUpdate(t);
        return success();
    }
    /**
     * 删除授信录入明细表
     *
     * @param id
     * @return
     */
    @ApiOperation("删除授信录入明细表")
    @LogMessage(content = "删除授信录入明细表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        RatBkrationDetail ratBkrationDetail = ratBkrationDetailService.get(id);
        ratBkrationDetailService.deleteEntity(ratBkrationDetail);
        return success();
    }
    /**
     * 批量删除授信录入明细表
     *
     * @param idsDTO
     * @return
     */
    @ApiOperation("批量删除授信录入明细表")
    @LogMessage(content = "批量删除授信录入明细表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    public AjaxJson batchDelete(@RequestBody BisIDsDTO idsDTO) {
        for (String id : idsDTO.getIds().split(",")) {
            RatBkrationDetail ratBkrationDetail = ratBkrationDetailService.get(id);
            ratBkrationDetailService.deleteEntity(ratBkrationDetail);
        }
        return success();
    }
}
