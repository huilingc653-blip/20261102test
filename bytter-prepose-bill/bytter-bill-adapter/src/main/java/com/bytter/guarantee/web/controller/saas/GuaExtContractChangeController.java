package com.bytter.guarantee.web.controller.saas;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.domain.service.GuaContractChangeService;
import com.bytter.guarantee.domain.service.GuaExtContractChangeService;
import com.bytter.guarantee.domain.service.GuaranteePlanService;
import com.bytter.guarantee.domain.vo.GuaContractInfoPageVO;
import com.bytter.guarantee.domain.vo.GuaContractInfoVO;
import com.bytter.guarantee.domain.vo.GuaContractWayVO;
import com.bytter.guarantee.domain.vo.GuaExtContractInfoPageVO;
import com.bytter.guarantee.enums.GuaranteeMessageEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "对外担保合同变更",value = "/v1-guarantee/cms/guarantee/guaExtContractChange")
@RestController("v1GuaExtContractChangeController")
@RequestMapping("/v1-guarantee/cms/guarantee/guaExtContractChange")
public class GuaExtContractChangeController extends BaseController {
    @Autowired
    private GuaExtContractChangeService guaExtContractChangeService;
    @Autowired
    private GuaContractChangeService guaContractChangeService;
    @Autowired
    private GuaranteePlanService guaranteePlanService;

    /**
     * 担保合同变更列表
     *
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation(value = "担保合同变更列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput input) {
        DataTablesInput dataTablesInput = input.convertAndInitColumns();
        return success(guaExtContractChangeService.queryChangeList(dataTablesInput, this.getCurrentUser()));
    }
    /**
     * 变更页面跳转
     *
     * @param id
     * @param model
     * @return
     */
    @ApiOperation(value = "担保合同变更页面跳转")
    @GetMapping("/changeContractInfo/{id}")
    public AjaxJson<GuaExtContractInfoPageVO> changeContractInfo(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            GuaContractInfoPageVO pageVO = new GuaContractInfoPageVO();
            GuaContractInfoVO info = guaContractChangeService.queryContractInfoById(id);
            pageVO.setInfo(info);
            String guaWays = guaContractChangeService.setGuaWayS(id);
            pageVO.setGuaWays(guaWays);
            return success(pageVO);
        }
        return success();
    }
    /**
     * 保存担保合同信息
     */
    @ApiOperation("保存担保合同信息")
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody GuaContractInfoVO contractVO) {
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
