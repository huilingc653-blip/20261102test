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
import com.bytter.finance.domain.equity.entity.CmsEquityRepoPlanDetail;
import com.bytter.finance.domain.equity.service.CmsEquityInfoService;
import com.bytter.finance.domain.equity.service.CmsEquityRepoPlanDetailService;
import com.bytter.finance.domain.equity.service.CmsEquityRepoPlanService;
import com.bytter.finance.domain.equity.service.CmsEquityRepoPlanUpdateService;
import com.bytter.finance.domain.equity.vo.CmsEquityInfoDetailVO;
import com.bytter.finance.domain.equity.vo.CmsEquityRepoPlanApproveListVO;
import com.bytter.finance.domain.equity.vo.CmsEquityRepoPlanApproveVO;
import com.bytter.finance.domain.equity.vo.CmsEquityRepoPlanDetailVO;
import com.bytter.finance.domain.equity.vo.CmsEquityRepoPlanPageVO;
import com.bytter.finance.domain.equity.vo.CmsEquityRepoPlanVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 股权融资回购计划
 */
@Api(value = "/v1-cms/cms/equity/repoPlan", tags = "股权融资回购计划")
@RestController("v1CmsEquityRepoPlanController")
@RequestMapping("/v1-cms/cms/equity/repoPlan")
public class CmsEquityRepoPlanController extends BaseController {

    @Autowired
    private CmsEquityRepoPlanService cmsEquityRepoPlanService;

    @Autowired
    private CmsEquityRepoPlanUpdateService cmsEquityRepoPlanUpdateService;

    @Autowired
    private CmsEquityRepoPlanDetailService cmsEquityRepoPlanDetailService;

    @Autowired
    private CmsEquityInfoService cmsEquityInfoService;

    /**
     * 查询股权融资回购计划登记列表
     */
    @LogMessage(content = "查询股权融资回购计划登记列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @ApiOperation("查询股权融资回购计划登记列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> datatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(cmsEquityRepoPlanService.getDataEquityRepoPlanTable(input, getCurrentUser()));
    }

    /**
     * 查询股权融资回购计划修改列表
     */
    @LogMessage(content = "查询股权融资回购计划修改列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @ApiOperation("查询股权融资回购计划修改列表")
    @PostMapping("/updateDatatable")
    public AjaxJson<DataTablesOutput> updateDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(cmsEquityRepoPlanUpdateService.getDataEquityRepoPlanUpdateTable(input, getCurrentUser()));
    }

