package com.bytter.guarantee.web.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.domain.service.GuaContractChangeService;
import com.bytter.guarantee.domain.service.GuaExtContractChangeService;
import com.bytter.guarantee.domain.service.GuaranteePlanService;
import com.bytter.guarantee.domain.vo.GuaContractInfoVO;
import com.bytter.guarantee.domain.vo.GuaContractWayVO;
import com.bytter.guarantee.enums.GuaranteeMessageEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
/**
 * @author luxy
 * @Title: GuaExtContractChangeController
 * @ProjectName T3(3.2)
 * @Description: 对外担保合同变更控制层
 * @date 2020/5/11 11:10
 */
@Controller
@RequestMapping("/cms/guarantee/guaExtContractChange")
public class GuaExtContractChangeController extends BaseController {
    @Autowired
    private GuaExtContractChangeService guaExtContractChangeService;
    @Autowired
    private GuaContractChangeService guaContractChangeService;
    @Autowired
    private GuaranteePlanService guaranteePlanService;
    /**
     * 跳转担保合同变更列表页面
     * 
     * @return
     */
    @GetMapping("/list")
    public String list() {
        return "cms/guarantee/listGuaExtContractChange";
    }
    /**
     * 担保合同变更列表
     * 
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        return guaExtContractChangeService.queryChangeList(input, this.getCurrentUser());
    }
    /**
     * 变更页面跳转
     * 
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/changeContractInfo/{id}")
    public String changeContractInfo(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            GuaContractInfoVO info = guaContractChangeService.queryContractInfoById(id);
            model.addAttribute("info", info);
            String guaWays = guaContractChangeService.setGuaWayS(id);
            if (!EmptyUtils.isBlank(guaWays)) {
                model.addAttribute("guaWays", guaWays);
            }
        }
        return "cms/guarantee/updateGuaExtContractChangeInfo";
    }
    /**
     * 保存担保合同信息
     */
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(GuaContractInfoVO contractVO) {
        this.handelData(contractVO);
        guaExtContractChangeService.tempOrSubmit(contractVO, this.getCurrentUser());
        guaranteePlanService.updateBillCodeByIds(contractVO.getPlanId(), contractVO.getId());
        return success();
    }
    private void handelData(GuaContractInfoVO contractVO) {
        // 去掉无效数据
        List<GuaContractWayVO> guaDtls = contractVO.getGuaDtls();
        if (EmptyUtils.isEmptyList(guaDtls)) {
            throw new ParameterException(GuaranteeMessageEnum.CMS_GUARANTEE_GUACONTRACT_GUAWAY_NULL);
        }
        List<GuaContractWayVO> wayList = new ArrayList<>();
        for (GuaContractWayVO guaWay : guaDtls) {
            if (guaWay.getMoney() == null) {
                continue;
            }
            if (null != guaWay.getWayCodes()) {
                wayList.add(guaWay);
            }
        }
        contractVO.setGuaDtls(wayList);
    }
}
