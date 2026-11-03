package com.bytter.finance.saas.controller.boond;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.finance.domain.bond.service.CmsBondInterestService;
import com.bytter.finance.domain.bond.vo.CmsBondInterestVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author mof
 * @version 1.0
 * @Title: Controller
 * @Description: 债券利息预计明细表
 * @date 2022-06-08 10:07:46
 */
@Api(value = "/v1-preposebill/cms/bond/cmsbondinterest", tags = "债券利息预计明细")
@RestController("v1CmsBondInterestController")
@RequestMapping("/v1-preposebill/cms/bond/cmsbondinterest")
public class CmsBondInterestController extends BaseController {
    @Autowired
    private CmsBondInterestService cmsBondInterestService;

    /**
     * 债券还款计划模块加载利息明细
     *
     * @param registerId 债劵注册id
     * @return 利息明细列表
     */
    @ApiOperation("根据债券注册ID查询利息明细")
    @PostMapping("/listInterestByRegisterId")
    public AjaxJson<List<CmsBondInterestVo>> listInterestByRegisterId(String registerId) {
        List<CmsBondInterestVo> list = cmsBondInterestService.getByRegisterId(registerId);
        return AjaxJson.success(list);
    }
}