    /**
     * 查询股权融资明细列表 - 登记
     */
    @ApiOperation("查询股权融资明细列表（登记）")
    @GetMapping("/infoDetailDatatable")
    public AjaxJson<List<CmsEquityInfoDetailVO>> infoDetailDatatable(String id) {
        List<String> bizStatuss = new ArrayList<>();
        bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_30);// 缴付/过户完成
        bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_31);// 回购计划审批打回
        bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_35);// 回购计划审批中
        // bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_40);//回购计划审批通过
        // 根据融资主信息id查询融资明细数据
        return success(cmsEquityInfoService.getDetails(id, bizStatuss));
    }

    /**
     * 查询股权融资明细列表 - 修改
     */
    @ApiOperation("查询股权融资明细列表（修改）")
    @GetMapping("/infoDetailUpdateDatatable")
    public AjaxJson<List<CmsEquityInfoDetailVO>> infoDetailUpdateDatatable(String id) {
        List<String> bizStatuss = new ArrayList<>();
        bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_40);// 回购计划登记审批通过
        bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_41);// 回购计划修改审批打回
        bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_45);// 回购计划修改审批中
        bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_50);// 回购计划审批通过
        bizStatuss.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_56);// 回购中
        // 根据融资主信息id查询融资明细数据
        return success(cmsEquityInfoService.getDetails(id, bizStatuss));
    }

    /**
     * 回购计划登记页面数据
     *
     * @param equityCode 融资编号
     */
    @ApiOperation("回购计划登记页面数据")
    @GetMapping("/addRepoPlanDetail/{equityCode}")
    public AjaxJson<CmsEquityRepoPlanPageVO> addRepoPlanDetail(@PathVariable("equityCode") String equityCode) {
        CmsEquityRepoPlanPageVO result = new CmsEquityRepoPlanPageVO();
        // 融资明细
        CmsEquityInfoDetailVO infoDetail = cmsEquityInfoService.getDetail(equityCode);
        result.setInfoDetail(infoDetail);
        // 回购计划主表
        CmsEquityRepoPlanVO repoPlanVO = cmsEquityRepoPlanService.getRepoPlanByEquityCodeOrId(null, equityCode);
        result.setRepoPlanVO(repoPlanVO);
        return success(result);
    }

    /**
     * 回购计划修改页面数据
     *
     * @param equityCode 融资编号
     */
    @ApiOperation("回购计划修改页面数据")
    @GetMapping("/updateRepoPlanDetail/{equityCode}")
    public AjaxJson<CmsEquityRepoPlanPageVO> updateRepoPlanDetail(@PathVariable("equityCode") String equityCode) {
        CmsEquityRepoPlanPageVO result = new CmsEquityRepoPlanPageVO();
        // 融资明细
        CmsEquityInfoDetailVO infoDetail = cmsEquityInfoService.getDetail(equityCode);
        result.setInfoDetail(infoDetail);
        // 回购计划主表
        CmsEquityRepoPlanVO repoPlanVO = cmsEquityRepoPlanService.getRepoPlanByEquityCodeOrId(null, equityCode);
        result.setRepoPlanVO(repoPlanVO);
        return success(result);
    }

    /**
     * 加载回购计划明细数据
     *
     * @param equityCode 融资编号
     */
    @ApiOperation("加载回购计划明细数据")
    @GetMapping("/getRepoDetailByEquityCode")
    public AjaxJson<List<CmsEquityRepoPlanDetailVO>> getRepoDetailByEquityCode(String equityCode) {
        // 回购计划明细
        return success(cmsEquityRepoPlanDetailService.listRepoPlanDetailByEquityCodeOrPlanId(null, equityCode));
    }

    /**
     * 股权融资回购计划登记保存
     */
    @LogMessage(content = "股权融资回购计划登记保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @ApiOperation("股权融资回购计划登记保存")
    @PostMapping("/save")
    public AjaxJson save(@RequestBody CmsEquityRepoPlanVO vo) {
        cmsEquityRepoPlanService.save(vo, this.getCurrentUser());
        return success();
    }

    /**
     * 股权融资回购计划修改保存
     */
    @LogMessage(content = "股权融资回购计划修改保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @ApiOperation("股权融资回购计划修改保存")
    @PostMapping("/repoPlanUpdate")
    public AjaxJson repoPlanUpdate(@RequestBody CmsEquityRepoPlanVO vo) {
        cmsEquityRepoPlanUpdateService.repoPlanUpdate(vo, this.getCurrentUser());
        return success();
    }

    /**
     * 查看回购计划详情
     *
     * @param id 回购计划主表id
     */
    @ApiOperation("查看回购计划详情")
    @GetMapping("/view/{id}")
    public AjaxJson<CmsEquityRepoPlanPageVO> view(@PathVariable("id") String id) {
        CmsEquityRepoPlanPageVO result = new CmsEquityRepoPlanPageVO();
        if (!EmptyUtils.isEmpty(id)) {
            // 回购计划主表
            CmsEquityRepoPlanVO repoPlanVO = cmsEquityRepoPlanService.getRepoPlanByEquityCodeOrId(id, null);
            result.setRepoPlanVO(repoPlanVO);
            // 融资明细
            CmsEquityInfoDetailVO infoDetail = cmsEquityInfoService.getDetail(repoPlanVO.getEquityCode());
            result.setInfoDetail(infoDetail);
        }
        return success(result);
    }

    /**
     * 根据融资编号查看回购计划详情
     *
     * @param equityCode 融资编号
     */
    @ApiOperation("根据融资编号查看回购计划详情")
    @GetMapping("/viewByEquityCode/{equityCode}")
    public AjaxJson<CmsEquityRepoPlanPageVO> viewByEquityCode(@PathVariable("equityCode") String equityCode) {
        CmsEquityRepoPlanPageVO result = new CmsEquityRepoPlanPageVO();
        if (!EmptyUtils.isEmpty(equityCode)) {
            // 回购计划主表
            CmsEquityRepoPlanVO repoPlanVO = cmsEquityRepoPlanService.getRepoPlanByEquityCodeOrId(null, equityCode);
            result.setRepoPlanVO(repoPlanVO);
            // 融资明细
            CmsEquityInfoDetailVO infoDetail = cmsEquityInfoService.getDetail(equityCode);
            result.setInfoDetail(infoDetail);
            result.setIsApprove(false);
        }
        return success(result);
    }

    /**
     * 保存回购计划明细
     */
    @LogMessage(content = "回购计划明细表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @ApiOperation("保存回购计划明细")
    @PostMapping("/saveDetail")
    public AjaxJson saveDetail(@RequestBody CmsEquityRepoPlanDetail detail) {
        return cmsEquityRepoPlanService.saveDetail(detail);
    }

    /**
     * 批量导入保存回购计划明细
     */
    @ApiOperation("批量导入保存回购计划明细")
    @PostMapping("/saveImport")
    public AjaxJson saveImport(@RequestBody List<CmsEquityRepoPlanDetailVO> list) {
        return cmsEquityRepoPlanService.saveImport(list);
    }

    /******************** 审批 **********************/

    /**
     * 查询股权融资回购计划登记审批列表
     */
    @LogMessage(content = "查询股权融资回购计划登记审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @ApiOperation("查询股权融资回购计划登记审批列表")
    @GetMapping("/listApprove")
    public AjaxJson<CmsEquityRepoPlanApproveListVO> listApprove(String flowStatus, String cycleTime) {
        CmsEquityRepoPlanApproveListVO result = new CmsEquityRepoPlanApproveListVO();
        result.setFlowStatus(flowStatus);
        result.setCycleTime(cycleTime);
        return success(result);
    }

    /**
     * 查询股权融资回购计划修改审批列表
     */
    @LogMessage(content = "查询股权融资回购计划修改审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @ApiOperation("查询股权融资回购计划修改审批列表")
    @GetMapping("/listUpdateApprove")
    public AjaxJson<CmsEquityRepoPlanApproveListVO> listUpdateApprove(String flowStatus, String cycleTime) {
        CmsEquityRepoPlanApproveListVO result = new CmsEquityRepoPlanApproveListVO();
        result.setFlowStatus(flowStatus);
        result.setCycleTime(cycleTime);
        return success(result);
    }

    /**
     * 股权融资回购计划登记审批列表 - 数据
     */
    @ApiOperation("股权融资回购计划登记审批列表数据")
    @PostMapping("/datatableApprove")
    public AjaxJson<DataTablesOutput> datatableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(cmsEquityRepoPlanService.getDatatableApprove(input));
    }

    /**
     * 股权融资回购计划修改审批列表 - 数据
     */
    @ApiOperation("股权融资回购计划修改审批列表数据")
    @PostMapping("/datatableUpdateApprove")
    public AjaxJson<DataTablesOutput> datatableUpdateApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(cmsEquityRepoPlanUpdateService.datatableUpdateApprove(input));
    }

    /**
     * 申请审批详情页面数据
     */
    @ApiOperation("申请审批详情页面数据")
    @GetMapping("/infoApprove")
    public AjaxJson<CmsEquityRepoPlanApproveVO> infoApprove(String id, String taskId) {
        CmsEquityRepoPlanApproveVO result = new CmsEquityRepoPlanApproveVO();
        if (!EmptyUtils.isEmpty(id)) {
            // 回购计划主表
            CmsEquityRepoPlanVO repoPlanVO = cmsEquityRepoPlanService.getRepoPlanByEquityCodeOrId(id, null);
            result.setRepoPlanVO(repoPlanVO);
            // 融资明细
            CmsEquityInfoDetailVO infoDetail = cmsEquityInfoService.getDetail(repoPlanVO.getEquityCode());
            result.setInfoDetail(infoDetail);
        }
        result.setTaskId(taskId);// 必须要做此步骤
        return success(result);
    }
}
