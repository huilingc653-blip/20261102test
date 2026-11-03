package com.bytter.finance.saas.controller.boond;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.bond.service.*;
import com.bytter.finance.domain.bond.vo.CmsBondIssueInfoVo;
import com.bytter.finance.domain.bond.vo.CmsBondRegisterInfoVo;
import com.bytter.finance.domain.bond.vo.CmsBondRepayPlanVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mof
 * @version 1.0
 * @Title: Controller
 * @Description: 债券还款计划修改
 * @date 2022-06-09 10:07:46
 */
@Api(value = "v1-preposebill/cms/bond/updatecmsbondrepayplan", tags = "债券还款计划修改")
@RestController("v1UpdateCmsBondRepayPlanController")
@RequestMapping("/v1-preposebill/cms/bond/updatecmsbondrepayplan")
public class UpdateCmsBondRepayPlanController extends BaseController {
    @Autowired
    private CmsBondRepayPlanService cmsBondRepayPlanService;
    @Autowired
    private CmsBondIssueInfoService cmsBondIssueInfoService;
    @Autowired
    private CmsBondRepayPlanDtlService cmsBondRepayPlanDtlService;
    @Autowired
    private CmsBondRegisterInfoService cmsBondRegisterInfoService;
    @Autowired
    private UpdateCmsBondRepayPlanService updateCmsBondRepayPlanService;
    /**
     * 债券还款计划登记表新增页面跳转
     *
     * @return
     */
    @GetMapping("/update")
    public AjaxJson< Map<String,Object>> update(String registerId) {
        Map<String,Object> model=new HashMap<>();
        if (StringUtils.isNotEmpty(registerId)) {
            CmsBondRegisterInfoVo cmsBondRegisterInfoVo = cmsBondRegisterInfoService.getRegisterById(registerId);
            model.put("cmsBondRegisterInfoVo", cmsBondRegisterInfoVo);
            if (!EmptyUtils.isEmpty(cmsBondRegisterInfoVo)) {
                // 债券发行信息
                CmsBondIssueInfoVo cmsBondIssueInfoVo = cmsBondIssueInfoService.getByRegisterId(registerId);
                model.put("cmsBondIssueInfoVo", cmsBondIssueInfoVo);
                // 债券计划主表信息
                CmsBondRepayPlanVo planVO = cmsBondRepayPlanService.getByIssueId(cmsBondIssueInfoVo.getId());
                if (!EmptyUtils.isEmpty(cmsBondRegisterInfoVo)) {
                    // 暂时默认是 07 自定义，等后面开放其他还款方式再删除
                    planVO.setRepayType(CmsConstant.REPAY_TYPE_07);
                    model.put("cmsBondRepayPlanVo", planVO);
                }
            }
        }
        return AjaxJson.success(model);
    }

}
