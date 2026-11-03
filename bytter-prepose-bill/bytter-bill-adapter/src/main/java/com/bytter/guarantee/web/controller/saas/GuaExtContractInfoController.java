package com.bytter.guarantee.web.controller.saas;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.api.IGuaWayServie;
import com.bytter.guarantee.constant.GuaranteeConstant;
import com.bytter.guarantee.domain.service.GuaContractInfoService;
import com.bytter.guarantee.domain.service.GuaExtContractInfoService;
import com.bytter.guarantee.domain.service.GuaranteePlanService;
import com.bytter.guarantee.domain.vo.*;
import com.bytter.guarantee.enums.GuaranteeMessageEnum;
import com.bytter.system.api.Ii18nService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author luxy
 * @Title: GuaExtContractInfoController
 * @ProjectName T3(3.2)
 * @Description: 对外担保合同登记控制层
 * @date 2020/5/9 9:35
 */
@Api(tags = "对外担保合同登记控制层", value = "/v1-guarantee/cms/guarantee/guaExtContractInfo")
@RestController("v1GuaExtContractInfoController")
@RequestMapping("/v1-guarantee/cms/guarantee/guaExtContractInfo")
public class GuaExtContractInfoController extends BaseController {
    @Autowired
    private GuaExtContractInfoService guaExtContractInfoService;
    @Autowired
    private GuaContractInfoService guaContractInfoService;
    @Autowired
    private IGuaWayServie iGuaWayServie;
    @Autowired
    private GuaranteePlanService guaranteePlanService;
    @DubboReference(check = false)
    private Ii18nService ii18nService;

