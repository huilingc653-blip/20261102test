package com.bytter.credit.web.controller.saas;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.domain.service.RatCpUrationService;
import com.bytter.credit.domain.vo.RatUrationBean;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.credit.dto.RatUrationDisDTO;
import com.bytter.credit.dto.UrationParamDTO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author luxy
 * @version 1.0
 * @Title:RatCpUrationController
 * @Description:内部授信结转
 * @date 2018年11月28日 下午3:51:31
 */
@Api(tags = "内部授信结转", value = "/v1-credit/cms/rat/ratcpuration")
@RestController("v1RatCpUrationController")
@RequestMapping({
        "/v1-credit/cms/rat/ratcpuration",
        "/v1-cms/cms/rat/ratcpuration",
        "/v1-preposebill/cms/rat/ratcpuration"
})
public class RatCpUrationController extends BaseController {
    @Autowired
    private RatCpUrationService ratCpUrationService;

    /**
     * 授信占用dataTable
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        Object beginDate = input.getFormdata().get("beginDate");
        if (!EmptyUtils.isEmpty(beginDate)) {
            input.addFilter(MatchTypeEnum.LE, "beginDate", beginDate.toString(), Date.class);// 大于等于授信开始日期
            input.addFilter(MatchTypeEnum.GE, "endDate", beginDate.toString(), Date.class);// 小于等于授信结束日期
        }
        return AjaxJson.success(ratCpUrationService.queryCpRatCanChangeList(input, RationConstant.CMS_CPRATION_CHANGE_APPLY,
                getUserId()));
    }

    /**
     * 检查授信是否可结转
     */
    @PostMapping("/checkChangeRation")
    public AjaxJson<List<RatUrationBean>> checkChangeRation(String urationIds) {
        return AjaxJson.success(ratCpUrationService.checkChangeRationCondition(urationIds));
    }

    /**
     * 同品种内部授信结转操作
     *
     * @return
     */
    @PostMapping("/rationChange")
    public AjaxJson rationChange(String urationIds, String joinFlag) {
        ratCpUrationService.changeSameRationCode(urationIds, joinFlag, getUserId());
        return success();
    }

    /**
     * 跳转到额度单选择页面
     */
    @GetMapping("/chooseration")
    public AjaxJson<List<RatUrationBean>> chooseration(String urationIds) {
        List<RatUrationBean> urationBeans = ratCpUrationService.queryChangeRation(urationIds);
        return AjaxJson.success(urationBeans);
    }

    /**
     * 授信选择页面
     */
    @LogMessage(content = "页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/toRationDtl")
    public AjaxJson<Map<String, Object>> toRationDtl(UrationParamDTO paramDTO, RatUrationDTO urationDTO) {
        Map<String, Object> model = new HashMap<>();
        paramDTO.setRatCode(null);
        paramDTO.setItemCode(urationDTO.getRatItemCode());
        List<RatUrationDisDTO> rations = ratCpUrationService.queryCorpDisRation(paramDTO, urationDTO);
        model.put("rations", rations);
        if (EmptyUtils.isEmptyList(rations)) {
            model.put("rationDtls", "0");// 无符合条件的授信合同
        } else {
            model.put("rationDtls", "1");
        }
        return AjaxJson.success(model);
    }

    /**
     * 查看已选授信单
     */
    @LogMessage(content = "页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewRationDtl")
    public AjaxJson<Map<String, Object>> viewRationDtl(RatUrationDTO urationDTO, String isRmbRat) {
        Map<String, Object> model = new HashMap<>();
        List<RatUrationDisDTO> rations = ratCpUrationService.viewCorpDisRation(urationDTO, isRmbRat);
        model.put("rations", rations);
        return AjaxJson.success(model);
    }

    @LogMessage(content = "页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/viewRationDtl")
    public AjaxJson<Map<String, Object>> viewRationDtlPost(@RequestBody RatUrationDTO urationDTO) {
        return viewRationDtl(urationDTO, urationDTO.getIsRmbRat());
    }
}
