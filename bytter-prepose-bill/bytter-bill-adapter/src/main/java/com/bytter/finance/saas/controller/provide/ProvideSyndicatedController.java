package com.bytter.finance.saas.controller.provide;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.finance.domain.basedata.dao.RepaymentScheduleDao;
import com.bytter.finance.domain.basedata.service.CmsRepaymentScheduleService;
import com.bytter.finance.domain.basedata.service.UpdateRepaymentScheduleService;
import com.bytter.finance.domain.provide.service.CmsProvideLoanSendService;
import com.bytter.finance.domain.provide.service.ProvideSyndicatedDtlService;
import com.bytter.finance.domain.provide.service.ProvideSyndicatedSendService;
import com.bytter.finance.domain.provide.service.ProvideSyndicatedService;
import com.bytter.finance.domain.provide.vo.ProvideSyndicatedDtlVo;
import com.bytter.finance.domain.provide.vo.ProvideSyndicatedSendVo;
import com.bytter.finance.domain.provide.vo.ProvideSyndicatedVo;
import com.bytter.core.base.BaseController;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 借款种类：银团相关
 * @date 2022-07-28 10:45:09
 */
@Api(value = "/v1-preposebill/cms/provide/providesyndicated",tags = "借款展期申请")
@RestController("v1ProvideSyndicatedController")
@RequestMapping("/v1-preposebill/cms/provide/providesyndicated")
public class ProvideSyndicatedController extends BaseController {
    @Autowired
    private RepaymentScheduleDao repaymentScheduleDao;
    @Autowired
    private CmsRepaymentScheduleService cmsRepaymentScheduleService;
    @Autowired
    private ProvideSyndicatedService provideSyndicatedService;
    @Autowired
    private ProvideSyndicatedSendService provideSyndicatedSendService;
    @Autowired
    private ProvideSyndicatedDtlService provideSyndicatedDtlService;
    @Autowired
    private CmsProvideLoanSendService cmsProvideLoanSendService;
    @Autowired
    private UpdateRepaymentScheduleService updateRepaymentScheduleService;
    /**
     * 加载银团借款明细
     *
     * @param directFinanceId
     * @return
     */
    @PostMapping("/listProvideSyndicated")
    @ResponseBody
    public AjaxJson<List<ProvideSyndicatedVo> > getByDirectFinanceId(String directFinanceId) {
        List<ProvideSyndicatedVo> list = provideSyndicatedService.getByDirectFinanceId(directFinanceId);
        return AjaxJson.success(list);
    }
    /**
     * 加载银团借款发放明细
     *
     * @param provideLoanSendId
     * @return
     */
    @PostMapping("/listProvideSyndicatedSend")
    @ResponseBody
    public AjaxJson<List<ProvideSyndicatedSendVo>> getSendByProvideLoanSendId(String provideLoanSendId) {
        List<ProvideSyndicatedSendVo> list = provideSyndicatedSendService.getSendByProvideLoanSendId(provideLoanSendId);
        return AjaxJson.success(list);
    }
    /**
     * 加载银团借款还款明细
     *
     * @param repayId
     * @return
     */
    @PostMapping("/listProvideSyndicatedDtl")
    @ResponseBody
    public AjaxJson<List<ProvideSyndicatedDtlVo> > getDtlByDirectFinanceId(String repayId) {
        List<ProvideSyndicatedDtlVo> list = provideSyndicatedDtlService.getByRepayId(repayId);
        return AjaxJson.success(list);
    }
    /**
     * 跳转到还款计划添加银团页面（可以修改数据:其他）
     *
     * @param provideSyndicatedDtlVo
     * @param model
     * @return
     */
    @GetMapping("/toProvideSyndicatedDtl")
    public AjaxJson<Map<String,Object>> toProvideSyndicatedDtl(ProvideSyndicatedDtlVo provideSyndicatedDtlVo) {
        Map<String,Object> model=new HashMap<>();
        model.put("provideSyndicatedDtlVo", provideSyndicatedDtlVo);
        return AjaxJson.success(model);
    }
}
