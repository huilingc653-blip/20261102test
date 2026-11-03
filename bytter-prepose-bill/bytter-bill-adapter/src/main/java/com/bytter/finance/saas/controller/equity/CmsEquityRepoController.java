package com.bytter.finance.saas.controller.equity;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.finance.domain.equity.dto.CmsEquityRepoSubmitDTO;
import com.bytter.finance.domain.equity.entity.CmsEquityInfo;
import com.bytter.finance.domain.equity.entity.CmsEquityRepo;
import com.bytter.finance.domain.equity.service.CmsEquityInfoService;
import com.bytter.finance.domain.equity.service.CmsEquityRepoPlanDetailService;
import com.bytter.finance.domain.equity.service.CmsEquityRepoPlanService;
import com.bytter.finance.domain.equity.service.CmsEquityRepoService;
import com.bytter.finance.domain.equity.vo.CmsEquityInfoDetailVO;
import com.bytter.finance.domain.equity.vo.CmsEquityRepoApproveListVO;
import com.bytter.finance.domain.equity.vo.CmsEquityRepoApproveVO;
import com.bytter.finance.domain.equity.vo.CmsEquityRepoPageVO;
import com.bytter.finance.domain.equity.vo.CmsEquityRepoPlanDetailVO;
import com.bytter.finance.domain.equity.vo.CmsEquityRepoPlanVO;
import com.bytter.finance.domain.equity.vo.CmsEquityRepoPlansVO;
import com.bytter.finance.domain.equity.vo.CmsEquityRepoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 股权融资回购
 */
@Api(value = "/v1-cms/cms/equity/repo", tags = "股权融资回购")
@RestController("v1CmsEquityRepoController")
@RequestMapping("/v1-cms/cms/equity/repo")
public class CmsEquityRepoController extends BaseController {
    @Autowired
    private CmsEquityInfoService cmsEquityInfoService;
    @Autowired
    private CmsEquityRepoService cmsEquityRepoService;
    @Autowired
    private CmsEquityRepoPlanService cmsEquityRepoPlanService;
    @Autowired
    private CmsEquityRepoPlanDetailService cmsEquityRepoPlanDetailService;

