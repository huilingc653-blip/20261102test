package com.bytter.credit.web.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;
/**
 * 
 * @author luxy
 * @version 1.0
 * @Title:RatCpUrationController
 * @Description:内部授信结转
 * @date 2018年11月28日 下午3:51:31
 */
@Controller
@RequestMapping("/cms/rat/ratcpuration")
public class RatCpUrationController extends BaseController {
    @Autowired
    private RatCpUrationService ratCpUrationService;
    /**
     * 内部授信占用列表页面
     */
    @GetMapping("/list")
    public String list(Model model) {
        return "cms/rat/cpration/listRatCpUration";
    }
    /**
     * 授信占用dataTable
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        Object beginDate = input.getFormdata().get("beginDate");
        if (!EmptyUtils.isEmpty(beginDate)) {
            input.addFilter(MatchTypeEnum.LE, "beginDate", beginDate.toString(), Date.class);// 大于等于授信开始日期
            input.addFilter(MatchTypeEnum.GE, "endDate", beginDate.toString(), Date.class);// 小于等于授信结束日期
        }
        return ratCpUrationService.queryCpRatCanChangeList(input, RationConstant.CMS_CPRATION_CHANGE_APPLY,
                getUserId());
    }
    /**
     * 检查授信是否可结转
     */
    @PostMapping("/checkChangeRation")
    @ResponseBody
    public List<RatUrationBean> checkChangeRation(String urationIds) {
        return ratCpUrationService.checkChangeRationCondition(urationIds);
    }
    /**
     * 同品种内部授信结转操作
     * 
     * @param ratUration
     * @return
     */
    @PostMapping("/rationChange")
    @ResponseBody
    public AjaxJson rationChange(String urationIds, String joinFlag) {
        ratCpUrationService.changeSameRationCode(urationIds, joinFlag, getUserId());
        return success();
    }
    /**
     * 跳转到额度单选择页面
     */
    @GetMapping("/chooseration")
    public String chooseration(String urationIds, Model model) {
        List<RatUrationBean> urationBeans = ratCpUrationService.queryChangeRation(urationIds);
        model.addAttribute("urationBeans", urationBeans);
        return "cms/rat/cpration/ratCpUrationChoose";
    }
    /**
     * 授信选择页面
     */
    @LogMessage(content = "页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/toRationDtl")
    public String toRationDtl(UrationParamDTO paramDTO, RatUrationDTO urationDTO, Model model) {
        paramDTO.setRatCode(null);
        paramDTO.setItemCode(urationDTO.getRatItemCode());
        List<RatUrationDisDTO> rations = ratCpUrationService.queryCorpDisRation(paramDTO, urationDTO);
        model.addAttribute("rations", rations);
        if (EmptyUtils.isEmptyList(rations)) {
            model.addAttribute("rationDtls", "0");// 无符合条件的授信合同
        } else {
            model.addAttribute("rationDtls", "1");
        }
        return "cms/rat/cpration/ratCpUrationSelect";
    }
    /**
     * 查看已选授信单
     */
    @LogMessage(content = "页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewRationDtl")
    public String viewRationDtl(RatUrationDTO urationDTO, String isRmbRat, Model model) {
        List<RatUrationDisDTO> rations = ratCpUrationService.viewCorpDisRation(urationDTO, isRmbRat);
        model.addAttribute("rations", rations);
        return "cms/rat/cpration/ratCpUrationDetail";
    }
}
