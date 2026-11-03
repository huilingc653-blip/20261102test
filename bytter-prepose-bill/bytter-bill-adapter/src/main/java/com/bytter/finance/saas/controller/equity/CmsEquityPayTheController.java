package com.bytter.finance.saas.controller.equity;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.finance.domain.equity.dto.CmsEquityPayTheSubmitDTO;
import com.bytter.finance.domain.equity.entity.CmsEquityInfo;
import com.bytter.finance.domain.equity.entity.CmsEquityPayThe;
import com.bytter.finance.domain.equity.service.CmsEquityInfoService;
import com.bytter.finance.domain.equity.service.CmsEquityPayPlanDetailService;
import com.bytter.finance.domain.equity.service.CmsEquityPayPlanService;
import com.bytter.finance.domain.equity.service.CmsEquityPayTheService;
import com.bytter.finance.domain.equity.vo.CmsEquityInfoDetailVO;
import com.bytter.finance.domain.equity.vo.CmsEquityPayPlanDetailVO;
import com.bytter.finance.domain.equity.vo.CmsEquityPayPlanVO;
import com.bytter.finance.domain.equity.vo.CmsEquityPayTheApproveListVO;
import com.bytter.finance.domain.equity.vo.CmsEquityPayTheApproveVO;
import com.bytter.finance.domain.equity.vo.CmsEquityPayThePageVO;
import com.bytter.finance.domain.equity.vo.CmsEquityPayThePlansVO;
import com.bytter.finance.domain.equity.vo.CmsEquityPayTheVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 股权融资缴付到账
 */
@Api(value = "/v1-cms/cms/equity/payThe", tags = "股权融资缴付到账")
@RestController("v1CmsEquityPayTheController")
@RequestMapping("/v1-cms/cms/equity/payThe")
public class CmsEquityPayTheController extends BaseController {
    @Autowired
    private CmsEquityInfoService cmsEquityInfoService;
    @Autowired
    private CmsEquityPayTheService cmsEquityPayTheService;
    @Autowired
    private CmsEquityPayPlanDetailService cmsEquityPayPlanDetailService;
    @Autowired
    private CmsEquityPayPlanService cmsEquityPayPlanService;

