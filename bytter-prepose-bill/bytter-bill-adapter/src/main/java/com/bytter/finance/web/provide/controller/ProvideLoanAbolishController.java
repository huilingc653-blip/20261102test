package com.bytter.finance.web.provide.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.credit.constant.RationConstant;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.CmsProcessMsgConstant;
import com.bytter.finance.constant.ProvideConstant;
import com.bytter.finance.domain.basedata.service.CmsProcessMsgService;
import com.bytter.finance.domain.provide.entity.CmsProvideLoanAbolish;
import com.bytter.finance.domain.provide.entity.CmsProvideLoanInfo;
import com.bytter.finance.domain.provide.service.CmsProvideLoanAbolishService;
import com.bytter.finance.domain.provide.service.LoanInfoService;
import com.bytter.finance.domain.provide.vo.LoanInfoVO;
import com.bytter.guarantee.api.IConService;
import com.bytter.guarantee.dto.ConInfoDTO;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.ConfigConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 借款作废
 * @date 2017-07-20 10:45:09
 */
@Controller
@RequestMapping("/cms/provide/abolish")
public class ProvideLoanAbolishController extends BaseController {
    @Autowired
    private CmsProvideLoanAbolishService cmsProvideLoanAbolishService;
    @Autowired
    private LoanInfoService loanInfoService;
    @Autowired
    private IRatUrationService urationService;
    @DubboReference(check = false)
    private IConfigService configServiceImpl;
    @Autowired
    private CmsProcessMsgService msService;
    @Resource
    private IConService conCommonService;
    /**
     * 功能介绍 ：借款作废申请跳转<br>
     * <b>日期：</b>2017年7月15日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @GetMapping("/listCancellation")
    public String listCancellation(Model model) {
        return "cms/provide/abolish/listCmsProvideLoanAbolish";
    }
    /**
     * 功能介绍 ：借款作废申请页面列表<br>
     * <b>日期：</b>2017年7月15日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @PostMapping("/tableCancellation")
    @ResponseBody
    public DataTablesOutput tableCancellation(@RequestBody DataTablesInput input) {
        return cmsProvideLoanAbolishService.queryTable(input, getCurrentUser());
    }
    /**
     * 功能介绍 ：借款作废查看页面<br>
     * <b>日期：</b>2017年7月15日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @GetMapping("/view")
    public String view(Model model, String infoBillCode, Integer struts) {
        model.addAttribute("billCode", infoBillCode);
        CmsProvideLoanAbolish abolish = cmsProvideLoanAbolishService.getLoanAbolishByBillCode(infoBillCode);
        model.addAttribute("abolish", abolish);
        model.addAttribute("struts", struts);
        return "cms/provide/abolish/viewCmsProvideLoanAbolish";
    }
    /**
     * 功能介绍 ：借款作废申请页面<br>
     * <b>日期：</b>2017年7月15日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @GetMapping("/update")
    public String update(Model model, String infoBillCode, String abolishId) {
        CmsProvideLoanInfo info = null;
        if (!EmptyUtils.isEmpty(abolishId) && !"null".equals(abolishId)) {
            CmsProvideLoanAbolish abolish = cmsProvideLoanAbolishService.get(abolishId);
            model.addAttribute("abolish", abolish);
        }
        info = loanInfoService.getLoanInfoByBillCode(infoBillCode);
        model.addAttribute("info", info);
        model.addAttribute("billCode", infoBillCode);
        model.addAttribute("date", new Date());
        return "cms/provide/abolish/addCmsProvideLoanAbolish";
    }
    /**
     * 功能介绍。借款作废保存<br>
     * <b>日期：</b>2017年7月26日<br>
     *
     * @param cmsProvideLoanAbolish
     *            0暂存 11提交审批 -1 打回 21补录审批 110业务结束
     * @version 1.0
     * @author zhengxn
     */
    @PostMapping("/abolishSave")
    @ResponseBody
    public AjaxJson abolishSave(CmsProvideLoanAbolish cmsProvideLoanAbolish) {
        cmsProvideLoanAbolishService.save(cmsProvideLoanAbolish, getCurrentUser());
        return success();
    }
    /**
     * 功能介绍。借款作废办理跳转<br>
     * <b>日期：</b>2017年7月26日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @GetMapping("/listCancellationStart")
    public String listCancellationStart(Model model) {
        String relateCon = configServiceImpl.getConfig(ConfigConstant.CMS, BaseDataConstant.BANK_RELEATE_CON)
                .getValue();// 是否对接合同管理
        model.addAttribute("relateCon", relateCon);
        return "cms/provide/abolish/listCmsProvideLoanAbolishStart";
    }
    /**
     * 功能介绍 ：借款作废办理页面列表<br>
     * <b>日期：</b>2017年8月24日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @PostMapping("/tableCancellationStart")
    @ResponseBody
    public DataTablesOutput tableCancellationStart(@RequestBody DataTablesInput input) {
        return cmsProvideLoanAbolishService.queryStartTable(input, getCurrentUser());
    }
    /**
     * 功能介绍 ：借款作废办理<br>
     * <b>日期：</b>2017年8月25日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @PostMapping("/handle/{id}")
    @ResponseBody
    public AjaxJson Handle(@PathVariable("id") String id) {
        cmsProvideLoanAbolishService.Handle(id, getCurrentUser());
        return success();
    }
    /**
     * 功能介绍 ：借款作废撤销<br>
     * <b>日期：</b>2017年8月25日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson Delete(@PathVariable("id") String id) {
        CmsProvideLoanAbolish abolish = cmsProvideLoanAbolishService.get(id);
        // 打回之后还原 info状态
        CmsProvideLoanInfo loan = loanInfoService.getLoanInfoByBillCode(abolish.getPliCode());
        if (abolish.getAbolishedStatus() != null) {
            loan.setStatus(abolish.getAbolishedStatus());
            loanInfoService.update(loan, getCurrentUser());
        }
        cmsProvideLoanAbolishService.deleteEntity(abolish, getUserId());
        msService.insertProcessMsg(abolish.getPliCode(), ProvideConstant.INFO_BILL_CODE_PREFIX,
                CmsProcessMsgConstant.MSGA_SUBMIT, CmsProcessMsgConstant.MSGA_ABOLISH_REPEAL, getCurrentUser());
        return success();
    }
    // ********************审批BEGIN*******************/
    /**
     * 借款作废审核列表
     **/
    @GetMapping("/listAbolishApprove")
    public String listAbolishApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/provide/abolish/approve/listAbolishApprove";
    }
    /**
     * 借款作废审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/listDataAbolishApproveTable")
    @ResponseBody
    public DataTablesOutput listDataAbolishApproveTable(@RequestBody DataTablesInput dataTablesInput) {
        return cmsProvideLoanAbolishService.getListDataAbolishApproveTable(dataTablesInput);
    }
    /**
     *
     */
    @GetMapping("/viewAbolishApprove")
    public String viewAbolishApprove(String businessId, String taskId, Model model) {
        if (!EmptyUtils.isEmpty(businessId)) {
            CmsProvideLoanAbolish abolish = cmsProvideLoanAbolishService.get(businessId);
            model.addAttribute("abolish", abolish);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/provide/abolish/approve/viewAbolishApprove";
    }
    /**
     * 手机审批
     */
    @GetMapping("/mobileAbolishApprove")
    public String mobileAbolishApprove(String businessId, String taskId, Model model) {
        if (!EmptyUtils.isEmpty(businessId)) {
            CmsProvideLoanAbolish abolish = cmsProvideLoanAbolishService.get(businessId);
            model.addAttribute("abolish", abolish);
            LoanInfoVO infoVO = loanInfoService.getLoanInfoVOByBillCode(abolish.getPliCode());
            model.addAttribute("plMoneyUpper", StringUtils.chineseMoney(infoVO.getPlMoney()));
            model.addAttribute("loanInfo", infoVO);
            if (RationConstant.RAT_COMMON_Y.equals(infoVO.getRatmodel())) {
                model.addAttribute("urationDTO",
                        urationService.getRatUrationDTOByBillId(ConfigConstant.CMS, infoVO.getId()));// 授信明细
            }
        }
        model.addAttribute("gua",
                configServiceImpl.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_BANKLOAN_GUA));
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/provide/abolish/approve/mobileAbolishApprove";
    }
    /**
     * 查询合同状态
     */
    @PostMapping("/getConInfo")
    @ResponseBody
    public Integer getConInfo(String conId, String busId) {
        Integer status = null;
        if (null != conId) {
            ConInfoDTO conInfoDTO = conCommonService.getConInfo(conId, busId);
            status = conInfoDTO.getStatus();
        }
        return status;
    }
}
