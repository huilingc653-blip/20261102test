package com.bytter.guarantee.web.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
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
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author luxy
 * @Title: GuaExtContractInfoController
 * @ProjectName T3(3.2)
 * @Description: 对外担保合同登记控制层
 * @date 2020/5/9 9:35
 */
@Controller
@RequestMapping("/cms/guarantee/guaExtContractInfo")
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
     * 对外担保合同列表
     *
     * @param model
     * @return
     */
    @GetMapping("/list")
    @LogMessage(content = "跳转到对外担保合同登记列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String list(Model model) {
        return "cms/guarantee/listGuaExtContractInfo";
    }
    /**
     * 列表内容
     *
     * @param dataTablesInput
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return guaExtContractInfoService.getDataTables(dataTablesInput, this.getCurrentUser());
    }
    /**
     * 跳转到新增对外担保合同登记页面
     */
    @GetMapping("/add")
    @LogMessage(content = "跳转到新增对外担保合同登记页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String addGuaPledgeInfo(Model model) {
        // 担保对接合同管理
        String relateCon = guaExtContractInfoService.relateCon();
        if (GuaranteeConstant.RELATE_CON_YES.equals(relateCon)) {
            String conType = guaExtContractInfoService.conType();
            model.addAttribute("conType", conType);
        }
        model.addAttribute("relateCon", relateCon);
        return "cms/guarantee/addGuaExtContractInfo";
    }
    /**
     * 跳转到还款计划页面（可以修改数据:其他）
     *
     * @param repayPlanVO
     * @param model
     * @return
     */
    @GetMapping("/toRepayPlan")
    public String toRepayPlan(RepayPlanVO repayPlanVO, Model model) {
        model.addAttribute("repayPlanVO", repayPlanVO);
        model.addAttribute("repayType", "07");
        return "cms/guarantee/drawdownPeriodRepayPlan";
    }
    /**
     * 保存还款计划
     *
     * @param rp
     * @return
     */
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(RepayPlanVO rp) {
        AjaxJson json = success();
        json.setObj(guaranteePlanService.save(rp, getCurrentUser()));
        return json;
    }
    /**
     * 保存担保合同信息
     */
    @PostMapping("/tempOrSubmit")
    @ResponseBody
    public AjaxJson savePledgeInfo(GuaContractInfoVO contractVO) {
        this.handelData(contractVO);
        contractVO.setBusinessType(GuaranteeConstant.CON_TYPE_1);// 担保合同类型为对外担保合同
        guaExtContractInfoService.tempOrSubmit(contractVO, this.getCurrentUser());
        guaranteePlanService.updateBillCodeByIds(contractVO.getPlanId(), contractVO.getId());
        return success();
    }
    /**
     * 修改对外担保合同登记页面跳转
     */
    @LogMessage(content = "修改对外担保合同", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            GuaExtContractInfoVO vo = guaExtContractInfoService.getGuaExtContractInfoById(id);
            if (vo != null) {
                model.addAttribute("vo", vo);
            }
            String guaWays = guaContractInfoService.setGuaWayS(id);
            if (!EmptyUtils.isBlank(guaWays)) {
                model.addAttribute("guaWays", guaWays);
            }
            // 对接合同管理
            String relateCon = guaExtContractInfoService.relateCon();
            if (GuaranteeConstant.RELATE_CON_YES.equals(relateCon)) {// 担保对接合同管理
                String conType = guaExtContractInfoService.conType();
                model.addAttribute("conType", conType);
                model.addAttribute("conId", vo.getConId());
                model.addAttribute("conTypeId", vo.getConTypeId());
            }
            model.addAttribute("relateCon", relateCon);
        }
        return "cms/guarantee/updateGuaExtContractInfo";
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
    @LogMessage(content = "查看担保合同信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewInfo/{id}")
    public String viewGuaContractInfo(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            GuaExtContractInfoVO vo = new GuaExtContractInfoVO();
            vo = guaExtContractInfoService.getGuaExtContractInfoById(id);
            if (vo != null) {
                model.addAttribute("vo", vo);
            }
            String guaWays = guaContractInfoService.setGuaWayS(id);
            model.addAttribute("guaWays", guaWays);
            String guaWayNames = iGuaWayServie.getGuaWayNameByIds(guaWays);
            model.addAttribute("guaWayNames", guaWayNames);
        }
        return "cms/guarantee/viewGuaExtContractInfo";
    }
    /**
     * 担保合同审批列表页面跳转
     */
    @GetMapping("/listApprove")
    @LogMessage(content = "担保合同审批列表页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/guarantee/listGuaExtContractApprove";
    }
    /**
     * 审批列表
     */
    @PostMapping("/approveDataList")
    @ResponseBody
    public DataTablesOutput approveDataList(@RequestBody DataTablesInput dataTablesInput) {
        return guaExtContractInfoService.getApproveDataTables(dataTablesInput);
    }
    /**
     * 查看审批详情
     */
    @GetMapping("/viewApprove")
    public String viewApprove(String id, String taskId, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            GuaExtContractInfoVO vo = new GuaExtContractInfoVO();
            vo = guaExtContractInfoService.getGuaExtContractInfoById(id);
            if (vo != null) {
                model.addAttribute("vo", vo);
            }
            String guaWays = guaContractInfoService.setGuaWayS(id);
            if (!EmptyUtils.isBlank(guaWays)) {
                model.addAttribute("guaWays", guaWays);
                String guaWayNames = iGuaWayServie.getGuaWayNameByIds(guaWays);
                model.addAttribute("guaWayNames", guaWayNames);
            }
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/guarantee/viewGuaExtContractInfoApprove";
    }
    /**
     * 查看手机审批详情
     */
    @GetMapping("/mobileApprove")
    public String mobileApprove(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            GuaExtContractInfoVO vo = new GuaExtContractInfoVO();
            vo = guaExtContractInfoService.getGuaExtContractInfoById(businessId);
            if (vo != null) {
                model.addAttribute("vo", vo);
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
                model.addAttribute("guaWayNames", guaWayNames);
                model.addAttribute("guaWayVOList", guaContractWayVOS);
            }
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/guarantee/mobileGuaExtContractInfo";
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
     * 对外担保合同查询列表
     *
     * @param model
     * @return
     */
    @GetMapping("/searchlist")
    @LogMessage(content = "跳转到担保合同登记列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String searchlist(Model model) {
        String relateCon = guaExtContractInfoService.relateCon();
        model.addAttribute("relateCon", relateCon);
        return "cms/guarantee/listSearchGuaExtContract";
    }
    /**
     * 对外担保合同查询列表
     *
     * @param model
     * @return
     */
    @GetMapping("/searchlistPlan")
    @LogMessage(content = "跳转到担保合同登记列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public String searchlistPlan(Model model) {
        String relateCon = guaExtContractInfoService.relateCon();
        model.addAttribute("relateCon", relateCon);
        return "cms/guarantee/listSearchGuaExtContractPlan";
    }
    /**
     * 加载还款计划数据
     *
     * @param repayPlanVo
     * @return
     */
    @PostMapping("/getRepayMentScheduleByBillCode")
    @ResponseBody
    public List<RepaymentScheduleVO> listRepayMentSchedule(RepayPlanVO repayPlanVo) {
        List<RepaymentScheduleVO> list = guaranteePlanService.listRepayMentSchedule(repayPlanVo);
        return list;
    }
    /**
     * 对外担保合同查询列表内容
     *
     * @param dataTablesInput
     * @return
     */
    @PostMapping("/searchdatatable")
    @ResponseBody
    public DataTablesOutput searchDataTables(@RequestBody DataTablesInput dataTablesInput) {
        return guaExtContractInfoService.searchDataTables(dataTablesInput, this.getCurrentUser());
    }
}