    /**
     * 查询股权融资列表 - 数据
     */
    @LogMessage(content = "查询股权融资列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("查询股权融资列表")
    @PostMapping("/datatablePass")
    public AjaxJson<DataTablesOutput> datatablePass(@RequestBody V1DataTablesInput dataTablesInput) {
        List<String> bizStatus = new ArrayList<>();
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_56);// 还未全部完成，还在过程中
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_40);// 登记计划审批通过
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_50);// 修改计划审批通过
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_52);// 暂存
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_55);// 审批中
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_51);// 打回
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(cmsEquityInfoService.getDataEquityPassTable(input, getCurrentUser(),
                ResourceConstant.CMS_EQUITY_REPO_VIEW, bizStatus));
    }

    /**
     * 缴付明细列表
     */
    @ApiOperation("查询缴付明细列表")
    @GetMapping("/equityDetails/{id}")
    public AjaxJson<List<CmsEquityInfoDetailVO>> equityDetails(@PathVariable("id") String id) {
        List<String> bizStatus = new ArrayList<>();
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_56);// 还未全部完成，还在过程中
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_40);// 登记计划审批通过
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_50);// 修改计划审批通过
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_52);// 暂存
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_55);// 审批中
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_51);// 打回
        return success(cmsEquityInfoService.getDetails(id, bizStatus));
    }

    /**
     * 回购页面数据
     */
    @ApiOperation("回购页面数据")
    @GetMapping("/repoEquity")
    public AjaxJson<CmsEquityRepoPageVO> repoEquity(String equityCode, String type) {
        CmsEquityRepoPageVO result = new CmsEquityRepoPageVO();
        CmsEquityRepoVO repoVO = new CmsEquityRepoVO();
        CmsEquityInfoDetailVO detailVO = cmsEquityInfoService.getDetail(equityCode);
        CmsEquityInfo info = cmsEquityInfoService.get(detailVO.getEquityId());
        result.setCorpId(info.getCorpId());
        result.setCurId(info.getCurId());
        if (type.equals("3")) {
            repoVO = cmsEquityRepoService.getByCode(equityCode, "temp").get(0);
        } else {
            // 计划主表
            CmsEquityRepoPlanVO repoPlanVO = cmsEquityRepoPlanService.getRepoPlanByEquityCodeOrId(null, equityCode);
            repoVO.setRepoPlanId(repoPlanVO.getId());
            repoVO.setRepoMoney(repoPlanVO.getRepoMoney());
            cmsEquityRepoService.createRepo(repoVO, detailVO, type);
        }
        result.setRepoVO(repoVO);
        return success(result);
    }

    /**
     * 回购计划明细列表
     */
    @ApiOperation("查询回购计划明细列表")
    @GetMapping("/equityPlans")
    public AjaxJson<CmsEquityRepoPlansVO> equityPlans(String equityCode, String repoPlanDetailId) {
        CmsEquityRepoPlansVO result = new CmsEquityRepoPlansVO();
        // 计划明细
        List<CmsEquityRepoPlanDetailVO> planDetailList = cmsEquityRepoPlanDetailService
                .listRepoPlanDetailByEquityCodeOrPlanId(null, equityCode);
        result.setPlanDetailList(planDetailList);
        result.setDetailPlanID(repoPlanDetailId);
        return success(result);
    }

    /**
     * 提交/暂存回购
     */
    @LogMessage(content = "回购表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @ApiOperation("提交/暂存回购")
    @PostMapping("/save")
    public AjaxJson save(@RequestBody CmsEquityRepoSubmitDTO dto) {
        cmsEquityRepoService.save(dto.getVo(), dto.getSaveType(), getCurrentUser());
        return success();
    }

    /**
     * 查看回购详情
     */
    @ApiOperation("查看回购详情")
    @GetMapping("/view")
    public AjaxJson<CmsEquityRepoVO> view(String equityCode) {
        if (StringUtils.isEmpty(equityCode) || "undefined".equals(equityCode)) {
            return success(null);
        }
        return success(cmsEquityRepoService.getByCode(equityCode, "comm").get(0));// 找审批中的缴付
    }

    /**
     * 删除暂存的回购
     */
    @ApiOperation("删除暂存的回购")
    @PostMapping("/delete")
    public AjaxJson delete(String equityCode) {
        cmsEquityRepoService.deleteRepo(equityCode, getCurrentUser());
        return success();
    }

    /******************** 审批 **********************/

    /**
     * 股权缴付过户审批列表
     */
    @LogMessage(content = "查询股权缴付过户审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("查询股权缴付过户审批列表")
    @GetMapping("/listApprove")
    public AjaxJson<CmsEquityRepoApproveListVO> listApprove(String flowStatus, String cycleTime) {
        CmsEquityRepoApproveListVO result = new CmsEquityRepoApproveListVO();
        result.setFlowStatus(flowStatus);
        result.setCycleTime(cycleTime);
        return success(result);
    }

    /**
     * 股权缴付过户审批列表 - 数据
     */
    @ApiOperation("查询股权缴付过户审批列表数据")
    @PostMapping("/datatableApprove")
    public AjaxJson<DataTablesOutput> datatableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(cmsEquityRepoService.getDatatableApprove(input));
    }

    /**
     * 申请审批 详情
     */
    @ApiOperation("申请审批详情")
    @GetMapping("/payApprove")
    public AjaxJson<CmsEquityRepoApproveVO> payApprove(String id, String taskId) {
        CmsEquityRepoApproveVO result = new CmsEquityRepoApproveVO();
        if (!EmptyUtils.isEmpty(id)) {
            result.setRepo(cmsEquityRepoService.get(id));
        }
        result.setTaskId(taskId);// 必须要做此步骤
        return success(result);
    }
}
