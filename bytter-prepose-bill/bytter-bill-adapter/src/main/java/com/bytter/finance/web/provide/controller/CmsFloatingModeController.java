package com.bytter.finance.web.provide.controller;
import com.bytter.finance.domain.basedata.service.LoanRateMaintenceService;
import com.bytter.finance.domain.provide.service.CmsProvideLoanSendService;
import com.bytter.finance.domain.provide.vo.LoanSendVO;
import com.bytter.system.api.dto.TbRateMaintenceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bytter.finance.domain.provide.service.CmsFloatingModeService;
import com.bytter.finance.domain.provide.vo.CmsFloatingModeVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
/**
 * @author songkk
 * @version 1.0
 * @Title: Controller
 * @Description: 浮动方式修改
 * @date 2017-08-09 16:40:48
 */
@Controller
@RequestMapping("/cms/provide/cmsFloatingMode")
public class CmsFloatingModeController extends BaseController {
    @Autowired
    private CmsFloatingModeService cmsFloatingModeService;
    @Autowired
    private CmsProvideLoanSendService cmsProvideLoanSendService;
    @Autowired
    private LoanRateMaintenceService loanRateMaintenceService;
    /**
     * 浮动方式 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询浮动方式修改列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model, String billCode, String id) {
        CmsFloatingModeVO cmsFloatingModeVO = cmsFloatingModeService.getCmsFloatingMode(id, billCode);
        LoanSendVO loanSendVo = cmsProvideLoanSendService.getLoanSendVOBySendBillCode(billCode);
        TbRateMaintenceDTO tbLoanRateMaintence = loanRateMaintenceService.getLoanRateMaintence(loanSendVo.getCurCode(),
                loanSendVo.getReferenceRateType(), loanSendVo.getRepayCycle());
        model.addAttribute("cmsFloatingModeVO", cmsFloatingModeVO);
        model.addAttribute("loanSend", loanSendVo);
        if (null != tbLoanRateMaintence) {
            model.addAttribute("tbLoanRateMaintence", tbLoanRateMaintence);
        }
        return "cms/provide/send/listCmsFloatingMode";
    }
    /**
     * 功能介绍 :浮动方式 查询页面table表<br>
     * <b>日期：</b>2017年7月24日<br>
     *
     * @version 1.0
     * @author songkk
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput, String id) {
        return cmsFloatingModeService.listcmsFloatingMode(dataTablesInput, id);
    }
    /**
     * 保存浮动方式
     *
     * @param rp
     * @return
     */
    @LogMessage(content = "添加CmsFloatingMode", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(CmsFloatingModeVO cmsFloatingModeVO) {
        cmsFloatingModeService.save(cmsFloatingModeVO, getCurrentUser());
        return success();
    }
}