    /**
     * 股权融资列表 - 数据
     */
    @LogMessage(content = "查询股权融资列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("查询股权融资列表")
    @PostMapping("/datatablePass")
    public AjaxJson<DataTablesOutput> datatablePass(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        List<String> bizStatus = new ArrayList<>();
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_26);// 还未全部完成，还在过程中
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_10);// 登记计划审批通过
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_20);// 修改计划审批通过
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_22);// 暂存
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_25);// 审批中
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_21);// 打回
        return success(cmsEquityInfoService.getDataEquityPassTable(input, getCurrentUser(),
                ResourceConstant.CMS_EQUITY_PAY_VIEW, bizStatus));
    }

    /**
     * 缴付明细列表
     */
    @ApiOperation("查询股权融资缴付明细列表")
    @GetMapping("/equityDetails/{id}")
    public AjaxJson<List<CmsEquityInfoDetailVO>> equityDetails(@PathVariable("id") String id) {
        List<String> bizStatus = new ArrayList<>();
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_26);// 还未全部完成，还在过程中
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_10);// 登记计划审批通过
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_20);// 修改计划审批通过
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_22);// 暂存
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_25);// 审批中
        bizStatus.add(CmsConstant.EQUITY_DETAIL_BIZSTATUS_21);// 打回
        return success(cmsEquityInfoService.getDetails(id, bizStatus));
    }

    /**
     * 缴付/过户页面数据
     */
    @ApiOperation("缴付/过户页面数据")
    @GetMapping("/payEquity")
    public AjaxJson<CmsEquityPayThePageVO> payEquity(String equityCode, String bizStatus) {
        CmsEquityPayThePageVO result = new CmsEquityPayThePageVO();
        CmsEquityPayTheVO payTheVO = new CmsEquityPayTheVO();
        CmsEquityInfoDetailVO detailVO = cmsEquityInfoService.getDetail(equityCode);
        CmsEquityInfo info = cmsEquityInfoService.get(detailVO.getEquityId());
        result.setCorpId(info.getCorpId());
        result.setCurId(info.getCurId());
        if (bizStatus.equals(CmsConstant.EQUITY_DETAIL_BIZSTATUS_22)
                || bizStatus.equals(CmsConstant.EQUITY_DETAIL_BIZSTATUS_21)) {
            payTheVO = cmsEquityPayTheService.getByCode(equityCode, "temp").get(0);// 找暂存打回的缴付
        } else {
            cmsEquityPayTheService.createPayThe(payTheVO, detailVO);
            payTheVO.setPayCurId(info.getCurId());
            // 计划主表
            CmsEquityPayPlanVO payPlanVO = cmsEquityPayPlanService.getPayPlanByEquityCodeOrId(null, equityCode);
            payTheVO.setPlanId(payPlanVO.getId());
        }
        result.setPayTheVO(payTheVO);
        return success(result);
    }

    /**
     * 缴付明细列表
     */
    @ApiOperation("查询缴付计划明细列表")
    @GetMapping("/equityPlans")
    public AjaxJson<CmsEquityPayThePlansVO> equityPlans(String equityCode, String planDetailId) {
        CmsEquityPayThePlansVO result = new CmsEquityPayThePlansVO();
        // 计划明细
        List<CmsEquityPayPlanDetailVO> planDetailList = cmsEquityPayPlanDetailService
                .listPayPlanDetailByEquityCodeOrPlanId(null, equityCode);
        result.setPlanDetailList(planDetailList);
        result.setDetailPlanID(planDetailId);
        return success(result);
    }

    /**
     * 提交
     */
    @LogMessage(content = "缴付到账表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @ApiOperation("提交缴付到账")
    @PostMapping("/savePay")
    public AjaxJson savePay(@RequestBody CmsEquityPayTheSubmitDTO dto) {
        cmsEquityPayTheService.savePay(dto.getVo(), dto.getSaveType(), getCurrentUser());
        return success();
    }

    /**
     * 折合币别
     *
     * @param curId      币别id
     * @param payCurId   缴付币别id
     * @param curPreTime 折算日期
     */
    @ApiOperation("查询折合币别")
    @GetMapping("/conver")
    public AjaxJson<BigDecimal> getConverRate(String curId, String payCurId, String curPreTime) {
        return success(cmsEquityPayTheService.getConverRate(curId, payCurId, DateUtils.parse(curPreTime, "yyyy-MM-dd")));
    }

    /**
     * 查看-页面数据
     */
    @ApiOperation("查看缴付到账")
    @GetMapping("/viewPay")
    public AjaxJson<CmsEquityPayTheVO> viewPay(String equityCode) {
        if (StringUtils.isEmpty(equityCode) || "undefined".equals(equityCode)) {
            return success(null);
        }
        return success(cmsEquityPayTheService.getByCode(equityCode, "comm").get(0));// 找审批中的缴付
    }

    /******************** 审批 **********************/
    /**
     * 股权缴付过户审批列表
     */
    @LogMessage(content = "查询股权缴付过户审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("查询股权缴付过户审批列表")
    @GetMapping("/listApprove")
    public AjaxJson<CmsEquityPayTheApproveListVO> listApprove(String flowStatus, String cycleTime) {
        CmsEquityPayTheApproveListVO result = new CmsEquityPayTheApproveListVO();
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
        return success(cmsEquityPayTheService.getDatatableApprove(input));
    }

    /**
     * 申请审批 详情页面数据
     */
    @ApiOperation("申请审批详情页面数据")
    @GetMapping("/payApprove")
    public AjaxJson<CmsEquityPayTheApproveVO> payApprove(String id, String taskId) {
        CmsEquityPayTheApproveVO result = new CmsEquityPayTheApproveVO();
        if (!EmptyUtils.isEmpty(id)) {
            CmsEquityPayThe payThe = cmsEquityPayTheService.get(id);
            result.setPayThe(cmsEquityPayTheService.getByCode(payThe.getEquityCode(), "comm").get(0));// 找审批中的缴付
        }
        result.setTaskId(taskId);// 必须要做此步骤
        return success(result);
    }
}
