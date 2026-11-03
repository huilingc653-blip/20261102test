package com.bytter.finance.saas.controller.equity;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.equity.entity.CmsEquityPayPlanDetail;
import com.bytter.finance.domain.equity.service.CmsEquityInfoService;
import com.bytter.finance.domain.equity.service.CmsEquityPayPlanDetailService;
import com.bytter.finance.domain.equity.service.CmsEquityPayPlanService;
import com.bytter.finance.domain.equity.service.CmsEquityPayPlanUpdateService;
import com.bytter.finance.domain.equity.vo.CmsEquityInfoDetailVO;
import com.bytter.finance.domain.equity.vo.CmsEquityPayPlanApproveListVO;
import com.bytter.finance.domain.equity.vo.CmsEquityPayPlanApproveVO;
import com.bytter.finance.domain.equity.vo.CmsEquityPayPlanDetailVO;
import com.bytter.finance.domain.equity.vo.CmsEquityPayPlanPageVO;
import com.bytter.finance.domain.equity.vo.CmsEquityPayPlanVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 缴付/过户计划登记
 */
@Api(value = "/v1-cms/cms/equity/payPlan", tags = "缴付/过户计划登记")
@RestController("v1CmsEquityPayPlanController")
@RequestMapping("/v1-cms/cms/equity/payPlan")
public class CmsEquityPayPlanController extends BaseController {

    @Autowired
    private CmsEquityPayPlanService cmsEquityPayPlanService;

    @Autowired
    private CmsEquityPayPlanUpdateService cmsEquityPayPlanUpdateService;

    @Autowired
    private CmsEquityPayPlanDetailService cmsEquityPayPlanDetailService;

    @Autowired
    private CmsEquityInfoService cmsEquityInfoService;

    /**
     * 缴付/过户计划登记列表 - 数据
     */
    @LogMessage(content = "查询缴付/过户计划登记列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @ApiOperation("查询缴付/过户计划登记列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> datatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(cmsEquityPayPlanService.getDataEquityPayPlanTable(input, getCurrentUser()));
    }

    /**
     * 缴付/过户计划修改列表 - 数据
     */
    @LogMessage(content = "查询缴付/过户计划修改列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @ApiOperation("查询缴付/过户计划修改列表")
    @PostMapping("/updateDatatable")
    public AjaxJson<DataTablesOutput> updateDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(cmsEquityPayPlanUpdateService.getDataEquityPayPlanUpdateTable(input, getCurrentUser()));
    }

