package com.bytter.guarantee.web.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.api.IGuaWayServie;
import com.bytter.guarantee.domain.service.GuaContractInfoService;
import com.bytter.guarantee.domain.service.GuaExtContractEndService;
import com.bytter.guarantee.domain.service.GuaExtContractInfoService;
import com.bytter.guarantee.domain.vo.GuaContractWayVO;
import com.bytter.guarantee.domain.vo.GuaExtContractInfoVO;
import com.bytter.system.api.Ii18nService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * @author luxy
 * @Title: GuaExtContractEndController
 * @ProjectName T3(3.2)
 * @Description: 对外担保合同终止控制层
 * @date 2020/5/11 14:49
 */
@Controller
@RequestMapping("/cms/guarantee/guaExtContractEnd")
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
    @GetMapping("/list")
    public String list() {
        return "cms/guarantee/listGuaExtContractEnd";
    }
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        return guaExtContractInfoService.endDataTables(input, getCurrentUser());
    }
    @PostMapping("/terminate/{id}")
    @ResponseBody
    public AjaxJson terminate(@PathVariable("id") String id) {
        guaExtContractEndService.terminate(id, getCurrentUser());
        return success();
    }
    @PostMapping("/cancelEnd/{id}")
    @ResponseBody
    public AjaxJson cancelEnd(@PathVariable("id") String id) {
        guaExtContractEndService.cancelEnd(id);
        return success();
    }
    @GetMapping("/approveList")
    public String approveList(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/guarantee/listGuaExtContractEndApprove";
    }
    @PostMapping("/approveDataList")
    @ResponseBody
    public DataTablesOutput approveDataList(@RequestBody DataTablesInput input) {
        return guaExtContractInfoService.endApproveData(input, getCurrentUser());
    }
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
        return "cms/guarantee/viewGuaExtContractEndApprove";
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
        return "cms/guarantee/mobileGuaExtContractEnd";
    }
}
