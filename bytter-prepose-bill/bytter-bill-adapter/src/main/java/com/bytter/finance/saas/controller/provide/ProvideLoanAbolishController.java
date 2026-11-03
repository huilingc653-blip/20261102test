package com.bytter.finance.saas.controller.provide;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
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
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 借款作废
 * @date 2017-07-20 10:45:09
 */
@Api(value = "/v1-preposebill/cms/provide/abolish",tags = "借款作废")
@RestController("v1ProvideLoanAbolishController")
@RequestMapping("/v1-preposebill/cms/provide/abolish")
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
     * 功能介绍 ：借款作废申请页面列表<br>
     * <b>日期：</b>2017年7月15日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @PostMapping("/tableCancellation")
    public AjaxJson<DataTablesOutput> tableCancellation(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input =dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsProvideLoanAbolishService.queryTable(input, getCurrentUser()));
    }
    /**
     * 功能介绍 ：借款作废查看页面<br>
     * <b>日期：</b>2017年7月15日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @GetMapping("/view")
    public AjaxJson<Map<String, Object>> view(String infoBillCode, Integer struts) {
        Map<String, Object> model = new HashMap<>();
        model.put("billCode", infoBillCode);
        CmsProvideLoanAbolish abolish = cmsProvideLoanAbolishService.getLoanAbolishByBillCode(infoBillCode);
        model.put("abolish", abolish);
        model.put("struts", struts);
        return AjaxJson.success(model);
    }
    /**
     * 功能介绍 ：借款作废申请页面<br>
     * <b>日期：</b>2017年7月15日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @GetMapping("/update")
    public AjaxJson<Map<String, Object>> update(String infoBillCode, String abolishId) {
        Map<String, Object> model = new HashMap<>();
        CmsProvideLoanInfo info = null;
        if (!EmptyUtils.isEmpty(abolishId) && !"null".equals(abolishId)) {
            CmsProvideLoanAbolish abolish = cmsProvideLoanAbolishService.get(abolishId);
            model.put("abolish", abolish);
        }
        info = loanInfoService.getLoanInfoByBillCode(infoBillCode);
        model.put("info", info);
        model.put("billCode", infoBillCode);
        model.put("date", new Date());
        return AjaxJson.success(model);
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
    public AjaxJson abolishSave(@RequestBody  CmsProvideLoanAbolish cmsProvideLoanAbolish) {
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
    public AjaxJson<Map<String, Object>> listCancellationStart() {
        Map<String, Object> model = new HashMap<>();
        String relateCon = configServiceImpl.getConfig(ConfigConstant.CMS, BaseDataConstant.BANK_RELEATE_CON)
                .getValue();// 是否对接合同管理
        model.put("relateCon", relateCon);
        return AjaxJson.success(model);
    }
    /**
     * 功能介绍 ：借款作废办理页面列表<br>
     * <b>日期：</b>2017年8月24日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @PostMapping("/tableCancellationStart")
    public AjaxJson<DataTablesOutput> tableCancellationStart(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input=dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsProvideLoanAbolishService.queryStartTable(input, getCurrentUser()));
    }
    /**
     * 功能介绍 ：借款作废办理<br>
     * <b>日期：</b>2017年8月25日<br>
     *
     * @version 1.0
     * @author zhengxn
     */
    @PostMapping("/handle/{id}")
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
    public AjaxJson<Map<String, Object>> listAbolishApprove(String flowStatus, String cycleTime ) {
        Map<String, Object> model = new HashMap<>();
        model.put("flowStatus", flowStatus);
        model.put("cycleTime", cycleTime);
        return AjaxJson.success(model);
    }
    /**
     * 借款作废审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/listDataAbolishApproveTable")
    public DataTablesOutput listDataAbolishApproveTable(@RequestBody DataTablesInput dataTablesInput) {
        return cmsProvideLoanAbolishService.getListDataAbolishApproveTable(dataTablesInput);
    }
    /**
     *
     */
    @GetMapping("/viewAbolishApprove")
    public AjaxJson<Map<String, Object>> viewAbolishApprove(String businessId, String taskId ) {
        Map<String, Object> model = new HashMap<>();
        if (!EmptyUtils.isEmpty(businessId)) {
            CmsProvideLoanAbolish abolish = cmsProvideLoanAbolishService.get(businessId);
            model.put("abolish", abolish);
        }
        model.put("taskId", taskId);// 必须要做此步骤
        return AjaxJson.success(model);
    }
    /**
     * 手机审批
     */
    @GetMapping("/mobileAbolishApprove")
    public AjaxJson<Map<String, Object>> mobileAbolishApprove(String businessId, String taskId ) {
        Map<String, Object> model = new HashMap<>();
        if (!EmptyUtils.isEmpty(businessId)) {
            CmsProvideLoanAbolish abolish = cmsProvideLoanAbolishService.get(businessId);
            model.put("abolish", abolish);
            LoanInfoVO infoVO = loanInfoService.getLoanInfoVOByBillCode(abolish.getPliCode());
            model.put("plMoneyUpper", StringUtils.chineseMoney(infoVO.getPlMoney()));
            model.put("loanInfo", infoVO);
            if (RationConstant.RAT_COMMON_Y.equals(infoVO.getRatmodel())) {
                model.put("urationDTO",
                        urationService.getRatUrationDTOByBillId(ConfigConstant.CMS, infoVO.getId()));// 授信明细
            }
        }
        model.put("gua",
                configServiceImpl.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_BANKLOAN_GUA));
        model.put("taskId", taskId);// 必须要做此步骤
        return AjaxJson.success(model);
    }
    /**
     * 查询合同状态
     */
    @PostMapping("/getConInfo")
    public AjaxJson<Integer> getConInfo(String conId, String busId) {
        Integer status = null;
        if (null != conId) {
            ConInfoDTO conInfoDTO = conCommonService.getConInfo(conId, busId);
            status = conInfoDTO.getStatus();
        }
        return AjaxJson.success(status);
    }
}
