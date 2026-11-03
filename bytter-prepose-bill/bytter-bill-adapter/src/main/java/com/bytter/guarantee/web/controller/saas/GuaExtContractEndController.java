package com.bytter.guarantee.web.controller.saas;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.api.IGuaWayServie;
import com.bytter.guarantee.domain.service.GuaContractInfoService;
import com.bytter.guarantee.domain.service.GuaExtContractEndService;
import com.bytter.guarantee.domain.service.GuaExtContractInfoService;
import com.bytter.guarantee.domain.vo.GuaContractInfoPageVO;
import com.bytter.guarantee.domain.vo.GuaContractWayVO;
import com.bytter.guarantee.domain.vo.GuaExtContractInfoPageVO;
import com.bytter.guarantee.domain.vo.GuaExtContractInfoVO;
import com.bytter.system.api.Ii18nService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author luxy
 * @Title: GuaExtContractEndController
 * @ProjectName T3(3.2)
 * @Description: 对外担保合同终止控制层
 * @date 2020/5/11 14:49
 */
@Api(tags = "对外担保合同终止", value = "对外担保合同终止")
@RestController("v1GuaExtContractEndController")
@RequestMapping("/v1-guarantee/cms/guarantee/guaExtContractEnd")
public class GuaExtContractEndController extends BaseController {
    @Autowired
    private GuaExtContractEndService guaExtContractEndService;
    @Autowired
    private GuaExtContractInfoService guaExtContractInfoService;
    @DubboReference(check = false)
    private Ii18nService ii18nService;
    @Autowired
    private IGuaWayServie iGuaWayServie;
    @Autowired
    private GuaContractInfoService guaContractInfoService;
    @ApiOperation(value = "对外担保合同终止列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput input) {
        DataTablesInput dataTablesInput = input.convertAndInitColumns();
        return success(guaExtContractInfoService.endDataTables(dataTablesInput, getCurrentUser()));
    }
    @ApiOperation(value = "对外担保合同终止")
    @PostMapping("/terminate/{id}")
    @ResponseBody
    public AjaxJson terminate(@PathVariable("id") String id) {
        guaExtContractEndService.terminate(id, getCurrentUser());
        return success();
    }
    @ApiOperation(value = "对外担保合同终止取消")
    @PostMapping("/cancelEnd/{id}")
    @ResponseBody
    public AjaxJson cancelEnd(@PathVariable("id") String id) {
        guaExtContractEndService.cancelEnd(id);
        return success();
    }
    @ApiOperation(value = "对外担保合同终止审批列表")
    @PostMapping("/approveDataList")
    @ResponseBody
    public AjaxJson<DataTablesOutput> approveDataList(@RequestBody V1DataTablesInput input) {
        DataTablesInput dataTablesInput = input.convertAndInitColumns();
        return success(guaExtContractInfoService.endApproveData(dataTablesInput, getCurrentUser()));
    }
    @GetMapping("/viewApprove")
    public AjaxJson<GuaExtContractInfoPageVO> viewApprove(String id, String taskId, Model model) {
        GuaExtContractInfoPageVO pageVO = new GuaExtContractInfoPageVO();
        if (StringUtils.isNotEmpty(id)) {
            GuaExtContractInfoVO vo = guaExtContractInfoService.getGuaExtContractInfoById(id);
            if (vo != null) {
                pageVO.setVo(vo);
            }
            String guaWays = guaContractInfoService.setGuaWayS(id);
            if (!EmptyUtils.isBlank(guaWays)) {
                String guaWayNames = iGuaWayServie.getGuaWayNameByIds(guaWays);
                pageVO.setGuaWays(guaWays);
                pageVO.setGuaWayNames(guaWayNames);
            }
        }
        pageVO.setTaskId(taskId);
        return success(pageVO);
    }
    /**
     * 查看手机审批详情
     */
    @ApiOperation(value = "查看手机审批详情")
    @GetMapping("/mobileApprove")
    public AjaxJson<GuaContractInfoPageVO> mobileApprove(String businessId, String taskId, Model model) {
        GuaExtContractInfoPageVO pageVO = new GuaExtContractInfoPageVO();
        if (StringUtils.isNotEmpty(businessId)) {
            GuaExtContractInfoVO vo = guaExtContractInfoService.getGuaExtContractInfoById(businessId);
            if (vo != null) {
                pageVO.setVo(vo);
            }
            // 设置担保方式和担保金额
            List<GuaContractWayVO> guaContractWayVOS = vo.getGuaDtls();// 合同关联担保方式信息
            if (guaContractWayVOS != null && guaContractWayVOS.size() > 0) {
                String guaWayNames = guaContractWayVOS.stream()
                        .map(GuaContractWayVO::getGuaWayName)
                        .collect(Collectors.joining(","));
                pageVO.setGuaWayNames(guaWayNames);
                pageVO.setGuaWayVOList(guaContractWayVOS);
            }
        }
        pageVO.setTaskId(taskId);
        return success(pageVO);
    }
}