    /**
     * 股权融资明细列表 - 数据
     */
    @ApiOperation("查询计划登记股权融资明细列表")
    @GetMapping("/infoDetailDatatable")
    public AjaxJson<List<CmsEquityInfoDetailVO>> infoDetailDatatable(String id) {
        List<String> bizStatuss = new ArrayList<>();
        bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_0);// 审批通过
        bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_1);// 计划审批打回
        bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_5);// 计划审批中
        // bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_10);//计划审批通过
        // 根据融资主信息id查询融资明细数据
        return success(cmsEquityInfoService.getDetails(id, bizStatuss));
    }

    /**
     * 股权融资明细列表 - 数据
     */
    @ApiOperation("查询计划修改股权融资明细列表")
    @GetMapping("/infoDetailUpdateDatatable")
    public AjaxJson<List<CmsEquityInfoDetailVO>> infoDetailUpdateDatatable(String id) {
        List<String> bizStatuss = new ArrayList<>();
        bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_10);// 计划登记审批通过
        bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_11);// 计划修改审批打回
        bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_15);// 计划修改审批中
        bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_20);// 计划审批通过
        bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_26);// 缴付中
        // 根据融资主信息id查询融资明细数据
        return success(cmsEquityInfoService.getDetails(id, bizStatuss));
    }

    /**
     * 计划登记页面数据
     *
     * @param equityCode 融资编号
     */
    @ApiOperation("计划登记页面数据")
    @GetMapping("/addPayPlanDetail/{equityCode}")
    public AjaxJson<CmsEquityPayPlanPageVO> addPayPlanDetail(@PathVariable("equityCode") String equityCode) {
        CmsEquityPayPlanPageVO result = new CmsEquityPayPlanPageVO();
        // 融资明细
        CmsEquityInfoDetailVO infoDetail = cmsEquityInfoService.getDetail(equityCode);
        result.setInfoDetail(infoDetail);
        // 计划主表
        CmsEquityPayPlanVO payPlanVO = cmsEquityPayPlanService.getPayPlanByEquityCodeOrId(null, equityCode);
        result.setPayPlanVO(payPlanVO);
        return success(result);
    }

    /**
     * 计划修改页面数据
     *
     * @param equityCode 融资编号
     */
    @ApiOperation("计划修改页面数据")
    @GetMapping("/updatePayPlanDetail/{equityCode}")
    public AjaxJson<CmsEquityPayPlanPageVO> updatePayPlanDetail(@PathVariable("equityCode") String equityCode) {
        CmsEquityPayPlanPageVO result = new CmsEquityPayPlanPageVO();
        // 融资明细
        CmsEquityInfoDetailVO infoDetail = cmsEquityInfoService.getDetail(equityCode);
        result.setInfoDetail(infoDetail);
        // 计划主表
        CmsEquityPayPlanVO payPlanVO = cmsEquityPayPlanService.getPayPlanByEquityCodeOrId(null, equityCode);
        result.setPayPlanVO(payPlanVO);
        return success(result);
    }

    /**
     * 加载计划明细数据
     *
     * @param equityCode 融资编号
     */
    @ApiOperation("加载计划明细数据")
    @GetMapping("/getPlanDetailByEquityCode")
    public AjaxJson<List<CmsEquityPayPlanDetailVO>> getPlanDetailByEquityCode(String equityCode) {
        // 计划明细
        return success(cmsEquityPayPlanDetailService.listPayPlanDetailByEquityCodeOrPlanId(null, equityCode));
    }

    /**
     * 缴付/过户计划登记保存
     *
     * @param vo 缴付/过户计划
     */
    @LogMessage(content = "缴付/过户计划登记保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @ApiOperation("缴付/过户计划登记保存")
    @PostMapping("/save")
    public AjaxJson save(@RequestBody CmsEquityPayPlanVO vo) {
        cmsEquityPayPlanService.save(vo, this.getCurrentUser());
        return success();
    }

    /**
     * 缴付/过户计划修改保存
     *
     * @param vo 缴付/过户计划
     */
    @LogMessage(content = "缴付/过户计划修改保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @ApiOperation("缴付/过户计划修改保存")
    @PostMapping("/payPlanUpdate")
    public AjaxJson payPlanUpdate(@RequestBody CmsEquityPayPlanVO vo) {
        cmsEquityPayPlanUpdateService.payPlanUpdate(vo, this.getCurrentUser());
        return success();
    }

    /**
     * 查看-数据
     *
     * @param id 计划id
     */
    @ApiOperation("查询缴付/过户计划登记详情")
    @GetMapping("/view/{id}")
    public AjaxJson<CmsEquityPayPlanPageVO> view(@PathVariable("id") String id) {
        CmsEquityPayPlanPageVO result = new CmsEquityPayPlanPageVO();
        if (!EmptyUtils.isEmpty(id)) {
            // 计划主表
            CmsEquityPayPlanVO payPlanVO = cmsEquityPayPlanService.getPayPlanByEquityCodeOrId(id, null);
            result.setPayPlanVO(payPlanVO);
            // 融资明细
            CmsEquityInfoDetailVO infoDetail = cmsEquityInfoService.getDetail(payPlanVO.getEquityCode());
            result.setInfoDetail(infoDetail);
        }
        return success(result);
    }

    /**
     * 根据融资编号查看-数据
     *
     * @param equityCode 融资编号
     */
    @ApiOperation("根据融资编号查询缴付/过户计划登记详情")
    @GetMapping("/viewByEquityCode/{equityCode}")
    public AjaxJson<CmsEquityPayPlanPageVO> viewByEquityCode(@PathVariable("equityCode") String equityCode) {
        CmsEquityPayPlanPageVO result = new CmsEquityPayPlanPageVO();
        if (!EmptyUtils.isEmpty(equityCode)) {
            // 计划主表
            CmsEquityPayPlanVO payPlanVO = cmsEquityPayPlanService.getPayPlanByEquityCodeOrId(null, equityCode);
            result.setPayPlanVO(payPlanVO);
            // 融资明细
            CmsEquityInfoDetailVO infoDetail = cmsEquityInfoService.getDetail(equityCode);
            result.setInfoDetail(infoDetail);
            result.setIsApprove(false);
        }
        return success(result);
    }

    /**
     * 保存明细
     *
     * @param detail 计划明细
     */
    @LogMessage(content = "计划明细表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @ApiOperation("保存计划明细")
    @PostMapping("/saveDetail")
    public AjaxJson saveDetail(@RequestBody CmsEquityPayPlanDetail detail) {
        return cmsEquityPayPlanService.saveDetail(detail);
    }

    /**
     * 导入保存计划明细
     *
     * @param list 计划明细集合
     */
    @ApiOperation("导入保存计划明细")
    @PostMapping("/saveImport")
    public AjaxJson saveImport(@RequestBody List<CmsEquityPayPlanDetailVO> list) {
        return cmsEquityPayPlanService.saveImport(list);
    }

    /******************** 审批 **********************/

    /**
     * 缴付/过户计划登记审批列表-页面数据
     */
    @LogMessage(content = "查询缴付/过户计划登记审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @ApiOperation("查询缴付/过户计划登记审批列表")
    @GetMapping("/listApprove")
    public AjaxJson<CmsEquityPayPlanApproveListVO> listApprove(String flowStatus, String cycleTime) {
        CmsEquityPayPlanApproveListVO result = new CmsEquityPayPlanApproveListVO();
        result.setFlowStatus(flowStatus);
        result.setCycleTime(cycleTime);
        return success(result);
    }

    /**
     * 缴付/过户计划修改审批列表-页面数据
     */
    @LogMessage(content = "查询缴付/过户计划修改审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @ApiOperation("查询缴付/过户计划修改审批列表")
    @GetMapping("/listUpdateApprove")
    public AjaxJson<CmsEquityPayPlanApproveListVO> listUpdateApprove(String flowStatus, String cycleTime) {
        CmsEquityPayPlanApproveListVO result = new CmsEquityPayPlanApproveListVO();
        result.setFlowStatus(flowStatus);
        result.setCycleTime(cycleTime);
        return success(result);
    }

    /**
     * 缴付/过户计划登记审批列表 - 数据
     */
    @ApiOperation("查询缴付/过户计划登记审批列表数据")
    @PostMapping("/datatableApprove")
    public AjaxJson<DataTablesOutput> datatableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(cmsEquityPayPlanService.getDatatableApprove(input));
    }

    /**
     * 缴付/过户计划修改审批列表 - 数据
     */
    @ApiOperation("查询缴付/过户计划修改审批列表数据")
    @PostMapping("/datatableUpdateApprove")
    public AjaxJson<DataTablesOutput> datatableUpdateApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(cmsEquityPayPlanUpdateService.datatableUpdateApprove(input));
    }

    /**
     * 申请审批 详情页面数据
     */
    @ApiOperation("缴付/过户计划登记审批详情")
    @GetMapping("/infoApprove")
    public AjaxJson<CmsEquityPayPlanApproveVO> infoApprove(String id, String taskId) {
        CmsEquityPayPlanApproveVO result = new CmsEquityPayPlanApproveVO();
        if (!EmptyUtils.isEmpty(id)) {
            // 计划主表
            CmsEquityPayPlanVO payPlanVO = cmsEquityPayPlanService.getPayPlanByEquityCodeOrId(id, null);
            result.setPayPlanVO(payPlanVO);
            // 融资明细
            CmsEquityInfoDetailVO infoDetail = cmsEquityInfoService.getDetail(payPlanVO.getEquityCode());
            result.setInfoDetail(infoDetail);
        }
        result.setTaskId(taskId);// 必须要做此步骤
        return success(result);
    }
}