    /**
     * 列表内容
     *
     * @param dataTablesInput
     * @return
     */
    @ApiOperation(value = "对外担保合同登记列表内容")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(guaExtContractInfoService.getDataTables(input, this.getCurrentUser()));
    }
    /**
     * 跳转到新增对外担保合同登记页面
     */
    @ApiOperation("获取对外担保合同登记配置")
    @GetMapping("/getGuaExtConfig")
    @LogMessage(content = "跳转到新增对外担保合同登记页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson<GuaExtContractConfigPage> getGuaExtConfig() {
        GuaExtContractConfigPage configPage = new GuaExtContractConfigPage();
        // 担保对接合同管理
        String relateCon = guaExtContractInfoService.relateCon();
        if (GuaranteeConstant.RELATE_CON_YES.equals(relateCon)) {
            String conType = guaExtContractInfoService.conType();
            configPage.setConType(conType);
        }
        configPage.setRelateCon(relateCon);
        return success(configPage);
    }

    /**
     * 保存还款计划
     *
     * @param rp
     * @return
     */
    @ApiOperation("保存还款计划")
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody RepayPlanVO rp) {
        AjaxJson json = success();
        json.setObj(guaranteePlanService.save(rp, getCurrentUser()));
        return json;
    }
    /**
     * 保存担保合同信息
     */
    @ApiOperation("暂存/提交担保合同信息")
    @PostMapping("/tempOrSubmit")
    @ResponseBody
    public AjaxJson savePledgeInfo(@RequestBody GuaContractInfoVO contractVO) {
        this.handelData(contractVO);
        contractVO.setBusinessType(GuaranteeConstant.CON_TYPE_1);// 担保合同类型为对外担保合同
        guaExtContractInfoService.tempOrSubmit(contractVO, this.getCurrentUser());
        guaranteePlanService.updateBillCodeByIds(contractVO.getPlanId(), contractVO.getId());
        return success();
    }
    /**
     * 修改对外担保合同登记页面跳转
     */
    @ApiOperation("修改对外担保合同登记数据获取")
    @LogMessage(content = "修改对外担保合同", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @GetMapping("/update/{id}")
    public AjaxJson<GuaExtContractInfoPageVO> update(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            GuaExtContractInfoPageVO pageVO = new GuaExtContractInfoPageVO();
            GuaExtContractInfoVO vo = guaExtContractInfoService.getGuaExtContractInfoById(id);
            if (vo != null) {
                pageVO.setVo(vo);
            }
            String guaWays = guaContractInfoService.setGuaWayS(id);
            if (!EmptyUtils.isBlank(guaWays)) {
                pageVO.setGuaWays(guaWays);
            }
            // 对接合同管理
            String relateCon = guaExtContractInfoService.relateCon();
            if (GuaranteeConstant.RELATE_CON_YES.equals(relateCon)) {// 担保对接合同管理
                String conType = guaExtContractInfoService.conType();
                pageVO.setConType(conType);
                pageVO.setRelateCon(relateCon);
                pageVO.setConTypeId(vo.getConTypeId());
            }
            pageVO.setRelateCon(relateCon);
            return success(pageVO);
        }
        return success();
    }
    @LogMessage(content = "删除合同信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delGuaContractInfo(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            guaExtContractInfoService.deleteContract(id, this.getCurrentUser());
        }
        return success();
    }
    @ApiOperation("查看担保合同信息")
    @LogMessage(content = "查看担保合同信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewInfo/{id}")
    public AjaxJson<GuaExtContractInfoPageVO> viewGuaContractInfo(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            GuaExtContractInfoPageVO pageVO = new GuaExtContractInfoPageVO();
            GuaExtContractInfoVO vo = new GuaExtContractInfoVO();
            vo = guaExtContractInfoService.getGuaExtContractInfoById(id);
            if (vo != null) {
                pageVO.setVo(vo);
            }
            String guaWays = guaContractInfoService.setGuaWayS(id);
            pageVO.setGuaWays(guaWays);
            String guaWayNames = iGuaWayServie.getGuaWayNameByIds(guaWays);
            pageVO.setGuaWayNames(guaWayNames);
            return success(pageVO);
        }
        return success();
    }
    /**
     * 审批列表
     */
    @ApiOperation("对外担保合同登记审批列表")
    @PostMapping("/approveDataList")
    @ResponseBody
    public AjaxJson<DataTablesOutput> approveDataList(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(guaExtContractInfoService.getApproveDataTables(input));
    }
    /**
     * 查看审批详情
     */
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
                pageVO.setGuaWays(guaWays);
                String guaWayNames = iGuaWayServie.getGuaWayNameByIds(guaWays);
                pageVO.setGuaWayNames(guaWayNames);
            }
        }
        pageVO.setTaskId(taskId);
        return success(pageVO);
    }
    /**
     * 查看手机审批详情
     */
    @GetMapping("/mobileApprove")
    public AjaxJson<GuaExtContractInfoPageVO> mobileApprove(String businessId, String taskId, Model model) {
        GuaExtContractInfoPageVO pageVO = new GuaExtContractInfoPageVO();
        if (StringUtils.isNotEmpty(businessId)) {
            GuaExtContractInfoVO vo = new GuaExtContractInfoVO();
            vo = guaExtContractInfoService.getGuaExtContractInfoById(businessId);
            if (vo != null) {
                pageVO.setVo(vo);
            }
            // 设置担保方式和担保金额
            List<GuaContractWayVO> guaContractWayVOS = vo.getGuaDtls();// 合同关联担保方式信息
            if (guaContractWayVOS != null && guaContractWayVOS.size() > 0) {
                String guaWayNames = "";
                int size = guaContractWayVOS.size();
                for (int i = 0; i < size; i++) {
                    if (size > 1) {
                        if (i == size - 1) {// 最后一项
                            guaWayNames += guaContractWayVOS.get(i).getGuaWayName();
                        } else {
                            guaWayNames += guaContractWayVOS.get(i).getGuaWayName() + ",";
                        }
                    } else {
                        guaWayNames += guaContractWayVOS.get(i).getGuaWayName();
                    }
                }
                pageVO.setGuaWayNames(guaWayNames);
                pageVO.setGuaWayVOList(guaContractWayVOS);
            }
        }
        pageVO.setTaskId(taskId);
        return success(pageVO);
    }

    /***
     * 担保方式明细处理
     *
     * @param contractVO 担保合同信息VO
     */
    private void handelData(GuaContractInfoVO contractVO) {
        List<GuaContractWayVO> guaDtls = contractVO.getGuaDtls();
        if (EmptyUtils.isEmptyList(guaDtls)) {
            throw new ParameterException(GuaranteeMessageEnum.CMS_GUARANTEE_GUACONTRACT_GUAWAY_NULL);
        }
        // 过滤无效数据（前端录入时，下标可能不连续）
        guaDtls = guaDtls.stream().filter(guaWay -> StringUtils.isNotEmpty(guaWay.getWayCodes())).collect(Collectors.toList());
        contractVO.setGuaDtls(guaDtls);
    }


    /**
     * 加载还款计划数据
     *
     * @return
     */
    @ApiOperation(value = "加载还款计划数据")
    @GetMapping("/getRepayMentScheduleByBillCode")
    @ResponseBody
    public AjaxJson<List<RepaymentScheduleVO>> listRepayMentSchedule(String billCode, String planId) {
        RepayPlanVO repayPlanVo = new RepayPlanVO();
        repayPlanVo.setBillCode(billCode);
        repayPlanVo.setPlanId(planId);
        List<RepaymentScheduleVO> list = guaranteePlanService.listRepayMentSchedule(repayPlanVo);
        return success(list);
    }
    /**
     * 对外担保合同查询列表内容
     *
     * @param dataTablesInput
     * @return
     */
    @ApiOperation(value = "对外担保合同查询列表内容")
    @PostMapping("/searchdatatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> searchDataTables(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(guaExtContractInfoService.searchDataTables(input, this.getCurrentUser()));
    }
}
